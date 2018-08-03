package com.keruyun;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Description:
 * Time 2018/7/26-下午5:21
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class ClientServer implements Runnable  {

    public SocketChannel sc;

    private Selector selector;

    private volatile boolean stop=false;


    public ClientServer(int port){
        try {
            selector = Selector.open();
            sc = SocketChannel.open();
            sc.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        try {
            if(sc.connect(new InetSocketAddress("localhost",7878))){
                sc.register(selector,SelectionKey.OP_READ);
            }else {
                sc.register(selector,SelectionKey.OP_CONNECT);
            }
            while (!stop){
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    handleInput(key);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(selector!=null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public
    void doWrite(SocketChannel sc) throws IOException, InterruptedException {

        Random cc = new Random();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        long start = System.currentTimeMillis();
        for (int i=0;i<5000000;i++){
            String req = "QUERY TIME ORDER "+i;
            byte[] shuzi = intToBytes(req.getBytes().length);
            while (byteBuffer.remaining()>=shuzi.length+req.getBytes().length) {
                byteBuffer.put(shuzi);
                byteBuffer.put(req.getBytes());
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()){
                    sc.write(byteBuffer);
                }
                byteBuffer.compact();
                break;

            }


        }
        System.out.println(System.currentTimeMillis()-start);

        System.out.println("Send order 2 server succeed");
    }
    public static byte[] intToBytes( int value )
    {
        byte[] src = new byte[4];
        src[3] =  (byte) ((value>>24) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[0] =  (byte) (value & 0xFF);
        return src;
    }

 private void handleInput(SelectionKey key) throws IOException, InterruptedException {
        if(key.isValid()){
            SocketChannel socketChannel = (SocketChannel)key.channel();
            if(key.isConnectable()){
                if(sc.finishConnect()){
                    sc.register(selector,SelectionKey.OP_READ);
                    //doWrite(sc);
                }else {
                    System.exit(-1);
                }
            }else if(key.isReadable()){
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(byteBuffer);
                if(readBytes>0){
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String body  = new String(bytes,"UTF-8");
                    System.out.println("Now is : "+body);
                    this.stop=true;
                }else if(readBytes<0){
                    key.cancel();
                    sc.close();
                }else {
                    ;
                }
            }
        }
    }

}
