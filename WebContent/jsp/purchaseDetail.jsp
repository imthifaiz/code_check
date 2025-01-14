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
	String title = "Purchase Detail";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String pono = StrUtils.fString(request.getParameter("PONO"));
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
	Map plntMap = (Map) plntList.get(0);
	String contactName = (String) plntMap.get("NAME");
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
	
	ArrayList poHdrDetails = new ArrayList();
	ArrayList poDetDetails = new ArrayList();
	POUtil _POUtil = new POUtil();
	PoDetDAO _PoDetDAO = new PoDetDAO();
	Hashtable htCond = new Hashtable();
	htCond.put("PLANT", plant);
	htCond.put("PONO", pono);
	Map doHdrMap = null;
	String orderDate = "", deliveryDate = "", shippingCost = "", supplier = "", dataOrderDiscount = "", ibgst = "", 
			status = "", vendno = "";
	htCond.put("PLANT", plant);
	htCond.put("PONO", pono);

	String query = " a.pono,isnull(a.ordertype,'') ordertype,"
			+ "(select isnull(display,'') display from " + "[" + plant
			+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,"
			+ "isnull(a.inbound_Gst,0) inbound_Gst,b.vname as custName,a.custCode,isnull(a.jobNum,'') as jobNum,"
			+ "a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,isnull(a.remark1,'') as remark1,"
			+ "isnull(a.remark2,'') as remark2,isnull(a.remark3,'') as remark3,isnull(b.name,'') as contactname,"
			+ "isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,"
			+ "isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,"
			+ "isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.deldate,'') as deldate,"
			+ "isnull(STATUS_ID,'') as statusid, STATUS, "
			+ "isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer,"
			+ "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost,"
			+ "isnull(incoterms,'') as incoterms,isnull(a.PAYMENTTYPE,'') payment_terms,isnull(a.DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,isnull(LOCALEXPENSES,0) as localexpenses";
	_POUtil.setmLogger(mLogger);
	poHdrDetails = _POUtil.getSupplierHdrDetails(query, htCond, "");
	if (poHdrDetails.size() > 0) {
		doHdrMap = (Map) poHdrDetails.get(0);
		orderDate = (String)doHdrMap.get("collectionDate");
		deliveryDate = (String)doHdrMap.get("deldate");
		shippingCost= (String) doHdrMap.get("shippingcost");
		double shippingcostVal ="".equals(shippingCost) ? 0.0d :  Double.parseDouble(shippingCost);
		shippingCost = StrUtils.addZeroes(shippingcostVal, numberOfDecimal);
		vendno = (String)doHdrMap.get("custCode");
		supplier = (String)doHdrMap.get("custName");
		dataOrderDiscount = (String)doHdrMap.get("orderdiscount");
		ibgst = (String)doHdrMap.get("inbound_Gst");
		status = (String)doHdrMap.get("STATUS");
		if(status.equalsIgnoreCase("N"))
			status="DRAFT";
		else if(status.equalsIgnoreCase("O"))
			status="OPEN";
		else if(status.equalsIgnoreCase("C"))
			status="CLOSED";
	}
	
	query = "pono,polnno,lnstat,item,qtyor,isnull(qtyrc,0) as qtyrc,isnull(PRODUCTDELIVERYDATE,'') as PRODUCTDELIVERYDATE,"
			+"isnull(userfld4,'') as manufacturer,isnull(unitcost,0)*isnull(currencyuseqt,0)unitcost,"
			+"isnull(comment1,'') as prdRemarks,isnull(prodgst,0) prodgst,UNITMO,"
			+"CAST(isnull(unitcost,0)*isnull(currencyuseqt,0) AS DECIMAL(20," + DbBean.NOOFDECIMALPTSFORCURRENCY + ")) as Convcost";
	_PoDetDAO.setmLogger(mLogger);
	poDetDetails = _PoDetDAO.selectPoDet(query, htCond," plant <> '' order by polnno", plant);
	
	double dSubTotal = 0.00;
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/font-awesome.min.css">
<script type="text/javascript" src="js/general.js"></script>
<script src="js/jspdf.debug.js"></script>
<script src="js/jspdf.plugin.autotable.js"></script>
<style>
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
#itemTable thead{
	text-align: center;
	background: black;
	color: white;
}
#itemTable>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}
</style>
<div class="container-fluid m-t-20">
	<div class="box">
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-default" onclick="editPage()"
					 data-toggle="tooltip"  data-placement="bottom" title="Edit">
						<i class="fa fa-pencil" aria-hidden="true"></i>
					</button>
					<button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
				</div>
				&nbsp;
				<div class="btn-group" role="group">
					<button type="button pull-right" class="btn btn-success" onclick="convertToBill()">Convert to Bill</button>
				</div>
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;">
<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
			<div class="ribbon-inner ribbon-draft"><%=status%></div>
		</div>
		<div style="height: 0.700000in;"></div>
     <table style="width:100%;table-layout: fixed;">
       <tbody>
         <tr>
           <td style="vertical-align: top; width:50%;">
             <div>
				<img src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>" style="width: 130.00px;" id="logo_content">
            </div>
            <span class="pcs-orgname"><b><%=PLNTDESC%></b></span><br>
			<span class="pcs-label">
				<span style="white-space: pre-wrap;word-wrap: break-word;" id="tmp_org_address">
