 <%@page import="com.track.db.object.estDet"%>
<%@page import="com.track.db.object.estHdr"%>
<%@page import="com.track.db.object.FinProject"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Sales Estimate Detail";
    String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
    String USERID= session.getAttribute("LOGIN_USER").toString();
    String systatus = session.getAttribute("SYSTEMNOW").toString();
	estHdr esthdr = (estHdr)request.getAttribute("estHdr");
	Map plntMap = (Map)request.getAttribute("PLNTMAP");
	Map esthdrDetails = (Map)request.getAttribute("ESTHDRDETAILS");
	List<estDet> estdetDetails = (ArrayList<estDet>)request.getAttribute("estDetList");
	ArrayList custDetails = (ArrayList)request.getAttribute("CUSTDETAILS");
	ArrayList shippingCustDetails = (ArrayList)request.getAttribute("SHIPPINGCUSTDETAILS");
	String imagePath = (String)request.getAttribute("IMAGEPATH");
	String numberOfDecimal = (String)request.getAttribute("NUMBEROFDECIMAL");
	String currency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
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
			displayByOrderType = "";
	
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
	String companyregnumber = (String) plntMap.get("companyregnumber");
	
	double shingpercentage =0.0;
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	if(!esthdrDetails.isEmpty()){
		orderDesc = (String) esthdrDetails.get("HDR1");
		rcbno = (String)esthdrDetails.get("RCBNO");
		Telephone = (String)esthdrDetails.get("TELEPHONE");
		FaxLBL = (String) esthdrDetails.get("FAX");
	    Email = (String) esthdrDetails.get("EMAIL");
	    toHeader = (String) esthdrDetails.get("HDR2");
	    fromHeader = (String) esthdrDetails.get("HDR3");
	    customerRcbno = (String) esthdrDetails.get("CUSTOMERRCBNO");	  
	    shipTo = (String) esthdrDetails.get("SHIPTO");  
	    terms = (String) esthdrDetails.get("TERMS");
	    date = (String) esthdrDetails.get("DATE");
	    lblDeliveryDate = (String) esthdrDetails.get("DELIVERYDATE");
	    lblINCOTERM = (String) esthdrDetails.get("INCOTERM");
	    lblEmployee = (String) esthdrDetails.get("EMPLOYEENAME");
	    lblPreBy = (String) esthdrDetails.get("PREPAREDBY");
	    lblSoNo = (String) esthdrDetails.get("SONO");
	    lblItem = (String) esthdrDetails.get("ITEM");
	    lblOrderQty = (String) esthdrDetails.get("ORDERQTY");
	    lblUom = (String) esthdrDetails.get("UOM");
	    lblRate = (String) esthdrDetails.get("RATE");
	    lblDiscount = (String) esthdrDetails.get("DISCOUNT");
	    lblAmt = (String) esthdrDetails.get("AMT");
	    lblOrderdiscount = (String) esthdrDetails.get("ORDERDISCOUNT");
	    lblShippingcost = (String) esthdrDetails.get("SHIPPINGCOST");
	    lblTotalDiscount = (String) esthdrDetails.get("TOTALAFTERDISCOUNT");
	    lblTotal = (String) esthdrDetails.get("TOTAL");
	    lblHscode = (String) esthdrDetails.get("HSCODE"); 
	    lblCoo = (String) esthdrDetails.get("COO"); 
	    
	    uenno = (String) esthdrDetails.get("UENNO");
		customeruenno = (String) esthdrDetails.get("CUSTOMERUENNO");
		printuenno = (String)esthdrDetails.get("PRINTWITHUENNO");
        printcustomeruenno = (String)esthdrDetails.get("PRINTWITHCUSTOMERUENNO");
	    
	    prdXtraDetails = (String) esthdrDetails.get("PRINTXTRADETAILS");
	    printWithBrand = (String) esthdrDetails.get("PRINTWITHBRAND");
	    printWithHscode = (String) esthdrDetails.get("PRINTWITHHSCODE");
	    printWithCoo = (String) esthdrDetails.get("PRINTWITHCOO");
	    printWithProductDeliveryDate = (String) esthdrDetails.get("PRINTWITHPRODUCTDELIVERYDATE");
	    printWithProductRemarks = (String) esthdrDetails.get("PRINTWITHPRODUCTREMARKS");
	    
	    displayByOrderType = (String) esthdrDetails.get("DISPLAYBYORDERTYPE");
	}
	double dTotalQty = 0,totalTax = 0, subTotal = 0,dTotalQtyis=0;
	List ItemList = (ArrayList)request.getAttribute("ItemList");
	List taxList =new ArrayList();
	
	FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
	FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(esthdr.getTAXID());

	 FinProjectDAO finProjectDAO = new FinProjectDAO();
		FinProject finProject=new FinProject();
		String projectname = "";
		if(esthdr.getPROJECTID() > 0){
			finProject = finProjectDAO.getFinProjectById(plant, esthdr.getPROJECTID());
			projectname = finProject.getPROJECT_NAME();
		}
		String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.ESTIMATE_ORDER%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ESTIMATE_ORDER%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
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
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../salesestimate/summary"><span class="underline-on-hover">Sales Estimate Summary</span> </a></li>
                <li><label>Sales Estimate Detail</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
				<%if(displaySummaryEdit){ %>
					<button type="button" class="btn btn-default" data-toggle="tooltip"  
						data-placement="bottom" title="Edit">
						<a href="../salesestimate/edit?estno=<%=esthdr.getESTNO()%>"><i class="fa fa-pencil" aria-hidden="true"></i></a>
					</button>
					<%}%>
				</div>
				<div class="btn-group" role="group">
				<%if(displayPrintPdf){ %>
					<button type="button" class="btn btn-default" data-toggle="dropdown" >Print 
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu" style="min-width: 0px;">
						<!-- <li id="" onClick="submitFormPrint('Print Estimate Order');"><a href="#">Print Sales Estimate</a></li> -->
						  <li id="" onClick="submitFormPrint('Print Estimate Order With Price');"><a href="#">Print Sales Estimate Order With Price</a></li>
						<li id=""onClick="submitForm('Export To Excel');"><a href="#">Export to Excel</a></li>
				  	</ul>
				  	<%}%>
				</div>
				<div class="btn-group" role="group">
				<%if(displayMore){ %>
					<button type="button" class="btn btn-default" data-toggle="dropdown" >More 
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu" style="min-width: 0px;">
						<li id=""><a href="../salesestimate/copy?estno=<%=esthdr.getESTNO()%>">Copy</a></li>
						<%if(!esthdr.getORDER_STATUS().equalsIgnoreCase("PROCESSED") && !esthdr.getORDER_STATUS().equalsIgnoreCase("Draft")){%>
						<li id=""><a href="../salesestimate/convertToSales?estno=<%=esthdr.getESTNO()%>">Convert To Sales</a></li>
						<%} %>
					    <li id="so-delete"><a href="#">Delete</a></li>
				  	</ul>
				  	<%} %>
				</div>
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../salesestimate/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;" id="print_id1">
			<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
				<div class="ribbon-inner ribbon-draft"><%=esthdr.getORDER_STATUS()%></div>
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
								<h1 id="headerpage"><%=esthdr.getORDERTYPE()%></h1>
							<%}else{%>
								<h1 id="headerpage"><%=orderDesc%></h1>
							<%} %>
						<figure class="pull-right text-center">
							<img id="barCode" style="width:215px;height:65px;">
							<figcaption># <%=esthdr.getESTNO()%></figcaption>
						</figure>
						<br style="clear:both"> 
					</div>
				</div>
				<div class="row">
					<div class="col-xs-7">
					<%if(custDetails.size()>0){%>
					<span><%=toHeader%></span>	<br>
					<span><%=custDetails.get(1)%></span>	<br>
					<%if(printcustomeruenno.equals("1")){%>  							
					<span><%=customeruenno%> <%=custDetails.get(25)%></span><%}%>
					<span><%=customerRcbno%> <%=custDetails.get(24)%></span>	<br>
					<span><%=custDetails.get(2)%> <%=custDetails.get(3)%></span>	<br> <!-- Block Address (Add1 + Add2) -->
					<span><%=custDetails.get(4)%> <%=custDetails.get(16)%></span>	<br> <!-- Road Address (Add3 + Add4) -->
					<span><%=custDetails.get(5)%> <%=custDetails.get(22)%> <%=custDetails.get(6)%></span>	<br> <!-- Country + State + Zip -->
					<span>Email: <%=custDetails.get(14)%></span>	<br>
					<span>Remarks: <%=custDetails.get(15)%></span>	<br>
					<%}%>
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
					<%if(custDetails.size()>0){%>
					<span><%=terms%> <%=custDetails.get(19)%>  <%=custDetails.get(20)%></span><br>
					<%} %>
					<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
					<span>Project : <%=projectname%></span><br>
					<%} %>
					</div>
					<div class="col-xs-5 text-right">
						<table id="table1" class="table pull-right">
							<tbody>
								<tr>
									<td><%=date%></td>
									<td><%=esthdr.getORDDATE()%></td>
								</tr>
								<%-- <%if(printWithDeliveryDate.equals("1") && ((String)invoiceHdr.get("DUE_DATE")).length() > 0){%> --%>
								<tr>
									<td><%=lblDeliveryDate%></td>
									<td><%=esthdr.getDELIVERYDATE()%></td>
								</tr>
								<%-- <%} %> --%>
								<tr>
									<td><%=lblINCOTERM%></td>
									<td><%=esthdr.getINCOTERMS()%></td>
								</tr>
								<%-- <%if(printEmployee.equals("1") && ((String)invoiceHdr.get("EMPNO")).length() > 0){%> --%>
								<tr>
									<td><%=lblEmployee%></td>
									<td><%=esthdr.getEMPNO()%></td>
								</tr>
								<%-- <%} %> --%>
								<tr>
									<td><%=lblPreBy%></td>
									<td><%=esthdr.getCRBY()%></td>
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
									<td><%=lblSoNo%></td>
									<td><%=lblItem%></td>
									<td><%=lblOrderQty%></td>
									<td>Converted Qty</td>
									<td>Balance Qty</td>
									<td><%=lblUom%></td>
									<td><%=lblRate%></td>
									<td><%=lblDiscount%></td>
									<td class="text-right"><%=lblAmt%></td>
								</tr>
							</thead>
							<tbody>
								<%
								int i = 0;
								String taxdisplay="";
								for(estDet estdet :estdetDetails){
									Map m = (Map)ItemList.get(i);
									String qty="", unitPrice="", amount="", percentage="", tax="",
											item_discounttype = "", lineDiscount = "",
											 cost="",qtyis="",balq="";
									double discount = 0, dTax = 0;
									item_discounttype = estdet.getDISCOUNT_TYPE();
									qty = estdet.getQTYOR().toString();
									double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
						  			qty = StrUtils.addZeroes(dQty, "3");
						  			dTotalQty += dQty;
						  			
						  			qtyis = estdet.getQTYIS().toString();
									double dQtyis ="".equals(qtyis) ? 0.0d :  Double.parseDouble(qtyis);
									qtyis = StrUtils.addZeroes(dQtyis, "3");
						  			dTotalQtyis += dQtyis;						  			
						  			
						  			double dCost = estdet.getUNITPRICE() * estdet.getCURRENCYUSEQT();
						  			unitPrice = Numbers.toMillionFormat(dCost, numberOfDecimal);
						  			double item_amount = (dCost*dQty);
						  			
						  			if(item_discounttype.equalsIgnoreCase("%")){
										double dDiscount = estdet.getDISCOUNT();
										discount = ((item_amount)/100)*dDiscount;
										lineDiscount = Numbers.toMillionFormat(dDiscount, "3");
									}else{
										discount = estdet.getDISCOUNT() * estdet.getCURRENCYUSEQT();
										lineDiscount = Numbers.toMillionFormat(discount, numberOfDecimal);
									}
						  			item_amount = item_amount - discount;
						  			
						  			double balqty = dQty-dQtyis;
						  			balq = StrUtils.addZeroes(balqty, "3");
						  			
						  			taxdisplay = estdet.getTAX_TYPE();
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
						  			tax = StrUtils.addZeroes(dTax, numberOfDecimal);
						  					
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
						  						tax = StrUtils.addZeroes(dTax, numberOfDecimal);
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
									<tr>
										<td class="text-center"><%=estdet.getESTLNNO() %></td>
										<td class="text-center">
										<%=estdet.getITEM()%>
										<%if(printWithBrand.equals("1")){ %>
											<br/>Brand: <%=m.get("brand")%>
										<%} %>
										<%if(printWithHscode.equals("1")){ %>
											<br/><%=lblHscode%> <%=m.get("hscode")%>
										<%} %>
										<%if(printWithCoo.equals("1")){ %>
											<br/><%=lblCoo%> <%=m.get("coo")%>
										<%} %>
										<%if(printWithProductDeliveryDate.equals("1")){ %>
											<%=lblProductDeliveryDate%> 
										<%} %>
										</td>
										<td class="text-center"><%=Numbers.toMillionFormat(qty, Integer.valueOf(numberOfDecimal))%></td>
										<td class="text-center"><%=qtyis%></td>
										<td class="text-center"><%=Numbers.toMillionFormat(balq, Integer.valueOf(numberOfDecimal))%></td>
										<td class="text-center"><%=estdet.getUNITMO()%></td>
										<td class="text-center"><%=unitPrice%></td>
										<td class="text-center"><%=lineDiscount%><%=item_discounttype%></td>
										<td class="text-right"><%=Numbers.toMillionFormat(item_amount, numberOfDecimal)%></td>
									</tr>
									
						<tr>
          					<td></td>
          					<td colspan="8">&emsp;&emsp;&emsp;<%=estdet.getItemDesc()%></td>
						</tr>
								<%i++;
								}
								
								if(esthdr.getITEM_RATES() == 1){
									subTotal = (subTotal*100)/(esthdr.getOUTBOUND_GST()+100);
								}
								
								double dorderdiscountcost=0;
								String orderdiscountcost="0";
								if(esthdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")){
									dorderdiscountcost = (subTotal/100)*esthdr.getORDERDISCOUNT();
									orderdiscountcost = Numbers.toMillionFormat(dorderdiscountcost, numberOfDecimal);
								}else{
									dorderdiscountcost = esthdr.getORDERDISCOUNT();
									orderdiscountcost = Numbers.toMillionFormat(dorderdiscountcost, numberOfDecimal);
								}

								String discounttype = esthdr.getDISCOUNT_TYPE();
								String discount = "0";
								double pdiscount=0;
								if(discounttype.equalsIgnoreCase("%")){
									double dDiscount = esthdr.getDISCOUNT();
									pdiscount = (subTotal/100)*dDiscount;
									discount = Numbers.toMillionFormat(pdiscount, numberOfDecimal);
								}else{
									double dDiscount = esthdr.getDISCOUNT();
									pdiscount = esthdr.getDISCOUNT();
									discount = Numbers.toMillionFormat(dDiscount, numberOfDecimal);
								}
								
								
								if(esthdr.getTAXID() != 0){
									if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){
										
										totalTax = (subTotal/100)*esthdr.getOUTBOUND_GST();
										
										if(esthdr.getISSHIPPINGTAX() == 1){
											totalTax = totalTax + ((esthdr.getSHIPPINGCOST()/100)*esthdr.getOUTBOUND_GST());
										}
										
										if(esthdr.getISORDERDISCOUNTTAX() == 1){
											totalTax = totalTax - ((dorderdiscountcost/100)*esthdr.getOUTBOUND_GST());
										}
										
										if(esthdr.getISDISCOUNTTAX() == 1){
											totalTax = totalTax - ((pdiscount/100)*esthdr.getOUTBOUND_GST());
										}
									}
								}
								
								double totalAmount = (subTotal + totalTax + esthdr.getADJUSTMENT()+esthdr.getSHIPPINGCOST())-(dorderdiscountcost+pdiscount);
								%>
							</tbody>
						</table>
						<table id="table3" class="table">
							<tbody style="text-align:right;">
								<tr>
									<td></td>
									<td class="text-center">Qty Total</td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalQty, "3")%></td>
									<td></td>
									<td></td>
									<td></td>
									<td><%=lblSubTotal%></td>
									<td><%=Numbers.toMillionFormat(subTotal, numberOfDecimal)%></td>
								</tr>
								
								<%if(esthdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")){%>
									<tr>
										<td colspan="7">
										Order Discount (<%=Numbers.toMillionFormat(esthdr.getORDERDISCOUNT(), "3")%>%)
										<br>
										<%if(esthdr.getISORDERDISCOUNTTAX() == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=orderdiscountcost%></td>
									</tr>
								<%}else{ %>
									<tr>
										<td colspan="7">
										Order Discount (<%=esthdr.getORDERDISCOUNTTYPE()%>)
										<br>
										<%if(esthdr.getISORDERDISCOUNTTAX() == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=orderdiscountcost%></td>
									</tr>
								<%} %>
								
								<%if(esthdr.getDISCOUNT_TYPE().equalsIgnoreCase("%")){%>
									<tr style="display:none"><!--  Author: Azees  Create date: July 6,2021  Description: Hide Discount -->
										<td colspan="7">
										Discount (<%=Numbers.toMillionFormat(esthdr.getDISCOUNT(), "3")%>%)
										<br>
										<%if(esthdr.getISDISCOUNTTAX() == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=discount%></td>
									</tr>
								<%}else{ %>
									<tr style="display:none"><!--  Author: Azees  Create date: July 6,2021  Description: Hide Discount -->
										<td colspan="7">
										Discount (<%=esthdr.getDISCOUNT_TYPE()%>)
										<br>
										<%if(esthdr.getISDISCOUNTTAX() == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=discount%></td>
									</tr>
								<%} %>
								
							
								<tr>
									<td colspan="7">
									<%=lblShippingcost%>
									<br>
										<%if(esthdr.getISSHIPPINGTAX() == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
									</td>
									<td><%=Numbers.toMillionFormat(esthdr.getSHIPPINGCOST(), numberOfDecimal)%></td>
								</tr>
								
								<%if(esthdr.getTAXID() != 0){
								if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td colspan="7"><%=taxdisplay%></td>
											<td><%=Numbers.toMillionFormat(totalTax, numberOfDecimal)%></td>
										</tr>
								<%}
								}%>
								
								<tr>
									<td colspan="7">Adjustment</td>
									<td><%=Numbers.toMillionFormat(esthdr.getADJUSTMENT(), numberOfDecimal)%></td>
								</tr>
								<tr>
									<td colspan="7"><b><%=lblTotal%> (<%=esthdr.getCURRENCYID()%>)</b></td>
									<td><b><%=Numbers.toMillionFormat(totalAmount, numberOfDecimal) %></b></td>
								</tr>
								<%if(!currency.equalsIgnoreCase(esthdr.getCURRENCYID())) {%><!--  Author: Azees  Create date: July 31,2021  Description:  Total of Local Currency -->
									<tr id="showtotalcur">
									<td colspan="7"><b><%=lblTotal%> (<%=currency%>)</b></td>
									<td><b><%=Numbers.toMillionFormat((totalAmount/esthdr.getCURRENCYUSEQT()), numberOfDecimal)%></b></td>
									</tr>
								<%}%>
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
			      <p> Deleted Sales Estimate information cannot be retrieved. Are you sure about deleting ?</p>
			      
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
		JsBarcode("#barCode", "<%=esthdr.getESTNO()%>", {format: "CODE128",displayValue: false});	
		
		$("#so-delete").click(function() {
		  	var status = "<%=esthdr.getSTATUS()%>";			 
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
		        url: "../salesestimate/delete?estno=<%=esthdr.getESTNO()%>",
			    contentType: false,
			    processData: false,
		        success: function (data) {
		        	window.location.href="../salesestimate/summary?msg="+data.MESSAGE;
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
				"/track/EstimateServlet?Submit="+actionvalue+"&ESTNO=<%=esthdr.getESTNO()%>",
				  '_blank' // <- This is what makes it open in a new window.
				);
	}
	function submitForm(actionvalue)
	{
		window.open(
				"/track/EstimateServlet?Submit="+actionvalue+"&ESTNO=<%=esthdr.getESTNO()%>&DIRTYPE=ESTIMATE"
				);
	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>