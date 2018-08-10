package com.keruyun.net.aio.handle;

import com.keruyun.net.aio.AioServerBootstrap;
import com.keruyun.net.context.AioChannelContext;

import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Description:
 * Time 2018/8/10-上午10:20
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class AcceptCompletionHandle implements CompletionHandler<AsynchronousSocketChannel,AioServerBootstrap> {
    @Override
    public void completed(AsynchronousSocketChannel result, AioServerBootstrap attachment) {
        attachment.getaChannel().accept(attachment,this);
        AioChannelContext aioChannelContext = new AioChannelContext(1024,result);
        result.read(aioChannelContext.getRcvBuf(),aioChannelContext,new ReadCompletionHandle(result));
    }

    @Override
    public void failed(Throwable exc, AioServerBootstrap attachment) {
        exc.printStackTrace();
        attachment.getLatch().countDown();
    }
}
