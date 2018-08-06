package com.keruyun.net.codec;

import java.nio.ByteBuffer;

/**
 * Description:
 * Time 2018/8/6-下午5:16
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public interface CodecHandle {

    byte[] encode();

    Object decode(ByteBuffer buffer);

}
