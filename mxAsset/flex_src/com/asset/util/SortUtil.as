package com.asset.util
{
	public class SortUtil
	{
		public static function sortByName(t1:Object, t2:Object)
		 {
		  if(t1.syr == null || t1.syr == '')return 1;
		  if(t2.syr == null || t2.syr == '')return 1;
		  if(t1.syr == t2.syr)
		  {
		   return sortById(t1,t2);
		  }
		  else if(t1.syr < t2.syr)
		  {
		   return -1;
		  }else{
		   return 1;
		  }
		 }
		 
		 public static function sortById(t1:Object,t2:Object)
		 {
		  if(t1.sbbh == null || t1.sbbh == '')return 1;
		  if(t2.sbbh == null || t2.sbbh == '')return 1;
		  if(t1.sbbh == t2.sbbh)
		  {
		   return 1;
		  }
		  else if(t1.sbbh < t2.sbbh)
		  {
		   return -1;
		  }else{
		   return 1;
		  }
 }



	}
}