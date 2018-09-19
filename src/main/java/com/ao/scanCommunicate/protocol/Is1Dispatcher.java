package com.ao.scanCommunicate.protocol;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ao.scanCommunicate.protocol.packetdown.Is1HeartbeatResponseCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1RegisterCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1StateResponseCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1StopResponseCommand;
import com.ao.scanCommunicate.protocol.packetup.Is1BillResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1ChargeResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1FaultResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1FinishResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1HeartbeatResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1RegisterResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1ResumeResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1StateResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1SuccessResponse;
import com.ao.scanElectricityBis.service.DeviceService;

import ahh.swallowIotServer.ServerIoHandlerAdapter;
import ahh.swallowIotServer.dispatcher.OnUpMessage;
import ahh.swallowIotServer.dispatcher.ProtocolDispatcher;
import ahh.swallowIotServer.exception.IotServerException;

/**
 * 智能插座（Intelligent Socket 1.0）的上行消息处理器
 * 
 * @author jimlee
 *
 */
@ProtocolDispatcher(Is1GlobalConst.IS1_PROTOCOL)
public class Is1Dispatcher {
	private static Logger logger = LoggerFactory.getLogger(Is1Dispatcher.class);

	@Autowired
	private ServerIoHandlerAdapter serverAdapter;
	@Autowired
	private DeviceService deviceService;

	/**
	 * 注册TCP总机上行
	 * 
	 * @param session
	 * @param cmd
	 * @throws IotServerException
	 */
	@OnUpMessage(value = Is1GlobalConst.REGISTER_RESPONSE)
	public void onRegisterResponse(IoSession session, Is1RegisterResponse cmd) throws IotServerException {
		try {
			logger.info("====================>注册TCP总机上行包，session=" + session.getId());

			String deviceCode = new String(cmd.getDeviceCode(), "UTF-8");
			// 更新当前session对应的设备号
			serverAdapter.setSessionDeviceCode(session, deviceCode);
			var device = deviceService.findItemByCode(deviceCode);
			device.setStatus((byte) 1);
			device = deviceService.saveItem(device);
			var response = new Is1RegisterCommand();
			// 验证结果,0：失败，1：成功
			response.setAuthResult((byte) 1);

			// 向设备回复指令
			session.write(response);

			logger.info("====================> 注册TCP总机下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 注册TCP总机上行业务处理出错！！！" + e.getMessage(), e);
		}
	}

	/**
	 * 心跳上行
	 * 
	 * @param session
	 * @param cmd
	 * @throws IotServerException
	 */
	@OnUpMessage(value = Is1GlobalConst.HEARTBEAT_RESPONSE)
	public void onHeartbeatResponse(IoSession session, Is1HeartbeatResponse cmd) throws IotServerException {
		try {
			logger.info("====================>心跳上行包，session=" + session.getId());

			// 更新当前session对应的设备号
			// serverAdapter.setSessionDeviceCode(session, String.valueOf(session.getId()));

			var response = new Is1HeartbeatResponseCommand();

			// 向设备回复指令
			session.write(response);

			logger.info("====================> 心跳下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 心跳上行业务处理出错！！！" + e.getMessage(), e);
		}
	}

	/**
	 * 状态上行
	 * 
	 * @param session
	 * @param cmd
	 * @throws IotServerException
	 */
	@OnUpMessage(value = Is1GlobalConst.STATE_RESPONSE)
	public void onStateResponse(IoSession session, Is1StateResponse cmd) throws IotServerException {
		try {
			logger.info("====================>状态上行包，session=" + session.getId());

			var response = new Is1StateResponseCommand(cmd.getAddr());

			// 向设备回复指令
			session.write(response);

			logger.info("====================> 状态下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 状态上行业务处理出错！！！" + e.getMessage(), e);
		}
	}

	/**
	 * 开始充电上行
	 * 
	 * @param session
	 * @param cmd
	 * @throws IotServerException
	 */
	@OnUpMessage(value = Is1GlobalConst.CHARGE_RESPONSE)
	public void onChargeResponse(IoSession session, Is1ChargeResponse cmd) throws IotServerException {
		try {
			logger.info("====================> 开始充电上行包，session=" + session.getId());

			// Is1ChargeCommand response = new Is1ChargeCommand(cmd.getAddr(), (short) 2);
			// session.write(response);
			//
			// logger.info("====================> 开始充电上下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 开始充电业务处理出错！！！" + e.getMessage(), e);
		}
	}

	/**
	 * 完成充电上行
	 * 
	 * @param session
	 * @param cmd
	 */
	@OnUpMessage(value = Is1GlobalConst.FINISH_RESPONSE)
	public void onFinishResponse(IoSession session, Is1FinishResponse cmd) throws IotServerException {
		try {
			logger.info("====================> 完成充电上行包，session=" + session.getId());

			var response = new Is1StopResponseCommand(cmd.getAddr());

			session.write(response);

			logger.info("====================> 完成充电下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 完成充电业务处理出错！！！" + e.getMessage(), e);
		}
	}

	/**
	 * 账单上行
	 * 
	 * @param session
	 * @param cmd
	 */
	@OnUpMessage(value = Is1GlobalConst.BILL_RESPONSE)
	public void onBillResponse(IoSession session, Is1BillResponse cmd) {
		try {
			logger.info("====================> 账单上行包，session=" + session.getId());

			// Is1BillCommand response = new Is1BillCommand(cmd.getAddr());
			// session.write(response);

			logger.info("====================> 账单下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 账单业务处理出错！！！" + e.getMessage() + e.getMessage(), e);
		}

	}

	/**
	 * 恢复充电上行
	 * 
	 * @param session
	 * @param cmd
	 */
	@OnUpMessage(value = Is1GlobalConst.RESUME_RESPONSE)
	public void onResumeResponse(IoSession session, Is1ResumeResponse cmd) throws IotServerException {
		try {
			logger.info("====================> 恢复充电上行包，session=" + session.getId());

			// Is1ResumeCommand response = new Is1ResumeCommand(cmd.getAddr());
			// 取得设备的数据库数据进行组装
			// session.write(response);

			logger.info("====================> 恢复充电下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 恢复充电业务处理出错！！！" + e.getMessage(), e);
		}
	}

	/**
	 * 成功反馈上行
	 * 
	 * @param session
	 * @param cmd
	 */
	@OnUpMessage(value = Is1GlobalConst.SUCCESS_RESPONSE)
	public void onSuccessResponse(IoSession session, Is1SuccessResponse cmd) {
		try {
			logger.info("====================> 成功反馈上行包，session=" + session.getId());

			// Is1AlertCommand response = new Is1AlertCommand(cmd.getAddr(), 100, 100);
			// 取得设备的数据库数据进行组装
			// session.write(response);

			logger.info("====================> 成功反馈下行包，session=" + session.getId());

		} catch (Exception e) {
			logger.error("====================> 成功反馈业务处理出错！！！" + e.getMessage(), e);
		}
	}

	/**
	 * 失败反馈上行
	 * 
	 * @param session
	 * @param cmd
	 */
	@OnUpMessage(value = Is1GlobalConst.FAULT_RESPONSE)
	public void onFaultResponse(IoSession session, Is1FaultResponse cmd) {
		logger.info("====================> 失败反馈上行包，session=" + session.getId());
	}

}
