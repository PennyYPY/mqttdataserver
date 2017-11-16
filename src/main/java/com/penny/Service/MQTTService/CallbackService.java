package com.penny.Service.MQTTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/11/12.
 */
@Controller
public class CallbackService {

    @Autowired
    public MessageHandler handler;

    public void callBack(){
        Message<?> message = MessageBuilder
                .withPayload("发送0回传的数据")
                .setHeader(MqttHeaders.TOPIC,"/test/topic")
                .build();
        handler.handleMessage(message);
        System.out.println("回传成功");
    }

}
