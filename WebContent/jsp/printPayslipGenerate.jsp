
<!-- PAGE CREATED BY : IMTHI -->
<!-- DATE 09-05-2022 -->
<!-- DESC : New Bulk Payslip generate screen-->

<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Date"%>
<%@page import="java.text.DecimalFormat"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Bulk Payslip Processing";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
  <jsp:param value="<%=title%>" name="title"/>
  <jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
   <jsp:param name="submenu" value="<%=IConstants.PAYSLIP%>"/>
</jsp:include>
<script src="js/general.js"></script>
<script>
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function checkAll(isChk)
{
	var len = document.form1.chkdEmpNo.length;
	var orderLNo; 
	if(len == undefined) len = 1;  
    if (document.form1.chkdEmpNo)
    {
        for (var i = 0; i < len ; i++)
        {      
           	if(len == 1){
           		document.form1.chkdEmpNo.checked = isChk;
           	}else{
           		document.form1.chkdEmpNo[i].checked = isChk;
           	}
        }
    }
}
function onPrint(type){
	var checkFound = false;
	var len = document.form1.chkdEmpNo.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdEmpNo.checked)) {
			checkFound = false;
		}else if (len == 1 && document.form1.chkdEmpNo.checked) {
			checkFound = true;
		}else {
			if (document.form1.chkdEmpNo[i].checked) {
				checkFound = true;
			}
		}
	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
	var paymentMode = document.form1.payment_mode.value;
	var from_year = document.form1.from_year.value;
	var from_month = document.form1.from_month.value;
	if(paymentMode == ""){
		alert("Please choose payment mode.");
		document.form1.payment_mode.focus();
		return false;
	}
	if(from_month == ""){
		alert("Please choose from month.");
		document.form1.from_month.focus();
		return false;
	}
	if(from_year == ""){
		alert("Please choose from year.");
		document.form1.from_year.focus();
		return false;
	}
	if(type == "Preview"){	
	/* window.open(
			"/track/DynamicFileServlet?action=printEmployeePayslip&SendAs="+type,
			  '_blank' // <- This is what makes it open in a new window.
			); */
		$('[target]').attr('target', '_blank');
	} else {
		$('[target]').attr('target', '_self');
		//$('FORM[target="_blank"]').removeAttr('target');
	}
    document.form1.action="/track/DynamicFileServlet?action=printEmployeePayslip&SendAs="+type;
    document.form1.submit();
}
</script>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/calendar.js"></script>
<!-- <script src="js/jquery-1.4.2.js"></script> -->
<script src="../jsp/js/json2.js"></script>

<%

Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
ArrayList movQryList  = new ArrayList();
ArrayList movCustomerList  = new ArrayList();
int k=0;
String rowColor="";		
session= request.getSession();
String USERID ="",plant="";
String FROM_DATE ="",  TO_DATE = "", DIRTYPE ="",USER="",fdate="",
tdate="",PONO="",CUST_NAME="",PGaction="";
String newstatus="";
PGaction         = StrUtils.fString(request.getParameter("PGaction")).trim();
String fieldDesc = StrUtils.fString(request.getParameter("result"));
String html = "",cntRec ="false",allChecked = "";
plant = (String)session.getAttribute("PLANT");
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
String pcountry = StrUtils.fString((String) session.getAttribute("COUNTRY"));
if(pcountry.equalsIgnoreCase("United Arab Emirates"))
	pcountry="UAE";
boolean displaySummaryLink=false,displaySummaryPrintBatch=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
	displaySummaryLink = ub.isCheckValAcc("summarylnkpdfnocost", plant,LOGIN_USER);
	displaySummaryPrintBatch = ub.isCheckValAcc("printpobatch", plant,LOGIN_USER);
	displaySummaryLink=true;
	displaySummaryPrintBatch=true;
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
	displaySummaryLink = ub.isCheckValinv("summarylnkpdfnocost", plant,LOGIN_USER);
	displaySummaryPrintBatch = ub.isCheckValinv("printpobatch", plant,LOGIN_USER);
	displaySummaryLink=true;
	displaySummaryPrintBatch=true;
}
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
// String curDate =_dateUtils.getDate();
String curDate =DateUtils.getDateMinusDays();
String btnContainerDisabled="disabled";
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
 	FROM_DATE=curDate; 
if (FROM_DATE.length()>5)
fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);
if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
USER          = StrUtils.fString(request.getParameter("USER"));
PONO       = StrUtils.fString(request.getParameter("PONO"));
CUST_NAME      = StrUtils.fString(request.getParameter("CUST_NAME"));
allChecked = StrUtils.fString(request.getParameter("allChecked"));

