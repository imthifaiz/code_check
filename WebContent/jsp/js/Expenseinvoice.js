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
		    "year": "PAID",
		    "value": "PAID",
		    "tokens": [
		      "PAID"
		    ]
		  },
		  {
			    "year": "PARTIALLY PAID",
			    "value": "PARTIALLY PAID",
			    "tokens": [
			      "PARTIALLY PAID"
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
	var plant = document.form1.plant.value;
	var cmd = document.form1.cmd.value;
	var TranId = document.form1.TranId.value;
	document.form1.STATE_PREFIX.value="AUH";
	if(cmd=="Edit")
	{
	if(TranId!="")
		{
		getEditInvoiceDetail(TranId);
		}
	}else if(cmd=="insert"){
		if(TranId!="")
		{
		getexpenseInvoiceDetail(TranId);
		}
	}
	
	var curencyval = document.form1.curency.value;
	var states =   [{
	    "year": curencyval,
	    "value": curencyval,
	    "tokens": [
	    	curencyval
	    ]
	  },
	  {
	    "year": "%",
	    "value": "%",
	    "tokens": [
	      "%"
	    ]
	  }];	
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	$('input[name ="cost"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	$('input[name ="item_discount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	$('input[name ="amount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	$("#subTotal").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#discount").html(parseFloat("0.00000").toFixed(numberOfDecimal));	
	$("#adjustment").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#total").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	/* Customer Auto Suggestion */
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'CNAME',  
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
						ACTION : "GET_CUSTOMER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CUSTMST);
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
		    	return '<p onclick="getcustname(\''+data.TAXTREATMENT+'\',\''+data.CUSTNO+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYUSEQT+'\')">' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.customerAddBtn').remove();  
			$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');
			$(".customerAddBtn").width($(".tt-menu").width());
			$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.customerAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.customerAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.CUST_CODE.value = "";
				document.form1.nTAXTREATMENT.value ="";
				document.getElementById('nTAXTREATMENT').innerHTML="";
				$("input[name ='TAXTREATMENT_VALUE']").val("");
			}
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			$('#nTAXTREATMENT').attr('disabled',false);
		}).on('typeahead:select',function(event,selection){
			$("#project").typeahead('val', '"');
			$("#project").typeahead('val', '');
			$("input[name=PROJECTID]").val('');
		});
	
	//transport
	$('#transport').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TRANSPORT_MODE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
						ACTION : "GET_TRANSPORT_LIST",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.TRANSPORTMODE);
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
		    return '<p>' + data.TRANSPORT_MODE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="transportAddBtn footer"  data-toggle="modal" data-target="#transportModal"><a href="#"> + New Transport </a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.transportAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.transportAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			$("input[name=TRANSPORTID]").val(selection.ID);
		});
	
	
	/* Shipping Customer Auto Suggestion */
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
						CUSTNO : $("input[name=CUST_CODE]").val(),
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
	
	$("#Salestax").typeahead({
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
					TAXKEY : "OUTBOUND",
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
						PLANT : "<%=plant%>",
						ACTION : "GET_LOCATION_BY_COUNTRY",
						COUNTRYCODE : $("input[name=COUNTRYCODE]").val(),
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
		    	return '<p onclick="document.form1.STATE_PREFIX.value = \''+data.STATE_PREFIX+'\'">' + data.STATE + '</p>';		    
			}
		  }
		}).on('typeahead:render',function(event,selection){
			
		  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			removetaxdropdown();
			addtaxdropdown();
			taxreset();
		});

	/* Order Number Auto Suggestion */
	$('#ORDERNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DONO',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/InvoiceServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
						CNAME : document.form1.CUSTOMER.value,
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
		    return '<p>' + data.DONO + '</p>';
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
			getOrderDetailsForInvoice(selection.DONO);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.ORDERNO.value = "";
			}
			/* To reset Order number Autosuggestion*/
			$("#invoice").typeahead('val', '"');
			$("#invoice").typeahead('val', '');
			removeSuggestionToTable();
			
		});
	

	/* Order Number Auto Suggestion */
	`$('#invoice').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'INVOICE',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/InvoiceServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,						
						Submit : "GET_INVOICE_DETAILS_FOR_ORDERNO",
						DONO : document.form1.ORDERNO.value,
						INVOICENO : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.invoices);
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
		    return '<p>' + data.INVOICE + '</p>';
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
			addSuggestionToTable(selection.INVOICE);
		});`
	
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
		    	return '<p onclick="document.form1.EMPNO.value = \''+data.EMPNO+'\'">' + data.FNAME + '</p>';		    
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
	
		/*}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.employeeAddBtn').remove();  
			$('.employee-section .tt-menu').after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#employeeModal"><a href="#"> + New Employee</a></div>');
			$(".employeeAddBtn").width($(".tt-menu").width());
			$(".employeeAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.employeeAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.employeeAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.EMPNO.value = "";
			}
		});*/
	
	/* Product Number Auto Suggestion */
	$('#item').typeahead({
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
			return '<div><p class="item-suggestion">'+data.ITEM+'</p></div>';
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
			setTimeout(function(){ menuElement.next().hide();}, 150);
		});
	
	/* To get the suggestion data for Discount Account */
	$(".discountAccountSearch").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'account_name',  
	  /*source: substringMatcher(states),*/
	  source: function (query, process,asyncProcess) {
		  	var urlStr = "/track/ChartOfAccountServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "getDataForSuggestion",
				NAME : query
			},
			dataType : "json",
			success : function(data) {

				return asyncProcess(data.accounts);
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
		return '<p>' + data.account_type + ' - ' + data.account_name + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>');
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
	
	/* To get the suggestion data for Discount Type */
	$(".item_discounttypeSearch").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'states',
	  display: 'value',  
	  source: substringMatcher(states),
	  limit: 9999,
	  templates: {
	  empty: [
		  '<div style="padding:3px 20px">',
			'No results found',
		  '</div>',
		].join('\n'),
		suggestion: function(data) {
		return '<p>' + data.value  + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}		
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
	
	$(".bill-table tbody").on('click','.bill-action',function(){
		debugger;	    
	    var obj = $(this).closest('tr').find('td:nth-child(6)');
	    calculateTax(obj, "", "", "");
	    $(this).parent().parent().remove();
	});
	
	$("#btnBillDraft").click(function(){
		$('input[name ="invoice_status"]').val('Draft');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		$("#createBillForm").submit();
	});
	$("#btnBillOpen").click(function(){
		$('input[name ="invoice_status"]').val('Open');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		$("#createBillForm").submit();
	});
	
		$("#btnSaveRemarks").click(function(){
		var data = $("#remarksForm").serialize();
		$.ajax({
			type : "POST",
			url : "../invoice/addRemarks",
			data : data,
			dataType : "json",
			success : function(data) {
				if(data.ERROR_CODE == 100)
					$("#remarksModal").modal('hide');
	        	else
	        		alert(data.MESSAGE);
			}
		});
	});
	
	$("#btnBillOpenEmail").click(function(){
		if ($('#supplier_email').val() == ''){
			alert('Supplier record does not have an email address. Please update supplier email and try again or Save as Draft OR Save as Open');
			return;
		}
		$('input[name ="invoice_status"]').val('Open');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		var isValid = validateBill();
		if (!isValid){
			return;
		}
		var data = new FormData($("#createBillForm")[0]); //$("#purchaseOrderForm").serialize();
			$.ajax({
				type : "POST",
				url : "../billing/save",
				data : data,
				dataType : "json",
				contentType: false,
				processData: false,
				beforeSend: function(){
					showLoader();
				},
				complete: function(){
					hideLoader();
				},
				success : function(data) {
					if(data.ERROR_CODE == 100){
						$('.success-msg').html(data.MESSAGE).css('display', 'inline');
						$('#common_email_modal').modal('toggle');
						$('#send_to').val($('#CUSTOMEREMAIL').val()).multiEmail();
						$('#send_subject').val($('#template_subject').val()
												.replace(/\{COMPANY_NAME\}/, $('#plant_desc').val())
												.replace(/\{INVOICE_NO\}/, $('#invoice').val())
												);
						$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(
									$('#template_body').val()
									.replace(/\{INVOICE_NO\}/, $('#invoice').val())
									.replace(/\{CUSTOMER_NAME\}/, $('#CUSTOMER').val())
									);
						$('#send_attachment').val('Invoice');
					}else if ($('.success-msg').length > 0){
						$('.success-msg').html(data.MESSAGE).addClass('error-msg').removeClass('success-msg').css('display', 'inline');
					}else if ($('.error-msg').length > 0){
						$('.error-msg').html(data.MESSAGE).css('display', 'inline');
					}
		        		
				}
			});
			});
	
	$(document).on("focusout",".taxSearch",function(){
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

	if($("#ORDERNO").val() !== ""){
		getOrderDetailsForInvoice($("#ORDERNO").val());
		$("input[name ='CUSTOMER']").typeahead('val', $("#ORDERNO").val());
		$(".add-line").hide();
		$("#item_rates").attr("readonly", true);
	}
	
	/* Invoice No Auto Suggestion */
	$('#auto_invoiceNo').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'INVOICE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvoiceServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_BILL_NO_FOR_AUTO_SUGGESTION",
				ORDERNO : document.form1.ORDERNO.value,
				CNAME : document.form1.CUSTOMER.value,
				INVOICE : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.InvoiceDetails);
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
		    return '<p>' + data.INVOICE + '</p>';
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
		});
	
	$("#remarks-table tbody").on('click','.remark-action',function(){
    $(this).parent().parent().remove();
	});
	
	/* To get the suggestion data for Currency */
	$("#currency").typeahead({
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
			return '<div><p onclick="getCurrencyid(\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
	
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			$("input[name ='CURRENCYID']").val("");	
	});
	
	$(document).on("focusout","input[name ='CURRENCYUSEQT']",function(){
		var CURRENCYUSEQTCost = parseFloat($("input[name ='CURRENCYUSEQT']").val());
		if(!Number.isNaN(CURRENCYUSEQTCost))
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});
	
	addSuggestionToTable();
});


