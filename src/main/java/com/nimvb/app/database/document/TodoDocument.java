package com.nimvb.app.database.document;

import com.nimvb.app.database.exception.KeyAlreadyExistsException;
import com.nimvb.app.database.exception.KeyNotFoundException;
import com.nimvb.app.database.model.Todo;
import com.nimvb.app.database.sequence.ValueGenerator;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TodoDocument implements Document<Todo,Integer> {
    private final Map<Integer, Todo>      TODO_STORAGE = new HashMap<>();
    private final ValueGenerator<Integer> todoSequence;

    @Override
    public Todo insert(Todo todo) throws KeyAlreadyExistsException {
        return insert(todo,true);
    }

    @Override
    public Todo insert(Todo todo, boolean wrap) throws KeyAlreadyExistsException {
        Todo resource = new Todo();
        if(todo.getId() != null) {
            Todo target = lookup(todo.getId());
            if (target != null) {
                throw new KeyAlreadyExistsException();
            }
        }
        resource.setId(todoSequence.next());
        resource.setName(todo.getName());
        resource.setCreationTimestamp(todo.getCreationTimestamp());
        resource.setItems(todo.getItems());
        TODO_STORAGE.put(resource.getId(),resource);
        if(wrap) {
            return new Todo() {{
                setId(resource.getId());
                setItems(resource.getItems());
                setName(resource.getName());
                setCreationTimestamp(resource.getCreationTimestamp());
            }};
        }
        return resource;
    }

    @Override
    public Todo update(Integer key, Todo todo) throws KeyNotFoundException {
        return update(key, todo,true);
    }

    @Override
    public Todo update(Integer key, Todo todo, boolean wrap) throws KeyNotFoundException {
        Todo target = lookup(key);
        if(target == null){
            throw new KeyNotFoundException();
        }
        target.setName(todo.getName());
        target.setCreationTimestamp(todo.getCreationTimestamp());
        target.setItems(todo.getItems());
        TODO_STORAGE.put(key, target);
        if(wrap) {
            return new Todo() {{
                setId(target.getId());
                setName(target.getName());
                setCreationTimestamp(target.getCreationTimestamp());
                setItems(target.getItems());
            }};
        }
        return target;
    }

    @Override
    public Todo find(Integer key) {
        final Todo todo = TODO_STORAGE.getOrDefault(key, null);
        if(todo != null){
            return new Todo(){{
                setId(todo.getId());
                setItems(todo.getItems());
                setName(todo.getName());
                setCreationTimestamp(todo.getCreationTimestamp());
            }};
        }
        return todo;
    }

    @Override
    public Todo fetch(Integer key) {
        return TODO_STORAGE.getOrDefault(key,null);
    }

    @Override
    public Collection<Todo> all() {
        return TODO_STORAGE.values().stream().map(todo -> new Todo(){{
            setId(todo.getId());
            setName(todo.getName());
            setCreationTimestamp(todo.getCreationTimestamp());
            setItems(todo.getItems());
        }}).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer key) {
        TODO_STORAGE.remove(key);
    }

    @Override
    public Integer count() {
        return TODO_STORAGE.size();
    }

    private Todo lookup(Integer key){
        return TODO_STORAGE.getOrDefault(key,null);
    }
}
