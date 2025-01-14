<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<!-- resvi -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- resvi -->
<%
String title = "Generate Location Barcode";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="javascript">
	var subWin = null;
  	function popUpWin(URL) {
    	subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  	}
 	function onGo()
	{ 
 		document.form1.action ="../inhouse/genlocationbarcode"
     document.form1.submit();
	} 	
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
 	              		chkstring = document.form1.chkdDoNo.value;
 	 					chkdvalue = chkstring.split(',');
 	 					orderLNo = chkdvalue[0];
 	               	}
 	              	else{
 	              		document.form1.chkdDoNo[i].checked = isChk;
 	              		chkstring = document.form1.chkdDoNo[i].value;
 	 					chkdvalue = chkstring.split(',');
 	 					orderLNo = chkdvalue[0];
 	              	}
 	              	if(document.form1.singlePrint.checked)
 					{
 					document.getElementById("PrintQty_"+orderLNo).value = 1;
 					}
 	              	else
 	              		document.getElementById("PrintQty_"+orderLNo).value = "";
 	              		
 	        }
 	    }
 	}
 	function onRePrint(type){
 		var checkFound = false;
 		var Traveler ;
 		var concatTraveler="";
 		var j=0;
 		var orderLNo;
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
 					chkstring = document.form1.chkdDoNo.value;
 					chkdvalue = chkstring.split(',');
 					orderLNo = chkdvalue[0];
 					if(!verifyPrintQty(orderLNo))	
 				    	  return false;
 			}

 			else {
 				if (document.form1.chkdDoNo[i].checked) {
 					checkFound = true;
 					chkstring = document.form1.chkdDoNo[i].value;
 					chkdvalue = chkstring.split(',');
 					orderLNo = chkdvalue[0];
 					if(!verifyPrintQty(orderLNo))	
 				    	  return false;
 				}
 			}

 		}
 		if (checkFound != true) {
 			alert("Please check at least one checkbox.");
 			return false;
 		}
 		for ( var i = 0; i < len; i++) {

 			if (len == 1 && document.form1.chkdDoNo.checked) {

 				chkstring = document.form1.chkdDoNo.value;
					chkdvalue = chkstring.split(',');
					orderLNo = chkdvalue[0];
	                Traveler=document.form1.chkdDoNo.value +","+ document.getElementById("PrintQty_"+orderLNo).value;
	                concatTraveler=concatTraveler+Traveler+"=";
 			
 			} else if (document.form1.chkdDoNo[i].checked) {

 					j=j+1;
 					chkstring = document.form1.chkdDoNo[i].value;
 					chkdvalue = chkstring.split(',');
 					orderLNo = chkdvalue[0];
 	                Traveler=document.form1.chkdDoNo[i].value +","+ document.getElementById("PrintQty_"+orderLNo).value;
 	                concatTraveler=concatTraveler+Traveler+"=";    							
 				}
 				document.form1.TRAVELER.value=concatTraveler;		
 			
 			}
 		document.form1.action="/track/inhouse/printbarcode?PAGE_TYPE=LOCATION&PRINT_TYPE="+type;
 	    //document.form1.action="/track/DynamicFileServlet?action=printLocBarcode&Submit="+type;
 	    document.form1.submit();
 	    //onGo();
 	}
 	function verifyPrintQty(orderLNo)
 	{

 			var recvQty = document.getElementById("PrintQty_"+(orderLNo)).value;
 			if (recvQty == "" || recvQty.length == 0 || recvQty == '0') {
 				alert("Please enter valid print quantity!");
 				document.getElementById("PrintQty_"+(orderLNo)).focus();
 				document.getElementById("PrintQty_"+(orderLNo)).select();
 		        return false;
 			}
 			if(!isNumericInput(recvQty)){
 				alert("Please enter valid print quantity!");
 				document.getElementById("PrintQty_"+(orderLNo)).focus();
 				document.getElementById("PrintQty_"+(orderLNo)).select();
 		         return false;
 			}
 			return true;
 	}
 	
 	function isNumericInput(strString) {
 		var strValidChars = "0123456789.-";
 		var strChar;
 		var blnResult = true;
 		if (strString.length == 0)
 			return false;
 		//  test strString consists of valid characters listed above
 		for (var i = 0; i < strString.length && blnResult == true; i++) {
 			strChar = strString.charAt(i);
 			if (strValidChars.indexOf(strChar) == -1) {
 				blnResult = false;
 			}
 		}
 		return blnResult;
 	}

 	function singlePrinting(isChk)
	{
		var len = document.form1.chkdDoNo.length;
		 var orderLNo; 
		 if(len == undefined) len = 1;  
	    if (document.form1.chkdDoNo)
	    {
	        for (var i = 0; i < len ; i++)
	        {      
	              	if(len == 1 && document.form1.chkdDoNo.checked){
	              		chkstring = document.form1.chkdDoNo.value;
	              		chkdvalue = chkstring.split(',');
	 					orderLNo = chkdvalue[0];
	 					if(document.form1.singlePrint.checked)
	 					{
	 					document.getElementById("PrintQty_"+orderLNo).value = 1;
	 					}
	 					else
	 	              		document.getElementById("PrintQty_"+orderLNo).value = "";
	              	}
	              	else if(len == 1 && !document.form1.chkdDoNo.checked){
	              		return;
	              	}
	              	else{
	                  	if(document.form1.chkdDoNo[i].checked){
	                  		chkstring = document.form1.chkdDoNo[i].value;
	                  		chkdvalue = chkstring.split(',');
		 					orderLNo = chkdvalue[0];
	              		if(document.form1.singlePrint.checked)
	 					{
	 					document.getElementById("PrintQty_"+orderLNo).value = 1;
	 					}
	              		else
	 	              		document.getElementById("PrintQty_"+orderLNo).value = "";
	                  	}
	              	}
	            	 
	        }
	    }
	}

 	
