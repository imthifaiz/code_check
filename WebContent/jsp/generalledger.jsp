<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.service.*"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="com.track.serviceImplementation.*"%>
<%@ page import="com.track.db.object.*"%>
<%@page import="com.track.util.StrUtils"%>
<%@page import="com.track.constants.IConstants"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Detailed General Ledger";
	LedgerService ledgerService=new LedgerServiceImpl();
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	CoaDAO coaDAO=new CoaDAO();
	ArrayList plntList = plantMstDAO.getPlantMstDetails(plant);
	List<String> chartOfAccount=coaDAO.getChartOfAccount(plant);
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
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String fiscalyear = plantMstDAO.getFiscalYear(plant);
	String fromDate="2020-07-01";
	String toDate="2020-07-31";
	List<LedgerDetails> ledgerDetails=new ArrayList<LedgerDetails>();
	ledgerDetails=ledgerService.getLedgerDetailsByDate(plant, fromDate, toDate);
	Double totalDebit=0.00;
	Double totalCredit=0.00;
	Double totalClosingBalance=0.00;
%>
<%@include file="sessionCheck.jsp"%>

<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
    <jsp:param name="submenu" value="<%=IConstants.ACCOUNTING_SUB_MENU%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<link href="../jsp/css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="../jsp/js/tabulator.min.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<script src="dist/js/html2canvas.js"></script>
<script src="../jsp/js/xlsx.full.min.js"></script>
<!--  <script src="https://cdnjs.cloudflare.com/ajax/libs/xlsx/0.17.0/xlsx.full.min.js"></script> -->
<div class="container-fluid m-t-20">

	<div class="box">
	<!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                <li><label>Detailed General Ledger</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 

		<div class="box-header menu-drop">
			
			<div class="row">
				<div class="col-sm-4"></div>
				<div class="col-sm-4" style="text-align:center">
					<div style="font-size:18px"><%=PLNTDESC %></div>
					<div style="font-size:28px">Detailed General Ledger</div>
					<div style="font-size:18px">Basis:Cash</div>
					<div style="font-size:15px">As of <span id="asof"></span></div>
				</div>
				<div class="col-sm-4">
				<br/>
				<div class="row">
						<div class=" pull-right">
							<div class="btn-group" role="group">
							    <button type="button" id="sendemail" class="btn btn-default"
									data-toggle="tooltip"  data-placement="bottom" title="Email">
										<i class="fa fa-envelope-o" aria-hidden="true"></i>
									</button>
								<button type="button" id="pdfdownload" class="btn btn-default"
								data-toggle="tooltip"  data-placement="bottom" title="PDF">
									<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
								</button>
								<button type="button" id="exceldownload" class="btn btn-default" data-toggle="tooltip" data-placement="bottom" title="Excel">
									
									<a href="javascript:exportDivToExcel()" id="tbsexcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>
									<!-- <a href="/track/TrialBalanceServlet?action=getTrialBalanceAsExcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>-->
								</button>
								<button type="button" class="btn btn-default printMe" 
								 data-toggle="tooltip"  data-placement="bottom" title="Print">
									<i class="fa fa-print" aria-hidden="true"></i>
								</button>
					&nbsp;<h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right"  onclick="window.location.href='../accounting/reports'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
							</div>
						</div>
					</div>
					</div>
			</div>
				</div>
				   <div id="print_id">
			<div class="row">
				<div class="col-sm-12">
				<div>Report period</div>
					<div class="row">
							<div class="col-sm-4"><select class="form-control" id="reportperiod" onchange="pldatechanged();">
							
							<option value="TODAY">Today</option>
							<option value="THISMONTH">This Month</option>
							<option value="THISQUARTER">This Quarter</option>
							<option value="THISYEAR">This Year</option>
							<option value="THISYEARTODATE">This Year-to-date</option>
							<option value="YESTERDAY">Yesterday</option>
								<option value="LASTMONTH">Last Month</option>
								<option value="LASTQUARTER">Last Quarter</option>
								<option value="LASTYEAR">Last Year</option>
								<option value="LASTYEARTODATE">Last Year-to-date</option>
								<option value="CUSTOM">Custom</option>
							
							</select></div>
							<div class="col-sm-2" style="vertical-align: baseline;"><INPUT class="form-control datepicker" id="fromdate" type="TEXT" size="30" MAXLENGTH="10" name="fromdate" autocomplete="off" placeholder="FROM DATE" readonly disabled/></div>
							
							<div class="col-sm-2" style="vertical-align: baseline;"><INPUT class="form-control datepicker" id="todate" type="TEXT" size="30" MAXLENGTH="10" name="todate" autocomplete="off" placeholder="TO DATE" readonly disabled/></div>
					<div class="col-sm-4" style="vertical-align: baseline;width:410px;">
							
							<%-- <select class="form-control" id="chartofAccount" onchange="pldatechanged();">
							<option value="">Select Account</option>
							<% for(int i=0;i<chartOfAccount.size();i++){ %>
							<option value="<%=chartOfAccount.get(i) %>" ><%=chartOfAccount.get(i) %> </option>
							<%} %>
							</select> --%>
							
							<input id="chartofAccount" name="chartofAccount"
									placeholder="Select Account" value="" class="form-control text-left"
									type="text" required> <span class="pay-select-icon"
									onclick="$(this).parent().find('input[name=\'chartofAccount\']').focus()">
								</span>
							
							</div>
					</div>
					<div class="row">
						<div class="col-sm-3" style="vertical-align: baseline;margin-top: 1%;"><button class="btn btn-success" id="srbtn" onclick="Searchcustom();" type="button">Run</button></div>
					</div>
			</div>
			</div>
				
			<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
			<input type="text" id="fiscalyear" style="display:none;" value=<%=fiscalyear%>>
		
			<div class="box-body">
			<div id="tableloader"></div>
			<div id="ledger-table_1"></div>
			 <!-- <table class="table table-striped">
    <thead>
      <tr>
        <th>DATE</th>
        <th>ACCOUNT</th>
        <th>TRANSACTION_DETAILS</th>
        <th>TRANSACTION_TYPE</th>
        <th>TRANSACTION_ID</th>
        <th>REFERENCE</th>
        <th>DEBIT</th>
        <th>CREDIT</th>
        <th>AMOUNT</th>
      </tr>
    </thead>
    <tbody id="ledgerReportContent">
     	
    </tbody>
  </table> -->
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
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>
<style>
@page { size: auto;  margin: 0mm; }
.searchAccFilter
{
	background-color: #d9534f;
    border-color: #d43f3a;
    padding: 9px;
    margin-top: 20px;
    margin-left: 12px;
    color: white;
}
.tabulator-row>span {
  display: none;
}
.tabulator-headers{
	font-size: 11px;
}
#tableloader {
    position: absolute;
    left: 50%;
    top: 350px;
    z-index: 1;
    width: 150px;
    height: 150px;
    margin: -25px 0 0 -25px;
    border: 5px solid #f3f3f3;
    border-top: 5px solid #00dc82;
    border-radius: 50%;
    width: 50px;
    height: 50px;
    -webkit-animation: spin 1s linear infinite;
    animation: spin 1s linear infinite;
}
</style>
<script type="text/javascript">
var tableJournalSummary;
var numberOfDecimal = $("#numberOfDecimal").val();
var fromDateFormatted;
var toDateFormatted;
var period;
function pldatechanged(){
	period=$("#reportperiod").val();
	sessionStorage.setItem('period', period);
	var to;
	var from;	
	$('#fromdate').prop('disabled', true);
	$('#todate').prop('disabled', true);
	$('#srbtn').hide();
	if(period=="TODAY")
		{
			from = moment().format('DD/MM/YYYY');
			to   = moment().format('DD/MM/YYYY');
			fromDateFormatted = moment().format('YYYY-MM-DD');
			toDateFormatted   = moment().format('YYYY-MM-DD');
		}
	else if(period=="THISYEARTODATE")
	 { 
		 var fiscalyear=$('#fiscalyear').val();
		 var formattedFiscal=moment(fiscalyear).format('YYYY-MM-DD');
		 from = moment(formattedFiscal).year(moment().year()).format('DD/MM/YYYY');
		 to   = moment().format('DD/MM/YYYY');
		 fromDateFormatted = moment(formattedFiscal).year(moment().year()).format('YYYY-MM-DD');
		 toDateFormatted   = moment().format('YYYY-MM-DD');
	 }
	else if(period=="THISMONTH")
	{
		from = moment().startOf('month').format('DD/MM/YYYY');
		to   = moment().endOf('month').format('DD/MM/YYYY');
		fromDateFormatted = moment().startOf('month').format('YYYY-MM-DD');
		toDateFormatted   = moment().endOf('month').format('YYYY-MM-DD');
	}
	else if(period=="THISQUARTER")
	{
		from = moment().quarter(moment().quarter()).startOf('quarter').format('DD/MM/YYYY');
		to   = moment().quarter(moment().quarter()).endOf('quarter').format('DD/MM/YYYY');
		fromDateFormatted = moment().quarter(moment().quarter()).startOf('quarter').format('YYYY-MM-DD');
		toDateFormatted   = moment().quarter(moment().quarter()).endOf('quarter').format('YYYY-MM-DD');
	}
	else if(period=="THISYEAR")
	{
		from = moment().year(moment().year()).startOf('year').format('DD/MM/YYYY');
		to   = moment().year(moment().year()).endOf('year').format('DD/MM/YYYY');
		fromDateFormatted = moment().year(moment().year()).startOf('year').format('YYYY-MM-DD');
		toDateFormatted   = moment().year(moment().year()).endOf('year').format('YYYY-MM-DD');
	}
	else if(period=="YESTERDAY")
	{
		from = moment().subtract(1, 'days').format('DD/MM/YYYY');
		to = moment().subtract(1, 'days').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(1, 'days').format('YYYY-MM-DD');
		toDateFormatted = moment().subtract(1, 'days').format('YYYY-MM-DD');
	}
	else if(period=="LASTMONTH")
	{
		from = moment().subtract(1, 'months').startOf('month').format('DD/MM/YYYY');
		to = moment().subtract(1, 'months').endOf('month').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD');
		toDateFormatted = moment().subtract(1, 'months').endOf('month').format('YYYY-MM-DD');
	}
	else if(period=="LASTQUARTER")
	{
		from = moment().subtract(3, 'months').startOf('month').format('DD/MM/YYYY');
		to = moment().subtract(1, 'months').endOf('month').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(3, 'months').startOf('month').format('YYYY-MM-DD');
		toDateFormatted = moment().subtract(1, 'months').endOf('month').format('YYYY-MM-DD');
	}
	else if(period=="LASTYEAR")
	{
		from = moment().subtract(1, 'year').startOf('year').format('DD/MM/YYYY');
		to = moment().subtract(1, 'year').endOf('year').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
		toDateFormatted = moment().subtract(1, 'year').endOf('year').format('YYYY-MM-DD');
	}
	else if(period == "LASTYEARTODATE")
	{
		from = moment().subtract(1, 'year').startOf('year').format('DD/MM/YYYY');
		to = moment().subtract(1, 'year').startOf('day').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
		toDateFormatted = moment().subtract(1, 'year').startOf('day').format('YYYY-MM-DD');
				
	}
	else if(period == "CUSTOM"){
		$('#fromdate').prop('disabled', false);
		$('#todate').prop('disabled', false);
		//$('#fromdate').val('');
		//$('#todate').val('');
		$('#srbtn').show();
		$("#tableloader").hide();
	}


	if(period != "CUSTOM"){
		$('#fromdate').val(from);
		$('#todate').val(to);
		var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
		$('#asof').html(asofdate);
		var chartofAccount=$("#chartofAccount").val();
		if(chartofAccount==null || chartofAccount==""){
			chartofAccount="";
		}
		loadLedgerDetailsDyn(fromDateFormatted,toDateFormatted,chartofAccount);
		$('#asofDate').val(asofdate);
	}
}

