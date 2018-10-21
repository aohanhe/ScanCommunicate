package com.ao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.ao.scanElectricityBis.service.DeviceService;
import ahh.swallowIotServer.ServerIoHandler;
import ahh.swallowIotServer.ServerIoHandlerAdapter;
import ahh.swallowIotServer.session.SessionInfo;

@Component
public class ScanCommServerIoHandler implements ServerIoHandler{
	@Autowired
	private DeviceService device;
	
	
	@Autowired
	private ServerIoHandlerAdapter serverAdapter;

	@Override
	public void sessionCreated(SessionInfo session) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionClosed(SessionInfo session) throws Exception {
		//当session 关闭时，标识设备关闭
		var code=session.getDevCode();
		if(StringUtils.isBlank(code))
			return;
		var deviceItem=device.findItemByCode(code);
		if(deviceItem!=null) {
			device.updateDeviceStatus(deviceItem.getId(), DeviceService.DeviceStatus_OfficeLine );
		}
		//如果当前设备还有正在进行的充电帐单，则要进行暂停
		
	}

	@Override
	public void exceptionCaught(SessionInfo session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageReceived(SessionInfo session, Object message) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
