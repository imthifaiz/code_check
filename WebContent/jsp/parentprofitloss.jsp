<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.StrUtils"%>
<%@ page import="com.track.db.object.ParentChildCmpDet"%>
<%@ page import="com.track.constants.*"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Consolidated Profit and Loss";
	String title2= "Profit and Loss";
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
	<!-- Thanzith Modified on 26.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                <li><label>Consolidated Profit and Loss</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 26.02.2022 --> 

		<div class="box-header menu-drop">


			<div class="row">
				<div class="col-sm-9">
					<div>Report period</div>
					<div class="row">
						<div class="col-sm-3">
							<select class="form-control" id="reportperiod"
								onchange="pldatechanged(this);">
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
							<INPUT class="form-control datepicker" id="fromdate" type="TEXT"
								placeholder="FROM DATE" autocomplete="off" readonly size="30"
								MAXLENGTH="10" name="fromdate" disabled />
						</div>

						<div class="col-sm-2" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" id="todate" type="TEXT"
								placeholder="TO DATE" autocomplete="off" readonly size="30"
								MAXLENGTH="10" name="todate" disabled />
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
									<li><input type="checkbox" id="<%=plant%>drop" class="companyname"
										value="<%=plant%>" checked />&nbsp;<%=name%></li>
									<%} %>

								</ul>
							</div>
						</div>

						<div class="col-sm-2" style="vertical-align: baseline;">
							<button class="btn btn-success" id="srbtn"
								onclick="Searchcustom();" type="button">Run</button>
						</div>
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

								<button type="button" id="exceldownload" class="btn btn-default"
									data-toggle="tooltip" data-placement="bottom" title="Excel">

									<a href="javascript:downTBExcel()" id="tbsexcel"
										style="text-decoration: none; color: black"><i
										class="fa fa-file-excel-o" aria-hidden="true"></i></a>
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
		<br> <br>
		<div id="print_id">
			<input type="hidden" name="plantlist" id="plantlist" value="" />
			<div class="containerx">
				<%for (ParentChildCmpDet plist : plantlist) {
					String plant=plist.getCHILD_PLANT(); 
					String name = plantMstDAO.getcmpyname(plant);
					%>
				<div class="item" id="<%=plant %>tab">
					<div class="row">
						<div class="col-sm-4"></div>
						<div class="col-sm-4" style="text-align: center">
							<div style="font-size: 14px"><%=name%></div>
							<div style="font-size: 16px; font-weight: 600;"><%=title2%></div>
							<div style="font-size: 14px">
								As of <span class="asof"></span>
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
								<div id="<%=plant %>profitloss-table_1" style="width: 514px"></div>
								<div id="<%=plant %>profitloss-table_2" style="width: 514px"></div>
								
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

#red {
	color: red;
}
</style>
<script>
var fromDateFormatted;
var toDateFormatted;
var period;
var arr = new Array();
<%for (ParentChildCmpDet plist : plantlist) {
	String plant=plist.getCHILD_PLANT(); %>
	arr.push('<%=plant %>');
<%}%>
function pldatechanged(node){
	$('#fromdate').prop('disabled', true);
	$('#todate').prop('disabled', true);
	$('#srbtn').hide();
	$('#plantlist').val(arr);
	period=node.value;
	sessionStorage.setItem('period', period);
	updateSelectedCompaniesInLocalStorage('parentprofitloss_companieslist');
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
		var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
		$('.asof').html(asofdate);
		<%for (ParentChildCmpDet plist : plantlist) {
			String plant=plist.getCHILD_PLANT(); 
			%>
			<%=plant %>loadPLSection1Data(fromDateFormatted,toDateFormatted);
			<%=plant %>loadPLSection2Data(fromDateFormatted,toDateFormatted);
		<%}%>
		$('#asofDate').val(asofdate);
	}
}
var numberOfDecimal = $("#numberOfDecimal").val();
<%for (ParentChildCmpDet plist : plantlist) {
	String plant=plist.getCHILD_PLANT(); 
	%>
	var <%=plant %>tablePLSummary;
	var <%=plant %>tableDataNested1;
	var <%=plant %>tableDataNested2;
<%}%>
function postDatePickerInit(){
	if (period == 'CUSTOM'){
		var parentprofitloss_fromdate = getLocalStorageValue('parentprofitloss_fromdate', '', 'fromdate');
		$('#fromdate').datepicker("setDate", 
		        moment(parentprofitloss_fromdate, 'DD/MM/YYYY').toDate());
		var parentprofitloss_todate = getLocalStorageValue('parentprofitloss_todate', '', 'todate');
		$('#todate').datepicker("setDate", 
				moment(parentprofitloss_todate, 'DD/MM/YYYY').toDate());
	}		
	$('#srbtn').click();
}
	$(document).ready(function() {
		var periodFromSession = getLocalStorageValue('parentprofitloss_reportperiod', 'TODAY', 'reportperiod');
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
			        updateSelectedCompaniesInLocalStorage('parentprofitloss_companieslist');   
			    });
		<%}%>
		var selectedCompanies = getLocalStorageValue('parentprofitloss_companieslist', '', '');
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
		// period = "TODAY";
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
		}


		if(period != "CUSTOM"){
			$('#fromdate').val(from);
			$('#todate').val(to);
			$('#reportperiod').val(period);
	
			//fromDateFormatted = "2020-01-02";
			//toDateFormatted = "2020-12-31";
			
			var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
			$('.asof').html(asofdate);
			<%for (ParentChildCmpDet plist : plantlist) {
				String plant=plist.getCHILD_PLANT(); 
				%>
			<%=plant %>loadPLSection1Data(fromDateFormatted,toDateFormatted);
			<%=plant %>loadPLSection2Data(fromDateFormatted,toDateFormatted);
			<%}%>
			$('#asofDate').val(asofdate);
		}
					});

	

	<%for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); 
		%>
	
	function <%=plant %>loadPLSection1Data(from,to)
	{
		storeInLocalStorage('parentprofitloss_reportperiod', $('#reportperiod').val());
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('parentprofitloss_fromdate', $('#fromdate').val());
		}
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('parentprofitloss_todate', $('#todate').val());
		}
		var acctypes= "('10','8')";
		$.ajax({
			type : "POST",
			url: '/track/ParentProfitLossServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getProfitLoss",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes,
				plant:"<%=plant %>"
			},
			success : function(profitlosslist) {
				var profitLossArray=profitlosslist;
				var myJSON = JSON.stringify(profitLossArray);
				//alert(myJSON);
				<%=plant %>tableDataNested1=profitLossArray;
				<%=plant %>loadPL_1();
			}
		});
	}
	function <%=plant %>loadPLSection2Data(from,to)
	{
		var acctypes= "('9','11')";
		$.ajax({
			type : "POST",
			url: '/track/ParentProfitLossServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getProfitLoss",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes,
				plant:"<%=plant %>"
			},
			success : function(profitlosslist) {
				var profitLossArray=profitlosslist;
				var myJSON = JSON.stringify(profitLossArray);
				//alert(myJSON);
				<%=plant %>tableDataNested2=profitLossArray;
				<%=plant %>loadPL_2();
			}
		});
	}

	var <%=plant %>table_1;
	var <%=plant %>table_2;
	var <%=plant %>grossGlobal=0.00;
	
	function <%=plant %>loadPL_1()
	{
		var <%=plant %>node1;
		<%=plant %>table_1 = new Tabulator("#<%=plant %>profitloss-table_1", {
				 downloadConfig:{
				        columnHeaders:true, //do not include column headers in downloaded table
				        columnGroups:true, //do not include column groups in column headers for downloaded table
				        rowGroups:true, //do not include row groups in downloaded table
				        columnCalcs:true, //do not include column calcs in downloaded table
				        dataTree:true, //do not include data tree in downloaded table
				    },
				    downloadRowRange:"all",
		    data:<%=plant %>tableDataNested1,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		    ],
 		     groupBy:["account_type"],
 		    groupStartOpen:false,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		        });
 		        return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal)+"</a>";
 		    },
 		   footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Gross Profit:</div><div class='pull-right' style='margin-right:10px' id='<%=plant %>grossprofit'></div></div>",
		    columns:[
		    {title:"ACCOUNT", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	return '<a onclick="<%=plant %>callDetails(\''+data.account_id+'\')">'+value+'</a>';
	    		
	    }},	
		  
		    {title:"Total", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
		    ],
		});
			 
		<%=plant %>grossprofit();	 
	}
	function <%=plant %>loadPL_2()
	{
		var <%=plant %>node2;
		<%=plant %>table_2 = new Tabulator("#<%=plant %>profitloss-table_2", {
		    data:<%=plant %>tableDataNested2,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		    ],
		    headerVisible:false, //hide header
 		     groupBy:["account_type"],
 		    groupStartOpen:false,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		        });
 		    	return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal)+"</a>";
 		    },
 		   footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Net Profit:</div><div class='pull-right' style='margin-right:10px' id='<%=plant %>netprofit'></div></div>",
		    columns:[
		    {title:"ACCOUNT", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	return '<a onclick="<%=plant %>callDetails(\''+data.account_id+'\')">'+value+'</a>';
	    		
	    }},	
		  
		    {title:"Total", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    }},
		    ],
		});
			 
		<%=plant %>netprofit();	 
	}
	
	function <%=plant %>grossprofit(){
		var data = <%=plant %>table_1.getData();
		var gross=0.00;
		var totalSales=0.00;
		var totalCostofsales=0.00;
		data.forEach(function(item){
			var accounttype=item.account_type;
			accounttype=accounttype.trim();
			if(accounttype=="Sales")
				{
					totalSales+=item.total;
				}
			else
				{
					totalCostofsales+=item.total;
				}
				
	        });
		gross=totalSales-totalCostofsales;
		<%=plant %>grossGlobal=gross;
		//console.log("g1-------"+grossGlobal);
		gross=gross.toFixed(numberOfDecimal);
		$('#<%=plant %>grossprofit').html(gross);
		<%=plant %>netprofit();
	}
	
	function <%=plant %>netprofit(){
		var data = <%=plant %>table_2.getData();
		var net=0.00;
		var totalIncome=0.00;
		var totalExpense=0.00;
		data.forEach(function(item){
			var accounttype=item.account_type;
			accounttype=accounttype.trim();
			if(accounttype=="Income")
				{
					totalIncome+=item.total;
				}
			else
				{
					totalExpense+=item.total;
				}
			
	        });
		net= totalIncome-totalExpense;
		//console.log("g2-------"+grossGlobal);
		net=<%=plant %>grossGlobal+net;
		net=net.toFixed(numberOfDecimal);
		$('#<%=plant %>netprofit').html(net);
	}
	
	function <%=plant %>callDetails(account,plant)
	{
		//alert("Called");
		window.location.href="../businessoverview/parentprofitlossdetail?account="+account+"&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"&plant=<%=plant %>";
	}
	
	<%}%>
	
	
	
	
	$("#pdfdownload").click(function(){
		generate();
	});
	$("#sendemail").click(function(){
		//$("#common_email_modal #send_subject).val("Your Profit Loss Report");
		$('#common_email_modal').modal('toggle');
		loadBodyStyle();
	});


	
