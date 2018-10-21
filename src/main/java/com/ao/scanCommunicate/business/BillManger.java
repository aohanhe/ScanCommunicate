package com.ao.scanCommunicate.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Component;

import com.ao.OrderStates;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.service.ExpensesService;
import com.ao.scanElectricityBis.service.PlugInfoService;
import com.ao.state.OrderEvents;


import ahh.swallowIotServer.exception.IotServerException;

/**
 * 订单管理类
 * @author aohanhe
 *
 */
@Component
public class BillManger {
	
	private static final Logger logger= LoggerFactory.getLogger(BillManger.class);

	
	// 状态机服务类
	@Autowired
	private StateMachineService<OrderStates, OrderEvents> stateMachineService;
	
	//@Autowired
	private ExpensesService expenseService;
	
	//@Autowired
	private PlugInfoService plugInfo;
	

	
	
	/**
	 * 在指定的设备及指定插头上创建一个新的订单
	 * @param userId
	 * @param deviceId
	 * @param index
	 * @param minutes
	 * @return
	 * @throws IotServerException 
	 */
	public int createExpenseBill(int userId,int deviceId,
			int plugIndex,int minutes) throws IotServerException{
		try {
			//在数据库创建充电帐单
			var billId=expenseService.createExpenseBill(deviceId, plugIndex, userId, minutes);
			//创建订单对应的状态机
			var statmachine=getStateMachineByBillId(billId);
			statmachine.getExtendedState().getVariables().put("Bill", billId);
			
			//绑定插头的订单信息
			plugInfo.setPlugBillIdByDeviceIdAndIndex(billId, deviceId, plugIndex);
			
			return 	billId;	
		}catch(Exception ex) {
			logger.error("====================> 创建订单出错！" + ex.getMessage(), ex);
			throw new IotServerException("创建订单出错:"+ex.getMessage(),ex);
		}
		
	}
	
	
	/**
	 * 通知订单开始充电
	 * @param billId
	 */
	public void notifyBillStartCharge(int billId) {
		var billstate=this.getStateMachineByBillId(billId);
		//通知订单开始充电
		billstate.sendEvent(OrderEvents.StartCharge);
	}
	
	/**
	 * 通知订单结束充电
	 * @param billId
	 * @param costMinute
	 */
	public void notifyBillFinishCharge(int billId,int costMinute) {
		var billstate=this.getStateMachineByBillId(billId);
		var message=MessageBuilder.withPayload(OrderEvents.EndCharge)
				.setHeader("CostMinutes", new Integer(costMinute)).build();
		billstate.sendEvent(message);
	}
	
	/**
	 * 通知订单心跳事件
	 * @param billId
	 * @param costMinute
	 */
	public void notifyBillHeader(int billId,int costMinute) {
		var billstate=this.getStateMachineByBillId(billId);
		var message=MessageBuilder.withPayload(OrderEvents.Heartbeat)
				.setHeader("CostMinutes", new Integer(costMinute)).build();
		billstate.sendEvent(message);
	}
	
	/**
	 * 通知设备所有的插头对应的未完成订单都暂停
	 * @param deviceId
	 * @throws ScanElectricityException 
	 */
	public void notifyAllDeviceCurBillPause(int deviceId) throws ScanElectricityException {
		
		//1 取得所有下在充电，并且是当前设备的列表
		var list=expenseService.getNotFinishItemByDeviceId(deviceId);
		
		for(var item : list) {
			var billstate=this.getStateMachineByBillId(item.getId());
			billstate.sendEvent(OrderEvents.PauseCharge); //通知订单暂停充电
		}				
	}
	
	/**
	 * 恢复订单
	 * @param billId
	 */
	public void notifyBillResum(int billId) {
		var billstate=this.getStateMachineByBillId(billId);
		billstate.sendEvent(OrderEvents.ResumeCharge);
	}
	
	
	
	/**
	 * 取得订单对应的状态机
	 * @param billId
	 * @return
	 */
	public StateMachine<OrderStates, OrderEvents> getStateMachineByBillId(int billId){
		return stateMachineService.acquireStateMachine("B_"+billId);
	}
	
	

}
