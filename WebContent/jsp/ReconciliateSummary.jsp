<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%
	DateUtils _dateUtils = new DateUtils();
	String title = "Bank Reconciliation Summary";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	String curDate = DateUtils.getDate();
	
	
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String zeroval = StrUtils.addZeroes(0, numberOfDecimal);
	
	ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
	Map plntMap = (Map) plntList.get(0);
	String PLNTDESC = (String) plntMap.get("PLNTDESC");
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value=""/>
	<jsp:param name="submenu" value=""/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/ReconciliateSummary.js"></script>
<link href="../jsp/css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="../jsp/js/tabulator.min.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<style>
.extraInfo {
    border: 1px dashed #555;
    background-color: #f9f8f8;
    border-radius: 3px;
    color: #555;
    padding: 15px;
}
.offset-lg-7 {
    margin-left: 58.33333%;
}
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td, #table3>tbody>tr>td {
	border: none;
}

#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}

/* Style the tab */
.tab {
  overflow: hidden;
  border: 1px solid #ccc;
  background-color: #f1f1f1;
  line-height: 0.5;
}

/* Style the buttons that are used to open the tab content */
.tab button {
  background-color: inherit;
  float: left;
  border: none;
  outline: none;
  cursor: pointer;
  padding: 14px 16px;
  transition: 0.3s;
}

/* Change background color of buttons on hover */
.tab button:hover {
  background-color: #ddd;
}

/* Create an active/current tablink class */
.tab button.active {
  background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
  display: none;
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-top: none;
}
.payment-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
.voucher-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}

/* ----------------------------------------------- */

ul.simple-pagination {
	list-style: none;
}

.simple-pagination {
	display: block;
	overflow: hidden;
	padding: 0 5px 5px 0;
	margin: 0;
}

.simple-pagination ul {
	list-style: none;
	padding: 0;
	margin: 0;
}

.simple-pagination li {
	list-style: none;
	padding: 0;
	margin: 0;
	float: left;
}

/*------------------------------------*\
	Compact Theme Styles
\*------------------------------------*/

