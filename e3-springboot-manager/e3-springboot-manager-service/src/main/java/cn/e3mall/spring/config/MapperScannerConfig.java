package cn.e3mall.spring.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mapper接口的扫描
 * @author 罗小黑
 *
 */
@Configuration
@AutoConfigureAfter(MyBatisConfig.class)//保证在MyBatisConfig实例化之后再实例化该类
public class MapperScannerConfig {
	
	/**
	 * Mapper接口的扫描器
	 * @return
	 */
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(){
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		//设置扫描接口所在的包名
		mapperScannerConfigurer.setBasePackage("cn.e3mall.mapper");
		return mapperScannerConfigurer;
	}

}
