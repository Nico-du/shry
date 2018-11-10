package net.chinanets.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import net.chinanets.pojos.ListCjsjVo;
import net.chinanets.pojos.ShryCjsjData;
import net.chinanets.service.CommonService;
import net.chinanets.utils.CommonMethods;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.mortbay.log.Log;
import org.springframework.context.ApplicationContext;


public class CJSJInfoImportServlet extends HttpServlet {
	/**
	 * @作用描述：插件数据导入
	 * @author: dzj
	 * @date:2018-10-17上午10:24:33
	 */
	private static final long serialVersionUID = 1L;
	public String str ;
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
               this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		str ="导入成功！";
		try {
			String importuser = request.getParameter("importuser");
			// 获取上传文件流，写入到服务器文件
			DiskFileItemFactory factory = new DiskFileItemFactory();
			String path = request.getRealPath("/");
			factory.setRepository(new File(path)); // 获取临时上传文件夹路径
			factory.setSizeThreshold(1024 * 1024); // 设置上传文件大小临界值
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("utf-8");    //设置编码格式
			upload.setSizeMax(100*1024*1024L); // 限制文件的上传大小100
			
			//取得servlet传入的对象参数
			List<FileItem> list = upload.parseRequest(request);
			for (FileItem item :list) {
				if(!item.isFormField()){
					String fileName = item.getName();
					item.write(new File(path, fileName));
					String opName = path + fileName;
					// 读取上传文件、解析插入数据库
					System.out.println("==================="+opName);
					//插件数据导入  sheet1：有刷 sheet2:无刷
					ListCjsjVo  listVo = parseExcel(opName);
				   if(listVo.getResult()==null && listVo.getCjsjList()!=null && !listVo.getCjsjList().isEmpty()) {
						ServletContext servletContext = request.getSession().getServletContext();
						ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(servletContext);
						CommonService comService = (CommonService) app.getBean("commonService");
						
						List<ShryCjsjData> djList = listVo.getCjsjList();
						Set<String> djxhSet = new HashSet<String>();
//						for (int i = 0; i <djList.size(); i++) {
//							//根据风叶型号查询风叶数据表。判断该风叶型号是已经存在,如果不存在则给出提示，
//							String hql = "From ShryFyData where xh='"+djList.get(i).getFyxh()+"'";
//							List<ShryFyData> fys = comService.getAllObjectByHql(hql);
//							if(fys == null || fys.size()<1){
//								str ="该风叶型号数据库中不存在，请检查型号为："+djList.get(i).getFyxh()+"的数据";
//								listVo.setResult(str);
//								return;
//							}else{
//								djList.get(i).setFyid(fys.get(0).getFyid());
//							}
//
//							hql = "From ShryZcData where xh='"+djList.get(i).getZcxh()+"'";
//							List<ShryZcData> zcs = comService.getAllObjectByHql(hql);
//							if(zcs == null ||zcs.size()<1){
//								str ="该总成型号数据库中不存在，请检查型号为："+djList.get(i).getZcxh()+"的数据";
//								listVo.setResult(str);
//								return;
//							}else{
//								djList.get(i).setZcid(zcs.get(0).getZcid());
//							}
//							if(djxhSet.contains(djList.get(i).getDjxh())){
//								str ="该Excel中电机型号存在重复值,请检查型号:"+djList.get(i).getDjxh();
//								listVo.setResult(str);
//								return;
//							}
//							djxhSet.add(djList.get(i).getDjxh());
//						}
						
						
						//保存
						for (int i = 0; i <listVo.getCjsjList().size(); i++) {
							listVo.getCjsjList().get(i).setInputdate(new Date());
							listVo.getCjsjList().get(i).setInputuser(importuser);
							listVo.getCjsjList().get(i).setUpdatedate(new Date());
							listVo.getCjsjList().get(i).setUpdateuser(importuser);

							comService.saveObject(listVo.getCjsjList().get(i));

						}
					}else{
						str=listVo.getResult(); 
					}
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
			str = "执行失败："+e.getMessage();
		}finally{
			Log.info("导入结束:"+str);
//			out.write(str.getBytes("utf-8").toString());
			out.write(str);
		}
	}

	
	
	
	
	

	    // 解析excel文件转换成对象 //插件数据导入  sheet1：有刷 sheet2:无刷
	//校验格式正确
		private  ListCjsjVo parseExcel(String fileName) {
			        ListCjsjVo  vo  = null;
					FileInputStream input = null;
					Workbook wbk = null;
					
					String name = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
					if(!"xls".equals(name)){	
						vo = new ListCjsjVo();
						vo.setResult("文件格式不正确，请重新选择xls后缀的文件！");
						return vo;
					}
					try {
						input = new FileInputStream(fileName);
						wbk = Workbook.getWorkbook(input);// 创建工作面板
						Sheet st1 = wbk.getSheet(0); // 获取excel中的第1个sheet 有刷
						List<ShryCjsjData> djList=  new ArrayList<ShryCjsjData>();
						
						/*=========有刷=======*/
						{
						int clos = st1.getColumns();// 得到所有的列
						int rows = st1.getRows();// 得到所有的行
						vo =  new  ListCjsjVo();
						/**
						 * Cell getCell(int column, int row) 注意第一个是列数 第二个是行数
						 */
						//对参数模版格式进行校验
						String name0 = st1.getCell(0, 1).getContents().trim(); 
						String name1 = st1.getCell(1, 1).getContents().trim(); 
						String name2 = st1.getCell(2, 1).getContents().trim(); 
						String name3 = st1.getCell(3, 1).getContents().trim(); 
						String name13 = st1.getCell(13, 1).getContents().trim()+""; 
						String name14 = st1.getCell(14, 1).getContents().trim()+""; 
						String name15 = st1.getCell(15, 1).getContents().trim()+""; 
						
						if( !"图号".equals(name0) || !"总成型号".equals(name1)|| !"客户图号".equals(name2) || !"车型项目".equals(name3)
								|| !name13.contains("环境工况") || !name14.contains("插拔力") || !name15.contains("档位")){
							vo.setResult("Sheet1插件数据模版格式不正确！请检查参数导入模版(检查方式：1.只支持2003.xls格式版本,2.下载模版对比表头)");
							return vo;
						}
						
						ShryCjsjData djData;
						//校验
						for (int i = 2; i < rows; i++) {
								//过滤掉为空的行
							 if(StringUtils.isNotBlank(st1.getCell(4,i).getContents())){
								 djData = new ShryCjsjData();
								
								/*对象数据*/
								String clm2 = getSheetCell(st1, 0,i); 
								String clm3 = getSheetCell(st1, 1,i); 
								
								if(CommonMethods.isBlank(clm2) ){
									vo.setResult("第"+(i+1)+"行图号为空!");
									return vo;
								} 
								if(CommonMethods.isBlank(clm3)){
									vo.setResult("第"+(i+1)+"行总成型号空!");
									return  vo;
								}
									
								this.setData(st1, i, djData);
								djList.add(djData);
								}
							}
						}
						
						vo.setCjsjList(djList);
					} catch (Exception ex) {
						ex.printStackTrace();
					}finally{
						try {
							wbk.close();
							input.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return vo;
				}
		/**
		 * 设值
		 * @param st
		 * @param i
		 * @param data
		 */
		private void setData(Sheet st,int i,ShryCjsjData data){
			data.setTh       (getSheetCell(st, 0,i));
			data.setZcxh     (getSheetCell(st, 1,i));
			data.setCx       (getSheetCell(st, 2 ,i));
			data.setXm       (getSheetCell(st, 3 ,i));
			data.setCjcl     (getSheetCell(st, 4 ,i));
			data.setCpcl     (getSheetCell(st, 5 ,i));
			data.setCpdc     (getSheetCell(st, 6 ,i));
			data.setXj       (getSheetCell(st, 7 ,i));
			data.setPinj     (getSheetCell(st, 8 ,i));
			data.setDwj      (getSheetCell(st, 9 ,i));
			data.setDl       (getSheetCell(st, 10 ,i));
			data.setZddj     (getSheetCell(st, 11,i));
			data.setFhdj     (getSheetCell(st, 12,i));
			data.setHjgk     (getSheetCell(st, 13,i));
			data.setCbl      (getSheetCell(st, 14,i));
			data.setDw       (getSheetCell(st, 15,i));
//			data.setSyzt      (getSheetCell(st, 2,i));
//			data.setMemo      (getSheetCell(st, 22,i));
		}
		
		private String getSheetCell(Sheet st,int x,int y){
			return st.getCell(x, y).getContents().trim();
		}
}