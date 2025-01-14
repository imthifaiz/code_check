<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.db.object.FinProject"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Edit Debit Note";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String region = StrUtils.fString((String) session.getAttribute("REGION"));
String customertypec = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_IDC"));
//Validate no.of Supplier
	CustomerBeanDAO venddao = new CustomerBeanDAO();
	CurrencyDAO currencyDAO = new CurrencyDAO();	

	String NOOFSUPPLIER=((String) session.getAttribute("NOOFSUPPLIER"));
	String SupValidNumber="";
	int nosupvalid =venddao.Vendorcount(plant);
	if(!NOOFSUPPLIER.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFSUPPLIER);
		if(nosupvalid>=convl)
		{
			SupValidNumber=NOOFSUPPLIER;
		}
	}
String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
PlantMstDAO plantMstDAO = new PlantMstDAO();
DateUtils _dateUtils = new DateUtils();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String curDate =_dateUtils.getDate();
String pono = StrUtils.fString(request.getParameter("PONO"));
String vendno = StrUtils.fString(request.getParameter("VENDNO"));
String vendName = StrUtils.fString(request.getParameter("VEND_NAME"));
String grno = StrUtils.fString(request.getParameter("GRNO"));
String action = StrUtils.fString(request.getParameter("action"));
String billHdrId = StrUtils.fString(request.getParameter("BILL_HDR"));
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String taxbylabel = ub.getTaxByLable(plant);
String sVendorCode = StrUtils.fString(request.getParameter("sCustCode"));
String sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT"));
String empname=StrUtils.fString(request.getParameter("EMP_NAME"));
String empno = StrUtils.fString(request.getParameter("EMPNO"));
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
String ISPEPPOL = plantMstDAO.getisPeppol(plant);
SupplierCreditUtil SCUtil = new SupplierCreditUtil();
SupplierCreditDAO crdnotedao= new SupplierCreditDAO();
String creditnote="",creditDate="", payTerms = "",billnumber="",grnumber ="",creditstatus="",sREVERSECHARGE="",sGOODSIMPORT="",orderdiscount="",
discount = "", dicountType = "", discountAccount = "", shippingCharge = "", adjLabel = "", adjustment = "", notes = "",item_rate="",purchaseloc="",stateprefix="",btname="Save";
String CURRENCYUSEQT="0",DISPLAY="",DISPLAYID="",oddisctaxstatus = "0",disctaxstatus="0",ptaxtype="",ptaxpercentage="0",ptaxdisplay="",ptaxiszero="1",
ptaxisshow="0",taxid="0",gst="0",aorderdiscount="0",orderdiscounttype="",projectid="",projectname="",shippingid="",shippingcust="";
String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
shipworkphone="",shipcountry="",shiphpno="",shipemail="";
if(action.equalsIgnoreCase("view")){
	fieldDesc=StrUtils.fString((String)request.getSession().getAttribute("RESULTINBRECEIVE"));
    fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
    request.getSession().setAttribute("RESULTINBRECEIVE","");
    
    Map billHdr = null;
    Map shipDetail = null;
    
    	Hashtable ht = new Hashtable();
    	ht.put("ID", billHdrId);
    	ht.put("PLANT", plant);
    	List billHdrList =  SCUtil.getHdrById(ht);
    	billHdr=(Map)billHdrList.get(0);
    	
    	pono = (String)billHdr.get("PONO");
    	if(vendno.equalsIgnoreCase(""))
    	{
    		vendno = (String)billHdr.get("VENDNO");
    		vendName = (String)billHdr.get("VNAME");
    	}
    	billnumber = (String)billHdr.get("BILL");
    	grnumber = (String)billHdr.get("GRNO");
    	creditnote = (String)billHdr.get("CREDITNOTE");
    	creditDate = (String)billHdr.get("CREDIT_DATE");
    	payTerms = (String)billHdr.get("PAYMENT_TERMS");
    
    	orderdiscount = StrUtils.addZeroes(Double.parseDouble((String)billHdr.get("ORDER_DISCOUNT")), "3");
    	
    	dicountType = (String)billHdr.get("DISCOUNT_TYPE");
    	if(dicountType.equalsIgnoreCase("%")){
    		discount = StrUtils.addZeroes(Double.parseDouble((String)billHdr.get("DISCOUNT")), "3");
    	}else{
    		discount = StrUtils.addZeroes((Float.parseFloat((String)billHdr.get("DISCOUNT"))*Float.parseFloat((String)billHdr.get("CURRENCYUSEQT"))), numberOfDecimal);
    	}
    	adjustment=	StrUtils.addZeroes((Float.parseFloat((String)billHdr.get("ADJUSTMENT"))*Float.parseFloat((String)billHdr.get("CURRENCYUSEQT"))), numberOfDecimal);
    	
    	discountAccount = (String)billHdr.get("DISCOUNT_ACCOUNT");
    	shippingCharge = (String)billHdr.get("SHIPPINGCOST");
    	adjLabel = (String)billHdr.get("ADJUSTMENT_LABEL");

    	empno = (String)billHdr.get("EMPNO");

    	empname = (String)billHdr.get("EMP_NAME");
    	notes = (String)billHdr.get("NOTE");
    	item_rate = (String)billHdr.get("ITEM_RATE");
    	purchaseloc=(String)billHdr.get("PURCHASE_LOCATION");
    	creditstatus=(String)billHdr.get("CREDIT_STATUS");
    	if(sTAXTREATMENT.equalsIgnoreCase(""))
    		sTAXTREATMENT=(String)billHdr.get("TAXTREATMENT");
    	sREVERSECHARGE = (String)billHdr.get("REVERSECHARGE");
		sGOODSIMPORT = (String)billHdr.get("GOODSIMPORT");
		curency = (String)billHdr.get("CURRENCYID");
		DISPLAY = (String)billHdr.get("DISPLAY");
		CURRENCYUSEQT = (String)billHdr.get("CURRENCYUSEQT");
		shipcontactname=(String)billHdr.get("SHIPCONTACTNAME");
		shipdesgination=(String)billHdr.get("SHIPDESGINATION");
		shipaddr1=(String)billHdr.get("SHIPADDR1");
		shipaddr2=(String)billHdr.get("SHIPADDR2");
		shipaddr3=(String)billHdr.get("SHIPADDR3");
		shipaddr4=(String)billHdr.get("SHIPADDR4");
		shipstate=(String)billHdr.get("SHIPSTATE");
		shipzip=(String)billHdr.get("SHIPZIP");
		shipworkphone=(String)billHdr.get("SHIPWORKPHONE");
		shipcountry=(String)billHdr.get("SHIPCOUNTRY");
		shiphpno=(String)billHdr.get("SHIPHPNO");
		shipemail=(String)billHdr.get("SHIPEMAIL");
		
		oddisctaxstatus = (String)billHdr.get("ISORDERDISCOUNTTAX");
		disctaxstatus= (String)billHdr.get("ISDISCOUNTTAX");
		ptaxpercentage= (String)billHdr.get("INBOUND_GST");
		taxid= (String)billHdr.get("TAXID");
		aorderdiscount= (String)billHdr.get("ORDER_DISCOUNT");
		gst= (String)billHdr.get("INBOUND_GST");
		orderdiscounttype = (String)billHdr.get("ORDERDISCOUNTTYPE");
		
		FinProjectDAO finProjectDAO = new FinProjectDAO();
		FinProject finProject=new FinProject();
		projectid = (String)billHdr.get("PROJECTID");
		int pid = Integer.valueOf(projectid);
		if(pid > 0){
			finProject = finProjectDAO.getFinProjectById(plant, pid);
			projectname = finProject.getPROJECT_NAME();
		}else{
			projectid = "";
		}
		shippingid= (String)billHdr.get("SHIPPINGID");
		shippingcust= (String)billHdr.get("SHIPPINGCUSTOMER");
		
		int taxintid = Integer.valueOf(taxid);
		FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
		FinCountryTaxType fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxintid);
		
		if(taxintid != 0){
			ptaxtype=fintaxtype.getTAXTYPE();
			ptaxiszero= String.valueOf(fintaxtype.getISZERO());
			ptaxisshow= String.valueOf(fintaxtype.getSHOWTAX());
		}
		
    	stateprefix=crdnotedao.getPrefixByState(purchaseloc);
		if(billHdr.get("CREDIT_STATUS").equals("Draft"))
    		btname="Save as Open";
		if(billHdr.get("CREDIT_STATUS").equals("CANCELLED"))
    		btname="Save as Draft";
    	
}
Hashtable ht1 = new Hashtable();
ht1.put("ID", billHdrId);
ht1.put("PLANT", plant);
List SuppliercrdnoteAttachList=crdnotedao.getSupplierCrdnoteAttachByHrdId(ht1);
String companyregnumber=StrUtils.fString(request.getParameter("companyregnumber"));
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
List curQryList=new ArrayList();
curQryList = currencyDAO.getCurrencyDetails(curency,plant);
String Supcurrency=plantMstDAO.getBaseCurrency(plant);
for(int i =0; i<curQryList.size(); i++) {
	ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
	DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
	DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
    CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
    }

