<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="../css/style.css">
 
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
 <link rel="stylesheet" href="../jsp/dist/css/bootstrap.min.css"/>
  <!-- DataTables -->
  <link rel="stylesheet" href="../jsp/dist/css/lib/datatables.min.css"/>
  <!-- jQuery 3 -->
  <script type="text/javascript" src="../jsp/dist/js/jquery.min.js"></script>
  <script type="text/javascript" src="../jsp/dist/js/jquery.dataTables.min.js"></script>
	<!-- jQuery UI -->
	<script src="../jsp/dist/js/jquery-ui-1.12.1.js"></script>
</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Product List</h3> 
</div>
</div>
<body>
<form method="post" name="form1">
<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa;font-size:12">
    <TR>
      <TH >Product ID</TH>
       <TH >Product Description</TH>
        
    </TR>
    </thead>
    <tbody>
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
StrUtils strUtils = new StrUtils();
ItemMstUtil itemUtil = new ItemMstUtil();
String plant =(String)session.getAttribute("PLANT");
System.out.println("plant"+plant);
String sItem = StrUtils.fString(request.getParameter("ITEM")).trim();
String sItemDesc = StrUtils.fString(request.getParameter("DESC")).trim();
String sExtraCond = StrUtils.fString(request.getParameter("COND")).trim();
String TYPE = strUtils.fString(request.getParameter("TYPE"));
if(sItemDesc.length()>0){
sItemDesc = new StrUtils().InsertQuotes(sItemDesc);
sItemDesc= " AND ITEMDESC like '%"+sItemDesc+"%'";
}
sItemDesc =sItemDesc+sExtraCond ;
ArrayList listQry = itemUtil.getMasterProductList(sItem,plant,sItemDesc);
try
{
  for(int i =0; i<listQry.size(); i++) {
 	Map m=(Map)listQry.get(i);
 	String item   =  strUtils.fString((String)m.get("ITEM"));
    String itemdesc    =  strUtils.fString((String)m.get("ITEMDESC"));
if(TYPE.equalsIgnoreCase("WIPSUMMARY")){%>
<TR>
		<td class="main2" style="font-weight: bold;font-size:12" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.ITEM.value='<%=item%>';window.close();"><%=item%></a></td>
		<td align="left" class="main2" style="font-size:11">&nbsp;<%=itemdesc%></td>
		
	</TR>
   <%} else {%>
   <TR>
		<td class="main2" style="font-weight: bold;font-size:12" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.ITEM.value='<%=item%>';window.close();"><%=item%></a></td>
		<td align="left" class="main2" style="font-size:11">&nbsp;<%=itemdesc%></td>
		
	</TR>
<%}
		}
	}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");
	}
%>
       
  </tbody>
</table>
<div class="text-center">       
        <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
	     </div>  

</div>
</form>
</body>
</html>

<script>
$(document).ready(function(){
	$('#myTable').dataTable();
    
    $('[data-toggle="tooltip"]').tooltip();
});
</script>


