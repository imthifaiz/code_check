<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.RecvDetDAO"%>
<%@ page import="com.track.util.*"%>
<%@page import="com.track.dao.LocMstDAO"%><html>
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
<title>Location List</title>
<link rel="stylesheet" href="css/style.css">

</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Location List</h3> 
</div>
</div>
<body>
<form method="post" name="form1">
<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Location ID</TH>
      <TH>Description</TH>
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

    StrUtils strUtils = new StrUtils();
    String PLANT= session.getAttribute("PLANT").toString();
     String USERID= session.getAttribute("LOGIN_USER").toString();
     String loc = request.getParameter("LOC");
     String TYPE = strUtils.fString(request.getParameter("TYPE"));
    LocMstDAO  _LocMstDAO  = new LocMstDAO();  
    _LocMstDAO.setmLogger(mLogger);
    String sBGColor = "";
   try{
    List listQry = _LocMstDAO.getLocByWMS(PLANT,"",loc);  
    for(int i =0; i<listQry.size(); i++) {
   
     	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     	Map m=(Map)listQry.get(i);
     	String sLoc    = (String)m.get("loc");
     	String slocDesc    = strUtils.fString((String)m.get("locdesc"));
if(TYPE.equalsIgnoreCase("WIPSUMMARY")){%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.LOC.value='<%=sLoc%>';
       		          window.close();"><%=sLoc%></a></td>
		<td align="left" class="main2">&nbsp;<%=slocDesc%></td>
	</TR>
<%} else {%>
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form1.LOC.value='<%=sLoc%>';
       		          window.close();"><%=sLoc%></a></td>
		<td align="left" class="main2">&nbsp;<%=slocDesc%></td>
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