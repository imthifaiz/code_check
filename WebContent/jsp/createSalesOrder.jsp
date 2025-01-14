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
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<%
	String title = "Create Sales Order";
	String isAutoGenerate = "false";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
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
	String numberOfDecimal = (String)request.getAttribute("NumberOfDecimal");
	String deldate = (String)request.getAttribute("DelDate");
	String collectionTime = (String)request.getAttribute("CollectionTime");
	String currency = (String)request.getAttribute("Currency"), zeroval = StrUtils.addZeroes(0.0, numberOfDecimal);
	String zerovalper = StrUtils.addZeroes(Float.parseFloat("0"), "3");
	String gst = (String)request.getAttribute("GST");
	String taxbylabel = (String)request.getAttribute("Taxbylabel");
	String region = (String)request.getAttribute("Region");
	String transports = StrUtils.fString(request.getParameter("TRANSPORTID"));
	String customertypec = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_IDC"));

	String msg = (String)request.getAttribute("Msg");
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
	String compindustry = plantMstDAO.getcompindustry(plant);
	String ISPEPPOL = plantMstDAO.getisPeppol(plant);
	
	 ArrayList plntList1 = plantMstDAO.getPlantMstDetails(plant);
		Map plntMap1 = (Map) plntList1.get(0);
		String isEmployee = (String) plntMap1.get("ISEMPLOYEEVALIDATESO");
	
	//Validate no.of Customers -- Azees 15.11.2020
	CustMstDAO custdao = new CustMstDAO();
	String NOOFCUSTOMER=((String) session.getAttribute("NOOFCUSTOMER"));
	String NOOFORDER=((String) session.getAttribute("NOOFORDER")); /* Resvi */
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
// 	resvi starts
	DoHdrDAO doHdrDAO = new DoHdrDAO();
	DateUtils _dateUtils = new DateUtils();
	String FROM_DATE = DateUtils.getDate();
	if (FROM_DATE.length() > 5)
		FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";

	String TO_DATE = DateUtils.getLastDayOfMonth();
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
// 	resvi ends

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
	 
	 String companyregnumber=StrUtils.fString(request.getParameter("cus_companyregnumber"));

	 String CURRENCYUSEQT="0",DISPLAY="",DISPLAYID="";;
		List curQryList=new ArrayList();
		CurrencyDAO currencyDAO = new CurrencyDAO();
		curQryList = currencyDAO.getCurrencyDetails(currency,plant);
		for(int i =0; i<curQryList.size(); i++) {
			ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
			DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
			DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
	        CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
	        }
		Map doHdrDetails = new HashMap();
		String replacePreviousSalesCost="0";
		doHdrDetails = new DOUtil().getDOReceiptInvoiceHdrDetails(plant, "Outbound Order");
		if(!doHdrDetails.isEmpty())
			replacePreviousSalesCost= (String) doHdrDetails.get("SHOWPREVIOUSSALESCOST");
		
		PltApprovalMatrixDAO pltApproval = new PltApprovalMatrixDAO();//Sales Approve 08.22 - Azees
		boolean approvalcheck = pltApproval.CheckApprovalByUser(plant, "SALES", "CREATE", username);
		
		 //
		 String empname = "";

	      if(!username.isEmpty()){
	         if (empname.isEmpty()) {
	          ArrayList arrList = new ArrayList();
	          EmployeeDAO movHisDAO = new EmployeeDAO();
	          Hashtable htData = new Hashtable();
	           htData.put("PLANT", plant);
	            htData.put("EMPNO", username);
	              arrList = movHisDAO.getEmployeeDetails("FNAME", htData, "");
	   
	              if (!arrList.isEmpty()) {
	                  Map m = (Map) arrList.get(0);
	      
	                  empname = (String) m.get("FNAME");
	       
	              }
	          }
	       }
			//	

		String plantCompany = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);
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

.user-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -10%;
	top: 20px;
}
.bill-action {
    right: -60% !important;
}
.phideshow{
    float: right;
    margin-top: -34px;
    position: relative;
    z-index: 2;
    margin-right: 40px;
}
/* .custom-scroll-table{    
	overflow-x: scroll !important;
    min-width: 112%;
    }
.custom-scroll-row{   
	 overflow-x: scroll;
} */
a:focus {
  outline: 1px solid blue;
}
 </style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/SalesOrder.js"></script>
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
                <li><a href="../salesorder/summary"><span class="underline-on-hover">Sales Order Summary</span> </a></li>              
                <li><label>Create Sales Order</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../salesorder/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" id="salesOrderForm" name="form1" autocomplete="off" method="post" action="../salesorder/new" enctype="multipart/form-data">
			<input name="ORDER_TYPE_MODAL" type="hidden" value="SALES">
			<input type = "hidden" name="plant" value="<%=plant%>">
			<input type = "hidden" name="PROJECTID" value="">
			<input type="hidden" name="TRANSPORTID" value="">
			<input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
			<input type="hidden" name="CURRENCYUSEQTOLD" value="1">
				<input type = "hidden" name="shiptaxstatus" value="0">
				<input type = "hidden" name="odiscounttaxstatus" value="1">
				<input type = "hidden" name="discounttaxstatus" value="1">
				<input type = "hidden" name="ptaxtype" value="">
				<input type = "hidden" name="ptaxpercentage" value="0">
				<input type = "hidden" name="ptaxdisplay" value="">
				<input type = "hidden" name="ptaxiszero" value="1">
				<input type = "hidden" name="ptaxisshow" value="0">
				<input type = "hidden" name="taxid" value="0">
				<input type = "hidden" name="CUST_ADDONCOST" value="0.0">
				<input type = "hidden" name="CUST_ADDONCOSTTYPE" value="0">
				<INPUT type="hidden" name="replacePreviousSalesCost"  value="<%=replacePreviousSalesCost%>">
				<INPUT type="hidden" name="oldreplacePreviousSalesCost"  value="<%=replacePreviousSalesCost%>">
				<input type = "hidden" name="custModal">
				<INPUT type="hidden" name="CURRENCYID"  value="<%=DISPLAYID%>">
				<INPUT type="hidden" name="curency"  value="<%=DISPLAYID%>">
	            <INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>">
	            <INPUT type="hidden" name="BASECURRENCYID"  value="<%=currency%>"> <%--Resvi--%>
	            <input name="COMP_INDUSTRY" type="hidden" value="<%=COMP_INDUSTRY%>">  <!-- imti added condition based on product popup -->
	            <input name="PARENT_PLANT" type="hidden" value="<%=plantCompany%>">  <!-- imti added condition based on Parentplant for tax-->
	            
	            <input type = "hidden" name="EMPNO" value="0">

				<%if(!ispuloc){ %>
				 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
				<%} %>

				<div class="form-group customer-section">
					<label class="control-label col-form-label col-sm-2 required">Customer:</label>
					<div class="col-sm-6 ac-box">
