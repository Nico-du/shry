package net.chinanets.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
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
import net.chinanets.pojos.ListZcxnVo;
import net.chinanets.pojos.ShryFyData;
import net.chinanets.pojos.ShrySydData;
import net.chinanets.pojos.ShryZcData;
import net.chinanets.pojos.ShryZcxnData;
import net.chinanets.service.CommonService;
import net.chinanets.utils.CommonMethods;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

public class ZCXNExcelUploadServlet extends HttpServlet {

	/**
	 * @作用描述:总成性能导入涉及试验单表导入，和总成性能表导入
	 * 保存实验单数据，产生后的id作为联系单id存入总成性能表中的联系单id
	 * @author: 徐超
	 * @date:2016-3-24上午12:28:36
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
            this.doPost(request, response);
	}

 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletContext = request.getSession().getServletContext();
		ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(servletContext);
		CommonService comService = (CommonService) app.getBean("commonService");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String opName = "";
		String str = "";
		try {
			// 获取上传文件流，写入到服务器文件
			DiskFileItemFactory factory = new DiskFileItemFactory();
			String path = request.getRealPath("/");
			factory.setRepository(new File(path)); // 获取临时上传文件夹路径
			factory.setSizeThreshold(1024 * 1024); // 设置上传文件大小临界值
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("utf-8");    //设置编码格式
			upload.setSizeMax(100*1024*1024L); // 限制文件的上传大小100兆
			
			//取得servlet传入的对象参数
			List list = upload.parseRequest(request);
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				FileItem item = (FileItem) iterator.next();
				if (!item.isFormField()) {
					String fileName = item.getName();
					item.write(new File(path, fileName));
					opName = path + fileName;
					// 读取上传文件、解析插入数据库
					ListZcxnVo  listxnVo = parseExcel(opName);
					Long returnId=null ; //保存实验单返回后的实验单id
				   if (listxnVo.getResult()==null && listxnVo.getZcxnDataList()!=null) {
					   //校验数据唯一性是否已经存在
					   int count = comService.getCountByObject(listxnVo.getSydData());
					   if(count==0){
						   //不存在则保存实验单数据，并返回试验单id 用于总成性能表中试验单id保存
						   listxnVo.getSydData().setInputdate(new Date());
						   listxnVo.getSydData().setInputuser("1");
						   listxnVo.getSydData().setUpdatedate(new Date());
						   listxnVo.getSydData().setUpdateuser("1");
						   
						   //取试验单的风叶型号，在风叶表中查询风叶信息，取查询记录中的风叶id保存总成性能表中的风叶id
					/*	   String  sql ="select * from shry_fy_data where 1=1 and xh='"+listxnVo.getSydData().getFyxh()+"'  ";
						   String tempClassPath = "net.chinanets.pojo.ShryFyData";
						   List<ShryFyData> listfyData=  (List<ShryFyData>) comService.RunSelectClassBySql(sql, tempClassPath);*/
						   //根据试验单中的总成型号，校验改记录是否在总成数据总唯一
						   List<ShryZcData> listZcData = comService.getInfoById(listxnVo.getSydData().getZcxh(), new ShryZcData());
						   if(listZcData.size()>0&&listZcData.get(0)!=null){
								  //保存实验单数据，返回试验单id
								  returnId = comService.saveObject(listxnVo.getSydData());
								  for (int i = 0; i <listxnVo.getZcxnDataList().size(); i++) {
										//保存总成性能数据
									   listxnVo.getZcxnDataList().get(i).setLxdid(returnId);
									   listxnVo.getZcxnDataList().get(i).setZcid(listZcData.get(0).getZcid());
									   listxnVo.getZcxnDataList().get(i).setFyid(listZcData.get(0).getFyid());
									   listxnVo.getZcxnDataList().get(i).setUpdatedate(new Date());
									   listxnVo.getZcxnDataList().get(i).setUpdateuser("1");
									   listxnVo.getZcxnDataList().get(i).setXl(listxnVo.getSydData().getZcxh());
									   comService.saveObject(listxnVo.getZcxnDataList().get(i));
							    	 }
								   str="导入数据成功！";
						   }else{
							   str="该型号："+listxnVo.getSydData().getZcxh()+"的总成数据不存在！";
						   }
					     }else{
						   str="数据已存在，请检查数据！";
					   }
					}else{
						str=listxnVo.getResult().toString();
					}
				}
			}
		} catch (Exception e) {
			out.write("导入异常！请检查数据");
			e.printStackTrace();
		}finally{
			out.write(str.toString());
		}
	}

	private ListZcxnVo  parseExcel(String opName) {
		FileInputStream input = null;
		Workbook wbk = null;
		ListZcxnVo  listxnVo = null;
		ShryZcxnData  zcxnData ;  //总成性能数据
		ShrySydData  sydData;      //试验单数据
		String name = opName.substring(opName.lastIndexOf(".")+1,opName.length());
		if(!"xls".equals(name)){
			listxnVo = new ListZcxnVo();
			listxnVo.setResult("文件格式不正确，请重新选择xls后缀的文件！");
			return listxnVo;
		}
		try {
			input = new FileInputStream(opName);
			wbk = Workbook.getWorkbook(input);// 创建工作面板
			Sheet st = wbk.getSheet(0); // 获取excel中的第一个sheet
			int clos = st.getColumns();// 得到所有的列
			int rows = st.getRows();// 得到所有的行
			List<ShryZcxnData> zcxnList = new ArrayList<ShryZcxnData>();
			List<ShrySydData> sydList = new ArrayList<ShrySydData>();
			String lxdh = st.getCell(4, 3).getContents().trim(); //联系单号
			//148#/RY0.1504010 截取“/”后面作为联系单号
			lxdh = lxdh.substring(lxdh.indexOf("/")+1);
			String zcxh = st.getCell(1,6).getContents().trim();//总成型号
			String fyxh = st.getCell(1,7).getContents().trim();//风叶型号
			String syry = st.getCell(1, 10).getContents().trim();//试验人员
			String sply = st.getCell(1, 9).getContents().trim();//试品来源
			String yps = st.getCell(7, 6).getContents().trim();//叶片数
			String  fyzj = st.getCell(7, 5).getContents().trim();//风叶直径
			String  fyxs = st.getCell(7, 3).getContents().trim();//风叶型式
			String bz = st.getCell(1, 11).getContents().trim();//备注
			
          	/*实验单数据*/
		   sydData = new ShrySydData();
    		sydData.setLxdh(lxdh);
			sydData.setFyxh(fyxh);
			sydData.setZcxh(zcxh);
			sydData.setSyry(syry);
			sydData.setYps(yps);
			sydData.setFyzj(fyzj);
			sydData.setFjxs(fyxs);
			sydData.setSply(sply);
			sydData.setMemo(bz);
		    sydData.setSydx("总成");
			for(int i=15;i<rows;i++){
				zcxnData = new ShryZcxnData();
				listxnVo = new ListZcxnVo();
					//过滤到空数据行
				if(StringUtils.isNotBlank(st.getCell(1, 15).getContents().trim())){
	               String  flow = st.getCell(1, i).getContents().trim(); //流量
	               String  jy = st.getCell(2,i).getContents().trim();      //静压
	               String  mainFy = st.getCell(3, i).getContents().trim();//主风扇
	               String  subFy = st.getCell(4, i).getContents().trim();//辅风扇
	               String  dy = st.getCell(5, i).getContents().trim();//电压
	               String  dl = st.getCell(6,i).getContents().trim();//电流
	               String  sr = st.getCell(7, i).getContents().trim(); //输入
	               String  xl = st.getCell(8,i).getContents().trim();//效率
	  
	               if(CommonMethods.isBlank(flow)&&CommonMethods.isBlank(jy)&&CommonMethods.isBlank(mainFy)
	            		   &&CommonMethods.isBlank(subFy)&&CommonMethods.isBlank(dy)&&CommonMethods.isBlank(dl)
	            		   &&CommonMethods.isBlank(sr)&&CommonMethods.isBlank(xl)){
	            	   break;
	               }
	               /*数据合法性校验*/
	           	if(CommonMethods.isBlank(flow) && !CommonMethods.isDouble(flow)){
	           		listxnVo.setResult("第"+i+"行流量为空或者不是数字");
	           		return listxnVo;
				} 
	           	if(CommonMethods.isBlank(jy) && !CommonMethods.isDouble(jy)){
	           		listxnVo.setResult("第"+i+"行静压为空或者不是数字");
	           		return listxnVo;
				} 
	        	if(CommonMethods.isBlank(mainFy) && !CommonMethods.isDouble(mainFy)){
	        		listxnVo.setResult("第"+i+"行静压为空或者不是数字");
	           		return listxnVo;
				} 
	        	if(CommonMethods.isBlank(subFy) && !CommonMethods.isDouble(subFy)){
	        		listxnVo.setResult("第"+i+"行辅风扇为空或者不是数字");
	           		return listxnVo;
				} 
	        	if(CommonMethods.isBlank(dy) && !CommonMethods.isDouble(dy)){
	        		listxnVo.setResult("第"+i+"行电压为空或者不是数字");
	           		return listxnVo;
				} 
	        	if(CommonMethods.isBlank(dl) && !CommonMethods.isDouble(dl)){
	        		listxnVo.setResult("第"+i+"行电流为空或者不是数字");
	           		return listxnVo;
				} 
	        	if(CommonMethods.isBlank(sr) && !CommonMethods.isDouble(sr)){	
	        	   listxnVo.setResult("第"+i+"行效率为空或者不是数字");
	           		return listxnVo;
				} 
	        	
	        	/*总成性能数据*/
            	zcxnData.setLl(flow);
               	zcxnData.setJyl(jy);
               	zcxnData.setZzs(mainFy);
               	zcxnData.setFzs(subFy);
               	zcxnData.setDy(dy);
               	zcxnData.setDl(dl);
               	zcxnData.setSrgl(sr);
               	zcxnData.setXl(xl);
               	zcxnData.setInputdate(new Date());
               	zcxnData.setUpdatedate(new Date());
               	zcxnData.setInputuser("1");
				zcxnList.add(zcxnData);
			}
		}
			listxnVo.setSydData(sydData);
			listxnVo.setZcxnDataList(zcxnList);
			wbk.close();
			input.close();
	}catch(Exception e){
			e.printStackTrace();
		}
		return listxnVo;
	}

}
