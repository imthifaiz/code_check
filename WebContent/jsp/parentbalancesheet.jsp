<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.ParentChildCmpDet"%>
<%@ page import="com.track.util.StrUtils"%>
<%@ page import="com.track.constants.*"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Consolidated Balance Sheet";
	String title2 = "Balance Sheet";
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
	for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT();
		System.out.println(plist.getCHILD_PLANT());
	}

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
                <li><label>Consolidated Balance Sheet</label></li>                                   
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
						<div class="col-sm-2" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" autocomplete="off"
								id="fromdate" type="TEXT" size="30" MAXLENGTH="10"
								name="fromdate" placeholder="FROM DATE" readonly disabled />
						</div>

						<div class="col-sm-2" style="vertical-align: baseline;">
							<INPUT class="form-control datepicker" autocomplete="off"
								id="todate" type="TEXT" size="30" MAXLENGTH="10" name="todate"
								placeholder="TO DATE" readonly disabled />
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
					<div id="<%=plant %>balancesheet-table_1" style="width:514px"></div>
					<div id="<%=plant %>balancesheet-table_2" style="width:514px"></div>
					<div id="<%=plant %>balancesheet-table_3" style="width:514px"></div>
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
<script>
var DataList=[]; 
var arr = new Array();
<%for (ParentChildCmpDet plist : plantlist) {
	String plant=plist.getCHILD_PLANT(); %>
	var <%=plant %>fromDateFormatted;
	var <%=plant %>toDateFormatted;
	var <%=plant %>period;
	DataList.push({ pcode:"<%=plant %>" });
	arr.push('<%=plant %>');
<%}%>
function pldatechanged(node){
	$('#fromdate').prop('disabled', true);
	$('#todate').prop('disabled', true);
	$('#srbtn').hide();
	$('#plantlist').val(arr);
	period=node.value;
	sessionStorage.setItem('period', period);
	<%
	System.out.println("plantlist : " + plantlist);
	for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); 
		System.out.println("plant" + plant);
		%>
	var <%=plant %>to;
	var <%=plant%>from;
	
	if(period=="TODAY")
		{
			<%=plant %>from = moment().format('DD/MM/YYYY');
			<%=plant %>to   = moment().format('DD/MM/YYYY');
			<%=plant %>fromDateFormatted = moment().format('YYYY-MM-DD');
			<%=plant %>toDateFormatted   = moment().format('YYYY-MM-DD');
		}
	else if(period=="THISYEARTODATE")
	 { 
		 var fiscalyear=$('#fiscalyear').val();
		 var formattedFiscal=moment(fiscalyear).format('YYYY-MM-DD');
		 <%=plant %>from = moment(formattedFiscal).year(moment().year()).format('DD/MM/YYYY');
		 <%=plant %>to   = moment().format('DD/MM/YYYY');
		 <%=plant %>fromDateFormatted = moment(formattedFiscal).year(moment().year()).format('YYYY-MM-DD');
		 <%=plant %>toDateFormatted   = moment().format('YYYY-MM-DD');
	 }
	else if(period=="THISMONTH")
	{
		<%=plant %>from = moment().startOf('month').format('DD/MM/YYYY');
		<%=plant %>to   = moment().endOf('month').format('DD/MM/YYYY');
		<%=plant %>fromDateFormatted = moment().startOf('month').format('YYYY-MM-DD');
		<%=plant %>toDateFormatted   = moment().endOf('month').format('YYYY-MM-DD');
	}
	else if(period=="THISQUARTER")
	{
		<%=plant %>from = moment().quarter(moment().quarter()).startOf('quarter').format('DD/MM/YYYY');
		<%=plant %>to   = moment().quarter(moment().quarter()).endOf('quarter').format('DD/MM/YYYY');
		<%=plant %>fromDateFormatted = moment().quarter(moment().quarter()).startOf('quarter').format('YYYY-MM-DD');
		<%=plant %>toDateFormatted   = moment().quarter(moment().quarter()).endOf('quarter').format('YYYY-MM-DD');
	}
	else if(period=="THISYEAR")
	{
		<%=plant %>from = moment().year(moment().year()).startOf('year').format('DD/MM/YYYY');
		<%=plant %>to   = moment().year(moment().year()).endOf('year').format('DD/MM/YYYY');
		<%=plant %>fromDateFormatted = moment().year(moment().year()).startOf('year').format('YYYY-MM-DD');
		<%=plant %>toDateFormatted   = moment().year(moment().year()).endOf('year').format('YYYY-MM-DD');
	}
	else if(period=="YESTERDAY")
	{
		<%=plant %>from = moment().subtract(1, 'days').format('DD/MM/YYYY');
		<%=plant %>to = moment().subtract(1, 'days').format('DD/MM/YYYY');
		<%=plant %>fromDateFormatted = moment().subtract(1, 'days').format('YYYY-MM-DD');
		<%=plant %>toDateFormatted = moment().subtract(1, 'days').format('YYYY-MM-DD');
	}
	else if(period=="LASTMONTH")
	{
		<%=plant %>from = moment().subtract(1, 'months').startOf('month').format('DD/MM/YYYY');
		<%=plant %>to = moment().subtract(1, 'months').endOf('month').format('DD/MM/YYYY');
		<%=plant %>fromDateFormatted = moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD');
		<%=plant %>toDateFormatted = moment().subtract(1, 'months').endOf('month').format('YYYY-MM-DD');
	}
	else if(period=="LASTQUARTER")
	{
		<%=plant %>from = moment().subtract(3, 'months').startOf('month').format('DD/MM/YYYY');
		<%=plant %>to = moment().subtract(1, 'months').endOf('month').format('DD/MM/YYYY');
		<%=plant %>fromDateFormatted = moment().subtract(3, 'months').startOf('month').format('YYYY-MM-DD');
		<%=plant %>toDateFormatted = moment().subtract(1, 'months').endOf('month').format('YYYY-MM-DD');
	}
	else if(period=="LASTYEAR")
	{
		<%=plant %>from = moment().subtract(1, 'year').startOf('year').format('DD/MM/YYYY');
		<%=plant %>to = moment().subtract(1, 'year').endOf('year').format('DD/MM/YYYY');
		<%=plant %>fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
		<%=plant %>toDateFormatted = moment().subtract(1, 'year').endOf('year').format('YYYY-MM-DD');
	}
	else if(period == "LASTYEARTODATE")
	{
		<%=plant %>from = moment().subtract(1, 'year').startOf('year').format('DD/MM/YYYY');
		<%=plant %>to = moment().subtract(1, 'year').startOf('day').format('DD/MM/YYYY');
		<%=plant %>fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
		<%=plant %>toDateFormatted = moment().subtract(1, 'year').startOf('day').format('YYYY-MM-DD');
				
	}
	else if(period == "CUSTOM"){
		console.log("In custom period");
		$('#fromdate').prop('disabled', false);
		$('#todate').prop('disabled', false);
		$('#fromdate').val('');
		$('#todate').val('');
		$('#srbtn').show();
	}


	if(period != "CUSTOM"){
		$('#fromdate').val(<%=plant %>from);
		$('#todate').val(<%=plant %>to);
		var asofdate=moment(<%=plant %>toDateFormatted).format('MMM D,YYYY');
		$('.asof').html(asofdate);
		//profitloss(fromDateFormatted,toDateFormatted);
		<%=plant %>loadPLSection1Data(<%=plant %>fromDateFormatted,<%=plant %>toDateFormatted);
		<%=plant %>loadPLSection2Data(<%=plant %>fromDateFormatted,<%=plant %>toDateFormatted);
		<%=plant %>loadPLSection3Data(<%=plant %>fromDateFormatted,<%=plant %>toDateFormatted);
		$('#asofDate').val(asofdate);
	}
	
	<%}%>
}

