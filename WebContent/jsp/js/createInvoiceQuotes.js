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
var invoicepostatus =   [{
    "id": "INVOICED",
    "value": "INVOICED",
    "tokens": [
      "INVOICED"
    ]
  },
	  {
		    "id": "NOT INVOICED",
		    "value": "NOT INVOICED",
		    "tokens": [
		      "NOT INVOICED"
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
	onNewInvoice();
	var cmd = document.form1.cmd.value;
	var TranId = document.form1.TranId.value;
	//$("#SALES_LOC").val("Abu Dhabi");
	document.form1.STATE_PREFIX.value="AUH";
	var displayCustomerpop = document.form1.displayCustomerpop.value;
	
	/*if(cmd=="Edit")
	{
	if(TranId!="")
		{
		getEditInvoiceDetail(TranId);
		}
	}*/
	
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
		    	return '<div onclick="getcustname(\''+data.TAXTREATMENT+'\',\''+data.CUSTNO+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();
			if(displayCustomerpop == 'true'){
			$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');
			}
			$(".customerAddBtn").width(menuElement.width());
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
			//document.form1.CUST_CODE.value = $(this).val();
			$('#nTAXTREATMENT').attr('disabled',false);
		}).on('typeahead:select',function(event,selection){
			$("#project").typeahead('val', '"');
			$("#project").typeahead('val', '');
			$("input[name=PROJECTID]").val('');
		});
	

	/* Shipping Customer Auto Suggestion */
	$('#SHIPPINGCUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			  var urlStr = "../MasterServlet";
				$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
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
		    	return '<div onclick="document.form1.SHIPPINGID.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.shipCustomerAddBtn').remove();  
			/*$('.shipCustomer-section .tt-menu').after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal" onclick="document.cpoform.custModal.value=\'shipcust\'"><a href="#" > + New Cutomer</a></div>');*/
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
			if($(this).val() == ""){
				document.form1.SHIPPINGID.value = "";
			}
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
	
	
	
	
	/* To get the suggestion data for Status */
	$("#plno").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PLNO',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/InvoiceServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,						
						Submit : "GET_PACKING_LIST_DETAILS_FOR_ORDERNO",
						DONO : document.form1.ORDERNO.value,
						GINO : document.form1.gino.value,
						PLNO : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.plno);
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
		    return '<p>' + data.PLNO + '</p>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	});
	
	/* To get the suggestion data for Status */
	$("#plnoInvoice").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PLNO',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/InvoiceServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,						
						Submit : "GET_PACKING_LIST_DETAILS_FOR_INVOICE",
						DONO : document.form1.ORDERNO.value,
						INVOICE : document.form1.invoice.value,
						PLNO : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.plno);
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
		    return '<p>' + data.PLNO + '</p>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
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
	
	/* Employee Type Auto Suggestion */
	$('#EMP_NAME_SRH').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'EMPNO',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "getEmployeeListStartsWithName",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.EMP_MST);
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
		    return '<div><p class="item-suggestion">'+data.EMPNO+'</p><br/><p class="item-suggestion">'+data.FNAME+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
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
			return '<div><p class="item-suggestion">'+data.ITEM+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
				
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
	
	/* Sales Location Auto Suggestion */
	/*$('#SALES_LOC').typeahead({
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
		    	return '<p onclick="document.form1.STATE_PREFIX.value = \''+data.STATE_PREFIX+'\'">' + data.STATE + '</p>';		    
			}
		  }
		}).on('typeahead:render',function(event,selection){
			
		  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			$(".taxSearch").typeahead('val', '"');
			$(".taxSearch").typeahead('val', '');			
		});
*/	
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
			var menuElement = $(this).parent().find(".smalldrop");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".smalldrop");
			setTimeout(function(){ menuElement.next().hide();}, 180);
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
	
	/* To get the suggestion data for Status */
	$(".invoicestatus").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'invoicepostatus',
	  display: 'value',  
	  source: substringMatcher(invoicepostatus),
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

	/* gino Number Auto Suggestion */
	/*$('#gino').typeahead({
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
						PLNO : document.form1.plno.value,
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
			$("#plno").typeahead('val', '"');
			$("#plno").typeahead('val', '');
			removeSuggestionToTable();
			addSuggestionToTable();
		});*/
	
		
	$(".bill-table tbody").on('click','.bill-action',function(){
	    
	    var obj = $(this).closest('tr').find('td:nth-child(6)');
	    //calculateTax(obj, "", "", "");
	    $(this).parent().parent().remove();
	    calculateTotal();
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
	
	/*$(document).on("focusout",".taxSearch",function(){
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
	});*/
	$("#btnBillOpenEmail").click(function(){
		if ($('#CUSTOMEREMAIL').val() == ''){
			alert('Customer record does not have an email address. Please update customer email and try again or Save as Draft OR Save as Open');
			return;
		}
		$('input[name ="invoice_status"]').val('Open');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		var isValid = validateInvoice();
		if (!isValid){
			return;
		}
		$('#Submit').val('Save');
		$('#ACTION').val('');
		var data = new FormData($("#createBillForm")[0]); //$("#purchaseOrderForm").serialize();
			$.ajax({
				type : "POST",
				url : "../InvoiceServlet",
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
					console.log(data);
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

/*	if($("#ORDERNO").val() !== ""){
		getOrderDetailsForInvoice($("#ORDERNO").val());
		$("input[name ='CUSTOMER']").typeahead('val', $("#ORDERNO").val());
		$(".add-line").hide();
		$("#item_rates").attr("readonly", true);
	}*/
	
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
				NAME : query
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
		}).on('typeahead:change',function(event,selection){			
			/* To reset auto number Autosuggestion*/
			$("#plnoInvoice").typeahead('val', '"');
			$("#plnoInvoice").typeahead('val', '');
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
		
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});
	
	$("input[name=QTY]").mouseenter(function(){
		var content = "<p class='text-left'>Reorder Level: "+$(this).data("rl")+"</p>";
		content += "<p class='text-left'>Max Stock Quantity: "+$(this).data("msq")+"</p>";
		content += "<p class='text-left'>Stock on Hand: "+$(this).data("soh")+"</p>";
		content += "<p class='text-left'>Estimated Quantity: "+$(this).data("eq")+"</p>";
		content += "<p class='text-left'>Available Quantity: "+$(this).data("aq")+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"}); 
	});
	
	$("input[name=unitprice]").mouseenter(function(){
		var minsp = parseFloat($(this).closest('tr').find("input[name=minsp]").val());
		var customerdiscount = parseFloat($(this).closest('tr').find("input[name=customerdiscount]").val())
		var content = "";
		if(isNaN(minsp)){
			content = "<p class='text-left'>Minimum Selling Price: 0.000</p>";
		}else{
			content = "<p class='text-left'>Minimum Selling Price: "+minsp+"</p>";
		}
		if(isNaN(customerdiscount)){
			content += "<p class='text-left'>Customer Discount (By Price or Percentage): 0.000</p>";
		}else{
			content += "<p class='text-left'>Customer Discount (By Price or Percentage): "+customerdiscount+"</p>";
		}
		
		
		$(this).tooltip({title: content, html: true, placement: "top"});
	});
	
	addSuggestionToTable();
});


function addRow(){
	var curency = $("input[name=CURRENCYID]").val();
	var taxdisplay = $("input[name=ptaxdisplay]").val();
	var numberOfDecimal = $("#numberOfDecimal").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var body="";
	body += '<tr>';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;"><input type="hidden" name="basecost" value="0.00">';
	body += '<input type="hidden" name="minsp" value="">';
	body += '<input type="hidden" name="customerdiscount" value="">';
	body += '<input type="hidden" name="outgoingqty" value="">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" style="width:87%" class="form-control itemSearch" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '</td>';	
	body += '<td class="bill-item">';
	body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Account"  value="Local sales - retail">';
	body += '</td>';
	body += '<td class="invEl">';
	body += '<input type="text" name="uom" class="form-control uomSearch" placeholder="UOM">';
	body += '</td>';
	body += '<td class="invEl">';
	body += '<input type="text" name="loc" class="form-control locSearch" placeholder="Location">';
	body += '</td>';
	body += '<td class="invEl">';
	body += '<input type="text" name="batch" value="NOBATCH" class="form-control batchSearch" placeholder="Batch">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)"></td>';
	body += "<td class=\"item-cost text-right\"><input type=\"text\" name=\"cost\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\"></td>";
	body += '<td class="item-discount text-right">';
	body += '<div class="row">';							
	body += '<div class=" col-lg-12 col-sm-3 col-12">';
	body += '<div class="input-group my-group" style="width:120px;">';
	body += "<input name=\"item_discount\" type=\"text\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\">";
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
	body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
	body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
	body += '</td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += "<input name=\"amount\" type=\"text\" class=\"form-control text-right\" value="+szero+" readonly=\"readonly\" style=\"display:inline-block;\">";
	body += '</td>';
	body += '</tr>';
	$(".bill-table tbody").append(body);
	removeSuggestionToTable();
	addSuggestionToTable();
	if($("input[name=DEDUCT_INV]").val() == "0"){
		$(".invEl").hide();
	}
	
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


function addSuggestionToTable(){
	var plant = document.form1.plant.value;
	
	/* To get the suggestion data for Product */
//	var deductInv = document.form1.DEDUCT_INV.value;
	var origin = document.form1.ORIGIN.value;
	var deductInv = $("input[name=DEDUCT_INV]").val();
	if(deductInv=="1") {
		
//	if($("input[name=DEDUCT_INV]").val() == "0"){
//	if(deductInv == "1" && origin == "manual"){
	$(".itemSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
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
			return '<div onclick="loadItemData(this,\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.SALESUOM+'\',\''+ data.ITEM+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' '+data.UOM+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
//			menuElement.after( '<div class="itemAddBtn footer"  data-toggle="modal" data-target="#productModal"><a href="#"> + Add New Product</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".bigdrop").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		});	
	}else {
			
		$(".itemSearch").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true,
			  classNames: {
				 	menu: 'bigdrop'
				  }
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
				return '<div onclick="loadItemData(this,\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.SALESUOM+'\',\''+ data.ITEM+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' '+data.UOM+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="itemAddBtn footer"  data-toggle="modal" data-target="#productModal"><a href="#"> + Add New Product</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".bigdrop").css('display') != "block")
					menuElement.next().hide();		  
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".bigdrop");
				setTimeout(function(){ menuElement.next().hide();}, 150);
			});	
	}
	
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
	
	/* To get the suggestion data for Uom */
	$('.uomSearch').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'UOM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_UOM_DATA",
				UOM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.UOMMST);
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p></div>';
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
	
	$('.locSearch').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCATION_DATA",
				LOCATION : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCMST);
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
			return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
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
			$(this).parent().parent().parent().find('input[name=batch]').typeahead('val', '');
			$(this).parent().parent().parent().find('input[name=batch]').typeahead('val', 'NOBATCH');
		});	
	
	$(".batchSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
		  display: 'BATCH',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			var obj = $(this)[0].$el.parent().parent().parent().parent().closest('tr');
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_BATCH_DATA",
				QUERY : query,
				ITEMNO : obj.find("input[name=item]").val(),
				UOM : obj.find("input[name=uom]").val(),
				LOC : obj.find("input[name=loc]").val()
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.batches);
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
				console.log(data);
				return '<div"><p class="item-suggestion">Batch: '+data.BATCH+'</p><p class="item-suggestion pull-right">PC/PCS/EA UOM :'+data.PCSUOM+'</p><br/><p class="item-suggestion">PC/PCS/EA UOM Quantity: '+data.PCSQTY+'</p><p class="item-suggestion pull-right">Inventory UOM: '+data.UOM+'</p><br/><p class="item-suggestion">Inventory UOM Quantity: '+data.QTY+'</p><p class="item-suggestion pull-right">Received Date: '+data.CRAT+'</p><br/><p class="item-suggestion">Expiry Date: '+data.EXPIRYDATE+'</p></div>';
				//return '<div"><p class="item-suggestion">'+data.BATCH+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' '+data.UOM+'</p></div>';
			}
		  }
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
	$(".taxSearch").typeahead('destroy');
	$(".accountSearch").typeahead('destroy');
	$(".uomSearch").typeahead('destroy');
	$(".locSearch").typeahead('destroy');
	$(".batchSearch").typeahead('destroy');
	//$(".item_discounttypeSearch").typeahead('destroy');
}

