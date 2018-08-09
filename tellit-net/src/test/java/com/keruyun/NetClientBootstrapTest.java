package com.keruyun;

import com.keruyun.net.NetClientBootstrap;
import com.keruyun.net.codec.LengthCodecHandle;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unit test for simple NetServerBootstrapTest.
 */
public class NetClientBootstrapTest
{
    public static void main(String[] args) {
        AtomicInteger num = new AtomicInteger(0);
        String msg = "我打完的娃大王大王大王大王大王大王大王大王擦擦撒上擦拭擦拭擦拭擦拭擦拭擦拭次 啊大王大王大王大王大王的娃大王大王大王大王大王娃大王大王大王大王大王的娃大王大王大王大大的娃娃的娃大王大王大王大王大王大王娃大王大王大王大王大王大王的娃打我打完的我呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复等我打完的娃大王大王大王大王大王大王大王大王擦擦撒上擦拭擦拭擦拭擦拭擦拭擦拭次 啊大王大王大王大王大王的娃大王大王大王大王大王娃大王大王大王大王大王的娃大王大王大王大大的娃娃的娃大王大王大王大王大王大王娃大王大王大王大王大王大王的娃打我打完的我呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜呜哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇哇反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复" +
                "反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复反反复复";
        String msg1 = "呵呵呵呵呵呵呵呵呵呵大";

        NetClientBootstrap netClientBootstrap = new NetClientBootstrap(new InetSocketAddress("localhost",7878));
        netClientBootstrap.bindCodesHandle(new LengthCodecHandle());
        try {
            netClientBootstrap.init();
            netClientBootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            long startTime=0;
            @Override
            public void run() {
                while (true){
                    try {
                        List<Object> result  = netClientBootstrap.getMsg();
                        if(result != null){
                            result.stream().forEach(cc -> {
                                if(startTime==0){
                                    startTime=System.currentTimeMillis();
                                }
                                num.incrementAndGet();
                                if(num.get()%10000==0){
                                    System.out.println(num.get());
                                    System.out.println(System.currentTimeMillis()-startTime);
                                }
                            });
                        }else {
                            System.out.println("null");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        for (SocketChannel socketChannel:netClientBootstrap.getSc()) {
            while (!socketChannel.isConnected()) {
                try {
                    Thread.sleep(200);
                    System.out.println("等待连接完成");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                netClientBootstrap.channelContext().get(String.valueOf(socketChannel.hashCode())).write("呵呵呵呵");
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    netClientBootstrap.channelContext().get(String.valueOf(socketChannel.hashCode())).close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
