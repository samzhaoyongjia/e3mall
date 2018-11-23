package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;


/**
 * 商品管理controller
 * @author 罗小黑
 *
 */
@Controller
public class ItemController {
	
	//注入service
	@Autowired
	private ItemService itemService;
	
	/**
	 * 根据id查询商品
	 * @param itemId
	 * @return itemjson
	 */
	@RequestMapping("/item/{itemId}")
	@ResponseBody
	public TbItem getItemById(@PathVariable Long itemId){
		TbItem item = itemService.getItemById(itemId);
		return item;
	}
	
	/**
	 * 分页查询
	 * @return
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		EasyUIDataGridResult result = itemService.getItemList(page,rows);
		return result;
	}
	
	/**
	 * 保存新增商品
	 * @param item 封装的商品对象
	 * @param desc 富文本商品详细信息
	 * @return 返回成功结果集
	 */
	@RequestMapping("/item/save")
	@ResponseBody
	public E3Result saveItem(TbItem item,String desc){
		E3Result result = itemService.saveItem(item,desc);
		return result;
	}
	
	/**
	 * 更新商品信息
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public E3Result updateItem(TbItem item,String desc){
		E3Result result = itemService.updateItem(item,desc);
		return result;
	}
	
	/**
	 * 删除商品
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/delete")
	@ResponseBody
	public E3Result deleteItem(Integer[] ids){
		E3Result result = itemService.deleteItem(ids);
		return result;
	}
	
	/**
	 * 下架商品
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public E3Result instockItem(Integer[] ids){
		E3Result result = itemService.instockItem(ids);
		return result;
	}
	
	/**
	 * 上架商品
	 * @param ids
	 * @return
	 */
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public E3Result reshelfItem(Integer[] ids){
		E3Result result = itemService.reshelfItem(ids);
		return result;
	}

}
