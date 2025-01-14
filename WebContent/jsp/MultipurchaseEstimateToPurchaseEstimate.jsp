<!-- Created By Resvi 07.09.21 -->
<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.db.object.MultiPoEstDet"%>
<%@page import="com.track.db.object.MultiPoEstHdr"%>
<%@page import="com.track.db.object.PoHdr"%>
<%@page import="com.track.db.object.PoDet"%>
<%@page import="com.track.db.object.FinProject"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<%
	String title = "Convert To Purchase Estimate";

    String plant    = StrUtils.fString((String)session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String region = StrUtils.fString((String) session.getAttribute("REGION"));

	//Validate no.of Supplier -- Azees 22.11.2020
		CustMstDAO custdao = new CustMstDAO();
		CustomerBeanDAO venddao = new CustomerBeanDAO();
		String NOOFCUSTOMER=((String) session.getAttribute("NOOFCUSTOMER"));
		String NOOFSUPPLIER=((String) session.getAttribute("NOOFSUPPLIER"));
		String NOOFORDER=((String) session.getAttribute("NOOFORDER"));
		String ValidNumber="",SupValidNumber="",OrdValidNumber="";

		//Validate Order azees - 12/2020
		PoHdrDAO poHdrDAO = new PoHdrDAO();
		DateUtils _dateUtils = new DateUtils();
		String FROM_DATE = DateUtils.getDate();
		if (FROM_DATE.length() > 5)
			FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";

		String TO_DATE = DateUtils.getLastDayOfMonth();
		if (TO_DATE.length() > 5)
			TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);

		int noordvalid =poHdrDAO.Ordercount(plant,FROM_DATE,TO_DATE);
		if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
		{
			int convl = Integer.valueOf(NOOFORDER);
			if(noordvalid>=convl)
			{
				OrdValidNumber=NOOFORDER;
			}
		}

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
		int nosupvalid =venddao.Vendorcount(plant);
		if(!NOOFSUPPLIER.equalsIgnoreCase("Unlimited"))
		{
			int convl = Integer.valueOf(NOOFSUPPLIER);
			if(nosupvalid>=convl)
			{
				SupValidNumber=NOOFSUPPLIER;
			}
		}
	/* String msg = (String)request.getAttribute("Msg"); */
	String msg = StrUtils.fString(request.getParameter("Msg"));
	String POMULTIESTNO = (String)request.getAttribute("POMULTIESTNO");
	String qtyest = StrUtils.fString(request.getParameter("ESTQTY")).trim();
	String qtyRc = StrUtils.fString(request.getParameter("QTYRC")).trim();
	MultiPoEstDet multiPoEstDet = new MultiPoEstDet();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	CurrencyDAO currencyDAO = new CurrencyDAO();
	MultiPoEstHdrDAO multiPoEstHdrDAO = new MultiPoEstHdrDAO();
	multiPoEstDetDAO MultiPoEstDetDAO = new multiPoEstDetDAO();
	MasterUtil masterUtil = new MasterUtil();
	FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();

	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String taxbylabel = ub.getTaxByLable(plant);
	String collectionDate=DateUtils.getDate();
	String zeroval = StrUtils.addZeroes(0.0, numberOfDecimal);
	String zerovalper = StrUtils.addZeroes(Float.parseFloat("0"), "3");
	String collectionTime=DateUtils.getTimeHHmm();
	String currency=plantMstDAO.getBaseCurrency(plant);


		
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);

	String gst = sb.getGST("PURCHASE",plant);
	float gstVatValue ="".equals(gst) ? 0.0f :  Float.parseFloat(gst);
	if(gstVatValue==0f){
		gst="0.000";
	}else{
		gst=gst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}
	MultiPoEstHdr poheader = multiPoEstHdrDAO.getPoHdrByPono(plant, POMULTIESTNO);
	List<MultiPoEstDet> podetail = MultiPoEstDetDAO.getPoDetByPono(plant, POMULTIESTNO);

	String STATEPREFIX ="";
	String COUNTRYLOC ="";
	if(poheader.getPURCHASE_LOCATION().length()>0){
		ArrayList sprefix =  masterUtil.getSalesLocationByState(poheader.getPURCHASE_LOCATION(), plant, "");
		Map msprefix=(Map)sprefix.get(0);
		STATEPREFIX = (String)msprefix.get("PREFIX");
		COUNTRYLOC = (String)msprefix.get("COUNTRY");
	}


	multiPurchaseEstAttachDAO MultiPurchaseEstAttachDAO=new multiPurchaseEstAttachDAO();
	List multipurchaseAttachmentList= MultiPurchaseEstAttachDAO .getpurchaseAttachByPONO(plant, POMULTIESTNO);
	
	 ArrayList countryList = new MasterUtil().getCountryList("",plant,region);
	 ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
	 ArrayList bankList =  new MasterUtil().getBankList("",plant);
	 POUtil posUtil = new POUtil();
	 Map mcost= posUtil.getPOReceiptInvoiceHdrDetails(plant);
	 String prepurchasecost = (String) mcost.get("SHOWPREVIOUSPURCHASECOST");
	 
	 String taxtreatment = multiPoEstDet.getTAXTREATMENT();
	 if(multiPoEstDet.getTAXTREATMENT() == null){
		 taxtreatment = "";
	 }

	FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(poheader.getTAXID());

	 String COUNTRYCODE = "";
	 String plantstate = "";
	 String plantstatecode = "";
	 String  isproductmaxqty = "";
	 PlantMstUtil plantmstutil = new PlantMstUtil();
	 String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
	 List viewlistQry = plantmstutil.getPlantMstDetails(plant);
	 for (int i = 0; i < viewlistQry.size(); i++) {
	     Map map = (Map) viewlistQry.get(i);
	     COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
	     plantstate = StrUtils.fString((String)map.get("STATE"));
	     isproductmaxqty = StrUtils.fString((String)map.get("ISPRODUCTMAXQTY"));
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
// 		currency = poheader.getCURRENCYID();

	 String CURRENCYUSEQT="0",DISPLAY="",DISPLAYID="";
	 CURRENCYUSEQT = StrUtils.addZeroes(multiPoEstDet.getCURRENCYUSEQT(), numberOfDecimal);
		String Supcurrency=plantMstDAO.getBaseCurrency(plant); //RESVI
	 List curQryList=new ArrayList();
	 curQryList = currencyDAO.getCurrencyDetails(multiPoEstDet.getCURRENCYID(),plant);
	 for(int i =0; i<curQryList.size(); i++) {
			ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
			DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
			DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));

	 }

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
			
			String empname = new EmployeeDAO().getEmpname(plant, poheader.getEMPNO(), "");
			String companyregnumber=StrUtils.fString(request.getParameter("companyregnumber"));
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASEESTIMATE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PURCHASEESTIMATE%>"/>
</jsp:include>
<style>
 .select2drop{
 	width:487px !important;
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
.bill-action {
    right: -60% !important;
}
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
 </style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/MultipurchaseEstimateToPurchaseEstimate.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../purchaseestimate/summary"><span class="underline-on-hover">Multi Purchase Estimate Summary</span> </a></li>
                <li><a href="../purchaseestimate/multipoestdetail?POMULTIESTNO=<%=poheader.getPOMULTIESTNO()%>"><span class="underline-on-hover">Multi Purchase Estimate Detail</span> </a></li>
                <li><label>Convert To Purchase Estimate</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<!-- <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../purchaseorder/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1> -->
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../purchaseestimate/multipoestdetail?POMULTIESTNO=<%=poheader.getPOMULTIESTNO()%>'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" id="purchaseOrderForm" name="cpoform" autocomplete="off" action="../purchaseestimate/converttope"  method="post"  enctype="multipart/form-data">
				<input type="hidden" name="plant" value="<%=plant%>" >
				<input type="hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>" >
				<input type = "hidden" name="PROJECTID" value="<%=poheader.getPROJECTID()%>">
				<input type = "hidden" name="TRANSPORTID" value="<%=poheader.getTRANSPORTID()%>">
				<input type="hidden" name="supplier_email" id="supplier_email" value=""/>
				<input type="hidden" name="ISAUTOGENERATE" value="false">
				<input type="hidden" name="SHIPPINGID" value="<%=poheader.getSHIPPINGID()%>">
				
				<input type="hidden" name="STATE_PREFIX" value="<%=STATEPREFIX%>" />
				<input type="hidden" name="COUNTRY" value="<%=COUNTRYLOC%>">
				<INPUT type="Hidden" name="COLLECTION_TIME" value="<%=poheader.getCollectionTime()%>"/>
				<input id="sub_total" name="sub_total" value="" type='hidden'>
				<input id="total_amount" name="total_amount" value="" type="hidden">
				<input id="taxamount" name="taxamount" value="" type="hidden">
				<input type = "hidden" name="EMAIL" value="">
				<input type = "hidden" name="REMARK2" value="<%=poheader.getRemark2()%>">
				<input type = "hidden" name="ADD4" value="">
				<input type = "hidden" name="ZIP" value="">
				<input type = "hidden" name="STATUS_ID" value="<%=poheader.getSTATUS_ID()%>">
				<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
				<input type = "hidden" name="orderstatus" value="<%=poheader.getORDER_STATUS()%>">
				<input type = "hidden" name="PONOOLD" value="<%=POMULTIESTNO%>">
				<input type = "hidden" name="precost"  value="<%=prepurchasecost%>">
				<input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
				<input type = "hidden" name="ptaxtype" value="<%=fintaxtype.getTAXTYPE()%>">
				<input type = "hidden" name="ptaxpercentage" value="<%=poheader.getINBOUND_GST()%>">
				<input type = "hidden" name="ptaxdisplay" value="">
				<input type = "hidden" name="ptaxiszero" value="<%=fintaxtype.getISZERO()%>">
				<input type = "hidden" name="ptaxisshow" value="<%=fintaxtype.getSHOWPERCENTAGE()%>">
				<input type = "hidden" name="taxid" value="<%=poheader.getTAXID()%>">
				<input type = "hidden" name="custModal">
				<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>">
				<INPUT type="hidden" name="BASECURRENCYID" value="<%=currency%>">	
				<INPUT type="hidden" name="ISPRODUCTMAXQTY"  value="<%=isproductmaxqty%>">   <!-- imthi -->			
				<%if(!ispuloc){ %>
				 <input type="hidden" id="PURCHASE_LOC" name="PURCHASE_LOC">
				<%} %>

				
				<div class="form-group shipCustomer-section" style="display: none">
					<label class="control-label col-form-label col-sm-2">Shipping Address:</label>
					<div class="col-sm-6 ac-box">
								<input type="text" class="form-control typeahead" id="SHIPPINGCUSTOMER" name="SHIPPINGCUSTOMER" value="<%=poheader.getSHIPPINGCUSTOMER()%>">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'SHIPPINGCUSTOMER\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
								
					</div>
				</div>
				
				<div class="form-group" style="display: none">
					<label class="control-label col-form-label col-sm-2">Transport Mode:</label>
					<div class="col-sm-6 ac-box">
								<input type="text" class="form-control typeahead"
									id="transport" placeholder="Select a transport"
									name="transport"  value="<%=transportmode%>">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
								
					</div>
				</div>
				<% if(COMP_INDUSTRY.equals("Construction")) {%>
				<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-6 ac-box">
								<input type="text" class="form-control typeahead"
									id="project" placeholder="Select a project"
									name="project"  onchange="checkproject(this.value)" value="<%=projectname%>">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'project\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
							
					</div>
				</div>
				<% } %>
				<div class="form-group" style="display: none">
					<label class="control-label col-form-label col-sm-2">Payment Type:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="payment_type" name="payment_type" value="<%=poheader.getPAYMENTTYPE()%>">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'payment_type\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				<div class="form-group" style="display: none;">
					<label class="control-label col-form-label col-sm-2 required">Order Number:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="POMULTIESTNO" name="POMULTIESTNO" <%-- value="<%=poheader.getPOMULTIESTNO()%>" --%> value=" ">
							<span class="input-group-addon" id="autoGen">
					   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
					   		 		<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i>
					   		 	</a>
				   		 	</span>
			   		 	</div>
					</div>
				</div>
				<div class="form-group">
					 <label class="control-label col-form-label col-sm-2" for="Reference No">Reference No:</label>
        			<div class="col-sm-4">
        				<INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="20" name="JOB_NUM" value="<%=poheader.getPOMULTIESTNO()%>" readonly>
       				</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2" for="Reference No">Order Date:</label>
        			<div class="col-sm-4">
        			<div class="input-group">
        				<INPUT class="form-control datepicker" type="TEXT" size="30" MAXLENGTH="10" name="ORDDATE" value="<%=poheader.getCollectionDate()%>"/>
       				</div>
       				</div>
				</div>
				<div class="form-group" style="display: none">
					<label class="control-label col-form-label col-sm-2" for="Delivery Date">Delivery Period/Date:</label>
        			<div class="col-sm-3">
        			<div class="input-group">
      					<INPUT type = "TEXT" size="30"  MAXLENGTH="20" id="DELDATE" name="DELDATE" value="<%=poheader.getDELDATE()%>" class="form-control"/>
    				</div>
    				</div>
    				<div class="form-inline">
       				<label class="control-label col-sm-1">
       				<input type = "checkbox" id = "DATEFORMAT" name = "DATEFORMAT" <%if(poheader.getDELIVERYDATEFORMAT() == 1){%>checked<%}%> onClick = "headerReadable();"/><font size="2.9"><b>&nbsp;By Date</b></font></label>
    				</div>
						<div class="col-sm-6 no-padding">
       				<label class="control-label col-form-label col-sm-5">Payment Terms</label>
       				<div class="col-sm-6">
       				<input type="text" class="form-control" id="payment_terms" name="payment_terms" value="<%=poheader.getPAYMENT_TERMS()%>" placeholder="Select Payment Terms">
					<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
    				</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Order Type:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="ORDERTYPE" name="ORDERTYPE" onchange="checkordertype(this.value)" value="<%=poheader.getORDERTYPE()%>">
							<span class="select-icon"
								onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
							</span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Employee:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="EMP_NAME"
							name="EMP_NAME" onchange="checkemployeess(this.value)" value="<%=empname%>"> <span
							class="select-icon"
							onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()">
							<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>				
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">INCOTERM:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" onchange="checkincoterms(this.value)" value="<%=poheader.getINCOTERMS()%>" id="INCOTERMS" name="INCOTERMS">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'INCOTERMS\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Standard <%=taxbylabelordermanagement%> :</label><!-- this is dynamic value -->
					<div class="col-sm-4">
						<%-- <div class="input-group">
							<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" value="<%=gst%>">
							<span class="input-group-addon" style="font-size: 15px; color: #0059b3">
								<b>%</b>
							</span>
			   		 	</div> --%>
			   		 	<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" onkeypress="return isNumberKey(event,this,4)"  value="<%=poheader.getINBOUND_GST()%>">
						<span class="sideiconspan"><p>%</p></span>

					</div>
				</div>
				
				<%if(ispuloc){ %>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2" for="Purchase Location">Purchase Location:</label>
        			<div class="col-sm-4">
			        	<input type="text" class="ac-selected form-control typeahead" id="PURCHASE_LOC" name="PURCHASE_LOC" value="<%=poheader.getPURCHASE_LOCATION()%>" placeholder="Select a Purchase Location">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'PURCHASE_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>

        			</div>
				</div>
				<%}%>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Purchasetax" name="Purchasetax" onchange="checktax(this.value)" placeholder="Select a Tax">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'Purchasetax\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
						<div id="CHK1">
				   		 		<br/>
	      						<input type = "checkbox" class="check" <%if(poheader.getREVERSECHARGE() == 1){%>checked<%}%>  id = "REVERSECHARGE" name = "REVERSECHARGE" /><b>&nbsp;This transaction is applicable for reverse charge </b>
	      						<br/>
	      						<input type = "checkbox" class="check" <%if(poheader.getGOODSIMPORT() == 1){%>checked<%}%>  id = "GOODSIMPORT" name = "GOODSIMPORT" /><b>&nbsp;This transaction is applicable for goods import &nbsp; &nbsp; </b>
	      					</div>
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
								<option <%if(poheader.getISTAXINCLUSIVE() == 0){%>selected<%}%> value="0">Tax Exclusive</option>
								<option <%if(poheader.getISTAXINCLUSIVE() == 1){%>selected<%}%> value="1">Tax Inclusive</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row" style="margin: 0px;">
				<div class="col-12 col-sm-12 no-padding">
  		        		<input type="Checkbox" class="form-check-input" style="border:0;" name="select" value="select" onclick="return checkAll(this.checked);">
                     	<strong>&nbsp;Select/Unselect All </strong>
                    </div>
					<table class="table table-bordered line-item-table po-table">
						<thead>
							<tr>
							<th>Chk</th>
								<th colspan=2 style="width:13%">Product</th>
								<th  style="width:9%">Supplier </th>
								<th  colspan=2 style="width:12%">Currency</th>
								<th style="width:6%">UOM</th>
								<th style="width:2%">Purchase Estimate Quantity</th>
								<th style="width:2%">Converted Quantity</th>
								<th style="width:7%">Quantity</th>
								<th colspan=2  style="width:6%">Unit Cost</th>
								<!-- <th style="width:5%">Discount</th> -->
<!-- 								<th class="bill-desc" style="width:8%">Tax</th> -->
								<th class="bill-desc" style="width:8%">Amount</th>
								<th colspan=2 style="width:15%">Est. Expiry Date</th>
								<!-- <th></th> -->
							</tr>
						</thead>
						<tbody>
							<tr>
							<td>
									<input type="Checkbox" style="border:0;background=#dddddd"
									name="chkdPOMULTIESTNO" value="">
								</td>
								<td class="item-img text-center">
									<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">
									<input type="hidden" name="lnno" value="1">
									<input type="hidden" name="itemdesc" value="">
                                    <input type="hidden" name="vendno" value="">
	                                <input type="hidden" name="nTAXTREATMENT" value="" >
									<input type="hidden" name="CURRENCYID" value="">
									<input type="hidden" name="CURRENCYUSEQTOLD" value="">
									<input type="hidden" name="unitpricerd" value="">
									<input type="hidden" name="minstkqty" value="">
									<input type="hidden" name="maxstkqty" value="">
									<input type="hidden" name="stockonhand" value="">
									<input type="hidden" name="incommingqty" value="">
									<input type="hidden" name="outgoingqty" value="">
									<input type="hidden" name="customerdiscount" value="">
									<input type="hidden" name="unitpricediscount" value="">
									<input type="hidden" name="discounttype" value="">
									<input type="hidden" name="customerdiscount" value="">
									<input type="hidden" name="minsp" value="">
									<INPUT type="hidden" name="curency"  value="">
									<input type="hidden" name="account_name" value="">
									<input type="hidden" name="item_discount" value="0.00">
									<input type="hidden" name="tax_type" class="taxSearch" value="">
									<select name="item_discounttype" hidden><option value="<%=DISPLAYID%>"><%=DISPLAYID%></option></select>
<!-- 									<INPUT type="hidden" name="POMULTIESTLNNO"  value=""> -->
								</td>
								<td class="bill-item">
									<input type="text" name="item" onchange="checkitems(this.value,this)" class="form-control itemSearch"
									placeholder="Type or click to select an item.">
									<input class="form-control"  name="ITEMDES" style="height: 23px;background-color: #fff;" readonly>
								</td>
								
								
								<td class ="bill-item">
								<INPUT class="form-control vendsearch"  name="vendname" onchange="checksupplier(this.value,this)" type="TEXT" style="width:90%"  placeholder="Supplier">
    		 				    </td>
								
							      <td class="bill-item" style="width:6%">
									<input name="CURRENCY"  type="text" onchange="checkcurrency(this.value,this)" class="form-control CURRENCYSEARCH">
    		 				       </td>
    		 				        <td style="width:6%">
    		 				        <input type="text" name="CURRENCYUSEQT"  class="form-control" READONLY>
    		 				        </td>

								<td class="bill-item">
									<input type="text" name="UOM" onchange="checkprduom(this.value,this)" class="form-control uomSearch"
									placeholder="UOM">
								</td>
								
								<td class="item-qty">
								<input type="text" name="ESTQTY" class="form-control text-right" readonly
									value="">
								</td>
								
								<td class="item-qty">
								<input type="text" name="QTYRC" class="form-control text-right" readonly
									value=" ">
								</td>
								
								<td class="item-qty">
									<input type="text" name="QTY" class="form-control text-right"
									 data-rl="0.000" data-msq="0.000" data-soh="0.000"
									  data-eq="0.000" data-aq="0.000" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
								</td>
								
								<td class="item-cost">
									<input type="text" name="unitprice" class="form-control text-right" value="<%=zeroval%>" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
								</td>
								<td class="table-icon">
									<a href="#" onclick="getPreviousPurchaseOrderDetails(this)">
										<i class="fa fa-info-circle" style="font-size: 15px;"></i>
									</a>
								</td>
								<%-- <td class="item-discount">
									<div class="row">
									<div class=" col-lg-12 col-sm-3 col-12">
									<div class="input-group my-group" style="width:120px;">
									<input name="item_discount" type="text" class="form-control text-right"
									value="0.00" onchange="calculateAmount(this)">
									<select name="item_discounttype" class="discountPicker form-control"
										onchange="calculateAmount(this)">
										<option value="<%=DISPLAY%>"><%=DISPLAY%></option>
										<option value="%">%</option>
									</select>
									</div>
									</div>
									</div>
								</td> --%>
<!-- 								<td class="bill-item"> -->
<!-- 									<input type="text" name="tax_type" class="form-control taxSearch" -->
<!-- 										placeholder="Select a Tax" readonly> -->
<!-- 								</td> -->
								<td class="item-amount text-right grey-bg">
									<input name="amount" type="text" class="form-control text-right"
									value="<%=zeroval%>" readonly="readonly">
								</td>
								<td class="item-amount grey-bg">
								<input type="text" name="EXPIREDATE" class="form-control datepicker" READONLY>									
								</td>
								<td class="table-icon">
									<a href="#" onclick="showRemarksDetails(this)">
										<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>
									</a>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<a class="add-line" style="text-decoration: none; cursor: pointer;" onclick="addRow()">
							+ Add another line
						</a>
					</div>
				</div>
				<div class="row grey-bg">

					<div class="col-sm-4"> 
						<div class="form-inline" style="display: none;">
							<label for="email">Attach Files(s)</label>
							<div class="attch-section">
								<input type="file" class="form-control input-attch"
									id="purchaseAttch" name="file" multiple="true">
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
						<div id="billAttchNote" style="display: none;">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
					</div>
					<div class="col-sm-6 notes-sec">
						<p>Remark1</p>
						<div>
							<textarea rows="2" name="REMARK1" class="ember-text-area form-control ember-view"
								placeholder="Max 100 characters"  maxlength="100"><%=poheader.getRemark1() %></textarea>
						</div>

						<p>Remark2</p>
						<div>
							<textarea rows="2" name="REMARK3" class="ember-text-area form-control ember-view"
								placeholder="Max 100 characters"  maxlength="100"><%=poheader.getREMARK3() %></textarea>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 txn-buttons">
						<div class="dropup">
						<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
					    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Save as Open
					    <span class="caret"></span></button>
					    <ul class="dropdown-menu">
					      <li><a id="btnSalesOpen" href="#">Save as Open</a></li>
<!-- 					      <li><a id="btnSalesDraft" href="#">Save as Draft</a></li> -->
					      <li><a id="btnSalesOpenEmail" href="#">Save as Open and Send Email</a></li>
					    </ul>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					  </div>
					</div>
				</div>

			</form>
		</div>
		<!-- Modal -->
		<!-- Modal -->

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
	Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER);
	PlantMstUtil plantMstUtil = new PlantMstUtil();
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
<!-- ----------------Modal-------------------------- -->
<%@include file="../jsp/newPaymentTypeModal.jsp"%>
<%@include file="../jsp/newEmployeeModal.jsp"%>
<%@include file="../jsp/newTransportModeModal.jsp"%> <!-- imti for add transport -->
<%-- <%@include file="../jsp/newProductModalPurchase.jsp" %> <!-- imti for add product --> --%>
<%@include file="../jsp/NewChartOfAccountpopup.jsp"%> <!-- imti for add account --> 
<%@include file="../jsp/newPaymentTermsModal.jsp"%> <!-- imti for add paymentterms -->
<%@include file="../jsp/newIncotermsModal.jsp"%>
<%@include file="../jsp/newOrderTypeModal.jsp"%>