<!-- 						<div class="input-group"> -->
							<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" onchange="checkcustomer(this.value)" onclick="removetaxtrestl()"
							placeholder="Select a customer" name="CUST_NAME" value="">
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUST_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
<!-- 							<span class="btn-danger input-group-addon" -->
<!-- 								onClick="javascript:popUpWin('../jsp/customer_list_order.jsp?CUST_NAME='+form.CUST_NAME.value);"> -->
<!-- 								<span class="glyphicon glyphicon-search" aria-hidden="true"></span> -->
<!-- 							</span> -->
<!-- 			   		 	</div> -->
			   		 	<span><a href="#" title="Tax Treatment" id="TAXTREATMENT" hidden></a></span>
			   		 	<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" id="nTAXTREATMENT" name="nTAXTREATMENT" value=""  style="width: 100%">
							<OPTION style="display:none;"></OPTION>
        					<%
							for(int i=0 ; i<taxTreatmentList.size();i++){
								Map m1=(Map)taxTreatmentList.get(i);
								String nTAXTREATMENT = (String)m1.get("TAXTREATMENT"); %>
		        				<option  value='<%=nTAXTREATMENT%>' ><%=nTAXTREATMENT %> </option>
		        			<%} %>
	        			</SELECT>
					</div>
				</div>



				<div class="form-group shipCustomer-section" id="shipbilladd">
					<!-- <label class="control-label col-form-label col-sm-2">Shipping Address:</label> -->
					<div class="col-sm-2"></div>
					<!-- <div class="col-sm-6 ac-box">
								<input type="text" class="ac-selected  form-control typeahead"
									id="SHIPPINGCUSTOMER" placeholder="Select a shipping address"
									name="SHIPPINGCUSTOMER" value="">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'SHIPPINGCUSTOMER\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>

					</div> -->
					
							<!-- <textarea rows="2" name="SHIPPINGCUSTOMER" id="shipaddres"class="ember-text-area form-control ember-view"  maxlength="500" readonly></textarea> -->
				</div>
				
					<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Transport Mode:</label>
					<div class="col-sm-4 ac-box">
<!-- 					<div class="input-group"> -->
								<input type="text" class="form-control typeahead"
									id="transport" placeholder="Select a transport"
									name="transport"  onchange="checktransport(this.value)" value="">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
								<!-- <span class="btn-danger input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
