package com.keruyun.net.handle;

import com.keruyun.net.context.ChannelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description:
 * Time 2018/8/7-下午6:16
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class LengthFixMessageHandle implements MessageHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(LengthFixMessageHandle.class);


    @Override
    public void handle(ChannelContext channelContext, Object msg) throws Exception {

        LOGGER.info((String) msg);


    }

    @Override
    public void handleBatch(ChannelContext channelContext, List<Object> msgList) throws Exception {

    }
}
