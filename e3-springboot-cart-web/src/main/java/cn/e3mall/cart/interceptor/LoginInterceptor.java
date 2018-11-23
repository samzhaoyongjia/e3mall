package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * 登录拦截器，用于判断用户是否登录.handler，处理程序
 * @author 罗小黑
 *
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
	
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	//注入tokenService。用于根据token查询redis中user信息
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//handler执行前
		//判断用户是否登录
		//从cookie中取出token
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
		//判断token是否为空
		if (StringUtils.isBlank(token)) {
			//放行
			return true;
		}
		//根据token从redis中查询user信息。
		E3Result e3Result = tokenService.getUserByToken(token);
		//判断user信息是否过期
		if (e3Result.getStatus() != 200) {
			//过期，放行
			return true;
		}
		//表示已经登录。将用户信息存储到requset中传递到controller
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
			throws Exception {
		//handler执行后，返回视图后执行，一般处理异常

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView arg3)
			throws Exception {
		//handler执行后，返回视图前执行。

	}

	

}
