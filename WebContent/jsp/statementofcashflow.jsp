<%@ page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.StrUtils"%>
<%@ page import="java.io.*"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Statement of Cash Flows";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String fiscalyear = plantMstDAO.getFiscalYear(plant);
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
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>" />
	<jsp:param name="submenu" value="<%=IConstants.ACCOUNTING_SUB_MENU%>" />
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/custom.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/dist/js/moment.min.js"></script>
<link href="../jsp/css/tabulator_bootstrap.min.css" rel="stylesheet">
<script src="../jsp/js/tabulator.min.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<div class="container-fluid m-t-20">

	<div class="box">
	<!-- Thanzith Modified on 26.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                <li><label>Statement of Cash Flows</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 26.02.2022 --> 

		<div class="box-header menu-drop">


			<div class="row">
				<div class="col-sm-9">
					<div>Report period</div>
					<div class="row">
						<div class="col-sm-3">
							<select class="form-control" id="reportperiod"
								onchange="journaldatechanged(this);">
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
							</select>
						</div>


						<div class="col-sm-2" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" id="fromdate" type="TEXT" autocomplete="off" placeholder="FROM DATE" readonly
								size="30" MAXLENGTH="10" name="fromdate" disabled />
								
						</div>
						
						
						
						<div class="col-sm-2" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" id="todate" type="TEXT" autocomplete="off" placeholder="TO DATE" readonly
								size="30" MAXLENGTH="10" name="todate" disabled />
						</div>
						<div class="col-sm-2" style="vertical-align: baseline;">
							<input type="checkbox" class="lastyear" id="Lastyear" name="Lastyear">
							<label for="Lastyear">Previous Year</label>
						</div>

						<div class="col-sm-3" style="vertical-align: baseline;"><button class="btn btn-success" id="srbtn" onclick="Searchcustom();" type="button">Run</button></div>
						<!-- <div style="margin-left: 16px;">Display columns by</div>
						<div class="col-sm-4">
						<select class="form-control" id="displaycolumnsby"
								onchange="">
								<option value="Total Only">Total Only</option>
								<option value="Days">Days</option>
								<option value="Weeks">Weeks</option>
								<option value="Months">Months</option>
								<option value="Quarters">Quarters</option>
								<option value="Years">Years</option>
								<option value="Customers">Customers</option>
								<option value="Suppliers">Suppliers</option>
								<option value="Products/Services">Products/Services</option>
								
						</select>
						</div>
						
						
						<div style="/* margin-left: -154px; */margin-top: -17px;">Show non-zero or active only</div>
						<div class="col-sm-6" style="margin-left: -16px;">
						<select class="form-control" id="displaycolumnsby"
								onchange="">
								<option value="Active rows/active columns">Active rows/active columns</option>
								<option value="Inactive rows/inactive columns">Inactive rows/inactive columns</option>
								
								
						</select>
						</div> -->
						
						
					</div>
				</div>

				<div class="col-sm-3">
					<div class="row">
						<div class=" pull-right">
							<div class="btn-group" role="group">
								<button type="button" id="sendemail" class="btn btn-default"
									data-toggle="tooltip" data-placement="bottom" title="Email">
									<i class="fa fa-envelope-o" aria-hidden="true"></i>
								</button>
								<button type="button" id="pdfdownload" class="btn btn-default"
									data-toggle="tooltip" data-placement="bottom" title="PDF">
									<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
								</button>
								<button type="button" id="exceldownload" class="btn btn-default" data-toggle="tooltip" data-placement="bottom" title="Excel">
									<a href="javascript:downTBExcel()" id="tbsexcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>
									<!-- <a href="/track/TrialBalanceServlet?action=getTrialBalanceAsExcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>-->
								</button>

								<button type="button" class="btn btn-default printMe"
									data-toggle="tooltip" data-placement="bottom" title="Print">
									<i class="fa fa-print" aria-hidden="true"></i>
								</button>
								&nbsp;
								<h1 style="font-size: 18px; cursor: pointer;"
									class="box-title pull-right"
								onclick="window.location.href='../accounting/reports'">
									<i class="glyphicon glyphicon-remove"></i>
								</h1>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>




		<div class="row">
			<div class="col-sm-12" id="chgColsm">
				<div class="row">

					<div class="col-sm-4"></div>
					<div class="col-sm-4" style="text-align: center">
						<div style="font-size: 18px">SUNPRO Mechanic</div>
						<div style="font-size: 28px"><%=title%></div>
						<div style="font-size: 18px">
							<span id="asof"></span>
						</div>
					</div>
					<div class="col-sm-4"></div>
				</div>

				<input type="number" id="numberOfDecimal" style="display: none;"
					value=<%=numberOfDecimal%>> <input type="text"
					id="fiscalyear" style="display: none;" value=<%=fiscalyear%>>



				<div class="box-body">

					<div class="row">
						<div class="col-sm-12">
							<div id="cashflow-table_1"></div>
							<div id="cashflow-table_2"></div>
							<div id="cashflow-table_3"></div>
							<div id="cashflow-table_4"></div>
						</div>
					</div>

				</div>

			</div>
			
			<div class="col-sm-6" id="chgColsmHide" hidden>
				<div class="row">
					<div class="col-sm-4"></div>
					<div class="col-sm-4" style="text-align: center">
						<div style="font-size: 18px">SUNPRO Mechanic</div>
						<div style="font-size: 28px"><%=title%></div>
						<div style="font-size: 18px">
							<span id="lastasof"></span>
						</div>
					</div>
					<div class="col-sm-4"></div>
				</div>
				<div class="box-body">
					<div class="row">
						<div class="col-sm-12">
							<div id="lastcashflow-table_1"></div>
							<div id="lastcashflow-table_2"></div>
							<div id="lastcashflow-table_3"></div>
							<div id="lastcashflow-table_4"></div>
						</div>
					</div>

				</div>

			</div>
		</div>




	</div>
</div>
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>
<style>
/* @page {
	size: auto;
	margin: 0mm;
} */

