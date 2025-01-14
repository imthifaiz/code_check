<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Copy Credit Note";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String region = StrUtils.fString((String) session.getAttribute("REGION"));
//Validate no.of Customers -- Azees 15.11.2020
	CustMstDAO custdao = new CustMstDAO();
	String NOOFCUSTOMER=((String) session.getAttribute("NOOFCUSTOMER"));
	String ValidNumber="";
	ArrayList arrCustot =custdao.getTotalCustomers(plant);
	Map mCustot=(Map)arrCustot.get(0);
	String Custot = (String)mCustot.get("TOTAL_CUSTOMERS");
	int novalid = Integer.valueOf(Custot);
	if(!NOOFCUSTOMER.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFCUSTOMER);
		if(novalid>=convl)
		{
			ValidNumber=NOOFCUSTOMER;
		}
	}
String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
PlantMstDAO plantMstDAO = new PlantMstDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String compindustry = plantMstDAO.getcompindustry(plant);
DateUtils _dateUtils = new DateUtils();
CustMstDAO custMstDAO = new CustMstDAO();
String curDate =_dateUtils.getDate();
CustomerCreditnoteDAO custcrdnotedao= new CustomerCreditnoteDAO();
String custcrdnoteHDRid = StrUtils.fString(request.getParameter("custcreditid"));
Hashtable ht = new Hashtable();
ht.put("ID", custcrdnoteHDRid);
ht.put("PLANT", plant);
//List CustcrdnoteHdrList =  custcrdnotedao.getCustCreditnoteHdrById(ht);
List CustcrdnoteHdrList =  custcrdnotedao.getCov_CustCreditnoteHdrById(ht);
Map CustcrdnoteHdr=(Map)CustcrdnoteHdrList.get(0);
String prefixsalesloc = (String)CustcrdnoteHdr.get("SALES_LOCATION");
//List CustcrdnoteDetList = custcrdnotedao.getCustCrdnoteDedtByHrdId(ht);
List CustcrdnoteDetList = custcrdnotedao.getConv_CustCrdnoteDedtByHrdId(ht);
List CustcrdnoteAttachList=custcrdnotedao.getCustCrdnoteAttachByHrdId(ht);
String custName = custMstDAO.getCustName(plant, (String) CustcrdnoteHdr.get("CUSTNO"));
String taxbylabel = ub.getTaxByLable(plant);
String CURRENCYUSEQT="0",DISPLAY="";
String custCode = StrUtils.fString(request.getParameter("CUST_CODE"));
String CUSTOMER = StrUtils.fString(request.getParameter("CUST_NAME"));
String sTAXTREATMENT = StrUtils.fString(request.getParameter("TAXTREATMENT"));
String sCustCode = StrUtils.fString(request.getParameter("sCustCode"));
String companyregnumber=StrUtils.fString(request.getParameter("cus_companyregnumber"));
if(sCustCode.equalsIgnoreCase(""))
{
	sCustCode = custCode;
}


String COUNTRYCODE = "";
String plantstate = "";
String plantstatecode = "";
PlantMstUtil plantmstutil = new PlantMstUtil();
List viewlistQry = plantmstutil.getPlantMstDetails(plant);
for (int i = 0; i < viewlistQry.size(); i++) {
    Map map = (Map) viewlistQry.get(i);
    COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
    plantstate = StrUtils.fString((String)map.get("STATE"));
}
MasterDAO masterDAO = new MasterDAO();
boolean ispuloc=false;		 
ArrayList purloctions = masterDAO.getSalesLocationListByCode("", plant, COUNTRYCODE);
if (purloctions.size() > 0) {
	 ispuloc = true;
	 ArrayList purstate = masterDAO.getSalesLocationList(plantstate, plant, "");
	 for (int i = 0; i < purstate.size(); i++) {
	     Map map1 = (Map) purstate.get(i);
	     plantstatecode = StrUtils.fString((String)map1.get("PREFIX"));
	 }
}

String DISPLAYID="";
List curQryList=new ArrayList();
CurrencyDAO currencyDAO = new CurrencyDAO();
curQryList = currencyDAO.getCurrencyDetails(curency,plant);
for(int i =0; i<curQryList.size(); i++) {
	ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
	DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
	DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
    CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
    }

ArrayList arrCust = new CustUtil().getCustomerDetails((String) CustcrdnoteHdr.get("CUSTNO"), plant);
String sAddr1 = (String) arrCust.get(2);
String sAddr2 = (String) arrCust.get(3);
String sAddr3 = (String) arrCust.get(4);
String sAddr4 = (String) arrCust.get(16);
String sCountry = (String) arrCust.get(5);
String sZip = (String) arrCust.get(6);
String	sDesgination = (String) arrCust.get(10);
String sTelNo = (String) arrCust.get(11);
String sHpNo = (String) arrCust.get(12);
String sEmail = (String) arrCust.get(14);
String sFax = (String) arrCust.get(13);
String sState = (String) arrCust.get(22);
String sWorkphone = (String) arrCust.get(33);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.CREDIT_NOTES%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<style>
 .sideiconspan {
	color: #23527c;
	position: absolute;
	right: 20px;
	top: 8px;
	z-index: 2;
	vertical-align: middle;
	font-size: 15px;
	font-weight: bold;
}
a:focus {
  outline: 1px solid blue;
}
</style>
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/creditNoteCopy.js"></script>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>    
                <li><a href="../creditnote/summary"><span class="underline-on-hover">Credit Note Summary</span> </a></li> 
                <li><a href="../creditnote/detail?custcreditid=<%=custcrdnoteHDRid%>"><span class="underline-on-hover">Credit Note Detail</span> </a></li>                
                <li><label>Copy Credit Note</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../creditnote/detail?custcreditid=<%=custcrdnoteHDRid%>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
