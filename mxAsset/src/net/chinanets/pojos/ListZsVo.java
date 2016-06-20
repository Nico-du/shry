package net.chinanets.pojos;

import java.util.List;

/**
 * 噪声vo对象
 */
public class ListZsVo {

	private  List<ShryFyZsData> zsList;
	private  List<ShryFyData> fyList;
	private  String result;
	
	
	public List<ShryFyZsData> getZsList() {
		return zsList;
	}
	public void setZsList(List<ShryFyZsData> zsList) {
		this.zsList = zsList;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public List<ShryFyData> getFyList() {
		return fyList;
	}
	public void setFyList(List<ShryFyData> fyList) {
		this.fyList = fyList;
	}
	
	
}
