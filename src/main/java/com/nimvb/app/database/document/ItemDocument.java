package com.nimvb.app.database.document;

import com.nimvb.app.database.exception.KeyAlreadyExistsException;
import com.nimvb.app.database.exception.KeyNotFoundException;
import com.nimvb.app.database.model.Item;
import com.nimvb.app.database.sequence.ValueGenerator;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ItemDocument implements Document<Item, Integer> {

    private final Map<Integer, Item> ITEM_STORAGE = new HashMap<>();
    private final ValueGenerator<Integer> itemSequence;

    @Override
    public Item insert(Item item) throws KeyAlreadyExistsException {
        return insert(item, true);
    }

    @Override
    public Item insert(Item item, boolean wrap) throws KeyAlreadyExistsException {
        Item resource = new Item();
        if (item.getId() != null) {
            Item target = lookup(item.getId());
            if (target != null) {
                throw new KeyAlreadyExistsException();
            }
        }
        resource.setId(itemSequence.next());
        resource.setTitle(item.getTitle());
        resource.setDescription(item.getDescription());
        resource.setCreationTimestamp(item.getCreationTimestamp());
        resource.setDeadlineTimestamp(item.getDeadlineTimestamp());
        resource.setCompleted(item.isCompleted());
        ITEM_STORAGE.put(resource.getId(), resource);
        if (wrap) {
            return new Item() {{
                setId(resource.getId());
                setDeadlineTimestamp(resource.getDeadlineTimestamp());
                setCreationTimestamp(resource.getCreationTimestamp());
                setTitle(resource.getTitle());
                setDescription(resource.getDescription());
                setCompleted(resource.isCompleted());
            }};
        }
        return resource;
    }

    @Override
    public Item update(Integer key, Item item) throws KeyNotFoundException {
        return update(key, item, true);
    }

    @Override
    public Item update(Integer key, Item item, boolean wrap) throws KeyNotFoundException {
        Item target = lookup(key);
        if (target == null) {
            throw new KeyNotFoundException();
        }
        target.setTitle(item.getTitle());
        target.setDescription(item.getDescription());
        target.setCreationTimestamp(item.getCreationTimestamp());
        target.setDeadlineTimestamp(item.getDeadlineTimestamp());
        target.setCompleted(item.isCompleted());
        ITEM_STORAGE.put(key, target);
        if (wrap) {
            return new Item() {{
                setId(target.getId());
                setTitle(target.getTitle());
                setDescription(target.getDescription());
                setCreationTimestamp(target.getCreationTimestamp());
                setDeadlineTimestamp(target.getDeadlineTimestamp());
                setCompleted(target.isCompleted());
            }};
        }
        return target;
    }

    @Override
    public Item find(Integer key) {
        final Item item = ITEM_STORAGE.getOrDefault(key, null);
        if (item != null) {
            return new Item() {{
                setId(item.getId());
                setTitle(item.getTitle());
                setDescription(item.getDescription());
                setCreationTimestamp(item.getCreationTimestamp());
                setDeadlineTimestamp(item.getDeadlineTimestamp());
                setCompleted(item.isCompleted());
            }};
        }
        return item;
    }

    @Override
    public Item fetch(Integer key) {
        return ITEM_STORAGE.getOrDefault(key, null);
    }

    @Override
    public Collection<Item> all() {
        return ITEM_STORAGE.values().stream().map(item -> new Item() {{
            setId(item.getId());
            setTitle(item.getTitle());
            setDescription(item.getDescription());
            setCreationTimestamp(item.getCreationTimestamp());
            setDeadlineTimestamp(item.getDeadlineTimestamp());
            setCompleted(item.isCompleted());
        }}).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer key) {
        ITEM_STORAGE.remove(key);
    }

    @Override
    public Integer count() {
        return ITEM_STORAGE.size();
    }

    private Item lookup(Integer key) {
        return ITEM_STORAGE.getOrDefault(key, null);
    }
}
