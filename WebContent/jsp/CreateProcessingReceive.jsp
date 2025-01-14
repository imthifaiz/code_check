<%@page import="com.track.dao.ProductionBomDAO"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.*"%>
<%
//String title = "Processing Receive";
String title = "De-Kitting";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
    <jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>

<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script src="../jsp/js/ProcessingReceive.js"></script>
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'ProcessingReceive', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "",action     =   "",pitem="",pbatch="",LOC_ID="",pqty="1",DESC="",sSAVE_RED="",
LOC_DESC="",LOC_TYPE_ID="",LOC_TYPE_DESC="",LOC_TYPE_ID2="",fieldDesc="",allChecked="",PUOM="",SerParent="checked",ORDDATE="",RFLAG="2",errcls="error-msg",
RECLOC_ID="PROCESSING";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
LOC_ID  = strUtils.fString(request.getParameter("LOC_ID"));
LOC_DESC  = strUtils.fString(request.getParameter("LOC_DESC"));
LOC_TYPE_ID  = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_DESC  = strUtils.fString(request.getParameter("LOC_TYPE_DESC"));
pitem  = strUtils.fString(request.getParameter("ITEM"));
pbatch  = strUtils.fString(request.getParameter("BATCH_0"));
pqty  = strUtils.fString(request.getParameter("PARENTQTY"));
PUOM  = strUtils.fString(request.getParameter("PUOM"));
LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
fieldDesc = strUtils.fString((String)request.getAttribute("Msg"));

if(action.equalsIgnoreCase(""))
	action = (String)(request.getAttribute("action"));
if(pitem.equalsIgnoreCase(""))
	pitem = (String)(request.getAttribute("ITEM"));
if(pbatch.equalsIgnoreCase(""))
	pbatch = (String)(request.getAttribute("BATCH_0"));
	DESC = (String)(request.getAttribute("DESC"));
if(pqty.equalsIgnoreCase(""))
{
	pqty="1";
}
if(pbatch.equalsIgnoreCase(""))
{
	pbatch="NOBATCH";
}

String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
String currency=new PlantMstDAO().getBaseCurrency(plant);
String CURRENCYUSEQT="0",DISPLAY="",DISPLAYID="";
List curQryList=new ArrayList();
curQryList = new CurrencyDAO().getCurrencyDetails(currency,plant);
for(int i =0; i<curQryList.size(); i++) {
		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
		DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
		DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
       CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
}
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../ProcessingReceive/summary"><span class="underline-on-hover">De-Kitting Summary</span></a></li>	
                <li><label>De-Kitting</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../ProcessingReceive/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		<center>
	<h2>
		<small class="<%=errcls%>"><%=fieldDesc%></small>
	</h2>
</center>
 <div class="container-fluid">
<form class="form-horizontal" id="processingReceiveForm" name="form" action="../ProcessingReceive/add"  method="post"  enctype="multipart/form-data">
<!-- <form class="form-horizontal" name="form" method="post" action=""> -->
<INPUT type="hidden" name="RFLAG" value="<%=RFLAG%>">
<INPUT type="hidden" name="plant" value=<%=plant%>>
<input type="hidden" name="LOC_DESC" id="LOC_DESC" value="<%=LOC_DESC%>">
<input type="hidden" name="LOC_TYPE_DESC" id="LOC_TYPE_DESC" value="<%=LOC_TYPE_DESC%>">
<input type="hidden" name="ORDDATE" id="ORDDATE" value="<%=ORDDATE%>">
<input type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
<input type="hidden" name="COMP_INDUSTRY" value="<%=COMP_INDUSTRY%>">
<input type="hidden" name="DESC" id="DESC">
<input type="hidden" name="DETDESC" id="DETDESC">
<input type="hidden" name="CDETDESC" id="CDETDESC">
<input type="hidden" name="CDESC" id="CDESC">
<input type="hidden" name="LOC_TYPE_ID" id="LOC_TYPE_ID">
<input type="hidden" name="LOC_TYPE_ID2" id="LOC_TYPE_ID2">
<input type="hidden" name="CDESC" id="CDESC">
<input type="hidden" name="ISAUTOGENERATE" value="false">
<input type="hidden" name="PCOST" value="0">
<input type="hidden" name="CALCOST">
<input type="hidden" name="PARENTQPUOM">
<input type="hidden" name="STKUOMQTY">
<input type="hidden" name="SUMCQTY">
<input type="number" id="numberOfDecimal" name="numberOfDecimal" style="display: none;" value=<%=numberOfDecimal%>>
 <div id = "ERROR_MSG"></div>
   <div id = "COMPLETED_MSG"></div>

                 
