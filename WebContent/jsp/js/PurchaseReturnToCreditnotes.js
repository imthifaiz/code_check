var subWin = null;
var itemList="";
var taxList = [];
var zerotype=0;
function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

var postatus =   [{
    "year": "DRAFT",
    "value": "DRAFT",
    "tokens": [
      "DRAFT"
    ]
  },
  {
	    "year": "OPEN",
	    "value": "OPEN",
	    "tokens": [
	      "OPEN"
	    ]
	  },
	  {
		    "year": "APPLIED",
		    "value": "APPLIED",
		    "tokens": [
		      "APPLIED"
		    ]
		  },
		  {
			    "year": "PARTIALLY APPLIED",
			    "value": "PARTIALLY APPLIED",
			    "tokens": [
			      "PARTIALLY APPLIED"
			    ]
			  }];


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
	
$(document).ready(function(){
	var plant = document.form.plant.value;
	getNextCreditnote();//resvi
	var poreturn = document.form.poreturn.value;
	var pono = document.form.pono.value;
	var grno = document.form.grnno.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	$('input[name ="cost"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	$('input[name ="item_discount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	$('input[name ="amount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	$("#subTotal").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#discount").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#shipping").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#adjustment").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#total").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	getPOreturn(poreturn,pono,grno);
	
	var creditnotenum=$('#creditnote').val();
	if(creditnotenum=="")
		{
			$("#SALES_LOC").val("Abu Dhabi");
			document.form.STATE_PREFIX.value="AUH";
		}
	
	
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
		    return '<p onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYUSEQT+'\')">' + data.VNAME + '</p>';
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
				document.getElementById('nTAXTREATMENT').value = "";
				document.form.nTAXTREATMENT.value = "";
				$("input[name ='TAXTREATMENT_VALUE']").val("");
			}
			/* To reset Order number Autosuggestion*/
			$("#pono").typeahead('val', '"');
			$("#pono").typeahead('val', '');
			$('#nTAXTREATMENT').attr('disabled',false);
			$("#billno").typeahead('val', '"');
			$("#billno").typeahead('val', '');
			if($('select[name ="nTAXTREATMENT"]').val() =="GCC VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="GCC NON VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="NON GCC")
			{
				document.getElementById('CHK1').style.display = 'block';
			}
			else
				document.getElementById('CHK1').style.display = 'none';
		});

	/* Order Number Auto Suggestion */
	$('#pono').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				CNAME : document.form.vendname.value,
				PONO : query
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
		    return '<p>' + data.PONO + '</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			//getOrderDetailsForBilling(selection.PONO);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.pono.value = "";
			}			
		});
		
	/* Employee Auto Suggestion */
	$('#EMP_NAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'FNAME',  
		  async: true,   
		  //source: substringMatcher(states),
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						ACTION : "GET_EMPLOYEE_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.EMPMST);
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
		    	return '<p>' + data.FNAME + '</p>';		    
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#employeeModal"><a href="#"> + New Employee</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		});
	
	/* Order Number Auto Suggestion */
	$('#billno').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'BILL',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/BillPaymentServlet?CMD=getBillByVendandcredit";
			$.ajax( {
			type : "GET",
			url : urlStr,
			async : true,
			data : {
				vendno:document.form.vendno.value,
				BILL_STATUS : "Paid",
				PLANT : plant
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.billno);
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
		    /*return '<p>' + data.BILL + '</p>';*/
		    return '<div onclick="loadBillDet(\''+ data.ID+'\',\''+ data.PONO+'\',\''+ data.GRNO+'\');"><p class="item-suggestion">'+data.BILL+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			//getOrderDetailsForBilling(selection.PONO);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.billno.value = "";
			}		
		});
	
	
	
	/* Payment Terms Auto Suggestion */
	$('#payment_terms').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'states',
		  display: 'PAYMENT_TERMS',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "/track/PaymentTermsServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_PAYMENT_TERMS_DETAILS",
					TERMS : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.terms);
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
		    return '<p>' + data.PAYMENT_TERMS + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="payTermsAddBtn footer"  data-toggle="modal" data-target="#payTermsModal"><a href="#"> + Add Payment Terms</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	/* Sales Location Auto Suggestion */
	$('#SALES_LOC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{ 
		  display: 'STATE',  
		  async: true, 
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						ACTION : "GET_SALES_LOCATION_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.SLOCMST);
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
		    	return '<p onclick="document.form.STATE_PREFIX.value = \''+data.STATE_PREFIX+'\'">' + data.STATE + '</p>';		    
			}
		  }
		}).on('typeahead:render',function(event,selection){
			
		  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			//$(".taxSearch").typeahead('val', '"');
			//$(".taxSearch").typeahead('val', '');
			changetaxnew();
		});
	/*$(".taxSearch").on('focus', function(e){
		$(".taxSearch").typeahead('val', '"');
		$(".taxSearch").typeahead('val', '');	
	});*/
	$(document).on("focus", ".taxSearch" , function() {
		$(this).typeahead('val', '"');
		$(this).typeahead('val', '');	
    });
	/* To get the suggestion data for Discount Account */
	$(".discountAccountSearch").typeahead({
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
						module:"purchasecreditaccount",
						NAME : query
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
				menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
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
	
	if($("#creditnote").val()){
		getEditDetail(document.form.billHdrId.value,document.form.billno.value);
		$("input[name ='vendname']").typeahead('val', $("#vendname").val());		
	}
	
	$(".bill-table tbody").on('click','.bill-action',function(){
		debugger;	    
	    var obj = $(this).closest('tr').find('td:nth-child(7)');
	    calculateTax(obj, "", "", "");
	    $(this).parent().parent().remove();
	    calculateTotal();
	});
	
	$("#btnBillDraft").click(function(){
		$('input[name ="bill_status"]').val('Draft');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		$("#createBillForm").submit();
	});
	$("#btnBillOpen").click(function(){
		$('input[name ="bill_status"]').val('Open');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		$("#createBillForm").submit();
	});
	
	$(document).on("focusout",".taxSearch",function(){
		var costA=$(this).closest('tr').find('input[name = "cost"]').val();
		
		var selectedTax=$(this).val();
		var selectedTaxType=selectedTax.substr(0, selectedTax.indexOf('('));
		if(selectedTaxType=="ZERO RATE")
			{
				if(costA==0)
					zerotype--;
			}
		if(costA==0)
			{
				alert("Unit cost must be greater than zero");
				$(this).typeahead('val', '');
				$(this).closest('td').data('name','');
				$(this).closest('td').data('tax','');
				renderTaxDetails();
				return false;
			}
		if($(this).val() == ""){
			var name = $(this).closest('td').data('name');
			var percentage = $(this).closest('td').data('tax');
			
			$(this).closest('td').data('name','');
			$(this).closest('td').data('tax','');
			var amount = $(this).closest('tr').find('input[name = "amount"]').val();
			discountValue = parseFloat(amount*(percentage/100)).toFixed(3);
			
			$.each(taxList, function( key, data ) {
				if(data.types == name){
					data.value = parseFloat(data.value)-parseFloat(discountValue);
				}
			});
		}
		renderTaxDetails()
	});
	
	$("#billAttch").change(function(){
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
	if($("#pono").val()){
		/*getOrderDetailsForBilling($("#pono").val());
		$("input[name ='vendname']").typeahead('val', $("#vendname").val());
		$(".add-line").hide();
		$("#item_rates").attr("readonly", true);*/
	}
	
	$(document).on("change",".itemChk",function(){
		debugger;
		if(this.checked){
			$(this).parent().parent().find('input[type="text"]').attr("disabled", false);
			var obj = $(this).parent().parent().find('td:nth-child(8)');
			calculatePartialTax(obj, "PURCHASE", "7.0", "PURCHASE [7.0%]");
		}else{
			$(this).parent().parent().find('input[type="text"]').attr("disabled", true);
			var obj = $(this).parent().parent().find('td:nth-child(8)');
			calculatePartialTax(obj, "", "", "");
		}	
		calculateTotal();
	});
	
	$(document).on("focusout","input[name ='shippingcost']",function(){
		var currentShippingCost = parseFloat($("input[name ='shippingcost']").val());
		var originalShippingCost = parseFloat($("input[name ='oShippingcost']").val());
		if(currentShippingCost > originalShippingCost){
			alert("Cannot exceed original shipping cost of "+originalShippingCost);
			$("input[name ='shippingcost']").val(originalShippingCost);
		}
		calculateTotal();
	});
	
	 /* To get the suggestion data for Status */
	$("#status").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'postatus',
	  display: 'value',  
	  source: substringMatcher(postatus),
	  limit: 9999,
	  templates: {
	  empty: [
		  '<div style="padding:3px 20px">',
			'No results found',
		  '</div>',
		].join('\n'),
		suggestion: function(data) {
		return '<p>' + data.value + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	});
	
	/* PURCHASE TAX Auto Suggestion */
	$("#Purchasetax").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DISPLAY',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax({
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_TAX_TYPE_DATA_PO",
					SALESLOC : $("input[name=STATE_PREFIX]").val(),
					GST_PERCENTAGE : $("input[name=GST]").val(),
					TAXKEY : "INBOUND",
					GSTTYPE : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.records);
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
			return '<p onclick="calculateTaxPO(this,\''+data.SGSTTYPES+'\',\''+data.SGSTPERCENTAGE+'\',\''+data.DISPLAY+'\',\''+data.ISZERO+'\',\''+data.ISSHOW+'\',\''+data.ID+'\')">' 
			+ data.DISPLAY + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			/*menuElement.after( '<div class="taxAddBtn footer"  data-toggle="modal" data-target="#gstModal"><a href="#"> + New Tax</a></div>');*/
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
	
	$(document).on("focusout","input[name ='CURRENCYUSEQT']",function(){
		var CURRENCYUSEQTCost = parseFloat($("input[name ='CURRENCYUSEQT']").val());
		if(!Number.isNaN(CURRENCYUSEQTCost))
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});
	
	addSuggestionToTable();
});
function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}


