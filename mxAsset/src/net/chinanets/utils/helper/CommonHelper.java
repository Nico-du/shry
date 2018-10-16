package net.chinanets.utils.helper;

import java.rmi.server.UID;
import java.security.MessageDigest;
import java.util.UUID;


/**
 * 自定辅助类
 * @author dzj
 *
 */
public final class CommonHelper{
	public CommonHelper(){}
	private static Integer nGlobalId = 0;

	final public synchronized static String GenAppGlobalId(){
		if (nGlobalId >= 10000)
			nGlobalId = 0;
		nGlobalId++;
		return "u" + nGlobalId.toString();
	}

	//新建一个GUID
	final public static String GenGuid(){
		UID uid = new UID();
		String strId = "uid_" + uid.toString();
		strId = strId.replace(":", "");
		strId = strId.replace("-", "");
		return strId;
		// return strGuid;
	}

	//新建一个GUID
	final public static String GenGuidEx(){
		UUID idOne = UUID.randomUUID();
		return idOne.toString().toUpperCase();
	}

	final private static String convertToHex(byte[] data){
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++){
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do{
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			}while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	//获取指定文本的MD5字符串
	final public static String GenMD5(String text){
		try{
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			byte[] md5hash = new byte[32];
			md.update(text.getBytes("iso-8859-1"), 0, text.length());
			md5hash = md.digest();
			return convertToHex(md5hash);
		}catch (Exception ex){
			return "";
		}
	}
	
	//获取指定文本的MD5字符串
	final	public static String GenMD5Ex(String text){
		try{
			MessageDigest md;
			md = MessageDigest.getInstance("MD5");
			byte[] md5hash = new byte[32];
			md.update(text.getBytes("utf-8"));
			md5hash = md.digest();
			return convertToHex(md5hash);
		}catch (Exception ex){
			return "";
		}
	}

	//获取当前的时间
	final public static long CurTime(){
		return (new java.util.Date()).getTime();
	}
	
	//产生唯一标识
	final public static String GenUniqueId(String strSrc1){
		return GenMD5Ex(strSrc1);
	}
	
	// 产生唯一标识
	final public static String GenUniqueId(String strSrc1,String strSrc2){
		return GenMD5Ex(StringHelper.Format("%1$s||%2$s",strSrc1, strSrc2));
	}
	
	//产生唯一标识
	final public static String GenUniqueId(String strSrc1,String strSrc2,String strSrc3){
		return GenMD5Ex(StringHelper.Format("%1$s||%2$s||%3$s",strSrc1, strSrc2,strSrc3));
	}
	
	//产生唯一标识
	final public static String GenUniqueId(String strSrc1,String strSrc2,String strSrc3,String strSrc4){
		return GenMD5Ex(StringHelper.Format("%1$s||%2$s||%3$s||%4$s",strSrc1, strSrc2,strSrc3,strSrc4));
	}
}
