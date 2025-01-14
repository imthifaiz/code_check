
<div id="create_account_modal" class="modal fade" role="dialog">

	<div class="modal-dialog">

		<div class="modal-content">
			<form class="form-horizontal" name="create_form">
			<input type="hidden" id="acc_det_type_id" name="acc_det_type_id">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h3 class="modal-title">Create Chart of Accounts</h3>
				</div>
				<div class="modal-body">	
					<div class="alert alert-danger">
					  test
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Account Group</label>				
	                     <div class="col-sm-6">                     
								<select class="form-control" name="acc_type" id="acc_type" style="width: 100%"></select>
							</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 required">Account
							Type</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="acc_det_type"
								name="acc_det_type"> <span class="select-icon"
								style="right: 25px;"> <i
								class="glyphicon glyphicon-menu-down"></i>
							</span>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 required">Account
							Name</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="acc_name"
								name="acc_name">
						</div>
					</div>	
					<div class="form-group" id="landedblock" style="display:none">
						<label class="control-label col-form-label col-sm-4">Landed
							Cost Calculation</label>
					    <div class="col-md-7">
					        <label class="radio-inline control-label">
					            <input id="costwise" name="landedcost" type="radio" value="1"> Cost
					        </label>
					        <label class="radio-inline control-label">
					            <input id="weightwise" name="landedcost" type="radio" value="2"> Weight
					        </label>
					        <label class="radio-inline control-label">
					            <input id="nonewise" name="landedcost" type="radio" value="0" checked="checked"> Not applicable
					        </label>
					    </div>
					</div>				
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Description</label>
						<div class="col-sm-6">
						<input type="hidden" class="form-control datepicker" id="acc_balanceDate" name="acc_balanceDate">
                     	<input type="hidden" class="form-control" id="acc_balance" name="acc_balance">
							<input type="text" class="form-control" id="acc_desc" name="acc_desc"></textarea>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 "></label>
						<div class="col-sm-6">
							<lable class="control-label col-form-label"><input type="Checkbox" name = "acc_is_sub" id="acc_is_sub" onclick="issubclick();">
                     <b> Is Sub Account</b></lable>
						</div>
					</div>
					<!-- <div class="form-group">
					<label class="control-label col-form-label col-sm-4 ">Sub Account</label>				
                     <div class="col-sm-6">                     
							<input type="text" class="form-control" id="acc_subAcct"
								name="acc_subAcct">
								<span class="select-icon"
								style="right: 25px;"> <i
								class="glyphicon glyphicon-menu-down"></i>
							</span>
						</div>
				</div> -->
				<div class="form-group">
					<label class="control-label col-form-label col-sm-4 ">Sub Account</label>				
                     <div class="col-sm-6">                     
							<select class="form-control" name="acc_subAcct" id="acc_subAcct" style="width: 100%"><option></option></select>
						</div>
				</div>
				<!-- <div class="form-group">
						<label class="control-label col-form-label col-sm-4">Opening
							Balance</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="acc_balance" hidden
								name="acc_balance">
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4">Opening
							Balance Date</label>
						<div class="col-sm-6">
							<input type="text" class="form-control datepicker" id="acc_balanceDate" hidden
								name="acc_balanceDate" READONLY>
						</div>
					</div> -->
				</div>				
				<div class="modal-footer">
					<button type="button" class="btn btn-success" id="create_user"
						onclick="create_account()">Save and Close</button>
					<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
				</div>
			</form>
		</div>

	</div>

</div>
<%@include file="addextendedtypepopupexpense.jsp"%>
 <link rel="stylesheet" href="dist/css/select2.min.css">
 <style>
 .bigdrop
 {
 	width: 487px !important;
 }
 </style>