function addRow(){
	var body="";
	body += '<tr>';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">';
	body += '</td>';
	body += '<td class="bill-item-dbt">';
	body += '<input type="text" name="item" class="form-control itemSearch" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';
	body += '<td class="bill-acc">';
	body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Select Account">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
	body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
	body += '<td class="item-discount text-right">';
	body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
	body += '</td>';
	body += '<td class="item-tax">';
	body += '<input type="hidden" name="tax_type">';
	body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" readonly>';
	body += '</td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += '<input name="amount" type="text" class="form-control text-right" value="0.00" readonly="readonly" style="display:inline-block;">';
	body += '<input name="landedCost" type="text" value="0.0" hidden>';
	body += '</td>';
	body += '</tr>';
	$(".bill-table tbody").append(body);
	removeSuggestionToTable();
	addSuggestionToTable();
	
	$('input,textarea').on('keypress', function (e) {
	    var ingnore_key_codes = [39, 91];
	  
	    if ($.inArray(e.which, ingnore_key_codes) >= 0) {
	        e.preventDefault();
			alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	       
	    } else {
	      
	    }
	}).on("paste",function(e){
		var textboxvalue = this;
	   setTimeout(function(){
	    var sValue = $(textboxvalue).val();
	    convertCharToString3(sValue); 
	 },100);
	 
	});
}

