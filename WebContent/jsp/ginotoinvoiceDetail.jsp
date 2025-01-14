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
	String title = "Goods Issued Detail";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	boolean printginotoinvoice=false,convertginotoinvoice=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		printginotoinvoice = ub.isCheckValAcc("printginotoinvoice", plant,username);
		convertginotoinvoice = ub.isCheckValAcc("convertginotoinvoice", plant,username);
	}	
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	InvoiceUtil invoiceUtil = new InvoiceUtil();
	InvoiceDAO invoiceDao = new InvoiceDAO(); 
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String dono = StrUtils.fString(request.getParameter("DONO"));
	String gino = StrUtils.fString(request.getParameter("GINO"));
	String CUSTOMER = StrUtils.fString(request.getParameter("CUST_NAME"));
	String custno = StrUtils.fString(request.getParameter("CUSTNO"));
	String sql ="";
	Hashtable ht = new Hashtable();	

	ht.put("GINO",gino);
	if(!dono.equalsIgnoreCase("")){
		ht.put("A.DONO",dono);
	 sql = "SELECT CNAME,ISNULL(NAME,'') as NAME,ISNULL(ADDR1,'') as ADDR1,ISNULL(ADDR2,'') as ADDR2,ISNULL(ADDR3,'') as ADDR3,ISNULL(ADDR4,'') as ADDR4,ISNULL(STATE,'') as STATE,ISNULL(COUNTRY,'') as COUNTRY,ISNULL(ZIP,'') as ZIP,(SUBSTRING(A.CRAT, 7, 2) + '/' + SUBSTRING(A.CRAT, 5, 2) + '/' + SUBSTRING(A.CRAT, 1, 4)) as CRAT,A.STATUS,AMOUNT,ISNULL((P.TAXTREATMENT),'') as TAXTREATMENT,P.SHIPPINGCOST,P.ORDERDISCOUNT,P.SALES_LOCATION,ISNULL((SELECT PREFIX FROM FINSALESLOCATION C WHERE P.SALES_LOCATION = C.STATE),'') as STATE_PREFIX,cast(((((AMOUNT+P.SHIPPINGCOST)*P.OUTBOUND_GST)/100)+(AMOUNT+P.SHIPPINGCOST)) as DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as TOTAL_AMOUNT,ISNULL(QTY,0) QTY FROM " + plant +"_FINGINOTOINVOICE A JOIN " + plant +"_CUSTMST V ON V.CUSTNO=A.CUSTNO JOIN " + plant +"_DOHDR P ON P.DONO=A.DONO WHERE A.PLANT='"+ plant+"'";
	}else{
		sql = "SELECT ''CNAME,''NAME,''ADDR1,''ADDR2,''ADDR3,''ADDR4,''STATE,'' COUNTRY,''ZIP,(SUBSTRING(A.CRAT, 7, 2) + '/' + SUBSTRING(A.CRAT, 5, 2) + '/' + SUBSTRING(A.CRAT, 1, 4)) as CRAT,A.STATUS,AMOUNT,''TAXTREATMENT,0 as SHIPPINGCOST,0 as ORDERDISCOUNT,''SALES_LOCATION,'' as STATE_PREFIX,0 as TOTAL_AMOUNT,ISNULL(QTY,0) QTY FROM " + plant +"_FINGINOTOINVOICE A WHERE A.PLANT='"+ plant+"'";	
	}
	List arrList = invoiceDao.selectForReport(sql, ht, "");
	Map grninvoiceHdr=(Map)arrList.get(0);
	ht = new Hashtable();	
	
	ht.put("INVOICENO",gino);
	if(!dono.equalsIgnoreCase("")){
		ht.put("A.DONO",dono);
		sql = "SELECT DOLNO as LNNO,ITEM,ITEMDESC,PICKQTY as QTY,ISNULL(UNITPRICE,0)*ISNULL((SELECT ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = (ISNULL((SELECT ISNULL(B.CURRENCYID,0)  FROM ["+plant+"_DOHDR] B WHERE B.DONO = A.DONO),''))),0) as COST,CURRENCYID,(PICKQTY*(ISNULL(UNITPRICE,0)*ISNULL((SELECT ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = (ISNULL((SELECT ISNULL(B.CURRENCYID,0)  FROM ["+plant+"_DOHDR] B WHERE B.DONO = A.DONO),''))),0))) as AMOUNT,ISNULL((SELECT ISNULL(B.OUTBOUND_GST,0) OUTBOUND_GST FROM ["+plant+"_DOHDR] B WHERE B.DONO = A.DONO),0) AS GSTPERCENTAGE,ISNULL((SELECT ISNULL(UNITMO,'') UNITMO FROM ["+plant+"_DODET]  WHERE item=a.item and dono=a.dono and DOLNNO=a.DOLNO),0) as UOM from "+plant+"_SHIPHIS A WHERE A.PLANT='"+ plant+"'";
	}
	else{
		sql = "SELECT DOLNO as LNNO,ITEM,ITEMDESC,PICKQTY as QTY,0 as COST,'' as CURRENCYID,(PICKQTY*(ISNULL(UNITPRICE,0)*ISNULL((SELECT ISNULL(CURRENCYUSEQT,'') CURRENCYUSEQT FROM ["+plant+"_CURRENCYMST] WHERE CURRENCYID = (ISNULL((SELECT ISNULL(B.CURRENCYID,0)  FROM ["+plant+"_DOHDR] B WHERE B.DONO = A.DONO),''))),0))) as AMOUNT,ISNULL((SELECT ISNULL(B.OUTBOUND_GST,0) OUTBOUND_GST FROM ["+plant+"_DOHDR] B WHERE B.DONO = A.DONO),0) AS GSTPERCENTAGE,ISNULL((SELECT TOP 1 ISNULL(UNITMO,'') UNITMO FROM ["+plant+"_POSDET]  WHERE item=a.item and POSTRANID=a.INVOICENO and DOLNNO=a.DOLNO),0) as UOM from "+plant+"_SHIPHIS A WHERE A.PLANT='"+ plant+"'";	
	}
		
	List grninvoiceDetList =  invoiceDao.selectForReport(sql, ht, " ORDER BY DOLNO + 0 ");
	String CUSADD1 = (String) grninvoiceHdr.get("ADDR1");
	String CUSADD2 = (String) grninvoiceHdr.get("ADDR2");
	String CUSADD3 = (String) grninvoiceHdr.get("ADDR3");
	String CUSADD4 = (String) grninvoiceHdr.get("ADDR4");
	String CUSSTATE = (String) grninvoiceHdr.get("STATE");
	String CUSCOUNTRY = (String) grninvoiceHdr.get("COUNTRY");
	String CUSZIP = (String) grninvoiceHdr.get("ZIP");
	
	String sTAXTREATMENT = (String) grninvoiceHdr.get("TAXTREATMENT");
	String sSTATE_PREFIX = (String) grninvoiceHdr.get("STATE_PREFIX");
	double shingpercentage =0.0;
	String shipingcost = (String) grninvoiceHdr.get("SHIPPINGCOST");
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
	String totalAmount= (String) grninvoiceHdr.get("TOTAL_AMOUNT");
	double dTotalAmount ="".equals(totalAmount) ? 0.0d :  Double.parseDouble(totalAmount);
	totalAmount = StrUtils.addZeroes(dTotalAmount, numberOfDecimal);
	String subTotal = (String) grninvoiceHdr.get("AMOUNT");
	double dSubTotal ="".equals(subTotal) ? 0.0d :  Double.parseDouble(subTotal);
	subTotal = StrUtils.addZeroes(dSubTotal, numberOfDecimal);
	 String discount ="0";
		double dDiscount ="".equals(discount) ? 0.0d :  Double.parseDouble(discount);
		discount = StrUtils.addZeroes(dDiscount, numberOfDecimal);
		
		String sqty = (String)grninvoiceHdr.get("QTY");
		
		double dqtyt ="".equals(sqty) ? 0.0d :  Double.parseDouble(sqty);
		String tqtydecimal = String.format("%.3f", dqtyt);
	
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.GOODS_ISSUED%>"/>
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
<input type="text" name="STATUS" id="STATUS" value="<%=grninvoiceHdr.get("STATUS")%>" hidden>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>                
                <li><a href="../goodsissued/summary"><span class="underline-on-hover">Goods Issued Summary</span> </a></li>                
                <li><label>Goods Issued Detail</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
					<%if(printginotoinvoice){ %>
					<button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					<%} %>
				</div>
				<%if(!((String)grninvoiceHdr.get("STATUS")).equalsIgnoreCase("NON INVOICEABLE")){ %>
				<%if(convertginotoinvoice){ %>
				<%if(!((String)grninvoiceHdr.get("STATUS")).equalsIgnoreCase("RETURNED")){ %>
				&nbsp;
				<div class="btn-group" role="group">
					<button type="button pull-right" class="btn btn-success" id="btnInvoice" onClick=" if ('<%=grninvoiceHdr.get("STATUS")%>'!='INVOICED') { window.location.href='../goodsissued/converttoinvoice?cmd=IssueingGoodsInvoice&DONO=<%=dono%>&GINO=<%=gino%>&invcusnum=<%=CUSTOMER%>&CUST_CODE=<%=custno%>&TAXTREATMENT=<%=sTAXTREATMENT%>'}" >Convert to Invoice</button>
				</div>
				<%} }%>
				<%} %>
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../goodsissued/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;">
		<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-draft"><%=grninvoiceHdr.get("STATUS")%></div>
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
				<h1>GINO</h1>
				<p># <%=gino%></p>
				<h1>Order Number</h1>
				<p># <%=dono%></p>
			</div>
		</div>

		<div class="row">
			<div class="col-xs-8">
				<span>Customer Address</span>			
				<a href="#" style="white-space: pre-wrap; word-wrap: break-word;">
