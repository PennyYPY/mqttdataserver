package com.penny.utils;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

/**
 * Created by Penny on 2017/11/13.
 * 用paho实现的框架的ServerService
 * 构造方法中新建MqttClient客户端，然后连接broker
 * connect()用来连接数据库
 * publish()用来发布数据
 */
public class ServerServiceUtil {
    private MqttClient client;
    private MqttTopic topic;
    private String Host = "tcp://47.94.242.70:61613";
    private String clientId = "server";
    public MqttMessage message;
    public String topTopic = "/China/HuBei";

    /**构造方法ServerService()：通过MqttClient连接broker*/
    public ServerServiceUtil()throws MqttException{
        client = new MqttClient(Host,clientId,new MemoryPersistence());
        connect();
    }

    /**connect()：对连接broker进行基本配置*/
    public void connect(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName("admin");
        options.setPassword("password".toCharArray());
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallBackUtil());
            client.connect(options);
//            topic = client.getTopic(topTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**publish():发布消息方法*/
    public void publish(MqttMessage message,String secondTopic) throws MqttException{
        topic = client.getTopic(topTopic+secondTopic);
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("发布完成? "+token.isComplete());
    }

}
