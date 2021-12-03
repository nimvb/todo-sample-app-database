package com.nimvb.app.database.util;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.UUID;

public class GuidConverter {

    public static byte[] toBytes(UUID uuid){
        Objects.requireNonNull(uuid);
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    public static UUID toGuid(byte[] bytes){
        Objects.requireNonNull(bytes);
        if(bytes.length != 16){
            throw new IllegalArgumentException();
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(),buffer.getLong());
    }
}

