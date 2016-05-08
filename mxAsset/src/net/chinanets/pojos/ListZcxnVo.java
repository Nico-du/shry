package net.chinanets.pojos;

import java.util.List;

/*总成性能vo*/
public class ListZcxnVo {

	private List<ShryZcxnData> zcxnDataList ;
	private  ShrySydData  sydData;
	private String result;
	
	public List<ShryZcxnData> getZcxnDataList() {
		return zcxnDataList;
	}
	public void setZcxnDataList(List<ShryZcxnData> zcxnDataList) {
		this.zcxnDataList = zcxnDataList;
	}
	public ShrySydData getSydData() {
		return sydData;
	}
	public void setSydData(ShrySydData sydData) {
		this.sydData = sydData;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	

}
