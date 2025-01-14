<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@page import="com.track.db.object.FinCountryTaxType"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<%
String title = "New Expenses";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String region = StrUtils.fString((String) session.getAttribute("REGION"));
String transports = StrUtils.fString(request.getParameter("TRANSPORTID"));
String customertypec = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_IDC"));
String SUPPLIER_TYPE_ID = StrUtils.fString(request.getParameter("SUPPLIER_TYPE_IDS"));
//Validate no.of Customers -- Azees 15.11.2020
	CustMstDAO custdao = new CustMstDAO();
	CustomerBeanDAO venddao = new CustomerBeanDAO();
	String NOOFCUSTOMER=((String) session.getAttribute("NOOFCUSTOMER"));
	String NOOFSUPPLIER=((String) session.getAttribute("NOOFSUPPLIER"));
	String ValidNumber="",SupValidNumber="";
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
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String cmd =StrUtils.fString(request.getParameter("cmd"));
String sTranId = StrUtils.fString(request.getParameter("TRANID")).trim();
String Urlred="../expenses/summary";
if(cmd.equalsIgnoreCase("Edit"))
{
	title = "Edit Expenses";
	Urlred="../expenses/detail?TRANID="+sTranId;
}
if(cmd.equalsIgnoreCase("Copy"))
{
	title = "Copy Expenses";
	Urlred="../expenses/detail?TRANID="+sTranId;
}
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
PlantMstDAO plantMstDAO = new PlantMstDAO();
CurrencyDAO currencyDAO = new CurrencyDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String basecurrency = plantMstDAO.getBaseCurrency(plant);
DateUtils _dateUtils = new DateUtils();
String curDate =_dateUtils.getDate();

String taxbylabel = ub.getTaxByLable(plant);
String sVendorCode = StrUtils.fString(request.getParameter("sCustCode"));
String vendno = StrUtils.fString(request.getParameter("VENDNO"));
String vendName = StrUtils.fString(request.getParameter("VEND_NAME"));
String sTAXTREATMENT = StrUtils.fString(request.getParameter("TAXTREATMENT"));
String sREVERSECHARGE = StrUtils.fString(request.getParameter("REVERSECHARGE"));
String sGOODSIMPORT = StrUtils.fString(request.getParameter("GOODSIMPORT"));
String companyregnumber=StrUtils.fString(request.getParameter("cus_companyregnumber"));
String CURRENCYUSEQT="0",DISPLAY="",DISPLAYID="";
String Supcurrency=plantMstDAO.getBaseCurrency(plant);
List curQryList=new ArrayList();
curQryList = currencyDAO.getCurrencyDetails(curency,plant);
for(int i =0; i<curQryList.size(); i++) {
	ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
	DISPLAYID = StrUtils.fString(StrUtils.removeQuotes((String) arrCurrLine.get(0)));
	DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
    CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
    }
ExpensesDAO expenseDAO=new ExpensesDAO();
ExpensesUtil expensesUtil = new ExpensesUtil();
List AttachList=new ArrayList<>();
FinCountryTaxType fintaxtype= new FinCountryTaxType();
if(cmd.equalsIgnoreCase("Edit"));
{
	if(sTranId!="")
	{
		 Hashtable ht=new Hashtable();
		    ht.put("ID", sTranId);
		    ht.put("PLANT", plant);
		    AttachList=expenseDAO.getExpenseAttachByHrdId(ht);
		    
		    Hashtable hts = new Hashtable();
		    hts.put("ID", sTranId);
		    List expenselisthdrdet = expensesUtil.getConvExpensesforDetails(hts, plant);
		    Map expenselist=(Map)expenselisthdrdet.get(0);
		    String taxidsting = (String) expenselist.get("TAXID");
		    int taxid = Integer.valueOf(taxidsting);
		    FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
		    fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxid);
	}
}

if(cmd.equalsIgnoreCase("Copy"));
{
	if(sTranId!="")
	{
		 Hashtable ht=new Hashtable();
		    ht.put("ID", sTranId);
		    ht.put("PLANT", plant);
		    AttachList=expenseDAO.getExpenseAttachByHrdId(ht);
		    
		    Hashtable hts = new Hashtable();
		    hts.put("ID", sTranId);
		    List expenselisthdrdet = expensesUtil.getConvExpensesforDetails(hts, plant);
		    Map expenselist=(Map)expenselisthdrdet.get(0);
		    String taxidsting = (String) expenselist.get("TAXID");
		    int taxid = Integer.valueOf(taxidsting);
		    FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
		    fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(taxid);
	}
}

String gst = sb.getGST("STANDARD RATE",plant);
gst=StrUtils.addZeroes(Double.parseDouble(gst), "3");
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
String ISPEPPOL = plantMstDAO.getisPeppol(plant);
CURRENCYUSEQT = StrUtils.addZeroes(Double.parseDouble(CURRENCYUSEQT), numberOfDecimal);
String shipContactName="",shipDesgination="",shipWORKPHONE="",shipHpNo="",shipEmail="",shipCountry="",shipAddr1="",shipAddr2="",shipAddr3="",shipAddr4="",shipState="",shipZip="",SameAsContactAddress="";