Date d=new Date();  
int year=d.getYear();
year=year+1900;
Calendar c = Calendar.getInstance();
String mnth=c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
if (mnth.length() > 3) 
{
    mnth = mnth.substring(0, 3);
}
mnth=mnth.toUpperCase();
%>
<center>
	<h2>
		<small class="success-msg"><%=fieldDesc%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
            <ol class="breadcrumb" style="background-color: rgb(255, 255, 255);">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><label>Bulk Payslip Processing</label></li>                             
            </ol>             
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
             <div class="box-title pull-right">
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="" target="_blank">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">

		<div id="target" style="display:none" style="padding: 18px;">
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				 <input type="text" class="ac-selected form-control typeahead" id="CUST_NAME" value="<%=StrUtils.forHTMLTag(CUST_NAME)%>" placeholder="EMPLOYEE NAME OR ID" name="CUST_NAME" >				
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'CUST_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
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
      <a href="#" class="Hide" style="font-size: 15px">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-10">
  	</div>
        </div>
       	  </div>
       	  
       	  <div class="form-group">
       	  <label class="col-sm-2 required">Payment Mode</label>
       	  <div class="col-sm-2 ac-box">
       	  <input id="payment_mode" name="payment_mode" class="form-control" type="text" placeholder="Payment Mode"  value="CASH">
						 <span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_mode\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
       	  </div>
       	  <label class="col-sm-2 required">Payslip Month</label>
       	  <div class="col-sm-2 selfrommonth">
							<select name="from_month" class="form-control" id="frommonth" >
							    <option selected value=''>Select Payslip Month</option>
							    <option value='JAN'>January</option>
							    <option value='FEB'>February</option>
							    <option value='MAR'>March</option>
							    <option value='APR'>April</option>
							    <option value='MAY'>May</option>
							    <option value='JUN'>June</option>
							    <option value='JUL'>July</option>
							    <option value='AUG'>August</option>
							    <option value='SEP'>September</option>
							    <option value='OCT'>October</option>
							    <option value='NOV'>November</option>
							    <option value='DEC'>December</option>
   							</select> 
				  		</div>
				  		<label class="col-sm-2 required">Payslip Year</label>
				  		<div class="col-sm-2 selfromyear">
				  			<select class="form-control text-left pay_year" name="from_year" id="fromyear" >
								<option selected value=''>Select Payslip Year</option>
							</select>
				  		</div>
				  	</div>
       	  
    <INPUT type="Hidden" name="DIRTYPE" value="PO_PRINT">
     <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name = "select" value="select" 
  		<%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">&nbsp; Select/Unselect All &nbsp;</div>
  </div>
	
      <div id="VIEW_RESULT_HERE" class="table-responsive">
      <table id="tablePOList" class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                	<th style="font-size: smaller;">CHK</th>
		                	<th style="font-size: smaller;">S/N</th>
		                	<th style="font-size: smaller;">Id</th>
		                	<th style="font-size: smaller;">Name</th>
		                	<th style="font-size: smaller;">Designation</th>
		                	<th style="font-size: smaller;">Gender</th>
		                	<th style="font-size: smaller;">Phone</th>
		                	<th style="font-size: smaller;">Email</th>
		                	<%if(pcountry.equalsIgnoreCase("Singapore")){%>
		                	<th style="font-size: smaller;">NRIC/FIN</th>
		                	<% } else { %>
		                	<th style="font-size: smaller;"><%=pcountry%> ID Number</th>
		                	<% } %>
		                	<th style="font-size: smaller;">DOJ</th>
		                    </tr>
		            </thead>
				</table>
      </div>
  <div id="spinnerImg" ></div>
  <br>
  
  <%            
  		POUtil poUtil = new POUtil();
        Map ma = poUtil.getPOReceiptHdrDetails(plant);
   		  
    %>  
    
    <div class="form-group">
  	<div class="col-sm-12" align="center">
  	<button type="button" class="Submit btn btn-default"  value="PrintPreview"  name="action" onclick="javascript:return onPrint('Preview');" >Preview</button>
  	<button type="button" class="Submit btn btn-default"  value="SendMail"  name="actionmail" onclick="javascript:return onPrint('Mail');" >Send Email</button>
  	</div>
  	</div>
  	
    
  </FORM>
  </div>
  </div>
  </div>
 <script>