function checkorderno(orderno){	
		var urlStr = "/track/SupplierCreditServlet?ACTION=CheckOrderno";
		$.ajax( {
			type : "GET",
			url : urlStr,
			async : true,
			data : {
				CREDITNOTE : orderno,
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						alert("Order Number Already Exists");
						document.getElementById("creditnote").focus();
						document.getElementById("creditnote").value="";
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkemployeess(employee){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				FNAME : employee,
				ACTION : "EMPLOYEE_CHECKS"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Employee Does't Exists");
						document.getElementById("EMP_NAME").focus();
						$("#EMP_NAME").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checktax(tax){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				TAXTYPE : tax,
				ACTION : "TAX_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Tax Does't Exists");
						document.getElementById("Purchasetax").focus();
						$("#Purchasetax").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function addSuggestionToTable(){
	var plant = document.form.plant.value;
	
	/* To get the suggestion data for Product */
	$(".itemSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
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
			return '<div onclick="loadItemData(this,\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.COST+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.PURCHASEINVQTY+' '+data.PURCHASEUOM+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="itemAddBtn footer"  data-toggle="modal" data-target="#productModal"><a href="#"> + Add New Product</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	/* To get the suggestion data for Account */
	$(".accountSearch").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true,
	  classNames: {
		 	menu: 'bigdrop'
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
					module:"purchasecreditaccount",
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
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
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
	
	/* To get the suggestion data for Tax */
	$(".taxSearch").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'DISPLAY',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax({
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_GST_TYPE_DATA",
				SALESLOC : document.form.STATE_PREFIX.value,
				GSTTYPE : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.records);
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
		return '<p onclick="calculateTax(this,\''+data.SGSTTYPES+'\',\''+data.SGSTPERCENTAGE+'\',\''+data.DISPLAY+'\')">' 
		+ data.DISPLAY + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="taxAddBtn footer"  data-toggle="modal" data-target="#gstModal"><a href="#"> + New Tax</a></div>');
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

function removeSuggestionToTable(){
	$(".itemSearch").typeahead('destroy');
	$(".accountSearch").typeahead('destroy');
	$(".taxSearch").typeahead('destroy');
}

function loadItemData(obj, catalogPath, account, cost){
	
	
	
	
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("td:nth-child(3)").find('input').typeahead('val', account);
	$(obj).closest('tr').find("td:nth-child(5)").find('input').val(cost);
	calculateAmount(obj);
}

function calculateAmount(obj){
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	var qty = parseFloat($(obj).closest('tr').find("td:nth-child(4)").find('input').val()).toFixed(3);
	var qtyold = parseFloat($(obj).closest('tr').find('input[name=qtyold]').val()).toFixed(3);
	if(qty > qtyold){
		alert("Quantity exceeds the return quantity");
		qty = qtyold;
	}
	var cost = parseFloat($(obj).closest('tr').find("td:nth-child(5)").find('input').val()).toFixed(numberOfDecimal);
	var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(6)").find('input').val()).toFixed(numberOfDecimal);
	var discounType = $(obj).closest('tr').find("td:nth-child(6)").find('select').val();
	var itemDiscountval=itemDiscount;
	 if(discounType == "%"){
		 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
		 itemDiscount = parseFloat(itemDiscount).toFixed(3);
	 }
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
	
	if(parseFloat(amount) > parseFloat("0")){
		$(obj).closest('tr').find("td:nth-child(6)").find('input').val(itemDiscount);
		$(obj).closest('tr').find("td:nth-child(8)").find('input').val(amount);
	}else{
		alert("discout should be less than tha amount");
		if(discounType == "%"){
			$(obj).closest('tr').find("td:nth-child(6)").find('input').val(parseFloat("0").toFixed(3));
		}else{
			$(obj).closest('tr').find("td:nth-child(6)").find('input').val(parseFloat("0").toFixed(numberOfDecimal));
		}
		var originalamount = parseFloat((qty*cost)).toFixed(numberOfDecimal);
		$(obj).closest('tr').find("td:nth-child(8)").find('input').val(originalamount);
	}
	
	$(obj).closest('tr').find("td:nth-child(4)").find('input').val(qty);
	$(obj).closest('tr').find("td:nth-child(5)").find('input').val(cost);
	
	changetaxfordiscountnew();
	calculateTotal();
}

/*function calculateTotal(){
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = 0, discountValue=0, shippingvalue=0, adjustmentvalue=0,totalvalue=0,orderdisc=0;
	
	var discount= document.form.discount.value;
	discount = parseFloat(discount).toFixed(numberOfDecimal);
	$("input[name ='discount']").val(discount);
	
	var adjustment= document.form.adjustment.value;
	adjustment = parseFloat(adjustment).toFixed(numberOfDecimal);
	$("input[name ='adjustment']").val(adjustment);
	
	$(".bill-table tbody tr td:last-child").each(function() {
		if($(this).find('input').attr("disabled") != "disabled"){
		    amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
		    amount = parseFloat(amount).toFixed(numberOfDecimal);
		    $("#subTotal").html(amount);
		    $('input[name ="sub_total"]').val(amount);// hidden input
		}
	});
	
	
	 
	if(discount > 0){
		 $(".discountAccountSection").show();
		 $(".discountAccountSearch").prop('disabled', false); 
	 }else{
		 $(".discountAccountSection").hide();
		 $(".discountAccountSearch").prop('disabled', true);
	 }
	 
	 var taxTotal = 0;
	 if($(".taxDetails").html().length > 0 && ($("#item_rates").val() == 0)){
		 $('.taxAmount').each(function(i, obj) {
			 taxTotal += parseFloat($(this).html());
		 });
	 }
	 
	 var orderdiscpercentage = $('input[name ="orderdiscount"]').val();
	 orderdiscpercentage = (orderdiscpercentage == "") ? 0.00 : orderdiscpercentage;
	 orderdisc = parseFloat(parseFloat(amount)*(parseFloat(orderdiscpercentage)/100)); 
	 $("#orderdiscount").html(parseFloat(-orderdisc).toFixed(numberOfDecimal));
	 
	 var fsubtotal = parseFloat(amount)-parseFloat(orderdisc);
	 var discount = $('input[name ="discount"]').val();
	 discount = (discount == "") ? 0.00 : parseFloat(discount).toFixed(numberOfDecimal);
	 var discounType = $('select[name ="discount_type"]').val();
	 if(discounType == "%"){
		 discountValue = parseFloat(fsubtotal*(discount/100)).toFixed(numberOfDecimal);
	 }else{
		 discountValue = discount;
	 }
	 
	 if(parseFloat(fsubtotal) >= parseFloat(discountValue)){
		 if(discounType == "%"){
			 $('input[name ="discount"]').val(parseFloat(discount).toFixed(3));
			 $("#discount").html(parseFloat(-discountValue).toFixed(numberOfDecimal));
		 }else{
			 $("#discount").html(parseFloat(-discountValue).toFixed(numberOfDecimal));
		 }
	 }else{
		 discountValue = "0";
		 if(discounType == "%"){
			 $('input[name ="discount"]').val(parseFloat("0").toFixed(3));
		 }else{
			 $('input[name ="discount"]').val(parseFloat("0").toFixed(numberOfDecimal));
		 }
		 $("#discount").html(parseFloat(-discountValue).toFixed(numberOfDecimal));
	 }
	 
	 
	 var adjustmentvalue = $('input[name ="adjustment"]').val();
	 adjustmentvalue = (adjustmentvalue == "") ? 0.00 : adjustmentvalue;

	 var pretotal = parseFloat(parseFloat(amount)- parseFloat(orderdisc) - parseFloat(discountValue) + parseFloat(taxTotal)).toFixed(numberOfDecimal);
	 
	 if(parseFloat(pretotal) >= parseFloat(adjustmentvalue)){
		 $("#adjustment").html(parseFloat(adjustmentvalue).toFixed(numberOfDecimal));
		 $('input[name ="adjustment"]').val(parseFloat(adjustmentvalue).toFixed(numberOfDecimal));
	 }else{
		 alert("adjustment should be less than "+pretotal);
		 adjustmentvalue = "0";
		 $("#adjustment").html(parseFloat("0").toFixed(numberOfDecimal));
		 $('input[name ="adjustment"]').val(parseFloat("0").toFixed(numberOfDecimal));
	 }
	 
	 
	 totalvalue = parseFloat(parseFloat(amount)- parseFloat(orderdisc) - parseFloat(discountValue) + parseFloat(taxTotal)
			 + parseFloat(adjustmentvalue)).toFixed(numberOfDecimal);
	 $("#total").html(totalvalue);
	 $("#total_amount").val(totalvalue); // hidden input


}*/

function calculateTax(obj, types, percentage, display){
	var originalTaxType= types;
	var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); 
	if(taxtype=="ZERO RATE")
		{
			zerotype++;
		}
	$(obj).closest('td').find('input[name = "tax_type"]').val(display);
	var numberOfDecimal = $("#numberOfDecimal").val();
	var tax = new Object();
	tax.types = types;
	tax.percentage = percentage;
	tax.display = display;
	var prevTypes = $(obj).closest('td').data('name');
	if(prevTypes !== "" && prevTypes != undefined){
		var name = $(obj).closest('td').data('name');
		var prevPercentage = $(obj).closest('td').data('tax');
		
		$(obj).closest('td').data('name','');
		$(obj).closest('td').data('tax','');
		var amount = $(obj).closest('tr').find('input[name = "amount"]').val();
		discountValue = parseFloat(amount*(prevPercentage/100)).toFixed(numberOfDecimal);
		
		$.each(taxList, function( key, data ) {
			if(data.types == name){
				data.value = parseFloat(parseFloat(data.value)-parseFloat(discountValue)).toFixed(numberOfDecimal);
				var currenttype=name.substr(0, name.indexOf('(')); 
				if(currenttype=="ZERO RATE")
					{
						zerotype--;
					}
			}
		});
	}
	
	$(obj).closest('td').data('name', types);
	$(obj).closest('td').data('tax', percentage);
	var amount = $(obj).closest('tr').find('input[name = "amount"]').val();
	discountValue = parseFloat(amount*(percentage/100)).toFixed(numberOfDecimal);
	tax.value = discountValue;
	
	if(taxList.length == 0){
		taxList.push(tax);	
	}else{
		var match = false;
		$.each(taxList, function( key, data ) {
			if(data.types == types){
				data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
				match = true;
			}
		});
		if(!match){
			taxList.push(tax);
		}
	}
	renderTaxDetails();
}

function renderTaxDetails(){var html="";
var taxTotal = 0;
var subtotal =0;
var numberOfDecimal = $("#numberOfDecimal").val();


 
 
$.each(taxList, function( key, data ) {
	var mydata=JSON.stringify(data);
	console.log("Tax dta:"+mydata);
	var originalTaxType= data.types;
	var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); 
	console.log("Tax Type:"+taxtype);
	console.log("Zero Type:"+zerotype);
	
	var percentageget = data.percentage;
	var discount = document.form.discount.value;
	var discounType = $('select[name ="discount_type"]').val();
	$(".bill-table tbody tr td:last-child").each(function() {
		if($(this).find('input').attr("disabled") != "disabled"){
			subtotal =  parseFloat(subtotal) + parseFloat($(this).find('input').val());
		}
	});
	
	 var orderdiscpercentage = $('input[name ="orderdiscount"]').val();
	 orderdiscpercentage = (orderdiscpercentage == "") ? 0.00 : orderdiscpercentage;
	 var orderdisc = parseFloat(parseFloat(subtotal)*(parseFloat(orderdiscpercentage)/100));
	 var orderdiscounttax = parseFloat(orderdisc*(percentageget/100)).toFixed(numberOfDecimal);
	 
	 var balance = parseFloat(subtotal)-parseFloat(orderdisc);
	 var discountValue ="0";
	 var discounttax = "0";

	 if(discounType == "%"){
		 discountValue = parseFloat(balance*(discount/100)).toFixed(3);
	 }else{
		 discountValue = discount;
	 }
	 
	 if(parseFloat(balance) >= parseFloat(discountValue)){
		 discounttax = parseFloat(discountValue*(percentageget/100)).toFixed(numberOfDecimal);
	 }else{
		 discounttax = "0";
		 alert("discount amount should be less than "+balance);
	 }
	
	if(data.value > 0 || taxtype=="ZERO RATE" && zerotype>0){
		var myJSON = JSON.stringify(data);
		console.log("Tax Data:"+myJSON);
		html+='<div class="total-row">';
		html+='<div class="total-label">'+data.display+'</div>';
		if(taxtype=="ZERO RATE"){
			html+='<div class="total-amount taxAmount">'+data.value+'</div>';
		}else{
			console.log("mik"+data.value);
			console.log("mik"+discounttax);
			console.log("mik"+orderdiscounttax);
			html+='<div class="total-amount taxAmount">'+parseFloat((parseFloat(data.value))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal)+'</div>';
		}
		
		html+='</div>';
		taxTotal += parseFloat((parseFloat(data.value))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal);
	}
});
$(".taxDetails").html(html);
calculateTotal();
($("#item_rates").val() == 0) ? $(".productRate").hide() : $(".productRate").show(); 

if($("#item_rates").val() == 0){
	$('input[name ="taxTotal"]').val(taxTotal);
}else{
	$(".taxDetails").html("");
	$('input[name ="taxTotal"]').val("0");
}
}

function validateBill(){
	var supplier = document.form.vendno.value;
	var creditnote = document.form.creditnote.value;
	var billDate = document.form.bill_date.value;
	var itemRates = document.form.item_rates.value;
	var discount = document.form.discount.value;
	var discountAccount = document.form.discount_account.value;
	var isItemValid = true, isAccValid = true;
	var CURRENCYUSEQT = document.form.CURRENCYUSEQT.value;
	if(supplier == ""){
		alert("Please select a Supplier.");
		return false;
	}	
	if(creditnote == ""){
		alert("Please enter the Credit Note.");
		return false;
	}
	
	if(CURRENCYUSEQT == ""){
		alert("Please select a Exchange Rate.");
		return false;
	}
	CheckCreditnote(creditnote);
	if(billDate == ""){
		alert("Please enter a valid Credit Date.");
		return false;
	}
	/*if(discount != "" && discountAccount == ""){
		if(discount > 0)
		{
		alert("Please choose an account for discount.");
		return false;
		}
	}*/
	
	$("input[name ='item']").each(function() {
	    if($(this).val() == ""){	    	
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The Product field cannot be empty.");
		return false;
	}
	$("input[name ='account_name']").each(function() {
		if($(this).val() == ""){
	    	isAccValid =  false;
	    }
	});	
	if(!isAccValid){
		alert("The Account field cannot be empty.");
		return false;
	}
	
	$('.item_discount_type').prop('disabled',false);
	$('.discount_type').prop('disabled',false);
	$('#oddiscount_type').prop('disabled',false);
	$('#SALES_LOC').prop('disabled',false);
	return true;
}

function supplierCallback(data){
	if(data.STATUS="SUCCESS"){
		alert(data.MESSAGE);
		$("#vendname").typeahead('val', data.VENDOR_NAME);
	}
}

function employeeCallback(data){
	if(data.STATUS="SUCCESS"){
		//alert(data.MESSAGE);
		$("#EMP_NAME").typeahead('val', data.EMP_NAME);
	}
}

function gstCallback(data){
	if(data.STATUS="SUCCESS"){
		alert(data.MESSAGE);
		$("input[name ='tax_type']").typeahead('val', data.GST);
	}
}

function setSupplierData(suppierData){	
	$("input[name ='vendno']").val(suppierData.vendno);
	$("#vendname").typeahead('val', suppierData.vendname);
	$('select[name ="nTAXTREATMENT"]').val(suppierData.sTAXTREATMENT);
	$('#nTAXTREATMENT').attr('disabled',false);
	if(suppierData.sTAXTREATMENT =="GCC VAT Registered"||suppierData.sTAXTREATMENT=="GCC NON VAT Registered"||suppierData.sTAXTREATMENT=="NON GCC")
	{
		document.getElementById('CHK1').style.display = 'block';
	}
	else
		document.getElementById('CHK1').style.display = 'none';
	/* To reset Order number Autosuggestion*/
	$("#pono").typeahead('val', '"');
	$("#pono").typeahead('val', '');
}

function successAccountCallback(accountData){
	if(accountData.STATUS="SUCCESS"){
		alert(accountData.MESSAGE);
		$("input[name ='account_name']").typeahead('val', accountData.ACCOUNT_NAME);
	}
}

function productCallback(productData){
	if(productData.STATUS="SUCCESS"){
		alert(productData.MESSAGE);
		$("input[name ='item']").typeahead('val', productData.ITEM);
	}
}

function payTermsCallback(payTermsData){
	if(payTermsData.STATUS="SUCCESS"){
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
	}
}

function getOrderDetailsForBilling(pono){
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/purchaseorderservlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_ORDER_DETAILS_FOR_BILLING",
			PONO : pono
		},
		dataType : "json",
		success : function(data) {
			loadBillTable(data.orders);
		}
	});
}

function loadBillTable(orders){
	var body="";
	var ITEMDESC = escapeHtml(data.ITEMDESC);
	taxList = [];
	$.each(orders, function( key, data ) {	
		body += '<tr>';
		body += '<td><input type="checkbox" class="itemChk" checked>';
		body += '<input type="text" name="lnno" value="'+data.POLNNO+'" hidden>';
		body += '</td>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item-dbt">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" readonly="readonly">';
		body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly  value="'+ITEMDESC+'">';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control" value="'+data.ACCOUNT+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTYOR+'" readonly="readonly"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITCOST+'" readonly="readonly"></td>';
		body += '<td class="item-discount text-right">';
		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
		body += '</td>';
		body += '<td class="item-tax" data-name="PURCHASE" data-tax="'+data.INBOUND_GST+'">';
		body += '<input type="text" name="tax_type" class="form-control" value="PURCHASE ['+data.INBOUND_GST+'%]" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" readonly="readonly" style="display:inline-block;">';
		body += '<input name="landedCost" type="text" value="0.0" hidden>';
		body += '</td>';
		body += '</tr>';

		var tax = new Object();
		var percentage = data.INBOUND_GST;
		var numberOfDecimal = $("#numberOfDecimal").val();
		tax.types = "PURCHASE";
		tax.percentage = percentage;
		tax.display = 'PURCHASE ['+data.INBOUND_GST+'%]';

		var amount = data.AMOUNT;
		discountValue = parseFloat(amount*(percentage/100)).toFixed(numberOfDecimal);
		tax.value = discountValue;

		if(taxList.length == 0){
			taxList.push(tax);
		}else{
			var match = false;
			$.each(taxList, function( key, data ) {
				if(data.types == "PURCHASE"){
					data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
					match = true;
				}
			});
			if(!match){
				taxList.push(tax);
			}
		}	
		if($("input[name='creditnote']").attr("readonly") != "readonly"){
			$("input[name ='shippingcost']").val(data.ACTUALSHIPPINGCOST);	
			$("input[name ='oShippingcost']").val(data.ACTUALSHIPPINGCOST);/* To store Original Shipping cost for validation*/	
		}
	});
	
	$(".bill-table tbody").html(body);
	renderTaxDetails();
	removeSuggestionToTable();
	tooltioedit();
	$('input,textarea').on('keypress', function (e) {
	    var ingnore_key_codes = [39, 91];
	  
	    if ($.inArray(e.which, ingnore_key_codes) >= 0) {
	        e.preventDefault();
			alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	       
	    } else {
	      
	    }
	}).on("paste",function(e){
		var textboxvalue = this;
	   setTimeout(function(){
	    var sValue = $(textboxvalue).val();
	    convertCharToString3(sValue); 
	 },100);
	 
	});
}
function tooltioedit(){
	$(".bill-table tbody tr").each(function() {
			var obj = $('td:eq(1)', this);
			loadItemDescToolTipedit(obj);
	});
}
function loadItemDescToolTipedit(obj){
	var content = "<p class='text-left'> "+$(obj).closest('tr').find("input[name=ITEMDES]").val()+"</p>";
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip({title: content, html: true, placement: "top"});
}
function getNextCreditnote(){
	$.ajax( {
		type : "GET",
		url : "/track/MasterServlet?ACTION=GET_CREDITNOTE_SEQUENCE",
		async : true,
		dataType : "json",
		contentType: false,
        processData: false,
        success : function(data) {
			if (data.ERROR_CODE == "100") {
				document.form.creditnote.value = data.CUSTCODE;
			}
			else{
				alert("Something went wrong. Please try again later.");
			}						
		},
        error: function (data) {	        	
        	alert(data.Message);
        }
	});	
	return false;
}



function getEditDetail(TranId,billno){
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/SupplierCreditServlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_EDIT_SUPPLIERCREDIT_DETAILS",
			Id : TranId
		},
		dataType : "json",
		success : function(data) {
			if(billno == "" || billno == null || billno == "0"){
				loadEditTableeditable(data.orders);
			}else{
				loadEditTable(data.orders);
			}
			
		}
	});
}


function loadEditTableeditable(orders){
	var curency = document.form.curency.value;
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
		var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");
		var numberOfDecimal = $("#numberOfDecimal").val();
		tax.types = percentage[0].trim();
		tax.percentage = percentage[1];
		tax.display = data.TAX_TYPE;
		var taxpercentage=percentage[1];
		var taxtype=percentage[0].trim();
		var loadtype=taxtype.substr(0, taxtype.indexOf('(')); 
		var ITEM_DESC = escapeHtml(data.ITEMDESC);
		
		if(loadtype=="ZERO RATE")
			{
				zerotype++;
			}
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item-dbt">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)">';
		body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly data-pd="'+ITEMDESC+'" value="'+ITEMDESC+'">';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" value="'+data.ACCOUNT+'">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITPRICE+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-discount text-right">';		
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEM_DISCOUNT+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
		body += '</td>';
		body += '<td class="item-tax" data-name="'+taxtype+'" data-tax="'+taxpercentage+'">';
		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';
		if(data.TAX_TYPE=="EXEMPT[0.0%]")
		{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="EXEMPT">';
		}
		else if(data.TAX_TYPE=="OUT OF SCOPE[0.0%]")
			{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="OUT OF SCOPE">';
			}
		else
			{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'">';
			}
		//body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" >';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" style="display:inline-block;"></td>';
		body += '</tr>';		
		

		
		
		var amount = data.AMOUNT;		
		discountValue = parseFloat(amount*(percentage[1]/100)).toFixed(numberOfDecimal);
		tax.value = discountValue;
		
		if(taxList.length == 0){
			taxList.push(tax);
		}else{
			var match = false;
			$.each(taxList, function( key, data ) {
				if(data.types == percentage[0].trim()){
					data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
					match = true;
				}
			});
			if(!match){
				taxList.push(tax);
			}
		}	
		$("textarea[name ='note']").val(data.NOTE);
		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
	});
	$(".bill-table tbody").html(body);
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
	tooltioedit();
	$('input,textarea').on('keypress', function (e) {
	    var ingnore_key_codes = [39, 91];
	  
	    if ($.inArray(e.which, ingnore_key_codes) >= 0) {
	        e.preventDefault();
			alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	       
	    } else {
	      
	    }
	}).on("paste",function(e){
		var textboxvalue = this;
	   setTimeout(function(){
	    var sValue = $(textboxvalue).val();
	    convertCharToString3(sValue); 
	 },100);
	 
	});
}

function loadEditTable(orders){
	var curency = document.form.curency.value;
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
		var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");
		var numberOfDecimal = $("#numberOfDecimal").val();
		tax.types = percentage[0].trim();
		tax.percentage = percentage[1];
		tax.display = data.TAX_TYPE;
		var taxpercentage=percentage[1];
		var taxtype=percentage[0].trim();
		var loadtype=taxtype.substr(0, taxtype.indexOf('(')); 
		var ITEM_DESC = escapeHtml(data.ITEMDESC);
		
		if(loadtype=="ZERO RATE")
			{
				zerotype++;
			}
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item-dbt">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)" readonly>';
		body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly data-pd="'+ITEMDESC+'" value="'+ITEMDESC+'">';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" value="'+data.ACCOUNT+'" readonly>';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)" readonly></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITPRICE+'" onchange="calculateAmount(this)" readonly></td>';
		body += '<td class="item-discount text-right">';		
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEM_DISCOUNT+'" onchange="calculateAmount(this)" readonly>';
		body += '</td>';
		body += '<td class="item-tax" data-name="'+taxtype+'" data-tax="'+taxpercentage+'" readonly>';
		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control" readonly>';
		if(data.TAX_TYPE=="EXEMPT[0.0%]")
		{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="EXEMPT" readonly>';
		}
		else if(data.TAX_TYPE=="OUT OF SCOPE[0.0%]")
			{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="OUT OF SCOPE" readonly>';
			}
		else
			{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly>';
			}
		//body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" >';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" style="display:inline-block;" readonly></td>';
		body += '</tr>';		
		

		
		
		var amount = data.AMOUNT;		
		discountValue = parseFloat(amount*(percentage[1]/100)).toFixed(numberOfDecimal);
		tax.value = discountValue;
		
		if(taxList.length == 0){
			taxList.push(tax);
		}else{
			var match = false;
			$.each(taxList, function( key, data ) {
				if(data.types == percentage[0].trim()){
					data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
					match = true;
				}
			});
			if(!match){
				taxList.push(tax);
			}
		}	
		$("textarea[name ='note']").val(data.NOTE);
		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
	});
	$(".bill-table tbody").html(body);
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
	tooltioedit();
	$('input,textarea').on('keypress', function (e) {
	    var ingnore_key_codes = [39, 91];
	  
	    if ($.inArray(e.which, ingnore_key_codes) >= 0) {
	        e.preventDefault();
			alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	       
	    } else {
	      
	    }
	}).on("paste",function(e){
		var textboxvalue = this;
	   setTimeout(function(){
	    var sValue = $(textboxvalue).val();
	    convertCharToString3(sValue); 
	 },100);
	 
	});
}
function downloadFile(id,fileName)
{
	 var urlStrAttach = "/track/SupplierCreditServlet?ACTION=downloadAttachmentById&attachid="+id;
	 var xhr=new XMLHttpRequest();
	 xhr.open("POST", urlStrAttach, true);
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
	var urlStrAttach = "/track/SupplierCreditServlet?ACTION=removeAttachmentById&removeid="+id;	
	$.ajax( {
		type : "POST",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
}
function CheckCreditnote(Creditnote){
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/SupplierCreditServlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "CHECK_SUPPLIER_CREDIT_NOTE",
			CreditNote : Creditnote
		},
		dataType : "json",
		success : function(data) {
			if (data.ERROR_CODE == "100") {
				alert("Credit note already available");
			}
			
		}
	});
		return false;
}

