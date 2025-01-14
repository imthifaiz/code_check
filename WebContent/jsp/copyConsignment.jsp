<%@page import="com.track.db.object.ToDet"%>
<%@page import="com.track.db.object.ToHdr"%>
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
	String title = "Copy Consignment Order";
	String isAutoGenerate = "false";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String compindustry = plantMstDAO.getcompindustry(plant);
	CurrencyDAO currencyDAO = new CurrencyDAO();

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
	
	String numberOfDecimal = (String)request.getAttribute("NUMBEROFDECIMAL");
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant); 
	String oldtono = (String)request.getAttribute("oldtono");
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
	String companyregnumber=StrUtils.fString(request.getParameter("cus_companyregnumber"));
	ToHdr toHdr = (ToHdr)request.getAttribute("ToHdr");
	List<ToDet> toDetDetails = (ArrayList<ToDet>)request.getAttribute("ToDetList");
	String discountType =  toHdr.getDISCOUNT_TYPE();
	CustUtil custUtil = new CustUtil();
	List ItemList = (ArrayList)request.getAttribute("ItemList");
	List attachmentList = (List)request.getAttribute("AttachmentList");
	
	//Validate no.of Customers -- Azees 15.11.2020
			CustMstDAO custdao = new CustMstDAO();
			String NOOFCUSTOMER=((String) session.getAttribute("NOOFCUSTOMER"));
			String NOOFORDER=((String) session.getAttribute("NOOFORDER"));
			String ValidNumber="",OrdValidNumber="";
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
			
			ArrayList arrCust =  custUtil.getCustomerDetails(toHdr.getCustCode(), plant);
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
			// NAVAS START
			ToHdrDAO toHdrDAO = new ToHdrDAO();
			DateUtils _dateUtils = new DateUtils();
			String FROM_DATE = _dateUtils.getDate();
			if (FROM_DATE.length() > 5)
			FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";

			String TO_DATE = _dateUtils.getLastDayOfMonth();
			if (TO_DATE.length() > 5)
			TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);

			int noordvalid =toHdrDAO.Salescount(plant,FROM_DATE,TO_DATE);
			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
			{
			int convl = Integer.valueOf(NOOFORDER);
			if(noordvalid>=convl)
			{
			OrdValidNumber=NOOFORDER;
			}
			}
			// RESVI ENDS
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
			if(toHdr.getTAXID() > 0){
				FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
				fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(toHdr.getTAXID());
			}
			
			FinProjectDAO finProjectDAO = new FinProjectDAO();
			FinProject finProject=new FinProject();
			String projectname = "";
			if(toHdr.getPROJECTID() > 0){
				finProject = finProjectDAO.getFinProjectById(plant, toHdr.getPROJECTID());
				projectname = finProject.getPROJECT_NAME();
			}
			MasterDAO masterdao = new MasterDAO();
			String STATEPREFIX ="";
			String COUNTRYLOC ="";
			if(toHdr.getSALES_LOCATION().length()>0){
				ArrayList sprefix =  masterdao.getSalesLocationByPrefix(toHdr.getSALES_LOCATION(), plant, COUNTRYCODE);
				Map msprefix=(Map)sprefix.get(0);
				STATEPREFIX = (String)msprefix.get("PREFIX");
				COUNTRYLOC = (String)msprefix.get("COUNTRY");
			}
			
			String taxtypeshow = "";
			if(toDetDetails.size() > 0){
				taxtypeshow = toDetDetails.get(0).getTAX_TYPE();
			}
			
			currency = toHdr.getCURRENCYID();
			String curency=plantMstDAO.getBaseCurrency(plant);
			 String DISPLAY="",DISPLAYID="";
			 List curQryList=new ArrayList();
			 curQryList = currencyDAO.getCurrencyDetails(currency,plant);
			 for(int i =0; i<curQryList.size(); i++) {
					ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
					DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
					DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
			 }

			String empname = new EmployeeDAO().getEmpname(plant, toHdr.getEMPNO(), "");
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.CONSIGNMENT%>"/>
    <jsp:param name="submenu" value="<%=IConstants.CONSIGNMENT%>"/>
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
    right: -25% !important;
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
<script src="../jsp/js/CopyConsignment.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                       
                <li><a href="../consignment/summary"><span class="underline-on-hover">Consignment Summary</span> </a></li>  
                <li><a href="../consignment/detail?tono=<%=toHdr.getJobNum()%>"><span class="underline-on-hover">Consignment Order Detail</span> </a></li>                     
                <li><label>Copy Consignment Order</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../consignment/detail?tono=<%=toHdr.getJobNum()%>'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" id="salesOrderForm" name="form1" autocomplete="off" method="post" 
			action="../consignment/copy" enctype="multipart/form-data" onsubmit="return validateConsignmentOrder()">
				<input type = "hidden" id="plant" name="plant" value="<%=plant%>">
				<input name="ORDER_TYPE_MODAL" type="hidden" value="CONSIGNMENT">
				<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>">
			<input type = "hidden" name="PROJECTID" value="<%=toHdr.getPROJECTID()%>">
			<input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
			<input type="hidden" name="currency" value="<%=currency%>">
			<input type="hidden" name="CURRENCYUSEQTOLD" value="<%=toHdr.getCURRENCYUSEQT()%>">
				<input type = "hidden" name="shiptaxstatus" value="<%=toHdr.getISSHIPPINGTAX()%>">
				<input type = "hidden" name="odiscounttaxstatus" value="<%=toHdr.getISORDERDISCOUNTTAX()%>">
				<input type = "hidden" name="discounttaxstatus" value="<%=toHdr.getISDISCOUNTTAX()%>">
				<input type = "hidden" name="ptaxtype" value="<%=fintaxtype.getTAXTYPE()%>">
				<input type = "hidden" name="ptaxpercentage" value="<%=toHdr.getCONSIGNMENT_GST()%>">
				<input type = "hidden" name="ptaxdisplay" value="<%=taxtypeshow%>">
				<input type = "hidden" name="ptaxiszero" value="<%=fintaxtype.getISZERO()%>">
				<input type = "hidden" name="ptaxisshow" value="<%=fintaxtype.getSHOWTAX()%>">
				<input type = "hidden" name="taxid" value="<%=toHdr.getTAXID()%>">
				<input type = "hidden" name="custModal">
				<INPUT type="hidden" name="CURRENCYID"  value="<%=toHdr.getCURRENCYID()%>">
				<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>
				 <input name="COMP_INDUSTRY" type="hidden" value="<%=COMP_INDUSTRY%>">  <!-- imti added condition based on product popup -->
				<input type = "hidden" name="EMPNO" value="<%=toHdr.getEMPNO()%>">	
				<input type="hidden" name="action_button" id="action_button" value="" />
				<%if(!ispuloc){ %>
				 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
				<%} %>
				<input type = "hidden" name="COPYTONO" value="<%=oldtono%>">
				<div class="form-group customer-section">
					<label class="control-label col-form-label col-sm-2 required">Customer:</label>
					<div class="col-sm-6 ac-box">
						<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" 
							 name="CUST_NAME" value="<%=toHdr.getCustName()%>" readonly>
			   		 	<span><a href="#" title="Tax Treatment" id="TAXTREATMENT" hidden></a></span>
			   		 	<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" name="nTAXTREATMENT" id="nTAXTREATMENT" value=""  style="width: 100%">
							<OPTION style="display:none;"></OPTION>
        					<%
        					String 	taxTreatment = toHdr.getTAXTREATMENT();
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
				
				<div class="form-group shipCustomer-section" id="shipbilladd">
					<div class="col-sm-2"></div>
					<div class="col-sm-5" style="line-height: 7px; font-size: 13px;">
						<h5 style="font-weight: bold;">Billing Address</h5>
						<p><%=toHdr.getCustName() %></p>
						<p><%=toHdr.getAddress() %> <%=toHdr.getAddress2() %></p>
						<p><%=toHdr.getAddress3() %>  <%=sAddr4%></p>
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
							
						<p><%=toHdr.getSHIPCONTACTNAME() %></p>
						<p><%=toHdr.getSHIPDESGINATION()%></p>
						<p><%=toHdr.getSHIPADDR1() %> <%=toHdr.getSHIPADDR2() %></p>
						<p><%=toHdr.getSHIPADDR3() %> <%=toHdr.getSHIPADDR4() %></p>
						<p><%=toHdr.getSHIPSTATE() %></p>
						<p><%=toHdr.getSHIPCOUNTRY() %> <%=toHdr.getSHIPZIP() %></p>
						<p><%=toHdr.getSHIPHPNO() %></p>
						<p><%=toHdr.getSHIPWORKPHONE() %></p>
						<p><%=toHdr.getSHIPEMAIL()%></p>
						</div>
					</div>
				</div>
				
				<%if(compindustry.equalsIgnoreCase("Construction") || compindustry.equalsIgnoreCase("Service")){ %>
				<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4 ac-box">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" 
									id="project" placeholder="Select a project" 
									name="project" onchange="checkproject(this.value)" value="<%=projectname%>"> 
								<span class="select-icon" 	onclick="$(this).parent().find('input[name=\'project\']').focus()">
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
					<label class="control-label col-form-label col-sm-2">Payment Mode:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="PAYMENTTYPE" name="PAYMENTTYPE" onchange="checkpaymenttype(this.value)" value="<%=toHdr.getPAYMENTTYPE()%>">
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
							<input type="text" class="form-control" id="TONO" name="TONO" value="<%=toHdr.getTONO()%>" readonly>
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Reference No:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="JOB_NUM" name="JOB_NUM" value="<%=toHdr.getJobNum() %>" 
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
								<%-- name="DELDATE" value="<%=deldate%>" value="<%=toHdr.getDELDATE()%>" readonly> --%>
						</div>
					</div>
					<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-5">Order Time:</label>
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
							placeholder="Max 20 Characters" maxlength="20" value="<%=toHdr.getDELIVERYDATE()%>">
						</div>
					</div>
					<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-5">
					       	<input type="checkbox" id="DATEFORMAT" name="DATEFORMAT" 
					       	onclick="headerReadable();" 
					       	<%=((toHdr.getDELIVERYDATEFORMAT() == 1) ? "checked" : "")%>><font size="2.9"><b>&nbsp;By Date</b></font>
				       	</label> 	
			    	</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">From Location:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="FROMWAREHOUSE" name="FROMWAREHOUSE" onchange="checkfromloc(this.value)" value="<%=toHdr.getFROMWAREHOUSE()%>">
							 <span class="select-icon" onclick="$(this).parent().find('input[name=\'FROMWAREHOUSE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">To Location:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="TOWAREHOUSE" name="TOWAREHOUSE" onchange="checktoloc(this.value)" value="<%=toHdr.getTOWAREHOUSE()%>">
							 <span class="select-icon" onclick="$(this).parent().find('input[name=\'TOWAREHOUSE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Order Type:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="ORDERTYPE" name="ORDERTYPE" onchange="checkordertype(this.value)" value="<%=toHdr.getORDERTYPE()%>">
							<span class="select-icon" 
								onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
							</span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Employee:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="EMP_NAME" name="EMP_NAME" onchange="checkemployeess(this.value)" value="<%=empname%>">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">INCOTERM:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="INCOTERMS" name="INCOTERMS" onchange="checkincoterms(this.value)" value="<%=toHdr.getINCOTERMS()%>">
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
			        	<input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" name="SALES_LOC" value="<%=toHdr.getSALES_LOCATION()%>" placeholder="Select a Purchase Location">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	        			
        			</div>
				</div>
				<%}%>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Currency</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control" id="CURRENCY" name="CURRENCY" onchange="checkcurrency(this.value)" value="<%=DISPLAY%>">
					</div>
					<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-5 required" id="exchangerate" ></label>
						<div class="col-sm-6">
							<div class="input-group">
								<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate"value="<%=toHdr.getCURRENCYUSEQT()%>" onkeypress="return isNumberKey(event,this,4)" required>
				   		 	</div>
						</div>
					</div>
				</div>
				
				<!-- <div class="form-group">
					<label class="control-label col-form-label col-sm-2">Equivalent Currency</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Equivalent Currency" value="1.00">
					</div>
				</div> -->
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Standard <%=taxbylabelordermanagement%> :</label><!-- this is dynamic value -->
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="GST" onchange="changetaxforgst()" onkeypress="return isNumberKey(event,this,4)" name="GST" value="<%=toHdr.getCONSIGNMENT_GST()%>">
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
							<select class="ac-box dropdown-noborder form-control" onchange="renderTaxDetails()" name="item_rates" id="item_rates">
								<option value="0" <%=(toHdr.getITEM_RATES() == 0 ? "selected" : "") %>>
									Tax Exclusive
								</option>
								<option value="1" <%=(toHdr.getITEM_RATES() == 1 ? "selected" : "") %>>
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
						for(ToDet toDet: toDetDetails){
							
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
							
							qty = toDet.getQTYOR().toString();
							double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
				  			qty = StrUtils.addZeroes(dQty, "3");
				  			
				  			double dPrice = toDet.getUNITPRICE() * toDet.getCURRENCYUSEQT();
				  			unitPrice = StrUtils.addZeroes(dPrice, numberOfDecimal);
				  			
				  			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
				  			String PUOMQTY ="1",SUOMQTY ="1";
                            ArrayList getuomqty = new MovHisDAO().selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+plant+"_UOM where UOM='"+ toDet.getUNITMO()+"'",htTrand1);
                			if(getuomqty.size()>0)
                			{
                			Map mapval = (Map) getuomqty.get(0);
                			PUOMQTY=(String)mapval.get("UOMQTY");
                			}
                			Double pcsprice = dPrice / Double.valueOf(PUOMQTY);
                			
                			String salesuom = new ItemMstDAO().getItemSalesUOM(plant,toDet.getITEM());
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
									<input type="hidden" name="lnno" value="<%=toDet.getTOLNNO()%>">
									<input type="hidden" name="itemdesc" value="<%=m.get("sItemDesc").toString().replaceAll("\"","&quot;")%>">
									<input type="hidden" name="unitpricerd" value="<%=dPrice%>">
