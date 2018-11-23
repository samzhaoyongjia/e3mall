package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	EasyUIDataGridResult getContentListByCategoryId(Long categoryId, Integer page, Integer rows);

	E3Result saveContent(TbContent content);

	E3Result editContent(TbContent content);

	E3Result deleteContent(Integer[] ids);

	List<TbContent> getListByCategoryId(Long CONTENT_CATEGORY_ID);

	

}
