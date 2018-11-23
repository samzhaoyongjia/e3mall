package cn.e3mall.order.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.pojo.TbUser;

public interface OrderCartService {
	
	E3Result creatOrder(OrderInfo orderInfo,TbUser user);
	
}
