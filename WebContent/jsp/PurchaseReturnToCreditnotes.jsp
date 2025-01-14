<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.db.object.FinProject"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@page import="com.track.db.object.PoHdr"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "New Debit Note";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
PlantMstDAO plantMstDAO = new PlantMstDAO();
CurrencyDAO currencyDAO = new CurrencyDAO();
DateUtils _dateUtils = new DateUtils();
ReturnOrderDAO returnOrderDAO = new ReturnOrderDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String curDate =_dateUtils.getDate();
String pono = StrUtils.fString(request.getParameter("PONO"));
String vendno = StrUtils.fString(request.getParameter("VENDNO"));
String vendName = StrUtils.fString(request.getParameter("VEND_NAME"));
String grno = StrUtils.fString(request.getParameter("GRNO"));
String action = StrUtils.fString(request.getParameter("action"));
String poreturnno = StrUtils.fString(request.getParameter("PORETURN"));
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String region = StrUtils.fString((String) session.getAttribute("REGION"));
String taxbylabel = ub.getTaxByLable(plant);
String sVendorCode = StrUtils.fString(request.getParameter("sCustCode"));
String sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT"));
SupplierCreditUtil SCUtil = new SupplierCreditUtil();
SupplierCreditDAO crdnotedao= new SupplierCreditDAO();
String creditnote="",creditDate="", payTerms = "",empno="",empName="",billnumber="",grnumber ="",creditstatus="",sREVERSECHARGE="",sGOODSIMPORT="",
discount = "", dicountType = "", discountAccount = "", shippingCharge = "0", adjLabel = "", adjustment = "0", notes = "",item_rate="",purchaseloc="",stateprefix="",btname="Save";
String CURRENCYUSEQT="0",DISPLAY="",DISPLAYID="",projectid="",projectname="",shippingid="",shippingcust="";
String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
shipworkphone="",shipcountry="",shiphpno="",shipemail="";
int pid = 0;
PoHdrDAO poHdrDAO= new PoHdrDAO();
PoHdr poHdr=new PoHdr();
PoHdr poheader = poHdrDAO.getPoHdrByPono(plant, pono);

if(action.equalsIgnoreCase("view")){
	fieldDesc=StrUtils.fString((String)request.getSession().getAttribute("RESULTINBRECEIVE"));
    fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
    request.getSession().setAttribute("RESULTINBRECEIVE","");
    
    	List poreturnList =  returnOrderDAO.getPOReturnDetailsbyprnoandcost(poreturnno,pono,grno,plant);
    	Map poreturnHdr=(Map)poreturnList.get(0);
    	
    	pono = (String)poreturnHdr.get("PONO");
    	if(vendno.equalsIgnoreCase(""))
    	{
    		vendno = (String)poreturnHdr.get("VENDNO");
    		vendName = (String)poreturnHdr.get("VNAME");
    	}
   		shippingid= (String)poreturnHdr.get("SHIPPINGID");
       	shippingcust= (String)poreturnHdr.get("SHIPPINGCUSTOMER");
       	sTAXTREATMENT= (String)poreturnHdr.get("TAXTREATMENT"); //RESVI
    	billnumber = (String)poreturnHdr.get("BILL");
    	grnumber = (String)poreturnHdr.get("GRNO");
    	notes = (String)poreturnHdr.get("NOTE");
    	
    	if(billnumber.length()>0){
    		
    	}else{
    		shippingid= (String)poheader.getSHIPPINGID();
    		shippingcust= (String)poheader.getSHIPPINGCUSTOMER();
    		sTAXTREATMENT= (String)poheader.getTAXTREATMENT();
    		payTerms= (String)poheader.getPAYMENT_TERMS();
    		empno= (String)poheader.getEMPNO();
    		empName = new EmployeeDAO().getEmpname(plant, poheader.getEMPNO(), "");
    		pid = Integer.valueOf(poheader.getPROJECTID());
    		shipcontactname=(String)poheader.getSHIPCONTACTNAME();
    		shipdesgination=(String)poheader.getSHIPDESGINATION();
    		shipaddr1=(String)poheader.getSHIPADDR1();
    		shipaddr2=(String)poheader.getSHIPADDR2();
    		shipaddr3=(String)poheader.getSHIPADDR3();
    		shipaddr4=(String)poheader.getSHIPADDR4();
    		shipstate=(String)poheader.getSHIPSTATE();
    		shipzip=(String)poheader.getSHIPZIP();
    		shipworkphone=(String)poheader.getSHIPWORKPHONE();
    		shipcountry=(String)poheader.getSHIPCOUNTRY();
    		shiphpno=(String)poheader.getSHIPHPNO();
    		shipemail=(String)poheader.getSHIPEMAIL();
    	}
    
    	
	    FinProjectDAO finProjectDAO = new FinProjectDAO();
		FinProject finProject=new FinProject();
    	if(pid > 0){
    		finProject = finProjectDAO.getFinProjectById(plant, pid);
    		projectname = finProject.getPROJECT_NAME();
    	}else{
    		projectid = "";
    	}
		
    	taxbylabel = ub.getTaxByLable(plant);
    	sVendorCode = StrUtils.fString(vendno);
//     	sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT"));
    	sREVERSECHARGE = StrUtils.fString(request.getParameter("REVERSECHARGE"));
    	sGOODSIMPORT = StrUtils.fString(request.getParameter("GOODSIMPORT"));
    	curency = (String)poreturnHdr.get("CURRENCYID");
		CURRENCYUSEQT = (String)poreturnHdr.get("CURRENCYUSEQT");
    	
}

