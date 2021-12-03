package com.nimvb.app.database.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Todo {
    private Integer id;
    private String name;
    private Long creationTimestamp;
    private List<Item> items = new ArrayList<>();
}
