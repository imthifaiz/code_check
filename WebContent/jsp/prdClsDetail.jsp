<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Product Category Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	function popWin(URL) {
		subWin = window
				.open(
						URL,
						'PRODUCT',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}

	function onView() {
		var ITEM_ID = document.form1.PRD_CLS_ID.value;
		if (ITEM_ID == "" || ITEM_ID == null) {
			alert("Please Enter Product CategoryID ");
			return false;
		}

		document.form1.action = "../jsp/maint_prdcls.jsp?action=VIEW";
		document.form1.submit();
	}
</script>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	String res = "";

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "disabled";
	String sUpdateEnb = "enabled";
	sAddEnb = "enabled";
	String action = "";
	String sItemId = "", sPrdClsId = "", sItemDesc = "", isActive = "",catalogpath="";

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	PrdClassUtil prdclsutil = new PrdClassUtil();

	prdclsutil.setmLogger(mLogger);

	DateUtils dateutils = new DateUtils();
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	sItemId = strUtils.fString(request.getParameter("PRD_CLS_ID"));
	sItemDesc = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("PRD_CLS_DESC")));
	isActive = strUtils.fString(request.getParameter("ISACTIVE"));
	if (sItemId.length() <= 0)
		sItemId = strUtils.fString(request.getParameter("PRD_CLS_ID1"));
	if(sItemId.length()>0) {
		ArrayList locQryList =  prdclsutil.getPrdTypeList(sItemId,plant,"");
		if (locQryList.size() > 0) {
			for(int i =0; i<locQryList.size(); i++) {
			Map arrCustLine = (Map)locQryList.get(i);
			catalogpath=(String)arrCustLine.get("CATALOG");
			}
		}
	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../productclass/summary"><span class="underline-on-hover">Product Category Summary</span></a></li>                        
                <li><label>Product Category Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
					onclick="window.location.href='../productclass/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
 
   <CENTER><strong><%=res%></strong></CENTER>


<form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Product Class ID">Product Category ID</label>
      <div class="col-sm-4">
      		<input name="PRD_CLS_ID" type="TEXT" value="<%=sItemId%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
		        
  		<INPUT type="hidden" name="PRD_CLS_ID1" value="">			
</div>
   </div>
<div class="form-group">
      <label class="control-label col-sm-4" for="Product Class Description">Product Category Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="PRD_CLS_DESC" type="TEXT" value="<%=sItemDesc%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
      <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y" disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%> >Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%> >Non Active
    </label>
     </div> 
</div>

<div class="row">
			<div class="col-sm-offset-4 col-sm-4">				
				<img src="../jsp/dist/img/NO_IMG.png" id="item_img" name="CATALOGPATH" alt="new image"
				class="img-thumbnail img-responsive col-sm-3" style="width: 70%;float:revert; padding: 3px;">
			</div>
		</div>

<div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
    <!--  <button type="button" class="Submit btn btn-default" onClick="window.location.href='prdclsSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
	 <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
	 	</div>
	 	</div>

  </form>
</div>					
</div>
</div>
<script>
var catalogpath='<%=catalogpath%>';
 if(catalogpath=="")
	 $("#item_img").attr("src","../jsp/dist/img/NO_IMG.png");
 else	 
	$("#item_img").attr("src",catalogpath);
	</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>


