var subWin = null;
var totalBillPaid=0;
var amountreceived=0;
var paidamounttotal=0;
function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    // an array that will be populated with substring matches
	    matches = [];
	    // regex used to determine if a string contains the substring `q`
	    substrRegex = new RegExp(q, 'i');
	    // iterate through the pool of strings and for any string that
	    // contains the substring `q`, add it to the `matches` array
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}

function vendorchanged()
{
	var vendno=document.form.vendno.value;
    if(vendno!=""||vendno!=null)
    	{
		var urlStr = "/track/BillPaymentServlet?CMD=getBillByVend";
		$.ajax( {
			type : "GET",
			url : urlStr,
			data: {
				vendno:vendno
			},
	        success: function (data) {
	        	var json = $.parseJSON(data);
	        	var billList=json.data;
	        	console.log(billList);
	        	if(billList.length>0)
	        		{
		        		$.each(billList,function(i,v){
			        		var stringjson=JSON.stringify(v);
			        		var key="bill_"+v.VENDNO+"_"+v.BILL+"_"+v.PONO+"_"+v.BILL_DATE+"_"+v.TOTAL_AMOUNT;
			        		$("#billlisttablebody").append("<tr>"+
			        				"<input type='hidden' name='billHdrId' value="+v.ID+" />"+
			        				"<input type='hidden' name='pono' value="+v.PONO+"  />"+
			        				"<input type='hidden' name='billamount' value="+v.TOTAL_AMOUNT+"  />"+
			        				"<input name='type' type='hidden' value='REGULAR' />"+
			        				"<td class='text-center'>"+v.BILL+"</td>"+
			        				"<td class='text-center'>"+v.BILL_DATE+"</td>"+
			        				"<td class='text-center'>"+v.TOTAL_AMOUNT+"</td>"+
			        				"<td class='text-center'><input type='text' style='border:1px solid #eee' class='paymentamountclass' id="+key+" name='amount' value='0' onchange='paidamountchanged("+v.TOTAL_AMOUNT+",this)' onkeypress='return isNumberKey(event,this,4)'></input></td>"+
			        				"</tr>");
			        		});
	        		}
	        	else
	        		{
	        			$("#billlisttablebody").html("");
	        		}
	        	
	        	
	        },
	        cache: false
			   });
    	}
//window.location.href="invoicePaymentNew.jsp?CUSTNO="+custno;
}


