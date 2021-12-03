package com.nimvb.app.database.model;

import lombok.Data;

@Data
public class Item {
    private Integer id;
    private String title;
    private String description;
    private Long creationTimestamp;
    private Long deadlineTimestamp;
    private boolean completed;
}
