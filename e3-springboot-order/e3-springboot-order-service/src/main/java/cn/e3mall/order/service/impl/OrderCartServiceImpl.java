package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderCartService;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;
import cn.e3mall.pojo.TbUser;
/**
 * 订单服务层
 * @author 罗小黑
 *
 */
@Service
@Transactional
public class OrderCartServiceImpl implements OrderCartService {
	
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper OprderShippingMapper;
	
	//订单id key
	@Value("${ORDER_GEN_KEY}")
	private String ORDER_GEN_KEY;
	//订单id初始值
	@Value("${ORDER_ID_BEGIN}")
	private String ORDER_ID_BEGIN;
	//订单商品明细id key
	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	public String ORDER_ITEM_ID_GEN_KEY;

	/**
	 * 创建订单
	 */
	@Override
	public E3Result creatOrder(OrderInfo orderInfo, TbUser user) {
		//封装order表中的数据
		 //订单id。采用redis自增长形式
		  //判断是否是第一个用户
		if (!jedisClient.exists(ORDER_GEN_KEY)) {
			//第一个等于用户，设置初始值
			jedisClient.set(ORDER_GEN_KEY, ORDER_ID_BEGIN);
		}
		  //不是第一个用户。则设定key自增
		String orderId = jedisClient.incr(ORDER_GEN_KEY).toString();
		  //存入pojo中
		orderInfo.setOrderId(orderId);
		 //订单支付状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭'
		orderInfo.setStatus(1);
		 //订单创建于更新时间
		Date date = new Date();
		orderInfo.setCreateTime(date);
		orderInfo.setUpdateTime(date);
		 //订单买家昵称
		orderInfo.setBuyerNick(user.getUsername());
		 //订单买家id
		orderInfo.setUserId(user.getId());
		//向order表中插入数据
		orderMapper.insert(orderInfo);
		
		//封装orderItem中的数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem orderItem : orderItems) {
			//生成订单商品详情id
			Long orderItemId = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY);
			orderItem.setId(orderItemId.toString());
			//设置订单id
			orderItem.setOrderId(orderId);
			//插入数据
			orderItemMapper.insert(orderItem);
		}
		
		//封装orderShipping对象
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		 //订单id
		orderShipping.setOrderId(orderId);
		 //创建更新时间
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		//插入数据
		OprderShippingMapper.insert(orderShipping);
		
		return E3Result.ok(orderId);
	}

}
