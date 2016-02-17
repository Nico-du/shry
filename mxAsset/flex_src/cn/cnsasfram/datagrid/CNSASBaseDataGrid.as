package cn.cnsasfram.datagrid{
	import flash.display.Sprite;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
	import mx.controls.DataGrid;
	
	/**
	 *自定义FLEX中DataGrid
	 *@author RLiuyong
	 */
	public class CNSASBaseDataGrid extends DataGrid{
		
		//自定义列表颜色
		private var _dgrowColorFunction:Function;
		//是否用户自定义
		private var _dgcustomed:Boolean;
		//用户自定义颜色
		private var _dgcustomedColor:uint=0;
		//单数列表颜色
		private var _dgOddRowColor:uint=0xF2F2F2;
		//双数列表颜色
		private var _dgDoubleRowColor:uint=0xFFFACD;
		
		public function CNSASBaseDataGrid(){
			super();
		}
		
		/**
		*重写父类指定行数据颜色,默认单行和偶数行颜色不同 
		*/
		override protected function drawRowBackground(s:Sprite,rowIndex:int,y:Number,height:Number,color:uint,dataIndex:int):void{
			if(dataIndex<this.getDataRowCount()){
				if(dataIndex%2==0){
					color=this._dgDoubleRowColor;
				}else{
					color=this._dgOddRowColor;
				}	
			}
			if(this._dgcustomed){
				if(this._dgrowColorFunction!=null){
					if(dataIndex<this.getDataRowCount()-1){
						var item:Object=this.selectedItem;
						color=this._dgrowColorFunction.call(this,item,color);
					}
				}else{
					if(this._dgcustomedColor!=0){
						if(dataIndex<this.getDataRowCount()-1){
							color=this._dgcustomedColor
						}
					}
				}
			}
			super.drawRowBackground(s,rowIndex,y,height,color,dataIndex);
		}
		
		/**
		 *获取数据的总行数 
		 */
		protected function getDataRowCount():int{
			var dataRowCount:int=0;
			if(this.dataProvider!=null){
				dataRowCount=ListCollectionView(this.dataProvider).length;	
			}
			return dataRowCount;
		}
		
		public function get dgrowColorFunction():Function
		{
			return _dgrowColorFunction;
		}

		public function set dgrowColorFunction(value:Function):void
		{
			_dgrowColorFunction = value;
		}

		public function get dgcustomed():Boolean
		{
			return _dgcustomed;
		}

		public function set dgcustomed(value:Boolean):void
		{
			_dgcustomed = value;
		}

		public function get dgcustomedColor():uint
		{
			return _dgcustomedColor;
		}

		public function set dgcustomedColor(value:uint):void
		{
			_dgcustomedColor = value;
		}

		public function get dgOddRowColor():uint
		{
			return _dgOddRowColor;
		}

		public function set dgOddRowColor(value:uint):void
		{
			_dgOddRowColor = value;
		}

		public function get dgDoubleRowColor():uint
		{
			return _dgDoubleRowColor;
		}

		public function set dgDoubleRowColor(value:uint):void
		{
			_dgDoubleRowColor = value;
		}
	}
}