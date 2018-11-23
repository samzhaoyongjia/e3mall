package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService {
	
	//注入dao
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	/**
	 * 根据父id查询子商品列表目录集合
	 */
	@Override
	public List<EasyUITreeNode> getItemCatList(Long parentId) {
		//1 创建一个集合用来储存返回值
		List<EasyUITreeNode> list = new ArrayList<EasyUITreeNode>();
		
		//2,通过父id查询数据库获得子商品列表目录集合
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> itemCatList = itemCatMapper.selectByExample(example);
		
		//3,将集合封装到EasyUITreeNode对象中遍历返回
		for (TbItemCat tbItemCat : itemCatList) {
			//创建商品列表目录对象
			EasyUITreeNode treeNode = new EasyUITreeNode();
			treeNode.setId(tbItemCat.getId());
			treeNode.setText(tbItemCat.getName());
			//三元运算符判断是否有还有子目录。open为没有，closed为有在EasyUITree中 
			//而条件查询已经把结果1设置为true 0为false
			treeNode.setState(tbItemCat.getIsParent()?"closed":"open");
			
			list.add(treeNode);
		}
		return list;
	}

}
