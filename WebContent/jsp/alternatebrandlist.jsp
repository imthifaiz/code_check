<%@ page import="java.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<html>
<head>
<title>Product Brand List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"></style>
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

<link rel="stylesheet" href="css/style.css">
</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Product Brand List</h3> 
</div>
</div>

<body>
<form method="post" name="form">
<div>
<table id="myTable" class="table" >
<thead style="background: #eaeafa">
        <tr>
          <th>Product Brand ID</th>
          <th>Product Brand Description</th>
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
PrdBrandUtil prdutil = new PrdBrandUtil();
 
  DateUtils  dateUtil = new DateUtils();
  prdutil.setmLogger(mLogger);
 
 
  String item_id = StrUtils.fString(request.getParameter("ITEM_ID"));
  String extraCond = StrUtils.fString(request.getParameter("Cond"));
  String plant = (String)session.getAttribute("PLANT");
  
  String formName =   StrUtils.fString(request.getParameter("formName"));
  if(formName.equals("")){
	  formName = "form";
  }
  
  List listQry  = new ArrayList();
  
  String sBGColor = "";

 try{
	if(extraCond.equals("OnlyActive")){
		listQry = prdutil.getPrdBrandList(item_id,plant,"AND ISACTIVE = 'Y'");
	}
	else{
		listQry = prdutil.getPrdBrandList(item_id,plant,"");
	}

  for (int i =0; i<listQry.size(); i++){
  Map map = (Map) listQry.get(i);
  sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
   String prdbrandid         = StrUtils.fString((String) map.get("PRD_BRAND_ID"));
   String desc             = StrUtils.fString((String) map.get("PRD_BRAND_DESC"));
    String isactive             = StrUtils.fString((String) map.get("ISACTIVE"));

%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.<%=formName%>.ALTERNATEBRAND.value='<%=prdbrandid%>';window.opener.<%=formName%>.PRD_BRAND_DESC.value='<%=desc%>'; setCheckedValue( window.opener.<%=formName%>.ACTIVE,'<%=isactive%>');window.close();"><%=prdbrandid%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
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
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script>
