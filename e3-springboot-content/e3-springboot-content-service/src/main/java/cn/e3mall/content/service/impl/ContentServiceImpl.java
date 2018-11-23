package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

@Service
@Transactional
public class ContentServiceImpl implements ContentService {
	
	@Autowired
	private TbContentMapper contentMapper;
	
	//注入redis缓存接口
	@Autowired
	private JedisClient jedisClient;
	
	//注入contentList在redis中key
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	/**
	 * 分页查询内容列表
	 */
	@Override
	public EasyUIDataGridResult getContentListByCategoryId(Long categoryId, Integer page, Integer rows) {
		//使用pagehelper插件进行分页查询
		PageHelper.startPage(page, rows);
		//设置查询条件
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		//执行查询操作
		List<TbContent> list = contentMapper.selectByExample(example);
		//查询之后的list交由pagehelper插件处理
		PageInfo<TbContent> info = new PageInfo<>(list);
		//获得总条数
		long total = info.getTotal();
		//封装分页对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setTotal((int) total);
		result.setRows(list);
		
		return result;
	}
	
	/**
	 * 保存内容
	 * 并将redis中的缓存清空。确保首页显示正确
	 */
	@Override
	public E3Result saveContent(TbContent content) {
		//补全content
		Date date = new Date();
		content.setCreated(date);
		content.setUpdated(date);
		//执行保存
		contentMapper.insert(content);
		//返回成功结果集
		
		//将redis中的缓存清空 首页从新加载
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId() + "");
		
		return E3Result.ok();
	}
	
	/**
	 * 修改内容
	 * 并将redis中的缓存清空。确保首页显示正确
	 */
	@Override
	public E3Result editContent(TbContent content) {
		//更改修改时间
		content.setUpdated(new Date());
		if ("".equals(content.getContent())) {
			content.setContent(null);
		}
		//执行修改操作
		contentMapper.updateByPrimaryKeySelective(content);
		
		//将redis中的缓存清空 首页从新加载
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId() + "");
		
		return E3Result.ok();
	}
	
	/**
	 * 删除内容
	 * 并将redis中的缓存清空。确保首页显示正确
	 */
	@Override
	public E3Result deleteContent(Integer[] ids) {
		//遍历ids
		for (Integer id : ids) {
			
			//为了清空对应的缓存，先根据id查询categoryId
			TbContent content = contentMapper.selectByPrimaryKey(id.longValue());

			//根据id删除
			contentMapper.deleteByPrimaryKey(id.longValue());
			
			//将redis中的缓存清空 首页从新加载
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId() + "");
		}
	
		return E3Result.ok();
	}
	
	/**
	 * 根据categoryId查询内容
	 * 使用jedis为图片加入缓存，单机和集群版在配置文件中切换
	 */
	@Override
	public List<TbContent> getListByCategoryId(Long CONTENT_CATEGORY_ID) {
		
		//查询缓存
		try {
			//通过jedis接口查询缓存
			String json = jedisClient.hget(CONTENT_LIST, CONTENT_CATEGORY_ID + "");
			//非空判断
			if (StringUtils.isNotBlank(json)) {
				//转换成list集合返回
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//查询数据库
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(CONTENT_CATEGORY_ID);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		
		
		//向缓存中添加数据 数据转换成json串
		try {
			//通过jedis接口保存缓存
			jedisClient.hset(CONTENT_LIST, CONTENT_CATEGORY_ID + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
