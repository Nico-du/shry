package net.chinanets.pojos;
import java.util.List;

/**
*风叶性能对象Vo 包含试验单对象和风叶性能对象（实验单和风叶性能，一对多）
*/
public class ListFyxnVo {

	private List<ShryFyxnData> fyxnData;
	private  ShrySydData  sydData;
	private String result;
	
	public List<ShryFyxnData> getFyxnData() {
		return fyxnData;
	}
	public void setFyxnData(List<ShryFyxnData> fyxnData) {
		this.fyxnData = fyxnData;
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
