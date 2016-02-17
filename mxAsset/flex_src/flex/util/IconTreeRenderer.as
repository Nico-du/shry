package flex.util
{
	import flash.xml.*;
	import mx.collections.*;
	import mx.controls.Alert;
	import mx.controls.listClasses.*;
	import mx.controls.Image;
	import mx.controls.treeClasses.*;

	public class IconTreeRenderer extends TreeItemRenderer
	{
		protected var myImage:Image;
		private var imageWidth:Number = 16;
		private var imageHeight:Number = 16;
		private static var defaultImg:String = "images/computer.gif";
		public function IconTreeRenderer()
		{
			super();
		}
		
		override protected function createChildren():void {
			super.createChildren();
			myImage = new Image();
			myImage.source = defaultImg;
			myImage.width = imageWidth;
			myImage.height = imageHeight;
			myImage.setStyle("verticalAlign", "middle");
			addChild(myImage);
		}
		override public function set data(value:Object):void {
			super.data = value;
			var imageSource:String = value.@icon;
			if(imageSource!="")
			{
				myImage.source = "images/"+imageSource;
			} else {
				myImage.source = defaultImg;
			} 
		}
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);  
			if(super.data !=null)  
             {  
                 if (super.icon != null)  
                {  
                     myImage.x = super.icon.x;  
                     myImage.y = 2;  
                     super.icon.visible=false;  
                 }  
                 else  
                {  
                     myImage.x = super.label.x;  
                     myImage.y = 2;  
                     super.label.x = myImage.x + myImage.width + 17;  
            	}
			}
		}
	}
}