<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<html>
<head>
<title>User List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="dist/css/font-awesome.min.css">
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title"> USER LIST</h3> 
</div>
</div>
   
<body>
<jsp:useBean id="sb"  class="com.track.gates.sqlBean" />
<SCRIPT LANGUAGE="JavaScript">
function onAdd(){
	   var USER_ID   = document.form.USER_ID1.value;
	   var EMAIL = document.form.EMAIL.value;
	   var utype = document.form.typ.value;
	    if(USER_ID == "" || USER_ID == null) {alert("Please Enter USER ID ");document.form.USER_ID.focus(); return false; }
	    //if(EMAIL == "" || EMAIL == null) {alert("Please Enter EMAIL");document.form.EMAIL.focus(); return false; }
	   document.form.action  = "userList.jsp?TYPE="+utype+"&action=ADD";
	   document.form.submit();
	}
</SCRIPT>
<%
StrUtils strUtils = new StrUtils();
session= request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String plant= sPlant;
String userid = strUtils.fString(request.getParameter("USER_ID"));
String cmpy = strUtils.fString(request.getParameter("DEPT1"));
String typ = strUtils.fString(request.getParameter("TYPE"));
String selplant = sPlant;
String res = "", sql="";       int n=0;
String action = "";
String sAddEnb = "enable";
boolean  inserted=false;
action = strUtils.fString(request.getParameter("action"));
DateUtils dateutils = new DateUtils();
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
String USER_ID="",USER_ID1="",USER_NAME="",EMAIL="",DEPT="",COMPANY="";
if(selplant.equalsIgnoreCase("track"))
	DEPT = request.getParameter("DEPT");
   else
	   DEPT = (String)session.getAttribute("PLANT");

String user_id = request.getParameter("USER_ID1");
String selecteduseremail = request.getParameter("EMAIL");
// >> Add
if (action.equalsIgnoreCase("ADD")) {
	MovHisDAO movdao =null;
    //      Setting the Authorisation to null
    if(selplant.equalsIgnoreCase("track"))
   	  movdao = new MovHisDAO();
    else
                movdao = new MovHisDAO(sPlant);
                movdao.setmLogger(mLogger);
                Hashtable htm = new Hashtable();
    	        htm.put(IDBConstants.PLANT,sPlant);
    	        htm.put(IDBConstants.DIRTYPE,TransactionConstants.UPDATE_USER);
    	        htm.put("RECID","");
    	        
    	        htm.put(IDBConstants.CREATED_BY,sUserId);  
    	        
    	        	 htm.put(IDBConstants.REMARKS,sUserId);  
    	        
    	        htm.put(IDBConstants.UPBY,sUserId);  
    	        htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
    	        htm.put(IDBConstants.UPDATED_AT,dateutils.getDateTime());
    	        htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
	sql = "update USER_INFO set EMAIL='"+selecteduseremail+"' where USER_ID='"+user_id+"'"+"and dept='"+DEPT+"'";
	n = sb.insertRecords(sql);
    if(n==1)
    	inserted = movdao.insertIntoMovHis(htm);		   

		if (n==1&&inserted==true) {
			res = "<font class = " + IDBConstants.SUCCESS_COLOR
					+ ">User Updated Successfully</font>";

					
		} else {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Failed to Update User </font>";

		}
	}


%>
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<% 
sl.setmLogger(mLogger);
%>
<form class="form-horizontal" method="post" name="form">
<div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

  <div id="target" style="display:none">
     <div class="form-group">
      <label class="control-label col-sm-2"  for="Company">      
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Company :</label>
      <div class="col-sm-4">
            <div class="input-group">       
                   <input name="DEPT" type="TEXT" value="<%=DEPT%>" size="50" MAXLENGTH=50 class="form-control" readonly>
        </div>
      </div>
	  <div class="form-inline">
      <label class="control-label col-sm-2"  for="User ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;User ID :</label>
      <div class="col-sm-4">
      <div class="input-group">
      <input name="USER_ID1" type="TEXT" value="<%=USER_ID1%>" size="50" MAXLENGTH=50 class="form-control" readonly>
		  </div>
	 <INPUT type="hidden" name="DEPT" value="">
	 <INPUT type="hidden" name="typ" value="<%=typ%>">
      </div>
    </div>
    </div>
     <div class="form-group">
      <label class="control-label col-sm-2"  for="User Name">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;User Name :</label>
      <div class="col-sm-4">
            <div class="input-group">
       <input name="USER_NAME" type="TEXT" value="<%=USER_NAME%>" size="50" MAXLENGTH=25 class="form-control" readonly>
        </div>
      </div>
	  <div class="form-inline">
      <label class="control-label col-sm-2"  for="Email">&nbsp;Email :</label>
      <div class="col-sm-4">
      <div class="input-group">
      <input name="EMAIL" value="<%=EMAIL%>" size="50" MAXLENGTH=40 class="form-control">
      </div>
    </div>
    </div>
