<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%
String title = "Sales Counter";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.ESTIMATE_ORDER%>"/>
 <jsp:param name="submenu" value="<%=IConstants.ESTIMATE_REPORTS%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/SalesCounter.js"></script>


<script type="text/javascript">

var subWin = null;
function popUpWin(URL) {
  
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


function ExportReport(){
	var checkdiscount = document.getElementById("ViewbyAlternateBrandDiscount");	
	 var customer = document.getElementById("CUSTOMER").value;
	 if (checkdiscount.checked == true){
	 if(customer=='')
		 {
		 alert("Enter Customer");
		 document.form1.CUSTOMER.focus();
		 return;
		 }
	 }
	document.form1.action = "/track/ReportServlet?action=Export_AlerBrand_Reports";
	document.form1.submit();
	
} 
</script>
<style>
.salesEstQty{
	text-decoration: underline;
}
</style>
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%@include file="header.jsp" %>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();

String fieldDesc="";
String USERID ="",PLANT="",PRD_BRAND_ID = "",LOC ="",  ITEM = "", BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="",VINNO="",MODEL="", 
FROM_DATE ="",  TO_DATE = "",fdate="",tdate="",ALTERNATEITEM="",ALTERNATEDESC="",ALTERNATEBRAND="",VIEWALTERNATE="",CUSTOMER="",
CNAME = "",TAXTREATMENT="",NAME="",CUSTOMER_TYPE_ID="";
String html = "";
int Total=0;
String SumColor="",PRD_CLS_ID="",PRD_CLS_ID1="",LOC_TYPE_ID="",LOC_TYPE_ID2="",LOC_TYPE_ID3="";
boolean flag=false;
String uom = StrUtils.fString(request.getParameter("UOM"));
String PGaction = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
ITEM    = StrUtils.fString(request.getParameter("ITEM"));
PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
ALTERNATEITEM = StrUtils.fString(request.getParameter("ALTERNATEITEM"));
ALTERNATEDESC= StrUtils.fString(request.getParameter("ALTERNATEDESC"));
ALTERNATEBRAND= StrUtils.fString(request.getParameter("ALTERNATEBRAND"));
VINNO= StrUtils.fString(request.getParameter("VINNO"));
MODEL= StrUtils.fString(request.getParameter("MODEL"));
LOC = StrUtils.fString(request.getParameter("LOC"));
LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2= StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
LOC_TYPE_ID3= StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
CNAME = StrUtils.fString(request.getParameter("CNAME"));
TAXTREATMENT = StrUtils.fString(request.getParameter("TAXTREATMENT"));
NAME = StrUtils.fString(request.getParameter("NAME"));
CUSTOMER_TYPE_ID = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);
if(VIEWALTERNATE.equals(""))
{
	VIEWALTERNATE="WithInventory";
}
boolean cntRec=false;

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);

