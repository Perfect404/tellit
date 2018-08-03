package com.keruyun;

import java.io.IOException;

/**
 * Description:
 * Time 2018/7/27-下午4:56
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class CLientTest {

    public static void main(String[] args) {
        ClientServer wdw =new ClientServer(7878);
        new Thread(wdw).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            wdw.doWrite(wdw.sc);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
