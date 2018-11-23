package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 搜索商品返回结果集
 * @author 罗小黑
 *
 */
public class SearchResult implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//商品列表
	private List<SearchItem> itemList;
	//总页数
	private int totalPages;
	//总记录数
	private int recourdCount;

	public List<SearchItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getRecourdCount() {
		return recourdCount;
	}

	public void setRecourdCount(int recourdCount) {
		this.recourdCount = recourdCount;
	}
	
	
}
