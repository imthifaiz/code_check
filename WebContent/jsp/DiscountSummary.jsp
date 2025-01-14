
<!-- PAGE CREATED BY : IMTHI -->
<!-- DATE 14-06-2022 -->
<!-- DESC : Discount Summary -->

<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<!-- IMTIZIAF -->
<%@ page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%
String title = "Discount";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
		<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.POS_REPORT%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script>
	var subWin = null;
	function popUpWin(URL) {
		subWin = window
				.open(
						URL,
						'GroupSummary',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}

	function ExportReport(){
		document.form.action="/track/deleveryorderservlet?Submit=ExportExcelDiscountSummary";
		document.form.submit();
	} 

</script>

<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	List locQryList = new ArrayList();

	String fieldDesc = "";
	String PLANT = "";
	String html = "";
	int Total = 0;
	String SumColor = "";
	
	String FROM_DATE="",TO_DATE="",searchtype="",CUST_NAME  = "",SALES_MAN="",OUTLET_CODE = "",TERMINAL_CODE ="",sOutCode="";
	
	
	boolean flag = false;
	session = request.getSession();
	PLANT = session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String msg =  StrUtils.fString(request.getParameter("Msg"));
	String types =  StrUtils.fString(request.getParameter("srctype"));
	fieldDesc =  StrUtils.fString(request.getParameter("result"));
	String PGaction =  StrUtils.fString(request.getParameter("PGaction"));
	
	String ENABLE_POS =new PlantMstDAO().getispos(PLANT);
	String ISTAXREGISTRED =new PlantMstDAO().getistaxregistred(PLANT);
	String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();
	FROM_DATE    = strUtils.fString(request.getParameter("FROM_DATE"));
	String curDate =du.getDateMinusDays();
	if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
		curDate =DateUtils.getDate();
	FROM_DATE=du.getDateinddmmyyyy(curDate);
	searchtype  = strUtils.fString(request.getParameter("srctype"));
	CUST_NAME  = strUtils.fString(request.getParameter("CUST_NAME"));
	SALES_MAN  = strUtils.fString(request.getParameter("SALES_MAN"));
	sOutCode = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTCODE")));
	
	//IMTIZIAF
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
	String collectionDate=DateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	//END
	
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../posreports/reports"><span class="underline-on-hover">POS Reports</span> </a></li>
                <li><label>Discount</label></li>                                   
            </ul>
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
                            
                            <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button>
					&nbsp;
				</div>
				<div class="btn-group" role="group">
            	<button type="button" class="btn btn-default printMe" onclick="PrintTable();"
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
            	</div>
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../posreports/reports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
		
 <div class="box-body">
 
	
<FORM class="form-horizontal" name="form" method="post" action="DiscountSummary.jsp">
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<input type="hidden" name="plant" value="<%=PLANT%>">
<input type="hidden" name="CUST_CODE" >
<input type="hidden" name="OUTLET_CODE" value="">
<input type="hidden" name="TERMINAL_CODE" value="">
<INPUT type="hidden" name="OUTCODE" value="<%=sOutCode%>">
<%-- <input type="hidden" name="srctype" value="<%=searchtype%>"> --%>
<input type="hidden" name="srctype" value="Discount">

	<div id="target" style="padding: 18px; display:none;">
   	<div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Search</label>
  		<div class="col-sm-2">
			<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
			<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" id="FROM_DATE" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
		</div>
		<div class="col-sm-2">
			<input class="form-control datepicker" name="TO_DATE" type = "text" id="TO_DATE" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
		</div>
  		
       <div class="col-sm-4 ac-box">
      	   	<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO"  placeholder="BILL NO">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeorderno(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->	
  		</div>
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
  		<div class="col-sm-4 ac-box">
    		<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecustomer(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			<!-- <span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
       <div class="col-sm-4 ac-box">
    		<INPUT name="OUTLET_NAME" id="OUTLET_NAME" type="TEXT" value="<%=OUTLET_CODE%>" placeholder="OUTLET" size="20" MAXLENGTH=100 class="ac-selected form-control"> 
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeoutlet(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			<!-- <span class="select-icon" onclick="$(this).parent().find('../jsp/input[name=\'OUTLET_NAME\']').focus()">
			<i class="glyphicon glyphicon-menu-down"></i> </span> -->
  		</div>
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
  		<div class="col-sm-4 ac-box">
    		<INPUT name="TERMINALNAME" id="TERMINALNAME" type="TEXT" value="<%=TERMINAL_CODE%>" placeholder="TERMINAL" size="20" MAXLENGTH=100 class="ac-selected form-control"> 
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeterminal(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			<!-- <span class="select-icon" onclick="$(this).parent().find('../jsp/input[name=\'TERMINALNAME\']').focus()">
			<i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
       <div class="col-sm-4 ac-box">
    		<INPUT id="CUST_NAME" name="CUST_NAME" type = "TEXT" value="<%=CUST_NAME%>" size="20"  placeholder="CASHIER" MAXLENGTH=100 class="ac-selected form-control">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changecust_name(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			<!-- <span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUST_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
  		<div class="col-sm-4 ac-box">
    		<INPUT id="SALES_MAN" name="SALES_MAN" type = "TEXT" value="<%=SALES_MAN%>" size="20"  placeholder="SALES PERSON" MAXLENGTH=100 class="ac-selected form-control">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changesales_man(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			<!-- <span class="select-icon"  onclick="$(this).parent().find('input[name=\'SALES_MAN\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
       <div class="col-sm-4 ac-box">
    		<input type="text" name="item" id="item" class="ac-selected form-control" placeholder="PRODUCT" >
    		<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
  			<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'item\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->  		
  		</div>
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
  		<div class="col-sm-4 ac-box">
    		<input type="text" class="ac-selected  form-control typeahead ProductBrand" id="PRD_BRAND_ID" placeholder="PRODUCT BRAND" name="PRD_BRAND_ID">
				<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeproductbrand(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				<!-- <span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
       <div class="col-sm-4 ac-box">
    		<input type="text" class="ac-selected  form-control typeahead ProductClass" id="PRD_CLS_ID" placeholder="PRODUCT CATEGORY" name="PRD_CLS_ID">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeCategory(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
  		<div class="col-sm-4 ac-box">
    	  	<input type="text" class="ac-selected  form-control typeahead ProductType" id="PRD_TYPE_ID" placeholder="PRODUCT SUB CATEGORY" name="PRD_TYPE_ID">
			<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeProductType(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
			<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> -->
  		</div>
  		
 		<div class="col-sm-4 ac-box">
  			<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
  		</div>
 	</div>

  	</div>
  	
  	      <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
  	  </div>
         </div>
       	   </div>  	
  	
</form>
<br>
 

<!-- PDF PRINT -->
<div id="dvContents" style="display:none">
	<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	align="center" class="table printtable" id="tableprint">
	<thead>  
          <tr>  
            <th style="font-size: smaller;">S/N</th>  
            <th style="font-size: smaller;">DATE</th>
            <th style="font-size: smaller;">BILL</th>
			<th style="font-size: smaller;">CUSTOMER</th>
            <th style="font-size: smaller;">AMOUNT</th>
            <th style="font-size: smaller;">DISCOUNT</th>    
            <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
            <%} else {%>
            <th style="font-size: smaller;">TAX</th>    
            <%} %>
            <th style="font-size: smaller;">TOTAL AMOUNT</th>
          </tr>  
        </thead> 
          <!-- IMTIZIAF -->
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
								<%} else {%>
								<th></th>
								<%} %>
								<th></th>
							</tr>
						</tfoot>
						<!-- END -->
</table>
</div> 
 
<div style="overflow-x:auto;">
<table id="tablePosSalesSummary" class="table table-bordred table-striped"  style="width: 100%;">  
   
   <thead style="text-align: center">  
          <tr>  
            <th style="font-size: smaller;">S/N</th>  
            <th style="font-size: smaller;">DATE</th>
            <th style="font-size: smaller;">BILL</th>
			<th style="font-size: smaller;">CUSTOMER</th>
            <th style="font-size: smaller;">AMOUNT</th>
            <th style="font-size: smaller;">DISCOUNT</th>    
            <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
            <%} else {%>
            <th style="font-size: smaller;">TAX</th>    
            <%} %>
            <th style="font-size: smaller;">TOTAL AMOUNT</th>
          </tr>  
        </thead> 
          <!-- IMTIZIAF -->
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
								<%} else {%>
								<th></th>
								<%} %>
								<th></th>
							</tr>
						</tfoot>
						<!-- END -->
</table>
</div>  
<script>
		
		var tablePosSalesSummary;
		var plant,fdate,tdate,type,orderno,customer,outlet_name,outlet_code,terminalname,cashier,sales_man,item,prd_brand_id,prd_cls_id,prd_type_id,groupRowColSpan = 6;
		var tablePrint;
		function getParameters(){
			return {
				"FDATE":fdate,
				"TDATE":tdate,
				"TYPE":type,
				"ORDERNO":orderno,
				"CUSTOMERCODE":customer,
				"OUTLET":outlet_code,
				"TERMINAL":terminalname,
				"CASHIER":cashier,
				"SALESMAN":sales_man,
				"ITEM":item,
				"PRD_BRAND_ID":prd_brand_id,
				"PRD_TYPE_ID":prd_type_id,
				"PRD_CLS_ID":prd_cls_id,
				"ACTION": "VIEW_POS_SALES_ORDER_SUMMARY",
				"PLANT": plant
			}
		}  

		function onGo(){
			var flag    = "false";
			
			plant = document.form.plant.value;
			fdate = document.form.FROM_DATE.value;
			tdate = document.form.TO_DATE.value;
			type = 'Order';
			orderno = document.form.ORDERNO.value;
			customer = document.form.CUST_CODE.value;
			outlet_name = document.form.OUTLET_NAME.value;
			terminalname = document.form.TERMINALNAME.value;
			outlet_code = document.form.OUTCODE.value;
			cashier = document.form.CUST_NAME.value;
			sales_man = document.form.SALES_MAN.value;
			item = document.form.item.value;
			prd_brand_id = document.form.PRD_BRAND_ID.value;
			prd_cls_id = document.form.PRD_CLS_ID.value;
			prd_type_id = document.form.PRD_TYPE_ID.value;

			var urlStr = "../posreports/PosSalesDiscountSummary";
			//tablePosSalesSummary.ajax.reload(null, false); 
			// Call the method of JQuery Ajax provided
		   	var groupColumn = 1;
		   	var totalQty = 0;
		   
		    // End code modified by Deen for product brand on 11/9/12
		    if (tablePosSalesSummary){
		    	tablePosSalesSummary.ajax.url( urlStr ).load();
		    	tableprint.ajax.url( urlStr ).load();
		    }else{
			    tablePosSalesSummary = $('#tablePosSalesSummary').DataTable({
					"processing": true,
					"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
					
				        	/* if(typeof data.items[0].INDEX === 'undefined'){ */
				        	if(data.items.length === 0){
				        		return [];
				        	}else {
				        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
									data.items[dataIndex]['DONO'] = '<a href="../posreports/salesreportsdetail?TYPE=Discount&DONO='+data.items[dataIndex]['DONO']+'">'+data.items[dataIndex]['DONO']+'</a>';
				        		}
				        		
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [

		    			{"data": 'INDEX', "orderable": true},
		    			{"data": 'SALESDATE', "orderable": true},
		    			{"data": 'DONO', "orderable": true},
		    			{"data": 'CNAME', "orderable": true},
		    			{"data": 'AMOUNT', "orderable": true},
		    			{"data": 'DISCOUNT', "orderable": true},
		    			<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		    			<%} else {%>
		    			{"data": 'TAX', "orderable": true},
		    			<%} %>
		    			{"data": 'TOTALAMOUNT', "orderable": true}
		    			
		    			],
		    		"columnDefs": [
		    			<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
			    		{"className": "t-right", "targets": [4,5,6]}
			    		<%}else {%>
			    		{"className": "t-right", "targets": [4,5,6,7]}
			    		<%} %>
			    		],
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
			                    	title: '<%=title %>',
			                    	exportOptions: {
			    	                	columns: [':visible']
			    	                }
			                    },
			                    {
			                    	extend : 'pdf',
			                    	title: '<%=title %>',
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
			   	       "order": [],
			   	    "drawCallback": function ( settings ) {
						var groupColumn = 0;
						var groupRowColSpan= 3;
					   	var api = this.api();
			            var rows = api.rows( {page:'current'} ).nodes();
			            var last=null;
			            var totalPickQty = 0;
			            var groupTotalPickQty = 0;
			            var totalIssueQty = 0;
			            var groupTotalIssueQty = 0;
			            var totalIssuePriceQty = 0;
			            var groupTotalIssuePriceQty = 0;
			            var totalTax = 0;
			            var groupTotalTax = 0;
			            var groupEnd = 0;
			            var currentRow = 0;
			            var totalGstTax=0;
			            var groupTotalGstTax=0;
			            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
			                if ( last !== group ) {
			                	var groupTotalTaxVal=null,groupTotalPickQtyVal=null,groupTotalIssueQtyVal=null,groupTotalIssuePriceQtyVal=null;
			               
			                	if(groupTotalTax==null || groupTotalTax==0){
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
			                		groupTotalPickQtyVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
			                		groupTotalIssueQtyVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
			                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
			                	}
			                	/* if (i > 0) {
			                		$(rows).eq( i ).before(
					                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + groupTotalPickQtyVal + '</td><td>' + groupTotalIssueQtyVal + '</td><td>' + groupTotalIssuePriceQtyVal + '</td><td>' + groupTotalTaxVal + '</td></td></tr>'
					                    );
			                	} */
			                    last = group;
			                    groupEnd = i;    
			                    groupTotalPickQty = 0;
			                    groupTotalIssueQty = 0;
			                    groupTotalIssuePriceQty = 0;
			                    groupTotalTax = 0;
			                    
			                }
			                
			                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
			                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
			                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
			                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
			                <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
			                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                totalTax += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
					    	<%} else {%>
			                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
			                totalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
			                <%}%>
			                
			                currentRow = i;
			            } );
			            if (groupEnd > 0 || rows.length == (currentRow + 1)){
			            	
			            	var totalPickQtyVal=null,groupTotalPickQtyVal=null,totalIssueQtyVal=null,groupTotalIssueQtyVal=null,
			            	totalIssuePriceQtyVal=null,groupTotalIssuePriceQtyVal=null,totalTaxVal=null,groupTotalTaxVal=null;
			            	
			            	if(totalPickQty==null || totalPickQty==0){
			            		totalPickQtyVal="0.000";
		                	}else{
		                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
		                		groupTotalPickQtyVal="0.000";
		                	}else{
		                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalIssueQty==null || totalIssueQty==0){
		                		totalIssueQtyVal=parseFloat("0.000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
		                		groupTotalIssueQtyVal="0.000";
		                	}else{
		                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalIssuePriceQty==null || totalIssuePriceQty==0){
		                		totalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalIssuePriceQtyVal=parseFloat(totalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
		                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalTax==null || totalTax==0){
		                		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalTax==null || groupTotalTax==0){
		                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
		                	}
	                		<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
						    	<%} else {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalIssuePriceQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
			                        <%}%>
		                    );
		                	/* $(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + groupTotalPickQtyVal + '</td><td>' + groupTotalIssueQtyVal + '</td><td></td><td></td><td></td><td></td><td>' + groupTotalIssuePriceQtyVal + '</td><td>' + groupTotalTaxVal + '</td><td>' + groupPriceVal + '</td><td></td><td></td></tr>'
		                    ); */
		                }
			        } 
				});
		        tableprint = $('#tableprint').DataTable({
					"processing": true,
					"lengthMenu": [[-1],["All"]],
					"ajax": {
						"type": "POST",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
					
				        	if(data.items.length === 0){
				        		return [];
				        	}else {
				        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
				        		}
				        		
				        		return data.items;
				        	}
				        }
				    },
			        "columns": [

		    			{"data": 'INDEX', "orderable": true},
		    			{"data": 'SALESDATE', "orderable": true},
		    			{"data": 'DONO', "orderable": true},
		    			{"data": 'CNAME', "orderable": true},
		    			{"data": 'AMOUNT', "orderable": true},
		    			{"data": 'DISCOUNT', "orderable": true},
		    			<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		    			<%} else {%>
		    			{"data": 'TAX', "orderable": true},
		    			<%} %>
		    			{"data": 'TOTALAMOUNT', "orderable": true}
		    			
		    			],
		    		"columnDefs": [
		    			<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
			    		{"className": "t-right", "targets": [4,5,6]}
			    		<%}else {%>
			    		{"className": "t-right", "targets": [4,5,6,7]}
			    		<%} %>
			    		],
					"orderFixed": [ ], 
					"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
					"<'row'<'col-md-6'><'col-md-6'>>" +
					"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
					buttons: [],
			        "order": [],
			        searching: false,
			        paging: false,
			        info: false,
			        "bLengthChange" : false, //thought this line could hide the LengthMenu
			   	    "drawCallback": function ( settings ) {
						var groupColumn = 0;
						var groupRowColSpan= 3;
					   	var api = this.api();
			            var rows = api.rows( {page:'current'} ).nodes();
			            var last=null;
			            var totalPickQty = 0;
			            var groupTotalPickQty = 0;
			            var totalIssueQty = 0;
			            var groupTotalIssueQty = 0;
			            var totalIssuePriceQty = 0;
			            var groupTotalIssuePriceQty = 0;
			            var totalTax = 0;
			            var groupTotalTax = 0;
			            var groupEnd = 0;
			            var currentRow = 0;
			            var totalGstTax=0;
			            var groupTotalGstTax=0;
			            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
			                if ( last !== group ) {
			                	var groupTotalTaxVal=null,groupTotalPickQtyVal=null,groupTotalIssueQtyVal=null,groupTotalIssuePriceQtyVal=null;
			               
			                	if(groupTotalTax==null || groupTotalTax==0){
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
			                		groupTotalPickQtyVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
			                		groupTotalIssueQtyVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
			                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
			                	}
			                    last = group;
			                    groupEnd = i;    
			                    groupTotalPickQty = 0;
			                    groupTotalIssueQty = 0;
			                    groupTotalIssuePriceQty = 0;
			                    groupTotalTax = 0;
			                    
			                }
			                
			                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
			                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
			                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
			                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
			                <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
			                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                totalTax += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
					    	<%} else {%>
			                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
			                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
			                totalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
			                <%}%>
			                
			                currentRow = i;
			            } );
			            if (groupEnd > 0 || rows.length == (currentRow + 1)){
			            	
			            	var totalPickQtyVal=null,groupTotalPickQtyVal=null,totalIssueQtyVal=null,groupTotalIssueQtyVal=null,
			            	totalIssuePriceQtyVal=null,groupTotalIssuePriceQtyVal=null,totalTaxVal=null,groupTotalTaxVal=null;
			            	
			            	if(totalPickQty==null || totalPickQty==0){
			            		totalPickQtyVal="0.000";
		                	}else{
		                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
		                		groupTotalPickQtyVal="0.000";
		                	}else{
		                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalIssueQty==null || totalIssueQty==0){
		                		totalIssueQtyVal=parseFloat("0.000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
		                		groupTotalIssueQtyVal="0.000";
		                	}else{
		                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalIssuePriceQty==null || totalIssuePriceQty==0){
		                		totalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalIssuePriceQtyVal=parseFloat(totalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
		                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
		                	}if(totalTax==null || totalTax==0){
		                		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
		                	}if(groupTotalTax==null || groupTotalTax==0){
		                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
		                	}
	                		<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
						    	<%} else {%>
		                	$(rows).eq( currentRow ).after(
			                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalIssuePriceQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
			                        <%}%>
		                    );
		                }
			        } 
				});
		    }

		}

		$('#tablePosSalesSummary').on('column-visibility.dt', function(e, settings, column, state ){
			if (!state){
				groupRowColSpan = parseInt(groupRowColSpan) - 1;
			}else{
				groupRowColSpan = parseInt(groupRowColSpan) + 1;
			}
			$('#tablePosSalesSummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
			$('#tablePosSalesSummary').attr('width', '100%');
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
			        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#dddddd";
		                     
			        	outPutdata = outPutdata+item.OUTBOUNDDETAILS
		                      	ii++;
			            
			          });
				}else{
			}
		      outPutdata = outPutdata +'</TABLE>';
		      document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
		       document.getElementById('spinnerImg').innerHTML ='';

		   
		 }
					
			


		</script>
	  </div>
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
	$('[data-toggle="tooltip"]').tooltip();	
	  var plant= '<%=PLANT%>';  
	  
	  

	  /* Order Number Auto Suggestion */
		$('#ORDERNO').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'DONO',  
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/InvoiceServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
							DONO : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.orders);
						}
			   });
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    return '<p>' + data.DONO + '</p>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					document.form.ORDERNO.value = "";
				}
			});

		/* Customer Auto Suggestion */
		$('#CUSTOMER').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'CNAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/MasterServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							ACTION : "GET_CUSTOMER_DATA",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.CUSTMST);
						}
			   });
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    	return '<div onclick="getcustname(\''+data.TAXTREATMENT+'\',\''+data.CUSTNO+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";
				$('.customerAddBtn').remove();  
				$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');
				$(".customerAddBtn").width(menuElement.width());
				$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				$('.customerAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					document.form.CUST_CODE.value = "";
				}
			});

		/* outlet Auto Suggestion */
		$('#OUTLET_NAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'OUTLET_NAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_OUTLET_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.POSOUTLETS);
				}
				});
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
// 			    return '<div><p class="item-suggestion"> ' + data.OUTLET_NAME + '</p></div>';
			    return '<div onclick="setOutletData(\''+data.OUTLET+'\',\''+data.OUTLET_NAME+'\')"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				removeterminaldropdown();
	     		addterminaldropdown();
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					document.form.OUTCODE.value = "";
				}
			});

		/* terminal Auto Suggestion */
		$('#TERMINALNAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'TERMINAL_NAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_TERMINAL_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.POSOUTLETS);
				}
				});
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    return '<div><p class="item-suggestion"> ' + data.TERMINAL_NAME + '</p></div>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					document.form.TERMINALNAME.value = "";
				}
			});

		/* Employee Auto Suggestion */
		$('#CUST_NAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'EMPNO',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "getEmployeeListStartsWithName",
					TYPE : "CASHIER",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.EMP_MST);
				}
				});
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    return '<div><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">'+data.FNAME+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = $(".tt-menu").height()+35;
				top+="px";
				$('.supplierAddBtn').remove();  
				$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
				$(".supplierAddBtn").width($(".tt-menu").width());
				$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();		  
			}).on('typeahead:open',function(event,selection){
				$('.supplierAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			});

		/* sales Auto Suggestion */
		$('#SALES_MAN').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'EMPNO',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "getEmployeeListStartsWithName",
					TYPE : "SALES",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.EMP_MST);
				}
				});
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    return '<div><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">'+data.FNAME+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = $(".tt-menu").height()+35;
				top+="px";
				$('.supplierAddBtn').remove();  
				$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
				$(".supplierAddBtn").width($(".tt-menu").width());
				$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();		  
			}).on('typeahead:open',function(event,selection){
				$('.supplierAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:change',function(event,selection){
				
			});

		/* Product Auto Suggestion */
		$('#item').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'ITEM',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/ItemMstServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
					ITEM : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.items);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
					return '<div><p class="item-suggestion">'+data.ITEM+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
					//return '<div><p class="item-suggestion">'+data.ITEM+'</p></div>';
				}
			  }
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			});

		/*category Auto Suggestion */
		$('.ProductClass').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'PRD_CLS_ID',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_PRD_CLS_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.PRD_CLSMST);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
				return '<div><p class="item-suggestion">'+data.PRD_CLS_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_CLS_DESC+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});

		/*SubCategory Auto Suggestion */
		$('.ProductType').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'PRD_TYPE_ID',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_PRD_TYPE_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.PRD_TYPEMST);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
				return '<div><p class="item-suggestion">'+data.PRD_TYPE_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_TYPE_DESC+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		
		/*Brand Auto Suggestion */
		$('.ProductBrand').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'PRD_BRAND_ID',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_PRD_BRAND_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.PRD_BRANDMST);
				}
					});
			},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
				return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});

});

