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
	String title = "Convert To Sales Order";
	String isAutoGenerate = "false";
	
	String plant = ((String)session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	ArrayList plntList = plantMstDAO.getPlantMstDetails(plant);
	Map plntMap = (Map) plntList.get(0);
	String PLNTDESC = (String) plntMap.get("PLNTDESC");
	ArrayList taxTreatmentList = (ArrayList)request.getAttribute("TaxTreatmentList");
	ArrayList salesLocations = (ArrayList)request.getAttribute("SalesLocations");
	ArrayList countryList = (ArrayList)request.getAttribute("CountryList");
	ArrayList bankList = (ArrayList)request.getAttribute("BankList");
	
	String numberOfDecimal = (String)request.getAttribute("NUMBEROFDECIMAL");
	//String olddono = (String)request.getAttribute("olddono");
	String newdono = (String)request.getAttribute("DONO");
	String jobno = (String)request.getAttribute("JOBNO");
	String deldate = (String)request.getAttribute("DelDate");
	String collectionTime = (String)request.getAttribute("CollectionTime");
	String currency = (String)request.getAttribute("Currency"),
			zeroval = StrUtils.addZeroes(0.0, numberOfDecimal);
	String zerovalper = StrUtils.addZeroes(Float.parseFloat("0"), "3");
	String gst = (String)request.getAttribute("GST");
	//String taxbylabel = (String)request.getAttribute("Taxbylabel");
	String taxbylabel = ub.getTaxByLable(plant);
	String region = (String)request.getAttribute("Region");
	String msg = (String)request.getAttribute("Msg");
	
	estHdr esthdr = (estHdr)request.getAttribute("estHdr");
	List<estDet> estdetDetails = (ArrayList<estDet>)request.getAttribute("estdetList");
	String discountType =  esthdr.getDISCOUNT_TYPE();
	List ItemList = (ArrayList)request.getAttribute("ItemList");
	List attachmentList = (List)request.getAttribute("AttachmentList");
	
	//Validate no.of Customers -- Azees 15.11.2020
		CustMstDAO custdao = new CustMstDAO();
		
		String NOOFCUSTOMER=((String) session.getAttribute("NOOFCUSTOMER"));
		String ValidNumber="",OrdValidNumber="";
		ArrayList arrCustot =custdao.getTotalCustomers(plant);
		Map mCustot=(Map)arrCustot.get(0);
		String Custot = (String)mCustot.get("TOTAL_CUSTOMERS");
		String NOOFORDER=((String) session.getAttribute("NOOFORDER")); /* Resvi */
		int novalid = Integer.valueOf(Custot);
		if(!NOOFCUSTOMER.equalsIgnoreCase("Unlimited"))
		{
			int convl = Integer.valueOf(NOOFCUSTOMER);
			if(novalid>=convl)
			{
				ValidNumber=NOOFCUSTOMER;
			}
		}
		
//	 	resvi starts 
		DoHdrDAO doHdrDAO = new DoHdrDAO();
		DateUtils _dateUtils = new DateUtils();
		String FROM_DATE = _dateUtils.getDate();
		if (FROM_DATE.length() > 5)
			FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
		
		String TO_DATE = _dateUtils.getLastDayOfMonth();
		if (TO_DATE.length() > 5)
			TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
		
		int noordvalid =doHdrDAO.Salescount(plant,FROM_DATE,TO_DATE);
		if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
		{
			int convl = Integer.valueOf(NOOFORDER);
			if(noordvalid>=convl)
			{
				OrdValidNumber=NOOFORDER;
			}
		}
//	 	resvi ends


		
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
		MasterUtil masterUtil = new MasterUtil();
		String STATEPREFIX ="";
		String COUNTRYLOC ="";
		if(esthdr.getSALES_LOCATION().length()>0){
			ArrayList sprefix =  masterDAO.getSalesLocationByPrefix(esthdr.getSALES_LOCATION(), plant, "");
			Map msprefix=(Map)sprefix.get(0);
			STATEPREFIX = (String)msprefix.get("PREFIX");
			COUNTRYLOC = (String)msprefix.get("COUNTRY");
		}
		
		String taxtypeshow = "";
		if(estdetDetails.size() > 0){
			taxtypeshow = estdetDetails.get(0).getTAX_TYPE();
		}
		
		currency = esthdr.getCURRENCYID();
		
		String empname = new EmployeeDAO().getEmpname(plant, esthdr.getEMPNO(), "");	
	
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
    <jsp:param name="submenu" value="<%=IConstants.SALES_ORDER%>"/>
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
 </style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/EstimateTOSalesOrderPro.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../salesestimate/detailPro?estno=<%=jobno%>'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" id="salesOrderForm" name="form1" method="post" 
			action="../salesorder/new" enctype="multipart/form-data" onsubmit="return validateSalesOrder()">
			<input type = "hidden" name="plant" value="<%=plant%>">
			<input name="ORDER_TYPE_MODAL" type="hidden" value="SALES">
			<input type = "hidden" name="PROJECTID" value="<%=esthdr.getPROJECTID()%>">
			<input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
			<input type="hidden" name="CURRENCYUSEQTOLD" value="<%=esthdr.getCURRENCYUSEQT()%>">
				<input type = "hidden" name="shiptaxstatus" value="<%=esthdr.getISSHIPPINGTAX()%>">
				<input type = "hidden" name="odiscounttaxstatus" value="<%=esthdr.getISDISCOUNTTAX()%>">
				<input type = "hidden" name="discounttaxstatus" value="1">
				<input type = "hidden" name="ptaxpercentage" value="<%=esthdr.getOUTBOUND_GST()%>">
				<input type = "hidden" name="ptaxtype" value="<%=fintaxtype.getTAXTYPE()%>">
				<input type = "hidden" name="ptaxpercentage" value="<%=esthdr.getOUTBOUND_GST()%>">
				<input type = "hidden" name="ptaxdisplay" value="<%=taxtypeshow%>">
				<input type = "hidden" name="ptaxiszero" value="<%=fintaxtype.getISZERO()%>">
				<input type = "hidden" name="ptaxisshow" value="<%=fintaxtype.getSHOWTAX()%>">
				<input type = "hidden" name="taxid" value="<%=esthdr.getTAXID()%>">
				<input type = "hidden" name="custModal">
				<input type = "hidden" name="EMPNO" value="<%=esthdr.getEMPNO()%>">
				<INPUT type="hidden" name="CURRENCYID"  value="<%=esthdr.getCURRENCYID()%>">	
				<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>">
				<%if(!ispuloc){ %>
				 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
				<%} %>
				<%-- <input type = "hidden" name="COPYDONO" value="<%=olddono%>"> --%>
				<%-- <div class="form-group customer-section">
					<label class="control-label col-form-label col-sm-2 required">Customer:</label>
					<div class="col-sm-6 ac-box">
						<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" 
							 name="CUST_NAME" value="" placeholder="Select a customer" >
			   		 	<span><a href="#" title="Tax Treatment" id="TAXTREATMENT" hidden></a></span>
			   		 	<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" name="TAXTREATMENT" style="width: 100%">
							<OPTION style="display:none;"></OPTION>
        					<%
        					String 	taxTreatment = poHdr.getTAXTREATMENT();
							for(int i=0 ; i<taxTreatmentList.size();i++){
								Map m1=(Map)taxTreatmentList.get(i);
								String nTAXTREATMENT = (String)m1.get("TAXTREATMENT"); %>
		        				<option  value='<%=nTAXTREATMENT%>' 
		        				<%=(taxTreatment.equalsIgnoreCase(nTAXTREATMENT) ? "selected" : "") %>>
		        				<%=nTAXTREATMENT %> </option>		          
		        			<%} %>
	        			</SELECT>
					</div>
				</div> --%>
				
				<div class="form-group customer-section">
					<label class="control-label col-form-label col-sm-2 required">Customer:</label>
					<div class="col-sm-6 ac-box">
						<div class="input-group">
							<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" onclick="removetaxtrestl()"
							placeholder="Select a customer" name="CUST_NAME"  value="<%=esthdr.getCustName()%>"> 
							<span class="select-icon" style="right: 45px;"
								onclick="$(this).parent().find('input[name=\'CUST_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span> 
							<span class="btn-danger input-group-addon"
								onClick="javascript:popUpWin('../jsp/customer_list_order.jsp?CUST_NAME='+form.CUST_NAME.value);">
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							</span>
			   		 	</div>
			   		 	<span><a href="#" title="Tax Treatment" id="TAXTREATMENT" hidden></a></span>
			   		 	<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" name="TAXTREATMENT" style="width: 100%">
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
				
				<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Shipment:</label>
					<div class="col-sm-6 ac-box">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" 
									id="SHIPPINGCUSTOMER" placeholder="Select a shipping customer" 
									name="SHIPPINGCUSTOMER" value="<%=esthdr.getSHIPPINGCUSTOMER()%>"> 
								<span class="select-icon" style="right: 45px;"
								onclick="$(this).parent().find('input[name=\'SHIPPINGCUSTOMER\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
								<span class="btn-danger input-group-addon"
								onclick="javascript:popUpWin('../jsp/shippingdetails_list.jsp?SHIPPINGCUSTOMER='+form.SHIPPINGCUSTOMER.value+'&FORMNAME=form');">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span>
						</div>
					</div>
				</div>
				
				<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-6 ac-box">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" 
									id="project" placeholder="Select a project" 
									name="project" value="<%=projectname%>"> 
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
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Payment Type:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="PAYMENTTYPE" name="PAYMENTTYPE" value="<%=esthdr.getPAYMENTTYPE()%>">
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
							<input type="text" class="form-control" id="DONO" name="DONO" value="<%=newdono%>">
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Reference No:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="JOB_NUM" name="JOB_NUM" value="<%=jobno%>" 
							placeholder="Max 20 Characters" maxlength="20">
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Order Date:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control datepicker" id="DELDATE" 
							
								name="DELDATE" value="<%=deldate%>" readonly>
						</div>
					</div>
					<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-3">Order Time:</label>
						<div class="col-sm-6">
							<div class="input-group">
								<input type="text" class="form-control" id="COLLECTION_TIME" 
									name="COLLECTION_TIME" value="<%=collectionTime%>" readonly>
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
							<input type="text" class="form-control" id="ORDERTYPE" name="ORDERTYPE" >
							<span class="select-icon" 
								onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
							</span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Sales VAT :</label><!-- this is dynamic value -->
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="GST" onchange="changetaxforgst()" name="GST" value="<%=gst%>">
							<span class="input-group-addon" style="font-size: 15px; color: #0059b3">
								<b>%</b>
							</span>
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Employee ID:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="EMP_NAME" name="EMP_NAME" value="<%=empname%>">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">INCOTERM:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="INCOTERMS" name="INCOTERMS" value="<%=esthdr.getINCOTERMS()%>">
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
			        	<input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" name="SALES_LOC" value="<%=esthdr.getSALES_LOCATION()%>" placeholder="Select a Purchase Location">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	        			
        			</div>
				</div>
				<%}%>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Currency</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control" id="CURRENCY" name="CURRENCY" value="<%=esthdr.getCURRENCYID()%>" >
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Equivalent Currency</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Equivalent Currency" value="<%=esthdr.getCURRENCYUSEQT()%>">
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Salestax" name="Salestax"  value="<%=taxtypeshow%>" placeholder="Select a Tax">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'Purchasetax\']').focus()">
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
					<div class="col-12 col-sm-12 no-padding">
  		        		<input type="Checkbox" class="form-check-input" style="border:0;" name="select" value="select" onclick="return checkAll(this.checked);">
                     	<strong>&nbsp;Select/Unselect All </strong>
                    </div>
					<table class="table table-bordered line-item-table so-table">
						<thead>
							<tr>
								<th>Chk</th>
								<th colspan=2 style="width:20%">Product</th>
								<th class="bill-desc"  style="width:10%">Account</th>
								<th  style="width:8%">UOM</th>
								<th style="width:4%">Sales Estimate Quantity</th>
								<th style="width:4%">Converted Quantity</th>
								<th style="width:7%">Quantity</th>								
								<th style="width:10%">Delivery Date</th>
								<!-- <th>Remarks</th> -->
								<!-- <th>Status</th> -->
								<th colspan=2  style="width:12%">Unit Price</th>
								<!-- <th></th> -->
								<th style="width:5%">Discount</th>
								<th style="width:10%">Tax</th>
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
							String qty="",qtyis="",qtyest ="",unitPrice="", percentage="", tax="";
							
							qtyest = estdet.getQTYOR().toString();
							double dQtyest ="".equals(qtyest) ? 0.0d :  Double.parseDouble(qtyest);
							qtyest = StrUtils.addZeroes(dQtyest, "3");
				  			
				  			qtyis = estdet.getQTYIS().toString();
							double dQtyIs ="".equals(qtyis) ? 0.0d :  Double.parseDouble(qtyis);
							qtyis = StrUtils.addZeroes(dQtyIs, "3");				  			
				  			
							double dQty = dQtyest-dQtyIs;
				  			qty = StrUtils.addZeroes(dQty, "3");
				  			
				  			//double dPrice = podet.getUNITCOST() * podet.getCURRENCYUSEQT();
				  			
				  			double dPrice = Double.valueOf((String)m.get("price")) * estdet.getCURRENCYUSEQT();
				  			unitPrice = StrUtils.addZeroes(dPrice, numberOfDecimal);
				  			String catalogpath = (String)m.get("catalogpath");
				  			catalogpath = ((catalogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catalogpath);
				  			if(dQty>0) {
						%>
							<tr>
								<td>
									<input type="Checkbox" style="border:0;background=#dddddd" 
									name="chkdDONO" value="<%=j%>">
								</td>
								<td class="item-img text-center">
									<img alt="" src="<%=catalogpath%>" style="width: 100%;"> 
									<input type="hidden" name="lnno" value="<%=estdet.getESTLNNO()%>">
									<input type="hidden" name="itemdesc" value="<%=m.get("sItemDesc")%>">
									<input type="hidden" name="unitpricerd" value="<%=calAmount%>">
									<input type="hidden" name="minstkqty" value="<%=m.get("stkqty")%>">
									<input type="hidden" name="maxstkqty" value="<%=m.get("maxstkqty")%>">
									<input type="hidden" name="stockonhand" value="<%=m.get("stockonhand")%>">
									<input type="hidden" name="outgoingqty" value="<%=m.get("outgoingqty")%>">
									<input type="hidden" name="customerdiscount" value="<%=m.get("outgoingOBDiscount")%>">
									<input type="hidden" name="unitpricediscount" value="<%=calAmount%>">
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
								<td>
									<input type="text" name="item" class="form-control itemSearch" style="width:84%" 
									placeholder="Type or click to select an item." 
									value="<%=estdet.getITEM()%>">
									<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
								</td>
								<td>
									<input type="text" name="account_name" class="form-control accountSearch" 
									placeholder="Account" value="<%=estdet.getACCOUNT_NAME()%>">
								</td>
								<td>
									<input type="text" name="UOM" class="form-control uomSearch" 
									placeholder="Select UOM" value="<%=estdet.getUNITMO()%>">
								</td>
								<td>
								<input type="text" name="ESTQTY" class="form-control text-right" readonly
									value="<%=qtyest%>">
								</td>
								<td>
								<input type="text" name="QTYIS" class="form-control text-right" readonly
									value="<%=qtyis%>">
								</td>
								<td>
									<input type="text" name="QTY" class="form-control text-right" 
									 data-rl="<%=m.get("stkqty")%>" data-msq="<%=m.get("maxstkqty")%>" data-soh="<%=m.get("stockonhand")%>"
									  data-eq="<%=m.get("EstQty")%>" data-aq="<%=m.get("AvlbQty")%>" data-oq="<%=qty%>" 
									  data-pq="" 
									  data-iq="" value="<%=qty%>" onchange="calculateAmount(this)">
								</td>
								<td>
									<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" 
									 value="<%=estdet.getPRODUCTDELIVERYDATE()%>" READONLY>
								</td>
								<td>
									<input type="text" name="unitprice" class="form-control text-right" 
									value="<%=unitPrice%>" onchange="calculateAmount(this)">
								</td>
								<td class="table-icon">
									<a href="#" onclick="getPreviousPurchaseOrderDetails(this)">
										<i class="fa fa-info-circle" style="font-size: 15px;"></i>
									</a>
								</td>
								<td>
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
									value="<%=itemdisc%>" onchange="calculateAmount(this)">
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
								<td>
									<input type="text" name="tax" class="form-control taxSearch" 
									value="<%=estdet.getTAX_TYPE()%>"
										placeholder="Select a Tax" readonly>
									<input type="hidden" name="tax_type" value="<%=estdet.getTAX_TYPE()%>">
								</td>
								<%
									double amount = dQty * dPrice;
									if(estdet.getDISCOUNT_TYPE().equals("%")){
										amount = amount - ((amount/100)*estdet.getDISCOUNT());
									}else{
										amount = amount - (estdet.getDISCOUNT()*estdet.getCURRENCYUSEQT());
									}
								%>
								<td class="text-right grey-bg">
									<input name="amount" type="text" class="form-control text-right"
									value="<%=StrUtils.addZeroes(amount, numberOfDecimal)%>" readonly="readonly">
								</td>
								<td class="table-icon" style="position:relative;">
									<a href="#" onclick="showRemarksDetails(this)">
										<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>
									</a>
								</td>
							</tr>
						<%	j++;
						}}%>
							
						</tbody>
					</table>
				</div>
				
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
						<div class="total-row">
							<div class="total-label">
								<div class="row">
									<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Order Discount</div>
										<%if(esthdr.getISDISCOUNTTAX() == 1){ %>
										<span id="odtax">(Tax Inclusive)</span>
										<%}else{%>
										<span id="odtax">(Tax Exclusive)</span>
										<%} %>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isodisctax" <%= (esthdr.getISDISCOUNTTAX() == 1 ? "checked" : "")%>  name="isodisctax" Onclick="isodisctaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right oddiscount" type="text" value="<%=esthdr.getORDERDISCOUNT() %>" 
												name="orderdiscount" onchange="calculateTotal()">
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
						<div class="total-row">
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
<input type="checkbox" class="form-check-input isdtax" <%= (esthdr.getISDISCOUNTTAX() == 1 ? "checked" : "")%> name="isdtax" Onclick="isdtaxing(this)">
</div>
<div class=" col-lg-6 col-sm-6 col-6">
<div class="input-group my-group">
<input class="form-control text-right" type="text" value="<%=esthdr.getDISCOUNT()%>"
name="discount" onchange="calculateTotal()">
<% %>
<select class="discountPicker form-control"
name="discount_type" onchange="calculateTotal()">
<option value="<%=currency%>"
<%= (discountType.equalsIgnoreCase(currency) ? "selected" : "")%> ><%=currency%></option>
<option value="%"
<%= (discountType.equalsIgnoreCase("%") ? "selected" : "")%> >%</option>
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
										<%}else{%>
										<span id="shtax">(Tax Exclusive)</span>
										<%} %>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isshiptax" <%= (esthdr.getISSHIPPINGTAX() == 1 ? "checked" : "")%> name="isshiptax" Onclick="isshiptaxing(this)">
									</div>
									<div class="col-lg-6 col-sm-6 col-6">
										<input class="form-control text-right ember-view" type="text"
											name="shippingcost" onchange="calculateTotal()" value="<%=esthdr.getSHIPPINGCOST()%>">
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
											name="adjustment" onchange="calculateTotal()">
									</div>
								</div>
							</div>
							<div class="total-amount " style="line-height: 2" id="adjustment">
								0.00</div>
						</div>

						<div class="total-section total-row">
							<div class="gross-total">
								<div class="total-label">Total</div>
								<div class="total-amount" id="total">0.00</div>
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
						<div id="billAttchNote">
							<small class="text-muted"> You can upload a maximum of 5
								files, 2MB each </small>
						</div>
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
						<div class="dropup">
						<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
					    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Save as Open
					    <span class="caret"></span></button>
					    <ul class="dropdown-menu">
					      <li><a id="btnSalesOpen" href="#">Save as Open</a></li>
					      <li><a id="btnSalesDraft" href="#">Save as Draft</a></li>
					      <li><a id="btnSalesOpenEmail" href="#">Save as Open and Send Email</a></li>
					    </ul>
						<button type="button" class="btn btn-default" onclick="window.location.href='../salesorder/summary'">Cancel</button>
					  </div>
					</div>
				</div>
				<input id="sub_total" name="sub_total" value="" hidden> 
				<input id="total_amount" name="total_amount" value="" hidden>
				<input type ="hidden" name="ISAUTOGENERATE" value="false">
				<input id="taxamount" name="taxamount" value="" hidden>
				
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
				
				<input type = "hidden" name="SHIPPINGID" value="">
				<input type = "hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>">
				<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
				<input type = "hidden" name="currency" value="<%=currency%>">
				
				<input type = "hidden" name="orderstatus" value="">
				
				
				<input type="hidden" name="STATE_PREFIX" value=""/>
			</form>
		</div>
		<!-- Modal -->
		<!-- Modal -->
		
	</div>
