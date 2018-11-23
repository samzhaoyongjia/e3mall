package cn.e3mall.order.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

/**
 * 订单结算拦截器。需要用户登录
 * @author 罗小黑
 *
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
	
	//注入redis缓存操作接口
	@Autowired
	private TokenService tokenService;
	@Autowired
	private CartService cartService;
	
	//未登录购物车cookie key
	@Value("${COOKIE_CART}")
	private String COOKIE_CART;
	//token
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	//sso登录url
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//从cookie中取token。
		String token = CookieUtils.getCookieValue(request, COOKIE_TOKEN_KEY);
		//判断用户是否登录
		if (StringUtils.isBlank(token)) {
			//未登录,重定向到登录页面
			response.sendRedirect(SSO_URL+"?redirect="+request.getRequestURL());
			//不放行
			return false;
		}
		//根据token从redis中取用户信息调用sso中的服务
		E3Result e3Result = tokenService.getUserByToken(token);
		//判断用户信息是否过期
		if (e3Result.getStatus() != 200) {
			//过期跳转到登录页面
			response.sendRedirect(SSO_URL+"?redirect="+request.getRequestURL());
			//不放行
			return false;
		}
		//将用户信息存入request中。方便后续订单使用
		TbUser user = (TbUser) e3Result.getData();
		request.setAttribute("user", user);
		//从cookie中取cart。
		String json = CookieUtils.getCookieValue(request, COOKIE_CART, true);
		if (StringUtils.isNotBlank(json)) {
			List<TbItem> cartList = JsonUtils.jsonToList(json, TbItem.class);
			//合并购物车
			cartService.mergeCart(user.getId(), cartList);
			//清空cookie中的购物车
			CookieUtils.deleteCookie(request, response, COOKIE_CART);
		}
		
		//放行
		return true;
	}


	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub

	}


}