String currency=plantMstDAO.getBaseCurrency(plant);
ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
ArrayList countryList = new MasterUtil().getCountryList("",plant,region);
String transportc = StrUtils.fString(request.getParameter("TRANSPORTIDC"));
String ValidNumber="";
CustUtil custUtil = new CustUtil();
ArrayList arrCust = custUtil.getVendorDetails(vendName,	plant);
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
<script src="../jsp/js/SupplierCredit.js"></script>
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
.phideshow{
    float: right;
    margin-top: -34px;
    position: relative;
    z-index: 2;
    margin-right: 40px;
}
a:focus {
  outline: 1px solid blue;
}
</style>
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box"> 
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../debit/summary"><span class="underline-on-hover">Debit Note Summary</span> </a></li>   
                <li><a href="../debit/detail?TRANID=<%=billHdrId%>"><span class="underline-on-hover">Debit Note Detail</span> </a></li>                          
                <li><label>Edit Debit Note</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../debit/detail?TRANID=<%=billHdrId%>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>

	<div class="container-fluid">
	 <input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
	 <input type="text" id="PageName" style="display: none;" value="">	 
	<form id="createBillForm" class="form-horizontal" name="form" autocomplete="off" action="/track/SupplierCreditServlet?Submit=Update"  method="post" enctype="multipart/form-data" onsubmit="return validateBill()">
	<input type="text" name="plant" value="<%=plant%>" hidden>
	<input type="text" name="username" value=<%=username%> hidden>
	<input type="text" name="vendno" value="<%=vendno%>" hidden>
	<input type="text" name="curency" value="<%=curency%>" hidden>
	<input type="text" name="billHdrId" value="<%=billHdrId%>" hidden>	
	<input type="text" name="cstatus" value="<%=creditstatus%>" hidden>	
	<INPUT type="hidden" name="STATE_PREFIX" value="<%=stateprefix %>" />
	<INPUT type="hidden" name="INV_STATE_LOC" value="" /> 
	<INPUT type="hidden" name="TAXTREATMENT_VALUE" value="<%=sTAXTREATMENT %>" />
	<input type="text" name="poreturnstatus" value="" hidden>
	<input type="text" name="poreturn" value="" hidden>
	<INPUT type="hidden" name="CURRENCYID"  value="<%=curency%>">
	
	<input type="hidden" name="PROJECTID" value="<%=projectid%>">
	<input type="hidden" name="SHIPPINGID" value="<%=shippingid%>">
	
	<input type = "hidden" name="odiscounttaxstatus" value="<%=oddisctaxstatus%>">
	<input type = "hidden" name="discounttaxstatus" value="<%=disctaxstatus%>">
	<input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
	<input type = "hidden" name="ptaxtype" value="<%=ptaxtype%>">
	<input type = "hidden" name="ptaxpercentage" value="<%=ptaxpercentage%>">
	<input type = "hidden" name="ptaxdisplay" value="<%=ptaxdisplay%>">
	<input type = "hidden" name="ptaxiszero" value="<%=ptaxiszero%>">
	<input type = "hidden" name="ptaxisshow" value="<%=ptaxisshow%>">
	<input type = "hidden" name="taxid" value="<%=taxid%>">
	<input type = "hidden" name="TGST" value="<%=gst%>">
	<input type="hidden" name="CURRENCYUSEQTOLD" value="<%=CURRENCYUSEQT%>">
	<input type = "hidden" name="aorderdiscount" value="<%=aorderdiscount%>">
	<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>
	<input type = "hidden" name="SHIPCONTACTNAME" value="<%=shipcontactname%>">
				<input type = "hidden" name="SHIPDESGINATION" value="<%=shipdesgination%>">
				<input type = "hidden" name="SHIPADDR1" value="<%=shipaddr1%>">
				<input type = "hidden" name="SHIPADDR2" value="<%=shipaddr2%>">
				<input type = "hidden" name="SHIPADDR3" value="<%=shipaddr3%>">
				<input type = "hidden" name="SHIPADDR4" value="<%=shipaddr4%>">
				<input type = "hidden" name="SHIPSTATE" value="<%=shipstate%>">
				<input type = "hidden" name="SHIPZIP" value="<%=shipzip%>">
				<input type = "hidden" name="SHIPWORKPHONE" value="<%=shipworkphone%>">
				<input type = "hidden" name="SHIPCOUNTRY" value="<%=shipcountry%>">
				<input type = "hidden" name="SHIPHPNO" value="<%=shiphpno%>">
				<input type = "hidden" name="SHIPEMAIL" value="<%=shipemail%>">
	<input type = "hidden" name="custModal">
	<%if(!ispuloc){ %>
		 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
	<%} %>
	
		<div class="form-group vendor-section">
			<label class="control-label col-form-label col-sm-2 required">Supplier Name</label>
			<div class="col-sm-6 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected  form-control typeahead" id="vendname" placeholder="Select a vendor" name="vendname" onchange="checksupplier(this.value)" value="<%=vendName%>" disabled>
				<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'vendname\']').focus()"><!-- <i class="glyphicon glyphicon-menu-down"></i> --></span>
				<span class="btn-danger input-group-addon" id="btnpop" onclick="javascript:popUpWin('../jsp/vendor_list.jsp?AUTO_SUGG=Y&CUST_NAME='+form.vendno.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	
				</div>
				<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" onchange="OnChkTaxChange(this.value)" data-placement="right" id="nTAXTREATMENT" name="nTAXTREATMENT" value="<%=sTAXTREATMENT %>" style="width: 100%">
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
								<input type="text" class="form-control typeahead" id="SHIPPINGCUSTOMER" name="SHIPPINGCUSTOMER" onchange="checkcustomer(this.value)" value="<%=shippingcust%>"> 
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'SHIPPINGCUSTOMER\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
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
						<p><%=sState %></p>
						<p><%=sCountry%> <%=sZip%></p>
						<p><%=sHpNo%></p>
						<p><%=sWorkphone%></p>
						<p><%=sEmail%></p>
						
						
					</div>
					<div class="col-sm-4" style="line-height: 7px; font-size: 13px;" id="shipadd">
						<h5 style="font-weight: bold;">
							Shipping Address <a><span data-toggle="modal"
								data-target="#shipaddr" onclick="getshipaddr();" style="font-size: 15px;font-weight: 450;">Change</span></a>
						</h5>
						<div id="cshipaddr">
							
						<p><%=shipcontactname%></p>
						<p><%=shipdesgination%></p>
						<p><%=shipaddr1 %> <%=shipaddr2 %></p>
						<p><%=shipaddr3 %> <%=shipaddr4 %></p>
						<p><%=shipstate%></p>
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
									id="project" placeholder="Select a project" name="project" onchange="checkproject(this.value)" value="<%=projectname%>">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'project\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
								<!-- <span class="btn-danger input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
					</div>
				</div>
				<%}else{%>
				<input type="hidden" name="project" value="">
				<%} %>
					
		
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
		        		<input class="form-control" name="creditnote" id="creditnote" type="text" readonly
		        			value="<%=creditnote%>" > 
	        			<span class="input-group-addon"  onClick="getNextCreditnote();" style="display: none">
				   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate" >
				   		 		<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i>
				   		 	</a>
			   		 	</span>
		   		 	</div>
			</div>
		</div>
		<%if(!grnumber.equalsIgnoreCase("")){ %>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Reference</label>
			
			<div class="col-sm-4">				
				<input type="text" class="form-control pono" id="pono" name="pono" value="<%=pono%>" disabled maxlength ="50">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'pono\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>	
			<%} else{%>
				<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Reference</label>
			
			<div class="col-sm-4">				
				<input type="text" class="form-control pono" id="pono" name="pono" value="<%=pono%>"  maxlength ="50">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'pono\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>	
			<%}%>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Debit Note Date</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" value="<%=creditDate%>" id="bill_date" name="bill_date" READONLY>
			</div>
				<div class="col-sm-6 no-padding">
			<label class="control-label col-form-label col-sm-5">Payment Terms</label>
			<div class="col-sm-6">
				<input type="text" class="form-control" id="payment_terms" name="payment_terms" onchange="checkpaymentterms(this.value)" value="<%=payTerms%>" placeholder="Select Payment Terms">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		</div>
		<div class="form-group employee-section">
			<label class="control-label col-form-label col-sm-2">Employee</label>
			<div class="col-sm-4 ac-box">
				<input type="text" class="ac-selected form-control typeahead" id="EMP_NAME" name="EMP_NAME" onchange="checkemployeess(this.value)" value="<%=empname%>" placeholder="Select a Employee">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		
			<%if(ispuloc){ %>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Purchase Location</label>
			<div class="col-sm-4 ac-box">
				<input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" value="<%=purchaseloc %>" name="SALES_LOC" placeholder="Select a Purchase Location">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		<%} %>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Currency</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control typeahead" id="CURRENCY" name="CURRENCY"  onchange="checkcurrency(this.value)" placeholder="Select a Currency" value="<%=DISPLAY%>" disabled>
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
					<%if(billnumber.length() > 0 || grnumber.length() > 0){ %>
			   		 	<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" value="<%=gst%>" readonly>
			   		<%}else{ %>
			   			<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" onkeypress="return isNumberKey(event,this,4)" value="<%=gst%>">
			   		<%} %>
						<span class="sideiconspan"><p>%</p></span>

					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Purchasetax" name="Purchasetax" onchange="checktax(this.value)" placeholder="Select a Tax">
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
					<select class="ac-box dropdown-noborder form-control" onchange="calculateTotal()" name="item_rates" id="item_rates"  value="<%=item_rate%>">
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
				  <input type="hidden" name="tax_type" class="taxSearch" value="">
				  <input type="hidden" name="tax" class="taxSearch" value="">
<!-- 				  <input type="hidden" name="lnno" value="1"> -->
				</td>
				<td class="bill-item-dbt">
					<input type="text" name="item" class="form-control itemSearch" onchange="checkitems(this.value,this)" placeholder="Type or click to select an item." onchange="calculateAmount(this)">
				   <input class="form-control"  name="ITEMDES" style="height: 23px;background-color: #fff;" readonly>	
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
<!-- 					<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax"> -->
<!-- 				</td>				 -->
				<td class="item-amount text-right grey-bg">
				<input name="amount" type="text" class="form-control text-right" value="0.00" readonly="readonly" tabindex="-1">
				<input name="landedCost" type="text" value="0.0" hidden>
				</td>
			  </tr>
			</tbody>
			</table>
		</div>
		<div class="row">
			<div class="col-sm-6">
			<%if(billnumber.equals("") || billnumber == null || billnumber.equals("0")){ %>
             <a href="#" onclick="addRow(event);">
			 <i class="add-line" id="addmore" title="Add another line" style="font-size: 15px;">+ Add another line</i>
		     </a> 
			<%} %>
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
										<%if(oddisctaxstatus.equalsIgnoreCase("1")){%><sapn id="odtax">(Tax Inclusive)</sapn><%}else{%><sapn id="odtax">(Tax Exclusive)</sapn><%} %>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isodisctax" <%if(oddisctaxstatus.equalsIgnoreCase("1")){%>checked<%}%> name="isodisctax" Onclick="isodisctaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right oddiscount" type="text" value="<%=orderdiscount%>" readonly
												name="orderdiscount" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
											<!-- <span class="input-group-addon">%</span> -->
											<select class="discountPicker form-control" disabled="true">
												<option <%if(orderdiscounttype.equalsIgnoreCase(curency)){ %>selected<%} %>value="<%=curency%>"><%=curency%></option>
												<option <%if(orderdiscounttype.equalsIgnoreCase("%")){ %>selected<%} %> value="%">%</option>	
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
								<%if(disctaxstatus.equalsIgnoreCase("1")){%><sapn id="dtax">(Tax Inclusive)</sapn><%}else{%><sapn id="dtax">(Tax Exclusive)</sapn><%} %>
							</div>
							<div class="col-lg-1 col-sm-1 col-1">
								<input type="checkbox" class="form-check-input isdtax" <%if(disctaxstatus.equalsIgnoreCase("1")){%>checked<%}%> name="isdtax" Onclick="isdtaxing(this)">
							</div>
							<div class=" col-lg-6 col-sm-6 col-6">
								<div class="input-group my-group"> 
									<input class="form-control text-right" type="text" id="dediscount" name="discount" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)" value="<%=discount%>" >
									
									<select class="discountPicker form-control discount_type"  name="discount_type" onchange="calculateTotal()">
										<option <%if(dicountType.equalsIgnoreCase(curency)){ %>selected<%} %>value="<%=curency%>"><%=curency%></option>
										<option <%if(dicountType.equalsIgnoreCase("%")){ %>selected<%} %> value="%">%</option>										
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
								<input class="form-control text-right ember-view" type="text" name="shippingcost" onkeypress="return isNumberKey(event,this,4)" value="<%=shippingCharge%>"> 
							</div>  
						</div>
					</div>
					<div class="total-amount" id="shipping"> 0.00 </div>
				</div>				
				<div class="total-row">
					<div class="badge-editable total-label">
						<div class="row">
							<div class="col-lg-5 col-sm-5 col-5"> 
								<input type="text" class="form-control" name="adjustmentLabel" value = "<%=adjLabel%>">
							</div> 
							<div class="col-lg-6 col-sm-6 col-6"> 
								<input class="form-control text-right ember-view" type="text" name="adjustment" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)" value = "<%=adjustment%>"> 
							</div>  
						</div>
					</div> 
					<div class="total-amount " style="line-height: 2" id="adjustment"> 0.00 </div>
				</div>
				
				<div class="total-section total-row">
							<div class="gross-total-removemargin"><!--  Author: Resvi  Add date: July 28,2021  Description: Total of Local Currency-->
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
				<%if(SuppliercrdnoteAttachList.size()>0){ %>
						<div id="billAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=SuppliercrdnoteAttachList.size()%> files Attached</a>
									<div class="tooltiptext">
										
										<%for(int i =0; i<SuppliercrdnoteAttachList.size(); i++) {   
									  		Map attach=(Map)SuppliercrdnoteAttachList.get(i); %>
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
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
						<%} %>
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
				<%-- <button id="btnBillUpdate" type="submit" class="btn btn-success"><%=btname%></button> --%>
				<% if(creditstatus.equalsIgnoreCase("Draft")){%>
					<button id="btnBillOpen" type="button" class="btn btn-success">Save as Open</button>
				<%}else if(creditstatus.equalsIgnoreCase("CANCELLED")){%>
					<button id="btnBillDraft" type="button" class="btn btn-success">Save as Draft</button>
				<% }else{%>
					<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				<% }%>
				
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			</div>
		</div>
	</form>
</div>
	<!-- Modal -->
	
	
	<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
<%-- 	<%@include file="newProductModal.jsp" %> --%>
			<jsp:include page="newProductModalPurchase.jsp" flush="true"></jsp:include> <!-- imti for add product -->
			<jsp:include page="newProductDepTypeModal.jsp" flush="true"></jsp:include><!-- resvi for add product department -->
			<jsp:include page="newProductCategoryModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product category -->
			<jsp:include page="newProductSubCatgoryModal.jsp" flush="true"></jsp:include><!-- Thanzith for add product Sub Category -->
			<jsp:include page="newProductBrandModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product Brand -->
			<jsp:include page="newProductHsCodeModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product HsCode -->
			<jsp:include page="newProductCOOModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product COO -->
			<jsp:include page="newProductUOMmodal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product UOM -->
	<%-- <%@include file="NewChartOfAccountAdd.jsp"%> --%>
	<jsp:include page="newEmployeeModal.jsp" ></jsp:include>
	<%@include file="NewChartOfAccountpopup.jsp"%>
	<%@include file="newGstDetailModal.jsp" %>
	<%@include file="newShipmentModal.jsp" %>
<input type="hidden" name="pronumberOfDecimal" id="pronumberOfDecimal" value="<%=numberOfDecimal%>">
	<!-- Modal -->
	<!-- <div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog">	
	    Modal content
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
	</div> -->
	</div>
</div>

<!-- ----------------Modal-------------------------- -->

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
							MAXLENGTH=30 class="form-control">
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
								ArrayList ccListSP = _MasterUtil1.getCountryList("", plant, region);
								for (int i = 0; i < ccListSP.size(); i++) {
									Map m = (Map) ccListSP.get(i);
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
  <!-- Author: Azees  Create date: June 26,2021  Description: New Supplier Popup Changes --> 
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
					<input type="number" id="numberOfDecimal" style="display: none;"
						value=<%=numberOfDecimal%>>
						<input type="text" name="PLANT"  style="display: none;"
						value=<%=plant%>>
						<input type="text" name="LOGIN_USER" style="display: none;"
						value=<%=username%>>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required"
							for="Create Supplier ID">Supplier Id</label>
						<div class="col-sm-4">
							<div class="input-group">
								<INPUT class="form-control" name="CUST_CODE" id="CUST_CODE" onkeypress="return blockSpecialChar(event)"
									type="TEXT" value=""
									onchange="checkitem(this.value)" size="50" MAXLENGTH=50
									width="50"> <span
									class="input-group-addon" onClick="onIDGen();"> <a
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
					
					
					<!-- 	                Author Name:Resviya ,Date:10/08/21 , Description -UEN Label     -->
          <div class="form-group" id="UEN">
	      <label class="control-label col-form-label col-sm-2" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
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
							for="supplier type">Supplier Group</label>
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

										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE_S" name="COUNTRY_CODE_S" value="" style="width: 100%">
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
										for="Payment Terms">Payment Mode</label>
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
							<button type="button" class="btn btn-success" onClick="onAdd();" >
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

<div id="customerModal" class="modal fade"  name="formcust" role="dialog">
	<div class="modal-dialog modal-lg">
		<form class="form-horizontal" name="formcust" id="formCustomer" method="post">
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
								<INPUT class="form-control" name="CUST_CODE_C" id="CUST_CODE_C" onkeypress="return blockSpecialChar(event)"
									type="TEXT" value="" onchange="checkitem(this.value)" size="50"
									MAXLENGTH=50 width="50"> <span
									class="input-group-addon" onClick="onIDGens();"> <a
									href="#" data-toggle="tooltip" data-placement="top"
									title="Auto-Generate"> <i class="glyphicon glyphicon-edit"
										style="font-size: 20px;"></i></a></span>
							</div>
							<INPUT type="hidden" name="CUST_CODE1_C" value="">
							<INPUT type="hidden" name="COUNTRY" value="">
							<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
							<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">
			                <INPUT type="hidden" name="CURRENCYID_C" value="<%=DISPLAYID%>">	<!--Resvi -->
			                <input type="hidden" name="TRANSPORTIDC" value="<%=transportc%>">
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
					</div>
					
						<!-- 	                Author Name:Resviya ,Date:9/08/21 , Description -UEN Label     -->
          <div class="form-group" id="UEN">
	      <label class="control-label col-form-label col-sm-2" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
	      <div class="col-sm-4">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="cus_companyregnumber" id="cus_companyregnumber" type = "TEXT" value="<%=companyregnumber%>" size="50"  MAXLENGTH=100> 
	  	  </div>
	       </div>
	       </div>
	         <!-- End -->
					
					<!-- 	                Author Name:Resviya ,Date:19/07/21 -->

                      <div class="form-group">
					  <label class="control-label col-form-label col-sm-2 required">Customer
							Currency</label>
					 <div class="col-sm-4">
					  <INPUT class="form-control" name="CUS_CURRENCY"  id="CUS_CURRENCY" type="TEXT" value="<%=currency%>"  size="50" MAXLENGTH=100>
					 </div>
					 </div>
					 

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
    							<input name="transportC" id="transportC" type="TEXT"  placeholder="Select a transport" size="50" onchange="checkcustomertransport(this.value)" value="" MAXLENGTH=50 class="form-control">
   		 						<span class="select-icon" onclick="$(this).parent().find('input[name=\'transportC\']').focus()">
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
							<li class="nav-item active"><a href="#other_cust"
								class="nav-link" data-toggle="tab" aria-expanded="true">Other
									Details</a></li>
							<li class="nav-item"><a href="#profile_cust" class="nav-link"
								data-toggle="tab">Contact And Address Details</a></li>
<!-- 							<li class="nav-item"><a href="#home_cust" class="nav-link"
								data-toggle="tab">Address</a></li> -->
							<li class="nav-item"><a href="#bank_cust" class="nav-link"
							 	data-toggle="tab">Bank Account Details</a></li>
							<li class="nav-item"> <a href="#user_c" class="nav-link" 
								data-toggle="tab">User</a></li> 	
							<li class="nav-item"><a href="#remark_cust" class="nav-link"
								data-toggle="tab">Remarks</a></li>
						</ul>
						<div class="tab-content clearfix">
							<%-- <div class="tab-pane fade" id="home_cust">
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

							</div> --%>

							<div class="tab-pane fade" id="profile_cust">
								<br>
					  <h1 class="col-sm-3" style="font-weight: bold;font-size: 16px;right: 16px;">  Contact Address </h1>
                      <h1 style="font-weight: bold;font-size: 16px;margin-left: 450px;">  Shipping Address <label class="checkbox-inline" style="margin-left: 50px;margin-top: 2px;">
                      <input type="checkbox" id="SameAsContactAddress" name="SameAsContactAddress" value="SameAsContactAddress" onclick="shippingAddress();">Same As Contact Address</label></h1>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Contact
										Name</label>
									<div class="col-sm-4">

										<INPUT name="CONTACTNAME" id="CONTACTNAME" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="100">
									</div>
									<div class="col-sm-4">

										<INPUT name="CUST_SHIP_CONTACTNAME" id="CUST_SHIP_CONTACTNAME" type="TEXT" class="form-control"
											value="" size="50" MAXLENGTH="100">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Designation</label>
									<div class="col-sm-4">

										<INPUT name="DESGINATION" id="DESGINATION" type="TEXT" class="form-control"
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
										<INPUT name="WORKPHONE" id="WORKPHONE" type="TEXT" value=""
											onkeypress="return isNumber(event)" size="50" MAXLENGTH=30
											class="form-control">
									</div>
									<div class="col-sm-4">
										<INPUT name="CUST_SHIP_WORKPHONE" id="CUST_SHIP_WORKPHONE" type="TEXT" value=""
											onkeypress="return isNumber(event)" size="50" MAXLENGTH=30
											class="form-control">
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

										<INPUT name="CUST_SHIP_HPNO" id="CUST_SHIP_HPNO" type="TEXT" value="" size="50"
											class="form-control" onkeypress="return isNumber(event)"
											MAXLENGTH="30">
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Email</label>
									<div class="col-sm-4">

										<INPUT name="EMAIL" id="EMAIL" type="TEXT" value="" size="50"
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

										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountrys(this.value)" id="COUNTRY_CODE_C" name="COUNTRY_CODE_C" value="" style="width: 100%">
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
									</div>
								</div>

								<div class="form-group">
									<label class="control-label col-form-label col-sm-2">Unit
										No.</label>
									<div class="col-sm-4">

										<INPUT name="ADDR1" id="ADDR1" type="TEXT" value="" size="50"
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

										<INPUT name="CUST_SHIP_ADDR2" id="CUST_SHIP_ADDR2" type="TEXT" value="" size="50"
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
									<div class="col-sm-4">
										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="CUST_SHIP_STATE" name="CUST_SHIP_STATE" value="" style="width: 100%">
										<OPTION style="display:none;">Select State</OPTION>
										</SELECT>
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

										<INPUT name="CUST_SHIP_ZIP" id="CUST_SHIP_ZIP" type="TEXT" value="" size="50" MAXLENGTH=10
											class="form-control">
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
			        <label class="control-label col-form-label col-sm-2">Payment Mode</label>
      				<div class="col-sm-4">
	       				<input id="CUST_PAYTERMS" name="CUST_PAYTERMS" class="form-control" type="text"  onchange="checkcustomerpaymenttype(this.value)" value="" MAXLENGTH=100 placeholder="Select Payment Mode">
		    			<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUST_PAYTERMS\']').focus()">
					 	<i class="glyphicon glyphicon-menu-down"></i></span>
	  				</div> 
	  				</div>
  
  					<div class="form-group">
       				<label class="control-label col-form-label col-sm-2">Payment Terms</label>
       				<div class="col-sm-4">
       					<input type="text" class="form-control" id="payment_term"	name="payment_term"  onchange="checkcustomerpaymentterms(this.value)" value="" placeholder="Select Payment Terms">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_term\']').focus()">
						<i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
					<div class="form-inline">
    				<label class="control-label col-form-label col-sm-1">Days</label>
    	    			<input name="PMENT_DAYS" type="TEXT" class="form-control" size="5" MAXLENGTH=10 readonly>
  					</div>
    				</div>

