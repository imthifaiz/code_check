<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.util.StrUtils"%>
<%@ page import="com.track.constants.*"%>
<%
	String title = "GST Summary Report";
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
	<jsp:param name="mainmenu" value="<%=IConstants.TAX%>"/>
	<jsp:param name="submenu" value="<%=IConstants.TAX_REPORTS%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/dist/js/moment.min.js"></script>
<link href="../jsp/css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="../jsp/js/tabulator.min.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<div class="container-fluid m-t-20">


	<div class="box">
<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>    
                 <li><a href="../tax/sg-report"><span class="underline-on-hover">Tax Reports</span> </a></li>                                             
                <li><label>Tax Filling Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">


			<div class="row">
				<div class="col-sm-6">
					<div>Report period</div>
					<div class="row">
						<div class="col-sm-4">
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
						<div class="col-sm-4" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" id="fromdate" onchange="fdatechange(this)" type="TEXT"
								size="30" MAXLENGTH="10" name="fromdate" disabled />
						</div>

						<div class="col-sm-4" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" id="todate" onchange="tdatechange(this)" type="TEXT"
								size="30" MAXLENGTH="10" name="todate" disabled />
						</div>
						
						<div class="col-sm-4 txn-buttons" style="vertical-align: baseline;">
							<input type="hidden" name="filterfromdate" value="" >
							<input type="hidden" name="filtertodate" value="" >
							<button type="button" class="btn btn-success"onClick="javascript:return summarySearch();">Search</button>
						</div>
					</div>
				</div>

				<div class="col-sm-6">
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
									onclick="window.location.href='../tax/sg-report'">
									<i class="glyphicon glyphicon-remove"></i>
								</h1>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<br> <br> <div id="print_id">
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
			</div>  --%>
			<input type="number" id="numberOfDecimal" style="display: none;" value=<%=numberOfDecimal%>> 
			<input type="text" id="fiscalyear" style="display: none;" value=<%=fiscalyear%>>

			<div class="box-body">
				<div class="row">
					<div class="col-sm-2"></div>
					<div class="col-sm-8" style="border: gray;border-style: double;">
						<div style="font-size: 18px;text-align: center"><%=PLNTDESC%></div>
						<div style="font-size: 28px;text-align: center"><%=title%></div>
						<div style="font-size: 18px;text-align: center">
							As of <span id="asof"></span>
						</div>
						<div id="taxreturnfill-table1"></div>
					</div>
					<div class="col-sm-2"></div>
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
	period=node.value;
	sessionStorage.setItem('period', period);
	var to;
	var from;	
	$('#fromdate').attr('disabled',true);
	$('#todate').attr('disabled',true);
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
		//to = moment().subtract(1, 'year').startOf('day').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
		//toDateFormatted = moment().subtract(1, 'year').startOf('day').format('YYYY-MM-DD');
		 to   = moment().format('DD/MM/YYYY');
		 toDateFormatted   = moment().format('YYYY-MM-DD');
	}else if(period == "CUSTOM"){
		$('#fromdate').attr('disabled',false);
		$('#todate').attr('disabled',false);
	}

	
	$('#fromdate').val(from);
	$('#todate').val(to);
	//var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
	//$('#asof').html(asofdate);
	//loadPLSection1Data(fromDateFormatted,toDateFormatted);
	//loadPLSection2Data(fromDateFormatted,toDateFormatted);
	//$('#asofDate').val(asofdate);
	$("input[name=filterfromdate]").val(fromDateFormatted);
	$("input[name=filtertodate]").val(toDateFormatted);
	loadTaxSalesData();
}

function fdatechange(obj){
	fromDateFormatted = moment($(obj).val(), "DD/MM/YYYY").format('YYYY-MM-DD');
	$("input[name=filterfromdate]").val(fromDateFormatted);
}

function tdatechange(obj){
	toDateFormatted = moment($(obj).val(), "DD/MM/YYYY").format('YYYY-MM-DD');
	$("input[name=filtertodate]").val(toDateFormatted);
}

