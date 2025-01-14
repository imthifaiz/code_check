<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<html>

<head>
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


<title>Consignment Order List </title>
<link rel="stylesheet" href="../css/style.css">
</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Consignment Order List</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div>
   <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH >Order No </TH>
      <TH>Customer Name</TH>
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

    TOUtil _TOUtil = new TOUtil();
    _TOUtil.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String tono = strUtils.fString(request.getParameter("TONO"));
    String type = strUtils.fString(request.getParameter("TYPE"));
   
    String sBGColor = "";
   try{
   	  	Hashtable ht=new Hashtable();
     	String extCond="";
     	session = request.getSession();
     	String plant=(String)session.getAttribute("PLANT");
     	ht.put("PLANT",plant);
  
     
     	if(tono.length()>0) extCond=" AND plant='"+plant+"' and tono like '"+tono+"%'";
     		if(type.equalsIgnoreCase("Reverse")){
     			extCond=extCond+" and status ='O'";	
     		}
     		else{
     			extCond=extCond+" and status <>'N'";	
     		}
     		extCond=extCond+" and tono like 'C%'";	
     		//extCond=extCond+" and status <>'C'";
        	extCond=extCond+" order by CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date) desc";
    		ArrayList listQry = _TOUtil.getToHdrDetails("tono,custName,custCode,status",ht,extCond);
     		for(int i =0; i<listQry.size(); i++) {
      			sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
     			Map m=(Map)listQry.get(i);
      			tono     = (String)m.get("tono");
 	    		String custName    = (String)m.get("custName");
     			String custCode    = (String)m.get("custCode");
     		        String status      =  (String)m.get("status");
    if(type.equalsIgnoreCase("Reverse")){	%>
    
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.TONO.value='<%=tono%>'
                     window.close();"><%=tono%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(custName)%></td>
		<td align="left" class="main2">&nbsp;<%=status%></td>
	</TR>
    
    
<%
}else{
	%>
	 <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.TONO.value='<%=tono%>'
                     window.close();"><%=tono%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(custName)%></td>
		<td align="left" class="main2">&nbsp;<%=status%></td>
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
	/* $('#myTable').dataTable(); */
    /* $('#myModal1').click(function(){
    	if(document.getElementById("alertValue").value!="")
    	{
    		//$("#myModal").modal();
    		document.getElementById('myModal').style.display = "block";
    	}
    }); */
    $('[data-toggle="tooltip"]').tooltip(); 
    $('#myTable').dataTable({
    	"order": []
	} );
});
</script>