.print-display {
	display: none;
}
/* @media print {
   .box {
       display: none;
    }
    .print-display {
       display: block;
    }
} */
.searchAccFilter {
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
</style>
<script>
var finalProjId = "";
var fromDateFormatted;
var numberOfDecimal = $("#numberOfDecimal").val();
var toDateFormatted;
function journaldatechanged(node){
	var period=node.value;
	
	$('#fromdate').prop('disabled', true);
		$('#todate').prop('disabled', true);
		$('#srbtn').hide();

	if(!period && node.length > 1)
	{
		period = node;	
	}
	
	var to;
	var from;	
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
		 //from = moment(formattedFiscal).year(moment().year()).format('DD/MM/YYYY');
		 from = moment().year(moment().year()).startOf('year').format('DD/MM/YYYY');
		 to   = moment().format('DD/MM/YYYY');
		 //fromDateFormatted = moment(formattedFiscal).year(moment().year()).format('YYYY-MM-DD');
		 fromDateFormatted = moment().year(moment().year()).startOf('year').format('YYYY-MM-DD');
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
				
	}else if(period == "CUSTOM"){
		$('#fromdate').prop('disabled', false);
		$('#todate').prop('disabled', false);
		$('#fromdate').val('');
		$('#todate').val('');
		$('#srbtn').show();
	}


	if(period != "CUSTOM"){
		$('#fromdate').val(from);
		$('#todate').val(to);
		var asofdate=moment(toDateFormatted).format('DD MMM,YYYY');
		loadSection1Data(fromDateFormatted,toDateFormatted);
		loadSection2Data(fromDateFormatted,toDateFormatted);
		loadSection3Data(fromDateFormatted,toDateFormatted);
		loadSection4Data(fromDateFormatted,toDateFormatted);
		var frmDate = moment(fromDateFormatted).format('DD MMM,YYYY');
		$('#asof').html(frmDate+"-"+asofdate);
		//loadJournalData(fromDateFormatted,toDateFormatted);
		$('#asofDate').val(asofdate);
		
		var lastfromDateFormatted =  moment(fromDateFormatted).subtract(1, 'years');
		var lasttoDateFormatted =  moment(toDateFormatted).subtract(1, 'years');
		lastfromDateFormatted = moment(lastfromDateFormatted).format('YYYY-MM-DD');
		lasttoDateFormatted = moment(lasttoDateFormatted).format('YYYY-MM-DD');
		var lastasofdate=moment(lasttoDateFormatted).format('DD MMM,YYYY');
		var lastfrmDate = moment(lastfromDateFormatted).format('DD MMM,YYYY');
		$('#lastasof').html(lastfrmDate+"-"+lastasofdate);
		lastloadSection1Data(lastfromDateFormatted,lasttoDateFormatted);
		lastloadSection2Data(lastfromDateFormatted,lasttoDateFormatted);
		lastloadSection3Data(lastfromDateFormatted,lasttoDateFormatted);
		lastloadSection4Data(lastfromDateFormatted,lasttoDateFormatted);
		
	}
}
var tableJournalSummary;
var tableDataNested1;
var tableDataNested2;
var tableDataNested3;
var tableDataNested4;

var fromDate;
var toDate;
function postDatePickerInit(){
	if (period == 'CUSTOM'){
		var statementofcashflow_fromdate = getLocalStorageValue('statementofcashflow_fromdate', '', 'fromdate');
		$('#fromdate').datepicker("setDate", 
		        moment(statementofcashflow_fromdate, 'DD/MM/YYYY').toDate());
		var statementofcashflow_todate = getLocalStorageValue('statementofcashflow_todate', '', 'todate');
		$('#todate').datepicker("setDate", 
				moment(statementofcashflow_todate, 'DD/MM/YYYY').toDate());
	}		
	$('#srbtn').click();
}