var numberOfDecimal = $("#numberOfDecimal").val();
<%for (ParentChildCmpDet plist : plantlist) {
	String plant=plist.getCHILD_PLANT(); %>
var <%=plant %>tablePLSummary;
var <%=plant %>tableDataNested1;
var <%=plant %>tableDataNested2;
var <%=plant %>tableDataNested3;
var <%=plant %>profitLossGlobal=0.00;
var <%=plant %>totallbt=0.00;
var <%=plant %>totaleqt=0.00;
<%}%>

function postDatePickerInit(){
	if (period == 'CUSTOM'){
		var parentbalancesheet_fromdate = getLocalStorageValue('parentbalancesheet_fromdate', '', 'fromdate');
		$('#fromdate').datepicker("setDate", 
		        moment(parentbalancesheet_fromdate, 'DD/MM/YYYY').toDate());
		var parentbalancesheet_todate = getLocalStorageValue('parentbalancesheet_todate', '', 'todate');
		$('#todate').datepicker("setDate", 
				moment(parentbalancesheet_todate, 'DD/MM/YYYY').toDate());
	}		
	$('#srbtn').click();
}

	$(document).ready(function() {
		$('#plantlist').val(arr);
		<%for (ParentChildCmpDet plist : plantlist) {
			String plant=plist.getCHILD_PLANT(); %>
		var <%=plant %>to;
		var <%=plant %>from;
		<%}%>
		$('#fromdate').prop('disabled', true);
		$('#todate').prop('disabled', true);
		$('#srbtn').hide();
		
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
			        updateSelectedCompaniesInLocalStorage('parentbalancesheet_companieslist');
			    });
		<%}%>
		var selectedCompanies = getLocalStorageValue('parentbalancesheet_companieslist', '', '');
		$(".companyname").each(function( index  ) {
	  		if (selectedCompanies != '' && selectedCompanies.search($( this ).val()) == -1){
	  			$( this ).prop('checked', false);
	  			$('#' + $( this ).val() + 'tab').hide();
	      	}
		});
		var periodFromSession = getLocalStorageValue('parentbalancesheet_reportperiod', 'TODAY', 'reportperiod');
		period = periodFromSession;
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
		 
		 <%for (ParentChildCmpDet plist : plantlist) {
				String plant=plist.getCHILD_PLANT(); %>
		 if(period=="TODAY")
			{
			 <%=plant %>from = moment().format('DD/MM/YYYY');
			 <%=plant %>to   = moment().format('DD/MM/YYYY');
			 <%=plant %>fromDateFormatted = moment().format('YYYY-MM-DD');
			 <%=plant %>toDateFormatted   = moment().format('YYYY-MM-DD');
			}
		 else if(period=="THISYEARTODATE")
		 { 
			 var fiscalyear=$('#fiscalyear').val();
			 var formattedFiscal=moment(fiscalyear).format('YYYY-MM-DD');
			 <%=plant %>from = moment(formattedFiscal).year(moment().year()).format('DD/MM/YYYY');
			 <%=plant %>to   = moment().format('DD/MM/YYYY');
			 <%=plant %>fromDateFormatted = moment(formattedFiscal).year(moment().year()).format('YYYY-MM-DD');
			 <%=plant %>toDateFormatted   = moment().format('YYYY-MM-DD');
		 }
		else if(period=="THISMONTH")
		{
			<%=plant %>from = moment().startOf('month').format('DD/MM/YYYY');
			<%=plant %>to   = moment().endOf('month').format('DD/MM/YYYY');
			<%=plant %>fromDateFormatted = moment().startOf('month').format('YYYY-MM-DD');
			<%=plant %>toDateFormatted   = moment().endOf('month').format('YYYY-MM-DD');
		}
		else if(period=="THISQUARTER")
		{
			<%=plant %>from = moment().quarter(moment().quarter()).startOf('quarter').format('DD/MM/YYYY');
			<%=plant %>to   = moment().quarter(moment().quarter()).endOf('quarter').format('DD/MM/YYYY');
			<%=plant %>fromDateFormatted = moment().quarter(moment().quarter()).startOf('quarter').format('YYYY-MM-DD');
			<%=plant %>toDateFormatted   = moment().quarter(moment().quarter()).endOf('quarter').format('YYYY-MM-DD');
		}
		else if(period=="THISYEAR")
		{
			<%=plant %>from = moment().year(moment().year()).startOf('year').format('DD/MM/YYYY');
			<%=plant %>to   = moment().year(moment().year()).endOf('year').format('DD/MM/YYYY');
			<%=plant %>fromDateFormatted = moment().year(moment().year()).startOf('year').format('YYYY-MM-DD');
			<%=plant %>toDateFormatted   = moment().year(moment().year()).endOf('year').format('YYYY-MM-DD');
		}
		else if(period=="YESTERDAY")
		{
			<%=plant %>from = moment().subtract(1, 'days').format('DD/MM/YYYY');
			<%=plant %>to = moment().subtract(1, 'days').format('DD/MM/YYYY');
			<%=plant %>fromDateFormatted = moment().subtract(1, 'days').format('YYYY-MM-DD');
			<%=plant %>toDateFormatted = moment().subtract(1, 'days').format('YYYY-MM-DD');
		}
		else if(period=="LASTMONTH")
		{
			<%=plant %>from = moment().subtract(1, 'months').startOf('month').format('DD/MM/YYYY');
			<%=plant %>to = moment().subtract(1, 'months').endOf('month').format('DD/MM/YYYY');
			<%=plant %>fromDateFormatted = moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD');
			<%=plant %>toDateFormatted = moment().subtract(1, 'months').endOf('month').format('YYYY-MM-DD');
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
			<%=plant %>from = moment().subtract(1, 'year').startOf('year').format('DD/MM/YYYY');
			<%=plant %>to = moment().subtract(1, 'year').endOf('year').format('DD/MM/YYYY');
			<%=plant %>fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
			<%=plant %>toDateFormatted = moment().subtract(1, 'year').endOf('year').format('YYYY-MM-DD');
		}
		else if(period == "LASTYEARTODATE")
		{
			<%=plant %>from = moment().subtract(1, 'year').startOf('year').format('DD/MM/YYYY');
			<%=plant %>to = moment().subtract(1, 'year').startOf('day').format('DD/MM/YYYY');
			<%=plant %>fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
			<%=plant %>toDateFormatted = moment().subtract(1, 'year').startOf('day').format('YYYY-MM-DD');
					
		}
		else if(period == "CUSTOM"){
			$('#fromdate').prop('disabled', false);
			$('#todate').prop('disabled', false);
			$('#fromdate').val('');
			$('#todate').val('');
			$('#srbtn').show();
		}


		if(period != "CUSTOM"){
			$('#fromdate').val(<%=plant %>from);
			$('#todate').val(<%=plant %>to);
			$('#reportperiod').val(period);
	
			//fromDateFormatted = "2020-01-02";
			//toDateFormatted = "2020-12-31";
			
			var asofdate=moment(<%=plant %>toDateFormatted).format('MMM D,YYYY');
			$('.asof').html(asofdate);
			<%=plant %>loadPLSection1Data(<%=plant %>fromDateFormatted,<%=plant %>toDateFormatted);
			<%=plant %>loadPLSection2Data(<%=plant %>fromDateFormatted,<%=plant %>toDateFormatted);
			<%=plant %>loadPLSection3Data(<%=plant %>fromDateFormatted,<%=plant %>toDateFormatted);
			$('#asofDate').val(asofdate);
		}
		<%}%>
	});
	
	
	<%for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); %>
	function <%=plant %>loadPLSection1Data(from,to)
	{
		storeInLocalStorage('parentbalancesheet_reportperiod', $('#reportperiod').val());
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('parentbalancesheet_fromdate', $('#fromdate').val());
		}
		if ($("#reportperiod").val().toLowerCase() == 'custom'){
			storeInLocalStorage('parentbalancesheet_todate', $('#todate').val());
		}
		updateSelectedCompaniesInLocalStorage('parentbalancesheet_companieslist');
		var acctypes= "('1','2','3')";
		$.ajax({
			type : "POST",
			url: '/track/ParentBalanceSheetServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getBalanceSheetAsset",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes,
				plant:'<%=plant %>'
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
		var acctypes= "('4','5','6')";
		$.ajax({
			type : "POST",
			url: '/track/ParentBalanceSheetServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getBalanceSheetLiability",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes,
				plant:'<%=plant %>'
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
	
	function <%=plant %>loadPLSection3Data(from,to)
	{
		var acctypes= "('7','12',13)";
		$.ajax({
			type : "POST",
			url: '/track/ParentBalanceSheetServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getBalanceSheetEquity",
				fromDate:from,
				toDate:to,
				accounttypes:acctypes,
				plant:'<%=plant %>'
			},
			success : function(profitlosslist) {
				var profitLossArray=profitlosslist;
				var myJSON = JSON.stringify(profitLossArray);
				//alert(myJSON);
				<%=plant %>tableDataNested3=profitLossArray;
				<%=plant %>loadPL_3();
			}
		});
	}
	
	function <%=plant %>profitloss(from,to)
	{
		$.ajax({
			type : "POST",
			url: '/track/ParentBalanceSheetServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getBalanceSheetProfitLoss",
				fromDate:from,
				toDate:to,
				plant:'<%=plant %>'
			},
			success : function(profitloss) {
				var currentearnings=profitloss.currentearnings;
				//alert(currentearnings);
					<%=plant %>profitLossGlobal=parseFloat(profitloss.currentearnings);
					<%=plant %>totalequity(<%=plant %>profitLossGlobal);
			}
		});
		
	}
	/* var openIcon = function(value, data, cell, row, options){ //plain text value
	    return "<i class='fa fa-edit'></i>"
	};
	var deleteIcon = function(value, data, cell, row, options){ //plain text value
	    return "<i class='fa fa-trash'></i>"
	};
	var menuTitleFormatter = function(cell, formatterParams, onRendered){
        var searchNode='<span class="glyphicon glyphicon-search searchAccFilter" aria-hidden="true"></span>';
	    return searchNode;
	}; */
	var <%=plant %>table_1;
	var <%=plant %>table_2;
	var <%=plant %>table_3;
	var <%=plant %>assetGlobal=0.00;
	var <%=plant %>liabilityGlobal=0.00;
	var <%=plant %>equityGlobal=0.00;
	function <%=plant %>loadPL_1()
	{
		var <%=plant %>node1;
		<%=plant %>table_1 = new Tabulator("#<%=plant %>balancesheet-table_1", {
		    data:<%=plant %>tableDataNested1,
		    layout:"fitColumns",
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
 		   footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Total Assets:</div><div class='pull-right' style='margin-right:10px' id='<%=plant %>totalasset'></div></div>",
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
			 
		<%=plant %>totalasset();	 
	}
	function <%=plant %>loadPL_2()
	{
		var <%=plant %>node2;
		<%=plant %>table_2 = new Tabulator("#<%=plant %>balancesheet-table_2", {
		    data:<%=plant %>tableDataNested2,
		    layout:"fitColumns",
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
 		   footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Total Liabilities:</div><div class='pull-right' style='margin-right:10px' id='<%=plant %>totalliability'></div></div>",
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
			 
		<%=plant %>totalliability();	 
	}
	function <%=plant %>loadPL_3()
	{
		var <%=plant %>node2;
		<%=plant %>table_3 = new Tabulator("#<%=plant %>balancesheet-table_3", {
		    data:<%=plant %>tableDataNested3,
		    layout:"fitColumns",
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
 		    footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Current earnings</div><div class='pull-right' style='margin-right:10px' id='<%=plant %>profitloss'></div></div><div class='row'><div class='pull-left' style='margin-left:30px'>Total Equity:</div><div class='pull-right' style='margin-right:10px' id='<%=plant %>totalequity'></div></div><div class='row'><div class='pull-left' style='margin-left:30px'>Total :</div><div class='pull-right' style='margin-right:10px' id='<%=plant %>totalvalue'></div></div>",
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
			 
		<%=plant %>profitloss(<%=plant %>fromDateFormatted,<%=plant %>toDateFormatted);
			 
	}
	function <%=plant %>totalasset(){
		var data = <%=plant %>table_1.getData();
		var asset=0.00;
		data.forEach(function(item){
				asset+= item.total;
	        });
		<%=plant %>assetGlobal=asset;
		asset=asset.toFixed(numberOfDecimal);
		$('#<%=plant %>totalasset').html(asset);
	}
	
	function <%=plant %>totalliability(){
		var data = <%=plant %>table_2.getData();
		var liability=0.00;
		data.forEach(function(item){
			liability+= item.total;
	        });
		<%=plant %>liabilityGlobal=liability;
		liability=liability.toFixed(numberOfDecimal);
		//totalvalue = parseFloat(totalvalue) + parseFloat(liability);
		var totalvalue = parseFloat(<%=plant %>liabilityGlobal) + parseFloat(<%=plant %>equityGlobal);
		totalvalue = parseFloat(totalvalue).toFixed(numberOfDecimal);
		$('#<%=plant %>totalvalue').html(totalvalue);
		$('#<%=plant %>totalliability').html(liability);
	}
	
	function <%=plant %>totalequity(currEarnings){
		var data = <%=plant %>table_3.getData();
		var equity=0.00;
		data.forEach(function(item){
			equity+= item.total;
	        });
		equity=currEarnings+equity;
		<%=plant %>equityGlobal=equity;
		equity=equity.toFixed(numberOfDecimal);
		
		currEarnings=currEarnings.toFixed(numberOfDecimal);
		//var currEarnings=profitloss(fromDateFormatted,toDateFormatted);
		var htmlCurrEar="<a onClick='<%=plant %>redirectProfitLoss();'>"+currEarnings+"</a>";
		console.log("liabilityGlobal-----"+<%=plant %>liabilityGlobal);
		console.log("equity-----"+equity);
		var totalvalue = parseFloat(<%=plant %>liabilityGlobal) + parseFloat(equity);
		totalvalue = parseFloat(totalvalue).toFixed(numberOfDecimal);
		$('#<%=plant %>profitloss').html(htmlCurrEar);
		$('#<%=plant %>totalequity').html(equity);
		$('#<%=plant %>totalvalue').html(totalvalue);
	}
	
	
	function <%=plant %>redirectProfitLoss()
	{
		window.location.href="profitloss.jsp";
	}

	function <%=plant %>callDetails(account)
	{
		//alert("Called");
		window.location.href="../businessoverview/parentbalancesheetdetail?account="+account+"&fromDate="+<%=plant %>fromDateFormatted+"&toDate="+<%=plant %>toDateFormatted+"&plant=<%=plant %>";
	}
		
	<%}%>



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
	<%for (ParentChildCmpDet plist : plantlist) {
		String plant=plist.getCHILD_PLANT(); %>
		<%=plant %>fromDateFormatted = moment(fromdate, "DD/MM/YYYY").format('YYYY-MM-DD');
		<%=plant %>toDateFormatted = moment(todate, "DD/MM/YYYY").format('YYYY-MM-DD');
	<%}%>

	if(moment(fromdate, "DD/MM/YYYY").isAfter(moment(todate, "DD/MM/YYYY"), 'days')){
		$('#fromdate').val('');
		$('#todate').val('');
		alert("Please select valid from and to date");
	}else{
		
		//profitloss(fromDateFormatted,toDateFormatted);
		<%for (ParentChildCmpDet plist : plantlist) {
			String plant=plist.getCHILD_PLANT(); %>
			var asofdate=moment(<%=plant %>toDateFormatted).format('MMM D,YYYY');
			$('.asof').html(asofdate);
			<%=plant %>loadPLSection1Data(<%=plant %>fromDateFormatted,<%=plant %>toDateFormatted);
			<%=plant %>loadPLSection2Data(<%=plant %>fromDateFormatted,<%=plant %>toDateFormatted);
			<%=plant %>loadPLSection3Data(<%=plant %>fromDateFormatted,<%=plant %>toDateFormatted);
			$('#asofDate').val(asofdate);
		<%}%>
		
		
	}
	updateSelectedCompaniesInLocalStorage('parentbalancesheet_companieslist');
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

	$('.printMe').click(function() {
		printdoc();
	});

	function printdoc() {
		var img = toDataURL($(".dash-logo").attr("src"), function(dataUrl) {
			generatePdfPrint(dataUrl);
		}, 'image/jpeg');
	}
	
	function generatePdfPrint(dataUrl){
		<%for (ParentChildCmpDet plist : plantlist) {
			String plant=plist.getCHILD_PLANT(); 
			%>
		var <%=plant %>data = <%=plant %>table_1.getData();
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
		var fromDate=moment(<%=plant %>fromDateFormatted).format('DD MMM YYYY');
		var toDate=moment(<%=plant %>toDateFormatted).format('DD MMM YYYY');
		doc.setFontSize(17);
		doc.setFontStyle("normal");
		doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
		doc.setFontSize(12);
		doc.text('<%=name%>', 110, 50, 'center');
		doc.setFontSize(20);
		doc.text('<%=title2%>',110,60, 'center');
		doc.setFontSize(12);
		var asofdate=moment(<%=plant %>toDateFormatted).format('MMM DD,YYYY');
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
		var groups_1=<%=plant %>table_1.getGroups();
		var groups_2=<%=plant %>table_2.getGroups();
		var groups_3=<%=plant %>table_3.getGroups();
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
		var totalasset=$('#<%=plant %>totalasset').html();
		htmlTable+="<tr><td class='calculated'>Total Assets:</td><td class='calculated'>"+totalasset+"</td></tr>";
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
		var totalliability=$('#<%=plant %>totalliability').html();
		htmlTable+="<tr><td class='calculated'>Total Liabilities:</td><td class='calculated'>"+totalliability+"</td></tr>";
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
		var profitloss=$('#<%=plant %>profitloss').html();
		var totalequity=$('#<%=plant %>totalequity').html();
		var totalvalue=$('#<%=plant %>totalvalue').html();
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
	
	$("#pdfdownload").click(function(){
		generate();
	});
	
	function generate() {
		
		var img = toDataURL($(".dash-logo").attr("src"),
				function(dataUrl) {
					generatePdf(dataUrl);
			  	},'image/jpeg');
			
		}
	
	function generatePdf(dataUrl){
		<%for (ParentChildCmpDet plist : plantlist) {
			String plant=plist.getCHILD_PLANT(); 
			%>
		var <%=plant %>data = <%=plant %>table_1.getData();
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
		var fromDate=moment(<%=plant %>fromDateFormatted).format('DD MMM YYYY');
		var toDate=moment(<%=plant %>toDateFormatted).format('DD MMM YYYY');
		doc.setFontSize(17);
		doc.setFontStyle("normal");
		doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
		doc.setFontSize(12);
		doc.text('<%=name%>', 110, 50, 'center');
		doc.setFontSize(20);
		doc.text('<%=title2%>',110,60, 'center');
		doc.setFontSize(12);
		var asofdate=moment(<%=plant %>toDateFormatted).format('MMM DD,YYYY');
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
		var groups_1=<%=plant %>table_1.getGroups();
		var groups_2=<%=plant %>table_2.getGroups();
		var groups_3=<%=plant %>table_3.getGroups();
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
		var totalasset=$('#<%=plant %>totalasset').html();
		htmlTable+="<tr><td class='calculated'>Total Assets:</td><td class='calculated'>"+totalasset+"</td></tr>";
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
		var totalliability=$('#<%=plant %>totalliability').html();
		htmlTable+="<tr><td class='calculated'>Total Liabilities:</td><td class='calculated'>"+totalliability+"</td></tr>";
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
		var profitloss=$('#<%=plant %>profitloss').html();
		var totalequity=$('#<%=plant %>totalequity').html();
		var totalvalue=$('#<%=plant %>totalvalue').html();
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
					    }
					}
		
		<%}%>
		
		
		
		// Total page number plugin only available in jspdf v1.0+
		if (typeof doc.putTotalPages === 'function') {
			doc.putTotalPages(totalPagesExp);
		}
		doc.save('BalanceSheet.pdf');

	}
	
	$("#sendemail").click(function(){
		//$("#common_email_modal #send_subject).val("Your Balance Sheet Report");
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
		var <%=plant %>data = <%=plant %>table_1.getData();
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
		var fromDate=moment(<%=plant %>fromDateFormatted).format('DD MMM YYYY');
		var toDate=moment(<%=plant %>toDateFormatted).format('DD MMM YYYY');
		doc.setFontSize(17);
		doc.setFontStyle("normal");
		doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
		doc.setFontSize(12);
		doc.text('<%=name%>', 110, 50, 'center');
		doc.setFontSize(20);
		doc.text('<%=title2%>',110,60, 'center');
		doc.setFontSize(12);
		var asofdate=moment(<%=plant %>toDateFormatted).format('MMM DD,YYYY');
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
		var groups_1=<%=plant %>table_1.getGroups();
		var groups_2=<%=plant %>table_2.getGroups();
		var groups_3=<%=plant %>table_3.getGroups();
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
		var totalasset=$('#<%=plant %>totalasset').html();
		htmlTable+="<tr><td class='calculated'>Total Assets:</td><td class='calculated'>"+totalasset+"</td></tr>";
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
		var totalliability=$('#<%=plant %>totalliability').html();
		htmlTable+="<tr><td class='calculated'>Total Liabilities:</td><td class='calculated'>"+totalliability+"</td></tr>";
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
		var profitloss=$('#<%=plant %>profitloss').html();
		var totalequity=$('#<%=plant %>totalequity').html();
		var totalvalue=$('#<%=plant %>totalvalue').html();
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
	
	function downTBExcel()
	{
		var fromDateFormatted;
		var toDateFormatted;
		var plist = $('input[name ="plantlist"]').val();
		console.log("---ffff------"+plist);
		plist = plist.toString();
		console.log("----dddd-----"+plist);
		<%for (ParentChildCmpDet plist : plantlist) {
				String plant = plist.getCHILD_PLANT();%>
	fromDateFormatted = <%=plant%>fromDateFormatted;
	toDateFormatted = <%=plant%>toDateFormatted
	<%}%>
		window.location = "/track/ParentBalanceSheetServlet?action=getBalanceSheetExcel&fromDate="+fromDateFormatted+"&toDate="+toDateFormatted+"&plist="+plist;
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
