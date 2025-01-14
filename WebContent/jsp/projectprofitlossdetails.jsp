<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.service.*"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="com.track.serviceImplementation.*"%>
<%@ page import="com.track.db.object.*"%>
<%@ page import="com.track.util.StrUtils"%>
<%@ page import="com.track.constants.*"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%
	String title = "Project Profit And Loss Account Transactions";
	JournalService journalService=new JournalEntry();
	LedgerService ledgerService=new LedgerServiceImpl();
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	PlantMstDAO plantMstDAO = new PlantMstDAO();
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
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String account=StrUtils.fString(request.getParameter("account")).trim();
	String fromDate= StrUtils.fString(request.getParameter("fromDate")).trim();
	String toDate= StrUtils.fString(request.getParameter("toDate")).trim();
	LocalDate fromdatetime=LocalDate.parse(fromDate);
	String fromdateString=fromdatetime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
	LocalDate todatetime=LocalDate.parse(toDate);
	String todateString=todatetime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
	List<LedgerDetails> ledgerDetails=new ArrayList<LedgerDetails>();
	ledgerDetails=ledgerService.getLedgerDetailsByAccount(plant, account, fromDate, toDate);
	LocalDate yearStartDay=LocalDate.parse(fromDate).withDayOfYear(1);
	System.out.println("Year Start Day:"+yearStartDay.toString());
	Double openingBalance=ledgerService.openingBalance(plant,account,yearStartDay.toString(),fromDate);
	Double totalDebit=0.00;
	Double totalCredit=0.00;
	Double totalClosingBalance=0.00;

	CoaDAO coaDAO=new CoaDAO();
	String actcode = coaDAO.GetAccountCodeByName(ledgerDetails.get(0).getACCOUNT(), plant);
%>
<%@include file="sessionCheck.jsp"%>

