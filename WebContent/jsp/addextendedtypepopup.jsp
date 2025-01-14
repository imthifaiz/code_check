<div id="create_extended_modal" class="modal fade" role="dialog">

	<div class="modal-dialog">

		<div class="modal-content">
			<form class="form-horizontal" name="create_extended_form">
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
						<label class="control-label col-form-label col-sm-4 required">Account Code</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="acc_code_pre_extended"
								name="acc_code_pre_extended" readonly>
						</div>
						<div class="col-sm-2">
							<input type="text" class="form-control" onchange="checkextendedcode()" id="acc_code_extended"
								name="acc_code_extended">
						</div>
						<div class="col-sm-3">
							<input type="text" class="form-control"  id="acc_code_show_extended"
								name="acc_code_show_extended" readonly>
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
	$("#ealert").hide();
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
				
				$("#acc_type_extended").select2({
					  data: ExtendAccountTypeList
					})
				
				
			}
		});
	}
	function selectExtAccType(id,acode)
	{
		$("#acc_type_extended").val(id).trigger('change');
		//$("input[name ='acc_code_pre']").val(data.CODE);
		$("input[name ='acc_code_pre_extended']").val(acode+"-");
		$.ajax({
			type : "POST",
			url: '/track/ChartOfAccountServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getAccountDETCode",
				Atid:id,
				AccountCode:acode+"-",
			},
			success : function(dataitem) {
				var accountcode = dataitem.results;
				//alert(accountcode);
				$("input[name ='acc_code_extended']").val(accountcode);
				$("input[name ='acc_code_show_extended']").val(acode+"-"+accountcode+"00");
			}
		});
		//$('#acc_type').val(id);
	}
	$("#create_extended_modal").on('show.bs.modal', function(){
		$("#ealert").hide();
		$('#ealert').html("Account type already exist.");
		$("#acc_det_type_extended").val("");
		var acctype=$('#acc_type').val();
		//AccDetailType(acctype);
		var acode = $("input[name ='acc_code_code']").val();
		selectExtAccType(acctype,acode);
	});
/* }); */
$('#acc_type_extended').on('select2:select', function (e) {
    var data = e.params.data;
    selectExtAccType(data.id,data.CODE);
    /* $("input[name ='coamainid']").val(data.MAINACCID);
    $("input[name ='accid']").val(data.id);
    $("input[name ='acc_code_pre']").val(data.CODE);
    $("input[name ='acc_code_show']").val(data.CODE);
    $("input[name ='acc_code_code']").val(data.CODE); */
    	
});

$('#create_account_modal #acc_subAcct').attr('disabled',true);
$('#create_account_modal #acc_subAcct').css("background-color", "#EEEEEE");
});
function create_extendend_account()
{
	var extaccid=$("#acc_type_extended").val();
	var extdettype=$("#acc_det_type_extended").val();
	
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
					$("#ealert").show();
		    		$('#ealert').html("Account type already exist.");
					return false;
				}
			else
				{
				var formData = $('form[name="create_extended_form"]').serialize();
				$.ajax({
					type : 'post',
					url : "/track/ChartOfAccountServlet?action=add_extended",
					dataType : 'json',
					data : formData,//{key:val}
					success : function(data) {
						if (data.STATUS == "FAIL") {		                               
							alert(data.MESSAGE);
						}else{
							$('#create_extended_modal').modal('toggle');
							var acctype=$('#acc_type').val();
							AccDetailType(acctype);
							//createDetail(data.EXTENDEDID,data.EXTENDEDTYPE);
							$("#acc_det_type").typeahead('val', data.EXTENDEDTYPE);
							$("#acc_det_type_id").val(data.EXTENDEDID);
							$("input[name ='acc_code']").val('01');
							$("input[name ='acc_code_show']").val($("input[name ='acc_code_pre']").val()+'-'+data.CODE+'01');
							$("input[name ='acc_code_pre']").val($("input[name ='acc_code_pre']").val()+'-'+data.CODE);
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
	
function checkextendedcode(){
	var accountcode = $("input[name ='acc_code_pre_extended']").val()+$("input[name ='acc_code_extended']").val()+"00";
	var code = $("input[name ='acc_code_extended']").val();
	if(code != ""){
		var numbers = /^[0-9]+$/;
		if(code.match(numbers)) 
		{ 
			$.ajax({
				type : "POST",
				url: '/track/ChartOfAccountServlet',
				async : true,
				dataType: 'json',
				data : {
					action : "checkAccountCode",
					AccountCode : accountcode,
				},
				success : function(dataitem) {
					var status = dataitem.results;
					if(status){
						alert("Please enter valid  Account Code, this Account Code Already Exist");
						$("input[name ='acc_code_extended']").val('');
						$("input[name ='acc_code_show_extended']").val($("input[name ='acc_code_pre_extended']").val());
					}else{
						$("input[name ='acc_code_show_extended']").val(accountcode);
					}
					
				}
			});
		}else{
			alert("Please enter valid  account code");
			$("input[name ='acc_code_extended']").val('');
			$("input[name ='acc_code_show_extended']").val($("input[name ='acc_code_pre_extended']").val());
		}
	}else{
		alert("Please enter account code");
		$("input[name ='acc_code_extended']").val('');
		$("input[name ='acc_code_show_extended']").val($("input[name ='acc_code_pre_extended']").val());
	}
}
</script>