$(document).ready(function(){
	var plant = document.form.plant.value;
	var trnsid = document.form.paymenthdrid.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	amountreceived=document.getElementById("amount_paid").value;
	amountreceived=parseFloat(amountreceived).toFixed(numberOfDecimal);
	document.getElementById('amountreceived').innerHTML=amountreceived;
	var bankchrge=$('#bankCharge').val();
	if(bankchrge>0)
		{
		$("#bankCharge").attr("readonly", false);
		}
	addchequedetails(trnsid);
	callTotalDetail(numberOfDecimal);
	
	$(".payment-table tbody").on('click','.payment-action',function(){
	    $(this).parent().parent().remove();
	    calculatetotal();
	});
	/* Supplier Auto Suggestion */
	$('#vendname').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_SUPPLIER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
		    return '<p onclick="document.form.vendno.value = \''+data.VENDO+'\'">' + data.VNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.vendno.value = "";
			}
			vendorchanged();
			/* To reset Order number Autosuggestion*/
			$("#pono").typeahead('val', '"');
			$("#pono").typeahead('val', '');
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
			}
		}
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
			  /*source: substringMatcher(states),*/
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
				var menuElement = $(this).parent().find(".smalldrop");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".smalldrop").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".smalldrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
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
			if(selection.PAYMENTMODE != "Cheque"){
				$("input[name ='paidpdcstatus']").val("0");
				$("input[name ='bank_charge']").val("");
				$("#bankCharge").attr("readonly", true);
				$(".hidepdc").hide();
			}else{
				if(isbank == "1"){
					$("input[name ='paidpdcstatus']").val("1");
					$("#bankCharge").attr("readonly", false);
					$(".hidepdc").show();
					
				}else{
					$("input[name ='paidpdcstatus']").val("0");
					$("input[name ='bank_charge']").val("");
					$("#bankCharge").attr("readonly", true);
					$(".hidepdc").hide();
					$("#paid_through_account_name").typeahead('val', '');
					alert("Please select Bank Balance account type");
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

/****Supplier list callback******/
function setSupplierData(suppierData){	
	$("input[name ='vendno']").val(suppierData.vendno);
	$("#vendname").typeahead('val', suppierData.vendname);	
	vendorchanged();
	/* To reset Order number Autosuggestion*/
	$("#pono").typeahead('val', '"');
	$("#pono").typeahead('val', '');
}
function validatePayment()
{
	
	
	calculatecurrency();
	var amount_paid=$('input[name = "amount_paid"]').val();
	if(!(parseFloat(amount_paid)>0))
		{
			alert("Please enter paid amount greater than zero");
			return false;
		}
	var amount_paid_Curr=$('input[name = "amount_paid_Curr"]').val();
	if(!(parseFloat(amount_paid_Curr)>0))
		{
			alert("Please enter paid amount greater than zero");
			return false;
		}
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

	var paymentDate = document.form.payment_date.value;
	var paymentThrough = document.form.paid_through_account_name.value;
	var paymentMode = document.form.payment_mode.value;
	
	var bankCharges = document.form.bank_charge.value;
/*	var bankName = document.form.bank_name.value;
	var chequeNo = document.form.checqueNo.value;
	var chequedate = document.form.cheque_date.value;*/
	
	var pdastatus=$('input[name = "paidpdcstatus"]').val();
	var pdcamount=$('input[name = "pdcamount"]').val();
	
	if(paymentDate == ""){
		alert("Please enter date of payment.");
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
	
	/*
	if(bankCharges > 0){
		if(bankName == ""){
			alert("Please choose bank account.");
			document.form.bank_name.focus();
			return false;
		}
		
		if(chequeNo == ""){
			alert("Please enter cheque number.");
			document.form.checqueNo.focus();
			return false;
		}
		
		if(chequedate == ""){
			alert("Please enter cheque Date.");
			document.form.cheque_date.focus();
			return false;
		}
	}*/
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
		
		if(parseFloat(pdcamount) != parseFloat(amount_paid)){
			alert("Cheque amount should be equal to Payment made");
			return false;
		}
	}

	$(".bankAccountSearch").prop('disabled', false); 
	$("#paid_through_account_name").prop('disabled', false);  
	$(".pdc").prop('disabled', false); 
	$('.datepicker').prop('disabled',false);

	return true;

	
}

function paidamountchanged(invoiceamt,paidnode)
{
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var invoiceamount=parseFloat(invoiceamt);
	var paidamount=parseFloat(paidnode.value);
	if(paidamount>invoiceamount)
		{
			alert("Please enter amount equal/less than invoice amount");
			paidnode.value=invoiceamount;
		}
	else
		{
			paidnode.value=parseFloat(paidamount).toFixed(numberOfDecimal);
		}
	paidamounttotal=totalpaidamount();
	if(paidamounttotal>amountreceived)
		{
			alert("Please enter amount equal/less than received amount");
			paidnode.value=0;
		}
	callTotalDetail(numberOfDecimal);
}


function callTotalDetail(numberOfDecimal)
{
	
	var totalamount=0;
	totalamount=totalpaidamount();
	var totalInvoiceAmount=parseFloat(totalamount).toFixed(numberOfDecimal);
	document.getElementById("totalInvoiceAmount").innerHTML=totalInvoiceAmount;
	document.getElementById("amountufp").innerHTML=totalInvoiceAmount;
	var amountExcess=document.getElementById("amount_paid").value-totalInvoiceAmount;
	amountExcess=parseFloat(amountExcess).toFixed(numberOfDecimal);
	document.getElementById("amountexcess").innerHTML=amountExcess;
	$("input[name ='excessamtcheck']").val(amountExcess);
}
function totalpaidamount()
{
	var totalamount=0;
	var amount=0;
	$('input[name = "amount"]').each(function() {
		amount=$(this).val();
		totalamount+=parseFloat(amount);
	});
	return totalamount;
}
function downloadFile(id,fileName)
{
	 var urlStrAttach = "/track/BillPaymentServlet?CMD=downloadAttachmentById&attachid="+id;
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
	var urlStrAttach = "/track/BillPaymentServlet?CMD=removeAttachmentById&removeid="+id;	
	$.ajax( {
		type : "GET",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
}

function addchequedetails(id){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var urlStr = "/track/BillPaymentServlet?CMD=getpdcbyid";
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
            			body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" value="'+data.BANK_BRANCH+'" readonly>';
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
        		isbankcharge(document.form.paid_through_account_name.value);
        		removerowclasses();
        		addrowclasses();
        		$("input[name ='isbank']").val("1");
        		$(".hidepdc").show();
        	}else{
        		$("input[name ='isbank']").val("0");
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
			/*subtotal =  parseFloat(subtotal) + parseFloat(amount);
			$(this).find('input').val(parseFloat(amount).toFixed(numberOfDecimal));*/
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

function vendornoandbank(vno,vbank){
	
	$("input[name ='vendno']").val(vno);
	$("input[name ='vbank']").val(vbank);
	$("input[name ='bankname']").val(vbank);
	
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

/*function isbankcharge(acountname){
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
					$("input[name ='paidpdcstatus']").val("1");
					$("#bankCharge").attr("readonly", false);
					$(".hidepdc").show();
				}
				removerowclasses();
				addrowclasses();
			}else{
				$("input[name ='paidpdcstatus']").val("0");
				$("input[name ='bank_charge']").val("");
				$("#bankCharge").attr("readonly", true);
				$(".hidepdc").hide();
			}
		}
	});
}*/

function isbankcharge(acountname){
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

function paymentTypeCallback(data)
{	
	$("input[name ='payment_mode']").val(data.PAYMENTMODE);
}


function aCreditExpense(id,pono,vno,balamount,CURRENCYID,CURRENCYUSEQT){
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var zeroshow = parseFloat("0").toFixed(numberOfDecimal);
	var ozero = "0";
	var uuid = $("input[name ='uuid']").val();
	var conv_balanceAmount= parseFloat(parseFloat(balamount)*parseFloat(CURRENCYUSEQT)).toFixed(numberOfDecimal);
	$('#expPayid').text(id);
	$('#creditBalanceAmountdisplayExp').text(' '+parseFloat(balamount).toFixed(numberOfDecimal) +' / '+CURRENCYID +' '+ conv_balanceAmount);
	$('#creditBalanceAmountExp').text(parseFloat(balamount).toFixed(numberOfDecimal));
	$('#creditTotalAmountExp').text(zeroshow);	
	var urlStr = "/track/ExpensesServlet?Submit=showcreditforapply";
	$.ajax( {
		type : "GET",
		url : urlStr,
		data: {
			vendno:vno,
			pono:pono,
			eid:id
		},
        success: function (data1) {
    
        	var obj = JSON.parse(data1);
        	console.log(obj);
        	
        	var body="";
        	var orderbody="";
        	$.each(obj.CREDIT,function(i,v){
        			var adfrom = v.ADVANCEFROM;
					var ponore = v.PONO;
					
					if(ponore == "" || ponore == null){
						var reference ="Excess Payment";
  						if(adfrom == "GENERAL"){
  							reference = v.REFERENCE;
  						}

						var cAmount = v.AMOUNT;
						cAmount = parseFloat(cAmount).toFixed(numberOfDecimal);
						
						var cbalAmount = v.BALANCE;
						cbalAmount = parseFloat(cbalAmount).toFixed(numberOfDecimal);
						
						body +='<tr>';
						body +='<td hidden>'+id+'</td>';
						body +='<td hidden>'+pono+'</td>';
						body +='<td hidden>'+v.PAYHDRID+'</td>';
						body +='<td>'+reference+'</td>';
						body +='<td>'+v.PAYMENT_DATE+'</td>';
						body +='<td class="text-right">'+cAmount+'</td>';
						body +='<td class="text-right">'+cbalAmount+'</td>';
						body +='<td><div class="float-right">';
						body +='<input class="form-control text-right creditAmountExp creditAmountvaluecExp" type="text"data-balance="'+cbalAmount+'" value="'+zeroshow+'" disabled>'
						body +='</div></td>';
						body +='<td hidden>'+v.ID+'</td>';
						body +='<td hidden>'+uuid+'</td>';
						body +='</tr>';
					
					}
        	});
        	var ordertottalamo = "0";
        	$.each(obj.CREDIT,function(i,v){
    			var adfrom = v.ADVANCEFROM;
				var ponore = v.PONO;
				if(ponore == "" || ponore == null){
					
				}else{
					if(pono == ponore){
						var cAmount = v.AMOUNT;
						cAmount = parseFloat(cAmount).toFixed(numberOfDecimal);
						
						var cbalAmount = v.BALANCE;
						cbalAmount = parseFloat(cbalAmount).toFixed(numberOfDecimal);
						ordertottalamo = parseFloat(cbalAmount) + parseFloat(ordertottalamo);
						
						orderbody +='<tr>';
						orderbody +='<td hidden>'+id+'</td>';
						orderbody +='<td hidden>'+pono+'</td>';
						orderbody +='<td hidden>'+v.PAYHDRID+'</td>';
						orderbody +='<td>'+v.PONO+'</td>';
						orderbody +='<td>'+v.PAYMENT_DATE+'</td>';
						orderbody +='<td class="text-right">'+cAmount+'</td>';
						orderbody +='<td class="text-right">'+cbalAmount+'</td>';
						orderbody +='<td><div class="float-right">';
						orderbody +='<input class="form-control text-right creditAmountExp creditAmountvalueExp" type="text"data-balance="'+v.BALANCE+'" value="'+zeroshow+'">'
						orderbody +='</div></td>';
						orderbody +='<td hidden>'+v.ID+'</td>';
						orderbody +='<td hidden>'+ozero+'</td>';
						orderbody +='</tr>';
					}
				}
    	});
        	
        	$("input[name ='ordertotalExp']").val(ordertottalamo);
        	$("input[name ='totalbalpopupExp']").val(balamount );
        	
			$(".creditListTableExp tbody").html(body);
			$(".ordercreditListTableExp tbody").html(orderbody);	
			
			if(ordertottalamo > 0){
  
        	}else{
        		
        		$( ".creditAmountvaluecExp" ).prop( "disabled", false );
        	}
        }
		
	});

}


function applyCreditExp(){

	//Resvi start
	  var ValidNumber   = document.creditFormExpense.ValidNumberExp.value;
	  if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" Payment's you can create"); return false; }
	// end
	
	
	var CURRENCYUSEQTs = $("#B_CURRENCYUSEQT").val();
	var expHdrId = [], pono = [], payHdrId = [], amount = [], payDetId = [], capplyKey = [];
	$(".creditListTableExp tbody tr").each(function() {
		
		var amt = $('td:eq(7)', this).find('input').val();
		if(amt > 0){
			expHdrId.push($('td:eq(0)', this).text());
			pono.push($('td:eq(1)', this).text());
			payHdrId.push($('td:eq(2)', this).text());
			payDetId.push($('td:eq(8)', this).text());
			capplyKey.push($('td:eq(9)', this).text());
			amount.push(amt);
		}					
	});

	
	$(".ordercreditListTableExp tbody tr").each(function() {
		
		var amt = $('td:eq(7)', this).find('input').val();
		if(amt > 0){
			expHdrId.push($('td:eq(0)', this).text());
			pono.push($('td:eq(1)', this).text());
			payHdrId.push($('td:eq(2)', this).text());
			payDetId.push($('td:eq(8)', this).text());
			capplyKey.push($('td:eq(9)', this).text());
			amount.push(amt);
		}					
	});

	$.ajax({
		type : "POST",
		url : "/track/ExpensesServlet?Submit=ApplyCredit",
		async : true,
		data : {
			EXPHDRID : expHdrId,
			PONO : pono,
			PAYHDRID : payHdrId,
			AMOUNT : amount,
			PAYDETID : payDetId,
			CAPPLYKEY : capplyKey,
			CURRENCYUSEQT : CURRENCYUSEQTs,
		},
		dataType : "json",
		success : function(data) {
			if(data.ERROR_CODE == 100){
				/*$("#explisttablebody").html("");
				vendorchanged();
				$('#creditListModalExpense').modal('toggle');
				var numberOfDecimal = document.getElementById("numberOfDecimal").value;
				callTotalDetail(numberOfDecimal);*/
				location.reload();
			}else{
				alert(data.MESSAGE);
			}
		}
	});
}


function checkpayablewo(amount)
{
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var pamt = parseFloat($("input[name ='amount_paid']").val());
	var woamt =  parseFloat(amount);
	if(woamt <= pamt){
		$("input[name ='payablewo']").val(parseFloat(woamt).toFixed(numberOfDecimal));
	}else{
		alert("Payable WO amount should be less than or equal to Amount");
		$("input[name ='payablewo']").val(parseFloat("0.0").toFixed(numberOfDecimal));
	}

}