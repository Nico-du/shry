package net.chinanets.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

public class CommonMethods {
	public static SimpleDateFormat commonDateFormate = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat longDateFormate = new SimpleDateFormat("yyyy年MM月dd日");
	public static final int MinRyhcsl = 20;//日用耗材数量提醒的阀值
	public static ArrayList<String> LoginedUserArray = new ArrayList<String>();//单点登录， 验证成功的UserId
	public static final String  UploadFile_BasePath = "/sysArgFiles/";
	
	
	
	/**
	 * 正则替换所有特殊字符
	 * @param orgStr
	 * @return
	 */
	public static String replaceSpecStr(String orgStr){
		if (null!=orgStr&&!"".equals(orgStr.trim())) {
			String regEx="[\\s~·`!！@#￥$%^……&*（()）\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《<。.》>、/？?]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(orgStr);
			return m.replaceAll("");
		}
		return null;
	}
	
	/**
	 * 四舍五入保留 scale位小数
	 * @param doubleIn
	 * @param scale
	 * @return
	 */
	public static String formateDouble(String doubleIn,int scale){
		if(!isDouble(doubleIn)){ return "not a number";}
		if(scale <0 || scale>100){ return "parameter scale must between (1,100) ";}
		BigDecimal b = new BigDecimal(doubleIn);
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue()+"";
	}
	/**
	 * 四舍五入保留 scale位小数
	 * @param doubleIn
	 * @param scale
	 * @return
	 */
	public static String formateDouble(Double doubleIn,int scale){
		if(doubleIn == null){ return "null doubleIn ";}
		if(scale <0 || scale>100){ return "parameter scale must between (1,100) ";}
		 BigDecimal b = new BigDecimal(doubleIn.toString());
		return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue()+"";
	}
	
	
	public static boolean isNullOrWhitespace(Object str){
		return ((str == null ) || (str.toString().trim().length() == 0));
	}
	
	public static String validateSfnbdj(String str){
		return (str==null || str.trim().length() == 0) ? "否" : str.trim();
		
	}
	
	public static String replaceNullToZero(Object objin){
		if(objin == null){return "0";}
		return objin.toString();
	}
	public static String replaceNullToString(Object objin){
		if(objin == null){return "";}
		return objin.toString();
	}
	
	public static String formateLongData(Object objin){
		if(objin == null){return "";}
		Date curDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			curDate = sdf.parse(objin.toString());
		} catch (ParseException e) {
			return "";
		}

		return longDateFormate.format(curDate);
	}
	
	public static boolean isContainInt(int[] arr,int tar){
		for(int k=0;k<arr.length;k++){
			if(arr[k] == tar){
				return true;
			}
		}
		return false;
	}
	/**
	 * 推送系统消息弹窗
	 * @param userid 推送给指定的登录用户
	 * @param msgbody 推送消息的内容 可在前台取得
	 */
	public static void pushSysMsg(String userid,Object msgbody){
		 MessageBroker msgBroker = MessageBroker.getMessageBroker(null);  
		 String clientID = UUIDUtils.createUUID();  
		 AsyncMessage msg = new AsyncMessage();  
         msg.setDestination("tick-data-feed");  
         msg.setHeader(AsyncMessage.SUBTOPIC_HEADER_NAME, "tick");  
          
         Map<String, String> headers=new HashMap<String,String>();  
         headers.put("userid", userid);  
         msg.setHeaders(headers);  
           
         msg.setClientId(clientID);  
         msg.setMessageId(UUIDUtils.createUUID());  
         msg.setTimestamp(System.currentTimeMillis());  
         msg.setBody(msgbody);  
         msgBroker.routeMessageToService(msg, null);
	}
	
	public static void getSpecificBean(String beanid){
		
		 
	}
	
	 // 根据Unicode编码完美的判断中文汉字和符号 
    private static boolean isChinese(char c) { 
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c); 
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS 
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B 
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS 
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) { 
            return true; 
        } 
        return false; 
    } 
	
    /**
	 * 转换为double数组
	 * @param ary
	 * @return
	 */
    public static  double[] toDoubleAry(String[] ary) {
    	double[] dAry = new double[ary.length];
    	for(int i=0;i<ary.length;i++){
    		dAry[i] = Double.parseDouble(ary[i]);
    	}
    	return dAry;
    }
    
    
	/**
	 * 判断是否double数组
	 * @param ary
	 * @return
	 */
    public static  boolean isDoubleAry(Object[] ary) 
    { 
    	if(ary == null || ary.length <1){ return true;}
    	try 
    	{ 
    		for(Object ch:ary){
    			if(!isDouble(ch+"")){ return false;}
    		}
    		return true;
    	} 
    	catch(Exception ex){} 
    	return false; 
    }
    
	/**
	 * @方法作用描述：判断是否是double类型
	 * @param:
	 * @author: 徐超
	 * @date: 2016-3-13上午12:58:46
	 */
	public static  boolean isDouble(String str) 
	{ 
	try 
	{ 
	Double.parseDouble(str); 
	return true; 
	} 
	catch(Exception ex){} 
	return false; 
	}
	
	/**
	 * 类型判断，只能输入数字
	 * 例如 0.001 小数点前面是0-9的数字零个或者多个，小数点后面
	 */
	public static boolean checkType(String str){
		String regex = "[0-9][1,].[0-9]{0,}[1-9]{1,}$";
	    boolean fleg = str.matches(regex);
		return fleg;
	}
	
	
	/**
	 * 是否为空字符串或者空串
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		if(str == null || str.equals("")){
			return true;
		}
		return false;
	}
	
}
