package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
/**
 * 购物车服务层
 * @author 罗小黑
 *
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CART_REDIS_KEY}")
	private String CART_REDIS_KEY;
	
	/**
	 * 添加购物车商品到redis缓存
	 */
	@Override
	public E3Result addCart(Long userId, Long itemId, Integer num) {
		//判断redis中是否已经含有该商品
		Boolean hexists = jedisClient.hexists(CART_REDIS_KEY +":"+userId, itemId + "");
		if (hexists) {
			//如果存在则将商品信息num相加
			String json = jedisClient.hget(CART_REDIS_KEY +":"+userId, itemId + "");
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			tbItem.setNum(tbItem.getNum()+num);
			//写入hash
			jedisClient.hset(CART_REDIS_KEY +":"+userId, itemId + "", JsonUtils.objectToJson(tbItem));
			//返回添加ok
			return E3Result.ok();
		}
		//redis中没有该商品
		//根据itemId查询item商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//更改商品的数量以及图片信息
		item.setNum(num);
		if (StringUtils.isNotBlank(item.getImage())) {
			item.setImage(item.getImage().split(",")[0]);
		}
		//将商品以hash的方式保存到redis缓存中
		jedisClient.hset(CART_REDIS_KEY +":"+userId, itemId + "", JsonUtils.objectToJson(item));
		
		return E3Result.ok();
	}
	
	/**
	 * 合并cookie与redis中的购物车
	 */
	@Override
	public E3Result mergeCart(Long userId, List<TbItem> cartList) {
		//因为合并与添加几乎相同。则调用add方法,将cookie中的商品保存到redis数据库
		for (TbItem tbItem : cartList) {
			this.addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		//返回合并成功
		return E3Result.ok();
	}
	
	/**
	 * 根据userid查询redis中购物车商品
	 */
	@Override
	public List<TbItem> getCartList(Long userId) {
		//根据key查询所有value
		List<String> jsons =  jedisClient.hvals(CART_REDIS_KEY +":"+userId);
		//循环遍历转换成itme存入list中
		List<TbItem> list = new ArrayList<>();
		for (String json : jsons) {
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			list.add(item);
		}
		return list;
	}
	
	/**
	 * 更新购物车商品购买数量
	 */
	@Override
	public E3Result updateCartItemNum(Long userId, Long itemId, Integer num) {
		//从redis中取出对应的商品
		String json = jedisClient.hget(CART_REDIS_KEY +":"+userId, itemId + "");
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		//更改购买商品数量
		item.setNum(num);
		//转换成json从新存入redis换缓存中
		jedisClient.hset(CART_REDIS_KEY +":"+userId, itemId + "", JsonUtils.objectToJson(item));
		return E3Result.ok();
	}
	
	/**
	 * 删除redis购物车中的商品
	 */
	@Override
	public E3Result deleteCartItem(Long userId, Long itemId) {
		jedisClient.hdel(CART_REDIS_KEY +":"+userId, itemId + "");
		return E3Result.ok();
	}
	
	/**
	 * 清空购物车
	 */
	@Override
	public E3Result cleanCart(Long userId) {
		jedisClient.del(CART_REDIS_KEY +":"+userId);
		return E3Result.ok();
	}

}
