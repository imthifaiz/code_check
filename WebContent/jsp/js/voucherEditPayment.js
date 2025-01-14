function invoicepaymentchange(changenode,invoiceamount)
	 {
			var initailAmount=invoiceamount;
			var numberOfDecimal = document.getElementById("numberOfDecimal").value;
			var amount= changenode.value;
			if(amount>initailAmount)
				{
					alert("Please enter amount equal/less than invoice amount");
					
					changenode.value=parseFloat(initailAmount).toFixed(numberOfDecimal);;
				}
			else
				{
				var changedvalue=parseFloat(amount).toFixed(numberOfDecimal);
					changenode.value=changedvalue;
					
				}
			callTotalDetail(numberOfDecimal);
	 }
function callTotalDetail(numberOfDecimal)
{
	var x = document.getElementsByClassName("paymentamountclass");
	var i;
	var totalamount=0;
	var amount;
	for (i = 0; i < x.length; i++) {
		amount=parseFloat(x[i].value);
		totalamount+=amount;
	}
	var totalInvoiceAmount=parseFloat(totalamount).toFixed(numberOfDecimal);
	document.getElementById("totalInvoiceAmount").innerHTML=totalInvoiceAmount;
	document.getElementById("amountufp").innerHTML=totalInvoiceAmount;
	var amountExcess=document.getElementById("amount").value-totalInvoiceAmount;
	amountExcess=parseFloat(amountExcess).toFixed(numberOfDecimal);
	document.getElementById("amountexcess").innerHTML=amountExcess;
}