<!-- 					</div> -->
					</div>
				</div>
				<%if(compindustry.equalsIgnoreCase("Construction") || compindustry.equalsIgnoreCase("Service")){ %>
				<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4">
<!-- 						<div class="input-group"> -->
								<input type="text" class="ac-selected  form-control typeahead"
									id="project" placeholder="Select a project"
									name="project" onchange="checkproject(this.value)" value="">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'project\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
								<!-- <span class="btn-danger input-group-addon">
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
						<input type="text" class="form-control" id="PAYMENTTYPE" name="PAYMENTTYPE" onchange="checkpaymenttype(this.value)" value="">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'PAYMENTTYPE\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
					
					<div class="col-sm-6 no-padding">
       				<label class="control-label col-form-label col-sm-5">Payment Terms</label>
       				<div class="col-sm-6">
       				<input type="text" class="form-control" id="payment_terms"	name="payment_terms" onchange="checkpaymentterms(this.value)" value="" placeholder="Select Payment Terms">
					<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
    				</div>
				</div>

				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Order Number:</label>
				<div class="col-sm-4">
					<div class="input-group">
					<input type="text" class="form-control" id="DONO" name="DONO" onchange="checkorderno(this.value)" onkeypress="return blockSpecialCharOrderNo(event)">
					<span class="input-group-addon" id="autoGen">
					<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
					<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i> </a>
				   	</span>
			   		 </div>
				</div>
				<div class="col-sm-6 no-padding">
					<label class="control-label col-form-label col-sm-5 required">Order Date:</label>
				<div class="col-sm-6 ac-box">
					<input type="text" class="form-control datepicker" id="DELDATE"	name="DELDATE" value="<%=deldate%>" readonly>
				</div>
				</div>
				</div>

				<!-- <div class="form-group">
					<label class="control-label col-form-label col-sm-2">Reference No:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="JOB_NUM" name="JOB_NUM"
							placeholder="Max 20 Characters" maxlength="20">
			   		 	</div>
					</div>
				</div> -->

				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Reference No:</label>
				<div class="col-sm-4">
					<div class="input-group">
						<input type="text" class="form-control" id="JOB_NUM" name="JOB_NUM" placeholder="Max 20 Characters" maxlength="20">
					</div>
				</div>
					<div class="col-sm-6 no-padding" style="display: none;">
						<label class="control-label col-form-label col-sm-5">Order Time:</label>
						<div class="col-sm-6" style="display: none;">
							<div class="input-group">
								<input type="text" class="form-control" id="COLLECTION_TIME"
									name="COLLECTION_TIME" value="<%=collectionTime%>" readonly>
				   		 	</div>
						</div>
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Delivery Period/Date:</label>
					<div class="col-sm-3">
						<div class="input-group">
							<input type="text" class="form-control" id="DELIVERYDATE" name="DELIVERYDATE"
							placeholder="Max 20 Characters" maxlength="20">
						</div>
					</div>
					<div class="form-inline">
				       	<label class="control-label col-sm-1">
					       	<input type="checkbox" id="DATEFORMAT" name="DATEFORMAT"
					       	onclick="headerReadable();"><font size="2.9"><b>&nbsp;By Date</b></font>
				       	</label>
			    	</div>
			    	
				</div>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Order Type:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="ORDERTYPE" name="ORDERTYPE" onchange="checkordertype(this.value)" value="">
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
						<input type="text" class="form-control" id="EMP_NAME" name="EMP_NAME" onchange="checkemployeess(this.value)" value="<%=empname%>">
						<input type="hidden" name="ISEMPLOYEEVALIDATESO" value="<%=isEmployee%>">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">INCOTERM:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="INCOTERMS" name="INCOTERMS" onchange="checkincoterms(this.value)" value="">
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
						        <option  value= <%=STATE_PREFIX%> ><%=STATE %> </option>
	        			<%} %>
	        			</SELECT>
					</div>
				</div> --%>

				<%if(ispuloc){ %>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2" for="Purchase Location">Sales Location:</label>
        			<div class="col-sm-4">
			        	<input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" name="SALES_LOC" value="<%=plantstate%>" placeholder="Select a Purchase Location">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>

        			</div>
				</div>
				<%}%>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Currency</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control typeahead" id="CURRENCY" name="CURRENCY" placeholder="Select a Currency" onchange="checkcurrency(this.value)" value="<%=currency%>">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				

			<!-- Author Name:Resviya ,Date:16/07/21 -->
                   
						<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-5 required" id="exchangerate" ></label>
						<div class="col-sm-6 ac-box">
							<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onkeypress="return isNumberKey(event,this,4)" required>	
						</div>
					    </div>
					    </div>
               <!--  Ends  -->
				
								<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Standard <%=taxbylabelordermanagement%> :</label><!-- this is dynamic value -->
        			<div class="col-sm-4">
        									<div class="input-group">
			        	<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" onkeypress="return isNumberKey(event,this,4)" value="<%=gst%>">
						<span class="input-group-addon" style="font-size: 15px; color: #0059b3">
						<b>%</b>
						</span>
        			</div>
        			</div>
				</div>
								
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
							<div class="input-group">
							<input type="text" class="form-control" id="Salestax" name="Salestax" onchange="checktax(this.value)" value="" placeholder="Select a Tax">
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'Salestax\']').focus()">
							<i class="glyphicon glyphicon-menu-down"></i>
							</span>
			   		 		</div>
					</div>
				
<!-- 					<div class="col-sm-6 no-padding"> -->
<%-- <%-- 						<label class="control-label col-form-label col-sm-5">Standard <%=taxbylabelordermanagement%> :</label><!-- this is dynamic value --> --%> 
<!-- 					<div class="col-sm-6"> -->
<!-- 						<div class="input-group"> -->
<%-- 						<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" value="<%=gst%>"> --%>
<!-- 						<span class="input-group-addon" style="font-size: 15px; color: #0059b3"> -->
<!-- 						<b>%</b> -->
<!-- 						</span> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					</div> -->
					</div>

				<!-- <div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Salestax" name="Salestax" placeholder="Select a Tax">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'Salestax\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div> -->

				<hr />
				<div class="form-group">
					<div class="col-sm-12">
						<label class="control-label col-form-label">Product Rates
							Are</label>
						<div class="dropdown-noborder">
							<select class="ac-box dropdown-noborder form-control"
								onchange="calculateTotal()" name="item_rates" id="item_rates">
								<option value="0">Tax Exclusive</option>
								<option value="1">Tax Inclusive</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row custom-scroll-row">
					<table class="table table-bordered line-item-table so-table custom-scroll-table">
						<thead>
							<tr>
								<th colspan=2 style="width:16%">Product</th>
								<th class="bill-desc"  style="width:7%">Account</th>
								<th  style="width:7%">UOM</th>
								<th style="width:7%">Qty</th>
								<th style="width:10%">Delivery Date</th>
								<th style="width:7%">Cost</th>
<%-- 								<th style="width:7%">Add On (% / <%=currency%>)</th> --%>
								<th style="width:7%" id="AODTYPE"></th>
								<th colspan=2  style="width:10%">Unit Price</th>
								<th style="width:7%">Discount</th>
								<!-- <th style="width:7%">After Discount price</th> -->
								<!-- <th style="width:10%">Tax</th> -->
								
								<th  colspan=2 style="width:10%">Amount</th>