<script src="dist/js/select2.full.min.js"></script>
<script>
var subType;
$(document).ready(function() {
	$(".alert").hide();
	var subAccountList;
	var AccountTypeList;
	loadAccount();
	
	//AccDetailType(3);
	loadSubAccount();
	function loadAccount()
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
				AccountTypeList=dataitem.results;
				var myJSON = JSON.stringify(AccountTypeList);
				
				$("#create_account_modal #acc_type").select2({
					  data: AccountTypeList
					})
				$("#create_account_modal #acc_det_type").typeahead('val', '"');
				$("#create_account_modal #acc_det_type").typeahead('val', '');
				$("#create_account_modal #acc_name").typeahead('val', '');
				selectAccType(3);
				
			}
		});
	}
	function selectAccType(id)
	{
		$("#create_account_modal #acc_type").val(id).trigger('change');
		//$('#acc_type').val(id);
	}
	function loadSubAccount()
	{
		$.ajax({
			type : "POST",
			url: '/track/ChartOfAccountServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getSubAccountTypeGroup",
			},
			success : function(datasubitem) {
				subAccountList=datasubitem.results;
				var myJSON = JSON.stringify(subAccountList);
				
				/* alert(myJSON); */
				$("#create_account_modal #acc_subAcct").select2({
						data:subAccountList,
						dropdownCssClass : 'bigdrop',
						placeholder: 'Enter parent account',
						templateResult: function(item)
						{
							if (item.issub) {
								var $state = $(
									    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
									  );
								}
							else
								{
								var $state = $(
										 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
									  );
								}
							  
							  return $state;
						},
						sorter: function(data) {    
					        return data.sort();
					    }
					})
				
			}
		});
	}
	$("#create_account_modal").on('show.bs.modal', function(){
			$(".alert").hide();
			$("#create_account_modal #landedblock").hide();
			$('#create_account_modal #nonewise').prop('checked', true);
			selectAccType(3);
			AccDetailType(3);
			loadSubAccount();
			$('#create_account_modal #acc_det_type').val("");
			$('#create_account_modal #acc_name').val("");
			$('#create_account_modal #acc_desc').val("");
			$('#create_account_modal #acc_is_sub').prop('checked', false);
			$('#create_account_modal #acc_subAcct').val(null).trigger('change');
			$('#create_account_modal #acc_subAcct').attr('disabled',true);
			//alert("Open modal event");
		});
	$('#create_account_modal #acc_subAcct').on('select2:select', function (e) {
	    var data = e.params.data;
	    subType=data.type;
	});
	
	$('#create_account_modal #acc_type').on('select2:select', function (e) {
	    var data = e.params.data;
	    AccDetailType(data.id);
	    if(data.MAINACCID==="5")
    	{
    		$("#create_account_modal #landedblock").show();
    	}
    else
    	{
    		$("#create_account_modal #landedblock").hide();
    		$('#create_account_modal #nonewise').prop('checked', true);
    	}
	    $('#create_account_modal #acc_det_type').val("");
	});
	
	$('#create_account_modal #acc_subAcct').attr('disabled',true);
	$('#create_account_modal #acc_subAcct').css("background-color", "#EEEEEE");
});
function issubclick(){
	if(document.create_form.acc_is_sub.checked)
	{
		
		document.create_form.acc_subAcct.value="";
		$('#create_account_modal #acc_subAcct').attr('disabled',false);
		$('#create_account_modal #acc_subAcct').css("background-color", "transparent");

	}
	else{
		document.create_form.acc_subAcct.value="";
		 $('#create_account_modal #acc_subAcct').attr('disabled',true);
		 $('#create_account_modal #acc_subAcct').css("background-color", "#EEEEEE");
	}
}

	
	/* $('#acc_type').typeahead({
			minLength : 0,
			highlight: true,
			showHintOnFocus:true,
			accent: true
			},
			{
			//name: 'states',
			display : 'ACCOUNTTYPE',
			async : true,
			//source: substringMatcher(states),
			source : function(query, process, asyncProcess) {
				var urlStr = "/track/ChartOfAccountServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						action : "getAccountType",
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
					return '<p>'+ data.ACCOUNTTYPE + '</p>';
				}
			}
		}).on('typeahead:change',function(event,selection){			
			$("#acc_det_type").typeahead('val', '"');
			$("#acc_det_type").typeahead('val', '');
			$("#acc_name").typeahead('val', '');
		}).on('typeahead:open',function(){
			$("#acc_type").typeahead('val', '');
		}); */
		function AccDetailType(type)
		{
			$("#create_account_modal #acc_det_type").typeahead("destroy");
			$('#create_account_modal #acc_det_type').typeahead({
				hint : true,
				minLength : 0,
				searchOnFocus : true
				},
				{
				//name: 'states',
				display : 'ACCOUNTDETAILTYPE',
				async : true,
				//source: substringMatcher(states),
				source : function(query, process, asyncProcess) {
					var urlStr = "/track/ChartOfAccountServlet";
					$.ajax({
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							action : "getAccountDetailType",
							TYPE: type,
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
						return '<p onclick="createDetailMain(\''+data.ACCOUNTDETAILTYPE_ID+'\',\''+data.ACCOUNTDETAILTYPE+'\')">'+ data.ACCOUNTDETAILTYPE + '</p>';
					}
				}
				}).on('typeahead:render',function(event,selection){
					var menuElement = $(this).parent().find(".tt-menu");
					var top = menuElement.height()+35;
					top+="px";	
					if(menuElement.next().hasClass("footer")){
						menuElement.next().remove();  
					}
					menuElement.after( '<div class="extnBtn footer"  data-toggle="modal" data-target="#create_extended_modalmain"><a href="#"> + New Account Type</a></div>');
					menuElement.next().width(menuElement.width());
					menuElement.next().css({ "top": top,"padding":"3px 20px" });
					if($(this).parent().find(".tt-menu").css('display') != "block")
						menuElement.next().hide();
				}).on('typeahead:open',function(){
				$("#acc_det_type").typeahead('val', '');
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
				}).on('typeahead:close',function(){
					var menuElement = $(this).parent().find(".tt-menu");
					setTimeout(function(){ menuElement.next().hide();}, 150);
				});
		}
		function createDetailMain(id,type)
		{
			//alert("coaaddyes");
			//alert(type);
			//alert(id);
			//document.edit_form.eacc_name.value=type;
			document.create_form.acc_det_type_id.value=id;
		}
	/* $('#acc_subAcct').typeahead({
		hint : true,
		minLength : 0,
		searchOnFocus : true
		},
		{
		//name: 'states',
		display : 'ACCOUNTDISPLAY',
		async : true,
		//source: substringMatcher(states),
		source : function(query, process, asyncProcess) {
			accType=$("#acc_type").typeahead('val');
			var urlStr = "/track/ChartOfAccountServlet";
			$.ajax({
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					action : "getSubAccountType",
					TYPE: accType,
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
				return '<p>'+ data.ACCOUNTNAME +':&nbsp;&nbsp;&nbsp;&nbsp;<strong>'+data.ACCOUNTTYPE+ '</strong></p>';
			}
		}
	}).on('typeahead:open',function(){
		$("#acc_subAcct").typeahead('val', '');
		
	}).on('typeahead:select',function(evt,suggestion){
		//var subdata = JSON.stringify(suggestion);
		//alert(subdata);
		$("#acc_subAcct").typeahead('val', suggestion.ACCOUNTNAME);
		
	}); */
	
</script>
