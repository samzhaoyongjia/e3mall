package cn.e3mall.search.listener;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Controller;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.ItemMapper;

/**
 * ActiveMQ消息监听。接收到消息的后续处理
 * @author 罗小黑
 *
 */
@Controller
public class MyMessageListener{
	
	//注入dao
	@Autowired
	private ItemMapper ItemMapper;
	//注入索引库对象
	@Autowired
	private SolrServer solrServer;
	
	/**
	 * 监听ActiveMQ发送的消息
	 * #默认情况下activemq提供的是queue模式，若要使用topic模式需要在application中配置下面配置
	 * spring.jms.pub-sub-domain=true
	 * @param text
	 */
	@JmsListener(destination = "item-change-topic")
    public void receiveQueue(String text) {
		try {
			Long itemId = new Long(text);
			//等待事务提交。因为事务提交之前发送的id可能会导致根据id查不到数据报错,让线程等待一秒
			Thread.sleep(1000);
			//通过id查询数据库
			SearchItem searchItem = ItemMapper.getSearchItemById(itemId);
			//创建索引库input文档对象
			SolrInputDocument document = new SolrInputDocument();
			//添加域和值
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			//将文档写入索引库
			solrServer.add(document);
			//提交
			solrServer.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
