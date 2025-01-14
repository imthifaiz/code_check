<%@page import="com.track.constants.IConstants"%>
<%@include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.util.StrUtils"%>
<%@page import="java.io.*"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Project Trial Balance";
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
	<!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                <li><label>Project Trial Balance</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 

		<div class="box-header menu-drop">


			<div class="row">
				<div class="col-sm-6">
					<div>Report period</div>
					<div class="row">
						<div class="col-sm-4">
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


						<div class="col-sm-4" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" id="fromdate" type="TEXT"
								size="30" MAXLENGTH="10" name="fromdate" autocomplete="off" placeholder="FROM DATE" readonly disabled />
						</div>

						<div class="col-sm-4" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" id="todate" type="TEXT"
								size="30" MAXLENGTH="10" name="todate" autocomplete="off" placeholder="TO DATE" readonly disabled />
						</div>

						<div style="margin-left: 16px;">Project</div>
						<div class="col-sm-6">
							<input type="hidden" name="PROJECTID" value="" id="projId">
							<input type="text" class="form-control typeahead" id="project"
								placeholder="Select a project" name="project" value=""
								width="251px"> <span class="select-icon"
								onclick="$(this).parent().find('input[name=\'project\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
						</div>
						
						<div class="col-sm-3" style="vertical-align: baseline;"><button class="btn btn-success" id="srbtn" onclick="Searchcustom();" type="button">Run</button></div>
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
								<a href="javascript:downTBExcel()" id="tbsexcel"
										style="text-decoration: none; color: black"><button type="button" id="exceldownload" class="btn btn-default"
									data-toggle="tooltip" data-placement="bottom" title="Excel">

									<i
										class="fa fa-file-excel-o" aria-hidden="true"></i>
									<!-- <a href="/track/TrialBalanceServlet?action=getTrialBalanceAsExcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>-->
								</button></a>

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


			<div id="journal-table"></div>
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
<script type="text/javascript">
var finalProjId = "";
var fromDateFormatted;
var numberOfDecimal = $("#numberOfDecimal").val();
var toDateFormatted;
function journaldatechanged(node){
	var period=node.value;
	if(!period && node.length > 1)
	{
		period = node;	
	}
	
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
	}


	if(period != "CUSTOM"){
		var projectid = $("#projId").val();
		finalProjId = projectid
		$('#fromdate').val(from);
		$('#todate').val(to);
		var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
		$('#asof').html(asofdate);
		loadJournalData(fromDateFormatted,toDateFormatted);
		$('#asofDate').val(asofdate);
	}
}
var tableJournalSummary;
var tableDataNested;

