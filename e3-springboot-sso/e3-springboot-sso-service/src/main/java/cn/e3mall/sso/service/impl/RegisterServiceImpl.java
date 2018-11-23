package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

@Service
@Transactional
public class RegisterServiceImpl implements RegisterService {
	
	@Autowired
	private TbUserMapper userMapper;

	/**
	 * 注册前校验用户名和手机号是否重复
	 */
	@Override
	public E3Result checkData(String param, int type) {
		//创建查询条件对象
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//添加查询条件
		//1 代表username 2 代表phone
		if (type == 1) {
			//username校验
			criteria.andUsernameEqualTo(param);
		}
		if (type == 2) {
			//phonr校验
			criteria.andPhoneEqualTo(param);
		}
		//执行查询。
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size()==0) {
			return E3Result.ok(true);
		}
		return E3Result.ok(false);
	}
	
	
	/**
	 * 注册用户
	 */
	@Override
	public E3Result register(TbUser user) {
		//后台校验。确保数据不为空
		if (StringUtils.isBlank(user.getUsername())) {
			return E3Result.build(400, "用户名不能为空");
		}
		if (StringUtils.isBlank(user.getPassword())) {
			return E3Result.build(400, "密码不能为空");
		}
		if (StringUtils.isBlank(user.getPhone())) {
			return E3Result.build(400, "手机号不能为空");
		}
		//校验数据是否可用
		E3Result e3Result = this.checkData(user.getUsername(), 1);
		if (!(boolean) e3Result.getData()) {
			return E3Result.build(400, "用户名已被使用");
		}
		e3Result = this.checkData(user.getPhone(), 2);
		if (!(boolean) e3Result.getData()) {
			return E3Result.build(400, "手机号已被使用");
		}
		
		//补全user
		Date date = new Date();
		user.setCreated(date);
		user.setUpdated(date);
		//使用springDigestUtils工具类里面的md5加密密码
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password);
		
		//将注册数据插入数据库
		userMapper.insert(user);
		return E3Result.ok();
	}
	
	

}
