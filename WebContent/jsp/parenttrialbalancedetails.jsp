<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.service.*"%>
<%@ page import="java.time.LocalDate"%>
<%@ page import="java.time.format.DateTimeFormatter"%>
<%@ page import="com.track.serviceImplementation.*"%>
<%@ page import="com.track.db.object.*"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.util.StrUtils"%>
<%
	String title = "Account Transactions";
	JournalService journalService=new JournalEntry();
	String plant = StrUtils.fString(request.getParameter("plant")).trim();
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
	String accountName=StrUtils.fString(request.getParameter("accname")).trim();
	String fromDate= StrUtils.fString(request.getParameter("fromDate")).trim();
	String toDate= StrUtils.fString(request.getParameter("toDate")).trim();
	LocalDate fromdatetime=LocalDate.parse(fromDate);
	String fromdateString=fromdatetime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
	LocalDate todatetime=LocalDate.parse(toDate);
	String todateString=todatetime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
	List<TrialBalanceDetails> trialBalanceDetails=new ArrayList<TrialBalanceDetails>();
	trialBalanceDetails=journalService.getTrialBalanceDetailsByAccountByType(plant, account, fromDate, toDate);
	Double totalDebit=0.00;
	Double totalCredit=0.00;
	Double totalClosingBalance=0.00;
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
			 <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../accountant/parenttrialbalance'">
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
									<button type="button" id="exceldownload" class="btn btn-default" data-toggle="tooltip" data-placement="bottom" title="Excel">
									
									<a href="javascript:downTBExcel()" id="tbsexcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>
									<!-- <a href="/track/TrialBalanceServlet?action=getTrialBalanceAsExcel" style="text-decoration: none; color: black"><i class="fa fa-file-excel-o" aria-hidden="true"></i></a>-->
									</button>
									
									
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
					<div style="font-size:18px"><%=accountName%></div>
					<div style="font-size:15px">From <%=fromdateString %> To <%=todateString %></div>
				</div>
				<div class="col-sm-4"></div>
			</div>
				
			<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
			<input type="text" id="account" style="display:none;" value=<%=account%>>
		
		<div class="box-body">
			
			 <table class="table table-striped" id="trialbalanceledgers">
    <thead>
      <tr>
        <th>DATE</th>
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
    	 <td>Opening Balance</td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	 <td><%=curency%> <%=String.format("%."+numberOfDecimal+"f", 0.00) %></td>
    	 <td></td>
    	 </tr>
    	<%for(TrialBalanceDetails trialBalDet:trialBalanceDetails) 
    	{
    		totalDebit+=trialBalDet.getDEBIT();
    		totalCredit+=trialBalDet.getCREDIT();
    	%>
      <tr>
        <td><%=trialBalDet.getDATE() %></td>
        <td><%=trialBalDet.getACCOUNT() %></td>
        <td><%=trialBalDet.getTRANSACTION_DETAILS() %></td>
        <td><%=trialBalDet.getTRANSACTION_TYPE() %></td>
       <%
           if("".equals(trialBalDet.getTRANSACTION_ID()) || trialBalDet.getTRANSACTION_ID()==null || "null".equalsIgnoreCase(trialBalDet.getTRANSACTION_ID())){
        %>
         <td><%=trialBalDet.getJOURNALHDRID() %></td>
        <%}else{ %>
         <td><%=trialBalDet.getTRANSACTION_ID() %></td>
        <%} %>
        <td><%=trialBalDet.getREFERENCE() %></td>
        <td><%=String.format("%."+numberOfDecimal+"f", trialBalDet.getDEBIT()) %></td>
        <td><%=String.format("%."+numberOfDecimal+"f", trialBalDet.getCREDIT()) %></td>
        <%Double amount=trialBalDet.getDEBIT()-trialBalDet.getCREDIT();
        if(amount>0){ %>
        <td><%=String.format("%."+numberOfDecimal+"f", amount)%>Dr</td>
        <%}else{%>
         <td><%=String.format("%."+numberOfDecimal+"f", Math.abs(amount)) %>Cr</td>
        <%} %>
      </tr>
      <%} %>
      <tr>
    	 <td></td>	
    	 <td>Total Debits and Credits</td>
    	 <td>(<%=fromdateString %>-<%=todateString %>)</td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	 <td><%=String.format("%."+numberOfDecimal+"f", totalDebit) %></td>
    	 <td><%=String.format("%."+numberOfDecimal+"f", totalCredit) %></td>
    	 <td></td>
    	 </tr>
      <tr>
      <% totalClosingBalance=(totalDebit-totalCredit);%>
    	 <td>As On <%=todateString %></td>	
    	 <td>Closing Balance</td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	 <td></td>
    	<% if(totalClosingBalance>0){ %>
        <td><%=curency%> <%=String.format("%."+numberOfDecimal+"f", totalClosingBalance)%></td>
        <%}else{%>
        <td></td>
        <%} %>
        <% if(totalClosingBalance<0){ %>
         <td><%=curency%> <%=String.format("%."+numberOfDecimal+"f", Math.abs(totalClosingBalance)) %></td>
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
	var accNameSelected = "<%=accountName %>";
	var plant = "<%=plant%>";
	window.location = "/track/TrialBalanceServlet?action=getCTrialBalanceTransactionDetails&fromDate="+fromdate+"&toDate="+todate+"&account="+accountSelected+"&accname="+accNameSelected+"&plant="+plant;
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
function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
	}
