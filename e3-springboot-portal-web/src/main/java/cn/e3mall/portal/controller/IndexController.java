package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 首页显示
 * @author 罗小黑
 *
 */
@Controller
public class IndexController {
	
	//注入配置文件中的categoryId
	@Value("${CONTENT_CATEGORY_ID}")
	private Long CONTENT_CATEGORY_ID;
	
	//注入contentservice
	@Autowired
	private ContentService contentService;
	
	/**
	 * 首页显示，地址问index.html.访问/自动跳转
	 * @return
	 */
	@RequestMapping("/index")
	public String showIndex(Model model){
		//首页轮播图动态显示
		List<TbContent> list = contentService.getListByCategoryId(CONTENT_CATEGORY_ID);
		model.addAttribute("ad1List", list);
		return "index";
	}
	
}
