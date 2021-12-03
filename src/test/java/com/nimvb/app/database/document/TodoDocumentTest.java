package com.nimvb.app.database.document;

import com.nimvb.app.database.exception.KeyAlreadyExistsException;
import com.nimvb.app.database.exception.KeyNotFoundException;
import com.nimvb.app.database.model.Todo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;


class TodoDocumentTest {

    private static final Integer[] KEYS = new Integer[]{
            1,
            2,
            3,
            4
    };
    private TodoDocument todoDocument;
    private Integer keyIndex = 0;

    @BeforeEach
    void setUp() {
        todoDocument = new TodoDocument(() -> KEYS[keyIndex++ % KEYS.length]);
    }

    @Test
    void insert() {
        var t1 = new Todo(){{
            setId(1);
            setName("t1");
            setCreationTimestamp(Instant.now().toEpochMilli());
        }};
        var t2 = new Todo(){{setId(t1.getId());setName("b2");setCreationTimestamp(Instant.now().toEpochMilli());}};
        Todo t3 = new Todo(){{setName("b3");setCreationTimestamp(Instant.now().toEpochMilli());}};
        var t1Inserted = todoDocument.insert(t1);
        t3 = todoDocument.insert(t3);
        Assertions.assertThatThrownBy(() -> {
            todoDocument.insert(t2);
        }).isInstanceOf(KeyAlreadyExistsException.class);
        Assertions.assertThat(t1Inserted).isNotSameAs(t1);
        Assertions.assertThat(todoDocument.count()).isEqualTo(2);
        Assertions.assertThat(t3.getId()).isEqualTo(KEYS[1]);
    }

    @Test
    void update() {
        final long t1CreationTimestamp = Instant.now().toEpochMilli();
        final long t1UpdateCreationTimestamp = Instant.now().plusSeconds(10).toEpochMilli();
        var t1 = new Todo(){{setId(1);setName("t1");setCreationTimestamp(t1CreationTimestamp);}};
        var t1updated = new Todo(){{setId(2);setName("t1updated");setCreationTimestamp(t1UpdateCreationTimestamp);}};
        Assertions.assertThatThrownBy(() -> {
            todoDocument.update(t1.getId(),t1);
        }).isInstanceOf(KeyNotFoundException.class);
        var t1Inserted = todoDocument.insert(t1);
        var t1Updated = todoDocument.update(t1.getId(),t1updated);
        Assertions.assertThat(todoDocument.count()).isEqualTo(1);
        Assertions.assertThat(t1Inserted.getId()).isEqualTo(t1.getId());
        Assertions.assertThat(t1Inserted.getName()).isEqualTo(t1.getName());
        Assertions.assertThat(t1Inserted.getCreationTimestamp()).isEqualTo(t1.getCreationTimestamp());
        Assertions.assertThat(t1Updated.getId()).isEqualTo(t1Inserted.getId());
        Assertions.assertThat(t1Updated.getName()).isEqualTo("t1updated");
        Assertions.assertThat(t1Updated.getCreationTimestamp()).isEqualTo(t1UpdateCreationTimestamp);
        Assertions.assertThat(t1Updated).isNotSameAs(t1updated);
        Assertions.assertThat(t1Updated).isNotSameAs(t1Inserted);
    }

    @Test
    void find() {
        Assertions.assertThat(todoDocument.count()).isEqualTo(0);
        final Todo todo = todoDocument.find(1);
        Assertions.assertThat(todo).isNull();
        var record = todoDocument.insert(new Todo(){{
            setName("b1");
            setCreationTimestamp(Instant.now().toEpochMilli());
        }});
        final Todo target = todoDocument.find(record.getId());
        Assertions.assertThat(target).isNotNull();
        Assertions.assertThat(target).isNotSameAs(record);
        Assertions.assertThat(target).isEqualTo(record);
    }

    @Test
    void all() {
        Assertions.assertThat(todoDocument.all()).isNotNull();
        Assertions.assertThat(todoDocument.all()).isEmpty();
        Assertions.assertThat(todoDocument.all()).hasSize(0);
        var record = todoDocument.insert(new Todo(){{
            setName("b1");
            setCreationTimestamp(Instant.now().toEpochMilli());
        }});
        Assertions.assertThat(todoDocument.all()).isNotNull();
        Assertions.assertThat(todoDocument.all()).hasSize(1);
    }

    @Test
    void delete() {
        Assertions.assertThat(todoDocument.count()).isEqualTo(0);
        todoDocument.delete(1);
        Assertions.assertThat(todoDocument.count()).isEqualTo(0);
        var record = todoDocument.insert(new Todo(){{
            setName("b1");
            setCreationTimestamp(Instant.now().toEpochMilli());
        }});
        Assertions.assertThat(todoDocument.count()).isEqualTo(1);
        todoDocument.delete(record.getId());
        Assertions.assertThat(todoDocument.count()).isEqualTo(0);
    }

    @Test
    void count() {
    }
}