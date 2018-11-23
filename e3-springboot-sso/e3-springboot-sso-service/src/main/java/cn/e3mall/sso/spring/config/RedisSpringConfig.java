package cn.e3mall.sso.spring.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.e3mall.common.jedis.JedisClientCluster;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

/**
 * redis集群版 spring整合配置
 * @author 罗小黑
 *
 */
@Configuration
public class RedisSpringConfig {
	
	@Bean
	public JedisCluster jedisCluster(){
		//设置集群中各个服务器的id和端口号
		HostAndPort hostAndPort1 = new HostAndPort("192.168.25.129", 7001);
		HostAndPort hostAndPort2 = new HostAndPort("192.168.25.129", 7002);
		HostAndPort hostAndPort3 = new HostAndPort("192.168.25.129", 7003);
		HostAndPort hostAndPort4 = new HostAndPort("192.168.25.129", 7004);
		HostAndPort hostAndPort5 = new HostAndPort("192.168.25.129", 7005);
		HostAndPort hostAndPort6 = new HostAndPort("192.168.25.129", 7006);
		//存到set集合中
		Set<HostAndPort> set = new HashSet<HostAndPort>();
		set.add(hostAndPort1);
		set.add(hostAndPort2);
		set.add(hostAndPort3);
		set.add(hostAndPort4);
		set.add(hostAndPort5);
		set.add(hostAndPort6);
		//将集合添加到jedisCluster中
		JedisCluster jedisCluster = new JedisCluster(set);
		//返回给spring
		return jedisCluster;
	}
	
	/**
	 * 集群版实现类，注入jedisCluster
	 */
	@Bean
	public JedisClientCluster jedisClientCluster(JedisCluster jedisCluster){
		JedisClientCluster jedisClientCluster = new JedisClientCluster();
		//将jedisCluster注入自己定义的实现类中
		jedisClientCluster.setJedisCluster(jedisCluster);
		return jedisClientCluster;
	}

}
