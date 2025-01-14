<%@ include file="header.jsp"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%
String title = "GST Transaction Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String box = StrUtils.fString(request.getParameter("BOX"));
String deschead = StrUtils.fString(request.getParameter("DESCHEAD"));
String taxheader = StrUtils.fString(request.getParameter("taxheader"));
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
    <jsp:param name="mainmenu" value="<%=IConstants.TAX_RETURN%>"/>
</jsp:include>
<!-- <script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script type="text/javascript" src="js/general.js"></script>
  <script type="text/javascript" src="js/calendar.js"></script>
  <script type="text/javascript" src="dist/js/moment.min.js"></script>
  <link href="css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="js/tabulator.min.js"></script>  
<script src="js/jspdf.debug.js"></script>
<script src="js/jspdf.plugin.autotable.js"></script>
<script src="js/Printpagepdf.js"></script> -->
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script type="text/javascript" src="js/general.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="dist/js/moment.min.js"></script>
<link href="css/tabulator_bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="js/tabulator.min.js"></script>
<script src="js/jspdf.debug.js"></script>
<script src="js/jspdf.plugin.autotable.js"></script>
<script src="js/Printpagepdf.js"></script>
<script type="text/javascript" src="js/sg-taxtransactionsummary.js"></script>
  <style>
  .text-dashed-underline:after {
    padding-bottom: 2px;
    border-bottom: 1px dashed #969696!important;
    width:10%;
}
  .requiredlabel {
    color: #b94a48;
}
.form-text {
    color: #999;
}
.d-block {
    display: block!important;
}
.alert-warning {
    background-color: #fff4e7 !important;
    border: 0;
    color: #222 !important;
}
  </style>
 

  <div class="container-fluid m-t-20">
	 <div class="box"> 
		<div class="box-header menu-drop">
			<%-- <div class="text-center">
				<h3><small><%=CNAME%></small></h3>
				<h2><small>Box <span><%=box%></span> - <%=deschead %></small></h2>
				<h3><small>From <span id="taxFrom"></span> To <span id="taxTo"></span></small></h3>
			</div> --%>
			
			
			


			<div class="row">
				
				<div class="col-sm-12">
					<div class="row">
						<div class=" pull-right">
							<div class="btn-group" role="group">
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
									<h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='sg-taxreturnfill.jsp?ID=<%=taxheader%>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
							</div>
						</div>
					</div>
				</div>
			</div>

			
			
		<%-- 	<h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='sg-taxreturnfill.jsp?ID=<%=taxheader%>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1> --%>
		</div>
<form>
<input type="hidden" id="taxheaderid" value="<%=taxheader%>"/>
<input type="hidden" id="box" value="<%=box%>"/>
</form>
		
 <div class="box-body">
 <div class="text-center">
				<h3><small><%=CNAME%></small></h3>
				<h2><small>Box <span><%=box%></span> - <%=deschead %></small></h2>
				<h3><small>From <span id="taxFrom"></span> To <span id="taxTo"></span></small></h3>
				<input type="text" name="taxFrom" value="" hidden>
				<input type="text" name="taxTo" value="" hidden>
			</div>
 	<div id="taxtransactionsummary"></div>
</div>
</div> 
</div>
<%@include file="sg-taxreturncustompopup.jsp"%>
<script type="text/javascript">
function printdoc()
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfPrint(dataUrl);
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

function generatePdfPrint(dataUrl){
	var data = tabledata
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	/* Top Right */
	//var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	//var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);

	
	
	doc.setFontSize(12);
	//doc.setFontStyle("bold");
	doc.text('<%=CNAME%>', 110, 48,'center');
	doc.setFontSize(16);
	doc.setFontStyle("bold");
	doc.text('Box '+'<%=box%>' + '-' +'<%=deschead %>',110,56,'center');
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate='From '+$("input[name=taxFrom]").val() +' To ' + $("input[name=taxTo]").val();
	doc.text(asofdate,110,64,'center');
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<thead><tr><th>DATE</th><th>ENTRY#</th><th>TRANSACTION TYPE</th><th>TAXABLE AMOUNT</th><th>TAX AMOUNT</th></tr></thead><tbody>";
	var col = ['DATE', 'ENTRY#','TRANSACTION TYPE','TAXABLE AMOUNT','TAX AMOUNT'];
	var rows = [];
	var groups_1=tabledata;
	data.forEach(element => {    
		htmlTable+="<tr><td>"+element.DATE+"</td>" +
					"<td>"+element.ENTRY+"</td>" +
					"<td>"+element.TRANSACTION_TYPE+"</td>";
		if(element.TAXABLE_AMOUNT == ''){
			htmlTable+="<td>0.00</td>";
		}else{
			htmlTable+="<td>"+parseFloat(element.TAXABLE_AMOUNT).toFixed("2")+"</td>";
		}
		if(element.TAX_AMOUNT == ''){
			htmlTable+="<td>0.00</td>";
		}else{
			htmlTable+="<td>"+parseFloat(element.TAX_AMOUNT).toFixed("2")+"</td>";
		}
		htmlTable+="</tr>";
    });
	htmlTable+="</tbody>";
	console.log(htmlTable);
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'left'},2 : {halign : 'left'},3 : {halign : 'right'},4 : {halign : 'right'}},
		theme : 'plain',
		headStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ],
			
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		willDrawCell: function (data) {},
		didParseCell: function (data) {
			if (data.column.index === 3) {
				data.cell.styles.halign = 'right';
			}
			if (data.column.index === 4) {
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


function generate() {
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdf(dataUrl);
		  	},'image/jpeg');
}


