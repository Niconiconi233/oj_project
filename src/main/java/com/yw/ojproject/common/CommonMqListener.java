package com.yw.ojproject.common;

import com.yw.ojproject.judge.Dispatcher;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
* @program: ojproject
*
* @description: 
*
* @author: YW
*
* @create: 2020-04-03 18:09
**/
@Component
public class CommonMqListener{

    @Autowired
    Dispatcher dispatcher;

    @RabbitListener(queues = "${judge.queue.name}")
    public void consumeJudgeQueue(@Payload String id)
    {
        //dispatcher.judge(id);
    }
}