boolean displayConvToEstBtn=false, displayConvToInvcBtn=true;
displayConvToEstBtn = ub.isCheckVal("salescounterconvtoest", PLANT,USERID);
displayConvToInvcBtn = ub.isCheckVal("salescounterconvtoinvc", PLANT,USERID);
%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Sales Counter</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
	<div class="container-fluid">
		<FORM class="form-horizontal" name="form1" method="post" action="">
			<input type="hidden" name="xlAction" value="">
			<input type="hidden" name="PGaction" value="View">
			<input type="hidden" name="PRD_CLS_ID1" value="">
			<input type="hidden" name="PRD_CLS_DESC" value="">
			<input type="hidden" name="PRD_TYPE_DESC" value="">
			<input type="hidden" name="PRD_TYPE_ID1" value="">
			<input type="hidden" name="PRD_DESCRIP" value="">
			<input type="hidden" name="VINNO" value="">
			<input type="hidden" name="plant" value="<%=PLANT%>">
			<input type="hidden" name="numberOfDecimal" id="numberOfDecimal" value="<%=numberOfDecimal%>">
			<input type="hidden" name="CNAME" value="<%=CNAME%>">
			<input type="hidden" name="TAXTREATMENT" value="<%=TAXTREATMENT%>">
			<input type="hidden" name="NAME" value="<%=NAME%>">
			<input type="hidden" name="CUSTOMER_TYPE_ID" value="<%=CUSTOMER_TYPE_ID%>">
  			<input type="hidden" name="CUSTNO" id="CUSTNO">
  
  			<center><small> <%=fieldDesc%></small></center>
  
  			<div id="target" style="padding: 18px;">
       			<div class="form-group">
       				<div class="row">
       					<div class="col-sm-2.5">
								<label class="control-label col-sm-2" for="search">Search</label>
						</div>   
						<div class="col-sm-4 ac-box">
							<div class="input-group"> 
								<input type="text" name="ITEM" id="item" class="ac-selected form-control" 
								placeholder="PRODUCT" value="<%=ITEM%>">
  								<span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()">
  									<i class="glyphicon glyphicon-menu-down"></i>
								</span>						
							</div>
						</div>
						<div class="col-sm-4 ac-box">
							<div class="input-group"> 
								<input type="text" name="LOC" id="LOC" class="ac-selected form-control" 
								placeholder="Location ID" value="<%=LOC%>">
  								<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC\']').focus()">
  									<i class="glyphicon glyphicon-menu-down"></i>
								</span>						
							</div>
						</div>    				
       				</div>
       				<div class="row" style="padding:3px">
       					<div class="col-sm-2">
				  		</div>
				  		<div class="col-sm-4 ac-box">
							<div class="input-group"> 
								<input type="text" name="LOC_TYPE_ID" id="LOC_TYPE_ID" class="ac-selected form-control" 
								placeholder="Location Type1/Desc" value="<%=LOC_TYPE_ID%>">
  								<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()">
  									<i class="glyphicon glyphicon-menu-down"></i>
								</span>						
							</div>
						</div>
						<div class="col-sm-4 ac-box">
							<div class="input-group"> 
								<input type="text" name="LOC_TYPE_ID2" id="LOC_TYPE_ID2" class="ac-selected form-control" 
								placeholder="Location Type2/Desc" value="<%=LOC_TYPE_ID2%>">
  								<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()">
  									<i class="glyphicon glyphicon-menu-down"></i>
								</span>						
							</div>
						</div>
       				</div>
       				<div class="row" style="padding:3px">
       					<div class="col-sm-2">
				  		</div>
				  		<div class="col-sm-4 ac-box">
						<div class="input-group"> 
							<input type="text" name="LOC_TYPE_ID3" id="LOC_TYPE_ID3" class="ac-selected form-control" 
							placeholder="Location Type3/Desc" value="<%=LOC_TYPE_ID3%>">
  							<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID3\']').focus()">
  								<i class="glyphicon glyphicon-menu-down"></i>
							</span>						
						</div>
						</div>
		  		<div class="col-sm-4 ac-box">
							<div class="input-group"> 
								<input type="text" name="CUSTOMER" id="CUSTOMER" class="ac-selected form-control" 
								placeholder="Customer"  value="<%=CUSTOMER%>">
  								<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()">
  									<i class="glyphicon glyphicon-menu-down"></i>
								</span>						
							</div>
						</div>
       				</div>
       				<div class="row" style="padding:3px">
       					<div class="col-sm-2">
				  		</div>
				  		<div class="col-sm-4 ac-box">
							<div class="input-group"> 
							<input type="text" name="MODEL" id="MODEL" class="ac-selected form-control" 
							placeholder="MODEL" value="<%=MODEL%>">
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'MODEL\']').focus()">
  								<i class="glyphicon glyphicon-menu-down"></i>
							</span>					
						</div>
						</div>
				  		<div class="col-sm-4 ac-box">
							<div class="input-group"> 
								<input type="text" name="UOM" id="UOM" class="ac-selected form-control" 
								placeholder="UOM" value="<%=uom%>">
  								<span class="select-icon" onclick="$(this).parent().find('input[name=\'UOM\']').focus()">
  									<i class="glyphicon glyphicon-menu-down"></i>
								</span>						
							</div>
						</div>
       				</div>
       				<div class="row" style="padding:3px">
				  		<div class="col-sm-2">
				  		</div>
						<div class="col-sm-4 txn-buttons">								
							<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>&nbsp;
							<!-- <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();">Export All Data</button> -->						
						</div>
					</div>
       			</div>
      		</div>
      		