function generatePdf(dataUrl){	
	//var data = table_1.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	/* Top Right */
	//var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	//var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	<%-- doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(10);
	//doc.setFontStyle("bold");
	doc.text('<%=PLNTDESC%>', 16, 46);
	doc.setFontStyle("bold");
	doc.text('From ',83,65);
	doc.text('<%=fromdateString%>',93,65);
	doc.text('to ',115,65);
	doc.text('<%=todateString%>',120,65);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);

	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);

	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);

	doc.setFontSize(6);
	doc.setFontStyle("normal"); --%>
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	doc.text('<%=PLNTDESC%>', 87, 30);
	doc.setFontSize(20);
	doc.text('<%=title%>',80,40);
	doc.setFontSize(12);
	doc.text('<%=accountName%>',89,50);
	doc.text('From ',83,57);
	doc.text('<%=fromdateString%>',95,57);
	doc.text('to ',120,57);
	doc.text('<%=todateString%>',125,57);
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
		html : '#trialbalanceledgers',
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
            text: {
              cellWidth: 'auto'
            },
        0: {
        	cellWidth: 25                
        },
        1: {
        	cellWidth: 15
        },
        2: {
        	cellWidth: 15
        },
        3: {
        	cellWidth: 15
        },
        4: {
        	cellWidth: 28
        },
        5: {
        	cellWidth: 15
        },
        6: {
        	cellWidth: 15,
        	halign:'right'
        },
        7: {
        	cellWidth: 15,
        	halign:'right'
        },
        8: {
        	cellWidth: 20,
        	halign:'right'
        },
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
	
	  
		 
	  doc.save('Trialbalancedetails.pdf')
}
function generatePdfPrint(dataUrl){	
	//var data = table_1.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	/* Top Right */
	//var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	//var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	<%-- doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(10);
	//doc.setFontStyle("bold");
	doc.text('<%=PLNTDESC%>', 16, 46);
	doc.setFontStyle("bold");
	doc.text('From ',83,65);
	doc.text('<%=fromdateString%>',93,65);
	doc.text('to ',115,65);
	doc.text('<%=todateString%>',120,65);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);

	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);

	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);

	doc.setFontSize(6);
	doc.setFontStyle("normal"); --%>
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	doc.text('<%=PLNTDESC%>', 87, 30);
	doc.setFontSize(20);
	doc.text('<%=title%>',80,40);
	doc.setFontSize(12);
	doc.text('<%=trialBalanceDetails.get(0).getACCOUNT()%>',89,50);
	doc.text('From ',83,57);
	doc.text('<%=fromdateString%>',95,57);
	doc.text('to ',120,57);
	doc.text('<%=todateString%>',125,57);
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
		html : '#trialbalanceledgers',
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
            text: {
              cellWidth: 'auto'
            },
        0: {
        	cellWidth: 25                
        },
        1: {
        	cellWidth: 15
        },
        2: {
        	cellWidth: 15
        },
        3: {
        	cellWidth: 15
        },
        4: {
        	cellWidth: 28
        },
        5: {
        	cellWidth: 15
        },
        6: {
        	cellWidth: 15,
        	halign:'right'
        },
        7: {
        	cellWidth: 15,
        	halign:'right'
        },
        8: {
        	cellWidth: 20,
        	halign:'right'
        },
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
function generatePdfMail(dataUrl,attachName){	
	//var data = table_1.getData();
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	/* Top Right */
	//var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	//var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	<%-- doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(10);
	//doc.setFontStyle("bold");
	doc.text('<%=PLNTDESC%>', 16, 46);
	doc.setFontStyle("bold");
	doc.text('From ',83,65);
	doc.text('<%=fromdateString%>',93,65);
	doc.text('to ',115,65);
	doc.text('<%=todateString%>',120,65);
	doc.setFontSize(9);
	doc.setFontStyle("normal");
	doc.text('<%=fromAddress_BlockAddress%>', 16, 50);

	doc.text('<%=fromAddress_RoadAddress%>', 16, 54);

	doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);

	doc.setFontSize(6);
	doc.setFontStyle("normal"); --%>
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
	
	doc.setFontSize(12);
	doc.text('<%=PLNTDESC%>', 87, 30);
	doc.setFontSize(20);
	doc.text('<%=title%>',80,40);
	doc.setFontSize(12);
	doc.text('<%=trialBalanceDetails.get(0).getACCOUNT()%>',89,50);
	doc.text('From ',83,57);
	doc.text('<%=fromdateString%>',95,57);
	doc.text('to ',120,57);
	doc.text('<%=todateString%>',125,57);
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
		html : '#trialbalanceledgers',
		startY : 75,
		startY : 75,
		theme : 'plain',
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
            text: {
              cellWidth: 'auto'
            },
        0: {
        	cellWidth: 25                
        },
        1: {
        	cellWidth: 15
        },
        2: {
        	cellWidth: 15
        },
        3: {
        	cellWidth: 15
        },
        4: {
        	cellWidth: 28
        },
        5: {
        	cellWidth: 15
        },
        6: {
        	cellWidth: 15,
        	halign:'right'
        },
        7: {
        	cellWidth: 15,
        	halign:'right'
        },
        8: {
        	cellWidth: 20,
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

</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>