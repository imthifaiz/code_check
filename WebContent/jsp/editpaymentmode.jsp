<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />

<%
String title = "Edit Payment Mode";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">



<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'PaymentMode', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</SCRIPT>

<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res= "",action="",paymentmode="",fieldDesc="",allChecked="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
paymentmode  = strUtils.fString(request.getParameter("PAYMENTMODE"));
fieldDesc=StrUtils.fString(request.getParameter("result"));

boolean displaySummaryDelete=false,displaySummaryNew=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryDelete = ub.isCheckValAcc("PaymentModeremove", plant,USERID);
displaySummaryNew = ub.isCheckValAcc("PaymentModenew", plant,USERID);

}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryDelete = ub.isCheckValinv("PaymentModeremove", plant,USERID);
displaySummaryNew = ub.isCheckValinv("PaymentModenew", plant,USERID);
}

System.out.println("action "+action );



if(action.equalsIgnoreCase("result"))
{
  fieldDesc=(String)request.getSession().getAttribute("RESULT");
  System.out.println("fieldesc"+fieldDesc);
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

%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Payment Mode</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <div class="box-title pull-right">
                 <%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default"onclick="window.location.href='../PaymentMode/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
                <%} %> 
              </div>
		</div>
		
 <div class="box-body">

  
<form class="form-horizontal" name="form" method="post" action="/track/MasterServlet?">
<div id = "ERROR_MSG"></div>
<INPUT type="hidden" name="RFLAG" value="1">
 	 
<div id="RESULT_MESSAGE">
<table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
<tr><td align="center"><%=fieldDesc%></td></tr>
</table>
</div>


<div class="row">
<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
   <strong>  &nbsp; Select/Unselect All </strong> </div>
   </div>
   

<div id="VIEW_RESULT_HERE"></div>
<div id="spinnerImg" ></div>

<div class="form-group">        
<div class="col-sm-12" align="center">
<%if(displaySummaryDelete){ %>
<button type="button" class="Submit btn btn-default" value="Remove From Payment Mode" onClick="onDelete()">Remove From Payment Mode</button>&nbsp;&nbsp;
 <%} %>
	</div>
	 </div>
	 
	 </form>
  </div>
  </div>
  </div>
  
  
  

<script>
onGo(0);

 function checkAll(isChk)
{
	var len = document.form.chkitem.length;
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
 

 
  function onGo(index) {
    
  var index = index;
  document.getElementById('RESULT_MESSAGE').innerHTML = ''; 
  document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
  document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
  var urlStr = "/track/MasterServlet";
  
  // Call the method of JQuery Ajax provided
  $.ajax({type: "POST",url: urlStr, data: {PLANT:"<%=plant%>",action: "VIEW_PAYMENTMODE_DETAILS_EDIT",PLANT:"<%=plant%>"},dataType: "json", success: callback });


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
	  	  document.form.action="/track/MasterServlet?action=DELETE_PAYMENTMODE";
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
		
        $.each(data.paymentmodemaster, function(i,item){
                   
        	outPutdata = outPutdata+item.PAYMENTMODEMASTERDATA;
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
      	     
}

function getTable(){
        return '<TABLE class="table" id="tabledata" BORDER="0" cellspacing="0" WIDTH="85%"  align = "center">'+
 	           '<thead style="background:#eaeafa">'+
	           '<tr>'+
 		       '<th width="5%">Select</th>'+
 		       '<th width="5%">S/No</th>'+
 		       '<th width="75%">Payment Mode</th>'+
 	           '</tr>'+
 	           '</thead>';

}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>

<jsp:include page="footer2.jsp" flush="true">
<jsp:param name="title" value="<%=title %>" />
</jsp:include>