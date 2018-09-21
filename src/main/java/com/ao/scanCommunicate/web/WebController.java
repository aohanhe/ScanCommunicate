package com.ao.scanCommunicate.web;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ao.scanCommunicate.protocol.BaseController;
import com.ao.scanCommunicate.protocol.IGetProtocolRmControl;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.entity.AccountExpense;
import com.ao.scanElectricityBis.entity.StationDevice;
import com.ao.scanElectricityBis.entity.StationPlugInfo;
import com.ao.scanElectricityBis.service.DeviceService;
import com.ao.scanElectricityBis.service.ExpensesService;
import com.ao.scanElectricityBis.service.PlugInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import ahh.swallowIotServer.ServerIoHandlerAdapter;
import ahh.swallowIotServer.session.SessionInfo;

/**
 * 外部控制指令
 * 
 * @author aohan
 *
 */

@RestController
@EnableAutoConfiguration
@Scope("prototype")
@RequestMapping("/api/protocols")
public class WebController {
	/**
	 * 静态日志Logger类
	 */
	protected static Logger logger = LoggerFactory.getLogger(WebController.class);

	

	@Autowired
	private ServerIoHandlerAdapter serverAdpter;

	@Autowired
	private PlugInfoService plugInfoSevice;
	
	
	
	@Autowired
	private ExpensesService expensesService;
	
	@Autowired
	private DeviceService deviceSevice;
	
	@Autowired
	private BaseController deviceControl;

	/**
	 * 平台下发开始充电指令
	 * @throws ScanElectricityException 
	 * 
	 */
	@RequestMapping(value = "/startExpense/{plugCode}/{userId}/{minutes}", method = RequestMethod.GET)
	public Result<Integer> startCharge(@PathVariable String plugCode,@PathVariable int userId,@PathVariable int minutes) throws ScanElectricityException {
	
		try {
			//取得插座的信息
			var plugInfo=plugInfoSevice.findItemByCode(plugCode);
			Assert.notNull(plugInfo, String.format("没有编码%s对应的插座信息", plugCode));
			//取得对应的设备code
			
			var device=deviceSevice.findItemById(plugInfo.getDeviceid(), StationDevice.class).block();
			Assert.notNull(plugInfo, String.format("没有ID=%d对应的设备信息", plugInfo.getDeviceid()));
			
			
			
			// 获取与桩号相关的会话
			IoSession session = serverAdpter.getSessionByDeviceCode(device.getCode());

			if (session == null) {
				return Result.fail("设备没有联网，请联系客服人员");
			}

			var info = SessionInfo.getSessionInfoFromSession(session);
			
			var res = deviceControl.startCharge(session,userId,device.getId(),plugInfo.getDeviceindex(), minutes);
			
			return Result.success(res);
		} catch (Exception e) {
			logger.error("====================> 平台下发开始充电出错！" + e.getMessage(), e);
			return Result.fail("启动充电失败:"+e.getMessage());
		}

		
	}

	/**
	 * 平台下发结束充电指令
	 * @throws ScanElectricityException 
	 */
	@RequestMapping(value = "/stopCharge/{billId}", method = RequestMethod.GET)
	public Result<Boolean> stopCharge(@PathVariable int billId) throws ScanElectricityException {		

		try {
			//取得billId 对应的设备
			var bill=expensesService.findItemById(billId, AccountExpense.class).block();
			Assert.notNull(bill, String.format("编号为%d的订单没有找到", billId));
			
			// 获取与桩号相关的会话
			IoSession session = serverAdpter.getSessionByDeviceCode(bill.getDevicecode());

			if (session == null) {
				return Result.fail("设备没有联网，请联系客服人员");
			}
			
			var plug=plugInfoSevice.findItemById(bill.getPlugid(), StationPlugInfo.class).block();
			Assert.notNull(plug, String.format("编号为%d的插头没有找到", bill.getPlugid()));
			
			
			deviceControl.stopCharge(session,billId,plug.getDeviceindex());
			
			return Result.success(true);
		} catch (Exception e) {
			logger.error("====================> 平台下发结束充电出错！" + e.getMessage(), e);
			
			return Result.fail("停止充电失败:"+e.getMessage());
		}
		
	}
	
}
