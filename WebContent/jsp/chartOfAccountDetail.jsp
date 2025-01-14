<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Chart of Accounts Summary";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	CoaUtil coaUtil = new CoaUtil();
	CoaDAO coaDao = new CoaDAO();	 
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String id = StrUtils.fString(request.getParameter("TRANID"));
	Hashtable ht = new Hashtable();
	if(id.length()>0)
		ht.put("ID",id);
	ht.put("a.PLANT",plant);
	String sql = "SELECT a.ID,a.ACCOUNT_NAME,ISNULL(a.ACCOUNT_CODE,'') AS ACCOUNT_CODE,b.ACCOUNTTYPE,d.ACCOUNTDETAILTYPE,a.OPENINGBALANCE,a.OPENINGBALANCEDATE,ISNULL(a.description,' ') as descr,SUBSTRING(a.CRAT, 7, 2) +'/'+ SUBSTRING(a.CRAT, 5, 2) +'/'+ SUBSTRING(a.CRAT, 1, 4) AS DATE FROM "+plant+"_FINCHARTOFACCOUNTS a left join " + "[" + plant + "_" +"FINACCOUNTTYPE] b on a.ACCOUNTTYPE=b.ID join " + "[" + plant +"_"+"FINACCOUNTDETAILTYPE] d on d.ID=a.ACCOUNTDETAILTYPE";
	List arrList = coaDao.selectReport(sql, ht, " ORDER BY a.ACCOUNT_CODE ASC");
	Map grnbillHdr=(Map)arrList.get(0);
	
	List taxList =new ArrayList();	
	ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
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
	
	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	File checkImageFile = new File(imagePath);
	if (!checkImageFile.exists()) {
		imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	}
	double dTotalAmount = 0;
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
    <jsp:param name="mainmenu" value="<%=IConstants.CHART_OF_ACCOUNTS%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/font-awesome.min.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<style>
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
.text-ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.ribbon {
    position: absolute!important;
    top: -5px;
    left: -5px;
    overflow: hidden;
    width: 96px;
    height: 94px;
}
.ribbon .ribbon-draft {
    background-color: #94a5a6;
    border-color: #788e8f;
}
.ribbon .ribbon-inner {
    text-align: center;
    color: #fff;
    top: 24px;
    left: -31px;
    width: 135px;
    padding: 3px;
    position: relative;
    transform: rotate(-45deg);
}
.ribbon .ribbon-inner:before {
    left: 0;
    border-left: 2px solid transparent;
}
.ribbon .ribbon-inner:after {
    right: -2px;
    border-bottom: 3px solid transparent;
}
.ribbon .ribbon-inner:after, .ribbon .ribbon-inner:before {
    content: "";
    border-top: 5px solid transparent;
    border-left: 5px solid;
    border-left-color: inherit;
    border-right: 5px solid transparent;
    border-bottom: 5px solid;
    border-bottom-color: inherit;
    position: absolute;
    top: 20px;
    transform: rotate(-45deg);
}
</style>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../banking/chartofaccounts"><span class="underline-on-hover">Chart of Accounts</span> </a></li>                      
                <li><label>Chart of Accounts Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">					
					<button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
				</div>
				
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../banking/chartofaccounts'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;">		
		<div style="height: 0.700000in;"></div>
		<span id="print_id">
<%--		<div class="row">
 			<div class="col-xs-6">
				<img src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>"
					style="width: 130.00px;" id="logo_content">
				<div>
					<b><%=PLNTDESC%></b>
				</div>
<span style="white-space: pre-wrap; word-wrap: break-word;">
<%=fromAddress_BlockAddress%>
<%=fromAddress_RoadAddress%>
<%=fromAddress_Country%> <%=ZIP%>
</span>
			</div>
 
			
		</div> --%>
		<div class="row">
			<div class="col-xs-12" >
			<p style="text-align: center;">
			<img src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>"
				style="width: 130.00px;" id="logo_content">
				</p>		
      <h1 style="font-size: 20px;text-align:center"><b><%=PLNTDESC%></b></h1>
    </div>
		<div class="row">
			<div class="col-xs-12" >		
      <h1 style="font-size: 20px;text-align:center">CHART OF ACCOUNTS SUMMARY</h1>
    </div>
    </div>

<div class="row">
			<div class="col-xs-12" style="color:#999;">			
			<%-- <p style="text-align:center">On <%=grnbillHdr.get("OPENINGBALANCEDATE")%></p> --%>
			</div>
