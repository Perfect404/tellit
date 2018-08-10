package com.keruyun.net.aio.handle;

import com.keruyun.net.codec.LengthCodecHandle;
import com.keruyun.net.context.AioChannelContext;
import com.keruyun.net.util.BufferUtil;
import com.keruyun.net.util.DataConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * Description:
 * Time 2018/8/10-上午10:48
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class ReadCompletionHandle implements CompletionHandler<Integer,AioChannelContext> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadCompletionHandle.class);

    private static final int HEAD_LENGTH = 4;

    private static byte[] HEAD_BYTES = new byte[HEAD_LENGTH];

    private static final int MAX_BUFFER = 4 * 1024 * 1024;

    private AsynchronousSocketChannel aChannel;

    public ReadCompletionHandle(AsynchronousSocketChannel aChannel){
        if(this.aChannel == null) {
            this.aChannel = aChannel;
        }
    }

    @Override
    public void completed(Integer result, AioChannelContext attachment) {
        ByteBuffer rcvBuf = attachment.getRcvBuf();
        if(result>0){
            while (true) {
                rcvBuf.flip();
                if(rcvBuf.remaining() >= HEAD_LENGTH) {
                    rcvBuf.mark();
                    rcvBuf.get(HEAD_BYTES);
                    int bodyLength = DataConversion.bytesToInt(HEAD_BYTES, 0);
                    if (bodyLength <= rcvBuf.remaining()) {
                        byte[] data = new byte[bodyLength];
                        rcvBuf.get(data);
                        rcvBuf.compact();
                        try {
                            System.out.println(new String(data,"UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        try {
                            attachment.write("哈哈哈123123123123123123");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (bodyLength > rcvBuf.capacity() - HEAD_LENGTH) {
                        if(bodyLength > MAX_BUFFER-HEAD_LENGTH){
                            try {
                                attachment.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            throw new RuntimeException("header+body length > 4 * 1024 * 1024");
                        }
                        rcvBuf.reset();
                        LOGGER.info("start scale ByteBuffer:{}",rcvBuf.capacity());
                        ByteBuffer temp = rcvBuf;
                        ByteBuffer newAttachment;
                        if(temp.isDirect()){
                            newAttachment=ByteBuffer.allocateDirect(temp.capacity()*2);
                        }else {
                            newAttachment=ByteBuffer.allocate(temp.capacity()*2);
                        }
                        newAttachment.put(temp);
                        if(temp.isDirect()){
                            BufferUtil.clean(temp);
                        }
                        attachment.setRcvBuf(newAttachment);
                        aChannel.read(newAttachment,attachment,this);
                        LOGGER.info("scale ByteBuffer success:{}",newAttachment.capacity());
                        break;
                    }else if(bodyLength>rcvBuf.remaining()){
                        rcvBuf.reset();
                        rcvBuf.compact();
                        break;
                    }

                }else {
                    rcvBuf.compact();
                    break;
                }
            }
            aChannel.read(attachment.getRcvBuf(),attachment,this);

        }else if(result == 0){
            aChannel.read(attachment.getRcvBuf(),attachment,this);
            LOGGER.info("正常");
        }else if(result < 0){
            try {
                attachment.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
