package net.chinanets.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
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
import net.chinanets.pojos.ShryZcJsyqData;
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
	 * 电机数据导入，先找是否存在，存在获取djid保存;不存在做新增，只insert djxh，在电机数据导入时判断 isysdj(是否有刷电机)字段来判断是否覆盖已有数据。
	 * @author: 徐超
	 * @date:2016-3-17下午10:24:33
	 */
	private static final long serialVersionUID = 1L;
	private static final String SUCCESS = "success";
	public SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/* 是否技术要求导入 */
	boolean isJsyqImp = false;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
               this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String outPutMsg ="导入失败：";
		try {
			String importuser = request.getParameter("importuser");
			//上传类型 FY/ZC/DJ
//			String updType = request.getParameter("updType");
//			if(StringUtils.isBlank(updType)){doResponse(response, outPutMsg+"上传参数异常:updType为空"); return; }
			
			// 获取上传文件流，写入到服务器文件
			List<String> uploadFileList = readUploadFile(request);
			if(uploadFileList == null || uploadFileList.size() < 1){ doResponse(response, outPutMsg+"读取上传文件异常"); return;}
			//单文件上传
			String opName = uploadFileList.get(0);
			if(StringUtils.isBlank(opName)){  doResponse(response, outPutMsg+"读取上传文件异常"); return;}
			
			ServletContext servletContext = request.getSession().getServletContext();
			ApplicationContext app = org.springframework.web.context.support.WebApplicationContextUtils
					.getWebApplicationContext(servletContext);
			CommonService comService = (CommonService) app.getBean("commonService");
			
			//Step 1: 读取上传文件/解析/初步校验
		    ListVo  listVo = parseExcel(opName);
		    //读取/校验失败,返回错误信息
		   if (StringUtils.isNotBlank(listVo.getResult()) || listVo.getFyDataList() == null) {
			   outPutMsg=listVo.getResult().toString();
				doResponse(response, outPutMsg);
				return;
			}
		   
		   this.isJsyqImp(listVo);
		   
		   //Step 2:数据准备,数据校验
		   outPutMsg = doPrepareData(comService, listVo);
		   if(!SUCCESS.equals(outPutMsg)){ doResponse(response, outPutMsg);	return;}
		   //Step 3:保存数据库
		   outPutMsg = doSaveAllData(comService, listVo,importuser);
		   if(!SUCCESS.equals(outPutMsg)){ doResponse(response, outPutMsg);	return;}
		   
		   //Step 4:失败回滚,只回滚总成数据(风叶/电机数据直接使用已存在数据)
			//暂不处理,出现保存出错的情况视为代码bug
				   
			
		} catch (Throwable e) {
			e.printStackTrace();
			doResponse(response, outPutMsg+"系统异常，执行保存时报错，请联系系统管理员。\n"+e.getMessage()+"\n"+e.getStackTrace());
			return;
		}
		doResponse(response, "导入成功！");
	}

	
	//判断是否技术要求数据导入:
	//以风扇型号、风叶型号为校验字段，判断总成其他参数字段为空时，走技术要求字段导入，只上传技术要求，并且覆盖 电机型号 字段。否则会校验是否已存在数据、已存在则不能导入。
	private void isJsyqImp(ListVo listVo){
		if(listVo == null || listVo.getFyDataList() == null || listVo.getFyDataList().isEmpty()){return;}
		ShryFyData chFy = listVo.getFyDataList().get(0);
		ShryZcData chZc = listVo.getZcDataList().get(0);
		if(StringUtils.isBlank(chFy.getDlhzj()) &&
				StringUtils.isBlank(chFy.getFbzj() ) &&
				StringUtils.isBlank(chFy.getJqfs() ) &&
				StringUtils.isBlank(chFy.getLggd() ) &&
				StringUtils.isBlank(chFy.getLgzj() ) &&
				StringUtils.isBlank(chFy.getQj()   ) &&
				StringUtils.isBlank(chFy.getYpsm() ) &&
				StringUtils.isBlank(chFy.getZl()   ) &&
				StringUtils.isBlank(chFy.getCl()   ) &&
				StringUtils.isBlank(chFy.getClbz() ) &&
				StringUtils.isBlank(chZc.getWxcc() ) &&
				StringUtils.isBlank(chZc.getSycx() ) &&
				StringUtils.isBlank(chZc.getJqfs() )){
			this.isJsyqImp = true;
		}else{
			this.isJsyqImp = false;
		}
	}
	
	/**
	 * 数据准备,数据校验
	 * 1.总成数据校验：总成型号是否已存在
	 * @param comService
	 * @param listVo
	 * @return
	 */
	private String doPrepareData(CommonService comService,ListVo listVo){
		String validateResult = "导入失败：";
		if(this.isJsyqImp){validateResult = "技术要求导入-"+validateResult;}
		String sql; 
		for (int i = 0; i <listVo.getFyDataList().size(); i++) {
			if(!this.isJsyqImp){
				sql = " select count(0) from shry_zc_data where  xh='"+listVo.getZcDataList().get(i).getXh()+"' ";
				int djCount = comService.getOneBysql(sql);
				if(djCount>0){
					validateResult +=  "\n该总成型号："+listVo.getZcDataList().get(i).getXh()+"的参数数据已经存在！";
				}
				if(StringUtils.isBlank(listVo.getFyDataList().get(i).getJqfs())){
					validateResult += "\n第"+(i+4)+"行[风叶型式]不能为空！";
				}
			}else{//技术要求导入 必须总成已经导入过 补充技术要求数据
				if(!"无刷".equals(listVo.getZcjsyqList().get(i).getWhkcKzfs()) && !"有刷".equals(listVo.getZcjsyqList().get(i).getWhkcKzfs()) 
						&& !"有刷PWM".equals(listVo.getZcjsyqList().get(i).getWhkcKzfs())){
					validateResult += "\n第"+(i+4)+"行技术要求-控制方式必须是[无刷/有刷/有刷PWM]中一个！";
				}
				sql = " select count(0) from shry_zc_data where  xh='"+listVo.getZcDataList().get(i).getXh()+"' ";
				int djCount = comService.getOneBysql(sql);
				if(djCount<1){
					validateResult +=  "\n该总成型号："+listVo.getZcDataList().get(i).getXh()+"的参数数据不存在,不能直接导入技术要求数据！";
				}
				
				sql = " select count(0) from shry_fy_data where  xh='"+listVo.getFyDataList().get(i).getXh()+"' ";
				djCount = comService.getOneBysql(sql);
				if(djCount<1){
					validateResult +=  "\n该风叶型号："+listVo.getZcDataList().get(i).getXh()+"的参数数据不存在,不能直接导入技术要求数据！";
				}
			}
		}
		if(validateResult.length() < 15){ validateResult = SUCCESS;}

		return validateResult;
	}
	
	
	
	/**
	 * 保存总成/风叶/电机数据
	 * 1.风叶数据如果已存在,直接使用
	 * 2.电机数据如果已存在,直接使用
	 * @param listVo
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	private String doSaveAllData(CommonService comService,ListVo listVo,String importuser) throws IllegalAccessException, InvocationTargetException{
		List<Long> savedZcList = new ArrayList<Long>();
		
		List<ShryFyData> fydataList; 
		List<ShryDjData> djdataList;
		List<ShryZcData> zcdataList;
		List<ShryZcJsyqData> zcjsyqList; 
		Long lFyid,lDjid,lZcid; lZcid = null;
		for (int i = 0; i <listVo.getFyDataList().size(); i++) {
			//根据风叶型号查询风叶数据表。判断该风叶型号是已经存在，如果已经存在则在该风叶型号前面加1#，若再有重复一次递增
//			   int fyCount = comService.getCountShryFy(listVo.getFyDataList().get(i).getXh());
//			   if(fyCount>0){
//				   listVo.getFyDataList().get(i).setXh(i+"#"+listVo.getFyDataList().get(i).getXh());
//			   }
		   listVo.getDjDataList().get(i).setInputdate(new Date());
		   listVo.getDjDataList().get(i).setInputuser(importuser);
			
		   listVo.getFyDataList().get(i).setInputdate(new Date());
		   listVo.getFyDataList().get(i).setInputuser(importuser);
		   
		   if(this.isJsyqImp){
			   zcdataList= comService.getObjectList(new ShryZcData()," xh='"+listVo.getZcDataList().get(i).getXh()+"' order by inputdate desc limit 1 ");
			   lZcid = zcdataList.get(0).getZcid();
			   listVo.getZcDataList().set(i, zcdataList.get(0));
			   listVo.getZcDataList().get(i).setUpdatedate(new Date());
			   listVo.getZcDataList().get(i).setUpdateuser(importuser);
		   }else{
			   listVo.getZcDataList().get(i).setUpdatedate(new Date());
			   listVo.getZcDataList().get(i).setUpdateuser(importuser);
			   listVo.getZcDataList().get(i).setInputdate(new Date());
			   listVo.getZcDataList().get(i).setInputuser(importuser);
		   }
			
			fydataList = comService.getObjectList(new ShryFyData(), " xh='"+listVo.getFyDataList().get(i).getXh()+"' order by inputdate desc limit 1 ");
			
			if(fydataList != null && fydataList.size() > 0){
				lFyid = fydataList.get(0).getFyid();
			}else{
			/* 保存风叶对象后返回的id，用于赋值给总成表中的风叶id */
				lFyid = comService.saveObject(listVo.getFyDataList().get(i));
			}
			
		   //保存电机数据表，并将电机id返回给总成表中的电机id
			   
			   //判断电机数据是否已经存在