%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.EXPENSES%>"/>
</jsp:include>
<style>
 .select2drop
 {
 width:487px !important;
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
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<link rel="stylesheet" href="dist/css/select2.min.css">
<script src="dist/js/select2.full.min.js"></script>
<script src="../jsp/js/Expenses.js"></script>
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../expenses/summary"><span class="underline-on-hover">Expenses Summary</span> </a></li>
                <% if(cmd.equalsIgnoreCase("Edit")){ %>
               <li><a href="<%=Urlred %>"><span class="underline-on-hover">Expense Detail</span> </a></li>
                <%}else if(cmd.equalsIgnoreCase("Copy")){%>  
                <li><a href="<%=Urlred %>"><span class="underline-on-hover">Expense Detail</span> </a></li>
                 <%}%> 
                 <li><label><%=title %></label></li>                                  
            </ul>             
            
    <!-- Muruganantham Modified on 16.02.2022 -->  
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='<%=Urlred %>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>

	<div class="container-fluid">
	<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
	<form id="createExpensesForm" class="form-horizontal" name="form1" autocomplete="off" action="/track/ExpensesServlet?Submit=Save"  method="post" enctype="multipart/form-data" onsubmit="return validateExpenses()">			
		
		<div class="form-group" id="hpurord">
					
			<div class="col-sm-2">
			<label class="control-label col-form-label">PO Number</label>
			</div>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="pono" name="pono" disabled="true" onkeypress="return blockSpecialCharOrderNo(event)">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'pono\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				    </div>  
				<div class="col-sm-4">
				<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="expenses_for_PO" name="expenses_for_PO" Onclick="checkval()">
				Expenses For PO</lable>
				</div>
		</div>
		<div class="form-group" id="hbill" hidden>
			<label class="control-label col-form-label col-sm-2 required">Bill</label>
			<div class="col-sm-4">
				<input type="text" class="form-control"  id="bill" name="bill" READONLY>
			</div>
		</div>
		
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Shipment Reference</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="shipment" name="shipment" disabled="true">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'shipment\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		
	<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Bill No</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" value="" id="exbillno" name="exbillno">
			</div>
			<label class="control-label col-form-label col-sm-2 required">Date</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" value="<%=curDate%>" id="expenses_date" name="expenses_date" READONLY>
			</div>
	</div>
		
	<div class="form-group vendor-section">
			<label class="control-label col-form-label col-sm-2 required">Supplier Name</label>
			<div class="col-sm-4 ac-box">
<!-- 				<div class="input-group">  -->
				<input type="text" class="ac-selected  form-control typeahead" id="vendname" placeholder="SUPPLIER" name="vendname" onchange="checksupplier(this.value)" value="<%=vendName%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'vendname\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('vendor_list.jsp?AUTO_SUGG=Y&CUST_NAME='+form1.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
<!-- 				</div> -->
				<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" onchange="OnChkTaxChange(this.value)" data-placement="right" id="nTAXTREATMENT" name="nTAXTREATMENT" value="" style="width: 100%">
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
			 <div id="CHK1">        
      	<input type = "checkbox" class="check" id = "REVERSECHARGE" name = "REVERSECHARGE" <%if(sREVERSECHARGE.equals("1")) {%>checked <%}%>/><b>&nbsp;This transaction is applicable for reverse charge </b>
      	</br>
      	<input type = "checkbox" class="check" id = "GOODSIMPORT" name = "GOODSIMPORT" <%if(sGOODSIMPORT.equals("1")) {%>checked <%}%>/><b>&nbsp;This transaction is applicable for goods import &nbsp; &nbsp; </b>
      	</div>
			</div>
				<div class="col-sm-4">
				<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="paid" name="paid" Onclick="changePaid()">
				Paid</lable>
				</div>
				
		</div>	
		
	<input type="hidden" name="PROJECTID" value="0">
	<input type="text" name="username" value=<%=username%> hidden>
	<input type="hidden" name="vendno" id="evendcode"  value="<%=vendno%>">	
	<input type="text" name="plant" value="<%=plant%>" hidden>
	<INPUT type = "hidden" name="CUST_CODE" id="ecustcode">
	<INPUT type="hidden" name="cmd" value="<%=cmd%>" />
			<INPUT type="hidden" name="CURRENCYID" value="<%=DISPLAYID%>">
			<INPUT type="hidden" name="TranId" value="<%=sTranId%>" />
			<INPUT type = "hidden" name="TAXTREATMENT_VALUE" value ="<%=sTAXTREATMENT%>">
			<%if(cmd.equalsIgnoreCase("Edit")){ %>
				<input type = "hidden" name="ptaxtype" value="<%=fintaxtype.getTAXTYPE()%>">
				<input type = "hidden" name="ptaxpercentage" value="">
				<input type = "hidden" name="ptaxdisplay" value="">
				<input type = "hidden" name="ptaxiszero" value="<%=fintaxtype.getISZERO()%>">
				<input type = "hidden" name="ptaxisshow" value="<%=fintaxtype.getSHOWTAX()%>">
				<input type = "hidden" name="taxid" value="">
				<input type="hidden" name="CURRENCYUSEQTOLD" value="">
								<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>
				
			<%}else if(cmd.equalsIgnoreCase("Copy")){ %>
				<input type = "hidden" name="ptaxtype" value="<%=fintaxtype.getTAXTYPE()%>">
				<input type = "hidden" name="ptaxpercentage" value="">
				<input type = "hidden" name="ptaxdisplay" value="">
				<input type = "hidden" name="ptaxiszero" value="<%=fintaxtype.getISZERO()%>">
				<input type = "hidden" name="ptaxisshow" value="<%=fintaxtype.getSHOWTAX()%>">
				<input type = "hidden" name="taxid" value="">
				<input type="hidden" name="CURRENCYUSEQTOLD" value="">
								<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>
				
			<%}else{ %>
				<input type = "hidden" name="ptaxtype" value="">
				<input type = "hidden" name="ptaxpercentage" value="0">
				<input type = "hidden" name="ptaxdisplay" value="">
				<input type = "hidden" name="ptaxiszero" value="1">
				<input type = "hidden" name="ptaxisshow" value="0">
				<input type = "hidden" name="taxid" value="0">
				<input type="hidden" name="CURRENCYUSEQTOLD" value="<%=CURRENCYUSEQT%>">
				<INPUT type="hidden" name="curency" value="<%=DISPLAYID%>">
				<INPUT type="hidden" name="BASECURRENCYID"  value="<%=curency%>"> <%--Resvi--%>
			<%} %>
				
		<div class="form-group" style="display:none" id="paidthroughfield">
			<label class="control-label col-form-label col-sm-2 required">Paid Through</label>
			<div class="col-sm-4">				
				<input type="text" id="paid_through_account_name" name="paid_through_account_name" class="form-control">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'paid_through_account_name\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
	
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Currency</label>
			<div class="col-sm-4">				
				<input type="text" class="form-control" id="currency" name="currency" value="<%=DISPLAY%>" onchange="checkcurrency(this.value)" value="">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'currency\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>				
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
						<%-- <div class="input-group">
							<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" value="<%=gst%>">
							<span class="input-group-addon" style="font-size: 15px; color: #0059b3">
								<b>%</b>
							</span>
			   		 	</div> --%>
			   		 	<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" onkeypress="return isNumberKey(event,this,4)" value="<%=gst%>">
						<span class="sideiconspan"><p>%</p></span>

					</div>
				</div>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Expensetax" name="Expensetax" onchange="checktax(this.value)" placeholder="Select a Tax">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'Expensetax\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
						
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Expense <%=taxbylabelordermanagement%> :</label><!-- this is dynamic value -->
					<div class="col-sm-4">
						<%-- <div class="input-group">
							<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" value="<%=gst%>">
							<span class="input-group-addon" style="font-size: 15px; color: #0059b3">
								<b>%</b>
							</span>
			   		 	</div> --%>
			   		 	<input type="text" class="form-control" id="EXGST" name="EXGST"  onchange="changingetaxpercentage()" onkeypress="return isNumberKey(event,this,4)" value="<%=gst%>">
						<span class="sideiconspan"><p>%</p></span>

					</div>
				</div>
		
		
		<div class="row" style="margin:0px;">
			<table class="table table-bordered line-item-table bill-table" style="width:95%;">
			<thead>
			  <tr>
				<th class="bill-desc" >Expenses Account</th>
				<th class="bill-acc">Notes</th>								
				<th class="item-amount text-right">Amount</th>
				<th class="item-tax">Tax</th>
			  </tr>
			</thead>
			<tbody>
			  <tr>
				<td class="bill-acc">
					<input type="hidden" name="isexptax" value="0">
					<input type="text" name="expenses_account_name" class="form-control expensesaccountSearch" placeholder="Select Account">
				</td>
				<td  class="col-sm-6 notes-sec">
					<textarea rows="2" name="note" class="ember-text-area form-control ember-view" maxlength="300"></textarea>
				</td>
				<td class="item-amount text-right">
				<input name="amount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
				</td>
				<td class="item-tax">
					<input type="hidden" name="tax_type">
					<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" readonly>
				</td>				
				
			  </tr>
			</tbody>
			</table>
		</div>
		<div class="row">
			<div class="col-sm-6">
				<a style="text-decoration:none;cursor: pointer;" onclick="addRow()">+ Add another line</a>
			</div>
			<div class="total-section col-sm-5">
				<div class="total-row sub-total">
					<div class="total-label"> Sub Total <br> 
						<span class="productRate" hidden>(Tax Inclusive)</span> 
					</div> 
					<div class="total-amount" id="subTotal">0.00</div>
				</div>
				
				<div class="taxDetails">
				</div>
				<div class="etaxDetails">
				</div>
				<div class="accountpayable">
				</div>
							
				<div class="total-section total-row">
							<div class="gross-total-removemargin"><!--  Author: Resvi  Add date: July 27,2021  Description: Total of Local Currency-->
								<div class="total-label"><label id="lbltotal"></label></div>
								<div class="total-amount" id="total">0.00</div>
							</div>
				</div>
			<div class="total-section total-row" id="showtotalcur">
					<div class="gross-total">
						<div class="total-label"> Total (<%=curency%>) </div> 
						<div class="total-amount" id="totalcur">0.00 </div>
					</div>
				</div>
		</div>
		
			
						
			<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Reference</label>
				<div class="col-sm-4">
					<input type="text" class="form-control" id="reference" name="reference" maxlength="100">
				</div>
		   </div>
		   
		   <div class="form-group customer-section">
			<label class="control-label col-form-label col-sm-2" id="cusnamereq">Customer Name</label>
			<div class="col-sm-4 ac-box">
<!-- 				<div class="input-group">  -->
				<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" placeholder="Select a customer" onchange="checkcustomer(this.value)" value="" name="CUSTOMER">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onclick="javascript:popUpWin('customer_list_issue_summary.jsp?CUST_NAME='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
<!-- 				</div> -->
				</div>
					<div class="col-sm-4">
				<input type="hidden" name="billable_status" value="0">
				<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="billable" name="billable" Onclick="checkbillable()">
				Billable</lable>
				</div>	
			</div>
			<% if(COMP_INDUSTRY.equals("Construction") || COMP_INDUSTRY.equalsIgnoreCase("Service")) {%>
			<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4 ac-box">
								<input type="text" class="form-control typeahead" 
									id="project" placeholder="Select a project" 
									name="project" onchange="checkproject(this.value)" value=""> 
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'project\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
								<!-- <span class="btn-danger input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
					</div>
				</div>
			<% } %>
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
				<%if(AttachList.size()>0){ %>
						<div id="billAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=AttachList.size()%> files Attached</a>
									<div class="tooltiptext">
										
										<%for(int i =0; i<AttachList.size(); i++) {   
									  		Map attach=(Map)AttachList.get(i); %>
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
			<!-- <div class="col-sm-6 notes-sec">
				<p>Notes <span class="text-muted">(For Internal Use)</span></p>
				<div> <textarea rows="2" name="note" class="ember-text-area form-control ember-view"></textarea> </div>
			</div> -->
		</div>
	
		<input id="sub_total" name="sub_total" value="" hidden>
		<input id="total_amount" name="total_amount" value="" hidden>
		<input id="total_tax_amount" name="total_tax_amount" value="" hidden>
		<input id="total_etax_amount" name="total_etax_amount" value="" hidden>
		<input name="Submit" value="Save" hidden>
		<input name="bill_status" value="Save" hidden>
		<div class="row">
			<div class="col-sm-12 txn-buttons">
				<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			</div>
		</div>
	</form>
</div>
	<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
	<!-- Modal -->
	<%-- <%@include file="newSupplierModal.jsp" %> --%>
	<%-- <%@include file="NewChartOfAccountAdd.jsp"%> --%>
	<%@include file="NewChartOfAccountpopup.jsp"%>
	<%-- <%@include file="NewExpenseAccountModal.jsp"%> --%>
	<%-- <%@include file="newCustomerModal.jsp" %> --%>
	<%@include file="newShipmentModal.jsp" %>
	<%@include file="newGstDetailModal.jsp" %>
	<!-- Modal -->
	<!-- <div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog">	
	    Modal content
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Modal Header</h4>
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

<div id="supplierModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form class="form-horizontal" name="formsupplier" id="formsupplierid" method="post">
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
								<INPUT class="form-control" name="CUST_CODE" id="CUST_CODE" onchange="checksupplierid(this.value)" onkeypress="return blockSpecialChar(event)"
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
							<INPUT type="hidden" name="CURRENCYID_S" value="<%=DISPLAYID%>">	<!--Resvi -->
							<input type="hidden" name="TRANSPORTSID" value="<%=transports%>">
							<input name="SUPPLIER_TYPE_ID" type="hidden" value="<%=SUPPLIER_TYPE_ID%>">
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
	         
					
					<!-- 	                Author Name:Resviya ,Date:15/07/21 -->

                      <div class="form-group">
					  <label class="control-label col-form-label col-sm-2 required">Supplier
							Currency</label>
					 <div class="col-sm-4">
					  <INPUT class="form-control" name="SUP_CURRENCY"  id="SUP_CURRENCY" type="TEXT" value="<%=Supcurrency%>"  size="50" MAXLENGTH=100>
					 </div>
					 </div>
					 
<!-- 					 End -->

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2"
							for="supplier type">Supplier Group</label>
						<div class="col-sm-4">
								<input name="SUPPLIER_TYPE_S" id=SUPPLIER_TYPE_S type="TEXT" placeholder="Select a Supplier Group"
									value="" size="50" MAXLENGTH=50
									class="form-control"> <span class="select-icon"  onclick="$(this).parent().find('input[name=\'SUPPLIER_TYPE_S\']').focus()"> 	
   		 						<i class="glyphicon glyphicon-menu-down"></i></span>
								</span>
						</div>
					</div>
					
					<div class="form-group">
      					<label class="control-label col-form-label col-sm-2" for="transmode">Transport Mode</label>
      						<div class="col-sm-4">           	
    							<input name="transports" id="transports" type="TEXT"  placeholder="Select a transport" onchange="checksuppliertransport(this.value)" value="" size="50" MAXLENGTH=50 class="form-control">
   		 						<span class="select-icon" onclick="$(this).parent().find('input[name=\'transports\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
								</span>
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
								data-toggle="tab">Contact Details</a></li>
							<li class="nav-item"><a href="#home" class="nav-link"
								data-toggle="tab">Address</a></li>
								<li class="nav-item"><a href="#bank_sup" class="nav-link" 
								data-toggle="tab">Bank Account Details</a></li>
							<li class="nav-item"><a href="#remark" class="nav-link"
								data-toggle="tab">Remarks</a></li>
						</ul>
						<div class="tab-content clearfix">
							<div class="tab-pane fade" id="home">
								<br>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2 required"
										for="Country">Country</label>
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
									<label class="control-label col-form-label col-sm-2"
										for="State">State</label>
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
											onkeypress="return isNumber(event)" size="50" MAXLENGTH=30
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
							<SELECT class="form-control" data-toggle="dropdown" data-placement="right"onchange="OnTaxChange(this.value)" id="TAXTREATMENT" name="TAXTREATMENT" style="width: 100%">
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
        				<label class="control-label col-form-label col-sm-2" for="Payment Terms">Payment Mode</label>
      				<div class="col-sm-4">
     		 			<input id="SPAYTERMS" name="SPAYTERMS" class="form-control" type="text" onchange="checksupplierpaymenttype(this.value)" value="" MAXLENGTH=100 placeholder="Select Payment Mode">
		    			<span class="select-icon" onclick="$(this).parent().find('input[name=\'SPAYTERMS\']').focus()">
						<i class="glyphicon glyphicon-menu-down"></i></span>
			  		</div>
  					</div>  
  					<div class="form-group">
       					<label class="control-label col-form-label col-sm-2">Payment Terms</label>
       				<div class="col-sm-4">
       					<input type="text" class="form-control" id="sup_payment_terms"	name="sup_payment_terms" onchange="checksupplierpaymentterms(this.value)" value="" placeholder="Select Payment Terms">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'sup_payment_terms\']').focus()">
						<i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
					<div class="form-inline">
    					<label class="control-label col-form-label col-sm-1">Days</label>
    	    			<input name="SUP_PMENT_DAYS" type="TEXT" value="" class="form-control" size="5" MAXLENGTH=10 readonly>
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
							
							<div class="tab-pane fade" id="bank_sup">
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
	<INPUT name="BANKNAME" type = "TEXT" value="" id="BANKNAME" onchange="checksupplierbank(this.value)"class="form-control"   placeholder="BANKNAME">
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
											size="50" MAXLENGTH=1000 class="form-control ">
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


<!-- --------------modal---------------- -->
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
									type="TEXT" value="" onchange="checkitem_c(this.value)" size="50"
									MAXLENGTH=50 width="50"> <span
									class="input-group-addon" onClick="onIDGenCust();"> <a
									href="#" data-toggle="tooltip" data-placement="top"
									title="Auto-Generate"> <i class="glyphicon glyphicon-edit"
										style="font-size: 20px;"></i></a></span>
							</div>
							<INPUT type="hidden" name="CUST_CODE1_C" value="">
							<INPUT type="hidden" name="COUNTRY" value="">
							<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
							<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">
		                     <INPUT type="hidden" name="CURRENCYID_C" value="<%=DISPLAYID%>">	<!--Resvi -->
		                     <input type="hidden" name="TRANSPORTID" value="<%=transports%>">
		                     <INPUT type="hidden" name="SHIP_COUNTRY" value="">
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
          <div class="form-group" id="C_UEN">
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
	         
					<!-- 	                Author Name:Resviya ,Date:26/07/21 -->

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
								<input name="CUSTOMER_TYPE_C" id="CUSTOMER_TYPE_C" type="TEXT" placeholder="Select a Customer Group" value="" size="50"
									MAXLENGTH=50 class="form-control"> 
								<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_C\']').focus()"> 	
   		 						<i class="glyphicon glyphicon-menu-down"></i></span>
						</div>
					</div>
					
					<div class="form-group">
      					<label class="control-label col-form-label col-sm-2" for="transmode">Transport Mode</label>
      						<div class="col-sm-4">           	
    							<input name="transport" id="transport" type="TEXT"  placeholder="Select a transport" onchange="checkcustomertransport(this.value)" value="" size="50" MAXLENGTH=50 class="form-control">
   		 						<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
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
							<li class="nav-item active"><a href="#other_c"
								class="nav-link" data-toggle="tab" aria-expanded="true">Other
									Details</a></li>
							<li class="nav-item"><a href="#profile_c" class="nav-link"
								data-toggle="tab">Contact And Address Details</a></li>
							
								<li class="nav-item"><a href="#bank_cus" class="nav-link" 
								data-toggle="tab">Bank Account Details</a></li>
								
								<li class="nav-item">
                                <a href="#user_c" class="nav-link" data-toggle="tab">User</a></li>
                                
							<li class="nav-item"><a href="#remark_c" class="nav-link"
								data-toggle="tab">Remarks</a></li>
						</ul>
						<div class="tab-content clearfix">
<!-- 							<div class="tab-pane fade" id="home_c"> -->
					<%-- 			<br>
								<div class="form-group">
									<label class="control-label col-form-label col-sm-2 required">Country</label>
									<div class="col-sm-4">

										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE_C" name="COUNTRY_CODE_C" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		   _MasterUtil=new  MasterUtil();
		   ccList =  _MasterUtil.getCountryList("",plant,region);
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
								</div> --%>

<!-- 							</div> -->

							<div class="tab-pane fade" id="profile_c">
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
                 
                                    <INPUT name="SHIP_CONTACTNAME" id="SHIP_CONTACTNAME" type="TEXT" class="form-control"
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
                
                                <INPUT name="SHIP_DESGINATION" id="SHIP_DESGINATION" type="TEXT" class="form-control"
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
                                   <INPUT name="SHIP_WORKPHONE" id="SHIP_WORKPHONE" type="TEXT" value="" onkeypress="return isNumber(event)"	size="50" MAXLENGTH=30 class="form-control">
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
                              <INPUT name="SHIP_HPNO" id="SHIP_HPNO" type="TEXT" value="" size="50" class="form-control" onkeypress="return isNumber(event)"
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
                                <INPUT name="SHIP_EMAIL" id="SHIP_EMAIL" type="TEXT" value="" size="50"
			                    MAXLENGTH="50" class="form-control">
                                 </div>
								</div>

                                      <div class="form-group">
									<label class="control-label col-form-label col-sm-2 required">Country</label>
									<div class="col-sm-4">

										<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE_C" name="COUNTRY_CODE_C" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		   _MasterUtil=new  MasterUtil();
		   ccList =  _MasterUtil.getCountryList("",plant,region);
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
       <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="SHIP_OnCountry(this.value)" id="SHIP_COUNTRY_CODE" name="SHIP_COUNTRY_CODE" value="" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		   MasterUtil _MasterUtil1=new  MasterUtil();
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
               
                             <INPUT name="SHIP_ADDR1" id="SHIP_ADDR1" type="TEXT" value="" size="50"
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
              
                           <INPUT name="SHIP_ADDR2" id="SHIP_ADDR2"type="TEXT" value="" size="50"
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
                
                        <INPUT name="SHIP_ADDR3" id="SHIP_ADDR3" type="TEXT" value="" size="50"
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
                
                              <INPUT name="SHIP_ADDR4" id="SHIP_ADDR4" type="TEXT" value="" size="50"
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
                   <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="SHIP_STATE" name="SHIP_STATE" value="" style="width: 100%">
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
                        <INPUT name="SHIP_ZIP" id="SHIP_ZIP" type="TEXT" value="" size="50"
		             	MAXLENGTH=10 class="form-control">
                        </div>
                                     </div>

							</div>

							<div class="tab-pane active" id="other_c">
								<br>
								
								<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Tax Treatment</label>
			<div class="col-sm-4 ac-box">				
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnTaxChange_C(this.value)" id="TAXTREATMENT" name="TAXTREATMENT" style="width: 100%">
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
							id="TaxLabel_C"></label>
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
											<input id="PAYTERMS" name="PAYTERMS" class="form-control" type="text" onchange="checkcustomerpaymenttype(this.value)" value="" MAXLENGTH=100 placeholder="Select Payment Mode">
		    								<span class="select-icon" onclick="$(this).parent().find('input[name=\'PAYTERMS\']').focus()">
						 					<i class="glyphicon glyphicon-menu-down"></i></span>
									
								<!-- 		<div class="input-group">
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
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAMECUS" name="BANKNAMECUS" onchange="checkcustomerbank(this.value)" value="" style="width: 100%">
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

							<div class="tab-pane fade" id="remark_c">
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
								data-toggle="modal" data-target="#myModal" onClick="onAddCust();">Save</button>
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
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip(); 
	 
    // Author Name:Resviya ,Date:19/07/21 , Description: For Currency
    var  curr = document.form1.CURRENCYID.value;
var basecurrency = '<%=curency%>';   
document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";

document.getElementById('lbltotal').innerHTML = "Total ("+curr+")";//  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency
if(basecurrency!=curr)
	document.getElementById('showtotalcur').style.display = 'block';	
else
	document.getElementById('showtotalcur').style.display = 'none';


//Resvi Add for User Row removal-27.10.2021
$(".user-table tbody").on('click', '.user-action', function() {
	$(this).parent().parent().remove();
});
//Ends

<!-- 	                Author Name:Resviya ,Date:10/08/21 , Description -UEN (GCC/ASIAPACIFIC)     -->

var region = '<%=region%>'; 
if(region == "GCC"){
	var x = document.getElementById("UEN");
	x.style.display = "none";
}else if(region == "ASIA PACIFIC"){
	var x = document.getElementById("UEN");
	x.style.display = "block";
}

 
if(region == "GCC"){
	var x = document.getElementById("C_UEN");
	x.style.display = "none";
}else if(region == "ASIA PACIFIC"){
	var x = document.getElementById("C_UEN");
	x.style.display = "block";
}

<!-- End -->


/* Supplier Type Auto Suggestion */
$('#SUPPLIER_TYPE_S').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'SUPPLIER_TYPE_DESC',  
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/MasterServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT :  "<%=plant%>",
			ACTION : "GET_SUPPLIER_TYPE_DATA",
			QUERY : query
		},
		dataType : "json",
		success : function(data) {
			return asyncProcess(data.SUPPLIER_TYPE_MST);
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
			return '<div onclick="document.form1.SUPPLIER_TYPE_ID.value = \''+data.SUPPLIER_TYPE_ID+'\'"><p class="item-suggestion">' + data.SUPPLIER_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.SUPPLIER_TYPE_DESC+'</p></div>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="SUPPLIER_TYPE_IDAddBtn footer"  data-toggle="modal" data-target="#suppliertypeModal"> <a href="#"> + Add Supplier Group</a></div>');
		menuElement.next().width(menuElement.width());
		menuElement.next().css({ "top": top,"padding":"3px 20px" });
		if($(this).parent().find(".tt-menu").css('display') != "block")
			menuElement.next().hide();
	  
	}).on('typeahead:open',function(event,selection){
		$('.SUPPLIER_TYPE_IDAddBtn').show();
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);
		var menuElement = $(this).parent().find(".tt-menu");
		menuElement.next().show();
	}).on('typeahead:close',function(){
		setTimeout(function(){ $('.SUPPLIER_TYPE_IDAddBtn').hide();}, 180);
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
		
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			document.form1.SUPPLIER_TYPE_IDS.value = "";
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

//transport
$('#transport').typeahead({
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
		$("input[name=TRANSPORTID]").val(selection.ID);
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

/* Payment Mode Auto Suggestion */
$("#SPAYTERMS").typeahead({
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
		menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal" onclick="document.cpoform.custModal.value=\'cust\'"><a href="#"> + New Payment Mode</a></div>');
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
		menuElement.after( '<div class="payTermsAddBtn footer"  data-toggle="modal" data-target="#payTermsModal" onclick="document.cpoform.custModal.value=\'cust\'"><a href="#"> + Add Payment Terms</a></div>');
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
		    	  return '<p onclick="document.formsupplier.BRANCH.value = \''+data.BRANCH_NAME+'\'">'+data.NAME+'</p>';
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
			return '<div onclick="document.formsupplier.CURRENCYID_S.value = \''+data.CURRENCYID+'\'"> <p>'+data.CURRENCY+'</p></div>';

			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){

	}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			$("input[name ='CURRENCYID_S']").val("");	
	});
		
	
	
	var ntaxtreat = document.getElementById('nTAXTREATMENT').value;	
	if(ntaxtreat=="")
	{
	if(document.getElementById('vendname').value=="")
		{
	$('#nTAXTREATMENT').attr('disabled',true);
	$('#nTAXTREATMENT').css("background-color", "#fffff");
		}
	}
else
	$('#nTAXTREATMENT').attr('disabled',false);

var TAXTREATMENT_VALUE = $("input[name ='TAXTREATMENT_VALUE']").val();
if(TAXTREATMENT_VALUE!="")
	{
	document.getElementById('nTAXTREATMENT').value = TAXTREATMENT_VALUE;
	ntaxtreat = TAXTREATMENT_VALUE;
	}
	
document.getElementById('CHK1').style.display = 'none';

if(ntaxtreat =="GCC VAT Registered"||ntaxtreat=="GCC NON VAT Registered"||ntaxtreat=="NON GCC")
{
	document.getElementById('CHK1').style.display = 'block';
}


$('.check').click(function() {
    $('.check').not(this).prop('checked', false);
});

    $('[data-toggle="tooltip"]').tooltip(); 
    var d ="<%=taxbylabel%>";

    document.getElementById('TaxLabel').innerHTML = d +" No.";	
    document.getElementById('TaxLabel_C').innerHTML = d +" No.";
 
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
function payTermsCallback(payTermsData){
	if(payTermsData.STATUS="SUCCESS"){
		$("input[name ='cpayment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=PMENT_DAYS]").val(payTermsData.NO_DAYS);
	}
}

function customertypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#CUSTOMER_TYPE_C").typeahead('val', data.CUSTOMER_TYPE_DESC);
		$("input[name=CUSTOMER_TYPE_ID]").val(data.CUSTOMER_TYPE_ID);
	}
}
function suppliertypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#SUPPLIER_TYPE_S").typeahead('val', data.SUPPLIER_TYPE_DESC);
		$("input[name=SUPPLIER_TYPE_ID]").val(data.SUPPLIER_TYPE_ID);
	}
}

