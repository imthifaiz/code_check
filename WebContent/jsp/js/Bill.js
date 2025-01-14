var subWin = null;
var itemList="";
var taxList = [];
var zerotype=0;
var typeSwitch=false;
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
var billpostatus =   [{
    "id": "BILLED",
    "value": "BILLED",
    "tokens": [
      "BILLED"
    ]
  },
	  {
		    "id": "NOT BILLED",
		    "value": "NOT BILLED",
		    "tokens": [
		      "NOT BILLED"
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
	var numberOfDecimal = $("#numberOfDecimal").val();
	$('input[name ="cost"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	$('input[name ="item_discount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	$('input[name ="amount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	$("#subTotal").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#discount").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#shipping").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#adjustment").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#total").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	var shippono = document.form.pono.value;
	$("#shipmentModal #shipmentpono").val(shippono);
	//$("#SALES_LOC").val("Abu Dhabi");
	//document.form.STATE_PREFIX.value="AUH";
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
		    return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.TRANSPORTNAME+'\',\''+data.TRANSPORTID+'\',\''+data.PAYMENT_TERMS+'\',\''+data.PAY_TERMS+'\',\''+data.VENDO+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYUSEQT+'\')"><p class="item-suggestion">Name: ' + data.VNAME 
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
			menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#" onclick="document.form.custModal.value=\'cust\'"> + New Supplier</a></div>');
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
		}).on('typeahead:select',function(event,selection){
						$('.bill-table tbody tr').each(function () {
				$(this).find("input[name ='item']").focus();
				
			});
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.vendno.value = "";
				document.form.nTAXTREATMENT.value ="";
//				document.getElementById('nTAXTREATMENT').innerHTML="";
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
				getvendname(selection.TAXTREATMENT,selection.TRANSPORTNAME,selection.TRANSPORTID,selection.PAYMENT_TERMS,selection.PAY_TERMS,selection.VENDO,selection.CURRENCYID,selection.CURRENCY,selection.CURRENCYUSEQT);
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

	$('#vendnames').typeahead({
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
		    return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.TRANSPORTNAME+'\',\''+data.TRANSPORTID+'\',\''+data.PAYMENT_TERMS+'\',\''+data.PAY_TERMS+'\',\''+data.VENDO+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYUSEQT+'\')"><p class="item-suggestion">Name: ' + data.VNAME 
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
			menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#" onclick="document.form.custModal.value=\'cust\'"> + New Supplier</a></div>');
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
		}).on('typeahead:select',function(event,selection){
						$('.bill-table tbody tr').each(function () {
				$(this).find("input[name ='item']").focus();
				
			});
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.vendno.value = "";
				document.form.nTAXTREATMENT.value ="";
//				document.getElementById('nTAXTREATMENT').innerHTML="";
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
				getvendname(selection.TAXTREATMENT,selection.TRANSPORTNAME,selection.TRANSPORTID,selection.PAYMENT_TERMS,selection.PAY_TERMS,selection.VENDO,selection.CURRENCYID,selection.CURRENCY,selection.CURRENCYUSEQT);
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
	
	$('#billvendname').typeahead({
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
		    return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.TRANSPORTNAME+'\',\''+data.TRANSPORTID+'\',\''+data.VENDO+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYUSEQT+'\')"><p class="item-suggestion">Name: ' + data.VNAME 
		    + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME
		    + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
			}
		  }
		  
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
		});
		
		/* Order Type Auto Suggestion */
	$('#ORDERTYPE_SRH').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ORDERTYPE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
						ACTION : "GET_ORDERTYPE_DATA",
						Type : "BILL",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.ORDERTYPES);
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
		    return '<p>' + data.ORDERTYPE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			//menuElement.after( '<div class="ordertypeAddBtn footer"  data-toggle="modal" data-target="#orderTypeModal"><a href="#"> + New Order Type</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			$('.ordertypeAddBtn').show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.ordertypeAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
		
			/* Order Type Auto Suggestion */
	$('#ORDERTYPE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ORDERTYPE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
						ACTION : "GET_ORDERTYPE_DATA",
						Type : "BILL",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.ORDERTYPES);
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
		    return '<p>' + data.ORDERTYPE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="ordertypeAddBtn footer"  data-toggle="modal" data-target="#orderTypeModal"><a href="#"> + New Order Type</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			$('.ordertypeAddBtn').show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.ordertypeAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
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
	
	/* Shipping Customer Auto Suggestion */
	/*$('#SHIPPINGCUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
			//display: 'CNAME',
			display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			  var urlStr = "../MasterServlet";
				$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						//ACTION : "GET_CUSTOMER_DATA",
						ACTION : "GET_SUPPLIER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						//return asyncProcess(data.CUSTMST);
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
		    	//return '<div onclick="document.form.SHIPPINGID.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
		    	return '<div onclick="document.form.SHIPPINGID.value = \''+data.VENDO+'\'"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Mobile: ' + data.HPNO + '</p><br/><p class="item-suggestion">Address: ' + data.ADDR1 + '  '+ data.ADDR2 +'</p><p class="item-suggestion pull-right">Customer TelNo: ' + data.TELNO + '</p></br><p class="item-suggestion"> ' + data.ADDR3 + '  '+ data.ADDR4 +'</p><p class="item-suggestion pull-right">Email: ' + data.EMAIL + '</p></br><p class="item-suggestion"> ' + data.COUNTRY + '  '+ data.ZIP +'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}  */
			/*$('.shipCustomer-section .tt-menu').after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal" onclick="document.cpoform.custModal.value=\'shipcust\'"><a href="#" > + New Cutomer</a></div>');*/
		/*	menuElement.after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#supplierModal" onclick="document.form.custModal.value=\'shipcust\'"><a href="#" > + New Shipping Address</a></div>');
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
				document.cpoform.SHIPPINGID.value = "";
			}
		}); */
		
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
			getOrderDetailsForBilling(selection.PONO);
			$("#shipmentModal #shipmentpono").val(selection.PONO);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.pono.value = "";
			}
			/* To reset Order number Autosuggestion*/
			$("#grno").typeahead('val', '"');
			$("#grno").typeahead('val', '');
		});
		
		/* GRNO Number Auto Suggestion */
/*	$('#grno').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'GRNO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_GRNO_FOR_AUTO_SUGGESTION",
				CNAME : document.form.vendname.value,
				PONO : document.form.pono.value,
				GRNO : query
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
		    return '<p>' + data.GRNO + '</p>';
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
		});*/
	
	
	
	/* GRNO Number Auto Suggestion */
	$('#grno').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'GRNO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_GRNO_FOR_AUTO_SUGGESTION",
				PONO : document.form.pono.value,
				GRNO : query
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
		    return '<p>' + data.GRNO + '</p>';
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
	
	    
	
	/* Bill No Auto Suggestion */
	$('#bill').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'BILL',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/BillingServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_BILL_NO_FOR_AUTO_SUGGESTION_PO_RETURN",
				ORDERNO : document.form.pono.value,
				GRNO : document.form.grno.value,
				BILL : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.BillDetails);
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
		    return '<p>' + data.BILL + '</p>';
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
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
  	/* To get the suggestion data for Status */
	$(".status").typeahead({
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
			return '<div onclick="getCurrencyid(\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</div>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
	
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			$("input[name ='CURRENCYID']").val("");	
	});
	
	/* To get the suggestion data for Status */
	$(".billstatus").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'billpostatus',
	  display: 'value',  
	  source: substringMatcher(billpostatus),
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
						module:"billaccount",
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
	
	$(".bill-table tbody").on('click','.bill-action',function(){
		debugger;	    
	    var obj = $(this).closest('tr').find('td:nth-child(7)');
	    calculateTax(obj, "", "", "");
	    $(this).parent().parent().remove();
	    calculateTotal();
	});
	
	$("#btnBillDraft").click(function(){
		var isValid = validateOrderNo();
		if (!isValid){
			return;
		}
		$('input[name ="bill_status"]').val('Draft');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		calculateLandedCost();
		$("#createBillForm").submit();
	});
	$("#btnBillOpen").click(function(){
		var isValid = validateOrderNo();
		if (!isValid){
			return;
		}
		$('input[name ="bill_status"]').val('Open');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		calculateLandedCost();
		$("#createBillForm").submit();
	});
	
	$("#btnBillOpenEmail").click(function(){
		if ($('#supplier_email').val() == ''){
			alert('Supplier record does not have an email address. Please update supplier email and try again or Save as Draft OR Save as Open');
			return;
		}
		$('input[name ="bill_status"]').val('Open');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		calculateLandedCost();
		var isValid = validateBill();
		if (!isValid){
			return;
		}
		var isValidOrd = validateOrderNo();
		if (!isValidOrd){
			return;			
		}
		var data = new FormData($("#createBillForm")[0]); 
		$.ajax({
			type : "POST",
			url : "../billing/Save",
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
					$('#send_to').val($('#supplier_email').val()).multiEmail();
					$('#send_subject').val($('#template_subject').val()
											.replace(/\{COMPANY_NAME\}/, $('#plant_desc').val())
											.replace(/\{BILL_NO\}/, $('#bill').val())
											);
					$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(
								$('#template_body').val()
								.replace(/\{BILL_NO\}/, $('#bill').val())
								.replace(/\{SUPPLIER_NAME\}/, $('#vendname').val())
								);
					$('#send_attachment').val('Bill');
				}else if ($('.success-msg').length > 0){
					$('.success-msg').html(data.MESSAGE).addClass('error-msg').removeClass('success-msg').css('display', 'inline');
				}else if ($('.error-msg').length > 0){
					$('.error-msg').html(data.MESSAGE).css('display', 'inline');
				}
			}
		});
	});
	
	//IMTI modified on 14-03-2022 to display qty and cost hover
		$("input[name=qty]").mouseenter(function(){
		var content = "<p class='text-left'>Min Stock Quantity: "+$(this).data("rl")+"</p>";
		content += "<p class='text-left'>Max Stock Quantity: "+$(this).data("msq")+"</p>";
		content += "<p class='text-left'>Stock on Hand: "+$(this).data("soh")+"</p>";
		content += "<p class='text-left'>Estimated Quantity: "+$(this).data("eq")+"</p>";
		content += "<p class='text-left'>Available Quantity: "+$(this).data("aq")+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"}); 
	});
	
