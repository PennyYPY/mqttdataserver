package com.penny;

import com.penny.Service.DataSaveService.*;
import com.penny.domain.*;
import com.penny.utils.ServerServiceUtil;
import com.penny.utils.UniqueKey;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.*;

import java.math.BigDecimal;
import java.util.Date;


@SpringBootApplication
@IntegrationComponentScan
public class MqttDataServerApplication {

	/**连接MQTT Broker相关*/
	ServerServiceUtil serverServiceUtil = new ServerServiceUtil();
	/**设备注册相关*/
	@Autowired
	private DevVerifyDataService devVerifyDataService;
	/**设备实时数据相关*/
	@Autowired
	private HistoricalDataService historicalDataService;
	/**设备报警数据相关*/
	@Autowired
	private DevAlarmDataService devAlarmDataService;
	/**设备报警类型相关*/
	@Autowired
	private AlarmMessageService alarmMessageService;

	DevVerifyData devVerifyData = new DevVerifyData();
	HistoricalData historicalData = new HistoricalData();
	DevAlarmData devAlarmData = new DevAlarmData();

	public MqttDataServerApplication() throws MqttException {
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context =
				new SpringApplicationBuilder(MqttDataServerApplication.class)
						.web(false)
						.run(args);
		MyGateway gateway = context.getBean(MyGateway.class);
		gateway.sentToMqtt("服务器运行，您好！");

	}

	/**
	 * 配置MQTT client Factory
	 * */
	@Bean
	public MqttPahoClientFactory mqttPahoClientFactory(){
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setServerURIs("tcp://47.94.242.70:61613");
		factory.setUserName("admin");
		factory.setPassword("password");
		return factory;
	}

	/**
	 * 配置consumer
	 * */
	@Bean
	public IntegrationFlow mqttInFlow(){
		return IntegrationFlows.from(mqttInbound())
				.handle(handler())
				.get();
	}

	@Bean
	public LoggingHandler logger(){
		LoggingHandler loggingHandler = new LoggingHandler("INFO");
		loggingHandler.setLoggerName("pySample");
		return loggingHandler;
	}

	/**入站适配器
	 * 1、mqttInputChannel()是订阅topic返回消息的通道
	 * 2、mqtt
	 * */
	@Bean
	public MessageChannel mqttInputChannel(){
		return new DirectChannel();
	}
	@Bean
	public MessageProducerSupport mqttInbound(){
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				"消费者"
				,mqttPahoClientFactory()
				,"/China/HuBei/#");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}



	/**处理的是双方发送的所有消息*/
	@Bean
	public MessageHandler handler(){
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				/**
				 * 处理消息时的服务；
				 * */

				String topic = message.getHeaders().get("mqtt_topic").toString();
				String sMsg = message.getPayload().toString();

				String[] sTopic = topic.split("/");

				String thirdTopic = sTopic[2];

				if (thirdTopic.equals("sys")){

					switch (topic){
						/**1、设备配置数据存储*/
						case "China/HuBei/sys/reg":
							String[] rMsg = sMsg.split("_");
							String rSnCode = rMsg[0];
							String rCheckCode = rMsg[1];
							String rProtocolVersion = rMsg[2];
							/**存储设备配置相应数据*/
							saveDevVerifyData(rSnCode,rCheckCode,rProtocolVersion);
							System.out.println("收到:" + topic + "  " + rSnCode + rCheckCode + rProtocolVersion);
							break;
						/**2、设备数据存储请求*/
						case "China/HuBei/sys/DataSave":
							/**将消息切割*/
							String[] dsMsg = sMsg.split("_");
							String dsSnCode = dsMsg[0];
							String dsProtocolVersion = dsMsg[1];
							String dsHistoricalMsg = dsMsg[2];
							int checkCode = dsMsg.length - 1;

							for (int i = 3;i <= dsMsg.length - 2;i++){

								dsHistoricalMsg =dsHistoricalMsg+"_"+ dsMsg[i];

							}

							System.out.println("收到:" + topic + " " + dsSnCode+"  " + dsProtocolVersion+"  "+ dsMsg[checkCode]);
							/**校验数据完整性*/
							if ((String.valueOf(dsMsg.length-3)).equals(dsMsg[checkCode])){
									/**循环存入数据的编号和数据值*/
									historicalData.setId(UniqueKey.genUniqueKey());
									historicalData.setSnCode(dsSnCode);
									historicalData.setProtocolVersion(dsProtocolVersion);
								    historicalData.setDeviceData(dsHistoricalMsg);

									historicalDataService.saveHistoricalData(historicalData);
								try {
									serverServiceUtil.publish(new MqttMessage("1".getBytes()),"/"+dsSnCode+"/DataSaveDone");
								} catch (MqttException e) {
									e.printStackTrace();
								}
							}else {
								try {
									serverServiceUtil.publish(new MqttMessage("0".getBytes()),"/"+dsSnCode+"/DataSaveDone");
								} catch (MqttException e) {
									e.printStackTrace();
								}
							}
							break;

						case "China/HuBei/sys/Alarm":

							System.out.println(message);

							String[] aMsg = sMsg.split("_");
							String aSn = aMsg[0];
							String aProtocol = aMsg[1];
							Integer aOffset = Integer.valueOf(aMsg[2]);
							Integer aCode = Integer.valueOf(aMsg[3]);
							String aCheck = aMsg[4];

							/**存入报警数据*/
							devAlarmData.setId(UniqueKey.genUniqueKey());
							devAlarmData.setSnCode(aSn);
							devAlarmData.setProtocolVersion(aProtocol);
							devAlarmData.setOffsetNumber(aOffset);
							devAlarmData.setAlarmCode(aCode);
							devAlarmData.setHandleStatus(0);
							devAlarmData.setAlarmTime(new Date());
							devAlarmDataService.saveDevAlarmData(devAlarmData);
							/**取出报警码的详细信息*/
							String alarmInfo = alarmMessageService.findAlarmInfo(aCode).getAlarmInfo();
							/**构造报警数据*/
							String alarmMessage = aSn+"_"+aProtocol+"_"+aOffset+"_"+alarmInfo+"_"+aCheck;

							try {
								serverServiceUtil.publish(new MqttMessage(alarmMessage.getBytes()),"/web/"+aSn+"/Alarm");
							} catch (MqttException e) {
								e.printStackTrace();
							}
							break;
					}
				}else{
					System.out.println("收到或发布的主题为：" + topic+"  "+"消息内容："+sMsg);
				}
			}
		};
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel",outputChannel = "")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler =
				new MqttPahoMessageHandler("client", mqttPahoClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("/China/HuBei/hello/server/hi");
		return messageHandler;
	}

	@Bean
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
	public interface MyGateway {
		void sentToMqtt(String data);
	}

	/**方法：
	 * 存储设备配置数据*/
	public void saveDevVerifyData(String sn,String checkCode,String proVersion){
		devVerifyData.setId(UniqueKey.genUniqueKey());
		devVerifyData.setSnCode(sn);
		devVerifyData.setCheckCode(checkCode);
		devVerifyData.setGenerateTime(new Date());
		devVerifyData.setProtocolVersion(proVersion);

		devVerifyDataService.saveDevVerifyData(devVerifyData);
	}

}
