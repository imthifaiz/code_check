<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<html>
<head>
<title>Payment Type  List</title>
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
<body>
<script type="text/javascript" src="../js/general.js"></script>
<SCRIPT LANGUAGE="JavaScript">



function popWin(URL) {
	 subWin = window.open(URL, 'Payment Type', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
	function onNew(){
	   document.form1.action  = "paymenttypelist_save.jsp?action=Clear";
	   document.form1.submit();
	}
	function onAdd(){
	   var ITEM_ID   = document.form1.ITEM_ID.value;
	      if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Payment Type");document.form1.ITEM_ID.focus(); return false; }
	   
	   document.form1.action  = "paymenttypelist_save.jsp?action=ADD";
	   document.form1.submit();
	}


	


function onClear(){
	document.form1.ITEM_ID.value = "";
	}
</SCRIPT>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String EDIT = (String) session.getAttribute("EDIT");
String sNewEnb = "enabled";
String sAddEnb = "disabled";
sAddEnb = "enabled";
String action = "";
String sItemId = "", sPrdClsId = "", sItemDesc = "";

MLogger mLogger = new MLogger();
ArrayList arrCust = new ArrayList();
StrUtils strUtils = new StrUtils();
MasterUtil masterutil = new MasterUtil();
String responseMsg = request.getParameter("response");
String paymenttype = request.getParameter("paymenttype");
String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER"));
action    = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sItemId  = strUtils.fString(request.getParameter("ITEM_ID"));
EDIT  = strUtils.fString(request.getParameter("EDIT"));
if(sItemId.length() <= 0) sItemId  = strUtils.fString(request.getParameter("ITEM_ID1"));

//User Check 	
	boolean al=false;
	al = ub.isCheckVal("paymenttypepopup", plant,sUserId);
	if(al==true)
	{		
		System.out.println("paymenttypepopup");
	}

//1. >> New
if(action.equalsIgnoreCase("Clear")){
    sItemId  = "";
  

}

//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
	

 Hashtable ht = new Hashtable(); 
	ht.put(IDBConstants.PLANT, plant);
	ht.put(IDBConstants.PAYMENTTYPE, sItemId);
	if (!(masterutil.isExistPaymentType(sItemId, plant))) // if the Item exists already
	{
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.PAYMENTTYPE, sItemId);
	
		new DateUtils();
		ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
		ht.put(IDBConstants.LOGIN_USER, userName);

		MovHisDAO mdao = new MovHisDAO(plant);
		mdao.setmLogger(mLogger);
		Hashtable<String, String> htm = new Hashtable<String, String>();
		htm.put(IDBConstants.PLANT, plant);
		htm.put("DIRTYPE", TransactionConstants.ADD_PAYMENTTYPE);
		htm.put("RECID", "");
		htm.put("ITEM",sItemId);
		htm.put("REMARKS", sItemId);
		htm.put("UPBY", userName);
		htm.put("CRBY", userName);
		htm.put("CRAT", DateUtils.getDateTime());
		htm.put("UPAT", DateUtils.getDateTime());
		htm.put(IDBConstants.TRAN_DATE, DateUtils
				.getDateinyyyy_mm_dd(DateUtils.getDate()));

		boolean itemInserted =masterutil.AddPaymentType(ht);
		boolean inserted = mdao.insertIntoMovHis(htm);
		
		 if(itemInserted&&inserted) {
			 responseMsg = "<font class = "+IDBConstants.SUCCESS_COLOR +">Payment Type Added Successfully</font>";

          
   } else {
	   responseMsg = "<font class = "+IDBConstants.FAILED_COLOR +">Failed to add New Payment Type </font>";
           
   }
	} else {
		responseMsg = "<font class = " + IDBConstants.FAILED_COLOR+ ">Payment Type  Exists already. Try again</font>";

}
}


if(responseMsg == null)
	responseMsg = "";




%>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Payment Type List</h3> 
</div>
</div>


<form class="form-horizontal" method="post" name="form1">

 <div class="box-body">
 
   <CENTER><strong><%=responseMsg%></strong></CENTER>

  <div id="target" style="display:none">
   
    <div class="form-group">
      <label class="control-label col-sm-4" for="Payment Type">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Payment Type:</label>
      <div class="col-sm-3">
        <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />        	  
        <INPUT  class="form-control" name="ITEM_ID" type="TEXT" value="<%=sItemId%>"
			size="150" MAXLENGTH=100>
      </div> 
    </div>
   
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">      
      	<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      	 <% if (al) { %>
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
      	<% } else { %>
      	<button disabled="disabled" type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
      	 <% } %>  
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>
      </div>
    </div>
    
</div>

<div class="form-group">
      <div class="col-sm-4">
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Create Payment Type</a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Create Payment Type</a>
      </div>
       	  </div>

</div>

<div>
<table id="myTable" class="table">
<thead style="background: #eaeafa"> 
   <tr>
        <th>Payment Type</th>
         </tr>
    </thead>
    <tbody>
    

    
<%

String PLANT = "", Class = "";

HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());

mLogger.setLoggerConstans(loggerDetailsHasMap);
   
    PLANT = session.getAttribute("PLANT").toString();
    
    
    String sBGColor = "";
    String paytype  = strUtils.fString(request.getParameter("PAYTYPE"));
   try{

    arrCust = masterutil.getPaymentTypeList(plant,"");
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrCustLine = (Map)arrCust.get(i);
        String strpaymenttype  = (String)arrCustLine.get("PAYMENTTYPE");
       
        

%>
<%if(EDIT.equals("EDIT")){%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.PAYMENTTYPE.value='<%=strUtils.insertEscp(strpaymenttype)%>';window.close();"><%=strpaymenttype%></a></td>
		
	</TR>
	<%} else if(paytype.equalsIgnoreCase("SUPMODALUOM")){%>
		<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.supplierForm.PAYTERMS.value='<%=strUtils.insertEscp(strpaymenttype)%>';window.close();"><%=strpaymenttype%></a></td>
		
	</TR>
	<%} else if(paytype.equalsIgnoreCase("CUSTMODAL")){%>
		<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.customerForm.PAYTERMS.value='<%=strUtils.insertEscp(strpaymenttype)%>';window.close();"><%=strpaymenttype%></a></td>
		
	</TR>
	<%} else if(paytype.equalsIgnoreCase("POPUP1")){%>
	<TR>
	<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
		onClick=" window.opener.form1.PAYTERMS.value='<%=strUtils.insertEscp(strpaymenttype)%>';window.close();"><%=strpaymenttype%></a></td>
	
	</TR>
	<%} else if(paytype.equalsIgnoreCase("POPUP2")){%>
	<TR>
	<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
		onClick=" window.opener.formsupplier.PAYTERMS.value='<%=strUtils.insertEscp(strpaymenttype)%>';window.close();"><%=strpaymenttype%></a></td>
	
	</TR>
	<%}else{%>
	<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.PAYTERMS.value='<%=strUtils.insertEscp(strpaymenttype)%>';window.close();"><%=strpaymenttype%></a></td>
		
	</TR>
    
   
<%
}
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>


</tbody>
</table>
      
</div>
</form>
</body>
</html>

<script>
$(document).ready(function(){
	$('#myTable').dataTable();
    
    $('[data-toggle="tooltip"]').tooltip();

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