<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp" %>
<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%>


<%
String title = "Sales Order Check";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'multiPickIssue', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
</script>
<%
String PLANT = "";
StrUtils strUtils = new StrUtils();
PLANT= session.getAttribute("PLANT").toString();
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

<form class="form-horizontal" name="form1" method="post" action="/track/DOMultiPickIssueServlet">
 <div class="form-group">
<label class="control-label col-sm-4" for="OutBound Order">Sales Order:</label>
<div class="col-sm-4">
<div class="input-group">   
    <input name="DONO" type="TEXT" id="DONO"  size="20" MAXLENGTH=20 class="form-control" value="">
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('list/ob_order_list.jsp?DONO='+form1.DONO.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Sales Order Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
      	      </div>
 <div class=form-inline>
   <div class="col-sm-2">
     <button type="button" class="Submit btn btn-default" name="action" value="View" onClick="onGo();"><b>View</b></button>&nbsp;&nbsp;
    </div>
      </div>
         </div> 
    
  
   <div class="form-group">        
      <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox" class="form-check-input" checked name="DEF_QTY1" onclick="return Checkdefqty()"><b>Default Qty to 1</b></label>
      </div>
    <div class="form-inline">
     <div class="col-sm-2">
      <label class="checkbox-inline">      
        <input type = "checkbox"  class="form-check-input" checked name="DEF_NOBATCH" onclick="return Checkdefbatch()"><b>Default to NOBATCH</b></label>
      </div>
      </div>
      </div>
      
 <div class="form-group">
<label class="control-label col-sm-1" for="Location">Location:</label>
<div class="col-sm-2">
<div class="input-group">   
    <input name="LOC_ID" type="TEXT" id="LOC_ID" value="" style="width: 100%" MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){focusitem();}" class="form-control">
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('LocListView.jsp?LOC_ID='+ form1.LOC_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
      	      </div>
<div class="form-inline">
<label class="control-label col-sm-1" for="Scan Product ID">Scan Prod ID:</label>
<div class="col-sm-2">          
<INPUT class="form-control" name="ITEM" type="TEXT" id="ITEM" value="" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateProductAgainstPickList(0);}"
			style="width: 100%" MAXLENGTH=50>
		      </div>
		      </div>
		      
<div class="form-inline">
<label class="control-label col-sm-1" for="Description">Description:</label>
<div class="col-sm-2">          
<INPUT class="form-control" name="ITEMDESC" id="ITEMDESC" type="TEXT" value="" style="width: 100%" maxlength="50">
		      </div>
		      </div>
		      
		      
<div class="form-inline">
<label class="control-label col-sm-1" for="Batch">Batch:</label>
<div class="col-sm-2">          
<INPUT class="form-control" name="BATCH" id="BATCH" type="TEXT" value="NOBATCH" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateANDPick();}"
			style="width: 100%" MAXLENGTH=40>
		      </div>
		      </div>
		      </div>
		      
<div class="form-group">
<label class="control-label col-sm-1" for="Qty">Qty:</label>
<div class="col-sm-1">          
<INPUT class="form-control" name="QTY" id="QTY" type="TEXT" value="1" size="4" maxlength="10" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){updatepickqty();}" > 
<!-- <input type = "text"  size = "2" READONLY name = "UOM"  style= "border: 0px;background-color:#dddddd ;"/>-->
		      </div>
		      
		      
<div class="form-inline">
<div class="col-sm-offset-2 col-sm-2">
<button type="button" class="Submit btn btn-default" onClick="window.location.href='inPremises.jsp'"><b>Back</b></button>&nbsp;&nbsp;
<button type="button" class="Submit btn btn-default"  name="action"
			value="Scan" onclick="return validateProductAgainstPickList(1)"><b>Scan</b></button>&nbsp;&nbsp;
          
</div>
</div>
</div>
<br>
<div id = "ERROR_MSG"></div>
<br>
<div id="VIEW_RESULT_HERE"></div>
<div id="spinnerImg" ></div>

      

 <SCRIPT LANGUAGE="JavaScript">
      
            
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
    							alert("Not a valid product to pick against the Sales Order OR Exceeded the ordered QTY");
    							document.form1.ITEM.value = "";
    							document.form1.ITEM.focus();
    							return ;
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
      function focusitem()
		{
			document.form1.ITEM.focus();
		}
      

		function updatepickqty(){
			var table=document.getElementById("tabledata");
			var len = table.rows.length;
			var scanitem = trim((document.getElementById("ITEM").value).toUpperCase());
			var scanqty = parseInt(document.getElementById("QTY").value);
			for ( var i = 0; i < len-1; i++) {
				var item = (document.getElementById("item_" + i).innerText).toUpperCase() ;
				var item = trim(item);
				var orderqty = parseInt(document.getElementById("qtyor_" + i).innerText) ;
				var balqty = parseInt(document.getElementById("balqty_" + i).innerText) ;
				var pickqty = parseInt(document.getElementById("qtypick_" + i).innerText) ;
				
				if(scanitem == item){
					if(scanqty>balqty)
					{
						alert("Scan Qty Exceeds Order Qty for the Product Scanned.");
						document.form1.ITEM.select();
		    			document.form1.ITEM.focus();
						return false;
					}
					document.getElementById("balqty_" + i).innerText=balqty-scanqty;
					document.getElementById("qtypick_" + i).innerText=pickqty+scanqty;
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
    	    var location = document.form1.LOC_ID.value; 
    	    if(orderNO == "" ){
    	    	alert("Select Order No");
    	    	return false;
    	    }
    	    
    	    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    	    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    	    var urlStr = "/track/DOMultiPickIssueServlet";
    	    
    	    // Call the method of JQuery Ajax provided
    	    $.ajax({type: "POST",url: urlStr, data: {DONO:orderNO,Submit: "GET_DODET_DETAILS_DOCHECK",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
    	  
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
    	      //  var issueHTML = '<br><br><TABLE WIDTH="10%"  border="0" cellspacing="1" cellpadding = 2 align = "center"> <TR><TD><INPUT class="button" type="button" name="action"'
    	        				//+ 'value="Issue" onClick="onIssue();"></TD><TR></TABLE>';
    	        				//outPutdata = outPutdata + issueHTML;                                                 
    	        document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
    	         document.getElementById('spinnerImg').innerHTML ='';    	         
    	         var errorMsg = data.errorMsg;
    	         if(typeof(errorMsg) == "undefined"){
    	        	 errorMsg = "";
    	         }
    	         errorHTML = "<table width= '30%' align = 'center' border='0' cellspacing='0' cellpadding='0' bgcolor='#eeeeee'>"+errorMsg+"</table>";
    	         document.getElementById('ERROR_MSG').innerHTML = errorHTML;
    	         document.form1.ITEM.select();
    			 document.form1.ITEM.focus();
    	          	     
    	   }

    	function getTable(){
    	            return '<TABLE  class="table" id="tabledata" WIDTH="100%" align = "center">'+
    	                   '<thead style="background:#eaeafa">'+
                           '<TR>'+
                           '<TH><font align="left"><b>Product ID</TH>'+
                           '<TH><font align="left"><b>Description</TH>'+
                           '<TH><font align="left"><b>Order Qty</TH>'+
                           '<TH><font align="left"><b>Balance Qty</TH>'+
                           '<TH><font align="left"><b>Scan Qty</TH>'+
                           '</TR>'+
                           '</thead>';
                    
    	                
    	}
    		
    	   
    	document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';


  
    	            
    	</SCRIPT>    
</form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>