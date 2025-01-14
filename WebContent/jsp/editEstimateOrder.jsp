<%@page import="com.track.db.object.estDet"%>
<%@page import="com.track.db.object.estHdr"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@page import="com.track.db.object.FinProject"%>
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
	String title = "Edit Estimate Order";
	String isAutoGenerate = "false";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String compindustry = plantMstDAO.getcompindustry(plant);
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	
	boolean displayCustomerpop=false,displayPaymentTypepop=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displayCustomerpop = ub.isCheckValAcc("popcustomer", plant,USERID);
		displayPaymentTypepop = ub.isCheckValAcc("paymenttypepopup", plant,USERID);
		displayCustomerpop =true;
		displayPaymentTypepop=true;	
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displayCustomerpop = ub.isCheckValinv("popcustomer", plant,USERID);	
		displayPaymentTypepop = ub.isCheckValinv("paymenttypepopup", plant,USERID);
	}
	
	ArrayList taxTreatmentList = (ArrayList)request.getAttribute("TaxTreatmentList");
	ArrayList salesLocations = (ArrayList)request.getAttribute("SalesLocations");
	ArrayList countryList = (ArrayList)request.getAttribute("CountryList");
	ArrayList bankList = (ArrayList)request.getAttribute("BankList");
	
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	String numberOfDecimal = (String)request.getAttribute("NUMBEROFDECIMAL");
	String deldate = (String)request.getAttribute("ORDDATE");
	String collectionTime = (String)request.getAttribute("CollectionTime");
	String currency = (String)request.getAttribute("Currency"),
			zeroval = StrUtils.addZeroes(0.0, numberOfDecimal);
	String zerovalper = StrUtils.addZeroes(Float.parseFloat("0"), "3");
	String gst = (String)request.getAttribute("GST");
	String taxbylabel = (String)request.getAttribute("Taxbylabel");
	String region = (String)request.getAttribute("Region");
	String msg = (String)request.getAttribute("Msg");
	
	estHdr esthdr = (estHdr)request.getAttribute("estHdr");
	List<estDet> estdetDetails = (ArrayList<estDet>)request.getAttribute("estDetList");
	String discountType =  esthdr.getDISCOUNT_TYPE();
	CustUtil custUtil = new CustUtil();
	List ItemList = (ArrayList)request.getAttribute("ItemList");
	List attachmentList = (List)request.getAttribute("AttachmentList");
	
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
		
		ArrayList arrCust =  custUtil.getCustomerDetails(esthdr.getCustCode(), plant);
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
		FinCountryTaxType fintaxtype = new FinCountryTaxType();
		if(esthdr.getTAXID() > 0){
			FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
			fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(esthdr.getTAXID());
		}
		
		FinProjectDAO finProjectDAO = new FinProjectDAO();
		FinProject finProject=new FinProject();
		String projectname = "";
		if(esthdr.getPROJECTID() > 0){
			finProject = finProjectDAO.getFinProjectById(plant, esthdr.getPROJECTID());
			projectname = finProject.getPROJECT_NAME();
		}
		MasterDAO masterdao = new MasterDAO();
		String STATEPREFIX ="";
		String COUNTRYLOC ="";
		if(esthdr.getSALES_LOCATION().length()>0){
			ArrayList sprefix =  masterdao.getSalesLocationByPrefix(esthdr.getSALES_LOCATION(), plant, COUNTRYCODE);
			Map msprefix=(Map)sprefix.get(0);
			STATEPREFIX = (String)msprefix.get("PREFIX");
			COUNTRYLOC = (String)msprefix.get("COUNTRY");
		}
		
		String taxtypeshow = "";
		if(estdetDetails.size() > 0){
			taxtypeshow = estdetDetails.get(0).getTAX_TYPE();
		}
		currency = esthdr.getCURRENCYID();
		String companyregnumber=StrUtils.fString(request.getParameter("cus_companyregnumber"));
		String Cuscurency=plantMstDAO.getBaseCurrency(plant);
		String CURRENCYUSEQT = "0", DISPLAY = "", DISPLAYID = "";;
		List curQryList = new ArrayList();
		CurrencyDAO currencyDAO = new CurrencyDAO();
		curQryList = currencyDAO.getCurrencyDetails(currency, plant);
		for (int i = 0; i < curQryList.size(); i++) {
			ArrayList arrCurrLine = (ArrayList) curQryList.get(i);
			DISPLAYID = StrUtils.fString(StrUtils.removeQuotes((String) arrCurrLine.get(0)));
			DISPLAY = StrUtils.fString(StrUtils.removeQuotes((String) arrCurrLine.get(2)));
			CURRENCYUSEQT = StrUtils.fString(StrUtils.removeQuotes((String) arrCurrLine.get(3)));
		}
		
		ArrayList plntList1 = plantMstDAO.getPlantMstDetails(plant);
		Map plntMap1 = (Map) plntList1.get(0);
		String isEmployee = (String) plntMap1.get("ISEMPLOYEEVALIDATESO");
		
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ESTIMATE_ORDER%>"/>
    <jsp:param name="submenu" value="<%=IConstants.ESTIMATE_ORDER%>"/>
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
a:focus {
  outline: 1px solid blue;
}
 </style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/editEstimateOrder.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../salesestimate/summary"><span class="underline-on-hover">Sales Estimate Summary</span> </a></li>
                <li><a href="../salesestimate/detail?estno=<%=esthdr.getESTNO()%>"><span class="underline-on-hover">Sales Estimate Detail</span> </a></li>
                <li><label>Edit Estimate Order</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../salesestimate/detail?estno=<%=esthdr.getESTNO()%>'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" id="salesOrderForm" name="form1" autocomplete="off" method="post" 
			action="../salesestimate/new" enctype="multipart/form-data" onsubmit="return validateSalesOrder()">
			
			<input type = "hidden" name="plant" value="<%=plant%>">
			<input name="ORDER_TYPE_MODAL" type="hidden" value="SALES ESTIMATE">
			<input type = "hidden" name="PROJECTID" value="<%=esthdr.getPROJECTID()%>">
			<input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
			<input type="hidden" name="CURRENCYUSEQTOLD" value="<%=esthdr.getCURRENCYUSEQT()%>">
				<input type = "hidden" name="shiptaxstatus" value="<%=esthdr.getISSHIPPINGTAX()%>">
				<input type = "hidden" name="odiscounttaxstatus" value="<%=esthdr.getISORDERDISCOUNTTAX()%>">
				<input type = "hidden" name="discounttaxstatus" value="<%=esthdr.getISDISCOUNTTAX()%>">
				<input type = "hidden" name="ptaxtype" value="<%=fintaxtype.getTAXTYPE()%>">
				<input type = "hidden" name="ptaxpercentage" value="<%=esthdr.getOUTBOUND_GST()%>">
				<input type = "hidden" name="ptaxdisplay" value="<%=taxtypeshow%>">
				<input type = "hidden" name="ptaxiszero" value="<%=fintaxtype.getISZERO()%>">
				<input type = "hidden" name="ptaxisshow" value="<%=fintaxtype.getSHOWTAX()%>">
				<input type = "hidden" name="taxid" value="<%=esthdr.getTAXID()%>">
				<input type = "hidden" name="EMPNO">
				<INPUT type="hidden" name="CURRENCYID"  value="<%=DISPLAYID%>">	
				<INPUT type="hidden" name="curency"  value="<%=DISPLAYID%>">
				
			    <INPUT type="hidden" name="BASECURRENCYID"  value="<%=Cuscurency%>"> <%--Resvi--%>   
			    <INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
			     <input name="COMP_INDUSTRY" type="hidden" value="<%=COMP_INDUSTRY%>">  <!-- imti added condition based on product popup -->
			     
					
				<INPUT type="hidden" name="ISPRO" id="ISPRO" value="0">
				<%if(!ispuloc){ %>
				 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
				<%} %>
			
				<div class="form-group customer-section">
					<label class="control-label col-form-label col-sm-2 required">Customer:</label>
					<div class="col-sm-6 ac-box">
						<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" 
							 name="CUST_NAME" onchange="checkcustomer(this.value)" value="<%=esthdr.getCustName()%>" readonly>
			   		 	<span><a href="#" title="Tax Treatment" id="TAXTREATMENT" hidden></a></span>
			   		 	<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" name="nTAXTREATMENT" id="nTAXTREATMENT" value=""  style="width: 100%">
							<OPTION style="display:none;"></OPTION>
        					<%
        					String 	taxTreatment = esthdr.getTAXTREATMENT();
							for(int i=0 ; i<taxTreatmentList.size();i++){
								Map m1=(Map)taxTreatmentList.get(i);
								String nTAXTREATMENT = (String)m1.get("TAXTREATMENT"); %>
		        				<option  value='<%=nTAXTREATMENT%>' 
		        				<%=(taxTreatment.equalsIgnoreCase(nTAXTREATMENT) ? "selected" : "") %>>
		        				<%=nTAXTREATMENT %> </option>		          
		        			<%} %>
	        			</SELECT>
					</div>
				</div>
				
				<%-- <div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Shipping Address:</label>
					<div class="col-sm-6 ac-box">