function addRow(){
	var curency = document.form1.curency.value;
	var body="";
	body += '<tr>';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">';
	body += '<input type="hidden" name="IS_COGS_SET" value="N">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" class="form-control itemSearch" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '</td>';
	body += '<td class="">';
	body += '<input type="text" name="account_name" value="Local sales - retail" class="form-control accountSearch" placeholder="Account">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)"></td>';
	body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="0.00" onchange="calculateAmount(this)"></td>';
	body += '<td class="item-discount text-right">';
	body += '<div class="row">';							
	body += '<div class=" col-lg-12 col-sm-3 col-12">';
	body += '<div class="input-group my-group" style="width:120px;">';
	body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)">';
	//body += '<textarea  rows="1" maxlength="3" style="resize: none;padding: 5px;" class="discountPicker form-control item_discounttypeSearch" name="item_discounttype" onchange="calculateAmount(this)"></textarea>';
	//body += "<input type=\"text\" class=\"discountPicker form-control item_discounttypeSearch\" id=\"item_discounttype\" value="+curency+" name=\"item_discounttype\" onchange=\"calculateAmount(this)\">";
	body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
	body += "<option value="+curency+">"+curency+"</option>";
	body += '<option>%</option>';										
	body += '</select>';
	body += '</div>';
	body += '</div>'; 
	body += '</div>';
	body += '</td>';
	body += '<td class="item-tax">';
	body += '<input type="hidden" name="tax_type">';
	body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" readonly>';
	body += '</td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += '<input name="amount" type="text" class="form-control text-right" value="0.00" readonly="readonly" style="display:inline-block;"></td>';
	body += '</tr>';
	$(".bill-table tbody").append(body);
	removeSuggestionToTable();
	addSuggestionToTable();
}