</script>

<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	ArrayList locQryList  = new ArrayList();
	
	_userBean.setmLogger(mLogger);
	String fieldDesc="";
	String PLANT="",LOC_ID ="",LOC_TYPE_ID="",LOC_TYPE_ID2="",allChecked = "",singlePrint="";
	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	session=request.getSession();
	String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	LOC_ID     = strUtils.fString(request.getParameter("LOC_ID"));
	LOC_TYPE_ID     = strUtils.fString(request.getParameter("LOC_TP_ID"));
	LOC_TYPE_ID2    = strUtils.fString(request.getParameter("LOC_TP_ID2"));
	//fieldDesc=StrUtils.fString(request.getParameter("result"));
	fieldDesc = (String)request.getAttribute("Msg");
	/* resvi */
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils _dateUtils = new DateUtils();
	String collectionDate=_dateUtils.getDate();
	ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	/* resvi */
	
	boolean displaySummaryImport=false,displaySummaryLink=false,displaySummaryEdit=false,displaySummaryNew=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
	displaySummaryLink = ub.isCheckValAcc("summarylnkloc", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValAcc("editloc", PLANT,USERID);
	displaySummaryNew = ub.isCheckValAcc("newloc", PLANT,USERID);
	displaySummaryImport = ub.isCheckValAcc("importloc", PLANT,USERID);

	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
	displaySummaryLink = ub.isCheckValinv("summarylnkloc", PLANT,USERID);
	displaySummaryEdit = ub.isCheckValinv("editloc", PLANT,USERID);
	displaySummaryNew = ub.isCheckValinv("newloc", PLANT,USERID);
	displaySummaryImport = ub.isCheckValinv("importloc", PLANT,USERID);
	}
	
 	LocUtil _LocUtil = new LocUtil();
 	_LocUtil.setmLogger(mLogger);
	if(PGaction.equalsIgnoreCase("View")){
 	try{
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
      ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
      ht.put("LOC_TYPE_ID2",LOC_TYPE_ID2);
      
         locQryList=      _LocUtil.getLocDetails(LOC_ID,PLANT," ",ht);
      if(locQryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }
  
 	}catch(Exception e) {mLogger.exception(true,
			"ERROR IN JSP PAGE - locSummary.jsp ", e); }
	}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>
                <li><a href="../inhouse/genbarcode"><span class="underline-on-hover">Generate Barcode</span></a></li>	
                <li><label>Generate Location Barcode</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 --> 
         <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
				
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../inhouse/genbarcode'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>   
		
 <div class="box-body">
 

