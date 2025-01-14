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
	String title = "Packing List/Delivery Note Detail";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	PackingDAO packingDAO = new PackingDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String dnplHdrId = StrUtils.fString(request.getParameter("HID"));
	String status = StrUtils.fString(request.getParameter("STATUS"));
	Hashtable ht = new Hashtable();
	ht.put("ID", dnplHdrId);
	ht.put("PLANT", plant);
	List dnplHdrList =  packingDAO.getDnplHdrById(ht);
	Map dnplHdr=(Map)dnplHdrList.get(0);
	List dnplShippingList =  packingDAO.getDnplShippingById(ht);
	ht.put("HDRID", dnplHdrId);
	List dnplDetList =  packingDAO.getDnplDetByHdrId(ht);
	String SHIPSTATUS="",SHIPDATE="",INTRANSITSTATUS="",INTRANSITDATE="",DELIVERYSTATUS="",DELIVERYDATE="",TRANSPORT="",CLEARAGENT="",CONTACTNAME="",TRACKINGNO="",FREIGHT="",JOURNEY="",CARRIER="";
	   String sSTATUS="",STATUSDATE="",SHIPDNPLID="",transportmode = "";
	if(dnplShippingList.size()>0){
	Map m=(Map)dnplShippingList.get(0);
	 SHIPSTATUS =(String)m.get("SHIPSTATUS");
     SHIPDATE =(String)m.get("SHIPDATE");
     INTRANSITSTATUS =(String)m.get("INTRANSITSTATUS");
     INTRANSITDATE =(String)m.get("INTRANSITDATE");
     DELIVERYSTATUS =(String)m.get("DELIVERYSTATUS");
     DELIVERYDATE =(String)m.get("DELIVERYDATE");
     TRANSPORT =(String)m.get("TRANSPORT");
     CLEARAGENT =(String)m.get("CLEARAGENT");
     CONTACTNAME =(String)m.get("CONTACTNAME");
     TRACKINGNO =(String)m.get("TRACKINGNO");
     FREIGHT =(String)m.get("FREIGHT");
     JOURNEY =(String)m.get("JOURNEY");
     CARRIER =(String)m.get("CARRIER");
     SHIPDNPLID  =(String)m.get("SHIPDNPLID");
     int trans = Integer.valueOf(TRANSPORT);
     	TransportModeDAO transportmodedao = new TransportModeDAO();
		if(trans > 0){
			transportmode = transportmodedao.getTransportModeById(plant, Integer.valueOf(TRANSPORT));
		}else{
			transportmode = "";
		}
     
     if(!SHIPSTATUS.equalsIgnoreCase("")){
  	   sSTATUS = SHIPSTATUS;
  	   STATUSDATE = SHIPDATE;
     }
     if(!INTRANSITSTATUS.equalsIgnoreCase("")){
  	   sSTATUS = INTRANSITSTATUS;
  	   STATUSDATE = INTRANSITDATE;
     }
     if(!DELIVERYSTATUS.equalsIgnoreCase("")){
  	   sSTATUS = DELIVERYSTATUS;
  	   STATUSDATE = DELIVERYSTATUS;
     }
	}
	ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
	Map plntMap = (Map) plntList.get(0);
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
	
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	File checkImageFile = new File(imagePath);
	if (!checkImageFile.exists()) {
		imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	}
	double dTotalQty = 0;
	
	ArrayList arrCust = new CustUtil().getCustomerDetails((String) dnplHdr.get("CUSTNO"),plant);
	String to_CompanyName = (String)arrCust.get(1);
	String to_RcbNo = (String) arrCust.get(23);
	String to_BlockAddress = (String)arrCust.get(2) + " " + (String)arrCust.get(3);
	String to_RoadAddress = (String)arrCust.get(4) + " " + (String)arrCust.get(14);
	String to_State = (String)arrCust.get(22);
	String to_Country = (String)arrCust.get(5);
	String to_Zip = (String)arrCust.get(6);
	String to_Attention = (String)arrCust.get(9);
	String to_TelNo = (String)arrCust.get(11);
	String to_Fax = (String)arrCust.get(13);
	String to_Email = (String)arrCust.get(14);
	String to_Remarks = (String)arrCust.get(15);
	
	String shipId = (String) dnplHdr.get("SHIPPINGID");
	String ship_CompanyName = "", ship_BlockAddress = "", ship_RoadAddress = "",
			ship_State = "", ship_Country = "", ship_Zip = "", ship_Attention = "",
			ship_TelNo = "";
	if(shipId.length()>0){
		ArrayList arrShipCust = new CustUtil().getCustomerDetails(shipId, plant);	
		ship_CompanyName = (String)arrShipCust.get(1);
		ship_BlockAddress = (String)arrShipCust.get(2) + " " + (String)arrShipCust.get(3);
		ship_RoadAddress = (String)arrShipCust.get(4) + " " + (String)arrShipCust.get(14);
		ship_State = (String)arrShipCust.get(22);
		ship_Country = (String)arrShipCust.get(5);
		ship_Zip = (String)arrShipCust.get(6);
		ship_Attention = (String)arrShipCust.get(9);
		ship_TelNo = (String)arrShipCust.get(11);
	}
	
	String InboundOrderHeader = "", InvoiceOrderToHeader = "", shipto = "", OrderNo = "", invoicedate = "",
			rcbno = "", customerrcbno = "", hscode = "", coo = "", remark1 = "", orderdiscountlbl = "",
			Discount = "", shippingcost = "", incoterm = "", Terms = "", SoNo = "", Item = "",
			OrderQty = "", UOM = "", Rate = "", totalafterdiscount = "", PreparedBy = "", Seller = "",
			SellerSignature = "", Buyer = "", BuyerSignature = "",SubTotal = "",Attention = "",
			Telephone = "", FaxLBL = "", Email = "",
			Total = "", printDetailDesc = "", printwithbrand = "", printwithhscode ="",printEmployee="",
			printWithDeliveryDate = "", printwithProduct ="",
			printwithcoo = "", Employee = "", duedate = "", Adjustment="",PaymentMade="",BalanceDue="";
	String PRINTPACKINGLIST="",PRINTDELIVERYNOTE="",InvoiceNo="",Gino="";
	Map invHdrDetails= new DOUtil().getDOReceiptInvoiceHdrDetailsDO(plant,"Tax Invoice English");
	Map DoDetails= new DOUtil().getDOReceiptHdrDetailsDO(plant,"Outbound order");
	if(!invHdrDetails.isEmpty()){
		InboundOrderHeader= (String) invHdrDetails.get("HDR1");
		InvoiceOrderToHeader = (String) invHdrDetails.get("HDR2");
		shipto = (String)invHdrDetails.get("SHIPTO");
		OrderNo = (String)invHdrDetails.get("ORDERNO");
		invoicedate = (String)invHdrDetails.get("INVOICEDATE");
		rcbno = (String)invHdrDetails.get("RCBNO");
		customerrcbno=(String)invHdrDetails.get("CUSTOMERRCBNO");
		hscode=(String)invHdrDetails.get("HSCODE");
		coo=(String)invHdrDetails.get("COO");
		remark1=(String)invHdrDetails.get("REMARK1");
		orderdiscountlbl=(String)invHdrDetails.get("ORDERDISCOUNT");
		Discount = (String)invHdrDetails.get("DISCOUNT");
		shippingcost=(String)invHdrDetails.get("SHIPPINGCOST");
		incoterm=(String)invHdrDetails.get("INCOTERM");
		Terms = (String) invHdrDetails.get("TERMS");
		SoNo = (String) invHdrDetails.get("SONO");
		Item = (String) invHdrDetails.get("ITEM");
		OrderQty = (String) invHdrDetails.get("ORDERQTY");
		UOM = (String) invHdrDetails.get("UOM");
		Rate = (String) invHdrDetails.get("RATE");
		totalafterdiscount=(String)invHdrDetails.get("TOTALAFTERDISCOUNT");
		PreparedBy = (String) invHdrDetails.get("PREPAREDBY");
		Seller = (String) invHdrDetails.get("SELLER");
		SellerSignature = (String) invHdrDetails.get("SELLERSIGNATURE");
		Buyer = (String) invHdrDetails.get("BUYER");
        BuyerSignature = (String) invHdrDetails.get("BUYERSIGNATURE");
        SubTotal = (String) invHdrDetails.get("SUBTOTAL");
        Total = (String) invHdrDetails.get("TOTAL");
        Employee=(String)invHdrDetails.get("EMPLOYEE");
        duedate=(String)invHdrDetails.get("DELIVERYDATE");
        Adjustment = (String)invHdrDetails.get("ADJUSTMENT");
	    PaymentMade = (String)invHdrDetails.get("PAYMENTMADE");
	    BalanceDue = (String)invHdrDetails.get("BALANCEDUE");
	    Attention = (String) invHdrDetails.get("ATTENTION");
	    Telephone = (String) invHdrDetails.get("TELEPHONE");
	    FaxLBL = (String) invHdrDetails.get("FAX");
	    Email = (String) invHdrDetails.get("EMAIL");
	    Gino = (String) invHdrDetails.get("GINO");
	    InvoiceNo = (String) invHdrDetails.get("INVOICENO");
        
        printDetailDesc = (String)invHdrDetails.get("PRINTXTRADETAILS");
        printwithbrand = (String)invHdrDetails.get("PRINTWITHBRAND");
        printwithhscode = (String)invHdrDetails.get("PRITNWITHHSCODE");
        printwithcoo = (String)invHdrDetails.get("PRITNWITHCOO");
        printEmployee=(String)invHdrDetails.get("PRINTEMPLOYEE");
        printWithDeliveryDate = (String) invHdrDetails.get("PRINTWITHDELIVERYDATE");
        printwithProduct = (String)invHdrDetails.get("PRINTWITHPRODUCT");
	}
        if(!DoDetails.isEmpty()){
        PRINTPACKINGLIST = (String)DoDetails.get("PRINTPACKINGLIST");
        PRINTDELIVERYNOTE = (String)DoDetails.get("PRINTDELIVERYNOTE");
	}
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="submenu" value="<%=IConstants.SALES_TRANSACTION%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script type="text/javascript" src="js/general.js"></script>
<script src="js/jspdf.debug.js"></script>
<script src="js/jspdf.plugin.autotable.js"></script>
<script src="js/JsBarcode.all.js"></script>
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
	<div class="box">
		<div class="box-header menu-drop">
		<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class="box-title pull-right">
				<div class="btn-group" role="group">
					<%if(PRINTPACKINGLIST.equals("1") || PRINTDELIVERYNOTE.equals("1")){%>
				<button type="button" class="btn btn-default" onclick="generatePDF()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<%}%>
				<%if(!status.equalsIgnoreCase("Delivered")){ %>
				<button type="button" class="btn btn-default" data-toggle="tooltip"  data-placement="bottom" title="Edit">
				<a href="../salestransaction/createpackinglist?HID=<%=(String) dnplHdr.get("HID")%>&INVOICENO=<%=(String) dnplHdr.get("INVOICENO")%>&GINO=<%=(String) dnplHdr.get("GINO")%>&DONO=<%=(String) dnplHdr.get("DONO")%>&custName=<%=(String) dnplHdr.get("CustName")%>&JobNum=&EDIT=EDIT">
				<i class="fa fa-pencil" aria-hidden="true"></i>
					</a>
					</button>
				<%} %>
					<%if(PRINTPACKINGLIST.equals("5")){%>
						<button type="button" class="btn btn-default printMe"
					 data-toggle="tooltip"  data-placement="bottom" title="Print Packing List">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
						<%} %>
						<%if(PRINTDELIVERYNOTE.equals("5")){%>
						<button type="button" class="btn btn-default printMeDN"
					 data-toggle="tooltip"  data-placement="bottom" title="Print Delivery Note">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
						<%} %>
				</div>
				<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../salestransaction/packinglistsummary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
			</div>
				
		</div>
		
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;" id="print_id1">
		<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-draft"><%=sSTATUS%></div>
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
				<span>
				<%=fromAddress_BlockAddress.trim()%></span>	<br>
				<span><%=fromAddress_RoadAddress.trim()%></span>	<br>
				<span><%=fromAddress_Country.trim()%> <%=ZIP.trim()%></span>	<br>
				<span><%=rcbno.trim()%> <%=RCBNO.trim()%></span>	<br>
				<span><%=Attention.trim()%> <%=NAME.trim()%></span>	<br>
				<span><%=Telephone.trim()%> <%=TELNO.trim()%> <%=FaxLBL.trim()%> <%=FAX.trim()%></span>	<br>
				<span><%=Email.trim()%> <%=EMAIL.trim()%></span>	<br>
				</span>
			</div>

			<div class="col-xs-6 text-right">
			<h1  id="headerpage"></h1>
 			
 			<div id="prtPackingList">
				<span>PACKING LIST</span>
				<h3 style="margin:0px;"># <%=dnplHdr.get("PLNO")%></h3>
				</div>
			<div id="prtDeliveryNote" style="bottom: -5px;position: inherit;">
				<span>DELIVERY NOTE</span>
				<h3 style="margin:0px;"># <%=dnplHdr.get("DNNO")%></h3>
				</div>
				
				<div id="prtPackingListbarcode" style="display:none">
				<figure class="pull-right text-center" >
					<img id="barCode" style="width:215px;height:55px;">
					<figcaption># <%=dnplHdr.get("PLNO")%></figcaption>
				</figure>
				</div>
				
				<div id="prtDeliveryNotebarcode" style="bottom: -5px;position: inherit;display: none">
				<figure class="pull-right text-center" >
					<img id="barCodeDN" style="width:215px;height:55px;">
					<figcaption># <%=dnplHdr.get("DNNO")%></figcaption>
				</figure>
				</div>
				
				<br style="clear:both">
			<%if(dnplHdr.get("DONO").equals("")|| dnplHdr.get("DONO") == null){ %>	
			
			<%}else{ %>
				<span><%=OrderNo%></span>
					<p style="margin:0px;"><b># <%=dnplHdr.get("DONO")%></b></p>
					<%} %>
					
			<%if(dnplHdr.get("GINO").equals("")|| dnplHdr.get("GINO") == null){ %>
			
			<%}else{ %>
				<span><%=Gino%></span>
					<p style="margin:0px;"><b># <%=dnplHdr.get("GINO")%></b></p>
					<%} %>
					
			<%if(dnplHdr.get("INVOICENO").equals("")|| dnplHdr.get("INVOICENO") == null){ %>
			
			<%}else{ %>
			<span><%=InvoiceNo%></span>
					<p style="margin:0px;"><b># <%=dnplHdr.get("INVOICENO")%></b></p>
			<%} %>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-7">
				<span><%=InvoiceOrderToHeader%></span>	<br>	
				<%if(to_CompanyName.trim().length()>0){%>
				<span><%=to_CompanyName%></span>	<br>	
				<%}if(to_RcbNo.trim().length()>0){%>
				<span><%=customerrcbno%> <%=to_RcbNo%></span>	<br>				
				<%}if(to_BlockAddress.trim().length()>0){%>
				<span><%=to_BlockAddress%></span>	<br>					
				<%}if(to_RoadAddress.trim().length()>0){%>
				<span><%=to_RoadAddress%></span>	<br>	
				<%}if(to_State.trim().length()>0 || to_Country.trim().length()>0  || to_Zip.trim().length()>0 ){%>
				<span><%=to_State%> <%=to_Country%> <%=to_Zip%></span>	<br>		
				<%}if(to_Email.trim().length()>0){%>
				<span>Email: <%=to_Email%></span>	<br>				
				<%}if(to_Remarks.trim().length()>0){%>
				<span>Remarks: <%=to_Remarks%></span>	<br>	
				<%}%>
				
				
				
			<%if(shipId.length()>0){%>		
				<br>
				<span><%=shipto%></span>	<br>	
				<%if(ship_CompanyName.trim().length()>0){%>
				<span><%=ship_CompanyName%></span>	<br>
				<%}if(ship_BlockAddress.trim().length()>0){%>
				<span><%=ship_BlockAddress%></span>	<br>	
				<%}if(ship_RoadAddress.trim().length()>0){%>
				<span><%=ship_RoadAddress%></span>	<br>
				<%}if(ship_State.trim().length()>0 || ship_Country.trim().length()>0  || ship_Zip.trim().length()>0 ){%>
				<span><%=ship_State%> <%=ship_Country%> <%=ship_Zip%></span>	<br>	
				<%}if(ship_Attention.trim().length()>0){%>
				<span>Attention: <%=ship_Attention%></span>	<br>		
				<%}if(ship_TelNo.trim().length()>0){%>
				<span>Tel: <%=ship_TelNo%></span>	<br>
				<%}%>
			<%}%>
				<br>
			</div>
			<div class="col-xs-5 text-right">
				<table id="table1" class="table pull-right">
					<tbody>
						<tr>
							<td>Date:</td>
							<td><%=dnplHdr.get("CollectionDate")%></td>
						</tr>
						<tr>
							<td>Freight Forwarder:</td>
							<td><%=FREIGHT%></td>
						</tr>
						<tr>
							<td>Transport:</td>
							<td><%=transportmode%></td>
						</tr>
						<tr>
							<td>Carrier Details:</td>
							<td><%=CARRIER%></td>
						</tr>
						<tr>
							<td>Duration of Journey:</td>
							<td><%=JOURNEY%></td>
						</tr>
						<tr>
							<td>Tracking No:</td>
							<td><%=TRACKINGNO%></td>
						</tr>
						<tr>
							<td><%=sSTATUS%> Date:</td>
							<td><%=STATUSDATE%></td>
						</tr>
						<tr>
							<td><%=PreparedBy%></td>
							<td><%=username%></td>
						</tr>
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
							<td><%=SoNo%></td>
							<td width="40%" ><%=Item%></td>
							<td><%=UOM%></td>
							<td><%=OrderQty%></td>
							<td>Net Weight</td>
							<td>Gross Weight</td>
							<td>Packing</td>
							<td>Dimension</td>
						</tr>
					</thead>
					<tbody>
					<%for(int i =0; i<dnplDetList.size(); i++) {   
				  		Map m=(Map)dnplDetList.get(i);
				  		String qty="";
				  		
			  			qty = (String) m.get("QTY");
			  			double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
			  			qty = StrUtils.addZeroes(dQty, "3");
			  			
			  			dTotalQty += dQty;
			  			
						String ITEM = (String) m.get("ITEM");
						ITEM += "<br>"+(String) m.get("ITEMDESC");
						String HSCODE = (String) m.get("HSCODE");
						String COO = (String) m.get("COO");
						String BRAND = (String) m.get("BRAND");
						String detailDesc = (String) m.get("DETAILDESC");
						String itemClass="";
						if(printDetailDesc.equals("1") && detailDesc.length() > 0){
							ITEM += "<br>"+detailDesc;
						}	
									
						if(printwithbrand.equals("1") && BRAND.length() > 0){
							ITEM +="<br>Brand: "+BRAND;
						}
						
						if(printwithhscode.equals("1") && HSCODE.length() > 0){
							ITEM +="<br>"+hscode+" "+HSCODE;
						}
						
						if(printwithcoo.equals("1") && COO.length() > 0){
							ITEM +="<br>"+coo+" "+COO;	
						}
						if(printwithProduct.equals("0")){
							itemClass = "no-print";
						}
			  		%>
				  		<tr>
							<td class="text-center"><%= (i+1) %></td>
							<td class="text-center"><span class="<%=itemClass%>"><%=ITEM %></span></td>
							<td class="text-center"><%=m.get("UOM")%></td>
							<td class="text-center"><%=qty%></td>
							<td class="text-center"><%=m.get("netweight")%></td>
							<td class="text-center"><%=m.get("grossweight")%></td>
							<td class="text-center"><%=m.get("packing")%></td>
							<td class="text-center"><%=m.get("dimension")%></td>
							
						</tr>
						
				  	<%}%>
					</tbody>
				</table>
				<table id="table3" class="table">
					<tbody style="text-align:right;">
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td>Qty Total</td>
							<td><%=StrUtils.addZeroes(dTotalQty, "3")%></td>
							<%-- <td class="text-center"><%=(String) dnplHdr.get("TOTALNETWEIGHT")%></td>
							<td class="text-center"><%=(String) dnplHdr.get("TOTALGROSSWEIGHT")%></td>
							<td class="text-center"><%=(String) dnplHdr.get("NETPACKING")%></td>
							<td class="text-center"><%=(String) dnplHdr.get("NETDIMENSION")%></td> --%>
							<td>Total Net Weight</td>
							<td><%=(String) dnplHdr.get("TOTALNETWEIGHT")%></td>
						</tr>
						<tr>
							<td colspan="7">Total Gross Weight</td>
							<td><%=(String) dnplHdr.get("TOTALGROSSWEIGHT")%></td>
						</tr>
						<tr>
							<td colspan="7">Total Packing</td>
							<td><%=(String) dnplHdr.get("NETPACKING")%></td>
						</tr>
						<tr>
							<td colspan="7">Total Dimension</td>
							<td><%=(String) dnplHdr.get("NETDIMENSION")%></td>
						</tr>
					</tbody>
				</table>
				<!-- <br> -->
				<footer>
				<table id="footerTable" class="table">		
					<tr>
						<td colspan="7" class="no-padding"><%=remark1%> <%=(String) dnplHdr.get("NOTE")%></td>
					</tr>	
					 <tr>
						<td colspan="7"></td>
					</tr> 		
					<!-- <tr>
						<td colspan="7"></td>
					</tr>		
					<tr>
						<td colspan="7"></td>
					</tr> -->	
					<tr>
						<td colspan="3" class="text-left no-padding">______________________________</td>
						<td class="no-padding"></td>
						<td colspan="3" class="text-right no-padding">______________________________</td>
					</tr>
					<tr>
						<td colspan="3" class="text-left no-padding"><%=Seller%></td>
						<td class="no-padding"></td>
						<td colspan="3" class="text-right no-padding"><%=SellerSignature%><br><br><br></td>
					</tr>
					
					<tr>
						<td colspan="3" class="text-left no-padding">______________________________</td>
						<td class="no-padding"></td>
						<td colspan="3" class="text-right no-padding">______________________________</td>
					</tr>
					<tr>
						<td colspan="3" class="text-left no-padding"><%=Buyer%></td>
						<td class="no-padding"></td>
						<td colspan="3" class="text-right no-padding"><%=BuyerSignature%></td>
					</tr>
				</table>
				</footer>
			</div>
		</div>
		</span>
		</div>
		
		<script>
			$(document).ready(function(){

			});

			function generatePDF() {
			    window.open("/track/DynamicFileServlet?action=printDnplWITHBATCH&DONO=<%=dnplHdr.get("DONO")%>",'_blank');
			}
			
		</script>
		
		<script type="text/javascript">
		/* @license 
		 * jQuery.print, version 1.6.0
		 *  (c) Sathvik Ponangi, Doers' Guild
		 * Licence: CC-By (http://creativecommons.org/licenses/by/3.0/)
		 *--------------------------------------------------------------------------*/
		(function ($) {
		    "use strict";
		    // A nice closure for our definitions

		    function jQueryCloneWithSelectAndTextAreaValues(elmToClone, withDataAndEvents, deepWithDataAndEvents) {
		        // Replacement jQuery clone that also clones the values in selects and textareas as jQuery doesn't for performance reasons - https://stackoverflow.com/questions/742810/clone-isnt-cloning-select-values
		        // Based on https://github.com/spencertipping/jquery.fix.clone
		        var $elmToClone = $(elmToClone),
		            $result           = $elmToClone.clone(withDataAndEvents, deepWithDataAndEvents),
		            $my_textareas     = $elmToClone.find('textarea').add($elmToClone.filter('textarea')),
		            $result_textareas = $result.find('textarea').add($result.filter('textarea')),
		            $my_selects       = $elmToClone.find('select').add($elmToClone.filter('select')),
		            $result_selects   = $result.find('select').add($result.filter('select'));

		        for (var i = 0, l = $my_textareas.length; i < l; ++i) {
		            $($result_textareas[i]).val($($my_textareas[i]).val());
		        }
		        for (var i = 0, l = $my_selects.length;   i < l; ++i) {
		            for (var j = 0, m = $my_selects[i].options.length; j < m; ++j) {
		                if ($my_selects[i].options[j].selected === true) {
		                    $result_selects[i].options[j].selected = true;
		                }
		            }
		        }
		        return $result;
		    }

		    function getjQueryObject(string) {
		        // Make string a vaild jQuery thing
		        var jqObj = $("");
		        try {
		            jqObj = jQueryCloneWithSelectAndTextAreaValues(string);
		        } catch (e) {
		            jqObj = $("<span />")
		                .html(string);
		        }
		        return jqObj;
		    }

		    function printFrame(frameWindow, content, options) {
		        // Print the selected window/iframe
		        var def = $.Deferred();
		        try {
		            frameWindow = frameWindow.contentWindow || frameWindow.contentDocument || frameWindow;
		            var wdoc = frameWindow.document || frameWindow.contentDocument || frameWindow;
		            if(options.doctype) {
		                wdoc.write(options.doctype);
		            }
		            wdoc.write(content);
		            wdoc.close();
		            var printed = false;
		            var callPrint = function () {
		                if(printed) {
		                    return;
		                }
		                // Fix for IE : Allow it to render the iframe
		                frameWindow.focus();
		                try {
		                    // Fix for IE11 - printng the whole page instead of the iframe content
		                    if (!frameWindow.document.execCommand('print', false, null)) {
		                        // document.execCommand returns false if it failed -http://stackoverflow.com/a/21336448/937891
		                        frameWindow.print();
		                    }
		                    // focus body as it is losing focus in iPad and content not getting printed
		                    $('body').focus();
		                } catch (e) {
		                    frameWindow.print();
		                }
		                frameWindow.close();
		                printed = true;
		                def.resolve();
		            }
		            // Print once the frame window loads - seems to work for the new-window option but unreliable for the iframe
		            $(frameWindow).on("load", callPrint);
		            // Fallback to printing directly if the frame doesn't fire the load event for whatever reason
		            setTimeout(callPrint, options.timeout);
		        } catch (err) {
		            def.reject(err);
		        }
		        return def;
		    }

		    function printContentInIFrame(content, options) {
		        var $iframe = $(options.iframe + "");
		        var iframeCount = $iframe.length;
		        if (iframeCount === 0) {
		            // Create a new iFrame if none is given
		            $iframe = $('<iframe height="0" width="0" border="0" wmode="Opaque"/>')
		                .prependTo('body')
		                .css({
		                    "position": "absolute",
		                    "top": -999,
		                    "left": -999
		                });
		        }
		        var frameWindow = $iframe.get(0);
		        return printFrame(frameWindow, content, options)
		            .done(function () {
		                // Success
		                setTimeout(function () {
		                    // Wait for IE
		                    if (iframeCount === 0) {
		                        // Destroy the iframe if created here
		                        $iframe.remove();
		                    }
		                }, 1000);
		            })
		            .fail(function (err) {
		                // Use the pop-up method if iframe fails for some reason
		                console.error("Failed to print from iframe", err);
		                printContentInNewWindow(content, options);
		            })
		            .always(function () {
		                try {
		                    options.deferred.resolve();
		                } catch (err) {
		                    console.warn('Error notifying deferred', err);
		                }
		            });
		    }

		    function printContentInNewWindow(content, options) {
		        // Open a new window and print selected content
		        var frameWindow = window.open();
		        return printFrame(frameWindow, content, options)
		            .always(function () {
		                try {
		                    options.deferred.resolve();
		                } catch (err) {
		                    console.warn('Error notifying deferred', err);
		                }
		            });
		    }

		    function isNode(o) {
		        /* http://stackoverflow.com/a/384380/937891 */
		        return !!(typeof Node === "object" ? o instanceof Node : o && typeof o === "object" && typeof o.nodeType === "number" && typeof o.nodeName === "string");
		    }
		    $.print = $.fn.print = function () {
		        // Print a given set of elements
		        var options, $this, self = this;
		        // console.log("Printing", this, arguments);
		        if (self instanceof $) {
		            // Get the node if it is a jQuery object
		            self = self.get(0);
		        }
		        if (isNode(self)) {
		            // If `this` is a HTML element, i.e. for
		            // $(selector).print()
		            $this = $(self);
		            if (arguments.length > 0) {
		                options = arguments[0];
		            }
		        } else {
		            if (arguments.length > 0) {
		                // $.print(selector,options)
		                $this = $(arguments[0]);
		                if (isNode($this[0])) {
		                    if (arguments.length > 1) {
		                        options = arguments[1];
		                    }
		                } else {
		                    // $.print(options)
		                    options = arguments[0];
		                    $this = $("html");
		                }
		            } else {
		                // $.print()
		                $this = $("html");
		            }
		        }
		        // Default options
		        var defaults = {
		            globalStyles: true,
		            mediaPrint: false,
		            stylesheet: null,
		            noPrintSelector: ".no-print",
		            iframe: true,
		            append: null,
		            prepend: null,
		            manuallyCopyFormValues: true,
		            deferred: $.Deferred(),
		            timeout: 750,
		            title: null,
		            doctype: '<!doctype html>'
		        };
		        // Merge with user-options
		        options = $.extend({}, defaults, (options || {}));
		        var $styles = $("");
		        if (options.globalStyles) {
		            // Apply the stlyes from the current sheet to the printed page
		            $styles = $("style, link, meta, base, title");
		        } else if (options.mediaPrint) {
		            // Apply the media-print stylesheet
		            $styles = $("link[media=print]");
		        }
		        if (options.stylesheet) {
		            // Add a custom stylesheet if given
		            $styles = $.merge($styles, $('<link rel="stylesheet" href="' + options.stylesheet + '">'));
		        }
		        // Create a copy of the element to print
		        var copy = jQueryCloneWithSelectAndTextAreaValues($this);
		        // Wrap it in a span to get the HTML markup string
		        copy = $("<span/>")
		            .append(copy);
		        // Remove unwanted elements
		        copy.find(options.noPrintSelector)
		            .remove();
		        // Add in the styles
		        copy.append(jQueryCloneWithSelectAndTextAreaValues($styles));
		        // Update title
		        if (options.title) {
		            var title = $("title", copy);
		            if (title.length === 0) {
		                title = $("<title />");
		                copy.append(title);                
		            }
		            title.text(options.title);            
		        }
		        // Appedned content
		        copy.append(getjQueryObject(options.append));
		        // Prepended content
		        copy.prepend(getjQueryObject(options.prepend));
		        if (options.manuallyCopyFormValues) {
		            // Manually copy form values into the HTML for printing user-modified input fields
		            // http://stackoverflow.com/a/26707753
		            copy.find("input")
		                .each(function () {
		                    var $field = $(this);
		                    if ($field.is("[type='radio']") || $field.is("[type='checkbox']")) {
		                        if ($field.prop("checked")) {
		                            $field.attr("checked", "checked");
		                        }
		                    } else {
		                        $field.attr("value", $field.val());
		                    }
		                });
		            copy.find("select").each(function () {
		                var $field = $(this);
		                $field.find(":selected").attr("selected", "selected");
		            });
		            copy.find("textarea").each(function () {
		                // Fix for https://github.com/DoersGuild/jQuery.print/issues/18#issuecomment-96451589
		                var $field = $(this);
		                $field.text($field.val());
		            });
		        }
		        // Get the HTML markup string
		        var content = copy.html();
		        // Notify with generated markup & cloned elements - useful for logging, etc
		        try {
		            options.deferred.notify('generated_markup', content, copy);
		        } catch (err) {
		            console.warn('Error notifying deferred', err);
		        }
		        // Destroy the copy
		        copy.remove();
		        if (options.iframe) {
		            // Use an iframe for printing
		            try {
		                printContentInIFrame(content, options);
		            } catch (e) {
		                // Use the pop-up method if iframe fails for some reason
		                console.error("Failed to print from iframe", e.stack, e.message);
		                printContentInNewWindow(content, options);
		            }
		        } else {
		            // Use a new window for printing
		            printContentInNewWindow(content, options);
		        }
		        return this;
		    };
		})(jQuery);
		</script>
		<script>
			$(document).ready(function(){
				document.getElementById('headerpage').innerHTML="PACKING LIST / DELIVERY NOTE";
				setTimeout(function() {
				    $('.alert').fadeOut('fast');
				}, 2000);
			  $('[data-toggle="tooltip"]').tooltip();
			  JsBarcode("#barCode", "<%=dnplHdr.get("PLNO")%>", {format: "CODE128",displayValue: false});
			  JsBarcode("#barCodeDN", "<%=dnplHdr.get("DNNO")%>", {format: "CODE128",displayValue: false});
			  $('.printMe').click(function(){
				  if($("#table2").height() > 370){
					  $("#table3").addClass("page-break-before");
				  }else{
					  $("#table3").removeClass("page-break-before");
				  }
				  		document.getElementById('headerpage').innerHTML="PACKING LIST";
				  		document.getElementById("prtDeliveryNote").style.display = "none";
				  		document.getElementById("prtPackingList").style.display = "none";
				  		document.getElementById("prtPackingListbarcode").style.display = "block";
				     $("#print_id").print();
				     	document.getElementById("prtPackingListbarcode").style.display = "none";
				     	document.getElementById("prtDeliveryNote").style.display = "block";
				     	document.getElementById("prtPackingList").style.display = "block";
				     	document.getElementById('headerpage').innerHTML="PACKING LIST / DELIVERY NOTE";
				});
			
			$('.printMeDN').click(function(){
				  if($("#table2").height() > 370){
					  $("#table3").addClass("page-break-before");
				  }else{
					  $("#table3").removeClass("page-break-before");
				  }
				  		document.getElementById('headerpage').innerHTML="DELIVERY NOTE";
				  		document.getElementById("prtDeliveryNote").style.display = "none";
				  		document.getElementById("prtPackingList").style.display = "none";
				  		document.getElementById("prtDeliveryNotebarcode").style.display = "block";
				  		
				     $("#print_id").print();
				     document.getElementById("prtDeliveryNotebarcode").style.display = "none";
				     	document.getElementById("prtPackingList").style.display = "block";
				     	document.getElementById("prtDeliveryNote").style.display = "block";
				     	document.getElementById('headerpage').innerHTML="PACKING LIST / DELIVERY NOTE";
				});
			});
		</script>
	</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>