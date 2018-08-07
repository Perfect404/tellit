package com.keruyun.net.task;

import com.keruyun.net.context.ChannelContext;
import com.keruyun.net.util.BufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 * Time 2018/8/7-下午2:18
 * Create by gaopan
 * comments:
 * Copyright © 2014-2017 keruyun Inc. All rights reserved.
 **/
public class ReadTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadTask.class);

    private SelectionKey key;

    private AtomicInteger taskNum;

    public ReadTask(SelectionKey key, AtomicInteger taskNum){
        this.key = key;
        this.taskNum = taskNum;
    }

    @Override
    public void run() {
        ChannelContext cc =(ChannelContext) key.attachment();
        if(key.isReadable()){
            try {
                List<Object> msgList= cc.getCodecHandle().decode(key);
                if(cc.getMessageHandleList() == null){
                    for (Object o : msgList)
                        LOGGER.info((String) o);
                }else {
                    LOGGER.info("正常流程");
                }
            } catch (Exception e) {
                key.cancel();
                try {
                    cc.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                LOGGER.error(e.getMessage());
            }

        }else {
            key.cancel();
            try {
                LOGGER.error("key is not Readable close channel:{},clean buffer!",key.channel().hashCode());
                cc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        taskNum.decrementAndGet();

    }
}
