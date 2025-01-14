<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.StrUtils"%>
<%@ page import="com.track.constants.*"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Balance Sheet";
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
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="submenu" value="<%=IConstants.ACCOUNTING_SUB_MENU%>"/>
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
                <li><label>Balance Sheet</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 26.02.2022 --> 
		<div class="box-header menu-drop">
		<div class="row">
				<div class="col-sm-9">
				<div>Report period</div>
		
				
					<div class="row">
						<div class="col-sm-3">
							<select class="form-control" id="reportperiod" onchange="pldatechanged(this);">
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
							<INPUT class="form-control datepicker" autocomplete="off"
								id="fromdate" type="TEXT" size="30" MAXLENGTH="10"
								name="fromdate" placeholder="FROM DATE" readonly disabled />
						</div>

						<div class="col-sm-3" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" autocomplete="off"
								id="todate" type="TEXT" size="30" MAXLENGTH="10" name="todate"
								placeholder="TO DATE" readonly disabled />
						</div>

						<div class="col-sm-3" style="vertical-align: baseline;">
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
								data-toggle="tooltip"  data-placement="bottom" title="Email">
									<i class="fa fa-envelope-o" aria-hidden="true"></i>
								</button>
								<button type="button" id="pdfdownload" class="btn btn-default"
								data-toggle="tooltip"  data-placement="bottom" title="PDF">
									<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
								</button>
								
								<button type="button" id="exceldownload" class="btn btn-default" data-toggle="tooltip" data-placement="bottom" title="Excel">
									
									<a href="javascript:downTBExcel()" id="tbsexcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>
									<!-- <a href="/track/TrialBalanceServlet?action=getTrialBalanceAsExcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>-->
								</button>
								
								<button type="button" class="btn btn-default printMe" 
								 data-toggle="tooltip"  data-placement="bottom" title="Print">
									<i class="fa fa-print" aria-hidden="true"></i>
								</button>
								&nbsp;
								 <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../accounting/reports'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
								
							</div>
						</div>
					</div>
				</div>
				</div>
			</div>
			<br>
			<br>
			<div id="print_id">
			<div class="row">
				<div class="col-sm-4">
				</div>
				<div class="col-sm-4" style="text-align:center">
					<div style="font-size:18px"><%=PLNTDESC %></div>
					<div style="font-size:28px"><%=title%></div>
					<div style="font-size:18px">As of <span id="asof"></span></div>
				</div>
				<div class="col-sm-4">
				</div>
			</div>
				
			<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
			<input type="text" id="fiscalyear" style="display:none;" value=<%=fiscalyear%>>
		<div class="box-body">
			<div class="row">
				<div class="col-sm-2">
					
				</div>
				<div class="col-sm-8">
					<div id="balancesheet-table_1" style="width:714px"></div>
					<div id="balancesheet-table_2" style="width:714px"></div>
					<div id="balancesheet-table_3" style="width:714px"></div>
				</div>
				<div class="col-sm-2">
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
.tabulator-arrow {
display: none!important;
}
</style>
<script>
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
		//profitloss(fromDateFormatted,toDateFormatted);
		loadPLSection1Data(fromDateFormatted,toDateFormatted);
		loadPLSection2Data(fromDateFormatted,toDateFormatted);
		loadPLSection3Data(fromDateFormatted,toDateFormatted);
		$('#asofDate').val(asofdate);
	}
}