<!-- 								<div class="form-group"> -->
<!-- 									<label class="control-label col-form-label col-sm-2">Payment -->
<!-- 										Type</label> -->
<!-- 									<div class="col-sm-4"> -->
<!-- 										<div class="input-group"> -->
<!-- 											<INPUT class="form-control" name="PAYTERMS" type="TEXT" -->
<!-- 												value="" size="20" MAXLENGTH=100 readonly> <span -->
<!-- 												class="input-group-addon" -->
<!-- 												onClick="javascript:popUpWin('../jsp/list/paymenttypelist_save.jsp?paymenttype='+form.PAYTERMS.value);"> -->
<!-- 												<a href="#" data-toggle="tooltip" data-placement="top" -->
<!-- 												title="Payment Type"> <i -->
<!-- 													class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a> -->
<!-- 											</span> -->
<!-- 										</div> -->
<!-- 									</div> -->

<!-- 									<div class="form-inline"> -->
<!-- 										<label class="control-label col-form-label col-sm-1">Days</label> -->
<!-- 										<input name="PMENT_DAYS" type="TEXT" value="" size="5" -->
<!-- 											MAXLENGTH=10 class="form-control"> -->
<!-- 									</div> -->
<!-- 								</div> -->

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
									<input class="form-control text-left" name="USER_NAME" type="text" placeholder="Enter User Name" maxlength="50" autocomplete="off" >
								</td>
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