<!-- 						<div class="input-group"> -->
								<input type="text" class="ac-selected  form-control typeahead" 
									id="SHIPPINGCUSTOMER" placeholder="Select a shipping address" 
									name="SHIPPINGCUSTOMER" value="<%=esthdr.getSHIPPINGCUSTOMER()%>"> 
								<span class="select-icon" 	onclick="$(this).parent().find('input[name=\'SHIPPINGCUSTOMER\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
							<!-- 	<span class="btn-danger input-group-addon"
								onclick="javascript:popUpWin('../jsp/shippingdetails_list.jsp?SHIPPINGCUSTOMER='+form.SHIPPINGCUSTOMER.value+'&FORMNAME=form');">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
<!-- 						</div> -->
					</div>
				</div> --%>
				<div class="form-group shipCustomer-section" id="shipbilladd">
					<div class="col-sm-2"></div>
					<div class="col-sm-5" style="line-height: 7px; font-size: 13px;">
						<h5 style="font-weight: bold;">Billing Address</h5>
						<p><%=esthdr.getCustName() %></p>
						<p><%=esthdr.getAddress() %> <%=esthdr.getAddress2() %></p>
						<p><%=esthdr.getAddress3() %>  <%=sAddr4%></p>
						<p><%=sState%></p>
						<p><%=sCountry%> <%=sZip%></p>
						<p><%=sHpNo%></p>
						<p><%=sWorkphone%></p>
						<p><%=sEmail%></p>
						
						
					</div>
					<div class="col-sm-5" style="line-height: 7px; font-size: 13px;">
						<h5 style="font-weight: bold;">
							Shipping Address <a><span data-toggle="modal"
								data-target="#shipaddr" onclick="getshipaddr();" style="font-size: 15px;font-weight: 450;">Change</span></a>
						</h5>
						<div id="cshipaddr">
							
						<p><%=esthdr.getSHIPCONTACTNAME() %></p>
						<p><%=esthdr.getSHIPDESGINATION()%></p>
						<p><%=esthdr.getSHIPADDR1() %> <%=esthdr.getSHIPADDR2() %></p>
						<p><%=esthdr.getSHIPADDR3() %> <%=esthdr.getSHIPADDR4() %></p>
						<p><%=esthdr.getSHIPSTATE()%></p>
						<p><%=esthdr.getSHIPCOUNTRY() %> <%=esthdr.getSHIPZIP() %></p>
						<p><%=esthdr.getSHIPHPNO() %></p>
						<p><%=esthdr.getSHIPWORKPHONE() %></p>
						<p><%=esthdr.getSHIPEMAIL()%></p>
						</div>
					</div>
				</div>
				
				<%if(compindustry.equalsIgnoreCase("Construction") || compindustry.equalsIgnoreCase("Service")){ %>
				<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-6 ac-box">
<!-- 						<div class="input-group"> -->
								<input type="text" class="ac-selected  form-control typeahead" 
									id="project" placeholder="Select a project" 
									name="project" onchange="checkproject(this.value)" value="<%=projectname%>"> 
								<span class="select-icon" 	onclick="$(this).parent().find('input[name=\'project\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
							<!-- 	<span class="btn-danger input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
<!-- 						</div> -->
					</div>
				</div>
				<%}else{%>
				<input type="hidden" name="project" value="">
				<%} %>
				
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Payment Mode:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="PAYMENTTYPE" name="PAYMENTTYPE" onchange="checkpaymenttype(this.value)" value="<%=esthdr.getPAYMENTTYPE()%>">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'PAYMENTTYPE\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Order Number:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="ESTNO" name="ESTNO" value="<%=esthdr.getESTNO()%>" readonly>
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Reference No:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="JOB_NUM" name="JOB_NUM" value="<%=esthdr.getJobNum() %>" 
							placeholder="Max 20 Characters" maxlength="20">
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Order Date:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control datepicker" id="DELDATE" 
							
								name="DELDATE" value="<%=esthdr.getORDDATE()%>" readonly>
						</div>
					</div>
					<div class="col-sm-6 no-padding" style="display:none">
						<label class="control-label col-form-label col-sm-5">Order Time:</label>
						<div class="col-sm-6">
							<div class="input-group">
								<input type="text" class="form-control" id="COLLECTION_TIME" 
									name="COLLECTION_TIME" value="<%=esthdr.getCollectionTime()%>" readonly>
				   		 	</div>
						</div>
					</div>					
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Delivery Period/Date:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="DELIVERYDATE" name="DELIVERYDATE" 
							placeholder="Max 20 Characters" maxlength="20" value="<%=esthdr.getDELIVERYDATE()%>">
						</div>
					</div>
					<div class="form-inline">
				       	<label class="control-label col-sm-1">
					       	<input type="checkbox" id="DATEFORMAT" name="DATEFORMAT" 
					       	onclick="headerReadable();" 
					       	<%=((esthdr.getDELIVERYDATEFORMAT() == 1) ? "checked" : "")%>><font size="2.9"><b>&nbsp;By Date</b></font>
				       	</label> 	
			    	</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Order Type:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="ORDERTYPE" name="ORDERTYPE" onchange="checkordertype(this.value)" value="<%=esthdr.getORDERTYPE()%>">
							<span class="select-icon" 
								onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
							</span>
					</div>
				</div>
				
				<div class="form-group">
					<%if(isEmployee.equalsIgnoreCase("1")){ %>		
					<label class="control-label col-form-label col-sm-2 required">Employee:</label>
					<%}else{ %>
					<label class="control-label col-form-label col-sm-2">Employee:</label>
					<%} %>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="EMP_NAME" name="EMP_NAME" onchange="checkemployeess(this.value)" value="<%=esthdr.getEMPNO()%>">
						<input type="hidden" name="ISEMPLOYEEVALIDATESO" value="<%=isEmployee%>">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				
				<div class="form-group">
        			<label class="control-label col-form-label col-sm-2" for="Expiry Date">Expiry Date:</label>
       					 <div class="col-sm-4">
       					<div class="input-group">          
       				<INPUT class="form-control datepicker" name="EXPIREDATE" type="TEXT" readonly="readonly" size="30" MAXLENGTH=50 value="<%=esthdr.getEXPIREDATE()%>">
      			</div>
    		</div>
    	</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">INCOTERM:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="INCOTERMS" name="INCOTERMS" onchange="checkincoterms(this.value)" value="<%=esthdr.getINCOTERMS()%>">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'INCOTERMS\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				
				<%-- <div class="form-group">
					<label class="control-label col-form-label col-sm-2">Sales Location:</label>
					<div class="col-sm-4">
						<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="SALES_LOC" name="SALES_LOC" onchange='OnChange(form1.SALES_LOC);' style="width: 100%">
						<%
							for(int i=0 ; i<salesLocations.size();i++){
								Map m=(Map)salesLocations.get(i);
								String STATE = (String)m.get("STATE");
						        String STATE_PREFIX = (String)m.get("PREFIX"); %>
						        <option  value= <%=STATE_PREFIX%> 
						        <%=doHdr.getSALES_LOCATION().equalsIgnoreCase(STATE_PREFIX) ? "selected" : "" %>><%=STATE %> </option>				          
	        			<%} %>
	        			</SELECT>
					</div>
				</div> --%>
				
				<%if(ispuloc){ %>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2" for="Purchase Location">Sales Location:</label>
        			<div class="col-sm-4">
			        	<input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" name="SALES_LOC" value="<%=esthdr.getSALES_LOCATION()%>" placeholder="Select a Sales Location">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	        			
        			</div>
				</div>
				<%}%>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Currency</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control" id="CURRENCY" name="CURRENCY" onchange="checkcurrency(this.value)" value="<%=DISPLAY%>" readonly>
					</div>
				<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-5 required" id="exchangerate" ></label>
						<div class="col-sm-6 ac-box">
							<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onkeypress="return isNumberKey(event,this,4)" required>	
						</div>
					    </div>
					    </div>				
				  <!-- End -->
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Standard <%=taxbylabelordermanagement%> :</label><!-- this is dynamic value -->
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="GST" name="GST" onchange="changetaxforgst()" onkeypress="return isNumberKey(event,this,4)"  value="<%=esthdr.getOUTBOUND_GST()%>">
							<span class="input-group-addon" style="font-size: 15px; color: #0059b3">
								<b>%</b>
							</span>
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Salestax" name="Salestax" onchange="checktax(this.value)" value="<%=taxtypeshow%>" placeholder="Select a Tax">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'Salestax\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				
				<hr />
				<div class="form-group">
					<div class="col-sm-12">
						<label class="control-label col-form-label">Product Rates Are</label>
						<div class="dropdown-noborder">
							<select class="ac-box dropdown-noborder form-control" onchange="calculateTotal()" name="item_rates" id="item_rates">
								<option value="0" <%=(esthdr.getITEM_RATES() == 0 ? "selected" : "") %>>
									Tax Exclusive
								</option>
								<option value="1" <%=(esthdr.getITEM_RATES() == 1 ? "selected" : "") %>>
									Tax Inclusive
								</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row" style="margin: 0px;">
					<table class="table table-bordered line-item-table so-table">
						<thead>
							<tr>
								<th colspan=2 style="width:20%">Product</th>
								<th class="bill-desc"  style="width:10%">Account</th>
								<th  style="width:8%">UOM</th>
								<th style="width:8%">Quantity</th>
								<th style="width:10%">Delivery Date</th>
								<!-- <th>Remarks</th> -->
								<!-- <th>Status</th> -->
								<th colspan=2  style="width:10%">Unit Price</th>
								<!-- <th></th> -->
								<th style="width:5%">Discount</th>
								<!-- <th style="width:10%">Tax</th> -->
								<th colspan=2 style="width:20%">Amount</th>
								<!-- <th></th> -->
								</tr>
						</thead>
						<tbody>
						<% 
						int j = 0;
						for(estDet estdet: estdetDetails){
							
							Map m = (Map)ItemList.get(j);
							
							double discount = 0.0, price = 0.0;
							String calAmount = "";
							
							/* if(m.get("OBDiscountType").equals("BYPERCENTAGE")){
								
								discount = (Double.parseDouble((String)m.get("ConvertedUnitCost")) * 
										Double.parseDouble((String)m.get("outgoingOBDiscount"))/100);
								price = (Double.parseDouble((String)m.get("ConvertedUnitCost"))
										-(Double.parseDouble((String)m.get("ConvertedUnitCost"))*
										Double.parseDouble((String)m.get("outgoingOBDiscount"))/100));
							}
							else
							{
								
								price = Double.parseDouble((String)m.get("outgoingOBDiscount"));
							}
							calAmount = StrUtils.addZeroes(price, numberOfDecimal); */
							String qty="", unitPrice="", percentage="", tax="";
							
							qty = estdet.getQTYOR().toString();
							double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
				  			qty = StrUtils.addZeroes(dQty, "3");
				  			
				  			double dPrice = estdet.getUNITPRICE() * estdet.getCURRENCYUSEQT();
				  			unitPrice = StrUtils.addZeroes(dPrice, numberOfDecimal);
				  		
				  			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
				  			String PUOMQTY ="1",SUOMQTY ="1";
                            ArrayList getuomqty = new MovHisDAO().selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+plant+"_UOM where UOM='"+ estdet.getUNITMO()+"'",htTrand1);
                			if(getuomqty.size()>0)
                			{
                			Map mapval = (Map) getuomqty.get(0);
                			PUOMQTY=(String)mapval.get("UOMQTY");
                			}
                			Double pcsprice = dPrice / Double.valueOf(PUOMQTY);
                			
                			
                			String salesuom = new ItemMstDAO().getItemSalesUOM(plant,estdet.getITEM());
                            getuomqty = new MovHisDAO().selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+plant+"_UOM where UOM='"+ salesuom +"'",htTrand1);
                			if(getuomqty.size()>0)
                			{
                			Map mapval = (Map) getuomqty.get(0);
                			SUOMQTY=(String)mapval.get("UOMQTY");
                			}
                			pcsprice = pcsprice * Double.valueOf(SUOMQTY);
				  			
				  			
				  			String catalogpath = (String)m.get("catalogpath");
				  			catalogpath = ((catalogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catalogpath);
						%>
							<tr>
								<td class="item-img text-center">
									<img alt="" src="<%=catalogpath%>" style="width: 100%;"> 
									<input type="hidden" name="lnno" value="<%=estdet.getESTLNNO()%>">
									<input type="hidden" name="itemdesc" value="<%=m.get("sItemDesc")%>">
									<input type="hidden" name="unitpricerd" value="<%=dPrice%>">
								
									<input type="hidden" name="minstkqty" value="<%=m.get("stkqty")%>">
									<input type="hidden" name="maxstkqty" value="<%=m.get("maxstkqty")%>">
									<input type="hidden" name="stockonhand" value="<%=m.get("stockonhand")%>">
									<input type="hidden" name="outgoingqty" value="<%=m.get("outgoingqty")%>">
									<input type="hidden" name="customerdiscount" value="<%=m.get("outgoingOBDiscount")%>">
									<input type="hidden" name="unitpricediscount" value="<%=calAmount%>">
									<input type="hidden" name="itemprice" value="<%=m.get("price")%>">
									<input type="hidden" name="tax_type" class="taxSearch" value="<%=estdet.getTAX_TYPE()%>">
									<input type="hidden" name="tax" class="taxSearch" value="<%=estdet.getTAX_TYPE()%>">
									<%
									if(m.get("OBDiscountType").equals("BYPERCENTAGE")){
									%>
									<input type="hidden" name="discounttype" value="<%=m.get("BYPERCENTAGE")%>">
									<input type="hidden" name="customerdiscount" value="<%=m.get("outgoingOBDiscount")%>%">
									<%
									}else{
									%>
										<input type="hidden" name="discounttype" value="">
										<input type="hidden" name="customerdiscount" value="<%=m.get("outgoingOBDiscount")%>">
									<%} %>
									<input type="hidden" name="minsp" value="<%=m.get("minsprice")%>">
								</td>
								<td class="bill-item">
									<input type="text" name="item" onchange="checkitems(this.value,this)" class="form-control itemSearch" 
									placeholder="Type or click to select an item." 
									value="<%=estdet.getITEM()%>">
								<input class="form-control"  name="ITEMDES" style="height: 23px;background-color: #fff;" readonly  data-pd="<%=m.get("sItemDesc")%>" value="<%=m.get("sItemDesc")%>">	
								</td>
								<td class="bill-item">
									<input type="text" name="account_name" class="form-control accountSearch" 
									placeholder="Account" value="<%=estdet.getACCOUNT_NAME()%>">
								</td>
								<td class="bill-item">
									<input type="text" name="UOM" onchange="checkprduom(this.value,this)" class="form-control uomSearch" 
									placeholder="Select UOM" value="<%=estdet.getUNITMO()%>">
								</td>
								<td class="item-qty">
									<input type="text" name="QTY" class="form-control text-right" 
									 data-rl="<%=m.get("stkqty")%>" data-msq="<%=m.get("maxstkqty")%>" data-soh="<%=m.get("stockonhand")%>"
									  data-eq="<%=m.get("EstQty")%>" data-aq="<%=m.get("AvlbQty")%>" data-oq="<%=estdet.getQTYOR().doubleValue()%>" 
							<%-- 		  data-pq="<%=estdet.getQtyPick().doubleValue()%>"  --%>
									  data-iq="<%=estdet.getQTYIS().doubleValue()%>" value="<%=StrUtils.addZeroes(estdet.getQTYOR().doubleValue(), "3")%>" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
								</td>
								<td>
									<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" 
									 value="<%=estdet.getPRODUCTDELIVERYDATE()%>" READONLY tabindex="-1">
								</td>
								<td class="item-cost">
									<input type="text" name="unitprice" class="form-control text-right" 
									value="<%=unitPrice%>" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
								</td>
								<td class="table-icon">
									<a href="#" onclick="getPreviousPurchaseOrderDetails(this)">
										<i class="fa fa-info-circle" style="font-size: 15px;"></i>
									</a>
								</td>
								<td class="item-discount">
									<div class="row">					
									<div class=" col-lg-12 col-sm-3 col-12">
									<div class="input-group my-group" style="width:120px;">
									<%
									String itemdisc = "";
									if(estdet.getDISCOUNT_TYPE().equals("%")){
										itemdisc = StrUtils.addZeroes(estdet.getDISCOUNT(), "3");
									}else{
										itemdisc = StrUtils.addZeroes((estdet.getDISCOUNT()*estdet.getCURRENCYUSEQT()), numberOfDecimal);
									}
									
									%>
									<input name="item_discount" type="text" class="form-control text-right" 
									value="<%=itemdisc%>" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
									<select name="item_discounttype" class="discountPicker form-control"
										onchange="calculateAmount(this)">
										<option value="<%=currency%>" 
										<%=(estdet.getDISCOUNT_TYPE().equals(currency)) ? "selected" : "" %> ><%=currency%></option>
										<option value="%" 
										<%=(estdet.getDISCOUNT_TYPE().equals("%")) ? "selected" : "" %>>%</option>
									</select>
									</div>
									</div>
									</div>
								</td>
								<%-- <td class="item-tax">
									<input type="text" name="tax" class="form-control taxSearch" 
									value="<%=estdet.getTAX_TYPE()%>" placeholder="Select a Tax" readonly>
									<input type="hidden" name="tax_type" value="<%=estdet.getTAX_TYPE()%>">
								</td> --%>
								<%
									double amount = estdet.getQTYOR().doubleValue() * dPrice;
									if(estdet.getDISCOUNT_TYPE().equals("%")){
										amount = amount - ((amount/100)*estdet.getDISCOUNT());
									}else{
										amount = amount - (estdet.getDISCOUNT()*estdet.getCURRENCYUSEQT());
									}
								%>
								<td class="item-amount text-right grey-bg">
									<input name="amount" type="text" class="form-control text-right"
									value="<%=StrUtils.addZeroes(amount, numberOfDecimal)%>" readonly="readonly" tabindex="-1">
								</td>
								<td class="table-icon" style="position:relative;">
							<%-- 		<%if(estdet.getLNSTAT().equals("O") || estdet.getLNSTAT().equals("C")) {%>
										<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" 
										onclick = "alert('Product ID is beign Processed.')"></span>
									<%}else{%>  --%>
									 <%-- <%}%>   --%>
									<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span> 
									
									<a href="#" onclick="showRemarksDetails(this)">
										<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>
									</a>
								</td>
							</tr>
						<%	j++;
						}%>
							
						</tbody>
					</table>
				</div>
				
				<div class="row">
					<div class="col-sm-6">
						<a href="#" onclick="addRow(event);">
                        <i class="add-line" title="Add another line" style="font-size: 15px;">+ Add another line</i>
                        </a>
					</div>
					<div class="total-section col-sm-6">
						<div class="total-row sub-total">
							<div class="total-label">
								Sub Total <br> <span class="productRate" hidden>(Tax
									Inclusive)</span>
							</div>
							<div class="total-amount" id="subTotal">0.00</div>
						</div>
						<div class="total-row">
							<div class="total-label">
								<div class="row">
									<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Order Discount</div>
										<%if(esthdr.getISORDERDISCOUNTTAX() == 1){ %>
										<span id="odtax">(Tax Inclusive)</span>
										<%}else{%>
										<span id="odtax">(Tax Exclusive)</span>
										<%} %>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isodisctax" <%= (esthdr.getISORDERDISCOUNTTAX() == 1 ? "checked" : "")%>  name="isodisctax" Onclick="isodisctaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right oddiscount" type="text" value="<%=esthdr.getORDERDISCOUNT() %>" 
												name="orderdiscount" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
											<select class="discountPicker form-control" name="oddiscount_type" id="oddiscount_type" onchange="calculateTotal()">
												<option <%=(esthdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%") ? "selected" : "") %> value="%">%</option>
												<option <%=(esthdr.getORDERDISCOUNTTYPE().equalsIgnoreCase(currency) ? "selected" : "") %> value="<%=currency%>"><%=currency%></option>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="total-amount" style="line-height: 2;">
								<span id="orderdiscount">  </span>
							</div>
						</div>
						<div class="total-row" style="display:none"><!--  Author: Azees  Create date: July 6,2021  Description: Hide Discount -->
							<div class="total-label">
								<div class="row">
									<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Discount</div>
										<%if(esthdr.getISDISCOUNTTAX() == 1){ %>
										<span id="dtax">(Tax Inclusive)</span>
										<%}else{%>
										<span id="dtax">(Tax Exclusive)</span>
										<%} %>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isdtax" <%= (esthdr.getISDISCOUNTTAX() == 1 ? "checked" : "")%>  name="isdtax" Onclick="isdtaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right" type="text" value="<%=esthdr.getDISCOUNT()%>"
												name="discount" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
												<% %>
											<select class="discountPicker form-control"	name="discount_type" onchange="calculateTotal()">
												<option <%=(discountType.equalsIgnoreCase("%") ? "selected" : "") %> value="%">%</option>
												<option <%=(discountType.equalsIgnoreCase(currency) ? "selected" : "") %> value="<%=currency%>"><%=currency%></option>
												<%-- <option value="<%=currency%>" 
												<%= (discountType.equalsIgnoreCase(currency) ? "selected" : "")%> ><%=currency%></option>
												<option value="%" 
												<%= (discountType.equalsIgnoreCase(currency) ? "%" : "")%> >%</option> --%>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="total-amount" style="line-height: 2;">
								<span id="discount"> 0.00 </span>
							</div>
						</div>
						<div class="total-row">
							<div class="badge-editable total-label">
								<div class="row">
									<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Shipping Charge</div>
										<%if(esthdr.getISSHIPPINGTAX() == 1){ %>
										<span id="shtax">(Tax Inclusive)</span>
										<%}else{ %>
										<span id="shtax">(Tax Exclusive)</span>
										<%} %>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isshiptax" <%= (esthdr.getISSHIPPINGTAX() == 1 ? "checked" : "")%> name="isshiptax" Onclick="isshiptaxing(this)">
									</div>
									<div class="col-lg-6 col-sm-6 col-6">
										<input class="form-control text-right ember-view" type="text"
											name="shippingcost"  onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)" value="<%=esthdr.getSHIPPINGCOST()%>">
									</div>
								</div>
							</div>
							<div class="total-amount deshipping" id="shipping" name="shipping"><%=zeroval %></div>
						</div>
						<div class="taxDetails"></div>
						<div class="total-row">
							<div class="badge-editable total-label">
								<div class="row">
									<div class="col-lg-5 col-sm-5 col-5">
										<div class="form-control-plaintext">
											Adjustment <br>
										</div>
									</div>
									<div class="col-lg-6 col-sm-6 col-6">
										<input class="form-control text-right ember-view" type="text" value="<%=esthdr.getADJUSTMENT() %>"
											name="adjustment" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
									</div>
								</div>
							</div>
							<div class="total-amount " style="line-height: 2" id="adjustment">
								0.00</div>
						</div>

						<div class="total-section total-row">
							<div class="gross-total-removemargin"><!--  Author: Azees  Create date: July 19,2021  Description: Total of Local Currency-->
								<div class="total-label">Total (<%=currency%>)</div>
								<div class="total-amount" id="total">0.00</div>
							</div>
						</div>
						
				<div class="total-section total-row" id="showtotalcur">
					<div class="gross-total">
						<div class="total-label"> Total (<%=Cuscurency%>) </div> 
						<div class="total-amount" id="totalcur">0.00 </div>
					</div>
				</div>
					</div>
				</div>
				<div class="row grey-bg">
					<div class="col-sm-4">
						<div class="form-inline">
							<label for="email">Attach Files(s)</label>
							<div class="attch-section">
								<input type="file" class="form-control input-attch"
									id="supplierAttch" name="file" multiple="true">
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
						<%if(attachmentList.size()>0){ %>
					<div id="billAttchNote">
						<small class="text-muted"><div class="attachclass"><a><%=attachmentList.size()%> files Attached</a>
								<div class="tooltiptext">
									
									<%for(int i =0; i<attachmentList.size(); i++) {   
								  		Map attach=(Map)attachmentList.get(i); %>
											<div class="row" style="padding-left:10px;padding-top:10px">
												<span class="text-danger col-sm-3">
													<%if(attach.get("FileType").toString().equalsIgnoreCase("application/pdf")) {%>
													<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M314.2 318.9c-6.4-3.7-13-7.7-18.2-12.5-13.9-13-25.5-31.1-32.7-50.8.5-1.9.9-3.5 1.3-5.2 0 0 7.8-44.5 5.8-59.6-.3-2.1-.5-2.6-1-4.3l-.7-1.8c-2.1-4.9-6.3-10.6-12.9-10.4l-3.8-.6h-.1c-7.3 0-13.3 4.2-14.8 9.9-4.8 17.5.2 43.9 9 77.9l-2.2 5.7c-6.3 15.5-14.4 31.2-21.4 44.9l-.9 1.9c-7.4 14.5-14.2 26.8-20.2 37.2l-6.2 3.3c-.5.2-11.2 6-13.8 7.4-21.4 12.8-35.6 27.3-38 38.9-.8 3.7-.2 8.4 3.6 10.5l6.1 3c2.6 1.4 5.4 2 8.3 2 15.2 0 33-19 57.4-61.5 28.2-9.2 60.3-16.8 88.4-21 21.4 12 47.8 20.4 64.5 20.4 2.9 0 5.5-.3 7.6-.9 3.2-.8 5.9-2.6 7.5-5.1 3.2-4.9 3.9-11.5 3-18.5-.3-2.1-1.9-4.6-3.6-6.2-4.9-4.9-15.9-7.4-32.5-7.6-11.6 0-25.2 1-39.5 3zM158 405c2.8-7.6 13.8-22.7 30.1-36 1.1-.8 3.5-3.2 5.9-5.4-17.1 27.1-28.5 38-36 41.4zm96.5-222.2c4.9 0 7.7 12.4 7.9 23.9.2 11.6-2.4 19.7-5.9 25.8-2.8-8.9-4.1-22.9-4.1-32.1 0 0-.2-17.6 2.1-17.6zm-28.8 158.3c3.4-6.2 6.9-12.6 10.6-19.4 8.9-16.7 14.5-29.9 18.7-40.6 8.3 15 18.6 27.8 30.8 38.2 1.5 1.3 3.1 2.5 4.8 3.8-24.9 4.8-46.2 10.8-64.9 18zm148.1-9.1c8.8 2.2 8.9 6.7 7.4 7.7s-5.8 1.5-8.6 1.5c-8.9 0-20-4.1-35.4-10.7 6-.5 11.4-.7 16.3-.7 8.9 0 11.5 0 20.3 2.2z"></path><path d="M441.6 116.6L329 4.7c-3-3-7.1-4.7-11.3-4.7H94.1C76.5 0 62.4 14.2 62.4 31.7v448.5c0 17.5 14.2 31.7 31.7 31.7h320.6c17.3 0 31.3-14 31.4-31.3l.3-352.7c-.1-4.1-1.8-8.2-4.8-11.3zm-14.9 358c0 9.4-7.8 17.1-17.3 17.1H99.2c-9.5 0-17.3-7.7-17.3-17.1V36.3c0-9.4 7.8-17.1 17.3-17.1h172.4c9.5 0 17.3 7.7 17.3 17.1v83.5c0 18.7 14.7 33.8 34.1 33.8h86.5c9.5 0 17.3 7.7 17.3 17.1l-.1 303.9zM326.8 136c-10.8 0-19.6-8.8-19.6-19.6V24.6c0-4.4 5.3-6.5 8.3-3.4l106.6 106.5c3.1 3.1.9 8.3-3.4 8.3h-91.9z"></path></svg>
													<%}else if(attach.get("FileType").toString().equalsIgnoreCase("image/jpeg") || attach.get("FileType").toString().equalsIgnoreCase("image/png") || attach.get("FileType").toString().equalsIgnoreCase("image/gif") || attach.get("FileType").toString().equalsIgnoreCase("image/tiff")){ %>
													<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M417.2 378.8H95.3c-7 0-12.8-5.7-12.8-12.8v-34.9c0-2.7.8-5.2 2.3-7.4l44.6-63c4-5.6 11.6-7 17.4-3.3l60.8 39.7c4.9 3.2 11.1 2.7 15.5-1.1l116.8-103.2c5.5-4.9 14.1-4.1 18.5 1.8l66.3 86c1.7 2.2 2.7 5 2.7 7.8v80.2c0 5.6-4.6 10.2-10.2 10.2z" fill="#40bab5"></path><path d="M212.2 157.7c23.2 0 42 19 42 42.4s-18.8 42.4-42 42.4-42-19-42-42.4c.1-23.4 18.9-42.4 42-42.4z" fill="#fbbe01"></path><path d="M462 60.8c16.5 0 30 13.5 30 30V422c0 16.5-13.5 30-30 30H50.4c-16.5 0-30-13.5-30-30V90.8c0-16.5 13.5-30 30-30H462m0-20H50.4c-27.6 0-50 22.4-50 50V422c0 27.6 22.4 50 50 50H462c27.6 0 50-22.4 50-50V90.8c0-27.6-22.4-50-50-50z" fill="#888"></path></svg>
													<%} else{%>
													<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M270.825,70.55L212.17,3.66C210.13,1.334,207.187,0,204.093,0H55.941C49.076,0,43.51,5.566,43.51,12.431V304.57  c0,6.866,5.566,12.431,12.431,12.431h205.118c6.866,0,12.432-5.566,12.432-12.432V77.633  C273.491,75.027,272.544,72.51,270.825,70.55z M55.941,305.073V12.432H199.94v63.601c0,3.431,2.78,6.216,6.216,6.216h54.903  l0.006,222.824H55.941z"></path></svg>	
													<%} %>
												</span>
												<div class="col-sm-9" style="padding-left:16px"><span class="fileNameFont"><a><%=attach.get("FileName").toString() %></a></span><br><span class="fileTypeFont">File Size: <%=Integer.parseInt(attach.get("FileSize").toString())/1024 %>KB</span></div>
											</div>
											<div class="row bottomline">
													<span class="col-sm-6" Style="font-size:14px;"><i class="fa fa-download" aria-hidden="true" onclick="downloadFile(<%=attach.get("ID") %>,'<%=(String) attach.get("FileName") %>')"> Download</i></span>
													<span class="col-sm-6" Style="font-size:14px;float:right"><i class="fa fa-trash" aria-hidden="true" onclick="removeFile(<%=attach.get("ID") %>)"> Remove</i></span>
											</div>	
									<%} %>
									
								</div>
							</div>
							
						</small>
					</div>
					<%}else{ %>
						<div id="billAttchNote">
							<small class="text-muted"> You can upload a maximum of 5
								files, 2MB each </small>
						</div>
					<%} %>
					</div>
					<div class="col-sm-6 notes-sec">
						<p>Remark1</p>
						<div>
							<textarea rows="2" name="REMARK1" class="ember-text-area form-control ember-view" 
								placeholder="Max 100 characters"  maxlength="100"><%=esthdr.getRemark1()%></textarea>
						</div>

						<p>Remark2</p>
						<div>
							<textarea rows="2" name="REMARK3" class="ember-text-area form-control ember-view"
								placeholder="Max 100 characters"  maxlength="100"><%=esthdr.getRemark3()%></textarea>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 txn-buttons">
						<%if(esthdr.getORDER_STATUS().equalsIgnoreCase("Draft")){%>
							<div class="dropup">
						<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
					    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Save as Open
					    <span class="caret"></span></button>
					    <ul class="dropdown-menu">
					      <li><a id="btnSalesOpen" href="#">Save as Open</a></li>
					      <li><a id="btnSalesOpenEmail" href="#">Save as Open and Send Email</a></li>
					    </ul>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					  </div>
						<%}else{%>
							<div class="dropup">
						<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
					    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Save
					    <span class="caret"></span></button>
					    <ul class="dropdown-menu">
					      <li><a id="btnSalesOpen" href="#">Save</a></li>
					      <li><a id="btnSalesOpenEmail" href="#">Save and Send Email</a></li>
					    </ul>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					  </div>
						<%}%>
					</div>
					<input type="hidden" value="<%=displayCustomerpop%>" name="displayCustomerpop" id="displayCustomerpop" />
					<input type="hidden" value="<%=displayPaymentTypepop%>" name="displayPaymentTypepop" id="displayPaymentTypepop" />
				</div>
				<input id="sub_total" name="sub_total" value="" type="hidden"> 
				<input id="total_amount" name="total_amount" value="" type="hidden">
				<input type ="hidden" name="ISAUTOGENERATE" value="false">
				<input id="taxamount" name="taxamount" value="" type="hidden">
				
				<input type = "hidden" name="PERSON_INCHARGE" value="">
				<input type = "hidden" name="CUSTOMERTYPEDESC" value="">				
				<input type = "hidden" name="CUST_CODE" value="<%=esthdr.getCustCode()%>">
				<input type = "hidden" name="CUST_CODE1" value="<%=esthdr.getCustCode()%>">
				<input type = "hidden" name="TELNO" value="">
				<input type = "hidden" name="EMAIL" value="">
				<input type = "hidden" name="ADD1" value="">
				<input type = "hidden" name="ADD2" value="">
				<input type = "hidden" name="ADD3" value="">
				<input type = "hidden" name="REMARK2" value="">
				<input type = "hidden" name="ADD4" value="">
				<input type = "hidden" name="COUNTRY" value="">
				<input type = "hidden" name="ZIP" value="">
				<input type = "hidden" name="CUSTOMERSTATUSDESC" value="">
				<!-- <input type = "hidden" name="TAXTREATMENT" value=""> -->
				
				<input type = "hidden" name="SHIPPINGID" value="<%=esthdr.getSHIPPINGID()%>">
				<input type = "hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>">
				<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
				<input type = "hidden" name="currency" value="<%=currency%>">
				
				<input type = "hidden" name="orderstatus" value="">
				
				<input type = "hidden" name="custModal">
				<input type="hidden" name="STATE_PREFIX" value="<%=STATEPREFIX%>" />
				<input type = "hidden" name="SHIPCONTACTNAME" value="<%=esthdr.getSHIPCONTACTNAME()%>">
				<input type = "hidden" name="SHIPDESGINATION" value="<%=esthdr.getSHIPDESGINATION()%>">
				<input type = "hidden" name="SHIPADDR1" value="<%=esthdr.getSHIPADDR1()%>">
				<input type = "hidden" name="SHIPADDR2" value="<%=esthdr.getSHIPADDR2()%>">
				<input type = "hidden" name="SHIPADDR3" value="<%=esthdr.getSHIPADDR3()%>">
				<input type = "hidden" name="SHIPADDR4" value="<%=esthdr.getSHIPADDR4()%>">
				<input type = "hidden" name="SHIPSTATE" value="<%=esthdr.getSHIPSTATE()%>">
				<input type = "hidden" name="SHIPZIP" value="<%=esthdr.getSHIPZIP()%>">
				<input type = "hidden" name="SHIPWORKPHONE" value="<%=esthdr.getSHIPWORKPHONE()%>">
				<input type = "hidden" name="SHIPCOUNTRY" value="<%=esthdr.getSHIPCOUNTRY()%>">
				<input type = "hidden" name="SHIPHPNO" value="<%=esthdr.getSHIPHPNO()%>">
				<input type = "hidden" name="SHIPEMAIL" value="<%=esthdr.getSHIPEMAIL()%>">
			</form>
		</div>
		<!-- Modal -->
		<!-- Modal -->
		
	</div>
