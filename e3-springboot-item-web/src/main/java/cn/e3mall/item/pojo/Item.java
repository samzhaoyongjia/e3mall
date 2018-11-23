package cn.e3mall.item.pojo;

import java.io.Serializable;

import cn.e3mall.pojo.TbItem;

/**
 * 增强Item。为了图片展示
 * @author 罗小黑
 *
 */
public class Item extends TbItem implements Serializable{

	private static final long serialVersionUID = 1L;
	//构造方法。将父类的一些属性注入到子类中,因为父类不能强转成子类
	public Item(TbItem tbItem) {
		this.setBarcode(tbItem.getBarcode());
		this.setCid(tbItem.getCid());
		this.setCreated(tbItem.getCreated());
		this.setId(tbItem.getId());
		this.setImage(tbItem.getImage());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setUpdated(tbItem.getUpdated());
	}

	public String[] getImages(){
		//从父类中获取图片字符串
		String image = this.getImage();
		//判断是否为空
		if (image != null && !"".equals(image)) {
			//切割字符串
			String[] split = image.split(",");
			return split;
		}
		return null;
	}
}
