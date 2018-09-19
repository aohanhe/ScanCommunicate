package com.ao.scanCommunicate.protocol;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

	private static final String deviceCode = "deviceCode";

	@Autowired
	private ServerIoHandlerAdapter serverAdpter;

	/**
	 * 平台下发单头状态指令
	 * 
	 */
	@RequestMapping(value = "/getState", method = RequestMethod.GET)
	public HashMap<String, Object> getState() {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			// 获取与桩号相关的会话
			IoSession session = serverAdpter.getSessionByDeviceCode(deviceCode);

			if (session == null) {
				return result;
			}

			var info = SessionInfo.getSessionInfoFromSession(session);
			BaseController baseDeivceControl = ((IGetProtocolRmControl) info.getProtocol()).getDeviceControl();
			result = baseDeivceControl.getState(session);

		} catch (Exception e) {
			logger.error("====================> 平台下发单头状态出错！" + e.getMessage(), e);
		}

		return result;

	}

	/**
	 * 平台下发开始充电指令
	 * 
	 */
	@RequestMapping(value = "/startCharge", method = RequestMethod.GET)
	public HashMap<String, Object> startCharge() {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			// 获取与桩号相关的会话
			IoSession session = serverAdpter.getSessionByDeviceCode(deviceCode);

			if (session == null) {
				return result;
			}

			var info = SessionInfo.getSessionInfoFromSession(session);
			BaseController baseDeivceControl = ((IGetProtocolRmControl) info.getProtocol()).getDeviceControl();
			result = baseDeivceControl.startCharge(session);

		} catch (Exception e) {
			logger.error("====================> 平台下发开始充电出错！" + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 平台下发结束充电指令
	 */
	@RequestMapping(value = "/stopCharge", method = RequestMethod.GET)
	public HashMap<String, Object> stopCharge() {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			// 获取与桩号相关的会话
			IoSession session = serverAdpter.getSessionByDeviceCode(deviceCode);

			if (session == null) {
				return result;
			}

			var info = SessionInfo.getSessionInfoFromSession(session);
			BaseController baseDeivceControl = ((IGetProtocolRmControl) info.getProtocol()).getDeviceControl();
			result = baseDeivceControl.stopCharge(session);

		} catch (Exception e) {
			logger.error("====================> 平台下发结束充电出错！" + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 平台下发账单(时长)指令
	 */
	@RequestMapping(value = "/getChargeDuration", method = RequestMethod.GET)
	public HashMap<String, Object> getChargeDuration() {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			// 获取与桩号相关的会话
			IoSession session = serverAdpter.getSessionByDeviceCode(deviceCode);

			if (session == null) {
				return result;
			}

			var info = SessionInfo.getSessionInfoFromSession(session);
			BaseController baseDeivceControl = ((IGetProtocolRmControl) info.getProtocol()).getDeviceControl();
			result = baseDeivceControl.getChargeDuration(session);

		} catch (Exception e) {
			logger.error("====================> 平台下发账单(时长)出错！" + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 平台下发恢复充电指令
	 */
	@RequestMapping(value = "/resume", method = RequestMethod.GET)
	public HashMap<String, Object> resume() {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			// 获取与桩号相关的会话
			IoSession session = serverAdpter.getSessionByDeviceCode(deviceCode);

			if (session == null) {
				return result;
			}

			var info = SessionInfo.getSessionInfoFromSession(session);
			BaseController baseDeivceControl = ((IGetProtocolRmControl) info.getProtocol()).getDeviceControl();
			result = baseDeivceControl.resume(session);

		} catch (Exception e) {
			logger.error("====================> 平台下发恢复充电出错！" + e.getMessage(), e);
		}

		return result;
	}

	/**
	 * 平台下发设备报警上下值指令
	 * 
	 */
	@RequestMapping(value = "/alert", method = RequestMethod.GET)
	public HashMap<String, Object> alert() {
		HashMap<String, Object> result = new HashMap<String, Object>();

		try {
			// 获取与桩号相关的会话
			IoSession session = serverAdpter.getSessionByDeviceCode(deviceCode);

			if (session == null) {
				return result;
			}

			var info = SessionInfo.getSessionInfoFromSession(session);
			BaseController baseDeivceControl = ((IGetProtocolRmControl) info.getProtocol()).getDeviceControl();
			result = baseDeivceControl.alert(session);

		} catch (Exception e) {
			logger.error("====================> 平台下发设备报警上下值出错！" + e.getMessage(), e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			CloseableHttpClient httpclient = HttpClients.createDefault();
			//String url = "http://localhost:8087/api/protocols/getState";
			//String url = "http://localhost:8087/api/protocols/startCharge";
			//String url = "http://localhost:8087/api/protocols/stopCharge";
			//String url = "http://localhost:8087/api/protocols/getChargeDuration";
			//String url = "http://localhost:8087/api/protocols/resume";
			String url = "http://localhost:8087/api/protocols/alert";
			

			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse response = httpclient.execute(httpget);
			InputStream in = response.getEntity().getContent();
			byte[] b = new byte[1024];
			int length = -1;
			if ((length = in.read(b)) > 0) {
				byte[] content = new byte[length];
				System.arraycopy(b, 0, content, 0, length);
				ObjectMapper om = new ObjectMapper();
				HashMap<String, Object> map = new HashMap<String, Object>();
				map = om.readValue(content, map.getClass());
				// String sn = map.get("sn").toString();
				// int errorCode = Integer.valueOf(map.get("errorCode").toString());
				// System.out.println("SN:" + sn);
				// System.out.println("ErrorCode:" + errorCode);
				System.out.println(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
