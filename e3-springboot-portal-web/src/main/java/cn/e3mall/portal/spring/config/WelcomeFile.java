package cn.e3mall.portal.spring.config;

import org.apache.catalina.Context;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 设置默认欢迎页。用来实现页面静态化下的 首页 / 路径跳转 
 * WebMvcConfigurerAdapter 方法设置的 / 路径会被 server.servlet-path过滤掉。
 * 无法实现/默认路径的跳转 
 * 
 * @author 罗小黑
 *
 */
@Configuration
public class WelcomeFile{
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
	   TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
	   TomcatContextCustomizer contextCustomizer = new TomcatContextCustomizer() {
			@Override
			public void customize(Context context) {
				// TODO Auto-generated method stub
				context.addWelcomeFile("/index.html");
			}
		};
	   factory.addContextCustomizers(contextCustomizer);
	   return factory;
	}
      
}