function summarySearch(){
	var fdate = $('#fromdate').val();
	if(fdate == "" || fdate == null) {
		   alert("Please Select From Date"); 
		   return false; 
	}
	var tdate = $('#todate').val();
	if(tdate == "" || tdate == null) {
		   alert("Please Select To Date"); 
		   return false; 
	}
	var fromDateFormattedsg = $("input[name=filterfromdate]").val();
	var toDateFormattedsg = $("input[name=filtertodate]").val();
	var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
	$('#asof').html(asofdate);
	//loadPLSection1Data(fromDateFormattedsg,toDateFormattedsg);
	//loadPLSection2Data(fromDateFormattedsg,toDateFormattedsg);
	loadTaxFillTable_1();
	$('#asofDate').val(asofdate);
}

var tablePLSummary;
var numberOfDecimal = $("#numberOfDecimal").val();
var tableDataNested1;
var tableDataNested2;
var table_1summary;
var tableDataSummary;
	$(document).ready(function() {
		var to;
		var from;
		$('#fromdate').attr('disabled',true);
		$('#todate').attr('disabled',true);
		loadTaxFillTable_2();
		if (typeof(Storage) !== "undefined") {
			var periodFromSession = sessionStorage.getItem('period');
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
					
		}else if(period == "CUSTOM"){
			$('#fromdate').attr('disabled',false);
			$('#todate').attr('disabled',false);
		}

		$("input[name=filterfromdate]").val(fromDateFormatted);
		$("input[name=filtertodate]").val(toDateFormatted);
			
		$('#fromdate').val(from);
		$('#todate').val(to);
		$('#reportperiod').val(period);

		//fromDateFormatted = "2020-01-02";
		//toDateFormatted = "2020-12-31";
		
		var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
		$('#asof').html(asofdate);
		//loadPLSection1Data(fromDateFormatted,toDateFormatted);
		//loadPLSection2Data(fromDateFormatted,toDateFormatted);
		loadTaxSalesData();
		loadTaxFillTable_1();
		$('#asofDate').val(asofdate);
	});

	function downTBExcel()
	{
		var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
		window.location = "/track/TaxReturnFiling?action=getSalesTaxReturnSummaryExcel&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"&asOfDate="+asofdate;
	}
	
	function loadPLSection1Data(from,to)
	{
		var acctypes= "('10','8')";
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
				tableDataNested1=profitLossArray;
				loadPL_1();
			}
		});
	}
	function loadPLSection2Data(from,to)
	{
		var acctypes= "('9','11')";
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
 		   footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Gross Profit:</div><div class='pull-right' style='margin-right:10px' id='grossprofit'></div></div>",
		    columns:[
		    {title:"ACCOUNT", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
	    		
	    }},	
		  
		    {title:"Total", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    },titleFormatter: function(cell, formatterParams, onRendered) 
	    {cell.getElement().style.textAlign='right'; return ''+cell.getValue()}},
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
 		   footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Net Profit:</div><div class='pull-right' style='margin-right:10px' id='netprofit'></div></div>",
		    columns:[
		    {title:"ACCOUNT", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	//var account=data.account_name;
		    	return '<a onclick="callDetails(\''+data.account_id+'\')">'+value+'</a>';
	    		
	    }},	
		  
		    {title:"Total", field:"total",align:"right",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	return value.toFixed(numberOfDecimal);
	    		
	    }},
		    ],
		});
			 
			netprofit();	 
	}
	
	function grossprofit(){
		var data = table_1.getData();
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
		grossGlobal=gross;
		gross=gross.toFixed(numberOfDecimal);
		$('#grossprofit').html(gross);
	}
	
	function netprofit(){
		var data = table_2.getData();
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
		net=grossGlobal+net;
		net=net.toFixed(numberOfDecimal);
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
	window.location.href="profitlossdetails.jsp?account="+account+"&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"";
}
	
