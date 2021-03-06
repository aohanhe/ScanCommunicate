package com.ao.scanCommunicate.protocol;

import org.apache.http.util.Asserts;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ao.scanCommunicate.business.BillManger;
import com.ao.scanCommunicate.protocol.packetdown.Is1HeartbeatResponseCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1RegisterCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1StateResponseCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1StopResponseCommand;
import com.ao.scanCommunicate.protocol.packetup.Is1BillResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1ChargeResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1FaultResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1FinishResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1HeartbeatRequest;
import com.ao.scanCommunicate.protocol.packetup.Is1RegisterRequest;
import com.ao.scanCommunicate.protocol.packetup.Is1ResumeResponse;
import com.ao.scanCommunicate.protocol.packetup.Is1StateRequest;
import com.ao.scanCommunicate.protocol.packetup.Is1SuccessResponse;
import com.ao.scanElectricityBis.service.DeviceService;
import com.ao.scanElectricityBis.service.ExpensesService;
import com.ao.scanElectricityBis.service.PlugInfoService;
import com.mysema.commons.lang.Pair;

import ahh.swallowIotServer.ServerIoHandlerAdapter;
import ahh.swallowIotServer.dispatcher.OnUpMessage;
import ahh.swallowIotServer.dispatcher.ProtocolDispatcher;
import ahh.swallowIotServer.exception.IotServerException;
import ahh.swallowIotServer.session.SessionInfo;
import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

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

	@Autowired
	private PlugInfoService plugInfoService;

	@Autowired
	private ExpensesService expensesService;
	
	@Autowired
	private BillManger billManger;

	/**
	 * 注册TCP总机上行
	 * 
	 * @param session
	 * @param cmd
	 * @throws IotServerException
	 */
	@OnUpMessage(value = Is1GlobalConst.REGISTER_RESPONSE)
	public void onRegisterResponse(IoSession session, Is1RegisterRequest cmd) throws IotServerException {
		try {
			logger.info("====================>注册TCP总机上行包，session=" + session.getId());

			String deviceCode = Long.toString(cmd.getDeviceCode());
			deviceCode = deviceCode.trim();
			// 更新当前session对应的设备号
			serverAdapter.setSessionDeviceCode(session, deviceCode);
			var device = deviceService.findItemByCode(deviceCode);

			Assert.notNull(device, String.format("没有的到编号%s的设备", deviceCode));

			// 把设备ID放在扩展数据中
			this.bindDataToSession(session, new ExtendSessionData(device.getId()));

			deviceService.updateDeviceStatus(device.getId(), DeviceService.DeviceStatus_OnLine);

			var response = new Is1RegisterCommand();
			// 验证结果,0：失败，1：成功
			response.setAuthResult((byte) 1);
			// 向设备回复指令
			session.write(response);

			logger.info("====================> 注册TCP总机上行包，session=" + session.getId() + " deivceCode"
					+ cmd.getDeviceCode());

		} catch (Exception e) {
			logger.error("====================> 注册TCP总机上行业务处理出错！！！" + e.getMessage(), e);
		}
	}

	private final static String ExtendDataKey = "ScanExtend_Data";

	private void bindDataToSession(IoSession session, ExtendSessionData data) {
		session.setAttribute(ExtendDataKey, data);
	}

	/**
	 * 从session中取得设备ID
	 * 
	 * @param session
	 * @return
	 */
	private Integer getSessionDeviceId(IoSession session) {
		if(session.getAttribute(ExtendDataKey)==null) return  null;
		return ((ExtendSessionData) session.getAttribute(ExtendDataKey)).getDeviceId();
	}

	/**
	 * 心跳上行
	 * 
	 * @param session
	 * @param cmd
	 * @throws IotServerException
	 */
	@OnUpMessage(value = Is1GlobalConst.HEARTBEAT_RESPONSE)
	public void onHeartbeatResponse(IoSession session, Is1HeartbeatRequest cmd) throws IotServerException {
		try {
			logger.info("====================>心跳上行包，session=" + session.getId());

			var response = new Is1HeartbeatResponseCommand();

			// 向设备回复指令
			session.write(response);

			// 更新设备插头的状态
			Integer deviceId = getSessionDeviceId(session);
			Asserts.notNull(deviceId, "当前session没有绑定设备ID");

			short state = cmd.getState();

			Flux.range(0, cmd.getLen()).map(index -> {
				short bt = (short) (0x01 << (16-index-1));
				return Pair.of(index+1, (bt & state) != 0);
			}).subscribe(item -> {
				// 把所有插头信息状态更新到数据库				
				plugInfoService.updatePlugIsWorking(deviceId, item.getFirst(), item.getSecond());
			});

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
	public void onStateResponse(IoSession session, Is1StateRequest cmd) throws IotServerException {
		try {
			logger.info("====================>状态上行包，session=" + session.getId());

			var response = new Is1StateResponseCommand(cmd.getAddr());

			// 向设备回复指令
			session.write(response);

			// 更新设备插头的状态
			Integer deviceId = getSessionDeviceId(session);
			Asserts.notNull(deviceId, "当前session没有绑定设备ID");

			// 更新插头的工作状态
			plugInfoService.updatePlugStatus(deviceId, (int) cmd.getAddr(), (int) cmd.getState());

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

			Integer deviceId = getSessionDeviceId(session);
			Asserts.notNull(deviceId, "当前session没有绑定设备ID");

			var billId = plugInfoService.getPlugBillIdByByDeviceIdAndIndex(deviceId, cmd.getAddr());			
			

			Assert.notNull(billId, String.format("设备id=%d 第%d个插头没有绑定帐单信息", deviceId, cmd.getAddr()));
			
			billManger.notifyBillStartCharge(billId);		

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

			Integer deviceId = getSessionDeviceId(session);
			Asserts.notNull(deviceId, "当前session没有绑定设备ID");
			var billId = plugInfoService.getPlugBillIdByByDeviceIdAndIndex(deviceId, cmd.getAddr());

			Assert.notNull(billId, String.format("设备id=%d 第%d个插头没有绑定帐单信息", deviceId, cmd.getAddr()));

			billManger.notifyBillFinishCharge(billId, cmd.getMinutes());

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

			Integer deviceId = getSessionDeviceId(session);
			Asserts.notNull(deviceId, "当前session没有绑定设备ID");
			var billId = plugInfoService.getPlugBillIdByByDeviceIdAndIndex(deviceId, cmd.getAddr());

			Assert.notNull(billId, String.format("设备id=%d 第%d个插头没有绑定帐单信息", (int)deviceId, cmd.getAddr()));

			billManger.notifyBillHeader(billId, Integer.parseInt(cmd.getMinutes()));

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
			
			Integer deviceId = getSessionDeviceId(session);
			Asserts.notNull(deviceId, "当前session没有绑定设备ID");
			var billId = plugInfoService.getPlugBillIdByByDeviceIdAndIndex(deviceId, cmd.getAddr());

			Assert.notNull(billId, String.format("设备id=%d 第%d个插头没有绑定帐单信息", (int)deviceId, cmd.getAddr()));

			billManger.notifyBillResum(billId);
			

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
