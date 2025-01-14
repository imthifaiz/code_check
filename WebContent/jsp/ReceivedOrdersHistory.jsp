<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.db.object.FinCountryTaxType"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.dao.ItemMstDAO"%>
<%@page import="java.math.BigDecimal" %>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Purchase Order Summary By Cost";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
  <jsp:param value="<%=title%>" name="title"/>
  <jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
   <jsp:param name="submenu" value="<%=IConstants.PURCHASE_REPORTS%>"/>
</jsp:include>
<style type="text/css">
.dt-button-collection.dropdown-menu{
left: 50px; !important;
}
</style>
<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
{
   document.form1.action = "/track/ReportServlet?action=ExportReceivedHistory";
   document.form1.submit();
  
}
function onGo(){
   var flag    = "false";
   var FROM_DATE      = document.form1.FROM_DATE.value;
   var TO_DATE        = document.form1.TO_DATE.value;
   var USER           = document.form1.CUSTOMER.value;
   var ORDERNO        = document.form1.ORDERNO.value;
   var LOCALEXPENSES        = document.form1.LOCALEXPENSES.value;
   var CURRENCYDISPLAY        = document.form1.CURRENCYDISPLAY.value;
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(USER != null    && USER != "") { flag = true;}
   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
  // if(LOCALEXPENSES != null     && LOCALEXPENSES != "") { flag = true;}
  //storeUserPreferences();
   document.form1.action="../purchaseorder/recivedordershistory";
   document.form1.submit();
}
/* function storeUserPreferences(){
	storeInLocalStorage('ReceivedOrdersHistory_FROMDATE', document.form1.FROM_DATE.value);
	storeInLocalStorage('ReceivedOrdersHistory_TODATE', document.form1.TO_DATE.value);
	storeInLocalStorage('ReceivedOrdersHistory_CUSTOMER', document.form1.CUSTOMER.value);
	storeInLocalStorage('ReceivedOrdersHistory_SUPPLIER_TYPE_ID',document.form1.SUPPLIER_TYPE_ID.value);
	storeInLocalStorage('ReceivedOrdersHistory_ORDERNO', document.form1.ORDERNO.value);
} */
</script>

<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>

