package cn.cnsasfram.helper{
	import cn.cnsasfram.entity.DoResult;
	
	import com.adobe.serialization.json.JSON;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;

	/**
	 *Flex中common辅助类
	 *@author dzj
	 */
	public class ASCommonHelper{
		
		public function ASCommonHelper(){
		}
		
		//控件显示或隐藏
		public static function controlVisible(tempControl:UIComponent,isVisible:Boolean):void{
			if(isVisible){
				tempControl.visible=true;
				tempControl.includeInLayout=true;
			}else{
				tempControl.visible=false;
				tempControl.includeInLayout=false;
			}
		}
		
		//向array新增selected属性
		public static function addArraySelectedPro(tempArrayCollection:ArrayCollection):ArrayCollection{
			if(tempArrayCollection==null || tempArrayCollection.length<1){
				return null;
			}
			for(var i:int=0;i<tempArrayCollection.length;i++){
				tempArrayCollection.getItemAt(i).selected=false;
			}
			return tempArrayCollection;
		}
		
	}
}