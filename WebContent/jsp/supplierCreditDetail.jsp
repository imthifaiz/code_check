<%@page import="com.track.db.object.Journal"%>
<%@page import="com.track.serviceImplementation.JournalEntry"%>
<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="com.track.db.util.ItemUtil"%>
<%@page import="com.track.db.util.InvUtil"%>
<%@page import="com.track.util.*"%>
<%@page import="com.track.dao.*"%>
<%@page import="com.track.constants.*"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="com.track.db.object.FinProject"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@page import="java.util.Set"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<%@include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! 
	@SuppressWarnings({"rawtypes", "unchecked"}) 
%>
<%
	String title = "Debit Note Detail";
	String rootURI = HttpUtils.getRootURI(request);
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String basecurrency = curency;
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc=StrUtils.fString(request.getParameter("result"));
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
	
	boolean displaySummaryEdit=false,displayPdfPrint=false,displayConvertToInvoice=false,displaySummaryMore=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displaySummaryEdit = ub.isCheckValAcc("editSupplierCreditNote", plant,username);
		displayPdfPrint = ub.isCheckValAcc("printSupplierCreditNote", plant,username);
		displaySummaryMore = ub.isCheckValAcc("moreSupplierCreditNote", plant,username);	
	}
	
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	SupplierCreditUtil SCUtil = new SupplierCreditUtil();
	SupplierCreditDAO SCDAO = new SupplierCreditDAO();
	BillDAO billDAO = new BillDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String COMP_INDUSTRY = _PlantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
	String billHdrId = StrUtils.fString(request.getParameter("TRANID"));
	Hashtable ht = new Hashtable();
	ht.put("ID", billHdrId);
	ht.put("PLANT", plant);
	//List billHdrList =  SCUtil.getHdrById(ht);
	List billHdrList =  SCDAO.getConvHdrById(ht);
	Map billHdr=(Map)billHdrList.get(0);
	ht.put("HDRID", billHdrId);
	//List billDetList =  SCUtil.getDetByHdrId(ht);
	List billDetList =  SCDAO.getConvDetByHdrId(ht);
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
	String totalAmount= (String) billHdr.get("TOTAL_AMOUNT");
	double dTotalAmount ="".equals(totalAmount) ? 0.0d :  Double.parseDouble(totalAmount);
	totalAmount = StrUtils.addZeroes(dTotalAmount, numberOfDecimal);
	String subTotal = (String) billHdr.get("SUB_TOTAL");
	double dSubTotal ="".equals(subTotal) ? 0.0d :  Double.parseDouble(subTotal);
	subTotal = StrUtils.addZeroes(dSubTotal, numberOfDecimal);	
	
	/* String orderdiscount = (String) billHdr.get("ORDER_DISCOUNT");
	double dorderdiscount ="".equals(orderdiscount) ? 0.0d :  Double.parseDouble(orderdiscount);
	String ordisc = StrUtils.addZeroes(dorderdiscount, "3");
	double dorderdiscountcost = (dSubTotal/100)*dorderdiscount;
	String orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
	orderdiscount = StrUtils.addZeroes(Double.parseDouble(orderdiscount), "3"); */
	
	String orderdiscounttype = (String) billHdr.get("ORDERDISCOUNTTYPE");
	String orderdiscountcost = "0";
	String ordisc = "";
	if(orderdiscounttype.equalsIgnoreCase("%")){
		String orderdiscount = (String) billHdr.get("ORDER_DISCOUNT");
		double dorderdiscount ="".equals(orderdiscount) ? 0.0d :  Double.parseDouble(orderdiscount);
		ordisc = StrUtils.addZeroes(dorderdiscount, "3");
		double dorderdiscountcost = (dSubTotal/100)*dorderdiscount;
		orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
	}else{
		String orderdiscount = (String) billHdr.get("ORDER_DISCOUNT");
		double dorderdiscountcost ="".equals(orderdiscount) ? 0.0d :  Double.parseDouble(orderdiscount);
		orderdiscountcost = StrUtils.addZeroes(dorderdiscountcost, numberOfDecimal);
	}
	
	String discounttype = (String) billHdr.get("DISCOUNT_TYPE");
	String discount = "0";
	if(discounttype.equalsIgnoreCase("%")){
		discount = (String) billHdr.get("DISCOUNT");
		double dDiscount ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
		double discountpercent = ((dSubTotal)/100)*dDiscount;
		discount = StrUtils.addZeroes(discountpercent, numberOfDecimal);
	}else{
		discount = (String) billHdr.get("DISCOUNT");
		double dDiscount ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
		discount = StrUtils.addZeroes(dDiscount, numberOfDecimal);
	}
	

	
	
	String adjustment = (String) billHdr.get("ADJUSTMENT");
	double dAdjustment ="".equals(adjustment) ? 0.0d :  Double.parseDouble(adjustment);
	adjustment = StrUtils.addZeroes(dAdjustment, numberOfDecimal);
	

	String CURRENCYUSEQT=(String) billHdr.get("CURRENCYUSEQT");
	
	BillPaymentDAO billDao = new BillPaymentDAO();
	ht.put("REFERENCE", billHdr.get("CREDITNOTE"));
	ht.put("VENDNO", billHdr.get("VENDNO"));
	double dBalanceAmount =0;
	String paymentMade = "0"; 
	double dPaymentMade=0.0;
	List creditDetailList = billDao.getCreditDetails(ht);
	if(creditDetailList.size()>0)
	{
	Map creditHdr=(Map)creditDetailList.get(0);
	String balanceAmount = (String) creditHdr.get("BALANCE");	
	dBalanceAmount ="".equals(balanceAmount) ? 0.0d :  Double.parseDouble(balanceAmount);
	balanceAmount = StrUtils.addZeroes((dBalanceAmount * Float.parseFloat((String) creditHdr.get("CURRENCYUSEQT"))), numberOfDecimal);
	dPaymentMade =(Double.parseDouble(totalAmount)-Double.parseDouble(balanceAmount));
	}
	
	
	paymentMade = StrUtils.addZeroes(dPaymentMade, numberOfDecimal);
	
	double dbalanceDue = dTotalAmount - dPaymentMade;
	String balanceDue = StrUtils.addZeroes(dbalanceDue, numberOfDecimal);
	
	Hashtable htHDR = new Hashtable();	
	htHDR.put("REFERENCE",billHdr.get("CREDITNOTE"));	
	String sqlHDR = "SELECT * FROM "+plant+"_FINPAYMENTHDR  WHERE PLANT='"+plant+"' ";
	String payid = "0";
	List arrList = billDAO.selectForReport(sqlHDR, htHDR, "");
	if(creditDetailList.size()>0){
		Map grnbillHdr=(Map)arrList.get(0);
		payid = (String)grnbillHdr.get("ID");
	}
		
	curency = (String) billHdr.get("DISPLAY");
	
	String isdisctax= (String) billHdr.get("ISDISCOUNTTAX");
	String isoddisctax= (String) billHdr.get("ISORDERDISCOUNTTAX");
	
	String showdsictax ="";
	String showorddisctax ="";
	
	 if(isdisctax.equalsIgnoreCase("1")){
		 showdsictax = "(Tax Inclusive)";
	  }else{
		  showdsictax = "(Tax Exclusive)";
	  } 
	 
	 if(isoddisctax.equalsIgnoreCase("1")){
		 showorddisctax = "(Tax Inclusive)";
	  }else{
		  showorddisctax = "(Tax Exclusive)";
	  } 
	String taxtodiaplay = "";
	String taxamount = (String) billHdr.get("TAXAMOUNT");
	double ptaxamount ="".equals(taxamount) ? 0.0d :  Double.parseDouble(taxamount);
	taxamount = StrUtils.addZeroes(ptaxamount, numberOfDecimal);
	
	String taxidsting = (String) billHdr.get("TAXID");
	int taxid = Integer.valueOf(taxidsting);
	FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
	FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxid);
	
	FinProjectDAO finProjectDAO = new FinProjectDAO();
	FinProject finProject=new FinProject();
	String projectname = "";
	if(Integer.valueOf((String) billHdr.get("PROJECTID")) > 0){
		finProject = finProjectDAO.getFinProjectById(plant, Integer.valueOf((String) billHdr.get("PROJECTID")));
		projectname = finProject.getPROJECT_NAME();
	}
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	 <jsp:param name="submenu" value="<%=IConstants.PURCHASE_CREDIT_NOTES%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/font-awesome.min.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
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

