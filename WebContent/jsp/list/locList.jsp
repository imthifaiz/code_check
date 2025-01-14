<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<html>
<head>
<!-- <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
 <link rel="stylesheet" href="../dist/css/bootstrap.min.css"/>
  <!-- DataTables -->
  <link rel="stylesheet" href="../dist/css/lib/datatables.min.css"/>
  <!-- jQuery 3 -->
  <script type="text/javascript" src="../dist/js/jquery.min.js"></script>
  <script type="text/javascript" src="../dist/js/jquery.dataTables.min.js"></script>
	<!-- jQuery UI -->
	<script src="../dist/js/jquery-ui-1.12.1.js"></script>
<title>Location List</title>
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
<link rel="stylesheet" href="../css/style.css">
</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Location List</h3> 
</div>
</div>
<body bgcolor="#ffffff">
<form method="post" name="form1">
<table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
		<TH>Location Id</TH>
		<TH>Location Desc</TH>
		<TH>IsActive</TH>
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
    String plant= (String)session.getAttribute("PLANT");
   
    StrUtils strUtils = new StrUtils();
    String sLocId = StrUtils.replaceStr2Char(strUtils.fString(request.getParameter("LOC_ID")));
    String sBGColor = "";
    try
    {
    	ArrayList arrCust=new ArrayList();
    	LocUtil _LocUtil = new LocUtil();
    	_LocUtil.setmLogger(mLogger);
    	PlantMstDAO plantMstDAO = new PlantMstDAO();
    	Boolean isuserloc = plantMstDAO.getisuserloc(plant);
   		if(sLocId=="" || sLocId == null){
   		if(isuserloc)
    	 	arrCust = _LocUtil.getAllLocDetails(plant,"",(String)session.getAttribute("LOGIN_USER"));
   		else
   			arrCust = _LocUtil.getAllLocDetails(plant,"","");	
   		}else{
   			if(isuserloc)
   			arrCust = _LocUtil.getLocDetails(sLocId,plant,(String)session.getAttribute("LOGIN_USER"));
   			else
   			arrCust = _LocUtil.getLocDetails(sLocId,plant,"");
   		}
    	
    	for(int i =0; i<arrCust.size(); i++) {
        	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
        	Map arrCustLine = (Map)arrCust.get(i);
        	String sCustCode    = strUtils.fString((String)arrCustLine.get("LOC"));
        	String sCustName1   = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("LOCDESC")));
        	String sRemark      = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("USERFLD1")));
            String isactive      = strUtils.fString((String)arrCustLine.get("ISACTIVE"));
%>
	<TR bgcolor="<%=sBGColor%>">
		<td class="main2"><a href="#"
			onClick="
      window.opener.form.LOC.value='<%=StrUtils.forHTMLTagPopup(sCustCode)%>';
      window.opener.form.LOCDESC.value='<%=StrUtils.forHTMLTagPopup(sCustName1)%>';
     
      window.close();"><%=StrUtils.forHTMLTag(sCustCode)%>
		</a></td>
		<td class="main2"><%=StrUtils.forHTMLTag(sCustName1)%></td>
		<td class="main2"><%=StrUtils.forHTMLTag(isactive)%></td>
	</TR>
	<%
		}
	}catch(Exception he){he.printStackTrace(); 
		System.out.println("Error in reterieving data");
	}
%>
	</tbody>
	
</table>
<div class="text-center">       
        <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
	     </div>    

</form>
<script>
$(document).ready(function(){
	$('#myTable').dataTable();
      
});
</script>
</body>
</html>





