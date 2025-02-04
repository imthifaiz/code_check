<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%
String title = "Tax Return Summary";
String taxReturnHdrId = StrUtils.fString(request.getParameter("ID"));
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
TaxReturnFilingDAO taxReturnDAO=new TaxReturnFilingDAO();
Hashtable ht = new Hashtable();
ht.put("PLANT", plant);
Map plantMst=taxReturnDAO.getPlantMst(ht);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.TAX%>"/>
    <jsp:param name="submenu" value="<%=IConstants.TAX_RETURN%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/taxreturn.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
  <script type="text/javascript" src="../jsp/js/calendar.js"></script>
  <script type="text/javascript" src="../jsp/dist/js/moment.min.js"></script>
  <link href="../jsp/css/tabulator_simple.min.css" rel="stylesheet">
<script type="text/javascript" src="../jsp/js/tabulator.min.js"></script>
   <script type="text/javascript" src="../jsp/js/uae-taxreturnfill.js"></script>
  <style>
  .text-dashed-underline:after {
    padding-bottom: 2px;
    border-bottom: 1px dashed #969696!important;
    width:10%;
}
  .requiredlabel {
    color: #b94a48;
}
.form-text {
    color: #999;
}
.d-block {
    display: block!important;
}
.alert-warning {
    background-color: #fff4e7 !important;
    border: 0;
    color: #222 !important;
}
.text-ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.ribbon {
    position: absolute!important;
    top: -5px;
    left: -5px;
    overflow: hidden;
    width: 96px;
    height: 94px;
}
.ribbon .ribbon-draft {
    background-color: #94a5a6;
    border-color: #788e8f;
}
.ribbon .ribbon-inner {
    text-align: center;
    color: #fff;
    top: 24px;
    left: -31px;
    width: 135px;
    padding: 3px;
    position: relative;
    transform: rotate(-45deg);
}
.ribbon .ribbon-inner:before {
    left: 0;
    border-left: 2px solid transparent;
}
.ribbon .ribbon-inner:after {
    right: -2px;
    border-bottom: 3px solid transparent;
}
.ribbon .ribbon-inner:after, .ribbon .ribbon-inner:before {
    content: "";
    border-top: 5px solid transparent;
    border-left: 5px solid;
    border-left-color: inherit;
    border-right: 5px solid transparent;
    border-bottom: 5px solid;
    border-bottom-color: inherit;
    position: absolute;
    top: 20px;
    transform: rotate(-45deg);
}
  </style>
 
<%@ include file="header.jsp"%>
<form id="form1" name="form1">
	<input type="hidden" name="taxheaderid" id="taxheaderid" value="<%=taxReturnHdrId %>"/>;
