package net.chinanets.dao;

/**
 * 框架支持的数据库
 * @author RLiuyong
 *
 */
public final class DaoType{
   final  public static int Unknown = 0; //未明
   final  public static int Oracle9i = 1; //Oracle数据库
   final  public static int SqlServer2K = 2; //SqlServer数据库
   final  public static int MySQL5 = 3; //MySQL 数据库
   final  public static int DB2 = 4; //DB2 数据库
   final  public static int Informix = 5; //Informix ids v11.5
   final  public static int SYBASE = 6; //SYBASE数据库
   final  public static int POSTGRESQL = 7; //POSTGRESQL数据库
  
   final public static String TAG_UNKNOWN = "UNKNOWN";
   final public static String TAG_ORACLE = "ORACLE";
   final public static String TAG_MSSQL = "MSSQL";
   final public static String TAG_MYSQL = "MYSQL";
   final public static String TAG_DB2 = "DB2";
   final public static String TAG_SYBASE = "SYBASE";
   final public static String TAG_INFORMIX = "INFORMIX";
   final public static String TAG_POSTGRESQL = "POSTGRESQL";
}
