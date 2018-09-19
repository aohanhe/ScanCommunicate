package com.ao.scanCommunicate.protocol;

/**
 * 全局常量类
 * 
 * @author lizimu
 *
 */
public class Is1GlobalConst {
	/**
	 * 智能接口（Intelligent Socket）协议常量
	 */
	public static final int IS1_PROTOCOL = 0x100;

	/**
	 * 设备响应心跳（主动上行）
	 */
	public static final int HEARTBEAT_RESPONSE = 0x00A1;

	/**
	 * 设备响应单头状态
	 */
	public static final int STATE_RESPONSE = 0x00A2;

	/**
	 * 设备响应开始充电
	 */
	public static final int CHARGE_RESPONSE = 0x00A3;

	/**
	 * 设备响应完成充电
	 */
	public static final int FINISH_RESPONSE = 0x00A4;

	/**
	 * 设备响应账单（充电时长及电量）
	 */
	public static final int BILL_RESPONSE = 0x00A5;

	/**
	 * 设备响应恢复充电
	 */
	public static final int RESUME_RESPONSE = 0x00A6;

	/**
	 * 设备响应接收成功
	 */
	public static final int SUCCESS_RESPONSE = 0x00AA;

	/**
	 * 设备响应接收失败
	 */
	public static final int FAULT_RESPONSE = 0x00AB;

	/**
	 * 注册登记充电总机
	 */
	public static final int REGISTER_RESPONSE = 0x00CD;

	/**
	 * 平台下发注册登记充电总机指令，设备将响应CHARGE_RESPONSE
	 */
	public static final int REGISTER_COMMAND = 0x00DD;

	/**
	 * 平台下发单头状态指令，设备将响应STATE_RESPONSE
	 */
	public static final int STATE_COMMAND = 0x00B2;

	/**
	 * 平台下发开始充电指令，设备将响应CHARGE_RESPONSE
	 */
	public static final int CHARGE_COMMAND = 0x00B3;

	/**
	 * 平台下发结束充电指令，设备将响应FINISH_RESPONSE
	 */
	public static final int STOP_COMMAND = 0x00B4;

	/**
	 * 平台下发账单指令，设备将响应BILL_RESPONSE
	 */
	public static final int BILL_COMMAND = 0x00B5;

	/**
	 * 平台下发恢复充电指令，设备将响应RESUME_RESPONSE
	 */
	public static final int RESUME_COMMAND = 0x00B6;

	/**
	 * 平台下发设备报警上下值指令，设备将响应RESUME_RESPONSE
	 */
	public static final int ALERT_COMMAND = 0x00B7;

	/**
	 * 设备回应心跳（下行指令）
	 */
	public static final int HEARTBEAT_RESPONSE_COMMAND = 0x00D1;

	/**
	 * 设备回应状态（下行指令）
	 */
	public static final int STATE_RESPONSE_COMMAND = 0x00D2;

	/**
	 * 设备回应充电完成（下行指令）
	 */
	public static final int STOP_RESPONSE_COMMAND = 0x00D4;

}
