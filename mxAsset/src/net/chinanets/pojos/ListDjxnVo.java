package net.chinanets.pojos;
import java.util.List;

/**
*风叶性能对象Vo包含试验单对象和风叶性能对象（实验单和风叶性能，一对多）
*/
public class ListDjxnVo {

	private List<ShryDjxnData> djxnList;
	private  ShrySydDjData  sydData;
	private String result;
	
	public List<ShryDjxnData> getDjxnList() {
		return djxnList;
	}
	public void setDjxnList(List<ShryDjxnData> djxnList) {
		this.djxnList = djxnList;
	}
	public ShrySydDjData getSydData() {
		return sydData;
	}
	public void setSydData(ShrySydDjData sydData) {
		this.sydData = sydData;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}
