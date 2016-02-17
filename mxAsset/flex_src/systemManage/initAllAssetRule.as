	// ActionScript file
	/**
	 * include到国标数据表Grid页面
	 * 包含
	 * 1.控制流程页面的权限
	 * 2.删除入库等变更 关联数据 时同时删除历史记录
	 * */
	import flex.pojos.UserSessionVo;
	import flex.pojos.UsersVo;
	import flex.util.CommonMethod;
	import flex.util.CommonXMLData;
	
	import mx.controls.Alert;
	import mx.core.Application;

    //权限控制
	public var isAddUsable:Boolean = true;
	public var isDelUsable:Boolean = true;
	public var isEditUsable:Boolean = true;
	public var isExportUsable:Boolean = true;
	public var isOtherUsable:Boolean = false;
	//Form界面的状态
	public var isNewPage:Boolean = false;//是否新建
	private var bmCondition:String = " 1=1 ";
	public var condit:String="";
	public var deptSearchCondtion:String="";
	

	//关联DICID批次编号 选择维修/报废 编号页面  的查询条件信息
	public var selectPage_pcbh:String=""; //批次编号
	public var selectPage_dicid:String="";  //国标编码
	public var selectPage_dicname:String=""; //物资名称
    public var selectPage_dataids:String=""; //已选择的数据ID
	public var selectPage_idColumnName:String=""; //ID列名
    public var selectPage_pageType:String="";//维修/报废/领用页面  设置可选状态
	/**
	 * 权限控制step2
	 *  根据权限设置查询条件 
	 **/
	public function setBmCondition(bmStr:String="glbm",syrStr:String="glr"):void {
		if(this.hasOwnProperty("bmmcStrSpecial")){bmStr = this["bmmcStrSpecial"];}
		if(this.hasOwnProperty("syrStrSpecial")){syrStr = this["syrStrSpecial"];}
		if(bmStr == null ){ return;}
		
		var userVo:UserSessionVo = Application.application.userVo;
		if(deptSearchCondtion == "" && CommonXMLData.IsTestEnvironment){Alert.show("deptSearchCondtion 为空 !"); return; }
		if(deptSearchCondtion == ""){deptSearchCondtion = "one";}
		deptSearchCondtion = convertStrToSQLStr(deptSearchCondtion,userVo);
		switch(deptSearchCondtion){
			case "one": bmCondition = (syrStr==null ? "" :(syrStr +" ='" + userVo.userName + "' "));break;
			case "all": return;
			default://选择部门
				if(userVo.isLeader == "1"){
					bmCondition = bmStr + " in (" + deptSearchCondtion + (userVo.bmmc == null ? "" : ",'" + userVo.bmmc + "'")+") ";
				}else{
					bmCondition = " ("+bmStr + " in (" + deptSearchCondtion + ") "+ (syrStr == null ? ")" : (" or "+syrStr+"='" + userVo.userName + "') "));
				}
				break;
		}
		/*var bmArr:Array = deptSearchCondtion.split(",");
		if(bmArr.length == 1 && (userVo.deptName.indexOf(",")>-1 || userVo.deptName.indexOf("(")>-1)){//考虑合并部门的情况eg: 编研部(档案宣传部)
		var myPattern:RegExp = /\(/gi;  
		var myPattern1:RegExp = /\)/gi;   
		bmArr = (userVo.deptName.replace(myPattern1,"").replace(myPattern,",")+",").split(",");
		}
		if(bmArr.length ==2 && bmArr.length == 1 && (userVo.deptName.indexOf(",")>-1 || userVo.deptName.indexOf("(")>-1)){
		bmArr.push(bmArr[1]);	
		}
		var bmString:String = "";
		for(var j:int = 0; j < bmArr.length - 1; j++) {
		if(j == bmArr.length - 2) {
		bmString += "'" + bmArr[j] + "'"
		}else {
		bmString += "'" + bmArr[j] + "',"
		}
		}
		if(userVo.ruleId.indexOf("41") > -1) {//超级管理员
		bmCondition = " 1=1 ";
		}else if(userVo.isLeader == "1") {//部门领导 查看本部门所有人的设备 ，个人查看本人的设备
		bmCondition = bmStr + " in (" + deptSearchCondtion + ",'" + userVo.deptName + "') ";
		}else {	//非部门领导
		if(deptSearchCondtion == "one") {
		bmCondition = " syr='" + userVo.userName + "' ";
		}else {
		//bmCondition = bmStr + " in (" + bmString + ") or syr='" + userVo.userName + "' ";
		bmCondition = bmStr + " in (" + deptSearchCondtion + ") ";
		}
		}
		*/
		if(condit!=""){
			bmCondition = bmCondition + " and " + condit;
		}
		
	}
	private function convertStrToSQLStr(str:String,userVo:UserSessionVo):String{
		
		if(deptSearchCondtion.lastIndexOf(",") == (deptSearchCondtion.length-1)){
			deptSearchCondtion = deptSearchCondtion.substr(0,(deptSearchCondtion.length-1));
		}
		if(str == "myDept"){
			str = userVo.deptName;
		}else if(str == "all" || str == "one"){
			return str;
		}
		var bmArr:Array = str.split(",");
		var bmString:String = "";
		for(var j:int = 0; j < bmArr.length; j++) {
			if(bmArr[j].length < 1)continue;
			if(j == bmArr.length - 1) {
				bmString += "'" + bmArr[j] + "'";
			}else {
				bmString += "'" + bmArr[j] + "',";
			}
		}
		return bmString;
	}
	
	/**
	 *添加默认搜索条件(批次编号)
	 **/
	private function addDefaultCondition(condition:Object,currentPage:Object):void{
		if(this.hasOwnProperty("idColumnName")){selectPage_idColumnName = this["idColumnName"];}
		if(!CommonMethod.isNullOrWhitespace(selectPage_dataids)){condition["s_"+selectPage_idColumnName.toLowerCase()+"_string_in"]= selectPage_dataids; }
		if(!CommonMethod.isNullOrWhitespace(selectPage_pageType)){ 
		if(selectPage_pageType == "报废"){condition.s_zt_string_noteq= "'报废'";} 
		if(selectPage_pageType == "维修"){condition.s_zt_string_notin= "维修,报废";}
		if(selectPage_pageType == "领用"){condition.s_zt_string_eq= "库存"; }
		}
		if(!CommonMethod.isNullOrWhitespace(selectPage_pcbh)){condition.s_pcbh_string_eq= selectPage_pcbh; }
		if(!CommonMethod.isNullOrWhitespace(selectPage_dicid)){condition.s_dicid_string_eq= selectPage_dicid; }
			 
		if((currentPage.data as XML) != null && !CommonMethod.isNullOrWhitespace((currentPage.data as XML).@pcbh)){
			condition.s_pcbh_string_eq=(currentPage.data as XML).@pcbh+"";
			}
	}


	/**
	 ** 删除 多个历史记录的方法   (删除数据时执行回调函数) 
	 **/
	
	//入库/领用/退库/维修/报废 登记时添加/删除入库数据同时添加历史记录信息
	public var alterBackFunc:Function = null;
	public var alterBackObj:Object = new Object();
	/**
	 * 删除 多个历史记录
	 * step 1:设置删除时的数据
	 **/
	private function setCallBackDelData(dataids:String):void{
		alterBackObj.altertype="delMuch";
		alterBackObj.asetetid = dataids;
		if(this.hasOwnProperty("idColumnName")){
			alterBackObj.assetidcolumn = this["idColumnName"];
		}
		if(this.hasOwnProperty("tempObjBeanName")){
			alterBackObj.assettabnename = this["tempObjBeanName"]; 
		}
	}
	/**
	 * 删除 多个历史记录
	 *step 2:执行回调函数 
	 **/
   private function doCallBackFunc():void{
	   if(alterBackFunc != null){
		   alterBackFunc.call(null,alterBackObj);
	   }
   }
	/**
	 * 设值已登记数量
	 **/
	private function setYDJSLText(ydjslStr:int):void{
		alterBackObj.altertype="setYdjsl";
		alterBackObj.ydjsl = ydjslStr;
		if(alterBackFunc != null){
			alterBackFunc.call(null,alterBackObj);
		}
	}





