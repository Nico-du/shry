package net.chinanets.service.imp;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.chinanets.dao.DatumDao;
import net.chinanets.pojos.CnstDatumData;
import net.chinanets.pojos.CnstDirectoryData;
import net.chinanets.service.DatumService;
import net.chinanets.utils.CommonMethods;
import net.chinanets.utils.common.DoResult;
import net.chinanets.utils.common.Errors;
import net.chinanets.utils.helper.CommonHelper;
import net.chinanets.utils.helper.FileHelper;
import net.chinanets.utils.helper.JsonHelper;
import net.chinanets.utils.helper.StringHelper;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 相关资料服务接口实现
 * @author RLiuyong
 *
 */
public class DatumServiceImp extends CommonServiceImp implements DatumService{	
	//相关资料Dao层对象
	private DatumDao datumDao;
	
	public List getDownloadFileList(){
		List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
		Map<String,String> dataMap ;
		String fileName,fileLength;
		Date lastModify;
		String webrootPath = System.getProperty("user.dir"); 
		String pluginFilesPath = webrootPath + "/../webapps/mxAsset/plugin";
		File file = new File(pluginFilesPath);
		if(file.exists()){
			File []files = file.listFiles();
			for(File eachFile : files){
				dataMap= new HashMap<String, String>();
				fileName = eachFile.getName();
				fileLength = eachFile.length() >= (1024*1024) ? (eachFile.length()/(1024*1024))+"M" : (eachFile.length()/1024)+"k";
				lastModify = new Date(eachFile.lastModified());
				dataMap.put("fullname", fileName);
				dataMap.put("filename", fileName.substring(0,fileName.lastIndexOf(".")));
				dataMap.put("filetype", fileName.substring(fileName.lastIndexOf(".")+1));
				dataMap.put("filesize", fileLength);
				dataMap.put("utime", CommonMethods.commonDateFormate.format(lastModify));
				resultList.add(dataMap);
			}
		}
		/*dataMap= new HashMap<String, String>();
		dataMap.put("fliename", "测试文件");
		dataMap.put("filetype", "txt");
		dataMap.put("filesize", "25M");
		dataMap.put("utime", "2015-01-25");
		resultList.add(dataMap);*/
		
		return resultList;
	}
	
	
	
	
	
	//查找所有目录
	public String GetDatumDirectoryXML() {
		List<CnstDirectoryData> directoryList= datumDao.GetDatumDirectoryList();
		if(directoryList==null || directoryList.size()<1){
			return null;
		}
		HashMap<String,Element> directoryTreeMap=new HashMap<String, Element>();
		Document document=DocumentHelper.createDocument();
		Element tempElement;
		CnstDirectoryData directory;
		//加载目录树元素
		for(int i=0;i<directoryList.size();i++){
			directory=directoryList.get(i);
			String strDirectoryID=directory.getDirectoryid();
			String strMenueName="MENUE"+i;
			tempElement=DocumentHelper.createElement(strMenueName);
			tempElement.addAttribute("id", strDirectoryID);
			tempElement.addAttribute("label", directory.getDirectoryname());
			tempElement.addAttribute("path", directory.getDirectorypath());
			if(!directoryTreeMap.containsKey(strDirectoryID)){
				directoryTreeMap.put(strDirectoryID, tempElement);
			}
			if(StringHelper.IsNullOrEmpty(directory.getLastdirectoryid())){
				document.add(tempElement);
			}
		}
		//整理上下级目录顺序
		for(CnstDirectoryData tempDirectory:directoryList){
			String strDirectoryID=tempDirectory.getDirectoryid();
			String strLastDidrectoryID=tempDirectory.getLastdirectoryid();
			if(!StringHelper.IsNullOrEmpty(strLastDidrectoryID)){
				directoryTreeMap.get(strLastDidrectoryID).add(directoryTreeMap.get(strDirectoryID));
			}
		}
		return document.asXML();
	}

	//查找目录下的文件
	public List<CnstDatumData> GetDatumListByDirectoryId(String directoryId) {
		return datumDao.GetDatumListByDirectoryId(directoryId);
	}
	
	//根据文件ID查找文件
	public CnstDatumData GetDatumByDatumid(String datumId){
		return datumDao.GetDatumById(datumId);
	}
	
	//分页展示数据
	public String PagingRmtData(String tempObjBeanName,int tempPageSize,int tempPageCurrent,String strcondition){
		return datumDao.GetDatumListByPaging(tempPageSize, tempPageCurrent, strcondition);
	}
	