function loadBillDet(id,pono,grno){
	if(grno == "" || grno == null){
		$("input[name ='grnno']").val('');
	}else{
		$("input[name ='grnno']").val(grno);
	}
	
	if(pono == "" || pono == null){
		$("#pono").typeahead('val', '"');
		$("#pono").typeahead('val', '');
	}else{
		$("input[name ='pono']").val(pono);
	}
	
	$("input[name ='billhdrid']").val(id);
	
	loaddatausingbillno(id);
} 

function loaddatausingbillno(TranId){
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/SupplierCreditServlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_BILL_DET",
			Id : TranId
		},
		dataType : "json",
		success : function(data) {
			loadTableusingnillhdrid(data.orders);
		}
	});
}

/*function loadTableusingnillhdrid(orders){
	var curency = document.form.curency.value;
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;" readonly="readonly">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)" readonly="readonly">';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" value="'+data.ACCOUNT_NAME+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)" readonly="readonly"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.COST+'" onchange="calculateAmount(this)" readonly="readonly"></td>';
		body += '<td class="item-discount text-right">';		
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.DISCOUNT+'" onchange="calculateAmount(this)" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-tax">';
		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';
		body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" style="display:inline-block;" readonly="readonly"></td>';
		body += '</tr>';		
		

		var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");
		var numberOfDecimal = $("#numberOfDecimal").val();
		tax.types = percentage[0].trim();
		tax.percentage = percentage[1];
		tax.display = data.TAX_TYPE;
		
		var amount = data.AMOUNT;		
		discountValue = parseFloat(amount*(percentage[1]/100)).toFixed(numberOfDecimal);
		tax.value = discountValue;
		
		if(taxList.length == 0){
			taxList.push(tax);
		}else{
			var match = false;
			$.each(taxList, function( key, data ) {
				if(data.types == percentage[0].trim()){
					data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
					match = true;
				}
			});
			if(!match){
				taxList.push(tax);
			}
		}	
		$("textarea[name ='note']").val(data.NOTE);
		
	});
	$(".bill-table tbody").html(body);
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
}*/