<div class="container-fluid">
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
	<form id="creditnoteForm" class="form-horizontal" name="form1" autocomplete="off" action="/track/CustomerCreditNoteServlet?Submit=Save"  method="post" enctype="multipart/form-data" onsubmit="return validateCreditnote()">
	<input type = "hidden" name="PROJECTID" value="">
	<input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
			<input type="hidden" name="CURRENCYUSEQTOLD" value="">
				<input type = "hidden" name="shiptaxstatus" value="0">
				<input type = "hidden" name="odiscounttaxstatus" value="1">
				<input type = "hidden" name="discounttaxstatus" value="1">
				<input type = "hidden" name="ptaxtype" value="">
				<input type = "hidden" name="ptaxpercentage" value="0">
				<input type = "hidden" name="ptaxdisplay" value="">
				<input type = "hidden" name="ptaxiszero" value="1">
				<input type = "hidden" name="ptaxisshow" value="0">
				<input type = "hidden" name="taxid" value="0">
				<!-- <input type = "hidden" name="GST" value="" > -->
				<input type = "hidden" name="custModal">
				<%if(!ispuloc){ %>
				 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
				<%} %>
				<input type="hidden" name="SHIPPINGID" value="">
	<div class="form-group customer-section">
			<label class="control-label col-form-label col-sm-2 required">Customer Name</label>
			<div class="col-sm-6 ac-box">
				<!-- <div class="input-group"> --> 
				<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" placeholder="Select a customer" name="CUSTOMER" readonly value="<%=CUSTOMER%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('customer_list_issue_summary.jsp?TYPE=ACCTCUST&CUST_NAME='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				<!-- </div> -->
				<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" id="nTAXTREATMENT" name="nTAXTREATMENT" value="<%=sTAXTREATMENT %>" style="width: 100%">
				<OPTION style="display:none;"></OPTION>
        <%
		   MasterUtil _MasterUtiln =new  MasterUtil();
		   ArrayList ccList1 =  _MasterUtiln.getTaxTreatmentList("",plant,"");
			for(int i=0 ; i<ccList1.size();i++)
      		 {
				Map m1=(Map)ccList1.get(i);
				String nTAXTREATMENT = (String)m1.get("TAXTREATMENT"); %>
		        <option  value='<%=nTAXTREATMENT%>' ><%=nTAXTREATMENT %> </option>		          
		        <%
      		}
			 %></SELECT>
				<input type="text" name="plant" value="<%=plant%>" hidden>
				<input type="text" name="username" value=<%=username%> hidden>
				<input type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
				<input type = "hidden" name="EMPNO">
				<input type = "hidden" name="HDRID" value="<%=custcrdnoteHDRid%>">
				<input type="hidden" name="invselect" />        
				<input type="hidden" name="DONO" />  
				<INPUT type="hidden" name="STATE_PREFIX" value="" />
				<INPUT type="hidden" name="INV_STATE_LOC" value="" />
				<INPUT type = "hidden" name="TAXTREATMENT_VALUE" value ="<%=sTAXTREATMENT%>">
				<input type = "hidden" name="CUST_CODE2" value="<%=sCustCode%>">   
				<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
				<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
				<INPUT type="hidden" name="curency"  value="<%=DISPLAYID%>">	
			    <INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>    
				<INPUT type="hidden" name="CURRENCYID"  value="<%=DISPLAYID%>">
				
			</div>
		</div>
		<!-- <div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Shipping Address:</label>
					<div class="col-sm-6 ac-box">
								<input type="text" class="form-control typeahead" id="SHIPPINGCUSTOMER" name="SHIPPINGCUSTOMER" value=""> 
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'SHIPPINGCUSTOMER\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> -->
								<!-- <span class="btn-danger input-group-addon"
								onclick="javascript:popUpWin('../jsp/shippingdetails_list.jsp?SHIPPINGCUSTOMER='+form.SHIPPINGCUSTOMER.value+'&FORMNAME=form');">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span>  -->
					<!-- </div>
				</div> -->
				<div class="form-group shipCustomer-section" id="shipbilladd">
					<div class="col-sm-2"></div>
					<div class="col-sm-3" style="line-height: 7px; font-size: 13px;">
						<h5 style="font-weight: bold;">Billing Address</h5>
						<p><%=custName %></p>
						<p><%=sAddr1 %>  <%=sAddr2 %></p>
						<p><%=sAddr3 %>  <%=sAddr4%></p>
						<p><%=sState%></p> 
						<p><%=sCountry%> <%=sZip%></p>
						<p><%=sHpNo%></p>
						<p><%=sWorkphone%></p>
						<p><%=sEmail%></p>
						
						
					</div>
					<div class="col-sm-3" style="line-height: 7px; font-size: 13px;">
						<h5 style="font-weight: bold;">
							Shipping Address <a><span data-toggle="modal"
								data-target="#shipaddr" onclick="getshipaddr();"  style="font-size: 15px;font-weight: 450;">Change</span></a>
						</h5>
						<div id="cshipaddr">
							
						<p><%=(String) CustcrdnoteHdr.get("SHIPCONTACTNAME") %></p>
						<p><%=(String) CustcrdnoteHdr.get("SHIPDESGINATION")%></p>
						<p><%=(String) CustcrdnoteHdr.get("SHIPADDR1") %> <%=(String) CustcrdnoteHdr.get("SHIPADDR2") %></p>
						<p><%=(String) CustcrdnoteHdr.get("SHIPADDR3") %> <%=(String) CustcrdnoteHdr.get("SHIPADDR4") %></p>
						<p><%=(String) CustcrdnoteHdr.get("SHIPSTATE") %></p> 
						<p><%=(String) CustcrdnoteHdr.get("SHIPCOUNTRY") %> <%=(String) CustcrdnoteHdr.get("SHIPZIP") %></p>
						<p><%=(String) CustcrdnoteHdr.get("SHIPHPNO") %></p>
						<p><%=(String) CustcrdnoteHdr.get("SHIPWORKPHONE") %></p>
						<p><%=(String) CustcrdnoteHdr.get("SHIPEMAIL")%></p>
						</div>
						</div>
				</div>
				<%if(compindustry.equalsIgnoreCase("Construction") || compindustry.equalsIgnoreCase("Service")){ %>	
		<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4 ac-box">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" 
									id="project" placeholder="Select a project" 
									name="project" onchange="checkproject(this.value)" value=""> 
								<span class="select-icon" style="right: 4px;"
								onclick="$(this).parent().find('input[name=\'project\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
								<!-- <span class="btn-danger input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
						</div>
					</div>
				</div>
				<%}else{%>
				<input type="hidden" name="project" value="">
				<%} %>
		<div class="form-group invoice-section">
			<label class="control-label col-form-label col-sm-2">Invoice</label>
			<div class="col-sm-4 ac-box">
			<input type="text" class="ac-selected  form-control typeahead" id="invoice" name="invoice" readonly="readonly">
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'invoice\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>	
			</div>
		</div>
		
		<div class="form-group invoice-section">
			<label class="control-label col-form-label col-sm-2">GINO</label>
			<div class="col-sm-4 ac-box">
			<input type="text" class="ac-selected  form-control typeahead" id="gino" name="gino" readonly>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Reference</label>
			<div class="col-sm-4">
				<!-- <input type="text" class="form-control" id="reference" maxlength="300" name="reference"> -->
				<input type="text" class="ac-selected form-control typeahead" maxlength="300" id="reference" name="reference"> <span
							class="select-icon"
							onclick="$(this).parent().find('input[name=\'reference\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Credit Note#</label>
			<div class="col-sm-4">
			<div class="input-group">
				<input type="text" class="form-control" id="creditnote" name="creditnote" onchange="checkorderno(this.value)" onkeypress="return blockSpecialChar(event)" value="" >
				<span class="input-group-addon"  onClick="getNextNo();">
				   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
				   		 		<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i>
				   		 	</a>
			   		 	</span>
			   		 	</div>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Credit Note Date</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" id="creditnote_date" value="<%=curDate%>" name="creditnote_date">
			</div>
											<div class="col-sm-6 no-padding">
       				<label class="control-label col-form-label col-sm-5">Payment Terms</label>
					<div class="col-sm-6 ac-box">
       				<input type="text" class="ac-selected form-control" id="payment_terms" name="payment_terms" onchange="checkpaymentterms(this.value)" value="" placeholder="Select Payment Terms">
					<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
    				</div>
		</div>
	
		<div class="form-group employee-section">
			<label class="control-label col-form-label col-sm-2">Employee</label>
			<div class="col-sm-4 ac-box">
				<input type="text" class="ac-selected form-control typeahead" id="EMP_NAME" name="EMP_NAME" onchange="checkemployeess(this.value)" placeholder="Select a employee">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		<%-- <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Sales Location</label>
			<div class="col-sm-4 ac-box">
				<SELECT class="form-control" data-toggle="dropdown" id="SALES_LOC" data-placement="right" name="SALES_LOC" onchange='OnChange(form1.SALES_LOC);' style="width: 100%">
				<%
		   MasterUtil _MasterUtil=new  MasterUtil();
		   ArrayList ccList =  _MasterUtil.getSalesLocationList("",plant,"");
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String STATE = (String)m.get("STATE");
		        String STATE_PREFIX = (String)m.get("PREFIX"); %>
		        <%if(prefixsalesloc.equalsIgnoreCase(STATE)){%>
		       	 	<option selected value= <%=STATE_PREFIX%> ><%=STATE %> </option>
		       	 <%}else{ %>
		       	  	<option  value= <%=STATE_PREFIX%> ><%=STATE %> </option>
		       	 <%} %>		          
		        <%
       			}
			 %></SELECT>
			</div>
		</div> --%>
		<%if(ispuloc){ %>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2" for="Purchase Location">Sales Location:</label>
        			<div class="col-sm-4">
			        	<input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" name="SALES_LOC" placeholder="Select a Purchase Location">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	        			
        			</div>
				</div>
				<%}%>
		        <div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Currency</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="form-control" id="CURRENCY" name="CURRENCY" placeholder="Select a Currency" onchange="checkcurrency(this.value)" value="<%=DISPLAY%>">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				
				
				
									<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-5 required" id="exchangerate" ></label>
						<div class="col-sm-6 ac-box">
							<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onkeypress="return isNumberKey(event,this,4)" required>	
						</div>
					</div>
					</div>
					
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Standard <%=taxbylabelordermanagement%> :</label><!-- this is dynamic value -->
					<div class="col-sm-4">
			   		 	<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" onkeypress="return isNumberKey(event,this,4)" value="">
						<span class="sideiconspan"><p>%</p></span>

					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Salestax" name="Salestax" onchange="checktax(this.value)" value="" placeholder="Select a Tax">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'Salestax\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
		<hr/>
		<div class="form-group">
			<div class="col-sm-12">
				<label class="control-label col-form-label">Product Rates Are</label>
				<div class="dropdown-noborder">
					<select class="ac-box dropdown-noborder form-control" onchange="renderTaxDetails()" name="item_rates" id="item_rates">
						<option value="0">Tax Exclusive</option>
						<option value="1">Tax Inclusive</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row" style="margin:0px;">
			<table class="table table-bordered line-item-table bill-table" style="width:100%;">
			<thead>
			  <tr>
				<th class="bill-desc" colspan=2>Product Details</th>
				<th class="bill-acc">Account</th>
				<th class="item-qty text-right">Quantity</th>
				<th class="item-cost text-right">Unit Price</th>
				<th class="item-discount text-center">Discount</th>
				<!-- <th class="item-tax">Tax</th> -->
				<th class="item-amount text-right">Amount</th>
			  </tr>
			</thead>
			<tbody>
			  <tr>
				<td class="item-img text-center">
				  <!-- <span class="glyphicon glyphicon-picture"></span> -->
				  <img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;"><input type="hidden" name="basecost" value="0.00">
				  <input type="hidden" name="tax_type" class="taxSearch" value="">
				  <input type="hidden" name="tax" class="taxSearch" value="">
				  <input type="hidden" name="lnno" value="1"> 
				</td>
				<td class="bill-item">
					<input type="text" name="item" style="width:90%" class="form-control itemSearch" onchange="checkitems(this.value,this)" placeholder="Type or click to select an item." onchange="calculateAmount(this)">
					<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
	                <input class="form-control"  name="ITEMDES" style="height: 23px;background-color: #fff;" readonly>			
				</td>
				<td class="bill-acc">
					<input type="text" name="account_name" class="form-control accountSearch" placeholder="Select Account">
				</td>				
				<td class="item-qty text-right"><input name="qty" type="text" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>
				<td class="item-cost text-right"><input name="cost" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>
				<td class="item-discount text-center">
				<div class="row">							
							<div class=" col-lg-12 col-sm-3 col-12">
								<div class="input-group my-group" style="width:120px;"> 
									<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
									 <%--<input type="text" class="discountPicker form-control item_discounttypeSearch" id="item_discounttype" value="<%=curency%>" name="item_discounttype" onchange="calculateAmount(this)"> --%>
									<%--<textarea rows="1" maxlength="3" style="resize: none;padding: 5px;" class="discountPicker form-control item_discounttypeSearch" name="item_discounttype" onchange="calculateAmount(this)"></textarea>--%>
									 <select name="item_discounttype" id="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">
										<%-- <option value="<%=curency%>"><%=curency%></option>
										<option value="%">%</option> --%>										
									</select>							
								</div>
							</div> 
						</div>
				</td>
				<!-- <td class="item-tax">
				<input type="hidden" name="tax_type">
					<input name="tax" type="text" class="form-control taxSearch" placeholder="Select a Tax">
				</td> -->
				<td class="item-amount text-right grey-bg">
				<input name="amount" type="text" class="form-control text-right" value="0.00" readonly="readonly" tabindex="-1">
				</td>
			  </tr>
			</tbody>
			</table>
		</div>
		<!-- Total Details -->
		<div class="row">
			<div class="col-sm-6">
            <a href="#" onclick="addRow(document.getElementById('invoice').value,event);">
			 <i class="add-line" title="Add another line" style="font-size: 15px;">+ Add another line</i>
			 </a>	
			 			</div>
			<div class="total-section col-sm-6">
				<div class="total-row sub-total">
					<div class="total-label"> Sub Total <br> 
						<span class="productRate" hidden>(Tax Inclusive)</span> 
					</div> 
					<div class="total-amount" id="subTotal">0.00</div>
				</div>	
				<div class="total-row">
							<div class="total-label">
								<div class="row">
									<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Order Discount</div>
										<sapn id="odtax">(Tax Inclusive)</sapn>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isodisctax" checked name="isodisctax" Onclick="isodisctaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right oddiscount" type="text" value="0.00" readonly
												name="orderdiscount" onchange="renderTaxDetails()" onkeypress="return isNumberKey(event,this,4)">
											<select class="discountPicker form-control" disabled="true" name="oddiscount_type" id="oddiscount_type" onchange="calculateTotal()">
												<option value="%">%</option>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="total-amount" style="line-height: 2;">
								<span id="orderdiscount"> 0.00 </span>
							</div>
						</div>					
				<div class="total-row"> 
					<div class="total-label">
						<div class="row">
							<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Discount</div>
										<sapn id="dtax">(Tax Inclusive)</sapn>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isdtax" checked name="isdtax" Onclick="isdtaxing(this)">
							</div>
							<div class=" col-lg-6 col-sm-6 col-6">
								<div class="input-group my-group"> 
									<input class="form-control text-right" type="text" id="dediscount" name="discount" onchange="renderTaxDetails()" onkeypress="return isNumberKey(event,this,4)">
									<!-- <span class="input-group-addon">%</span>	-->
									<select class="discountPicker form-control" id="discount_type" name="discount_type" onchange="renderTaxDetails()">
										<%-- <option value="<%=curency%>"><%=curency%></option>
										<option value="%">%</option> --%>										
									</select>							
								</div>
							</div> 
						</div>
					</div> 
					<div class="total-amount" style="line-height: 2;">
						<span id="discount"> 0.00 </span>
					</div> 
				</div>								
				<div class="total-row hightlight discountAccountSection" style="margin-top: 0px;" hidden>
					<div class="total-label supporting-acc-field" style="padding-bottom: 0;">
						<div class="row">
							<div class="col-lg-5 col-sm-5 col-5">
								<div class="form-control-plaintext required">Discount Account</div>
							</div> 
							<div class="col-lg-6 col-sm-6 col-6"> 
								<input class="form-control discountAccountSearch" type="text" 
	  											name="discount_account" placeholder="Select Account">
							</div>
						</div>
					</div>
				</div>
				<div class="taxDetails">
				</div>								
				<div class="total-row">
					<div class="badge-editable total-label">
						<div class="row">
							<div class="col-lg-5 col-sm-5 col-5"> 
								<input class="form-control text-right ember-view" type="text" name="adjustment_name" > 
							</div> 
							<div class="col-lg-6 col-sm-6 col-6"> 
								<input class="form-control text-right ember-view" type="text" name="adjustment" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)"> 
							</div>  
						</div>
					</div> 
					<div class="total-amount " style="line-height: 2" id="adjustment"> 0.00 </div>
				</div>
				
				<div class="total-section total-row">
							<div class="gross-total-removemargin"><!--  Author: Azees  Create date: July 16,2021  Description: Total of Local Currency-->
								<div class="total-label"><label id="lbltotal"></label></div>
								<div class="total-amount" id="total">0.00</div>
							</div>
						</div>						
						<div class="total-section total-row" id="showtotalcur">
							<div class="gross-total">
								<div class="total-label">Total (<%=curency%>)</div>
								<div class="total-amount" id="totalcur">0.00</div>
							</div>
						</div>
			</div>
		</div>
		<!-- Attach Files -->
		<div class="row grey-bg">
			<div class="col-sm-4">
				<div class="form-inline">
					<label for="email">Attach Files(s)</label>
					<div class="attch-section">
						<input type="file" class="form-control input-attch" id="billAttch" name="file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
					
							<div id="billAttchNote">
								<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
							</div>
							
			</div>
			<div class="col-sm-6 notes-sec">
				<p>Customer Notes</p>
				<div> <textarea rows="2" name="note" maxlength="1000" class="ember-text-area form-control ember-view"></textarea> </div>
			
				<p>Terms & Conditions</p>
				<div> <textarea rows="2" name="terms" maxlength="1000" class="ember-text-area form-control ember-view" placeholder="Enter the terms and conditions of your business to be displayed in your transaction"></textarea> </div>
			</div>
		</div>
		<input id="sub_total" name="sub_total" value="" hidden>
		<input id="total_amount" name="total_amount" value="" hidden>
		<input name="Submit" value="Save" hidden>
		<input name="credit_status" value="Draft" hidden>
		<input id="taxamount" name="taxamount" value="" hidden>
		<input type = "hidden" name="SHIPCONTACTNAME" value="<%=(String) CustcrdnoteHdr.get("SHIPCONTACTNAME")%>">
				<input type = "hidden" name="SHIPDESGINATION" value="<%=(String) CustcrdnoteHdr.get("SHIPDESGINATION")%>">
				<input type = "hidden" name="SHIPADDR1" value="<%=(String) CustcrdnoteHdr.get("SHIPADDR1")%>">
				<input type = "hidden" name="SHIPADDR2" value="<%=(String) CustcrdnoteHdr.get("SHIPADDR2")%>">
				<input type = "hidden" name="SHIPADDR3" value="<%=(String) CustcrdnoteHdr.get("SHIPADDR3")%>">
				<input type = "hidden" name="SHIPADDR4" value="<%=(String) CustcrdnoteHdr.get("SHIPADDR4")%>">
				<input type = "hidden" name="SHIPSTATE" value="<%=(String) CustcrdnoteHdr.get("SHIPSTATE")%>">
				<input type = "hidden" name="SHIPZIP" value="<%=(String) CustcrdnoteHdr.get("SHIPZIP")%>">
				<input type = "hidden" name="SHIPWORKPHONE" value="<%=(String) CustcrdnoteHdr.get("SHIPWORKPHONE")%>">
				<input type = "hidden" name="SHIPCOUNTRY" value="<%=(String) CustcrdnoteHdr.get("SHIPCOUNTRY")%>">
				<input type = "hidden" name="SHIPHPNO" value="<%=(String) CustcrdnoteHdr.get("SHIPHPNO")%>">
				<input type = "hidden" name="SHIPEMAIL" value="<%=(String) CustcrdnoteHdr.get("SHIPEMAIL")%>">
		<div class="row">
			<div class="col-sm-12 txn-buttons">
				<button id="btnBillDraft" type="button" class="btn btn-default">Save as Draft</button>
				<button id="btnBillOpen" type="button" class="btn btn-success">Save as Open</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			</div>
		</div>
