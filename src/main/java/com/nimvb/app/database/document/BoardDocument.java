package com.nimvb.app.database.document;

import com.nimvb.app.database.exception.KeyAlreadyExistsException;
import com.nimvb.app.database.exception.KeyNotFoundException;
import com.nimvb.app.database.model.Board;
import com.nimvb.app.database.model.Todo;
import com.nimvb.app.database.sequence.ValueGenerator;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class BoardDocument implements Document<Board,String> {

    private final Map<String, Board>   BOARDS_STORAGE = new HashMap<>();
    private final ValueGenerator<UUID> sequentialGuidGenerator;


    @Override
    public Board insert(Board board) {
        return insert(board,true);
    }

    @Override
    public Board insert(Board board, boolean wrap) throws KeyAlreadyExistsException {
        Objects.requireNonNull(board);
        final Board domain = insert(board.getId(), board.getName(), board.getColor());
        if(wrap) {
            return new Board() {{
                setId(domain.getId());
                setName(domain.getName());
                setColor(domain.getColor());
                setTodos(domain.getTodos());
            }};
        }
        return domain;
    }

    @Override
    public Board update(String key, Board board) {
        return update(key, board,true);
    }

    @Override
    public Board update(String key, Board board, boolean wrap) throws KeyNotFoundException {
        Objects.requireNonNull(key);
        Objects.requireNonNull(board);
        final Board domain = update(key, board.getName(), board.getColor(), board.getTodos());
        if(wrap) {
            return (new Board() {{
                setId(domain.getId());
                setName(domain.getName());
                setColor(domain.getColor());
                setTodos(domain.getTodos());
            }});
        }
        return domain;
    }


    @Override
    public void delete(String id){
        Objects.requireNonNull(id);
        BOARDS_STORAGE.remove(id);
    }

    @Override
    public Integer count() {
        return BOARDS_STORAGE.values().size();
    }

    @Override
    public Board find(String id){
        final Board board = lookup(id);
        if(board != null) {
            return new Board() {{
                setId(board.getId());
                setName(board.getName());
                setColor(board.getColor());
                setTodos(board.getTodos());
            }};
        }
        return null;
    }

    @Override
    public Board fetch(String key) {
        return lookup(key);
    }

    @Override
    public Collection<Board> all() {
        return BOARDS_STORAGE.values().stream().map(board -> new Board(){{
            setId(board.getId());
            setName(board.getName());
            setColor(board.getColor());
            setTodos(board.getTodos());
        }}).collect(Collectors.toList());
    }


    private Board insert(String name,String color) {
        return insert(sequentialGuidGenerator.next().toString(),name,color);
    }

    private Board insert(String key,String name,String color){
        if(key == null){
            return insert(name,color);
        }
        if(lookup(key) != null){
            throw new KeyAlreadyExistsException();
        }
        var board = new Board();
        board.setName(name);
        board.setColor(color);
        board.setId(key);
        BOARDS_STORAGE.put(board.getId(),board);
        return board;
    }

    private Board update(String id, String name, String color, List<Todo> todos){
        var board = lookup(id);
        if(board == null){
            throw new KeyNotFoundException();
        }
        board.setName(name);
        board.setColor(color);
        board.setTodos(todos);
        save(board.getId(), board);
        return board;
    }

    private Board lookup(String key){
        return BOARDS_STORAGE.getOrDefault(key,null);
    }

    private void save(String key, Board board){
        Objects.requireNonNull(key);
        Objects.requireNonNull(board);
        BOARDS_STORAGE.put(key, board);
    }

}

