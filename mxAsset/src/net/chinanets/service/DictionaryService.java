package net.chinanets.service;

import java.util.List;
import java.util.Map;

import net.chinanets.pojos.DictionaryModel;
import net.chinanets.pojos.TbType;

public interface DictionaryService extends CommonService{
	/**
	 * 获取国标数据
	 * @return
	 */
	public String getDictionaryTree(String cdnStr);
	 
	//分类
	public List<DictionaryModel> getTypes();
	
	public void deleteDictionaryItem(List<DictionaryModel> lt);
	
	public DictionaryModel getOneDictionaryByID(long id);
	 
	public Map<String, List> PurList();
	  
	public	void deleteTbTypeItem(List lt);
	
	public	TbType getOneTbTypeByID(long id);
		
	public String seltbtype(String typeid,String gzNumber,String typeName,String typeMark);//是否有重复数据
}