</form>
</div>
	<!-- Modal -->
	
	<%@include file="newEmployeeModal.jsp" %>
	<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
	<%@include file="newProductModal.jsp" %>
	<%-- <%@include file="NewChartOfAccountAdd.jsp"%> --%>
	<%@include file="newGstDetailModal.jsp" %>
	<%@include file="newPaymentTermsModal.jsp" %>
	<%@include file="NewChartOfAccountpopup.jsp"%>
		<input type="text" id="PageName" style="display: none;" value=""> <!-- imti -->
		<input type="hidden" name="pronumberOfDecimal" id="pronumberOfDecimal" value="<%=numberOfDecimal%>">
	<!-- Modal -->
	<!-- <div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog">	
	    Modal content
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h3 class="modal-title">Modal Header</h3>
	      </div>
	      <div class="modal-body">
	        <p>Some text in the modal.</p>
	      </div>
	      <div class="modal-footer">
	      		<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	  </div>
	</div> -->
	
	<div id="shipaddr" class="modal fade" role="dialog">
  	<div class="modal-dialog modal-lg">
  		<div class="modal-content">
  			<div class="modal-header">
          		<button type="button" class="close" data-dismiss="modal">&times;</button>
          		<h4 class="modal-title">New Shipping Address</h4>
        	</div>
        	<div class="modal-body">

				<form class="form-horizontal" name="shipaddform">
				<div class="form-group">
					<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">Contact
						Name</label>

					<div class="col-sm-6">

						<INPUT name="SHIP_CONTACTNAME" id="SHIP_CONTACTNAME" type="TEXT"
							class="form-control" value="" size="50" MAXLENGTH="100">
					</div>
					<div class="col-sm-2"></div>
				</div>
				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">Designation</label>
					<div class="col-sm-6">

						<INPUT name="SHIP_DESGINATION" id="SHIP_DESGINATION" type="TEXT"
							class="form-control" value="" size="50" MAXLENGTH="30">
					</div>
					<div class="col-sm-2"></div>
				</div>
				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">Work
						Phone</label>
					<div class="col-sm-6">
						<INPUT name="SHIP_WORKPHONE" id="SHIP_WORKPHONE" type="TEXT"
							value="" onkeypress="return isNumber(event)" size="50"
							MAXLENGTH=50 class="form-control">
					</div>
					<div class="col-sm-2"></div>
				</div>
				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">Mobile</label>
					<div class="col-sm-6">

						<INPUT name="SHIP_HPNO" id="SHIP_HPNO" type="TEXT" value=""
							size="50" class="form-control"
							onkeypress="return isNumber(event)" MAXLENGTH="30">
					</div>
					<div class="col-sm-2"></div>
				</div>
				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">Email</label>
					<div class="col-sm-6">
						<INPUT name="SHIP_EMAIL" id="SHIP_EMAIL" type="TEXT" value=""
							size="50" MAXLENGTH="50" class="form-control">
					</div>
					<div class="col-sm-2"></div>
				</div>
				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2 required">Country</label>

					<div class="col-sm-6">
						<SELECT class="form-control" data-toggle="dropdown"
							data-placement="right" onchange="SHIP_OnCountry(this.value)"
							id="SHIP_COUNTRY_CODE" name="SHIP_COUNTRY_CODE" value=""
							style="width: 100%">
							<OPTION style="display: none;">Select Country</OPTION>
							<%
								MasterUtil _MasterUtil1 = new MasterUtil();
								ccList1=new ArrayList();
								ccList1 = _MasterUtil1.getCountryList("", plant, region);
								for (int i = 0; i < ccList1.size(); i++) {
									Map m = (Map) ccList1.get(i);
									String vCOUNTRYNAME = (String) m.get("COUNTRYNAME");
									String vCOUNTRY_CODE = (String) m.get("COUNTRY_CODE");
							%>
							<option value='<%=vCOUNTRYNAME%>'><%=vCOUNTRYNAME%>
							</option>
							<%
								}
							%>
						</SELECT>
						<%-- <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control"> --%>
					</div>
					<div class="col-sm-2"></div>
				</div>
				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">Unit
						No.</label>
					<div class="col-sm-6">

						<INPUT name="SHIP_ADDR1" id="SHIP_ADDR1" type="TEXT" value=""
							size="50" MAXLENGTH=50 class="form-control">
					</div>
					<div class="col-sm-2"></div>
				</div>
				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">Building</label>
					<div class="col-sm-6">

						<INPUT name="SHIP_ADDR2" id="SHIP_ADDR2" type="TEXT" value=""
							size="50" MAXLENGTH=50 class="form-control">
					</div>
					<div class="col-sm-2"></div>
				</div>
				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">Street</label>
					<div class="col-sm-6">

						<INPUT name="SHIP_ADDR3" id="SHIP_ADDR3" type="TEXT" value=""
							size="50" MAXLENGTH=50 class="form-control">
					</div>
					<div class="col-sm-2"></div>
				</div>
				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">City</label>
					<div class="col-sm-6">

						<INPUT name="SHIP_ADDR4" id="SHIP_ADDR4" type="TEXT" value=""
							size="50" MAXLENGTH=80 class="form-control">
					</div>
					<div class="col-sm-2"></div>
				</div>
				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">State</label>
					<div class="col-sm-6">
						<SELECT class="form-control" data-toggle="dropdown"
							data-placement="right" id="SHIP_STATE" name="SHIP_STATE" value=""
							style="width: 100%">
							<OPTION style="display: none;">Select State</OPTION>
						</SELECT>
					</div>
					<div class="col-sm-2"></div>
				</div>

				<div class="form-group">
				<div class="col-sm-2"></div>
					<label class="control-label col-form-label col-sm-2">Postal
						Code</label>
					<div class="col-sm-6">
						<INPUT name="SHIP_ZIP" id="SHIP_ZIP" type="TEXT" value=""
							size="50" MAXLENGTH=10 class="form-control">
					</div>
					<div class="col-sm-2"></div>
				</div>
				
				</form>
				
			</div>
			<div class="modal-footer">
			<div class="col-sm-4"></div>
			<div class="col-sm-4">
			  <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	          <button onClick="newshipddr();" class="btn btn-success">Save</button>
	          </div>
	          <div class="col-sm-4"></div>
	        </div>
  		</div>
  	</div>
  	<script>
  	function newshipddr(){
		$('#cshipaddr').empty();
		$("input[name=SHIPCONTACTNAME]").val($("input[name=SHIP_CONTACTNAME]").val());
		$("input[name=SHIPDESGINATION]").val($("input[name=SHIP_DESGINATION]").val());
		$("input[name=SHIPADDR1]").val($("input[name=SHIP_ADDR1]").val());
		$("input[name=SHIPADDR2]").val($("input[name=SHIP_ADDR2]").val());
		$("input[name=SHIPADDR3]").val($("input[name=SHIP_ADDR3]").val());
		$("input[name=SHIPADDR4]").val($("input[name=SHIP_ADDR4]").val());
		if($("select[name=SHIP_STATE]").val() == "Select State"){
			$("input[name=SHIPSTATE]").val("");
		}else{
			$("input[name=SHIPSTATE]").val($("select[name=SHIP_STATE]").val());
		}
		if($("select[name=SHIP_COUNTRY_CODE]").val() == "Select Country"){
			$("input[name=SHIPCOUNTRY]").val("");
		}else{
			$("input[name=SHIPCOUNTRY]").val($("select[name=SHIP_COUNTRY_CODE]").val());
		}
		
		$("input[name=SHIPZIP]").val($("input[name=SHIP_ZIP]").val());
		$("input[name=SHIPWORKPHONE]").val($("input[name=SHIP_WORKPHONE]").val());
		$("input[name=SHIPHPNO]").val($("input[name=SHIP_HPNO]").val());
		$("input[name=SHIPEMAIL]").val($("input[name=SHIP_EMAIL]").val());
	
		var addr = '<p>'+$("input[name=SHIP_CONTACTNAME]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_DESGINATION]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_ADDR1]").val()+' '+$("input[name=SHIP_ADDR2]").val()+'</p>';		
		addr += '<p>'+$("input[name=SHIP_ADDR3]").val()+' '+$("input[name=SHIP_ADDR4]").val()+'</p>';
		
		if($("select[name=SHIP_STATE]").val() == "Select State"){
			
		}else{			
			addr += '<p>'+ $("select[name=SHIP_STATE]").val()+'</p>';
		}
		if($("select[name=SHIP_COUNTRY_CODE]").val() == "Select Country"){
			addr += '<p>'+ $("input[name=SHIP_ZIP]").val()+'</p>';
			
		}else{
			addr += '<p>'+$("select[name=SHIP_COUNTRY_CODE]").val()+' '+$("input[name=SHIP_ZIP]").val()+'</p>';
		}
		addr += '<p>'+$("input[name=SHIP_HPNO]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_WORKPHONE]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_EMAIL]").val()+'</p>';
		$('#cshipaddr').append(addr);
		$('#shipaddr').modal('toggle');
	}
	
	function isNumber(evt) {	
		evt = (evt) ? evt : window.event;
		var charCode = (evt.which) ? evt.which : evt.keyCode;

		if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
			if( (charCode!=43 && charCode!=32 && charCode!=45))
				{
				
		    alert("  Please enter only Numbers.  ");
		    return false;
				}
			}
		return true;
		}
	</script>
