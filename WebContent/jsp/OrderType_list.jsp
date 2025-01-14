<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<html>
<head>
<title>Order Type List</title>
<link rel="stylesheet" href="css/style.css">
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

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

<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'ORDERTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=900,height=900,left = 200,top = 184');
	}
	function onNew(){
		document.form1.action  = "OrderType_list.jsp?action=Clear";
   		document.form1.submit();
	}
	
	function onAdd(){
   		var ORDERTYPE   = document.form1.ORDERTYPE.value;
                var ORDERDESC   = document.form1.ORDERDESC.value;
                if(ORDERTYPE == "" || ORDERTYPE == null) {alert("Please Enter OrderType");	document.form1.ORDERTYPE.focus(); return false; }
                if(ORDERDESC == "" || ORDERDESC == null) {alert("Please Enter Order Description"); document.form1.ORDERDESC.focus(); return false; }
   		document.form1.action  = "/track/OrderTypeServlet?action=ADD1";
   		document.form1.submit();
	}
</script>

<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String sOrderType = "", sOrderDesc = "", sType="",sRemarks = "";
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	String EDIT    = StrUtils.fString(request.getParameter("EDIT"));
	String formtype    = StrUtils.fString(request.getParameter("FORMTYPE"));
	
	//User Check 	
	boolean al=false;
	al = ub.isCheckVal("ordertypepopup", plant,sUserId);
	if(al==true)
	{		
		System.out.println("ordertypepopup");
	}
	
	  if(formtype.equals("inbound")) {  
		  		  sType="PURCHASE";
		  }
	 if(formtype.equals("outbound")) {
		 sType="SALES";
		 }
if(formtype.equals("estimate")) { 
	sType="SALES ESTIMATE";
	}
if(formtype.equals("taxinvoice")) { sType="TAX INVOICE";
}
if(formtype.equals("others")) { 
	sType="OTHERS";
}
if(formtype.equals("rental")) { 
	sType="RENTAL";
	}
if(formtype.equals("")) { sType="";
}
	
	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		sOrderType = "";
		sOrderDesc = "";
		sType = "";
		sRemarks = "";
	} else if (action.equalsIgnoreCase("SHOW_RESULT")) {
		res = request.getParameter("result");
		Hashtable arrCust = (Hashtable) request.getSession()
				.getAttribute("orderTypeData");
		sOrderType = (String) arrCust.get("ORDERTYPE");
		sOrderDesc = (String) arrCust.get("ORDERDESC");
		sType = (String) arrCust.get("TYPE");
		sRemarks = (String) arrCust.get("REMARKS");
		formtype = (String) arrCust.get("FORMTYPE");
	} else if (action.equalsIgnoreCase("UPDATE")) {
		sCustEnb = "disabled";
	}
%>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Order Type List</h3> 
</div>
</div>
<body bgcolor="#ffffff">
<form method="post" name="form1">
<CENTER><strong><%=res%></strong></CENTER>
<INPUT  class="form-control" name="FORMTYPE" type="hidden" value="<%=formtype%>"
			size="50" MAXLENGTH=100>
 <% if(!EDIT.equals("EDIT")) { System.out.println("EDIT "+EDIT);%>
 <div id="target" style="display:none">
 		<div class="row">
		<div class="col-sm-3" style="padding:6px;">
      	<label for="Order Type Master">&nbsp;&nbsp;&nbsp;&nbsp;
      	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>Order Type:</label></div>
      	<div class="col-sm-3">
		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
      	<input name="ORDERTYPE" type="TEXT" value="<%=sOrderType%>"	size="50" MAXLENGTH=50 class="form-control"<%=sCustEnb%>>
      	</div>
        <div class="col-sm-3" style="padding:6px;">
      	<label  for="Order Type Description">&nbsp;&nbsp;
      	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>Description:</label></div>
      	<div class="col-sm-3">          
        <INPUT  class="form-control" name="ORDERDESC" type="TEXT" value="<%=sOrderDesc%>" style="width:100%" size="50" MAXLENGTH=100>
      	</div>
    	</div>
     
     <div class="row">
     <div class="col-sm-3" style="padding:6px;">
     <label for="Order Type">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Type:</label></div>
     <div class="col-sm-3"><INPUT name="TYPE" class="form-control" type="text" value="<%=sType%>" size="50" MAXLENGTH=100 readonly></div>
  	 <div class="col-sm-3" style="padding:6px;">          
     <label  for="Order Type">&nbsp;&nbsp;&nbsp;&nbsp;Remarks:</label></div>
     <div class="col-sm-3"><INPUT  class="form-control" name="REMARKS" type="text" value="<%=sRemarks%>" size="50" MAXLENGTH=100>
     </div>
     </div>
    
  
    	<div class="form-group">        
      	<div class="col-sm-offset-4 col-sm-4" >
		<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
       	<% if (al) { %>
       	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      <% } else { %>
     	<button disabled="disabled" type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
     <% } %>
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>
        </div>
    	</div>
   </div>
   <%}%>
   
   <div class="row">
      <div class="col-sm-4">
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Create Order Type</a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Create Order Type</a>
      </div>
       	  </div>
   
