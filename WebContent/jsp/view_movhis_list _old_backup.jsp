<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@page import="com.track.constants.TransactionConstants"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%

String title = "Activity Logs Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="submenu" value="<%=IConstants.ACTIVITY_LOGS%>"/>
</jsp:include>
<style type="text/css">
.dt-button-collection.dropdown-menu{
left: 50px; !important;
}
</style>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=850,height=700,left = 200,top = 184');
}

function popUpWin1(URL) {
	  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=900,height=950,left = 400,top = 50');
	}
//---Modified by Deen on May 22 2014, Description:To open Activity Logs in excel powershell format
function ExportReport()
{
	   var flag    = "false";
	   var FROM_DATE    = document.form.FROM_DATE.value;
	   var TO_DATE      = document.form.TO_DATE.value;
	   var DIRTYPE      = document.form.DIRTYPE.value;
	   var USER         = document.form.USER.value;
	   var ITEMNO         = document.form.ITEM.value;
	   var REASONCODE         = document.form.REASONCODE.value;
	   var REMARKS         = document.form.REMARKS.value;

	   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
	   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
	   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
	   
	   if(USER != null    && USER != "") { flag = true;}
	   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
	   if(REASONCODE != null     && REASONCODE != "") { flag = true;}
	   if(REMARKS != null     && REMARKS != "") { flag = true;}

	   if(flag == "false"){ alert("Please define any one search criteria"); return false;}
       document.form.action="/track/ReportServlet?action=ExportExcelMovementHistory";
 	   document.form.submit();
 }
//---End Modified by Deen on May 22 2014, Description:To open Activity Logs in excel powershell format

function onGo(){

   var flag    = "false";
   document.form.xlAction.value="";
   var FROM_DATE    = document.form.FROM_DATE.value;
   var TO_DATE      = document.form.TO_DATE.value;
   var DIRTYPE      = document.form.DIRTYPE.value;
   
   var USER         = document.form.USER.value;
   var ITEMNO       = document.form.ITEM.value;

   var JOBNO         = document.form.JOBNO.value;
   var ORDERNO       = document.form.ORDERNO.value;
   var CUSTOMER         = document.form.CUSTOMER.value;
   var LOC             = document.form.LOC_0.value;
   var REASONCODE      = document.form.REASONCODE.value;
   var LOCTYPEID      = document.form.LOC_TYPE_ID.value;
   var REMARKS      = document.form.REMARKS.value;
   
   

   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   
   if(JOBNO != null     && JOBNO != "") { flag = true;}
   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
   if(CUSTOMER != null     && CUSTOMER != "") { flag = true;}
   if(LOC != null     && LOC != "") { flag = true;}
   if(REASONCODE != null     && REASONCODE != "") { flag = true;}
   if(LOCTYPEID != null     && LOCTYPEID != "") { flag = true;}

   if(flag == "false"){ alert("Please define any one search criteria"); return false;}

// document.form.action="view_movhis_list.jsp";
document.form.action="../reports/activitylogs";
document.form.submit();
}



</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
Hashtable ht = new Hashtable();
List cnslist=new ArrayList();
session = request.getSession();
//String action         = _strUtils.fString(request.getParameter("action")).trim();
String PLANT= session.getAttribute("PLANT").toString();
String USERID= session.getAttribute("LOGIN_USER").toString();
String FROM_DATE ="",  TO_DATE = "",mkey="",mvalue="", cnskey="",cnsval="",DIRTYPE ="",BATCH ="",USER="",ITEM="",sItemDesc="",fdate="",
tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String constkey = "",LOC="",REASONCODE="",PRD_CLS_ID="",PRD_DEPT_ID="",PRD_TYPE_ID="",PRD_BRAND_ID= "";
String html = "",cntRec ="false",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="",TYPE="", REMARKS="" ;

PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
// String curDate =_dateUtils.getDate();
String curDate =du.getDateMinusDays();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));


if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
if (FROM_DATE.length()>5)
if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();

DIRTYPE          = _strUtils.fString(request.getParameter("DIRTYPE"));
JOBNO            = _strUtils.fString(request.getParameter("JOBNO"));
USER             = _strUtils.fString(request.getParameter("USER"));
ITEMNO           = _strUtils.fString(request.getParameter("ITEM"));
sItemDesc        = _strUtils.replaceCharacters2Recv(_strUtils.fString(request.getParameter("DESC")));
ORDERNO          = _strUtils.fString(request.getParameter("ORDERNO"));
CUSTOMER         = _strUtils.fString(request.getParameter("CUSTOMER"));
BATCH            = _strUtils.fString(request.getParameter("BATCH"));
LOC              = _strUtils.fString(request.getParameter("LOC_0"));
REASONCODE              = _strUtils.fString(request.getParameter("REASONCODE"));
constkey 	 = _strUtils.fString(request.getParameter("DIRTYPE"));
LOC_TYPE_ID =  _strUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2 =  _strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
LOC_TYPE_ID3 =  _strUtils.fString(request.getParameter("LOC_TYPE_ID3"));
TYPE =  _strUtils.fString(request.getParameter("TYPE"));
PRD_CLS_ID =  _strUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID =  _strUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID =  _strUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_DEPT_ID =  _strUtils.fString(request.getParameter("PRD_DEPT_ID"));
REMARKS =  _strUtils.fString(request.getParameter("REMARKS"));