function loadItemData(obj, catalogPath, account, cost,salesuom, productId, nonStkFlag){
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	$(obj).closest('tr').find("td:nth-child(1)").find('input[name=basecost]').val(cost);
	$(obj).closest('tr').find("input[name=uom]").val(salesuom);
	cost = parseFloat($("input[name ='CURRENCYUSEQT']").val()) * parseFloat(cost);
	cost = parseFloat(cost).toFixed(numberOfDecimal);

	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("input[name=cost]").val(cost);
	$(obj).closest('tr').find("input[name=nonStkFlag]").val(nonStkFlag);	
	//calculateAmount(obj);
	
	var currencyID = $("input[name=CURRENCYID]").val();
	var custCode = $("input[name=CUST_CODE]").val();
	var dono = $("input[name=ORDERNO]").val();
	
	var urlStr = "/track/ItemMstServlet";
	 var discount;
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				DONO: dono,
				TYPE: "SO",
				CURRENCYID: currencyID,
				CUSTCODE: custCode,
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					//outgoingOBDiscount
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
						
						if(resultVal.outgoingqty == null || resultVal.outgoingqty == undefined || resultVal.outgoingqty == 0){
							$(obj).closest('tr').find("input[name=outgoingqty]").val("0.000");
						}else{
							$(obj).closest('tr').find("input[name=outgoingqty]").val(resultVal.outgoingqty.match(regex)[0]);
						}
						
						Convertedmprice=resultVal.minSellingConvertedUnitCost;
						$(obj).closest('tr').find("input[name=minsp]").val(Convertedmprice);
						
						if(resultVal.outgoingOBDiscount=='' || resultVal.outgoingOBDiscount=='0' ||resultVal.outgoingOBDiscount=='0.00'||resultVal.outgoingOBDiscount==undefined)
						{
							$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
						}
						else
						{
							if(resultVal.OBDiscountType=="BYPERCENTAGE")
							{
								$(obj).closest('tr').find("input[name=customerdiscount]").val(resultVal.outgoingOBDiscount.match(regex)[0]+'%');
							}
							else
							{
								$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat(resultVal.outgoingOBDiscount).toFixed(numberOfDecimal));
							}
						
						}
						
						$(obj).closest('tr').find("input[name=qty]").data('rl',resultVal.stkqty);
						$(obj).closest('tr').find("input[name=qty]").data('msq',resultVal.maxstkqty);
						$(obj).closest('tr').find("input[name=qty]").data('soh',resultVal.stockonhand);
						$(obj).closest('tr').find("input[name=qty]").data('eq',resultVal.EstQty);
						$(obj).closest('tr').find("input[name=qty]").data('aq',resultVal.AvlbQty);
					}
					
					
					calculateAmount(obj);
					loadQtyToolTip(obj);
					loadUnitPriceToolTip(obj);
				}
			});
}

