<%@ page contentType="text/html;charset=UTF-8"%><%
	String code = "pc1122";//条形码内容
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
  function dojump(){
    var temp = document.getElementById('code').value;
  window.location="index.jsp?code="+temp;
  }
  

 </script>

</head>
<body topmargin="0px" leftmargin="0px" rightmargin="0px" bottommargin="0px">

<div id="viewpanel" align="center">
<form id ="form1">
<input name="code" type="text" id="code"/>
<input name="bequery" type="button" value="提交" style="cursor:hand;" onclick="dojump()">
<img src="<%=request.getContextPath() %>/barcode?msg=12345678&BARCODE_TYPE=code128" />

       <img src="<%=request.getContextPath()%>/barcode?msg=PC1234&BARCODE_TYPE=code128"  height="100px" width=300px/>
</form>
</div>
<!--script>nowprint();</script-->
</body>
</html>  
