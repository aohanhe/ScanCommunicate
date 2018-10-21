package com.ao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextClosedEvent;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


import ahh.swallowIotServer.IotServer;

import ahh.swallowIotServer.exception.IotServerException;
import ahh.swallowIotServer.protocol.config.AutoScanIotConfig;


@EnableJpaRepositories
@EnableMongoRepositories
@DependsOn("iotServer")
@SpringBootApplication(scanBasePackages= { "com.ao","ahh","ao"})
@AutoScanIotConfig(basePackage = "com.ao.*")
@EnableScheduling()
@IntegrationComponentScan(basePackages = { "ahh.*",
		"com.ao.*" })
@EntityScan(basePackages= {"com.ao","ahh","ao"})
public class ScanCommunicateApplication implements CommandLineRunner,ApplicationContextAware,ApplicationListener{
	private static final String SYSTEM_NAME = "智能插座服务系统";
	private static Logger logger = LoggerFactory.getLogger(ScanCommunicateApplication.class);
	@Autowired
	private IotServer server;
	
	private ScanCommServerIoHandler handler;
	
	private ApplicationContext context;
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		//当前的context初始化完后，取得handler对象
		this.context = context;		
	}

	public static void main(String[] args)  throws IotServerException {		
		var context = SpringApplication.run(ScanCommunicateApplication.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {		
		if(event instanceof ContextClosedEvent) {
			if(server!=null)
				server.StopTcpServer();
			
		}
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info(SYSTEM_NAME + "启动................................");
		try {
			//实例化handler对象
			handler=this.context.getBean(ScanCommServerIoHandler.class);
			
			server.loadConfig();			
			/**
			 * 添加处理器来监听session事件
			 */
			
			server.setServerIoHandler(handler);			
			
			server.init();
			server.StartTcpServer();
			logger.info(SYSTEM_NAME + "启动完成................................");
		} catch (Exception ex) {
			logger.info(ex.getMessage(), ex);
			throw new IotServerException(ex.getMessage(), ex); 
		} 
		
	}

	

}