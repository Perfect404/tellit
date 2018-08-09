package com.keruyun.net;

import com.keruyun.net.codec.CodecHandle;
import com.keruyun.net.context.ChannelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Time 2018/8/7-下午4:45
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class NetClientBootstrap {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetClientBootstrap.class);

    private Selector selector;

    private InetSocketAddress inetSocketAddress;

    private SocketChannel[] sc = new SocketChannel[1];

    private CodecHandle codecHandle;

    private ConcurrentHashMap<String,ChannelContext> channelContextMap = new ConcurrentHashMap<>();

    private int BUFFER_SIZE = 2 * 1024;

    public NetClientBootstrap(InetSocketAddress inetSocketAddress){
        this.inetSocketAddress = inetSocketAddress;
    }

    public NetClientBootstrap init() throws IOException, InterruptedException {
        for (int i=0;i<sc.length;i++) {
            selector = Selector.open();
            sc[i] = SocketChannel.open();
            sc[i].configureBlocking(false);
        }
        return this;
    }
    public NetClientBootstrap start() throws InterruptedException {
        for (int i=0;i<sc.length;i++) {
            TimeUnit.MILLISECONDS.sleep(2);
            try {
                if (sc[i].connect(inetSocketAddress)) {
                    ChannelContext cc = new ChannelContext(BUFFER_SIZE, sc[i]);
                    cc.bindCodecHandle(codecHandle);
                    channelContextMap.put(String.valueOf(sc[i].hashCode()),cc);
                    sc[i].register(selector, SelectionKey.OP_READ, cc);
                } else {
                    sc[i].register(selector, SelectionKey.OP_CONNECT);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return this;
    }
    public List<Object> getMsg() throws Exception {
        selector.select();
        List<Object> resultList = new ArrayList<>();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        SelectionKey key = null;
        while (iterator.hasNext()){
            key = iterator.next();
            iterator.remove();
            if(key.isValid()){
                SocketChannel socketChannel = (SocketChannel)key.channel();
                if(key.isConnectable()){
                    if(socketChannel.finishConnect()){
                        ChannelContext cc = new ChannelContext(BUFFER_SIZE,socketChannel);
                        cc.bindCodecHandle(codecHandle);
                        channelContextMap.put(String.valueOf(socketChannel.hashCode()),cc);
                        socketChannel.register(selector,SelectionKey.OP_READ,cc);
                    }else {
                        System.exit(-1);
                    }
                }else if(key.isReadable()){
                    ChannelContext channelContext = (ChannelContext)key.attachment();
                    resultList.add(channelContext.read(key));
                    channelContext.write("哟哟哟哦哦哦");
                }
            }
        }
        return resultList;
    }

    public NetClientBootstrap bindCodesHandle(CodecHandle codecHandle){
        this.codecHandle = codecHandle;
        return this;
    }

    public SocketChannel[] getSc() {
        return sc;
    }

    public ConcurrentHashMap<String,ChannelContext> channelContext(){
        return channelContextMap;
    }
}