/*function calculateAmount(obj){

	var numberOfDecimal = $("#numberOfDecimal").val();
	var state = document.form1.STATE_PREFIX.value;
	var plant = document.form1.plant.value;
	
	var qty = parseFloat($(obj).closest('tr').find("td:nth-child(7)").find('input').val()).toFixed(3);
	var cost = parseFloat($(obj).closest('tr').find("td:nth-child(8)").find('input').val()).toFixed(numberOfDecimal);
	var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(9)").find('input').val()).toFixed(numberOfDecimal);
	var discounType = $(obj).closest('tr').find("td:nth-child(9)").find('select').val();
	var nonStkFlag = $(obj).closest('tr').find("input[name=nonStkFlag]").val();
	var itemDiscountval=itemDiscount;
	 if(discounType == "%"){
		 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
		 itemDiscount = parseFloat(itemDiscount).toFixed(3);
	 }
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
	
	if(parseFloat(amount) >= parseFloat("0") || (amount != 0 && nonStkFlag == "Y")){
		$(obj).closest('tr').find("td:nth-child(9)").find('input').val(itemDiscount);
		$(obj).closest('tr').find("td:nth-child(11)").find('input').val(amount);
	}else if(nonStkFlag != "Y"){
		alert("discout should be less than tha amount");
		if(discounType == "%"){
			$(obj).closest('tr').find("td:nth-child(9)").find('input').val(parseFloat("0").toFixed(3));
		}else{
			$(obj).closest('tr').find("td:nth-child(9)").find('input').val(parseFloat("0").toFixed(numberOfDecimal));
		}
		var originalamount = parseFloat((qty*cost)).toFixed(numberOfDecimal);
		$(obj).closest('tr').find("td:nth-child(11)").find('input').val(originalamount);
	}
	
	$(obj).closest('tr').find("td:nth-child(7)").find('input').val(qty);
	$(obj).closest('tr').find("td:nth-child(8)").find('input').val(cost);
	
	changetaxfordiscountnew();
	calculateTotal();

}

function calculateTotal(){
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = 0, discountValue=0, shippingvalue=0, adjustmentvalue=0,totalvalue=0,orderdisc=0;
	
	var discount= document.form1.discount.value;
	discount = parseFloat(discount).toFixed(numberOfDecimal);
	$("input[name ='discount']").val(discount);
	
	var shippingcost= document.form1.shippingcost.value;
	shippingcost = parseFloat(shippingcost).toFixed(numberOfDecimal);
	$("input[name ='shippingcost']").val(shippingcost);
	
	var adjustment= document.form1.adjustment.value;
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
	 
	 var shippingvalue = $('input[name ="shippingcost"]').val();
	 shippingvalue = (shippingvalue == "") ? 0.00 : shippingvalue;
	 $("#shipping").html(parseFloat(shippingvalue).toFixed(numberOfDecimal));
	 
	 var orderdiscpercentage = $('input[name ="orderdiscount"]').val();
	 orderdiscpercentage = (orderdiscpercentage == "") ? 0.00 : orderdiscpercentage;
	 orderdisc = parseFloat(parseFloat(amount)*(parseFloat(orderdiscpercentage)/100)); 
	 $("#orderdiscount").html(parseFloat(-orderdisc).toFixed(numberOfDecimal));
	 $('input[name ="orderdiscount"]').val(parseFloat(orderdiscpercentage).toFixed(3));
	 
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
	 
	 
	 var pretotal = parseFloat(parseFloat(amount)- parseFloat(orderdisc) - parseFloat(discountValue) + parseFloat(taxTotal)
			 + parseFloat(shippingvalue)).toFixed(numberOfDecimal);
	 
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
			 + parseFloat(shippingvalue) + parseFloat(adjustmentvalue)).toFixed(numberOfDecimal);
	 $("#total").html(totalvalue);
	 $("#total_amount").val(totalvalue); // hidden input

	
}*/

