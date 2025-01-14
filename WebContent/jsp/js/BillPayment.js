var subWin = null;
var billList=null;
var totalBillPaid=0;
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

function vendorchanged()
{
	$("#billlisttablebody").html("");
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
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
	        	billList=json.data;
	        	
	        	console.log(billList);
	        	var tbillamt = 0;
	        	var tcbillamt = 0;
	        	var tbilldue = 0;
	        	if(billList.length>0)
	        		{
		        		$.each(billList,function(i,v){
		        			
		        			var balanceamount = "0";
		        			var urlStr = "/track/BillPaymentServlet?CMD=getbalanceofbill";
		        			$.ajax( {
		        				type : "GET",
		        				url : urlStr,
		        				data: {
		        					vendno:vendno,
		        					pono:v.PONO,
		        					bid:v.ID
		        				},
		        		        success: function (data1) {
		        		    
		        		        	var obj = JSON.parse(data1);
		        		        	
		        		        	console.log(obj);
		        		        	var zeroshow = parseFloat("0").toFixed(numberOfDecimal);
		        		        	balanceamount = v.TOTAL_AMOUNT - obj.BALANCE;
		        		        	balanceamount = parseFloat(balanceamount).toFixed(numberOfDecimal);
		        		        	
		        		        	var covunitCostValue= (parseFloat(v.TOTAL_AMOUNT)*parseFloat(v.CURRENCYUSEQT));
		        		        	var stringjson=JSON.stringify(v);
					        		var key="bill_"+v.VENDNO+"_"+v.BILL+"_"+v.PONO+"_"+v.BILL_DATE+"_"+v.TOTAL_AMOUNT;
					        		var tableapp = null;
					        		tableapp = "<tr>"+
					        				"<input type='hidden' name='billHdrId' value="+v.ID+">"+
					        				"<input type='hidden' name='pono' value="+v.PONO+">"+
					        				/*"<input type='hidden' name='billamount' value="+v.TOTAL_AMOUNT+">"+*/
					        				"<input type='hidden' name='billamount' value="+balanceamount+">"+
					        				"<input name='type' type='hidden' value='REGULAR'>"+
					        				"<td class='text-center'>"+v.BILL+"</td>"+
					        				/*"<td class='text-center'>"+v.GRNO+"</td>"+*/
					        				"<td class='text-center'>"+v.REFERENCE_NUMBER+"</td>"+
					        				"<td class='text-center'>"+v.PONO+"</td>"+
					        				"<td class='text-center'>"+v.BILL_DATE+"</td>"+
					        				"<td class='text-center'>"+parseFloat(covunitCostValue).toFixed(numberOfDecimal)+"</td>"+
					        				"<td class='text-center'>"+parseFloat(v.CURRENCYUSEQT).toFixed(numberOfDecimal)+"</td>"+
					        				"<td class='text-center'>"+parseFloat(v.TOTAL_AMOUNT).toFixed(numberOfDecimal)+"</td>"+
					        				"<td class='text-center'>"+balanceamount+"</td>";
					        				
					        		if(obj.CREDIT > 0){
					        			tableapp += "<td class='text-center'><input type='text' style='border:1px solid #eee;width: 100%;' class='paymentamountclass' id="+key+" disabled name='amount' value='"+zeroshow+"' onchange='paidamountchanged("+balanceamount+",this)' onkeypress='return isNumberKey(event,this,4)'></input></td>";
					        			tableapp +=	'<td class="text-center"><button type="button" class="btn btn-default acredits" onClick="aCredit('+v.ID+',\''+v.PONO+'\',\''+v.VENDNO+'\',\''+v.BILL+'\',\''+balanceamount+'\',\''+v.CURRENCYID+'\',\''+v.CURRENCYUSEQT+'\')" data-toggle="modal" data-target="#creditListModal">Apply Credits</button></td>';
					        			tableapp +=	"</tr>";
					        		}else{
					        			tableapp +="<td class='text-center'><input type='text' style='border:1px solid #eee;width: 100%;' class='paymentamountclass' id="+key+" name='amount' value='"+zeroshow+"' onchange='paidamountchanged("+balanceamount+",this)' onkeypress='return isNumberKey(event,this,4)'></input></td>";
					        			if(obj.ALLCREDIT > 0){
					        				tableapp +=	'<td class="text-center"><button type="button" class="btn btn-default acredits" onClick="aCredit('+v.ID+',\''+v.PONO+'\',\''+v.VENDNO+'\',\''+v.BILL+'\',\''+balanceamount+'\',\''+v.CURRENCYID+'\',\''+v.CURRENCYUSEQT+'\')"  data-toggle="modal" data-target="#creditListModal">Apply Credits</button></td>';
					        			}else{
					        				tableapp +=	"<td class='text-center'>NO CREDITS</td>";
					        			}
					        			
					        			tableapp +=	"</tr>";
					        		}
					        		
					        		$("#billlisttablebody").append(tableapp);
					        		
					        		tbillamt = parseFloat(tbillamt)+parseFloat(covunitCostValue);
						        	tcbillamt = parseFloat(tcbillamt)+parseFloat(v.TOTAL_AMOUNT);
						        	tbilldue = parseFloat(tbilldue)+parseFloat(balanceamount);
						        	
						        	/*$("td.tbillamt").html("");
						        	$("td.tcbillamt").html("");
						        	$("td.tbilldue").html("");*/
						        	
						        	$("td.tbillamt").html(parseFloat(tbillamt).toFixed(numberOfDecimal));
						        	$("td.tcbillamt").html(parseFloat(tcbillamt).toFixed(numberOfDecimal));
						        	$("td.tbilldue").html(parseFloat(tbilldue).toFixed(numberOfDecimal));
					        		
		        		        }
		        			});
		        			
			        		
			        		});
		        		$("#table2").removeClass("tbodyheight");
		        		$("#table2").addClass("tbodyfixheight");
		        		
	        		}
	        	else
	        		{
	        			$("#billlisttablebody").html("");
	        			$("td.tbillamt").html(parseFloat(tbillamt).toFixed(numberOfDecimal));
	    	        	$("td.tcbillamt").html(parseFloat(tcbillamt).toFixed(numberOfDecimal));
	    	        	$("td.tbilldue").html(parseFloat(tbilldue).toFixed(numberOfDecimal));
	    	        	
	    	        	$("#table2").removeClass("tbodyfixheight");
	    	        	$("#table2").addClass("tbodyheight");
	        		}
	        	
	        	
	        	
	        	
	        },
	        cache: false
			   });
		sortTable();
		vendorchangedexp();
    	}
    
    
