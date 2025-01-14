<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.StrUtils"%>
<%@ page import="com.track.constants.*"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Profit and Loss";
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
                <li><label>Profit and Loss</label></li>                                   
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
						<div class="col-sm-3" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" id="fromdate" type="TEXT" placeholder="FROM DATE" autocomplete="off" readonly
								size="30" MAXLENGTH="10" name="fromdate" disabled />
						</div>

						<div class="col-sm-3" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" id="todate" type="TEXT" placeholder="TO DATE" autocomplete="off" readonly
								size="30" MAXLENGTH="10" name="todate" disabled />
						</div>
						
						<div class="col-sm-3" style="vertical-align: baseline;"><button class="btn btn-success" id="srbtn" onclick="Searchcustom();" type="button">Run</button></div>
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
		<br> <br> <div id="print_id">
			<div class="row">
				<div class="col-sm-4"></div>
				<div class="col-sm-4" style="text-align: center">
					<div style="font-size: 18px"><%=PLNTDESC%></div>
					<div style="font-size: 28px"><%=title%></div>
					<div style="font-size: 18px">
						As of <span id="asof"></span>
					</div>
				</div>
				<div class="col-sm-4"></div>
			</div> <input type="number" id="numberOfDecimal" style="display: none;"
			value=<%=numberOfDecimal%>> <input type="text"
			id="fiscalyear" style="display: none;" value=<%=fiscalyear%>>

			<div class="box-body">
				<div class="row">
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<div id="profitloss-table_1" style="width: 514px"></div>
						<div id="profitloss-table_2" style="width: 514px"></div>
					</div>
					<div class="col-sm-3"></div>
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
<script type="text/javascript">
var fromDateFormatted;
var toDateFormatted;
var period;
function pldatechanged(node){
	$('#fromdate').prop('disabled', true);
	$('#todate').prop('disabled', true);
	$('#srbtn').hide();
	period=node.value;
	sessionStorage.setItem('period', period);
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
		$('#asof').html(asofdate);
		loadPLSection1Data(fromDateFormatted,toDateFormatted);
		loadPLSection2Data(fromDateFormatted,toDateFormatted);
		$('#asofDate').val(asofdate);
	}
}
var tablePLSummary;
var numberOfDecimal = $("#numberOfDecimal").val();
var tableDataNested1;
var tableDataNested2;
function postDatePickerInit(){
	if (period == 'CUSTOM'){
		var profitloss_fromdate = getLocalStorageValue('profitloss_fromdate', '', 'fromdate');
		$('#fromdate').datepicker("setDate", 
		        moment(profitloss_fromdate, 'DD/MM/YYYY').toDate());
		var profitloss_todate = getLocalStorageValue('profitloss_todate', '', 'todate');
		$('#todate').datepicker("setDate", 
				moment(profitloss_todate, 'DD/MM/YYYY').toDate());
	}		
	$('#srbtn').click();
}

	$(document).ready(function() {
		var periodFromSession = getLocalStorageValue('profitloss_reportperiod', 'TODAY', 'reportperiod');
		period = periodFromSession;
		var to;
		var from;
		$('#fromdate').prop('disabled', true);
		$('#todate').prop('disabled', true);
		$('#srbtn').hide();
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
		 //period = "TODAY";
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
			$('#asof').html(asofdate);
			loadPLSection1Data(fromDateFormatted,toDateFormatted);
			loadPLSection2Data(fromDateFormatted,toDateFormatted);
			$('#asofDate').val(asofdate);
		}
					});

	function downTBExcel()
	{
		window.location = "/track/ProfitLossServlet?action=getProfitLossExcel&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted;
	}

	
	function loadPLSection1Data(from,to)
	{
		storeInLocalStorage('profitloss_reportperiod', $('#reportperiod').val());
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('profitloss_fromdate', $('#fromdate').val());
		}
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('profitloss_todate', $('#todate').val());
		}
		var acctypes= "('10','8','9')";
		$.ajax({
			type : "POST",
			url: '/track/ProfitLossServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getProfitLoss",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes
			},
			success : function(profitlosslist) {
				var profitLossArray=profitlosslist;
				var myJSON = JSON.stringify(profitLossArray);
				console.log("myjason-----"+myJSON);
				tableDataNested1=profitLossArray;
				loadPL_1();
			}
		});
	}
	function loadPLSection2Data(from,to)
	{
		var acctypes= "('11')";
		$.ajax({
			type : "POST",
			url: '/track/ProfitLossServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getProfitLoss",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes
			},
			success : function(profitlosslist) {
				var profitLossArray=profitlosslist;
				var myJSON = JSON.stringify(profitLossArray);
				//alert(myJSON);
				tableDataNested2=profitLossArray;
				loadPL_2();
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
	
	var adultCalc = function(values, data, calcParams){
	    //values - array of column values
	    //data - all table data
	    //calcParams - params passed from the column definition object

	    var calc = 0;


	    return calc;
	}
	
	var table_1;
	var table_2;
	var grossGlobal=0.00;
	function loadPL_1()
	{
		var node1;
			table_1 = new Tabulator("#profitloss-table_1", {
				 downloadConfig:{
				        columnHeaders:true, //do not include column headers in downloaded table
				        columnGroups:true, //do not include column groups in column headers for downloaded table
				        rowGroups:true, //do not include row groups in downloaded table
				        columnCalcs:true, //do not include column calcs in downloaded table
				        dataTree:true, //do not include data tree in downloaded table
				    },
				    downloadRowRange:"all",
		    data:tableDataNested1,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		        {column:"account_name", dir:"asc"},
		    ],
 		     groupBy:["account_type"],
 		    groupStartOpen:true,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	var actype="";
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		    		actype = row.getData().account_type;
 		        });
 		    	if(actype.trim() == "Cost of sales"){
 		    		TotalValue = -1 * TotalValue;
 		    	}
 		        return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</a>";
 		    },
 		   footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Gross Profit:</div><div class='pull-right' style='margin-right:10px' id='grossprofit'></div></div>",
		    columns:[
		    {title:"ACCOUNT", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
	    		
	    },bottomCalc:adultCalc,bottomCalcFormatter:function(cell, formatterParams, onRendered){
   		 var cellElement = cell.getElement();
		 return "<span>Total</span>";
	    }},	
		  
		    {title:"Total", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()},bottomCalc:"sum", bottomCalcParams:{precision:numberOfDecimal}
	    },
		    ],
		});
			 
			grossprofit();	 
	}
	function loadPL_2()
	{
		var node2;
		table_2 = new Tabulator("#profitloss-table_2", {
		    data:tableDataNested2,
		    layout:"fitColumns",
		    initialSort:[
		        {column:"account_type", dir:"desc"},
		        {column:"account_name", dir:"asc"},
		    ],
		    headerVisible:false, //hide header
 		     groupBy:["account_type"],
 		    groupStartOpen:true,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	var actype="";
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		    		actype = row.getData().account_type;
 		        });

 		    	if(actype.trim() == "Expenses"){
 		    		TotalValue = -1 * TotalValue;
 		    	}
 		    	return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</a>";
 		    },
 		   footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Net Profit:</div><div class='pull-right' style='margin-right:10px' id='netprofit'></div></div>",
		    columns:[
		    {title:"ACCOUNT", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
	    		
	    },bottomCalc:adultCalc,bottomCalcFormatter:function(cell, formatterParams, onRendered){
	   		 var cellElement = cell.getElement();
			 return "<span>Total</span>";
		    }},	
		  
		    {title:"Total", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
	    		
	    },bottomCalc:"sum", bottomCalcParams:{precision:numberOfDecimal}},
		    ],
		});
			 
			netprofit();	 
	}
	
	
	
	function grossprofit(){
		var data = table_1.getData();
		var gross=0.00;
		var totalSales=0.00;
		var totalCostofsales=0.00;
		var totalIncome=0.00;
		data.forEach(function(item){
			var accounttype=item.account_type;
			accounttype=accounttype.trim();
			if(accounttype=="Sales")
				{
					totalSales+=item.total;
				}
			else if(accounttype=="Income")
			{
				totalIncome+=item.total;
			}
			else
				{
					totalCostofsales+=item.total;
				}
				
	        });
		gross=(totalSales+totalIncome)-totalCostofsales;
		grossGlobal=gross;
		//console.log("g1-------"+grossGlobal);
		gross=gross.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
		$('#grossprofit').html(gross);
		netprofit();
	}
	
	function netprofit(){
		var data = table_2.getData();
		var net=0.00;
		//var totalIncome=0.00;
		var totalExpense=0.00;
		data.forEach(function(item){
			var accounttype=item.account_type;
			accounttype=accounttype.trim();
			/* if(accounttype=="Income")
				{
					totalIncome+=item.total;
				}
			else
				{ */
					totalExpense+=item.total;
				/* } */
			
	        });
		net= totalExpense;
		//console.log("g2-------"+grossGlobal);
		net=grossGlobal-net;
		net=net.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
		$('#netprofit').html(net);
	}
	$("#pdfdownload").click(function(){
		generate();
	});
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
	window.location.href="../businessoverview/profitlossdetail?account="+account+"&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"";
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
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=table_1.getGroups();
	var groups_2=table_2.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		
		var headertotaoal = 0;
		var footertotal=0;
		
		grouprows.forEach(element => {    
			footertotal= parseFloat(footertotal) + parseFloat(element.getData().total);
			footertotal = parseFloat(footertotal).toFixed(numberOfDecimal);
	    });
		
		if(key.trim() == "Cost of sales"){
			headertotaoal = -1 * footertotal;
		}else{
			headertotaoal = footertotal;
		}
		
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+headertotaoal+"</td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	    }); 
		
		htmlTable+="<tr><td  class='calculated'>Total</td><td  class='calculated'>"+footertotal+"</td></tr>";
	});
	var grossprofit=$('#grossprofit').html();
	htmlTable+="<tr><td class='calculated'>Gross Profit:</td><td class='calculated'>"+grossprofit+"</td></tr>";
	groups_2.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		var headertotaoal = 0;
		var footertotal=0;
		
		grouprows.forEach(element => {    
			//footertotal += element.getData().total.toFixed(numberOfDecimal);
			footertotal = parseFloat(footertotal) + parseFloat(element.getData().total);
			footertotal = parseFloat(footertotal).toFixed(numberOfDecimal);
	    });
		
		if(key.trim() == "Expenses"){
			headertotaoal = -1 * footertotal;
		}else{
			headertotaoal = footertotal;
		}
		
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+headertotaoal+"</td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	        

	    }); 
		
		htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+footertotal+"</td></tr>";
	});
	var netprofit=$('#netprofit').html();
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
	//doc.setFontStyle("bold");
	doc.text('<%=PLNTDESC%>', cwidth, 50,'center');
	doc.setFontSize(20);
	doc.setFontStyle("bold");
	doc.text('<%=title%>',cwidth,60,'center');
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,cwidth,70,'center');
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=table_1.getGroups();
	var groups_2=table_2.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		var headertotaoal = 0;
		var footertotal=0;
		
		grouprows.forEach(element => {    
			//footertotal += element.getData().total.toFixed(numberOfDecimal);
			footertotal = parseFloat(footertotal) + parseFloat(element.getData().total);
			footertotal = parseFloat(footertotal).toFixed(numberOfDecimal);
	    });
		
		if(key.trim() == "Cost of sales"){
			headertotaoal = -1 * footertotal;
		}else{
			headertotaoal = footertotal;
		}
		
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+headertotaoal+"</td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	        

	    }); 
		
		htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+footertotal+"</td></tr>";
	});
	var grossprofit=$('#grossprofit').html();
	htmlTable+="<tr><td class='calculated'>Gross Profit:</td><td class='calculated'>"+grossprofit+"</td></tr>";
	groups_2.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		var headertotaoal = 0;
		var footertotal=0;
		
		grouprows.forEach(element => {    
			//footertotal += element.getData().total.toFixed(numberOfDecimal);
			footertotal = parseFloat(footertotal) + parseFloat(element.getData().total);
			footertotal = parseFloat(footertotal).toFixed(numberOfDecimal);
	    });
		
		if(key.trim() == "Expenses"){
			headertotaoal = -1 * footertotal;
		}else{
			headertotaoal = footertotal;
		}
		
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+headertotaoal+"</td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	        

	    }); 
		
		htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+footertotal+"</td></tr>";
	});
	var netprofit=$('#netprofit').html();
	htmlTable+="<tr><td class='calculated'>Net Profit:</td><td class='calculated'>"+netprofit+"</td></tr>";
	htmlTable+="</tbody>";
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'right'}},
		theme : 'plain',
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
	  //return formData;
	  
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
	//doc.setFontStyle("bold");
	doc.text('<%=PLNTDESC%>',cwidth, 50,'center');
	doc.setFontSize(20);
	doc.setFontStyle("bold");
	doc.text('<%=title%>',cwidth,60,'center');
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,cwidth,70,'center');
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=table_1.getGroups();
	var groups_2=table_2.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		var headertotaoal = 0;
		var footertotal=0;
		
		grouprows.forEach(element => {    
			//footertotal += element.getData().total.toFixed(numberOfDecimal);
			footertotal = parseFloat(footertotal) + parseFloat(element.getData().total);
			footertotal = parseFloat(footertotal).toFixed(numberOfDecimal);
	    });
		
		if(key.trim() == "Cost of sales"){
			headertotaoal = -1 * footertotal;
		}else{
			headertotaoal = footertotal;
		}
		
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+headertotaoal+"</td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	        

	    }); 
		
		htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+footertotal+"</td></tr>";

	});
	var grossprofit=$('#grossprofit').html();
	htmlTable+="<tr><td class='calculated'>Gross Profit:</td><td class='calculated'>"+grossprofit+"</td></tr>";
	groups_2.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		var headertotaoal = 0;
		var footertotal=0;
		
		grouprows.forEach(element => {    
			//footertotal += element.getData().total.toFixed(numberOfDecimal);
			footertotal = parseFloat(footertotal) + parseFloat(element.getData().total);
			footertotal = parseFloat(footertotal).toFixed(numberOfDecimal);
	    });
		
		if(key.trim() == "Expenses"){
			headertotaoal = -1 * footertotal;
		}else{
			headertotaoal = footertotal;
		}
		
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+headertotaoal+"</td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	        

	    }); 
		
		htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+footertotal+"</td></tr>";

	});
	var netprofit=$('#netprofit').html();
	htmlTable+="<tr><td class='calculated'>Net Profit:</td><td class='calculated'>"+netprofit+"</td></tr>";
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
			textColor : [ 0, 0, 0 ],
			
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
		var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
		$('#asof').html(asofdate);
		loadPLSection1Data(fromDateFormatted,toDateFormatted);
		loadPLSection2Data(fromDateFormatted,toDateFormatted);
		$('#asofDate').val(asofdate);
	}
}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