List curQryList=new ArrayList();
curQryList = currencyDAO.getCurrencyDetails(curency,plant);
for(int i =0; i<curQryList.size(); i++) {
	ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
	DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
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
curQryList = currencyDAO.getCurrencyDetails(curency,plant);
String Supcurrency=plantMstDAO.getBaseCurrency(plant);
for(int i =0; i<curQryList.size(); i++) {
	ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
	DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
	DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
    CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
    }
adjLabel = "Adjustment";
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry

CustUtil custUtil = new CustUtil();
ArrayList arrCust = custUtil.getVendorDetails(poheader.getCustName(),
		plant);
String sAddr1 = (String) arrCust.get(2);
String sAddr2 = (String) arrCust.get(3);
String sAddr3 = (String) arrCust.get(4);
String sAddr4 = (String) arrCust.get(15);
String sCountry = (String) arrCust.get(5);
String sZip = (String) arrCust.get(6);
String	sDesgination = (String) arrCust.get(9);
String sTelNo = (String) arrCust.get(10);
String sHpNo = (String) arrCust.get(11);
String sEmail = (String) arrCust.get(12);
String sFax = (String) arrCust.get(13);
String sState = (String) arrCust.get(19);
String sWorkphone = (String) arrCust.get(29);

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	 <jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	 <jsp:param name="submenu" value="<%=IConstants.PURCHASE_CREDIT_NOTES%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/PurchaseReturnToCreditnotes.js"></script>
<style>
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
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box"> 
	<ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../purchasereturn/summary"><span class="underline-on-hover">Purchase Order Return Summary</span> </a></li>
                <li><a href="../purchasereturn/detail?PONO=<%=pono%>&GRNO=<%=grno%>&PORETURN=<%=poreturnno%>"><span class="underline-on-hover">Purchase Order Return Detail</span> </a></li>
                <li><label>New Debit Note</label></li>                                   
            </ul> 
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../purchasereturn/detail?PONO=<%=pono%>&GRNO=<%=grno%>&PORETURN=<%=poreturnno%>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>

	<div class="container-fluid">
	 <input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>	 
	<form id="createBillForm" class="form-horizontal" name="form" action="/track/SupplierCreditServlet?Submit=Save"  method="post" enctype="multipart/form-data" onsubmit="return validateBill()">
	<input type="text" name="plant" value="<%=plant%>" hidden>
	<input type="text" name="poreturnstatus" value="poreturn" hidden>
	<input type="text" name="username" value=<%=username%> hidden>
	<input type="text" name="vendno" value="<%=vendno%>" hidden>
	<input type="text" name="curency" value="<%=curency%>" hidden>
	<input type="text" name="poreturn" value="<%=poreturnno%>" hidden>	
	<input type="text" name="cstatus" value="<%=creditstatus%>" hidden>	
	<INPUT type="hidden" name="STATE_PREFIX" value="<%=stateprefix %>" />
	<INPUT type="hidden" name="CURRENCYID"  value="<%=curency%>">
	<INPUT type="hidden" name="INV_STATE_LOC" value="" /> 
	<INPUT type="hidden" name="TAXTREATMENT_VALUE" value="<%=sTAXTREATMENT %>" />
	<INPUT type="hidden" name="NOFO_DEC"  value="<%=numberOfDecimal%>">
	<input type="hidden" name="PROJECTID" value="<%=projectid%>">
	<input type="hidden" name="SHIPPINGID" value="<%=shippingid%>">
	<input type="hidden" name="STATE" value="">
	
					<input type = "hidden" name="odiscounttaxstatus" value="0">
					<input type = "hidden" name="discounttaxstatus" value="0">
					<input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
					<input type = "hidden" name="ptaxtype" value="">
					<input type = "hidden" name="ptaxpercentage" value="0">
					<input type = "hidden" name="ptaxdisplay" value="">
					<input type = "hidden" name="ptaxiszero" value="1">
					<input type = "hidden" name="ptaxisshow" value="0">
					<input type = "hidden" name="taxid" value="0">
					<input type = "hidden" name="SHIPCONTACTNAME" value="<%=poheader.getSHIPCONTACTNAME()%>">
				<input type = "hidden" name="SHIPDESGINATION" value="<%=poheader.getSHIPDESGINATION()%>">
				<input type = "hidden" name="SHIPADDR1" value="<%=poheader.getSHIPADDR1()%>">
				<input type = "hidden" name="SHIPADDR2" value="<%=poheader.getSHIPADDR2()%>">
				<input type = "hidden" name="SHIPADDR3" value="<%=poheader.getSHIPADDR3()%>">
				<input type = "hidden" name="SHIPADDR4" value="<%=poheader.getSHIPADDR4()%>">
				<input type = "hidden" name="SHIPSTATE" value="<%=poheader.getSHIPSTATE()%>">
				<input type = "hidden" name="SHIPZIP" value="<%=poheader.getSHIPZIP()%>">
				<input type = "hidden" name="SHIPWORKPHONE" value="<%=poheader.getSHIPWORKPHONE()%>">
				<input type = "hidden" name="SHIPCOUNTRY" value="<%=poheader.getSHIPCOUNTRY()%>">
				<input type = "hidden" name="SHIPHPNO" value="<%=poheader.getSHIPHPNO()%>">
				<input type = "hidden" name="SHIPEMAIL" value="<%=poheader.getSHIPEMAIL()%>">
					<!-- <input type = "hidden" name="GST" value=""> -->
					<input type="hidden" name="CURRENCYUSEQTOLD" value="<%=CURRENCYUSEQT%>">
					<input type = "hidden" name="aorderdiscount" value="0">
					<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
	            	<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>    
					
					<%if(!ispuloc){ %>
						 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
					<%} %>
	
		<div class="form-group vendor-section">
			<label class="control-label col-form-label col-sm-2 required">Supplier Name</label>
			<div class="col-sm-6 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected  form-control typeahead" id="vendname" placeholder="Select a vendor" name="vendname" value="<%=vendName%>" disabled>
				</div>
				<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" onchange="OnTaxChange(this.value)" data-placement="right" id="nTAXTREATMENT" name="nTAXTREATMENT" value="<%=sTAXTREATMENT %>" style="width: 100%">
				<OPTION style="display:none;"></OPTION>
        <%
		   MasterUtil _MasterUtiln =new  MasterUtil();
		   ArrayList ccList1 =  _MasterUtiln.getTaxTreatmentList("",plant,"");
			for(int i=0 ; i<ccList1.size();i++)
      		 {
				Map m1=(Map)ccList1.get(i);
				String nTAXTREATMENT = (String)m1.get("TAXTREATMENT"); %>
		        <option  value='<%=nTAXTREATMENT%>' ><%=nTAXTREATMENT %> </option>		          
		        <%
      		}
			 %></SELECT>
			</div>
		</div>
		
		<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Shipping Address:</label>
					<div class="col-sm-6 ac-box">
								<input type="text" class="form-control typeahead" id="SHIPPINGCUSTOMER" name="SHIPPINGCUSTOMER" value="<%=shippingcust%>" readonly> 
								<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'SHIPPINGCUSTOMER\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> -->
								<!-- <span class="btn-danger input-group-addon"
								onclick="javascript:popUpWin('../jsp/shippingdetails_list.jsp?SHIPPINGCUSTOMER='+form.SHIPPINGCUSTOMER.value+'&FORMNAME=form');">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
					</div>
				</div>
				<div class="form-group shipCustomer-section">
					<div class="col-sm-2"></div>
					<div class="col-sm-4" style="line-height: 7px; font-size: 13px;" id="shipbilladd">
						<h5 style="font-weight: bold;">Billing Address</h5>
						<p><%=vendName %></p>
						<p><%=sAddr1%>  <%=sAddr2 %></p>
						<p><%=sAddr3 %>  <%=sAddr4%></p>
						<p><%=sState%></p> 
						<p><%=sCountry%> <%=sZip%></p>
						<p><%=sHpNo%></p>
						<p><%=sWorkphone%></p>
						<p><%=sEmail%></p>
						
						
					</div>
					<div class="col-sm-4" style="line-height: 7px; font-size: 13px;" id="shipadd">
						<h5 style="font-weight: bold;">
							Shipping Address <a><span data-toggle="modal"
								data-target="#shipaddr" onclick="getshipaddr(); style="font-size: 15px;font-weight: 450;">Change</span></a>
						</h5>
						<div id="cshipaddr">
							
						<p><%=shipcontactname%></p>
						<p><%=shipdesgination%></p>
						<p><%=shipaddr1 %> <%=shipaddr2 %></p>
						<p><%=shipaddr3 %> <%=shipaddr4 %></p>
						<p><%=shipstate %></p>
						<p><%=shipcountry %> <%=shipzip %></p>
						<p><%=shiphpno %></p>
						<p><%=shipworkphone %></p>
						<p><%=shipemail%></p>
						</div>
					</div>
					</div>
					<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
				<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4 ac-box">
								<input type="text" class="form-control typeahead" 
									id="project" placeholder="Select a project" name="project" value="<%=projectname%>" readonly>
							<!-- 	<span class="select-icon" onclick="$(this).parent().find('input[name=\'project\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>  -->
								<!-- <span class="btn-danger input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
					</div>
				</div>
				<%}else{%>
				<input type="hidden" name="project" value="">
					<% } %>
					
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Bill Number</label>
			
			<div class="col-sm-4">				
				<input type="text" class="form-control billno" id="billno" value="<%=billnumber %>" name="billno" readonly>
			</div>
			
		</div>	
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">GRN Number</label>
			
			<div class="col-sm-4">				
				<input type="text" class="form-control grnno" id="grnno" name="grnno" value="<%=grnumber %>" readonly>
			</div>
			
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Debit Note#</label>
			<div class="col-sm-4">
				<div class="input-group">   
		        		<input class="form-control" name="creditnote" id="creditnote" onchange="checkorderno(this.value)" onkeypress="return blockSpecialCharOrderNo(event)" type="text" 
		        			value="<%=creditnote%>" > 
	        			<span class="input-group-addon"  onClick="getNextCreditnote();">
				   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
				   		 		<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i>
				   		 	</a>
			   		 	</span>
		   		 	</div>
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Reference</label>
			
			<div class="col-sm-4">				
				<input type="text" class="form-control pono" id="pono" name="pono" value="<%=pono%>" maxlength ="50">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'pono\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
			
		</div>		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Debit Note Date</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" value="<%=curDate%>" id="bill_date" name="bill_date" READONLY>
			</div>
				<div class="col-sm-6 no-padding">
			<label class="control-label col-form-label col-sm-5">Payment Terms</label>
			<div class="col-sm-6">
				<input type="text" class="form-control" id="payment_terms" name="payment_terms" value="<%=payTerms%>" placeholder="Select Payment Terms" READONLY>
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		</div>
		
			<div class="form-group employee-section">
			<label class="control-label col-form-label col-sm-2">Employee</label>
			<div class="col-sm-4 ac-box">
				<input type="text" class="ac-selected form-control typeahead" id="EMP_NAME" name="EMP_NAME" onchange="checkemployeess(this.value)" value="<%=empName%>" placeholder="Select a Employee">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		
		<%if(ispuloc){ %>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Purchase Location</label>
			<div class="col-sm-4 ac-box">
				<input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" value="<%=purchaseloc %>" name="SALES_LOC" placeholder="Select a Purchase Location" disabled>
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		<%}%>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Currency</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control typeahead" id="CURRENCY" name="CURRENCY" placeholder="Select a Currency" value="<%=DISPLAY%>" disabled>
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-5 required" id="exchangerate" ></label>
						<div class="col-sm-6 ac-box">
							<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" onkeypress="return isNumberKey(event,this,4)" required>	
						</div>
					    </div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Standard <%=taxbylabelordermanagement%> :</label><!-- this is dynamic value -->
					<div class="col-sm-4">
			   		 	<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" value="" readonly>
						<span class="sideiconspan"><p>%</p></span>

					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Purchasetax" name="Purchasetax" onchange="checktax(this.value)"  placeholder="Select a Tax">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'Purchasetax\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
		<div class="form-group">
		<div class="col-sm-2">
        </div>
        <div class="col-sm-4" >
        <div id="CHK1">        
      	<input type = "checkbox" class="check" id = "REVERSECHARGE" name = "REVERSECHARGE" <%if(sREVERSECHARGE.equals("1")) {%>checked <%}%>/><b>&nbsp;This transaction is applicable for reverse charge </b>
      	</br>
      	<input type = "checkbox" class="check" id = "GOODSIMPORT" name = "GOODSIMPORT" <%if(sGOODSIMPORT.equals("1")) {%>checked <%}%>/><b>&nbsp;This transaction is applicable for goods import &nbsp; &nbsp; </b>
      	</div>
      	</div>
      	</div>
		<hr/>
		<div class="form-group">
			<div class="col-sm-12">
				<label class="control-label col-form-label">Product Rates Are</label>
				<div class="dropdown-noborder">
					<select class="ac-box dropdown-noborder form-control" onchange="renderTaxDetails()" name="item_rates" id="item_rates"  value="<%=item_rate%>">
						<option value="0">Tax Exclusive</option>
						<option value="1">Tax Inclusive</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row" style="margin:0px;">
			<table class="table table-bordered line-item-table bill-table">
			<thead>
			  <tr>
			  <%-- <%if(action.equalsIgnoreCase("view")){ %>
			  	<th>Select</th>
			  	<%} %> --%>
				<th class="bill-desc" colspan=2>Product Details</th>
				<th class="bill-acc">Account</th>
				<th class="item-qty text-right">Quantity</th>
				<th class="item-cost text-right">Unit Cost</th>
				<th class="item-discount">Product Discount</th>
