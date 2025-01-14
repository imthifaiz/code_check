<div id="paymentModeModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="paymentModeForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Payment Mode</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Payment Mode:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="PAYMENTMODE" id="PAYMENTMODE" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onPaymentTypeAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>
function onPaymentTypeAdd(){
	var paymenttype = document.paymentModeForm.PAYMENTMODE.value;
	if(paymenttype=="" || paymenttype.length==0 ) {
		alert("Enter Payment Mode");
		document.getElementById("PAYMENTMODE").focus();
		return false;
	}
	    
	var data = $("form[name ='paymentModeForm']").serialize();
	
	var urlStr = "/track/PaymentModeMst?action=CREATE_PAYMENT_MODE";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : data,
		dataType : "json",
		success : function(data) {
			if (data.STATUS == "99") {		                               
				alert(data.MESSAGE);
			}
			else{
				document.paymentModeForm.reset();
				$('#paymentModeModal').modal('toggle');
				paymentTypeCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}
</script>