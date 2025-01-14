<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String TRANID = StrUtils.fString(request.getParameter("TRANID"));
PlantMstDAO plantMstDAO = new PlantMstDAO();
DateUtils _dateUtils = new DateUtils();
InvoicePaymentDAO invpaymentdao = new InvoicePaymentDAO();
String title = "Edit PDC Received";

String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String curDate = _dateUtils.getDate();

Hashtable ht = new Hashtable();
ht.put("ID", TRANID);
ht.put("PLANT", plant);
List pdcHdrList =  invpaymentdao.getpdcpaymentById(ht);
if(pdcHdrList.size()>0)
{}
else
{
	response.sendRedirect("../banking/pdcpayreceivesummary?result=No Data Found");
}
Map pdcHdr=(Map)pdcHdrList.get(0);
String PAYMENTID= (String) pdcHdr.get("RECEIVEID");
String PAYMENT_DATE= (String) pdcHdr.get("RECEIVE_DATE");
String custName= (String) pdcHdr.get("CUSTOMER");
String ACCOUNT= (String) pdcHdr.get("ACCOUNT");
String BANK_BRANCH= (String) pdcHdr.get("BANK_BRANCH");
String CHECQUE_NO= (String) pdcHdr.get("CHECQUE_NO");
String CHEQUE_DATE= (String) pdcHdr.get("CHEQUE_DATE");
String CHEQUE_AMOUNT= (String) pdcHdr.get("CHEQUE_AMOUNT");
String CHEQUE_REVERSAL_DATE= (String) pdcHdr.get("CHEQUE_REVERSAL_DATE");
if(CHEQUE_REVERSAL_DATE=="null"||CHEQUE_REVERSAL_DATE==null)
	CHEQUE_REVERSAL_DATE="";
String STATUS= (String) pdcHdr.get("STATUS");
String showBtn="",showclnBtn="";
if((STATUS.equalsIgnoreCase("PROCESSED")))
		showBtn = "style='display:none;'";
//else if((STATUS.equalsIgnoreCase("NOT PROCESSED")))
			showclnBtn = "style='display:none;'";		
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PDC_PAYMENT_RECEIVED%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/PDCPaymentReceive.js"></script>
<style>

</style>
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
			<div class="btn-group" role="group">
			<button type="button" class="btn btn-default" data-toggle="dropdown" <%=showclnBtn%> >More <span class="caret"></span></button>
					   <ul class="dropdown-menu" style="min-width: 0px;">
					    <li id="bill-cancel"><a href="#">Cancel</a></li>
					  </ul>
				</div>
				&nbsp;
			<h1 style="font-size: 18px; cursor: pointer;position: relative;bottom: -7px;"
				class="box-title pull-right"
				onclick="window.location.href='../banking/pdcpayreceivesummary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
			</div>
		</div>

		<div class="container-fluid">
			<input type="number" id="numberOfDecimal" style="display: none;"
				value=<%=numberOfDecimal%>>
			<form class="form-horizontal" name="form" method="post" action="">
				<input type="text" name="plant" value="<%=plant%>" hidden> <input
					type="text" name="username" value=<%=username%> hidden>
					<INPUT type = "hidden" name="CUST_CODE" value=""> <input
					type="text" name="TRANID" value=<%=TRANID%> hidden>
				
				<div class="form-group">
			<label class="control-label col-form-label col-sm-2">PAYMENT ID</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="PAYMENTID" name="PAYMENTID" value="<%=PAYMENTID%>" readonly>
			</div>
		</div>
					
					<div class="form-group">
			<label class="control-label col-form-label col-sm-2">PAYMENT DATE</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="PAYMENT_DATE" name="PAYMENT_DATE" value="<%=PAYMENT_DATE%>" readonly>
			</div>
		</div>
					
					<div class="form-group">
			<label class="control-label col-form-label col-sm-2">CUSTOMER</label>
			<div class="col-sm-4">
				<div class="input-group"> 
				<input type="text" class="form-control" name="vendname" value="<%=custName%>" readonly>	
				</div>				
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">ACCOUNT NAME</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="ACCOUNT" name="ACCOUNT" value="<%=ACCOUNT%>" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">BANK</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="BANK_BRANCH" name="BANK_BRANCH" value="<%=BANK_BRANCH%>" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">CHEQUE NO</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="CHECQUE_NO" name="CHECQUE_NO" value="<%=CHECQUE_NO%>" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">CHEQUE DATE</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="CHEQUE_DATE" name="CHEQUE_DATE" value="<%=CHEQUE_DATE%>" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">CHEQUE REVERSED DATE</label>
			<div class="col-sm-4">
				<input type="text" size="30"  MAXLENGTH=20 class="form-control" id="CHEQUE_REVERSAL_DATE" name="CHEQUE_REVERSAL_DATE" value="<%=CHEQUE_REVERSAL_DATE%>" readonly>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">AMOUNT</label>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="CHEQUE_AMOUNT" name="CHEQUE_AMOUNT" value="<%=CHEQUE_AMOUNT%>" readonly>
			</div>
		</div>
		
		<div class="row">
					<div class="col-sm-12 text-center">
						<button type="button" onclick="if(onProcess(document.form)) {submitForm();}" class="btn btn-success" <%=showBtn%>>Save</button>
					</div>
				</div>