<div class="tab-pane fade" id="bank_cust">
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
	<INPUT name="BANKNAME" type = "TEXT" value="" id="BANKNAMECUS" class="form-control" onchange="checkcustomerbank(this.value)" placeholder="BANKNAME">
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
							<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
							<!-- <button type="button" class="Submit btn btn-default"
								onClick="onNew();">Clear</button> -->
							&nbsp;&nbsp;
							<button type="button" class="btn btn-success"
								data-toggle="modal" data-target="#myModal" onClick="onAddcust();">Save</button>
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

$(".user-table tbody").on('click', '.user-action', function() {
	$(this).parent().parent().remove();
});

//transport
$('#transportC').typeahead({
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
		menuElement.after( '<div class="transportcAddBtn footer"  data-toggle="modal" data-target="#transportModal"><a href="#"> + New Transport </a></div>');
		menuElement.next().width(menuElement.width());
		menuElement.next().css({ "top": top,"padding":"3px 20px" });
		if($(this).parent().find(".tt-menu").css('display') != "block")
			menuElement.next().hide();
	  
	}).on('typeahead:open',function(event,selection){
		$('.transportcAddBtn').show();
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);    
	}).on('typeahead:close',function(){
		setTimeout(function(){ $('.transportcAddBtn').hide();}, 180);	
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
	}).on('typeahead:select',function(event,selection){
		$("input[name=TRANSPORTIDC]").val(selection.ID);
	});

