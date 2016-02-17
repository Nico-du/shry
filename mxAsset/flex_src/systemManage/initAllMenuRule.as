// ActionScript file
/**
 * 控制资产的权限
 * */
import flex.pojos.UserSessionVo;

import mx.controls.Alert;
import mx.core.Application;

private var bmCondition:String = " 1=1 ";
public var condit:String="";
public var deptSearchCondtion:String="";
/**
 * 权限控制step2
 *  根据权限设置查询条件 
 **/
public function setBmCondition(bmStr:String="bmmc",syrStr:String="syr"):void {
	if(this.hasOwnProperty("bmmcStrSpecial")){bmStr = this["bmmcStrSpecial"];}
	if(this.hasOwnProperty("syrStrSpecial")){syrStr = this["syrStrSpecial"];}
	if(bmStr == null){ return;}
	var userVo:UserSessionVo = Application.application.userVo;
	if(deptSearchCondtion == ""){Alert.show("deptSearchCondtion 为空 !"); return; }
	if(deptSearchCondtion.lastIndexOf(",") == (deptSearchCondtion.length-1)){
		deptSearchCondtion = deptSearchCondtion.substr(0,(deptSearchCondtion.length-1));
	}
	deptSearchCondtion = convertStrToSQLStr(deptSearchCondtion);
	switch(deptSearchCondtion){
		case "one": bmCondition = (syrStr==null ? "" :(syrStr + " ='" + userVo.userName + "' "));break;
		case "all": return;
		default://本部门或选择部门
			if(userVo.isLeader == "1"){
				bmCondition = bmStr + " in (" + deptSearchCondtion + (userVo.bmmc == null ? "" : ",'" + userVo.bmmc + "'")+") ";
			}else{
				bmCondition =  " ("+bmStr + " in (" + deptSearchCondtion + ") "+ (syrStr == null ? "" : (" or "+syrStr+"='" + userVo.userName + "') "));
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
private function convertStrToSQLStr(str:String):String{
	if(deptSearchCondtion.lastIndexOf(",") == (deptSearchCondtion.length-1)){
		deptSearchCondtion = deptSearchCondtion.substr(0,(deptSearchCondtion.length-1));
	}
	if(str == "myDept"){
		str = userVo.deptName;
	}
	
	var bmArr:Array = str.split(",");
	var bmString:String = "";
	for(var j:int = 0; j < bmArr.length; j++) {
		if(bmArr[j].length < 1)continue;
		if(j == bmArr.length - 1) {
			bmString += "'" + bmArr[j] + "'"
		}else {
			bmString += "'" + bmArr[j] + "',"
		}
	}
	return bmString;
}