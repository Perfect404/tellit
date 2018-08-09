package com.keruyun.net.event;

import com.keruyun.net.codec.CodecHandle;
import com.keruyun.net.codec.LengthCodecHandle;
import com.keruyun.net.context.ChannelContext;
import com.keruyun.net.handle.MessageHandle;
import com.keruyun.net.task.AcceptTask;
import com.keruyun.net.task.ReadTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
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

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    private CodecHandle codecHandle = new LengthCodecHandle();

    private List<MessageHandle> handleList = new ArrayList<>();

    private int BUFFER_SIZE = 2 * 1024;

    public NioEventLoop(int corePoolSize,int maxPoolSize) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                60L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void submmit(SelectionKey key, CountDownLatch countDownLatch) {
        if(key.isValid()){
            if (key.isAcceptable()){
                threadPoolExecutor.execute(new AcceptTask(key,codecHandle,handleList,BUFFER_SIZE,selector,countDownLatch));
            }else if(key.isReadable()){
                threadPoolExecutor.execute(new ReadTask(key,countDownLatch));
            }else if(key.isWritable()){
                countDownLatch.countDown();
            }
        }else {
            countDownLatch.countDown();
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

    public boolean addMessagehandle(MessageHandle messageHandle){
        return handleList.add(messageHandle);
    }
}