//	$("input[name=ITEMDES]").mouseenter(function(){
//		var content = "<p class='text-left'> "+$(this).closest('tr').find("input[name=ITEMDESH]").val()+"</p>";
//		$(this).tooltip({title: content, html: true, placement: "top"});
//	});

	$("input[name=cost]").mouseenter(function(){
		var customerdiscount = parseFloat($(this).closest('tr').find("input[name=customerdiscount]").val())
		var discounttype = $(this).closest('tr').find("input[name=discounttype]").val();
		var content = "";
		if(isNaN(customerdiscount)){
			content += "<p class='text-left'>Supplier Discount (By Cost or Percentage): 0.000</p>";
		}else{
			if(discounttype == "BYPERCENTAGE"){
				content += "<p class='text-left'>Supplier Discount (By Cost or Percentage): "+customerdiscount+"%</p>";
			}else{
				content += "<p class='text-left'>Supplier Discount (By Cost or Percentage): "+customerdiscount+"</p>";
			}
		}
		
		
		$(this).tooltip({title: content, html: true, placement: "top"});
	});
	//END
	
/*	$(document).on("focusout",".taxSearch",function(){
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
	if(($("#pono").val()) && ($("#grno").val())){
		getBillingDetailsByGRNO($("#pono").val(),$("#grno").val());
		$("input[name ='vendname']").typeahead('val', $("#vendname").val());
		$(".add-line").hide();
		$("#item_rates").attr("readonly", true);
	}else if($("#pono").val()){
		$("#shipmentModal #shipmentpono").val($("#pono").val());
		getOrderDetailsForBilling($("#pono").val());
		$("input[name ='vendname']").typeahead('val', $("#vendname").val());
		$(".add-line").hide();
		$("#item_rates").attr("readonly", true);
	}
	
	/* Shipment Auto Suggestion */
	$('#shipRef').typeahead({
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
					ORDERNO : document.form.pono.value,
					SHIPMENT_CODE : query
				},
				dataType : "json",
				success : function(data) {
					if(document.form.pono.value!="")
					return asyncProcess(data.items);
					else if(document.form.DEDUCT_INV.value=="1")
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
		    		return '<div onclick="getExpenseDetailForBill(\''+data.SHIPMENTCODE+'\',\''+data.PONO+'\')"><p>' + data.SHIPMENTCODE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			/*menuElement.after( '<div class="shipmentAddBtn footer"  data-toggle="modal" data-target="#shipmentModal"><a href="#"> + Add Shipment</a></div>');*/
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
	$(document).on("change",".itemChk",function(){
		debugger;
		if(this.checked){
			$(this).parent().parent().find('input[type="text"]').attr("disabled", false);
			var obj = $(this).parent().parent().find('td:nth-child(7)');
			calculatePartialTax(obj, "PURCHASE", "7.0", "PURCHASE [7.0%]");
		}else{
			$(this).parent().parent().find('input[type="text"]').attr("disabled", true);
			var obj = $(this).parent().parent().find('td:nth-child(7)');
			calculatePartialTax(obj, "", "", "");
		}	
		calculateTotal();
	});
	
	$(document).on("focusout","input[name ='shippingcost']",function(){
		var currentShippingCost = parseFloat($("input[name ='shippingcost']").val());
		var originalShippingCost = parseFloat($("input[name ='oShippingcost']").val());
		var action = $("input[name ='action']").val();
		if(action == "View"){
			if(currentShippingCost > originalShippingCost){
				alert("Cannot exceed original shipping cost of "+originalShippingCost);
				$("input[name ='shippingcost']").val(originalShippingCost);
			}
			
			var shipmentcode = $("input[name ='shipRef']").val();
			var pono = $("input[name ='pono']").val();
			if(pono.length > 0 && shipmentcode.length > 0){
				getExpenseDetailForBill(shipmentcode,pono);
			}
		}
		calculateTotal();
		
	});
	
	$(document).on("focusout","input[name ='orderdiscount']",function(){
		var currentDiscountCost = parseFloat($("input[name ='orderdiscount']").val());
		var originalDiscountCost = parseFloat($("input[name ='aorderdiscount']").val());
		if(currentDiscountCost > originalDiscountCost){
			alert("Cannot exceed original discount cost of "+originalDiscountCost);
			$("input[name ='orderdiscount']").val(originalDiscountCost);
		}
		calculateTotal();
		var shipmentcode = $("input[name ='shipRef']").val();
		var pono = $("input[name ='pono']").val();
		if(pono.length > 0 && shipmentcode.length > 0){
			getExpenseDetailForBill(shipmentcode,pono);
		}
	});
	
	
	
	$(document).on("focusout","input[name ='CURRENCYUSEQT']",function(){
		var CURRENCYUSEQTCost = parseFloat($("input[name ='CURRENCYUSEQT']").val());
		if(!Number.isNaN(CURRENCYUSEQTCost))
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});
	
	/* Bill No Auto Suggestion */
	$('#auto_billNo').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'BILL',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/BillingServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_BILL_NO_FOR_AUTO_SUGGESTION",
				ORDERNO : document.form.pono.value,
				CNAME : document.form.vendname.value,
				BILL : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.BillDetails);
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
		    return '<p>' + data.BILL + '</p>';
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
	body += '<input type="hidden" name="unitpricerd" value="">';
	body += '<input type="hidden" name="minstkqty" value="">';
	body += '<input type="hidden" name="maxstkqty" value="">';
	body += '<input type="hidden" name="stockonhand" value="">';
	body += '<input type="hidden" name="incommingqty" value="">';
	body += '<input type="hidden" name="outgoingqty" value="">';
	body += '<input type="hidden" name="itemdesc" value="">';
	body += '<input type="hidden" name="customerdiscount" value="">';
	body += '<input type="hidden" name="unitpricediscount" value="">';
	body += '<input type="hidden" name="discounttype" value="">';
	body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
	body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+taxdisplay+'">';
	body += '<input type="hidden" name="convcost" value="0.00">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" class="form-control itemSearch" onchange="checkitems(this.value,this)" style="width:92%" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>'
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="account_name" class="form-control accountSearch" value="Inventory Asset" placeholder="Select Account">';
	body += '</td>';
	body += '<td class="invEl">';
	body += '<input type="text" name="uom" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="UOM">';
	body += '</td>';
	body += '<td class="invEl">';
	body += '<input type="text" name="loc" class="form-control locSearch" onchange="checkprdloc(this.value,this)" placeholder="Location">';
	body += '</td>';
	body += '<td class="invEl"><div class="input-group">';
	body += '<input type="text" name="batch" value="NOBATCH" class="form-control batchSearch" placeholder="Batch">';
	body += '<span class="input-group-addon" onclick="javascript:generateBatch(this);return false;" id="actionBatch" name="actionBatch">';
	body += '<a href="#" data-toggle="tooltip" data-placement="top" title="Generate">';
	body += '<i class="glyphicon glyphicon-edit"></i></a></span>';
	body += '</div></td>';
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
	
	if($("input[name=DEDUCT_INV]").val() == "0"){
		$(".invEl").hide();
	}
	

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

function checkprduom(uom,obj){	
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
						$(obj).typeahead('val', '');
						$(obj).parent().find('input[name="uom"]').focus();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkprdloc(loc,obj){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				LOC : loc,
				ACTION : "LOC_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Location Does't Exists");
						$(obj).typeahead('val', '');
						$(obj).parent().find('input[name="loc"]').focus();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
//table validation ends

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

function checktransport(transport){	
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

function checkorderno(orderno){	
		var urlStr = "/track/billing/CheckOrderno";
		$.ajax( {
			type : "GET",
			url : urlStr,
			async : true,
			data : {
				BILL : orderno,
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						alert("Order Number Already Exists");
						document.getElementById("bill").focus();
					//	document.getElementById("bill").value="";
					$("#bill").typeahead('val',"");
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

function setITEMDESC(obj,desc){
		$(obj).closest('tr').find("input[name ='ITEMDES']").val(desc);
	}


//customer popup changes end

function addSuggestionToTable(){
	var plant = document.form.plant.value;
	
	/* To get the suggestion data for Product */
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
			//return '<div onclick="loadItemData(this,\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.COST+'\',\''+ data.PURCHASEUOM+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.PURCHASEINVQTY+' '+data.PURCHASEUOM+'</p></div>';
			return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.PURCHASEINVQTY+' '+data.PURCHASEUOM+'</p></div>';
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
		}).on('typeahead:select',function(event,selection){
			loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.COST,selection.PURCHASEUOM,selection.UNITPRICE);
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
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
					module:"billaccount",
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
			  '<div style="padding:3px 20px;">',
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
		menuElement.after( '<div class="itemAddBtn footer accrmv"   data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
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
	
	

	
	/* Sales Location Auto Suggestion */
/*	$('#SALES_LOC').typeahead({
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
			var action = document.form.action.value;
			if(action == "View"){
				changetax();
			}else{
				changetaxnew();
			}
		});*/
	
		/*$(".taxSearch").on('focus', function(e){
			$(".taxSearch").typeahead('val', '"');
			$(".taxSearch").typeahead('val', '');	
		});*/
	$(document).on("focus", ".taxSearch" , function() {
		$(this).typeahead('val', '"');
		$(this).typeahead('val', '');	
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
			//return '<div><p class="item-suggestion">'+data.UOM+'</p></div>';
			return '<div onclick="CheckPriceVal(this,\''+data.UOM+'\')"><p class="item-suggestion">'+data.UOM+'</p></div>';
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
		}).on('typeahead:select',function(event,selection){
			$(this).closest('tr').find('input[name="qty"]').focus();
			$(this).closest('tr').find('input[name="qty"]').select();
		});	
		
		$('.bill-table tbody tr').each(function () {
		$(this).find("input[name ='item']").focus();
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
	$(".accountSearch").typeahead('destroy');
	$(".taxSearch").typeahead('destroy');
	$(".uomSearch").typeahead('destroy');
	$(".locSearch").typeahead('destroy');
	$(".batchSearch").typeahead('destroy');
}


function CheckPriceVal(obj, uom) {
	var productId = $(obj).closest('tr').find("td:nth-child(2)").find('input[name=item]').val();
	var pono = $("input[name=DONO]").val();
	var desc = "";
    var disc = $(obj).closest('tr').find("input[name=customerdiscount]").val();
    var numberOfDecimal = $("input[name=numberOfDecimal]").val();
    var currencyID = $("input[name=CURRENCYID]").val();
    var currencyUSEQT = $("input[name=CURRENCYUSEQT]").val();
	if((productId=="" || productId.length==0) && (desc == "" ||desc.length == 0)) {
		alert("Enter Product ID/Description !");
		document.form.ITEM.focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				DESC : desc,
				PLANT :document.form.plant.value,
				CURRENCY:currencyID,
	            UOM:uom,
	            TYPE:"Purchase",
	            DISC:disc.replace("%",""),
	            MINPRICE:1,
				ACTION : "VALIDATE_PRODUCT_UOM_CURRENCY_PURCHASE"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;						
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							$(obj).closest('tr').find("input[name=cost]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
						}else{
							$(obj).closest('tr').find("input[name=cost]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricerd]").val(resultVal.ConvertedUnitCostWTC.match(regex)[0]);
						}
						//alert(disc);
						//alert(resultVal.ConvertedUnitCost);
	                   if(parseFloat(disc)>0)
	                    {
	                    	if($(obj).closest('tr').find("input[name=discounttype]").val() == "BYPERCENTAGE")
							{								 
	                    		var getdist = disc.replace("%","");								
								discount = parseFloat((resultVal.ConvertedUnitCost*getdist)/100);
								price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*getdist)/100));
							}
							else
							{	
								var getdist = disc.replace("%","");
								price = parseFloat(resultVal.ConvertedUnitCost-(getdist*currencyUSEQT));							
								//price = parseFloat(resultVal.ConvertedDiscWTC);
	 						}
	                    	var calAmount = parseFloat(price).toFixed(numberOfDecimal);						
							$(obj).closest('tr').find("input[name=cost]").val(parseFloat(calAmount).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
							$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);
							
	                    }
	                   	//calculateAmount(obj);
	                   calculateAmount(obj);
						loadUnitPriceToolTip(obj);
						loadItemDescToolTip(obj);
					} else {
						
					}
				}
			});
	}
}

//IMTI modified on 14-03-2022 to display qty and cost hover
function loadItemData(obj,productId, catalogPath, account, cost,purchaseuom,price){
//	var numberOfDecimal = $("#numberOfDecimal").val();
//	
//	$(obj).closest('tr').find("td:nth-child(1)").find('input[name=basecost]').val(cost);
//	
//	cost = parseFloat($("input[name ='CURRENCYUSEQT']").val()) * parseFloat(cost);
//	cost = parseFloat(cost).toFixed(numberOfDecimal);
//	$(obj).closest('tr').find("input[name=uom]").val(purchaseuom);
//	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
//	$(obj).closest('tr').find("td:nth-child(3)").find('input').typeahead('val', account);
//	//$(obj).closest('tr').find("td:nth-child(5)").find('input').val(cost);
//	$(obj).closest('tr').find("input[name=cost]").val(cost);
//	loadQtyToolTip(obj);
//	calculateAmount(obj);
//}
$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("input[name=cost]").val(parseFloat(cost));
	$(obj).closest('tr').find('input[name = "account_name"]').val(account);
	$(obj).closest('tr').find("input[name=uom]").val(purchaseuom);
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var currencyID = $("input[name=CURRENCYID]").val();
	var custCode = $("input[name=CUST_CODE]").val();
	
	 var urlStr = "/track/ItemMstServlet";
	 var discount;
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				CURRENCY : currencyID,
				VENDNO : document.form.vendno.value,
				//ACTION : "GET_PRODUCT_DETAILS_PURCHASE"
				ACTION : "GET_PURCHASE_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					//incomingIBDiscount
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
						
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							$(obj).closest('tr').find("input[name=cost]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
						}else{
							var covcalAmount = parseFloat($("input[name ='CURRENCYUSEQT']").val()) * parseFloat(resultVal.cost);
							var covcalAmountwtc = parseFloat(covcalAmount).toFixed(numberOfDecimal);
							$(obj).closest('tr').find("input[name=cost]").val(parseFloat(covcalAmount).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricerd]").val(covcalAmountwtc.match(regex)[0]);
						}if(resultVal.stkqty == null || resultVal.stkqty == undefined || resultVal.stkqty == 0  ){
							$(obj).closest('tr').find("input[name=minstkqty]").val("0.000");
						}else{
							$(obj).closest('tr').find("input[name=minstkqty]").val(resultVal.stkqty.match(regex)[0]);
						}if(resultVal.maxstkqty == null || resultVal.maxstkqty == undefined || resultVal.maxstkqty == 0){
							$(obj).closest('tr').find("input[name=maxstkqty]").val("0.000");
						}else{
							$(obj).closest('tr').find("input[name=maxstkqty]").val(resultVal.maxstkqty.match(regex)[0]);
						}if(resultVal.stockonhand == null || resultVal.stockonhand == undefined || resultVal.stockonhand == 0){
							$(obj).closest('tr').find("input[name=stockonhand]").val("0.000");
						}else{
							$(obj).closest('tr').find("input[name=stockonhand]").val(resultVal.stockonhand.match(regex)[0]);
						}if(resultVal.incommingQty == null || resultVal.incommingQty == undefined || resultVal.incommingQty == 0){
							$(obj).closest('tr').find("input[name=incommingqty]").val("0.000");
						}else{
							$(obj).closest('tr').find("input[name=incommingqty]").val(parseFloat(resultVal.incommingQty).toFixed(3));
						}if(resultVal.outgoingqty == null || resultVal.outgoingqty == undefined || resultVal.outgoingqty == 0){
							$(obj).closest('tr').find("input[name=outgoingqty]").val("0.000");
						}else{
							$(obj).closest('tr').find("input[name=outgoingqty]").val(resultVal.outgoingqty.match(regex)[0]);	
						}
                     	

						//i/Convertedmprice=resultVal.minSellingConvertedUnitCost;
						//i/$(obj).closest('tr').find("input[name=minsp]").val(Convertedmprice);
						
						if(resultVal.incomingIBDiscount=='' || resultVal.incomingIBDiscount=='0' ||resultVal.incomingIBDiscount=='0.00'||resultVal.incomingIBDiscount==undefined)
						{
							$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricediscount]").val(resultVal.ConvertedUnitCost);
						}
						else
						{
							if(resultVal.IBDiscountType=="BYPERCENTAGE")
							{
								$(obj).closest('tr').find("input[name=discounttype]").val("BYPERCENTAGE");
								$(obj).closest('tr').find("input[name=customerdiscount]").val(resultVal.incomingIBDiscount.match(regex)[0]+'%');
								
								discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.incomingIBDiscount)/100);
								price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*resultVal.incomingIBDiscount)/100));
							}
							else
							{
								$(obj).closest('tr').find("input[name=discounttype]").val("");
								$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat(resultVal.incomingIBDiscount).toFixed(numberOfDecimal));
								var cruDisAmount = parseFloat($("input[name ='CURRENCYUSEQT']").val()) * parseFloat(resultVal.incomingIBDiscount);
								discount = parseFloat(resultVal.ConvertedUnitCost-cruDisAmount);
								price = parseFloat(discount);
							}
							var calAmount = parseFloat(price).toFixed(numberOfDecimal);
							var covcalAmount = parseFloat(price);
							covcalAmount = parseFloat(covcalAmount).toFixed(numberOfDecimal);
							$(obj).closest('tr').find("input[name=cost]").val(covcalAmount);
							$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
							$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);
						}
					} 
					
					
					$(obj).closest('tr').find("input[name=qty]").data('rl',resultVal.stkqty);
					$(obj).closest('tr').find("input[name=qty]").data('msq',resultVal.maxstkqty);
					$(obj).closest('tr').find("input[name=qty]").data('soh',resultVal.stockonhand);
					$(obj).closest('tr').find("input[name=qty]").data('eq',resultVal.EstQty);
					$(obj).closest('tr').find("input[name=qty]").data('aq',resultVal.AvlbQty);
					$(obj).closest('tr').find("input[name=ITEMDES]").data('pd',resultVal.prdesc);
					
					calculateAmount(obj);
					loadQtyToolTip(obj);
					loadUnitPriceToolTip(obj);
					loadItemDescToolTip(obj);
					if($("input[name=DEDUCT_INV]").val() == "0")
					{ 
					$(obj).closest('tr').find('input[name="qty"]').focus();
					$(obj).closest('tr').find('input[name="qty"]').select();
					}else{
					$(obj).closest('tr').find('input[name="loc"]').focus();
					$(obj).closest('tr').find('input[name="loc"]').select();
					}
				}
			});
		
	}
