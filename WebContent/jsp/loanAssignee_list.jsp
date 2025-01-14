<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Loan Assignee List</title>
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

function setCheckedValue(radioObj, newValue) {
	if(!radioObj)
		return;
	var radioLength = radioObj.length;
	if(radioLength == undefined) {
		radioObj.checked = (radioObj.value == newValue.toString());
		return;
	}
	for(var i = 0; i < radioLength; i++) {
		radioObj[i].checked = false;
		if(radioObj[i].value == newValue.toString()) {
			radioObj[i].checked = true;
		}
	}
}
</Script>
<link rel="stylesheet" href="../css/style.css">
</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">LOAN ASSIGNEE LIST</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div >
<table id="myTable" class="table">
<thead style="background: #eaeafa">
        <tr>
          <th>Assignee Code</th>
          <th>Assignee Name</th>
          <th>IsActive</th>
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

    CustUtil custUtils = new CustUtil();
    StrUtils strUtils = new StrUtils();
    custUtils.setmLogger(mLogger);
    
    String plant= (String)session.getAttribute("PLANT");
    String sCustName = strUtils.fString(request.getParameter("CUST_NAME"));
  
    String sBGColor = "";
   try{
    ArrayList arrCust = custUtils.getLoanAssigneeListStartsWithName(sCustName,"",plant,"");
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrCustLine = (Map)arrCust.get(i);
        String sCustCode     = (String)arrCustLine.get("LASSIGNNO");
        String sCustName1     = strUtils.removeQuotes((String)arrCustLine.get("CNAME"));
         String isactive     = strUtils.fString((String)arrCustLine.get("ISACTIVE"));


%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.CUST_CODE.value='<%=sCustCode%>';
                     window.opener.form.CUST_CODE1.value='<%=sCustCode%>';
                     window.opener.form.CUST_NAME.value='<%=sCustName1%>';
                     setCheckedValue( window.opener.form.ACTIVE,'<%=isactive%>');
                     window.close();"><%=sCustCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

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