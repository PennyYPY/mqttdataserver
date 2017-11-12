package com.penny.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/11/10.
 */

@RestController
public class PublishController {

    @Qualifier("mqttOutbound")
    @Autowired
    public MessageHandler mqtt;

    /**发送给设备消息*/
    @RequestMapping(value = "/send")
    public void sendMessage(){
        Message<String> message = MessageBuilder
                .withPayload("服务器发来的消息")
                .setHeader(MqttHeaders.TOPIC,"/test/topic")
                .build();
        mqtt.handleMessage(message);
        System.out.println("成功");

    }

}