</div>

<!-- ----------------Modal-------------------------- -->
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

<!-- ----------------Modal-------------------------- -->
<jsp:include page="newEmployeeModal.jsp" flush="true"></jsp:include>
<jsp:include page="newPaymentTypeModal.jsp" flush="true"></jsp:include>
<jsp:include page="newOrderTypeModal.jsp" flush="true"></jsp:include>
<jsp:include page="NewChartOfAccountpopup.jsp" flush="true"></jsp:include>
<jsp:include page="newIncotermsModal.jsp" flush="true"></jsp:include>
<jsp:include page="newGstDetailModal.jsp" flush="true"></jsp:include>

 <!-- imti for add account --> 

<input type="text" id="PageName" style="display: none;" value="Estimate">
<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
<input	type="hidden" name="pronumberOfDecimal" id="pronumberOfDecimal" value="<%=numberOfDecimal%>">
<%@include file="../jsp/newProductModal.jsp" %>

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
								ArrayList ccList1 = _MasterUtil1.getCountryList("", plant, region);
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
  		
  		<div class="modal-content">
  			<div class="modal-header">
          		<h4 class="modal-title">Previous Estimate Price Detail</h4>
        	</div>
        	<div class="modal-body">
	          <table class="table lastSalesPriceDetails">
	          	<thead>
					<tr>
						<th>Order No</th>
						<th>Customer</th>
						<th>Date</th>
						<th>Price</th>
					</tr>
				</thead>
				<tbody>
				 
				</tbody>
	          </table>
	        </div>
	        <div class="modal-footer">
	          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
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
							<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
							<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">
				            <INPUT type="hidden" name="CURRENCYID_C" value="<%=DISPLAYID%>">
				            <INPUT type="hidden" name="CUST_SHIP_COUNTRY" value="">	
							
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
	         
					
					<!-- 	                Author Name:Resviya ,Date:15/07/21 -->

                      <div class="form-group">
					  <label class="control-label col-form-label col-sm-2 required">Customer
							Currency</label>
					 <div class="col-sm-4">
					  <INPUT class="form-control" name="CUS_CURRENCY"  id="CUS_CURRENCY" type="TEXT" value="<%=Cuscurency%>"  size="50" MAXLENGTH=100>
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
			 
			  <div class="col-sm-4">
       <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="SHIPCUST_OnCountry(this.value)" id="CUST_SHIP_COUNTRY_CODE" name="CUST_SHIP_COUNTRY_CODE" value="" style="width: 100%">
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
			 <%-- <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control"> --%>
      </div>
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
										<INPUT name="CREDITLIMIT" type="TEXT" value="" size="50"
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
			for(int i=0 ; i<bankList.size();i++)
      		 {
				Map m=(Map)bankList.get(i);
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
<%
	EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
	Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.ESTIMATE_ORDER);
	String template_subject = (String)emailMsg.get("SUBJECT");
	String template_body = (String)emailMsg.get("BODY1");

	ArrayList custDetails = custUtil.getCustomerDetails(esthdr.getCustCode(), plant);
	String custEmail = StrUtils.fString((String)custDetails.get(14));