<input type="text" id="PageName" style="display: none;" value="Purchase">
<input type="hidden" name="pronumberOfDecimal" id="pronumberOfDecimal" value="<%=numberOfDecimal%>">
<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>

<div id="lastTranPriceModal" class="modal fade" role="dialog">
  	<div class="modal-dialog modal-lg">
  		<div class="modal-content">
  			<div class="modal-header">
          		<button type="button" class="close" data-dismiss="modal">&times;</button>
          		<h4 class="modal-title">Previous Purchase Cost Detail</h4>
        	</div>
        	<div class="modal-body">
	          <table class="table lastPurCostDetails">
	          	<thead>
					<tr>
						<th>Order No</th>
						<th>Supplier</th>
						<th>Date</th>
						<th>Cost</th>
					</tr>
				</thead>
				<tbody>

				</tbody>
	          </table>
	        </div>
  		</div>

  	</div>
</div>

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

<div id="supplierModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form class="form-horizontal" name="form1" id="formsupplierid" method="post">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">New Supplier</h4>
				</div>
				<div class="modal-body">
					<input name="SUPPLIER_TYPE_DESC" type="hidden" value="">
					<input name="ORDER_TYPE_MODAL" type="hidden" value="PURCHASE">
					<input type="number" id="numberOfDecimal" style="display: none;"
						value=<%=numberOfDecimal%>>
						<input type="text" name="PLANT"  style="display: none;"
						value=<%=plant%>>
						<input type="text" name="plant"  style="display: none;"
						value=<%=plant%>>
						<input type="text" name="LOGIN_USER" style="display: none;"
						value=<%=username%>>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required"
							for="Create Supplier ID">Supplier Id</label>
						<div class="col-sm-4">
							<div class="input-group">
								<INPUT class="form-control" name="CUST_CODE" id="CUST_CODE"
									type="TEXT" value=""
									onchange="checkitem(this.value)" size="50" MAXLENGTH=50
									width="50"> <span
									class="input-group-addon" onClick="onIDGenSupplier();"> <a
									href="#" data-toggle="tooltip" data-placement="top"
									title="Auto-Generate"> <i class="glyphicon glyphicon-edit"
										style="font-size: 20px;"></i></a></span>
							</div>
							<INPUT type="hidden" name="CUST_CODE1" value="">
							<INPUT type="hidden" name="COUNTRY" value="">
  							<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
  							<INPUT type="hidden" name="ValidNumber" value="<%=SupValidNumber%>">
  							<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
  							<INPUT type="hidden" name="CURRENCYID_S" value="<%=DISPLAYID%>">	<!--Resvi -->
  							
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required"
							for="Supplier Name">Supplier Name</label>
						<div class="col-sm-4">

							<INPUT class="form-control" name="CUST_NAME" type="TEXT"
								value="" size="50" MAXLENGTH=100>
						</div>
					</div>
					
						<!-- 	                Author Name:Resviya ,Date:9/08/21 , Description -UEN Label     -->
          <div class="form-group" id="UEN">
	      <label class="control-label col-form-label col-sm-2 required" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
	      <div class="col-sm-4">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="companyregnumber" id="companyregnumber" type = "TEXT" value="<%=companyregnumber%>" size="50"  MAXLENGTH=100> 
	  	  </div>
	       </div>
	       </div>
	         <!-- End -->
					
					<!-- 	                Author Name:Resviya ,Date:20/07/21 -->

                      <div class="form-group">
					  <label class="control-label col-form-label col-sm-2 required">Supplier
							Currency</label>
					 <div class="col-sm-4">
					  <INPUT class="form-control" name="SUP_CURRENCY"  id="SUP_CURRENCY" type="TEXT" value="<%=Supcurrency%>"  size="50" MAXLENGTH=100>
					 </div>
					 </div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2"
							for="supplier type">Supplier Type</label>
						<div class="col-sm-4">
							<div class="input-group">
								<input name="SUPPLIER_TYPE_ID" type="TEXT"
									value="" size="50" MAXLENGTH=50
									class="form-control"> <span class="input-group-addon"
									onClick="javascript:popUpWin('../jsp/suppliertypelistsave.jsp?SUPPLIER_TYPE_ID='+form1.SUPPLIER_TYPE_ID.value+'&TYPE=POPUP1');">
									<a href="#" data-toggle="tooltip" data-placement="top"
									title="Supplier Type Details"> <i
										class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
								</span>
							</div>
						</div>
					</div>

					<INPUT type="hidden" id="TaxByLabel" name="taxbylabel"
						value=<%=taxbylabel%>>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2"
							for="Telephone No">Supplier Phone</label>
						<div class="col-sm-4">

							<INPUT name="TELNO" type="TEXT" value="" size="50"
								class="form-control" onkeypress="return isNumber(event)"
								MAXLENGTH="30">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2" for="Fax">Supplier
							Fax</label>
						<div class="col-sm-4">

							<INPUT name="FAX" type="TEXT" value="" size="50"
								onkeypress="return isNumber(event)" MAXLENGTH="30"
								class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2"
							for="Customer Email">Supplier Email</label>
						<div class="col-sm-4">
							<INPUT name="CUSTOMEREMAIL" type="TEXT"
								value="" size="50" MAXLENGTH=50
								class="form-control">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2" for="Website">Website</label>
						<div class="col-sm-4">
							<INPUT name="WEBSITE" type="TEXT" value="" size="50"
								MAXLENGTH=50 class="form-control">
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
									<label class="control-label col-form-label col-sm-2"
										for="Unit No">Unit No.</label>
									<div class="col-sm-4">

										<INPUT name="ADDR1" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Building">Building</label>
									<div class="col-sm-4">

										<INPUT name="ADDR2" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Street">Street</label>
									<div class="col-sm-4">

										<INPUT name="ADDR3" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2" for="City">City</label>
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
									<label class="control-label col-form-label col-sm-2"
										for="Postal Code">Postal Code</label>
									<div class="col-sm-4">

										<INPUT name="ZIP" type="TEXT" value="" size="50"
											MAXLENGTH=10 class="form-control">
									</div>
								</div>

							</div>

							<div class="tab-pane fade" id="profile">
								<br>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Contact Name">Contact Name</label>
									<div class="col-sm-4">

										<INPUT name="CONTACTNAME" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="100">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Designation">Designation</label>
									<div class="col-sm-4">

										<INPUT name="DESGINATION" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Work phone">Work Phone</label>
									<div class="col-sm-4">
										<INPUT name="WORKPHONE" type="TEXT" value=""
											onkeypress="return isNumber(event)" size="50" MAXLENGTH=50
											class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Hand Phone">Mobile</label>
									<div class="col-sm-4">

										<INPUT name="HPNO" type="TEXT" value="" size="50"
											class="form-control" onkeypress="return isNumber(event)"
											MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Email">Email</label>
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
		   _MasterUtil=new  MasterUtil();
		   ccList =  _MasterUtil.getTaxTreatmentList("",plant,"");
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vTAXTREATMENT = (String)m.get("TAXTREATMENT"); %>
		        <option  value='<%=vTAXTREATMENT%>' ><%=vTAXTREATMENT %> </option>
		        <%
       			}
			 %></SELECT>
			</div>
		</div>

							<div class="form-group">
							<label class="control-label col-form-label col-sm-2" for="TRN No"
								id="TaxLabel"></label>
							<div class="col-sm-4">

								<INPUT name="RCBNO" type="TEXT" class="form-control"
									value="" size="50" MAXLENGTH="30">
							</div>
							</div>

								<div class="form-group" style="display: none;">
									<label class="control-label col-form-label col-sm-2"
										for="Opening Balance">Opening Balance</label>
									<div class="col-sm-4">
										<INPUT name="OPENINGBALANCE" type="hidden"
											value=""
											onkeypress="return isNumberKey(event,this,4)" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Payment Terms">Payment Type</label>
									<div class="col-sm-4">
										<div class="input-group">
											<INPUT class="form-control" name="PAYTERMS" type="TEXT"
												value="" size="20" MAXLENGTH=100 readonly>
											<span class="input-group-addon"
												onClick="javascript:popUpWin('../jsp/list/paymenttypelist_save.jsp?paymenttype='+form1.PAYTERMS.value+'&PAYTYPE=POPUP1');">
												<a href="#" data-toggle="tooltip" data-placement="top"
												title="Payment Type"> <i
													class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
											</span>
										</div>
									</div>

									<div class="form-inline">
										<label class="control-label col-form-label col-sm-1"
											for="Payment Terms">Days</label> <input name="PMENT_DAYS"
											type="TEXT" value="" size="5" MAXLENGTH=10
											class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Facebook">Facebook Id</label>
									<div class="col-sm-4">
										<INPUT name="FACEBOOK" type="TEXT" value=""
											size="50" MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Twitter">Twitter Id</label>
									<div class="col-sm-4">
										<INPUT name="TWITTER" type="TEXT" value=""
											size="50" MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Linkedin">LinkedIn Id</label>
									<div class="col-sm-4">
										<INPUT name="LINKEDIN" type="TEXT" value=""
											size="50" MAXLENGTH=50 class="form-control">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2"
										for="Linkedin">Skype Id</label>
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
<%-- 				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" value="<%=sBANKNAME%>" style="width: 100%"> --%>
<!-- 				<OPTION style="display:none;">Select Bank</OPTION> -->
	<INPUT name="BANKNAME" type = "TEXT" value="" id="BANKNAME" class="form-control"   placeholder="BANKNAME">
	 <span class="select-icon"  onclick="$(this).parent().find('input[name=\'BANKNAME\']').focus()">
	<i class="glyphicon glyphicon-menu-down"></i></span>
	
  	</div>
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
			 %> --%>
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

										<INPUT name="REMARKS" type="TEXT" value=""
											size="50" MAXLENGTH=100 class="form-control ">
									</div>
								</div>

							</div>
						</div>
					</div>

					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-8">
							<!-- <button type="button" class="Submit btn btn-default"
								onClick="window.location.href='../home'">Back</button>
							&nbsp;&nbsp;
							<button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp;
							<button type="button" class="Submit btn btn-default"
								onClick="onNew();" >Clear</button>
							&nbsp;&nbsp; -->
							<button type="button" class="btn btn-success" onClick="onAddSupplier();" >
								Save
							</button>
							&nbsp;&nbsp;
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

						</div>
					</div>




				</div>
				<!-- <div class="modal-footer">
	      	<button id="btnBillOpen" type="button" class="btn btn-success" onClick="onAdd();">Save</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div> -->
			</div>
		</form>
	</div>