<div class="form-group">
     <label class="control-label col-form-label col-sm-2 required">De-Kitting Product</label>
    <div class="col-sm-4 ac-box">
				<div class="input-group">   
    		<input type="TEXT"  name="ITEM" id="ITEM" value="<%=pitem%>" onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}"
			size="20" MAXLENGTH=100  class="ac-selected  form-control typeahead">
			<span class="select-icon" style="right:10px;" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>   		 		
  		</div>
  		<input type = "hidden" class="form-check-input" style="border:0;" name = "pserialqty"  id = "pserialqty" value="pserialqty" onclick="ParentSerialqty();">
  		</div>
  		<div class="form-inline">
<div class="col-sm-4">
    <button type="button" class="Submit btn btn-default" value="View" name="Submit" onclick="onGo(1)">View</button>
    <label class="radio-inline">
				      	<input type="radio" name="Processing" type = "radio"  value="1"  id="Auto" checked onclick="chkProcessing();">Auto 
				    	</label>
				    	<label class="radio-inline">
				      	<input type="radio" name="Processing" type = "radio" value="0"  id = "Manual" onclick="chkProcessing();">Manual 
				     	</label>
       </div>
          </div>
  		</div>

		  		<div class="form-group">
					<label class="control-label col-form-label col-sm-2 ">Product Description:</label>
					<div class="col-sm-4">
						<input type="text" readonly class="form-control" id="ITEM_DESC" name="ITEM_DESC" value="">
					</div>
				</div>     
  		
  		<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">De-Kitting Location:</label>
					<div class="col-sm-4">
					
						<input type="text" readonly class="form-control" id="LOC_ID" name="LOC_ID" value="<%=RECLOC_ID%>" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation();}">
					</div>
				</div>               
<% if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) { %>
<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Batch:</label>
					<div class="col-sm-4">
					<div class="input-group">
						<input class="ac-selected  form-control typeahead" name="BATCH_0" id="BATCH_0" type="TEXT" value="<%=pbatch%>" readonly size="20" MAXLENGTH=40>   		 	
					</div>
					</div>
				</div>
				<%}else{ %>
<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Batch:</label>
					<div class="col-sm-4">
					<div class="input-group">
						<input class="ac-selected  form-control typeahead" name="BATCH_0" id="BATCH_0" type="TEXT" value="<%=pbatch%>" size="20" MAXLENGTH=40>
   		 	<span class="input-group-addon"  onClick="javascript:generateParBatch(0);return false;"  id="autoGenBatch">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Generate Batch">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
					</div>
					</div>
				</div>
				<% }%>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Cost:</label>
					<div class="col-sm-4">
					<div class="input-group">
						<input class="form-control" name="COST" id="COST" readonly type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble("0"), numberOfDecimal)%>" size="20" MAXLENGTH=40>
					</div>
					</div>
				</div>

<div class="form-group">
      <label class="control-label col-form-label col-sm-2 required">De-Kitting Quantity</label>
     <div class="col-sm-2">
				<div class="input-group">     
          <INPUT  class="form-control" name="PARENTQTY" type="TEXT" id="PARENTQTY" onkeypress="return isNumberKey(event,this,4)" value="<%=pqty%>" size="4" MAXLENGTH=50 onchange="pqtychange();">
			</div>
            </div>
            <div class="col-sm-2">
    	<input type="text" name="PARENTUOM" id="PARENTUOM" class="form-control" placeholder="Parent UOM" readonly value="<%=PUOM%>">
    	<span class="select-icon" style="right:25px;" onclick="$(this).parent().find('input[name=\'PARENTUOM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	 	</div>
            
            </div>
   <div class="form-group">
					<label class="control-label col-form-label col-sm-2">Wastage</label>
					<div class="col-sm-2">
					<div class="input-group">
						<input class="form-control" name="WASTAGE" id="WASTAGE" type="TEXT" onkeypress="return isNumberKey(event,this,4)" value="<%=StrUtils.addZeroes(Double.parseDouble("0"), "3")%>" size="20" MAXLENGTH=40 onchange="getWastageCost();">
					</div>
					</div>
					<div class="col-sm-1">
					<div class="input-group">
						<input class="form-control" name="WASTAGECOST" id="WASTAGECOST" type="TEXT" readonly  value="<%=StrUtils.addZeroes(Double.parseDouble("0"), numberOfDecimal)%>" size="20" MAXLENGTH=40>
					</div>
					</div>
					<div class="col-sm-1">
					<div class="input-group">
						<input class="form-control" name="CURRENCY" type="TEXT" readonly  value="<%=DISPLAY%>" >
					</div>
					</div>
				</div>		
  		  
  <div class="form-group">        
     <div class="col-sm-12" align="center">
      <button type="button" class="btn btn-success" value="Kit" onClick="onAdd()">Receive</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Clear" id="Clear" onClick="onClear();">Clear</button>&nbsp;&nbsp;
      	
     </div>
       </div>        
      