var tablePLSummary;
var numberOfDecimal = $("#numberOfDecimal").val();
var tableDataNested1;
var tableDataNested2;
var tableDataNested3;
var profitLossGlobal=0.00;
var totallbt=0.00;
var totaleqt=0.00;
function postDatePickerInit(){
	if (period == 'CUSTOM'){
		var balancesheet_fromdate = getLocalStorageValue('balancesheet_fromdate', '', 'fromdate');
		$('#fromdate').datepicker("setDate", 
		        moment(balancesheet_fromdate, 'DD/MM/YYYY').toDate());
		var balancesheet_todate = getLocalStorageValue('balancesheet_todate', '', 'todate');
		$('#todate').datepicker("setDate", 
				moment(balancesheet_todate, 'DD/MM/YYYY').toDate());
	}		
	$('#srbtn').click();
}

	$(document).ready(function() {
		var periodFromSession = getLocalStorageValue('balancesheet_reportperiod', 'TODAY', 'reportperiod');
		period = periodFromSession;
		var to;
		var from;
		$('#fromdate').prop('disabled', true);
		$('#todate').prop('disabled', true);
		$('#srbtn').hide();
		if (typeof(Storage) !== "undefined") {
			//periodFromSession = sessionStorage.getItem('period');
			if(periodFromSession===null || !periodFromSession || periodFromSession=="null")
				{
					period="TODAY";
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
	
			//fromDateFormatted = "2020-01-02";
			//toDateFormatted = "2020-12-31";
			
			var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
			$('#asof').html(asofdate);
			
			loadPLSection1Data(fromDateFormatted,toDateFormatted);
			loadPLSection2Data(fromDateFormatted,toDateFormatted);
			loadPLSection3Data(fromDateFormatted,toDateFormatted);
			$('#asofDate').val(asofdate);
		}
	});
	
	function loadPLSection1Data(from,to)
	{
		storeInLocalStorage('balancesheet_reportperiod', $('#reportperiod').val());
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('balancesheet_fromdate', $('#fromdate').val());
		}
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('balancesheet_todate', $('#todate').val());
		}
		var acctypes= "('1','2','3')";
		$.ajax({
			type : "POST",
			url: '/track/BalanceSheetServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getBalanceSheetReport",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes
			},
			success : function(profitlosslist) {
				var profitLossArray=profitlosslist;
				var myJSON = JSON.stringify(profitLossArray);
				//alert(myJSON);
				tableDataNested1=profitLossArray;
				loadPL_1();
			}
		});
	}


	function downTBExcel()
	{
		//alert("coming");
		window.location = "/track/BalanceSheetServlet?action=getBalanceSheetExcel&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted;
	}
	
	function loadPLSection2Data(from,to)
	{
		var acctypes= "('4','5','6')";
		$.ajax({
			type : "POST",
			url: '/track/BalanceSheetServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getBalanceSheetReport2",
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
	function loadPLSection3Data(from,to)
	{
		var acctypes= "('7','12',13)";
		$.ajax({
			type : "POST",
			url: '/track/BalanceSheetServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getBalanceSheetReport2",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes
			},
			success : function(profitlosslist) {
				var profitLossArray=profitlosslist;
				var myJSON = JSON.stringify(profitLossArray);
				//alert(myJSON);
				tableDataNested3=profitLossArray;
				loadPL_3();
			}
		});
	}
	
	function profitloss(from,to)
	{
		$.ajax({
			type : "POST",
			url: '/track/BalanceSheetServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getBalanceSheetProfitLoss",
				fromDate:from,
				toDate:to
			},
			success : function(profitloss) {
				var currentearnings=profitloss.currentearnings;
					profitLossGlobal=parseFloat(profitloss.currentearnings);
					totalequity(profitLossGlobal);
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
	var assetGlobal=0.00;
	var liabilityGlobal=0.00;
	var equityGlobal=0.00;
	function loadPL_1()
	{
		var node1;
			table_1 = new Tabulator("#balancesheet-table_1", {
		    data:tableDataNested1,
		    dataTree:true,
		    dataTreeFilter:false,
		    dataTreeBranchElement:false,
		    dataTreeStartExpanded:true,
		    layout:"fitColumns",
 		    groupBy:["account_type"],
 		    groupStartOpen:[true],
 		  	groupToggleElement:false,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	var mainacc="";
 		    	var subacc="";
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		    		//mainacc = row.getData().account_name;
 		    		//subacc = row.getData().sub_account;
 		        });
 		    	
 		    	if(value == ""){
 		    		return value;
 		    	}else if(TotalValue == 0.00){
 		    		return value;
 		    	}else{
 		    		return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,")+"</a>";	
 		    	}
 		    	
 		    	//return value;
 		    },
 		   footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Total Assets:</div><div class='pull-right' style='margin-right:10px' id='totalasset'></div></div>",
		    columns:[
		    {title:"Assets", field:"account_name",responsive:0,headerSort:false, resizable:false,sortable:false,formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')" style="margin-left: 19%;">'+value+'</a>';
		    	if(data.account_id == "0"){
		    		return value;
		    	}else{
		    		return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	}
		    	
	    }},	
		  
		    {title:"Total", field:"total",align:"right",headerSort:false, resizable:false,sortable:false,formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	if(data.account_id == "0"){
		    		return "";
		    	}else{
		    		return value.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
		    	}
		    	
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
		    ],
		});
			 
			totalasset();	 
	}
	function loadPL_2()
	{
		var node2;
		table_2 = new Tabulator("#balancesheet-table_2", {
		    data:tableDataNested2,
		    layout:"fitColumns",
		    //headerVisible:false, //hide header
		    dataTree:true,
		    dataTreeFilter:false,
		    dataTreeBranchElement:false,
		    dataTreeStartExpanded:true,
		    layout:"fitColumns",
 		    groupBy:["account_type"],
 		    groupStartOpen:[true],
 		  	groupToggleElement:false,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	var mainacc="";
 		    	var subacc="";
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		    		//mainacc = row.getData().account_name;
 		    		//subacc = row.getData().sub_account;
 		        });
 		    	
 		    	//return value;
 		    	if(value == ""){
 		    		return value;
 		    	}else if(TotalValue == 0.00){
 		    		return value;
 		    	}else{
 		    		return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,")+"</a>";	
 		    	}
 		    },
 		   footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Total Liabilities:</div><div class='pull-right' style='margin-right:10px' id='totalliability'></div></div>",
		    columns:[
		    {title:"Liabilities", field:"account_name",responsive:0,headerSort:false, resizable:false,sortable:false,formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')" style="margin-left: 14%;">'+value+'</a>';
		    	if(data.account_id == "0"){
		    		return value;
		    	}else{
		    		return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	}
	    		
	    }},	
		  
		    {title:"", field:"total",align:"right",headerSort:false, resizable:false,sortable:false,formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	//return value.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
		    	var data=cell.getRow().getData();
		    	if(data.account_id == "0"){
		    		return "";
		    	}else{
		    		return value.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
		    	}
	    		
	    }},
		    ],
		});
			 
			totalliability();	 
	}
	function loadPL_3()
	{
		var node2;
		table_3 = new Tabulator("#balancesheet-table_3", {
		    data:tableDataNested3,
		    layout:"fitColumns",
		    //headerVisible:false, //hide header
		    dataTree:true,
		    dataTreeFilter:false,
		    dataTreeBranchElement:false,
		    dataTreeStartExpanded:true,
		    layout:"fitColumns",
 		    groupBy:["account_type"],
 		    groupStartOpen:[true],
 		  	groupToggleElement:false,
 		    groupHeader: function(value, count, data, group){
 		    	var rows = group.getRows();
 		    	var TotalValue=0.00;
 		    	var mainacc="";
 		    	var subacc="";
 		    	rows.forEach(function(row){
 		    		TotalValue+= row.getData().total;
 		    		//mainacc = row.getData().account_name;
 		    		//subacc = row.getData().sub_account;
 		        });
 		    	
 		    	//return value;
 		    	if(value == ""){
 		    		return value;
 		    	}else if(TotalValue == 0.00){
 		    		return value;
 		    	}else{
 		    		return value + "<a style='float:right;'>" + TotalValue.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,")+"</a>";	
 		    	}
 		    },
 		    footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Current Earnings</div><div class='pull-right' style='margin-right:10px' id='profitloss'></div></div><div class='row'><div class='pull-left' style='margin-left:30px'>Total Equity:</div><div class='pull-right' style='margin-right:10px' id='totalequity'></div></div><div class='row'><div class='pull-left' style='margin-left:30px'>Total :</div><div class='pull-right' style='margin-right:10px' id='totalvalue'></div></div>",
		    columns:[
		    {title:"Equity", field:"account_name",responsive:0,headerSort:false, resizable:false,sortable:false,formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	//return '<a onclick="callDetails(\''+data.account_id+'\')" style="margin-left: 14%;">'+value+'</a>';
		    	if(data.account_id == "0"){
		    		return value;
		    	}else{
		    		return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
		    	}
	    		
	    }},	
		  
		    {title:"", field:"total",align:"right",headerSort:false, resizable:false,sortable:false,formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	//return value.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
		    	var data=cell.getRow().getData();
		    	if(data.account_id == "0"){
		    		return "";
		    	}else{
		    		return value.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
		    	}
	    		
	    }},
		    ],
		});
			 
		profitloss(fromDateFormatted,toDateFormatted);
			 
	}
	function totalasset(){
		var data = table_1.getData();
		var asset=0.00;
		data.forEach(function(item){
				asset+= item.total;
	        });
		assetGlobal=asset;
		asset=asset.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
		$('#totalasset').html(asset);
	}
	
	function totalliability(){
		var data = table_2.getData();
		var liability=0.00;
		data.forEach(function(item){
			liability+= item.total;
	        });
		liabilityGlobal=liability;
		liability=liability.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
		//totalvalue = parseFloat(totalvalue) + parseFloat(liability);
		var totalvalue = parseFloat(liabilityGlobal) + parseFloat(equityGlobal);
		console.log("liabilityGlobal--"+liabilityGlobal);
		console.log("equityGlobal--"+equityGlobal);
		console.log("totalvalue--"+totalvalue);
		totalvalue = parseFloat(totalvalue).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
		$('#totalvalue').html(totalvalue);
		$('#totalliability').html(liability);
	}
	
	function totalequity(currEarnings){
		var data = table_3.getData();
		var equity=0.00;
		data.forEach(function(item){
			equity+= item.total;
	        });
		equity=currEarnings+equity;
		equityGlobal=equity;
		equity=equity.toFixed(numberOfDecimal);
		
		currEarnings=currEarnings.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,");
		//var currEarnings=profitloss(fromDateFormatted,toDateFormatted);
		var htmlCurrEar="<a onClick='redirectProfitLoss();'>"+currEarnings+"</a>";
		console.log("liabilityGlobal-----"+liabilityGlobal);
		console.log("equity-----"+equity);
		var totalvalue = parseFloat(liabilityGlobal) + parseFloat(equity);
		console.log("liabilityGlobal--"+liabilityGlobal);
		console.log("equity--"+equityGlobal);
		console.log("totalvalue--"+totalvalue);
		totalvalue = parseFloat(totalvalue).toFixed(numberOfDecimal);
		$('#profitloss').html(htmlCurrEar);
		$('#totalequity').html(parseFloat(equity).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,"));
		$('#totalvalue').html(parseFloat(totalvalue).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, "$&,"));
	}
	function redirectProfitLoss()
	{
		window.location.href="profitloss.jsp";
	}
	$("#pdfdownload").click(function(){
		generate();
	});
	
	$("#sendemail").click(function(){
		//$("#common_email_modal #send_subject).val("Your Balance Sheet Report");
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
	window.location.href="../businessoverview/balancesheetdetail?account="+account+"&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"";
}
	
	
function generatePdf(dataUrl){
	var data = table_1.getData();
	var json = JSON.stringify(data);
	console.log(table_1);
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
	
	var htmlTable="<thead><tr><th>Assets</th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_all=table_1;
	var groups_1=table_1.getGroups();
	var groups_2=table_2.getGroups();
	var groups_3=table_3.getGroups();
	groups_1.forEach(group =>{
			var grouprows=group.getRows();
			var key=group.getKey();
			var htmlkey="<b>"+key+"</b>"
			var groupname=[key,''];
			rows.push(groupname);
			
			var subtotal = 0.0;
			grouprows.forEach(element => { 				
				var total=element.getData().total.toFixed(numberOfDecimal);
				if(key != element.getData().account_name){
					subtotal = parseFloat(subtotal)+parseFloat(total); 
				}
			 }); 

			if(subtotal != 0.0){
				htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+parseFloat(subtotal).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
			}else{
				htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
			}
			
			
			
			//console.log(grouprows[0].getData().account_name);
			grouprows.forEach(element => {  
				var total=element.getData().total.toFixed(numberOfDecimal);
		        var temp = [element.getData().account_name,total];
		       // var temp1 = [element.id,element.name];
		        rows.push(temp);
		        //rows1.push(temp1);
		         var childtree= element.getTreeChildren();
		         var check="0";
		         childtree.forEach(celement => {  
		        	 check="1";
				    }); 
		         if(check == "1"){
		        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td></td></tr>";
		         }else{
		        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		         }
		        
		       
		        childtree.forEach(celement => {  
					var total=celement.getData().total.toFixed(numberOfDecimal);
			        var temp = [celement.getData().account_name,total];
			        rows.push(temp);
			        htmlTable+="<tr><td>&nbsp&nbsp&nbsp&nbsp"+celement.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
			    }); 
		    }); 
	});
	var totalasset=$('#totalasset').html();
	htmlTable+="<tr><td class='calculated'>Total Assets:</td><td class='calculated'>"+totalasset+"</td></tr>";
	htmlTable+="<tr><td><span style='font-weight:bold'>Liabilities</span></td><td></td></tr>";
	groups_2.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		
		var subtotal = 0.0;
		grouprows.forEach(element => { 				
			var total=element.getData().total.toFixed(numberOfDecimal);
			if(key != element.getData().account_name){
				subtotal = parseFloat(subtotal)+parseFloat(total); 
			}
		 }); 

		if(subtotal != 0.0){
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+parseFloat(subtotal).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		}else{
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		}
		
		
		
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {  
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	         var childtree= element.getTreeChildren();
	         var check="0";
	         childtree.forEach(celement => {  
	        	 check="1";
			    }); 
	         if(check == "1"){
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td></td></tr>";
	         }else{
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	         }
	        
	       
	        childtree.forEach(celement => {  
				var total=celement.getData().total.toFixed(numberOfDecimal);
		        var temp = [celement.getData().account_name,total];
		        rows.push(temp);
		        htmlTable+="<tr><td>&nbsp&nbsp&nbsp&nbsp"+celement.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		    }); 
	    }); 
});
	var totalliability=$('#totalliability').html();
	htmlTable+="<tr><td class='calculated'>Total Liabilities:</td><td class='calculated'>"+totalliability+"</td></tr>";
	htmlTable+="<tr><td><span style='font-weight:bold'>Equity</span></td><td></td></tr>";
	groups_3.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		
		var subtotal = 0.0;
		grouprows.forEach(element => { 				
			var total=element.getData().total.toFixed(numberOfDecimal);
			if(key != element.getData().account_name){
				subtotal = parseFloat(subtotal)+parseFloat(total); 
			}
		 }); 

		if(subtotal != 0.0){
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+parseFloat(subtotal).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		}else{
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		}
		
		
		
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {  
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	         var childtree= element.getTreeChildren();
	         var check="0";
	         childtree.forEach(celement => {  
	        	 check="1";
			    }); 
	         if(check == "1"){
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td></td></tr>";
	         }else{
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	         }
	        
	       
	        childtree.forEach(celement => {  
				var total=celement.getData().total.toFixed(numberOfDecimal);
		        var temp = [celement.getData().account_name,total];
		        rows.push(temp);
		        htmlTable+="<tr><td>&nbsp&nbsp&nbsp&nbsp"+celement.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		    }); 
	    }); 
});	var profitloss=$('#profitloss').html();
	var totalequity=$('#totalequity').html();
	var totalvalue=$('#totalvalue').html();
	htmlTable+="<tr><td class='calculated'>Current earnings:</td><td class='calculated'>"+profitloss+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Total Equity:</td><td class='calculated'>"+totalequity+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Total:</td><td class='calculated'>"+totalvalue+"</td></tr>";
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
	doc.save('BalanceSheet.pdf');
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
	var json = JSON.stringify(data);
	console.log(table_1);
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
	
	var htmlTable="<thead><tr><th>Assets</th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_all=table_1;
	var groups_1=table_1.getGroups();
	var groups_2=table_2.getGroups();
	var groups_3=table_3.getGroups();
	groups_1.forEach(group =>{
			var grouprows=group.getRows();
			var key=group.getKey();
			var htmlkey="<b>"+key+"</b>"
			var groupname=[key,''];
			rows.push(groupname);
			
			var subtotal = 0.0;
			grouprows.forEach(element => { 				
				var total=element.getData().total.toFixed(numberOfDecimal);
				if(key != element.getData().account_name){
					subtotal = parseFloat(subtotal)+parseFloat(total); 
				}
			 }); 

			if(subtotal != 0.0){
				htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+parseFloat(subtotal).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
			}else{
				htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
			}
			
			
			
			//console.log(grouprows[0].getData().account_name);
			grouprows.forEach(element => {  
				var total=element.getData().total.toFixed(numberOfDecimal);
		        var temp = [element.getData().account_name,total];
		       // var temp1 = [element.id,element.name];
		        rows.push(temp);
		        //rows1.push(temp1);
		         var childtree= element.getTreeChildren();
		         var check="0";
		         childtree.forEach(celement => {  
		        	 check="1";
				    }); 
		         if(check == "1"){
		        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td></td></tr>";
		         }else{
		        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		         }
		        
		       
		        childtree.forEach(celement => {  
					var total=celement.getData().total.toFixed(numberOfDecimal);
			        var temp = [celement.getData().account_name,total];
			        rows.push(temp);
			        htmlTable+="<tr><td>&nbsp&nbsp&nbsp&nbsp"+celement.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
			    }); 
		    }); 
	});
	var totalasset=$('#totalasset').html();
	htmlTable+="<tr><td class='calculated'>Total Assets:</td><td class='calculated'>"+totalasset+"</td></tr>";
	htmlTable+="<tr><td><span style='font-weight:bold'>Liabilities</span></td><td></td></tr>";
	groups_2.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		
		var subtotal = 0.0;
		grouprows.forEach(element => { 				
			var total=element.getData().total.toFixed(numberOfDecimal);
			if(key != element.getData().account_name){
				subtotal = parseFloat(subtotal)+parseFloat(total); 
			}
		 }); 

		if(subtotal != 0.0){
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+parseFloat(subtotal).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		}else{
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		}
		
		
		
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {  
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	         var childtree= element.getTreeChildren();
	         var check="0";
	         childtree.forEach(celement => {  
	        	 check="1";
			    }); 
	         if(check == "1"){
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td></td></tr>";
	         }else{
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	         }
	        
	       
	        childtree.forEach(celement => {  
				var total=celement.getData().total.toFixed(numberOfDecimal);
		        var temp = [celement.getData().account_name,total];
		        rows.push(temp);
		        htmlTable+="<tr><td>&nbsp&nbsp&nbsp&nbsp"+celement.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		    }); 
	    }); 
});
	var totalliability=$('#totalliability').html();
	htmlTable+="<tr><td class='calculated'>Total Liabilities:</td><td class='calculated'>"+totalliability+"</td></tr>";
	htmlTable+="<tr><td><span style='font-weight:bold'>Equity</span></td><td></td></tr>";
	groups_3.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		
		var subtotal = 0.0;
		grouprows.forEach(element => { 				
			var total=element.getData().total.toFixed(numberOfDecimal);
			if(key != element.getData().account_name){
				subtotal = parseFloat(subtotal)+parseFloat(total); 
			}
		 }); 

		if(subtotal != 0.0){
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+parseFloat(subtotal).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		}else{
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		}
		
		
		
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {  
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	         var childtree= element.getTreeChildren();
	         var check="0";
	         childtree.forEach(celement => {  
	        	 check="1";
			    }); 
	         if(check == "1"){
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td></td></tr>";
	         }else{
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	         }
	        
	       
	        childtree.forEach(celement => {  
				var total=celement.getData().total.toFixed(numberOfDecimal);
		        var temp = [celement.getData().account_name,total];
		        rows.push(temp);
		        htmlTable+="<tr><td>&nbsp&nbsp&nbsp&nbsp"+celement.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		    }); 
	    }); 
});	var profitloss=$('#profitloss').html();
	var totalequity=$('#totalequity').html();
	var totalvalue=$('#totalvalue').html();
	htmlTable+="<tr><td class='calculated'>Current earnings:</td><td class='calculated'>"+profitloss+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Total Equity:</td><td class='calculated'>"+totalequity+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Total:</td><td class='calculated'>"+totalvalue+"</td></tr>";
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
	  //return formData;
	  
}




