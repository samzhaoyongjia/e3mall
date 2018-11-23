package cn.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;

/**
 * 搜寻商品service
 * @author 罗小黑
 *
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {
	
	@Autowired
	private ItemMapper itemMapper;
	
	//注入索引库对象。
	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 一键导入商品到索引库
	 */
	@Override
	public E3Result importItems() {
		try {
			//从数据库中查询到需要导入索引库中的数据
			List<SearchItem> itemList = itemMapper.getItemList();
			//遍历数据
			for (SearchItem searchItem : itemList) {
				//创建文档对象
				SolrInputDocument document = new SolrInputDocument();
				//将数据存入文档对象 要与索引库配置的域名一致
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				//将文档对象添加到索引库服务对象
				solrServer.add(document);
			}
			//提交数据
			solrServer.commit();
			//返回成功结果集
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			//出错。返回失败结果集
			return E3Result.build(300, "error");
		}
	
	}

}
