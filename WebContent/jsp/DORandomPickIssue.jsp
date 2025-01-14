<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp" %>

<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%><html>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<title>Pick/Issue By OutBound Order</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
var isOpenOrder = false;
function popUpWin(URL) {
  subWin = window.open(URL, 'multiPickIssue', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
</script>
<%
String PLANT = "",TRANSACTIONDATE="";
StrUtils strUtils = new StrUtils();
PLANT= session.getAttribute("PLANT").toString();
TRANSACTIONDATE = strUtils.fString(request.getParameter("TRANSACTIONDATE"));
DateUtils _dateUtils = new DateUtils();
String curDate =_dateUtils.getDate();
if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
%>
<%@ include file="body.jsp" %>
<FORM name="form1" method="post" action="/track/DOMultiPickIssueServlet">

  <br>
   <CENTER>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11">
          <FONT color="#ffffff">Multi Pick/Issue By OutBound Order</FONT>&nbsp;
        </TH></table>
  <br>
  

 <TABLE WIDTH="70%" border="0" cellspacing="1" cellpadding="2"
	align="center" bgcolor="#dddddd">
	<tr>
	<td>
	<TABLE WIDTH="50%" border="0" cellspacing="0" cellpadding="0"
	align="center" >
	<tr>
		<TH ALIGN="right" >OutBound Order :</TH>
		<TD ALIGN="left" >
		 <INPUT type="TEXT" size="20" MAXLENGTH="20" name="DONO" id="DONO" value="" />
          <a href="#" onClick="javascript:popUpWin('list/ob_order_list.jsp?DONO='+form1.DONO.value);">
                           <img src="images/populate.gif" border="0"/>
          </a>
         </td>		
		<TD><INPUT  type="button"
			value="View" onClick="onGo();"></TD>
			</tr>
			</table>
		</td>
	</TR>
	</TABLE>
	<br>
	<TABLE WIDTH="90%" border="0" cellspacing="1" cellpadding="2"
	align="center" bgcolor="#dddddd">
	<tr>
	<td>
		<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
			align="center" >
			<tr>
				<td width="15%"><input type="checkbox" checked name="DEF_QTY1" onclick="return Checkdefqty()">&nbsp;Default
					Qty to 1</td>
				<td width="25%"><input type="checkbox" checked name="DEF_NOBATCH" onclick="return Checkdefbatch()">&nbsp;Default to
					NOBATCH</td>
					<td colspan = "2"></td>
			</tr>
		</table>

		<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center">
	<TR>
		<TH ALIGN="RIGHT" >Location :</TH>
		<TD ALIGN="left" ><INPUT name="LOC_ID" type="TEXT" id="LOC_ID"
			value="" size="20" MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){focusitem();}">
		<a href="#" onClick="javascript:popUpWin('LocListView.jsp?LOC_ID='+ form1.LOC_ID.value);">
		<img src="images/populate.gif" border="0" /> </a></TD>
		<TH ALIGN="right" >Scan Product ID : &nbsp;&nbsp;</TH>
		<TD ALIGN="left" ><INPUT name="ITEM" type="TEXT" id="ITEM"
			value="" size="20" MAXLENGTH=50 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateProductAgainstPickList(0);}">
			<!-- <a href="#" 
			onClick="javascript:popUpWin('OutBoundPickingItemList.jsp?ITEM='+form1.ITEM.value+'&DONO='+form1.DONO.value);"><img
			src="images/populate.gif" border="0"></a> --></TD>
	    <TH ALIGN="right" >Desc :  &nbsp;&nbsp;</TH>
		<TD  ALIGN="left"><INPUT name="ITEMDESC" id="ITEMDESC" type="TEXT" value="" size="30" maxlength="50"></td>
		<TH ALIGN="right" >Batch :  &nbsp;&nbsp;</TH>
		<TD  ALIGN="left"><INPUT name="BATCH" id="BATCH" type="TEXT" value="NOBATCH" size="20" maxlength="40"
		onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateANDPick();}">		
		</td>	
		<TH ALIGN="right" >Qty :  &nbsp;&nbsp;</TH>
		<TD  ALIGN="left"><INPUT name="QTY" id="QTY" type="TEXT" value="1" size="1" maxlength="10"
		onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){updatepickqty();}">	
		 <input type = "text"  size = "2" READONLY name = "UOM"  style= "border: 0px;background-color:#dddddd ;"/>
		</td>
		<td ALIGN="left" ><input type="button" 
			value="Next" onclick="return validateProductAgainstPickList(1)"></td>
		</tr>
