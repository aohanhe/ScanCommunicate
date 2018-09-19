package com.ao.scanCommunicate.protocol;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ao.scanCommunicate.protocol.packetdown.Is1AlertCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1BillCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1ChargeCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1ResumeCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1StateCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1StopCommand;
import com.ao.scanCommunicate.protocol.BaseController;

import ahh.swallowIotServer.exception.IotServerException;

public class Is1Controller implements BaseController {

	/**
	 * 静态日志Logger类
	 */
	protected static Logger logger = LoggerFactory.getLogger(Is1Controller.class);

	/**
	 * 平台下发单头状态指令
	 */
	@Override
	public HashMap<String, Object> getState(IoSession session) throws IotServerException {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			Is1StateCommand response = new Is1StateCommand();
			response.setAddr((byte) 1);
			// 取得设备的数据库数据进行组装
			session.write(response);

			logger.info("====================> 平台下发单头状态下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 平台下发单头状态下行包出错！" + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 平台下发开始充电指令
	 */
	@Override
	public HashMap<String, Object> startCharge(IoSession session) throws IotServerException {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			Is1ChargeCommand response = new Is1ChargeCommand();
			response.setAddr((byte) 1);
			response.setMinutes((short) 2);
			// 取得设备的数据库数据进行组装
			session.write(response);

			logger.info("====================> 平台下发开始充电下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 平台下发开始充电下行包出错！" + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 平台下发结束充电指令
	 */
	@Override
	public HashMap<String, Object> stopCharge(IoSession session) throws IotServerException {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			Is1StopCommand response = new Is1StopCommand();
			response.setAddr((byte) 1);
			// 取得设备的数据库数据进行组装
			session.write(response);

			logger.info("====================> 平台下发结束充电下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 平台下发结束充电下行包出错！" + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 平台下发账单(时长)指令
	 */
	@Override
	public HashMap<String, Object> getChargeDuration(IoSession session) throws IotServerException {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			Is1BillCommand response = new Is1BillCommand();
			response.setAddr((byte) 1);
			// 取得设备的数据库数据进行组装
			session.write(response);

			logger.info("====================> 平台下发账单(时长)下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 平台下发账单(时长)下行包出错！" + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 平台下发恢复充电指令
	 */
	@Override
	public HashMap<String, Object> resume(IoSession session) throws IotServerException {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			Is1ResumeCommand response = new Is1ResumeCommand();
			response.setAddr((byte) 1);
			// 取得设备的数据库数据进行组装
			session.write(response);

			logger.info("====================> 平台下发恢复充电下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 平台下发恢复充电下行包出错！" + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 平台下发设备报警上下值指令
	 */
	@Override
	public HashMap<String, Object> alert(IoSession session) throws IotServerException {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			Is1AlertCommand response = new Is1AlertCommand();
			response.setAddr((byte) 1);
			response.setMax(0);
			response.setMin(0);
			// 取得设备的数据库数据进行组装
			session.write(response);

			logger.info("====================> 平台下发设备报警上下值下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 平台下发设备报警上下值指令下行包出错！" + e.getMessage(), e);
		}

		return result;
	}

}
