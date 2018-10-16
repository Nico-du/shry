package net.chinanets.utils.helper;

import java.lang.reflect.Field;

import net.chinanets.utils.common.ConditionMates;
import net.chinanets.utils.common.Enums.DATATYPE;
import net.chinanets.utils.common.Enums.EXPTYPE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 处理条件辅助类
 * @author dzj
 *
 */
public class ConditionHelper{
	private static Log log=LogFactory.getLog(ConditionHelper.class);
	
	/**
	 * 映射实体对映字段匹配数据库识别的表达式
	 * @param strFiled
	 * @param strValue
	 * @param tempClass
	 * @return
	 */
	final public static String GetConditionString(String strFiled,Object objValue,boolean isFiledneedsplit){
		String realFiled=strFiled;
		String expType=EXPTYPE.LIKE.toString();
		String dataType=DATATYPE.UNKNOW.toString();
		if(isFiledneedsplit){
			int startPoint=strFiled.indexOf("_");
			int endPoint=strFiled.lastIndexOf("_");
			expType=strFiled.substring(endPoint+1);
			strFiled=strFiled.substring(0,endPoint);
			endPoint=strFiled.lastIndexOf("_");
			dataType=strFiled.substring(endPoint+1).toUpperCase();
			realFiled=strFiled.substring(startPoint+1,endPoint);
		}
		realFiled="TMPT."+realFiled;
		return GetConditionStringByParam(realFiled,dataType,expType,objValue);
	}
	
	/**
	 * 映射实体对映字段匹配数据库识别的表达式
	 * @param strFiled
	 * @param strValue
	 * @param tempClass
	 * @return
	 */
	final public static String GetConditionString(String strFiled,Object objValue,Class<?> tempClass,boolean isFiledneedsplit){
		String realFiled=strFiled;
		String expType=EXPTYPE.LIKE.toString();
		String dataType=DATATYPE.UNKNOW.toString();
		if(isFiledneedsplit){
			int startPoint=strFiled.indexOf("_");
			int endPoint=strFiled.lastIndexOf("_");
			realFiled=strFiled.substring(startPoint+1,endPoint);
			expType=strFiled.substring(endPoint+1);
		}
		if(tempClass != null){
			for(Field tempField:tempClass.getDeclaredFields()){
				if(StringHelper.Compare(realFiled, tempField.getName(), true)==0){
					dataType=tempField.getType().getSimpleName().toUpperCase();
				}
			}
			realFiled=tempClass.getSimpleName()+"."+realFiled;
		}
		return GetConditionStringByParam(realFiled,dataType,expType,objValue);
	}
	
	/**
	 * 根据相关参数生成条件语句
	 * @param realFiled
	 * @param dataType
	 * @param expType
	 * @param objValue
	 * @return
	 */
	final public static String GetConditionStringByParam(String realFiled,String dataType,String expType,Object objValue){
		if(StringHelper.IsNullOrEmpty(expType)){
			return "";
		}
		if(StringHelper.Compare(dataType, DATATYPE.UNKNOW.toString(), true)==0){
			log.debug("无法解析字段中对应的数据类型");
			return "";
		}else if(StringHelper.Compare(dataType, DATATYPE.DATE.toString(), true)==0){
			objValue=DateHelper.GetUtilDateByDateString(objValue, null);
		}
		//为空判断
		if(StringHelper.Compare(expType, EXPTYPE.ISNULL.toString(), true)==0
				||StringHelper.Compare(expType, EXPTYPE.ISNOTNULL.toString(), true)==0){
			return ConditionMates.GetIsNullOrNotNullConditionString(realFiled, objValue, expType);
		}
		//不能为空判断
		if(objValue==null){
			return "";
		}
		if(StringHelper.Compare(expType, EXPTYPE.EQ.toString(), true)==0){
			if(StringHelper.Compare(dataType, DATATYPE.STRING.toString(), true)==0 
				||StringHelper.Compare(dataType, DATATYPE.STRINGBUFFER.toString(), true)==0 
				||StringHelper.Compare(dataType, DATATYPE.STRINGBUILD.toString(), true)==0){
				return ConditionMates.GetStringConditionString(realFiled, objValue, expType);
			}else{
				return ConditionMates.GetDefaultConditionString(realFiled, objValue, expType);
			}
		}else if(StringHelper.Compare(expType, EXPTYPE.LIKE.toString(), true)==0){
			return ConditionMates.GetLikeConditionString(realFiled, objValue, expType);
		}else if(StringHelper.Compare(expType, EXPTYPE.LEFTLIKE.toString(), true)==0){
			return ConditionMates.GetLeftLikeConditionString(realFiled, objValue, expType);
		}else if(StringHelper.Compare(expType, EXPTYPE.RIGHTLIKE.toString(), true)==0){
			return ConditionMates.GetRightLikeConditionString(realFiled, objValue, expType);
		}else if(StringHelper.Compare(expType, EXPTYPE.ABSEQ.toString(), true)==0){
			return ConditionMates.GetABSConditionString(realFiled, objValue, expType);
		}else if(StringHelper.Compare(expType, EXPTYPE.IN.toString(), true)==0
				||StringHelper.Compare(expType, EXPTYPE.NOTIN.toString(), true)==0){
			return ConditionMates.GetInOrNotInConditionString(realFiled, objValue, expType);
		}else{
			return ConditionMates.GetDefaultConditionString(realFiled, objValue, expType);
		}
	} 
	
}