<FORM class="form-horizontal" name="form1" method="post" action="../inhouse/genlocationbarcode">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<INPUT type = "hidden" name="LOC_ID1" value ="">
<INPUT type = "hidden" name="LOC_DESC" value ="">
<INPUT type = "hidden" name="REMARKS" value ="">
<input type="hidden" name="plant" value="<%=PLANT%>">
  <!-- below lines added by deen --> 
  <INPUT type = "hidden" name="COMNAME" value ="">
  <INPUT type = "hidden" name="RCBNO" value ="">
  <INPUT type = "hidden" name="ADDR1" value ="">
  <INPUT type = "hidden" name="ADDR2" value ="">
  <INPUT type = "hidden" name="ADDR3" value ="">
  <INPUT type = "hidden" name="ADDR4" value ="">
  <INPUT type = "hidden" name="STATE" value ="">
  <INPUT type = "hidden" name="COUNTRY" value ="">
  <INPUT type = "hidden" name="ZIP" value ="">
  <INPUT type = "hidden" name="TELNO" value ="">
  <INPUT type = "hidden" name="FAX" value ="">
  <INPUT type = "hidden" name="CHK_ADDRESS" value ="Y">
  <INPUT type = "hidden" name="LOC_TYPE_ID" value ="">
  <!-- end --> 
	<INPUT name="ACTIVE" type = "hidden" value="Y"   >
    <INPUT name="ACTIVE" type = "hidden" value="N"   >
    <INPUT	type="hidden" name="TRAVELER" value="">
  
 <div class="form-group">
<label class="control-label col-sm-2" for="Location Type">Location Type One</label>
<div class="col-sm-4 ac-box" >
<div class="input-group">
   <INPUT class="ac-selected  form-control typeahead" name="LOC_TP_ID" ID="LOC_TYPE_ID" type="TEXT" value="<%=LOC_TYPE_ID%>" size="35" MAXLENGTH=20>
  <span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TP_ID.value+'&TYPE=locsummary');">
<span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>
</div>
</div>

<label class="control-label col-sm-2" for="Location Type">Location Type Two</label>
<div class="col-sm-4 ac-box" >
<div class="input-group">
<INPUT class="ac-selected  form-control typeahead" name="LOC_TP_ID2" ID="LOC_TYPE_ID2" type="TEXT" value="<%=LOC_TYPE_ID%>"  size="35" MAXLENGTH=20>
<span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID2\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/LocType2List.jsp?LOC_TYPE_ID2='+form1.LOC_TP_ID2.value+'&TYPE=locsummary');">
<span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>
</div>
</div>

</div>
<div class="form-group">
<label class="control-label col-sm-2" for="Location">Location</label>
<div class="col-sm-4 ac-box" >
<div class="input-group">
 <INPUT class="ac-selected  form-control typeahead" name="LOC_ID" ID="LOC_ID" type ="TEXT" value="<%=LOC_ID%>"  MAXLENGTH=20>
  <span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'LOC_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/Loc_list.jsp?LOC_ID='+form1.LOC_ID.value+'&LOC_TYPE_ID='+form1.LOC_TP_ID.value);">
 <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>
  	</div>
  	</div>
  	<button type="button" class="btn btn-success" value="View" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp;
  	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
  	</div>
  
  
  
  <br>	
		
<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
                       &nbsp; Select/Unselect All &nbsp;<INPUT Type=Checkbox  style="border:0;" name="singlePrint"  <%if(singlePrint.equalsIgnoreCase("true")){%>checked <%}%> value="singlePrint" onclick="return singlePrinting(this.checked)"> 
                       &nbsp; Fill 1 Quantity For Selected Location &nbsp;</div>
  </div>		
