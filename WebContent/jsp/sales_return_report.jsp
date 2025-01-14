<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@page import="com.track.constants.TransactionConstants"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Sales Order Return Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
</jsp:include>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<!-- <script language="JavaScript" type="text/javascript" src="js/general2.js"></script> -->
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
//---Modified by Deen on May 22 2014, Description:To open Activity Logs  in excel powershell format
function ExportReport()
{
	   var flag    = "false";
	   var FROM_DATE    = document.form.FROM_DATE.value;
	   var TO_DATE      = document.form.TO_DATE.value;
	   var ITEMNO         = document.form.ITEM.value;

	   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
	   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
	   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
	   if(flag == "false"){ alert("Please define any one search criteria"); return false;}
       document.form.action="/track/ReportServlet?action=ExportExcelSalesReturnSummary";
 	   document.form.submit();
 }
//---End Modified by Deen on May 22 2014, Description:To open Activity Logs in excel powershell format

function onGo(){

   var flag    = "false";
   document.form.xlAction.value="";
   var FROM_DATE    = document.form.FROM_DATE.value;
   var TO_DATE      = document.form.TO_DATE.value;
   var ITEMNO       = document.form.ITEM.value;
   var ORDERNO       = document.form.ORDERNO.value;
   
   

   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   if(ORDERNO != null     && ORDERNO != "") { flag = true;}

   if(flag == "false"){ alert("Please define any one search criteria"); return false;}

document.form.action="sales_return_report.jsp";
document.form.submit();
}



</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
Hashtable ht = new Hashtable();
List cnslist=new ArrayList();
session = request.getSession();
//String action         = _strUtils.fString(request.getParameter("action")).trim();
String PLANT= session.getAttribute("PLANT").toString();
String USERID= session.getAttribute("LOGIN_USER").toString();
String FROM_DATE ="",  TO_DATE = "",mkey="",mvalue="", cnskey="",cnsval="",DIRTYPE ="",BATCH ="",USER="",ITEM="",sItemDesc="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String constkey = "",LOC="",REASONCODE="",PRD_CLS_ID="",PRD_TYPE_ID="",PRD_BRAND_ID= "";
String html = "",cntRec ="false",LOC_TYPE_ID="",TYPE="";

PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));


if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

ITEMNO           = _strUtils.fString(request.getParameter("ITEM"));
sItemDesc        = _strUtils.replaceStr2Char(_strUtils.fString(request.getParameter("DESC")));
ORDERNO          = _strUtils.fString(request.getParameter("ORDERNO"));


if(PGaction.equalsIgnoreCase("View")){
 
 try{
        if(ITEMNO.length() > 0) {
         ItemMstUtil itemMstUtil = new ItemMstUtil();
         itemMstUtil.setmLogger(mLogger);
         try{
         String temItem = itemMstUtil.isValidAlternateItemInItemmst( PLANT, ITEMNO);
         ITEMNO = temItem;
         }catch(Exception e){
                 
         }
         }
         
        //Hashtable ht = new Hashtable();
       
        if(_strUtils.fString(ITEMNO).length() > 0)        ht.put("ITEM",ITEMNO);;
        if(_strUtils.fString(ORDERNO).length() > 0)         ht.put("ORDNUM",ORDERNO);
       

        //movQryList = movHisUtil.getSalesReturn(ht,PLANT,fdate,tdate,sItemDesc);
        
        //if(movQryList.size()<=0)
                //cntRec ="true";

 }catch(Exception e) { 
	 System.out.println(e.getMessage());
	 throw new Exception();
 }
}
%>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form" method="post" action="view_movhis_list.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">