<!-- 								<th colspan=2 style="width:15%">Delivery Date</th> -->
							</tr>
						</thead>
						<tbody>
							<tr id="invtrid">
								<td class="item-img text-center">
									<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">
									<input type="hidden" name="lnno" value="1">
									<input type="hidden" name="itemdesc" value="">
									<input type="hidden" name="itemprice" value="0.00">
									<input type="hidden" name="itemcost" value="">
									<input type="hidden" name="unitpricerd" value="0.00">
									<input type="hidden" name="minstkqty" value="">
									<input type="hidden" name="maxstkqty" value="">
									<input type="hidden" name="stockonhand" value="">
									<input type="hidden" name="outgoingqty" value="">
									<input type="hidden" name="customerdiscount" value="">
									<input type="hidden" name="unitpricediscount" value="">
									<input type="hidden" name="discounttype" value="">
									<input type="hidden" name="minsp" value="">
									<input type="hidden" name="tax" class="taxSearch" value="">
								</td>
								<td class="bill-item">
									<input type="text" name="item" class="form-control itemSearch" onclick="loaddropdata(this)" onchange="checkitems(this.value,this)" style="width:87%;display: inline-block;"
									placeholder="Type or click to select an item.">
									<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
								    <input class="form-control"  name="ITEMDES" style="height: 23px;background-color: #fff;" readonly>
								</td>
								<td class="bill-item"><input type="text" name="account_name" onchange="checkaccount(this.value,this)"
									class="form-control accountSearch"
									placeholder="Account" value="Local sales - retail"></td>

								<td class="bill-item">
									<input type="text" name="UOM" class="form-control uomSearch" onchange="checkprduom(this.value,this)"
									placeholder="UOM">
								</td>
								<td class="item-qty">
									<input type="text" name="QTY" class="form-control text-right"
									 data-rl="0.000" data-msq="0.000" data-soh="0.000"
									  data-eq="0.000" data-aq="0.000" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
								</td>
								
								<td class="item-amount text-right grey-bg">
									<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" READONLY  tabindex="-1">
								</td>
								
								<td>
									<!-- <input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" READONLY> -->
									<input type="text" name="unitcost" class="form-control text-right" value="<%=zeroval%>" readonly tabindex="-1">
								</td>
								
									
								<td>
									<input type="text" name="addonshow" class="form-control text-right" value="<%=zeroval%>" readonly tabindex="-1">
									<input type="hidden" name="addonprice" value="<%=zeroval%>">
									<input type="hidden" name="addontype" value="<%=currency%>">
								</td>
								
								
								
								<td class="item-cost">
									<input type="text" name="unitprice" class="form-control text-right" value="<%=zeroval%>" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
									<input type="hidden" name="CUST_ADDONCOST_DET" class="form-control text-right" value="<%=zeroval%>" onkeypress="return isNumberKey(event,this,4)">
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
									<input name="item_discount" type="text" class="form-control text-right"
									value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
									<select name="item_discounttype" class="discountPicker form-control"
										onchange="calculateAmount(this)">
										<option value="<%=currency%>"><%=currency%></option>
										<option value="%">%</option>
									</select>
									</div>
									</div>
									</div>
								</td>
								<!-- <td class="item-tax">
									<input type="text" name="tax" class="form-control taxSearch"
										placeholder="Select a Tax" readonly>
								</td> -->
								
								
								<%-- <td>
								<input type="text" name="adprice" class="form-control text-right" value="<%=zeroval%>" readonly>
								</td> --%>
								<td>
									
									<input name="amount" type="text" class="form-control text-right"
									value="<%=zeroval%>" readonly="readonly"  tabindex="-1">
								</td>
<!-- 								<td class="item-amount text-right grey-bg"> -->
<!-- 									<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" READONLY> -->
<!-- 								</td> -->
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
						<!-- <a href="#"onclick="addRow(event)"> -->
						<a href="#"onclick="addRow2(event)">
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
										<span id="odtax">(Tax Inclusive)</span>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isodisctax" checked name="isodisctax" Onclick="isodisctaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right oddiscount" type="text" value="<%=zerovalper %>"
												name="orderdiscount" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
											<%-- <select class="discountPicker form-control" id="oddiscount">
												<option value="<%=currency%>"><%=currency%></option>
												<option value="%">%</option>
											</select> --%>
											<select class="discountPicker form-control" name="oddiscount_type" id="oddiscount_type" onchange="calculateTotal()">
												<option value="<%=currency%>"><%=currency%></option>
												<option value="%">%</option>												
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="total-amount" style="line-height: 2;">
								<span id="orderdiscount"> <%=zeroval %> </span>
							</div>
						</div>
						<div class="total-row" style="display:none"><!--  Author: Azees  Create date: July 5,2021  Description: Hide Discount --> 
							<div class="total-label">
								<div class="row">
									<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Discount</div>
										<span id="dtax">(Tax Inclusive)</span>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isdtax" checked name="isdtax" Onclick="isdtaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right" type="text" value="<%=zeroval%>"
												name="discount" onchange="calculateTotal()">
											<select class="discountPicker form-control" id="discount_type"
												name="discount_type" onchange="calculateTotal()">
												<option value="<%=currency%>"><%=currency%></option>
												<option value="%">%</option>
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
										<span id="shtax">(Tax Exclusive)</span>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isshiptax" name="isshiptax" Onclick="isshiptaxing(this)">
									</div>
									<div class="col-lg-6 col-sm-6 col-6">
										<input class="form-control text-right ember-view" type="text"
											name="shippingcost" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)" value="<%=zeroval%>">
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
										<input class="form-control text-right ember-view" type="text" value="<%=zeroval %>"
											name="adjustment" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
									</div>
								</div>
							</div>
							<div class="total-amount " style="line-height: 2" id="adjustment">
								0.00</div>
						</div>

						<div class="total-section total-row">
							<div class="gross-total-removemargin"><!--  Author: Azees  Create date: July 19,2021  Description: Total of Local Currency-->
								<div class="total-label"><label id="lbltotal"></label></div>
								<div class="total-amount" id="total">0.00</div>
							</div>
						</div>
						<div class="total-section total-row" id="showtotalcur">
							<div class="gross-total">
								<div class="total-label">Total (<%=currency%>)</div>
								<div class="total-amount" id="totalcur">0.00</div>
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
								placeholder="Max 100 characters"  maxlength="100"></textarea>
						</div>

						<p>Remark2</p>
						<div>
							<textarea rows="2" name="REMARK3" class="ember-text-area form-control ember-view"
								placeholder="Max 100 characters"  maxlength="100"></textarea>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 txn-buttons">
						<div class="dropup">
						<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
						<%
							if (approvalcheck) {
							%>
							<button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">
								Save and Waiting For Approval <span class="caret"></span>
							</button>
							<ul class="dropdown-menu">
								<li><a id="btnWaitForApp" href="#">Save and Waiting For	Approval</a></li>
							</ul>
							<%
							} else {
							%>
					    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Save as Open
					    <span class="caret"></span></button>
					    <ul class="dropdown-menu">
					      <li><a id="btnSalesOpen" href="#">Save as Open</a></li>
					      <li><a id="btnSalesDraft" href="#">Save as Draft</a></li>
					      <li><a id="btnSalesOpenEmail" href="#">Save as Open and Send Email</a></li>
					    </ul>
					    <%
							}
						%>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					  </div>
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
				<input type = "hidden" name="CUST_CODE" id="CUST_CODE" value="">
				<input type = "hidden" name="CUST_CODE1" value="">
				<input type = "hidden" name="TELNO" value="">
