package com.nimvb.app.database.document;

import com.nimvb.app.database.exception.KeyAlreadyExistsException;
import com.nimvb.app.database.exception.KeyNotFoundException;
import com.nimvb.app.database.model.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

class BoardDocumentTest {


    private static final UUID[] KEYS = new UUID[]{
            UUID.fromString("01576405-cb97-4ec2-b704-1112f59f3142"),
            UUID.fromString("6b6007ae-02fe-4443-ac72-4788721e3e04"),
            UUID.fromString("73528492-668a-4a4e-b461-b62c5ac488b9"),
            UUID.fromString("92fa2935-aa8b-4753-b6a8-c658e915f29e")
    };

    private BoardDocument boardDocument;
    private Integer keyIndex = 0;
    MockedStatic<UUID> uuidMockedStatic;


    @BeforeEach
    void init() {
        uuidMockedStatic = Mockito.mockStatic(UUID.class);
        uuidMockedStatic.when(UUID::randomUUID).thenAnswer(invocation -> KEYS[keyIndex++ % KEYS.length]);
        boardDocument = new BoardDocument(UUID::randomUUID);
    }

    @AfterEach
    void destroy(){
        uuidMockedStatic.close();
    }




    @Test
    void insert() {
        var b1 = new Board(){{setId(UUID.randomUUID().toString());setName("b1");setColor("c1");}};
        var b2 = new Board(){{setId(b1.getId());setName("b2");setColor("c2");}};
        Board b3 = new Board(){{setName("b3");setColor("c3");}};
        var b1Inserted = boardDocument.insert(b1);
        b3 = boardDocument.insert(b3);
        Assertions.assertThatThrownBy(() -> {
            boardDocument.insert(b2);
        }).isInstanceOf(KeyAlreadyExistsException.class);
        Assertions.assertThat(b1Inserted).isNotSameAs(b1);
        Assertions.assertThat(boardDocument.count()).isEqualTo(2);
        Assertions.assertThat(b3.getId()).isEqualTo(KEYS[1].toString());
    }

    @Test
    void update() {
        var b1 = new Board(){{setId(UUID.randomUUID().toString());setName("b1");setColor("c1");}};
        var b1Update = new Board(){{setId(UUID.randomUUID().toString());setName("b1updated");setColor("c1updated");}};
        Assertions.assertThatThrownBy(() -> {
            boardDocument.update(b1.getId(),b1);
        }).isInstanceOf(KeyNotFoundException.class);
        var b1Inserted = boardDocument.insert(b1);
        var b1Updated = boardDocument.update(b1.getId(),b1Update);
        Assertions.assertThat(boardDocument.count()).isEqualTo(1);
        Assertions.assertThat(b1Inserted.getId()).isEqualTo(b1.getId());
        Assertions.assertThat(b1Inserted.getName()).isEqualTo(b1.getName());
        Assertions.assertThat(b1Inserted.getColor()).isEqualTo(b1.getColor());
        Assertions.assertThat(b1Updated.getId()).isEqualTo(b1Inserted.getId());
        Assertions.assertThat(b1Updated.getName()).isEqualTo("b1updated");
        Assertions.assertThat(b1Updated.getColor()).isEqualTo("c1updated");
        Assertions.assertThat(b1Updated).isNotSameAs(b1Update);
        Assertions.assertThat(b1Updated).isNotSameAs(b1Inserted);
    }

    @Test
    void delete() {
        Assertions.assertThat(boardDocument.count()).isEqualTo(0);
        boardDocument.delete("1");
        Assertions.assertThat(boardDocument.count()).isEqualTo(0);
        var record = boardDocument.insert(new Board(){{
            setName("b1");
            setColor("c1");
        }});
        Assertions.assertThat(boardDocument.count()).isEqualTo(1);
        boardDocument.delete(record.getId());
        Assertions.assertThat(boardDocument.count()).isEqualTo(0);
    }

    @Test
    void find() {
        Assertions.assertThat(boardDocument.count()).isEqualTo(0);
        final Board board = boardDocument.find("id");
        Assertions.assertThat(board).isNull();
        var record = boardDocument.insert(new Board(){{
            setName("b1");
            setColor("c1");
        }});
        final Board target = boardDocument.find(record.getId());
        Assertions.assertThat(target).isNotNull();
        Assertions.assertThat(target).isNotSameAs(record);
        Assertions.assertThat(target).isEqualTo(record);
    }

    @Test
    void all() {
        Assertions.assertThat(boardDocument.all()).isNotNull();
        Assertions.assertThat(boardDocument.all()).isEmpty();
        Assertions.assertThat(boardDocument.all()).hasSize(0);
        var record = boardDocument.insert(new Board(){{
            setName("b1");
            setColor("c1");
        }});
        Assertions.assertThat(boardDocument.all()).isNotNull();
        Assertions.assertThat(boardDocument.all()).hasSize(1);
    }
}