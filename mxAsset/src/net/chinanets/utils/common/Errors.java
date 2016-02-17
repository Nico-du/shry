package net.chinanets.utils.common;

/**
 * 系统错误信息
 * @author RLiuyong
 *
 */
public class Errors{
	
	/**
	 * 成功，无错误
	 */
	public final static int OK = 0;
	
	/**
	 * 内部发生错误
	 */
	public final static int INTERNALERROR = 1; //内部发生错误
	
	/**
	 * 访问被拒绝
	 */
	public final static int ACCESSDENY = 2;   //访问被拒绝
	
	/**
	 * 无效的数据
	 */
	public final static int INVALIDDATA = 3;  //无效的数据
	
	/**
	 * 无效的数据键
	 */
	public final static int INVALIDDATAKEYS = 4; //无效的数据键
	
	/**
	 * 输入的信息有误
	 */
	public final static int INPUTERROR = 5; //输入的信息有误
	
	/**
	 * 重复的数据键值
	 */
	public final static int DUPLICATEKEY = 6; //重复的数据键值

	/**
	 * 重复的数据
	 */
	public final static int DUPLICATEDATA = 7; //重复的数据
	
	/**
	 * 删除限制
	 */
	public final static int DELETEREJECT = 8; //删除限制
	
	/**
	 * 逻辑处理错误
	 */
	public final static int LOGICERROR = 9; //逻辑处理错误
	
	/**
	 * 数据不匹配
	 */
	public final static int DATANOTMATCH = 10; //数据不匹配
	
	/**
	 * 没有实现指定功能
	 */
	public final static int NOTIMPL = 20; //没有实现指定功能
	
	/**
	 * 系统中不存在的用户CA
	 */
	public final static int NOTEXISTUSERCA = 30; //系统中不存在的用户CA
	
	/**
	 * 用户自定义错误编号开始
	 */
	public final static int USERERROR=1000; //用户错误从1000开始
	
	
	/**
	 * 判断返回值是否为用户自定义错误
	 * @param nErrorCode
	 * @return
	 */
	final public static boolean IsUserError(int nErrorCode){
		return nErrorCode>=USERERROR;
	}
	
	
	/**
	 * 获取错误的描述信息
	 * @param nErrorCode
	 * @return
	 */
	final public static String GetErrorInfo(int nErrorCode){
		if(IsUserError(nErrorCode))
			return "不明的用户自定义错误";
		
		switch(nErrorCode){
		case INTERNALERROR:
			return "系统内部发生错误";
		case ACCESSDENY:
			return "访问被拒绝，可能由于权限原因导致";
		case INVALIDDATA:
			return "数据不存在";
		case INVALIDDATAKEYS:
			return "数据的索引条件有误或不足";
		case INPUTERROR:
			return "数据的信息有误或不足";		
		case DUPLICATEKEY:
			return "重复的数据键";
		case DUPLICATEDATA:
			return "重复的数据";
		case DELETEREJECT:
			return "删除拒绝，可能由于权限原因导致";
		case LOGICERROR:
			return "逻辑处理错误";
		case DATANOTMATCH:
			return "数据不一致，可能后台数据已经被修改";
		case NOTIMPL:
			return "没有实现指定功能";
		case NOTEXISTUSERCA:
			return "系统中不存在的用户CA,请确认CA是否所属闵行法院,如确认请联系系统管理员进行数据同步.";
		default:
			return "不明错误";
		}
	}
	
	/**
	 * 判断是否为指定错误
	 * @param nErrorCode
	 * @param nSpecialError
	 * @return
	 */
	final public static boolean IsSpecialError(int nErrorCode,int nSpecialError){
		return (nErrorCode == nSpecialError) || (nErrorCode == nSpecialError+Errors.USERERROR);
	}
}
