package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;

/**
 * 注册功能
 * @author 罗小黑
 *
 */
@Controller
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;
	
	/**
	 * 注册页面显示
	 * @return
	 */
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	
	/**
	 * 注册前用户名和手机号重复校验
	 * @param param 用户名或者手机号
	 * @param type 识别用户名和手机号的属性
	 * @return E3result
	 */
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param,@PathVariable Integer type){
		E3Result e3Result = registerService.checkData(param, type);
		return e3Result;
	}
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	@RequestMapping("/user/register")
	@ResponseBody
	public E3Result register(TbUser user){
		E3Result e3Result = registerService.register(user);
		return e3Result;
	}
	
}