var tableDataNested1;
var tableDataNested2;

function postDatePickerInit(){
	if (period == 'CUSTOM'){
		var generalledger_fromdate = getLocalStorageValue('generalledger_fromdate', '', 'fromdate');
		getLocalStorageValue('generalledger_chartofAccount', '', 'chartofAccount');
		$('#fromdate').datepicker("setDate", 
		        moment(generalledger_fromdate, 'DD/MM/YYYY').toDate());
		var generalledger_todate = getLocalStorageValue('generalledger_todate', '', 'todate');
		$('#todate').datepicker("setDate", 
				moment(generalledger_todate, 'DD/MM/YYYY').toDate());
	}		
	$('#srbtn').click();
}

	$(document).ready(function() {
		$("#loader").hide();
		var to;
		var from;
		$('#fromdate').prop('disabled', true);
		$('#todate').prop('disabled', true);
		$('#srbtn').hide();
		var periodFromSession = getLocalStorageValue('generalledger_reportperiod', 'TODAY', 'reportperiod');
		period = periodFromSession;
		if (typeof(Storage) !== "undefined") {
			//var periodFromSession = sessionStorage.getItem('period');
			if(periodFromSession===null || !periodFromSession || periodFromSession=="null")
				{
					period="THISYEARTODATE";
					sessionStorage.setItem('period', period);
				}
			else
				{
					period=periodFromSession;
				}
			
		} else {
		  // Sorry! No Web Storage support..
		}
		//period="TODAY";
		 if(period=="TODAY")
			{
				from = moment().format('DD/MM/YYYY');
				to   = moment().format('DD/MM/YYYY');
				fromDateFormatted = moment().format('YYYY-MM-DD');
				toDateFormatted   = moment().format('YYYY-MM-DD');
			}
		 else if(period=="THISYEARTODATE")
			 { 
				 var fiscalyear=$('#fiscalyear').val();
				 var formattedFiscal=moment(fiscalyear).format('YYYY-MM-DD');
				 from = moment(formattedFiscal).year(moment().year()).format('DD/MM/YYYY');
				 to   = moment().format('DD/MM/YYYY');
				 fromDateFormatted = moment(formattedFiscal).year(moment().year()).format('YYYY-MM-DD');
				 toDateFormatted   = moment().format('YYYY-MM-DD');
				 console.log("Year to Date From:"+from);
				 console.log("Year to Date Formatted:"+fromDateFormatted);
			 }
		else if(period=="THISMONTH")
		{
			from = moment().startOf('month').format('DD/MM/YYYY');
			to   = moment().endOf('month').format('DD/MM/YYYY');
			fromDateFormatted = moment().startOf('month').format('YYYY-MM-DD');
			toDateFormatted   = moment().endOf('month').format('YYYY-MM-DD');
		}
		else if(period=="THISQUARTER")
		{
			from = moment().quarter(moment().quarter()).startOf('quarter').format('DD/MM/YYYY');
			to   = moment().quarter(moment().quarter()).endOf('quarter').format('DD/MM/YYYY');
			fromDateFormatted = moment().quarter(moment().quarter()).startOf('quarter').format('YYYY-MM-DD');
			toDateFormatted   = moment().quarter(moment().quarter()).endOf('quarter').format('YYYY-MM-DD');
		}
		else if(period=="THISYEAR")
		{
			from = moment().year(moment().year()).startOf('year').format('DD/MM/YYYY');
			to   = moment().year(moment().year()).endOf('year').format('DD/MM/YYYY');
			fromDateFormatted = moment().year(moment().year()).startOf('year').format('YYYY-MM-DD');
			toDateFormatted   = moment().year(moment().year()).endOf('year').format('YYYY-MM-DD');
		}
		else if(period=="YESTERDAY")
		{
			from = moment().subtract(1, 'days').format('DD/MM/YYYY');
			to = moment().subtract(1, 'days').format('DD/MM/YYYY');
			fromDateFormatted = moment().subtract(1, 'days').format('YYYY-MM-DD');
			toDateFormatted = moment().subtract(1, 'days').format('YYYY-MM-DD');
		}
		else if(period=="LASTMONTH")
		{
			from = moment().subtract(1, 'months').startOf('month').format('DD/MM/YYYY');
			to = moment().subtract(1, 'months').endOf('month').format('DD/MM/YYYY');
			fromDateFormatted = moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD');
			toDateFormatted = moment().subtract(1, 'months').endOf('month').format('YYYY-MM-DD');
		}
		else if(period=="LASTQUARTER")
		{
			from = moment().subtract(3, 'months').startOf('month').format('DD/MM/YYYY');
			to = moment().subtract(1, 'months').endOf('month').format('DD/MM/YYYY');
			fromDateFormatted = moment().subtract(3, 'months').startOf('month').format('YYYY-MM-DD');
			toDateFormatted = moment().subtract(1, 'months').endOf('month').format('YYYY-MM-DD');
		}
		else if(period=="LASTYEAR")
		{
			from = moment().subtract(1, 'year').startOf('year').format('DD/MM/YYYY');
			to = moment().subtract(1, 'year').endOf('year').format('DD/MM/YYYY');
			fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
			toDateFormatted = moment().subtract(1, 'year').endOf('year').format('YYYY-MM-DD');
		}
		else if(period == "LASTYEARTODATE")
		{
			from = moment().subtract(1, 'year').startOf('year').format('DD/MM/YYYY');
			to = moment().subtract(1, 'year').startOf('day').format('DD/MM/YYYY');
			fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
			toDateFormatted = moment().subtract(1, 'year').startOf('day').format('YYYY-MM-DD');
					
		}
		else if(period == "CUSTOM"){
			$('#fromdate').prop('disabled', false);
			$('#todate').prop('disabled', false);
			$('#fromdate').val('');
			$('#todate').val('');
			$('#srbtn').show();
			$("#tableloader").hide();
		}


		if(period != "CUSTOM"){
			$('#fromdate').val(from);
			$('#todate').val(to);
			$('#reportperiod').val(period);
			var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
			$('#asof').html(asofdate);
			var chartofAccount=$("#chartofAccount").val();
			if(chartofAccount==null || chartofAccount==""){
				chartofAccount="";
			}
			loadLedgerDetailsDyn(fromDateFormatted,toDateFormatted,chartofAccount);
			$('#asofDate').val(asofdate);
		}
					
	
		/* Paid Through Auto Suggestion */
		$("#chartofAccount").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true,
			  classNames: {
				 	menu: 'smalldrop'
				  }
			},
			{	  
			  display: 'accountname',  
			  source: function (query, process,asyncProcess) {
					var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : "<%=plant%>",
						action : "getSubAccountTypeGroup",
						module:"journalaccount",
						ITEM : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.results);
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
				suggestion: function(item) {
					/* if (item.issub) {
						var $state = $(
							    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
							  );
						}
					else
						{
						var $state = $(
								 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
							  );
						} */
						
						var $state = $(
								 '<span>'+ item.text +'</span>'
							  );
					  
					  return $state;
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".smalldrop");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				/*menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>');*/
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".smalldrop").css('display') != "block")
					menuElement.next().hide();	  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".smalldrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".smalldrop");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			}).on('typeahead:select',function(event,selection){
				//onGo(selection.id);
				//$("input[name ='paidthrough']").val(selection.id);
				pldatechanged();
			});
	
	
	});
	
	function loadLedgerDetailsDyn(from,to,Search)
	{
		storeInLocalStorage('generalledger_reportperiod', $('#reportperiod').val());
		storeInLocalStorage('generalledger_chartofAccount', $('#chartofAccount').val());
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('generalledger_fromdate', $('#fromdate').val());
		}
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('generalledger_todate', $('#todate').val());
		}
		$("#tableloader").show();
		$.ajax({
			type : "GET",
			url: '/track/GeneralLedgerServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getLedgerDetails",
				fromDate:from,
				toDate:to,
				chartofAccount:Search
			},
			success : function(ledgerArray) {
				var myJSON = JSON.stringify(ledgerArray);
				//alert(myJSON);
				tableDataNested1=ledgerArray;
				loadLedger2(from,to);
			}
		});
	}
	
	function getOpeningBalance(from,to,accountname)
	{
		var openingbalance=0.00;
		$.ajax({
			type : "GET",
			url: '/track/GeneralLedgerServlet',
			dataType: 'json',
			data : {
				action : "getAccountOpeningBalance",
				fromDate:from,
				toDate:to,
				account:accountname
			},
			success : function(balance) {
				//alert("SecondAjax");
				
				openingbalance=parseFloat(balance.openingbalance);
				console.log("Called:"+openingbalance);
				return openingbalance;	
			}
		});
		
		
	}
	var adultCalc = function(values, data, calcParams){
	    //values - array of column values
	    //data - all table data
	    //calcParams - params passed from the column definition object

	    var calc = 0;


	    return calc;
	}
	var table;
	function loadLedger2(from,to)
	{
		var openingbalance=0.00;
		var closingbalance=0.00;
		var debitOpen=0.00;
		var creditOpen=0.00;
		var debitClose=0.00;
		var creditClose=0.00;
			 table = new Tabulator("#ledger-table_1", {
		    data:tableDataNested1,
		    layout:"fitColumns",
		    pagination:"local",
		    paginationSize:10,
		        dataLoaded:function(data){
		        	//$("#tableloader").hide();
		        },
		    columnCalcs:"group",
		    ajaxLoader:true,
	    	ajaxLoaderLoading:"<div style='display:inline-block; border:4px solid #333; border-radius:10px; background:#fff; font-weight:bold; font-size:16px; color:#000; padding:10px 20px;'>Loading Data</div>",
 		     groupBy:["ACCOUNT"],
		    columns:[
		  
		    ],
		    rowFormatter:function(row){
		    	
		        //create and style holder elements
		       var holderEl = document.createElement("div");
		       var tableEl = document.createElement("div");
				openingbalance=row.getData().OPENING_BALANCE;
				closingbalance=row.getData().CLOSING_BALANCE;
				debitOpen=0.00;
				creditOpen=0.00;
				debitClose=0.00;
				creditClose=0.00;
				//console.log("Opening Balance:"+openingbalance);
				if(openingbalance<0)
					{
						creditOpen=Math.abs(openingbalance);
						creditOpen=creditOpen.toFixed(numberOfDecimal);
					}
				else
					{
						if(openingbalance)
							{
								debitOpen=openingbalance.toFixed(numberOfDecimal);
								console.log("Opening Balance:"+openingbalance);
								console.log("debitOpen:"+debitOpen);
							}
							
					}
				
					if(closingbalance<0)
					{
						creditClose=Math.abs(closingbalance);
						creditClose=creditClose.toFixed(numberOfDecimal);
					}
				else
					{
						if(closingbalance)
							{
								debitClose=closingbalance.toFixed(numberOfDecimal);
							}
							
					}
		       holderEl.appendChild(tableEl);

		       row.getElement().appendChild(tableEl);

		       var subTable = new Tabulator(tableEl, {
		    	   layout:"fitDataFill",
		    	   ajaxLoader:true,
		    	   ajaxLoaderLoading:"<div style='display:inline-block; border:4px solid #333; border-radius:10px; background:#fff; font-weight:bold; font-size:16px; color:#000; padding:10px 20px;'>Loading Data</div>",
		           data:row.getData().LEDGER_DETAILS,
		           columns:[
		        	   {title:"", field:"RECONCILIATION",formatter:function(cell, formatterParams, onRendered){
			    		 	var rstatus=cell.getValue();
			    		 	if(rstatus=="1")
			    		 		{
			    		 			return "<i class='fa fa-circle' aria-hidden='true' style='color:green;'></i>";
			    		 		}
			    		 	else
			    		 		{
			    		 			return "<i class='fa fa-circle' aria-hidden='true' style='color:red;'></i>";
			    		 		}
			    		}},
				    	 {title:"DATE", field:"DATE",topCalc:adultCalc,topCalcFormatter:function(cell, formatterParams, onRendered){
				    		 var cellElement = cell.getElement();
				    		 $(cellElement).width(150);
				 	    	return "<span style='font-size:13px'>As On "+from+"</span>";
				 	    },bottomCalc:adultCalc,bottomCalcFormatter:function(cell, formatterParams, onRendered){
				    		 var cellElement = cell.getElement();
				    		 cellElement.style.width = 150;
				 	    	return "<span style='font-size:13px'>As On "+to+"</span>";
				 	    }},
				    	 {title:"ACCOUNT", field:"ACCOUNT",topCalc:adultCalc,topCalcFormatter:function(cell, formatterParams, onRendered){
				    		 var cellElement = cell.getElement();
				    		 $(cellElement).width(150);
				 	    	return "<span style='font-size:13px'>Opening Balance</span>";
				 	    },bottomCalc:adultCalc,bottomCalcFormatter:function(cell, formatterParams, onRendered){
				    		 var cellElement = cell.getElement();
				    		 cellElement.style.width = 150;
				    		 return "<span style='font-size:13px'>Closing Balance</span>";
				 	    }},
				    	 {title:"TRANSACTION_DETAILS", field:"TRANSACTION_DETAILS",topCalc:adultCalc,topCalcFormatter:function(cell, formatterParams, onRendered){
				    		 debitOpen=openingbalance.toFixed(numberOfDecimal);
				    		 if(debitOpen>0)
				    			 {
				    			 	return "<span style='font-size:13px'><%=curency%> "+debitOpen.replace(/\d(?=(\d{3})+\.)/g, "$&,") +" DR</span>";
				    			 }
				    		 else if(creditOpen>0)
				    			 {
				    			 return "<span style='font-size:13px'><%=curency%> "+creditOpen.replace(/\d(?=(\d{3})+\.)/g, "$&,")+" CR</span>";
				    			 }
				    		 else
			    			 {
			    			 	var debitOpen=0.00;
			    			 	debitOpen=debitOpen.toFixed(numberOfDecimal);
			    			 	return "<span style='font-size:13px'><%=curency%> "+debitOpen.replace(/\d(?=(\d{3})+\.)/g, "$&,")+"</span>";
			    			 }
				 	    	
				 	    },bottomCalc:adultCalc,bottomCalcFormatter:function(cell, formatterParams, onRendered){
				    		 debitClose=closingbalance.toFixed(numberOfDecimal);
				    		 if(debitClose>0)
			    			 {
				    			 //debitClose=debitClose.toFixed(numberOfDecimal);
			    			 	return "<span style='font-size:13px'><%=curency%> "+debitClose.replace(/\d(?=(\d{3})+\.)/g, "$&,") +" DR</span>";
			    			 }else if(creditClose>0)
			    			 {
				    			 //creditClose=creditClose.toFixed(numberOfDecimal);
			    			 	return "<span style='font-size:13px'><%=curency%> "+creditClose.replace(/\d(?=(\d{3})+\.)/g, "$&,") +" CR</span>";
			    			 }else
			    			 {
				    			 	var debitClose=0.00;
				    			 	debitClose=debitClose.toFixed(numberOfDecimal);
				    			 	return "<span style='font-size:13px'><%=curency%> "+debitClose.replace(/\d(?=(\d{3})+\.)/g, "$&,")+"</span>";
				    			 } 
				 	    }},
				    	 {title:"TRANSACTION_TYPE", field:"TRANSACTION_TYPE"},
				    	 {title:"TRANSACTION_ID", field:"TRANSACTION_ID",formatter:function(cell, formatterParams, onRendered){
				    		 	var data = cell.getData();
				    		 	var trnid=cell.getValue();
				    		 	if(trnid!=null && trnid!="null" && trnid!="")
				    		 		{
				    		 			return trnid;
				    		 		}
				    		 	else
				    		 		{
				    		 			return data.JOURNALHDRID;
				    		 		}
				    		}},
				    	 {title:"REFERENCE", field:"REFERENCE",bottomCalc:adultCalc,bottomCalcFormatter:function(cell, formatterParams, onRendered){
				    		 var cellElement = cell.getElement();
				    		 return "<span style='font-size:13px'>Total</span>";
				 	    }},
				    	 {title:"DEBIT", field:"DEBIT",align:"right",formatter:function(cell, formatterParams, onRendered){
								var debit=cell.getValue();
								debit=debit.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				    		    return debit; //return the contents of the cell;
				    		},bottomCalc:"sum", bottomCalcParams:{precision:numberOfDecimal}},
				    	 {title:"CREDIT", field:"CREDIT",align:"right",formatter:function(cell, formatterParams, onRendered){
								var credit=cell.getValue();
								credit=credit.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				    		    return credit; //return the contents of the cell;
				    		},bottomCalc:"sum", bottomCalcParams:{precision:numberOfDecimal}},
				    		{title:"AMOUNT", field:"BALANCEAMT"},
				    	 /* {title:"AMOUNT", field:"AMOUNT",align:"right",formatter:function(cell, formatterParams, onRendered){
				    		 	var data = cell.getData();
				    		 	var debit=data.DEBIT;
				    		 	var credit=data.CREDIT;
				    		 	var amount=debit-credit;
				    		 	amount=amount.toFixed(numberOfDecimal);
				    		 	if(amount>0)
				    		 		{
				    		 			return amount.replace(/\d(?=(\d{3})+\.)/g, "$&,")+"Dr"; 
				    		 		}
				    		 	else
				    		 		{
				    		 			amount=Math.abs(amount);
				    		 			amount=amount.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
				    		 			return amount+"Cr"; 
				    		 		}
				    		}}, */
				    		{title:"TRANSACTION DETAILS", field:"JOURNALHDRID",formatter:function(cell, formatterParams, onRendered){
				    		 	var jid =cell.getValue();
				    		 	//return jid;
				    		 	return '<button class="btn btn-danger ember-view" onclick="journaltransaction(\''+jid+'\')">JOURNAL</button>'; 
				    		}},
				    		
				    ],
		       })
		    },
		});
			 $("#tableloader").hide();
	}
	function loadLedger(from,to)
	{
			 table = new Tabulator("#ledger-table_1", {
		    data:tableDataNested1,
		    layout:"fitColumns",
		    columnCalcs:"group",
		    ajaxLoader: true,
 		     groupBy:["ACCOUNT"],
		    columns:[
		    	 {title:"DATE", field:"LEDGER_DETAILS.DATE",topCalc:adultCalc,topCalcFormatter:function(cell, formatterParams, onRendered){
		    		 var cellElement = cell.getElement();
		    		 $(cellElement).width(150);
		 	    	return "<span style='font-size:13px'>As On "+from+"</span>";
		 	    },bottomCalc:adultCalc,bottomCalcFormatter:function(cell, formatterParams, onRendered){
		    		 var cellElement = cell.getElement();
		    		 cellElement.style.width = 150;
		 	    	return "<span style='font-size:13px'>As On "+to+"</span>";
		 	    }},
		    	 {title:"ACCOUNT", field:"LEDGER_DETAILS.ACCOUNT",topCalc:adultCalc,topCalcFormatter:function(cell, formatterParams, onRendered){
		    		 var cellElement = cell.getElement();
		    		 $(cellElement).width(150);
		 	    	return "<span style='font-size:13px'>Opening Balance</span>";
		 	    },bottomCalc:adultCalc,bottomCalcFormatter:function(cell, formatterParams, onRendered){
		    		 var cellElement = cell.getElement();
		    		 cellElement.style.width = 150;
		    		 return "<span style='font-size:13px'>Closing Balance</span>";
		 	    }},
		    	 {title:"TRANSACTION_DETAILS", field:"LEDGER_DETAILS.TRANSACTION_DETAILS"},
		    	 {title:"TRANSACTION_TYPE", field:"LEDGER_DETAILS.TRANSACTION_TYPE"},
		    	 {title:"TRANSACTION_ID", field:"LEDGER_DETAILS.TRANSACTION_ID"},
		    	 {title:"REFERENCE", field:"LEDGER_DETAILS.REFERENCE"},
		    	 {title:"DEBIT", field:"LEDGER_DETAILS.DEBIT",topCalc:adultCalc,topCalcFormatter:function(cell, formatterParams, onRendered){
		    		
		 	    	return "<span style='font-size:13px'></span>";
		 	    },bottomCalc:adultCalc,bottomCalcFormatter:function(cell, formatterParams, onRendered){
		    		 var cellElement = cell.getElement();
		    		 return "<span style='font-size:13px'>Closing Balance</span>";
		 	    }},
		    	 {title:"CREDIT", field:"LEDGER_DETAILS.CREDIT"},
		    	 {title:"AMOUNT", field:"LEDGER_DETAILS.AMOUNT"},
		    ],
		});
	}
	
	function generatePdf(dataUrl){
		
		var data = table.getData();
		//var rows = table_1.getRows();
		//var json = JSON.stringify(data);
		//alert(json);
		var doc = new jsPDF('l', 'mm', 'a4');
		var cwidth = doc.internal.pageSize.getWidth()/2;
		var pageNumber;
		/* Top Right */
		var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
		var toDate=moment(toDateFormatted).format('DD MMM YYYY');
		doc.setFontSize(17);
		doc.setFontStyle("normal");
		doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
		
		doc.setFontSize(12);
		//doc.setFontStyle("bold");
		doc.text('<%=PLNTDESC%>', cwidth, 50,'center');
		doc.setFontSize(14);
		doc.setFontStyle("bold");
		doc.text('<%=title%>',cwidth,60,'center');
		doc.setFontSize(14);
		doc.setFontStyle("normal");
		var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
		doc.text('As of '+asofdate,cwidth,70,'center');
		doc.setFontStyle("normal");
		/* **** */
		var totalPagesExp = "{total_pages_cont_string}";
		
		var htmlTable="<thead><tr><th>DATE</th><th>ACCOUNT</th><th>TRANSACTION_DETAILS</th><th>TRANSACTION_TYPE</th><th>TRANSACTION_ID</th><th>REFERENCE</th><th>DEBIT</th><th>CREDIT</th><th>AMOUNT</th></tr></thead><tbody>";
		var rows = [];
		var groups_1=table.getGroups();
		groups_1.forEach(group =>{
			var grouprows=group.getRows();
			var key=group.getKey();
			var htmlkey="<b>"+key+"</b>"
			var groupname=[key,''];
			rows.push(groupname);
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td></tr>";
			//console.log(grouprows[0].getData().account_name);
			grouprows.forEach(element => { 
				//console.log(element.getTable().innerHTML);
				//console.log(element.getTable().element.innerHTML);
		        //rows1.push(temp1);
		        var openingbalance=element.getData().OPENING_BALANCE;
				var closingbalance=element.getData().CLOSING_BALANCE;
				var debitOpen=0.00;
				var creditOpen=0.00;
				var debitClose=0.00;
				var creditClose=0.00;
				if(openingbalance<0)
					{
						creditOpen=Math.abs(openingbalance);
						creditOpen=creditOpen.toFixed(numberOfDecimal);
					}
				else
					{
						if(openingbalance)
							debitOpen=openingbalance.toFixed(numberOfDecimal);
					}
				
					if(closingbalance<0)
					{
						creditClose=Math.abs(closingbalance);
						creditClose=creditClose.toFixed(numberOfDecimal);
					}
				else
					{
						if(closingbalance)
							debitClose=closingbalance.toFixed(numberOfDecimal);
					}
		        htmlTable+="<tr><td>As On "+fromDateFormatted+"</td><td>Opening Balance</td><td></td><td></td><td></td><td></td>";
		       if(debitOpen>0)
		    	   {
		    	  	 htmlTable+="<td><%=curency%>"+debitOpen.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
		    	   }
		       else if(creditOpen>0)
	  			 {
			    	   htmlTable+="<td></td><td><%=curency%>"+creditOpen.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td>";
	  			 }
		  		 else
					 {
					 	var debitOpen=0.00;
					 	debitOpen=debitOpen.toFixed(numberOfDecimal);
					 	 htmlTable+="<td><%=curency%>"+debitOpen.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
					 }
		       
		     
		        htmlTable+="<td></td></tr>";
		        element.getData().LEDGER_DETAILS.forEach(childElem => {
		        	var debit=childElem.DEBIT;
	    		 	var credit=childElem.CREDIT;
	    		 	var debitText=debit.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	    		 	var creditText=credit.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	    		 	var amount=debit-credit;
	    		 	amount=amount.toFixed(numberOfDecimal);
	    		 	var amountText;
	    		 	/* if(childElem.TRANSACTION_ID==null || childElem.TRANSACTION_ID=='null'){
	    		 		childElem.TRANSACTION_ID=" ";
        		 	} */
	    		 	var transID = "";
	    		 	if(childElem.TRANSACTION_ID==null || childElem.TRANSACTION_ID=='null' || childElem.TRANSACTION_ID==''){
	    		 		//childElem.TRANSACTION_ID=" ";
	    		 		transID = childElem.JOURNALHDRID;
        		 	}else {
        		 		transID = childElem.TRANSACTION_ID;
        		 	}
	    		 	if(amount>0)
	    		 		{
	    		 			amountText= amount.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"Dr"; 
	    		 		}
	    		 	else
	    		 		{
	    		 			amount=Math.abs(amount);
	    		 			amount=amount.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	    		 			amountText= amount+"Cr"; 
	    		 		}
	    		 	
		        	 htmlTable+="<tr><td>"+childElem.DATE+"</td><td>"+childElem.ACCOUNT+"</td><td>"+childElem.TRANSACTION_DETAILS+"</td><td>"+childElem.TRANSACTION_TYPE+"</td><td>"+transID+"</td><td>"+childElem.REFERENCE+"</td><td>"+debitText+"</td><td>"+creditText+"</td><td>"+amountText+"</td></tr>";
		        });
		        
		        htmlTable+="<tr><td>As On "+toDateFormatted+"</td><td>Closing Balance</td><td></td><td></td><td></td><td></td>";
		        if(debitClose>0)
	   			 {
		        	 htmlTable+="<td><%=curency%>"+debitClose.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
	   			 }
		        else if(creditClose>0)
	   			 {
		    			 //creditClose=creditClose.toFixed(numberOfDecimal);
			         htmlTable+="<td></td><td><%=curency%>"+creditClose.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td>";
	   			 }
		        else
		        	{
		        	var debitClose=0.00;
		        	debitClose=debitClose.toFixed(numberOfDecimal);
				 	 htmlTable+="<td><%=curency%>"+debitClose.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
		        	}
		        htmlTable+="<td></td></tr>";
		    }); 
		});
		htmlTable+="</tbody>";
		var tableElement=document.createElement('table');
		tableElement.innerHTML=htmlTable;
		 doc.autoTable({
			html : tableElement,
			startY : 75,
			theme : 'striped',
			styles: {
				cellWidth: 'wrap',
	            fontSize: 12,
	            cellPadding: 0.5
	        },
			headStyles : {
				margin:{top:30},
	            fontSize: 7,
	            fillColor : [ 217, 217, 217],
				textColor : [ 0, 0, 0 ]
			},
			bodyStyles: {
	            fontSize: 10,
	            lineWidth: 0.025,
	            lineColor: [217, 217, 217]
	        },
	        columnStyles: {
	            text: {
	              cellWidth: 'auto'
	            },
	        0: {
	        	cellWidth: 30                
            },
            1: {
            	cellWidth: 45
            },
            2: {
            	cellWidth: 12
            },
            3: {
            	cellWidth: 35
            },
            4: {
            	cellWidth: 30
            },
            5: {
            	cellWidth: 15
            },
            6: {
            	cellWidth: 26,
            	halign:'right'
            },
            7: {
            	cellWidth: 26,
            	halign:'right'
            },
            8: {
            	cellWidth: 26,
            	halign:'right'
            },
	        },
			willDrawCell: function (data) {
	            // Colspan
	            console.log(data.cell.raw.className);
	            if (data.cell.raw.className=="innerHeader") {
	            	//console.log("Entered");
	            	doc.setFontStyle('bold');
	                doc.setFontSize(9);
	                doc.setTextColor(0, 0, 0);
	                doc.setFillColor(242, 242, 242);
			}
			},
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
				doc.text(str, 218,
						pageHeight - 10);
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
	
		doc.save('General Ledger Details.pdf');
	}
function generatePdfPrint(dataUrl){
		
		var data = table.getData();
		//var rows = table_1.getRows();
		//var json = JSON.stringify(data);
		//alert(json);
		var doc = new jsPDF('l', 'mm', 'a4');
		var cwidth = doc.internal.pageSize.getWidth()/2;
		var pageNumber;
		/* Top Right */
		var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
		var toDate=moment(toDateFormatted).format('DD MMM YYYY');
		doc.setFontSize(17);
		doc.setFontStyle("normal");
		doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
		
		doc.setFontSize(12);
		//doc.setFontStyle("bold");
		doc.text('<%=PLNTDESC%>', cwidth, 50,'center');
		doc.setFontSize(14);
		doc.setFontStyle("bold");
		doc.text('<%=title%>',cwidth,60,'center');
		doc.setFontSize(14);
		doc.setFontStyle("normal");
		var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
		doc.text('As of '+asofdate,cwidth,70,'center');
		doc.setFontStyle("normal");
		/* **** */
		var totalPagesExp = "{total_pages_cont_string}";
		
		var htmlTable="<thead><tr><th>DATE</th><th>ACCOUNT</th><th>TRANSACTION_DETAILS</th><th>TRANSACTION_TYPE</th><th>TRANSACTION_ID</th><th>REFERENCE</th><th>DEBIT</th><th>CREDIT</th><th>AMOUNT</th></tr></thead><tbody>";
		var rows = [];
		var groups_1=table.getGroups();
		groups_1.forEach(group =>{
			var grouprows=group.getRows();
			var key=group.getKey();
			var htmlkey="<b>"+key+"</b>"
			var groupname=[key,''];
			rows.push(groupname);
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td></tr>";
			//console.log(grouprows[0].getData().account_name);
			grouprows.forEach(element => { 
				//console.log(element.getTable().innerHTML);
				//console.log(element.getTable().element.innerHTML);
		        //rows1.push(temp1);
		        var openingbalance=element.getData().OPENING_BALANCE;
				var closingbalance=element.getData().CLOSING_BALANCE;
				var debitOpen=0.00;
				var creditOpen=0.00;
				var debitClose=0.00;
				var creditClose=0.00;
				if(openingbalance<0)
					{
						creditOpen=Math.abs(openingbalance);
						creditOpen=creditOpen.toFixed(numberOfDecimal);
					}
				else
					{
						if(openingbalance)
							debitOpen=openingbalance.toFixed(numberOfDecimal);
					}
				
					if(closingbalance<0)
					{
						creditClose=Math.abs(closingbalance);
						creditClose=creditClose.toFixed(numberOfDecimal);
					}
				else
					{
						if(closingbalance)
							debitClose=closingbalance.toFixed(numberOfDecimal);
					}
		        htmlTable+="<tr><td>As On "+fromDateFormatted+"</td><td>Opening Balance</td><td></td><td></td><td></td><td></td>";
		       if(debitOpen>0)
		    	   {
		    	  	 htmlTable+="<td><%=curency%>"+debitOpen.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
		    	   }
		       else if(creditOpen>0)
	  			 {
			    	   htmlTable+="<td></td><td><%=curency%>"+creditOpen.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td>";
	  			 }
		  		 else
					 {
					 	var debitOpen=0.00;
					 	debitOpen=debitOpen.toFixed(numberOfDecimal);
					 	 htmlTable+="<td><%=curency%>"+debitOpen.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
					 }
		       
		     
		        htmlTable+="<td></td></tr>";
		        element.getData().LEDGER_DETAILS.forEach(childElem => {
		        	var debit=childElem.DEBIT;
	    		 	var credit=childElem.CREDIT;
	    		 	var debitText=debit.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	    		 	var creditText=credit.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	    		 	var amount=debit-credit;
	    		 	amount=amount.toFixed(numberOfDecimal);
	    		 	var amountText;
	    		 	var transID = "";
	    		 	if(childElem.TRANSACTION_ID==null || childElem.TRANSACTION_ID=='null' || childElem.TRANSACTION_ID==''){
	    		 		//childElem.TRANSACTION_ID=" ";
	    		 		transID = childElem.JOURNALHDRID;
        		 	}else {
        		 		transID = childElem.TRANSACTION_ID;
        		 	}
	    		 	if(amount>0)
	    		 		{
	    		 			amountText= amount.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"Dr"; 
	    		 		}
	    		 	else
	    		 		{
	    		 			amount=Math.abs(amount);
	    		 			amount=amount.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	    		 			amountText= amount+"Cr"; 
	    		 		}
	    		 	
		        	 htmlTable+="<tr><td>"+childElem.DATE+"</td><td>"+childElem.ACCOUNT+"</td><td>"+childElem.TRANSACTION_DETAILS+"</td><td>"+childElem.TRANSACTION_TYPE+"</td><td>"+transID+"</td><td>"+childElem.REFERENCE+"</td><td>"+debitText+"</td><td>"+creditText+"</td><td>"+amountText+"</td></tr>";
		        });
		        
		        htmlTable+="<tr><td>As On "+toDateFormatted+"</td><td>Closing Balance</td><td></td><td></td><td></td><td></td>";
		        if(debitClose>0)
	   			 {
		        	 htmlTable+="<td><%=curency%>"+debitClose.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
	   			 }
		        else if(creditClose>0)
	   			 {
		    			 //creditClose=creditClose.toFixed(numberOfDecimal);
			         htmlTable+="<td></td><td><%=curency%>"+creditClose.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td>";
	   			 }
		        else
		        	{
		        	var debitClose=0.00;
		        	debitClose=debitClose.toFixed(numberOfDecimal);
				 	 htmlTable+="<td><%=curency%>"+debitClose.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
		        	}
		        htmlTable+="<td></td></tr>";
		    }); 
		});
		htmlTable+="</tbody>";
		var tableElement=document.createElement('table');
		tableElement.innerHTML=htmlTable;
		 doc.autoTable({
			html : tableElement,
			startY : 75,
			theme : 'striped',
			styles: {
				cellWidth: 'wrap',
	            fontSize: 12,
	            cellPadding: 0.5
	        },
			headStyles : {
				margin:{top:30},
	            fontSize: 7,
	            fillColor : [ 217, 217, 217],
				textColor : [ 0, 0, 0 ]
			},
			bodyStyles: {
	            fontSize: 10,
	            lineWidth: 0.025,
	            lineColor: [217, 217, 217]
	        },
	        columnStyles: {
	            text: {
	              cellWidth: 'auto'
	            },
	        0: {
	        	cellWidth: 30                
            },
            1: {
            	cellWidth: 40
            },
            2: {
            	cellWidth: 12
            },
            3: {
            	cellWidth: 28
            },
            4: {
            	cellWidth: 28
            },
            5: {
            	cellWidth: 15
            },
            6: {
            	cellWidth: 25,
            	halign:'right'
            },
            7: {
            	cellWidth: 25,
            	halign:'right'
            },
            8: {
            	cellWidth: 25,
            	halign:'right'
            },
	        }, 
			willDrawCell: function (data) {
	            // Colspan
	            console.log(data.cell.raw.className);
	            if (data.cell.raw.className=="innerHeader") {
	            	//console.log("Entered");
	            	doc.setFontStyle('bold');
	                doc.setFontSize(9);
	                doc.setTextColor(0, 0, 0);
	                doc.setFillColor(242, 242, 242);
			}
			},
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
				doc.text(str, 218,
						pageHeight - 10);
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
$("#sendemail").click(function(){
	$('#common_email_modal').modal('toggle');
	//var asofdate="From "+fromdate+ " To "+todate;
//	$('#asofDate').val(asofdate);
	loadBodyStyle();
});
function generatePdfMail(dataUrl,attachName){
		
	var data = table.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('l', 'mm', 'a4');
	var cwidth = doc.internal.pageSize.getWidth()/2;
	var pageNumber;
	/* Top Right */
	var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	//doc.setFontStyle("bold");
	doc.text('<%=PLNTDESC%>', cwidth, 50,'center');
	doc.setFontSize(14);
	doc.setFontStyle("bold");
	doc.text('<%=title%>',cwidth,60,'center');
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,cwidth,70,'center');
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<thead><tr><th>DATE</th><th>ACCOUNT</th><th>TRANSACTION_DETAILS</th><th>TRANSACTION_TYPE</th><th>TRANSACTION_ID</th><th>REFERENCE</th><th>DEBIT</th><th>CREDIT</th><th>AMOUNT</th></tr></thead><tbody>";
	var rows = [];
	var groups_1=table.getGroups();
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => { 
			//console.log(element.getTable().innerHTML);
			//console.log(element.getTable().element.innerHTML);
	        //rows1.push(temp1);
	        var openingbalance=element.getData().OPENING_BALANCE;
			var closingbalance=element.getData().CLOSING_BALANCE;
			var debitOpen=0.00;
			var creditOpen=0.00;
			var debitClose=0.00;
			var creditClose=0.00;
			if(openingbalance<0)
				{
					creditOpen=Math.abs(openingbalance);
					creditOpen=creditOpen.toFixed(numberOfDecimal);
				}
			else
				{
					if(openingbalance)
						debitOpen=openingbalance.toFixed(numberOfDecimal);
				}
			
				if(closingbalance<0)
				{
					creditClose=Math.abs(closingbalance);
					creditClose=creditClose.toFixed(numberOfDecimal);
				}
			else
				{
					if(closingbalance)
						debitClose=closingbalance.toFixed(numberOfDecimal);
				}
	        htmlTable+="<tr><td>As On "+fromDateFormatted+"</td><td>Opening Balance</td><td></td><td></td><td></td><td></td>";
	       if(debitOpen>0)
	    	   {
	    	  	 htmlTable+="<td><%=curency%>"+debitOpen.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
	    	   }
	       else if(creditOpen>0)
  			 {
		    	   htmlTable+="<td></td><td><%=curency%>"+creditOpen.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td>";
  			 }
	  		 else
				 {
				 	var debitOpen=0.00;
				 	debitOpen=debitOpen.toFixed(numberOfDecimal);
				 	 htmlTable+="<td><%=curency%>"+debitOpen.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
				 }
	       
	     
	        htmlTable+="<td></td></tr>";
	        element.getData().LEDGER_DETAILS.forEach(childElem => {
	        	var debit=childElem.DEBIT;
    		 	var credit=childElem.CREDIT;
    		 	var debitText=debit.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    		 	var creditText=credit.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    		 	var amount=debit-credit;
    		 	amount=amount.toFixed(numberOfDecimal);
    		 	var amountText;
    		 	/* if(childElem.TRANSACTION_ID==null || childElem.TRANSACTION_ID=='null'){
    		 		childElem.TRANSACTION_ID=" ";
    		 	} */
    		 	var transID = "";
    		 	if(childElem.TRANSACTION_ID==null || childElem.TRANSACTION_ID=='null' || childElem.TRANSACTION_ID==''){
    		 		//childElem.TRANSACTION_ID=" ";
    		 		transID = childElem.JOURNALHDRID;
    		 	}else {
    		 		transID = childElem.TRANSACTION_ID;
    		 	}
    		 	if(amount>0)
    		 		{
    		 			amountText= amount.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"Dr"; 
    		 		}
    		 	else
    		 		{
    		 			amount=Math.abs(amount);
    		 			amount=amount.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
    		 			amountText= amount+"Cr"; 
    		 		}
    		 	
	        	 htmlTable+="<tr><td>"+childElem.DATE+"</td><td>"+childElem.ACCOUNT+"</td><td>"+childElem.TRANSACTION_DETAILS+"</td><td>"+childElem.TRANSACTION_TYPE+"</td><td>"+transID+"</td><td>"+childElem.REFERENCE+"</td><td>"+debitText+"</td><td>"+creditText+"</td><td>"+amountText+"</td></tr>";
	        });
	        
	        htmlTable+="<tr><td>As On "+toDateFormatted+"</td><td>Closing Balance</td><td></td><td></td><td></td><td></td>";
	        if(debitClose>0)
   			 {
	        	 htmlTable+="<td><%=curency%>"+debitClose.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
   			 }
	        else if(creditClose>0)
   			 {
	    			 //creditClose=creditClose.toFixed(numberOfDecimal);
		         htmlTable+="<td></td><td><%=curency%>"+creditClose.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td>";
   			 }
	        else
	        	{
	        	var debitClose=0.00;
	        	debitClose=debitClose.toFixed(numberOfDecimal);
			 	 htmlTable+="<td><%=curency%>"+debitClose.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td><td></td>";
	        	}
	        htmlTable+="<td></td></tr>";
	    }); 
	});
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	 doc.autoTable({
		html : tableElement,
		startY : 75,
		theme : 'striped',
		styles: {
			cellWidth: 'wrap',
            fontSize: 12,
            cellPadding: 0.5
        },
		headStyles : {
			margin:{top:30},
            fontSize: 7,
            fillColor : [ 217, 217, 217],
			textColor : [ 0, 0, 0 ]
		},
		bodyStyles: {
            fontSize: 10,
            lineWidth: 0.025,
            lineColor: [217, 217, 217]
        },
        columnStyles: {
            text: {
              cellWidth: 'auto'
            },
        0: {
        	cellWidth: 30                
        },
        1: {
        	cellWidth: 45
        },
        2: {
        	cellWidth: 12
        },
        3: {
        	cellWidth: 35
        },
        4: {
        	cellWidth: 30
        },
        5: {
        	cellWidth: 15
        },
        6: {
        	cellWidth: 26,
        	halign:'right'
        },
        7: {
        	cellWidth: 26,
        	halign:'right'
        },
        8: {
        	cellWidth: 26,
        	halign:'right'
        },
        },
		willDrawCell: function (data) {
            // Colspan
            console.log(data.cell.raw.className);
            if (data.cell.raw.className=="innerHeader") {
            	//console.log("Entered");
            	doc.setFontStyle('bold');
                doc.setFontSize(9);
                doc.setTextColor(0, 0, 0);
                doc.setFillColor(242, 242, 242);
		}
		},
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
			doc.text(str, 218,
					pageHeight - 10);
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
	
		const pdf = new File([doc.output("blob")], attachName+".pdf", {  type: "pdf" }),
		formData = new FormData();

		formData.append("file", pdf);
		progressBar();
		sendMailTemplate(formData);
	}
	function printdoc()
	{
		var img = toDataURL($(".dash-logo").attr("src"),
				function(dataUrl) {
					generatePdfPrint(dataUrl);
			  	},'image/jpeg');
		
		
		}
	function generate() {
		var img = toDataURL($(".dash-logo").attr("src"),
				function(dataUrl) {
					generatePdf(dataUrl);
			  	},'image/jpeg');
			
		}
	$("#pdfdownload").click(function(){
		generate();
	});
	function generateEmail(attachName)
	{
		var img = toDataURL($(".dash-logo").attr("src"),
				function(dataUrl) {
					generatePdfMail(dataUrl,attachName);
			  	},'image/jpeg');
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
	
	function loadLedgerDetails(from,to)
	{
		$('#ledgerReportContent').html("");
		$.ajax({
			type : "GET",
			url: '/track/GeneralLedgerServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getLedgerDetails",
				fromDate:from,
				toDate:to
			},
			success : function(ledgerDetailArray) {
				var ledgerContent="";
				for (i = 0; i < ledgerDetailArray.length; i++) {
					//ledgerContent="";
					var accountname=ledgerDetailArray[i].ACCOUNT;
					var details=ledgerDetailArray[i].LEDGER_DETAILS;
					
					var topRow="<tr>"+accountname+"</tr>";
					var headerRow="<tr>";
					var contentRow;
					var bottomRow="<tr>";
					openingbalance=ledgerDetailArray[i].OPENING_BALANCE;
					//openingbalance=parseFloat(balance.openingbalance);
					
					if(openingbalance<0)
						{
							headerRow+="<td>As On"+from+"</td>"+
			  				"<td>Opening Balance</td>"+
			  				"<td></td>"+
			  				"<td></td>"+
			  				"<td></td>"+
			  				"<td></td>"+
			  				"<td></td>"+
			  				"<td>"+Math.abs(openingbalance)+"</td>"+
			  				"<td></td>"+
			  				"</tr>";
						}
					else
						{
							headerRow+="<td>As On"+from+"</td>"+
			  				"<td>Opening Balance</td>"+
			  				"<td></td>"+
			  				"<td></td>"+
			  				"<td></td>"+
			  				"<td></td>"+
			  				"<td>"+Math.abs(openingbalance)+"</td>"+
			  				"<td></td>"+
			  				"<td></td>"+
			  				"</tr>";
						}
					var totalDebit=0.00;
					var totalCredit=0.00;
					$.each( details, function( key, value ) {
						  //console.log( key + ": " + value );
						  totalDebit+=parseFloat(value.DEBIT);
						  totalCredit+=parseFloat(value.CREDIT);
						  var content="<tr>";
						  content+="<td>"+value.DATE+"</td>"+
				       		"<td>"+value.ACCOUNT+"</td>"+
				       		"<td>"+value.TRANSACTION_DETAILS+"</td>"+
				       		"<td>"+value.TRANSACTION_TYPE+"</td>"+
				       		"<td>"+value.TRANSACTION_ID+"</td>"+
				       		"<td>"+value.REFERENCE+"</td>"+
				       		"<td>"+value.DEBIT+"</td>"+
				       		"<td>"+value.CREDIT+"</td>";
				       		var amount=parseFloat(value.DEBIT)-parseFloat(value.CREDIT);
				       		if(amount>0)
				       			{
				       			content+="<td>"+amount+"Dr</td>";
				       			}
				       		else
				       			{
				       			content+="<td>"+Math.abs(amount)+"Cr</td>";
				       			}
				       		content+="</tr>";
				       		contentRow+=content;
						});
					var closingbalance=ledgerDetailArray[i].CLOSING_BALANCE;
					if(closingbalance<0)
						{
							bottomRow+="<td>As On"+to+"</td>"+
							"<td>Closing Balance</td>"+
							"<td></td>"+
							"<td></td>"+
							"<td></td>"+
							"<td></td>"+
							"<td>"+closingbalance+"</td>"+
							"<td></td>"+
							"<td></td>";
						}
					else
						{
							bottomRow+="<td>As On"+to+"</td>"+
							"<td>Closing Balance</td>"+
							"<td></td>"+
							"<td></td>"+
							"<td></td>"+
							"<td></td>"+
							"<td></td>"+
							"<td>"+Math.abs(closingbalance)+"</td>"+
							"<td></td>";
						}
					
						bottomRow+="</tr>";
					
					
					//console.log("account: " + account );
					//console.log("-----------------------------");
					ledgerContent+=topRow+headerRow+contentRow+bottomRow;
					//console.log(ledgerContent);
					$('#ledgerReportContent').append(ledgerContent);
				}
				//$('#ledgerReportContent').html(ledgerContent);
				//$('#ledgerReportContent').html(ledgerContent);
				 
			}
		});
	}
$('.printMe').click(function(){
	printdoc();
});

function Searchcustom(){
	var fromdate = $('#fromdate').val();
	var todate = $('#todate').val();
	if(fromdate == ""){
		alert("Please select valid from or to date");
		return false;
	}
	if(todate == ""){
		alert("Please select valid from or to date");
		return false;
	}
	fromDateFormatted = moment(fromdate, "DD/MM/YYYY").format('YYYY-MM-DD');
	toDateFormatted = moment(todate, "DD/MM/YYYY").format('YYYY-MM-DD');
	if(moment(fromdate, "DD/MM/YYYY").isAfter(moment(todate, "DD/MM/YYYY"), 'days')){
		$('#fromdate').val('');
		$('#todate').val('');
		alert("Please select valid from and to date");
	}else{
		var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
		$('#asof').html(asofdate);
		var chartofAccount=$("#chartofAccount").val();
		if(chartofAccount==null || chartofAccount==""){
			chartofAccount="";
		}
		loadLedgerDetailsDyn(fromDateFormatted,toDateFormatted,chartofAccount);
		$('#asofDate').val(asofdate);
	}
}

function journaltransaction(hid){
	$.ajax({
		type : "GET",
		url: '/track/GeneralLedgerServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "getjournalDetails",
			hid:hid,
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

function exportDivToExcel() {
    // Get all rows within the div
    var rows = document.querySelectorAll("#ledger-table_1 .tabulator-row");
    var rows1 = document.querySelectorAll("#ledger-table_1 .tabulator-headers");

    // Create a 2D array to store table data
    var data = [];
        var rowDatah = [];
        rowDatah.push('<%=PLNTDESC%>');
        data.push(rowDatah);
        
        var rowDatat = [];
        rowDatat.push('<%=title%>');
        data.push(rowDatat);

    // Extract header row data
    rows1.forEach(function(row, index) {
        if (index === 0) return;
        var cells = row.querySelectorAll(".tabulator-col");
        var rowData = [];

        // Extract text from each cell and push to rowData
        cells.forEach(function(cell, idx, array) {
            if (idx === 0) return;
            if (idx + 1 === array.length) {
                // Skip if needed
            } else {
                rowData.push(cell.innerText.trim());
            }
        });

        // Add rowData to data array
        data.push(rowData);
    });

    // Iterate through each row and extract cell data
    rows.forEach(function(row, index) {
        if (index === 0) return;
        if (index === 1) return;
        var cells = row.querySelectorAll(".tabulator-cell");
        var rowData = [];

        // Extract text from each cell and push to rowData
        cells.forEach(function(cell, idx, array) {
            if (idx === 0) return;
            if (idx + 1 === array.length) {
                // Skip if needed
            } else {
                rowData.push(cell.innerText.trim());
            }
        });

        // Add rowData to data array
        data.push(rowData);
    });

    // Create a new worksheet with the extracted data
    var ws = XLSX.utils.aoa_to_sheet(data);

    // Apply bold style to the first row (headers)
    var range = XLSX.utils.decode_range(ws['!ref']);
    for (var C = range.s.c; C <= range.e.c; ++C) {
        var cellAddress = XLSX.utils.encode_cell({r: 0, c: C});
        if (!ws[cellAddress]) ws[cellAddress] = {v: ""}; // Ensure the cell exists

        // Initialize the style object if it doesn't exist
        ws[cellAddress].s = ws[cellAddress].s || {};
        ws[cellAddress].s.font = {bold: true}; // Apply bold font style
    }

    // Create a new workbook
    var wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, "Ledger Data");

    // Export the workbook to an Excel file
    XLSX.writeFile(wb, "General_Ledger_Details.xlsx");
}


</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>