if(PGaction.equalsIgnoreCase("View")){
 
 try{
        if(ITEMNO.length() > 0) {
         ItemMstUtil itemMstUtil = new ItemMstUtil();
         itemMstUtil.setmLogger(mLogger);
         try{
         String temItem = itemMstUtil.isValidAlternateItemInItemmst(PLANT, ITEMNO);
         ITEMNO = temItem;
         }catch(Exception e){
                 
         }
         }
  
        if(_strUtils.fString(JOBNO).length() > 0)        ht.put("JOBNUM",JOBNO);
        if(_strUtils.fString(USER).length() > 0)          ht.put("CRBY",USER);
        if(_strUtils.fString(ITEMNO).length() > 0)        ht.put("ITEM",ITEMNO);
        if(_strUtils.fString(CUSTOMER).length() > 0)        ht.put("CUSTNO",CUSTOMER);
        if(_strUtils.fString(ORDERNO).length() > 0)         ht.put("ORDNUM",ORDERNO);
        if(_strUtils.fString(BATCH).length() > 0)           ht.put("BATNO",BATCH);
        
 }catch(Exception e) { 
	 System.out.println(e.getMessage());
	 throw new Exception();
 }
}
%>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              
              <div class="box-title pull-right">
                     <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button>
					&nbsp;
				</div>
				
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
              </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form" method="post" action="view_movhis_list.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">

<div id="target" style="display:none"  style="padding: 18px;"> 


<div class="form-group">

<!-- 1st row -->
					<div class="row" style="padding:3px">
						<label class="control-label col-sm-2" for="search">Search</label>
						<div class="col-sm-1" style="width: 12%;">
				  			<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" 
				  			size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY 
				  			placeholder="FROM DATE">
				  		</div>
				  		<div class="col-sm-1" style="width: 13%;">
				  			<input class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
				  		</div>
				  		<div class="col-sm-3 ac-box">
				  		<input type="text" name="DIRTYPE" id="DIRTYPE" class="ac-selected form-control" value="<%=DIRTYPE%>" placeholder="LOGS GROUP" >
				  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'DIRTYPE\']').focus()">
					  	<i class="glyphicon glyphicon-menu-down"></i> </span>