</table>
<br>
		<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
			align="left" >
			<tr>
				<TD ALIGN="left" ><b>Transaction Date : </b><INPUT name="TRANSACTIONDATE" id="TRANSACTIONDATE"
					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" MAXLENGTH="80" readonly="readonly">
				<a href="javascript:show_calendar('form1.TRANSACTIONDATE');"
			   onmouseover="window.status='Date Picker';return true;"
			   onmouseout="window.status='';return true;"> <img
			   src="images\show-calendar.gif" width="24" height="22" border="0" /></a>
	          </TD>
			</tr>
		</table>
</td>
</tr>
</TABLE>
<br>
   
   <div id = "ERROR_MSG"></div>
   <br>
   
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  <br>
  <div id = "issue"></div>
   <br>
      <SCRIPT LANGUAGE="JavaScript">
      
      function autoGenerate(){
    	  var dono = document.form1.DONO.value;
    	  if(dono == "" || dono.length==0 ) {
  			alert("Select Order Number!");
  			document.form1.DONO.focus();
  			return ;
  		}else{
    	  var urlStr = "/track/DOMultiPickIssueServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
					DONO  : dono,
					PLANT : "<%=PLANT%>",
					ACTION : "GENERATE_PACKINGLIST_NUM"
					},
					dataType : "json",
					success : function(data) {
						
						if (data.status == "100") {
							var resultVal = data.result;
							document.form1.PCKLISTNUM.value = resultVal.PCKLISTNUM;
							
						} else {
							alert("Error in generating PackingList Number");
						}
					}
					
				});
      }
      
      }

      function Checkdefqty()
      {
          if(document.form1.DEF_QTY1.checked)
          {
        	  document.getElementById("QTY").value=1;
          }
      }

      function Checkdefbatch()
      {
          if(document.form1.DEF_NOBATCH.checked)
          {
        	  document.form1.BATCH.value = "NOBATCH";
          }
      }
      
      
      
      function validateProductAgainstPickList(index) {
          
          var chkindex = index;
    		var productId = document.form1.ITEM.value;
    		var dono = document.form1.DONO.value;
    		var location = document.form1.LOC_ID.value; 
    	    if(location == "" || location.length==0 ){
    	    	alert("Select Location");
    	    	document.form1.LOC_ID.focus();
    	    	return false;
    	    }
    		
    		if(document.form1.ITEM.value=="" || document.form1.ITEM.value.length==0 ) {
    			alert("Enter Product ID!");
    			document.form1.ITEM.focus();
    			return false;
    		}else{ 	
    			var urlStr = "/track/MiscOrderHandlingServlet";
    			$.ajax( {
    				type : "POST",
    				url : urlStr,
    				data : {
    					ITEM : productId,
    					DONO  : dono,
    					PLANT : "<%=PLANT%>",
    					ACTION : "VALIDATE_PRODUCT_AGAINST_PICKLIST"
    					},
    					dataType : "json",
    					success : function(data) {
    						
    						if (data.status == "100") {
    							var resultVal = data.result;
    							document.form1.ITEM.value = resultVal.item;
    							document.form1.ITEMDESC.value = resultVal.discription;
    							document.form1.UOM.value = resultVal.uom;
    							//document.form1.BATCH.value = "NOBATCH";
    							if(chkindex == 1){
    								updatepickqty(); 
    							}
    							else{	
    							if(document.form1.DEF_QTY1.checked && document.form1.DEF_NOBATCH.checked){
        			    			 
        			    			updatepickqty(); 
        			    			
        			    		}
    							else
    							{
    								document.form1.BATCH.select();
        							document.form1.BATCH.focus();
    							}
    						}
    							
    						} else {
    							alert("Not a valid product to pick against the Outbound Order OR Exceeded the ordered QTY");
    							document.form1.ITEM.value = "";
    							document.form1.ITEM.focus();
    							return ;
    						}
    					}    					
    				});
    			}
    		
    		
    		}

		function focusitem()
		{
			document.form1.ITEM.focus();
		}
      function updatepickqty(){

    	  
    	  if(document.form1.ITEM.value=="" || document.form1.ITEM.value.length==0 ) {
  			alert("Enter Product ID!");
  			document.form1.ITEM.focus();
  			return false;
    	  }
    	  if(document.form1.ITEMDESC.value=="" || document.form1.ITEMDESC.value.length==0 ) {
    			alert("Enter Product Description!");
    			document.form1.ITEMDESC.focus();
    			return false;
      	  }
    	  if(document.form1.BATCH.value=="" || document.form1.BATCH.value.length==0 ) {
    			alert("Enter Batch!");
    			document.form1.BATCH.focus();
    			return false;
      	  }
    	  if(document.form1.QTY.value=="" || document.form1.QTY.value.length==0 ) {
    			alert("Enter QTY!");
    			document.form1.QTY.focus();
    			return false;
      	  }
         
			var table=document.getElementById("tabledata");
			var len = table.rows.length;
			var scanitem = trim((document.getElementById("ITEM").value).toUpperCase());
			var scanqty = parseInt(document.getElementById("QTY").value);
			for ( var i = 0; i < len-1; i++) {
				//var lno = document.getElementById("lno_" + i).innerText ;
				var item = (document.getElementById("item_" + i).innerText).toUpperCase();
				var item = trim(item);
				var orderqty = parseInt(document.getElementById("qtyor_" + i).innerText) ;
				var balqty = parseInt(document.getElementById("balqty_" + i).innerText) ;
				var issueqty = parseInt(document.getElementById("qtyis_" + i).innerText) ;
				var scanedqty = parseInt(document.getElementById("scan_" + i).innerText) ;
				if(scanitem == item){
					if(scanqty>balqty)
					{
						alert("Scan Qty Exceeds Order Qty for the Product Scanned.");
						document.form1.ITEM.select();
		    			document.form1.ITEM.focus();
						return false;
					}
					document.getElementById("balqty_" + i).innerText=balqty-scanqty;
					document.getElementById("scan_" + i).innerText=scanedqty+scanqty;
					//document.getElementById("qtyis_" + i).innerText=issueqty+scanqty;
					updatetemptable(); 
					document.form1.ITEM.select();
	    			document.form1.ITEM.focus();
				}
				
			}
			
		}
      
      function validateANDPick(){
    	  if(document.form1.DEF_QTY1.checked){
    		  updatepickqty();
    		  
    		  
  		}
  		else{
  		 document.form1.QTY.select();
  		   document.form1.QTY.focus();
    	  
  		}
    	  
      }
    		
      
      function onGo(){
            var orderNO = document.form1.DONO.value; 
    	    //var location = document.form1.LOC_ID.value; 
    	    if(orderNO == "" || orderNO.length==0 ){
    	    	alert("Select OrderNo");
    	    	return false;
    	    }
    	    
    	    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    	    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    	    var urlStr = "/track/DOMultiPickIssueServlet";
    	    
    	    // Call the method of JQuery Ajax provided
    	    $.ajax({type: "POST",url: urlStr, data: {DONO:orderNO,Submit: "GET_DODET_DETAILS_RANDOMISSUE",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
    	  
    	}

      function updatetemptable()
      {
    	 
    	    var orderNO = document.form1.DONO.value;   
    	    var item = document.form1.ITEM.value; 
    	    var itemDesc = document.form1.ITEMDESC.value;
    	    var batch = document.form1.BATCH.value;
    	    var loc = document.form1.LOC_ID.value;
    	    var qty = document.form1.QTY.value;
    	   // if(item == "" || itemDesc == "" || batch == "" ||qty == ""){
    	    //	alert("All fields cannot be empty.");
    	    //	return false;
    	   // }
    	   // document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    	   // document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    	    var urlStr = "/track/DOMultiPickIssueServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
					ITEM :item,
					DONO:orderNO,
					ITEMDESC :itemDesc,
					BATCH : batch,
					LOC_ID :loc,
					QTY : qty,
					PLANT : "<%=PLANT%>",
					Submit: "UPDATE_TEMP"
					},
					dataType : "json",
					success : function(data) {
						
						if (data.status == "100") {
							//var resultVal = data.result;
							//document.form1.ITEMDESC.value = resultVal.discription;
							} else {
								var errorMsg = data.errorMsg;
				    	         if(typeof(errorMsg) == "undefined"){
				    	        	 errorMsg = "";
				    	         }
				    	         errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
				    	         document.getElementById('ERROR_MSG').innerHTML = errorHTML;
				    	       
							return ;
						}
					}    					
				});
    	    
    	    
    	    // Call the method of JQuery Ajax provided
    	   // $.ajax({type: "POST",url: urlStr, data: {DONO:orderNO,ITEM :item ,ITEMDESC :itemDesc ,BATCH : batch , LOC_ID :loc ,QTY : qty ,Submit: "UPDATE_TEMP",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
    	  
      }

      function onResetScanqty()
      {
    	var orderNO = document.form1.DONO.value;   
  	    var loc = document.form1.LOC_ID.value;
  	  if(orderNO == ""){
	    	alert("Select Order No");
	    	return false;
	    }
			
  	   		document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
 	    	document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
 	   	    var urlStr = "/track/DOMultiPickIssueServlet";
 	    
 	    // Call the method of JQuery Ajax provided
 	   	   $.ajax({type: "POST",url: urlStr, data: {DONO:orderNO,LOC_ID :loc,Submit: "RESET_SCANQTY",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
 	  	
      }
      
      function onRePrintWithOutBatch(){

    	    var flag    = "false";
    	    var DONO    = document.form1.DONO.value;
    	    if(DONO != null    && DONO != "") { flag = true;}
    	    if(flag == "false"){ alert("Please Enter Order No"); return false;}    	   
    	    if(isOpenOrder){
    	    	alert("Order has not yet picked/Issued"); 
    	    	return false;
    	    }
    	    else{
    	    	document.form1.action="/track/DynamicFileServlet?action=printDOWITHOUTBATCH&TYPE=DORANDOM&DONO="+DONO;
        	    document.form1.submit();
    	    }
    	    
    	    
    	}

      function onPickIssue()
      {
    	   
    	    var orderNO = document.form1.DONO.value;   
			var tranDate = document.form1.TRANSACTIONDATE.value; 
    	    var loc = document.form1.LOC_ID.value;
    	    var table=document.getElementById("tabledata");
			var len = table.rows.length;
			//var ScannedQty = new Array();
			var ScannedQty = 0;
		    if(orderNO == ""){
    	    	alert("Select Order No");
    	    	return false;
    	    }
    	    for ( var i = 0; i < len-1; i++) {
        	    
    	    	ScannedQty += parseInt(document.getElementById("scan_" + i).innerText) ;
						 
    	    }
	        if(ScannedQty == 0){
			alert("No Scanned Qty To Issue.Scan the Product For Qty to Issue.");
			return false;
	        }
	    	   	   
    	    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    	    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    	    var urlStr = "/track/DOMultiPickIssueServlet";
    	    
    	    // Call the method of JQuery Ajax provided
    	    $.ajax({type: "POST",url: urlStr, data: {DONO:orderNO,LOC_ID :loc,TRANSACTIONDATE:tranDate,Submit: "PROCESS_MULTI_PICKISSUE",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
    	  
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
    	                       
    		        	outPutdata = outPutdata+item.PRODUCT;
    	                        	ii++;
    		            
    		          });
    		        
    			}
    	        outPutdata = outPutdata +'</TABLE>';
    	        var issueHTML = '<br><br><TABLE WIDTH="20%"  border="0" cellspacing="1" cellpadding = 2 align = "center"> <TR><TD><INPUT  type="button" value="Reset Scan QTY" style="height: 25px; width: 100px" onClick="onResetScanqty();">'
        	        			+'&nbsp;&nbsp;<INPUT  type="button" '
    	        				+ 'value="Issue" style="height: 25px; width: 80px" onClick="onPickIssue();">'
    	        				+ '&nbsp;&nbsp;<INPUT  type="button"  value="Print DO" style="height: 25px; width: 100px" onClick="javascript:return onRePrintWithOutBatch();">'
    	        				+ '</TD>  <TR></TABLE>';
    	        				
    	        				
    	        				outPutdata = outPutdata + issueHTML;                                                 
    	        document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
    	         document.getElementById('spinnerImg').innerHTML =''; 
    	         isOpenOrder = data.isOpenOrder;
    	         if(typeof(isOpenOrder) == "undefined"){
    	        	 isOpenOrder = "";
    	         }
    	         var errorMsg = data.errorMsg;
    	         if(typeof(errorMsg) == "undefined"){
    	        	 errorMsg = "";
    	         }
    	         errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
    	         document.getElementById('ERROR_MSG').innerHTML = errorHTML;
    	         document.form1.ITEM.select();
    			 document.form1.ITEM.focus();
    	          	     
    	   }

    	function getTable(){
    	            return '<TABLE id="tabledata" WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
    	                    '<TR BGCOLOR="#000066">'+
    	                    '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
    	                    '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
    	                    '<TH><font color="#ffffff" align="left"><b>Order Qty</TH>'+
    	                    '<TH><font color="#ffffff" align="left"><b>Balance Qty</TH>'+
    	                    '<TH><font color="#ffffff" align="left"><b>Issued Qty</TH>'+
    	                    '<TH><font color="#ffffff" align="left"><b>Scan Qty</TH>'+
    	                    '</TR>';
    	                
    	}
    		
    	   
    	document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';


  
    	 function onIssue(){
    		  var orderNO = document.form1.DONO.value;         	
        	  var loc = document.form1.LOC_ID.value;
        	  
        	    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
        	    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
        	    var urlStr = "/track/DOMultiPickIssueServlet";
        	    
        	    // Call the method of JQuery Ajax provided
        	    $.ajax({type: "POST",url: urlStr, data: {DONO:orderNO,LOC_ID :loc ,Submit: "PROCESS_MULTI_ISSUE",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
    	 }             
    	</SCRIPT>    

</FORM>
</HTML>
	
<%@ include file="footer.jsp" %>
