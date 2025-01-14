
<div id="create_employee_type" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h3 class="modal-title">Add Employee Type</h3>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" name="emptypeformpopup">
					<input type="hidden" class="form-control" id="ukeyet" name="ukeyet" value="">
					<input type="hidden" class="form-control" id="tri" name="tri" value="input">
					<div class="form-group">
						<label class="control-label col-form-label col-sm-3 required">Employee Type</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="emptypepopup" name="emptypepopup"  maxlength="50">
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-3 required">Employee Type Description</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="emptypedescpopup" name="emptypedescpopup"  maxlength="100">
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 txn-buttons">
						<!-- 	<button type="button" class="btn btn-success"  onclick="create_emptype()">Save</button> -->
							<button type="button" class="btn btn-success" onclick="create_emptype()">Save and Close</button>
							<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script>
	$(".table tbody").on('click','.emptypepopup',function(){	
		 var obj = $(this);
		 var timestamp = new Date().getUTCMilliseconds();
		 kayid = "key"+timestamp;
		 $(obj).closest('td').attr('id', kayid); 
		 $("input[name ='ukeyet']").val(kayid);
		 $("input[name ='tri']").val("table");
	});
	
	/* $( "#emptypepopup" ).click(function() {
		  alert( "Handler for .click() called." );
	});
	
	var something = document.getElementById('emptypepopup');
	something.style.cursor = 'pointer';
	something.onclick = function() {
	    alert("somthing");
	}; */
	
	function create_emptype() {
	
		if ($('#create_employee_type #emptypepopup').val() == "") {
			alert("Please enter employee type.");
			$('#create_employee_type #emptypepopup').focus();
			return false;
		}
		
		if ($('#create_employee_type #emptypedescpopup').val() == "") {
			alert("Please enter employee type description.");
			$('#create_employee_type #emptypedescpopup').focus();
			return false;
		}
		
		
		var formData = $('form[name="emptypeformpopup"]').serialize();
		$.ajax({
			type : 'post',
			url : "/track/EmployeeTypeServlet?Submit=Savepopup",
			dataType : 'json',
			data : formData,//{key:val}
			success : function(data) {
				if (data.STATUS == "FAIL") {		                               
					alert(data.MESSAGE);
				}else{
					var ukey = document.emptypeformpopup.ukeyet.value;
					ukey = "#"+ukey;
					if(data.TRI == "table"){
						$(ukey).parents("tr").find('input[name="employeetype"]').val(data.EMPLOYEETYPE);
						$(ukey).parents("tr").find('input[name="employeetypeid"]').val(data.EMPLOYEETYPEID);
					}else{
						$('input[name="employeetype"]').val(data.EMPLOYEETYPE);
						$('input[name="employeetypeid"]').val(data.EMPLOYEETYPEID);
					} 
					$('#create_employee_type').modal('toggle');
				}
			},
			error : function(data) {

				alert(data.responseText);
			}
		});
		return false;
	}
	
</script>
