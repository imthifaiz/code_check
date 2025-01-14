<%@page import="com.track.db.object.DoDet"%>
<%@page import="com.track.db.object.DoHdr"%>
<%@page import="com.track.db.object.InvPaymentDetail"%>
<%@page import="com.track.db.object.FinProject"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%
	String title = "Sales Order Detail";
    String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
    String USERID= session.getAttribute("LOGIN_USER").toString();
    String systatus = session.getAttribute("SYSTEMNOW").toString();
    String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	DoHdr doHdr = (DoHdr)request.getAttribute("DoHdr");
	Map plntMap = (Map)request.getAttribute("PLNTMAP");
	Map doHdrDetails = (Map)request.getAttribute("DOHDRDETAILS");
	List<DoDet> doDetDetails = (ArrayList<DoDet>)request.getAttribute("DoDetList");
	ArrayList custDetails = (ArrayList)request.getAttribute("CUSTDETAILS");
	ArrayList shippingCustDetails = (ArrayList)request.getAttribute("SHIPPINGCUSTDETAILS");
	String imagePath = (String)request.getAttribute("IMAGEPATH");
	String numberOfDecimal = (String)request.getAttribute("NUMBEROFDECIMAL");
	ItemMstDAO itemMstDAO = new ItemMstDAO();
	boolean displaySummaryNew=false,displaySummaryEdit=false,displayPrintPdf=false,displaySummaryLink=false,displayMore=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{	
	displaySummaryLink = ub.isCheckValAcc("summarylnksalesorder", plant,USERID);
	displaySummaryEdit = ub.isCheckValAcc("editsalesorder", plant,USERID);
	displayPrintPdf = ub.isCheckValAcc("printsalesorder", plant,USERID);
	displayMore = ub.isCheckValAcc("moresalesorder", plant,USERID);
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryLink = ub.isCheckValinv("summarylnksalesorder", plant,USERID);
		displaySummaryEdit = ub.isCheckValinv("editsalesorder", plant,USERID);
		displayPrintPdf = ub.isCheckValinv("printsalesorder", plant,USERID);
		displayMore = ub.isCheckValinv("moresalesorder", plant,USERID);
		
	}
	
	String orderDesc = "", rcbno = "", Attention = "", Telephone = "", TELEPHONE = "", FaxLBL = "",printcustomeruenno = "", printuenno = "", customeruenno = "", uenno = "",
			Email = "", toHeader = "", fromHeader = "", customerRcbno = "", shipTo = "", terms = "",
			date = "", lblDeliveryDate = "", lblINCOTERM = "", lblEmployee = "", lblPreBy = "",
			lblSoNo = "", lblItem = "", lblOrderQty = "", lblUom = "", lblRate = "", lblDiscount = "",
			lblAmt = "", lblSubTotal = "Sub Total", lblOrderdiscount = "", lblTotalDiscount = "",
			lblShippingcost = "", lblTotal = "", printWithBrand = "", printWithHscode = "",
			printWithCoo = "", printWithProductDeliveryDate = "", printWithProductRemarks = "",
			prdXtraDetails = "", lblHscode = "", lblCoo = "", lblProductDeliveryDate = "",
			displayByOrderType = "",lblPaymentMade = "",lblBalanceDue = "";
	
	String PLNTDESC = (String) plntMap.get("PLNTDESC");
	String ADD1 = (String) plntMap.get("ADD1");
	String ADD2 = (String) plntMap.get("ADD2");
	String ADD3 = (String) plntMap.get("ADD3");
	String ADD4 = (String) plntMap.get("ADD4");
	String STATE = (String) plntMap.get("STATE");
	String COUNTRY = (String) plntMap.get("COUNTY");
	String ZIP = (String) plntMap.get("ZIP");
	String RCBNO = (String) plntMap.get("RCBNO");
	String NAME = (String) plntMap.get("NAME");
	String TELNO = (String) plntMap.get("TELNO");
	String FAX = (String) plntMap.get("FAX");
	String EMAIL = (String) plntMap.get("EMAIL");
	String companyregnumber = (String) plntMap.get("companyregnumber");//imti
	
	double shingpercentage =0.0;
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	if(!doHdrDetails.isEmpty()){
		orderDesc = (String) doHdrDetails.get("HDR1");
		rcbno = (String)doHdrDetails.get("RCBNO");
		Telephone = (String)doHdrDetails.get("TELEPHONE");
		FaxLBL = (String) doHdrDetails.get("FAX");
	    Email = (String) doHdrDetails.get("EMAIL");
	    toHeader = (String) doHdrDetails.get("HDR2");
	    fromHeader = (String) doHdrDetails.get("HDR3");
	    customerRcbno = (String) doHdrDetails.get("CUSTOMERRCBNO");	  
	    shipTo = (String) doHdrDetails.get("SHIPTO");  
	    terms = (String) doHdrDetails.get("TERMS");
	    date = (String) doHdrDetails.get("DATE");
	    lblDeliveryDate = (String) doHdrDetails.get("DELIVERYDATE");
	    lblINCOTERM = (String) doHdrDetails.get("INCOTERM");
	    lblEmployee = (String) doHdrDetails.get("EMPLOYEE");
	    lblPreBy = (String) doHdrDetails.get("PREPAREDBY");
	 
	    lblSoNo = (String) doHdrDetails.get("SONO");
	    lblItem = (String) doHdrDetails.get("ITEM");
	    lblOrderQty = (String) doHdrDetails.get("ORDERQTY");
	    lblUom = (String) doHdrDetails.get("UOM");
	    lblRate = (String) doHdrDetails.get("RATE");
	    lblDiscount = (String) doHdrDetails.get("DISCOUNT");
	    lblAmt = (String) doHdrDetails.get("AMT");
	    lblOrderdiscount = (String) doHdrDetails.get("ORDERDISCOUNT");
	    lblShippingcost = (String) doHdrDetails.get("SHIPPINGCOST");
	    lblTotalDiscount = (String) doHdrDetails.get("TOTALAFTERDISCOUNT");
	    lblTotal = (String) doHdrDetails.get("TOTAL");
	    lblHscode = (String) doHdrDetails.get("HSCODE"); 
	    lblCoo = (String) doHdrDetails.get("COO"); 
	    lblBalanceDue = (String) doHdrDetails.get("BALANCEDUE"); 
	    lblPaymentMade = (String) doHdrDetails.get("PAYMENTMADE"); 
	    
	    uenno = (String) doHdrDetails.get("UENNO");
		customeruenno = (String) doHdrDetails.get("CUSTOMERUENNO");
		printuenno = (String)doHdrDetails.get("PRINTWITHUENNO");
        printcustomeruenno = (String)doHdrDetails.get("PRINTWITHCUSTOMERUENNO");
	    
	    prdXtraDetails = (String) doHdrDetails.get("PRINTXTRADETAILS");
	    printWithBrand = (String) doHdrDetails.get("PRINTWITHBRAND");
	    printWithHscode = (String) doHdrDetails.get("PRITNWITHHSCODE");
	    printWithCoo = (String) doHdrDetails.get("PRITNWITHCOO");
	    printWithProductDeliveryDate = (String) doHdrDetails.get("PRINTWITHPRODUCTDELIVERYDATE");
	    printWithProductRemarks = (String) doHdrDetails.get("PRINTWITHPRODUCTREMARKS");
	    
	    displayByOrderType = (String) doHdrDetails.get("DISPLAYBYORDERTYPE");
	    
	   
		
	}
	double dTotalQty = 0, dTotalPickedQty = 0, dTotalIssuedQty = 0,totalTax = 0, subTotal = 0;
	List ItemList = (ArrayList)request.getAttribute("ItemList");
	List taxList =new ArrayList();
	
	FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
	FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(doHdr.getTAXID());
	
	 FinProjectDAO finProjectDAO = new FinProjectDAO();
		FinProject finProject=new FinProject();
		String projectname = "";
		if(doHdr.getPROJECTID() > 0){
			finProject = finProjectDAO.getFinProjectById(plant, doHdr.getPROJECTID());
			projectname = finProject.getPROJECT_NAME();
		}
		
		TransportModeDAO transportmodedao = new TransportModeDAO();
		String transportmode = "";
		if(doHdr.getTRANSPORTID() > 0){
			transportmode = transportmodedao.getTransportModeById(plant, doHdr.getTRANSPORTID());
		}
		
		String empname = "";
		String ORDER_STATUS = doHdr.getORDER_STATUS();
		String CUST_ORDER_STATUS = doHdr.getAPP_CUST_ORDER_STATUS();
		String STATUS = doHdr.getSTATUS();
	     if (ORDER_STATUS.equalsIgnoreCase("Open")){ 
	       if(STATUS.equalsIgnoreCase("O")){
	    		 ORDER_STATUS="PARTIALLY PROCESSED";
	    	 }
	       else if(STATUS.equalsIgnoreCase("N")){
	       ORDER_STATUS="OPEN";
	    	 }
	       else if(STATUS.equalsIgnoreCase("C")){
		       ORDER_STATUS="PROCESSED";
	       	}
	     }
	     
	     String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
			
	     
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALES_ORDER%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/JsBarcode.all.js"></script>
<style>
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td{
	border: none;
	padding: 0px;
}
#table3>tbody>tr>td{
	border: none;
}

