
<!-- PAGE CREATED BY : IMTHI -->
<!-- DATE 14-06-2022 -->
<!-- DESC : Shift Close Summary -->

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
String title = "Shift Close";
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
	
	String FROM_DATE="",TO_DATE="",searchtype="",CUST_NAME  = "",SALES_MAN="",OUTLET_CODE = "",TERMINAL_CODE ="",sOutCode="",sTerminalCode="";
	
	
	boolean flag = false;
	session = request.getSession();
	PLANT = session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String msg =  StrUtils.fString(request.getParameter("Msg"));
	String types =  StrUtils.fString(request.getParameter("srctype"));
	fieldDesc =  StrUtils.fString(request.getParameter("result"));
	String PGaction =  StrUtils.fString(request.getParameter("PGaction"));
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
	sTerminalCode = strUtils.InsertQuotes(strUtils.fString(request.getParameter("TERMINALCODE")));
	
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
                <li><label>Shift Close</label></li>                                   
            </ul>
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
                            
               <!-- <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button>
					&nbsp;
				</div> -->
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
 
	
<FORM class="form-horizontal" name="form" method="post" action="ShiftCloseSummary.jsp">
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<input type="hidden" name="plant" value="<%=PLANT%>">
<input type="hidden" name="CUST_CODE" >
<input type="hidden" name="OUTLET_CODE" value="">
<input type="hidden" name="TERMINAL_CODE" value="">
<INPUT type="hidden" name="OUTCODE" value="<%=sOutCode%>">
<INPUT type="hidden" name="TERMINALCODE" value="<%=sTerminalCode%>">
<%-- <input type="hidden" name="srctype" value="<%=searchtype%>"> --%>
<!-- <input type="hidden" name="srctype" value=""> -->

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
            <th style="font-size: smaller;">SHIFT</th>
            <th style="font-size: smaller;">SHIFT DATE</th>
            <th style="font-size: smaller;">OUTLET</th>
            <th style="font-size: smaller;">COUNTER</th>
			<th style="font-size: smaller;">CASHIER</th>
            <th style="font-size: smaller;">TOTAL BILLS</th>
            <th style="font-size: smaller;">CASH SALES</th>
            <th style="font-size: smaller;">OTHER SALES</th>    
            <th style="font-size: smaller;">DISCOUNT</th>    
            <th style="font-size: smaller;">VOID SALES</th>    
            <th style="font-size: smaller;">RETURNED SALES</th>   
            <th style="font-size: smaller;">EXPENSE</th>   
            <th style="font-size: smaller;">DIFFERENCE AMOUNT</th>
            <th style="font-size: smaller;">TOTAL SALES</th>
            <th style="font-size: smaller;">TOTAL COST</th>
            <th style="font-size: smaller;">GP%</th>
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
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
						<!-- END -->
</table>
</div>  

<div style="overflow-x:auto;">
<table id="tablePOSShiftClos" class="table table-bordred table-striped"  style="width: 100%;">  
   
   <thead style="text-align: center">  
          <tr>  
            <th style="font-size: smaller;">S/N</th>  
            <th style="font-size: smaller;">SHIFT</th>
            <th style="font-size: smaller;">SHIFT DATE</th>
            <th style="font-size: smaller;">OUTLET</th>
            <th style="font-size: smaller;">COUNTER</th>
			<th style="font-size: smaller;">CASHIER</th>
            <th style="font-size: smaller;">TOTAL BILLS</th>
            <th style="font-size: smaller;">CASH SALES</th>
            <th style="font-size: smaller;">OTHER SALES</th>    
            <th style="font-size: smaller;">DISCOUNT</th>    
            <th style="font-size: smaller;">VOID SALES</th>    
            <th style="font-size: smaller;">RETURNED SALES</th>   
            <th style="font-size: smaller;">EXPENSE</th>
            <th style="font-size: smaller;">DIFFERENCE AMOUNT</th>   
            <th style="font-size: smaller;">TOTAL SALES</th>
            <th style="font-size: smaller;">TOTAL COST</th>
            <th style="font-size: smaller;">GP%</th>
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
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
						<!-- END -->
