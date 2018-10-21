package com.ao.scanCommunicate.protocol;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

import com.ao.scanElectricityBis.base.ScanElectricityException;

import ahh.swallowIotServer.exception.IotServerException;

public interface BaseController {

	

	
	

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

	


	int startCharge(IoSession session,int userId,int deviceId,int index,int minutes) throws IotServerException;

	/**
	 * 平台下发结束充电指令
	 * @param session
	 * @param billId
	 * @param deviceId
	 * @param index
	 * @throws ScanElectricityException 
	 */
	void stopCharge(IoSession session, int billId, int deviceId, int index) throws ScanElectricityException;

}
