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
	String title = "New Invoice";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String region = StrUtils.fString((String) session.getAttribute("REGION"));
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	CurrencyDAO currencyDAO = new CurrencyDAO();
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String compindustry = plantMstDAO.getcompindustry(plant);
	String ISPEPPOL = plantMstDAO.getisPeppol(plant);
	DateUtils _dateUtils = new DateUtils();
	String curDate = _dateUtils.getDate();
	String ORDERNO = StrUtils.fString(request.getParameter("DONO"));
	String custCode = StrUtils.fString(request.getParameter("CUST_CODE"));
	String CUSTOMER = StrUtils.fString(request.getParameter("CUST_NAME"));
	String NOOFORDER=((String) session.getAttribute("NOOFORDER")); /* Resvi */
	String empno = StrUtils.fString(request.getParameter("EMPNO"));
	String cmd = StrUtils.fString(request.getParameter("cmd"));
	String sTranId = StrUtils.fString(request.getParameter("TRANID")).trim();
	String fieldDesc=StrUtils.fString(request.getParameter("result"));
	String taxbylabel = ub.getTaxByLable(plant);
	String USERID= StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String systatus = StrUtils.fString((String) session.getAttribute("SYSTEMNOW"));
	String transports = StrUtils.fString(request.getParameter("TRANSPORTID"));
	String customertypec = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_IDC"));
	
	String sCustCode = StrUtils.fString(request.getParameter("sCustCode"));
	String sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT"));
	
	String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);
	String zerovalper = StrUtils.addZeroes(Float.parseFloat("0"), "3");
	
	boolean displaySaveAsOpen=true, displaySaveAsDraft=true;
	//displaySaveAsOpen = ub.isCheckVal("invoiceByOpen", plant,username);
	//displaySaveAsDraft = ub.isCheckVal("invoiceByDraft", plant,username);
	boolean displayCustomerpop=false,displayPaymentTypepop=false;
	
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displayCustomerpop = ub.isCheckValAcc("popcustomer", plant,USERID);
		displayCustomerpop =true; //Remove it after Adding check box in user access rights
	}
	String tbody = (String) request.getSession().getAttribute("TBODY");
	String OrdValidNumber="";//resvi
	
	String personIncharge = StrUtils.fString(request.getParameter("NAME"));
	String customertypedesc = StrUtils.fString(request.getParameter("CUSTOMERTYPEDESC"));
	String ctype = StrUtils.fString(request.getParameter("CTYPE"));	
	String fitem = StrUtils.fString(request.getParameter("FITEM"));
	String floc = StrUtils.fString(request.getParameter("FLOC"));
	String floc_type_id = StrUtils.fString(request.getParameter("FLOC_TYPE_ID"));
	String floc_type_id2 = StrUtils.fString(request.getParameter("FLOC_TYPE_ID2"));
	String fmodel = StrUtils.fString(request.getParameter("FMODEL"));
	String fuom = StrUtils.fString(request.getParameter("FUOM"));
	
	String detList = (String) request.getSession().getAttribute("DETLIST");
	if(detList == null){
		detList = "";
	}
	String companyregnumber=StrUtils.fString(request.getParameter("cus_companyregnumber"));
	String CURRENCYUSEQT="0",DISPLAY="",DISPLAYID="";
	List curQryList=new ArrayList();
	curQryList = currencyDAO.getCurrencyDetails(curency,plant);
	for(int i =0; i<curQryList.size(); i++) {
		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
		DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
		curency = DISPLAYID;
		DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
	    CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
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
	 

		//resvi starts
			InvoiceDAO invoiceDAO = new InvoiceDAO();
			String FROM_DATE = _dateUtils.getDate();
			if (FROM_DATE.length() > 5)
				FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";
			
			String TO_DATE = _dateUtils.getLastDayOfMonth();
			if (TO_DATE.length() > 5)
				TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			
			int noordvalid =invoiceDAO.Invoicecount(plant,FROM_DATE,TO_DATE);
			if(!NOOFORDER.equalsIgnoreCase("Unlimited"))
			{
				int convl = Integer.valueOf(NOOFORDER);
				if(noordvalid>=convl)
				{
					OrdValidNumber=NOOFORDER;
				}
			}
			//ends

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
	 
	 String gst = new selectBean().getGST("SALES",plant);
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.INVOICE%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/ConvertToInvoice.js"></script>
<style>
.invcbatch span.twitter-typeahead .tt-menu{
	min-width: 500px !important;
}
.invcbatch span.twitter-typeahead .tt-suggestion{
	text-align : center;
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
.phideshow{
    float: right;
    margin-top: -34px;
    position: relative;
    z-index: 2;
    margin-right: 40px;
}
</style>
<center>
	<h2>
		<small class="error-msg"><%=fieldDesc%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>	
                 <li><a href="../salesestimate/salescounter?PGaction=view&ITEM=<%=fitem%>&LOC=<%=floc%>&LOC_TYPE_ID=<%=floc_type_id%>&LOC_TYPE_ID2=<%=floc_type_id2%>&CUSTOMER=<%=custCode%>&MODEL=<%=fmodel%>&UOM=<%=fuom%>&CNAME=<%=CUSTOMER%>&TAXTREATMENT=<%=sTAXTREATMENT%>&NAME=<%=personIncharge%>&CUSTOMER_TYPE_ID=<%=customertypedesc%>"><span class="underline-on-hover">Sales Counter</span></a></li>
                <li><label>New Invoice</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="onback()">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		<div class="container-fluid">
			<input type="number" id="numberOfDecimal" style="display: none;"
				value=<%=numberOfDecimal%>>
				<input type="text" id="PageName" style="display: none;" value="Invoice">
			<form id="createBillForm" class="form-horizontal" name="form1"
				action="/track/InvoiceServlet?Submit=Save" method="post"
				enctype="multipart/form-data" onsubmit="return validateInvoice()">
				
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
				<%-- <input type = "hidden" name="GST" value="<%=gst%>"> --%>
				<input type = "hidden" name="custModal">
				<%-- <INPUT type="hidden" name="CURRENCYID"  value="<%=DISPLAYID%>">	 --%>
				<input type = "hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>">
				<INPUT type="hidden" name="ValidNumber" value="<%=OrdValidNumber%>">
				<INPUT type="hidden" name="curency"  value="<%=DISPLAYID%>">
				<INPUT type="hidden" name="CURRENCYID"  value="<%=DISPLAYID%>">
		       <INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>    
				
				<%if(!ispuloc){ %>
				 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
				<%} %>
				
				
				
				<div class="form-group customer-section">
					<label class="control-label col-form-label col-sm-2 required">Customer
						Name</label>
					<div class="col-sm-6 ac-box">
<!-- 						<div class="input-group"> -->
							<input type="text" class="ac-selected  form-control typeahead" onclick="removetaxtrestl()"
								id="CUSTOMER" placeholder="Select a customer" name="CUSTOMER" onchange="checkcustomer(this.value)"
								value="<%=CUSTOMER%>">
								 <span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i
								class="glyphicon glyphicon-menu-down"></i></span> 
								<%-- <span class="btn-danger input-group-addon"
								onclick="javascript:popUpWin('customer_list_issue_summary.jsp?TYPE=ACCTCUST&CUST_NAME='+form1.CUSTOMER.value);"><span
								class="glyphicon glyphicon-search" aria-hidden="true"></span></span> --%>
<!-- 						</div> -->
						<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" id="nTAXTREATMENT" name="nTAXTREATMENT" value="" style="width: 100%">
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
						<input type="text" name="plant" value="<%=plant%>" hidden>
						<input type="text" name="username" value=<%=username%> hidden>
						<input type="hidden" name="TRANSPORTID" value="">
						<INPUT type="hidden" name="CUST_CODE"  ID="CUST_CODE" value="<%=custCode%>">
						<INPUT type="hidden" name="EMPNO" value="<%=empno%>"> 
						<INPUT type="hidden" name="cmd" value="<%=cmd%>" /> 
						<INPUT type="hidden" name="TranId" value="<%=sTranId%>" />
						<INPUT type="hidden" name="STATE_PREFIX" value="" />
						<INPUT type = "hidden" name="TAXTREATMENT_VALUE" value ="<%=sTAXTREATMENT%>">
						<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
						<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
						
						<INPUT type="hidden" name="NAME" value="<%=personIncharge%>" />
						<INPUT type="hidden" name="CTYPE" value="<%=ctype%>" />
						
						<INPUT type="hidden" name="FITEM" value="<%=fitem%>" />
						<INPUT type="hidden" name="FLOC" value="<%=floc%>" />
						<INPUT type="hidden" name="FLOC_TYPE_ID" value="<%=floc_type_id%>" />
						<INPUT type="hidden" name="FLOC_TYPE_ID2" value="<%=floc_type_id2%>" />
						<INPUT type="hidden" name="FMODEL" value="<%=fmodel%>" />
						<INPUT type="hidden" name="FUOM" value="<%=fuom%>" />
						<INPUT type="hidden" name="SHIPPINGID" value="">
						<input type="hidden" name="SHIPPINGCUSTOMER" value="">	
						<INPUT type="hidden" name="ORIGIN" value="manual">	
						<INPUT type="hidden" name="DEDUCT_INV" value="1">	
						<input type="hidden" value="<%=displayCustomerpop%>" name="displayCustomerpop" id="displayCustomerpop" />
					<%=detList%>
					</div>
				</div>
				<div class="form-group shipCustomer-section" id="shipbilladd">
					<!-- <label class="control-label col-form-label col-sm-2">Shipping Address</label>
					<div class="col-sm-6 ac-box"> -->
					<div class="col-sm-2">
<!-- 						<div class="input-group"> -->
							<!-- <input type="text" class="ac-selected  form-control typeahead" onclick="removetaxtrestl()"
								id="SHIPPINGCUSTOMER" placeholder="Select a shipping address" name="SHIPPINGCUSTOMER" value=""> 
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'SHIPPINGCUSTOMER\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>  -->
								<!-- <span class="btn-danger input-group-addon"
								onclick="javascript:popUpWin('shippingdetails_list.jsp?SHIPPINGCUSTOMER='+form1.SHIPPINGCUSTOMER.value+'&FORMNAME=form1');">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
<!-- 						</div> -->
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Transport Mode:</label>
					<div class="col-sm-4 ac-box">
								<input type="text" class="form-control typeahead"
									id="transport" placeholder="Select a transport"
									name="transport" onchange="checktransport(this.value)" value="">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
								<!-- <span class="btn-danger input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
					</div>
				</div>
				<%if(compindustry.equalsIgnoreCase("Construction") || compindustry.equalsIgnoreCase("Service")){ %>	
				<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4 ac-box">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" 
									id="project" placeholder="Select a project" 
									name="project" onchange="checkproject(this.value)" value=""> 
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
				<%}else{%>
				<input type="hidden" name="project" value="">
				<%} %>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Invoice#</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="invoice" name="invoice" onchange="checkorderno(this.value)" onkeypress="return blockSpecialCharOrderNo(event)">
							<span class="input-group-addon"  onClick="onNewInvoice()">
					   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
					   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a>
	   		 				</span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">GINO#</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="gino" name="gino" value="" disabled>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Order
						Number</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control" id="ORDERNO"
							name="ORDERNO" value="<%=ORDERNO%>" disabled> <span
							class="select-icon"
							onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Invoice
						Date</label>
					<div class="col-sm-4">
						<input type="text" class="form-control datepicker"
							id="invoice_date" name="invoice_date" value="<%=curDate%>">
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Due
						Date</label>
					<div class="col-sm-4">
						<input type="text" class="form-control datepicker" id="due_date" placeholder="Select due date"
							name="due_date">
					</div>
						<div class="col-sm-6 no-padding">
					<label class="control-label col-form-label col-sm-5">Payment
						Terms</label>
					<div class="col-sm-6 ac-box">
						<input type="text" class="ac-selected form-control" placeholder="Select Payment Terms" onchange="checkpaymentterms(this.value)" value=""
							id="payment_terms" name="payment_terms"> <span
							class="select-icon"
							onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				</div>
				<div class="form-group employee-section">
					<label class="control-label col-form-label col-sm-2">Employee</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control typeahead"
							id="EMP_NAME" name="EMP_NAME" onchange="checkemployeess(this.value)" value="" placeholder="Select a employee">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">INCOTERM</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control typeahead"
								id="INCOTERM" name="INCOTERM" onchange="checkincoterms(this.value)" value="" placeholder="Select a INCOTERM">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'INCOTERM\']').focus()">
							<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				<%-- <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Sales Location</label>
			<div class="col-sm-4 ac-box">
				<!-- <input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" name="SALES_LOC" placeholder="Select a Sales Location">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="SALES_LOC" name="SALES_LOC" onchange='OnChange(form1.SALES_LOC);' style="width: 100%">
				<%
		   MasterUtil _MasterUtil=new  MasterUtil();
		   ArrayList ccList =  _MasterUtil.getSalesLocationList("",plant,"");
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String STATE = (String)m.get("STATE");
		        String STATE_PREFIX = (String)m.get("PREFIX"); %>
		        <option  value= <%=STATE_PREFIX%> ><%=STATE %> </option>		          
		        <%
       			}
			 %></SELECT>
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
						<input type="text" class="ac-selected form-control typeahead" id="CURRENCY" name="CURRENCY" onchange="checkcurrency(this.value)" placeholder="Select a Currency" value="<%=DISPLAY%>">
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
			   		 	<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" onkeypress="return isNumberKey(event,this,4)" value="<%=gst%>">
						<span class="sideiconspan"><p>%</p></span>

					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Salestax" name="Salestax" onchange="checktax(this.value)" value="" placeholder="Select a Tax">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'Salestax\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
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
								<option value="0">Tax Exclusive</option>
								<option value="1">Tax Inclusive</option>
							</select>
						</div>
					</div>
				</div>
				<div class="row" style="margin: 0px;">
					<table class="table table-bordered line-item-table bill-table"
						style="width: 100%;">
						<thead>
							<tr>
								<th colspan=2>Product Details</th>
								<th>Account</th>
								<th>UOM</th>
								<th>Location</th>
								<th>Batch</th>
								<th class="text-right">Qty</th>
								<th class="text-right">Unit Price</th>
								<th class="text-center">Discount</th>
								<!-- <th>Tax</th> -->
								<th class="text-center"colspan=2 style="width:13%">Amount</th>
							</tr>
						</thead>
						<tbody>
							<%=tbody%>
							
						</tbody>
					</table>
				</div>
				<!-- Total Details -->
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
										<sapn id="odtax">(Tax Inclusive)</sapn>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isodisctax" checked name="isodisctax" Onclick="isodisctaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right oddiscount" type="text" value="<%=zerovalper %>" readonly
												name="orderdiscount" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
											<!-- <span class="input-group-addon">%</span> -->
											<select class="discountPicker form-control"  name="oddiscount_type" id="oddiscount_type" disabled="true">
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
						<div class="total-row">
							<div class="total-label">
								<div class="row">
									<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Discount</div>
										<sapn id="dtax">(Tax Inclusive)</sapn>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isdtax" checked name="isdtax" Onclick="isdtaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right" type="text" value="<%=zeroval %>"
												name="discount" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
											<!-- <span class="input-group-addon">%</span>	-->
											<select class="discountPicker form-control"
												id="discount_type"  name="discount_type" onchange="calculateTotal()">
											<%-- 	<option value="<%=curency%>"><%=curency%></option>
												<option value="%">%</option> --%>
											</select>
										</div>
									</div>
								</div>
							</div>
							<div class="total-amount" style="line-height: 2;">
								<span id="discount"> 0.00 </span>
							</div>
						</div>
						<div class="total-row hightlight discountAccountSection"
							style="margin-top: 0px;" hidden>
							<div class="total-label supporting-acc-field"
								style="padding-bottom: 0;">
								<div class="row">
									<div class="col-lg-5 col-sm-5 col-5">
										<div class="form-control-plaintext required">Discount
											Account</div>
									</div>
									<div class="col-lg-6 col-sm-6 col-6">
										<input class="form-control discountAccountSearch" type="text"
											name="discount_account" placeholder="Select Account">
									</div>
								</div>
							</div>
						</div>
						<div class="total-row">
							<div class="badge-editable total-label">
								<div class="row">
									<div class="col-lg-4 col-sm-4 col-4">
										<div class="form-control-plaintext">Shipping Charge</div>
										<sapn id="shtax">(Tax Inclusive)</sapn>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isshiptax" name="isshiptax" Onclick="isshiptaxing(this)">
									</div>
									<div class="col-lg-6 col-sm-6 col-6">
										<input class="form-control text-right ember-view" type="text"
											name="shippingcost" onkeypress="return isNumberKey(event,this,4)" value="<%=zeroval%>">
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
							<div class="gross-total-removemargin"><!--  Author: Azees  Create date: July 16,2021  Description: Total of Local Currency-->
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
				<!-- Attach Files -->
				<div class="row grey-bg">
					<div class="col-sm-4">
						<div class="form-inline">
							<label for="email">Attach Files(s)</label>
							<div class="attch-section">
								<input type="file" class="form-control input-attch"
									id="billAttch" name="file" multiple="true">
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
						<p>Customer Notes</p>
						<div>
							<textarea rows="2" name="note" maxlength="990" 
								class="ember-text-area form-control ember-view"></textarea>
						</div>

						<p>Terms & Conditions</p>
						<div>
							<textarea rows="2" name="terms" maxlength="990"
								class="ember-text-area form-control ember-view"
								placeholder="Enter the terms and conditions of your business to be displayed in your transaction"></textarea>
						</div>
					</div>
				</div>
				<input id="sub_total" name="sub_total" value="" hidden> <input
					id="total_amount" name="total_amount" value="" hidden> <input
					name="Submit" value="Save" hidden> <input
					name="invoice_status" value="Save" hidden>
					<input id="taxamount" name="taxamount" value="" hidden>
				<div class="row">
					<div class="col-sm-12 txn-buttons">
						<%if(displaySaveAsDraft){ %>
						<button id="btnBillDraft" type="button" class="btn btn-default">Save as Draft</button>
						<%} if(displaySaveAsOpen){ %>
						<button id="btnBillOpen" type="button" class="btn btn-success">Save as Open</button>
						<%} %>
						<button type="button" class="btn btn-default"
							onclick="onback()">Cancel</button>
					</div>
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
				</div>
			</form>
		</div>
		<!-- Modal -->
		
		<%@include file="newEmployeeModal.jsp"%>
		<INPUT type="hidden" id="TaxByLabelOrderManagement"
			name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
		<%@include file="newProductModal.jsp"%>
		<%-- <%@include file="NewChartOfAccountAdd.jsp"%> --%>
		<%@include file="NewChartOfAccountpopup.jsp"%>
		<%@include file="newGstDetailModal.jsp"%>
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
								ccList1=new ArrayList();
								ccList1 = _MasterUtil1.getCountryList("", plant, region);
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

