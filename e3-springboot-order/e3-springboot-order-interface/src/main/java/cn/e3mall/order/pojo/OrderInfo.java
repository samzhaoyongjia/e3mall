package cn.e3mall.order.pojo;

import java.io.Serializable;
import java.util.List;

import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;

/**
 * 订单提交时所需要的数据封装,TbOrder的子类拓展
 * @author 罗小黑
 *
 */
public class OrderInfo extends TbOrder implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private TbOrderShipping orderShipping;
	
	private List<TbOrderItem> orderItems;

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	

}
