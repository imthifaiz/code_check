<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.util.DateUtils"%>
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<jsp:useBean id="ub" class="com.track.gates.userBean" />


<%@ include file="header.jsp"%>
<%
String title = "Consignment Order Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.CONSIGNMENT%>"/>
<jsp:param name="submenu" value="<%=IConstants.CONSIGNMENT_REPORTS%>"/>
</jsp:include>

<script src="../jsp/js/general.js"></script>

    <script>

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'List', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }

// function onView(){
	 
//      /* var flag    = "false";
//      var TONO    = document.form1.TONO.value;
//      if(TONO != null    && TONO != "") { flag = true;}
//      if(flag == "false"){ alert("Please Enter Order No"); return false;} */
//       document.form1.cmd.value="View" ;
//      document.form1.action="printTO.jsp";
//            document.form1.submit();
//   }

function checkAll(isChk)
{
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form1.chkdDoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkdDoNo.checked = isChk;
               	}
              	else{
              		document.form1.chkdDoNo[i].checked = isChk;
              	}
            	
        }
    }
}

function onRePrint(){

    /* var flag    = "false";
    var TONO    = document.form1.TONO.value;
    if(TONO != null    && TONO != "") { flag = true;}
    if(flag == "false"){ alert("Please Enter Order No"); return false;} */
	
	var checkFound = false;
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdDoNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdDoNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdDoNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
	form1.setAttribute("target", "_blank");
    document.form1.action="/track/DynamicFileServlet?action=printTOWITHBATCH";
    document.form1.submit();
}