function generatePdf(dataUrl){	
	var data = table_1summary.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	/* Top Right */
	var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	doc.text('<%=PLNTDESC%>', 87, 50);
	doc.setFontSize(20);
	doc.text('<%=title%>',70,60);
	doc.setFontSize(12);
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,88,70);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);
	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<thead><tr><th></th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['', 'TOTAL'];
	var rows = [];
	var groups_1=table_1summary.getGroups();
	
	/* groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var key=group.getKey();
		var htmlkey="<b>"+key+"</b>"
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+key+"</td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var total="";
			if(element.getData().BOX=='8'){
    			if (element.getData().tamount >= 0) {
    				total= element.getData().CURRENCY +element.getData().tamount;
    			}else{
    				total="- "+element.getData().CURRENCY +Math.abs(element.getData().tamount);
    			}
	    	}else{
	    		total= element.getData().CURRENCY +element.getData().tamount;
	    	}
			
			
	        var temp = [element.getData().DESC_HEADING,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        if(element.getData().BOX == ''){
	        	htmlTable+="<tr><td>"+element.getData().DESC_HEADING+"</td><td>"+total+"</td></tr>";
	        }else{
	        	htmlTable+="<tr><td>BOX "+element.getData().BOX+" "+ +element.getData().DESC_HEADING+"</td><td>"+total+"</td></tr>";
	        }
	    }); 
	}); */
	

	data.forEach(element => {    
			var total="";
			if(element.BOX=='8'){
    			if (element.tamount >= 0) {
    				total= element.CURRENCY +element.tamount;
    			}else{
    				total="- "+element.CURRENCY +Math.abs(element.tamount);
    			}
	    	}else{
	    		total= element.CURRENCY +element.tamount;
	    	}
			
			
	        var temp = [element.DESC_HEADING,total];
	       // var temp1 = [element.id,element.name];
	        rows.push(temp);
	        //rows1.push(temp1);
	        if(element.BOX == ''){
	        	htmlTable+="<tr><td>"+element.DESC_HEADING+"</td><td>"+total+"</td></tr>";
	        }else{
	        	htmlTable+="<tr><td>BOX "+element.BOX+" "+element.DESC_HEADING+"</td><td>"+total+"</td></tr>";
	        }
	    }); 

	
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
	
	  
		 
	  doc.save('GSTSummaryReport.pdf')
}
function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
	}
function generatePdfMail(dataUrl,attachName){	
	var data = table_1summary.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	/* Top Right */
	var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	//doc.setFontStyle("bold");
	doc.text('<%=PLNTDESC%>', 87, 50);
	doc.setFontSize(20);
	doc.setFontStyle("bold");
	doc.text('<%=title%>',70,60);
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,88,70);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<thead><tr><th></th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=table_1summary.getGroups();

	
	/* groups_1.forEach(group =>{
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
	var grossprofit=$('#grossprofit').html();
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
	var netprofit=$('#netprofit').html();
	htmlTable+="<tr><td class='calculated'>Net Profit:</td><td class='calculated'>"+netprofit+"</td></tr>"; */
	
	data.forEach(element => {    
		var total="";
		if(element.BOX=='8'){
			if (element.tamount >= 0) {
				total= element.CURRENCY +element.tamount;
			}else{
				total="- "+element.CURRENCY +Math.abs(element.tamount);
			}
    	}else{
    		total= element.CURRENCY +element.tamount;
    	}
		
		
        var temp = [element.DESC_HEADING,total];
       // var temp1 = [element.id,element.name];
        rows.push(temp);
        //rows1.push(temp1);
        if(element.BOX == ''){
        	htmlTable+="<tr><td>"+element.DESC_HEADING+"</td><td>"+total+"</td></tr>";
        }else{
        	htmlTable+="<tr><td>BOX "+element.BOX+" "+element.DESC_HEADING+"</td><td>"+total+"</td></tr>";
        }
    });
	
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
	
	  	 
	const pdf = new File([doc.output("blob")], attachName+".pdf", {  type: "pdf" }),
	formData = new FormData();

	formData.append("file", pdf);
	progressBar();
	sendMailTemplate(formData);
	  //return formData;
	  
}

function generatePdfPrint(dataUrl){	
	var data = table_1summary.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	/* Top Right */
	var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	//doc.setFontStyle("bold");
	doc.text('<%=PLNTDESC%>', 87, 50);
	doc.setFontSize(20);
	doc.setFontStyle("bold");
	doc.text('<%=title%>',70,60);
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,88,70);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<thead><tr><th></th><th>TOTAL</th></tr></thead><tbody>";
	var col = ['ACCOUNT', 'TOTAL'];
	var rows = [];
	var groups_1=table_1summary.getGroups();
	/* var groups_2=table_2.getGroups();
	
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
	var grossprofit=$('#grossprofit').html();
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
	var netprofit=$('#netprofit').html();
	htmlTable+="<tr><td class='calculated'>Net Profit:</td><td class='calculated'>"+netprofit+"</td></tr>"; */
	data.forEach(element => {    
		var total="";
		if(element.BOX=='8'){
			if (element.tamount >= 0) {
				total= element.CURRENCY +element.tamount;
			}else{
				total="- "+element.CURRENCY +Math.abs(element.tamount);
			}
    	}else{
    		total= element.CURRENCY +element.tamount;
    	}
		
		
        var temp = [element.DESC_HEADING,total];
       // var temp1 = [element.id,element.name];
        rows.push(temp);
        //rows1.push(temp1);
        if(element.BOX == ''){
        	htmlTable+="<tr><td>"+element.DESC_HEADING+"</td><td>"+total+"</td></tr>";
        }else{
        	htmlTable+="<tr><td>BOX "+element.BOX+" "+element.DESC_HEADING+"</td><td>"+total+"</td></tr>";
        }
    });
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