<!--     	<div class="form-group">
			<div class="col-sm-3">
				<a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
				<a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
		    </div>
			<div class="ShowSingle">			      
			</div>
		</div>  -->   
  	</div>
  	
  	
<!--   	<div class="form-group">
      <div class="ShowSingle">
      	<div class="col-sm-offset-8">
  	        <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
		</div>
       </div>
	</div> -->
	<div class="form-group">
		<input type="checkbox" class="form-check-input" id="ViewbyAlternateBrandDiscount"  Onclick="checkval()" />&nbsp;<strong>Unit Price (by Customer Discount)</strong>&nbsp;
	</div>
	
       	

     <!--   Start code added by Deen for product brand on 11/9/12 -->
     
     <!--   End code added by Deen for product brand on 11/9/12 -->  
    
  	<div id="VIEW_RESULT_HERE" class="table-responsive">
              <div class="row">
              	<div class="col-sm-12">              
              		<table id="tableInventorySummary" class="table table-bordred table-striped">									 
						<thead>	          
			                	<tr><th style="font-size: smaller;">Chk</th>     
			                	<th hidden>GROUP</th>
			                	<th style="font-size: smaller;">Product ID</th>
			                	<th style="font-size: smaller;">Description</th>
			                	<th style="font-size: smaller;">Catalog</th>
			                	<th style="font-size: smaller;">Brand</th>
			                	<th style="font-size: smaller;">Model</th>
			                	<th style="font-size: smaller;">Location</th>
			                	<th style="font-size: smaller;">Unit Price</th>
			                	<th style="font-size: smaller;">Last Transaction Price</th>
			                	<th style="font-size: smaller;">PC/PCS/EA UOM</th>
			                	<th style="font-size: smaller;">PC/PCS/EA Quantity</th>
			                	<th style="font-size: smaller;">Inventory UOM</th>
			                	<th style="font-size: smaller;">Inventory Quantity</th>
			                	<th style="font-size: smaller;">Available Quantity</th>
			                	<th style="font-size: smaller;">Estimate Quantity</th>
			                	<th style="font-size: smaller;">Quantity</th></tr>
			            </thead>
					</table>
           		</div>
           		
			</div>

  </div>
  <br>
  <div class="row">
	  <div class="col-sm-12">
		<button type="button" class="btn btn-default" onclick="onAddList()">Add Product</button> 
	  </div>
  </div>
  <br>
  <div class="form-group">
	<INPUT Type=Checkbox class="form-check-input" style="border:0;" onclick="checkAllEstList(this.checked);">
	<strong>   &nbsp; Select/Unselect All </strong> 
  </div>
  <div class="table-responsive">
  	<table id="estListSummary" class="table table-bordred table-striped" hidden>									 
		<thead>	          
               	<tr><th style="font-size: smaller;">Chk</th>     
               	<th style="font-size: smaller;">Product ID</th>
               	<th style="font-size: smaller;">Description</th>
               	<th style="font-size: smaller;">Catalog</th>
               	<th style="font-size: smaller;">Brand</th>
               	<th style="font-size: smaller;">Model</th>
               	<th style="font-size: smaller;">Location</th>
               	<th style="font-size: smaller;">Unit Price</th>
               	<th style="font-size: smaller;">Inventory UOM</th>
               	<th style="font-size: smaller;">Inventory Quantity</th>
               	<th style="font-size: smaller;">Available Quantity</th>
               	<th style="font-size: smaller;">Order Uom</th>
               	<th style="font-size: smaller;">Order Qty</th></tr>
           </thead>
           <tbody>
           </tbody>
	</table>
  </div>
  <br>
  <div class="row">
	  <div class="col-sm-12">
	  	<%if(displayConvToEstBtn){ %>
	  		<button type="button" class="btn btn-default" onclick="convertToEstimate()">Convert To Estimate</button>
	  	<%} %>
		
		<%if(displayConvToInvcBtn){ %>
			<button type="button" class="btn btn-default" onclick="convertToInvoice()">Convert To Invoice</button>
		<%} %>
		<!-- <button type="button" class="btn btn-default" onclick="">Convert To Invoice</button>  -->
	  </div>
  </div>
  <div id="spinnerImg" ></div>
 
  </FORM>
  <form id="convertData" name="convertDataForm" method="post" action="" hidden>
  </form>
  <div id="salesEstListModal" class="modal fade" role="dialog">
  	<div class="modal-dialog modal-lg">
  		<div class="modal-content">
  			<div class="modal-header">
          		<button type="button" class="close" data-dismiss="modal">&times;</button>
          		<h4 class="modal-title">Estimate Details</h4>
        	</div>
        	<div class="modal-body">
	          <table id="salesEstDetails" class="table" style="width:100%">
	          	<thead>
					<tr>
						<th>Order No</th>
						<th>Order Date</th>
						<th>Expiry Date</th>
						<th>Customer</th>
						<th>Product ID</th>
						<th>Order Qty</th>
					</tr>
				</thead>
				<tbody>
				 
				</tbody>
				<tfoot>
		            <tr>
		                <th colspan="5" style="text-align:right">Total:</th>
		                <th></th>
		            </tr>
		        </tfoot>
	          </table>
	        </div>
	        <div class="modal-footer">
	          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        </div>
  		</div>
  	</div>
  </div>
  
  <div id="lastTranPriceModal" class="modal fade" role="dialog">
  	<div class="modal-dialog modal-lg">
  		<div class="modal-content">
  			<div class="modal-header">
          		<button type="button" class="close" data-dismiss="modal">&times;</button>
          		<h4 class="modal-title">Estimate Details</h4>
        	</div>
        	<div class="modal-body">
	          <table class="table lastEstPriceDetails">
	          	<thead>
					<tr>
						<th>Order No</th>
						<th>Order Date</th>
						<th>Customer</th>
						<th>Product ID</th>
						<th>Price</th>
					</tr>
				</thead>
				<tbody>
				 
				</tbody>
	          </table>
	        </div>
  		</div>
  		
  		<div class="modal-content">
  			<div class="modal-header">
          		<h4 class="modal-title">Invoice Details</h4>
        	</div>
        	<div class="modal-body">
	          <table class="table lastInvoicePriceDetails">
	          	<thead>
					<tr>
						<th>Invoice No</th>
						<th>Order Date</th>
						<th>Customer</th>
						<th>Product ID</th>
						<th>Price</th>
					</tr>
				</thead>
				<tbody>
				 
				</tbody>
	          </table>
	        </div>
	        <div class="modal-footer">
	          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	        </div>
  		</div>
  	</div>
  </div>
  <script type="text/javascript">
  var tableInventorySummary, salesEstDetails;
  var productId,prdBrand,alernateProductId,alternateBrand,loc,locType,locType2,locType3,vinno,model,desc,viewalternate,canme, groupRowColSpan = 7,uom;
