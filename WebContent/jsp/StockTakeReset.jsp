<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Stock Take Reset";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>
<style type="text/css">
.dt-button-collection.dropdown-menu{
left: 50px; !important;
}
</style>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'InboundOrderList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onReset() {

		var mes = confirm("Do you want to Delete the Stock Take !");
		if (mes == true) {
			document.form2.xlAction.value = "Reset";
			document.form2.action = "../inhouse/stocktakereset";
			document.form2.submit();

		} else {
			return false;
		}

	}
</script>

<%

	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String PLANT = (String) session.getAttribute("PLANT");

	String action = StrUtils.fString(request.getParameter("action"))
			.trim();
	String itemNo = StrUtils.fString(request.getParameter("ITEM"))
			.trim();
        String itemDesc = StrUtils.fString(request.getParameter("PRD_DESCRIP")).trim();
	String location = StrUtils.fString(request.getParameter("LOC_ID"))
			.trim();
	String LOC_TYPE_ID =  StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
	String PGaction = StrUtils.fString(request.getParameter("PGaction"))
	.trim();
	

	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	InvUtil invUtil = new InvUtil();
	invUtil.setmLogger(mLogger);
	ArrayList movQryList = new ArrayList();
	StrUtils _strUtils = new StrUtils();
	StockTakeUtil stockTakeUtil = new StockTakeUtil();
	stockTakeUtil.setmLogger(mLogger);
	boolean resetFlag = false;
	String res = "";
	String xlAction = StrUtils.fString(request.getParameter("xlAction")).trim();
	System.out.println("xlAction" + xlAction);
	if (xlAction.equalsIgnoreCase("Reset")) {

		try {
			 ItemMstUtil itemMstUtil = new ItemMstUtil();
			 itemMstUtil.setmLogger(mLogger); String temItem="";
			 if(!itemNo.equals("")){
			 temItem = itemMstUtil.isValidAlternateItemInItemmst( PLANT, itemNo);
			 if (temItem != "") {
				 itemNo = temItem;
			 } else {
			 	throw new Exception("Product not found!");
			 }}
			HashMap<String, String> inputValues = new HashMap<String, String>();
			if (!itemNo.equals("")) {
				inputValues.put("ITEM", itemNo);
			}
			if (!location.equals("")) {
				inputValues.put("LOC", location);
			}
			System.out.println("Location : "+ location);
			System.out.println("Item : "+ itemNo);
			inputValues.put("PLANT", PLANT);
                        inputValues.put("USER", sUserId);
			resetFlag = stockTakeUtil.removeStockTake(inputValues);
			if (resetFlag) {
				res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">StockTake  Deleted Successfully</font>";
			} else {
				res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">No Data Found For Stock Take</font>";
			}

		} catch (Exception e) {
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">No Data Found For Stock Take</font>";
		}
	}
	if (PGaction.equalsIgnoreCase("View")) {
	try {
		HashMap<String, String> inputValues = new HashMap<String, String>();
		if (!itemNo.equals("")) {
			inputValues.put("stktake.ITEM", itemNo);
		}
		if (!location.equals("")) {
			inputValues.put("stktake.LOC", location);
		}
		/*if (!LOC_TYPE_ID.equals("")) {
			inputValues.put("c.LOC_TYPE_ID", LOC_TYPE_ID);
		}*/
		
		movQryList = stockTakeUtil.getStockTakeDetails(PLANT,
				inputValues,itemDesc,LOC_TYPE_ID);

	} catch (Exception e) {
	}
	}
%>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>
                <li><label>Stock Take Reset</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
                     <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="onExport();">
						Export All Data</button>
					&nbsp;
				</div>
				
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
              </div>
		</div>
<div class="box-body">

		
<FORM class="form-horizontal" name="form1" method="post" action=../inhouse/stocktakereset">
<input type="hidden" name="PGaction" value=<%=PGaction%>>

