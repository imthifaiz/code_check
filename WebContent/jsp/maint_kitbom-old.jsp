<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%
String title = "Edit Kit BOM";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'ProductionBOM', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",pitem="",pdescitem="",fieldDesc="",allChecked="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
pitem  = strUtils.fString(request.getParameter("ITEM"));
pdescitem = strUtils.fString(request.getParameter("DESC"));

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
  
  
}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='kitbomsummary.jsp'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

  <%--  <CENTER><strong><%=res%></strong></CENTER> --%>

  <form class="form-horizontal" name="form" method="post" action="/track/ProductionBomServlet?">
  <br>
  <INPUT type="hidden" name="RFLAG" value="2">
  <INPUT type="hidden" name="BATCH_0" value="">
  
  <div id = "ERROR_MSG"></div>
  
<div class="box-header menu-drop">
              <h2>
              <small>Parent Product Details</small>
              </h2>
		</div>

    <div class="form-group">
      <label class="control-label col-form-label col-sm-3 required" for="Product ID">Product ID</label>
      <!-- <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product ID:</label> -->
      <div class="col-sm-3">
      	<div class="input-group">    
    		<input type="TEXT"  name="ITEM" id="ITEM"	value="<%=pitem%>"	onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}"
			size="20" MAXLENGTH=100 class="form-control">
   		 	<span class="input-group-addon" 
   		  onClick="javascript:popUpWin('ProductionBOMPitemList.jsp?ITEM='+form.ITEM.value+'&TYPE=KITBOM');"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
      <label class="control-label col-form-label col-sm-3" for="Prd Desc">Product Description</label>
      <div class="col-sm-3">
      	<div class="input-group">    
    		<input name="DESC" id="DESC" type="TEXT" value="<%=pdescitem%>"
			size="20" MAXLENGTH=100 class="form-control">
   		 	<span class="input-group-addon" 
   		  onClick="javascript:popUpWin('ProductionBOMPitemList.jsp?ITEM='+form.ITEM.value+'&TYPE=KITBOM');"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
      </div>
    </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-form-label col-sm-3" for="Detailed Description">Detailed Description</label>
      <div class="col-sm-3">          
        <INPUT  class="form-control" name="DETDESC" id="DETDESC" type="TEXT" value=""
			size="20" MAXLENGTH=100 readonly>
      </div>
    
   
   
 <div class="form-inline">
      <label class="control-label col-form-label col-sm-3" for="Quantity">Quantity</label>
      <div class="col-sm-2">          
        <INPUT  class="form-control" name="PARENTQTY" type="TEXT" id="PARENTQTY" value="1" size="4" MAXLENGTH=50 readonly>
		</div>	
      </div>
      
      <div class="form-inline">        
       	<button type="button" class="Submit btn btn-default" onClick="onGo(1)">View</button>&nbsp;&nbsp; 	
      </div>
    </div>
   
    <br>
    <div class="form-group">        
      <div class="col-sm-12" align="center">
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      </div>
 </div>
 
   
       
    <div id="RESULT_MESSAGE">
          <table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
	     <%-- <tr><td align="center"><%=fieldDesc%></td></tr> --%>
	</table>
     </div>
     
     <br>
 
<div class="panel panel-default">
<div class="panel-heading" style="background-color: #eaeafa " align="center">
<h3 class="panel-title"><b>Child Product Summary</b></h3> 
</div>
</div>

     
     

<div class="form-group">
      
      <div class="col-sm-2">    
      <label class="checkbox-inline">      
        <INPUT Type=Checkbox  class="form-check-input" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);"
			size="50" MAXLENGTH=100 onclick="DisplayAddress();">Select/Unselect</label>
			</div>
    </div>
		
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
   <div class="form-group">        
      <div class="col-sm-12" align="center">
					<button class="Submit btn btn-default" type="button" onClick="onDelete()">Remove from  BOM</button>
					</div>
					</div>
	
<input type="hidden" name="PTYPE" id="PTYPE" value="CREATEKITBOM">



    
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>


<script>
onGo(0);
function checkAll(isChk)
{
	var len = document.form.chkitem.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkitem)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkitem.checked = isChk;
              		 
              	}
              	else{
              		document.form.chkitem[i].checked = isChk;
              		 
              	}
            	   	
                
        }
    }
}
function validateProduct() {
	var productId = document.form.ITEM.value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
	}else{
	var urlStr = "/track/MiscOrderHandlingServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		ITEM : productId,
		PLANT : "<%=plant%>",
		ACTION : "VALIDATE_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("DESC").value = resultVal.discription;
							document.getElementById("DETDESC").value = resultVal.detaildesc;
							document.form.CITEM.focus();

						} else {
							alert("Not a valid product!");
							document.form.ITEM.value = "";
							document.form.ITEM.focus();
						}
					}
				});
		}
	}
function onGo(index) {

	var index = index;
	var product = document.form.ITEM.value;
	//var childproduct = document.form.CITEM.value;
	
	if(index == '1'){
	if(product=="" || product.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
		return false;
		}
     
	}
	document.getElementById('RESULT_MESSAGE').innerHTML = ''; 
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,action: "VIEW_KITBOM_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
}
function onDelete()
{
	var checkFound = false;
	var chkitems = document.form.chkitem.value;
	 var len = document.form.chkitem.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
   {
		if(len == 1 && (!document.form.chkitem.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkitem.checked)
	     {
	    	 checkFound = true;
	    	 
	     }
	
	     else {
		     if(document.form.chkitem[i].checked){
		    	 checkFound = true;
		    	 
		     }
	     }
         		
       	     
   }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var chkmsg = confirm("Are you sure you would like to delete?");
		if (chkmsg) {
	  	  document.form.action="/track/ProductionBomServlet?action=DELETE_KITBOM";
	 	 document.form.submit();
	 	 return true;   
		}
		 else {
				return false;
			}
	
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
		
        $.each(data.kittingbom, function(i,item){
                   
        	outPutdata = outPutdata+item.KITBOMDATA;
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
     
}

function getTable(){
        return '<TABLE BORDER="0" id="tabledata" cellspacing="0" WIDTH="100%"  align = "center" class="table">'+
        	   '<tr style="background:#eaeafa">'+
         		'<th width="5%"><font >Select</font></th>'+
         		'<th width="5%"><font >No</font></th>'+
         		'<th width="10%"><font >Prod ID</font></th>'+
         		'<th width="11%"><font >Prod Desc</font></th>'+
         		'<th width="13%"><font >Prod Detail Desc</font></th>'+
         		'<th width="10%"><font >Equivalent Prod ID</font></th>'+
         		'<th width="11%"><font >Equivalent Prod Desc</font></th>'+
         		'<th width="15%"><font >Equivalent Prod Detail Desc</font></th>'+
         		'<th width="5%"><font >BOM QTY</font></th>'+
         		'</tr>';
       
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script> 


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>