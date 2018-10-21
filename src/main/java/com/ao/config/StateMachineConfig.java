package com.ao.config;


import java.util.EnumSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineFactory;

import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;


import org.springframework.statemachine.data.mongodb.MongoDbPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.mongodb.MongoDbStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;

import com.ao.OrderStates;

import com.ao.scanCommunicate.business.BillStatActions;
import com.ao.state.OrderEvents;



@Configuration
public class StateMachineConfig {
	
	
	/**
	 * jpa序列化配置
	 * @author aohanhe
	 *
	 */
	@Configuration
	public static class JpaPersisterConfig{
		@Bean
		public StateMachineRuntimePersister<OrderStates, OrderEvents, String> stateMachineRuntimePersister(
				MongoDbStateMachineRepository stateMachineRepository) {			
			return new MongoDbPersistingStateMachineInterceptor<>(stateMachineRepository);
		}
	}
	
	/**
	 * 状态机配置
	 * @author aohanhe
	 *
	 */
	@Configuration
	@EnableStateMachineFactory
	public static class MachineConfig extends StateMachineConfigurerAdapter<OrderStates, OrderEvents>{
		@Autowired
		private StateMachineRuntimePersister<OrderStates, OrderEvents, String> stateMachineRuntimePersister;
		
		@Override
		public void configure(StateMachineConfigurationConfigurer<OrderStates, OrderEvents> config) throws Exception {
			config.withPersistence().runtimePersister(stateMachineRuntimePersister);
		}
		
		@Override
		public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
			states.withStates().initial(OrderStates.PendingStart).end(OrderStates.Finished)
				.states(EnumSet.allOf(OrderStates.class));
		}
		
		@Override
		public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
			transitions.withExternal()
					.source(OrderStates.PendingStart).target(OrderStates.Charging)
					.event(OrderEvents.StartCharge).action(startChargeAction())
				.and()
				.withExternal()
					.source(OrderStates.Charging).target(OrderStates.Pausing)
					.event(OrderEvents.PauseCharge).action(pauseChargeAction())
				.and()
				.withExternal()
					.source(OrderStates.Pausing).target(OrderStates.Charging)
					.event(OrderEvents.ResumeCharge).action(resumChargeAction())
				.and()
				.withExternal()
					.source(OrderStates.Charging).target(OrderStates.Charging)
					.event(OrderEvents.Heartbeat).action(headerChargeAction())
				.and()
				.withExternal()
					.source(OrderStates.Charging).target(OrderStates.Finished)
					.event(OrderEvents.EndCharge).action(finishChargeAction())
				;
		}
		
		@Autowired
		private BillStatActions actions;
		
		/**
		 * 开始充电操作
		 * @return
		 */
		@Bean
		public Action<OrderStates, OrderEvents> startChargeAction(){
			return new Action<OrderStates, OrderEvents>(){

				@Override
				public void execute(StateContext<OrderStates, OrderEvents> context) {
					//开始充电操作
					actions.onStartChargAction(context);				
				}
				
			};
		}
		
		@Bean
		public Action<OrderStates, OrderEvents> pauseChargeAction(){
			return new Action<OrderStates, OrderEvents>(){

				@Override
				public void execute(StateContext<OrderStates, OrderEvents> context) {
					//开始充电操作
					actions.onPauseChargAction(context);				
				}
				
			};
		}
		
		@Bean
		public Action<OrderStates, OrderEvents> resumChargeAction(){
			return new Action<OrderStates, OrderEvents>(){

				@Override
				public void execute(StateContext<OrderStates, OrderEvents> context) {
					//开始充电操作
					actions.onResumChargAction(context);				
				}
				
			};
		}
		
		@Bean
		public Action<OrderStates, OrderEvents> finishChargeAction(){
			return new Action<OrderStates, OrderEvents>(){

				@Override
				public void execute(StateContext<OrderStates, OrderEvents> context) {
					//开始充电操作
					actions.onFinishChargAction(context);				
				}
				
			};
		}
		
		@Bean
		public Action<OrderStates, OrderEvents> headerChargeAction(){
			return new Action<OrderStates, OrderEvents>(){

				@Override
				public void execute(StateContext<OrderStates, OrderEvents> context) {
					//开始充电操作
					actions.onChargeHeaderAction(context);				
				}
				
			};
		}
		
	}
	
	/**
	 * 配置stateMachineService
	 * @author aohanhe
	 *
	 */
	@Configuration
	public static class ServiceConfig {
		
		@Bean
		public StateMachineService<OrderStates, OrderEvents> stateMachineService(
				StateMachineFactory<OrderStates, OrderEvents> factroy, 
				StateMachineRuntimePersister<OrderStates, OrderEvents, String> stateMachineRuntimePersister
				)
		{
			return new  DefaultStateMachineService<OrderStates, OrderEvents>(factroy,stateMachineRuntimePersister);					
		}
	}	
	

}