</div>
<div id="customerModal" class="modal fade"  name="form" role="dialog">
	<div class="modal-dialog modal-lg">
		<form class="form-horizontal" name="form" id="formCustomer" method="post">
			<input type="hidden" name="plant" id="plant" value="<%=plant%>" >
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">New Customer</h4>
				</div>
				<div class="modal-body">
					<input name="CUSTOMER_TYPE_DESC" type="hidden" value="">
					<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
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
							<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
							<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">
						   <INPUT type="hidden" name="CURRENCYID_C" value="<%=DISPLAYID%>">	<!--Resvi -->
						   
							
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
					
					<!-- 	                Author Name:Resviya ,Date:9/08/21 , Description -UEN Label     -->
          <div class="form-group" id="UEN">
	      <label class="control-label col-form-label col-sm-2 required" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
	      <div class="col-sm-4">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="cus_companyregnumber" id="cus_companyregnumber" type = "TEXT" value="<%=companyregnumber%>" size="50"  MAXLENGTH=100> 
	  	  </div>
	       </div>
	       </div>
	         <!-- End -->
					
						<!-- 	                Author Name:Resviya ,Date:20/07/21 -->

                      <div class="form-group">
					  <label class="control-label col-form-label col-sm-2 required">Customer
							Currency</label>
					 <div class="col-sm-4">
					  <INPUT class="form-control" name="CUS_CURRENCY"  id="CUS_CURRENCY" type="TEXT" value="<%=currency%>"  size="50" MAXLENGTH=100>
					 </div>
					 </div>
					

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
							<li class="nav-item active"><a href="#other_cust"
								class="nav-link" data-toggle="tab" aria-expanded="true">Other
									Details</a></li>
							<li class="nav-item"><a href="#profile_cust" class="nav-link"
								data-toggle="tab">Contact Details</a></li>
							<li class="nav-item"><a href="#home_cust" class="nav-link"
								data-toggle="tab">Address</a></li>
							<li class="nav-item"><a href="#bank_cust" class="nav-link"
							 	data-toggle="tab">Bank Account Details</a></li>
							<li class="nav-item"><a href="#remark_cust" class="nav-link"
								data-toggle="tab">Remarks</a></li>
						</ul>
						<div class="tab-content clearfix">
							<div class="tab-pane fade" id="home_cust">
								<br>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2 required">Country</label>
									<div class="col-sm-4">

										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE_C" name="COUNTRY_CODE_C" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
			for(int i=0 ; i<countryList.size();i++)
      		 {
				Map m=(Map)countryList.get(i);
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
										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE_C" name="STATE" value="" style="width: 100%">
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

							<div class="tab-pane fade" id="profile_cust">
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

							<div class="tab-pane active" id="other_cust">
								<br>

								<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Tax Treatment</label>
			<div class="col-sm-4 ac-box">
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnTaxChange(this.value)" id="TAXTREATMENT" name="TAXTREATMENT" style="width: 100%">
				<OPTION style="display:none;">Select Tax Treatment</OPTION>
				<%
			for(int i=0 ; i<taxTreatmentList.size();i++)
      		 {
				Map m=(Map)taxTreatmentList.get(i);
				String vTAXTREATMENT = (String)m.get("TAXTREATMENT"); %>
		        <option  value='<%=vTAXTREATMENT%>' ><%=vTAXTREATMENT %> </option>
		        <%
       			}
			 %></SELECT>
			</div>
		</div>

		<div class="form-group">

						<label class="control-label col-form-label col-sm-2" for="TRN No."
							id="TaxLabelCust"></label>
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
										<INPUT name="CREDITLIMIT" type="TEXT" value=""  size="50"
											MAXLENGTH=50 class="form-control"
											onkeypress="return isNumberKey(event,this,4)">
									</div>

	<INPUT name="BANKNAME" type = "TEXT" value="" id="BANKNAMECUS" class="form-control"   placeholder="BANKNAME">
	 <span class="select-icon"  onclick="$(this).parent().find('input[name=\'BANKNAME\']').focus()">
	<i class="glyphicon glyphicon-menu-down"></i></span>
	
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

							<div class="tab-pane fade" id="remark_cust">
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
<%@include file="../jsp/newBankModal.jsp"%>
<script>
function popUpWin(URL) {
	 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
function bankCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#BANKNAME").typeahead('val', data.BANK_NAME);
		$("#BANKNAMECUS").typeahead('val', data.BANK_NAME);
		  $("input[name ='BRANCH']").val(data.BANK_BRANCH);
		
	}
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
			  
			    	 return false;
			    	}
			}
	}
	
	
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

