package com.penny.utils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/**
 * Created by Administrator on 2017/11/13.
 */

/**发布消息的回调类
 *
 * connectionLost(Throwable cause)
 * 在断开连接时调用
 *
 * messageArrived（String s,MqttMessage mqttMessage）
 * 接收已经预定的发布
 *
 * deliveryComplete(MqttDeliveryToken token)
 * 接收到已经发布的Qos1或Qos2消息的传递令牌时调用
 * */

public class PushCallBackUtil implements MqttCallback{
    @Override
    public void connectionLost(Throwable throwable) {
        //连接丢失后，在这里重新连接
        System.out.println("连接断开，可以做重连");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        //订阅之后会执行到这里
        System.out.println("接收到的消息："+mqttMessage.getPayload().toString());
        System.out.println("接收消息的Qos："+mqttMessage.getQos());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        //publish之后会执行到这里
        System.out.println("传输完成");
    }
}