<%=fromAddress_BlockAddress%>
<%=fromAddress_RoadAddress%>
<%=fromAddress_Country%> <%=ZIP%>
				</span>
            </span>           
			</td>
           <td style="vertical-align: top; text-align:right;width:50%;">
             <h1>PURCHASE ORDER</h1>
             <span id="tmp_entity_number" class="pcs-label"><b># <%=pono%></b></span>
             <div style="clear:both;margin-top:20px;">
             </div>
          </td>
         </tr>
       </tbody>
      </table>

     <table style="width:100%;margin-top:30px;table-layout:fixed;">
     	<tbody><tr>
     	<td style="width:55%;vertical-align:bottom;word-wrap: break-word;">
<div><label style="font-size: 10pt;" id="tmp_billing_address_label" class="pcs-label">Vendor Address</label>
            <br>
			<span style="white-space: pre-wrap;" id="tmp_billing_address"><strong><span class="pcs-customer-name" id="zb-pdf-customer-detail"><a href="#"><%=supplier%></a></span></strong></span>
    		</div>           
    		<div style="clear:both;width:50%;margin-top: 20px;">
             <label style="font-size: 10pt;" id="tmp_shipping_address_label" class="pcs-label">Deliver To</label>
           <span style="white-space: pre-wrap;" id="tmp_shipping_address">