$(document).ready(function() {
		var periodFromSession = getLocalStorageValue('statementofcashflow_reportperiod', 'TODAY', 'reportperiod');
		period = periodFromSession;
		$('#Lastyear').change(function() {
	        if(this.checked) {
	        	$('#chgColsm').removeClass('col-sm-12');
	        	$('#chgColsm').addClass('col-sm-6');
	        	$('#chgColsmHide').show();
		    	Searchcustomchecked();
	        }else{
	        	$('#chgColsm').removeClass('col-sm-6');
	        	$('#chgColsm').addClass('col-sm-12');
	        	$('#chgColsmHide').hide();
		    	Searchcustom();
	        }
		});
		  
			  
		$('#fromdate').prop('disabled', true);
			$('#todate').prop('disabled', true);
			$('#srbtn').hide();
			
		var to;
		var from;
		 if (typeof(Storage) !== "undefined") {
//			var periodFromSession = getLocalStorageValue('statementofcashflow_reportperiod', 'THISYEARTODATE', 'reportperiod');
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
			/*  var fiscalyear=$('#fiscalyear').val();
			 var formattedFiscal=moment(fiscalyear).format('YYYY-MM-DD');
			 from = moment(formattedFiscal).year(moment().year()).format('DD/MM/YYYY');
			 to   = moment().format('DD/MM/YYYY');
			 fromDateFormatted = moment(formattedFiscal).year(moment().year()).format('YYYY-MM-DD');
			 toDateFormatted   = moment().format('YYYY-MM-DD'); */
			 
			 var fiscalyear=$('#fiscalyear').val();
			 var formattedFiscal=moment(fiscalyear).format('YYYY-MM-DD');
			 //from = moment(formattedFiscal).year(moment().year()).format('DD/MM/YYYY');
			 from = moment().year(moment().year()).startOf('year').format('DD/MM/YYYY');
			 to   = moment().format('DD/MM/YYYY');
			 //fromDateFormatted = moment(formattedFiscal).year(moment().year()).format('YYYY-MM-DD');
			 fromDateFormatted = moment().year(moment().year()).startOf('year').format('YYYY-MM-DD');
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
					
		}	else if(period == "CUSTOM"){
			$('#fromdate').prop('disabled', false);
			$('#todate').prop('disabled', false);
			var fromdate=getLocalStorageValue('statementofcashflow_fromdate', '', 'fromdate');
			var todate = getLocalStorageValue('statementofcashflow_todate', '', 'todate');
			$('#fromdate').datepicker("setDate", 
			        moment(fromdate, 'DD/MM/YYYY').toDate());
			$('#todate').datepicker("setDate", 
			        moment(todate, 'DD/MM/YYYY').toDate());
			from = moment(fromdate).format('DD/MM/YYYY');
			to   = moment(todate).format('DD/MM/YYYY');
			fromDateFormatted = moment(fromdate).format('YYYY-MM-DD');
			toDateFormatted   = moment(todate).format('YYYY-MM-DD');
			$('#srbtn').show();
			if (fromdate != ''){
				Searchcustom();
			}
		}
		 $('#reportperiod').val(period);
		if(period != "CUSTOM"){
		
			$('#fromdate').val(from);
			$('#todate').val(to);
	
			var asofdate=moment(toDateFormatted).format('DD MMM,YYYY');
			var frmDate = moment(fromDateFormatted).format('DD MMM,YYYY');
			$('#asof').html(frmDate+"-"+asofdate);
			loadSection1Data(fromDateFormatted,toDateFormatted);
			loadSection2Data(fromDateFormatted,toDateFormatted);
			loadSection3Data(fromDateFormatted,toDateFormatted);
			loadSection4Data(fromDateFormatted,toDateFormatted);
			$('#asofDate').val(asofdate);
			fromDate = fromDateFormatted;
			toDate = toDateFormatted;

			var lastfromDateFormatted =  moment(fromDateFormatted).subtract(1, 'years');
			var lasttoDateFormatted =  moment(toDateFormatted).subtract(1, 'years');
			lastfromDateFormatted = moment(lastfromDateFormatted).format('YYYY-MM-DD');
			lasttoDateFormatted = moment(lasttoDateFormatted).format('YYYY-MM-DD');
			var lastasofdate=moment(lasttoDateFormatted).format('DD MMM,YYYY');
			var lastfrmDate = moment(lastfromDateFormatted).format('DD MMM,YYYY');
			$('#lastasof').html(lastfrmDate+"-"+lastasofdate);
			lastloadSection1Data(lastfromDateFormatted,lasttoDateFormatted);
			lastloadSection2Data(lastfromDateFormatted,lasttoDateFormatted);
			lastloadSection3Data(lastfromDateFormatted,lasttoDateFormatted);
			lastloadSection4Data(lastfromDateFormatted,lasttoDateFormatted);
			
		}
		
	});

	 $("#download-xlsx").click(function(){
		    $("#cashflow-table_1").tabulator("download", "xlsx", "data.xlsx");
		  });
	
	function loadSection1Data(from,to)
	{
		storeInLocalStorage('statementofcashflow_reportperiod', $('#reportperiod').val());
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('statementofcashflow_fromdate', $('#fromdate').val());
		}
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('statementofcashflow_todate', $('#todate').val());
		}
		var acctypes= "('32','31','27','34')";
		$.ajax({
			type : "POST",
			url: '/track/CashFlowServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getcashflow1",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes
			},
			success : function(cashflowlist) {
				var cashFlowArray=cashflowlist;
				var myJSON = JSON.stringify(cashFlowArray);
				//alert(myJSON);
				tableDataNested1=cashFlowArray;
				load_1();
			}
		});
	}
	
	function loadSection2Data(from,to)
	{
		$.ajax({
			type : "POST",
			url: '/track/CashFlowServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getcashflow2",
				fromDate:from,
				toDate:to
			},
			success : function(cashflowlist) {
				var cashFlowArray=cashflowlist;
				var myJSON = JSON.stringify(cashFlowArray);
				//alert(myJSON);
				tableDataNested2=cashFlowArray;
				load_2();
			}
		});
	}
	
	function loadSection3Data(from,to)
	{
		$.ajax({
			type : "POST",
			url: '/track/CashFlowServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getcashflow3",
				fromDate:from,
				toDate:to
			},
			success : function(cashflowlist) {
				var cashFlowArray=cashflowlist;
				var myJSON = JSON.stringify(cashFlowArray);
				//alert(myJSON);
				tableDataNested3=cashFlowArray;
				load_3();
			}
		});
	}
	
	
	
	function loadSection4Data(from,to)
	{
		$.ajax({
			type : "POST",
			url: '/track/CashFlowServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getcashflow4",
				fromDate:from,
				toDate:to
			},
			success : function(cashflowlist) {
				var cashFlowArray=cashflowlist;
				var myJSON = JSON.stringify(cashFlowArray);
				//alert(myJSON);
				tableDataNested4=cashFlowArray;
				load_4();
			}
		});
	}
	var openIcon = function(value, data, cell, row, options){ //plain text value
	    return "<i class='fa fa-edit'></i>"
	};
	var deleteIcon = function(value, data, cell, row, options){ //plain text value
	    return "<i class='fa fa-trash'></i>"
	};
	var menuTitleFormatter = function(cell, formatterParams, onRendered){
        var searchNode='<span class="glyphicon glyphicon-search searchAccFilter" aria-hidden="true"></span>';
	    return searchNode;
	};
	
	var table_1;
	var table_2;
	var table_3;
	var table_4;
	
	var table_1_total = 0.00;
	var table_2_total = 0.00;
	var table_3_total = 0.00;
	var table_4_total = 0.00;

	function load_1()
	{
		var node1;
			table_1 = new Tabulator("#cashflow-table_1", {
				 downloadConfig:{
				        columnHeaders:true, //do not include column headers in downloaded table
				        columnGroups:true, //do not include column groups in column headers for downloaded table
				        rowGroups:true, //do not include row groups in downloaded table
				        columnCalcs:true, //do not include column calcs in downloaded table
				        dataTree:true, //do not include data tree in downloaded table
				    },
				    downloadRowRange:"all",
		    data:tableDataNested1,
		    headerVisible:false,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		    ],
 		    groupBy:["account_type"],
 		    groupStartOpen:true,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		        });
 		        return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal)+"</a>";
 		    },
 		  // footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Gross Profit:</div><div class='pull-right' style='margin-right:10px' id='grossprofit'></div></div>",
		    columns:[
		    {title:"ACCOUNT", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	return value;
	    		
	    }},	
		  
		    {title:"Total", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
		    ],
		});
		ncpoa();	 
	}
	
	function load_2()
	{
		var node1;
			table_2 = new Tabulator("#cashflow-table_2", {
				 downloadConfig:{
				        columnHeaders:true, //do not include column headers in downloaded table
				        columnGroups:true, //do not include column groups in column headers for downloaded table
				        rowGroups:true, //do not include row groups in downloaded table
				        columnCalcs:true, //do not include column calcs in downloaded table
				        dataTree:true, //do not include data tree in downloaded table
				    },
				    downloadRowRange:"all",
		    data:tableDataNested2,
		   // headerVisible:false,
		   	headerSort:false,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		    ],
 		    groupBy:["account_type"],
 		    groupStartOpen:true,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		        });
 		        return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal)+"</a>";
 		    },
 		  footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Net Cash provided by operating activity:</div><div class='pull-right' style='margin-right:10px' id='ncpoa'></div></div>",
		    columns:[
		    {title:"Movements in working capital", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	return value;
	    		
	    }},	
		  
		    {title:"", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
		    ],
		});
			 
		ncpoa();	 
	}
	
	
	function ncpoa(){
		var data = table_1.getData();
		var total1=0.00;
		var profit=0.00;
		data.forEach(function(item){
			var accounttype=item.account_type;
			accounttype=accounttype.trim();
			if(accounttype=="Profit")
				{
				profit+=item.total;
				}
			else
				{
				total1+=item.total;
				}
	    });
		total1=total1.toFixed(numberOfDecimal);
		table_1_total = parseFloat(total1) + parseFloat(profit);
		var data2 = table_2.getData();
		var total2=0.00;
		data2.forEach(function(item){
				total2+=item.total;
	        });
		console.log("-------");
		console.log("total1-----"+total1);
		console.log("total2-----"+total2);
		console.log("-------");
		total2=total2.toFixed(numberOfDecimal);
		table_2_total = parseFloat(total2);
		var total3 = parseFloat(total1) + parseFloat(total2);
		total3=parseFloat(total3).toFixed(numberOfDecimal);
		$('#ncpoa').html(total3);
		NetCashatbank();
	}
	
	function load_3()
	{
		var node1;
			table_3 = new Tabulator("#cashflow-table_3", {
				 downloadConfig:{
				        columnHeaders:true, //do not include column headers in downloaded table
				        columnGroups:true, //do not include column groups in column headers for downloaded table
				        rowGroups:true, //do not include row groups in downloaded table
				        columnCalcs:true, //do not include column calcs in downloaded table
				        dataTree:true, //do not include data tree in downloaded table
				    },
				    downloadRowRange:"all",
		    data:tableDataNested3,
		   // headerVisible:false,
		   	headerSort:false,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		    ],
 		    groupBy:["account_type"],
 		    groupStartOpen:true,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		        });
 		        return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal)+"</a>";
 		    },
 		  footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Net cash (used in) investing activities:</div><div class='pull-right' style='margin-right:10px' id='ncia'></div></div>",
		    columns:[
		    {title:"Cash flows from investing activities", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	return value;
	    		
	    }},	
		  
		    {title:"", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
		    ],
		});
			 
		ncia();	 
	}
	
	function ncia(){
		var data3 = table_3.getData();
		var total=0.00;
		data3.forEach(function(item){
			total+=item.total;
	    });
		total=parseFloat(total).toFixed(numberOfDecimal);
		table_3_total = parseFloat(total);
		$('#ncia').html(total);
		NetCashatbank();
	}
	
	
	function load_4()
	{
		var node1;
			table_4 = new Tabulator("#cashflow-table_4", {
				 downloadConfig:{
				        columnHeaders:true, //do not include column headers in downloaded table
				        columnGroups:true, //do not include column groups in column headers for downloaded table
				        rowGroups:true, //do not include row groups in downloaded table
				        columnCalcs:true, //do not include column calcs in downloaded table
				        dataTree:true, //do not include data tree in downloaded table
				    },
				    downloadRowRange:"all",
		    data:tableDataNested4,
		   // headerVisible:false,
		   	headerSort:false,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		    ],
 		    groupBy:["account_type"],
 		    groupStartOpen:true,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		        });
 		        return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal)+"</a>";
 		    },
 		  footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Net cash generated from financing activities :</div><div class='pull-right' style='margin-right:10px' id='ncgfa'></div></div>" 
 		  +"<div class='row' style='margin-top: 2%;'><div class='pull-left' style='margin-left:30px'>Net increase in cash at bank :</div><div class='pull-right' style='margin-right:10px' id='netcash'></div></div>"
 		 +"<div class='row'><div class='pull-left' style='margin-left:30px'>Cash at bank at beginning of the year :</div><div class='pull-right' style='margin-right:10px' id='openbal'></div></div>"
 		 +"<div class='row' style='margin-top: 2%;'><div class='pull-left' style='margin-left:30px'>Cash at bank at end of the year :</div><div class='pull-right' style='margin-right:10px' id='closebal'></div></div>",
		    columns:[
		    {title:"Cash flows from financing activities", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	return value;
	    		
	    }},	
		  
		    {title:"", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
		    ],
		});
			 
		ncgfa();	 
	}
	
	function ncgfa(){
		var data4 = table_4.getData();
		var total=0.00;
		data4.forEach(function(item){
			total+=item.total;
	    });
		total=parseFloat(total).toFixed(numberOfDecimal);
		table_4_total = parseFloat(total);
		$('#ncgfa').html(total);
		NetCashatbank();
	}
	
	function NetCashatbank(){
		var total=0.00;
	    total = parseFloat(table_1_total)+parseFloat(table_2_total)+parseFloat(table_3_total)+parseFloat(table_4_total);
		total=parseFloat(total).toFixed(numberOfDecimal);
		$('#netcash').html(total);
		var from = $('#fromdate').val();
		var to = $('#todate').val();
		fromDateFormatted = moment(from, "DD/MM/YYYY").format('YYYY-MM-DD');
		toDateFormatted = moment(to, "DD/MM/YYYY").format('YYYY-MM-DD');
		$.ajax({
			type : "POST",
			url: '/track/CashFlowServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getcashflow5",
				fromDate : fromDateFormatted,
				toDate : toDateFormatted
			},
			success : function(cashflowlist) {
				var cashFlowArray=cashflowlist;
				$('#openbal').html(parseFloat(cashFlowArray.OPENBALANCE).toFixed(numberOfDecimal));
				$('#closebal').html(parseFloat(cashFlowArray.CLOSEBALANCE).toFixed(numberOfDecimal));
			}
		});
	}
	
	$("#pdfdownload").click(function(){
	   /*  table.download("pdf", "trialbalance.pdf", {
	        orientation:"portrait", //set page orientation to portrait
	        title:"Trial Balance Report", //add title to report
	    }); */
	    generate();
	    
	});

	function downTBExcel()
	{
		//alert("coming");
		window.location = "/track/CashFlowServlet?action=getCashFlowExcel&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted;
	}

	

	<%-- $("#exceldownload").click(function()
	{
		var tableData = table.getData();
		var titleNm = '<%=title%>';
		var asofdate = moment(toDateFormatted).format('MMM DD,YYYY');
		var dateStr = 'As of '+asofdate;		
		var json = JSON.stringify(tableData);
		$.ajax({
			type : "GET",
			url: '/track/CashFlowServlet',
			data : {
				action : "getCashFlowExcel",
				excelHeaderTitle: titleNm,
				excelHeaderDate: dateStr, 
				excelData: json
				},
			success : function(resultdata) {
				console.log(resultdata);
			}
		});   
		    
	}); --%>
	
	$("#sendemail").click(function(){
		//$("#common_email_modal #send_subject).val("Your Profit Loss Report");
		$('#common_email_modal').modal('toggle');
		loadBodyStyle();
	});
	
	function readTableRecord(itemid){
		//console.log($(ele).attr('id'));
		//alert("ReadTable"+itemid);
		$.ajax({
			type: 'post',
	        url: "/track/ChartOfAccountServlet?action=read_record",
	       	dataType:'json',
	   		data:  {
	   			"id":itemid
	   		},
	      
	        success: function (data) {
				
	         	
	        },
	        error: function (data) {	        	
	            alert(data.responseText);
	        }
		})
	}
