package net.chinanets.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Hanzi2Pinyin {   
	
	public List<String> getPinyinList(List<String> list){
		List<String> pinyinList = new ArrayList<String>();
		for(Iterator<String> i=list.iterator(); i.hasNext();) {
			String str = (String)i.next();
			try {
				String pinyin = getPinYin(str);
				pinyinList.add(pinyin);
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		return pinyinList;
	}
	public String getPinYin(String zhongwen)   
            throws BadHanyuPinyinOutputFormatCombination {   
  
        StringBuffer zhongWenPinYin = new StringBuffer();   
        char[] chars = zhongwen.toCharArray();   
  
        for (int i = 0; i < chars.length; i++) {   
            String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(chars[i],   
                    getDefaultOutputFormat());   
            if (pinYin != null) {   
            	zhongWenPinYin.append(pinYin[0]);   //get the first pronunciation
            } else {   
            	zhongWenPinYin.append(chars[i]); //if not a CN char   
            }   
        }   
  
        return zhongWenPinYin.toString();   
    }   

    private HanyuPinyinOutputFormat getDefaultOutputFormat() {   
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();   
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// Сд   
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 
        format.setVCharType(HanyuPinyinVCharType.WITH_V);// 
  
        return format;   
    }   
  
}  