function paymentTypeCallback(payTermsData)
{
	if(payTermsData.STATUS="100"){
		$("input[name ='PAYTERMS']").typeahead('val',payTermsData.PAYMENTMODE);
	}
}
function checkManagerApp(obj){
	var manageapp = $(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val();
	if(manageapp == 0)
	$(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val(1);
	else
	$(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val(0);

}
function onAdd(){
	
   var CUST_CODE   = document.formsupplier.CUST_CODE.value;
   var CUST_NAME   = document.formsupplier.CUST_NAME.value;
   var PEPPOL   = document.formsupplier.PEPPOL.value;
   var PEPPOL_ID   = document.formsupplier.PEPPOL_ID.value;
 //  var companyregnumber   = document.formsupplier.companyregnumber.value;
   var TAXTREATMENT   = document.formsupplier.TAXTREATMENT.value;
   var RCBNO   = document.formsupplier.RCBNO.value;
   var rcbn = RCBNO.length;
   var CURRENCY = document.formsupplier.SUP_CURRENCY.value;
   var region = document.formsupplier.COUNTRY_REG.value;
   
   var ValidNumber   = document.formsupplier.ValidNumber.value;
   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" suppliers you can create"); return false; }
   
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Supplier ID");;document.formsupplier.CUST_CODE.focus(); return false; }
   
   if(CUST_NAME == "" || CUST_NAME == null) {
   alert("Please Enter Supplier Name"); 
   document.formsupplier.CUST_NAME.focus();
   return false; 
   }

   if(document.getElementById("PEPPOL").checked == true)
		PEPPOL="1";
	 else 
		PEPPOL="0";

	   if(PEPPOL_ID == "" &&  PEPPOL == "1") {
	 	  alert("Please Enter Peppol Id"); 
	 	return false; 
	}

//     Author Name:Resviya ,Date:10/08/21 , Description -UEN Alert    

   /* if(region == "GCC"){
	   document.formsupplier.companyregnumber.value="";
	}else if(region == "ASIA PACIFIC"){
		if (companyregnumber == "" || companyregnumber == null) {
		alert("Please Enter Unique Entity Number (UEN)");
		document.formsupplier.companyregnumber.focus();
		return false; 
		}
	} */

//   END
   
   
   //	 <!-- Author Name:Resviya ,Date:19/07/21 -->

	 if(CURRENCY == "" || CURRENCY == null) {
		 alert("Please Enter Currency ID"); 
		 document.formsupplier.SUP_CURRENCY.focus();
		 return false; 
		 }
//	 End
   if(document.formsupplier.TAXTREATMENT.selectedIndex==0)
	{
	alert("Please Select TAXTREATMENT");
	document.formsupplier.TAXTREATMENT.focus();
	return false;
	}
   if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
   {
	   var  d = document.getElementById("TaxLabel").innerHTML;
   	if(RCBNO == "" || RCBNO == null) {
   		
	   alert("Please Enter "+d+" No."); 
	   document.formsupplier.RCBNO.focus();
	   return false; 
	   }
   	
   	//if(document.formsupplier.COUNTRY_REG.value=="GCC")// region based validtion
	//{
	if(!isNumber(RCBNO))
	{
    alert(" Please Enter "+d+" No. Input In Number"); 
   	document.formsupplier.RCBNO.focus();
   	return false; 
  	}

	if("15"!=rcbn)
	{
	alert(" Please Enter your 15 digit numeric "+d+" No."); 
		document.formsupplier.RCBNO.focus();
		return false; 
		}
	//}
   }
else if(50<rcbn)
{
	var  d = document.getElementById("TaxLabel").innerHTML;
   alert(" "+d+" No. length has exceeded the maximum value"); 
   formsupplier.RCBNO.focus();
   return false; 
 }
   if(!isNumber(formsupplier.SUP_PMENT_DAYS.value))
   {
     alert(" Please Enter Days In Number");
     formsupplier.SUP_PMENT_DAYS.focus();  formsupplier.SUP_PMENT_DAYS.select(); return false;
   }
   if(formsupplier.COUNTRY_CODE.selectedIndex==0)
	{
	   alert("Please Select Country from Address");
	   formsupplier.COUNTRY_CODE.focus();
	 	return false;
	}
   var datasend = $('#formsupplierid').serialize();
   
   /* document.formsupplier.action  = "/track/CreateSupplierServlet?action=ADD&reurl=createExpenses";
   
   document.formsupplier.submit(); */
   var urlStr = "/track/CreateSupplierServlet?action=JADD&reurl=createExpenses";
	$.ajax( {
	type : "POST",
	url : urlStr,
	async : true,
	data : datasend,
	dataType : "json",
	success : function(data) {
		/* console.log(data);*/
		//alert(data.supplier[0].SID); 
		$("input[name ='vendno']").val(data.supplier[0].SID); 
		/* document.getElementById("evendcode").value  = data.supplier[0].SID; */
		$("input[name ='vendname']").val(data.supplier[0].SName);
		$('select[name ="nTAXTREATMENT"]').val(data.supplier[0].sTAXTREATMENT);
		$("input[name ='transport']").val(data.supplier[0].TRANSPORTNAME);
		$("input[name ='TRANSPORTID']").val(data.supplier[0].TRANSPORTID);
		$("input[name ='SUPPLIER_TYPE_ID']").val(data.supplier[0].SUPPLIER_TYPE_ID);
		getCurrencyid(data.supplier[0].sCURRENCY_ID,data.supplier[0].CURRENCYUSEQT);
		$('#nTAXTREATMENT').attr('disabled',false);
		if(data.supplier[0].sTAXTREATMENT =="GCC VAT Registered"||data.supplier[0].sTAXTREATMENT=="GCC NON VAT Registered"||data.supplier[0].sTAXTREATMENT=="NON GCC")
		{
			document.getElementById('CHK1').style.display = 'block';
		}
		else
			document.getElementById('CHK1').style.display = 'none';
		
		document.getElementById("REVERSECHARGE").checked = false;
		document.getElementById("GOODSIMPORT").checked = false;
		document.formsupplier.reset();
		$('#supplierModal').modal('hide');
	}
	});
}

function onIDGen()
{
 /* document.formsupplier.action  = "/track/CreateSupplierServlet?action=Auto-ID&reurl=createExpenses";
   document.formsupplier.submit();  */
   
	var urlStr = "/track/CreateSupplierServlet";
	$.ajax( {
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		PLANT : "<%=plant%>",
		action : "JAuto-ID",
		reurl : "createExpenses"
	},
	dataType : "json",
	success : function(data) {
		
		$("input[name ='CUST_CODE']").val(data.supplier[0].SID);
		$("input[name ='CUST_CODE1']").val(data.supplier[0].SID);
		
	}
	});

}

