package net.chinanets.excel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * string to date 的工具类"
 * stringToDate、dateToString、两个dateFormat方法，每种格式需要一起加注释
 * @author
 */
public class StringDateUtil {
	/**
	 * 输出本类的相关log日志
	 */
	protected static final Logger logger = Logger.getLogger(StringDateUtil.class);

	public static final String TIME_FORMAT = "00:00:00";
	public static final String DAY_ENDTIME = "23:59";
	public static final String DAY_BEGINTIME = "00:00";

	/**
	 * 主方" 供临时测试工具类方法，请勿删"
	 * @param args
	 * @author        
	 * @date 2011-1-17
	 */
	public static void main(String args[]) {
		//		System.out.println(StringDateUtil.dateToString(StringDateUtil.AddDay(new Date(), -2), 4));
		//		Map<String, Long> map = new HashMap<String, Long>();
		//		for (int i = 0; i < 5000; i++) {
		//			String str = StringDateUtil.getRandomStrNo(null, -1);
		//			if(map.get(str) == null){
		//				map.put(str, 1L);
		//			}else{
		//				map.put(str, map.get(str) + 1L);
		//			}
		//		}
		//		Iterator<Map.Entry<String, Long>> iter = map.entrySet().iterator();
		//		while(iter.hasNext()){
		//			Entry<String, Long> e = iter.next();
		//			if(e.getValue().longValue() > 1l) {
		//				System.out.println(e.getKey()+"  "+e.getValue());
		//			}
		//		}
		//System.out.println("====" + StringDateUtil.getZeroDate(new Date()));
		//System.out.println(StringDateUtil.checkMinllis(new Date(),StringDateUtil.AddMinute(new Date(), 3)));
		
		Date start = StringDateUtil.stringToDate("2015-10-20", 3);
		Date end = StringDateUtil.stringToDate("2015-10-30", 3);
		Date now = StringDateUtil.dateFormat(new Date(), 3);
		
		System.out.println("比较两个日期的相差毫秒数:"  + now);
		
		System.out.println("比较两个日期的相差毫秒数:"  + (now.compareTo(start)>=0)  + (now.compareTo(end)<=0));
	}

	/**
	 * 返回字符串的字节"
	 * @param str
	 * @author        
	 */
	public static int getStrByteNum(String str) {
		if (str != null) {
			return str.getBytes().length;
		} else {
			return 0;
		}
	}

	/**
	 * 截取指定字节数的字符串，此方法避免把汉字"��为二
	 * @param str
	 * @author        
	 */
	public static String getCutStrByByte(String str, int chinaSize) {
		int byteSize = chinaSize * 3;//"��汉字等于3个字"
		int byteNum = getStrByteNum(str);

		// 字节数少于指定字节数，直接返"
		if (byteNum <= byteSize) {
			return str;
		}

		int len = str.length();
		String result = "";
		for (int i = chinaSize; i < len; i++) {
			String temp = str.substring(0, i);
			if (getStrByteNum(temp) > byteSize) {
				break;
			} else {
				result = temp;
			}
		}

		return result;
	}

	/**
	 * 把string类型的日期转化为相应的格"
	 * @param dateString 字符串日"
	 * @param formatNum "��按照哪种类型
	 * 1 yyyyMMdd
	 * 2 yyyy/MM/dd
	 * 3 yyyy-MM-dd
	 * 4 yyyy-MM-dd HH:mm:ss
	 * 5 yyyyMMddHHmmss
	 * 6 yyyy-MM-dd HH:mm:ss.sss
	 * 7 HH:mm
	 * 8 yyyy年MM月dd"
	 * 9 yyyyMMddHHmmssSSS
	 * 10 yyyy年MM月dd"HH:mm
	 * 11 yyyy-MM-dd HH
	 * 12 HH:mm:ss
	 * 13 yyyy/MM/dd HH:mm:ss
	 * 15 yyyy-MM-dd HH:mm
	 * @return
	 */
	public static Date stringToDate(String dateString, int formatNum) {
		Date date = null;
		if (StringUtils.isNotBlank(dateString)) {
			try {
				date = dateFormat(formatNum).parse(dateString);
			} catch (ParseException e) {
				date = null;
				logger.error("StringToDate转换错误", e);
			}
		}

		return date;
	}
	
