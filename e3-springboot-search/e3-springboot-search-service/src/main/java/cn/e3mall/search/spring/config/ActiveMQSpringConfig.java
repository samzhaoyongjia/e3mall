package cn.e3mall.search.spring.config;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ActiveMq spring整合配置
 * @author 罗小黑
 *
 */
@Configuration
public class ActiveMQSpringConfig {
	/**
	 * 这个是队列目的地，点对点的
	 * @return
	 */
	@Bean
	public ActiveMQQueue queueDestination(){
		ActiveMQQueue activeMQQueue = new ActiveMQQueue("spring-queue");
		return activeMQQueue;
	}
	
	/**
	 * 这个是主题目的地，一对多的
	 * @return
	 */
	@Bean
	public ActiveMQTopic topicDestination(){
		ActiveMQTopic activeMQTopic = new ActiveMQTopic("item-change-topic");
		return activeMQTopic;
	}

}