<%
	StrUtils _strUtils = new StrUtils();
	Generator generator = new Generator();
	HTReportUtil movHisUtil = new HTReportUtil();
	PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
	CurrencyDAO  _CurrencyDAO  = new CurrencyDAO();
	RecvDetDAO  _RecvDetDAO =new RecvDetDAO();
	ArrayList movQryList = new ArrayList();
	ArrayList prodGstList = new ArrayList();
	Hashtable ht = new Hashtable();
	movHisUtil.setmLogger(mLogger);
	
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
        String userID = (String) session.getAttribute("LOGIN_USER");
        String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
        String systatus = session.getAttribute("SYSTEMNOW").toString();
	String FROM_DATE = "", TO_DATE = "", USER = "",DIRTYPE ="", 
			fdate = "", tdate = "", ORDERNO = "",JOBNO="", cntRec = "false",CUSTOMER = "",INVOICENUM = "",LOCALEXPENSES="",CURRENCYDISPLAY="", 
			PGaction = "",statusID="",taxby="",SUPPLIERTYPE="";
	String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
	String basecurrency=_PlantMstDAO.getBaseCurrency(plant);
	/* if(CURRENCYDISPLAY.length()<0||CURRENCYDISPLAY==null||CURRENCYDISPLAY.equalsIgnoreCase("")) */
	/* CURRENCYDISPLAY=basecurrency; */
	
	boolean displaySummaryExport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displaySummaryExport = ub.isCheckValAcc("exportrecvhis", plant,LOGIN_USER);
		displaySummaryExport = true;
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryExport = ub.isCheckValinv("exportrecvhis", plant,LOGIN_USER);
		displaySummaryExport = true;
	}
	//float subtotal=0,gst=0,total=0;
	BigDecimal gst;
	BigDecimal subtotal;
	BigDecimal total;
	float gsttotal=0;
	//float grandtotal=0,gstpercentage=0,prodgstsubtotal1=0;
	BigDecimal grandtotal;
	BigDecimal gstpercentage;
	BigDecimal prodgstsubtotal1;
	int k=0;
	
	DecimalFormat decformat = new DecimalFormat("#,##0.00");

	FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
	String  fieldDesc="";

	if (FROM_DATE == null)
		FROM_DATE = "";
	else
		FROM_DATE = FROM_DATE.trim();
	String curDate = DateUtils.getDateMinusDays();
	if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
		curDate =DateUtils.getDate();
	if (FROM_DATE.length() < 0 || FROM_DATE == null
			|| FROM_DATE.equalsIgnoreCase(""))
		FROM_DATE = curDate;
	if (FROM_DATE.length() > 5)

		//fdate = FROM_DATE.substring(6) + "-"+ FROM_DATE.substring(3, 5) + "-"+ FROM_DATE.substring(0, 2);
		fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);
		

	//MLogger.log(0," FROM_DATE  : "+ FROM_DATE);

	if (TO_DATE == null)
		TO_DATE = "";
	else
		TO_DATE = TO_DATE.trim();
	if (TO_DATE.length() > 5)
		// tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5)+ "-" + TO_DATE.substring(0, 2);
		 tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);
		
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	//MLogger.log(0," TO_DATE  : "+ TO_DATE);
	DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
	PGaction = StrUtils.fString(request.getParameter("PGaction")).trim();
	USER = StrUtils.fString(request.getParameter("USER"));
	ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
	CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
	statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
	CURRENCYDISPLAY = StrUtils.fString(request.getParameter("CURRENCYDISPLAY"));
	
	if(CURRENCYDISPLAY.length()<0||CURRENCYDISPLAY==null||CURRENCYDISPLAY.equalsIgnoreCase("")){
		CURRENCYDISPLAY=basecurrency;
	}
	
	SUPPLIERTYPE = StrUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
	LOCALEXPENSES = StrUtils.fString(request.getParameter("LOCALEXPENSES"));
	if(DIRTYPE.length()<=0){
		DIRTYPE = "RECEIVEDHISTORY";
		}
	
	 if(LOCALEXPENSES.equals(""))
	 {
	 	LOCALEXPENSES="1";
	 }  
	if (PGaction.equalsIgnoreCase("View")) {
		
		try {
				//Hashtable ht = new Hashtable();	
			
					if (StrUtils.fString(ORDERNO).length() > 0)ht.put("a.PONO", ORDERNO);
					if (StrUtils.fString(statusID).length() > 0)ht.put("b.STATUS_ID", statusID);
				    if(StrUtils.fString(SUPPLIERTYPE).length() > 0) ht.put("SUPPLIERTYPE",SUPPLIERTYPE);
				    if(StrUtils.fString(JOBNO).length() > 0) ht.put("JobNum",JOBNO);
				   //if(StrUtils.fString(LOCALEXPENSES).length() > 0) ht.put("LOCALEXPENSES",LOCALEXPENSES);
				//movQryList = movHisUtil.getReceivedInboundOrdersSummary(ht, fdate,tdate,plant,CUSTOMER);
								
							

				//if (movQryList.size() <= 0)
					//cntRec = "true";
			    
			} catch (Exception e) {
				  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
			}
	}	
	
	String collectionDate=DateUtils.getDate();
	PlantMstUtil plantmstutil = new PlantMstUtil();
	List viewlistQry = plantmstutil.getPlantMstDetails(plant);
	    Map map = (Map) viewlistQry.get(0);
	String DELIVERYAPP = StrUtils.fString((String)map.get("ISRIDERRAPP"));
	String CNAME = (String) map.get("PLNTDESC");
	String ADD1 = (String) map.get("ADD1");
	String ADD2 = (String) map.get("ADD2");
	String ADD3 = (String) map.get("ADD3");
	String ADD4 = (String) map.get("ADD4");
	String STATE = (String) map.get("STATE");
	String COUNTRY = (String) map.get("COUNTY");
	String ZIP = (String) map.get("ZIP");
	String TELNO = (String) map.get("TELNO");

	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY+" "+ZIP;
	
	String currency=_PlantMstDAO.getBaseCurrency(plant);
		
%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb" style="background-color: rgb(255, 255, 255);">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>                
                <li><label>Purchase Order Summary By Cost</label></li>                             
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                   <%if(displaySummaryExport){ %>
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data </button>
<!-- 					   <ul class="dropdown-menu" style="min-width: 0px;"> -->
<!-- 					   <li id="Export-Remarks"><a href="javascript:ExportReportWithProductRemarks();">Export With Product Remarks</a></li> -->
<!-- 					   <li id="Export-All"><a href="javascript:ExportReport();">Excel</a></li> -->
<!-- 					  </ul> -->
					   <%} %>					
				</div>
				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">
<form class="form-horizontal" name="form1" method="post" action="/track/ReportServlet?">
<input type="hidden" name="PGaction" value="View"> 
<INPUT type="Hidden" name="DIRTYPE" value="RECEIVEDHISTORY">
<input type="hidden" name="CUSTOMERID" value="">
<input type="hidden" name="STATUS_ID" value="">
<input type="hidden" name="currencyuseqt" value="">

