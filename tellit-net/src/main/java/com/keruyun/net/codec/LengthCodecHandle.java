package com.keruyun.net.codec;

import com.keruyun.net.context.ChannelContext;
import com.keruyun.net.util.BufferUtil;
import com.keruyun.net.util.DataConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Time 2018/8/6-下午5:13
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class LengthCodecHandle implements CodecHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(LengthCodecHandle.class);

    private static final int HEAD_LENGTH = 4;

    private static byte[] HEAD_BYTES = new byte[HEAD_LENGTH];

    private static final int MAX_BUFFER = 4 * 1024 * 1024;

    @Override
    public byte[] encode(List<Object> list) {
        return new byte[0];
    }

    @Override
    public byte[] encode(Object o) {
        byte[] data =  ((String)o).getBytes();
        byte[] length = DataConversion.intToBytes(data.length);
        byte[] res =new byte[4+data.length];
        System.arraycopy(length, 0, res, 0, length.length);
        System.arraycopy(data, 0, res, length.length, data.length);
        return res;
    }

    @Override
    public List<Object> decode(SelectionKey key) throws Exception {
        List<Object> result = new ArrayList();
        ChannelContext cc =(ChannelContext)key.attachment();
        ByteBuffer rcvBuf = cc.getRcvBuf();
        SocketChannel sc = cc.getSc();
        int num = sc.read(rcvBuf);
        if(num>0){
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
                        result.add(new String(data,"UTF-8"));
                    } else if (bodyLength > rcvBuf.capacity() - HEAD_LENGTH) {
                        if(bodyLength > MAX_BUFFER-HEAD_LENGTH){
                            cc.close();
                            key.attach(null);
                            key.cancel();
                            throw new RuntimeException("header+body length > 4 * 1024 * 1024");
                        }
                        rcvBuf.reset();
                        LOGGER.info("start scale ByteBuffer:{}",rcvBuf.capacity());
                        ByteBuffer temp = rcvBuf;
                        if(temp.isDirect()){
                            cc.setRcvBuf(ByteBuffer.allocateDirect(temp.capacity()*2));
                        }else {
                            cc.setRcvBuf(ByteBuffer.allocate(temp.capacity()*2));
                        }
                        cc.getRcvBuf().put(temp);
                        if(temp.isDirect()){
                            BufferUtil.clean(temp);
                        }
                        LOGGER.info("scale ByteBuffer success:{}",cc.getRcvBuf().capacity());
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

        }else if(num == 0){
        }else if(num < 0){
            cc.close();
            key.attach(null);
            key.cancel();
        }
        return result;
    }
}
