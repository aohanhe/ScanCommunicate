package com.ao.state;

/**
 * 订单事件
 * @author aohanhe
 *
 */
public enum OrderEvents {
	StartCharge(1,"开始充电"),PauseCharge(2,"暂停"),ResumeCharge(3,"恢复"),EndCharge(4,"结束"),Heartbeat(5,"充电心跳")
	;
	private int eventId;
	private String eventName;
	
	private OrderEvents(int id,String name) {
		this.eventId = id;
		this.eventName = name;
	}
	
	public int getEventId() {
		return this.eventId;
	}
	
	public String getName() {
		return this.eventName;
	}

}
