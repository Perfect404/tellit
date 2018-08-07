package com.keruyun.net.context;

import com.keruyun.net.codec.CodecHandle;
import com.keruyun.net.handle.MessageHandle;
import com.keruyun.net.util.BufferUtil;
import com.keruyun.net.util.DataConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * Time 2018/8/2-下午4:10
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class ChannelContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelContext.class);

    private SocketChannel sc;

    private ByteBuffer rcvBuf;

    private ByteBuffer sendBuf;

    private CodecHandle codecHandle;

    private List<MessageHandle> messageHandleList;

    public ChannelContext(int size,SocketChannel sc){
        rcvBuf = ByteBuffer.allocateDirect(size);
        sendBuf = ByteBuffer.allocateDirect(size);
        this.sc = sc;
    }

    public void bindCodecHandle(CodecHandle codecHandle){
        this.codecHandle = codecHandle;
    }

    public void bindMsgHandle(List<MessageHandle> messageHandleList){
        this.messageHandleList = messageHandleList;
    }

    public SocketChannel getSc() {
        return sc;
    }

    public void setSc(SocketChannel sc) {
        this.sc = sc;
    }

    public ByteBuffer getRcvBuf() {
        return rcvBuf;
    }

    public void setRcvBuf(ByteBuffer rcvBuf) {
        this.rcvBuf = rcvBuf;
    }

    public ByteBuffer getSendBuf() {
        return sendBuf;
    }

    public void setSendBuf(ByteBuffer sendBuf) {
        this.sendBuf = sendBuf;
    }

    public CodecHandle getCodecHandle() {
        return codecHandle;
    }

    public void setCodecHandle(CodecHandle codecHandle) {
        this.codecHandle = codecHandle;
    }

    public List<MessageHandle> getMessageHandleList() {
        return messageHandleList;
    }

    public void setMessageHandleList(List<MessageHandle> messageHandleList) {
        this.messageHandleList = messageHandleList;
    }

    public void close() throws IOException {
        BufferUtil.clean(rcvBuf);
        BufferUtil.clean(sendBuf);
        sc.close();
        LOGGER.info("ID:{} close ");
    }

    public List<Object> read(SelectionKey key) throws Exception {
        return codecHandle.decode(key);
    }
    public int write(List<Object> list) throws Exception {
        int sendNum = 0;
        byte[] data = codecHandle.encode(list);
        sendBuf.put(data);
        while (sendBuf.hasRemaining()){
            sendNum =sendNum + sc.write(sendBuf);
            sendBuf.compact();
        }
        return sendNum;
    }
    public int write(Object o) throws Exception {
        int sendNum = 0;
        byte[] data = codecHandle.encode(o);
        sendBuf.put(data);
        while (sendBuf.hasRemaining()){
            sendNum =sendNum + sc.write(sendBuf);
            sendBuf.compact();
        }
        return sendNum;

    }
}