function loadTableusingnillhdrid(orders){

	var curency = document.form.curency.value;
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
		var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");
		var numberOfDecimal = $("#numberOfDecimal").val();
		tax.types = percentage[0].trim();
		tax.percentage = percentage[1];
		tax.display = data.TAX_TYPE;
		var taxpercentage=percentage[1];
		var taxtype=percentage[0].trim();
		var loadtype=taxtype.substr(0, taxtype.indexOf('(')); 
		var ITEM_DESC = escapeHtml(data.ITEMDESC);
		
		if(loadtype=="ZERO RATE")
			{
				zerotype++;
			}
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;" readonly="readonly">';
		body += '</td>';
		body += '<td class="bill-item-dbt">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)" readonly="readonly">';
		body += '<input class="form-control"  name="ITEMDES" value="'+ITEM_DESC+'" readonly style="height: 23px;background-color: #fff;" readonly>';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" value="'+data.ACCOUNT_NAME+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)" readonly="readonly"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.COST+'" onchange="calculateAmount(this)" readonly="readonly"></td>';
		body += '<td class="item-discount text-right">';		
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.DISCOUNT+'" onchange="calculateAmount(this)" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-tax" data-name="'+taxtype+'" data-tax="'+taxpercentage+'" readonly="readonly">';
		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';
		if(data.TAX_TYPE=="EXEMPT[0.0%]")
		{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="EXEMPT" readonly="readonly">';
		}
		else if(data.TAX_TYPE=="OUT OF SCOPE[0.0%]")
			{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="OUT OF SCOPE" readonly="readonly">';
			}
		else
			{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly="readonly">';
			}
		//body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" >';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" style="display:inline-block;" readonly="readonly"></td>';
		body += '</tr>';		
		

		
		
		var amount = data.AMOUNT;		
		discountValue = parseFloat(amount*(percentage[1]/100)).toFixed(numberOfDecimal);
		tax.value = discountValue;
		
		if(taxList.length == 0){
			taxList.push(tax);
		}else{
			var match = false;
			$.each(taxList, function( key, data ) {
				if(data.types == percentage[0].trim()){
					data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
					match = true;
				}
			});
			if(!match){
				taxList.push(tax);
			}
		}	
		$("textarea[name ='note']").val(data.NOTE);
		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
	});
	$(".bill-table tbody").html(body);
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
	tooltioedit();
	$('input,textarea').on('keypress', function (e) {
	    var ingnore_key_codes = [39, 91];
	  
	    if ($.inArray(e.which, ingnore_key_codes) >= 0) {
	        e.preventDefault();
			alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	       
	    } else {
	      
	    }
	}).on("paste",function(e){
		var textboxvalue = this;
	   setTimeout(function(){
	    var sValue = $(textboxvalue).val();
	    convertCharToString3(sValue); 
	 },100);
	 
	});

	
}
function escapeHtml(text) {
	  var map = {
	    '&': '&amp;',
	    '<': '&lt;',
	    '>': '&gt;',
	    '"': '&quot;',
	    "'": '&#039;'
	  };
	   return text.replace(/[&<>"']/g, function(m) { return map[m]; });
	}
function getvendname(TAXTREATMENT,VENDO,CURRENCYID,CURRENCY,CURRENCYUSEQT){
	
	
	$("input[name ='vendno']").val(VENDO);
	
	$('select[name ="CURRENCYID"]').val(CURRENCYID);
	$('select[name ="CURRENCY"]').val(CURRENCY);
	$('select[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
	document.form.vendno.value =VENDO;
	if(TAXTREATMENT =="GCC VAT Registered"||TAXTREATMENT=="GCC NON VAT Registered"||TAXTREATMENT=="NON GCC")
	{
		document.getElementById('CHK1').style.display = 'block';
	}
	else
		document.getElementById('CHK1').style.display = 'none';
	}



function removetaxtrestl(){
	$('#nTAXTREATMENT').attr('disabled',false);
}

function OnTaxChange(TAXTREATMENT)
{	
	document.getElementById("REVERSECHARGE").checked = false;
	document.getElementById("GOODSIMPORT").checked = false;
	if(TAXTREATMENT =="GCC VAT Registered"||TAXTREATMENT=="GCC NON VAT Registered"||TAXTREATMENT=="NON GCC")
	{
		document.getElementById('CHK1').style.display = 'block';
	}
	else
		document.getElementById('CHK1').style.display = 'none';
	}


function getPOreturn(prno,pono,grno){
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/SupplierCreditServlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_PO_RETURN",
			PORETURN : prno,
			PONO:pono,
			GRNO:grno
			
		},
		dataType : "json",
		success : function(data) {
			
			loadgetPOreturntable(data.orders);
			
		}
	});
}