<table id="myTable" class="table">
    <thead style="background: #eaeafa">
	<TR>
		<TH align="left">Order Type</TH>
		<TH align="left">Order Desc</TH>
		<TH align="left">Type</TH>
		<TH align="left">IsActive</TH>
	</TR>
	</thead>
	<tbody>
	<%

//HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
//MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
    //String plant= (String)session.getAttribute("PLANT");
   formtype=""; 
   // StrUtils strUtils = new StrUtils();
    String sCustName = strUtils.fString(request.getParameter("ORDERTYPE")).trim();
    String sBGColor = "";
    try
    {
    	OrderTypeUtil _OrderTypeUtil = new OrderTypeUtil();
    	_OrderTypeUtil.setmLogger(mLogger);
    	formtype=request.getParameter("FORMTYPE");
    	List arrCust=new ArrayList();
    	System.out.println("formtype"+formtype);
    	if(formtype.equalsIgnoreCase("notype")){
    		
   		if(sCustName=="" || sCustName == null){
    			
    	 arrCust = _OrderTypeUtil.getAllOrderTypeDetails(plant,"");
   		}
   		else{
    			
      arrCust = _OrderTypeUtil.getOrderTypeDetails(sCustName,plant);
   		}
    	}
    	else if(formtype.equalsIgnoreCase("outbound"))
    	{
    		 arrCust = _OrderTypeUtil.getTypeDetails("SALES",plant);	
    	}
    	else if(formtype.equalsIgnoreCase("inbound"))
    	{
    		 arrCust = _OrderTypeUtil.getTypeDetails("PURCHASE",plant);	
    	}
    	else if(formtype.equalsIgnoreCase("estimate"))
    	{
    		 arrCust = _OrderTypeUtil.getTypeDetails("SALES ESTIMATE",plant);	
    	}
    	else if(formtype.equalsIgnoreCase("others"))
    	{
    		 arrCust = _OrderTypeUtil.getTypeDetails("OTHERS",plant);	
    	}
		else if(formtype.equalsIgnoreCase("taxinvoice"))
    	{
    		 arrCust = _OrderTypeUtil.getTypeDetails("TAX INVOICE",plant);	
    	}
		else if(formtype.equalsIgnoreCase("rental"))
    	{
    		 arrCust = _OrderTypeUtil.getTypeDetails("RENTAL",plant);	
    	}
    	else
    	{
    		if(sCustName=="" || sCustName == null){
    			
    	    	 arrCust = _OrderTypeUtil.getAllOrderTypeDetails(plant,"");
    	   		}
    	   		else{
    	    			
    	      arrCust = _OrderTypeUtil.getOrderTypeDetails(sCustName,plant);
    	   		}
    	}
    	//ArrayList arrCust = _OrderTypeUtil.getAllOrderTypeDetails(plant,"");
    	
    	for(int i =0; i<arrCust.size(); i++) {
        	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        	Map arrCustLine = (Map)arrCust.get(i);
        	 sOrderType    = strUtils.fString((String)arrCustLine.get("ORDERTYPE"));
           	 sOrderDesc   = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("ORDERDESC")));
        	 sType="";
        	if(formtype.equalsIgnoreCase("estimate"))
        	{
        		sType="Estimate";
        		 
        	}
        	else
        	{
        	   sType   = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("TYPE")));
        	}
        	String sRemark      = strUtils.fString(strUtils.removeQuotes((String)arrCustLine.get("REMARKS")));
            String isactive      = strUtils.fString((String)arrCustLine.get("ISACTIVE"));
%>
	<TR bgcolor="<%=sBGColor%>">

		<%if(formtype.equalsIgnoreCase("outbound")||formtype.equalsIgnoreCase("inbound")|| formtype.equalsIgnoreCase("rental")|| formtype.equalsIgnoreCase("others")||formtype.equalsIgnoreCase("PMTSMRY")||formtype.equalsIgnoreCase("estimate")||formtype.equalsIgnoreCase("taxinvoice")){%>
		<td class="main2"><a href="#"
			onClick="
      window.opener.form.ORDERTYPE.value='<%=sOrderType%>';
      window.close();"><%=sOrderType%>
		</a></td>
		<%} else{%>

		<td class="main2"><a href="#"
			onClick="
      window.opener.form1.ORDERTYPE.value='<%=sOrderType%>';
      window.opener.form1.ORDERTYPE1.value='<%=sOrderType%>';
      window.opener.form1.ORDERDESC.value='<%=sOrderDesc%>';
      window.opener.form1.TYPE.value='<%=sType%>';
      window.opener.form1.REMARKS.value='<%=sRemark%>';
      setCheckedValue( window.opener.form1.ACTIVE,'<%=isactive%>');
      window.close();"><%=sOrderType%>
		</a></td>
		<%} %>
		<td class="main2"><%=sOrderDesc%></td>
		<td class="main2"><%=sType%></td>
		<td class="main2"><%=isactive%></td>
	</TR>
	<%
		}
	}catch(Exception he){he.printStackTrace(); 
		System.out.println("Error in reterieving data");
	}
%>
	</tbody>
		
</table>
   
</form>
<script>
$(document).ready(function(){
	$('#myTable').dataTable();
	
	//Below Jquery Script used for Show/Hide Function
    
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
</script>
</body>
</html>
