package net.chinanets.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.dom4j.DocumentHelper;
import org.springframework.context.ApplicationContext;

import flex.messaging.MessageBroker;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.util.UUIDUtils;

public class CommonMethods {
	public static SimpleDateFormat commonDateFormate = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat longDateFormate = new SimpleDateFormat("yyyy年MM月dd日");
	public static final int MinRyhcsl = 20;//日用耗材数量提醒的阀值
	public static ArrayList<String> LoginedUserArray = new ArrayList<String>();//单点登录， 验证成功的UserId
	
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
	
	//获取设备标识     公共方法
	//根据设备名称获取 设备的所属表 和  标识字段
	//返回 表名;标识字段
	public static String getSbbs(String assetName){
		String tableName ="",bctj ="";
		if(assetName != null){
			if(assetName.equals("台式机")){ tableName = "ASSET_COMPUTER"; bctj = "zclxid = '361'"; }
			if(assetName.equals("笔记本") || assetName.equals("便携电脑")){ tableName = "ASSET_COMPUTER"; bctj = "zclxid = '362' "; }
			if(assetName.equals("显示器")){ tableName = "ASSET_COMPUTER"; bctj = "zclxid = '363'"; }
			if(assetName.equals("U盘")){tableName="ASSET_JZ";bctj=" mc ='U盘' ";}
			if(assetName.equals("移动硬盘")){tableName="ASSET_JZ";bctj=" mc ='移动硬盘' ";}
			if(assetName.equals("录音笔")){tableName="ASSET_JZ";bctj=" mc ='录音笔' ";}
			if(assetName.equals("打印机")){tableName="ASSET_Wssb";bctj=" zclxid ='364' ";}
			if(assetName.equals("扫描仪")){tableName="ASSET_Wssb";bctj=" zclxid ='365' ";}
			if(assetName.equals("传真机")){tableName="ASSET_Wssb";bctj=" zclxid ='366' ";}
			if(assetName.equals("机顶盒")){tableName="ASSET_Wssb";bctj=" zclxid ='367' ";}
			if(assetName.equals("视频切换器")){tableName="ASSET_Wssb";bctj=" zclxid ='368' ";}
			if(assetName.equals("速录机")){tableName="ASSET_Wssb";bctj=" zclxid ='369' ";}
			if(assetName.equals("音箱")){tableName="ASSET_Wssb";bctj=" zclxid ='370' ";}
			if(assetName.equals("数字录音电话")){tableName="ASSET_Wssb";bctj=" zclxid ='371' ";}
			if(assetName.equals("单兵摄录仪")){tableName="ASSET_Wssb";bctj=" zclxid ='372' ";}
			if(assetName.equals("读卡器")){tableName="ASSET_Wssb";bctj=" zclxid ='373' ";}
			if(assetName.equals("电视机")){tableName="ASSET_Wssb";bctj=" zclxid ='374' ";}
			if(assetName.equals("小型机")){  tableName="ASSET_SERVER";bctj=" zclx ='小型机' ";}
			if(assetName.equals("工控机")){  tableName="ASSET_SERVER";bctj=" zclx ='工控机' ";}
			if(assetName.equals("PC服务器")){  tableName="ASSET_SERVER";bctj=" zclx ='PC服务器' ";}
			if(assetName.equals("刀片服务器")){  tableName="ASSET_SERVER";bctj=" zclx ='刀片服务器' ";}
			if(assetName.equals("磁盘阵列")){tableName="ASSET_STORAGE";bctj=" zclx ='磁盘阵列' ";}
			if(assetName.equals("磁盘")){tableName="ASSET_STORAGE";bctj=" zclx ='磁盘' ";}
			if(assetName.equals("磁带")){tableName="ASSET_STORAGE";bctj=" zclx ='磁带' ";}
			if(assetName.equals("磁盘扩展柜")){tableName="ASSET_STORAGE";bctj=" zclx ='磁盘扩展柜' ";}
			if(assetName.equals("磁盘库")){tableName="ASSET_STORAGE";bctj=" zclx ='磁盘库' ";}
			if(assetName.equals("路由器")){tableName="ASSET_NET";bctj=" zclx ='路由器' ";}
			if(assetName.equals("交换机")){tableName="ASSET_NET";bctj=" zclx ='交换机' ";}
			if(assetName.equals("安全设备")){tableName="ASSET_NET";bctj=" zclx ='安全设备' ";}
			if(assetName.equals("光纤收发器")){tableName="ASSET_NET";bctj=" zclx ='光纤收发器' ";}
			if(assetName.equals("录像设备")){tableName="ASSET_VIDEO";bctj=" zclxid ='367' ";}
			if(assetName.equals("摄像设备")){tableName="ASSET_VIDEO";bctj=" zclxid ='368' ";}
			if(assetName.equals("投影机")){tableName="ASSET_VIDEO";bctj=" zclxid ='369' ";}
			if(assetName.equals("显示屏")){tableName="ASSET_VIDEO";bctj=" zclxid ='370' ";}
			if(assetName.equals("音响设备")){tableName="ASSET_VIDEO";bctj=" zclxid ='371' ";}
			if(assetName.equals("UPS")){tableName="ASSET_UPS";bctj=" zclx ='UPS' ";}
		}
		return tableName+";"+ bctj;
	}
	public static void main(String args[]){
		System.out.println(isNullOrWhitespace("		"));
	}
	
}