<div id="RESULT_MESSAGE">
<%--  <table class="table table-bordered line-item-table po-table">
	<tr><td align="center"><%=(String)session.getAttribute("MESSAGE")%></td></tr>
	
</table> --%>
</div>
    <div id="VIEW_RESULT_HERE"></div>
  <div class="row" id="divProcessing">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRow()">+ Add another line</a>
						</div>
						</div>
  <div id="spinnerImg" ></div>
  
<input type="hidden" name="PTYPE" id="PTYPE" value="CREATEKITBOM">
</form>
</div>
  </div>
  </div>
       

<script>
onGo(0);

function validateLocation() {
	var locId = document.getElementById("LOC_ID").value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC_ID").focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
				PLANT : "<%=plant%>",
					ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						document.getElementById("LOC_DESC").value = resultVal.locdesc;
						document.getElementById("LOC_TYPE_ID").focus();
					} else {
						alert("Not a valid Location");
						document.getElementById("LOC_ID").value = "";
						document.getElementById("LOC_ID").focus();
					}
				}
			});
		}
	}
function validateLocationtype() {
	var loctypeId = document.getElementById("LOC_TYPE_ID").value;
	if(loctypeId=="" || loctypeId.length==0 ) {
		alert("Enter Location Type!");
		document.getElementById("LOC_TYPE_ID").focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOCTYPE : loctypeId,
				PLANT : "<%=plant%>",
					ACTION : "VALIDATE_LOCTYPE"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						document.getElementById("LOC_TYPE_DESC").value = resultVal.loctypedesc;
						document.getElementById("REMARKS").focus();
					} else {
						alert("Not a valid Location");
						document.getElementById("LOC_TYPE_ID").value = "";
						document.getElementById("LOC_TYPE_ID").focus();
					}
				}
			});
		}
	}

function validateProduct() {
	var productId = document.form.ITEM.value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
	}else{
	var urlStr = "/track/ProductionBomServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		ITEM : productId,
		PLANT : "<%=plant%>",
		action : "VALIDATE_PARENT_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("DESC").value = resultVal.discription;
							document.getElementById("DETDESC").value = resultVal.detaildesc;
							document.form.BATCH_0.value = "NOBATCH";
							document.form.BATCH_0.focus();
							document.form.BATCH_0.select();
							
						} else {
							alert("Not a valid Parent product!");
							document.form.ITEM.value = "";
							document.form.ITEM.focus();
						}
					}
				});
		}
	}	

function generateParBatch(index){
	var currentbatch=index;
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
			PLANT : "<%=plant%>",
			ACTION : "GENERATE_BATCH"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;
				document.getElementById("BATCH_"+currentbatch).value = resultVal.batchCode;
				//document.getElementById("QTY_"+currentbatch).focus();

			} else {
				alert("Unable to genarate Batch");
				document.getElementById("BATCH_"+currentbatch).value = "";
				document.getElementById("BATCH_"+currentbatch).focus();
			}
		}
	});
}


