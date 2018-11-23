package cn.e3mall.item.spring.config;



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
	
	/**
	 *  定义文件上传解析器
	 *  spring自带multipartResolver 自己配置会导致MultiPartFile为空
	 * @return
	 */
	/*
	@Bean
	public CommonsMultipartResolver multipartResolver(){
		CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
		commonsMultipartResolver.setDefaultEncoding("UTF-8");
		commonsMultipartResolver.setMaxUploadSize(5242880);
		return commonsMultipartResolver;
	}
	*/

}