<!-- 				<th class="item-tax">Tax</th>				 -->
				<th class="item-amount text-right">Amount</th>
			  </tr>
			</thead>
			<tbody>
			  <tr>
				<td class="item-img text-center">
				  <!-- <span class="glyphicon glyphicon-picture"></span>  -->
				  <img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">
				  <input type="hidden" name="tax_type" value="">
				  <input type="hidden" name="tax" class="taxSearch" value="">
<!-- 				  <input type="hidden" name="lnno" value="1"> -->
				</td>
				<td class="bill-item-dbt">
					<input type="text" name="item" class="form-control itemSearch" placeholder="Type or click to select an item." onchange="calculateAmount(this)">
				</td>
				<td class="bill-acc">
					<input type="text" name="account_name" class="form-control accountSearch" placeholder="Select Account">
				</td>
				<td class="item-qty text-right"><input name="qty" type="text" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>
				<td class="item-cost text-right"><input name="cost" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>
				<td class="item-discount text-right">
				<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
				</td>
<!-- 				<td class="item-tax"> -->
<!-- 					<input type="hidden" name="tax_type"> -->
<!-- 					<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" readonly>  -->
<!-- 				</td>				 -->
				<td class="item-amount text-right grey-bg">
				<input name="amount" type="text" class="form-control text-right" value="0.00" readonly="readonly">
				<input name="landedCost" type="text" value="0.0" hidden>
				</td>
			  </tr>
			</tbody>
			</table>
		</div>
		<div class="row">
			<div class="col-sm-6">
			
			</div>
			<div class="total-section col-sm-6">
				<div class="total-row sub-total">
					<div class="total-label"> Sub Total <br> 
						<span class="productRate" hidden>(Tax Inclusive)</span> 
					</div> 
					<div class="total-amount" id="subTotal">0.00</div>
				</div>	
				<div class="total-row">
							<div class="total-label">
								<div class="row">
									<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Order Discount</div>
										<sapn id="odtax">(Tax Exclusive)</sapn>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
										<input type="checkbox" class="form-check-input isodisctax" name="isodisctax" Onclick="isodisctaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right oddiscount" id="oddiscount" type="text" value="0.00" readonly
												name="orderdiscount" onchange="renderTaxDetails()">
											<!-- <span class="input-group-addon">%</span> -->
											<select class="discountPicker form-control" name="oddiscount_type" id="oddiscount_type" disabled="true">
												<option value="%">%</option>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="total-amount" style="line-height: 2;">
								<span id="orderdiscount"> 0.00 </span>
							</div>
						</div>						
				<div class="total-row"> 
					<div class="total-label">
						<div class="row">
							<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Discount</div>
										<sapn id="dtax">(Tax Exclusive)</sapn>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isdtax" name="isdtax" Onclick="isdtaxing(this)">
									</div>
							<div class=" col-lg-6 col-sm-6 col-6">
								<div class="input-group my-group"> 
									<input class="form-control text-right" type="text" name="discount" readonly onchange="renderTaxDetails()" value="<%=discount%>" >
									
									<select class="discountPicker form-control discount_type" name="discount_type" onchange="renderTaxDetails()" value="<%=dicountType%>">
										<option value="<%=curency%>"><%=curency%></option>
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
				<div class="total-row hightlight discountAccountSection" style="margin-top: 0px;" hidden>
					<div class="total-label supporting-acc-field" style="padding-bottom: 0;">
						<div class="row">
							<div class="col-lg-5 col-sm-5 col-5">
								<div class="form-control-plaintext required">Discount Account</div>
							</div> 
							<div class="col-lg-6 col-sm-6 col-6"> 
								<input class="form-control discountAccountSearch" type="text" value="<%=discountAccount%>" 
	  											name="discount_account" placeholder="Select Account">
							</div>
						</div>
					</div>
				</div>
				<div class="taxDetails">
				</div>
				<div class="total-row" Style="display: none;">
					<div class="badge-editable total-label">
						<div class="row">
							<div class="col-lg-5 col-sm-5 col-5"> 
								<div class="form-control-plaintext"> Shipping Charge </div>
							</div> 
							<div class="col-lg-6 col-sm-6 col-6"> 
								<input class="form-control text-right ember-view" type="text" name="shippingcost" value="<%=shippingCharge%>"> 
							</div>  
						</div>
					</div>
					<div class="total-amount" id="shipping"> 0.00 </div>
				</div>				
				<div class="total-row">
					<div class="badge-editable total-label">
						<div class="row">
							<div class="col-lg-5 col-sm-5 col-5"> 
								<input type="text" class="form-control" name="adjustmentLabel" value ="<%=adjLabel%>">
							</div> 
							<div class="col-lg-6 col-sm-6 col-6"> 
								<input class="form-control text-right ember-view" type="text" name="adjustment" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)" value = "<%=adjustment%>"> 
							</div>  
						</div>
					</div> 
					<div class="total-amount " style="line-height: 2" id="adjustment"> 0.00 </div>
				</div>
				
				<div class="total-section total-row">
							<div class="gross-total-removemargin"><!--  Author: Azees  Create date: July 19,2021  Description: Total of Local Currency-->
								<div class="total-label">Total (<%=curency%>)</div>
								<div class="total-amount" id="total">0.00</div>
							</div>
						</div>
						
				<div class="total-section total-row" id="showtotalcur">
					<div class="gross-total">
						<div class="total-label"> Total (<%=Supcurrency%>) </div> 
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
						<input type="file" class="form-control input-attch" id="billAttch" name="file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
				<div id="billAttchNote">
					<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
				</div>
			</div>
			<div class="col-sm-6 notes-sec">
				<p>Notes <span class="text-muted">(For Internal Use)</span></p>
				<div> <textarea rows="2" name="note" class="ember-text-area form-control ember-view" maxlength ="950"></textarea> </div>
			</div>
		</div>
		<input id="sub_total" name="sub_total" value="" hidden>
		<input id="total_amount" name="total_amount" value="" hidden>
		<input name="Submit" value="Save" hidden>
		<input name="bill_status" value="" hidden>
		<input name="oShippingcost" value="" hidden>
		<input name="taxTotal" value="0.0" hidden>
		<input name="totalAdditionalExpenses" value="0.0" hidden>
		
		<div class="row">
			<div class="col-sm-12 txn-buttons">
				<button id="btnBillDraft" type="button" class="btn btn-default">Save as Draft</button>
				<button id="btnBillOpen" type="button" class="btn btn-success">Save as Open</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			</div>
		</div>
	</form>
	<form  class="form-horizontal" name="form1" >
	<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
	</form>