<span style="text-align:center;"><small><%=fieldDesc%></small></span>

<%-- <Center><h1><small><%=fieldDesc%></small></h1></Center> --%>

		<div id="target" style="padding: 18px; display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" id="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		
  			<div class="col-sm-4 ac-box">
  			<div class=""> 
  		<input type="text" class="ac-selected form-control typehead" id="ORDERNO" name="ORDERNO"  placeholder="ORDER NO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>">
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeorderno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  		</div>
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
		 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER"  placeholder="SUPPLIER" name="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>">				
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 				<span class="select-search btn-danger input-group-addon" onclick="javascript:popUpWin('../jsp/supplierlist.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>								 -->
		<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
		</div>			
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="SUPPLIER_TYPE_ID" name="SUPPLIER_TYPE_ID"   placeholder="SUPPLIER GROUP" value="<%=StrUtils.forHTMLTag(SUPPLIERTYPE)%>">
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomertypeid(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
<!-- 	<span class="select-search btn-danger input-group-addon " onclick="javascript:popUpWin('../jsp/SupplierTypeList.jsp?SUPPLIER_TYPE_ID='+form1.SUPPLIER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->				
  		</div>
		<div class="col-sm-4 ac-box">
		<div class=""> 
		<input type="text" class="ac-selected form-control" id="CURRENCYDISPLAY" name="CURRENCYDISPLAY"    placeholder="CURRENCY" value="<%=StrUtils.forHTMLTag(CURRENCYDISPLAY)%>">
  	    <button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecurrency(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
		</div>			
  		</div>
  		</div>
  		
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  	  		<div class="col-sm-4 ac-box">  	
			<div class="col-sm-10 txn-buttons">
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
			</div>
		</div>
		</div>

  		
<!--   		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				
			</div>
		</div> -->
		
		
  		</div>
		</div>
  	
  	
  	
  	  <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
      <!-- <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  	  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RIB');}"> <b>Back</b></button> -->
  	   </div>
        </div>
       	  </div>     
<br>
<div class="form-group">
 				<div class="col-sm-5" >
 	  <input type="checkbox" class="form-check-input" id="withlocalexpenses"   Onclick="checkval()" <%if (LOCALEXPENSES.equalsIgnoreCase("1")) {%> checked <%}%>/>&nbsp;View By Landed Cost&nbsp;  		
 	<INPUT class="form-control"  name="LOCALEXPENSES" type="hidden" value="<%=StrUtils.forHTMLTag(LOCALEXPENSES)%>"	size="30" MAXLENGTH=20>
 				</div>
 				</div>


<div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableIssueSummary"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
        <!--  <TH><font color="#ffffff" align="center">S/N</TH> -->
          <th style="font-size: smaller;">ORDER NO</TH>
          <th style="font-size: smaller;">REF NO</TH>
          <th style="font-size: smaller;">SUPPLIER NAME</TH>
          <th style="font-size: smaller;">RECEIVED DATE</TH>
          <th style="font-size: smaller;">EXCHANGE RATE</TH>
          <th style="font-size: smaller;">SUBTOTAL (<%=currency%>)</th>
          <th style="font-size: smaller;">SUBTOTAL</TH>
          <th style="font-size: smaller;">TAX (<%=currency%>)</th>
          <th style="font-size: smaller;">TAX</TH>
          <th style="font-size: smaller;">TOTAL (<%=currency%>)</th>
          <th style="font-size: smaller;">TOTAL</TH>
          <th style="font-size: smaller;">RECEIVED BY</TH>
          </tr>
		            </thead>
		            
		            <tbody>
				 
				</tbody>
				<tfoot style="display: none;">
		            <tr class="group">
		            <th colspan='4'></th>
		            <th style="text-align: left !important">Grand Total</th>
		            <!-- <th></th> -->
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th style="text-align: left !important"></th>
		            <th></th>
		            </tr>
		        </tfoot>
				</table>
            		</div>
						</div>
					</div>
  </div>
<script>
  var tableData = [];
 
 
  <%
  
	 if (LOCALEXPENSES.equalsIgnoreCase("1"))
		
	{ 
		 
		movQryList = movHisUtil.getReceivedInboundOrdersSummarylocalexpenses(ht, fdate,tdate,plant,CUSTOMER);
	}else
	
	{ 
		movQryList = movHisUtil.getReceivedInboundOrdersSummary(ht, fdate,tdate,plant,CUSTOMER);
	}
	
	
	if (movQryList.size() <= 0)
		cntRec = "true";
		if (movQryList.size() <= 0 && cntRec == "true") {
	%>
	/* <TR>
		<TD colspan=15 align=center>No Data For This criteria</TD>
	</TR> */
	<%
		}
	%>

	<%	
	    taxby=_PlantMstDAO.getTaxBy(plant);  
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			//k=k+1;
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#FFFFFF";
			
			//Float subtotal1 =  Float.parseFloat(((String) lineArr.get("subtotal").toString())) ;
			BigDecimal  subtotal1 =  new BigDecimal(((String) lineArr.get("subtotal").toString())) ;
			
			String CURRENCYUSEQT="",CURRENCYID="";

			//System.out.println(subtotal1);
			if(taxby.equalsIgnoreCase("BYORDER"))
			{
				gstpercentage = new BigDecimal(((String) lineArr.get("inbound_gst").toString())) ;
				//gst = (subtotal1*gstpercentage)/100;
// 				gst = new BigDecimal(((String) lineArr.get("taxval").toString())) ;
				    gst = new BigDecimal("0");
      	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
      	           System.out.println("imthiyas faizil = "+taxid);
      	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
      	           if(taxid != 0){
      	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
      	        		   gst = (subtotal1.multiply(gstpercentage));
      	        		   gst= gst.divide(new BigDecimal("100"));
      	        	   } else 
      	        		 gstpercentage=new BigDecimal("0.000");
      	        	   
      	           }else 
      	        		 gstpercentage=new BigDecimal("0.000");
			}
			else
			{
				prodGstList = _RecvDetDAO.getRecvProductGst(plant,(String) lineArr.get("pono").toString());
				prodgstsubtotal1= BigDecimal.ZERO;
				for (int jCnt = 0; jCnt < prodGstList.size(); jCnt++) {
					Map prodGstArr = (Map)prodGstList.get(jCnt);
					int jIndex = jCnt + 1;
					BigDecimal prosubtotal =new BigDecimal(((String) prodGstArr.get("subtotal").toString()));
					prodgstsubtotal1.add(prosubtotal);
				}
					gst=prodgstsubtotal1;
			}
			String subTotalValue=String.valueOf(subtotal1);
			float subTotalVal= "".equals(subTotalValue) ? 0.0f :  Float.parseFloat(subTotalValue);
			//System.out.println(subTotalVal);
			if(subTotalVal==0f){
				subTotalValue="0.00000";
			}else{
				subTotalValue=subTotalValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			}
			
			total = subtotal1.add(gst);
			//System.out.println(total);
			String pono = (String) lineArr.get("pono");
			String GSTValue = String.valueOf(gst);
			String TOTALValue =  String.valueOf(total);  
			//System.out.println("Total "+TOTALValue);
			float gstVal= "".equals(GSTValue) ? 0.0f :  Float.parseFloat(GSTValue);
			float totalVal= "".equals(TOTALValue) ? 0.0f :  Float.parseFloat(TOTALValue);
			
			
            
            List listQry = _CurrencyDAO.getCurrencyListWithcurrencySeq(plant,CURRENCYDISPLAY);
            for(int i =0; i<listQry.size(); i++) {
                Map m=(Map)listQry.get(i);
                 CURRENCYID=(String)m.get("currencyid");
                 CURRENCYUSEQT=(String)m.get("CURRENCYUSEQT");
            }
			
	           //DISPLAY COST BASED ON CURRENCY VALUE
            float CursubTotalValue=Float.parseFloat(subTotalValue)*Float.parseFloat(CURRENCYUSEQT);
            float CurGSTValue=Float.parseFloat(GSTValue)*Float.parseFloat(CURRENCYUSEQT);
            float CurTOTALValue=Float.parseFloat(TOTALValue)*Float.parseFloat(CURRENCYUSEQT);
			
			if(totalVal==0f){
				TOTALValue="0.00000";
			}else{
				TOTALValue=TOTALValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			}if(gstVal==0f){
				GSTValue="0.000";
			}else{
				GSTValue=GSTValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			}
			
			
			String currencyid = (String)lineArr.get("CURRENCYID");
            double currencyseqt = Double.valueOf((String)lineArr.get("CURRENCYUSEQT"));
            double rcvCostconv = 0,recvCostwTaxConv=0,taxValcon=0;
            
            rcvCostconv = subTotalVal * currencyseqt; 
            String SrcvCostconv= String.valueOf(rcvCostconv);
//            currencyid = "";
            SrcvCostconv = currencyid + String.format("%.2f", rcvCostconv);
            
            recvCostwTaxConv = totalVal * currencyseqt; 
            String SrecvCostwTaxconv = currencyid + String.format("%.2f", recvCostwTaxConv);
            String exchangerate = String.format("%.2f", currencyseqt);
            
            taxValcon = gstVal  * currencyseqt; 
            String StaxValcon = currencyid + String.format("%.2f", taxValcon);
	
	%>
	
	var rowData = [];
     <%-- rowData[rowData.length] = '<a href="../purchaseorder/recivedorderdetails?PONO=<%=pono%>&DATE=<%=(String)lineArr.get("crat")%>" ><%=(String) lineArr.get("pono")%></a>'; --%>
     rowData[rowData.length] = '<%=(String)lineArr.get("pono")%>';
     rowData[rowData.length] = '<%=(String)lineArr.get("JobNum")%>';
     rowData[rowData.length] = '<%=(String)lineArr.get("custname")%>';
     rowData[rowData.length] = '<%=(String)lineArr.get("recvdate")%>';
     rowData[rowData.length] = '<%=exchangerate%>';
     rowData[rowData.length] = '<%=subTotalValue%>';
     rowData[rowData.length] = '<%=SrcvCostconv%>';
<%--      rowData[rowData.length] = '<%=CursubTotalValue%>'; --%>
     rowData[rowData.length] = '<%=GSTValue%>';
<%--      rowData[rowData.length] = '<%=CurGSTValue%>'; --%>
     rowData[rowData.length] = '<%=StaxValcon%>';
     rowData[rowData.length] = '<%=TOTALValue%>';
<%--      rowData[rowData.length] = '<%=CurTOTALValue%>'; --%>
     rowData[rowData.length] = '<%=SrecvCostwTaxconv%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("receivedby")%>';
     <%-- rowData[rowData.length] = '<%=(String)lineArr.get("status_id") %>'; --%>
     tableData[tableData.length] = rowData;
     <%}%>    
 
