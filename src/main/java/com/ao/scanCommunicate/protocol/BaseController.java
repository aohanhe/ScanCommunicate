package com.ao.scanCommunicate.protocol;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

import ahh.swallowIotServer.exception.IotServerException;

public interface BaseController {

	/**
	 * 平台下发单头状态指令
	 * 
	 * @param session
	 * @return
	 * @throws IotServerException
	 */
	HashMap<String, Object> getState(IoSession session) throws IotServerException;

	/**
	 * 平台下发开始充电指令
	 * 
	 * @param session
	 * @return
	 * @throws IotServerException
	 */
	HashMap<String, Object> startCharge(IoSession session) throws IotServerException;

	/**
	 * 平台下发结束充电指令
	 * 
	 * @param session
	 * @return
	 * @throws IotServerException
	 */
	HashMap<String, Object> stopCharge(IoSession session) throws IotServerException;

	/**
	 * 平台下发账单(时长)指令
	 * 
	 * @param session
	 * @return
	 * @throws IotServerException
	 */
	HashMap<String, Object> getChargeDuration(IoSession session) throws IotServerException;

	/**
	 * 平台下发恢复充电指令
	 * 
	 * @param session
	 * @return
	 * @throws IotServerException
	 */
	HashMap<String, Object> resume(IoSession session) throws IotServerException;

	/**
	 * 平台下发设备报警上下值指令
	 * 
	 * @param session
	 * @return
	 * @throws IotServerException
	 */
	HashMap<String, Object> alert(IoSession session) throws IotServerException;

}
