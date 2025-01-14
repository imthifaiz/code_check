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
	getNextCreditnote();
	var numberOfDecimal = $("#numberOfDecimal").val();
	$('input[name ="cost"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	$('input[name ="item_discount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	$('input[name ="amount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	$("#subTotal").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#discount").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#shipping").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#adjustment").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#total").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	/*var creditnotenum=$('#creditnote').val();
	if(creditnotenum=="")
		{
			$("#SALES_LOC").val("Abu Dhabi");
			document.form.STATE_PREFIX.value="AUH";
		}*/
	
	/*  Author: Azees  Create date: June 26,2021  Description: Typeahead Changes */
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
				ACTION : "GET_SUPPLIER_DATA_ACTIVE",
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
    	   return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\',\''+data.CURRENCY+'\',\''+data.PAYMENT_TERMS+'\',\''+data.PAY_TERMS+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')"><p class="item-suggestion">Name: ' + data.VNAME 
    	   + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME 
    	   + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}  
			menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"  onclick="document.form.custModal.value=\'cust\'"> + New Supplier</a></div>');
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
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.vendno.value = "";
				document.getElementById('nTAXTREATMENT').value = "";
				document.form.nTAXTREATMENT.value = "";
				$("input[name ='TAXTREATMENT_VALUE']").val("");
				$("input[name=PERSON_INCHARGE]").val("");
				$("input[name=TELNO]").val("");
				$("input[name=EMAIL]").val("");
				$("input[name=ADD1]").val("");
				$("input[name=ADD2]").val("");
				$("input[name=ADD3]").val("");
				$("input[name=REMARK2]").val("");
				$("input[name=ADD4]").val("");
				$("input[name=COUNTRY]").val("");
				$("input[name=ZIP]").val("");
				$("input[name=PAYMENTTYPE]").val("");
				$("input[name=transports]").val("");
						$('#shipbilladd').empty();
			}
			/* To reset Order number Autosuggestion*/
			$("#pono").typeahead('val', '"');
			$("#pono").typeahead('val', '');
			$('#nTAXTREATMENT').attr('disabled',false);
			$("#billno").typeahead('val', '"');
			$("#billno").typeahead('val', '');
			$("input[name ='GST']").val($("input[name ='TGST']").val());
			$("input[name ='GST']").prop('readonly', false);
			removetaxdropdown();
			addtaxdropdown();
			taxreset();
			$("#poreturn").typeahead('val', '"');
			$("#poreturn").typeahead('val', '');
			if($('select[name ="nTAXTREATMENT"]').val() =="GCC VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="GCC NON VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="NON GCC")
			{
				document.getElementById('CHK1').style.display = 'block';
			}
			else
				document.getElementById('CHK1').style.display = 'none';
		}).on('typeahead:select',function(event,selection){
				if($(this).val() == ""){
				$('#shipbilladd').empty();
				$('#shipbilladd').append('');
			}else{
				getvendname(selection.TAXTREATMENT,selection.VENDO,selection.CURRENCY,selection.PAYMENT_TERMS,selection.PAY_TERMS,selection.CURRENCYID,selection.CURRENCYUSEQT)
				$('#shipbilladd').empty();
				var addr = '<div><h5 style="font-weight: bold;">Billing Address</h5>';
				addr += '<p>'+selection.NAME+'</p>';
				addr += '<p>'+selection.ADDR1+' '+selection.ADDR2+'</p>';				
				addr += '<p>'+selection.ADDR3+' '+selection.ADDR4+'</p>';
				addr += '<p>'+selection.STATE+'</p>';
				addr += '<p>'+selection.COUNTRY+' '+selection.ZIP+'</p>';
				addr += '<p>'+selection.HPNO+'</p>';
				addr += '<p>'+selection.EMAIL+'</p>';
				addr += '</div>';
				$('#shipbilladd').append(addr);
				}
			
		});
	
	/* Supplier Auto Suggestion */
	$('#VNAME').typeahead({
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
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}  
			
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
			menuElement.next().remove();  
			/*if(displayCustomerpop == 'true'){
			$('.shipCustomer-section .tt-menu').after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#" onclick="document.form1.custModal.value=\'shipcust\'"> + New Cutomer</a></div>');
			}*/
			menuElement.width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.ProjectAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.ProjectAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			$("input[name=PROJECTID]").val(selection.ID);
		});
	
	/* Shipping Customer Auto Suggestion */
	$('#SHIPPINGCUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CNAME',
			//display: 'VNAME',
		  async: true,   
		  source: function (query, process,asyncProcess) {
			  var urlStr = "../MasterServlet";
				$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						ACTION : "GET_CUSTOMER_DATA_ACTIVE",
						//ACTION : "GET_SUPPLIER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CUSTMST);
						//return asyncProcess(data.VENDMST);
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
		    	return '<div onclick="document.form.SHIPPINGID.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
		    	//return '<div onclick="document.cpoform.SHIPPINGID.value = \''+data.VENDO+'\'"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Mobile: ' + data.HPNO + '</p><br/><p class="item-suggestion">Address: ' + data.ADDR1 + '  '+ data.ADDR2 +'</p><p class="item-suggestion pull-right">Customer TelNo: ' + data.TELNO + '</p></br><p class="item-suggestion"> ' + data.ADDR3 + '  '+ data.ADDR4 +'</p><p class="item-suggestion pull-right">Email: ' + data.EMAIL + '</p></br><p class="item-suggestion"> ' + data.COUNTRY + '  '+ data.ZIP +'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}  
			menuElement.after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal" onclick="document.form.custModal.value=\'shipcust\'"><a href="#" > + New Shipping Address</a></div>');
			//menuElement.after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#supplierModal" onclick="document.cpoform.custModal.value=\'shipcust\'"><a href="#" > + New Shipping Address</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });			
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
				document.form.SHIPPINGID.value = "";
				
				$("input[name=SHIPCONTACTNAME]").val("");
				$("input[name=SHIPDESGINATION]").val("");
				$("input[name=SHIPADDR1]").val("");
				$("input[name=SHIPADDR2]").val("");
				$("input[name=SHIPADDR3]").val("");
				$("input[name=SHIPADDR4]").val("");
				$("input[name=SHIPSTATE]").val("");
				$("input[name=SHIPZIP]").val("");
				$("input[name=SHIPWORKPHONE]").val("");
				$("input[name=TRANSPORTID]").val("");
				$("#transport").typeahead('val', '');
				$('#shipadd').empty();
			}
			}).on('typeahead:select',function(event,selection){
				if($(this).val() == ""){
				$('#shipadd').empty();
				$('#shipadd').append('');
			}else{
				$('#shipadd').empty();
				var addr = '<div><h5 style="font-weight: bold;">Shipping Address   <a><span data-toggle="modal" data-target="#shipaddr" onclick="getshipaddr();" style="font-size: 15px;font-weight: 450;">Change</span></a></h5>';  
//				<span data-toggle="modal" data-target="#shipaddr">Change</span> //old
				addr += '<div id="cshipaddr">';
				addr += '<p>'+selection.SHIPCONTACTNAME+'</p>';
				addr += '<p>'+selection.SHIPDESGINATION+'</p>';
				addr += '<p>'+selection.SHIPADDR1+' '+selection.SHIPADDR2+'</p>';				
				addr += '<p>'+selection.SHIPADDR3+' '+selection.SHIPADDR4+'</p>';	
				addr += '<p>'+selection.SHIPSTATE+'</p>';			
				addr += '<p>'+selection.SHIPCOUNTRY+' '+selection.SHIPZIP+'</p>';
				addr += '<p>'+selection.SHIPHPNO+'</p>';
				addr += '<p>'+selection.SHIPWORKPHONE+'</p>';
				addr += '<p>'+selection.SHIPEMAIL+'</p>';
				addr += '</div>';
				addr += '</div>';
				$('#shipadd').append(addr);
				
				$("input[name=SHIPCONTACTNAME]").val(selection.SHIPCONTACTNAME);
				$("input[name=SHIPDESGINATION]").val(selection.SHIPDESGINATION);
				$("input[name=SHIPADDR1]").val(selection.SHIPADDR1);
				$("input[name=SHIPADDR2]").val(selection.SHIPADDR2);
				$("input[name=SHIPADDR3]").val(selection.SHIPADDR3);
				$("input[name=SHIPADDR4]").val(selection.SHIPADDR4);
				$("input[name=SHIPSTATE]").val(selection.SHIPSTATE);
				$("input[name=SHIPZIP]").val(selection.SHIPZIP);
				$("input[name=SHIPWORKPHONE]").val(selection.SHIPWORKPHONE);
				$("input[name=SHIPCOUNTRY]").val(selection.SHIPCOUNTRY);
				$("input[name=SHIPHPNO]").val(selection.SHIPHPNO);
				$("input[name=SHIPEMAIL]").val(selection.SHIPEMAIL);				
				
			}
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
				poreturn:document.form.poreturn.value,
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
		    return '<div onclick="loadBillDet(\''+ data.ID+'\',\''+ data.PONO+'\',\''+ data.GRNO+'\',\''+ data.BILLFROM+'\',\''+ data.PURCHASE_LOCATION+'\');"><p class="item-suggestion">'+data.BILL+'</p></div>';
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
				$("input[name ='GST']").val($("input[name ='TGST']").val());
				$("input[name ='GST']").prop('readonly', false);
				removetaxdropdown();
				addtaxdropdown();
				taxreset();
			}else{
				$("input[name ='GST']").val($("input[name ='TGST']").val());
				$("input[name ='GST']").prop('readonly', true);
				removetaxdropdown();
				addtaxdropdown();
				taxreset();
			}		
			
		});
	
	/* Order Number Auto Suggestion */
	$('#poreturn').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PORETURN',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/SupplierCreditServlet?Submit=getporeturnnoByVendandcredit";
			$.ajax( {
			type : "GET",
			url : urlStr,
			async : true,
			data : {
				vendno:document.form.vendno.value,
				PORETURN_STATUS : "Not Applied",
				PLANT : plant
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.portno);
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
		    return '<p>' + data.PORETURN + '</p>';
		   /* return '<div onclick="loadBillDet(\''+ data.ID+'\',\''+ data.PONO+'\',\''+ data.GRNO+'\');"><p class="item-suggestion">'+data.BILL+'</p></div>';*/
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
			if($(this).val() == ""){
				document.form.poreturn.value = "";
				$("input[name ='poreturnstatus']").val('');
			}					
			$("#pono").typeahead('val', '"');
			$("#pono").typeahead('val', '');
			$("#billno").typeahead('val', '"');
			$("input[name ='GST']").val($("input[name ='TGST']").val());
			$("input[name ='GST']").prop('readonly', false);
			removetaxdropdown();
			addtaxdropdown();
			taxreset();
			$("#billno").typeahead('val', '');
			$("input[name ='grnno']").val('');
			$("input[name ='poreturnstatus']").val('poreturn');
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.poreturn.value = "";
			}					
			$("#pono").typeahead('val', '"');
			$("#pono").typeahead('val', '');
			$("#billno").typeahead('val', '"');
			$("#billno").typeahead('val', '');
			$("input[name ='GST']").val($("input[name ='TGST']").val());
			$("input[name ='GST']").prop('readonly', false);
			removetaxdropdown();
			addtaxdropdown();
			taxreset();
			$("input[name ='grnno']").val('');
			$("input[name ='poreturnstatus']").val('poreturn');
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
	/* Sales Location Auto Suggestion 
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
	$(".taxSearch").on('focus', function(e){
		$(".taxSearch").typeahead('val', '"');
		$(".taxSearch").typeahead('val', '');	
	});
	$(document).on("focus", ".taxSearch" , function() {
		$(this).typeahead('val', '"');
		$(this).typeahead('val', '');	
    });*/
	
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
		    	return '<p onclick="document.form.STATE_PREFIX.value = \''+data.STATE_PREFIX+'\'">' + data.STATE + '</p>';		    
			}
		  }
		}).on('typeahead:render',function(event,selection){
			
		  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			removetaxdropdown();
			addtaxdropdown();
			taxreset();
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
		var poreturn = document.form.poreturn.value;
		if(poreturn == ""){
			$("input[name ='poreturnstatus']").val('');
		}
		$('input[name ="bill_status"]').val('Draft');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		$("#createBillForm").submit();
	});
	$("#btnBillOpen").click(function(){
		var poreturn = document.form.poreturn.value;
		if(poreturn == ""){
			$("input[name ='poreturnstatus']").val('');
		}
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
	
	/* To get the suggestion data for Status */
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
		if(!Number.isNaN(CURRENCYUSEQTCost))		
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
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
	addSuggestionToTable();
	$("input[name ='vendname']").focus();
});

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}

