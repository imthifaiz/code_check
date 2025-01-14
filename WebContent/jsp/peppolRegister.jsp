<!-- CREATED BY imthi -->
<!-- CREATED ON 24-12-2022 -->
<!-- DESC : peppol Registeration-->
<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@include file="sessionCheck.jsp" %>
<%String title = "Peppol ID Registration";%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.INTEGRATIONS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PEPPOL%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<style>
a.grey-link {
  color: grey;
}
</style>

<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String action="",fieldDesc="";
session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
// fieldDesc =  StrUtils.fString(request.getParameter("result"));
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
Map plntMap = (Map) plntList.get(0);
String peppol_id = (String) plntMap.get("PEPPOL_ID");
String peppol_ukey = (String) plntMap.get("PEPPOL_UKEY");
%>
<div id = "SUCCESS_MSG"></div>
<div class="container-fluid m-t-20">
<div class="box"> 
	 <ul class="breadcrumb backpageul">    
		<li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
		<li><a href="../integrations/peppolintegration"><span class="underline-on-hover">Peppol Integration</span> </a></li>                    
		<li><label>Peppol ID Registration</label></li>                                   
	</ul>
	<div class="box-header menu-drop">
		<h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		<h1 style="font-size: 18px; cursor: pointer;"class="box-title pull-right"
		onclick="window.location.href='../integrations/peppolintegration'">
		<i class="glyphicon glyphicon-remove"></i></h1>
	</div>
<div class="box-body">
<CENTER><strong><font style="font-size:40px;"><%=fieldDesc%></font></strong></CENTER>
<form class="form-horizontal" name="form" method="post" action="/track/MasterServlet?">
<div class="form-group">
   <div class="form-group" style="margin-block: auto;">
			<label class="col-form-label col-sm-12" for="RegsiterPeppolID"><a id="URL_LINK" href="https://portal.einvoice.sg/signup" target="_blank" onclick="return checkInput()" style="left: 46px;">Click here to register PEPPOL ID for Company</a></label>
	</div>
		
	<div class="form-group" style="margin-bottom: auto;">
<!-- 	    <label class="control-label col-form-label col-sm-6" style="color: red;">*(Terms &amp; Condition : To Register Peppol ID Follow Steps in the Link)</label> -->
<label class="control-label col-form-label col-sm-12" style="color: red;/* text-decoration-line: underline; */">Terms &amp; Conditions:</label>
<label class="control-label col-form-label col-sm-12" style="color: red;left: 45px;"><b>*</b> To register PEPPOL ID please click and follow the steps in the link</label>
<label class="control-label col-form-label col-sm-12" style="color: red;left: 45px;"><b>*</b> Once successfully registered the PEPPOL ID then copy and paste the Id and click on the save button</label>
	</div>
       
	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Peppol ID">Peppol ID:</label>
      	<div class="col-sm-3">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="PEPPOL_ID" id="PEPPOL_ID" type="TEXT" value="<%=peppol_id%>" oninput="updateLink()" size="50" maxlength="50" style="left: -85px;"> 
  			</div>
      	</div>
	</div>
       
	<div class="form-group" style="display: none;">
      	<label class="control-label col-form-label col-sm-2" for="Peppol UKEY">Peppol UKEY:</label>
      	<div class="col-sm-3">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="PEPPOL_UKEY" id="PEPPOL_UKEY" type="TEXT" value="<%=peppol_ukey%>" size="50" maxlength="50" style="left: -85px;"> 
  			</div>
      	</div>
	</div>
</div>
  
	<div class="form-group">        
		<div class="col-sm-7" align="center">
			<button type="button" class="btn btn-success" value="Update Peppol ID" onClick="onAdd()">Save</button>&nbsp;&nbsp;
		</div>
	</div>
	
</form>
</div>
</div>
</div>
<script type="text/javascript">
updateLink();
function onAdd() {
 	var peppol_id   = document.form.PEPPOL_ID.value;
 	var peppol_ukey   = document.form.PEPPOL_UKEY.value;
    var urlStr = "/track/MasterServlet";
    $.ajax({type: "POST",url: urlStr, data: {PEPPOL_ID:peppol_id,PEPPOL_UKEY:peppol_ukey,action: "ADD_PEPPOL",PLANT:"<%=plant%>"},dataType: "json", success: callback });
}

function callback(data){
     var message = data.message;
     if(typeof(message) == "undefined"){message = "";}
     HTMLmsg = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+message+"</td></tr></table>"; 
     document.getElementById('SUCCESS_MSG').innerHTML = HTMLmsg;
 }
 
function updateLink() {
	var input = document.getElementById("PEPPOL_ID").value;
	var link = document.getElementById("URL_LINK");
	if (!input) {
	    link.classList.remove("grey-link");
	    //link.style.display = "inline";
	} else {
	    link.classList.add("grey-link");
	}
}

function checkInput() {
	var input = document.getElementById("PEPPOL_ID").value;
	var link = document.getElementById("URL_LINK");
	if (!input) {
	  return true;
	} else {
	  return false;
	}
}
</script>

<jsp:include page="footer2.jsp" flush="true">
<jsp:param name="title" value="<%=title %>" />
</jsp:include>
