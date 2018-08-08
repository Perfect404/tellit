package com.keruyun;

import com.keruyun.net.NetClientBootstrap;
import com.keruyun.net.codec.LengthCodecHandle;

import java.io.IOException;
import java.net.InetSocketAddress;
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
        long startTime=0;
        NetClientBootstrap netClientBootstrap = new NetClientBootstrap(new InetSocketAddress("localhost",7878));
        netClientBootstrap.bindCodesHandle(new LengthCodecHandle());
        try {
            netClientBootstrap.init();
            netClientBootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


        while (true){

            try {
                List<Object> result  = netClientBootstrap.getMsg();
                if(result != null){
                    if(result.size()!=0){
                        if(num.incrementAndGet()==1){
                            startTime = System.currentTimeMillis();
                        }
                        if(num.get()%100000==0){
                            System.out.println((String)result.get(0));
                        }

                        if(num.get() >= 5000000){
                            System.out.println("耗时:"+(System.currentTimeMillis()-startTime));
                            System.exit(-1);
                        }
                        netClientBootstrap.channelContext().write(msg1);

                    }
                }else {
                    System.out.println("null");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
