package com.keruyun.net.handle;

import com.keruyun.net.context.ChannelContext;

import java.util.List;

/**
 * Description:
 * Time 2018/8/7-下午2:14
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public interface MessageHandle {

    void handle(ChannelContext channelContext, Object msg) throws Exception;

    void handleBatch(ChannelContext channelContext, List<Object> msgList) throws Exception;

}
