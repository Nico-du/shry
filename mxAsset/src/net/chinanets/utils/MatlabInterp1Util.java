package net.chinanets.utils;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import ployfitX.ployfitX;
import InterpMultiX.InterpMultiX;
import InterpOneX.InterpOneX;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWComplexity;
import com.mathworks.toolbox.javabuilder.MWNumericArray;


public class MatlabInterp1Util {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MatlabInterp1Util();
		
	}
	
	MatlabInterp1Util(){
		try {
//		testA(13.5d);
//		testB();
//		testB();
			testD();
//			testB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void testD() throws Exception{
		double []xArray = {0.199,0.3,0.4,0.5,0.6,0.7,0.8,0.9};
		double []yArray={3068,2976,2890,2811,2736,2669,2596,2532};
		double aList[] = {0.5,0.6,0.7,0.8,0.9,1,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,2};
		double [][]multiXArray = {xArray,xArray,xArray};
		double [][]multiYArray = {yArray,yArray,yArray};
		double [][]multiAList = {aList,aList,aList};
		Object[][] result = InterpMultiX(multiXArray, multiYArray, multiAList);
		System.out.println("interp a :"+JSONArray.fromObject(multiAList));
		System.out.println("result is:"+JSONArray.fromObject(result));
	}
	private void testC() throws Exception{
		double []xArray = {0.199,0.3,0.4,0.5,0.6,0.7,0.8,0.9};
		double []yArray={3068,2976,2890,2811,2736,2669,2596,2532};
		double aList[] = {0.5,0.6,0.7,0.8,0.9,1,1.1,1.2,1.3,1.4,1.5,1.6,1.7,1.8,1.9,2};
		Object[] result = PolyfitX(xArray, yArray, aList);
		System.out.println("interp a :"+convertToString(aList));
		System.out.println("result is:"+convertToString(result));
	}
	private void testB() throws Exception{
		double []xArray = {0,2,4,6,8,10, 12,14,16,18,20,22,24};
		double []yArray={12,9,9,10,18,24, 28,27,25,20,18,15,13};
		double aList[] = {5.5,6.5,7.5,8.5,9.5,10.5,11.5,12.5,13.5,15};
		Object[] result = InterpOneX(xArray, yArray, aList);
		System.out.println("interp a :"+convertToString(aList));
	    System.out.println("result is:"+convertToString(result));
	}
	//一次只执行一个a
	private void testA(double paramA){
		long startTime = System.currentTimeMillis();

		MWNumericArray x = null; // 存放x值的数组
		MWNumericArray y = null; // 存放y值的数组
		MWNumericArray a = null;   
		InterpOneX thePlot = null; // plotter类的实例（在MatLab编译时，新建的类）
		Object[] result = null; 

		double aX = paramA;
		try {
			//			x=0:2:24;
			//			  y=[12   9   9   10   18  24   28   27   25   20  18  15  13];
			int []xArray = {0,2,4,6,8,10, 12,14,16,18,20,22,24};
			int []yArray={12,9,9,10,18,24, 28,27,25,20,18,15,13};

			// 分配x、y的值
			int[] dims = { 1, xArray.length };
			x = MWNumericArray.newInstance(dims, MWClassID.DOUBLE,
					MWComplexity.REAL);
			y = MWNumericArray.newInstance(dims, MWClassID.DOUBLE,
					MWComplexity.REAL);

			a = new MWNumericArray(Double.valueOf(aX),MWClassID.DOUBLE);

			//定义x y
			for (int i = 0; i < xArray.length; i++) {
				x.set(i+1, xArray[i]);
				y.set(i+1, yArray[i]);
			}


			// 初始化plotter的对象
			thePlot = new InterpOneX();

			// 计算结果
			result = thePlot.interpOneX(1,x, y,a);
			System.out.print("interp a :"+a.getDouble()+" result is :");
			System.out.println(result[0]);
			System.out.println("耗时1："+(System.currentTimeMillis() - startTime)+"ms");
		}

		catch (Exception e) {
			System.out.println("Exception: " + e.toString());
		}

		finally {
			// 释放本地资源
			MWArray.disposeArray(x);
			MWArray.disposeArray(y);
			MWArray.disposeArray(a);
			if (thePlot != null)
				thePlot.dispose();
		}
	}
	
	private static String convertToString(Object[] a){
		if(a == null) return "";
		StringBuilder sb = new StringBuilder();
		for(Object ch:a){
			sb.append("["+ch+"],");
		}
		return sb.toString();
	}
	private static String convertToString(double[] a){
		if(a == null) return "";
		StringBuilder sb = new StringBuilder();
		for(double ch:a){
			sb.append("["+ch+"],");
		}
		return sb.toString();
	}
	
	/**
	 * 调用matlab获取插值
	 * matlab中function为：
	 * U=interp1(x,y,a,'spline');
	 * @param xArray 源数据x数组
	 * @param yArray 源数据y数组
	 * @param aList 指定值a的数组
	 * @return
	 * @throws Exception 
	 */
	public static Double[] InterpOneX(double []xArray,double []yArray,double []aList) throws Exception {
		long startTime = System.currentTimeMillis();
		if(xArray == null || yArray==null || xArray.length <2 || xArray.length != yArray.length ||
				aList == null || aList.length < 1){
			System.out.println("MatlabInterp1Util.InterpOneX-参数错误,调用失败...");
			throw new Exception("MatlabInterp1Util.InterpOneX-参数错误,调用失败...");
		}
		
		
		MWNumericArray x = null; // 存放x值的数组
		MWNumericArray y = null; // 存放y值的数组
		MWNumericArray a = null; //存放a值
		InterpOneX thePlot = null; // InterpOneX类的实例（在MatLab编译时，新建的类）
		Object[] result = null; //结果集合
		Double[] dResult = new Double[aList.length]; //返回集合
		try {
			// 初始化plotter的对象
			thePlot = new InterpOneX();
			
			// 分配x、y的值
			int[] xyDims = { 1, xArray.length };
			x = MWNumericArray.newInstance(xyDims, MWClassID.DOUBLE,MWComplexity.REAL);
			y = MWNumericArray.newInstance(xyDims, MWClassID.DOUBLE,MWComplexity.REAL);
			//定义x y
			for (int i = 0; i < xArray.length; i++) {
				x.set(i+1, xArray[i]);
				y.set(i+1, yArray[i]);
			}
			
			for(int i=0;i<aList.length;i++){
				a = new MWNumericArray(Double.valueOf(aList[i]),MWClassID.DOUBLE);
				// 计算结果
				result = thePlot.interpOneX(1,x, y,a);
				dResult[i] =Double.valueOf(result[0]+"");
			}
			
			System.out.println("MatlabInterp1Util.InterpOneX-耗时："+(System.currentTimeMillis() - startTime)+"ms");
		}catch (Exception e) {
			System.out.println("Exception: " + e.toString());
			e.printStackTrace();
			throw e;
		}finally {
			// 释放本地资源
			MWArray.disposeArray(x);
			MWArray.disposeArray(y);
			MWArray.disposeArray(a);
			if (thePlot != null)
				thePlot.dispose();
		}
		return dResult;
	}
	/**
	 * 调用matlab获取插值
	 * matlab中function为：
	 * U=interp1(x,y,a,'spline');
	 * @param xArray 源数据x数组
	 * @param yArray 源数据y数组
	 * @param aList 指定值a的数组
	 * @return
	 * @throws Exception 
	 */
	public static Double[][] InterpMultiX(double [][]xArray,double [][]yArray,double [][]aList) throws Exception {
		long startTime = System.currentTimeMillis();
		if(xArray == null || yArray==null || xArray[0].length <2 || xArray.length <1 || xArray[0].length != yArray[0].length 
				|| xArray.length != yArray.length || aList == null || aList.length < 1 || aList[0].length < 1 || xArray.length != aList.length){
			System.out.println("MatlabInterp1Util.InterpOneX-参数错误,调用失败...");
			throw new Exception("MatlabInterp1Util.InterpOneX-参数错误,调用失败...");
		}
		
		final int sonAryLgth = 30;//默认子数组长度,为空填充0
		int aryLgth = xArray.length;//父数组长度
		if(xArray[aryLgth-1]==null){ aryLgth--;} if(xArray[aryLgth-1]==null){ aryLgth--;}
		MWNumericArray x = null; //存放x值的数组
		MWNumericArray y = null; //存放y值的数组
		MWNumericArray a = null; //存放a值
		InterpMultiX thePlot = null; //InterpOneX类的实例（在MatLab编译时，新建的类）
		Object[] result = null; //结果集合
		Double[][] dResult = new Double[aList.length][sonAryLgth]; //返回集合
		try {
			// 初始化plotter的对象
			thePlot = new InterpMultiX();
			
			// 分配x、y的值 xyDims={3,6} 代表一个3x6的二维数组
			int[] xyDims = { aryLgth, sonAryLgth };
			double[] xData = new double[aryLgth*sonAryLgth];
			double[] yData = new double[aryLgth*sonAryLgth];
			/*matlab数组是纵向排列,空值填充为-0.123456,在matlab程序中过滤掉
  int[] dims = {3, 6};
double[] Adata = { 1, 7, 13,
                   2, 8, 14,
                   3, 9, 15,
                   4, 10, 16,
                   5, 11, 17,
                   6, 12, 18};
    MWNumericArray A =
       MWNumericArray.newInstance(dims, Adata, MWClassID.DOUBLE);
    System.out.println("A = " + A);
 
When run, the example displays this output:
     A =   1   2   3   4   5   6
           7   8   9   10  11  12
           13  14  15  16  17  18
			 */
			//定义x y
			for(int i=0;i<sonAryLgth;i++){//30
				for (int j = 0; j < aryLgth; j++) {//3
					if(i < xArray[j].length){
						xData[i*aryLgth+j] = xArray[j][i];
						yData[i*aryLgth+j] = yArray[j][i];
					}else{
						xData[i*aryLgth+j] = -0.123456;
						yData[i*aryLgth+j] = -0.123456;
					}
				}
			}
			x = MWNumericArray.newInstance(xyDims, xData,MWClassID.DOUBLE);
//			System.out.println("X="+x);
			y = MWNumericArray.newInstance(xyDims, yData,MWClassID.DOUBLE);
//			System.out.println("Y="+y);
			//定义a
			final int aLgth = aList.length;
			int[] aDims = {aLgth,sonAryLgth};
			double[] aData = new double[aLgth*sonAryLgth];
			for(int i=0;i<sonAryLgth;i++){
				for(int j=0;j<aLgth;j++){
					if(i < aList[j].length){
						aData[i*aLgth+j] = aList[j][i];
					}else{
						aData[i*aLgth+j] = -0.123456;
					}
				} 
			}
			a = MWNumericArray.newInstance(aDims,aData,MWClassID.DOUBLE);
//			System.out.println("A="+a);
			result = thePlot.interpMultiX(1,x, y,a);
			
			//解析结果字符串
			String rstStr = result[0]+"";
			for(int i=0;i<rstStr.split(",").length;i++){
				String chStr = rstStr.split(",")[i];
				int dbCnt = 0;
				if(StringUtils.isBlank(chStr)){ continue;}
				for(String chDbl : chStr.split("      ")){
					if(StringUtils.isBlank(chDbl)){ continue;}
					dResult[i][dbCnt++] = NumberUtils.toDouble(chDbl);
				}
			}
			
			System.out.println("MatlabInterp1Util.InterpOneX-耗时："+(System.currentTimeMillis() - startTime)+"ms");
		}catch (Exception e) {
			System.out.println("Exception: " + e.toString());
			e.printStackTrace();
			throw e;
		}finally {
			// 释放本地资源
			MWArray.disposeArray(x);
			MWArray.disposeArray(y);
			MWArray.disposeArray(a);
			if (thePlot != null)
				thePlot.dispose();
		}
		return dResult;
	}
	/**
	 * 调用matlab ployfit(x,y,1)一次函数,获取插值
	 * matlab中function为：
	 * p = polyfit(x,y,1) 
	 * U = polyval(p,a);
	 * @param xArray 源数据x数组
	 * @param yArray 源数据y数组
	 * @param aList 指定值a的数组
	 * @return
	 * @throws Exception 
	 */
	public static Double[] PolyfitX(double []xArray,double []yArray,double []aList) throws Exception {
		long startTime = System.currentTimeMillis();
		if(xArray == null || yArray==null || xArray.length <2 || xArray.length != yArray.length ||
				aList == null || aList.length < 1){
			System.out.println("MatlabInterp1Util.PolyfitX-参数错误,调用失败...");
			throw new Exception("MatlabInterp1Util.PolyfitX-参数错误,调用失败...");
		}
		
		
		MWNumericArray x = null; // 存放x值的数组
		MWNumericArray y = null; // 存放y值的数组
		MWNumericArray a = null; //存放a值
		ployfitX thePlot = null; // InterpOneX类的实例（在MatLab编译时，新建的类）
		Object[] result = null; //结果集合
		Double[] dResult = new Double[aList.length]; //返回集合
		try {
			// 初始化plotter的对象
			thePlot = new ployfitX();
			
			// 分配x、y的值
			int[] xyDims = { 1, xArray.length };
			x = MWNumericArray.newInstance(xyDims, MWClassID.DOUBLE,MWComplexity.REAL);
			y = MWNumericArray.newInstance(xyDims, MWClassID.DOUBLE,MWComplexity.REAL);
			//定义x y
			for (int i = 0; i < xArray.length; i++) {
				x.set(i+1, xArray[i]);
				y.set(i+1, yArray[i]);
			}

			for(int i=0;i<aList.length;i++){
				a = new MWNumericArray(Double.valueOf(aList[i]),MWClassID.DOUBLE);
				// 计算结果
				result = thePlot.ployfitX(1,x, y,a);
				dResult[i] =Double.valueOf(result[0]+"");
			}
			
		    System.out.println("MatlabInterp1Util.PolyfitX-耗时："+(System.currentTimeMillis() - startTime)+"ms");
		}catch (Exception e) {
			System.out.println("Exception: " + e.toString());
			e.printStackTrace();
			throw e;
		}finally {
			// 释放本地资源
			MWArray.disposeArray(x);
			MWArray.disposeArray(y);
			MWArray.disposeArray(a);
			if (thePlot != null)
				thePlot.dispose();
		}
		return dResult;
	}
	
}
