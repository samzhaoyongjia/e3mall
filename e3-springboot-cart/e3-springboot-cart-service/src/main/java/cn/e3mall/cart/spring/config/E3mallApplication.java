package cn.e3mall.cart.spring.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * springboot应用
 * Configuration：这是一个配置Spring的配置类
 * SpringBootApplication：Spring Boot项目的核心注解，主要目的是开启自动配置
 * PropertySource：读取外部配置文件
 * ImportResource: 实现xml配置的装载
 * ComponentScan:设置扫描包
 * ignoreResourceNotFound：如果属性文件找不到，是否忽略，默认false，即不忽略，找不到文件并不会抛出异常。 
 * ignore-unresolvable：是否忽略解析不到的属性，如果不忽略，找不到将抛出异常。但它设置为true的主要原因是
 * @author 罗小黑
 *
 */
@Configuration
@PropertySource(value = {"classpath:conf/db.properties","classpath:conf/resource.properties"},
ignoreResourceNotFound = true)
@ComponentScan(basePackages = "cn.e3mall.cart")
@ImportResource("classpath:dubbo/dubbo.xml")
@SpringBootApplication
public class E3mallApplication {
	
	//定义数据源 相当于spring配置文件的dao
	@Value("${jdbc.url}")
	private String jdbcUrl;	
	@Value("${jdbc.username}")
	private String jdbcUsername;
	@Value("${jdbc.password}")
	private String jdbcPassword;
	@Value("${jdbc.driver}")
	private String jdbcDriver;

	/**
	 * 注入bean 
	 * destroy-method="close"的作用是当数据库连接不使用的时候,就把该连接重新放到数据池中,方便下次使用调用.
	 * 方法名相当于配置文件中 bean 的id
	 */
	@Bean(destroyMethod = "close")
	public DataSource dataSource(){
		//创建DruidDataSource连接池工具对象
		DruidDataSource druidDataSource = new DruidDataSource();
		//定义数据源的各个参数
		druidDataSource.setUrl(jdbcUrl);
		druidDataSource.setUsername(jdbcUsername);
		druidDataSource.setPassword(jdbcPassword);
		druidDataSource.setDriverClassName(jdbcDriver);
		druidDataSource.setMaxActive(10);
		druidDataSource.setMinIdle(5);
		//返回连接池工具对象，交由spring管理
		return druidDataSource;
	}

}