//jsp validation 
function checkorderno(orderno){	
		var urlStr = "../goodsissued/CheckOrderno";
		$.ajax( {
			type : "GET",
			url : urlStr,
			async : true,
			data : {
				ORDERNO : orderno,
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						alert("Order Number Already Exists");
						document.getElementById("invoice").focus();
						document.getElementById("invoice").value="";
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkpaymentterms(payterms){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PAYMENT_TERMS : payterms,
				ACTION : "PAYTERMS_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Payment Terms Does't Exists");
						document.getElementById("payment_terms").focus();
						$("#payment_terms").typeahead('val', '');
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
						document.getElementById("Salestax").focus();
						$("#Salestax").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
//jsp validation ends
function addSuggestionToTable(){
	var plant = document.form1.plant.value;
	
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
			return '<div onclick="loadItemData(this,\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' '+data.UOM+'</p></div>';
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
			setTimeout(function(){ menuElement.next().hide();}, 150);
		});	
	
	/* To get the suggestion data for Tax */
	/*$(".taxSearch").typeahead({
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
				ACTION : "GET_GST_TYPE_DATA_SALES",
				SALESLOC : document.form1.STATE_PREFIX.value,
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
	});	*/
	
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
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/ChartOfAccountServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					action : "getSubAccountTypeGroup",
					module:"salescreditaccount",
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
			menuElement.after( '<div class="expenseaccountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
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
}

function removeSuggestionToTable(){
	$(".itemSearch").typeahead('destroy');
	$(".taxSearch").typeahead('destroy');
	//$(".item_discounttypeSearch").typeahead('destroy');
}

function loadItemData(obj, catalogPath, account, cost){
	
	
	
	
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("td:nth-child(4)").find('input').val(cost);
	calculateAmount(obj);
}

function calculateAmount(obj){
	/*var numberOfDecimal = $("#numberOfDecimal").val();
	var qty = parseFloat($(obj).closest('tr').find("td:nth-child(3)").find('input').val()).toFixed(3);
	var cost = parseFloat($(obj).closest('tr').find("td:nth-child(4)").find('input').val()).toFixed(numberOfDecimal);
	var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(5)").find('input').val()).toFixed(numberOfDecimal);
	var discounType = $(obj).closest('tr').find("td:nth-child(5)").find('select').val();
	
	var itemDiscountval=itemDiscount;
	 if(discounType == "%"){
		 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
	 }
	 
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
	
	$(obj).closest('tr').find("td:nth-child(3)").find('input').val(qty);
	$(obj).closest('tr').find("td:nth-child(4)").find('input').val(cost);
	//$(obj).closest('tr').find("td:nth-child(5)").find('input').val(itemDiscount);
	$(obj).closest('tr').find("td:nth-child(7)").find('input').val(amount);
	calculateTotal();*/
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	var state = document.form1.STATE_PREFIX.value;
	var plant = document.form1.plant.value;
	
	var qty = parseFloat($(obj).closest('tr').find("td:nth-child(4)").find('input').val()).toFixed(3);
	var cost = parseFloat($(obj).closest('tr').find("td:nth-child(5)").find('input').val()).toFixed(numberOfDecimal);
	var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(6)").find('input').val()).toFixed(numberOfDecimal);
	var discounType = $(obj).closest('tr').find("td:nth-child(6)").find('select').val();
	var itemDiscountval=itemDiscount;
	 if(discounType == "%"){
		 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
		 itemDiscount = parseFloat(itemDiscount).toFixed(3);
	 }
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
	
	if(parseFloat(amount) >= parseFloat("0")){
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
	
	//changetaxfordiscountnew();
	calculateTotal();
}

function calculateTotal(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = 0, discountValue=0, adjustmentvalue=0,totalvalue=0;
	
	$(".bill-table tbody tr td:last-child").prev().each(function() {
	    amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
	    amount = parseFloat(amount).toFixed(numberOfDecimal);
	    $("#subTotal").html(amount);
	    $('input[name ="sub_total"]').val(amount);// hidden input
	});
	
	var shippingcost= document.form1.shippingcost.value;
	shippingcost = parseFloat(shippingcost).toFixed(numberOfDecimal);
	$("input[name ='shippingcost']").val(shippingcost);
	 
	 var taxTotal = 0;
	/* if($(".taxDetails").html().length > 0 && ($("#item_rates").val() == 0)){
		 $('.taxAmount').each(function(i, obj) {
			 taxTotal += parseFloat($(this).html());
		 });
	 }*/
	 	var ptaxstatus = $("#item_rates").val();
		var ptaxiszero = $("input[name='ptaxiszero']").val();
		var ptaxisshow = $("input[name='ptaxisshow']").val();
		var ptaxpercentage = $("input[name='ptaxpercentage']").val();
		var ptaxdisplay = $("input[name='ptaxdisplay']").val();
		
	 
	 if(ptaxstatus != "0"){
		    if(ptaxiszero == "0" && ptaxisshow == "1"){
				var taxsubtotal = parseFloat((parseFloat("100")*parseFloat(amount))/(parseFloat("100")+parseFloat(ptaxpercentage))).toFixed(numberOfDecimal);
				$("#subTotal").html(taxsubtotal);
			    $('input[name ="sub_total"]').val(taxsubtotal);// hidden input
			    amount = taxsubtotal;
			}
		}
	 
	 if(ptaxstatus == "0"){
			if(ptaxisshow == "1"){
				taxTotal = parseFloat(taxTotal) + parseFloat((amount/100)*ptaxpercentage);
			}
	 }else{
			if(ptaxiszero == "0" && ptaxisshow == "1"){
				taxTotal = parseFloat(taxTotal) + parseFloat((amount/100)*ptaxpercentage);
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
		$('input[name ="taxamount"]').val(taxTotal);
		$('input[name = "taxTotal"]').val(taxTotal)
	}else{
		$(".taxDetails").html("");
		$('input[name ="taxamount"]').val("0");
		$('input[name = "taxTotal"]').val("0");
	}
	 
	 
	 var shippingvalue = $('input[name ="shippingcost"]').val();
	 shippingvalue = (shippingvalue == "") ? 0.00 : shippingvalue;
	 $("#shipping").html(parseFloat(shippingvalue).toFixed(numberOfDecimal));
	 
	 totalvalue = parseFloat(parseFloat(amount) + parseFloat(taxTotal) + parseFloat(shippingvalue)).toFixed(numberOfDecimal);
	 $("#total").html(totalvalue);
	 $("#total_amount").val(totalvalue); // hidden input
	 
	 var CURRENCYUSEQT = $('input[name ="CURRENCYUSEQT"]').val();//Author: Azees  Create date: July 17,2021  Description:  Total of Local Currency
	 var convttotalvalue= (totalvalue/CURRENCYUSEQT).toFixed(numberOfDecimal)
	 $("#totalcur").html(convttotalvalue);
}

function calculateTax(obj, types, percentage, display){
	if(types=="ZERO RATE")
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
				if(name=="ZERO RATE")
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
	//changetaxfordiscountnew();



	//var originalTaxType= types;
	//var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); 


}

function renderTaxDetails(){
	var html="";
	var taxTotal = 0;
	var numberOfDecimal = $("#numberOfDecimal").val();
	var cmd = $("input[name ='cmd']").val();
	
	$.each(taxList, function( key, data ) {
		percentageget=data.percentage;
		console.log("Tax dta:"+data);
		console.log(data.types);
		var originalTaxType= data.types;
		var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); 
		if(taxtype == ""){
			taxtype = originalTaxType;
		}
		console.log("taxtype-------"+taxtype);
		console.log("zerotype-------"+zerotype);
		var shipingcost = document.form1.shippingcost.value;
		var shipingtax = parseFloat(shipingcost*(percentageget/100)).toFixed(numberOfDecimal);
		if(cmd == "Edit"){
			if(data.value > 0 || taxtype=="ZERO RATE"){
				html+='<div class="total-row">';
				html+='<div class="total-label">'+data.display+'</div>';
				html+='<div class="total-amount taxAmount">'+parseFloat(parseFloat(data.value)+parseFloat(shipingtax)).toFixed(numberOfDecimal)+'</div>';
				html+='</div>';
				taxTotal += parseFloat(data.value);
			}
		}else{
			if(data.value > 0 || taxtype=="ZERO RATE" && zerotype>0){
				html+='<div class="total-row">';
				html+='<div class="total-label">'+data.display+'</div>';
				html+='<div class="total-amount taxAmount">'+parseFloat(parseFloat(data.value)+parseFloat(shipingtax)).toFixed(numberOfDecimal)+'</div>';
				html+='</div>';
				taxTotal += parseFloat(data.value);
			}
		}
	});
	$(".taxDetails").html(html);
	calculateTotal();
	($("#item_rates").val() == 0) ? $(".productRate").hide() : $(".productRate").show(); 
}

function validateInvoice(){
	var supplier = document.form1.CUSTOMER.value;
	var bill = document.form1.invoice.value;
	var billDate = document.form1.invoice_date.value;
	var Currency = document.form1.currency.value;
	var CURRENCYUSEQT = document.form1.CURRENCYUSEQT.value;
	
	var isItemValid = true, isAccValid = true;
	
	if(supplier == ""){
		alert("Please select a Customer.");
		return false;
	}	
	if(bill == ""){
		alert("Please enter the Invoice number.");
		return false;
	}	
	if(billDate == ""){
		alert("Please enter a valid Invoice date.");
		return false;
	}
	if(Currency == ""){
		alert("Please select a Currency.");
		return false;
	}
	if(CURRENCYUSEQT == ""){
		alert("Please enter the Exchange Rate.");
		return false;
	}
	/*if(discount != "" && discountAccount == ""){
		alert("Please choose an account for discount.");
		return false;
	}*/
	
	/*$("input[name ='item']").each(function() {
	    if($(this).val() == ""){	    	
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The Product field cannot be empty.");
		return false;
	}*/
	$("input[name ='account_name']").each(function() {
		if($(this).val() == ""){
	    	isAccValid =  false;
	    }
	});	
	if(!isAccValid){
		alert("The Account field cannot be empty.");
		return false;
	}
	return true;
}

function customerCallback(data){
	if(data.STATUS="SUCCESS"){
		alert(data.MESSAGE);
		$("#CUSTOMER").typeahead('val', data.CUSTOMER);
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

function setCustomerData(customerData){	
	$("input[name ='CUST_CODE']").val(customerData.vendno);
	$("#CUSTOMER").typeahead('val', customerData.vendname);	
	/* To reset Order number Autosuggestion*/
	$("#ORDERNO").typeahead('val', '"');
	$("#ORDERNO").typeahead('val', '');
}

function productCallback(productData){
	if(productData.STATUS="SUCCESS"){
		alert(productData.MESSAGE);
		$("input[name ='item']").typeahead('val', productData.ITEM);
	}
}


function transportCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
	}
}


function payTermsCallback(payTermsData){
	if(payTermsData.STATUS="SUCCESS"){
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
	}
}

function getOrderDetailsForInvoice(dono){
	var plant = document.form1.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/InvoiceServlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_ORDER_DETAILS_FOR_INVOICE",
			DONO : dono
		},
		dataType : "json",
		success : function(data) {
			loadBillTable(data.orders);
		}
	});
}

function loadBillTable(orders){
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '<input type="hidden" name="IS_COGS_SET" value="N">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="">';
		body += '<input type="text" name="account_name" value="Local sales - retail" class="form-control accountSearch" value="">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTYOR+'" readonly="readonly"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITPRICE+'" readonly="readonly"></td>';
		body += '<td class="item-discount text-right">';
		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-tax">';
		body += '<input type="hidden" name="tax_type" value="SALES ['+data.OUTBOUND_GST+'%]" class="form-control">';
		body += '<input type="text" name="tax" class="form-control" value="SALES ['+data.OUTBOUND_GST+'%]" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" readonly="readonly" style="display:inline-block;"></td>';
		
		
		body += '</tr>';

		var tax = new Object();
		var percentage = data.OUTBOUND_GST;
		var numberOfDecimal = $("#numberOfDecimal").val();
		tax.types = "SALES";
		tax.percentage = percentage;
		tax.display = 'SALES ['+data.OUTBOUND_GST+'%]';

		var amount = data.AMOUNT;
		discountValue = parseFloat(amount*(percentage/100)).toFixed(numberOfDecimal);
		tax.value = discountValue;

		if(taxList.length == 0){
			taxList.push(tax);
		}else{
			var match = false;
			$.each(taxList, function( key, data ) {
				if(data.types == "SALES"){
					data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
					match = true;
				}
			});
			if(!match){
				taxList.push(tax);
			}
		}	
		$("input[name ='shippingcost']").val(data.SHIPPINGCOST);		
	});
	$(".bill-table tbody").html(body);
	renderTaxDetails();
	removeSuggestionToTable();
}


