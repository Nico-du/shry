package net.chinanets.utils.helper;

import java.util.ArrayList;

/**
 * 字符辅助类
 * @author dzj
 *
 */
public final class StringHelper{
	public StringHelper(){

	}
	final public static String Format(String strFormat){
		return strFormat;
	}

	final public static String Format(String strFormat, Object obj1){
		return String.format(strFormat, obj1);
	}

	final public static String Format(String strFormat, Object obj1, Object obj2){
		return String.format(strFormat, obj1, obj2);
	}

	final public static String Format(String strFormat, Object obj1, Object obj2, Object obj3){
		return String.format(strFormat, obj1, obj2, obj3);
	}

	final public static String Format(String strFormat, Object obj1, Object obj2, Object obj3, Object obj4){
		return String.format(strFormat, obj1, obj2, obj3, obj4);
	}

	final public static String Format(String strFormat, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5){
		return String.format(strFormat, obj1, obj2, obj3, obj4, obj5);
	}

	final public static String Format(String strFormat, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6){
		return String.format(strFormat, obj1, obj2, obj3, obj4, obj5, obj6);
	}

	final public static String Format(String strFormat, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7){
		return String.format(strFormat, obj1, obj2, obj3, obj4, obj5, obj6, obj7);
	}

	final public static String Format(String strFormat, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8){
		return String.format(strFormat, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8);
	}

	final public static String Format(String strFormat, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9){
		return String.format(strFormat, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9);
	}

	final public static String Format(String strFormat, Object obj1, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9, Object obj10){
		return String.format(strFormat, obj1, obj2, obj3, obj4, obj5, obj6, obj7, obj8, obj9, obj10);
	}

	final public static String Format(String strFormat, Object[] arrList){
		int nArrCount = arrList.length;

		switch (nArrCount){
		case 0:
			return strFormat;
		case 1:
			return String.format(strFormat, arrList[0]);
		case 2:
			return String.format(strFormat, arrList[0], arrList[1]);
		case 3:
			return String.format(strFormat, arrList[0], arrList[1], arrList[2]);
		case 4:
			return String.format(strFormat, arrList[0], arrList[1], arrList[2], arrList[3]);
		case 5:
			return String.format(strFormat, arrList[0], arrList[1], arrList[2], arrList[3], arrList[4]);
		case 6:
			return String.format(strFormat, arrList[0], arrList[1], arrList[2], arrList[3], arrList[4], arrList[5]);
		case 7:
			return String.format(strFormat, arrList[0], arrList[1], arrList[2], arrList[3], arrList[4], arrList[5], arrList[6]);
		case 8:
			return String.format(strFormat, arrList[0], arrList[1], arrList[2], arrList[3], arrList[4], arrList[5], arrList[6], arrList[7]);
		case 9:
			return String.format(strFormat, arrList[0], arrList[1], arrList[2], arrList[3], arrList[4], arrList[5], arrList[6], arrList[7], arrList[8]);
		case 10:
			return String.format(strFormat, arrList[0], arrList[1], arrList[2], arrList[3], arrList[4], arrList[5], arrList[6], arrList[7], arrList[8], arrList[9]);
		}
		return strFormat;
	}

	final public static int StringLength(String strValue){
		if (strValue == null){
			return 0;
		}
		return strValue.length();
	}

	final public static int Length(String strValue){
		if (strValue == null){
			return 0;
		}
		return strValue.length();
	}

	final public static boolean IsNullOrEmpty(String strValue){
		return (Length(strValue) == 0);
	}

	final public static int Compare(String strValue1, String strValue2, boolean bIgnoreCase){
		if (strValue1 == null || strValue2 == null){
			return -1;
		}
		if (bIgnoreCase){
			return strValue1.compareToIgnoreCase(strValue2);
		}
		else{
			return strValue1.compareTo(strValue2);
		}
	}

	final public static String[] Split(String strValue, char chSeperator){
		if (StringHelper.Length(strValue) == 0)
			return null;

		ArrayList<String> arrList = new ArrayList<String>();
		while (true){
			int nPos = strValue.indexOf(chSeperator);
			if (nPos != -1){
				String strPartA = strValue.substring(0, nPos);
				arrList.add(strPartA);
				strValue = strValue.substring(nPos + 1);
			}
			else{
				arrList.add(strValue);
				break;
			}
		}
		String[] strList = new String[arrList.size()];
		arrList.toArray(strList);
		return strList;

	}

	final public static String[] Split(String strValue, String chSeperator){
		if (StringHelper.Length(strValue) == 0)
			return null;

		ArrayList<String> arrList = new ArrayList<String>();
		while (true){
			int nPos = strValue.indexOf(chSeperator);
			if (nPos != -1){
				String strPartA = strValue.substring(0, nPos);
				arrList.add(strPartA);
				strValue = strValue.substring(nPos + chSeperator.length());
			}
			else{
				arrList.add(strValue);
				break;
			}
		}
		String[] strList = new String[arrList.size()];
		arrList.toArray(strList);
		return strList;

	}

	final public static String TrimLeft(String strValue){
		return TrimLeft(strValue, ' ');
	}

	final public static String TrimLeft(String strValue, char ch){
		while (strValue.length() > 0){
			if (strValue.charAt(0) == ch){
				strValue = strValue.substring(1);
			}
			else
				break;
		}
		return strValue;
	}

	final public static String[] SplitEx(String strValue){
		if (StringHelper.Length(strValue) == 0)
			return null;

		strValue = strValue.replace("|", ";");
		return strValue.split("[;]");
	}
	
	/**
	 * 将多个值字符串转为数据识别的字符串
	 * @param strValue
	 * @return
	 */
	final public static String ConvertStrToDBStr(String strValue){
		if(StringHelper.IsNullOrEmpty(strValue)){
			return null;
		}
		String [] strArray=strValue.split(",");
		String strContion="";
		for(String strTemp:strArray){
			strContion+="'"+strTemp+"',";
		}
		strContion=strContion.substring(0,strContion.length()-1);
		return strContion;
	}	
}