/* Payment Mode Auto Suggestion */
$("#PAYTERMS").typeahead({
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
		menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal" onclick="document.form.custModal.value=\'cust\'"><a href="#"> + New Payment Mode</a></div>');
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
		menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal" onclick="document.form.custModal.value=\'shipcust\'"><a href="#"> + New Payment Mode</a></div>');
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

/* Payment Terms Auto Suggestion */
$('#payment_term').typeahead({
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
		menuElement.after( '<div class="payTermsAddBtn footer"  data-toggle="modal" data-target="#payTermsModal" onclick="document.form.custModal.value=\'shipcust\'"><a href="#"> + Add Payment Terms</a></div>');
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

/* Payment Terms Auto Suggestion */
$('#sup_payment_terms').typeahead({
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
		menuElement.after( '<div class="payTermsAddBtn footer"  data-toggle="modal" data-target="#payTermsModal" onclick="document.form.custModal.value=\'cust\'"><a href="#"> + Add Payment Terms</a></div>');
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
		$("input[name=SUP_PMENT_DAYS]").val(selection.NO_DAYS);
	});

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
		

	
	$('#discount_type').append('<option value="<%=curency%>"><%=curency%></option>');
	$('#discount_type').append('<option value="%">%</option>');
	
	$('#item_discounttype').empty();
	$('#item_discounttype').append('<option value="<%=curency%>"><%=curency%></option>');
	$('#item_discounttype').append('<option value="%">%</option>');
	
	$('#CURRENCY').css("background-color", "#eeeeee");
	
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

    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.";	
    
 	var cust = "<%=sVendorCode%>";
    
    if(cust=="" || cust.length==0 ) {
    }else{
    	
    	$("input[name ='CUST_CODE']").val(cust);
    	$("input[name ='CUST_CODE1']").val(cust);
    	 $('#supplierModal').modal('show');
    }
    document.getElementById("btnpop").style.display = "none";
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
function onAdd(){
	   var CUST_CODE   = document.form1.CUST_CODE.value;
	   var CUST_NAME   = document.form1.CUST_NAME.value;
//	   var companyregnumber   = document.form1.companyregnumber.value;
	   var TAXTREATMENT   = document.form1.TAXTREATMENT.value;
	   var RCBNO   = document.form1.RCBNO.value;
	   var rcbn = RCBNO.length;
	   var CURRENCY = document.form1.SUP_CURRENCY.value;
	   var region = document.form1.COUNTRY_REG.value;
	   var ValidNumber   = document.form1.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" suppliers you can create"); return false; }
	   
	   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Supplier ID");;document.form1.CUST_CODE.focus(); return false; }
	   
	   if(CUST_NAME == "" || CUST_NAME == null) {
	   alert("Please Enter Supplier Name"); 
	   document.form1.CUST_NAME.focus();
	   return false; 
	   }

	// Author Name:Resviya ,Date:10/08/21 , Description -UEN Alert    

	/*    if(region == "GCC"){
		   document.form1.companyregnumber.value="";
		}else if(region == "ASIA PACIFIC"){
			if (companyregnumber == "" || companyregnumber == null) {
			alert("Please Enter Unique Entity Number (UEN)");
			document.form1.companyregnumber.focus();
			return false; 
			}
		}
 */
	//   END
	
//		 <!-- Author Name:Resviya ,Date:20/07/21 -->

		 if(CURRENCY == "" || CURRENCY == null) {
			 alert("Please Enter Currency ID"); 
			 document.form1.SUP_CURRENCY.focus();
			 return false; 
			 }
//		 End
	   if(document.form1.TAXTREATMENT.selectedIndex==0)
		{
		alert("Please Select TAXTREATMENT");
		document.form1.TAXTREATMENT.focus();
		return false;
		}
	   if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	   {
	   var  d = document.getElementById("TaxByLabel").value;
	   	if(RCBNO == "" || RCBNO == null) {
	   		
		   alert("Please Enter "+d+" No."); 
		   document.form1.RCBNO.focus();
		   return false; 
		   }
	   	//if(document.form1.COUNTRY_REG.value=="GCC")// region based validtion
		//{
		if(!IsNumeric(RCBNO))
		{
	    alert(" Please Enter "+d+" No. Input In Number"); 
	   	document.form1.RCBNO.focus();
	   	return false; 
	  	}

		if("15"!=rcbn)
		{
		alert(" Please Enter your 15 digit numeric "+d+" No."); 
			document.form1.RCBNO.focus();
			return false; 
			}
		//}
	   }
	else if(50<rcbn)
	{
	   var  d = document.getElementById("TaxByLabel").value;
	   alert(" "+d+" No. length has exceeded the maximum value"); 
	   document.form1.RCBNO.focus();
	   return false; 
	 }

	   if(!IsNumeric(form1.SUP_PMENT_DAYS.value))
	   {
	     alert(" Please Enter Days In Number");
	     form1.SUP_PMENT_DAYS.focus();  form1.SUP_PMENT_DAYS.select(); return false;
	   }
	   if(document.form1.COUNTRY_CODE_S.selectedIndex==0)
		{
		   alert("Please Select Country from Address");
		   document.form.COUNTRY_CODE_S.focus();
		 return false;
		}
	   
	   var datasend = $('#formsupplierid').serialize();
	   
	   var urlStr = "/track/CreateSupplierServlet?action=JADD&reurl=editSupplierCredit";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : datasend,
		dataType : "json",
		success : function(data) {
			/* console.log(data);*/
			//alert(data.supplier[0].SID); 
			if(document.form.custModal.value == "cust"){
			$("input[name ='vendno']").val(data.supplier[0].SID); 
			/* document.getElementById("evendcode").value  = data.supplier[0].SID; */
			$("input[name ='vendname']").val(data.supplier[0].SName);
			$('select[name ="nTAXTREATMENT"]').val(data.supplier[0].sTAXTREATMENT);
			$("input[name ='payment_terms']").val(data.supplier[0].PAY_TERMS);
			getCurrencyid(data.supplier[0].CURRENCY,data.supplier[0].sCURRENCY_ID,data.supplier[0].CURRENCYUSEQT);
			$('#nTAXTREATMENT').attr('disabled',false);
			if(data.supplier[0].sTAXTREATMENT =="GCC VAT Registered"||data.supplier[0].sTAXTREATMENT=="GCC NON VAT Registered"||data.supplier[0].sTAXTREATMENT=="NON GCC")
			{
				document.getElementById('CHK1').style.display = 'block';
			}
			else
				document.getElementById('CHK1').style.display = 'none';
			
			document.getElementById("REVERSECHARGE").checked = false;
			document.getElementById("GOODSIMPORT").checked = false;
			
			$('#shipbilladd').empty();
			var addr = '<div><h5 style="font-weight: bold;">Billing Address</h5>';
			addr += '<p>'+data.supplier[0].NAME+'</p>';
			addr += '<p>'+data.supplier[0].ADDR1+' '+data.supplier[0].ADDR2+'</p>';				
			addr += '<p>'+data.supplier[0].ADDR3+' '+data.supplier[0].ADDR4+'</p>';
			addr += '<p>'+data.supplier[0].STATE+'</p>';
			addr += '<p>'+data.supplier[0].COUNTRY+' '+data.supplier[0].ZIP+'</p>';
			addr += '<p>'+data.supplier[0].HPNO+'</p>';
			addr += '<p>'+data.supplier[0].EMAIL+'</p>';
			addr += '</div>';
			$('#shipbilladd').append(addr);
			
			document.form1.reset();
			$('#supplierModal').modal('hide');
			}else{
				document.form1.reset();
				document.form.SHIPPINGCUSTOMER.value = data.supplier[0].SName;
				document.form.SHIPPINGID.value = data.supplier[0].SID;
				$('#supplierModal').modal('hide');
			}
		}
		});
	}