</div>

<!-- ----------------Modal-------------------------- -->
<%@include file="../jsp/newEmployeeModal.jsp"%>
<%@include file="../jsp/newPaymentTypeModal.jsp"%>
<%@include file="../jsp/newOrderTypeModal.jsp"%>
<%@include file="../jsp/newIncotermsModal.jsp"%>
<%@include file="../jsp/newGstDetailModal.jsp"%>
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
          		<h4 class="modal-title">Previous Sales Price Detail</h4>
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

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Opening
										Balance</label>
									<div class="col-sm-4">
										<INPUT name="OPENINGBALANCE" type="TEXT" value=""
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
										<INPUT name="CREDITLIMIT" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control"
											onkeypress="return isNumberKey(event,this,4)">
									</div>
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
	Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER);
	String template_subject = (String)emailMsg.get("SUBJECT");
	String template_body = (String)emailMsg.get("BODY1");
	CustUtil custUtil = new CustUtil();
	ArrayList custDetails = custUtil.getCustomerDetails(esthdr.getCustCode(), plant);
	String custEmail = StrUtils.fString((String)custDetails.get(14));
%>
<input type="hidden" id="plant_desc" value="<%=PLNTDESC %>" />
<input type="hidden" id="template_subject" value="<%=template_subject %>" />
<input type="hidden" id="template_body" value="<%=template_body %>" />
<input type="hidden" id="customer_email" value = '<%=custEmail %>' />
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
});
</script>
<%@include file="../jsp/newBankModal.jsp"%>
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

