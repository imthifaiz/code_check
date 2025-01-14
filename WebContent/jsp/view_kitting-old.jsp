<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Kitting/De-Kitting Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
 <jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
 <jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<style type="text/css">
.dt-button-collection.dropdown-menu{
left: 50px; !important;
}
</style>
<script language="javascript">

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'view_kitting', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

//---Modified by Deen on May 22 2014, Description:To open Kitting Summary  in excel powershell format
function ExportReport()
{
   var flag    = "false";
   var ITEM    = document.form1.ITEM.value;
   var PARENTBATCH   = document.form1.PARENTBATCH.value;
   var CHILDITEM  = document.form1.CHILDITEM.value;
   var CHILDBATCH  = document.form1.CHILDBATCH.value;
   document.form1.action="/track/ReportServlet?action=ExportExcelKittingSummary";
   document.form1.submit();

}
//---Modified by Deen on May 22 2014, Description:To open Kitting Summary  in excel powershell format


function onGo(){
	
	 /* if(document.form1.ITEM.value=="" || document.form1.ITEM.value.length==0 )
		 {
			alert("Please Enter Parent Product!");
			document.form1.ITEM.focus();
			return false;
	   }*/
	document.form1.action="view_kitting.jsp";
	document.form1.submit();
}

function showRow() {
      document.form1.ITEM.value="";
      document.form1.CHILDITEM.value="";
      document.form1.PARENTBATCH.value="";
      document.form1.CHILDBATCH.value="";
	  document.getElementById("SORT").value='ISSUEDPRODUCT';
      var row = document.getElementById("HideRow");
      //var rowData=document.getElementById("HideDatas");
	  row.style.display = '';
	  //rowData.style.display = 'none';
	  document.form1.submit();
 }

  function hideRow() {
	 document.form1.ITEM.value="";
     document.form1.CHILDITEM.value="";
     document.form1.PARENTBATCH.value="";
     document.form1.CHILDBATCH.value="";
	 document.getElementById("SORT").value='AVAILABLEINV';
     var row = document.getElementById("HideRow");
     //var rowData=document.getElementById("HideDatas");
    //if(row==null) return false;
     row.style.display = 'none';
     //rowData.style.display = 'none';
      
  }
  


</script>


<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
BOMUtil  bomUtil       = new BOMUtil();
bomUtil.setmLogger(mLogger);
ArrayList bomQryList  = new ArrayList();

String fieldDesc="";
String USERID ="",PLANT="",LOC ="",  ITEM = "", PRD_DESCRIP_PARENT="",PARENTBATCH="",CHILDITEM="",CHILDBATCH="",PRD_DESCRIP="", QTY ="";
String html = "", FROM_DATE ="",  TO_DATE = "",fdate="",tdate="",status="",SORT="";

boolean flag=false;

StrUtils _strUtils     = new StrUtils();
DateUtils _dateUtils = new DateUtils();
String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
ITEM    = strUtils.fString(request.getParameter("ITEM"));
PARENTBATCH    = strUtils.fString(request.getParameter("PARENTBATCH"));
CHILDITEM    = strUtils.fString(request.getParameter("CHILDITEM"));
CHILDBATCH   = strUtils.fString(request.getParameter("CHILDBATCH"));
PRD_DESCRIP_PARENT = strUtils.fString(request.getParameter("PRD_DESCRIP_PARENT"));
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
LOC     = strUtils.fString(request.getParameter("LOC"));
status         = _strUtils.fString(request.getParameter("STATUS"));
SORT = _strUtils.fString(request.getParameter("SORT"));

FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));

ItemMstUtil itemMstUtil = new ItemMstUtil();
itemMstUtil.setmLogger(mLogger);


	
	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	if (FROM_DATE.length()>5)
		fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	if (TO_DATE.length()>5)
		tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
	if(SORT.equals(""))
	{
		SORT="AVAILABLEINV";
	}
	
boolean cntRec=false;