function onAddcust(){
	 var CUST_CODE   = document.formcust.CUST_CODE_C.value;
	 var CUST_NAME   = document.formcust.CUST_NAME_C.value;
	 var PEPPOL_C  = document.form.PEPPOL_C.value;
	 var PEPPOL_IDC   = document.form.PEPPOL_IDC.value;
//	 var companyregnumber   = document.formcust.cus_companyregnumber.value;
	 var CL   = document.formcust.CREDITLIMIT.value;
	 var TAXTREATMENT   = document.formcust.TAXTREATMENT.value;
	 var RCBNO   = document.formcust.RCBNO.value;
	 var CURRENCY = document.formcust.CUS_CURRENCY.value;
	   var region = document.formcust.COUNTRY_REG.value;

	   var rcbn = RCBNO.length;
	   
	   var ValidNumber   = document.formcust.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" customers you can create"); return false; }
	   
	 if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.formcust.CUST_CODE_C.focus(); return false; }
	 if(CUST_NAME == "" || CUST_NAME == null) {
		 alert("Please Enter Customer Name"); 
		 document.formcust.CUST_NAME_C.focus();
		 return false; 
		 }

	 if(document.getElementById("PEPPOL_C").checked == true)
			PEPPOL_C="1";
		 else 
			PEPPOL_C="0";

		   if(PEPPOL_IDC == "" &&  PEPPOL_C == "1") {
		 	  alert("Please Enter Peppol Id"); 
		 	return false; 
		}
	 