</div>
	
	 <!-- Author: Azees  Create date: June 27,2021  Description: Customer Save Changes -->
	<!-- --------------modal---------------- -->
<div id="customerModal" class="modal fade"  name="form" role="dialog">
	<div class="modal-dialog modal-lg">
		<form class="form-horizontal" name="form" id="formCustomer" method="post">

			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">New Customer</h4>
				</div>
				<div class="modal-body">
					<input name="CUSTOMER_TYPE_DESC" type="hidden" value=""> 
					<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
					<input type="text" id="plant" name="PLANT" style="display:none;" value=<%=plant%>>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Customer
							Id</label>
						<div class="col-sm-4">

							<div class="input-group">
								<INPUT class="form-control" name="CUST_CODE_C" id="CUST_CODE_C"
									type="TEXT" value="" onchange="checkitem(this.value)" size="50"
									MAXLENGTH=50 width="50"> <span
									class="input-group-addon" onClick="onIDGen();"> <a
									href="#" data-toggle="tooltip" data-placement="top"
									title="Auto-Generate"> <i class="glyphicon glyphicon-edit"
										style="font-size: 20px;"></i></a></span>
							</div>
							<INPUT type="hidden" name="CUST_CODE1_C" value="">
							<INPUT type="hidden" name="COUNTRY" value="">
							<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG"  value="<%=region%>">
							<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">
							<INPUT type="hidden" name="CURRENCYID_C" value="<%=DISPLAYID%>">	
							

						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Customer
							Name</label>
						<div class="col-sm-4">
							<INPUT class="form-control" name="CUST_NAME_C" type="TEXT" value=""
								size="50" MAXLENGTH=100>
						</div>
					</div>
					
					
					
					<!-- 	                Author Name:Resviya ,Date:10/08/21 , Description -UEN Label     -->
          <div class="form-group" id="UEN">
	      <label class="control-label col-form-label col-sm-2 required" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
	      <div class="col-sm-4">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="cus_companyregnumber" id="cus_companyregnumber" type = "TEXT" value="<%=companyregnumber%>" size="50"  MAXLENGTH=100> 
	  	  </div>
	       </div>
	       </div>
	         <!-- End --> 
	         
					<!-- 	                Author Name:Resviya ,Date:17/07/21 -->

                      <div class="form-group">
					  <label class="control-label col-form-label col-sm-2 required">Customer
							Currency</label>
					 <div class="col-sm-4">
					  <INPUT class="form-control" name="CUS_CURRENCY"  id="CUS_CURRENCY" type="TEXT" value="<%=curency%>"  size="50" MAXLENGTH=100>
					 </div>
					 </div>
					 
