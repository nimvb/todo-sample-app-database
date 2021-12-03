package com.nimvb.app.database.util;

import java.nio.ByteBuffer;

public class LongConverter {

    private static final ByteBuffer BYTE_BUFFER = ByteBuffer.allocate(Long.BYTES);

    public static synchronized byte[] toBytes(long input){
        BYTE_BUFFER.putLong(0,input);
        return BYTE_BUFFER.array();
    }
}