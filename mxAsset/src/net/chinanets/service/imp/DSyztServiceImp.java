package net.chinanets.service.imp;

import java.util.List;

import net.chinanets.dao.DSyztDao;
import net.chinanets.service.DSyztService;

public class DSyztServiceImp implements DSyztService {

	private DSyztDao dsyztDao;
	public DSyztDao getDsyztDao() {
		return dsyztDao;
	}
	public void setDsyztDao(DSyztDao dsyztDao) {
		this.dsyztDao = dsyztDao;
	}
	/**
	 * 获得所有的使用状态
	 */
	public List getAllDSyzt() {
		String hql="From DSyzt";
		return dsyztDao.getAllObjectByHql(hql);
	}

}