function getEditInvoiceDetail(TranId){
	var plant = document.form1.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/InvoiceServlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_EDIT_INVOICE_DETAILS",
			Id : TranId
		},
		dataType : "json",
		success : function(data) {
			loadEditTable(data.orders);
		}
	});
}

function loadEditTable(orders){
	var curency = document.form1.curency.value;
	var NOFO_DEC = $("#NOFO_DEC").val();
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
		
		var uprice= parseFloat((data.UNITPRICE*data.CURRENCYUSEQT)).toFixed(NOFO_DEC);
		var amt= parseFloat((data.AMOUNT*data.CURRENCYUSEQT)).toFixed(NOFO_DEC);
		
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="basecost" value="'+data.UNITPRICE+'">';
		body += '<input type="hidden" name="lnno" value="1">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '<input type="hidden" name="IS_COGS_SET" value="N">';
		body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+data.TAX_TYPE+'">';
		body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.TAX_TYPE+'">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)">';
		body += '<input type="text" name="notesexp" class="form-control" value="'+data.Notesexp+'">';
		body += '<input type="hidden" name="edit_item" class="form-control" value="'+data.ITEM+'">';
		body += '</td>';
		body += '<td class="">';
		body += '<input type="text" name="account_name" class="form-control" value="'+data.ACCOUNT_NAME+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)">';
		body += '<input type="hidden" name="edit_qty" class="form-control" value="'+data.QTY+'">';
		body += '</td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+uprice+'" onchange="calculateAmount(this)"></td>';
		body += '<td class="item-discount text-right">';
		body += '<div class="row"><div class=" col-lg-12 col-sm-3 col-12"><div class="input-group my-group" style="width:120px;">';
		if(data.ITEM_DISCOUNT_TYPE == "%"){
			body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" readonly="readonly">';
		}else{
			body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT*data.CURRENCYUSEQT).toFixed(numberOfDecimal)+'" readonly="readonly">';
		}
		
		body += '<select name="item_discounttype" class="discountPicker form-control" readonly>';
		body += '<option value="'+data.ITEM_DISCOUNT_TYPE+'">'+data.ITEM_DISCOUNT_TYPE+'</option>';					
		body += '</select>';
		body += '</div></div></div>';
		body += '</td>';
		//body += '<td class="item-tax">';
		//body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';
		//body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly>';
		/*if(data.TAX_TYPE == ""){
			body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" readonly>';
		}else{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly="readonly">';
		}
		body += '</td>';*/
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		/*body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';*/
		body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" style="display:inline-block;"></td>';
		body += '<td class="table-icon" style="position:relative;">';
		body += '<a href="#" onclick="showRemarksDetails(this)">';
		body += '<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>';
		body += '</a>';
		body += '</td>';
		body += '</td>';
		body += '</tr>';
		
		setLineNo();
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
		if(document.form1.CUST_CODE2.value==""||document.form1.CUST_CODE2.value==null)
		{
		$("input[name ='CUST_CODE']").val(data.CUST_CODE);
		$("input[name ='CUSTOMER']").val(data.CUSTOMER);		
 		$('select[name ="nTAXTREATMENT"]').val(data.TAXTREATMENT);
		}
		$("input[name ='EMPNO']").val(data.EMPNO);
		$("input[name ='EMP_NAME']").val(data.EMP_NAME);		
		$("input[name ='invoice']").val(data.INVOICE);
		$("input[name ='ORDERNO']").val(data.ORDERNO);
		$("input[name ='invoice_date']").val(data.INVOICE_DATE);
		$("input[name ='due_date']").val(data.DUE_DATE);		
		$("input[name ='payment_terms']").val(data.PAYMENT_TERMS);
		$("textarea[name ='terms']").val(data.TERMSCONDITIONS);
		$("textarea[name ='note']").val(data.NOTE);
		$("select[name ='item_rates']").val(data.ITEM_RATES);
		$("select[name ='SALES_LOC']").val(data.STATE_PREFIX);
		
		$("input[name ='discount']").val(data.DISCOUNT);
		$("select[name ='discount_type']").val(data.DISCOUNT_TYPE);
		$("input[name ='discount_account']").val(data.DISCOUNT_ACCOUNT);
		$("input[name ='adjustment']").val(data.ADJUSTMENT);
		$("input[name ='shippingcost']").val(data.SHIPPINGCOST);	
		$("input[name ='oShippingcost']").val(data.SHIPPINGCOST);
		$("input[name ='CURRENCYID']").val(data.CURRENCYID);
		$("input[name ='currency']").val(data.DISPLAY);
		$("input[name ='CURRENCYUSEQT']").val(data.CURRENCYUSEQT);
		
		$("input[name ='CURRENCYUSEQTOLD']").val(data.CURRENCYUSEQT);
		$("input[name ='PROJECTID']").val(data.PROJECTID);
		$("input[name ='ptaxtype']").val(data.FINTAXTYPE);
		$("input[name ='ptaxpercentage']").val(data.OUTBOUD_GST);
		$("input[name ='ptaxdisplay']").val(data.TAX_TYPE);
		$("input[name ='ptaxiszero']").val(data.FINTAXISZERO);
		$("input[name ='ptaxisshow']").val(data.FINTAXSHOW);
		$("input[name ='taxid']").val(data.TAXID);
		$("input[name ='GST']").val(data.OUTBOUD_GST);
		$("input[name ='project']").val(data.PROJECTNAME);
		$("input[name ='Salestax']").val(data.TAX_TYPE);
		$("input[name ='isexpense']").val("1");
		var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
		document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+data.CURRENCYID+")";
		
		document.getElementById('lbltotal').innerHTML = "Total ("+data.CURRENCYID+")"; //  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency

		if(basecurrency!=data.CURRENCYID)
			document.getElementById('showtotalcur').style.display = 'block';
		else
			document.getElementById('showtotalcur').style.display = 'none';
		$('#currency').css("background-color", "#eeeeee");
		
		if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");
	});
	$(".bill-table tbody").html(body);
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
}


