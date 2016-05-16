package net.chinanets.excel;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

/**
 * 对象工具�?
 * @author        
 */
public class ObjectUtil {
	/**
	 * 输出本类的相关log日志
	 */
	protected static final Logger logger = Logger.getLogger(ObjectUtil.class);
	

	
	/**
     * 根据指定格式格式化流水码
     * @param rule
     * @param code
     * @return 
     */
    private static String formatNumber(String rule, String code){
        // 使用�?”填充编码至指定长度
        String formatter = "%0"+rule.length()+"d";
        return String.format(formatter, Long.valueOf(code));
    }
	
	
	/**
	 * 私有构�?函数
	 */
	private ObjectUtil() {
		
	}
	
	/**
	 * JAVA 替换 字符串中:回车符�?换行�?为空�?
	 * @param str
	 * @return
	 * @author        
	 * @date 2013-11-1
//		�?��方法：String s = "你要去除的字符串";            
//	           这样也可以把空格和回车去掉，其他也可以照这样做�?    
//		1.去除空格：s = s.replace('\\s','');   
//	    2.去除回车：s = s.replace('\n','');  
	 * 注：\n 回车(\u000a)  \t 水平制表�?\u0009)  \s 空格(\u0008)  \r 换行(\u000d)
	 */
	public static String replaceBlank(String str){
		if(str == null || "".equals(str)){
			return str;
		}
	    Pattern p = Pattern.compile("\\r|\n");
	    Matcher m = p.matcher(str);
	    String after = m.replaceAll("");
	    return after;
	}
	/**
	 * 作�?�?        把List列表按照固定个数分页
	 * @param list 待分页的列表
	 * @param pageSize 每页固定条数
	 * @param pageStart �?��条数
	 */
	public static List listToPage(List list, int pageSize, int pageStart){
		//列表为空，直接返�?
		if(list == null || list.size() <= 0){
			return list;
		}
		
		//按照每pageSize条分页，并返回该页的subList对象
		int listSize = list.size();  							//列表总条�?
		int pageEnd = pageStart + pageSize;						//�?��条数 + 每页条数 = 结束条数
		
		// 当结束条�?大于 总条数，改取 列表总条�?
		if(pageEnd > listSize){
			pageEnd = listSize;
		}
		
		//返回当前页对应的列表
		return list.subList(pageStart, pageEnd);
	}
	/**
	 * 作�?�?        用�?号分隔List列表
	 * @param str
	 */
	public static String listToStr(List list){
		if(list !=null && !list.isEmpty()){
			String str = null;
			for(int i=0; i<list.size(); i++){
				Object obj = list.get(i);
				if(obj != null){
					if(str != null){
						str += ","+ obj.toString();
					}else{
						str = obj.toString();
					}
				}
			}
			return str;
		}else{
			return null;
		}
	}
	
	/**
	 * 作�?�?        将Long型转为字符串
	 * @param str
	 * @return
	 */
	public static String longToStr(Long obj){
		if(obj !=null){
			return obj.toString();
		}else{
			return null;
		}
	}
	
	/**
	 * 作�?�?        将字符串转为Double型，错误返回null
	 * @param str
	 */
	public static Double strToDouble(String str){
		Double d = null;
		if(str != null && !"".equals(str)){
			try{
				d = Double.parseDouble(str);
			}catch(Exception e){
				logger.warn("strToDouble：字符串[" + str + "]不是浮点�?");
				d = null;
			}
		}
		return d;
	}
	
	/**
	 * 作�?�?        将字符串转为Long型，错误返回null
	 * @param str
	 * @return
	 */
	public static Long stringToLong(String str){
		Long rl = null;
		if(str != null && !"".equals(str)){
			try{
				rl = Long.valueOf(str);
			}catch(Exception e){
				logger.warn("stringToLong：字符串[" + str + "]不全是数�?");
				rl = null;
			}
		}
		return rl;
	}
	
	/** 
	 * 作�?�?        String转Long
	 * @param str
	 * @return
	 */
	public static Long str2Long(String str)	{
		if(isNumeric(str)){
			return Long.valueOf(str);
		}else{
			return null;
		}
	} 
	
