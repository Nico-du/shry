package flex.util
{
	  import flash.errors.*;
	  import flash.events.*;
	  import flash.external.*;
	  import flash.net.URLRequest;
	  import flash.net.URLRequestMethod;
	  import flash.net.URLVariables;
	  import flash.net.navigateToURL;
	  
	  import mx.controls.DataGrid;
      
	public class OutExcel
	{
			public function OutExcel()
			{
			}
				
			public function convertDGToHTMLTable(dg:DataGrid):String {
				//Set default values
				var font:String = dg.getStyle('fontFamily');
				var size:String = dg.getStyle('fontSize');
				var str:String = '';
				var colors:String = '';
				var style:String = 'style="font-family:'+font+';font-size:'+size+'pt;"';				
				var hcolor:Array;
				
				//Retrieve the headercolor
				if(dg.getStyle("headerColor") != undefined) {
					hcolor = [dg.getStyle("headerColor")];
				} else {
					hcolor = dg.getStyle("headerColors");
				}				
				
				//Set the htmltabel based upon knowlegde from the datagrid
				str+= '<table width="'+dg.width+'" border="1"><thead><tr width="'+dg.width+'" style="background-color:#' +Number((hcolor[0])).toString(16)+'">';
				
				//Set the tableheader data (retrieves information from the datagrid header				
				for(var i:int = 1;i<dg.columns.length;i++) {
					colors = dg.getStyle("themeColor");
						
					if(dg.columns[i].headerText != undefined) {
						str+="<th "+style+">"+dg.columns[i].headerText+"</th>";
					} else {
						str+= "<th "+style+">"+dg.columns[i].dataField+"</th>";
					}
				}
				str += "</tr></thead><tbody>";
				colors = dg.getStyle("alternatingRowColors");
				
				//Loop through the records in the dataprovider and 
				//insert the column information into the table
				for(var j:int =0;j<dg.dataProvider.length;j++) {					
					str+="<tr width=\""+Math.ceil(dg.width)+"\">";
					for(var k:int=1; k < dg.columns.length; k++) {
						//Do we still have a valid item?						
						if(dg.dataProvider.getItemAt(j) != undefined && dg.dataProvider.getItemAt(j) != null) {
							//Check to see if the user specified a labelfunction which we must
							//use instead of the dataField
							if(dg.columns[k].labelFunction != undefined) {
								
								str += "<td width=\""+Math.ceil(dg.columns[k].width)+"\" "+style+">"+dg.columns[k].labelFunction(dg.dataProvider.getItemAt(j),dg.columns[k].dataField)+"</td>";
							} else {
								//Our dataprovider contains the real data
								//We need the column information (dataField)
								//to specify which key to use.
							
								str += "<td width=\""+Math.ceil(dg.columns[k].width)+"\" "+style+">"+dg.dataProvider.getItemAt(j)[dg.columns[k].dataField]+"</td>";
							}
						}
					}
					str += "</tr>";
				}
				str+="</tbody></table>";
			
				return str;
			}


			   public function loadDGInExcel(dg:DataGrid,url:String):void {
				
					//Pass the htmltable in a variable so that it can be delivered
					//to the backend script
					var variables:URLVariables = new URLVariables(); 
					variables.htmltable	= convertDGToHTMLTable(dg);
					
					//Setup a new request and make sure that we are 
					//sending the data through a post
					var u:URLRequest = new URLRequest(url);
					u.data = variables; //Pass the variables
					u.method = URLRequestMethod.POST; //Don't forget that we need to send as POST
					
					//Navigate to the script
					//We can use _self here, since the script will through a filedownload header
					//which results in offering a download to the user (and still remaining in you Flex app.)
				    navigateToURL(u,"_self");
			     } 
	}      
}