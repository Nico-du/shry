package net.chinanets.service.imp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import net.chinanets.dao.CommonDao;
import net.chinanets.dao.DictionaryDao;
import net.chinanets.entity.CnstDictionaryData;
import net.chinanets.pojos.DictionaryModel;
import net.chinanets.pojos.TbType;
import net.chinanets.service.DictionaryService;
import net.chinanets.utils.CommonMethods;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class DictionaryServiceImpl extends CommonServiceImp implements DictionaryService{
	private DictionaryDao dictionaryDao;

	/**
	 * 获取国标数据
	 * @return
	 */
	public String getDictionaryTree(String cdnStr){

	org.dom4j.Document document = org.dom4j.DocumentHelper.createDocument();
	org.dom4j.Element root = document.addElement("root");
			root.addAttribute("label", "固定资产");
			
			try{
			JSONArray listDicAll = dictionaryDao.RunSelectJSONArrayBySql("select * from CNSV_DICTIONARY where 1=1 and "+cdnStr+" order by length(trim(dicid)),dicid asc", null);
			 
			Map<String,Element> treeMap = new LinkedHashMap<String,Element>();
			int k=0; 
			JSONObject dicIterS1;
			Element tempElement,childElement;
			for(Iterator<JSONObject> iterator = listDicAll.iterator(); iterator.hasNext();k++){
				dicIterS1 = iterator.next();
				tempElement=org.dom4j.DocumentHelper.createElement("node");
				tempElement.addAttribute("id", dicIterS1.getString("dicid").toString());
				tempElement.addAttribute("isSelect", "0");
				if(dicIterS1.getString("dicid").contains("00000000")){
					tempElement.addAttribute("label", dicIterS1.getString("dicname"));
				}else{
				tempElement.addAttribute("label", dicIterS1.getString("dicid") + " " + dicIterS1.getString("dicname"));
				}
				if("Y".equals(dicIterS1.getString("haschildren") )){
					tempElement.addAttribute("haschildren", dicIterS1.getString("haschildren").toString());
				//	if(dicIterS1.has("parentid") && !CommonMethods.isNullOrWhitespace(dicIterS1.getString("parentid"))){
					childElement=org.dom4j.DocumentHelper.createElement("node");
					childElement.addAttribute("label", "正在加载...");
					tempElement.add(childElement);	 
				//	}
				}
				treeMap.put(dicIterS1.getString("dicid"),tempElement);
			}  
			for(Iterator<JSONObject> iterator = listDicAll.iterator(); iterator.hasNext();){
				dicIterS1 = iterator.next();
				if(dicIterS1.has("parentid") && !CommonMethods.isNullOrWhitespace(dicIterS1.getString("parentid")) &&  treeMap.containsKey(dicIterS1.getString("parentid")) ){
					treeMap.get(dicIterS1.getString("parentid")).add(treeMap.get(dicIterS1.getString("dicid")));
				//	treeMap.remove(dicIterS1.getDicid());
				}
			}
			for(Iterator<String> iterator = treeMap.keySet().iterator(); iterator.hasNext();){
				tempElement = treeMap.get(iterator.next());
				if(tempElement.getParent() == null){
					root.add(tempElement);
				}
				//	 System.out.println(tempElement.asXML());
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			//writXml(document);
			System.out.println(document.asXML());
			return document.asXML();
		}
	
	
	

	public DictionaryModel getOneDictionaryByID(long id) {
		// TODO Auto-generated method stub
		return (DictionaryModel) dictionaryDao.getObjectById(DictionaryModel.class,
				id);
	}

	public Map<String, List> PurList() {
		// TODO Auto-generated method stub
		Map<String, List> list=new HashMap<String, List>();
		List listdest=dictionaryDao.getObjectByExample(new DictionaryModel());
		list.put("dictionaryList", listdest);//list  dictionary
		return list;
	}

	public void deleteTbTypeItem(List lt) {
		// TODO Auto-generated method stub
		if (lt.size() > 0) {
			for (int i = 0; i < lt.size(); i++) {
				TbType meet = (TbType) lt.get(i);
					meet.setUsable(0);
			
				dictionaryDao.update(meet);
				
			}
		}
	}

	public TbType getOneTbTypeByID(long id) {
		// TODO Auto-generated method stub
		return (TbType) dictionaryDao.getObjectById(TbType.class,
				id);
	}

	public String seltbtype(String typeid,String gzNumber, String typeName, String typeMark) {
		// TODO Auto-generated method stub
		TbType tb=new TbType();
		tb.setGzNumber(gzNumber);
		tb.setUsable(1);
		List<TbType> listtb=null;
		if(typeid==null&&typeid!=""){
			if(gzNumber!=null&&gzNumber!=""){
				listtb=dictionaryDao.getObjectByExample(tb);
			}
			if(listtb==null||listtb.size()==0){
				TbType tb2=new TbType();
				tb2.setTypeName(typeName);
				tb2.setTypeMark(typeMark);
				tb2.setUsable(1);
				List<TbType> listtb2=dictionaryDao.getObjectByExample(tb2);
				if(listtb2!=null&&listtb2.size()>0){
					return "error";
				}else{
					return "success";
				}
			}else{
				return "errorGzNumber";
			}
			
		}else{///修改
			
			if(gzNumber!=null&&gzNumber!=""){
				listtb=dictionaryDao.getObjectByExample(tb);
			}
			long tbid1=0l;
			TbType tb2=new TbType();
			tb2.setTypeName(typeName);
			tb2.setTypeMark(typeMark);
			tb2.setUsable(1);
			List<TbType> listtb2=dictionaryDao.getObjectByExample(tb2);
			long tbid2=0l;
			if(listtb!=null&&listtb.size()>0){
				
				for (TbType type : listtb) {
					tbid1=type.getTypeId();
				}
			}
			if(listtb2!=null||listtb2.size()>0){
				
				for (TbType type : listtb2) {
					tbid2=type.getTypeId();
				}
			}
			if(tbid1==Long.parseLong(typeid)&&listtb!=null&&listtb.size()>1){
				return "errorGzNumber";
			}else if(tbid1!=Long.parseLong(typeid)&&listtb!=null&&listtb.size()>0){
				return "errorGzNumber";
			}else{
				if(tbid2==Long.parseLong(typeid)&&listtb2!=null&&listtb2.size()>1){
					return "error";
				}else if(tbid2!=Long.parseLong(typeid)&&listtb2!=null&&listtb2.size()>0){
					return "error";
				}else{
					return "success";
				}
				
			}
			
			
		}
			
	}
	
	//图书类型树
	public Object[] getBookTypes(){
		@SuppressWarnings("unchecked")
		List<DictionaryModel> list = commonDao.getAllObjectByHql("from DictionaryModel dm where dm.status = 0");
		return list.toArray();
	}
	//分类
	@SuppressWarnings("unchecked")
	public List<DictionaryModel> getTypes(){
		List<DictionaryModel> list = commonDao.getAllObjectByHql("from DictionaryModel dm where dm.status >0");
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<DictionaryModel> getDictionartList(){
		List<DictionaryModel> list = commonDao.getAllObjectByHql("from DictionaryModel dm where dm.pId is null or dm.pId = 0");
		return list;
	}
	
	public String getDictionaryByID(Long id){
		DictionaryModel dic = (DictionaryModel)commonDao.getObjectById(DictionaryModel.class,id);
		return dic.getName();
	}
	
	/**
	 * 删除对象数组
	 * @param List
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void deleteDictionaryItem(List<DictionaryModel> lt) {
	  if(lt.size()>0)
	  {
		  for(int i=0;i<lt.size();i++)
		  {
			  List<DictionaryModel> dList = null;
			  if(lt.get(i).getpId()==0){
				  dList = commonDao.getAllObjectByHql("from DictionaryModel t where t.pId = '"+lt.get(i).getId()+"'");
				  if(dList.size()>0){
					  for (int j = 0; j < dList.size(); j++) {
						  commonDao.delete(dList.get(j));	  
					  }
				  }
			  }
			  commonDao.delete(lt.get(i));
		  }
	  }
	}
	
	public Map<String, List<?>>  PurList1() {
		// TODO Auto-generated method stub
		Map<String, List<?>> list=new HashMap<String, List<?>>();
		
		TbType consumeStatus=new TbType();
		consumeStatus.setTypeMark("tb_consumeStatus");
		consumeStatus.setUsable(1);
		List<?> listConsumeStatus=commonDao.getObjectByExample(consumeStatus);
		list.put("consumeStatus",listConsumeStatus);//耗材状态
		
		TbType furnitureStatus=new TbType();
		furnitureStatus.setTypeMark("tb_furnitureStatus");
		furnitureStatus.setUsable(1);
		List<?> listFurnitureStatus=commonDao.getObjectByExample(furnitureStatus);
		list.put("furnitureStatus",listFurnitureStatus);//耗材状态
		
		
		TbType furnitureType=new TbType();
		furnitureType.setTypeMark("t_furniture");
		furnitureType.setUsable(1);
		List<?> listFurnitureType=commonDao.getObjectByExample(furnitureType);
		list.put("furnitureType",listFurnitureType);//家具用具类型
		return list;
	}




	public DictionaryDao getDictionaryDao() {
		return dictionaryDao;
	}

	public void setDictionaryDao(DictionaryDao dictionaryDao) {
		this.dictionaryDao = dictionaryDao;
	}


}