function calculateTax(obj, types, percentage, display){
	//var originalTaxType= types;
	//var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); 
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
				//var currenttype=name.substr(0, name.indexOf('(')); 
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
}

function renderTaxDetails(){
	var html="";
	var taxTotal = 0;
	var subtotal =0;
	var numberOfDecimal = $("#numberOfDecimal").val();
	$.each(taxList, function( key, data ) {
		var mydata=JSON.stringify(data);
		console.log("Tax dta:"+mydata);
		var originalTaxType= data.types;
		//var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); )
		var taxtype = originalTaxType;
		console.log("Tax Type:"+taxtype);
		console.log("Zero Type:"+zerotype);
		percentageget=data.percentage;
		
		var shipingcost = document.form1.shippingcost.value;
		var shipingtax = parseFloat(shipingcost*(percentageget/100)).toFixed(numberOfDecimal);
		var discount = document.form1.discount.value;
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
			html+='<div class="total-row">';
			html+='<div class="total-label">'+data.display+'</div>';
			html+='<div class="total-amount taxAmount">'+parseFloat((parseFloat(data.value)+parseFloat(shipingtax))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal)+'</div>';
			html+='</div>';
			taxTotal += parseFloat((parseFloat(data.value)+parseFloat(shipingtax))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal);
		}
	});
	$(".taxDetails").html(html);
	
	($("#item_rates").val() == 0) ? $(".productRate").hide() : $(".productRate").show(); 
	
	if($("#item_rates").val() == 0){
		$('input[name ="taxamount"]').val(taxTotal);
	}else{
		$(".taxDetails").html("");
		$('input[name ="taxamount"]').val("0");
	}
	calculateTotal();
}

function validateInvoice(){
	var supplier = document.form1.CUSTOMER.value;
	var bill = document.form1.invoicequotes.value;
	var billDate = document.form1.invoice_date.value;
	var itemRates = document.form1.item_rates.value;
	var discount = document.form1.discount.value;
	var discountAccount = document.form1.discount_account.value;
	var Currency = document.form1.CURRENCY.value;
	var isItemValid = true, isAccValid = true, isUnitPriceValid = true;
	
	//RESVI START
	var ValidNumber   = document.form1.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" order's you can create"); return false; }
	//RESVI END
	
	if(supplier == ""){
		alert("Please select a Customer.");
		return false;
	}	
	if(bill == ""){
		alert("Please enter the Invoice quotes number.");
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
	/*if(discount != "" && discountAccount == ""){
		alert("Please choose an account for discount.");
		return false;
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
	
/*	$("input[name ='cost']").each(function() {
		if($(this).val() <= 0){
			isUnitPriceValid =  false;
	    }
	});	
	
	if(!isUnitPriceValid){
		alert("Rate cannot be Zero ");
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

function transportCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
	}
}

function setCustomerData(customerData){	
	$("input[name ='CUST_CODE']").val(customerData.vendno);
	$("#CUSTOMER").typeahead('val', customerData.vendname);	
	/* To reset Order number Autosuggestion*/
	$("#ORDERNO").typeahead('val', '"');
	$("#ORDERNO").typeahead('val', '');
}