</div>
	<!-- Modal -->
	
	
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
								ArrayList ccList1sh = _MasterUtil1.getCountryList("", plant, region);
								for (int i = 0; i < ccList1sh.size(); i++) {
									Map m = (Map) ccList1sh.get(i);
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

	<%@include file="newProductModal.jsp" %>
	<%-- <%@include file="NewChartOfAccountAdd.jsp"%> --%>
	<%@include file="NewChartOfAccountpopup.jsp"%>
	<%@include file="newGstDetailModal.jsp" %>
	<%@include file="newPaymentTermsModal.jsp" %>
	<%@include file="newShipmentModal.jsp" %>
	<jsp:include page="newEmployeeModal.jsp" ></jsp:include>
	<!-- Modal -->
	<div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog">	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h3 class="modal-title">Modal Header</h3>
	      </div>
	      <div class="modal-body">
	        <p>Some text in the modal.</p>
	      </div>
	      <div class="modal-footer">
	      		<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	  </div>
	</div>
	</div>
</div>

<!-- ----------------Modal-------------------------- -->



<script type="text/javascript">

$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip(); 
	 
    <!-- Author Name:Resviya ,Date:20/07/21 , Description: For Currency -->
    var  curr = document.form.CURRENCYID.value;
var basecurrency = '<%=Supcurrency%>';   
document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";

if(basecurrency!=curr)//  Author: Resvi  Create date: July 28,2021  Description:  Total of Local Currency
	document.getElementById('showtotalcur').style.display = 'block';	
else
	document.getElementById('showtotalcur').style.display = 'none';  

	var TAXTREATMENT_VALUE = $("input[name ='TAXTREATMENT_VALUE']").val();
	if(TAXTREATMENT_VALUE!="")
	{
		document.getElementById('nTAXTREATMENT').value = TAXTREATMENT_VALUE;
		ntaxtreat = TAXTREATMENT_VALUE;
	}
	
	document.getElementById('CHK1').style.display = 'none';
    var ntaxtreat = document.getElementById('nTAXTREATMENT').value;
    if(ntaxtreat =="GCC VAT Registered"||ntaxtreat=="GCC NON VAT Registered"||ntaxtreat=="NON GCC")
    {
    	document.getElementById('CHK1').style.display = 'block';
    }

    $('.check').click(function() {
        $('.check').not(this).prop('checked', false);
    });
	
	/* var nPONO = document.getElementById('pono').value;
	if(nPONO!="")
		$('#CHK1').css('pointer-events','none');
	else
		$('#CHK1').css('pointer-events',''); */
	
    $('[data-toggle="tooltip"]').tooltip();

    /* var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.";	 */
    
 	var cust = "<%=sVendorCode%>";
    
    if(cust=="" || cust.length==0 ) {
    }else{
    	
    	$("input[name ='CUST_CODE']").val(cust);
    	$("input[name ='CUST_CODE1']").val(cust);
    	 $('#supplierModal').modal('show');
    }
    
});
function checkitem(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Supplier ID!");
		document.getElementById("CUST_CODE").focus();
		return false;
	 }else{ 
			var urlStr = "/track/MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					CUST_CODE : aCustCode,
	                USERID : "<%=username%>",
					PLANT : "<%=plant%>",
					ACTION : "SUPPLIER_CHECK"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
	                               
							alert("Supplier Already Exists");
							document.getElementById("CUST_CODE").focus();
							//document.getElementById("ITEM").value="";
							return false;
						}
						else
							return true;
					}
	});	
			return true;
	}
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
			$('#SHIP_STATE').append('<OPTION style="display:none;">Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					 if(document.form.STATE.value==value.text)
						 	$('#SHIP_STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
					 else
					   $('#SHIP_STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
		}
	});	
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