</form>

  <div class="container-fluid m-t-20">
	 <div class="box"> 
        <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../tax/uae-gstreturnsummary'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		<div class="box-header menu-drop">
			<button type="button" class="btn btn-default pull-right" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
			<a class="btn btn-default pull-left" href="../tax/uae-gstreturnsummary"><i class="fa fa-cog"></i> VAT Returns</a>	

		</div>

		
 <div class="box-body">
	<div style="position:relative;padding: 0px 15px;box-shadow: 0 0 6px #ccc;" id="print_id1">
		<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-overdue" id="ribbon-unfill">Unfiled</div>
			<div class="ribbon-inner ribbon-success" id="ribbon-fill">Filed</div>
		</div>
		<div style="height: 0.700000in;"></div>
		<div class="mx-n4 vat-return-header">
			<div class="row return-criteria">
				<div class="col-lg-7 mt-4"> 
					<div class="row inline-fields">
						<div class="col-lg-5 text-muted">Tax Period</div> 
						<div class="col-lg-7"> :<span id="taxFrom"> 01 Jun 2019 </span><span class="text-muted">To </span><span id="taxTo"> 30 Jun 2019</span> <small class="text-muted" id="reportperiod">(Monthly)</small></div>
					</div>
					<div class="row inline-fields">
						<div class="col-lg-5 text-muted">Tax Basis</div> 
						<div class="col-lg-7">: <%=plantMst.get("REPROTSBASIS") %></div>
					</div>
				</div>
				<div class="col-lg-5">
					<div class="btn-toolbar float-right"> 
						<button class="btn btn-primary" data-test-action="mark-as-filed" data-ember-action="" data-ember-action-1410="1410" onclick="markFiled()" id="fillButton"> Mark as Filed </button> 
						<button class="btn btn-default" data-test-action="mark-as-filed" data-ember-action="" data-ember-action-1410="1410" onclick="unFiled()" id="unfillButton"> Mark as Unfiled </button>
					</div>
				</div>
			</div>
			<div class="prev-txns-chk"><div class="form-check my-3" data-ember-action="" data-ember-action-1411="1411">  <input id="includeprevioustax" class="form-check-input ember-checkbox ember-view" type="checkbox"> <label class="form-check-label" for="aa44ffbcd" data-test-title="include-adjustments"> Include previous period's unfiled transactions <i id="ember1412" class="tooltip-container ember-view" data-toggle="tooltip" data-placement="right" title="Tick this if you want the unfiled transactions (if any) of previous Tax periods, to be included in this Tax return"><svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-sm align-middle text-muted cursor-pointer"><path d="M255.4 31.9c30.2 0 59.4 5.9 87 17.5 26.6 11.2 50.4 27.4 71 47.9 20.5 20.5 36.6 44.3 47.9 71 11.6 27.6 17.5 56.7 17.5 87s-5.9 59.4-17.5 87c-11.2 26.6-27.4 50.4-47.9 71-20.5 20.5-44.3 36.6-71 47.9-27.6 11.6-56.7 17.5-87 17.5s-59.4-5.9-87-17.5c-26.6-11.2-50.4-27.4-71-47.9-20.5-20.5-36.6-44.3-47.9-71-11.6-27.6-17.5-56.7-17.5-87s5.9-59.4 17.5-87c11.2-26.6 27.4-50.4 47.9-71s44.3-36.6 71-47.9c27.5-11.6 56.7-17.5 87-17.5m0-31.9C114.3 0 0 114.3 0 255.4s114.3 255.4 255.4 255.4 255.4-114.3 255.4-255.4S396.4 0 255.4 0z"></path><path d="M303.4 351.1h-8.2c-4.4 0-8-3.6-8-8v-94.4c0-15.3-11.4-28-26.6-29.7-2.5-.3-4.8-.5-6.7-.5-23.6 0-44.4 11.9-56.8 30l-.1.1v-.1c-1 2-1.7 5.2.7 6.5.6.3 1.2.5 1.8.5h15.9c4.4 0 8 3.6 8 8v79.8c0 4.4-3.6 8-8 8h-8.1c-8.7 0-15.8 7.1-15.8 15.8v.3c0 8.7 7.1 15.8 15.8 15.8h96.1c8.7 0 15.8-7.1 15.8-15.8v-.3c0-8.9-7.1-16-15.8-16zM255.4 127.7c-17.6 0-31.9 14.3-31.9 31.9s14.3 31.9 31.9 31.9 31.9-14.3 31.9-31.9-14.3-31.9-31.9-31.9z"></path></svg></i> </label> </div></div>
		</div>
	</div>
	
	<div class="text-center">
		<h4><%=plantMst.get("PLNTDESC") %></h4> 
		<h3 class="reports-headerspacing"><%=plantMst.get("TAXBYLABEL") %> Return</h3> 
		<h5><span>From </span>&nbsp;<span id="inFrom"></span>&nbsp; <span>To </span>&nbsp;<span id="inTo"></span> </h5>
	</div>
	<div class="tax-main-block_sales">
		<div class="row">
			<label class="tax-return-header"><%=plantMst.get("TAXBYLABEL") %> on Sales and all other Outputs </label>
		</div>
		<div id="taxreturnfill-table1">
		</div>
	</div>
	<div class="tax-main-block_expense">
		<div class="row">
			<label class="tax-return-header"><%=plantMst.get("TAXBYLABEL") %> on Expenses and all other Inputs </label>
		</div>
		<div id="taxreturnfill-table2">
		</div>
	</div>
	<div class="tax-main-block_due">
		<div class="row">
			<label class="tax-return-header"> Net <%=plantMst.get("TAXBYLABEL") %> due </label>
		</div>
		<div id="taxreturnfill-table3">
		</div>
	</div>
		
</div>
</div> 
</div>
<div id="uae-taxreturnfilepopup" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<form class="form-horizontal" name="taxfilepopup" method="post">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-body">
				<div class="row form-group">
						<label class="control-label col-form-label col-sm-4 required"
							for="filedon">Filed On</label>
						<div class="col-sm-6">
						<INPUT class="form-control datepicker" id="filedon" type="TEXT" size="30" MAXLENGTH="10" name="filedon" placeholder="Select Date"/>
						</div>
				</div>
				</div>
				<div class="modal-footer">
	    		<button id="btnBillOpen" type="button" class="btn btn-success" onClick="fileTax();">Save</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	    </div>
			</div>
		</form>
	</div>
</div>
<%@include file="uae-taximportadjustment.jsp"%>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>