</div>

		<div class="row">
			<div class="col-xs-12">
				<table id="table2" class="table">
					<thead>
						<tr>
							<!-- <td>ID</td> -->
							<!-- <td>Date</td> -->
							<td>Account Code</td>
							<td>Account Name</td>
							<td>Account Group</td>
							<td>Account Type</td>
							<!-- <td>AMOUNT</td>
							<td>BALANCE</td> -->							
						</tr>
					</thead>
					<tbody>
					<%for(int i =0; i<arrList.size(); i++) {   
				  		Map m=(Map)arrList.get(i);
				  		/* String  Paymentamount="";				  		
			  						  			
			  			Paymentamount = (String) m.get("OPENINGBALANCE");
			  			double dPaymentamount ="".equals(Paymentamount) ? 0.0d :  Double.parseDouble(Paymentamount);
			  			Paymentamount = StrUtils.addZeroes(dPaymentamount, numberOfDecimal);
			  			
			  			 dTotalAmount =dTotalAmount+dPaymentamount; */
		  			
			  		%>
				  		<tr>
							<%-- <td class="text-center"><%=m.get("ID") %></td> --%>
							<%-- <td class="text-center"><%=m.get("DATE") %></td> --%>
							<td class="text-center"><%=m.get("ACCOUNT_CODE") %></td>
							<td class="text-center"><%=m.get("ACCOUNT_NAME") %></td>
							<td class="text-center"><%=m.get("ACCOUNTTYPE") %></td>
							<td class="text-center"><%=m.get("ACCOUNTDETAILTYPE") %></td>
							
							<%-- <td class="text-right"><%=Paymentamount%></td>
							<td class="text-right"><%=Paymentamount%></td> --%>
						</tr>
				  	<%}%>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-6"></div>
			<div class="col-xs-4">
				<table id="table3" class="table text-right">
					<%-- <tbody>
						<tr>
						<%
						String  Paymentamount="";
			  			Paymentamount = String.valueOf(dTotalAmount);
			  			%>
							<td >Total for <%=grnbillHdr.get("ACCOUNT_NAME") %></td>
							<td ><%=Paymentamount%></td>
						</tr>
						</tbody> --%>
				</table>
			<br><br><br>
			<br><br><br>		
		</div>
		<div class="col-xs-2"></div>
		</span>
		</div>
		<script>
			$(document).ready(function(){
			  $('[data-toggle="tooltip"]').tooltip();
			  $('.printMe').click(function(){
				     $("#print_id").print();
				  
				});
			});
			
			function generatePdf(dataUrl){
				var doc = new jsPDF('p', 'mm', 'a4');
				var pageNumber;
				/* Top Right */
				
				

				/* doc.autoTable({
					html : '#table1',
					startY : 63,
					margin : {left : 142},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
					theme : 'plain'
				}); */
				/* **** */

				/* Top Left */
				
				doc.addImage(dataUrl, 'JPEG', 100, 23, 35,15);
				//doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);				
				<%-- doc.setFontSize(10);
				doc.setFontStyle("bold");
				doc.text('<%=PLNTDESC%>', 16, 46);

				doc.setFontSize(9);
				doc.setFontStyle("normal");
				doc.text('<%=fromAddress_BlockAddress%>', 16, 50);

				doc.text('<%=fromAddress_RoadAddress%>', 16, 54);

				doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);
 --%>
				doc.setFontSize(10);
				doc.setFontStyle("bold");
				doc.text('<%=PLNTDESC%>', 100, 46);
				doc.text('CHART OF ACCOUNTS SUMMARY', 90, 54);
				doc.setFontStyle("normal");
				
				<%-- doc.text('On ', 105, 63);
				doc.text('<%=grnbillHdr.get("OPENINGBALANCEDATE")%>', 110, 63); --%>
				
				
				/* **** */
				var totalPagesExp = "{total_pages_cont_string}";
				doc.fromHTML(document.getElementById('table'));
				doc.autoTable({
					html : '#table2',
					startY : 75,
					headStyles : {
						fillColor : [ 0, 0, 0 ],
						textColor : [ 255, 255, 255 ],
						halign : 'left',
						font:'times',
						fontSize:10,
						fontStyle:'bold'
					},
					bodyStyles : {
						fillColor : [ 255, 255, 255 ],
						textColor : [ 0, 0, 0 ],
						halign : 'left',
						font:'times',
						fontSize:10
					},
					theme : 'plain',
					columnStyles: {0: {halign: 'left',columnWidth: 30},1: {halign: 'left',columnWidth:60},2: {halign: 'left',columnWidth:50},3: {halign: 'left'}},
					didDrawPage : function(data) {
						doc.setFont("times");
						doc.setFontStyle("bold");
						
						// Footer
						pageNumber = doc.internal.getNumberOfPages();
						var str = "Page " + doc.internal.getNumberOfPages();
						// Total page number plugin only available in jspdf v1.0+
						if (typeof doc.putTotalPages === 'function') {
							str = str + " of " + totalPagesExp;
						}
						doc.setFontSize(8);

						// jsPDF 1.4+ uses getWidth, <1.4 uses .width
						var pageSize = doc.internal.pageSize;
						var pageHeight = pageSize.height ? pageSize.height
								: pageSize.getHeight();
						doc.text(str, data.settings.margin.left,
								pageHeight - 10);
					}
				});

				let finalY = doc.previousAutoTable.finalY;

				doc.autoTable({
					html : '#table3',
					margin : {left : 83},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'},2 : {halign : 'right'}},
					theme : 'plain'
				});
				
				doc.setFontStyle("normal");
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
				doc.save('table.pdf');
			}
			function generate() {
				
			var img = toDataURL($("#logo_content").attr("src"),
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
			
		</script>
	</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>