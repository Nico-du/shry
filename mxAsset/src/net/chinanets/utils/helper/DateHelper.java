package net.chinanets.utils.helper;

import java.text.SimpleDateFormat;
import java.util.TimeZone;


/**
 * 日期类型数据辅助类
 * @author RLiuyong
 *
 */
public class DateHelper {
	/**
	 * 每天的毫秒数
	 */
	public final static long MILLISECONDS_OF_DAY = 86400000;
	
	/**
	 * 默认时区
	 */
	public static TimeZone DEFAULT_TIMEZONE=TimeZone.getDefault();

	public DateHelper(){
	}
	
	/**
	 * 获取DateFormat
	 * @return
	 */
	final public static SimpleDateFormat GetDateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * 获取SimpleDateFormat
	 * @return
	 */
	final public static SimpleDateFormat GetSimpleDateFormat(){
		return new SimpleDateFormat("yyyy-MM-dd");
	}
	
	/**
	 * 根据格式类型获取DateFormat
	 * @param strMode
	 * @return
	 */
	final public static SimpleDateFormat GetDateFormat(String strMode){
		return null;
	}
	
	/**
	 * 根据时区获取DateFormat
	 * @param timeZone
	 * @return
	 */
	final public static SimpleDateFormat GetDateFormat(TimeZone timeZone){
		return null;
	}
	
	/**
	 * 根据格式类型和时区获取DateFormat
	 * @param strMode
	 * @param timeZone
	 * @return
	 */
	final public static SimpleDateFormat GetDateFormat(String strMode,TimeZone timeZone){
		return null;
	}
	
