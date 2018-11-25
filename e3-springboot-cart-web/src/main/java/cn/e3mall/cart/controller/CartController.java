package cn.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
/**
 * 购物车登录未登录增删改查
 * @author 罗小黑
 *
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;
@Controller
public class CartController {
	//本地仓库添加代码冲突测试
	//远程仓库对代码进行修改冲突测试

	//cookie购物车key
	@Value("${COOKIE_CART}")
	private String COOKIE_CART;
	//cookiekey过期时间
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CartService cartService;
	
	/**
	 * 添加购物车商品
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request,HttpServletResponse response){
		
		//判断用户是否登录
		//从request中取出用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			//已经登录添加购物车商品
			//将商品信息传递到service层进行redis保存处理
			cartService.addCart(user.getId(), itemId, num);
			//返回到添加购物车成功视图页面
			return "cartSuccess";
		}
		
		//未登录添加购物车商品
		//从cookie中取出购物车商品信息
		List<TbItem> cartList = this.getCartList(request);
		//遍历商品 设置记录值，记录添加的商品是否已经存在
		boolean hasItem = false;
		for (TbItem tbItem : cartList) {
			//如果存在添加商品，则num相加 
			//注意这里是Long对象，要转换成基本数据类型才可以比较，不然是比较对象的地址值
			if (tbItem.getId()==itemId.longValue()) {
				tbItem.setNum(tbItem.getNum()+num);
				//设置为存在
				hasItem = true;
				//找到商品，跳出循环
				break;
			}
		}
		//如果不存在，则添加新的商品对象到list中
		if (!hasItem) {
			//通过商品id查询商品详情信息
			TbItem item = itemService.getItemById(itemId);
			//更改购买数量
			item.setNum(num);
			//去一张商品图片
			if (StringUtils.isNotBlank(item.getImage())) {
				item.setImage(item.getImage().split(",")[0]);
			}
			
			cartList.add(item);
		}
		
		//再将list转换成cookie存储到客户端,设置过期时间
		CookieUtils.setCookie(request, response, COOKIE_CART, JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		
		return "cartSuccess";
	}
	
	/**
	 * 获取cookie中的购物车商品列表方法
	 * @return
	 */
	private List<TbItem> getCartList(HttpServletRequest request){
		//使用cookie工具类,设定其有编码格式
		String json = CookieUtils.getCookieValue(request, COOKIE_CART, true);
		//转换成list
		if (StringUtils.isNotBlank(json)) {
			List<TbItem> itemList = JsonUtils.jsonToList(json, TbItem.class);
			return itemList;
		}
		//没有数据，初始化list容器
		return new ArrayList<>();
	}
	
	/**
	 * 展示购物车页面及其商品
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCart(HttpServletRequest request,HttpServletResponse response){
		//从cookie中获得购物车商品列表
		List<TbItem> cartList = this.getCartList(request);
		
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			//用户登录
			//判断coolie是否为空
			if (!cartList.isEmpty()) {
				//合并cookie与redis中的购物车
				cartService.mergeCart(user.getId(),cartList);
				//清空cookie
				CookieUtils.deleteCookie(request, response, COOKIE_CART);
			}
			//查询redis中缓存的购物车信息
			cartList = cartService.getCartList(user.getId());
		}
		//未登录直接将cookie中的数据写入
		//将商品列表写入request找那个发送到页面
		request.setAttribute("cartList", cartList);
		//返回页面
		return "cart";
	}
	
	/**
	 * 购物车页面添加商品数量
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			//用户登录
			//改变redis中的商品数量
			cartService.updateCartItemNum(user.getId(),itemId,num);
			//返回更新成功
			return E3Result.ok();
		}
		
		//未登录
		//从cookie中获取商品信息。
		List<TbItem> cartList = this.getCartList(request);
		//遍历商品信息
		for (TbItem tbItem : cartList) {
			//判断出数量添加的商品
			if (tbItem.getId() == itemId.longValue()) {
				//更新商品数量
				tbItem.setNum(num);
			}
		}
		//把商品信息重新写入cookie
		CookieUtils.setCookie(request, response, COOKIE_CART, JsonUtils.objectToJson(cartList), 
				COOKIE_CART_EXPIRE, true);
		//返回E3Result
		return E3Result.ok();
	}
	
	/**
	 * 删除购物车中的商品
	 * @return
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,HttpServletResponse response){
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			//用户登录
			//删除redis中的商品
			cartService.deleteCartItem(user.getId(),itemId);
			//返回更新成功
			return "redirect:/cart/cart.html";
		}
		
		//未登录
		//获取cookie中的购物车商品信息。
		List<TbItem> cartList = this.getCartList(request);
		//比对商品id
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				//删除cookie中的商品
				cartList.remove(tbItem);
				break;
			}
		}
		//将商品信息重新写入cookie
		CookieUtils.setCookie(request, response, COOKIE_CART, JsonUtils.objectToJson(cartList), 
				COOKIE_CART_EXPIRE, true);
		//跳转到展示购物车页面
		return "redirect:/cart/cart.html";
	}
}