function generatePdf(dataUrl){
	<%for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); %>
	var <%=plant%>data = <%=plant%>table_1.getData();
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
	doc.text('<%=name%>', 110, 50, 'center');
	doc.setFontSize(20);
	doc.text('<%=title2%>',110,60, 'center');
	doc.setFontSize(12);
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,110,70, 'center');
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);
	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	/* **** */

	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=<%=plant%>table_1.getGroups();
	var groups_2=<%=plant%>table_2.getGroups();
	
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
	var grossprofit=$('#<%=plant%>grossprofit').html();
	htmlTable+="<tr><td class='calculated'>Gross Profit:</td><td class='calculated'>"+grossprofit+"</td></tr>";
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
	var netprofit=$('#<%=plant%>netprofit').html();
	htmlTable+="<tr><td class='calculated'>Net Profit:</td><td class='calculated'>"+netprofit+"</td></tr>";
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'right'}},
		theme : 'plain',
		headStyles: {0 : {halign : 'left'},1 : {halign : 'right'}},
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
	
	

					    }
					}
		<%}%>
		
	
	// Total page number plugin only available in jspdf v1.0+
	if (typeof doc.putTotalPages === 'function') {
		doc.putTotalPages(totalPagesExp);
	}
	
	  
		 
	  doc.save('Profitloss.pdf')
}
function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
	}
