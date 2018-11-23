package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
@Transactional
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	//注入dao
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	/**
	 * 根据父id查询子节点
	 */
	@Override
	public List<EasyUITreeNode> getContentCatList(Long parentId) {
		//根据父id查询子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> contentCategoryList = contentCategoryMapper.selectByExample(example);
		
		//创建结果集list集合封装参数
		List<EasyUITreeNode> list = new ArrayList<>();
		for (TbContentCategory ContentCategory : contentCategoryList) {
			//创建树结果集对象，进行封装
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(ContentCategory.getId());
			node.setText(ContentCategory.getName());
			node.setState(ContentCategory.getIsParent()?"closed":"open");
			//将结果集封装到集合中
			list.add(node);
		}
		
		return list;
	}
	
	/**
	 * 添加节点
	 */
	@Override
	public E3Result createCategory(Long parentId, String name) {
		//1,创建节点对象。封装参数
		TbContentCategory category = new TbContentCategory();
		category.setParentId(parentId);
		category.setName(name);
		 //该类目是否为父类目，1为true，0为false
		category.setIsParent(false);
		 //如数值相等则按名称次序排列。取值范围:大于零的整数
		category.setSortOrder(1);
		 //状态。可选值:1(正常),2(删除)
		category.setStatus(1);
		Date date = new Date();
		category.setCreated(date);
		category.setUpdated(date);
		
		//2,通过dao将节点对象保存到数据库。
		contentCategoryMapper.insert(category);
		
		//3,通过parentId获取父节点。
		TbContentCategory parentCategory = contentCategoryMapper.selectByPrimaryKey(parentId);
		
		//4,判断父节点的is_parent状态。
		Boolean isParent = parentCategory.getIsParent();
		if (!isParent) {
			parentCategory.setIsParent(true);
			parentCategory.setUpdated(date);
			contentCategoryMapper.updateByPrimaryKey(parentCategory);
		}
		
		//5,将保存好带有id的子节点封装到结果集返回
		return E3Result.ok(category);
	}
	
	/**
	 * 重命名节点
	 */
	@Override
	public E3Result updateCategory(Long id, String name) {
		//创建节点对象封装
		TbContentCategory category = new TbContentCategory();
		category.setId(id);
		category.setName(name);
		category.setUpdated(new Date());
		//执行更新操作
		contentCategoryMapper.updateByPrimaryKeySelective(category);
		return E3Result.ok();
	}
	
	/**
	 * 删除节点
	 */
	@Override
	public E3Result deleteCategory(Long id) {
		//查询数据库获得该节点，用于判断是否是父节点
		TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(id);
		Boolean isParent = category.getIsParent();
		if (isParent) {
			//是父节点，返回删除失败提示
			return E3Result.build(300, "error");
		}else {
			//不是父节点
			//判断该父节点下面是否还存在其他的子节点
			Long parentId = category.getParentId();
			TbContentCategoryExample example = new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(parentId);
			List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
			if (list.size() == 1) {
				//说明没有其他节点,更改父节点的isparent状态
				TbContentCategory parentCategory = new TbContentCategory();
				parentCategory.setId(parentId);
				parentCategory.setIsParent(false);
				parentCategory.setUpdated(new Date());
				contentCategoryMapper.updateByPrimaryKeySelective(parentCategory);
			}
			//删除子节点
			contentCategoryMapper.deleteByPrimaryKey(id);
			return E3Result.ok();
		}
		
	}

}
