<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Generate Manual Barcode";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}


function onRePrint(type){
	
	 var ITEM_DESC = document.form1.ITEM_DESC.value;
	   var barcode   = document.form1.BARCODE.value;
	   var QUANTITY = document.form1.QUANTITY.value;
	   
	    if(ITEM_DESC == "" || ITEM_DESC == null) {alert("Please enter description");document.form1.ITEM_DESC.focus(); return false; }
	    if(barcode == "" || barcode == null) {alert("Please enter barcode text");document.form1.BARCODE.focus(); return false; }
	    if(QUANTITY == "" || QUANTITY == null) {alert("Please enter valid print quantity");document.form1.QUANTITY.focus(); return false; }
	    if(!isNumericInput(QUANTITY)){
				alert("Please enter valid print quantity!");
				document.getElementById("QUANTITY").focus();
				document.getElementById("QUANTITY").select();
		         return false;
			}
	    document.form1.action="/track/inhouse/printbarcode?PAGE_TYPE=MANUAL&PRINT_TYPE="+type;
	    //document.form1.action="/track/DynamicFileServlet?action=printManualBarcode&Submit="+type;
 	    document.form1.submit();
}


function isNumericInput(strString) {
		var strValidChars = "0123456789.-";
		var strChar;
		var blnResult = true;
		if (strString.length == 0)
			return false;
		//  test strString consists of valid characters listed above
		for (var i = 0; i < strString.length && blnResult == true; i++) {
			strChar = strString.charAt(i);
			if (strValidChars.indexOf(strChar) == -1) {
				blnResult = false;
			}
		}
		return blnResult;
	}



</script>
<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";
sAddEnb    = "enabled";
TblControlDAO _TblControlDAO =new TblControlDAO();
String action     =   "";
String sbar  ="",
       sItemDesc  = "",sQty="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
PrdTypeUtil prdtypeutil = new PrdTypeUtil();
DateUtils dateutils = new DateUtils();

prdtypeutil.setmLogger(mLogger);

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sbar  = strUtils.fString(request.getParameter("BARCODE"));
sItemDesc  = strUtils.fString(request.getParameter("ITEM_DESC"));
sQty  = strUtils.fString(request.getParameter("QUANTITY"));
sQty="1";
String fieldDesc = (String)request.getAttribute("Msg");
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>
                <li><a href="../inhouse/genbarcode"><span class="underline-on-hover">Generate Barcode</span></a></li>	
                <li><label>Generate Manual Barcode</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				 onclick="window.location.href='../inhouse/genbarcode'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
 
   <CENTER><strong><%=res%></strong></CENTER>


   <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Description">Description</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="ITEM_DESC" type="TEXT" value="<%=sItemDesc%>"
			size="50" MAXLENGTH=250 class="form-control">
   		 	
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Barcode Text">Barcode Text</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="BARCODE" type="TEXT" value="<%=sbar%>"
			size="50" MAXLENGTH=20>
      </div>
    </div> 
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Quantity">Print Quantity</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="QUANTITY" id="QUANTITY" type="TEXT" value="<%=sQty%>"
			size="50" MAXLENGTH=15>
      </div>
    </div> 
    
      </form>
      </div>
    </div>
</div>


 <div class="form-group">
<div class="col-sm-12" align="center">
<button type="button" class="Submit btn btn-default" value="Print" id="GB50X25" name="action" onclick="javascript:return onRePrint('50X25');"><b>Generate Barcode 50X25 mm</b></button>
<button type="button" class="Submit btn btn-default" value="Print" name="action" onclick="javascript:return onRePrint('100X50');"><b>Generate Barcode 100X50 mm</b></button>

</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>