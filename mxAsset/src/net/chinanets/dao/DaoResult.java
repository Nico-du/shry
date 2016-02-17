package net.chinanets.dao;

import java.util.Hashtable;

/**
 * 基础的数据结果集
 * @author RLiuyong
 *
 */
public class DaoResult{
    protected int curDBType = DaoType.Oracle9i;
    protected int nRetCode = 0;
    protected String strErrorInfo = "";
    protected Hashtable<Object,Object> returnValues = null;
    public DaoResult(){
    }

    public int getRetCode(){
        return nRetCode;
    }

    public void setRetCode(int value){
        nRetCode = value;
    }

    public String getErrorInfo(){
        return strErrorInfo;
    }

    public void setErrorInfo(String value){
        strErrorInfo = value;
    }

    /// <summary>
    /// 当前的数据库类型
    /// </summary>
    public int getDatabase(){
        return curDBType;
    }

    public void setDatabase(int value){

        curDBType = value;
    }

    /// <summary>
    /// 返回值列表
    /// </summary>
    public Hashtable<Object,Object> getOutValues(){
        if (returnValues == null){
            returnValues = new Hashtable<Object,Object>();
        }
        return returnValues;
    }
}
