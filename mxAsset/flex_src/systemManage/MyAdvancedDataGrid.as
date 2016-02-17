package systemManage
{
	import flash.display.Sprite;
	
	import mx.controls.AdvancedDataGrid;

	public class MyAdvancedDataGrid extends AdvancedDataGrid
	{
		public function MyAdvancedDataGrid()
		{
			super();
		}
		
		override protected function drawRowBackground(s:Sprite, rowIndex:int, y:Number, height:Number, color:uint, dataIndex:int):void {
			var list:XMLList = new XMLList(dataProvider)
        	if(dataIndex < list.length() && list[dataIndex].@sfAdd=="")color = 0xFF6600
			super.drawRowBackground(s,rowIndex,y,height,color,dataIndex);
		}
		
	}
}