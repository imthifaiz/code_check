<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp" %>

<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%><html>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Pick/Issue By Transfer Order</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
var isOpenOrder = false;
function popUpWin(URL) {
  subWin = window.open(URL, 'multiPickIssue', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
</script>
<%
String PLANT = "",fromWareHouse="",toWareHouse="",TRANSACTIONDATE="";
StrUtils strUtils = new StrUtils();
PLANT= session.getAttribute("PLANT").toString();
TRANSACTIONDATE = strUtils.fString(request.getParameter("TRANSACTIONDATE"));
DateUtils _dateUtils = new DateUtils();
String curDate =_dateUtils.getDate();
if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
%>
<%@ include file="body.jsp" %>
<FORM name="form" method="post" action="/track/TransferOrderServlet">

  <br>
   <CENTER>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11">
          <FONT color="#ffffff">Multi Pick/Issue By Transfer Order</FONT>&nbsp;
        </TH></table>
  <br>
  

 <TABLE WIDTH="70%" border="0" cellspacing="1" cellpadding="2"
	align="center" bgcolor="#dddddd">
	<tr>
	<td>
	<TABLE WIDTH="50%" border="0" cellspacing="0" cellpadding="0"
	align="center" >
	<tr>
		<TH ALIGN="right" >Transfer Order :</TH>
		<TD ALIGN="left" >
		 <INPUT type="TEXT" size="20" MAXLENGTH="20" name="TONO" id="TONO" value="" />
          <a href="#" onClick="javascript:popUpWin('to_list_do.jsp?TONO='+form.TONO.value);">
                           <img src="images/populate.gif" border="0"/>
          </a>
         </td>		
		<TD ><INPUT  type="button" name="action"
			value="View" onClick="onGo();">
			</tr>
			</TABLE>
			<div id="Locations"></div>
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
		<TH ALIGN="right" >Scan Product ID : &nbsp;&nbsp;</TH>
		<TD ALIGN="left" ><INPUT name="ITEM" type="TEXT" id="ITEM"
			value="" size="20" MAXLENGTH=50 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateProductAgainstPickList(0);}">
			<!-- <a href="#" 
			onClick="javascript:popUpWin('OutBoundPickingItemList.jsp?ITEM='+form.ITEM.value+'&DONO='+form.DONO.value);"><img
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
		 <input type = "text"  size = "3" READONLY name = "UOM"  style= "border: 0px;background-color:#dddddd ;"/>
		</td>
		<td ALIGN="left" ><input  type="button" name="action"
			value="Next" onclick="return validateProductAgainstPickList(1)"></td>
		</tr>
</table>
<br>
		<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
			align="left" >
			<tr>
				<TD ALIGN="left" ><b>Transaction Date : </b><INPUT name="TRANSACTIONDATE" id="TRANSACTIONDATE"
					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" MAXLENGTH="80" readonly="readonly">
				<a href="javascript:show_calendar('form.TRANSACTIONDATE');"
			   onmouseover="window.status='Date Picker';return true;"
			   onmouseout="window.status='';return true;"> <img
			   src="images\show-calendar.gif" width="24" height="22" border="0" /></a>
	          </TD>
			</tr>
		</table>
</td>
</tr>
<INPUT type = "hidden" name="CUST_NAME" value = ""></TD>
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
      var Locarray = new Array();   
      
      function Checkdefqty()
      {
          if(document.form.DEF_QTY1.checked)
          {
        	  document.getElementById("QTY").value=1;
          }
      }

      function Checkdefbatch()
      {
          if(document.form.DEF_NOBATCH.checked)
          {
        	  document.form.BATCH.value = "NOBATCH";
          }
      }
      
      
      
      function validateProductAgainstPickList(index) {
          
          var chkindex = index;
    		var productId = document.form.ITEM.value;
    		var tono = document.form.TONO.value;
    	/*	var location = document.form.LOC_ID.value; 
    	    if(location == "" || location.length==0 ){
    	    	alert("Select Location");
    	    	document.form.LOC_ID.focus();
    	    	return false;
    	    }*/
    		
    		if(document.form.ITEM.value=="" || document.form.ITEM.value.length==0 ) {
    			alert("Enter Product ID!");
    			document.form.ITEM.focus();
    			return false;
    		}else{ 	
    			var urlStr = "/track/TransferOrderHandlerServlet";
    			$.ajax( {
    				type : "POST",
    				url : urlStr,
    				data : {
    					ITEM : productId,
    					TONO  : tono,
    					PLANT : "<%=PLANT%>",
    					ACTION : "VALIDATE_PRODUCT_AGAINST_PICKLIST"
    					},
    					dataType : "json",
    					success : function(data) {
    						
    						if (data.status == "100") {
    							var resultVal = data.result;
    							document.form.ITEM.value = resultVal.item;
    							document.form.ITEMDESC.value = resultVal.discription;
    							document.form.UOM.value = resultVal.uom;
    							//document.form.BATCH.value = "NOBATCH";
    							if(chkindex == 1){
    								updatepickqty(); 
    							}
    							else{	
    							if(document.form.DEF_QTY1.checked && document.form.DEF_NOBATCH.checked){
        			    			 
        			    			updatepickqty(); 
        			    			
        			    		}
    							else
    							{
    								document.form.BATCH.select();
        							document.form.BATCH.focus();
    							}
    						}
    							
    						} else {
    							alert("Not a valid product to pick against the Transfer Order OR Exceeded the ordered QTY");
    							document.form.ITEM.value = "";
    							document.form.ITEM.focus();
    							return ;
    						}
    					}    					
    				});
    			}
    		
    		
    		}

		function focusitem()
		{
			document.form.ITEM.focus();
		}
      function updatepickqty(){

    	  
    	  if(document.form.ITEM.value=="" || document.form.ITEM.value.length==0 ) {
  			alert("Enter Product ID!");
  			document.form.ITEM.focus();
  			return false;
    	  }
    	  if(document.form.ITEMDESC.value=="" || document.form.ITEMDESC.value.length==0 ) {
    			alert("Enter Product Description!");
    			document.form.ITEMDESC.focus();
    			return false;
      	  }
    	  if(document.form.BATCH.value=="" || document.form.BATCH.value.length==0 ) {
    			alert("Enter Batch!");
    			document.form.BATCH.focus();
    			return false;
      	  }
    	  if(document.form.QTY.value=="" || document.form.QTY.value.length==0 ) {
    			alert("Enter QTY!");
    			document.form.QTY.focus();
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
						document.form.ITEM.select();
		    			document.form.ITEM.focus();
						return false;
					}
					document.getElementById("balqty_" + i).innerText=balqty-scanqty;
					document.getElementById("scan_" + i).innerText=scanedqty+scanqty;
					//document.getElementById("qtyis_" + i).innerText=issueqty+scanqty;
					updatetemptable(); 
					document.form.ITEM.select();
	    			document.form.ITEM.focus();
				}
				
			}
			
		}
      
      function validateANDPick(){
    	  if(document.form.DEF_QTY1.checked){
    		  updatepickqty();
    		  
    		  
  		}
  		else{
  		 document.form.QTY.select();
  		   document.form.QTY.focus();
    	  
  		}
    	  
      }
    		
      
      function onGo(){
            var orderNO = document.form.TONO.value; 
    	    //var location = document.form.LOC_ID.value; 
    	    if(orderNO == "" || orderNO.length==0 ){
    	    	alert("Select OrderNo");
    	    	return false;
    	    }
    	    
    	    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    	    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    	    var urlStr = "/track/TransferOrderHandlerServlet";

    	  
    	    // Call the method of JQuery Ajax provided
    	    $.ajax({type: "POST",url: urlStr, data: {TONO:orderNO,ACTION: "GET_TODET_DETAILS_RANDOMISSUE",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
    	  
    	}

      function updatetemptable()
      {
    	 
    	    var orderNO = document.form.TONO.value;   
    	    var item = document.form.ITEM.value; 
    	    var itemDesc = document.form.ITEMDESC.value;
    	    var batch = document.form.BATCH.value;
    	    var fromloc = document.form.FROM_WAREHOUSE.value;
    	    var toloc = document.form.TO_WAREHOUSE.value;
    	    var qty = document.form.QTY.value;
    	   // if(item == "" || itemDesc == "" || batch == "" ||qty == ""){
    	    //	alert("All fields cannot be empty.");
    	    //	return false;
    	   // }
    	   // document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    	   // document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    	    var urlStr = "/track/TransferOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
					ITEM :item,
					TONO:orderNO,
					ITEMDESC :itemDesc,
					BATCH : batch,
					FROMLOC :fromloc,
					TOLOC :toloc,
					QTY : qty,
					PLANT : "<%=PLANT%>",
					ACTION: "UPDATE_TEMP"
					},
					dataType : "json",
					success : function(data) {
						
						if (data.status == "100") {
							//var resultVal = data.result;
							//document.form.ITEMDESC.value = resultVal.discription;
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
    	var orderNO = document.form.TONO.value;   
  	   // var loc = document.form.LOC_ID.value;
  	  if(orderNO == ""){
	    	alert("Select Order No");
	    	return false;
	    }
			
  	   		document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
 	    	document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
 	   	    var urlStr = "/track/TransferOrderHandlerServlet";
 	    
 	    // Call the method of JQuery Ajax provided
 	   	   $.ajax({type: "POST",url: urlStr, data: {TONO:orderNO,ACTION: "RESET_SCANQTY",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
 	  	
      }


      function onRePrintWithOutBatch(){

    	  debugger;
    	    var flag    = "false";
    	    var TONO    = document.form.TONO.value;
    	    if(TONO != null    && TONO != "") { flag = true;}
    	    if(flag == "false"){ alert("Please Enter Order No"); return false;}
    	    if(isOpenOrder){
    	    	alert("Order has not yet picked/Issued"); 
    	    	return false;
    	    }
    	    else{
    	    document.form.action="/track/DynamicFileServlet?action=printTOWITHOUTBATCH&TONO="+TONO;
    	    document.form.submit();
    	    }
    	}
      

      function onPickIssue()
      {
    	 
    	    var orderNO = document.form.TONO.value; 
			 var tranDate = document.form.TRANSACTIONDATE.value; 
    	    var table=document.getElementById("tabledata");
			var len = table.rows.length;
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
    	    var urlStr = "/track/TransferOrderHandlerServlet";
    	    
    	    // Call the method of JQuery Ajax provided
    	    $.ajax({type: "POST",url: urlStr, data: {TONO:orderNO,TRANSACTIONDATE:tranDate,ACTION: "PROCESS_MULTI_PICKISSUE",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
    	  
      }
    	function callback(data){
        	
    			var outPutdata = getTable();
    			var ii = 0;
    			var loc="",fromloc="",toloc="";
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

    			$.each(data.items, function(i,item){
    				
    				loc= item.LOC;
        		    				
    			});
    			 if(typeof(loc) == "undefined"){
    				 fromloc = "";
    				 toloc = "";
    	         }
    			 else
    			 {
    				Locarray = loc.split(',');
    				fromloc = Locarray[0];
    				toloc = Locarray[1];
    			 }
    	        outPutdata = outPutdata +'</TABLE>';
    	        var issueHTML = '<br><br><TABLE WIDTH="20%"  border="0" cellspacing="1" cellpadding = 2 align = "center"> <TR><TD><INPUT  type="button" name="action" value="Reset Scan QTY" style="height: 25px; width: 100px" onClick="onResetScanqty();">'
        	        			+'&nbsp;&nbsp;<INPUT  type="button" name="action"'
    	        				+ 'value="Issue" style="height: 25px; width: 80px" onClick="onPickIssue();">'
    	        				+ '&nbsp;&nbsp;<INPUT  type="button" name="action" value="Print TO" style="height: 25px; width: 100px" onClick="javascript:return onRePrintWithOutBatch();"></TD><TR></TABLE>';
    	        				outPutdata = outPutdata + issueHTML;                                                 
    	        document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
    	         document.getElementById('spinnerImg').innerHTML =''; 

				var lochtml = '<TABLE WIDTH="50%" border="0" cellspacing="0" cellpadding="0" align="center" ><tr>'
							  +'<th ALIGN = "right" >From Loc:</th>'
	            			  +'<td ><INPUT type = "TEXT" size="20"   class="inactivegry" MAXLENGTH="20" readonly  MAXLENGTH=20 name="FROM_WAREHOUSE" id="FROM_WAREHOUSE" value=""></td>'
	             			  +'<th ALIGN = "right">To Loc:</th>'
	             			  +'<td ><INPUT type="TEXT" size="20"   class="inactivegry" MAXLENGTH="20" readonly name="TO_WAREHOUSE" id="TO_WAREHOUSE"  value=""> </td>'
	            			  +'</tr></table>'
	            document.getElementById('Locations').innerHTML = lochtml;	

	           document.getElementById('FROM_WAREHOUSE').value = fromloc;			
	           document.getElementById('TO_WAREHOUSE').value = toloc;   	            			
     	            	         
    	         var errorMsg = data.errorMsg;
    	         if(typeof(errorMsg) == "undefined"){
    	        	 errorMsg = "";
    	         }

    	         isOpenOrder = data.isOpenOrder;
    	         if(typeof(isOpenOrder) == "undefined"){
    	        	 isOpenOrder = "";
    	         }
    	         
    	         errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
    	         document.getElementById('ERROR_MSG').innerHTML = errorHTML;
    	         document.form.ITEM.select();
    			 document.form.ITEM.focus();
    	          	     
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


  
    	          
    	</SCRIPT>    

</FORM>
</HTML>
	
<%@ include file="footer.jsp" %>
