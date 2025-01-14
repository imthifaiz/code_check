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
	String title = "New Invoice";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String region = StrUtils.fString((String) session.getAttribute("REGION"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String compindustry = plantMstDAO.getcompindustry(plant);
	DateUtils _dateUtils = new DateUtils();
	String curDate = DateUtils.getDate();
	String cmd = StrUtils.fString(request.getParameter("cmd"));
	String sTranId = StrUtils.fString(request.getParameter("TRANID")).trim();
	String fieldDesc=StrUtils.fString(request.getParameter("result"));
	String taxbylabel = ub.getTaxByLable(plant);
	String CURRENCYUSEQT="0",DISPLAY="";
	String custCode = StrUtils.fString(request.getParameter("CUST_CODE"));
	String CUSTOMER = StrUtils.fString(request.getParameter("CUST_NAME"));
	String sCustCode = StrUtils.fString(request.getParameter("sCustCode"));
	String sTAXTREATMENT = StrUtils.fString(request.getParameter("TAXTREATMENT"));
	String companyregnumber=StrUtils.fString(request.getParameter("cus_companyregnumber"));
	
	if(sCustCode.equalsIgnoreCase(""))
	{
		sCustCode = custCode;
	}
	
	String btnname = "Save as Open";
	 	String transportmode = "";
	
	InvoiceUtil invoiceUtil = new InvoiceUtil();
	Hashtable ht = new Hashtable();
	ht.put("ID", sTranId);
	ht.put("PLANT", plant);
	List invoiceHdrList =  invoiceUtil.getInvoiceHdrById(ht);
	if(invoiceHdrList.size()>0)
	{
		Map invoiceHdr=(Map)invoiceHdrList.get(0);
		String status= (String)invoiceHdr.get("BILL_STATUS");
		if(!status.equalsIgnoreCase("DRAFT"))
			btnname="Save";
		
		TransportModeDAO transportmodedao = new TransportModeDAO();
	 	int transportid = Integer.valueOf((String)invoiceHdr.get("TRANSPORTID"));
		if(transportid > 0){
			transportmode = transportmodedao.getTransportModeById(plant,transportid);
		}else{
			transportmode = "";
		}
		
	}
	String Urlred="../expenses/detail?TRANID="+sTranId;
	if(cmd.equalsIgnoreCase("Edit")){
		title = "Edit Invoice";
		Urlred="../invoice/detail?INVOICE_HDR="+sTranId;
	}
	if(cmd.equalsIgnoreCase("insert")){
 
	}
	
	String DISPLAYID="";
	List curQryList=new ArrayList();
	CurrencyDAO currencyDAO = new CurrencyDAO();
	curQryList = currencyDAO.getCurrencyDetails(curency,plant);
	for(int i =0; i<curQryList.size(); i++) {
		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
		DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
		curency = DISPLAYID;
		DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
        CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
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
	 
	 String gst = new selectBean().getGST("SALES",plant);
	
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.EXPENSES%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/Expenseinvoice.js"></script>
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
.table-icon{
 	text-align: center;
 }
 .table-icon i{
    vertical-align: middle;
 }
 #remarks-table>tbody>tr>td{
 	padding: 8px;
 }
  .remark-action {
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -5%;
    top: 15px;
}
 </style>
<center>
	<h2>
		<small class="error-msg"><%=fieldDesc%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../expenses/summary"><span class="underline-on-hover">Expenses Summary</span> </a></li>
                <li><a href="../expenses/detail?TRANID=<%=sTranId%>"><span class="underline-on-hover">Expense Detail</span> </a></li>
                <li><label>New Invoice</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='<%=Urlred%>'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		<div class="container-fluid">
			<input type="number" id="numberOfDecimal" style="display: none;"
				value=<%=numberOfDecimal%>>
			<input type="number" id="pronumberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
			<form id="createBillForm" class="form-horizontal" name="form1"
				action="/track/InvoiceServlet?Submit=Save" method="post"
				enctype="multipart/form-data" onsubmit="return validateInvoice()">
				
				<input type = "hidden" name="PROJECTID" value="0">
			    <input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
			    <input type="hidden" name="CURRENCYUSEQTOLD" value="1">
				<input type = "hidden" name="ptaxtype" value="">
				<input type = "hidden" name="ptaxpercentage" value="0">
				<input type = "hidden" name="ptaxdisplay" value="">
				<input type = "hidden" name="ptaxiszero" value="1">
				<input type = "hidden" name="ptaxisshow" value="0">
				<input type = "hidden" name="taxid" value="0">
				<%-- <input type = "hidden" name="GST" value="<%=gst%>"> --%>
				<input type = "hidden" name="custModal">
				<input type = "hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>">
				<INPUT type="hidden" name="CURRENCYID"  value="<%=DISPLAYID%>">
				<INPUT type="hidden" name="curency"  value="<%=DISPLAYID%>">	
			    <INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%> 
				<%if(!ispuloc){ %>
				 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
				<%} %>
				
				<div class="form-group customer-section">
					<label class="control-label col-form-label col-sm-2 required">Customer
						Name</label>
					<div class="col-sm-6 ac-box">
						<div class="input-group">
							<input type="text" class="ac-selected  form-control typeahead"
								id="CUSTOMER" placeholder="Select a customer" name="CUSTOMER" id="CUSTOMER" disabled="disabled"
								value="<%=CUSTOMER%>"> <span class="select-icon"
								style="right: 45px;"
								onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><!-- <i
								class="glyphicon glyphicon-menu-down"></i> --></span> <span
								class="btn-danger input-group-addon" id="btnpop" 
								onclick="javascript:popUpWin('../jsp/customer_list_issue_summary.jsp?TYPE=ACCTCUST&CUST_NAME='+form1.CUSTOMER.value);"><span
								class="glyphicon glyphicon-search" aria-hidden="true"></span></span>
						</div>
						<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" id="nTAXTREATMENT" name="nTAXTREATMENT" value="" style="width: 100%">
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
						<input type="hidden" name="plant" value="<%=plant%>" >
						<input type="hidden" name="username" value=<%=username%> >
						<INPUT type="hidden" name="CUST_CODE" value="<%=custCode%>">
						<INPUT type="hidden" name="isexpense" value="1">
						<INPUT type="hidden" name="EMPNO" value=""> <INPUT
							type="hidden" name="cmd" value="<%=cmd%>" /> <INPUT
							type="hidden" name="TranId" value="<%=sTranId%>" />
							<INPUT type="hidden" name="STATE_PREFIX" value="" />
							<INPUT type = "hidden" name="TAXTREATMENT_VALUE" value ="<%=sTAXTREATMENT%>">
							<input type = "hidden" name="CUST_CODE2" value="<%=sCustCode%>">
							<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
							<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
							<input type="hidden" name="TRANSPORTID" value="">
							<INPUT type="hidden" name="CURRENCYAMT"  value="<%=CURRENCYUSEQT%>">
					</div>
				</div>
				
				<div class="form-group" style="display: none">
					<label class="control-label col-form-label col-sm-2">Transport Mode:</label>
					<div class="col-sm-6 ac-box">
								<input type="hidden" class="form-control typeahead"
										id="transport" placeholder="Select a transport" name="transport" value="<%=transportmode%>">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
					</div>
				</div>
				<%if(compindustry.equalsIgnoreCase("Construction") || compindustry.equalsIgnoreCase("Service")){ %>					
				<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-6 ac-box">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" 
									id="project" placeholder="Select a project" 
									name="project"> 
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
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Invoice#</label>
					<div class="col-sm-4">
					<div class="input-group">
						<input type="text" class="form-control" id="invoice" name="invoice" onchange="checkorderno(this.value)" onkeypress="return blockSpecialCharOrderNo(event)">
						<span class="input-group-addon"  onClick="newinvno()">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i></a></span>
					</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">GINO#</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="gino" name="gino" value="" disabled>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Order
						Number</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control" id="ORDERNO"
							name="ORDERNO" value="" disabled> <span
							class="select-icon"
							onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Invoice
						Date</label>
					<div class="col-sm-4">
						<input type="text" class="form-control datepicker"
							id="invoice_date" name="invoice_date" value="<%=curDate%>">
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Due
						Date</label>
					<div class="col-sm-4">
						<input type="text" class="form-control datepicker" id="due_date" placeholder="Select due date" name="due_date">
					</div>
						<div class="col-sm-6 no-padding">
					<label class="control-label col-form-label col-sm-5">Payment
						Terms</label>
					<div class="col-sm-6 ac-box">
						<input type="text" class="ac-selected form-control" placeholder="Select Payment Terms" onchange="checkpaymentterms(this.value)"
							id="payment_terms" name="payment_terms"> <span
							class="select-icon"
							onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				</div>
				<div class="form-group employee-section">
					<label class="control-label col-form-label col-sm-2">Employee</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control typeahead"
							id="EMP_NAME" name="EMP_NAME" onchange="checkemployeess(this.value)" placeholder="Select a employee">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				<%-- <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Sales Location</label>
			<div class="col-sm-4 ac-box">
				<!-- <input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" name="SALES_LOC" placeholder="Select a Sales Location">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="SALES_LOC" name="SALES_LOC" onchange='OnChange(form1.SALES_LOC);' style="width: 100%">
				<%
		   MasterUtil _MasterUtil=new  MasterUtil();
		   ArrayList ccList =  _MasterUtil.getSalesLocationList("",plant,"");
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String STATE = (String)m.get("STATE");
		        String STATE_PREFIX = (String)m.get("PREFIX"); %>
		        <option  value= <%=STATE_PREFIX%> ><%=STATE %> </option>		          
		        <%
       			}
			 %></SELECT>
			</div>
		</div> --%>
		<%if(ispuloc){ %>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2" for="Purchase Location">Sales Location:</label>
        			<div class="col-sm-4">
			        	<input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" name="SALES_LOC" value="<%=plantstate%>" placeholder="Select a Purchase Location">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	        			
        			</div>
				</div>
				<%}%>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Currency</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control typeahead" id="currency" name="currency" placeholder="Select a Currency" onchange="checkcurrency(this.value)" value="<%=DISPLAY%>">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'currency\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				
				<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-5 required" id="exchangerate" ></label>
						<div class="col-sm-6 ac-box">
							<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" required>	
						</div>
					</div>
					</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Standard <%=taxbylabelordermanagement%> :</label><!-- this is dynamic value -->
					<div class="col-sm-4">
			   		 	<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" value="<%=gst%>">
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
				<hr />
				<div class="form-group">
					<div class="col-sm-12">
						<label class="control-label col-form-label">Product Rates
							Are</label>
						<div class="dropdown-noborder">
							<select class="ac-box dropdown-noborder form-control"
								onchange="calculateTotal()" name="item_rates" id="item_rates">
								<option value="0">Tax Exclusive</option>
								<option value="1">Tax Inclusive</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row" style="margin: 0px;">
					<table class="table table-bordered line-item-table bill-table"
						style="width: 100%;">
						<thead>
							<tr>
								<th class="bill-desc" colspan=2 style="width: 35%;">Product Details</th>
								<th>Account</th>
								<th class="item-qty text-right">Quantity</th>
								<th class="item-cost text-right">Unit Price</th>
								<th class="item-discount text-center">Discount</th>
								<!-- <th class="item-tax">Tax</th> -->
								<th class="item-amount text-center"colspan=2 style="width:13%">Amount</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="item-img text-center">
									<!-- <span class="glyphicon glyphicon-picture"></span> --> <img
									alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;"> <input
									type="hidden" name="lnno" value="1">
									<input type="hidden" name="IS_COGS_SET" value="N">
									<input type='hidden' name='tax_type'>
									<input type='hidden' name='tax' class='form-control taxSearch'>
								</td>
								<td class="bill-item"><input type="text" name="item"
									class="form-control itemSearch"
									placeholder="Type or click to select an item."
									onchange="calculateAmount(this)"></td>
								<td class="item-qty text-right"><input name="qty"
									type="text" class="form-control text-right" value="1.000"
									onchange="calculateAmount(this)"></td>
								<td class="item-cost text-right"><input name="cost"
									type="text" class="form-control text-right" value="0.00"
									onchange="calculateAmount(this)"></td>
								<!-- <td class="item-tax"><input type="hidden" name="tax_type">
									<input name="tax" type="text" class="form-control taxSearch"
									placeholder="Select a Tax"></td> -->
								<td class="item-amount text-right grey-bg"><input
									name="amount" type="text" class="form-control text-right"
									value="0.00" readonly="readonly"></td>
								<td class="table-icon">
								<a href="#" onclick="showRemarksDetails(this)">
									<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>
								</a></td>
							</tr>
						</tbody>
					</table>
				</div>
				<!-- Total Details -->
				<div class="row">
					<div class="col-sm-6">
						
					</div>
					<div class="total-section col-sm-6">
						<div class="total-row sub-total">
							<div class="total-label">
								Sub Total <br> <span class="productRate" hidden>(Tax
									Inclusive)</span>
							</div>
							<div class="total-amount" id="subTotal">0.00</div>
						</div>
						<div class="total-row" style="display: none;">
							<div class="badge-editable total-label">
								<div class="row">
									<div class="col-lg-5 col-sm-5 col-5">
										<div class="form-control-plaintext">Shipping Charge</div>
									</div>
									<div class="col-lg-6 col-sm-6 col-6">
										<input class="form-control text-right ember-view" type="text"
											name="shippingcost" value="0.0">
									</div>
								</div>
							</div>
							<div class="total-amount deshipping" id="shipping" name="shipping">0.00</div>
					</div>
						<div class="taxDetails"></div>
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
								<input type="file" class="form-control input-attch"
									id="billAttch" name="file" multiple="true">
								<div class="input-group">
									<svg version="1.1" id="Layer_1"
										xmlns="http://www.w3.org/2000/svg" x="0" y="0"
										viewBox="0 0 512 512" xml:space="preserve"
										class="icon icon-xs align-text-top action-icons input-group-addon"
										style="height: 30px; display: inline-block; color: #c63616;">
										<path
											d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
									<button type="button" class="btn btn-sm btn-attch">Upload
										File</button>
								</div>

							</div>
						</div>
						<div id="billAttchNote">
							<small class="text-muted"> You can upload a maximum of 5
								files, 2MB each </small>
						</div>
					</div>
					<div class="col-sm-6 notes-sec">
						<p>Customer Notes</p>
						<div>
							<textarea rows="2" name="note" maxlength="990"
								class="ember-text-area form-control ember-view"></textarea>
						</div>

						<p>Terms &amp; Conditions</p>
						<div>
							<textarea rows="2" name="terms" maxlength="990"
								class="ember-text-area form-control ember-view"
								placeholder="Enter the terms and conditions of your business to be displayed in your transaction"></textarea>
						</div>
					</div>
				</div>
				<input id="sub_total" name="sub_total" value="" type="hidden"> <input
					id="total_amount" name="total_amount" value="" type="hidden"> <input
					name="Submit" value="Save" type="hidden"> <input
					name="invoice_status" value="Save" type="hidden">
					<input id="taxamount" name="taxamount" value="" type="hidden">
				<div class="row">
					<div class="col-sm-12 txn-buttons">
					<%if(!cmd.equalsIgnoreCase("Edit")) {%>
						<!-- <button id="btnBillDraft" type="button" class="btn btn-default">Save
							as Draft</button> -->
							<div class="dropup">
						<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
					    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Save as Open
					    <span class="caret"></span></button>
					    <ul class="dropdown-menu">
					      <li><a id="btnBillOpen" href="#">Save as Open</a></li>
					      <li><a id="btnBillDraft" href="#">Save as Draft</a></li>
					      <li><a id="btnBillOpenEmail" href="#">Save as Open and Send Email</a></li>
					    </ul>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					  </div>
						<%}else{ %>
						<%-- <button id="btnBillOpen" type="button" class="btn btn-success"><%=btnname %></button>
						<button type="button" class="btn btn-default"
							onclick="window.location.href='../home'">Cancel</button> --%>
							 <div class="dropup">
						<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
					    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Save as Open
					    <span class="caret"></span></button>
					    <ul class="dropdown-menu">
					      <li><a id="btnBillOpen" href="#">Save as Open</a></li>
					      <li><a id="btnBillOpenEmail" href="#">Save as Open and Send Email</a></li>
					    </ul>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					  </div>
						<%} %>
					</div>
				</div>
			</form>
		</div>
		<!-- Modal -->
		
		<%@include file="newEmployeeModal.jsp"%>
		<INPUT type="hidden" id="TaxByLabelOrderManagement"
			name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
		<%@include file="newProductModal.jsp"%>
		<%@include file="NewChartOfAccountAdd.jsp"%>
		<%@include file="../jsp/newTransportModeModal.jsp"%> <!-- imti for add transport -->
		<%@include file="newGstDetailModal.jsp"%>
		<%@include file="newPaymentTermsModal.jsp"%>
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


	</div>
</div>

<%
	String title1 = "Save as Open & Send Email";

	String fiscalyear = plantMstDAO.getFiscalYear(plant);
	ArrayList plntList = plantMstDAO.getPlantMstDetails(plant);
	Map plntMap = (Map) plntList.get(0);
	String PLNTDESC = (String) plntMap.get("PLNTDESC");
	String ADD1 = (String) plntMap.get("ADD1");
	String ADD2 = (String) plntMap.get("ADD2");
	String ADD3 = (String) plntMap.get("ADD3");
	String ADD4 = (String) plntMap.get("ADD4");
	String STATE = (String) plntMap.get("STATE");
	String COUNTRY = (String) plntMap.get("COUNTY");
	String ZIP = (String) plntMap.get("ZIP");

	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
%>
<%
	EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
	Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.INVOICE);
	String template_subject = (String)emailMsg.get("SUBJECT");
	String template_body = (String)emailMsg.get("BODY1");
%>
<input type="hidden" id="plant_desc" value="<%=PLNTDESC %>" />
<input type="hidden" id="template_subject" value="<%=template_subject %>" />
<input type="hidden" id="template_body" value="<%=template_body %>" />
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>
<!-- --------------modal---------------- -->

<div id="remarksModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
  			<div class="modal-header">
          		<button type="button" class="close" data-dismiss="modal">&times;</button>
          		<h4 class="modal-title">Remarks</h4>
        	</div>
        	<div class="modal-body">
        		<div class="container-fluid">
	        		<form id="remarksForm" name="remarksForm" method="post">
						<table id="remarks-table" style="width:65%;">
							<tbody>
							</tbody>
						</table>
	        		</form>
	        		<br>
	        		<div class="form-group">
						<div class="row">
							<div class="col-sm-6">
								<a class="add-line" style="text-decoration: none; cursor: pointer;"
								onclick="addRemarksRow()">+ Add another Remarks</a>
							</div>
						</div>
					</div>
        		</div>
	        </div>
	        <div class="modal-footer">
	          <button type="button" id="btnSaveRemarks" class="btn btn-success pull-left">Save</button>
	        </div>
  		</div>
	</div>
</div>

<div id="customerModal" class="modal fade"  name="form" role="dialog">
	<div class="modal-dialog modal-lg">
		<form class="form-horizontal" name="form" method="post">

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
								<INPUT class="form-control" name="CUST_CODE" id="CUST_CODE"
									type="TEXT" value="" onchange="checkitem(this.value)" size="50"
									MAXLENGTH=50 width="50"> <span
									class="input-group-addon" onClick="onIDGen();"> <a
									href="#" data-toggle="tooltip" data-placement="top"
									title="Auto-Generate"> <i class="glyphicon glyphicon-edit"
										style="font-size: 20px;"></i></a></span>
							</div>
							<INPUT type="hidden" name="CUST_CODE1" value="">
							<INPUT type="hidden" name="COUNTRY" value="">
							<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
							   <INPUT type="hidden" name="CURRENCYID_C" value="<%=DISPLAYID%>">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Customer
							Name</label>
						<div class="col-sm-4">
							<INPUT class="form-control" name="CUST_NAME" type="TEXT" value=""
								size="50" MAXLENGTH=100>
						</div>
					</div>
					
					
					<!-- 	                Author Name:Resviya ,Date:10/08/21 , Description -UEN Label     -->
          <div class="form-group" id="UEN">
	      <label class="control-label col-form-label col-sm-2" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
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
									onClick="javascript:popUpWin('customertypelistsave.jsp?CUSTOMER_TYPE_ID='+form.CUSTOMER_TYPE_ID.value);">
									<a href="#" data-toggle="tooltip" data-placement="top"
									title="Customer Type Details"> <i
										class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
								</span>
							</div>
						</div>
					</div>

					<INPUT type="hidden" id="TaxByLabel" name="taxbylabel"
						value="<%=taxbylabel%>" >

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

										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE" name="COUNTRY_CODE" value="" style="width: 100%">
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
										Mode</label>
									<div class="col-sm-4">
										<div class="input-group">
											<INPUT class="form-control" name="PAYTERMS" type="TEXT"
												value="" size="20" MAXLENGTH=100 readonly> <span
												class="input-group-addon"
												onClick="javascript:popUpWin('list/paymenttypelist_save.jsp?paymenttype='+form.PAYTERMS.value);">
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
										<INPUT name="CREDITLIMIT" type="TEXT" value=""  size="50"
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
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" value="" style="width: 100%">
				<OPTION style="display:none;">Select Bank</OPTION>
				<%
		    _MasterUtil=new  MasterUtil();
		    ccList =  _MasterUtil.getBankList("",plant);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vNAME = (String)m.get("NAME"); %>
		        <option  value='<%=vNAME%>' ><%=vNAME %> </option>		          
		        <%
       			}
			 %></SELECT>
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


