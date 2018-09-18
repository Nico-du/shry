import cn.cnsasfram.helper.ASJsonHelper;

import com.adobe.serialization.json.JSON;

import flex.util.CommonMethod;

import mx.core.FlexGlobals;

//查看页面的选中数据
public var tempSelectedItem:Object = null;

/**
 *包含到变更登记, 登记关联的固定资产数据的页面
 * 1.包含 操作历史记录的方法
 **/

/**
 *保存变更记录 
 **/
protected function SaveOrUpdatetBGDJAction(paramObj:Object):void{
	var submitjson:String = JSON.encode(paramObj);
	AllAssetServiceRmt.SaveOrUpdateObject(submitjson,"net.chinanets.pojos.CnstAssetbgjlData","assetbgjlid","历史记录");
	AllAssetServiceRmt.addEventListener(ResultEvent.RESULT,dataSaveOrUpdateBGDJActionBack);
}
//保存数据回调方法
protected function dataSaveOrUpdateBGDJActionBack(rmtResult:ResultEvent):void{
	AllAssetServiceRmt.removeEventListener(ResultEvent.RESULT,dataSaveOrUpdateBGDJActionBack);
	if(rmtResult.result.toString().indexOf("[") > -1){return;	}
	var doResult:DoResult=ASJsonHelper.stringJsonConverToDoResult(rmtResult.result.toString());
	if(doResult!=null && doResult.nRetCode!=Errors.OK){
		this.resultmsg.text=doResult.strErrorInfo;
	}else{
		this.resultmsg.text="历史记录数据保存成功";
	}
	this.resultmsg.alpha=1;
	this.fadeEffect.stop();
	this.fadeEffect.play(null,false);
}	
/**
 * 删除当前数据的历史记录
 **/
protected function dataBGDJRemoveAction(curdataId:String):void{
	if(CommonMethod.isNullOrWhitespace(curdataId)){Alert.show("curdataId不能为空");}
	AllAssetServiceRmt.RemoveObject(curdataId,"CNST_ASSETBGJL_DATA","asetetid","变更历史记录");
	AllAssetServiceRmt.addEventListener(ResultEvent.RESULT,dataBGDJRemoveActionBack);
}
//删除当前数据回调方法
protected function dataBGDJRemoveActionBack(rmtResult:ResultEvent):void{
	AllAssetServiceRmt.removeEventListener(ResultEvent.RESULT,dataBGDJRemoveActionBack);
	var doResult:DoResult=ASJsonHelper.stringJsonConverToDoResult(rmtResult.result.toString());
	if(doResult!=null && doResult.nRetCode!=Errors.OK){
		this.resultmsg.text=doResult.strErrorInfo;
		return;
	}else{
		this.resultmsg.text="变更历史记录数据删除成功";
	}
}