<!-- 				  		<select class="form-control" data-toggle="dropdown" data-placement="right"  name="DIRTYPE" style="width: 100%" onchange="EmptyMovementtype();"> -->
<!-- <!-- 						          <option selected value="">LOGS GROUP</option> -->
<!-- 						<option value="" disabled="true" selected="true" style="color: #888;"> LOGS GROUP </option> -->
<%-- 						          <OPTION   value='Purchase Order' <%if(DIRTYPE.equalsIgnoreCase("Purchase Order")){ %>selected<%} %>>Purchase </OPTION> --%>
<%-- 						          <OPTION   value='Expenses' <%if(DIRTYPE.equalsIgnoreCase("Expenses")){ %>selected<%} %>>Expenses </OPTION> --%>
<%-- 						          <OPTION   value='Bill' <%if(DIRTYPE.equalsIgnoreCase("Bill")){ %>selected<%} %>>Bill </OPTION> --%>
<%-- 						          <OPTION   value='Payment' <%if(DIRTYPE.equalsIgnoreCase("Payment")){ %>selected<%} %>>Payment </OPTION> --%>
<%-- 						          <OPTION   value='Purchase Credit Note' <%if(DIRTYPE.equalsIgnoreCase("Purchase Credit Note")){ %>selected<%} %>>Purchase Credit Notes </OPTION> --%>
<%-- 						     	  <OPTION   value='Salese Estimate Order' <%if(DIRTYPE.equalsIgnoreCase("Sales Estimate Order")){ %>selected<%} %>>Sales Estimate </OPTION> --%>
<%-- 						     	  <OPTION   value='Sales Order' <%if(DIRTYPE.equalsIgnoreCase("Sales Order")){ %>selected<%} %>>Sales </OPTION> --%>
<%-- 						     	  <OPTION   value='Invoice' <%if(DIRTYPE.equalsIgnoreCase("Invoice")){ %>selected<%} %>>Invoice </OPTION> --%>
<%-- 						     	  <OPTION   value='Payment Received' <%if(DIRTYPE.equalsIgnoreCase("Payment Received")){ %>selected<%} %>>Payment Received </OPTION> --%>
<%-- 						     	  <OPTION   value='Credit Note' <%if(DIRTYPE.equalsIgnoreCase("Credit Note")){ %>selected<%} %>>Credit Notes </OPTION> --%>
<%-- 						     	  <OPTION   value='Rental And Service Order' <%if(DIRTYPE.equalsIgnoreCase("Rental And Service Order")){ %>selected<%} %>>Rental And Consignment</OPTION> --%>
<%-- 						     	  <OPTION   value='Consignment Order' <%if(DIRTYPE.equalsIgnoreCase("Consignment Order")){ %>selected<%} %>>Consignment Order </OPTION> --%>
<%-- 						     	  <OPTION   value='Goods_Receipt_Issue' <%if(DIRTYPE.equalsIgnoreCase("Goods_Receipt_Issue")){ %>selected<%} %>>Goods_Receipt_Issue </OPTION> --%>
<%-- 						     	  <OPTION   value='Stock Move' <%if(DIRTYPE.equalsIgnoreCase("Stock Move")){ %>selected<%} %>>Stock Move</OPTION> --%>
<%-- 						      	  <OPTION   value='Stock Take' <%if(DIRTYPE.equalsIgnoreCase("Stock Take")){ %>selected<%} %>>Stock Take </OPTION> --%>
<%-- 						     	  <OPTION   value='Kitting And DeKitting' <%if(DIRTYPE.equalsIgnoreCase("Kitting And DeKitting")){ %>selected<%} %>>Kitting And DeKitting </OPTION> --%>
<%-- 						     	  <OPTION   value='TAX_FILE' <%if(DIRTYPE.equalsIgnoreCase("TAX_FILE")){ %>selected<%} %>>Tax Return</OPTION> --%>
<%-- 						     	  <OPTION   value='Holiday' <%if(DIRTYPE.equalsIgnoreCase("Holiday")){ %>selected<%} %>>Holiday</OPTION> --%>
<%-- 						     	  <OPTION   value='Employee Type' <%if(DIRTYPE.equalsIgnoreCase("Employee Type")){ %>selected<%} %>>Employee Type</OPTION> --%>
<%-- 						     	  <OPTION   value='Leave Type' <%if(DIRTYPE.equalsIgnoreCase("Leave Type")){ %>selected<%} %>>Leave Type</OPTION> --%>
<%-- 						     	  <OPTION   value='Salary Type' <%if(DIRTYPE.equalsIgnoreCase("Salary Type")){ %>selected<%} %>>Salary Type</OPTION> --%>
<%-- 						     	  <OPTION   value='Apply Leave' <%if(DIRTYPE.equalsIgnoreCase("Apply Leave")){ %>selected<%} %>>Apply Leave</OPTION> --%>
<%-- 						     	  <OPTION   value='Payroll' <%if(DIRTYPE.equalsIgnoreCase("Payroll")){ %>selected<%} %>>Payroll</OPTION> --%>
<!-- 						 		</select>  -->
						</div>
				  		<div class="col-sm-3 ac-box">
						  		<div class="input-group">
        						<INPUT class="ac-selected  form-control typeahead" name="TYPE" placeholder = "LOGS TYPE" type = "TEXT" value="<%=TYPE%>" style="width: 100%" >
        						<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin1('../jsp/MovementType_List.jsp?formName=form&MOVEMENTGROUP='+form.DIRTYPE.value);">
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>
<!--    		 						<a href="#" data-toggle="tooltip" data-placement="top" title="Movement Details"> -->
<!--    		 						<i  class="glyphicon glyphicon-search" aria-hidden="true"></i></a></span> -->
   		 						</div>
						</div>
					</div>
					
					
<!-- 2nd row -->
					<div class="row" style="padding:3px">
<!-- 						<div class="col-sm-1"></div> -->
							<label class="control-label col-sm-2" for="search"></label>
							<div class="col-sm-3">
						        <INPUT class="form-control" name="JOBNO" placeholder="REFERENCE" type = "TEXT" value="<%=JOBNO%>" style="width: 100%"  MAXLENGTH=20>
    						</div> 
    					<div class="col-sm-3 ac-box">
    						<INPUT class="form-control" name="ORDERNO" placeholder="ORDER NO" type = "TEXT" value="<%=ORDERNO%>" size="30"  MAXLENGTH=40>	
						</div>
						<div class="col-sm-3 ac-box">
    							<INPUT class="form-control" placeholder = "PRODUCT" name="ITEM" id="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEMNO)%>" size="30"  MAXLENGTH=50>
    							<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM\']').focus()">
  		  						<i class="glyphicon glyphicon-menu-down"></i></span>
<!--    		 						<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/item_list.jsp?ITEM='+form.ITEM.value);"> -->
<!--    		 						<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details"> -->
<!--    		 						<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
						</div>
					</div>
					
<!-- 3rd row -->
					<div class="row" style="padding:3px">
<!-- 						<div class="col-sm-1"></div> -->
							<label class="control-label col-sm-2" for="search"></label>
						<div class="col-sm-3">
        						<INPUT class="form-control" placeholder = "DESCRIPTION" name="DESC" id="DESC" type = "TEXT" value="<%=StrUtils.forHTMLTag(sItemDesc)%>" size="30"  MAXLENGTH=100> 
        						<span class="select-icon"  onclick="$(this).parent().find('input[name=\'DESC\']').focus()">
  		  						<i class="glyphicon glyphicon-menu-down"></i></span>
<!--     							<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/item_list.jsp?ITEM_DESC='+form.DESC.value);"> -->
<!--    		 						<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details"> -->
<!--    		 						<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
    					</div> 
    					<div class="col-sm-3 ac-box">
        					<INPUT class="form-control" placeholder = "BATCH" name="BATCH" type = "TEXT" value="<%=StrUtils.forHTMLTag(BATCH)%>" style="width: 100%"  MAXLENGTH=40>
						</div>
						<div class="col-sm-3 ac-box">
    							<INPUT class="form-control" placeholder = "PRODUCT DEPARTMENT" name="PRD_DEPT_ID" id="PRD_DEPT_ID"  type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>" size="30"  MAXLENGTH=20>
    							<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()">
  		  						<i class="glyphicon glyphicon-menu-down"></i></span>
