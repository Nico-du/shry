package net.chinanets.service.imp;

import java.util.List;

import net.chinanets.dao.DYuDao;
import net.chinanets.service.DYuService;

public class DYuServiceImp implements DYuService {

	private DYuDao dyuDao;
	public List getAllDYu() {
		String hql="From DYu";
		return dyuDao.getAllObjectByHql(hql);
	}
	public void setDyuDao(DYuDao dyuDao) {
		this.dyuDao = dyuDao;
	}
	public DYuDao getDyuDao() {
		return dyuDao;
	}

}