$(".po-table tbody").on('click','.accrmv',function(){
	debugger;	    
    var obj = $(this);
    var timestamp = new Date().getUTCMilliseconds();
    kayid = "key"+timestamp;
    $(obj).closest('td').attr('id', kayid); 
    $("input[name ='ukey']").val(kayid);
});

$('#vendname').on('typeahead:selected', function(evt, item) {
	getSupplierDetails();
});
function getSupplierDetails(){
	$.ajax({
		type: "GET",
		url: "../MasterServlet",
		data: {
			"action":'GET_SUPPLIER_BY_CODE',
			"SUPPLIERCODE":$('#vendno').val(),
			"PLANT":'<%=plant%>'
		},
		dataType: "json",
		beforeSend: function(){
			showLoader();
		},
		success: function(data) {
			if (data.ERROR_CODE == '100'){
				$("#supplier_email").val(data.SUPPLIER_EMAIL);
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
$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip(); 
	 
    <!-- Author Name:Resviya ,Date:19/07/21 , Description: For Currency -->
var basecurrency = '<%=Supcurrency%>';   
  
	 
<!-- 	                Author Name:Resviya ,Date:9/08/21 , Description -UEN (GCC/ASIAPACIFIC)     -->

var region = '<%=region%>'; 
if(region == "GCC"){
	var x = document.getElementById("UEN");
	x.style.display = "none";
}else if(region == "ASIA PACIFIC"){
	var x = document.getElementById("UEN");
	x.style.display = "block";
}
<!-- End -->

	/* Bank Name Auto Suggestion */
	$('#BANKNAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'NAME',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/BankServlet";
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
		    	  return '<p onclick="document.form1.BRANCH.value = \''+data.BRANCH_NAME+'\'">'+data.NAME+'</p>';
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
		
/* Bank Cus Name Auto Suggestion */
$('#BANKNAMECUS').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'NAME',  
	  source: function (query, process,asyncProcess) {
		  var urlStr = "/track/BankServlet";
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


/* To get the suggestion data for Currency */
$("#SUP_CURRENCY").typeahead({
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
		return '<div onclick="document.form1.CURRENCYID_S.value = \''+data.CURRENCYID+'\'"> <p>'+data.CURRENCY+'</p></div>';

		}
	  }
}).on('typeahead:render',function(event,selection){
	  
}).on('typeahead:open',function(event,selection){
	
}).on('typeahead:close',function(){

}).on('typeahead:change',function(event,selection){
	if($(this).val() == "")
		$("input[name ='CURRENCYID_S']").val("");	
});

});

</script>

<jsp:include page="newProductModalPurchase.jsp" flush="true"></jsp:include> <!-- imti for add product -->
<jsp:include page="newProductDepTypeModal.jsp" flush="true"></jsp:include><!-- Resvi for add product department -->
<jsp:include page="newProductCategoryModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product category -->
<jsp:include page="newProductSubCatgoryModal.jsp" flush="true"></jsp:include><!-- Thanzith for add product Sub Category -->
<jsp:include page="newProductBrandModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product Brand -->
<jsp:include page="newProductHsCodeModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product HsCode -->
<jsp:include page="newProductCOOModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product COO -->
<jsp:include page="newProductUOMmodal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product UOM -->
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
