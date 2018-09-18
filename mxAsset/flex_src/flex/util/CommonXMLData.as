package flex.util
{
	import mx.collections.ArrayCollection;

	/**
	 *静态公共常量类 
	 **/
	public class CommonXMLData
	{
		public static var UploadFile_BasePath:String = "sysArgFiles/";
		public static var InitData:Object = "";
		public static var WebAbsPath:String = "";
		
		public static var Bgdj_AddPage_FpSearch_CmbXml:XML = 
			<root>
			<item label="品牌" type="ppxh"/>
			<item label="型号"  type="xh"/>
			<item label="设备编号" type="sbbh"/>
			<item label="设备序列号" type="sbxlh"/>
			<item label="购置金额" type="gzje"/>
			</root>;
		
		//页面提示
		public static const Alert_SYSTitle:String = "系统提示";
		public static const Alert_Title:String = "信息提示框";
		public static const Alert_Add_Suc:String = "添加成功!";
		public static const Alert_Add_Failed:String = "添加失败!";
		public static const Alert_Alter_Suc:String = "修改成功!";
		public static const Alert_Alter_Failed:String = "修改失败!";
		public static const Alert_Save_Suc:String = "保存成功!";
		public static const Alert_Save_Failed:String = "保存失败!";
		public static const Alert_Del_Suc:String = "删除成功!";
		public static const Alert_Del_Failed:String = "删除失败!";
		public static const Alert_CAN_NOT_DELETE:String = "审核过的数据不能被删除!";
		public static const Alert_Alter_NotExistRecord:String = "这条记录已经被删除!";
		public static const Alert_Alter_NotExistFile:String = "文件已经被删除!";
		
		public static const Alert_SysMsgService_OutOfDate:String = "登陆超时,\"系统提醒\"服务已断开连接,请重新登录!";
		
		/**
		 *项目管理模块是否显示操作成功弹出框的判断
		 **/
		public static const HasSucc_Alert:Boolean = false;
		
		
		/**
		 *全选反选 
		 **/
		public static var Asset_SelectAll_ReverseXML:XML = 
			<root>
			<paremt  name="Select" mc="选择" />
			<paremt  name="All" mc="全选" />
			<paremt  name="Reverse" mc="反选" />
			</root>;
		/**
		 *其他功能
		 **/
		public static var Asset_OtherFuncListXML:XML = 
			<root>
			<paremt  name="otherFunc" mc="其他操作" />
			<paremt  name="download" mc="下载模版" />
			<paremt  name="export" mc="导入数据" />
			</root>;
		
		/**
		 * 所有类型资产XML
		 **/
		public static var Asset_ALLType_XML:XML = null;
		
		/**
		 * 房屋结构XML
		 **/
		public static var CBXData_FWJG_XML:ArrayCollection = new ArrayCollection([
			{value:"",label:"-"},
			{value:"fwjg-gjg",label:"钢结构"},
			{value:"fwjg-hnjg",label:"钢混结构"},
			{value:"fwjg-zm",label:"砖木"},
			{value:"fwjg-qt",label:"其他"}
		]);
		
		/**
		 * 房屋产权形式XML
		 **/
		public static var CBXData_FWCQXS_XML:ArrayCollection = new ArrayCollection([
			{value:"",label:"-"},
			{value:"fwcqxs-gm",label:"通过购买取得"},
			{value:"fwcqxs-js",label:"通过建设取得"},
			{value:"fwcqxs-sz",label:"因为受赠取得"},
			{value:"fwcqxs-dy",label:"因为抵押取得"},
			{value:"fwcqxs-jc",label:"因为继承取得"}
		]);
		
		/**
		 * 土地来源XML
		 **/
		public static var CBXData_TDLY_XML:ArrayCollection = new ArrayCollection([
			{value:"",label:"-"},
			{value:"tdly-hb",label:"划拨"},
			{value:"tdly-zr",label:"转让"},
			{value:"tdly-qt",label:"其他"}
		]);
		/**
		 * 土地入账形式XML
		 **/
		public static var CBXData_TDRZXS_XML:ArrayCollection = new ArrayCollection([
			{value:"",label:"-"},
			{value:"tdrzxs-ddzwgdzc",label:"单独作为固定资产"},
			{value:"tdrzxs-jrwxzc",label:"计入无形资产"},
			{value:"tdrzxs-jrfwjzw",label:"计入房屋建筑物"},
			{value:"tdrzxs-wrz",label:"未入账"}
		]);
		
		//taskId=0申请  1已审核  2已采购 3已验收 4已付款
		
		/**
		 *是否测试环境 (测试环境下会有报错弹出框提示)
		 **/
		public static const IsTestEnvironment:Boolean = false;
		
		/**
		 *部门组织XML 
		 **/
		public static var BmzzXml:XML = null;
		/**
		 *角色XML 
		 **/
		public static var RulesData:ArrayCollection = null;
		
		
		/**
		 * 是否只有上传用户才能操作数据
		 **/
		public static var IsOnlyUpdateUserEditAble:Boolean = true;
		
		
	}
}