<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
    <jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING_SUB_MENU%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<link href="../jsp/css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="../jsp/js/tabulator.min.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<div class="container-fluid m-t-20">

	<div class="box">

		<div class="box-header menu-drop">
			 <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../businessoverview/projectprofitloss'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
              <div class="row">
              		<div class="col-sm-6 pull-left">
              		</div>
              		<div class="col-sm-6 pull-right">
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
									
									<a href="javascript:downTBExcel()" id="tbsexcel" style="text-decoration: none; color: black">
									<button type="button" id="exceldownload" class="btn btn-default" data-toggle="tooltip" data-placement="bottom" title="Excel">
									
									<i class="fa fa-file-excel-o" aria-hidden="true"></i>
									<!-- <a href="/track/TrialBalanceServlet?action=getTrialBalanceAsExcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>-->
									</button>
									</a>
									
									<button type="button" class="btn btn-default printMe" 
									 data-toggle="tooltip"  data-placement="bottom" title="Print">
										<i class="fa fa-print" aria-hidden="true"></i>
									</button>
								</div>
							</div>
						</div>
				</div>
              </div>
              </div>
              <div id="print_id">
			<div class="row">
				<div class="col-sm-4"></div>
				<div class="col-sm-4" style="text-align:center">
					<div style="font-size:18px"><%=PLNTDESC %></div>
					<div style="font-size:28px">Account Transactions</div>
					<div style="font-size:18px"><%=actcode+" "+ledgerDetails.get(0).getACCOUNT()%></div>
					<%-- <div style="font-size:15px">From <%=fromdateString %> To <%=todateString %></div> --%>
					
				</div>
				<div class="col-sm-4"></div>
			</div>
				
			<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
			<input type="text" id="account" style="display:none;" value=<%=account%>>
		
		<div class="box-body">
			
			 <table class="table table-striped" id="profitlossledgers">
    <thead>
      <tr>
        <th>DATE</th>
        <th>ACCOUNT CODE</th>
        <th>ACCOUNT</th>
        <th>TRANSACTION_DETAILS</th>
        <th>TRANSACTION_TYPE</th>
        <th>TRANSACTION_ID</th>
        <th>REFERENCE</th>
        <th>DEBIT</th>
        <th>CREDIT</th>
        <th>AMOUNT</th>
      </tr>
    </thead>
    <tbody>
     <tr>
    	 <td>As On <%=fromdateString %></td>
    	  <td></td>	
    	 <td>Opening Balance</td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	 <% if(openingBalance>=0){ %>
        <td align="right"><%=curency%> <%=Numbers.toMillionFormat(openingBalance, Integer.parseInt(numberOfDecimal))%> </td>
        <%}else{%>
        <td></td>
        <%} %>
        <% if(openingBalance<0){ %>
         <td align="right"><%=curency%><%=Numbers.toMillionFormat(openingBalance, Integer.parseInt(numberOfDecimal))%></td>
        <%}else{%>
        <td></td>
        <%} %>
    	 <td></td>
    	 </tr>
    	<%for(LedgerDetails ledgerDet:ledgerDetails) 
    	{
    		totalDebit+=ledgerDet.getDEBIT();
    		totalCredit+=ledgerDet.getCREDIT();
    		String accountcode = coaDAO.GetAccountCodeByName(ledgerDet.getACCOUNT(), plant);
    	%>
      <tr>
        <td><%=ledgerDet.getDATE() %></td>
        <td><%=accountcode%></td>
        <td><%=ledgerDet.getACCOUNT() %></td>
        <td><%=ledgerDet.getTRANSACTION_DETAILS() %></td>
        <td><%=ledgerDet.getTRANSACTION_TYPE() %></td>
        <%
           if("".equals(ledgerDet.getTRANSACTION_ID()) || ledgerDet.getTRANSACTION_ID()==null || "null".equalsIgnoreCase(ledgerDet.getTRANSACTION_ID())){
        %>
         <td><%=ledgerDet.getJOURNALHDRID() %></td>
        <%}else{ %>
         <td><%=ledgerDet.getTRANSACTION_ID() %></td>
        <%} %>
       
        <td><%=ledgerDet.getREFERENCE() %></td>
        <td align="right"><%=Numbers.toMillionFormat(ledgerDet.getDEBIT(), Integer.parseInt(numberOfDecimal))%></td>
        <td align="right"><%=Numbers.toMillionFormat(ledgerDet.getCREDIT(), Integer.parseInt(numberOfDecimal))%></td>
        <%Double amount=ledgerDet.getDEBIT()-ledgerDet.getCREDIT();
        if(amount>0){ %>
        <td align="right"><%=Numbers.toMillionFormat(amount, Integer.parseInt(numberOfDecimal))%>Dr</td>
        <%}else{%>
         <td align="right"><%=Numbers.toMillionFormat(Math.abs(amount), Integer.parseInt(numberOfDecimal))%>Cr</td>
        <%} %>
      </tr>
      <%} %>
      <tr>
    	 <td></td>	
    	 <td></td>	
    	 <td>Total Debits and Credits</td>
    	 <td>(<%=fromdateString %>-<%=todateString %>)</td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	 <td align="right"><%=Numbers.toMillionFormat(totalDebit, Integer.parseInt(numberOfDecimal))%></td>
    	 <td align="right"><%=Numbers.toMillionFormat(totalCredit, Integer.parseInt(numberOfDecimal))%></td>
    	 <td></td>
    	 </tr>
      <tr>
      <% totalClosingBalance=(totalDebit-totalCredit+openingBalance);%>
    	 <td>As On <%=todateString %></td>	
    	 <td></td>	
    	 <td>Closing Balance</td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	<% if(totalClosingBalance>=0){ %>
        <td align="right"><%=curency%> <%=Numbers.toMillionFormat(totalClosingBalance, Integer.parseInt(numberOfDecimal))%></td>
        <%}else{%>
        <td></td>
        <%} %>
        <% if(totalClosingBalance<0){ %>
         <td align="right"><%=curency%> <%=Numbers.toMillionFormat(Math.abs(totalClosingBalance), Integer.parseInt(numberOfDecimal))%></td>
        <%}else{%>
        <td></td>
        <%} %>
    	 <td></td>
    	 </tr>
    </tbody>
  </table>
		</div>
</div>

	</div>
</div>
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>
<style>
@page { size: auto;  margin: 0mm; }
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
.tabulator-headers{
	font-size: 11px;
}
</style>
<script type="text/javascript">
var tableJournalSummary;
var numberOfDecimal = $("#numberOfDecimal").val();
var account = $("#account").val();
$("#pdfdownload").click(function(){
	generate();
});
$("#sendemail").click(function(){
	
	//$("#common_email_modal #send_subject).val("Your Profit Loss Report");
	$('#common_email_modal').modal('toggle');
	var fromdate="<%=fromdateString %>";
	var todate="<%=todateString %>";
	var asofdate="From "+fromdate+ " To "+todate;
	$('#asofDate').val(asofdate);
	loadBodyStyle();
});


