package com.ao.scanCommunicate.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttConfig {
	private String serverUrl;
	private String user;
	private String pwd;
	private String clientId;
	private String topicPre;

	@Bean
	public MqttPahoClientFactory mqttClientFactory() {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

		MqttConnectOptions ops = new MqttConnectOptions();
		if (!StringUtils.isEmpty(user))
			ops.setUserName(user);
		if (!StringUtils.isEmpty(pwd))
			ops.setPassword(pwd.toCharArray());
		String[] urs = { serverUrl };
		ops.setServerURIs(urs);

		factory.setConnectionOptions(ops);

		return factory;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttOutboundChannel")
	public MessageHandler mqttOutbound() {
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId + System.currentTimeMillis(),
				mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultTopic(topicPre);

		return messageHandler;
	}

	@Bean("mqttOutboundChannel")
	public MessageChannel mqttOutboundChannel() {
		return new DirectChannel();
	}

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getTopicPre() {
		return topicPre;
	}

	public void setTopicPre(String topicPre) {
		this.topicPre = topicPre;
	}

}