function addRow(event){
	event.preventDefault();
    event.stopPropagation();
	var numberOfDecimal = $("#numberOfDecimal").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var curency = document.form.CURRENCYID.value;
	var taxdisplay = $("input[name=ptaxdisplay]").val();
	var body="";
	body += '<tr>';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;"><input type="hidden" name="basecost" value="0.00">';
	body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
	body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+taxdisplay+'">';
	body += '</td>';
	body += '<td class="bill-item-dbt">';
	body += '<input type="text" name="item" style="width:95%" class="form-control itemSearch" onchange="checkitems(this.value,this)" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';
	body += '<td class="bill-acc">';
	body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Select Account">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
	body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
	body += '<td class="item-discount text-right">';
	body += '<div class="row">';							
	body += '<div class=" col-lg-12 col-sm-3 col-12">';
	body += '<div class="input-group my-group" style="width:120px;">';
	body += '<input name="item_discount" type="text" class="form-control text-right" value="'+zeroval+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
	body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
	body += "<option value="+curency+">"+curency+"</option>";
	body += '<option>%</option>';										
	body += '</select>';
	body += '</div>';
	body += '</div>'; 
	body += '</div>';
	body += '</td>';
//	body += '<td class="item-tax">';
//	body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
//	body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
//	body += '</td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += '<input name="amount" type="text" class="form-control text-right" value="0.00" readonly="readonly" style="display:inline-block;" tabindex="-1">';
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
function setITEMDESC(obj,desc){
	$(obj).closest('tr').find("input[name=ITEMDES]").data('pd',desc);
	$(obj).closest('tr').find("input[name ='ITEMDES']").val(desc);
}
//table validation
function checkitems(itemvalue,obj){	
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ITEM : itemvalue,
				ACTION : "PRODUCT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Does't Exists");
						$(obj).typeahead('val', '');
						$(obj).parent().find('input[name="item"]').focus();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
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
						document.getElementById("SHIPPINGCUSTOMER").focus();
						$("#SHIPPINGCUSTOMER").typeahead('val', '');
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
//jsp validation ends

// modal onchange
function checkuom(uom){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				UOM : uom,
				ACTION : "UOM_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Uom Does't Exists");
						document.getElementById("Basicuom").focus();
						$("#Basicuom").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkpuruom(uom){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				UOM : uom,
				ACTION : "UOM_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Uom Does't Exists");
						document.getElementById("purchaseuom").focus();
						$("#purchaseuom").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checksaluom(uom){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				UOM : uom,
				ACTION : "UOM_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Uom Does't Exists");
						document.getElementById("salesuom").focus();
						$("#salesuom").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkinvuom(uom){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				UOM : uom,
				ACTION : "UOM_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Uom Does't Exists");
						document.getElementById("inventoryuom").focus();
						$("#inventoryuom").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkhscode(hscode){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				HSCODE : hscode,
				ACTION : "HSCODE_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("HS Code Does't Exists");
						document.getElementById("HSCODE").focus();
						$("#HSCODE").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkcoo(coo){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				COO : coo,
				ACTION : "COO_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("COO Does't Exists");
						document.getElementById("COO").focus();
						$("#COO").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkItemsupplier(supplier){	
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
						document.getElementById("vendname_itemmst").focus();
						$("#vendname_itemmst").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}


function checkprddept(dept){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PRD_DEPT_ID : dept,
				ACTION : "DEPT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Department Does't Exists");
						document.getElementById("PRD_DEPT_DESC").focus();
						$("#PRD_DEPT_DESC").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkprdcat(cat){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PRD_CLS_ID : cat,
				ACTION : "CAT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Category Does't Exists");
						document.getElementById("PRD_CLS_DESC").focus();
						$("#PRD_CLS_DESC").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkprdtype(type){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PRD_TYPE_ID : type,
				ACTION : "TYPE_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Sub Category Does't Exists");
						document.getElementById("PRD_TYPE_DESC").focus();
						$("#PRD_TYPE_DESC").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkprdbrand(brand){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PRD_BRAND_ID : brand,
				ACTION : "BRAND_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Brand Does't Exists");
						document.getElementById("PRD_BRAND_DESC").focus();
						$("#PRD_BRAND_DESC").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkprditem(itemvalue)
{	
	 if(itemvalue=="" || itemvalue.length==0 ) {
		alert("Enter Item!");
		document.getElementById("ITEM").focus();
		return false;
	}else{
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ITEM : itemvalue,
				ACTION : "PRODUCT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
                               
						alert("Product Already Exists");
						document.getElementById("ITEM").focus();
						document.getElementById("ITEM").value="";
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
		}	
}
//modal changes end

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
						document.getElementById("transportC").focus();
						$("#transportC").typeahead('val', '');
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
						document.getElementById("CUST_PAYTERMS").focus();
						$("#CUST_PAYTERMS").typeahead('val', '');
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
						document.getElementById("payment_term").focus();
						$("#payment_term").typeahead('val', '');
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
				//ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
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
		//	return '<div onclick="loadItemData(this,\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.COST+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.PURCHASEINVQTY+' '+data.PURCHASEUOM+'</p></div>';
			//vicky
			return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.PURCHASEINVQTY+' '+data.PURCHASEUOM+'</p></div>';
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
		}).on('typeahead:select',function(event,selection){
			$(this).closest('tr').find("input[name ='ITEMDES']").data('pd',selection.ITEMDESC);
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
			loadItemData(this, selection.CATLOGPATH,selection.ACCOUNT,selection.COST);
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				$(this).closest('tr').find('input[name="ITEMDES"]').val("");
			}		
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
		
		$('.bill-table tbody tr').each(function () {
		$(this).find("input[name ='item']").focus();
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
	});*/
}

function removeSuggestionToTable(){
	$(".itemSearch").typeahead('destroy');
	$(".accountSearch").typeahead('destroy');
	$(".taxSearch").typeahead('destroy');
}

function loadItemData(obj, catalogPath, account, cost){
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	$(obj).closest('tr').find("td:nth-child(1)").find('input[name=basecost]').val(cost);
	
	cost = parseFloat($("input[name ='CURRENCYUSEQT']").val()) * parseFloat(cost);
	cost = parseFloat(cost).toFixed(numberOfDecimal);
	
	
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("td:nth-child(3)").find('input').typeahead('val', account);
	$(obj).closest('tr').find("td:nth-child(5)").find('input').val(cost);
	$(obj).closest('tr').find('input[name="qty"]').focus();
	$(obj).closest('tr').find('input[name="qty"]').select();
	calculateAmount(obj);
	loadItemDescToolTip(obj);
}

function loadItemDescToolTip(obj){
//	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip("destroy");
	var content = "<p class='text-left'>"+$(obj).closest('tr').find("input[name=ITEMDES]").val()+"</p>";
//	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"}); 
}

function calculateAmount(obj){

	var numberOfDecimal = $("#numberOfDecimal").val();
	var state = $("input[name=STATE_PREFIX]").val();
	
	var qty = parseFloat($(obj).closest('tr').find('input[name=qty]').val()).toFixed(3);
	var qtyold = parseFloat($(obj).closest('tr').find('input[name=qtyold]').val()).toFixed(3);
	if(parseFloat(qty) > parseFloat(qtyold)){
		alert("Quantity exceeds the return quantity");
		qty = qtyold;
	}
	
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

/*function calculateTotal(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = 0, discountValue=0, shippingvalue=0, adjustmentvalue=0,totalvalue=0;
	var rowcount=$(".bill-table tbody tr").length;
	if(rowcount>0)
		{
			$(".bill-table tbody tr td:last-child").each(function() {
				if($(this).find('input').attr("disabled") != "disabled"){
				    amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
				    amount = parseFloat(amount).toFixed(numberOfDecimal);
				    $("#subTotal").html(amount);
				    $('input[name ="sub_total"]').val(amount);// hidden input
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
	
	 var discount = $('input[name ="discount"]').val();
	 discount = (discount == "") ? 0.00 : parseFloat(discount).toFixed(numberOfDecimal);
	 var discounType = $('select[name ="discount_type"]').val();
	 if(discounType == "%"){
		 discountValue = parseFloat(amount*(discount/100)).toFixed(3);
	 }else{
		 discountValue = discount;
	 }
	 $("#discount").html(parseFloat(-discountValue).toFixed(numberOfDecimal));
	 
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
	 $('input[name ="taxTotal"]').val(taxTotal);
	 var shippingvalue = $('input[name ="shippingcost"]').val();
	 shippingvalue = (shippingvalue == "") ? 0.00 : shippingvalue;
	 $("#shipping").html(parseFloat(shippingvalue).toFixed(numberOfDecimal));
	 
	 var orderdiscpercentage = $('input[name ="orderdiscount"]').val();
	 orderdiscpercentage = (orderdiscpercentage == "") ? 0.00 : orderdiscpercentage;
	 orderdisc = parseFloat(parseFloat(amount)*(parseFloat(orderdiscpercentage)/100)); 
	 $("#orderdiscount").html(parseFloat(-orderdisc).toFixed(numberOfDecimal));
	 
	 
	 var adjustmentvalue = $('input[name ="adjustment"]').val();
	 adjustmentvalue = (adjustmentvalue == "") ? 0.00 : adjustmentvalue;
	 $("#adjustment").html(parseFloat(adjustmentvalue).toFixed(numberOfDecimal));

	 totalvalue = parseFloat(parseFloat(amount)- parseFloat(orderdisc) - parseFloat(discountValue) + parseFloat(taxTotal)
			 + parseFloat(shippingvalue) - parseFloat(adjustmentvalue)).toFixed(numberOfDecimal);
	 
	 $("#total").html(totalvalue);
	 $("#total_amount").val(totalvalue); // hidden input



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

function renderTaxDetails(){
/*	var html="";
	var taxTotal = 0;
	var subtotal =0;
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	$(".bill-table tbody tr td:last-child").each(function() {
		if($(this).find('input').attr("disabled") != "disabled"){
			subtotal =  parseFloat(subtotal) + parseFloat($(this).find('input').val());
		}
	});
	
	 
	
	$.each(taxList, function( key, data ) {
		var mydata=JSON.stringify(data);
		console.log("Tax dta:"+mydata);
		var originalTaxType= data.types;
		var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); 
		console.log("Tax Type:"+taxtype);
		console.log("Zero Type:"+zerotype);
		var percentageget = data.percentage;
		var discountcost = $('input[name ="discount"]').val();
		var discountcosttax = parseFloat(discountcost*(percentageget/100)).toFixed(numberOfDecimal);
		
		var orderdiscpercentage = $('input[name ="orderdiscount"]').val();
		 orderdiscpercentage = (orderdiscpercentage == "") ? 0.00 : orderdiscpercentage;
		 var orderdisc = parseFloat(parseFloat(subtotal)*(parseFloat(orderdiscpercentage)/100));
		 var orderdiscounttax = parseFloat(orderdisc*(percentageget/100)).toFixed(numberOfDecimal);
		
		if(data.value > 0 || taxtype=="ZERO RATE" && zerotype>0){
			html+='<div class="total-row">';
			html+='<div class="total-label">'+data.display+'</div>';
			html+='<div class="total-amount taxAmount">'+parseFloat(parseFloat(data.value)-(parseFloat(discountcosttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal)+'</div>';
			html+='</div>';
			taxTotal += parseFloat(data.value)-(parseFloat(discountcosttax)+parseFloat(orderdiscounttax));
		}
	});
	$(".taxDetails").html(html);
	calculateTotal();
	($("#item_rates").val() == 0) ? $(".productRate").hide() : $(".productRate").show(); 
	$('input[name ="taxTotal"]').val(taxTotal);*/
	
	var html="";
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
	
	/*if($("#item_rates").val() == 0){
		$('input[name ="taxTotal"]').val(taxTotal);
	}else{
		$(".taxDetails").html("");
		$('input[name ="taxTotal"]').val("0");
	}*/
	
	
	}

function validateBill(){
	var supplier = document.form.vendno.value;
	var creditnote = document.form.creditnote.value;
	var billDate = document.form.bill_date.value;
	var itemRates = document.form.item_rates.value;
	var discount = document.form.discount.value;
	var discountAccount = document.form.discount_account.value;
	var isItemValid = true, isAccValid = true;
	
	var poreturn = document.form.poreturn.value;
	var bill = document.form.billno.value;
	var Currency = document.form.CURRENCY.value;
	var CURRENCYUSEQT = document.form.CURRENCYUSEQT.value;
	if(poreturn != ""){
		if(bill == ""){
			alert("Please select a Bill No.");
			return false;
		}	
	}
	
	if(supplier == ""){
		alert("Please select a Supplier.");
		return false;
	}	
	if(creditnote == ""){
		alert("Please enter the Credit Note.");
		return false;
	}
	CheckCreditnote(creditnote);
	if(billDate == ""){
		alert("Please enter a valid Credit Date.");
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
	$('#SALES_LOC').prop('disabled',false);
	$('#oddiscount_type').prop('disabled',false);
	$('.item_discount_type').prop('disabled',false);

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

function OnCountrys(Country)
{
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_STATE_DATA",
			PLANT : plant,
			COUNTRY : Country,
		},
		success : function(dataitem) {
			var StateList=dataitem.STATEMST;
			var myJSON = JSON.stringify(StateList);
			//alert(myJSON);
			$('#STATE').empty();
		//	$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
			$('#STATE').append('<OPTION>Select State</OPTION>');
			$.each(StateList, function (key, value) {
				$('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
			});
			$('#STATE_C').empty();
		//	$('#STATE_C').append('<OPTION style="display:none;">Select State</OPTION>');
			$('#STATE_C').append('<OPTION>Select State</OPTION>');
			$.each(StateList, function (key, value) {
				$('#STATE_C').append('<option value="' + value.text + '">' + value.text + '</option>');
			});	 
		}
	});	
	
}
function checkitem(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Customer ID!");
		document.getElementById("CUST_CODE").focus();
		return false;
	 }else{ 
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CUST_CODE : aCustCode,
                USERID : "<%=username%>",
				PLANT : "<%=plant%>",
					ACTION : "CUSTOMER_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {

						alert("Customer ID Already Exists");
						document.getElementById("CUST_CODE").focus();
						//document.getElementById("ITEM").value="";
						return false;
					} else
						return true;
				}
			});
			return true;
		}
	}