function getParameters(){
	return { 
		"ITEM": productId,
		"LOC":loc,
		//"PRD_BRAND_ID":prdBrand,
		"PRD_DESCRIP": desc,
		//"ALTERNATEITEM":alernateProductId,
		//"ALTERNATEBRAND":alternateBrand,
		"LOC_TYPE_ID":locType, 
		"LOC_TYPE_ID2":locType2, 
		"LOC_TYPE_ID3":locType3, 
		"VINNO":vinno, 
		"MODEL":model,
		"VIEWALTERNATE":viewalternate,
		"CUSTOMER":canme,
		"UOM":uom,
		"ACTION": "VIEW_INV_SUMMARY_ALTERNATE_BRAND",
		"PLANT":"<%=PLANT%>"
	}
}  
function onGo(){
	var checkdiscount = document.getElementById("ViewbyAlternateBrandDiscount");	
	 var customer = document.getElementById("CUSTOMER").value;
	 productId = document.form1.ITEM.value;
	 
	 if(productId=='')
	 {
	 alert("Enter PRODUCT ID");
	 document.form1.ITEM.focus();
	 return;
	 }
	 
	 if (checkdiscount.checked == true){
	 if(customer=='')
		 {
		 alert("Enter Customer");
		 document.form1.CUSTOMER.focus();
		 return;
		 }
	 }
    
    //prdBrand = document.form1.PRD_BRAND_ID.value;
    desc = document.form1.PRD_DESCRIP.value;
    //alernateProductId = document.form1.ALTERNATEITEM.value;
    //alternateBrand = document.form1.ALTERNATEBRAND.value;
    loc = document.form1.LOC.value;
    locType = document.form1.LOC_TYPE_ID.value;
    locType2 = document.form1.LOC_TYPE_ID2.value;
    locType3 = document.form1.LOC_TYPE_ID3.value;
    vinno = document.form1.VINNO.value;
    model = document.form1.MODEL.value;
    uom = document.form1.UOM.value;
    /* viewalternate = document.form1.VIEWALTERNATE.value;
	 if (viewalternate=="WithInventory"){		 
		 viewalternate="1";		 
	 }
	 else
		 {		 
		 viewalternate="0";		 
		 } */
    canme           = document.form1.CUSTOMER.value;
    var urlStr = "../InvMstServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 1;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableInventorySummary){
    	tableInventorySummary.ajax.url( urlStr ).load();
    }else{
	    tableInventorySummary = $('#tableInventorySummary').DataTable({
	    	 "ordering": false,
			"processing": true,
			"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
			"pageLength": 50,
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].ITEM === 'undefined'){
		        		return [];
		        	}else {
		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			data.items[dataIndex]['GROUP'] = (data.items[dataIndex]['GROUP'] == "A") ? "SEARCHED PRODUCT" : ((data.items[dataIndex]['GROUP'] == "B") ? "EQUIVALENT PRODUCT" : "OTHER PRODUCT");
		        			data.items[dataIndex]['CHKPO'] = '<INPUT Type=checkbox style=border: 0; name=chkdPoNo value="'+data.items[dataIndex]['ITEM']+'" ><INPUT type="hidden" name="SALESUOM" value="'+data.items[dataIndex]['SALESUOM']+'" ><INPUT type="hidden" name="NONSTKFLAG" value="'+data.items[dataIndex]['NONSTKFLAG']+'" >';
		        			data.items[dataIndex]['ESTQTY'] = '<a href="#" class="salesEstQty" onclick="loadEstDetails(\''+data.items[dataIndex]['ITEM']+'\')">' + data.items[dataIndex]['ESTQTY'] + '</a>';
		        			data.items[dataIndex]['LSP'] = '<a href="#" class="fa fa-info-circle" onclick="loadLTPDetails(\''+data.items[dataIndex]['ITEM']+'\')"></a>';
		        			/* data.items[dataIndex]['CHKPO'] = '<INPUT Type=checkbox style=border: 0; name=chkdPoNo value="'+data.items[dataIndex]['PONO']+'" >';
		        			data.items[dataIndex]['PONO'] = '<a href="/track/jsp/PrintPODetails.jsp?PONO=' +data.items[dataIndex]['PONO']+ '&INVOICE='+data.items[dataIndex]['INVOICE']+'&FROMDATE='+data.items[dataIndex]['FROMDATE']+'&TODATE='+data.items[dataIndex]['TODATE']+'">' + data.items[dataIndex]['PONO'] + '</a>'; */
		        		}
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
	        	{"data": 'CHKPO', "orderable": true},
	        	{"data": 'GROUP', "orderable": true},
	        	{"data": 'ITEM', "orderable": true},
    			{"data": 'ITEMDESC', "orderable": true},
    			{"data": 'CATALOG', "orderable": true},
    			{"data": 'BRAND', "orderable": true},
    			{"data": 'MODEL', "orderable": true},
    			{"data": 'LOC', "orderable": true},
    			{"data": 'UNITPRICE', "orderable": true},
    			{"data": 'LSP', "orderable": true},
    			{"data": 'STKUOM', "orderable": true},
     			{"data": 'STKQTY', "orderable": true},
    			{"data": 'INVENTORYUOM', "orderable": true},
    			{"data": 'INVUOMQTY', "orderable": true},
    			{"data": 'AVAILABLE_QUANTITY', "orderable": true},
    			{"data": 'ESTQTY', "orderable": true},
    			{"data": 'QTY', "orderable": true}
    			],
			"columnDefs": [{"visible": false, "targets": groupColumn}],
			"orderFixed": [ groupColumn, 'asc' ], 
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
                            pageSize: 'A4'
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
                }
	        ],
	        rowCallback: function(row, data, index){
	        	if(data.GROUP === "PARENT PRODUCT ID"){
	        		$(row).addClass("text-primary");
	        	}else if(data.GROUP === "CHILD PRODUCT ID"){
	        		$(row).addClass("text-success");
	        	}
	        	$(row).find("td:nth-child(4)").addClass("item-img");
	          	/*if(data[0]> 11.7){
	            	$(row).find('td:eq(3)').css('color', 'red');
	            }
	            if(data[2].toUpperCase() == 'EE'){
	            	$(row).find('td:eq(2)').css('color', 'blue');
	            }*/
	          },
			"drawCallback": function ( settings ) {
				var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	 
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                    $(rows).eq( i ).before(
	                        '<tr class="group"><td colspan="16">'+group+'</td></tr>'
	                    );
	 
	                    last = group;
	                }
	            } );
				/*this.attr('width', '100%');
	            var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalQty = 0;
	            var groupTotalQty = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;
	                    groupTotalQty = 0;
	                }
	                groupTotalQty += parseFloat($(rows).eq( i ).find('td:last').html());
	                totalQty += parseFloat($(rows).eq( i ).find('td:last').html());
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){*/
	            	/*$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td class="t-right">' + parseFloat(totalQty).toFixed(3) + '</td></tr>'
                    );*/
                	/*$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class="t-right">' + parseFloat(groupTotalQty).toFixed(3) + '</td></tr>'
                    );
                }*/
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

