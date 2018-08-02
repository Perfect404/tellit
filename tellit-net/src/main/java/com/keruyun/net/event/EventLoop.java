package com.keruyun.net.event;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Description:
 * Time 2018/8/2-上午10:07
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public interface EventLoop {

    void submmit(SelectionKey key);


    Selector selector();

    void setSelector(Selector selector);

}