<%=CUSTOMER%>
<%=toAddress_BlockAddress%>
<%=toAddress_RoadAddress%>
<%=toAddress_Country%> <%=CUSZIP%>
				</a>				
			</div>
			<div class="col-xs-4 text-right">
				<table id="table1" class="table pull-right">
					<tbody>
						<tr>
							<td>GI Date :</td>
							<td><%=grninvoiceHdr.get("CRAT")%></td>
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
					<%for(int i =0; i<grninvoiceDetList.size(); i++) {   
				  		Map m=(Map)grninvoiceDetList.get(i);
				  		String qty="", cost="", amount="", percentage="", tax="";
				  		
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
          					<td colspan="6">&emsp;&emsp;&emsp;<%=m.get("ITEMDESC") %></td>
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
				doc.text('GINO', 195, 40, {align:'right'});

				doc.setFontSize(10);
				doc.setFontStyle("normal");
				doc.text('#<%=gino%>', 195, 47, {align:'right'});
				
				doc.setFontSize(20);
				doc.setFontStyle("normal");
				doc.text('Order Number', 195, 60, {align:'right'});
				
				doc.setFontSize(10);
				doc.setFontStyle("normal");
				doc.text('#<%=dono%>', 195, 67, {align:'right'});
				

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
				doc.text('Customer Address', 16, 79);

				doc.setFontStyle("normal");
				var splitcustname = doc.splitTextToSize('<%=CUSTOMER%>', 90);
				doc.text(splitcustname, 16, 83);
				doc.setFontStyle("normal");
				doc.text('<%=toAddress_BlockAddress%>', 16, 91);

				doc.text('<%=toAddress_RoadAddress%>', 16, 95);

				doc.text('<%=toAddress_Country%> <%=CUSZIP%>', 16, 99);
				
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
					
	                didDrawCell: function (data){
	                    if (data.section === 'head' && data.column.index === 4) {
	                    	data.cell.styles.halign = 'right';	
	                    }
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

				//let finalY = doc.previousAutoTable.finalY;

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
					doc.text(str, 16, pageHeight - 10);
				}
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					doc.putTotalPages(totalPagesExp);
				}
				doc.save('GINO.pdf');
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
			if(inputVal==='INVOICED')
				{
				  var x = document.getElementById("btnInvoice");
				  x.style.display = "none";
				}
			});
		</script>
	</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>