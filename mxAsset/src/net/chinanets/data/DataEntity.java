package net.chinanets.data;

import java.io.BufferedReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import net.chinanets.utils.helper.DateHelper;
import net.chinanets.utils.helper.StringHelper;
import net.sf.json.JSONObject;

/**
 * 保存数据值
 * @author dzj
 *
 */
@SuppressWarnings("serial")
public class DataEntity implements Serializable{

	protected TreeMap<String, Object> dataMap=new TreeMap<String, Object>();
	
	public DataEntity(){}
	
	/**
	 * 添加数据
	 * @param key
	 * @param value
	 */
	public void SetValue(String key,Object value){
		if(StringHelper.IsNullOrEmpty(key)){
			return;
		}
		dataMap.put(key.toLowerCase(), value);
	}
	
	/**
	 * 得到Object类型值
	 * @param key
	 * @return
	 */
	public Object GetObjectValue(String key,Object defaultValue){
		if(StringHelper.IsNullOrEmpty(key)){
			return defaultValue;
		}
		return dataMap.get(key.toLowerCase());
	}
	
	/**
	 * 得到String类型值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String GetStringValue(String key,String defaultValue){
		if(StringHelper.IsNullOrEmpty(key)){
			return defaultValue;
		}
		Object value=dataMap.get(key.toLowerCase());
		if(value==null){
			return defaultValue;
		}
		try{
			return value.toString();
		}catch(Exception e){
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	/**
	 * 得到Int类型值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int GetIntValue(String key,int defaultValue){
		if(StringHelper.IsNullOrEmpty(key)){
			return defaultValue;
		}
		Object value=dataMap.get(key.toLowerCase());
		if(value==null){
			return defaultValue;
		}
		try{
			return Integer.parseInt(value.toString());
		}catch(Exception e){
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	/**
	 * 得到long类型值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public long GetLongValue(String key,long defaultValue){
		if(StringHelper.IsNullOrEmpty(key)){
			return defaultValue;
		}
		Object value=dataMap.get(key.toLowerCase());
		if(value==null){
			return defaultValue;
		}
		try{
			return Long.parseLong(value.toString());
		}catch(Exception e){
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	/**
	 * 得到Double值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public double GetDoubleValue(String key,double defaultValue){
		if(StringHelper.IsNullOrEmpty(key)){
			return defaultValue;
		}
		Object value=dataMap.get(key.toLowerCase());
		if(value==null){
			return defaultValue;
		}
		try{
			return Double.parseDouble(value.toString());
		}catch(Exception e){
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	/**
	 * 得到Float值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public float GetFloatValue(String key,float defaultValue){
		if(StringHelper.IsNullOrEmpty(key)){
			return defaultValue;
		}
		Object value=dataMap.get(key.toLowerCase());
		if(value==null){
			return defaultValue;
		}
		try{
			return Float.parseFloat(value.toString());
		}catch(Exception e){
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	/**
	 * 得到日期值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Date GetUtilDateValue(String key,Date defaultValue){
		if(StringHelper.IsNullOrEmpty(key)){
			return defaultValue;
		}
		Object value=dataMap.get(key.toLowerCase());
		if(value==null){
			return defaultValue;
		}
		try{
			Date tempDate=DateHelper.GetUtilDateValue(value);
			if(tempDate==null){
				return defaultValue;
			}
			return tempDate;
		}catch(Exception e){
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	/**
	 * 得到Clob值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String GetClobValue(String key,String defaultValue){
		if(StringHelper.IsNullOrEmpty(key)){
			return defaultValue;
		}
		Object value=dataMap.get(key.toLowerCase());
		if(value==null){
			return defaultValue;
		}
		try{
			Reader tempReader=null;
			if(value instanceof oracle.sql.CLOB){
				oracle.sql.CLOB oracleClobValue=(oracle.sql.CLOB)value;
				tempReader=oracleClobValue.getCharacterStream();
			}else if(value instanceof java.sql.Clob){
				java.sql.Clob javasqlClobValue=(java.sql.Clob)value;
				tempReader=javasqlClobValue.getCharacterStream();
			}
			if(tempReader==null){
				return defaultValue;
			}
			boolean bFirst=true;
			BufferedReader bReader=new BufferedReader(tempReader);
			String strLine=bReader.readLine();
			StringBuffer tempString=new StringBuffer();
			while(strLine!=null){
				if(bFirst){
					bFirst=false;
				}else{
					tempString.append("\r\n");
				}
				tempString.append(strLine);
				strLine=bReader.readLine();
			}
			return tempString.toString();
		}catch(Exception e){
			e.printStackTrace();
			return defaultValue;
		}
	}
	
	/**
	 * 得到所有keys
	 * @return
	 */
	public List<String> GetListKeys(){
		List<String> keyList=new ArrayList<String>();
		Set<Entry<String, Object>> tempSet =dataMap.entrySet();
		Iterator<Entry<String, Object>> tempIterator=tempSet.iterator();
		while(tempIterator.hasNext()){
			Entry<String,Object> tempEntry=tempIterator.next();
			String key=tempEntry.getKey();
			keyList.add(key);
		}
		return keyList;
	}
	
	/**
	 * 转换得到JSON
	 * @return
	 */
	public JSONObject GetJSONObject(){
		if(dataMap.size()<1){
			return null;
		}
		Set<Entry<String, Object>> tempSet =dataMap.entrySet();
		Iterator<Entry<String, Object>> tempIterator=tempSet.iterator();
		JSONObject tempJson=new JSONObject();
		while(tempIterator.hasNext()){
			Entry<String,Object> tempEntry=tempIterator.next();
			String key=tempEntry.getKey();
			Object value=tempEntry.getValue();
			tempJson.put(key, value);
		}
		return tempJson;
	}
	
	/**
	 * 添加数据建
	 * @param tempDataEntity
	 * @param isIncloudEmpty 是否包含空数据
	 * @return
	 */
	public boolean AddDataEntity(DataEntity tempDataEntity,boolean isIncloudEmpty){
		if(tempDataEntity==null){
			return false;
		}
		TreeMap<String,Object> tempDataMap=tempDataEntity.GetValueMap();
		if(tempDataMap==null){
			return false;
		}
		Set<Entry<String, Object>> tempSet =tempDataMap.entrySet();
		Iterator<Entry<String, Object>> tempIterator=tempSet.iterator();
		while(tempIterator.hasNext()){
			Entry<String,Object> tempEntry=tempIterator.next();
			String key=tempEntry.getKey();
			Object value=tempEntry.getValue();
			if(value==null && !isIncloudEmpty){
				continue;
			}
			this.dataMap.put(key, value);
		}
		return true;
	}
	
	/**
	 * 得到当前数据集合
	 * @return
	 */
	public TreeMap<String, Object> GetValueMap(){
		return this.dataMap;
	}
	
	/**
	 * 是否包含该值数据
	 * @param key
	 * @return
	 */
	public boolean IsContainKey(String key){
		if(StringHelper.IsNullOrEmpty(key)){
			return false;
		}
		return this.dataMap.containsKey(key.toLowerCase());
	}
	
	/**
	 * 重置实体对象
	 */
	public void Reset(){
		this.dataMap.clear();
		this.dataMap=new TreeMap<String, Object>();
	}
	
}
