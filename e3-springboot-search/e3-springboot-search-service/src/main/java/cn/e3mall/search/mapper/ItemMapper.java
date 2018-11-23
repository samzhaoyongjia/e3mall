package cn.e3mall.search.mapper;

import java.util.List;

import cn.e3mall.common.pojo.SearchItem;

/**
 * 自定义sql语句查询商品，用于索引库数据
 * @author 罗小黑
 *
 */
public interface ItemMapper {
	
	//查询solr索引库导入所需数据
	List<SearchItem> getItemList();
	
	//根据id查询SearchItem对象
	SearchItem getSearchItemById(Long itemId);
}