<!-- 					 End -->


					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Customer
							Type</label>
						<div class="col-sm-4">
							<div class="input-group">
								<input name="CUSTOMER_TYPE_ID" type="TEXT" value="" size="50"
									MAXLENGTH=50 class="form-control"> <span
									class="input-group-addon"
									onClick="javascript:popUpWin('../jsp/customertypelistsave.jsp?CUSTOMER_TYPE_ID='+form.CUSTOMER_TYPE_ID.value);">
									<a href="#" data-toggle="tooltip" data-placement="top"
									title="Customer Type Details"> <i
										class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
								</span>
							</div>
						</div>
					</div>

					<INPUT type="text" id="TaxByLabel" name="taxbylabel"
						value="<%=taxbylabel%>" hidden>
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Customer
							Phone</label>
						<div class="col-sm-4">

							<INPUT name="TELNO" type="TEXT" value="" size="50"
								class="form-control" onkeypress="return isNumber(event)"
								MAXLENGTH="30">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Customer
							Fax</label>
						<div class="col-sm-4">

							<INPUT name="FAX" type="TEXT" value="" size="50"
								onkeypress="return isNumber(event)" MAXLENGTH="30"
								class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Customer
							Email</label>
						<div class="col-sm-4">
							<INPUT name="CUSTOMEREMAIL" type="TEXT" value="" size="50"
								MAXLENGTH=50 class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Website</label>
						<div class="col-sm-4">
							<INPUT name="WEBSITE" type="TEXT" value="" size="50" MAXLENGTH=50
								class="form-control">
						</div>
					</div>

					<div class="bs-example">
						<ul class="nav nav-tabs" id="myTab">
							<li class="nav-item active"><a href="#other"
								class="nav-link" data-toggle="tab" aria-expanded="true">Other
									Details</a></li>
							<li class="nav-item"><a href="#profile" class="nav-link"
								data-toggle="tab">Contact Details</a></li>
							<li class="nav-item"><a href="#home" class="nav-link"
								data-toggle="tab">Address</a></li>
							<li class="nav-item"><a href="#bank_cus" class="nav-link"
							 	data-toggle="tab">Bank Account Details</a></li>	
							<li class="nav-item"><a href="#remark" class="nav-link"
								data-toggle="tab">Remarks</a></li>
						</ul>
						<div class="tab-content clearfix">
							<div class="tab-pane fade" id="home">
								<br>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2 required">Country</label>
									<div class="col-sm-4">

										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE_C" name="COUNTRY_CODE_C" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
				MasterUtil _MasterUtil=new  MasterUtil();
				ArrayList ccList =  _MasterUtil.getCountryList("",plant,region);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vCOUNTRYNAME = (String)m.get("COUNTRYNAME");
				String vCOUNTRY_CODE = (String)m.get("COUNTRY_CODE"); %>
		        <option  value='<%=vCOUNTRY_CODE%>' ><%=vCOUNTRYNAME%> </option>		          
		        <%
       			}
			 %></SELECT>
									</div>
								</div>
								
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Unit
										No.</label>
									<div class="col-sm-4">

										<INPUT name="ADDR1" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Building</label>
									<div class="col-sm-4">

										<INPUT name="ADDR2" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Street</label>
									<div class="col-sm-4">

										<INPUT name="ADDR3" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">City</label>
									<div class="col-sm-4">

										<INPUT name="ADDR4" type="TEXT" value="" size="50"
											MAXLENGTH=80 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">State</label>
									<div class="col-sm-4">
										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE" name="STATE" value="" style="width: 100%">
										<OPTION style="display:none;">Select State</OPTION>
										</SELECT>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Postal
										Code</label>
									<div class="col-sm-4">

										<INPUT name="ZIP" type="TEXT" value="" size="50" MAXLENGTH=10
											class="form-control">
									</div>
								</div>

							</div>

							<div class="tab-pane fade" id="profile">
								<br>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Contact
										Name</label>
									<div class="col-sm-4">

										<INPUT name="CONTACTNAME" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="100">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Designation</label>
									<div class="col-sm-4">

										<INPUT name="DESGINATION" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Work
										Phone</label>
									<div class="col-sm-4">
										<INPUT name="WORKPHONE" type="TEXT" value=""
											onkeypress="return isNumber(event)" size="50" MAXLENGTH=50
											class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Mobile</label>
									<div class="col-sm-4">

										<INPUT name="HPNO" type="TEXT" value="" size="50"
											class="form-control" onkeypress="return isNumber(event)"
											MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Email</label>
									<div class="col-sm-4">

										<INPUT name="EMAIL" type="TEXT" value="" size="50"
											MAXLENGTH="50" class="form-control">
									</div>
								</div>


							</div>

							<div class="tab-pane active" id="other">
								<br>
								
								<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Tax Treatment</label>
			<div class="col-sm-4 ac-box">				
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnTaxChange(this.value)" id="TAXTREATMENT" name="TAXTREATMENT" style="width: 100%">
				<OPTION style="display:none;">Select Tax Treatment</OPTION>
				<%		   
		   ArrayList cvList =  _MasterUtil.getTaxTreatmentList("",plant,"");
			for(int i=0 ; i<cvList.size();i++)
      		 {
				Map m=(Map)cvList.get(i);
				String vTAXTREATMENT = (String)m.get("TAXTREATMENT"); %>
		        <option  value='<%=vTAXTREATMENT%>' ><%=vTAXTREATMENT %> </option>		          
		        <%
       			}
			 %></SELECT>
			</div>
		</div>
		
		<div class="form-group">

						<label class="control-label col-form-label col-sm-2" for="TRN No."
							id="TaxLabel"></label>
						<div class="col-sm-4">

							<INPUT name="RCBNO" type="TEXT" class="form-control" value=""
								size="50" MAXLENGTH="100">
						</div>
					</div>
								

								<div class="form-group" style="display: none;">
									<label class="control-label col-form-label col-sm-2">Opening
										Balance</label>
									<div class="col-sm-4">
										<INPUT name="OPENINGBALANCE" type="hidden" value=""
											onkeypress="return isNumberKey(event,this,4)" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Payment
										Type</label>
									<div class="col-sm-4">
										<div class="input-group">
											<INPUT class="form-control" name="PAYTERMS" type="TEXT"
												value="" size="20" MAXLENGTH=100 readonly> <span
												class="input-group-addon"
												onClick="javascript:popUpWin('../jsp/list/paymenttypelist_save.jsp?paymenttype='+form.PAYTERMS.value);">
												<a href="#" data-toggle="tooltip" data-placement="top"
												title="Payment Type"> <i
													class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
											</span>
										</div>
									</div>

									<div class="form-inline">
										<label class="control-label col-form-label col-sm-1">Days</label>
										<input name="PMENT_DAYS" type="TEXT" value="" size="5"
											MAXLENGTH=10 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Credit
										Limit</label>
									<div class="col-sm-4">
										<INPUT name="CREDITLIMIT" type="TEXT" value="" " size="50"
											MAXLENGTH=50 class="form-control"
											onkeypress="return isNumberKey(event,this,4)">
									</div>
									<!-- <div class="form-inline">
										<div class="col-sm-2">
											<label class="checkbox-inline"> <input
												type="checkbox" name="ISCREDITLIMIT" value="" />Apply
												Credit Limit
											</label>
										</div>
									</div> -->
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"></label>
									<div class="col-sm-4">
										<input type = "radio" name="CREDIT_LIMIT_BY" value="DAILY"/>By Daily
      									<input type = "radio" name="CREDIT_LIMIT_BY" value="MONTHLY"/>By Monthly
      									<input type = "radio" name="CREDIT_LIMIT_BY" value="NOLIMIT" checked/>No Credit Limit
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Facebook
										Id</label>
									<div class="col-sm-4">
										<INPUT name="FACEBOOK" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Twitter
										Id</label>
									<div class="col-sm-4">
										<INPUT name="TWITTER" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Linkedin">LinkedIn Id</label>
									<div class="col-sm-4">
										<INPUT name="LINKEDIN" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Skype">Skype Id</label>
									<div class="col-sm-4">
										<INPUT name="SKYPE" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

							</div>

