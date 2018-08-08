package com.keruyun.net.util;

/**
 * Description:
 * Time 2018/8/2-下午5:01
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class DataConversion {

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = ((src[offset] & 0xFF)
                | ((src[offset+1] & 0xFF)<<8)
                | ((src[offset+2] & 0xFF)<<16)
                | ((src[offset+3] & 0xFF)<<24));
        return value;
    }
    public static byte[] intToBytes( int value )
    {
        byte[] src = new byte[4];
        src[3] =  (byte) ((value>>24) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[0] =  (byte) (value & 0xFF);
        return src;
    }
}
