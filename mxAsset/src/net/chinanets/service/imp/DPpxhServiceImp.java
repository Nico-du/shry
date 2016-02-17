package net.chinanets.service.imp;

import java.util.List;

import net.chinanets.dao.DPpxhDao;
import net.chinanets.service.DPpxhService;

public class DPpxhServiceImp implements DPpxhService {
    
	private DPpxhDao dppxhDao;
	public DPpxhDao getDppxhDao() {
		return dppxhDao;
	}
	public void setDppxhDao(DPpxhDao dppxhDao) {
		this.dppxhDao = dppxhDao;
	}
	/**
	 *  获得所有的品牌信息
	 */
	public List getAllDPpxh() {
		String hql="From DPpxh";
		return dppxhDao.getAllObjectByHql(hql);
		 
	}

}
