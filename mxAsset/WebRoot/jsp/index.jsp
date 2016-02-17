<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="net.chinanets.service.CommonService"%>
<%@page import="net.chinanets.service.imp.CommonServiceImp"%>
<%@page import="java.util.List"%>
<%
	//String code = "pc1122";//条形码内容
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<OBJECT classid=CLSID:8856F961-340A-11D0-A96B-00C04FD705A2 height=0 id=WB width=0></OBJECT>  



<script language="javascript">
<!--
NS4 = (document.layers) ? 1 : 0;
visble_property_prefix = (NS4) ? "document.layers." : "";
visble_property_suffix = (NS4) ? ".visibility" : ".style.display";
visble_property_true = (NS4) ? "show" : "block";
visble_property_false = (NS4) ? "hide" : "none";
visble_property_printview = visble_property_prefix + "viewpanel" + visble_property_suffix;

	function nowprint() {
		window.print();
		
	}
	function window.onbeforeprint() {
		eval(visble_property_printview + " = \"" + visble_property_false + "\"");
	}
	function window.onafterprint() {
		eval(visble_property_printview + " = \"" + visble_property_true + "\"");
	}
    

		function doPrintSetup(){  
		//打印设置  
		WB.ExecWB(8,1)  
		}  


//-->
</script>
</head>
<body topmargin="0px" leftmargin="0px"  rightmargin="0px" bottommargin="0px" >
<%		

	if(request.getParameter("code")!=null && request.getParameter("code")!="" ){
		StringBuffer barCode = new StringBuffer();
		barCode.append("<img  src='");
		barCode.append(request.getContextPath());
		barCode.append("/CreateBarCode?code=");
		barCode.append(request.getParameter("code"));
		barCode.append("&barType=CODE128&checkCharacter=n&checkCharacterInText=n'>");
		out.println(barCode.toString());
	}
	
%>
<div id="viewpanel" align="center">
<form action="../barcode/CodeServlet">
	<input name="bequery" type="button" value="打  印" style="cursor:hand;" onclick="nowprint();" >
	<input name="all" type="submit" value="打印所有" style="cursor:hand;" >

	<input type="button" value="打印设置" onclick="doPrintSetup()" id="button1" name="button1">
</form>
</div>
<!--script>nowprint();</script-->
</body>
</html>  
