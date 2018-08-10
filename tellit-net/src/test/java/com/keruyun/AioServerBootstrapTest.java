package com.keruyun;

import com.keruyun.net.aio.AioServerBootstrap;

import java.io.IOException;

/**
 * Description:
 * Time 2018/8/10-上午11:13
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class AioServerBootstrapTest {

    public static void main(String[] args) {
        AioServerBootstrap aioServerBootstrap = new AioServerBootstrap();
        try {
            aioServerBootstrap.init();
            new Thread(aioServerBootstrap).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