//    Author Name:Resviya ,Date:9/08/21 , Description -UEN Alert    

	  /*  if(region == "GCC"){
		   document.formcust.companyregnumber.value="";
		}else if(region == "ASIA PACIFIC"){
			if (companyregnumber == "" || companyregnumber == null) {
			alert("Please Enter Unique Entity Number (UEN)");
			document.formcust.companyregnumber.focus();
			return false; 
			}
		} */

//	   END
	 
	 //	 <!-- Author Name:Resviya ,Date:19/07/21 -->

	 if(CURRENCY == "" || CURRENCY == null) {
		 alert("Please Enter Currency ID"); 
		 document.formcust.CUS_CURRENCY.focus();
		 return false; 
		 }
//	 End
	 if(document.formcust.TAXTREATMENT.selectedIndex==0)
		{
		alert("Please Select TAXTREATMENT");
		document.formcust.TAXTREATMENT.focus();
		return false;
		}
	 if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	   {
	   var  d = document.getElementById("TaxByLabel").value;
	   	if(RCBNO == "" || RCBNO == null) {
	   		
		   alert("Please Enter "+d+" No."); 
		   document.formcust.RCBNO.focus();
		   return false; 
		   }
	   	//if(document.formcust1.COUNTRY_REG.value=="GCC")// region based validtion
		//{
		if(!IsNumeric(RCBNO))
		{
	    alert(" Please Enter "+d+" No. Input In Number"); 
	   	document.formcust.RCBNO.focus();
	   	return false; 
	  	}

		if("15"!=rcbn)
		{
		alert(" Please Enter your 15 digit numeric "+d+" No."); 
			document.formcust.RCBNO.focus();
			return false; 
			}
		//}
	   }
	else if(50<rcbn)
	{
	   var  d = document.getElementById("TaxByLabel").value;
	   alert(" "+d+" No. length has exceeded the maximum value"); 
	   document.formcust.RCBNO.focus();
	   return false; 
	 }

	 if(CL < 0) 
	 {
		   alert("Credit limit cannot be less than zero");
		   document.formcust.CREDITLIMIT.focus(); 
		   return false; 
		   }	 
	// alert(isCL);
	
	 //alert("2nd");
	 if(!IsNumeric(CL))
	 {
	   alert(" Please Enter Credit Limit Input In Number");
	   formcust.CREDITLIMIT.focus();  
	   formcust.CREDITLIMIT.select(); 
	   return false;
	 }
	 if(!IsNumeric(formcust.PMENT_DAYS.value))
	 {
	   alert(" Please Enter Days Input In Number");
	   formcust.PMENT_DAYS.focus();  formcust.PMENT_DAYS.select(); return false;
	 }
	 
	 
	//  alert(CL);
	 /* if(this.cpoformcust.ISCREDITLIMIT.checked == true && CL == ""){
		   alert("Please Enter Credit Limit");
		   document.cpoform.CREDITLIMIT.focus();
		   return false; 
	 } */
	 if(document.formcust.COUNTRY_CODE_C.selectedIndex==0)
		{
		   alert("Please Select Country from Address");
		   document.formcust.COUNTRY_CODE_C.focus();
		 return false;
		}
	 /* if(isCL.equals("1") && CL.equals(""))
		  {
			  alert("Please Enter Credit Limit"); 
			   document.form.CREDITLIMIT.focus();
			   return false; 
		  }	 */
	 
	 /* document.cpoform.action  = "/track/CreateCustomerServlet?action=ADD&reurl=createInvoice";
	 document.cpoform.submit(); */
	 var datasend = $('#formCustomer').serialize();
	   
	  
	   var urlStr = "/track/CreateCustomerServlet?action=JADD&reurl=createInvoice";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : datasend,
		dataType : "json",
		success : function(data) {
			if(document.form.custModal.value == "cust"){
				//alert(JSON.stringify(data));
				$("input[name ='CUST_CODE']").val(data.customer[0].CID);
				//document.getElementById("CUST_CODE").value  = data.customer[0].CID;
				document.getElementById("CUSTOMER").value  = data.customer[0].CName;
				$("input[name ='CUSTOMER']").val(data.customer[0].CName);
				$("input[name ='TAXTREATMENT_VALUE']").val(data.customer[0].CTAXTREATMENT);
				$("input[name ='transport']").val(data.customer[0].TRANSPORTNAME);
				$("input[name ='TRANSPORTID']").val(data.customer[0].TRANSPORTID);
				setCurrencyid(data.customer[0].CURRENCY,data.customer[0].CURRENCY_ID,data.customer[0].CURRENCYUSEQT);
				document.getElementById('TAXTREATMENT').value = data.customer[0].CTAXTREATMENT;
				document.form1.reset();
				$('#customerModal').modal('hide');
			}else{
				document.form1.reset();
				
				$("input[name=SHIPCONTACTNAME]").val(data.customer[0].SHIPCONTACTNAME);
				$("input[name=SHIPDESGINATION]").val(data.customer[0].SHIPDESGINATION);
				$("input[name=SHIPADDR1]").val(data.customer[0].SHIPADDR1);
				$("input[name=SHIPADDR2]").val(data.customer[0].SHIPADDR2);
				$("input[name=SHIPADDR3]").val(data.customer[0].SHIPADDR3);
				$("input[name=SHIPADDR4]").val(data.customer[0].SHIPADDR4);
				$("input[name=SHIPSTATE]").val(data.customer[0].SHIPSTATE);
				$("input[name=SHIPZIP]").val(data.customer[0].SHIPZIP);
				$("input[name=SHIPWORKPHONE]").val(data.customer[0].SHIPWORKPHONE);
				$("input[name=SHIPCOUNTRY]").val(data.customer[0].SHIPCOUNTRY);
				$("input[name=SHIPHPNO]").val(data.customer[0].SHIPHPNO);
				$("input[name=SHIPEMAIL]").val(data.customer[0].SHIPEMAIL);	
				
				$('#shipadd').empty();
				var addr = '<div><h5 style="font-weight: bold;">Shipping Address   <a><span data-toggle="modal" data-target="#shipaddr" onclick="getshipaddr();" style="font-size: 15px;font-weight: 450;">Change</span></a></h5>';
				addr += '<div id="cshipaddr">';
				addr += '<p>'+data.customer[0].SHIPCONTACTNAME+'</p>';
				addr += '<p>'+data.customer[0].SHIPDESGINATION+'</p>';
				addr += '<p>'+data.customer[0].SHIPADDR1+' '+data.customer[0].SHIPADDR2+'</p>';
				addr += '<p>'+data.customer[0].SHIPADDR3+' '+data.customer[0].SHIPADDR4+'</p>';
				addr += '<p>'+data.customer[0].SHIPSTATE+'</p>';
				addr += '<p>'+data.customer[0].SHIPCOUNTRY+' '+data.customer[0].SHIPZIP+'</p>';
				addr += '<p>'+data.customer[0].SHIPHPNO+'</p>';
				addr += '<p>'+data.customer[0].SHIPWORKPHONE+'</p>';
				addr += '<p>'+data.customer[0].SHIPEMAIL+'</p>';
				addr += '</div>';
				addr += '</div>';
				$('#shipadd').append(addr);
				
				document.form.SHIPPINGCUSTOMER.value = data.customer[0].CName;
				document.form.SHIPPINGID.value = data.customer[0].CID;
				$('#customerModal').modal('hide');
			}
		}
		});
}