function create_account() {
	
	if ($('#create_account_modal #acc_type').val() == "") {
		alert("please fill account type");
		$('#create_account_modal #acc_type').focus();
		return false;
	}
	
	if ($('#create_account_modal #acc_det_type').val() == "") {
		alert("please fill account detail type");
		$('#create_account_modal #acc_det_type').focus();
		return false;
	}
	
	if ($('#create_account_modal #acc_name').val() == "") {
		alert("please fill account name");
		$('#create_account_modal #acc_name').focus();
		return false;
	}
	
	if(document.create_form.acc_is_sub.checked)
	{
		if ($('#create_account_modal #acc_subAcct').val() == "") {
			alert("please fill sub account");
			$('#create_account_modal #acc_subAcct').focus();
			return false;
		}
		else
			{
			 var parType=$('#create_account_modal #acc_det_type').val();
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
	if ($('#create_account_modal #acc_balance').val() != "") {
		if ($('#create_account_modal #acc_balance').val() != "0") {
		if ($('#create_account_modal #acc_balanceDate').val() == "") {
		alert("please fill date");
		$('#create_account_modal #acc_balanceDate').focus();
		return false;
		}
		}
	}
	
	var formData = $('form[name="create_form"]').serialize();
	$.ajax({
		type : 'post',
		url : "/track/ChartOfAccountServlet?action=create",
		dataType : 'json',
		data : formData,//{key:val}
		success : function(data) {
			if (data.STATUS == "FAIL") {		                               
				alert(data.MESSAGE);
			}else{
				$("input[name ='discount_account']").val(data.ACCOUNT_NAME);
				$('#create_account_modal').modal('toggle');
				
			}
		},
		error : function(data) {

			alert(data.responseText);
		}
	});
	return false;
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
	
$(".bill-table tbody").on('click','.accrmv',function(){
	debugger;	    
    var obj = $(this);
    var timestamp = new Date().getUTCMilliseconds();
    kayid = "key"+timestamp;
    $(obj).closest('td').attr('id', kayid); 
    $("input[name ='ukey']").val(kayid);
});
</script>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>

