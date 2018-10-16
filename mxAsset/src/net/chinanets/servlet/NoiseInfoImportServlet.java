package net.chinanets.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;
import net.chinanets.pojos.ListZsVo;
import net.chinanets.pojos.ShryFyData;
import net.chinanets.pojos.ShryFyZsData;
import net.chinanets.service.CommonService;
import net.chinanets.utils.CommonMethods;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;


public class NoiseInfoImportServlet extends HttpServlet {
	/**
	 * @作用描述：噪声数据导入包含：噪声数据表
	 * @author: 徐超
	 * @date:2016-6-17下午10:24:33
	 */
	private static final long serialVersionUID = 1L;
	public String str ="";
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
               this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
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
					ListZsVo  listVo = parseExcel(opName);
				   if(listVo.getResult()==null && listVo.getZsList()!=null && !listVo.getZsList().isEmpty()) {
						ServletContext servletContext = request.getSession().getServletContext();
						ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(servletContext);
						CommonService comService = (CommonService) app.getBean("commonService");
						for (int i = 0; i <listVo.getZsList().size(); i++) {
							//根据风叶型号查询风叶数据表。判断该风叶型号是已经存在,如果不存在则给出提示，
							String hql = "From ShryFyData where xh="+listVo.getFyList().get(0).getXh();
							List<ShryFyData> fys = comService.getAllObjectByHql(hql);
							   if(fys.size()<1){
								   str ="该型号对应的风叶数据不存在，请检查型号为："+listVo.getFyList().get(i).getXh()+"的数据";
								   listVo.setResult(str);
								   break;
							   }
							   //保存噪声表
							   listVo.getZsList().get(i).setInputdate(new Date());
							   listVo.getZsList().get(i).setFyid(fys.get(0).getFyid());
							   listVo.getZsList().get(i).setInputuser(importuser);
							   listVo.getZsList().get(i).setUpdatedate(new Date());
							   listVo.getZsList().get(i).setUpdateuser(importuser);
							   
							   comService.saveObject( listVo.getZsList().get(i));
							   
							   }
					}else{
						str=listVo.getResult(); 
					}
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			out.write(str.getBytes("utf-8").toString());
		}
	}


	    // 解析excel文件转换成对象
		private  ListZsVo parseExcel(String fileName) {
			        ListZsVo  vo  = null;
					FileInputStream input = null;
					Workbook wbk = null;
					ShryFyData fyData;
					ShryFyZsData zsData;
					List<ShryFyData> fyList=  new ArrayList<ShryFyData>();
					List<ShryFyZsData> zsList=  new ArrayList<ShryFyZsData>();
					String name = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
					if(!"xls".equals(name)){	
						vo = new ListZsVo();
						vo.setResult("文件格式不正确，请重新选择xls后缀的文件！");
						return vo;
					}
					try {
						input = new FileInputStream(fileName);
						wbk = Workbook.getWorkbook(input);// 创建工作面板
						Sheet st = wbk.getSheet(0); // 获取excel中的第一个sheet
						int clos = st.getColumns();// 得到所有的列
						int rows = st.getRows();// 得到所有的行
						vo =  new  ListZsVo();
						/**
						 * Cell getCell(int column, int row) 注意第一个是列数 第二个是行数
						 */
						//对参数模版格式进行校验
						String fyxhName = st.getCell(0, 0).getContents().trim(); //风叶型号
						String cebzName = st.getCell(0, 1).getContents().trim(); //测试标准
						if( !"风叶型号".equals(fyxhName) || !"测试标准".equals(cebzName)){
							vo.setResult("参数模版格式不正确！请检查参数导入模版(检查方式：1.只支持2003.xls格式版本,2.下载模版对比表头)");
							return vo;
						}
						//保存风叶对象数据
						fyData = new ShryFyData();
						fyData.setXh(fyxhName);
						fyList.add(fyData);
						
						for (int i = 4; i < rows; i++) {
								//过滤掉为空的行
							 if(StringUtils.isNotBlank(st.getCell(3,i).getContents())){
								zsData = new ShryFyZsData();
								
								/*噪声对象数据*/
								String zs = st.getCell(0, i).getContents().trim();   // 转速
								String voise = st.getCell( 1,i).getContents().trim(); //噪声
								
								if(CommonMethods.isBlank(zs) ){
									vo.setResult("第"+i+"行转速为空!");
									return vo;
								} 
								if(CommonMethods.isBlank(voise) ){
									vo.setResult("第"+i+"行噪声空!");
									return  vo;
								}
									
								zsData.setSpeed(zs);
								zsData.setCsbz(cebzName);
								zsData.setNoise(voise);
								zsList.add(zsData);
								}
							}
					    vo.setZsList(zsList);
					    vo.setFyList(fyList);
						wbk.close();
						input.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					return vo;
				}
}