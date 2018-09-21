package com.ao.scanCommunicate.protocol;

import java.util.HashMap;

import org.springframework.util.Assert;

public class ExtendSessionData {
	private int deviceId;
	private HashMap<Integer, Integer> map=new HashMap<>();
	
	
	public ExtendSessionData(int deviceId) {
		this.deviceId = deviceId;
	}
	
	
	/**
	 * 设备插座的当前billID
	 * @param index
	 */
	public synchronized void setPlugBillId(int index,int billId) {
		map.put(index, billId);
	}
	
	/**
	 * 取得插头的billID
	 * @param index
	 * @return
	 */
	public synchronized int getPlugBillId(int index) {
		var id=map.get(index);
		
		Assert.notNull(id, "当前插头还没有绑定帐单ID");
		
		return id;
	}


	public int getDeviceId() {
		return deviceId;
	}

}
