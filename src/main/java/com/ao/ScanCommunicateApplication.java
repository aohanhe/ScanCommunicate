package com.ao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import ahh.swallowIotServer.IotServer;
import ahh.swallowIotServer.ServerIoHandler;
import ahh.swallowIotServer.exception.IotServerException;
import ahh.swallowIotServer.protocol.config.AutoScanIotConfig;
import ahh.swallowIotServer.session.SessionInfo;

@DependsOn("iotServer")
@SpringBootApplication
@ComponentScan(basePackages = { "ahh", "com.ao" })
@AutoScanIotConfig(basePackage = "com.ao.*")
@EnableScheduling()
@IntegrationComponentScan(basePackages = { "ahh.*",
		"com.ao.*" })
@EnableMongoRepositories
@EnableAutoConfiguration
public class ScanCommunicateApplication {
	private static final String SYSTEM_NAME = "智能插座服务系统";
	private static Logger logger = LoggerFactory.getLogger(ScanCommunicateApplication.class);
	@Autowired
	private IotServer server;
	@Autowired
	private ScanCommServerIoHandler handler;

	public static void main(String[] args) throws IotServerException {
		var context = SpringApplication.run(ScanCommunicateApplication.class, args);

		var app = context.getBean(ScanCommunicateApplication.class);

		// 添加spring 应用的启动监听代码，以方便处理启动释放资源
		// context.addApplicationListener(new
		// ApplicationListener<ClassPathChangedEvent>() {
		//
		// @Override
		// public void onApplicationEvent(ClassPathChangedEvent event) {
		// app.server.StopTcpServer();
		// }
		//
		// });

		logger.info(SYSTEM_NAME + "启动................................");
		try {
			app.server.loadConfig();
			
			/**
			 * 添加处理器来监听session事件
			 */
			app.server.setServerIoHandler(app.handler);
			
			
			app.server.init();
			app.server.StartTcpServer();
			logger.info(SYSTEM_NAME + "启动完成................................");
		} catch (Exception ex) {
			logger.info(ex.getMessage(), ex);
			throw new IotServerException(ex.getMessage(), ex);
		}

	}

}