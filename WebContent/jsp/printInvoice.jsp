<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormat"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
String title = "Sales Order with price";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="submenu" value="<%=IConstants.SALES_REPORTS%>"/>
</jsp:include>

<script src="js/general.js"></script>
<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function checkAll(isChk)
{
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form1.chkdDoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkdDoNo.checked = isChk;
               	}
              	else{
              		document.form1.chkdDoNo[i].checked = isChk;
              	}
            	
        }
    }
}

function onRePrint(){
	var checkFound = false;
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdDoNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdDoNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdDoNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
    document.form1.action="/track/DynamicFileServlet?action=printInvoiceWITHBATCH&Submit=Print Outbound Order With Price";
    document.form1.submit();
}
function onRePrintWithContainer(){
	var checkFound = false;
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdDoNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdDoNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdDoNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
    document.form1.action="/track/DynamicFileServlet?action=printInvoiceWITHBATCHANDCONTAINER&Submit=Print Outbound Order";
    document.form1.submit();
}
function onRePrintWithOutBatch(){
	var checkFound = false;
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdDoNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdDoNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdDoNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
    document.form1.action="/track/DynamicFileServlet?action=printInvoiceWITHOUTBATCH&Submit=Print Outbound Order With Price";
    document.form1.submit();
}
var postatus =   [{
	    "year": "PARTIALLY ISSUED",
	    "value": "PARTIALLY ISSUED",
	    "tokens": [
	      "PARTIALLY ISSUED"
	    ]
	  },
	  {
		    "year": "SHIPPED",
		    "value": "SHIPPED",
		    "tokens": [
		      "SHIPPED"
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
</script>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<!-- <script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script> -->
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>

<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
ArrayList movCustomerList  = new ArrayList();

int k=0;
String rowColor="";		


session= request.getSession();

String USERID ="",plant="";
String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ISSUESTATUS="",ORDERTYPE="",DIRTYPE ="",BATCH ="",USER="",fdate="",tdate="",JOBNO="",DONO="",CUSTOMER="",PGaction="";
String CUSTOMERID="",statusID="",EMP_NAME="",CUSTOMERTYPE="",INVOICENO="",issuests="";
PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false",allChecked = "";

plant = (String)session.getAttribute("PLANT");
USERID= session.getAttribute("LOGIN_USER").toString();
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();//azees
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = StrUtils.fString(request.getParameter("ORDERTYPE"));

boolean displaySummaryPrintContainer=false,displaySummaryLink=false,displaySummaryPrintBatch=false,displaySummaryPrintNoBatch=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryLink = ub.isCheckValAcc("summarylnkdoprice", plant,LOGIN_USER);
	displaySummaryPrintBatch = ub.isCheckValAcc("printdoinvoicebatch", plant,LOGIN_USER);
	displaySummaryPrintNoBatch = ub.isCheckValAcc("printdonoinvoicebatch", plant,LOGIN_USER);
	displaySummaryPrintContainer = ub.isCheckValAcc("printdoinvoicecontainer", plant,LOGIN_USER);
	displaySummaryLink=true;
	displaySummaryPrintBatch=true;
	displaySummaryPrintNoBatch=true;
	displaySummaryPrintContainer=true;
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryLink = ub.isCheckValinv("summarylnkdoprice", plant,LOGIN_USER);
	displaySummaryPrintBatch = ub.isCheckValinv("printdoinvoicebatch", plant,LOGIN_USER);
	displaySummaryPrintNoBatch = ub.isCheckValinv("printdonoinvoicebatch", plant,LOGIN_USER);
	displaySummaryPrintContainer = ub.isCheckValinv("printdoinvoicecontainer", plant,LOGIN_USER);
	displaySummaryLink=true;
	displaySummaryPrintBatch=true;
	displaySummaryPrintNoBatch=true;
	displaySummaryPrintContainer=true;
}
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
// String curDate =_dateUtils.getDate();
String curDate =du.getDateMinusDays(); //resvi
if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
	curDate =DateUtils.getDate();
String btnContainerDisabled="disabled";
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;  //resvi

if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);



DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
USER          = StrUtils.fString(request.getParameter("USER"));
DONO       = StrUtils.fString(request.getParameter("DONO"));
CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
CUSTOMERID      = StrUtils.fString(request.getParameter("CUSTOMERID"));
PICKSTATUS    = StrUtils.fString(request.getParameter("PICKSTATUS"));
issuests = PICKSTATUS;
ISSUESTATUS  = StrUtils.fString(request.getParameter("ISSUESTATUS"));
statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
EMP_NAME = StrUtils.fString(request.getParameter("EMP_NAME"));
INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));