$('.buttons-columnVisibility').each(function(){
  var $li = $(this),
      $cb = $('<input>', {
              type:'checkbox',
              style:'margin:0 .25em 0 0; vertical-align:middle'}
            ).prop('checked', $(this).hasClass('active') );
  $li.find('a').prepend( $cb );
});
	 
$('.buttons-columnVisibility').on('click', 'input:checkbox,li', function(){
  var $li = $(this).closest('li'),
      $cb = $li.find('input:checkbox');
  $cb.prop('checked', $li.hasClass('active') );
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
			
	        $.each(data.items, function(i,item){
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
                       
	        	outPutdata = outPutdata+item.PRODUCT
                        	ii++;
	            
	          });
		}else{
		//	outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="6"><BR><CENTER><B><FONT COLOR="RED">No details found!</FONT></B></CENTER></TD></TR>';
		}
        outPutdata = outPutdata +'</TABLE>';
        document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
         document.getElementById('spinnerImg').innerHTML ='';

     
   }

function getTable(){
            return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
                    '<TR BGCOLOR="#000066">'+
                    '<TH><font color="#ffffff" align="left" width = "15%"><b>Product ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Catalog</TH>'+
                    /* '<TH><font color="#ffffff" align="left"><b>Brand ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Alternate Product</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Description<</TH>'+ */
                    '<TH><font color="#ffffff" align="left"><b>Brand</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>VIN No.</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Model</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Location</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Unit Price</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Quantity</TH>'+                    
                    '</TR>';
                
}