.compact-theme a, .compact-theme span {
	float: left;
	color: #333;
	font-size:14px;
	line-height:24px;
	font-weight: normal;
	text-align: center;
	border: 1px solid #AAA;
	border-right: none;
	min-width: 14px;
	padding: 0 7px;
	box-shadow: 2px 2px 2px rgba(0,0,0,0.2);
	background: #efefef; /* Old browsers */
	background: -moz-linear-gradient(top, #ffffff 0%, #efefef 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#ffffff), color-stop(100%,#efefef)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #ffffff 0%,#efefef 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #ffffff 0%,#efefef 100%); /* Opera11.10+ */
	background: -ms-linear-gradient(top, #ffffff 0%,#efefef 100%); /* IE10+ */
	background: linear-gradient(top, #ffffff 0%,#efefef 100%); /* W3C */
}

.compact-theme a:hover {
	text-decoration: none;
	background: #efefef; /* Old browsers */
	background: -moz-linear-gradient(top, #efefef 0%, #bbbbbb 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#efefef), color-stop(100%,#bbbbbb)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #efefef 0%,#bbbbbb 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #efefef 0%,#bbbbbb 100%); /* Opera11.10+ */
	background: -ms-linear-gradient(top, #efefef 0%,#bbbbbb 100%); /* IE10+ */
	background: linear-gradient(top, #efefef 0%,#bbbbbb 100%); /* W3C */
}

.compact-theme .prev {
	border-radius: 3px 0 0 3px;
}

.compact-theme .next {
	border-right: 1px solid #AAA;
	border-radius: 0 3px 3px 0;
}

.compact-theme .current {
	background: #bbbbbb; /* Old browsers */
	background: -moz-linear-gradient(top, #bbbbbb 0%, #efefef 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#bbbbbb), color-stop(100%,#efefef)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #bbbbbb 0%,#efefef 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #bbbbbb 0%,#efefef 100%); /* Opera11.10+ */
	background: -ms-linear-gradient(top, #bbbbbb 0%,#efefef 100%); /* IE10+ */
	background: linear-gradient(top, #bbbbbb 0%,#efefef 100%); /* W3C */
	cursor: default;
}

.compact-theme .ellipse {
	background: #EAEAEA;
	padding: 0 10px;
	cursor: default;
}

/*------------------------------------*\
	Light Theme Styles
\*------------------------------------*/

.light-theme a, .light-theme span {
	float: left;
	color: #666;
	font-size:14px;
	line-height:24px;
	font-weight: normal;
	text-align: center;
	border: 1px solid #BBB;
	min-width: 14px;
	padding: 0 7px;
	margin: 0 5px 0 0;
	border-radius: 3px;
	box-shadow: 0 1px 2px rgba(0,0,0,0.2);
	background: #efefef; /* Old browsers */
	background: -moz-linear-gradient(top, #ffffff 0%, #efefef 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#ffffff), color-stop(100%,#efefef)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #ffffff 0%,#efefef 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #ffffff 0%,#efefef 100%); /* Opera11.10+ */
	background: -ms-linear-gradient(top, #ffffff 0%,#efefef 100%); /* IE10+ */
	background: linear-gradient(top, #ffffff 0%,#efefef 100%); /* W3C */
}

.light-theme a:hover {
	text-decoration: none;
	background: #FCFCFC;
}

.light-theme .current {
	background: #666;
	color: #FFF;
	border-color: #444;
	box-shadow: 0 1px 0 rgba(255,255,255,1), 0 0 2px rgba(0, 0, 0, 0.3) inset;
	cursor: default;
}

.light-theme .ellipse {
	background: none;
	border: none;
	border-radius: 0;
	box-shadow: none;
	font-weight: bold;
	cursor: default;
}

/*------------------------------------*\
	Dark Theme Styles
\*------------------------------------*/

.dark-theme a, .dark-theme span {
	float: left;
	color: #CCC;
	font-size:14px;
	line-height:24px;
	font-weight: normal;
	text-align: center;
	border: 1px solid #222;
	min-width: 14px;
	padding: 0 7px;
	margin: 0 5px 0 0;
	border-radius: 3px;
	box-shadow: 0 1px 2px rgba(0,0,0,0.2);
	background: #555; /* Old browsers */
	background: -moz-linear-gradient(top, #555 0%, #333 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#555), color-stop(100%,#333)); /* Chrome,Safari4+ */
	background: -webkit-linear-gradient(top, #555 0%,#333 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(top, #555 0%,#333 100%); /* Opera11.10+ */
	background: -ms-linear-gradient(top, #555 0%,#333 100%); /* IE10+ */
	background: linear-gradient(top, #555 0%,#333 100%); /* W3C */
}

.dark-theme a:hover {
	text-decoration: none;
	background: #444;
}

.dark-theme .current {
	background: #222;
	color: #FFF;
	border-color: #000;
	box-shadow: 0 1px 0 rgba(255,255,255,0.2), 0 0 1px 1px rgba(0, 0, 0, 0.1) inset;
	cursor: default;
}

.dark-theme .ellipse {
	background: none;
	border: none;
	border-radius: 0;
	box-shadow: none;
	font-weight: bold;
	cursor: default;
}
/* ----------------------------------------------- */

</style>
<center>
	<h2>
		<small class="success-msg"><%=fieldDesc%></small>
	</h2>
</center>

<div class="container-fluid m-t-20">

<!-- <div class="tab">
  		<button class="tablinks active" onclick="openPaymentprocess(event, 'issue')">PDC Payment</button>
 		 <button class="tablinks" onclick="openPaymentprocess(event, 'receive')">PDC Payment Receive</button>
 	</div> -->
	<!-- <div id="issue" class="tabcontent active" style="display: block;"> -->
	<div class="box">
	<!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../accounting/reports"><span class="underline-on-hover">Accounting Reports</span></a></li>	
                <li><label>Bank Reconciliation Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
	            	<button type="button" class="btn btn-default" onClick="window.location.href='../accountant/bankreconcilation'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);margin-right: 15px;" >Process Reconciliation</button>&nbsp;             
	              	<button type="button" id="sendemail" class="btn btn-default"
									data-toggle="tooltip" data-placement="bottom" title="Email">
									<i class="fa fa-envelope-o" aria-hidden="true"></i>
					</button>
	              	<button type="button" id="pdfdownload" class="btn btn-default"
									data-toggle="tooltip" data-placement="bottom" title="PDF">
									<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<button type="button" id="exceldownload" class="btn btn-default" data-toggle="tooltip" data-placement="bottom" title="Excel">
						<a  href="javascript:downTBExcel()" id="tbsexcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>						
					</button>
	              	<button type="button" class="btn btn-default printMe" data-toggle="tooltip" data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
	            </div>
				<h1 style="font-size: 18px; cursor: pointer;"
					class="box-title pull-right"
				onclick="window.location.href='../accounting/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>




		<div class="container-fluid">
			<form class="form-horizontal" name="form" method="post" action="">
				<input type="hidden" name="plant" value="<%=plant%>" > <input
					type="number" id="numberOfDecimal" style="display: none;"
					value=<%=numberOfDecimal%>> <input type="hidden"
					name="LOGIN_USER" value="<%=username%>" >
					<input type="hidden" name="paidthrough" value="" >
					<input type="hidden" name="actcode" value="" >
					<input type="hidden" name="datestatus" value="rdate" >

					<div class="form-group" style="margin-left: 0%;">
						<div class="row">
							
							<div class="col-sm-4">
								<input id="paid_through_account_name" name="paid_through_account_name"
									placeholder="Select Account" value="" class="form-control text-left"
									type="text" required> <span class="pay-select-icon"
									onclick="$(this).parent().find('input[name=\'paid_through_account_name\']').focus()">
								</span>
							</div>
							<div class="col-sm-2">
								<INPUT name="F_DATE" type="text" size="30" MAXLENGTH=20
									class="form-control datepicker" READONLY
									placeholder="FROM DATE">
							</div>
							<div class="col-sm-2">
								<INPUT class="form-control datepicker" name="T_DATE"
									type="text" size="30" MAXLENGTH=20 READONLY
									placeholder="TO DATE">
							</div> 
							<div class="col-sm-4">
							</div> 
							<div class="col-sm-12" style="margin-top:2%">
								<input type="radio" id="rdate" checked name="SDate" value="rdate">
								<label for="rdate">Date</label>
								<input type="radio" id="idate" name="SDate" value="idate">
								<label for="idate">Instrument Date</label>
							</div>
							<div class="col-sm-12" style="margin-top:2%">
								<button type="button" onclick="accountchanged()" class="btn btn-success">Search</button>
							</div>

						</div>
					</div>

				<div id="print_id" class="table-responsive">
					<table id="tablerec"
						class="table table-bordred table-striped rec-table">
						<thead>
							<tr>
								<th style="font-size: smaller;">DATE</th>
								<th style="font-size: smaller;">PARTICULARS</th>
								<th style="font-size: smaller;">VOUCHER TYPE</th>
								<th style="font-size: smaller;">INSTRUMENT DATE</th>
								<th style="font-size: smaller;">BANK DATE</th>
								<th style="font-size: smaller;">DEBIT</th>
								<th style="font-size: smaller;">CREDIT</th>
								
							</tr>
						</thead>
						<tbody id="recbody">
								
						</tbody>
						
					</table>
					
				</div>
				<div class="pagination-page"></div>
				<div class="row">
					<div class="col-xs-12">
						<table class="table text-right">
							<tbody>
								<tr>
									<td>Balance as per company books</td>
									<td>:</td>
									<td id="cbookbal"><%=zeroval %></td>
								</tr>
								<tr>
									<td>Amounts not reflected in bank</td>
									<td>:</td>
									<td id="cbookpending"><%=zeroval %></td>
								</tr>
								<tr>
									<td>Balance as per bank</td>
									<td>:</td>
									<td  id="bankbal"><%=zeroval %></td>
								</tr>
								
							</tbody>
						</table>
					</div>
				</div>
				<!-- <div class="row">
					<div class="col-sm-12 text-center" style="margin-bottom: 2%;">
						<button type="button"
							onclick="recsubmit();"
							class="btn btn-success">Process Reconciliate</button>
					</div>
				</div>  -->
			</form>
		</div>
	</div>
<!-- </div> -->

<!-- <div id="receive" class="tabcontent">
<div class="box"> 
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">PDC Payment Receive Process</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>

</div>
</div> -->
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
$(document).ready(function(){
	$("#rdate").click(function(){
		$("input[name ='datestatus']").val("rdate");
	});

	$("#idate").click(function(){
		$("input[name ='datestatus']").val("idate");
	});
});

$('.printMe').click(function(){
	printdoc();
});

$("#sendemail").click(function(){
	//$("#common_email_modal #send_subject).val("Your Profit Loss Report");
	$('#common_email_modal').modal('toggle');
	loadBodyStyleREC();
});

function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
}	


function generatePdfMail(dataUrl,attachName){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var accountid = $("input[name ='paidthrough']").val();
	if(accountid == ""){
		 alert ("Please select a account.");
		 return false;
	}
	var fromdate =  $("input[name ='F_DATE']").val();
	var todate =  $("input[name ='T_DATE']").val();
	var datestatus =  $("input[name ='datestatus']").val();
    if(accountid!=""||accountid!=null)
    	{
			var urlStr = "/track/JournalServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				data: {
					"action":"Get_Reconciliation_Summary",
					"fromDate":fromdate,
					"toDate":todate,
					"datestatus":datestatus,
					"AccountId":accountid
				}, 
		        success: function (data) {
		        	
		        	
		        	var doc = new jsPDF('p', 'mm', 'a4');
		        	var pageNumber;

		        	doc.setFontSize(17);
		        	doc.setFontStyle("normal");
		        	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
		        	
		        	doc.setFontSize(12);
		        	doc.text('<%=PLNTDESC%>', 87, 50);
		        	doc.setFontSize(20);
		        	doc.setFontStyle("bold");
		        	doc.text('<%=title%>',50,60);
		        	doc.setFontSize(10);
		        	doc.setFontStyle("bold");
		        	doc.setFontSize(12);
		        	doc.setFontStyle("normal");
		        	doc.text("Account: "+$("input[name ='actcode']").val()+" "+$("input[name ='paid_through_account_name']").val(), 16 , 70);
		        	/* **** */
		        	var totalPagesExp = "{total_pages_cont_string}";
		        	
		        	var htmlTable="<thead><tr><th>DATE</th><th>PARTICULARS</th><th>VOUCHER TYPE</th><th>INSTRUMENT DATE</th><th>BANK DATE</th><th>DEBIT</th><th>CREDIT</th></tr></thead><tbody>";
		        	
		        	
		        	var json = $.parseJSON(data);
					var recList=json.REC;
		        	console.log(recList);
		        	if(recList.length>0){
		        		$.each(recList,function(i,r){
		        			var zeroval = parseFloat("0").toFixed(numberOfDecimal);
		        			htmlTable += '<tr>';
		        			htmlTable += '<td>'+r.DATE+'</td>';
		        			htmlTable += '<td>'+r.ACCOUNT+'</td>';
		        			htmlTable += '<td>'+r.VOUCHERTYPE+'</td>';
		        			htmlTable += '<td>'+r.INSTRUMENTDATE+'</td>';
		        			htmlTable += '<td>'+r.BANKDATE+'</td>';
		        			htmlTable += '<td>'+parseFloat(r.DEBIT).toFixed(numberOfDecimal)+'</td>';
		        			htmlTable += '<td>'+parseFloat(r.CREDIT).toFixed(numberOfDecimal)+'</td>';
		        			htmlTable += '</tr>';
		        		});
		        	}
		        	
		        	/* $('#cbookbal').text(parseFloat(json.CBOOK).toFixed(numberOfDecimal));
	        		$('#cbookpending').text(parseFloat(json.PENDINGS).toFixed(numberOfDecimal));
	        		$('#bankbal').text(parseFloat(json.BANK).toFixed(numberOfDecimal)); */
	        		htmlTable += '<tr>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			htmlTable += '<td colspan="2"  style="text-align:right">Balance as per company books</td>';
        			htmlTable += '<td colspan="2"  style="text-align:right">'+parseFloat(json.CBOOK).toFixed(numberOfDecimal)+'</td>';
        			htmlTable += '</tr>';
        			
        			htmlTable += '<tr>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">Amounts not reflected in bank</td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">'+parseFloat(json.PENDINGS).toFixed(numberOfDecimal)+'</td>';
        			htmlTable += '</tr>';
        			
        			htmlTable += '<tr>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">Balance as per bank</td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">'+parseFloat(json.BANK).toFixed(numberOfDecimal)+'</td>';
        			htmlTable += '</tr>';
	        		
		        	htmlTable+="</tbody>";
		        	var tableElement=document.createElement('table');
		        	tableElement.innerHTML=htmlTable;
		        	doc.autoTable({
		        		html : tableElement,
		        		startY : 75,
		        		columnStyles : {0 : {halign : 'left'},1 : {halign : 'left'},2:{halign : 'left'},3:{halign : 'left'},4:{halign : 'left'},5:{halign : 'left'},6:{halign : 'left'}},
		        		theme : 'plain',
		        		/*  styles: {
		        		        fontSize: 6,
		        		    }, */
		        		headStyles : {
		        			fillColor : [ 255, 255, 255 ],
		        			textColor : [ 0, 0, 0 ]
		        		},
		        		bodyStyles : {
		        			fillColor : [ 255, 255, 255 ],
		        			textColor : [ 0, 0, 0 ],
		        			fontSize: number = 8
		        		},
		        		didParseCell: function (data) {
		        			/* if (data.column.index === 0) { */
		        				data.cell.styles.halign = 'left';
		        			/* }
		        			else{
		        				data.cell.styles.halign = 'right';
		        			} */
		        		},
		        		willDrawCell: function (data) {
		                    // Colspan
		                   // console.log(data.cell.raw.className);
		                    if (data.cell.raw.className=="innerHeader") {
		                    	//console.log("Entered");
		                    	doc.setFontStyle('bold');
		                        doc.setFontSize(8);
		                        doc.setTextColor(0, 0, 0);
		                        doc.setFillColor(230, 229, 229);
		        		}
		                    else if(data.cell.raw.className=="calculated")
		                    	{
		                    		doc.setFontStyle('bold');
		                        	doc.setFontSize(8);
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
		        	
		        	const pdf = new File([doc.output("blob")], attachName+".pdf", {  type: "pdf" }),
		        	formData = new FormData();

		        	formData.append("file", pdf);
		        	progressBar();
		        	sendMailTemplate(formData);
		        },
		        cache: false
			});
    	}
	
}

function printdoc()
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfPrint(dataUrl);
		  	},'image/jpeg');
}

$("#pdfdownload").click(function(){
	    generate();
});

function generate()
{
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

/* $("#exceldownload").click(function(){
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var accountid = $("input[name ='paidthrough']").val();
	if(accountid == ""){
		 alert ("Please select a account.");
		 return false;
	}
	var fromdate =  $("input[name ='F_DATE']").val();
	var todate =  $("input[name ='T_DATE']").val();
	var datestatus =  $("input[name ='datestatus']").val();
    if(accountid!=""||accountid!=null)
    	{
			var urlStr = "/track/JournalServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				data: {
					"action":"Get_Reconciliation_excel",
					"fromDate":fromdate,
					"toDate":todate,
					"datestatus":datestatus,
					"AccountId":accountid
				}, 
		        success: function (data) {
					console.log(data);
				}
			});   
    	}   
}); */

function downTBExcel()
{
	var accountid = $("input[name ='paidthrough']").val();
	if(accountid == ""){
		 alert ("Please select a account.");
		 return false;
	}
	var fromdate =  $("input[name ='F_DATE']").val();
	var todate =  $("input[name ='T_DATE']").val();
	var datestatus =  $("input[name ='datestatus']").val();
	var aname =  $("input[name ='paid_through_account_name']").val();
	var acode =  $("input[name ='actcode']").val();
	window.location = "/track/JournalServlet?action=Get_Reconciliation_excel&fromDate="+fromdate+"&toDate="+todate+"&datestatus="+datestatus+"&AccountId="+accountid+"&aname="+aname+"&acode="+acode;
}


function generatePdfPrint(dataUrl){	

	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var accountid = $("input[name ='paidthrough']").val();
	if(accountid == ""){
		 alert ("Please select a account.");
		 return false;
	}
	var fromdate =  $("input[name ='F_DATE']").val();
	var todate =  $("input[name ='T_DATE']").val();
	var datestatus =  $("input[name ='datestatus']").val();
    if(accountid!=""||accountid!=null)
    	{
			var urlStr = "/track/JournalServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				data: {
					"action":"Get_Reconciliation_Summary",
					"fromDate":fromdate,
					"toDate":todate,
					"datestatus":datestatus,
					"AccountId":accountid
				}, 
		        success: function (data) {
		        	
		        	
		        	var doc = new jsPDF('p', 'mm', 'a4');
		        	var pageNumber;

		        	doc.setFontSize(17);
		        	doc.setFontStyle("normal");
		        	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
		        	
		        	doc.setFontSize(12);
		        	doc.text('<%=PLNTDESC%>', 87, 50);
		        	doc.setFontSize(20);
		        	doc.setFontStyle("bold");
		        	doc.text('<%=title%>',50,60);
		        	doc.setFontSize(10);
		        	doc.setFontStyle("bold");
		        	doc.setFontSize(12);
		        	doc.setFontStyle("normal");
		        	//doc.text("Account Name: "+$("input[name ='paid_through_account_name']").val(), 16 , 70);
		        	doc.text("Account: "+$("input[name ='actcode']").val()+" "+$("input[name ='paid_through_account_name']").val(), 16 , 70);
		        	/* **** */
		        	var totalPagesExp = "{total_pages_cont_string}";
		        	
		        	var htmlTable="<thead><tr><th>DATE</th><th>PARTICULARS</th><th>VOUCHER TYPE</th><th>INSTRUMENT DATE</th><th>BANK DATE</th><th>DEBIT</th><th>CREDIT</th></tr></thead><tbody>";
		        	
		        	
		        	var json = $.parseJSON(data);
					var recList=json.REC;
		        	console.log(recList);
		        	if(recList.length>0){
		        		$.each(recList,function(i,r){
		        			var zeroval = parseFloat("0").toFixed(numberOfDecimal);
		        			htmlTable += '<tr>';
		        			htmlTable += '<td>'+r.DATE+'</td>';
		        			htmlTable += '<td>'+r.ACCOUNT+'</td>';
		        			htmlTable += '<td>'+r.VOUCHERTYPE+'</td>';
		        			htmlTable += '<td>'+r.INSTRUMENTDATE+'</td>';
		        			htmlTable += '<td>'+r.BANKDATE+'</td>';
		        			htmlTable += '<td>'+parseFloat(r.DEBIT).toFixed(numberOfDecimal)+'</td>';
		        			htmlTable += '<td>'+parseFloat(r.CREDIT).toFixed(numberOfDecimal)+'</td>';
		        			htmlTable += '</tr>';
		        		});
		        	}
		        	
		        	/* $('#cbookbal').text(parseFloat(json.CBOOK).toFixed(numberOfDecimal));
	        		$('#cbookpending').text(parseFloat(json.PENDINGS).toFixed(numberOfDecimal));
	        		$('#bankbal').text(parseFloat(json.BANK).toFixed(numberOfDecimal)); */
	        		htmlTable += '<tr>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			htmlTable += '<td colspan="2"  style="text-align:right">Balance as per company books</td>';
        			htmlTable += '<td colspan="2"  style="text-align:right">'+parseFloat(json.CBOOK).toFixed(numberOfDecimal)+'</td>';
        			htmlTable += '</tr>';
        			
        			htmlTable += '<tr>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">Amounts not reflected in bank</td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">'+parseFloat(json.PENDINGS).toFixed(numberOfDecimal)+'</td>';
        			htmlTable += '</tr>';
        			
        			htmlTable += '<tr>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">Balance as per bank</td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">'+parseFloat(json.BANK).toFixed(numberOfDecimal)+'</td>';
        			htmlTable += '</tr>';
	        		
		        	htmlTable+="</tbody>";
		        	var tableElement=document.createElement('table');
		        	tableElement.innerHTML=htmlTable;
		        	doc.autoTable({
		        		html : tableElement,
		        		startY : 75,
		        		columnStyles : {0 : {halign : 'left'},1 : {halign : 'left'},2:{halign : 'left'},3:{halign : 'left'},4:{halign : 'left'},5:{halign : 'left'},6:{halign : 'left'}},
		        		theme : 'plain',
		        		/*  styles: {
		        		        fontSize: 6,
		        		    }, */
		        		headStyles : {
		        			fillColor : [ 255, 255, 255 ],
		        			textColor : [ 0, 0, 0 ]
		        		},
		        		bodyStyles : {
		        			fillColor : [ 255, 255, 255 ],
		        			textColor : [ 0, 0, 0 ],
		        			fontSize: number = 8
		        		},
		        		didParseCell: function (data) {
		        			/* if (data.column.index === 0) { */
		        				data.cell.styles.halign = 'left';
		        			/* }
		        			else{
		        				data.cell.styles.halign = 'right';
		        			} */
		        		},
		        		willDrawCell: function (data) {
		                    // Colspan
		                   // console.log(data.cell.raw.className);
		                    if (data.cell.raw.className=="innerHeader") {
		                    	//console.log("Entered");
		                    	doc.setFontStyle('bold');
		                        doc.setFontSize(8);
		                        doc.setTextColor(0, 0, 0);
		                        doc.setFillColor(230, 229, 229);
		        		}
		                    else if(data.cell.raw.className=="calculated")
		                    	{
		                    		doc.setFontStyle('bold');
		                        	doc.setFontSize(8);
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
		        },
		        cache: false
			});
    	}
	
}

function generatePdf(dataUrl){	

	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var accountid = $("input[name ='paidthrough']").val();
	if(accountid == ""){
		 alert ("Please select a account.");
		 return false;
	}
	var fromdate =  $("input[name ='F_DATE']").val();
	var todate =  $("input[name ='T_DATE']").val();
	var datestatus =  $("input[name ='datestatus']").val();
    if(accountid!=""||accountid!=null)
    	{
			var urlStr = "/track/JournalServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				data: {
					"action":"Get_Reconciliation_Summary",
					"fromDate":fromdate,
					"toDate":todate,
					"datestatus":datestatus,
					"AccountId":accountid
				}, 
		        success: function (data) {
		        	
		        	
		        	var doc = new jsPDF('p', 'mm', 'a4');
		        	var pageNumber;

		        	doc.setFontSize(17);
		        	doc.setFontStyle("normal");
		        	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
		        	
		        	doc.setFontSize(12);
		        	doc.text('<%=PLNTDESC%>', 87, 50);
		        	doc.setFontSize(20);
		        	doc.setFontStyle("bold");
		        	doc.text('<%=title%>',50,60);
		        	doc.setFontSize(10);
		        	doc.setFontStyle("bold");
		        	doc.setFontSize(12);
		        	doc.setFontStyle("normal");
		        	//doc.text("Account Name: "+$("input[name ='paid_through_account_name']").val(), 16 , 70);
		        	doc.text("Account: "+$("input[name ='actcode']").val()+" "+$("input[name ='paid_through_account_name']").val(), 16 , 70);
		        	/* **** */
		        	var totalPagesExp = "{total_pages_cont_string}";
		        	
		        	var htmlTable="<thead><tr><th>DATE</th><th>PARTICULARS</th><th>VOUCHER TYPE</th><th>INSTRUMENT DATE</th><th>BANK DATE</th><th>DEBIT</th><th>CREDIT</th></tr></thead><tbody>";
		        	
		        	
		        	var json = $.parseJSON(data);
					var recList=json.REC;
		        	console.log(recList);
		        	if(recList.length>0){
		        		$.each(recList,function(i,r){
		        			var zeroval = parseFloat("0").toFixed(numberOfDecimal);
		        			htmlTable += '<tr>';
		        			htmlTable += '<td>'+r.DATE+'</td>';
		        			htmlTable += '<td>'+r.ACCOUNT+'</td>';
		        			htmlTable += '<td>'+r.VOUCHERTYPE+'</td>';
		        			htmlTable += '<td>'+r.INSTRUMENTDATE+'</td>';
		        			htmlTable += '<td>'+r.BANKDATE+'</td>';
		        			htmlTable += '<td>'+parseFloat(r.DEBIT).toFixed(numberOfDecimal)+'</td>';
		        			htmlTable += '<td>'+parseFloat(r.CREDIT).toFixed(numberOfDecimal)+'</td>';
		        			htmlTable += '</tr>';
		        		});
		        	}
		        	
		        	/* $('#cbookbal').text(parseFloat(json.CBOOK).toFixed(numberOfDecimal));
	        		$('#cbookpending').text(parseFloat(json.PENDINGS).toFixed(numberOfDecimal));
	        		$('#bankbal').text(parseFloat(json.BANK).toFixed(numberOfDecimal)); */
	        		
	        		htmlTable += '<tr>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			htmlTable += '<td colspan="2"  style="text-align:right">Balance as per company books</td>';
        			htmlTable += '<td colspan="2"  style="text-align:right">'+parseFloat(json.CBOOK).toFixed(numberOfDecimal)+'</td>';
        			htmlTable += '</tr>';
        			
        			htmlTable += '<tr>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">Amounts not reflected in bank</td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">'+parseFloat(json.PENDINGS).toFixed(numberOfDecimal)+'</td>';
        			htmlTable += '</tr>';
        			
        			htmlTable += '<tr>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			//htmlTable += '<td></td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">Balance as per bank</td>';
        			htmlTable += '<td  colspan="2"  style="text-align:right">'+parseFloat(json.BANK).toFixed(numberOfDecimal)+'</td>';
        			htmlTable += '</tr>';
	        		
		        	htmlTable+="</tbody>";
		        	var tableElement=document.createElement('table');
		        	tableElement.innerHTML=htmlTable;
		        	doc.autoTable({
		        		html : tableElement,
		        		startY : 75,
		        		columnStyles : {0 : {halign : 'left'},1 : {halign : 'left'},2:{halign : 'left'},3:{halign : 'left'},4:{halign : 'left'},5:{halign : 'left'},6:{halign : 'left'}},
		        		theme : 'plain',
		        		/*  styles: {
		        		        fontSize: 6,
		        		    }, */
		        		headStyles : {
		        			fillColor : [ 255, 255, 255 ],
		        			textColor : [ 0, 0, 0 ]
		        		},
		        		bodyStyles : {
		        			fillColor : [ 255, 255, 255 ],
		        			textColor : [ 0, 0, 0 ],
		        			fontSize: number = 8
		        		},
		        		didParseCell: function (data) {
		        			/* if (data.column.index === 0) { */
		        				data.cell.styles.halign = 'left';
		        			/* }
		        			else{
		        				data.cell.styles.halign = 'right';
		        			} */
		        		},
		        		willDrawCell: function (data) {
		                    // Colspan
		                   // console.log(data.cell.raw.className);
		                    if (data.cell.raw.className=="innerHeader") {
		                    	//console.log("Entered");
		                    	doc.setFontStyle('bold');
		                        doc.setFontSize(8);
		                        doc.setTextColor(0, 0, 0);
		                        doc.setFillColor(230, 229, 229);
		        		}
		                    else if(data.cell.raw.className=="calculated")
		                    	{
		                    		doc.setFontStyle('bold');
		                        	doc.setFontSize(8);
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
		        	
		        	doc.save('Bank Reconciliation.pdf');
		        },
		        cache: false
			});
    	}
	
}
	 
function accountchanged()
{
	$("#recbody").html("");
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var accountid = $("input[name ='paidthrough']").val();
	if(accountid == ""){
		 alert ("Please select a account.");
		 return false;
	}
	var fromdate =  $("input[name ='F_DATE']").val();
	var todate =  $("input[name ='T_DATE']").val();
	var datestatus =  $("input[name ='datestatus']").val();
    if(accountid!=""||accountid!=null)
    	{
			var urlStr = "/track/JournalServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				data: {
					"action":"Get_Reconciliation_Summary",
					"fromDate":fromdate,
					"toDate":todate,
					"datestatus":datestatus,
					"AccountId":accountid
				}, 
		        success: function (data) {
		        	var json = $.parseJSON(data);
					var recList=json.REC;
		        	console.log(recList);
		        	if(recList.length>0){
		        		$.each(recList,function(i,r){
		        			var zeroval = parseFloat("0").toFixed(numberOfDecimal);
		        			var body="";
		        			body += '<tr>';
		        			body += '<td class="text-left">'+r.DATE+'</td>';
		        			body += '<td class="text-left">'+r.ACCOUNT+'</td>';
		        			body += '<td class="text-left">'+r.VOUCHERTYPE+'</td>';
		        			body += '<td class="text-left">'+r.INSTRUMENTDATE+'</td>';
		        			body += '<td class="text-left">'+r.BANKDATE+'</td>';
		        		//	body += '<input class="form-control datepicker" type="text" name="bankdate" placeholder="Enter Bank Date">';
		        		//	body += '<input type="text" name="jid" value="'+r.ID+'" hidden>';
		        		//	body += '</td>';
		        			body += '<td class="text-left">'+parseFloat(r.DEBIT).toFixed(numberOfDecimal)+'</td>';
		        			body += '<td class="text-left">'+parseFloat(r.CREDIT).toFixed(numberOfDecimal)+'</td>';
		        			body += '</tr>';
		        			$(".rec-table tbody").append(body);
		        			addrowclasses();
		        		});
		        	}else{
		        		$("#recbody").html("");
		        	}
		        	
		        	$('#cbookbal').text(parseFloat(json.CBOOK).toFixed(numberOfDecimal));
	        		$('#cbookpending').text(parseFloat(json.PENDINGS).toFixed(numberOfDecimal));
	        		$('#bankbal').text(parseFloat(json.BANK).toFixed(numberOfDecimal));
		        },
		        cache: false
			});
    	}
}



function addrowclasses(){
	var plant = document.form.plant.value;
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
	
	jQuery(function($) {
	    // Consider adding an ID to your table
	    // incase a second table ever enters the picture.
	    var items = $("#tablerec tbody tr");

	    var numItems = items.length;
	    var perPage = 10;

	    // Only show the first 2 (or first `per_page`) items initially.
	    items.slice(perPage).hide();

	    // Now setup the pagination using the `.pagination-page` div.
	    $(".pagination-page").pagination({
	        items: numItems,
	        itemsOnPage: perPage,
	        cssStyle: "light-theme",

	        // This is the actual page changing functionality.
	        onPageClick: function(pageNumber) {
	            // We need to show and hide `tr`s appropriately.
	            var showFrom = perPage * (pageNumber - 1);
	            var showTo = showFrom + perPage;

	            // We'll first hide everything...
	            items.hide()
	                 // ... and then only show the appropriate rows.
	                 .slice(showFrom, showTo).show();
	        }
	    });



	    // EDIT: Let's cover URL fragments (i.e. #page-3 in the URL).
	    // More thoroughly explained (including the regular expression) in: 
	    // https://github.com/bilalakil/bin/tree/master/simplepagination/page-fragment/index.html

	    // We'll create a function to check the URL fragment
	    // and trigger a change of page accordingly.
	    
	    
	   /*  function checkFragment() {
	        // If there's no hash, treat it like page 1.
	        var hash = window.location.hash || "#page-1";

	        // We'll use a regular expression to check the hash string.
	        hash = hash.match(/^#page-(\d+)$/);

	        if(hash) {
	            // The `selectPage` function is described in the documentation.
	            // We've captured the page number in a regex group: `(\d+)`.
	            $(".pagination-page").pagination("selectPage", parseInt(hash[1]));
	        }
	    };

	    // We'll call this function whenever back/forward is pressed...
	    $(window).bind("popstate", checkFragment);

	    // ... and we'll also call it when the page has loaded
	    // (which is right now).
	    checkFragment(); */
	});
}



</script>



<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

<script>

/**
* simplePagination.js v1.6
* A simple jQuery pagination plugin.
* http://flaviusmatis.github.com/simplePagination.js/
*
* Copyright 2012, Flavius Matis
* Released under the MIT license.
* http://flaviusmatis.github.com/license.html
*/

(function($){

	var methods = {
		init: function(options) {
			var o = $.extend({
				items: 1,
				itemsOnPage: 1,
				pages: 0,
				displayedPages: 5,
				edges: 2,
				currentPage: 1,
				hrefTextPrefix: '#page-',
				hrefTextSuffix: '',
				prevText: 'Prev',
				nextText: 'Next',
				ellipseText: '&hellip;',
				cssStyle: 'light-theme',
				labelMap: [],
				selectOnClick: true,
				onPageClick: function(pageNumber, event) {
					// Callback triggered when a page is clicked
					// Page number is given as an optional parameter
				},
				onInit: function() {
					// Callback triggered immediately after initialization
				}
			}, options || {});

			var self = this;

			o.pages = o.pages ? o.pages : Math.ceil(o.items / o.itemsOnPage) ? Math.ceil(o.items / o.itemsOnPage) : 1;
			o.currentPage = o.currentPage - 1;
			o.halfDisplayed = o.displayedPages / 2;

			this.each(function() {
				self.addClass(o.cssStyle + ' simple-pagination').data('pagination', o);
				methods._draw.call(self);
			});

			o.onInit();

			return this;
		},

		selectPage: function(page) {
			methods._selectPage.call(this, page - 1);
			return this;
		},

		prevPage: function() {
			var o = this.data('pagination');
			if (o.currentPage > 0) {
				methods._selectPage.call(this, o.currentPage - 1);
			}
			return this;
		},

		nextPage: function() {
			var o = this.data('pagination');
			if (o.currentPage < o.pages - 1) {
				methods._selectPage.call(this, o.currentPage + 1);
			}
			return this;
		},

		getPagesCount: function() {
			return this.data('pagination').pages;
		},

		getCurrentPage: function () {
			return this.data('pagination').currentPage + 1;
		},

		destroy: function(){
			this.empty();
			return this;
		},

		drawPage: function (page) {
			var o = this.data('pagination');
			o.currentPage = page - 1;
			this.data('pagination', o);
			methods._draw.call(this);
			return this;
		},

		redraw: function(){
			methods._draw.call(this);
			return this;
		},

		disable: function(){
			var o = this.data('pagination');
			o.disabled = true;
			this.data('pagination', o);
			methods._draw.call(this);
			return this;
		},

		enable: function(){
			var o = this.data('pagination');
			o.disabled = false;
			this.data('pagination', o);
			methods._draw.call(this);
			return this;
		},

		updateItems: function (newItems) {
			var o = this.data('pagination');
			o.items = newItems;
			o.pages = methods._getPages(o);
			this.data('pagination', o);
			methods._draw.call(this);
		},

		updateItemsOnPage: function (itemsOnPage) {
			var o = this.data('pagination');
			o.itemsOnPage = itemsOnPage;
			o.pages = methods._getPages(o);
			this.data('pagination', o);
			methods._selectPage.call(this, 0);
			return this;
		},

		_draw: function() {
			var	o = this.data('pagination'),
				interval = methods._getInterval(o),
				i,
				tagName;

			methods.destroy.call(this);
			
			tagName = (typeof this.prop === 'function') ? this.prop('tagName') : this.attr('tagName');

			var $panel = tagName === 'UL' ? this : $('<ul></ul>').appendTo(this);

			// Generate Prev link
			if (o.prevText) {
				methods._appendItem.call(this, o.currentPage - 1, {text: o.prevText, classes: 'prev'});
			}

			// Generate start edges
			if (interval.start > 0 && o.edges > 0) {
				var end = Math.min(o.edges, interval.start);
				for (i = 0; i < end; i++) {
					methods._appendItem.call(this, i);
				}
				if (o.edges < interval.start && (interval.start - o.edges != 1)) {
					$panel.append('<li class="disabled"><span class="ellipse">' + o.ellipseText + '</span></li>');
				} else if (interval.start - o.edges == 1) {
					methods._appendItem.call(this, o.edges);
				}
			}

			// Generate interval links
			for (i = interval.start; i < interval.end; i++) {
				methods._appendItem.call(this, i);
			}

			// Generate end edges
			if (interval.end < o.pages && o.edges > 0) {
				if (o.pages - o.edges > interval.end && (o.pages - o.edges - interval.end != 1)) {
					$panel.append('<li class="disabled"><span class="ellipse">' + o.ellipseText + '</span></li>');
				} else if (o.pages - o.edges - interval.end == 1) {
					methods._appendItem.call(this, interval.end++);
				}
				var begin = Math.max(o.pages - o.edges, interval.end);
				for (i = begin; i < o.pages; i++) {
					methods._appendItem.call(this, i);
				}
			}

			// Generate Next link
			if (o.nextText) {
				methods._appendItem.call(this, o.currentPage + 1, {text: o.nextText, classes: 'next'});
			}
		},

		_getPages: function(o) {
			var pages = Math.ceil(o.items / o.itemsOnPage);
			return pages || 1;
		},

		_getInterval: function(o) {
			return {
				start: Math.ceil(o.currentPage > o.halfDisplayed ? Math.max(Math.min(o.currentPage - o.halfDisplayed, (o.pages - o.displayedPages)), 0) : 0),
				end: Math.ceil(o.currentPage > o.halfDisplayed ? Math.min(o.currentPage + o.halfDisplayed, o.pages) : Math.min(o.displayedPages, o.pages))
			};
		},

		_appendItem: function(pageIndex, opts) {
			var self = this, options, $link, o = self.data('pagination'), $linkWrapper = $('<li></li>'), $ul = self.find('ul');

			pageIndex = pageIndex < 0 ? 0 : (pageIndex < o.pages ? pageIndex : o.pages - 1);

			options = {
				text: pageIndex + 1,
				classes: ''
			};

			if (o.labelMap.length && o.labelMap[pageIndex]) {
				options.text = o.labelMap[pageIndex];
			}

			options = $.extend(options, opts || {});

			if (pageIndex == o.currentPage || o.disabled) {
				if (o.disabled) {
					$linkWrapper.addClass('disabled');
				} else {
					$linkWrapper.addClass('active');
				}
				$link = $('<span class="current">' + (options.text) + '</span>');
			} else {
				$link = $('<a href="' + o.hrefTextPrefix + (pageIndex + 1) + o.hrefTextSuffix + '" class="page-link">' + (options.text) + '</a>');
				$link.click(function(event){
					return methods._selectPage.call(self, pageIndex, event);
				});
			}

			if (options.classes) {
				$link.addClass(options.classes);
			}

			$linkWrapper.append($link);

			if ($ul.length) {
				$ul.append($linkWrapper);
			} else {
				self.append($linkWrapper);
			}
		},

		_selectPage: function(pageIndex, event) {
			var o = this.data('pagination');
			o.currentPage = pageIndex;
			if (o.selectOnClick) {
				methods._draw.call(this);
			}
			return o.onPageClick(pageIndex + 1, event);
		}

	};

	$.fn.pagination = function(method) {

		// Method calling logic
		if (methods[method] && method.charAt(0) != '_') {
			return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
		} else if (typeof method === 'object' || !method) {
			return methods.init.apply(this, arguments);
		} else {
			$.error('Method ' +  method + ' does not exist on jQuery.pagination');
		}

	};

})(jQuery);

</script>