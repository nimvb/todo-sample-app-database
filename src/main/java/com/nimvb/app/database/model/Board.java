package com.nimvb.app.database.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Board {
    private String id;
    private String name;
    private String color;
    private List<Todo> todos = new ArrayList<>();
}
