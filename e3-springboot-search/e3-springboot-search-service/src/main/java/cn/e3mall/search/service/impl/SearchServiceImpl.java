package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

/**
 * 首页搜索商品service
 * @author 罗小黑
 *
 */
@Service
public class SearchServiceImpl implements SearchService {
	
	//注入索引库dao
	@Autowired
	private SearchDao searchDao;
	
	//注入默认查询域。解决硬编码问题
	@Value("${DEFAULT_FIELD}")
	private String DEFAULT_FIELD;
	

	@Override
	public SearchResult search(String keyWord, int page, int rows) throws Exception {
		//创建索引查询条件对象
		SolrQuery solrQuery = new SolrQuery();
		
		//封装查询条件
		 //查询的内容
		solrQuery.setQuery(keyWord);
		 //默认查询的key
		solrQuery.set("df", DEFAULT_FIELD);
		 //查询的条数，以及从哪里开始查
		solrQuery.setStart((page-1)*rows);
		solrQuery.setRows(rows);
		 //开启高亮
		solrQuery.setHighlight(true);
		 //设置高亮的key
		solrQuery.addHighlightField("item_title");
		 //设置高亮的前后样式
		solrQuery.setHighlightSimplePre("<em style = \"color:red \">");
		solrQuery.setHighlightSimplePost("</em>");
		
		//查询索引库
		SearchResult searchResult = searchDao.search(solrQuery);
		
		//计算出总页数
		int totalCount = searchResult.getRecourdCount();
		double d = (double)totalCount/rows;
		int totalPages = (int) Math.ceil(d);
		
		//封装结果集返回
		searchResult.setTotalPages(totalPages);
		
		return searchResult;
	}

}