</table>
</div>  
<script>
		
	
	var tabletype;
	var tableprint;
	var fdate, tdate, outlet_name, terminalname, cashier_name,outletcode,terminalcode, groupRowColSpan = 6;

	function getParameters() {
		return {
		"FDATE":fdate,
		"TDATE":tdate,
		"OUTLET_NAME":outlet_name,
		"TERMINALNAME":terminalname,
		"OUTLETCODE":outletcode,
		"TERMINALCODE":terminalcode,
		"CASHIER_NAME":cashier_name,
		"ACTION": "VIEW_POS_SHIFTCLOSE_SUMMARY",
		"PLANT":"<%=PLANT%>",
		}
	}

	function onGo() {
		plant = document.form.plant.value;
		fdate = document.form.FROM_DATE.value;
		tdate = document.form.TO_DATE.value;
		outlet_name = document.form.OUTLET_NAME.value;
		terminalname = document.form.TERMINALNAME.value;
		casier_name = document.form.CUST_NAME.value;
		outletcode = document.form.OUTCODE.value;
		terminalcode = document.form.TERMINALCODE.value;
		var urlStr = "../posreports/PosShiftCloseSummary";
		//tablePosSalesSummary.ajax.reload(null, false); 
		// Call the method of JQuery Ajax provided
	   	var groupColumn = 1;
	   	var totalQty = 0;
	   
	    // End code modified by Deen for product brand on 11/9/12
	    if (tabletype){
	    	tabletype.ajax.url( urlStr ).load();
	    	tableprint.ajax.url( urlStr ).load();
	    }else{
	    	tabletype = $('#tablePOSShiftClos').DataTable({
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
								data.items[dataIndex]['SHIFT'] = '<a href="../posreports/shiftclosedetail?SID='+data.items[dataIndex]['ID']+'">'+data.items[dataIndex]['SHIFT']+'</a>';
								
			        		}
			        		return data.items;
			        	}
			        }
			    },
		        "columns": [

	    			{"data": 'INDEX', "orderable": true},
	    			{"data": 'SHIFT', "orderable": true},
	    			{"data": 'SHIFTDATE', "orderable": true},
	    			{"data": 'OUTLET', "orderable": true},
	    			{"data": 'TERMINAL', "orderable": true},
	    			{"data": 'CASHIER', "orderable": true},
	    			{"data": 'SALESCOUNT', "orderable": true},
	    			{"data": 'CASH', "orderable": true},
	    			{"data": 'OTHERSALES', "orderable": true},
	    			{"data": 'TOTALDISCOUNT', "orderable": true},
	    			{"data": 'VOIDEDSALES', "orderable": true},
	    			{"data": 'RETURNEDAMOUNT', "orderable": true},
	    			{"data": 'EXPENSE', "orderable": true},
	    			{"data": 'DIFFAMOUNT', "orderable": true},
	    			{"data": 'TOTALAMOUNT', "orderable": true},
	    			{"data": 'TOTALSALESCOST', "orderable": true},
	    			{"data": 'GP', "orderable": true},
	    			],
				"columnDefs": [{"className": "t-right", "targets": [6,7,8,9,10,11,12,13,14]}],
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
	                    /* columns: ':not(:eq('+groupColumn+')):not(:last)' */
	                }
		        ],
		   	       "order": [],
		   	    "drawCallback": function ( settings ) {
					var groupColumn = 0;
					var groupRowColSpan= 5;
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
		            var totalunitPrice = 0;
		            var groupTotalUnitPrice=0;
		            var groupTotalTax = 0;
		            var totalPrice = 0;
		            var groupPrice = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            var totalGstTax=0;
		            var totalDiffAmtTax=0;
		            var groupTotalDiffAmtTax=0;
		            var groupTotalGstTax=0;
		            var totalAmount=0;
		            var groupTotalAmount=0;
		            var TotalSalesCost=0;
		            var groupTotalSalesCost=0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		                	var groupTotalTaxVal=null,groupTotalPickQtyVal=null,groupTotalIssueQtyVal=null,groupTotalIssuePriceQtyVal=null,
		                	groupPriceVal=null,totalUnitPriceVal=null,totalGstTaxVal=null,totalAmountVal=null,TotalSalesCostVal=null,totalDiffAmtTaxVal=null;
		               
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
		                	}if(groupPrice==null || groupPrice==0){
		                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalunitPrice==null || totalunitPrice==0){
		                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalGstTax==null || totalGstTax==0){
		                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(<%=numberOfDecimal%>);
		                	}if(totalAmount==null || totalAmount==0){
		                		totalAmountVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalAmountVal=parseFloat(totalAmount).toFixed(<%=numberOfDecimal%>);
		                	}if(TotalSalesCost==null || TotalSalesCost==0){
		                		TotalSalesCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		TotalSalesCostVal=parseFloat(TotalSalesCost).toFixed(<%=numberOfDecimal%>);
		                	}if(totalDiffAmtTax==null || totalDiffAmtTax==0){//imti
		                		totalDiffAmtTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalDiffAmtTaxVal=parseFloat(totalDiffAmtTax).toFixed(<%=numberOfDecimal%>);
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
		                    groupPrice = 0;
		                    groupTotalUnitPrice=0;
		                    groupTotalGstTax=0;
		                    groupTotalAmount=0;
		                    groupTotalSalesCost=0;
		                    
		                    
		                }
		                
		                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
		                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
		                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
		                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
		                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
		                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
		                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                totalTax += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                groupPrice += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                totalPrice += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                groupTotalUnitPrice += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', '').replace('$', ''));
		                totalunitPrice += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', '').replace('$', ''));
		                groupTotalGstTax += parseFloat($(rows).eq( i ).find('td:eq(12)').html().replace(',', '').replace('$', ''));
		                totalGstTax += parseFloat($(rows).eq( i ).find('td:eq(12)').html().replace(',', '').replace('$', ''));
		                totalDiffAmtTax += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', '').replace('$', ''));
		                groupTotalDiffAmtTax += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', '').replace('$', ''));
		                groupTotalAmount += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', '').replace('$', ''));
		                totalAmount += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', '').replace('$', ''));
		                groupTotalSalesCost += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                TotalSalesCost += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                //console.log("sales cost "+TotalSalesCost);
		                currentRow = i;
		                
		            } );
		            if (groupEnd > 0 || rows.length == (currentRow + 1)){
		            	
		            	var totalPickQtyVal=null,groupTotalPickQtyVal=null,totalIssueQtyVal=null,groupTotalIssueQtyVal=null,
		            	totalIssuePriceQtyVal=null,groupTotalIssuePriceQtyVal=null,totalTaxVal=null,groupTotalTaxVal=null,totalPriceVal=null,
		            	groupPriceVal=null,totalUnitPriceVal=null,groupTotalUnitPriceVal=null,groupTotalGstTaxVal=null,totalGstTaxVal=null,
		            	totalAmountVal=null,groupTotalAmountVal=null,groupTotalSalesCostVal=null,TotalSalesCostVal=null,totalDiffAmtTaxVal=null,groupTotalDiffAmtTaxVal=null;
		            	
		            	if(totalPickQty==null || totalPickQty==0){
		            		totalPickQtyVal="0.00";
	                	}else{
	                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
	                		groupTotalPickQtyVal="0.00";
	                	}else{
	                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
	                	}if(totalIssueQty==null || totalIssueQty==0){
	                		totalIssueQtyVal=parseFloat("0.000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(2);
	                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
	                		groupTotalIssueQtyVal="0.00";
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
	                	}if(totalPrice==null || totalPrice==0){
	                		totalPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalPriceVal=parseFloat(totalPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(groupPrice==null || groupPrice==0){
	                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(totalunitPrice==null || totalunitPrice==0){
	                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalUnitPrice==null || groupTotalUnitPrice==0){
	                		groupTotalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalUnitPriceVal=parseFloat(groupTotalUnitPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(totalGstTax==null || totalGstTax==0){
	                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalGstTax==null || groupTotalGstTax==0){
	                		groupTotalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalGstTaxVal=parseFloat(groupTotalGstTax).toFixed(<%=numberOfDecimal%>);
	                	}if(totalAmount==null || totalAmount==0){
	                		totalAmountVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalAmountVal=parseFloat(totalAmount).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalAmount==null || groupTotalAmount==0){
	                		groupTotalAmountVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalAmountVal=parseFloat(groupTotalAmount).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalSalesCost==null || groupTotalSalesCost==0){
	                		groupTotalSalesCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalSalesCostVal=parseFloat(groupTotalSalesCost).toFixed(<%=numberOfDecimal%>);
	                	}if(TotalSalesCost==null || TotalSalesCost==0){
	                		TotalSalesCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		TotalSalesCostVal=parseFloat(TotalSalesCost).toFixed(<%=numberOfDecimal%>);
	                	}if(totalDiffAmtTax==null || totalDiffAmtTax==0){//imti
	                		totalDiffAmtTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalDiffAmtTaxVal=parseFloat(totalDiffAmtTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalDiffAmtTax==null || groupTotalDiffAmtTax==0){//imti
	                		groupTotalDiffAmtTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalDiffAmtTaxVal=parseFloat(groupTotalDiffAmtTax).toFixed(<%=numberOfDecimal%>);
	                	}
	                	
	                	var gpvalue = ((parseFloat(totalAmountVal)-parseFloat(TotalSalesCost))/(parseFloat(totalAmountVal)))*100;
	                	gpvalue = parseFloat(gpvalue).toFixed(<%=numberOfDecimal%>);
	                	
	                	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalIssuePriceQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td><td class=" t-right">' + totalPriceVal + '</td><td class=" t-right">' + totalUnitPriceVal + '</td><td class=" t-right">' + totalGstTaxVal + '</td><td class=" t-right">' + totalDiffAmtTaxVal + '</td><td class=" t-right">' + totalAmountVal + '</td><td class=" t-right">' + TotalSalesCostVal + '</td><td class=" t-right">' + gpvalue + '</td></tr>'
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
			        	/* if(typeof data.items[0].INDEX === 'undefined'){ */
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
	    			{"data": 'SHIFT', "orderable": true},
	    			{"data": 'SHIFTDATE', "orderable": true},
	    			{"data": 'OUTLET', "orderable": true},
	    			{"data": 'TERMINAL', "orderable": true},
	    			{"data": 'CASHIER', "orderable": true},
	    			{"data": 'SALESCOUNT', "orderable": true},
	    			{"data": 'CASH', "orderable": true},
	    			{"data": 'OTHERSALES', "orderable": true},
	    			{"data": 'TOTALDISCOUNT', "orderable": true},
	    			{"data": 'VOIDEDSALES', "orderable": true},
	    			{"data": 'RETURNEDAMOUNT', "orderable": true},
	    			{"data": 'EXPENSE', "orderable": true},
	    			{"data": 'DIFFAMOUNT', "orderable": true},
	    			{"data": 'TOTALAMOUNT', "orderable": true},
	    			{"data": 'TOTALSALESCOST', "orderable": true},
	    			{"data": 'GP', "orderable": true},
	    			
	    			],
				"columnDefs": [{"className": "t-right", "targets": [6,7,8,9,10,11,12,13,14]}],
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
					var groupRowColSpan= 5;
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
		            var totalunitPrice = 0;
		            var groupTotalUnitPrice=0;
		            var groupTotalTax = 0;
		            var totalPrice = 0;
		            var groupPrice = 0;
		            var groupEnd = 0;
		            var currentRow = 0;
		            var totalGstTax=0;
		            var groupTotalGstTax=0;
		            var totalAmount=0;
		            var groupTotalAmount=0;
		            var TotalSalesCost=0;
		            var groupTotalSalesCost=0;
		            var totalDiffAmtTax=0;
		            var groupTotalDiffAmtTax=0;
		            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
		                if ( last !== group ) {
		                	var groupTotalTaxVal=null,groupTotalPickQtyVal=null,groupTotalIssueQtyVal=null,groupTotalIssuePriceQtyVal=null,
		                	groupPriceVal=null,totalUnitPriceVal=null,totalGstTaxVal=null,totalAmountVal=null,TotalSalesCostVal=null,totalDiffAmtTaxVal=null;
		               
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
		                	}if(groupPrice==null || groupPrice==0){
		                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalunitPrice==null || totalunitPrice==0){
		                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
		                	}if(totalGstTax==null || totalGstTax==0){
		                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(<%=numberOfDecimal%>);
		                	}if(totalAmount==null || totalAmount==0){
		                		totalAmountVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalAmountVal=parseFloat(totalAmount).toFixed(<%=numberOfDecimal%>);
		                	}if(TotalSalesCost==null || TotalSalesCost==0){
		                		TotalSalesCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		TotalSalesCostVal=parseFloat(TotalSalesCost).toFixed(<%=numberOfDecimal%>);
		                	}if(totalDiffAmtTax==null || totalDiffAmtTax==0){//imti
		                		totalDiffAmtTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
		                	}else{
		                		totalDiffAmtTaxVal=parseFloat(totalDiffAmtTax).toFixed(<%=numberOfDecimal%>);	
		                	}
		                    last = group;
		                    groupEnd = i;    
		                    groupTotalPickQty = 0;
		                    groupTotalIssueQty = 0;
		                    groupTotalIssuePriceQty = 0;
		                    groupTotalTax = 0;
		                    groupPrice = 0;
		                    groupTotalUnitPrice=0;
		                    groupTotalGstTax=0;
		                    groupTotalAmount=0;
		                    groupTotalSalesCost=0;
		                    groupTotalDiffAmtTax=0;
		                    
		                }
		                
		                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
		                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
		                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
		                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
		                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
		                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
		                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                totalTax += parseFloat($(rows).eq( i ).find('td:eq(9)').html().replace(',', '').replace('$', ''));
		                groupPrice += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                totalPrice += parseFloat($(rows).eq( i ).find('td:eq(10)').html().replace(',', '').replace('$', ''));
		                groupTotalUnitPrice += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', '').replace('$', ''));
		                totalunitPrice += parseFloat($(rows).eq( i ).find('td:eq(11)').html().replace(',', '').replace('$', ''));
		                groupTotalGstTax += parseFloat($(rows).eq( i ).find('td:eq(12)').html().replace(',', '').replace('$', ''));
		                totalGstTax += parseFloat($(rows).eq( i ).find('td:eq(12)').html().replace(',', '').replace('$', ''));
		                groupTotalDiffAmtTax += parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', '').replace('$', ''));
		                var totalDiffAmt= parseFloat($(rows).eq( i ).find('td:eq(13)').html().replace(',', '').replace('$', ''));
		                totalDiffAmtTax = parseFloat(totalDiffAmtTax)+parseFloat(totalDiffAmt);
		                groupTotalAmount += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', '').replace('$', ''));
		                totalAmount += parseFloat($(rows).eq( i ).find('td:eq(14)').html().replace(',', '').replace('$', ''));
		                groupTotalSalesCost += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                TotalSalesCost += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
		                console.log("sales cost "+TotalSalesCost);
		                currentRow = i;
		            } );
		            if (groupEnd > 0 || rows.length == (currentRow + 1)){
		            	
		            	var totalPickQtyVal=null,groupTotalPickQtyVal=null,totalIssueQtyVal=null,groupTotalIssueQtyVal=null,
		            	totalIssuePriceQtyVal=null,groupTotalIssuePriceQtyVal=null,totalTaxVal=null,groupTotalTaxVal=null,totalPriceVal=null,
		            	groupPriceVal=null,totalUnitPriceVal=null,groupTotalUnitPriceVal=null,groupTotalGstTaxVal=null,totalGstTaxVal=null,
		            	totalAmountVal=null,groupTotalAmountVal=null,groupTotalSalesCostVal=null,TotalSalesCostVal=null,totalDiffAmtTaxVal=null;
		            	
		            	if(totalPickQty==null || totalPickQty==0){
		            		totalPickQtyVal="0.00";
	                	}else{
	                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
	                		groupTotalPickQtyVal="0.00";
	                	}else{
	                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
	                	}if(totalIssueQty==null || totalIssueQty==0){
	                		totalIssueQtyVal=parseFloat("0.000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(2);
	                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
	                		groupTotalIssueQtyVal="0.00";
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
	                	}if(totalPrice==null || totalPrice==0){
	                		totalPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalPriceVal=parseFloat(totalPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(groupPrice==null || groupPrice==0){
	                		groupPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupPriceVal=parseFloat(groupPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(totalunitPrice==null || totalunitPrice==0){
	                		totalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalUnitPriceVal=parseFloat(totalunitPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalUnitPrice==null || groupTotalUnitPrice==0){
	                		groupTotalUnitPriceVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalUnitPriceVal=parseFloat(groupTotalUnitPrice).toFixed(<%=numberOfDecimal%>);
	                	}if(totalGstTax==null || totalGstTax==0){
	                		totalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalGstTaxVal=parseFloat(totalGstTax).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalGstTax==null || groupTotalGstTax==0){
	                		groupTotalGstTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalGstTaxVal=parseFloat(groupTotalGstTax).toFixed(<%=numberOfDecimal%>);
	                	}if(totalAmount==null || totalAmount==0){
	                		totalAmountVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalAmountVal=parseFloat(totalAmount).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalAmount==null || groupTotalAmount==0){
	                		groupTotalAmountVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalAmountVal=parseFloat(groupTotalAmount).toFixed(<%=numberOfDecimal%>);
	                	}if(groupTotalSalesCost==null || groupTotalSalesCost==0){
	                		groupTotalSalesCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		groupTotalSalesCostVal=parseFloat(groupTotalSalesCost).toFixed(<%=numberOfDecimal%>);
	                	}if(TotalSalesCost==null || TotalSalesCost==0){
	                		TotalSalesCostVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		TotalSalesCostVal=parseFloat(TotalSalesCost).toFixed(<%=numberOfDecimal%>);
	                	}if(totalDiffAmtTax==null || totalDiffAmtTax==0){//imti
	                		totalDiffAmtTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
	                	}else{
	                		totalDiffAmtTaxVal=parseFloat(totalDiffAmtTax).toFixed(<%=numberOfDecimal%>);	
	                	}
	                	
	                	var gpvalueprint = ((parseFloat(totalAmountVal)-parseFloat(TotalSalesCostVal))/(parseFloat(totalAmountVal)))*100;
	                	gpvalueprint = parseFloat(gpvalueprint).toFixed(<%=numberOfDecimal%>);
	                	
	                	$(rows).eq( currentRow ).after(
		                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalIssuePriceQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td><td class=" t-right">' + totalPriceVal + '</td><td class=" t-right">' + totalUnitPriceVal + '</td><td class=" t-right">' + totalGstTaxVal + '</td><td class=" t-right">' + totalDiffAmtTaxVal + '</td><td class=" t-right">' + totalAmountVal + '</td><td class=" t-right">' + TotalSalesCostVal + '</td><td class=" t-right">'+gpvalueprint+'</td></tr>'
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
			   /*  return '<div><p class="item-suggestion"> ' + data.TERMINAL_NAME + '</p></div>'; */
			    	 return '<div onclick="setTerminalData(\''+data.TERMINAL+'\',\''+data.TERMINAL_NAME+'\')"><p class="item-suggestion">Name: ' + data.TERMINAL_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.TERMINAL + '</p></div>';
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
					document.form.TERMINALCODE.value = "";
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

});

function setOutletData(OUTLET,OUTLET_NAME){
	$("input[name=OUTCODE]").val(OUTLET);
}

function setTerminalData(TERMINAL,TERMINAL_NAME){
	$("input[name=TERMINALCODE]").val(TERMINAL);
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
.printtable td:nth-last-child(8),
td:nth-last-child(7),
td:nth-last-child(6),
td:nth-last-child(5),
td:nth-last-child(4),
td:nth-last-child(3),
td:nth-last-child(2),
td:nth-last-child(1) {
text-align: right;
}
</style>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>