function checkitem_c(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Customer ID!");
		document.getElementById("CUST_CODE_C").focus();
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
						document.getElementById("CUST_CODE_C").focus();
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
	
	
	function onAddCust(){
	 var CUST_CODE   = document.form.CUST_CODE_C.value;
	 var CUST_NAME   = document.form.CUST_NAME_C.value;
	 var PEPPOL_C  = document.form.PEPPOL_C.value;
	 var PEPPOL_IDC   = document.form.PEPPOL_IDC.value;
//	 var cus_companyregnumber   = document.form.cus_companyregnumber.value;
	 var CL   = document.form.CREDITLIMIT.value;
	 var TAXTREATMENT   = document.form.TAXTREATMENT.value;
	 var RCBNO   = document.form.RCBNO.value;
	 var CURRENCY = document.form.CUS_CURRENCY.value;
	 var rcbn = RCBNO.length;
	 var region = document.form.COUNTRY_REG.value;
	 
	 
	 var ValidNumber   = document.form.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" customers you can create"); return false; }
	 
	 if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE_C.focus(); return false; }
	 if(CUST_NAME == "" || CUST_NAME == null) {alert("Please Enter Customer Name");document.form.CUST_NAME_C.focus(); return false; }


//   Author Name:Resviya ,Date:9/08/21 , Description -UEN Alert    

	 /*   if(region == "GCC"){
		   document.form.cus_companyregnumber.value="";
		}else if(region == "ASIA PACIFIC"){
			if (cus_companyregnumber == "" || cus_companyregnumber == null) {
			alert("Please Enter Unique Entity Number (UEN)");
			document.form.cus_companyregnumber.focus();
			return false; 
			}
		} */