function onIDGens()
{
	var urlStr = "/track/CreateCustomerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			action : "JAuto-ID",
			reurl : "createInvoice"
		},
		dataType : "json",
		success : function(data) {
			$("input[name ='CUST_CODE_C']").val(data.customer[0].CID);
			$("input[name ='CUST_CODE1_C']").val(data.customer[0].CID);			
		}
	});
}

function onIDGen()
{
	<%-- var rc="0";
	var gd="0";
	if(document.getElementById("REVERSECHARGE").checked == true)
		rc="1";
	if(document.getElementById("GOODSIMPORT").checked == true)
		gd="1";
	var idd = "<%=billHdrId%>";
 document.form1.action  = "/track/CreateSupplierServlet?action=Auto-ID&reurl=editSupplierCredit.jsp?action=View&BILL_HDR="+idd+"&REVERSECHARGE="+rc+"&GOODSIMPORT="+gd;
   document.form1.submit();  --%>
   var urlStr = "/track/CreateSupplierServlet";
	$.ajax( {
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		PLANT : "<%=plant%>",
		action : "JAuto-ID",
		reurl : "editSupplierCredit"
	},
	dataType : "json",
	success : function(data) {
		
		$("input[name ='CUST_CODE']").val(data.supplier[0].SID);
		$("input[name ='CUST_CODE1']").val(data.supplier[0].SID);
		
	}
	});

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

function OnTaxChange(TAXTREATMENT)
{
	
	if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	{
		$("#TaxLabel").addClass("required");
	}
	else
		$("#TaxLabel").removeClass("required");
	}
function OnBank(BankName)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_BANK_DATA",
			PLANT : "<%=plant%>",
			NAME : BankName,
		},
		success : function(dataitem) {
			var BankList=dataitem.BANKMST;
			var myJSON = JSON.stringify(BankList);						
			var dt = JSON.stringify(BankList).replace('[', '').replace(']', '');
			if (dt) {
			  var result = jQuery.parseJSON(dt);			  
			  var val = result.BRANCH;			  
			  $("input[name ='BRANCH']").val(val);
			}
		}
	});		
}

function bankCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#BANKNAME").typeahead('val', data.BANK_NAME);
		  $("input[name ='BRANCH']").val(data.BANK_BRANCH);
		
	}
}


function customertypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#CUSTOMER_TYPE_C").typeahead('val', data.CUSTOMER_TYPE_DESC);
		$("input[name=CUSTOMER_TYPE_ID]").val(data.CUSTOMER_TYPE_ID);
	}
}
/* //Resvi add Call Back For ProductDept
function productdepartmentCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=PRD_DEPT_DESC]").typeahead ('val',data.PRD_DEPT_DESC);
	}
} */

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
					 if(document.formcust.STATE.value==value.text)
						 	$('#SHIP_STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
					 else if(document.form.SHIPSTATE.value==value.text)
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
					 if(document.formcust.STATE.value==value.text)
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


$('select[name="COUNTRY_CODE_C"]').on('change', function(){		
    var text = $("#COUNTRY_CODE_C option:selected").text();
//     $("input[name ='COUNTRY_C']").val(text.trim());
	document.formcust.COUNTRY.value = text.trim();
});

$('select[name="COUNTRY_CODE_S"]').on('change', function(){		
	var text = $("#COUNTRY_CODE_S option:selected").text();
	// $("input[name ='COUNTRY']").val(text.trim());
	document.form1.COUNTRY.value = text.trim();
	});
	
$('select[name="COUNTRY_CODE"]').on('change', function(){		
    var text = $("#COUNTRY_CODE option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
});

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
<%@include file="newPaymentTermsModal.jsp" %>
<jsp:include page="newPaymentModeModal.jsp" flush="true"></jsp:include><!-- added imthi for payment mode -->
<jsp:include page="newCustomerTypeModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add customertype -->
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>

