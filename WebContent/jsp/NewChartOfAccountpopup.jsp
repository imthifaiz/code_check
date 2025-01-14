<div id="create_account_modalcoa" class="modal fade" role="dialog">

	<div class="modal-dialog">

		<div class="modal-content">
			<form class="form-horizontal" name="create_formcoa">
			<input type="hidden" id="acc_det_type_id" name="acc_det_type_id">
			<input type="hidden" id="acc_desc" name="acc_desc">
			<input type="hidden" id="coamainid" name="coamainid">
			<input type="hidden" id="acc_code_code" name="acc_code_code">
			<input type="hidden" id="accid" name="accid">
			<input type="hidden" id="subaccode" name="subaccode">
			<input type="hidden" id="sub_maincode" name="sub_maincode">
			<input type="hidden" id="sub_sub_code" name="sub_sub_code">
			<input type="hidden" class="form-control datepicker" id="acc_balanceDate" name="acc_balanceDate">
			<input type="hidden" class="form-control" id="acc_balance" name="acc_balance">
			<input type="hidden" id="acc_det_type_id" name="acc_det_type_id">
			<input type="hidden" id="ukey" name="ukey">
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
					<div class="form-group" hidden>
						<label class="control-label col-form-label col-sm-4 required">Account
							Type</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="acc_det_type_name"
								name="acc_det_type_name" readonly> 
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-4 required">Account Code</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="acc_code_pre"
								name="acc_code_pre" readonly>
						</div>
						<div class="col-sm-2">
							<input type="text" class="form-control" onchange="checkcode()" id="acc_code"
								name="acc_code">
						</div>
						<div class="col-sm-3">
							<input type="text" class="form-control"  id="acc_code_show"
								name="acc_code_show" readonly>
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
						<label class="control-label col-form-label col-sm-4">Landed Cost Calculation</label>
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
					<!-- <div class="form-group">
						<label class="control-label col-form-label col-sm-4 ">Description</label>
						<div class="col-sm-6">
						<input type="hidden" class="form-control datepicker" id="acc_balanceDate" name="acc_balanceDate">
                     	<input type="hidden" class="form-control" id="acc_balance" name="acc_balance">
							<input type="text" class="form-control" id="acc_desc" name="acc_desc"></textarea>
						</div>
					</div> -->
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
			<div class="form-group">
						<label class="control-label col-form-label col-sm-4 "></label>
						<div class="col-sm-6">
							<lable class="control-label col-form-label"><input type="Checkbox" name = "acc_is_exp_gst" id="acc_is_exp_gst">
                     <b> Is Expense GST</b></lable>
						</div>
					</div>
				<!-- <div class="form-group opbal" >
						<label class="control-label col-form-label col-sm-4" for="ballable">
							Balance</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" id="acc_balance" 
								name="acc_balance">
						</div>
					</div>
					<div class="form-group opbal" >
						<label class="control-label col-form-label col-sm-4" for="balanceDate">
							Balance As Of</label>
						<div class="col-sm-6">
							<input type="text" class="form-control datepicker" id="acc_balanceDate" placeholder="Select Date"
								name="acc_balanceDate" READONLY>
						</div>
					</div>  -->
				</div>				
				<div class="modal-footer">
					<button type="button" class="btn btn-success" id="create_user"
						onclick="create_accountcoa()">Save and Close</button>
					<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
				</div>
			</form>
		</div>

	</div>