function loadEstDetails(item){
	/**/
	var urlStr = "/track/EstimateServlet?ITEM="+item;
	if (salesEstDetails){
		salesEstDetails.ajax.url( urlStr ).load();
    }else{
    	salesEstDetails = $('#salesEstDetails').DataTable({
    		"processing": true,
    		"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
    		"pageLength": 50,
    		"ajax": {
    			"type": "POST",
    			"url": urlStr,
    			"data": {
    				PLANT : "<%=PLANT%>",
    				Submit : "GetEstimateQty"
    			},
    			"contentType": "application/x-www-form-urlencoded; charset=utf-8",
    			"dataType": "json",
    			"dataSrc": function(data){
    				if(typeof data.items[0].ITEM === 'undefined'){
    					return [];
    				}else {
		        		return data.items;
		        	}
   				}
   			},
   			"columns": [
	        	{"data": 'ESTNO', "orderable": true},
	        	{"data": 'COLLECTIONDATE', "orderable": true},
	        	{"data": 'EXPIREDATE', "orderable": true},
    			{"data": 'CUSTNAME', "orderable": true},
    			{"data": 'ITEM', "orderable": true},
    			{"data": 'QTYOR', "orderable": true}
   			],/* 
   			"columnDefs": [{"visible": false, "targets": groupColumn}],
   			"orderFixed": [ groupColumn, 'asc' ],
   			"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
			"<'row'<'col-md-6'><'col-md-6'>>" +
			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>"  */
			"footerCallback": function ( row, data, start, end, display ) {
	            var api = this.api(), data;
	            var intVal = function ( i ) {
	                return typeof i === 'string' ?
	                    i.replace(/[\$,]/g, '')*1 :
	                    typeof i === 'number' ?
	                        i : 0;
	            };
	         // Total over all pages
	            totalord = api
	                .column(5,{ page: 'current'})
	                .data()
	                .reduce( function (a, b) {
	                    return intVal(a) + intVal(b);
	                }, 0 );		         
	            		         
	            $( api.column( 4 ).footer() ).html('Total');
	            $( api.column( 5 ).footer() ).html(parseFloat(totalord).toFixed(3));		            		            
	        }
    	});
    }
	/**/
	
	
		$("#salesEstListModal").modal();
}

