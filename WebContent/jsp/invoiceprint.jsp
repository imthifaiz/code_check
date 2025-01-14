<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Invoice Print";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));

String FROM_DATE ="",  TO_DATE = "",USER="",fdate="",tdate="",ORDERNO="",CUSTOMER="",PGaction="",invoice="",STATUS="",allChecked = "";


boolean displaySummaryNew=false,displaySummaryPrintBatch=false,displaySummaryPrintNoBatch=false,displaySummaryLink=false,displaySummaryExport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	displaySummaryNew = ub.isCheckValAcc("newinvoice", plant,username);
	displaySummaryPrintBatch = ub.isCheckValAcc("printpoinvoicebatch", plant,LOGIN_USER);
	displaySummaryPrintNoBatch = ub.isCheckValAcc("printponoinvoicebatch", plant,LOGIN_USER);
	displaySummaryLink = ub.isCheckValAcc("summarylnkinvoice", plant,username);
	displaySummaryExport = ub.isCheckValAcc("exportinvoice", plant,username);
	displaySummaryPrintBatch=true;
	displaySummaryPrintNoBatch=true;
}
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=du.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
allChecked = StrUtils.fString(request.getParameter("allChecked"));

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
String RCBNO = (String) plntMap.get("RCBNO");
String NAME = (String) plntMap.get("NAME");
String TELNO = (String) plntMap.get("TELNO");
String FAX = (String) plntMap.get("FAX");
String EMAIL = (String) plntMap.get("EMAIL");

double shingpercentage =0.0;
String fromAddress_BlockAddress = ADD1 + " " + ADD2;
String fromAddress_RoadAddress = ADD3 + " " + ADD4;
String fromAddress_Country = STATE + " " + COUNTRY;

String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
File checkImageFile = new File(imagePath);
if (!checkImageFile.exists()) {
	imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
}


