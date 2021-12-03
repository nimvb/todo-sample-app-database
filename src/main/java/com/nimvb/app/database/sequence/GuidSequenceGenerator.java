package com.nimvb.app.database.sequence;

import com.nimvb.app.database.util.DateTime;
import com.nimvb.app.database.util.GuidConverter;
import com.nimvb.app.database.util.LongConverter;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class GuidSequenceGenerator implements ValueGenerator<UUID> {

    private final AtomicLong counter = new AtomicLong(DateTime.UtcNow.ticks());
    @Override
    public UUID next() {
        var guidBytes = GuidConverter.toBytes(UUID.randomUUID());
        var counterBytes = LongConverter.toBytes(counter.getAndIncrement());
        guidBytes[8] = counterBytes[1];
        guidBytes[9] = counterBytes[0];
        guidBytes[10] = counterBytes[7];
        guidBytes[11] = counterBytes[6];
        guidBytes[12] = counterBytes[5];
        guidBytes[13] = counterBytes[4];
        guidBytes[14] = counterBytes[3];
        guidBytes[15] = counterBytes[2];
        return GuidConverter.toGuid(guidBytes);
    }
}
