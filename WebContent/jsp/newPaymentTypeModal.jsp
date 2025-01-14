<div id="paymentTypeModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="paymentTypeForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Payment Type</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Payment Type:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="PAYMENTTYPE" id="PAYMENTTYPE" type="text" value=""> 
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
	var paymenttype = document.paymentTypeForm.PAYMENTTYPE.value;
	if(paymenttype=="" || paymenttype.length==0 ) {
		alert("Enter Payment Mode");
		document.getElementById("PAYMENTTYPE").focus();
		return false;
	}
	    
	var data = $("form[name ='paymentTypeForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_PAYMENT_TYPE";
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
				document.paymentTypeForm.reset();
				$('#paymentTypeModal').modal('toggle');
				paymentTypeCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}
</script>