<%=contactName%>
<%=fromAddress_BlockAddress%>
<%=fromAddress_RoadAddress%>
<%=fromAddress_Country%> <%=ZIP%>
</span>
         </div>

     	</td>
     	<td align="right" style="vertical-align:bottom;width: 45%;">
     		<table id="table1" style="float:right;width: 100%;table-layout: fixed;word-wrap: break-word;" border="0" cellspacing="0" cellpadding="0">
                 <tbody>
                     <tr>
                         <td style="text-align:right;padding:5px 10px 5px 0px;font-size:10pt;width:60%;">
                            <span class="pcs-label">Date :</span>
                        </td>
                        <td style="text-align:right;width:40%;">
                            <span id="tmp_entity_date"><%=orderDate%></span>
                        </td>
                    </tr>


                    <tr>
                         <td style="text-align:right;padding:5px 10px 5px 0px;font-size: 10pt;width:60%;">
                            <span class="pcs-label">Delivery Date :</span>
                        </td>
                        <td style="text-align:right;width:40%;">
                            <span id="tmp_due_date"><%=deliveryDate%></span>
                        </td>
                    </tr>

                    <tr>
                    	<td style="text-align:right;padding:5px 10px 5px 0px;font-size: 10pt;width:60%;">
                         	<span class="pcs-label" id="tmp_entity_cflabel">Shipping Cost :</span>
                      </td>
                      <td style="text-align:right;width:40%;">
                          <span id="tmp_entity_cfvalue"><%=shippingCost%></span>
                      </td>
                    </tr>

                 </tbody>
              </table>
     	</td>
     	</tr>
     </tbody></table>

  <table id="itemTable" style="width:100%;margin-top:20px;table-layout:fixed;"  class="table" border="0" cellspacing="0" cellpadding="0">
    <thead>
        <tr style="height:32px;">
                <td style="padding: 5px 0px 5px 5px;width: 5%;text-align: center;" id="" class="pcs-itemtable-header pcs-itemtable-breakword">
      #
    </td>
    <td style="padding: 5px 10px 5px 20px;width: ;text-align: left;" id="" class="pcs-itemtable-header pcs-itemtable-breakword">
      Product Details
    </td>
    <td style="padding: 5px 10px 5px 20px;width: ;text-align: left;" id="" class="pcs-itemtable-header pcs-itemtable-breakword">
      UOM
    </td>
    <td style="padding: 5px 10px 5px 5px;width: 11%;text-align: right;" id="" class="pcs-itemtable-header pcs-itemtable-breakword">
      Qty
    </td>
    <td style="padding: 5px 10px 5px 5px;width: 11%;text-align: right;" id="" class="pcs-itemtable-header pcs-itemtable-breakword">
      Rate
    </td>
    <td style="padding: 5px 10px 5px 5px;width: 15%;text-align: right;" id="" class="pcs-itemtable-header pcs-itemtable-breakword">
      Amount
    </td>

        </tr>
     </thead>
     <tbody class="itemBody">
     <% for (int i = 0; i < poDetDetails.size(); i++) { 
    		 Map m = (Map) poDetDetails.get(i);
			String polnno = (String) m.get("polnno");
			String item = (String) m.get("item");
			String uom = (String) m.get("UNITMO");
			
			String qtyOr = (String) m.get("qtyor");
			float qtyOrValue="".equals(qtyOr) ? 0.0f :  Float.parseFloat(qtyOr);
			if(qtyOrValue==0f){
				qtyOr="0.000";
			}else{
				qtyOr=qtyOr.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			}
			
			String convcost =	(String) m.get("Convcost");
			convcost=convcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			double dCost = Double.parseDouble(convcost);
			convcost = StrUtils.addZeroes(dCost, numberOfDecimal);
			
			double dAmount = (Double.parseDouble(qtyOr) * Double.parseDouble(convcost));
			String amount = StrUtils.addZeroes(dAmount, numberOfDecimal);
			
			dSubTotal += Double.parseDouble(amount);
     %>
	     <tr class="breakrow-inside breakrow-after">
	     	<td valign="top" style="padding: 10px 0 10px 5px;text-align: center;word-wrap: break-word;" class="pcs-item-row">
             <%=polnno%>
            </td>
            <td valign="top" style="padding: 10px 0px 10px 20px;" class="pcs-item-row">
				<%=item%>
            </td>
            <td valign="top" style="padding: 10px 0px 10px 20px;" class="pcs-item-row">
				<%=uom%>
            </td>
            <td valign="top" style="padding: 10px 10px 5px 10px;text-align:right;word-wrap: break-word;" class="pcs-item-row">
                  <span id="tmp_item_rate"><%=qtyOr%></span>
            </td>
            <td valign="top" style="padding: 10px 10px 5px 10px;text-align:right;word-wrap: break-word;" class="pcs-item-row">
                  <span id="tmp_item_rate"><%=convcost%></span>
            </td>
            <td valign="top" style="text-align:right;padding: 10px 10px 10px 5px;word-wrap: break-word;" class="pcs-item-row">
              <span id="tmp_item_amount"><%=amount%></span>
            </td>
	     </tr>
     <%} %>  
    </tbody>
    </table>
    <%
    double doubledataorderdiscount = new Double(dataOrderDiscount);
    
    double orderDiscount = ((dSubTotal)*(doubledataorderdiscount/100));
    
    double dDiscountTotal = dSubTotal + orderDiscount;
    String discountTotal =  StrUtils.addZeroes(dDiscountTotal, numberOfDecimal);
    
    double dtax = (Double.parseDouble(discountTotal)*(Double.parseDouble(ibgst)/100));
    String tax = StrUtils.addZeroes(dtax, numberOfDecimal);
    
    double dTaxTotal = Double.parseDouble(discountTotal) + Double.parseDouble(shippingCost) + Double.parseDouble(tax);
    String taxTotal = StrUtils.addZeroes(dTaxTotal, numberOfDecimal);
    
    %>
   <div style="width: 100%;margin-top: 1px;">
    <div style="width: 45%;padding: 3px 10px 3px 3px;font-size: 9pt;float: left;">
      <div style="white-space: pre-wrap;"></div>
    </div>
    <div style="width: 50%;float:right;">
      <table id="table3" class="pcs-totals" cellspacing="0" border="0" width="100%">
        <tbody>
          <tr>
                <td valign="middle" align="right" style="padding: 5px 10px 5px 0;">Sub Total</td>
                <td id="tmp_subtotal" valign="middle" align="right" style="width:120px;padding: 10px 10px 10px 5px;"><%=StrUtils.addZeroes(dSubTotal, numberOfDecimal)%></td>
          </tr>
          <tr style="height:10px;">
            <td valign="middle" align="right" style="padding: 5px 10px 5px 0;">Order Discount (<%=doubledataorderdiscount%>%)  </td>
            <td valign="middle" align="right" style="width:120px;padding: 10px 10px 10px 5px;"><%=orderDiscount%></td>
          </tr>
		  <tr>
            <td valign="middle" align="right" style="padding: 5px 10px 5px 0;">Total After Discount</td>
            <td id="tmp_total" valign="middle" align="right" style="width:120px;;padding: 10px 10px 10px 5px;"><%=discountTotal%></td>
          </tr>
          <tr>
            <td valign="middle" align="right" style="padding: 5px 10px 5px 0;">Shipping Cost (<%=curency%>)</td>
            <td id="tmp_total" valign="middle" align="right" style="width:120px;;padding: 10px 10px 10px 5px;"><%=shippingCost%></td>
          </tr>
          <tr>
            <td valign="middle" align="right" style="padding: 5px 10px 5px 0;">Tax (<%=ibgst%>)</td>
            <td id="tmp_total" valign="middle" align="right" style="width:120px;;padding: 10px 10px 10px 5px;"><%=tax%></td>
          </tr>
          <tr>
            <td valign="middle" align="right" style="padding: 5px 10px 5px 0;"><b>Total (<%=curency%>)</b></td>
            <td id="tmp_total" valign="middle" align="right" style="width:120px;;padding: 10px 10px 10px 5px;"><b><%=taxTotal%></b></td>
          </tr>
        </tbody>
      </table>
    </div>
    <div style="clear: both;"></div>
  </div>