//window.location.href="invoicePaymentNew.jsp?CUSTNO="+custno;
}




$(document).ready(function(){
	var plant = document.form.plant.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	$(".payment-table tbody").on('click','.payment-action',function(){
	    $(this).parent().parent().remove();
	    calculatetotal();
	});
	
	$(".voucher-table tbody").on('click','.voucher-action',function(){
	    $(this).parent().parent().remove();
	    v_calculatetotal();
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
		    return '<p onclick="vendornoandbank(\''+data.VENDO+'\',\''+data.BANKNAME+'\')">' + data.VNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			} 
			menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });						
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
			vendorchanged();
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.vendno.value = "";
			}
			
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
			 alert("Please Enter Valid Bank Charges");
			}
		}
		
		if(value.length > 0){
			
		}else{
			$(".bankAccountSection").hide();
			 $("#bankAccountSearch").prop('disabled', true);
			 $("input[name ='bank_name']").typeahead('val','');
			 $("input[name ='bank_branch']").val('');
		}
	});
	
	
	
$(document).on("focusout","#voucher_bankCharge", function(){
		
		var numberOfDecimal = document.getElementById("numberOfDecimal").value;
		var value = $(this).val();
		var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(value.match(decimal) || value.match(numbers)) 
		{ 	
			var paidamount = document.vform.voucher_amount_paid.value;
			var amount=parseFloat(value).toFixed(numberOfDecimal);
			$(this).val(amount);
			if(value.length > 0){

				if(parseFloat(paidamount) > parseFloat(value)){
					 $(".voucher_bankAccountSection").show();
					 $("#voucher_bankAccountSearch").prop('disabled', false); 
				}else{
					$(this).val('');
					 $(".voucher_bankAccountSection").hide();
					 $("#voucher_bankAccountSearch").prop('disabled', true);
					 $("input[name ='voucher_bank_name']").typeahead('val','');
					 $("input[name ='voucher_bank_branch']").val('');
					 alert("Please Enter Bank Charges Less Than Amount Paid");
				}
			 }else{
				 $(".voucher_bankAccountSection").hide();
				 $("#voucher_bankAccountSearch").prop('disabled', true);
				 $("input[name ='voucher_bank_name']").typeahead('val','');
				 $("input[name ='voucher_bank_branch']").val('');
			 }
		}else{
			if(value.length > 0){
			$(this).val('');
			 $(".voucher_bankAccountSection").hide();
			 $("#voucher_bankAccountSearch").prop('disabled', true);
			 $("input[name ='voucher_bank_name']").typeahead('val','');
			 $("input[name ='voucher_bank_branch']").val('');
			 alert("Please Enter Valid Bank Charges");
			}
		}
	});
	
	
	
	/* Paid Through Auto Suggestion */
	$("#account_name").typeahead({
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
					module:"billpaymentvoucheraccount",
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
	
	/* project Auto Suggestion */
	$('#vproject').typeahead({
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
			$("input[name=VPROJECTID]").val(selection.ID);
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
	
	
	/* Paid Through Auto Suggestion */
	$("#voucher_paid_through_account_name").typeahead({
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
						    '<span onclick="v_isbankcharge(\''+item.accountname+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
						  );
					}
				else
					{
					var $state = $(
							 '<span onclick="v_isbankcharge(\''+item.accountname+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
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
	
	/* Payment Mode Auto Suggestion */
	$("#voucher_payment_mode").typeahead({
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
			var isbank = $("input[name ='v_isbank']").val();
			var vpthrough = $("input[name ='voucher_paid_through_account_name']").val();
			if(selection.PAYMENTMODE != "Cheque"){
				$("input[name ='v_paidpdcstatus']").val("0");
				$("input[name ='v_bank_charge']").val("");
				$("#voucher_bankCharge").attr("readonly", true);
				$(".v_hidepdc").hide();
			}else{
				if(isbank == "1"){
					v_addnewRow();
					$("input[name ='v_paidpdcstatus']").val("1");
					$("#voucher_bankCharge").attr("readonly", false);
					$(".v_hidepdc").show();
				}else{
					$("input[name ='v_paidpdcstatus']").val("0");
					$("input[name ='v_bank_charge']").val("");
					$("#voucher_bankCharge").attr("readonly", true);
					$(".v_hidepdc").hide();
					if(vpthrough != ""){
						$("#voucher_paid_through_account_name").typeahead('val', '');
						alert("Please select Bank Balance account type");
					}
				}
			}
		});
	/* Bank Auto Suggestion
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
		});*/
	
	addrowclasses();
	
	/* Bank Auto Suggestion 
	$(".v_bankAccountSearch").typeahead({
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
				return '<div onclick="document.vform.voucher_bank_branch.value=\''+data.BRANCH_CODE+'\';"><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></div>';
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
		});*/
	
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
	
	$("#voucher_billPaymentAttch").change(function(){
		var files = $(this)[0].files.length;
		var sizeFlag = false;
			if(files > 5){
				$(this)[0].value="";
				alert("You can upload only a maximum of 5 files");
				$("#voucher_billPaymentAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
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
					$("#voucher_billPaymentAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
				}else{
					$("#voucher_billPaymentAttchNote").html(files +" files attached");
				}
				
			}
		});
	
	/* To get the suggestion data for Currency */
	$("#CURRENCY").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CURRENCY',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_CURRENCY_DATA",
				CURRENCY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CURRENCYMST);
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
			return '<div><p onclick="getCurrencyid(\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
	
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			{
			$("input[name ='CURRENCYID']").val("");
			$("input[name ='CURRENCYUSEQT']").val("1");
			}
	});

	/* To get the suggestion data for Currency */
	$("#voucher_CURRENCY").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CURRENCY',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_CURRENCY_DATA",
				CURRENCY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CURRENCYMST);
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
			return '<div><p onclick="getvoucher_Currencyid(\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
	
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == ""){
			$("input[name ='voucher_CURRENCYID']").val("");
			$("input[name ='voucher_CURRENCYUSEQT']").val("1");
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
	
	
	var paymentDate = document.form.payment_date.value;
	var paymentThrough = document.form.paid_through_account_name.value;
	var paymentMode = document.form.payment_mode.value;
	
	var bankCharges = document.form.bank_charge.value;
	var Currency = document.form.CURRENCY.value;
	var pdastatus=$('input[name = "paidpdcstatus"]').val();
	var pdcamount=$('input[name = "pdcamount"]').val();
	var examt=$("input[name ='excessamtcheck']").val();
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	
	//RESVI START
    var ValidNumber   = document.form.ValidNumber.value;
    if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" Payment's you can create"); return false; }
	  // END


	var CURREQT = document.form.CURRENCYUSEQT.value;
	
	if (CURREQT.indexOf('.') == -1) CURREQT += ".";
 	var decNum = CURREQT.substring(CURREQT.indexOf('.')+1, CURREQT.length);
 	if (decNum.length > numberOfDecimal)
 	{
 		alert("Invalid more than "+numberOfDecimal+" digits after decimal in Exchange Rate");
 		document.form.CURRENCYUSEQT.focus();
 		return false;
 		
 	}
 	
 	
 	
	if(paymentDate == ""){
		alert("Please enter date of payment.");
		document.form.payment_date.focus();
		return false;
	}
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
	if(Currency == ""){
		alert("Please select a Currency.");
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
	
	if(parseFloat(examt) != parseFloat("0")){
		alert("Amount in excess Should be Zero");
		return false;
	}
	
	
	$('.datepicker').prop('disabled',false);
	
	return true;

}


function onPay(){
	   
	var urlStr ="/track/BillPaymentServlet?Submit=Save";
	
	event.preventDefault();
	if(validatePayment()){
		  $( ".paymentamountclass" ).prop( "disabled", false );
		  var formData = new FormData(document.getElementById("billPaymentForm"));
		  formData.append("billlist",billList);
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
	                window.location.href="../banking/billpaysummary";
	            },
	            data: formData,
	            cache: false,
	            contentType: false,
	            processData: false
				   });
		}
	}