if(PGaction.equalsIgnoreCase("View")){
 try{
	  //ITEM = itemMstUtil.isValidAlternateItemInItemmst( PLANT, ITEM);
      Hashtable htKitting = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)       htKitting .put("A.PLANT",PLANT);
      if(strUtils.fString(ITEM).length() > 0)        htKitting.put("A.PARENT_PRODUCT",ITEM);
      if(strUtils.fString(PARENTBATCH).length() > 0)        htKitting.put("A.PARENT_PRODUCT_BATCH",PARENTBATCH);
      if(strUtils.fString(CHILDITEM).length() > 0)        htKitting.put("A.CHILD_PRODUCT",CHILDITEM);
      if(strUtils.fString(CHILDBATCH).length() > 0)        htKitting.put("A.CHILD_PRODUCT_BATCH",CHILDBATCH);
      //if(_strUtils.fString(status).length() > 0)        htKitting.put("A.STATUS",status);
     
      bomQryList=bomUtil.getKittingSummary(htKitting,PLANT,fdate,tdate,SORT);
      if(bomQryList.size()>=0  )
      {
			 cntRec =false;
      }
      else if(bomQryList.isEmpty() || bomQryList.size() <= 0)
      {
    	     cntRec =true;
    	     bomQryList.clear();
			fieldDesc = "<font class = " + IDBConstants.FAILED_COLOR + "> No Data Found For Kitting </font>";
    	  
      }

 }catch(Exception e) { 
	 bomQryList.clear();
	 cntRec=true;
	
 }
}

%>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="window.location.href='importKitBomExcelSheet.jsp'">
						Import Kit BOM</button>
					&nbsp;
				</div>
				
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-default"
						onClick="window.location.href='Kitting_Dekitting.jsp'"
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						+ New</button>
					&nbsp;
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
<FORM class="form-horizontal" name="form1" method="post" action="view_kitting.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
   
  <center><small><%=fieldDesc%></small></center>
  
