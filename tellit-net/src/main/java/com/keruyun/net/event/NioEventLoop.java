package com.keruyun.net.event;

import com.keruyun.net.codec.CodecHandle;
import com.keruyun.net.codec.LengthCodecHandle;
import com.keruyun.net.context.ChannelContext;
import com.keruyun.net.task.ReadTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * Time 2018/8/2-下午1:53
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class NioEventLoop implements EventLoop {

    private static final Logger LOGGER = LoggerFactory.getLogger(NioEventLoop.class);

    private final AtomicInteger taskNum = new AtomicInteger(0);

    private Selector selector;

    private ThreadPoolExecutor threadPoolExecutor;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private CodecHandle codecHandle = new LengthCodecHandle();

    private int BUFFER_SIZE = 1024;

    public NioEventLoop(int corePoolSize,int maxPoolSize) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                60L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void submmit(SelectionKey key) {
        if(key.isValid()){
            if (key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                try {
                    SocketChannel socketChannel = ssc.accept();
                    socketChannel.configureBlocking(false);
                    ChannelContext channelContext = new ChannelContext(BUFFER_SIZE,socketChannel);
                    channelContext.bindCodecHandle(codecHandle);
                    socketChannel.register(selector,SelectionKey.OP_READ,channelContext);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(key.isReadable()){
                taskNum.incrementAndGet();
                threadPoolExecutor.execute(new ReadTask(key,taskNum));
            }else if(key.isWritable()){

            }
        }
    }

    @Override
    public Selector selector() {
        return selector;
    }

    @Override
    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    @Override
    public AtomicInteger getTaskNum() {
        return taskNum;
    }
}