function validateVoucherPayment()
{


	v_calculatecurrency();
var paymentDate = document.vform.voucher_payment_date.value;
var paymentThrough = document.vform.voucher_paid_through_account_name.value;
var paymentMode = document.vform.voucher_payment_mode.value;

var Currency = document.vform.voucher_CURRENCY.value;
var bankCharges = document.vform.voucher_bank_charge.value;
var numberOfDecimal = document.getElementById("voucher_numberOfDecimal").value;
//RESVI START
var ValidNumber   = document.vform.V_ValidNumber.value;
 if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" Payment's you can create"); return false; }
//END
    var CURREQT = document.vform.voucher_CURRENCYUSEQT.value;
    
    if (CURREQT.indexOf('.') == -1) CURREQT += ".";
	var decNum = CURREQT.substring(CURREQT.indexOf('.')+1, CURREQT.length);
	if (decNum.length > numberOfDecimal)
	{
		alert("Invalid more than "+numberOfDecimal+" digits after decimal in Exchange Rate");
		document.vform.voucher_CURRENCYUSEQT.focus();
		return false;
		
	}
	var accountname = $('input[name = "account_name"]').val();
	var pdastatus=$('input[name = "v_paidpdcstatus"]').val();
	var pdcamount=$('input[name = "v_pdcamount"]').val();
	
	
	
	
	if(accountname == ""){
		alert("Please select account name.");
		document.vform.account_name.focus();
		return false;
	}
	if(paymentDate == ""){
		alert("Please enter payment date.");
		document.vform.voucher_payment_date.focus();
		return false;
	}
	var amount_paid=$('input[name = "voucher_amount_paid"]').val();
	if(!(parseFloat(amount_paid)>0))
		{
			alert("Please enter paid amount greater than zero");
			return false;
		}
	var amount_paid_Curr=$('input[name = "voucher_amount_paid_Curr"]').val();
	if(!(parseFloat(amount_paid_Curr)>0))
		{
			alert("Please enter paid amount greater than zero");
			return false;
		}
	
	
	if(Currency == ""){
		alert("Please select a Currency.");
		return false;
	}
	if(paymentThrough == ""){
		alert("Please choose paid through.");
		document.vform.voucher_paid_through_account_name.focus();
		return false;
	}
	
	if(paymentMode == ""){
		alert("Please choose mode of payment.");
		document.vform.voucher_payment_mode.focus();
		return false;
	}
	
	if(pdastatus == "1"){
		
		
		var isItemValid = true;
		var ischeck = false;
		$("input[name ='v_pdc']").each(function() {
			if($(this).is(":checked")){
				ischeck = true;
		    }
		});
		
		if(ischeck){
			$("input[name ='v_pdc']").each(function() {
				if(!$(this).is(":checked")){
					isItemValid = false;
			    }
			});
		}else{
			$("input[name ='v_pdc']").each(function() {
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
		$("input[name ='v_bankname']").each(function() {
		    if($(this).val() == ""){
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The bank field cannot be empty.");
			return false;
		}	
		
		$("input[name ='v_cheque-no']").each(function() {
		    if($(this).val() == ""){	
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque number field cannot be empty.");
			return false;
		}
		
		$("input[name ='v_cheque-date']").each(function() {
		    if($(this).val() == ""){
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque date field cannot be empty.");
			return false;
		}
		
		$("input[name ='v_cheque-amount']").each(function() {
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
			alert("Cheque amount should be equal to Amount Paid");
			return false;
		}
	}

	$('.datepicker').prop('disabled',false);
	
	return true;

}

function onV_Pay(){
	
	
var urlStr ="/track/BillPaymentServlet?Submit=Save_voucher";
	
	event.preventDefault();
	if(validateVoucherPayment()){
		  var formData = new FormData(document.getElementById("BillvoucherPaymentForm"));
		  formData.append("billlist",billList);
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
	                window.location.href="../banking/billpaysummary";
	            },
	            data: formData,
	            cache: false,
	            contentType: false,
	            processData: false
				   });
		}
		
			
	 	}


var amountreceived=0;
var paidamounttotal=0;
function paidamountchanged(invoiceamt,paidnode)
{
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var invoiceamount=parseFloat(invoiceamt);
	var paidamount=parseFloat(paidnode.value);
	if(isNaN(paidamount)){
		paidamount = "0";
	}
	
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
	
	if ($('#paycaltype').is(":checked")){
  		autocalculatebasecurrency(parseFloat(paidamounttotal).toFixed(numberOfDecimal));
	}else{
	
		if(paidamounttotal>amountreceived)
			{
				var difamt = parseFloat(paidnode.value)-(parseFloat(paidamounttotal)-parseFloat(amountreceived))
				alert("Please enter amount equal/less than received amount.");
				paidnode.value=parseFloat(difamt).toFixed(numberOfDecimal);
			}
	}
		
		
	callTotalDetail(numberOfDecimal);
}

function amountchanged(amountnode)
{
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var zeroval=parseFloat("0").toFixed(numberOfDecimal);
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	if(amountnode.value.match(decimal) || amountnode.value.match(numbers)) 
	{ 
		var numberOfDecimal = document.getElementById("numberOfDecimal").value;
		var amount=parseFloat(amountnode.value).toFixed(numberOfDecimal);
		document.getElementById('amountreceived').innerHTML=amount;
		$("input[name ='amount_paid']").val(amount);
		amountreceived=amount;
		$("input[name ='cheque-amount']").each(function() {	
		    $(this).val(zeroval);
		});
		calculatetotal();
		
		calculatecurrency();
		
	}else{
		$("input[name ='amount_paid']").val('1');
		document.getElementById('amountreceived').innerHTML='';
		alert("Please Enter Valid Amount");
	}

	callTotalDetail(numberOfDecimal);
}




function voucher_amountchanged(amountnode)
{
	var numberOfDecimal = document.getElementById("voucher_numberOfDecimal").value;
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	var zeroval=parseFloat("0").toFixed(numberOfDecimal);
	if(amountnode.value.match(decimal) || amountnode.value.match(numbers)) 
	{ 
		var amount=parseFloat(amountnode.value).toFixed(numberOfDecimal);
		$("input[name ='voucher_amount_paid']").val(amount);
		$("input[name ='v_cheque-amount']").each(function() {	
		    $(this).val(zeroval);
		});
		v_calculatetotal();
		
		v_calculatecurrency();
		
	}else{
		$("input[name ='voucher_amount_paid']").val(zeroval);
		alert("Please Enter Valid Amount Paid");
	}
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
	var numberOfDecimal = $("#numberOfDecimal").val();
	var totalamount=0;
	var amount=0;
	var billamt =0;
	var expamt=0;
	$('input[name = "amount"]').each(function() {
		amount=$(this).val();
		totalamount+=parseFloat(amount);
		billamt+=parseFloat(amount);
	});
	$('input[name = "Expamount"]').each(function() {
		amount=$(this).val();
		totalamount+=parseFloat(amount);
		expamt+=parseFloat(amount);
	});
	//calculatebasecurrency_new(totalamount);
	$("td.tpayamt").html(parseFloat(billamt).toFixed(numberOfDecimal));
	$("td.etpayamt").html(parseFloat(expamt).toFixed(numberOfDecimal));
	calculatebasecurrency_new($("input[name ='amount_paid_Curr']").val());
	return totalamount;
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

//jsp validation
function checksupplier(supplier){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CUST_CODE : supplier,
				ACTION : "SUPPLIER_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Supplier Does't Exist");
						document.getElementById("vendname").focus();
						$("#vendname").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkproject(project){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PROJECT_NAME : project,
				ACTION : "PROJECT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Project Does't Exist");
						document.getElementById("project").focus();
						$("#project").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

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
						alert("Payment Type Does't Exists");
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

function checkcurrency(currency){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CURRENCYID : currency,
				ACTION : "CURRENCY_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Currency Does't Exists");
						document.getElementById("CURRENCY").focus();
						$("#CURRENCY").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
//jsp validation ends

function v_addRow(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var bankname = $("input[name ='v_vbank']").val();
	var newdate = $("input[name ='payment_date']").val();
	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="v_pdcstatus" value="0">';
	body += '<input type="checkbox" class="form-check-input" id="v_pdc" name="v_pdc" Onclick="v_checkpdc(this)">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input name="v_bank_branch" type="hidden" value="">';
	if(bankname.length > 0){
		body += '<input class="form-control text-left v_bankAccountSearch" type="text" name="v_bankname" value="'+bankname+'" readonly>';
	}else{
		body += '<input class="form-control text-left v_bankAccountSearch" type="text" name="v_bankname" placeholder="Select a bank" readonly>';
	}
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" type="text" name="v_cheque-no" placeholder="Enter Cheque No">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left datepicker" type="text" name="v_cheque-date" placeholder="Enter Cheque Date" value="'+newdate+'" disabled>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle voucher-action" aria-hidden="true"></span>';
	body += '<input class="form-control text-right" type="text" name="v_cheque-amount" onchange="v_calculateAmount(this)" value="'+zeroval+'">';
	body += '</td>';
	body += '</tr>';
	$(".voucher-table tbody").append(body);
	v_removerowclasses();
	v_addrowclasses();
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

function v_addnewRow(){
	$(".voucher-table tbody").html("");
	var numberOfDecimal = $("#numberOfDecimal").val();
	var bankname = $("input[name ='v_vbank']").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var newdate = $("input[name ='payment_date']").val();
	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="v_pdcstatus" value="0">';
	body += '<input type="checkbox" class="form-check-input v_pdc" name="v_pdc" Onclick="v_checkpdc(this)">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input name="v_bank_branch" type="hidden" value="">';
	if(bankname.length > 0){
		body += '<input class="form-control text-left v_bankAccountSearch" type="text" name="v_bankname" value="'+bankname+'" readonly>';
	}else{
		body += '<input class="form-control text-left v_bankAccountSearch" type="text" name="v_bankname" placeholder="Select a bank" readonly>';
	}
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" type="text" name="v_cheque-no" placeholder="Enter Cheque No">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left datepicker" type="text" name="v_cheque-date" placeholder="Enter Cheque Date" value="'+newdate+'" disabled>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<input class="form-control text-right" type="text" name="v_cheque-amount" onchange="v_calculateAmount(this)" value="'+zeroval+'">';
	body += '</td>';
	body += '</tr>';
	$(".voucher-table tbody").append(body);
	$("#v_subTotal").html(parseFloat("0.0").toFixed(numberOfDecimal));
	$("#v_balamount").html(parseFloat("0.0").toFixed(numberOfDecimal));
	 $("input[name ='v_pdcamount']").val("0");
	 v_removerowclasses();
	 v_addrowclasses();
}

/*$(document).on("focus", ".bankAccountSearch" , function() {
	$(this).typeahead('val', '"');
	$(this).typeahead('val', '');	
});*/

$(document).on("focus", ".v_bankAccountSearch" , function() {
	$(this).typeahead('val', '"');
	$(this).typeahead('val', '');	
});

function removerowclasses(){
	/*$(".bankAccountSearch").typeahead('destroy');*/
}

function v_removerowclasses(){
	/*$(".v_bankAccountSearch").typeahead('destroy');*/
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

function v_addrowclasses(){
	var plant = document.form.plant.value;
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
	
		/*$(".v_bankAccountSearch").typeahead({
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
					return '<span onclick="v_addbankcode(this,\''+data.BRANCH_CODE+'\')" ><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></span>';
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

function v_checkpdc(obj){
	var newdate = $("input[name ='payment_date']").val();
	 if ($(obj).is(":checked")){
		 $(obj).closest('td').find("input[name ='v_pdcstatus']").val("1");
		 $(obj).closest('tr').find("input[name ='v_cheque-date']").prop('disabled',false);
	 }else{
		 $(obj).closest('td').find("input[name ='v_pdcstatus']").val("0");
		 $(obj).closest('tr').find('input[name = "v_cheque-date"]').val(newdate);
		 $(obj).closest('tr').find("input[name ='v_cheque-date']").prop('disabled',true);
	 }
}

function addbankcode(obj,branchcode){
	$(obj).closest('td').find('input[name = "bank_branch"]').val(branchcode);
}

function v_addbankcode(obj,branchcode){
	$(obj).closest('td').find('input[name = "v_bank_branch"]').val(branchcode);
}

function calculateAmount(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	var subtotal = "0";
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

function v_calculateAmount(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var subtotal = "0";
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	var amountpaid =  $("input[name ='voucher_amount_paid']").val();
	if(!(parseFloat(amountpaid)>0)){
		$(".voucher-table tbody tr td:last-child").each(function() {
			$(this).find('input').val(parseFloat("0.0").toFixed(numberOfDecimal));
	});
	}else{
		$(".voucher-table tbody tr td:last-child").each(function() {
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
		 $("#v_subTotal").html(parseFloat(subtotal).toFixed(numberOfDecimal));
		 $("#v_balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(subtotal)).toFixed(numberOfDecimal));
		 $("input[name ='v_pdcamount']").val(subtotal);
	}else{
		alert("Cheque amount exceeds Amount Paid")
		$(obj).val(parseFloat("0.0").toFixed(numberOfDecimal));
		var esubtotal = "0";
		$(".voucher-table tbody tr td:last-child").each(function() {
			var amount = $(this).find('input').val();
			esubtotal =  parseFloat(esubtotal) + parseFloat(amount);
		});
		$("#v_subTotal").html(parseFloat(esubtotal).toFixed(numberOfDecimal));
		$("#v_balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(esubtotal)).toFixed(numberOfDecimal));
		$("input[name ='v_pdcamount']").val(esubtotal);
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

function v_calculatetotal(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amountpaid =  $("input[name ='voucher_amount_paid']").val();
	var subtotal = "0";
	$(".voucher-table tbody tr td:last-child").each(function() {
		var amount = $(this).find('input').val();
		subtotal =  parseFloat(subtotal) + parseFloat(amount);
		$(this).find('input').val(parseFloat(amount).toFixed(numberOfDecimal));
	});
	$("#v_subTotal").html(parseFloat(subtotal).toFixed(numberOfDecimal));
	$("#v_balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(subtotal)).toFixed(numberOfDecimal));
	$("input[name ='v_pdcamount']").val(subtotal);
}

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

function v_isbankcharge(acountname){
	var plant = document.form.plant.value;
	var paymode =  $("input[name ='voucher_payment_mode']").val();
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
				$("input[name ='v_isbank']").val("1");
				$("input[name ='v_vbank']").val(acountname);
				var pdcstatus = $("input[name ='v_paidpdcstatus']").val();
				if(pdcstatus == "1"){
					$("input[name ='v_bankname']").each(function() {
					    $(this).val(acountname);
					});
				}else{
					if(paymode == "Cheque"){
						v_addnewRow();
						$("input[name ='v_paidpdcstatus']").val("1");
						$("#voucher_bankCharge").attr("readonly", false);
						$(".v_hidepdc").show();
					}
				}
			}else{
				$("input[name ='v_isbank']").val("0");
				$("input[name ='v_paidpdcstatus']").val("0");
				$("input[name ='v_bank_charge']").val("");
				$("#voucher_bankCharge").attr("readonly", true);
				$(".v_hidepdc").hide();
				if(paymode == "Cheque"){
					$("#voucher_paid_through_account_name").typeahead('val', '');
					alert("Please select Bank Balance account type");
				}
			}
		}
	});
}

function vendornoandbank(vno,vbank){
	
	$("input[name ='vendno']").val(vno);
	$("input[name ='bankname']").val(vbank);
	
}
function paymentTypeCallback(data)
{
	var inpay = $("input[name ='inpayment']").val();
	if(inpay=="0")
	$("input[name ='payment_mode']").val(data.PAYMENTMODE);
	else if(inpay=="1")
	$("input[name ='voucher_payment_mode']").val(data.PAYMENTMODE);
}
function getCurrencyid(CURRENCYID,CURRENCYUSEQT){
	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	
	
	var basecurrency = document.form.BASECURRENCYID.value; //Resvi
	
	
	
	document.getElementById('PaymentMade').innerHTML = "Amount Paid ("+CURRENCYID+")";
	document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi
	
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var amountnode = document.getElementById("amount_paid").value;
	
	
	
	var baseamount=parseFloat(amountnode) * parseFloat(CURRENCYUSEQT);
	$("input[name ='conv_amount_paid_Curr']").val(baseamount);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	$("input[name ='amount_paid_Curr']").val(baseamount);
    
}

function getvoucher_Currencyid(CURRENCYID,CURRENCYUSEQT){
	
	$('input[name ="voucher_CURRENCYID"]').val(CURRENCYID);
	$('input[name ="voucher_CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	
	var basecurrency = document.form.BASECURRENCYID.value; //Resvi
	
	document.getElementById('voucher_PaymentMade').innerHTML = "Amount Paid ("+CURRENCYID+")";
	document.getElementById('voucher_exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi
	
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var amountnode = document.getElementById("voucher_amount_paid").value;
	
	
	
	var baseamount=parseFloat(amountnode) * parseFloat(CURRENCYUSEQT);
	$("input[name ='v_conv_amount_paid_Curr']").val(baseamount);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	$("input[name ='voucher_amount_paid_Curr']").val(baseamount);
    
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
function v_calculatecurrency(){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = document.getElementById("voucher_CURRENCYUSEQT").value;
	var amountnode = document.getElementById("voucher_amount_paid").value;

	var baseamount=parseFloat(amountnode) * parseFloat(baseCurrency);
	$("input[name ='v_conv_amount_paid_Curr']").val(baseamount);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	$("input[name ='voucher_amount_paid_Curr']").val(baseamount);
	

}
function currencychanged(data)
{
	//calculatecurrency();
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = data.value;
	//var amountnode = document.getElementById("amount_paid_Curr").value;
	var amountnode = $("input[name ='amount_paid_Curr']").val();
	$("input[name ='conv_amount_paid_Curr']").val(amountnode);
	
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='amount_paid']").val(baseamount);
	calculateTotalbasecurrency(baseamount);
}
function voucher_currencychanged(data)
{
	//v_calculatecurrency();
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = data.value;
	var amountnode = $("input[name ='voucher_amount_paid_Curr']").val();
	$("input[name ='v_conv_amount_paid_Curr']").val(amountnode);
	
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='voucher_amount_paid']").val(baseamount);
	v_calculateTotalbasecurrency(baseamount);
}

function calculatebasecurrency(data){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = document.getElementById("CURRENCYUSEQT").value;
	var amountnode = data.value;
	$("input[name ='conv_amount_paid_Curr']").val(amountnode);
	
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='amount_paid']").val(baseamount);
	calculateTotalbasecurrency(baseamount);

}

function v_calculatebasecurrency(data){
	
	var numberOfDecimal = document.getElementById("voucher_numberOfDecimal").value;	
	var baseCurrency = document.getElementById("voucher_CURRENCYUSEQT").value;
	var amountnode = data.value;
	$("input[name ='v_conv_amount_paid_Curr']").val(amountnode);
	
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='voucher_amount_paid']").val(baseamount);
	v_calculateTotalbasecurrency(baseamount);

}

function calculateTotalbasecurrency(amountnode){

var numberOfDecimal = document.getElementById("numberOfDecimal").value;
var zeroval=parseFloat("0").toFixed(numberOfDecimal);
var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
var numbers = /^[0-9]+$/;
if(amountnode.match(decimal) || amountnode.match(numbers)) 
{ 
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var amount=parseFloat(amountnode).toFixed(numberOfDecimal);
	document.getElementById('amountreceived').innerHTML=amount;
	$("input[name ='amount_paid']").val(amount);
	amountreceived=amount;
	$("input[name ='cheque-amount']").each(function() {	
	    $(this).val(zeroval);
	});
	calculatetotal();
	
}else{
	$("input[name ='amount_paid']").val('1');
	document.getElementById('amountreceived').innerHTML='';
	alert("Please Enter Valid Amount");
}

callTotalDetail(numberOfDecimal);
}

function v_calculateTotalbasecurrency(baseamount){

	var numberOfDecimal = document.getElementById("voucher_numberOfDecimal").value;
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	var zeroval=parseFloat("0").toFixed(numberOfDecimal);
	if(baseamount.match(decimal) || baseamount.match(numbers)) 
	{ 
		var amount=parseFloat(baseamount).toFixed(numberOfDecimal);
		$("input[name ='voucher_amount_paid']").val(amount);
		$("input[name ='v_cheque-amount']").each(function() {	
		    $(this).val(zeroval);
		});
		v_calculatetotal();
		
		
	}else{
		$("input[name ='voucher_amount_paid']").val(zeroval);
		alert("Please Enter Valid Amount Paid");
	}
}

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}

function vendorchangedexp()
{
	$("#explisttablebody").html("");
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var zeroshow = parseFloat("0").toFixed(numberOfDecimal);
	var vendno=document.form.vendno.value;
    if(vendno!=""||vendno!=null)
    	{
		var urlStr = "/track/ExpensesServlet?ACTION=getexpByVend";
		$.ajax( {
			type : "GET",
			url : urlStr,
			data: {
				vendno:vendno
			},
	        success: function (data) {
	        	var json = $.parseJSON(data);
	        	expList=json.data;
	        	expcredit=json.credit;
	        	console.log("-----------------------------");
	        	console.log("-----------------------------");
	        	console.log(expList);
	        	console.log(expcredit);
	        	console.log("-----------------------------");
	        	console.log("-----------------------------");
	        	
	        	var etbillamt = 0;
	        	var etcbillamt = 0;
	        	var etbilldue = 0;
	        	
	        	if(expList.length>0)
	        		{
	        		$.each(expList,function(i,v){
	        			
	        			var balanceamount = "0";
	        			var urlStr = "/track/ExpensesServlet?Submit=getbalanceofexp";
	        			$.ajax( {
	        				type : "GET",
	        				url : urlStr,
	        				data: {
	        					vendno:vendno,
	        					pono:v.PONO,
	        					eid:v.ID
	        				},
	        		        success: function (data1) {
	        		    
	        		        	var obj = JSON.parse(data1);
	        		        	
	        		        	console.log(obj);
	        		        	var zeroshow = parseFloat("0").toFixed(numberOfDecimal);
	        		        	balanceamount = v.TOTAL_AMOUNT - obj.BALANCE;
	        		        	balanceamount = parseFloat(balanceamount).toFixed(numberOfDecimal);
	        			
	        		        	/*var stringjson=JSON.stringify(v);
	        		        	balanceamount = v.TOTAL_AMOUNT - expbal;
	        		        	balanceamount = parseFloat(balanceamount).toFixed(numberOfDecimal);*/
	        		        	if(balanceamount != zeroshow){
		        		        	var covunitCostValue= (parseFloat(v.TOTAL_AMOUNT)*parseFloat(v.CURRENCYTOBASE));
					        		var tableapp = null;
					        		tableapp = "<tr>"+
					        				"<input type='hidden' name='ExpHdrId' value="+v.ID+">"+
					        				"<input type='hidden' name='ExpPono' value="+v.PONO+">"+
					        				"<input name='ExpType' type='hidden' value='REGULAR'>"+
					        				"<input type='hidden' name='ExpenseAmount' value="+balanceamount+">"+
					        				"<td class='text-center'>"+v.EXBILL+"</td>"+
					        				"<td class='text-center'>"+v.PONO+"</td>"+
					        				"<td class='text-center'>"+v.SHIPMENT_CODE+"</td>"+
					        				"<td class='text-center'>"+v.EXPENSES_DATE+"</td>"+
					        				"<td class='text-center'>"+parseFloat(covunitCostValue).toFixed(numberOfDecimal)+"</td>"+
					        				"<td class='text-center'>"+parseFloat(v.CURRENCYTOBASE).toFixed(numberOfDecimal)+"</td>"+
					        				"<td class='text-center'>"+parseFloat(v.TOTAL_AMOUNT).toFixed(numberOfDecimal)+"</td>"+
					        				"<td class='text-center'>"+balanceamount+"</td>";
					        		
					        			tableapp +="<td class='text-center'><input type='text' style='border:1px solid #eee;width: 100%;' class='paymentamountclass' id="+v.ID+" name='Expamount' value='"+zeroshow+"' onchange='paidamountchanged("+balanceamount+",this)' onkeypress='return isNumberKey(event,this,4)'></input></td>";
					        			if(expcredit > 0){
					        				tableapp +=	'<td class="text-center"><button type="button" class="btn btn-default acredits" onClick="aCreditExpense('+v.ID+',\''+v.PONO+'\',\''+v.VENDNO+'\',\''+balanceamount+'\',\''+v.CURRENCYID+'\',\''+v.CURRENCYTOBASE+'\')" data-toggle="modal" data-target="#creditListModalExpense">Apply Credits</button></td>';
					        			}else{
					        				tableapp +=	"<td class='text-center'>NO CREDITS</td>";
					        			}
					        			
					        			tableapp +=	"</tr>";
					        		
					        		$("#explisttablebody").append(tableapp);
					        		
					        		etbillamt = parseFloat(etbillamt)+parseFloat(covunitCostValue);
					        		etcbillamt = parseFloat(etcbillamt)+parseFloat(v.TOTAL_AMOUNT);
					        		etbilldue = parseFloat(etbilldue)+parseFloat(balanceamount);
					        		
					        		$("td.etbillamt").html(parseFloat(etbillamt).toFixed(numberOfDecimal));
					        		$("td.etcbillamt").html(parseFloat(etcbillamt).toFixed(numberOfDecimal));
					        		$("td.etbilldue").html(parseFloat(etbilldue).toFixed(numberOfDecimal));

	        		        	}
	        		        }
		        		});
	        		
	        			});      
	        		
	        		$("#table3").removeClass("tbodyheight");
	        		$("#table3").addClass("tbodyfixheight");
		        		  
	        		}
	        	else
	        		{
	        			$("#explisttablebody").html("");
	        			
	        			$("td.etbillamt").html(parseFloat(etbillamt).toFixed(numberOfDecimal));
		        		$("td.etcbillamt").html(parseFloat(etcbillamt).toFixed(numberOfDecimal));
		        		$("td.etbilldue").html(parseFloat(etbilldue).toFixed(numberOfDecimal));
		        		
		        		$("#table3").removeClass("tbodyfixheight");
		        		$("#table3").addClass("tbodyheight");
	        		}

	        	
	        },
	        cache: false
			   });
    	}
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
				$("#explisttablebody").html("");
				vendorchanged();
				$('#creditListModalExpense').modal('toggle');
				var numberOfDecimal = document.getElementById("numberOfDecimal").value;
				callTotalDetail(numberOfDecimal);
			}else{
				alert(data.MESSAGE);
			}
		}
	});
}



