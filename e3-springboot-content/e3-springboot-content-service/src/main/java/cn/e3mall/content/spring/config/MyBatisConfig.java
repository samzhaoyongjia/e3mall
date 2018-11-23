package cn.e3mall.content.spring.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * MyBaits与spring整合配置
 * @author 罗小黑
 *
 */
@Configuration
public class MyBatisConfig {
	
	/**
	 * 让spring管理sqlsessionfactory 使用mybatis和spring整合包中的SqlSessionFactoryBean
	 * 需要spring中的数据源和Mybaits配置文件 直接在方法中添加属性，就可以从spring中提取数据源
	 * @param dataSource
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean //当容器里没有定义的bean情况下创建该对象
	public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource){
		//创建SqlSessionFactoryBean对象
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		//向对象注入数据源
		sqlSessionFactoryBean.setDataSource(dataSource);
		//设置MyBaits配置文件
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource mybatisConfigXml = resolver.getResource("classpath:mybatis/SqlMapConfig.xml");
		sqlSessionFactoryBean.setConfigLocation(mybatisConfigXml);
		return sqlSessionFactoryBean;
	}

}
