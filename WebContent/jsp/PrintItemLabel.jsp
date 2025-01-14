<%@ include file="header.jsp"%>
<%@ page contentType="text/html;charset=windows-1252"%>




<script type="text/javascript">
function printpage()
  {
  window.print()
  }
</script>

<%
String fontStyle="font-family:Arial Black;color: red;font-size:40px";

String item= request.getParameter("ITEM");
item="*"+item+"*";
String itemDesc=request.getParameter("ITEMDESC");
//String item="*12345*";
//String itemDesc="Item Description";
%>

<html>
<head>
   <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
</head>
<body onclick="printpage()" >

<table  cellspacing="0" cellpadding="0" border="1" width="20%">
<tr>
    <td>Item Number</td>
    <td style="<%=fontStyle%>"><%=item%></td>
</tr>
<tr>
    <td>Item Description</td>
     <td style="font-family:Free 3 of 9 Extended;color: red;font-size:40px "><%=itemDesc%></td>
</tr>
</table>

</body>
</html>