#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}
.text-ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.ribbon {
    position: absolute!important;
    top: -5px;
    left: -5px;
    overflow: hidden;
    width: 96px;
    height: 94px;
}
.ribbon .ribbon-draft {
    background-color: #94a5a6;
    border-color: #788e8f;
}
.ribbon .ribbon-inner {
    text-align: center;
    color: #fff;
    top: 24px;
    left: -31px;
    width: 135px;
    padding: 3px;
    position: relative;
    transform: rotate(-45deg);
}
.ribbon .ribbon-inner:before {
    left: 0;
    border-left: 2px solid transparent;
}
.ribbon .ribbon-inner:after {
    right: -2px;
    border-bottom: 3px solid transparent;
}
.ribbon .ribbon-inner:after, .ribbon .ribbon-inner:before {
    content: "";
    border-top: 5px solid transparent;
    border-left: 5px solid;
    border-left-color: inherit;
    border-right: 5px solid transparent;
    border-bottom: 5px solid;
    border-bottom-color: inherit;
    position: absolute;
    top: 20px;
    transform: rotate(-45deg);
}
.invoice-banner {
    margin-top: 15px;
    margin-bottom: 10px;
    font-size: 13px;
    background-color: #fdfae4;
    border: 1px solid #ede5ae;
    padding: 10px;
    overflow: visible;
}
#table2>tfoot>tr>td {
	border: none;
}
#footerTable>tbody>tr>td {
	border: none;
}
#footerTable{
	display:none;
}
.page-break-before {
page-break-before : always;
}
@media print {
 /*  @page { margin: 0; } */
  body { margin: 1cm 1.6cm 1.6cm 1.6cm; }
  #footerTable{
	display:table !important;
  }  
}
</style>
<div class="container-fluid m-t-20">
	<div class="alert alert-danger alert-dismissible" style="width: max-content;margin:0 auto;" hidden>
	    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    <span id="err-msg"></span>
    </div>
    <div class="alert alert-success alert-dismissible" style="width: max-content;margin:0 auto;" hidden>
    	<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    	<span id="success-msg"></span>
   	</div>
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>      
                <li><a href="../salesorder/summary"><span class="underline-on-hover">Sales Order Summary</span> </a></li>              
                <li><label>Sales Order Detail</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
			<div class="btn-group" role="group">
			<%if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")){ 
			if(CUST_ORDER_STATUS.equalsIgnoreCase("Waiting For HQ Manager Approval") && ORDER_STATUS.equalsIgnoreCase("Draft")){
			%>
				<%-- <a href="../salesorder/Approve_Sales_Order?dono=<%=doHdr.getDONO()%>">
					<button type="button" class="btn btn-success" title="Approve">
						Approve
					</button>
					</a> --%>
			<%}else{%>
			<%-- <%if("OPEN".equalsIgnoreCase(ORDER_STATUS) || "PARTIALLY PROCESSED".equalsIgnoreCase(ORDER_STATUS)){ %>
						<button type="button" class="btn btn-success" data-toggle="dropdown" 						
						onClick="{processSalesOrder()}">
						Sales PICK &amp; ISSUE</button>
						<form method="POST" action="../OrderIssuingServlet?action=View" id="frmDONO" style="display: none;">
							<input type="hidden" name="DONO" value="<%=doHdr.getDONO()%>" />
							<input type="hidden" name="RFLAG" value="5" />
							<input type="hidden" name="ENCRYPT_FLAG" value="1" />
						</form>
						<script>
							function processSalesOrder(){
								$('#frmDONO').submit();
							}
						</script>
			
			<%}%> --%>
			<%}}else{ %>
			<%if (!CUST_ORDER_STATUS.equalsIgnoreCase("CREATE APPROVAL PENDING") && !CUST_ORDER_STATUS.equalsIgnoreCase("EDIT APPROVAL PENDING")&& !CUST_ORDER_STATUS.equalsIgnoreCase("DELETE APPROVAL PENDING")){ %> <!-- Sales Approve 09.22 - Azees -->
				<%if("OPEN".equalsIgnoreCase(ORDER_STATUS) || "PARTIALLY PROCESSED".equalsIgnoreCase(ORDER_STATUS)){ %>
						<button type="button" class="btn btn-success" data-toggle="dropdown" 						
						onClick="{processSalesOrder()}">
						Sales PICK &amp; ISSUE</button>
						<form method="POST" action="../OrderIssuingServlet?action=View" id="frmDONO" style="display: none;">
							<input type="hidden" name="DONO" value="<%=doHdr.getDONO()%>" />
							<input type="hidden" name="RFLAG" value="5" />
							<input type="hidden" name="ENCRYPT_FLAG" value="1" />
						</form>
						<script>
							function processSalesOrder(){
								$('#frmDONO').submit();
							}
						</script>
				<%} } }%>
				</div>
				<%-- <div class="btn-group" role="group">
				<%if("OPEN".equalsIgnoreCase(ORDER_STATUS) || "PARTIALLY PROCESSED".equalsIgnoreCase(ORDER_STATUS)){ %>
						<button type="button" class="btn btn-success" data-toggle="dropdown" 						
						onClick="window.location.href='../salestransaction/orderpickissue?DONO=<%=doHdr.getDONO()%>&action=View'">
						Sales Pick &amp; Issue</button>
				<%} %>
				</div> --%>
				<div class="btn-group" role="group">
				<%-- For Al-Azhar Testing, Edit is opened on 17.11.22 by Azees --%>
				<% if(displaySummaryEdit){
				// if(!doHdr.getORDERTYPE().equalsIgnoreCase("POS") && !CUST_ORDER_STATUS.equalsIgnoreCase("CREATE APPROVAL PENDING") && !CUST_ORDER_STATUS.equalsIgnoreCase("EDIT APPROVAL PENDING")&& !CUST_ORDER_STATUS.equalsIgnoreCase("DELETE APPROVAL PENDING")){
				//if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")){ %>
				
				<%//if (doHdr.getORDER_STATUS().equalsIgnoreCase("Draft")){ %>
					<%-- <a href="../salesorder/edit?dono=<%=doHdr.getDONO()%>">
					<button type="button" class="btn btn-default" data-toggle="tooltip"  
						data-placement="bottom" title="Edit">
						<i class="fa fa-pencil" aria-hidden="true"></i>
					</button>
					</a> --%>
					<%//}
				
				//}else{ %>
					<%if (!doHdr.getORDER_STATUS().equalsIgnoreCase("FORCE CLOSE")){ %>
					<%-- <a href="../salesorder/edit?dono=<%=doHdr.getDONO()%>">
						<button type="button" class="btn btn-default"
							data-toggle="tooltip" data-placement="bottom" title="Edit">
							<i class="fa fa-pencil" aria-hidden="true"></i>
						</button>
					</a> --%>
					<%}
				 //}
				//} 
				}%>
				</div>
				<div class="btn-group" role="group">
				<%if(displayPrintPdf){ %>
					<!-- <button type="button" class="btn btn-default" data-toggle="dropdown" >Print 
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu" style="min-width: 0px;">
						<li id="" onClick="submitFormPrint('Print Outbound Order');"><a href="#">Print Sales Order</a></li>
						<li id="" onClick="submitFormPrint('Print Outbound Order With Price');"><a href="#">Print Sales Order With Price</a></li>
						<li id="" onClick="submitForm('Export To Excel');"><a href="#">Export To Excel</a></li>
				  	</ul> -->
				  	<%}%>
				</div>
				<div class="btn-group" role="group">

				<%if(displayMore){
				
				if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")){ %>
				
					<%-- <button type="button" class="btn btn-default" data-toggle="dropdown" >More 
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu" style="min-width: 0px;">
						<li id=""><a href="../salesorder/convertToSales?dono=<%=doHdr.getDONO()%>">Convert To Sales</a></li>
						<li id=""><a href="../salesorder/convertToPurchase?dono=<%=doHdr.getDONO()%>">Convert To Purchase</a></li>
					    <li id="so-delete"><a href="#">Delete</a></li>
				  	</ul> --%>
				  	<%}} %>
				</div>
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../peppolapi/salesordersummary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;" id="print_id1">
			<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
				<div class="ribbon-inner ribbon-draft"><%=ORDER_STATUS%></div>
			</div>
			<div style="height: 0.700000in;"></div>
			<span id="print_id">
				<div class="row">
					<div class="col-xs-6">
						<img src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>"
					style="width: 130.00px;" id="logo_content">
					<div>
						<b><%=PLNTDESC%></b>
					</div>
					<span style="white-space: pre-wrap; word-wrap: break-word;">