	/** 
	 * 作�?�?        String转Integer
	 * @param str
	 * @return
	 */
	public static Integer str2Integer(String str)	{
		if(isNumeric(str)){
			return Integer.valueOf(str);
		}else{
			return null;
		}
	} 
	
	/** 
	 * 作�?�?        Object转Long
	 * @param str
	 * @return
	 */
	public static Long obj2Long(Object obj)	{
		if(obj != null){
			try{
				return Long.valueOf(obj.toString());
			}catch(Exception e){
				logger.warn("obj2Long：字符串[" + obj.toString() + "]不全是数�?");
				return null;
			}
		}else{
			return null;
		}
	}
	
	/** 
	 * 作�?�?        Object转String
	 * @param str
	 * @return
	 */
	public static String obj2Str(Object obj)	{
		if(obj != null){
			return obj.toString();
		}else{
			return null;
		}
	} 
	/** 
	 * 作�?�?        Object转String 或�?空串
	 * @param str
	 * @return
	 */
	public static String obj2Empty(Object obj){
		if(obj != null){
			return obj.toString();
		}else{
			return "";
		}
	} 
	
	/** 
	 * 作�?�?        判断字符串是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str)	{
		if(str == null || "".equals(str)){
			//logger.warn("字符串[" + str + "]为空!");
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			logger.warn("字符串[" + str + "]不全是数�?");
			return false;
		}
		return true;
	} 
	


	/**
	 * 根据15位或�?8位身份证号码判断性别,按照系统参数表attr_id=161获取
	 * @param idCard
	 * @return
	 */
	public static String judgeGenderByIdCard(String idCard) {
		if (StringUtils.isNotBlank(idCard)) {
			Pattern pattern = Pattern.compile("[0-9]*");
			if (idCard.length() == 15 && pattern.matcher(idCard).matches()) {
				// 身份证号�?5位，都是数字，最后一位为奇数则为男否则为�?
				if (Integer.valueOf(idCard.substring(idCard.length() - 1, idCard.length())) % 2 == 0) {
					return "1010050002"; // �?
				} else {
					return "1010050001"; // �?
				}
			} else if (idCard.length() == 18
					&& pattern.matcher(idCard.substring(0, idCard.length() - 1)).matches()) {
				// 身份证号�?8位，除最后一位其他都是数�?倒数第二位为奇数则为男否则为�?
				if (Integer.valueOf(idCard.substring(idCard.length() - 2, idCard.length() - 1)) % 2 == 0) {
					return "1010050002"; // �?
				} else {
					return "1010050001"; // �?
				}
			}
		}
		return "2"; // 未知
	}
	
	/**
	 * 根据15位或�?8位身份证号码构�?出生日期
	 */
	public static String constructBirthdayByIdCard(String idCard) {
		String birthday = "";
		if (StringUtils.isNotBlank(idCard)) {
			Pattern pattern = Pattern.compile("[0-9]*");
			if (idCard.length() == 15 && pattern.matcher(idCard).matches()) {
				String year = "19"+idCard.substring(6,8);
				String month = idCard.substring(8,10);
				String day = idCard.substring(10,12);
				birthday = year+" "+month+"-"+day;
			}else if (idCard.length() == 18){
				String year = idCard.substring(6,10);
				String month = idCard.substring(10,12);
				String day = idCard.substring(12,14);
				birthday = year+"-"+month+"-"+day;
			}
		}
		return birthday;
	}
	/**
	 * �?5位身份证号转换成18位身份证号码
	 * 出生月份前加"19"(20世纪才使用的15位身份证号码),�?���?��加校验码
	 */
	public static String transformationIdFrom15To18(String custNo) {
		String idCardNo = null;
		if (custNo != null && custNo.trim().length() == 15) {
			custNo = custNo.trim();
			StringBuffer newIdCard = new StringBuffer(custNo);
			newIdCard.insert(6, "19");
			newIdCard.append(trasformationLastNo(newIdCard.toString()));
			
			idCardNo = newIdCard.toString();
		}
		return idCardNo;
	}