<div id="target" style="display:none">
		<div class="form-group">
		<div class="row" style="padding:3px">
        	<label class="control-label col-sm-2" for="Location">Search</label>
	        	<div class="col-sm-3 ac-box">
        			<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
        			<INPUT class="ac-selected  form-control typeahead"placeholder="LOCATION" name="LOC_ID" id="LOC_ID"  type = "TEXT" value="<% if(!location.equals("")) {out.print(location);} %>" size="30"  MAXLENGTH=20>
        			<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_ID\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i> </span>
   				</div>
       			<div class="col-sm-3 ac-box">
    				<INPUT class="ac-selected  form-control typeahead" placeholder="LOCATION TYPE" name="LOC_TYPE_ID" id="LOC_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(LOC_TYPE_ID)%>" size="30"  MAXLENGTH=20>
    	  			<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i> </span>
 				</div> 	
 		      	<div class="col-sm-3 ac-box">
        			<INPUT class="ac-selected  form-control typeahead" placeholder="PRODUCT" name="ITEM" id="ITEM" type = "TEXT" value="<% if(!itemNo.equals("")) {out.print(itemNo);} %>" size="30"  MAXLENGTH=20>
          			<span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()">
		  			<i class="glyphicon glyphicon-menu-down"></i> </span>
   				</div>
 		</div>
 		<input type="hidden" name="LOC_DESC" value="">
 		
 		<div class="row" style="display: none;">
        <label class="control-label col-sm-2" for="Product ID"></label>
        <div class="col-sm-3 ac-box" style="display: none;">
        <INPUT class="ac-selected  form-control typeahead" placeholder="PRODUCT" name="ITEM" id="ITEM" type = "hidden" value="<% if(!itemNo.equals("")) {out.print(itemNo);} %>" size="30"  MAXLENGTH=20>
          <span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()">
					  		<i class="glyphicon glyphicon-menu-down"></i> </span>
   		</div>
  		<div class="col-sm-3 ac-box" style="display: none;">
  		<label class="control-label col-sm-2" for="Description"></label>
       <div class="col-sm-3">
    	<INPUT class="form-control" name="PRD_DESCRIP" id="PRD_DESCRIP" type = "hidden" value="<%=strUtils.forHTMLTag(itemDesc)%>" size="30"  MAXLENGTH=20>   		 	
 		</div>
 		</div> 
 		</div>
 		
  	 			<div class="row" style="padding:3px">
					<label class="control-label col-sm-2" for="search"></label>
					<div class="col-sm-3">
  					<button type="button" class="Submit btn btn-success" name="VIEW_REPORT" id="VIEW_REPORT" onClick="viewReport();">Search</button>&nbsp;  	
<!--   	<button type="button" class="Submit btn btn-default"  onClick="onExport();"><b>Export All Data</b></button>&nbsp;  	 -->
  	<button type="button" class="Submit btn btn-danger" onClick="onReset()">Reset All</button>&nbsp;  
<!--   	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;	   -->
  	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RST');}"> <b>Back</b></button>&nbsp;&nbsp; -->
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
      <div class="col-sm-offset-8">
<!--     <button type="button" class="Submit btn btn-default" name="VIEW_REPORT" id="VIEW_REPORT" onClick="viewReport();"><b>View</b></button>&nbsp; -->
<!--   	<button type="button" class="Submit btn btn-default"  onClick="onExport();"><b>Export All Data</b></button>&nbsp; -->
<!--   	<button type="button" class="Submit btn btn-default" onClick="onReset()"><b>Reset All</b></button>&nbsp;  -->
<!--   	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
  	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RST');}"> <b>Back</b></button> -->
  	</div>
        </div>
       	  </div>

</form>

<FORM name="form2" method="post" action="StockTakeReset.jsp">
<input type="hidden" name="ITEM" id="ITEM" value="<% if(!itemNo.equals("")) {out.print(itemNo);} %>">
<input type="hidden" name="LOC_ID" id="LOC_ID" value="<% if(!location.equals("")) {out.print(location);} %>">
<input type="hidden" name="xlAction" value=<%=xlAction%>> 

<center><%=res%></center>
<div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12" >
              	<table id="tableIssueSummary" class="table table-bordred table-striped"	role="grid" aria-describedby="tableIssueSummary_info">
