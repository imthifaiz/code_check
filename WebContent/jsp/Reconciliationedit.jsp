<%@page import="com.track.dao.PlantMstDAO"%>
<%@page import="com.track.dao.ReconciliationHdrDAO"%>
<%@page import="com.track.dao.CoaDAO"%>
<%@ page import="com.track.db.object.ReconciliationHdr"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%
	DateUtils _dateUtils = new DateUtils();
	String title = "Edit Bank Reconciliation";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	String curDate = _dateUtils.getDate();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String zeroval = StrUtils.addZeroes(0, numberOfDecimal);
	String cdate[] = curDate.split("/");
	String rmonth = cdate[1];
	String ryear = cdate[2];
	String rid = StrUtils.fString(request.getParameter("ID"));
	ReconciliationHdr rechdr = new ReconciliationHdrDAO().getByID(plant, rid);
	CoaDAO coaDAO=new CoaDAO();
	String accountid=coaDAO.GetIdByName(rechdr.getRACCOUNT(),plant);
	
	PlantMstDAO plantMstDAO = new PlantMstDAO();
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
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value=""/>
	<jsp:param name="submenu" value=""/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="../jsp/stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<script src="../jsp/js/Reconciliationedit.js"></script>
<style>
.extraInfo {
    border: 1px dashed #555;
    background-color: #f9f8f8;
    border-radius: 3px;
    color: #555;
    padding: 15px;
}
.offset-lg-7 {
    margin-left: 58.33333%;
}
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td, #table3>tbody>tr>td {
	border: none;
}

#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}

/* Style the tab */
.tab {
  overflow: hidden;
  border: 1px solid #ccc;
  background-color: #f1f1f1;
  line-height: 0.5;
}

/* Style the buttons that are used to open the tab content */
.tab button {
  background-color: inherit;
  float: left;
  border: none;
  outline: none;
  cursor: pointer;
  padding: 14px 16px;
  transition: 0.3s;
}

/* Change background color of buttons on hover */
.tab button:hover {
  background-color: #ddd;
}

/* Create an active/current tablink class */
.tab button.active {
  background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
  display: none;
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-top: none;
}
.payment-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
.voucher-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}

/* ----------------------------------------------- */

ul.simple-pagination {
	list-style: none;
}

.simple-pagination {
	display: block;
	overflow: hidden;
	padding: 0 5px 5px 0;
	margin: 0;
}

.simple-pagination ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

.simple-pagination li {
	list-style: none;
	padding: 0;
	margin: 0;
	float: left;
}

/*------------------------------------*\
	Compact Theme Styles
\*------------------------------------*/

