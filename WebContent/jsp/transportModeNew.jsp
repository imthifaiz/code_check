<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>


<%
String title = "Create Transport Mode";
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
	
 		subWin = window.open(URL, 'TransportMode', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</script>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res= "",action="",transportmode="",fieldDesc="",allChecked="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
transportmode  = strUtils.fString(request.getParameter("TRANSPORT_MODE"));



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
if(fieldDesc.equals("Transport Added Successfully")){
	response.sendRedirect("../transportmode/summary");
}
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../transportmode/summary"><span class="underline-on-hover">Edit Transport Mode</span></a></li>                       
                <li><label>Create Transport Mode</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../transportmode/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 <CENTER><strong><font style="font-size:40px;"><%=fieldDesc%></font></strong></CENTER>
<form class="form-horizontal" name="form" method="post" action=" ">
<div id = "ERROR_MSG"></div>
<div class="form-group">
<label class="control-label col-sm-2" for="Transportmode">Transport Mode</label>

<INPUT type="hidden" name="RFLAG" value="1">
<div class="col-sm-8">
<input name="TRANSPORTMODDE" id="TRANSPORTMODE" type="TEXT" value="<%=transportmode%>" onkeypress="return blockSpecialChar(event)"
			size="100" MAXLENGTH=200 class="form-control">
   </div>
        </div>
  
<div class="form-group">        
<div class="col-sm-7" align="center">
<button type="button" class="btn btn-success" value="Add To Transport Mode" onClick="onAdd()">Add To Transport Mode</button>&nbsp;&nbsp;
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
	document.form.TRANSPORTMODE.value="";
	
}

function onAdd() {
	var transportmode = document.form.TRANSPORTMODE.value;
	if(transportmode=="" || transportmode.length==0 ) {
		alert("Enter Transport Mode");
		document.getElementById("TRANSPORTMODE").focus();
		return false;
	}   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {TRANSPORT_MODE:transportmode,action: "ADD_TRANSPORT_MODE",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    document.form.TRANSPORTMODE.value = "";
          
}


function onGo(index) {

	var index = index; 
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {PLANT:"<%=plant%>",action: "VIEW_TRANSPORT_MODE",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
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
		
        $.each(data.transportmaster, function(i,item){
                   
        	outPutdata = outPutdata+item.TRANSPORTMASTERDATA;
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
		 if(errorMsg=="Transport Mode Added Successfully")
	 	{
	 	document.form.action  = "../transportmode/summary?result=Transport Mode Added Successfully";
	 	document.form.submit();
	 	}
	      	     
	}

	function getTable(){
	        return '<TABLE class="table" id="tabledata" BORDER="0" cellspacing="0" WIDTH="85%"  align = "center">'+
	 	           '<thead style="background:#eaeafa">'+
		           '<tr>'+
	 		       '<th width="5%">S/No</th>'+
	 		       '<th width="75%">Transport Mode</th>'+
	 	           '</tr>'+
	 	           '</thead>';

	}

document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>

<jsp:include page="footer2.jsp" flush="true">
<jsp:param name="title" value="<%=title %>" />
</jsp:include>
