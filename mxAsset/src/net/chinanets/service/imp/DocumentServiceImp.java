package net.chinanets.service.imp;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.chinanets.service.DocumentService;
import org.dom4j.Document;
import org.dom4j.Element;
@SuppressWarnings("unchecked")
public class DocumentServiceImp  extends CommonServiceImp implements DocumentService {
/*	
	 * 获取所有的项目和所属文档(non-Javadoc)
	 * @see net.chinanets.service.DocumentService#getAllDocument()
	 
	public String getDocumentTree(Long id){
		ProjectXm xm=(ProjectXm)super.getObjectById(new ProjectXm(), id);
		XmProcess obj= new XmProcess();
		obj.setParentID(id);
		List prossList=super.getObjectList(obj, " 1=1 order by bzdesc asc");
		int count=0;
		Document document=org.dom4j.DocumentHelper.createDocument();
		Element root=document.addElement("root");
		 root.addAttribute("name",xm.getXmmc());
		 Element all=root.addElement("all");
		 all.addAttribute("name", "全部文档");
		 all.addAttribute("id", xm.getId().toString());
		 all.addAttribute("pro", "1");
		  for(Iterator proTor=prossList.iterator();proTor.hasNext();){
				XmProcess p=(XmProcess) proTor.next();
				XmDocument xmdoc= new XmDocument();
				xmdoc.setDocParentid(p.getId()+"");
				List docList=super.getObjectList(xmdoc);
				
				Element lc=root.addElement("lc");
				lc.addAttribute("name", p.getBzmc()+"("+docList.size()+")");
				lc.addAttribute("id", p.getId().toString());
				lc.addAttribute("pro", "2");
				for(Iterator docTor=docList.iterator();docTor.hasNext();){
					XmDocument doc=(XmDocument)docTor.next();
					//doc.getDocMc();doc.getId();
					Element wd=lc.addElement("wd");
					wd.addAttribute("name", doc.getDocMc()+"."+doc.getDocType());
					wd.addAttribute("id", doc.getDocParentid().toString());
					wd.addAttribute("nr", doc.getDocNr());
					wd.addAttribute("icon", "pdf");
				}
		  }
		//  net.chinanets.utils.ChinaNetsUtil.writXml(document);
		return document.asXML();
	}
	*//**
	 * 根据文档获得项目结构树
	 *//*
	
	public List getDocTreeAndXm(Long id){
		List lt=new ArrayList();
		XmProcess p=(XmProcess)super.getObjectById(new XmProcess(), id);
		ProjectXm xm=(ProjectXm)super.getObjectById(new ProjectXm(),p.getParentID());
		String strXml= getDocumentTree(p.getParentID());
		Map mp = new HashMap();
		mp.put("xml", strXml);
		mp.put("obj", xm);
		lt.add(mp);
		return lt;
	}
	*//**
	 * 获得一个项目下所有的文档
	 *//*
	public List getOneProjectDoc(Long id){
		List docList = new ArrayList();
		XmProcess obj= new XmProcess();
		obj.setParentID(id);
		List prossList=super.getObjectList(obj, " 1=1 order by bzdesc asc");
		for(Iterator proTor=prossList.iterator();proTor.hasNext();){
			XmProcess p=(XmProcess) proTor.next();
			XmDocument xmdoc= new XmDocument();
			xmdoc.setDocParentid(p.getId()+"");
			List doc=super.getObjectList(xmdoc);
			docList.addAll(doc);
		}
		return docList;
	}
	*//**
	 * 删除文档
	 * 文档List
	 *//*
	public void deleteDoc(Long id ,String path) {
		try{
		XmDocument xoc = new XmDocument();
		xoc.setId(id);
		super.deleteObject(xoc);
		File file = new File("");
		file = new File(file.getAbsolutePath()+"/../webapps/mxAsset/uploadFiles/"+path);
		if(xoc.getDocNr() != null && xoc.getDocNr().length() > 0 && file.exists())
			file.delete();
		else{
			throw new Exception(file.getAbsolutePath()+",文件找不到!");
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	*//**
	 * 删除文档
	 * @param xmdocList
	 * 文档List
	 *//*
	public void deleteDocument(List<XmDocument> xmdocList){
		if(xmdocList.isEmpty()) return;
		try{
		 File file;
			for(XmDocument doc : xmdocList){
				file = new File(new File("").getAbsolutePath()+"/../webapps/mxAsset/uploadFiles/"+doc.getDocNr());
				if(doc.getDocNr() != null && doc.getDocNr().length() > 0 && file.exists() && file.isFile())
					file.delete();
				else{
					Exception ex = new Exception(file.getAbsolutePath()+",文件找不到!");
					ex.printStackTrace();
				}
				super.deleteObject(doc);
			}
			}catch(Exception e){
				e.printStackTrace();
			}
	}*/
	/**
	 * 判断文件是否存在
	 */
	public boolean isFileExist(String filePath){
		File file = new File("");
		file = new File(file.getAbsolutePath()+"/../webapps/mxAsset/uploadFiles/"+filePath);
		if(file.exists()) return true;
		return false;
	}

@Override
public String getDocumentTree(Long id) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public List getDocTreeAndXm(Long id) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public List getOneProjectDoc(Long id) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void deleteDoc(Long id, String path) {
	// TODO Auto-generated method stub
	
}

@Override
public void deleteDocument(List<Object> xmdocList) {
	// TODO Auto-generated method stub
	
}
	
	
	
	
}
