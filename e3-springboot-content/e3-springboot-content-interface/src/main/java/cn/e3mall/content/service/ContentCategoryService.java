package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCatList(Long parentId);

	E3Result createCategory(Long parentId, String name);

	E3Result updateCategory(Long id, String name);

	E3Result deleteCategory(Long id);

}
