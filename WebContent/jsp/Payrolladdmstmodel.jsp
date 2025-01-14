<div id="PayaddmstModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="paymentTypeForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Payroll Addition Master</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<form id="formaddmstpopup" name="formaddmstpopup" method="post">
						<input type="text" name="pop_up_id" hidden>
						<input type="text" name="pop_up_parent" hidden>
						<div class="form-group row">
							<label class="control-label col-form-label col-sm-6 required">Addition Name</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="pop_add_name" maxlength="100">
							</div>
						</div>
						
						<div class="form-group row">
							<label class="control-label col-form-label col-sm-6 required">Addition Description</label>
							<div class="col-sm-6">
								<input type="text" class="form-control" name="pop_add_description">						
							</div>
						</div>
						
						
						<div class="form-group row">
							<label class="control-label col-form-label col-sm-6">Is Loan or Advance?</label>
							<div class="col-sm-6 row">
								<input type="hidden" name="pop_isrecoverable" value="0">
								<input type="checkbox" id="pop_recoverable" name="recoverable" onclick="pop_setrecoverable(this)">
							</div>
						</div>
						
						<div class="form-group row">
							<label class="control-label col-form-label col-sm-6">Is Claim?</label>
							<div class="col-sm-6 row">
								<input type="hidden" name="pop_isclaim" value="0">
								<input type="checkbox" id="pop_claim" name="claim" onclick="pop_setclaim(this)">
							</div>
						</div>
						
					</form>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onpoppayaddmstadd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>
function onpoppayaddmstadd(){
	var add_name=$('input[name = "pop_add_name"]').val();
	var pop_up_id=$('input[name = "pop_up_id"]').val();
	var pop_up_parent=$('input[name = "pop_up_parent"]').val();
	
	if(add_name == ""){
		alert("Please enter addition name.");
		document.formaddmstpopup.pop_add_name.focus();
		return false;
	}
	var urlStr = "/track/HrPayrollAdditionMstServlet?Submit=SavePopup";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			pop_add_name:$('input[name = "pop_add_name"]').val(),
			pop_add_description:$('input[name = "pop_add_description"]').val(),
			pop_isrecoverable:$('input[name = "pop_isrecoverable"]').val(),
			pop_isclaim:$('input[name = "pop_isclaim"]').val()
		},
		dataType : "json",
		success : function(data) {
			console.log(data);
			if (data.STATUS == "NOT OK") {		                               
				alert(data.MESSAGE);
			}else{
				if(data.PAGE == pop_up_parent){
					$('#'+pop_up_id).val(data.MESSAGE);
					$('#PayaddmstModal').modal('toggle');
				}else{
					$('#PayaddmstModal').modal('toggle');
				}
			}						
		}
	});	
	return false;
}

function pop_setrecoverable(obj) {
	if ($(obj).is(":checked")) {
		$('input[name = "pop_isrecoverable"]').val("1");
		$("input[name ='pop_isclaim']").val("0");
		$("input[name ='claim']").prop('checked', false);
	} else {
		$('input[name = "pop_isrecoverable"]').val("0");
	}
}

function pop_setclaim(obj) {
	if ($(obj).is(":checked")) {
		$("input[name ='pop_isclaim']").val("1");
		$("input[name ='pop_isrecoverable']").val("0");
		$("input[name ='recoverable']").prop('checked', false);
	} else {
		$("input[name ='pop_isclaim']").val("0");
	}
}


</script>