function productCallbackwithallInvoice(productData){
	if(productData.STATUS="SUCCESS"){
		var $tbody = $(".bill-table tbody");
		var $last = $tbody.find('tr:last');
		var taxdisplay = $("input[name=ptaxdisplay]").val();
		 $last.remove();
		 var deductInv = document.form1.DEDUCT_INV.value;
		 var origin = document.form1.ORIGIN.value;
		 var catalogPath=productData.CATLOGPATH;
		 var account="Local sales - retail";
		 var cost=productData.UNITPRICE;
		 var salesuom=productData.SALESUOM;
		 var productId=productData.ITEM;
		 var curency = document.form1.curency.value;
		 
		var body="";
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+productData.CATLOGPATH+'" style="width: 100%;">';
		//body += '<input type="hidden" name="lnno" value="1">';
		body += '<input type="hidden" name="basecost" value="0.00">';
		body += '<input type="hidden" name="minsp" value="">';
		body += '<input type="hidden" name="customerdiscount" value="">';
		body += '<input type="hidden" name="outgoingqty" value="">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control itemSearch" value="'+productData.ITEM+'" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
		body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Account"  value="Local sales - retail">';
		body += '</td>';
		//if(deductInv == "1" && origin == "manual"){
		body += '<td class="invEl">';
		body += '<input type="text" name="uom" class="form-control uomSearch"  value="'+productData.PURCHASEUOM+'"  placeholder="UOM">';
		body += '</td>';
		body += '<td class="invEl">';
		body += '<input type="text" name="loc" class="form-control locSearch" placeholder="Location">';
		body += '</td>';
		body += '<td class="invEl">';
		body += '<input type="text" name="batch" value="NOBATCH" class="form-control batchSearch" placeholder="Batch">';
		body += '</td>';
		//}
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+productData.UNITPRICE+'" onchange="calculateAmount(this)"></td>';
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)">';
		
		body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
		body += "<option value="+curency+">"+curency+"</option>";
		body += '<option>%</option>';										
		body += '</select>';
		body += '</div>';
		body += '</div>'; 
		body += '</div>';
		body += '</td>';
		body += '<td class="item-tax">';
		body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+productData.UNITPRICE+'" readonly="readonly" style="display:inline-block;"></td>';
		body += '</tr>';
		$(".bill-table tbody").append(body);
		calculateTotal();
		removeSuggestionToTable();
		addSuggestionToTable();
		loadItemData($tbody.find('tr:last'), catalogPath, account, cost,salesuom,productId);
	}
	if($("input[name=DEDUCT_INV]").val() == "0"){
		$(".invEl").hide();
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
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item"  style="width:87%" class="form-control" value="'+data.ITEM+'" readonly="readonly">';
		body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
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
		
		
		if(loadtype=="ZERO RATE")
			{
				zerotype++;
			}
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item"  style="width:87%" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)">';
		body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITPRICE+'" onchange="calculateAmount(this)"></td>';
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEM_DISCOUNT+'" onchange="calculateAmount(this)">';
		//body += '<textarea rows="1" maxlength="3" style="resize: none;padding: 5px;width:32% !important;background: #fbfafa;" class="form-control item_discounttypeSearch" id="item_discounttype" name="item_discounttype" onchange="calculateAmount(this)">'+data.ITEM_DISCOUNT_TYPE+'</textarea>';
		//body += "<input type=\"text\" class=\"discountPicker form-control item_discounttypeSearch\" id=\"item_discounttype\" value="+data.ITEM_DISCOUNT_TYPE+" name=\"item_discounttype\" onchange=\"calculateAmount(this)\">";
		body += '<select name="item_discounttype" id="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
		body += "<option value="+curency+">"+curency+"</option>";
		body += '<option value="%">%</option>';										
		body += '</select>';
		body += '</div>';
		body += '</div>'; 
		body += '</div>';
		body += '</td>';
		body += '<td class="item-tax" data-name="'+taxtype+'" data-tax="'+taxpercentage+'">';
		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';
		body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" >';
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
		
		$("input[name ='CUST_CODE']").val(data.CUST_CODE);
		$("input[name ='CUSTOMER']").val(data.CUSTOMER);
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
		
		$("input[name ='discount']").val(data.DISCOUNT);
		$("select[name ='discount_type']").val(data.DISCOUNT_TYPE);
		$("input[name ='discount_account']").val(data.DISCOUNT_ACCOUNT);
		$("input[name ='adjustment']").val(data.ADJUSTMENT);
		if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");
	});
	$(".bill-table tbody").html(body);
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
}
function onNewInvoice()
{
	var plant = document.form1.plant.value;	
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : plant,
		ACTION : "INVOICE_QUOTES"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.invno;
				document.form1.invoicequotes.value= resultV;
	
			} else {
				alert("Unable to genarate INVOICE QUOTE NO");
				document.form1.invoice.value = "";
			}
		}
	});	
	}



