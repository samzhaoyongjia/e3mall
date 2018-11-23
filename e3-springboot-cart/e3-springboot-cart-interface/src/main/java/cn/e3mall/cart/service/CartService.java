package cn.e3mall.cart.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItem;

public interface CartService {
	
	E3Result addCart(Long userId,Long itemId,Integer num);

	E3Result mergeCart(Long userId, List<TbItem> cartList);

	List<TbItem> getCartList(Long userId);

	E3Result updateCartItemNum(Long userId, Long itemId, Integer num);

	E3Result deleteCartItem(Long userId, Long itemId);
	
	E3Result cleanCart(Long userId);
	
}