function onRePrintWithOutBatch(){

    /* var flag    = "false";
    var TONO    = document.form1.TONO.value;
    if(TONO != null    && TONO != "") { flag = true;}
    if(flag == "false"){ alert("Please Enter Order No"); return false;} */
	
	var checkFound = false;
	var len = document.form1.chkdDoNo.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdDoNo.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdDoNo.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdDoNo[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
	form1.setAttribute("target", "_blank");
    document.form1.action="/track/DynamicFileServlet?action=printTOWITHOUTBATCH";
    document.form1.submit();
}

var postatus =   [{
    "year": "PARTIALLY ISSUED",
    "value": "PARTIALLY ISSUED",
    "tokens": [
      "PARTIALLY ISSUED"
    ]
  },
  {
	    "year": "ISSUED",
	    "value": "ISSUED",
	    "tokens": [
	      "ISSUED"
	    ]
	  }];
		  
var substringMatcher = function(strs) {
  return function findMatches(q, cb) {
    var matches, substringRegex;
    // an array that will be populated with substring matches
    matches = [];
    // regex used to determine if a string contains the substring `q`
    substrRegex = new RegExp(q, 'i');
    // iterate through the pool of strings and for any string that
    // contains the substring `q`, add it to the `matches` array
    $.each(strs, function(i, str) {
      if (substrRegex.test(str.value)) {
        matches.push(str);
      }
    });
    cb(matches);
  };
};
</script>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">

<script src="../jsp/js/calendar.js"></script>
 
  <% 
        ArrayList QryList  = new ArrayList();
        TOUtil toUtil = new TOUtil();
        DateUtils _dateUtils = new DateUtils();
        Map defMap = new HashMap();
        String PLANT        = (String)session.getAttribute("PLANT");
        String USER         = (String)session.getAttribute("LOGIN_USER");
        String ALLOWCATCH_ADVANCE_SEARCH = session.getAttribute("ALLOWCATCH_ADVANCE_SEARCH").toString();//azees
        String SETCURRENTDATE_ADVANCE_SEARCH = session.getAttribute("SETCURRENTDATE_ADVANCE_SEARCH").toString();//azees
     
        String cmd         = StrUtils.fString(request.getParameter("cmd")).trim();
     
        String TRANDT="",TONO="",result="",  chkString= "";
        String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ISSUESTATUS="",ORDERTYPE="",DIRTYPE ="",BATCH ="",fdate="",tdate="",JOBNO="",CUSTOMER="",PGaction="";
        String CUSTOMERID="",EMPNO="",CUSTOMERTYPE="",EMP_NAME="",issuests="";
        String html = "",cntRec ="false",allChecked = "";
        TONO    = StrUtils.fString(request.getParameter("TONO"));
        double pickQty = 0;String btnDisabled="disabled";
    
        FROM_DATE       = StrUtils.fString(request.getParameter("FROM_DATE"));
        TO_DATE         = StrUtils.fString(request.getParameter("TO_DATE"));
    
        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
//         String curDate =_dateUtils.getDate();
        String curDate =DateUtils.getDateMinusDays();
        if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1"))
        	curDate =DateUtils.getDate();
        String btnContainerDisabled="disabled";
        if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
        	FROM_DATE=curDate;

        if (FROM_DATE.length()>5)

        fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



        if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
        if (TO_DATE.length()>5)
        tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);



        DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
        JOBNO         = StrUtils.fString(request.getParameter("JOBNO"));
        USER          = StrUtils.fString(request.getParameter("USER"));
        TONO       = StrUtils.fString(request.getParameter("TONO"));
        CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
        CUSTOMERID      = StrUtils.fString(request.getParameter("CUSTOMERID"));
        PICKSTATUS    = StrUtils.fString(request.getParameter("PICKSTATUS"));
        issuests = PICKSTATUS; 
        ISSUESTATUS  = StrUtils.fString(request.getParameter("ISSUESTATUS"));
        
        EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
        CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
        allChecked = StrUtils.fString(request.getParameter("allChecked"));
 
       
     if(cmd.equalsIgnoreCase("View")){
        try{
        	Hashtable ht = new Hashtable();
            if(StrUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
            if(StrUtils.fString(TONO).length() > 0)      ht.put("B.PONO",TONO);
            if(StrUtils.fString(ORDERTYPE).length() > 0)    ht.put("A.ORDERTYPE",ORDERTYPE);
            if(StrUtils.fString(PICKSTATUS).length() > 0)   ht.put("A.STATUS",PICKSTATUS);
            if(StrUtils.fString(EMPNO).length() > 0) ht.put("A.EMPNO",EMPNO);
            
        	QryList=  toUtil.listTODetilstoPrintTo(ht,fdate,tdate,DIRTYPE,PLANT, CUSTOMER,CUSTOMERTYPE);
       //QryList=  toUtil.listTODetilstoPrint(PLANT,TONO);
        System.out.println(" QryList.size() : "+QryList.size());
        
        }catch(Exception e){
        System.out.println("Exception :: View : "+e.toString());
       result =  "<tr><td><font class=mainred><B><h4><centre>" + e.getMessage() + "!</centre></b></font><h4></td></tr>";
        }
        }      
  

%>
    <div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                       
                <li><label>Consignment Order Details</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
                   <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
              <i class="glyphicon glyphicon-remove"></i>            
              </h1>
              </div>
		</div>
<div class="box-body">
    <FORM class="form-horizontal" name="form1" method="post" action="printTO.jsp">
    <INPUT type="hidden" name="cmd" value="View"/>
    <input type="hidden" name="CUSTOMER_TYPE_DESC" value="">
  
  <input type = "hidden" name="EMPNO" value="<%=StrUtils.forHTMLTag(EMPNO)%>">
  
    <%-- <div class="form-group">
       <label class="control-label col-sm-4" for="Transfer Order No">Order Number:</label>
       <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" name="TONO" type = "TEXT" value="<%=TONO%>" size="20">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/to_order_list.jsp?TONO='+form1.TONO.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Transfer Order Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
   <div class="form-inline">
   <div class="col-sm-1">
   <button type="button" class="Submit btn btn-default" value="View" onClick="javascript:return onView();"><b>View</b></button>
   </div>
   </div>
   </div> --%>
   
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
				 <input type="text" class="ac-selected form-control typeahead" id="CUSTOMER" value="<%=StrUtils.forHTMLTag(CUSTOMER)%>" placeholder="Customer Name" name="CUSTOMER" >				
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="select-search btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/customer_list_issue_summary.jsp?CUSTOMER='+form1.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span>								 -->
				<!--<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('vendorlist.jsp?VENDNO='+form.vendname.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->	
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="">   		
  		<input type="text" class="ac-selected form-control" id="CUSTOMER_TYPE_ID" name="CUSTOMER_TYPE_ID" value="<%=StrUtils.forHTMLTag(CUSTOMERTYPE)%>"  placeholder="Customer Group">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/CustomerTypeList.jsp?CUSTOMER_TYPE_ID='+form1.CUSTOMER_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
				</div>
  		</div>
		<div class="col-sm-4 ac-box">
  		<input type="text" class="ac-selected form-control" id="TONO" name="TONO" value="<%=StrUtils.forHTMLTag(TONO)%>" placeholder="Order Number" >
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'TONO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/list/to_order_list.jsp?TONO='+form1.TONO.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">  		
  		<input type="text" class="ac-selected form-control" id="JOBNO" name="JOBNO" value="<%=StrUtils.forHTMLTag(JOBNO)%>" placeholder="Reference No">				
  		</div>
		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="ORDERTYPE" id="ORDERTYPE" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(ORDERTYPE)%>" placeholder="ORDER TYPE" >
  		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/displayOrderType.jsp?OTYPE=CONSIGNMENT&ORDERTYPE='+form1.ORDERTYPE.value);"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>

<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
    		<div class="col-sm-4 ac-box">
  
 <input type="text" name="PICKSTATUS" id="PICKSTATUS" class="ac-selected form-control" value="<%=StrUtils.forHTMLTag(issuests)%>" placeholder="Status" >
  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PICKSTATUS\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>	
  		<div class="col-sm-4 ac-box">
  		<div class=""> 
  		<input type="text" name="EMP_NAME" id="EMP_NAME" class="ac-selected form-control status" value="<%=StrUtils.forHTMLTag(EMP_NAME)%>" placeholder="Employee" >
  		<span class="select-icon"  onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!--   		<span class="select-search btn-danger input-group-addon " onClick="javascript:popUpWin('../jsp/employee_list.jsp?EMP_NAME='+form1.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form1');"><span class="glyphicon glyphicon-search" aria-hidden="true" style="left: -4px;"></span></span> -->
  		</div>
  		</div>
  		</div>
  		

  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
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
      <div class="col-sm-offset-10">
      </div>
        </div>
       	  </div>
       	  
       	  <INPUT type="Hidden" name="DIRTYPE" value="OB_PRINT">
         
  	<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
                       &nbsp; <strong>Select/Unselect All </strong>&nbsp;</div>
  </div>
   
   <div id="VIEW_RESULT_HERE" class="table-responsive">              
              	<table id="tablePOList" class="table table-bordred table-striped">
					<thead>
		                <tr role="row">
		                <th style="font-size: smaller;">Chk</th>
  						<th style="font-size: smaller;">S/N</th>
		                <th style="font-size: smaller;">Order No</th>
		                <th style="font-size: smaller;">Order Type</th>
		                <th style="font-size: smaller;">Ref. No.</th>
					    <th style="font-size: smaller;">Customer Name</th>
					    <th style="font-size: smaller;">Order Quantity</th>
					    <th style="font-size: smaller;">Issue Quantity</th>
					    <th style="font-size: smaller;">Employee</th>
					    <th style="font-size: smaller;">Status</th>
    </tr>
		           
		        </thead>
				</table>
    </div>
  
  
   
<script>
var tablePOList;
var FROM_DATE, TO_DATE, DIRTYPE, USER, ORDERNO, JOBNO, PICKSTATUS, ORDERTYPE, STATUS_ID, EMPNO, CUSTOMERTYPE;
function getParameters(){
	return {
		
			FDATE:FROM_DATE,TDATE:TO_DATE,DTYPE:DIRTYPE,CUSTOMER:USER,ORDERNO:ORDERNO,JOBNO:JOBNO,PICKSTATUS:PICKSTATUS,ORDERTYPE:ORDERTYPE,TYPE:"PICKISSUE",EMPNO:EMPNO,CUSTOMERTYPE:CUSTOMERTYPE,Submit: "VIEW_CONSIGNMENT_DETAILS_PRINT",
			PLANT:"<%=PLANT%>",LOGIN_USER:"<%=USER%>"
		
	}
}

function storeUserPreferences(){
	   //storeInLocalStorage('printTO_FROMDATE', $('#FROM_DATE').val());
		//storeInLocalStorage('printTO_TODATE', $('#TO_DATE').val());
		storeInLocalStorage('printTO_CUSTOMER', $('#CUSTOMER').val());
		storeInLocalStorage('printTO_CUSTOMER_TYPE_ID', $('#CUSTOMER_TYPE_ID').val());
		storeInLocalStorage('printTO_TONO', $('#TONO').val());
		storeInLocalStorage('printTO_JOBNO', $('#JOBNO').val());
		storeInLocalStorage('printTO_ORDERTYPE', $('#ORDERTYPE').val());
		storeInLocalStorage('printTO_PICKSTATUS',$('#PICKSTATUS').val());
		storeInLocalStorage('printTO_EMP_NAME',$('#EMP_NAME').val());
}

 function onGo(){
//debugger;
   
  var flag    = "false";

   FROM_DATE      = document.form1.FROM_DATE.value;
   TO_DATE        = document.form1.TO_DATE.value;
   DIRTYPE        = document.form1.DIRTYPE.value;
   USER           = document.form1.CUSTOMER.value;
   ORDERNO        = document.form1.TONO.value;
   JOBNO          = document.form1.JOBNO.value;
   PICKSTATUS     = document.form1.PICKSTATUS.value;
 //imti start
   if(PICKSTATUS=="PARTIALLY ISSUED")
   	PICKSTATUS="O";
   else if(PICKSTATUS=="ISSUED")
   	PICKSTATUS="C";
   //imti end
   ORDERTYPE      = document.form1.ORDERTYPE.value; 
//    STATUS_ID      = document.form1.STATUS_ID.value; 
   EMPNO          = document.form1.EMP_NAME.value;
   CUSTOMERTYPE   = document.form1.CUSTOMER_TYPE_ID.value;
   
  if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
  if(TO_DATE != null    && TO_DATE != "") { flag = true;}
  if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

  if(USER != null    && USER != "") { flag = true;}
   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
   if(JOBNO != null     && JOBNO != "") { flag = true;}
   var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
   if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
   storeUserPreferences();
   }
   var urlStr = "/track/TransferOrderServlet";
   if (tablePOList){
	   tablePOList.ajax.url( urlStr ).load();
   }else{
	   tablePOList = $('#tablePOList').DataTable({
			"processing": true,
			"lengthMenu": [[50, 100, 500], [50, 100, 500]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
			"ajax": {
				"type": "GET",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].tono === 'undefined'){
		        		return [];
		        	}else {
		        		for(var dataIndex = 0; dataIndex < data.items.length; dataIndex ++){
		        			data.items[dataIndex]['SNO'] = dataIndex + 1;
		        			data.items[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.items[dataIndex]['tono']+'" >';
		        			var lnstat = data.items[dataIndex]['status']
		        			if(lnstat=="O")
		        		    	   lnstat="PARTIALLY ISSUED";
		        		       else if(lnstat=="C")
		        		    	   lnstat="ISSUED";
		        			 data.items[dataIndex]['status'] = lnstat;
		        			}
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
	        	{"data": 'CHKOB', "orderable": false},
	        	{"data": 'SNO', "orderable": false},
	        	{"data": 'tono', "orderable": false},
    			{"data": 'ordertype', "orderable": false},
    			{"data": 'jobnum', "orderable": false},
    			{"data": 'custname', "orderable": false},
    			{"data": 'qtyor', "orderable": false},
    			{"data": 'qtypick', "orderable": false},
    			{"data": 'empname', "orderable": false},
    			{"data": 'status', "orderable": false},
    			],
			"columnDefs": [{"className": "t-right", "targets": [6, 7, 8]}],
			/*"orderFixed": [ groupColumn, 'asc' ], 
			/*"dom": 'lBfrtip',*/
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
			
	        $.each(data.items, function(i,item){
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#dddddd";
                    
	        	outPutdata = outPutdata+item.OUTBOUNDDETAILS
                     	ii++;
	            
	          });
		}else{
		//	outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="6"><BR><CENTER><B><FONT COLOR="RED">No details found!</FONT></B></CENTER></TD></TR>';
		}
     outPutdata = outPutdata +'</TABLE>';
     document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
      document.getElementById('spinnerImg').innerHTML ='';

  
}

 function getvendname(VENDO){
		
	}

function getTable(){
  return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
         '<TR BGCOLOR="#000066">'+
         '<TH><font color="#ffffff" align="left"><b>Chk</TH>'+
         '<TH><font color="#ffffff" align="center">S/N</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Order No</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Order Type</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Ref No</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Cust Name</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Order Qty</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Issue Qty</TH>'+
         '<TH><font color="#ffffff" align="left"><b>Employee</TH>'+ 
        '<TH><font color="#ffffff" align="left"><b>Status</TH>'+ 
         '</tr>';
             
}
 

            
  </script> 


    
<%--   <script>
  
  var tableData = []; 
  
   <%

          for (int iCnt =0; iCnt<QryList.size(); iCnt++){
          Map lineArr = (Map) QryList.get(iCnt);
          int iIndex = iCnt + 1;
          String pickedQty = (String)lineArr.get("qtypick");
          pickQty = pickQty + Double.parseDouble(pickedQty);
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
             %>
             var lnstat = '<%=(String)lineArr.get("status")%>';
             if(lnstat=="O")
		    	   lnstat="PARTIALLY ISSUED";
             else if(lnstat=="N")
		    	   lnstat="OPEN";
		       else if(lnstat=="C")
		    	   lnstat="ISSUED";
             
             var rowData = [];
             rowData[rowData.length] = '<input type="checkbox" style="border: 0;" name="chkdDoNo" value=<%=(String)lineArr.get("dono")%> >';
             rowData[rowData.length] = '<%=iIndex%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("dono")%>';             
             rowData[rowData.length] = '<%=(String)lineArr.get("ordertype")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("jobnum")%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("custname")%>';
             rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("qtyor"))%>';
             rowData[rowData.length] = '<%=StrUtils.formatNum((String)lineArr.get("qtypick"))%>';
             rowData[rowData.length] = '<%=(String)lineArr.get("empname")%>';
             rowData[rowData.length] = lnstat;
             tableData[tableData.length] = rowData ; 
             <%
  		}
  	%> 
  	<%  if (pickQty>0){btnDisabled="";}%>
  	
  	$(document).ready(function(){
     	 $('#tableIssueSummary').DataTable({
     		
     		 "lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
     		  	data: tableData,
     		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
     			"<'row'<'col-md-6'><'col-md-6'>>" +
     			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
     	         buttons: [
     	        	/* {
     	                extend: 'collection',
     	                text: 'Export',
     	                   buttons:  [
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
                     
                    } */
     	        ] , 
     		  });	 
     });
            
  	$('#tableIssueSummary').on('column-visibility.dt', function(e, settings, column, state ){
  		if (!state){
  			groupRowColSpan = parseInt(groupRowColSpan) - 1;
  		}else{
  			groupRowColSpan = parseInt(groupRowColSpan) + 1;
  		}
  		$('#tableInventorySummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan);
  		$('#tableInventorySummary').attr('width', '100%');
  	});    
             
             
       <TR bgcolor = <%=bgcolor%>>

                  <TD align="left"> <%=(String)lineArr.get("TOLNNO")%></TD>
                  <TD align="left"><%=(String)lineArr.get("ITEM")%></TD>
                  <TD align="left"><%=(String)lineArr.get("ITEMDESC")%></TD>
                  <TD align="right"><%=StrUtils.formatNum((String)lineArr.get("QTYOR"))%></TD>
                  <TD align="right"><%=StrUtils.formatNum((String)lineArr.get("qtypick"))%></TD>
                  <TD align="left"><%=(String)lineArr.get("UNITMO")%></TD>
                
        </TR>
        
      <% } if (pickQty>0){btnDisabled="";}%>  
       
        </script> --%>

   
     <b>
       <%= result%>
      </b>
      
      
<div class="form-group">
  	<div class="col-sm-12" align="center">   
  	<button type="button" class="Submit btn btn-default"  value="PrintWithBatch/Sno"  name="action" onclick="javascript:return onRePrint();" >PrintWithBatch/Sno</button>
  	<button type="button" class="Submit btn btn-default"  value="PrintWithOutBatch/Sno"  name="action" onclick="javascript:return onRePrintWithOutBatch();" >PrintWithOutBatch/Sno</button>
  	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
  	<button type="button" class="Submit btn btn-default" onClick="{backNavigation('reports.jsp','RT');}"> <b>Back</b></button>&nbsp;&nbsp; -->
  	</div>
  	</div> 
      
  </FORM>
  </div>
  </div>
  </div>
          
 <script>
$(document).ready(function(){
	var ALLOWCATCH_ADVANCE_SEARCH = '<%=ALLOWCATCH_ADVANCE_SEARCH%>';
    if(ALLOWCATCH_ADVANCE_SEARCH==='1'){
	if (document.form1.FROM_DATE.value == ''){
		   getLocalStorageValue('printTO_FROMDATE', '','FROM_DATE');
	 }
	   if (document.form1.TO_DATE.value == ''){
		   getLocalStorageValue('printTO_TODATE', '','TO_DATE');
		} 
	   if (document.form1.CUSTOMER.value == ''){
			getLocalStorageValue('printTO_CUSTOMER', '','CUSTOMER');
		} 	
	   if (document.form1.CUSTOMER_TYPE_ID.value == ''){
			getLocalStorageValue('printTO_CUSTOMER_TYPE_ID', '','CUSTOMER_TYPE_ID');
		} 
	   if (document.form1.TONO.value == ''){
		   getLocalStorageValue('printTO_TONO','','TONO');
		}  
	   if (document.form1.JOBNO.value == ''){
		   getLocalStorageValue('printTO_JOBNO','', 'JOBNO');
		} 
	   if (document.form1.ORDERTYPE.value == ''){
			getLocalStorageValue('printTO_ORDERTYPE','','ORDERTYPE');
		}
	   if (document.form1.PICKSTATUS.value == ''){
			getLocalStorageValue('printTO_PICKSTATUS','','PICKSTATUS');
		}
	   if (document.form1.EMP_NAME.value == ''){
		   getLocalStorageValue('printTO_EMP_NAME','','EMP_NAME');
		}
    }

	 onGo();
    $('[data-toggle="tooltip"]').tooltip();
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
    var plant= '<%=PLANT%>';

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
		    return '<p onclick="getvendname(\''+data.CUSTNO+'\')">' + data.CNAME + '</p>';
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
    
	/* Order Number Auto Suggestion */
	$('#TONO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TONO',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/TransferOrderServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						Submit : "GET_ORDER_NUM_FOR_AUTO_SUGGESTION",
						CNAME : document.form1.CUSTOMER.value,
						TONO : query
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
		    return '<p>' + data.TONO + '</p>';
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
				document.form1.ORDERNO.value = "";
			}
			/* To reset Order number Autosuggestion*/
			
		});
	
	/* Customer Type Auto Suggestion */
	$('#CUSTOMER_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CUSTOMER_TYPE_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "getCustomerListTypeData",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUST_TYPE_MST);
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
		    return '<div><p class="item-suggestion">'+data.CUSTOMER_TYPE_ID+'</p></div>';
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
	
	/* Employee Type Auto Suggestion */
	$('#EMP_NAME').typeahead({
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
		    return '<div  onclick="document.form1.EMPNO.value = \''+data.EMPNO+'\'"><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">'+data.FNAME+'</p></div>';
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
    
/* Order Type Auto Suggestion */
$('#ORDERTYPE').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'ORDERTYPE',  
	  source: function (query, process,asyncProcess) {
		  	var urlStr = "../MasterServlet";
			$.ajax({
				type : "POST",
				url : urlStr,
				async : true,
				data : {				
					ACTION : "GET_ORDERTYPE_DATA",
					Type : "CONSIGNMENT",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.ORDERTYPES);
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
	    return '<p>' + data.ORDERTYPE + '</p>';
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
			document.form1.ORDERTYPE.value = "";
		}
	
	});

$("#PICKSTATUS").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'postatus',
	  display: 'value',  
	  source: substringMatcher(postatus),
	  limit: 9999,
	  templates: {
	  empty: [
		  '<div style="padding:3px 20px">',
			'No results found',
		  '</div>',
		].join('\n'),
		suggestion: function(data) {
		return '<p>' + data.value + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	});
});
</script>
   
  <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

      