<%-- 									<input type="hidden" name="itemprice" value="<%=pcsprice%>"> --%>
									<input type="hidden" name="itemprice" value="<%=m.get("price")%>">
									<input type="hidden" name="minstkqty" value="<%=m.get("stkqty")%>">
									<input type="hidden" name="maxstkqty" value="<%=m.get("maxstkqty")%>">
									<input type="hidden" name="stockonhand" value="<%=m.get("stockonhand")%>">
									<input type="hidden" name="outgoingqty" value="<%=m.get("outgoingqty")%>">
									<input type="hidden" name="customerdiscount" value="<%=m.get("outgoingOBDiscount")%>">
									<input type="hidden" name="unitpricediscount" value="<%=calAmount%>">
									<input type="hidden" name="tax_type" class="taxSearch" value="<%=toDet.getTAX_TYPE()%>">
									<input type="hidden" name="tax" class="taxSearch" value="<%=toDet.getTAX_TYPE()%>">
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
									<input type="text" name="item" class="form-control itemSearch" onchange="checkitems(this.value,this)" style="width:84%" 
									placeholder="Type or click to select an item." 
									value="<%=toDet.getITEM()%>">
									<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
									<input class="form-control"  value="<%=m.get("sItemDesc").toString().replaceAll("\"","&quot;")%>" name="ITEMDES" style="height: 23px;background-color: #fff;" readonly
									data-pd="<%=m.get("sItemDesc").toString().replaceAll("\"","&quot;")%>">
								</td>
								<td>
									<input type="text" name="account_name" class="form-control accountSearch" 
									placeholder="Account" value="<%=toDet.getACCOUNT_NAME()%>">
								</td>
								<td>
									<input type="text" name="UOM" class="form-control uomSearch" onchange="checkprduom(this.value,this)"
									placeholder="Select UOM" value="<%=toDet.getUNITMO()%>">
								</td>
								<td>
									<input type="text" name="QTY" class="form-control text-right" 
									 data-rl="<%=m.get("stkqty")%>" data-msq="<%=m.get("maxstkqty")%>" data-soh="<%=m.get("stockonhand")%>"
									  data-eq="<%=m.get("EstQty")%>" data-aq="<%=m.get("AvlbQty")%>" data-oq="<%=toDet.getQTYOR().doubleValue()%>" 
									 <%--  data-pq="<%=estdet.getQtyPick().doubleValue()%>"  --%>
									  data-iq="<%=toDet.getQTYRC().doubleValue()%>" value="<%=StrUtils.addZeroes(toDet.getQTYOR().doubleValue(), "3")%>" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
								</td>
								<td>
									<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" 
									 value="<%=toDet.getPRODUCTDELIVERYDATE()%>" READONLY tabindex="-1">
								</td>
								<td>
									<input type="text" name="unitprice" class="form-control text-right" 
									value="<%=unitPrice%>" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
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
									if(toDet.getDISCOUNT_TYPE().equals("%")){
										itemdisc = StrUtils.addZeroes(toDet.getDISCOUNT(), "3");
									}else{
										itemdisc = StrUtils.addZeroes((toDet.getDISCOUNT()*toDet.getCURRENCYUSEQT()), numberOfDecimal);
									}
									
									%>
									<input name="item_discount" type="text" class="form-control text-right" 
									value="<%=itemdisc%>" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
									<select name="item_discounttype" class="discountPicker form-control"
										onchange="calculateAmount(this)">
										<option value="<%=currency%>" 
										<%=(toDet.getDISCOUNT_TYPE().equals(currency)) ? "selected" : "" %> ><%=currency%></option>
										<option value="%" 
										<%=(toDet.getDISCOUNT_TYPE().equals("%")) ? "selected" : "" %>>%</option>
									</select>
									</div>
									</div>
									</div>
								</td>
								<%-- <td>
									<input type="text" name="tax" class="form-control taxSearch" 
									value="<%=toDet.getTAX_TYPE()%>"
										placeholder="Select a Tax" readonly>
									<input type="hidden" name="tax_type" value="<%=toDet.getTAX_TYPE()%>">
								</td> --%>
								
								<%
									double amount = toDet.getQTYOR().doubleValue() * dPrice;
									if(toDet.getDISCOUNT_TYPE().equals("%")){
										amount = amount - ((amount/100)*toDet.getDISCOUNT());
									}else{
										amount = amount - (toDet.getDISCOUNT()*toDet.getCURRENCYUSEQT());
									}
								%>
								<td class="text-right grey-bg">
									<input name="amount" type="text" class="form-control text-right"
									value="<%=StrUtils.addZeroes(amount, numberOfDecimal)%>" readonly="readonly" tabindex="-1">
								</td>
								<td class="table-icon" style="position:relative;">
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
						<a href="#" onclick="addRow(event)">
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
<%if(toHdr.getISORDERDISCOUNTTAX() == 1){ %>
<span id="odtax">(Tax Inclusive)</span>
<%}else{%>
<span id="odtax">(Tax Exclusive)</span>
<%} %>
</div>
<div class="col-lg-1 col-sm-1 col-1">
<input type="checkbox" class="form-check-input isodisctax" <%= (toHdr.getISORDERDISCOUNTTAX() == 1 ? "checked" : "")%> name="isodisctax" Onclick="isodisctaxing(this)">
</div>
<div class=" col-lg-6 col-sm-6 col-6">
<div class="input-group my-group">
<input class="form-control text-right oddiscount" type="text" value="<%=toHdr.getORDERDISCOUNT() %>"
name="orderdiscount" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
<select class="discountPicker form-control" name="oddiscount_type" id="oddiscount_type" onchange="calculateTotal()">
<option <%=(toHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%") ? "selected" : "") %> value="%">%</option>
<option <%=(toHdr.getORDERDISCOUNTTYPE().equalsIgnoreCase(currency) ? "selected" : "") %> value="<%=currency%>"><%=currency%></option>
</select>
</div>
</div>
</div>
</div>
<div class="total-amount" style="line-height: 2;">
<span id="orderdiscount"> </span>
</div>
</div>
<div class="total-row" style="display:none">
<div class="total-label">
<div class="row">
<div class="col-lg-4 col-sm-4 col-4">
<div class="form-control-plaintext">Discount</div>
<%if(toHdr.getISDISCOUNTTAX() == 1){ %>
<span id="dtax">(Tax Inclusive)</span>
<%}else{%>
<span id="dtax">(Tax Exclusive)</span>
<%} %>
</div>
<div class="col-lg-1 col-sm-1 col-1">
<input type="checkbox" class="form-check-input isdtax" <%= (toHdr.getISDISCOUNTTAX() == 1 ? "checked" : "")%> name="isdtax" Onclick="isdtaxing(this)">
</div>
<div class=" col-lg-6 col-sm-6 col-6">
<div class="input-group my-group">
<input class="form-control text-right" type="text" value="<%=toHdr.getDISCOUNT()%>"
name="discount" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
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
<%if(toHdr.getISSHIPPINGTAX() == 1){ %>
<span id="shtax">(Tax Inclusive)</span>
<%}else{%>
<span id="shtax">(Tax Exclusive)</span>
<%} %>
</div>
<div class="col-lg-1 col-sm-1 col-1">
<input type="checkbox" class="form-check-input isshiptax" <%= (toHdr.getISSHIPPINGTAX() == 1 ? "checked" : "")%> name="isshiptax" Onclick="isshiptaxing(this)">
</div>
<div class="col-lg-6 col-sm-6 col-6">
<input class="form-control text-right ember-view" type="text"
name="shippingcost" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)" value="<%=toHdr.getSHIPPINGCOST()%>">
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
<input class="form-control text-right ember-view" type="text" value="<%=toHdr.getADJUSTMENT() %>"
name="adjustment" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
</div>
</div>
</div>
<div class="total-amount " style="line-height: 2" id="adjustment">
0.00</div>
</div>

