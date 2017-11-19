package com.penny.utils;

import com.penny.Service.DataSaveService.DevVerifyDataService;
import com.penny.Service.DataSaveService.ProtocolConfigDetailDataService;
import com.penny.Service.DataSaveService.ProtocolConfigMasterDataService;
import com.penny.domain.DevVerifyData;
import com.penny.domain.ProtocolConfigDetailData;
import com.penny.domain.ProtocolConfigMasterData;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.util.Date;

/**
 * Created by Administrator on 2017/11/19.
 *
 * 该类用于判断主题，来实现根据不同主题回复不同内容
 * 设备注册：
 * 1、判断主题是否为“/China/HuBei/sys/reg”
 * 2、将消息内容切分为三部分：snCode、6位校验码、协议版本
 * 3、将id、snCode、checkCode、protocolVersion、topic存入device_verify中
 *
 * 数据存储
 * 1、先判断协议是否适配；
 * 2、如果适配就发布主题是：“/sn/DataSaveDone”，内容是“1”/“0”的消息；
 * 3、如果不适配就发布主题是：/sn/SysCfg/req，内容是：“1”的请求读消息；
 * 如果订阅的“/sn/SysCfg/ack”主题收到消息，且内容是协议号加数据，那么服务器就回复主题为：“/sn/SysCfg/ok”，内容为：“1”的消息；
 */

public class DevSaveChooseTopic {

    /**连接MQTT Broker相关*/
    ServerServiceUtil serverServiceUtil = new ServerServiceUtil();
    /**设备注册相关*/
    @Autowired
    private DevVerifyDataService devVerifyDataService;
    /**协议配置主表相关*/
    @Autowired
    private ProtocolConfigMasterDataService protocolConfigMasterDataService;
    /**协议配置详细列表相关*/
    @Autowired
    private ProtocolConfigDetailDataService protocolConfigDetailDataService;

    ProtocolConfigMasterData protocolConfigMasterData = new ProtocolConfigMasterData();
    DevVerifyData devVerifyData = new DevVerifyData();
    ProtocolConfigDetailData protocolConfigDetailData = new ProtocolConfigDetailData();

    public DevSaveChooseTopic() throws MqttException {
    }

    public void ChooseTopic(Message<?> message) throws MqttException {
//        /**
//         * 处理消息时的服务；
//         * */
//        String topic = message.getHeaders().get("mqtt_topic").toString();
//        String sMsg = message.getPayload().toString();
//        String[] sTopic = topic.split("/");
//        //三级主题的内容
//        String choosePub = sTopic[2];
//        //判断三级主题，sys或者具体的sn码
//        if (choosePub.equals("sys")){
//            switch (topic){
//                /**1、设备配置*/
//                case "China/HuBei/sys/reg":
//                    String[] rMsg = sMsg.split("_");
//                    String rSnCode = rMsg[0];
//                    String rCheckCode = rMsg[1];
//                    String rProtocolVersion = rMsg[2];
//                    /**存储设备配置相应数据*/
//                    saveDevVerifyData(rSnCode,rCheckCode,"China/HuBei/sys/reg",rProtocolVersion);
//                    System.out.println("收到:" + topic + "  " + rSnCode + rCheckCode + rProtocolVersion);
//                    break;
//
//                /**2、设备数据存储请求*/
//                case "China/HuBei/sys/DataSave":
//                    /**将消息切割*/
//                    String[] dsMsg = sMsg.split("_");
//                    String dsSnCode = dsMsg[0];
//                    String dsProtocolVersion = dsMsg[1];
//                    /**
//                     * 1、协议适配
//                     * 将消息中的内容切分成三块snCode，checkCode，protocolVersion；
//                     * 根据snCode和protocolVersion在数据库的protocol_config_master中查询isUsed标志;
//                     * */
//                    protocolConfigMasterData = protocolConfigMasterDataService.findProtocolConfigMasterDataBySnCodeAndProtocolVersion(dsSnCode,dsProtocolVersion);
//                    Integer isUsed = protocolConfigMasterData.getIsUsed();
//                    if(isUsed == 1){
//                        /**如果 isUsed = 1，即协议启用*/
//                        for (int i = 2; i<dsMsg.length; i++){
//                            /**循环存入数据的编号和数据值*/
//                            protocolConfigDetailData.setId(String.valueOf(Math.random()));
//                            protocolConfigDetailData.setSnCode(dsSnCode);
//                            protocolConfigDetailData.setProtocolVersion(dsProtocolVersion);
//                            protocolConfigDetailData.setOffset(i-1);
//                            protocolConfigDetailData.setDataName(dsMsg[i]);
//                        }
//                        /**存入数据库后进行回复，主题为“/China/HuBei/(具体的sn码)”/DataSave*/
//                            serverServiceUtil.publish(new MqttMessage("1".getBytes()),"/"+dsMsg[0]+"/DataSaveDone");
//                    }else {
//                        /**如果 isUsed = 0，即协议未启用*/
//
//                            serverServiceUtil.publish(new MqttMessage("1".getBytes()),"/"+dsMsg[0]+"/SysCfg/req");
//
//                    }
//                    break;
//                default:
//
//                        serverServiceUtil.publish(new MqttMessage("您的配置不合法".getBytes()),"");
//
//                    break;
//
//            }
//
//        }else {
//            /**
//             * 主题为/China/HuBei/sn/SysCfg/ack的消息处理
//             * @Param topic
//             * @Param
//             * */
//            String[] sSAMsg = sMsg.split("_");
//            String[] sSATopic = topic.split("/");
//            String sSASn = sSATopic[2];
//
//            String sSAProtocolVersion = sSAMsg[0];
//            /**要存储的数据为*/
//            for (int i = 1; i <= sSAMsg.length - 2; i++) {
//                /**将数据存在协议子表中*/
//                protocolConfigDetailData.setId(String.valueOf(Math.random()));
//                protocolConfigDetailData.setSnCode(sSASn);
//                protocolConfigDetailData.setProtocolVersion(sSAProtocolVersion);
//                protocolConfigDetailData.setOffset(i);
//                protocolConfigDetailData.setDataName(sSAMsg[i]);
//                protocolConfigDetailDataService.saveProtocolConfigData(protocolConfigDetailData);
//            }
//            protocolConfigMasterDataService.upDateProtocolConfigMaster(sSASn,sSAProtocolVersion,1);
//            serverServiceUtil.publish(new MqttMessage("1".getBytes()),"/"+sSAMsg[0]+"/SysCfg/ok");
//        }

    }

//    /**存储设备配置数据*/
//    public void saveDevVerifyData(String sn,String checkCode,String topic,String proVersion){
//        devVerifyData.setId(String.valueOf(Math.random()));
//        devVerifyData.setTopic("China/HuBei/sys/reg");
//        devVerifyData.setSnCode(sn);
//        devVerifyData.setCheckCode(checkCode);
//        devVerifyData.setProtocolVersion(proVersion);
//        devVerifyData.setGenerateTime(new Date(System.currentTimeMillis()));
//        devVerifyDataService.saveDevVerifyData(devVerifyData);
//    }


}
