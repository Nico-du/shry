package net.chinanets.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Sheet;
import jxl.Workbook;
import net.chinanets.pojos.ListVo;
import net.chinanets.pojos.ShryDjData;
import net.chinanets.pojos.ShryFyData;
import net.chinanets.pojos.ShryZcData;
import net.chinanets.service.CommonService;
import net.chinanets.utils.CommonMethods;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;


public class ParamDataExcelUploadServlet extends HttpServlet {
	/**
	 * @作用描述：参数数据导入包含：总成数据表、风叶数据表、电机数据表
	 * @author: 徐超
	 * @date:2016-3-17下午10:24:33
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
		String opName = "";
		try {
			// 获取上传文件流，写入到服务器文件
			DiskFileItemFactory factory = new DiskFileItemFactory();
			String path = request.getRealPath("/");
			factory.setRepository(new File(path)); // 获取临时上传文件夹路径
			factory.setSizeThreshold(1024 * 1024); // 设置上传文件大小临界值
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("utf-8");    //设置编码格式
			upload.setSizeMax(100*1024*1024L); // 限制文件的上传大小100za
			
			//取得servlet传入的对象参数
			List list = upload.parseRequest(request);
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				FileItem item = (FileItem) iterator.next();
				if (!item.isFormField()) {
					String fileName = item.getName();
					item.write(new File(path, fileName));
					opName = path + fileName;
					// 读取上传文件、解析插入数据库
				    ListVo  listVo = parseExcel(opName);
				   if (listVo.getResult()==null && listVo.getFyDataList()!=null) {
						ServletContext servletContext = request.getSession().getServletContext();
						ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(servletContext);
						CommonService comService = (CommonService) app.getBean("commonService");
						for (int i = 0; i <listVo.getFyDataList().size(); i++) {
							//根据风叶型号查询风叶数据表。判断该风叶型号是已经存在，如果已经存在则在该风叶型号前面加1#，若再有重复一次递增
							   int fyCount = comService.getCountByObject(listVo.getFyDataList().get(i));
							   if(fyCount>0){
								   listVo.getFyDataList().get(i).setXh(i+"#"+listVo.getFyDataList().get(i).getXh());
							   }
								/* 保存风叶对象后返回的id，用于赋值给总成表中的风叶id */
							   Long num = comService.saveObject(listVo.getFyDataList().get(i));
							   //保存电机数据表，并将电机id返回给总成表中的电机id
							   listVo.getDjDataList().get(i).setInputdate(new Date());
							   listVo.getDjDataList().get(i).setInputuser("1");
							   listVo.getDjDataList().get(i).setUpdatedate(new Date());
							   listVo.getDjDataList().get(i).setUpdateuser("1");
							   //判断电机数据是否已经存在，给出提示，不存在保存数据库
							   String sql = "select count(*) from shry_dj_data where  xh='"+listVo.getDjDataList().get(i).getXh()+"'";
							   int djCount = comService.getOneBysql(sql);
							   if(djCount>0){
								   str = "该电机型号："+listVo.getDjDataList().get(i).getXh()+"的数据已经存在！";
							   }else{
								   listVo.getZcDataList().get(i).setFyid(num);
								   listVo.getZcDataList().get(i).setInputdate(new Date());
								   listVo.getZcDataList().get(i).setInputuser("1");
								   listVo.getZcDataList().get(i).setUpdatedate(new Date());
								   listVo.getZcDataList().get(i).setUpdateuser("1");
								   //保存电机数据
								   Long  djId = comService.saveObject( listVo.getDjDataList().get(i));
								   //保存总成数据
								   listVo.getZcDataList().get(i).setDjid(String.valueOf(djId));
								   comService.saveObject(listVo.getZcDataList().get(i));
								   
								   str="导入数据成功!";
							   }
						}
					}else{
						str=listVo.getResult().toString();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			out.write(str.toString());
		}
	}


	    // 解析excel文件转换成对象
		private  ListVo parseExcel(String fileName) {
			       ListVo  vo  = null;
					FileInputStream input = null;
					Workbook wbk = null;
					ShryFyData fyData;
					ShryZcData zcData;
					ShryDjData djData;
					List<ShryFyData> fyList=  new ArrayList<ShryFyData>();
					List<ShryZcData> zcList=  new ArrayList<ShryZcData>();
					List<ShryDjData>  djList = new ArrayList<ShryDjData>();
					String name = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
					if(!"xls".equals(name)){
						vo = new ListVo();
						vo.setResult("文件格式不正确，请重新选择xls后缀的文件！");
						return vo;
					}
					try {
						input = new FileInputStream(fileName);
						wbk = Workbook.getWorkbook(input);// 创建工作面板
						Sheet st = wbk.getSheet(0); // 获取excel中的第一个sheet
						int clos = st.getColumns();// 得到所有的列
						int rows = st.getRows();// 得到所有的行
						vo =  new  ListVo();
						/**
						 * 因excel中数据包含三张表的数据，故针对不同表分别导入（总成数据表、风叶数据表、电机数据表（暂不考虑））
						 * Cell getCell(int column, int row) 注意第一个是列数 第二个是行数
						 */
							for (int i = 1; i < rows; i++) {
								//过滤掉为空的行
								if(StringUtils.isNotBlank(st.getCell(3,i).getContents())){
									fyData = new ShryFyData();
									zcData = new ShryZcData();
									djData = new ShryDjData();
									//对参数模版格式进行校验
									String djxhName = st.getCell(1, 0).getContents().trim();
									String fsccName = st.getCell(2, 0).getContents().trim();
									String fyxhName = st.getCell(3,0).getContents().trim();
									if( !"电机型号".equals(djxhName) || !"风扇尺寸(mm)".equals(fsccName) || !"风叶型号".equals(fyxhName)){
										vo.setResult("参数模版格式不正确！请检查参数导入模版");
										break;
									}
									
									/*风叶对象数据*/
									String xh = st.getCell(3, i).getContents().trim(); // 风叶型号
									String fbzj = st.getCell( 4,i).getContents().trim(); // 翻边直径
									String dlhzj = st.getCell(5,i).getContents().trim(); // 导流环直径
									String lgzj = st.getCell(6,i).getContents().trim(); // 轮毂直径
									String lggd = st.getCell( 7,i).getContents().trim(); // 轮毂高度
									String zl = st.getCell(8,i).getContents().trim(); // 风叶重量
									String ypsm = st.getCell(9,i).getContents().trim(); // 叶片数目
									String jqfs = st.getCell(10,i).getContents().trim(); // 进气方式
									String cl = st.getCell( 11,i).getContents().trim(); // 材料
									String clbz = st.getCell(12,i).getContents().trim(); // 材料标注
									String qj = st.getCell(13,i).getContents().trim(); // 嵌件
									
									/*总成对象数据*/
									String zcxh = st.getCell(0, i).getContents().trim(); //总成型号
									String zccc = st.getCell(2, i).getContents().trim();//外形尺寸
									String sycx = st.getCell(15, i).getContents().trim();//使用车型
						           /*电机对象数据*/
									String djxh = st.getCell(1, i).getContents().trim(); //电机型号
									if(CommonMethods.isBlank(xh) ){
										vo.setResult("第"+i+"行型号为空!");
										return vo;
									} 
							/*		if(CommonMethods.isBlank(fbzj)&& !CommonMethods.isDouble(fbzj)){
									 vo.setResult("第"+i+"行翻边外径为空或者不是数值类型");
					        		 return vo;
					        	 }
					       	      if(CommonMethods.isBlank(dlhzj)&& !CommonMethods.isDouble(dlhzj)){
					        		vo.setResult("第"+i+"行导环流外径为空或者不是数值类型");
					        		 return vo;
					        	 }
					       	/* if(CommonMethods.isBlank(lgzj)&& !CommonMethods.isDouble(lgzj)){
					        		vo.setResult("第"+i+"行轮毂直径为空或者不是数值类型");
					        		 return vo;
					        	 }
					        	 if(CommonMethods.isBlank(lggd)&& !CommonMethods.isDouble(lggd)){
					        		vo.setResult("第"+i+"行轮毂高度为空或者不是数值类型");
					        		 return vo;
					        	 }
					        	 if(CommonMethods.isBlank(zl)&& !CommonMethods.isDouble(zl)){
					        		vo.setResult("第"+i+"行风叶重量为空或者不是数值类型");
					        		 return vo;
					        	 }
					        	 if(CommonMethods.isBlank(ypsm)&& !CommonMethods.isDouble(ypsm)){
					        		 vo.setResult("第"+i+"行叶片数为空或者不是数值类型");
					        		 return vo;
					        	 }*/
									if(CommonMethods.isBlank(jqfs) ){
										vo.setResult("第"+i+"行风叶型式为空!");
										return  vo;
									}
									if(CommonMethods.isBlank(cl) ){
										vo.setResult("第"+i+"行材料为空!");
										return  vo;
									}
									/* if(CommonMethods.isBlank(clbz) ){
					        		vo.setResult("第"+i+"行材料标准为空!");
					        		 return  vo;
					        	 }
					        	 if(CommonMethods.isBlank(qj) ){
					        		 vo.setResult("第"+i+"行嵌件为空!");
					        		 return  vo;
					        	 }
					        	 
					        	  if(CommonMethods.isBlank(zcxh) ){
					        		 vo.setResult("第"+i+"行总成型号为空!");
					        		 return  vo;
					        	 }
					        	  if(CommonMethods.isBlank(djxh) ){
					        		 vo.setResult("第"+i+1+"行电机型号为空!");
					        		 return  vo;
					        	 }
					        	  if(CommonMethods.isBlank(zccc) ){
					        		 vo.setResult("第"+i+1+"行外形尺寸为空!");
					        		 return  vo;
					        	 }
					        	 */
									fyData.setXh(xh);
									fyData.setDlhzj(dlhzj);
									fyData.setFbzj(fbzj);
									fyData.setInputdate(new Date());
									fyData.setInputuser("1");
									fyData.setJqfs(jqfs);
									fyData.setLggd(lggd);
									fyData.setLgzj(lgzj);
									fyData.setQj(qj);
									fyData.setUpdatedate(new Date());
									fyData.setUpdateuser("1");
									fyData.setXh(xh);
									fyData.setYpsm(ypsm);
									fyData.setZl(zl);
									fyData.setCl(cl);
									fyData.setClbz(clbz);
									
									zcData.setXh(zcxh);
									zcData.setWxcc(zccc);
									zcData.setSycx(sycx);
									djData.setXh(djxh);
									
									fyList.add(fyData);
									zcList.add(zcData);
									djList.add(djData);
								}
							}
					    vo.setFyDataList(fyList);
					    vo.setZcDataList(zcList);
					    vo.setDjDataList(djList);
						wbk.close();
						input.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					return vo;
				}
}