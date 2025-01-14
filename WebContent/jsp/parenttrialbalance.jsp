<%@ page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.ParentChildCmpDet"%>
<%@ page import="com.track.util.StrUtils"%> 
<%@ page import="java.io.*" %>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Consolidated Trial Balance";
	String title2 = "Trial Balance";
	String plant1 = StrUtils.fString((String) session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant1);
	String fiscalyear = plantMstDAO.getFiscalYear(plant1);
	ArrayList plntList = plantMstDAO.getPlantMstDetails(plant1);
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
	
	ParentChildCmpDetDAO pcdao = new ParentChildCmpDetDAO();
	List<ParentChildCmpDet> plantlist = pcdao.getAllParentChildCmpDetdropdown(plant1,"");
	
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
<style>
.dropdown-check-list {
  display: inline-block;
}

.dropdown-check-list .anchor {
  position: relative;
  cursor: pointer;
  display: inline-block;
  padding: 5px 50px 5px 10px;
  border: 1px solid #ccc;
}

.dropdown-check-list .anchor:after {
  position: absolute;
  content: "";
  border-left: 2px solid black;
  border-top: 2px solid black;
  padding: 5px;
  right: 10px;
  top: 20%;
  -moz-transform: rotate(-135deg);
  -ms-transform: rotate(-135deg);
  -o-transform: rotate(-135deg);
  -webkit-transform: rotate(-135deg);
  transform: rotate(-135deg);
}

.dropdown-check-list .anchor:active:after {
  right: 8px;
  top: 21%;
}

.dropdown-check-list ul.items {
  padding: 2px;
  display: none;
  margin: 0;
  border: 1px solid #ccc;
  border-top: none;
}

.dropdown-check-list ul.items li {
  list-style: none;
}

.dropdown-check-list.visible .anchor {
  color: #0094ff;
}

.dropdown-check-list.visible .items {
  display: block;
}

.item {
  width: 50%;
  flex-shrink: 0;
  background-color: white;
  /* border: 1px solid; */
  margin: 1%;
  padding-top: 2%;
}

.containerx {
  /* width: 177px;
  height: 102px; */
  display: flex;
  overflow-x: auto;
  background-color: lightgrey;
}
</style>
<div class="container-fluid m-t-20">

	<div class="box">
	<!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                <li><label>Consolidated Trial Balance</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 

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
						
						<div class="col-sm-3" style="vertical-align: baseline;">
							<div id="list1" class="dropdown-check-list" tabindex="100">
							  <span class="anchor">Select Company</span>
							  <ul class="items">
							   <!--  <li><input type="checkbox" />aaa </li> -->
							   <%for (ParentChildCmpDet plist : plantlist) {
								   	String plant=plist.getCHILD_PLANT(); 
									String name = plantMstDAO.getcmpyname(plant);
									%>
							   <li><input type="checkbox" class="companyname" id="<%=plant%>drop" value="<%=plant%>" checked/>&nbsp;<%=name%></li>
							   <%} %>
							    
							  </ul>
							</div>
						</div>
						
						<div class="col-sm-2" style="vertical-align: baseline;"><button class="btn btn-success" id="srbtn" onclick="Searchcustom();" type="button">Run</button></div>
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
									<!-- <a href="/track/ParentTrialBalanceServlet?action=getTrialBalanceAsExcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>-->
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
		
		
	 <%-- <div class="row">
			<div class="col-sm-4"></div>
			<div class="col-sm-4" style="text-align: center">
				<div style="font-size: 18px"><%=PLNTDESC%></div>
				<div style="font-size: 28px"><%=title%></div>
				<div style="font-size: 18px">
					As of <span id="asof"></span>
				</div>
			</div>
			<div class="col-sm-4"></div>
		</div>

		<input type="number" id="numberOfDecimal" style="display: none;"
			value=<%=numberOfDecimal%>> <input type="text"
			id="fiscalyear" style="display: none;" value=<%=fiscalyear%>>
			
			
			
		<div class="box-body">
			
			
			<div id="journal-table">
			
			</div>
		</div>  --%>
		
		<div id="print_id">
			<input type="hidden" name="plantlist" id="plantlist" value=""/>
			<div class="containerx">
			<%for (ParentChildCmpDet plist : plantlist) {
					String plant=plist.getCHILD_PLANT(); 
					String name = plantMstDAO.getcmpyname(plant);
					%>
			<div class="item" id="<%=plant %>tab">
			<div class="col-sm-12">
				<div class="col-sm-4">
				</div>
				<div class="col-sm-4" style="text-align:center">
					<div style="font-size:14px"><%=name%></div>
					<div style="font-size:16px;font-weight: 600;"><%=title2%></div>
					<div style="font-size:14px">As of <span class="asof"></span></div>
				</div>
				<div class="col-sm-4">
				</div>
			</div>
				
			<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
			<input type="text" id="fiscalyear" style="display:none;" value=<%=fiscalyear%>>
		<div class="box-body">
			<div class="row">
				
				<div class="col-sm-12">
					<div id="<%=plant %>journal-table" style="width:514px"></div>
				</div>
				
			</div>		
			
		</div>
		</div>
		<%} %>
		</div>
		</div> 
		
		
		

	</div>
