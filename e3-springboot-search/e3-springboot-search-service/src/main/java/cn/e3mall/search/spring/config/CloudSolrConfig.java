package cn.e3mall.search.spring.config;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * solr集群版配置
 * @author 罗小黑
 *
 */
@Configuration
public class CloudSolrConfig {
	
	@Bean
	public CloudSolrServer cloudSolrServer(){
		CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.130:2181,192.168.25.130:2182,192.168.25.130:2183");
		cloudSolrServer.setDefaultCollection("collection2");
		return cloudSolrServer;
	}
	
}
