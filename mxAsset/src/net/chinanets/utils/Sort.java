package net.chinanets.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sort {

	public static List<String> getSortStirng(List<String> list) {
		Hanzi2Pinyin hanziPinyin = new Hanzi2Pinyin();
		
		List<String> numList = new ArrayList<String>();
		List<String> letterList = new ArrayList<String>();
		List<String> hanziList = new  ArrayList<String>();
		List<String> eptList = new  ArrayList<String>();// null List
		boolean isAllNum,isAllLetter,isMix;
		
		for(int i=0;i<list.size();i++){
			isAllNum = isAllLetter = isMix = true;
			String stri = list.get(i);
			if(stri!=null && stri.length()>0){
				char []chars = stri.toCharArray();
			for(int j=0;j<chars.length;j++ ){
				char cr = chars[j];
				if(!Character.isLetterOrDigit(cr) || !(cr+"").matches("^[0-9a-zA-Z]")){
					isAllLetter = isAllNum = false;
				isMix=true; break;}
				if(isAllLetter){ if(Character.isDigit(cr)){ isAllLetter = false;} }
				if(isAllNum){ if(Character.isLetter(cr)){ isAllNum = false;}}
				if(j == chars.length-1){if(isAllLetter || isAllNum)isMix=false;}
			}
			if(isAllNum){numList.add(stri);}
			if(isAllLetter){letterList.add(stri.toUpperCase());}
			if(isMix || (!isAllNum && !isAllLetter)){hanziList.add(stri);}
			}else{ eptList.add(stri);}
		}
		
		//step one : mark the list
	    final String ct = ";*&#*;";  //specify the separator
		List<String> cpList = new ArrayList<String>(); 
		for(int i=0;i<hanziList.size();i++){
			cpList.add(hanziList.get(i).toUpperCase()+ ct + i);
		}
		//step two : sort the marked list
		cpList =hanziPinyin.getPinyinList(cpList);
		 Collections.sort(cpList);
		//step three :build the result list according to the sorted and marked list
		List<String> rstHanziList = new ArrayList<String>(); 
		for(String str : cpList){
			int idx = Integer.parseInt(str.substring(str.indexOf(ct)+ct.length()));
			rstHanziList.add(hanziList.get(idx));
		}
		Collections.sort(numList);
		Collections.sort(letterList);
		
		List<String> rstList = new ArrayList<String>(); 
		rstList.addAll(numList);
		rstList.addAll(letterList);
		rstList.addAll(rstHanziList);
		rstList.addAll(eptList);
		
		return rstList;
	}
	
	public static List<Object> getSortPp(List<Object> list) {
		/*
		if(list.isEmpty()){return list;}
		//step one : mark the list
		final String ct = ";*&#&0#&*;";  //specify the separator
		List<String> cpList = new ArrayList<String>(); 
		for(int i=0;i<list.size();i++){
			cpList.add(list.get(i).getMc()+ ct + i);
		}
		//step two : sort the marked list
		cpList = getSortStirng(cpList);
		//step three :build the result list according to the sorted and marked list
		List<DJzpp> rstList = new ArrayList<DJzpp>(); 
		for(String str : cpList){
			int idx = Integer.parseInt(str.substring(str.indexOf(ct)+ct.length()));
			rstList.add(list.get(idx));
		}*/
		return list;
	}
	/*
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("000090"); 
		list.add("100001"); 
		list.add("a10"); 
		list.add("a11"); 
		list.add("a12"); 
		list.add("A00001"); 
		list.add("A00002"); 
		list.add("A00003"); 
		list.add("A00004"); 
		list.add("A00005"); 
		list.add("A00006"); 
		list.add("B10"); 
		list.add("B11"); 
		list.add("B001"); 
		list.add("B002"); 
		list.add("B003"); 
		list.add("B004"); 
		list.add("B005"); 
		list.add("B006"); 

		list.add("2");
		list.add("1");
		list.add("8");
		list.add("5");
		list.add("abb");
		list.add("h");
		list.add("v");
		list.add("baa");
		list.add("Aaa");
		list.add("Daa");
		list.add("B");
		list.add("1A");
		list.add("7W");
		list.add("2Z");
		list.add("1飞");
		list.add("6飞");
		list.add("2飞");
		list.add("a啊");
		list.add("d啊");
		list.add("b啊");
		list.add("我");
		list.add("好");
		list.add("暗示");
		list.add("飞");
		list.add("访问");
		list.add("大方");
		list.add("七位");
		list.add("啊");
		list.add("的");
		list.add("狗头");
		List<String> list1 = getSortStirng(list);
		for(String sr : list1){
			System.out.println(sr);
		}
		List<DJzpp> pplist = new ArrayList<DJzpp>();
		DJzpp pp;
		for(int i=0;i<100;i++){
			pp = new DJzpp();
			pp.setMc((int)(Math.random()*100000)+"");
			pplist.add(pp);
		}
		pplist = getSortPp(pplist);
		for(DJzpp pp1:pplist)
			System.out.println(pp1.getMc());
		
		
	}
*/
}
