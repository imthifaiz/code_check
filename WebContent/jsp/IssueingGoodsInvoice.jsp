 <%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>
<%@ page import="com.track.serviceImplementation.*"%>
<%@ page import="com.track.service.*"%>
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
String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String region = StrUtils.fString((String) session.getAttribute("REGION"));
String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
PlantMstDAO plantMstDAO = new PlantMstDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String compindustry = plantMstDAO.getcompindustry(plant);
DateUtils _dateUtils = new DateUtils();
String curDate =DateUtils.getDate();
String ORDERNO = StrUtils.fString(request.getParameter("DONO"));
String tdono = ORDERNO;
String custCode = StrUtils.fString(request.getParameter("CUST_CODE"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String CUSTOMER = StrUtils.fString(request.getParameter("CUST_NAME"));
String empno = StrUtils.fString(request.getParameter("EMPNO"));
String cmd =StrUtils.fString(request.getParameter("cmd"));
String sTranId = StrUtils.fString(request.getParameter("TRANID")).trim();
String INVOICENO=StrUtils.fString(request.getParameter("INVOICENO")); 
String GINO=StrUtils.fString(request.getParameter("GINO")); 
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String taxbylabel = ub.getTaxByLable(plant);
String instatus = "", origin = "", deductInv = "";
String CURRENCYUSEQT="0",DISPLAY="";
String sCustCode = StrUtils.fString(request.getParameter("sCustCode"));
String CUSTOMERNAME=StrUtils.fString(request.getParameter("invcusnum")); 
String InCusCode=StrUtils.fString(request.getParameter("invcuscode")); 
String InvNo=StrUtils.fString(request.getParameter("invnum")); 
String sTAXTREATMENT = StrUtils.fString(request.getParameter("TAXTREATMENT"));
String companyregnumber=StrUtils.fString(request.getParameter("cus_companyregnumber"));
if(CUSTOMERNAME.equalsIgnoreCase(""))
{
	if(!CUSTOMER.equalsIgnoreCase(""))
	{
	CUSTOMERNAME=CUSTOMER;
	sCustCode=custCode;
	}
}
String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
shipworkphone="",shipcountry="",shiphpno="",shipemail="";
String status = "",btnname = "Save as Open",JobNum="",due_date="",payment_terms="",ordertype="";
InvoiceUtil invoiceUtil = new InvoiceUtil();
Hashtable ht = new Hashtable();
ht.put("ID", sTranId);
ht.put("PLANT", plant);
List invoiceHdrList =  invoiceUtil.getInvoiceHdrById(ht);

if(invoiceHdrList.size()>0)
{
	Map invoiceHdr=(Map)invoiceHdrList.get(0);
	instatus =(String) invoiceHdr.get("BILL_STATUS");
	status= (String)invoiceHdr.get("BILL_STATUS");
	if(!status.equalsIgnoreCase("DRAFT"))
		btnname="Save";
	origin = (String)invoiceHdr.get("ORIGIN");
	deductInv = (String)invoiceHdr.get("DEDUCT_INV");
	JobNum = (String)invoiceHdr.get("JobNum");
	ordertype = (String)invoiceHdr.get("ORDERTYPE");
	
 	TransportModeDAO transportmodedao = new TransportModeDAO();
 	String transportmode = "";
 	int transportid = Integer.valueOf((String)invoiceHdr.get("TRANSPORTID"));
	if(transportid > 0){
		transportmode = transportmodedao.getTransportModeById(plant,transportid);
	}else{
		transportmode = "";
	}
	
	shipcontactname = (String) invoiceHdr.get("SHIPCONTACTNAME");
	shipdesgination = (String) invoiceHdr.get("SHIPDESGINATION");
	shipaddr1 = (String) invoiceHdr.get("SHIPADDR1");
	shipaddr2 = (String) invoiceHdr.get("SHIPADDR2");
	shipaddr3 = (String) invoiceHdr.get("SHIPADDR3");
	shipaddr4 = (String) invoiceHdr.get("SHIPADDR4");
	shipstate = (String) invoiceHdr.get("SHIPSTATE");
	shipworkphone = (String) invoiceHdr.get("SHIPWORKPHONE");
	shipcountry = (String) invoiceHdr.get("SHIPCOUNTRY");
	shiphpno = (String) invoiceHdr.get("SHIPHPNO");
	shipemail = (String) invoiceHdr.get("SHIPEMAIL");
	shipzip = (String) invoiceHdr.get("SHIPZIP");
}
InvoiceDAO invoicedao = new InvoiceDAO();
Hashtable ht1 = new Hashtable();
ht1.put("INVOICEHDRID", sTranId);
ht1.put("PLANT", plant);

List invAttachList= invoicedao.getInvoiceAttachByInvId(ht1);
String Urlred="../home";

String projectid="",currencyeqt="1",isshippingtax="0",projectname="",transportid="",transportmode="", 
isorderdisctax="0",isdiscounttax="0",financetaxtype="",outboundgst="0",
taxtypeshow="",finiszero="1",finisshow="0",taxid="",currecyid="",ship="",shipid="";

MasterUtil masterUtil = new MasterUtil();
String STATEPREFIX ="";
String COUNTRYLOC ="";

int isodtax = 0;
int isdtax = 0;
int isshiptax = 0;
int trans  ;

ArrayList arrCust = new CustUtil().getCustomerDetails(custCode, plant);
String sAddr1 = (String) arrCust.get(2);
String sAddr2 = (String) arrCust.get(3);
String sAddr3 = (String) arrCust.get(4);
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
String sDob = (String) arrCust.get(61);
String sNational = (String) arrCust.get(62);
String DOB="";
String NATIONAL = "";
//		IMTHIYAS Modified on 02.03.2022
if(sDob.equalsIgnoreCase("")&& sNational.equalsIgnoreCase("")){
 DOB = "";
 NATIONAL = "";
}else if(sDob.equalsIgnoreCase("")){
 DOB = "";
 NATIONAL = "NATIONALITY : "+sNational;
}else if(sNational.equalsIgnoreCase("")){
 DOB = "DOB : "+sDob;
 NATIONAL = "";
}else{
 DOB = "DOB : "+sDob;
 NATIONAL = "NATIONALITY : "+sNational;
}
//IMTHIYAS Modified on 02.03.2022 

if(cmd.equalsIgnoreCase("Edit")){
	title = "Edit Invoice";
}
if(cmd.equalsIgnoreCase("IssueingGoodsInvoice")){
	
	Urlred="../goodsissued/detail?action=View&DONO="+ORDERNO+"&GINO="+GINO+"&CUST_NAME="+CUSTOMERNAME+"&CUSTNO="+custCode;
	origin = "sales";
	deductInv = "0";
	DoHDRService doHDRService = new DoHdrServiceImpl();
	DoDetService doDetService = new DoDetServiceImpl();
	DoHdr doHdr = doHDRService.getDoHdrById(plant, ORDERNO);
	List<DoDet> doDetList = doDetService.getDoDetById(plant, ORDERNO);
	
	taxtypeshow=doDetList.get(0).getTAX_TYPE();
	
	projectid=Integer.toString(doHdr.getPROJECTID());
	transportid=Integer.toString(doHdr.getTRANSPORTID());
// 	trans=doHdr.getTRANSPORTID();
// 	transportid=doHdr.getTRANSPORTID();
	currencyeqt=Double.toString(doHdr.getCURRENCYUSEQT());
	isshippingtax=Short.toString(doHdr.getISSHIPPINGTAX());
	isorderdisctax=Short.toString(doHdr.getISORDERDISCOUNTTAX());
	isdiscounttax=Short.toString(doHdr.getISDISCOUNTTAX());
	outboundgst=Double.toString(doHdr.getOUTBOUND_GST());
	currecyid=doHdr.getCURRENCYID();
	sTAXTREATMENT=doHdr.getTAXTREATMENT();
	taxid=Integer.toString(doHdr.getTAXID());
	isodtax = doHdr.getISORDERDISCOUNTTAX();
	isshiptax = doHdr.getISSHIPPINGTAX();
	isdtax = doHdr.getISDISCOUNTTAX();
	ship = doHdr.getSHIPPINGCUSTOMER();
	shipid = doHdr.getSHIPPINGID();
	due_date = doHdr.getDELDATE();
	payment_terms = doHdr.getPAYMENT_TERMS();
	shipcontactname = doHdr.getSHIPCONTACTNAME();
	shipdesgination = doHdr.getSHIPDESGINATION();
	shipaddr1 = doHdr.getSHIPADDR1();
	shipaddr2 = doHdr.getSHIPADDR2();
	shipaddr3 = doHdr.getSHIPADDR3();
	shipaddr4 = doHdr.getSHIPADDR4();
	shipstate = doHdr.getSHIPSTATE();
	shipworkphone = doHdr.getSHIPWORKPHONE();
	shipcountry = doHdr.getSHIPCOUNTRY();
	shiphpno = doHdr.getSHIPHPNO();
	shipemail = doHdr.getSHIPEMAIL();
	shipzip = doHdr.getSHIPZIP();
	
	FinCountryTaxType fintaxtype = new FinCountryTaxType();
	if(doHdr.getTAXID() > 0){
		FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
		fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(doHdr.getTAXID());
		financetaxtype=fintaxtype.getTAXTYPE();
		finiszero=Short.toString(fintaxtype.getISZERO());
		finisshow=Short.toString(fintaxtype.getSHOWTAX());
	}
	
	FinProjectDAO finProjectDAO = new FinProjectDAO();
	FinProject finProject=new FinProject();
	if(doHdr.getPROJECTID() > 0){
		finProject = finProjectDAO.getFinProjectById(plant, doHdr.getPROJECTID());
		projectname = finProject.getPROJECT_NAME();
	}
	
 	TransportModeDAO transportmodedao = new TransportModeDAO();
	if(doHdr.getTRANSPORTID() > 0){
		transportmode = transportmodedao.getTransportModeById(plant,doHdr.getTRANSPORTID());
	}else{
		transportmode = "";
	}
	
	if(doHdr.getSALES_LOCATION().length()>0){
		ArrayList sprefix =  masterUtil.getSalesLocationByState(doHdr.getSALES_LOCATION(), plant, "");
		Map msprefix=(Map)sprefix.get(0);
		STATEPREFIX = (String)msprefix.get("PREFIX");
		COUNTRYLOC = (String)msprefix.get("COUNTRY");
	}
// 	TransportModeDAO transportmodedao = new TransportModeDAO();
// 	transportmode = Integer.toString(doHdr.getTRANSPORTID());
// 	int trans = Integer.valueOf(transportmode);
// 	if(trans > 0){
// 		transportmode = transportmodedao.getTransportModeById(plant,trans);
// 	}else{
// 		transportmode = "";
// 	}

/* 	InvoiceUtil invUtil = new InvoiceUtil();
	 ArrayList listQry = invUtil.getEditInvoiceDetails(ht,plant);
	Map m=(Map)listQry;
	TransportModeDAO transportmodedao = new TransportModeDAO();
// 	int transportid1 = Integer.valueOf((String)m.get("TRANSPORTID"));
// 		trans=doHdr.getTRANSPORTID();
		transportid=Integer.toString(doHdr.getTRANSPORTID());
// 	 trans = Integer.valueOf((String)m.get("TRANSPORTID"));
	if(doHdr.getTRANSPORTID() > 0){
		transportmode = transportmodedao.getTransportModeById(plant,doHdr.getTRANSPORTID());
	}else{
		transportmode = "";
	}
 */	
// 	TransportModeDAO transportmodedao = new TransportModeDAO();
// 	transportmode = (String)billHdr.get("TRANSPORTID");
// 	int trans = Integer.valueOf(transportmode);
// 	if(trans > 0){
// 		transportmode = transportmodedao.getTransportModeById(plant,trans);
// 	}else{
// 		transportmode = "";
// 	}
	
}
	
if(cmd.equalsIgnoreCase("copy") || cmd.equalsIgnoreCase("Edit"))
{
	Urlred="../invoice/detail?dono="+ORDERNO+"&custno="+custCode+"&INVOICE_HDR="+sTranId;
	ORDERNO="";
}
if(cmd.equalsIgnoreCase("Edit")){
	origin = "manual";
	
}
if(cmd.equalsIgnoreCase("copy")){
	btnname = "Save as Open";
	title = "Copy Invoice" ;
}

MasterUtil _MasterUtil=new  MasterUtil();
ArrayList ccList =  _MasterUtil.getSalesLocationList("",plant,"");

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
boolean displayCustomerpop=false,displayPaymentTypepop=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displayCustomerpop = ub.isCheckValAcc("popcustomer", plant,username);
	displayCustomerpop =true; //Remove it after Adding check box in user access rights
}

CustMstDAO custdao = new CustMstDAO();

String NOOFCUSTOMER=StrUtils.fString((String) session.getAttribute("NOOFCUSTOMER"));
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
String DISPLAYID="";
String Cuscurency=plantMstDAO.getBaseCurrency(plant);
List curQryList=new ArrayList();
CurrencyDAO currencyDAO = new CurrencyDAO();
curQryList = currencyDAO.getCurrencyDetails(curency,plant);
for(int i =0; i<curQryList.size(); i++) {
	ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
	DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
	DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
    CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
    }

DOUtil doUtil = new DOUtil();//Azees -10.2022
String replacePreviousInvoiceCost="0";
   Map invoiceDetails= doUtil.getDOReceiptInvoiceHdrDetailsDO(plant,"Tax Invoice English");
   if(!invoiceDetails.isEmpty())
   	replacePreviousInvoiceCost = (String) invoiceDetails.get("SHOWPREVIOUSINVOICECOST");
   

	//employee checkbox check
	
	ArrayList plntList1 = plantMstDAO.getPlantMstDetails(plant);
Map plntMap1 = (Map) plntList1.get(0);
String isEmployee = (String) plntMap1.get("ISEMPLOYEEVALIDATESO");

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.INVOICE%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/issuegoodsinvoice.js"></script>
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
a:focus {
  outline: 1px solid blue;
}
</style>
<center>
	<h2>
		<small class="error-msg"><%=fieldDesc%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>     
                <% if(cmd.equalsIgnoreCase("Edit")) { %> 
                 <li><a href="../invoice/summary"><span class="underline-on-hover">Invoice Summary</span> </a></li>
                <li><a href="../invoice/../invoice/detail?dono=<%=ORDERNO %>&custno=<%=custCode %>&INVOICE_HDR=<%=sTranId %>"><span class="underline-on-hover">Invoice Detail</span> </a></li>
                <li><label>Edit Invoice</label></li> 
                <% }else if(cmd.equalsIgnoreCase("Copy")) { %>
                 <li><a href="../invoice/summary"><span class="underline-on-hover">Invoice Summary</span> </a></li>
                <li><a href="../invoice/../invoice/detail?dono=<%=ORDERNO %>&custno=<%=custCode %>&INVOICE_HDR=<%=sTranId %>"><span class="underline-on-hover">Invoice Detail</span> </a></li>
                <li><label>Copy Invoice</label></li> 
                <%} if(cmd.equalsIgnoreCase("IssueingGoodsInvoice")){%>
                 <li><a href="../goodsissued/summary"><span class="underline-on-hover">Goods Issued Summary</span> </a></li> 
                 <li><a href="../goodsissued/detail?action=View&DONO=<%=ORDERNO %>&GINO=<%=GINO %>&CUST_NAME=<%=CUSTOMERNAME %>&CUSTNO=<%=custCode%>"><span class="underline-on-hover">Goods Issued Detail</span> </a></li> 
                <li><label>Convert To Invoice</label></li> 
                <%}%> 
                                              
                
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
<input type="text" id="PageName" style="display: none;" value="Invoice">
	<form id="createBillForm" class="form-horizontal" name="form1" autocomplete="off" action="/track/InvoiceServlet?Submit=Save"  method="post" enctype="multipart/form-data" onsubmit="return validateInvoice()">
	<input type = "hidden" name="PROJECTID" value="<%=projectid%>">
				<input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
				 <% if(COMP_INDUSTRY.equals("Centralised Kitchen")){%>
			    <input name="ORDER_TYPE_MODAL" type="hidden" value="SALES">
			    <%} else { %>
			    <input name="ORDER_TYPE_MODAL" type="hidden" value="INVOICE">
			    <%} %>
				<input type="hidden" name="CURRENCYUSEQTOLD" value="<%=currencyeqt%>">
				<input type = "hidden" name="shiptaxstatus" value="<%=isshippingtax%>">
				<input type = "hidden" name="odiscounttaxstatus" value="<%=isorderdisctax%>">
				<input type = "hidden" name="discounttaxstatus" value="<%=isdiscounttax%>">
				<input type = "hidden" name="ptaxtype" value="<%=financetaxtype%>">
				<input type = "hidden" name="ptaxpercentage" value="<%=outboundgst%>">
				<input type = "hidden" name="ptaxdisplay" value="<%=taxtypeshow%>">
				<input type = "hidden" name="ptaxiszero" value="<%=finiszero%>">
				<input type = "hidden" name="ptaxisshow" value="<%=finisshow%>">
				<input type = "hidden" name="taxid" value="<%=taxid%>">
				<%-- <input type = "hidden" name="GST" value="<%=outboundgst%>"> --%>
				<input type = "hidden" name="aorderdiscount" value="">
				<input type = "hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>">
				<input type = "hidden" name="TRANSPORTID" value="<%=transportid%>">
				<input type = "hidden" name="custModal">
				<INPUT type="hidden" name="CURRENCYID"  value="<%=curency%>">
				<INPUT type="hidden" name="curency"  value="<%=curency%>">
			    <INPUT type="hidden" name="BASECURRENCYID"  value="<%=Cuscurency%>"> <%--Resvi--%>    
			    <INPUT type="hidden" name="EDUCATION"  value="<%=compindustry%>">    
		       	<INPUT type="hidden" name="DOBYEAR"  value="">    
		       	<INPUT type="hidden" name="NATIONAL"  value="">
		       	<INPUT type="hidden" name="replacePreviousInvoiceCost"  value="<%=replacePreviousInvoiceCost%>">  
		       	<input name="COMP_INDUSTRY" type="hidden" value="<%=COMP_INDUSTRY%>">  <!-- imti added condition based on product popup -->
					
				<%if(!ispuloc){ %>
				 <input type="hidden" id="SALES_LOC" name="SALES_LOC">
				<%} %>
				<input type="hidden" name="SHIPPINGID" value="<%=shipid%>">
				<input type="hidden" name="SHIPPINGCUSTOMER" value="<%=ship%>">
	<div class="form-group customer-section">
			<label class="control-label col-form-label col-sm-2 required">Customer Name</label>
			<div class="col-sm-6 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected  form-control" placeholder="Select a customer" name="CUSTOMER" id="CUSTOMER" value="<%=CUSTOMERNAME%>" <% if(!cmd.equalsIgnoreCase("Copy")) { %>readonly<% } %>>
				<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><!-- <i class="glyphicon glyphicon-menu-down"></i> --></span>
<!-- 				<span class="btn-danger input-group-addon" id="btnpop" onclick="javascript:popUpWin('customer_list_issue_summary.jsp?TYPE=ACCTCUST&CUST_NAME='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				</div>
				<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" id="nTAXTREATMENT" name="nTAXTREATMENT" value="<%=sTAXTREATMENT %>" style="width: 100%">
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
				<input name="plant" value="<%=plant%>" type="hidden">
				<input name="username" value=<%=username%> type="hidden">
			<INPUT type = "hidden" name="CUST_CODE" ID="CUST_CODE" value = "<%=custCode%>">
			<INPUT type = "hidden" name="curency" value = "<%=curency%>">
			<INPUT type = "hidden" name="EMPNO" value = "<%=empno%>">
			<INPUT type="hidden" name="cmd" value="<%=cmd%>" />
			<INPUT type="hidden" name="TranId" value="<%=sTranId%>" />
			<INPUT type="hidden" name="STATE_PREFIX" value="<%=STATEPREFIX%>" />
			<INPUT type="hidden" name="DO_STATE_PREFIX" value="" /> 
			<INPUT type = "hidden" name="TAXTREATMENT_VALUE" value ="<%=sTAXTREATMENT%>">
			<input type = "hidden" name="CUST_CODE2" value="<%=sCustCode%>">
			<input type = "hidden" name="SALES_LOC" value="">
			<INPUT type="hidden" name="COUNTRY_REG" id="COUNTRY_REG" value="<%=region%>">
			<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
			<%-- <INPUT type="hidden" name="CURRENCYID"  value="<%=curency%>"> --%>
			<INPUT type="hidden" name="ORIGIN" value="<%=origin%>">
			<INPUT type="hidden" name="DEDUCT_INV" value="<%=deductInv%>">
			<INPUT type="hidden" name="TONO" value="<%=JobNum%>"> 
			<INPUT type="hidden" name="billstatus" value="">   
			</div>
		</div>
		<%if(cmd.equalsIgnoreCase("IssueingGoodsInvoice")){ %>
		<%-- <div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Shipping Address:</label>
					<div class="col-sm-6 ac-box">
								<input type="text" class="form-control typeahead" id="SHIPPINGCUSTOMER" name="SHIPPINGCUSTOMER"  value="<%=ship%>" readonly> 
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'SHIPPINGCUSTOMER\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> --%>
								<!-- <span class="btn-danger input-group-addon"
								onclick="javascript:popUpWin('../jsp/shippingdetails_list.jsp?SHIPPINGCUSTOMER='+form.SHIPPINGCUSTOMER.value+'&FORMNAME=form');">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
					<!-- </div>
				</div> -->
				<%}else{ %>
				<%-- <div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Shipping Address:</label>
					<div class="col-sm-6 ac-box">
								<input type="text" class="form-control typeahead" id="SHIPPINGCUSTOMER" name="SHIPPINGCUSTOMER"  value="<%=ship%>" > 
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'SHIPPINGCUSTOMER\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> --%>
								<!-- <span class="btn-danger input-group-addon"
								onclick="javascript:popUpWin('../jsp/shippingdetails_list.jsp?SHIPPINGCUSTOMER='+form.SHIPPINGCUSTOMER.value+'&FORMNAME=form');">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
					<!-- </div>
				</div> -->
				<% } %>
				<div class="form-group shipCustomer-section" id="shipbilladd">
					<div class="col-sm-2"></div>
					<div class="col-sm-5" style="line-height: 7px; font-size: 13px;">
						<h5 style="font-weight: bold;">Billing Address</h5>
						<p><%=CUSTOMERNAME %></p>
						<p><%=sAddr1 %>  <%=sAddr2 %></p>
						<p><%=sAddr3 %>  <%=sAddr4%></p>
						<p><%=sState%></p>
						<p><%=sCountry%> <%=sZip%></p>
						<p><%=sHpNo%></p>
						<p><%=sWorkphone%></p>
						<p><%=sEmail%></p>
						<%if(compindustry.equalsIgnoreCase("Education")){ %>
						<p><%=DOB%></p>
						<p><%=NATIONAL%></p>
						<% } %>
					</div>
					<%if(!compindustry.equalsIgnoreCase("Education")){ %>
					<div class="col-sm-5" style="line-height: 7px; font-size: 13px;">
						<h5 style="font-weight: bold;">
							Shipping Address <a><span data-toggle="modal"
								data-target="#shipaddr" onclick="getshipaddr();"  style="font-size: 15px;font-weight: 450;">Change</span></a>
						</h5>
						<div id="cshipaddr">
							
						<p><%=shipcontactname %></p>
						<p><%=shipdesgination%></p>
						<p><%=shipaddr1 %> <%=shipaddr2 %></p>
						<p><%=shipaddr3 %> <%=shipaddr4 %></p>
						<p><%=shipstate %></p> 
						<p><%=shipcountry%> <%=shipzip%></p>
						<p><%=shiphpno %></p>
						<p><%=shipworkphone %></p>
						<p><%=shipemail%></p>
						</div>
					</div>
					<% } %>
					</div>
				<%if(cmd.equalsIgnoreCase("IssueingGoodsInvoice")){ %>
				<div class="form-group"><div id="btnpop"></div>
					<label class="control-label col-form-label col-sm-2">Transport Mode:</label>
					<div class="col-sm-4 ac-box">
								<input type="text" class="form-control typeahead" 
									id="transport" placeholder="Select a transport" name="transport" value="<%=transportmode%>" readonly>
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
					</div>
				</div>
				<%}else{ %>
				<div class="form-group"><div id="btnpop"></div>
					<label class="control-label col-form-label col-sm-2">Transport Mode:</label>
					<div class="col-sm-4 ac-box">
								<input type="text" class="form-control typeahead" 
									id="transport" placeholder="Select a transport" onchange="checktransport(this.value)" name="transport" value="<%=transportmode%>">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
					</div>
				</div>
				<% } %>
				<%if(compindustry.equalsIgnoreCase("Construction") || compindustry.equalsIgnoreCase("Service") || compindustry.equals("Education")){ %>
		<%if(cmd.equalsIgnoreCase("IssueingGoodsInvoice")){ %>
		<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4 ac-box">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" readonly
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
				<%}else{ %>
				<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4 ac-box">
						<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" onchange="checkproject(this.value)"
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
				<% } %>
				<%}else{%>
				<input type="hidden" name="project" value="">
				<%} %>
		<%if(cmd.equalsIgnoreCase("Edit")){ %>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Invoice#</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="invoice" name="invoice" value="<%=GINO%>" readonly="readonly">
			</div>
		</div>
		<%}else{ %>
			<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Invoice#</label>
						<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="invoice" onchange="checkorderno(this.value)" onkeypress="return blockSpecialCharOrderNo(event)" name="invoice">
							<span class="input-group-addon"  onClick="newinvno()">
	   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
	   		 	<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i></a></span>
						</div>
						</div>
					</div>
					<%} %>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">GINO#</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="gino" name="gino" value="<%=GINO%>" readonly="readonly">
			</div>
		</div>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Order Number</label>
			<div class="col-sm-4 ac-box">				
				<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" value="<%=ORDERNO%>" readonly="readonly">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		<%if(!JobNum.equalsIgnoreCase("")){ %>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Reference No:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="JOB_NUM" name="JOB_NUM" value="<%=JobNum%>" placeholder="Max 20 Characters" maxlength="20" readonly>
			   		 	</div>
					</div>
				</div>
		<%}else if(cmd.equalsIgnoreCase("copy")) {%>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Reference No:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="JOB_NUM" name="JOB_NUM" value="<%=JobNum%>" placeholder="Max 20 Characters" maxlength="20" >
			   		 	</div>
					</div>
				</div>
		<%} else{%>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Reference No:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="JOB_NUM" name="JOB_NUM" value="<%=JobNum%>" placeholder="Max 20 Characters" maxlength="20" >
			   		 	</div>
					</div>
				</div>
		<%}%>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Invoice Date</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" id="invoice_date" name="invoice_date" value="<%=curDate%>">
			</div>
		</div>
		<%if(cmd.equalsIgnoreCase("IssueingGoodsInvoice")){ %>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Due Date</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" id="due_date" name="due_date" placeholder="Select due date" value="<%=due_date%>" readonly>
			</div>
			
			<label class="control-label col-form-label col-sm-2">Payment Terms</label>			
			<div class="col-sm-3 ac-box">
				<input type="text" class="ac-selected form-control" id="payment_terms" name="payment_terms" placeholder="Select Payment Terms" value="<%=payment_terms%>" readonly>
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		<%}else{ %>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Due Date</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" id="due_date" name="due_date" placeholder="Select due date" value="<%=due_date%>">
			</div>
           <div class="col-sm-6 no-padding">
			<label class="control-label col-form-label col-sm-5">Payment Terms</label>			
			<div class="col-sm-6 ac-box">
				<input type="text" class="ac-selected form-control" id="payment_terms" name="payment_terms" placeholder="Select Payment Terms" onchange="checkpaymentterms(this.value)" value="<%=payment_terms%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		</div>
		<% } %>
		
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Order Type:</label>
					<div class="col-sm-4">
							<input type="text" class="form-control" id="ORDERTYPE" name="ORDERTYPE" onchange="checkordertype(this.value)" value="<%=ordertype%>">
							<span class="select-icon"
								onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
							</span>
					</div>
				</div>
		
		<div class="form-group employee-section">
			       <%if(isEmployee.equalsIgnoreCase("1")){ %>		
					<label class="control-label col-form-label col-sm-2 required">Employee:</label>
					<%}else{ %>
					<label class="control-label col-form-label col-sm-2">Employee:</label>
					<%} %>
			<div class="col-sm-4 ac-box">
				<input type="text" class="ac-selected form-control typeahead" id="EMP_NAME" name="EMP_NAME" onchange="checkemployeess(this.value)" placeholder="Select a employee">
				<input type="hidden" name="ISEMPLOYEEVALIDATESO" value="<%=isEmployee%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
			</div>
		</div>
		<%-- <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Sales Location</label>
			<div class="col-sm-4 ac-box">
				<!-- <input type="text" class="ac-selected form-control typeahead" id="SALES_LOC" name="SALES_LOC" placeholder="Select a Sales Location">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'SALES_LOC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
				<SELECT class="form-control" data-toggle="dropdown" id="SALES_LOC" data-placement="right" name="SALES_LOC" onchange='OnChange(form1.SALES_LOC);' style="width: 100%">
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
					<label class="control-label col-form-label col-sm-2 required" >Currency</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="ac-selected form-control typeahead" id="CURRENCY" name="CURRENCY" placeholder="Select a Currency" onchange="checkcurrency(this.value)" value="<%=DISPLAY%>">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				
				 <!-- Author Name:Resviya ,Date:16/07/21 -->
           
                   
									<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-5 required" id="exchangerate" ></label>
						<div class="col-sm-6 ac-box">
							<input type="text" class="form-control" id="CURRENCYUSEQT" name="CURRENCYUSEQT" onkeypress="return isNumberKey(event,this,4)" placeholder="Enter Exchange Rate" value="<%=CURRENCYUSEQT%>" required>	
						</div>
					</div>
					</div>
               <!--  Ends  -->
               
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Standard <%=taxbylabelordermanagement%> :</label><!-- this is dynamic value -->
					<div class="col-sm-4">
					<%-- <% if(cmd.equalsIgnoreCase("IssueingGoodsInvoice")){%>ORDERNO --%>
					<% if(tdono.length() > 0){%>
			   		 	<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" value="<%=outboundgst%>" readonly>
						<span class="sideiconspan"><p>%</p></span>
					<%}else{ %>
						<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" onkeypress="return isNumberKey(event,this,4)" value="<%=outboundgst%>">
						<span class="sideiconspan"><p>%</p></span>
					<%} %>
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
		<hr/>
		<div class="form-group">
			<div class="col-sm-12">
				<label class="control-label col-form-label">Product Rates Are</label>
				<div class="dropdown-noborder">
					<select class="ac-box dropdown-noborder form-control" onchange="calculateTotal()" name="item_rates" id="item_rates">
						<option value="0">Tax Exclusive</option>
						<option value="1">Tax Inclusive</option>
					</select>
				</div>
			</div>
		</div>
		<div class="row" style="margin:0px;">
			<table class="table table-bordered line-item-table bill-table" style="width:100%;">
			<thead>
			  <tr>
				<th class="bill-desc" colspan=2>Product Details</th>
				<th>Account</th>
				<%if(deductInv.equalsIgnoreCase("1")&&origin.equalsIgnoreCase("manual")){ %>
				<th>UOM</th>
				<th>Location</th>
				<th>Batch</th>
				<%} %>
				<th class="item-qty text-right">Quantity</th>
				<th style="width:7%">Cost</th>
				<th style="width:7%" id="AODTYPE"></th>
				<th class="item-cost text-right">Unit Price</th>
				<th class="item-discount text-center">Discount</th>
				<!-- <th class="item-tax">Tax</th> -->
				<th class="item-amount text-center"colspan=2 style="width:13%">Amount</th>
			 </tr>
			</thead>
			<tbody>
			  <tr>
				<td class="item-img text-center">
				  <!-- <span class="glyphicon glyphicon-picture"></span> -->
				  <img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">
				  <input type="hidden" name="basecost" value="0.00">
				  <input type="hidden" name="lnno" value="1"> 
				  <input type="hidden" name="customerdiscount" value="">
				  	<input type="hidden" name="itemprice" value="0.00">
					<input type="hidden" name="itemcost" value="">
					<input type="hidden" name="discounttype" value="">
				  <input type="hidden" name="tax_type" class="taxSearch" value="">
				</td>
				<td class="bill-item"><input type="text" name="item" onchange="checkitems(this.value,this)"
									class="form-control itemSearch" style="width:87%;display: inline-block;" 
									placeholder="Type or click to select an item."
									onchange="calculateAmount(this)">
									<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
									<input class="form-control"  name="ITEMDES" style="height: 23px;background-color: #fff;" readonly>
									</td>
								<td class=""><input type="text" name="account_name" 
									class="form-control accountSearch" 
									placeholder="Account"  value="Local sales - retail"></td>
								<td class="invEl"><input type="text" name="uom" onchange="checkprduom(this.value,this)"
									class="form-control uomSearch" 
									placeholder="UOM">
									</td>
								<td class="invEl"><input type="text" name="loc" onchange="checkprdloc(this.value,this)"
									class="form-control locSearch" 
									placeholder="Location"></td>									
								<td class="invEl"><input type="text" name="batch" 
									class="form-control batchSearch" value="" 
									placeholder="Batch">
									</td>			
				<td class="item-qty text-right"><input name="qty" type="text" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>
				<td>
				<input type="text" name="unitcost" class="form-control text-right" value="0.00" readonly>
				</td>
				<td>
				<input type="text" name="addonshow" class="form-control text-right" value="0.00" readonly>
				<input type="hidden" name="addonprice" value="0.00">
				<input type="hidden" name="addontype" value="<%=curency%>">
				</td>
				<td class="item-cost text-right"><input name="cost" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>
				<td class="item-discount text-center">
				<div class="row">							
							<div class=" col-lg-12 col-sm-3 col-12">
								<div class="input-group my-group" style="width:120px;"> 
									<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
									 <%--<input type="text" class="discountPicker form-control item_discounttypeSearch" id="item_discounttype" value="<%=curency%>" name="item_discounttype" onchange="calculateAmount(this)"> --%>
									<%--<textarea rows="1" maxlength="3" style="resize: none;padding: 5px;" class="discountPicker form-control item_discounttypeSearch" name="item_discounttype" onchange="calculateAmount(this)"></textarea>--%>
									 <select name="item_discounttype" id="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">
										<%-- <option value="<%=curency%>"><%=curency%></option>
										<option value="%">%</option> --%>										
									</select>							
								</div>
							</div> 
						</div>
				</td>
				<!-- <td class="item-tax">
					<input name="tax_type" type="text" class="form-control taxSearch" placeholder="Select a Tax">
				</td> -->
				<td class="item-amount text-right grey-bg">
				<input name="amount" type="text" class="form-control text-right" value="0.00" readonly="readonly">
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
		<!-- Total Details -->
		<div class="row">
		<div class="col-sm-6">
			<% if(JobNum.isEmpty()) { %>
				<a href="#" onclick="addRow(event)">
						<i class="add-line" title="Add another line" style="font-size: 15px;">+ Add another line</i>
						</a>
				<% } %>
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
										<%if(isodtax == 1){ %>
										<span id="odtax">(Tax Inclusive)</span>
										<%}else{%>
										<span id="odtax">(Tax Exclusive)</span>
										<%} %>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isodisctax" <%= (isodtax == 1 ? "checked" : "")%>  name="isodisctax" Onclick="isodisctaxing(this)">
									</div>
									<div class=" col-lg-6 col-sm-6 col-6">
										<div class="input-group my-group">
											<input class="form-control text-right oddiscount" type="text" value="0.00" readonly
												name="orderdiscount" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
											<!-- <span class="input-group-addon">%</span> -->
											<!-- <select class="discountPicker form-control" disabled="true">
												<option value="%">%</option>
											</select> -->
											<select class="discountPicker form-control" name="oddiscount_type" id="oddiscount_type" onchange="calculateTotal()" disabled="true">
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
										<span id="dtax">(Tax Inclusive)</span>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isdtax"  <%= (isdtax == 1 ? "checked" : "")%> name="isdtax" Onclick="isdtaxing(this)">
							</div>
<!-- 							IMTHIYAS Modified on 02.03.2022 -->
							<% if(compindustry.equals("Education")){%>
							<div class=" col-lg-6 col-sm-6 col-6">
								<div class="input-group my-group"> 
									<input class="form-control text-right" id="dediscount" type="text" name="discount" value="0.0" onchange="calculateTotal()" disabled onkeypress="return isNumberKey(event,this,4)">
									<!-- <span class="input-group-addon">%</span>	-->
									<select class="discountPicker form-control" id="discount_type" name="discount_type" disabled onchange="calculateTotal()">
										<%-- <option value="<%=curency%>"><%=curency%></option>
										<option value="%">%</option> --%>										
									</select>							
								</div>
							</div> 
							<%}else{ %>
							<div class=" col-lg-6 col-sm-6 col-6">
								<div class="input-group my-group"> 
									<input class="form-control text-right" id="dediscount" type="text" name="discount" value="0.0" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)">
									<!-- <span class="input-group-addon">%</span>	-->
									<select class="discountPicker form-control" id="discount_type" name="discount_type" onchange="calculateTotal()">
										<%-- <option value="<%=curency%>"><%=curency%></option>
										<option value="%">%</option> --%>										
									</select>							
								</div>
							</div> 
							<%}%>
							<!-- IMTHIYAS Modified on 02.03.2022 -->
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
										<% if(isshiptax == 1) { %>
										<span id="shtax">(Tax Inclusive)</span>
										<% } else { %>
										<span id="shtax">(Tax Exclusive)</span>
										<% } %>
									</div>
									<div class="col-lg-1 col-sm-1 col-1">
									<input type="checkbox" class="form-check-input isshiptax"  <%= (isshiptax == 1 ? "checked" : "")%> name="isshiptax" Onclick="isshiptaxing(this)">
									</div>
									<div class="col-lg-6 col-sm-6 col-6">
										<input class="form-control text-right ember-view" type="text"
											name="shippingcost" value="0.0" onkeypress="return isNumberKey(event,this,4)">
									</div>
								</div>
							</div>
							<div class="total-amount deshipping" id="shipping" name="shipping">0.00</div>
				</div>
				<div class="taxDetails">
				</div>								
				<div class="total-row">
					<div class="badge-editable total-label">
						<div class="row">
							<div class="col-lg-5 col-sm-5 col-5"> 
								<div class="form-control-plaintext"> Adjustment  <br> 
								</div>
							</div> 
							<div class="col-lg-6 col-sm-6 col-6"> 
								<input class="form-control text-right ember-view" type="text" value="0.0" name="adjustment" onchange="calculateTotal()" onkeypress="return isNumberKey(event,this,4)"> 
							</div>  
						</div>
					</div> 
					<div class="total-amount " style="line-height: 2" id="adjustment"> 0.00 </div>
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
						<input type="file" class="form-control input-attch" id="billAttch" name="file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
				<%if(cmd.equalsIgnoreCase("Copy")) {%>
					<div id="billAttchNote">
						<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
					</div>
				<%}else{ %>
					<%if(invAttachList.size()>0){ %>
					<div id="billAttchNote">
						<small class="text-muted"><div class="attachclass"><a><%=invAttachList.size()%> files Attached</a>
								<div class="tooltiptext">
									
									<%for(int i =0; i<invAttachList.size(); i++) {   
								  		Map attach=(Map)invAttachList.get(i); %>
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
				
				<%} %>
			</div>
			<div class="col-sm-6 notes-sec">
				<p>Customer Notes</p>
				<div> <textarea rows="2" name="note" maxlength="990" class="ember-text-area form-control ember-view"></textarea> </div>
			
				<p>Terms &amp; Conditions</p>
				<div> <textarea rows="2" name="terms" maxlength="990" class="ember-text-area form-control ember-view" placeholder="Enter the terms and conditions of your business to be displayed in your transaction"></textarea> </div>
			</div>
		</div>
		<input id="sub_total" name="sub_total" value="" type="hidden">
		<input id="total_amount" name="total_amount" value="" type="hidden">
		<input id="taxamount" name="taxamount" value="" type="hidden">
		<input name="Submit" value="Save" type="hidden">
		<input name="invoice_status" value="Save" type="hidden">
		<input name="oShippingcost" value="" type="hidden">
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
				<input type="hidden" value="<%=displayCustomerpop%>" name="displayCustomerpop" id="displayCustomerpop" />
		<div class="row">
			<div class="col-sm-12 txn-buttons">
			<%if(instatus.equals("CANCELLED")) {%>
				<button id="btnBillDraft" type="button" class="btn btn-success">Save as Draft</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			<%}else{	%>
				<div class="dropup">
						<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
					    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown"><%=btnname %>
					    <span class="caret"></span></button>
					    <ul class="dropdown-menu">
					      <li><a id="btnBillOpen" href="#"><%=btnname %></a></li>
						<%if(!cmd.equalsIgnoreCase("Edit")) {%>
					      <li><a id="btnBillDraft" href="#">Save as Draft</a></li>
					    <%}%> 
					      <li><a id="btnBillOpenEmail" href="#"><%=btnname %> and Send Email</a></li>
					    </ul>
						<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					  </div>
					<!-- <button id="btnBillDraft" type="button" class="btn btn-default">Save as Draft</button> -->
				
					<%-- <button id="btnBillOpen" type="button" class="btn btn-success"><%=btnname %></button>
					<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button> --%>
			<%}	%>	
			</div>
		</div>
</form>
</div>
<%
	EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
	Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.INVOICE);
	PlantMstUtil plantMstUtil = new PlantMstUtil();
	ArrayList plntMstdetail = plantMstUtil.getPlantMstDetails(plant);
	Map plntMstdetailMap = (Map)plntMstdetail.get(0);
	String PLNTDESC = (String)plntMstdetailMap.get("PLNTDESC");
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
	<!-- Modal -->
	
	<jsp:include page="newEmployeeModal.jsp" flush="true"></jsp:include>
	<jsp:include page="newOrderTypeModal.jsp" flush="true"></jsp:include> 
	<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
	<%@include file="newProductModal.jsp" %>
	<%@include file="NewChartOfAccountpopup.jsp"%>
	<%@include file="newGstDetailModal.jsp" %>
	<%@include file="../jsp/newPaymentTermsModal.jsp" %>
	<%@include file="../jsp/newTransportModeModal.jsp"%> <!-- imti for add transport -->
	<input type="hidden" name="pronumberOfDecimal" id="pronumberOfDecimal" value=<%=numberOfDecimal%>>
	<!-- Modal -->
<!-- 	<div id="myModal" class="modal fade" role="dialog"> -->
<!-- 	  <div class="modal-dialog">	 -->
<!-- 	    Modal content -->
<!-- 	    <div class="modal-content"> -->
<!-- 	      <div class="modal-header"> -->
<!-- 	        <button type="button" class="close" data-dismiss="modal">&times;</button> -->
<!-- 	        <h3 class="modal-title">Modal Header</h3> -->
<!-- 	      </div> -->
<!-- 	      <div class="modal-body"> -->
<!-- 	        <p>Some text in the modal.</p> -->
<!-- 	      </div> -->
<!-- 	      <div class="modal-footer"> -->
<!-- 	      		<button id="btnBillOpen" type="button" class="btn btn-success">Save</button> -->
<!-- 				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
<!-- 	      </div> -->
<!-- 	    </div> -->
<!-- 	  </div> -->
<!-- 	</div> -->
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
								<a class="add-lines" style="text-decoration: none; cursor: pointer;"
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
	         
					<!-- 	                Author Name:Resviya ,Date:16/07/21 -->

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
							<INPUT name="CUSTOMEREMAIL" ID="CUSTOMEREMAIL" type="TEXT" value="" size="50"
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
				MasterUtil _MasterUtils=new  MasterUtil();
				 ArrayList ccLists =  _MasterUtils.getCountryList("",plant,region);
			for(int i=0 ; i<ccLists.size();i++)
      		 {
				Map m=(Map)ccLists.get(i);
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
			_MasterUtil=new  MasterUtil();
		    ccList =  _MasterUtil.getBankList("",plant);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
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


<script type="text/javascript">
$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip(); 
	var deductInv = document.form1.DEDUCT_INV.value;
	var origin = document.form1.ORIGIN.value;

	<!-- Author Name:Resviya ,Date:16/07/21 , Description: For Currency -->
	var  curr = document.form1.CURRENCYID.value;
	var basecurrency = '<%=Cuscurency%>';   
// 	var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
	
document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+curr+")";
document.getElementById('AODTYPE').innerHTML = "Add On (% / "+curr+")";
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
	


	       <!-- End -->

	$('#discount_type').append('<option value="<%=curency%>"><%=curency%></option>');
	$('#discount_type').append('<option value="%">%</option>');
	
	var cmd = "<%=cmd%>";
	if(cmd != 'Edit'){
		newinvno();
	}
	if(cmd == 'copy'){
		if(deductInv == "1" && origin == "manual"){
		onNewGINO();
		}
	}
	
	$('#discount_type').empty();
	$('#discount_type').append('<option value="<%=curency%>"><%=curency%></option>');
	$('#discount_type').append('<option value="%">%</option>');
	
	var TAXTREATMENT_VALUE = $("input[name ='TAXTREATMENT_VALUE']").val();
	if(TAXTREATMENT_VALUE!="")
		document.getElementById('nTAXTREATMENT').value = TAXTREATMENT_VALUE;
	
    var d = "<%=taxbylabel%>";
    document.getElementById('TaxLabel').innerHTML = d +" No.";
    var cust = "<%=sCustCode%>";
    var cust2 = "<%=custCode%>";
    
    if(cust=="" || cust.length==0 ) {
    }else{
    	
    	$("input[name ='CUST_CODE']").val(cust);
    	if(cust!=cust2)
		{
    	 $('#customerModal').modal('show');
		}
    }
    
   
    document.getElementById("btnpop").style.display = "none";
    
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
		 var cus_companyregnumber   = document.form.cus_companyregnumber.value;
		 var CL   = document.form.CREDITLIMIT.value;
		 var TAXTREATMENT   = document.form.TAXTREATMENT.value;
		 var RCBNO   = document.form.RCBNO.value;
		 var rcbn = RCBNO.length;
		 var CURRENCY = document.form.CUS_CURRENCY.value;
		 var region = document.form.COUNTRY_REG.value;
			   	   
		   var ValidNumber   = document.form.ValidNumber.value;
		   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" customers you can create"); return false; }
		   
		 if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE_C.focus(); return false; }
		 if(CUST_NAME == "" || CUST_NAME == null) {
			 alert("Please Enter Customer Name"); 
			 document.form.CUST_NAME_C.focus();
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

//		   END

		//Author Name:Resviya ,Date:17/07/21

		 if(CURRENCY == "" || CURRENCY == null) {
			 alert("Please Enter Currency ID"); 
			 document.form.CUS_CURRENCY.focus();
			 return false; 
			 }
	      //	END
		 
//		 <!-- Author Name:Resviya ,Date:16/07/21 -->

		 if(CURRENCY == "" || CURRENCY == null) {
			 alert("Please Enter Currency ID"); 
			 document.form.CUS_CURRENCY.focus();
			 return false; 
			 }
//		 End
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
				if(document.form1.custModal.value == "cust"){
				//alert(JSON.stringify(data));
				$("input[name ='CUST_CODE']").val(data.customer[0].CID);
				//document.getElementById("CUST_CODE").value  = data.customer[0].CID;
				document.getElementById("CUSTOMER").value  = data.customer[0].CName;
				$("input[name ='CUSTOMER']").val(data.customer[0].CName);
				$("input[name ='TAXTREATMENT_VALUE']").val(data.customer[0].CTAXTREATMENT);
				getCurrencyid(data.customer[0].CURRENCY,data.customer[0].CURRENCY_ID,data.customer[0].CURRENCYUSEQT);
				document.getElementById('nTAXTREATMENT').value = data.customer[0].CTAXTREATMENT;
				$('#customerModal').modal('hide');

			}else{
				document.form.reset();
				document.form1.SHIPPINGCUSTOMER.value = data.customer[0].CName;
				document.form1.SHIPPINGID.value = data.customer[0].CID;
				$('#customerModal').modal('hide');
			}
			}
			});
		}

	function onIDGen()
	{
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

window.onload = function() {
	//onNew();
	};
function onNew()
{
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		ACTION : "INVOICE"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.invno;
				document.form1.invoice.value= resultV;
	
			} else {
				alert("Unable to genarate INVOICE NO");
				document.form.INVOICENO.value = "";
			}
		}
	});	
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