<div class="tab-pane fade" id="bank_cus">
        <br>
        
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="IBAN">IBAN</label>
      	<div class="col-sm-4">  
        <INPUT name="IBAN" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
       
      <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Bank</label>
			<div class="col-sm-4 ac-box">				
				<!-- <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" value="" style="width: 100%">
				<OPTION style="display:none;">Select Bank</OPTION> -->
					<INPUT name="BANKNAME" type = "TEXT" value="" id=BANKNAMECUS class="form-control"   placeholder="BANKNAME">
	                <span class="select-icon"  onclick="$(this).parent().find('input[name=\'BANKNAME\']').focus()">
	               <i class="glyphicon glyphicon-menu-down"></i></span>
				<%-- <%
			_MasterUtil=new  MasterUtil();
		    ccList =  _MasterUtil.getBankList("",plant);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vNAME = (String)m.get("NAME"); %>
		        <option  value='<%=vNAME%>' ><%=vNAME %> </option>		          
		        <%
       			}
			 %></SELECT> --%>
			</div>
</div>

		
		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Branch">Branch</label>
      	<div class="col-sm-4">  
        <INPUT name="BRANCH" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control" readonly>
      	</div>
    	</div>
        
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Routing Code">Routing Code</label>
      	<div class="col-sm-4">  
        <INPUT name="BANKROUTINGCODE" type="TEXT" value=""	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
        
        </div>


							<div class="tab-pane fade" id="remark">
								<br>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Remarks">Remarks</label>
									<div class="col-sm-4">

										<INPUT name="REMARKS" type="TEXT" value="" size="50"
											MAXLENGTH=100 class="form-control ">
									</div>
								</div>

							</div>
						</div>
					</div>




					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-8">

							&nbsp;&nbsp;
							<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
							<!-- <button type="button" class="Submit btn btn-default"
								onClick="onNew();">Clear</button> -->
							&nbsp;&nbsp;
							<button type="button" class="btn btn-success"
								data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>
							&nbsp;&nbsp;
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
	
	
<script>

