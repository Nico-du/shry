package net.chinanets.utils.common;

import java.util.Hashtable;

import net.chinanets.utils.common.Enums.EXPTYPE;
import net.chinanets.utils.helper.StringHelper;

/**
 * 根据表达式转换为数据库能识别的表达式
 * @author dzj
 *
 */
public class ConditionMates {
	public final static Hashtable<String, String> expMap=new Hashtable<String, String>();
	/**
	 * 等于
	 */
	public final static String CON_EQ="=";
	
	/**
	 * 绝对值相等
	 */
	public final static String CON_ABSEQ="==";
	
	/**
	 * 大于
	 */
	public final static String CON_GT=">";
	
	/**
	 * 大于等于
	 */
	public final static String CON_GTEQ=">=";
	
	/**
	 * 小于
	 */
	public final static String CON_LT="<";
	
	/**
	 * 小于等于
	 */
	public final static String CON_LTEQ="<=";
	
	/**
	 * 不等于
	 */
	public final static String CON_NOTEQ="<>";
	
	/**
	 * 为空
	 */
	public final static String CON_ISNULL="IS NULL";
	
	/**
	 * 不为空
	 */
	public final static String CON_ISNOTNULL="IS NOT NULL";
	
	/**
	 *全包含
	 */
	public final static String CON_LIKE="LIKE";
	
	/**
	 * 左包含
	 */
	public final static String CON_LEFTLIKE="LIKE";
	
	/**
	 * 右包含
	 */
	public final static String CON_RIGHTLIKE="LIKE";
	
	/**
	 * 在范围内
	 */
	public final static String CON_IN="IN";
	
	/**
	 * 不再范围内
	 */
	public final static String CON_NOTIN="NOT IN";
	
	/**
	 * 初始化值
	 */
	static{
		expMap.put(EXPTYPE.EQ.toString().toUpperCase(), CON_EQ.toUpperCase());
		expMap.put(EXPTYPE.ABSEQ.toString().toUpperCase(), CON_ABSEQ.toUpperCase());
		expMap.put(EXPTYPE.GT.toString().toUpperCase(), CON_GT.toUpperCase());
		expMap.put(EXPTYPE.GTEQ.toString().toUpperCase(), CON_GTEQ.toUpperCase());
		expMap.put(EXPTYPE.LT.toString().toUpperCase(), CON_LT.toUpperCase());
		expMap.put(EXPTYPE.LTEQ.toString().toUpperCase(), CON_LTEQ.toUpperCase());
		expMap.put(EXPTYPE.NOTEQ.toString().toUpperCase(), CON_NOTEQ.toUpperCase());
		expMap.put(EXPTYPE.ISNULL.toString().toUpperCase(), CON_ISNULL.toUpperCase());
		expMap.put(EXPTYPE.ISNOTNULL.toString().toUpperCase(), CON_ISNOTNULL.toUpperCase());
		expMap.put(EXPTYPE.LIKE.toString().toUpperCase(), CON_LIKE.toUpperCase());
		expMap.put(EXPTYPE.LEFTLIKE.toString().toUpperCase(), CON_LEFTLIKE.toUpperCase());
		expMap.put(EXPTYPE.RIGHTLIKE.toString().toUpperCase(), CON_RIGHTLIKE.toUpperCase());
		expMap.put(EXPTYPE.IN.toString().toUpperCase(), CON_IN.toUpperCase());
		expMap.put(EXPTYPE.NOTIN.toString().toUpperCase(), CON_NOTIN.toUpperCase());
	}
	
	
	
	/**
	 * 默认转换表达式
	 * @param strFiled
	 * @param strValue
	 * @return
	 */
	final public static String GetDefaultConditionString(String strFiled,Object objValue,String expType){
		if(StringHelper.IsNullOrEmpty(expType)){
			return "";
		}
		return StringHelper.Format("%1$s %2$s %3$s",strFiled,expMap.get(expType.toUpperCase()),objValue);
	}
	
	/**
	 * ABS绝对值转换表达式
	 * @param strFiled
	 * @param objValue
	 * @param expType
	 * @return
	 */
	final public static String GetABSConditionString(String strFiled,Object objValue,String expType){
		if(StringHelper.IsNullOrEmpty(expType)){
			return "";
		}
		if(objValue==null){
			return "";
		}
		return StringHelper.Format("ABS(%1$s)=ABS(%2$s)",strFiled,objValue);
	}
	
	/**
	 *转换为字符串表达式
	 * @param strFiled
	 * @param strValue
	 * @return
	 */
	final public static String GetStringConditionString(String strFiled,Object objValue,String expType){
		if(StringHelper.IsNullOrEmpty(expType)){
			return "";
		}
		if(objValue==null){
			return "";
		}
		String strValue=objValue.toString();
		if(StringHelper.IsNullOrEmpty(strValue)){
			return "";
		}
		return strFiled+" "+expMap.get(expType.toUpperCase())+" '"+strValue+"'";
	}
	
	/**
	 * Like表达式转换
	 * @param strFiled
	 * @param strValue
	 * @return
	 */
	final public static String GetLikeConditionString(String strFiled,Object objValue,String expType){
		if(StringHelper.IsNullOrEmpty(expType)){
			return null;
		}
		if(objValue==null){
			return "";
		}
		String strValue=objValue.toString();
		if(StringHelper.IsNullOrEmpty(strValue)){
			return "";
		}
		return strFiled+" "+expMap.get(expType.toUpperCase())+" '%"+strValue+"%'";
	}
	
	/**
	 * LeftLike表达式转换
	 * @param strFiled
	 * @param strValue
	 * @return
	 */
	final public static String GetLeftLikeConditionString(String strFiled,Object objValue,String expType){
		if(StringHelper.IsNullOrEmpty(expType)){
			return "";
		}
		if(objValue==null){
			return "";
		}
		String strValue=objValue.toString();
		if(StringHelper.IsNullOrEmpty(strValue)){
			return "";
		}
		return strFiled+" "+expMap.get(expType.toUpperCase())+" '"+strValue+"%'";
	}
	
	/**
	 * RightLike表达式转换
	 * @param strFiled
	 * @param strValue
	 * @return
	 */
	final public static String GetRightLikeConditionString(String strFiled,Object objValue,String expType){
		if(StringHelper.IsNullOrEmpty(expType)){
			return "";
		}
		if(objValue==null){
			return "";
		}
		String strValue=objValue.toString();
		if(StringHelper.IsNullOrEmpty(strValue)){
			return "";
		}
		strValue=strValue.replace("%", "/%");
		strValue=strValue.replace("_", "/_");
		return strFiled+" "+expMap.get(expType.toUpperCase())+" '%"+strValue+"'";
	}
	
	/**
	 * ISNull或者ISNotNull表达式转换
	 * @param strFiled
	 * @param strValue
	 * @return
	 */
	final public static String GetIsNullOrNotNullConditionString(String strFiled,Object objValue,String expType){
		if(StringHelper.IsNullOrEmpty(expType)){
			return "";
		}
		return strFiled+" "+expMap.get(expType.toUpperCase());
	}
	
	/**
	 * In或者NotIn表达式转换
	 * @param strFiled
	 * @param strValue
	 * @return
	 */
	final public static String GetInOrNotInConditionString(String strFiled,Object objValue,String expType){
		if(StringHelper.IsNullOrEmpty(expType)){
			return "";
		}
		String strValue="";
		if(objValue!=null){
			strValue=StringHelper.ConvertStrToDBStr(objValue.toString());
		}
		return strFiled+" "+expMap.get(expType.toUpperCase())+" ("+strValue+")";
	}
}
