package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回数据对象
 * @author 罗小黑
 *
 */
public class EasyUIDataGridResult implements Serializable {

	private static final long serialVersionUID = 1L;
	//总页数
	private Integer total;
	//每页显示的数据
	private List rows;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List getRows() {
		return rows;
	}

	public void setRows(List rows) {
		this.rows = rows;
	}
	
	

}
