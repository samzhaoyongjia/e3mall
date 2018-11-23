package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
/**
 * 搜索商品
 * @author 罗小黑
 *
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.service.SearchItemService;
@Controller
public class SearchItemController {
	
	@Autowired
	private SearchItemService searchItemService;
	
	/**
	 * 一键导入商品列表到索引库
	 * @return
	 */
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importItems(){
		E3Result e3Result = searchItemService.importItems();
		return e3Result;
	}
}
