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
                 ByteBuffer temp = rcvBuf.duplicate();
                 temp.flip();
                 if(temp.remaining()>=4) {
                     temp.get(headbytes);
                     int bodyLength = DataConversion.bytesToInt(headbytes, 0);
                     if (bodyLength <= temp.remaining()) {
                         byte[] data = new byte[bodyLength];
                         temp.get(data);
                         rcvBuf = temp;
                         rcvBuf.compact();
                     } else if (bodyLength > rcvBuf.capacity() - 4) {
                         System.out.println("啊啊啊啊");
                         break;
                     }else if(bodyLength>temp.remaining()){
                         System.out.println("下一次读取 不够数据");
                         break;
                     }

                 }else {
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
