package net.chinanets.service.imp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import net.chinanets.dao.CommonDao;
import net.chinanets.pojos.Dept;
import net.chinanets.service.CommonService;
import net.chinanets.u.Sort;
import net.chinanets.vo.DeptVo;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import edu.emory.mathcs.backport.java.util.TreeSet;
@SuppressWarnings("unchecked")
public class CommonServiceImp implements CommonService {
	
	//通用DAO
	public CommonDao commonDao;
	
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}
	
	//排序方法
	private Sort sort;
	
	public void setSort(Sort sort) {
		this.sort = sort;
	}

	/**
	 * 保存对象
	 * @param obj
	 * @return
	 */
	public Serializable saveObject(Object obj) {
		return  commonDao.save(obj);
	}
	
	/***
	 * 删除对象
	 * @param obj
	 */
	public void deleteObject(Object obj) {
		commonDao.delete(obj);
	}
	
	/**
	 * 根据ID查询对象
	 * @param obj
	 * @param id
	 * @return
	 */
	public Object getObjectById(Object c,Long id) {
		
		return commonDao.getObjectById(c.getClass(), id);
	}
	 
	public Object getObjectBySId(Object c,Serializable id) {
		
		return commonDao.getObjectById(c.getClass(), id);
	}
	
	/**
	 * 修改对象
	 * @param obj
	 */
	public void updateObject(Object obj) {
	try{
		commonDao.update(obj);
	}catch(Exception e){
		e.printStackTrace();
	}
	}
	
	
	public void mergeObject(Object obj){
		commonDao.merge(obj);
	}
	
	public void mergeObjectList(List list){
		if(list != null && list.size() > 0) {
			for (Object object : list) {
				commonDao.merge(object);
			}
		}
	}
	/**
	 * 根据对象属性值查询对象
	 * @param obj
	 * @return
	 */
	public List getObjectList(Object obj) {
		return commonDao.getObjectByExample(obj);
	}
	
	public List getObjectList(Object obj, String condition) {
		List list = commonDao.getObjectByExample(obj, condition);
		return list;
	}
	/**
	 * 根据HQL查询所有对象
	 * @param hql
	 * @return
	 */
	public List getAllObjectByHql(String hql){
		return commonDao.getAllObjectByHql(hql);
	}
	
	/**
	 * 根据对象属性值查询对象,并且分页
	 * @param obj
	 * @param pageSize
	 * @param firstResult
	 * @return
	 */
	public List getObjectList(Object obj, int pageSize, int pageNumber) {
		return commonDao.getObjectByExample(obj, pageSize, pageSize*(pageNumber-1));
	}
	
	/**
	 * 根据对象属性值查询对象,并且分页
	 * @param obj 分页对象
	 * @param pageSize	页大小
	 * @param pageNumber	第几页
	 * @param condition	sql语句条件
	 * @return
	 */
	public List getObjectList(Object obj, int pageSize, int pageNumber,
			String condition) {
		return commonDao.getObjectByExample(obj, pageSize, pageSize*(pageNumber-1),condition);
	}
	
	/**
	 * 根据对象属性查询总记录条数
	 * @param obj
	 * @return
	 */
	public int getCountByObject(Object obj) {
		return commonDao.getCountByExample(obj);
	}
	
	/***
	 * 根据对象属性查询、条件总记录条数
	 * @param obj
	 * @param condition
	 * @return
	 */
	public int getCountByObject(Object obj, String condition) {
		return commonDao.getCountByExample(obj, condition);
	}

	
	/**
	 * 根据对象属性值查询对象,并且分页
	 * @param obj 分页对象
	 * @param pageSize	页大小
	 * @param pageNumber	第几页
	 * @param 
	 * @return 一次性返回结果和行数
	 */
	@SuppressWarnings("unchecked")
	public List getPageList(Object obj, int pageSize, int pageNumber,String condition) {
		List result= new ArrayList();
		
		List item=getObjectList(obj,pageSize,pageNumber, condition );
		
		int count= getCountByObject(obj,condition);
		HashMap mp = new HashMap();
		mp.put("item", item);
		mp.put("count", count);
		result.add(mp);
		return result;
		
	}
	/**
	 * 自定义hql语句
	 * @param hql
	 * @return 返回结果行的数量
	 */
	public int getObjectItemTotalCount(String hql) {
		return commonDao.getObjectItemTotalCount(hql);
	}
	/**
	 * 删除对象数组
	 * @param List
	 * 
	 */
	public void deleteObjectItem(List lt) {
	  if(lt.size()>0)
	  {
		  for(int i=0;i<lt.size();i++)
		  {
			  commonDao.delete(lt.get(i));
		  }
	  }
	}

	public void deleteOjbectItem(String type, Long[] id) {
		Class cls;
		try {
			cls = Class.forName(type);
			Object obj = cls.newInstance();
			for (int i = 0; i < id.length; i++) {
				Long long1 = id[i];
				deleteObject(getObjectById(obj, id[i]));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	public List getAllBm()
	{
		List lt=new ArrayList();
		List listDept = this.getObjectList(new Dept());
		for (Iterator iterator = listDept.iterator(); iterator.hasNext();) {
			Dept deptIter = (Dept) iterator.next();
			DeptVo vo = new DeptVo();
			vo.setMc(deptIter.getMc());
			vo.setId(deptIter.getId());
			lt.add(vo);
		}
		return lt;
	}
	/**
	 * 保存对象数组
	 * @param List
	 * 
	 */
	public void saveObjectItem(List lt){
		  if(lt.size()>0)
		  {
			  for(int i=0;i<lt.size();i++)
			  {
				  commonDao.saveOrUpdateObject(lt.get(i));
			  }
		  }
	}
	/**
	 * 根据sql语句获得数组
	 * @param sql
	 * 
	 */
	public List getObjectBySql(String sql){
		return commonDao.getObjectBySql(sql);
	}
	
	public void updateObjectBySql(String sql){
		commonDao.updateObjectBySql(sql);
	}
	
	public void updateObjectItem(List lt){
		  if(lt.size()>0)
		  {
			  for(int i=0;i<lt.size();i++)
			  {
				  this.mergeObject(lt.get(i));
			  }
		  }
	}
	
	/**
	 * 保存或更新数据对象
	 */
	public void saveOrUpdateObject(Object obj){
		commonDao.saveOrUpdateObject(obj);
	}
	/**
	 * 根据sql获得唯一的数字
	 */
	public int getOneBysql(String sql){
		return commonDao.getSequence(sql);
	}

	/**
	 * 删除参数集合中的所有对象
	 */
	public void removeObjectList(List list) {
		// TODO Auto-generated method stub
		
	}
	public void deleteByHql(String hql) {
		commonDao.deleteByHql(hql);
	}
	//读txt
	public  List readInputByRow(String path){
		   List list=new ArrayList();
	        File file=new File(path);
	           try {     
	               FileInputStream fis = new FileInputStream(file);     
	               InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
	               BufferedReader reader = new BufferedReader(isr);     
	               String tempstring="";
	        while((tempstring=reader.readLine())!=null)
	        {
	        	list.add(tempstring);
	         //System.out.println("debug in ReadWriteOEMInfo.java on line 99 tempstring : "+tempstring);
	        }
	        	   reader.close();
	               isr.close();
	               fis.close();
	                  return list;     
	           } catch (IOException e) {     
	               e.printStackTrace();     
	               return null;     
	           }  
	}
	//读维护计划
	public  String readData(String key) {
		// TODO Auto-generated method stub
		key = "1";
		Properties props = new Properties();   
        try {   
            InputStream in = new BufferedInputStream(new FileInputStream(   
            		"D:/tomcat6/webapps/asset/data.properties"));   
	        props.load(in);   
	        in.close();   
            String value = props.getProperty(key);   
            return value;   
        } catch (Exception e) {   
            e.printStackTrace();   
            return null;   
        }   
	}
	
//	private static ArrayList filelist = new ArrayList(); 

	/**
	 * 获得所有txt文件名
	 * @param strPath
	 */

		List<String> filelist = new java.util.ArrayList<String>();
		 
		 public List<String> refreshFileList(String strPath) throws Exception {
			 File f = new File(strPath);
			 File[] sub = f.listFiles();
			 filelist.clear();
				for(int i=0; i<sub.length; i++){
					if(sub[i].isDirectory()){continue;}
					System.out.println(sub[i].getName());
					String str = sub[i].getName();
					String[] strin = str.split("\\.");
					filelist.add(strin[0]);
				}
		  return filelist;
		  }


//		String strFileName ="" ;
//		
		 
		  private Document document = DocumentHelper.createDocument();
		  private Element root = document.addElement("Root");
		  private  int j=0;
		 // 递归方法   读取文件夹列表
//		 public  String tree(String path, int level) {   
//		// 创建xml对象"D:\\tomcat6\\webapps\\asset\\pandian"
//		 File f = new File(path);
//	     String preStr = "";   
//	     for (int i = 0; i < level; i++) {   
//	         preStr += "-";   
//	     }   
//	     File[] childs = f.listFiles();   
//	     System.out.println(childs.length+"");
//	     for (int i = 0; i < childs.length; i++) {   
//	         System.out.println(preStr + childs[i].getName());   
//	         if (childs[i].isDirectory()) {  
//	        	 	if("pandian".equals(childs[i].getName())){
//	        	 		continue;
//	        	 	}
//	        	 	Element firstElement = root.addElement("firstMenu");
//					firstElement.addAttribute("id", i+"");
//					firstElement.addAttribute("label",childs[i].getName()); 
//					firstElement.addAttribute("icon", "file.jpg");
//					j=i;
//					tree(childs[j].toString(), level + 1);   
//	             
//	         }   
//	         else{
//	        	String xPath = "/Root/firstMenu[@id='" + j	+ "']";
//				Element firstElement = (Element) document.selectNodes(xPath)
//						.get(0);
//				Element secondElement = firstElement.addElement("secondMenu");
//				secondElement.addAttribute("id", i+"");
//				String[] str = childs[i].getName().split(".txt");
//				secondElement.addAttribute("label",str[0]);
//				secondElement.addAttribute("icon", "pcj.jpg");
//	         }
//	     }
//	     writXml(document);
//	     return document.asXML();
//	 }   
		  //从数据库生成的xml不存在的类型不显示出来
		  public  String tree(Object obj, int level) {   
			 ArrayList<String> str = new  ArrayList<String>();
			 List<String> list =  getObjectBySql("select txtname from xjjl t group by txtname order by txtname");
			 if(list.size()>0){
				 for (int i = 0; i < list.size(); i++) { 
					Element firstElement = root.addElement("firstMenu");
					firstElement.addAttribute("id", i+"");
					firstElement.addAttribute("label",list.get(i)); 
					firstElement.addAttribute("icon", "file.jpg");
					 List<String> l =  getObjectBySql("select sbbh from xjjl where txtname='"+list.get(i)+"'");
					 if(l.size()>0){
						 for(int j=0;j<l.size();j++){
							 String bh=l.get(j).substring(0, 2).toLowerCase();
							 if(bh.equals("pc")|| bh.equals("la")){
								 str.add("计算机终端");
							 }
							 if(bh.equals("gz")){
								 str.add("介质");
							 }
							 if(bh.equals("pr")|| bh.equals("sc")){
								 str.add("外设设备");
							 }
							 if(bh.equals("se")){
								 str.add("主机");
							 }
						 }
						 Set set = new TreeSet();
							for (int k = 0; k < str.size(); k++) {
								set.add(str.get(k));
							}
							String[] temp = new String[]{};
							temp = (String[]) set.toArray(new String[0]);
							set.clear();
							str.clear();
						for(j=0;j<temp.length;j++){
	//						String xPath = "/Root/firstMenu[@id='" + i	+ "']";
	//						 firstElement = (Element) document.selectNodes(xPath)
	//						.get(0);
							Element secondElement = firstElement.addElement("secondMenu");
							secondElement.addAttribute("id", i+"");
							secondElement.addAttribute("label",temp[j]);
							secondElement.addAttribute("icon", "pcj.jpg");
						}
					}
				 }
			 }
			 	
			 writXml(document);
		     return document.asXML();
		  }
		  
	 public static void writXml(Document document) {
			
			try {
				FileOutputStream fos = new FileOutputStream("D:\\a.xml");
				OutputFormat of = new OutputFormat("    ", true);
				XMLWriter xw = new XMLWriter(fos, of);
				xw.write(document);
				xw.close();
				
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			
		}
	 //清空document
	 public int clearxml(){
//		 if(document!=null)
//		 if (root.isTextOnly()) {
//			 root.remove(root);
//		 }
//		 document.clearContent();
		 root.clearContent();
//		 document.remove(root);
		 return 1;
		 
	 }
	 
	/* *//**
	  * 根据编号查各类设备信息
	  */
	 public List getImfomat(String[] a){
		 List list = new ArrayList();
		 /* for(int i=0;i<a.length;i++)
			{
			 	Sbxx sbxx = new Sbxx();
				String str = a[i].toString().substring(0,2);
				if(str.toLowerCase().equals("pc") || str.toLowerCase().equals("la")){
					List l = new ArrayList();
					l = getAllObjectByHql("from AssetComputer  where sbbh='"+a[i]+"'");
					if(l.size()>0){
						AssetComputer ac = (AssetComputer)l.get(0);
						sbxx.setId(ac.getId());
						sbxx.setQysj(ac.getQysj());
						sbxx.setSbbh(ac.getSbbh());
						sbxx.setSydd(ac.getSydd());
						sbxx.setZrr(ac.getZrr());
					}
	  			 }
				else if(str.toLowerCase().equals("gz")){
	  				List l = new ArrayList();
	  				l = getAllObjectByHql("from AssetJz  where sbbh='"+a[i]+"'");
	  				if(l.size()>0){
		  				AssetJz aj=(AssetJz)(l.get(0));
		  				sbxx.setId(aj.getId());
		  				sbxx.setQysj(aj.getQysj());
						sbxx.setSbbh(aj.getSbbh());
						sbxx.setSydd(aj.getSydd());
						sbxx.setZrr(aj.getZrr());
	  				}
	  			 }
				else if(str.toLowerCase().equals("se")){
	  				 List l = new ArrayList();
	  				l = getAllObjectByHql("from AssetServer  where sbbh='"+a[i]+"'");
	  				if(l.size()>0){
		  				AssetServer as = (AssetServer)l.get(0);
		  				sbxx.setId(as.getId());
		  				sbxx.setQysj(as.getQysj());
						sbxx.setSbbh(as.getSbbh());
						sbxx.setSydd(as.getSydd());
						sbxx.setZrr(as.getZrr());
	  				}
	  			 }
				else if(str.toLowerCase().equals("pr") || str.toLowerCase().equals("sc")){
	  				 List l = new ArrayList();
	  				l = getAllObjectByHql("from AssetWssb  where sbbh='"+a[i]+"'");
	  				if(l.size()>0){
		  				AssetWssb aw=(AssetWssb)l.get(0);
		  				sbxx.setId(aw.getId());
		  				sbxx.setQysj(aw.getQysj());
						sbxx.setSbbh(aw.getSbbh());
						sbxx.setSydd(aw.getSydd());
						sbxx.setZrr(aw.getZrr());
	  				}
	  			 }
	  			 if(sbxx.getSbbh()!=null){
	  				 list.add(sbxx);
	  			 }
			}*/
		 return list;
	 }
	 
	 
	 /**
	  * 查找巡检到但没登记到数据库的信息
	  */
	 public List<String> geNu(String[] a){
		 List list = new ArrayList();
		 for(int i=0;i<a.length;i++)
			{
			 	List l = new ArrayList();
				String str = a[i].toString().substring(0,2);
				if(str.toLowerCase().equals("pc") || str.toLowerCase().equals("la")){
					
					l = getAllObjectByHql("from AssetComputer  where sbbh='"+a[i]+"'");
	  			 }
	  			 if(str.toLowerCase().equals("gz")){
	  				l = getAllObjectByHql("from AssetJz  where sbbh='"+a[i]+"'");
	  			 }
	  			 if(str.toLowerCase().equals("se")){
	  				l = getAllObjectByHql("from AssetServer  where sbbh='"+a[i]+"'");
	  			 }
	  			 if(str.toLowerCase().equals("pr") || str.toLowerCase().equals("sc")){
	  				l = getAllObjectByHql("from AssetWssb  where sbbh='"+a[i]+"'");
	  			 }
					if(l.size()==0){
						list.add(a[i]);
					}

			}
			  Set<String> set = new HashSet<String>();
		      List<String> newList = new ArrayList<String>();
		      for (Iterator<String> iter = list.iterator(); iter.hasNext();) {
		          String element = iter.next();
		          if (set.add(element))
		             newList.add(element);
		       } 
		      list.clear();
		      list.addAll(newList);

		 return list;
	 }
	 
	 public List<String> getFu(String[] a,String txtName){
		 List list = new ArrayList();
		 for(int i=0;i<a.length;i++)
			{
			 	List l = new ArrayList();
			 	l = getAllObjectByHql("from Xjjl  where sbbh='"+a[i]+"' and txtName='"+txtName+"'");
			 	if(l.size()==0){
					list.add(a[i]);
				}
			}
		 return list;
	 }
	 /**
	  * 去掉表中重复值
	  */
	 public void Delt(){
//		 List l =getObjectBySql("select * from xjjl a where (a.sbbh,a.txtname) in   (select sbbh,txtname from xjjl group by sbbh,txtname having count(*) > 1) and rowid not in (select min(rowid) from xjjl group by sbbh,txtname having count(*)>1)");
//		 deleteObject("delete from xjjl where sbbh   in (select   sbbh   from xjjl   group   by   sbbh    having   count(sbbh) > 1 ) and rowid not in (select min(rowid) from   xjjl   group by sbbh   having count(sbbh )> 1 ) and txtname   in (select   txtname   from xjjl   group   by   txtname    having   count(txtname) > 1 )");
		// "delete from Xjjl a where (a.sbbh,a.txtName) in   (select sbbh,txtName from Xjjl group by sbbh,txtName having count(*) > 1) and rowid not in (select min(rowid) from Xjjl group by sbbh,txtnName having count(*)>1)");
	 commonDao.deleteByHql("delete from Xjjl where (sbbh,txtName) in   (select sbbh,txtName from Xjjl group by sbbh,txtName having count(*) > 1) and rowid not in (select min(rowid) from Xjjl group by sbbh,txtName having count(*)>1)");
	 }
	 
	 private String errorMsg = "";
	 private int count = 0;
	 
	//用jxl解析excel文件   
		@SuppressWarnings("unchecked")
		public List parseExcel(String fileName,String txtName) {
			List lt = new ArrayList();
		/*	//NumberCell nm;
			FileInputStream input=null;
			Workbook wbk=null;
			errorMsg = "";
			count = 0;
			try {
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");// hh:mm:ss
				 input = new FileInputStream(fileName);
				 wbk = Workbook.getWorkbook(input);// 创建工作面板
				// Sheet st = wbk.getSheet(0); // 获取excel中的第一个sheet
				// Cell c00 = st.getCell(0, 0);// 获取第一行第一列的值(列数,行数)
				Sheet st = wbk.getSheet(0);
				int rowCount = st.getRows();
				int columnCount = st.getColumns();
				for (int r =1; r < rowCount; r++) {
					if(st.getCell(0, r).getContents()!=null&&!st.getCell(0, r).getContents().equals("")){
						Xjjl cmt = new Xjjl();
	                      cmt.setSbbh(st.getCell(0, r).getContents());//设备编号
	                      
	                  try {
	                	 String s =st.getCell(2, r).getContents().substring(0,4); 
	                	 String j =st.getCell(2, r).getContents().substring(4,6); 
	                	 String sj =st.getCell(2, r).getContents().substring(6,8);
	                      if(st.getCell(2, r).getContents() != null & "" != st.getCell(2, r).getContents()){
	                    	  cmt.setXjsj(s+"-"+j+"-"+sj);
	      			    	}
	                      else{
	  						cmt.setXjsj(fmt.format(new Date()));
	  					}
	                      cmt.setTxtname(txtName);
							  lt.add(cmt);
							  saveObjectItem(lt);
						} catch (Exception e) {
							errorMsg += "格式错误！\n";
							count++;
							lt.add(null);
							System.out.println(errorMsg);
							continue;
						}
	                      
					}
				}
			} catch (Exception e) {
		           System.out.println("解析文件异常");
		           e.printStackTrace();
			}finally
			{
				wbk.close();
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// 删除上传文件
//				File file = new File(fileName);
//				if (file.exists()) {
//					file.delete();
//				}
			}*/
			return lt;
		}
		
		/**
		 * 用poi解析excel
		 * @param fileName
		 * @param txtName
		 * @return
		 */
		public List pe(String fileName,String txtName) {
			List lt = new ArrayList();
			/* File f = new File(fileName);   
			 try {   
				             FileInputStream is = new FileInputStream(f);   
				             HSSFWorkbook wbs = new HSSFWorkbook(is);   
				             HSSFSheet childSheet = wbs.getSheetAt(0);   
				             // System.out.println(childSheet.getPhysicalNumberOfRows());   
				             System.out.println("有行数" + childSheet.getLastRowNum());   
				             for (int j = 1; j < childSheet.getLastRowNum()+1; j++) {   
				            	 Xjjl cmt = new Xjjl();
				                 HSSFRow row = childSheet.getRow(j);   
				                 // System.out.println(row.getPhysicalNumberOfCells());   
				                 // System.out.println("有列数" + row.getLastCellNum());   
				                 if (null != row) {   
				                     for (int k = 0; k < row.getLastCellNum(); k++) {   
				                         HSSFCell cell = row.getCell(k);   
				                         if (null != cell) {   
				                             switch (cell.getCellType()) {   
				                             case HSSFCell.CELL_TYPE_NUMERIC: // 数字   
				                                 System.out.print(cell.getNumericCellValue()   
				                                         + "   ");   
				                                 break;   
				                             case HSSFCell.CELL_TYPE_STRING: // 字符串   
				                                 System.out.print(cell.getStringCellValue()   
				                                         + "   ");   
				                                 break;   
				                             case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean   
				                                 System.out.println(cell.getBooleanCellValue()   
				                                         + "   ");   
				                                 break;   
				                             case HSSFCell.CELL_TYPE_FORMULA: // 公式   
				                                 System.out.print(cell.getCellFormula() + "   ");   
				                                 break;   
				                             case HSSFCell.CELL_TYPE_BLANK: // 空值   
				                                 System.out.println(" ");   
				                                 break;   
				                             case HSSFCell.CELL_TYPE_ERROR: // 故障   
				                                 System.out.println(" ");   
				                                 break;   
				                             default:   
				                                 System.out.print("未知类型   ");   
				                                 break;   
				                             }   
				                         } else {   
				                             System.out.print("-   ");   
				                         }   
				                     }   
				                 }   
				                 System.out.println();   
				             }   
				         } catch (Exception e) {   
				             e.printStackTrace();   
				         }   */

			         return lt;

		}
		/**
		 * 解析txt
		 */
		
		public List pt(String fileName,String txtName) {
			int lineNumber = 0; 
			List lt = new ArrayList();
			/*SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");// hh:mm:ss
			 try { 
		            BufferedReader reader = new BufferedReader(new FileReader(fileName)); 
		            while(reader.ready()) { 
		            	 Xjjl cmt = new Xjjl();
		                lineNumber = lineNumber + 1; 
		                String line = reader.readLine(); 
		                int columnNumber = line.length(); 
		                String[] str=line.split(";");
		                cmt.setSbbh(str[0].trim());
		                	 String s =str[2].substring(0,4); 
		                	 String j =str[2].substring(4,6); 
		                	 String sj =str[2].substring(6,8);
		                      if(str[2] != null & "" != str[2]){
		                    	  cmt.setXjsj(s+"-"+j+"-"+sj);
		      			    	}
		                      else{
		  						cmt.setXjsj(fmt.format(new Date()));
		  					}
		                      cmt.setTxtname(txtName);
								  lt.add(cmt);
								  saveObjectItem(lt);
		            }
			 }
		         catch (FileNotFoundException e) { 
		            // TODO Auto-generated catch block
		        	 errorMsg += "格式错误！\n";
						count++;
						lt.add(null);
						System.out.println(errorMsg);
		            e.printStackTrace(); 
		        } catch (IOException e) { 
		            // TODO Auto-generated catch block 
		        	errorMsg += "格式错误！\n";
					count++;
					lt.add(null);
					System.out.println(errorMsg);
		            e.printStackTrace(); 
		        } */

			return lt;
		}

		public List<?> RunSelectClassBySql(String strSQL, String tempClassPath) {
			return commonDao.RunSelectClassBySql(strSQL, tempClassPath);
		}
		
		public boolean RunUpdateBySQL(String strSQL){
			return commonDao.RunUpdateBySQL(strSQL, null);
		}
}