	/**
	 * 取得日期的零点，"2014-12-12 00:00:00
	 * @param date
	 * @return
	 * @author        
	 * @date 2014-12-19
	 */
	public static String getZeroDate(Date date) {
		if(date == null){
			return null;
		}
		return dateToString(date, 3) + " " + TIME_FORMAT;
	}

	/**
	 * 把string类型的日期转化为相应的格"
	 * @param dateString 字符串日"
	 * @param formatNum
	 * 1 yyyyMMdd
	 * 2 yyyy/MM/dd
	 * 3 yyyy-MM-dd
	 * 4 yyyy-MM-dd HH:mm:ss
	 * 5 yyyyMMddHHmmss
	 * 6 yyyy-MM-dd HH:mm:ss.sss
	 * 7 HH:mm
	 * 8 yyyy年MM月dd"
	 * 9 yyyyMMddHHmmssSSS
	 * 10 yyyy年MM月dd"HH:mm
	 * 11 yyyy-MM-dd HH
	 * 12 HH:mm:ss
	 * 14 yyyy-MM-ddTHH:mm:ss+08:00
	 * 15 yyyy-MM-dd HH:mm
	 * 16 yyyyMM
	 * 17 yyyy年MM月dd日hh时mm"
	 * @return
	 */
	public static String dateToString(Date date, int formatNum) {
		String str = null;
		if (date != null) {
			str = dateFormat(formatNum).format(date);
		}
		return str;
	}

	/**
	 * 实例SimpleDateFormat
	 * @param formatNum
	 * 1 yyyyMMdd
	 * 2 yyyy/MM/dd
	 * 3 yyyy-MM-dd
	 * 4 yyyy-MM-dd HH:mm:ss
	 * 5 yyyyMMddHHmmss
	 * 6 yyyy-MM-dd HH:mm:ss.sss
	 * 7 HH:mm
	 * 8 yyyy年MM月dd"
	 * 9 yyyyMMddHHmmssSSS
	 * 10 yyyy年MM月dd"HH:mm
	 * 11 yyyy-MM-dd HH
	 * 12 HH:mm:ss
	 * 13 yyyy/MM/dd HH:mm:ss
	 * 14 yyyy-MM-dd'T'HH:mm:ss+08:00
	 * 15 yyyy-MM-dd HH:mm
	 * 16 yyyyMM
	 * 17 yyyy年MM月dd日hh时mm"
	 * 18 yyyy/MM/ddHH:mm:ss
	 * @return
	 */
	private static SimpleDateFormat dateFormat(int formatNum) {
		SimpleDateFormat dateFormatter = null;
		switch (formatNum) {
		case 1:
			dateFormatter = new SimpleDateFormat("yyyyMMdd");
			break;
		case 2:
			dateFormatter = new SimpleDateFormat("yyyy/MM/dd");
			break;
		case 3:
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			break;
		case 4:
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			break;
		case 5:
			dateFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
			break;
		case 6:
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
			break;
		case 7:
			dateFormatter = new SimpleDateFormat("HH:mm");
			break;
		case 8:
			dateFormatter = new SimpleDateFormat("yyyy年MM月dd");
			break;
		case 9:
			dateFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			break;
		case 10:
			dateFormatter = new SimpleDateFormat("yyyy年MM月dd HH:mm");
			break;
		case 11:
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH");
			break;
		case 12:
			dateFormatter = new SimpleDateFormat("HH:mm:ss");
			break;
		case 13:
			dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			break;
		case 14:
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+08:00");
			break;
		case 15:
			dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			break;
		case 16:
			dateFormatter = new SimpleDateFormat("yyyyMM");
			break;
		case 17:
			dateFormatter = new SimpleDateFormat("yyyy年MM月dd日HH时mm");
			break;
		case 18:
			dateFormatter = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");
			break;
		}
		return dateFormatter;
	}

	/**
	 * 按指定格式返回时"实际是舍弃某些格"
	 * （入参是Date对象，出参也是Date对象，比"date是new Date，则
	 * dateFormat(date,1)返回的是时分秒为0的date"
	 * @param date
	 * @param formatNum
	 * 1 yyyyMMdd
	 * 2 yyyy/MM/dd
	 * 3 yyyy-MM-dd
	 * 4 yyyy-MM-dd HH:mm:ss
	 * 5 yyyyMMddHHmmss
	 * 6 yyyy-MM-dd HH:mm:ss.sss
	 * 7 HH:mm
	 * 8 yyyy年MM月dd"
	 * 9 yyyyMMddHHmmssSSS
	 * 10 yyyy年MM月dd"HH:mm
	 * 11 yyyy-MM-dd HH
	 * 12 HH:mm:ss
	 * 15 yyyy-MM-dd HH:mm
	 * @return
	 */
	public static Date dateFormat(Date date, int formatNum) {
		return stringToDate(dateToString(date, formatNum), formatNum);
	}