<div class="total-section total-row">
<div class="gross-total-removemargin"><!--  Author: Azees  Create date: July 17,2021  Description: Total of Local Currency-->
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
								placeholder="Max 100 characters"  maxlength="100"><%=toHdr.getRemark1()%></textarea>
						</div>

						<p>Remark2</p>
						<div>
							<textarea rows="2" name="REMARK3" class="ember-text-area form-control ember-view"
								placeholder="Max 100 characters"  maxlength="100"><%=toHdr.getRemark3()%></textarea>
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
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					  </div>
					</div>
					<input type="hidden" value="<%=displayCustomerpop%>" name="displayCustomerpop" id="displayCustomerpop" />
					<input type="hidden" value="<%=displayPaymentTypepop%>" name="displayPaymentTypepop" id="displayPaymentTypepop" />
				</div>
				<input id="sub_total" name="sub_total" value="" hidden> 
				<input id="total_amount" name="total_amount" value="" hidden>
				<input type ="hidden" name="ISAUTOGENERATE" value="false">
				<input id="taxamount" name="taxamount" value="" hidden>
				
				<input type = "hidden" name="PERSON_INCHARGE" value="">
				<input type = "hidden" name="CUSTOMERTYPEDESC" value="">				
				<input type = "hidden" name="CUST_CODE" id="CUST_CODE" value="<%=toHdr.getCustCode()%>">
				<input type = "hidden" name="CUST_CODE1" value="<%=toHdr.getCustCode()%>">
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
				
				<input type = "hidden" name="SHIPPINGID" value="<%=toHdr.getSHIPPINGID()%>">
				<input type = "hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>">
				<input type = "hidden" name="pronumberOfDecimal" value="<%=numberOfDecimal%>">
				<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
				<input type = "hidden" name="currency" value="<%=currency%>">
				
				<input type = "hidden" name="orderstatus" value="">
				
				
				<input type="hidden" name="STATE_PREFIX" value="<%=STATEPREFIX%>" />
				
				<input type = "hidden" name="SHIPCONTACTNAME" value="<%=toHdr.getSHIPCONTACTNAME()%>">
				<input type = "hidden" name="SHIPDESGINATION" value="<%=toHdr.getSHIPDESGINATION()%>">
				<input type = "hidden" name="SHIPADDR1" value="<%=toHdr.getSHIPADDR1()%>">
				<input type = "hidden" name="SHIPADDR2" value="<%=toHdr.getSHIPADDR2()%>">
				<input type = "hidden" name="SHIPADDR3" value="<%=toHdr.getSHIPADDR3()%>">
				<input type = "hidden" name="SHIPADDR4" value="<%=toHdr.getSHIPADDR4()%>">
				<input type = "hidden" name="SHIPSTATE" value="<%=toHdr.getSHIPSTATE()%>">
				<input type = "hidden" name="SHIPZIP" value="<%=toHdr.getSHIPZIP()%>">
				<input type = "hidden" name="SHIPWORKPHONE" value="<%=toHdr.getSHIPWORKPHONE()%>">
				<input type = "hidden" name="SHIPCOUNTRY" value="<%=toHdr.getSHIPCOUNTRY()%>">
				<input type = "hidden" name="SHIPHPNO" value="<%=toHdr.getSHIPHPNO()%>">
				<input type = "hidden" name="SHIPEMAIL" value="<%=toHdr.getSHIPEMAIL()%>">
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
%>
<%
	EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
	Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.CONSIGNMENT_ORDER);
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