<!--    		 						<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/PrdClsIdList.jsp?PRD_CLS_ID='+form.PRD_CLS_ID.value);"> -->
<!--    		 						<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT CATEGORY Details"> -->
<!--    		 						<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
						</div>
					</div>
					
<!-- 4th row -->
					<div class="row" style="padding:3px">
<!-- 					<div class="col-sm-1"></div> -->
					<label class="control-label col-sm-2" for="search"></label>
					<div class="col-sm-3 ac-box">
    							<INPUT class="form-control" placeholder = "PRODUCT CATEGORY" name="PRD_CLS_ID" id="PRD_CLS_ID"  type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" size="30"  MAXLENGTH=20>
    							<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()">
  		  						<i class="glyphicon glyphicon-menu-down"></i></span>
<!--    		 						<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/PrdClsIdList.jsp?PRD_CLS_ID='+form.PRD_CLS_ID.value);"> -->
<!--    		 						<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT CATEGORY Details"> -->
<!--    		 						<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
						</div>
    				<div class="col-sm-3">
				    		<INPUT class="form-control" placeholder = "PRODUCT SUB CATEGORY" name="PRD_TYPE_ID" id="PRD_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" size="30"  MAXLENGTH=20>
				    		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()">
  		  					<i class="glyphicon glyphicon-menu-down"></i></span>
<!--    		 					<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/productTypeIdList.jsp?ITEM_ID='+form.PRD_TYPE_ID.value);"> -->
<!--    		 					<a href="#" data-toggle="tooltip" data-placement="top" title="PRODUCT SUB CATEGORY Details"> -->
<!--    		 					<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
    				</div> 
					<div class="col-sm-3 ac-box">
    						<INPUT class="form-control" placeholder = "PRODUCT BRAND" name="PRD_BRAND_ID" id="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" size="30"  MAXLENGTH=20>
    						<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()">
  		  					<i class="glyphicon glyphicon-menu-down"></i></span>
<!--    		 					<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form');"> -->
<!--    		 					<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand Details"> -->
<!--    		 					<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
					</div>
					</div>
					
<!-- 5th row -->
					<div class="row" style="padding:3px">
<!-- 					<div class="col-sm-1"></div> -->
					<label class="control-label col-sm-2" for="search"></label>
					<div class="col-sm-3 ac-box">
							<INPUT class="form-control" placeholder = "SUPPLIER/CUSTOMER ID" name="CUSTOMER" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" size="30"  MAXLENGTH=20>
					</div>
    				<div class="col-sm-3">
        				<INPUT class="form-control" placeholder = "USER" name="USER" type = "TEXT" value="<%=StrUtils.forHTMLTag(USER)%>" style="width: 100%"  MAXLENGTH=100>
    				</div> 
					<div class="col-sm-3 ac-box">
        				<INPUT class="form-control" placeholder = "REASON CODE" name="REASONCODE" id="REASONCODE" type = "TEXT" value="<%=StrUtils.forHTMLTag(REASONCODE)%>" size="16"  MAXLENGTH=40>
        				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'REASONCODE\']').focus()">
  		  				<i class="glyphicon glyphicon-menu-down"></i></span>
<!--     					<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/miscreasoncode.jsp?ITEMNO='+form.ITEM.value);"> -->
<!--    		 				<a href="#" data-toggle="tooltip" data-placement="top" title="Reason Code Details"> -->
<!--    		 				<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
					</div>
					</div>
					
<!-- 6th row -->
					<div class="row" style="padding:3px">
<!-- 					<div class="col-sm-1"></div> -->
					<label class="control-label col-sm-2" for="search"></label>
					<div class="col-sm-3 ac-box">
    					<INPUT class="form-control" placeholder = "LOCATION"  name="LOC_0" id="LOC_0"  type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC)%>"  size="30"  MAXLENGTH=20>
    					<span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC_0\']').focus()">
  		  				<i class="glyphicon glyphicon-menu-down"></i></span>
<!--    		 				<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/loc_list_MultiReceivewms.jsp?LOC='+form.LOC_0.value+'&INDEX=0');"> -->
<!--    		 				<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details"> -->
<!--    		 				<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
					</div>
    				<div class="col-sm-3">
    					<INPUT class="form-control" placeholder = "LOCATION TYPE ONE" name="LOC_TYPE_ID" id="LOC_TYPE_ID" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>"	size="30" MAXLENGTH=20>
    					<span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()">
  		  				<i class="glyphicon glyphicon-menu-down"></i></span>
<!--    		 				<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form.LOC_TYPE_ID.value+'&TYPE=movhis');"> -->
<!--    		 				<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details"> -->
<!--    		 				<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
    				</div> 
					<div class="col-sm-3 ac-box">
    					<INPUT class="form-control" placeholder = "LOCATION TYPE TWO" name="LOC_TYPE_ID2" id="LOC_TYPE_ID2" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID2)%>"	size="30" MAXLENGTH=20>
    					<span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()">
  		  				<i class="glyphicon glyphicon-menu-down"></i></span>
