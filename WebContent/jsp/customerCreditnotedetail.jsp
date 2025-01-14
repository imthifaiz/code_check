<%@page import="com.track.db.object.Journal"%>
<%@page import="com.track.serviceImplementation.JournalEntry"%>
<%@page import="com.track.db.util.ItemUtil"%>
<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.db.object.InvPaymentDetail"%>
<%@page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.util.*"%>
<%@page import="com.track.dao.*"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@page import="com.track.db.object.FinProject"%>
<%@page import="com.track.constants.*"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="java.util.Set"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<%@include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! 
	@SuppressWarnings({"rawtypes", "unchecked"}) 
%>
<%
	String title = "Credit Note Detail";
	String rootURI = HttpUtils.getRootURI(request);
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String basecurrency = curency;
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc=StrUtils.fString(request.getParameter("result"));
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
	
	boolean displaySummaryEdit=false,displayPdfPrint=false,displayApplyCredits=false,displayApplyRecords=false,displaySummaryMore=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryEdit = ub.isCheckValAcc("editCreditNote", plant,username);
		displayPdfPrint = ub.isCheckValAcc("printCreditNote", plant,username);
		displaySummaryMore = ub.isCheckValAcc("moreCreditNote", plant,username);	
	}
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	//InvoiceUtil invoiceUtil = new InvoiceUtil();
	CustomerCreditnoteDAO custcrdnotedao= new CustomerCreditnoteDAO();
	CustMstDAO custMstDAO = new CustMstDAO();
	InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String COMP_INDUSTRY = _PlantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
	String custcrdnoteHDRid = StrUtils.fString(request.getParameter("custcreditid"));
	String CURRENCYUSEQT="1";
	Hashtable ht = new Hashtable();
	ht.put("ID", custcrdnoteHDRid);
	ht.put("PLANT", plant);
	//List CustcrdnoteHdrList =  custcrdnotedao.getCustCreditnoteHdrById(ht);
	List CustcrdnoteHdrList =  custcrdnotedao.getCov_CustCreditnoteHdrById(ht);
	Map CustcrdnoteHdr=(Map)CustcrdnoteHdrList.get(0);
	CURRENCYUSEQT = (String) CustcrdnoteHdr.get("CURRENCYUSEQT");
	curency = (String) CustcrdnoteHdr.get("CURRENCYID");
	//List CustcrdnoteDetList = custcrdnotedao.getCustCrdnoteDedtByHrdId(ht);
	List CustcrdnoteDetList = custcrdnotedao.getConv_CustCrdnoteDedtByHrdId(ht);
	List taxList =new ArrayList();
	
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
	
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	File checkImageFile = new File(imagePath);
	if (!checkImageFile.exists()) {
		imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	}
	String totalAmount= (String) CustcrdnoteHdr.get("TOTAL_AMOUNT");
	double dTotalAmount ="".equals(totalAmount) ? 0.0d :  Double.parseDouble(totalAmount);
	totalAmount = StrUtils.addZeroes(dTotalAmount, numberOfDecimal);
	String balanceAmount= (String) CustcrdnoteHdr.get("BALANCE");
	balanceAmount= String.valueOf((Float.parseFloat(balanceAmount))*(Float.parseFloat(CURRENCYUSEQT)));
	double dBalanceAmount ="".equals(balanceAmount) ? 0.0d :  Double.parseDouble(balanceAmount);
	balanceAmount = StrUtils.addZeroes(dBalanceAmount, numberOfDecimal);
	
	
	String subTotal = (String) CustcrdnoteHdr.get("SUB_TOTAL");
	double dSubTotal ="".equals(subTotal) ? 0.0d :  Double.parseDouble(subTotal);
	subTotal = StrUtils.addZeroes(dSubTotal, numberOfDecimal);	
	
	String adjustment = (String) CustcrdnoteHdr.get("ADJUSTMENT");
	double dAdjustment ="".equals(adjustment) ? 0.0d :  Double.parseDouble(adjustment);
	adjustment = StrUtils.addZeroes(dAdjustment, numberOfDecimal);
	String custName = custMstDAO.getCustName(plant, (String) CustcrdnoteHdr.get("CUSTNO"));
	
	String orderdiscount = (String) CustcrdnoteHdr.get("ORDER_DISCOUNT");
	double dorderdiscount ="".equals(orderdiscount) ? 0.0d :  Double.parseDouble(orderdiscount);
	String orderdiscounttype = (String) CustcrdnoteHdr.get("ORDERDISCOUNTTYPE");
	String orderdiscountcost = "";
	String ordisc = "";
	if(orderdiscounttype.equalsIgnoreCase("%")){
		ordisc = StrUtils.addZeroes(dorderdiscount, "3");
		double dorderdiscountcost = (dSubTotal/100)*dorderdiscount;
		orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
	}else{
		orderdiscountcost = Numbers.toMillionFormat(dorderdiscount, numberOfDecimal);
	}
	
	
	String discount="0";
	String discounttype = (String) CustcrdnoteHdr.get("DISCOUNT_TYPE");
	String discountpercent = "";
	if(discounttype.equalsIgnoreCase("%")){
		
		discount = (String) CustcrdnoteHdr.get("DISCOUNT");
		double dDiscount ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
		discountpercent = StrUtils.addZeroes(dDiscount, "3");
		double pdiscount = ((dSubTotal)/100)*dDiscount;
		
		discount = StrUtils.addZeroes(pdiscount, numberOfDecimal);
	}else{
		discount = (String) CustcrdnoteHdr.get("DISCOUNT");
		double dDiscount ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
		discount = StrUtils.addZeroes(dDiscount, numberOfDecimal);
	}
	
	InvPaymentDetail invpadet = invoicePaymentDAO.getInvoicePaymentDetailsbycreditid(Integer.valueOf(custcrdnoteHDRid), plant);
	int payhdrid =  invpadet.getRECEIVEHDRID();
	
	
	int isodtax = Integer.valueOf((String) CustcrdnoteHdr.get("ISORDERDISCOUNTTAX"));
	int isdtax = Integer.valueOf((String) CustcrdnoteHdr.get("ISDISCOUNTTAX"));
	double ceqt = Double.valueOf((String) CustcrdnoteHdr.get("CURRENCYUSEQT"));
	int taxid = Integer.valueOf((String) CustcrdnoteHdr.get("TAXID"));
	
	FinCountryTaxType  fintaxtype = new FinCountryTaxType();
	if(taxid != 0){
		FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
		fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxid);
	}
	
	String totalTax = (String) CustcrdnoteHdr.get("TAXAMOUNT");
	double dtotalTax ="".equals(totalTax) ? 0.0d :  Double.parseDouble(totalTax);
	totalTax = StrUtils.addZeroes(dtotalTax, numberOfDecimal);
	
	FinProjectDAO finProjectDAO = new FinProjectDAO();
	FinProject finProject=new FinProject();
	String projectname = "";
	if(Integer.valueOf((String) CustcrdnoteHdr.get("PROJECTID")) > 0){
		finProject = finProjectDAO.getFinProjectById(plant, Integer.valueOf((String) CustcrdnoteHdr.get("PROJECTID")));
		projectname = finProject.getPROJECT_NAME();
	}
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.CREDIT_NOTES%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/font-awesome.min.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/tabulator_bootstrap.min.css" />
<style>
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td, #table3>tbody>tr>td {
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

