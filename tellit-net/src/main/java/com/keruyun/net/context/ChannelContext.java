package com.keruyun.net.context;

import com.keruyun.net.util.DataConversion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Description:
 * Time 2018/8/2-下午4:10
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class ChannelContext {

    private SocketChannel sc;

    private ByteBuffer rcvBuf;

    private ByteBuffer sendBuf;

    private byte[] headbytes = new byte[4];

    public ChannelContext(int size,SocketChannel sc){
        rcvBuf = ByteBuffer.allocate(size);
        sendBuf = ByteBuffer.allocate(size);
        this.sc = sc;
    }

    public byte[] readBytes(SelectionKey key) throws IOException {
         int num = sc.read(rcvBuf);
         if(num>0){
             ByteBuffer temp = rcvBuf.duplicate();
             temp.flip();
             if(temp.remaining()>=4){
                temp.get(headbytes);
                int bodyLength= DataConversion.bytesToInt(headbytes,0);
                if(bodyLength<=temp.remaining()){
                    byte[] data = new byte[bodyLength];
                    temp.get(data);

                    System.out.println(new String(data,"UTF-8"));
                    rcvBuf = temp;
                    rcvBuf.compact();
                    return data;
                }else if(bodyLength>rcvBuf.capacity()-4){

                }
             }else {
                 System.out.println("长度不够");
             }
         }else if(num==0){
             // TODO: 2018/8/2 正常现象
         }else if(num<0){
             key.channel();
             sc.close();
         }
        return null;
    }

    private void reset(){



    }

}
