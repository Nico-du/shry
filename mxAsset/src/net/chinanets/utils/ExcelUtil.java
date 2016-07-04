package net.chinanets.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Excel导入导出
 * 
 */
public class ExcelUtil {
	public static final String ErrorPrefix = "Error:";
	public static final String SUCCESS = "AC";

	/**
	 * 写入数据到模版Excel文件中
	 * @param excelPath 模版excel路径
	 * @param columnDataMap 字段数据内容
	 * @param columnXYMap 字段数据在Excel中坐标  Map<String,int[]> 0-X  1-Y
	 * @param listDataMap 列表数据
	 * @param listXYMap 列表数据在Excel中坐标,包含字段第一行的XY坐标
	 */
	public static String writeDataToExcel(String excelPath,String targetExcelPath,Map<String,String> columnDataMap,Map<String,int[]> columnXYMap,List<Map<String,String>> dataList,Map<String,int[]> listXYMap){
		File excelFile = new File(excelPath);
		if(!excelFile.exists() || !excelFile.isFile() || !excelPath.endsWith(".xls")){ return ErrorPrefix+"Excel文件格式错误或找不到文件,请使用有效的97~03excel"; }
		 WritableWorkbook book  = null;
		 Workbook srcbk = null;
		try {
			// 打开文件
			srcbk = Workbook.getWorkbook(excelFile);
	         
          book = Workbook.createWorkbook(new File(targetExcelPath),srcbk);
			// 打开模版第一页 参数0表示这是第一页
          WritableSheet sheet = book.getSheet(0);
			//	            WritableSheet sheet = book.createSheet("学生", 0);
			Set<String> eachSet;
			int[] eachXY;
			eachSet =  columnDataMap.keySet();
			//填充字段数据内容
			for(String eachKey : eachSet){
				eachXY = columnXYMap.get(eachKey);
				WritableCell cell =sheet.getWritableCell(eachXY[0], eachXY[1]);//获取第一个单元格  
				jxl.format.CellFormat cf = cell.getCellFormat();//获取第单元格的格式  
				if(CommonMethods.isDouble(columnDataMap.get(eachKey))){
					jxl.write.Number lbl = new jxl.write.Number(eachXY[0], eachXY[1], Double.parseDouble(columnDataMap.get(eachKey)));//将第一个单元格的值改为“修改後的值”  
					lbl.setCellFormat(cf);//将修改后的单元格的格式设定成跟原来一样  
					sheet.addCell(lbl);//将改过的单元格保存到sheet  
				}else{
					jxl.write.Label lbl = new jxl.write.Label(eachXY[0], eachXY[1], columnDataMap.get(eachKey));//将第一个单元格的值改为“修改後的值”  
					lbl.setCellFormat(cf);//将修改后的单元格的格式设定成跟原来一样  
					sheet.addCell(lbl);//将改过的单元格保存到sheet  
				}
			}

			//填充列表数据内容  
			int rowCount = 0;
			eachSet =  dataList.get(0).keySet();
			for(Map<String,?> eachObj : dataList){
				for(String eachKey : eachSet){
					eachXY = listXYMap.get(eachKey);
					if(eachXY == null){continue; }
					WritableCell cell =sheet.getWritableCell(eachXY[0], eachXY[1]+ rowCount);//获取第一个单元格  
					jxl.format.CellFormat cf = cell.getCellFormat();//获取第单元格的格式  
					String eachVal = eachObj.get(eachKey)+"";
					if(CommonMethods.isDouble(eachVal)){
						jxl.write.Number lbl = new jxl.write.Number(eachXY[0], eachXY[1]+ rowCount,Double.parseDouble(eachVal) );//将第一个单元格的值改为“修改後的值”  
						lbl.setCellFormat(cf);//将修改后的单元格的格式设定成跟原来一样  
						sheet.addCell(lbl);//将改过的单元格保存到sheet  
					}else{
						jxl.write.Label lbl = new jxl.write.Label(eachXY[0], eachXY[1]+ rowCount,eachVal );//将第一个单元格的值改为“修改後的值”  
						lbl.setCellFormat(cf);//将修改后的单元格的格式设定成跟原来一样  
						sheet.addCell(lbl);//将改过的单元格保存到sheet  
					}
//					sheet.addCell( new Label(eachXY[0] , eachXY[1] + rowCount, eachObj.get(eachKey)+"") );
				}
				rowCount++;
			}

			//	            sheet.addCell(new Number(1, i, stuList.get(i).getAge()));

			// 写入数据并关闭文件
			book.write();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
			return ErrorPrefix+"写入Excel文件发生异常";
		}finally{
			try {
				if(srcbk!=null){
					srcbk.close();
				}
				if(book!=null){
					book.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}

		return SUCCESS;
	}

	/**
	 * 将List<Object>转换为List<Map>
	 * @param listIn
	 * @return
	 */
	public List<Map<String,String>> convertListObjectToListMap(List<Object> listIn){
		List<Map<String,String>> rstList = new ArrayList<Map<String,String>>();
		if(listIn == null){ return rstList;}
		for(Object each : listIn){
			rstList.add(convertObjectToMap(each));
		}
		return rstList;
	}

	/**
	 * 将Object转换为Map
	 * @param objIn
	 * @return
	 */
	public Map<String,String> convertObjectToMap(Object objIn){
		Map<String,String> rstMap = new HashMap<String,String>();
		try {
			if(objIn == null){ return rstMap;}
			Class clazz = objIn.getClass();
			Field[] fields = clazz.getFields();
			for(Field each : fields){
				rstMap.put(each.getName(), each.get(objIn)+"");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return rstMap;
	}

	/**
	 * 导入(导入到内存)
	 */

	public void importExcel() {
		Workbook book = null;
		try {
			book = Workbook.getWorkbook(new File("D:/test/测试.xls"));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			int rows=sheet.getRows();
			int columns=sheet.getColumns();
			// 遍历每行每列的单元格
			for(int i=0;i<rows;i++){
				for(int j=0;j<columns;j++){
					Cell cell = sheet.getCell(j, i);
					String result = cell.getContents();
					if(j==0){
						System.out.print("姓名："+result+" ");
					}
					if(j==1){
						System.out.print("年龄："+result+" ");
					}
					if((j+1)%2==0){ 
						System.out.println();
					}
				}
			}
			System.out.println("========");
			// 得到第一列第一行的单元格
			Cell cell1 = sheet.getCell(0, 0);
			String result = cell1.getContents();
			System.out.println(result);
			System.out.println("========");
		} catch (Exception e) {
			System.out.println(e);
		}finally{
			if(book!=null){
				book.close();
			}
		}
	}

	/**
	 * 导出(导出到磁盘)
	 */

	public void exportExcel() {
		WritableWorkbook book = null;
		try {
			// 打开文件
			book = Workbook.createWorkbook(new File("D:/test/测试.xls"));
			// 生成名为"学生"的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("学生", 0);
			// 指定单元格位置是第一列第一行(0, 0)以及单元格内容为张三
			Label label = new Label(0, 0, "张三");
			// 将定义好的单元格添加到工作表中
			sheet.addCell(label);
			// 保存数字的单元格必须使用Number的完整包路径
			jxl.write.Number number = new jxl.write.Number(1, 0, 30);
			sheet.addCell(number);
			// 写入数据并关闭文件
			book.write();
		} catch (Exception e) {
			System.out.println(e);
		}finally{
			if(book!=null){
				try {
					book.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}
	}

	/**
	 * 对象数据写入到Excel
	 */

	public void writeExcel() {
		WritableWorkbook book = null;
		try {
			// 打开文件
			book = Workbook.createWorkbook(new File("D:/test/stu.xls"));
			// 生成名为"学生"的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("学生", 0);

			//            List<Student> stuList=queryStudentList();
			//            if(stuList!=null && !stuList.isEmpty()){
			//                for(int i=0; i<stuList.size(); i++){
			//                    sheet.addCell(new Label(0, i, stuList.get(i).getName()));
			//                    sheet.addCell(new Number(1, i, stuList.get(i).getAge()));
			//                }
			//            }

			// 写入数据并关闭文件
			book.write();
		} catch (Exception e) {
			System.out.println(e);
		}finally{
			if(book!=null){
				try {
					book.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}

	}

	/**
	 * 读取Excel数据到内存
	 */

	public void readExcel() {
		Workbook book = null;
		try {
			// 打开文件
			book = Workbook.getWorkbook(new File("D:/test/stu.xls"));
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			int rows=sheet.getRows();
			int columns=sheet.getColumns();
			//            List<Student> stuList=new ArrayList<Student>();
			//            // 遍历每行每列的单元格
			//            for(int i=0;i<rows;i++){
			//                Student stu = new Student();
			//                for(int j=0;j<columns;j++){
			//                    Cell cell = sheet.getCell(j, i);
			//                    String result = cell.getContents();
			//                    if(j==0){
			//                        stu.setName(result);
			//                    }
			//                    if(j==1){
			//                        stu.setAge(NumberUtils.toInt(result));
			//                    }
			//                    if((j+1)%2==0){
			//                        stuList.add(stu);
			//                        stu=null;
			//                    }
			//                }
			//            }
			//            
			//            //遍历数据
			//            for(Student stu : stuList){
			//                System.out.println(String.format("姓名：%s, 年龄：%s", 
			//                        stu.getName(), stu.getAge()));
			//            }

		} catch (Exception e) {
			System.out.println(e);
		}finally{
			if(book!=null){
				try {
					book.close();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		}

	}

	/**
	 * 图片写入Excel，只支持png图片
	 */

	public void writeImg() {
		WritableWorkbook wwb = null;
		try {
			wwb = Workbook.createWorkbook(new File("D:/test/image.xls"));
			WritableSheet ws = wwb.createSheet("图片", 0);
			File file = new File("D:\\test\\png.png");
			//前两位是起始格，后两位是图片占多少个格，并非是位置
			WritableImage image = new WritableImage(1, 4, 6, 18, file);
			ws.addImage(image);
			wwb.write();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(wwb!=null){
				try {
					wwb.close();
				} catch (Exception e) {
					e.printStackTrace();
				}  
			}
		}
	}



	private static String MESSAGE = "";  

	/** 
	 * 复制单个文件 
	 *  
	 * @param srcFileName 
	 *            待复制的文件名 
	 * @param descFileName 
	 *            目标文件名 
	 * @param overlay 
	 *            如果目标文件存在，是否覆盖 
	 * @return 如果复制成功返回true，否则返回false 
	 */  
	public static boolean copyFile(String srcFileName, String destFileName,  
			boolean overlay) {  
		File srcFile = new File(srcFileName);  

		// 判断源文件是否存在  
		if (!srcFile.exists()) {  
			MESSAGE = "源文件：" + srcFileName + "不存在！";  
			
			return false;  
		} else if (!srcFile.isFile()) {  
			MESSAGE = "复制文件失败，源文件：" + srcFileName + "不是一个文件！";  
			
			return false;  
		}  

		// 判断目标文件是否存在  
		File destFile = new File(destFileName);  
		if (destFile.exists()) {  
			// 如果目标文件存在并允许覆盖  
			if (overlay) {  
				// 删除已经存在的目标文件，无论目标文件是目录还是单个文件  
				new File(destFileName).delete();  
			}  
		} else {  
			// 如果目标文件所在目录不存在，则创建目录  
			if (!destFile.getParentFile().exists()) {  
				// 目标文件所在目录不存在  
				if (!destFile.getParentFile().mkdirs()) {  
					// 复制文件失败：创建目标文件所在目录失败  
					return false;  
				}  
			}  
		}  

		// 复制文件  
		int byteread = 0; // 读取的字节数  
		InputStream in = null;  
		OutputStream out = null;  

		try {  
			in = new FileInputStream(srcFile);  
			out = new FileOutputStream(destFile);  
			byte[] buffer = new byte[1024];  

			while ((byteread = in.read(buffer)) != -1) {  
				out.write(buffer, 0, byteread);  
			}  
			return true;  
		} catch (FileNotFoundException e) {  
			return false;  
		} catch (IOException e) {  
			return false;  
		} finally {  
			try {  
				if (out != null)  
					out.close();  
				if (in != null)  
					in.close();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
	}  

	/** 
	 * 复制整个目录的内容 
	 *  
	 * @param srcDirName 
	 *            待复制目录的目录名 
	 * @param destDirName 
	 *            目标目录名 
	 * @param overlay 
	 *            如果目标目录存在，是否覆盖 
	 * @return 如果复制成功返回true，否则返回false 
	 */  
	public static boolean copyDirectory(String srcDirName, String destDirName,  
			boolean overlay) {  
		// 判断源目录是否存在  
		File srcDir = new File(srcDirName);  
		if (!srcDir.exists()) {  
			MESSAGE = "复制目录失败：源目录" + srcDirName + "不存在！";  
			
			return false;  
		} else if (!srcDir.isDirectory()) {  
			MESSAGE = "复制目录失败：" + srcDirName + "不是目录！";  
			
			return false;  
		}  

		// 如果目标目录名不是以文件分隔符结尾，则加上文件分隔符  
		if (!destDirName.endsWith(File.separator)) {  
			destDirName = destDirName + File.separator;  
		}  
		File destDir = new File(destDirName);  
		// 如果目标文件夹存在  
		if (destDir.exists()) {  
			// 如果允许覆盖则删除已存在的目标目录  
			if (overlay) {  
				new File(destDirName).delete();  
			} else {  
				MESSAGE = "复制目录失败：目的目录" + destDirName + "已存在！";  
				return false;  
			}  
		} else {  
			// 创建目的目录  
			System.out.println("目的目录不存在，准备创建。。。");  
			if (!destDir.mkdirs()) {  
				System.out.println("复制目录失败：创建目的目录失败！");  
				return false;  
			}  
		}  

		boolean flag = true;  
		File[] files = srcDir.listFiles();  
		for (int i = 0; i < files.length; i++) {  
			// 复制文件  
			if (files[i].isFile()) {  
				flag = copyFile(files[i].getAbsolutePath(),  
						destDirName + files[i].getName(), overlay);  
				if (!flag)  
					break;  
			} else if (files[i].isDirectory()) {  
				flag = copyDirectory(files[i].getAbsolutePath(),  
						destDirName + files[i].getName(), overlay);  
				if (!flag)  
					break;  
			}  
		}  
		if (!flag) {  
			MESSAGE = "复制目录" + srcDirName + "至" + destDirName + "失败！";  
			return false;  
		} else {  
			return true;  
		}  
	}
	
	/**
     * 删除单个文件
     * @param sPath       被删除文件的路径+文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        Boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
 
    /**
     * 删除目录（文件夹）以及目录下的文件
     * 
     * @param sPath
     *            被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        Boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }


}