var subWin = null;
var taxList = [];
var zerotype=0;
var typeSwitch=false;
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

var postatus =   [ 
{
	"year": "UNBILLED",
	"value": "UNBILLED",
	"tokens": [
		"UNBILLED"
	]
},{
	"year": "BILLED",
	"value": "BILLED",
	"tokens": [
		"BILLED"
	]
},
{
	"year": "PARTIALLY BILLED",
	"value": "PARTIALLY BILLED",
	"tokens": [
		"PARTIALLY BILLED"
	]
}];

$(document).ready(function(){
	var plant = document.form1.plant.value;
	var cmd = document.form1.cmd.value;
	var TranId = document.form1.TranId.value;
	
	if(cmd=="Edit")
	{
	if(TranId!="")
		{
		getEditDetail(TranId);
		}
	}else if(cmd=="Copy")
	{
		if(TranId!="")
			{
			getCopyDetail(TranId);
			}
		}
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	$('input[name ="cost"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	$('input[name ="item_discount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	$('input[name ="amount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	$("#subTotal").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#discount").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#shipping").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#adjustment").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#total").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	
/*	$(".datepicker").datepicker({
		format: "dd/mm/yyyy",
		autoclose: true,
		todayHighlight: true
	});*/
	
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
//		    return '<p onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\')">' + data.VNAME + '</p>';
		    return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYUSEQT+'\')"><p class="item-suggestion">Name: ' + data.VNAME 
		    + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME 
		    + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width(menuElement.width());
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
				document.form1.vendno.value = "";
				document.form1.nTAXTREATMENT.value ="";
				document.getElementById('nTAXTREATMENT').innerHTML="";
				$("input[name ='TAXTREATMENT_VALUE']").val("");
			}
			$('#nTAXTREATMENT').attr('disabled',false);
			if($('select[name ="nTAXTREATMENT"]').val() =="GCC VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="GCC NON VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="NON GCC")
			{
				document.getElementById('CHK1').style.display = 'block';
			}
			else
				document.getElementById('CHK1').style.display = 'none';
		}).on('typeahead:select',function(event,selection){
			if($(this).val() != "")
				checkorderno(document.form1.exbillno.value);
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
		    return '<p class="shipmentchange" onclick="GetShipmentPO(this,\''+data.PONO+'\',\''+data.CUSTNAME+'\',\''+data.CUSTCODE+'\')">' + data.PONO + '</p>';
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
			if($(this).val() == ""){
				document.form1.vendno.value = "";
			}
			/*var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "VIEW_SHIPMENT_SUMMARY_VIEW",
				ORDERNO : document.form1.pono.value
			},
			dataType : "json",
			success : function(data) {				
				if ( data.items.length == 0 ) {
					$("#shipment").typeahead('val', '"');
					$("#shipment").typeahead('val', '');
				}else{
					
					if(data.items[0].SHIPMENTCODE===undefined)						
						$("#shipment").typeahead('val', '');						
					else
						$("#shipment").typeahead('val', data.items[0].SHIPMENTCODE);					
				}
				
			}
			});*/
			
		}).on('typeahead:change',function(event,selection){
			
			
			/* To reset Autosuggestion*/
			
		});
	
	/* To get the suggestion data for Account */
	//loadSubAccount();
	//loadExpenseSubAccount();
	function loadSubAccount()
	{
		$.ajax({
			type : "POST",
			url: '/track/ChartOfAccountServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getSubAccountTypeGroupedFilter",
			},
			success : function(datasubitem) {
				var subAccountListFilter=datasubitem.results;
				var myJSON = JSON.stringify(subAccountListFilter);
				
				/* alert(myJSON); */
				$("#acc_subAcctPaidThr").select2({
						data:subAccountListFilter,
						dropdownCssClass : 'select2drop',
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
					/*$('#acc_subAcctPaidThr')
					    .select2()
					    .on('select2:open', () => {
					        $(".select2-results:not(:has(a))").append('<a href="#" style="padding: 6px;height: 20px;display: inline-table;">Create new item</a>');
					})*/
				
			}
		});
	}
	
	 $('#acc_subAcctPaidThr').select2().on('select2:open', function () {
         var a = $(this).data('select2');
         if (!$('.select2-link').length) {
             a.$results.parents('.select2-results')
                     .append('<div class="accountAddBtn footer" data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>')
                     .on('click', function (b) {
                         a.trigger('close');

                         // add your code
                     });
   }
});
	function loadExpenseSubAccount()
	{
		$.ajax({
			type : "POST",
			url: '/track/ChartOfAccountServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "getSubAccountTypeGroup",
				module:"expenseaccount",
			},
			success : function(datasubitem) {
				var subAccountListExpense=datasubitem.results;
				var myJSON = JSON.stringify(subAccountListExpense);
				
				/* alert(myJSON); */
				$(".acc_subAcctExpense").select2({
						data:subAccountListExpense,
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
	$('.acc_subAcctExpense').select2().on('select2:open', function () {
        var a = $(this).data('select2');
        if (!$('.select2-link').length) {
            a.$results.parents('.select2-results')
                    .append('<div class="accountAddBtn footer" data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>')
                    .on('click', function (b) {
                        a.trigger('close');

                        // add your code
                    });
  }
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
				module:"expensepaiddrop",
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
		    	return '<div onclick="document.form1.CUST_CODE.value = \''+data.CUSTNO+'\'"> <p class="item-suggestion">Name: ' + data.CNAME + '</p> <br/> <p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p> <p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();  
			$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');
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
			setTimeout(function(){ $('.customerAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.CUST_CODE.value = "";
			}			
		}).on('typeahead:select',function(event,selection){
			$("#project").typeahead('val', '"');
			$("#project").typeahead('val', '');
			$("input[name=PROJECTID]").val('');
		});

	
	/* Shipment Auto Suggestion */
	$('#shipment').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{		  
		  display: 'SHIPMENTCODE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					action : "VIEW_SHIPMENT_SUMMARY_VIEW",
					ORDERNO : document.form1.pono.value,
					SHIPMENT_CODE : query
				},
				dataType : "json",
				success : function(data) {
					if(document.form1.pono.value!="")
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
		    /*return '<p onclick="Shipmentcheck(\''+data.PONO+'\',\''+data.SHIPMENTCODE+'\')">' + data.SHIPMENTCODE + '</p>';*/
		    	return '<p>' + data.SHIPMENTCODE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="shipmentAddBtn footer" onclick="Shippopup()"><a href="#"><p> + Add Shipment</p></a></div>');
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
	
	
	/* currency Auto Suggestion */
	$('#currency').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DISPLAY',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ExpensesServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_CURRENCY",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.currency);
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
		    	return '<div><p onclick="getCurrencyid(\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.DISPLAY+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			/*$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');*/
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
			if($(this).val() == "")
				$("input[name ='CURRENCYID']").val("");
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
	
	$(".bill-table tbody").on('click','.bill-action',function(){
		debugger;	    
	    var obj = $(this).closest('tr').find('td:nth-child(4)');
	    calculateTax(obj, "", "", "");
	    $(this).parent().parent().remove();
	    calculateTotal();
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
	
	$("#btnBillOpen").click(function(){
		
		var checkBox = document.getElementById("billable");
		 if (checkBox.checked == true){		 
			 $("input[name ='bill_status']").val('UNBILLED');
		 } else{		 
			 $("input[name ='bill_status']").val('NON-BILLABLE');
		 } 
		/*$('input[name ="bill_status"]').val('UNBILLED');*/
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		$("#createExpensesForm").submit();
	});
	
	$("#Expensetax").typeahead({
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
					SALESLOC : "",
					GST_PERCENTAGE : $("input[name=GST]").val(),
					TAXKEY : "EXPENSE",
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
			return '<p onclick="calculateTaxEXP(this,\''+data.SGSTTYPES+'\',\''+data.SGSTPERCENTAGE+'\',\''+data.DISPLAY+'\',\''+data.ISZERO+'\',\''+data.ISSHOW+'\',\''+data.ID+'\')">' 
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
		var CURRENCYUSEQTCost = checkno($("input[name ='CURRENCYUSEQT']").val());
		$("input[name ='CURRENCYUSEQT']").val(CURRENCYUSEQTCost);
		if(!Number.isNaN(CURRENCYUSEQTCost))
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});
	
	addSuggestionToTable();
});

function addRow(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var taxdisplay = $("input[name=ptaxdisplay]").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var body="";
	body += '<tr>';
	body += '<td class="bill-acc">';
	body += '<input type="hidden" name="isexptax" value="0">';
	body += '<input type="text" name="expenses_account_name" class="form-control expensesaccountSearch" placeholder="Select Account">';
	body += '</td>';
	body += '<td class="col-sm-6 notes-sec">';
	body += '<textarea rows="2" name="note" class="ember-text-area form-control ember-view" maxlength="300"></textarea>';
	body += '</td>';
	body += '<td class="item-amount text-right">';	
	body += '<input name="amount" type="text" class="form-control text-right" value="'+szero+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
	body += '<td class="item-tax grey-bg" style="position:relative;">';
	body += '<input type="hidden" name="tax_type">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
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

function checkcustomer(customer){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CUST_CODE : customer,
				ACTION : "CUSTOMER_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Customer Does't Exists");
						document.getElementById("CUSTOMER").focus();
						$("#CUSTOMER").typeahead('val', '');
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
						document.getElementById("currency").focus();
						$("#currency").typeahead('val', '');
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
						document.getElementById("Expensetax").focus();
						$("#Expensetax").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
//jsp validation ends

//supplier popup changes
function checksupplierid(supvalue){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CUST_CODE : supvalue,
				ACTION : "SUPPLIER_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						alert("Supplier ID Already Exists");
						document.getElementById("CUST_CODE").focus();
						document.getElementById("CUST_CODE").value="";
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checksuppliertransport(transport){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				TRANSPORT_MODE : transport,
				ACTION : "TRANSPORT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Transport Does't Exists");
						document.getElementById("transports").focus();
						$("#transports").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checksupplierpaymenttype(paytype){	
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
						document.getElementById("SPAYTERMS").focus();
						$("#SPAYTERMS").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checksupplierpaymentterms(payterms){	
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
						document.getElementById("sup_payment_terms").focus();
						$("#sup_payment_terms").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checksupplierbank(bank){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				BANKNAME : bank,
				ACTION : "BANK_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Bank Does't Exists");
						document.getElementById("BANKNAME").focus();
						$("#BANKNAME").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
//supplier popup changes end

//customer popup changes
function checkcustomertransport(transport){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				TRANSPORT_MODE : transport,
				ACTION : "TRANSPORT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Transport Does't Exists");
						document.getElementById("transport").focus();
						$("#transport").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkcustomerpaymenttype(paytype){	
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
						document.getElementById("PAYTERMS").focus();
						$("#PAYTERMS").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkcustomerpaymentterms(payterms){	
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
						document.getElementById("cpayment_terms").focus();
						$("#cpayment_terms").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkcustomerbank(bank){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				BANKNAME : bank,
				ACTION : "BANK_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Bank Does't Exists");
						document.getElementById("BANKNAMECUS").focus();
						$("#BANKNAMECUS").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
//customer popup changes end

function addSuggestionToTable(){
	var plant = document.form1.plant.value;	
	
	/* To get the suggestion data for Expenses Account */
	$(".expensesaccountSearch").typeahead({
		input:".expensesaccountSearch",
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
				module:"expenseaccount",
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
	}).on('typeahead:select',function(event,selection){
			if($(this).val() == ""){
				
			}else{
				setexptax(this,selection.ISEXPENSEGST);
			}
			
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
				ACTION : "GET_GST_TYPE_DATA_EXPENSE",
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
		setTimeout(function(){ menuElement.next().hide();}, 180);
	});	*/
}

function removeSuggestionToTable(){
	$(".expensesaccountSearch").typeahead('destroy');
	$(".taxSearch").typeahead('destroy');
}


/*function calculateAmount(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	var item=$(obj);
	var taxrate=$(obj).closest('tr').find("td:nth-child(4)").find('input').val();
	//alert(taxrate);
	var display = taxrate.replace("%]","");
	var percentage = display.split("[");
	var numberOfDecimal = $("#numberOfDecimal").val();
	var taxpercentage=percentage[1];
	var taxtype=percentage[0].trim();
	typeSwitch=true;
	calculateTax(item,taxtype,taxpercentage,taxrate);
	
	var amount = parseFloat($(obj).closest('tr').find("td:nth-child(3)").find('input').val()).toFixed(numberOfDecimal);
	
	$(obj).closest('tr').find("td:nth-child(3)").find('input').val(amount);
	calculateTotal();
}

function calculateTotal(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = 0, totalvalue=0;
	var rowcount=$(".bill-table tbody tr").length;
	if(rowcount>0)
		{
			$(".bill-table tbody tr td:nth-child(3").each(function() {
			    amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
			    amount = parseFloat(amount).toFixed(numberOfDecimal);
			    $("#subTotal").html(amount);
			    $('input[name ="sub_total"]').val(amount);// hidden input
			});
		}
	else
	{
		amount=0;
		amount=parseFloat(amount).toFixed(numberOfDecimal);
		$("#subTotal").html(amount);
		$('input[name ="sub_total"]').val(amount);// hidden input
	}
	 
	 var taxTotal = 0;
	 if($(".taxDetails").html().length > 0){
		 $('.taxAmount').each(function(i, obj) {
			 taxTotal += parseFloat($(this).html());
		 });
	 }
	 
	 $("#total_tax_amount").val(taxTotal); // hidden input}
	 
	 totalvalue = parseFloat(parseFloat(amount) + parseFloat(taxTotal)).toFixed(numberOfDecimal);
	 $("#total").html(totalvalue);
	 $("#total_amount").val(totalvalue); // hidden input}
}*/
function validDecimal(str) {
	if (str.indexOf('.') == -1) str += ".";
	var decNum = str.substring(str.indexOf('.')+1, str.length);
	var declength =	parseInt(document.getElementById("numberOfDecimal").value);
	if (decNum.length > declength)
	{
		return false;
		
	}
	return true;
}

function calculateTax(obj, types, percentage, display){
	if(!typeSwitch)
	{
		if(types=="ZERO RATE")
		{
			zerotype++;
		}
	}
	typeSwitch=false;
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
}

function renderTaxDetails(){
	var html="";
	var taxTotal = 0;
	var numberOfDecimal = $("#numberOfDecimal").val();
	$.each(taxList, function( key, data ) {
		var mydata=JSON.stringify(data);
		console.log("Tax dta:"+mydata);
		var originalTaxType= data.types;
		console.log("Zero Type:"+zerotype);
		if(data.value > 0 || originalTaxType=="ZERO RATE" && zerotype>0){
			html+='<div class="total-row">';
			html+='<div class="total-label">'+data.display+'</div>';
			html+='<div class="total-amount taxAmount">'+data.value+'</div>';
			html+='</div>';
			taxTotal += parseFloat(data.value);
		}
	});
	$(".taxDetails").html(html);
	calculateTotal();
	 
	}

function checkval()
{
	 var checkBox = document.getElementById("expenses_for_PO");
	 if (checkBox.checked == true){		 
		 document.form1.expenses_for_PO.value="1";
		 document.getElementById('pono').disabled  = false;
		 document.getElementById('shipment').disabled  = false;
		 /*document.getElementById('vendname').disabled  = true;
		 $('#vendname').css("background-color", "#EEEEEE");*/
	 }
	 else
		 {		 
		 document.form1.expenses_for_PO.value="0";
		 document.form1.pono.value="";
		 document.form1.shipment.value="";
		 document.form1.vendname.value="";
		 document.form1.vendno.value="";
		    document.getElementById('pono').disabled  = true;
		    document.getElementById('shipment').disabled  = true;
		    /*document.getElementById('vendname').disabled  = false;
		    $('#vendname').css("background-color", "transparent");*/
		 }
	 
}

function successAccountCallback(accountData){
	if(accountData.STATUS="SUCCESS"){
		alert(accountData.MESSAGE);
		$("input[name ='paid_through_account_name']").typeahead('val', accountData.ACCOUNT_NAME);
	}
}

function successExpenseAccountCallback(accountData){
	if(accountData.STATUS="SUCCESS"){
		alert(accountData.MESSAGE);
		//$("input[name ='expenses_account_name']").typeahead('val', accountData.ACCOUNT_TYPE);
	}
}

function shipmentCallback(shipmentData){
	if(shipmentData.STATUS="SUCCESS"){
		//alert(shipmentData.MESSAGE);
		var checkBox = document.getElementById("expenses_for_PO");
		 if (checkBox.checked == true){	
		$("input[name ='pono']").typeahead('val', shipmentData.PONO);
		$("input[name ='shipment']").typeahead('val', shipmentData.SHIPMENT_CODE);
		 }
	}
}

function gstCallback(data){
	if(data.STATUS="SUCCESS"){
		alert(data.MESSAGE);
		$("input[name ='tax_type']").typeahead('val', data.GST);
	}
}

function validateExpenses(){
	var checkBox = document.getElementById("expenses_for_PO");
	 if (checkBox.checked == true){	
	
		var pono = document.form1.pono.value;
		var shipment = document.form1.shipment.value;
		var cusno = document.form1.CUSTOMER.value;
		var vendname = document.form1.vendname.value;
		var CURRENCYUSEQT = document.form1.CURRENCYUSEQT.value;
		
		/*if(vendname == ""){
			alert("Please select a Supplier Name.");
			return false;
		}*/	
		
		var checkBox = document.getElementById("billable");
		 if (checkBox.checked == true){	
			 if(cusno == ""){
				 alert("Please select Customer.");
				 return false;
			 }
		 } 
		
		if(shipment == ""){
			if(pono == ""){
				alert("Please select PO Number.");
				return false;
			}else{
				alert("Please select a shipment.");
				return false;
			}
		}
		 
	 }
	var supplier = document.form1.vendno.value;
	var cmd = document.form1.cmd.value;
	if(cmd!="Edit")
	{
	checkorderno(document.form1.exbillno.value);
	}
	var paid_through = document.form1.paid_through_account_name.value;
	var expenses_date = document.form1.expenses_date.value;
	var Currency = document.form1.currency.value;
	var isItemValid = true, isAccValid = true;
	var suptamt = $("input[name=SUPPLIER_TAMOUNT]").val();
	var valisuptamt = $("input[name=total_amount]").val();
	
	if($("#paid").is(':checked')){
		if(paid_through == ""){
			alert("Please select a paid_through.");
			return false;
		}	
	}
	if($("#paid").is(':checked')==false){
		if(supplier == ""){
			alert("Please select a Supplier.");
			return false;
		}
		$("#paid_through_account_name").val("");
	}
	
		if(supplier != ""){
		
		if(parseFloat(valisuptamt) == parseFloat(suptamt)){	
		}else{
			alert("Supplier Invoice Amount And AP Document Amount Does't Match");
			document.getElementById("SUPPLIER_TAMOUNT").focus();
			return false;
		}
		
		}
	
	
	if(expenses_date == ""){
		alert("Please enter a valid expenses date.");
		return false;
	}
	if(Currency == ""){
		alert("Please select a Currency.");
		return false;
	}
	
	if(CURRENCYUSEQT == ""){
		alert("Please select a Exchange Rate.");
		return false;
	}
	
	
	$("input[name ='expenses_account_name']").each(function() {
	    if($(this).val() == ""){	    	
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The Expenses Account field cannot be empty.");
		return false;
	}	
	
	$("input[name ='amount']").each(function() {
		if($(this).val() <= 0){
			isItemValid =  false;
	    }
	});	
	
	if(!isItemValid){
		alert("Amount cannot be Zero ");
		return false;
	}
	return true;
}
function addUserRow() {

	var body = "";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden"name="customeruserid" value="0">';
	body += '<input class="form-control text-left" name="USER_NAME" type="text" placeholder="Enter User Name" maxlength="50" autocomplete="off" ></td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" name="USER_HPNO" type="text" placeholder="Enter User Phone No" maxlength="30" onkeypress="return isNumber(event)" autocomplete="off" ></td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" name="USER_EMAIL" type="text" placeholder="Enter User Email" maxlength="50" autocomplete="off" ></td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" type="text" name="USER_ID" placeholder="Enter User id" maxlength="100" autocomplete="off"  onchange="checkuser(this.value)">';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
//	body += '<span class="glyphicon glyphicon-remove-circle user-action" aria-hidden="true"></span>';    	
	body += '<input  name="PASSWORD" class="form-control text-left" maxlength="60" placeholder="Enter Password" type="password" autocomplete="off">';
	body += '<span class="input-group-btn phideshow" onclick="javascript:viewpassword(this);return false;" >';
    body += '<button class="btn btn-default reveal" type="button" style="background: white;"><i class="fa fa-fw fa-eye" id="peye" style="color: #23527c;"></i></button>';
    body += '</span>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle user-action" aria-hidden="true" style="right: -95px;"></span>';    	
	body += '<input type="hidden" name="MANAGER_APP_VAL" value="0">';
	body += '<input type="Checkbox" style="border:0;background=#dddddd"	name="MANAGER_APP" value="1" onclick="checkManagerApp(this)">';
	body += '</td>';
	body += '</tr>';
	$(".user-table tbody").append(body);
}

function Shipmentcheck(pono, shipmentcode){
	var plant = document.form1.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/ExpensesServlet",
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_EXPENSEHDRID",
			SHIPMENT_CODE : shipmentcode,
			PONO : pono
		},
		dataType : "json",
		success : function(data) {
			console.log(data);
			if(data.expense[0].length != 0){
				/*if(data.expense[0].STATUS == "BILLED"){
					alert("Shipping Reference "+shipmentcode+" processed already");
					$("input[name ='shipment']").val("");
					loadEmptyTable();
				}else{
					
					$("input[name ='cmd']").val("Edit");
					$("input[name ='TranId']").val(data.expense[0].ID);		
					
					getEditDetail(data.expense[0].ID);
					
				}*/
				$("#shipment").typeahead('val', '"');
				$("#shipment").typeahead('val', '');
				alert("Shipment Code already processed");
				loadEmptyTable();
			}else{
				loadEmptyTable();
			}
			
		}
	});
}

function getEditDetail(TranId){
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
			loadEditTable(data.orders);
		}
	});
}

function loadEditTable(orders){
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
		/*var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");*/
		var numberOfDecimal = $("#numberOfDecimal").val();
	/*	tax.types = percentage[0].trim();
		tax.percentage = percentage[1];
		tax.display = data.TAX_TYPE;
		var taxpercentage=percentage[1];
		var taxtype=percentage[0].trim();*/
		var amt= parseFloat((data.AMOUNT*data.CURRENCYTOBASE)).toFixed(numberOfDecimal);
		/*if(taxtype=="ZERO RATE")
		{
			zerotype++;
		}*/
		
		body += '<tr>';
		
		body += '<td class="bill-acc">';
		body += '<input type="hidden" name="isexptax" value="'+data.ISEXPENSEGST+'">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"><input type="text" name="expenses_account_name" class="form-control expensesaccountSearch" value="'+data.EXPENSES_ACCOUNT+'" placeholder="Select Account">';
		body += '</td>';
		body += '<td class="col-sm-6 notes-sec">';
		/*body += '<input name="note" type="textarea" class="ember-text-area form-control ember-view" value="'+data.DESCRIPTION+'">';*/
		
		body += '<textarea rows="2" name="note" class="ember-text-area form-control ember-view" maxlength="300">'+data.DESCRIPTION+'</textarea>';
		
		body += '</td>';
		body += '<td class="item-amount text-right">';	
		body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-tax grey-bg" style="position:relative;">';
		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		//body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+data.TAX_TYPE+'">';
		/*if(data.TAX_TYPE=="EXEMPT[0.0%]")
		{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="EXEMPT">';
		}
		else if(data.TAX_TYPE=="OUT OF SCOPE[0.0%]")
			{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="OUT OF SCOPE">';
			}
		else
			{
			if(data.TAX_TYPE == ""){
				body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax">';
			}else{
				body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'">';
			}
			
			}*/
		body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly>';
		body += '</td>';
		body += '</tr>';		

		var amount = (data.AMOUNT*data.CURRENCYTOBASE);		

		//$("input[name ='CUST_CODE']").val(data.CUST_CODE);
		document.form1.CUST_CODE.value=data.CUST_CODE;
		$("input[name ='CUSTOMER']").val(data.CUSTOMER);
		$("input[name ='vendno']").val(data.VENDNO);
		$("input[name ='vendname']").val(data.VNAME);		
		$("input[name ='shipment']").val(data.SHIPMENT_CODE);
		$("input[name ='pono']").val(data.ORDERNO);
		$("input[name ='bill']").val(data.BILL);
		
		$("input[name ='OUTCODE']").val(data.OUTLET);
		$("input[name ='TERMINALCODE']").val(data.TERMINAL);
		$("input[name ='OUTLET_NAME']").val(data.OUTLETNAME);
		$("input[name ='TERMINALNAME']").val(data.TERMINALNAME);
		
		if(data.BILL != ""){
			$("#hpurord").hide();
		    $("#hbill").show();
		}
		$("input[name ='expenses_date']").val(data.EXPENSES_DATE);		
		$("input[name ='paid_through_account_name']").val(data.PAID_THROUGH);
		$("input[name ='reference']").val(data.REFERENCE);
		$("input[name ='currency']").val(data.DISPLAY);
		$("#shipmentModal #shipmentpono").val(data.ORDERNO);
		$('select[name ="nTAXTREATMENT"]').val(data.sTAXTREATMENT);
		$("input[name ='total_tax_amount']").val(data.TAXAMOUNT*data.CURRENCYTOBASE);
		$("input[name ='CURRENCYUSEQT']").val(parseFloat(data.CURRENCYTOBASE).toFixed(numberOfDecimal));
		$("input[name ='CURRENCYUSEQTOLD']").val(parseFloat(data.CURRENCYTOBASE).toFixed(numberOfDecimal));
		$("input[name ='CURRENCYID']").val(data.CURRENCYID);
		$("input[name ='exbillno']").val(data.EXBILL);
		var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
		document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+data.CURRENCYID+")";
		document.getElementById('lbltotal').innerHTML = "Total ("+data.CURRENCYID+")"; //Author: Azees  Create date: July 29,2021  Description:  Total of Local Currency
		if(basecurrency!=data.CURRENCYID)
			document.getElementById('showtotalcur').style.display = 'block';	
		else
			document.getElementById('showtotalcur').style.display = 'none';
		$("input[name ='ptaxpercentage']").val(data.STANDARDTAX);
		$("input[name ='GST']").val(parseFloat(data.STANDARDTAX).toFixed(3));

		$("input[name ='EXGST']").val(parseFloat(data.EXPENSETAX).toFixed(3));
		$("input[name ='taxid']").val(data.TAXID);
		if(data.ISEXPENSEGST == "0"){
			$("input[name ='Expensetax']").val(data.TAX_TYPE);
			$("input[name ='ptaxdisplay']").val(data.TAX_TYPE);
		}
		$("input[name ='PROJECTID']").val(data.PROJECTID);
		$("input[name ='project']").val(data.PROJECTNAME);
		document.getElementById('currency').disabled  = true;
		
		$('#currency').css("background-color", "#eeeeee");
		$('#nTAXTREATMENT').attr('disabled',false);
		if(data.sTAXTREATMENT =="GCC VAT Registered"||data.sTAXTREATMENT=="GCC NON VAT Registered"||data.sTAXTREATMENT=="NON GCC")
		{
			document.getElementById('CHK1').style.display = 'block';
		}
		else
			document.getElementById('CHK1').style.display = 'none';
		
		document.form1.REVERSECHARGE.value=data.sREVERSECHARGE;
		if(data.sREVERSECHARGE=="1")
			document.getElementById("REVERSECHARGE").checked = true;
		else
			document.getElementById("REVERSECHARGE").checked = false;
		
		document.form1.GOODSIMPORT.value=data.sREVERSECHARGE;
		if(data.sGOODSIMPORT=="1") 
			document.getElementById("GOODSIMPORT").checked = true;
		else
			document.getElementById("GOODSIMPORT").checked = false;
		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
		if(data.ISPAID!=null && data.ISPAID=="1" ){
			document.getElementById("paid").checked=true;
			$("#paidthroughfield").show();
		}
		if(data.SHIPMENT_CODE!="")
			{
	    document.getElementById("expenses_for_PO").checked = true;
	    document.form1.expenses_for_PO.value="1";
		 document.getElementById('pono').disabled  = false;
		 document.getElementById('shipment').disabled  = false;
		 document.getElementById('vendname').disabled  = false;
		
			}
		if(data.ISBILLABLE == 1){
			 document.getElementById("billable").checked = true;
		}
		
		$('#expenses_for_PO').prop('readonly', true);
		
		$('#pono').prop('readonly', true);
		 
	});
	$(".bill-table tbody").html(body);
	renderTaxDetails();
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

function loadEmptyTable(){
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	var  zerovalue = parseFloat("0.0").toFixed(numberOfDecimal);
	var body="";
	
		body += '<tr>';
		
		body += '<td class="bill-acc">';
		body += '<input type="hidden" name="DETID" class="form-control" value=""><input type="text" name="expenses_account_name" class="form-control expensesaccountSearch" value="" placeholder="Select Account">';
		body += '</td>';
		body += '<td class="col-sm-6 notes-sec">';
		body += '<textarea rows="2" name="note" class="ember-text-area form-control ember-view" maxlength="300"></textarea>';
		body += '</td>';
		body += '<td class="item-amount text-right">';	
		body += '<input name="amount" type="text" class="form-control text-right" value="'+zerovalue+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-tax grey-bg" style="position:relative;">';
		body += '<input type="hidden" name="tax_type" value="" class="form-control">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="">';
		body += '</td>';
		body += '</tr>';		
		

		
		$("input[name ='CUST_CODE']").val("");
		$("input[name ='CUSTOMER']").val("");
		$("input[name ='vendno']").val("");
		$("input[name ='vendname']").val("");		
		/*$("input[name ='expenses_date']").val("");*/		
		$("input[name ='paid_through_account_name']").val("");
		$("input[name ='reference']").val("");
		/*$("input[name ='currency']").val("");*/
		document.form1.nTAXTREATMENT.value ="";
		document.getElementById('nTAXTREATMENT').innerHTML="";
		$("input[name ='TAXTREATMENT_VALUE']").val("");
		$('#nTAXTREATMENT').attr('disabled',false);
		if($('select[name ="nTAXTREATMENT"]').val() =="GCC VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="GCC NON VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="NON GCC")
		{
			document.getElementById('CHK1').style.display = 'block';
		}
		else
			document.getElementById('CHK1').style.display = 'none';

		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
		/*if(data.SHIPMENT_CODE!="")
			{
	    document.getElementById("expenses_for_PO").checked = true;
	    document.form1.expenses_for_PO.value="1";
		 document.getElementById('pono').disabled  = false;
		 document.getElementById('shipment').disabled  = false;
		 document.getElementById('vendname').disabled  = true;
		 $('#vendname').css("background-color", "#EEEEEE");
			}
		if(data.ISBILLABLE == 1){
			 document.getElementById("billable").checked = true;
		}*/
		 

	$(".bill-table tbody").html(body);
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
}

$( "#shipmentchange" ).click(function() {
	$("input[name ='shipment']").val(data.items[0].SHIPMENTCODE);
	$("#shipment").find("input,button,textarea,select").attr("disabled", "disabled");

	});
function downloadFile(id,fileName)
{
	 var urlStrAttach = "/track/ExpensesServlet?ACTION=downloadAttachmentById&attachid="+id;
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
	var urlStrAttach = "/track/ExpensesServlet?ACTION=removeAttachmentById&removeid="+id;	
	$.ajax( {
		type : "POST",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
}
function GetShipmentPO(obj, pono,custname,custcode){
	$("#shipmentModal #shipmentpono").val(pono);
	/*$("input[name ='vendname']").val(custname);
	$("input[name ='vendno']").val(custcode);*/
}
function setSupplierData(suppierData){	
	$("input[name ='vendno']").val(suppierData.vendno);
	$("#vendname").typeahead('val', suppierData.vendname);
	$('select[name ="nTAXTREATMENT"]').val(suppierData.sTAXTREATMENT);
//	$('input[name ="CURRENCY"]').val(suppierData.CURRENCY);
//	$('input[name ="CURRENCYID"]').val(suppierData.sCURRENCY_ID);
//	$('input[name ="CURRENCYUSEQT"]').val(suppierData.CURRENCYUSEQT);
//	getCurrencyid(data.supplier[0].sCURRENCY_ID,data.supplier[0].CURRENCYUSEQT);
	$('#nTAXTREATMENT').attr('disabled',false);
	if(suppierData.sTAXTREATMENT =="GCC VAT Registered"||suppierData.sTAXTREATMENT=="GCC NON VAT Registered"||suppierData.sTAXTREATMENT=="NON GCC")
	{
		document.getElementById('CHK1').style.display = 'block';
	}
	else
		document.getElementById('CHK1').style.display = 'none';
}

function Shippopup(){
	var pono = document.form1.pono.value;
	$("#shipmentModal #shipmentpono").val(pono);
	$("#shipmentModal #shipmentCode").val("");
	$('#shipmentModal').modal('show');
}
//Author: Azees  Create date: July 14,2021  Description: Show Supplier Based Currency
function getvendname(TAXTREATMENT,VENDO,CURRENCYID,CURRENCY,CURRENCYUSEQT){
	//document.getElementById('nTAXTREATMENT').innerHTML = TAXTREATMENT;
	
	getCurrencyid(CURRENCYID,CURRENCYUSEQT);
	$("#currency").typeahead('val', CURRENCY);
	
	$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
	document.form1.vendno.value =VENDO;
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

	function OnChkTaxChange(TAXTREATMENT)
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


function getCopyDetail(TranId){
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
			loadCopyTable(data.orders);
		}
	});
}

function loadCopyTable(orders){
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
		var numberOfDecimal = $("#numberOfDecimal").val();
		var amt= parseFloat((data.AMOUNT*data.CURRENCYTOBASE)).toFixed(numberOfDecimal);

		body += '<tr>';
		
		body += '<td class="bill-acc">';
		body += '<input type="hidden" name="isexptax" value="'+data.ISEXPENSEGST+'">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"><input type="text" name="expenses_account_name" class="form-control expensesaccountSearch" value="'+data.EXPENSES_ACCOUNT+'" placeholder="Select Account">';
		body += '</td>';
		body += '<td class="col-sm-6 notes-sec">';
		/*body += '<input name="note" type="textarea" class="ember-text-area form-control ember-view" value="'+data.DESCRIPTION+'">';*/
		
		body += '<textarea rows="2" name="note" class="ember-text-area form-control ember-view" maxlength="300">'+data.DESCRIPTION+'</textarea>';
		
		body += '</td>';
		body += '<td class="item-amount text-right">';	
		body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-tax grey-bg" style="position:relative;">';
		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		//body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+data.TAX_TYPE+'">';
		/*if(data.TAX_TYPE=="EXEMPT[0.0%]")
		{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="EXEMPT">';
		}
		else if(data.TAX_TYPE=="OUT OF SCOPE[0.0%]")
			{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="OUT OF SCOPE">';
			}
		else
			{
			if(data.TAX_TYPE == ""){
				body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax">';
			}else{
				body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'">';
			}
			
			}*/
		body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'">';
		body += '</td>';
		body += '</tr>';		
		

		
		
		var amount = (data.AMOUNT*data.CURRENCYTOBASE);		
		
		//$("input[name ='CUST_CODE']").val(data.CUST_CODE);
		document.form1.CUST_CODE.value=data.CUST_CODE;
		$("input[name ='CUSTOMER']").val(data.CUSTOMER);
		$("input[name ='vendno']").val(data.VENDNO);
		$("input[name ='vendname']").val(data.VNAME);		
		$("input[name ='shipment']").val(data.SHIPMENT_CODE);
		$("input[name ='pono']").val(data.ORDERNO);
		$("input[name ='expenses_date']").val(data.EXPENSES_DATE);		
		$("input[name ='paid_through_account_name']").val(data.PAID_THROUGH);
		$("input[name ='reference']").val(data.REFERENCE);
		$("input[name ='currency']").val(data.DISPLAY);
		$("#shipmentModal #shipmentpono").val(data.ORDERNO);
		$('select[name ="nTAXTREATMENT"]').val(data.sTAXTREATMENT);
		$("input[name ='total_tax_amount']").val(data.TAXAMOUNT*data.CURRENCYTOBASE);
		$("input[name ='CURRENCYUSEQT']").val(parseFloat(data.CURRENCYTOBASE).toFixed(numberOfDecimal));
		$("input[name ='CURRENCYUSEQTOLD']").val(parseFloat(data.CURRENCYTOBASE).toFixed(numberOfDecimal));
		$("input[name ='CURRENCYID']").val(data.CURRENCYID);
		var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
		document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+data.CURRENCYID+")";
		document.getElementById('lbltotal').innerHTML = "Total ("+data.CURRENCYID+")"; //Author: Azees  Create date: July 29,2021  Description:  Total of Local Currency
		if(basecurrency!=data.CURRENCYID)
			document.getElementById('showtotalcur').style.display = 'block';	
		else
			document.getElementById('showtotalcur').style.display = 'none';
		$("input[name ='ptaxpercentage']").val(data.STANDARDTAX);
		//$("input[name ='GST']").val(parseFloat(data.STANDARDTAX).toFixed(3));
		$("input[name ='GST']").val(parseFloat(data.STANDARDTAX).toFixed(3));

		$("input[name ='EXGST']").val(parseFloat(data.EXPENSETAX).toFixed(3));
		$("input[name ='taxid']").val(data.TAXID);
		if(data.ISEXPENSEGST == "0"){
			$("input[name ='Expensetax']").val(data.TAX_TYPE);
			$("input[name ='ptaxdisplay']").val(data.TAX_TYPE);
		}
		$("input[name ='PROJECTID']").val(data.PROJECTID);
		$("input[name ='project']").val(data.PROJECTNAME);
		
		//document.getElementById('currency').disabled  = true;
		
		//$('#currency').css("background-color", "#eeeeee");
		$('#nTAXTREATMENT').attr('disabled',false);
		if(data.sTAXTREATMENT =="GCC VAT Registered"||data.sTAXTREATMENT=="GCC NON VAT Registered"||data.sTAXTREATMENT=="NON GCC")
		{
			document.getElementById('CHK1').style.display = 'block';
		}
		else
			document.getElementById('CHK1').style.display = 'none';
		
		document.form1.REVERSECHARGE.value=data.sREVERSECHARGE;
		if(data.sREVERSECHARGE=="1")
			document.getElementById("REVERSECHARGE").checked = true;
		else
			document.getElementById("REVERSECHARGE").checked = false;
		
		document.form1.GOODSIMPORT.value=data.sREVERSECHARGE;
		if(data.sGOODSIMPORT=="1") 
			document.getElementById("GOODSIMPORT").checked = true;
		else
			document.getElementById("GOODSIMPORT").checked = false;
		if(data.ISPAID!=null && data.ISPAID=="1" ){
			document.getElementById("paid").checked=true;
			$("#paidthroughfield").show();
		}
		if(data.SHIPMENT_CODE!="")
			{
	    document.getElementById("expenses_for_PO").checked = true;
	    document.form1.expenses_for_PO.value="1";
		 document.getElementById('pono').disabled  = false;
		 document.getElementById('shipment').disabled  = false;
		 document.getElementById('vendname').disabled  = false;
		
			}
		
		if(data.ISBILLABLE == 1){
			 document.getElementById("billable").checked = true;
			 $("input[name ='billable_status']").val("1");
		}
		
		$('#expenses_for_PO').prop('readonly', true);
		
		$('#pono').prop('readonly', true);
		 
	});
	$(".bill-table tbody").html(body);
	calculateTotal();
	removeSuggestionToTable();
	addSuggestionToTable();
}

function getCurrencyid(CURRENCYID,CURRENCYUSEQT){
	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	setCURRENCYUSEQT(CURRENCYUSEQT);
	var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
	document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi

	document.getElementById('lbltotal').innerHTML = "Total ("+CURRENCYID+")"; //  Author: Resvi  Add date: July 28,2021  Description:  Total of Local Currency
	if(basecurrency!=CURRENCYID)
		document.getElementById('showtotalcur').style.display = 'block';
	else
		document.getElementById('showtotalcur').style.display = 'none';
}

function calculateTaxEXP(obj, types, percentage, display, iszero, isshow, id){
	$("input[name=ptaxtype]").val(types);
	$("input[name=ptaxpercentage]").val(percentage);
	$("input[name=ptaxdisplay]").val(display);
	$("input[name=ptaxiszero]").val(iszero);
	$("input[name=ptaxisshow]").val(isshow);
	$("input[name=taxid]").val(id);
	$('.taxSearch').each(function(){
		var isexgst = $(this).parent().parent().find('input[name=isexptax]').val();
		if(isexgst == "0"){
	    	$(this).val(display);
	    }
	});
	calculateTotal();
}


function calculateAmount(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = parseFloat($(obj).closest('tr').find("td:nth-child(3)").find('input').val()).toFixed(numberOfDecimal);
	$(obj).closest('tr').find("td:nth-child(3)").find('input').val(amount);
	calculateTotal();
}

function calculateTotal(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var ptaxiszero = $("input[name='ptaxiszero']").val();
	var ptaxisshow = $("input[name='ptaxisshow']").val();
	var ptaxpercentage = $("input[name='ptaxpercentage']").val();
	var petaxpercentage = $("input[name='EXGST']").val();
	var ptaxdisplay = $("input[name='ptaxdisplay']").val();
	
	var amount = 0, totalvalue=0 , tvalue=0, etvalue=0, acpayable=0;
	var rowcount=$(".bill-table tbody tr").length;
	if(rowcount>0)
		{
			$(".bill-table tbody tr td:nth-child(3").each(function() {
			    amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
			    amount = parseFloat(amount).toFixed(numberOfDecimal);
			    $("#subTotal").html(amount);
			    $('input[name ="sub_total"]').val(amount);// hidden input
			    var isexptax = $(this).parent().find('input[name=isexptax]').val();
			    if(isexptax == "0"){
					tvalue = parseFloat(tvalue) + parseFloat($(this).find('input').val());
				}else{
					etvalue = parseFloat(etvalue) + parseFloat($(this).find('input').val());
				}	
				
				if($(this).parent().find('input[name=expenses_account_name]').val() == "Account Payable"){
					acpayable=parseFloat(acpayable) + parseFloat($(this).find('input').val());
				}		    
			});
		}
	else
	{
		amount=0;
		amount=parseFloat(amount).toFixed(numberOfDecimal);
		$("#subTotal").html(amount);
		$('input[name ="sub_total"]').val(amount);// hidden input
	}
	 
	var taxTotal = 0;
	var tTotal = 0;
	var eTotal = 0;

	if(ptaxiszero == "0" && ptaxisshow == "1"){
		tTotal = parseFloat((tvalue/100)*ptaxpercentage);
		taxTotal = taxTotal + tTotal;
	}

	 if(ptaxisshow == "1"){
			var html ="";
			html+='<div class="total-row">';
			html+='<div class="total-label">'+ptaxdisplay+'</div>';
			html+='<div class="total-amount taxAmount">'+parseFloat(parseFloat(tTotal)).toFixed(numberOfDecimal)+'</div>';
			html+='</div>';
			$(".taxDetails").html(html);
			$("#total_tax_amount").val(tTotal); // hidden input}
		}else{
			$(".taxDetails").html("");
			$("#total_tax_amount").val("0"); // hidden input}
		}
		
		var dexgst = 0.0;
		var newtaxdisplay="";
		if(petaxpercentage.length != 0){ 
			dexgst =  parseFloat(petaxpercentage).toFixed("1");
			newtaxdisplay="STANDARD RATED ["+dexgst+"%]";
			
			eTotal = parseFloat((etvalue/100)*dexgst);
			taxTotal = taxTotal + eTotal;
			var html ="";
			html+='<div class="total-row">';
			html+='<div class="total-label">'+newtaxdisplay+'</div>';
			html+='<div class="total-amount etaxAmount">'+parseFloat(parseFloat(eTotal)).toFixed(numberOfDecimal)+'</div>';
			html+='</div>';
			$(".etaxDetails").html(html);
			$("#total_etax_amount").val(eTotal); // hidden input}
		}else{
			newtaxdisplay="STANDARD RATED [0.0%]";
			
			$(".etaxDetails").html("");
			$("#total_etax_amount").val("0"); // hidden input}
		}
	 
	 
	 if(acpayable != 0){
			var html1 ="";
			html1+='<div class="total-row">';
			html1+='<div class="total-label">Account Payable</div>';
			html1+='<div class="total-amount">'+parseFloat(parseFloat(acpayable)).toFixed(numberOfDecimal)+'</div>';
			html1+='</div>';
			$(".accountpayable").html(html1);
	}else{
		$(".accountpayable").html("");
	}
	 
	 totalvalue = parseFloat(parseFloat(amount) + parseFloat(taxTotal)).toFixed(numberOfDecimal);
	 $("#total").html(totalvalue);
	 $("#total_amount").val(totalvalue); // hidden input}
	 
	 var CURRENCYUSEQT = $('input[name ="CURRENCYUSEQT"]').val();//Author: Resvi  Add date: July 28,2021  Description:  Total of Local Currency
	 var convttotalvalue= (totalvalue/CURRENCYUSEQT).toFixed(numberOfDecimal)
	 $("#totalcur").html(convttotalvalue);
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


function taxreset(){
	$("input[name=ptaxtype]").val("");
	$("input[name=ptaxpercentage]").val("0");
	$("input[name=ptaxdisplay]").val("");
	$("input[name=ptaxiszero]").val("1");
	$("input[name=ptaxisshow]").val("0");
	$("input[name=taxid]").val("0");
	$('.taxSearch').each(function(){
		var isexgst = $(this).parent().parent().find('input[name=isexptax]').val();
		if(isexgst == "0"){
			$(this).val("");
		}else{
			var exgst = $("input[name=EXGST]").val();
			var dexgst = 0.0;
			var newtaxdisplay="";
			if(exgst.length != 0){ 
				dexgst =  parseFloat(exgst).toFixed("1");
				newtaxdisplay="STANDARD RATED ["+dexgst+"%]";
			}else{
				newtaxdisplay="STANDARD RATED [0.0%]";
			}
			$(this).val(newtaxdisplay);
		}
	});
	$("#Expensetax").typeahead('val', '');
	calculateTotal();
}

function etaxreset(){
	$('.taxSearch').each(function(){
		var isexgst = $(this).parent().parent().find('input[name=isexptax]').val();
		if(isexgst == "0"){
		}else{
			var exgst = $("input[name=EXGST]").val();
			var dexgst = 0.0;
			var newtaxdisplay="";
			if(exgst.length != 0){ 
				dexgst =  parseFloat(exgst).toFixed("1");
				newtaxdisplay="STANDARD RATED ["+dexgst+"%]";
			}else{
				newtaxdisplay="STANDARD RATED [0.0%]";
			}
			$(this).val(newtaxdisplay);
		}
	});
	calculateTotal();
}

function removetaxdropdown(){
	$("#Expensetax").typeahead('destroy');
}
function addtaxdropdown(){
	var plant = document.form1.plant.value;
	$("#Expensetax").typeahead({
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
					SALESLOC : "",
					GST_PERCENTAGE : $("input[name=GST]").val(),
					TAXKEY : "EXPENSE",
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
			return '<p onclick="calculateTaxEXP(this,\''+data.SGSTTYPES+'\',\''+data.SGSTPERCENTAGE+'\',\''+data.DISPLAY+'\',\''+data.ISZERO+'\',\''+data.ISSHOW+'\',\''+data.ID+'\')">' 
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
}

function setCURRENCYUSEQT(CURRENCYUSEQTCost){
	var plant = document.form1.plant.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	var oldeqtcost = $('input[name ="CURRENCYUSEQTOLD"]').val();
	var cost = 0;
	$('.bill-table tbody tr').each(function () {
		cost = ((parseFloat($(this).find('input[name=amount]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
		cost = parseFloat(cost).toFixed(numberOfDecimal);
		$(this).find('input[name=amount]').val(cost);
	});
	
	$("input[name ='CURRENCYUSEQTOLD']").val(CURRENCYUSEQTCost);
	
	calculateTotal();
}

function checkno(amount){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var baseamount = amount;
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);;
	if(baseamount != ""){
		var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(baseamount.match(decimal) || baseamount.match(numbers)) 
		{ 
			return parseFloat(baseamount).toFixed(numberOfDecimal);
		}else{
			return zeroval;
		}
	}else{
		return zeroval;
	}
	
}

function setexptax(data,status){
	$(data).parent().parent().find('input[name=isexptax]').val(status);
	var exgst = $("input[name=EXGST]").val();
	if(status == "1"){
		//alert("status--------- "+status);
		//exgst += "%";
		//var newtaxdisplay=taxdisplay.replace(/\[(.+?)\]/g, "["+exgst+"]")
		var dexgst = 0.0;
		var newtaxdisplay="";
		if(exgst.length != 0){ 
			dexgst =  parseFloat(exgst).toFixed("1");
			newtaxdisplay="STANDARD RATED ["+dexgst+"%]";
		}else{
			newtaxdisplay="STANDARD RATED [0.0%]";
		}
		//var newtaxdisplay="STANDARD RATED ["+dexgst+"%]";
		$(data).parent().parent().parent().find('input[name=tax]').val(newtaxdisplay);
		
	}else{
		var taxdisplay = $("input[name=ptaxdisplay]").val();
		$(data).parent().parent().parent().find('input[name=tax]').val(taxdisplay);
	}
	calculateTotal();
}

function changingetaxpercentage(){
	var baseamount = $("#EXGST").val();
	var zeroval = parseFloat("0").toFixed(3);
	if(baseamount != ""){
		var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(baseamount.match(decimal) || baseamount.match(numbers)) 
		{ 
			$("#EXGST").val(parseFloat(baseamount).toFixed(3));	
		}else{
			$("#EXGST").val(zeroval);
			alert("Please Enter Valid Percentage");
		}
	}else{
		$("#EXGST").val(zeroval);
	}
	etaxreset();
}

function checkorderno(orderno){
	var vendno = document.form1.vendno.value;	
		var urlStr = "/track/ExpensesServlet";
		$.ajax( {
			type : "GET",
			url : urlStr,
			async : true,
			data : {
				ORDERNO : orderno,
				VENDNO : vendno,
				ACTION : "CHECK_BILL_SUPPLIER",
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						alert("Bill Number & Supplier Already Exists");
						document.getElementById("exbillno").focus();
						document.getElementById("exbillno").value="";
						document.form1.vendname.value="";
		 				document.form1.vendno.value="";
		 				$("#vendname").typeahead('val', '"');
						$("#vendname").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}