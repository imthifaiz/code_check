<div id="edit_create_extended_modal" class="modal fade" role="dialog">

	<div class="modal-dialog">

		<div class="modal-content">
			<form class="form-horizontal" name="edit_create_extended_form">
			<input type="hidden" id="edit_acc_type_extended_id" name="edit_acc_type_extended_id">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h3 class="modal-title">Create Account Type</h3>
				</div>
				<div class="modal-body">	
					<div id="editealert" class="alert alert-danger">
					  test
					</div>
					<br>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Account Group</label>				
	                     <div class="col-sm-6">                     
								<select class="form-control" name="acc_type_extended" id="edit_acc_type_extended" style="width: 100%"></select>
							</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 required">Account
							Type</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="edit_acc_det_type_extended"
								name="acc_det_type_extended">
						</div>
					</div>
				</div>				
				<div class="modal-footer">
					<button type="button" class="btn btn-success" id="create_user"
						onclick="edit_create_extendend_account()">Save and Close</button>
					<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
				</div>
			</form>
		</div>

	</div>

</div>
<script>
var EditExtendAccountTypeList;
$(document).ready(function() {
	$("#editealert").hide();
	editexendloadAccount();
	function editexendloadAccount()
	{
		$.ajax({
			type : "POST",
			url: '/track/ChartOfAccountServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getAccountType",
			},
			success : function(dataitem) {
				EditExtendAccountTypeList=dataitem.results;
				var myJSON = JSON.stringify(EditExtendAccountTypeList);
				
				$("#edit_acc_type_extended").select2({
					  data: EditExtendAccountTypeList
					})
				editselectExtAccType(3);
				
			}
		});
	}
	function editselectExtAccType(id)
	{
		$("#edit_acc_type_extended").val(id).trigger('change');
		//$('#acc_type').val(id);
	}
	$("#edit_create_extended_modal").on('show.bs.modal', function(){
		$("#editealert").hide();
		$('#editealert').html("Account type already exist.");
		$("#edit_acc_det_type_extended").val("");
		var acctype=$('#eacc_type').val();
		console.log("Acc type:"+acctype);
		editselectExtAccType(acctype);
	});
});

function edit_create_extendend_account()
{
	var extaccid=$("#edit_acc_type_extended").val();
	var extdettype=$("#edit_acc_det_type_extended").val();
	
	if(extaccid=="")
		{
			alert("please fill account group");
			return false;
		}
	else if(extdettype=="")
		{
			alert("please fill account detail type");
			return false;
		}
	$.ajax({
		type : 'post',
		url : "/track/ChartOfAccountServlet?action=checkDetailAccountCount",
		dataType : 'json',
		data : 
			{
				detname:extdettype
			},
		success : function(data) {
			var count=parseInt(data);
			if(count>0)
				{
					$("#editealert").show();
		    		$('#editealert').html("Account type already exist.");
					return false;
				}
			else
				{
				var formData = $('form[name="edit_create_extended_form"]').serialize();
				$.ajax({
					type : 'post',
					url : "/track/ChartOfAccountServlet?action=add_extended",
					dataType : 'json',
					data : formData,//{key:val}
					success : function(data) {
						if (data.STATUS == "FAIL") {		                               
							alert(data.MESSAGE);
						}else{
							$('#edit_create_extended_modal').modal('toggle');
							var acctype=$('#eacc_type').val();
							EditAccDetailType(acctype);
							//createDetail(data.EXTENDEDID,data.EXTENDEDTYPE);
							$("#eacc_det_type").typeahead('val', data.EXTENDEDTYPE);
							$("#eacc_det_type_id").val(data.EXTENDEDID);
						}
					},
					error : function(data) {

						alert(data.responseText);
					}
				});
				return false;
				}
		},
		error : function(data) {

			alert(data);
		}
	});
	}
</script>