function checkuser(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter User ID!");
		return false;
	 }else{ 
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				CUST_CODE : aCustCode,
                USERID : "<%=username%>",
				PLANT : "<%=plant%>",
				ACTION : "CUSTOMER_CHECKUSER"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
                            
						alert("User ID Already Exists");
						return false;
					}
					else
						return true;
				}
});	
		return true;
}
}


function shippingAddress(){

	if (document.getElementById("SameAsContactAddress").checked == true) {
		document.getElementById("SameAsContactAddress").value = "1";
		var contactname = document.getElementById("CONTACTNAME").value;
		var desgination = document.getElementById("DESGINATION").value;
		var workphone = document.getElementById("WORKPHONE").value;
		var hpno= document.getElementById("HPNO").value;
		var email =document.formcust.EMAIL.value;
		var country_code = document.getElementById("COUNTRY_CODE_C").value;
		SHIPCUST_OnCountry(country_code);
		var scountry = document.formcust.COUNTRY.value;
		var addr1 = document.getElementById("ADDR1").value;
		var addr2 = document.getElementById("ADDR2").value;
		var addr3 = document.getElementById("ADDR3").value;
		var addr4 = document.getElementById("ADDR4").value;
		var state = document.getElementById("STATE_C").value;
		var zip = document.getElementById("ZIP").value;
			     
		document.getElementById("CUST_SHIP_CONTACTNAME").value = contactname;
		document.getElementById("CUST_SHIP_DESGINATION").value = desgination;
		document.getElementById("CUST_SHIP_WORKPHONE").value = workphone;
		document.getElementById("CUST_SHIP_HPNO").value = hpno;
		document.getElementById("CUST_SHIP_EMAIL").value = email;
		document.getElementById("CUST_SHIP_COUNTRY_CODE").value = country_code;
		document.formcust.CUST_SHIP_COUNTRY.value = scountry;
		document.getElementById("CUST_SHIP_ADDR1").value = addr1;
		document.getElementById("CUST_SHIP_ADDR2").value = addr2;
		document.getElementById("CUST_SHIP_ADDR3").value = addr3;
		document.getElementById("CUST_SHIP_ADDR4").value = addr4;
		//document.getElementById("SHIP_STATE").value = state;
		$("select[name ='CUST_SHIP_STATE']").val(state);
		document.getElementById("CUST_SHIP_STATE").value = state;
		document.getElementById("CUST_SHIP_ZIP").value = zip;
				 
		}  
	else {
		document.getElementById("SameAsContactAddress").value = "0";
		document.getElementById("CUST_SHIP_CONTACTNAME").value = "";
		document.getElementById("CUST_SHIP_DESGINATION").value = "";
		document.getElementById("CUST_SHIP_WORKPHONE").value = "";
		document.getElementById("CUST_SHIP_HPNO").value = "";
		document.getElementById("CUST_SHIP_EMAIL").value = "";
		document.getElementById("CUST_SHIP_COUNTRY_CODE").value = "Select Country";
		document.formcust.CUST_SHIP_COUNTRY.value = "";
		document.getElementById("CUST_SHIP_ADDR1").value = "";
		document.getElementById("CUST_SHIP_ADDR2").value = "";
		document.getElementById("CUST_SHIP_ADDR3").value = "";
		document.getElementById("CUST_SHIP_ADDR4").value = "";
		document.getElementById("CUST_SHIP_STATE").value = "Select State";
		document.getElementById("CUST_SHIP_ZIP").value = "";
		}
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
	body += '<input  name="PASSWORD" class="form-control text-left" maxlength="60" placeholder="Enter Password" type="password" autocomplete="off">';
	body += '<span class="input-group-btn phideshow" onclick="javascript:viewpassword(this);return false;" >';
    body += '<button class="btn btn-default reveal" type="button" style="background: white;"><i class="fa fa-fw fa-eye" id="peye" style="color: #23527c;"></i></button>';
    body += '</span>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<input type="hidden" name="MANAGER_APP_VAL" value="0">';
	body += '<input type="Checkbox" style="border:0;background=#dddddd"	name="MANAGER_APP" value="1" onclick="checkManagerApp(this)">';
	body += '<span class="glyphicon glyphicon-remove-circle user-action" aria-hidden="true" style="right: -90px;"></span>';    	
	body += '</td>';
	body += '</tr>';
	$(".user-table tbody").append(body);
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
//		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
//		}
	if(document.form.custModal.value=="shipcust"){
		$("input[name ='payment_term']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=PMENT_DAYS]").val(payTermsData.NO_DAYS);
		}else if(document.form.custModal.value=="cust"){
		$("input[name ='sup_payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=SUP_PMENT_DAYS]").val(payTermsData.NO_DAYS);
		}else{
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		}
	}
}