function generatePdf(dataUrl){	
	var data = tabledata
	//var rows = table_1.getRows();
	//var json = JSON.stringify(data);
	//alert(json);
	var doc = new jsPDF('p', 'mm', 'a4');
	var pageNumber;
	/* Top Right */
	//var fromDate=moment(fromDateFormatted).format('DD MMM YYYY');
	//var toDate=moment(toDateFormatted).format('DD MMM YYYY');
	doc.setFontSize(17);
	doc.setFontStyle("normal");
	doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);

	
	
	doc.setFontSize(12);
	//doc.setFontStyle("bold");
	doc.text('<%=CNAME%>', 110, 48,'center');
	doc.setFontSize(16);
	doc.setFontStyle("bold");
	doc.text('Box '+'<%=box%>' + '-' +'<%=deschead %>',110,56,'center');
	doc.setFontSize(14);
	doc.setFontStyle("normal");
	var asofdate='From '+$("input[name=taxFrom]").val() +' To ' + $("input[name=taxTo]").val();
	doc.text(asofdate,110,64,'center');
	doc.setFontStyle("normal");
	/* **** */
	var totalPagesExp = "{total_pages_cont_string}";
	
	var htmlTable="<thead><tr><th>DATE</th><th>ENTRY#</th><th>TRANSACTION TYPE</th><th>TAXABLE AMOUNT</th><th>TAX AMOUNT</th></tr></thead><tbody>";
	var col = ['DATE', 'ENTRY#','TRANSACTION TYPE','TAXABLE AMOUNT','TAX AMOUNT'];
	var rows = [];
	var groups_1=tabledata;
	data.forEach(element => {    
		htmlTable+="<tr><td>"+element.DATE+"</td>" +
					"<td>"+element.ENTRY+"</td>" +
					"<td>"+element.TRANSACTION_TYPE+"</td>";
		if(element.TAXABLE_AMOUNT == ''){
			htmlTable+="<td>0.00</td>";
		}else{
			htmlTable+="<td>"+parseFloat(element.TAXABLE_AMOUNT).toFixed("2")+"</td>";
		}
		if(element.TAX_AMOUNT == ''){
			htmlTable+="<td>0.00</td>";
		}else{
			htmlTable+="<td>"+parseFloat(element.TAX_AMOUNT).toFixed("2")+"</td>";
		}
		htmlTable+="</tr>";
    });
	htmlTable+="</tbody>";
	console.log(htmlTable);
	var tableElement=document.createElement('table');
	tableElement.innerHTML=htmlTable;
	doc.autoTable({
		html : tableElement,
		startY : 75,
		columnStyles : {0 : {halign : 'left'},1 : {halign : 'left'},2 : {halign : 'left'},3 : {halign : 'right'},4 : {halign : 'right'}},
		theme : 'plain',
		headStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ],
			
		},
		bodyStyles : {
			fillColor : [ 255, 255, 255 ],
			textColor : [ 0, 0, 0 ]
		},
		willDrawCell: function (data) {},
		didParseCell: function (data) {
			if (data.column.index === 3) {
				data.cell.styles.halign = 'right';
			}
			if (data.column.index === 4) {
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
		 
	  doc.save('Transactionsummary.pdf');
}

function downTBExcel()
{
	var taxheaderid = '<%=taxheader%>';
	var company = '<%=CNAME%>';
	var heading = 'Box '+'<%=box%>' + '-' +'<%=deschead %>';
	var fromtodate = 'From '+$("input[name=taxFrom]").val() +' To ' + $("input[name=taxTo]").val();
	var boxsend = $('#box').val();
	window.location = "/track/TaxReturnFiling?action=transactionSummaryForSGExcel&taxheaderid="+taxheaderid+"&company="+company+"&heading="+heading+"&fromtodate="+fromtodate+"&box="+boxsend;
}
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>