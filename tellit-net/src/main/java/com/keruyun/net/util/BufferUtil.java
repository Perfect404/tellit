package com.keruyun.net.util;

import sun.nio.ch.DirectBuffer;

import java.nio.ByteBuffer;

/**
 * Description:
 * Time 2018/8/6-上午11:07
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class BufferUtil {

    public static void clean(final ByteBuffer byteBuffer) {
        if (byteBuffer.isDirect()) {
            ((DirectBuffer)byteBuffer).cleaner().clean();
        }
    }

    public static ByteBuffer scale(ByteBuffer buffer,int dataSize){
        int initSize = buffer.capacity();
        while (dataSize > initSize){
            initSize = dataSize * 2;
        }
        if(buffer.isDirect()){
            BufferUtil.clean(buffer);
            return ByteBuffer.allocateDirect(initSize);
        }else {
            return ByteBuffer.allocate(initSize);
        }
    }

}
