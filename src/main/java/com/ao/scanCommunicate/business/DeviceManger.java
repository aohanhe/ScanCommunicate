package com.ao.scanCommunicate.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ao.scanCommunicate.mqtt.MqttMessagerHandler;

import ahh.swallowIotServer.exception.IotServerException;

/**
 * 电桩设备管理模块
 * 
 * @author aohan
 *
 */
@Service
public class DeviceManger {
	private Logger logger = LoggerFactory.getLogger(DeviceManger.class);

	/**
	 * 设置状态序列化处理器
	 */
	@Autowired
	private MongoTemplate mongoTpl;

	@Autowired
	private ObjectMapper obMapper;

	@Autowired
	private MqttMessagerHandler mqtt;

	/**
	 * 取得当前设备的版本编号
	 * 
	 * @param deviceCode
	 * @throws IotServerException
	 */
	public void getCurDeviceVersionCode(String deviceCode) throws IotServerException {
		if (StringUtils.isEmpty(deviceCode)) {
			throw new IotServerException("====================> 参数deviceCode不允许为空 <====================");
		}

	}

}