//IMTI modified on 14-03-2022 to display qty and cost hover
function loadQtyToolTip(obj){
	//$(obj).closest('tr').find("input[name=qty]").tooltip("destroy");
	var content = "<p class='text-left'>Min Stock Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("rl")+"</p>";
	content += "<p class='text-left'>Max Stock Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("msq")+"</p>";
	content += "<p class='text-left'>Stock on Hand: "+$(obj).closest('tr').find("input[name=qty]").data("soh")+"</p>";
	content += "<p class='text-left'>Incomming Quantity: "+$(obj).closest('tr').find("input[name=incommingqty]").val()+"</p>";
	content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(obj).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
	content += "<p class='text-left'>Sales Estimate Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("eq")+"</p>";
	content += "<p class='text-left'>Available Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("aq")+"</p>";	
	//$(obj).closest('tr').find("input[name=qty]").tooltip({title: content, html: true, placement: "top"}); 
	$(obj).closest('tr').find("input[name=qty]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"}); 
}

function loadItemDescToolTip(obj){
//$(obj).closest('tr').find("input[name=ITEMDES]").tooltip("destroy");
	var content = "<p class='text-left'>"+$(obj).closest('tr').find("input[name=ITEMDES]").data("pd")+"</p>";
	//$(obj).closest('tr').find("input[name=ITEMDES]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}


function loadUnitPriceToolTip(obj){
//$(obj).closest('tr').find("input[name=cost]").tooltip("destroy");
	
	var content = "";
	var customerdiscount = parseFloat($(obj).closest('tr').find("input[name=customerdiscount]").val());
	var discounttype = $(obj).closest('tr').find("input[name=discounttype]").val();
	if(isNaN(customerdiscount)){
		content += "<p class='text-left'>Supplier Discount (By Cost or Percentage): 0.000</p>";
	}else{
		if(discounttype == "BYPERCENTAGE"){
			content += "<p class='text-left'>Supplier Discount (By Cost or Percentage): "+customerdiscount+"%</p>";
		}else{
			content += "<p class='text-left'>Supplier Discount (By Cost or Percentage): "+customerdiscount+"</p>";
		}
		
	}
	
	//$(obj).closest('tr').find("input[name=cost]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=cost]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}
//END

/*function calculateAmount(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	var action = document.form.action.value;
	var state = document.form.STATE_PREFIX.value;
	var plant = document.form.plant.value;
	if(action == "View"){
		var qty = parseFloat($(obj).closest('tr').find("td:nth-child(5)").find('input').val()).toFixed(3);
		var cost = parseFloat($(obj).closest('tr').find("td:nth-child(6)").find('input').val()).toFixed(numberOfDecimal);
		var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(7)").find('input').val()).toFixed(numberOfDecimal);
		var discounType = $(obj).closest('tr').find("td:nth-child(7)").find('select').val();
		var itemDiscountval=itemDiscount;
		 if(discounType == "%"){
			 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
			 itemDiscount = parseFloat(itemDiscount).toFixed(3);
		 }
		var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
		
		if(parseFloat(amount) >= parseFloat("0")){
			$(obj).closest('tr').find("td:nth-child(7)").find('input').val(itemDiscount);
			$(obj).closest('tr').find("td:nth-child(9)").find('input').val(amount);
		}else{
			alert("discout should be less than the amount");
			if(discounType == "%"){
				$(obj).closest('tr').find("td:nth-child(7)").find('input').val(parseFloat("0").toFixed(3));
			}else{
				$(obj).closest('tr').find("td:nth-child(7)").find('input').val(parseFloat("0").toFixed(numberOfDecimal));
			}
			var originalamount = parseFloat((qty*cost)).toFixed(numberOfDecimal);
			$(obj).closest('tr').find("td:nth-child(9)").find('input').val(originalamount);
		}
		
		$(obj).closest('tr').find("td:nth-child(5)").find('input').val(qty);
		$(obj).closest('tr').find("td:nth-child(6)").find('input').val(cost);
		
		changetaxfordiscount();
		calculateTotal();
	   
	}else{
		var qty = parseFloat($(obj).closest('tr').find("td:nth-child(4)").find('input').val()).toFixed(3);
		var cost = parseFloat($(obj).closest('tr').find("td:nth-child(5)").find('input').val()).toFixed(numberOfDecimal);
		var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(6)").find('input').val()).toFixed(numberOfDecimal);
		var amount = parseFloat(((qty*cost)-itemDiscount)).toFixed(numberOfDecimal);
		
		$(obj).closest('tr').find("td:nth-child(4)").find('input').val(qty);
		$(obj).closest('tr').find("td:nth-child(5)").find('input').val(cost);
		$(obj).closest('tr').find("td:nth-child(6)").find('input').val(itemDiscount);
		$(obj).closest('tr').find("td:nth-child(8)").find('input').val(amount);
		
		var objtax = $(obj).closest('tr').find('td:nth-child(7)');
		var taxname = $(obj).closest('tr').find('input[name = "tax_type"]').val();
		var urlStr = "/track/MasterServlet";
		$.ajax({
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_GST_TYPE_DATA",
			SALESLOC : state,
			GSTTYPE : ""
		},
		dataType : "json",
		success : function(data) {
			 $.each(data.records, function(i, item) {
				 if(taxname == item.DISPLAY){
					 taxList = [];
					 calculateTax(objtax,item.SGSTTYPES,item.SGSTPERCENTAGE,item.DISPLAY);
				 }    
			});
		}
		});
		calculateTotal();
	}
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
		
		changetaxfordiscountnew();
		calculateTotal();
	}
}*/

function calculateAmountcost(obj,ocost){
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	var str = ocost;
	if (str.indexOf('.') == -1) str += ".";
	var decNum = str.substring(str.indexOf('.')+1, str.length);
	var declength =	parseInt(numberOfDecimal);
	
	if (decNum.length > declength)
	{
		alert("Invalid more than "+declength+" digits after decimal in Unit Cost");
		return false;
		
	}
	else
		{
		
	
	var action = document.form.action.value;
	if(action == "View"){
		var qty = parseFloat($(obj).closest('tr').find("td:nth-child(5)").find('input').val()).toFixed(3);
		var costcheck = parseFloat($(obj).closest('tr').find("td:nth-child(6)").find('input').val()).toFixed(numberOfDecimal);
		var cost=0;
		if(ocost <= costcheck){
			cost=costcheck;
		}else{
			alert("Unit Cost should be greater than the Order Cost");
			cost=parseFloat(ocost).toFixed(numberOfDecimal);
		}
		var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(7)").find('input').val()).toFixed(numberOfDecimal);
		var amount = parseFloat(((qty*cost)-itemDiscount)).toFixed(numberOfDecimal);
		
		$(obj).closest('tr').find("td:nth-child(5)").find('input').val(qty);
		$(obj).closest('tr').find("td:nth-child(1)").find('input[name=convcost]').val(cost);
		$(obj).closest('tr').find("td:nth-child(6)").find('input').val(cost);
		$(obj).closest('tr').find("td:nth-child(7)").find('input').val(itemDiscount);
		$(obj).closest('tr').find("td:nth-child(9)").find('input').val(amount);
		calculateTotal();
	}else{
		var qty = parseFloat($(obj).closest('tr').find("td:nth-child(4)").find('input').val()).toFixed(3);
		var costcheck = parseFloat($(obj).closest('tr').find("td:nth-child(5)").find('input').val()).toFixed(numberOfDecimal);
		var cost=0;
		if(ocost <= costcheck){
			cost=costcheck;
		}else{
			alert("Unit Cost should be greater than the Order Cost");
			cost=parseFloat(ocost).toFixed(numberOfDecimal);
		}
		var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(6)").find('input').val()).toFixed(numberOfDecimal);
		var amount = parseFloat(((qty*cost)-itemDiscount)).toFixed(numberOfDecimal);
		
		$(obj).closest('tr').find("td:nth-child(4)").find('input').val(qty);
		$(obj).closest('tr').find("td:nth-child(5)").find('input').val(cost);
		$(obj).closest('tr').find("td:nth-child(6)").find('input').val(itemDiscount);
		$(obj).closest('tr').find("td:nth-child(8)").find('input').val(amount);
		calculateTotal();
	}
		}
}

/*function calculateTotal(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = 0, discountValue=0, shippingvalue=0, adjustmentvalue=0,totalvalue=0,orderdisc=0;
	
	var discount= document.form.discount.value;
	discount = parseFloat(discount).toFixed(numberOfDecimal);
	$("input[name ='discount']").val(discount);
	
	var shippingcost= document.form.shippingcost.value;
	shippingcost = parseFloat(shippingcost).toFixed(numberOfDecimal);
	$("input[name ='shippingcost']").val(shippingcost);
	
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

function renderTaxDetails(){
	var html="";
	var taxTotal = 0;
	var subtotal =0;
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	var percentageget = $('input[name ="taxpercentage"]').val();
	var shipingcost = document.form.shippingcost.value;
	var shipingtax = parseFloat(shipingcost*(percentageget/100)).toFixed(numberOfDecimal);
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
	 
	 
	$.each(taxList, function( key, data ) {
		var mydata=JSON.stringify(data);
		console.log("Tax dta:"+mydata);
		var shtax = $('input[name ="shiptaxstatus"]').val();
		var originalTaxType= data.types;
		var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); 
		console.log("Tax Type:"+taxtype);
		console.log("Zero Type:"+zerotype);
		if(data.value > 0 || taxtype=="ZERO RATE" && zerotype>0){
			var myJSON = JSON.stringify(data);
			console.log("Tax Data:"+myJSON);
			html+='<div class="total-row">';
			html+='<div class="total-label">'+data.display+'</div>';
			if(taxtype=="ZERO RATE"){
				html+='<div class="total-amount taxAmount">'+data.value+'</div>';
			}else{
				if(shtax == "1"){
					html+='<div class="total-amount taxAmount">'+parseFloat((parseFloat(data.value)+parseFloat(shipingtax))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal)+'</div>';
				}else{
					html+='<div class="total-amount taxAmount">'+parseFloat((parseFloat(data.value))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal)+'</div>';
				}
				
			}
			
			html+='</div>';
			if(shtax == "1"){
				taxTotal += parseFloat((parseFloat(data.value)+parseFloat(shipingtax))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal);
			}else{
				taxTotal += parseFloat((parseFloat(data.value))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal);
			}
			
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
	
	
	/*var totalvalue = parseFloat($("#total_amount").val());
	
	totalvalue = (parseFloat(totalvalue) + parseFloat(taxTotal)).toFixed(numberOfDecimal);
	
	$("#total").html(totalvalue);
	$("#total_amount").val(totalvalue);// hidden input
*/}


function currencyTaxDetails(){
	var html="";
	var taxTotal = 0;
	var subtotal =0;
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	var percentageget = $('input[name ="taxpercentage"]').val();
	var shipingcost = document.form.shippingcost.value;
	var shipingtax = parseFloat(shipingcost*(percentageget/100)).toFixed(numberOfDecimal);
	var discount = document.form.discount.value;
	var discounType = $('select[name ="discount_type"]').val();
	$(".bill-table tbody tr td:last-child").each(function() {
		
			subtotal =  parseFloat(subtotal) + parseFloat($(this).find('input').val());
		
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
	 
	 
	$.each(taxList, function( key, data ) {
		var mydata=JSON.stringify(data);
		console.log("Tax dta:"+mydata);
		var shtax = $('input[name ="shiptaxstatus"]').val();
		var originalTaxType= data.types;
		var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); 
		console.log("Tax Type:"+taxtype);
		console.log("Zero Type:"+zerotype);
		if(data.value > 0 || taxtype=="ZERO RATE" && zerotype>0){
			var myJSON = JSON.stringify(data);
			console.log("Tax Data:"+myJSON);
			html+='<div class="total-row">';
			html+='<div class="total-label">'+data.display+'</div>';
			if(taxtype=="ZERO RATE"){
				html+='<div class="total-amount taxAmount">'+data.value+'</div>';
			}else{
				if(shtax == "1"){
					html+='<div class="total-amount taxAmount">'+parseFloat((parseFloat(data.value)+parseFloat(shipingtax))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal)+'</div>';
				}else{
					html+='<div class="total-amount taxAmount">'+parseFloat((parseFloat(data.value))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal)+'</div>';
				}
				
			}
			
			html+='</div>';
			if(shtax == "1"){
				taxTotal += parseFloat((parseFloat(data.value)+parseFloat(shipingtax))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal);
			}else{
				taxTotal += parseFloat((parseFloat(data.value))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal);
			}
			
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
	var bill = document.form.bill.value;
	var billDate = document.form.bill_date.value;
	var itemRates = document.form.item_rates.value;
	var discount = document.form.discount.value;
	var discountAccount = document.form.discount_account.value;
	var Currency = document.form.CURRENCY.value;
	var CURRENCYUSEQT = document.form.CURRENCYUSEQT.value;
	var isEmployee = $("input[name=ISEMPLOYEEVALIDATEPO]").val();
	var Employeee = $("input[name=EMP_NAME]").val();
	
	var deductInv = document.form.DEDUCT_INV.value;
	var origin = document.form.ORIGIN.value;
	
	var isItemValid = true, isAccValid = true, isuomValid = true, islocValid = true, isbatchValid = true,isUnitPriceValid = true;
	//imti start
	var ValidNumber   = document.form.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" order's you can create"); return false; }
	//imti end
	if(supplier == ""){
		alert("Please select a Supplier.");
		return false;
	}
	if(bill == ""){
		alert("Please enter the bill number.");
		return false;
	}
	if(billDate == ""){
		alert("Please enter a valid bill date.");
		return false;
	}
	
	if(isEmployee == "1"){
		if(Employeee == ""){
			alert("Please select a Employee.");
			document.getElementById("EMP_NAME").focus();
			return false;
		}
	}
	
	if(Currency == ""){
		alert("Please select a Currency.");
		return false;
	}
	if(CURRENCYUSEQT == ""){
		alert("Please select a Exchange Rate.");
		return false;
	}
	
/*	if(discount != "" && discountAccount == ""){
			if(discount>0)
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
	
	/*$("input[name ='cost']").each(function() {
		if($(this).val() <= 0){
			isUnitPriceValid =  false;
	    }
	});	
	
	if(!isUnitPriceValid){
		alert("Unit Cost cannot be Zero ");
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
	if(deductInv == "1" && origin == "manual"){
	$("input[name ='uom']").each(function() {
		if($(this).val() == ""){
			isuomValid =  false;
	    }
	});	
	
	if(!isuomValid){
		alert("The UOM field cannot be empty.");
		return false;
	}
	
	$("input[name ='loc']").each(function() {
		if($(this).val() == ""){
			islocValid =  false;
	    }
	});	
	
	if(!islocValid){
		alert("The Location field cannot be empty.");
		return false;
	}
	
	$("input[name ='batch']").each(function() {
		if($(this).val() == ""){
			isbatchValid =  false;
	    }
	});	
	
	if(!isbatchValid){
		alert("The Batch field cannot be empty.");
		return false;
	}
	}
	$('#SALES_LOC').prop('disabled',false);
	$('#oddiscount_type').prop('disabled',false);
	
	return true;
}

function validateOrderNo(){
	var bill = document.form.bill.value;
	if(bill != ""){
		var urlStr = "/track/billing/CheckOrderno";
		$.ajax( {
			type : "GET",
			url : urlStr,
			async : false,
			data : {
				BILL : bill,
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						alert("Order Number Already Exists");
						document.getElementById("bill").focus();
						$("#bill").typeahead('val',"");
							return false;	
					}
				}
			});
		}
		return true;
}

function supplierCallback(data){
	if(data.STATUS="SUCCESS"){
		alert(data.MESSAGE);
		$("#vendname").typeahead('val', data.VENDOR_NAME);
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


function checkordertype(ordertype){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ORDERTYPE : ordertype,
				ACTION : "ORDERTYPE_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Order Type Does't Exists");
						document.getElementById("ORDERTYPE").focus();
						$("#ORDERTYPE").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}


function employeeCallback(data){
	if(data.STATUS="SUCCESS"){
		//alert(data.MESSAGE);
		$("#EMP_NAME").typeahead('val', data.EMP_NAME);
	}
}

function orderTypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#ORDERTYPE").typeahead('val', data.ORDERTYPE);
	}
}

function transportCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
	}
}

function productCallbackwithall(productData){
	if(productData.STATUS="SUCCESS"){
		var $tbody = $(".bill-table tbody");
		var $last = $tbody.find('tr:last');
		var taxdisplay = $("input[name=ptaxdisplay]").val();
		$last.remove();
//		var curency = document.form.curency.value;
		var curency = $("input[name ='CURRENCYID']").val();
		var ITEM_DESC = escapeHtml(productData.ITEM_DESC);
		var catalogPath=productData.CATLOGPATH;
		var account="Local sales - retail";
		var cost=productData.UNITCOST;
//		var price=productData.UNITCOST;
		var purchaseuom=productData.PURCHASEUOM;
		var productId=productData.ITEM;
		var body="";
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+productData.CATLOGPATH+'" style="width: 100%;">';
		body += '<input type="hidden" name="unitpricerd" value="">';
		body += '<input type="hidden" name="minstkqty" value="">';
		body += '<input type="hidden" name="maxstkqty" value="">';
		body += '<input type="hidden" name="stockonhand" value="">';
		body += '<input type="hidden" name="incommingqty" value="0.000">';
		body += '<input type="hidden" name="outgoingqty" value="">';
		body += '<input type="hidden" name="itemdesc" value="">';
		body += '<input type="hidden" name="customerdiscount" value="">';
		body += '<input type="hidden" name="unitpricediscount" value="">';
		body += '<input type="hidden" name="discounttype" value="">';
		body += '<input type="hidden" name="minsp" value="">';
		body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
		body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+taxdisplay+'">';
		body += '<input type="hidden" name="ITEMDESH" value="'+ITEM_DESC+'">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control itemSearch" style="width:92%" value="'+productData.ITEM+'" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
		body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
		body += '<input class="form-control"  name="ITEMDES" value="'+ITEM_DESC+'" readonly style="height: 23px;background-color: #fff;" readonly>';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="account_name" class="form-control accountSearch"  value="Inventory Asset" placeholder="Select Account">';
		body += '</td>';
		body += '<td class="invEl">';
		body += '<input type="text" name="UOM" class="form-control uomSearch"  value="'+productData.PURCHASEUOM+'"  placeholder="UOM">';
		body += '</td>';
		body += '<td class="invEl">';
		body += '<input type="text" name="loc" class="form-control locSearch" placeholder="Location">';
		body += '</td>';
		body += '<td class="invEl"><div class="input-group">';
		body += '<input type="text" name="batch" value="NOBATCH" class="form-control batchSearch" placeholder="Batch">';
		body += '<span class="input-group-addon" onclick="javascript:generateBatch(this);return false;" id="actionBatch" name="actionBatch">';
		body += '<a href="#" data-toggle="tooltip" data-placement="top" title="Generate">';
		body += '<i class="glyphicon glyphicon-edit"></i></a></span>';
		body += '</div></td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+parseFloat(productData.UNITCOST).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
		body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
		body += "<option value="+curency+">"+curency+"</option>";
		body += '<option>%</option>';										
		body += '</select>';
		body += '</div>';
		body += '</div>'; 
		body += '</div>';
		body += '</td>';
//		body += '<td class="item-tax">';	
//		body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
//		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
//		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+parseFloat(productData.UNITCOST).toFixed(numberOfDecimal)+'" readonly="readonly" style="display:inline-block;" tabindex="-1">';
		body += '<input name="landedCost" type="text" value="0.0" hidden>';
		body += '</td>';
		body += '</tr>';
		
		$(".bill-table tbody").append(body);
		calculateTotal();
		removeSuggestionToTable();
		addSuggestionToTable();
		loadItemData($tbody.find('tr:last'), productId,catalogPath, account, cost,purchaseuom,'');
		tooltipedit();
	}
	if($("input[name=DEDUCT_INV]").val() == "0"){
		$(".invEl").hide();
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
		
		$("input[name ='account_name']").typeahead('val', accountData.ACCOUNT_NAME);
	}
}

function productCallback(productData){
	if(productData.STATUS="SUCCESS"){
		
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

function getBillingDetailsByGRNO(pono,grno){
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/purchaseorderservlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_BILLING_DETAILS_BY_GRNO",
			PONO : pono,
			GRNO : grno
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
	var numberOfDecimal = $("#numberOfDecimal").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var curency = document.form.CURRENCYID.value;
	var STATE_PREFIX= document.form.STATE_PREFIX.value;
	var PO_STATE_PREFIX= document.form.PO_STATE_PREFIX.value;
	$.each(orders, function( key, data ) {
		if(PO_STATE_PREFIX!="")
			{	
			}
		else
			{
			STATE_PREFIX=data.STATE_PREFIX;
			$("input[name ='SALES_LOC']").val(data.PURCHASE_LOCATION);
			$("input[name ='STATE_PREFIX']").val(data.STATE_PREFIX);
			}
		var tamount = "0";
		var discamount = "0"
		if(data.ITEM_DISCOUNT_TYPE == "%"){
			var totalamount = parseFloat(data.AMOUNT) - ((parseFloat(data.AMOUNT)/100)*(parseFloat(data.ITEM_DISCOUNT)));
			tamount = parseFloat(totalamount).toFixed(numberOfDecimal);
		}else{
			var totalamount = parseFloat(data.AMOUNT) - ((parseFloat(data.ITEM_DISCOUNT)/parseFloat(data.ORQTY))*(parseFloat(data.QTYOR)));
			tamount = parseFloat(totalamount).toFixed(numberOfDecimal);	
			discamount = parseFloat((parseFloat(data.ITEM_DISCOUNT)/parseFloat(data.ORQTY))*(parseFloat(data.QTYOR))).toFixed(numberOfDecimal);
		}
		
		var ITEMDESC = escapeHtml(data.ITEMDESC);
		body += '<tr>';
		body += '<td hidden><input type="checkbox" class="itemChk" checked>';
		body += '<input type="text" name="lnno" value="'+data.POLNNO+'" hidden>';
		body += '<input type="hidden" name="convcost" value="'+data.CONVCOST+'">';
		body += '<input type="hidden" name="basecost" value="'+data.BASECOST+'">';
		body += '<input name="unitcost_aod" type="text" value="'+data.CONVERTEDUNITCOST_AOD+'" hidden>';
		body += '<input name="amount_aod" type="text" value="'+data.CONVERTEDAMOUNT_AOD+'" hidden>';
		body += '<input type="hidden" name="unitpricerd" value="">';
		
			body += '<input type="hidden" name="minstkqty" value="'+data.minstkqty+'">';
			body += '<input type="hidden" name="maxstkqty" value="'+data.maxstkqty+'">';
			body += '<input type="hidden" name="stockonhand" value="'+data.stockonhand+'">';
			body += '<input type="hidden" name="incommingqty" value="'+data.incommingqty+'">';
			body += '<input type="hidden" name="ITEMDESH" value="'+ITEMDESC+'">';
			body += '<input type="hidden" name="outgoingqty" value="'+data.outgoingqty+'">';
			body += '<input type="hidden" name="customerdiscount" value="'+data.customerdiscount+'">';
			body += '<input type="hidden" name="unitpricediscount" value="">';
			body += '<input type="hidden" name="discounttype" value="'+data.discounttype+'">';
			body += '<input type="hidden" name="minsp" value="">';
			body += '<input type="hidden" name="uom" value="'+data.UOM+'">';
			
		body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.TAX_TYPE+'">';
		body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+data.TAX_TYPE+'">';
		body += '</td>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" readonly="readonly">';
		body += '<input class="form-control"  name="ITEMDES" value="'+ITEMDESC+'" readonly style="height: 23px;background-color: #fff;" readonly>';		
		body += '</td>';
		body += '<td class="bill-acc">';
		body += '<input type="text" name="account_name" class="form-control" value="'+data.ACCOUNT+'" readonly="readonly">';
		body += '</td>';
		//body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+data.QTYOR+'" readonly="readonly" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" value="'+data.QTYOR+'" class="form-control text-right" data-rl="'+data.minstkqty+'" data-msq="'+data.maxstkqty+'" data-soh="'+data.stockonhand+'" data-eq="'+data.EstQty+'" data-aq="'+data.AvlbQty+'" readonly="readonly"></td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITCOST+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		
		
		
		/*body += '<input name="item_discount" type="text" class="form-control text-right" value="'+zeroval+'" onchange="calculateAmount(this)">';
		body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
		body += "<option value="+data.CURRENCYID+">"+data.CURRENCY_CODE+"</option>";
		body += '<option>%</option>';										
		body += '</select>';*/
		
		if(data.ITEM_DISCOUNT_TYPE == "%"){
			body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)" readonly>';
			}else{
				body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(discamount).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)" readonly>';	
			}
			body += '<select name="item_discounttype" class="discountPicker form-control item_discount_type" onchange="calculateAmount(this)">';
			if(data.ITEM_DISCOUNT_TYPE == curency){
				body += "<option selected value="+data.CURRENCY_CODE+">"+data.CURRENCY_CODE+"</option>";
			}else{
				body += "<option value="+data.CURRENCY_CODE+">"+data.CURRENCY_CODE+"</option>";
			}
			
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<option selected >%</option>';	
			}else{
				body += '<option>%</option>';	
			}
		
		body += '</div>';
		body += '</div>'; 
		body += '</div>';
		body += '</td>';
//		body += '<td class="item-tax">';
//		body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';
//		body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly="readonly">';
//		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+tamount+'" readonly="readonly" style="display:inline-block;">';
		
		body += '<input name="landedCost" type="text" value="0.0" hidden>';
		body += '</td>';
		body += '</tr>';

		var percentage = data.INBOUND_GST;
		$('input[name ="taxpercentage"]').val(percentage);

		var amount = parseFloat(data.AMOUNT);
		
		if($("input[name='bill']").attr("readonly") != "readonly"){
			$("input[name ='shippingcost']").val(data.ACTUALSHIPPINGCOST);	
			$("input[name ='oShippingcost']").val(data.ACTUALSHIPPINGCOST);/* To store Original Shipping cost for validation*/	
		}
		
		$("input[name='Purchasetax']").val(data.TAX_TYPE);
		$("input[name ='orderdiscount']").val(data.ORDERDISCOUNT);
		$("input[name ='aorderdiscount']").val(data.ORDERDISCOUNT);
		$("input[name ='PO_STATE_PREFIX']").val(data.STATE_PREFIX);
		$("input[name ='CURRENCYID']").val(data.CURRENCY_CODE);
		$("input[name ='CURRENCY']").val(data.CURRENCYID);
		$("input[name ='bill_date']").val(data.GOOD_DATE);
		var CURRENCYUSEQT = parseFloat(data.CURRENCYUSEQT).toFixed(numberOfDecimal);
		$("input[name ='CURRENCYUSEQT']").val(CURRENCYUSEQT);
		$("input[name ='CURRENCYUSEQTOLD']").val(CURRENCYUSEQT);
		$("input[name ='GST']").val(data.INBOUND_GST);
		$("input[name ='ptaxpercentage']").val(data.INBOUND_GST);
		$("select[name ='item_rates']").val(data.ITEMRATES);
		$("input[name='ptaxdisplay']").val(data.TAX_TYPE);
		$("input[name=ptaxtype]").val(data.ptaxtype);
		$("input[name=ptaxiszero]").val(data.ptaxiszero);
		$("input[name=ptaxisshow]").val(data.ptaxisshow);
		$("input[name=taxid]").val(data.TAXID);
		var basecurrency = document.form.BASECURRENCYID.value; //Resvi
		document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+data.CURRENCY_CODE+")";
		
		document.getElementById('lbltotal').innerHTML = "Total ("+data.CURRENCY_CODE+")"; //  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency
		if(basecurrency!=data.CURRENCY_CODE)
			{
			document.getElementById('showtotalcur').style.display = 'block';
			document.getElementById('addexpensemodel').style.marginTop = '-45px';
			}
		else {
			document.getElementById('showtotalcur').style.display = 'none';
			document.getElementById('addexpensemodel').style.marginTop = '0px';
			}
		
		$("input[name=SHIPPINGID]").val(data.SHIPPINGID);
		//if(data.SHIPPINGID!="")
		//{
			$('#SHIPPINGCUSTOMER').attr('disabled',true);
			$('#SHIPPINGCUSTOMER').css("background-color", "#eeeeee");
		//}
		//if(data.TRANSPORTID!="")
		//{
			$('#transport').attr('disabled',true);
			$('#transport').css("background-color", "#eeeeee");
		//}
		$("input[name=SHIPPINGCUSTOMER]").val(data.SHIPPINGCUSTOMER);
		$("input[name ='transport']").val(data.TRANSPORTNAME);
		$("input[name ='TRANSPORTID']").val(data.TRANSPORTID);
		$("input[name ='EMP_NAME']").val(data.EMP_NAME);
		$("input[name ='EMPNO']").val(data.EMPNO);
		$("input[name=PROJECTID]").val(data.PROJECTID);
		$("input[name=project]").val(data.PROJECTNAME);
		$("input[name ='payment_terms']").typeahead('val', data.PAYMENT_TERMS);
		$("input[name=due_date]").val(data.DELDATE);
		
		$('#project').attr('disabled',true);
		$('#project').css("background-color", "#eeeeee");
		
		//$('#payment_terms').attr('disabled',true);
		//$('#payment_terms').css("background-color", "#eeeeee");
		
		$('#discount_type').empty();
		$('#discount_type').append('<option value="' + data.CURRENCY_CODE + '">' + data.CURRENCY_CODE + '</option>');
		$('#discount_type').append('<option value="%">%</option>');
		
		if(data.ORDERDISCOUNT_TYPE == "%"){
			$('#oddiscount_type').empty();
			$('#oddiscount_type').append('<option value="%">%</option>');
			$("#oddiscount").attr("readonly", true);
		}else{
			$('#oddiscount_type').empty();
			$('#oddiscount_type').append('<option value="' + data.CURRENCY_CODE + '">' + data.CURRENCY_CODE + '</option>');
			$("#oddiscount").attr("readonly", false);
		}
		
		if(data.ISDISCOUNTTAX == "1"){
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
		
		if(data.ISSHIPPINGTAX == "1"){
			 $('input[name ="shiptaxstatus"]').val("1");
			 $("#shtax").html("");
			 $("#shtax").html("(Tax Inclusive)");
			 $('input[name ="isshiptax"]').prop('checked', true);
		 }else{
			 $('input[name ="shiptaxstatus"]').val("0");
			 $("#shtax").html("");
			 $("#shtax").html("(Tax Exclusive)");
			 $('input[name ="isshiptax"]').prop('checked', false);
		 }
		
		$('input[name ="isodisctax"]').attr("disabled", true);
		$('input[name ="isshiptax"]').attr("disabled", true);
	});
	$('#SALES_LOC').prop('disabled',true);
	$(".bill-table tbody").html(body);
	//renderTaxDetails();
	calculateTotal();
	removeSuggestionToTable();
	
	//IMTI modified on 14-03-2022 to display qty and cost hover
	tooltipedit();
	
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

