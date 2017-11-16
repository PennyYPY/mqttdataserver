package com.penny;

import com.penny.Service.DataSaveService.*;
import com.penny.utils.ServerService;
import com.penny.Service.Impl.PublishServiceImpl;
import com.penny.domain.DevVerifyData;
import com.penny.domain.TestData;
import org.eclipse.paho.client.mqttv3.MqttException;
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


@SpringBootApplication
@IntegrationComponentScan
public class MqttDataServerApplication {

	//
	private ServerService service;
	@Autowired
	private TestDataService testDataService;
	@Autowired
	private DevAlarmDataService devAlarmDataService;
	@Autowired
	private DevVerifyDataService devVerifyDataService;
	@Autowired
	private DevOnlineDataService devOnlineDataService;
	@Autowired
	private ProtocolConfigDataService protocolConfigDataService;

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
		//消息在栈中的地址；
		String messag = String.valueOf(IntegrationFlows.from(mqttInbound()).transform(p -> p+"").get());
		return IntegrationFlows.from(mqttInbound())
//				.transform(p -> p+"(佩颖发送的消息)")
				.handle(handler())
				.get();
	}

	@Bean
	public LoggingHandler logger(){
		LoggingHandler loggingHandler = new LoggingHandler("INFO");
		loggingHandler.setLoggerName("pySample");
		return loggingHandler;
	}

	/**入站适配器*/
	@Bean
	public MessageChannel mqttInputChannel(){
		return new DirectChannel();
	}

	@Bean
	public MessageProducerSupport mqttInbound(){
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				"消费者"
				,mqttPahoClientFactory()
				,"/test/#");
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

				String sMsg = message.getPayload().toString();
				String topic = message.getHeaders().get("mqtt_topic").toString();
				//设备注册返回
				//设备数据存储分支
				switch (topic){
					//注册Topic的分支
					case "/China/HuBei/sys/reg":
						String[] oMsg = sMsg.split("_");
						//设备注册，将设备sn号、验证码、协议版本存入数据库
						String snCode = oMsg[0];
						String checkCode = oMsg[1];
						String protocolVersion = oMsg[2];
						DevVerifyData devVerifyData = new DevVerifyData();
						devVerifyData.setSnCode(snCode);
						devVerifyData.setCheckCode(checkCode);
						devVerifyData.setProtocolVersion(protocolVersion);
						devVerifyDataService.saveDevVerifyData(devVerifyData);
						break;
					//设备存储的分支；
					case "/China/HuBei/sys/DataSave":
						//设备数据存储
						String[] msg = sMsg.split("_");
						//校验协议版本；
						String protocolVersionSave = msg[1];

						break;
				}

				switch (sMsg){
					case "0":
						//收到设备发送的消息为0时的回传数据
						try {
							PublishServiceImpl publishService = new PublishServiceImpl();
							publishService.publishMsg(1,false,"消息为0的回传！");
						} catch (MqttException e) {
							e.printStackTrace();
						}
						break;
					case "1":
						try {
							PublishServiceImpl publishService = new PublishServiceImpl();
							publishService.publishMsg(1,false,"消息为1的回传！");
						} catch (MqttException e) {
							e.printStackTrace();
						}
						break;
					case "报警":

						System.out.println("设备报警！！！");
						break;
					default:
						//存入数据库
						TestData testData = new TestData();
						testData.setPayload(sMsg);
						testData.setTopic(topic);
						testDataService.saveData(testData);
						System.out.println("消息："+sMsg+"主题是："+topic);
						break;
				}
			}
		};
	}



	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound(){

		MqttPahoMessageHandler messageHandler =
				new MqttPahoMessageHandler("client",mqttPahoClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("/test/topic");
		return messageHandler;
	}

	@Bean
	public MessageChannel mqttOutboundChannel(){
		return new DirectChannel();
	}

	@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
	public interface MyGateway{
		void sentToMqtt(String data);
	}


//	/**
//	 * 配置Producer
//	 * */
//	@Bean
//	public IntegrationFlow mqttOutFlow(){
//
//		return IntegrationFlows.from(outChannel())
//				.transform(p -> p +"发送给MQTT的")
//				.handle(mqttOutbound())
//				.get();
//	}
//
//
//
//	@Bean
//	public MessageChannel mqttOutputChannel() {
//		return new DirectChannel();
//	}
//
//	@Bean
//	public MessageHandler mqttOutbound() {
//		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("pennySamplePublisher", mqttPahoClientFactory());
//		messageHandler.setAsync(true);
//		messageHandler.setDefaultTopic("/test/#");
//		return messageHandler;
//	}
//
//	@MessagingGateway(defaultRequestChannel = "outChannel")
//	public interface MsgWriter{
//		void write(String note);
//	}
}
