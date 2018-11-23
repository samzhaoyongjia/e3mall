package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;

/**
 * 内容分类的树
 * @author 罗小黑
 *
 */
@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	/**
	 * 获取内容分类节点对象，以EasyUITreeNode的类型返回
	 * @param parentId 根据父id来查找子节点集合
	 * @return EasyUITreeNode 树所要的结果集
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(@RequestParam(value = "id",defaultValue = "0")Long parentId){
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		return list;
	}
	
	/**
	 * 添加内容分类节点
	 * @param parentId 要添加节点的父id
	 * @param name	添加节点的名称
	 * @return result e3结果集
	 */
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result createCategory(Long parentId,String name){
		E3Result result = contentCategoryService.createCategory(parentId,name);
		return result;
	}
	
	/**
	 * 重命名内容分类节点
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result updateCategory(Long id,String name){
		E3Result result = contentCategoryService.updateCategory(id,name);
		return result;
	}
	
	/**
	 * 删除节点
	 * @param id
	 * @return
	 */
	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public E3Result deleteCategory(Long id){
		E3Result result = contentCategoryService.deleteCategory(id);
		return result;
	}

}
