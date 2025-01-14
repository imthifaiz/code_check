<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>User List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/style.css">

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"></style>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">USER LIST</h3> 
</div>
</div>
<body>
<form method="post" name="form1">
<div >
<table id="myTable" class="table" > 
 <thead style="background: #eaeafa">
        <tr>
        <th>Company Name</th>
        <th>User Id</th>
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

    ArrayList invQryList  = new ArrayList();
    userBean _userBean      = new userBean();
    
    _userBean.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
    String COMPANY = strUtils.fString(request.getParameter("COMPANY"));
     String USERID = strUtils.fString(request.getParameter("USERID"));
     String USER_LEVEL = strUtils.fString(request.getParameter("USER_LEVEL"));
    
      Hashtable ht = new Hashtable();
    //  if(strUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
      if(strUtils.fString(COMPANY).length() > 0)        ht.put("DEPT",COMPANY);
       if(strUtils.fString(USERID).length() > 0)        ht.put("USER_ID",USERID);
       if(strUtils.fString(USER_LEVEL).length() > 0)        ht.put("USER_LEVEL",USER_LEVEL);
    
       List listQry  =_userBean.getUserListUserId(ht,PLANT,COMPANY,USERID,USER_LEVEL);

    String sBGColor = "";
   try{
   
    
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sCompany   = (String)m.get("DEPT");
     String sUserId   = (String)m.get("USER_ID");


%>
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form1.COMPANY.value='<%=sCompany%>';
                      window.opener.form1.USERID.value='<%=sUserId%>';
                      window.close();"><%=sCompany%></a></td>
		<td align="left" class="main2">&nbsp;<%=sUserId%></td>

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
