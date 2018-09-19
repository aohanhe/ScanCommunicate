package com.ao.scanCommunicate.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttMessagerHandler {
	void sendToMqtt(String data, @Header(MqttHeaders.TOPIC) String topic);
}