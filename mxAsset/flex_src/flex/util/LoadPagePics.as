
import flex.util.CommonXMLData;

import mx.controls.Image;
import mx.core.Application;

public function loadPictures():void{
	CommonMethod.loadTablePictures(this.tempObjBeanName,idColumnName,dataId,this,loadPicturesBack);
}
private function loadPicturesBack(result:Object):void{
	fyts.removeAllChildren();
	var existedFiles:ArrayCollection = result as ArrayCollection;
	if( !(existedFiles == null || existedFiles.length < 1)){
		for(var idx:int=0;idx<existedFiles.length;idx++){
			var eachObj:Object = existedFiles.getItemAt(idx);
			var eachImg:Image = new Image();
			eachImg.toolTip = eachObj.filename +"."+ eachObj.filetype;
			if(!isPICFile(eachObj.filepath)){//非图片文件显示的缩略图标
				eachImg.source= "style/flex_skins/file_pic.png";
				eachImg.addEventListener(MouseEvent.CLICK,addMoreBtnClick);
			}else{
			    eachImg.source= CommonXMLData.UploadFile_BasePath + eachObj.filepath; 
				eachImg.addEventListener(MouseEvent.CLICK,CommonMethod.doshowZoomSelectedPic(this.tempObjBeanName,idColumnName,this.dataId,FlexGlobals.topLevelApplication));
			}
			eachImg.width = aboutheight*5;
			eachImg.height = aboutheight*5;
			eachImg.data = idx;
			eachImg.addEventListener(MouseEvent.MOUSE_OVER, mouseOverFunction);
			eachImg.addEventListener(MouseEvent.MOUSE_OUT, mouseOutFunction);
			fyts.addChild(eachImg);
		}
			if(this.hasOwnProperty("nvPageFyFrom1Group1")){
			PICROW.height = aboutheight * 5;
			fyts.height = aboutheight * 5;
			}
	}else{
		if(this.hasOwnProperty("nvPageFyFrom1Group1")){
			PICROW.height = aboutheight+2;
			fyts.height = aboutheight+2;
		}
	}
	if(!this.hasOwnProperty("nvPageFyFrom1Group1")){
		var addMoreBtn:Button = new Button();
		addMoreBtn.label="添加文件";
		addMoreBtn.addEventListener(MouseEvent.CLICK,addMoreBtnClick);
		fyts.addChild(addMoreBtn);
	}
}

private function isPICFile(filepath:String):Boolean{
	var fleNameSuffix:String = filepath.substr(filepath.lastIndexOf(".")+1);
	if(fleNameSuffix.toLowerCase() == "bmp" ||  fleNameSuffix.toLowerCase() == "jpg" || fleNameSuffix.toLowerCase() == "jpeg"
		|| fleNameSuffix.toLowerCase() == "png" || fleNameSuffix.toLowerCase() == "gif" ){
		//bmp	jpg jpeg png gif
		return true;
	}
	return false;
}

private function addMoreBtnClick(evt:Event):void{
	CommonMethod.modifyUploadFile(this,tempObjBeanName,idColumnName,this.dataId);
}
private function mouseOverFunction(evt:Event):void{
	Mouse.cursor = MouseCursor.BUTTON;
}
private function mouseOutFunction(evt:Event):void{
	Mouse.cursor = MouseCursor.AUTO;
}