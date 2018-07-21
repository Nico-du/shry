package net.chinanets.pojos;

import java.util.List;

public class ListVo {
	
	private  List<ShryFyData>  fyDataList;
	private  List<ShryZcData>  zcDataList;
	private  List<ShryDjData>  djDataList;
	private  List<ShryZcJsyqData>  zcjsyqList;
 	private  String result;
	
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public List<ShryFyData> getFyDataList() {
		return fyDataList;
	}
	public void setFyDataList(List<ShryFyData> fyDataList) {
		this.fyDataList = fyDataList;
	}
	public List<ShryZcData> getZcDataList() {
		return zcDataList;
	}
	public void setZcDataList(List<ShryZcData> zcDataList) {
		this.zcDataList = zcDataList;
	}
	public List<ShryDjData> getDjDataList() {
		return djDataList;
	}
	public void setDjDataList(List<ShryDjData> djDataList) {
		this.djDataList = djDataList;
	}
	public List<ShryZcJsyqData> getZcjsyqList() {
		return zcjsyqList;
	}
	public void setZcjsyqList(List<ShryZcJsyqData> zcjsyqList) {
		this.zcjsyqList = zcjsyqList;
	}
	
}
