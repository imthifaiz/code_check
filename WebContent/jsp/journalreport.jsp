<%@page import="com.track.dao.*"%>
<%@page import="com.track.constants.*"%>
<%@page import="com.track.util.StrUtils"%>
<%@include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Journal Report";
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
<script type="text/javascript" src="../jsp/js/general.js"></script>
  <script type="text/javascript" src="../jsp/js/calendar.js"></script>
  <script type="text/javascript" src="../jsp/dist/js/moment.min.js"></script>
<link href="../jsp/css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="../jsp/js/tabulator.min.js"></script>
<script type="text/javascript" src="../jsp/js/journalutil.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<div class="container-fluid m-t-20">

	<div class="box">
	<!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                <li><label>Journal</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 

		<div class="box-header menu-drop">

			
			<div class="row">
				<div class="col-sm-4">
				
				</div>
				<div class="col-sm-4" style="text-align:center">
					<div style="font-size:18px"><%=PLNTDESC %></div>
					<div style="font-size:28px"><%=title%></div>
					<div style="font-size:18px">As of <span id="asof"></span></div>
				</div>
				<div class="col-sm-4">
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
								<button type="button" id="exceldownload" class="btn btn-default" data-toggle="tooltip" data-placement="bottom" title="Excel" onclick="{downTBExcel();}">
									
									<i class="fa fa-file-excel-o" aria-hidden="true"></i>
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
			<div class="row">
				<div class="col-sm-10">
				 <div class="row">
				
				<div>Report period</div>
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
				<div class="col-sm-2"><select class="form-control" id="limit" onchange="pldatechanged();">
					<option value="50">50</option>
					<option value="100">100</option>
					<option value="200" selected>200</option>
					</select>
				</div>
				<div class="col-sm-2" style="vertical-align: baseline;"><button class="btn btn-success" id="srbtn" onclick="Searchcustom();" type="button">Run</button></div>
			 </div>
			</div>
			</div>
				
			<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
			<input type="text" id="fiscalyear" style="display:none;" value=<%=fiscalyear%>>
		</div>
		<div class="box-body">
			<div class="input-group" style="float: right;" hidden>
				<label for="fSearch" style="padding-right: 15px;font-weight: bold;">Search</label> 
				<input type="text" id="fSearch" name="fSearch">
			</div>
			<div class="row">
				<div class="col-sm-12">
					<div id="journalledger-table_1"></div>
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

</style>
<script type="text/javascript">
var table_1;
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
	}


	if(period != "CUSTOM"){
		$('#fromdate').val(from);
		$('#todate').val(to);
		var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
		$('#asof').html(asofdate);
		var limit=$("#limit").val();
		if(limit==null || limit==""){
			limit="";
			}
		loadPLSection1Data(fromDateFormatted,toDateFormatted,limit);
		$('#asofDate').val(asofdate);
	}
}
var tablePLSummary;
var numberOfDecimal = $("#numberOfDecimal").val();
var tableDataNested1;


