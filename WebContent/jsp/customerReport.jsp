<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Customer Discount Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
{
  document.form1.action="/track/ReportServlet?action=Export_Customer_Discount_Excel";  
  document.form1.submit();
}


function onGo(){
document.form1.action="customerReport.jsp";
document.form1.submit();
}
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
List saleList =null, stockin=null;
ArrayList invQryListSumTotal  = new ArrayList();
CustMstDAO custdao = new CustMstDAO();

String fieldDesc="";
String USERID ="",PLANT="",LOC ="",  ITEM = "",CUSTOMER="",STOCK_SALE="",STOCK_CLAIM="",BATCH="",PRD_TYPE_ID="",PRD_DESCRIP="", STATUS ="",QTY ="",QTYALLOC ="", FROM_DATE ="",  TO_DATE = "",fdate="",tdate="";
String html = "";
int Total=0,STOCK_EXPIRE_INT=0,STOCK_CLAIM_INT=0, STOCK_FLOATING_INT=0,STOCK_SALE_INT=0;
String SumColor="",PRD_CLS_ID="",PRD_CLS_ID1="",sCustomerTypeId="";
boolean flag=false;
String view_Inv="",RECS_PER_PAGE="";
long listRec=0;

String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
LOC     = strUtils.fString(request.getParameter("LOC"));
ITEM    = strUtils.fString(request.getParameter("ITEM"));
CUSTOMER    = strUtils.fString(request.getParameter("CUSTOMER"));
sCustomerTypeId  = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
RECS_PER_PAGE = strUtils.fString(request.getParameter("RECS_PER_PAGE"));
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));

boolean displaySummaryExport=false,displaySummaryImport=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryExport = ub.isCheckValAcc("exportcustomerdiscount", PLANT,USERID);
displaySummaryImport = ub.isCheckValAcc("importcustomerdiscount", PLANT,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
displaySummaryExport = ub.isCheckValinv("exportcustomerdiscount", PLANT,USERID);
displaySummaryImport = ub.isCheckValinv("importcustomerdiscount", PLANT,USERID);
}
ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);
boolean cntRec=false;

int curPage      = 1;
if(RECS_PER_PAGE.length()==0) RECS_PER_PAGE = "100";
  int recPerPage   = Integer.parseInt(RECS_PER_PAGE);
  long totalRec     = 0;
  String isDisabled ="disabled";
      view_Inv      = strUtils.fString(request.getParameter("VIEW_INV"));
String currentPage        = strUtils.fString(request.getParameter("cur_page"));

String listRecSize =strUtils.fString(request.getParameter("listRecSize")); 
if(listRecSize.length()==0){
listRecSize ="0";
}

if(PGaction.equalsIgnoreCase("View")){
 try{
	 currentPage="1";
	 
	 ITEM = itemMstUtil.isValidInvAlternateItemInItemmst( PLANT, ITEM);
      
      
      Hashtable ht = new Hashtable();
      ht.put("M.PLANT",PLANT);
      if(strUtils.fString(PLANT).length() > 0)        
      if(strUtils.fString(ITEM).length() > 0)      
      {
        ht.put("I.ITEM",ITEM);
      } 
     
      
      if(strUtils.fString(CUSTOMER).length() > 0)      
      {
        ht.put("C.CNAME",new StrUtils().InsertQuotes(CUSTOMER));
      }
      
      if(strUtils.fString(sCustomerTypeId).length() > 0)      
      {
        ht.put("C.CUSTOMER_TYPE_ID",sCustomerTypeId);
      }
      invQryList = custdao.getCustomerCount(ht,PRD_DESCRIP); 
      Map recordArr = (Map) invQryList.get(0);
      listRec = Long.parseLong((String)recordArr.get("RECORDS"));
      int start =  Integer.parseInt(currentPage);
      int end = Integer.parseInt(currentPage)*recPerPage;
         invQryList = custdao.getCustomerForReportPgn(ht,PRD_DESCRIP,start,end);
      if(invQryList.size() <=0)
      {
    	  cntRec =true;

        fieldDesc="Data's Not Found";
       
      }
      if (listRec >0 )  {         totalRec = listRec; listRecSize=new Long(totalRec).toString();
      isDisabled="";
      }
      if (currentPage.length() > 0)
      {
	     try  {   curPage = (new Integer(currentPage)).intValue(); 
	     System.out.println("curPage :: "+curPage); 
     	}
     	catch (Exception e)   {   curPage = 1;                                      
     	}
     }
      
 }catch(Exception e) { 
	 invQryList.clear();
	 cntRec=true;
	 
 }
}
if(PGaction.equalsIgnoreCase("PREVORNEXT")){
	 try{

		int start = (new Integer(currentPage)-1)*recPerPage;
		int end = (new Integer(currentPage)*recPerPage)+1;
	    Hashtable ht = new Hashtable();
	    ht.put("M.PLANT",PLANT);
	    if(strUtils.fString(PLANT).length() > 0)        
	    if(strUtils.fString(ITEM).length() > 0)      
	    {
	      ht.put("I.ITEM",ITEM);
	    } 
	   
	    
	    if(strUtils.fString(CUSTOMER).length() > 0)      
	    {
	      ht.put("C.CNAME",new StrUtils().InsertQuotes(CUSTOMER));
	    }
	    
	    if(strUtils.fString(sCustomerTypeId).length() > 0)      
	    {
	      ht.put("C.CUSTOMER_TYPE_ID",sCustomerTypeId);
	    }
		      invQryList = custdao.getCustomerForReportPgn(ht,PRD_DESCRIP,start,end);
	     if (invQryList != null)  {         totalRec = invQryList.size();
	     isDisabled="";
	     }
	     if (currentPage.length() > 0)
	     {
	     
	    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
	    catch (Exception e)   {   curPage = 1;                                      }
	    }
	 }catch(Exception e) {System.out.println("Exception :getStockTakeList"+e.toString()); }
	}