</form>
</div>
</div>
</div>

<div id="cancelbill" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-sm">	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-body">
	        <div class="row">
			   <div class="col-lg-2">
			      <i>
			         <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xxlg-md icon-attention-circle" style="fill: red">
			            <path d="M256 32c30.3 0 59.6 5.9 87.2 17.6 26.7 11.3 50.6 27.4 71.2 48s36.7 44.5 48 71.2c11.7 27.6 17.6 56.9 17.6 87.2s-5.9 59.6-17.6 87.2c-11.3 26.7-27.4 50.6-48 71.2s-44.5 36.7-71.2 48C315.6 474.1 286.3 480 256 480s-59.6-5.9-87.2-17.6c-26.7-11.3-50.6-27.4-71.2-48s-36.7-44.5-48-71.2C37.9 315.6 32 286.3 32 256s5.9-59.6 17.6-87.2c11.3-26.7 27.4-50.6 48-71.2s44.5-36.7 71.2-48C196.4 37.9 225.7 32 256 32m0-32C114.6 0 0 114.6 0 256s114.6 256 256 256 256-114.6 256-256S397.4 0 256 0z"></path>
			            <circle cx="256" cy="384" r="32"></circle>
			            <path d="M256.3 96.3h-.6c-17.5 0-31.7 14.2-31.7 31.7v160c0 17.5 14.2 31.7 31.7 31.7h.6c17.5 0 31.7-14.2 31.7-31.7V128c0-17.5-14.2-31.7-31.7-31.7z"></path>
			         </svg>
			      </i>
			   </div>
			   <div class="col-lg-10" style="padding-left: 2px">
			      <p> Are you sure about cancel the cheque?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" id="cfmcancel" style="background:red;">
			        	Yes 
			         </button>
			         <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
			      </div>
			   </div>
			</div>
	      </div>
	    </div>
	  </div>
	</div>

<script>
$(document).ready(function(){
	$('#CHEQUE_REVERSAL_DATE').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
	var status = "<%=pdcHdr.get("STATUS")%>";
	if(status=="PROCESSED")
		$('#CHEQUE_REVERSAL_DATE').datepicker("disable");
	else
		$('#CHEQUE_REVERSAL_DATE').css({'background-color' : '#ffffff'});
});
$("#bill-cancel").click(function() {
	$('#cancelbill').modal('show');
});

$("#cfmcancel").click(function(){
	document.form.action ="/track/InvoicePayment?CMD=PDA_PAYMENTCANCEL&TRANID=<%=pdcHdr.get("ID")%>";
	document.form.submit();
	});

function onProcess(form){
	document.form.action ="/track/InvoicePayment?CMD=PDA_PAYMENTEDIT";
	document.form.submit();
}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>