var fromDate;
var toDate;
function postDatePickerInit(){
	if (period == 'CUSTOM'){
		var projecttrialbalance_fromdate = getLocalStorageValue('projecttrialbalance_fromdate', '', 'fromdate');
		$('#fromdate').datepicker("setDate", 
		        moment(projecttrialbalance_fromdate, 'DD/MM/YYYY').toDate());
		var projecttrialbalance_todate = getLocalStorageValue('projecttrialbalance_todate', '', 'todate');
		$('#todate').datepicker("setDate", 
				moment(projecttrialbalance_todate, 'DD/MM/YYYY').toDate());
	}		
	$('#srbtn').click();
	$("#fromdate").datepicker("option", "onSelect", function(selectedDate) {
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('projecttrialbalance_fromdate', moment(selectedDate, 'DD/MM/YYYY').format('DD MMM YYYY'));
		}
	}
	);
	$("#todate").datepicker("option", "onSelect", function(selectedDate) {
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('projecttrialbalance_todate', moment(selectedDate, 'DD/MM/YYYY').format('DD MMM YYYY'));
		}
	}
	);
}


	$(document).ready(function() {
		var periodFromSession = getLocalStorageValue('projecttrialbalance_reportperiod', 'THISYEARTODATE', 'reportperiod');
		period = periodFromSession;
		getLocalStorageValue('projecttrialbalance_project', '', 'project');
		/* project Auto Suggestion */
		$('#project').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'PROJECT_NAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				  var urlStr = "../FinProject";
					$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							action : "GET_PROJECT_LIST",
							CUSTNO : "",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							console.log("incoming="+JSON.stringify(data));

							var projects = data.PROJECT;
							projects.splice(0, 0, {"PROJECT_NAME":"General Project","PROJECT":"General", "CUSTNO":"General"});
							
							//data.project.push({"PROJECT_NAME":"General Project","PROJECT":"General"});
							
							return asyncProcess(data.PROJECT);
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
			    suggestion: function(data) {
			    	return '<div><p class="item-suggestion">Project Name: ' + data.PROJECT_NAME + '</p><br/><p class="item-suggestion">Project No: ' + data.PROJECT + '</p><br/><p class="item-suggestion">Customer No: ' + data.CUSTNO + '</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				$('.shipCustomerAddBtn').remove();  
				/*if(displayCustomerpop == 'true'){
				$('.shipCustomer-section .tt-menu').after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#" onclick="document.form1.custModal.value=\'shipcust\'"> + New Cutomer</a></div>');
				}*/
				$(".shipCustomerAddBtn").width(menuElement.width());
				$(".shipCustomerAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				$('.shipCustomerAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.shipCustomerAddBtn').hide();}, 150);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			}).on('typeahead:select',function(event,selection){
				$("input[name=PROJECTID]").val(selection.ID);
				var selectedVal = $("#reportperiod").val();
				   journaldatechanged(selectedVal);
				
				
			});


		
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
			//$('#fromdate').val('');
			//$('#todate').val('');
			$('#srbtn').show();
		}


		if(period != "CUSTOM"){

			$('#fromdate').val(from);
			$('#todate').val(to);
			$('#reportperiod').val(period);
			var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
			$('#asof').html(asofdate);
			loadJournalData(fromDateFormatted,toDateFormatted);
			$('#asofDate').val(asofdate);
	
			fromDate = fromDateFormatted;
			toDate = toDateFormatted;
		}
		
});


	
	function loadJournalData(from,to)
	{
		storeInLocalStorage('projecttrialbalance_reportperiod', $('#reportperiod').val());
		storeInLocalStorage('projecttrialbalance_project', $('#project').val());
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('projecttrialbalance_fromdate', $('#fromdate').val());
		}
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('projecttrialbalance_todate', $('#todate').val());
		}
		//alert("loading data");
		$("#loader").show();
		$.ajax({
			type : "GET",
			url: '/track/TrialBalanceServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getProjectTrialBalance",
				fromDate:from,
				toDate:to,
				projectId:finalProjId
				},
			success : function(journallist) {
				$("#loader").hide();
				var journalArray=journallist;
				var myJSON = JSON.stringify(journalArray);
				console.log(myJSON);
				tableDataNested=journalArray;
				loadJournal();
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
	var table;
	var totaldebit;
	var totalcredit;
	function loadJournal()
	{
		 var bottomCalculas1 = function(values, data, calcParams){
		    var calc = 0;
		    values.forEach(function(value){
		     calc+=value;
		    });
		    totaldebit=parseFloat(calc).toFixed(numberOfDecimal);
		    return parseFloat(calc).toFixed(numberOfDecimal);
		} 
		 var bottomCalculas2 = function(values, data, calcParams){
			    var calc = 0;
			    values.forEach(function(value){
			     calc+=value;
			    });
			    totalcredit=parseFloat(calc).toFixed(numberOfDecimal);
			    return parseFloat(calc).toFixed(numberOfDecimal);
			} 
		
			 table = new Tabulator("#journal-table", {				
			printAsHtml:true,
			printVisibleRows:false,
			 printCopyStyle:true,			
		    height:"711px",
		    data:tableDataNested,		    
		    layout:"fitColumns",
		    columnCalcs:"table",
		   
 		     groupBy:["main_account"],
 		    groupStartOpen:true,
		    columns:[
		    {title:"ACCOUNT", field:"account_name",formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	var data=cell.getRow().getData();
		    	var account=data.account_name;
		    	return '<a onclick="newcallDetails(\''+data.account_id+'\',\''+value+'\')">'+getNumberInMillionFormat(value)+'</a>';
	    		
	    },bottomCalcFormatter:function(cell, formatterParams, onRendered){
	    	return "Total";
	    }
		    },	
		  
		    {title:"NET DEBIT", field:"net_debit",align:"right",bottomCalc:bottomCalculas1, formatterParams:{precision:numberOfDecimal},bottomCalcParams:{
		        precision:numberOfDecimal,	
		        
		    },formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	
		    	if(value>0)
		    		{
		    		value=parseFloat(value).toFixed(numberOfDecimal);
		    			return getNumberInMillionFormat(value);
		    		}
		    	else
		    		{
		    			return "";
		    		}
	    		
	    }},
		  
		    {title:"NET CREDIT", field:"net_credit",align:"right",formatterParams:{precision:numberOfDecimal},bottomCalc:bottomCalculas2,bottomCalcParams:{
		        precision:numberOfDecimal,	
		      
		    },formatter:function(cell, formatterParams, onRendered){
		    	var value=cell.getValue();
		    	
		    	if(value>0)
	    		{
		    		value=parseFloat(value).toFixed(numberOfDecimal);
	    			return getNumberInMillionFormat(value);
	    		}
	    	else
	    		{
	    			return "";
	    		}
	    }},
		    ],
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
		//projectId:finalProjId
		window.location = "/track/TrialBalanceServlet?action=getProjectTrialBalanceExcel&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"&projectId="+finalProjId;
	}
// 	$("#tbsexcel").on('click', function() 
//     {
// 		window.location = "/track/TrialBalanceServlet?action=getTrialBalanceAsExcel&fromDate="+fromDate+"&toDate="+toDate;
// 		  //location.href = "/track/TrialBalanceServlet?action=getTrialBalanceAsExcel&fromDate="+fromDate+"&toDate="+toDate;
		 
//     });
	

	$("#exceldownload").click(function()
	{
		var tableData = table.getData();
		var titleNm = '<%=title%>';
		var asofdate = moment(toDateFormatted).format('MMM DD,YYYY');
		var dateStr = 'As of '+asofdate;		
		var json = JSON.stringify(tableData);
		$.ajax({
			type : "GET",
			url: '/track/TrialBalanceServlet',
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
	window.location.href="../projecttrial/projtrialbaldetail?account="+account+"&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"";
}
	
function newcallDetails(account,accountName)
{
	//alert("Called");
	window.location.href="../projecttrial/projtrialbaldetail?account="+account+"&accname="+accountName+"&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"";
	}
function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
}	
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
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>NET DEBIT</th><th>NET CREDIT</th></tr></thead><tbody>";
	var rows = [];
	var groups_1=table.getGroups();
	
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
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+getNumberInMillionFormat(netdebit)+"</td><td>"+getNumberInMillionFormat(netcredit)+"</td></tr>";
	    }); 
	});
	htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+getNumberInMillionFormat(totaldebit)+"</td><td class='calculated'>"+getNumberInMillionFormat(totalcredit)+"</td></tr>";
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
			if (data.column.index === 0) {
				data.cell.styles.halign = 'left';
			}
			else{
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
	var data = table.getData();
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
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>NET DEBIT</th><th>NET CREDIT</th></tr></thead><tbody>";
	var rows = [];
	var groups_1=table.getGroups();
	
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
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+getNumberInMillionFormat(netdebit)+"</td><td>"+getNumberInMillionFormat(netcredit)+"</td></tr>";
	    }); 
		
	});
	htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+getNumberInMillionFormat(totaldebit)+"</td><td class='calculated'>"+getNumberInMillionFormat(totalcredit)+"</td></tr>";
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
			if (data.column.index === 0) {
				data.cell.styles.halign = 'left';
			}
			else{
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
	
	doc.save('Trialbalance.pdf');
}
function generatePdfPrint(dataUrl){	
	var data = table.getData();
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
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>NET DEBIT</th><th>NET CREDIT</th></tr></thead><tbody>";
	var rows = [];
	var groups_1=table.getGroups();
	
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
	        htmlTable+="<tr><td>"+element.getData().account_name+"</td><td>"+getNumberInMillionFormat(netdebit)+"</td><td>"+getNumberInMillionFormat(netcredit)+"</td></tr>";
	    }); 
	});
	htmlTable+="<tr><td class='calculated'>Total</td><td class='calculated'>"+getNumberInMillionFormat(totaldebit)+"</td><td class='calculated'>"+getNumberInMillionFormat(totalcredit)+"</td></tr>";
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
		var projectid = $("#projId").val();
		finalProjId = projectid
		var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
		$('#asof').html(asofdate);
		loadJournalData(fromDateFormatted,toDateFormatted);
		$('#asofDate').val(asofdate);
	}
}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