//IMTI modified on 14-03-2022 to display qty and cost hover
function tooltipedit(){
	$(".bill-table tbody tr").each(function() {
			var obj = $('td:eq(1)', this);
			loadQtyToolTipedit(obj);
			loadUnitPriceToolTipedit(obj);
			loadItemDescToolTipedit(obj);
			if($("input[name=DEDUCT_INV]").val() == "0")
					{ 
					$(obj).closest('tr').find('input[name="qty"]').focus();
					$(obj).closest('tr').find('input[name="qty"]').select();
					}else{
					$(obj).closest('tr').find('input[name="loc"]').focus();
					$(obj).closest('tr').find('input[name="loc"]').select();
					}
	});
}


function loadQtyToolTipedit(obj){
	//$(obj).closest('tr').find("input[name=qty]").tooltip("destroy");
	var content = "<p class='text-left'>Min Stock Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("rl")+"</p>";
	content += "<p class='text-left'>Max Stock Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("msq")+"</p>";
	content += "<p class='text-left'>Stock on Hand: "+$(obj).closest('tr').find("input[name=qty]").data("soh")+"</p>";
	content += "<p class='text-left'>Incomming Quantity: "+$(obj).closest('tr').find("input[name=incommingqty]").val()+"</p>";
	content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(obj).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
	content += "<p class='text-left'>Sales Estimate Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("eq")+"</p>";
	content += "<p class='text-left'>Available Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("aq")+"</p>";
	$(obj).closest('tr').find("input[name=qty]").tooltip({title: content, html: true, placement: "top"});
}

