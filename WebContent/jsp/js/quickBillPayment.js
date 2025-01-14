$(document).ready(function(){
	var plant = document.form.plant.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	$(".payment-table tbody").on('click','.payment-action',function(){
	    $(this).parent().parent().remove();
	    calculatetotal();
	});
	
	   
	$(document).on("focusout","#bankCharge", function(){
		var value = $(this).val();
		var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(value.match(decimal) || value.match(numbers)) 
		{ 
			var numberOfDecimal = document.getElementById("numberOfDecimal").value;
			var paidamount = document.form.amount_paid.value;
			var amount=parseFloat(value).toFixed(numberOfDecimal);
			$(this).val(amount);
			
			if(value.length > 0){
				 /*$(".bankAccountSection").show();
				 $("#bankAccountSearch").prop('disabled', false); */
				 
				 if(parseFloat(paidamount) > parseFloat(value)){
					 $(".bankAccountSection").show();
					 $("#bankAccountSearch").prop('disabled', false); 
				}else{
					$(this).val('');
					 $(".bankAccountSection").hide();
					 $("#bankAccountSearch").prop('disabled', true);
					 $("input[name ='bank_name']").typeahead('val','');
					 $("input[name ='bank_branch']").val('');
					 alert("Please Enter Bank Charges Less Than Payment Made");
				}
			 }else{
				 $(".bankAccountSection").hide();
				 $("#bankAccountSearch").prop('disabled', true);
				 $("input[name ='bank_name']").typeahead('val','');
				 $("input[name ='bank_branch']").val('');
			 }
		}else{
			if(value.length > 0){
			 $(this).val('');
			 $(".bankAccountSection").hide();
			 $("#bankAccountSearch").prop('disabled', true);
			 $("input[name ='bank_name']").typeahead('val','');
			 $("input[name ='bank_branch']").val('');
			 alert("Please Enter Valid Bank Charges");
			}
		}
	});
	
	
	/* Paid Through Auto Suggestion */
	$("#paid_through_account_name").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'smalldrop'
			  }
		},
		{	  
		  display: 'accountname',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/ChartOfAccountServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					action : "getSubAccountTypeGroup",
					module:"billpaymentpaidthrough",
					ITEM : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.results);
				}
					});
			},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(item) {
				if (item.issub) {
					var $state = $(
						    '<span onclick="isbankcharge(this,\''+item.accountname+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
						  );
					}
				else
					{
					var $state = $(
							 '<span onclick="isbankcharge(this,\''+item.accountname+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
						  );
					}
				  
				  return $state;
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".smalldrop");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".smalldrop").css('display') != "block")
				menuElement.next().hide();	  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".smalldrop");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".smalldrop");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	
	/* Payment Mode Auto Suggestion */
	/*$("#payment_mode").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{	  
		  display: 'PAYMENTTYPE',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					action : "GET_PAYMENT_TYPE_LIST",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.payTypes);
				}
					});
			},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
				return '<p>' + data.PAYMENTTYPE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentTypeModal"><a href="#"> + New Payment Mode</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();	  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});*/
	
	/* Bank Auto Suggestion
	$("#bankAccountSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{	  
		  display: 'NAME',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/BankServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					action : "GET_BRANCH_NAME_FOR_AUTO_SUGGESTION",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.orders);
				}
					});
			},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
				return '<div onclick="document.form.bank_branch.value=\''+data.BRANCH_CODE+'\';"><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="bnkAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();	  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}); */
	
	/* Payment Mode Auto Suggestion */
	$("#payment_mode").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{	  
		  display: 'PAYMENTMODE',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/PaymentModeMst";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					action : "GET_PAYMENT_MODE_LIST",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.PAYMENTMODE);
				}
					});
			},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
				return '<p>' + data.PAYMENTMODE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal"><a href="#"> + New Payment Mode</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();	  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}).on('typeahead:select',function(event,selection){
			//loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.PURCHASEUOM);
			var isbank = $("input[name ='isbank']").val();
			var pthrough = $("input[name ='paid_through_account_name']").val();
			if(selection.PAYMENTMODE != "Cheque"){
				$("input[name ='paidpdcstatus']").val("0");
				$("input[name ='bank_charge']").val("");
				$("#bankCharge").attr("readonly", true);
				$(".hidepdc").hide();
			}else{
				if(isbank == "1"){
					addnewRow();
					$("input[name ='paidpdcstatus']").val("1");
					$("#bankCharge").attr("readonly", false);
					$(".hidepdc").show();
				}else{
					$("input[name ='paidpdcstatus']").val("0");
					$("input[name ='bank_charge']").val("");
					$("#bankCharge").attr("readonly", true);
					$(".hidepdc").hide();
					if(pthrough != ""){
						$("#paid_through_account_name").typeahead('val', '');
						alert("Please select Bank Balance account type");
					}
				}
			}
		});
	
	$("#billPaymentAttch").change(function(){
		var files = $(this)[0].files.length;
		var sizeFlag = false;
			if(files > 5){
				$(this)[0].value="";
				alert("You can upload only a maximum of 5 files");
				$("#billPaymentAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
			}else{
				for (var i = 0; i < $(this)[0].files.length; i++) {
				    var imageSize = $(this)[0].files[i].size;
				    if(imageSize > 2097152 ){
				    	sizeFlag = true;
				    }
				}	
				if(sizeFlag){
					$(this)[0].value="";
					alert("Maximum file size allowed is 2MB, please try with different file.");
					$("#billPaymentAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
				}else{
					$("#billPaymentAttchNote").html(files +" files attached");
				}
				
			}
		});
});

