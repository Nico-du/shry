package net.chinanets.utils.common;

public class Enums {	
	
	//数据类型种类
	public static enum DATATYPE{
		STRING,STRINGBUFFER,STRINGBUILD,INT,INTEGER,DATE,FLOAT,LONG,DOUBLE,BOOLEAN,CLOB,UNKNOW
	}
	
	//条件表达式种类
	public static enum EXPTYPE{
		EQ,ABSEQ,GT,GTEQ,LT,LTEQ,NOTEQ,ISNULL,ISNOTNULL,LIKE,LEFTLIKE,RIGHTLIKE,IN,NOTIN
	}
}
