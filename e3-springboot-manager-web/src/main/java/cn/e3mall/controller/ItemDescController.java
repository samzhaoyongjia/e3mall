package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.service.ItemDescService;

/**
 * 商品描述富文本controller
 * @author 罗小黑
 *
 */
@Controller
public class ItemDescController {
	
	//注入service
	@Autowired
	private ItemDescService itemDescService;
	
	/**
	 * 根据itemId获取商品描述
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public E3Result getItemDescByItemId(@PathVariable Long itemId){
		E3Result result = itemDescService.getItemDescByItemId(itemId);
		return result;
	}
}
