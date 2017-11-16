package com.penny.Service.MQTTService;

/**
 * Created by Penny on 2017/11/15.
 *处理设备发来的Mqtt消息接口
 * 1、处理主题
 * 2、处理消息
 */
public interface MqttHandleService {

    void handleTopic();

    void handleMessage();

}
