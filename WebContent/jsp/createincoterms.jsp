<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>

<%
String title = "Create INCOTERM Details";
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
	
 		subWin = window.open(URL, 'INCOTERMS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res= "",action="",incoterms="",fieldDesc="",allChecked="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
incoterms  = strUtils.fString(request.getParameter("INCOTERMS"));



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

%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../incoterms/edit"><span class="underline-on-hover">Edit INCOTERM Details</span></a></li>                       
                <li><label>Create INCOTERM Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../incoterms/edit'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
    <CENTER><strong><font style="font-size:40px;"><%=fieldDesc%></font></strong></CENTER>
<form class="form-horizontal" name="form" method="post" action="/trackk/MasterServlet?">
<div id = "ERROR_MSG"></div>
<div class="form-group">
<label class="control-label col-sm-2" for="INCOTERM">INCOTERM</label>
<!-- <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;INCOTERM</label> -->
<INPUT type="hidden" name="RFLAG" value="1">
<div class="col-sm-4">
<input name="INCOTERMS" id="INCOTERMS"	value="<%=incoterms%>" onkeypress="return blockSpecialChar(event)"
			size="100" MAXLENGTH=200 class="form-control">
   </div>
        </div>
        
<div class="form-group">        
<div class="col-sm-7" align="center">
<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
<button type="button" class="btn btn-success" value="Add To INCOTERMS" onClick="onAdd()">Add To INCOTERMS</button>&nbsp;&nbsp;
<button type="button" class="Submit btn btn-default" value="Clear" onClick="onClear();">Clear</button>&nbsp;&nbsp;
 
	</div>
	 </div>
	 
<div id="RESULT_MESSAGE">
<table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
<tr><td align="center"><%=fieldDesc%></td></tr>
</table>
</div>

   
<div id="VIEW_RESULT_HERE"></div>
<div id="spinnerImg" ></div>
 
	 </form>
  </div>
  </div>
  </div>  
  
        

<script>
onGo(0);
function onClear()
{
	document.form.INCOTERMS.value="";
	/* debugger;
	document.getElementById("INCOTERMS").value = ""; */
	
}


function onAdd() {
	var incoterms = document.form.INCOTERMS.value;
	if(incoterms=="" || incoterms.length==0 ) {
		alert("Enter INCOTERMS");
		document.getElementById("INCOTERMS").focus();
		return false;
	}
	document.getElementById('RESULT_MESSAGE').innerHTML = '';   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {INCOTERMS:incoterms,action: "ADD_INCOTERMS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    document.form.INCOTERMS.value = "";
          
}


function onGo(index) {

	var index = index;
	document.getElementById('RESULT_MESSAGE').innerHTML = ''; 
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {PLANT:"<%=plant%>",action: "VIEW_INCOTERMS_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
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
		
        $.each(data.incotermsmaster, function(i,item){
                   
        	outPutdata = outPutdata+item.INCOTERMSMASTERDATA;
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
     if(errorMsg=="INCOTERM Added Successfully")
  	{
  	document.form.action  = "../incoterms/edit?result=INCOTERM Added Successfully";
  	document.form.submit();
  	}
}

function getTable(){
        return '<TABLE class="table" id="tabledata" BORDER="0" cellspacing="0" WIDTH="85%"  align = "center">'+
               '<thead style="background:#eaeafa">'+
               '<tr>'+
	           '<th width="5%">S/No</th>'+
	           '<th width="75%">INCOTERM</th>'+
               '</tr>'+
               '</thead>';
       
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>

<jsp:include page="footer2.jsp" flush="true">
<jsp:param name="title" value="<%=title %>" />
</jsp:include>