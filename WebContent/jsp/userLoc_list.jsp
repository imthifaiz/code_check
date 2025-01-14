<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>

<html>
<head>
<title>User Loc List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="../css/style.css">

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">USER LOCATION LIST</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div>
<table id="myTable" class="table">
         

<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
   userBean _userBean      = new userBean();
    StrUtils strUtils = new StrUtils();
    
    _userBean.setmLogger(mLogger);
    String plant= (String)session.getAttribute("PLANT");
      String type= strUtils.fString(request.getParameter("TYPE"));
    String user= strUtils.fString(request.getParameter("USER"));
    String loc = strUtils.fString(request.getParameter("LOC"));
    
  
    String sBGColor = "";
   try{
 if(type.equalsIgnoreCase("USER_LIST")){
       Hashtable ht = new Hashtable();

       if(strUtils.fString(plant).length() > 0)        ht.put("DEPT",plant);
       if(strUtils.fString(user).length() > 0)        ht.put("USER_ID",user);
       ArrayList arrUser = _userBean.getUserListUserId(ht,plant,"",user,"");
    %>
    <thead style="background: #eaeafa"> 
    <tr>
    <th>User ID</th>
       </tr>
        </thead>
        <tbody>
    
    <%
         for(int i =0; i<arrUser.size(); i++) {
       
         sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
         Map m=(Map)arrUser.get(i);
         String sUserId   = (String)m.get("USER_ID");
  %>
  <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			 onClick="window.opener.form1.USER.value='<%=sUserId%>'; window.close();"><%=sUserId%></a></td>

	</TR>
	</tbody>
    
  <% }}
  }catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}%>
  
   
 
 <%  try{ 
      if(type.equalsIgnoreCase("LOC_LIST")){
        LocUtil locUtil = new LocUtil();
        locUtil.setmLogger(mLogger);
                ArrayList listQry = locUtil.getAllLocDetails(plant," AND  LOC LIKE '"+loc+"%' AND  ISACTIVE ='Y' and loc not like 'SHIPPINGAREA%' and loc not like 'TEMP_TO%'","");
         %>
      
      
       <thead style="background: #eaeafa"> 
       <tr>
        <th>Location</th>
        <th>Description</th>
       </tr>
       </thead>
       <tbody>
    
    <%
        for(int i =0; i<listQry.size(); i++) {
       
         sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
          Map m=(Map)listQry.get(i);
          loc     = strUtils.fString((String)m.get("LOC"));
          String locDesc     = strUtils.fString((String)m.get("LOCDESC"));
        
   %>
   <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.LOC.value='<%=loc%>';window.close();"><%=loc%></a></td>
		<td align="left" class="main2">&nbsp;<%=locDesc%></td>

	</TR>
   
   
   <% } }}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}%>


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