	/**
	 * 给一个日期加上N天或减去N天得到一个新的日"
	 * @param startDate "��增加的日期时"
	 * @param addnos 添加的天数，可以是正数也可以是负"
	 * @return 操作后的日期
	 */
	public static Date AddDay(Date startDate, int addnos) {
		if (startDate == null) {
			startDate = new Date();
		}

		Calendar cc = Calendar.getInstance();
		cc.setTime(startDate);
		cc.add(Calendar.DATE, addnos);
		return cc.getTime();
	}

	/**
	 * 给一个日期加上N小时或减去N小时得到"��新的日期
	 * @param baseTime "��增加的日期时"
	 * @param amountToAdd 添加的小时数，可以是正数也可以是负数
	 * @return 操作后的日期
	 */
	public static Date addHours(Date baseTime, int amountToAdd) {
		if (baseTime == null) {
			baseTime = new Date();
		}
		Calendar cc = Calendar.getInstance();
		cc.setTime(baseTime);
		cc.add(Calendar.HOUR, amountToAdd);
		return cc.getTime();
	}

	/**
	 * 给一个字符格式的日期加上N天或减去N天得到一个新的日"
	 * @param stringDate "��增加的日期时"
	 * @param addnos 添加的天数，可以是正数也可以是负"
	 * @return 操作后的日期
	 */
	public static String AddDay(String stringDate, int addnos) {
		int f = 3;
		if (stringDate.indexOf(":") != -1) {
			f = 4;
		}
		Date date = AddDay(stringToDate(stringDate.trim(), f), addnos);
		return dateToString(date, f);
	}

	/**
	 * 给一个时间加上N分种或减去N分种后得到一个新的日"
	 * @param startDate "��增加的日期时"
	 * @param addnos 添加的分钟数，可以是正数也可以是负数
	 * @return 操作后的日期
	 */
	public static Date AddMinute(Date startDate, int addnos) {
		Calendar cc = Calendar.getInstance();
		if (startDate != null) {
			cc.setTime(startDate);
			cc.add(Calendar.MINUTE, addnos);
			return cc.getTime();
		} else {
			return null;
		}
	}

	/**
	 * 添加月份
	 * @param startDate
	 * @param addnos
	 * @return
	 */
	public static Date addMonth(Date startDate, int addnos) {
		Calendar cc = Calendar.getInstance();
		if (startDate != null) {
			cc.setTime(startDate);
			cc.add(Calendar.MONTH, addnos);
			return cc.getTime();
		} else {
			return null;
		}
	}

	/**
	 * 给一个时间加上N年或减去N年后得到"��新的日期
	 * @param startDate "��增加的日期时"
	 * @param addnos 添加的年数，可以是正数也可以是负"
	 * @return 操作后的日期
	 */
	public static Date AddYear(Date startDate, int addnos) {
		if (startDate == null){
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		int currenYear = calendar.get(Calendar.YEAR);
		calendar.set(Calendar.YEAR, currenYear + addnos);

		return calendar.getTime();
	}

	/**
	 * 得到当前年份
	 * @return
	 */
	public static int getNowYear() {
		Calendar cc = Calendar.getInstance();
		return cc.get(Calendar.YEAR);
	}

	/**
	 * 得到当前月份
	 * @return
	 */
	public static int getNowMonth() {
		Calendar cc = Calendar.getInstance();
		return cc.get(Calendar.MONTH) + 1;
	}

	/**
	 * 比较两个日期的相差毫秒数,如果"��日期比结束日期早，则返回正数，否则返回负数"
	 * @param start "��日期
	 * @param end 结束日期
	 * @return
	 */
	public static long compareDate(Date start, Date end) {
		long temp = 0;
		Calendar starts = Calendar.getInstance();
		Calendar ends = Calendar.getInstance();
		starts.setTime(start);
		ends.setTime(end);
		temp = ends.getTime().getTime() - starts.getTime().getTime();
		return temp;
	}

	/**
	 * 比较两个日期的相差天"如果"��日期比结束日期早，则返回正数，否则返回负数"
	 * @param start
	 * @param end
	 * @return
	 */
	public static long compareDay(Date start, Date end) {
		long day = compareDate(start, end);
		return day / 1000 / 60 / 60 / 24;
	}

	/**
	 * 比较两个日期的相差天"如果"��日期比结束日期早，则返回正数，否则返回负数" 0天返"、小于等于一天返"、大于一天返"、依次类"
	 * @param start
	 * @param end
	 * @return
	 */
	public static long dayDiff(Date start, Date end) {
		long day = compareDate(start, end);
		if (day == 0) {
			return 0;
		}
		if (day > 0) {
			return (day - 1) / 1000 / 60 / 60 / 24 + 1;
		} else {
			return (day + 1) / 1000 / 60 / 60 / 24 - 1;
		}

	}

	/**
	 * 得到"��日期的星期几数，星期日返"以此类推,与真实的要减"
	 * @param date
	 * @return
	 */
	public static int getDateIsWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int week = c.get(Calendar.DAY_OF_WEEK);
		return week;
	}
	/**
	 * 日期增加减几"
	 * days:天数,可以为负
	 * @param startDate
	 * @param days
	 * @return
	 */
	public static Date addDates(Date startDate,int days) {
		Calendar cc = Calendar.getInstance();
		if (startDate != null) {
			cc.setTime(startDate);
			cc.add(Calendar.DATE, days);
			return cc.getTime();
		} else {
			return null;
		}
	}
	
