package net.chinanets.service.imp;

import java.util.List;

import net.chinanets.dao.DWlDao;
import net.chinanets.service.DWlService;

public class DWlServiceImp implements DWlService {

	private DWlDao dwlDao;
	public DWlDao getDwlDao() {
		return dwlDao;
	}
	public void setDwlDao(DWlDao dwlDao) {
		this.dwlDao = dwlDao;
	}
	/**
	 * 获得所有的网络
	 */
	public List getAllDWl() {
		String hql="From DWl";
		return dwlDao.getAllObjectByHql(hql);
	}

}