function getexpenseInvoiceDetail(TranId){
	var plant = document.form1.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/ExpensesServlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_EDIT_EXPENSES_DETAILS",
			Id : TranId
		},
		dataType : "json",
		success : function(data) {
			loadexpenseTable(data.orders);
		}
	});
}

function loadexpenseTable(orders){
	var numberOfDecimal = $("#NOFO_DEC").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var curency = document.form1.curency.value;
	var plant = document.form1.plant.value;
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
		var amt= parseFloat((data.AMOUNT*data.CURRENCYTOBASE)).toFixed(numberOfDecimal);
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="basecost" value="'+data.AMOUNT+'">';
		body += '<input type="hidden" name="lnno" value="1">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'" readonly><img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '<input type="hidden" name="tax_type" class="taxSearch" value="">';
		body += '<input type="hidden" name="tax" class="taxSearch" value="">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control" value="'+data.EXPENSES_ACCOUNT+'" onchange="calculateAmount(this)" readonly>';
		body += '<input type="text" name="notesexp" class="form-control" value="'+data.DESCRIPTION+'" readonly>';
		body += '</td>';
		body += '<td class="">';
		body += '<input type="text" name="account_name" value="Local sales - retail" class="form-control accountSearch" value="">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="1" onchange="calculateAmount(this)" readonly></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+amt+'" onchange="calculateAmount(this)" readonly></td>';
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+szero+'" onchange="calculateAmount(this)">';
		body += '<select name="item_discounttype" id="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
		body += "<option value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
		body += '<option>%</option>';										
		body += '</select>';
		body += '</div>';
		body += '</div>'; 
		body += '</div>';
		body += '</td>';
		/*body += '<td class="item-tax">';
		body += '<input type="hidden" name="tax_type" class="form-control">';
		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" readonly>';
		body += '</td>';*/
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		/*body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';*/
		body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" style="display:inline-block;" readonly>';
		body += '<td class="table-icon" style="position:relative;">';
		body += '<a href="#" onclick="showRemarksDetails(this)">';
		body += '<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>';
		body += '</a>';
		body += '</td>';
		body += '</td>';
		body += '</tr>';
		

		/*var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");
		
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
		}	*/
		
		if(document.form1.CUST_CODE2.value==""||document.form1.CUST_CODE2.value==null)
		{
		$("input[name ='CUST_CODE']").val(data.CUST_CODE);
		$("input[name ='CUSTOMER']").val(data.CUSTOMER);
		$('select[name ="nTAXTREATMENT"]').val(data.TAXTREATMENT);
		}		
		$("input[name ='vendno']").val(data.VENDNO);
		$("input[name ='vendname']").val(data.VNAME);		
		$("input[name ='shipment']").val(data.SHIPMENT_CODE);
		$("input[name ='pono']").val(data.ORDERNO);
		$("input[name ='expenses_date']").val(data.EXPENSES_DATE);		
		$("input[name ='paid_through_account_name']").val(data.PAID_THROUGH);
		$("input[name ='reference']").val(data.REFERENCE);
		$("input[name ='currency']").val(data.DISPLAY);
		$("#shipmentModal #shipmentpono").val(data.ORDERNO);
		$("input[name ='project']").val(data.PROJECTNAME);
		$("input[name ='PROJECTID']").val(data.PROJECTID);
		
		$("input[name ='CURRENCYUSEQT']").val(data.CURRENCYTOBASE);
		$("input[name ='CURRENCYID']").val(data.CURRENCYID);
		//$('#currency').css("background-color", "#eeeeee");;
		
		/*var urlStr = "/track/OutboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
			PLANT : plant,
			ACTION : "INVOICE"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "100") {
					var resultVal = data.result;				
					var resultV = resultVal.invno;
					$("input[name ='invoice']").val(resultV);
				} else {
					document.form1.invoice.value = "";
				}
			}
		});	*/
		
		/*$("input[name ='invoice']").val(data.INVOICE);*/
	
		var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
		document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+data.CURRENCYID+")";
		document.getElementById('lbltotal').innerHTML = "Total ("+data.CURRENCYID+")"; //  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency

		if(basecurrency!=data.CURRENCYID)
			document.getElementById('showtotalcur').style.display = 'block';
		else
			document.getElementById('showtotalcur').style.display = 'none';
		$("input[name ='due_date']").val(data.DUE_DATE);		
		$("input[name ='payment_terms']").val(data.PAYMENT_TERMS);
		
		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
	});
	$(".bill-table tbody").html(body);
	setLineNo();
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
}

