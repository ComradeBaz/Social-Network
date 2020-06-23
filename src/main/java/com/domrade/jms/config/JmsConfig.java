/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.domrade.jms.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author https://www.devglan.com/spring-boot/spring-boot-jms-activemq-example
 */
@Configuration
public class JmsConfig {
    String BROKER_URL = "tcp://localhost:61616"; 
	String BROKER_USERNAME = "admin"; 
	String BROKER_PASSWORD = "admin";
	
	@Bean
	public ActiveMQConnectionFactory connectionFactory(){
	    ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
	    connectionFactory.setBrokerURL(BROKER_URL);
	    connectionFactory.setPassword(BROKER_USERNAME);
	    connectionFactory.setUserName(BROKER_PASSWORD);
	    return connectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate(){
	    JmsTemplate template = new JmsTemplate();
	    template.setConnectionFactory(connectionFactory());
            template.setDefaultDestinationName("inbound.queue");
            template.setPubSubDomain(Boolean.TRUE);
	    return template;
	}

	@Bean
	public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
	    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
	    factory.setConnectionFactory(connectionFactory());
            factory.setPubSubDomain(Boolean.TRUE);
	    factory.setConcurrency("1-1");
	    return factory;
	}
}
