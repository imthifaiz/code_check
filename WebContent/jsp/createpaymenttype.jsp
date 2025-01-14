<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>


<%
String title = "Create Payment Type Master";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">



<script type="text/javascript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'Remarks', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</script>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res= "",action="",paymenttype="",fieldDesc="",allChecked="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
paymenttype  = strUtils.fString(request.getParameter("PAYMENTTYPE"));



if(action.equalsIgnoreCase("result"))
{
  fieldDesc=(String)request.getSession().getAttribute("RESULT");
  fieldDesc = "<font class='maingreen'>" + fieldDesc + "</font>";
}
else if(action.equalsIgnoreCase("resulterror"))
{
	fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
	fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
}

if(action.equalsIgnoreCase("catchrerror"))
{
  fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
  fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
  allChecked = strUtils.fString(request.getParameter("allChecked"));

  
  
}
if(fieldDesc.equals("Payment Type Added Successfully")){
	response.sendRedirect("../paymenttype/edit");
}
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../paymenttype/edit"><span class="underline-on-hover">Edit Payment Type</span></a></li>                       
                <li><label>Create Payment Type Master</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../paymenttype/edit'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 <CENTER><strong><font style="font-size:40px;"><%=fieldDesc%></font></strong></CENTER>
<form class="form-horizontal" name="form" method="post" action="/track/MasterServlet?">
<div id = "ERROR_MSG"></div>
<div class="form-group">
<label class="control-label col-sm-2" for="Payment Type">Payment Type</label>
<INPUT type="hidden" name="RFLAG" value="1">
<div class="col-sm-4">
<input name="PAYMENTTYPE" id="PAYMENTTYPE" type="TEXT" value="<%=paymenttype%>" onkeypress="return blockSpecialChar(event)"
			size="100" MAXLENGTH=50 class="form-control">
   </div>
        </div>
  
<div class="form-group">        
<div class="col-sm-8" align="center">
<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
<button type="button" class="btn btn-success" value="Add To Payment Type" onClick="onAdd()">Add To Payment Type</button>&nbsp;&nbsp;
<button type="button" class="Submit btn btn-default" value="Clear" onClick="onClear();">Clear</button>&nbsp;&nbsp;
 
	</div>
	 </div>


   
<div id="VIEW_RESULT_HERE"></div>
<div id="spinnerImg" ></div>

	 
	 </form>
  </div>
  </div>
  </div>
  
  
  

<script type="text/javascript">
onGo(0);
function onClear()
{
	 document.form.PAYMENTTYPE.value = "";
	/* debugger;
	document.getElementById("PAYMENTTYPE").value = ""; */
	
}

function onAdd() {
	var paymenttype = document.form.PAYMENTTYPE.value;
	if(paymenttype=="" || paymenttype.length==0 ) {
		alert("Enter Payment Type");
		document.getElementById("PAYMENTTYPE").focus();
		return false;
	}
	//document.getElementById('RESULT_MESSAGE').innerHTML = '';   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {PAYMENTTYPE:paymenttype,action: "ADD_PAYMENTTYPE",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    document.form.PAYMENTTYPE.value = "";
          
}


function onGo(index) {

	var index = index;
	//document.getElementById('RESULT_MESSAGE').innerHTML = ''; 
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {PLANT:"<%=plant%>",action: "VIEW_PAYMENTTYPE_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
}



function callback(data){
	
	var outPutdata = getTable();
	var ii = 0;
	var errorBoo = false;
	$.each(data.errors, function(i,error){
		if(error.ERROR_CODE=="99"){
			errorBoo = true;
			
		}
	});
	
	if(!errorBoo){
		
        $.each(data.paymenttypemaster, function(i,item){
                   
        	outPutdata = outPutdata+item.PAYMENTTYPEMASTERDATA;
                    	ii++;
            
          });
        
	}
    outPutdata = outPutdata +'</TABLE>';
                                                  
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
     document.getElementById('spinnerImg').innerHTML =''; 
     var errorMsg = data.errorMsg;
     if(typeof(errorMsg) == "undefined"){
    	 errorMsg = "";
     }
     errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
     document.getElementById('ERROR_MSG').innerHTML = errorHTML;
     //document.form.TRANSACTIONNO.select();
	 //document.form.TRANSACTIONNO.focus();
	 if(errorMsg=="Payment Type Added Successfully")
 	{
 	document.form.action  = "../paymenttype/edit?result=Payment Type Added Successfully";
 	document.form.submit();
 	}
      	     
}

function getTable(){
        return '<TABLE class="table" id="tabledata" BORDER="0" cellspacing="0" WIDTH="85%"  align = "center">'+
 	           '<thead style="background:#eaeafa">'+
	           '<tr>'+
 		       '<th width="5%">S/No</th>'+
 		       '<th width="75%">Payment Type</th>'+
 	           '</tr>'+
 	           '</thead>';

}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>

<jsp:include page="footer2.jsp" flush="true">
<jsp:param name="title" value="<%=title %>" />
</jsp:include>
