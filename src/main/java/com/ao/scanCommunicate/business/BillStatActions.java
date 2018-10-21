package com.ao.scanCommunicate.business;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import com.ao.OrderStates;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.service.ExpensesService;
import com.ao.scanElectricityBis.service.PlugInfoService;
import com.ao.state.OrderEvents;
import com.mysema.commons.lang.Assert;

/**
 * 订单处理操作
 * @author aohanhe
 *
 */
@Component
public class BillStatActions {
	
	private static final Logger logger = LoggerFactory.getLogger(BillStatActions.class);

	
	@Autowired
	private ExpensesService expensesService;
	
	
	/**
	 * 订单开始充电操作
	 * @param context
	 */
	public void onStartChargAction(StateContext<OrderStates, OrderEvents> context) {	
		var billId=getBillIdFromStateContext(context);
		Assert.notNull(billId, String.format("在状态机中没有取得订单id"));		
		expensesService.upExpenseBillStart(billId);		
	}
	
	/**
	 * 订单暂时充电操作
	 * @param context
	 */
	public void onPauseChargAction(StateContext<OrderStates, OrderEvents> context) {
		var billId=getBillIdFromStateContext(context);
		Assert.notNull(billId, String.format("在状态机中没有取得订单id"));	
		expensesService.pauseExpenseBill(billId);
	}
	
	/**
	 * 恢复充电操作
	 * @param context
	 */
	public void onResumChargAction(StateContext<OrderStates, OrderEvents> context) {
		var billId=getBillIdFromStateContext(context);
		Assert.notNull(billId, String.format("在状态机中没有取得订单id"));	
		
		expensesService.upExpenseBillStart(billId);	
	}
	
	/**
	 * 结束充电操作
	 * @param context
	 */
	public void onFinishChargAction(StateContext<OrderStates, OrderEvents> context) {
		var billId=getBillIdFromStateContext(context);
		
		var costMinutes=context.getMessageHeaders().get("CostMinutes");
		Assert.notNull(billId, String.format("在状态机中没有取得订单id"));	
		Assert.notNull(costMinutes, "消息中没有结束充电时的实际充电时长");
		try {
			expensesService.finishExpenseBill(billId, (int)costMinutes);
		} catch (ScanElectricityException e) {
			logger.error("结束订单状态失败:"+e.getMessage(),e);
			throw new RuntimeException("结束订单状态失败:"+e.getMessage(),e);
		}
	}
	
	/**
	 * 充电心跳操作
	 * @param context
	 */
	public void onChargeHeaderAction(StateContext<OrderStates, OrderEvents> context) {
		var billId=getBillIdFromStateContext(context);
		var costMinutes=context.getMessageHeaders().get("CostMinutes");
		if(costMinutes==null) return ;
		
		Assert.notNull(billId, String.format("在状态机中没有取得订单id"));	
		
		try {
			expensesService.upExpenseBillCost(billId, (int)costMinutes);
		} catch (ScanElectricityException e) {
			logger.error("更新订单充电时长失败:"+e.getMessage(),e);
			throw new RuntimeException("更新订单充电时长失败:"+e.getMessage(),e);
		}				
	}
	
	/**
	 * 取得状态机的订单编号
	 * @param stateMachine
	 * @return
	 */
	public static int getBillIdFromStateMachine(StateMachine<OrderStates, OrderEvents> stateMachine) {
		return stateMachine.getExtendedState().get("Bill", Integer.class);
	}
	
	/**
	 * 从扩展上下文中取得订单编号
	 * @param context
	 * @return
	 */
	public static int getBillIdFromStateContext(StateContext<OrderStates, OrderEvents> context) {
		return context.getExtendedState().get("Bill", Integer.class);
	}

}