function OnChange(dropdown)
{
    var myindex  = dropdown.selectedIndex;
    var SelValue = dropdown.options[myindex].value;
    document.form1.STATE_PREFIX.value = SelValue;
   /* $(".taxSearch").typeahead('val', '"');
	$(".taxSearch").typeahead('val', '');*/
    changetax();
}

function getcustname(TAXTREATMENT,CUSTNO,CURRENCY,CURRENCYID,CURRENCYUSEQT){
	//document.getElementById(TAXTREATMENT).innerHTML = TAXTREATMENT;
	$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
	document.form1.CUST_CODE.value = CUSTNO;
	getCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT);

	}

function setCustomerData(customerData){	
	$("input[name ='CUST_CODE']").val(customerData.custno);
	$("#CUSTOMER").typeahead('val', customerData.custname);
	$('select[name ="nTAXTREATMENT"]').val(customerData.sTAXTREATMENT);
	$('#nTAXTREATMENT').attr('disabled',false);
	/* To reset Order number Autosuggestion*/
	$("#ORDERNO").typeahead('val', '"');
	$("#ORDERNO").typeahead('val', '');
}
$(document).on("focusout","input[name ='shippingcost']",function(){
	renderTaxDetails();
});


function calculateTaxforsatate(obj, types, percentage, display){

	if(types=="ZERO RATE")
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

function changetax(){
	var urlStr = "/track/MasterServlet";
	$.ajax({
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		PLANT : document.form1.plant.value,
		ACTION : "GET_GST_TYPE_DATA_SALES",
		SALESLOC : document.form1.STATE_PREFIX.value
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
				if(v.SGSTTYPES == taxtype){		
					pertax = v.DISPLAY;
					SGSTTYPES=v.SGSTTYPES;
					SGSTPERCENTAGE=v.SGSTPERCENTAGE;
					DISPLAY=v.DISPLAY;
				}
			});
				$('td:eq(6)', this).find('input[name="tax_type"]').val(pertax);
				$('td:eq(6)', this).find('input[name="tax"]').val(pertax)
				if(SGSTTYPES != "0"){
					var obj1 = $(this).find('td:nth-child(6)');
					calculateTaxforsatate(obj1,SGSTTYPES,SGSTPERCENTAGE,DISPLAY);
					
				}
		});
	}
	});
	

}