	/**
	 * Date转Calendar
	 * @param date
	 */
	public static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date != null) {
			calendar.setTime(date);
			return calendar;
		} else {
			return null;
		}
	}

	/**
	 * 返回"��日期的星期字符串
	 * @param date
	 * @return
	 */
	public static String getDateToWeek(Date date) {
		int w = getDateIsWeek(date);
		String week = "星期";
		switch (w) {
		case 1:
			week += "";
			break;
		case 2:
			week += "";
			break;
		case 3:
			week += "";
			break;
		case 4:
			week += "";
			break;
		case 5:
			week += "";
			break;
		case 6:
			week += "";
			break;
		case 7:
			week += "";
			break;
		}
		return week;
	}

	/**
	 * 根据日期得到入司时长<br>
	 * 1-3"1;3-6"3;6-12"6;12-24"12;24月以"24;
	 * 请注意，如果"��员工"008-01-01进入公司的，那么"008
	 * -04-01那天仍然算是(1-3",2008-04-02就算"3-6""
	 * @param startWorkTime 入职时间
	 * @return 1,3,6,12,24
	 */
	public static int getJoinTime(Date startWorkTime) {
		if (startWorkTime == null) {
			return 1;
		}
		Date nowDate = dateFormat(AddDay(new Date(), 1), 3);
		Date newdate = addMonth(startWorkTime, 3);
		long time = compareDate(newdate, nowDate);
		if (time > 0) {
			// 超过3个月
			newdate = addMonth(startWorkTime, 6);
			time = compareDate(newdate, nowDate);
			if (time > 0) {
				newdate = addMonth(startWorkTime, 12);
				time = compareDate(newdate, nowDate);
				if (time > 0) {
					newdate = addMonth(startWorkTime, 24);
					time = compareDate(newdate, nowDate);
					if (time > 0) {
						return 24;
					} else {
						return 12;
					}
				} else {
					return 6;
				}
			} else {
				return 3;
			}
		} else {
			return 1;
		}
	}

	/**
	 * 提供精确的加法运算"
	 * @param v1 被加"
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static double doubleAdd(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return (b1.add(b2)).doubleValue();
	}

	/**
	 * 将DOUBLE类型的数据进行格式化
	 * @param str double类型数据
	 * @param max 保存小数点后"��位数"
	 * @param min 如果小数点后都为0，则保留"��位数
	 * @return
	 */
	public static String NumberFormat(double str, int max, int min) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(max);
		nf.setMinimumFractionDigits(min);
		return nf.format(str);
	}

	/**
	 * 将string类型转化为double类型,默认"
	 * @param doublestr
	 */
	public static double stringTodouble(String doublestr) {
		double d = 0d;
		if (doublestr == null || doublestr.length() == 0) {
			return d;
		}
		try {
			d = Double.parseDouble(doublestr);
		} catch (Exception e) {
			return d;
		}
		return d;
	}

	/**
	 * 将日期格式转化为java.sql.Date类型，如果入参为null，则返回null
	 * @param date
	 * @return
	 */
	public static java.sql.Date toSqlDate(Date date) {
		if (date != null) {
			return new java.sql.Date(date.getTime());
		} else {
			return null;
		}
	}

	/**
	 * 将传入的s数转化为时分秒格"
	 * @param time
	 * @return
	 */
	public static String convertTime(Long time) {
		long timelong = time.longValue();
		if (timelong == 0) {
			return TIME_FORMAT;
		}
		if (timelong > 0) {
			long h = 0, hh = 0;
			long m = 0, mm = 0;
			long s = 0;
			h = timelong / 3600;
			hh = timelong % 3600;
			m = hh / 60;
			mm = hh % 60;
			s = mm;
			String hStr, mStr, sStr = "";
			if (h < 10) {
				hStr = "0" + h;
			} else {
				hStr = "" + h;
			}
			if (m < 10) {
				mStr = "0" + m;
			} else {
				mStr = "" + m;
			}
			if (s < 10) {
				sStr = "0" + s;
			} else {
				sStr = "" + s;
			}
			// if (h > 0) {
			// return hStr + ":" + mStr + ":" + sStr;
			// }
			// if (h == 0 && m > 0) {
			// return mStr + ":" + s;
			// }
			// if (m == 0 && s > 0) {
			// return s + "s";
			// }
			return hStr + ":" + mStr + ":" + sStr;
		}
		return TIME_FORMAT;
	}

	/**
	 * 判断字符串是否是Double
	 * @param str
	 * @return
	 */
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?\\d*(\\.\\d*)?");
		return pattern.matcher(str).matches();
	}

	/**
	 * 根据分割符，把字符串转化集合Set
	 * @param str
	 * @param sign
	 * @return
	 */
	public static Set strToTreeSet(String str, String split) {
		Set set = new HashSet();
		if (str != null) {
			set = new HashSet(Arrays.asList(str.split(split)));
		}
		return set;
	}

	/**
	 * 取得某一时间的某"��时刻的时"
	 * @param date 某一时间
	 * @param millisecond 毫秒"
	 */
	public static String getNeedDate(Date date, long millisecond) {
		Date needDate = new Date();
		needDate.setTime(date.getTime() + millisecond);

		return dateFormat(4).format(needDate);
	}

	/**
	 * 返回接口"��的格式时"
	 * @param date
	 * @return
	 */
	public static String getInterFaceTime(Date date) {
		if (date != null) {
			String dateStr = dateToString(date, 4);
			return dateStr.substring(0, 10) + "T" + dateStr.substring(11);
		} else {
			return null;
		}
	}

	/**
	 * 接口时间String "本地日期Date
	 * @param dateStr
	 * @return
	 */
	public static Date getInterFaceToLocalTime(String dateStr) {
		if (dateStr != null) {
			dateStr = dateStr.replaceAll("T", " ");
			Date date = StringDateUtil.stringToDate(dateStr, 4);
			return date;
		} else {
			return null;
		}
	}
	
	/**
	 * 接口时间String "本地日期Date
	 * @param dateStr
	 * @return
	 */
	public static String getInterFaceToTimeStr(String dateStr) {
		if (dateStr != null) {
			return dateStr.replaceAll("T", " ");
		} else {
			return null;
		}
	}

	/**
	 * 判断字符串是否是HH:MM的格"
	 * @param str
	 * @return
	 */
	public static boolean isTime(String str) {
		Pattern pattern = Pattern.compile("^[0-9]{2}:[0-9]{2}");
		return pattern.matcher(str).matches();
	}

	/**
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param date
	 * @return
	 * @author        
	 * @date 2014-9-29
	 */
	public static Date getDateTime(String beginTime, String endTime, Date date){
		String dateString = StringDateUtil.dateToString(date, 3);
		String fromTime = StringDateUtil.dateToString(date, 7);
		if(beginTime == null || endTime == null){
			return date;
		}
		//"��入参的格"是不"HH:MM
		if(!StringDateUtil.isTime(beginTime) || !StringDateUtil.isTime(endTime)){
			return date;

		}
		if(beginTime.compareTo(endTime)>=0){//跨天
			if(fromTime.compareTo(endTime)<=0){//当前时间小于等于结束时间，说明开始时间是前一天的
				dateString = dateString + " " +beginTime+":00";
				return StringDateUtil.AddDay(StringDateUtil.stringToDate(dateString, 4), -1);

			}else if(fromTime.compareTo(beginTime)>0){
				dateString = dateString + " " +beginTime+":00";
				return StringDateUtil.stringToDate(dateString, 4);
			}else{
				return date;
			}
		}else{//不跨天，返回当天日期加beginTime
			dateString = dateString + " " +beginTime+":00";
			return StringDateUtil.stringToDate(dateString, 4);
		}
	}

	/**
	 * 根据指定时间（"常为当前时间）比较开始时间和结束时间来决定短信发送时"此处的比较只精确到分钟，秒忽略不"
	 * @param beginTime
	 * @param endTime
	 * @param date
	 * @return
	 */
	public static Date getCompareTimeResult(String beginTime, String endTime, Date date) {
		if (null != beginTime && beginTime.equals(endTime)) {// 24小时情况
			return date;
		}
		Date returnDate;
		String formTime = StringDateUtil.dateToString(date, 7);
		if (beginTime.compareTo(endTime) > 0) {// 这个条件说明时间段存在跨"
			// 注意:formDate 没有"24:00"的情"
			if (null == formTime) {
				return null;
			}
			if ((beginTime.compareTo(formTime) <= 0 && formTime.compareTo(DAY_ENDTIME) <= 0)
					|| (DAY_BEGINTIME.compareTo(formTime) <= 0 && formTime.compareTo(endTime) <= 0)) {
				returnDate = date;
			} else {// 非跨天段，当天发"
				// (DAY_BEGINTIME.compareTo(formDate)<=0&&beginTime.compareTo(formDate)<=0)
				returnDate = concatDate(date, beginTime);
			}
		} else {
			// 正常的非跨天情况
			if (beginTime.compareTo(formTime) <= 0 && formTime.compareTo(endTime) <= 0) {
				returnDate = date;
			} else {// 跨天段，判断是取当天还是次日
				if (DAY_BEGINTIME.compareTo(formTime) <= 0
						&& formTime.compareTo(endTime) < 0) {// 0点以后的，在"��时间前的，取当天日期
					returnDate = concatDate(date, beginTime);
				} else {
					returnDate = concatDate(AddDay(date, 1), beginTime);
				}
			}
		}
		return returnDate;
	}

	/**
	 * 根据当前日期与指定时间，拼接成yyyy-mm-dd HH:mm:ss格式的新的日"
	 * @param date
	 * @param formTime
	 * @return
	 */
	public static Date concatDate(Date date, String formTime) {
		StringBuffer buffer = new StringBuffer();
		String formDay = StringDateUtil.dateToString(date, 3);
		buffer.append(formDay);
		buffer.append(" ");
		if (null == date) {
			return null;
		}
		String[] timeArray = new String[3];
		if (formTime != null && !"".equals(formTime)) {
			String formTimeArray[] = formTime.split(":");
			int flag = 0;
			for (int i = 0; i < formTimeArray.length; i++) {
				if (i == 3) {
					break;
				}
				timeArray[i] = formTimeArray[i];
				flag++;
			}
			for (int j = flag; j < 3; j++) {
				timeArray[j] = "00";
			}
		} else {
			for (int i = 0; i < 3; i++) {
				timeArray[i] = "00";
			}
		}
		for (int i = 0; i < timeArray.length; i++) {
			buffer.append(timeArray[i]);
			if (i == 2) {
				break;
			}
			buffer.append(":");
		}
		return stringToDate(buffer.toString(), 4);
	}

	/**
	 * 根据"��日期(YYYY-MM-DD)，结束日"YYYY-MM-DD)"
	 * "��时间(HH24:MM)，结束时"HH24:MM)，判断当前时刻是否有效，注意跨天
	 * @param startDate  "��日期(YYYY-MM-DD)
	 * @param endDate	  结束日期(YYYY-MM-DD)
	 * @param beginTime	 "��时间(HH24:MM)
	 * @param closeTime  结束时间(HH24:MM)
	 * @param needDate   "��判断日期(YYYY-MM-DD HH24:MM:SS)
	 * @return boolean
	 * @author        
	 * @date 2011-9-21
	 */
	public static boolean isValidDate(Date startDate,Date endDate, String beginTime,String closeTime, Date needDate){
		if(null == startDate || null == endDate || StringUtils.isBlank(beginTime) || StringUtils.isBlank(closeTime) || null == needDate){
			return false;
		}else{
			Date bt = stringToDate(beginTime,7);
			Date et = stringToDate(closeTime,7);
			Long days = StringDateUtil.dayDiff(startDate, endDate) + 1;//判断"��日期  <= 结束日期
			if(days>0){
				List<ForValidDate> lstFvd = new ArrayList<ForValidDate>();
				if(bt.before(et)){//不跨"
					for(int i = 0; i < days; i++){
						ForValidDate vd = new ForValidDate();
						vd.setBeginDate(concatDate(AddDay(startDate,i), beginTime));
						vd.setEndDate(concatDate(AddDay(startDate,i), closeTime));
						lstFvd.add(vd);
					}
				}else{
					for(int i = 0; i < days; i++){
						ForValidDate vd = new ForValidDate();
						vd.setBeginDate(concatDate(AddDay(startDate,i), beginTime));
						vd.setEndDate(concatDate(AddDay(startDate,i+1), closeTime));
						lstFvd.add(vd);
					}
				}
				if(lstFvd.isEmpty()){
					return false;
				}else{
					boolean flag = false;
					for (ForValidDate forValidDate : lstFvd) {
						if(!needDate.before(forValidDate.getBeginDate()) && !needDate.after(forValidDate.getEndDate())){
							flag = true;
							break;
						}
					}
					return flag;
				}
			}else{
				return false;
			}
		}
	}

	static class ForValidDate{
		Date beginDate;
		Date endDate;
		public Date getBeginDate() {
			return beginDate;
		}
		public void setBeginDate(Date beginDate) {
			this.beginDate = beginDate;
		}
		public Date getEndDate() {
			return endDate;
		}
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
	}

	/**
	 * 回访根据投保日期和投保人出生年月来判断投保人的年"
	 * 投保人出生：1989-10，如果投保日期是2011-09，则投保人投保年龄为21岁；
	 * 如果投保日期"011-10，则投保人投保年龄为22"
	 * 同一年"么显示，如果超过月份就显"，没超过就显"
	 * "��不会出现投保日期的年月比投保人出生年月早的情况"
	 * @param PolicyAppdate 投保日期
	 * @param HolderYearMonth 投保人出"
	 * @return age
	 */
	public static String reAge(Date policyAppdate,String holderYearMonth){
		String age = null;
		int ageTemp =0;
		String date1 = dateToString(policyAppdate, 3);
		String year1 = date1.split("-")[0];
		String month1 = date1.split("-")[1];
		String year2 = holderYearMonth.split("-")[0];
		String month2 = holderYearMonth.split("-")[1];
		int i = Integer.parseInt(month1);
		int j = Integer.parseInt(month2);
		int m = Integer.parseInt(year1);
		int n = Integer.parseInt(year2);
		ageTemp = m - n ;
		if(i>=j){
			if(ageTemp>0){
				age = String.valueOf(ageTemp);
			}else{
				age = "0";
			}
		}else{
			if(ageTemp>0){
				age = String.valueOf(ageTemp-1);
			}else{
				age = "0";
			}
		}
		return age;
	}

	/**
	 * 计算年龄
	 * @param birthday 出生日期 yyyy-mm-dd
	 * @return
	 */
	public static int calculateAge(String birthday){
		String date = dateToString(new Date(), 1);
		Long nowDate = Long.parseLong(date);
		Long birth = Long.parseLong(birthday.replace("-", ""));
		String str = String.valueOf(nowDate - birth);
		int age = Integer.parseInt(str.substring(0, str.length()-4));
		if(age < 0){
			logger.warn("出生日期错误");
		}
		return age;
	}

	/**
	 * 如果有空值，返回-1
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static double calculateYear(Date startDate, Date endDate) {
		double difference = -1;
		if (startDate != null && endDate != null) {
			double time = endDate.getTime() - startDate.getTime();
			double year = StringDateUtil.stringToDate("2014-01-21", 3).getTime()
			- StringDateUtil.stringToDate("2013-01-22", 3).getTime();
			difference = ((int) Math.round((time * 100) / year)) / 100.00;
		}
		return difference;
	}

	/**
	 * 计算两个日期相差几天
	 * @param startTime
	 * @param endTime
	 * @param format  如：yyyy-MM-dd
	 * @return
	 */
	public static long dateDiff(String startTime, String endTime, String format) {
		//按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000*24*60*60;//"��的毫秒数
		long nh = 1000*60*60;//"��时的毫秒"
		long nm = 1000*60;//"��钟的毫秒"
		long ns = 1000;//"��钟的毫秒"
		long diff;
		try {
			//获得两个时间的毫秒时间差"
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			long day = diff/nd;//计算差多少天
			long hour = diff%nd/nh;//计算差多少小"
			long min = diff%nd%nh/nm;//计算差多少分"
			long sec = diff%nd%nh%nm/ns;//计算差多少秒
			//输出结果
			return day;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 根据当前日期随机获取字符串（抬头+8位当前日"num位随机字符）
	 * 目前申请保单挂起批单号采用此方法生成
	 * @param headStr 随机字符串抬头字符，默认以P17抬头
	 * @return
	 */
	public static String getRandomStrNo(String headStr, int num) {
		StringBuilder result = new StringBuilder();
		if (StringUtils.isNotBlank(headStr)) {
			result.append(headStr);
		} else {
			result.append("P17"); // 3位抬"
		}
		result.append(new SimpleDateFormat("yyMMddHH").format(new Date()));// 8位时"
		Random random = new Random();
		String randomStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		// 默认4位随机数
		if (num < 4) {
			num = 4;
		}
		// "的目的是为了下面randomNum-1不存在负"同时randomNum能取值为randomStr的length
		int randomNum = random.nextInt(randomStr.length()) + 1;
		for (int i = 0; i < num; i++) {
			result.append(randomStr.subSequence((randomNum - 1), randomNum));
			randomNum = random.nextInt(randomStr.length()) + 1;
		}
		return result.toString();
	}
	/**
	 * 验证是否日期格式
	 * @param date
	 * @return
	 */
	public static boolean validateDate(String date) {
		if (date != null && !"".equals(date)) {
			Pattern patternMobile = Pattern.compile("^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-" +
					"(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))" +
					"|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$");
			Matcher matcherMobile = patternMobile.matcher(date);
			if (!matcherMobile.matches()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 日历类转化成日期字符"
	 * @author        
	 * @return
	 */
	public static String calendarToTimeStr(Calendar datetime,int formatNum){
		if(datetime == null){
			return null;
		}
		Date date = datetime.getTime();
		return StringDateUtil.dateToString(date, formatNum);
	}
	
	/**
	 * 根据身份证得到证件人的生"yyyy-mm-dd),或"年龄
	 * param idCardNumber 身份"
	 * param type 1生日"年龄
	 */
	public static int getBrithdayByIdCard(String idCardNumber,String type){
        String birthDate="";
	    if(idCardNumber.length() == 15) {  //老版身份证号15"
	        birthDate = 19 + idCardNumber.substring(6,8) + '-' + idCardNumber.substring(8,10) + '-' + idCardNumber.substring(10,12);
	    }else if(idCardNumber.length() == 18) {  //新版身份证号 18"
	        birthDate = idCardNumber.substring(6,10) + '-' + idCardNumber.substring(10,12) + '-' + idCardNumber.substring(12,14);
	    }
	    if("1".equals(type)) {
	        return ObjectUtil.str2Integer(birthDate);
	    }else {
	        return calculateAge(birthDate);
	    }
    }
	
	/**
	 * 校验时间格式是否为YYYY-MM-DD格式
	 * @param dateStr
	 * @return
	 * @author 陈孝"
	 * @date 2015-11-6
	 */
	public static boolean validateDateFormat(String dateStr) {
		if(StringUtils.isBlank(dateStr)) {
			return false;
		}
		//使用正则表达"校验时间范围是否满足YYYY-MM-DD格式
		Pattern p = Pattern.compile("^((?!0000)[0-9]{4}-((0[1-9]|1[0-2])-(0[1-9]|1[0-9]|2[0-8])|(0[13-9]|1[0-2])-(29|30)" +
				"|(0[13578]|1[02])-31)|([0-9]{2}(0[48]|[2468][048]|[13579][26])|(0[48]|[2468][048]|[13579][26])00)-02-29)$");
		Matcher m = p.matcher(dateStr);
		return m.matches();
	}
	
	/**
	 * 把日期格"2014-06-11T00:00:00.000+08:00)中转   yyyy-MM-dd HH:mm:ss
	 * @param datetimeStr
	 * @author  
	 * @date 2014/9/23  
	 */
	public static String converTime(String datetimeStr){
		if(StringUtil.isBlank(datetimeStr)){
			return null;
		}
		if(datetimeStr.trim().length()>0){
			String newTime = datetimeStr.replace("T"," ");
			if(newTime.length()>18){
				return newTime.substring(0, 10) +" " + newTime.substring(11,19);
			}else{
				return newTime;
			}
		}else{
			return null;
		}
	}
}