var tablePOList;
var CUSTOMERNAME;
function getParameters(){
	return { 
		CUSTOMERNAME:CUSTOMERNAME,ACTION: "GET_EMPLOYEE_FOR_SUMMARY",PLANT:"<%=plant%>",LOGIN_USER:"<%=USERID%>"
	}
}  
  function onGo(){
   var flag    = "false";
   CUSTOMERNAME        = document.form1.CUST_NAME.value;
// 	storeInLocalStorage('printPO_EMPNO', CUSTOMERNAME);
// 	storeInLocalStorage('printPO_ORDERNO', ORDERNO);
	
   var urlStr = "/track/MasterServlet";
   if (tablePOList){
	   tablePOList.ajax.url( urlStr ).load();
   }else{
	   tablePOList = $('#tablePOList').DataTable({
			"processing": true,
			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.CUSTOMERTYPELIST[0].EMPNO === 'undefined'){
		        		return [];
		        	}else {
		        		for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
		        			data.CUSTOMERTYPELIST[dataIndex]['SNO'] = dataIndex + 1;
		        			data.CUSTOMERTYPELIST[dataIndex]['CHKPO'] = '<INPUT Type=checkbox style=border: 0; name=chkdEmpNo value="'+data.CUSTOMERTYPELIST[dataIndex]['EMPNO']+'" >';
			        		}
		        		return data.CUSTOMERTYPELIST;
		        	}
		        }
		    },
	        "columns": [
	        	{"data": 'CHKPO', "orderable": false},
	        	{"data": 'SNO', "orderable": false},
	        	{"data": 'EMPNO', "orderable": false},
    			{"data": 'FNAME', "orderable": false},
    			{"data": 'DESGINATION', "orderable": false},
    			{"data": 'GENDER', "orderable": false},
    			{"data": 'TELNO', "orderable": false},
    			{"data": 'EMAIL', "orderable": false},
    			{"data": 'EMIRATESID', "orderable": false},
    			{"data": 'DATEOFJOINING', "orderable": false},
    		],
			"columnDefs": [{"className": "t-right", "targets": [6]}],
			"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
			"<'row'<'col-md-6'><'col-md-6'>>" +
			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
	        buttons: [
	        ],
			"drawCallback": function ( settings ) {
			}
		});
   }	   

}
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
	        $.each(data.CUSTOMERTYPELIST, function(i,item){
	        var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#dddddd";
	        outPutdata = outPutdata+item.INBOUNDDETAILSii++;
	        });
		}else{
		}
      outPutdata = outPutdata +'</TABLE>';
      document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
      document.getElementById('spinnerImg').innerHTML ='';
 }

function getTable(){
   return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
          '<TR BGCOLOR="#000066">'+
          '<TH><font color="#ffffff" align="left"><b>Chk</TH>'+
          '<TH><font color="#ffffff" align="center">S/N</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Id</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Name</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Designation</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Gender</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Phone</TH>'+
          '<TH><font color="#ffffff" align="left"><b>Email</TH>'+
          '<TH><font color="#ffffff" align="left"><b>NRIC/FIN</TH>'+
          '<TH><font color="#ffffff" align="center"><b>DOJ</TH>'+	     
          '</tr>';
}
 </SCRIPT>
 <script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();

    var start1 = new Date().getFullYear();
 	var start = parseFloat(start1)-parseFloat("10");
    var end = parseFloat(start1)+parseFloat("100");
    var options = "";
    for(var year = start ; year <=end; year++){
    	  $('.pay_year')
          .append($("<option></option>")
                     .attr("value", year)
                     .text(year)); 
    }
    var n_mnt='<%=mnth%>';
    var n_year='<%=year%>';
    $("select[name ='from_month']").val(n_mnt);
    $("select[name ='from_year']").val(n_year);
});
</script>
                  <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
// 	 getLocalStorageValue('printPO_EMPNO', '', 'CUST_NAME');
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
    var plant= '<%=plant%>';  
    
    /* Employee Type Auto Suggestion */
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
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
		});

 /* Payment Mode Auto Suggestion */
	$("#payment_mode").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{	  
		  display: 'PAYMENTTYPE',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					action : "GET_PAYMENT_TYPE_LIST",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.payTypes);
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
				return '<p>' + data.PAYMENTTYPE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentTypeModal"><a href="#"> + New Payment Mode</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();	  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	
 });

 function paymentTypeCallback(data)
 {
 	$("input[name ='payment_mode']").val(data.PAYMENTMODE);
 }
 </script>
   <%@include file="newPaymentTypeModal.jsp"%>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>