function loadItemDescToolTipedit(obj){
	//$(obj).closest('tr').find("input[name=ITEMDES]").tooltip("destroy");
	var content = "<p class='text-left'> "+$(obj).closest('tr').find("input[name=ITEMDES]").val()+"</p>";
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip({title: content, html: true, placement: "top"});
}

function loadUnitPriceToolTipedit(obj){
//$(obj).closest('tr').find("input[name=cost]").tooltip("destroy");

	var content = "";
	var customerdiscount = parseFloat($(obj).closest('tr').find("input[name=customerdiscount]").val());
	var discounttype = $(obj).closest('tr').find("input[name=discounttype]").val();
	if(isNaN(customerdiscount)){
		content += "<p class='text-left'>Supplier Discount (By Cost or Percentage): 0.000</p>";
	}else{
		if(discounttype == "BYPERCENTAGE"){
			content += "<p class='text-left'>Supplier Discount (By Cost or Percentage): "+customerdiscount+"%</p>";
		}else{
			content += "<p class='text-left'>Supplier Discount (By Cost or Percentage): "+customerdiscount+"</p>";
		}

	}

	$(obj).closest('tr').find("input[name=cost]").tooltip({title: content, html: true, placement: "top"});
}
//END

function getExpenseDetailForBill(shipmentcode,pono){
	var plant = document.form.plant.value;
		$.ajax({
			type : "POST",
			url : "/track/MasterServlet",
			async : true,
			data : {
				PLANT : plant,
				action : "GET_EXPENSE_DETAIL_FOR_BILL",
				SHIPMENT_CODE : shipmentcode,
				ORDERNO : document.form.pono.value
			},
			dataType : "json",
			success : function(data) {
				if(data.expenses[0].SCODESTATUS == "NO"){
				    $("input[name ='cmdexp']").val("Edit");
			    	$("input[name ='TranIdexp']").val(data.expenses[0].ID);
			    	if(data.expenses!="")
					loadExpenseDetails(data.expenses, data.expenseTax);
				}else{
					$(".expense-details").empty();
					alert("Shipping Reference "+shipmentcode+" processed already");
					$("#shipRef").typeahead('val', '');
					$('input[name = "shipmentexp"]').val("");
				    $("input[name ='cmdexp']").val("");
			    	$("input[name ='TranIdexp']").val("");
					
				}
			}
		});
	
	
}


