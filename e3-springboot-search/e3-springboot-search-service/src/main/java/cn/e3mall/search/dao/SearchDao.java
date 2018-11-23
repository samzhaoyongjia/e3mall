package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

/**
 * 搜索商品复杂查询索引库dao
 * @author 罗小黑
 *
 */
@Repository
public class SearchDao {
	
	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 根据查询条件对象查询索引库
	 * @param query
	 * @return
	 * @throws Exception 
	 */
	public SearchResult search(SolrQuery query) throws Exception{
		//根据查询条件查询索引库。
		QueryResponse queryResponse = solrServer.query(query);
		//通过查询请求获得查询结果集
		SolrDocumentList results = queryResponse.getResults();
		//获得查询总条数
		Long recourdCount = results.getNumFound();
		//创建返回结果集对象
		SearchResult searchResult = new SearchResult();
		//将总页数封装进结果集对象
		searchResult.setRecourdCount(recourdCount.intValue());
		//创建商品列表对象
		List<SearchItem> ItemList = new ArrayList<>();
		//取高亮的结果map
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		//遍历SolrDocumentList取商品列表
		for (SolrDocument solrDocument : results) {
			//创建搜索商品对象
			SearchItem item = new SearchItem();
			//封装商品对象
			item.setId((String) solrDocument.get("id"));
			item.setImage((String) solrDocument.get("item_image"));
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			item.setPrice((Long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			//根据id和高亮显示位置key取高亮的结果
			List<String> titleList = highlighting.get(solrDocument.get("id")).get("item_title");
			//判断是否为空。因为一个id对应一个title。titleList只能有一条数据
			String itemTitle = "";
			if (titleList !=null && titleList.size()>0) {
				itemTitle = titleList.get(0);
			}else {
				//如果没有加高亮，就取出普通的标题
				itemTitle = (String) solrDocument.get("item_title");
			}
			item.setTitle(itemTitle);
			//将商品对象装入list中
			ItemList.add(item);
		}
		//将商品集合封装到结果集中
		searchResult.setItemList(ItemList);
		
		return searchResult;
	}
	
}
