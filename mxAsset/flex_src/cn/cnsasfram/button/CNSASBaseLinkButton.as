package cn.cnsasfram.button{
	import mx.controls.LinkButton;
	
	/**
	 *自定义FLEX中LinkButton
	 *@author RLiuyong
	 */
	public class CNSASBaseLinkButton extends LinkButton{
		
		//按钮背景颜色
		private var _backGroundColor:uint=0;
		
		public function CNSASBaseLinkButton(){
			super();
		}
		
		//重写初始化方法
		override protected function initializeAccessibility():void{
			super.initializeAccessibility();
			this.setBackGroundColor(this._backGroundColor);
		}
		
		//设置背景颜色
		public function setBackGroundColor(tempBackGroundColor:uint):void{
			if(tempBackGroundColor==0){
				return;
			}
			this.graphics.beginFill(tempBackGroundColor);
			this.graphics.drawRoundRect(0,0,this.width,this.height,2,20);		
			this.graphics.endFill();
		}
		
		//取消设置背景颜色
		public function resetBackGroundColor():void{
			this.graphics.clear();	
		}
		
		public function get backGroundColor():uint{
			return _backGroundColor;
		}

		public function set backGroundColor(value:uint):void{
			_backGroundColor = value;
		}
	}
}