function loadExpenseDetails(expenses, taxDetails){
	console.log("------------------");
	console.log(expenses);
	console.log(taxDetails);
	console.log("------------------");
	$(".expense-details").html('');
	var result = "";
	var totalAmount = '0';
	var totalAmountWoTax = '0';
	var tamount ='';
	var currencytobase='0';
	var basetoordercurrency='0';
	var numberOfDecimal = $("#numberOfDecimal").val();
	var CURRENCYUSEQT = $("#CURRENCYUSEQT").val();
	
	result += '<h4>Additional Expenses</h4>';
	result += '<div class="row">';
	result += '<div class="total-section col-sm-12">';	
	$.each(expenses, function( key, data ) {	
		
		var amount = (data.AMOUNT/data.CURRENCYTOBASE)*data.BASETOORDERCURRENCY;
		
		//if(data.BASETOORDERCURRENCY==data.CURRENCYTOBASE)
			//amount = (data.AMOUNT*data.CURRENCYTOBASE);
		amount = (data.AMOUNT*CURRENCYUSEQT);
		
		result += '<div class="total-row">';
		result += '<div class="total-label">';
		result += data.EXPENSESACCOUNT;
		result += '</div> ';
		result += '<div class="total-amount" id="subTotal">'+parseFloat(amount).toFixed(numberOfDecimal)+'</div>';
		result += '</div>';
	
		currencytobase = data.CURRENCYTOBASE;
		basetoordercurrency = data.BASETOORDERCURRENCY;
		/*totalAmount = data.TOTALAMOUNT;*/
		totalAmount = parseFloat(totalAmount)+parseFloat(amount);
		totalAmountWoTax = parseFloat(totalAmount);
	});
	result += '<div class="taxDetailsexpense">';
	

	var exptaxdetails =[];
	
	$.each(taxDetails, function( key, data ) {
		
		if(exptaxdetails.length == 0){
			exptaxdetails.push({
				TAXTYPE:data.TAXTYPE,
				TAXTOTAL:data.TAXTOTAL,
			});
		}else{
			var ckey ="0";
			$.each(exptaxdetails, function( key1, data1 ) {
				if(data.TAXTYPE == data1.TAXTYPE){
					data1.TAXTOTAL = parseFloat(data1.TAXTOTAL) + parseFloat(data.TAXTOTAL);
					ckey ="1";
				}
			});
			
			if(ckey == "0"){
				exptaxdetails.push({
					TAXTYPE:data.TAXTYPE,
					TAXTOTAL:data.TAXTOTAL,
				});
			}
		}
		
	});
	
	var mydddddata=JSON.stringify(exptaxdetails);
	var CURRENCYUSEQT = $("#CURRENCYUSEQT").val();
	$.each(exptaxdetails, function( key, data ) {
		if(!data.TAXTYPE==""){
		var txt = data.TAXTYPE;

		var tax = txt.substring( txt.lastIndexOf("[") + 1, txt.lastIndexOf("%"));
		
		var taxconv = (data.TAXTOTAL/currencytobase)*basetoordercurrency;
		
		//if(currencytobase==basetoordercurrency)
			//taxconv = (data.TAXTOTAL*currencytobase);
		taxconv = (data.TAXTOTAL*CURRENCYUSEQT);
		
		var taxTotal = parseFloat(parseFloat((taxconv * (tax/100)))).toFixed(numberOfDecimal)
		
		var originalTaxType= data.TAXTYPE;
		var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('[')); 
		if(taxTotal > 0 || taxtype=="ZERO RATE"){
		result += '<div class="total-row">';
		result += '<div class="total-label">'+data.TAXTYPE+'</div> ';
		result += '<div class="total-amount" id="subTotal">'+taxTotal+'</div>';
		result += '</div>';
		
		totalAmount = parseFloat(totalAmount)+parseFloat(taxTotal);
		}
		}
	});
	/*totalAmount = (totalAmount/currencytobase)*basetoordercurrency;*/
	totalAmount = parseFloat(totalAmount).toFixed(numberOfDecimal);
	
	
	result += '</div>';
	result += '<div class="total-section total-row">';
	result += '<div class="gross-total">';
	result += '<div class="total-label"> Total </div> ';
	result += '<div class="total-amount" id="total">'+totalAmount+'</div>';
	result += '</div>';
	result += '</div>';
	result += '</div>';
	result += '</div>';
	
	$(".expense-details").html(result);
	$('input[name ="totalAdditionalExpenses"]').val(totalAmountWoTax);
	
	/*$(".addexphide").empty(); 
	$(".addexphide").append("<a href='#'>Edit Additional Expenses</a>");

	$(".popaddexp").empty();
	$(".popaddexp").append("Edit Expenses");*/
	
}

