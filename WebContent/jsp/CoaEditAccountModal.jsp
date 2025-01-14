 
<div id="edit_account_modal" class="modal fade" role="dialog">

	<div class="modal-dialog">

		<div class="modal-content">
			<form class="form-horizontal" name="edit_form">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h3 class="modal-title">Edit Chart of Accounts</h3>
				</div>
				<input type="hidden" id="acc_id" name="acc_id">
				<input type="hidden" id="acc_typeH" name="acc_typeH">
				<input type="hidden" id="acc_det_typeH" name="acc_det_typeH">
				<input type="hidden" id="acc_subAcctH" name="acc_subAcctH">
				<input type="hidden" id="eacc_det_type_id" name="eacc_det_type_id">
				<input type="hidden" id="ecoamainid" name="ecoamainid">
				<input type="hidden" id="eacc_desc" name="eacc_desc">
				<div class="modal-body">	
					<div class="alert alert-danger">
					  test
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Account Group</label>				
	                     <div class="col-sm-6">                     
								<select class="form-control" name="eacc_type" id="eacc_type" style="width: 100%" readonly></select>
							</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 required">Account
							Type</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="eacc_det_type"
								name="eacc_det_type" readonly> <!-- <span class="select-icon"
								style="right: 25px;"> <i
								class="glyphicon glyphicon-menu-down" ></i>
							</span> -->
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 required">Account Code</label>
						<div class="col-sm-6">
							<input type="text" class="form-control"  id="eacc_code_show"
								name="acc_code_show" readonly>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 required">Account
							Name</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="eacc_name"
								name="eacc_name">
						</div>
					</div>	
					<div class="form-group" id="editlandedblock" style="display:none">
						<label class="control-label col-form-label col-sm-4">Landed
							Cost Calculation</label>
					    <div class="col-md-7">
					        <label class="radio-inline control-label">
					            <input id="editcostwise" name="editlandedcost" type="radio" value="1"> Cost
					        </label>
					        <label class="radio-inline control-label">
					            <input id="editweightwise" name="editlandedcost" type="radio" value="2"> Weight
					        </label>
					        <label class="radio-inline control-label">
					            <input id="editnonewise" name="editlandedcost" type="radio" value="0" checked="checked"> Not applicable
					        </label>
					    </div>
					</div>				
					<!-- <div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Description</label>
						<div class="col-sm-6">
						<input type="hidden" class="form-control datepicker" id="eacc_balanceDate" name="eacc_balanceDate">
                     	<input type="hidden" class="form-control" id="eacc_balance" name="eacc_balance">
							<input type="text" class="form-control" id="eacc_desc" name="eacc_desc">
						</div>
					</div> -->
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 "></label>
						<div class="col-sm-6">
							<lable class="control-label col-form-label"><input type="Checkbox" name = "eacc_is_sub" id="eacc_is_sub" onclick="iseditsubclick();" disabled>
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
							<select class="form-control" name="eacc_subAcct" id="eacc_subAcct" style="width: 100%"><option></option></select>
						</div>
				</div>
				
				<div class="form-group">
						<label class="control-label col-form-label col-sm-4 "></label>
						<div class="col-sm-6">
							<lable class="control-label col-form-label"><input type="Checkbox" name="eacc_is_exp_gst" id="eacc_is_exp_gst">
                     <b> Is Expense GST</b></lable>
						</div>
				</div>
					
				<div class="form-group" hidden>
						<label class="control-label col-form-label col-sm-4" for="eballable">
							Balance</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="eacc_balance_show" 
								name="eacc_balance_show" disabled>
						</div>
					</div>
				<div class="form-group" hidden>
						<label class="control-label col-form-label col-sm-4" for="eballable">
							Balance</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="eacc_balance"
								name="eacc_balance">
						</div>
					</div>
					<div class="form-group" hidden>
						<label class="control-label col-form-label col-sm-4" for="ebalanceDate">
							Balance Date</label>
						<div class="col-sm-6">
							<input type="text" class="form-control datepicker" id="eacc_balanceDate" placeholder="Select Date"
								name="eacc_balanceDate" READONLY>
						</div>
					</div> 
				</div>				
				<div class="modal-footer">
					<button type="button" class="btn btn-success" id="create_user"
						onclick="update_account()">Save and Close</button>
					<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
				</div>
			</form>
		</div>

	</div>

