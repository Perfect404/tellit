package com.keruyun;

import com.keruyun.net.NetServerBootstrap;
import com.keruyun.net.event.NioEventLoop;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        NetServerBootstrap serverBootstrap = new NetServerBootstrap(7878);
        serverBootstrap.wrokerEventLoop(new NioEventLoop(5,10));
        try {
            serverBootstrap.init();
            serverBootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
