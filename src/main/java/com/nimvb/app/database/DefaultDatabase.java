package com.nimvb.app.database;

import com.nimvb.app.database.document.Document;
import com.nimvb.app.database.model.Board;
import com.nimvb.app.database.model.Item;
import com.nimvb.app.database.model.Todo;

import java.util.UUID;


public record DefaultDatabase(
        Document<Board, String> boardDocument,
        Document<Todo, Integer> todoDocument,
        Document<Item, Integer> itemDocument) implements Database {


    @Override
    public Document<Board, String> boards() {
        return boardDocument;
    }

    @Override
    public Document<Todo, Integer> todos() {
        return todoDocument;
    }

    @Override
    public Document<Item, Integer> items() {
        return itemDocument;
    }
}