<script type="text/javascript">
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
		    
	newinvno();	//onload func for AutoInvoice input imthi

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
    document.getElementById("btnpop").style.display = "none";

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
	 var CUST_CODE   = document.form.CUST_CODE.value;
	 var CUST_NAME   = document.form.CUST_NAME.value;
//	 var cus_companyregnumber   = document.form.cus_companyregnumber.value;
	 var CL   = document.form.CREDITLIMIT.value;
	 var TAXTREATMENT   = document.form.TAXTREATMENT.value;
	 var RCBNO   = document.form.RCBNO.value;
	 var currency = document.form.CUS_CURRENCY.value;
	 var region = document.form.COUNTRY_REG.value; 
	 
	 var rcbn = RCBNO.length;
	 if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE.focus(); return false; }
	 if(CUST_NAME == "" || CUST_NAME == null) {alert("Please Enter Customer Name");document.form.CUST_NAME.focus(); return false; }

//   Author Name:Resviya ,Date:9/08/21 , Description -UEN Alert    

	  /*  if(region == "GCC"){
		   document.form.cus_companyregnumber.value="";
		}else if(region == "ASIA PACIFIC"){
			if (cus_companyregnumber == "" || cus_companyregnumber == null) {
			alert("Please Enter Unique Entity Number (UEN)");
			document.form.cus_companyregnumber.focus();
			return false; 
			}
		} */

