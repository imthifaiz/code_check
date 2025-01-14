<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<title>Customer Status List</title>
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
		if (!radioObj)
			return;
		var radioLength = radioObj.length;
		if (radioLength == undefined) {
			radioObj.checked = (radioObj.value == newValue.toString());
			return;
		}
		for ( var i = 0; i < radioLength; i++) {
			radioObj[i].checked = false;
			if (radioObj[i].value == newValue.toString()) {
				radioObj[i].checked = true;
			}
		}
	}

</Script>
<link rel="stylesheet" href="css/style.css">
</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">CUSTOMER STATUS LIST</h3> 
</div>
</div>
  
<body>
<form method="post" name="form1">
<div >
<table id="myTable" class="table">
<thead style="background: #eaeafa">
    <tr>
        <th>Customer Status Id</th>
        <th>Description</th>
        <th>IsActive</th>
      </tr>
    </thead>
    <tbody>

	<%

HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
    String plant= (String)session.getAttribute("PLANT");
    StrUtils strUtils = new StrUtils();
    String sCustomerStatusId = strUtils.fString(request.getParameter("CUSTOMERSTATUID"));
    String sBGColor = "",extcond="";
    Hashtable ht = new Hashtable();
    try
    {
    	ArrayList arrCust=new ArrayList();
    	 CustUtil _CustUtil = new CustUtil();
    	_CustUtil.setmLogger(mLogger);
    	ht.put("CUSTOMER_STATU_ID",sCustomerStatusId);
    	if(sCustomerStatusId=="" || sCustomerStatusId == null){
    	 arrCust = _CustUtil.getAllCustomerStatusDetails(plant,extcond,"");
   		}else{
     	 arrCust = _CustUtil.getCustomerStatusDetails(sCustomerStatusId,plant," ",ht);
   		}
     	for(int i =0; i<arrCust.size(); i++) {
        	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        	Map arrCustLine = (Map)arrCust.get(i);
        	String CustomerStatus  = strUtils.fString((String)arrCustLine.get("CUSTOMERSTATUID"));
        	String Description = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("CUSTOMERSTATUSDESC")));
        	String Remarks   = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("REMARKS")));
            String IsActive  = strUtils.fString((String)arrCustLine.get("ISACTIVE"));
            //end
%>
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.CUSTOMER_STATUS_ID.value='<%=CustomerStatus%>';
                     window.opener.form1.DESC.value='<%=Description%>';
                     window.opener.form1.REMARKS.value='<%=Remarks%>';
                     setCheckedValue(window.opener.form1.ACTIVE,'<%=IsActive%>');
                     window.close();"><%=CustomerStatus%></a></td>
		<td align="left" class="main2">&nbsp;<%=Description%></td>
		<td align="left" class="main2">&nbsp;<%=IsActive%></td>

	</TR>
   
	<%
		}
	}catch(Exception he){he.printStackTrace(); 
		System.out.println("Error in reterieving data");
	}
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