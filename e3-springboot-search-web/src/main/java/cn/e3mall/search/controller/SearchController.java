package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

/**
 * 首页搜索商品controller层
 * @author 罗小黑
 *
 */
@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	//配置文件注入每页显示条数，防止硬编码
	@Value("${PAGE_ROWS}")
	private Integer PAGE_ROWS;
	
	/**
	 * 根据首页查询信息查询索引库商品 并实现分页
	 * @param keyword 搜索条件
	 * @param page 当前页。默认第一页
	 * @return 跳转到商品展示页面
	 * @throws Exception 
	 */
	@RequestMapping("/search")
	public String search(String keyword,@RequestParam(defaultValue = "1")Integer page,Model model) throws Exception{
		
		//从service层获得页面所需要参数
		SearchResult searchResult = searchService.search(keyword, page, PAGE_ROWS);
		//将信息传递给页面
		 //回显搜索信息
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages",searchResult.getTotalPages());
		model.addAttribute("page",page);
		model.addAttribute("recourdCount",searchResult.getRecourdCount());
		model.addAttribute("itemList",searchResult.getItemList());
		
		return "search";
	}
}
