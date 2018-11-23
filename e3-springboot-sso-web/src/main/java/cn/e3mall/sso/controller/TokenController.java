package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.sso.service.TokenService;

@Controller
public class TokenController {
	
	@Autowired
	private TokenService tokenService;
	
	/**
	 * 根据token获取用户登录信息。因为涉及到ajax跨域。使用jsonp方式
	 * @return
	 */
	@RequestMapping("user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback){
		//获取用户返回结果集
		E3Result e3Result = tokenService.getUserByToken(token);
		//判断是否为jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			//通过springmvc封装的MappingJacksonValue，把结果封装成一个js语句响应
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(e3Result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return e3Result;
	}
	
	/**
	 * 用户登出功能，跳转到首页
	 * @param token
	 * @return
	 */
	@RequestMapping("/user/logout/{token}")
	public String deleteByToken(@PathVariable String token){
		tokenService.deleteUserByToken(token);
		return "redirect:http://localhost:8082";
	}
	
	
}
