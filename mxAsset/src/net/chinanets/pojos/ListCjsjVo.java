package net.chinanets.pojos;

import java.util.List;

/**
 * 电机vo对象
 */
public class ListCjsjVo {

	private  List<ShryCjsjData> cjsjList;
	private  String result;
	
	
	public List<ShryCjsjData> getCjsjList() {
		return cjsjList;
	}
	public void setCjsjList(List<ShryCjsjData> cjsjList) {
		this.cjsjList = cjsjList;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
