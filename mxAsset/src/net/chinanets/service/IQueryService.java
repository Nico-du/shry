package net.chinanets.service;

import java.util.List;

public interface IQueryService {
	public int getCountByObject(Object obj);
	public int getCountByObject(Object obj, String condition);
	public List getObjectList(Object obj,String condition);
	public List getObjectList(Object obj,int pageSize, int pageNumber);
	public List getObjectList(Object obj,int pageSize, int pageNumber,String condition);
	public List executeJdbcSql(String jdbcSql, int columns);
	public String getBmzc(String type,String name);
	public String getBmjg(String deptName);
	public List executerHsql(String hSql,int columns);
}