	/**
	 * 根据日期字符串得到java.util.Date对象
	 * @param strValue
	 * @param defaultDate
	 * @return
	 */
	final public static java.util.Date GetUtilDateByDateString(Object objValue,java.util.Date defaultDate){
		if(objValue==null){
			return defaultDate;
		}
		try{
			String strValue=objValue.toString();
			if(StringHelper.IsNullOrEmpty(strValue)){
				return null;
			}
			if(strValue.length() == 10){
				return GetSimpleDateFormat().parse(strValue);
			}
			return GetDateFormat().parse(strValue);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据日期字符串得到java.sql.Timestamp对象
	 * @param strValue
	 * @param defaultDate
	 * @return
	 */
	final public static java.sql.Timestamp GetTimestampByDateString(String strValue,java.sql.Timestamp defaultDate){
		return null;
	}
	
	/**
	 * 根据日期字符串得到java.util.Calendar对象
	 * @param strValue
	 * @param defaultDate
	 * @return
	 */
	final public static java.util.Calendar GetCalendarByDateString(String strValue,java.util.Calendar defaultDate){
		return null;
	}
	
	/**
	 * 根据日期字符串得到java.sql.Date对象
	 * @param strValue
	 * @param defaultDate
	 * @return
	 */
	final public static java.sql.Date GetSqlDateByDateString(String strValue,java.sql.Date defaultDate){
		return null;
	}
	
	/**
	 * 根据日期字符串得到java.sql.Time对象
	 * @param strValue
	 * @param defaultDate
	 * @return
	 */
	final public static java.sql.Time GetSqlTimeByDateString(String strValue,java.sql.Time defaultDate){
		return null;
	}
	
	/**
	 * 根据字符串日期时间得到日期
	 * @param strValue
	 * @param defaultStrDateTime
	 * @return
	 */
	final public static String GetStrDateByDateString(String strValue,String defaultStrDateTime){
		return defaultStrDateTime;
	}
	
	/**
	 * 根据字符串日期时间得到时间
	 * @param strValue
	 * @param defaultStrDateTime
	 * @return
	 */
	final public static String GetStrTimeByDateString(String strValue,String defaultStrDateTime){
		return defaultStrDateTime;
	}
	
	/**
	 * 根据字符串日期时间得到年份
	 * @param strValue
	 * @param defaultYear
	 * @return
	 */
	final public static String GetStrYearByDateString(String strValue,String defaultYear){
		return defaultYear;
	}
	
	/**
	 * 根据字符串日期时间得到月份
	 * @param strValue
	 * @param defaultMouth
	 * @return
	 */
	final public static String GetStrMouthByDateString(String strValue,String defaultMouth){
		return defaultMouth;
	}
	
	/**
	 * 根据字符串日期时间得到天数
	 * @param strValue
	 * @param defaultDay
	 * @return
	 */
	final public static String GetStrDayByDateString(String strValue,String defaultDay){
		return defaultDay;
	}
	
	/**
	 * 根据字符串日期时间得到小时
	 * @param strValue
	 * @param defaultHour
	 * @return
	 */
	final public static String GetStrHourByDateString(String strValue,String defaultHour){
		return defaultHour;
	}
	
	/**
	 * 根据字符串日期时间得到分钟
	 * @param dateValue
	 * @param defaultMinute
	 * @return
	 */
	final public static String GetStrMinuteByDateString(String strValue,String defaultMinute){
		return defaultMinute;
	}
	
	/**
	 * 根据字符串日期时间到秒数
	 * @param strValue
	 * @param defaultSecond
	 * @return
	 */
	final public static String GetStrSecondByDateString(String strValue,String defaultSecond){
		return defaultSecond;
	}	
	
	/**
	 * 根据日期对象获取日期时间字符串
	 * @param dateValue
	 * @param defaultStrDateTime
	 * @return
	 */
	final public static String GetStrDateTimeByDateClass(Object dateValue,String defaultStrDateTime){
		java.util.Date tempDate=GetUtilDateValue(dateValue);
		if(tempDate==null){
			return null;
		}
		try{
			if(GetDateFormat().format(tempDate).endsWith("00:00:00")){
				return GetSimpleDateFormat().format(tempDate);
			}
			return GetDateFormat().format(tempDate);
		}catch(Exception ex){
			return null;
		}
	}
	
	/**
	 * 根据日期对象获日期字符串
	 * @param dateValue
	 * @param defaultStrDate
	 * @return
	 */
	final public static String GetStrDateByDateClass(Object dateValue,String defaultStrDate){
		return defaultStrDate;
	}
	
	/**
	 * 根据日期对象获取时间字符串
	 * @param dateValue
	 * @param defaultStrTime
	 * @return
	 */
	final public static String GetStrTimeByDateClass(Object dateValue,String defaultStrTime){
		return defaultStrTime;
	}
	
	/**
	 * 根据日期对象获取年份字符串
	 * @param dateValue
	 * @param defaultYear
	 * @return
	 */
	final public static String GetStrYearByDateClass(Object dateValue,String defaultYear){
		return defaultYear;
	}
	
	/**
	 * 根据日期对象获取月份字符串
	 * @param dateValue
	 * @param defaultMouth
	 * @return
	 */
	final public static String GetStrMouthByDateClass(Object dateValue,String defaultMouth){
		return defaultMouth;
	}
	
	/**
	 * 根据日期对象获取天数字符串
	 * @param dateValue
	 * @param defaultDay
	 * @return
	 */
	final public static String GetStrDayByDateClass(Object dateValue,String defaultDay){
		return defaultDay;
	}
	
	/**
	 * 根据日期对象获取小时字符串
	 * @param dateValue
	 * @param defaultHour
	 * @return
	 */
	final public static String GetStrHourByDateClass(Object dateValue,String defaultHour){
		return defaultHour;
	}

	/**
	 * 根据日期对象获取分钟字符串
	 * @param dateValue
	 * @param defaultMinute
	 * @return
	 */
	final public static String GetStrMinuteByDateClass(Object dateValue,String defaultMinute){
		return defaultMinute;
	}
	
	/**
	 * 根据日期对象获取秒数字符串
	 * @param dateValue
	 * @param defaultSecond
	 * @return
	 */
	final public static String GetStrSecondByDateClass(Object dateValue,String defaultSecond){
		return defaultSecond;
	}
	
	/**
	 * 得到当前时间微秒数
	 * @return
	 */
	final public static long GetCurrentTimeMillis(){
		return System.currentTimeMillis();
	}
	
	/**
	 * 得到当前时间对象
	 * @return
	 */
	final public static java.util.Date GetCurrentUtilDateTimeClass(){
		return new java.util.Date();
	}
	final public static java.sql.Timestamp GetCurrentTimestampClass(){
		return new java.sql.Timestamp(GetCurrentTimeMillis());
	}
	final public static java.sql.Date GetCurrentSqlDateClass(){
		return new java.sql.Date(GetCurrentTimeMillis());
	}
	final public static java.sql.Time GetCurrentSqlTimeClass(){
		return new java.sql.Time(GetCurrentTimeMillis());
	}
	final public static java.util.Calendar GetCurrentCalendarClass(){
		return java.util.Calendar.getInstance();
	}
	
	/**
	 * 得到当前日期时间
	 * @return
	 */
	final public static String GetCurrentDateTimeString(){
		return GetStrDateTimeByDateClass(GetCurrentUtilDateTimeClass(),"1970-01-01 00:00:00");
	}
	
	/**
	 * 得到当前日期
	 * @return
	 */
	final public static String GetCurrentDateString(){
		return GetStrDateByDateClass(GetCurrentUtilDateTimeClass(),"1970-01-01");
	}
	
	/**
	 * 得到当前时间
	 * @return
	 */
	final public static String GetCurrentTimeString(){
		return GetStrTimeByDateClass(GetCurrentUtilDateTimeClass(), "00:00:00");
	}
	
	/**
	 * 得到当前的年份
	 * @return
	 */
	final public static String GetCurrentYearString(){
		return GetStrYearByDateClass(GetCurrentUtilDateTimeClass(), "1970");
	}
	
	/**
	 * 得到当前的月份
	 * @return
	 */
	final public static String GetCurrentMouthString(){
		return GetStrMouthByDateClass(GetCurrentUtilDateTimeClass(), "01");
	}
	
	/**
	 * 得到当前的天数
	 * @return
	 */
	final public static String GetCurrentDayString(){
		return GetStrDayByDateClass(GetCurrentUtilDateTimeClass(), "01");
	}
	
	/**
	 * 得到当前的小时
	 * @return
	 */
	final public static String GetCurrentHourString(){
		return GetStrHourByDateClass(GetCurrentUtilDateTimeClass(), "00");
	}
	
	/**
	 * 得到当前分钟
	 * @return
	 */
	final public static String GetCurrentMinuteString(){
		return GetStrMinuteByDateClass(GetCurrentUtilDateTimeClass(), "00");
	}
	
	/**
	 * 得到当前秒数
	 * @return
	 */
	final public static String GetCurrentSecondeString(){
		return GetStrHourByDateClass(GetCurrentUtilDateTimeClass(), "00");
	}
	
	/**
	 * 将传入对象转换为 java.util.Date 类型输出
	 * @param objValue
	 * @return
	 */
	public static java.util.Date GetUtilDateValue(Object objValue){
		try{
			if(objValue == null){
				return null;
			}
			if(objValue instanceof java.sql.Date){
				return new java.util.Date(((java.sql.Date) objValue).getTime());
			}
			if(objValue instanceof java.sql.Timestamp){
				return new java.util.Date(((java.sql.Timestamp) objValue).getTime());
			}
			if(objValue instanceof java.util.Date){
				return (java.util.Date) objValue;
			}
			if(objValue instanceof java.util.Calendar){
				return ((java.util.Calendar) objValue).getTime();
			}
			return null;
		}catch (Exception ex){
			return null;
		}
	}
	
	/**
	 * 将传入对象转换为 java.sql.Date 类型输出
	 * @param objValue
	 * @return
	 */
	public static java.sql.Date GetSqlDateValue(Object objValue){
		try{
			if(objValue == null){
				return null;
			}
			if(objValue instanceof java.sql.Date){
				return (java.sql.Date) objValue;
			}
			if(objValue instanceof java.sql.Timestamp){
				java.sql.Timestamp ti = (java.sql.Timestamp) objValue;
				return new java.sql.Date(ti.getTime());
			}
			if(objValue instanceof java.util.Date){
				return new java.sql.Date(((java.util.Date) objValue).getTime());
			}
			return null;
		}catch (Exception ex){
			return null;
		}
	}
	
	/**
	 * 将传入对象转换为 java.sql.Timestamp 类型输出
	 * @param objValue
	 * @return
	 */
	public static java.sql.Timestamp GetTimestampValue(Object objValue){
		try{
			if(objValue == null){
				return null;
			}
			if(objValue instanceof java.sql.Timestamp){
				return (java.sql.Timestamp) objValue;
			}
			if(objValue instanceof java.sql.Date){
				java.sql.Date ti = (java.sql.Date) objValue;
				return new java.sql.Timestamp(ti.getTime());
			}
			if(objValue instanceof java.util.Date){
				return new java.sql.Timestamp(((java.util.Date) objValue).getTime());
			}
			return null;
		}catch (Exception ex){
			return null;
		}
	}
}
