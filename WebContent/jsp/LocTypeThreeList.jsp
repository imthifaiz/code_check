<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Location Type Two List</title>
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

<link rel="stylesheet" href="css/style.css">
</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Location Type Three List</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div>
<table id="myTable" class="table" > 
<thead style="background: #eaeafa">
        <tr>
          <th>Location Type ID</th>
          <th>Location Type Description</th>
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
StrUtils strUtils = new StrUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();
 
  DateUtils  dateUtil = new DateUtils();
  loctypeutil.setmLogger(mLogger);
 
 
  String loc_id = strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
  String type  = strUtils.fString(request.getParameter("TYPE"));
  String plant = (String)session.getAttribute("PLANT");
  
  String sBGColor = "";

 try{
  List listQry = loctypeutil.getLocTypeListthree(loc_id,plant,"");

  for (int i =0; i<listQry.size(); i++){
  Map map = (Map) listQry.get(i);
  sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
   String loctypeid         = strUtils.fString((String) map.get("LOC_TYPE_ID3"));
   String desc             = strUtils.fString((String) map.get("LOC_TYPE_DESC3"));
    String isactive             = strUtils.fString((String) map.get("ISACTIVE"));
if(type.equalsIgnoreCase("locsummary")){
	%>
	  <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.LOC_TP_ID.value='<%=loctypeid%>';window.close();"><%=loctypeid%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

	</TR>
	   
	<%}else if(type.equalsIgnoreCase("movhis")){
%>
    <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.LOC_TYPE_ID3.value='<%=loctypeid%>';window.close();"><%=loctypeid%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

	</TR>
   
<%}else if(type.equalsIgnoreCase("KITDEKIT")){
%>
   <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			 onClick="window.opener.form.LOC_TYPE_ID3.value='<%=loctypeid%>';window.opener.form.LOC_TYPE_DESC3.value='<%=desc%>';window.close();"><%=loctypeid%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

	</TR>
   <%}else if(type.equalsIgnoreCase("alternatebrand")){
		%>
		   <TR>
			<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			   onClick="window.opener.form1.LOC_TYPE_ID3.value='<%=loctypeid%>';window.close();"><%=loctypeid%></a></td>
		    <td align="left" class="main2">&nbsp;<%=desc%></td>
		     <td align="left" class="main2">&nbsp;<%=isactive%></td>


			</TR>
	<%}else{
%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			 onClick="window.opener.form1.LOC_TYPE_ID3.value='<%=loctypeid%>';window.opener.form1.lOC_TYPE_DESC3.value='<%=desc%>'; setCheckedValue( window.opener.form1.ACTIVE,'<%=isactive%>');window.close();"><%=loctypeid%></a></td>
		<td align="left" class="main2">&nbsp;<%=desc%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

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
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script>