</div>
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>
<style>
@page {
	size: auto;
	margin: 0mm;
}

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
var fromDateFormatted;
var numberOfDecimal = $("#numberOfDecimal").val();
var toDateFormatted;

var arr = new Array();
<%for (ParentChildCmpDet plist : plantlist) {
	String plant=plist.getCHILD_PLANT(); %>
	arr.push('<%=plant %>');
<%}%>

function journaldatechanged(node){
	var period=node.value;
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
		var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
		$('.asof').html(asofdate);
		<%for (ParentChildCmpDet plist : plantlist) {
			String plant=plist.getCHILD_PLANT(); %>
			<%=plant %>loadJournalData(fromDateFormatted,toDateFormatted);
			<%}%>
		$('#asofDate').val(asofdate);
	}
}
<%for (ParentChildCmpDet plist : plantlist) {
	String plant=plist.getCHILD_PLANT(); %>
var <%=plant %>tableJournalSummary;
var <%=plant %>tableDataNested;
<%}%>

var fromDate;
var toDate;
function postDatePickerInit(){
	if (period == 'CUSTOM'){
		var parenttrialbalance_fromdate = getLocalStorageValue('parenttrialbalance_fromdate', '', 'fromdate');
		$('#fromdate').datepicker("setDate", 
		        moment(parenttrialbalance_fromdate, 'DD/MM/YYYY').toDate());
		var parenttrialbalance_todate = getLocalStorageValue('parenttrialbalance_todate', '', 'todate');
		$('#todate').datepicker("setDate", 
				moment(parenttrialbalance_todate, 'DD/MM/YYYY').toDate());
	}		
	$('#srbtn').click();
}


	$(document).ready(function() {
		var periodFromSession = getLocalStorageValue('parenttrialbalance_reportperiod', 'THISYEARTODATE', 'reportperiod');
		period = periodFromSession;
		var to;
		var from;
		$('#fromdate').prop('disabled', true);
		$('#todate').prop('disabled', true);
		$('#srbtn').hide();
		$('#plantlist').val(arr);
		var checkList = document.getElementById('list1');
		checkList.getElementsByClassName('anchor')[0].onclick = function(evt) {
		  if (checkList.classList.contains('visible'))
		    checkList.classList.remove('visible');
		  else
		    checkList.classList.add('visible');
		}
		
		<%for (ParentChildCmpDet plist : plantlist) {
			String plant=plist.getCHILD_PLANT(); %>
			 $('#<%=plant %>drop').change(function() {
			        if(this.checked) {
			        	$('#<%=plant %>tab').show();
			        }else{
			        	$('#<%=plant %>tab').hide();
			        }
			        updateSelectedCompaniesInLocalStorage('parenttrialbalance_companieslist');   
			    });
		<%}%>
		var selectedCompanies = getLocalStorageValue('parenttrialbalance_companieslist', '', '');
		$(".companyname").each(function( index  ) {
			if (selectedCompanies != '' && selectedCompanies.search($( this ).val()) == -1){
	  			$( this ).prop('checked', false);
	  			$('#' + $( this ).val() + 'tab').hide();
	      	}
		});
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
		}


		if(period != "CUSTOM"){
			$('#fromdate').val(from);
			$('#todate').val(to);
			$('#reportperiod').val(period);
			var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
			$('.asof').html(asofdate);
			<%for (ParentChildCmpDet plist : plantlist) {
				String plant=plist.getCHILD_PLANT(); %>
				<%=plant%>loadJournalData(fromDateFormatted,toDateFormatted);
			<%}%>
			$('#asofDate').val(asofdate);
	
			fromDate = fromDateFormatted;
			toDate = toDateFormatted;
		}
		
					});
	
	
	<%for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); %>
	
	function <%=plant%>loadJournalData(from,to)
	{
		storeInLocalStorage('parenttrialbalance_reportperiod', $('#reportperiod').val());
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('parenttrialbalance_fromdate', $('#fromdate').val());
		}
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('parenttrialbalance_todate', $('#todate').val());
		}
		updateSelectedCompaniesInLocalStorage('parenttrialbalance_companieslist');
		//alert("loading data");
		$("#loader").show();
		$.ajax({
			type : "GET",
			url: '/track/ParentTrialBalanceServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getTrialBalance",
				fromDate:from,
				toDate:to,
				plant:"<%=plant%>"
			},
			success : function(journallist) {
				$("#loader").hide();
				var journalArray=journallist;
				var myJSON = JSON.stringify(journalArray);
				console.log(myJSON);
				<%=plant%>tableDataNested=journalArray;
				<%=plant%>loadJournal();
			}
		});
	}

	var <%=plant%>table;
	var <%=plant%>totaldebit;
	var <%=plant%>totalcredit;
	function <%=plant%>loadJournal()
	{
		 var <%=plant%>bottomCalculas1 = function(values, data, calcParams){
		    var calc = 0;
		    values.forEach(function(value){
		     calc+=value;
		    });
		    <%=plant%>totaldebit=parseFloat(calc).toFixed(numberOfDecimal);
		    return parseFloat(calc).toFixed(numberOfDecimal);
		} 
		 var <%=plant%>bottomCalculas2 = function(values, data, calcParams){
			    var calc = 0;
			    values.forEach(function(value){
			     calc+=value;
			    });
			    <%=plant%>totalcredit=parseFloat(calc).toFixed(numberOfDecimal);
			    return parseFloat(calc).toFixed(numberOfDecimal);
			} 
		
		 <%=plant%>table = new Tabulator("#<%=plant%>journal-table", {
			printAsHtml:true,
			printVisibleRows:false,
			 printCopyStyle:true,
		    //height:"711px",
		     data:<%=plant%>tableDataNested,
		    layout:"fitColumns",
		    columnCalcs:"table",
 		     groupBy:["main_account"],
		    columns:[
		    {title:"ACCOUNT", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	var account=data.account_name;
		    	return '<a onclick="<%=plant%>newcallDetails(\''+data.account_id+'\',\''+value+'\')">'+value+'</a>';
	    		
	    },bottomCalcFormatter:function(cell, formatterParams, onRendered){
	    	return "Total";
	    }
		    },	
		  
		    {title:"NET DEBIT", field:"net_debit",bottomCalc:<%=plant%>bottomCalculas1, formatterParams:{precision:numberOfDecimal},bottomCalcParams:{
		        precision:numberOfDecimal,
		    },formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	if(value>0)
		    		{
		    		value=parseFloat(value).toFixed(numberOfDecimal);
		    			return value;
		    		}
		    	else
		    		{
		    			return "";
		    		}
	    		
	    }},
		  
		    {title:"NET CREDIT", field:"net_credit",formatterParams:{precision:numberOfDecimal},bottomCalc:<%=plant%>bottomCalculas2,bottomCalcParams:{
		        precision:numberOfDecimal,
		    },formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	if(value>0)
	    		{
		    		value=parseFloat(value).toFixed(numberOfDecimal);
	    			return value;
	    		}
	    	else
	    		{
	    			return "";
	    		}
	    }},
		    ],
		});
			 
	}
	
	function <%=plant%>callDetails(account)
	{
		//alert("Called");
		window.location.href="../accountant/parenttrialbaldetail?account="+account+"&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"&plant=<%=plant%>";
		}
	function <%=plant%>newcallDetails(account,accountName)
	{
		//alert("Called");
		window.location.href="../accountant/parenttrialbaldetail?account="+account+"&accname="+accountName+"&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"&plant=<%=plant%>";
		}
	<%}%>
	
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
		var plist = $('input[name ="plantlist"]').val();
		window.location = "/track/ParentTrialBalanceServlet?action=getTrialBalanceAsExcel&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"&plist="+plist;
	}