allChecked = StrUtils.fString(request.getParameter("allChecked"));

/*
if(DIRTYPE.length()<=0){
DIRTYPE = "OB_SUMMARY_ORD_WITH_PRICE";//OUTBOUNDSUMMARYWITHPRICE
}*/

%>

<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Sales Order with price</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
                   <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>            
              </h1>
              </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="" target="_blank">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
    <input type="hidden" name="STATUS_ID" value="">
		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="CUSTOMER NAME" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" >				
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>								 -->
				<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="CUSTOMER_TYPE_ID" name="CUSTOMER_TYPE_ID"  placeholder="CUSTOMER GROUP" value="<%=StrUtils.forHTMLTag(CUSTOMERTYPE)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomertypeid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="DONO" name="DONO" placeholder="ORDER NO" value="<%=StrUtils.forHTMLTag(DONO)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeorderno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('list/ob_order_list.jsp?DONO='+form1.DONO.value+'&STATUS='+form1.PICKSTATUS.value+'&PICKISSUE=Y'+'&FROMDATE='+form1.FROM_DATE.value+'&TODATE='+form1.TO_DATE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="JOBNO" name="JOBNO" value="<%=StrUtils.forHTMLTag(JOBNO)%>" placeholder="REFERENCE NO">				
  		</div>
		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status"  placeholder="ORDER TYPE" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>">
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeordertype(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('displayOrderType.jsp?OTYPE=SALES&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>

<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
    		<div class="col-sm-4 ac-box">
  
 <input type="text" name="PICKSTATUS" id="PICKSTATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(issuests)%>" placeholder="STATUS" >
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changepicksatus(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>	
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="EMP_NAME" id="EMP_NAME" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(EMP_NAME)%>" placeholder="EMPLOYEE" >
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeempname(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form1.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  		

  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  
  			<div class="col-sm-4 ac-box" style="display: none;">
  		<div class=""> 
  		<input type="text" name="INVOICENO" id="INVOICENO" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(INVOICENO)%>" placeholder="INVOICENO" >
		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeinvoiceno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('list/ob_invoiceno_list.jsp?DONO='+form1.DONO.value+'&INVOICENO='+form1.INVOICENO.value+'&STATUS='+form1.PICKSTATUS.value+'&PICKISSUE=Y'+'&FROMDATE='+form1.FROM_DATE.value+'&TODATE='+form1.TO_DATE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				
			</div>
			
			
  	</div>
		</div>
		
  		</div>
 		
 		<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-10">
      <!-- <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','ROB');}"> <b>Back</b></button> -->
  	</div>
        </div>
       	  </div>
 		
        <INPUT type="Hidden" name="DIRTYPE" value="OB_PRINT_PRICE">
        
        
        
        <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
                        &nbsp; Select/Unselect All &nbsp;<div style="display: none;"><input type="checkbox" class="form-check-input" name="printwithigino" value="printwithigino" />&nbsp;Print with GINO</div>
                     &nbsp;<input type="checkbox" class="form-check-input" name="printwithinvoiceno" value="printwithinvoiceno" />&nbsp;Print with Invoice</div>
  </div>
        
  
       <div id="VIEW_RESULT_HERE" class="table-responsive">
      <table id="tablePOList"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">CHK</th>
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">ORDER NO</th>
		                	<th style="font-size: smaller;">ORDER TYPE</th>
		                	<th style="font-size: smaller;">REF. NO.</th>
		                	<th style="font-size: smaller;">CUSTOMER NAME</th>
		                	<th style="font-size: smaller;">ORDER QTY.</th>
		                	<th style="font-size: smaller;">PICK QTY.</th>
		                	<th style="font-size: smaller;">ISSUE QTY</th>
		                	<th style="font-size: smaller;">EMPLOYEE</th>
		                	<th style="font-size: smaller;">STATUS</th>
		                     </tr>
		            </thead>
				</table>
      </div>
  <div id="spinnerImg" ></div>
  
  <%            
  		DOUtil poUtil = new DOUtil();
  
        Map ma = poUtil.getDOReceiptInvoiceHdrDetails(plant,"OUTBOUND ORDER");
		       //-----Modified by Deen on Feb 26 2014, Description:To display NOCONTAINER data's in PrintWithContainer/Batch/Sno report
		       if(ma.get("DisplayContainer").equals("1"))
		        {
		            	btnContainerDisabled="";
		        }
    	      
    %>  
    
    <div class="form-group">
  	<div class="col-sm-12" align="center">
  	<%if(displaySummaryPrintBatch){ %>    
  	<button type="button" class="Submit btn btn-default"  value="PrintWithBatch/Sno"  name="action" onclick="javascript:return onRePrint();"><b>PrintWithBatch/Sno</b></button>
  	<%} %>
  	<%if(displaySummaryPrintNoBatch){ %>  
  	<button type="button" class="Submit btn btn-default"  value="PrintWithOutBatch/Sno"  name="action" onclick="javascript:return onRePrintWithOutBatch();"><b>PrintWithOutBatch/Sno</b></button>
  	<%} %>
  	<%if(displaySummaryPrintContainer){ %>  
  	<button type="button" class="Submit btn btn-default"  value="PrintWithContainer/Batch/Sno"  name="action" onclick="javascript:return onRePrintWithContainer();" <%=btnContainerDisabled%>><b>PrintWithContainer/Batch/Sno</b></button>
  	<%} %> 		 
  	</div>
  	</div>
  	
    
  </FORM>
  </div>
  </div>
  </div>
    
    
 <SCRIPT LANGUAGE="JavaScript">
 var tablePOList;
 var FROM_DATE, TO_DATE, DIRTYPE, USER, ORDERNO, JOBNO, PICKSTATUS, ORDERTYPE, STATUS_ID, EMPNO, CUSTOMERTYPE,INVOICENO;
 function getParameters(){
 	return {
 		
 	FDATE:FROM_DATE,TDATE:TO_DATE,DTYPE:DIRTYPE,CNAME:USER,ORDERNO:ORDERNO,JOBNO:JOBNO,PICKSTATUS:PICKSTATUS,ORDERTYPE:ORDERTYPE,STATUS_ID:STATUS_ID,TYPE:"PICKISSUE",INVOICE:"Y",EMPNO:EMPNO,INVOICENO:INVOICENO,CUSTOMERTYPE:CUSTOMERTYPE,ACTION: "VIEW_OUTBOUND_DETAILS_PRINT",PLANT:"<%=plant%>",LOGIN_USER:"<%=USERID%>"	
 		
 	}
 }   
  function onGo(){
//debugger;
   var flag    = "false";

    FROM_DATE      = document.form1.FROM_DATE.value;
    TO_DATE        = document.form1.TO_DATE.value;
    DIRTYPE        = document.form1.DIRTYPE.value;
    USER           = document.form1.CUSTOMER.value;
    ORDERNO        = document.form1.DONO.value;
    INVOICENO        = document.form1.INVOICENO.value;
    JOBNO          = document.form1.JOBNO.value;
    PICKSTATUS     = document.form1.PICKSTATUS.value;
    //imti start
    if(PICKSTATUS=="PARTIALLY ISSUED")
    	PICKSTATUS="O";
    else if(PICKSTATUS=="SHIPPED")
    	PICKSTATUS="C";
    //imti end
    ORDERTYPE      = document.form1.ORDERTYPE.value; 
    STATUS_ID      = document.form1.STATUS_ID.value; 
    EMPNO          = document.form1.EMP_NAME.value;
    CUSTOMERTYPE   = document.form1.CUSTOMER_TYPE_ID.value;
    
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(INVOICENO != null     && INVOICENO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}
    var urlStr = "/track/OutboundOrderHandlerServlet";
    if (tablePOList){
 	   tablePOList.ajax.url( urlStr ).load();
    }else{
 	   tablePOList = $('#tablePOList').DataTable({
 			"processing": true,
 			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
//  			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
 			"ajax": {
 				"type": "POST",
 				"url": urlStr,
 				"data": function(d){
 					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
 				}, 
 				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
 		        "dataType": "json",
 		        "dataSrc": function(data){
 		        	if(typeof data.items[0].dono === 'undefined'){
 		        		return [];
 		        	}else {
 		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
 		        			data.items[dataIndex]['SNO'] = dataIndex + 1;
 		        			//<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="+dono+" >
 		        			data.items[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.items[dataIndex]['dono']+'" >';
 		        			<%if(displaySummaryLink){ %> 
 		        			data.items[dataIndex]['dono'] = '<a href="../salesorder/salespdfdetails?DONO=' +data.items[dataIndex]['dono']+ '&INVOICE=N&TYPE=PICKISSUE&FROMDATE='+FROM_DATE+'&TODATE='+TO_DATE+'">' + data.items[dataIndex]['dono'] + '</a>';
 		        			//<a href=/track/jsp/PrintDODetails.jsp?DONO=" +dono+ "&INVOICE="+INVOICE+"&TYPE="+TYPE+"&FROMDATE="+fdate+"&TODATE="+tdate+">" + dono + "</a>
 		        			<%}else{ %> 
 		        			data.items[dataIndex]['dono'] = data.items[dataIndex]['dono'];
 		        			<%} %> 
 		        			var lnstat = data.items[dataIndex]['status']
 		        			if(lnstat=="O")
 		        		    	   lnstat="PARTIALLY ISSUED";
 		        		       else if(lnstat=="C")
 		        		    	   lnstat="SHIPPED";
 		        			 data.items[dataIndex]['status'] = lnstat;
 		        		}
 		        		return data.items;
 		        	}
 		        }
 		    },
 	        "columns": [
 	        	{"data": 'CHKOB', "orderable": false},
 	        	{"data": 'SNO', "orderable": false},
 	        	{"data": 'dono', "orderable": false},
     			{"data": 'ordertype', "orderable": false},
     			{"data": 'jobnum', "orderable": false},
     			{"data": 'custname', "orderable": false},
     			{"data": 'qtyor', "orderable": false},
     			{"data": 'qtypick', "orderable": false},
     			{"data": 'qty', "orderable": false},
     			{"data": 'empname', "orderable": false},
     			{"data": 'status', "orderable": false},
     			],
 			"columnDefs": [{"className": "t-right", "targets": [6, 7, 8]}],
 			/*"orderFixed": [ groupColumn, 'asc' ], 
 			/*"dom": 'lBfrtip',*/
 			"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
 			"<'row'<'col-md-6'><'col-md-6'>>" +
 			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
 	        buttons: [
 	        	
 	        ],
 			"drawCallback": function ( settings ) {
 			}
 		});
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
			
	        $.each(data.items, function(i,item){
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#dddddd";
                     
	        	outPutdata = outPutdata+item.OUTBOUNDDETAILS
                      	ii++;
	            
	          });
		}else{
		//	outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="6"><BR><CENTER><B><FONT COLOR="RED">No details found!</FONT></B></CENTER></TD></TR>';
		}
      outPutdata = outPutdata +'</TABLE>';
      document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
       document.getElementById('spinnerImg').innerHTML ='';

   
 }

function getTable(){
   return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
          '<TR BGCOLOR="#000066">'+
          '<TH><font color="#ffffff" align="left"><b>Chk</TH>'+
          '<TH><font color="#ffffff" align="center">S/N</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Type</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Ref No</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Cust Name</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Order Qty</TH>'+
           '<TH><font color="#ffffff" align="left"><b>Pick Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Issue Qty</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Employee</TH>'+ 
          '<TH><font color="#ffffff" align="left"><b>Status</TH>'+ 
           '</tr>';
              
}
 

            
  </SCRIPT> 

  
                  <!-- Below Jquery Script used for Show/Hide Function-->
 <script>

 function changecustomer(obj){
	 $("#CUSTOMER").typeahead('val', '"');
	 $("#CUSTOMER").typeahead('val', '');
	 $("#CUSTOMER").focus();
	}
 
 function changecustomertypeid(obj){
	 $("#CUSTOMER_TYPE_ID").typeahead('val', '"');
	 $("#CUSTOMER_TYPE_ID").typeahead('val', '');
	 $("#CUSTOMER_TYPE_ID").focus();
 }

 function changeorderno(obj){
	 $("#DONO").typeahead('val', '"');
	 $("#DONO").typeahead('val', '');
	 $("#DONO").focus();
	}

 function changeordertype(obj){
	 $("#ORDERTYPE").typeahead('val', '"');
	 $("#ORDERTYPE").typeahead('val', '');
	 $("#ORDERTYPE").focus();
	}

 function changepicksatus(obj){
	 $("#PICKSTATUS").typeahead('val', '"');
	 $("#PICKSTATUS").typeahead('val', '');
	 $("#PICKSTATUS").focus();
	}

 function changeempname(obj){
	 $("#EMP_NAME").typeahead('val', '"');
	 $("#EMP_NAME").typeahead('val', '');
	 $("#EMP_NAME").focus();
	}

 function changeinvoiceno(obj){
	 $("#INVOICENO").typeahead('val', '"');
	 $("#INVOICENO").typeahead('val', '');
	 $("#INVOICENO").focus();
	}
 
 $(document).ready(function(){
 onGo();
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
    var plant= '<%=plant%>';
	/* Customer Auto Suggestion */
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "getCustomerListData",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTMST);
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
		    return '<p onclick="getvendname(\''+data.CUSTNO+'\')">' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});
	
	/* Customer Type Auto Suggestion */
	$('#CUSTOMER_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CUSTOMER_TYPE_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "getCustomerListTypeData",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUST_TYPE_MST);
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
		    return '<div><p class="item-suggestion">'+data.CUSTOMER_TYPE_ID+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});
	/* Order Number Auto Suggestion */
	$('#DONO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvoiceServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				DONO : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.orders);
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
		    return '<p>' + data.DONO + '</p>';
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
		}).on('typeahead:select',function(event,selection){
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.DONO.value = "";
			}
		
		});
	
	/* Employee Type Auto Suggestion */
	$('#EMP_NAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'EMPNO',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "getEmployeeListStartsWithName",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.EMP_MST);
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
		    return '<div><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">'+data.FNAME+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});
	
	/* Order Number Auto Suggestion */
	$('#ORDERTYPE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ORDERTYPE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvoiceServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_TYPE_FOR_AUTO_SUGGESTION",
				OTYPE : "SALES",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.ORDERTYPEMST);
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
		    return '<p>' + data.ORDERTYPE + '</p>';
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
		}).on('typeahead:select',function(event,selection){
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.ORDERTYPE.value = "";
			}
		
		});
	$("#PICKSTATUS").typeahead({
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
 });
 </script>
   
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
  
