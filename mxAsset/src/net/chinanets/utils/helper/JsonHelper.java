package net.chinanets.utils.helper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.chinanets.utils.CommonMethods;
import net.chinanets.utils.common.Enums;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JSON帮助类
 * @author RLiuyong
 *
 */
public class JsonHelper {
	private static Log log=LogFactory.getLog(JsonHelper.class);
	
	/**
	 * 分析JSON,并予以更正,加上“[]”
	 * @param strJSON
	 * @return
	 */
	public static final String AnsyJsonString(String strJSON){
		if(StringHelper.IsNullOrEmpty(strJSON)){
			return "";
		}
		if(!strJSON.startsWith("[")){
			strJSON="["+strJSON;
		}
		if(!strJSON.endsWith("]")){
			strJSON=strJSON+"]";
		}
		return strJSON;
	}
	
	/**
	 * 反向分析JSON,并予以更正，去掉“[]”
	 * @param strJSON
	 * @return
	 */
	public static final String AnsyJsonStringBack(String strJSON){
		if(StringHelper.IsNullOrEmpty(strJSON)){
			return "";
		}
		if(strJSON.indexOf("[")==0){
			strJSON=strJSON.substring(1,strJSON.length()-1);
		}
		if(strJSON.lastIndexOf("]")!=-1){
			strJSON=strJSON.substring(0,strJSON.length()-1);
		}
		return strJSON;
	}
	
