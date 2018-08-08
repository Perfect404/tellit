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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

    private SocketChannel sc;

    private CodecHandle codecHandle;

    private ChannelContext channelContext;

    private int BUFFER_SIZE = 4 * 1024;

    public NetClientBootstrap(InetSocketAddress inetSocketAddress){
        this.inetSocketAddress = inetSocketAddress;
    }

    public NetClientBootstrap init() throws IOException {
        selector = Selector.open();
        sc = SocketChannel.open();
        sc.configureBlocking(false);
        return this;
    }
    public NetClientBootstrap start() {
        try {
            if(sc.connect(inetSocketAddress)){
                channelContext = new ChannelContext(BUFFER_SIZE,sc);
                channelContext.bindCodecHandle(codecHandle);
                sc.register(selector,SelectionKey.OP_READ,channelContext);
            }else {
                sc.register(selector,SelectionKey.OP_CONNECT);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return this;
    }
    public List<Object> getMsg() throws Exception {
        selector.select();
        Set<SelectionKey> selectionKeys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = selectionKeys.iterator();
        SelectionKey key = null;
        while (iterator.hasNext()){
            key = iterator.next();
            iterator.remove();
            if(key.isValid()){
                SocketChannel socketChannel = (SocketChannel)key.channel();
                if(key.isConnectable()){
                    if(sc.finishConnect()){
                        channelContext = new ChannelContext(BUFFER_SIZE,socketChannel);
                        channelContext.bindCodecHandle(codecHandle);
                        sc.register(selector,SelectionKey.OP_READ,channelContext);
                        channelContext.write("第一次来了");
                    }else {
                        System.exit(-1);
                    }
                }else if(key.isReadable()){
                    List<Object> resultList = channelContext.read(key);
                    return resultList;
                }
            }
        }
        return null;
    }

    public NetClientBootstrap bindCodesHandle(CodecHandle codecHandle){
        this.codecHandle = codecHandle;
        return this;
    }

    public SocketChannel getSc() {
        return sc;
    }

    public ChannelContext channelContext(){
        return channelContext;
    }
}