function paymentTypeCallback(data){
	if(data.STATUS="SUCCESS"){
//		$("#payment_type").typeahead('val', data.PAYMENTMODE);
//		$("#payment_type").typeahead('val', data.PAYMENTTYPE);
	if(document.form.custModal.value=="shipcust"){
		$("#CUST_PAYTERMS").typeahead('val', data.PAYMENTMODE);
		}else if(document.form.custModal.value=="cust"){
		$("#PAYTERMS").typeahead('val', data.PAYMENTMODE);
		}else{
		$("#payment_type").typeahead('val', data.PAYMENTMODE);
		}
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
	var ITEM_DESC = escapeHtml(data.ITEMDESC);
	taxList = [];
	$.each(orders, function( key, data ) {	
		body += '<tr>';
		body += '<td><input type="checkbox" class="itemChk" checked>';
		body += '<input type="text" name="lnno" value="'+data.POLNNO+'" hidden>';
		body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.INBOUND_GST+'">';
		body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+data.INBOUND_GST+'%">';
		body += '</td>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item-dbt">';
		body += '<input type="text" name="item" class="form-control" onchange="checkitems(this.value,this)" value="'+data.ITEM+'" readonly="readonly">';
		body += '<input class="form-control"  name="ITEMDES" value="'+ITEM_DESC+'" readonly style="height: 23px;background-color: #fff;" readonly>';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control" value="'+data.ACCOUNT+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTYOR+'" readonly="readonly"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITCOST+'" readonly="readonly"></td>';
		body += '<td class="item-discount text-right">';
		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
		body += '</td>';
//		body += '<td class="item-tax" data-name="PURCHASE" data-tax="'+data.INBOUND_GST+'">';
//		body += '<input type="text" name="tax_type" class="form-control" value="PURCHASE ['+data.INBOUND_GST+'%]" readonly="readonly">';
//		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" readonly="readonly" style="display:inline-block;" tabindex="-1">';
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

	var curency = document.form.CURRENCY.value;
	var currencyid = document.form.CURRENCYID.value;
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
		/*var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");*/
		var numberOfDecimal = $("#numberOfDecimal").val();
		/*tax.types = percentage[0].trim();
		tax.percentage = percentage[1];
		tax.display = data.TAX_TYPE;
		var taxpercentage=percentage[1];
		var taxtype=percentage[0].trim();
		var loadtype=taxtype.substr(0, taxtype.indexOf('(')); */
		
		var uprice= parseFloat((data.UNITPRICE*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
		var amt= parseFloat((data.AMOUNT*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
		var ITEMDESC = escapeHtml(data.ITEMDESC);
	/*	if(loadtype=="ZERO RATE")
			{
				zerotype++;
			}*/
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="basecost" value="'+data.BASECOST+'">';
		body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.TAX_TYPE+'">';
		body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+data.TAX_TYPE+'">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item-dbt">';
		body += '<input type="text" name="item" style="width:95%" class="form-control itemSearch" onchange="checkitems(this.value,this)" value="'+data.ITEM+'" onchange="calculateAmount(this)">';
		body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
		body += '<input class="form-control"  name="ITEMDES" value="'+ITEMDESC+'" readonly style="height: 23px;background-color: #fff;" >';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" value="'+data.ACCOUNT+'">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+uprice+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		/*body += '<td class="item-discount text-right">';		
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEM_DISCOUNT+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
		body += '</td>';*/
		
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		if(data.ITEM_DISCOUNT_TYPE == "%"){
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)" >';
		}else{
			body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT*data.CURRENCYUSEQT).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)"  onkeypress="return isNumberKey(event,this,4)">';	
		}
		body += '<select name="item_discounttype" class="discountPicker form-control item_discount_type" onchange="calculateAmount(this)">';
		if(data.ITEM_DISCOUNT_TYPE == currencyid){
			body += "<option selected value="+currencyid+">"+currencyid+"</option>";
		}else{
			body += "<option value="+currencyid+">"+currencyid+"</option>";
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
//		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';
	/*	if(data.TAX_TYPE=="EXEMPT[0.0%]")
		{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="EXEMPT">';
		}
		else if(data.TAX_TYPE=="OUT OF SCOPE[0.0%]")
			{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="OUT OF SCOPE">';
			}
		else
			{
			if(data.TAX_TYPE==""){
				body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax">';
			}else{
				body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'">';
			}
			
			}*/
//		body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly>';
		//body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" >';
//		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" style="display:inline-block;" readonly></td>';
		body += '</tr>';		
		

		
		
		/*var amount = amt;		
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
		
		$("input[name ='Purchasetax']").val(data.TAX_TYPE);
		$("input[name ='ptaxdisplay']").val(data.TAX_TYPE);
		$("textarea[name ='note']").val(data.NOTE);
		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
	});
	$(".bill-table tbody").html(body);
	calculateTotal();
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
	var currencyid= document.form.CURRENCYID.value;
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {	
	/*	var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");*/
		var numberOfDecimal = $("#numberOfDecimal").val();
	/*	tax.types = percentage[0].trim();
		tax.percentage = percentage[1];
		tax.display = data.TAX_TYPE;
		var taxpercentage=percentage[1];
		var taxtype=percentage[0].trim();
		var loadtype=taxtype.substr(0, taxtype.indexOf('(')); */
		var ITEMDESC = escapeHtml(data.ITEMDESC);
		var uprice= parseFloat((data.UNITPRICE*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
		var amt= parseFloat((data.AMOUNT*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
		
		
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="basecost" value="'+data.BASECOST+'">';
		body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.TAX_TYPE+'">';
		body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+data.TAX_TYPE+'">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item-dbt">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)" readonly>';
		body += '<input class="form-control"  name="ITEMDES" value="'+ITEMDESC+'" readonly style="height: 23px;background-color: #fff;">';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control" value="'+data.ACCOUNT+'" readonly>';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)" readonly></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+uprice+'" onchange="calculateAmount(this)" readonly></td>';
		/*body += '<td class="item-discount text-right">';		
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEM_DISCOUNT+'" onchange="calculateAmount(this)" readonly>';
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
		if(data.ITEM_DISCOUNT_TYPE == currencyid){
			body += "<option selected value="+currencyid+">"+currencyid+"</option>";
		}else{
			body += "<option value="+currencyid+">"+currencyid+"</option>";
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
//		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control" readonly>';
		/*if(data.TAX_TYPE=="EXEMPT[0.0%]")
		{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="EXEMPT" readonly>';
		}
		else if(data.TAX_TYPE=="OUT OF SCOPE[0.0%]")
			{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="OUT OF SCOPE" readonly>';
			}
		else
		if(data.TAX_TYPE==""){
			body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" readonly>';
		}else{
			body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly>';
		}*/
//		body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly>';
		//body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" >';
//		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" style="display:inline-block;" readonly></td>';
		body += '</tr>';		
		

		
		
		/*var amount = amt;		
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
		$("textarea[name ='note']").val(data.NOTE);
		$("input[name ='Purchasetax']").val(data.TAX_TYPE);
		$("input[name ='ptaxdisplay']").val(data.TAX_TYPE);
		$("textarea[name ='note']").val(data.NOTE);
		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
	});
	$(".bill-table tbody").html(body);
	calculateTotal();
	$('.item_discount_type').prop('disabled',true);
	$('.discount_type').prop('disabled',true);
	$('#SALES_LOC').prop('disabled',true);
	$('#Purchasetax').prop('disabled',true);
	$("#dediscount").attr("readonly", true);
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

function tooltioedit(){
	$(".bill-table tbody tr").each(function() {
			var obj = $('td:eq(1)', this);
			loadItemDescToolTipedit(obj);
			$(obj).closest('tr').find('input[name="QTY"]').focus();
			$(obj).closest('tr').find('input[name="QTY"]').select();
	});
}

function loadItemDescToolTipedit(obj){
	//$(obj).closest('tr').find("input[name=ITEMDES]").tooltip("destroy");
	var content = "<p class='text-left'> "+$(obj).closest('tr').find("input[name=ITEMDES]").val()+"</p>";
	//$(obj).closest('tr').find("input[name=ITEMDES]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
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

function loadBillDet(id,pono,grno,billfrom,loc){
	$("#addmore").css("display", "none");
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
	
	
	if(billfrom == "PORETURN"){
		var poreturn = document.form.poreturn.value
		getPOreturn(poreturn,pono,grno);
	}else{
		$("input[name ='billhdrid']").val(id);
		$("input[name ='SALES_LOC']").val(loc);
		loaddatausingbillno(id);
	}
	
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
	var currencyid = document.form.CURRENCYID.value;
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var body="";
	var isoddisctax = "0";
	var isdisctax = "0";
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
		var taxtype=percentage[0].trim();
		var loadtype=taxtype.substr(0, taxtype.indexOf('(')); 
		
		
		if(loadtype=="ZERO RATE")
			{
				zerotype++;
			}*/
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;" readonly="readonly">';
		body += '<input type="hidden" name="qtyold" value="'+data.QTY+'">';
		body += '<input type="hidden" name="lnno" value="'+data.LNNO+'">';
		body += '<input type="hidden" name="dnqty" value="'+data.DEBITNOTE_QTY+'">';
		body += '<input type="hidden" name="billqty" value="'+data.BILL_QTY+'">';
		body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.TAX_TYPE+'">';
		body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+data.TAX_TYPE+'">';
		body += '</td>';
		body += '<td class="bill-item-dbt">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)" readonly="readonly">';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" value="'+data.ACCOUNT_NAME+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.COST+'" onchange="calculateAmount(this)" readonly="readonly"></td>';
		/*body += '<td class="item-discount text-right">';		
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.DISCOUNT+'" onchange="calculateAmount(this)" readonly="readonly">';
		body += '</td>';*/
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		if(data.DISCOUNT_TYPE == "%"){
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
		}else{
			body += '<input name="item_discount" type="text" class="form-control text-right item_discount" value="'+parseFloat(data.DISCOUNT).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';	
		}
		body += '<select name="item_discounttype" class="discountPicker form-control item_discount_type" onchange="calculateAmount(this)">';
		if(data.DISCOUNT_TYPE == currencyid){
			body += "<option selected value="+currencyid+">"+currencyid+"</option>";
		}else{
			body += "<option value="+currencyid+">"+currencyid+"</option>";
		}
		
		if(data.DISCOUNT_TYPE == "%"){
			body += '<option selected >%</option>';	
		}else{
			body += '<option>%</option>';	
		}								
		body += '</select>';
		body += '</div>';
		body += '</div>'; 
		body += '</div>';
//		body += '</td>';
		
		
		
//		body += '<td class="item-tax" readonly="readonly">';
//		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';
	/*	if(data.TAX_TYPE=="EXEMPT[0.0%]")
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
			}*/
		//body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" >';
//		body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly="readonly">';
//		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" style="display:inline-block;" readonly="readonly"></td>';
		body += '</tr>';		
		
		$("input[name ='orderdiscount']").val(parseFloat(data.ORDER_DISCOUNT).toFixed(3));
		$("input[name ='discount']").val(data.ALLDISCOUNT);
		
		
		/*var amount = data.AMOUNT;		
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
		
		
		
		
		$("textarea[name ='note']").val(data.NOTE);
		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
		$("#discountblock").prop("readonly", true);
		$("input[name ='Purchasetax']").val(data.TAX_TYPE);
		$("input[name ='CURRENCYUSEQTOLD']").val(data.CURRENCYUSEQT);
		$("input[name ='CURRENCYUSEQT']").val(data.CURRENCYUSEQT);
		$("input[name ='GST']").val(data.GST);
		$("select[name ='item_rates']").val(data.ISTAXINCLUSIVE);
		$("input[name ='ptaxdisplay']").val(data.TAX_TYPE);
		$("input[name ='taxid']").val(data.TAXID);
		$("input[name ='aorderdiscount']").val(data.ORDER_DISCOUNT);
		$("input[name ='orderdiscount']").val(data.ORDER_DISCOUNT);
		$("input[name ='ptaxdisplay']").val(data.TAX_TYPE);
		$("input[name ='ptaxtype']").val(data.PTAXTYPE);
		$("input[name ='ptaxpercentage']").val(data.PTAXPERCENTAGE);
		$("input[name ='ptaxiszero']").val(data.PTAXISZERO);
		$("input[name ='ptaxisshow']").val(data.PTAXISSHOW);
		
		isoddisctax = data.ISODTAX;
		isdisctax = data.ISDTAX;
		
		if(data.ODISCTYPE == ""){
			$("select[name='oddiscount_type']").empty();
			$("select[name='oddiscount_type']").append('<option selected value="%">%</option>');
		}else{
			$("select[name='oddiscount_type']").empty();
			$("select[name='oddiscount_type']").append('<option selected value="'+data.ODISCTYPE+'">'+data.ODISCTYPE+'</option>');
		}
		
		$("select[name='discount_type']").empty();
		$("select[name='discount_type']").append('<option selected value="'+data.DISCOUNT_TYPE+'">'+data.DISCOUNT_TYPE+'</option>');
		
		
		$("input[name ='PROJECTID']").val(data.PROJECTID);
		$("input[name ='project']").val(data.PROJECTNAME);
		$("input[name ='SHIPPINGID']").val(data.SHIPPINGID);
		$("input[name ='SHIPPINGCUSTOMER']").val(data.SHIPPINGCUSTOMER);
		$("input[name ='EMP_NAME']").val(data.EMP_NAME);
		$("input[name ='EMPNO']").val(data.EMPNO);
	});
	$(".bill-table tbody").html(body);
	
	
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
	
	
	
	calculateTotal();

	$('.item_discount_type').prop('disabled',true);
	$('.discount_type').prop('disabled',true);
	$(".item_discount").attr("readonly", true);
	$("#discountblock").attr("readonly", true);
	$('#SALES_LOC').prop('disabled',true);
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

function getvendname(TAXTREATMENT,VENDO,CURRENCY,PAYMENT_TERMS,PAY_TERMS,CURRENCYID,CURRENCYUSEQT){
	
	
	$("input[name ='vendno']").val(VENDO);
	$("input[name=payment_terms]").val(PAYMENT_TERMS);
	getCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT);

	
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


/*function loadgetPOreturntable(orders){
	var curency = document.form.curency.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {
		

		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)" readonly>';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Select Account">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)" readonly></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITPRICE+'" onchange="calculateAmount(this)" readonly></td>';
		body += '<td class="item-discount text-right">';		
		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
		body += '</td>';
		body += '<td class="item-tax">';
		body += '<input type="hidden" name="tax_type">';
		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax">';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" style="display:inline-block;" readonly></td>';
		body += '</tr>';		
		
	
		$("textarea[name ='note']").val(data.NOTE);
		if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");
		$("input[name ='orderdiscount']").val(data.ORDERDISCOUNT);
	});
	$(".bill-table tbody").html(body);
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
}*/


/*function loadgetPOreturntable(orders){
	var curency = document.form.curency.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	var body="";
	var billdiscount="0";
	var billsubtotal="0";
	var orderdiscount="0";
	var billdiscounttype="0";
	var subtotal ="0";
	taxList = [];
	$.each(orders, function( key, data ) {
		

		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)" readonly>';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Select Account">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTY+'" onchange="calculateAmount(this)" readonly></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITPRICE+'" onchange="calculateAmount(this)" readonly></td>';
		body += '<td class="item-discount text-right">';		
		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
		body += '</td>';
		
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		if(data.ITEM_DISCOUNT_TYPE == "%"){
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" readonly>';
		}else{
			body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" readonly>';	
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
		body += '<td class="item-tax">';
		body += '<input type="hidden" name="tax_type">';
		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax">';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" style="display:inline-block;" readonly></td>';
		body += '</tr>';		
		
	
		$("textarea[name ='note']").val(data.NOTE);
		if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");

		$("input[name ='orderdiscount']").val(parseFloat(data.ORDERDISCOUNT).toFixed(3));
		billdiscount = data.BILLDISCOUNT;
		billsubtotal = parseFloat(data.BILLSUB_TOTAL) +  parseFloat(data.BILLLINEDISCOUNT);
		orderdiscount = data.ORDERDISCOUNT;
		billdiscounttype = data.BILLDISCOUNTTYPE;
		$("#discountblock").prop("readonly", true);
		 
	});
	$(".bill-table tbody").html(body);
	$('.item_discount_type').prop('disabled',true);
	$('.discount_type').prop('disabled',true);
	$(".item_discount").attr("readonly", true);
	$("#discountblock").attr("readonly", true);
	$('#SALES_LOC').prop('disabled',true);
	
	if(billdiscount > 0){
	
		$(".bill-table tbody tr td:last-child").each(function() {
			if($(this).find('input').attr("disabled") != "disabled"){
				subtotal =  parseFloat(subtotal) + parseFloat($(this).find('input').val());
			}
		});
		
		
		var orderdiscpercentage = orderdiscount;
		orderdiscpercentage = (orderdiscpercentage == "") ? 0.00 : orderdiscpercentage;
		var orderdisc = parseFloat(parseFloat(subtotal)*(parseFloat(orderdiscpercentage)/100));
		
		
		var orderdiscpercentagebill = orderdiscount;
		orderdiscpercentagebill = (orderdiscpercentagebill == "") ? 0.00 : orderdiscpercentagebill;
		var orderdiscbill = parseFloat(parseFloat(billsubtotal)*(parseFloat(orderdiscpercentagebill)/100));
	
		subtotal = parseFloat(subtotal) - parseFloat(orderdisc);
		billsubtotal = parseFloat(billsubtotal) - parseFloat(orderdiscbill);
		
		var percentage = (parseFloat(subtotal)*100)/parseFloat(billsubtotal);
		
		if(billdiscounttype == "%"){
			$("input[name ='discount']").val(parseFloat(billdiscount).toFixed(3));
			$("select[name ='discount_type']").val(billdiscounttype);
		}else{
			billdiscount = (parseFloat(billdiscount)/100)*parseFloat(percentage);
			$("select[name ='discount_type']").val(billdiscounttype);
			$("input[name ='discount']").val(parseFloat(billdiscount).toFixed(numberOfDecimal));
		}
		
		$('.discount_type').prop('disabled',true);
	}
	renderTaxDetails();
	removeSuggestionToTable();
	addSuggestionToTable();
}*/


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
		body += '<input type="hidden" name="tax_type" class="taxSearch" >';
		body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item-dbt">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)" readonly>';
		body += '<input class="form-control"  name="ITEMDES" value="'+ITEMDESC+'" readonly style="height: 23px;background-color: #fff;" readonly>';
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Select Account">';
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
		
		$("input[name ='PROJECTID']").val(data.PROJECTID);
		$("input[name ='project']").val(data.PROJECTNAME);
		$("input[name ='SHIPPINGID']").val(data.SHIPPINGID);
		$("input[name ='SHIPPINGCUSTOMER']").val(data.SHIPPINGCUSTOMER);
		 
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
	
	//renderTaxDetails();
	calculateTotal();
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

/*function setCURRENCYUSEQT(CURRENCYUSEQTCost){
	
	var numberOfDecimal = $("#numberOfDecimal").val();
	var cost = 0;
	var ischange=0;
	
$('tr').each(function () {
		
		var CURRENCYID = $('input[name ="CURRENCYID"]').val();
		var CURRENCY = $('input[name ="CURRENCY"]').val();
		var ITEM_CURRENCYID = $(this).find("td:nth-child(6)").find('select').val();
		
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
	
		var qty = parseFloat($(this).find("td:nth-child(4)").find('input').val()).toFixed(3);
		cost = parseFloat($(this).find("td:nth-child(1)").find('input[name=basecost]').val()) *parseFloat(CURRENCYUSEQTCost);
		cost = parseFloat(cost).toFixed(numberOfDecimal);
		$(this).find("td:nth-child(5)").find('input').val(cost);
		
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
			itemDiscountval = parseFloat(parseFloat(itemDiscountval)/parseFloat(CURRENCYUSEQTCost)).toFixed(numberOfDecimal);
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
	
});
changetaxfordiscountnew();
calculateTotal();
}*/

function getCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT){
	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	$('input[name ="CURRENCY"]').val(CURRENCY);
	

	/*$('#discount_type').empty();
	$('#discount_type').append('<option value="' + CURRENCYID + '">' + CURRENCY + '</option>');
	$('#discount_type').append('<option value="%">%</option>');
	
	$('#oddiscount_type').empty();
	$('#oddiscount_type').append('<option value="%">%</option>');
	$('#oddiscount_type').append('<option value="' + CURRENCYID + '">' + CURRENCY + '</option>');*/
	
	setCURRENCYUSEQT(CURRENCYUSEQT);
	
	var basecurrency = document.form.BASECURRENCYID.value; //Resvi
	document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi
	
	document.getElementById('lbltotal').innerHTML = "Total ("+CURRENCYID+")"; //  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency
	if(basecurrency!=CURRENCYID)
		document.getElementById('showtotalcur').style.display = 'block';
	else
		document.getElementById('showtotalcur').style.display = 'none';
	
}


function setCURRENCYUSEQT(CURRENCYUSEQTCost){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var oldeqtcost = $('input[name ="CURRENCYUSEQTOLD"]').val();
	var CURRENCYID = $('input[name ="CURRENCYID"]').val();
	var CURRENCY = $('input[name ="CURRENCY"]').val();
	var amount ="0";
	var cost = "0";

	$('.bill-table tbody tr').each(function () {
		var qty = parseFloat($(this).find('input[name ="QTY"]').val()).toFixed(3);
		cost = ((parseFloat($(this).find('input[name=unitprice]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
		cost = parseFloat(cost).toFixed(numberOfDecimal);
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
				if(ptaxiszero == "0" && ptaxisshow == "1"){
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
			//alert("taxTotal     "+taxTotal);
			$('input[name = "taxTotal"]').val(parseFloat(taxTotal).toFixed(numberOfDecimal));
		}else{
			$(".taxDetails").html("");
			//$('input[name ="taxamount"]').val("0");
			//alert("taxTotal     "+0);
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
		 
		 taxTotal = parseFloat(taxTotal).toFixed(numberOfDecimal); 
		 console.log("amount   "+parseFloat(amount));
		 console.log("orddiscountValue   "+parseFloat(orddiscountValue));
		 console.log("discountValue   "+parseFloat(discountValue));
		 console.log("taxTotal   "+parseFloat(taxTotal));
		 console.log("adjustmentvalue   "+parseFloat(adjustmentvalue));
		 
		 totalvalue = parseFloat(parseFloat(amount)- parseFloat(orddiscountValue) - parseFloat(discountValue) + parseFloat(taxTotal) + parseFloat(adjustmentvalue)).toFixed(numberOfDecimal);
		 console.log("totalvalue   "+totalvalue);
		 $("#total").html(totalvalue);
		 
		 $("#total_amount").val(totalvalue); // hidden input
		 
		 var CURRENCYUSEQT = $('input[name ="CURRENCYUSEQT"]').val();//Author: Resvi  Add date: July 28,2021  Description:  Total of Local Currency
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
	
	function removetaxdropdown(){
		$("#Purchasetax").typeahead('destroy');
	}
	function addtaxdropdown(){
		var plant = document.form.plant.value;
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
		$("#Purchasetax").typeahead('val', '');
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
	
	function changeitem(obj){
		var obj2 = $(obj).parent().find('input[name="item"]');
		$(obj2).typeahead('val', '');
		$(obj).parent().find('input[name="item"]').focus();
	}
	
	function productCallbackwithall(productData){
		if(productData.STATUS="SUCCESS"){
			var $tbody = $(".bill-table tbody");
			var $last = $tbody.find('tr:last');
			$last.remove();
			var taxdisplay = $("input[name=ptaxdisplay]").val();
//			var curency = document.form.curency.value;
			var curency = $("input[name=CURRENCYID]").val();
			var ITEM_DESC = escapeHtml(productData.ITEM_DESC);
			var body="";
			body += '<tr>';
			body += '<td class="item-img text-center">';
			body += '<img alt="" src="'+productData.CATLOGPATH+'" style="width: 100%;">';
			body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
			body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+taxdisplay+'">';
			body += '</td>';
			body += '<td class="bill-item-dbt">';
			body += '<input type="text" name="item" style="width:95%" class="form-control itemSearch" value="'+productData.ITEM+'" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
			body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
			body += '<input class="form-control"  name="ITEMDES" value="'+ITEM_DESC+'" readonly style="height: 23px;background-color: #fff;" readonly>';
			body += '</td>';
			body += '<td class="bill-acc">';
			body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Select Account" value="Inventory Asset">';
			body += '</td>';
			body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
			body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+productData.UNITCOST+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
			body += '<td class="item-discount text-right">';
			body += '<div class="row">';							
			body += '<div class=" col-lg-12 col-sm-3 col-12">';
			body += '<div class="input-group my-group" style="width:120px;">';
			body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00"  onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
			body += "<option value="+curency+">"+curency+"</option>";
			body += '<option>%</option>';										
			body += '</select>';
			body += '</div>';
			body += '</div>'; 
			body += '</div>';
			body += '</td>';
//			body += '<td class="item-tax">';
//			body += '<input type="hidden" name="tax_type">';
//			body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
//			body += '</td>';
			body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
			body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
			body += '<input name="amount" type="text" class="form-control text-right" value="'+productData.UNITCOST+'" readonly="readonly" style="display:inline-block;">';
			body += '<input name="landedCost" type="text" value="0.0" hidden>';
			body += '</td>';
			body += '</tr>';
		
		
			$(".bill-table tbody").append(body);
			calculateTotal();
			removeSuggestionToTable();
			addSuggestionToTable();
			tooltioedit();
		}
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