function loadgetPOreturntable(orders){
	var curency = document.form.curency.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	var body="";
	var billdiscount="0";
	var billsubtotal="0";
	var orderdiscount="0";
	var billdiscounttype="0";
	var subtotal ="0";
	var orderdiscounttype="";
	var isoddisctax ="0";
	var isdisctax ="0";
	var cequ="1";
	
	taxList = [];
	
	$.each(orders, function( key, data ) {
		
		var uprice= parseFloat((data.UNITPRICE*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
		var amt= parseFloat((data.AMOUNT*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
		var ITEMDESC = escapeHtml(data.ITEMDESC);
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="basecost" value="'+data.BASECOST+'">';
		body += '<input type="hidden" name="qtyold" value="'+data.QTY+'">';
		body += '<input type="hidden" name="tax" class="taxSearch">';
		body += '<input type="hidden" name="tax_type">';
		body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item-dbt">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)" readonly>';
		body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly value="'+ITEMDESC+'">';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" value="Inventory Asset" placeholder="Select Account">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+uprice+'" onchange="calculateAmount(this)" readonly></td>';
		/*body += '<td class="item-discount text-right">';		
		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
		body += '</td>';*/
		
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		if(data.ITEM_DISCOUNT_TYPE == "%"){
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" readonly>';
		}else{
			body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT*data.CURRENCYUSEQT).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" readonly>';	
		}
		body += '<select name="item_discounttype" class="discountPicker form-control item_discount_type" onchange="calculateAmount(this)">';
		if(data.ITEM_DISCOUNT_TYPE == curency){
			body += "<option selected value="+curency+">"+curency+"</option>";
		}else{
			body += "<option value="+curency+">"+curency+"</option>";
		}
		
		if(data.ITEM_DISCOUNT_TYPE == "%"){
			body += '<option selected >%</option>';	
		}else{
			body += '<option>%</option>';	
		}								
		body += '</select>';
		body += '</div>';
		body += '</div>'; 
		body += '</div>';
		body += '</td>';
//		body += '<td class="item-tax">';
//		body += '<input type="hidden" name="tax_type">';
//		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" readonly>';
//		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" style="display:inline-block;" readonly></td>';
		body += '</tr>';		
		
	
	/*	$("textarea[name ='note']").val(data.NOTE);*/
		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
		if(data.EMP_NAME!="")
		$("input[name ='EMP_NAME']").val(data.EMP_NAME);
		if(data.PAYMENT_TERMS!="")
		$("input[name ='payment_terms']").val(data.PAYMENT_TERMS);
		
		
		billdiscount = data.BILLDISCOUNT;
		//billsubtotal = parseFloat(data.BILLSUB_TOTAL) +  parseFloat(data.BILLLINEDISCOUNT);
		billsubtotal = parseFloat(data.BILLSUB_TOTAL);
		orderdiscount = data.ORDERDISCOUNT;
		billdiscounttype = data.BILLDISCOUNTTYPE;
		orderdiscounttype = data.ORDERDISCOUNTTYPE;
		isoddisctax = data.ISODISCOUNTTAXINC;
		isdisctax = data.ISDISCOUNTTAXINC;
		$("input[name ='CURRENCYUSEQTOLD']").val(data.CURRENCYUSEQT);
		$("input[name ='GST']").val(data.GST);
		$("select[name ='item_rates']").val(data.ISTAXINCLUSIVE);
		$("#discountblock").prop("readonly", true);
		 
	});
	$(".bill-table tbody").html(body);
	$('.item_discount_type').prop('disabled',true);
	
	$(".bill-table tbody tr td:last-child").each(function() {
		if($(this).find('input').attr("disabled") != "disabled"){
			subtotal =  parseFloat(subtotal) + parseFloat($(this).find('input').val());
		}
	});
	
	var percentage = (parseFloat(subtotal)*100)/parseFloat(billsubtotal);
	
	if(parseFloat(orderdiscount) > 0){
		if(orderdiscounttype == "%"){
			$('#oddiscount_type').empty();
			$('#oddiscount_type').append('<option value="%">%</option>');
			$("input[name ='orderdiscount']").val(parseFloat(orderdiscount).toFixed(3));
			//$("#oddiscount").attr("readonly", true);
		}else{
			$('#oddiscount_type').empty();
			$('#oddiscount_type').append('<option value="' + curency + '">' + curency+ '</option>');
			orderdiscountcal = (parseFloat(orderdiscount)/100)*parseFloat(percentage);
			$("input[name ='orderdiscount']").val(parseFloat((orderdiscountcal*cequ)).toFixed(numberOfDecimal));
			//$("#oddiscount").attr("readonly", false);
		}
	}

	if(isoddisctax == "1"){
		 $('input[name ="odiscounttaxstatus"]').val("1");
		 $("#odtax").html("");
		 $("#odtax").html("(Tax Inclusive)");
		 $('input[name ="isodisctax"]').prop('checked', true);
	 }else{
		 $('input[name ="odiscounttaxstatus"]').val("0");
		 $("#odtax").html("");
		 $("#odtax").html("(Tax Exclusive)");
		 $('input[name ="isodisctax"]').prop('checked', false);
	 }
	
	$('input[name ="isodisctax"]').attr("disabled", true);

	
	
	if(parseFloat(billdiscount) > 0){
		
		if(billdiscounttype == "%"){
			$("input[name ='discount']").val(parseFloat(billdiscount).toFixed(3));
			$("select[name ='discount_type']").val(billdiscounttype);
		}else{
			billdiscount = (parseFloat(billdiscount)/100)*parseFloat(percentage);
			$("select[name ='discount_type']").val(billdiscounttype);
			$("input[name ='discount']").val(parseFloat((billdiscount*cequ)).toFixed(numberOfDecimal));
		}
	}
	
	$('.discount_type').prop('disabled',true);
	
	if(isdisctax == "1"){
		 $('input[name ="discounttaxstatus"]').val("1");
		 $("#dtax").html("");
		 $("#dtax").html("(Tax Inclusive)");
		 $('input[name ="isdtax"]').prop('checked', true);
	 }else{
		 $('input[name ="discounttaxstatus"]').val("0");
		 $("#dtax").html("");
		 $("#dtax").html("(Tax Exclusive)");
		 $('input[name ="isdtax"]').prop('checked', false);
	 }
	
	$('input[name ="isdtax"]').attr("disabled", true);
	
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
	tooltioedit();
	$('input,textarea').on('keypress', function (e) {
	    var ingnore_key_codes = [39, 91];
	  
	    if ($.inArray(e.which, ingnore_key_codes) >= 0) {
	        e.preventDefault();
			alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	       
	    } else {
	      
	    }
	}).on("paste",function(e){
		var textboxvalue = this;
	   setTimeout(function(){
	    var sValue = $(textboxvalue).val();
	    convertCharToString3(sValue); 
	 },100);
	 
	});
}

/*function changetax(){
	var urlStr = "/track/MasterServlet";
	$.ajax({
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		PLANT : document.form.plant.value,
		ACTION : "GET_GST_TYPE_DATA",
		SALESLOC : document.form.STATE_PREFIX.value
	},
	dataType : "json",
	success : function(data) {
		taxList=[];
		$(".bill-table tbody tr").each(function() {
			var taxing = $('td:eq(6)', this).find('input[name="tax_type"]').val();
			var taxtype=taxing.substr(0, taxing.indexOf('(')); 
			if(taxtype=="" || taxtype==undefined)
				{
					taxtype=taxing;
				}
			var pertax="";
			var SGSTTYPES="0";
			var SGSTPERCENTAGE="0";
			var DISPLAY="0";
			$.each(data.records,function(i,v){
				var cgettaxtype = v.SGSTTYPES;
				var gettaxtype=cgettaxtype.substr(0, cgettaxtype.indexOf('(')); 
				if(gettaxtype == taxtype){	
					pertax = v.DISPLAY;
					SGSTTYPES=v.SGSTTYPES;
					SGSTPERCENTAGE=v.SGSTPERCENTAGE;
					DISPLAY=v.DISPLAY;
				}
			});
			
				$('td:eq(6)', this).find('input[name="tax_type"]').val(pertax);
				$('td:eq(6)', this).find('input[name="tax"]').val(pertax)
				if(SGSTTYPES != "0"){
					var obj1 = $('td:eq(7)', this);
					calculateTax(obj1,SGSTTYPES,SGSTPERCENTAGE,DISPLAY);
				}
		});
		renderTaxDetails();
	}
	});
}*/

function changetaxfordiscountnew(){
	var urlStr = "/track/MasterServlet";
	$.ajax({
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		PLANT : document.form.plant.value,
		ACTION : "GET_GST_TYPE_DATA",
		SALESLOC : document.form.STATE_PREFIX.value
	},
	dataType : "json",
	success : function(data) {
		taxList=[];
		$(".bill-table tbody tr").each(function() {
			var taxtype = $('td:eq(6)', this).find('input[name="tax_type"]').val();
			var SGSTTYPES="0";
			var SGSTPERCENTAGE="0";
			var DISPLAY="0";
			$.each(data.records,function(i,v){
				var cgettaxtype = v.DISPLAY;
				if(cgettaxtype == taxtype){	
					SGSTTYPES=v.SGSTTYPES;
					SGSTPERCENTAGE=v.SGSTPERCENTAGE;
					DISPLAY=v.DISPLAY;
				}
			});
				if(SGSTTYPES != "0"){
					var obj1 = $('td:eq(6)', this);
					calculateTaxState(obj1,SGSTTYPES,SGSTPERCENTAGE,DISPLAY);
				}
		});
		renderTaxDetails();
	}
	});
}

function calculateTaxState(obj, types, percentage, display){
	var originalTaxType= types;
	var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); 
	if(taxtype=="ZERO RATE")
		{
			zerotype++;
		}
	$(obj).closest('td').find('input[name = "tax_type"]').val(display);
	var numberOfDecimal = $("#numberOfDecimal").val();
	var tax = new Object();
	tax.types = types;
	tax.percentage = percentage;
	tax.display = display;
	
	$(obj).closest('td').data('name', types);
	$(obj).closest('td').data('tax', percentage);
	var amount = $(obj).closest('tr').find('input[name = "amount"]').val();
	discountValue = parseFloat(amount*(percentage/100)).toFixed(numberOfDecimal);
	tax.value = discountValue;
	$('input[name ="taxpercentage"]').val(percentage);
	if(taxList.length == 0){
		taxList.push(tax);	
	}else{
		var match = false;
		$.each(taxList, function( key, data ) {
			if(data.types == types){
				data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
				match = true;
			}
		});
		if(!match){
			taxList.push(tax);
		}
	}
	renderTaxDetails();
}

function changetaxnew(){
	
	var urlStr = "/track/MasterServlet";
	$.ajax({
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		PLANT : document.form.plant.value,
		ACTION : "GET_GST_TYPE_DATA",
		SALESLOC : document.form.STATE_PREFIX.value
	},
	dataType : "json",
	success : function(data) {
		taxList=[];
		$(".bill-table tbody tr").each(function() {
			var taxing = $('td:eq(6)', this).find('input[name="tax_type"]').val();
			var taxtype=taxing.substr(0, taxing.indexOf('(')); 
			if(taxtype=="" || taxtype==undefined)
				{
					taxtype=taxing;
				}
			var pertax="";
			var SGSTTYPES="0";
			var SGSTPERCENTAGE="0";
			var DISPLAY="0";
			$.each(data.records,function(i,v){
				var cgettaxtype = v.SGSTTYPES;
				var gettaxtype=cgettaxtype.substr(0, cgettaxtype.indexOf('(')); 
				if(gettaxtype == taxtype){	
					pertax = v.DISPLAY;
					SGSTTYPES=v.SGSTTYPES;
					SGSTPERCENTAGE=v.SGSTPERCENTAGE;
					DISPLAY=v.DISPLAY;
				}
			});
			
				$('td:eq(6)', this).find('input[name="tax_type"]').val(pertax);
				$('td:eq(6)', this).find('input[name="tax"]').val(pertax)
				if(SGSTTYPES != "0"){
					var obj1 = $('td:eq(6)', this);
					calculateTaxState(obj1,SGSTTYPES,SGSTPERCENTAGE,DISPLAY);
				}
		});
		renderTaxDetails();
		
	}
	});
}