%>
<input type="hidden" id="plant_desc" value="<%=PLNTDESC %>" />
<input type="hidden" id="template_subject" value="<%=template_subject %>" />
<input type="hidden" id="template_body" value="<%=template_body %>" />
<input type="hidden" id="customer_email" value = '<%=custEmail %>' />
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>

<script>
$('#CUSTOMER').on('typeahead:selected', function(evt, item) {
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
				$("#customer_email").val(data.CUSTOMER_EMAIL);
			}else{
				alert('Unable to get customer details. Please try again later.');
			}
		},
		error: function(data) {
			alert('Unable to get customer details. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
});

function bankCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#BANKNAMECUS").typeahead('val', data.BANK_NAME);
		  $("input[name ='BRANCH']").val(data.BANK_BRANCH);
		
	}
}

function SHIPCUST_OnCountry(Country)
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
			$('#CUST_SHIP_STATE').empty();
		//	$('#CUST_SHIP_STATE').append('<OPTION style="display:none;">Select State</OPTION>');
			$('#CUST_SHIP_STATE').append('<OPTION>Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					 if(document.form.STATE.value==value.text)
						 	$('#CUST_SHIP_STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
					 else
					   $('#CUST_SHIP_STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
		}
	});	
	
}

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
$('select[name="COUNTRY_CODE"]').on('change', function(){		
    var text = $("#COUNTRY_CODE option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
});
$('select[name="COUNTRY_CODE_C"]').on('change', function(){		
    var text = $("#COUNTRY_CODE_C option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
});

$('select[name="SHIP_COUNTRY_CODE"]').on('change', function(){
    var text = $("#SHIP_COUNTRY_CODE option:selected").text();
    $("input[name ='SHIP_COUNTRY']").val(text.trim());
});


function bankCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#BANKNAMECUS").typeahead('val', data.BANK_NAME);
		  $("input[name ='BRANCH']").val(data.BANK_BRANCH);
		
	}
}