function loadTaxSalesData()
{
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		dataType: 'json',
		data : {
			action : "getSalesTaxReturnSummary",
			FROMDATE:$("input[name=filterfromdate]").val(),
			TODATE:$("input[name=filtertodate]").val()
		},
		success : function(data) {
			//alert(data.isTaxReg);
			var myJSON = JSON.stringify(data);
			tableDataSummary=myJSON;
			table_1summary.replaceData(data);
			//loadTaxExpenseData();
			//console.log(data);
		}
	});

}

function loadTaxFillTable_1()
{
		table_1summary = new Tabulator("#taxreturnfill-table1", {
	    downloadConfig:{
	        columnHeaders:true, //do not include column headers in downloaded table
	        columnGroups:true, //do not include column groups in column headers for downloaded table
	        rowGroups:true, //do not include row groups in downloaded table
	        columnCalcs:true, //do not include column calcs in downloaded table
	        dataTree:true, //do not include data tree in downloaded table
	    },
	    data:tableDataSummary,
	    downloadRowRange:"all",
	    layout:"fitColumns",
	    columns:[
	    {title:"", field:"DESCRIPTION",headerSort:false,width:440,formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	      if(data.BOX == ''){
	    	  return "<span>"+ data.DESC_HEADING + "</span><br><span style='white-space: normal;height:40px'>" + data.DESCRIPTION + "</span>";
	      }else{
	            return "<span>BOX "+data.BOX+" "+ data.DESC_HEADING + "</span><br><span style='white-space: normal;height:40px'>" + data.DESCRIPTION + "</span>";
	      }
	      }},
	    {title:"<div style='text-align:right'>TOTAL</div>", field:"TAXABLE_AMOUNT",width:240,align:"right",headerSort:false,formatter:function(cell, formatterParams){
	    	var data = cell.getData();	
	    	
	    	/* if(data.BOX=='4' || data.BOX=='9' || data.BOX=='13'){
	    		return data.CURRENCY +data.tamount;
    		}else if(data.BOX=='8'){
    			if (data.tamount >= 0) {
    				return data.CURRENCY +data.tamount;
    			}else{
    				return "- "+data.CURRENCY +Math.abs(data.tamount);
    			}
	    	}else if(data.BOX=='1' || data.BOX=='2' || data.BOX=='3' || data.BOX=='5' || data.BOX=='6' || data.BOX=='7'){
	    		return "<a style='align:center' href='/track/jsp/sg-transactionsummary.jsp?DESCHEAD="+data.DESC_HEADING+"&BOX="+data.BOX+"&taxheader="+taxheaderid+"'>" + data.CURRENCY +data.tamount+"</a>";
	    	}else{
	    		return "<a style='align:center' href='/track/jsp/sg-transactionsummary.jsp?DESCHEAD="+data.DESC_HEADING+"&BOX=13&taxheader="+taxheaderid+"'>" + data.CURRENCY +data.tamount+"</a>";
	    	} */
	    	
	    	if(data.BOX=='8'){
    			if (data.tamount >= 0) {
    				return "<span style='font-weight:bold;'>"+data.CURRENCY +data.tamount+ "</span>";
    			}else{
    				return "<span style='font-weight:bold;'>- "+data.CURRENCY +Math.abs(data.tamount)+"</span>";
    			}
	    	}else if(data.BOX=='4'){
	    		return "<span style='font-weight:bold;'>"+data.CURRENCY +data.tamount+ "</span>";
	    	}else{
	    		return data.CURRENCY +data.tamount;
	    	}
            
    }},
    	/* {title:"ADJUSTMENTS", field:"ADJUSTMENTS",align:"center",headerSort:false,width:90,formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	    	if(data.BOX=='6')
	    		{
	    			return "<a style='align:center' data-toggle='modal' data-target='#sg-taximportadjustmentModal' onclick='callAdjustmentModal("+data.BOX+")'>Adjust</a>";
	    		}else if(data.BOX=='7')
	    		{
	    			return "<a style='align:center' data-toggle='modal' data-target='#sg-taximportadjustmentModal' onclick='callAdjustmentModal("+data.BOX+")'>Adjust</a>";
	    		}
	    	else
	    		{
	    		return "";
	    		}
       
    	}}, */
    
	    ],
	});
	loadTaxSalesData();
}

