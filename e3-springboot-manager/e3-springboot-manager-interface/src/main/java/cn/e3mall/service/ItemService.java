package cn.e3mall.service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;

public interface ItemService {

	TbItem getItemById(Long itemId);

	EasyUIDataGridResult getItemList(Integer page, Integer rows);

	E3Result saveItem(TbItem item, String desc);

	E3Result updateItem(TbItem item, String desc);

	E3Result deleteItem(Integer[] ids);

	E3Result instockItem(Integer[] ids);

	E3Result reshelfItem(Integer[] ids);
	
	TbItemDesc getItemDescByItemId(Long itemId);

}
