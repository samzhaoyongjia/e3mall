package cn.e3mall.order.spring.config;



import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;


/**
 * @author 罗小黑
 *
 */
@Configuration
@PropertySource(value = {"classpath:conf/resource.properties"},
ignoreResourceNotFound = true)
@ComponentScan(basePackages = "cn.e3mall")
@ImportResource("classpath:dubbo/dubbo.xml")
@SpringBootApplication
public class E3mallApplication {
	


}
