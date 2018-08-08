package com.keruyun.net.task;

import com.keruyun.net.context.ChannelContext;
import com.keruyun.net.handle.MessageHandle;
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
        ChannelContext cc = (ChannelContext) key.attachment();
        if(key.isReadable()){
            try {
                List<Object> msgList = cc.read(key);
                if(cc.getMessageHandleList() == null){
                    LOGGER.error("there is no MessageHandle ");
                    for (Object o : msgList)
                        LOGGER.error((String) o);
                }else {
                    for (Object object : msgList) {
                        Object o = object;
                        for (MessageHandle messageHandle : cc.getMessageHandleList()) {
                            o = messageHandle.handle(cc,o);
                        }
                    }
                }
            } catch (Exception e) {
                try {
                    cc.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                key.attach(null);
                key.cancel();
                LOGGER.error(e.getLocalizedMessage());
            }

        }else {
            try {
                LOGGER.error("key is not Readable close channel:{},clean buffer!",key.channel().hashCode());
                cc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            key.attach(null);
            key.cancel();
        }
        taskNum.decrementAndGet();

    }
}
