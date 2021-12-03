package com.nimvb.app.database.sequence;

import java.util.concurrent.atomic.AtomicInteger;

public class IntegerSequenceGenerator implements ValueGenerator<Integer> {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Integer next() {
        return counter.getAndIncrement();
    }
}
