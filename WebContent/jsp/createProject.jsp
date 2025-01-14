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
	String title = "Create Project";
	String isAutoGenerate = "false";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String ISPEPPOL = plantMstDAO.getisPeppol(plant);
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
	String transports = StrUtils.fString(request.getParameter("TRANSPORTID"));
	String customertypec = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_IDC"));
	String numberOfDecimal = (String)request.getAttribute("NumberOfDecimal");
	String deldate = (String)request.getAttribute("DelDate");
	String expdate = (String)request.getAttribute("ExpiryDate");
	String collectionTime = (String)request.getAttribute("CollectionTime");
	String currency = (String)request.getAttribute("Currency"), zeroval = StrUtils.addZeroes(0.0, numberOfDecimal);
	String zerovalper = StrUtils.addZeroes(Float.parseFloat("0"), "3");
	String gst = (String)request.getAttribute("GST");
	String taxbylabel = (String)request.getAttribute("Taxbylabel");
	String region = (String)request.getAttribute("Region");
	String msg = (String)request.getAttribute("Msg");
	
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
	
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PROJECT%>"/>
    <jsp:param name="submenu" value="<%=IConstants.PROJECT%>"/>
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
.phideshow{
    float: right;
    margin-top: -34px;
    position: relative;
    z-index: 2;
    margin-right: 40px;
}
 </style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/Project.js"></script>
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
                <li><a href="../project/summary"><span class="underline-on-hover">Project Summary</span> </a></li>
                <li><label>Create Project</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../project/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" id="salesOrderForm" name="form1" method="post" 
			action="../project/new" enctype="multipart/form-data" onsubmit="return validateSalesOrder()">
			
			<input type = "hidden" name="plant" value="<%=plant%>">
			<input type = "hidden" name="PROJECTID" value="">
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
				<INPUT type="hidden" name="CURRENCYID"  value="<%=DISPLAYID%>">	
				<%if(!ispuloc){ %>
				 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
				<%} %>
			
				<div class="form-group customer-section">
					<label class="control-label col-form-label col-sm-2 required">Customer:</label>
					<div class="col-sm-6 ac-box">
						<div class="input-group">
							<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" onclick="removetaxtrestl()"
							placeholder="Select a customer" name="CUST_NAME" onchange="checkcustomer(this.value)" value=""  MAXLENGTH="50"> 
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUST_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span> 
							<!-- <span class="btn-danger input-group-addon"
								onClick="javascript:popUpWin('../jsp/customer_list_order.jsp?CUST_NAME='+form1.CUST_NAME.value);">
								<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
							</span> -->
			   		 	</div>
					</div>
				</div>

		
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Project Number:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="PRNO" name="PRNO" onkeypress="return blockSpecialCharOrderNo(event)" MAXLENGTH="50">
							<span class="input-group-addon" id="autoGen">
					   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
					   		 		<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i>
					   		 	</a>
				   		 	</span>
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Project Name:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="PR_NAME" name="PR_NAME" 
							placeholder="Max 100 Characters" maxlength="100">
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Project Date:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control datepicker" id="DELDATE" 
								name="DELDATE" value="<%=deldate%>" readonly>
						</div>
					</div>
				</div>
				
					<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Expiry Date:</label>
					<div class="col-sm-4">
						<div class="input-group">
						<input type="text" class="form-control datepicker" id="EXPIRYDATE" 
								name="EXPIRYDATE"  readonly>
							
						</div>
					</div>
				</div>
				
			
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Estimate Cost :</label><!-- this is dynamic value -->
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="PRESTCOST" name="PRESTCOST"  onkeypress="return isNumberKey(event,this,4)" value="0.00">	
							
			   		 	</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2"></label>
			 		<div class="col-sm-4">
			  		<label class="radio-inline">
			      	<input type="radio" name="MANDAYHOUR" type = "radio"  value="0"  id="manday" checked="checked" >Man-day</label>
			    	<label class="radio-inline">
			      	<input type="radio" name="MANDAYHOUR" type = "radio" value="1"  id = "hour" >Hour</label>
			     	</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Number of Man-day/Hour</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="DAYHOUR" name="DAYHOUR" onkeypress="return isNumberKey(event,this,4)" value="0">	
			   		 	</div>
					</div>
				</div>
				<hr />
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
						<p>
							Notes <span class="text-muted">(For Internal Use)</span>
						</p>
						<div>
							<textarea rows="2" name="Notes" id="Notes" class="ember-text-area form-control ember-view" 
								placeholder="Max 1000 characters"  maxlength="1000"></textarea>
						</div>

					
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 txn-buttons">
						<button id="btnSalesOpen" type="button" class="btn btn-success">Save</button>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">
							Cancel
						</button>
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
				<input type = "hidden" name="CUST_CODE" value="">
				<input type = "hidden" name="CUST_CODE1" value="">
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
				
				<input type = "hidden" name="custModal">
				<input type="hidden" name="STATE_PREFIX" value="<%=plantstatecode%>" />
			</form>
		</div>
		<!-- Modal -->
		<!-- Modal -->
		
	</div>
</div>

<!-- ----------------Modal-------------------------- -->
<%@include file="../jsp/newEmployeeModal.jsp"%>
<%@include file="../jsp/newPaymentTypeModal.jsp"%>
<%@include file="../jsp/newIncotermsModal.jsp"%>
<%@include file="../jsp/newGstDetailModal.jsp"%>


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
      <INPUT name="PEPPOL"  type = "checkbox" value="PEPPOL" id="PEPPOL" >
    </label>
      </div>
    </div>
     <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Peppol id">Peppol Id</label>
      	<div class="col-sm-4">  
    	<INPUT name="PEPPOL_ID" id="PEPPOL_ID" type="TEXT" value="" size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	<%}else{%>
    	<input type="hidden" name="PEPPOL" id="PEPPOL" value="">
					<input type="hidden" name="PEPPOL_ID" id="PEPPOL_ID" value="">
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
									 <input id="CUST_PAYTERMS" name="CUST_PAYTERMS" class="form-control" type="text" onchange="checkcustomerpaymenttype(this.value)" value=""  MAXLENGTH=100 placeholder="Select Payment Mode">
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
											MAXLENGTH=1000 class="form-control ">
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
  
<%@include file="../jsp/newBankModal.jsp"%>
<script>

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

$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip(); 



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
				menuElement.after( '<div class="customertypecAddBtn footer"  data-toggle="modal" data-target="#CustTypeModal"> <a href="#"> + Add Customer Type</a></div>');
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

<%@include file="../jsp/newPaymentModeModal.jsp"%><!-- thansith Add for paymentmode -->
<%@include file="../jsp/newTransportModeModal.jsp"%> <!-- imti for add transport -->
<%@include file="../jsp/newPaymentTermsModal.jsp"%> <!-- imti for add paymentterms -->
<%@include file="../jsp/newCustomerTypeModal.jsp"%> <!-- Thanzith for add customertype -->
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

