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
	String title = "Goods Receipt Detail";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	
	boolean displayPdfPrint=false,displayConvertToBill=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displayPdfPrint = ub.isCheckValAcc("printgrn", plant,username);
		displayConvertToBill = ub.isCheckValAcc("convertgrn", plant,username);
	}
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	BillUtil billUtil = new BillUtil();
	BillDAO billDao = new BillDAO();
	RecvDetDAO recvDetDAO = new RecvDetDAO(); 
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String pono = StrUtils.fString(request.getParameter("PONO"));
	String grno = StrUtils.fString(request.getParameter("GRNO"));
	String vendname = StrUtils.fString(request.getParameter("VEND_NAME"));
	String vendno = StrUtils.fString(request.getParameter("VENDNO"));	
	Hashtable ht = new Hashtable();	
	
	ht.put("GRNO",grno);
	String sql="";
	if(!pono.equalsIgnoreCase("")){
	ht.put("A.PONO",pono);
	 sql = "SELECT VNAME,ISNULL(NAME,'') as NAME,ISNULL(ADDR1,'') as ADDR1,ISNULL(ADDR2,'') as ADDR2,ISNULL(ADDR3,'') as ADDR3,ISNULL(ADDR4,'') as ADDR4,ISNULL(STATE,'') as STATE,ISNULL(COUNTRY,'') as COUNTRY,ISNULL(ZIP,'') as ZIP,(SUBSTRING(A.CRAT, 7, 2) + '/' + SUBSTRING(A.CRAT, 5, 2) + '/' + SUBSTRING(A.CRAT, 1, 4)) as CRAT,A.STATUS,AMOUNT,ISNULL((P.TAXTREATMENT),'') as TAXTREATMENT,ISNULL(P.REVERSECHARGE, 0) REVERSECHARGE,ISNULL(P.GOODSIMPORT, 0) GOODSIMPORT,P.SHIPPINGCOST,P.ORDERDISCOUNT,P.PURCHASE_LOCATION,ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE P.PURCHASE_LOCATION = C.STATE),'') as STATE_PREFIX,cast(((((AMOUNT+P.SHIPPINGCOST)*P.INBOUND_GST)/100)+(AMOUNT+P.SHIPPINGCOST)) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL(QTY,0) QTY FROM " + plant +"_FINGRNOTOBILL A JOIN " + plant +"_VENDMST V ON V.VENDNO=A.VENDNO JOIN " + plant +"_POHDR P ON P.PONO=A.PONO WHERE A.PLANT='"+ plant+"'";
	}
	else{
	sql = "SELECT ''VNAME,''NAME,''ADDR1,''ADDR2,''ADDR3,''ADDR4,''STATE,''COUNTRY,''ZIP,(SUBSTRING(A.CRAT, 7, 2) + '/' + SUBSTRING(A.CRAT, 5, 2) + '/' + SUBSTRING(A.CRAT, 1, 4)) as CRAT,A.STATUS,AMOUNT,''TAXTREATMENT,0 as REVERSECHARGE,''GOODSIMPORT,0 as SHIPPINGCOST,0 as ORDERDISCOUNT,''PURCHASE_LOCATION,'' as STATE_PREFIX,0 as TOTAL_AMOUNT,ISNULL(QTY,0) QTY  FROM " + plant +"_FINGRNOTOBILL A  WHERE A.PLANT='"+ plant+"'";
	}
	
	List arrList = billDao.selectForReport(sql, ht, "");
	Map grnbillHdr=(Map)arrList.get(0);
	if(!pono.equalsIgnoreCase("")){
		
	sql = "SELECT LNNO,ITEM,ITEMDESC,RECQTY as QTY,ISNULL(UNITCOST,0)*ISNULL((SELECT ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = (ISNULL((SELECT ISNULL(B.CURRENCYID,0)  FROM ["+plant+"_POHDR] B WHERE B.PONO = A.PONO),''))),0) as COST,CURRENCYID,(RECQTY*(ISNULL(UNITCOST,0)*ISNULL((SELECT ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = (ISNULL((SELECT ISNULL(B.CURRENCYID,0)  FROM ["+plant+"_POHDR] B WHERE B.PONO = A.PONO),''))),0))) as AMOUNT,ISNULL((SELECT B.JobNum  FROM ["+plant+"_POHDR] B WHERE B.PONO = A.PONO),'') AS JOBNO,ISNULL((SELECT ISNULL(B.INBOUND_GST,0) INBOUND_GST FROM ["+plant+"_POHDR] B WHERE B.PONO = A.PONO),0) AS GSTPERCENTAGE,ISNULL((SELECT ISNULL(UNITMO,'') UNITMO FROM ["+plant+"_PODET]  WHERE item=a.item and pono=a.pono and POLNNO=a.LNNO),0) as UOM from "+plant+"_RECVDET A WHERE A.PLANT='"+ plant+"'";
	}
	else{
	sql = "SELECT LNNO,ITEM,ITEMDESC,RECQTY as QTY,0 as COST,'' as CURRENCYID,(RECQTY*(ISNULL(UNITCOST,0)*ISNULL((SELECT ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = (ISNULL((SELECT ISNULL(B.CURRENCYID,0)  FROM ["+plant+"_POHDR] B WHERE B.PONO = A.PONO),''))),0))) as AMOUNT,ISNULL((SELECT ISNULL(B.INBOUND_GST,0) INBOUND_GST FROM ["+plant+"_POHDR] B WHERE B.PONO = A.PONO),0) AS GSTPERCENTAGE,ISNULL((SELECT TOP 1 ISNULL(UNITMO,'') UNITMO FROM ["+plant+"_POSDET]  WHERE item=a.item and POSTRANID=a.GRNO and DOLNNO=a.LNNO),0) as UOM from "+plant+"_RECVDET A WHERE A.PLANT='"+ plant+"'";	
	}
	
	List grnbillDetList =  recvDetDAO.selectForReport(sql, ht, " ORDER BY LNNO + 0 ");
	String CUSADD1 = (String) grnbillHdr.get("ADDR1");
	String CUSADD2 = (String) grnbillHdr.get("ADDR2");
	String CUSADD3 = (String) grnbillHdr.get("ADDR3");
	String CUSADD4 = (String) grnbillHdr.get("ADDR4");
	String CUSSTATE = (String) grnbillHdr.get("STATE");
	String CUSCOUNTRY = (String) grnbillHdr.get("COUNTRY");
	String CUSZIP = (String) grnbillHdr.get("ZIP");
	
	String sTAXTREATMENT = (String) grnbillHdr.get("TAXTREATMENT");
	String sREVERSECHARGE = (String)grnbillHdr.get("REVERSECHARGE");
	String sGOODSIMPORT = (String)grnbillHdr.get("GOODSIMPORT");
	String sSTATE_PREFIX = (String)grnbillHdr.get("STATE_PREFIX");
	double shingpercentage =0.0;
	String shipingcost = (String) grnbillHdr.get("SHIPPINGCOST");
	double dshipingcost ="".equals(shipingcost) ? 0.0d :  Double.parseDouble(shipingcost);
	shipingcost = StrUtils.addZeroes(dshipingcost, numberOfDecimal);
	
	
	String toAddress_BlockAddress = CUSADD1 + " " + CUSADD2;
	String toAddress_RoadAddress = CUSADD3 + " " + CUSADD4;
	String toAddress_Country = CUSSTATE + " " + CUSCOUNTRY;
	
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
	String totalAmount= (String) grnbillHdr.get("TOTAL_AMOUNT");
	double dTotalAmount ="".equals(totalAmount) ? 0.0d :  Double.parseDouble(totalAmount);
	totalAmount = StrUtils.addZeroes(dTotalAmount, numberOfDecimal);
	String subTotal = (String) grnbillHdr.get("AMOUNT");
	double dSubTotal ="".equals(subTotal) ? 0.0d :  Double.parseDouble(subTotal);
	subTotal = StrUtils.addZeroes(dSubTotal, numberOfDecimal);	
	 //String discount = (String) billHdr.get("DISCOUNT");
	 String discount ="0";
	double dDiscount ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
	discount = StrUtils.addZeroes(dDiscount, numberOfDecimal);