<!--    		 				<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID='+form.LOC_TYPE_ID2.value+'&TYPE=movhis');"> -->
<!--    		 				<a href="#" data-toggle="tooltip" data-placement="top" title="Location Type Details"> -->
<!--    		 				<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
					</div>
				   </div>
					
<!-- 7th row -->
					<div class="row" style="padding:3px">
<!-- 					<div class="col-sm-1"></div> -->
					<label class="control-label col-sm-2" for="search"></label>
					<div class="col-sm-3 ac-box">
    					<INPUT class="form-control" placeholder = "LOCATION TYPE THREE" name="LOC_TYPE_ID3" ID="LOC_TYPE_ID3" type="TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID3)%>"  size="30" MAXLENGTH=20>
  		  				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()">
  		  				<i class="glyphicon glyphicon-menu-down"></i></span>
				   </div>
				   
				   <div class="col-sm-3">
        				<INPUT class="form-control" placeholder = "REMARKS" name="REMARKS" type="TEXT" value="<%=REMARKS%>"	style="width: 100%" MAXLENGTH=20> 
    				</div> 
    				
    				<div class="col-sm-3 ac-box">
  						<button type="button" class="Submit btn btn-success" onClick="javascript:return onGo();"><b>Search</b></button>&nbsp;
<!--   						<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; -->
<!--   						<button type="button" class="Submit btn btn-danger" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
					</div>
					</div>
			</div>
  	<INPUT type="Hidden" name="USERID" value="<%=USERID%>">
   	
   		
   		 
  	    </div> 
  	    
  	    <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
<!--       <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>Search</b></button>&nbsp; -->
<!--   	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; -->
<!--   	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RTR');}"> <b>Back</b></button> -->
  	</div>
        </div>
       	  </div>

  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableMovementHistory_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableMovementHistory" class="table table-bordred table-striped"
									role="grid" aria-describedby="tableMovementHistory_info">