function changetaxfordiscountnew(){
	var urlStr = "/track/MasterServlet";
	$.ajax({
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		PLANT : document.form1.plant.value,
		ACTION : "GET_GST_TYPE_DATA_SALES",
		SALESLOC : document.form1.STATE_PREFIX.value
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
					calculateTaxforsatate(obj1,SGSTTYPES,SGSTPERCENTAGE,DISPLAY);
				}
		});
		renderTaxDetails();
	}
	});
}
function setCURRENCYUSEQT(CURRENCYUSEQTCost){
var numberOfDecimal = $("#numberOfDecimal").val();
var oldeqtcost = parseFloat($("input[name ='CURRENCYUSEQTOLD']").val());
/*var CURRENCYAMT = $("#CURRENCYAMT").val();
var defCURRENCYAMT="0";
if(CURRENCYAMT>1)
	defCURRENCYAMT=CURRENCYAMT-1;
else
	defCURRENCYAMT=1-CURRENCYAMT;
CURRENCYUSEQTCost=CURRENCYUSEQTCost+defCURRENCYAMT;*/
var ischange=0;
var cost = 0;
$('.bill-table tbody tr').each(function () {
	var CURRENCYID = $('input[name ="CURRENCYID"]').val();
	var CURRENCY = $('input[name ="currency"]').val();
	var ITEM_CURRENCYID = $(this).find("td:nth-child(6)").find('select').val();

	if(ITEM_CURRENCYID!=undefined)
		{
	if(ITEM_CURRENCYID!="%")
		{
		if(CURRENCYID!=ITEM_CURRENCYID)
			{
			
			$('#item_discounttype').empty();
			$('#item_discounttype').append('<option value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
			$('#item_discounttype').append('<option value="%">%</option>');
			ischange=1;
			}
		}
		}

var qty = parseFloat($(this).find("td:nth-child(4)").find('input').val()).toFixed(3);
cost = parseFloat($(this).find("td:nth-child(1)").find('input[name=basecost]').val()) *parseFloat(CURRENCYUSEQTCost);
cost = parseFloat(cost).toFixed(numberOfDecimal);
$(this).find('input[name=cost]').val(cost);

var itemDiscount = parseFloat($(this).find("td:nth-child(6)").find('input').val()).toFixed(numberOfDecimal);
var discounType = $(this).find("td:nth-child(6)").find('select').val();
var itemDiscountval=itemDiscount;
 if(discounType == "%"){
	 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
	 itemDiscount = parseFloat(itemDiscount).toFixed(3);
	 ischange=0;
 }
 if(ischange==1)
	{
	var oldceq = $("input[name ='CURRENCYUSEQTOLD']").val();
	var idiscorgvalue = parseFloat(itemDiscountval)/parseFloat(oldceq);
	itemDiscountval = parseFloat(parseFloat(idiscorgvalue)*parseFloat(CURRENCYUSEQTCost)).toFixed(numberOfDecimal);
	itemDiscount=itemDiscountval;
	}
var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);

if(parseFloat(amount) >= parseFloat("0")){
	$(this).find("td:nth-child(6)").find('input').val(itemDiscount);
	$(this).find("td:nth-child(8)").find('input').val(amount);
}else{
	//alert("discout should be less than tha amount");
	if(discounType == "%"){
		$(this).find("td:nth-child(6)").find('input').val(parseFloat("0").toFixed(3));
	}else{
		$(this).find("td:nth-child(6)").find('input').val(parseFloat("0").toFixed(numberOfDecimal));
	}
	var originalamount = parseFloat((qty*cost)).toFixed(numberOfDecimal);
	$(this).find("td:nth-child(8)").find('input').val(originalamount);
}

//changetaxfordiscountnew();
});

$("input[name ='CURRENCYUSEQTOLD']").val(CURRENCYUSEQTCost);
calculateTotal();
}
function getCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT){
	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	$('input[name ="currency"]').val(CURRENCY);
	
	setCURRENCYUSEQT(CURRENCYUSEQT);
	
	var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
	document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi
	document.getElementById('lbltotal').innerHTML = "Total ("+CURRENCYID+")"; //  Author: Azees  Create date: July 16,2021  Description:  Total of Local Currency
	if(basecurrency!=CURRENCYID)
		document.getElementById('showtotalcur').style.display = 'block';
	else
		document.getElementById('showtotalcur').style.display = 'none';
	

}

