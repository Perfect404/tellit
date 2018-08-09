package com.keruyun.net.task;

import com.keruyun.net.codec.CodecHandle;
import com.keruyun.net.context.ChannelContext;
import com.keruyun.net.handle.MessageHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Description:
 * Time 2018/8/9-下午5:20
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class AcceptTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptTask.class);

    private SelectionKey key;

    private CodecHandle codecHandle;

    protected List<MessageHandle> handleList;

    private int buffer_size;

    private Selector selector;

    private CountDownLatch countDownLatch;

    public AcceptTask(SelectionKey key, CodecHandle codecHandle, List<MessageHandle> handleList, int buffer_size, Selector selector, CountDownLatch countDownLatch){
        this.key = key;
        this.codecHandle = codecHandle;
        this.handleList = handleList;
        this.buffer_size = buffer_size;
        this.selector = selector;
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
        try {
            SocketChannel socketChannel = ssc.accept();
            socketChannel.configureBlocking(false);
            ChannelContext channelContext = new ChannelContext(buffer_size,socketChannel);
            channelContext.bindCodecHandle(codecHandle);
            channelContext.setMessageHandleList(handleList);
            socketChannel.register(selector,SelectionKey.OP_READ,channelContext);
            LOGGER.info("{}:连接成功",socketChannel.hashCode());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            countDownLatch.countDown();
        }
    }
}
