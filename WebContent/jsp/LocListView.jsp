<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Location List</title>
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

<link rel="stylesheet" href="css/style.css">
</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Location List</h3> 
</div>
</div>
<body>
<form method="post" name="form1">
<div >
<table id="myTable" class="table">
 <thead style="background: #eaeafa"> 
        <tr>
          <th>Loc Id</th>
          <th>Loc Desc</th>
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
ArrayList arrCust=new ArrayList();
    String plant= (String)session.getAttribute("PLANT");
    CustUtil custUtils = new CustUtil();
    custUtils.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String sLoc = strUtils.fString(request.getParameter("LOC_ID"));
    String sBGColor = "";
   	try{
    	LocUtil _LocUtil = new LocUtil();
    	_LocUtil.setmLogger(mLogger);
    	Hashtable ht = new Hashtable();
    	ht.put("LOC_TYPE_ID","");
   		if(sLoc=="" || sLoc == null){
    	 arrCust = _LocUtil.getAllLocDetails(plant," AND ISACTIVE='Y' ","");
  		}else{
     	 arrCust = _LocUtil.getLocDetails(sLoc,plant, " AND ISACTIVE='Y' ",ht);
   		}
      
    	System.out.println("Total record found : " + arrCust.size());
    	for(int i =0; i<arrCust.size(); i++) {
        	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        	Map arrCustLine = (Map)arrCust.get(i);
        	String sCustCode    = (String)arrCustLine.get("LOC");
        	String sCustName1   = strUtils.removeQuotes((String)arrCustLine.get("LOCDESC"));
        	String sRemark      = strUtils.removeQuotes((String)arrCustLine.get("USERFLD1"));
%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.LOC_ID.disabled = false;
      		window.opener.form1.LOC_ID.value='<%=sCustCode%>';
      		window.close();"><%=sCustCode%>
      	</a></td>
		<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
	</TR>
    
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