	/**
	 * 生成身份证最后一位效验码
	 */
	private static String trasformationLastNo(String id) {
		char pszSrc[] = id.toCharArray();
		int iS = 0;
		int iW[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		char szVerCode[] = new char[] { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		int i;
		for (i = 0; i < id.length(); i++) {
			iS += (pszSrc[i] - '0') * iW[i];
		}
		int iY = iS % 11;
		return String.valueOf(szVerCode[iY]);
	}

	/**
	 * �?8位身份证号转换成15位身份证号码
	 * 但存在身份证号码重复
	 */
	public static String transformationIdFrom18To15(String sCustno) {
		String first7No = sCustno.substring(0, 6);
		String lastNo = sCustno.substring(8, sCustno.length()-1);
		return first7No + lastNo;
	}
	

	
	/**
	 * JAVA 将字符串�?:"�?'"替换�?�?�?""
	 * @param str
	 * @return 
	 * @date 2015-02-11
	 */
	public static String replaceEnglishChar(String str){
		if(str == null || "".equals(str))
			return str;
		return str.replaceAll(":", "").replaceAll("'", "\"");
	}

	/**
	 * 接口单一对象，转化为字符串，用于打印日志
	 * @param obj
	 */
	public static String log(Object obj){
		StringBuffer strBuf = new StringBuffer();
		if (obj == null) {
			return strBuf.toString();
		}
		try {
			Class cls = obj.getClass();
			Method methlist[] = cls.getMethods();// .getDeclaredMethods();
			for (int i = 0; i < methlist.length; i++) {
				Method m = methlist[i];
				String methodName = m.getName();
				String sh = methodName.substring(0, 3);
				if (sh.equals("get")) {
					Class[] cc = null;
					Object[] oo = null;
					Method meth = cls.getMethod(methodName, cc);
					Object retobj = meth.invoke(obj, oo);
					strBuf.append("[m]" + methodName + "[v]" + retobj + "\n");
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return strBuf.toString();
	}
	
	/**
	 * json格式的字符串含有反斜杠和单引号时传�?会破坏json的数据格式，故进行转义处�?
	 * 字符串替换：反斜杠替换成\\，单引号替换成\'，其他特殊字符不会破坏json数据格式，故不替�?
	 * @param str
	 * @return 替换后的字符�?
	 * @author 陈孝�?
	 * @date 2015-5-13
	 */
	public static String escapeStr(String str) {
		if(StringUtils.isBlank(str)) {
			return "";
		} else {
			/*
			 * replaceAll的参数使用的是正则表达式，特殊字符需使用\来转�?
			 * java中\\输出\，\\\\代表\\，正则匹配时，第�?��\是转义字符，第二\才是�?��替换的字�?
			 */
			String escapeStr = str.replaceAll("\\\\", "\\\\\\\\").replaceAll("\'", "\\\\'");
			return escapeStr;
		}
	}
	/**
	 * 手机号码的校�?规则:�?�?��11位纯数字
	 * @param phoneNum 手机号码
	 * @return
	 * @author c_wangyu-082
	 * @date 2015-6-19 下午2:00:26
	 */
	public static boolean phoneNumVerify(String phoneNum){
		if(StringUtils.isBlank(phoneNum)){
			return false;
		}
		//由于话务有时会带入加0�?���?2位手机号�?删除�?���?  判断后面�?1位是否满足手机号码校�?-2015-7-29
		if(phoneNum.length() > 11 && phoneNum.startsWith("0")){
			phoneNum = phoneNum.substring(1);
		}
		Pattern p =Pattern.compile("^1\\d{10}$");
		Matcher m = p.matcher(phoneNum);
		boolean flag = m.matches();
		if(!flag){
			logger.warn("手机号码[" + phoneNum + "]不符合手机号码校验规�?");
		}
		return flag;
	}
	
	/**
	 * 手机号码的校�?规则:首位�?1，且长度12位，�?.
	 * @param phoneNum 手机号码
	 * @return
	 */
	public static String getPhone01(String phoneNum){
		if(StringUtils.isBlank(phoneNum)){
			return phoneNum;
		}
		//首位�?1，且长度12位，�?.
		if(phoneNum.length() == 12 && phoneNum.startsWith("01")){
			phoneNum = phoneNum.substring(1);
		}
		logger.warn("手机号码[" + phoneNum + "]不符合手机号码校验规�?");
		return phoneNum;
	}
	
}