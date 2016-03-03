package cn.cnsasfram.dashline
{
	import flash.display.Graphics;
	import flash.geom.Point;
	import flash.text.TextField;
	
	import mx.charts.chartClasses.CartesianChart;
	import mx.charts.chartClasses.CartesianTransform;
	import mx.charts.chartClasses.ChartElement;
	import mx.charts.chartClasses.ChartState;
	import mx.charts.chartClasses.IAxis;
	
	public class DashedLine extends ChartElement
	{
		public function DashedLine() 
		{
			super();
		}
		/**
		 *垂直虚线是否可见
		 * */
		public var xLineVisible:Boolean = false;
		/**
		 *水平虚线是否可见
		 * */
		public var yLineVisible:Boolean = false;
		/**
		 *水平虚线的Y值 
		 **/
		private var _yValue:Number = NaN;
		/**
		 *垂直虚线的X值 
		 **/
		private var _xValue:Number = NaN;
		/**
		 * 实线部分的长度
		 * @default 10
		 */
		public var length:Number = 5;
		
		/**
		 * 空白部分的长度
		 * @default 5
		 */
		public var gap:Number = 3;
		
		/**
		 * 线条的宽度
		 * @default 1
		 */
		public var lineThickness:Number = 1;
		
		/**
		 * 线条的颜色
		 * @default 黑色
		 */
		public var lineColor:uint = 0;
		
		/**
		 * 该线所对应的数值名称(平均值，最大值等等)
		 * @default 
		 */
		private var _displayName:String;
		
		
		/**
		 * 该线对应的y值
		 */
		public function get yValue():Number
		{
			return _yValue;
		}
		public function set yValue(value:Number):void
		{
			_yValue = value;
			invalidateDisplayList();
		}
		/**
		 * 该线对应的x值
		 */
		public function get xValue():Number
		{
			return _xValue;
		}
		public function set xValue(value:Number):void
		{
			_xValue = value;
			invalidateDisplayList();
		}
		
		
		
		public function get displayName():String
		{
			return _displayName;
		}
		
		/**
		 * @private
		 */
		public function set displayName(value:String):void
		{
			_displayName = value;
			invalidateDisplayList();
		}
		
		
		 protected var label:TextField;
		
		override protected function createChildren():void
		{
			// TODO Auto Generated method stub
			super.createChildren();
			
			if(!label)
			{
				label = new TextField();
				label.mouseEnabled = false;
				addChild(label);
			}
		}
		
		/**
		 *关键方法,实时画虚线的方法 
		 **/
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			if (!chart||
				chart.chartState == ChartState.PREPARING_TO_HIDE_DATA ||
				chart.chartState == ChartState.HIDING_DATA)
			{
				return;
			}
			
			var g:Graphics = this.graphics;
			g.clear();
			
			if(yLineVisible){
				// 如果没有设置数据，不显示
				if(isNaN(yValue))
				{
					return;
				}
				
				var w:Number = unscaledWidth;
				
				var y:Number = dataToLocal(0, yValue).y;
				
				var pFrom:Point = new Point(0, y);
				var pTo:Point = new Point(w, y);
				
				cn.cnsasfram.dashline.GraphicUtils.drawDashed(g, pFrom, pTo, this.length, this.gap, this.lineThickness, this.lineColor);
				
				label.text = (displayName ? (displayName + " : ") : "") + yValue;
				label.x = 5;
				label.y = y;
			}
			
			if(xLineVisible){
				// 如果没有设置数据，不显示
				if(isNaN(xValue))
				{
					return;
				}
				
				var h:Number = unscaledHeight;
				
				var x:Number = dataToLocal(xValue,0).x;
				
				var pFrom:Point = new Point(x, 5);
				var pTo:Point = new Point(x, h);
				
				cn.cnsasfram.dashline.GraphicUtils.drawDashed(g, pFrom, pTo, this.length, this.gap, this.lineThickness, this.lineColor);
				label.text = (displayName ? (displayName + " : ") : "") + xValue;
				label.y = 5;
				label.x = x;
			}
			
		}
		
		
		// 这个方法复制自LineSeries
		override public function dataToLocal(... dataValues):Point
		{
			var data:Object = {};
			var da:Array /* of Object */ = [ data ];
			var n:int = dataValues.length;
			
			if (n > 0)
			{
				data["d0"] = dataValues[0];
				dataTransform.getAxis(CartesianTransform.HORIZONTAL_AXIS).
					mapCache(da, "d0", "v0");
			}
			
			if (n > 1)
			{
				data["d1"] = dataValues[1];
				dataTransform.getAxis(CartesianTransform.VERTICAL_AXIS).
					mapCache(da, "d1", "v1");           
			}
			
			dataTransform.transformCache(da,"v0","s0","v1","s1");
			
			return new Point(data.s0 + this.x,
				data.s1 + this.y);
		}
	}
}