function postDatePickerInit(){
	if (period == 'CUSTOM'){
		var journalreport_fromdate = getLocalStorageValue('journalreport_fromdate', '', 'fromdate');
		$('#fromdate').datepicker("setDate", 
		        moment(journalreport_fromdate, 'DD/MM/YYYY').toDate());
		var journalreport_todate = getLocalStorageValue('journalreport_todate', '50', 'todate');
		$('#todate').datepicker("setDate", 
				moment(journalreport_todate, 'DD/MM/YYYY').toDate());
	}		
	$('#srbtn').click();
}


	$(document).ready(function() {
		var to;
		var from;
		$('#fromdate').prop('disabled', true);
		$('#todate').prop('disabled', true);
		$('#srbtn').hide();
		var periodFromSession = getLocalStorageValue('journalreport_reportperiod', 'TODAY', 'reportperiod');
		period = periodFromSession;
		getLocalStorageValue('journalreport_limit', '', 'limit');
		if (typeof(Storage) !== "undefined") {
			//periodFromSession = sessionStorage.getItem('period');
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
		}


		if(period != "CUSTOM"){
			$('#fromdate').val(from);
			$('#todate').val(to);
			$('#reportperiod').val(period);
			var asofdate=moment(toDateFormatted).format('MMM D,YYYY');
			$('#asof').html(asofdate);
			$("#limit").val("200");
			var limit=$("#limit").val();
			if(limit==null || limit==""){
				limit="";
				}
			loadPLSection1Data(fromDateFormatted,toDateFormatted,limit);
			$('#asofDate').val(asofdate);
		}
		
		const input = document.getElementById("fSearch");
		input.addEventListener("keyup", function() {
		    table_1.setFilter(matchAny, { value: input.value });
		    if (input.value == " ") {
		        table.clearFilter()
		    }
		});
		
		
	});
	
	function matchAny(data, filterParams) {
	      //data - the data for the row being filtered
	      //filterParams - params object passed to the filter
	      //RegExp - modifier "i" - case insensitve

	    var match = false;
	    const regex = RegExp(filterParams.value, 'i');

	    for (var key in data) {
	        if (regex.test(data[key]) == true) {
	            match = true;
	        }
	    }
	    return match;
	}
	
	function loadPLSection1Data(from,to,limit)
	{
		storeInLocalStorage('journalreport_reportperiod', $('#reportperiod').val());
		storeInLocalStorage('journalreport_limit', $('#limit').val());
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('journalreport_fromdate', $('#fromdate').val());
		}
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('journalreport_todate', $('#todate').val());
		}
		$.ajax({
			type : "GET",
			url: '/track/JournalServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getJournalReport",
				fromDate:from,
				toDate:to
			},
			success : function(journalledgerlist) {
				var journalledgerArray=journalledgerlist;
				var myJSON = JSON.stringify(journalledgerArray);
				//console.log(myJSON);
				tableDataNested1=journalledgerArray;
				if(limit=='All'){
					limit=journalledgerArray.length;
					limit=limit*2;
				}
				loadPL_1(limit);
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
	
	function loadPL_1(limit)
	{
		table_1 = populateJournal('journalledger-table_1', limit, tableDataNested1, numberOfDecimal, "local");
	}
	
	$("#pdfdownload").click(function(){
		generate();
	});

	function downTBExcel()
	{
		//alert("coming");
		window.location = "/track/JournalServlet?action=getJournalReportAsExcel&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted;
	}
	
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
	doc.text('<%=title%>',85,60);
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,88,70);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>DEBIT</th><th>CREDIT</th></tr></thead><tbody>";
	var rows = [];
	var groups_1=table_1.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var rows = group.getRows();
	    	var type=rows[0].getData().TRANSACTION_TYPE;
	    	var jdate=rows[0].getData().DATE;
	    	/* var stringDate=""+jdate;
	    	console.log("Date"+stringDate);
	    	var journaldate=moment(stringDate).format('DD/MM/YYYY');
	    	var journaldateFormatted=moment(journaldate).format('DD MMM YYYY'); */
	    	var id;
	    	if(type==="JOURNAL" || type==="CONTRA")
	    		{
	    			id=rows[0].getData().JOURNALHDRID;
	    		}
	    	else
	    		{
	    			id=rows[0].getData().TRANSACTION_ID;
	    		}
	    	var htmlkey= "<i style='font-size:14px;'>"+jdate+"</i>-"+type+"&nbsp;&nbsp;&nbsp;"+id;
		var key=group.getKey();
		var groupElement = group.getElement();
		//var htmlkey=$(groupElement).html();
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+htmlkey+"</td><td class='innerHeader'></td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var debit=element.getData().DEBIT.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
			var credit=element.getData().CREDIT.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().ACCOUNT+"</td><td>"+debit+"</td><td>"+credit+"</td></tr>";
	    }); 
	});
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
	doc.text('<%=title%>',85,60);
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
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>DEBIT</th><th>CREDIT</th></tr></thead><tbody>";
	var rows = [];
	var groups_1=table_1.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var rows = group.getRows();
	    	var type=rows[0].getData().TRANSACTION_TYPE;
	    	var jdate=rows[0].getData().DATE;
	    	/* var stringDate=""+jdate;
	    	console.log("Date"+stringDate);
	    	var journaldate=moment(stringDate).format('DD/MM/YYYY');
	    	var journaldateFormatted=moment(journaldate).format('DD MMM YYYY'); */
	    	var id;
	    	if(type==="JOURNAL" || type==="CONTRA")
	    		{
	    			id=rows[0].getData().JOURNALHDRID;
	    		}
	    	else
	    		{
	    			id=rows[0].getData().TRANSACTION_ID;
	    		}
	    	var htmlkey= "<i style='font-size:14px;'>"+jdate+"</i>-"+type+"&nbsp;&nbsp;&nbsp;"+id;
		var key=group.getKey();
		var groupElement = group.getElement();
		//var htmlkey=$(groupElement).html();
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+htmlkey+"</td><td class='innerHeader'></td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var debit=element.getData().DEBIT.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
			var credit=element.getData().CREDIT.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().ACCOUNT+"</td><td>"+debit+"</td><td>"+credit+"</td></tr>";
	    }); 
	});
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
	
	doc.save('JournalReport.pdf');
}
function generatePdfPrint(dataUrl){	
	var data = table_1.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	/* Top Right */
	var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	//doc.setFontStyle("bold");
	doc.text('<%=PLNTDESC%>', 87, 50);
	doc.setFontSize(20);
	doc.setFontStyle("bold");
	doc.text('<%=title%>',85,60);
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate=moment(toDateFormatted).format('MMM DD,YYYY');
	doc.text('As of '+asofdate,88,70);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<thead><tr><th>ACCOUNT</th><th>DEBIT</th><th>CREDIT</th></tr></thead><tbody>";
	var rows = [];
	var groups_1=table_1.getGroups();
	
	groups_1.forEach(group =>{
		var grouprows=group.getRows();
		var rows = group.getRows();
	    	var type=rows[0].getData().TRANSACTION_TYPE;
	    	var jdate=rows[0].getData().DATE;
	    	/* var stringDate=""+jdate;
	    	console.log("Date"+stringDate);
	    	var journaldate=moment(stringDate).format('DD/MM/YYYY');
	    	var journaldateFormatted=moment(journaldate).format('DD MMM YYYY'); */
	    	var id;
	    	if(type==="JOURNAL" || type==="CONTRA")
	    		{
	    			id=rows[0].getData().JOURNALHDRID;
	    		}
	    	else
	    		{
	    			id=rows[0].getData().TRANSACTION_ID;
	    		}
	    	var htmlkey= "<i style='font-size:14px;'>"+jdate+"</i>-"+type+"&nbsp;&nbsp;&nbsp;"+id;
		var key=group.getKey();
		var groupElement = group.getElement();
		//var htmlkey=$(groupElement).html();
		var groupname=[key,''];
		rows.push(groupname);
		htmlTable+="<tr><td class='innerHeader'>"+htmlkey+"</td><td class='innerHeader'></td><td class='innerHeader'></td></tr>";
		//console.log(grouprows[0].getData().account_name);
		grouprows.forEach(element => {    
			var debit=element.getData().DEBIT.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
			var credit=element.getData().CREDIT.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	        //rows1.push(temp1);
	        htmlTable+="<tr><td>"+element.getData().ACCOUNT+"</td><td>"+debit+"</td><td>"+credit+"</td></tr>";
	    }); 
	});
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
		var limit=$("#limit").val();
		if(limit==null || limit==""){
			limit="";
			}
		loadPLSection1Data(fromDateFormatted,toDateFormatted,limit);
		$('#asofDate').val(asofdate);
	}
}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>