String InboundOrderHeader = "", InvoiceOrderToHeader = "", shipto = "", OrderNo = "", invoicedate = "",
rcbno = "", customerrcbno = "", hscode = "", coo = "", remark1 = "", orderdiscountlbl = "",
Discount = "", shippingcost = "", incoterm = "", Terms = "", SoNo = "", Item = "",
OrderQty = "", UOM = "", Rate = "", totalafterdiscount = "", PreparedBy = "", Seller = "",
SellerSignature = "", Buyer = "", BuyerSignature = "",SubTotal = "",Attention = "",
Telephone = "", FaxLBL = "", Email = "", Brand = "",TotalDiscount = "",
Total = "", printDetailDesc = "", printwithbrand = "", printwithhscode ="",printEmployee="",
printWithDeliveryDate = "", printwithProduct ="",remark3 = "",
printwithcoo = "", Employee = "", duedate = "", Adjustment="",PaymentMade="",BalanceDue="";
Map invHdrDetails= new DOUtil().getDOReceiptInvoiceHdrDetailsDO(plant,"Tax Invoice English");
if(!invHdrDetails.isEmpty()){
InboundOrderHeader= (String) invHdrDetails.get("HDR1");
InvoiceOrderToHeader = (String) invHdrDetails.get("HDR2");
shipto = (String)invHdrDetails.get("SHIPTO");
OrderNo = (String)invHdrDetails.get("ORDERNO");
invoicedate = (String)invHdrDetails.get("INVOICEDATE");
rcbno = (String)invHdrDetails.get("RCBNO");
customerrcbno=(String)invHdrDetails.get("CUSTOMERRCBNO");
hscode=(String)invHdrDetails.get("HSCODE");
coo=(String)invHdrDetails.get("COO");
remark1=(String)invHdrDetails.get("REMARK1");
orderdiscountlbl=(String)invHdrDetails.get("ORDERDISCOUNT");
Discount = (String)invHdrDetails.get("DISCOUNT");
shippingcost=(String)invHdrDetails.get("SHIPPINGCOST");
incoterm=(String)invHdrDetails.get("INCOTERM");
Terms = (String) invHdrDetails.get("TERMS");
SoNo = (String) invHdrDetails.get("SONO");
Item = (String) invHdrDetails.get("ITEM");
OrderQty = (String) invHdrDetails.get("ORDERQTY");
UOM = (String) invHdrDetails.get("UOM");
Rate = (String) invHdrDetails.get("RATE");
totalafterdiscount=(String)invHdrDetails.get("TOTALAFTERDISCOUNT");
PreparedBy = (String) invHdrDetails.get("PREPAREDBY");
Seller = (String) invHdrDetails.get("SELLER");
SellerSignature = (String) invHdrDetails.get("SELLERSIGNATURE");
Buyer = (String) invHdrDetails.get("BUYER");
BuyerSignature = (String) invHdrDetails.get("BUYERSIGNATURE");
SubTotal = (String) invHdrDetails.get("SUBTOTAL");
Total = (String) invHdrDetails.get("TOTAL");
Employee=(String)invHdrDetails.get("EMPLOYEE");
duedate=(String)invHdrDetails.get("DELIVERYDATE");
Adjustment = (String)invHdrDetails.get("ADJUSTMENT");
PaymentMade = (String)invHdrDetails.get("PAYMENTMADE");
BalanceDue = (String)invHdrDetails.get("BALANCEDUE");
Attention = (String) invHdrDetails.get("ATTENTION");
Telephone = (String) invHdrDetails.get("TELEPHONE");
FaxLBL = (String) invHdrDetails.get("FAX");
Email = (String) invHdrDetails.get("EMAIL");
Brand = (String) invHdrDetails.get("BRAND");
TotalDiscount = (String) invHdrDetails.get("TOTALDISCOUNT");
remark3 = (String) invHdrDetails.get("REMARK3");

printDetailDesc = (String)invHdrDetails.get("PRINTXTRADETAILS");
printwithbrand = (String)invHdrDetails.get("PRINTWITHBRAND");
printwithhscode = (String)invHdrDetails.get("PRITNWITHHSCODE");
printwithcoo = (String)invHdrDetails.get("PRITNWITHCOO");
printEmployee=(String)invHdrDetails.get("PRINTEMPLOYEE");
printWithDeliveryDate = (String) invHdrDetails.get("PRINTWITHDELIVERYDATE");
printwithProduct = (String)invHdrDetails.get("PRINTWITHPRODUCT");

String curDate =du.getDateMinusDays();
FROM_DATE=du.getDateinddmmyyyy(curDate);
}

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.INVOICE%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script src="js/jspdf.debug.js"></script>
<script src="js/jspdf.plugin.autotable.js"></script>
<script src="js/JsBarcode.all.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script src="js/Invoice.js"></script>
<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function checkAll(isChk)
{
	var len = document.form1.chkdPoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form1.chkdPoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkdPoNo.checked = isChk;
               	}
              	else{
              		document.form1.chkdPoNo[i].checked = isChk;
              	}
            	
        }
    }
}

