package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.sso.service.LoginService;

/**
 * 用户登录
 * @author 罗小黑
 *
 */
@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;
	@Value("${COOKIE_TOKEN_KEY}")
	private String COOKIE_TOKEN_KEY;
	
	/**
	 * 登录页面展示
	 * @return
	 */
	@RequestMapping("/page/login")
	public String showLogin(){
		return "login";
	}
	
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/user/login",method = RequestMethod.POST)
	@ResponseBody
	public E3Result login(String username,String password,HttpServletRequest request,HttpServletResponse response){
		E3Result e3Result = loginService.login(username, password);
		//将token写入cookie发送到客户端
		if (StringUtils.isNotBlank((String)e3Result.getData())) {
			String token = e3Result.getData().toString();
			CookieUtils.setCookie(request, response, COOKIE_TOKEN_KEY, token);
		}
		return e3Result;
	}
}