@media print {
  @page { margin: 0; }
  body { margin: 1.6cm; }
}
</style>
<center>
	<h2><small><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
<%if(resultnew.equals("") || resultnew == null){}else{ %>
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
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../debit/summary"><span class="underline-on-hover">Debit Note Summary</span> </a></li>                            
                <li><label>Debit Note Detail</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=billHdr.get("CREDITNOTE")%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
				<% if (displaySummaryEdit) { %>
					<button type="button" class="btn btn-default" 
					onclick="window.location.href='../debit/edit?action=View&BILL_HDR=<%=billHdrId%>'" 
					 data-toggle="tooltip"  data-placement="bottom" title="Edit">
						<i class="fa fa-pencil" aria-hidden="true"></i>
					</button>
					<% } %>
					<% if (displayPdfPrint) { %>
					<button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<% } %>
					<% if (displayPdfPrint) { %>
					<button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					<% }%>
				</div>
				&nbsp;
				<div class="btn-group" role="group">
					<%if(billHdr.get("CREDIT_STATUS").equals("Draft")){ %>
						<a href="/track/SupplierCreditServlet?ACTION=CONVERT_TO_OPEN&PLANT=<%=plant%>&ID=<%=billHdrId%>">
							<button type="button pull-right" class="btn btn-success">Convert To Open</button>
						</a>
					<%} %>
						
				</div>	
				<%if(billHdr.get("CREDIT_STATUS").equals("CANCELLED")){ %>
				&nbsp;
				<div class="btn-group" role="group">
					<button type="button pull-right" class="btn btn-success" onclick="convertToDraft()">Convert to Draft</button>
				</div>
				<%} %>
				&nbsp;
				<div class="btn-group" role="group">
					  <!-- <button class="btn btn-default dropdown-toggle" type="button pull-right" data-toggle="dropdown">More</button>
					  <ul class="dropdown-menu">
					    <li><a href="#">Cancel</a></li>
					    <li><a href="#">Delete</a></li>
					  </ul> -->
					  <% if (displaySummaryMore) { %>
					  <button type="button" class="btn btn-default" data-toggle="dropdown" >More <span class="caret"></span></button>
					   <ul class="dropdown-menu" style="min-width: 0px;">
					    <li id="bill-copy"><a href="../debit/copy?action=View&BILL_HDR=<%=billHdrId%>">Copy</a></li>
					    <li id="bill-cancel"><a href="#">Cancel</a></li>
					    <li id="bill-delete"><a href="#">Delete</a></li>
					  </ul>
					  <% } %>
				</div>			
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onClick="window.location.href='../debit/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;">
		<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-draft"><%=billHdr.get("CREDIT_STATUS")%></div>
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
				<h1>DEBIT NOTE</h1>
				<p>DebitNote# <%=billHdr.get("CREDITNOTE")%></p>
				<br> <span>Credits Remaining</span>
				<h3><%=curency%><%=Numbers.toMillionFormat(balanceDue, Integer.valueOf(numberOfDecimal))%></h3>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-8">
				<span>Supplier Address</span>	<br>			
				<a href="#"><%=billHdr.get("VNAME")%></a><br>
				<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
				<span>Project : <%=projectname %></span><br>
				<%} %>			
			</div>
			<div class="col-xs-4 text-right">
				<table id="table1" class="table pull-right">
					<tbody>
						<tr>
							<td>Debit Note Date :</td>
							<td><%=billHdr.get("CREDIT_DATE")%></td>
						</tr>
						<tr>
							<td>Terms :</td>
							<td><%=billHdr.get("PAYMENT_TERMS")%></td>
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
					<%for(int i =0; i<billDetList.size(); i++) {   
				  		Map m=(Map)billDetList.get(i);
				  		String qty="", cost="", amount="", percentage="", tax="";
				  		
			  			qty = (String) m.get("QTY");
			  			double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
			  			qty = StrUtils.addZeroes(dQty, "3");
			  			
			  			cost = (String) m.get("COST");
			  			double dCost ="".equals(cost) ? 0.0d :  Double.parseDouble(cost);
			  			cost = StrUtils.addZeroes(dCost, numberOfDecimal);
			  			
			  			amount = (String) m.get("AMOUNT");
			  			double dAmount ="".equals(amount) ? 0.0d :  Double.parseDouble(amount);
			  			amount = StrUtils.addZeroes(dAmount, numberOfDecimal);
			  			if(!((String)m.get("TAX_TYPE")).equalsIgnoreCase("")){
			  			//percentage = (String)m.get("GSTPERCENTAGE");
			  			Map<String, String> taxDetail = new HashMap();
			  			boolean match = false;
			  			String display = (String)m.get("TAX_TYPE");
			  			taxtodiaplay = (String)m.get("TAX_TYPE");
			  			/* percentage = display.substring(display.indexOf("[")+1, display.lastIndexOf("%"));
			  			double dPercntage = "".equals(cost) ? 0.0d :  Double.parseDouble(percentage);
			  			
			  			double dTax = (dAmount * (dPercntage / 100));
			  			tax = StrUtils.addZeroes(dTax, numberOfDecimal);
			  					
			  			
			  			taxDetail.put("name", display);
			  			taxDetail.put("types", (String)m.get("TAX_TYPE"));
			  			taxDetail.put("tax", tax);
			  			
			  			if(taxList.size() == 0){
			  				taxList.add(taxDetail);
			  			}else{
			  				for(int j =0; j < taxList.size(); j++) {
			  					Map tMap=(Map)taxList.get(j);
			  					if(((String)tMap.get("types")).equalsIgnoreCase((String)m.get("TAX_TYPE"))){			  						
			  						dTax = dTax + Double.parseDouble((String)tMap.get("tax"));
			  						tax = StrUtils.addZeroes(dTax, numberOfDecimal);
			  						tMap.put("tax", tax);
			  						match = true;
			  					}
			  				}
			  				if(!match){
				  				taxList.add(taxDetail);
				  			}
			  			} */
		  			}
			  			
			  			String linediscount = (String)m.get("DISCOUNT");
			  			String linediscounytype = (String) m.get("DISCOUNT_TYPE");
			  			if(linediscounytype.equalsIgnoreCase("%")){
			  				linediscount = StrUtils.addZeroes(Double.parseDouble(linediscount), "3");	
			  			}else{
			  				linediscount = StrUtils.addZeroes(Double.parseDouble(linediscount), numberOfDecimal);	
			  			}
			  		%>
				  		<tr>
							<td class="text-center"><%=m.get("LNNO") %></td>
							<td class="text-center"><%=m.get("ITEM") %></td>
							<td class="text-center"><%=qty%></td>
							<td class="text-center"><%=Numbers.toMillionFormat(cost, Integer.valueOf(numberOfDecimal)) %></td>
							<td class="text-center"><%=Numbers.toMillionFormat(linediscount, Integer.valueOf(numberOfDecimal)) %><%=linediscounytype %></td>
							<td class="text-right"><%=Numbers.toMillionFormat(amount, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<tr>
          					<td></td>
          					<td colspan="6">&emsp;&emsp;&emsp;<%=m.get("ITEMDESC") %></td>
						</tr>
				  	<%}%>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<!-- <div class="col-xs-6"></div> -->
			<div class="col-xs-12">
				<table id="table3" class="table text-right" style="margin-bottom: 0px;">
					<tbody>
						<tr>
							<td>Sub Total</td>
							<td><%=Numbers.toMillionFormat(subTotal, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
							<%if(orderdiscounttype.equalsIgnoreCase("%")){ %>
						<tr>
							<td>Order Discount (<%=ordisc%>%) <%=showorddisctax%></td>
							<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<%}else{ %>
						<tr>
							<td>Order Discount <%=showorddisctax%></td>
							<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<%} %>
						<tr>
							<td>Discount <%=showdsictax%></td>
							<td>(-) <%=Numbers.toMillionFormat(discount, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
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
								
								String displaydisc = (String)tMap.get("types");
					  			
					  			String percentagedisc = displaydisc.substring(displaydisc.indexOf("[")+1, displaydisc.lastIndexOf("%"));
					  			double dPercntage = Double.parseDouble(percentagedisc);	
					  			if(dPercntage > 0){
					  			double dTaxdisc = (Double.parseDouble(discount) * (dPercntage / 100));
					  			double dTaxorderdiscount = (Double.parseDouble(orderdiscountcost) * (dPercntage / 100));
					  			
					  			double dfinaltax = Double.parseDouble((String)tMap.get("tax")) - (dTaxdisc+dTaxorderdiscount);
					  			String finaltax = StrUtils.addZeroes(dfinaltax, numberOfDecimal);
						%>
							<tr>
								<td><%=(String)tMap.get("name")%></td>
								<td><%=finaltax%></td>
							</tr>
						<%}else{
							String finaltax = StrUtils.addZeroes(Double.parseDouble((String)tMap.get("tax")), numberOfDecimal);
						%>
							<tr>
								<td><%=(String)tMap.get("name")%></td>
								<td><%=finaltax%></td>
							</tr>
						<%}
					  			}
							}
						}
						%> --%>
						<%if(taxid != 0){if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td><%=taxtodiaplay%></td>
											<td><%=Numbers.toMillionFormat(taxamount, Integer.valueOf(numberOfDecimal)) %></td>
										</tr>
						<%} }%>
						<tr>
							<td>Adjustment</td>
							<td><%=Numbers.toMillionFormat(adjustment, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<tr>
							<td><b>Total (<%=billHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(totalAmount, Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)billHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: July 30,2021  Description:  Total of Local Currency -->
						<tr id="showtotalcur">
							<td><b>Total (<%=basecurrency%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(StrUtils.addZeroes((dTotalAmount/Double.parseDouble(CURRENCYUSEQT)), numberOfDecimal), Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<%}%>
						<tr>
							<td>Payments Made (<%=billHdr.get("CURRENCYID")%>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(paymentMade, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)billHdr.get("CURRENCYID"))) {%>
						<tr id="showtotalPay">
							<td>Payments Made (<%=basecurrency%>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(StrUtils.addZeroes((dPaymentMade/Double.parseDouble(CURRENCYUSEQT)), numberOfDecimal), Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<%}%>
						<tr class="grey-bg">
							<td><b>Balance Due (<%=billHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(balanceDue, Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<%if(!basecurrency.equalsIgnoreCase((String)billHdr.get("CURRENCYID"))) {%>
						<tr class="grey-bg" id="showtotalBal">
							<td><b>Balance Due (<%=basecurrency%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(StrUtils.addZeroes((dbalanceDue/Double.parseDouble(CURRENCYUSEQT)), numberOfDecimal), Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<%}%>
					</tbody>
				</table>
				
				<table id="table4" class="table text-right" style="display: none"><!--  Author: Azees  Create date: July 31,2021  Description:  Total of Local Currency -->
					<tbody>
						<tr>
							<td>Sub Total</td>
							<td><%=Numbers.toMillionFormat(subTotal, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
							<%if(orderdiscounttype.equalsIgnoreCase("%")){ %>
						<tr>
							<td>Order Discount (<%=ordisc%>%) <%=showorddisctax%></td>
							<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<%}else{ %>
						<tr>
							<td>Order Discount <%=showorddisctax%></td>
							<td>(-) <%=Numbers.toMillionFormat(orderdiscountcost, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<%} %>
						<tr>
							<td>Discount <%=showdsictax%></td>
							<td>(-) <%=Numbers.toMillionFormat(discount, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<%if(taxid != 0){if(fintaxtype.getSHOWTAX() == 1){%>
										<tr>
											<td><%=taxtodiaplay%></td>
											<td><%=Numbers.toMillionFormat(taxamount, Integer.valueOf(numberOfDecimal)) %></td>
										</tr>
						<%} }%>
						<tr>
							<td>Adjustment</td>
							<td><%=Numbers.toMillionFormat(adjustment, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<tr>
							<td><b>Total (<%=billHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(totalAmount, Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
						<tr>
							<td>Payments Made (<%=billHdr.get("CURRENCYID")%>)</td>
							<td style="color:red;">(-) <%=Numbers.toMillionFormat(paymentMade, Integer.valueOf(numberOfDecimal)) %></td>
						</tr>
						<tr class="grey-bg">
							<td><b>Balance Due(<%=billHdr.get("CURRENCYID")%>)</b></td>
							<td><b><%=Numbers.toMillionFormat(balanceDue, Integer.valueOf(numberOfDecimal)) %></b></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		</span>
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
									<small class="text-muted">Amount is displayed in your debit note currency</small>&nbsp;<span class="badge text-semibold badge-success"><%=billHdr.get("CURRENCYID")%></span>
									<!---->
								</div>
								<p class="font-large">
									<b>  <!---->
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
			      <p> Are you sure about cancel the debit note?</p>
			      
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
			      <p> Deleted debit note information cannot be retrieved. Are you sure about deleting ?</p>
			      
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
			  var numberOfDecimal = $("#numberOfDecimal").val();
			  setTimeout(function() {
				    $('.alert').fadeOut('fast');
				}, 4000);
			  $('[data-toggle="tooltip"]').tooltip();
			  $('.printMe').click(function(){
				  <%if(!basecurrency.equalsIgnoreCase((String)billHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: July 31,2021  Description:  Total of Local Currency -->
				  document.getElementById('showtotalcur').style.display = 'none';
				  document.getElementById('showtotalPay').style.display = 'none';
				  document.getElementById('showtotalBal').style.display = 'none';
				  <%}%>
				     $("#print_id").print();
				     <%if(!basecurrency.equalsIgnoreCase((String)billHdr.get("CURRENCYID"))) {%><!--  Author: Azees  Create date: July 31,2021  Description:  Total of Local Currency -->
				     document.getElementById('showtotalcur').style.display = 'contents';
				     document.getElementById('showtotalPay').style.display = 'contents';
				     document.getElementById('showtotalBal').style.display = 'contents';
					  <%}%>
				});	
			  
			  $("#bill-cancel").click(function() {
				  var creditnoteHdrId ="<%=billHdr.get("ID")%>";
				  var cnstatus ="<%=billHdr.get("CREDIT_STATUS")%>";
				  
				  if(cnstatus.toLowerCase() == "draft" || cnstatus.toLowerCase() == "open" ){
					  $('#cancelcreditnote').modal('show');
				  }else if(cnstatus.toLowerCase() == "cancelled"){
					  result = "Debit note already cancelled.";
					  window.location.href = "../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result;
				  }else{
					  result = "Once Debit Note payment Apply Credit in other payment not allow to cancel. <a href='billPaymentDetail.jsp?TRANID=<%=payid%>' target='_blank'>CLICK HERE</a> or the payment details";
					  window.location.href = "../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result; 
				  }
			  });
			  
			  $("#cfmcancel").click(function(){
				  window.location.href = "/track/SupplierCreditServlet?ACTION=convertToCancel&CreditnoteHdrId=<%=billHdr.get("ID")%>&CreditNote=<%=billHdr.get("CREDITNOTE")%>&status=<%=billHdr.get("CREDIT_STATUS")%>";
				});
				   
			 $("#bill-delete").click(function() {    
				 var creditnoteHdrId ="<%=billHdr.get("ID")%>";
				  var cnstatus ="<%=billHdr.get("CREDIT_STATUS")%>";
				  
				  if(cnstatus.toLowerCase() == "draft" || cnstatus.toLowerCase() == "cancelled" || cnstatus.toLowerCase() == "open"){
					  $('#deletecreditnote').modal('show');
				  }else{
					  result = "Once Debit Note payment Apply Credit in other payment not allow to delete. <a href='billPaymentDetail.jsp?TRANID=<%=payid%>' target='_blank'>CLICK HERE</a> or the payment details";
					  window.location.href = "../debit/detail?TRANID="+creditnoteHdrId+"&resultnew="+ result; 
				  }
			 });
			 
			 $("#cfmdelete").click(function(){
				 window.location.href = "/track/SupplierCreditServlet?ACTION=deletecreditnote&CreditnoteHdrId=<%=billHdr.get("ID")%>&CreditNote=<%=billHdr.get("CREDITNOTE")%>&status=<%=billHdr.get("CREDIT_STATUS")%>";
				});
			 
			 <%
				Journal journalHeaderDetail = new JournalEntry().getJournalByTransactionId(plant, billHdrId, "SUPPLIERCREDITNOTES");
			  %>
			  loadJournalDetailByJournalId('<%=rootURI%>','<%=journalHeaderDetail.getJournalHeader().getID()%>', 'journal_detail_box_body', 'journal_detail','All', '<%=numberOfDecimal%>');
			});
						
			function generatePdf(dataUrl){
				var doc = new jsPDF('p', 'mm', 'a4');
				var pageNumber;
				/* Top Right */
				doc.setFontSize(22);
				doc.text('DEBIT NOTE', 195, 30, {align:'right'});

				doc.setFontSize(10);
				doc.setFontStyle("bold");
				doc.text('DebitNote# <%=billHdr.get("CREDITNOTE")%>', 195, 37, {align:'right'});

				doc.setFontSize(8);
				doc.text('Credits Remaining', 195, 48, {align:'right'});

				doc.setFontSize(12);
				doc.text('<%=curency%><%=balanceDue%>', 195, 55, {align:'right'});

				doc.autoTable({
					html : '#table1',
					startY : 83,
					margin : {left : 142},
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
				doc.text('Supplier Address', 16, 90);

				//doc.setFontStyle("bold");
				var splitvendname = doc.splitTextToSize('<%=billHdr.get("VNAME")%>', 100);
				doc.text(splitvendname, 16, 95);
				
				doc.setFontSize(10);
				<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
				doc.text('Project : <%=projectname%>', 16, 100);
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
						doc.text(str, 178,
								pageHeight - 10);
					}
				});

				let finalY = doc.previousAutoTable.finalY;

				doc.autoTable({
					html : '#table4',
					margin : {left : 95},
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
				doc.save('SupplierCreditNotes.pdf');
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
			
			function convertToDraft(){
				window.location.href = "/track/SupplierCreditServlet?ACTION=convertToDraft&billHdrId=<%=billHdr.get("ID")%>";
			}
</script>
<script src="<%=rootURI%>/jsp/js/tabulator.min.js"></script>		
<script src="<%=rootURI%>/jsp/js/journalutil.js"></script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>