//	   END

if(document.getElementById("PEPPOL_C").checked == true)
	PEPPOL_C="1";
 else 
	PEPPOL_C="0";

   if(PEPPOL_IDC == "" &&  PEPPOL_C == "1") {
 	  alert("Please Enter Peppol Id"); 
 	return false; 
}
	   
	   
	  //	 <!-- Author Name:Resviya ,Date:19/07/21 -->

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
		 var  d = document.getElementById("TaxLabel_C").innerHTML;
	   	if(RCBNO == "" || RCBNO == null) {
	   		
		   alert("Please Enter "+d+" No."); 
		   document.form.RCBNO.focus();
		   return false; 
		   }
	   	//if(document.form.COUNTRY_REG.value=="GCC")// region based validtion
		//{
		if(!isNumber(RCBNO))
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
	   var  d = document.getElementById("TaxLabel_C").innerHTML;
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
	 if(!isNumber(CL))
	 {
	   alert(" Please Enter Credit Limit Input In Number");
	   form.CREDITLIMIT.focus();  
	   form.CREDITLIMIT.select(); 
	   return false;
	 }
	 if(!isNumber(form.PMENT_DAYS.value))
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
	 
	/*  document.form.action  = "/track/CreateCustomerServlet?action=ADD&reurl=createInvoice";
	 document.form.submit(); */
	 var datasend = $('#formCustomer').serialize();
	   
	  
	   var urlStr = "/track/CreateCustomerServlet?action=JADD&reurl=createExpenses";
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
			/* console.log(data);
			alert(data.supplier[0].SID); */
			/* $("input[name ='']").val(data.customer[0].CID);
			$("input[name ='']").val(data.customer[0].CName); */
			
			document.getElementById("ecustcode").value  = data.customer[0].CID;
			document.getElementById("CUSTOMER").value  = data.customer[0].CName;
			$("input[name ='transport']").val(data.customer[0].TRANSPORTNAME);
			$("input[name ='TRANSPORTID']").val(data.customer[0].TRANSPORTID);
			getCurrencyid(data.customer[0].CURRENCY_ID,data.customer[0].CURRENCYUSEQT);
			document.form.reset();
			$('#customerModal').modal('hide');
		}
		}
		});
	 
	}
	
	function onIDGenCust()
	{
		
		
	/*  document.form.action  = "/track/CreateCustomerServlet?action=Auto-ID&reurl=createInvoice";
	 document.form.submit();  */
	 
		var urlStr = "/track/CreateCustomerServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : "<%=plant%>",
			action : "JAuto-ID",
			reurl : "createExpenses"
		},
		dataType : "json",
		success : function(data) {
			
			$("input[name ='CUST_CODE_C']").val(data.customer[0].CID);
			$("input[name ='CUST_CODE1_C']").val(data.customer[0].CID);
			
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
				    	$("#create_account_modal .alert").show();
				    	$('#create_account_modal .alert').html("For subaccounts, you must select the same account and extended type as their parent.");
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
					$("input[name ='paid_through_account_name']").val(data.ACCOUNT_NAME);
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
				 //var parType=$( "#create_account_modalcoa #acc_type option:selected" ).text();
				 var parType=$('#create_account_modalcoa #acc_det_type').val();
				 subType=subType.trim();
				 var n = parType.localeCompare(subType);
				    if(n!=0)
				    	{
				    	$("#create_account_modalcoa .alert").show();
				    	$('#create_account_modalcoa .alert').html("For subaccounts, you must select the same account and extended type as their parent.");
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
					$(ukey).parents("tr").find('input[name="expenses_account_name"]').val(data.ACCOUNT_NAME); 
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


	function checkbillable()
	{
		 var checkBox = document.getElementById("billable");
		 if (checkBox.checked == true){		 
			 $("input[name ='billable_status']").val("1");
			 $("#cusnamereq").addClass("required");
		 } else{		 
			 $("input[name ='billable_status']").val("0");
			 $("#cusnamereq").removeClass("required");
		 } 
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
	function OnTaxChange_C(TAXTREATMENT)
	{
		
		if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
		{
			$("#TaxLabel_C").addClass("required");
		}
		else
			$("#TaxLabel_C").removeClass("required");
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
				action : "GET_STATE_DATA",
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


	function checkuser(aCustCode)
	{	
		 if(aCustCode=="" || aCustCode.length==0 ) {
			alert("Enter User ID!");
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
					ACTION : "CUSTOMER_CHECKUSER"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
	                            
							alert("User ID Already Exists");
							return false;
						}
						else
							return true;
					}
	});	
			return true;
	}
	}

	function shippingAddress(){

		if (document.getElementById("SameAsContactAddress").checked == true) {
			document.getElementById("SameAsContactAddress").value = "1";
			var contactname = document.getElementById("CONTACTNAME").value;
			var desgination = document.getElementById("DESGINATION").value;
			var workphone = document.getElementById("WORKPHONE").value;
			var hpno= document.getElementById("HPNO").value;
			var email = document.getElementById("EMAIL").value;
			var country_code = document.getElementById("COUNTRY_CODE_C").value;
			SHIP_OnCountry(country_code);
			var scountry = document.form.COUNTRY.value;
			var addr1 = document.getElementById("ADDR1").value;
			var addr2 = document.getElementById("ADDR2").value;
			var addr3 = document.getElementById("ADDR3").value;
			var addr4 = document.getElementById("ADDR4").value;
			var state = document.getElementById("STATE").value;
			var zip = document.getElementById("ZIP").value;
				     
			document.getElementById("SHIP_CONTACTNAME").value = contactname;
			document.getElementById("SHIP_DESGINATION").value = desgination;
			document.getElementById("SHIP_WORKPHONE").value = workphone;
			document.getElementById("SHIP_HPNO").value = hpno;
			document.getElementById("SHIP_EMAIL").value = email;
			document.getElementById("SHIP_COUNTRY_CODE").value = country_code;
			document.form.SHIP_COUNTRY.value = scountry;
			document.getElementById("SHIP_ADDR1").value = addr1;
			document.getElementById("SHIP_ADDR2").value = addr2;
			document.getElementById("SHIP_ADDR3").value = addr3;
			document.getElementById("SHIP_ADDR4").value = addr4;
			//document.getElementById("SHIP_STATE").value = state;
			$("select[name ='SHIP_STATE']").val(state);
			document.getElementById("SHIP_STATE").value = state;
			document.getElementById("SHIP_ZIP").value = zip;
					 
			}  
		else {
			document.getElementById("SameAsContactAddress").value = "0";
			document.getElementById("SHIP_CONTACTNAME").value = "";
			document.getElementById("SHIP_DESGINATION").value = "";
			document.getElementById("SHIP_WORKPHONE").value = "";
			document.getElementById("SHIP_HPNO").value = "";
			document.getElementById("SHIP_EMAIL").value = "";
			document.getElementById("SHIP_COUNTRY_CODE").value = "Select Country";
			document.form.SHIP_COUNTRY.value = "";
			document.getElementById("SHIP_ADDR1").value = "";
			document.getElementById("SHIP_ADDR2").value = "";
			document.getElementById("SHIP_ADDR3").value = "";
			document.getElementById("SHIP_ADDR4").value = "";
			document.getElementById("SHIP_STATE").value = "Select State";
			document.getElementById("SHIP_ZIP").value = "";
			}
		}
function changePaid(){
	if($("#paid").is(':checked')){
		$("#paidthroughfield").show();
	} else {
		$("#paidthroughfield").hide();
		$("#paid_through_account_name").val("");
	}
	
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
<%@include file="../jsp/newTransportModeModal.jsp"%>
<%@include file="../jsp/newPaymentTermsModal.jsp"%>
<%@include file="../jsp/newPaymentModeModal.jsp"%><!-- thansith Add for paymentmode -->
<%@include file="../jsp/newCustomerTypeModal.jsp"%> <!-- Thanzith for add customertype -->
<%@include file="../jsp/NewSupplierTypeModal.jsp"%> <!-- thanzith Add For SupplierType -->

<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
</jsp:include>

