package com.keruyun;

import com.keruyun.net.NetServerBootstrap;
import com.keruyun.net.event.NioEventLoop;
import com.keruyun.net.handle.LengthFixMessageHandle;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class NetServerBootstrapTest
{
    public static void main( String[] args )
    {
        NetServerBootstrap serverBootstrap = new NetServerBootstrap(7878);
        NioEventLoop nioEventLoop = new NioEventLoop(5,8);
        nioEventLoop.addMessagehandle(new LengthFixMessageHandle());
        serverBootstrap.wrokerEventLoop(nioEventLoop);
        try {
            serverBootstrap.init();
            serverBootstrap.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
