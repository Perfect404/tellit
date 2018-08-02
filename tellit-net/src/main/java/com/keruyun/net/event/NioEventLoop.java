package com.keruyun.net.event;

import com.keruyun.net.context.ChannelContext;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Time 2018/8/2-下午1:53
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class NioEventLoop implements EventLoop {

    private Selector selector;

    private ThreadPoolExecutor threadPoolExecutor;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public NioEventLoop(int corePoolSize,int maxPoolSize) {
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                60L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void submmit(SelectionKey key) {
        if(key.isValid()){
            if (key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                try {
                    SocketChannel socketChannel = ssc.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ,new ChannelContext(1024,socketChannel));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(key.isReadable()){
                ChannelContext cc = (ChannelContext) key.attachment();
                try {
                    cc.readBytes(key);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
}
