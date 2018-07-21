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
import net.chinanets.pojos.ListFyxnVo;
import net.chinanets.pojos.ShryFyData;
import net.chinanets.pojos.ShryFyxnData;
import net.chinanets.pojos.ShrySydData;
import net.chinanets.service.CommonService;
import net.chinanets.utils.CommonMethods;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

public class FYXNExcelUploadServlet extends HttpServlet {

	/**
	 * @作用描述：风叶性能导入 涉及风叶性能表和试验单表，
	 * 先保存实验单数据，产生后的id作为联系单id存入风叶性能表中的联系单id
	 * @author: 徐超
	 * @date:2016-3-24上午12:27:13
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
					ListFyxnVo listFyxnVo = parseExcel(opName);
					Long returnId = null; // 保存实验单返回后的实验单id
					ShryFyData fyData = new ShryFyData();
					if (listFyxnVo.getResult() == null&& listFyxnVo.getFyxnData() != null) {
							// 取试验单的风叶型号，去风叶表中查询风叶信息，取查询记录中的风叶id保存风叶性能表中的风叶id,和实验单中的风叶id，若不存在则给出提示
							List<ShryFyData> listfyData = comService.getInfoById(listFyxnVo.getSydData().getFyxh(), new ShryFyData());
							if (listfyData.size() > 0) {
								if (listfyData.get(0).getFyid() != null) {
									Long fyId = listfyData.get(0).getFyid();
									List<ShrySydData> listFyxn = comService.getFYSydByParam(listFyxnVo.getSydData().getLxdh(), fyId);
									if(listFyxn!=null && !listFyxn.isEmpty()){
										 str = "该风叶性能表中已存在，风叶型号为:"+listFyxnVo.getSydData().getFyxh()+",联系单号为:"+listFyxnVo.getSydData().getLxdid()+"的数据！";
									}
									
									// 保存试验单数据并返回试验单id
									listFyxnVo.getSydData().setInputdate(new Date());
									listFyxnVo.getSydData().setInputuser("1");
									listFyxnVo.getSydData().setUpdatedate(new Date());
									listFyxnVo.getSydData().setUpdateuser("1");
									listFyxnVo.getSydData().setFyid(fyId);
									returnId = comService.saveObject(listFyxnVo.getSydData());
									
									// 保存风叶性能数据
									for (int i = 0; i < listFyxnVo.getFyxnData().size(); i++) {
										listFyxnVo.getFyxnData().get(i).setLxdid(returnId);
										listFyxnVo.getFyxnData().get(i).setFyid(fyId);
										listFyxnVo.getFyxnData().get(i).setUpdatedate(new Date());
										listFyxnVo.getFyxnData().get(i).setUpdateuser("1");
										comService.saveObject(listFyxnVo.getFyxnData().get(i));
									}
									str="导入成功！";
								}	
							}else{
								str ="该风叶型号"+listFyxnVo.getSydData().getFyxh()+"不存在基础数据中，请先导入风叶基础数据";
							}
						}else{
							str=listFyxnVo.getResult().toString();
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
	
	/*解析EXCEL到数据对象中*/
	private ListFyxnVo parseExcel(String opName) {
		FileInputStream input = null;
		Workbook wbk = null;
		ListFyxnVo  listFyxnVo = null;
		ShryFyxnData  fyxnData ;
		ShrySydData  	sydData = new ShrySydData();
		
		String name = opName.substring(opName.lastIndexOf(".")+1,opName.length());
		if(!"xls".equals(name)){
			listFyxnVo = new ListFyxnVo();
			listFyxnVo.setResult("文件格式不正确，请重新选择xls后缀的文件！");
			return listFyxnVo;
		}
		try {
			input = new FileInputStream(opName);
			wbk = Workbook.getWorkbook(input);// 创建工作面板
			Sheet st = wbk.getSheet(0); // 获取excel中的第一个sheet
			int clos = st.getColumns();// 得到所有的列
			int rows = st.getRows();// 得到所有的行
			List<ShryFyxnData> fyxnList = new ArrayList<ShryFyxnData>();
			List<ShrySydData> sydList = new ArrayList<ShrySydData>();
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
			String  fyxs = st.getCell(6, 3).getContents().trim();//风叶型式
			String symd = st.getCell(1,8).getContents().trim();//试验性质
			String memo = st.getCell(1, 11).getContents().trim();//备注 
			
			String syfd = st.getCell(1,4).getContents().trim();//试验风洞:
			String ckmj = st.getCell(7,4).getContents().trim();//出口面积
			String syfs = st.getCell(1,5).getContents().trim();//试验方式
			
			String dqy = st.getCell(7,7).getContents().trim();//大气压
			String kqwd = st.getCell(7,8).getContents().trim();//空气温度(℃)	

			String xdsd = st.getCell(7,9).getContents().trim();//相对湿度(%RH)	
			String skqbz = st.getCell(7,10).getContents().trim();//湿空气比重(kg/m3)	
			
			
			listFyxnVo = new ListFyxnVo();
			if(StringUtils.isBlank(lxdh)){
				listFyxnVo.setResult("联系单号为空(截取“/”后面作为联系单号)！请检查导入模版");
	         	   return listFyxnVo;
			}
			if(StringUtils.isBlank(fyxh)){
				listFyxnVo.setResult("风叶型号为空！请检查导入模版");
	         	   return listFyxnVo;
			}
			
	      	/*实验单数据*/
    		sydData.setLxdh(lxdh);
			sydData.setFyxh(fyxh);
			sydData.setZcxh(zcxh);
			sydData.setSyry(syry);
			sydData.setYps(yps);
			sydData.setFyzj(fyzj);
			sydData.setFjxs(fyxs);
			sydData.setSply(sply);
			sydData.setSydx("风叶");
			sydData.setSymd(symd);
			sydData.setSyrq(StringDateUtil.stringToDate(syrq, 3));
			sydData.setMemo(memo);
			
			sydData.setSyfd(syfd);
		    sydData.setCkmj(ckmj);
		    sydData.setSyfs(syfs);
		    sydData.setDqy(dqy);
		    
		    sydData.setSymd(symd);
		    sydData.setKqwd(kqwd);
		    sydData.setXdsd(xdsd);
		    sydData.setSkqbz(skqbz);
			
			for(int i=15;i<rows;i++){
				fyxnData = new ShryFyxnData();
				listFyxnVo = new ListFyxnVo();
				//过滤到空数据行
				if(StringUtils.isNotBlank(st.getCell(1, 15).getContents().trim())){
			       //对模版标题行校验，判断模版是否是风叶性能导入模版
	               String zzsName = st.getCell(3,13).getContents().trim();
	               String fzsName = st.getCell(4,13).getContents().trim();
	               String zjName = st.getCell(5, 13).getContents().trim();
	               if(!"主转速".equals(zzsName) || !"副转速".equals(fzsName) || !"转矩".equals(zjName)){
	            	   listFyxnVo.setResult("该模版不符合风叶性能导入模版格式！请检查导入模版");
	            	   break;
	               }
	               String  flow = st.getCell(1, i).getContents().trim(); //流量
	               String  jy = st.getCell(2,i).getContents().trim();      //静压
	               String  zzs = st.getCell(3, i).getContents().trim();//主转速
	               String  fzs = st.getCell(4, i).getContents().trim();//辅转速
	               String  zj = st.getCell(5, i).getContents().trim();//转矩
	               String  zgl = st.getCell(6,i).getContents().trim();//轴功率
	               String  xl = st.getCell(7, i).getContents().trim(); //效率
	              
	               //解决 模版中含有下面曲线图导致的行数下标越界，当行里数据都为空，这表示读完，跳出循环
	            if(CommonMethods.isBlank(flow) && CommonMethods.isBlank(jy) &&CommonMethods.isBlank(zzs) 
	            		&&CommonMethods.isBlank(fzs) &&CommonMethods.isBlank(zj)&&CommonMethods.isBlank(zgl)
	            		&&CommonMethods.isBlank(xl)){
	            	break;
	            }
	               /*数据合法性校验*/
	           	if(CommonMethods.isBlank(flow) && !CommonMethods.isDouble(flow)){
	           		listFyxnVo.setResult("第"+i+"行流量为空或者不是数字");
	           		return listFyxnVo;
				} 
	           	if(CommonMethods.isBlank(jy) && !CommonMethods.isDouble(jy)){
	           		listFyxnVo.setResult("第"+i+"行静压为空或者不是数字");
	           		return listFyxnVo;
				} 
	        	if(CommonMethods.isBlank(zzs) && !CommonMethods.isDouble(zzs)){
	        		listFyxnVo.setResult("第"+i+"行主转速为空或者不是数字");
	           		return listFyxnVo;
				} 
	        	if(CommonMethods.isBlank(fzs) && !CommonMethods.isDouble(fzs)){
	        		listFyxnVo.setResult("第"+i+"行辅转速为空或者不是数字");
	           		return listFyxnVo;
				} 
	        	if(CommonMethods.isBlank(zj) && !CommonMethods.isDouble(zj)){
	        		listFyxnVo.setResult("第"+i+"行转矩为空或者不是数字");
	           		return listFyxnVo;
				} 
	        	if(CommonMethods.isBlank(zgl) && !CommonMethods.isDouble(zgl)){
	        		listFyxnVo.setResult("第"+i+"行轴功率为空或者不是数字");
	           		return listFyxnVo;
				} 
	        	if(CommonMethods.isBlank(xl) && !CommonMethods.isDouble(xl)){	
	        	   listFyxnVo.setResult("第"+i+"行效率为空或者不是数字");
	           		return listFyxnVo;
				} 
	        	
	        	/*风叶性能数据*/
//	        	fyxnData.setFyid(fyid);//风叶ID TODO 
//	        	fyxnData.setFyxnid(fyxnid);///风叶性能id TODO
	        	fyxnData.setZzs(zzs);//主转速
	        	fyxnData.setFzs(fzs);//副转速
	        	fyxnData.setInputdate(new Date());
	        	fyxnData.setInputuser("1"); 
	        	fyxnData.setJyl(jy);//静压力
	        	fyxnData.setLl(flow);//流量
//	        	fyxnData.setLxdid(lxdid); //试验单  TODO
	        	fyxnData.setMemo(memo); // 备注
	        	fyxnData.setNj(zj); //扭矩
	        	fyxnData.setUpdatedate(new Date());
	        	fyxnData.setUpdateuser("1");
	        	fyxnData.setXl(xl); //效率
//	        	fyxnData.setYxgl(yxgl); //有效功率
	        	fyxnData.setZgl(zgl); //轴功率
//	        	fyxnData.setZy(zy); //噪音 TODO
				fyxnList.add(fyxnData);
			}
		}
			listFyxnVo.setSydData(sydData);
			listFyxnVo.setFyxnData(fyxnList);
			wbk.close();
			input.close();
	  }catch(Exception e){
			e.printStackTrace();
		}
		return listFyxnVo;
	}
}
