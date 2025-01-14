<div id="create_extended_modalcoa" class="modal fade" role="dialog">

	<div class="modal-dialog">

		<div class="modal-content">
			<form class="form-horizontal" name="create_extended_formcoa">
			<input type="hidden" id="acc_type_extended_id" name="acc_type_extended_id">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h3 class="modal-title">Create Account Type</h3>
				</div>
				<div class="modal-body">	
					<div id="ealert" class="ealert alert-danger">
					  test
					</div>
					<br>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Account Group</label>				
	                     <div class="col-sm-6">                     
								<select class="form-control" name="acc_type_extended" id="acc_type_extended" style="width: 100%"></select>
							</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 required">Account
							Type</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="acc_det_type_extended"
								name="acc_det_type_extended">
						</div>
					</div>
				</div>				
				<div class="modal-footer">
					<button type="button" class="btn btn-success" id="create_user"
						onclick="create_extendend_account()">Save and Close</button>
					<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
				</div>
			</form>
		</div>

	</div>

</div>
<script>
var ExtendAccountTypeList;
$(document).ready(function() {
	$("#create_extended_modalcoa #ealert").hide();
	exendloadAccount();
	function exendloadAccount()
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
				ExtendAccountTypeList=dataitem.results;
				var myJSON = JSON.stringify(ExtendAccountTypeList);
				
				$("#create_extended_modalcoa #acc_type_extended").select2({
					  data: ExtendAccountTypeList
					})
				
				
			}
		});
	}
	function selectExtAccType(id)
	{
		$("#create_extended_modalcoa #acc_type_extended").val(id).trigger('change');
		//$('#acc_type').val(id);
	}
	$("#create_extended_modalcoa").on('show.bs.modal', function(){
		$("#create_extended_modalcoa #ealert").hide();
		$('#create_extended_modalcoa #ealert').html("Account type already exist.");
		$("#create_extended_modalcoa #acc_det_type_extended").val("");
		var acctype=$('#create_account_modalcoa #acc_type').val();
		//AccDetailType(acctype);
		console.log("Extend type:"+acctype);
		selectExtAccType(acctype);
	});
});

function create_extendend_account()
{
	var extaccid=$("#create_extended_modalcoa #acc_type_extended").val();
	var extdettype=$("#create_extended_modalcoa #acc_det_type_extended").val();
	
	if(extaccid=="")
		{
			alert("please fill account type");
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
					$("#create_extended_modalcoa #ealert").show();
		    		$('#create_extended_modalcoa #ealert').html("Account type already exist.");
					return false;
				}
			else
				{
				var formData = $('form[name="create_extended_formcoa"]').serialize();
				$.ajax({
					type : 'post',
					url : "/track/ChartOfAccountServlet?action=add_extended",
					dataType : 'json',
					data : formData,//{key:val}
					success : function(data) {
						if (data.STATUS == "FAIL") {		                               
							alert(data.MESSAGE);
						}else{
							$('#create_extended_modalcoa').modal('toggle');
							var acctype=$('#create_account_modalcoa #acc_type').val();
							AccDetailTypecoa(acctype);
							//createDetail(data.EXTENDEDID,data.EXTENDEDTYPE);
							$("#create_account_modalcoa #acc_det_type").typeahead('val', data.EXTENDEDTYPE);
							$("#create_account_modalcoa #acc_det_type_id").val(data.EXTENDEDID);
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