// 	$("#tbsexcel").on('click', function() 
//     {
// 		window.location = "/track/ParentTrialBalanceServlet?action=getTrialBalanceAsExcel&fromDate="+fromDate+"&toDate="+toDate;
// 		  //location.href = "/track/ParentTrialBalanceServlet?action=getTrialBalanceAsExcel&fromDate="+fromDate+"&toDate="+toDate;
		 
//     });
	

	<%-- $("#exceldownload").click(function()
	{
		var tableData = table.getData();
		var titleNm = '<%=title%>';
		var asofdate = moment(toDateFormatted).format('MMM DD,YYYY');
		var dateStr = 'As of '+asofdate;		
		var json = JSON.stringify(tableData);
		$.ajax({
			type : "GET",
			url: '/track/ParentTrialBalanceServlet',
			data : {
				action : "getTrialBalanceAsExcel",
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
	

	

function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
}	
function generatePdfMail(dataUrl,attachName){
	<%for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); 
		%>
	var <%=plant %>data = <%=plant %>table.getData();
	<%}%>
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	var totalPagesExp = "{total_pages_cont_string}";
	/* Top Right */
	var pnext = "0";
		<%for (ParentChildCmpDet plist : plantlist) {
					String plant=plist.getCHILD_PLANT(); 
					String name = plantMstDAO.getcmpyname(plant);
					%>
					var inc;
					for (inc = 0; inc < arr.length; ++inc) {
					    if(arr[inc] == '<%=plant %>'){
					 

					if(pnext != "0"){
						doc.addPage();
					}
					pnext="1";
	var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	//doc.setFontStyle("bold");
	doc.text('<%=name%>', 110, 50, 'center');
	doc.setFontSize(20);
	doc.setFontStyle("bold");
	doc.text('<%=title2%>',110,60, 'center');
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,110,70, 'center');
	doc.setFontStyle("normal");
	/* **** */
	
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>NET DEBIT</th><th>NET CREDIT</th></tr></thead><tbody>";
	var rows = [];
	var groups_1=<%=plant %>table.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var netdebit=element.getData().net_debit.toFixed(numberOfDecimal);
			var netcredit=element.getData().net_credit.toFixed(numberOfDecimal);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+netdebit+"</td><td>"+netcredit+"</td></tr>";
	    }); 
	});
	htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+<%=plant%>totaldebit+"</td><td class='calculated'>"+<%=plant%>totalcredit+"</td></tr>";
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'right'},2:{halign : 'right'}},
		theme : 'plain',
		headStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		didParseCell: function (data) {
			if (data.column.index === 0) {
				data.cell.styles.halign = 'left';
			}
			else{
				data.cell.styles.halign = 'right';
			}
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
					    }
					}
	<%}%>
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
	<%for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); 
		%>
	var <%=plant %>data = <%=plant %>table.getData();
	<%}%>
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	var totalPagesExp = "{total_pages_cont_string}";
	/* Top Right */
	var pnext = "0";
		<%for (ParentChildCmpDet plist : plantlist) {
					String plant=plist.getCHILD_PLANT(); 
					String name = plantMstDAO.getcmpyname(plant);
					%>
					var inc;
					for (inc = 0; inc < arr.length; ++inc) {
					    if(arr[inc] == '<%=plant %>'){
					 

					if(pnext != "0"){
						doc.addPage();
					}
					pnext="1";
	var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	//doc.setFontStyle("bold");
	doc.text('<%=name%>', 110, 50, 'center');
	doc.setFontSize(20);
	doc.setFontStyle("bold");
	doc.text('<%=title2%>',110,60, 'center');
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,110,70, 'center');
	doc.setFontStyle("normal");
	/* **** */
	
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>NET DEBIT</th><th>NET CREDIT</th></tr></thead><tbody>";
	var rows = [];
	var groups_1=<%=plant %>table.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var netdebit=element.getData().net_debit.toFixed(numberOfDecimal);
			var netcredit=element.getData().net_credit.toFixed(numberOfDecimal);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+netdebit+"</td><td>"+netcredit+"</td></tr>";
	    }); 
	});
	htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+<%=plant%>totaldebit+"</td><td class='calculated'>"+<%=plant%>totalcredit+"</td></tr>";
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'right'},2:{halign : 'right'}},
		theme : 'plain',
		headStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		didParseCell: function (data) {
			if (data.column.index === 0) {
				data.cell.styles.halign = 'left';
			}
			else{
				data.cell.styles.halign = 'right';
			}
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
					    }
					}
	<%}%>
	// Total page number plugin only available in jspdf v1.0+
	if (typeof doc.putTotalPages === 'function') {
		doc.putTotalPages(totalPagesExp);
	}
	
	doc.save('Trialbalance.pdf');
}
function generatePdfPrint(dataUrl){	
	<%for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); 
		%>
	var <%=plant %>data = <%=plant %>table.getData();
	<%}%>
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	var totalPagesExp = "{total_pages_cont_string}";
	/* Top Right */
	var pnext = "0";
		<%for (ParentChildCmpDet plist : plantlist) {
					String plant=plist.getCHILD_PLANT(); 
					String name = plantMstDAO.getcmpyname(plant);
					%>
					var inc;
					for (inc = 0; inc < arr.length; ++inc) {
					    if(arr[inc] == '<%=plant %>'){
					 

					if(pnext != "0"){
						doc.addPage();
					}
					pnext="1";
	var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	//doc.setFontStyle("bold");
	doc.text('<%=name%>', 110, 50, 'center');
	doc.setFontSize(20);
	doc.setFontStyle("bold");
	doc.text('<%=title2%>',110,60, 'center');
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,110,70, 'center');
	doc.setFontStyle("normal");
	/* **** */
	
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>NET DEBIT</th><th>NET CREDIT</th></tr></thead><tbody>";
	var rows = [];
	var groups_1=<%=plant %>table.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var netdebit=element.getData().net_debit.toFixed(numberOfDecimal);
			var netcredit=element.getData().net_credit.toFixed(numberOfDecimal);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+netdebit+"</td><td>"+netcredit+"</td></tr>";
	    }); 
	});
	htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+<%=plant%>totaldebit+"</td><td class='calculated'>"+<%=plant%>totalcredit+"</td></tr>";
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'right'},2:{halign : 'right'}},
		theme : 'plain',
		headStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		didParseCell: function (data) {
			if (data.column.index === 0) {
				data.cell.styles.halign = 'left';
			}
			else{
				data.cell.styles.halign = 'right';
			}
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
					    }
					}
	<%}%>
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
		var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
		$('.asof').html(asofdate);
		<%for (ParentChildCmpDet plist : plantlist) {
			String plant = plist.getCHILD_PLANT();%>
			<%=plant%>loadJournalData(fromDateFormatted,toDateFormatted);
		<%}%>
		$('#asofDate').val(asofdate);
	}
	updateSelectedCompaniesInLocalStorage('parenttrialbalance_companieslist');
}
<%for (ParentChildCmpDet plist : plantlist) {
	String plant = plist.getCHILD_PLANT();%>
$('#<%=plant%>drop').change(function() {
	var ch = $(this).val();  
if(this.checked) {
	 arr.push('<%=plant%>');
}else{
	arr = jQuery.grep(arr, function(value) {
		  return value != '<%=plant%>';
		});
}
$('#plantlist').val(arr);
});
<%}%>
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

