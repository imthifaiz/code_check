<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Sales Order Summary By Outlets (By Price)";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="submenu" value="<%=IConstants.SALES_REPORTS%>"/>
</jsp:include>
<style type="text/css">
.dt-button-collection.dropdown-menu{
left: 50px; !important;
}
</style>

<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/typeahead.jquery.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>


<script>
var subWin = null;

function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function ExportReport()
{
 
   //document.form1.action = "/track/ReportServlet?action=ExportIssuedHistory";
   //document.form1.submit();
  
}
function onGo(){


   var FROM_DATE      = document.form1.FROM_DATE.value;
   var TO_DATE        = document.form1.TO_DATE.value;
   var TERMINAL_NAME        = document.form1.TERMINAL_NAME.value;
   var OUTLET_NAME        = document.form1.OUTLET_NAME.value;
   var USER           = document.form1.CUSTOMER.value;
   var ORDERNO        = document.form1.ORDERNO.value;
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
  
   if(USER != null    && USER != "") { flag = true;}
   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
   
  

 //document.form1.action="../salesorder/orderhistorybyoutlet";
  //document.form1.submit();
}

</script>

<%
	Generator generator = new Generator();
	HTReportUtil movHisUtil = new HTReportUtil();
	PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
	ShipHisDAO _ShipHisDAO=new ShipHisDAO();
	ArrayList movQryList = new ArrayList();
	ArrayList prodGstList = new ArrayList();
	Hashtable ht = new Hashtable();	
	movHisUtil.setmLogger(mLogger);
	
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
        String userID = (String) session.getAttribute("LOGIN_USER");
	String FROM_DATE = "", TO_DATE = "", USER = "",DIRTYPE ="", 
			fdate = "", tdate = "", ORDERNO = "", cntRec = "false",OUTLET_NAME="",TERMINAL_NAME="",CUSTOMER = "",PGaction = "",statusID="";
			
	boolean displaySummaryExport=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displaySummaryExport = ub.isCheckValAcc("exportissuedobsummry", plant,LOGIN_USER);
		displaySummaryExport=true;
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displaySummaryExport = ub.isCheckValinv("exportissuedobsummry", plant,LOGIN_USER);
		displaySummaryExport=true;
	}
	float subtotal=0;
	double gst=0,total=0;
	float gsttotal=0;
	float grandtotal=0,gstpercentage=0,prodgstsubtotal1=0;
	int k=0;
	
	DecimalFormat decformat = new DecimalFormat("#,##0.00");

	FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
	String  fieldDesc="";

	if (FROM_DATE == null)
		FROM_DATE = "";
	else
		FROM_DATE = FROM_DATE.trim();
// 	String curDate = _dateUtils.getDate();
    String curDate = DateUtils.getDateMinusDays();//resvi
	if (FROM_DATE.length() < 0 || FROM_DATE == null
			|| FROM_DATE.equalsIgnoreCase(""))
		FROM_DATE = curDate;//resvi
	if (FROM_DATE.length() > 5)
		
		fdate = FROM_DATE.substring(6) + FROM_DATE.substring(3, 5) +  FROM_DATE.substring(0, 2);
		
	if (TO_DATE == null)
		TO_DATE = "";
	else
		TO_DATE = TO_DATE.trim();
	if (TO_DATE.length() > 5)
		
		 tdate = TO_DATE.substring(6) +  TO_DATE.substring(3, 5)+  TO_DATE.substring(0, 2);
		
	DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
	PGaction = StrUtils.fString(request.getParameter("PGaction")).trim();
	USER = StrUtils.fString(request.getParameter("USER"));
	ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
	if(DIRTYPE.length()<=0){
		DIRTYPE = "ISSUEDHISTORY";
		}
	
	if (PGaction.equalsIgnoreCase("View")) {
		
		try {
				//Hashtable ht = new Hashtable();	
			
					//if (StrUtils.fString(ORDERNO).length() > 0)    ht.put("a.DONO", ORDERNO);
			    
			} catch (Exception e) {
				  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
			}
	}	
		
%>
<center>
	<h2><small class="success-msg"> <%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>                
                <li><label>Sales Order Summary By Outlets (By Price)</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                                                           <div class="box-title pull-right">
                  <div class="btn-group" role="group">
                  <%if(displaySummaryExport){ %>
                  <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data </button>
					  <%}%>

					
				</div>

				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
             
              </h1>
               </div>
		</div>