$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip(); 

    var  curr = document.form1.CURRENCYID.value;		    
	    var basecurrency = '<%=curency%>';  <%--    resvi --%>
	    document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";//Resvi
	    document.getElementById('lbltotal').innerHTML = "Total ("+curr+")";//  Author: Azees  Create date: July 16,2021  Description:  Total of Local Currency
	    if(basecurrency!=curr)//  Author: Azees  Create date: July 17,2021  Description:  Total of Local Currency
	    	document.getElementById('showtotalcur').style.display = 'block';	
	    else
	    	document.getElementById('showtotalcur').style.display = 'none';
    	
	$('#discount_type').append('<option value="<%=curency%>"><%=curency%></option>');
	$('#discount_type').append('<option value="%">%</option>');
	
	$('#discount_type').empty();
	$('#discount_type').append('<option value="<%=curency%>"><%=curency%></option>');
	$('#discount_type').append('<option value="%">%</option>');


	<!-- 	                Author Name:Resviya ,Date:10/08/21 , Description -UEN (GCC/ASIAPACIFIC)     -->

	var region = '<%=region%>'; 
	if(region == "GCC"){
		var x = document.getElementById("UEN");
		x.style.display = "none";
	}else if(region == "ASIA PACIFIC"){
		var x = document.getElementById("UEN");
		x.style.display = "block";
	}
	<!-- End -->
	
	
	var TAXTREATMENT_VALUE = $("input[name ='TAXTREATMENT_VALUE']").val();
	if(TAXTREATMENT_VALUE!="")
		document.getElementById('nTAXTREATMENT').value = TAXTREATMENT_VALUE;
	
    var d = "<%=taxbylabel%>";
    document.getElementById('TaxLabel').innerHTML = d +" No.";
    var cust = "<%=sCustCode%>";
    var cust2 = "<%=custCode%>";
    if(cust=="" || cust.length==0 ) {
    }else{
    	
    	$("input[name ='CUST_CODE']").val(cust);
    	if(cust!=cust2)
    		{
    	 	$('#customerModal').modal('show');
    		}
    }
    
});
function checkitem(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Customer ID!");
		document.getElementById("CUST_CODE").focus();
		return false;
	 }else{ 
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CUST_CODE : aCustCode,
                USERID : "<%=username%>",
				PLANT : "<%=plant%>",
					ACTION : "CUSTOMER_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {

						alert("Customer ID Already Exists");
						document.getElementById("CUST_CODE").focus();
						//document.getElementById("ITEM").value="";
						return false;
					} else
						return true;
				}
			});
			return true;
		}
	}
	function isNumberKey(evt, element, id) {
		  var charCode = (evt.which) ? evt.which : event.keyCode;
		  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
			  {
		    	return false;
			  }
		  return true;
		}
	
	function isNumber(evt) {	
	  evt = (evt) ? evt : window.event;
	  var charCode = (evt.which) ? evt.which : evt.keyCode;
	  
	  if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
	  	if( (charCode!=43 && charCode!=32 && charCode!=45))
	  		{
	  		
	      alert("  Please enter only Numbers.  ");
	      return false;
	  		}
	  	}
	  return true;
	}
	function onAdd(){
		var CUST_CODE   = document.form.CUST_CODE_C.value;
		 var CUST_NAME   = document.form.CUST_NAME_C.value;
		 var cus_companyregnumber   = document.form.cus_companyregnumber.value;
		 var CL   = document.form.CREDITLIMIT.value;
		 var TAXTREATMENT   = document.form.TAXTREATMENT.value;
		   var RCBNO   = document.form.RCBNO.value;
		   var rcbn = RCBNO.length;
			var CURRENCY = document.form.CUS_CURRENCY.value;
			   var region = document.form.COUNTRY_REG.value;
			    
		   var ValidNumber   = document.form.ValidNumber.value;
		   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" customers you can create"); return false; }
		   
		 if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE_C.focus(); return false; }
		 if(CUST_NAME == "" || CUST_NAME == null) {
			 alert("Please Enter Customer Name"); 
			 document.form.CUST_NAME_C.focus();
			 return false; 
			 }

	//   Author Name:Resviya ,Date:10/08/21 , Description -UEN Alert    

		   if(region == "GCC"){
			   document.form.cus_companyregnumber.value="";
			}else if(region == "ASIA PACIFIC"){
				if (cus_companyregnumber == "" || cus_companyregnumber == null) {
				alert("Please Enter Unique Entity Number (UEN)");
				document.form.cus_companyregnumber.focus();
				return false; 
				}
			}

//		   END

		//Author Name:Resviya ,Date:17/07/21

		 if(CURRENCY == "" || CURRENCY == null) {
			 alert("Please Enter Currency ID"); 
			 document.form.CUS_CURRENCY.focus();
			 return false; 
			 }
	      //	END
		 
		 if(document.form.TAXTREATMENT.selectedIndex==0)
			{
			alert("Please Select TAXTREATMENT");
			document.form.TAXTREATMENT.focus();
			return false;
			}
		 if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
		   {
		   var  d = document.getElementById("TaxByLabel").value;
		   	if(RCBNO == "" || RCBNO == null) {
		   		
			   alert("Please Enter "+d+" No."); 
			   document.form.RCBNO.focus();
			   return false; 
			   }
		   	//if(document.form1.COUNTRY_REG.value=="GCC")// region based validtion
			//{
			if(!IsNumeric(RCBNO))
			{
		    alert(" Please Enter "+d+" No. Input In Number"); 
		   	document.form.RCBNO.focus();
		   	return false; 
		  	}

			if("15"!=rcbn)
			{
			alert(" Please Enter your 15 digit numeric "+d+" No."); 
				document.form.RCBNO.focus();
				return false; 
				}
			//}
		   }
		else if(50<rcbn)
		{
		   var  d = document.getElementById("TaxByLabel").value;
		   alert(" "+d+" No. length has exceeded the maximum value"); 
		   document.form.RCBNO.focus();
		   return false; 
		 }

		 if(CL < 0) 
		 {
			   alert("Credit limit cannot be less than zero");
			   document.form.CREDITLIMIT.focus(); 
			   return false; 
			   }	 
		// alert(isCL);
		
		 //alert("2nd");
		 if(!IsNumeric(CL))
		 {
		   alert(" Please Enter Credit Limit Input In Number");
		   form.CREDITLIMIT.focus();  
		   form.CREDITLIMIT.select(); 
		   return false;
		 }
		 if(!IsNumeric(form.PMENT_DAYS.value))
		 {
		   alert(" Please Enter Days Input In Number");
		   form.PMENT_DAYS.focus();  form.PMENT_DAYS.select(); return false;
		 }
		 
		 
		//  alert(CL);
		 /* if(this.form.ISCREDITLIMIT.checked == true && CL == ""){
			   alert("Please Enter Credit Limit");
			   document.form.CREDITLIMIT.focus();
			   return false; 
		 } */
		 if(document.form.COUNTRY_CODE_C.selectedIndex==0)
			{
			   alert("Please Select Country from Address");
			   document.form.COUNTRY_CODE_C.focus();
			 return false;
			}
	 <%-- var idd = "<%=custcrdnoteHDRid%>";
	 document.form.action  = "/track/CreateCustomerServlet?action=ADD&reurl=CustomerCreditNoteEdit.jsp?custcreditid="+idd;
	 document.form.submit(); --%>
	 var datasend = $('#formCustomer').serialize();
	   
	  
	   var urlStr = "/track/CreateCustomerServlet?action=JADD&reurl=CustomerCreditNoteCopy";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : datasend,
		dataType : "json",
		success : function(data) {
			if(document.form1.custModal.value == "cust"){
			//alert(JSON.stringify(data));
			$("input[name ='CUST_CODE']").val(data.customer[0].CID);
			//document.getElementById("CUST_CODE").value  = data.customer[0].CID;
			document.getElementById("CUSTOMER").value  = data.customer[0].CName;
			$("input[name ='CUSTOMER']").val(data.customer[0].CName);
			$("input[name ='TAXTREATMENT_VALUE']").val(data.customer[0].CTAXTREATMENT);
			document.getElementById('nTAXTREATMENT').value = data.customer[0].CTAXTREATMENT;
			document.form.reset();
			$('#customerModal').modal('hide');
			}else{
				document.form.reset();
				document.form1.SHIPPINGCUSTOMER.value = data.customer[0].CName;
				document.form1.SHIPPINGID.value = data.customer[0].CID;
				$('#customerModal').modal('hide');
			}
		}
		});
	}
	
	function onIDGen()
	{
		<%-- var idd = "<%=custcrdnoteHDRid%>";
	 document.form.action  = "/track/CreateCustomerServlet?action=Auto-ID&reurl=CustomerCreditNoteEdit.jsp?custcreditid="+idd;
	 document.form.submit(); --%>
	 var urlStr = "/track/CreateCustomerServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : "<%=plant%>",
			action : "JAuto-ID",
			reurl : "CustomerCreditNoteCopy"
		},
		dataType : "json",
		success : function(data) {
			
			$("input[name ='CUST_CODE_C']").val(data.customer[0].CID);
			$("input[name ='CUST_CODE1_C']").val(data.customer[0].CID);
			
		}
		});
	}