function generatePdfMail(dataUrl,attachName){	
		<%for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); %>
	var <%=plant%>data = <%=plant%>table_1.getData();
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
	doc.text('<%=name%>', 110, 50, 'center');
	doc.setFontSize(20);
	doc.text('<%=title2%>',110,60, 'center');
	doc.setFontSize(12);
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,110,70, 'center');
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);
	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	/* **** */

	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=<%=plant%>table_1.getGroups();
	var groups_2=<%=plant%>table_2.getGroups();
	
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
	var grossprofit=$('#<%=plant%>grossprofit').html();
	htmlTable+="<tr><td class='calculated'>Gross Profit:</td><td class='calculated'>"+grossprofit+"</td></tr>";
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
	var netprofit=$('#<%=plant%>netprofit').html();
	htmlTable+="<tr><td class='calculated'>Net Profit:</td><td class='calculated'>"+netprofit+"</td></tr>";
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'right'}},
		theme : 'plain',
		headStyles: {0 : {halign : 'left'},1 : {halign : 'right'}},
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
	  //return formData;
	  
}

function generatePdfPrint(dataUrl){	
	<%for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); %>
	var <%=plant%>data = <%=plant%>table_1.getData();
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
	doc.text('<%=name%>', 110, 50, 'center');
	doc.setFontSize(20);
	doc.text('<%=title2%>',110,60, 'center');
	doc.setFontSize(12);
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,110,70, 'center');
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);
	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	/* **** */

	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=<%=plant%>table_1.getGroups();
	var groups_2=<%=plant%>table_2.getGroups();
	
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
	var grossprofit=$('#<%=plant%>grossprofit').html();
	htmlTable+="<tr><td class='calculated'>Gross Profit:</td><td class='calculated'>"+grossprofit+"</td></tr>";
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
	var netprofit=$('#<%=plant%>netprofit').html();
	htmlTable+="<tr><td class='calculated'>Net Profit:</td><td class='calculated'>"+netprofit+"</td></tr>";
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'right'}},
		theme : 'plain',
		headStyles: {0 : {halign : 'left'},1 : {halign : 'right'}},
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
			String plant=plist.getCHILD_PLANT(); 
			%>
			<%=plant %>loadPLSection1Data(fromDateFormatted,toDateFormatted);
			<%=plant %>loadPLSection2Data(fromDateFormatted,toDateFormatted);
			<%}%>
		$('#asofDate').val(asofdate);
	}
	updateSelectedCompaniesInLocalStorage('parentprofitloss_companieslist');
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

function downTBExcel()
{
	var plist = $('input[name ="plantlist"]').val();
	window.location = "/track/ParentProfitLossServlet?action=getProfitLossExcel&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"&plist="+plist;
}
<%}%>
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
