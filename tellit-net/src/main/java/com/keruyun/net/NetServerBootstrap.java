package com.keruyun.net;

import com.keruyun.net.event.EventLoop;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Description:
 * Time 2018/8/2-上午10:02
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class NetServerBootstrap {

    private Selector selector;

    private ServerSocketChannel sc;

    private EventLoop eventLoop;

    private int port;

    private volatile boolean shutdown = false;

    public NetServerBootstrap(int port){
        this.port=port;
    }

    public void init() throws IOException {
        selector = Selector.open();
        sc = ServerSocketChannel.open();
        sc.configureBlocking(false);
        sc.bind(new InetSocketAddress(port),1024);
        sc.register(selector,SelectionKey.OP_ACCEPT);
        System.out.println("port:" + port);
    }

    public void start() throws IOException {
        eventLoop.setSelector(selector);
        for (;;){
            selector.select();
            Set<SelectionKey> selectionKeySet = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeySet.iterator();
            while (keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                if(!Objects.isNull(eventLoop)){
                    eventLoop.submmit(key);
                }else {
                    throw  new RuntimeException("eventLoop is Null");
                }

            }
            if(shutdown){
                break;
            }
        }
        if(selector != null){
            selector.close();
        }
    }

    public NetServerBootstrap wrokerEventLoop(EventLoop eventLoop){
        this.eventLoop = eventLoop;
        return this;
    }

    public void shutdown(){
        this.shutdown = true;
    }



}