<div id="target" style="display:none"> 
  <div class="form-group">    	 
  		<label class="control-label col-sm-2" for="From Date">From Date:</label>
        <div class="col-sm-2">
       <div class="input-group" > 
       <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />          
       <INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY>
       </div>
    	</div>
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="To Date">To Date:</label>
        <div class="col-sm-2">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY>
        </div>
    	</div>
    	</div>
    	<div class="form-inline">
  		<label class="control-label col-sm-2" for="Product ID">Order No:</label>
        <div class="col-sm-2">
        <INPUT class="form-control" name="ORDERNO" type = "TEXT" value="<%=StrUtils.forHTMLTag(ORDERNO)%>" style="width: 100%"  MAXLENGTH=100> 
    	</div> 
 		</div>
    	</div>
    	
    	<div class="form-group">
        <label class="control-label col-sm-2" for="Product ID">Product ID</label>
        <div class="col-sm-2">
        <div class="input-group">
        <INPUT class="form-control" name="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEMNO)%>" size="30"  MAXLENGTH=20>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('item_list.jsp?ITEM='+convertCharToString(form.ITEM.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Description">Description:</label>
       <div class="col-sm-2">
      	<div class="input-group">
    	<INPUT class="form-control" name="DESC" type = "TEXT" value="<%=StrUtils.forHTMLTag(sItemDesc)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('item_list.jsp?ITEM_DESC='+convertCharToString(form.DESC.value));">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div>
 		<div class="form-inline">
  		<div class="col-sm-4">   
  		<button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  		<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  		<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
  	  	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','ROB');}"> <b>Back</b></button>&nbsp;&nbsp; -->
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
      <div class="col-sm-offset-9">
      <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  		<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  		<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
  	  	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','ROB');}"> <b>Back</b></button> -->
  	</div>
        </div>
       	  </div>
  		
  		 <!--  Added by Deen on June 17 2014  -->
       <INPUT type="Hidden" name="USERID" value="<%=StrUtils.forHTMLTag(USERID)%>">
     <!-- End  Added by Deen on June 17 2014  -->
 
		
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableIssueSummary"
									class="table table-bordered table-hover dataTable no-footer "
									role="grid" aria-describedby="tableIssueSummary_info">
					<thead>
		                <tr role="row">
        
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">S/N</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Order No</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Customer</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Location</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Product ID</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Description</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Order Qty</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Issue Qty</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Return Qty</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Remarks</TH>
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
		  movQryList = movHisUtil.getSalesReturn(ht,PLANT,fdate,tdate,sItemDesc);
	        
	        if(movQryList.size()<=0)
	                cntRec ="true";
                    com.track.dao.InvMstDAO _InvMstDAO = new com.track.dao.InvMstDAO();
                    com.track.dao.CustMstDAO _CustMstDAO = new com.track.dao.CustMstDAO();
                    
                    _InvMstDAO.setmLogger(mLogger);
                    _CustMstDAO.setmLogger(mLogger);
                    double  grandtotalOrderQty = 0 ;
                    double grandtotalIssueQty =0;
                    double grandtotalReturnQty = 0;
                  // int iIndex = 0;
                    int irow = 0;
                    
                    for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
                                       Map lineArr = (Map) movQryList.get(iCnt);
            
                      String trDate= "";
                      trDate=(String)lineArr.get("CRAT");
                      
                   
                              if (trDate.length()>8) {
                                      //System.out.println(trDate);
                              trDate    = trDate.substring(8,10)+":"+ trDate.substring(10,12)+":"+trDate.substring(12,14);
                              }
                 
                    
                              int  iIndex = iCnt + 1; 
                      String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                /*   Display grand total at the bottom.  */
                      grandtotalOrderQty = grandtotalOrderQty + Double.parseDouble((String)lineArr.get("QTYOR"));
                      grandtotalIssueQty = grandtotalIssueQty + Double.parseDouble((String)lineArr.get("QTYIS"));
                      grandtotalReturnQty = grandtotalReturnQty + Double.parseDouble((String)lineArr.get("QTYRETURN"));
       %>
       var rowData = [];
       rowData[rowData.length] = '<%=iIndex%>';
       rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("ORDNUM")) %>';
       rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("CUSTOMER"))%>';
       rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("LOC")) %>';
       rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("ITEM")) %>';
       rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("ITEMDESC")) %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("QTYOR"))%>';
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("QTYIS"))%>';
       rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("QTYRETURN"))%>';
       rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("REMARKS"))%>';
                tableData[tableData.length] = rowData;
       <%}%>  
   

       var groupColumn = 1;
       $(document).ready(function(){
      	 $('#tableIssueSummary').DataTable({
      		 	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
      		  	data: tableData,
      		  	"columnDefs": [{"className": "t-right", "targets": [6,7,8]}],
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
                          columns: ':not(:eq('+groupColumn+')):not(:eq(3)):not(:eq(4)):not(:eq(6))'
                      }
      	        ], "drawCallback": function ( settings ) {
	        	 var groupColumn = 1;
				var groupRowColSpan = 5;
			   	var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalSub = 0;
	            var groupTotalSub = 0;
	            var totalTax = 0;
	            var groupTotalTax = 0;
	            var totalPrice = 0;
	            var groupTotalPrice = 0;
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                      //  '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + parseFloat(groupTotalSub).toFixed(3) + '</td><td>' + parseFloat(groupTotalTax).toFixed(3) + '</td><td>' + parseFloat(groupTotalPrice).toFixed(3) + '</td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;    
	                    groupTotalSub = 0;
	                    groupTotalTax = 0;
	                    groupTotalPrice = 0;
	                    	                    
	                }
	                groupTotalSub += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
	                totalSub += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
	                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
	                totalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
	                groupTotalPrice += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
	                totalPrice += parseFloat($(rows).eq( i ).find('td:eq(8)').html().replace(',', '').replace('$', ''));
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + parseFloat(totalSub).toFixed(3) + '</td><td>' + parseFloat(totalTax).toFixed(3) + '</td><td>' + parseFloat(totalPrice).toFixed(3) + '</td><td></td></tr>'
                   );
               	$(rows).eq( currentRow ).after(
	                      //  '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + parseFloat(groupTotalSub).toFixed(3) + '</td><td>' + parseFloat(groupTotalTax).toFixed(3) + '</td><td>' + parseFloat(groupTotalPrice).toFixed(3) + '</td><td></td></tr>'
                   );
               }
	        } 
		  });	 
});
 
</script>
	   
  </FORM>
  </div></div></div>
  
                  <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
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
	<jsp:param name="nobackblock" value="1" />
</jsp:include>
<Script>
function EmptyMovementtype()
{
	document.form.TYPE.value="";
}


function getProductDetails() {
	var productId = document.form.ITEM.value;
	
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
                        async:false ,
			data : {
				ITEM : productId,
				PLANT : "<%=PLANT%>",
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					
                                if (data.status == "100") {
                                var resultVal = data.result;
                                document.form.DESC.value = resultVal.sItemDesc;
                                 }
				}
			});
		
	}
</Script>