function isNumericInput(strString) {
	var strValidChars = "0123456789.-";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for ( var i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
}
function onClear()
{
	$("input[name ='ITEM']").typeahead('val', "");
	document.getElementById("ITEM").value = ""
	document.getElementById("DESC").value = ""
	document.getElementById("ITEM_DESC").value = ""
	document.getElementById("DETDESC").value = ""
	document.getElementById("BATCH_0").value = "NOBATCH"
	document.getElementById("PARENTUOM").value = ""
	document.getElementById("PARENTQTY").value = "1"
	document.form.SUMCQTY.value=parseFloat(0).toFixed(3);;
	document.form.WASTAGE.value=parseFloat(0).toFixed(3);
	document.form.PARENTQPUOM.value=parseFloat(0).toFixed(3);
	document.form.STKUOMQTY.value=parseFloat(0).toFixed(3);
	document.getElementById("COST").value = "<%=StrUtils.addZeroes(Double.parseDouble("0"), numberOfDecimal)%>"
	document.form.PCOST.value = "<%=StrUtils.addZeroes(Double.parseDouble("0"), numberOfDecimal)%>"
	document.form.CALCOST.value = "<%=StrUtils.addZeroes(Double.parseDouble("0"), numberOfDecimal)%>"
	document.getElementById("ORDDATE").value = ""
	document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</tbody></TABLE>';
	addRow();
}


function onAdd() {
	var loc = document.form.LOC_ID.value;
	var loctype = document.form.LOC_TYPE_ID.value;
	var pitem = document.form.ITEM.value;
	var pbatch = document.form.BATCH_0.value;
	var pqty = document.form.PARENTQTY.value;
	var puom = document.form.PARENTUOM.value;
	var ORDDATE = document.form.ORDDATE.value;
	var COST = document.form.COST.value;
	var plant = document.form.plant.value;
	var isItemValid = true;
	if(loc=="" || loc.length==0 ) {
		alert("Enter Location");
		document.getElementById("LOC_ID").focus();
		return false;
	}
	if(pitem=="" || pitem.length==0 ) {
		alert("Enter De-Kitting Product");
		document.getElementById("ITEM").focus();
		return false;
	}
	if(pbatch=="" || pbatch.length==0 ) {
		alert("Enter De-Kitting Product Batch");
		document.getElementById("BATCH_0").focus();
		return false;
	}
	if(puom=="" || puom.length==0 ) {
		alert("Enter De-Kitting UOM");
		document.getElementById("PARENTUOM").focus();
		return false;
	}
	$("input[name ='citem']").each(function() {
	    if($(this).val() == ""){	    	
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The Product field cannot be empty.");
		return false;
	}
	var errorBoo = false;
	$(".po-table tbody tr").each(function() {
		var kuom = $(this).find("input[name ='UOM']").val();
		var kloc = $(this).find("input[name ='loc']").val();
		if(kuom=="" || kuom.length==0 ) {
			alert("Enter Child UOM");
			errorBoo = true;
			return false;
		}
		if(kloc=="" || kloc.length==0 ) {
			alert("Enter Child Location");
			errorBoo = true;
			return false;
		}
	});
	if(errorBoo)
	{
		return false;
	}
	 errorBoo = false;
	var urlStr = "../MasterServlet";
	$.ajax({
		type : "POST",
		url : urlStr,
		async : false,
		data : {
			PLANT : plant,
			ACTION : "GET_STOCK_BATCH_DATA",
			QUERY : pbatch,
			ITEMNO : pitem,
			UOM : puom,
			LOC : loc,
			QTY : pqty
		},
		cache: false,
		dataType : "json",
		success : function(data) {
			if(data.batches[0].ERROR_CODE=='98')
			{				
				errorBoo = true;
			}						
		}
	});
if(errorBoo)
	{
		alert("De-Kitting Qty is greater then inventory qty !!!");
		return false;
	} 
	
	var tabled=document.getElementById("tabledata");
	var len = tabled.rows.length;
	//for ( var i = 1; i < len; i++) {
		
		//var item = (document.getElementById("citem_" + i).innerText).toUpperCase();
		//var eitem = (document.getElementById("eitem_" + i).innerText).toUpperCase();
		//var item = trim(item);
		//var eitem = trim(eitem);
		//var eitem = "";
	//}
		
	
	document.getElementById('RESULT_MESSAGE').innerHTML = '';   
    //document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    //var urlStr = "/track/ProcessingReceive/add";
    $("#loader").show();
	$(".wrapper").css("opacity","0.1");
    $( "#processingReceiveForm" ).submit();
    $("#loader").hide();
	$(".wrapper").css("opacity","1");
    // Call the method of JQuery Ajax provided
    <%-- $.ajax({type: "POST",url: urlStr, data: {LOC_ID:loc,LOC_TYPE_ID:loctype,ITEM:pitem,BATCH_0:pbatch,PARENTQTY:pqty,PUOM:puom,ORDDATE:ORDDATE,PCOST:COST,KITTYPE:"KITDEKITWITHBOM",PLANT:"<%=plant%>"},dataType: "json", success: savecallback }); --%>
	    
 }

function onGo(index) {

	var index = index;
	var product = document.form.ITEM.value;
	var loc = document.form.LOC_ID.value;
	var pbatch = document.form.BATCH_0.value;
	var pqty = document.form.PARENTQTY.value;
	
	if(index == '1'){
	if(product=="" || product.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
		return false;
		}
	if(pqty=="" || pqty.length==0 ) {
		alert("Please Enter Qty of De-Kitting Product");
		document.getElementById("PARENTQTY").focus();
		return false;
		}
     
	}
	document.getElementById('RESULT_MESSAGE').innerHTML = ''; 
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    for( i = 0; i < document.form.Processing.length; i++ )
	  {
		  if( document.form.Processing[i].checked == true )
		  {
			  val = document.form.Processing[i].value;
			  if(val=='1')
			  {
    var urlStr = "/track/ProcessingReceive/VIEW_PROCESSING_DETAILS_TO_RECEIVE";
    $("#loader").show();
	$(".wrapper").css("opacity","0.1");
    // Call the method of JQuery Ajax provided
    $.ajax({async: true,type: "POST",url: urlStr, data: {ITEM:product,BATCH_0:pbatch,PARENTQTY:pqty,KITTYPE:"KITDEKITWITHBOM",PLANT:"<%=plant%>"},dataType: "json", success: callback, 
        complete: function(){
        	$("#loader").hide();
        	$(".wrapper").css("opacity","1");
        } });
    //addSuggestionToTable();
			  } else {
				  document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</tbody></TABLE>';
				  addRow();
				  }
		  }
	  }
}

function savecallback(data){
	
	var outPutdata = getTable();
	var ii = 0;
	var errorBoo = false;
	$.each(data.errors, function(i,error){
		if(error.ERROR_CODE=="99"){
			errorBoo = true;
			
		}
	});
	
	var errorMsg = data.errorMsg;
    if(typeof(errorMsg) == "undefined"){
   	 errorMsg = "";
    }
    errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
    document.getElementById('ERROR_MSG').innerHTML = errorHTML;
    if(errorMsg.indexOf("Product Processed Successfully") >= 0){
    	document.form.action  = "../ProcessingReceive/summary?msg="+errorMsg;
    	document.form.submit();
        }
    var comMsg = data.completedMes;
    if(typeof(comMsg) == "undefined"){
   	 comMsg = "";
    }
    mesHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+comMsg+"</td></tr></table>";
    document.getElementById('COMPLETED_MSG').innerHTML = mesHTML;
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
        $.each(data.kittingbom, function(i,item){
                   
        	outPutdata = outPutdata+item.KITBOMDATA;
                    	ii++;
            
          });
        
	}
    outPutdata = outPutdata +'</TABLE>';
                                                  
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
    getcost();
    document.getElementById('spinnerImg').innerHTML =''; 
     var errorMsg = data.errorMsg;
     if(typeof(errorMsg) == "undefined"){
    	 errorMsg = "";
     }
     errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
     document.getElementById('ERROR_MSG').innerHTML = errorHTML;
     var comMsg = data.completedMes;
     if(typeof(comMsg) == "undefined"){
    	 comMsg = "";
     }
     mesHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+comMsg+"</td></tr></table>";
     document.getElementById('COMPLETED_MSG').innerHTML = mesHTML;
    
      	     
}

function getTable(){
        return '<TABLE class="table table-bordered line-item-table po-table" id="tabledata" WIDTH="100%" align = "center">'+
               '<thead>'+
        	   '<tr >'+
        	   '<th colspan=2 style="width:20%">Product</th>'+
				'<th style="width:8%">UOM</th>'+
				'<th class="bill-desc">Location</th>'+
				'<th class="bill-desc">Batch</th>'+
				'<th style="width:8%">Quantity</th>'+
				'<th style="width:8%">Tran.Quantity</th>'+
				'<th class="item-amount">Average UnitCost</th>'+
				'<th class="item-amount">Total Average UnitCost</th>'+
        		'</tr>'+
         		'</thead>'+
         		'<tbody>';
       
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</tbody></TABLE>';

</script>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>