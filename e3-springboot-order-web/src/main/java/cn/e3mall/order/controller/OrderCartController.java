package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderCartService;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;

/**
 * 订单表现层
 * @author 罗小黑
 *
 */
@Controller
public class OrderCartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderCartService orderCartService;
	
	/**
	 * 展示订单
	 * @param request
	 * @return
	 */
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		//获取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		//取得用户购物车中的商品
		List<TbItem> cartList = cartService.getCartList(user.getId());
		//将商品传递给jsp
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "order-cart";
	}
	
	/**
	 * 创建订单
	 * @param orderInfo
	 * @param request
	 * @return
	 */
	@RequestMapping("/order/create")
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request){
		//获得用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		//调用service层进行订单创建
		E3Result e3result = orderCartService.creatOrder(orderInfo, user);
		//清空购物车
		cartService.cleanCart(user.getId());
		//将订单id和payment传递给页面
		request.setAttribute("orderId", e3result.getData().toString());
		request.setAttribute("payment", orderInfo.getPayment());
		//返回成功逻辑视图
		return "success";
	}
}
