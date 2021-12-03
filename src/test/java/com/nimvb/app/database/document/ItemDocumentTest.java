package com.nimvb.app.database.document;

import com.nimvb.app.database.exception.KeyAlreadyExistsException;
import com.nimvb.app.database.exception.KeyNotFoundException;
import com.nimvb.app.database.model.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

class ItemDocumentTest {

    private static final Integer[] KEYS = new Integer[]{
            1,
            2,
            3,
            4
    };
    private ItemDocument           itemDocument;
    private Integer                keyIndex = 0;

    @BeforeEach
    void setUp() {
        itemDocument = new ItemDocument(() -> KEYS[keyIndex++ % KEYS.length]);
    }

    @Test
    void insert() {
        var i1 = new Item(){{
            setId(1);
            setTitle("i1");
            setDescription("i1d1");
            setCreationTimestamp(Instant.now().toEpochMilli());
            setDeadlineTimestamp(Instant.now().plusSeconds(5).toEpochMilli());
            setCompleted(false);
        }};
        var i2 = new Item(){{
            setId(i1.getId());
            setTitle("i2");
            setDescription("i2d2");
            setCreationTimestamp(Instant.now().toEpochMilli());
            setDeadlineTimestamp(Instant.now().plusSeconds(5).toEpochMilli());
            setCompleted(true);
        }};
        Item i3 = new Item(){{
            setTitle("i3");
            setDescription("i3d3");
            setCreationTimestamp(Instant.now().toEpochMilli());
            setDeadlineTimestamp(Instant.now().plusSeconds(5).toEpochMilli());
            setCompleted(true);
        }};
        var i1Inserted = itemDocument.insert(i1);
        i3 = itemDocument.insert(i3);
        Assertions.assertThatThrownBy(() -> {
            itemDocument.insert(i2);
        }).isInstanceOf(KeyAlreadyExistsException.class);
        Assertions.assertThat(i1Inserted).isNotSameAs(i1);
        Assertions.assertThat(i1Inserted.isCompleted()).isEqualTo(i1.isCompleted());
        Assertions.assertThat(i1Inserted.getCreationTimestamp()).isEqualTo(i1.getCreationTimestamp());
        Assertions.assertThat(i1Inserted.getDeadlineTimestamp()).isEqualTo(i1.getDeadlineTimestamp());
        Assertions.assertThat(i1Inserted.getTitle()).isEqualTo(i1.getTitle());
        Assertions.assertThat(i1Inserted.getDescription()).isEqualTo(i1.getDescription());
        Assertions.assertThat(i1Inserted.getId()).isEqualTo(i1.getId());
        Assertions.assertThat(itemDocument.count()).isEqualTo(2);
        Assertions.assertThat(i3.getId()).isEqualTo(KEYS[1]);
    }

    @Test
    void update() {
        final long i1CreationTimestamp = Instant.now().toEpochMilli();
        final long i1DeadlineTimestamp = Instant.ofEpochMilli(i1CreationTimestamp).plusSeconds(100).toEpochMilli();
        final long i1UpdateCreationTimestamp = Instant.now().plusSeconds(10).toEpochMilli();
        final long i1UpdateDeadlineTimestamp = Instant.ofEpochMilli(i1UpdateCreationTimestamp).plusSeconds(10).toEpochMilli();
        var i1 = new Item(){{
            setId(1);
            setTitle("i1");
            setDescription("i1d1");
            setCreationTimestamp(i1CreationTimestamp);
            setDeadlineTimestamp(i1DeadlineTimestamp);
            setCompleted(false);
        }};
        var i1updated = new Item(){{

            setId(2);
            setTitle("i1updated");
            setDescription("i1updatedd1");
            setCreationTimestamp(i1UpdateCreationTimestamp);
            setDeadlineTimestamp(i1UpdateDeadlineTimestamp);
            setCompleted(true);
        }};
        Assertions.assertThatThrownBy(() -> {
            itemDocument.update(i1.getId(),i1);
        }).isInstanceOf(KeyNotFoundException.class);
        var i1Inserted = itemDocument.insert(i1);
        var i1Updated = itemDocument.update(i1.getId(),i1updated);
        Assertions.assertThat(itemDocument.count()).isEqualTo(1);
        Assertions.assertThat(i1Inserted.getId()).isEqualTo(i1.getId());
        Assertions.assertThat(i1Inserted.getTitle()).isEqualTo(i1.getTitle());
        Assertions.assertThat(i1Inserted.getCreationTimestamp()).isEqualTo(i1.getCreationTimestamp());
        Assertions.assertThat(i1Updated.getId()).isEqualTo(i1Inserted.getId());
        Assertions.assertThat(i1Updated.getTitle()).isEqualTo("i1updated");
        Assertions.assertThat(i1Updated.getCreationTimestamp()).isEqualTo(i1UpdateCreationTimestamp);
        Assertions.assertThat(i1Updated).isNotSameAs(i1updated);
        Assertions.assertThat(i1Updated).isNotSameAs(i1Inserted);
    }

    @Test
    void find() {
        Assertions.assertThat(itemDocument.count()).isEqualTo(0);
        var todo = itemDocument.find(1);
        Assertions.assertThat(todo).isNull();
        var record = itemDocument.insert(new Item(){{
            setTitle("i1");
            setDescription("i1d1");
            setCreationTimestamp(Instant.now().toEpochMilli());
            setDeadlineTimestamp(Instant.now().plusSeconds(5).toEpochMilli());
            setCompleted(false);
        }});
        var target = itemDocument.find(record.getId());
        Assertions.assertThat(target).isNotNull();
        Assertions.assertThat(target).isNotSameAs(record);
        Assertions.assertThat(target).isEqualTo(record);
    }

    @Test
    void all() {
        Assertions.assertThat(itemDocument.all()).isNotNull();
        Assertions.assertThat(itemDocument.all()).isEmpty();
        Assertions.assertThat(itemDocument.all()).hasSize(0);
        var record = itemDocument.insert(new Item(){{
            setTitle("i1");
            setDescription("i1d1");
            setCreationTimestamp(Instant.now().toEpochMilli());
            setDeadlineTimestamp(Instant.now().plusSeconds(5).toEpochMilli());
            setCompleted(false);
        }});
        Assertions.assertThat(itemDocument.all()).isNotNull();
        Assertions.assertThat(itemDocument.all()).hasSize(1);
    }

    @Test
    void delete() {
        Assertions.assertThat(itemDocument.count()).isEqualTo(0);
        itemDocument.delete(1);
        Assertions.assertThat(itemDocument.count()).isEqualTo(0);
        var record = itemDocument.insert(new Item(){{
            setTitle("i1");
            setDescription("i1d1");
            setCreationTimestamp(Instant.now().toEpochMilli());
            setDeadlineTimestamp(Instant.now().plusSeconds(5).toEpochMilli());
            setCompleted(false);
        }});
        Assertions.assertThat(itemDocument.count()).isEqualTo(1);
        itemDocument.delete(record.getId());
        Assertions.assertThat(itemDocument.count()).isEqualTo(0);
    }

    @Test
    void count() {
    }
}