/*---------------------------------------------------*/


function removetaxdropdown(){
	$("#Salestax").typeahead('destroy');
}
function addtaxdropdown(){
	var plant = document.form1.plant.value;
	$("#Salestax").typeahead({
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
					TAXKEY : "OUTBOUND",
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

function taxreset(){
	$("input[name=ptaxtype]").val("");
	$("input[name=ptaxpercentage]").val("0");
	$("input[name=ptaxdisplay]").val("");
	$("input[name=ptaxiszero]").val("1");
	$("input[name=ptaxisshow]").val("0");
	$("input[name=taxid]").val("0");
	$('.taxSearch').each(function(){
	    $(this).val("");
	});
	$("#Salestax").typeahead('val', '"');
	$("#Salestax").typeahead('val', '');
	calculateTotal();
}

function setLineNo(){
	var i=1;
	$(".bill-table tbody tr td:first-child").each(function() {
		$(this).find('input[name=lnno]').val(i);
		i++;
	});
}

function showRemarksDetails(obj) {
	var lnno = $(obj).closest('tr').find("input[name=lnno]").val();
	var invoice = $("input[name=invoice]").val();
	var item = $(obj).closest('tr').find("input[name=item]").val();

	if(item != ''){
		$.ajax({
			type : "GET",
			url : "../invoice/getSalesOrderRemarks",
			data : {
				ITEM: item,
				INVOICE: invoice,
				INLNO: lnno
			},
			dataType : "json",
			success : function(data) {
				var body="";
				if(data.REMARKS[0].remarks != undefined){
					for(i=0; i< data.REMARKS.length; i++){
						var remkval = data.REMARKS[i].remarks;
						remkval = remkval.replace(/"/g, '&quot;');						
						body += '<tr>';
						body += '<td style="position:relative;">';
						if(i != 0){
							body += '<span class="glyphicon glyphicon-remove-circle remark-action" aria-hidden="true"></span>';
						}
						body += '<input type="text" class="form-control" name="remarks" value="'+remkval+' "placeholder="Max 500 Characters" maxlength="500">';
						body += '</td>';
						body += '</tr>';
					}
				}else{
					body += '<tr>';
					body += '<td style="position:relative;">';
					body += '<input type="text" class="form-control" name="remarks" placeholder="Max 500 Characters" maxlength="500">';
					body += '</td>';
					body += '</tr>';
				}
				body += '<input type="hidden" name="r_item" value="'+item+'">';
				body += '<input type="hidden" name="r_invoice" value="'+invoice+'">';
				body += '<input type="hidden" name="r_lnno" value="'+lnno+'">';
				$("#remarks-table tbody").html(body);
			}
		});
	}else{

	}
	$("#remarksModal").modal();
}

function addRemarksRow(){
	var body="";
	body += '<tr>';
	body += '<td style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle remark-action" aria-hidden="true"></span>';
	body += '<input type="text" class="form-control" name="remarks" placeholder="Max 500 Characters" maxlength="500">';
	body += '</td>';
	body += '</tr>';
	$("#remarks-table tbody").append(body);
}

function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
	}
function generatePdfMail(dataUrl,attachName){
	formData = new FormData();
	formData.append("INVOICENO", $("#invoice").val());
	progressBar();
	sendMailTemplate(formData);
	  //return formData;

}
function toDataURL(src, callback, outputFormat) {
	  var img = new Image();
	  img.crossOrigin = 'Anonymous';
	  img.onload = function() {
	    var canvas = document.createElement('CANVAS');
	    var ctx = canvas.getContext('2d');
	    var dataURL;
	    canvas.height = this.naturalHeight;
	    canvas.width = this.naturalWidth;
	    ctx.drawImage(this, 0, 0);
	    dataURL = canvas.toDataURL(outputFormat);
	    callback(dataURL);
	  };
	  img.src = src;
	  if (img.complete || img.complete === undefined) {
	    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
	    img.src = src;
	  }
}

function changingtaxpercentage(){
	var baseamount = $("#GST").val();
	var zeroval = parseFloat("0").toFixed(3);
	if(baseamount != ""){
		var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(baseamount.match(decimal) || baseamount.match(numbers)) 
		{ 
			$("#GST").val(parseFloat(baseamount).toFixed(3));	
		}else{
			$("#GST").val(zeroval);
			alert("Please Enter Valid Percentage");
		}
	}else{
		$("#GST").val(zeroval);
	}
	removetaxdropdown();
	addtaxdropdown();
	taxreset();
}
