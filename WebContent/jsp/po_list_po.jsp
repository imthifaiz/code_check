<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<html>
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
 <link rel="stylesheet" href="../jsp/dist/css/bootstrap.min.css"/>
  <!-- DataTables -->
  <link rel="stylesheet" href="../jsp/dist/css/lib/datatables.min.css"/>
  <!-- jQuery 3 -->
  <script type="text/javascript" src="../jsp/dist/js/jquery.min.js"></script>
  <script type="text/javascript" src="../jsp/dist/js/jquery.dataTables.min.js"></script>
	<!-- jQuery UI -->
	<script src="../jsp/dist/js/jquery-ui-1.12.1.js"></script>

<title>Purchase Order List</title>
<link rel="stylesheet" href="css/style.css">
</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Purchase Order List</h3> 
</div>
</div>
<body>
<form method="post" name="form1">
<div>
<table id="myTable" class="table">
    <thead style="background: #eaeafa">
	<TR >
		<TH >Order Number </TH>
		<TH >Supplier Name</TH>
		<TH >Order Date</TH>
		<TH >Reference Number</TH>
		<TH>Status</TH>
	</TR>
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

    POUtil itemUtil = new POUtil();
    StrUtils strUtils = new StrUtils();
    itemUtil.setmLogger(mLogger);
   // StrUtils strUtils = new StrUtils();
    session= request.getSession();
    String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
    String pono = StrUtils.fString(request.getParameter("PONO"));
    String ProductOrder = StrUtils.fString(request.getParameter("OpenForReceipt"));

    String sBGColor = "";
    try{
   
     Hashtable ht=new Hashtable();
     String extCond="";
     ht.put("PLANT",plant);
     if(pono.length()>0) extCond=" AND plant='"+plant+"' and pono like '"+pono+"%' ";
     if("YES".equalsIgnoreCase(ProductOrder)) extCond += " and (ORDER_STATUS ='OPEN' OR ORDER_STATUS = 'PARTIALLY PROCESSED')";
    	 
     extCond=extCond+" and status <> 'C' "; //not by Draft - azees
     extCond=extCond+" order by CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date) desc";
     ArrayList listQry = itemUtil.getPoHdrDetails("pono,custName,custcode,jobNum,status,collectiondate",ht,extCond);
     for(int i =0; i<listQry.size(); i++) {
   
      sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
      Map m=(Map)listQry.get(i);
      pono     = (String)m.get("pono");
      String custName    = StrUtils.replaceCharacters2Send((String)m.get("custName"));
      String custcode    = (String)m.get("custcode");
      String orderdate      =  (String)m.get("collectiondate");
      String jobNum    = (String)m.get("jobNum");
      String status      =  (String)m.get("status");
    
  %>
  <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.PONO.value='<%=pono%>';
                     window.opener.form.JOB_NUM.value='<%=jobNum%>';
                     window.opener.form.CUST_NAME.value='<%=StrUtils.insertEscp(custName)%>';
                     window.opener.form.CUST_CODE.value='<%=custcode%>';
                     window.opener.form.CUST_CODE1.value='<%=custcode%>';
                     window.close();"><%=pono%></a></td>
		<td align="left" class="main2">&nbsp;<%=StrUtils.replaceCharacters2Recv(custName)%></td>
		<td align="left" class="main2">&nbsp;<%=orderdate%></td>
		<td align="left" class="main2">&nbsp;<%=jobNum%></td>
		<td align="left" class="main2">&nbsp;<%=status%></td>
	</TR>
  
	
	<%
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
	$('[data-toggle="tooltip"]').tooltip();
	$('#myTable').dataTable({
		"order": []    
} );
});
</script>