.compact-theme a, .compact-theme span {
	float: left;
	color: #333;
	font-size:14px;
	line-height:24px;
	font-weight: normal;
	text-align: center;
	border: 1px solid #AAA;
	border-right: none;
	min-width: 14px;
	padding: 0 7px;
	box-shadow: 2px 2px 2px rgba(0,0,0,0.2);
	background: #efefef; /* Old browsers */
	background: -moz-linear-gradient(top, #ffffff 0%, #efefef 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#ffffff), color-stop(100%,#efefef)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #ffffff 0%,#efefef 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #ffffff 0%,#efefef 100%); /* Opera11.10+ */
	background: -ms-linear-gradient(top, #ffffff 0%,#efefef 100%); /* IE10+ */
	background: linear-gradient(top, #ffffff 0%,#efefef 100%); /* W3C */
}

.compact-theme a:hover {
	text-decoration: none;
	background: #efefef; /* Old browsers */
	background: -moz-linear-gradient(top, #efefef 0%, #bbbbbb 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#efefef), color-stop(100%,#bbbbbb)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #efefef 0%,#bbbbbb 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #efefef 0%,#bbbbbb 100%); /* Opera11.10+ */
	background: -ms-linear-gradient(top, #efefef 0%,#bbbbbb 100%); /* IE10+ */
	background: linear-gradient(top, #efefef 0%,#bbbbbb 100%); /* W3C */
}

.compact-theme .prev {
	border-radius: 3px 0 0 3px;
}

.compact-theme .next {
	border-right: 1px solid #AAA;
	border-radius: 0 3px 3px 0;
}

.compact-theme .current {
	background: #bbbbbb; /* Old browsers */
	background: -moz-linear-gradient(top, #bbbbbb 0%, #efefef 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#bbbbbb), color-stop(100%,#efefef)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #bbbbbb 0%,#efefef 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #bbbbbb 0%,#efefef 100%); /* Opera11.10+ */
	background: -ms-linear-gradient(top, #bbbbbb 0%,#efefef 100%); /* IE10+ */
	background: linear-gradient(top, #bbbbbb 0%,#efefef 100%); /* W3C */
	cursor: default;
}

.compact-theme .ellipse {
	background: #EAEAEA;
	padding: 0 10px;
	cursor: default;
}

/*------------------------------------*\
	Light Theme Styles
\*------------------------------------*/

.light-theme a, .light-theme span {
	float: left;
	color: #666;
	font-size:14px;
	line-height:24px;
	font-weight: normal;
	text-align: center;
	border: 1px solid #BBB;
	min-width: 14px;
	padding: 0 7px;
	margin: 0 5px 0 0;
	border-radius: 3px;
	box-shadow: 0 1px 2px rgba(0,0,0,0.2);
	background: #efefef; /* Old browsers */
	background: -moz-linear-gradient(top, #ffffff 0%, #efefef 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#ffffff), color-stop(100%,#efefef)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #ffffff 0%,#efefef 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #ffffff 0%,#efefef 100%); /* Opera11.10+ */
	background: -ms-linear-gradient(top, #ffffff 0%,#efefef 100%); /* IE10+ */
	background: linear-gradient(top, #ffffff 0%,#efefef 100%); /* W3C */
}

.light-theme a:hover {
	text-decoration: none;
	background: #FCFCFC;
}

.light-theme .current {
	background: #666;
	color: #FFF;
	border-color: #444;
	box-shadow: 0 1px 0 rgba(255,255,255,1), 0 0 2px rgba(0, 0, 0, 0.3) inset;
	cursor: default;
}

.light-theme .ellipse {
	background: none;
	border: none;
	border-radius: 0;
	box-shadow: none;
	font-weight: bold;
	cursor: default;
}

/*------------------------------------*\
	Dark Theme Styles
\*------------------------------------*/

.dark-theme a, .dark-theme span {
	float: left;
	color: #CCC;
	font-size:14px;
	line-height:24px;
	font-weight: normal;
	text-align: center;
	border: 1px solid #222;
	min-width: 14px;
	padding: 0 7px;
	margin: 0 5px 0 0;
	border-radius: 3px;
	box-shadow: 0 1px 2px rgba(0,0,0,0.2);
	background: #555; /* Old browsers */
	background: -moz-linear-gradient(top, #555 0%, #333 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#555), color-stop(100%,#333)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #555 0%,#333 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #555 0%,#333 100%); /* Opera11.10+ */
	background: -ms-linear-gradient(top, #555 0%,#333 100%); /* IE10+ */
	background: linear-gradient(top, #555 0%,#333 100%); /* W3C */
}

.dark-theme a:hover {
	text-decoration: none;
	background: #444;
}

.dark-theme .current {
	background: #222;
	color: #FFF;
	border-color: #000;
	box-shadow: 0 1px 0 rgba(255,255,255,0.2), 0 0 1px 1px rgba(0, 0, 0, 0.1) inset;
	cursor: default;
}

.dark-theme .ellipse {
	background: none;
	border: none;
	border-radius: 0;
	box-shadow: none;
	font-weight: bold;
	cursor: default;
}
/* ----------------------------------------------- */
.fixTableHead {
      overflow-y: auto;
    }
    .fixTableHead thead th {
      position: sticky;
      top: 0;
    }
    table {
      border-collapse: collapse;        
      width: 100%;
    }
    th {
      background: #c0c4be;
    }

</style>
<center>
	<h2>
		<small class="success-msg"><%=fieldDesc%></small>
	</h2>
</center>

<div class="container-fluid m-t-20">

	<div class="box">
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                 <li><a href="../accountant/bankreconcilationsummary"><span class="underline-on-hover">Bank Reconciliation Summary</span></a></li>	
                <li><label>Edit Bank Reconciliation</label></li>                                   
            </ul>
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class="box-title pull-right">
              <div class="btn-group" role="group">
			<button type="button" class="btn btn-default printMe" style="margin-right: 10px;" 
								 data-toggle="tooltipAZZ"  data-placement="bottom" title="Print">
									<i class="fa fa-print" aria-hidden="true"></i>
								</button>
								
								<button type="button" class="btn btn-default" id="rec-delete" style="margin-right: 10px;" 
								 data-toggle="tooltipAZZ"  data-placement="bottom" title="Delete">
									<i class="fa fa-trash" aria-hidden="true"></i>
								</button>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../accountant/bankreconcilationsummary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		</div>
		</div>




		<div class="container-fluid">
			<form class="form-horizontal" name="form" method="post" action="">
				<input type="text" name="plant" value="<%=plant%>" hidden> <input
					type="number" id="numberOfDecimal" style="display: none;"
					value=<%=numberOfDecimal%>> <input type="text"
					name="LOGIN_USER" value="<%=username%>" hidden>
					<input type="text" name="paidthrough" value="<%=accountid%>" hidden>
					<input type="text" name="datestatus" value="rdate" hidden>
					<input type="text" name="recdeposits" value="0" hidden>
					<input type="text" name="recwithdrawels" value="0" hidden>
					<input type="text" name="CDEPOSITS" value="0" hidden>
					<input type="text" name="CPAYMENTS" value="0" hidden>

					<div class="form-group" style="margin-left: 0%;">
						<div class="row">

			<div class="col-sm-12" style="margin-top: 1%;">
					
					<label class="col-form-modal-label col-sm-2">Bank Account</label>
					<div class="col-sm-4">
				        <div class="input-group">
					        <input class="form-control" name="paid_through_account_name" id="paid_through_account_name" type="text" value="<%=rechdr.getRACCOUNT() %>" style="width: 359px;" readonly> 
					        <span class="pay-select-icon"
								onclick="$(this).parent().find('input[name=\'paid_through_account_name\']').focus()">
							</span>
						</div>
					</div>
				    <label class="col-form-modal-label col-sm-2" style="position: relative;">Fin Mon/Year</label>
				    <div class="col-sm-4">
						<div class="col-sm-6" style="padding-left: 0%;width: 38%;">
							<div class="input-group">
						        <input class="form-control" name="MON" id="MON" type="text" value="<%=rechdr.getRMONTH()%>" readonly> 
							</div>
						</div>
						<div class="col-sm-6">
					        <div class="input-group">
						        <input class="form-control" name="YEAR" id="YEAR" type="text" value="<%=rechdr.getRYEAR()%>" style="position: relative;left: -27px;" readonly>
							</div>
						</div>
					</div>
			</div>
			
			
			<div class="col-sm-12" style="margin-top: 1%;">
					<label class="col-form-modal-label col-sm-2">Open Balance</label>
					<div class="col-sm-4">
      					<div class="input-group">    
          					<input readonly="" class="form-control" maxlength="50" size="6" value="<%=rechdr.getCURRENCYID()%>" type="TEXT" name="Basecurrency">
				  		    <span class="input-group-btn"></span>
    						<input readonly="" class="form-control" maxlength="50" size="45" value="1.000" type="TEXT" value="<%=rechdr.getBANKOPENBALANCE()%>" name="OPENBALANCE" style="width: 238px;" readonly>
   		 				</div>
   					</div>
					
				    <label class="col-form-modal-label col-sm-2" style="position: relative;">Ledger Open Bal</label>
					<div class="col-sm-4">
						<div class="input-group">
					        <input class="form-control" name="OPENBAL" id="OPENBAL" type="text" value="<%=rechdr.getOPENBALANCE()%>" style="width: 359px;" readonly>  
						</div>
					</div>
			</div>
			
			
			<div class="col-sm-12" style="margin-top: 1%;">
					
					<label class="col-form-modal-label col-sm-2">Statement Date</label>
					<div class="col-sm-4">
      					<div class="input-group">    
      						<input type="text" class="form-control datepicker" id="STATEMENTDATE" name="STATEMENTDATE" value="<%=rechdr.getRDATE()%>" readonly style="width: 359px;">
   		 				</div>
   					</div>
				    <label class="col-form-modal-label col-sm-2" style="position: relative;">Ledger Close Bal</label>
					<div class="col-sm-4">
						<div class="input-group">
					        <input class="form-control" name="CLOSEBAL" id="CLOSEBAL" type="text" value="<%=rechdr.getCLOSEBALANCE()%>" style="width: 359px;" readonly>  
						</div>
					</div>
			</div>
			
		
			
						
			<div class="col-sm-12" style="margin-top: 1%;">
					<label class="col-form-modal-label col-sm-2">Bank Balance</label>
					<div class="col-sm-4">
      					<div class="input-group">    
          					<input readonly="" class="form-control" maxlength="50" size="6" value="<%=curency%>" type="TEXT" name="Basecurrency">
				  		    <span class="input-group-btn"></span>
    						<input class="form-control" maxlength="50" size="45" type="TEXT" onchange="chgbkbal();" value="<%=rechdr.getBANKCLOSEBALANCE()%>" name="BANKBALANCE" style="width: 238px;">
   		 				</div>
   					</div>
			</div>
			
        	
							<div class="col-sm-12" style="margin-top:2%">
							<div class="col-sm-4">
							</div>
							<div class="col-sm-4" hidden>
								<button type="button" onclick="accountchanged()" class="btn btn-success">Search</button>
								</div>
								<div class="col-sm-4"></div>
							</div>

						</div>
					</div>

				<div id="VIEW_RESULT_HERE" class="table-responsive fixTableHead"  style="max-height: 350px;">
					<table id="tablerec"
						class="table table-bordred table-striped rec-table">
						<thead>
							<tr>
								<th style="font-size: smaller;width: 2.5%;" >CHK</th>
								<th style="font-size: smaller;width: 2.5%;" >VIEW</th>
								<th style="font-size: smaller;width: 10%;">DATE</th>
								<th style="font-size: smaller;width: 35%;">PARTICULARS</th>
								<th style="font-size: smaller;width: 10%;">VOUCHER TYPE</th>
								<!-- <th style="font-size: smaller;">INSTRUMENT DATE</th>
								<th style="font-size: smaller;">BANK DATE</th> -->
								<th style="font-size: smaller;width: 20%;">DEBIT</th>
								<th style="font-size: smaller;width: 20%;">CREDIT</th>
								
							</tr>
						</thead>
						<tbody id="recbody">
								
						</tbody>
					</table>
					
				</div>
				<div class="table-responsive">
				<table id="tablerectotal" class="table rec-table-total">
						<tbody id="recbodytotal">
								
						</tbody>
					</table>
				</div>
					<table id="tablerecprint" class="table recprint-table" style="display: none">
						<thead>
							<tr>
								<th style="font-size: smaller;">DATE</th>
								<th style="font-size: smaller;">PARTICULARS</th>
								<th style="font-size: smaller;">VOUCHER TYPE</th>
								<th style="font-size: smaller;">DEBIT</th>
								<th style="font-size: smaller;">CREDIT</th>
								
							</tr>
						</thead>
						<tbody id="recbodyprint">
								
						</tbody>
					</table>
				<div class="pagination-page"></div>
				<div class="row">
					<div class="col-xs-12">
						<table class="table text-right">
							<tbody>
								<tr>
									<td>Bank Balance As On</td>
									<td>:</td>
									<td id="cbookbal"><%=rechdr.getBANKCLOSEBALANCE() %></td>
								</tr>
								<tr>
									<td style="position: relative;right: -72px;color: red;">Less : Unclear Payments()</td>
									<td>:</td>
									<td id="cbookpayment" style="color: red;"><%=zeroval %></td>
								</tr>
								<tr>
									<td style="position: relative;right: -72px;color: blue;">Add : Unclear Deposits()</td>
									<td>:</td>
									<td id="cbookpending" style="color: blue;"><%=zeroval %></td>
								</tr>
								<tr>
									<td>Adjusted Balance</td>
									<td>:</td>
									<td  id="adjbal" style="color: blue;"><%=zeroval %></td>
								</tr>
								<tr>
									<td>Ledger Balance</td>
									<td>:</td>
									<td  id="ledgerbal" style="color: blue;"><%=rechdr.getCLOSEBALANCE()%></td>
								</tr>
								<tr>
									<td>Difference</td>
									<td>:</td>
									<td  id="bankdiffbal" style="color: red;"><%=zeroval %></td>
								</tr>
								
							</tbody>
						</table>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 text-center" style="margin-bottom: 2%;">
					<button type="button"
							onclick="recsubmitsave();"
							class="btn btn-success">update</button>
						<button type="button"
							onclick="recpopupcfm();"
							class="btn btn-success">Process Reconciliation</button>
					</div>
				</div> 
			</form>
		</div>
		
		<div id="reclcfm" class="modal fade" role="dialog">
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
			      <p> Are you sure about processing reconciliation?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" onclick="recsubmit();" style="background:red;">
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
		
	</div>
<!-- </div> -->

<div id="receive" class="tabcontent">
<div class="box"> 
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">PDC Payment Receive Process</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
             <!--  <i class="glyphicon glyphicon-remove"></i> -->
              </h1>
		</div>

</div>
</div>


<div id="deleterec" class="modal fade" role="dialog">
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
			      <p> Deleted Reconciled information cannot be retrieved. Are you sure about deleting ?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" onclick="recdelete();" style="background:red;">
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
	
	<div id="journalInModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg box-body">
			<form>
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h3 class="modal-title">Journal Detail</h3>
					</div>
					<div class="modal-body">
						<div class="row form-group">
							<div class="col-sm-12">
							 <table class="table table-striped rjournal-table">
							    <thead>
							      <tr>
							        <th>Account Name</th>
							        <th>Reference</th>
							        <th>Debit</th>
							        <th>Credit</th>
							      </tr>
							    </thead>
							    <tbody>
							     
							    </tbody>
							  </table>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>

</div>
<script>
var debittotal = 0;
var credittotal = 0;
$(document).ready(function(){
	$("#rdate").click(function(){
		$("input[name ='datestatus']").val("rdate");
	});

	$("#idate").click(function(){
		$("input[name ='datestatus']").val("idate");
	});
	
	accountchanged();
});


function changebankdate(obj){
	var idate = $(obj).closest('tr').find('input[name="idate"]').val();
	var bdate = $(obj).closest('tr').find('input[name="bankdate"]').val();
	idate = idate.split('/');
	bdate = bdate.split('/');
    var new_idate_date = new Date(idate[2],idate[1],idate[0]);
    var new_bdate_date = new Date(bdate[2],bdate[1],bdate[0]);
    if(new_idate_date <= new_bdate_date) {
    	
    }else{
    	if(bdate != ""){
	    	$(obj).parent().find('input[name="bankdate"]').val("");
	    	$(obj).parent().find('input[name="bankdate"]').focus();
	    	alert("Bank date should be greater than or equal to Intrument date");
    	}
    }
}
function stringToDate(_date,_format,_delimiter)
{
            var formatLowerCase=_format.toLowerCase();
            var formatItems=formatLowerCase.split(_delimiter);
            var dateItems=_date.split(_delimiter);
            var monthIndex=formatItems.indexOf("mm");
            var dayIndex=formatItems.indexOf("dd");
            var yearIndex=formatItems.indexOf("yyyy");
            var month=parseInt(dateItems[monthIndex]);
            month-=1;
            var formatedDate = new Date(dateItems[yearIndex],month,dateItems[dayIndex]);
            formatedDate.setDate(formatedDate.getDate() + 1);
            const yyyy = formatedDate.getFullYear();
            let mm = formatedDate.getMonth() + 1; // Months start at 0!
            let dd = formatedDate.getDate();

            if (dd < 10) dd = '0' + dd;
            if (mm < 10) mm = '0' + mm;

            const formattedToday = dd + '/' + mm + '/' + yyyy;

            return formattedToday;
}

function chkd(obj){
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var rdebit = $(obj).closest('tr').find('input[name="rdebit"]').val();
	var rcredit = $(obj).closest('tr').find('input[name="rcredit"]').val();	
	
	var recdeposit = $("input[name ='recdeposits']").val();
	var recwithdrawl = $("input[name ='recwithdrawels']").val();
	
	if (obj.checked) {
		debittotal = parseFloat(debittotal) - parseFloat(rdebit);
		credittotal = parseFloat(credittotal) - parseFloat(rcredit);
		$('#cbookpending').text(parseFloat(debittotal).toFixed(numberOfDecimal));
		//$('#cbookpayment').text("-"+parseFloat(credittotal).toFixed(numberOfDecimal));
		if(parseFloat(credittotal) <= 0){
			$('#cbookpayment').text(parseFloat(credittotal).toFixed(numberOfDecimal));
		}else{
			$('#cbookpayment').text("-"+parseFloat(credittotal).toFixed(numberOfDecimal));
		}
		$(obj).closest('tr').find('input[name="checkstatus"]').val("1");
		
		recdeposit = parseFloat(recdeposit) + parseFloat(rdebit);
		recwithdrawl = parseFloat(recwithdrawl) + parseFloat(rcredit);
		$("input[name ='recdeposits']").val(parseFloat(recdeposit).toFixed(numberOfDecimal));
		$("input[name ='recwithdrawels']").val(parseFloat(recwithdrawl).toFixed(numberOfDecimal));
		
		allcalculations();
	}else{
		debittotal = parseFloat(debittotal) + parseFloat(rdebit);
		credittotal = parseFloat(credittotal) + parseFloat(rcredit);
		$('#cbookpending').text(parseFloat(debittotal).toFixed(numberOfDecimal));
		//$('#cbookpayment').text("-"+parseFloat(credittotal).toFixed(numberOfDecimal));
		if(parseFloat(credittotal) <= 0){
			$('#cbookpayment').text(parseFloat(credittotal).toFixed(numberOfDecimal));
		}else{
			$('#cbookpayment').text("-"+parseFloat(credittotal).toFixed(numberOfDecimal));
		}
		$(obj).closest('tr').find('input[name="checkstatus"]').val("0");
		
		recdeposit = parseFloat(recdeposit) - parseFloat(rdebit);
		recwithdrawl = parseFloat(recwithdrawl) - parseFloat(rcredit);
		$("input[name ='recdeposits']").val(parseFloat(recdeposit).toFixed(numberOfDecimal));
		$("input[name ='recwithdrawels']").val(parseFloat(recwithdrawl).toFixed(numberOfDecimal));
		
		allcalculations();
	}
}

function chgbkbal(){
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var cgbankbal = $("input[name ='BANKBALANCE']").val();
	$('#cbookbal').text(parseFloat(cgbankbal).toFixed(numberOfDecimal));
	allcalculations();
}

function allcalculations(){
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var bankbalnce = $("input[name ='BANKBALANCE']").val();
	var bdeposits = $('#cbookpending').text();
	var bcredits = $('#cbookpayment').text();
	var adjustment = parseFloat(bankbalnce)+parseFloat(bdeposits)+parseFloat(bcredits);
	$('#adjbal').text(parseFloat(adjustment).toFixed(numberOfDecimal));
	var ledcbalance = $('#ledgerbal').text();
	var difference = parseFloat(adjustment)+parseFloat(ledcbalance)
	$('#bankdiffbal').text(parseFloat(difference).toFixed(numberOfDecimal));
}

function accountchanged()
{
	$("#recbody").html("");
	$("#recbodytotal").html("");
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var accountid = $("input[name ='paidthrough']").val();
	if(accountid == ""){
		 alert ("Please select a account.");
		 return false;
	}
	var fromdate =  $("input[name ='F_DATE']").val();
	var reconDate =  $("input[name ='STATEMENTDATE']").val();
	var accountname = $("input[name ='paid_through_account_name']").val();
	var todate =  $("input[name ='STATEMENTDATE']").val();
	if(todate.length > 0){
	var ccdate = stringToDate(todate,"dd/MM/yyyy","/");
	todate = ccdate.toString();
	}
	var cdeposits=0;
	var cpayments=0;
	var datestatus =  $("input[name ='datestatus']").val();
    if(accountid!=""||accountid!=null)
    	{
			var urlStr = "/track/JournalServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				data: {
					"action":"Get_Reconciliation_Details_Edit_re",
					"fromDate":fromdate,
					"reconDate":reconDate,
					"toDate":todate,
					"datestatus":datestatus,
					"AccountId":accountid,
					"accountname":accountname,
					"rid":<%=rid%>,
				}, 
		        success: function (data) {
		        	var json = $.parseJSON(data);
					/* var recList=json.REC;
		        	console.log(recList);
		        	if(recList.length>0){ */
		        	var rstatus=json.STATUS;
		        	var recList=json.REC;
		        	console.log(recList);
		        	var recListedit=json.RECEDIT;
		        	console.log("edit ------------- edit");
		        	console.log(recListedit);
		        	console.log("edit ------------- edit");
		        	if(rstatus === 0){
		        		var tdebit = 0;
		        		var tcredit = 0;
		        		$.each(recList,function(i,r){
		        			var zeroval = parseFloat("0").toFixed(numberOfDecimal);
		        			var body="";
		        			body += '<tr>';
		        			body += '<td class="text-left">';
		        			body += '<input class="form-check-input" type="Checkbox" onclick="chkd(this)" name="CHKD" value="">';
		        			body += '<input type="text" name="checkstatus" value="0" hidden>';
		        			body += '<input type="hidden" name="rdebit" value="'+parseFloat(r.DEBIT).toFixed(numberOfDecimal)+'">';
		        			body += '<input type="hidden" name="rcredit" value="'+parseFloat(r.CREDIT).toFixed(numberOfDecimal)+'">';
		        			body += '<input type="text" name="jid" value="'+r.ID+'" hidden>';
		        			body += '<input type="text" name="idate" value="'+r.INSTRUMENTDATE+'" hidden>';
		        			body += '</td>';
		        			body += '<td class="text-left"><a style="color: inherit;" onclick="journaltransaction('+r.ID+')"><i class="fa fa-eye" style="color:#167dd6"></i></a></td>';
		        			body += '<td class="text-left">'+r.DATE+'</td>';
		        			body += '<td class="text-left">'+r.ACCOUNT+'</td>';
		        			body += '<td class="text-left">'+r.VOUCHERTYPE+'</td>';
		        			/* body += '<td class="text-left">'+r.INSTRUMENTDATE+'</td>';
		        			body += '<td class="text-left">';
		        			body += '<input class="form-control datepicker" type="text" onchange="changebankdate(this)" name="bankdate" placeholder="Enter Bank Date">';
		        			
		        			body += '</td>'; */
		        			body += '<td class="text-left">'+parseFloat(r.DEBIT).toFixed(numberOfDecimal)+'</td>';
		        			body += '<td class="text-left">'+parseFloat(r.CREDIT).toFixed(numberOfDecimal)+'</td>';
		        			body += '</tr>';
		        			$(".rec-table tbody").append(body);
		        			//print pdf
		        			var bodyprt="";
		        			bodyprt += '<tr>';
		        			bodyprt += '<td class="text-left">'+r.DATE+'</td>';
		        			bodyprt += '<td class="text-left">'+r.ACCOUNT+'</td>';
		        			bodyprt += '<td class="text-left">'+r.VOUCHERTYPE+'</td>';
		        			bodyprt += '<td class="text-left">'+parseFloat(r.DEBIT).toFixed(numberOfDecimal)+'</td>';
		        			bodyprt += '<td class="text-left">'+parseFloat(r.CREDIT).toFixed(numberOfDecimal)+'</td>';
		        			bodyprt += '</tr>';
		        			$(".recprint-table tbody").append(bodyprt);
		        			addrowclasses();
		        			tdebit = parseFloat(tdebit) + parseFloat(r.DEBIT);
		        			tcredit = parseFloat(tcredit) + parseFloat(r.CREDIT);
		        		});
		        		
		        		$.each(recListedit,function(i,r){
		        			var zeroval = parseFloat("0").toFixed(numberOfDecimal);
		        			var body="";
		        			body += '<tr>';
		        			body += '<td class="text-left">';
		        			body += '<input class="form-check-input" type="Checkbox" onclick="chkd(this)" name="CHKD" value="" checked>';
		        			body += '<input type="text" name="checkstatus" value="1" hidden>';
		        			body += '<input type="hidden" name="rdebit" value="'+parseFloat(r.DEBIT).toFixed(numberOfDecimal)+'">';
		        			body += '<input type="hidden" name="rcredit" value="'+parseFloat(r.CREDIT).toFixed(numberOfDecimal)+'">';
		        			body += '<input type="text" name="jid" value="'+r.ID+'" hidden>';
		        			body += '<input type="text" name="idate" value="'+r.INSTRUMENTDATE+'" hidden>';
		        			body += '</td>';
		        			body += '<td class="text-left"><a style="color: inherit;" onclick="journaltransaction('+r.ID+')"><i class="fa fa-eye" style="color:#167dd6"></i></a></td>';
		        			body += '<td class="text-left">'+r.DATE+'</td>';
		        			body += '<td class="text-left">'+r.ACCOUNT+'</td>';
		        			body += '<td class="text-left">'+r.VOUCHERTYPE+'</td>';
		        			/* body += '<td class="text-left">'+r.INSTRUMENTDATE+'</td>';
		        			body += '<td class="text-left">'+r.DATE+'</td>'; */
		        			body += '<td class="text-left">'+parseFloat(r.DEBIT).toFixed(numberOfDecimal)+'</td>';
		        			body += '<td class="text-left">'+parseFloat(r.CREDIT).toFixed(numberOfDecimal)+'</td>';
		        			body += '</tr>';
		        			$(".rec-table tbody").append(body);
		        			addrowclasses();
		        			cdeposits=parseFloat(cdeposits)+parseFloat(r.DEBIT);
		        			cpayments=parseFloat(cpayments)+parseFloat(r.CREDIT);
		        			
		        			tdebit = parseFloat(tdebit) + parseFloat(r.DEBIT);
		        			tcredit = parseFloat(tcredit) + parseFloat(r.CREDIT);
		        		});
		        		
		        		/* var bodyfooter="";
		        		bodyfooter += '<tr>';
		        		bodyfooter += '<td class="text-left"></td>';
		        		bodyfooter += '<td class="text-left"></td>';
		        		bodyfooter += '<td class="text-left"></td>';
		        		bodyfooter += '<td class="text-left" style="font-weight:bold">Total</td>';
		        		bodyfooter += '<td class="text-left" style="font-weight:bold">'+parseFloat(tdebit).toFixed(numberOfDecimal)+'</td>';
		        		bodyfooter += '<td class="text-left" style="font-weight:bold">'+parseFloat(tcredit).toFixed(numberOfDecimal)+'</td>';
		        		bodyfooter += '</tr>';
	        			$(".rec-table tbody").append(bodyfooter); */
	        			
		        		var bodyfootert="";
		        		bodyfootert += '<tr>';
		        		bodyfootert += '<td class="text-left" style="width: 5%;"></td>';
		        		bodyfootert += '<td class="text-left" style="width: 10%;"></td>';
		        		bodyfootert += '<td class="text-left" style="width: 35%;"></td>';
		        		bodyfootert += '<td class="text-left" style="font-weight:bold;width: 10%;">Total</td>';
		        		bodyfootert += '<td class="text-left" style="font-weight:bold;width: 20%;">'+parseFloat(tdebit).toFixed(numberOfDecimal)+'</td>';
		        		bodyfootert += '<td class="text-left" style="font-weight:bold;width: 20%;">'+parseFloat(tcredit).toFixed(numberOfDecimal)+'</td>';
		        		bodyfootert += '</tr>';
	        			$(".rec-table-total tbody").append(bodyfootert);
		        	}else if(rstatus === 1){
		        		$("#recbody").html("");
		        		$("#recbodytotal").html("");
		        		alert("This Financial Month/Year Already Reconcilied")
		        	}else{
		        		$("#recbody").html("");
		        		$("#recbodytotal").html("");
		        	}
		        	
		        	//$('#cbookbal').text(parseFloat(json.CBOOK).toFixed(numberOfDecimal));
	        		//$('#cbookpending').text(parseFloat(json.PENDINGS).toFixed(numberOfDecimal));
	        		//$('#bankbal').text(parseFloat(json.BANK).toFixed(numberOfDecimal));
	        		$('#cbookpending').text(parseFloat(json.RDEPOSIT).toFixed(numberOfDecimal));
	        		$('#cbookpayment').text("-"+parseFloat(json.RCREDIT).toFixed(numberOfDecimal));
	        		$('#ledgerbal').text("-"+parseFloat(json.LEDGERCLOSINGGBAL).toFixed(numberOfDecimal));
	        		$("input[name ='YEAR']").val(json.RYEAR);
	        		$("input[name ='MON']").val(json.RMONTH);
	        		$("input[name ='OPENBALANCE']").val(parseFloat(json.BANKOPENINGBAL).toFixed(numberOfDecimal));
	        		$("input[name ='OPENBAL']").val(parseFloat(json.LEDGEROPENINGBAL).toFixed(numberOfDecimal));
	        		$("input[name ='CLOSEBAL']").val(parseFloat(json.LEDGERCLOSINGGBAL).toFixed(numberOfDecimal));
	        		$("input[name ='CDEPOSITS']").val(cdeposits);
	        		$("input[name ='CPAYMENTS']").val(cpayments);
	        		debittotal = parseFloat(json.RDEPOSIT).toFixed(numberOfDecimal);
	        		credittotal = parseFloat(json.RCREDIT).toFixed(numberOfDecimal);
	        		allcalculations();
		        },
		        cache: false
			});
			
			allcalculations();
    	}
}
    
function recsubmitsave(){
	var bdate = [], jhdrid = [];
	var accountid = $("input[name ='paidthrough']").val();
	$(".rec-table tbody tr").each(function() {
		 //var bankdate = $(this).find("input[name ='bankdate']").val();
		 var jid = $(this).find("input[name ='jid']").val();
		 //if(bankdate != ""){
		if ($(this).find("input[name ='checkstatus']").val() == "1") {
			 //bdate.push(bankdate);
			 jhdrid.push(jid);
		 }
	});
	
	if(jhdrid.length > 0){
		var recdate = $("input[name ='STATEMENTDATE']").val();
		var reccid = $("input[name ='Basecurrency']").val();
		var recmonth = $("input[name ='MON']").val();
		var recyear = $("input[name ='YEAR']").val();
		var recbopenbal = $("input[name ='OPENBALANCE']").val();
		var recbclosebal = $("input[name ='BANKBALANCE']").val();
		var recopenbal = $("input[name ='OPENBAL']").val();
		var recclosebal = $("input[name ='CLOSEBAL']").val();
		var recdeposit = $("input[name ='recdeposits']").val();
		var recwithdrawl = $("input[name ='recwithdrawels']").val();
		var accountname = $("input[name ='paid_through_account_name']").val();
		
		var urlStr = "/track/JournalServlet";
		$.ajax( {
			type : "GET",
			url : urlStr,
			data: {
				"action":"Process_Reconciliation_Edit_re",
				/* "bdate":bdate, */
				"jid":jhdrid,
				"rid":<%=rid%>,
				"AccountId":accountid,
				"recdate":recdate,
				"reccid":reccid,
				"recmonth":recmonth,
				"recyear":recyear,
				"recbopenbal":recbopenbal,
				"recbclosebal":recbclosebal,
				"recopenbal":recopenbal,
				"recclosebal":recclosebal,
				"recdeposit":recdeposit,
				"recwithdrawl":recwithdrawl,
				"accountname":accountname,
				"rstatus":"open"
			}, 
	        success: function (data) {
	 		 	var json = $.parseJSON(data);
	        	console.log(json); 
	        	if(json.STATUS == "OK"){
	        		window.location.href = '../accountant/bankreconcilationsummary?result=Reconciliation process successfully';
	        	}
	        	if(json.STATUS == "NOT OK"){
	        		window.location.href = '../accountant/bankreconcilation';
	        	}
	        },
	        cache: false
		});
	}else{
		alert("please selecr bank date");
	} 

}
	
function recpopupcfm(){
	var difference =$('#bankdiffbal').text();
	if(parseFloat(difference) != parseFloat("0") ){
		alert("Unable to process reconciliation until difference amount zero");
	}else{
		$('#reclcfm').modal('show');
	}
}

function recsubmit(){
	var bdate = [], jhdrid = [];
	var accountid = $("input[name ='paidthrough']").val();
	$(".rec-table tbody tr").each(function() {
		 //var bankdate = $(this).find("input[name ='bankdate']").val();
		 var jid = $(this).find("input[name ='jid']").val();
		 //if(bankdate != ""){
		if ($(this).find("input[name ='checkstatus']").val() == "1") {
			 //bdate.push(bankdate);
			 jhdrid.push(jid);
		 }
	});
	
	if(jhdrid.length > 0){
		var recdate = $("input[name ='STATEMENTDATE']").val();
		var reccid = $("input[name ='Basecurrency']").val();
		var recmonth = $("input[name ='MON']").val();
		var recyear = $("input[name ='YEAR']").val();
		var recbopenbal = $("input[name ='OPENBALANCE']").val();
		var recbclosebal = $("input[name ='BANKBALANCE']").val();
		var recopenbal = $("input[name ='OPENBAL']").val();
		var recclosebal = $("input[name ='CLOSEBAL']").val();
		var recdeposit = $("input[name ='recdeposits']").val();
		var recwithdrawl = $("input[name ='recwithdrawels']").val();
		var accountname = $("input[name ='paid_through_account_name']").val();
		
		var urlStr = "/track/JournalServlet";
		$.ajax( {
			type : "GET",
			url : urlStr,
			data: {
				"action":"Process_Reconciliation_Edit_re",
				/* "bdate":bdate, */
				"jid":jhdrid,
				"rid":<%=rid%>,
				"AccountId":accountid,
				"recdate":recdate,
				"reccid":reccid,
				"recmonth":recmonth,
				"recyear":recyear,
				"recbopenbal":recbopenbal,
				"recbclosebal":recbclosebal,
				"recopenbal":recopenbal,
				"recclosebal":recclosebal,
				"recdeposit":recdeposit,
				"recwithdrawl":recwithdrawl,
				"accountname":accountname,
				"rstatus":"close"
			}, 
	        success: function (data) {
	 		 	var json = $.parseJSON(data);
	        	console.log(json); 
	        	if(json.STATUS == "OK"){
	        		window.location.href = '../accountant/bankreconcilationsummary?result=Reconciliation process successfully';
	        	}
	        	if(json.STATUS == "NOT OK"){
	        		window.location.href = '../accountant/bankreconcilation';
	        	}
	        },
	        cache: false
		});
	}else{
		alert("please selecr bank date");
	} 

}

function addrowclasses(){
	var plant = document.form.plant.value;
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
	
	/* jQuery(function($) {
	    // Consider adding an ID to your table
	    // incase a second table ever enters the picture.
	    var items = $("#tablerec tbody tr");

	    var numItems = items.length;
	    var perPage = 10;

	    // Only show the first 2 (or first `per_page`) items initially.
	    items.slice(perPage).hide();

	    // Now setup the pagination using the `.pagination-page` div.
	    $(".pagination-page").pagination({
	        items: numItems,
	        itemsOnPage: perPage,
	        cssStyle: "light-theme",

	        // This is the actual page changing functionality.
	        onPageClick: function(pageNumber) {
	            // We need to show and hide `tr`s appropriately.
	            var showFrom = perPage * (pageNumber - 1);
	            var showTo = showFrom + perPage;

	            // We'll first hide everything...
	            items.hide()
	                 // ... and then only show the appropriate rows.
	                 .slice(showFrom, showTo).show();
	        }
	    });
	}); */
}



</script>



<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

<script>

/**
* simplePagination.js v1.6
* A simple jQuery pagination plugin.
* http://flaviusmatis.github.com/simplePagination.js/
*
* Copyright 2012, Flavius Matis
* Released under the MIT license.
* http://flaviusmatis.github.com/license.html
*/

(function($){

	var methods = {
		init: function(options) {
			var o = $.extend({
				items: 1,
				itemsOnPage: 1,
				pages: 0,
				displayedPages: 5,
				edges: 2,
				currentPage: 1,
				hrefTextPrefix: '#page-',
				hrefTextSuffix: '',
				prevText: 'Prev',
				nextText: 'Next',
				ellipseText: '&hellip;',
				cssStyle: 'light-theme',
				labelMap: [],
				selectOnClick: true,
				onPageClick: function(pageNumber, event) {
					// Callback triggered when a page is clicked
					// Page number is given as an optional parameter
				},
				onInit: function() {
					// Callback triggered immediately after initialization
				}
			}, options || {});

			var self = this;

			o.pages = o.pages ? o.pages : Math.ceil(o.items / o.itemsOnPage) ? Math.ceil(o.items / o.itemsOnPage) : 1;
			o.currentPage = o.currentPage - 1;
			o.halfDisplayed = o.displayedPages / 2;

			this.each(function() {
				self.addClass(o.cssStyle + ' simple-pagination').data('pagination', o);
				methods._draw.call(self);
			});

			o.onInit();

			return this;
		},

		selectPage: function(page) {
			methods._selectPage.call(this, page - 1);
			return this;
		},

		prevPage: function() {
			var o = this.data('pagination');
			if (o.currentPage > 0) {
				methods._selectPage.call(this, o.currentPage - 1);
			}
			return this;
		},

		nextPage: function() {
			var o = this.data('pagination');
			if (o.currentPage < o.pages - 1) {
				methods._selectPage.call(this, o.currentPage + 1);
			}
			return this;
		},

		getPagesCount: function() {
			return this.data('pagination').pages;
		},

		getCurrentPage: function () {
			return this.data('pagination').currentPage + 1;
		},

		destroy: function(){
			this.empty();
			return this;
		},

		drawPage: function (page) {
			var o = this.data('pagination');
			o.currentPage = page - 1;
			this.data('pagination', o);
			methods._draw.call(this);
			return this;
		},

		redraw: function(){
			methods._draw.call(this);
			return this;
		},

		disable: function(){
			var o = this.data('pagination');
			o.disabled = true;
			this.data('pagination', o);
			methods._draw.call(this);
			return this;
		},

		enable: function(){
			var o = this.data('pagination');
			o.disabled = false;
			this.data('pagination', o);
			methods._draw.call(this);
			return this;
		},

		updateItems: function (newItems) {
			var o = this.data('pagination');
			o.items = newItems;
			o.pages = methods._getPages(o);
			this.data('pagination', o);
			methods._draw.call(this);
		},

		updateItemsOnPage: function (itemsOnPage) {
			var o = this.data('pagination');
			o.itemsOnPage = itemsOnPage;
			o.pages = methods._getPages(o);
			this.data('pagination', o);
			methods._selectPage.call(this, 0);
			return this;
		},

		_draw: function() {
			var	o = this.data('pagination'),
				interval = methods._getInterval(o),
				i,
				tagName;

			methods.destroy.call(this);
			
			tagName = (typeof this.prop === 'function') ? this.prop('tagName') : this.attr('tagName');

			var $panel = tagName === 'UL' ? this : $('<ul></ul>').appendTo(this);

			// Generate Prev link
			if (o.prevText) {
				methods._appendItem.call(this, o.currentPage - 1, {text: o.prevText, classes: 'prev'});
			}

			// Generate start edges
			if (interval.start > 0 && o.edges > 0) {
				var end = Math.min(o.edges, interval.start);
				for (i = 0; i < end; i++) {
					methods._appendItem.call(this, i);
				}
				if (o.edges < interval.start && (interval.start - o.edges != 1)) {
					$panel.append('<li class="disabled"><span class="ellipse">' + o.ellipseText + '</span></li>');
				} else if (interval.start - o.edges == 1) {
					methods._appendItem.call(this, o.edges);
				}
			}

			// Generate interval links
			for (i = interval.start; i < interval.end; i++) {
				methods._appendItem.call(this, i);
			}

			// Generate end edges
			if (interval.end < o.pages && o.edges > 0) {
				if (o.pages - o.edges > interval.end && (o.pages - o.edges - interval.end != 1)) {
					$panel.append('<li class="disabled"><span class="ellipse">' + o.ellipseText + '</span></li>');
				} else if (o.pages - o.edges - interval.end == 1) {
					methods._appendItem.call(this, interval.end++);
				}
				var begin = Math.max(o.pages - o.edges, interval.end);
				for (i = begin; i < o.pages; i++) {
					methods._appendItem.call(this, i);
				}
			}

			// Generate Next link
			if (o.nextText) {
				methods._appendItem.call(this, o.currentPage + 1, {text: o.nextText, classes: 'next'});
			}
		},

		_getPages: function(o) {
			var pages = Math.ceil(o.items / o.itemsOnPage);
			return pages || 1;
		},

		_getInterval: function(o) {
			return {
				start: Math.ceil(o.currentPage > o.halfDisplayed ? Math.max(Math.min(o.currentPage - o.halfDisplayed, (o.pages - o.displayedPages)), 0) : 0),
				end: Math.ceil(o.currentPage > o.halfDisplayed ? Math.min(o.currentPage + o.halfDisplayed, o.pages) : Math.min(o.displayedPages, o.pages))
			};
		},

		_appendItem: function(pageIndex, opts) {
			var self = this, options, $link, o = self.data('pagination'), $linkWrapper = $('<li></li>'), $ul = self.find('ul');

			pageIndex = pageIndex < 0 ? 0 : (pageIndex < o.pages ? pageIndex : o.pages - 1);

			options = {
				text: pageIndex + 1,
				classes: ''
			};

			if (o.labelMap.length && o.labelMap[pageIndex]) {
				options.text = o.labelMap[pageIndex];
			}

			options = $.extend(options, opts || {});

			if (pageIndex == o.currentPage || o.disabled) {
				if (o.disabled) {
					$linkWrapper.addClass('disabled');
				} else {
					$linkWrapper.addClass('active');
				}
				$link = $('<span class="current">' + (options.text) + '</span>');
			} else {
				$link = $('<a href="' + o.hrefTextPrefix + (pageIndex + 1) + o.hrefTextSuffix + '" class="page-link">' + (options.text) + '</a>');
				$link.click(function(event){
					return methods._selectPage.call(self, pageIndex, event);
				});
			}

			if (options.classes) {
				$link.addClass(options.classes);
			}

			$linkWrapper.append($link);

			if ($ul.length) {
				$ul.append($linkWrapper);
			} else {
				self.append($linkWrapper);
			}
		},

		_selectPage: function(pageIndex, event) {
			var o = this.data('pagination');
			o.currentPage = pageIndex;
			if (o.selectOnClick) {
				methods._draw.call(this);
			}
			return o.onPageClick(pageIndex + 1, event);
		}

	};

	$.fn.pagination = function(method) {

		// Method calling logic
		if (methods[method] && method.charAt(0) != '_') {
			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error('Method ' +  method + ' does not exist on jQuery.pagination');
		}

	};

})(jQuery);

$('.printMe').click(function(){
	printdoc();
});

function printdoc()
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfPrint(dataUrl);
		  	},'image/jpeg');
	
	
	}

function generatePdfPrint(dataUrl){
	//var data = table_1.getData();
	//var json = JSON.stringify(data);
	//console.log(table_1);
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var bdeposits = $('#cbookpending').text();
	var bpayments = $('#cbookpayment').text();
	//var badjbal = $('#adjbal').text();
	//var bledgerbal = $('#ledgerbal').text();
	
	
	//var bankdiffbal = $('#bankdiffbal').text();

	var cdeposits = parseFloat($("input[name ='CDEPOSITS']").val()).toFixed(numberOfDecimal);
	var cpayments = parseFloat($("input[name ='CPAYMENTS']").val()).toFixed(numberOfDecimal);
	var relbankbal = parseFloat($("input[name ='BANKBALANCE']").val()).toFixed(numberOfDecimal);
	var totcleared = parseFloat(cdeposits)-parseFloat(cpayments);
	console.log("totcleared----"+totcleared);
	var bankop = '<%=rechdr.getBANKOPENBALANCE()%>';
	console.log("bankop----"+bankop);
	var gdtotcleared = parseFloat(bankop)+parseFloat(totcleared);
	console.log("gdtotcleared----"+gdtotcleared);
	var badjbal = parseFloat(bdeposits)+parseFloat(bpayments);
	console.log("badjbal----"+badjbal);
	var bledgerbal = gdtotcleared+badjbal;
	console.log("bledgerbal----"+bledgerbal);
	totcleared=parseFloat(totcleared).toFixed(numberOfDecimal);
	gdtotcleared=parseFloat(gdtotcleared).toFixed(numberOfDecimal);
	
	//var bankdiffbal = (parseFloat(gdtotcleared)+parseFloat(badjbal))-parseFloat(bledgerbal);
	
	console.log("relbankbal----"+relbankbal);
	console.log("badjbal----"+badjbal);
	console.log("bledgerbal----"+bledgerbal);
	
	var bankdiffbal = (parseFloat(relbankbal)+parseFloat(badjbal))-parseFloat(bledgerbal);
	bankdiffbal = parseFloat(bankdiffbal).toFixed(numberOfDecimal);
	var dbadjbal = parseFloat(badjbal).toFixed(numberOfDecimal);
	var dbledgerbal = parseFloat(bledgerbal).toFixed(numberOfDecimal);
	
	var doc = new jsPDF('p', 'mm', 'a4');
	var cwidth = doc.internal.pageSize.getWidth()/2;
	var pageNumber;

	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 12, 35,15);
	var rY=35;	/*right Y-axis*/
	doc.setFontSize(10);
	doc.setFontStyle("bold");
	doc.text('<%=PLNTDESC%>', 16, rY+2);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, rY+=6);
	doc.text('<%=fromAddress_RoadAddress%>', 16, rY+=4);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, rY+=4);

	doc.setFontSize(20);
	doc.text('Bank Reconcil',cwidth,rY+=4,'center');
	
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('Account Name: <%=rechdr.getRACCOUNT()%>', 20, rY+=10);
	doc.text('Statement Date: <%=rechdr.getRDATE()%>', 20, rY+=4);

	doc.setFontSize(10);
	doc.text('Statement Open Balance:', 20, rY+=10);
	doc.text('<%=rechdr.getBANKOPENBALANCE()%>', 195, rY, {align:'right'});
	
	doc.text('Cleared Transaction', 20, rY+=6);
	
	doc.setFontSize(9);
	doc.text('235', 30, rY+=6);
	doc.text('Deposits', 50, rY);
	doc.text(cdeposits, 170, rY, {align:'right'});

	doc.text('109', 30, rY+=6);
	doc.text('Payments', 50, rY);
	doc.text('-'+cpayments, 170, rY, {align:'right'});
	
	doc.text('Total Cleared', 20, rY+=6);
	doc.text(totcleared, 170, rY, {align:'right'});
	doc.text(gdtotcleared, 195, rY, {align:'right'});

	doc.setFontSize(10);
	doc.text('Uncleared Transaction', 20, rY+=6);
	doc.setFontSize(9);
	doc.text('5', 30, rY+=6);
	doc.text('Deposits', 50, rY);
	doc.text(bdeposits, 170, rY, {align:'right'});

	doc.text('0', 30, rY+=6);
	doc.text('Payments', 50, rY);
	doc.text(bpayments, 170, rY, {align:'right'});
	
	console.log(dbadjbal);
	console.log(dbledgerbal);
	
	doc.text('Total Uncleared', 20, rY+=6);
	doc.text(dbadjbal.toString(), 170, rY, {align:'right'});
	doc.text(dbledgerbal.toString(), 195, rY, {align:'right'});

	rY+=6;
	doc.line(16, rY, 200, rY);

	doc.text('Statement Balance As on <%=rechdr.getRDATE()%>', 20, rY+=6);
	const textWidth = doc.getTextWidth('Statement Balance As on <%=rechdr.getRDATE()%>');
	doc.line(20, rY+1, 20+textWidth, rY+1);
	doc.text(gdtotcleared, 170, rY, {align:'right'});

	doc.text('Uncleared Amount', 20, rY+=6);
	doc.text(dbadjbal.toString(), 170, rY, {align:'right'});

	doc.text('Ledger Balance', 20, rY+=6);
	doc.text(dbledgerbal.toString(), 170, rY, {align:'right'});

	doc.text('Reconcillation Difference', 20, rY+=6);
	doc.text(bankdiffbal.toString(), 170, rY, {align:'right'});
	
	doc.text('Uncleared Transaction Details', 16, rY+=6);

	doc.setFontSize(10);
	var totalPagesExp = "{total_pages_cont_string}";
	
	doc.autoTable({
		html : '#tablerecprint',
		startY : rY+=4,
		headStyles : {
			fillColor : [ 0, 0, 0 ],
			textColor : [ 255, 255, 255 ],
			halign : 'center',
			fontSize : 10
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ],
			fontSize : 10
		},
		footStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ],
			fontStyle : 'normal',
			halign : 'right'
		},
		theme : 'plain',
		columnStyles: {0: {halign: 'center'},1: {halign: 'center'},2: {halign: 'center'},3: {halign: 'right'},4: {halign: 'right'}},
		didDrawPage : function(data) {
			doc.setFontStyle("normal");
			// Footer
			pageNumber = doc.internal.getNumberOfPages();
			var str = "Page " + doc.internal.getNumberOfPages();
			// Total page number plugin only available in jspdf v1.0+
			if (typeof doc.putTotalPages === 'function') {
				str = str + " of " + totalPagesExp;
			}
			doc.setFontSize(10);

			// jsPDF 1.4+ uses getWidth, <1.4 uses .width
			var pageSize = doc.internal.pageSize;
			var pageHeight = pageSize.height ? pageSize.height
					: pageSize.getHeight();
			doc.text(str, 185,
					pageHeight - 10);
		},
		didParseCell : function(data) {						
			for(i=0;i<data.table.body.length;i++){
				if(data.table.body[i].cells[4] != undefined){
					data.table.body[i].cells[4].styles["halign"]="right";
				}
			}
		}
	});

	if(pageNumber < doc.internal.getNumberOfPages()){
		// Footer
		var str = "Page " + doc.internal.getNumberOfPages()
		// Total page number plugin only available in jspdf v1.0+
		if (typeof doc.putTotalPages === 'function') {
			str = str + " of " + totalPagesExp;
		}
		doc.setFontSize(10);

		// jsPDF 1.4+ uses getWidth, <1.4 uses .width
		var pageSize = doc.internal.pageSize;
		var pageHeight = pageSize.height ? pageSize.height
				: pageSize.getHeight();
		doc.text(str, 16, pageHeight - 10);
	}
	// Total page number plugin only available in jspdf v1.0+
	if (typeof doc.putTotalPages === 'function') {
		doc.putTotalPages(totalPagesExp);
	}
	doc.autoPrint();
	const hiddFrame = document.createElement('iframe');
	hiddFrame.style.position = 'fixed';
	hiddFrame.style.width = '1px';
	hiddFrame.style.height = '1px';
	hiddFrame.style.opacity = '0.01';
	const isSafari = /^((?!chrome|android).)*safari/i.test(window.navigator.userAgent);
	if (isSafari) {
	  // fallback in safari
	  hiddFrame.onload = () => {
	    try {
	      hiddFrame.contentWindow.document.execCommand('print', false, null);
	    } catch (e) {
	      hiddFrame.contentWindow.print();
	    }
	  };
	}
	hiddFrame.src = doc.output('bloburl');
	document.body.appendChild(hiddFrame);
}

function toDataURL(src, callback, outputFormat) {
	  var img = new Image();
	  img.crossOrigin = 'Anonymous';
	  img.onload = function() {
	    var canvas = document.createElement('CANVAS');
	    var ctx = canvas.getContext('2d');
	    var dataURL;
	    canvas.height = this.naturalHeight;
	    canvas.width = this.naturalWidth;
	    ctx.drawImage(this, 0, 0);
	    dataURL = canvas.toDataURL(outputFormat);
	    callback(dataURL);
	  };
	  img.src = src;
	  if (img.complete || img.complete === undefined) {
	    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
	    img.src = src;
	  }
}


function recdelete(){
		var urlStr = "/track/JournalServlet";
		$.ajax( {
			type : "GET",
			url : urlStr,
			data: {
				"action":"Reconciliation_Delete",
				"rid":<%=rid%>,
			}, 
	        success: function (data) {
	 		 	var json = $.parseJSON(data);
	        	console.log(json); 
	        	if(json.STATUS == "OK"){
	        		window.location.href = '../accountant/bankreconcilationsummary?result=Reconciliation Deleted Successfully';
	        	}
	        	if(json.STATUS == "NOT OK"){
	        		window.location.href = '../accountant/bankreconcilation';
	        	}
	        },
	        cache: false
		});
}

$("#rec-delete").click(function() {    
	$('#deleterec').modal('show');
});
	

function journaltransaction(did){
	$.ajax({
		type : "GET",
		url: '/track/GeneralLedgerServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "getjournalDetailsByDetId",
			did:did,
		},
		success : function(data) {
			console.log(data);
			
			if (data.STATUS == "FAIL") {	
				alert("fail");
			}else{
				if(data.DETSTATUS == "1"){
					$(".rjournal-table tbody").html("");
					var body="";
					for(var i = 0; i < data.JOURNAL.length; i ++){
						body += '<tr>';
						body += '<td>'+data.JOURNAL[i]['ACCOUNT_NAME']+'</td>';
						body += '<td>'+data.JOURNAL[i]['DESCRIPTION']+'</td>';
						body += '<td>'+parseFloat(data.JOURNAL[i]['DEBITS']).toFixed(<%=numberOfDecimal%>)+'</td>';
						body += '<td>'+parseFloat(data.JOURNAL[i]['CREDITS']).toFixed(<%=numberOfDecimal%>)+'</td>';
						body += '</tr>';
					}
					$(".rjournal-table tbody").append(body);
					$('#journalInModal').modal('show');
				}

			}
		},
		error : function(data) {
			alert(data.responseText);
		}
	});
}	

</script>