function generatePdfPrint(dataUrl){
	var data = table_1.getData();
	var json = JSON.stringify(data);
	console.log(table_1);
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
	
	var htmlTable="<thead><tr><th>Assets</th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_all=table_1;
	var groups_1=table_1.getGroups();
	var groups_2=table_2.getGroups();
	var groups_3=table_3.getGroups();
	groups_1.forEach(group =>{
			var grouprows=group.getRows();
			var key=group.getKey();
			var htmlkey="<b>"+key+"</b>"
			var groupname=[key,''];
			rows.push(groupname);
			
			var subtotal = 0.0;
			grouprows.forEach(element => { 				
				var total=element.getData().total.toFixed(numberOfDecimal);
				if(key != element.getData().account_name){
					subtotal = parseFloat(subtotal)+parseFloat(total); 
				}
			 }); 

			if(subtotal != 0.0){
				htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+parseFloat(subtotal).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
			}else{
				htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
			}
			
			
			
			//console.log(grouprows[0].getData().account_name);
			grouprows.forEach(element => {  
				var total=element.getData().total.toFixed(numberOfDecimal);
		        var temp = [element.getData().account_name,total];
		       // var temp1 = [element.id,element.name];
		        rows.push(temp);
		        //rows1.push(temp1);
		         var childtree= element.getTreeChildren();
		         var check="0";
		         childtree.forEach(celement => {  
		        	 check="1";
				    }); 
		         if(check == "1"){
		        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td></td></tr>";
		         }else{
		        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		         }
		        
		       
		        childtree.forEach(celement => {  
					var total=celement.getData().total.toFixed(numberOfDecimal);
			        var temp = [celement.getData().account_name,total];
			        rows.push(temp);
			        htmlTable+="<tr><td>&nbsp&nbsp&nbsp&nbsp"+celement.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
			    }); 
		    }); 
	});
	var totalasset=$('#totalasset').html();
	htmlTable+="<tr><td class='calculated'>Total Assets:</td><td class='calculated'>"+totalasset+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Liabilities</td><td></td></tr>";
	groups_2.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		
		var subtotal = 0.0;
		grouprows.forEach(element => { 				
			var total=element.getData().total.toFixed(numberOfDecimal);
			if(key != element.getData().account_name){
				subtotal = parseFloat(subtotal)+parseFloat(total); 
			}
		 }); 

		if(subtotal != 0.0){
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+parseFloat(subtotal).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		}else{
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		}
		
		
		
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {  
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	         var childtree= element.getTreeChildren();
	         var check="0";
	         childtree.forEach(celement => {  
	        	 check="1";
			    }); 
	         if(check == "1"){
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td></td></tr>";
	         }else{
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	         }
	        
	       
	        childtree.forEach(celement => {  
				var total=celement.getData().total.toFixed(numberOfDecimal);
		        var temp = [celement.getData().account_name,total];
		        rows.push(temp);
		        htmlTable+="<tr><td>&nbsp&nbsp&nbsp&nbsp"+celement.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		    }); 
	    }); 
});
	var totalliability=$('#totalliability').html();
	htmlTable+="<tr><td class='calculated'>Total Liabilities:</td><td class='calculated'>"+totalliability+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Equity</td><td></td></tr>";
	groups_3.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		
		var subtotal = 0.0;
		grouprows.forEach(element => { 				
			var total=element.getData().total.toFixed(numberOfDecimal);
			if(key != element.getData().account_name){
				subtotal = parseFloat(subtotal)+parseFloat(total); 
			}
		 }); 

		if(subtotal != 0.0){
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'>"+parseFloat(subtotal).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		}else{
			htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		}
		
		
		
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {  
			var total=element.getData().total.toFixed(numberOfDecimal);
	        var temp = [element.getData().account_name,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	         var childtree= element.getTreeChildren();
	         var check="0";
	         childtree.forEach(celement => {  
	        	 check="1";
			    }); 
	         if(check == "1"){
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td></td></tr>";
	         }else{
	        	 htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
	         }
	        
	       
	        childtree.forEach(celement => {  
				var total=celement.getData().total.toFixed(numberOfDecimal);
		        var temp = [celement.getData().account_name,total];
		        rows.push(temp);
		        htmlTable+="<tr><td>&nbsp&nbsp&nbsp&nbsp"+celement.getData().account_name+"</td><td>"+total.replace(/\d(?=(\d{3})+\.)/g, '$&,')+"</td></tr>";
		    }); 
	    }); 
});	var profitloss=$('#profitloss').html();
	var totalequity=$('#totalequity').html();
	var totalvalue=$('#totalvalue').html();
	htmlTable+="<tr><td class='calculated'>Current earnings:</td><td class='calculated'>"+profitloss+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Total Equity:</td><td class='calculated'>"+totalequity+"</td></tr>";
	htmlTable+="<tr><td class='calculated'>Total:</td><td class='calculated'>"+totalvalue+"</td></tr>";
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
		var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
		$('#asof').html(asofdate);
		//profitloss(fromDateFormatted,toDateFormatted);
		loadPLSection1Data(fromDateFormatted,toDateFormatted);
		loadPLSection2Data(fromDateFormatted,toDateFormatted);
		loadPLSection3Data(fromDateFormatted,toDateFormatted);
		$('#asofDate').val(asofdate);
	}
}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

