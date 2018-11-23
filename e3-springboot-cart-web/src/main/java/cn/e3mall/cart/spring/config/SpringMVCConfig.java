package cn.e3mall.cart.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.e3mall.cart.interceptor.LoginInterceptor;

/**
 * 登录拦截器配置。需要自定义拦截器加载到spring容器中
 * @author 罗小黑
 *
 */
@Configuration
public class SpringMVCConfig extends WebMvcConfigurerAdapter {
	
	//注入自定义拦截器
	@Autowired
	private LoginInterceptor loginInterceptor;
	
	/**
	 * 继承父类方法添加自定义拦截器，并添加拦截路径
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//登录拦截器配置
		registry.addInterceptor(loginInterceptor).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

}
