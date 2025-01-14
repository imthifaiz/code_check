<div id="payTermsModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="payTermsForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Payment Terms</h4>
		      	</div>
		      	<div class="modal-body">
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">Payment Terms</label>
			        	<div class="col-sm-6">
			        		<input class="form-control" name="PAYMENT_TERMS" id="payment_terms" type="text" value=""> 
			        	</div>
		      		</div>
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">Number of Days</label>
			        	<div class="col-sm-6">
			        		<input class="form-control" name="NO_DAYS" id="no_days" type="text" value="" MAXLENGTH=10> 
			        	</div>
		      		</div>
		      	</div>
		      	<div class="modal-footer">
			      	<button id="btnBillOpen" type="button" class="btn btn-success" onClick="onpayTermsAdd();">Save</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			     </div>
			</div>
		</form>
	</div>
</div>
<script>
function onpayTermsAdd(){
	var PAYMENT_TERMS = document.payTermsForm.PAYMENT_TERMS.value;
	var NO_DAYS = document.payTermsForm.NO_DAYS.value;
	
	if(PAYMENT_TERMS == "" || PAYMENT_TERMS == null){
		alert("Please enter Payment Terms");
		document.payTermsForm.PAYMENT_TERMS.focus();
		return false;
	}
	
	if(NO_DAYS == "" || NO_DAYS == null){
		alert("Please enter No Days");
		document.payTermsForm.NO_DAYS.focus();
		return false;
	}

	if(isNaN(NO_DAYS)) {
		alert("Please enter valid No Days");
		document.payTermsForm.NO_DAYS.focus(); 
		return false;
	}
	
	var data = $("form[name ='payTermsForm']").serialize();
	
	var urlStr = "/track/PaymentTermsServlet?ACTION=ADD_PAYMENT_TERMS";
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
				document.payTermsForm.reset();
				$('#payTermsModal').modal('toggle');
				payTermsCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}
</script>