function loadTaxFillTable_2()
{
		table_1summary = new Tabulator("#taxreturnfill-table1", {
	    downloadConfig:{
	        columnHeaders:true, //do not include column headers in downloaded table
	        columnGroups:true, //do not include column groups in column headers for downloaded table
	        rowGroups:true, //do not include row groups in downloaded table
	        columnCalcs:true, //do not include column calcs in downloaded table
	        dataTree:true, //do not include data tree in downloaded table
	    },
	    downloadRowRange:"all",
	    layout:"fitColumns",
	    columns:[
	    {title:"", field:"DESCRIPTION",headerSort:false,width:440,formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	      if(data.BOX == ''){
	    	  return "<span>"+ data.DESC_HEADING + "</span><br><span style='white-space: normal;height:40px'>" + data.DESCRIPTION + "</span>";
	      }else{
	            return "<span>BOX "+data.BOX+" "+ data.DESC_HEADING + "</span><br><span style='white-space: normal;height:40px'>" + data.DESCRIPTION + "</span>";
	      }
	      }},
	    {title:"<div style='text-align:right'>TOTAL</div>", field:"TAXABLE_AMOUNT",width:240,align:"right",headerSort:false,formatter:function(cell, formatterParams){
	    	var data = cell.getData();	
	    	
	    	/* if(data.BOX=='4' || data.BOX=='9' || data.BOX=='13'){
	    		return data.CURRENCY +data.tamount;
    		}else if(data.BOX=='8'){
    			if (data.tamount >= 0) {
    				return data.CURRENCY +data.tamount;
    			}else{
    				return "- "+data.CURRENCY +Math.abs(data.tamount);
    			}
	    	}else if(data.BOX=='1' || data.BOX=='2' || data.BOX=='3' || data.BOX=='5' || data.BOX=='6' || data.BOX=='7'){
	    		return "<a style='align:center' href='/track/jsp/sg-transactionsummary.jsp?DESCHEAD="+data.DESC_HEADING+"&BOX="+data.BOX+"&taxheader="+taxheaderid+"'>" + data.CURRENCY +data.tamount+"</a>";
	    	}else{
	    		return "<a style='align:center' href='/track/jsp/sg-transactionsummary.jsp?DESCHEAD="+data.DESC_HEADING+"&BOX=13&taxheader="+taxheaderid+"'>" + data.CURRENCY +data.tamount+"</a>";
	    	} */
	    	
	    	if(data.BOX=='8'){
    			if (data.tamount >= 0) {
    				return "<span style='font-weight:bold;'>"+data.CURRENCY +data.tamount+ "</span>";
    			}else{
    				return "<span style='font-weight:bold;'>- "+data.CURRENCY +Math.abs(data.tamount)+"</span>";
    			}
	    	}else if(data.BOX=='4'){
	    		return "<span style='font-weight:bold;'>"+data.CURRENCY +data.tamount+ "</span>";
	    	}else{
	    		return data.CURRENCY +data.tamount;
	    	}
            
    }},
    	/* {title:"ADJUSTMENTS", field:"ADJUSTMENTS",align:"center",headerSort:false,width:90,formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	    	if(data.BOX=='6')
	    		{
	    			return "<a style='align:center' data-toggle='modal' data-target='#sg-taximportadjustmentModal' onclick='callAdjustmentModal("+data.BOX+")'>Adjust</a>";
	    		}else if(data.BOX=='7')
	    		{
	    			return "<a style='align:center' data-toggle='modal' data-target='#sg-taximportadjustmentModal' onclick='callAdjustmentModal("+data.BOX+")'>Adjust</a>";
	    		}
	    	else
	    		{
	    		return "";
	    		}
       
    	}}, */
    
	    ],
	});
}
</script>
		  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>