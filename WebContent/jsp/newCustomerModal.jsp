<div id="customerModal" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-lg">	
	  <form name="customerForm" method="post" >
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">New Customer</h4>
	      </div>
	      <div class="modal-body">
	        <div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2 required">Customer Id:</label>
	        	<div class="col-sm-4">
		        	<div class="input-group">   
		        		<input class="form-control" name="CUST_CODE" id="CUST_CODE" type="text" 
		        			value="" onchange="checkitem(this.value)"> 
	        			<span class="input-group-addon"  onClick="getNextCustomer();">
				   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
				   		 		<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i>
				   		 	</a>
			   		 	</span>
		   		 	</div>
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2 required">Customer Name:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="CUST_NAME" id="CUST_NAME" type="text" value=""> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">GST No.:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="RCBNO" id="RCBNO" type="text" value=""> 
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Customer Type:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">    
		        		<input class="form-control" name="CUSTOMER_TYPE_ID" id="CUSTOMER_TYPE_ID" type="text" value=""> 
		        		<span class="input-group-addon" onClick="javascript:popUpWin('customertypelistsave.jsp?CUSTOMER_TYPE_ID='+customerForm.CUSTOMER_TYPE_ID.value+'&TYPE=CUSTMODAL');"> 	
				   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Type Details">
				   		 		<i class="glyphicon glyphicon-log-in" style="font-size: 15px;"></i>
				   		 	</a>
			   		 	</span>
		   		 	</div>
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Contact Name:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="CONTACTNAME" id="CONTACTNAME" type="text" value=""> 
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Designation:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="DESGINATION" id="DESGINATION" type="text" value=""> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Telephone:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="TELNO" id="TELNO" type="text" value=""> 
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Mobile:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="HPNO" id="HPNO" type="text" value=""> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Fax:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="FAX" id="FAX" type="text" value=""> 
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Email:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="EMAIL" id="EMAIL" type="text" value=""> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Unit No:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="ADDR1" id="ADDR1" type="text" value=""> 
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Building:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="ADDR2" id="ADDR2" type="text" value=""> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Street:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="ADDR3" id="ADDR3" type="text" value=""> 
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">City:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="ADDR4" id="ADDR4" type="text" value=""> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">State:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="STATE" id="STATE" type="text" value=""> 
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Country:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="COUNTRY" id="COUNTRY" type="text" value=""> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Postal Code:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="ZIP" id="ZIP" type="text" value=""> 
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Remarks:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="REMARKS" id="REMARKS" type="text" value=""> 
	        	</div>
        	</div>
        	
        	<div class="row form-group">
	        	<label class="col-form-modal-label col-sm-2">Payment Type:</label>
	        	<div class="col-sm-4">
	        		<div class="input-group">
		        		<input class="form-control" name="PAYTERMS" id="PAYTERMS" type="text" value="" readonly>
		        		<span class="input-group-addon"  onClick="javascript:popUpWin('list/paymenttypelist_save.jsp?paymenttype='+customerForm.PAYTERMS.value+'&PAYTYPE=CUSTMODAL');">
					   		<a href="#" data-toggle="tooltip" data-placement="top" title="Payment Type">
					   		<i class="glyphicon glyphicon-log-in" style="font-size: 15px;"></i></a>
				   		</span>
			  		</div>
	        	</div>
	        	
	        	<label class="col-form-modal-label col-sm-2">Days:</label>
	        	<div class="col-sm-4">
	        		<input class="form-control" name="PMENT_DAYS" id="PMENT_DAYS" type="text" value=""> 
	        	</div>
        	</div>
	      </div>
	      <div class="modal-footer">
	      	<button id="btnBillOpen" type="button" class="btn btn-success" onClick="onCustomerAdd();">Save</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	    </form>
	  </div>
	</div>
	<script>
	function onCustomerAdd(){
	   var CUST_CODE = document.customerForm.CUST_CODE.value;
	   var CUST_NAME = document.customerForm.CUST_NAME.value;
	   if(CUST_CODE == "" || CUST_CODE == null) {
		   alert("Please Enter Customer ID");
		   document.customerForm.CUST_CODE.focus(); 
		   return false; 
	   }	   
	   if(CUST_NAME == "" || CUST_NAME == null) {
	   	alert("Please Enter Customer Name"); 
	   	document.customerForm.CUST_NAME.focus();
	   	return false; 
	   }
	   if(!IsNumeric(document.customerForm.PMENT_DAYS.value))
	   {
	     alert(" Please Enter Days In Number");
	     document.customerForm.PMENT_DAYS.focus();  
	     document.customerForm.PMENT_DAYS.select(); 
	     return false;
	   }
	   var data = $("form[name ='customerForm']").serialize();
	   var urlStr = "/track/MasterServlet?action=CREATE_CUSTOMER";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : data,
				dataType : "json",
				success : function(data) {
					if (data.STATUS == "FAIL") {		                               
						alert(data.MESSAGE);
					}
					else{
						$('#customerModal').modal('toggle');
						customerCallback(data);	/* Define this method in parent page*/
					}						
				}
		});	
		return false;
	}
	
	function checkitem(aCustCode)
	{	
		 if(aCustCode=="" || aCustCode.length==0 ) {
			alert("Enter Customer ID!");
			document.getElementById("CUST_CODE").focus();
			return false;
		 }else{ 
			var urlStr = "/track/MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					CUST_CODE : aCustCode,
	                USERID : document.form1.username,/* create this field in parent page*/
					PLANT : document.form1.plant,/* create this field in parent page*/
					ACTION : "CUSTOMER_CHECK"
				},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {		                               
							alert("Customer Already Exists");
							document.getElementById("CUST_CODE").focus();
							return false;
						}
						else
							return true;
					}
			});	
			return true;
		}
	}
	
	function getNextCustomer(){
		$.ajax( {
			type : "GET",
			url : "/track/MasterServlet?ACTION=GET_CUSTOMER_SEQUENCE",
			async : true,
			dataType : "json",
			contentType: false,
	        processData: false,
	        success : function(data) {
				if (data.ERROR_CODE == "100") {
					document.customerForm.CUST_CODE.value = data.CUSTCODE;
				}
				else{
					alert("Something went wrong. Please try again later.");
				}						
			},
	        error: function (data) {	        	
	        	alert(data.Message);
	        }
		});	
		return false;
	}
</script>