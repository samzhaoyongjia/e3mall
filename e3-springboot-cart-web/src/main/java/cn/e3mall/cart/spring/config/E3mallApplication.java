package cn.e3mall.cart.spring.config;



import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;


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
	
	//设置多个访问路径
	//解决ajax请求406问题。.htmlajax解析不了,*.action
	@Bean
	public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
	    ServletRegistrationBean reg = new ServletRegistrationBean(dispatcherServlet);
	    reg.getUrlMappings().clear();
	    reg.addUrlMappings("*.html");
	    reg.addUrlMappings("*.action");
	    return reg;
	}


}
