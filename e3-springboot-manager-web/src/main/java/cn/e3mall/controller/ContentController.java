package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 内容管理
 * @author 罗小黑
 *
 */

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	/**
	 * 分页查询
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentListByCategoryId(Long categoryId,Integer page,Integer rows){
		EasyUIDataGridResult result = contentService.getContentListByCategoryId(categoryId,page,rows);
		return result;
	}
	
	/**
	 * 保存内容
	 * @param content
	 * @return
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result saveContent(TbContent content){
		E3Result result = contentService.saveContent(content);
		return result;
	}
	
	/**
	 * 修改内容
	 * @param content
	 * @return
	 */
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public E3Result editContent(TbContent content){
		E3Result result = contentService.editContent(content);
		return result;
	}
	
	/**
	 * 删除内容
	 * @param ids
	 * @return
	 */
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3Result deleteContent(Integer[] ids){
		E3Result result = contentService.deleteContent(ids);
		return result;
	}
	
}