<%-- <center><h1><small><%=fieldDesc%></small></h1></center> --%>
  
  <div id="target" style="display:none"> 
  		<div class="form-group" id="HideRow"> 
  		<% if(SORT.equals("ISSUEDPRODUCT")){%>   	 
  		<label class="control-label col-sm-2" for="From Date">From Date:</label>
        <div class="col-sm-3">
        <div class="input-group" >          
        <INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY>
      	</div>
    	</div>
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="To Date">To Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY>
      	</div>
    	</div>
    	</div>
    	<%}else if(SORT.equals("AVAILABLEINV")){%>
    	 <INPUT name="FROM_DATE" type = "hidden" value="">
      <INPUT name="TO_DATE" type = "hidden" value="">
   <%}%>
    	</div>
    	
    	<div class="form-group">
        <label class="control-label col-sm-2" for="Product ID">Parent Prod ID:</label>
        <div class="col-sm-3">
      	<div class="input-group">
      	<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    	<INPUT class="form-control" name="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('item_list_kitting.jsp?ITEM='+form1.ITEM.value+'&FROM_DATE='+form1.FROM_DATE.value+'&TO_DATE='+form1.TO_DATE.value+'&SORT='+form1.SORT.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Parent Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Description">Parent Desc:</label>
        <div class="col-sm-3">
        <div class="input-group">
      	<INPUT class="form-control" name="DESC" id="DESC" type = "TEXT" value="" size="30"  MAXLENGTH=20>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('item_list_kitting.jsp?ITEM='+form1.ITEM.value+'&FROM_DATE='+form1.FROM_DATE.value+'&TO_DATE='+form1.TO_DATE.value+'&SORT='+form1.SORT.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Parent Desc Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
   		</div>
 		</div>
 		</div>
 		
 		<div class="form-group">
  		<label class="control-label col-sm-2" for="Prod Detail Desc">Prod Detail Desc:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" id="DETDESC" name="DETDESC" type = "TEXT" value="" size="15"  MAXLENGTH=100> 
    	</div> 
    	<div class="form-inline">
  		<label class="control-label col-sm-2" for="Prod Detail Desc">Parent Prod Batch:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="PARENTBATCH" type = "TEXT" value="<%=PARENTBATCH%>" size="30"  MAXLENGTH=100> 
    	<span class="input-group-addon"  onClick="javascript:popUpWin('parentbatch_list_kitting.jsp?ITEM='+form1.ITEM.value+'&FROM_DATE='+form1.FROM_DATE.value+'&TO_DATE='+form1.TO_DATE.value+'&PARENTBATCH='+form1.PARENTBATCH.value+'&SORT='+form1.SORT.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Parent Batch Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
    	</div> 
    	</div>
 		</div> 
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Child Product ID">Child Prod ID:</label>
        <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="CHILDITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(CHILDITEM)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('childitem_list_kitting.jsp?ITEM='+form1.ITEM.value+'&PARENBATCH='+form1.PARENTBATCH.value+'&CHILDPRODUCT='+form1.CHILDITEM.value+'&FROM_DATE='+form1.FROM_DATE.value+'&TO_DATE='+form1.TO_DATE.value+'&SORT='+form1.SORT.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Child Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Child Description">Child Desc:</label>
        <div class="col-sm-3">
        <div class="input-group">
      	<INPUT class="form-control"  name="CDESC" id="CDESC" type = "TEXT" value="" size="30"  MAXLENGTH=20>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('childitem_list_kitting.jsp?ITEM='+form1.ITEM.value+'&PARENBATCH='+form1.PARENTBATCH.value+'&CHILDPRODUCT='+form1.CHILDITEM.value+'&FROM_DATE='+form1.FROM_DATE.value+'&TO_DATE='+form1.TO_DATE.value+'&SORT='+form1.SORT.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Child Desc Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
   		</div>
 		</div>
 		</div>
 		
 		<div class="form-group">
  		<label class="control-label col-sm-2" for="Prod Detail Desc">Prod Detail Desc:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" id="CDETDESC" name="CDETDESC" type = "TEXT" value="" size="30"  MAXLENGTH=100> 
    	</div> 
    	<div class="form-inline">
  		<label class="control-label col-sm-2" for="Prod Detail Desc">Child Prod Batch:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control"   name="CHILDBATCH" type = "TEXT" value="<%=CHILDBATCH%>" size="30"  MAXLENGTH=100> 
    	<span class="input-group-addon"  onClick="javascript:popUpWin('childbatch_list_kitting.jsp?ITEM='+form1.ITEM.value+'&PARENTBATCH='+form1.PARENTBATCH.value+'&CHILDPRODUCT='+form1.CHILDITEM.value+'&CHILDBATCH='+form1.CHILDBATCH.value+'&FROM_DATE='+form1.FROM_DATE.value+'&TO_DATE='+form1.TO_DATE.value+'&SORT='+form1.SORT.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Child Batch Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
    	</div> 
    	</div>
 		</div>
 		
 		<div class="form-group">
 		<div class="col-sm-offset-2 col-sm-4">
 		<label class="radio-inline"><INPUT name="SORT" id="SORT" type = "radio"  value="AVAILABLEINV"    onClick="hideRow();" <%if(SORT.equalsIgnoreCase("AVAILABLEINV")) {%>checked <%}%>><b>Available In Inventory</b></label>
 		<label class="radio-inline"><INPUT  name="SORT" id="SORT" type = "radio" value="ISSUEDPRODUCT"   onClick="showRow();"     <%if(SORT.equalsIgnoreCase("ISSUEDPRODUCT")) {%>checked <%}%>><b>Issued From Inventory</b></label>
 		</div>
 		<div class="form-inline">
  		<div class="col-sm-offset-1 col-sm-4">  
  		<button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;
  		<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp;
  		<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
  	  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RK');}"> <b>Back</b></button>&nbsp;&nbsp; -->
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
  	    <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RK');}"> <b>Back</b></button> -->
  	</div>
        </div>
       	  </div>     
     
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableIssueSummary"
									class="table table-bordered table-hover dataTable no-footer "
									role="grid" aria-describedby="tableIssueSummary_info">
					<thead>
		                <tr role="row">
        
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">SNO</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Parent Product ID</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Parent Product Desc</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Parent Product Detailed Desc</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Parent Batch</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Parent Qty</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Child Product ID</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Chid Product Desc</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Child Product Detaild Desc</TH>
          <TH style="background-color: #eaeafa; color:#333; text-align: center;">Child Batch</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Qty</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Loc</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Expiry Date</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Remarks/Reason Code</TH>
         <TH style="background-color: #eaeafa; color:#333; text-align: center;">Status</TH>
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
         ItemMstDAO itemMstDAO = new ItemMstDAO();
         itemMstDAO.setmLogger(mLogger);
        InvMstDAO _InvMstDAO = new InvMstDAO();
       
         
		for (int iCnt = 0; iCnt < bomQryList.size(); iCnt++) {
		
			System.out.println(bomQryList.size());
			String  sDesc="",sdetdesc="";
			Map lineArr = (Map) bomQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#FFFFFF";
			String sItem =(String)lineArr.get("CHILD_PRODUCT");
			String scanitem =(String)lineArr.get("SCANITEM");
			if(scanitem.equalsIgnoreCase(sItem))
			{
				sDesc = strUtils.fString(itemMstDAO.getItemDesc(PLANT,sItem));
				sdetdesc = strUtils.fString(itemMstDAO.getItemDetailDesc(PLANT,sItem));
			}
			else
			{
				sItem = scanitem;
				sDesc = strUtils.fString(itemMstDAO.getItemDesc(PLANT,scanitem));
				sdetdesc = strUtils.fString(itemMstDAO.getItemDetailDesc(PLANT,scanitem));	
			}
			
			
			//get expirydate from inventory
				  String  expiredate= _InvMstDAO.getInvExpiryDatekitting(PLANT,(String) lineArr.get("CHILD_PRODUCT"),(String)lineArr.get("CHILD_PRODUCT_BATCH"));
			//get expirydat from inventory end
			
			//get parentdesc PRD_DESCRIP_PARENT
			String  parentdesc = itemMstDAO.getKittingParentItem(PLANT,(String) lineArr.get("PARENT_PRODUCT"));
			String pdetdesc = itemMstDAO.getItemDetailDesc(PLANT,(String) lineArr.get("PARENT_PRODUCT"));
			
			String remarks = (String) lineArr.get("REMARKS");
			String reasoncode = (String) lineArr.get("RSNCODE");
			
			if(reasoncode.length()>0)
			{
				remarks = remarks +","+reasoncode;
			}
						
	%>
	 var rowData = [];
	 rowData[rowData.length] = '<%=iIndex%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("PARENT_PRODUCT")%>';
     rowData[rowData.length] = '<%=parentdesc%>';
     rowData[rowData.length] = '<%=pdetdesc%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("PARENT_PRODUCT_BATCH")%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("PARENT_PRODUCT_QTY")%>';
     rowData[rowData.length] = '<%=sItem%>';
     rowData[rowData.length] = '<%=strUtils.replaceCharacters2Recv(sDesc)%>';
     rowData[rowData.length] = '<%=sdetdesc%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("CHILD_PRODUCT_BATCH")%>';
     rowData[rowData.length] = '<%=StrUtils.formatNum((String) lineArr.get("QTY"))%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("CHILD_PRODUCT_LOC")%>';
     rowData[rowData.length] = '<%=expiredate%>';
     rowData[rowData.length] = '<%=remarks%>';
     rowData[rowData.length] = '<%=(String) lineArr.get("STATUS")%>';
     tableData[tableData.length] = rowData;
     <%}%>  
	
     $(document).ready(function(){
       	 $('#tableIssueSummary').DataTable({
       		 	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
       		  	data: tableData,
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
                          extend: 'colvis'
                      }
       	        ],
       	     "createdRow": function(row, data, dataIndex){
		        	var parts = data[12].split("/");
		        	var dt = new Date(parseInt(parts[2], 10),
		        	                  parseInt(parts[1], 10) - 1,
		        	                  parseInt(parts[0], 10));
		        	if (dt.getTime() < new Date().getTime()){
		        		$(row).css('color', 'red');
		        	}
		        },  
       	     
       		  });	 
       });
          
 </script>
      
    
 <!--  Added by Deen on June 17 2014  -->
       <INPUT type="Hidden" name="USERID" value="<%=USERID%>">
     <!-- End  Added by Deen on June 17 2014  -->
     <INPUT     name="PRD_DESCRIP_PARENT"  type ="hidden" value="<%=PRD_DESCRIP_PARENT%>" size="1"   MAXLENGTH=80 >
    
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
</jsp:include>