function downTBExcel()
{     
 	var fromdate="<%=fromdateString %>";
	var todate="<%=todateString %>";
	var accountSelected = "<%=account %>";
	var accname = "<%=ledgerDetails.get(0).getACCOUNT()%>";
	window.location = "/track/TrialBalanceServlet?action=getTrialBalanceTransactionDetails&fromDate="+fromdate+"&toDate="+todate+"&account="+accountSelected+"&accname="+accname;
}


function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
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
function generatePdf(dataUrl){	
	//var data = table_1.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('l', 'mm', 'a4');
	var cwidth = doc.internal.pageSize.getWidth()/2;
	var pageNumber;
	/* Top Right */
	//var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	//var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	doc.text('<%=PLNTDESC%>', cwidth, 30,'center');
	doc.setFontSize(20);
	doc.text('<%=title%>',cwidth,40,'center');
	doc.setFontSize(12);
	doc.text('<%=actcode+" "+ledgerDetails.get(0).getACCOUNT()%>',cwidth,50,'center');
	var fromto = 'From '+'<%=fromdateString%>'+' to '+'<%=todateString%>';
	doc.text(fromto,cwidth,57,'center');
	<%-- doc.text('From ',83,57);
	doc.text('<%=fromdateString%>',95,57);
	doc.text('to ',120,57);
	doc.text('<%=todateString%>',125,57); --%>
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);
	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	doc.autoTable({
		html : '#profitlossledgers',
		startY : 75,
		theme : 'striped',
		styles: {
			cellWidth: 'wrap',
            fontSize: 12,
            cellPadding: 0.5
        },
		headStyles : {
			margin:{top:30},
            fontSize: 7,
            fillColor : [ 217, 217, 217],
			textColor : [ 0, 0, 0 ]
		},
		bodyStyles: {
            fontSize: 10,
            lineWidth: 0.025,
            lineColor: [217, 217, 217]
        },
        columnStyles: {
            0: {
            	cellWidth: 25                
            },
            1: {
            	cellWidth: 15
            },
            2: {
            	cellWidth: 30
            },
            3: {
            	cellWidth: 15
            },
            4: {
            	cellWidth: 28
            },
            5: {
            	cellWidth: 25
            },
            6: {
            	cellWidth: 15
            },
            7: {
            	cellWidth: 30,
            	halign:'right'
            },
            8: {
            	cellWidth: 30,
            	halign:'right'
            },
            9: {
            	cellWidth: 30,
            	halign:'right'
            },
        },
		willDrawCell: function (data) {
            // Colspan
            console.log(data.cell.raw.className);
            if (data.cell.raw.className=="innerHeader") {
            	//console.log("Entered");
            	doc.setFontStyle('bold');
                doc.setFontSize(9);
                doc.setTextColor(0, 0, 0);
                doc.setFillColor(242, 242, 242);
		}
		},
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
	
	  
		 
	  doc.save('Profitlossdetails.pdf')
}
function generatePdfPrint(dataUrl){	
	//var data = table_1.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('l', 'mm', 'a4');
	var cwidth = doc.internal.pageSize.getWidth()/2;
	var pageNumber;
	/* Top Right */
	//var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	//var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	doc.text('<%=PLNTDESC%>', cwidth, 30,'center');
	doc.setFontSize(20);
	doc.text('<%=title%>',cwidth,40,'center');
	doc.setFontSize(12);
	doc.text('<%=actcode+" "+ledgerDetails.get(0).getACCOUNT()%>',cwidth,50,'center');
	var fromto = 'From '+'<%=fromdateString%>'+' to '+'<%=todateString%>';
	doc.text(fromto,cwidth,57,'center');
	<%-- doc.text('From ',83,57);
	doc.text('<%=fromdateString%>',95,57);
	doc.text('to ',120,57);
	doc.text('<%=todateString%>',125,57); --%>
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);
	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	doc.autoTable({
		html : '#profitlossledgers',
		startY : 75,
		theme : 'striped',
		styles: {
			cellWidth: 'wrap',
            fontSize: 12,
            cellPadding: 0.5
        },
		headStyles : {
			margin:{top:30},
            fontSize: 7,
            fillColor : [ 217, 217, 217],
			textColor : [ 0, 0, 0 ]
		},
		bodyStyles: {
            fontSize: 10,
            lineWidth: 0.025,
            lineColor: [217, 217, 217]
        },
        columnStyles: {
            0: {
            	cellWidth: 25                
            },
            1: {
            	cellWidth: 15
            },
            2: {
            	cellWidth: 25
            },
            3: {
            	cellWidth: 15
            },
            4: {
            	cellWidth: 28
            },
            5: {
            	cellWidth: 25
            },
            6: {
            	cellWidth: 15
            },
            7: {
            	cellWidth: 25,
            	halign:'right'
            },
            8: {
            	cellWidth: 25,
            	halign:'right'
            },
            9: {
            	cellWidth: 25,
            	halign:'right'
            },
        },
		willDrawCell: function (data) {
            // Colspan
            console.log(data.cell.raw.className);
            if (data.cell.raw.className=="innerHeader") {
            	//console.log("Entered");
            	doc.setFontStyle('bold');
                doc.setFontSize(9);
                doc.setTextColor(0, 0, 0);
                doc.setFillColor(242, 242, 242);
		}
		},
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
function generatePdfMail(dataUrl,attachName){	

	
	//var data = table_1.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('l', 'mm', 'a4');
	var cwidth = doc.internal.pageSize.getWidth()/2;
	var pageNumber;
	/* Top Right */
	//var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	//var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	doc.text('<%=PLNTDESC%>', cwidth, 30,'center');
	doc.setFontSize(20);
	doc.text('<%=title%>',cwidth,40,'center');
	doc.setFontSize(12);
	doc.text('<%=actcode+" "+ledgerDetails.get(0).getACCOUNT()%>',cwidth,50,'center');
	var fromto = 'From '+'<%=fromdateString%>'+' to '+'<%=todateString%>';
	doc.text(fromto,cwidth,57,'center');
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);
	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);
	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	doc.autoTable({
		html : '#profitlossledgers',
		startY : 75,
		/* theme : 'plain',
		headStyles : {
			fillColor : [ 0, 0, 0 ],
			textColor : [ 255, 255, 255 ],
			halign : 'center'
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		}, */
		theme : 'striped',
		styles: {
			cellWidth: 'wrap',
            fontSize: 12,
            cellPadding: 0.5
        },
		headStyles : {
			margin:{top:30},
            fontSize: 7,
            fillColor : [ 217, 217, 217],
			textColor : [ 0, 0, 0 ]
		},
		bodyStyles: {
            fontSize: 10,
            lineWidth: 0.025,
            lineColor: [217, 217, 217]
        },
        columnStyles: {
            0: {
            	cellWidth: 25                
            },
            1: {
            	cellWidth: 15
            },
            2: {
            	cellWidth: 30
            },
            3: {
            	cellWidth: 15
            },
            4: {
            	cellWidth: 28
            },
            5: {
            	cellWidth: 25
            },
            6: {
            	cellWidth: 15
            },
            7: {
            	cellWidth: 30,
            	halign:'right'
            },
            8: {
            	cellWidth: 30,
            	halign:'right'
            },
            9: {
            	cellWidth: 30,
            	halign:'right'
            },
        },
		willDrawCell: function (data) {
            // Colspan
            console.log(data.cell.raw.className);
            if (data.cell.raw.className=="innerHeader") {
            	//console.log("Entered");
            	doc.setFontStyle('bold');
                doc.setFontSize(9);
                doc.setTextColor(0, 0, 0);
                doc.setFillColor(242, 242, 242);
		}
		},
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

function printdoc()
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfPrint(dataUrl);
		  	},'image/jpeg');
	
	
	}
$('.printMe').click(function(){
	printdoc();
});
/* $('.printMe').click(function(){
    $("#print_id").print({
       	globalStyles: true,
       	mediaPrint: false,
       	stylesheet: null,
       	noPrintSelector: ".no-print",
       	iframe: false,
       	append: null,
       	prepend: null,
       	manuallyCopyFormValues: true,
       	deferred: $.Deferred(),
       	timeout: 750,
       	title: " ",
       	doctype: '<!doctype html>'
	});
 
}); */

/* var table = new Tabulator("#profitlossledgers", {pagination:"local", //enable local pagination.
    paginationSize:10, }); */
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>