<!-- --------------modal---------------- -->

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
							<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG"  value="<%=region%>">
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
		
	<!-- 	                Author Name:Resviya ,Date:10/08/21 , Description -UEN Label     -->
         <div class="form-group" id="UEN">
	      <label class="control-label col-form-label col-sm-2 required" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
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
	         
	         
		
					<!-- 	                Author Name:Resviya ,Date:16/07/21 -->

                      <div class="form-group">
					  <label class="control-label col-form-label col-sm-2 required">Customer
							Currency</label>
					 <div class="col-sm-4">
					  <INPUT class="form-control" name="CUS_CURRENCY"  id="CUS_CURRENCY" type="TEXT" onchange="checkcustomercurrency(this.value)" value="<%=curency%>"  size="50" MAXLENGTH=100>
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

							</div> --%>

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
											onkeypress="return isNumber(event)" size="50" MAXLENGTH=50
											class="form-control">
									</div>
									
										<div class="col-sm-4">  
                                   <INPUT name="CUST_SHIP_WORKPHONE" id="CUST_SHIP_WORKPHONE" type="TEXT" value="" onkeypress="return isNumber(event)"	size="50" MAXLENGTH=50 class="form-control">
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
									   <div class="col-sm-4">
       <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="SHIPCUST_OnCountry(this.value)" id="CUST_SHIP_COUNTRY_CODE" name="CUST_SHIP_COUNTRY_CODE" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		    _MasterUtil1=new  MasterUtil();
		   ccList1 =  _MasterUtil1.getCountryList("",plant,region);
			for(int i=0 ; i<ccList1.size();i++)
      		 {
				Map m=(Map)ccList1.get(i);
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
		   ArrayList cvList =  _MasterUtil.getTaxTreatmentList("",plant,"");
			for(int i=0 ; i<cvList.size();i++)
      		 {
				Map m=(Map)cvList.get(i);
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
										Type</label>
									<div class="col-sm-4">
											<input id="PAYTERMS" name="PAYTERMS" class="form-control" type="text" onchange="checkcustomerpaymenttype(this.value)" value="" MAXLENGTH=100 placeholder="Select Payment Types">
		    						<span class="select-icon" onclick="$(this).parent().find('input[name=\'PAYTERMS\']').focus()">
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
										<INPUT name="CREDITLIMIT" type="TEXT" value="" " size="50"
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
									<input class="form-control text-left" name="USER_HPNO" type="text" placeholder="Enter User Phone No" maxlength="30" onkeypress="return isNumber(event)"  autocomplete="off" ></td>
									<td class="text-center">
									<input class="form-control text-left" name="USER_EMAIL" type="text" placeholder="Enter User Email" maxlength="50" autocomplete="off" ></td>
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
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" onchange="checkcustomerbank(this.value)" value="" style="width: 100%">
				<OPTION style="display:none;">Select Bank</OPTION>
				<%
		    _MasterUtil=new  MasterUtil();
		    ccList =  _MasterUtil.getBankList("",plant);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vNAME = (String)m.get("NAME"); %>
		        <option  value='<%=vNAME%>' ><%=vNAME %> </option>		          
		        <%
       			}
			 %></SELECT>
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


<script type="text/javascript">
$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip(); 
	 
    <!-- Author Name:Resviya ,Date:16/07/21 , Description: For Currency -->
	  var  curr = document.form1.CURRENCYID.value;		    
var basecurrency = '<%=curency%>';   
document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";
document.getElementById('lbltotal').innerHTML = "Total ("+curr+")";//  Author: Azees  Create date: July 16,2021  Description:  Total of Local Currency
if(basecurrency!=curr)//  Author: Azees  Create date: July 17,2021  Description:  Total of Local Currency
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

//Resvi Add for User Row removal-27.10.2021
$(".user-table tbody").on('click', '.user-action', function() {
	$(this).parent().parent().remove();
});
//Ends
	


     
	
	if(document.getElementById('nTAXTREATMENT').value=="")
	{
	if(document.getElementById('CUSTOMER').value=="")
		{
	$('#nTAXTREATMENT').attr('disabled',true);
	$('#nTAXTREATMENT').css("background-color", "#fffff");
		}
	}
else
	$('#nTAXTREATMENT').attr('disabled',false);

var TAXTREATMENT_VALUE = $("input[name ='TAXTREATMENT_VALUE']").val();
if(TAXTREATMENT_VALUE!="")
	document.getElementById('nTAXTREATMENT').value = TAXTREATMENT_VALUE;
	
    var d = "<%=taxbylabel%>";
    document.getElementById('TaxLabel').innerHTML = d +" No.";
    var cust = "<%=sCustCode%>";
    
    if(cust=="" || cust.length==0 ) {
    }else{
    	
    	$("input[name ='CUST_CODE']").val(cust);
    	 $('#customerModal').modal('show');
    }

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
			menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal"><a href="#"> + New Payment Type</a></div>');
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
function checkitem(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Customer ID!");
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
					ACTION : "CUSTOMER_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {

						alert("Customer ID Already Exists");
						document.getElementById("CUST_CODE").focus();
						//document.getElementById("ITEM").value="";
						return false;
					} else
						return true;
				}
			});
			return true;
		}
	}
	function isNumberKey(evt, element, id) {
		  var charCode = (evt.which) ? evt.which : event.keyCode;
		  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
			  {
		    	return false;
			  }
		  return true;
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
	 var CUST_CODE   = document.form.CUST_CODE_C.value;
	 var CUST_NAME   = document.form.CUST_NAME_C.value;
	 var PEPPOL_C  = document.form.PEPPOL_C.value;
	 var PEPPOL_IDC   = document.form.PEPPOL_IDC.value;
	 var cus_companyregnumber   = document.form.cus_companyregnumber.value;
	 var CL   = document.form.CREDITLIMIT.value;
	 var TAXTREATMENT   = document.form.TAXTREATMENT.value;
	 var RCBNO   = document.form.RCBNO.value;
	 var rcbn = RCBNO.length;
	 var region = document.form.COUNTRY_REG.value;
	 if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE_C.focus(); return false; }
	 if(CUST_NAME == "" || CUST_NAME == null) {
		 alert("Please Enter Customer Name"); 
		 document.form.CUST_NAME.focus();
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


//   Author Name:Resviya ,Date:9/08/21 , Description -UEN Alert    

	   if(region == "GCC"){
		   document.form.cus_companyregnumber.value="";
		}else if(region == "ASIA PACIFIC"){
			if (cus_companyregnumber == "" || cus_companyregnumber == null) {
			alert("Please Enter Unique Entity Number (UEN)");
			document.form.cus_companyregnumber.focus();
			return false; 
			}
		}

//	   END

//	 <!-- Author Name:Resviya ,Date:16/07/21 -->

	 if(CURRENCY == "" || CURRENCY == null) {
		 alert("Please Enter Currency ID"); 
		 document.form.CUS_CURRENCY.focus();
		 return false; 
		 }
//	 End

	 if(document.form.TAXTREATMENT.selectedIndex==0)
		{
		alert("Please Select TAXTREATMENT");
		document.form.TAXTREATMENT.focus();
		return false;
		}
	 if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	   {
	   var  d = document.getElementById("TaxByLabel").value;
	   	if(RCBNO == "" || RCBNO == null) {
	   		
		   alert("Please Enter "+d+" No."); 
		   document.form.RCBNO.focus();
		   return false; 
		   }
	   	//if(document.form1.COUNTRY_REG.value=="GCC")// region based validtion
		//{
		if(!IsNumeric(RCBNO))
		{
	    alert(" Please Enter "+d+" No. Input In Number"); 
	   	document.form.RCBNO.focus();
	   	return false; 
	  	}

		if("15"!=rcbn)
		{
		alert(" Please Enter your 15 digit numeric "+d+" No."); 
			document.form.RCBNO.focus();
			return false; 
			}
		//}
	   }
	else if(50<rcbn)
	{
	   var  d = document.getElementById("TaxByLabel").value;
	   alert(" "+d+" No. length has exceeded the maximum value"); 
	   document.form.RCBNO.focus();
	   return false; 
	 }

	 if(CL < 0) 
	 {
		   alert("Credit limit cannot be less than zero");
		   document.form.CREDITLIMIT.focus(); 
		   return false; 
		   }	 
	// alert(isCL);
	
	 //alert("2nd");
	 if(!IsNumeric(CL))
	 {
	   alert(" Please Enter Credit Limit Input In Number");
	   form.CREDITLIMIT.focus();  
	   form.CREDITLIMIT.select(); 
	   return false;
	 }
	 if(!IsNumeric(form.PMENT_DAYS.value))
	 {
	   alert(" Please Enter Days Input In Number");
	   form.PMENT_DAYS.focus();  form.PMENT_DAYS.select(); return false;
	 }
	 
	 
	//  alert(CL);
	 /* if(this.form.ISCREDITLIMIT.checked == true && CL == ""){
		   alert("Please Enter Credit Limit");
		   document.form.CREDITLIMIT.focus();
		   return false; 
	 } */
	 if(document.form.COUNTRY_CODE_C.selectedIndex==0)
		{
		   alert("Please Select Country from Address");
		   document.form.COUNTRY_CODE_C.focus();
		 return false;
		}
	 /* if(isCL.equals("1") && CL.equals(""))
		  {
			  alert("Please Enter Credit Limit"); 
			   document.form.CREDITLIMIT.focus();
			   return false; 
		  }	 */
	 
	 /* document.form.action  = "/track/CreateCustomerServlet?action=ADD&reurl=createInvoice";
	 document.form.submit(); */
	 var datasend = $('#formCustomer').serialize();
	   
	  
	   var urlStr = "/track/CreateCustomerServlet?action=JADD&reurl=createInvoice";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : datasend,
		dataType : "json",
		success : function(data) {
			if(data.customer[0].STATUS=="FAILED")
			{
				alert(data.customer[0].MESSAGE);
			}else{
			if(document.form1.custModal.value == "cust"){
			//alert(JSON.stringify(data));
			$("input[name ='CUST_CODE']").val(data.customer[0].CID);
			//document.getElementById("CUST_CODE").value  = data.customer[0].CID;
			document.getElementById("CUSTOMER").value  = data.customer[0].CName;
			$("input[name ='CUSTOMER']").val(data.customer[0].CName);
			$("input[name ='transport']").val(data.customer[0].TRANSPORTNAME);
			$("input[name ='TRANSPORTID']").val(data.customer[0].TRANSPORTID);
			$("input[name ='TAXTREATMENT_VALUE']").val(data.customer[0].CTAXTREATMENT);
			getCurrencyid(data.customer[0].CURRENCY,data.customer[0].CURRENCY_ID,data.customer[0].CURRENCYUSEQT);
			document.getElementById('nTAXTREATMENT').value = data.customer[0].CTAXTREATMENT;

			$('#shipbilladd').empty();
			var addr = '<div class="col-sm-2"></div>';
			addr += '<div class="col-sm-5" style="line-height: 7px;font-size: 13px;"><h5 style="font-weight: bold;">Billing Address</h5>';
			addr += '<p>'+data.customer[0].CNAME+'</p>';
			addr += '<p>'+data.customer[0].ADDR1+' '+data.customer[0].ADDR2+'</p>';
			addr += '<p>'+data.customer[0].ADDR3+' '+data.customer[0].ADDR4+'</p>';
			addr += '<p>'+data.customer[0].STATE+'</p>';
			addr += '<p>'+data.customer[0].COUNTRY+' '+data.customer[0].ZIP+'</p>';
			addr += '<p>'+data.customer[0].HPNO+'</p>';
			addr += '<p>'+data.customer[0].EMAIL+'</p>';
			addr += '</div>';
			addr += '<div class="col-sm-5" style="line-height: 7px;font-size: 13px;"><h5 style="font-weight: bold;">Shipping Address   <a><span data-toggle="modal" data-target="#shipaddr" style="font-size: 15px;font-weight: 450;">Change</span></a></h5>';
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
			$('#shipbilladd').append(addr);
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
			
			$("input[name=payment_terms]").val(data.customer[0].PAYMENT_TERMS);
			$("input[name=SHIPPINGID]").val(data.customer[0].CID);
			$("input[name=SHIPPINGCUSTOMER]").val(data.customer[0].CName);
			
			document.form.reset();
			$('#customerModal').modal('hide');
		}
		else{
			document.form.reset();
			document.form1.SHIPPINGCUSTOMER.value = data.customer[0].CName;
			document.form1.SHIPPINGID.value = data.customer[0].CID;
			$('#customerModal').modal('hide');
		}
		}
		}
		});
	}
	
	function onIDGen()
	{
	 /* document.form.action  = "/track/CreateCustomerServlet?action=Auto-ID&reurl=createInvoice";
	 document.form.submit(); */
		var urlStr = "/track/CreateCustomerServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : "<%=plant%>",
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

function transportCallback(data){
	if(data.STATUS="SUCCESS"){
	if(document.form1.custModal.value=="cust"){
		$("#transports").typeahead('val', data.transport);
		$("input[name=TRANSPORTSID]").val(data.TRANSPORTID);
		}else if(document.form1.custModal.value=="shipcust"){
			$("#transports").typeahead('val', data.transport);
			$("input[name=TRANSPORTSID]").val(data.TRANSPORTID);
			}else {
		$("#transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
		}
	}
}

function payTermsCallback(payTermsData){
	if(payTermsData.STATUS="SUCCESS"){
	if(document.form1.custModal.value=="cust")
	{
		$("input[name ='cpayment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=PMENT_DAYS]").val(payTermsData.NO_DAYS);
	} else {
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
	}
  }
}

function customertypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#CUSTOMER_TYPE_C").typeahead('val', data.CUSTOMER_TYPE_DESC);
		$("input[name=CUSTOMER_TYPE_ID]").val(data.CUSTOMER_TYPE_ID);
	}
}

