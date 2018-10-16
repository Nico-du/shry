package net.chinanets.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
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
import net.chinanets.pojos.ListDjxnVo;
import net.chinanets.pojos.ShryDjData;
import net.chinanets.pojos.ShryDjxnData;
import net.chinanets.pojos.ShrySydDjData;
import net.chinanets.service.CommonService;
import net.chinanets.utils.CommonMethods;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.mortbay.log.Log;
import org.springframework.context.ApplicationContext;


public class DJXNInfoImportServlet extends HttpServlet {
	/**
	 * @作用描述：电机参数导入
	 * @author: dzj
	 * @date:2018-7-17上午10:24:33
	 */
	private static final long serialVersionUID = 1L;
	public String str;
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
					//电机参数导入  sheet1：有刷 sheet2:无刷
					ListDjxnVo  listVo = parseExcel(opName);

					if(listVo.getResult()==null && listVo.getDjxnList()!=null && !listVo.getDjxnList().isEmpty()) {
						Long djid = -1L;
						ServletContext servletContext = request.getSession().getServletContext();
						ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils.getWebApplicationContext(servletContext);
						CommonService comService = (CommonService) app.getBean("commonService");
						//根据电机型号查询电机数据表。判断该电机型号是已经存在,如果不存在则给出提示，
						String hql = "From ShryDjData where djxh='"+listVo.getSydData().getMotor()+"'";
						List<ShryDjData> djs = comService.getAllObjectByHql(hql);
						if(djs == null || djs.size()<1){
							str ="该电机型号数据库中不存在，请检查型号为："+listVo.getSydData().getMotor()+"的数据";
							listVo.setResult(str);
							return;
						}else{
							djid = djs.get(0).getDjid();
						}
						//有刷:Project + DateTime唯一 校验，无刷Project校验
						hql = " From ShrySydDjData where djid='"+djid;
						if("Y".equals(listVo.getSydData().getIsysdj())){ hql += "' and datetime='"+sdf.format(listVo.getSydData().getDatetime())+"' ";}
						List<ShrySydDjData> syds = comService.getAllObjectByHql(hql);
						if(syds != null && syds.size() > 0){
							str ="该Excel中Project+DateTime存在重复值,请检查型号:"+listVo.getSydData().getMotor();
							listVo.setResult(str);
							return;
						}


						listVo.getSydData().setInputdate(new Date());
						listVo.getSydData().setInputuser(importuser);
						listVo.getSydData().setUpdatedate(new Date());
						listVo.getSydData().setUpdateuser(importuser);
						listVo.getSydData().setDjid(djid);
						Long sydId = comService.saveObject(listVo.getSydData());
						if(djid == null || djid <1 || sydId == null || sydId <1){
							throw new Exception("sydId或djid必须大于1!");
						}
						//保存
						for (int i = 0; i <listVo.getDjxnList().size(); i++) {
							listVo.getDjxnList().get(i).setInputdate(new Date());
							listVo.getDjxnList().get(i).setInputuser(importuser);
							listVo.getDjxnList().get(i).setUpdatedate(new Date());
							listVo.getDjxnList().get(i).setUpdateuser(importuser);
							listVo.getDjxnList().get(i).setLxdid(sydId);
							listVo.getDjxnList().get(i).setDjid(djid);
							comService.saveObject(listVo.getDjxnList().get(i));
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

	
	    // 解析excel文件转换成对象 //电机参数导入  sheet1：有刷 sheet2:无刷
	//校验格式正确
		private  ListDjxnVo parseExcel(String fileName) {
			        ListDjxnVo  vo  = null;
					FileInputStream input = null;
					Workbook wbk = null;
					
					String name = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
					if(!"xls".equals(name)){	
						vo = new ListDjxnVo();
						vo.setResult("文件格式不正确，请重新选择xls后缀的文件！");
						return vo;
					}
					try {
						input = new FileInputStream(fileName);
						wbk = Workbook.getWorkbook(input);// 创建工作面板
						Sheet st1 = wbk.getSheet(0); // 获取excel中的第1个sheet 有刷
						List<ShryDjxnData> djxnList=  new ArrayList<ShryDjxnData>();
						ShrySydDjData sydData = new ShrySydDjData();
						int clos = st1.getColumns();// 得到所有的列
						int rows = (st1.getRows() > 40 ? 40 : st1.getRows());// 得到所有的行
						//判断导入模版类型:有刷/无刷
						String nameYs = st1.getCell(1, 5).getContents().trim(); 
						String nameWs = st1.getCell(0, 0).getContents().trim();
						boolean isYsdj = false;
						if("Project".equals(nameYs)){
							isYsdj = true;
						}else if("Project".equals(nameWs)){
							isYsdj = false;
						}else{
							vo = new ListDjxnVo();
							vo.setResult("文件格式不正确，请重新选择xls后缀的文件！");
							return vo;
						}
						
						/*=========有刷=======*/
						if(isYsdj){
						vo =  new  ListDjxnVo();
						/**
						 * Cell getCell(int column, int row) 注意第一个是列数 第二个是行数
						 */
						//对参数模版格式进行校验
						String name2 = st1.getCell(1, 5).getContents().trim(); 
						String name3 = st1.getCell(1, 6).getContents().trim(); 
						String name4 = st1.getCell(1, 9).getContents().trim(); 
						String name11 = st1.getCell(2, 9).getContents().trim()+""; 
						String name12 = st1.getCell(3, 9).getContents().trim()+""; 
						String name13 = st1.getCell(14, 1).getContents().trim()+""; 
						String name14 = st1.getCell(14, 6).getContents().trim()+""; 
						String name15 = st1.getCell(10, 9).getContents().trim()+""; 
						if( !"Project".equals(name2) || !"Motor".equals(name3)|| !"Data".equals(name4)
								|| !name11.contains("Input") || !name12.contains("Torque") || !name13.contains("Date:")
								|| !name14.contains("Voltage:") || !name15.contains("Eff.")){
							vo.setResult("Sheet1有刷电机性能模版格式不正确！请检查导入模版");
							return vo;
						}
						String valPrj = st1.getCell(2, 5).getContents().trim(); //必传字段
						String valMt = st1.getCell(2, 6).getContents().trim(); 
						if(StringUtils.isBlank(valPrj) ||StringUtils.isBlank(valMt)){
							vo.setResult("Sheet1有刷电机性能数据Project和Motor不能为空！请检查导入数据");
							return vo;
						}
						
						ShryDjxnData djxnData;
						this.setYsdjSydData(st1,sydData);
						
						//有刷校验
						for (int i = 10; i < rows; i++) {
								//过滤掉为空的行
							 if(StringUtils.isNotBlank(st1.getCell(4,i).getContents())){
								 djxnData = new ShryDjxnData();
								 String clmNo = getSheetCell(st1, 1,i); 
								if(StringUtils.isBlank(clmNo) || !NumberUtils.isNumber(clmNo)){ continue;}
								
								/*电机对象数据*/
								String clm2 = getSheetCell(st1, 3,i); 
								String clm3 = getSheetCell(st1, 5,i); 
								String clm4 = getSheetCell(st1, 6,i); 
								String clm5 = getSheetCell(st1, 9,i); 
								String clm6 = getSheetCell(st1, 10,i); 
								
								if(CommonMethods.isBlank(clm2) ){
									vo.setResult("第"+(i+1)+"行Torque为空!");
									return vo;
								} 
								if(CommonMethods.isBlank(clm3)){
									vo.setResult("第"+(i+1)+"行Speed为空!");
									return  vo;
								}
								if(CommonMethods.isBlank(clm4)){
									vo.setResult("第"+(i+1)+"行Voltage为空!");
									return  vo;
								}
								if(CommonMethods.isBlank(clm5)){
									vo.setResult("第"+(i+1)+"行Pow. Out为空!");
									return  vo;
								}
								if(CommonMethods.isBlank(clm6)){
									vo.setResult("第"+(i+1)+"行Eff.为空!");
									return  vo;
								}
									
								this.setYsDjxnData(st1, i, djxnData);
								djxnList.add(djxnData);
								}
							}
						/*=========无刷=======*/
						}else{
							vo =  new  ListDjxnVo(); 
							/**
							 * Cell getCell(int column, int row) 注意第一个是列数 第二个是行数
							 */
							//对参数模版格式进行校验
							String namePj = st1.getCell(0, 0).getContents().trim(); 
							String nameMt = st1.getCell(0, 1).getContents().trim(); 
							String nameData = st1.getCell(1, 4).getContents().trim(); 
							String name2 = st1.getCell(2, 3).getContents().trim(); 
							String name3 = st1.getCell(3, 3).getContents().trim(); 
							String name4 = st1.getCell(6, 3).getContents().trim(); 
							String name5 = st1.getCell(7, 3).getContents().trim(); 
							String name6 = st1.getCell(8, 3).getContents().trim(); 
							String name11 =st1.getCell(5, 5).getContents().trim()+""; 
							String name12 =st1.getCell(9, 5).getContents().trim()+""; 
							String name13 =st1.getCell(11, 5).getContents().trim()+""; 
							if( !"Project".equals(namePj) ||!"Motor".equals(nameMt) ||!"Data".equals(nameData) || !"Current".equals(name2) 
									|| !"Voltage".equals(name3)|| !"Eff.".equals(name4)|| !"Speed".equals(name5)|| !"Torque".equals(name6)
									|| !name11.contains("PF") || !name12.contains("Horsepower") || !name13.contains("Time")){
								vo.setResult("Sheet1无刷电机参数模版格式不正确！请检查参数导入模版(检查方式：1.只支持2003.xls格式版本,2.下载模版对比表头)");
								return vo;
							}
							String valPj = st1.getCell(1, 0).getContents().trim(); 
							String valMt = st1.getCell(1, 1).getContents().trim(); 
							if( StringUtils.isBlank(valPj) || StringUtils.isBlank(valMt)){
								vo.setResult("Sheet1无刷电机Project和Motor不能为空！请检查参数导入数据");
								return vo;
							}
							
							
							ShryDjxnData djxnData; 
							sydData.setProject(valPj);
							sydData.setMotor(valMt);
							sydData.setIsysdj("N");
							sydData.setDatetime(new Date());
							//有刷校验
							for (int i = 6; i < rows; i++) {
								//过滤掉为空的行
								djxnData = new ShryDjxnData();
								String dataNo = st1.getCell(1,i).getContents();
								if(StringUtils.isBlank(dataNo) || !NumberUtils.isNumber(dataNo)){ continue;}

								/*电机性能对象数据*/
								String clm2 = getSheetCell(st1, 2,i); 
								String clm3 = getSheetCell(st1, 3,i); 
								String clm6 = getSheetCell(st1, 6,i); 
								String clm7 = getSheetCell(st1, 7,i); 
								String clm8 = getSheetCell(st1, 8,i); 

								if(CommonMethods.isBlank(clm2)){  vo.setResult("第"+(i+1)+"行Current为空!");	return vo;	} 
								if(CommonMethods.isBlank(clm3)){  vo.setResult("第"+(i+1)+"行Voltage为空!"); return  vo;		}
								if(CommonMethods.isBlank(clm6)){  vo.setResult("第"+(i+1)+"行Eff.为空!");return  vo;  }
								if(CommonMethods.isBlank(clm7)){  vo.setResult("第"+(i+1)+"行Speed为空!");return  vo;  }
								if(CommonMethods.isBlank(clm8)){  vo.setResult("第"+(i+1)+"行Torque为空!");return  vo;  }

								this.setWsDjxnData(st1, i, djxnData);
								djxnList.add(djxnData);
							}
							}
						
						vo.setDjxnList(djxnList);
						vo.setSydData(sydData);
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
		
		private void setWsDjxnData(Sheet st, int i, ShryDjxnData djxnData) {
//			djxnData.setDjxnid       (getSheetCell(st, x,i));
//			djxnData.setDjid         (getSheetCell(st, x,i));
//			djxnData.setLxdid        (getSheetCell(st, x,i));
			djxnData.setData         (getSheetCell(st, 1,i));
			djxnData.setCurrent      (getSheetCell(st, 2,i));
			djxnData.setVoltage      (getSheetCell(st, 3,i));
			djxnData.setPowIn        (getSheetCell(st, 4,i));
			djxnData.setWsPf1         (getSheetCell(st, 5,i));
			djxnData.setEff          (getSheetCell(st, 6,i));
			djxnData.setSpeed        (getSheetCell(st, 7,i));
			djxnData.setTorqueNm     (getSheetCell(st, 8,i));
			djxnData.setWsHorsepower         (getSheetCell(st, 9,i));
			djxnData.setPowOut       (getSheetCell(st, 10,i));
			djxnData.setWsTime       (getSheetCell(st, 11,i));
			djxnData.setWsDirection         (getSheetCell(st, 12,i));
			
//			djxnData.setInput        (getSheetCell(st, 2,i));
//			djxnData.setTorqueOzIn   (getSheetCell(st, 4,i));
			
			djxnData.setInputdate    (new Date());
			djxnData.setInputuser    ("1");
			djxnData.setUpdatedate   (new Date());
			djxnData.setUpdateuser   ("1");
			
		}

		private void setYsDjxnData(Sheet st, int i, ShryDjxnData djxnData) {
//			djxnData.setDjxnid       (getSheetCell(st, x,i));
//			djxnData.setDjid         (getSheetCell(st, x,i));
//			djxnData.setLxdid        (getSheetCell(st, x,i));
			djxnData.setData         (getSheetCell(st, 1,i));
			djxnData.setInput        (getSheetCell(st, 2,i));
			djxnData.setTorqueNm     (getSheetCell(st, 3,i));
			djxnData.setTorqueOzIn   (getSheetCell(st, 4,i));
			djxnData.setSpeed        (getSheetCell(st, 5,i));
			djxnData.setVoltage      (getSheetCell(st, 6,i));
			djxnData.setCurrent      (getSheetCell(st, 7,i));
			djxnData.setPowIn        (getSheetCell(st, 8,i));
			djxnData.setPowOut       (getSheetCell(st, 9,i));
			djxnData.setEff          (getSheetCell(st, 10,i));
			
			djxnData.setInputdate    (new Date());
			djxnData.setInputuser    ("1");
			djxnData.setUpdatedate   (new Date());
			djxnData.setUpdateuser   ("1");
		}

		private void setYsdjSydData(Sheet st, ShrySydDjData sydData) throws ParseException {
			SimpleDateFormat sdf= new SimpleDateFormat("yy-MM-dd hh:mm:ss a");
			String dateStr = getSheetCell(st, 15,1);
			String timeStr = getSheetCell(st, 15,2);
			
			sydData.setIsysdj              ("Y");
			sydData.setProject             (getSheetCell(st, 2,5));
			sydData.setMotor               (getSheetCell(st, 2,6));
			sydData.setDatetime(sdf.parse(dateStr+" "+timeStr));
			sydData.setOperator            (getSheetCell(st, 15,3));
			sydData.setTestRef             (getSheetCell(st, 6,5));
			sydData.setMotorRef            (getSheetCell(st, 6,6));
			sydData.setDirection           (getSheetCell(st, 10,5));
			sydData.setWinding             (getSheetCell(st, 10,6));
			sydData.setSof                 (getSheetCell(st, 10,7));
			
			sydData.setVoltage             (getSheetCell(st, 15,6));
			sydData.setSimulationRes       (getSheetCell(st, 15,7));
			sydData.setHardwareRes         (getSheetCell(st, 15,8));
			sydData.setNlcMeasuredSpeed    (getSheetCell(st, 15,12));
			sydData.setNlcMeasuredCurrent  (getSheetCell(st, 15,13));
			sydData.setNlcCalculatedSpeed  (getSheetCell(st, 15,14));
			sydData.setNlcCalculatedCurrent(getSheetCell(st, 15,15));
			sydData.setScTorque            (getSheetCell(st, 15,18));
			sydData.setScCurrent           (getSheetCell(st, 15,19));
			sydData.setMpcTorque           (getSheetCell(st, 15,22));
			sydData.setMpcSpeed            (getSheetCell(st, 15,23));
			sydData.setMpcCurrent          (getSheetCell(st, 15,24));
			sydData.setMpcPowOut           (getSheetCell(st, 15,25));
			sydData.setMpcEfficiency       (getSheetCell(st, 15,26));
			sydData.setMecTorque           (getSheetCell(st, 15,29));
			
			sydData.setMecSpeed            (getSheetCell(st, 15,30));
			sydData.setMecCurrent          (getSheetCell(st, 15,31));
			sydData.setMecPowOut           (getSheetCell(st, 15,32));
			sydData.setMecEfficiency       (getSheetCell(st, 15,33));
			sydData.setMcTorqueConstant    (getSheetCell(st, 15,37));
			sydData.setMcDynamicResistance (getSheetCell(st, 15,38));
			sydData.setMcMotorRegulation   (getSheetCell(st, 15,39));
			sydData.setMemo                (getSheetCell(st, 2,41));
		}

		
		private String getSheetCell(Sheet st,int x,int y){
			return st.getCell(x, y).getContents().trim();
		}
}