package cn.cnsasfram.dashline
{
	import flash.display.Graphics;
	import flash.geom.Point;
	
	/**
	 * 一些绘图相关的方法
	 * @author lip
	 */
	public class GraphicUtils
	{
		public function GraphicUtils()
		{
		}
		
		
		/**
		 * 画虚线
		 * @param graphics  this.graphics
		 * @param pFrom 起点
		 * @param pTo 终点
		 * @param length 实线段的长度
		 * @param gap 实线段的间距
		 * @param thickness 线的宽度
		 * @param color 线的颜色
		 */
		public static function drawDashed(graphics:Graphics, pFrom:Point, pTo:Point, length:Number = 5, gap:Number = 5, thickness:Number = 1, color:uint = 0):void
		{
			var max:Number = Point.distance(pFrom, pTo);
			var l:Number = 0;
			var p3:Point;
			var p4:Point;
			graphics.lineStyle(thickness, color);
			while (l < max)
			{
				p3 = Point.interpolate(pTo, pFrom, l / max);
				l += length;
				if (l > max)
					l = max;
				p4 = Point.interpolate(pTo, pFrom, l / max);
				graphics.moveTo(p3.x, p3.y)
				graphics.lineTo(p4.x, p4.y)
				l += gap;
			}
		}
	}
}