</div>
<%-- <%@include file="addextendedtypepopupexpensecoa.jsp"%> --%>
<%@include file="addextendedtypepopup.jsp"%>
 <link rel="stylesheet" href="dist/css/select2.min.css">
 <style>
 .hbigdrop
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
	var numberOfDecimal = $("input[name ='numberOfDecimal']").val();
	$("input[name ='acc_balance']").val(parseFloat("0").toFixed(numberOfDecimal));
	
	AccDetailType(3);
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
				
				$("#acc_type").select2({
					  data: AccountTypeList
					})
				$("#acc_det_type").typeahead('val', '"');
				$("#acc_det_type").typeahead('val', '');
				$("#acc_name").typeahead('val', '');
				selectAccType(3);
				
			}
		});
	}
	function selectAccType(id)
	{
		$("#acc_type").val(id).trigger('change');
		$("input[name ='accid']").val(id);
		$("input[name ='acc_code_pre']").val('300');
	    $("input[name ='acc_code_code']").val('300');
	    $("input[name ='acc_code_show']").val('300');
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
				$("#acc_subAcct").select2({
						data:subAccountList,
						dropdownCssClass : 'hbigdrop',
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
	$("#create_account_modalcoa").on('show.bs.modal', function(){
			$(".alert").hide();
			$("#landedblock").hide();
			$('#nonewise').prop('checked', true);
			selectAccType(3);
			AccDetailType(3);
			loadSubAccount();
			$("input[name ='acc_code']").val('');
			$("input[name ='acc_code_show']").val('');
			$("input[name ='acc_det_type_name']").val('');
			$('#create_account_modalcoa #acc_det_type').val("");
			$('#create_account_modalcoa #acc_name').val("");
			$('#create_account_modalcoa #acc_desc').val("");
			$('#acc_is_sub').prop('checked', false);
			$('#acc_subAcct').val(null).trigger('change');
			$('#create_account_modalcoa #acc_subAcct').attr('disabled',true);
			//alert("Open modal event");
		});
	$('#acc_subAcct').on('select2:select', function (e) {
	    var data = e.params.data;
	    subType=data.type;
	    $("input[name ='sub_maincode']").val(data.CODE);
	    //$("input[name ='subaccode']").val("-"+data.CODE);
	   // $("input[name ='acc_code_show']").val($("input[name ='acc_code_show']").val()+"-"+data.CODE);
	   // $("input[name ='acc_code_show']").val($("input[name ='acc_code_pre']").val()+$("input[name ='acc_code']").val()+"-"+data.CODE);
	    
	    $.ajax({
			type : "POST",
			url: '/track/ChartOfAccountServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getSubAccountCode",
				SID : data.id,
				ACODE : data.ACCOUNT_CODE,
			},
			success : function(dataitem) {
				var scode = dataitem.results;
				$("input[name ='acc_code_show']").val(scode);
				$("input[name ='subaccode']").val(scode);
				$("input[name ='sub_sub_code']").val(dataitem.scode);
			}
		});
	});
	
	$('#acc_type').on('select2:select', function (e) {
	    var data = e.params.data;
	    //var myJSON = JSON.stringify(data);
	    //console.log("acc:"+myJSON);
	    var numberOfDecimal = $("input[name ='numberOfDecimal']").val();
	    AccDetailType(data.id);
	    $("input[name ='coamainid']").val(data.MAINACCID);
	    $("input[name ='accid']").val(data.id);
	    $("input[name ='acc_code_pre']").val(data.CODE);
	    var subacid ="";
	    if(document.create_formcoa.acc_is_sub.checked){
	    	subacid = $("input[name ='subaccode']").val();
	    	$("input[name ='acc_code_show']").val(subacid);
	    }else{
	    	$("input[name ='acc_code_show']").val(data.CODE);
		    $("input[name ='acc_code_code']").val(data.CODE);
	    }
	    
	    	if(data.MAINACCID==="5"){
	    		$("#landedblock").show();
	    		$("input[name ='acc_balance']").val(parseFloat("0").toFixed(numberOfDecimal));
	    		$(".opbal").hide();
	    	}else if(data.MAINACCID==="1" || data.MAINACCID==="3"){
	    		$("#landedblock").hide();
	    		$("input[name ='acc_balance']").val(parseFloat("0").toFixed(numberOfDecimal));
	    		$('#nonewise').prop('checked', true);
	    		$("label[for='ballable']").text('Balance');
	    		$("label[for='balanceDate']").text('Balance Date');
	    		
	    		$(".opbal").show();	
    		}else if(data.MAINACCID==="2"){
	    		$("#landedblock").hide();
	    		$("input[name ='acc_balance']").val(parseFloat("0").toFixed(numberOfDecimal));
	    		$('#nonewise').prop('checked', true);
	    		$("label[for='ballable']").text('Unpaid Balance');
	    		$("label[for='balanceDate']").text('Unpaid Balance Date');
	    		$(".opbal").show();
    		}else{
    			$("input[name ='acc_balance']").val(parseFloat("0").toFixed(numberOfDecimal));
	    		$("#landedblock").hide();
	    		$(".opbal").hide();
	    		$('#nonewise').prop('checked', true);
	    	}
	    $("#acc_det_type").typeahead('val', '"');
		$("#acc_det_type").typeahead('val', '');
	    	
	});
	
	$('#create_account_modalcoa #acc_subAcct').attr('disabled',true);
	$('#create_account_modalcoa #acc_subAcct').css("background-color", "#EEEEEE");
});
function issubclick(){
	if(document.create_formcoa.acc_is_sub.checked)
	{
		
		document.create_formcoa.acc_subAcct.value="";
		$('#create_account_modalcoa #acc_subAcct').attr('disabled',false);
		$('#create_account_modalcoa #acc_subAcct').css("background-color", "transparent");
		//$("input[name ='acc_code_show']").val($("input[name ='acc_code_pre']").val()+$("input[name ='acc_code']").val()+$("input[name ='subaccode']").val());
		$('#create_account_modalcoa #acc_code').attr('readonly',true);
		subacid = $("input[name ='subaccode']").val();
    	$("input[name ='acc_code_show']").val(subacid);
	}
	else{
		document.create_formcoa.acc_subAcct.value="";
		 $('#create_account_modalcoa #acc_subAcct').attr('disabled',true);
		 $('#create_account_modalcoa #acc_subAcct').css("background-color", "#EEEEEE");
		 $("input[name ='acc_code_show']").val($("input[name ='acc_code_pre']").val()+$("input[name ='acc_code']").val());
		 $('#create_account_modalcoa #acc_code').attr('readonly',false);
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
			$("#acc_det_type").typeahead("destroy");
			$('#acc_det_type').typeahead({
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
						return '<p onclick="createDetail(\''+data.ACCOUNTDETAILTYPE_ID+'\',\''+data.ACCOUNTDETAILTYPE+'\',\''+data.CODE+'\')">'+ data.ACCOUNT_CODE +'  '+data.ACCOUNTDETAILTYPE + '</p>';
					}
				}
				}).on('typeahead:render',function(event,selection){
					var menuElement = $(this).parent().find(".tt-menu");
					var top = menuElement.height()+35;
					top+="px";	
					if(menuElement.next().hasClass("footer")){
						menuElement.next().remove();  
					}
					menuElement.after( '<div class="extnBtn footer"  data-toggle="modal" data-target="#create_extended_modal"><a href="#"> + New Account Type</a></div>');
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
		function createDetail(id,type,code)
		{
			//alert(id);
			//alert(type);
			//document.edit_form.eacc_name.value=type;
			document.create_formcoa.acc_det_type_id.value=id;
			$("input[name ='acc_det_type_id']").val(id);
			$("input[name ='acc_det_type_name']").val(type);
			var text = $("input[name ='acc_code_code']").val();
			text = text+'-'+code;
			$("input[name ='acc_code_pre']").val(text);
			$.ajax({
				type : "POST",
				url: '/track/ChartOfAccountServlet',
				async : true,
				dataType: 'json',
				data : {
					action : "getAccountCode",
					AccountCode : text,
					Atid:$("input[name ='accid']").val(),
					Adtid:id,
				},
				success : function(dataitem) {
					var accountcode = dataitem.results;
					//alert(accountcode);
					$("input[name ='acc_code']").val(accountcode);
					 var subacid ="";
					    if(document.create_formcoa.acc_is_sub.checked){
					    	subacid = $("input[name ='subaccode']").val();
					    	$("input[name ='acc_code_show']").val(subacid);
					    }else{
					    	$("input[name ='acc_code_show']").val(text+accountcode);
					    }
					
				}
			});
			
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
	
	$(document).on("change","input[name ='acc_balance']",function(){
		var baseamount = $("input[name ='acc_balance']").val();
		var numberOfDecimal = $("input[name ='numberOfDecimal']").val();
		var zeroval = "0";
		if(baseamount != ""){
			var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
			var numbers = /^[0-9]+$/;
			if(baseamount.match(decimal) || baseamount.match(numbers)) 
			{ 
				$("input[name ='acc_balance']").val(parseFloat(baseamount).toFixed(numberOfDecimal));
			}else{
				alert("Please enter valid opening balance");
				$("input[name ='acc_balance']").val(parseFloat(zeroval).toFixed(numberOfDecimal));
			}
		}else{
			alert("Please enter valid opening balance");
			$("input[name ='acc_balance']").val(parseFloat(zeroval).toFixed(numberOfDecimal));
		}
		
		
	});
	
	function checkno(amount){
		var baseamount = amount;
		var zeroval = "0";
		if(baseamount != ""){
			var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
			var numbers = /^[0-9]+$/;
			if(baseamount.match(decimal) || baseamount.match(numbers)) 
			{ 
				return baseamount;
			}else{
				$("input[name ='acc_balance']").val()
				return zeroval;
				
			}
		}else{
			return zeroval;
		}
		
	}
	
	function checkcode(){
		var accountcode = $("input[name ='acc_code_pre']").val()+$("input[name ='acc_code']").val();
		var code = $("input[name ='acc_code']").val();
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
							$("input[name ='acc_code']").val('');
							var subacid ="";
						    if(document.create_formcoa.acc_is_sub.checked){
						    	subacid = $("input[name ='subaccode']").val();
						    	$("input[name ='acc_code_show']").val(subacid);
						    }else{
						    	$("input[name ='acc_code_show']").val($("input[name ='acc_code_pre']").val());
						    }
							
						}else{
							var subacid ="";
						    if(document.create_formcoa.acc_is_sub.checked){
						    	subacid = $("input[name ='subaccode']").val();
						    	$("input[name ='acc_code_show']").val(subacid);
						    }else{
						    	$("input[name ='acc_code_show']").val(accountcode);
						    }
							
						}
						
					}
				});
			}else{
				alert("Please enter valid  account code");
				$("input[name ='acc_code']").val('');
				var subacid ="";
			    if(document.create_formcoa.acc_is_sub.checked){
			    	subacid = $("input[name ='subaccode']").val();
			    	$("input[name ='acc_code_show']").val(subacid);
			    }else{
			    	$("input[name ='acc_code_show']").val($("input[name ='acc_code_pre']").val());
			    }
				
			}
		}else{
			alert("Please enter account code");
			$("input[name ='acc_code']").val('');
			var subacid ="";
		    if(document.create_formcoa.acc_is_sub.checked){
		    	subacid = $("input[name ='subaccode']").val();
		    	$("input[name ='acc_code_show']").val(subacid);
		    }else{
		    	$("input[name ='acc_code_show']").val($("input[name ='acc_code_pre']").val());
		    }
			
		}
	}
	
</script>
