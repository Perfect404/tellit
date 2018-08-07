package com.keruyun.net.codec;

import java.nio.channels.SelectionKey;
import java.util.List;

/**
 * Description:
 * Time 2018/8/6-下午5:16
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public interface CodecHandle {


    byte[] encode();


    List<Object> decode(SelectionKey key) throws Exception;

}
