package net.chinanets.dao;

import java.util.List;

public interface IQueryDAO {
	List getObjectByExample(Object obj,int maxResults, int firstResult,String condition);
	int getCountByExample(Object obj);
	List getObjectByExample(Object obj, int pageSize, int pageNumber);
	List getObjectByExample(Object obj);
	List getObjectByExample(Object obj,String condition);
	List executeJdbcSql(String jdbcSql, int columns);
	public int getCountByExample(final Object obj, final String condition);
	List getDistinctDept();
	List executerHsql(String hSql,int columns);
}