//			   String sql = "select count(*) from shry_dj_data where  xh='"+listVo.getDjDataList().get(i).getXh()+"'";
//			   int djCount = comService.getOneBysql(sql);
//			   if(djCount>0){
//				   str = "该电机型号："+listVo.getDjDataList().get(i).getXh()+"的数据已经存在！";
//			   }
//			   //保存电机数据
//			   Long  djId = comService.saveObject( listVo.getDjDataList().get(i));
			   
			   djdataList = comService.getObjectList(new ShryDjData(), " djxh='"+listVo.getDjDataList().get(i).getDjxh()+"' order by inputdate desc limit 1 ");
				if(djdataList != null && djdataList.size() > 0){
					lDjid = djdataList.get(0).getDjid();
				}else{
				/* 保存风叶对象后返回的id，用于赋值给总成表中的电机id */
					lDjid = comService.saveObject(listVo.getDjDataList().get(i));
				}
				
			   listVo.getZcDataList().get(i).setFyid(lFyid);
			   //保存总成数据
			   listVo.getZcDataList().get(i).setDjid(String.valueOf(lDjid));
			   if(this.isJsyqImp){
				  comService.updateObject(listVo.getZcDataList().get(i));
			   }else{
				   lZcid = comService.saveObject(listVo.getZcDataList().get(i));
			   }
			   //在第一条数据报错失败时立即返回
			   if(lZcid == null ){ return "执行数据保存时报错";}
			   listVo.getZcjsyqList().get(i).setRfZcid(lZcid);

			   zcjsyqList = comService.getObjectList(new ShryZcJsyqData(), " rf_Zcid='"+listVo.getZcjsyqList().get(i).getRfZcid()+"' order by jsyqid desc limit 1 ");
				if(zcjsyqList == null || zcjsyqList.isEmpty()){
					comService.saveObject(listVo.getZcjsyqList().get(i));
				}else{
					listVo.getZcjsyqList().get(i).setJsyqid(zcjsyqList.get(0).getJsyqid());
					org.springframework.beans.BeanUtils.copyProperties(listVo.getZcjsyqList().get(i), zcjsyqList.get(0));
					comService.updateObject(zcjsyqList.get(0));
				}
			   
			   savedZcList.add(lZcid);
			   
		}
		
		return SUCCESS;
	}
	
	

	    // 解析excel文件转换成对象
	//以风扇型号、风叶型号为校验字段，判断总成其他参数字段为空时，走技术要求字段导入，只上传技术要求，并且覆盖 电机型号 字段。否则会校验是否已存在数据、已存在则不能导入。
		private  ListVo parseExcel(String fileName) {
			       ListVo  vo  = null;
					FileInputStream input = null;
					Workbook wbk = null;
					ShryFyData fyData;
					ShryZcData zcData;
					ShryZcJsyqData zcjsyqData;
					ShryDjData djData;
					List<ShryFyData> fyList=  new ArrayList<ShryFyData>();
					List<ShryZcData> zcList=  new ArrayList<ShryZcData>();
					List<ShryZcJsyqData>  zcjsyqList = new ArrayList<ShryZcJsyqData>();
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
						 * 因excel中数据包含4张表的数据，故针对不同表分别导入（总成数据表、风叶数据表、电机数据表（暂不考虑））
						 * Cell getCell(int column, int row) 注意第一个是列数 第二个是行数
						 */
							for (int i = 1; i < rows; i++) {
								//过滤掉为空的行
								if(StringUtils.isNotBlank(st.getCell(0,i).getContents())){
									fyData = new ShryFyData();
									zcData = new ShryZcData();
									zcjsyqData = new ShryZcJsyqData();
									djData = new ShryDjData();
									
									//对参数模版格式进行校验
									String djxhName = st.getCell(1, 0).getContents().trim();
									String fsccName = st.getCell(2, 0).getContents().trim();
									String fyxhName = st.getCell(3,0).getContents().trim();
									if( !"电机型号".equals(djxhName) || !"风扇尺寸(mm)".equals(fsccName) || !"风叶型号".equals(fyxhName)){
										vo.setResult("参数模版格式不正确！请检查参数导入模版(检查方式：1.只支持2003.xls格式版本,2.下载模版对比表头)");
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
									String sycx = st.getCell(15, i).getContents().trim();//适用车型
						           /*电机对象数据*/
									String djxh = st.getCell(1, i).getContents().trim(); //电机型号
								   /*技术要求对象数据*/
									String whkc_kzfs = st.getCell(16, i).getContents().trim();// 控制方式
									String whkc_zkb1 = st.getCell(17, i).getContents().trim();//占空比1（%）  
									String whkc_zs1  = st.getCell(18, i).getContents().trim();//转速1（rpm)   
									String whkc_dl1  = st.getCell(19, i).getContents().trim();//电流1(A)      
									String whkc_zkb2 = st.getCell(20, i).getContents().trim();//占空比2（%）  
									String whkc_zs2  = st.getCell(21, i).getContents().trim();//转速2（rpm)   
									String whkc_dl2  = st.getCell(22, i).getContents().trim();//电流2(A)      
									String whkc_zkb3 = st.getCell(23, i).getContents().trim();//占空比3（%）  
									String whkc_zs3  = st.getCell(24, i).getContents().trim();//转速3（rpm)   
									String whkc_dl3  = st.getCell(25, i).getContents().trim();//电流3(A)      
									String gzd_jy1   = st.getCell(26, i).getContents().trim();//静压1（Pa)    
									String gzd_ll1   = st.getCell(27, i).getContents().trim();//流量1（m3/h)  
									String gzd_jy2   = st.getCell(28, i).getContents().trim();//静压2（Pa)    
									String gzd_ll2   = st.getCell(29, i).getContents().trim();//流量2（m3/h)  
									String gzd_jy3   = st.getCell(30, i).getContents().trim();//静压3（Pa)    
									String gzd_ll3   = st.getCell(31, i).getContents().trim();//流量3（m3/h)  
									String zs_zt     = st.getCell(32, i).getContents().trim();//状态          
									String zs_gs_ed  = st.getCell(33, i).getContents().trim();//额定（dB）    
									String zs_gs_sc  = st.getCell(34, i).getContents().trim();//实测（dB）    
									String zs_ds_ed  = st.getCell(35, i).getContents().trim();//额定（dB）    
									String zs_ds_sc  = st.getCell(36, i).getContents().trim();//实测（dB）    
									String ph_jph    = st.getCell(37, i).getContents().trim();//静平衡（g.mm) 
									String ph_dph    = st.getCell(38, i).getContents().trim();//动平衡（g.mm)  
									
									
									if(CommonMethods.isBlank(xh) ){
										vo.setResult("第"+(i+1)+"行风叶型号为空!");
										return vo;
									} 
									if(CommonMethods.isBlank(zcxh) ){
										vo.setResult("第"+(i+1)+"行总成型号为空!");
										return vo;
									} 
							/*		if(CommonMethods.isBlank(fbzj)&& !CommonMethods.isDouble(fbzj)){
									 vo.setResult("第"+(i+1)+"行翻边外径为空或者不是数值类型");
					        		 return vo;
					        	 }
					       	      if(CommonMethods.isBlank(dlhzj)&& !CommonMethods.isDouble(dlhzj)){
					        		vo.setResult("第"+(i+1)+"行导环流外径为空或者不是数值类型");
					        		 return vo;
					        	 }
					       	/* if(CommonMethods.isBlank(lgzj)&& !CommonMethods.isDouble(lgzj)){
					        		vo.setResult("第"+(i+1)+"行轮毂直径为空或者不是数值类型");
					        		 return vo;
					        	 }
					        	 if(CommonMethods.isBlank(lggd)&& !CommonMethods.isDouble(lggd)){
					        		vo.setResult("第"+(i+1)+"行轮毂高度为空或者不是数值类型");
					        		 return vo;
					        	 }
					        	 if(CommonMethods.isBlank(zl)&& !CommonMethods.isDouble(zl)){
					        		vo.setResult("第"+(i+1)+"行风叶重量为空或者不是数值类型");
					        		 return vo;
					        	 }
					        	 if(CommonMethods.isBlank(ypsm)&& !CommonMethods.isDouble(ypsm)){
					        		 vo.setResult("第"+(i+1)+"行叶片数为空或者不是数值类型");
					        		 return vo;
					        	 }*/
//									if(CommonMethods.isBlank(jqfs) ){
//										vo.setResult("第"+(i+1)+"行风叶型式为空!");
//										return  vo;
//									}
//									if(CommonMethods.isBlank(cl) ){
//										vo.setResult("第"+(i+1)+"行材料为空!");
//										return  vo;
//									}
									/* if(CommonMethods.isBlank(clbz) ){
					        		vo.setResult("第"+(i+1)+"行材料标准为空!");
					        		 return  vo;
					        	 }
					        	 if(CommonMethods.isBlank(qj) ){
					        		 vo.setResult("第"+(i+1)+"行嵌件为空!");
					        		 return  vo;
					        	 }
					        	 
					        	  if(CommonMethods.isBlank(zcxh) ){
					        		 vo.setResult("第"+(i+1)+"行总成型号为空!");
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
									fyData.setJqfs(jqfs);
									fyData.setLggd(lggd);
									fyData.setLgzj(lgzj);
									fyData.setQj(qj);
									fyData.setXh(xh);
									fyData.setYpsm(ypsm);
									fyData.setZl(zl);
									fyData.setCl(cl);
									fyData.setClbz(clbz);
									
									zcData.setXh(zcxh);
									zcData.setWxcc(zccc);
									zcData.setSycx(sycx);
									zcData.setJqfs(jqfs);
									
									djData.setDjxh(djxh);
									
									zcjsyqData.setWhkcKzfs(whkc_kzfs);
									zcjsyqData.setWhkcZkb1(whkc_zkb1);
									zcjsyqData.setWhkcZs1 (whkc_zs1 );
									zcjsyqData.setWhkcDl1 (whkc_dl1 );
									zcjsyqData.setWhkcZkb2(whkc_zkb2);
									zcjsyqData.setWhkcZs2 (whkc_zs2 );
									zcjsyqData.setWhkcDl2 (whkc_dl2 );
									zcjsyqData.setWhkcZkb3(whkc_zkb3);
									zcjsyqData.setWhkcZs3 (whkc_zs3 );
									zcjsyqData.setWhkcDl3 (whkc_dl3 );
									zcjsyqData.setGzdJy1  (gzd_jy1  );
									zcjsyqData.setGzdLl1  (gzd_ll1  );
									zcjsyqData.setGzdJy2  (gzd_jy2  );
									zcjsyqData.setGzdLl2  (gzd_ll2  );
									zcjsyqData.setGzdJy3  (gzd_jy3  );
									zcjsyqData.setGzdLl3  (gzd_ll3  );
									zcjsyqData.setZsZt    (zs_zt    );
									zcjsyqData.setZsGsEd (zs_gs_ed );
									zcjsyqData.setZsGsSc (zs_gs_sc );
									zcjsyqData.setZsDsEd (zs_ds_ed );
									zcjsyqData.setZsDsSc (zs_ds_sc );
									zcjsyqData.setPhJph   (ph_jph   );
									zcjsyqData.setPhDph   (ph_dph   );
									
									fyList.add(fyData);
									zcList.add(zcData);
									djList.add(djData);
									zcjsyqList.add(zcjsyqData);
								}
							}
					    vo.setFyDataList(fyList);
					    vo.setZcDataList(zcList);
					    vo.setDjDataList(djList);
					    vo.setZcjsyqList(zcjsyqList);
						wbk.close();
						input.close();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					return vo;
				}
		
		/**
		 * 获取上传文件流，写入到服务器文件
		 * @param request
		 * @return 文件路径
		 * @throws Exception
		 */
		private List<String> readUploadFile(HttpServletRequest request) throws Exception {
			List<String> fileList = new ArrayList<String>();
			
			DiskFileItemFactory factory = new DiskFileItemFactory();
			String path = request.getRealPath("/");
			factory.setRepository(new File(path)); // 获取临时上传文件夹路径
			factory.setSizeThreshold(1024 * 1024); // 设置上传文件大小临界值
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("utf-8");    //设置编码格式
			upload.setSizeMax(100*1024*1024L); // 限制文件的上传大小100M
			
			//取得servlet传入的对象参数 --多个文件上传
			List list = upload.parseRequest(request);
			String opName = "";
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				FileItem item = (FileItem) iterator.next();
				if (!item.isFormField()) {
					String fileName = item.getName();
					item.write(new File(path, fileName));
					opName = path + fileName;
					fileList.add(opName);
				}
			}
			
			return fileList;
		}

		/**
		 * 响应页面导入结果数据
		 * @param out
		 * @throws IOException 
		 */
		private void doResponse(HttpServletResponse response,String outPutMsg) throws IOException{
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.write(outPutMsg.toString());
		}
		
		
}