function paymentTypeCallback(payTermsData)
{
	if(payTermsData.STATUS="100"){
		$("input[name ='PAYTERMS']").typeahead('val',payTermsData.PAYMENTMODE);
	}
}
function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
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
function checkManagerApp(obj){
	var manageapp = $(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val();
	if(manageapp == 0)
	$(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val(1);
	else
	$(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val(0);

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




function onback(){
	window.location.href="../salesestimate/salescounter?PGaction=view&ITEM=<%=fitem%>&LOC=<%=floc%>&LOC_TYPE_ID=<%=floc_type_id%>&LOC_TYPE_ID2=<%=floc_type_id2%>&CUSTOMER=<%=custCode%>&MODEL=<%=fmodel%>&UOM=<%=fuom%>&CNAME=<%=CUSTOMER%>&TAXTREATMENT=<%=sTAXTREATMENT%>&NAME=<%=personIncharge%>&CUSTOMER_TYPE_ID=<%=customertypedesc%>";
}
</script>
<%@include file="newPaymentTermsModal.jsp"%>
<%@include file="../jsp/newTransportModeModal.jsp"%> <!-- imti for add transport -->
<%@include file="../jsp/newPaymentModeModal.jsp"%><!-- thansith Add for paymentmode -->
<%@include file="../jsp/newCustomerTypeModal.jsp"%> <!-- Thanzith for add customertype -->
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>