function bankCallback(bankData){
	if(bankData.STATUS="SUCCESS"){		
		$("input[name ='bank_name']").typeahead('val', bankData.BANK_NAME);
		$("input[name ='bank_branch']").val(bankData.BANK_BRANCH_CODE);
		alert(bankData.MESSAGE);
	}
}
function paymentTypeCallback(paymentTypeData){
	if(paymentTypeData.STATUS="100"){
		$("input[name ='payment_mode']").typeahead('val', paymentTypeData.PAYMENTMODE);
		//alert(paymentTypeData.MESSAGE);
	}
}

//jsp validation
function checkpaymenttype(paytype){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PAYMENTMODE : paytype,
				ACTION : "PAYTYPE_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Payment Does't Exists");
						document.getElementById("payment_mode").focus();
						$("#payment_mode").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
//jsp validation end

function validatePayment(){
	
	calculatecurrency();
	
	var paymentPaid = document.form.amount_paid.value;
	var paymentDate = document.form.payment_date.value;
	var paymentThrough = document.form.paid_through_account_name.value;
	var paymentMode = document.form.payment_mode.value;
	
	//RESVI START
	var ValidNumber   = document.form.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" Payment's you can create"); return false; }
	// END
		
	var bankCharges = document.form.bank_charge.value;
	/*var bankName = document.form.bank_name.value;
	var chequeNo = document.form.checqueNo.value;
	var chequedate = document.form.cheque_date.value;*/
	var pdastatus=$('input[name = "paidpdcstatus"]').val();
	var pdcamount=$('input[name = "pdcamount"]').val();
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var CURREQT = document.form.CURRENCYUSEQT.value;
	

	
	if (CURREQT.indexOf('.') == -1) CURREQT += ".";
 	var decNum = CURREQT.substring(CURREQT.indexOf('.')+1, CURREQT.length);
 	if (decNum.length > numberOfDecimal)
 	{
 		alert("Invalid more than "+numberOfDecimal+" digits after decimal in Exchange Rate");
 		document.form.CURRENCYUSEQT.focus();
 		return false;
 		
 	}
	
	if(paymentPaid == ""){
		alert("Please enter payment amount.");
		document.form.amount_paid.focus();
		return false;
	}
	
	if(paymentDate == ""){
		alert("Please enter date of payment .");
		document.form.payment_date.focus();
		return false;
	}
	
	if(paymentThrough == ""){
		alert("Please choose paid through.");
		document.form.paid_through_account_name.focus();
		return false;
	}
	
	if(paymentMode == ""){
		alert("Please choose mode of payment.");
		document.form.payment_mode.focus();
		return false;
	}
	
	
	var allowedAmount = document.form.allowedAmount.value;
	if(parseFloat(paymentPaid) > parseFloat(allowedAmount)){
		alert("Payment made cannot be greater than "+allowedAmount);
		document.form.amount_paid.focus();
		return false;
	}
	