<!-- 				<input type = "hidden" name="EMAIL" value=""> -->
				<input type = "hidden" name="ADD1" value="">
				<input type = "hidden" name="ADD2" value="">
				<input type = "hidden" name="ADD3" value="">
				<input type = "hidden" name="REMARK2" value="">
				<input type = "hidden" name="HPNO" value="">
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


				<input type="hidden" name="STATE_PREFIX" value="<%=plantstatecode%>" />
				<input type = "hidden" name="SHIPCONTACTNAME" value="">
				<input type = "hidden" name="SHIPDESGINATION" value="">
				<input type = "hidden" name="SHIPADDR1" value="">
				<input type = "hidden" name="SHIPADDR2" value="">
				<input type = "hidden" name="SHIPADDR3" value="">
				<input type = "hidden" name="SHIPADDR4" value="">
				<input type = "hidden" name="SHIPSTATE" value="">
				<input type = "hidden" name="SHIPZIP" value="">
				<input type = "hidden" name="SHIPWORKPHONE" value="">
				<input type = "hidden" name="SHIPCOUNTRY" value="">
				<input type = "hidden" name="SHIPHPNO" value="">
				<input type = "hidden" name="SHIPEMAIL" value="">
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
<jsp:include page="newEmployeeModal.jsp" flush="true"></jsp:include>
<jsp:include page="newPaymentTypeModal.jsp" flush="true"></jsp:include>
<jsp:include page="newProductModal.jsp" flush="true"></jsp:include>
<jsp:include page="NewChartOfAccountpopup.jsp" flush="true"></jsp:include>
<jsp:include page="newOrderTypeModal.jsp" flush="true"></jsp:include>
<jsp:include page="newIncotermsModal.jsp" flush="true"></jsp:include>
<jsp:include page="newGstDetailModal.jsp" flush="true"></jsp:include>


<!-- resvi -->

<input type="text" id="PageName" style="display: none;" value="Estimate">
<input	type="hidden" name="pronumberOfDecimal" id="pronumberOfDecimal" value="<%=numberOfDecimal%>">
<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
<!-- ends -->
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
								<INPUT class="form-control" name="CUST_CODE_C" id="CUST_CODE_C" onkeypress="return blockSpecialChar(event)"
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
							 <input type="hidden" name="TRANSPORTSID" value="<%=transports%>">
							 <INPUT type="hidden" name="CUST_SHIP_COUNTRY" value="">
							  <input type="hidden" name="CUSTOMER_TYPE_ID" value="<%=customertypec%>">
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Customer
							Name</label>
						<div class="col-sm-4">
							<INPUT class="form-control" name="CUST_NAME_C" type="TEXT" value=""
								size="50" MAXLENGTH=100>
						</div>
						<div class="form-inline">
      <label class ="checkbox-inline">
	<input type = "checkbox" id = "APP_SHOWBY_PRODUCT" name = "APP_SHOWBY_PRODUCT" value = "APP_SHOWBY_PRODUCT" />Show APP Products Image</label>
    </div> 
					</div>
					
					
					 <div class="form-group" id="UEN">
	      <label class="control-label col-form-label col-sm-2" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
	      <div class="col-sm-4">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="cus_companyregnumber" id="cus_companyregnumber" type = "TEXT" value="<%=companyregnumber%>" size="50"  MAXLENGTH=50> 
	  	  </div>
	       </div>
	       
	   <div class="form-inline">
       <label class ="checkbox-inline">
       <input type = "checkbox" id = "APP_SHOWBY_CATEGORY" name = "APP_SHOWBY_CATEGORY" value = "APP_SHOWBY_CATEGORY" />Show APP Category Image</label>
    </div>
	       </div>
	         <!-- End --> 
	         
							
					<!-- 	                Author Name:Resviya ,Date:15/07/21 -->

                      <div class="form-group">
					  <label class="control-label col-form-label col-sm-2 required">Customer
							Currency</label>
					 <div class="col-sm-4">
					  <INPUT class="form-control" name="CUS_CURRENCY"  id="CUS_CURRENCY" type="TEXT" onchange="checkcustomercurrency(this.value)" value="<%=currency%>"  size="50" MAXLENGTH=100>
					 </div>
					 </div>
					 