	/**
	 * 将JSON字符串转换JSONArray
	 * @param strValue
	 * @return
	 */
	public static final JSONArray GetJsonArrayByJsonString(String strValue){
		try{
			strValue=AnsyJsonString(strValue);
			JSONArray tempJsonObj=JSONArray.fromObject(strValue);
			return tempJsonObj;
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
			return null;
		}
	}

	
	/**
	 * 将JSON字符串转换为数组
	 * @param strValue
	 * @return
	 */
	public static final Object[] GetArrayByJsonString(String strValue){
		try{
			JSONArray tempJsonArray=GetJsonArrayByJsonString(strValue);
			return (Object[])JSONArray.toArray(tempJsonArray);
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 将JSON字符串转换为对象
	 * @param strValue
	 * @param beanClass
	 * @return
	 */
	public static final Object GetBeanByJsonString(String strValue,Class<?> beanClass){
		try{
			if(beanClass==null){
				return null;
			}
			JSONObject tempJsonObj=JSONObject.fromObject(strValue);
			//加入show_order 到showorder 转换
			Iterator<String> itr = tempJsonObj.keys();
			Map<String,String> addMap  = new HashMap<String,String>();
			while(itr.hasNext()){
				String key = itr.next().toString();
				if(key.contains("_")){
					addMap.put(key.replace("_","").toUpperCase(), tempJsonObj.get(key).toString());
				}
			}
			tempJsonObj.putAll(addMap);
			
			Object beanObj=beanClass.newInstance();
			for(Field tempField:beanClass.getDeclaredFields()){
				String fieldName=tempField.getName();
				if(!tempJsonObj.containsKey(fieldName) && !tempJsonObj.containsKey(fieldName.toUpperCase()) && !tempJsonObj.containsKey(fieldName.toLowerCase())){
					continue;
				}
				Object objValue=tempJsonObj.get(fieldName);
				if(objValue == null){
					objValue=tempJsonObj.get(fieldName.toUpperCase());
				}
				if(objValue == null){
					objValue=tempJsonObj.get(fieldName.toLowerCase());
				}
				String strObjVlue="";
				if(objValue!=null){
					strObjVlue=objValue+"";
				}
				String fieldType=tempField.getType().getSimpleName().toUpperCase();
				tempField.setAccessible(true);
				if(StringHelper.Compare(fieldType, Enums.DATATYPE.DATE.toString(), true)==0){
					tempField.set(beanObj,DateHelper.GetUtilDateByDateString(strObjVlue, null));
					continue;
				}else if(StringHelper.Compare(fieldType, Enums.DATATYPE.STRING.toString(), true)==0){
					tempField.set(beanObj,strObjVlue);
					continue;
				}
				//下面的校验都是Number类型校验
				if(!CommonMethods.isDouble(strObjVlue)){
					continue;
				}
				if(StringHelper.IsNullOrEmpty(strObjVlue)){
					strObjVlue="0";
				}
				if(StringHelper.Compare(fieldType, Enums.DATATYPE.INT.toString(), true)==0){
					tempField.set(beanObj,Integer.parseInt(strObjVlue));
				}else if(StringHelper.Compare(fieldType, Enums.DATATYPE.FLOAT.toString(), true)==0){
					tempField.set(beanObj,Float.parseFloat(strObjVlue));
				}else if(StringHelper.Compare(fieldType, Enums.DATATYPE.LONG.toString(), true)==0){
					tempField.set(beanObj,Long.parseLong(strObjVlue));
				}else if(StringHelper.Compare(fieldType, Enums.DATATYPE.DOUBLE.toString(), true)==0){
					tempField.set(beanObj,Double.parseDouble(strObjVlue));
				}
			}
			return beanObj;
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 将JSON字符串转换为对象
	 * @param strValue
	 * @param tempObj
	 * @return
	 */
	public static final void GetBeanByJsonString(String strValue,Object tempObj){
		try{
			if(tempObj==null){
				return;
			}
			JSONObject tempJsonObj=JSONObject.fromObject(strValue);
			//加入show_order 到showorder 转换
			Iterator<String> itr = tempJsonObj.keys();
			Map<String,String> addMap  = new HashMap<String,String>();
			while(itr.hasNext()){
				String key = itr.next().toString();
				if(key.contains("_")){
					addMap.put(key.replace("_",""), tempJsonObj.get(key).toString());
				}
			}
			tempJsonObj.putAll(addMap);
			
			Class<?> tempClass=tempObj.getClass();
			for(Field tempField:tempClass.getDeclaredFields()){
				String fieldName=tempField.getName().toLowerCase();
				if(!tempJsonObj.containsKey(fieldName) && !tempJsonObj.containsKey(fieldName.toUpperCase())){
					continue;
				}
				Object objValue=tempJsonObj.get(fieldName);
				if(objValue == null){
					objValue=tempJsonObj.get(fieldName.toUpperCase());
				}
				String strObjVlue="";
				if(objValue!=null){
					strObjVlue=objValue.toString();
				}
				String fieldType=tempField.getType().getSimpleName().toUpperCase();
				tempField.setAccessible(true);
				if(StringHelper.Compare(fieldType, Enums.DATATYPE.DATE.toString(), true)==0){
					tempField.set(tempObj,DateHelper.GetUtilDateByDateString(strObjVlue, null));
					continue;
				}else if(StringHelper.Compare(fieldType, Enums.DATATYPE.STRING.toString(), true)==0){
					tempField.set(tempObj,strObjVlue);
					continue;
				}
				if(!CommonMethods.isDouble(strObjVlue)){
					continue;
				}
				if(StringHelper.IsNullOrEmpty(strObjVlue)){
					strObjVlue="0";
				}
				if(StringHelper.Compare(fieldType, Enums.DATATYPE.INT.toString(), true)==0){
					tempField.set(tempObj,Integer.parseInt(strObjVlue));
				}else if(StringHelper.Compare(fieldType, Enums.DATATYPE.FLOAT.toString(), true)==0){
					tempField.set(tempObj,Float.parseFloat(strObjVlue));
				}else if(StringHelper.Compare(fieldType, Enums.DATATYPE.LONG.toString(), true)==0){
					tempField.set(tempObj,Long.parseLong(strObjVlue));
				}else if(StringHelper.Compare(fieldType, Enums.DATATYPE.DOUBLE.toString(), true)==0){
					tempField.set(tempObj,Double.parseDouble(strObjVlue));
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
		}
	}
	
	/**
	 * 将JSON字符串转换为实体对象集合
	 * @param strValue
	 * @param beanClass
	 * @return
	 */
	public static final List<?> GetListBeanByJsonString(String strValue,Class<?> beanClass){
		try{
			if(beanClass==null){
				return null;
			}
			List<Object> tempList=new ArrayList<Object>();
			JSONArray jsonArray=GetJsonArrayByJsonString(strValue);
			for(int i=0;i<jsonArray.size();i++){
				JSONObject temoJsonObject=jsonArray.getJSONObject(i);
				Object tempObject=GetBeanByJsonString(temoJsonObject.toString(),beanClass);
				tempList.add(tempObject);
			}
			return tempList;
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 将JSON字符串转换为List集合
	 * @param strValue
	 * @return
	 */
	public static final List<?> GetListByJsonString(String strValue){
		try{
			JSONArray tempJsonArray=JSONArray.fromObject(strValue);
			return JSONArray.toList(tempJsonArray);
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 将JSON字符串转换为MAP集合
	 * @param strValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final TreeMap<Object,Object> GetMapByJsonString(String strValue){
		try{
			TreeMap<Object,Object> tempMap=new TreeMap<Object, Object>();
			JSONObject tempObject=JSONObject.fromObject(strValue);
			Iterator<Object> iterator=tempObject.keys();
			Object strKey=null;
			while(iterator.hasNext()){
				strKey=iterator.next();
				tempMap.put(strKey, tempObject.get(strKey));
			}
			return tempMap;
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 将实体对象转换为JSONObject
	 * @param tempObject
	 * @return
	 */
	public static final JSONObject GetJsonObjByClass(Object tempObject){
		try{
			JSONObject tempObj=JSONObject.fromObject(tempObject);
			return tempObj;
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 将集合转换为JSONObject
	 * @param tempList
	 * @return
	 */
	public static final JSONObject GetJsonObjByList(List<?> tempList){
		try{
			JSONObject tempObj=JSONObject.fromObject(tempList);
			return tempObj;
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 将数组转换为JSONObject
	 * @param tempArray
	 * @return
	 */
	public static final JSONObject GetJsonObjByArray(Object[] tempArray){
		try{
			JSONObject tempObj=JSONObject.fromObject(tempArray);
			return tempObj;
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 将Map集合转换为JSONObject
	 * @param tempMap
	 * @return
	 */
	public static final JSONObject GetJsonObjByMap(Map<?,?> tempMap){
		try{
			JSONObject tempObj=JSONObject.fromObject(tempMap);
			return tempObj;
		}catch(Exception ex){
			ex.printStackTrace();			log.error(ex.getMessage());
			return null;
		}
	}
}
