package com.penny;

import com.penny.Service.DataSaveService.*;
import com.penny.domain.DevOnlineData;
import com.penny.domain.DevVerifyData;
import com.penny.domain.ProtocolConfigDetailData;
import com.penny.domain.ProtocolConfigMasterData;
import com.penny.utils.DevSaveChooseTopic;
import com.penny.utils.ServerServiceUtil;
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

	private final static Logger logger = org.slf4j.LoggerFactory.getLogger(MqttDataServerApplication.class);
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
	/**设备实时数据相关*/
	@Autowired
	private DevOnlineDataService devOnlineDataService;

	DevVerifyData devVerifyData = new DevVerifyData();
	ProtocolConfigDetailData protocolConfigDetailData = new ProtocolConfigDetailData();
	DevOnlineData devOnlineData = new DevOnlineData();

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
				/**
				 * 处理消息时的服务；
				 * */
				String topic = message.getHeaders().get("mqtt_topic").toString();
				String sMsg = message.getPayload().toString();
				String[] sTopic = topic.split("/");
				//三级主题的内容
				String choosePub = sTopic[2];
				//判断三级主题，sys或者具体的sn码
				if (choosePub.equals("sys")){
					switch (topic){
						/**1、设备配置*/
						case "China/HuBei/sys/reg":
							String[] rMsg = sMsg.split("_");
							String rSnCode = rMsg[0];
							String rCheckCode = rMsg[1];
							String rProtocolVersion = rMsg[2];
							/**存储设备配置相应数据*/
							saveDevVerifyData(rSnCode,rCheckCode,"China/HuBei/sys/reg",rProtocolVersion);
							System.out.println("收到:" + topic + "  " + rSnCode + rCheckCode + rProtocolVersion);
							try {
								serverServiceUtil.publish(new MqttMessage("去你妈的，还发不出去".getBytes()),"");
							} catch (MqttException e) {
								e.printStackTrace();
							}
							break;

						/**2、设备数据存储请求*/
						case "China/HuBei/sys/DataSave":
							/**将消息切割*/
							String[] dsMsg = sMsg.split("_");
							String dsSnCode = dsMsg[0];
							String dsProtocolVersion = dsMsg[1];
							/**
							 * 1、协议适配
							 * 将消息中的内容切分成三块snCode，checkCode，protocolVersion；
							 * 根据snCode和protocolVersion在数据库的protocol_config_master中查询isUsed标志;
							 * */
							ProtocolConfigMasterData protocolConfigMasterData = protocolConfigMasterDataService.findProtocolConfigMasterDataBySnCodeAndProtocolVersion(dsSnCode,dsProtocolVersion);
							int isUsed = protocolConfigMasterData.getIsUsed();
							if(isUsed == 1){
								/**如果 isUsed = 1，即协议启用*/
								for (int i = 2; i <= dsMsg.length - 2; i++){
									/**循环存入数据的编号和数据值*/
									devOnlineData.setId(String.valueOf(Math.random()));
									devOnlineData.setSnCode(dsSnCode);
									devOnlineData.setProtocolVersion(dsProtocolVersion);
									devOnlineData.setOffset(i-1);
									devOnlineData.setDataValue(new BigDecimal(dsMsg[i]));
									devOnlineDataService.saveDevOnlineData(devOnlineData);
								}
								/**存入数据库后进行回复，主题为“/China/HuBei/(具体的sn码)”/DataSave*/
								try {
									serverServiceUtil.publish(new MqttMessage("1".getBytes()),"/"+dsMsg[0]+"/DataSaveDone");
								} catch (MqttException e) {
									e.printStackTrace();
								}
							}else if (isUsed == 0){
								/**如果 isUsed = 0，即协议未启用*/
								try {
									serverServiceUtil.publish(new MqttMessage("1".getBytes()),"/"+dsMsg[0]+"/SysCfg/req");
								} catch (MqttException e) {
									e.printStackTrace();
								}
							}
							break;
						default:
							try {
								serverServiceUtil.publish(new MqttMessage("您的配置不合法".getBytes()),"");
							} catch (MqttException e) {
								e.printStackTrace();
							}
							break;
					}

				}else {
					/**
					 * 主题为/China/HuBei/sn/SysCfg/ack的消息处理
					 * @Param topic
					 * @Param
					 * */
					String[] sSAMsg = sMsg.split("_");
					String[] sSATopic = topic.split("/");
					String sSASn = sSATopic[2];

					String sSAProtocolVersion = sSAMsg[0];
					/**要存储的数据为*/
					for (int i = 1; i <= sSAMsg.length - 2; i++) {
						/**将数据存在协议子表中*/
						System.out.println("协议适配完成开始存储"+i+"条数据");
						protocolConfigDetailData.setId(String.valueOf(Math.random()));
						protocolConfigDetailData.setSnCode(sSASn);
						protocolConfigDetailData.setProtocolVersion(sSAProtocolVersion);
						protocolConfigDetailData.setOffset(i);
						protocolConfigDetailData.setDataName(sSAMsg[i]);
						protocolConfigDetailDataService.saveProtocolConfigData(protocolConfigDetailData);
					}
					protocolConfigMasterDataService.upDateProtocolConfigMaster(sSASn,sSAProtocolVersion,1);
					try {
						serverServiceUtil.publish(new MqttMessage("1".getBytes()),"/"+sSAMsg[0]+"/SysCfg/ok");
					} catch (MqttException e) {
						e.printStackTrace();
					}
				}

			}
		};
	}

			@Bean
			@ServiceActivator(inputChannel = "mqttOutboundChannel")
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
	/**存储设备配置数据*/
	public void saveDevVerifyData(String sn,String checkCode,String topic,String proVersion){
		devVerifyData.setId(String.valueOf(Math.random()));
		devVerifyData.setTopic("China/HuBei/sys/reg");
		devVerifyData.setSnCode(sn);
		devVerifyData.setCheckCode(checkCode);
		devVerifyData.setProtocolVersion(proVersion);
		devVerifyData.setGenerateTime(new Date(System.currentTimeMillis()));
		devVerifyDataService.saveDevVerifyData(devVerifyData);
	}

}