<!--               	<table id="tableIssueSummary" class="table table-bordered table-hover dataTable no-footer "	role="grid" aria-describedby="tableIssueSummary_info"> -->
					<thead>
		                <tr role="row">
        
          <th style="font-size: smaller;">S/N</TH>
          <th style="font-size: smaller;">LOCATION</TH>
          <th style="font-size: smaller;">TRANDATE</TH>
          <th style="font-size: smaller;">PRODUCT ID</TH>
          <th style="font-size: smaller;">DESCRIPTION</TH>
          <th style="font-size: smaller;">PRODUCT CLASS</TH>
          <th style="font-size: smaller;">PRODUCT TYPE</TH>
          <th style="font-size: smaller;">PRODUCT BRAND</TH>
          <th style="font-size: smaller;">UOM</TH>
          <th style="font-size: smaller;">BATCH</TH>
          <th style="font-size: smaller;">STOCK TAKE QUANTITY</TH>          
          <th style="font-size: smaller;">INVENTORY QUANTITY</TH>
          <th style="font-size: smaller;">PC/PCS/EA QUANTITY</th>
          <th style="font-size: smaller;">QUANTITY DIFF</TH>
          <th style="font-size: smaller;">PC/PCS/EA QUANTITY DIFF</th>
         
                    </tr>
		            </thead>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <script type="text/javascript">
  var tableData = [];

	<% 
	double invqty,pcsqty = 0;
    double stockqty=0;
    String strDiffQty,strpcsDiffQty="";
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {

			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#FFFFFF";
			
			invqty  =  Double.parseDouble((String)lineArr.get("INVQTY"));
            pcsqty  =  Double.parseDouble((String)lineArr.get("PCSQTY"));
            stockqty  =  Double.parseDouble((String)lineArr.get("QTY"));
            
            strDiffQty=Double.toString(stockqty-invqty);
			if(stockqty==0 && invqty!=0)
            {
            if(pcsqty>0)
           	 strpcsDiffQty=Double.toString(stockqty-pcsqty);
            else
           	 strpcsDiffQty="0";	 
            }
            else
            {
            	if(pcsqty!=0)
           	 		strpcsDiffQty="-"+Double.toString(pcsqty);
            	else
            		strpcsDiffQty=Double.toString(pcsqty);	
            }
	%>
	var rowData = [];
    rowData[rowData.length] = '<%=iIndex%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("LOC")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("TRANDATE")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("ITEM")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("DESCRIP")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("PRD_CLS_ID")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("ITEMTYPE")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("PRD_BRAND_ID")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("STKUOM")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("BATCH")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("QTY")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("INVQTY")%>';
    rowData[rowData.length] = '<%=(String) lineArr.get("PCSQTY")%>';
    rowData[rowData.length] = '<%=(String) strDiffQty%>';
    rowData[rowData.length] = '<%=(String) strpcsDiffQty%>';
             tableData[tableData.length] = rowData;
	
	<%
		}
	%>
	var groupColumn = 1;
	$(document).ready(function(){
   	 $('#tableIssueSummary').DataTable({
   		
   		 "lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
   		  	data: tableData,
   		   "columnDefs": [{"className": "t-right", "targets": [9,10,11]}],
			"orderFixed": [ groupColumn, 'asc' ], 
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
                              pageSize: 'A3'
   	                    }
   	                ]
   	            },
   	            {
   	            	extend: 'colvis',
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
                   
                  }
   	        ],  
   		  });	 
   });
	 
</script>
<%-- <table align="center">
	<TR>
		 <td><input type="button" value=" Back "	onClick="window.location.href='../home'">&nbsp;</td>
		<td><input type="button" value=" ExportReport " onclick="onExport();" /></td>
		<td><input type="button" value=" Reset All " onclick="onReset()" /></td> 
	</TR>
	<TR>
		<TD COLSPAN=2><BR>
		<B>
		<CENTER><%=res%>
		</B></TD>
	</TR>
</table> --%>
<SCRIPT LANGUAGE="JavaScript">
function validateProduct() {
        var productId = document.form1.ITEM.value;
	var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=PLANT%>",
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
                                                document.form1.PRD_DESCRIP.value = resultVal.sItemDesc;
                                         } 
				}
			});
		
	}
function onExport(){
	document.form1.action = "/track/StockTakeServlet?action=Export_Excel";
	document.form1.submit();
	
} 
function viewReport(){
	document.form1.PGaction.value = "View";
	document.form1.action = "../inhouse/stocktakereset";
	document.form1.submit();
	
} 


</Script>
</FORM>
</div></div></div>

                  <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
	 var plant= '<%=PLANT%>'; 
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

    /* Product Number Auto Suggestion */
	$('#ITEM').typeahead({
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
// 			return '<p onclick="document.form.ITEM_DESC.value = \''+data.ITEMDESC+'\'">'+data.ITEM+'</p>';
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

	/* location Auto Suggestion */
	$('#LOC_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOC_MST);
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
			return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
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
    
    /* location 1 Auto Suggestion */
	$('#LOC_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
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
    
 });
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>