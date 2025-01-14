<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Production BOM Child List</title>
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
<h3 class="panel-title">Production BOM Child List</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div >
<table id="myTable" class="table">
 <thead style="background: #eaeafa"> 
        <tr>
          <th>Child Product</th>
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
 
  String pitem = strUtils.fString(request.getParameter("PITEM"));
  String citem = strUtils.fString(request.getParameter("CITEM"));
  String type  = strUtils.fString(request.getParameter("TYPE"));
  String opseq  = strUtils.fString(request.getParameter("OPSEQ"));
  String plant = (String)session.getAttribute("PLANT");
  
  
  String sBGColor = "",extcond="";
  List listQry = null;
  
  if(opseq.length()>0)
  {
	  extcond = " AND SEQNUM='"+opseq+"'";
  }

 try{
	 if(type.equalsIgnoreCase("KITWITHBOM"))
	 {
		 listQry = _ProductionBomUtil.getProdBomchilditemList(pitem,citem, plant, " AND BOMTYPE='KIT'");
	 }
	 else
	 {
		 listQry = _ProductionBomUtil.getProdBomchilditemList(pitem,citem, plant, " AND BOMTYPE='PROD'"); 
	 }
	  
	

  for (int i =0; i<listQry.size(); i++){
  Map map = (Map) listQry.get(i);
  sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
   String item         = strUtils.fString((String) map.get("CITEM"));
   String desc             = strUtils.fString((String) map.get("CDESC"));
   String detdesc         = strUtils.fString((String) map.get("CDETDESC"));
   String uom             = strUtils.fString((String) map.get("UOM"));
   
	 if(type.equalsIgnoreCase("MOVETOWIP")){%>
	 
	 <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.CITEM.value='<%=item%>'; window.opener.ValidateChildProduct();window.close();"><%=item%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=uom%></td>
	</TR>
	 
<% } else if(type.equalsIgnoreCase("WIPINVSUMMARY")){%>
 <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.CITEM.value='<%=item%>'; window.close();"><%=item%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=uom%></td>
	</TR>

<% } else if(type.equalsIgnoreCase("KITWITHBOM")){%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.CITEM.value='<%=item%>';window.opener.form.CDESC.value='<%=desc%>';window.opener.form.CDETDESC.value='<%=detdesc%>';window.opener.form.BATCH_1.value='NOBATCH'; window.close();"><%=item%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=uom%></td>
	</TR>
<%} else { %>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.CITEM.value='<%=item%>'; window.close();"><%=item%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=uom%></td>
	</TR>

<%}
  }

 }catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
   </tbody>
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