<div class="box-body">

<FORM class="form-horizontal" name="form1" method="post" action="/track/ReportServlet?">
<input type="hidden" name="PGaction" value="View"> 
<INPUT type="Hidden" name="DIRTYPE" value="ISSUEDHISTORY">
<input type="hidden" name="CUSTOMERID" value="">
<input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
<input type="hidden" name="STATUS_ID" value="">
<input type="hidden" name="plant" value="<%=plant%>">
<input type="hidden" name="LOC_DESC" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">
<INPUT name="ACTIVE" type = "hidden" value="">
<input type="hidden" name="CUSTNO" value="">
<input type="hidden" name="OUTLET" value="">
<input type="hidden" name="TERMINAL" value="">

<div id="target" style="padding: 18px; display:none;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-2">
  		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
  		<INPUT name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" id="FROM_DATE" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
  		</div><div class="col-sm-2">
  		<INPUT class="form-control datepicker" name="TO_DATE" id="TO_DATE" type = "text" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="OUTLET_NAME" value="<%=StrUtils.forHTMLTag(OUTLET_NAME)%>" placeholder="OUTLETS" name="OUTLET_NAME" >				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'OUTLET_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
			</div>		
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="TERMINAL_NAME" name="TERMINAL_NAME" value="<%=StrUtils.forHTMLTag(TERMINAL_NAME)%>"  placeholder="TERMINAL">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'TERMINAL_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" placeholder="CUSTOMER" name="CUSTOMER" >				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
			</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
 			<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="ORDERNO" name="ORDERNO" value="<%=StrUtils.forHTMLTag(ORDERNO)%>" placeholder="ORDER NO" >
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>	
  		</div> 
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
				
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
				
			</div>
			
			
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
	
<div id="VIEW_RESULT_HERE" class="table-responsive">
  <div id="tableIssueSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableIssueSummary"
									class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
        
          <th style="font-size: smaller;">OUTLET</TH>
          <th style="font-size: smaller;">TERMINAL</TH>
          <th style="font-size: smaller;">ORDER NO</TH>
          <th style="font-size: smaller;">CUSTOMER</TH>
          <th style="font-size: smaller;">ORDER DATE</TH>
          <th style="font-size: smaller;">SUBTOTAL</TH>
          <th style="font-size: smaller;">TAX</TH>
          <th style="font-size: smaller;">TOTAL</TH>
          <th style="font-size: smaller;">ISSUED BY</TH>
        
                    </tr>
		            </thead>
				</table>
            		</div>
						</div>
					</div>
  </div>
  <script>
  var tableData = [];
	
     $(document).ready(function(){
    	 	 
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
    var plant= '<%=plant%>';
    
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
				PLANT : plant,
				ACTION : "getCustomerListData",
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
		    return '<p onclick="document.form1.CUSTNO.value =\''+data.CUSTNO+'\'">' + data.CNAME + '</p>';
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
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			
			
		});


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
    			PLANT : "<%=plant%>",
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
    			document.form.OUTCODE1.value = "";
    		}
    	});


/* TERMINAL*/
 $('#TERMINAL_NAME').typeahead({
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
    			PLANT : "<%=plant%>",
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
    		 return '<div onclick="setOutletTerminalData(\''+data.TERMINAL+'\',\''+data.TERMINAL_NAME+'\')"><p class="item-suggestion">Name: ' + data.TERMINAL_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.TERMINAL + '</p></div>';
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
    			document.form1.TERMINAL.value = "";
    		}
    	});
		
	
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
				PLANT : plant,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				CNAME : document.form1.CUSTOMER.value,
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
		}).on('typeahead:select',function(event,selection){
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.ORDERNO.value = "";
			}
		
		});
	
	
 });
 
  function setOutletData(OUTLET,OUTLET_NAME){
		$("input[name=OUTCODE]").val(OUTLET);
		$("input[name=OUTLET_NAME]").val(OUTLET_NAME);
	}
 function setOutletTerminalData(TERMINAL,TERMINAL_NAME){
		$("input[name=TERMINAL]").val(TERMINAL);
		$("input[name=TERMINAL_NAME]").val(TERMINAL_NAME);
	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>