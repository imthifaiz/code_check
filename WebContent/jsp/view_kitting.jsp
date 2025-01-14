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
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
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
   var pitem    = document.form1.ITEM.value;
   var pbatch   = document.form1.BATCH_0.value;
   var citem  = document.form1.CITEM.value;
   var cbatch  = document.form1.BATCH_1.value;
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
	document.form1.action="../kittingdekitting/summarydetails";
	document.form1.submit();
}

function showRow() {
      document.form1.ITEM.value="";
      document.form1.CITEM.value="";
      document.form1.BATCH_0.value="";
      document.form1.BATCH_1.value="";
	  document.getElementById("SORT").value='ISSUEDPRODUCT';
      var row = document.getElementById("HideRow");
      var rowData=document.getElementById("HideDatas");
	  row.style.display = '';
	  rowData.style.display = 'none';
	  //document.form1.submit();
 }

  function hideRow() {
	 document.form1.ITEM.value="";
     document.form1.CITEM.value="";
     document.form1.BATCH_0.value="";
     document.form1.BATCH_1.value="";
	 document.getElementById("SORT").value='AVAILABLEINV';
     var row = document.getElementById("HideRow");
     var rowData=document.getElementById("HideDatas");
    //if(row==null) return false;
     row.style.display = 'none';
     rowData.style.display = '';
     //document.form1.submit();
      
  }

  


</script>


<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
BOMUtil  bomUtil       = new BOMUtil();
bomUtil.setmLogger(mLogger);
ArrayList bomQryList  = new ArrayList();

String fieldDesc="";
String USERID ="",PLANT="",LOC ="",  ITEM = "", PRD_DESCRIP_PARENT="",BATCH_0="",CITEM="",BATCH_1="",PRD_DESCRIP="", QTY ="",kono="";
String html = "", FROM_DATE ="",  TO_DATE = "",fdate="",tdate="",status="",SORT="",pitem="",citem="",pbatch="",cbatch="",plant="";

boolean flag=false;

StrUtils _strUtils     = new StrUtils();
DateUtils _dateUtils = new DateUtils();
String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
if(PGaction.equalsIgnoreCase(""))
	PGaction = (String)(request.getAttribute("RESULT"));
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
pitem    = strUtils.fString(request.getParameter("ITEM"));
pbatch    = strUtils.fString(request.getParameter("BATCH_0"));
citem    = strUtils.fString(request.getParameter("CITEM"));
cbatch   = strUtils.fString(request.getParameter("BATCH_1"));
PRD_DESCRIP_PARENT = strUtils.fString(request.getParameter("PRD_DESCRIP_PARENT"));
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
LOC     = strUtils.fString(request.getParameter("LOC"));
status         = _strUtils.fString(request.getParameter("STATUS"));
kono         = _strUtils.fString(request.getParameter("KONO"));
SORT = _strUtils.fString(request.getParameter("SORT"));

FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
String curDate =_dateUtils.getDateMinusDays();
FROM_DATE=_dateUtils.getDateinddmmyyyy(curDate);
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
      if(strUtils.fString(pitem).length() > 0)        htKitting.put("A.PARENT_PRODUCT",pitem);
      if(strUtils.fString(pbatch).length() > 0)        htKitting.put("A.PARENT_PRODUCT_BATCH",pbatch);
      if(strUtils.fString(citem).length() > 0)        htKitting.put("A.CHILD_PRODUCT",citem);
      if(strUtils.fString(cbatch).length() > 0)        htKitting.put("A.CHILD_PRODUCT_BATCH",cbatch);
      if(strUtils.fString(kono).length() > 0)        htKitting.put("A.KONO",kono);
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
	<!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Kitting/De-Kitting Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
             <button type="button" class="Submit btn btn-default" 
					 onClick="javascript:ExportReport();">Export All Data</button>&nbsp;
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
<FORM class="form-horizontal" name="form1" method="post" action="../kittingdekitting/summarydetails">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
   
  <center><small><%=fieldDesc%></small></center>
  
    	  <div id="target" style="display:none"> 
  		<div class="" id="HideRow"> 
  		<%-- <% if(SORT.equals("ISSUEDPRODUCT")){%> --%> 
  		
    		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div>
  		<div class="col-sm-2">
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div>
  		<div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		
  		<div class="col-sm-4 ">
    		<input class="ac-selected form-control kittingno" type="TEXT" size="20" MAXLENGTH=100 name="KONO" placeholder="Kitting Dekitting No" value="<%=kono%>">
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'KONO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>   		 	
   		 </div>	  		
  		</div>
  		    	<%-- <%}else if(SORT.equals("AVAILABLEINV")){%> --%>
    	 
      
   <%-- <%}%> --%>   
    	</div> 
    	
    	<div class="" id="HideDatas">
   <div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div>
      <div class="col-sm-4 ">
    		<input class="ac-selected form-control kittingno" type="TEXT" size="20" MAXLENGTH=100 name="KONO" placeholder="Kitting Dekitting No" value="<%=kono%>">
    		<span class="select-icon" onclick="$(this).parent().find('input[name=\'KONO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>   		 	
   		 </div>	  		
  		</div>
  		</div>
    	
    	<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
        <div class="col-sm-4">
      	
      	<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    	<input type="TEXT"  name="ITEM" id="ITEM" value="<%=pitem%>" placeholder="Parent Product" size="20" MAXLENGTH=100  class="ac-selected  form-control typeahead">
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		
       <div class="col-sm-4">
       <input class="ac-selected  form-control typeahead" name="BATCH_0" id="BATCH_0" type="TEXT" value="<%=pbatch%>" placeholder="Parent Batch" size="45" MAXLENGTH=40> 
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'BATCH_0\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
    	</div>
 		</div>
 		
 		
 		
 		<div class="row" style="padding:3px">
						<div class="col-sm-2"></div>
        <div class="col-sm-4">
      	
    	<input type="TEXT"  name="CITEM" id="CITEM" value="<%=citem%>" placeholder="Child Product" size="20" MAXLENGTH=100 class="form-control">
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'CITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		
  		</div>
  		
        <div class="col-sm-4">        
        <input class="form-control" name="BATCH_1" id="BATCH_1" type="TEXT" value="<%=cbatch%>" placeholder="Child Batch" size="45" MAXLENGTH=40>   	 
        <span class="select-icon" onclick="$(this).parent().find('input[name=\'BATCH_1\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>   		 
    	</div>
 		</div>
 		
 		
 		<div class="form-group">
 		<div class="col-sm-offset-2 col-sm-4">
 		<label class="radio-inline"><INPUT name="SORT" id="SORT" type = "radio"  value="AVAILABLEINV"    onClick="hideRow();" <%if(SORT.equalsIgnoreCase("AVAILABLEINV")) {%>checked <%}%>><b>Available In Inventory</b></label>
 		<label class="radio-inline"><INPUT  name="SORT" id="SORT" type = "radio" value="ISSUEDPRODUCT"   onClick="showRow();"     <%if(SORT.equalsIgnoreCase("ISSUEDPRODUCT")) {%>checked <%}%>><b>Issued From Inventory</b></label>
 		</div>
 		
  		<div class="col-sm-4">
  		<button type="button" class="btn btn-success" value="View" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp;
  		</div>
  		
  		</div>
  	</div>
  	
  		
  		<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
     
       	  </div> 
       	      
     
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableIssueSummary"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
        
         <TH style="background-color: #ffffff; color:#333; text-align: center;">SNO</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">KONO</TH>
          <TH style="background-color: #ffffff; color:#333; text-align: center;">Parent Product ID</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Parent Product Desc</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Parent Product Detailed Desc</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Parent Batch</TH>
          <TH style="background-color: #ffffff; color:#333; text-align: center;">Parent Qty</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Child Product ID</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Chid Product Desc</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Child Product Detaild Desc</TH>
          <TH style="background-color: #ffffff; color:#333; text-align: center;">Child Batch</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Qty</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Loc</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Expiry Date</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Remarks/Reason Code</TH>
         <TH style="background-color: #ffffff; color:#333; text-align: center;">Status</TH>
                    </tr>
		            </thead>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
    var SORTPD= '<%=SORT%>';
    if(SORTPD=='ISSUEDPRODUCT')
    	 showRow();
    else
    	hideRow();
    var plant= '<%=PLANT%>'; 
	
	
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
				ACTION : "GET_BOM_PRODUCT_LIST_FOR_SUGGESTION",
				TYPE : "KITBOM",
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
				return '<p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' </p>';
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
			if($(this).val() != "")
			{
			$("#BATCH_0").typeahead('val', '"');
			$("#BATCH_0").typeahead('val', '');
			$("#CITEM").typeahead('val', '"');
			$("#CITEM").typeahead('val', '');
			}	
	});
    
    $('#CITEM').typeahead({
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
				ACTION : "GET_BOM_CHILD_PRODUCT_LIST_FOR_SUGGESTION",
				TYPE : "KITWITHBOM",
				ITEM : document.form1.ITEM.value,
				CITEM : query
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
				return '<p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' </p>';
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
			if($(this).val() != "")
				{
				$("#BATCH_1").typeahead('val', '"');
				$("#BATCH_1").typeahead('val', '');
				}	
		});
	
	$('#BATCH_0').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'BATCH',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_KITING_BATCH_DATA",
				ITEM : document.form1.ITEM.value,
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.batches);
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
				return '<div><p class="item-suggestion">'+data.BATCH+'</p><p class="item-suggestion pull-right">Quantity: '+data.QTY+'</p></div>';
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
	
	$('#BATCH_1').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'BATCH',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_KITING_CHILD_BATCHDETAILS_DATA",
				ITEM : document.form1.CITEM.value,
			
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.batches);
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
				return '<div><p class="item-suggestion">'+data.BATCH+'</p><p class="item-suggestion pull-right">Quantity: '+data.QTY+'</p></div>';
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
	/* Order Number Auto Suggestion */
	$('#KONO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'KONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../ProductionBomServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				KONO : query
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
		    return '<p>' + data.KONO + '</p>';
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
		}).on('typeahead:select',function(event,selection){
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.KONO.value = "";
			}
		
		});
   
});
</script>
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
	 rowData[rowData.length] = '<%=(String) lineArr.get("KONO")%>';
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