<div style="overflow-x:auto;">
<table id="table" class="table table-bordred table-striped" > 
   
   <thead style="text-align: center"> 
          <tr>  
            <th style="font-size: smaller;">Chk</th>  
            <th style="font-size: smaller;">Location</th>
            <th style="font-size: smaller;">Description</th>
            <th style="font-size: smaller;">Location Type One</th>
             <th style="font-size: smaller;">Location Type Two</th>
            <th style="font-size: smaller;">Print Qty</th> 
          </tr>  
        </thead> 
         <!-- resvi --> 
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>								
							</tr>
						</tfoot>
						
</table>
</div>

<div class="form-group">
  	<div class="col-sm-12" align="center"> 
  	<button type="button" class="Submit btn btn-default"  value="Print" id="GB50X25" name="action" onclick="javascript:return onRePrint('50X25');"><b>Generate Barcode 50X25 mm</b></button>
  	<button type="button" class="Submit btn btn-default"  value="Print"  name="action" onclick="javascript:return onRePrint('100X50');"><b>Generate Barcode 100X50 mm</b></button>
  	  
  	</div>
  	</div>
 </FORM> 
<!-- resvi -->
<script LANGUAGE="JavaScript">
		var plant = document.form1.plant.value;
		var tabletype;
		var LOCTYPE1,LOCTYPE2,LOCT, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"LOCTYPE1":LOCTYPE1,
				"LOCTYPE2":LOCTYPE2,
				"LOCT":LOCT,
				"PLANT":plant,
				"action":"GET_LOC_FOR_SUMMARY"
			}
		}
		function onGo(){
			LOCTYPE1=document.form1.LOC_TP_ID.value;
			LOCTYPE2=document.form1.LOC_TP_ID2.value;
			LOCT=document.form1.LOC_ID.value;
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
						"processing": true,
						"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
						"ajax": {
							"type": "GET",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	
						        	if(typeof data.SUPPLIERTYPELIST[0].LOC === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.SUPPLIERTYPELIST.length; dataIndex ++){
						        				var lineno = data.SUPPLIERTYPELIST[dataIndex].LOC;
						        				var sno=dataIndex+1;
						        				
						        				data.SUPPLIERTYPELIST[dataIndex]['CHKOB'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+data.SUPPLIERTYPELIST[dataIndex]['LOC']+','+data.SUPPLIERTYPELIST[dataIndex]['LOCDESC']+'" >';
						        				data.SUPPLIERTYPELIST[dataIndex]['PRINTQTY'] = '<INPUT Type=text name=PrintQty_'+lineno+' id=PrintQty_'+lineno+' size=6 maxlength=10 >';
<%-- 												<%if(displaySummaryLink){ %> --%>
// 												data.SUPPLIERTYPELIST[dataIndex]['LOC'] = '<a href ="../inhouse/locdetail?action=View&LOC=' +lineno+'&LOCDESC=' +data.SUPPLIERTYPELIST[dataIndex].LOCDESC+'&USERFLD1=' +data.SUPPLIERTYPELIST[dataIndex].USERFLD1+'&COMNAME=' +data.SUPPLIERTYPELIST[dataIndex].COMNAME+'&RCBNO=' +data.SUPPLIERTYPELIST[dataIndex].RCBNO+'&ADD1=' +data.SUPPLIERTYPELIST[dataIndex].ADD1+'&ADD2=' +data.SUPPLIERTYPELIST[dataIndex].ADD2+'&ADD3=' +data.SUPPLIERTYPELIST[dataIndex].ADD3+'&ADD4=' +data.SUPPLIERTYPELIST[dataIndex].ADD4+'&STATE=' +data.SUPPLIERTYPELIST[dataIndex].STATE+'&COUNTRY=' +data.SUPPLIERTYPELIST[dataIndex].COUNTRY+'&ZIP=' +data.SUPPLIERTYPELIST[dataIndex].ZIP+'&TELNO=' +data.SUPPLIERTYPELIST[dataIndex].TELNO+'&FAX=' +data.SUPPLIERTYPELIST[dataIndex].FAX+'&ISACTIVE=' +data.SUPPLIERTYPELIST[dataIndex].ISACTIVE+'&LOC_TYPE_ID=' +data.SUPPLIERTYPELIST[dataIndex].LOC_TYPE_ID+'&LOC_TYPE_ID2=' +data.SUPPLIERTYPELIST[dataIndex].LOC_TYPE_ID2+'">'+data.SUPPLIERTYPELIST[dataIndex].LOC+'</a>';										
<%-- 													<% } %> --%>
						        			}
						        		return data.SUPPLIERTYPELIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'CHKOB', "orderable": true},
				        	{"data": 'LOC', "orderable": true},
				        	{"data": 'LOCDESC', "orderable": true},
				        	{"data": 'LOC_TYPE_ID', "orderable": true},
				        	{"data": 'LOC_TYPE_ID2', "orderable": true},
				        	{"data": 'PRINTQTY', "orderable": true},
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
				                    	/* exportOptions: {
				    	                	columns: [':visible']
				    	                } */
					                    exportOptions: {
					                    	columns: [0,1,2,3,4,5]
					                    }
				                    },
				                    {
				                    	extend : 'pdf',
				                    	/* exportOptions: {
				                    		columns: [':visible']
				                    	}, */
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4,5]
				                        },
			                    		orientation: 'landscape',
			                            pageSize: 'A4',
			                            	extend : 'pdfHtml5',
			    	                    	/* exportOptions: {
			    	                    		columns: [':visible']
			    	                    	}, */
			    	                    	exportOptions: {
			    	                    		columns: [0,1,2,3,4,5]
					                        },
			    	                        title: function () { var dataview = "<%=CNAME%> \n \n <%=title%> \n \n As On <%=collectionDate%>"  ; return dataview },    	                        
			                     		orientation: 'landscape',
			                     		customize: function (doc) {
			                     			doc.defaultStyle.fontSize = 16;
			                     	        doc.styles.tableHeader.fontSize = 16;
			                     	        doc.styles.title.fontSize = 20;
			                     	       doc.content[1].table.body[0].forEach(function (h) {
			                     	          h.fillColor = '#ECECEC';                 	         
			                     	          alignment: 'center'
			                     	      });
			                     	      var rowCount = doc.content[1].table.body.length;
			                     	     for (i = 1; i < rowCount; i++) {                     	     
			                     	     doc.content[1].table.body[i][1].alignment = 'center';
			  
			                     	     } 
			                     	      doc.styles.tableHeader.color = 'black';
			                     	      
			                     	        // Create a footer
			                     	        doc['footer']=(function(page, pages) {
			                     	            return {
			                     	                columns: [
			                     	                    '',
			                     	                    {
			                     	                        // This is the right column
			                     	                        alignment: 'right',
			                     	                        text: ['page ', { text: page.toString() },  ' of ', { text: pages.toString() }]
			                     	                    }
			                     	                ],
			                     	                margin: [10, 0]
			                     	            }
			                     	        });
			                     		},
			                             pageSize: 'A2',
			                             footer: true
				                    }
				                ]
				            },
				            {
			                    extend: 'colvis',
			                   /*  columns: ':not(:eq('+groupColumn+')):not(:last)' */
			                }		                
				        ],
				        "order": [],
				        "drawCallback": function ( ) {		
							$('.buttons-collection')[0].style.display = 'none';
							$('.buttons-colvis')[0].style.display = 'none';
				        }
					});
			    }
			    
			}

		</script>
		<!-- END --> 
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>
$(document).ready(function(){
/* 	$('#table').dataTable({
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
	}); */
    onGo();
    $('[data-toggle="tooltip"]').tooltip();
    var plant= '<%=PLANT%>'; 

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
	/* location Auto Suggestion */
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
	/* location Auto Suggestion */
	$('#LOC_TYPE_ID2').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID2',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETWO_LIST_FOR_SUGGESTION",
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID2+'</p></div>';
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