</div>
    <div class="form-group">        
      <div class="col-sm-offset-5 col-sm-8">
            	
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>

      </div>
    
</div>

</div>
<div class="form-group" id="Vid">
      <div class="col-sm-4">
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Edit UserId Email</a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Edit UserId Email</a>
      </div>
       	  </div>
</div>
<div >
<table id="myTable" class="table" > 
<thead style="background: #eaeafa">
        <tr>
         <th>User ID</th>
        <th>User Name</th>
        <th>Company</th>
        <th>Email</th>        
        <th id="Eid">Edit</th>        
      </tr>
    </thead>
    <tbody>
    
	 
<jsp:useBean id="eb" class="com.track.gates.encryptBean" />
	<%
	session= request.getSession();
    CustUtil custUtils = new CustUtil();
    
    userBean _userBean      = new userBean();
    _userBean.setmLogger(mLogger);
    

   String seldept="";
 Hashtable ht = new Hashtable();
    String sBGColor = "";ArrayList arrCust=null;
    if(cmpy.equalsIgnoreCase("0")||cmpy==null||cmpy.equalsIgnoreCase("")){
    session.setAttribute("CMPY","track");}
    else
    {
    	session.setAttribute("CMPY",seldept);
    }
   try{
//    ArrayList arrCust = custUtils.getVendorListStartsWithName(sCustName,plant);
    if(cmpy.equalsIgnoreCase("0")||cmpy==null||cmpy.equalsIgnoreCase("")){
     arrCust= _userBean.getUserListforCompany(ht,plant,"",userid);}
    else{
      arrCust= _userBean.getUserListforCompany(ht,plant,cmpy,userid);
      
      }
  /*  for(int i =0; i<arrCust.size(); i++) {
       
       Map arrCustLine = (Map)arrCust.get(0);
       seldept=(String)arrCustLine.get("DEPT");
       session.setAttribute("CMPY",seldept);
       } */
    
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
       Map arrCustLine = (Map)arrCust.get(i);
        String sCustCode     = (String)arrCustLine.get("USER_ID");
        String sCustName1     = strUtils.removeQuotes((String)arrCustLine.get("USER_NAME"));
        String dept = (String)arrCustLine.get("DEPT");
        String user_level = (String)arrCustLine.get("USER_LEVEL");
        String password = eb.decrypt((String)arrCustLine.get("PASSWORD"));
        String desig = (String)arrCustLine.get("DESGINATION");
        String tel = (String)arrCustLine.get("TELNO");
        String hpno = (String)arrCustLine.get("HPNO");
  		String fax = (String)arrCustLine.get("FAX");
  		String email = (String)arrCustLine.get("EMAIL");
  		String remarks = (String)arrCustLine.get("REMARKS");
  		String imagePath = (String)arrCustLine.get("IMAGEPATH");
  		if(imagePath.equalsIgnoreCase(""))
  			imagePath = "../jsp/images/trackNscan/nouser.png";
  		else
  			imagePath="/track/ReadFileServlet/?fileLocation="+imagePath;
        
//        String sCustNameLast    = strUtils.removeQuotes((String)arrCustLine.get(2));

%>
	
<%
if(typ.equalsIgnoreCase("PurchaseAdminUser")){
%>
	<td class="main2" style="font-weight: bold " align="left">&nbsp;<a href="#" 
	onClick="window.opener.form1.PurchaseAdminUser.value='<%=sCustCode%>';             
             window.opener.form1.PurchaseAdminmail.value='<%=email%>';
             window.close();"><%=sCustCode%></a></td>
	<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
	<td align="left" class="main2">&nbsp;<%=dept%></td>
	<td align="left" class="main2">&nbsp;<%=email%></td>
	<td align="left" class="main2"><a onclick="EditUser('<%=sCustCode%>','<%=sCustName1%>','<%=dept%>','<%=email%>')"><i class="fa fa-pencil-square-o"></i></a></td>
	</TR>
	<%}
	else if(typ.equalsIgnoreCase("PurchaseUser")){
		%>
		<td class="main2" style="font-weight: bold " align="left">&nbsp;<a href="#" 
		onClick="window.opener.form1.PurchaseUser.value='<%=sCustCode%>';             
	             window.opener.form1.PurchaseUsermail.value='<%=email%>';
	             window.close();"><%=sCustCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
		<td align="left" class="main2">&nbsp;<%=dept%></td>
		<td align="left" class="main2">&nbsp;<%=email%></td>
		<td align="left" class="main2"><a onclick="EditUser('<%=sCustCode%>','<%=sCustName1%>','<%=dept%>','<%=email%>')"><i class="fa fa-pencil-square-o"></i></a></td>
		</TR>
		<%}
		else if(typ.equalsIgnoreCase("EstimateAdminUser")){
		%>
	<td class="main2" style="font-weight: bold " align="left">&nbsp;<a href="#" 
	onClick="window.opener.form1.EstimateAdminUser.value='<%=sCustCode%>';             
             window.opener.form1.EstimateAdminmail.value='<%=email%>';
             window.close();"><%=sCustCode%></a></td>
	<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
	<td align="left" class="main2">&nbsp;<%=dept%></td>
	<td align="left" class="main2">&nbsp;<%=email%></td>
	<td align="left" class="main2"><a onclick="EditUser('<%=sCustCode%>','<%=sCustName1%>','<%=dept%>','<%=email%>')"><i class="fa fa-pencil-square-o"></i></a></td>
	</TR>
		<%}
	else if(typ.equalsIgnoreCase("EstimateUser")){
		%>
		<td class="main2" style="font-weight: bold " align="left">&nbsp;<a href="#" 
		onClick="window.opener.form1.EstimateUser.value='<%=sCustCode%>';             
	             window.opener.form1.EstimateUsermail.value='<%=email%>';
	             window.close();"><%=sCustCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
		<td align="left" class="main2">&nbsp;<%=dept%></td>
		<td align="left" class="main2">&nbsp;<%=email%></td>
		<td align="left" class="main2"><a onclick="EditUser('<%=sCustCode%>','<%=sCustName1%>','<%=dept%>','<%=email%>')"><i class="fa fa-pencil-square-o"></i></a></td>
		</TR>
		<%}
		else if(typ.equalsIgnoreCase("SalesAdminUser")){
		%>
	<td class="main2" style="font-weight: bold " align="left">&nbsp;<a href="#" 
	onClick="window.opener.form1.SalesAdminUser.value='<%=sCustCode%>';             
             window.opener.form1.SalesAdminmail.value='<%=email%>';
             window.close();"><%=sCustCode%></a></td>
	<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
	<td align="left" class="main2">&nbsp;<%=dept%></td>
	<td align="left" class="main2">&nbsp;<%=email%></td>
	<td align="left" class="main2"><a onclick="EditUser('<%=sCustCode%>','<%=sCustName1%>','<%=dept%>','<%=email%>')"><i class="fa fa-pencil-square-o"></i></a></td>
	</TR>
		<%}
	else if(typ.equalsIgnoreCase("SalesUser")){
		%>
		<td class="main2" style="font-weight: bold " align="left">&nbsp;<a href="#" 
		onClick="window.opener.form1.SalesUser.value='<%=sCustCode%>';             
	             window.opener.form1.SalesUsermail.value='<%=email%>';
	             window.close();"><%=sCustCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
		<td align="left" class="main2">&nbsp;<%=dept%></td>
		<td align="left" class="main2">&nbsp;<%=email%></td>
		<td align="left" class="main2"><a onclick="EditUser('<%=sCustCode%>','<%=sCustName1%>','<%=dept%>','<%=email%>')"><i class="fa fa-pencil-square-o"></i></a></td>
		</TR>
		<%}
		else if(typ.equalsIgnoreCase("RentalAdminUser")){
		%>
	<td class="main2" style="font-weight: bold " align="left">&nbsp;<a href="#" 
	onClick="window.opener.form1.RentalAdminUser.value='<%=sCustCode%>';             
             window.opener.form1.RentalAdminmail.value='<%=email%>';
             window.close();"><%=sCustCode%></a></td>
	<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
	<td align="left" class="main2">&nbsp;<%=dept%></td>
	<td align="left" class="main2">&nbsp;<%=email%></td>
	<td align="left" class="main2"><a onclick="EditUser('<%=sCustCode%>','<%=sCustName1%>','<%=dept%>','<%=email%>')"><i class="fa fa-pencil-square-o"></i></a></td>
	</TR>
		<%}
	else if(typ.equalsIgnoreCase("RentalUser")){
		%>
		<td class="main2" style="font-weight: bold " align="left">&nbsp;<a href="#" 
		onClick="window.opener.form1.RentalUser.value='<%=sCustCode%>';             
	             window.opener.form1.RentalUsermail.value='<%=email%>';
	             window.close();"><%=sCustCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
		<td align="left" class="main2">&nbsp;<%=dept%></td>
		<td align="left" class="main2">&nbsp;<%=email%></td>
		<td align="left" class="main2"><a onclick="EditUser('<%=sCustCode%>','<%=sCustName1%>','<%=dept%>','<%=email%>')"><i class="fa fa-pencil-square-o"></i></a></td>
		</TR>
		<%}
		else
	{%>
	
	<TR>
	<td class="main2" style="font-weight: bold " align="left">&nbsp;<a href="#" 
	onClick="window.opener.form.USER_ID.value='<%=sCustCode%>';
             window.opener.form.USER_ID1.value='<%=sCustCode%>';
             window.opener.form.DEPT.value='<%=dept%>';
             window.opener.form.DEPT1.value='<%=dept%>';
             window.opener.form.USER_LEVEL.value='<%=user_level%>';
             window.opener.form.USER_NAME.value='<%=sCustName1%>';
             window.opener.form.PASSWORD.value='<%=password%>';
             window.opener.form.CPASSWORD.value='<%=password%>';
             window.opener.form.DESGINATION.value='<%=desig%>';
             window.opener.form.TELNO.value='<%=tel%>';
             window.opener.form.HPNO.value='<%=hpno%>';
             window.opener.form.FAX.value='<%=fax%>';
             window.opener.form.EMAIL.value='<%=email%>';
             window.opener.form.REMARKS.value='<%=remarks%>';
             if(window.opener.form.IMAGE_UPLOAD){
             	window.opener.form.IMAGE_UPLOAD.src = '<%=imagePath%>';
           	 }
             window.close();"><%=sCustCode%></a></td>
	<td align="left" class="main2">&nbsp;<%=sCustName1%></td>
	<td align="left" class="main2">&nbsp;<%=dept%></td>
	<td align="left" class="main2">&nbsp;<%=email%></td>
	<td align="left" class="main2" id="Did">&nbsp;</td>
	</TR>
	<%
	}
	
	
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
<%
if(typ=="")
{
%>
<script>
document.getElementById("Vid").style.display = "none";
document.getElementById("Eid").style.display = "none";
document.getElementById("Did").style.display = "none";
</script>
<%}
%>
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
    $('[data-toggle="tooltip"]').tooltip(); //Below Jquery Script used for Show/Hide Function
    
    $('#Show').click(function() {
	    $('#target').show(500);
	    $('#Show').hide(0);
	    $('#Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('#Hide').click(function() {
	    $('#target').hide(500);
	    $('#Show').show(0);
	    $('#Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
    if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('#Show').click();
    }else{
    	$('#Hide').click();
    }	
});
function EditUser(UserId,UserName,Company,Email)
{
	$('#Show').click();
	document.form.DEPT.value=Company;
	document.form.USER_ID1.value=UserId;
	document.form.USER_NAME.value=UserName;
	document.form.EMAIL.value=Email;
}
</script>




