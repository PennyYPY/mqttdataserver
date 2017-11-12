package com.penny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;


@SpringBootApplication
public class MqttDataServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MqttDataServerApplication.class, args);
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
				.transform(p -> p+"(佩颖发送的消息)")
				.handle(handler())
				.get();
	}

	public LoggingHandler logger(){
		LoggingHandler loggingHandler = new LoggingHandler("INFO");
		loggingHandler.setLoggerName("pySample");
		return loggingHandler;
	}

	/**入站适配器*/
	@Bean
	public MessageProducerSupport mqttInbound(){
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("消费者",mqttPahoClientFactory(),"/test/#");
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);

		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler(){
		return new MessageHandler() {
			@Override
			public void handleMessage(Message<?> message) throws MessagingException {
				String sMsg = message.getPayload().toString();
				if (sMsg.equals("佩颖")){
					mqttOutFlow();
				}

				System.out.println("设备发送的消息："+sMsg);

			}
		};
	}

	/**
	 * 配置Producer
	 * */
	@Bean
	public IntegrationFlow mqttOutFlow(){

		return IntegrationFlows.from(outChannel())
				.transform(p -> p +"发送给MQTT的")
				.handle(mqttOutbound())
				.get();
	}

	@Bean
	public MessageChannel outChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("pennySamplePublisher", mqttPahoClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic("/test/topic");
		return messageHandler;
	}

	@MessagingGateway(defaultRequestChannel = "outChannel")
	public interface MsgWriter{
		void write(String note);
	}
}
