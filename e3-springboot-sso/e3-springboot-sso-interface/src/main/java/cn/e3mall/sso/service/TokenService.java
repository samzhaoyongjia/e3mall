package cn.e3mall.sso.service;

import cn.e3mall.common.pojo.E3Result;

public interface TokenService {
	
	E3Result getUserByToken(String token);
	
	void deleteUserByToken(String token);
	
}
