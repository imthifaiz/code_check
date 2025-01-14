<%@page import="com.track.db.object.PoDet"%>
<%@page import="com.track.db.object.PoHdr"%>
<%@page import="com.track.db.object.FinProject"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Purchase Invoice Detail";

	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String pono = (String)request.getAttribute("PONO");
	
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
// RESVI STARTS
	boolean displaySummaryEdit=false,displayPrintPdf=false,displayMore=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
	displaySummaryEdit = ub.isCheckValAcc("editpurchaseorder", plant,USERID);
	displayPrintPdf = ub.isCheckValAcc("printpurchaseorder", plant,USERID);
	displayMore = ub.isCheckValAcc("morepurchaseorder", plant,USERID);
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryEdit = ub.isCheckValinv("editpurchaseorder", plant,USERID);
		displayPrintPdf = ub.isCheckValinv("printpurchaseorder", plant,USERID);
		displayMore = ub.isCheckValinv("morepurchaseorder", plant,USERID);
	}

	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	
	PlantMstDAO PlantMstDAO = new PlantMstDAO();
	PoHdrDAO poHdrDAO= new PoHdrDAO();
	PoDetDAO poDetDAO = new PoDetDAO();
	FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
	
	String numberOfDecimal = PlantMstDAO.getNumberOfDecimal(plant);
	String COMP_INDUSTRY = PlantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
	ArrayList plntList = PlantMstDAO.getPlantMstDetails(plant);
	Map plntMap  = (Map) plntList.get(0);
	
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
	
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	PoHdr poheader = poHdrDAO.getPoHdrByPono(plant, pono);
	List<PoDet> podetail = poDetDAO.getPoDetByPono(plant, pono);
	
	ArrayList SupplyDetails = new ArrayList();	
	SupplyDetails = new CustUtil().getVendorDetailsForPO(plant, pono);
	
	ArrayList shippingCustDetails = new ArrayList();
	shippingCustDetails = new MasterUtil().getInboundShippingDetails(pono, poheader.getSHIPPINGID(), plant);
	
	FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(poheader.getTAXID());

	FinProjectDAO finProjectDAO = new FinProjectDAO();
	FinProject finProject=new FinProject();
	String projectname = "";
	if(poheader.getPROJECTID() > 0){
		finProject = finProjectDAO.getFinProjectById(plant, poheader.getPROJECTID());
		projectname = finProject.getPROJECT_NAME();
	}
	
	
	
	TransportModeDAO transportmodedao = new TransportModeDAO();
	String transportmode = "";
	if(poheader.getTRANSPORTID() > 0){
		transportmode = transportmodedao.getTransportModeById(plant, poheader.getTRANSPORTID());
	}
	
	POUtil poUtil = new POUtil();
	Map m = poUtil.getPOReceiptHdrDetails(plant);
	String prolable = (String) m.get("PROJECT");
	int prostatus = Integer.valueOf((String) m.get("PRINTWITHPROJECT"));
	String trans = (String) m.get("TRANSPORT_MODE");
	int printwithtransportmode = Integer.valueOf((String) m.get("PRINTWITHTRANSPORT_MODE"));
	String rcbno =  (String) m.get("RCBNO");
	String uenno =  (String) m.get("UENNO");//imtiuen
	String supplieruenno = (String) m.get("SUPPLIERUENNO");//imtiuen
	int printuenno = Integer.valueOf((String) m.get("PRINTWITHUENNO"));//imtiuen
	int printsupplieruenno = Integer.valueOf((String) m.get("PRINTWITHSUPPLIERUENNO"));//imtiuen
	String empname = new EmployeeDAO().getEmpname(plant, poheader.getEMPNO(), "");
	
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
<jsp:param name="submenu" value="<%=IConstants.PURCHASE_ORDER%>"/>
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
             <ul class="breadcrumb backpageul">      	
                  <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>       
                  <li><a href="../peppolapi/purchaseinvoicesummary"><span class="underline-on-hover">Purchase Invoice Summary</span></a></li> 
                  <li><label>Purchase Invoice Detail</label></li>                                   
             </ul>   
     <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">




				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../peppolapi/purchaseinvoicesummary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;" id="print_id1">
			<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
				<div class="ribbon-inner ribbon-draft"><%=poheader.getPOSENDSTATUS()%></div>
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
<%if(printuenno == 1){ %><%=uenno%> :<%=companyregnumber.trim()%><%} %>
<%=rcbno%> <%=RCBNO.trim()%>
Contact: <%=NAME.trim()%>
Tel: <%=TELNO.trim()%> Fax : <%=FAX.trim()%>
Email: <%=EMAIL.trim()%>
					</span>
					</div>
					<div class="col-xs-6 text-right">
								<h1 id="headerpage"><%=poheader.getORDERTYPE()%></h1>
						<figure class="pull-right text-center">
							<img id="barCode" style="width:215px;height:65px;">
							<figcaption># <%=poheader.getPONO()%></figcaption>
						</figure>
						<br style="clear:both"> 
					</div>
				</div>
				<div class="row">
					<div class="col-xs-7">
					<span>Supplier</span>	<br>
					<span><%=SupplyDetails.get(1)%></span>	<br>
					<%if(printsupplieruenno == 1){ %><span><%=supplieruenno%> : <%=SupplyDetails.get(25)%></span>	<br><%} %>			
					<span><%=rcbno%> <%=SupplyDetails.get(24)%></span>	<br>
					<span><%=SupplyDetails.get(2)%> <%=SupplyDetails.get(3)%></span>	<br> <!-- Block Address (Add1 + Add2) -->
					<span><%=SupplyDetails.get(4)%> <%=SupplyDetails.get(15)%></span>	<br> <!-- Road Address (Add3 + Add4) -->
					<span><%=SupplyDetails.get(22)%> <%=SupplyDetails.get(5)%> <%=SupplyDetails.get(6)%></span>	<br> <!-- Country + State + Zip -->
					<span>Email: <%=SupplyDetails.get(12)%></span>	<br>
					<span>Remarks: <%=SupplyDetails.get(15)%></span>	<br>
					<%if(shippingCustDetails.size()>0){%>
					<br>
					<span>Shipping Address</span>	<br>
					<span><%=shippingCustDetails.get(1)%></span>	<br>
					<span><%=shippingCustDetails.get(7)%> <%=shippingCustDetails.get(8)%></span>	<br> <!-- unitno + building -->
					<span><%=shippingCustDetails.get(9)%> <%=shippingCustDetails.get(10)%></span>	<br> <!-- street + city -->
					<span><%=shippingCustDetails.get(11)%> <%=shippingCustDetails.get(12)%> <%=shippingCustDetails.get(13)%></span>	<br><!-- Country + State + Zip -->
					<!-- <span>Attention: </span>	<br> -->
					<span>Tel: <%=shippingCustDetails.get(3)%></span>	<br>		
					<span></span><br>					
					<%} %>
					<%if(printwithtransportmode == 1){ %>
					<span><%=trans%> : <%=transportmode %></span>	
					<%} %> 
					<br>
					<%if(prostatus == 1){ %>
					<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
					<span><%=prolable%> : <%=projectname %></span>	<br>
					<%} }%>
					</div>
					<div class="col-xs-5 text-right">
						<table id="table1" class="table pull-right">
							<tbody>
								<tr>
									<td>Date</td>
									<td><%=poheader.getCollectionDate()%></td>
								</tr>
								<%-- <%if(printWithDeliveryDate.equals("1") && ((String)invoiceHdr.get("DUE_DATE")).length() > 0){%> --%>
								<%-- <tr>
									<td>Project</td>
									<td><%=projectname%></td>
								</tr> --%>
								<tr>
									<td>Delivery Date</td>
									<td><%=poheader.getDELDATE()%></td>
								</tr>
								<%-- <%} %> --%>
								<tr>
									<td>INCOTERM</td>
									<td><%=poheader.getINCOTERMS()%></td>
								</tr>
								<%-- <%if(printEmployee.equals("1") && ((String)invoiceHdr.get("EMPNO")).length() > 0){%> --%>
								<%-- <%} %> --%>
								
								<tr>
									<td>Employee</td>
									<td><%=empname %></td>
								</tr>
								
								<tr>
									<td>Prepared By</td>
									<td><%=poheader.getCRBY()%></td>
								</tr>
								
								<tr>
									<td>Terms</td>
									<td><%=poheader.getPAYMENT_TERMS()%></td>
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
									<td>No</td>
									<td>Product ID</td>
									<td>Order Qty</td>
									<td>Recived Qty</td>
									<td>UOM</td>
									<td>Unit Cost</td>
									<td>Discount</td>
									<td class="text-right">Total Amount</td>
								</tr>
							</thead>
							<tbody>
								<%
								int i = 0;
								double dTotalQty=0,dTotalPickedQty=0,shingpercentage=0,subTotal=0,totax=0;
								String taxdisplay="";
								for(PoDet poDet :podetail){

									String qty="", unitPrice="", amount="", percentage="", tax="",
											item_discounttype = "", lineDiscount = "",
											pickedQty = "", cost="";
									
									double discount = 0, dTax = 0;
									taxdisplay = poDet.getTAX_TYPE();
									item_discounttype = poDet.getDISCOUNT_TYPE();
									qty = poDet.getQTYOR().toString();
									double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
						  			qty = StrUtils.addZeroes(dQty, "3");
						  			dTotalQty += dQty;
						  			
						  			pickedQty = poDet.getQTYRC().toString();
						  			double dpQty ="".equals(pickedQty) ? 0.0d :  Double.parseDouble(pickedQty);
						  			pickedQty = StrUtils.addZeroes(dpQty, "3");
						  			dTotalPickedQty += dpQty;

						  			
						  			double dCost = poDet.getUNITCOST();
						  			unitPrice = Numbers.toMillionFormat(dCost, numberOfDecimal);
						  			double item_amount = (dCost*dQty);
						  			
						  			if(item_discounttype.equalsIgnoreCase("%")){
										double dDiscount = poDet.getDISCOUNT();
										discount = ((item_amount)/100)*dDiscount;
										lineDiscount = StrUtils.addZeroes(dDiscount, "3");
									}else{
										discount = poDet.getDISCOUNT();
										lineDiscount = Numbers.toMillionFormat(discount, numberOfDecimal);
									}
						  			item_amount = item_amount - discount;
						  			subTotal += item_amount;
						  			
								%>
									<tr>
										<td class="text-center"><%=poDet.getPOLNNO() %></td>
										<td class="text-center"><%=poDet.getITEM()%></td>
										<td class="text-center"><%=Numbers.toMillionFormat(qty, numberOfDecimal)%></td>
										<td class="text-center"><%=StrUtils.addZeroes(poDet.getQTYRC().doubleValue(), "3")%></td>
										<td class="text-center"><%=poDet.getUNITMO()%></td>
										<td class="text-center"><%=Numbers.toMillionFormat(unitPrice, numberOfDecimal)%></td>
										<td class="text-center"><%=lineDiscount%> <%=item_discounttype%></td>
										<td class="text-right"><%=Numbers.toMillionFormat(item_amount, numberOfDecimal)%></td>
									</tr>
						<tr>
          					<td></td>
          					<td colspan="7">&emsp;&emsp;&emsp;<%=poDet.getItemDesc() %></td>
						</tr>
								<%i++;
								}
								
								
								if(poheader.getISTAXINCLUSIVE() == 1){
									subTotal = (subTotal*100)/(poheader.getINBOUND_GST()+100);
								}
								double dorderdiscountcost=0;
								String orderdiscountcost="0";
								if(poheader.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")){
									dorderdiscountcost = (subTotal/100)*poheader.getORDERDISCOUNT();
									orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
								}else{
									dorderdiscountcost = poheader.getORDERDISCOUNT();
									orderdiscountcost = Numbers.toMillionFormat(dorderdiscountcost, numberOfDecimal);
								}
								
								if(poheader.getTAXID() != 0){
									if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){
										
										totax = (subTotal/100)*poheader.getINBOUND_GST();
										
										if(poheader.getISSHIPPINGTAX() == 1){
											totax = totax + ((poheader.getSHIPPINGCOST()/100)*poheader.getINBOUND_GST());
										}
										
										if(poheader.getISDISCOUNTTAX() == 1){
											totax = totax - ((dorderdiscountcost/100)*poheader.getINBOUND_GST());
										}
									}
								}

								double totalAmount = (subTotal + poheader.getSHIPPINGCOST() + totax + poheader.getADJUSTMENT()) - dorderdiscountcost ;
								%>
								<tr>
									<td class="text-center">Qty Total</td>
									<td></td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalQty, "3")%></td>
									<td class="text-center"><%=Numbers.toMillionFormat(dTotalPickedQty, "3")%></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</tbody>
						</table>
						<table id="table3" class="table">
							<tbody style="text-align:right;">
								<tr>
									<td colspan="7">Sub Total</td>
									<td><%=Numbers.toMillionFormat(subTotal, numberOfDecimal)%></td>
								</tr>
								<%if(poheader.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")){%>
									<tr>
										<td colspan="7">Order Discount (<%=StrUtils.addZeroes(poheader.getORDERDISCOUNT(), "3")%>%)</td>
										<td>(-) <%=orderdiscountcost%></td>
									</tr>
								<%}else{ %>
									<tr>
										<td colspan="7">Order Discount (<%=poheader.getORDERDISCOUNTTYPE()%>)</td>
										<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost, numberOfDecimal)%></td>
									</tr>
								<%} %>
								<tr>
									<td colspan="7">Shipping Cost</td>
									<td><%=Numbers.toMillionFormat(poheader.getSHIPPINGCOST(), numberOfDecimal)%></td>
								</tr>
								<%if(poheader.getTAXID() != 0){
								if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td colspan="7"><%=taxdisplay%></td>
											<td><%=Numbers.toMillionFormat(totax, numberOfDecimal)%></td>
										</tr>
								<%}
								}%>
								<tr>
									<td colspan="7">Adjustment</td>
									<td><%=Numbers.toMillionFormat(poheader.getADJUSTMENT(), numberOfDecimal)%></td>
								</tr>
								<tr>
									<td colspan="7"><b>Total With Tax (<%=poheader.getCURRENCYID()%>)</b></td>
									<td><b><%=Numbers.toMillionFormat(totalAmount, numberOfDecimal) %></b></td>
								</tr>
								<%if(!curency.equalsIgnoreCase(poheader.getCURRENCYID())) {%><!--  Author: Azees  Create date: July 27,2021  Description:  Total of Local Currency -->								
								<tr>
									<td colspan="7"><b>Total With Tax (<%=curency%>)</b></td>
									<td><b><%=Numbers.toMillionFormat((totalAmount/poheader.getCURRENCYUSEQT()), numberOfDecimal)%></b></td>
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
<div id="deletepurchaseOrder" class="modal fade" role="dialog">
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
			      <p> Deleted Purchase Order information cannot be retrieved. Are you sure about deleting ?</p>
			      
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
		JsBarcode("#barCode", "<%=poheader.getPONO()%>", {format: "CODE128",displayValue: false});	
		
		$("#so-delete").click(function() {
		  	var status = "<%=poheader.getSTATUS()%>";			 
		  	var result="";
	  		if(status == "N") {
				$('#deletepurchaseOrder').modal('show');			
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
		        url: "../purchaseorder/delete?PONO=<%=poheader.getPONO()%>",
			    contentType: false,
			    processData: false,
		        success: function (data) {
		        	window.location.href="../purchaseorder/summary?msg="+data.MESSAGE;
		        },
		        error: function (data) {
		            alert(data.responseText);
		        }
		    });
	        return false; 
		});
	});
	
	
	function submitFormPrint(actionvalue)
	{
	    window.open(
				"/track/purchaseorderservlet?Submit="+actionvalue+"&PONO=<%=poheader.getPONO()%>",
				  '_blank' // <- This is what makes it open in a new window.
				);
	}
	
	function submitForm(actionvalue)
	{
	    window.open(
				"/track/purchaseorderservlet?Submit="+actionvalue+"&PONO=<%=poheader.getPONO()%>&DIRTYPE=INBOUND"
				);
	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>