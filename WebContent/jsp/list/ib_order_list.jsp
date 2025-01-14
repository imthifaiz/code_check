<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Purchase Order List</title>
<link rel="stylesheet" href="../css/style.css">

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
        <tr>
          <th>Order Number</th>
          <th>Supplier Name</th>
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

    POUtil itemUtil = new POUtil();
    itemUtil.setmLogger(mLogger);
   // StrUtils strUtils = new StrUtils();
    session= request.getSession();
    String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
    String pono = StrUtils.fString(request.getParameter("PONO"));
    String status = StrUtils.fString(request.getParameter("STATUS"));
    String afrmDate=StrUtils.fString(request.getParameter("FROMDATE"));
    String atoDate=StrUtils.fString(request.getParameter("TODATE"));
    String fdate="",tdate="";

    if (afrmDate.length() > 5)
		fdate = afrmDate.substring(6) + "-"+ afrmDate.substring(3, 5) +  "-"+ afrmDate.substring(0, 2);
	if (atoDate == null)
		atoDate= "";
	else
		atoDate= atoDate.trim();
	if (atoDate.length() > 5)
		 tdate = atoDate.substring(6) +  "-"+ atoDate.substring(3, 5)+  "-"+ atoDate.substring(0, 2);
     String sBGColor = "",dtCondStr="",extCond="";
    
 	 dtCondStr =    "  AND  ISNULL(recvdate,'')<>'' AND CAST((SUBSTRING(recvdate, 7, 4) + '-' + SUBSTRING(recvdate, 4, 2) + '-' + SUBSTRING(recvdate, 1, 2)) AS date)"; 
 	
     if (fdate.length() > 0) {
		  extCond = extCond + dtCondStr + "  >= '" 
				+ fdate
				+ "'  ";
		if (atoDate.length() > 0) {
			extCond = extCond +dtCondStr+ " <= '" 
			+  tdate
			+ "'  ";
		}
	} else {
		if (tdate.length() > 0) {
			extCond = extCond +dtCondStr+ " <= '" 
			+ tdate
			+ "'  ";
		}
	}   
    try{
   
     Hashtable ht=new Hashtable();
     ht.put("PLANT",plant);
     extCond=extCond  + " and ISNULL(recvdate,'')<>'' and plant='"+plant+"' and pono like '"+pono+"%' and pono in(select pono from [" + plant + "_" + "POHDR" + "] where status like '"+status+"%' and status<>'N') and tran_type='IB'";
     extCond=extCond+" order by pono desc";
     ArrayList listQry = itemUtil.getIBRecvList(" distinct pono,cname",ht,extCond);
     for(int i =0; i<listQry.size(); i++) {
      sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
      Map m=(Map)listQry.get(i);
      pono     = (String)m.get("pono");
      String custName    = (String)m.get("cname");
    %>
    
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.PONO.value='<%=pono%>';
                     window.close();"><%=pono%></a></td>
		<td align="left" class="main2">&nbsp;<%=StrUtils.replaceCharacters2Recv(custName)%></td>
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