if(pdastatus == "1"){
	
		var isItemValid = true;
		var ischeck = false;
		$("input[name ='pdc']").each(function() {
			if($(this).is(":checked")){
				ischeck = true;
		    }
		});
		
		if(ischeck){
			$("input[name ='pdc']").each(function() {
				if(!$(this).is(":checked")){
					isItemValid = false;
			    }
			});
		}else{
			$("input[name ='pdc']").each(function() {
				if($(this).is(":checked")){
					isItemValid = false;
			    }
			});
		}
		
		if(!isItemValid){
			alert("Payment not allow with PDC and CDC. It should be either PDC(Check All) or CDC(UnCheck All)");
			return false;
		}
	
		
		var isItemValid = true;
		$("input[name ='bankname']").each(function() {
		    if($(this).val() == ""){
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The bank field cannot be empty.");
			return false;
		}	
		
		$("input[name ='cheque-no']").each(function() {
		    if($(this).val() == ""){	
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque number field cannot be empty.");
			return false;
		}
		
		$("input[name ='cheque-date']").each(function() {
		    if($(this).val() == ""){
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque date field cannot be empty.");
			return false;
		}
		
		$("input[name ='cheque-amount']").each(function() {
		    if(parseFloat($(this).val()) == parseFloat("0")){	
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque amount field cannot be zero.");
			return false;
		}
		
		if(parseFloat(pdcamount) != parseFloat(paymentPaid)){
			alert("Cheque amount should be equal to Payment made");
			return false;
		}
	}
	
	document.form.amount.value = document.form.amount_paid.value;
	
	$('.datepicker').prop('disabled',false);
	
	
	return true;
}

function amountchanged(amountnode)
{
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var tamount = $("input[name ='allowedAmount']").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var dtamount=parseFloat(tamount).toFixed(numberOfDecimal);
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	if(amountnode.value.match(decimal) || amountnode.value.match(numbers)) 
	{ 
		var amount=parseFloat(amountnode.value).toFixed(numberOfDecimal);
		if(parseFloat(amount) <= parseFloat(dtamount)){
			$("input[name ='amount_paid']").val(amount);
			$("input[name ='cheque-amount']").each(function() {
			    $(this).val(zeroval);
			});
			calculatetotal();
			calculatecurrency();
		}else{
			$("input[name ='amount_paid']").val('');
			alert("Amount Exceeds The Balance Due");
		}
		
	}else{
		$("input[name ='amount_paid']").val('');
		alert("Please Enter Valid Payment Made");
	}
	
}
function loadCOAData(){
	var accName=$("#create_account_modal #acc_name").val();
	$("#paid_through_account_name").val(accName);
}


function addRow(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var bankname = $("input[name ='vbank']").val();
	var newdate = $("input[name ='payment_date']").val();
	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="pdcstatus" value="0">';
	body += '<input type="checkbox" class="form-check-input" id="pdc" name="pdc" Onclick="checkpdc(this)">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input name="bank_branch" type="hidden" value="">';
	if(bankname.length > 0){
		body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" value="'+bankname+'" readonly>';
	}else{
		body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" placeholder="Select a bank" readonly>';
	}
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" type="text" name="cheque-no" placeholder="Enter Cheque No">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date" value="'+newdate+'" disabled>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle payment-action" aria-hidden="true"></span>';
	body += '<input class="form-control text-right" type="text" name="cheque-amount" onchange="calculateAmount(this)" value="'+zeroval+'">';
	body += '</td>';
	body += '</tr>';
	$(".payment-table tbody").append(body);
	removerowclasses();
	addrowclasses();
}



function addnewRow(){
	$(".payment-table tbody").html("");
	var numberOfDecimal = $("#numberOfDecimal").val();
	var bankname = $("input[name ='vbank']").val();
	var newdate = $("input[name ='payment_date']").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="pdcstatus" value="0">';
	body += '<input type="checkbox" class="form-check-input pdc" name="pdc" Onclick="checkpdc(this)">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input name="bank_branch" type="hidden" value="">';
	if(bankname.length > 0){
		body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" value="'+bankname+'" readonly>';
	}else{
		body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" placeholder="Select a bank" readonly>';
	}
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" type="text" name="cheque-no" placeholder="Enter Cheque No">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date" value="'+newdate+'" disabled>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<input class="form-control text-right" type="text" name="cheque-amount" onchange="calculateAmount(this)" value="'+zeroval+'">';
	body += '</td>';
	body += '</tr>';
	$(".payment-table tbody").append(body);
	$("#subTotal").html(parseFloat("0.0").toFixed(numberOfDecimal));
	$("#balamount").html(parseFloat("0.0").toFixed(numberOfDecimal));
	 $("input[name ='pdcamount']").val("0");
	removerowclasses();
	addrowclasses();
}


$(document).on("focus", ".bankAccountSearch" , function() {
	$(this).typeahead('val', '"');
	$(this).typeahead('val', '');	
});


function removerowclasses(){
	/*$(".bankAccountSearch").typeahead('destroy');*/
}


function addrowclasses(){
	var plant = document.form.plant.value;
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
	
		/*$(".bankAccountSearch").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'NAME',   
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/BankServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						action : "GET_BRANCH_NAME_FOR_AUTO_SUGGESTION",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.orders);
					}
						});
				},
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
					return '<span onclick="addbankcode(this,\''+data.BRANCH_CODE+'\')" ><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></span>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="bnkAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 150);
			});*/
}


function checkpdc(obj){
	 var newdate = $("input[name ='payment_date']").val();
	 if ($(obj).is(":checked")){
		 $(obj).closest('td').find("input[name ='pdcstatus']").val("1");
		 $(obj).closest('tr').find("input[name ='cheque-date']").prop('disabled',false);
	 }else{
		 $(obj).closest('td').find("input[name ='pdcstatus']").val("0");
		 $(obj).closest('tr').find('input[name = "cheque-date"]').val(newdate);
		 $(obj).closest('tr').find("input[name ='cheque-date']").prop('disabled',true);
	 }
}

function addbankcode(obj,branchcode){
	$(obj).closest('td').find('input[name = "bank_branch"]').val(branchcode);
}


function calculateAmount(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var subtotal = "0";
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	var amountpaid =  $("input[name ='amount_paid']").val();
	if(!(parseFloat(amountpaid)>0)){
		$(".payment-table tbody tr td:last-child").each(function() {
			$(this).find('input').val(parseFloat("0.0").toFixed(numberOfDecimal));
	});
	}else{
		$(".payment-table tbody tr td:last-child").each(function() {
			var amount = $(this).find('input').val();
			if(amount.match(decimal) || amount.match(numbers)) 
			{ 
				subtotal =  parseFloat(subtotal) + parseFloat(amount);
				$(this).find('input').val(parseFloat(amount).toFixed(numberOfDecimal));
			}else{
				$(this).find('input').val(parseFloat("0.0").toFixed(numberOfDecimal));
				alert("Please Enter Valid Cheque Amount")
			}
		});
	}
	
	if(parseFloat(subtotal) <= parseFloat(amountpaid)){
		 $("#subTotal").html(parseFloat(subtotal).toFixed(numberOfDecimal));
		 $("#balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(subtotal)).toFixed(numberOfDecimal));
		 $("input[name ='pdcamount']").val(subtotal);
	}else{
		alert("Cheque amount exceeds payment made")
		$(obj).val(parseFloat("0.0").toFixed(numberOfDecimal));
		var esubtotal = "0";
		$(".payment-table tbody tr td:last-child").each(function() {
			var amount = $(this).find('input').val();
			esubtotal =  parseFloat(esubtotal) + parseFloat(amount);
		});
		$("#subTotal").html(parseFloat(esubtotal).toFixed(numberOfDecimal));
		$("#balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(esubtotal)).toFixed(numberOfDecimal));
		$("input[name ='pdcamount']").val(esubtotal);
	}
	
}

function calculatetotal(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amountpaid =  $("input[name ='amount_paid']").val();
	var subtotal = "0";
	$(".payment-table tbody tr td:last-child").each(function() {
		var amount = $(this).find('input').val();
		subtotal =  parseFloat(subtotal) + parseFloat(amount);
		$(this).find('input').val(parseFloat(amount).toFixed(numberOfDecimal));
	});
	$("#subTotal").html(parseFloat(subtotal).toFixed(numberOfDecimal));
	$("#balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(subtotal)).toFixed(numberOfDecimal));
	$("input[name ='pdcamount']").val(subtotal);
}


/*function isbankcharge(obj,acountname){
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/ChartOfAccountServlet",
		async : true,
		data : {
			PLANT : plant,
			action : "isbankcharge",
			accountname : acountname
		},
		dataType : "json",
		success : function(data) {
			if(data.status == "OK"){
				$("input[name ='vbank']").val(acountname);
				var pdcstatus = $("input[name ='paidpdcstatus']").val();
				if(pdcstatus == "1"){
					$("input[name ='bankname']").each(function() {
					    $(this).val(acountname);
					});
				}else{
					addnewRow();
					$("input[name ='paidpdcstatus']").val("1");
					$("#bankCharge").attr("readonly", false);
					$(".hidepdc").show();
				}
			}else{
				$("input[name ='paidpdcstatus']").val("0");
				$("input[name ='bank_charge']").val("");
				$("#bankCharge").attr("readonly", true);
				$(".hidepdc").hide();
			}
		}
	});
}*/
function isbankcharge(obj,acountname){
	var plant = document.form.plant.value;
	var paymode =  $("input[name ='payment_mode']").val();
	$.ajax({
		type : "POST",
		url : "/track/ChartOfAccountServlet",
		async : true,
		data : {
			PLANT : plant,
			action : "isbankcharge",
			accountname : acountname
		},
		dataType : "json",
		success : function(data) {
			if(data.status == "OK"){
				$("input[name ='isbank']").val("1");
				$("input[name ='vbank']").val(acountname);
				var pdcstatus = $("input[name ='paidpdcstatus']").val();
				if(pdcstatus == "1"){
					$("input[name ='bankname']").each(function() {
					    $(this).val(acountname);
					});
				}else{
					if(paymode == "Cheque"){
						addnewRow();
						$("input[name ='paidpdcstatus']").val("1");
						$("#bankCharge").attr("readonly", false);
						$(".hidepdc").show();
					}
				}
			}else{
				$("input[name ='isbank']").val("0");
				$("input[name ='paidpdcstatus']").val("0");
				$("input[name ='bank_charge']").val("");
				$("#bankCharge").attr("readonly", true);
				$(".hidepdc").hide();
				if(paymode == "Cheque"){
					$("#paid_through_account_name").typeahead('val', '');
					alert("Please select Bank Balance account type");
				}
			}
		}
	});
}

function vendornoandbank(vno,vbank){
	
	$("input[name ='vendno']").val(vno);
	//$("input[name ='vbank']").val(vbank);
	$("input[name ='bankname']").val(vbank);
	
}




function calculatecurrency(){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = document.getElementById("CURRENCYUSEQT").value;
	var amountnode = document.getElementById("amount_paid").value;

	var baseamount=parseFloat(amountnode) * parseFloat(baseCurrency);
	$("input[name ='conv_amount_paid_Curr']").val(baseamount);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	$("input[name ='amount_paid_Curr']").val(baseamount);

	}

function currencychanged(data)
{
	calculatecurrency();
}