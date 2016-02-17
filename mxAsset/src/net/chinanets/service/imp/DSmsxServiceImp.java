package net.chinanets.service.imp;

import java.util.List;

import net.chinanets.dao.DSmsxDao;
import net.chinanets.service.DSmsxService;

public class DSmsxServiceImp implements DSmsxService {

	private DSmsxDao dsmsxDao;
	public DSmsxDao getDsmsxDao() {
		return dsmsxDao;
	}
	public void setDsmsxDao(DSmsxDao dsmsxDao) {
		this.dsmsxDao = dsmsxDao;
	}
	/**
	 * 获得所有的安全保密属性
	 */
	public List getAllDSmsx() {
		String hql="From DSmsx ";
		return dsmsxDao.getAllObjectByHql(hql);
	}

}