function loadLTPDetails(item){
	var customer = document.getElementById("CUSTOMER").value;
	if(customer==''){
		alert("Enter Customer");
		document.form1.CUSTOMER.focus();
		return;
 	}
	var urlStr = "/track/EstimateServlet";
	$.ajax({
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : "<%=PLANT%>",
			Submit : "GetLastTransactionPrice",
			ITEM : item,
			CUSTCODE : customer
		},
		dataType : "json",
		success : function(dataList) {
			var body1="", body2="";
			if(dataList.estdetails[0].ITEM != undefined){
				dataList.estdetails.forEach(function(data){
					body1 += "<tr>";
					body1 += "<td>"+ data.ESTNO+"</td>";
					body1 += "<td>"+ data.COLLECTIONDATE+"</td>";
					body1 += "<td>"+ data.CUSTNAME+"</td>";
					body1 += "<td>"+ data.ITEM+"</td>";
					body1 += "<td>"+ data.UNITPRICE+"</td>";
					body1 += "</tr>";
				});
			}else{
				body1 += "<tr>";
				body1 += "<td colspan='5' style='text-align: center;'> No details found</td>";
				body1 += "</tr>";
			}
			$(".lastEstPriceDetails tbody").html(body1);
			
			if(dataList.dodetails[0].ITEM != undefined){
				dataList.dodetails.forEach(function(data){
					body2 += "<tr>";
					body2 += "<td>"+ data.DONO+"</td>";
					body2 += "<td>"+ data.COLLECTIONDATE+"</td>";
					body2 += "<td>"+ data.CUSTNAME+"</td>";
					body2 += "<td>"+ data.ITEM+"</td>";
					body2 += "<td>"+ data.UNITPRICE+"</td>";
					body2 += "</tr>";
				});
			}else{
				body2 += "<tr>";
				body2 += "<td colspan='5' style='text-align: center;'> No details found</td>";
				body2 += "</tr>";
			}
			$(".lastInvoicePriceDetails tbody").html(body2);

			$("#lastTranPriceModal").modal();
		}
	});
}
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
</SCRIPT>
</div></div></div>
 <script>
$(document).ready(function(){
var PGaction='<%=PGaction%>';
if(PGaction == 'view'){
	onGo();
}
loadEstList();
});
 function checkval()
 {
	 var checkBox = document.getElementById("ViewbyAlternateBrand");
	 if (checkBox.checked == true){		 
		 document.form1.VIEWALTERNATE.value="1";		 
	 }
	 else
		 {		 
		 document.form1.VIEWALTERNATE.value="0";		 
		 }	 
 }
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>