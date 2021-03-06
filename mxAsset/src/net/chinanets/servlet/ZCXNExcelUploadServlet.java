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
import net.chinanets.excel.StringDateUtil;
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

 
	@SuppressWarnings("unused")
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
			String importuser = request.getParameter("importuser");
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
					   /**
					    * 总成性能导入，查询是否存在重复记录
					    * param:   试验单号 +试验性质+试验日期
					    */
					List<ShrySydData>  listData = comService.getZCSydByParam(listxnVo.getSydData().getLxdh(), listxnVo.getSydData().getSymd(), listxnVo.getSydData().getSyrq());
					   if(listData.isEmpty() || listData==null){
						   //不存在则保存实验单数据，并返回试验单id 用于总成性能表中试验单id保存
						   listxnVo.getSydData().setInputdate(new Date());
						   listxnVo.getSydData().setInputuser(importuser);
						   listxnVo.getSydData().setUpdatedate(new Date());
						   listxnVo.getSydData().setUpdateuser(importuser);
						   
						   
						   //取试验单的风叶型号，在总成表中查询风叶信息，取查询记录中的风叶id，总成id，电机id
						   List<ShryZcData> listZcData = comService.getInfoById(listxnVo.getSydData().getZcxh(), new ShryZcData());
						   //根据试验单中的总成型号，校验改记录是否在总成数据总唯一
						   if(listZcData.size()>0&&listZcData.get(0)!=null){
							     //保存风叶id总成id
							      listxnVo.getSydData().setFyid(listZcData.get(0).getFyid());
							      listxnVo.getSydData().setZcid(listZcData.get(0).getZcid());
							      listxnVo.getSydData().setDjid(Long.parseLong(listZcData.get(0).getDjid()));
							      //保存实验单数据，返回试验单id
								  returnId = comService.saveObject(listxnVo.getSydData());
								  for (int i = 0; i <listxnVo.getZcxnDataList().size(); i++) {
										//保存总成性能数据
									   listxnVo.getZcxnDataList().get(i).setLxdid(returnId);
									   listxnVo.getZcxnDataList().get(i).setZcid(listZcData.get(0).getZcid());
									   listxnVo.getZcxnDataList().get(i).setFyid(listZcData.get(0).getFyid());
									   listxnVo.getZcxnDataList().get(i).setInputdate(new Date());
									   listxnVo.getZcxnDataList().get(i).setInputuser(importuser);
									   listxnVo.getZcxnDataList().get(i).setUpdatedate(new Date());
									   listxnVo.getZcxnDataList().get(i).setUpdateuser(importuser);
									   comService.saveObject(listxnVo.getZcxnDataList().get(i));
							    	 }
								   str="导入数据成功！";
						   }else{
							   str="该型号："+listxnVo.getSydData().getZcxh()+"的总成数据不存在！";
						   }
					     }else{
						   str="该总成性能模版中，实验单号+试验性质+试验日期，存在重复记录，请检查数据！";
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
			
		    //对模版标题行校验，判断模版是否是风叶性能导入模版
            String zfsName = st.getCell(3,13).getContents().trim();
            String ffsName = st.getCell(4,13).getContents().trim();
            String dyName = st.getCell(5, 13).getContents().trim();
            listxnVo = new ListZcxnVo();
            if(!"主风扇".equals(zfsName) || !"辅风扇".equals(ffsName) || !"电压".equals(dyName)){
         	   listxnVo.setResult("该模版不符合总成性能导入模版格式！请检查导入模版");
         	   return listxnVo;
            }
            
			String lxdh = st.getCell(4, 3).getContents().trim(); //联系单号
			//148#/RY0.1504010 截取“/”后面作为联系单号
			lxdh = lxdh.substring(lxdh.indexOf("/")+1);
			String  syrq = st.getCell(1,3).getContents().trim();//试验日期
			String zcxh = st.getCell(1,6).getContents().trim();//总成型号
			String fyxh = st.getCell(1,7).getContents().trim();//风叶型号
			String syry = st.getCell(1, 10).getContents().trim();//试验人员
			String sply = st.getCell(1, 9).getContents().trim();//试品来源
			String yps = st.getCell(7, 6).getContents().trim();//叶片数
			String  fyzj = st.getCell(7, 5).getContents().trim();//风叶直径
			String  fjxs = st.getCell(6, 3).getContents().trim();//风机型式
			String bz = st.getCell(1, 11).getContents().trim();//备注
			String symd = st.getCell(1,8).getContents().trim();//试验性质
			
			String syfd = st.getCell(1,4).getContents().trim();//试验风洞:
			String ckmj = st.getCell(7,4).getContents().trim();//出口面积
			String syfs = st.getCell(1,5).getContents().trim();//试验方式
			
			String dqy = st.getCell(7,7).getContents().trim();//大气压
			String kqwd = st.getCell(7,8).getContents().trim();//空气温度(℃)	

			String xdsd = st.getCell(7,9).getContents().trim();//相对湿度(%RH)	
			String skqbz = st.getCell(7,10).getContents().trim();//湿空气比重(kg/m3)	

			
			if(StringUtils.isBlank(lxdh)){
				 listxnVo.setResult("联系单号为空(截取“/”后面作为联系单号)！请检查导入模版");
	         	   return listxnVo;
			}
			if(StringUtils.isBlank(zcxh)){
				 listxnVo.setResult("风扇型号为空！请检查导入模版");
	         	   return listxnVo;
			}
			
			
          	/*实验单数据*/
		   sydData = new ShrySydData();
    		sydData.setLxdh(lxdh);
			sydData.setFyxh(fyxh);
			sydData.setZcxh(zcxh);
			sydData.setSyry(syry);
			sydData.setYps(yps);
			
			sydData.setFyzj(fyzj);
			sydData.setFjxs(fjxs);
			sydData.setSply(sply);
			sydData.setSymd(symd);
			sydData.setSyrq(StringDateUtil.stringToDate(syrq, 3));
			sydData.setMemo(bz);
		    sydData.setSydx("总成");
		    
		    sydData.setSyfd(syfd);
		    sydData.setCkmj(ckmj);
		    sydData.setSyfs(syfs);
		    sydData.setDqy(dqy);
		    
		    sydData.setSymd(symd);
		    sydData.setKqwd(kqwd);
		    sydData.setXdsd(xdsd);
		    sydData.setSkqbz(skqbz);
		    
		    
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
