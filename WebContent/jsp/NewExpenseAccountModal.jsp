
<div id="create_expenseaccount_modal" class="modal fade" role="dialog">

	<div class="modal-dialog">

		<div class="modal-content">
			<form class="form-horizontal" name="create_form">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h3 class="modal-title">Create Expense Account</h3>
				</div>
				<div class="modal-body">
				<input type="text" name="acc_groupid" hidden>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Account
							Group</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="acc_group"
								name="acc_group"> <span class="select-icon"
								style="right: 45px;"> <i
								class="glyphicon glyphicon-menu-down"></i>
							</span>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4">Account
							Type</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="acctypes"
								name="acctypes">
						</div>
					</div>					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" id="create_user"
						onclick="create_account()">Create</button>
					<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
				</div>
			</form>
		</div>

	</div>

</div>
<script>
	function create_account() {
		if ($('#create_expenseaccount_modal #acc_group').val() == "") {
			alert("please fill account group");
			$('#create_expenseaccount_modal #acc_group').focus();
			return false;
		}
		if ($('#create_expenseaccount_modal #acctypes').val() == "") {
			alert("please fill account type");
			$('#create_expenseaccount_modal #acctypes').focus();
			return false;
		}
		var formData = $('form[name="create_form"]').serialize();
		$.ajax({
			type : 'post',
			url : "/track/ChartOfAccountServlet?action=createExpense",
			dataType : 'json',
			data : formData,//{key:val}
			success : function(data) {
				if (data.STATUS == "FAIL") {		                               
					alert(data.MESSAGE);
				}else{
					$('#create_expenseaccount_modal').modal('toggle');
					successExpenseAccountCallback(data);
				}
			},
			error : function(data) {

				alert(data.responseText);
			}
		});
		return false;
	}
	$('#acc_group').typeahead({
			hint : true,
			minLength : 0,
			searchOnFocus : true
			},
			{
			//name: 'states',
			display : 'GROUPS',
			async : true,
			//source: substringMatcher(states),
			source : function(query, process, asyncProcess) {
				var urlStr = "/track/ChartOfAccountServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						action : "GetGroupDetail",
						GROUP : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.groups);
					}
				});
			},
			limit : 9999,
			templates : {
				empty : [ '<div style="padding:3px 20px">',
						'No results found', '</div>', ].join('\n'),
				suggestion : function(data) {					
					return '<p>' +data.GROUPS + '</p>';
				}
			}
		});
</script>