@media print {
  @page { margin: 0; }
  body { margin: 1.6cm; }
}
</style>
<div class="container-fluid m-t-20"><%if(resultnew.equals("") || resultnew == null){}else{ %>
	  <div class="alert alert-danger alert-dismissible" style="width: max-content;margin:0 auto;">
	    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    <%=resultnew %>
	  </div>
  <%} %>
  
  <%if(rsuccess.equals("") || resultnew == null){}else{ %>
	    <div class="alert alert-success alert-dismissible" style="width: max-content;margin:0 auto;">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <%=rsuccess %>
  </div>
  <%} %>
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>    
                <li><a href="../creditnote/summary"><span class="underline-on-hover">Credit Note Summary</span> </a></li>               
                <li><label>Credit Note Detail</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
				<%-- <%if(CustcrdnoteHdr.get("CREDIT_STATUS").equals("Draft")){ %> --%>
				 <% if (displaySummaryEdit) { %>
					<button type="button" class="btn btn-default"
					 data-toggle="tooltip"  data-placement="bottom" title="Edit">
						<a href="../creditnote/edit?custcreditid=<%=CustcrdnoteHdr.get("ID")%>"><i class="fa fa-pencil" aria-hidden="true"></i></a>
					</button>
					 <% } %>
					<%-- <%}%> --%>
					 <% if (displayPdfPrint) { %>
					<button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					<% } %>
				</div>
				&nbsp;
				&nbsp;
				<div class="btn-group" role="group">
					<%if(CustcrdnoteHdr.get("CREDIT_STATUS").equals("Draft")){ %>
						<button type="button pull-right" class="btn btn-success " id="convertdrafttoopen">Convert To Open</button>
					<%}%>
				</div>
				<%if(CustcrdnoteHdr.get("CREDIT_STATUS").equals("CANCELLED")){ %>
				&nbsp;
				<div class="btn-group" role="group">
					<button type="button pull-right" class="btn btn-success" onclick="convertToDraft()">Convert to Draft</button>
				</div>
				<%}%>
				<div class="btn-group" role="group">
					  <!-- <button class="btn btn-default dropdown-toggle" type="button pull-right" data-toggle="dropdown">More</button>
					  <ul class="dropdown-menu">
					    <li><a href="#">Cancel</a></li>
					    <li><a href="#">Delete</a></li>
					  </ul> -->
					  <% if (displaySummaryMore) { %>
					  <button type="button" class="btn btn-default" data-toggle="dropdown" >More <span class="caret"></span></button>
					   <ul class="dropdown-menu" style="min-width: 0px;">
					    <li id="bill-copy"><a href="../creditnote/copy?custcreditid=<%=CustcrdnoteHdr.get("ID")%>">Copy</a></li>
					    <li id="bill-cancel"><a href="#">Cancel</a></li>
					    <li id="bill-delete"><a href="#">Delete</a></li>
					  </ul>
					  <% } %>
				</div>
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../creditnote/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;" id="print_id1">
		<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-draft"><%=CustcrdnoteHdr.get("CREDIT_STATUS")%></div>
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
<%=fromAddress_BlockAddress%>
<%=fromAddress_RoadAddress%>
<%=fromAddress_Country%> <%=ZIP%>
</span>
			</div>

			<div class="col-xs-6 text-right">
				<h1>CREDIT NOTE</h1>
				<p># <%=CustcrdnoteHdr.get("CREDITNOTE")%></p>
				<%-- <%if(CustcrdnoteHdr.get("DONO").equals("") || CustcrdnoteHdr.get("DONO").equals(null) ){ %>	
				<%}else{ %>
					<h3>Order Number</h3>
					<p># <%=CustcrdnoteHdr.get("DONO")%></p>
				<%} %> --%>
				<br> <span>Credits Remaining</span>
				<h3><%=curency%><%=Numbers.toMillionFormat(balanceAmount,Integer.valueOf(numberOfDecimal))%></h3>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-7">
				<br><br><br>
				<span>Invoice To</span>	<br>			
				<%-- <a href="#"><%=invoiceHdr.get("CUSTNO")%></a> --%>	
				<a href="#"><%=custName%></a><br>
				<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
				<span>Project : <%=projectname%></span><br>	
				<%} %>
			</div>
			<div class="col-xs-5 text-right">
				<table id="table1" class="table pull-right">
					<tbody>
						<tr>
							<td>Credit Note Date :</td>
							<td><%=CustcrdnoteHdr.get("CREDIT_DATE")%></td>
						</tr>						
						<tr>
							<td>Invoice#  :</td>
							<td><%=CustcrdnoteHdr.get("INVOICE")%></td>
						</tr>
						<tr>
							<td>Invoice Date :</td>
							<td><%=CustcrdnoteHdr.get("CREDIT_DATE")%></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12">
				<table id="table2" class="table">
					<thead>
						<tr>
							<td>#</td>
							<td>Product ID</td>
							<td>Qty</td>
							<td>Rate</td>
							<td>Discount</td>
							<td class="text-right">Amount</td>
						</tr>
					</thead>
					<tbody>
					<%
					String taxdisplay = "";
					for(int i =0; i<CustcrdnoteDetList.size(); i++) {   
				  		Map m=(Map)CustcrdnoteDetList.get(i);
				  		String qty="", cost="", amount="", percentage="", tax="";
				  		
			  			qty = (String) m.get("QTY");
			  			double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
			  			qty = StrUtils.addZeroes(dQty, "3");
			  			
			  			cost = (String) m.get("UNITPRICE");
			  			double dCost ="".equals(cost) ? 0.0d :  Double.parseDouble(cost);
			  			cost = StrUtils.addZeroes(dCost, numberOfDecimal);
			  			
			  			amount = (String) m.get("AMOUNT");
			  			double dAmount ="".equals(amount) ? 0.0d :  Double.parseDouble(amount);
			  			amount = StrUtils.addZeroes(dAmount, numberOfDecimal);
			  			taxdisplay = (String)m.get("TAX_TYPE");
			  			/* if(!((String)m.get("TAX_TYPE")).equalsIgnoreCase("")){
			  				String str =(String)m.get("TAX_TYPE");
			  				str=str.replace("%]","");			  				
			  				String[] arrOfStr = str.split("\\[");
			  				percentage = arrOfStr[1];
			  				//percentage = (String)m.get("GSTPERCENTAGE");			  			
			  			double dPercntage = "".equals(cost) ? 0.0d :  Double.parseDouble(percentage);
			  			
			  			double dTax = (dAmount * (dPercntage / 100));
			  			tax = StrUtils.addZeroes(dTax, numberOfDecimal);
			  					
			  			Map<String, String> taxDetail = new HashMap();
			  			boolean match = false;
			  			String display = (String)m.get("TAX_TYPE");
			  			taxDetail.put("name", display);
			  			taxDetail.put("types", arrOfStr[0]);
			  			taxDetail.put("tax", tax);
			  			
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
		  			} */
			  			String linediscount = (String)m.get("DISCOUNT");
			  			String linediscounytype = (String) m.get("DISCOUNT_TYPE");
			  			if(linediscounytype.equalsIgnoreCase("%")){
			  				linediscount = StrUtils.addZeroes(Double.parseDouble(linediscount), "3");	
			  			}else{
			  				linediscount = Numbers.toMillionFormat(Double.parseDouble(linediscount), numberOfDecimal);	
			  			}
						
						
			  		%>
				  		<tr>
							<td class="text-center"><%=m.get("LNNO") %></td>
							<td class="text-center"><%=m.get("ITEM") %></td>
							<td class="text-center"><%=Numbers.toMillionFormat(qty,Integer.valueOf(numberOfDecimal))%></td>
							<td class="text-center"><%=Numbers.toMillionFormat(cost,Integer.valueOf(numberOfDecimal))%></td>
							<td class="text-center"><%=linediscount%><%=linediscounytype %></td>
							<td class="text-right"><%=Numbers.toMillionFormat(amount,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
				  	<%}%>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-6"></div>
			<div class="col-xs-6">
				<table id="table3" class="table text-right" style="margin-bottom: 0px;">
					<tbody>
						<tr>
							<td>Sub Total</td>
							<td><%=Numbers.toMillionFormat(subTotal,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
					<%-- 	<tr>
							<td>Order Discount (<%=ordisc%>%)</td>
							<td>(-) <%=orderdiscountcost%></td>
						</tr> --%>
						<%if(orderdiscounttype.equalsIgnoreCase("%")){%>
									<tr>
										<td>
										Order Discount (<%=Numbers.toMillionFormat(Double.valueOf(ordisc), "3")%>%)
										<br>
										<%if(isodtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=orderdiscountcost%></td>
									</tr>
								<%}else{ %>
									<tr>
										<td>
										Order Discount (<%=orderdiscounttype%>)
										<br>
										<%if(isodtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost,Integer.valueOf(numberOfDecimal))%></td>
									</tr>
						<%} %>
						<%-- <tr>
							<td>Discount</td>
							<td>(-) <%=discount%></td>
						</tr> --%>
						
						<%if(discounttype.equalsIgnoreCase("%")){%>
									<tr>
										<td>
										Discount (<%=Numbers.toMillionFormat(Double.valueOf(discountpercent), "3") %>%)
										<br>
										<%if(isdtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=discount%></td>
									</tr>
								<%}else{ %>
									<tr>
										<td>
										Discount (<%=discounttype%>)
										<br>
										<%if(isdtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(discount,Integer.valueOf(numberOfDecimal))%></td>
									</tr>
								<%} %>
								
								<%if(taxid != 0){
								if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td><%=taxdisplay%></td>
											<td><%=totalTax %></td>
										</tr>
								<%}
								}%>
						
						<%-- <%
						if(taxList.size() > 0){
						for(int i =0; i<taxList.size(); i++) {
							Map tMap=(Map)taxList.get(i);
							String name=(String)tMap.get("name");
							if(name.equalsIgnoreCase("EXEMPT[0.0%]") || name.equalsIgnoreCase("OUT OF SCOPE[0.0%]"))
							{
								
							}
							else
							{
								String displaydisc = (String)tMap.get("name");
					  			
					  			String percentagedisc = displaydisc.substring(displaydisc.indexOf("[")+1, displaydisc.lastIndexOf("%"));
					  			double dPercntage = Double.parseDouble(percentagedisc);	
					  			if(dPercntage > 0){
					  			double dTaxdisc = (Double.parseDouble(discount) * (dPercntage / 100));
					  			double dTaxorderdiscount = (Double.parseDouble(orderdiscountcost) * (dPercntage / 100));
					  			double dfinaltax = Double.parseDouble((String)tMap.get("tax")) - (dTaxdisc-dTaxorderdiscount);
					  			String finaltax = StrUtils.addZeroes(dfinaltax, numberOfDecimal);
						%>
							<tr>
								<td><%=(String)tMap.get("name")%></td>
								<td><%=finaltax%></td>
							</tr>
						<%
						}else{
							double dfinaltax = Double.parseDouble((String)tMap.get("tax"));
				  			String finaltax = StrUtils.addZeroes(dfinaltax, numberOfDecimal);
							%>
							<tr>
								<td><%=(String)tMap.get("name")%></td>
								<td><%=finaltax%></td>
							</tr>
						<%
						}
					  			}
							}
						}
						%> --%>
						<tr>
							<td>Adjustment</td>
							<td><%=Numbers.toMillionFormat(adjustment,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<tr>
							<td><b>Total (<%=CustcrdnoteHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(totalAmount,Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)CustcrdnoteHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: August 04,2021  Description:  Total of Local Currency -->
						<tr id="showtotalcur">
							<td><b>Total (<%=basecurrency%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(String.valueOf((dTotalAmount/Double.parseDouble(CURRENCYUSEQT))),Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
						<%}%>
						<tr class="grey-bg">
							<td><b>Balance (<%=CustcrdnoteHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(String.valueOf(balanceAmount),Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)CustcrdnoteHdr.get("CURRENCYID"))) {%>
						<tr id="showtotalBal">
							<td>Balance (<%=basecurrency%>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(String.valueOf((dBalanceAmount/Double.parseDouble(CURRENCYUSEQT))),Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%}%>
					</tbody>
				</table>
				<table id="table4" class="table text-right" style="display: none"><!--  Author: Azees  Create date: August 04,2021 Description:  Total of Local Currency -->
					<tbody>
						<tr>
							<td>Sub Total</td>
							<td><%=Numbers.toMillionFormat(subTotal,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<%if(orderdiscounttype.equalsIgnoreCase("%")){%>
									<tr>
										<td>
										Order Discount (<%=Numbers.toMillionFormat(Double.valueOf(ordisc), "3")%>%)
										<br>
										<%if(isodtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=orderdiscountcost%></td>
									</tr>
								<%}else{ %>
									<tr>
										<td>
										Order Discount (<%=orderdiscounttype%>)
										<br>
										<%if(isodtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost,Integer.valueOf(numberOfDecimal))%></td>
									</tr>
						<%} %>
						<%-- <tr>
							<td>Discount</td>
							<td>(-) <%=discount%></td>
						</tr> --%>
						
						<%if(discounttype.equalsIgnoreCase("%")){%>
									<tr>
										<td>
										Discount (<%=Numbers.toMillionFormat(Double.valueOf(discountpercent), "3") %>%)
										<br>
										<%if(isdtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=discount%></td>
									</tr>
								<%}else{ %>
									<tr>
										<td>
										Discount (<%=discounttype%>)
										<br>
										<%if(isdtax == 1){ %>
										(Tax Inclusive)
										<%}else{%>
										(Tax Exclusive)
										<%} %>
										</td>
										<td>(-) <%=Numbers.toMillionFormat(discount,Integer.valueOf(numberOfDecimal))%></td>
									</tr>
								<%} %>
								
								<%if(taxid != 0){
								if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td><%=taxdisplay%></td>
											<td><%=totalTax %></td>
										</tr>
								<%}
								}%>					
						
						<tr>
							<td>Adjustment</td>
							<td><%=Numbers.toMillionFormat(adjustment,Integer.valueOf(numberOfDecimal))%></td>
						</tr>
						<tr>
							<td><b>Total (<%=CustcrdnoteHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(String.valueOf(totalAmount),Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
						<tr class="grey-bg">
							<td><b>Balance (<%=CustcrdnoteHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(String.valueOf(balanceAmount),Integer.valueOf(numberOfDecimal))%></b></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		</span>
		</div>


	</div>
	
</div>
<div id="journal_detail_box" class="box">
		<div class="box-header">
			<div id="ember1145"
				class="bg-secondary  accord-title p-3 cursor-pointer no-border text-semibold ember-view">
				 <h1 style="font-size: 20px;" class="box-title">Display Journal</h1>
				<svg version="1.1" id="SVG_Up_Arrow" style="display: none;"
					xmlns="http://www.w3.org/2000/svg" idth="512" height="512"
					viewBox="0 0 512 512" xml:space="preserve"
					class="icon icon-xs align-text-bottom float-right text-dodgerblue mr-3 pull-right pointer-arrow"
					onclick="{hideJournalDetail('journal_detail_box_body');}">
					<path
						d="M495.7 419.6L295 225.1c-21.6-20.9-56.4-20.9-78 0L16.3 419.6c-21.7 21-21.8 55.2-.3 76.4 21.5 21.2 56.6 21.3 78.3.3L256 339.6l161.6 156.7c21.7 21 56.8 20.9 78.3-.3 10.7-10.5 16.1-24.3 16.1-38.1s-5.5-27.7-16.3-38.3z"></path></svg>
				<svg version="1.1" id="SVG_Down_Arrow"
					xmlns="http://www.w3.org/2000/svg" width="512" height="512"
					viewBox="0 0 512 512"
					class="icon icon-xs align-text-bottom float-right text-dodgerblue mr-3 pull-right pointer-arrow"
					onclick="{showJournalDetail('journal_detail_box_body', 'journal_detail');}">
					<path
						d="M2.157 159.57c0 13.773 5.401 27.542 16.195 38.02l198.975 192.867c21.411 20.725 55.94 20.725 77.34 0L493.63 197.59c21.508-20.846 21.637-54.778.269-75.773-21.35-20.994-56.104-21.098-77.612-.26L256.004 276.93 95.721 121.562c-21.528-20.833-56.268-20.734-77.637.26C7.472 132.261 2.157 145.923 2.157 159.57z"></path></svg>
			</div>
		</div>
		<div class="box-body" id="journal_detail_box_body" style="display:none;">
			<div class="row">
				<div class="col-sm-12">
					<div id="ember1146" class="ember-view collapse show" style="">
						<div class="p-3 border border-secondary">
							<div id="ember1147" class="ember-view">
								<div class="clearfix pb-3">
									<small class="text-muted">Amount is displayed in your credit note currency</small>&nbsp;<span class="badge text-semibold badge-success"><%=CustcrdnoteHdr.get("CURRENCYID")%></span>
									<!---->
								</div>
								<p class="font-large">
									<b> <!---->
										<!---->
									</b>
								</p>
								
								<div id="ember1154" class="ember-view">
									<!---->
								</div>
							</div>
						</div>
						<br>
					</div>
					<div id="journal_detail"></div>
				</div>
			</div>		
		</div>
<div id="cancelcreditnote" class="modal fade" role="dialog">
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
			      <p> Are you sure about cancel the credit note?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" id="cfmcancel" style="background:red;">
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
	
	<div id="deletecreditnote" class="modal fade" role="dialog">
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
			      <p> Deleted credit note information cannot be retrieved. Are you sure about deleting ?</p>
			      
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
				setTimeout(function() {
				    $('.alert').fadeOut('fast');
				}, 2000);
				
			  $('[data-toggle="tooltip"]').tooltip();  
			  $('.printMe').click(function(){
				  <%if(!basecurrency.equalsIgnoreCase((String)CustcrdnoteHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: August 04,2021  Description:  Total of Local Currency -->
				  document.getElementById('showtotalcur').style.display = 'none';
				  document.getElementById('showtotalBal').style.display = 'none';
				  <%}%>
				     $("#print_id").print();
				     <%if(!basecurrency.equalsIgnoreCase((String)CustcrdnoteHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: July 31,2021  Description:  Total of Local Currency -->
				     document.getElementById('showtotalcur').style.display = 'contents';
				     document.getElementById('showtotalBal').style.display = 'contents';
					  <%}%>
				});
			  
			  $("#bill-cancel").click(function() {
				  var creditnoteHdrId ="<%=CustcrdnoteHdr.get("ID")%>";
				  var cnstatus ="<%=CustcrdnoteHdr.get("CREDIT_STATUS")%>";
				  
				  if(cnstatus.toLowerCase() == "draft" || cnstatus.toLowerCase() == "open"){
					  $('#cancelcreditnote').modal('show');
				  }else if(cnstatus.toLowerCase() == "cancelled"){
					  result = "Credit note already cancelled.";
					  window.location.href = "../creditnote/detail?custcreditid="+creditnoteHdrId+"&resultnew="+ result;
				  }else{
					  result = "Once Credit Note payment Apply Credit in other payment not allow to cancel. <a href='invoicePaymentDetail.jsp?ID=<%=payhdrid%>' target='_blank'>CLICK HERE</a> or the payment details";
					  window.location.href = "../creditnote/detail?custcreditid="+creditnoteHdrId+"&resultnew="+ result;
				  }
			  });
			  
			  $("#cfmcancel").click(function(){
				  window.location.href = "/track/CustomerCreditNoteServlet?ACTION=convertToCancel&CreditnoteHdrId=<%=CustcrdnoteHdr.get("ID")%>&CreditNote=<%=CustcrdnoteHdr.get("CREDITNOTE")%>&status=<%=CustcrdnoteHdr.get("CREDIT_STATUS")%>";
				});
				   
			 $("#bill-delete").click(function() {    
				 var creditnoteHdrId ="<%=CustcrdnoteHdr.get("ID")%>";
				  var cnstatus ="<%=CustcrdnoteHdr.get("CREDIT_STATUS")%>";
				  if(cnstatus.toLowerCase() == "draft" || cnstatus.toLowerCase() == "open" || cnstatus.toLowerCase() == "cancelled"){
					  $('#deletecreditnote').modal('show');
				  }else{
					  result = "Once Credit Note payment Apply Credit in other payment not allow to delete. <a href='invoicePaymentDetail.jsp?ID=<%=payhdrid%>' target='_blank'>CLICK HERE</a> or the payment details";
					  window.location.href = "../creditnote/detail?custcreditid="+creditnoteHdrId+"&resultnew="+ result;
				  } 
			 });
			 
			$("#cfmdelete").click(function(){
				 window.location.href = "/track/CustomerCreditNoteServlet?ACTION=deletecreditnote&CreditnoteHdrId=<%=CustcrdnoteHdr.get("ID")%>&CreditNote=<%=CustcrdnoteHdr.get("CREDITNOTE")%>&status=<%=CustcrdnoteHdr.get("CREDIT_STATUS")%>";
				}); 


			 <%
				Journal journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant, (String)CustcrdnoteHdr.get("CREDITNOTE"), "CUSTOMERCREDITNOTES");
			  %>
			  loadJournalDetailByJournalId('<%=rootURI%>','<%=journalHeaderDetail.getJournalHeader().getID()%>', 'journal_detail_box_body', 'journal_detail','All', '<%=numberOfDecimal%>');
			  <%-- loadJournalDetail('<%=CustcrdnoteHdr.get("CREDITNOTE")%>>', 'All'); --%>
			});
			function convertToDraft(){
				window.location.href = "/track/CustomerCreditNoteServlet?ACTION=convertToDraft&CreditnoteHdrId=<%=CustcrdnoteHdr.get("ID")%>";
			}
			function generatePdf(dataUrl){
				
				var doc = new jsPDF('p', 'mm', 'a4');
				var pageNumber;
			
			
				var pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
				var pageWidth = doc.internal.pageSize.width || doc.internal.pageSize.getWidth();
				
				/* Top Right */
				doc.setFontSize(27);
				doc.text('CREDIT NOTE', 195, 30, {align:'right'});
			

				doc.setFontSize(10);
				//doc.setFontStyle("bold");
				doc.text('# <%=CustcrdnoteHdr.get("CREDITNOTE")%>', 195, 37, {align:'right'});
				
					doc.setFontSize(10);
					doc.text('Credits Remaining', 195, 46, {align:'right'});
	
					doc.setFontSize(12);
					doc.text('<%=curency%><%=Numbers.toMillionFormat(balanceAmount,Integer.valueOf(numberOfDecimal))%>', 195, 52, {align:'right'});
				
				

				doc.autoTable({
					html : '#table1',
					startY : 83,
					margin : {left : 132},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
					theme : 'plain'
				});
				/* **** */

				/* Top Left */
				
				doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
				
				doc.setFontSize(10);
				doc.setFontStyle("bold");
				doc.text('<%=PLNTDESC%>', 16, 46);

				doc.setFontSize(9);
				doc.setFontStyle("normal");
				doc.text('<%=fromAddress_BlockAddress%>', 16, 50);

				doc.text('<%=fromAddress_RoadAddress%>', 16, 54);

				doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);

				//doc.text('United Arab Emirates', 16, 73);

				doc.setFontSize(10);
				doc.text('Invoice To', 16, 90);

				doc.setFontStyle("bold");
				var splitcustname = doc.splitTextToSize('<%=custName%>', 100);
				doc.text(splitcustname, 16, 94);
				
				doc.setFontStyle("normal");
				<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
				doc.text('Project : <%=projectname%>', 16, 103);
				<%} %>
				/* **** */
				var totalPagesExp = "{total_pages_cont_string}";
				doc.fromHTML(document.getElementById('table'));
				doc.autoTable({
					html : '#table2',
					startY : 110,
					headStyles : {
						fillColor : [ 0, 0, 0 ],
						textColor : [ 255, 255, 255 ],
						halign : 'center'
					},
					bodyStyles : {
						fillColor : [ 255, 255, 255 ],
						textColor : [ 0, 0, 0 ]
					},
					theme : 'plain',
					columnStyles: {0: {halign: 'center'},1: {halign: 'center'},2: {halign: 'center'},3: {halign: 'center'},4: {halign: 'center'},5: {halign: 'right'}},
					didDrawPage : function(data) {
						doc.setFontStyle("normal");
						// Footer
						pageNumber = doc.internal.getNumberOfPages();
						var str = "Page " + doc.internal.getNumberOfPages();
						// Total page number plugin only available in jspdf v1.0+
						if (typeof doc.putTotalPages === 'function') {
							str = str + " of " + totalPagesExp;
						}
						doc.setFontSize(10);

						// jsPDF 1.4+ uses getWidth, <1.4 uses .width
						var pageSize = doc.internal.pageSize;
						var pageHeight = pageSize.height ? pageSize.height
								: pageSize.getHeight();
						doc.text(str, 180,
								pageHeight - 10);
					}
				});

				let finalY = doc.previousAutoTable.finalY;

				doc.autoTable({
					html : '#table4',
					margin : {left : 123},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
					theme : 'plain'
				});
				
				doc.setFontStyle("normal");
				if(pageNumber < doc.internal.getNumberOfPages()){
					// Footer
					var str = "Page " + doc.internal.getNumberOfPages()
					// Total page number plugin only available in jspdf v1.0+
					if (typeof doc.putTotalPages === 'function') {
						str = str + " of " + totalPagesExp;
					}
					doc.setFontSize(10);
	
					// jsPDF 1.4+ uses getWidth, <1.4 uses .width
					var pageSize = doc.internal.pageSize;
					var pageHeight = pageSize.height ? pageSize.height
							: pageSize.getHeight();
					doc.text(str, 16, pageHeight - 10);
				}
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					doc.putTotalPages(totalPagesExp);
				}
				doc.save('CustomerCreditNotes.pdf');
			}
			function generate() {
				
			var img = toDataURL($("#logo_content").attr("src"),
					function(dataUrl) {
						generatePdf(dataUrl);
				  	},'image/jpeg');
				
			}
			
			function toDataURL(src, callback, outputFormat) {
				  var img = new Image();
				  img.crossOrigin = 'Anonymous';
				  img.onload = function() {
				    var canvas = document.createElement('CANVAS');
				    var ctx = canvas.getContext('2d');
				    var dataURL;
				    canvas.height = this.naturalHeight;
				    canvas.width = this.naturalWidth;
				    ctx.drawImage(this, 0, 0);
				    dataURL = canvas.toDataURL(outputFormat);
				    callback(dataURL);
				  };
				  img.src = src;
				  if (img.complete || img.complete === undefined) {
				    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
				    img.src = src;
				  }
			}
			
			$(function(){
			    $('#convertdrafttoopen').click(function() {
			    	$("#contoopen").submit();
			    });
			});
			
			
		</script>
<script src="<%=rootURI%>/jsp/js/tabulator.min.js"></script>
<script src="<%=rootURI%>/jsp/js/journalutil.js"></script>
		<form id="contoopen" class="form-horizontal" name="contoopen" action="/track/CustomerCreditNoteServlet?ACTION=CONVERT_CREDIT_NOTE_DRAFT_TO_OPEN"  method="post">
			<input type="hidden" name="PLANT" value="<%=plant%>" >
			<input type="hidden" name="CRHDRID" value="<%=custcrdnoteHDRid%>" >
		</form>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>