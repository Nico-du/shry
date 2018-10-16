package net.chinanets.utils.common;

import net.chinanets.dao.DaoResult;
import net.chinanets.utils.helper.StringHelper;
import net.sf.json.JSONObject;

/**
 * 调用返回结果
 * @author dzj
 *
 */
public class DoResult{
    /**
     * 返回代码
     */
    protected int nRetCode = Errors.OK;
    
    /**
     * 错误信息
     */
    protected String strErrorInfo = "";
    
    /**
     * 错误信息语言资源编号
     */
    protected String strErrorInfoRes = "";
    
    /**
     * 错误信息语言资源参数
     */
    protected String strErrorInfoResArg = "";
    
    /**
     * 用户对象
     */
    protected Object userObject = null;
    
    public DoResult(){
    }
    
    /**
     * 获取返回值
     * @return
     */
    public int getRetCode(){
        return nRetCode;
    }

    /**
     * 设置返回值
     * @param value
     */
    public void setRetCode(int value){
        nRetCode = value;
        if(nRetCode == -1){
        	nRetCode = Errors.INTERNALERROR;
        }
    }

    /**
     * 获取错误信息
     * @return
     */
    public String getErrorInfo(){
    	if(nRetCode == 0)
    		return "";
    	if(StringHelper.Length(strErrorInfo)== 0)
    		return Errors.GetErrorInfo(nRetCode);
        return strErrorInfo;
    }

    /**
     * 设置错误信息
     * @param value
     */
    public void setErrorInfo(String value){
        strErrorInfo = value;
    }
    
    /**
     * 判断结果是否为用户错误
     * @return
     */
    public boolean IsUserError(){
    	return  Errors.IsUserError(nRetCode);
    }
    
    
    /**
     * 判断结果是否为错误
     * @return
     */
    public boolean IsError(){
    	return  nRetCode!=Errors.OK;
    }
    
    /**
     * 判断结果是否为正确
     * @return
     */
    public boolean IsOk(){
    	return  nRetCode==Errors.OK;
    }
    
    
    /**
     * 填充结果
     * @param result
     */
    public void Fill(DoResult result){
    	result.setRetCode(nRetCode);
    	result.setErrorInfo(strErrorInfo);
    	result.setUserObject(userObject);
    }
    
    /**
     * 从DB结果获取
     * @param result
     */
    public void From(DoResult result){
    	this.setRetCode(result.getRetCode());
    	this.setErrorInfo(result.getErrorInfo());
    	this.setUserObject(result.getUserObject());
    	this.setErrorInfoRes(result.getErrorInfoRes());
    	this.setErrorInfoResArg(result.getErrorInfoResArg());
    }
    
    /**
     * 从数据库调用结果中填充
     * @param dbResult
     */
    public void From(DaoResult dbResult){
    	this.setRetCode(dbResult.getRetCode());
    	this.setErrorInfo(dbResult.getErrorInfo());
     }
    
    /**
     * 获取用户自定义对象
     * @return
     */
    public Object getUserObject(){
    	return this.userObject;
    }
    
    /**
     * 设置用户自定义对象
     * @param userObject
     */
    public void setUserObject(Object userObject){
    	this.userObject = userObject;
    }
    
    /**
	 * @return the strErrorInfoRes
	 */
	public String getErrorInfoRes(){
		return strErrorInfoRes;
	}

	/**
	 * @return the strErrorInfoResArg
	 */
	public String getErrorInfoResArg(){
		return strErrorInfoResArg;
	}

	/**
	 * @param strErrorInfoRes the strErrorInfoRes to set
	 */
	public void setErrorInfoRes(String strErrorInfoRes){
		this.strErrorInfoRes = strErrorInfoRes;
	}

	/**
	 * @param strErrorInfoResArg the strErrorInfoResArg to set
	 */
	public void setErrorInfoResArg(String strErrorInfoResArg){
		this.strErrorInfoResArg = strErrorInfoResArg;
	}

	/**
     * 重新格式化错误信息
     * @param strFormat
     */
    public void ReformatErrorInfo(String strFormat){
    	strErrorInfo = StringHelper.Format(strFormat,strErrorInfo);
    }
    
    /**
     * 重置返回结果，错误代码设置为 Errors.OK
     */
    public void Reset(){
    	nRetCode = Errors.OK;
    	strErrorInfo = "";
    	strErrorInfoRes = "";
    	strErrorInfoResArg = "";
    	userObject = null;
    }
    
    public static DoResult Create(int nCode){
    	DoResult callResult = new DoResult();
    	callResult.setRetCode(nCode);
    	return callResult;
    }
    
    public static DoResult Create(int nCode,String strErrorInfo){
    	DoResult callResult = new DoResult();
    	callResult.setRetCode(nCode);
    	callResult.setErrorInfo(strErrorInfo);
    	return callResult;
    }
    
    public static DoResult Create(int nCode,String strErrorInfo,Object objUserObject){
    	DoResult callResult = new DoResult();
    	callResult.setRetCode(nCode);
    	callResult.setErrorInfo(strErrorInfo);
    	callResult.setUserObject(objUserObject);
    	return callResult;
    }
    
    public static DoResult ToCallResult(DoResult callResult){
    	if(callResult!=null)
    		return callResult;
    	DoResult cr = new DoResult();
    	cr.setRetCode(Errors.INTERNALERROR);
    	cr.setErrorInfo("处理异常，返回空结果");
    	return cr;
    }
    
    /**
     * 根据操作结果返回JSON
     * @param callResult
     * @return
     */
    public String GetJSONByDoResult(){
    	JSONObject json=new JSONObject();
    	json.put("nRetCode", this.nRetCode);
    	json.put("strErrorInfo", this.strErrorInfo);
    	json.put("strErrorInfoRes", this.strErrorInfoRes);
    	json.put("strErrorInfoResArg", this.strErrorInfoResArg);
    	return json.toString();
    }
}