$(document).ready(function(){
	   /* if (document.form1.FROM_DATE.value == ''){
		   getLocalStorageValue('ReceivedOrdersHistory_FROMDATE', '' , 'FROM_DATE');
        }
	   if (document.form1.TO_DATE.value == ''){
		   getLocalStorageValue('ReceivedOrdersHistory_TODATE', '', 'TO_DATE');
        }
	   if (document.form1.CUSTOMER.value == ''){
		   getLocalStorageValue('ReceivedOrdersHistory_CUSTOMER', '', 'CUSTOMER');
        }
	   if (document.form1.SUPPLIER_TYPE_ID.value == ''){
		   getLocalStorageValue('ReceivedOrdersHistory_SUPPLIER_TYPE_ID', '', 'SUPPLIER_TYPE_ID');
        }
	   if (document.form1.ORDERNO.value == ''){
		   getLocalStorageValue('ReceivedOrdersHistory_ORDERNO', '', 'ORDERNO');
        }
	 
	  storeUserPreferences(); */
	 for(var rowIndex = 0; rowIndex < tableData.length; rowIndex ++){
		 tableData[rowIndex][5] = addZeroes(parseFloat(tableData[rowIndex][5]).toFixed(<%=numberOfDecimal%>));
		 tableData[rowIndex][7] = addZeroes(parseFloat(tableData[rowIndex][7]).toFixed(<%=numberOfDecimal%>));
		 tableData[rowIndex][9] = addZeroes(parseFloat(tableData[rowIndex][9]).toFixed(<%=numberOfDecimal%>));
<%-- 		 tableData[rowIndex][3] = addZeroes(parseFloat(tableData[rowIndex][3]).toFixed(<%=numberOfDecimal%>));
		 tableData[rowIndex][4] = addZeroes(parseFloat(tableData[rowIndex][4]).toFixed(<%=numberOfDecimal%>));
		 tableData[rowIndex][5] = addZeroes(parseFloat(tableData[rowIndex][5]).toFixed(<%=numberOfDecimal%>)); --%>
	 }
	 $('#tableIssueSummary').DataTable({
		 "lengthMenu": [[50, 100, 500], [50, 100, 500]],
// 		 "lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
		  	data: tableData,
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
	    	                },
	    	                title: '<%=title%>',
	    	                footer: true
	                    },
	                    {
	                    	extend : 'pdf',
                            footer: true,
	                    	text: 'PDF Portrait',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
	                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
	                    	<%} else {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
	                    	<%}%>
                    		orientation: 'portrait',
	                    	customize: function(doc) {
	                    		doc.defaultStyle.fontSize = 7;
                     	        doc.styles.tableHeader.fontSize = 7;
                     	        doc.styles.title.fontSize = 10;
                     	        doc.styles.tableFooter.fontSize = 7;
	                    	},
                            pageSize: 'A4'
	                    },
	                    {
	                    	extend : 'pdf',
	                    	footer: true,
	                    	text: 'PDF Landscape',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
	                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
	                    	<%} else {%>
	                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
	                    	<%}%>
                    		orientation: 'landscape',
                    		customize: function(doc) {
	                    		doc.defaultStyle.fontSize = 6;
                     	        doc.styles.tableHeader.fontSize = 6;
                     	        doc.styles.title.fontSize = 8;                     	       
                     	        doc.content[1].table.widths = "*";
                     	       doc.styles.tableFooter.fontSize = 7;
	                    	     },
                            pageSize: 'A4'
	                    }
	                ]
	            },
	            {
                   extend: 'colvis',
                   columns: ':not(:eq(3)):not(:eq(4)):not(:eq(5)):not(:eq(6))'
                	   //columns: ':not(:eq('+groupColumn+')):not(:eq(9)):not(:eq(10))'
               }
	               
	        ],
	        "order":[], 
	        "drawCallback": function ( settings ) {
	        	this.attr('width', '100%');
	        	var groupColumn = 1;
				var groupRowColSpan= 4;
			   	var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalPickQty = 0;
	            var groupTotalPickQty = 0;
	            var totalIssueQty = 0;
	            var groupTotalIssueQty = 0;
	            var totalIssuePriceQty = 0;
	            var groupIssuePriceQty = 0;
	            
	            var totalPickQty1 = 0;
	            var groupTotalPickQty1 = 0;
	            var totalIssueQty1 = 0;
	            var groupTotalIssueQty1 = 0;
	            var totalIssuePriceQty1 = 0;
	            var groupIssuePriceQty1 = 0;
	            
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
						
	                	 if(groupTotalPickQty==null || groupTotalPickQty==0){
	                		 groupTotalPickQtyVal="0.000";
	     	            	}else{
	     	            		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
	     	            	} 
	     	            	if(groupTotalIssueQty==null ||groupTotalIssueQty==0){
	     	            		groupTotalIssueQtyVal="0.000";
	     	            	}else{
	     	            		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
	     	            	}if(groupIssuePriceQty==null || groupIssuePriceQty==0){
	     	            		groupIssuePriceQtyVal="0.000";
	     	            	}else{
	     	            		groupIssuePriceQtyVal=parseFloat(groupIssuePriceQty).toFixed(<%=numberOfDecimal%>);
	     	            	}
	     	            		                	
	                	
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
// 			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + addZeroes(groupTotalPickQtyVal) + '</td><td></td><td>' + addZeroes(groupTotalIssueQtyVal) + '</td><td></td><td>' + addZeroes(groupIssuePriceQtyVal) + '</td><td></td><td></td></tr>'
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + addZeroes(groupTotalPickQtyVal) + '</td><td class="t-right">' + parseFloat(groupTotalPickQty1).toFixed(<%=numberOfDecimal%>) + '</td><td>' + addZeroes(groupTotalIssueQtyVal) + '</td><td class="t-right">' + parseFloat(groupTotalIssueQty1).toFixed(<%=numberOfDecimal%>) + '</td><td>' + addZeroes(groupIssuePriceQtyVal) + '</td><td class="t-right">' + parseFloat(groupIssuePriceQty1).toFixed(<%=numberOfDecimal%>) + '</td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;    
	                    groupTotalPickQty = 0;
	                    groupTotalIssueQty = 0;
	                    groupIssuePriceQty = 0;
	                    groupTotalPickQty1 = 0;
	                    groupTotalIssueQty1 = 0;
	                    groupIssuePriceQty1 = 0;
	                    	                    
	                }
	                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
	                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
	                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
	                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
	                groupIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
	                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
	                
	                groupTotalPickQty1 += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(/[^0-9.]/g, ''));
	                totalPickQty1 += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(/[^0-9.]/g, ''));
	                groupTotalIssueQty1 += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(/[^0-9.]/g, ''));
	                totalIssueQty1 += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(/[^0-9.]/g, ''));
	                groupIssuePriceQty1 += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(/[^0-9.]/g, ''));
	                totalIssuePriceQty1 += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(/[^0-9.]/g, ''));
	                     
	                
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
					
					if(totalPickQty==null || totalPickQty==0){
	           		totalPickQtyVal="0.000";
	            	}else{
	            		totalPickQtyVal=parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>);
	            	} 
	            	if(totalIssueQty==null ||totalIssueQty==0){
	            		totalIssueQtyVal="0.000";
	            	}else{
	            		totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(<%=numberOfDecimal%>);
	            	}if(totalIssuePriceQty==null || totalIssuePriceQty==0){
	            		totalIssuePriceQtyVal="0.000";
	            	}else{
	            		totalIssuePriceQtyVal=parseFloat(totalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
	            	}
	            	if(groupTotalPickQty==null || groupTotalPickQty==0){
               		 groupTotalPickQtyVal="0.000";
    	            	}else{
    	            		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
    	            	} 
    	            	if(groupTotalIssueQty==null ||groupTotalIssueQty==0){
    	            		groupTotalIssueQtyVal="0.000";
    	            	}else{
    	            		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
    	            	}if(groupIssuePriceQty==null || groupIssuePriceQty==0){
    	            		groupIssuePriceQtyVal="0.000";
    	            	}else{
    	            		groupIssuePriceQtyVal=parseFloat(groupIssuePriceQty).toFixed(<%=numberOfDecimal%>);
    	            	}
	            	$(rows).eq( currentRow ).after(
// 	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td>><td>' + addZeroes(totalPickQtyVal) + '</td><td>' + addZeroes(totalIssueQtyVal) + '</td><td>' + addZeroes(totalIssuePriceQtyVal) + '</td><td></td></tr>'
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + addZeroes(totalPickQtyVal) + '</td><td class="t-right">' + parseFloat(totalPickQty1).toFixed(<%=numberOfDecimal%>) + '</td><td>' + addZeroes(totalIssueQtyVal) + '</td><td class="t-right">' + parseFloat(totalIssueQty1).toFixed(<%=numberOfDecimal%>) + '</td><td>' + addZeroes(totalIssuePriceQtyVal) + '</td><td class="t-right">' + parseFloat(totalIssuePriceQty1).toFixed(<%=numberOfDecimal%>) + '</td><td></td></tr>'
                   );
               	$(rows).eq( currentRow ).after(
// 	                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td></td><td>' + addZeroes(groupTotalPickQtyVal) + '</td><td></td><td>' + addZeroes(groupTotalIssueQtyVal) + '</td><td></td><td>' + addZeroes(groupIssuePriceQtyVal) + '</td><td></td><td></td></tr>'
	                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + addZeroes(groupTotalPickQtyVal) + '</td><td class="t-right">' + parseFloat(groupTotalPickQty1).toFixed(<%=numberOfDecimal%>) + '</td><td>' + addZeroes(groupTotalIssueQtyVal) + '</td><td class="t-right">' + parseFloat(groupTotalIssueQty1).toFixed(<%=numberOfDecimal%>) + '</td><td>' + addZeroes(groupIssuePriceQtyVal) + '</td><td class="t-right">' + parseFloat(groupIssuePriceQty1).toFixed(<%=numberOfDecimal%>) + '</td><td></td></tr>'
                   );
               }
	        },
	        "footerCallback": function(row, data, start, end, display) {
	        	var groupColumn = 1;
	            var api = this.api(),
	              data;

	            // Remove the formatting to get integer data for summation
	            var intVal = function(i) {
	              return typeof i === 'string' ?
	                i.replace(/[\$,]/g, '') * 1 :
	                typeof i === 'number' ?
	                i : 0;
	            };

	            // Total over all pages
	            total = api
	              .column(4)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);

	            // Total over this page
	            totalCostVal = api
	              .column(5)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalTaxVal = api
	              .column(7)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
	              
	            totalVal = api
	              .column(9)
	              .data()
	              .reduce(function(a, b) {
	                return intVal(a) + intVal(b);
	              }, 0);
				  
				  var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalPickQty = 0;
	            var groupTotalPickQty = 0;
	            var totalIssueQty = 0;
	            var groupTotalIssueQty = 0;
	            var totalIssuePriceQty = 0;
	            var groupIssuePriceQty = 0;
	            
	            var totalPickQty1 = 0;
	            var groupTotalPickQty1 = 0;
	            var totalIssueQty1 = 0;
	            var groupTotalIssueQty1 = 0;
	            var totalIssuePriceQty1 = 0;
	            var groupIssuePriceQty1 = 0;
	            
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
						
	                	 if(groupTotalPickQty==null || groupTotalPickQty==0){
	                		 groupTotalPickQtyVal="0.000";
	     	            	}else{
	     	            		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
	     	            	} 
	     	            	if(groupTotalIssueQty==null ||groupTotalIssueQty==0){
	     	            		groupTotalIssueQtyVal="0.000";
	     	            	}else{
	     	            		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
	     	            	}if(groupIssuePriceQty==null || groupIssuePriceQty==0){
	     	            		groupIssuePriceQtyVal="0.000";
	     	            	}else{
	     	            		groupIssuePriceQtyVal=parseFloat(groupIssuePriceQty).toFixed(<%=numberOfDecimal%>);
	     	            	}
	     	            		                	
	                
	                    last = group;
	                    groupEnd = i;    
	                    groupTotalPickQty = 0;
	                    groupTotalIssueQty = 0;
	                    groupIssuePriceQty = 0;
	                    groupTotalPickQty1 = 0;
	                    groupTotalIssueQty1 = 0;
	                    groupIssuePriceQty1 = 0;
	                    	                    
	                }
	                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
	                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
	                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
	                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
	                groupIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
	                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
	                
	                groupTotalPickQty1 += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(/[^0-9.]/g, ''));
	                totalPickQty1 += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(/[^0-9.]/g, ''));
	                groupTotalIssueQty1 += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(/[^0-9.]/g, ''));
	                totalIssueQty1 += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(/[^0-9.]/g, ''));
	                groupIssuePriceQty1 += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(/[^0-9.]/g, ''));
	                totalIssuePriceQty1 += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(/[^0-9.]/g, ''));
	                     
	                
	                currentRow = i;
	            } );

	            // Update footer
	            $(api.column(5).footer()).html(parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>));
	            $(api.column(7).footer()).html(parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>));
	            $(api.column(9).footer()).html(parseFloat(groupIssuePriceQty).toFixed(<%=numberOfDecimal%>));
				
				/* $(api.column(5).footer()).html(parseFloat(totalCostVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(7).footer()).html(parseFloat(totalTaxVal).toFixed(<%=numberOfDecimal%>));
	            $(api.column(9).footer()).html(parseFloat(totalVal).toFixed(<%=numberOfDecimal%>)); */
	          } 
		  });	 
});
function checkval()

