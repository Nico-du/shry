package net.chinanets.pojos;

import java.util.List;

/**
 * 电机vo对象
 */
public class ListDjVo {

	private  List<ShryDjData> djList;
	private  String result;
	
	public List<ShryDjData> getDjList() {
		return djList;
	}
	public void setDjList(List<ShryDjData> djList) {
		this.djList = djList;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
