package shry_tpgl
{
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	import mx.containers.Panel;
	import mx.controls.Image;
	import mx.core.Container;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;

	public class ImgMagnifier extends Canvas
	{
		private var _multiple:Number = 2; //放大倍数，默认两倍
		private var _glassHeight:Number = 550;
		private var _glassWidget:Number = 550;
		private var bigGlass:Panel;
		private var currentImg:Image;
		private var bigImg:Image;
		private var lockPopup:Boolean = false
		public var showbigGlass:Boolean = true;
		//设置放大倍数
		public function set multiple(value:Number):void{
			_multiple = value;
		}
		
		public function get multiple():Number{
			return _multiple;
		}
		
		//设置镜片高度
	    public function set glassHeight(value:Number):void{
            _glassHeight = value;
        }
        
        public function get glassHeight():Number{
            return _glassHeight;
        }
        
        //设置镜宽度
        public function set glassWidth(value:Number):void{
            _glassWidget = value;
        }
        
        public function get glassWidth():Number{
            return _glassWidget;
        }
        
        //设置镜片样式
        public function set glassStyleName(value:String):void{
            this.bigGlass.styleName = value;
        }
        
        public function setGlassStyle(stylrProp:String,newValue:Object):void{
            this.bigGlass.setStyle(stylrProp,newValue)
        }

		public function ImgMagnifier()
		{
			super();
			this.addEventListener(FlexEvent.CREATION_COMPLETE,creationCompleteHandle);
			bigGlass = new Panel();
			//glass 默认属性设置
			bigGlass.verticalScrollPolicy = "off";
			bigGlass.horizontalScrollPolicy = "off"
			bigGlass.mouseEnabled = false;
			bigGlass.mouseFocusEnabled = false;
			bigGlass.mouseChildren =false;
			bigGlass.setStyle("borderStyle","solid")
			bigGlass.setStyle("headerHeight", "0");
			bigGlass.setStyle("paddingTop", "0");
			bigGlass.setStyle("borderThicknessLeft", "0");
			bigGlass.setStyle("borderThicknessRight", "0");
			bigGlass.setStyle("borderThicknessHeight", "0");
			bigGlass.setStyle("borderThicknessBottom", "0");
			bigGlass.setStyle("cornerRadius","0");
			bigGlass.setStyle("paddingLeft","0");
			
		}
		
		//递归找出所有image
        private function registerEventsForImages(container:Container):void{
            for(var i:int=0; i<container.numChildren; i++){
                if(container.getChildAt(i) is Image){
                    container.getChildAt(i).addEventListener(MouseEvent.MOUSE_MOVE,mouseMoveHandle);
                    container.getChildAt(i).addEventListener(MouseEvent.MOUSE_OUT,mouseOutHandle);
                }else if(container.getChildAt(i) is Container){
                    registerEventsForImages(container.getChildAt(i) as Container)
                }
                
            }
        }
        
		//将所有Image对象注册mouseMove与mouseOut handle
		protected function creationCompleteHandle(e:Event):void{
			for(var i:int=0; i<this.numChildren; i++){
                if(this.getChildAt(i) is Image){
                    this.getChildAt(i).addEventListener(MouseEvent.MOUSE_MOVE,mouseMoveHandle);
                    this.getChildAt(i).addEventListener(MouseEvent.MOUSE_OUT,mouseOutHandle);
                }else if(this.getChildAt(i) is Container){
                    registerEventsForImages(this.getChildAt(i) as Container)
                }
                
            } 
		}
		
		//划出图片时不显示镜片
		protected function mouseOutHandle(e:MouseEvent):void{
			bigGlass.visible = false;
			bigGlass.removeAllChildren()
			currentImg = new Image();
		    bigImg = null;
		} 
		
		//如果目标是Image，开始放大
		protected function mouseMoveHandle(e:MouseEvent):void{
			if((e.currentTarget is Image) && showbigGlass){
				if(currentImg != e.currentTarget){
					this.initGlass(e.currentTarget as Image)
				}		
				magnify();
			}
			
		}
		
		//放大
		private function magnify():void{
			if(!bigGlass.visible){
                      bigGlass.visible = true
                }
            var currentX:Number = currentImg.mouseX;
            var currentY:Number = currentImg.mouseY;
            var newX:Number = bigImg.width*currentX/currentImg.width
            var newY:Number = bigImg.height*currentY/currentImg.height
            bigImg.x = -newX + bigGlass.width/2;
            bigImg.y = -newY + bigGlass.height/2;
            var point:Point =  this.localToGlobal(new Point(this.mouseX,this.mouseY))
            bigGlass.x = point.x -bigGlass.width/2
            bigGlass.y = point.y -  bigGlass.height/2;
		}
		
		//初始化镜片，与镜片中放大的图片
		private function initGlass(target:Image):void{
			this.currentImg = target;
            bigImg = new Image();
            bigImg.source = currentImg.source;
            bigImg.width = currentImg.width*multiple;
            bigImg.height = currentImg.height*multiple;
            bigGlass.addChild(bigImg);
            bigGlass.width = _glassWidget;
            bigGlass.height = _glassHeight;
            if(!lockPopup){
                PopUpManager.addPopUp(bigGlass,this);
                lockPopup = true
            }
		}
		
	}
}