	//添加或更新目录信息
	@SuppressWarnings("unchecked")
	public String AddOrUpdateDatumDirectory(String directorJSON) {
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		List<CnstDirectoryData> directoryList=(List<CnstDirectoryData>)JsonHelper.GetListBeanByJsonString(directorJSON,CnstDirectoryData.class);
		if(directoryList==null || directoryList.size()<1){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		CnstDirectoryData directory=directoryList.get(0);
		String directoryId=directory.getDirectoryid();
		if(StringHelper.IsNullOrEmpty(directoryId)){
			String strGUID=CommonHelper.GenGuidEx();
			//先处理文件目录
			String strDiretoryPath=directory.getDirectorypath();
			if(StringHelper.IsNullOrEmpty(strDiretoryPath)){
				strDiretoryPath=strGUID;
			}else{
				strDiretoryPath=strDiretoryPath+"\\"+strGUID;
			}
			String strGlobalPath=FileHelper.GetCommonFolderFile();
			if(StringHelper.IsNullOrEmpty(strGlobalPath)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("未获取到全局路径");
				return doResult.GetJSONByDoResult();
			}
			String strRealDirectoryPath=strGlobalPath+strDiretoryPath;
			File directoryFile=new File(strRealDirectoryPath);
			if(!directoryFile.exists()){
				if(!directoryFile.mkdir()){
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("创建目录失败");
					return doResult.GetJSONByDoResult();
				}
			}
			directory.setDirectoryid(strGUID);
			directory.setDirectorypath(strDiretoryPath);
			directory.setCtime(new Date());
			if(!this.datumDao.SaveOrUpdateDatumDirectory(directory, true)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("添加目录信息失败");
				return doResult.GetJSONByDoResult();
			}
		}else{
			CnstDirectoryData tempDirectory=this.datumDao.GetDirectoryById(directoryId);
			tempDirectory.setDirectoryname(directory.getDirectoryname());
			tempDirectory.setUtime(directory.getUtime());
			tempDirectory.setUuser(directory.getUuser());
			if(!this.datumDao.SaveOrUpdateDatumDirectory(tempDirectory, false)){
				doResult.setRetCode(Errors.INTERNALERROR);
				doResult.setErrorInfo("更改目录信息失败");
				return doResult.GetJSONByDoResult();
			}
		}
		return doResult.GetJSONByDoResult();
	}

	//添加或更改文件
	@SuppressWarnings("unchecked")
	public String AddOrUpdateDatum(String datumJSON){
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		List<CnstDatumData> datumList=(List<CnstDatumData>)JsonHelper.GetListBeanByJsonString(datumJSON, CnstDatumData.class);
		if(datumList==null || datumList.size()<1){
			doResult.setRetCode(Errors.INVALIDDATA);
			doResult.setErrorInfo("请求数据无效");
			return doResult.GetJSONByDoResult();
		}
		for(CnstDatumData datum:datumList){
			String strid=datum.getDatumid();
			if(StringHelper.IsNullOrEmpty(strid)){
				String strGUID=CommonHelper.GenGuidEx();
				datum.setDatumid(strGUID);
				if(!this.datumDao.SaveOrUpdateDatum(datum, true)){
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("创建文件失败");
					return doResult.GetJSONByDoResult();
				}
			}else{
				if(!this.datumDao.SaveOrUpdateDatum(datum, true)){
					doResult.setRetCode(Errors.INTERNALERROR);
					doResult.setErrorInfo("更改文件信息失败");
					return doResult.GetJSONByDoResult();
				}
			}
		}
		return doResult.GetJSONByDoResult();
	}
	
	//删除目录
	public String DeleteDatumDirectoryById(String directoryId) {
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		CnstDirectoryData directory=this.datumDao.GetDirectoryById(directoryId);
		directory.setUsable(0);
		directory.setDirectoryname(directory.getDirectoryname()+directoryId);
		if(!this.datumDao.SaveOrUpdateDatumDirectory(directory, false)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除目录失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}

	//删除文件
	public String DeleteDatumById(String datumId) {
		DoResult doResult=new DoResult();
		doResult.setRetCode(Errors.OK);
		String strContion=StringHelper.ConvertStrToDBStr(datumId);
		if(StringHelper.IsNullOrEmpty(strContion)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("未获取到要删除的值");
			return doResult.GetJSONByDoResult();
		}
		String strHQL=StringHelper.Format("upate CnstDatumData as datum set datum.usable=0 where datum.datum.id in (%1$s)",datumId);
		if(!this.datumDao.UpdateDataByHQL(strHQL)){
			doResult.setRetCode(Errors.INTERNALERROR);
			doResult.setErrorInfo("删除文件失败");
			return doResult.GetJSONByDoResult();
		}
		return doResult.GetJSONByDoResult();
	}
	
	public DatumDao getDatumDao() {
		return datumDao;
	}

	public void setDatumDao(DatumDao datumDao) {
		this.datumDao = datumDao;
	}
}