function callDetails(account)
{
	//alert("Called");
	window.location.href="projecttrialbalancedetails.jsp?account="+account+"&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"";
}
	
function newcallDetails(account,accountName)
{
	//alert("Called");
	window.location.href="projecttrialbalancedetails.jsp?account="+account+"&accname="+accountName+"&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"";
	}
function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
}	
function generatePdfMail(dataUrl,attachName){
	var data = table_1.getData();
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
	doc.text('<%=PLNTDESC%>', cwidth, 50,'center');
	doc.setFontSize(20);
	doc.text('<%=title%>',cwidth,60,'center');
	doc.setFontSize(12);
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,cwidth,70,'center');
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);
	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=table_1.getGroups();
	var groups_2=table_2.getGroups();
	var groups_3=table_3.getGroups();
	var groups_4=table_4.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	    }); 
	});
	//var totalasset=$('#totalasset').html();
	//htmlTable+="<tr><td class='calculated'>Total Assets:</td><td class='calculated'>"+totalasset+"</td></tr>";
	groups_2.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	        

	    }); 
	});
	var ncpoa=$('#ncpoa').html();
	htmlTable+="<tr><td class='calculated'>Net Cash provided by operating activity:</td><td class='calculated'>"+ncpoa+"</td></tr>";
	groups_3.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	        

	    }); 
	});
	var ncia=$('#ncia').html();
	htmlTable+="<tr><td class='calculated'>Net cash (used in) investing activities:</td><td class='calculated'>"+ncia+"</td></tr>";
	groups_4.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	        

	    }); 
	});
	var ncgfa=$('#ncgfa').html();
	var netcash=$('#netcash').html();
	var openbal=$('#openbal').html();
	var closebal=$('#closebal').html();
	htmlTable+="<tr><td class='calculated'>Net cash generated from financing activities:</td><td class='calculated'>"+ncgfa+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Net increase in cash at bank:</td><td class='calculated'>"+netcash+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Cash at bank at beginning of the year:</td><td class='calculated'>"+openbal+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Cash at bank at end of the year:</td><td class='calculated'>"+closebal+"</td></tr>";
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'right'}},
		theme : 'plain',
		headStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		willDrawCell: function (data) {
            // Colspan
           // console.log(data.cell.raw.className);
            if (data.cell.raw.className=="innerHeader") {
            	//console.log("Entered");
            	doc.setFontStyle('bold');
                doc.setFontSize(10);
                doc.setTextColor(0, 0, 0);
                doc.setFillColor(230, 229, 229);
		}
            else if(data.cell.raw.className=="calculated")
            	{
            		doc.setFontStyle('bold');
                	doc.setFontSize(10);
                	doc.setTextColor(0, 0, 0);
                	//doc.setFillColor(230, 229, 229);
            	}
		},
		didParseCell: function (data) {
			if (data.column.index === 1) {
				data.cell.styles.halign = 'right';
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
			doc.text(str, 258,
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
	
function generatePdf(dataUrl){	
	var data = table_1.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var cwidth = doc.internal.pageSize.getWidth()/2;
	var pageNumber;
	/* Top Right */
	var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	doc.setFontSize(12);
	doc.text('<%=PLNTDESC%>', cwidth, 50,'center');
	doc.setFontSize(20);
	doc.text('<%=title%>',cwidth,60,'center');
	doc.setFontSize(12);
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,cwidth,70,'center');
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);
	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=table_1.getGroups();
	var groups_2=table_2.getGroups();
	var groups_3=table_3.getGroups();
	var groups_4=table_4.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	    }); 
	});
	//var totalasset=$('#totalasset').html();
	//htmlTable+="<tr><td class='calculated'>Total Assets:</td><td class='calculated'>"+totalasset+"</td></tr>";
	groups_2.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	        

	    }); 
	});
	var ncpoa=$('#ncpoa').html();
	htmlTable+="<tr><td class='calculated'>Net Cash provided by operating activity:</td><td class='calculated'>"+ncpoa+"</td></tr>";
	groups_3.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	        

	    }); 
	});
	var ncia=$('#ncia').html();
	htmlTable+="<tr><td class='calculated'>Net cash (used in) investing activities:</td><td class='calculated'>"+ncia+"</td></tr>";
	groups_4.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	        

	    }); 
	});
	var ncgfa=$('#ncgfa').html();
	var netcash=$('#netcash').html();
	var openbal=$('#openbal').html();
	var closebal=$('#closebal').html();
	htmlTable+="<tr><td class='calculated'>Net cash generated from financing activities:</td><td class='calculated'>"+ncgfa+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Net increase in cash at bank:</td><td class='calculated'>"+netcash+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Cash at bank at beginning of the year:</td><td class='calculated'>"+openbal+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Cash at bank at end of the year:</td><td class='calculated'>"+closebal+"</td></tr>";
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'right'}},
		theme : 'plain',
		headStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		willDrawCell: function (data) {
            // Colspan
           // console.log(data.cell.raw.className);
            if (data.cell.raw.className=="innerHeader") {
            	//console.log("Entered");
            	doc.setFontStyle('bold');
                doc.setFontSize(10);
                doc.setTextColor(0, 0, 0);
                doc.setFillColor(230, 229, 229);
		}
            else if(data.cell.raw.className=="calculated")
            	{
            		doc.setFontStyle('bold');
                	doc.setFontSize(10);
                	doc.setTextColor(0, 0, 0);
                	//doc.setFillColor(230, 229, 229);
            	}
		},
		didParseCell: function (data) {
			if (data.column.index === 1) {
				data.cell.styles.halign = 'right';
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
			doc.text(str, 178,
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
	doc.save('CashFlow.pdf');
}
function generatePdfPrint(dataUrl){
	var data = table_1.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var cwidth = doc.internal.pageSize.getWidth()/2;
	var pageNumber;
	/* Top Right */
	var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	doc.setFontSize(12);
	doc.text('<%=PLNTDESC%>', cwidth, 50,'center');
	doc.setFontSize(20);
	doc.text('<%=title%>',cwidth,60,'center');
	doc.setFontSize(12);
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,cwidth,70,'center');
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);
	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=table_1.getGroups();
	var groups_2=table_2.getGroups();
	var groups_3=table_3.getGroups();
	var groups_4=table_4.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	    }); 
	});
	//var totalasset=$('#totalasset').html();
	//htmlTable+="<tr><td class='calculated'>Total Assets:</td><td class='calculated'>"+totalasset+"</td></tr>";
	groups_2.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	        

	    }); 
	});
	var ncpoa=$('#ncpoa').html();
	htmlTable+="<tr><td class='calculated'>Net Cash provided by operating activity:</td><td class='calculated'>"+ncpoa+"</td></tr>";
	groups_3.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	        

	    }); 
	});
	var ncia=$('#ncia').html();
	htmlTable+="<tr><td class='calculated'>Net cash (used in) investing activities:</td><td class='calculated'>"+ncia+"</td></tr>";
	groups_4.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total+"</td></tr>";
	        

	    }); 
	});
	var ncgfa=$('#ncgfa').html();
	var netcash=$('#netcash').html();
	var openbal=$('#openbal').html();
	var closebal=$('#closebal').html();
	htmlTable+="<tr><td class='calculated'>Net cash generated from financing activities:</td><td class='calculated'>"+ncgfa+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Net increase in cash at bank:</td><td class='calculated'>"+netcash+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Cash at bank at beginning of the year:</td><td class='calculated'>"+openbal+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Cash at bank at end of the year:</td><td class='calculated'>"+closebal+"</td></tr>";
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'right'}},
		theme : 'plain',
		headStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		willDrawCell: function (data) {
            // Colspan
           // console.log(data.cell.raw.className);
            if (data.cell.raw.className=="innerHeader") {
            	//console.log("Entered");
            	doc.setFontStyle('bold');
                doc.setFontSize(10);
                doc.setTextColor(0, 0, 0);
                doc.setFillColor(230, 229, 229);
		}
            else if(data.cell.raw.className=="calculated")
            	{
            		doc.setFontStyle('bold');
                	doc.setFontSize(10);
                	doc.setTextColor(0, 0, 0);
                	//doc.setFillColor(230, 229, 229);
            	}
		},
		didParseCell: function (data) {
			if (data.column.index === 1) {
				data.cell.styles.halign = 'right';
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
			doc.text(str, 178,
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
function generate() {
	
var img = toDataURL($(".dash-logo").attr("src"),
		function(dataUrl) {
			generatePdf(dataUrl);
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
		var asofdate=moment(toDateFormatted).format('DD MMM,YYYY');
		loadSection1Data(fromDateFormatted,toDateFormatted);
		loadSection2Data(fromDateFormatted,toDateFormatted);
		loadSection3Data(fromDateFormatted,toDateFormatted);
		loadSection4Data(fromDateFormatted,toDateFormatted);
		var frmDate = moment(fromDateFormatted).format('DD MMM,YYYY');
		$('#asof').html(frmDate+"-"+asofdate);
		//loadJournalData(fromDateFormatted,toDateFormatted);
		$('#asofDate').val(asofdate);
		
		var lastfromDateFormatted =  moment(fromDateFormatted).subtract(1, 'years');
		var lasttoDateFormatted =  moment(toDateFormatted).subtract(1, 'years');
		lastfromDateFormatted = moment(lastfromDateFormatted).format('YYYY-MM-DD');
		lasttoDateFormatted = moment(lasttoDateFormatted).format('YYYY-MM-DD');
		var lastasofdate=moment(lasttoDateFormatted).format('DD MMM,YYYY');
		var lastfrmDate = moment(lastfromDateFormatted).format('DD MMM,YYYY');
		$('#lastasof').html(lastfrmDate+"-"+lastasofdate);
		lastloadSection1Data(lastfromDateFormatted,lasttoDateFormatted);
		lastloadSection2Data(lastfromDateFormatted,lasttoDateFormatted);
		lastloadSection3Data(lastfromDateFormatted,lasttoDateFormatted);
		lastloadSection4Data(lastfromDateFormatted,lasttoDateFormatted);
		
	}
}


function Searchcustomchecked(){
	debugger;
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
		var asofdate=moment(toDateFormatted).format('DD MMM,YYYY');
		loadSection1Data(fromDateFormatted,toDateFormatted);
		loadSection2Data(fromDateFormatted,toDateFormatted);
		loadSection3Data(fromDateFormatted,toDateFormatted);
		loadSection4Data(fromDateFormatted,toDateFormatted);
		var frmDate = moment(fromDateFormatted).format('DD MMM,YYYY');
		$('#asof').html(frmDate+"-"+asofdate);
		//loadJournalData(fromDateFormatted,toDateFormatted);
		$('#asofDate').val(asofdate);
		
		var lastfromDateFormatted =  moment(fromDateFormatted).subtract(1, 'years');
		var lasttoDateFormatted =  moment(toDateFormatted).subtract(1, 'years');
		lastfromDateFormatted = moment(lastfromDateFormatted).format('YYYY-MM-DD');
		lasttoDateFormatted = moment(lasttoDateFormatted).format('YYYY-MM-DD');
		var lastasofdate=moment(lasttoDateFormatted).format('DD MMM,YYYY');
		var lastfrmDate = moment(lastfromDateFormatted).format('DD MMM,YYYY');
		$('#lastasof').html(lastfrmDate+"-"+lastasofdate);
		lastloadSection1Data(lastfromDateFormatted,lasttoDateFormatted);
		lastloadSection2Data(lastfromDateFormatted,lasttoDateFormatted);
		lastloadSection3Data(lastfromDateFormatted,lasttoDateFormatted);
		lastloadSection4Data(lastfromDateFormatted,lasttoDateFormatted);

	}
}


/* --------------------------------------------------- */
var lastfromDateFormatted;
var lasttoDateFormatted;
var lasttableDataNested1;
var lasttableDataNested2;
var lasttableDataNested3;
var lasttableDataNested4;

var lastfromDate;
var lasttoDate;
function lastloadSection1Data(from,to)
	{
		var acctypes= "('32','31','27','34')";
		$.ajax({
			type : "POST",
			url: '/track/CashFlowServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getcashflow1",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes
			},
			success : function(cashflowlist) {
				var cashFlowArray=cashflowlist;
				var myJSON = JSON.stringify(cashFlowArray);
				//alert(myJSON);
				lasttableDataNested1=cashFlowArray;
				lastload_1();
			}
		});
	}
	
	function lastloadSection2Data(from,to)
	{
		$.ajax({
			type : "POST",
			url: '/track/CashFlowServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getcashflow2",
				fromDate:from,
				toDate:to
			},
			success : function(cashflowlist) {
				var cashFlowArray=cashflowlist;
				var myJSON = JSON.stringify(cashFlowArray);
				//alert(myJSON);
				lasttableDataNested2=cashFlowArray;
				lastload_2();
			}
		});
	}
	
	function lastloadSection3Data(from,to)
	{
		$.ajax({
			type : "POST",
			url: '/track/CashFlowServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getcashflow3",
				fromDate:from,
				toDate:to
			},
			success : function(cashflowlist) {
				var cashFlowArray=cashflowlist;
				var myJSON = JSON.stringify(cashFlowArray);
				//alert(myJSON);
				lasttableDataNested3=cashFlowArray;
				lastload_3();
			}
		});
	}
	
	
	
	function lastloadSection4Data(from,to)
	{
		$.ajax({
			type : "POST",
			url: '/track/CashFlowServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getcashflow4",
				fromDate:from,
				toDate:to
			},
			success : function(cashflowlist) {
				var cashFlowArray=cashflowlist;
				var myJSON = JSON.stringify(cashFlowArray);
				//alert(myJSON);
				lasttableDataNested4=cashFlowArray;
				lastload_4();
			}
		});
	}
	
	var lasttable_1;
	var lasttable_2;
	var lasttable_3;
	var lasttable_4;
	
	var lasttable_1_total = 0.00;
	var lasttable_2_total = 0.00;
	var lasttable_3_total = 0.00;
	var lasttable_4_total = 0.00;

	function lastload_1()
	{
		var node1;
		lasttable_1 = new Tabulator("#lastcashflow-table_1", {
				 downloadConfig:{
				        columnHeaders:true, //do not include column headers in downloaded table
				        columnGroups:true, //do not include column groups in column headers for downloaded table
				        rowGroups:true, //do not include row groups in downloaded table
				        columnCalcs:true, //do not include column calcs in downloaded table
				        dataTree:true, //do not include data tree in downloaded table
				    },
				    downloadRowRange:"all",
		    data:lasttableDataNested1,
		    headerVisible:false,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		    ],
 		    groupBy:["account_type"],
 		    groupStartOpen:true,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		        });
 		        return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal)+"</a>";
 		    },
 		  // footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Gross Profit:</div><div class='pull-right' style='margin-right:10px' id='grossprofit'></div></div>",
		    columns:[
		    {title:"ACCOUNT", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	return value;
	    		
	    }},	
		  
		    {title:"Total", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
		    ],
		});
		lastncpoa();	 
	}
	
	function lastload_2()
	{
		var node1;
		lasttable_2 = new Tabulator("#lastcashflow-table_2", {
				 downloadConfig:{
				        columnHeaders:true, //do not include column headers in downloaded table
				        columnGroups:true, //do not include column groups in column headers for downloaded table
				        rowGroups:true, //do not include row groups in downloaded table
				        columnCalcs:true, //do not include column calcs in downloaded table
				        dataTree:true, //do not include data tree in downloaded table
				    },
				    downloadRowRange:"all",
		    data:lasttableDataNested2,
		   // headerVisible:false,
		   	headerSort:false,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		    ],
 		    groupBy:["account_type"],
 		   	groupStartOpen:true,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		        });
 		        return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal)+"</a>";
 		    },
 		  footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Net Cash provided by operating activity:</div><div class='pull-right' style='margin-right:10px' id='lastncpoa'></div></div>",
		    columns:[
		    {title:"Movements in working capital", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	return value;
	    		
	    }},	
		  
		    {title:"", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
		    ],
		});
			 
		lastncpoa();	 
	}
	
	
	function lastncpoa(){
		var data = lasttable_1.getData();
		var total1=0.00;
		var profit=0.00;
		data.forEach(function(item){
			var accounttype=item.account_type;
			accounttype=accounttype.trim();
			if(accounttype=="Profit")
				{
				profit+=item.total;
				}
			else
				{
				total1+=item.total;
				}
	    });
		total1=total1.toFixed(numberOfDecimal);
		lasttable_1_total = parseFloat(total1) + parseFloat(profit);
		var data2 = lasttable_2.getData();
		var total2=0.00;
		data2.forEach(function(item){
				total2+=item.total;
	        });
		console.log("-------");
		console.log("total1-----"+total1);
		console.log("total2-----"+total2);
		console.log("-------");
		total2=total2.toFixed(numberOfDecimal);
		lasttable_2_total = parseFloat(total2);
		var total3 = parseFloat(total1) + parseFloat(total2);
		total3=parseFloat(total3).toFixed(numberOfDecimal);
		$('#lastncpoa').html(total3);
		lastNetCashatbank();
	}
	
	function lastload_3()
	{
		var node1;
		lasttable_3 = new Tabulator("#lastcashflow-table_3", {
				 downloadConfig:{
				        columnHeaders:true, //do not include column headers in downloaded table
				        columnGroups:true, //do not include column groups in column headers for downloaded table
				        rowGroups:true, //do not include row groups in downloaded table
				        columnCalcs:true, //do not include column calcs in downloaded table
				        dataTree:true, //do not include data tree in downloaded table
				    },
				    downloadRowRange:"all",
		    data:lasttableDataNested3,
		   // headerVisible:false,
		   	headerSort:false,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		    ],
 		    groupBy:["account_type"],
 		   groupStartOpen:true,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		        });
 		        return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal)+"</a>";
 		    },
 		  footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Net cash (used in) investing activities:</div><div class='pull-right' style='margin-right:10px' id='lastncia'></div></div>",
		    columns:[
		    {title:"Cash flows from investing activities", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	return value;
	    		
	    }},	
		  
		    {title:"", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
		    ],
		});
			 
		lastncia();	 
	}
	
	function lastncia(){
		var data3 = lasttable_3.getData();
		var total=0.00;
		data3.forEach(function(item){
			total+=item.total;
	    });
		total=parseFloat(total).toFixed(numberOfDecimal);
		lasttable_3_total = parseFloat(total);
		$('#lastncia').html(total);
		lastNetCashatbank();
	}
	
	
	function lastload_4()
	{
		var node1;
		lasttable_4 = new Tabulator("#lastcashflow-table_4", {
				 downloadConfig:{
				        columnHeaders:true, //do not include column headers in downloaded table
				        columnGroups:true, //do not include column groups in column headers for downloaded table
				        rowGroups:true, //do not include row groups in downloaded table
				        columnCalcs:true, //do not include column calcs in downloaded table
				        dataTree:true, //do not include data tree in downloaded table
				    },
				    downloadRowRange:"all",
		    data:lasttableDataNested4,
		   // headerVisible:false,
		   	headerSort:false,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		    ],
 		    groupBy:["account_type"],
 		   groupStartOpen:true,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		        });
 		        return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal)+"</a>";
 		    },
 		  footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Net cash generated from financing activities :</div><div class='pull-right' style='margin-right:10px' id='lastncgfa'></div></div>" 
 		  +"<div class='row' style='margin-top: 2%;'><div class='pull-left' style='margin-left:30px'>Net increase in cash at bank :</div><div class='pull-right' style='margin-right:10px' id='lastnetcash'></div></div>"
 		 +"<div class='row'><div class='pull-left' style='margin-left:30px'>Cash at bank at beginning of the year :</div><div class='pull-right' style='margin-right:10px' id='lastopenbal'></div></div>"
 		 +"<div class='row' style='margin-top: 2%;'><div class='pull-left' style='margin-left:30px'>Cash at bank at end of the year :</div><div class='pull-right' style='margin-right:10px' id='lastclosebal'></div></div>",
		    columns:[
		    {title:"Cash flows from financing activities", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	return value;
	    		
	    }},	
		  
		    {title:"", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
		    ],
		});
			 
		lastncgfa();	 
	}
	
	function lastncgfa(){
		var data4 = lasttable_4.getData();
		var total=0.00;
		data4.forEach(function(item){
			total+=item.total;
	    });
		total=parseFloat(total).toFixed(numberOfDecimal);
		lasttable_4_total = parseFloat(total);
		$('#lastncgfa').html(total);
		lastNetCashatbank();
	}
	
	function lastNetCashatbank(){
		var total=0.00;
	    total = parseFloat(lasttable_1_total)+parseFloat(lasttable_2_total)+parseFloat(lasttable_3_total)+parseFloat(lasttable_4_total);
		total=parseFloat(total).toFixed(numberOfDecimal);
		$('#lastnetcash').html(total);
		var from = $('#fromdate').val();
		var to = $('#todate').val();
		fromDateFormatted = moment(from, "DD/MM/YYYY").format('YYYY-MM-DD');
		toDateFormatted = moment(to, "DD/MM/YYYY").format('YYYY-MM-DD');
		$.ajax({
			type : "POST",
			url: '/track/CashFlowServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getcashflow5",
				fromDate : fromDateFormatted,
				toDate : toDateFormatted
			},
			success : function(cashflowlist) {
				var cashFlowArray=cashflowlist;
				$('#lastopenbal').html(parseFloat(cashFlowArray.OPENBALANCE).toFixed(numberOfDecimal));
				$('#lastclosebal').html(parseFloat(cashFlowArray.CLOSEBALANCE).toFixed(numberOfDecimal));
			}
		});
	}




</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

