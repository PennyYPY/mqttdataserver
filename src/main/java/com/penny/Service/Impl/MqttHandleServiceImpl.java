package com.penny.Service.Impl;

import com.penny.Service.MQTTService.MqttHandleService;
import org.springframework.messaging.Message;

/**
 * Created by Penny on 2017/11/15.
 * Mqtt消息处理实现类
 */
public class MqttHandleServiceImpl implements MqttHandleService{

    Message<?> message;
    String topic = message.getHeaders().get("mqtt_topic").toString();
    String sMsg = message.getPayload().toString();

    /**MqttHandleService():构造方法，用来给message赋值*/
    public void MqttHandleService(Message<?> message){
        this.message = message;
    }
    /**处理Topic方法*/
    @Override
    public void handleTopic() {

    }

    /**处理Message方法*/
    @Override
    public void handleMessage() {

    }
}