function calculatePartialTax(obj, types, percentage, display){
	//$(obj).closest('td').find('input[name = "tax_type"]').val(types);
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
		//$(obj).closest('td').data('tax','');
		var amount = $(obj).closest('tr').find('input[name = "amount"]').val();
		discountValue = parseFloat(amount*(prevPercentage/100)).toFixed(numberOfDecimal);
		
		$.each(taxList, function( key, data ) {
			if(data.types == name){
				data.value = parseFloat(parseFloat(data.value)-parseFloat(discountValue)).toFixed(numberOfDecimal);
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

function calculateLandedCost(){
	var taxTotal = parseFloat($('input[name = "taxTotal"]').val());
	var discountvalue = parseFloat($("#discount").html());
	var shippingCharge = parseFloat($('input[name = "shippingcost"]').val());
	var adjustment = parseFloat($('#adjustment').html());
	var additionalExpenses = parseFloat($('input[name ="totalAdditionalExpenses"]').val());
	
	var subTotal = parseFloat($("#subTotal").html());
	
	var additionalCost = parseFloat(shippingCharge + additionalExpenses);
	
	var actualPropportonate = (additionalCost / subTotal);
	
	var amount =  0, landedCost = 0;
	$(".bill-table tbody tr td:last-child").each(function() {
		/*amount =  parseFloat($(this).find('input').val());*/
		amount =  $(this).parent().find('input[name = "cost"]').val()
	    /*landedCost = (amount) + (amount * actualPropportonate);*/
	    landedCost = (amount * actualPropportonate);
	    $(this).find('input[name ="landedCost"]').val(landedCost);
	});
}
function downloadFile(id,fileName)
{
	 var urlStrAttach = "/track/BillingServlet?ACTION=downloadAttachmentById&attachid="+id;
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
	var urlStrAttach = "/track/BillingServlet?ACTION=removeAttachmentById&removeid="+id;	
	$.ajax( {
		type : "POST",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
}
function getNextBill(){
	$.ajax( {
		type : "GET",
		url : "/track/MasterServlet?ACTION=GET_BILLNO_SEQUENCE",
		async : true,
		dataType : "json",
		contentType: false,
        processData: false,
        success : function(data) {
			if (data.ERROR_CODE == "100") {
				document.form.bill.value = data.CUSTCODE;
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

function checkshipref(shipmentcode,pono){
	var plant = document.form.plant.value;
	var check = "false";
	$.ajax({
		type : "POST",
		url : "/track/MasterServlet",
		async : true,
		data : {
			PLANT : plant,
			action : "GET_EXPENSE_DETAIL_FOR_BILL",
			SHIPMENT_CODE : shipmentcode,
			ORDERNO : pono
		},
		dataType : "json",
		success : function(data) {
			
			if(data.expenses[0].STATUS == "NON-BILLABLE"){
				
				check ="true";
			}else{
				
				check ="false";
			}
			
		}
	});

	return check;
}
//Author: Azees  Create date: July 14,2021  Description: Show Supplier Based Currency
function getvendname(TAXTREATMENT,TRANSPORTNAME,TRANSPORTID,PAYMENT_TERMS,PAY_TERMS,VENDO,CURRENCYID,CURRENCY,CURRENCYUSEQT){
	getCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT);
//document.getElementById('nTAXTREATMENT').innerHTML = TAXTREATMENT;
$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
$("input[name=transport]").val(TRANSPORTNAME);
$("input[name=TRANSPORTID]").val(TRANSPORTID);
$("input[name=payment_type]").val(PAY_TERMS);
$("input[name=payment_terms]").val(PAYMENT_TERMS);
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
	//alert(TAXTREATMENT);
	document.getElementById("REVERSECHARGE").checked = false;
	document.getElementById("GOODSIMPORT").checked = false;
	if(TAXTREATMENT =="GCC VAT Registered"||TAXTREATMENT=="GCC NON VAT Registered"||TAXTREATMENT=="NON GCC")
	{
		document.getElementById('CHK1').style.display = 'block';
	}
	else
		document.getElementById('CHK1').style.display = 'none';
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

function changetax(){
	
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
			var taxing = $('td:eq(7)', this).find('input[name="tax_type"]').val();
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
			
				$('td:eq(7)', this).find('input[name="tax_type"]').val(pertax);
				$('td:eq(7)', this).find('input[name="tax"]').val(pertax)
				if(SGSTTYPES != "0"){
					var obj1 = $('td:eq(7)', this);
					calculateTaxState(obj1,SGSTTYPES,SGSTPERCENTAGE,DISPLAY);
				}
		});
		renderTaxDetails();
		
	}
	});
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

function changetaxfordiscount(){
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
			var taxtype = $('td:eq(7)', this).find('input[name="tax_type"]').val();
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
					var obj1 = $('td:eq(7)', this);
					calculateTaxState(obj1,SGSTTYPES,SGSTPERCENTAGE,DISPLAY);
				}
		});
		renderTaxDetails();
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


function setCURRENCYUSEQT(CURRENCYUSEQTCost){
	var plant = document.form.plant.value;	
var numberOfDecimal = $("#numberOfDecimal").val();
var isgrn = parseFloat($("input[name ='isgrn']").val());
var oldeqtcost = $('input[name ="CURRENCYUSEQTOLD"]').val();
var CURRENCYID = $('input[name ="CURRENCYID"]').val();
var CURRENCY = $('input[name ="CURRENCY"]').val();
var ISINVENTORY = $('input[name ="BillType"]').val();
var cost = 0;
var ischange=0;

if(isNaN(isgrn))
{

	$('tr').each(function () {
		
		/*if(ISINVENTORY == "INVENTORY"){*/
			
			var ITEM_CURRENCYID = $(this).find("td:nth-child(9)").find('select[name ="item_discounttype"]').val();
			
			if(ITEM_CURRENCYID!=undefined)
				{
			if(ITEM_CURRENCYID!="%")
				{
				if(CURRENCYID!=ITEM_CURRENCYID)
					{
					
					$(this).find('select[name ="item_discounttype"]').empty();
					$(this).find('select[name ="item_discounttype"]').append('<option value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
					$(this).find('select[name ="item_discounttype"]').append('<option value="%">%</option>');
					ischange=1;
					}
				}
				}
			var qty = parseFloat($(this).find("td:nth-child(7)").find('input').val()).toFixed(3);
//			cost = parseFloat($(this).find("td:nth-child(1)").find('input[name=basecost]').val()) *parseFloat(CURRENCYUSEQTCost);
			cost = ((parseFloat($(this).find('input[name=cost]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
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
				itemDiscountval = parseFloat((parseFloat(itemDiscountval)/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost)).toFixed(numberOfDecimal);
				//itemDiscountval = parseFloat(parseFloat(itemDiscountval)/parseFloat(CURRENCYUSEQTCost)).toFixed(numberOfDecimal);
				itemDiscount=itemDiscountval;
				}
			var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
			
			if(parseFloat(amount) >= parseFloat("0")){
				$(this).find("td:nth-child(9)").find('input').val(itemDiscount);
				$(this).find("td:nth-child(10)").find('input').val(amount);
			}else{
				//alert("discout should be less than tha amount");
				if(discounType == "%"){
					$(this).find("td:nth-child(9)").find('input').val(parseFloat("0").toFixed(3));
				}else{
					$(this).find("td:nth-child(9)").find('input').val(parseFloat("0").toFixed(numberOfDecimal));
				}
				var originalamount = parseFloat((qty*cost)).toFixed(numberOfDecimal);
				$(this).find("td:nth-child(10)").find('input').val(originalamount);
			}
			
			
		/*}else{
		
		
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
		}*/

		
	});
	
	
}
else
	{
	
	if(isgrn=="1")
	{
	
	$('tr').each(function () {
		
		amount = parseFloat($(this).find("td:nth-child(1)").find('input[name=basecost]').val()) *parseFloat(CURRENCYUSEQTCost);
		$(this).find("td:nth-child(1)").find('input[name=convcost]').val(amount);
		amount = parseFloat(amount).toFixed(numberOfDecimal);
		$(this).find("td:nth-child(6)").find('input').val(amount);
		
		var qtyamount = parseFloat($(this).find("td:nth-child(1)").find('input[name=basecost]').val()) *parseFloat(CURRENCYUSEQTCost) ;
		amount = parseFloat($(this).find("td:nth-child(5)").find('input').val()) *parseFloat(qtyamount);
		amount = parseFloat(amount).toFixed(numberOfDecimal);
		$(this).find("td:nth-child(9)").find('input').val(amount);
		
	});
	
	}

	}

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

function getCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT){
	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	$("#CURRENCY").typeahead('val', CURRENCY);
	
	$('#discount_type').empty();
	$('#discount_type').append('<option value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
	$('#discount_type').append('<option value="%">%</option>');
	
	setCURRENCYUSEQT(CURRENCYUSEQT);
	var basecurrency = document.form.BASECURRENCYID.value; //Resvi
	document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi
	
	document.getElementById('lbltotal').innerHTML = "Total ("+CURRENCYID+")"; //  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency
	if(basecurrency!=CURRENCYID)
		document.getElementById('showtotalcur').style.display = 'block';
	else
		document.getElementById('showtotalcur').style.display = 'none';
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
	$("#Purchasetax").typeahead('val', '');
	calculateTotal();
}


function calculateAmount(obj){

	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var state = $("input[name=STATE_PREFIX]").val();
	
	var qty = parseFloat($(obj).closest('tr').find('input[name=qty]').val()).toFixed(3);
	
	var avlqty = parseFloat($(obj).closest('tr').find("input[name=qty]").data("aq")).toFixed(3);
	var maxqty = parseFloat($(obj).closest('tr').find("input[name=qty]").data("msq")).toFixed(3);
	
	if($("input[name=DEDUCT_INV]").val() == "1"){
	var isproductmaxqty = $("input[name=ISPRODUCTMAXQTY]").val();
	if(isproductmaxqty == 1) {
		if(maxqty>0) {
		if(maxqty<(parseFloat(qty)+parseFloat(avlqty))){
		var chk = confirm("You order more than Max Stock Quantity. Do you want continue to add the product?");
			if (!chk) {
				$(obj).closest('tr').find('input[name=qty]').val(parseFloat("0").toFixed(3));
				qty =parseFloat("0").toFixed(3);
				}
			}
		}
	}
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
			if(ptaxiszero == "0" && ptaxisshow == "1"){
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
		//$('input[name ="taxamount"]').val(taxTotal);
		$('input[name = "taxTotal"]').val(taxTotal)
	}else{
		$(".taxDetails").html("");
		//$('input[name ="taxamount"]').val("0");
		$('input[name = "taxTotal"]').val("0");
	}
	 
	 
	 var pretotal = parseFloat(parseFloat(amount)- parseFloat(orderdisc) - parseFloat(discountValue) - parseFloat(orddiscountValue) + parseFloat(taxTotal)
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
	 
	 
	 var CURRENCYUSEQT = $('input[name ="CURRENCYUSEQT"]').val();//Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency
	 var convttotalvalue= (totalvalue/CURRENCYUSEQT).toFixed(numberOfDecimal)
	 $("#totalcur").html(convttotalvalue);
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

function changeitem(obj){
	var obj2 = $(obj).parent().find('input[name="item"]');
	$(obj2).typeahead('val', '');
	$(obj).parent().find('input[name="item"]').focus();
}

function onNewGRN()
{
	
	var urlStr = "/track/InboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : document.form.plant.value,
		ACTION : "GRN"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.grno;
				document.form.grno.value= resultV;
	
			} else {
				alert("Unable to genarate GRN NO");
				document.form.grno.value = "";
			}
		}
	});	
	}

function generateBatch(obj){
	
	var urlStr = "/track/InboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : document.form.plant.value,
		ACTION : "GENERATE_BATCH"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				$(obj).closest('tr').find('input[name=batch]').val(resultVal.batchCode);
			} else {
				alert("Unable to genarate Batch");
				$(obj).closest('tr').find('input[name=batch]').val("NOBATCH");
			}
		}
	});
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
	formData.append("BILLNO", $("#bill").val());
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
