package net.chinanets.excel;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 字符串工具类
 * @author 
 */
public class StringUtil {
	/**
	 * 输出本类的相关log日志
	 */
	protected static final Logger logger = Logger.getLogger(StringUtil.class);
	/**
	 * 比较两个字符串，将页面上传给后台�?"与数据库返回的null看做�?��
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean equalsWithinPage(String str1,String str2){
		String trimStr1 = StringUtils.trimToNull(str1);
		String trimStr2 = StringUtils.trimToNull(str2);
		return StringUtils.equals(trimStr1, trimStr2);
	}

	/**
	 * 将对象转换为字符串，若为空，则返�?"
	 * @param obj
	 * @return
	 */
	public static String convertToString(Object obj){
		if(obj == null){
			return "";
		}else{
			if(Date.class.isInstance(obj)){
				return StringDateUtil.dateToString((Date)obj, 4);
			}
			return obj.toString();
		}
	}

	/**
	 * 解决properties文件的乱�?
	 * properties默认的字符串�?ISO8859-1"编码，将之转换为byte重新�?UTF-8"编码
	 * @param strValue
	 * @return
	 */
	public static String formatPropertiesCN(String strValue){
		try {
			return new String(strValue.getBytes("ISO8859-1"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public static boolean stringNoBlank(String dest){
		if(dest == null || dest.trim().equals("")){
			return false;
		}
		return true;
	}

	/**
	 * 文本文件转换为指定编码的字符�?
	 * 
	 * @param file         文本文件
	 * @param encoding 编码类型
	 * @return 转换后的字符�?
	 * @throws IOException
	 */
	public static String fileToString(File file, String encoding) {
		InputStreamReader reader = null;
		StringWriter writer = new StringWriter();
		try {
			if (encoding == null || "".equals(encoding.trim())) {
				reader = new InputStreamReader(new FileInputStream(file));
			} else {
				reader = new InputStreamReader(new FileInputStream(file), encoding);
			}
			//将输入流写入输出�?
			char[] buffer = new char[1024];
			int n = 0;
			while (-1 != (n = reader.read(buffer))) {
				writer.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		//返回转换结果
		if (writer != null)
			return writer.toString();
		else return null;
	}

	/**
	 * 将字符串写入指定文件(当指定的父路径中文件夹不存在时，会最大限度去创建，以保证保存成功�?
	 * @param res 原字符串
	 * @param filePath 文件路径
	 * @return 成功标记
	 * @author shixiaobao
	 */
	public static boolean stringToFile(String res, File distFile,String charset) {
		boolean flag = true;
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			//File distFile = new File(filePath);
			if (!distFile.getParentFile().exists()) distFile.getParentFile().mkdirs();
			bufferedReader = new BufferedReader(new StringReader(res));
			if(charset==null)
			{
				bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			}else {
				bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(distFile), charset));
			}
			char buf[] = new char[1024];         //字符缓冲�?
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
			return flag;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}
	/**
	 * 过滤字符串中的html标签
	 * @param inputString
	 * @return
	 */
	public static String html2Text(String inputString) {
		String htmlStr = inputString;
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		java.util.regex.Pattern p_html1;
		java.util.regex.Matcher m_html1;

		try {
			// 定义script的正则表达式{�?script[^>]*?>[\\s\\S]*?<\\/script>
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; 
			// 定义style的正则表达式{�?style[^>]*?>[\\s\\S]*?<\\/style>
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; 
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		return textStr;// 返回文本字符�?
	}
	
	/**
	 * Unicode还原，比�?#38142;&#25509;作为参数传入，则会返回汉字�?链接�?
	 */
	public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            int system = 10;//进制
            if(start==0){
                int t = dataStr.indexOf("&#");
                if(start!=t)start = t;
            }
            end = dataStr.indexOf(";", start + 2);
            String charStr = "";
            if (end != -1) {
                charStr = dataStr.substring(start + 2, end);

                //判断进制
                char s = charStr.charAt(0);
                if(s=='x' || s=='X'){
                    system = 16;
                    charStr = charStr.substring(1);
                }
            }
            //转换
            try{
                char letter = (char) Integer.parseInt(charStr,system);
                buffer.append(new Character(letter).toString());
            }catch(NumberFormatException e){
                e.printStackTrace();
            }

            //处理当前unicode字符到下�?��unicode字符之间的非unicode字符
            start = dataStr.indexOf("&#",end);
            if(start-end>1){
                buffer.append(dataStr.substring(end+1, start));
            }

            //处理�?��面的非unicode字符
            if(start==-1){
                int length = dataStr.length();
                if(end+1!=length){
                    buffer.append(dataStr.substring(end+1,length));
                }
            }
        }
        return buffer.toString();
    }
	
	public static String strToHhtmEntites(String source){
		StringBuffer rtnString= new StringBuffer("");
		
		char[] chars = source.toCharArray();
		for (char c : chars) {
			rtnString.append("&#").append(Integer.toString(c,10)).append(";");
		}
		
		return rtnString.toString();
	}
	
	/**
	 * @Description: 子字符串在父串中出现的次�?
	 * @param @param parStr
	 * @param @param subStr
	 * @return int  
	 * @author <a href="mailto:liujj@china.kjlink.com">Liu Jinjing</a>
	 */
	public static int getSubStrCount(String parStr, String subStr) {
		int length = parStr.length();
		int index = 0;
		int count = 0;
		while (index != -1) {
			index = parStr.indexOf(subStr);
			if(index != -1 && index + subStr.length() < length){
				parStr = parStr.substring(index + subStr.length());
			}else {
				break;
			}
			count++;
		}
		return count;
	}

	/**
	 * 将不正规浮点型的数字字符串转换成int
	 * 14.00000 -> 14
	 * @param doubleStr
	 * @return Integer: 因入参有可能为空，故返回值不采用基础数据类型
	 */
	public static Long double2Long(String doubleStr){
		Long returnLong = null;
		if (StringUtils.isBlank(doubleStr)) {
			return returnLong;
		}
		double doubleTempt = Double.parseDouble(doubleStr);
		returnLong = Math.round(doubleTempt);
		return returnLong;
	}
	
	/**
	 * 去除数组中重复的字符�?
	 */
	public static String[] filterDuplicateStringInArray(String[] array) {
		if(array==null||array.length==0){
			return array;
		}
		Map<String, String> map = new HashMap<String, String>();
		for (String str : array) {
			map.put(str, "");
		}
		return map.keySet().toArray(new String[0]);
	}
	
	/**
	 * 字节数组按照�?��截取的长度和数据编码转换成字符串
	 * @param bytes 原数�?
	 * @param index 截取起始位置
	 * @param count 要截取的总个�?
	 * @param charSet 编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String bytesToString(byte[] bytes,int index, int count, Charset charSet) throws UnsupportedEncodingException {
		if(bytes == null || bytes.length == 0){
			return null;
		}
		count = count > bytes.length ? bytes.length : count;
		byte[] res = new byte[count];
		System.arraycopy(bytes, index, res, 0, count);
//		return new String(res, charSet.getValue()).trim();
		return null;
	}
	
	/**
	 * 判断2个字符串数字是否相等
	 * 例如34.40=34.4,返回false
	 * 非数字返回false
	 * @return
	 */
	public static boolean compareDoubles(String str1, String str2) {
		try {
			if (Double.valueOf(str1).doubleValue() == Double.valueOf(str2).doubleValue()) {
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
	
	/**
	 * 是否为空字符�?
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		if(str == null || str.equals("")){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 判断字符串是否为�?
	 * true不为空，false为空
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str){
		if(str == null || "".equals(str.trim())){
			return false;
		}
		return true;
	}
	
	 /**
	  * String数组转Long数组
	  * @param stringArray
	  * @return
	  */
	 public static Long[] stringToLong_2(String stringArray[]) {
	        if (stringArray == null)
	            return null;
	        return (Long[]) ConvertUtils.convert(stringArray, Long.class);
	 }

}