function removeterminaldropdown(){
	$("#TERMINALNAME").typeahead('destroy');
}
function changeorderno(obj){
	 $("#ORDERNO").typeahead('val', '"');
	 $("#ORDERNO").typeahead('val', '');
	 $("#ORDERNO").focus();
	}
function changecustomer(obj){
	 $("#CUSTOMER").typeahead('val', '"');
	 $("#CUSTOMER").typeahead('val', '');
	 $("#CUSTOMER").focus();
	}
function changeoutlet(obj){
	 $("#OUTLET_NAME").typeahead('val', '"');
	 $("#OUTLET_NAME").typeahead('val', '');
	 $("#OUTLET_NAME").focus();
	}
function changeterminal(obj){
	 $("#TERMINALNAME").typeahead('val', '"');
	 $("#TERMINALNAME").typeahead('val', '');
	 $("#TERMINALNAME").focus();
	}
function changecust_name(obj){
	 $("#CUST_NAME").typeahead('val', '"');
	 $("#CUST_NAME").typeahead('val', '');
	 $("#CUST_NAME").focus();
	}
function changesales_man(obj){
	 $("#SALES_MAN").typeahead('val', '"');
	 $("#SALES_MAN").typeahead('val', '');
	 $("#SALES_MAN").focus();
	}