function calculatebasecurrency_new(data){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = document.getElementById("CURRENCYUSEQT").value;
	
	$("input[name ='amount_paid_Curr']").val(parseFloat(data).toFixed(numberOfDecimal));
	var amountnode = data;
	$("input[name ='conv_amount_paid_Curr']").val(amountnode);
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	$("input[name ='amount_paid']").val(baseamount);
	calculateTotalbasecurrency_new(baseamount);
	
/*	$("input[name ='amount_paid']").val(parseFloat(data).toFixed(numberOfDecimal));
	var amountnode = data;
	var baseamount=parseFloat(amountnode) * parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	$("input[name ='conv_amount_paid_Curr']").val(baseamount);
	$("input[name ='amount_paid_Curr']").val(baseamount);
	
	calculateTotalbasecurrency_new(parseFloat(data).toFixed(numberOfDecimal));*/
	

}

function calculateTotalbasecurrency_new(amountnode){

var numberOfDecimal = document.getElementById("numberOfDecimal").value;
var zeroval=parseFloat("0").toFixed(numberOfDecimal);
var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
var numbers = /^[0-9]+$/;
if(amountnode.match(decimal) || amountnode.match(numbers)) 
{ 
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var amount=parseFloat(amountnode).toFixed(numberOfDecimal);
	document.getElementById('amountreceived').innerHTML=amount;
	$("input[name ='amount_paid']").val(amount);
	amountreceived=amount;
	$("input[name ='cheque-amount']").each(function() {	
	    $(this).val(zeroval);
	});
	calculatetotal();
	
}else{
	$("input[name ='amount_paid']").val('1');
	document.getElementById('amountreceived').innerHTML='';
	alert("Please Enter Valid Amount");
}

	var totalamount=0;
	totalamount=amountnode;
	var totalInvoiceAmount=parseFloat(totalamount).toFixed(numberOfDecimal);
	document.getElementById("totalInvoiceAmount").innerHTML=totalInvoiceAmount;
	document.getElementById("amountufp").innerHTML=totalInvoiceAmount;
	var amountExcess=document.getElementById("amount_paid").value-totalInvoiceAmount;
	amountExcess=parseFloat(amountExcess).toFixed(numberOfDecimal);
	document.getElementById("amountexcess").innerHTML=amountExcess;
	$("input[name ='excessamtcheck']").val(amountExcess);
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

function autocalculatebasecurrency(amountnode){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = document.getElementById("CURRENCYUSEQT").value;
	$("input[name ='amount_paid_Curr']").val(amountnode);
	$("input[name ='conv_amount_paid_Curr']").val(amountnode);
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='amount_paid']").val(baseamount);
	calculateTotalbasecurrency(baseamount);

}

function sortTable() {
				  var table, rows, switching, i, x, y, shouldSwitch;
				  table = document.getElementById("table2");
				  if (table == null){
						return;
					  }
				  switching = true;
				  /* Make a loop that will continue until
				  no switching has been done: */
				  while (switching) {
				    // Start by saying: no switching is done:
				    switching = false;
				    rows = table.rows;
				    /* Loop through all table rows (except the
				    first, which contains table headers): */
				    for (i = 1; i < (rows.length - 1); i++) {
				      // Start by saying there should be no switching:
				      shouldSwitch = false;
				      /* Get the two elements you want to compare,
				      one from current row and one from the next: */
				      x = rows[i].getElementsByTagName("TD")[0];
				      y = rows[i + 1].getElementsByTagName("TD")[0];
				      // Check if the two rows should switch place:
				      if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
				        // If so, mark as a switch and break the loop:
				        shouldSwitch = true;
				        break;
				      }
				    }
				    if (shouldSwitch) {
				      /* If a switch has been marked, make the switch
				      and mark that a switch has been done: */
				      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
				      switching = true;
				    }
				  }
				}