/*//Resvi add Call Back For ProductDept
function productdepartmentCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=PRD_DEPT_DESC]").typeahead ('val',data.PRD_DEPT_DESC);
	}
}*/

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

$(".so-table tbody").on('click','.accrmv',function(){
	debugger;	    
    var obj = $(this);
    var timestamp = new Date().getUTCMilliseconds();
    kayid = "key"+timestamp;
    $(obj).closest('td').attr('id', kayid); 
    $("input[name ='ukey']").val(kayid);
});

$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip(); 
	 
    <!-- Author Name:Resviya ,Date:13/07/21 , Description: For Currency Label-->

    var  curr = document.form1.CURRENCYID.value;
var basecurrency = '<%=Cuscurency%>';   
document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";

if(basecurrency!=curr)//  Author: Resvi  Create date: July 28,2021  Description:  Total of Local Currency
	document.getElementById('showtotalcur').style.display = 'block';	
else
	document.getElementById('showtotalcur').style.display = 'none';   


	
       <!-- End -->



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
</script>

<%@include file="../jsp/newBankModal.jsp"%>
<jsp:include page="newProductDepTypeModal.jsp" flush="true"></jsp:include> <!-- Resvi for add product department -->
<jsp:include page="newProductCategoryModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product category -->
<jsp:include page="newProductSubCatgoryModal.jsp" flush="true"></jsp:include><!-- Thanzith for add product Sub Category -->
<jsp:include page="newProductBrandModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product Brand -->
<jsp:include page="newProductHsCodeModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product HsCode -->
<jsp:include page="newProductCOOModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product COO -->
<jsp:include page="newProductUOMmodal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product UOM -->
<%@include file="../jsp/newPaymentModeModal.jsp"%><!-- thansith Add for paymentmode -->
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