<jsp:include page="newEmployeeModal.jsp" flush="true"></jsp:include>
<jsp:include page="newProductModal.jsp" flush="true"></jsp:include><!-- resvi -->
<jsp:include page="NewChartOfAccountpopup.jsp" flush="true"></jsp:include> <!-- imti for add account --> 
<jsp:include page="newPaymentTypeModal.jsp" flush="true"></jsp:include>
<jsp:include page="newOrderTypeModal.jsp" flush="true"></jsp:include>
<jsp:include page="newIncotermsModal.jsp" flush="true"></jsp:include>
<jsp:include page="newGstDetailModal.jsp" flush="true"></jsp:include>
<input type="text" id="PageName" style="display: none;" value="Estimate">
<input type="hidden" name="pronumberOfDecimal" id="pronumberOfDecimal" value="<%=numberOfDecimal%>"> <!-- imti -->
<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>

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

			<input type="hidden" name="plant" id="plant" value="<%=plant%>" > <!-- imti -->
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
							  <INPUT type="hidden" name="CURRENCYID_C"  value="<%=DISPLAYID%>">
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
	         
					
					<!-- 	                Author Name:Resviya ,Date:14/07/21 -->
					
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
							Group</label>
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
							<INPUT name="CUSTOMEREMAIL" id="CUSTOMEREMAIL" type="TEXT" value="" size="50"
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
			 
			 div class="col-sm-4">
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
<script>
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