function create_account() {
	
	if ($('#create_account_modal #acc_type').val() == "") {
		alert("please fill account type");
		$('#create_account_modal #acc_type').focus();
		return false;
	}
	
	if ($('#create_account_modal #acc_det_type').val() == "") {
		alert("please fill account detail type");
		$('#create_account_modal #acc_det_type').focus();
		return false;
	}
	
	if ($('#create_account_modal #acc_name').val() == "") {
		alert("please fill account name");
		$('#create_account_modal #acc_name').focus();
		return false;
	}
	
	if(document.create_form.acc_is_sub.checked)
	{
		if ($('#create_account_modal #acc_subAcct').val() == "") {
			alert("please fill sub account");
			$('#create_account_modal #acc_subAcct').focus();
			return false;
		}
		else
			{
			 var parType=$('#create_account_modal #acc_det_type').val();
			 subType=subType.trim();
			 var n = parType.localeCompare(subType);
			    if(n!=0)
			    	{
			    	$(".alert").show();
			    	$('.alert').html("For subaccounts, you must select the same account and extended type as their parent.");
			    	/* setTimeout(function() {
			            $(".alert").alert('close');
			        }, 5000); */
			    	 return false;
			    	}
			}
	}
	if ($('#create_account_modal #acc_balance').val() != "") {
		if ($('#create_account_modal #acc_balance').val() != "0") {
		if ($('#create_account_modal #acc_balanceDate').val() == "") {
		alert("please fill date");
		$('#create_account_modal #acc_balanceDate').focus();
		return false;
		}
		}
	}
	
	var formData = $('form[name="create_form"]').serialize();
	$.ajax({
		type : 'post',
		url : "/track/ChartOfAccountServlet?action=create",
		dataType : 'json',
		data : formData,//{key:val}
		success : function(data) {
			if (data.STATUS == "FAIL") {		                               
				alert(data.MESSAGE);
			}else{
				$("input[name ='discount_account']").val(data.ACCOUNT_NAME);
				$('#create_account_modal').modal('toggle');
				
			}
		},
		error : function(data) {

			alert(data.responseText);
		}
	});
	return false;
}


function create_accountcoa() {
	
	if ($('#create_account_modalcoa #acc_type').val() == "") {
		alert("please fill account type");
		$('#create_account_modalcoa #acc_type').focus();
		return false;
	}
	
	if ($('#create_account_modalcoa #acc_det_type').val() == "") {
		alert("please fill account detail type");
		$('#create_account_modalcoa #acc_det_type').focus();
		return false;
	}
	
	if ($('#create_account_modalcoa #acc_name').val() == "") {
		alert("please fill account name");
		$('#create_account_modalcoa #acc_name').focus();
		return false;
	}
	
	if(document.create_formcoa.acc_is_sub.checked)
	{
		if ($('#create_account_modalcoa #acc_subAcct').val() == "") {
			alert("please fill sub account");
			$('#create_account_modalcoa #acc_subAcct').focus();
			return false;
		}
		else
			{
			var parType=$('#create_account_modalcoa #acc_det_type').val();
			 subType=subType.trim();
			 var n = parType.localeCompare(subType);
			    if(n!=0)
			    	{
			    	$(".alert").show();
			    	$('.alert').html("For subaccounts, you must select the same account and extended type as their parent.");
			    	/* setTimeout(function() {
			            $(".alert").alert('close');
			        }, 5000); */
			    	 return false;
			    	}
			}
	}
	/* if ($('#create_account_modalcoa #acc_balance').val() != "") {
		if ($('#create_account_modalcoa #acc_balance').val() != "0") {
		if ($('#create_account_modalcoa #acc_balanceDate').val() == "") {
		alert("please fill date");
		$('#create_account_modalcoa #acc_balanceDate').focus();
		return false;
		}
		}
	} */
	
	var formData = $('form[name="create_formcoa"]').serialize();
	$.ajax({
		type : 'post',
		url : "/track/ChartOfAccountServlet?action=create",
		dataType : 'json',
		data : formData,//{key:val}
		success : function(data) {
			if (data.STATUS == "FAIL") {		                               
				alert(data.MESSAGE);
			}else{
				var ukey = document.create_formcoa.ukey.value;
				ukey = "#"+ukey;
				$(ukey).parents("tr").find('input[name="account_name"]').val(data.ACCOUNT_NAME); 
				$('#create_account_modalcoa').modal('toggle');
			}
		},
		error : function(data) {

			alert(data.responseText);
		}
	});
	return false;
}
	
$(".bill-table tbody").on('click','.accrmv',function(){
	debugger;	    
    var obj = $(this);
    var timestamp = new Date().getUTCMilliseconds();
    kayid = "key"+timestamp;
    $(obj).closest('td').attr('id', kayid); 
    $("input[name ='ukey']").val(kayid);
});

function OnTaxChange(TAXTREATMENT)
{
	
	if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	{
		$("#TaxLabel").addClass("required");
	}
	else
		$("#TaxLabel").removeClass("required");
	}
function OnBank(BankName)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_BANK_DATA",
			PLANT : "<%=plant%>",
			NAME : BankName,
		},
		success : function(dataitem) {
			var BankList=dataitem.BANKMST;
			var myJSON = JSON.stringify(BankList);						
			var dt = JSON.stringify(BankList).replace('[', '').replace(']', '');
			if (dt) {
			  var result = jQuery.parseJSON(dt);			  
			  var val = result.BRANCH;			  
			  $("input[name ='BRANCH']").val(val);
			}
		}
	});		
}
function OnCountry(Country)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_STATE_DATA",
			PLANT : "<%=plant%>",
			COUNTRY : Country,
		},
		success : function(dataitem) {
			var StateList=dataitem.STATEMST;
			var myJSON = JSON.stringify(StateList);
			//alert(myJSON);
			$('#STATE').empty();
	//		$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
			$('#STATE').append('<OPTION>Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					   $('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
		}
	});	
	
}
function bankCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#BANKNAMECUS").typeahead('val', data.BANK_NAME);
		  $("input[name ='BRANCH']").val(data.BANK_BRANCH);
		
	}
}
$(document).ready(function(){

/* Bank Name Auto Suggestion */
$('#BANKNAMECUS').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'NAME',  
	  source: function (query, process,asyncProcess) {
		  var urlStr = "../BankServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT :"<%=plant%>",
					action : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
					NAME : query
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
	    	  return '<p onclick="document.form.BRANCH.value = \''+data.BRANCH_NAME+'\'">'+data.NAME+'</p>';
		}
	  }

	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
		menuElement.next().width(menuElement.width());
		menuElement.next().css({ "top": top,"padding":"3px 20px" });
		if($(this).parent().find(".tt-menu").css('display') != "block")
			menuElement.next().hide();
		
	}).on('typeahead:open',function(event,selection){
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);
		$('.employeeAddBtn').show();
	}).on('typeahead:close',function(){
		setTimeout(function(){ $('.employeeAddBtn').hide();}, 150);	
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
	});
/* To get the suggestion data for Currency */
$("#CUS_CURRENCY").typeahead({
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
		return '<div onclick="document.form.CURRENCYID_C.value = \''+data.CURRENCYID+'\'"> <p>'+data.CURRENCY+'</p></div>';

		}
	  }
}).on('typeahead:render',function(event,selection){
	  
}).on('typeahead:open',function(event,selection){
	
}).on('typeahead:close',function(){

}).on('typeahead:change',function(event,selection){
	if($(this).val() == "")
		$("input[name ='CURRENCYID_C']").val("");	
});

});

$('select[name="COUNTRY_CODE_C"]').on('change', function(){		
    var text = $("#COUNTRY_CODE_C option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
});

function SHIP_OnCountry(Country)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_STATE_DATA_POPUP",
			PLANT : "<%=plant%>",
			COUNTRY : Country,
		},
		success : function(dataitem) {
			var StateList=dataitem.STATEMST;
			var myJSON = JSON.stringify(StateList);
			//alert(myJSON);
			$('#SHIP_STATE').empty();
		//	$('#SHIP_STATE').append('<OPTION style="display:none;">Select State</OPTION>');
			$('#SHIP_STATE').append('<OPTION>Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					 if(document.form.STATE.value==value.text)
						 	$('#SHIP_STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
					 else if(document.form1.SHIPSTATE.value==value.text)
						 	$('#SHIP_STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
					 else
					   $('#SHIP_STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
		}
	});	
	
}

</script>

</div>
</div>

<%@include file="../jsp/newBankModal.jsp"%>
<jsp:include page="newProductDepTypeModal.jsp" flush="true"></jsp:include> <!-- Resvi for add product department -->
<jsp:include page="newProductCategoryModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product category -->
<jsp:include page="newProductSubCatgoryModal.jsp" flush="true"></jsp:include><!-- Thanzith for add product Sub Category -->
<jsp:include page="newProductBrandModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product Brand -->
<jsp:include page="newProductHsCodeModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product HsCode -->
<jsp:include page="newProductCOOModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product COO -->
<jsp:include page="newProductUOMmodal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product UOM -->
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>