<!-- 					 End -->


					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Customer
							Group</label>
						<div class="col-sm-4">
								<input name="CUSTOMER_TYPE_C" id="CUSTOMER_TYPE_C" type="TEXT" placeholder="Select a Customer Group" value="" size="50"
									MAXLENGTH=50 class="form-control"> 
								<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_C\']').focus()"> 	
   		 						<i class="glyphicon glyphicon-menu-down"></i></span>
						</div>
					</div>
					
					<div class="form-group">
      					<label class="control-label col-form-label col-sm-2" for="transmode">Transport Mode</label>
      						<div class="col-sm-4">           	
    							<input name="transports" id="transports" type="TEXT"  placeholder="Select a transport" onchange="checkcustomertransport(this.value)" value="" size="50" MAXLENGTH=50 class="form-control">
   		 						<span class="select-icon" onclick="$(this).parent().find('input[name=\'transports\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
								</span>
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
					
					<%if(ISPEPPOL.equalsIgnoreCase("1")){ %>
      	 <div class="form-group">
      <label class="control-label col-form-label col-sm-2">Peppol</label>
      <div class="col-sm-4">                
        <label class="checkbox-inline">
      <INPUT name="PEPPOL_C"  type = "checkbox" value="PEPPOL_C" id="PEPPOL_C" >
    </label>
      </div>
    </div>
     <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Peppol idc">Peppol Id</label>
      	<div class="col-sm-4">  
    	<INPUT name="PEPPOL_IDC" id="PEPPOL_IDC" type="TEXT" value="" size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	<%}else{%>
    	<input type="hidden" name="PEPPOL_C" id="PEPPOL_C" value="">
					<input type="hidden" name="PEPPOL_IDC" id="PEPPOL_IDC" value="">
    <%}%>

					<div class="bs-example">
						<ul class="nav nav-tabs" id="myTab">
							<li class="nav-item active"><a href="#other"
								class="nav-link" data-toggle="tab" aria-expanded="true">Other
									Details</a></li>
							<li class="nav-item"><a href="#profile" class="nav-link"
								data-toggle="tab">Contact And Address Details</a></li>
							<li class="nav-item"><a href="#bank_cus" class="nav-link"
							 	data-toggle="tab">Bank Account Details</a></li>
							 	<li class="nav-item">
                                <a href="#user_c" class="nav-link" data-toggle="tab">User</a></li>
							<li class="nav-item"><a href="#remark" class="nav-link"
								data-toggle="tab">Remarks</a></li>
						</ul>
						<div class="tab-content clearfix">
							<%-- <div class="tab-pane fade" id="home">
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
								</div> --%>