</div>
<%@include file="editextendedtypepopup.jsp"%>
 <link rel="stylesheet" href="dist/css/select2.min.css">
 <style>
 .ebigdrop
 {
 	width: 487px !important;
 }
 </style>
<script src="dist/js/select2.full.min.js"></script>
<script>
var subType;
var OrignalAccName;
var updateStatus=true;
$(document).ready(function() {
	$(".alert").hide();
	var EditsubAccountList;
	var EditAccountTypeList;
	//var actType=$('#acc_typeH').val();
	//alert(actType);
	EditloadAccount();
	
	//EditAccDetailType(3);
	EditloadSubAccount();
	function EditloadAccount()
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
				EditAccountTypeList=dataitem.results;
				var myJSON = JSON.stringify(EditAccountTypeList);
				
				$("#eacc_type").select2({
					  data: EditAccountTypeList
					})
				/* $("#acc_det_type").typeahead('val', '"');
				$("#acc_det_type").typeahead('val', '');
				$("#acc_name").typeahead('val', '');*/
				
			}
		});
	}
	/* function selectAccType(id)
	{
		//alert(id);
		$("#eacc_type").val(id).trigger('change');
		//$('#acc_type').val(id);
	} */
	function EditloadSubAccount()
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
				EditsubAccountList=datasubitem.results;
				var myJSON = JSON.stringify(EditsubAccountList);
				
				/* alert(myJSON); */
				$("#eacc_subAcct").select2({
						data:EditsubAccountList,
						dropdownCssClass : 'ebigdrop',
						placeholder: 'Enter parent account',
						templateResult: function(item)
						{
							var $state = $('<span>'+ item.text +'</span>');
							if (item.issub) {
								 $state = $(
									    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</i></p></span>'
									  );
								}
							else
								{
									$state = $(
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
	$("#edit_account_modal").on('show.bs.modal', function(){
			$(".alert").hide();
			/* var actType=$('#acc_typeH').val();
			//alert("open"+actType);
			//selectAccType(actType); 
			//AccDetailType(null);
			//selectAccType(3);
			//AccDetailType(3);
			loadSubAccount();
			$('#edit_account_modal #eacc_det_type').val("");
			$('#edit_account_modal #eacc_name').val("");
			$('#edit_account_modal #eacc_desc').val("");
			$('#acc_is_sub').prop('checked', false);
			$('#acc_subAcct').val(null).trigger('change');
			$('#edit_account_modal #acc_subAcct').attr('disabled',true);
			//alert("Open modal event"); */
		}); 
	$('#eacc_subAcct').on('select2:select', function (e) {
	    var data = e.params.data;
	    subType=data.type;
	});
	
	$('#eacc_type').on('select2:select', function (e) {
	    var data = e.params.data;
	    EditAccDetailType(data.id);
	    //alert("Mainid:"+data.MAINACCID);
	    if(data.MAINACCID==="5")
	    	$("#editlandedblock").show();
	    else
	    	$("#editlandedblock").hide();
	    
	    //--$('#edit_account_modal #eacc_det_type').val("");
	});
	//$('#edit_account_modal #acc_subAcct').attr('disabled',true);
	//$('#edit_account_modal #acc_subAcct').css("background-color", "#EEEEEE");
	OrignalAccName=$('#edit_account_modal #eacc_name').val();
	$('#edit_account_modal #eacc_name').on('change',function(){
		var eaccName=$('#edit_account_modal #eacc_name').val();
		
		if(!(OrignalAccName.toLowerCase() === eaccName.toLowerCase()))
			{
				$.ajax({
					type : 'post',
					url : "/track/ChartOfAccountServlet?action=checkAccountCount",
					dataType : 'json',
					data : 
						{
							accname:eaccName
						},
					success : function(data) {
						var count=parseInt(data);
						if(count>0)
							{
								$(".alert").show();
					    		$('.alert').html("Account name already exist.");
								$('#edit_account_modal #eacc_name').focus();
								updateStatus=false;
								return false;
							}
						else
							{
								updateStatus=true;
								$(".alert").hide();
							}
								},
								error : function(data) {
					
									alert(data);
								}
							});
			}
		
	});
});

function selectEditAcctType(id)
{
	
	$("#eacc_type").val(id).trigger('change');
	//--EditAccDetailType(id);
}
function selectEditSubAcc(subid)
{
	subType=subid;
	$("#eacc_subAcct").val(subid).trigger('change');
}
function uncheckSubAcc()
{
	//$("#edit_account_modal #eacc_is_sub").attr('checked',false);
	 $("#edit_account_modal #eacc_is_sub").prop("checked", false);
}
function checkSubAcc()
{
	$("#edit_account_modal #eacc_is_sub").prop("checked", true);
}

function uncheckExpGst()
{
	//$("#edit_account_modal #eacc_is_sub").attr('checked',false);
	 $("#edit_account_modal #eacc_is_exp_gst").prop("checked", false);
}
function checkExpGst()
{
	$("#edit_account_modal #eacc_is_exp_gst").prop("checked", true);
}

function iseditsubclick(){
	if(document.edit_form.eacc_is_sub.checked)
	{
		//alert(document.edit_form.eacc_is_sub.checked);
		
		$('#edit_account_modal #eacc_subAcct').attr('disabled',false);
		$('#edit_account_modal #eacc_subAcct').css("background-color", "transparent");

	}
	else{
		selectEditSubAcc(null);
		$("#edit_account_modal #eacc_is_sub").attr('checked',false);
		 $('#edit_account_modal #eacc_subAcct').attr('disabled',true);
		 $('#edit_account_modal #eacc_subAcct').css("background-color", "#EEEEEE");
	}
}

	function update_account() {
		
		if ($('#edit_account_modal #eacc_type').val() == "") {
			alert("please fill account group");
			$('#edit_account_modal #eacc_type').focus();
			return false;
		}
		
		if ($('#edit_account_modal #eacc_det_type').val() == "") {
			alert("please fill account detail type");
			$('#edit_account_modal #eacc_det_type').focus();
			return false;
		}
		
		if ($('#edit_account_modal #eacc_name').val() == "") {
			alert("please fill account name");
			$('#edit_account_modal #eacc_name').focus();
			return false;
		}
		
		if(document.edit_form.eacc_is_sub.checked)
		{
			$('#edit_account_modal #eacc_is_sub').val("1");
			if ($('#edit_account_modal #eacc_subAcct').val() == "") {
				alert("please fill sub account");
				$('#edit_account_modal #eacc_subAcct').focus();
				return false;
			}
			else
				{
				// var parType=$( "#eacc_type option:selected" ).text();
				 var parType=$('#edit_account_modal #eacc_det_type').val();
				 
				 
				 var subdata=$("#eacc_subAcct").select2('data');
				 var subType=subdata[0].type;
				 subType=subType.trim();
				 var n = parType.localeCompare(subType);
				    if(n!=0)
				    	{
				    	$(".alert").show();
				    	$('.alert').html("For subaccounts, you must select the same account and extended type as their parent.");
				    	/* setTimeout(function() {
				            $(".alert").alert('close');
				        }, 5000); */
				    	 return false;
				    	}
				}
		}
	   if ($('#edit_account_modal #eacc_balance').val() != "") {
			if (parseFloat($('#edit_account_modal #eacc_balance').val()) != 0) {
				if ($('#edit_account_modal #eacc_balanceDate').val() == "") {
					if ($('#edit_account_modal #ecoamainid').val() == "1" || $('#edit_account_modal #ecoamainid').val() == "3") {
						alert("Please Fill Balance Date");
					}else if($('#edit_account_modal #ecoamainid').val() == "2"){
						alert("Please Fill Unpaid Balance Date");
					}else{
						alert("Please Fill Date");
					}
					$('#edit_account_modal #eacc_balanceDate').focus();
				return false;
				}
			}
		} 
		if(updateStatus)
			{
				var formData = $('form[name="edit_form"]').serialize();
				$.ajax({
					type : 'post',
					url : "/track/ChartOfAccountServlet?action=update",
					dataType : 'json',
					data : formData,//{key:val}
					success : function(data) {
						if (data.STATUS == "FAIL") {		                               
							alert(data.MESSAGE);
						}else{
							$('#edit_account_modal').modal('toggle');
							loadCOAData();
							successAccountCallback(data);
						}
					},
					error : function(data) {
	
						alert(data.responseText);
					}
				});
			return false;
			}
		
		/* var eaccName=$('#edit_account_modal #eacc_name').val();
		$.ajax({
			type : 'post',
			url : "/track/ChartOfAccountServlet?action=checkAccountCount",
			dataType : 'json',
			data : 
				{
					accname:eaccName
				},
			success : function(data) {
				var count=parseInt(data);
				if(count>0)
					{
						$(".alert").show();
			    		$('.alert').html("Account name already exist.");
						$('#edit_account_modal #eacc_name').focus();
						return false;
					}
				else
					{
						
								}
						},
						error : function(data) {
			
							alert(data);
						}
					}); */
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
		/* function EditAccDetailType(type)
		{
			$("#eacc_det_type").typeahead("destroy");
			$('#eacc_det_type').typeahead({
				hint : true,
				minLength : 0,
				searchOnFocus : true
				},
				{
				//name: 'states',
				display : 'NAME_CODE',
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
						return '<p onclick="updateDetail(\''+data.ACCOUNTDETAILTYPE_ID+'\',\''+data.ACCOUNTDETAILTYPE+'\')">'+ data.NAME_CODE + '</p>';
						//return '<p onclick="document.edit_form.eacc_name.value = \''+data.ACCOUNTDETAILTYPE+'\';document.edit_form.acc_det_type_id.value = \''+data.ACCOUNTDETAILTYPE_ID+'\'">'+ data.ACCOUNTDETAILTYPE + '</p>';
					}
				}
				}).on('typeahead:render',function(event,selection){
					var menuElement = $(this).parent().find(".tt-menu");
					var top = menuElement.height()+35;
					top+="px";	
					if(menuElement.next().hasClass("footer")){
						menuElement.next().remove();  
					}
					menuElement.after( '<div class="extnBtn footer"  data-toggle="modal" data-target="#edit_create_extended_modal"><a href="#"> + New Account Type</a></div>');
					menuElement.next().width(menuElement.width());
					menuElement.next().css({ "top": top,"padding":"3px 20px" });
					if($(this).parent().find(".tt-menu").css('display') != "block")
						menuElement.next().hide();
				}).on('typeahead:open',function(){
				$("#eacc_det_type").typeahead('val', '');
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
				}).on('typeahead:close',function(){
					var menuElement = $(this).parent().find(".tt-menu");
					setTimeout(function(){ menuElement.next().hide();}, 150);
				});
		} --*/
	function updateDetail(id,type)
	{
		//alert(id);
		//alert(type);
		//document.edit_form.eacc_name.value=type;
		document.edit_form.eacc_det_type_id.value=id;
	}
	
	$(document).on("change","input[name ='eacc_balance']",function(){
		var baseamount = $("input[name ='eacc_balance']").val();
		var numberOfDecimal = $("input[name ='numberOfDecimal']").val();
		var zeroval = "0";
		if(baseamount != ""){
			var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
			var numbers1 = /^[-+]?[0-9]+$/;
			var numbers2 = /^[0-9]+$/;
			if(baseamount.match(decimal) || baseamount.match(numbers1) || baseamount.match(numbers2)) 
			{ 
				$("input[name ='eacc_balance']").val(parseFloat(baseamount).toFixed(numberOfDecimal));
			}else{
				alert("Please enter valid opening balance");
				$("input[name ='eacc_balance']").val(parseFloat(zeroval).toFixed(numberOfDecimal));
			}
		}else{
			alert("Please enter valid opening balance");
			$("input[name ='eacc_balance']").val(parseFloat(zeroval).toFixed(numberOfDecimal));
		}
		
		
	});
</script>