{
	
	 var checkBox = document.getElementById("withlocalexpenses");
	 if (checkBox.checked == true){
		
		 document.form1.LOCALEXPENSES.value="1";		 
	 }
	 else
	
		 {	
		
		 document.form1.LOCALEXPENSES.value="0";		 
		 }
	 
}
</script>
	
</form>
</div></div></div>

                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>

 function changeorderno(obj){
	 $("#ORDERNO").typeahead('val', '"');
	 $("#ORDERNO").typeahead('val', '');
	 $("#ORDERNO").focus();
	}

 function changecustomer(obj){
	 $("#CUSTOMER").typeahead('val', '"');
	 $("#CUSTOMER").typeahead('val', '');
	 $("#CUSTOMER").focus();
	}


 function changecustomertypeid(obj){
	 $("#SUPPLIER_TYPE_ID").typeahead('val', '"');
	 $("#SUPPLIER_TYPE_ID").typeahead('val', '');
	 $("#SUPPLIER_TYPE_ID").focus();
	}

 function changecurrency(obj){
	 $("#CURRENCYDISPLAY").typeahead('val', '"');
	 $("#CURRENCYDISPLAY").typeahead('val', '');
	 $("#CURRENCYDISPLAY").focus();
	}
 
 $(document).ready(function(){
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
	/* Supplier Auto Suggestion */
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
// 		    return '<p>' + data.VNAME + '</p>';
		    	return '<div><p class="item-suggestion">Name: ' + data.VNAME 
		    	   + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME 
		    	   + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
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

	/* To get the suggestion data for Currency */
	$("#CURRENCYDISPLAY").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CURRENCY',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					ACTION : "GET_CURRENCY_DATA",
					CURRENCY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.CURRENCYMST);
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
// 			return '<div><p onclick="setCurrencyid(\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
			return '<div onclick="document.form1.currencyuseqt.value = \''+data.CURRENCYUSEQT+'\'"><p class="item-suggestion">'+data.CURRENCY+'</p></div>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
	
	}).on('typeahead:change',function(event,selection){
		
	});
	
	/* Supplier Type Auto Suggestion */
	$('#SUPPLIER_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'SUPPLIER_TYPE_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIERTYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.SUPPLIER_TYPE_MST);
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
		    return '<div><p class="item-suggestion">'+data.SUPPLIER_TYPE_ID+'</p></div>';
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
	$('#ORDERNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
				PONO : query
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
		    return '<p>' + data.PONO + '</p>';
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
				document.form1.ORDERNO.value = "";
			}
		
		});
 });
 

 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>