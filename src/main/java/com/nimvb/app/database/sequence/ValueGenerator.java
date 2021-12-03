package com.nimvb.app.database.sequence;

public interface ValueGenerator<TType> {

    TType next();
}