<%=fromAddress_BlockAddress.trim()%>
<%=fromAddress_RoadAddress.trim()%>
<%=fromAddress_Country.trim()%> <%=ZIP.trim()%>
<%if(printuenno.equals("1")){ %><%=uenno%> :<%=companyregnumber.trim()%><br><%} %><%=rcbno.trim()%> <%=RCBNO.trim()%>
Contact: <%=NAME.trim()%>
Tel: <%=TELNO.trim()%> Fax : <%=FAX.trim()%>
Email: <%=EMAIL.trim()%>
					</span>
					</div>
					<div class="col-xs-6 text-right">
							<%if(displayByOrderType.equals("1")){%>
								<h1 id="headerpage"><%=doHdr.getORDERTYPE()%></h1>
							<%}else{%>
								<h1 id="headerpage"><%=orderDesc%></h1>
							<%} %>
						<figure class="pull-right text-center">
							<img id="barCode" style="width:215px;height:65px;">
							<figcaption># <%=doHdr.getDONO()%></figcaption>
						</figure>
						<br style="clear:both"> 
					</div>
				</div>
				<div class="row">
					<div class="col-xs-7">
					<span><%=toHeader%></span>	<br>
					<span><%=custDetails.get(1)%></span>	<br>
					<%if(printcustomeruenno.equals("1")){%>  							
					<span><%=customeruenno%> <%=custDetails.get(27)%></span><%}%>
					<span><%=customerRcbno%> <%=custDetails.get(26)%></span>	<br>
					<span><%=custDetails.get(2)%> <%=custDetails.get(3)%></span>	<br> <!-- Block Address (Add1 + Add2) -->
					<span><%=custDetails.get(4)%> <%=custDetails.get(16)%></span>	<br> <!-- Road Address (Add3 + Add4) -->
					<span><%=custDetails.get(5)%> <%=custDetails.get(24)%> <%=custDetails.get(6)%></span>	<br> <!-- Country + State + Zip -->
					<span>Email: <%=custDetails.get(14)%></span>	<br>
					<span>Remarks: <%=custDetails.get(15)%></span>	<br>
					<%if(shippingCustDetails.size()>0){%>
					<br>
					<span><%=shipTo%></span>	<br>
					<span><%=shippingCustDetails.get(1)%></span>	<br>
					<span><%=shippingCustDetails.get(7)%></span>	<br>
					<span><%=shippingCustDetails.get(8)%></span>	<br>
					<span><%=shippingCustDetails.get(9)%> <%=shippingCustDetails.get(10)%></span>	<br>
					<span><%=shippingCustDetails.get(11)%> <%=shippingCustDetails.get(12)%> <%=shippingCustDetails.get(13)%></span>	<br><!-- Country + State + Zip -->
					<span>Attention: </span>	<br>
					<span>Tel: <%=shippingCustDetails.get(3)%></span>	<br>		
					<span></span>	<br>
					<%} %>
					<br>
					<span><%=terms%> <%=custDetails.get(19)%>  <%=custDetails.get(20)%></span><br>
					<span>TransportMode : <%=transportmode%></span><br>
					<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
					<span>Project : <%=projectname%></span><br>
					<%} %>
					</div>
					<div class="col-xs-5 text-right">
						<table id="table1" class="table pull-right">
							<tbody>
								<tr>
									<td><%=date%></td>
									<td><%=doHdr.getDELDATE()%></td>
								</tr>
								<%-- <%if(printWithDeliveryDate.equals("1") && ((String)invoiceHdr.get("DUE_DATE")).length() > 0){%> --%>
								<tr>
									<td><%=lblDeliveryDate%></td>
									<td><%=doHdr.getDELIVERYDATE()%></td>
								</tr>
								<%-- <%} %> --%>
								<tr>
									<td><%=lblINCOTERM%></td>
									<td><%=doHdr.getINCOTERMS()%></td>
								</tr>
								<%-- <%if(printEmployee.equals("1") && ((String)invoiceHdr.get("EMPNO")).length() > 0){%> --%>
								<tr>
									<td><%=lblEmployee%></td>
									<td><%=empname%></td>
								</tr>
								<%-- <%} %> --%>
								<tr>
									<td><%=lblPreBy%></td>
									<td><%=doHdr.getCRBY()%></td>
								</tr>
								<tr>
									<td>Terms</td>
									<td><%=doHdr.getPAYMENT_TERMS()%></td>
									<tr>
							</tbody>
						</table>
					</div>
				</div>
				<br>
				<div class="row">
					<div class="col-xs-12">
						<table id="table2" class="table">
							<thead>
								<tr>
									<td><%=lblSoNo%></td>
									<td><%=lblItem%></td>
									<td><%=lblOrderQty%></td>
									<td>Picked Qty</td>
									<td>Issued Qty</td>
									<td><%=lblUom%></td>
									<td>Cost</td>
									<td>Add On Cost</td>
									<td><%=lblRate%></td>
									<td><%=lblDiscount%></td>
									<td class="text-right"><%=lblAmt%></td>
								</tr>
							</thead>
							<tbody>
								<%
								int i = 0;
								String taxdisplay="";
								for(DoDet doDet :doDetDetails){
									//Map m = (Map)ItemList.get(i);
									String qty="", unitPrice="", amount="", percentage="", tax="",
											item_discounttype = "", lineDiscount = "",
											pickedQty = "", issuedQty = "", cost="";
									double discount = 0, dTax = 0;
									item_discounttype = doDet.getDISCOUNT_TYPE();
									qty = doDet.getQTYOR().toString();
									double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
						  			qty = StrUtils.addZeroes(dQty, "3");
						  			dTotalQty += dQty;
						  			
						  			pickedQty = doDet.getQtyPick().toString();
						  			double dpQty ="".equals(pickedQty) ? 0.0d :  Double.parseDouble(pickedQty);
						  			pickedQty = StrUtils.addZeroes(dpQty, "3");
						  			dTotalPickedQty += dpQty;
						  			
						  			issuedQty = doDet.getQtyPick().toString();
						  			double diQty ="".equals(pickedQty) ? 0.0d :  Double.parseDouble(issuedQty);
						  			issuedQty = StrUtils.addZeroes(diQty, "3");
						  			dTotalIssuedQty += diQty;
						  			
						  			double dCost = doDet.getUNITPRICE() * doDet.getCURRENCYUSEQT();
						  			unitPrice = Numbers.toMillionFormat(dCost, numberOfDecimal);
						  			double item_amount = (dCost*dQty);
						  			
						  			if(item_discounttype.equalsIgnoreCase("%")){
										double dDiscount = doDet.getDISCOUNT();
										discount = ((item_amount)/100)*dDiscount;
										lineDiscount = Numbers.toMillionFormat(dDiscount, "3");
									}else{
										discount = doDet.getDISCOUNT() * doDet.getCURRENCYUSEQT();
										lineDiscount = Numbers.toMillionFormat(discount, numberOfDecimal);
									}
						  			item_amount = item_amount - discount;
						  			
						  			taxdisplay = doDet.getTAX_TYPE();
						  			/* double itemtax = 0;
						  			if(!doDet.getTAX_TYPE().equalsIgnoreCase("")){
						  				String str = doDet.getTAX_TYPE();
						  				if(str.equalsIgnoreCase("EXEMPT") || str.equalsIgnoreCase("OUT OF SCOPE"))
						  					str = StrUtils.fString(str+"[0.0%]").trim();
						  				String display = str;
						  				str=str.replace("%]","");			  				
						  				String[] arrOfStr = str.split("\\[");
						  				percentage = arrOfStr[1];
						  				System.out.println(percentage);
						  				System.out.println(cost);
						  			double dPercntage = 0.0d == (doDet.getUNITPRICE()) ? 0.0d :  Double.parseDouble(percentage);
						  			shingpercentage = dPercntage;
						  			
						  			dTax = (item_amount * (dPercntage / 100));
						  			itemtax = dTax;
						  			System.out.println(dTax);
						  			tax = Numbers.toMillionFormat(dTax, numberOfDecimal);
						  					
						  			Map<String, String> taxDetail = new HashMap();
						  			boolean match = false;
						  			//String display = (String)m.get("TAX_TYPE")+" ["+(String)m.get("GSTPERCENTAGE")+"%]";
						  			taxDetail.put("name", display);
						  			taxDetail.put("types", arrOfStr[0]);
						  			taxDetail.put("tax", tax);
						  			System.out.println(taxDetail);
						  			if(taxList.size() == 0){
						  				taxList.add(taxDetail);
						  			}else{
						  				for(int j =0; j < taxList.size(); j++) {
						  					Map tMap=(Map)taxList.get(j);
						  					if(((String)tMap.get("types")).equalsIgnoreCase(arrOfStr[0])){			  						
						  						dTax = dTax + Double.parseDouble((String)tMap.get("tax"));
						  						tax = Numbers.toMillionFormat(dTax, numberOfDecimal);
						  						tMap.put("tax", tax);
						  						match = true;
						  					}
						  				}
						  				if(!match){
							  				taxList.add(taxDetail);
							  			}
						  			}
						  			}
						  			
						  			
						  			totalTax += itemtax; */
						  			subTotal += item_amount;
								%>
								<%
								Hashtable hit = new Hashtable();
								hit.put("PLANT", plant);
								hit.put("ITEM", doDet.getITEM());
								hit.put("ISCOMPRO", "1");
									boolean itemexist = itemMstDAO.isExisit(hit, "");
									if(itemexist){
									%>
									<tr>
										<td class="text-left" colspan="9"><b>PARENT PRODUCT</b></td>
									</tr>
									<%} %>
									<tr>
										<td class="text-center"><%=doDet.getDOLNNO() %></td>
										<td class="text-center">
										<%=doDet.getITEM()%>
										<%if(printWithBrand.equals("1")){ %>
											<%-- <br/>Brand: <%=m.get("brand")%> --%>
										<%} %>
										<%if(printWithHscode.equals("1")){ %>
											<%-- <br/><%=lblHscode%> <%=m.get("hscode")%> --%>
										<%} %>
										<%if(printWithCoo.equals("1")){ %>
											<%-- <br/><%=lblCoo%> <%=m.get("coo")%> --%>
										<%} %>
										<%if(printWithProductDeliveryDate.equals("1")){ %>
											<%=lblProductDeliveryDate%> 
										<%} %>
										</td>
										<td class="text-center"><%=qty%></td>
										<td class="text-center"><%=StrUtils.addZeroes(doDet.getQtyPick().doubleValue(), "3")%></td>
										<td class="text-center"><%=StrUtils.addZeroes(doDet.getQTYIS().doubleValue(), "3")%></td>
										<td class="text-center"><%=doDet.getUNITMO()%></td>
										<td class="text-center"><%= Numbers.toMillionFormat(String.valueOf(doDet.getUNITCOST()),Integer.valueOf(numberOfDecimal)) %></td>
										<%if(doDet.getADDONTYPE().equalsIgnoreCase("%")){ %>
											<td class="text-center"><%=StrUtils.addZeroes(doDet.getADDONAMOUNT(), "3")+' '+doDet.getADDONTYPE()%></td>
										<%}else{ %>
											<td class="text-center"><%=Numbers.toMillionFormat(String.valueOf(doDet.getADDONAMOUNT()),Integer.valueOf(numberOfDecimal))+' '+doDet.getADDONTYPE()%></td>
										<%} %>
										<td class="text-center"><%=Numbers.toMillionFormat(unitPrice,Integer.valueOf(numberOfDecimal))%></td>
										<td class="text-center"><%=lineDiscount%><%=item_discounttype%></td>
										<td class="text-right"><%=Numbers.toMillionFormat(String.valueOf(item_amount),Integer.valueOf(numberOfDecimal))%></td>
									</tr>
						<tr>
          					<td></td>
          					<td colspan="10">&emsp;&emsp;&emsp;<%=doDet.getItemDesc() %></td>
						</tr>
									<%
									
									
									if(itemexist){
										
									%>
									<tr>
										<td class="text-left" colspan="9"><b>CHILD PRODUCT</b></td>
									</tr>
									<%
									ProductionBomUtil _ProductionBomUtil = new ProductionBomUtil();
									ArrayList  movQryList = _ProductionBomUtil.getProdBomList(doDet.getITEM(),plant, " AND BOMTYPE='KIT'");
									 if (movQryList.size() > 0) {
										for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
											 Map lineArr = (Map) movQryList.get(iCnt);
											String cchilditem = StrUtils.fString((String)lineArr.get("CITEM")) ;
                           					String cdesc = StrUtils.fString((String)lineArr.get("CDESC")) ;
                            				String cqty = StrUtils.fString((String)lineArr.get("QTY")) ;
                            				String cCUOM = StrUtils.fString((String)lineArr.get("CUOM")) ;
                            				
                            				Double childqty = Double.valueOf(qty) * Double.valueOf(cqty);
                            				
                            		%>
									
									<tr>
										<td class="text-center"></td>
										<td class="text-center"><%=cdesc %>(<%=cchilditem%>)</td>
										<td class="text-center"><%=Numbers.toMillionFormat(String.valueOf(childqty),Integer.valueOf(numberOfDecimal))%></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
										<td class="text-center"><%=cCUOM%></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
										<td class="text-center"></td>
									</tr>
									<% 
										}
									 }
										} %>
									
								<%i++;
								}
								
								if(doHdr.getITEM_RATES() == 1){
									subTotal = (subTotal*100)/(doHdr.getOUTBOUND_GST()+100);
								}
								
								double dorderdiscountcost=0;
								String orderdiscountcost="0";
								if(doHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")){
									dorderdiscountcost = (subTotal/100)*doHdr.getORDERDISCOUNT();
									orderdiscountcost = Numbers.toMillionFormat(dorderdiscountcost, numberOfDecimal);
								}else{
									dorderdiscountcost = doHdr.getORDERDISCOUNT();
									orderdiscountcost = Numbers.toMillionFormat(dorderdiscountcost, numberOfDecimal);
								}

								String discounttype = doHdr.getDISCOUNT_TYPE();
								String discount = "0";
								double pdiscount=0;
								if(discounttype.equalsIgnoreCase("%")){
									double dDiscount = doHdr.getDISCOUNT();
									pdiscount = (subTotal/100)*dDiscount;
									discount = Numbers.toMillionFormat(pdiscount, numberOfDecimal);
								}else{
									double dDiscount = doHdr.getDISCOUNT();
									pdiscount = doHdr.getDISCOUNT();
									discount = Numbers.toMillionFormat(dDiscount, numberOfDecimal);
								}
								
								
								if(doHdr.getTAXID() != 0){
									if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){
										
										totalTax = (subTotal/100)*doHdr.getOUTBOUND_GST();
										
										if(doHdr.getISSHIPPINGTAX() == 1){
											totalTax = totalTax + ((doHdr.getSHIPPINGCOST()/100)*doHdr.getOUTBOUND_GST());
										}
										
										if(doHdr.getISORDERDISCOUNTTAX() == 1){
											totalTax = totalTax - ((dorderdiscountcost/100)*doHdr.getOUTBOUND_GST());
										}
										
										if(doHdr.getISDISCOUNTTAX() == 1){
											totalTax = totalTax - ((pdiscount/100)*doHdr.getOUTBOUND_GST());
										}
									}
								}
								
								double totalAmount = (subTotal + totalTax + doHdr.getADJUSTMENT()+doHdr.getSHIPPINGCOST())-(dorderdiscountcost+pdiscount);
								
								double dTotalCost ="".equals(totalAmount) ? 0.0d : totalAmount;
								InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
								double orderadvanceamt = invoicePaymentDAO.getcreditamoutusingorderno(plant, doHdr.getDONO());
								double paidamountfororder = invoicePaymentDAO.getpaidamoutusingorderno(plant, doHdr.getDONO());	
								double pdcamount = 0.0;
								
								List<InvPaymentDetail> invdetlist = invoicePaymentDAO.getInvoicePaymentDetailsbydono(doHdr.getDONO(), plant, USERID);
								for(InvPaymentDetail invdet:invdetlist){
									Hashtable ht = new Hashtable();	
									ht.put("PAYMENTID",String.valueOf(invdet.getRECEIVEHDRID()));
									ht.put("PLANT",plant);
									List pdcDetailListpc = invoicePaymentDAO.getpdcbipayid(ht);
									for(int j =0; j < pdcDetailListpc.size(); j++) {
										Map pdcdet=(Map)pdcDetailListpc.get(j);
										String status = (String)pdcdet.get("STATUS");
										if(status.equalsIgnoreCase("NOT PROCESSED")) {
											pdcamount = pdcamount+(Double.parseDouble((String)pdcdet.get("CHEQUE_AMOUNT")) * Double.parseDouble((String)pdcdet.get("CURRENCYUSEQT")));
										}
									}
								}
								
								
								String invoiceAmount=StrUtils.addZeroes(((orderadvanceamt + paidamountfororder + pdcamount)), numberOfDecimal);
								
								String conv_invoiceAmount=StrUtils.addZeroes((Double.parseDouble(invoiceAmount)/(doHdr.getCURRENCYUSEQT())), numberOfDecimal);
								
								double baldue=(Double.valueOf(totalAmount)-Double.valueOf(invoiceAmount));
								String balamtrp = StrUtils.addZeroes(baldue, numberOfDecimal);
								String balduest = Numbers.toMillionFormat(baldue, numberOfDecimal);
								balduest = balduest.replace("-", "");
								String conv_balduest = Numbers.toMillionFormat(((baldue) / (doHdr.getCURRENCYUSEQT())), numberOfDecimal);
								
								%>
							</tbody>
						</table>
						<table id="table3" class="table">
							<tbody style="text-align:right;">
								<tr>
									<td></td>
									<td class="text-center">Qty Total</td>
									<td class="text-center"><%=StrUtils.addZeroes(dTotalQty, "3")%></td>
									<td class="text-center"><%=StrUtils.addZeroes(dTotalPickedQty, "3")%></td>
									<td class="text-center"><%=StrUtils.addZeroes(dTotalIssuedQty, "3")%></td>
									<td><%=lblSubTotal%></td>
									<td><%=Numbers.toMillionFormat(String.valueOf(subTotal),Integer.valueOf(numberOfDecimal))%></td>
								</tr>
								
								<%if(doHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")){%>
									<tr>
										<td colspan="6">
										Order Discount (<%=Numbers.toMillionFormat(doHdr.getORDERDISCOUNT(), "3")%>%)
										<br>
										<%if(doHdr.getISORDERDISCOUNTTAX() == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=orderdiscountcost%></td>
									</tr>
								<%}else{ %>
									<tr>
										<td colspan="6">
										Order Discount (<%=doHdr.getORDERDISCOUNTTYPE()%>)
										<br>
										<%if(doHdr.getISORDERDISCOUNTTAX() == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=orderdiscountcost%></td>
									</tr>
								<%} %>
								
								<%if(doHdr.getDISCOUNT_TYPE().equalsIgnoreCase("%")){%>
									<tr style="display:none"><!--  Author: Azees  Create date: July 5,2021  Description: Hide Discount -->
										<td colspan="6">
										Discount (<%=Numbers.toMillionFormat(doHdr.getDISCOUNT(), "3")%>%)
										<br>
										<%if(doHdr.getISDISCOUNTTAX() == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=discount%></td>
									</tr>
								<%}else{ %>
									<tr style="display:none"><!--  Author: Azees  Create date: July 5,2021  Description: Hide Discount -->
										<td colspan="6">
										Discount (<%=doHdr.getDISCOUNT_TYPE()%>)
										<br>
										<%if(doHdr.getISDISCOUNTTAX() == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=discount%></td>
									</tr>
								<%} %>
								
								<%-- <tr>
									<td colspan="6"><%=lblOrderdiscount%> (<%=Numbers.toMillionFormat(doHdr.getORDERDISCOUNT(), "3")%>%)</td>
									<td>(-) <%=orderdiscountcost%></td>
								</tr>
								<tr>
									<td colspan="6">Discount</td>
									<td>(-) <%=discount%></td>
								</tr> --%>
								<tr>
									<td colspan="6">
									<%=lblShippingcost%>
									<br>
										<%if(doHdr.getISSHIPPINGTAX() == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
									</td>
									<td><%=Numbers.toMillionFormat(doHdr.getSHIPPINGCOST(), numberOfDecimal)%></td>
								</tr>
								
								<%if(doHdr.getTAXID() != 0){
								if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td colspan="6"><%=taxdisplay%></td>
											<td><%=Numbers.toMillionFormat(totalTax, numberOfDecimal)%></td>
										</tr>
								<%}
								}%>
								
								<tr>
									<td colspan="6">Adjustment</td>
									<td><%=Numbers.toMillionFormat(doHdr.getADJUSTMENT(), numberOfDecimal)%></td>
								</tr>
								<tr>
									<td colspan="6"><b><%=lblTotal%> (<%=doHdr.getCURRENCYID()%>)</b></td>
									<td><b><%=Numbers.toMillionFormat(String.valueOf(totalAmount),Integer.valueOf(numberOfDecimal))%></b></td>
								</tr>
								<%if(!curency.equalsIgnoreCase(doHdr.getCURRENCYID())) {%><!--  Author: Azees  Create date: July 17,2021  Description:  Total of Local Currency -->
								<tr id="showtotalcur">
									<td colspan="6"><b><%=lblTotal%> (<%=curency%>)</b></td>
									<td><b><%=Numbers.toMillionFormat(String.valueOf(totalAmount/doHdr.getCURRENCYUSEQT()),Integer.valueOf(numberOfDecimal))%></b></td>
								</tr>
								<%}%>
								<%if(COMP_INDUSTRY.equalsIgnoreCase("Textile and Garment")){%>
								<tr>
							<td colspan="6"><%=lblPaymentMade%> (<%=doHdr.getCURRENCYID() %>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(invoiceAmount, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<%if(!curency.equalsIgnoreCase((String)doHdr.getCURRENCYID())) {%>
						<tr id="showtotalPay">
							<td colspan="6"><%=lblPaymentMade%> (<%=curency%>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(conv_invoiceAmount, numberOfDecimal) %></td>
						</tr>
						<%}%>
						<tr class="grey-bg" style="font-size:14px;">
							<td colspan="6"><b><%=lblBalanceDue %> (<%=doHdr.getCURRENCYID() %>)</b></td>
							<td><b><%=Numbers.toMillionFormat(balduest, Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<%if(!curency.equalsIgnoreCase((String)doHdr.getCURRENCYID())) {%>
						<tr class="grey-bg" style="font-size:14px;" id="showtotalBal">
							<td colspan="6"><b><%=lblBalanceDue %> (<%=curency%>)</b></td>
							<td><b><%=Numbers.toMillionFormat((baldue/(doHdr.getCURRENCYUSEQT())), numberOfDecimal) %></b></td>
						</tr>
						<%} }%>
							</tbody>
						</table>
					</div>
				</div>
			</span>
		</div>		
	</div>
</div>
<div id="deleteinvoice" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-sm">	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-body">
	        <div class="row">
			   <div class="col-lg-2">
			      <i>
			         <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xxlg-md icon-attention-circle" style="fill: red">
			            <path d="M256 32c30.3 0 59.6 5.9 87.2 17.6 26.7 11.3 50.6 27.4 71.2 48s36.7 44.5 48 71.2c11.7 27.6 17.6 56.9 17.6 87.2s-5.9 59.6-17.6 87.2c-11.3 26.7-27.4 50.6-48 71.2s-44.5 36.7-71.2 48C315.6 474.1 286.3 480 256 480s-59.6-5.9-87.2-17.6c-26.7-11.3-50.6-27.4-71.2-48s-36.7-44.5-48-71.2C37.9 315.6 32 286.3 32 256s5.9-59.6 17.6-87.2c11.3-26.7 27.4-50.6 48-71.2s44.5-36.7 71.2-48C196.4 37.9 225.7 32 256 32m0-32C114.6 0 0 114.6 0 256s114.6 256 256 256 256-114.6 256-256S397.4 0 256 0z"></path>
			            <circle cx="256" cy="384" r="32"></circle>
			            <path d="M256.3 96.3h-.6c-17.5 0-31.7 14.2-31.7 31.7v160c0 17.5 14.2 31.7 31.7 31.7h.6c17.5 0 31.7-14.2 31.7-31.7V128c0-17.5-14.2-31.7-31.7-31.7z"></path>
			         </svg>
			      </i>
			   </div>
			   <div class="col-lg-10" style="padding-left: 2px">
			      <p> Deleted Sales Order information cannot be retrieved. Are you sure about deleting ?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" id="cfmdelete" style="background:red;">
			        	Yes 
			         </button>
			         <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
			      </div>
			   </div>
			</div>
	      </div>
	    </div>
	  </div>
	</div>
<script>
	$(document).ready(function(){
		JsBarcode("#barCode", "<%=doHdr.getDONO()%>", {format: "CODE128",displayValue: false});	
		
		$("#so-delete").click(function() {
		  	var status = "<%=doHdr.getSTATUS()%>";			 
		  	var result="";
	  		if(status == "N") {
				$('#deleteinvoice').modal('show');			
			}else {
				$("#err-msg").html("Order already marked as 'PROCESSED or PARTIALLY PROCESSED' not allow to delete.");
				$(".alert-danger").show();
				setTimeout(function() {
				    $('.alert').fadeOut('fast');
				}, 2000);
			}
		 });
		
		$("#cfmdelete").click(function(){
		    $.ajax({
		        type: 'POST',
		        url: "../salesorder/delete?dono=<%=doHdr.getDONO()%>",
			    contentType: false,
			    processData: false,
		        success: function (data) {
		        	window.location.href="../salesorder/summary?msg="+data.MESSAGE;
		        },
		        error: function (data) {
		            alert(data.responseText);
		        }
		    });
	        return false; 
		});
	});
	
	function submitFormPrint(actionvalue){
		window.open(
				"/track/deleveryorderservlet?Submit="+actionvalue+"&DONO=<%=doHdr.getDONO()%>",
				  '_blank' // <- This is what makes it open in a new window.
				);
	}
	function submitForm(actionvalue)
	{
		window.open(
				"/track/deleveryorderservlet?Submit="+actionvalue+"&DONO=<%=doHdr.getDONO()%>&DIRTYPE=OUTBOUND"
				);
	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>