function onRePrint(){
	var checkFound = false;
	var len = document.form1.chkdPoNo.length;
	var plant = document.form1.plant.value;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdPoNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdPoNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdPoNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
    
	var TranId = null; 
	var inputElements = document.getElementsByClassName('messageCheckbox');
	var img = toDataURL($("#logo_content").attr("src"),
   			function(dataUrl) {
	var doc ="";
	doc = new jsPDF('p', 'mm', 'a4');
	for(var i=0; inputElements[i]; ++i){
	      if(inputElements[i].checked){
	    	  TranId = inputElements[i].value;
	           alert(TranId);
	           
	           $.ajax({
	       		type : "POST",
	       		url : "/track/InvoiceServlet",
	       		async : true,
	       		data : {
	       			PLANT : plant,
	       			Submit : "GET_EDIT_INVOICE_DETAILS",
	       			Id : TranId
	       		},
	       		dataType : "json",
	       		success : function(data1) {
	       			$.each(data1.orders, function( key, data ) {		
	       			var invoiceno =data.INVOICE;
	       			var DONO =data.ORDERNO;
	       			var DISPLAY =data.DISPLAY;
	       			//alert(invoiceno);
	           JsBarcode("#barCode", invoiceno, {format: "CODE128",displayValue: false});
	           			if(i>0)
	       				doc.addPage();
	           			//strat
	       				var pageNumber;
	       				
	       				var pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
	       				var pageWidth = doc.internal.pageSize.width || doc.internal.pageSize.getWidth();

	       				/* Top Left */
	       				
	       				/*From Address*/
	       				doc.addImage(dataUrl, 'JPEG', 16, 12, 35,15);
	       				var rY=35;	/*right Y-axis*/
	       				doc.setFontSize(10);
	       				doc.setFontStyle("bold");
	       				doc.text('<%=PLNTDESC%>', 16, rY+2);

	       				doc.setFontSize(9);
	       				doc.setFontStyle("normal");
	       				doc.text('<%=fromAddress_BlockAddress%>', 16, rY+=10);

	       				doc.text('<%=fromAddress_RoadAddress%>', 16, rY+=4);

	       				doc.text('<%=fromAddress_Country%> <%=ZIP%>', 16, rY+=4);
	       				
	       				doc.text('<%=rcbno%> <%=RCBNO%>', 16, rY+=4);
	       				
	       				doc.text('<%=Attention%> <%=NAME%>', 16, rY+=4);
	       				
	       				doc.text('<%=Telephone%> <%=TELNO%> <%=FaxLBL%> <%=FAX%>', 16, rY+=4);
	       				
	       				doc.text('<%=Email%> <%=EMAIL%>', 16, rY+=4);
	       				
	       				
	       				/* Top Right */
	       				doc.setFontSize(27);
	       				doc.text('<%=InboundOrderHeader%>', 195, 19, {align:'right'});
	       				
	       				const img = document.querySelector('img#barCode');
	    				doc.addImage(img.src, 'JPEG', 140, 20, 55,15);
	       				
	       				doc.setFontSize(10);
	       				//doc.setFontStyle("bold");
	       				doc.text('# '+ invoiceno , 180, 37, {align:'right'});
	       				if(DONO=""){
						doc.setFontSize(10);
						doc.text('Balance Due', 195, 46, {align:'right'});
		
						doc.setFontSize(12);
						doc.text(DISPLAY +'0', 195, 52, {align:'right'});
					   }else{
					
						doc.setFontSize(12);
						doc.text('<%=OrderNo%>', 195, 46, {align:'right'});
		
						doc.setFontSize(10);
						doc.text('# '+DONO, 195, 52, {align:'right'});						
		
						doc.setFontSize(10);
						doc.text('Balance Due', 195, 63, {align:'right'});
		
						doc.setFontSize(12);
						doc.text(DISPLAY +'0', 195, 70, {align:'right'});
					 }
	       				
	       				
	       				var totalPagesExp = "{total_pages_cont_string}";
	       				
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
	       					doc.text(str, 185, pageHeight - 10);
	       				}
	       				
	       				if(pageNumber == doc.internal.getNumberOfPages()){
	       					// Footer
	       					doc.setFontSize(10);
	       					var pageSize = doc.internal.pageSize;
	       					var pageHeight = pageSize.height ? pageSize.height : pageSize.getHeight();
	       					
	       				
	       				}
	       				// Total page number plugin only available in jspdf v1.0+
	       				if (typeof doc.putTotalPages === 'function') {
	       					doc.putTotalPages(totalPagesExp);
	       				}
	       				
	       			//end
	       			});
	       		}
	       		});
	      }
	}
	alert(doc);
	doc.save('InvoicePrint.pdf');
	
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

var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    // an array that will be populated with substring matches
	    matches = [];
	    // regex used to determine if a string contains the substring `q`
	    substrRegex = new RegExp(q, 'i');
	    // iterate through the pool of strings and for any string that
	    // contains the substring `q`, add it to the `matches` array
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};
</script>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <% if (displaySummaryNew) { %>
              <button type="button" class="btn btn-default" onClick="window.location.href='createInvoice.jsp'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;              
              <% }%>
              </div>
              </h1>
		</div>
		<div class="container-fluid">
		<FORM class="form-horizontal" name="form1" method="post" action="" >
		<input type="text" name="plant" value="<%=plant%>" hidden>
		<input type="text" name="cmd" value="<%="cmd"%>" hidden>
		<input type="text" name="TranId" value="<%="TranId"%>" hidden>
		<input type="text" name="curency" value="<%="curency"%>" hidden>
		<input type="text" name="CUST_CODE" hidden>
		<input type="text" name="STATE_PREFIX" hidden>
		<input hidden name="displayCustomerpop" id="displayCustomerpop" />
		<div style="display:none" >		
				<img src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>"
					style="width: 130.00px;" id="logo_content">
					<img id="barCode" style="width:215px;height:65px;">
		</div>
		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER">
				<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	
				</div>
			</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>" placeholder="ORDER NO">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="auto_invoiceNo" name="invoice" placeholder="INVOICE NO">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'invoice\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>  		
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="plnoInvoice" name="plno" placeholder="PACKING LIST">
		<span class="select-icon" onclick="$(this).parent().find('input[name=\'plno\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" name="item" id="item" class="ac-selected form-control" placeholder="PRODUCT" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'item\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	
  		<div class="col-sm-4 ac-box">
  		<input type="text" name="status" id="status" class="ac-selected form-control" placeholder="STATUS" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'status\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<INPUT id="EMP_NAME_SRH" name="EMP_NAME" type = "TEXT" class="ac-selected form-control" placeholder="Employee Name or ID">
<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
 <span class="btn-danger input-group-addon"
								onclick="javascript:popUpWin('employee_list.jsp?FORM=form1&TYPE=ESTIMATE&EMP_NAME='+form1.EMP_NAME.value);"><span
								class="glyphicon glyphicon-search" aria-hidden="true"></span></span>
			</div>	
			</div>
		</div>
		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
  	
  		<div class="col-sm-4 ac-box">
  		<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>
  		</div>
  		<div class="col-sm-4 ac-box">
				
				
				
			</div>
		</div>
  		</div>
		</div>
		<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      
        </div>
       	  </div>
       	  
       	  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
                        &nbsp; Select/Unselect All &nbsp;</div>
  </div>
		
		<div id="VIEW_RESULT_HERE" class="table-responsive">
		<div class="row"><div class="col-sm-12">
		<!-- <font face="Proxima Nova" > -->                
              <table id="tableInventorySummary" class="table table-bordred table-striped">                   
                   <thead>
                   <th style="font-size: smaller;">Chk</th>
                   <th style="font-size: smaller;">DATE</th>
                    <th style="font-size: smaller;">INVOICE</th>
                     <th style="font-size: smaller;">GINO</th>
                     <th style="font-size: smaller;">ORDER NO.</th>
                     <th style="font-size: smaller;">CUSTOMER NAME</th>
                     <th style="font-size: smaller;">STATUS</th>
                     <th style="font-size: smaller;">DUE DATE</th>
                     <th style="font-size: smaller;">AMOUNT</th>
                     <th style="font-size: smaller;">EXCHANGE RATE</th>
                     <th style="font-size: smaller;">AMOUNT (<%=curency%>)</th>
                     <th style="font-size: smaller;">BALANCE DUE (<%=curency%>)</th>
                     <th style="font-size: smaller;">BALANCE CREDIT (<%=curency%>)</th>
                   </thead>
              </table> 
              <!-- </font> -->    
		</div>
		  <div id="spinnerImg" ></div>
  
  <%            
  		POUtil poUtil = new POUtil();
        Map ma = poUtil.getPOReceiptHdrDetails(plant);
   		  
    %>  
    
     <div class="form-group">
  	<div class="col-sm-12" align="center">
  	<%if(displaySummaryPrintBatch){ %>      
  	<button type="button" class="Submit btn btn-default"  value="PrintWithBatch/Sno"  name="action" onclick="javascript:return onRePrint();" >Print</button>
  	<%} %>
  	</div>
  	</div>
		</FORM>
		</div>
		<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tableInventorySummary;
		 var FROM_DATE,TO_DATE,USER,ORDERNO,ITEM,INVOICENO,STATUS,PLNO,EMP_NAME, groupRowColSpan = 6;
		function getParameters(){
			return {
				"FDATE":FROM_DATE,"TDATE":TO_DATE,"EMP_NAME":EMP_NAME,
				"CNAME":USER,"ORDERNO":ORDERNO,"ITEM":ITEM,"INVOICENO":INVOICENO,"STATUS":STATUS,"PLNO":PLNO,					
				"ACTION": "VIEW_INVOICE_SUMMARY_VIEW",
				"PLANT":"<%=plant%>"
			}
		}  
		  function onGo(){
		   var flag    = "false";
		    FROM_DATE      = document.form1.FROM_DATE.value;
		    TO_DATE        = document.form1.TO_DATE.value;		    
		    USER           = document.form1.CUSTOMER.value;
		    ORDERNO        = document.form1.ORDERNO.value;
		    ITEM        = document.form1.item.value;
		    INVOICENO        = document.form1.invoice.value;
		    STATUS        = document.form1.status.value;
		    PLNO		= document.form1.plno.value;
		    EMP_NAME	= document.form1.EMP_NAME.value;
		    
		   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
		   if(TO_DATE != null    && TO_DATE != "") { flag = true;}		   

		   if(USER != null    && USER != "") { flag = true;}
		   
		    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
		    if(ITEM != null     && ITEM != "") { flag = true;}
		    if(INVOICENO != null     && INVOICENO != "") { flag = true;}
		    if(STATUS != null     && STATUS != "") { flag = true;}
		    if(PLNO != null     && PLNO != "") { flag = true;}
		    if(EMP_NAME != null     && EMP_NAME != "") { flag = true;}
		    
		    var urlStr = "../InvoiceServlet";
		   	// Call the method of JQuery Ajax provided
		   	var groupColumn = 1;
		   	var totalQty = 0;
		    // End code modified by Deen for product brand on 11/9/12
		    if (tableInventorySummary){
		    	tableInventorySummary.ajax.url( urlStr ).load();
		    }else{
			    tableInventorySummary = $('#tableInventorySummary').DataTable({
					"processing": true,
					"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
				        	if(typeof data.items[0].jobnum === 'undefined'){
				        		return [];
				        	}else {				        		
				        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        			data.items[dataIndex]['CHKPO'] = '<INPUT class="messageCheckbox" type="checkbox" style="border: 0;" name="chkdPoNo" value="'+data.items[dataIndex]['invoiceid']+'" >';
				        			<% if (displaySummaryLink) { %>
				        			data.items[dataIndex]['bill'] = '<a href="/track/jsp/invoiceDetail.jsp?dono=' +data.items[dataIndex]['jobnum']+ '&custno=' +data.items[dataIndex]['custno']+'&INVOICE_HDR=' +data.items[dataIndex]['invoiceid']+'">'+data.items[dataIndex]['bill']+'</a>';
				        			<% }else{ %>
				        			data.items[dataIndex]['bill'] = data.items[dataIndex]['bill'];
				        			<% } %>
				        			if(data.items[dataIndex]['status']=='Paid')
				        				data.items[dataIndex]['status'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
				        			else if(data.items[dataIndex]['status']=='Open')
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
					        		else if(data.items[dataIndex]['status']=='Draft')
						        		data.items[dataIndex]['status'] = '<span style="color:rgb(0,0,0);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';
					        		else
					        			data.items[dataIndex]['status'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.items[dataIndex]['status']+'</span>';	
				        		}
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [
			        	{"data": 'CHKPO', "orderable": false},
		    			{"data": 'billdate', "orderable": true},
		    			{"data": 'bill', "orderable": true},
		    			{"data": 'gino', "orderable": true},
		    			{"data": 'jobnum', "orderable": true},
		    			{"data": 'custname', "orderable": true},
		    			{"data": 'status', "orderable": true},
		    			{"data": 'duedate', "orderable": true},
		    			{"data": 'convamount', "orderable": true},
		    			{"data": 'exchangerate', "orderable": true},
		    			{"data": 'amount', "orderable": true},
		    			{"data": 'balancedue', "orderable": true},		    			
		    			{"data": 'creditbalance', "orderable": true},
		    			],
					"columnDefs": [{"className": "t-right", "targets": [7,8,9,10,11]}],
					"orderFixed": [ ], 
					/*"dom": 'lBfrtip',*/
					"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
					"<'row'<'col-md-6'><'col-md-6'>>" +
					"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
			        buttons: [
			        	{
			                extend: 'collection',
			                text: 'Export',
			                buttons: [
			                    {
			                    	extend : 'excel',
			                    	exportOptions: {
			    	                	columns: [':visible']
			    	                }
			                    },
			                    {
			                    	extend : 'pdf',
			                    	exportOptions: {
			                    		columns: [':visible']
			                    	},
		                    		orientation: 'landscape',
		                            pageSize: 'A3',
		                            	extend : 'pdfHtml5',
		    	                    	exportOptions: {
		    	                    		columns: [':visible']
		    	                    	},	                    	
		    	                        title: function () { var dataview = "<%=CNAME%> \n \n <%=title%> \n \n As On <%=collectionDate%>"  ; return dataview },    	                        
		                     		orientation: 'landscape',
		                     		customize: function (doc) {
		                     			doc.defaultStyle.fontSize = 16;
		                     	        doc.styles.tableHeader.fontSize = 16;
		                     	        doc.styles.title.fontSize = 20;
		                     	       doc.content[1].table.body[0].forEach(function (h) {
		                     	          h.fillColor = '#ECECEC';                 	         
		                     	          alignment: 'center'
		                     	      });
		                     	      var rowCount = doc.content[1].table.body.length;
		                     	     for (i = 1; i < rowCount; i++) {                     	     
		                     	     doc.content[1].table.body[i][6].alignment = 'right';
		                     	    doc.content[1].table.body[i][7].alignment = 'right';
		                     	     } 
		                     	      doc.styles.tableHeader.color = 'black';
		                     	      
		                     	        // Create a footer
		                     	        doc['footer']=(function(page, pages) {
		                     	            return {
		                     	                columns: [
		                     	                    '',
		                     	                    {
		                     	                        // This is the right column
		                     	                        alignment: 'right',
		                     	                        text: ['page ', { text: page.toString() },  ' of ', { text: pages.toString() }]
		                     	                    }
		                     	                ],
		                     	                margin: [10, 0]
		                     	            }
		                     	        });
		                     		},
		                             pageSize: 'A2',
		                             footer: true
			                    }
			                ]
			            },
			            {
		                    extend: 'colvis',
		                    columns: ':not(:eq('+groupColumn+')):not(:last)'
		                }
			        ],
			        "order": [],
			        drawCallback: function() {
			        	<%if(!displaySummaryExport){ %>
			        	$('.buttons-collection')[0].style.display = 'none';
			        	<% } %>
			        	} 
				});
		    }
		}

		$('#tableInventorySummary').on('column-visibility.dt', function(e, settings, column, state ){
			if (!state){
				groupRowColSpan = parseInt(groupRowColSpan) - 1;
			}else{
				groupRowColSpan = parseInt(groupRowColSpan) + 1;
			}
			$('#tableInventorySummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
			$('#tableInventorySummary').attr('width', '100%');
		});

		  function callback(data){
				
				var outPutdata = getTable();
				var ii = 0;
				var errorBoo = false;
				$.each(data.errors, function(i,error){
					if(error.ERROR_CODE=="99"){
						errorBoo = true;
						
					}
				});
				
				if(!errorBoo){
					
			        
				}else{
			}
		      outPutdata = outPutdata +'</TABLE>';
		      document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
		       document.getElementById('spinnerImg').innerHTML ='';

		   
		 }

		function getTable(){
		   return '<TABLE>'+
		          '<TR>'+
		          '<TH><font color="#ffffff" align="left"><b>Chk</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Date</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Invoice</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Reference Number</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Customer Name</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>Status</TH>'+
				  '<TH><font color="#ffffff" align="left"><b>Due Date</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Amount</TH>'+
		          '<TH><font color="#ffffff" align="left"><b>Balance Due</TH>'+
		          '</TR>';
		              
		}
		
		
		</script>
		</div>
		</div>
		
		 <script>
 $(document).ready(function(){
 onGo();
 $('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	    $('#search_criteria_status').val('show');
	});

 $('.Hide').click(function() {
	    $('#target').hide(500);
	    $('.ShowSingle').show(0);
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
 if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
 	$('.Show').click();
 }else{
 	$('.Hide').click();
 }
 });
 </script>
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>