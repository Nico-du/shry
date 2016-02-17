package net.chinanets.service.imp;

import java.util.List;

import net.chinanets.dao.DSmzqztDao;
import net.chinanets.service.DSmzqztService;

public class DSmzqztServiceImp implements DSmzqztService {

	private DSmzqztDao dsmzqztDao;
	/**
	 * 获得所有的生命周期状态
	 */
	public List getAllDSmzqzt() {
		String hql="From DSmzqzt";
		return dsmzqztDao.getAllObjectByHql(hql);
	}
	public void setDsmzqztDao(DSmzqztDao dsmzqztDao) {
		this.dsmzqztDao = dsmzqztDao;
	}
	public DSmzqztDao getDsmzqztDao() {
		return dsmzqztDao;
	}

}