String sqty = (String)grnbillHdr.get("QTY");
	
	double dqtyt ="".equals(sqty) ? 0.0d :  Double.parseDouble(sqty);
	String tqtydecimal = String.format("%.3f", dqtyt); 
	/* String adjustment = (String) billHdr.get("ADJUSTMENT");
	double dAdjustment ="".equals(adjustment) ? 0.0d :  Double.parseDouble(adjustment);
	adjustment = StrUtils.addZeroes(dAdjustment, numberOfDecimal); */
	
	Map jno = (Map)grnbillDetList.get(0);
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	 <jsp:param name="submenu" value="<%=IConstants.RECEIPT%>"/>
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

@media print {
  @page { margin: 0; }
  body { margin: 1.6cm; }
}
</style>
<input type="text" name="STATUS" id="STATUS" value="<%=grnbillHdr.get("STATUS")%>" hidden>
<div class="container-fluid m-t-20">
	<div class="box">
	<ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
               <li><a href="../goods/summary"><span class="underline-on-hover">Goods Receipt Summary</span> </a></li>
                <li><label>Goods Receipt Detail</label></li>                                   
            </ol> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
					<!-- <button type="button" class="btn btn-default"
					 data-toggle="tooltip"  data-placement="bottom" title="Edit">
						<i class="fa fa-pencil" aria-hidden="true"></i>
					</button> -->
					<% if (displayPdfPrint) { %>
					<button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<% } %>
					<% if (displayPdfPrint) { %>
					<button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					<% } %>
				</div>
				<%if(!((String)grnbillHdr.get("STATUS")).equalsIgnoreCase("NON BILLABLE")){ %>
				<% if (displayConvertToBill) { %>
				<%if(!((String)grnbillHdr.get("STATUS")).equalsIgnoreCase("RETURNED")){ %>
				&nbsp;
				<div class="btn-group" role="group">
				<button type="button pull-right" class="btn btn-success" id="btnBill" onClick=" if ('<%=grnbillHdr.get("STATUS")%>'!='BILLED') { window.location.href='../bill/new?action=View&PONO=<%=pono%>&GRNO=<%=grno%>&VEND_NAME=<%=vendname%>&VENDNO=<%=vendno%>&refnum=<%=jno.get("JOBNO")%>&TAXTREATMENT=<%=sTAXTREATMENT%>&REVERSECHARGE=<%=sREVERSECHARGE%>&GOODSIMPORT=<%=sGOODSIMPORT%>&isgrn=1'}" >Convert to Bill</button>
				</div>
				<%} %>
				<% } %>
				<% } %>
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../goods/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;">
		<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-draft"><%=grnbillHdr.get("STATUS")%></div>
		</div>
		<div style="height: 0.700000in;"></div>
		<span id="print_id">
		<div class="row">
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

			<div class="col-xs-6 text-right">
				<h1>GRN</h1>
				<p># <%=grno%></p>
				<h1>Order Number</h1>
				<p># <%=pono%></p>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-8">
				<span>Supplier Address</span>		
				<a href="#" style="white-space: pre-wrap; word-wrap: break-word;">
