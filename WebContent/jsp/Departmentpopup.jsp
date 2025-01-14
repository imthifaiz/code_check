
<div id="create_department" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h3 class="modal-title">Add Department</h3>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" name="departmentformpopup">
					<input type="hidden" class="form-control" id="ukeylt" name="ukeylt" value="">
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-3 required">Department</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" name="departmentlt" maxlength="100" placeholder="Enter Department">
						</div>
					</div>

					<div class="row">
						<div class="col-sm-12 txn-buttons">
							<button type="button" class="btn btn-success" onclick="create_department()">Save and Close</button>
							<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script>

$(document).ready(function(){

	$(".table tbody").on('click','.departmentpopup',function(){	
		 var obj = $(this);
		 var timestamp = new Date().getUTCMilliseconds();
		 kayid = "key"+timestamp;
		 $(obj).closest('td').attr('id', kayid); 
		 $("input[name ='ukeylt']").val(kayid);
	});
	
});
	function create_department() {
		
		var salarytype=$('input[name = "departmentlt"]').val();
		if(salarytype == ""){
			alert("Please enter Department.");
			document.departmentformpopup.departmentlt.focus();
			return false;
		}
		
		
		var formData = $('form[name="departmentformpopup"]').serialize();
		$.ajax({
			type : 'post',
			url : "/track/HrDepartmentServlet?Submit=Savepopup",
			dataType : 'json',
			data : formData,//{key:val}
			success : function(data) {
				if (data.STATUS == "FAIL") {		                               
					alert(data.MESSAGE);
				}else{
					document.departmentformpopup.reset();
					$('input[name="department"]').val(data.DEPARTMENT);
					$('input[name="departmentid"]').val(data.DEPARTMENTID);
					$('#create_department').modal('toggle');
					departmentCallback(data);
				}
			},
			error : function(data) {

				alert(data.responseText);
			}
		});
		return false;
	}
	
</script>
