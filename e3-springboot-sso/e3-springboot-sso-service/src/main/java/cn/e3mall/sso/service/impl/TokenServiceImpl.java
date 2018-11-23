package cn.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {
	
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	/**
	 * 根据token获取redis缓存中的用户登录信息
	 */
	@Override
	public E3Result getUserByToken(String token) {
		
		//获取登录用户信息
		String json = jedisClient.get("SESSION:" + token);
		//判断缓存是否还生效
		if (StringUtils.isBlank(json)) {
			return E3Result.build(400, "用户登录已经过期，请重新登录");
		}
		//重置token的过期时间
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		//查询到的json转成user
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		//信息封装到E3Result中返回
		return E3Result.ok(user);
	}
	
	/**
	 * 根据token删除redis缓存中的用户登录信息
	 */
	@Override
	public void deleteUserByToken(String token) {
		jedisClient.del("SESSION:" + token);
	}

}