$(document).ready(function(){
	var plant = $("#plant").val();
	var numberOfDecimal = $("#numberOfDecimal").val();
	var bankchrges=$('#bankcharges').val();
	var headerid=$('#headerid').val();
	bankchrges=parseFloat(bankchrges);
	if(bankchrges>0)
	{
	$("#bankcharges").attr("readonly", false);
	}
	addchequedetails(headerid);
	
	$(".payment-table tbody").on('click','.payment-action',function(){
	    $(this).parent().parent().remove();
	    calculatetotal();
	});
	
	
	 $('#invoiceEditPaymentForm').submit(function(event) {
		var urlStr = "/track/InvoicePayment?CMD=V_EDIT";
		event.preventDefault();
		if(validatePayment()){
	        var formData = new FormData(document.getElementById("invoiceEditPaymentForm"));
	        var amountufp=$('#amountufp').html();
	        formData.append('amountufp', amountufp);
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				enctype: 'multipart/form-data',
	            xhr: function() {
	                var myXhr = $.ajaxSettings.xhr();
	                return myXhr;
	            },
	            success: function (data) {
	                window.location.href="../banking/invoicepaysummary";
	            },
	            data: formData,
	            cache: false,
	            contentType: false,
	            processData: false
				   });
		}
	 });
	 
	 $('#amount').on('change', function () {
			var numberOfDecimal = $("#numberOfDecimal").val();
			var initailAmount=$('#initialAmount').val();
			initailAmount=parseFloat(initailAmount);
			var amount= $(this).val();
			amount=parseFloat(amount);
			
			var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
			var numbers = /^[0-9]+$/;
			if($(this).val().match(decimal) || $(this).val().match(numbers)) 
			{ 
				
					$(this).val(parseFloat(amount).toFixed(numberOfDecimal));
				
			}else{
				$(this).val(parseFloat(initailAmount).toFixed(numberOfDecimal));
				alert("Please Enter Valid Amount");
			}
		});
	 
	
	 $('#invAttch').on('change', function () {
			var files = $(this)[0].files.length;
			var sizeFlag = false;
				if(files > 5){
					$(this)[0].value="";
					alert("You can upload only a maximum of 5 files");
					$("#billAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
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
						$("#billAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
					}else{
						$("#billAttchNote").html(files +" files attached");
					}
					
				}
			});
	 /* Bank Charges */
	 $(document).on("focusout","#bankcharges", function(){
			/*var value = $(this).val();
			var newvalue=parseFloat(value);
			if(value.length > 0 && newvalue>0){
				 $(".bankAccountSection").show();
				 $("#bankAccountSearch").prop('disabled', false); 
			 }else{
				 $(".bankAccountSection").hide();
				 $("#bankAccountSearch").prop('disabled', true);
				 $("input[name ='bank_name']").typeahead('val','');
				 $("input[name ='bank_branch']").val('');
			 }*/
		 var value = $(this).val();
			
			var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
			var numbers = /^[0-9]+$/;
			if(value.match(decimal) || value.match(numbers)) 
			{ 
				var numberOfDecimal = document.getElementById("numberOfDecimal").value;
				var paidamount = document.form1.amount.value;
				var amount=parseFloat(value).toFixed(numberOfDecimal);
				$(this).val(amount);
				
				if(value.length > 0){
					if(parseFloat(paidamount) > parseFloat(value)){
						 $(".bankAccountSection").show();
						 $("#bankAccountSearch").prop('disabled', false); 
					}else{
						$(this).val('');
						 $(".bankAccountSection").hide();
						 $("#bankAccountSearch").prop('disabled', true);
						 $("input[name ='bank_name']").typeahead('val','');
						 $("input[name ='bank_branch']").val('');
						 alert("Please Enter Bank Charges Less Than Amount");
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
	 /* Bank Auto Suggestion */
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
					return '<div onclick="document.form1.bank_branch.value=\''+data.BRANCH_CODE+'\';"><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></div>';
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
			});
		
		$("#paid_through_account_name").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true,
			  classNames: {
				 	menu: 'bigdrop'
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
						module:"invoicepaymentdepositto",
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
							    '<span onclick="isbankcharge(\''+item.accountname+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
							  );
						}
					else
						{
						var $state = $(
								 '<span onclick="isbankcharge(\''+item.accountname+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
							  );
						}
					  
					  return $state;
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".bigdrop").css('display') != "block")
					menuElement.next().hide();	  
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".bigdrop");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
	 /* Payment Mode Auto Suggestion */
		$("#payment_mode").typeahead({
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
			});
		
		/* project Auto Suggestion */
		 $('#project').typeahead({
		 	  hint: true,
		 	  minLength:0,  
		 	  searchOnFocus: true
		 	},
		 	{
		 	  display: 'PROJECT_NAME',  
		 	  async: true,   
		 	  source: function (query, process,asyncProcess) {
		 		  var urlStr = "../FinProject";
		 			$.ajax( {
		 				type : "POST",
		 				url : urlStr,
		 				async : true,
		 				data : {
		 					action : "GET_PROJECT_LIST",
		 					CUSTNO : "",
		 					QUERY : query
		 				},
		 				dataType : "json",
		 				success : function(data) {
		 					return asyncProcess(data.PROJECT);
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
		 	    	return '<div><p class="item-suggestion">Project Name: ' + data.PROJECT_NAME + '</p><br/><p class="item-suggestion">Project No: ' + data.PROJECT + '</p><br/><p class="item-suggestion">Customer No: ' + data.CUSTNO + '</p></div>';
		 		}
		 	  }
		 	}).on('typeahead:render',function(event,selection){
		 		var menuElement = $(this).parent().find(".tt-menu");
		 		var top = menuElement.height()+35;
		 		top+="px";
		 		$('.shipCustomerAddBtn').remove();  
		 		/*if(displayCustomerpop == 'true'){
		 		$('.shipCustomer-section .tt-menu').after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#" onclick="document.form1.custModal.value=\'shipcust\'"> + New Cutomer</a></div>');
		 		}*/
		 		$(".shipCustomerAddBtn").width(menuElement.width());
		 		$(".shipCustomerAddBtn").css({ "top": top,"padding":"3px 20px" });			
		 		if($(this).parent().find(".tt-menu").css('display') != "block")
		 			menuElement.next().hide();
		 	}).on('typeahead:open',function(event,selection){
		 		$('.shipCustomerAddBtn').show();
		 		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		 		element.toggleClass("glyphicon-menu-up",true);
		 		element.toggleClass("glyphicon-menu-down",false);    
		 	}).on('typeahead:close',function(){
		 		setTimeout(function(){ $('.shipCustomerAddBtn').hide();}, 150);	
		 		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		 		element.toggleClass("glyphicon-menu-up",false);
		 		element.toggleClass("glyphicon-menu-down",true);
		 	}).on('typeahead:change',function(event,selection){
		 		
		 	}).on('typeahead:select',function(event,selection){
		 		$("input[name=PROJECTID]").val(selection.ID);
		 	});
});
function validatePayment(){
	var bankCharges = document.form1.bankcharges.value;
	var paymentmode = document.form1.payment_mode.value;
	var depositto = document.form1.paid_through_account_name.value;
	
	
	if(paymentmode == ""){
		alert("Please choose Mode of receipt.");
		document.form1.payment_mode.focus();
		return false;
	}
	
	if(depositto == ""){
		alert("Please choose Deposit To.");
		document.form1.paid_through_account_name.focus();
		return false;
	}
	
	var pdastatus=$('input[name = "paidpdcstatus"]').val();
	var pdcamount=$('input[name = "pdcamount"]').val();
	var amount_paid=$('input[name = "amount"]').val();
	
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
			alert("check all pdc or uncheck all pdc");
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
		
		if(parseFloat(pdcamount) != parseFloat(amount_paid)){
			alert("Cheque amount should be equal to amount received");
			return false;
		}
	}
	
	$(".bankAccountSearch").prop('disabled', false); 
	$("#paid_through_account_name").prop('disabled', false);  
	$(".pdc").prop('disabled', false); 
	$('.datepicker').prop('disabled',false);
	return true;

}
function viewFile(id)
{
	 var urlStrAttach = "/track/InvoicePayment?CMD=downloadAttachmentById&attachid="+id;
	 var xhr=new XMLHttpRequest();
	 xhr.open("GET", urlStrAttach, true);
	 //Now set response type
	 xhr.responseType = 'arraybuffer';
	 xhr.addEventListener('load',function(){
	   if (xhr.status === 200){
	     console.log(xhr.response) // ArrayBuffer
	     console.log(new Blob([xhr.response])) // Blob
	     var datablob=new Blob([xhr.response]);
	     var a = document.createElement('a');
         var url = window.URL.createObjectURL(datablob);
         a.href = url;
         a.target = '_blank';
         document.body.append(a);
         a.click();
         a.remove();
         window.URL.revokeObjectURL(url); 
	   }
	 })
	 xhr.send();
}
function downloadFile(id,fileName)
{
	 var urlStrAttach = "/track/InvoicePayment?CMD=downloadAttachmentById&attachid="+id;
	 var xhr=new XMLHttpRequest();
	 xhr.open("GET", urlStrAttach, true);
	 //Now set response type
	 xhr.responseType = 'arraybuffer';
	 xhr.addEventListener('load',function(){
	   if (xhr.status === 200){
	     console.log(xhr.response) // ArrayBuffer
	     console.log(new Blob([xhr.response])) // Blob
	     var datablob=new Blob([xhr.response]);
	     var a = document.createElement('a');
         var url = window.URL.createObjectURL(datablob);
         a.href = url;
         a.download = fileName;
         document.body.append(a);
         a.click();
         a.remove();
         //window.URL.revokeObjectURL(url); 
	   }
	 })
	 xhr.send();
}
function removeFile(id)
{
	var urlStrAttach = "/track/InvoicePayment?CMD=removeAttachmentById&removeid="+id;	
	$.ajax( {
		type : "GET",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
}

/*function amountchanged(amountnode)
{
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	if(amountnode.value.match(decimal) || amountnode.value.match(numbers)) 
	{ 
		var numberOfDecimal = document.getElementById("numberOfDecimal").value;
		var amount=parseFloat(amountnode.value).toFixed(numberOfDecimal);
		$("input[name ='amount']").val(amount);
		
		
	}else{
		$("input[name ='amount']").val('0');
		alert("Please Enter Valid Amount");
	}

	
}*/

function addchequedetails(id){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var urlStr = "/track/InvoicePayment?CMD=getpdcbyid";
	$.ajax( {
		type : "GET",
		url : urlStr,
		data: {
			paymentid:id
		},
        success: function (data) {
        	var json = $.parseJSON(data);
        	var pdcList=json.PDC;
        	if(pdcList.length > 0){
        		$(".payment-table tbody").html("");
        		$.each(pdcList, function( key, data ) {
        			
        			var body="";
        			if(data.STATUS == "PROCESSED"){
        				body += '<tr>';
            			body += '<td class="text-center">';
            			body += '<input type="hidden" name="pdcstatus" value="'+data.ISPDC+'">';
            			body += '<input type="hidden" name="chequestatus" value="'+data.STATUS+'">';
            			body += '<input type="hidden" name="pdcid" value="'+data.ID+'">';
            			if(data.ISPDC == "1"){
            				body += '<input type="checkbox" disabled class="form-check-input pdc" name="pdc" Onclick="checkpdc(this)" checked>';
            			}else{
            				body += '<input type="checkbox" disabled class="form-check-input pdc" name="pdc" Onclick="checkpdc(this)">';
            			}
            			body += '</td>';
            			body += '<td class="text-center">';
            			body += '<input name="bank_branch" type="hidden" value="">';
            			body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" value="'+data.BANK_BRANCH+'" disabled>';
            			body += '</td>';
            			body += '<td class="text-center">';
            			body += '<input class="form-control text-left" type="text" name="cheque-no" placeholder="Enter Cheque No" value="'+data.CHECQUE_NO+'" readonly>';
            			body += '</td>';
            			body += '<td class="text-center">';
            			if(data.ISPDC == "1"){
            				body += '<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date" value="'+data.CHEQUE_DATE+'" readonly>';
            			}else{
            				body += '<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date" value="'+data.CHEQUE_DATE+'" disabled>';
            			} 
            			body += '</td>';
            			body += '<td class="text-center grey-bg" style="position:relative;">';
            			//body += '<span class="glyphicon glyphicon-remove-circle payment-action" aria-hidden="true"></span>';
            			body += '<input class="form-control text-right" type="text" name="cheque-amount" onchange="calculateAmount(this)" value="'+parseFloat(data.CHEQUE_AMOUNT).toFixed(numberOfDecimal)+'" readonly>';
            			body += '</td>';
            			body += '</tr>';
            			$("#paid_through_account_name").attr("disabled", true);
        			}else{
        				body += '<tr>';
            			body += '<td class="text-center">';
            			body += '<input type="hidden" name="pdcstatus" value="'+data.ISPDC+'">';
            			body += '<input type="hidden" name="chequestatus" value="'+data.STATUS+'">';
            			body += '<input type="hidden" name="pdcid" value="'+data.ID+'">';
            			if(data.ISPDC == "1"){
            				body += '<input type="checkbox" class="form-check-input pdc" name="pdc" Onclick="checkpdc(this)" checked>';
            			}else{
            				body += '<input type="checkbox" class="form-check-input pdc" name="pdc" Onclick="checkpdc(this)">';
            			}
            			body += '</td>';
            			body += '<td class="text-center">';
            			body += '<input name="bank_branch" type="hidden" value="">';
            			body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" value="'+data.BANK_BRANCH+'">';
            			body += '</td>';
            			body += '<td class="text-center">';
            			body += '<input class="form-control text-left" type="text" name="cheque-no" placeholder="Enter Cheque No" value="'+data.CHECQUE_NO+'">';
            			body += '</td>';
            			body += '<td class="text-center">';
            			if(data.ISPDC == "1"){
            				body += '<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date" value="'+data.CHEQUE_DATE+'" readonly>';
            			}else{
            				body += '<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date" value="'+data.CHEQUE_DATE+'" disabled>';
            			} 
            			body += '</td>';
            			body += '<td class="text-center grey-bg" style="position:relative;">';
            			body += '<span class="glyphicon glyphicon-remove-circle payment-action" aria-hidden="true"></span>';
            			body += '<input class="form-control text-right" type="text" name="cheque-amount" onchange="calculateAmount(this)" value="'+parseFloat(data.CHEQUE_AMOUNT).toFixed(numberOfDecimal)+'">';
            			body += '</td>';
            			body += '</tr>';
        			}
        			
        			$(".payment-table tbody").append(body);
        		});
        		calculatetotal();
        		isbankcharge(document.form1.paid_through_account_name.value);
        		removerowclasses();
        		addrowclasses();
        		$(".hidepdc").show();
        	}else{
        		$(".hidepdc").hide();
        	}
        	
        },
        cache: false
		   });
}

function checkpdc(obj){
	var newdate = $("input[name ='payment_date']").val();
	 if ($(obj).is(":checked")){
		 $(obj).closest('td').find("input[name ='pdcstatus']").val("1");
		 $(obj).closest('td').find("input[name ='chequestatus']").val("NOT PROCESSED");
		 $(obj).closest('tr').find("input[name ='cheque-date']").prop('disabled',false);
	 }else{
		 $(obj).closest('td').find("input[name ='pdcstatus']").val("0");
		 $(obj).closest('td').find("input[name ='chequestatus']").val("NOT APPLICABLE");
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
	var amountpaid =  $("input[name ='amount']").val();
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
		alert("Cheque amount exceeds amount received")
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
	var amountpaid =  $("input[name ='amount']").val();
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


function addRow(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var bankname = $("input[name ='vbank']").val();
	var newdate = $("input[name ='payment_date']").val();
	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="pdcstatus" value="0">';
	body += '<input type="hidden" name="chequestatus" value="NOT APPLICABLE">';
	body += '<input type="hidden" name="pdcid" value="0">';
	body += '<input type="checkbox" class="form-check-input pdc" name="pdc" Onclick="checkpdc(this)">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input name="bank_branch" type="hidden" value="">';
	if(bankname.length > 0){
		body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" value="'+bankname+'">';
	}else{
		body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" placeholder="Select a bank">';
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

function removerowclasses(){
	$(".bankAccountSearch").typeahead('destroy');
}

function addrowclasses(){
	var plant = document.form1.plant.value;
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
	
		$(".bankAccountSearch").typeahead({
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
			});
}function removerowclasses(){
	$(".bankAccountSearch").typeahead('destroy');
}

function addrowclasses(){
	var plant = document.form1.plant.value;
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
	
		$(".bankAccountSearch").typeahead({
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
			});
}

function isbankcharge(acountname){
	var plant = document.form1.plant.value;
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
				$("input[name ='paidpdcstatus']").val("1");
				$("#bankcharges").attr("readonly", false);
				$(".hidepdc").show();
				removerowclasses();
				addrowclasses();
			}else{
				$("input[name ='paidpdcstatus']").val("0");
				$("input[name ='bank_charge']").val("");
				$("#bankcharges").attr("readonly", true);
				$(".hidepdc").hide();
			}
		}
	});
}
function paymentTypeCallback(data)
{	
	$("input[name ='payment_mode']").val(data.PAYMENTTYPE);
}