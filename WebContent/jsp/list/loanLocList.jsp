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

<title>Location Details</title>
<link rel="stylesheet" href="../css/style.css">


</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Location Details</h3> 
</div>
</div>
<body>
<form method="post" name="form1">
<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Location Id</TH>
      <TH>Description</TH>
    </TR>
    </thead>
    <tbody>
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);

    String plant= (String)session.getAttribute("PLANT");
    String userID= (String)session.getAttribute("LOGIN_USER");
    CustUtil custUtils = new CustUtil();
    custUtils.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String sFeildName = strUtils.fString(request.getParameter("FEILD_NAME"));
    String loc = strUtils.fString(request.getParameter("LOC"));


    String sBGColor = "";
   try{
    LocUtil _LocUtil = new LocUtil();
    _LocUtil.setmLogger(mLogger);
    ArrayList arrLoc = new ArrayList();
    if(loc.length()>0)
    {
    	 arrLoc = _LocUtil.getAllLocDetails(plant, " AND ISACTIVE ='Y' AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%' and loc like '%"+loc+"%'",userID);
    }
    else
    {
    	 arrLoc = _LocUtil.getAllLocDetails(plant, " AND ISACTIVE ='Y' AND LOC NOT LIKE 'SHIPPINGAREA%' AND LOC NOT LIKE 'TEMP_TO%'",userID);	
    }
   
    //ArrayList arrLoc = _LocUtil.getAllLocDetails(plant, " AND ISACTIVE ='Y'");
   
    for(int i =0; i<arrLoc.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrLocLine = (Map)arrLoc.get(i);
        String sLocCode    = (String)arrLocLine.get("LOC");
        String sLocDesc   = strUtils.removeQuotes((String)arrLocLine.get("LOCDESC"));
        String sRemark      = strUtils.removeQuotes((String)arrLocLine.get("USERFLD1"));
%>
<% if (sFeildName.equalsIgnoreCase("FRLOC")){%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.FRLOC.disabled = false;
                     window.opener.form.FRLOC.value='<%=sLocCode%>';
                     window.close();"><%=sLocCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=sLocDesc%></td>
	</TR>
    
<%}else {%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			 onClick="window.opener.form.LOC1.disabled = false;
                      window.opener.form.LOC1.value='<%=sLocCode%>';
                      window.close();"><%=sLocCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=sLocDesc%></td>
	</TR>

<%}%>
<%
}
}catch(Exception he){he.printStackTrace(); 
System.out.println("Error in reterieving data");}
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