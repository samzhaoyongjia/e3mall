package cn.e3mall.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemDescService;

@Service
@Transactional
public class ItemDescServiceImpl implements ItemDescService {
	
	//注入dao
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	/**
	 * 根据itemId获取商品描述信息
	 */
	@Override
	public E3Result getItemDescByItemId(Long itemId) {
		//查询商品描述
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		//封装结果集
		E3Result result = new E3Result(itemDesc);
		return result;
	}

}