function setCURRENCYUSEQT(CURRENCYUSEQTCost){
var numberOfDecimal = $("#numberOfDecimal").val();
var oldeqtcost = $('input[name ="CURRENCYUSEQTOLD"]').val();
var CURRENCYID = $('input[name ="CURRENCYID"]').val();
var CURRENCY = $('input[name ="CURRENCY"]').val();
var amount ="0";
var cost = "0";

$('.bill-table tbody tr').each(function () {
	cost = ((parseFloat($(this).find('input[name=cost]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
	cost = parseFloat(cost).toFixed(numberOfDecimal);
	$(this).find('input[name=cost]').val(cost);
	amount = ((parseFloat($(this).find('input[name=amount]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
	amount = parseFloat(amount).toFixed(numberOfDecimal);
	$(this).find('input[name=amount]').val(amount);
});



var orderdiscount= $("input[name='orderdiscount']").val();
var orderdiscounttype= $("select[name='oddiscount_type']").val();
if(orderdiscounttype == "%"){
	$("select[name='oddiscount_type']").empty();
	$("select[name='oddiscount_type']").append('<option selected value="%">%</option>');
	orderdiscount = checkno(orderdiscount);
	orderdiscount = parseFloat(orderdiscount).toFixed(3);
	$("input[name ='orderdiscount']").val(orderdiscount);
}else{
	$("select[name='oddiscount_type']").empty();
	$("select[name='oddiscount_type']").append('<option selected value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
	orderdiscount = checkno(orderdiscount);
	orderdiscount = ((parseFloat(orderdiscount))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
	orderdiscount = parseFloat(orderdiscount).toFixed(numberOfDecimal);
	$("input[name ='orderdiscount']").val(orderdiscount);
}
$("input[name ='aorderdiscount']").val(orderdiscount);

var discount= $("input[name='discount']").val();
var odiscounttype= $("select[name='discount_type']").val();
if(odiscounttype == "%"){
	$("select[name='discount_type']").empty();
	$("select[name='discount_type']").append('<option value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
	$("select[name='discount_type']").append('<option selected value="%">%</option>');
	discount = checkno(discount);
	discount = parseFloat(discount).toFixed(3);
	$("input[name ='discount']").val(discount);
}else{
	$("select[name='discount_type']").empty();
	$("select[name='discount_type']").append('<option selected value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
	$("select[name='discount_type']").append('<option value="%">%</option>');
	discount = checkno(discount);
	discount = ((parseFloat(discount))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
	discount = parseFloat(discount).toFixed(numberOfDecimal);
	$("input[name ='discount']").val(discount);
}


var adjustment= $("input[name ='adjustment']").val();
adjustment = checkno(adjustment);
adjustment = ((parseFloat(adjustment))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
adjustment = parseFloat(adjustment).toFixed(numberOfDecimal);
$("input[name ='adjustment']").val(adjustment);

$("input[name ='CURRENCYUSEQTOLD']").val(CURRENCYUSEQTCost);

calculateTotal();
}

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
			return zeroval;
		}
	}else{
		return zeroval;
	}
}


function calculateTotal(){
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = 0, discountValue=0, shippingvalue=0, adjustmentvalue=0,totalvalue=0,orderdisc=0;
	var ptaxstatus = $("#item_rates").val();
	var ptaxiszero = $("input[name='ptaxiszero']").val();
	var ptaxisshow = $("input[name='ptaxisshow']").val();
	var ptaxpercentage = $("input[name='ptaxpercentage']").val();
	var ptaxdisplay = $("input[name='ptaxdisplay']").val();
	
	var discount= $("input[name='discount']").val();
	var odiscounttype= $("select[name='discount_type']").val();
	if(odiscounttype == "%"){
		discount = checkno(discount);
		discount = parseFloat(discount).toFixed(3);
		$("input[name ='discount']").val(discount);
	}else{
		discount = checkno(discount);
		discount = parseFloat(discount).toFixed(numberOfDecimal);
		$("input[name ='discount']").val(discount);
	}
	
	var orderdisc= $("input[name='orderdiscount']").val();
	var orderdisctype= $("select[name='oddiscount_type']").val();
	if(orderdisctype == "%"){
		orderdisc = checkno(orderdisc);
		orderdisc = parseFloat(orderdisc).toFixed(3);
		$("input[name ='orderdiscount']").val(orderdisc);
	}else{
		orderdisc = checkno(orderdisc);
		orderdisc = parseFloat(orderdisc).toFixed(numberOfDecimal);
		$("input[name ='orderdiscount']").val(orderdisc);
	}
	
	var adjustment= $("input[name ='adjustment']").val();
	adjustment = checkno(adjustment);
	adjustment = parseFloat(adjustment).toFixed(numberOfDecimal);
	$("input[name ='adjustment']").val(adjustment);
	
	var subtotalamount = "0";
	$(".bill-table tbody tr td:last-child").each(function() {
		var kk = $(this).find("input[name ='amount']").val();
		if($(this).find("input[name ='amount']").attr("disabled") != "disabled"){
		    amount =  parseFloat(amount) + parseFloat($(this).find("input[name ='amount']").val());
		    amount = parseFloat(amount).toFixed(numberOfDecimal);
		    subtotalamount = amount;
		}
	});
	var subamount = amount;
	if(ptaxstatus == "0"){
		$("#subTotal").html(subtotalamount);
	    $('input[name ="sub_total"]').val(subtotalamount);// hidden input
	}else{
		if(ptaxiszero == "0" && ptaxisshow == "0"){
			$("#subTotal").html(subtotalamount);
		    $('input[name ="sub_total"]').val(subtotalamount);// hidden input
		}else if(ptaxiszero == "0" && ptaxisshow == "1"){
			
			var taxsubtotal = parseFloat((parseFloat("100")*parseFloat(subtotalamount))/(parseFloat("100")+parseFloat(ptaxpercentage))).toFixed(numberOfDecimal);
			$("#subTotal").html(taxsubtotal);
		    $('input[name ="sub_total"]').val(taxsubtotal);// hidden input
		    amount = taxsubtotal;
		    subtotalamount = taxsubtotal;
		}else if(ptaxiszero == "1" && ptaxisshow == "0"){
			$("#subTotal").html(subtotalamount);
		    $('input[name ="sub_total"]').val(subtotalamount);// hidden input
		}else{
			$("#subTotal").html(subtotalamount);
		    $('input[name ="sub_total"]').val(subtotalamount);// hidden input
		}
	}
	 
	 

	 var discount = $('input[name ="discount"]').val();
	 discount = (discount == "") ? 0.00 : parseFloat(discount).toFixed(numberOfDecimal);
	 var discounType = $('select[name ="discount_type"]').val();
	 if(discounType == "%"){
		 discountValue = parseFloat(subtotalamount*(discount/100)).toFixed(numberOfDecimal);
	 }else{
		 discountValue = discount;
	 }
	 
	 if(parseFloat(subtotalamount) >= parseFloat(discountValue)){
		 if(discounType == "%"){
			 $('input[name ="discount"]').val(parseFloat(discount).toFixed(3));
			 $("#discount").html(parseFloat(-discountValue).toFixed(numberOfDecimal));
		 }else{
			 $("#discount").html(parseFloat(-discountValue).toFixed(numberOfDecimal));
		 }
	 }else{
		 discountValue = "0";
		 if(discounType == "%"){
			 $('input[name ="discount"]').val(parseFloat("0").toFixed(3));
		 }else{
			 $('input[name ="discount"]').val(parseFloat("0").toFixed(numberOfDecimal));
		 }
		 $("#discount").html(parseFloat(-discountValue).toFixed(numberOfDecimal));
	 }
	 
	 
	 
	 var orddiscountValue = "0";
	 var orddiscount = $('input[name ="orderdiscount"]').val();
	 orddiscount = (orddiscount == "") ? 0.00 : parseFloat(orddiscount).toFixed(numberOfDecimal);
	 var orddiscounType = $('select[name ="oddiscount_type"]').val();
	 if(orddiscounType == "%"){
		 orddiscountValue = parseFloat(subtotalamount*(orddiscount/100)).toFixed(numberOfDecimal);
	 }else{
		 orddiscountValue = orddiscount;
	 }
	 
	 if(parseFloat(subtotalamount) >= parseFloat(orddiscountValue)){
		 if(orddiscounType == "%"){
			 $('input[name ="orderdiscount"]').val(parseFloat(orddiscount).toFixed(3));
			 $("#orderdiscount").html(parseFloat(-orddiscountValue).toFixed(numberOfDecimal));
		 }else{
			 $("#orderdiscount").html(parseFloat(-orddiscountValue).toFixed(numberOfDecimal));
		 }
	 }else{
		 orddiscountValue = "0";
		 if(orddiscounType == "%"){
			 $('input[name ="orderdiscount"]').val(parseFloat("0").toFixed(3));
		 }else{
			 $('input[name ="orderdiscount"]').val(parseFloat("0").toFixed(numberOfDecimal));
		 }
		 $("#orderdiscount").html(parseFloat(-orddiscountValue).toFixed(numberOfDecimal));
	 }
	 
	 
	 
	 var adjustmentvalue = $('input[name ="adjustment"]').val();
	 adjustmentvalue = (adjustmentvalue == "") ? 0.00 : adjustmentvalue;
	 
	 
	 var taxTotal = "0";
	 var odisctaxstatus = $("input[name='odiscounttaxstatus']").val();
	 var disctaxstatus = $("input[name='discounttaxstatus']").val();
	 
	 if(ptaxstatus == "0"){
			if(ptaxisshow == "1"){
				taxTotal = parseFloat(taxTotal) + parseFloat((subtotalamount/100)*ptaxpercentage);
				
				if(odisctaxstatus == "1"){
					taxTotal = parseFloat(taxTotal) - parseFloat((orddiscountValue/100)*ptaxpercentage);
				}
				
				if(disctaxstatus == "1"){
					taxTotal = parseFloat(taxTotal) - parseFloat((discountValue/100)*ptaxpercentage);
				}
			}
	 }else{
			if(ptaxiszero == "0" && ptaxisshow == "1"){
				/*var taxsubtotal = parseFloat((parseFloat("100")*parseFloat(subamount))/(parseFloat("100")+parseFloat(ptaxpercentage))).toFixed(numberOfDecimal);
				
				taxTotal = parseFloat(taxTotal) + parseFloat(subamount - taxsubtotal);*/
				taxTotal = parseFloat(taxTotal) + parseFloat((subtotalamount/100)*ptaxpercentage);
				
				if(odisctaxstatus == "1"){
					taxTotal = parseFloat(taxTotal) - parseFloat((orddiscountValue/100)*ptaxpercentage);
				}
				
				if(disctaxstatus == "1"){
					taxTotal = parseFloat(taxTotal) - parseFloat((discountValue/100)*ptaxpercentage);
				}
				
			}
	}
	 
	 if(ptaxisshow == "1"){
		var html ="";
		html+='<div class="total-row">';
		html+='<div class="total-label">'+ptaxdisplay+'</div>';
		html+='<div class="total-amount taxAmount">'+parseFloat(parseFloat(taxTotal)).toFixed(numberOfDecimal)+'</div>';
		html+='</div>';
		$(".taxDetails").html(html);
		($("#item_rates").val() == 0) ? $(".productRate").hide() : $(".productRate").show(); 
		//$('input[name ="taxamount"]').val(taxTotal);
		$('input[name = "taxTotal"]').val(taxTotal)
	}else{
		$(".taxDetails").html("");
		//$('input[name ="taxamount"]').val("0");
		$('input[name = "taxTotal"]').val("0");
	}
	 
	 
	 var pretotal = parseFloat(parseFloat(amount) - parseFloat(discountValue) - parseFloat(orddiscountValue) + parseFloat(taxTotal)).toFixed(numberOfDecimal);
	 
	 if(parseFloat(pretotal) >= parseFloat(adjustmentvalue)){
		 $("#adjustment").html(parseFloat(adjustmentvalue).toFixed(numberOfDecimal));
		 $('input[name ="adjustment"]').val(parseFloat(adjustmentvalue).toFixed(numberOfDecimal));
	 }else{
		 alert("adjustment should be less than "+pretotal);
		 adjustmentvalue = "0";
		 $("#adjustment").html(parseFloat("0").toFixed(numberOfDecimal));
		 $('input[name ="adjustment"]').val(parseFloat("0").toFixed(numberOfDecimal));
	 }
	 
	 totalvalue = parseFloat(parseFloat(amount)- parseFloat(orddiscountValue) - parseFloat(discountValue) + parseFloat(taxTotal) + parseFloat(adjustmentvalue)).toFixed(numberOfDecimal);
	 $("#total").html(totalvalue);
	 
	 $("#total_amount").val(totalvalue); // hidden input
	 
	 var CURRENCYUSEQT = $('input[name ="CURRENCYUSEQT"]').val();//Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency
	 var convttotalvalue= (totalvalue/CURRENCYUSEQT).toFixed(numberOfDecimal)
	 $("#totalcur").html(convttotalvalue);
}

function calculateTaxPO(obj, types, percentage, display, iszero, isshow, id){
	$("input[name=ptaxtype]").val(types);
	$("input[name=ptaxpercentage]").val(percentage);
	$("input[name=ptaxdisplay]").val(display);
	$("input[name=ptaxiszero]").val(iszero);
	$("input[name=ptaxisshow]").val(isshow);
	$("input[name=taxid]").val(id);
	$('.taxSearch').each(function(){
	    $(this).val(display);
	});
	calculateTotal();
}

function getshipaddr(){
		SHIP_OnCountry($("input[name=SHIPCOUNTRY]").val());
		$("input[name=SHIP_CONTACTNAME]").val($("input[name=SHIPCONTACTNAME]").val());
		$("input[name=SHIP_DESGINATION]").val($("input[name=SHIPDESGINATION]").val());
		$("input[name=SHIP_ADDR1]").val($("input[name=SHIPADDR1]").val());
		$("input[name=SHIP_ADDR2]").val($("input[name=SHIPADDR2]").val());
		$("input[name=SHIP_ADDR3]").val($("input[name=SHIPADDR3]").val());
		$("input[name=SHIP_ADDR4]").val($("input[name=SHIPADDR4]").val());		
		$("select[name=SHIP_COUNTRY_CODE]").val($("input[name=SHIPCOUNTRY]").val());		
		$("input[name=SHIP_ZIP]").val($("input[name=SHIPZIP]").val());
		$("input[name=SHIP_WORKPHONE]").val($("input[name=SHIPWORKPHONE]").val());
		$("input[name=SHIP_HPNO]").val($("input[name=SHIPHPNO]").val());
		$("input[name=SHIP_EMAIL]").val($("input[name=SHIPEMAIL]").val());
		$("select[name=SHIP_STATE]").val($("input[name=SHIPSTATE]").val());
		}