function OnChange(dropdown)
{
    var myindex  = dropdown.selectedIndex;
    var SelValue = dropdown.options[myindex].value;
    document.form1.STATE_PREFIX.value = SelValue;
    changetax();
}

function getcustname(TAXTREATMENT,CUSTNO){
//document.getElementById(TAXTREATMENT).innerHTML = TAXTREATMENT;
$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
document.form1.CUST_CODE.value = CUSTNO;
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

function removetaxtrestl(){
	$('#nTAXTREATMENT').attr('disabled',false);
}

$(document).on("focusout","input[name ='shippingcost']",function(){
	var currentShippingCost = parseFloat($("input[name ='shippingcost']").val());
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
			var taxing = $(this).find('input[name="tax_type"]').val();
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
				$('td:eq(5)', this).find('input[name="tax_type"]').val(pertax);
				$('td:eq(5)', this).find('input[name="tax"]').val(pertax)
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
			var taxtype = $(this).find('input[name="tax_type"]').val();
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
					var obj1 = $('td:eq(5)', this);
					calculateTaxforsatate(obj1,SGSTTYPES,SGSTPERCENTAGE,DISPLAY);
				}
		});
		renderTaxDetails();
	}
	});
}

/*function setCURRENCYUSEQT(CURRENCYUSEQTCost){

	var numberOfDecimal = $("#numberOfDecimal").val();
	var plant = document.form1.plant.value;
	var cost = 0;
	var ischange=0;
	$('tr').each(function () {
		var CURRENCYID = $('input[name ="CURRENCYID"]').val();
		var CURRENCY = $('input[name ="CURRENCY"]').val();
		var ITEM_CURRENCYID = $(this).find("td:nth-child(9)").find('select').val();
		
		if(ITEM_CURRENCYID!=undefined)
			{
		if(ITEM_CURRENCYID!="%")
			{
			if(CURRENCYID!=ITEM_CURRENCYID)
				{
				
				$('#item_discounttype').empty();
				$('#item_discounttype').append('<option value="' + CURRENCYID + '">' + CURRENCY + '</option>');
				$('#item_discounttype').append('<option value="%">%</option>');
				ischange=1;
				}
			}
			}
		var qty = parseFloat($(this).find("td:nth-child(7)").find('input').val()).toFixed(3);
		cost = parseFloat($(this).find("td:nth-child(1)").find('input[name=basecost]').val()) *parseFloat(CURRENCYUSEQTCost);
		cost = parseFloat(cost).toFixed(numberOfDecimal);
		$(this).find("td:nth-child(8)").find('input').val(cost);
		
		var itemDiscount = parseFloat($(this).find("td:nth-child(9)").find('input').val()).toFixed(numberOfDecimal);
		var discounType = $(this).find("td:nth-child(9)").find('select').val();
	var itemDiscountval=itemDiscount;
	 if(discounType == "%"){
		 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
		 itemDiscount = parseFloat(itemDiscount).toFixed(3);
		 ischange=0;
	 }
	 if(ischange==1)
		{
		itemDiscountval = parseFloat(parseFloat(itemDiscountval)/parseFloat(CURRENCYUSEQTCost)).toFixed(numberOfDecimal);
		itemDiscount=itemDiscountval;
		}
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
	
	if(parseFloat(amount) >= parseFloat("0")){
		$(this).find("td:nth-child(9)").find('input').val(itemDiscount);
		$(this).find("td:nth-child(11)").find('input').val(amount);
	}else{
		//alert("discout should be less than tha amount");
		if(discounType == "%"){
			$(this).find("td:nth-child(9)").find('input').val(parseFloat("0").toFixed(3));
		}else{
			$(this).find("td:nth-child(9)").find('input').val(parseFloat("0").toFixed(numberOfDecimal));
		}
		var originalamount = parseFloat((qty*cost)).toFixed(numberOfDecimal);
		$(this).find("td:nth-child(11)").find('input').val(originalamount);
	}
	
	changetaxfordiscountnew();
	});
	calculateTotal();
	
}*/

function getCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT){
	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	$('input[name ="CURRENCY"]').val(CURRENCY);
	
	$('#discount_type').empty();
	$('#discount_type').append('<option value="' + CURRENCYID + '">' + CURRENCY + '</option>');
	$('#discount_type').append('<option value="%">%</option>');
	
	setCURRENCYUSEQT(CURRENCYUSEQT);
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

function calculateAmount(obj){

	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var state = $("input[name=STATE_PREFIX]").val();
	
	var qty = parseFloat($(obj).closest('tr').find('input[name=qty]').val()).toFixed(3);
	var cost = parseFloat($(obj).closest('tr').find('input[name=cost]').val()).toFixed(numberOfDecimal);
	var itemDiscount = parseFloat($(obj).closest('tr').find('input[name=item_discount]').val()).toFixed(numberOfDecimal);
	var discounType = $(obj).closest('tr').find('select[name=item_discounttype]').val();
	var itemDiscountval=itemDiscount;
	if(discounType == "%"){
		 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
		 itemDiscount = parseFloat(itemDiscount).toFixed(3);
	}
	 
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
	
	if(parseFloat(amount) >= parseFloat("0")){
		$(obj).closest('tr').find('input[name=item_discount]').val(itemDiscount);
		$(obj).closest('tr').find('input[name=amount]').val(amount);
	}else{
		alert("discout should be less than the amount");
		if(discounType == "%"){
			$(obj).closest('tr').find('input[name=item_discount]').val(parseFloat("0").toFixed(3));
		}else{
			$(obj).closest('tr').find('input[name=item_discount]').val(parseFloat("0").toFixed(numberOfDecimal));
		}
		var originalamount = parseFloat((qty*cost)).toFixed(numberOfDecimal);
		$(obj).closest('tr').find('input[name=amount]').val(originalamount);
	}
	
	$(obj).closest('tr').find('input[name=qty]').val(qty);
	$(obj).closest('tr').find('input[name=cost]').val(cost);
	
	calculateTotal();
}

function calculateTotal(){
	
	var numberOfDecimal = $("input[name='numberOfDecimal']").val();
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

	var shippingcost= $("input[name='shippingcost']").val();
	shippingcost = checkno(shippingcost);
	shippingcost = parseFloat(shippingcost).toFixed(numberOfDecimal);
	$("input[name ='shippingcost']").val(shippingcost);
	
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
	 
	 
	 var shippingvalue = $('input[name ="shippingcost"]').val();
	 shippingvalue = (shippingvalue == "") ? 0.00 : shippingvalue;
	 $("#shipping").html(parseFloat(shippingvalue).toFixed(numberOfDecimal));

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
	 var shiptaxstatus = $("input[name='shiptaxstatus']").val();
	 var odisctaxstatus = $("input[name='odiscounttaxstatus']").val();
	 var disctaxstatus = $("input[name='discounttaxstatus']").val();
	 
	 if(ptaxstatus == "0"){
			if(ptaxisshow == "1"){
				taxTotal = parseFloat(taxTotal) + parseFloat((subtotalamount/100)*ptaxpercentage);
				
				if(shiptaxstatus == "1"){
					taxTotal = parseFloat(taxTotal) + parseFloat((shippingvalue/100)*ptaxpercentage);
				}
				
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
				
				if(shiptaxstatus == "1"){
					taxTotal = parseFloat(taxTotal) + parseFloat((shippingvalue/100)*ptaxpercentage);
				}
				
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
		$('input[name ="taxamount"]').val(taxTotal);
		$('input[name = "taxTotal"]').val(taxTotal)
	}else{
		$(".taxDetails").html("");
		$('input[name ="taxamount"]').val("0");
		$('input[name = "taxTotal"]').val("0");
	}
	 
	 
	 var pretotal = parseFloat(parseFloat(amount) - parseFloat(discountValue) - parseFloat(orddiscountValue) + parseFloat(taxTotal)
			 + parseFloat(shippingvalue)).toFixed(numberOfDecimal);
	 
	 if(parseFloat(pretotal) >= parseFloat(adjustmentvalue)){
		 $("#adjustment").html(parseFloat(adjustmentvalue).toFixed(numberOfDecimal));
		 $('input[name ="adjustment"]').val(parseFloat(adjustmentvalue).toFixed(numberOfDecimal));
	 }else{
		 alert("adjustment should be less than "+pretotal);
		 adjustmentvalue = "0";
		 $("#adjustment").html(parseFloat("0").toFixed(numberOfDecimal));
		 $('input[name ="adjustment"]').val(parseFloat("0").toFixed(numberOfDecimal));
	 }
	 
	 totalvalue = parseFloat(parseFloat(amount)- parseFloat(orddiscountValue) - parseFloat(discountValue) + parseFloat(taxTotal)
			 + parseFloat(shippingvalue) + parseFloat(adjustmentvalue)).toFixed(numberOfDecimal);
	 $("#total").html(totalvalue);
	 
	 $("#total_amount").val(totalvalue); // hidden input
}

function changingtaxpercentage(){
	var baseamount = $("#GST").val();
	var zeroval = "0";
	if(baseamount != ""){
		var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(baseamount.match(decimal) || baseamount.match(numbers)) 
		{ 
			$("#GST").val(baseamount);	
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

function changeitem(obj){
	var obj2 = $(obj).parent().find('input[name="item"]');
	$(obj2).typeahead('val', '');
	$(obj).parent().find('input[name="item"]').focus();
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

function isshiptaxing(obj){
	 if ($(obj).is(":checked")){
		 $('input[name ="shiptaxstatus"]').val("1");
		 $("#shtax").html("");
		 $("#shtax").html("(Tax Inclusive)");
	 }else{
		 $('input[name ="shiptaxstatus"]').val("0");
		 $("#shtax").html("");
		 $("#shtax").html("(Tax Exclusive)");
	 }
	calculateTotal();
}

function isodisctaxing(obj){
	 if ($(obj).is(":checked")){
		 $('input[name ="odiscounttaxstatus"]').val("1");
		 $("#odtax").html("");
		 $("#odtax").html("(Tax Inclusive)");
	 }else{
		 $('input[name ="odiscounttaxstatus"]').val("0");
		 $("#odtax").html("");
		 $("#odtax").html("(Tax Exclusive)");
	 }
	 calculateTotal();
}

function isdtaxing(obj){
	 if ($(obj).is(":checked")){
		 $('input[name ="discounttaxstatus"]').val("1");
		 $("#dtax").html("");
		 $("#dtax").html("(Tax Inclusive)");
	 }else{
		 $('input[name ="discounttaxstatus"]').val("0");
		 $("#dtax").html("");
		 $("#dtax").html("(Tax Exclusive)");
	 }
	 calculateTotal();
}



function setCURRENCYUSEQT(CURRENCYUSEQTCost){
	var plant = document.form1.plant.value;	
	var numberOfDecimal = $("#numberOfDecimal").val();
	var oldeqtcost = $('input[name ="CURRENCYUSEQTOLD"]').val();
	var CURRENCYID = $('input[name ="CURRENCYID"]').val();
	var CURRENCY = $('input[name ="CURRENCY"]').val();
	var cost = 0;
	var ischange=0;

$('.bill-table tbody tr').each(function () {
	
	var qty = parseFloat($(this).find('input[name ="qty"]').val()).toFixed(3);
	cost = ((parseFloat($(this).find('input[name=cost]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
	cost = parseFloat(cost).toFixed(numberOfDecimal);
	$(this).find('input[name=cost]').val(cost);

	var ITEM_CURRENCYID = $(this).find('select[name ="item_discounttype"]').val();
	
	var itemDiscount = parseFloat($(this).find('input[name=item_discount]').val()).toFixed(numberOfDecimal);
	var itemDiscountval=itemDiscount;
	if(ITEM_CURRENCYID!=undefined){
		if(ITEM_CURRENCYID != "%"){
			if(CURRENCYID!=ITEM_CURRENCYID){
				$(this).find('select[name ="item_discounttype"]').empty();
				$(this).find('select[name ="item_discounttype"]').append('<option selected value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
				$(this).find('select[name ="item_discounttype"]').append('<option value="%">%</option>');
				itemDiscountval = parseFloat((parseFloat(itemDiscountval)/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost)).toFixed(numberOfDecimal);
				itemDiscount=itemDiscountval;
				$(this).find('input[name=item_discount]').val(itemDiscountval);
			}else{
				itemDiscountval = parseFloat((parseFloat(itemDiscountval)/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost)).toFixed(numberOfDecimal);
				itemDiscount=itemDiscountval;
				$(this).find('input[name=item_discount]').val(itemDiscountval);
			}
		}else{
			$(this).find('select[name ="item_discounttype"]').empty();
			$(this).find('select[name ="item_discounttype"]').append('<option value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
			$(this).find('select[name ="item_discounttype"]').append('<option selected value="%">%</option>');
			itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
			itemDiscount = parseFloat(itemDiscount).toFixed(3);
		}
	}
	
	var discounType = $(this).find('select[name ="item_discounttype"]').val();
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
	
	if(parseFloat(amount) >= parseFloat("0")){
		$(this).find('input[name=item_discount]').val(itemDiscount);
		$(this).find('input[name=amount]').val(amount);
	}else{
		//alert("discout should be less than tha amount");
		if(discounType == "%"){
			$(this).find('input[name=item_discount]').val(parseFloat("0").toFixed(3));
		}else{
			$(this).find('input[name=item_discount]').val(parseFloat("0").toFixed(numberOfDecimal));
		}
		var originalamount = parseFloat((qty*cost)).toFixed(numberOfDecimal);
		$(this).find('input[name=amount]').val(originalamount);
	}
	
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

var shippingcost= $("input[name='shippingcost']").val();
shippingcost = checkno(shippingcost);
shippingcost = ((parseFloat(shippingcost))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
shippingcost = parseFloat(shippingcost).toFixed(numberOfDecimal);
$("input[name ='shippingcost']").val(shippingcost);
$("input[name ='oShippingcost']").val(shippingcost);


var adjustment= $("input[name ='adjustment']").val();
adjustment = checkno(adjustment);
adjustment = ((parseFloat(adjustment))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
adjustment = parseFloat(adjustment).toFixed(numberOfDecimal);
$("input[name ='adjustment']").val(adjustment);

$("input[name ='CURRENCYUSEQTOLD']").val(CURRENCYUSEQTCost);

calculateTotal();
}

function loadQtyToolTip(obj){
	$(obj).closest('tr').find("input[name=qty]").tooltip("destroy");
	var content = "<p class='text-left'>Reorder Level: "+$(obj).closest('tr').find("input[name=qty]").data("rl")+"</p>";
	content += "<p class='text-left'>Max Stock Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("msq")+"</p>";
	content += "<p class='text-left'>Stock on Hand: "+$(obj).closest('tr').find("input[name=qty]").data("soh")+"</p>";
	content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(obj).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
	content += "<p class='text-left'>Estimated Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("eq")+"</p>";
	content += "<p class='text-left'>Available Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("aq")+"</p>";	
	$(obj).closest('tr').find("input[name=qty]").tooltip({title: content, html: true, placement: "top"}); 
}

function loadUnitPriceToolTip(obj){
$(obj).closest('tr').find("input[name=cost]").tooltip("destroy");
	
	var minsp = parseFloat($(obj).closest('tr').find("input[name=minsp]").val());
	var customerdiscount = parseFloat($(obj).closest('tr').find("input[name=customerdiscount]").val());
	
	if(isNaN(minsp)){
		content = "<p class='text-left'>Minimum Selling Price: 0.000</p>";
	}else{
		content = "<p class='text-left'>Minimum Selling Price: "+minsp+"</p>";
	}
	if(isNaN(customerdiscount)){
		content += "<p class='text-left'>Customer Discount (By Price or Percentage): 0.000</p>";
	}else{
		content += "<p class='text-left'>Customer Discount (By Price or Percentage): "+customerdiscount+"</p>";
	}
	
	$(obj).closest('tr').find("input[name=cost]").tooltip({title: content, html: true, placement: "top"});
}

function changeitem(obj){
	var obj2 = $(obj).parent().find('input[name="item"]');
	$(obj2).typeahead('val', '');
	$(obj).parent().find('input[name="item"]').focus();
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


