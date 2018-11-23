package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.service.ItemCatService;

/**
 * 商品目录列表
 * @author 罗小黑
 *
 */
@Controller
public class ItemCatController {
	//注入service
	@Autowired
	private ItemCatService itemCatService;
	
	/**
	 * 根据父id查询子商品目录列表 
	 * 设置其默认参数为0.默认查询顶级目录
	 * @return
	 */
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(
			@RequestParam(value = "id",defaultValue = "0")Long parentId){
		//根据父id查询子商品列表
		List<EasyUITreeNode> list = itemCatService.getItemCatList(parentId);
		return list;
	}
}