<!-- 							</div> -->

							<div class="tab-pane fade" id="profile">
								<br>
                     <h1 class="col-sm-3" style="font-weight: bold;font-size: 16px;right: 16px;">  Contact Address </h1>
                      <h1 style="font-weight: bold;font-size: 16px;margin-left: 450px;">  Shipping Address <label class="checkbox-inline" style="margin-left: 50px;margin-top: 2px;">
                             <input type="checkbox" id="SameAsContactAddress" name="SameAsContactAddress" value="SameAsContactAddress" onclick="shippingAddress();">Same As Contact Address</label></h1>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Contact
										Name</label>
									<div class="col-sm-4">

										<INPUT name="CONTACTNAME" id="CONTACTNAME"  type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="100">
									</div>
									
									<div class="col-sm-4">
                 
                                    <INPUT name="CUST_SHIP_CONTACTNAME" id="CUST_SHIP_CONTACTNAME" type="TEXT" class="form-control"
			                      value="" size="50" MAXLENGTH="100" >
                                  </div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Designation</label>
									<div class="col-sm-4">

										<INPUT name="DESGINATION" id="DESGINATION"type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="30">
									</div>
									
									 <div class="col-sm-4">
                
                                <INPUT name="CUST_SHIP_DESGINATION" id="CUST_SHIP_DESGINATION" type="TEXT" class="form-control"
			                    value="" size="50" MAXLENGTH="30">
                                </div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Work
										Phone</label>
									<div class="col-sm-4">
										<INPUT name="WORKPHONE"  id="WORKPHONE" type="TEXT" value=""
											onkeypress="return isNumber(event)" size="50" MAXLENGTH=30
											class="form-control">
									</div>
									
										<div class="col-sm-4">  
                                   <INPUT name="CUST_SHIP_WORKPHONE" id="CUST_SHIP_WORKPHONE" type="TEXT" value="" onkeypress="return isNumber(event)"	size="50" MAXLENGTH=30 class="form-control">
                              	</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Mobile</label>
									<div class="col-sm-4">

										<INPUT name="HPNO" id="HPNO" type="TEXT" value="" size="50"
											class="form-control" onkeypress="return isNumber(event)"
											MAXLENGTH="30">
									</div>
							   <div class="col-sm-4">
                              <INPUT name="CUST_SHIP_HPNO" id="CUST_SHIP_HPNO" type="TEXT" value="" size="50" class="form-control" onkeypress="return isNumber(event)"
			                  MAXLENGTH="30">
                              </div>
									
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Email</label>
									<div class="col-sm-4">

										<INPUT name="EMAIL"  id="EMAIL" type="TEXT" value="" size="50"
											MAXLENGTH="50" class="form-control">
									</div>
									
									<div class="col-sm-4"> 
                                <INPUT name="CUST_SHIP_EMAIL" id="CUST_SHIP_EMAIL" type="TEXT" value="" size="50"
			                    MAXLENGTH="50" class="form-control">
                                 </div>
								</div>
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
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Unit
										No.</label>
									<div class="col-sm-4">

										<INPUT name="ADDR1" id="ADDR1"  type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
									<div class="col-sm-4">  
               
                             <INPUT name="CUST_SHIP_ADDR1" id="CUST_SHIP_ADDR1" type="TEXT" value="" size="50"
		                    	MAXLENGTH=50 class="form-control">
                               </div>
								</div>
								
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Building</label>
									<div class="col-sm-4">

										<INPUT name="ADDR2" id="ADDR2" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
									<div class="col-sm-4">
              
                           <INPUT name="CUST_SHIP_ADDR2" id="CUST_SHIP_ADDR2"type="TEXT" value="" size="50"
		                	MAXLENGTH=50 class="form-control">
                           </div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Street</label>
									<div class="col-sm-4">

										<INPUT name="ADDR3" id="ADDR3" type="TEXT" value="" size="50"
											MAXLENGTH=50 class="form-control">
									</div>
									<div class="col-sm-4">
                
                        <INPUT name="CUST_SHIP_ADDR3" id="CUST_SHIP_ADDR3" type="TEXT" value="" size="50"
			           MAXLENGTH=50 class="form-control">
                        </div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">City</label>
									<div class="col-sm-4">

										<INPUT name="ADDR4" id="ADDR4" type="TEXT" value="" size="50"
											MAXLENGTH=80 class="form-control">
									</div>
									 <div class="col-sm-4"> 
                
                              <INPUT name="CUST_SHIP_ADDR4" id="CUST_SHIP_ADDR4" type="TEXT" value="" size="50"
		                      	MAXLENGTH=80  class="form-control">
                                </div>
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">State</label>
									<div class="col-sm-4">
										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="C_STATE" name="STATE" value="" style="width: 100%">
										<OPTION style="display:none;">Select State</OPTION>
										</SELECT>
									</div>
									      <div class="col-sm-4">           
                   <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="CUST_SHIP_STATE" name="CUST_SHIP_STATE" value="" style="width: 100%">
			       	<OPTION style="display:none;">Select State</OPTION>
				     </SELECT>
				<%-- <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control"> --%>
      </div>
									
								</div>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Postal
										Code</label>
									<div class="col-sm-4">

										<INPUT name="ZIP" id="ZIP" type="TEXT" value="" size="50" MAXLENGTH=10
											class="form-control">
                                    </div>
                                    <div class="col-sm-4">
                        <INPUT name="CUST_SHIP_ZIP" id="CUST_SHIP_ZIP" type="TEXT" value="" size="50"
		             	MAXLENGTH=10 class="form-control">
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
								size="50" MAXLENGTH="50">
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
									 <input id="CUST_PAYTERMS" name="CUST_PAYTERMS" class="form-control" type="text" onchange="checkcustomerpaymenttype(this.value)" value="" MAXLENGTH=100 placeholder="Select Payment Mode">
		    						<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUST_PAYTERMS\']').focus()">
						 			<i class="glyphicon glyphicon-menu-down"></i></span>
						 			
						 			
						 			
										<!-- <div class="input-group">
											<INPUT class="form-control" name="PAYTERMS" type="TEXT"
												value="" size="20" MAXLENGTH=100 readonly> <span
												class="input-group-addon"
												onClick="javascript:popUpWin('../jsp/list/paymenttypelist_save.jsp?paymenttype='+form.PAYTERMS.value);">
												<a href="#" data-toggle="tooltip" data-placement="top"
												title="Payment Type"> <i
													class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
												</span>
												</div> -->
									</div>
								</div>
								
						<div class="form-group">
       						<label class="control-label col-form-label col-sm-2">Payment Terms</label>
       					<div class="col-sm-4">
       						<input type="text" class="form-control" id="cpayment_terms"	name="cpayment_terms" onchange="checkcustomerpaymentterms(this.value)" value="" placeholder="Select Payment Terms">
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'cpayment_terms\']').focus()">
							<i class="glyphicon glyphicon-menu-down"></i></span>
						</div>
					
						<div class="form-inline">
    						<label class="control-label col-form-label col-sm-1">Days</label>
    	   					<input name="PMENT_DAYS" type="TEXT" value="" class="form-control" size="5" MAXLENGTH=10 readonly>
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
							
														 <div class="tab-pane fade" id="user_c">
        <br>
        
        <div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table user-table">
						<thead>
							<tr>
								<th>User Name</th>
								<th>User Phone No</th>
								<th>User Email</th>
								<th>User Id</th>
								<th>Password</th>
								<th>Customer App Manager Access</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center">
									<input type="hidden" name="customeruserid" value="0">
									<input class="form-control text-left" name="USER_NAME" type="text" placeholder="Enter User Name" maxlength="50" autocomplete="off" ></td>
								<td class="text-center">
									<input class="form-control text-left" name="USER_HPNO" type="text" placeholder="Enter User Phone No" maxlength="30" onkeypress="return isNumber(event)" autocomplete="off" >
								</td>
								<td class="text-center">
									<input class="form-control text-left" name="USER_EMAIL" type="text" placeholder="Enter User Email" maxlength="50" autocomplete="off" >
								</td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="USER_ID" placeholder="Enter User id" maxlength="100" autocomplete="off"  onchange="checkuser(this.value)">
								</td>								
								<td class="text-center grey-bg" style="position:relative;">
									<input  name="PASSWORD" class="form-control text-left" maxlength="60" placeholder="Enter Password" type="password" autocomplete="off">
									<span class="input-group-btn phideshow" onclick="javascript:viewpassword(this);return false;">
					            <button class="btn btn-default reveal" type="button" style="background: white;"><i class="fa fa-fw fa-eye" id="peye" style="color: #23527c;"></i></button>
					          	</span>	
								</td>
								<td class="text-center">
								    <input type="hidden" name="MANAGER_APP_VAL" value = "0"/>
									<input type="checkbox" name="MANAGER_APP" id="MANAGER_APP" value="1" onclick="checkManagerApp(this)" />&nbsp;&nbsp;
								</td>
							
							</tr>
						</tbody>
					</table>
			</div>
			<div class="form-group">
					<div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;margin-left: 5%;"
								onclick="addUserRow()">+ Add another User Detail</a>
						</div>
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
					<INPUT name="BANKNAME" type = "TEXT" value="" id=BANKNAMECUS class="form-control" onchange="checkcustomerbank(this.value)" value="" placeholder="BANKNAME">
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
%>
<input type="hidden" id="plant_desc" value="<%=PLNTDESC %>" />
<input type="hidden" id="template_subject" value="<%=template_subject %>" />
<input type="hidden" id="template_body" value="<%=template_body %>" />
<input type="hidden" id="customer_email" value = '' />
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

function customertypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#CUSTOMER_TYPE_C").typeahead('val', data.CUSTOMER_TYPE_DESC);
		$("input[name=CUSTOMER_TYPE_ID]").val(data.CUSTOMER_TYPE_ID);
	}
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
	 
    <!-- Author Name:Resviya ,Date:16/07/21 , Description: For Currency -->
var  curr = document.form1.CURRENCYID.value;
var basecurrency = '<%=currency%>';   
document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";
document.getElementById('AODTYPE').innerHTML = "Add On (% / "+curr+")";

       <!-- End -->

       	document.getElementById('lbltotal').innerHTML = "Total ("+curr+")";//  Author: Azees  Create date: July 16,2021  Description:  Total of Local Currency
	    document.getElementById('showtotalcur').style.display = 'none';
       
     //Resvi Add For Discount Currency
   	$('#item_discounttype').empty();
   	$('#item_discounttype').append('<option value="<%=currency%>"><%=currency%></option>');
   	$('#item_discounttype').append('<option value="%">%</option>');


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

  //Resvi Add for User Row removal-27.10.2021
   	$(".user-table tbody").on('click', '.user-action', function() {
   		$(this).parent().parent().remove();
   	});
   	//Ends


		/* Payment Terms Auto Suggestion */
		$('#cpayment_terms').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  name: 'states',
			  display: 'PAYMENT_TERMS',  
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/PaymentTermsServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : "<%=plant%>",
						ACTION : "GET_PAYMENT_TERMS_DETAILS",
						TERMS : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.terms);
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
			    return '<p>' + data.PAYMENT_TERMS + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="payTermsAddBtn footer"  data-toggle="modal" data-target="#payTermsModal"><a href="#"> + Add Payment Terms</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			}).on('typeahead:select',function(event,selection){
				$("input[name=PMENT_DAYS]").val(selection.NO_DAYS);
			});
		/* Payment Mode Auto Suggestion */
		$("#CUST_PAYTERMS").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{	  
			  display: 'PAYMENTMODE',  
			  source: function (query, process,asyncProcess) {
					var urlStr = "/track/PaymentModeMst";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : "<%=plant%>",
						action : "GET_PAYMENT_MODE_LIST",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.PAYMENTMODE);
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
					return '<p>' + data.PAYMENTMODE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal"><a href="#"> + New Payment Mode</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();	  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});

		/* Customer Type Auto Suggestion */
		$('#CUSTOMER_TYPE_C').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'CUSTOMER_TYPE_DESC',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT :  "<%=plant%>",
					ACTION : "GET_CUSTOMER_TYPE_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.CUSTOMER_TYPE_MST);
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
					return '<div onclick="document.form.CUSTOMER_TYPE_ID.value = \''+data.CUSTOMER_TYPE_ID+'\'"><p class="item-suggestion">' + data.CUSTOMER_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.CUSTOMER_TYPE_DESC+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="customertypecAddBtn footer"  data-toggle="modal" data-target="#CustTypeModal"> <a href="#"> + Add Customer Group</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				$('.customertypecAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.customertypecAddBtn').hide();}, 180);
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == "")
					document.form.CUSTOMER_TYPE_IDC.value = "";
			});
	    
     //transport
	$('#transports').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TRANSPORT_MODE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
						ACTION : "GET_TRANSPORT_LIST",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.TRANSPORTMODE);
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
		    return '<p>' + data.TRANSPORT_MODE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="transportAddBtn footer"  data-toggle="modal" data-target="#transportModal"><a href="#"> + New Transport </a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.transportAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.transportAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			$("input[name=TRANSPORTSID]").val(selection.ID);
		});

	
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

function checkManagerApp(obj){
	var manageapp = $(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val();
	if(manageapp == 0)
	$(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val(1);
	else
	$(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val(0);
	
}

function viewpassword(obj){
	  var trid = $(obj).closest('tr').find("td:nth-child(5)").find('input[name=PASSWORD]').attr('type');
	  if (trid == 'password') {
		  $(obj).closest('tr').find("td:nth-child(5)").find('input[name=PASSWORD]').attr('type', 'text');
		  $(obj).closest('tr').find("td:nth-child(5)").find('i').attr('class', 'fa fa-fw fa-eye-slash');
    } else {
  	  $(obj).closest('tr').find("td:nth-child(5)").find('input[name=PASSWORD]').attr('type', 'password');
  	  $(obj).closest('tr').find("td:nth-child(5)").find('i').attr('class', 'fa fa-fw fa-eye');
    }
}


</script>
<jsp:include page="newBankModal.jsp" flush="true"></jsp:include> 
<jsp:include page="newProductDepTypeModal.jsp" flush="true"></jsp:include> <!-- Resvi for add product department -->
<jsp:include page="newProductCategoryModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product category -->
<jsp:include page="newProductSubCatgoryModal.jsp" flush="true"></jsp:include><!-- Thanzith for add product Sub Category -->
<jsp:include page="newProductBrandModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product Brand -->
<jsp:include page="newProductHsCodeModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product HsCode -->
<jsp:include page="newProductCOOModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product COO -->
<jsp:include page="newProductUOMmodal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product UOM -->
<jsp:include page="newCustomerTypeModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add customertype -->
<jsp:include page="newPaymentModeModal.jsp" flush="true"></jsp:include> <!-- thansith Add for paymentmode -->
<jsp:include page="newTransportModeModal.jsp" flush="true"></jsp:include> <!-- imti for add transport -->
<jsp:include page="newPaymentTermsModal.jsp" flush="true"></jsp:include> <!-- imti for add paymentterms -->


<jsp:include page="footer2.jsp" flush="true">
<jsp:param name="title" value="<%=title%>" />
</jsp:include>
