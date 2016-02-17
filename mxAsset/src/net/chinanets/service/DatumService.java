package net.chinanets.service;

import java.util.List;

import net.chinanets.pojos.CnstDatumData;

/**
 * 相关资料服务接口
 * @author RLiuyong
 *
 */
public interface DatumService extends CommonService {
	
	//得到WEBROOT/plugin可以下载的文件
	public List getDownloadFileList();	
	
	//得到所有目录信息
	public String GetDatumDirectoryXML();
	
	//根据目录得到文件信息
	public List<CnstDatumData> GetDatumListByDirectoryId(String directoryId);
	
	//根据文件ID查找文件
	public CnstDatumData GetDatumByDatumid(String datumId);
	
	//分页展示数据
	public String PagingRmtData(String tempObjBeanName,int tempPageSize,int tempPageCurrent,String strcondition);
	
	//添加或更改目录
	public String AddOrUpdateDatumDirectory(String directorJSON);
	
	//添加或更改文件
	public String AddOrUpdateDatum(String datumJSON);
	
	//删除某个目录
	public String DeleteDatumDirectoryById(String directoryId);
	
	//删除文件
	public String DeleteDatumById(String datumId);
}