int totalPages = (Integer.parseInt(listRecSize) + recPerPage -1)/recPerPage;
if (curPage > Integer.parseInt(listRecSize)) curPage = 1;                    // Out of range

System.out.println("totalPages :: "+totalPages);
System.out.println("curPage :: "+curPage);

PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
%>
<SCRIPT LANGUAGE="JavaScript">

    var cur_page     = <%=curPage%>;                            // Current display page
    var total_pages   = <%=totalPages%>;                         // The total number of records

    // Display previous page of user list
    function onPrev()
    {
	if (cur_page <= 1)  return false;
	cur_page = parseInt(cur_page) -1;
	document.form1.cur_page.value = cur_page;
       document.form1.PGaction.value="PREVORNEXT";
      document.form1.action = "customerReport.jsp";
	document.form1.submit();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Display next page of user list
    function onNext()
    {
    if (cur_page >= total_pages)  {
      return false;
    }else{
      cur_page = parseInt(cur_page) + 1;
      document.form1.cur_page.value = cur_page;
      document.form1.PGaction.value="PREVORNEXT";
      document.form1.action = "customerReport.jsp";
      document.form1.submit();
      }
    }
 </SCRIPT>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
              <%if(displaySummaryImport){ %>
              <button type="button" class="btn btn-default"
						onClick="window.location.href='importCustomerDiscountExcelSheet.jsp'">
						Import Customer Discount</button>
					&nbsp;
					<%} %>
				</div>
				<h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="customerReport.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
<INPUT type="hidden" name="listRecSize" value="<%=listRecSize%>">
   
   <center><strong><font color="red"><%=fieldDesc%></font></strong></center> 
   
  <%-- <center>
  
     <h1><small><font color="red"> <%=fieldDesc%> </font></small></h1>
   
  </center> --%>
  
  <div id="target" style="display:none">
  <div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
       <div class="col-sm-3 ac-box">
      	    <div class="input-group">
    		<INPUT class="ac-selected form-control" name="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"  MAXLENGTH=50>
   		 	<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+convertCharToString(form1.ITEM.value));">
   		 	<span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-3 ac-box">
        <div class="input-group">
        <INPUT class="ac-selected form-control" name="PRD_DESCRIP" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" size="30"  MAXLENGTH=100> 
    	<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('item_list_discount_summary.jsp?DESC='+convertCharToString(form1.PRD_DESCRIP.value));">

   		 <span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span>
    	</div> 
 		</div> 
 		</div>
 		</div> 
  		<div class="form-group">
       <label class="control-label col-sm-2" for="Customer Name">Customer Name/ID:</label>
       <div class="col-sm-3 ac-box">
      	    <div class="input-group">
      	    <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    		<INPUT class="ac-selected form-control" name="CUSTOMER" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" size="30"  MAXLENGTH=50>
   		 	<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('customer_list_issue_summary.jsp?CUSTOMER='+convertCharToString(form1.CUSTOMER.value));">
   		 	<span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Customer Type">Customer Type:</label>
        <div class="col-sm-3 ac-box">
        <div class="input-group">
        <INPUT class="ac-selected form-control" name="CUSTOMER_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(sCustomerTypeId)%>" size="30"  MAXLENGTH=100> 
    	<span class="btn-danger input-group-addon"  onClick="javascript:popUpWin('CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+convertCharToString(form1.CUSTOMER_TYPE_ID.value));">
   		 	<span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span>
    	</div> 
 		</div> 
 		</div>
 		</div>
 		
   <div class="form-group">
  	<div class="col-sm-offset-5 col-sm-4">   
  	<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
  	<%if(displaySummaryExport){ %>
  	<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();">Export All Data</button>&nbsp;
    <%} %>
  	<!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='javascript:history.back()'"><b>Back</b></button> -->
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
         <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
         <%if(displaySummaryExport){ %>
  	     <button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();">Export All Data</button>&nbsp;
  	     <%}%>
  	     <!-- <button type="button" class="Submit btn btn-default"  onClick="window.location.href='javascript:history.back()'"><b>Back</b></button> -->
  	</div>
        </div>
       	  </div>  
       	  
       	  
  <INPUT name="RECS_PER_PAGE" type = "hidden" value="<%=RECS_PER_PAGE%>" size="10"  MAXLENGTH=4>
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableMovementHistory_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableMovementHistory"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">Customer ID</th>
		                	<th style="font-size: smaller;">Customer Type Desc</th>
		                	<th style="font-size: smaller;">Customer Name</th>
		                	<th style="font-size: smaller;">Product ID</th>
		                	<th style="font-size: smaller;">Description</th>
		                	<th style="font-size: smaller;">Price</th>
		                	<th style="font-size: smaller;">Discount</th>
		                	<th style="font-size: smaller;">Price With Discount</th>
		                	
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
          int j=0;
          String rowColor="";
         
          Hashtable htship = new Hashtable();
          htship.put("PLANT",PLANT);
          Hashtable htexp = new Hashtable();
          custdao.setmLogger(mLogger);
          int total_stock_claim = 0;
              for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
            	 rowColor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#dddddd"; 
                j=j+1;
                Map lineArr = (Map) invQryList.get(iCnt);
                String trDate="";
                String discountType="";
                String discount=(String)lineArr.get("OBDISCOUNT");
                double UnitPrice =Double.parseDouble((String)lineArr.get("UnitPrice"));
                double discountedcost=0.00d;
                double converteddiscount=0.00d;
                double discount1=0.00d;
                double discountedper=0.00d;
                int plusIndex = discount.indexOf("%");
                if (plusIndex != -1) {
                	discount = discount.substring(0, plusIndex);
                	converteddiscount=Double.parseDouble(discount);
                	discountType = "BYPERCENTAGE";
                	discountedcost=(converteddiscount/100)*UnitPrice;
                	discountedper=UnitPrice-discountedcost;
                   }
                else{
                	 discount1 =Double.parseDouble(discount);
                }
              
          %>
          var rowData = [];
          rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("CUSTID"))%>';
          rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("CUSTTYPE")) %>';
          rowData[rowData.length] = '<%=StrUtils.forHTMLTag((String)lineArr.get("CUSTOMERNAME"))%>';
          rowData[rowData.length] = '<%= StrUtils.forHTMLTag((String)lineArr.get("ITEM"))%>';
          rowData[rowData.length] = '<%= StrUtils.forHTMLTag((String)lineArr.get("ITEMDESC"))%>';
          rowData[rowData.length] = '<%=  StrUtils.addZeroes(UnitPrice,numberOfDecimal)%>';
          rowData[rowData.length] = '<%= StrUtils.forHTMLTag((String)lineArr.get("OBDISCOUNT"))%>';
      	<%if(discountType.equalsIgnoreCase("BYPERCENTAGE")){ %>
          rowData[rowData.length] = '<%= StrUtils.addZeroes(discountedper,numberOfDecimal)%>';
          <%}else{ %>
          rowData[rowData.length] = '<%= StrUtils.addZeroes(discount1,numberOfDecimal)%>';
          <%} %>
          tableData[tableData.length] = rowData;
        <%}%>
                
    var groupColumn = 0;
    $(document).ready(function(){
   	 $('#tableMovementHistory').DataTable({
   		 	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
   		  	data: tableData,
   		  	//"columnDefs": [{"className": "t-right", "targets": [7]}],
   			"orderFixed": [ groupColumn, 'asc' ], 
   		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
   			"<'row'<'col-md-6'><'col-md-6'>>" +
   			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
   	        buttons: [
   	        	  /* {
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
   	            } , 
   	            {
   	            	extend: 'colvis',
                       columns: ':not(:eq('+groupColumn+')):not(:eq(2)):not(:eq(3))'
                   }
 */   	        ],
   		  });	 
    });
    
     </script>
   
		   <input type = "hidden" value= "Prev" onclick="return onPrev();" <%=isDisabled%> >&nbsp;
		   <input type = "hidden" value= "Next" onclick="return onNext();" <%=isDisabled%> >&nbsp;
           <input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >&nbsp;&nbsp;

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