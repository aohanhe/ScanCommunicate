package com.ao.scanCommunicate.protocol;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ao.scanCommunicate.protocol.packetdown.Is1AlertCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1BillCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1ChargeCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1ResumeCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1StateCommand;
import com.ao.scanCommunicate.protocol.packetdown.Is1StopCommand;
import com.ao.scanElectricityBis.base.ScanElectricityException;
import com.ao.scanElectricityBis.service.ExpensesService;
import com.ao.scanElectricityBis.service.PlugInfoService;
import com.mysema.commons.lang.Assert;
import com.ao.scanCommunicate.business.BillManger;
import com.ao.scanCommunicate.business.BillStatActions;
import com.ao.scanCommunicate.protocol.BaseController;

import ahh.swallowIotServer.exception.IotServerException;

@Component
public class Is1Controller implements BaseController {

	/**
	 * 静态日志Logger类
	 */
	protected static Logger logger = LoggerFactory.getLogger(Is1Controller.class);
	@Autowired
	private ExpensesService expenseService;
	
	@Autowired
	private PlugInfoService plugInfo;
	
	@Autowired
	private BillManger billManger;

	

	/**
	 * 平台下发开始充电指令
	 */
	@Override
	public int startCharge(IoSession session,int userId,int deviceId,int index,int minutes) throws IotServerException {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			//在数据库创建充电帐单
			var billId=billManger.createExpenseBill(userId, deviceId, index, minutes);
			
			
			Is1ChargeCommand response = new Is1ChargeCommand();
			response.setAddr((byte) index);
			response.setMinutes((short) minutes);
			// 取得设备的数据库数据进行组装
			session.write(response);

			logger.info("====================> 平台下发开始充电下行包，session=" + session.getId());
						
			
			return billId;

		} catch (Exception e) {
			logger.error("====================> 平台下发开始充电下行包出错！" + e.getMessage(), e);
			throw new IotServerException("启动充电出错:"+e.getMessage(),e);
		}		
	}

	/**
	 * 平台下发结束充电指令
	 * @throws ScanElectricityException 
	 */
	@Override
	public void stopCharge(IoSession session,int billId,int deviceId,int index) throws ScanElectricityException  {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {		
			
			expenseService.finishExpenseBill(billId, null); //立即中止帐单

			//检查指定的设备当前的帐单是不是同一个
			var billCode=plugInfo.getPlugBillIdByByDeviceIdAndIndex(deviceId, index);
			
			Assert.isTrue(billCode!=null&&billCode==billId, 
					String.format("设备(%d) 第(%d)插头当前的id是为%d订单，与要结束的订单id=%d不符", deviceId,index,billCode,billId));
			
			logger.info("====================> 平台下发结束充电下行包，session=" + session.getId());
			
			Is1StopCommand response = new Is1StopCommand();
			response.setAddr((byte) 1);
			// 取得设备的数据库数据进行组装
			session.write(response);
			 

		} catch (Exception e) {
			logger.error("====================> 平台下发结束充电下行包出错！" + e.getMessage(), e);
			throw new ScanElectricityException("结束充电出错:"+e.getMessage(),e);
		}	
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