<!-- 									class="table table-bordered table-hover dataTable no-footer" -->
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">DATE</th>
		                	<th style="font-size: smaller;">TIME</th>
		                	<th style="font-size: smaller;">LOGS TYPE</th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">LOC</th>
		                	<th style="font-size: smaller;">PRODUCT ID</th>
		                	<th style="font-size: smaller;">DESCRIPTION</th>
		                	<th style="font-size: smaller;">BATCH</th>
		                	<th style="font-size: smaller;">QTY</th>
		                	<th style="font-size: smaller;">USER</th>
		                	<th style="font-size: smaller;">REMARKS</th>
		                </tr>
		            </thead>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <script type="text/javascript">
  var tableData = [];
  <%
  movQryList = movHisUtil.getMovHisListWithRemarks(ht,PLANT,FROM_DATE,TO_DATE,USERID,sItemDesc,REASONCODE,LOC_TYPE_ID,LOC_TYPE_ID2,LOC_TYPE_ID3,LOC,DIRTYPE,TYPE,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,REMARKS);
  if(movQryList.size()<=0)
          cntRec ="true";
  if(movQryList.size()<=0 && cntRec=="true" ){ %>
  
  <%}else{
  com.track.dao.InvMstDAO _InvMstDAO = new com.track.dao.InvMstDAO();
  com.track.dao.CustMstDAO _CustMstDAO = new com.track.dao.CustMstDAO();
  
  _InvMstDAO.setmLogger(mLogger);
  _CustMstDAO.setmLogger(mLogger);


  for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                     Map lineArr = (Map) movQryList.get(iCnt);

    String trDate= "",TRANDATE="";
    trDate=(String)lineArr.get("CRAT");
    TRANDATE=(String)lineArr.get("TRANDATE");
 
            if (trDate.length()>8) {
                    //System.out.println(trDate);
            trDate    = trDate.substring(8,10)+":"+ trDate.substring(10,12)+":"+trDate.substring(12,14);
            }

            if(TRANDATE.contains("-"))
            {
            	TRANDATE = TRANDATE.substring(8,10)+"/"+ TRANDATE.substring(5,7)+"/"+TRANDATE.substring(0,4);
            }
  
    int iIndex = iCnt + 1;
    String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
    %>
    var rowData = [];
    rowData[rowData.length] = '<%=iIndex%>';
    rowData[rowData.length] = '<%=TRANDATE%>';
    rowData[rowData.length] = '<%=trDate%>';
    rowData[rowData.length] = '<%=(String)lineArr.get("DIRTYPE")%>';
    rowData[rowData.length] = '<%=(String)lineArr.get("ORDNUM")%>';
    rowData[rowData.length] = '<%=(String)lineArr.get("LOC")%>';
    rowData[rowData.length] = '<%=(String)lineArr.get("ITEM")%>';
    rowData[rowData.length] = '<%=(String)lineArr.get("ITEMDESC") %>';
    rowData[rowData.length] = '<%=(String)lineArr.get("BATNO") %>';
    rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("QTY")) %>';
    rowData[rowData.length] = '<%=(String)lineArr.get("CRBY")%>';
    rowData[rowData.length] = '<%=(String)lineArr.get("REMARKS")%>';
    tableData[tableData.length] = rowData;
  <%}%>
  <%}%>
  var groupColumn = 1;
 $(document).ready(function(){
	 $('#tableMovementHistory').DataTable({
		 	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
		  	data: tableData,
		  	"columnDefs": [{"className": "t-right", "targets": [9]}],
			/* "orderFixed": [ groupColumn, 'asc' ], */ 
		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
			"<'row'<'col-md-6'><'col-md-6'>>" +
			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
	        buttons: [
	        	{
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend : 'excel',
	                    	exportOptions: {
	    	                	columns: [':visible']
	    	                }
	                    },
	                    {
	                    	extend : 'pdf',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
                    		orientation: 'landscape',
                            pageSize: 'A3'
	                    }
	                ]
	            },
	            {
	            	extend: 'colvis',
                    columns: ':not(:eq('+groupColumn+')):not(:eq(3)):not(:eq(4)):not(:eq(6))'
                }
	        ],
		  });	 
 });
 
 
 
  </script>
 
  </FORM>
  </div></div></div>
  
                  <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
	 var plant= '<%=PLANT%>'; 
    $('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('.Hide').click(function() {
	    $('#target').hide(500);
	    $('.ShowSingle').show(0);
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
    if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('.Show').click();
    }else{
    	$('.Hide').click();
    }



    var postatus =   [{
        "year": "Purchase Estimate Order",
        "value": "Purchase Estimate Order",
        "tokens": [
          "Purchase Estimate Order"
        ]
      },{
        "year": "Purchase Order",
        "value": "Purchase Order",
        "tokens": [
          "Purchase Order"
        ]
      },
      {
    	    "year": "Expenses",
    	    "value": "Expenses",
    	    "tokens": [
    	      "Expenses"
    	    ]
    	  },
      {
    	    "year": "Bill",
    	    "value": "Bill",
    	    "tokens": [
    	      "Bill"
    	    ]
    	  },
      {
    	    "year": "Payment",
    	    "value": "Payment",
    	    "tokens": [
    	      "Payment"
    	    ]
    	  },
      {
    	    "year": "Debit Note",
    	    "value": "Debit Note",
    	    "tokens": [
    	      "Debit Note"
    	    ]
    	  },
      {
    	    "year": "Sales Estimate Order",
    	    "value": "Sales Estimate Order",
    	    "tokens": [
    	      "Sales Estimate Order"
    	    ]
    	  },
      {
    	    "year": "Sales Order",
    	    "value": "Sales Order",
    	    "tokens": [
    	      "Sales Order"
    	    ]
    	  },
      {
    	    "year": "Invoice",
    	    "value": "Invoice",
    	    "tokens": [
    	      "Invoice"
    	    ]
    	  },
      {
    	    "year": "Payment Received",
    	    "value": "Payment Received",
    	    "tokens": [
    	      "Payment Received"
    	    ]
    	  },
      {
    	    "year": "Credit Note",
    	    "value": "Credit Note",
    	    "tokens": [
    	      "Credit Note"
    	    ]
    	  },
      {
    	    "year": "Rental And Service Order",
    	    "value": "Rental And Service Order",
    	    "tokens": [
    	      "Rental And Service Order"
    	    ]
    	  },
      {
    	    "year": "Consignment Order",
    	    "value": "Consignment Order",
    	    "tokens": [
    	      "Consignment Order"
    	    ]
    	  },
    	  {
    		    "year": "Goods_Receipt_Issue",
    		    "value": "Goods_Receipt_Issue",
    		    "tokens": [
    		      "Goods_Receipt_Issue"
    		    ]
    		  },
    		  {
    	    	    "year": "Stock Move",
    	    	    "value": "Stock Move",
    	    	    "tokens": [
    	    	      "Stock Move"
    	    	    ]
    	    	  },
    		  {
    	    	    "year": "Stock Take",
    	    	    "value": "Stock Take",
    	    	    "tokens": [
    	    	      "Stock Take"
    	    	    ]
    	    	  },
    		  {
    	    	    "year": "Kitting And DeKitting",
    	    	    "value": "Kitting And DeKitting",
    	    	    "tokens": [
    	    	      "Kitting And DeKitting"
    	    	    ]
    	    	  },
    	    	  {
      	    	    "year": "De-Kitting",
      	    	    "value": "De-Kitting",
      	    	    "tokens": [
      	    	      "De-Kitting"
      	    	    ]
      	    	  },
      			  
      			  {
   				    "year": "Kitting",
   				    "value": "Kitting",
   				    "tokens": [
   				      "Kitting"
   				    ]
   				  },
    		  {
    	    	    "year": "TAX_FILE",
    	    	    "value": "TAX_FILE",
    	    	    "tokens": [
    	    	      "TAX_FILE"
    	    	    ]
    	    	  },
    		  {
    	    	    "year": "Holiday",
    	    	    "value": "Holiday",
    	    	    "tokens": [
    	    	      "Holiday"
    	    	    ]
    	    	  },
    		  {
    	    	    "year": "Employee Type",
    	    	    "value": "Employee Type",
    	    	    "tokens": [
    	    	      "Employee Type"
    	    	    ]
    	    	  },
    		  {
    	    	    "year": "Leave Type",
    	    	    "value": "Leave Type",
    	    	    "tokens": [
    	    	      "Leave Type"
    	    	    ]
    	    	  },
    		  {
    	    	    "year": "Salary Type",
    	    	    "value": "Salary Type",
    	    	    "tokens": [
    	    	      "Salary Type"
    	    	    ]
    	    	  },
    		  
    		  {
    			    "year": "Apply Leave",
    			    "value": "Apply Leave",
    			    "tokens": [
    			      "Apply Leave"
    			    ]
    			  },  
    		  {
    			    "year": "Payroll",
    			    "value": "Payroll",
    			    "tokens": [
    			      "Payroll"
    			    ]
    			  },
        		  {
        			    "year": "Journal",
        			    "value": "Journal",
        			    "tokens": [
        			      "Journal"
        			    ]
        			  }];

    var substringMatcher = function(strs) {
    	  return function findMatches(q, cb) {
    	    var matches, substringRegex;
    	    // an array that will be populated with substring matches
    	    matches = [];
    	    // regex used to determine if a string contains the substring `q`
    	    substrRegex = new RegExp(q, 'i');
    	    // iterate through the pool of strings and for any string that
    	    // contains the substring `q`, add it to the `matches` array
    	    $.each(strs, function(i, str) {
    	      if (substrRegex.test(str.value)) {
    	        matches.push(str);
    	      }
    	    });
    	    cb(matches);
    	  };
    };

    
    /* To get the suggestion data for Status */
	$("#DIRTYPE").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'postatus',
	  display: 'value',  
	  source: substringMatcher(postatus),
	  limit: 9999,
	  templates: {
	  empty: [
		  '<div style="padding:3px 20px">',
			'No results found',
		  '</div>',
		].join('\n'),
		suggestion: function(data) {
		return '<p>' + data.value + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	});
	


    /* Product Number Auto Suggestion */
	$('#ITEM').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p onclick="document.form.ITEM_DESC.value = \''+data.ITEMDESC+'\'">'+data.ITEM+'</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});


	/* Product Dept Auto Suggestion */
	$('#PRD_DEPT_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'prd_dep_id',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTDEPARTMENTID_FOR_SUMMARY",
				PRODUCTDEPARTMENTID : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTOMERTYPELIST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
// 	return '<p class="item-suggestion">'+ data.prd_dep_id +'</p>';
				return '<div onclick="document.form.PRD_DEPT_ID.value = \''+data.prd_dep_id+'\'"><p class="item-suggestion">' + data.prd_dep_id + '</p><br/><p class="item-suggestion">DESC: '+data.prd_dep_desc+'</p></div>';
	}
	}

	}).on('typeahead:open',function(event,selection){
	var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
	element.toggleClass("glyphicon-menu-up",true);
	element.toggleClass("glyphicon-menu-down",false);
	}).on('typeahead:close',function(){
	var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
	element.toggleClass("glyphicon-menu-up",false);
	element.toggleClass("glyphicon-menu-down",true);
	});
	
	/* Product Description  Auto Suggestion */
	$('#DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEMDESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p onclick="document.form.ITEM.value = \''+data.ITEM+'\'">'+data.ITEMDESC+'</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
    
	    /* PRODUCT CATEGORY Auto Suggestion */
	    $('#PRD_CLS_ID').typeahead({
	    	  hint: true,
	    	  minLength:0,  
	    	  searchOnFocus: true
	    	},
	    	{
	    	  display: 'prd_cls_id',  
	    	  source: function (query, process,asyncProcess) {
	    		var urlStr = "/track/MasterServlet";
	    		$.ajax( {
	    		type : "POST",
	    		url : urlStr,
	    		async : true,
	    		data : {
	    			PLANT : plant,
	    			ACTION : "GET_PRODUCTCLASSID_FOR_SUMMARY",
	    			PRODUCTCLASSID : query
	    		},
	    		dataType : "json",
	    		success : function(data) {
	    			return asyncProcess(data.CUSTOMERTYPELIST);
	    		}
	    			});
	    	},
	    	  limit: 9999,
	    	  templates: {
	    	  empty: [
	    		  '<div style="padding:3px 20px">',
	    			'No results found',
	    		  '</div>',
	    		].join('\n'),
	    		suggestion: function(data) {
// 	    return '<p class="item-suggestion">'+ data.prd_cls_id +'</p>';
	    			return '<div onclick="document.form.PRD_CLS_ID.value = \''+data.prd_cls_id+'\'"><p class="item-suggestion">' + data.prd_cls_id + '</p><br/><p class="item-suggestion">DESC: '+data.prd_cls_desc+'</p></div>';
	    }
	    }
	
	    }).on('typeahead:open',function(event,selection){
	    var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
	    element.toggleClass("glyphicon-menu-up",true);
	    element.toggleClass("glyphicon-menu-down",false);
	    }).on('typeahead:close',function(){
	    var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
	    element.toggleClass("glyphicon-menu-up",false);
	    element.toggleClass("glyphicon-menu-down",true);
	    });
    
    /* PRODUCT SUB CATEGORY Auto Suggestion */
		    $('#PRD_TYPE_ID').typeahead({
		  	  hint: true,
		  	  minLength:0,  
		  	  searchOnFocus: true
		  	},
		  	{
		  	  display: 'prd_type_id',  
		  	  source: function (query, process,asyncProcess) {
		  		var urlStr = "/track/MasterServlet";
		  		$.ajax( {
		  		type : "POST",
		  		url : urlStr,
		  		async : true,
		  		data : {
		  			PLANT : plant,
		  			ACTION : "GET_PRD_FOR_SUMMARY",
		  			PRODUCTTYPEID : query
		  		},
		  		dataType : "json",
		  		success : function(data) {
		  			return asyncProcess(data.SUPPLIERTYPELIST);
		  		}
		  			});
		  	},
		  	  limit: 9999,
		  	  templates: {
		  	  empty: [
		  		  '<div style="padding:3px 20px">',
		  			'No results found',
		  		  '</div>',
		  		].join('\n'),
		  		suggestion: function(data) {
// 		  return '<p class="item-suggestion">'+ data.prd_type_id +'</p>';
		  			return '<div onclick="document.form.PRD_TYPE_ID.value = \''+data.prd_type_id+'\'"><p class="item-suggestion">' + data.prd_type_desc + '</p><br/><p class="item-suggestion">DESC: '+data.prd_type_desc+'</p></div>';
		  }
		  }
		
		  }).on('typeahead:open',function(event,selection){
		  var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		  element.toggleClass("glyphicon-menu-up",true);
		  element.toggleClass("glyphicon-menu-down",false);
		  }).on('typeahead:close',function(){
		  var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		  element.toggleClass("glyphicon-menu-up",false);
		  element.toggleClass("glyphicon-menu-down",true);
		  });

    /* product brand Auto Suggestion */
		    $('#PRD_BRAND_ID').typeahead({
		  	  hint: true,
		  	  minLength:0,  
		  	  searchOnFocus: true
		  	},
		  	{
		  	  display: 'PRD_BRAND_ID',  
		  	  source: function (query, process,asyncProcess) {
		  		var urlStr = "/track/MasterServlet";
		  		$.ajax( {
		  		type : "POST",
		  		url : urlStr,
		  		async : true,
		  		data : {
		  			PLANT : plant,
		  			ACTION : "GET_PRODUCTBRANDID_FOR_SUMMARY",
		  			PRODUCTBRANDID : query
		  		},
		  		dataType : "json",
		  		success : function(data) {
		  			return asyncProcess(data.PRODUCTBRAND);
		  		}
		  			});
		  	},
		  	  limit: 9999,
		  	  templates: {
		  	  empty: [
		  		  '<div style="padding:3px 20px">',
		  			'No results found',
		  		  '</div>',
		  		].join('\n'),
		  		suggestion: function(data) {
// 		  return '<p class="item-suggestion">'+ data.PRD_BRAND_ID +'</p>';
		  			return '<div onclick="document.form.PRD_BRAND_ID.value = \''+data.PRD_BRAND_ID+'\'"><p class="item-suggestion">' + data.PRD_BRAND_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
		  }
		  }
		
		  }).on('typeahead:open',function(event,selection){
		  var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		  element.toggleClass("glyphicon-menu-up",true);
		  element.toggleClass("glyphicon-menu-down",false);
		  }).on('typeahead:close',function(){
		  var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		  element.toggleClass("glyphicon-menu-up",false);
		  element.toggleClass("glyphicon-menu-down",true);
		  });
    
    /* reacon code Auto Suggestion */
		$('#REASONCODE').typeahead({
			hint: true,
			minLength:0,
			searchOnFocus: true
			},
			{
			display: 'rsncode',
			async: true,
			source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
				PLANT : plant,
				ACTION : "GET_RSN_SUMMARY",
				REASONCODE : query
				},
			dataType : "json",
			success : function(data) {
			return asyncProcess(data.CUSTOMERTYPELIST);
			}
		});
		},
		limit: 9999,
		templates: {
		empty: [
		'<div style="padding:3px 20px">',
		'No results found',
		'</div>',
		].join('\n'),
		suggestion: function(data) {
		return '<p class="item-suggestion">'+ data.rsncode +'</p>';
		}
		}	
		}).on('typeahead:open',function(event,selection){
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
		});
		
    /* location Auto Suggestion */
	$('#LOC_0').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOC_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
    
    /* location 1 Auto Suggestion */
	$('#LOC_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
    
    /* location 2 Auto Suggestion */
	$('#LOC_TYPE_ID2').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID2',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETWO_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID2+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
    
	/* location 3 Auto Suggestion */
	$('#LOC_TYPE_ID3').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID3',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETHREE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID3+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
    
 });
 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<Script>
function EmptyMovementtype()
{
	document.form.TYPE.value="";
}


function getProductDetails() {
	var productId = document.form.ITEM.value;
	
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
                        async:false ,
			data : {
				ITEM : productId,
				PLANT : "<%=PLANT%>",
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					
                                if (data.status == "100") {
                                var resultVal = data.result;
                                document.form.DESC.value = resultVal.sItemDesc;
                                 }
				}
			});
		
	}
</Script> 
