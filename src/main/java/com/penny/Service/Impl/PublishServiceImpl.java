package com.penny.Service.Impl;

import com.penny.utils.ServerServiceUtil;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Administrator on 2017/11/14.
 * 发布消息实现类
 */

public class PublishServiceImpl {

     ServerServiceUtil service = new ServerServiceUtil();



    public void publishMsg(int qos, boolean retrained, String payload) throws MqttException {

//         service.message = new MqttMessage();
//         service.message
//         service.message.setQos(qos);
//         service.message.setRetained(retrained);
//         service.message.setPayload(payload.getBytes());
//         service.publish(service.message);
     }

    public PublishServiceImpl() throws MqttException {

    }
}
