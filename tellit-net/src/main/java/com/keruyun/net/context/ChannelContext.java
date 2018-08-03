package com.keruyun.net.context;

import com.keruyun.net.util.DataConversion;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

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

    private AtomicInteger BBBB=new AtomicInteger(0);

    private byte[] headbytes = new byte[4];

    public ChannelContext(int size,SocketChannel sc){
        rcvBuf = ByteBuffer.allocate(size);
        sendBuf = ByteBuffer.allocate(size);
        this.sc = sc;
    }

    public byte[] readBytes(SelectionKey key) throws IOException {
         int num = sc.read(rcvBuf);
         if(num>0){
             while (true) {
                 rcvBuf.flip();
                 if(rcvBuf.remaining()>=4) {
                     rcvBuf.mark();
                     rcvBuf.get(headbytes);
                     int bodyLength = DataConversion.bytesToInt(headbytes, 0);
                     if (bodyLength <= rcvBuf.remaining()) {
                         byte[] data = new byte[bodyLength];
                         rcvBuf.get(data);
                         rcvBuf.compact();
                         System.out.println(new String(data,"UTF-8"));
                     } else if (bodyLength > rcvBuf.capacity() - 4) {
                         rcvBuf.reset();
                         rcvBuf.compact();
                         System.out.println("啊啊啊啊");
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

         }else if(num==0){
             // TODO: 2018/8/2 正常现象
             System.out.println("正常现象");
         }else if(num<0){
             key.channel();
             sc.close();
             System.out.println(sc.hashCode()+"关闭");
         }
        return null;
    }

    private void reset(){



    }

}