<%=vendname%>
<%=toAddress_BlockAddress%>
<%=toAddress_RoadAddress%>
<%=toAddress_Country%> <%=CUSZIP%>
				</a>				
			</div>
			<div class="col-xs-4 text-right">
				<table id="table1" class="table pull-right">
					<tbody>
						<tr>
							<td class="text-right">GR Date :</td>
							<td><%=grnbillHdr.get("CRAT")%></td>
						</tr>
						
					</tbody>
				</table>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-12">
				<table id="table2" class="table">
					<thead>
						<tr>
							<td>#</td>
							<td>Product ID</td>
							<td>UOM</td>
							<td class="text-right">Qty</td>
						</tr>
					</thead>
					<tbody>
					<%for(int i =0; i<grnbillDetList.size(); i++) {   
				  		Map m=(Map)grnbillDetList.get(i);
				  		String qty="";
				  		
			  			qty = (String) m.get("QTY");
			  			double dQty ="".equals(qty) ? 0.0d :  Double.parseDouble(qty);
			  			qty = StrUtils.addZeroes(dQty, "3");
			  			
		  			
			  		%>
				  		<tr>
							<td class="text-center"><%=m.get("LNNO") %></td>
							<td class="text-center"><%=m.get("ITEM") %></td>
							<td class="text-center"><%=m.get("UOM")%></td>
							<td class="text-right"><%=qty%></td>
							
						</tr>
						<tr>
          					<td></td>
          					<td colspan="3">&emsp;&emsp;&emsp;<%=m.get("ITEMDESC") %></td>
						</tr>
				  	<%}%>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-9"></div>
			<div class="col-xs-3">
				<table id="table3" class="table text-right">
					<tbody>
						<tr>
							<td>Total Qty</td>
							
							<td><%=tqtydecimal%></td>
						</tr>
						
					</tbody>
				</table>
			</div>
			
		</div>
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
				doc.setFontSize(20);
				doc.text('GRN', 195, 40, {align:'right'});

				doc.setFontSize(10);
				doc.setFontStyle("normal");
				doc.text('#<%=grno%>', 195, 47, {align:'right'});
				
				doc.setFontSize(20);
				doc.setFontStyle("normal");
				doc.text('Order Number', 195, 60, {align:'right'});
				
				doc.setFontSize(10);
				doc.setFontStyle("normal");
				doc.text('#<%=pono%>', 195, 67, {align:'right'});
				

				doc.autoTable({
					html : '#table1',
					startY : 80,
					margin : {left : 142},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
					theme : 'plain'
				});
				/* **** */

				/* Top Left */
				
				doc.addImage(dataUrl, 'JPEG', 16, 23, 35,15);
				
				doc.setFontSize(10);
				doc.setFontStyle("bold");
				doc.text('<%=PLNTDESC%>', 16, 46);

				doc.setFontSize(9);
				doc.setFontStyle("normal");
				doc.text('<%=fromAddress_BlockAddress%>', 16, 50);

				doc.text('<%=fromAddress_RoadAddress%>', 16, 54);

				doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 58);

				//doc.text('United Arab Emirates', 16, 73);

				doc.setFontSize(10);
				doc.setFontStyle("bold");
				doc.text('Supplier Address', 16, 79);

				doc.setFontStyle("normal");
				var splitvendname = doc.splitTextToSize('<%=vendname%>', 90);
				doc.text(splitvendname, 16, 83);
				doc.setFontStyle("normal");
				doc.text('<%=toAddress_BlockAddress%>', 16, 87);

				doc.text('<%=toAddress_RoadAddress%>', 16, 91);

				doc.text('<%=toAddress_Country%> <%=CUSZIP%>', 16, 95);
				
				/* **** */
				var totalPagesExp = "{total_pages_cont_string}";
				doc.fromHTML(document.getElementById('table'));
				doc.autoTable({
					html : '#table2',
					startY : 105,
					headStyles : {
						fillColor : [ 0, 0, 0 ],
						textColor : [ 255, 255, 255 ],
						halign : 'center'
					},
					bodyStyles : {
						fillColor : [ 255, 255, 255 ],
						textColor : [ 0, 0, 0 ]
					},
					theme : 'plain',
					columnStyles: {0: {halign: 'center'},1: {halign: 'center'},2: {halign: 'center'},3: {halign: 'right'}},
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
						doc.text(str, 180,
								pageHeight - 10);
					}
				});

				let finalY = doc.previousAutoTable.finalY;

				doc.autoTable({
					html : '#table3',
					margin : {left : 123},
					columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
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
					doc.text(str, 180, pageHeight - 10);
				}
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					doc.putTotalPages(totalPagesExp);
				}
				doc.save('GRNO.pdf');
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
			$(document).ready(function(){
	            var inputVal = document.getElementById("STATUS").value;
			if(inputVal==='BILLED')
				{
				  var x = document.getElementById("btnBill");
				  x.style.display = "none";
				}
			});
		</script>
	</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>