//	   END

	//Author Name:Resviya ,Date:17/07/21

	 if(currency == "" || currency == null) {
		 alert("Please Enter Currency ID"); 
		 document.form.CUS_CURRENCY.focus();
		 return false; 
		 }
      //	END
	 if(form.TAXTREATMENT.selectedIndex==0)
		{
		alert("Please Select TAXTREATMENT");
		form.TAXTREATMENT.focus();
		return false;
		}
	 if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	   {
		 var  d = document.getElementById("TaxLabel").innerHTML;
	   	if(RCBNO == "" || RCBNO == null) {
	   		
		   alert("Please Enter "+d+" No."); 
		   document.form.RCBNO.focus();
		   return false; 
		   }
	   	//if(document.form.COUNTRY_REG.value=="GCC")// region based validtion
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
	 if(document.form.COUNTRY_CODE.selectedIndex==0)
		{
		   alert("Please Select Country from Address");
		   document.form.COUNTRY_CODE.focus();
		 return false;
		}
	 /* if(isCL.equals("1") && CL.equals(""))
		  {
			  alert("Please Enter Credit Limit"); 
			   document.form.CREDITLIMIT.focus();
			   return false; 
		  }	 */
		  document.getElementById('nTAXTREATMENT').innerHTML="";
		  $("input[name ='nTAXTREATMENT']").val("");
		  var tranId = document.form1.TranId.value;
		  var cmd1 = document.form1.cmd.value;
	 document.form.action  = "/track/CreateCustomerServlet?action=ADD&reurl=expensetoInvoice.jsp?TRANID="+tranId+"&cmd="+cmd1;
	 document.form.submit();
	}
	
	function onIDGen()
	{
		var tranId = document.form1.TranId.value;
		var cmd1 = document.form1.cmd.value;
	 document.form.action  = "/track/CreateCustomerServlet?action=Auto-ID&reurl=expensetoInvoice.jsp?TRANID="+tranId+"&cmd="+cmd1;
	 document.form.submit(); 
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
	
	function newinvno(){
		var plant = document.form1.plant.value;
		var urlStr = "/track/OutboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
			PLANT : plant,
			ACTION : "INVOICE"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "100") {
					var resultVal = data.result;				
					var resultV = resultVal.invno;
					$("input[name ='invoice']").val(resultV);
				} else {
					document.form1.invoice.value = "";
				}
			}
		});	
	}
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
				$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
					 $.each(StateList, function (key, value) {
						   $('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
						});
			}
		});	
		
	}
	$('select[name="COUNTRY_CODE"]').on('change', function(){		
	    var text = $("#COUNTRY_CODE option:selected").text();
	    $("input[name ='COUNTRY']").val(text.trim());
	});


	$('#CUSTOMER').on('typeahead:selected', function(evt, item) {
		getCustomerDetails();
	});
	function getCustomerDetails(){
		$.ajax({
			type: "GET",
			url: "../MasterServlet",
			data: {
				"action":'GET_CUSTOMER_BY_CODE',
				"CUSTOMERCODE":$('#CUST_CODE').val(),
				"PLANT":'<%=plant%>'
			},
			dataType: "json",
			beforeSend: function(){
				showLoader();
			},
			success: function(data) {
				if (data.ERROR_CODE == '100'){
					$("#CUSTOMEREMAIL").val(data.CUSTOMER_EMAIL);
				}else{
					alert('Unable to get supplier details. Please try again later.');
				}
			},
			error: function(data) {
				alert('Unable to get supplier details. Please try again later.');
			},
			complete: function(){
				hideLoader();
			}
		});
	}
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>