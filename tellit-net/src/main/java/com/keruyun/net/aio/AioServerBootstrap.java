package com.keruyun.net.aio;

import com.keruyun.net.aio.handle.AcceptCompletionHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Time 2018/8/10-上午10:05
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class AioServerBootstrap implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AioServerBootstrap.class);

    private AsynchronousServerSocketChannel aChannel;

    private CountDownLatch latch;

    public AioServerBootstrap init() throws IOException {
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup.withCachedThreadPool(new ThreadPoolExecutor(5, 8,
                60L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>()),4);
        aChannel = AsynchronousServerSocketChannel.open(channelGroup);
        aChannel.bind(new InetSocketAddress("192.168.18.179",7878),20 * 1024 * 1024);
        LOGGER.info("bind port:{} ",7878);
        return this;
    }


    @Override
    public void run() {
        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doAccept(){
        aChannel.accept(this,new AcceptCompletionHandle());
    }

    public AsynchronousServerSocketChannel getaChannel() {
        return aChannel;
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