function newinvno(){
	var plant = document.form1.plant.value;
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : plant,
		ACTION : "INVOICE"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.invno;
				$("input[name ='invoice']").val(resultV);
			} else {
				document.form1.invoice.value = "";
			}
		}
	});	
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
$('select[name="COUNTRY_CODE"]').on('change', function(){		
    var text = $("#COUNTRY_CODE option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
});

function openTab(evt, ref) {
	  // Declare all variables
	  var i, tabcontent, tablinks;

	  // Get all elements with class="tabcontent" and hide them
	 /*  tabcontent = document.getElementsByClassName("tabcontent");
	  for (i = 0; i < tabcontent.length; i++) {
	    tabcontent[i].style.display = "none";
	  } */

	  // Get all elements with class="tablinks" and remove the class "active"
	  tablinks = document.getElementsByClassName("tablinks");
	  for (i = 0; i < tablinks.length; i++) {
	    tablinks[i].className = tablinks[i].className.replace(" active", "");
	  }

	  // Show the current tab, and add an "active" class to the button that opened the tab
	  //document.getElementById(ref).style.display = "block";
	  evt.currentTarget.className += " active";
	  if(ref == "noninventory"){
		  $(".invEl").hide();
		  $("input[name=DEDUCT_INV]").val("0");
		  $("input[name='gino']").val("")
		  removeSuggestionToTable();
		addSuggestionToTable();
	  }else{
		  $(".invEl").show();
		  $("input[name=DEDUCT_INV]").val("1");
		  onNewGINO();
		  removeSuggestionToTable();
		addSuggestionToTable();
	  }
	  
	}

function onNewGINO()
{
	
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		ACTION : "GINO"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.invno;
				$("input[name='gino']").val(resultV);
	
			} else {
				alert("Unable to genarate GINO");
				$("input[name='gino']").val("");
			}
		}
	});	
	}

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
/* $(document).ready(function(){
	getCustomerDetails();
}); */
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
</script>

</div>
</div>
<jsp:include page="newBankModal.jsp" flush="true"></jsp:include>
<jsp:include page="newProductDepTypeModal.jsp" flush="true"></jsp:include> <!-- Resvi for add product department -->
<jsp:include page="newProductCategoryModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product category -->
<jsp:include page="newProductSubCatgoryModal.jsp" flush="true"></jsp:include><!-- Thanzith for add product Sub Category -->
<jsp:include page="newProductBrandModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product Brand -->
<jsp:include page="newProductHsCodeModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product HsCode -->
<jsp:include page="newProductCOOModal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product COO -->
<jsp:include page="newProductUOMmodal.jsp" flush="true"></jsp:include> <!-- Thanzith for add product UOM -->
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>
