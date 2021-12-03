package com.nimvb.app.database;

import com.nimvb.app.database.document.Document;
import com.nimvb.app.database.model.Board;
import com.nimvb.app.database.model.Item;
import com.nimvb.app.database.model.Todo;

import java.util.UUID;

public interface Database {

    Document<Board, String> boards();
    Document<Todo,Integer> todos();
    Document<Item,Integer> items();
}
