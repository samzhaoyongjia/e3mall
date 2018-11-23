package cn.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.LoginService;

/**
 * 用户登录表现层
 * @author 罗小黑
 *
 */
@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	/**
	 * 用户登录
	 */
	@Override
	public E3Result login(String username, String password) {
		//判断用户名是否正确
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		//根据用户名查询
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			//根据手机号查询
			TbUserExample exampleByPhone = new TbUserExample();
			Criteria criteriaByPhone = exampleByPhone.createCriteria();
			criteriaByPhone.andPhoneEqualTo(username);
			list = userMapper.selectByExample(exampleByPhone);
			if (list == null || list.size() == 0) {
				return E3Result.build(400, "用户名或密码错误");
			}
		}
		//取出查询到的user
		TbUser user = list.get(0);
		//判断密码是否正确
		password = DigestUtils.md5DigestAsHex(password.getBytes());
		if (!user.getPassword().equals(password)) {
			return E3Result.build(400, "用户名或密码错误");
		}
		
		//解决登录session问题
		//随机生成token。也就是ridis缓冲中的key
		String token = UUID.randomUUID().toString();
		//将token key 和用户信息存入缓存中，将密码除去
		user.setPassword(null);
		jedisClient.set("SESSION:" + token, JsonUtils.objectToJson(user));
		//设置token key过期时间，因为登录有过期
		jedisClient.expire("SESSION:" + token, SESSION_EXPIRE);
		//将token包装到E3Result中返回。
		return E3Result.ok(token);
	}

}