function changeproductbrand(obj){
	 $("#PRD_BRAND_ID").typeahead('val', '"');
	 $("#PRD_BRAND_ID").typeahead('val', '');
	 $("#PRD_BRAND_ID").focus();
	}
function changeProductType(obj){
	 $("#PRD_TYPE_ID").typeahead('val', '"');
	 $("#PRD_TYPE_ID").typeahead('val', '');
	 $("#PRD_TYPE_ID").focus();
	}
function changeitem(obj){
  	 $("#item").typeahead('val', '"');
  	 $("#item").typeahead('val', '');
  	 $("#item").focus();
  	}
   function changeCategory(obj){
  	 $("#PRD_CLS_ID").typeahead('val', '"');
  	 $("#PRD_CLS_ID").typeahead('val', '');
  	 $("#PRD_CLS_ID").focus();
  	}

function addterminaldropdown(){
	  $('#TERMINALNAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TERMINAL_NAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_TERMINAL_DATA",
				ONAME : document.form.OUTCODE.value,
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.POSOUTLETS);
			}
			});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<div><p class="item-suggestion"> ' + data.TERMINAL_NAME + '</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.TERMINALNAME.value = "";
			}
		});

}
function setOutletData(OUTLET,OUTLET_NAME){
	$("input[name=OUTCODE]").val(OUTLET);
}
	
function getcustname(TAXTREATMENT,CUSTNO){
	$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
	document.form.CUST_CODE.value = CUSTNO;
}

function PrintTable() {
    var printWindow = window.open('', '', '');
    printWindow.document.write('<html><head><title>Print <%=title %></title>');

    //Print the Table CSS.
    var table_style = document.getElementById("table_style").innerHTML;
    printWindow.document.write('<style type = "text/css">');
    printWindow.document.write(table_style);
    printWindow.document.write('</style>');
    printWindow.document.write('</head>');

    //Print the DIV contents i.e. the HTML Table.
    printWindow.document.write('<body>');
    var divContents = document.getElementById("dvContents").innerHTML;
    printWindow.document.write(divContents);
    printWindow.document.write('</body>');

    printWindow.document.write('</html>');
    printWindow.document.close();
    printWindow.print();
}
</script>
<style id="table_style" type="text/css">
.printtable
{
border: 1px solid #ccc;
border-collapse: collapse;
}
.printtable th, .printtable td
{
padding: 5px;
border: 1px solid #ccc;
}
.printtable tr:last-child {
font-weight: bold;
}
.printtable td:nth-last-child(3),
td:nth-last-child(2),
td:nth-last-child(1) {
text-align: right;
}
</style>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>