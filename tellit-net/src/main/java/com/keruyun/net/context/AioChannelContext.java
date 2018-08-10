package com.keruyun.net.context;

import com.keruyun.net.codec.CodecHandle;
import com.keruyun.net.handle.MessageHandle;
import com.keruyun.net.util.BufferUtil;
import com.keruyun.net.util.DataConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.List;

/**
 * Description:
 * Time 2018/8/10-上午11:02
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class AioChannelContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelContext.class);

    private AsynchronousSocketChannel aChannel;

    private ByteBuffer rcvBuf;

    private ByteBuffer sendBuf;

    private int MAX_BUFFER_SIZE = 4 * 1024 * 1024;

    public AioChannelContext(int size,AsynchronousSocketChannel aChannel){
        rcvBuf = ByteBuffer.allocate(size);
        sendBuf = ByteBuffer.allocate(size);
        this.aChannel = aChannel;
    }

    public AsynchronousSocketChannel getaChannel() {
        return aChannel;
    }

    public void setaChannel(AsynchronousSocketChannel aChannel) {
        this.aChannel = aChannel;
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

    public void close() throws IOException {
        BufferUtil.clean(rcvBuf);
        BufferUtil.clean(sendBuf);
        aChannel.close();
        LOGGER.info("ID:{} close ",aChannel.hashCode());
    }
    public void write(Object o) throws Exception {
        byte[] data =  ((String)o).getBytes();
        byte[] length = DataConversion.intToBytes(data.length);
        byte[] res =new byte[4+data.length];
        System.arraycopy(length, 0, res, 0, length.length);
        System.arraycopy(data, 0, res, length.length, data.length);
        if(res.length > MAX_BUFFER_SIZE){
            throw new RuntimeException("max_buffer_size is 4 * 1024 * 1024 ");
        }
        if(res.length > sendBuf.remaining() || res.length > sendBuf.capacity()){
            sendBuf = BufferUtil.scale(sendBuf,res.length);
        }
        sendBuf.put(res);
        sendBuf.flip();
        aChannel.write(sendBuf, this, new CompletionHandler<Integer, AioChannelContext>() {
            @Override
            public void completed(Integer result, AioChannelContext attachment) {
                if(attachment.getSendBuf().hasRemaining()){
                    aChannel.write(attachment.getSendBuf(),attachment,this);
                }
            }
            @Override
            public void failed(Throwable exc, AioChannelContext attachment) {
                exc.printStackTrace();
                try {
                    attachment.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
