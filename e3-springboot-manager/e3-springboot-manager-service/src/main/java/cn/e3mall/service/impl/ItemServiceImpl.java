package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.service.ItemService;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {
	
	//注入dao
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	//注入ActiveMQ相关的对象
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	
	//注入jedis缓存对象
	@Autowired
	private JedisClient jedisClient;
	//注入配置文件
	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;
	@Value("${ITEM_INFO_EXPIRE}")
	private Integer ITEM_INFO_EXPIRE;
	
	/**
	 * 根据id查询商品
	 */
	@Override
	public TbItem getItemById(Long itemId) {
		
		//先查询缓存中是否含有该商品
		try {
			String json = jedisClient.get(ITEM_INFO_PRE+":"+itemId+":BASE");
			//判断json是否为空
			if (StringUtils.isNotBlank(json)) {
				//将json转成对象返回
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		
		//判断商品是否为空
		if (item != null) {
			//将查询结果加入缓存
			try {
				jedisClient.set(ITEM_INFO_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(item));
				//设置key缓存持续时间
				jedisClient.expire(ITEM_INFO_PRE+":"+itemId+":BASE", ITEM_INFO_EXPIRE);			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return item;
		}
	
		return null;
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//使用pagehelper插件进行分页查询
		PageHelper.startPage(page, rows);
		//之后的查询就加在了分页
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//将查询后的list交由pagehelper插件处理
		PageInfo<TbItem> info = new PageInfo<>(list);
		//获得总条数
		long total = info.getTotal();
		//封装分页对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal((int) total);
		return result;
	}
	
	/**
	 * 添加商品
	 */
	@Override
	public E3Result saveItem(TbItem item, String desc) {
		//1,封装item
		 //商品id,通过工具类生成，毫秒加2位随机数
		final long itemId = IDUtils.genItemId();
		item.setId(itemId);
		 //商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte)1);
		 //创建时间和更新时间
		Date date = new Date();
		item.setCreated(date);
		item.setUpdated(date);
		
		//2,封装富文本desc储存对象
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		
		//3,向商品表中插入数据
		itemMapper.insert(item);
		//4,向商品富文本表插入数据
		itemDescMapper.insert(itemDesc);
		
		//添加商品后通过ActiveMQ发送广播告知其他服务。
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(itemId + "");
				return message;
			}
		});
		
		//5,返回插入成功结果集
		return E3Result.ok();
	}
	
	/**
	 * 更新商品信息
	 */
	@Override
	public E3Result updateItem(TbItem item, String desc) {
		//1,封装item
		if ("".equals(item.getCreated())) {
			item.setCreated(null);
		}
		//封装更新日期
		Date date = new Date();
		item.setUpdated(date);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte)1);
		
		//2,封装商品详情
		//查询商品的详情
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setUpdated(date);
		
		//更新到数据库
		itemMapper.updateByPrimaryKeySelective(item);
		itemDescMapper.updateByPrimaryKeySelective(itemDesc);
		
		//删除redis商品缓存
		jedisClient.del(ITEM_INFO_PRE+":"+item.getId()+":BASE");
		
		return E3Result.ok();
	}
	
	/**
	 * 删除商品
	 */
	@Override
	public E3Result deleteItem(Integer[] ids) {
		for (Integer id : ids) {
			TbItem item = new TbItem();
			item.setId(id.longValue());
			//更新商品状态 //商品状态，1-正常，2-下架，3-删除
			item.setStatus((byte)3);
			itemMapper.updateByPrimaryKeySelective(item);
			//删除redis商品缓存
			jedisClient.del(ITEM_INFO_PRE+":"+id+":BASE");
		}
		
		return E3Result.ok();
	}
	
	/**
	 * 下架商品
	 */
	@Override
	public E3Result instockItem(Integer[] ids) {
		for (Integer id : ids) {
			TbItem item = new TbItem();
			item.setId(id.longValue());
			//更新商品状态 //商品状态，1-正常，2-下架，3-删除
			item.setStatus((byte)2);
			itemMapper.updateByPrimaryKeySelective(item);
			//删除redis商品缓存
			jedisClient.del(ITEM_INFO_PRE+":"+id+":BASE");
		}
		return E3Result.ok();
	}
	
	/**
	 * 上架商品
	 */
	@Override
	public E3Result reshelfItem(Integer[] ids) {
		for (Integer id : ids) {
			TbItem item = new TbItem();
			item.setId(id.longValue());
			//更新商品状态 //商品状态，1-正常，2-下架，3-删除
			item.setStatus((byte)1);
			itemMapper.updateByPrimaryKeySelective(item);
			//删除redis商品缓存
			jedisClient.del(ITEM_INFO_PRE+":"+id+":BASE");
		}
		return E3Result.ok();
	}
	
	/**
	 * 根据商品id获取商品详情
	 */
	@Override
	public TbItemDesc getItemDescByItemId(Long itemId) {
		//先查询缓存中是否含有该商品详情
		try {
			String json = jedisClient.get(ITEM_INFO_PRE+":"+itemId+":DESC");
			//判断json是否为空
			if (StringUtils.isNotBlank(json)) {
				//将json转成对象返回
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//查询商品详情描述
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		//判断商品详情是否为空
		if (itemDesc != null) {
			//将查询详情结果加入缓存
			try {
				jedisClient.set(ITEM_INFO_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
				//设置key缓存持续时间
				jedisClient.expire(ITEM_INFO_PRE+":"+itemId+":DESC", ITEM_INFO_EXPIRE);			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return itemDesc;
		}
	
		return null;
	}
	
	

}