//jsp validation
function checkproject(project){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PROJECT_NAME : project,
				ACTION : "PROJECT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Project Does't Exist");
						document.getElementById("project").focus();
						$("#project").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkpaymenttype(paytype){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PAYMENTMODE : paytype,
				ACTION : "PAYTYPE_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Payment Type Does't Exists");
						document.getElementById("PAYMENTTYPE").focus();
						$("#PAYMENTTYPE").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkfromloc(loc){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				LOC : loc,
				ACTION : "LOC_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Location Does't Exists");
						document.getElementById("FROMWAREHOUSE").focus();
						$("#FROMWAREHOUSE").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checktoloc(loc){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				LOC : loc,
				ACTION : "LOC_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Location Does't Exists");
						document.getElementById("TOWAREHOUSE").focus();
						$("#TOWAREHOUSE").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkordertype(ordertype){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ORDERTYPE : ordertype,
				ACTION : "ORDERTYPE_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Order Type Does't Exists");
						document.getElementById("ORDERTYPE").focus();
						$("#ORDERTYPE").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
function checkemployeess(employee){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				FNAME : employee,
				ACTION : "EMPLOYEE_CHECKS"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Employee Does't Exists");
						document.getElementById("EMP_NAME").focus();
						$("#EMP_NAME").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkincoterms(incoterms){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				INCOTERMS : incoterms,
				ACTION : "INCOTERMS_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Incoterms Does't Exists");
						document.getElementById("INCOTERMS").focus();
						$("#INCOTERMS").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkcurrency(currency){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CURRENCYID : currency,
				ACTION : "CURRENCY_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Currency Does't Exists");
						document.getElementById("CURRENCY").focus();
						$("#CURRENCY").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checktax(tax){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				TAXTYPE : tax,
				ACTION : "TAX_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Tax Does't Exists");
						document.getElementById("Salestax").focus();
						$("#Salestax").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
//jsp validation ends

//table validation
function checkitems(itemvalue,obj){	
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ITEM : itemvalue,
				ACTION : "PRODUCT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Does't Exists");
						$(obj).typeahead('val', '');
						$(obj).parent().find('input[name="item"]').focus();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkprduom(uom,obj){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				UOM : uom,
				ACTION : "UOM_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Uom Does't Exists");
						$(obj).typeahead('val', '');
						$(obj).parent().find('input[name="UOM"]').focus();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
//table validation ends

// modal onchange
function checkuom(uom){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				UOM : uom,
				ACTION : "UOM_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Uom Does't Exists");
						document.getElementById("Basicuom").focus();
						$("#Basicuom").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkpuruom(uom){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				UOM : uom,
				ACTION : "UOM_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Uom Does't Exists");
						document.getElementById("purchaseuom").focus();
						$("#purchaseuom").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checksaluom(uom){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				UOM : uom,
				ACTION : "UOM_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Uom Does't Exists");
						document.getElementById("salesuom").focus();
						$("#salesuom").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkinvuom(uom){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				UOM : uom,
				ACTION : "UOM_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Uom Does't Exists");
						document.getElementById("inventoryuom").focus();
						$("#inventoryuom").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkhscode(hscode){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				HSCODE : hscode,
				ACTION : "HSCODE_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("HS Code Does't Exists");
						document.getElementById("HSCODE").focus();
						$("#HSCODE").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkcoo(coo){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				COO : coo,
				ACTION : "COO_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("COO Does't Exists");
						document.getElementById("COO").focus();
						$("#COO").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkItemsupplier(supplier){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CUST_CODE : supplier,
				ACTION : "SUPPLIER_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Supplier Does't Exist");
						document.getElementById("vendname_itemmst").focus();
						$("#vendname_itemmst").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}


function checkprddept(dept){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PRD_DEPT_ID : dept,
				ACTION : "DEPT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Department Does't Exists");
						document.getElementById("PRD_DEPT_DESC").focus();
						$("#PRD_DEPT_DESC").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkprdcat(cat){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PRD_CLS_ID : cat,
				ACTION : "CAT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Category Does't Exists");
						document.getElementById("PRD_CLS_DESC").focus();
						$("#PRD_CLS_DESC").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkprdtype(type){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PRD_TYPE_ID : type,
				ACTION : "TYPE_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Sub Category Does't Exists");
						document.getElementById("PRD_TYPE_DESC").focus();
						$("#PRD_TYPE_DESC").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkprdbrand(brand){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PRD_BRAND_ID : brand,
				ACTION : "BRAND_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Brand Does't Exists");
						document.getElementById("PRD_BRAND_DESC").focus();
						$("#PRD_BRAND_DESC").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkprditem(itemvalue)
{	
	 if(itemvalue=="" || itemvalue.length==0 ) {
		alert("Enter Item!");
		document.getElementById("ITEM").focus();
		return false;
	}else{
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ITEM : itemvalue,
				ACTION : "PRODUCT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
                               
						alert("Product Already Exists");
						document.getElementById("ITEM").focus();
						document.getElementById("ITEM").value="";
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
		}	
}
//modal changes end

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
		//	$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
			$('#STATE').append('<OPTION>Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					   $('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
				 $('#C_STATE').empty();
				//	$('#C_STATE').append('<OPTION style="display:none;">Select State</OPTION>');
					$('#C_STATE').append('<OPTION>Select State</OPTION>');
						 $.each(StateList, function (key, value) {
							   $('#C_STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
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
		


	  var  curr = document.form1.CURRENCYID.value;
		    
		    var basecurrency = '<%=curency%>';  <%--    resvi --%>
		    document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";//Resvi
		    if(basecurrency!=curr)//  Author: Azees  Create date: July 17,2021  Description:  Total of Local Currency
				document.getElementById('showtotalcur').style.display = 'block';	
		    else
		    	document.getElementById('showtotalcur').style.display = 'none';
		    
		    document.getElementById('lbltotal').innerHTML = "Total ("+curr+")";

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


</script>
<%@include file="../jsp/newBankModal.jsp"%>
<script>
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
}
</script>
<jsp:include page="newPaymentModeModal.jsp" flush="true"></jsp:include> <!-- thansith Add for paymentmode -->
<jsp:include page="newProductDepTypeModal.jsp" flush="true"></jsp:include> <!-- Resvi for add product department -->
<jsp:include page="newProductCategoryModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product category -->
<jsp:include page="newProductSubCatgoryModal.jsp" flush="true"></jsp:include><!-- Thanzith for add product Sub Category -->
<jsp:include page="newProductBrandModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product Brand -->
<jsp:include page="newProductHsCodeModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product HsCode -->
<jsp:include page="newProductCOOModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product COO -->
<jsp:include page="newProductUOMmodal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product UOM -->
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

