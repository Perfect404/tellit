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

    /**
     * handle the msg, you need throw the exception if there have any error
     * @param channelContext
     * @param msg
     * @throws Exception
     */
    Object handle(ChannelContext channelContext, Object msg) throws Exception;

    /**
     * handleBatch the msgs, you need throw the exception if there have any error
     * @param channelContext
     * @param msgList
     * @throws Exception
     */
    List<Object> handleBatch(ChannelContext channelContext, List<Object> msgList) throws Exception;

}
