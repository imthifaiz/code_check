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



<title>Order in Product list</title>
<link rel="stylesheet" href="css/style.css">

</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Order In Product List</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div>
<table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
		<TH>Order No</TH>
		<TH>Customer Name</TH>
		<TH>Reference No</TH>
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
   // ItemUtil itemUtil = new ItemUtil();
    DOUtil _DOUtil = new DOUtil();
    _DOUtil.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String dono = StrUtils.fString(request.getParameter("DONO"));
   
   

    String sBGColor = "";
   try{
   
   
     Hashtable ht=new Hashtable();
     String extCond="";
     session = request.getSession();
     String plant=(String)session.getAttribute("PLANT");
     String SalesPickIssue = StrUtils.fString(request.getParameter("OpenForPick"));
     
     ht.put("PLANT",plant);
     if(dono.length()>0) extCond=" AND plant='"+plant+"' and dono like '"+dono+"%'";
     if("YES".equalsIgnoreCase(SalesPickIssue)){
    	 extCond = extCond+ "and (ORDER_STATUS = 'OPEN' OR ORDER_STATUS='PARTIALLY PROCESSED')";
     }
     extCond=extCond+" and status <>'C' and  dono in (select a.dono from "  + plant + "_" + "DOHDR a,"+ plant + "_" + "DODET  b where a.DONO=b.DONO  and b.LNSTAT <>'C' and  b.pickstatus <> 'C') ";
     extCond=extCond+" order by CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date) desc";
     
     
     ArrayList listQry = _DOUtil.getDoHdrDetails("dono,custName,custCode,jobNum,status",ht,extCond);
     
     for(int i =0; i<listQry.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
        Map m=(Map)listQry.get(i);
        dono     = (String)m.get("dono");
        String custName    = StrUtils.replaceCharacters2Send((String)m.get("custName"));
        String custCode    = (String)m.get("custCode");
        String jobNum    = (String)m.get("jobNum");
        String status      =  (String)m.get("status");
    %>
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.DONO.value='<%=dono%>';
                     window.opener.form.JOB_NUM.value='<%=jobNum%>';
                     window.opener.form.CUST_NAME.value='<%=StrUtils.insertEscp(custName)%>';
                     window.close();"><%=dono%></a></td>
		<td align="left" class="main2">&nbsp;<%=StrUtils.replaceCharacters2Recv(custName)%></td>
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