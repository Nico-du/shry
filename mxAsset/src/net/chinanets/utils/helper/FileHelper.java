package net.chinanets.utils.helper;

import java.io.File;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 文件辅助类
 * @author RLiuyong
 *
 */
public class FileHelper{
	private static Log log=LogFactory.getLog(FileHelper.class);
	private static String CNSCOMXMLFILE="xml\\common\\common.xml";
	private static String CNSWEBFILEELEMENT="CNSCOMMON";
	private static String CNSCOMMONFILEELEMENT="COMMONFILE";
	/**
	 * 删除目录
	 * @param strFolderPath
	 * @param bContentOnly 只删除内容，保留当前目录
	 * @throws IOException
	 */
	public static final void RemoveFolder(String strFolderPath, boolean bContentOnly) throws IOException{
		File f = new File(strFolderPath);// 定义文件路径
		if (f.exists() && f.isDirectory()){
			// 判断是文件还是目录
			if (f.listFiles().length == 0){// 若目录下没有文件则直接删除
				if(!bContentOnly)
					f.delete();
			}
			else{
				//若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++){
					if (delFile[j].isDirectory()){
						RemoveFolder(delFile[j].getAbsolutePath(), false);//递归调用del方法并取得子目录路径
					}
					delFile[j].delete();//删除文件
				}
			}
		}
	}
	
	/**
	 * 得到公用的文件目录路径
	 */
	public static final String GetCommonFolderFile(){
		SAXReader saxReader=new SAXReader();
		Document document;
		try{
			String strRealPath=GetProjectPath()+CNSCOMXMLFILE;
			document=saxReader.read(new File(strRealPath));
			Element cnsFileElement=document.getRootElement().element(CNSWEBFILEELEMENT).element(CNSCOMMONFILEELEMENT);
			String strComFilePath=cnsFileElement.attributeValue("FILEPATH");
			File comFileFloder=new File(strComFilePath);
			if(!comFileFloder.exists()){
				comFileFloder.mkdir();
			}
			return strComFilePath;
		}catch(Exception ex){
			log.error(ex.getMessage());
			return null;
		}
	}
	
	/**
	 * 得到当前项目路径
	 * @return
	 */
	public static final String GetProjectPath(){
		try{
			String strFilePath=FileHelper.class.getResource("/").toURI().getPath().toString();
			if(strFilePath.startsWith("\\")){
				strFilePath=strFilePath.substring(1,strFilePath.length());
			}
			int ipoint=strFilePath.indexOf("WEB-INF");
			if(ipoint!=-1){
				strFilePath=strFilePath.substring(0,ipoint);
			}
			return strFilePath;
		}catch(Exception e){
			log.error(e.getMessage());
			return null;
		}
		
	}
}