</div>
	</div>  
</div>
<script>
function convertToBill(){
	var pono = '<%=pono%>';
	var vendno = '<%=vendno%>';
	var vendname = '<%=supplier%>';
	window.location.href = "createBill.jsp?PONO="+pono+"&VENDNO="+vendno+"&VEND_NAME="+vendname;
	
}

function editPage(){
	var pono = '<%=pono%>';
	window.location.href = "/track/purchaseorderservlet?PONO="+pono+"&RFLAG=4&Submit=View";
}

$(document).ready(function(){
	  $('[data-toggle="tooltip"]').tooltip();   
	});
	
	function generatePdf(dataUrl){
		var doc = new jsPDF('p', 'mm', 'a4');
		var pageNumber;
		/* Top Right */
		doc.setFontSize(27);
		doc.text('PURCHASE ORDER', 118, 30);

		doc.setFontSize(10);
		doc.setFontStyle("bold");
		doc.text('# <%=pono%>', 177, 37);

		/*doc.setFontSize(8);
		doc.text('Balance Due', 180, 46);

		doc.setFontSize(12);
		doc.text('', 178, 52);*/

		doc.autoTable({
			html : '#table1',
			startY : 83,
			margin : {left : 142},
			columnStyles : {0 : {halign : 'right'},1 : {halign : 'right'}},
			styles: {cellPadding: 0.5, fontSize: 9},
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
		doc.text('Vendor Address', 16, 69);//99

		doc.setFontStyle("bold");
		doc.text('<%=supplier%>', 16, 73);//103
		
		doc.setFontSize(9);
		doc.setFontStyle("normal");
		doc.text('Deliver To', 16, 80);
		doc.text('<%=contactName%>', 16, 84);
		doc.text('<%=fromAddress_BlockAddress%>', 16, 88);
		doc.text('<%=fromAddress_RoadAddress%>', 16, 92);
		doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, 96);
		/* **** */
		var totalPagesExp = "{total_pages_cont_string}";
		doc.fromHTML(document.getElementById('table'));
		doc.autoTable({
			html : '#itemTable',
			startY : 110,
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
			columnStyles: {0: {halign: 'center'},1: {halign: 'left'},2: {halign: 'center'},3: {halign: 'center'},4: {halign: 'right'},5: {halign: 'right'}},
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
				doc.text(str, data.settings.margin.left,
						pageHeight - 10);
			}
		});

		let finalY = doc.previousAutoTable.finalY;

		doc.autoTable({
			html : '#table3',
			margin : {left : 123},
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
 
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>