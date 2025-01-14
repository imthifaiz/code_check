<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Kitting Parent List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

<SCRIPT LANGUAGE="JavaScript">



</Script>

<link rel="stylesheet" href="css/style.css">
</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Kitting Parent List</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div >
<table id="myTable" class="table">
 <thead style="background: #eaeafa"> 
        <tr>
          <th>Parent Product</th>
          <th>Description</th>
          <th>UOM</th>
      </tr>
    </thead>
    <tbody>

<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
StrUtils strUtils = new StrUtils();
ProductionBomUtil _ProductionBomUtil = new ProductionBomUtil();
 
  DateUtils  dateUtil = new DateUtils();
  _ProductionBomUtil.setmLogger(mLogger);
 
 
  String pitem = strUtils.fString(request.getParameter("ITEM"));
  String type  = strUtils.fString(request.getParameter("TYPE"));
  String plant = (String)session.getAttribute("PLANT");
  
  String sBGColor = "";
  List listQry = null;

 try{
	 
	listQry = _ProductionBomUtil.getKitpitemList(pitem, plant, " ");
	 
  	for (int i =0; i<listQry.size(); i++){
  	Map map = (Map) listQry.get(i);
  	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
   		String item         = strUtils.fString((String) map.get("PARENT_PRODUCT"));
   		String desc             = strUtils.fString((String) map.get("PDESC"));
   		String detdesc         = strUtils.fString((String) map.get("PDETDESC"));
   		String uom             = strUtils.fString((String) map.get("UOM"));
   
	%>
	<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.ITEM.value='<%=item%>';window.opener.form.DESC.value='<%=desc%>';window.opener.form.DETDESC.value='<%=detdesc%>';window.opener.form.BATCH_0.value='NOBATCH';window.close();"><%=item%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=uom%></td>
	</TR>

<%

}
 }catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
   </table>
 <br>
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
    /* $('#myModal1').click(function(){
    	if(document.getElementById("alertValue").value!="")
    	{
    		//$("#myModal").modal();
    		document.getElementById('myModal').style.display = "block";
    	}
    }); */
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script>