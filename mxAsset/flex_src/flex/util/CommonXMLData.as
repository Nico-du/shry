package flex.util
{
	import mx.collections.ArrayCollection;

	/**
	 *静态公共常量类 
	 **/
	public class CommonXMLData
	{
		public static var Bgdj_AddPage_FpSearch_CmbXml:XML = 
			<root>
			<item label="品牌" type="ppxh"/>
			<item label="型号"  type="xh"/>
			<item label="设备编号" type="sbbh"/>
			<item label="设备序列号" type="sbxlh"/>
			<item label="购置金额" type="gzje"/>
			</root>;
		public static var Bgdj_AddPage_Search_CmbXml:XML = 
			<root>
			<item label="品牌" type="ppxh"/>
			<item label="型号"  type="xh"/>
			<item label="设备编号" type="sbbh"/>
			<item label="设备序列号" type="sbxlh"/>
			<item label="使用部门"  type="bmmc"/>
			<item label="使用人"  type="syr"/>
			<item label="使用地点" type="sydd"/>
			</root>;
		public static var Bgdj_AddPage_Sblx_CmbXml:XML = 
				<root>
					<item label="台式机"  type="台式机"/>
					<item label="便携电脑" type="便携电脑"/>
					<item label="显示器" type="显示器"/>
					<item label="打印机" type="打印机"/>
					<item label="扫描仪" type="扫描仪"/>
					<item label="传真机" type="传真机"/>
					<item label="机顶盒" type="机顶盒"/>
					<item label="视频切换器" type="视频切换器"/>
					<item label="速录机" type="速录机"/>
					<item label="音箱" type="音箱"/>
					<item label="数字录音电话" type="数字录音电话"/>
					<item label="单兵摄录仪" type="单兵摄录仪"/>
					<item label="读卡器" type="读卡器" />
					<item label="电视机" type="电视机"/>
					<item label="介质"   type="介质"/>
					<item label="服务器" type="服务器"/>
					<item label="动力设备" type="动力设备"/>
					<item label="视频设备" type="视频设备"/>
					<item label="网络设备"   type="网络设备"/>
					<item label="存储设备"   type="存储设备"/>
				</root>;
		public static var Bgdj_AddPage_BfOrHs_ZtXml:XML =
			<root>
				<item label="报废"  type="报废"/>
				<item label="回收" type="回收"/>
				<item label="拟报废" type="拟报废"/>
			</root>;
		/**
		 * 所有设备子类型
		 **/
		public static var Query_BrowserQuery_TypeTreeXml:XML = 
					<root>
						<menu label="台式机" icon="pcj.jpg" app="computer_tsj"/>
						<menu label="便携电脑" icon="pcj.jpg" app="computer_bxdl"/>
						<menu label="显示器"  icon="pcj.jpg" app="computer_xsq"/>
						
						<menu label="U盘" icon="file.jpg" app="jz_up"/>
						<menu label="移动硬盘" icon="file.jpg" app="jz_ydyp"/>
						<menu label="录音笔" icon="file.jpg" app="jz_lyb"/>
						
						<menu label="打印机" icon="file.jpg" app="wssb_dyj"/>
						<menu label="扫描仪" icon="file.jpg" app="wssb_smy"/>
						<menu label="传真机" icon="file.jpg" app="wssb_czj"/>
						<menu label="机顶盒" icon="file.jpg" app="wssb_jdh"/>
						<menu label="视频切换器" icon="file.jpg" app="wssb_spqhq"/>
						<menu label="速录机" icon="file.jpg" app="wssb_slj"/>
						<menu label="音箱" icon="file.jpg" app="wssb_yx"/>
						<menu label="数字录音电话" icon="file.jpg" app="wssb_szlydh"/>
						<menu label="读卡器" icon="file.jpg" app="wssb_dkq"/>
						<menu label="电视机" icon="file.jpg" app="wssb_dhj"/>
						<menu label="单兵摄录仪" icon="file.jpg" app="wssb_dbsly"/>
						<menu label="小型机" icon="file.jpg" app="server_xxj"/>
						<menu label="工控机" icon="file.jpg" app="server_gkj"/>
						<menu label="PC服务器" icon="file.jpg" app="server_pcfwq"/>
						<menu label="刀片服务器" icon="file.jpg" app="server_dpfwq"/>
						
						<menu label="磁盘阵列" icon="file.jpg" app="storage_cplz"/>
						<menu label="磁盘" icon="file.jpg" app="storage_cp"/>
						<menu label="磁带" icon="file.jpg" app="storage_cd"/>
						<menu label="磁盘扩展柜" icon="file.jpg" app="storage_cpkzg"/>
						<menu label="磁盘库" icon="file.jpg" app="storage_cpk"/>
						
						<menu label="路由器" icon="file.jpg" app="net_lyq"/>
						<menu label="交换机" icon="file.jpg" app="net_jhj"/>
						<menu label="安全设备" icon="file.jpg" app="net_aqsb"/>
						<menu label="光纤收发器" icon="file.jpg" app="net_gqsfq"/>
						
						<menu label="录像设备" icon="file.jpg" app="video_lxsb"/>
						<menu label="摄像设备" icon="file.jpg" app="video_sxsb"/>
						<menu label="投影机" icon="file.jpg" app="video_syj"/>
						<menu label="显示屏" icon="file.jpg" app="video_xsp"/>
						<menu label="音响设备" icon="file.jpg" app="video_yxsb"/>
						
						<menu label="UPS" icon="file.jpg" app="ups"/>
					</root>;
		/**
		 * 所有设备类型
		 **/
		public static var Query_ConditionReport_AssetTypeXml:XML = 
					<root>
						<menu label="计算机终端" icon="pcj.jpg" app="computer"/>
						<menu label="移动存储介质" icon="pcj.jpg" app="jz"/>
						<menu label="服务器" icon="pcj.jpg" app="server"/>
						<menu label="存储设备" icon="pcj.jpg" app="storage"/>
						<menu label="网络设备" icon="pcj.jpg" app="net"/>
						<menu label="动力设备" icon="pcj.jpg" app="ups"/>
						<menu label="视频设备" icon="pcj.jpg" app="video"/>
						<menu label="外设设备" icon="pcj.jpg" app="wssb"/>
					</root>;
		/**
		 *采购方式  所有
		 **/
		public static var XmApply_cgfsXML:XML = 
			<root>
			<paremt  name="jzcg" mc="集中采购-公开招投标" />
			<paremt  name="jzcg" mc="集中采购-非公开招投标" />
			<paremt  name="jzcg" mc="集中采购-公开询价比价" />
			<paremt  name="jzcg" mc="集中采购-自行询价比价" />
			<paremt  name="jzcg" mc="集中采购-单一来源" />
			<paremt  name="jscg" mc="集市采购" />
			<paremt  name="fscg" mc="分散采购-公开招投标" />
			<paremt  name="fscg" mc="分散采购-非公开招投标" />
			<paremt  name="fscg" mc="分散采购-公开询价比价" />
			<paremt  name="fscg" mc="分散采购-自行询价比价" />
			<paremt  name="fscg" mc="分散采购-单一来源" />
			<paremt  name="zxcg" mc="自行采购" />
			</root>;
			/*<paremt  name="zxcg" mc="自行采购" />*/
		
		/**
		 *采购方式  分散/集中   细分
		 **/
		public static var XmApply_cgfs_sfjzXML:XML = 
			<root>
			<paremt  name="jzcg" mc="公开招投标" />
			<paremt  name="jzcg" mc="非公开招投标" />
			<paremt  name="jzcg" mc="公开询价比价" />
			<paremt  name="jzcg" mc="自行询价比价" />
			<paremt  name="jzcg" mc="单一来源" />
			</root>;
		
		/**
		 * 项目类型
		 **/
		public static var XmApply_xmlxXML:XML = 
			<root>
			<paremt  name="ywxm" mc="运维项目" />
			<paremt  name="xjxm" mc="新建项目" />
			<paremt  name="sjgz" mc="升级改造项目" />
			<paremt  name="bmys" mc="部门预算项目" />
			<paremt  name="zyczys" mc="中央财政预算项目" />
			</root>;
		
		/**
		 * 项目管理默认显示本年度项目
		 **/
		public static var XmApply_defaultYearXML:XML = 
			<root>
			<paremt  name="bnd" mc="本年度" />
			<paremt  name="qb" mc="全部" />
			<paremt  name="asj" mc="按时间" />
			</root>;
		
		public static const COMPUTER:String = "计算机终端";
		public static const JZ:String = "移动存储介质";
		public static const STORAGE:String = "存储设备";
		public static const NET:String = "网络设备";
		public static const UPS:String = "动力设备";
		public static const VIDEO:String = "视频设备";
		public static const WSSB:String = "外设设备";
		public static const SERVER:String = "服务器";
		public static const HC:String = "硒鼓/色带";
		public static const BGYP:String = "办公用品";

		
		public static const STATUS_XG:String = "新购";
		public static const STATUS_SY:String = "使用";
		public static const STATUS_HS:String = "回收";
		public static const STATUS_NBF:String = "拟报废";
		public static const STATUS_BF:String = "报废";
		
		public static const STATUS_BGDJ_WX:String = "维修";
		public static const STATUS_BGDJ_DSH:String = "待审核";
		
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
	}
}