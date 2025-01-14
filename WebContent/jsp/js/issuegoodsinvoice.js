var subWin = null;
var itemList="";
var taxList = [];
var zerotype=0;
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

$(document).ready(function(){
	var plant = document.form1.plant.value;
	var cmd = document.form1.cmd.value;
	var TranId = document.form1.TranId.value;
	var displayCustomerpop = document.form1.displayCustomerpop.value;
	var education = document.form1.EDUCATION.value;
	var itemchk=0;
	var replacePreviousInvoiceCost = $("input[name=replacePreviousInvoiceCost]").val();
	//$("#SALES_LOC").val("Abu Dhabi");
	//document.form1.STATE_PREFIX.value="AUH";
	
	if(cmd=="Edit")
	{
	if(TranId!="")
		{
			getEditInvoiceDetail(TranId);
		}
	}else if(cmd=="copy"){
		if(TranId!="")
		{
			getCopyInvoiceDetail(TranId);
		}
	}else{
	
		var gino = document.form1.gino.value;
		if($("#gino").val() !== ""){
			getOrderDetailsForInvoice(gino);
			$("input[name ='CUSTOMER']").typeahead('val', $("#ORDERNO").val());
			$(".add-line").hide();
			$("#item_rates").attr("readonly", true);
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
						ACTION : "GET_CUSTOMER_DATA_ACTIVE",
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
		    	return '<div onclick="getcustname(\''+data.TAXTREATMENT+'\',\''+data.TRANSPORTNAME+'\',\''+data.TRANSPORTID+'\',\''+data.CUSTNO+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\',\''+data.DOB+'\',\''+data.NATIONALITY+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();
			if(displayCustomerpop == 'true'){
				$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal" onclick="document.form1.custModal.value=\'cust\'"><a href="#"> + New Cutomer</a></div>');
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
			
			/* To reset Order number Autosuggestion*/
			$("#ORDERNO").typeahead('val', '"');
			$("#ORDERNO").typeahead('val', '');
			$("input[name=transports]").val("");
			$('#nTAXTREATMENT').attr('disabled',false);
			
			$('#shipbilladd').empty();
				
				$("input[name=SHIPCONTACTNAME]").val("");
				$("input[name=SHIPDESGINATION]").val("");
				$("input[name=SHIPADDR1]").val("");
				$("input[name=SHIPADDR2]").val("");
				$("input[name=SHIPADDR3]").val("");
				$("input[name=SHIPADDR4]").val("");
				$("input[name=SHIPCOUNTRY]").val("");
				$("input[name=SHIPSTATE]").val("");
				$("input[name=SHIPZIP]").val("");
				$("input[name=SHIPWORKPHONE]").val("");
				$("input[name=payment_terms]").val("");
				$("input[name=SHIPPINGID]").val("");
				$("input[name=SHIPPINGCUSTOMER]").val("");
			}
		}).on('typeahead:select',function(event,selection){
			$("#project").typeahead('val', '"');
			$("#project").typeahead('val', '');
			$("input[name=PROJECTID]").val('');
			
			if(replacePreviousInvoiceCost=="1") {
			if(itemchk==1||itemchk==0)
				{
				removeSuggestionToTable();
				addSuggestionToTable();
				itemchk=1;
				}
			}
			
			if($(this).val() == ""){
				$('#shipbilladd').empty();
				$('#shipbilladd').append('');
			}else{
				$('#shipbilladd').empty();
				var addr = '<div class="col-sm-2"></div>';
				addr += '<div class="col-sm-5" style="line-height: 7px;font-size: 13px;"><h5 style="font-weight: bold;">Billing Address</h5>';
				addr += '<p>'+selection.CNAME+'</p>';
				addr += '<p>'+selection.ADDR1+' '+selection.ADDR2+'</p>';
				addr += '<p>'+selection.ADDR3+' '+selection.ADDR4+'</p>';
				addr += '<p>'+selection.STATE+'</p>';
				addr += '<p>'+selection.COUNTRY+' '+selection.ZIP+'</p>';
				addr += '<p>'+selection.HPNO+'</p>';
				addr += '<p>'+selection.EMAIL+'</p>';
				//		IMTHIYAS Modified on 02.03.2022
				if(education == 'Education'){
					if(selection.DOB == '' && selection.NATIONALITY == ''){
						addr += '<p>'+selection.DOB+'</p>';
						addr += '<p>'+selection.NATIONALITY+'</p>';
					}else if(selection.DOB == ''){
						addr += '<p>'+selection.DOB+'</p>';
						addr += '<p>'+"NATIONALITY : "+selection.NATIONALITY+'</p>';
					}else if(selection.NATIONALITY == ''){
					addr += '<p>'+"DOB : "+selection.DOB+'</p>';
						addr += '<p>'+selection.NATIONALITY+'</p>';
					}else{
						addr += '<p>'+"DOB : "+selection.DOB+'</p>';
						addr += '<p>'+"NATIONALITY : "+selection.NATIONALITY+'</p>';
					}
				}
				
				addr += '</div>';
				if(education != 'Education'){
				addr += '<div class="col-sm-5" style="line-height: 7px;font-size: 13px;"><h5 style="font-weight: bold;">Shipping Address   <a><span data-toggle="modal" data-target="#shipaddr" onclick="getshipaddr();" style="font-size: 15px;font-weight: 450;">Change</span></a></h5>';
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
				}
				addr += '</div>';
				addr += '</div>';
				$('#shipbilladd').append(addr);
				
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
				
				$("input[name=TRANSPORTID]").val(selection.TRANSPORTID);
				$("#transport").typeahead('val', selection.TRANSPORT_MODE);
				
				$("#PAYMENTTYPE").typeahead('val', selection.PAY_TERMS);
				$("input[name=payment_terms]").val(selection.PAYMENT_TERMS);
				$("input[name=SHIPPINGID]").val(selection.CUSTNO);
				$("input[name=SHIPPINGCUSTOMER]").val(selection.CNAME);
				$("input[name=DOBYEAR]").val(selection.DOB);
				$("input[name=NATIONAL]").val(selection.NATIONALITY);
				if(education == 'Education'){
				getDobNationality(DOB,NATIONALITY)
				}
			}
			
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
		    	//return '<div onclick="document.form1.SHIPPINGID.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
		    	return '<div onclick="document.form1.SHIPPINGID.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Mobile: ' + data.HPNO + '</p><br/><p class="item-suggestion">Address: ' + data.ADDR1 + '  '+ data.ADDR2 +'</p><p class="item-suggestion pull-right">Customer TelNo: ' + data.TELNO + '</p></br><p class="item-suggestion"> ' + data.ADDR3 + '  '+ data.ADDR4 +'</p><p class="item-suggestion pull-right">Email: ' + data.EMAIL + '</p></br><p class="item-suggestion"> ' + data.COUNTRY + '  '+ data.ZIP +'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			
			if(displayCustomerpop == 'true'){
				menuElement.after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#" onclick="document.form1.custModal.value=\'shipcust\'"> + New Shipping Address</a></div>');
			}
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
				document.form1.SHIPPINGID.value = "";
			}
		});

	/* Order Number Auto Suggestion 
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
		});*/
	
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
	/*As Per Auto Invoice for Centralised Kitchen, Order Type Changed to SALES-Azees 03.23*/
		var orderType="INVOICE";	
		if(education == 'Centralised Kitchen'){
		orderType="SALES";
		}	
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
						Type : orderType,
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
			if(cmd!="Edit")
			getOrderDetailsForInvoice(document.form1.invoice.value);
		});*/
	
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
	    
	    var delQty = parseFloat($(obj).closest('tr').find("td:nth-child(1)").find('input[name=ORDQTY]').val());
	    //var delLno = parseFloat($(obj).closest('tr').find("td:nth-child(1)").find('input[name=LNNO]').val());
	    var delLno = parseFloat($(obj).closest('tr').find("td:nth-child(1)").find('input[name=lnno]').val());
	    if(!isNaN(delQty)){
	    	var item = $(obj).closest('tr').find("td:nth-child(2)").find('input[name=item]').val();
	    	var uom = $(obj).closest('tr').find("td:nth-child(4)").find('input[name=uom]').val();
	    	var loc = $(obj).closest('tr').find("td:nth-child(5)").find('input[name=loc]').val();
	    	var batch = $(obj).closest('tr').find("td:nth-child(6)").find('input[name=batch]').val();
	    	
	    	$("form[name=form1]").append("<input type=hidden name=DELLNO value="+delLno+">");
	    	$("form[name=form1]").append("<input type=hidden name=DELVAL value="+delQty+">");
	    	$("form[name=form1]").append("<input type=hidden name=DELITEM value="+item+">");
	    	$("form[name=form1]").append("<input type=hidden name=DELUOM value="+uom+">");
	    	$("form[name=form1]").append("<input type=hidden name=DELLOC value="+loc+">");
	    	$("form[name=form1]").append("<input type=hidden name=DELBATCH value="+batch+">");
	    }
	    
	    calculateTax(obj, "", "", "");
	    $(this).parent().parent().remove();
	       setLineNo();
	    calculateTotal();
	});
	
	$("#btnBillDraft").click(function(){
		$('input[name ="invoice_status"]').val('Draft');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		/*document.form1.DEDUCT_INV.value="0";*/
		$("#createBillForm").submit();
	});
	$("#btnBillOpen").click(function(){
		var deductInv = document.form1.DEDUCT_INV.value;
		var origin = document.form1.ORIGIN.value;
		var status = document.form1.billstatus.value;
		if(status != "Paid"){
			$('input[name ="invoice_status"]').val('Open');
		}else {
			$('input[name ="invoice_status"]').val(status);
		}
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		if(deductInv != "0" && origin != "manual"){
			document.form1.DEDUCT_INV.value="1";
		}		
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
		if ($('#CUSTOMEREMAIL').val() == ''){
			alert('Customer record does not have an email address. Please update customer email and try again or Save as Draft OR Save as Open');
			return;
		}
		var deductInv = document.form1.DEDUCT_INV.value;
		var origin = document.form1.ORIGIN.value;
		$('input[name ="invoice_status"]').val('Open');
		$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());
		if(deductInv != "0" && origin != "manual"){
			document.form1.DEDUCT_INV.value="1";
		}	
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
	
	/*if($("#invoice").val() !== ""){
		getOrderDetailsForInvoice($("#invoice").val());
		$("input[name ='CUSTOMER']").typeahead('val', $("#ORDERNO").val());
		$(".add-line").hide();
		$("#item_rates").attr("readonly", true);
	}*/
	
	
	
	$(document).on("focusout","input[name ='CURRENCYUSEQT']",function(){
		var CURRENCYUSEQTCost = parseFloat($("input[name ='CURRENCYUSEQT']").val());
		if(!Number.isNaN(CURRENCYUSEQTCost))
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});
	
	/*$(document).on("focusout","input[name ='CURRENCYUSEQT']",function(){
		var CURRENCYUSEQTCost = parseFloat($("input[name ='CURRENCYUSEQT']").val());
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});
	*/
	
	
	$("#remarks-table tbody").on('click','.remark-action',function(){
    $(this).parent().parent().remove();
	});
	
	
	$("input[name=qty]").mouseenter(function(){
		var content = "<p class='text-left'>Reorder Level: "+$(this).data("rl")+"</p>";
		content += "<p class='text-left'>Max Stock Quantity: "+$(this).data("msq")+"</p>";
		content += "<p class='text-left'>Stock on Hand: "+$(this).data("soh")+"</p>";
		content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(this).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
		content += "<p class='text-left'>Sales Estimate Quantity: "+$(this).data("eq")+"</p>";
		content += "<p class='text-left'>Available Quantity: "+$(this).data("aq")+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"}); 
	});
	
	$("input[name=cost]").mouseenter(function(){
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
	
	
	if(replacePreviousInvoiceCost=="1") {
	$(".itemSearch").click(function(){
		var customer = $("input[name=CUSTOMER]").val();
		if(customer == ""){
			itemchk=1;
			alert("Please select a Customer.");
			return false;
		}
		itemchk=itemchk+1;
	});
	}
	
	calculateTotal();
	
	
	//addSuggestionToTable();
});



function addRow(event){
	event.preventDefault(); 
    event.stopPropagation();
	var curency = $("input[name=CURRENCYID]").val();
	var taxdisplay = $("input[name=ptaxdisplay]").val();
	var deductInv = document.form1.DEDUCT_INV.value;
	var origin = document.form1.ORIGIN.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var educations = document.form1.EDUCATION.value;
	var body="";
	body += '<tr>';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;"><input type="hidden" name="basecost" value="0.00">';
	body += '<input type="hidden" name="lnno" value="1">';
	body += '<input type="hidden" name="minsp" value="">';
	body += '<input type="hidden" name="customerdiscount" value="">';
	body += '<input type="hidden" name="outgoingqty" value="">';
	body += '<input type="hidden" name="itemprice" value="0.00">';
	body += '<input type="hidden" name="discounttype" value="">';
	body += '<input type="hidden" name="itemcost" value="">';
	body += '<input type="hidden" name="ORDQTY" value="0.0">';
	body += '<input type="hidden" name="IS_COGS_SET" value="N">';
	body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+taxdisplay+'">';
	body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
	if(deductInv == "1" && origin == "manual"){
		
	}
	else {
	body += '<input type="hidden" name="uom" value="">';
	}
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" style="width:87%" class="form-control itemSearch" onchange="checkitems(this.value,this)" placeholder="Type or click to select an Product." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '<input type="hidden" name="edit_item" class="form-control" value="">';
	body += '</td>';	
	body += '<td class="">';
	body += '<input type="text" name="account_name" value="Local sales - retail" class="form-control accountSearch" placeholder="Account">';
	body += '</td>';
	if(deductInv == "1" && origin == "manual"){
		body += '<td class="">';
		body += '<input type="text" name="uom" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="UOM">';
		body += '</td>';
		body += '<td class="">';
		body += '<input type="text" name="loc" class="form-control locSearch" onchange="checkprdloc(this.value,this)" placeholder="Location">';
		body += '</td>';
		body += '<td class="">';
		body += '<input type="text" name="batch" value="NOBATCH" class="form-control batchSearch" placeholder="Batch">';
		body += '</td>';
	}
	body += '<td class="item-qty text-right">';
	body += '<input type="text" data-rl="0.000" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" data-pq="0.000" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
	body += '<input type="hidden" name="edit_qty" class="form-control text-right" value="0.000" ">';
	body += '</td>';
	body += '<td>';
	body += '<input type="text" name="unitcost" class="form-control text-right" value="'+szero+'" readonly tabindex="-1">';
	body += '</td>';
	body += '<td>';
	body += '<input type="text" name="addonshow" class="form-control text-right" value="'+szero+' '+curency+'" readonly tabindex="-1">';
	body += '<input type="hidden" name="addonprice" value="'+szero+'">';
	body += '<input type="hidden" name="addontype" value="'+curency+'">';
	body += '</td>';
	body += "<td class=\"item-cost text-right\"><input type=\"text\" name=\"cost\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\"></td>";
	//		IMTHIYAS Modified on 02.03.2022
	if(educations == 'Education'){
	body += '<td class="item-discount text-right" disabled>';
	body += '<div class="row">';							
	body += '<div class=" col-lg-12 col-sm-3 col-12">';
	body += '<div class="input-group my-group" style="width:120px;">';	
	body += "<input name=\"item_discount\" type=\"text\" class=\"form-control text-right\" disabled value="+szero+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\">";
	//body += '<textarea  rows="1" maxlength="3" style="resize: none;padding: 5px;" class="discountPicker form-control item_discounttypeSearch" name="item_discounttype" onchange="calculateAmount(this)"></textarea>';
	//body += "<input type=\"text\" class=\"discountPicker form-control item_discounttypeSearch\" id=\"item_discounttype\" value="+curency+" name=\"item_discounttype\" onchange=\"calculateAmount(this)\">";
	body += '<select name="item_discounttype" class="discountPicker form-control" disabled onchange="calculateAmount(this)">';
	body += "<option value="%">"%"</option>";
	body += '<option>%</option>';										
	body += '</select>';
	body += '</div>';
	body += '</div>'; 
	body += '</div>';
	body += '</td>';
	}else{
	//body += "<td class=\"item-cost text-right\"><input type=\"text\" name=\"cost\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\"></td>";
	body += '<td class="item-discount text-right">';
	body += '<div class="row">';							
	body += '<div class=" col-lg-12 col-sm-3 col-12">';
	body += '<div class="input-group my-group" style="width:120px;">';	
	body += "<input name=\"item_discount\" type=\"text\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\">";
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
	}
	/*body += '<td class="item-tax">';
	body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
	body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
	body += '</td>';*/
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += "<input name=\"amount\" type=\"text\" class=\"form-control text-right\" value="+szero+" readonly=\"readonly\" style=\"display:inline-block;\" tabindex\=-1\">";
	body += '<td class="table-icon" style="position:relative;">';
	body += '<a href="#" onclick="showRemarksDetails(this)">';
	body += '<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>';
	body += '</a>';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" style="left: 35px;"></span>';
	body += '</td>';
	body += '</td>';
	body += '</tr>';
	$(".bill-table tbody").append(body);
	removeSuggestionToTable();
	addSuggestionToTable();
	   setLineNo();
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
	$(obj).closest('tr').find("input[name ='ITEMDES']").val(desc);
}

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
	var CUSTNO = '';
	var replacePreviousInvoiceCost = $("input[name=replacePreviousInvoiceCost]").val();
	if(replacePreviousInvoiceCost=="1")
		CUSTNO = $("input[name=CUST_CODE]").val();
		else
		CUSTNO = '';
	var origin = document.form1.ORIGIN.value;
	var deductInv = $("input[name=DEDUCT_INV]").val();
	if(deductInv=="1") {
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
				CUSTNO : CUSTNO,
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
			//	return '<div onclick="loadItemData(this,\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.SALESUOM+'\',\''+ data.ITEM+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
				//vicky
				return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
//			menuElement.after( '<div class="itemAddBtn footer"  data-toggle="modal" data-target="#productModal"><a href="#"> + Add my Product</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".bigdrop").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			menuElement.next().show();
		}).on('typeahead:select',function(event,selection){
			loadItemData(this, selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.SALESUOM,selection.ITEM,selection.COST);	
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);	
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					$(this).closest('tr').find('input[name="ITEMDES"]').val("");
				}	
		});	
}else{
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
				CUSTNO : CUSTNO,
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
		//		return '<div onclick="loadItemData(this,\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.SALESUOM+'\',\''+ data.ITEM+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
				//vicky
				return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
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
			loadItemData(this, selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.SALESUOM,selection.ITEM,selection.COST);
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);			
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					$(this).closest('tr').find('input[name="ITEMDES"]').val("");
				}	
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
			debugger;
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
				return '<div"><p class="item-suggestion pull-left">Batch: '+data.BATCH+'</p><p class="item-suggestion">&nbsp;&nbsp; PC/PCS/EA UOM :'+data.PCSUOM+' &nbsp;&nbsp;</p><p class="item-suggestion pull-right">PC/PCS/EA UOM Quantity: '+data.PCSQTY+'</p><br/><p class="item-suggestion pull-left">Inventory UOM: '+data.UOM+'</p><p class="item-suggestion pull-right">Inventory UOM Quantity: '+data.QTY+'</p><br/><p class="item-suggestion pull-left">Received Date: '+data.CRAT+'</p><p class="item-suggestion  pull-right">Expiry Date: '+data.EXPIRYDATE+'</p><p style="clear:both;"></p></div>';
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



/*
function loadItemData(obj, catalogPath, account, cost,salesuom,productId,price){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var deductInv = $("input[name=DEDUCT_INV]").val();
	
	$(obj).closest('tr').find("td:nth-child(1)").find('input[name=basecost]').val(cost);
	$(obj).closest('tr').find("input[name=uom]").val(salesuom);
	$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(cost));
	$(obj).closest('tr').find("input[name=itemcost]").val(parseFloat(price));
	var custcost = parseFloat(cost);
	cost = parseFloat($("input[name ='CURRENCYUSEQT']").val()) * parseFloat(cost);
	cost = parseFloat(cost).toFixed(numberOfDecimal);
	
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("input[name=cost]").val(cost);
	
	//calculateAmount(obj);
	var currencyUSEQT = $("input[name=CURRENCYUSEQT]").val();
	var currencyID = $("input[name=CURRENCYID]").val();
	var custCode = $("input[name=CUST_CODE]").val();
	var ACTION ="GET_PRODUCT_AUTO_DETAILS";
	var dono = $("input[name=ORDERNO]").val();
	var replacePreviousInvoiceCost = $("input[name=replacePreviousInvoiceCost]").val();
	if(replacePreviousInvoiceCost=="1") {
		ACTION ="GET_INVOICE_PRODUCT_AUTO_DETAILS";
		cost =custcost;
		}
	//else
		//custCode ="";
	
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
				UOM:salesuom,
				UNITPRICE : cost,
				REPLACEPREVIOUSSALESCOST: replacePreviousInvoiceCost,
				//ACTION : "GET_PRODUCT_DETAILS"
				ACTION : ACTION
				},
				dataType : "json",
				success : function(data) {
					//outgoingOBDiscount
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
						var itemsPrice = parseFloat(resultVal.ConvertedUnitCost);
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
								$(obj).closest('tr').find("input[name=discounttype]").val("BYPERCENTAGE");
								$(obj).closest('tr').find("input[name=customerdiscount]").val(resultVal.outgoingOBDiscount.match(regex)[0]+'%');
								if(replacePreviousInvoiceCost=="1"){
								discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100);
								price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100));
								resultVal.ConvertedUnitCost = price;
								}else if(replacePreviousInvoiceCost=="2"){
								discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100);
								price = parseFloat(resultVal.ConvertedUnitCost-discount);
								resultVal.ConvertedUnitCost = price;
								}
								else{
								//discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.curOBDiscount)/100);
								//price = parseFloat(resultVal.ConvertedUnitCost-discount);
								discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100);
								price = parseFloat(resultVal.ConvertedUnitCost-discount);
								resultVal.ConvertedUnitCost = price;}
							}
							else
							{
								$(obj).closest('tr').find("input[name=discounttype]").val("");
								$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat(resultVal.outgoingOBDiscount).toFixed(numberOfDecimal));
								if(replacePreviousInvoiceCost=="1")
								price = parseFloat(resultVal.ConvertedUnitCost-(resultVal.outgoingOBDiscount*currencyUSEQT));
								else if(replacePreviousInvoiceCost=="2")
								price = parseFloat(resultVal.ConvertedUnitCost-(resultVal.outgoingOBDiscount*currencyUSEQT));
								else
								//price = parseFloat(resultVal.ConvertedUnitCost-resultVal.curOBDiscount);
								price = parseFloat(resultVal.ConvertedUnitCost-(resultVal.outgoingOBDiscount*currencyUSEQT));
								resultVal.ConvertedUnitCost = price;
							}
						
						}
						
						$(obj).closest('tr').find("input[name=qty]").data('rl',resultVal.stkqty);
						$(obj).closest('tr').find("input[name=qty]").data('msq',resultVal.maxstkqty);
						$(obj).closest('tr').find("input[name=qty]").data('soh',resultVal.stockonhand);
						$(obj).closest('tr').find("input[name=qty]").data('eq',resultVal.EstQty);
						$(obj).closest('tr').find("input[name=qty]").data('aq',resultVal.AvlbQty);
						$(obj).closest('tr').find("input[name=ITEMDES]").data('pd',resultVal.ITEMDESC);
						$(obj).closest('tr').find("input[name=cost]").val(resultVal.ConvertedUnitCost);
						$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(resultVal.ConvertedUnitCosts).toFixed(numberOfDecimal));
						if(replacePreviousInvoiceCost=="1"){
						$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(resultVal.UNITPRICE));
						}
						if(replacePreviousInvoiceCost=="3"){
						var auprice = 0;
						var addshow = "0";
						if(resultVal.incpriceunit == "%"){
							//auprice = parseFloat(resultVal.ConvertedUnitCost) + ((parseFloat(resultVal.ConvertedUnitCost)/100)*resultVal.incprice);
							//addshow = parseFloat(resultVal.incprice).toFixed("3")+" "+resultVal.incpriceunit;
							auprice = parseFloat(price) + ((parseFloat(price)/100)*resultVal.incprice);
							addshow = parseFloat(resultVal.incprice).toFixed("3")+" "+resultVal.incpriceunit;
						}else{
							//auprice = parseFloat(resultVal.ConvertedUnitCost) + parseFloat(resultVal.incprice);
							//addshow = parseFloat(resultVal.ADDONCOST).toFixed(numberOfDecimal)+" "+resultVal.AODTYPE;
							auprice = parseFloat(price) + parseFloat(resultVal.incprice);
							addshow = parseFloat(resultVal.ADDONCOST).toFixed(numberOfDecimal)+" "+resultVal.AODTYPE;
						}
						}
						if(replacePreviousInvoiceCost=="3"){
							$(obj).closest('tr').find("input[name=addonshow]").val(addshow);
							$(obj).closest('tr').find("input[name=addonprice]").val(resultVal.incprice);
							$(obj).closest('tr').find("input[name=addontype]").val(resultVal.incpriceunit);
							$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(resultVal.ConvertedUnitCosts).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(auprice));
							$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(resultVal.itemprice));
						}
						
						if(replacePreviousInvoiceCost=="1"){
						$(obj).closest('tr').find("td:nth-child(1)").find('input[name=basecost]').val(resultVal.UNITPRICE);
						}
					}
					
					
					calculateAmount(obj);
					loadQtyToolTip(obj);
					loadUnitPriceToolTip(obj);
					loadItemDescToolTip(obj);
					if(deductInv=="1") {
					$(obj).closest('tr').find('input[name="loc"]').focus();
					$(obj).closest('tr').find('input[name="loc"]').select();
					} else {
					$(obj).closest('tr').find('input[name="qty"]').focus();
					$(obj).closest('tr').find('input[name="qty"]').select();
					}
				}
			});
}*/




function loadItemData(obj, catalogPath, account, cost,salesuom, productId, nonStkFlag,price){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var deductInv = $("input[name=DEDUCT_INV]").val();
	
	$(obj).closest('tr').find("td:nth-child(1)").find('input[name=basecost]').val(cost);
	$(obj).closest('tr').find("input[name=uom]").val(salesuom);
	$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(cost));
	$(obj).closest('tr').find("input[name=itemcost]").val(parseFloat(price));
	var custcost = parseFloat(cost);
	//alert(custcost);
	cost = parseFloat($("input[name ='CURRENCYUSEQT']").val()) * parseFloat(cost);
	cost = parseFloat(cost).toFixed(numberOfDecimal);
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("input[name=cost]").val(cost);
	$(obj).closest('tr').find("input[name=nonStkFlag]").val(nonStkFlag);	
	//calculateAmount(obj);
	var currencyUSEQT = $("input[name=CURRENCYUSEQT]").val();
	var currencyID = $("input[name=CURRENCYID]").val();
	var custCode = $("input[name=CUST_CODE]").val();
	var dono = $("input[name=ORDERNO]").val();
	var replacePreviousInvoiceCost = $("input[name=replacePreviousInvoiceCost]").val();
	
	/*
	var ACTION ="GET_PRODUCT_AUTO_DETAILS";
	
	
	if(replacePreviousInvoiceCost=="1") {
		ACTION ="GET_INVOICE_PRODUCT_AUTO_DETAILS";
		cost =custcost;
		}
	//else
		//custCode ="";
		
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
				UOM:salesuom,
				UNITPRICE : cost,
				REPLACEPREVIOUSSALESCOST: replacePreviousInvoiceCost,
				//ACTION : "GET_PRODUCT_DETAILS"
				ACTION : ACTION
				},
				dataType : "json",
				success : function(data) {
					//outgoingOBDiscount
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
						var itemsPrice = parseFloat(resultVal.ConvertedUnitCost);
						//alert(resultVal.ConvertedUnitCost);
						//alert(resultVal.outgoingOBDiscount);
						//alert(currencyUSEQT);
						//alert(resultVal.ConvertedUnitCosts);
						//alert(resultVal.OBDiscountType);
						//alert(resultVal.UNITPRICE);
						//alert(resultVal.curOBDiscount);
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
							if(resultVal.OBDiscountType=="BYPERCENTAGE"){
								$(obj).closest('tr').find("input[name=discounttype]").val("BYPERCENTAGE");
								$(obj).closest('tr').find("input[name=customerdiscount]").val(resultVal.outgoingOBDiscount.match(regex)[0]+'%');
								
								if(replacePreviousInvoiceCost=="1"){
									discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100);
									price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100));
									resultVal.ConvertedUnitCost = price;
								}else{
									//discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.curOBDiscount)/100);
									//price = parseFloat(resultVal.ConvertedUnitCost-discount);
									discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100);
									price = parseFloat(resultVal.ConvertedUnitCost-discount);
									resultVal.ConvertedUnitCost = price;}
							}else{
									$(obj).closest('tr').find("input[name=discounttype]").val("");
									$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat(resultVal.outgoingOBDiscount).toFixed(numberOfDecimal));
									//alert(resultVal.outgoingOBDiscount*currencyUSEQT);
								if(replacePreviousInvoiceCost=="1")
									price = parseFloat(resultVal.ConvertedUnitCost-(resultVal.outgoingOBDiscount*currencyUSEQT));
								else
									//price = parseFloat(resultVal.ConvertedUnitCost-resultVal.curOBDiscount);
									price = parseFloat(resultVal.ConvertedUnitCost-(resultVal.outgoingOBDiscount*currencyUSEQT));
									resultVal.ConvertedUnitCost = price;
							}
						}
						var calAmount = parseFloat(price).toFixed(numberOfDecimal);
						//alert(calAmount);
						$(obj).closest('tr').find("input[name=qty]").data('rl',resultVal.stkqty);
						$(obj).closest('tr').find("input[name=qty]").data('msq',resultVal.maxstkqty);
						$(obj).closest('tr').find("input[name=qty]").data('soh',resultVal.stockonhand);
						$(obj).closest('tr').find("input[name=qty]").data('eq',resultVal.EstQty);
						$(obj).closest('tr').find("input[name=qty]").data('aq',resultVal.AvlbQty);
						$(obj).closest('tr').find("input[name=ITEMDES]").data('pd',resultVal.ITEMDESC);
//						$(obj).closest('tr').find("input[name=cost]").val(resultVal.ConvertedUnitCost);
						$(obj).closest('tr').find("input[name=cost]").val(resultVal.ConvertedUnitCost);
						$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(resultVal.ConvertedUnitCosts).toFixed(numberOfDecimal));
						if(replacePreviousInvoiceCost=="1"){
						$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(resultVal.UNITPRICE));
						//$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(resultVal.itemprice));
						//$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(custcost));
						}
						
						var auprice = 0;
						var addshow = "0";
						if(resultVal.incpriceunit == "%"){
					//		auprice = parseFloat(resultVal.ConvertedUnitCost) + ((parseFloat(resultVal.ConvertedUnitCost)/100)*resultVal.incprice);
					//		addshow = parseFloat(resultVal.incprice).toFixed("3")+" "+resultVal.incpriceunit;
							auprice = parseFloat(price) + ((parseFloat(price)/100)*resultVal.incprice);
							addshow = parseFloat(resultVal.incprice).toFixed("3")+" "+resultVal.incpriceunit;
						}else{
							//auprice = parseFloat(resultVal.ConvertedUnitCost) + parseFloat(resultVal.incprice);
							//addshow = parseFloat(resultVal.ADDONCOST).toFixed(numberOfDecimal)+" "+resultVal.AODTYPE;
							auprice = parseFloat(price) + parseFloat(resultVal.incprice);
							addshow = parseFloat(resultVal.ADDONCOST).toFixed(numberOfDecimal)+" "+resultVal.AODTYPE;
						}
						if(replacePreviousInvoiceCost=="3"){
							$(obj).closest('tr').find("input[name=addonshow]").val(addshow);
							$(obj).closest('tr').find("input[name=addonprice]").val(resultVal.incprice);
							$(obj).closest('tr').find("input[name=addontype]").val(resultVal.incpriceunit);
							$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(resultVal.ConvertedUnitCosts).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(auprice));
							//$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(itemsPrice));
							$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(resultVal.itemprice));
						}
						
						if(replacePreviousInvoiceCost=="1"){
						$(obj).closest('tr').find("td:nth-child(1)").find('input[name=basecost]').val(resultVal.UNITPRICE);
						}
					}
					
					
					calculateAmount(obj);
					loadQtyToolTip(obj);
					loadUnitPriceToolTip(obj);
					loadItemDescToolTip(obj);
					if(deductInv=="1") {
					$(obj).closest('tr').find('input[name="loc"]').focus();
					$(obj).closest('tr').find('input[name="loc"]').select();
					} else {
					$(obj).closest('tr').find('input[name="qty"]').focus();
					$(obj).closest('tr').find('input[name="qty"]').select();
					}
				}
			});*/
			
			
			
			 var urlStr = "/track/ItemMstServlet";
	 var discount;
	 if(replacePreviousInvoiceCost=="1") {
		$(obj).closest('tr').find("input[name=addonshow]").val(parseFloat("0").toFixed(numberOfDecimal)+" "+currencyID);
		$(obj).closest('tr').find("input[name=addonprice]").val(parseFloat("0").toFixed(numberOfDecimal));
		$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(cost).toFixed(numberOfDecimal));
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				DONO: dono,
				TYPE: "SO",
				CURRENCYID: currencyID,
				CUSTCODE: custCode,
				UNITPRICE : price,
				UOM:salesuom,
				//ACTION : "GET_INVOICE_PRODUCT_DETAILS"
				ACTION : "GET_INVOICE_PRODUCT_AUTO_DETAILS" 
				},
				dataType : "json",
				success : function(data) {
					//outgoingOBDiscount
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
						console.log(resultVal);
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							$(obj).closest('tr').find("input[name=cost]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat("0.00000"));
							$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
						}else{
							$(obj).closest('tr').find("input[name=cost]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(resultVal.UNITPRICE));
							$(obj).closest('tr').find("input[name=unitpricerd]").val(resultVal.ConvertedUnitCost.match(regex)[0]);
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
						}if(resultVal.outgoingqty == null || resultVal.outgoingqty == undefined || resultVal.outgoingqty == 0){
							$(obj).closest('tr').find("input[name=outgoingqty]").val("0.000");
						}else{
							$(obj).closest('tr').find("input[name=outgoingqty]").val(resultVal.outgoingqty.match(regex)[0]);
						}if(resultVal.ConvertedUnitCosts == null || resultVal.ConvertedUnitCosts == undefined || resultVal.ConvertedUnitCosts == 0){
							$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
						}else{
							$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(resultVal.ConvertedUnitCosts).toFixed(numberOfDecimal));
						}

						//mprice=resultVal.minsprice;
						Convertedmprice=resultVal.minSellingConvertedUnitCost;
						$(obj).closest('tr').find("input[name=minsp]").val(Convertedmprice);

						if(resultVal.outgoingOBDiscount=='' || resultVal.outgoingOBDiscount=='0' ||resultVal.outgoingOBDiscount=='0.00'||resultVal.outgoingOBDiscount==undefined)
						{
							$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricediscount]").val(resultVal.ConvertedUnitCost);
						}
						else
						{
							if(resultVal.OBDiscountType=="BYPERCENTAGE")
							{
								$(obj).closest('tr').find("input[name=discounttype]").val("BYPERCENTAGE");
								$(obj).closest('tr').find("input[name=customerdiscount]").val(resultVal.outgoingOBDiscount.match(regex)[0]+'%');

								discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100);
								price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100));
							}
							else
							{
								$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat(resultVal.outgoingOBDiscount).toFixed(numberOfDecimal));
								price = parseFloat(resultVal.ConvertedUnitCost-(resultVal.outgoingOBDiscount*currencyUSEQT));
								//price = parseFloat(resultVal.outgoingOBDiscount);
							}
							var calAmount = parseFloat(price).toFixed(numberOfDecimal);
							$(obj).closest('tr').find("input[name=cost]").val(calAmount);
							//$(obj).closest('tr').find("input[name=itemprice]").val(calAmount);
							$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
							$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);
						}
					}
					$(obj).closest('tr').find("input[name=QTY]").data('rl',resultVal.stkqty);
					$(obj).closest('tr').find("input[name=QTY]").data('msq',resultVal.maxstkqty);
					$(obj).closest('tr').find("input[name=QTY]").data('soh',resultVal.stockonhand);
					$(obj).closest('tr').find("input[name=QTY]").data('eq',resultVal.EstQty);
					$(obj).closest('tr').find("input[name=QTY]").data('aq',resultVal.AvlbQty);
					$(obj).closest('tr').find("input[name=ITEMDES]").data('pd',resultVal.prdesc);
					
					calculateAmount(obj);
					loadQtyToolTip(obj);
					loadUnitPriceToolTip(obj);
					loadItemDescToolTip(obj);
						if(deductInv=="1") {
					$(obj).closest('tr').find('input[name="loc"]').typeahead('val', "'");
					$(obj).closest('tr').find('input[name="loc"]').typeahead('val', "");
					$(obj).closest('tr').find('input[name="loc"]').focus();
					$(obj).closest('tr').find('input[name="loc"]').select();
					} else {
					$(obj).closest('tr').find('input[name="qty"]').focus();
					$(obj).closest('tr').find('input[name="qty"]').select();
					}
				}
			});
		
			
	 } else if(replacePreviousInvoiceCost=="0"){

		 
		 $.ajax( {
				type : "POST",
				url : urlStr,
				data : {
					ITEM : productId,
					DONO: dono,
					TYPE: "SO",
					UOM:salesuom,
					CURRENCYID: currencyID,
					CUSTCODE: custCode,
					//ACTION : "GET_AVERAGE_COST_PRODUCT_DETAILS"
					ACTION :"GET_AVERAGE_COST_PRODUCT_AUTO_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						//outgoingOBDiscount
						if (data.status == "100") {
							var resultVal = data.result;
							var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
							console.log(resultVal);
							var auprice = 0;
							if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
								$(obj).closest('tr').find("input[name=cost]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat("0.00000"));
								$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
								$(obj).closest('tr').find("input[name=addonshow]").val(parseFloat("0").toFixed(numberOfDecimal)+" "+currencyID);
								 $(obj).closest('tr').find("input[name=addonprice]").val(parseFloat("0").toFixed(numberOfDecimal));
								 $(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(cost).toFixed(numberOfDecimal));
							}else{
								var iprice = 0;
								var addshow = "0";
								if(resultVal.incpriceunit == "%"){
									//auprice = parseFloat(resultVal.ConvertedUnitCost) + ((parseFloat(resultVal.ConvertedUnitCost)/100)*resultVal.incprice);
									//addshow = parseFloat(resultVal.incprice).toFixed("3")+" "+resultVal.incpriceunit;
									iprice = parseFloat(resultVal.ConvertedUnitCosts) + ((parseFloat(resultVal.ConvertedUnitCosts)/100)*resultVal.ADDONCOST);
									auprice = parseFloat(resultVal.ConvertedUnitCost) + ((parseFloat(resultVal.ConvertedUnitCost)/100)*resultVal.ADDONCOST);
									addshow = parseFloat(resultVal.ADDONCOST).toFixed("3")+" "+resultVal.AODTYPE;
								}else{
									//auprice = parseFloat(resultVal.ConvertedUnitCost) + parseFloat(resultVal.incprice);
									//addshow = parseFloat(resultVal.incprice).toFixed(numberOfDecimal)+" "+resultVal.incpriceunit;
									iprice = parseFloat(resultVal.ConvertedUnitCosts) + parseFloat(resultVal.ADDONCOST);
									auprice = parseFloat(resultVal.ConvertedUnitCost) + parseFloat(resultVal.ADDONCOST);
									addshow = parseFloat(resultVal.ADDONCOST).toFixed(numberOfDecimal)+" "+resultVal.AODTYPE;
								}
								
								
								$(obj).closest('tr').find("input[name=addonshow]").val(addshow);
								$(obj).closest('tr').find("input[name=addonprice]").val(resultVal.incprice);
								$(obj).closest('tr').find("input[name=addontype]").val(resultVal.incpriceunit);
								$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=itemcost]").val(parseFloat(resultVal.ConvertedUnitCosts).toFixed(numberOfDecimal));	
								$(obj).closest('tr').find("input[name=cost]").val(parseFloat(auprice).toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(iprice));
								$(obj).closest('tr').find("input[name=unitpricerd]").val(auprice);
								//$(obj).closest('tr').find("input[name=unitpricerd]").val(resultVal.ConvertedUnitCostWTC.match(regex)[0]);
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
							}if(resultVal.outgoingqty == null || resultVal.outgoingqty == undefined || resultVal.outgoingqty == 0){
								$(obj).closest('tr').find("input[name=outgoingqty]").val("0.000");
							}else{
								$(obj).closest('tr').find("input[name=outgoingqty]").val(resultVal.outgoingqty.match(regex)[0]);
							}

							//mprice=resultVal.minsprice;
							Convertedmprice=resultVal.minSellingConvertedUnitCost;
							$(obj).closest('tr').find("input[name=minsp]").val(Convertedmprice);

							if(resultVal.outgoingOBDiscount=='' || resultVal.outgoingOBDiscount=='0' ||resultVal.outgoingOBDiscount=='0.00'||resultVal.outgoingOBDiscount==undefined)
							{
								$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=unitpricediscount]").val(resultVal.ConvertedUnitCost);
							}
							else
							{
								if(resultVal.OBDiscountType=="BYPERCENTAGE")
								{
									$(obj).closest('tr').find("input[name=discounttype]").val("BYPERCENTAGE");
									$(obj).closest('tr').find("input[name=customerdiscount]").val(resultVal.outgoingOBDiscount.match(regex)[0]+'%');

									discount = parseFloat((auprice*resultVal.outgoingOBDiscount)/100);
									price = parseFloat(auprice-discount);
									//discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100);
									//price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100));
								}
								else
								{
									$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat(resultVal.outgoingOBDiscount).toFixed(numberOfDecimal));
									price = parseFloat(auprice-(resultVal.outgoingOBDiscount*currencyUSEQT));
									//price = parseFloat(resultVal.outgoingOBDiscount);
								}
								var calAmount = parseFloat(price).toFixed(numberOfDecimal);
								$(obj).closest('tr').find("input[name=cost]").val(calAmount);
								$(obj).closest('tr').find("input[name=itemprice]").val(calAmount);
								//$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
								$(obj).closest('tr').find("input[name=unitpricerd]").val(price);
								$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);
							}
						}
						$(obj).closest('tr').find("input[name=QTY]").data('rl',resultVal.stkqty);
						$(obj).closest('tr').find("input[name=QTY]").data('msq',resultVal.maxstkqty);
						$(obj).closest('tr').find("input[name=QTY]").data('soh',resultVal.stockonhand);
						$(obj).closest('tr').find("input[name=QTY]").data('eq',resultVal.EstQty);
						$(obj).closest('tr').find("input[name=QTY]").data('aq',resultVal.AvlbQty);
						$(obj).closest('tr').find("input[name=ITEMDES]").data('pd',resultVal.prdesc);

						calculateAmount(obj);
						loadQtyToolTip(obj);
						loadUnitPriceToolTip(obj);
						loadItemDescToolTip(obj);
							if(deductInv=="1") {
					$(obj).closest('tr').find('input[name="loc"]').typeahead('val', "'");
					$(obj).closest('tr').find('input[name="loc"]').typeahead('val', "");
					$(obj).closest('tr').find('input[name="loc"]').focus();
					$(obj).closest('tr').find('input[name="loc"]').select();
					} else {
					$(obj).closest('tr').find('input[name="qty"]').focus();
					$(obj).closest('tr').find('input[name="qty"]').select();
					}
					}
				});

	 
	 }else {
		 $(obj).closest('tr').find("input[name=addonshow]").val(parseFloat("0").toFixed(numberOfDecimal)+" "+currencyID);
		 $(obj).closest('tr').find("input[name=addonprice]").val(parseFloat("0").toFixed(numberOfDecimal));
		 $(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(cost).toFixed(numberOfDecimal));
		 $.ajax( {
				type : "POST",
				url : urlStr,
				data : {
					ITEM : productId,
					DONO: dono,
					TYPE: "SO",
					CURRENCYID: currencyID,
					CUSTCODE: custCode,
					REPLACEPREVIOUSSALESCOST: replacePreviousInvoiceCost,
					//ACTION : "GET_PRODUCT_DETAILS"
					ACTION :"GET_PRODUCT_AUTO_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						//outgoingOBDiscount
						if (data.status == "100") {
							var resultVal = data.result;
							var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
							console.log(resultVal);
							if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
								$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
								//$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat("0.00000"));
								$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
							}else{
							//imthi start for cost and aod curency conv
							var auprice = 0;
								var addshow = "0";
								if(resultVal.incpriceunit == "%"){
//									auprice = parseFloat(resultVal.ConvertedUnitCost) + ((parseFloat(resultVal.ConvertedUnitCost)/100)*resultVal.incprice);
	//								addshow = parseFloat(resultVal.incprice).toFixed("3")+" "+resultVal.incpriceunit;
									auprice = parseFloat(cost) + ((parseFloat(cost)/100)*resultVal.incprice);
									addshow = parseFloat(resultVal.incprice).toFixed("3")+" "+resultVal.incpriceunit;
								}else{
//									auprice = parseFloat(resultVal.ConvertedUnitCost) + parseFloat(resultVal.incprice);
//									addshow = parseFloat(resultVal.incprice).toFixed(numberOfDecimal)+" "+resultVal.incpriceunit;
									auprice = parseFloat(cost) + parseFloat(resultVal.incprice);
									addshow = parseFloat(resultVal.ADDONCOST).toFixed(numberOfDecimal)+" "+resultVal.AODTYPE;
								}
								
								if(replacePreviousInvoiceCost==3) {
								$(obj).closest('tr').find("input[name=addonshow]").val(addshow);
								$(obj).closest('tr').find("input[name=addonprice]").val(resultVal.incprice);
								$(obj).closest('tr').find("input[name=addontype]").val(resultVal.incpriceunit);
								$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(auprice));
								}
								//imthi end
								$(obj).closest('tr').find("input[name=cost]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
								//$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(resultVal.ConvertedUnitCost));
								$(obj).closest('tr').find("input[name=unitpricerd]").val(resultVal.ConvertedUnitCost);
//								$(obj).closest('tr').find("input[name=unitpricerd]").val(resultVal.ConvertedUnitCostWTC.match(regex)[0]);
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
							}if(resultVal.outgoingqty == null || resultVal.outgoingqty == undefined || resultVal.outgoingqty == 0){
								$(obj).closest('tr').find("input[name=outgoingqty]").val("0.000");
							}else{
								$(obj).closest('tr').find("input[name=outgoingqty]").val(resultVal.outgoingqty.match(regex)[0]);
							}if(resultVal.ConvertedUnitCosts == null || resultVal.ConvertedUnitCosts == undefined || resultVal.ConvertedUnitCosts == 0){
								$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							}else{
								$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(resultVal.ConvertedUnitCosts).toFixed(numberOfDecimal));
							}

							//mprice=resultVal.minsprice;
							Convertedmprice=resultVal.minSellingConvertedUnitCost;
							$(obj).closest('tr').find("input[name=minsp]").val(Convertedmprice);

							if(resultVal.outgoingOBDiscount=='' || resultVal.outgoingOBDiscount=='0' ||resultVal.outgoingOBDiscount=='0.00'||resultVal.outgoingOBDiscount==undefined)
							{
								$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=unitpricediscount]").val(resultVal.ConvertedUnitCost);
							}
							else
							{
								if(resultVal.OBDiscountType=="BYPERCENTAGE")
								{
									$(obj).closest('tr').find("input[name=discounttype]").val("BYPERCENTAGE");
									$(obj).closest('tr').find("input[name=customerdiscount]").val(resultVal.outgoingOBDiscount.match(regex)[0]+'%');

									discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100);
									price = parseFloat(resultVal.ConvertedUnitCost-discount);
									//price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100));
								}
								else
								{
									$(obj).closest('tr').find("input[name=discounttype]").val("");
									$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat(resultVal.outgoingOBDiscount).toFixed(numberOfDecimal));
									price = parseFloat(resultVal.ConvertedUnitCost-(resultVal.outgoingOBDiscount*currencyUSEQT));
									//price = parseFloat(resultVal.outgoingOBDiscount);
								}
								var calAmount = parseFloat(price).toFixed(numberOfDecimal);
								$(obj).closest('tr').find("input[name=cost]").val(calAmount);
								//$(obj).closest('tr').find("input[name=itemprice]").val(calAmount);
								$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
								$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);
							}
						}
						$(obj).closest('tr').find("input[name=QTY]").data('rl',resultVal.stkqty);
						$(obj).closest('tr').find("input[name=QTY]").data('msq',resultVal.maxstkqty);
						$(obj).closest('tr').find("input[name=QTY]").data('soh',resultVal.stockonhand);
						$(obj).closest('tr').find("input[name=QTY]").data('eq',resultVal.EstQty);
						$(obj).closest('tr').find("input[name=QTY]").data('aq',resultVal.AvlbQty);
						$(obj).closest('tr').find("input[name=ITEMDES]").data('pd',resultVal.prdesc);

						calculateAmount(obj);
						loadQtyToolTip(obj);
						loadUnitPriceToolTip(obj);
						loadItemDescToolTip(obj);
							if(deductInv=="1") {
					$(obj).closest('tr').find('input[name="loc"]').typeahead('val', "'");
					$(obj).closest('tr').find('input[name="loc"]').typeahead('val', "");
					$(obj).closest('tr').find('input[name="loc"]').focus();
					$(obj).closest('tr').find('input[name="loc"]').select();
					} else {
					$(obj).closest('tr').find('input[name="qty"]').focus();
					$(obj).closest('tr').find('input[name="qty"]').select();
					}
					}
				});

	 }
}







function calculateAmount(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var state = document.form1.STATE_PREFIX.value;
	var plant = document.form1.plant.value;
	
	var qty = parseFloat($(obj).closest('tr').find('input[name=qty]').val()).toFixed(3);
	var cost = parseFloat($(obj).closest('tr').find('input[name=cost]').val()).toFixed(numberOfDecimal);
	var itemDiscount = parseFloat($(obj).closest('tr').find('input[name=item_discount]').val()).toFixed(numberOfDecimal);
	var discounType = $(obj).closest('tr').find('select[name=item_discounttype]').val();
	var ordQty = parseFloat($(obj).closest('tr').find("td:nth-child(1)").find('input[name=ORDQTY]').val());
	if(!isNaN(ordQty)){
		var invstatus = $("input[name ='billstatus']").val();
		if(invstatus == "Draft"){
			
		}else if(invstatus == "Open"){
			
		}else{
			if(qty<ordQty){
				alert("Quantity cannot be less than "+ordQty);
				$(obj).closest('tr').find('input[name=qty]').val(parseFloat(ordQty).toFixed(3));
				return false;
			}
		}
	}
	
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
	
	//changetaxfordiscountnew();
	calculateTotal();
}

function calculateAmountcost(obj,ocost){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var qty = parseFloat($(obj).closest('tr').find("td:nth-child(3)").find('input').val()).toFixed(3);
	var costcheck = parseFloat($(obj).closest('tr').find("td:nth-child(4)").find('input').val()).toFixed(numberOfDecimal);
	var cost=0;
	if(ocost <= costcheck){
		cost=costcheck;
	}else{
		alert("Unit Price should be greater than the Order price");
		cost=parseFloat(ocost).toFixed(numberOfDecimal);
		
	}
	var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(5)").find('input').val()).toFixed(numberOfDecimal);
	var discounType = $(obj).closest('tr').find("td:nth-child(5)").find('select').val();
	//var discounType = $(this).closest('td').find('discountPicker form-control item_discounttypeSearch').text();
	$(obj).closest('tr').find("td:nth-child(5)").each(function () {
	    var quantity = $(obj).find('input').val();
	    alert(quantity);
	});
	//discounType = $(obj).closest('tr').find("td:nth-child(5)").find('input').val();
	
	
	
	var itemDiscountval=itemDiscount;
	 if(discounType == "%"){
		 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
	 }
	 
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
	
	$(obj).closest('tr').find("td:nth-child(3)").find('input').val(qty);
	$(obj).closest('tr').find("td:nth-child(4)").find('input').val(cost);
	//$(obj).closest('tr').find("td:nth-child(5)").find('input').val(itemDiscount);
	$(obj).closest('tr').find("td:nth-child(7)").find('input').val(amount);
	
	calculateTotal();
}

/*function calculateTotal(){
	

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
	debugger;
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
	var html="";
	var taxTotal = 0;
	var subtotal =0;
	var numberOfDecimal = $("#numberOfDecimal").val();
	$.each(taxList, function( key, data ) {
		var mydata=JSON.stringify(data);
		console.log("Tax dta:"+mydata);
		var originalTaxType= data.types;
		var taxtype=originalTaxType.substr(0, originalTaxType.indexOf('(')); 
		if(taxtype == ""){
			taxtype = originalTaxType;
		}
		console.log("Tax Type:"+taxtype);
		console.log("Zero Type:"+zerotype);
		percentageget=data.percentage;
		
		var shipingcost = document.form1.shippingcost.value;
		var shipingtax = parseFloat(shipingcost*(percentageget/100)).toFixed(numberOfDecimal);
		var discount = document.form1.discount.value;
		var discounType = $('select[name ="discount_type"]').val();
		$(".bill-table tbody tr td:last-child").prev().each(function() {
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
	calculateTotal();
	($("#item_rates").val() == 0) ? $(".productRate").hide() : $(".productRate").show(); 
	
	if($("#item_rates").val() == 0){
		$('input[name ="taxamount"]').val(taxTotal);
	}else{
		$(".taxDetails").html("");
		$('input[name ="taxamount"]').val("0");
	}
}

function validateInvoice(){
	var supplier = document.form1.CUSTOMER.value;
	var bill = document.form1.invoice.value;
	var billDate = document.form1.invoice_date.value;
	var itemRates = document.form1.item_rates.value;
	var currency = $("input[name=CURRENCY]").val();
	var CURRENCYUSEQT = document.form1.CURRENCYUSEQT.value;
    var isEmployee = $("input[name=ISEMPLOYEEVALIDATESO]").val();
	var Employeee = $("input[name=EMP_NAME]").val();
    
	var discount = document.form1.discount.value;
	var discountAccount = document.form1.discount_account.value;
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
	
	if(isEmployee == "1"){
		if(Employeee == ""){
			alert("Please select a Employee.");
			document.getElementById("EMP_NAME").focus();
			return false;
		}
	}
	
	if(currency == ""){
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
	$('#discount_type').prop('disabled',false);
	$('#SALES_LOC').prop('disabled',false);
	$("select[name='oddiscount_type']").prop('disabled',false);
	
	/* Author: Azees  Create date: July 3,2021  Description: Stock Validation */
	if($("input[name=DEDUCT_INV]").val() != "0"){
		var plant = document.form1.plant.value;
		var lnno =0;
		var totlnno ="";
		var errorBoo = false;
		$(".bill-table tbody tr").each(function() {
			var kitem = $(this).find("input[name ='item']").val();
			var kuom = $(this).find("input[name ='uom']").val();
			var kloc = $(this).find("input[name ='loc']").val();
			var kbatch = $(this).find("input[name ='batch']").val();
			var kqty = $(this).find("input[name ='qty']").val();
			var kordqty = $(this).find("input[name ='ORDQTY']").val();
			
			var kqty = parseFloat(kqty) - parseFloat(kordqty);
			
			if(kqty > 0){
					var urlStr = "../MasterServlet";
					$.ajax({
						type : "POST",
						url : urlStr,
						async : false,
						data : {
							PLANT : plant,
							ACTION : "GET_STOCK_BATCH_DATA",
							QUERY : kbatch,
							ITEMNO : kitem,
							UOM : kuom,
							LOC : kloc,
							QTY : kqty
						},
						cache: false,
						dataType : "json",
						success : function(data) {
							lnno=lnno+1;
							if(data.batches[0].ERROR_CODE=='98')
							{
								if(totlnno=="")
									totlnno=lnno;
								else
									totlnno=totlnno+","+lnno;
								errorBoo = true;
							}						
						}
					});
			}
		});
		
			if(errorBoo)
				{
					alert("Not enough inventory found for ProductID/Batch for Order Line No "+ totlnno + " in the location selected");
					return false;
				}
		
	}
	
	return true;
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

function gstCallback(data){
	if(data.STATUS="SUCCESS"){
		alert(data.MESSAGE);
		$("input[name ='tax_type']").typeahead('val', data.GST);
	}
}

function productCallbackwithallInvoice(productData){
	if(productData.STATUS="SUCCESS"){
		var $tbody = $(".bill-table tbody");
		var $last = $tbody.find('tr:last');
		var taxdisplay = $("input[name=ptaxdisplay]").val();
		 var catalogPath=productData.CATLOGPATH;
		 var account="Local sales - retail";
		 var cost=productData.UNITPRICE;
		 var salesuom=productData.SALESUOM;
		 var productId=productData.ITEM;
		 var curency = document.form1.CURRENCYID.value;
		 $last.remove();
		 var deductInv = document.form1.DEDUCT_INV.value;
		 var origin = document.form1.ORIGIN.value;
		 var ITEM_DESC = escapeHtml(productData.ITEM_DESC);	
		var body="";
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+productData.CATLOGPATH+'" style="width: 100%;">';
		body += '<input type="hidden" name="minsp" value="">';
		body += '<input type="hidden" name="ORDQTY" value="0.0">';
		body += '<input type="hidden" name="basecost" value="0.00">';
		body += '<input type="hidden" name="customerdiscount" value="">';
		body += '<input type="hidden" name="outgoingqty" value="">';
		body += '<input type="hidden" name="itemprice" value="0.00">';
		body += '<input type="hidden" name="discounttype" value="">';
		body += '<input type="hidden" name="itemcost" value="">';
		body += '<input type="hidden" name="IS_COGS_SET" value="N">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control itemSearch" value="'+productData.ITEM+'" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
		body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
		body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly value="'+ITEM_DESC+'">';
		body += '<input type="hidden" name="edit_item" class="form-control" value="">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Account"  value="Local sales - retail">';
		body += '</td>';
		if(deductInv == "1" && origin == "manual"){
		body += '<td class="invEl">';
		body += '<input type="text" name="uom" class="form-control uomSearch"  value="'+productData.PURCHASEUOM+'"  placeholder="UOM">';
		body += '</td>';
		body += '<td class="invEl">';
		body += '<input type="text" name="loc" class="form-control locSearch" placeholder="Location">';
		body += '</td>';
		body += '<td class="invEl">';
		body += '<input type="text" name="batch" value="NOBATCH" class="form-control batchSearch" placeholder="Batch">';
		body += '</td>';
		}
//		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)"></td>';
		body += '<td class="item-qty text-right">';
		body += '<input type="text" data-rl="0.000" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" data-pq="0.000" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
		body += '<input type="hidden" name="edit_qty" class="form-control text-right" value="0.000" ">';
		body += '</td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+productData.UNITPRICE+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
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
		body += '<td class="item-tax">';
		body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+productData.UNITPRICE+'" readonly="readonly" style="display:inline-block;"></td>';
		body += '</tr>';
		$(".bill-table tbody").append(body);
//		calculateTotal();
		removeSuggestionToTable();
		addSuggestionToTable();
		loadItemData($tbody.find('tr:last'), catalogPath, account, cost,salesuom,productId,price);
	}
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

function setCustomerData(customerData){	
	$("input[name ='CUST_CODE']").val(customerData.vendno);
	$("#CUSTOMER").typeahead('val', customerData.vendname);	
	/* To reset Order number Autosuggestion*/
	$("#ORDERNO").typeahead('val', '"');
	$("#ORDERNO").typeahead('val', '');
}

//function productCallback(productData){
//	if(productData.STATUS="SUCCESS"){
//		alert(productData.MESSAGE);
//		$("input[name ='item']").typeahead('val', productData.ITEM);
//	}
//}

function payTermsCallback(payTermsData){
	if(payTermsData.STATUS="SUCCESS"){
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
	}
}

function getOrderDetailsForInvoice(gino){
	var plant = document.form1.plant.value;
	var DONO = document.form1.ORDERNO.value;
	var replacePreviousInvoiceCost = $("input[name=replacePreviousInvoiceCost]").val();
	$.ajax({
		type : "POST",
		url : "/track/InvoiceServlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_ORDER_DETAILS_USING_INVOICENO",
			REPLACEPREVIOUSSALESCOST: replacePreviousInvoiceCost,
			INVOICENO : gino,
			DONO: DONO
		},
		dataType : "json",
		success : function(data) {
			loadBillTable(data.orders);
		}
	});
}

function loadBillTable(orders){
	var curency = $("input[name=CURRENCYID]").val();
	var numberOfDecimal = $("#NOFO_DEC").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var taxdisplay = $("input[name=ptaxdisplay]").val();
	var body="";
	var STATE_PREFIX= document.form1.STATE_PREFIX.value;
	var DO_STATE_PREFIX= document.form1.DO_STATE_PREFIX.value;
	taxList = [];
	$.each(orders, function( key, data ) {
		var dqty = data.QTYOR;
		var duprz = data.UNITPRICE;
		var damt = data.AMOUNT;
	/*	if(DO_STATE_PREFIX!="")
		{	
		}
	else
		{
		STATE_PREFIX=data.SALES_LOCATION;
		$("select[name ='SALES_LOC']").val(data.SALES_LOCATION);
		$("input[name ='STATE_PREFIX']").val(data.SALES_LOCATION);
		}*/
		
		var tamount = "0";
		var discamount = "0"
		if(data.ITEM_DISCOUNT_TYPE == "%"){
			var totalamount = parseFloat(data.AMOUNT) - ((parseFloat(data.AMOUNT)/100)*(parseFloat(data.ITEM_DISCOUNT)));;
			tamount = parseFloat(totalamount).toFixed(numberOfDecimal);
		}else{
			var totalamount = parseFloat(data.AMOUNT) - ((parseFloat(data.ITEM_DISCOUNT*data.CURRENCYUSEQT)/parseFloat(data.ORQTY))*(parseFloat(data.QTYOR)));
			tamount = parseFloat(totalamount).toFixed(numberOfDecimal);	
			discamount = parseFloat((parseFloat(data.ITEM_DISCOUNT)/parseFloat(data.ORQTY))*(parseFloat(data.QTYOR))).toFixed(numberOfDecimal);
		}
		var ITEMDESC = escapeHtml(data.ITEMDESC);
		
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<input type="hidden" name="convcost" value="'+data.CONVCOST+'">';
		body += '<input type="hidden" name="basecost" value="'+data.BASECOST+'">';
		body += '<input type="hidden" name="uom" value="'+data.UOM+'">';
		body += '<input type="hidden" name="lnno" value="'+data.LNNO+'">';
		body += '<input type="hidden" name="dolnno" value="'+data.DOLNNO+'">';
		body += '<input type="hidden" name="minsp" value="'+data.minSellingConvertedUnitCost+'">';
		body += '<input type="hidden" name="customerdiscount" value="'+data.outgoingOBDiscount+'">';
		
		body += '<input type="hidden" name="reorder" value="'+data.stkqty+'">';
		body += '<input type="hidden" name="outgoingqty" value="'+data.outgoingqty+'">';
		body += '<input type="hidden" name="itemprice" value="0.00">';
		body += '<input type="hidden" name="discounttype" value="">';
		body += '<input type="hidden" name="itemcost" value="">';
		body += '<input type="hidden" name="maxstkqty" value="'+data.maxstkqty+'">';
		body += '<input type="hidden" name="stkqty" value="'+data.stockonhand+'">';
		body += '<input type="hidden" name="estqty" value="'+data.EstQty+'">';
		body += '<input type="hidden" name="avlqty" value="'+data.AvlbQty+'">';
		
		body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
		body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+taxdisplay+'">';
		body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" readonly="readonly">';
		body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly data-pd="'+ITEMDESC+'" value="'+ITEMDESC+'">';
		body += '</td>';
		body += '<td class="">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" value="'+data.ACCOUNT+'">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="'+parseFloat(data.QTYOR).toFixed(3)+'" readonly="readonly"></td>';
			body += '<td>';
			body += '<input type="text" name="unitcost" class="form-control text-right" value="'+data.UCOST+'" readonly tabindex="-1">';
			body += '</td>';
			body += '<td>';
			body += '<input type="text" name="addonshow" class="form-control text-right" value="'+data.AOD+'" readonly tabindex="-1">';
			//body += '<input type="hidden" name="addonprice" value="'+szero+'">';
			body += '<input type="hidden" name="addonprice" value="'+data.INCPRICE+'">';
			body += '<input type="hidden" name="addontype" value="'+data.CPPI+'">';
			body += '</td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITPRICE+'"  onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
//		body += '<td class="item-discount text-right">';
//		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" readonly="readonly">';
//		body += '<input name="item_discounttype" type="text" class="form-control text-right" value="%" hidden>';
//		body += '</td>';
		
		/*body += '<td class="item-tax">';
		body += '<input type="text" name="tax_type" class="form-control" value="SALES ['+data.OUTBOUND_GST+'%]" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" readonly="readonly" style="display:inline-block;"></td>';*/
		
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		
		/*body += "<input name=\"item_discount\" type=\"text\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\">";
		//body += '<textarea rows="1" maxlength="3" style="resize: none;padding: 5px;width:32% !important;background: #fbfafa;" class="form-control item_discounttypeSearch" id="item_discounttype" name="item_discounttype" onchange="calculateAmount(this)">'+data.ITEM_DISCOUNT_TYPE+'</textarea>';
		//body += "<input type=\"text\" class=\"discountPicker form-control item_discounttypeSearch\" id=\"item_discounttype\" value="+data.ITEM_DISCOUNT_TYPE+" name=\"item_discounttype\" onchange=\"calculateAmount(this)\">";
		body += '<select name="item_discounttype" id="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
		body += "<option value="+data.CURRENCYID+">"+data.DISPLAY+"</option>";
		body += '<option value="%">%</option>';										
		body += '</select>';*/
		
		if(data.ITEM_DISCOUNT_TYPE == "%"){
			body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)" readonly>';
			}else{
				body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(discamount*data.CURRENCYUSEQT).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)" readonly>';	
			}
			body += '<select name="item_discounttype" class="discountPicker form-control item_discount_type" onchange="calculateAmount(this)">';
			if(data.ITEM_DISCOUNT_TYPE == data.CURRENCYID){
				body += "<option selected value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
			}else{
				body += "<option value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
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
		/*body += '<td class="item-tax">';*/
		/*body += '<input type="hidden" name="tax_type" value="SALES('+STATE_PREFIX+') ['+data.OUTBOUND_GST+'%]" class="form-control">';
		body += '<input type="text" name="tax" class="form-control" value="SALES('+STATE_PREFIX+') ['+data.OUTBOUND_GST+'%]" readonly="readonly">';*/
		/*body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'" class="form-control">';
		body += '<input type="text" name="tax" class="form-control taxSearch" value="'+taxdisplay+'" readonly="readonly">';
		body += '</td>';*/
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+tamount+'" readonly="readonly" style="display:inline-block;" tabindex="-1">';
			body += '<td class="table-icon" style="position:relative;">';
			body += '<a href="#" onclick="showRemarksDetails(this)">';
			body += '<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>';
			body += '</a>';
			body += '<span class="glyphicon glyphicon-remove-circle bill-action" style="left: 35px;"aria-hidden="true"></span>';
			body += '</td>';
			body += '</td>';
		body += '</tr>';

		/*var tax = new Object();
		var percentage = data.OUTBOUND_GST;
		
		tax.types = "SALES("+STATE_PREFIX+")";
		tax.percentage = percentage;
		tax.display = 'SALES('+STATE_PREFIX+') ['+data.OUTBOUND_GST+'%]';

		var amount = damt;
		discountValue = parseFloat(amount*(percentage/100)).toFixed(numberOfDecimal);
		tax.value = discountValue;

		if(taxList.length == 0){
			taxList.push(tax);
		}else{
			var match = false;
			$.each(taxList, function( key, data ) {
				if(data.types == tax.types){
					data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
					match = true;
				}
			});
			if(!match){
				taxList.push(tax);
			}
		}*/
//		$("input[name ='transport']").val(data.TRANSPORTNAME);
//		$("input[name ='TRANSPORTID']").val(data.TRANSPORTID);
//		$("input[name ='SHIPPINGID']").val(data.SHIPPINGID);
//		$("input[name ='SHIPPINGCUSTOMER']").val(data.SHIPPINGCUSTOMER);
		$("input[name ='Salestax']").val(taxdisplay);
		$("input[name ='DO_STATE_PREFIX']").val(data.STATE_PREFIX);
		//document.getElementById('TAXTREATMENT').innerHTML=data.TAXTREATMENT;
		$('select[name ="nTAXTREATMENT"]').val(data.TAXTREATMENT);
		$("input[name ='shippingcost']").val(parseFloat(data.SHIPPINGCOST*data.CURRENCYUSEQT).toFixed(numberOfDecimal));	
		$("input[name ='oShippingcost']").val(parseFloat(data.SHIPPINGCOST*data.CURRENCYUSEQT));
		//$("input[name ='orderdiscount']").val(data.ORDERDISCOUNT);
		
		if(data.ORDERDISCOUNTTYPE == "%"){
			$("select[name='oddiscount_type']").empty();
			$("select[name='oddiscount_type']").append('<option selected value="%">%</option>');
			var orderdiscount = parseFloat(data.ORDERDISCOUNT).toFixed(3);
			$("input[name ='orderdiscount']").val(orderdiscount);
			$("input[name ='aorderdiscount']").val(orderdiscount);
		}else{
			$("select[name='oddiscount_type']").empty();
			$("select[name='oddiscount_type']").append('<option selected value="' + data.CURRENCYID + '">' + data.CURRENCYID + '</option>');
			var orderdiscount = parseFloat(data.ORDERDISCOUNT*data.CURRENCYUSEQT);
			orderdiscount = parseFloat(orderdiscount).toFixed(numberOfDecimal);
			$("input[name ='orderdiscount']").val(orderdiscount);
			$("input[name ='aorderdiscount']").val(orderdiscount);
			$("input[name ='orderdiscount']").attr('readonly',false);
		}
		
		$("input[name ='CURRENCYID']").val(data.CURRENCYID);
		$("input[name ='CURRENCY']").val(data.DISPLAY);
		
		var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
		document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+data.CURRENCYID+")";
		document.getElementById('AODTYPE').innerHTML = "Add On (% / "+data.CURRENCYID+")";
		document.getElementById('lbltotal').innerHTML = "Total ("+data.CURRENCYID+")"; //  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency

		if(basecurrency!=data.CURRENCYID)
			document.getElementById('showtotalcur').style.display = 'block';
		else
			document.getElementById('showtotalcur').style.display = 'none';
		
		$("input[name ='invoice_date']").val(data.GOOD_DATE);
		$("input[name ='EMP_NAME']").val(data.EMPNAME);
		$("input[name ='CURRENCYUSEQT']").val(data.CURRENCYUSEQT);
		$('#CURRENCY').css("background-color", "#eeeeee");
		
		$('#discount_type').empty();
		$('#discount_type').append('<option value="' + data.CURRENCYID + '">' + data.CURRENCYID + '</option>');
		$('#discount_type').append('<option value="%">%</option>');
	});
	$(".bill-table tbody").html(body);
	setLineNo();
	$('#SALES_LOC').prop('disabled',true);	
	$("input[name ='isodisctax']").prop('disabled',true);	
	//$("input[name ='isdtax']").prop('disabled',true);
	$("input[name ='isshiptax']").prop('disabled',true);	
	//renderTaxDetails();
	calculateTotal();
	removeSuggestionToTable();
	addSuggestionToTable();
	calltooltips();
	
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


function getEditInvoiceDetail(TranId){
	var plant = document.form1.plant.value;
	var CUR = document.form1.BASECURRENCYID.value;
	$.ajax({
		type : "POST",
		url : "/track/InvoiceServlet",
		async : true,
		data : {
			PLANT : plant,
			CUR : CUR,
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
	var deductInv = document.form1.DEDUCT_INV.value;
	var origin = document.form1.ORIGIN.value;
	var tono = document.form1.TONO.value;
	var educations = document.form1.EDUCATION.value;
	var setreadonly='';
	if(tono != ""){
		setreadonly='readonly';
	}	
	var echeck = "0";
	var body="";
	taxList = [];
	$.each(orders, function( key, data ) {
		
		var numberOfDecimal = $("#numberOfDecimal").val();
		/*var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");
		
		tax.types = percentage[0].trim();
		tax.percentage = percentage[1];
		tax.display = data.TAX_TYPE;
		var taxpercentage=percentage[1];
		var taxtype=percentage[0].trim();
		var loadtype=taxtype.substr(0, taxtype.indexOf('(')); */
		var uprice= parseFloat((data.UNITPRICE*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
		var amt= parseFloat((data.AMOUNT*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
		var disc="0";
		var ITEMDESC = escapeHtml(data.ITEMDESC);
		/*if(loadtype=="ZERO RATE")
			{
				zerotype++;
			}*/
		if(data.ORDERNO!="" && data.INVOICE!="")
		{	
			echeck="1";
			
			body += '<tr>';
			body += '<td class="item-img text-center">';
			body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"><img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
			body += '<input type="hidden" name="lnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="dolnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="minsp" value="'+data.minSellingConvertedUnitCost+'">';
			body += '<input type="hidden" name="customerdiscount" value="'+data.outgoingOBDiscount+'">';
			body += '<input type="hidden" name="outgoingqty" value="'+data.outgoingqty+'">';
			body += '<input type="hidden" name="itemprice" value="0.00">';
			body += '<input type="hidden" name="discounttype" value="">';
			body += '<input type="hidden" name="itemcost" value="">';
			body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.TAX_TYPE+'">';
			body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+data.TAX_TYPE+'">';
			if(deductInv == "1" && origin == "manual"){
			}else{
				body += '<input type="hidden" name="uom" class="form-control" onchange="checkprduom(this.value,this)" value="'+data.UOM+'" readonly="readonly">';
			}
			if(data.BILL_STATUS !="Draft"){
				body += '<input type="hidden" name="ORDQTY" value="'+data.QTY+'" readonly="readonly">';
			}			
			body += '<input type="hidden" name="LNNO" value="'+data.LNNO+'" readonly="readonly">';
			body += '<input type="hidden" name="IS_COGS_SET" value="'+data.IS_COGS_SET+'">';			
			body += '</td>';
			body += '<td class="bill-item">';
			body += '<input type="text" name="item" class="form-control" onchange="checkitems(this.value,this)" value="'+data.ITEM+'" readonly="readonly">';
			body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly value="'+ITEMDESC+'">';
			body += '<input type="hidden" name="edit_item" class="form-control" onchange="checkitems(this.value,this)" value="'+data.ITEM+'">';
			body += '</td>';
			body += '<td class="">';
			body += '<input type="text" name="account_name" class="form-control" value="'+data.ACCOUNT_NAME+'" readonly="readonly">';
			body += '</td>';
			if(deductInv == "1" && origin == "manual"){
			body += '<td class="">';
			body += '<input type="text" name="uom" class="form-control" onchange="checkprduom(this.value,this)" value="'+data.UOM+'" readonly="readonly">';
			body += '</td>';
			body += '<td class="">';
			body += '<input type="text" name="loc" class="form-control" onchange="checkprdloc(this.value,this)" value="'+data.LOC+'" readonly="readonly">';
			body += '</td>';
			body += '<td class="">';
			body += '<input type="text" name="batch" class="form-control" value="'+data.BATCH+'" readonly="readonly">';
			body += '</td>';
			}
			body += '<td class="item-qty text-right">';
			body += '<input type="text" name="qty" data-rl="'+data.stkqty+'" data-msq="'+data.maxstkqty+'" data-soh="'+data.stockonhand+'" data-eq="'+data.EstQty+'" data-aq="'+data.AvlbQty+'" class="form-control text-right" value="'+parseFloat(data.QTY).toFixed(3)+'" readonly="readonly">';
			body += '<input type="hidden" name="edit_qty" class="form-control" value="'+data.QTY+'">';
			body += '</td>';
			body += '<td>';
			body += '<input type="text" name="unitcost" class="form-control text-right" value="'+data.UCOST+'" readonly>';
			body += '</td>';
			body += '<td>';
			body += '<input type="text" name="addonshow" class="form-control text-right" value="'+data.AOD+'" readonly>';
			//body += '<input type="hidden" name="addonprice" value="'+szero+'">';
			body += '<input type="hidden" name="addonprice" value="'+data.CPPI+'">';
			//body += '<input type="hidden" name="addontype" value="'+curency+'">';
			body += '<input type="hidden" name="addontype" value="'+data.DETADDTYPE+'">';
			body += '</td>';
			body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+uprice+'" readonly="readonly"></td>';
			body += '<td class="item-discount text-right">';
			body += '<div class="row"><div class=" col-lg-12 col-sm-3 col-12"><div class="input-group my-group" style="width:120px;">';
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			}else{
				body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT*data.CURRENCYUSEQT).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			}
			
			/*body += '<input name="item_discounttype" type="text" class="form-control text-right" value="%" hidden>';*/
			body += '<select name="item_discounttype" class="discountPicker form-control item_discount_type" onchange="calculateAmount(this)">';
			//body += '<option value="'+data.ITEM_DISCOUNT_TYPE+'">'+data.ITEM_DISCOUNT_TYPE+'</option>';	
			if(data.ITEM_DISCOUNT_TYPE == data.CURRENCYID){
				body += "<option selected value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
			}else{
				body += "<option value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
			}
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<option selected >%</option>';		
			}else{
				body += '<option>%</option>';		
			}
			body += '</select>';
			body += '</div></div></div>';
			body += '</td>';
			/*body += '<td class="item-tax">';
			body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'"  >';*/			
			//body += '<input type="text" name="tax" class="form-control" value="'+data.TAX_TYPE+'" readonly="readonly">';
			/*if(data.TAX_TYPE=="EXEMPT[0.0%]")
			{
				body += '<input type="text" name="tax" class="form-control taxSearch" value="EXEMPT" readonly="readonly">';
			}
			else if(data.TAX_TYPE=="OUT OF SCOPE[0.0%]")
				{
				body += '<input type="text" name="tax" class="form-control taxSearch" value="OUT OF SCOPE" readonly="readonly">';
				}
			else
				{	
				if(data.TAX_TYPE == "" || data.TAX_TYPE == null){
					body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax">';
				}else{
					body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly="readonly" placeholder="Select a Tax">';
				}
				
				}*/
			/*body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly="readonly" placeholder="Select a Tax">';
			body += '</td>';*/
			//body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
			//body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" readonly="readonly" style="display:inline-block;"></td>';
			
			body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
			body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" style="display:inline-block;"  readonly="readonly">';
			body += '<td class="table-icon" style="position:relative;">';
			if(echeck != "1"){
				body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
			}
			body += '<a href="#" onclick="showRemarksDetails(this)">';
			body += '<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>';
			body += '</a>';
			if(echeck != "1"){
			body += '<span class="glyphicon glyphicon-remove-circle bill-action" style="left: 35px;"aria-hidden="true"></span>';
			}
			body += '</td>';
			body += '</td>';
			//body += '</tr>';
			
			body += '</tr>';
			$(".add-line").hide();
		}
		else
			{
			body += '<tr>';
			body += '<td class="item-img text-center">';
			body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
			body += '<input type="hidden" name="lnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="dolnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="minsp" value="'+data.minSellingConvertedUnitCost+'">';
			body += '<input type="hidden" name="customerdiscount" value="'+data.outgoingOBDiscount+'">';
			body += '<input type="hidden" name="outgoingqty" value="'+data.outgoingqty+'">';
			body += '<input type="hidden" name="itemprice" value="0.00">';
			body += '<input type="hidden" name="discounttype" value="">';
			body += '<input type="hidden" name="itemcost" value="">';
			body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.TAX_TYPE+'">';
			body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+data.TAX_TYPE+'">';
			if(deductInv == "1" && origin == "manual"){
			}else{
				body += '<input type="hidden" name="uom" class="form-control" value="'+data.UOM+'" readonly="readonly">';
			}
			if(data.BILL_STATUS !="Draft"){
				body += '<input type="hidden" name="ORDQTY" value="'+data.QTY+'" readonly="readonly">';
			}
			body += '<input type="hidden" name="LNNO" value="'+data.LNNO+'" readonly="readonly">';
			body += '<input type="hidden" name="IS_COGS_SET" value="'+data.IS_COGS_SET+'">';
			body += '</td>';
			body += '<td class="bill-item">';
			if(tono == ""){
			body += '<input type="text" name="item" style="width:87%" class="form-control itemSearch" onchange="checkitems(this.value,this)" value="'+data.ITEM+'" onchange="calculateAmount(this)">';
			body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
			body += '<input type="hidden" name="edit_item" class="form-control" value="'+data.ITEM+'">';
			}
			else
				{
				body += '<input type="text" name="item" class="form-control" onchange="checkitems(this.value,this)" value="'+data.ITEM+'" readonly>';
				body += '<input type="hidden" name="edit_item" class="form-control" onchange="checkitems(this.value,this)" value="'+data.ITEM+'">';
				}
			body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly data-pd="'+ITEMDESC+'" value="'+ITEMDESC+'">';
			body += '</td>';
			body += '<td class="">';
			body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Account" value="'+data.ACCOUNT_NAME+'">';
			body += '</td>';
			if(deductInv == "1" && origin == "manual"){
				var readOnly='readonly';
				if(data.BILL_STATUS =="Draft"){
					readOnly='';
				}
			
			body += '<td class="">';
			body += '<input type="text" name="uom" class="form-control uomSearch" onchange="checkprduom(this.value,this)" value="'+data.UOM+'"  '+readOnly+'>';
			body += '</td>';
			body += '<td class="">';
			body += '<input type="text" name="loc" class="form-control locSearch" onchange="checkprdloc(this.value,this)" value="'+data.LOC+'" '+readOnly+'>';
			body += '</td>';
			body += '<td class="">';
			body += '<input type="text" name="batch" class="form-control batchSearch" value="'+data.BATCH+'" '+readOnly+'>';
			body += '</td>';
			}
			body += '<td class="item-qty text-right">';
			body += '<input type="text" name="qty" '+setreadonly+' data-rl="'+data.stkqty+'" data-msq="'+data.maxstkqty+'" data-soh="'+data.stockonhand+'" data-eq="'+data.EstQty+'" data-aq="'+data.AvlbQty+'" class="form-control text-right" value="'+parseFloat(data.QTY).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			body += '<input type="hidden" name="edit_qty" class="form-control" value="'+data.QTY+'">';
			body += '</td>';
			body += '<td>';
			body += '<input type="text" name="unitcost" class="form-control text-right" value="'+data.UCOST+'" readonly>';
			body += '</td>';
			body += '<td>';
			body += '<input type="text" name="addonshow" class="form-control text-right" value="'+data.AOD+'" readonly>';
			//body += '<input type="hidden" name="addonprice" value="'+szero+'">';
			body += '<input type="hidden" name="addonprice" value="'+data.CPPI+'">';
			//body += '<input type="hidden" name="addontype" value="'+curency+'">';
			body += '<input type="hidden" name="addontype" value="'+data.DETADDTYPE+'">';
			body += '</td>';
			body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+uprice+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
			//		IMTHIYAS Modified on 02.03.2022
			if(educations == 'Education'){
			body += '<td class="item-discount text-right" disabled>';
			body += '<div class="row"><div class=" col-lg-12 col-sm-3 col-12"><div class="input-group my-group" style="width:120px;">';
			/*body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEM_DISCOUNT+'" value="0.00" readonly="readonly">';*/
			/*body += '<input name="item_discounttype" type="text" class="form-control text-right" value="%" hidden>';*/
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<input name="item_discount" type="text" class="form-control text-right" disabled value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			}else{
				body += '<input name="item_discount" type="text" class="form-control text-right" disabled value="'+parseFloat(data.ITEM_DISCOUNT*data.CURRENCYUSEQT).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			}
			body += '<select name="item_discounttype" disabled class="discountPicker form-control item_discount_type" onchange="calculateAmount(this)">';
			//body += '<option value="'+data.ITEM_DISCOUNT_TYPE+'">'+data.ITEM_DISCOUNT_TYPE+'</option>';		
			
			if(data.ITEM_DISCOUNT_TYPE == data.CURRENCYID){
				body += "<option selected value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
			}else{
				body += "<option value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
			}
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<option selected >%</option>';		
			}else{
				body += '<option>%</option>';		
			}
			body += '</select>';
			body += '</div></div></div>';
			body += '</td>';
			}else{
				body += '<td class="item-discount text-right">';
			body += '<div class="row"><div class=" col-lg-12 col-sm-3 col-12"><div class="input-group my-group" style="width:120px;">';
			/*body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEM_DISCOUNT+'" value="0.00" readonly="readonly">';*/
			/*body += '<input name="item_discounttype" type="text" class="form-control text-right" value="%" hidden>';*/
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			}else{
				body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT*data.CURRENCYUSEQT).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			}
			body += '<select name="item_discounttype" class="discountPicker form-control item_discount_type" onchange="calculateAmount(this)">';
			//body += '<option value="'+data.ITEM_DISCOUNT_TYPE+'">'+data.ITEM_DISCOUNT_TYPE+'</option>';		
			
			if(data.ITEM_DISCOUNT_TYPE == data.CURRENCYID){
				body += "<option selected value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
			}else{
				body += "<option value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
			}
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<option selected >%</option>';		
			}else{
				body += '<option>%</option>';		
			}
			body += '</select>';
			body += '</div></div></div>';
			body += '</td>';
			}
			
			
			/*body += '<td class="item-tax">';
			body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';*/			
			//body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" >';
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

					if(data.TAX_TYPE == "" || data.TAX_TYPE == null){
						body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax">';
					}else{
						body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" placeholder="Select a Tax">';
					}
					
					
					}*/
			/*body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly="readonly" placeholder="Select a Tax">';
			body += '</td>';*/
			body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
			body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" style="display:inline-block;"  readonly="readonly">';
			body += '<td class="table-icon" style="position:relative;">';
			if(echeck != "0"){
				body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
			}
			body += '<a href="#" onclick="showRemarksDetails(this)">';
			body += '<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>';
			body += '</a>';
			if(tono == ""){
			body += '<span class="glyphicon glyphicon-remove-circle bill-action" style="left: 35px;"aria-hidden="true"></span>';
			}
			body += '</td>';
			body += '</td>';
			body += '</tr>';
						}
				
		

		
		setLineNo();
		var amount = amt;		
		/*discountValue = parseFloat(amount*(percentage[1]/100)).toFixed(numberOfDecimal);
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
		//document.getElementById('TAXTREATMENT').innerHTML=data.TAXTREATMENT;
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
		disc = data.DISCOUNT;
		if(data.DISCOUNT_TYPE != "%"){
			disc=parseFloat(data.DISCOUNT*data.CURRENCYUSEQT).toFixed(numberOfDecimal);	
		}
		$("input[name ='discount']").val(disc);
		$("select[name ='discount_type']").val(data.DISCOUNT_TYPE);
		$("input[name ='discount_account']").val(data.DISCOUNT_ACCOUNT);
		$("input[name ='adjustment']").val(parseFloat(data.ADJUSTMENT*data.CURRENCYUSEQT).toFixed(numberOfDecimal));
		$("input[name ='SALES_LOC']").val(data.STATE_PREFIX);
		$("select[name ='SALES_LOC']").val(data.STATE_PREFIX);
		$("input[name ='STATE_PREFIX']").val(data.STATE_PREFIX);
		$('#SALES_LOC').attr('disabled',true); 		
 		$('#SALES_LOC').css("background-color", "#EEEEEE");
 		$("input[name ='shippingcost']").val(parseFloat(data.SHIPPINGCOST*data.CURRENCYUSEQT).toFixed(numberOfDecimal));	
		$("input[name ='oShippingcost']").val(parseFloat(data.SHIPPINGCOST*data.CURRENCYUSEQT).toFixed(numberOfDecimal));
		$("input[name ='orderdiscount']").val(data.ORDER_DISCOUNT);
 		
		$("input[name ='CURRENCYID']").val(data.CURRENCYID);
		$("input[name ='CURRENCY']").val(data.DISPLAY);
		$("input[name ='CURRENCYUSEQT']").val(data.CURRENCYUSEQT);
		var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
		document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+data.CURRENCYID+")";
		document.getElementById('AODTYPE').innerHTML = "Add On (% / "+data.CURRENCYID+")";
		document.getElementById('lbltotal').innerHTML = "Total ("+data.CURRENCYID+")"; //  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency

		if(basecurrency!=data.CURRENCYID)
			document.getElementById('showtotalcur').style.display = 'block';
		else
			document.getElementById('showtotalcur').style.display = 'none';
		$('#CURRENCY').attr('disabled',true); 		
		$('#CURRENCY').css("background-color", "#eeeeee");
		
		$("input[name ='CURRENCYUSEQTOLD']").val(data.CURRENCYUSEQT);
		$("input[name ='shiptaxstatus']").val(data.ISSHIPPINGTAX);
		$("input[name ='odiscounttaxstatus']").val(data.ISORDERDISCOUNTTAX);
		$("input[name ='discounttaxstatus']").val(data.ISDISCOUNTTAX);
		$("input[name ='ptaxtype']").val(data.FINTAXTYPE);
		$("input[name ='ptaxpercentage']").val(data.OUTBOUD_GST);
		$("input[name ='ptaxdisplay']").val(data.TAX_TYPE);
		$("input[name ='Salestax']").val(data.TAX_TYPE);
		$("input[name ='ptaxiszero']").val(data.FINTAXISZERO);
		$("input[name ='ptaxisshow']").val(data.FINTAXSHOW);
		$("input[name ='taxid']").val(data.TAXID);
		$("input[name ='GST']").val(data.OUTBOUD_GST);
		$("input[name ='aorderdiscount']").val(data.ORDER_DISCOUNT);
		$("input[name ='project']").val(data.PROJECTNAME);
		$("input[name ='PROJECTID']").val(data.PROJECTID);
		$("input[name ='transport']").val(data.TRANSPORTNAME);
		$("input[name ='TRANSPORTID']").val(data.TRANSPORTID);
		$("input[name ='SHIPPINGID']").val(data.SHIPPINGID);
		$("input[name ='SHIPPINGCUSTOMER']").val(data.SHIPPINGCUSTOMER);
		$("input[name ='billstatus']").val(data.BILL_STATUS);
		$("input[name=DOBYEAR]").val(data.DOB);
		$("input[name=NATIONAL]").val(data.NATIONAL);
		
		if(data.ORDERDISCOUNTTYPE == "%"){
			$('#oddiscount_type').empty();
			$('#oddiscount_type').append('<option value="' + data.CURRENCYID + '">' + data.CURRENCYID + '</option>');
			$('#oddiscount_type').append('<option selected value="%">%</option>');
		}else{
			$('#oddiscount_type').empty();
			$('#oddiscount_type').append('<option selected value="' + data.CURRENCYID + '">' + data.CURRENCYID + '</option>');
			$('#oddiscount_type').append('<option value="%">%</option>');
		}

		if(data.DISCOUNT_TYPE == "%"){
			$('#discount_type').empty();
			$('#discount_type').append('<option value="' + data.CURRENCYID + '">' + data.CURRENCYID + '</option>');
			$('#discount_type').append('<option selected value="%">%</option>');
		}else{
			$('#discount_type').empty();
			$('#discount_type').append('<option selected value="' + data.CURRENCYID + '">' + data.CURRENCYID + '</option>');
			$('#discount_type').append('<option value="%">%</option>');
		}
		
		if (data.ISSHIPPINGTAX == "1"){
			 $('input[name ="shiptaxstatus"]').val("1");
			 $("#shtax").html("");
			 $("#shtax").html("(Tax Inclusive)");
			 $('.isshiptax').prop('checked', true); 
		 }else{
			 $('input[name ="shiptaxstatus"]').val("0");
			 $("#shtax").html("");
			 $("#shtax").html("(Tax Exclusive)");
			 $('.isshiptax').prop('checked', false); 
		 }
		 
		 if (data.ISORDERDISCOUNTTAX == "1"){
			 $('input[name ="odiscounttaxstatus"]').val("1");
			 $("#odtax").html("");
			 $("#odtax").html("(Tax Inclusive)");
			 $('.isodisctax').prop('checked', true); 
		 }else{
			 $('input[name ="odiscounttaxstatus"]').val("0");
			 $("#odtax").html("");
			 $("#odtax").html("(Tax Exclusive)");
			 $('.isodisctax').prop('checked', false); 
		 }
		 
		 if (data.ISDISCOUNTTAX == "1"){
			 $('input[name ="discounttaxstatus"]').val("1");
			 $("#dtax").html("");
			 $("#dtax").html("(Tax Inclusive)");
			 $('.isdtax').prop('checked', true); 
		 }else{
			 $('input[name ="discounttaxstatus"]').val("0");
			 $("#dtax").html("");
			 $("#dtax").html("(Tax Exclusive)");
			 $('.isdtax').prop('checked', false); 
		 }
	});
	$(".bill-table tbody").html(body);
	//renderTaxDetails();
	calculateTotal();
	if(echeck == 1){
		//$('.item_discount_type').prop('disabled',true);
		//$('#discount_type').prop('disabled',true);
		//$(".item_discount").attr("readonly", true);
		//$("#dediscount").attr("readonly", true);
	}else{
		$('#SALES_LOC').prop('disabled',false);
		$('#SALES_LOC').css('background-color','white');
	}
	removeSuggestionToTable();
	addSuggestionToTable();
	calltooltip();
	
	
	
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
	 var urlStrAttach = "/track/InvoiceServlet?ACTION=downloadAttachmentById&attachid="+id;
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
	var urlStrAttach = "/track/InvoiceServlet?ACTION=removeAttachmentById&removeid="+id;	
	$.ajax( {
		type : "POST",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
}
function OnChange(dropdown)
{
	var cmd = document.form1.cmd.value;
    var myindex  = dropdown[1].selectedIndex;
    var SelValue = dropdown[1].options[myindex].value;
    document.form1.STATE_PREFIX.value = SelValue;
    $("input[name ='SALES_LOC']").val(SelValue);
    $(".taxSearch").typeahead('val', '"');
	$(".taxSearch").typeahead('val', '');
	/*if(cmd!="Edit"){
		$(".bill-table tbody").html("");
		getOrderDetailsForInvoice(document.form1.gino.value);
	}else{
		changetax();
		
	}*/
	changetax();

}
function getcustname(TAXTREATMENT,TRANSPORTNAME,TRANSPORTID,CUSTNO,CURRENCY,CURRENCYID,CURRENCYUSEQT,DOB,NATIONALITY){
//document.getElementById(TAXTREATMENT).innerHTML = TAXTREATMENT;
$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
$("input[name=transport]").val(TRANSPORTNAME);
$("input[name=TRANSPORTID]").val(TRANSPORTID);
document.form1.CUST_CODE.value = CUSTNO;
//		IMTHIYAS Modified on 02.03.2022
var education = $("input[name='EDUCATION']").val();
if(education != "Education"){
getCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT)
}
if(education == "Education"){
getDobNationality(DOB,NATIONALITY)
}
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
	
	var currentShippingCost = parseFloat($("input[name ='shippingcost']").val());
	var originalShippingCost = parseFloat($("input[name ='oShippingcost']").val());
	var dono = $("input[name ='ORDERNO']").val();
	var cmd = document.form1.cmd.value;
	if(cmd != "copy"){
	if(currentShippingCost > originalShippingCost){
		if(dono.length > 0){
			alert("Cannot exceed original shipping cost of "+originalShippingCost);
			$("input[name ='shippingcost']").val(originalShippingCost);
		}
	}
	}
	calculateTotal();
	//renderTaxDetails();
});

$(document).on("focusout","input[name ='orderdiscount']",function(){
	
	var currentdiscCost = parseFloat($("input[name ='orderdiscount']").val());
	var originaldiscCost = parseFloat($("input[name ='aorderdiscount']").val());
	var cmd = document.form1.cmd.value;
	if(cmd != "copy"){
	if(currentdiscCost > originaldiscCost){
		alert("Cannot exceed original order discount cost of "+originaldiscCost);
		$("input[name ='orderdiscount']").val(originaldiscCost);
	}
	}
	calculateTotal();
	//renderTaxDetails();
});

function calculateTaxforsatate(obj, types, percentage, display){
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

function changetaxnew(){
	
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
				var cgettaxtype = v.SGSTTYPES;
				var gettaxtype=cgettaxtype.substr(0, cgettaxtype.indexOf('(')); 
				if(gettaxtype == taxtype){	
					pertax = v.DISPLAY;
					SGSTTYPES=v.SGSTTYPES;
					SGSTPERCENTAGE=v.SGSTPERCENTAGE;
					DISPLAY=v.DISPLAY;
				}
			});
			
				$('td:eq(5)', this).find('input[name="tax_type"]').val(pertax);
				$('td:eq(5)', this).find('input[name="tax"]').val(pertax)
				if(SGSTTYPES != "0"){
					var obj1 = $('td:eq(5)', this);
					calculateTaxforsatate(obj1,SGSTTYPES,SGSTPERCENTAGE,DISPLAY);
				}
		});
		renderTaxDetails();
		
	}
	});
}


function getCopyInvoiceDetail(TranId){
	var plant = document.form1.plant.value;
	var CUR = document.form1.BASECURRENCYID.value;
	$.ajax({
		type : "POST",
		url : "/track/InvoiceServlet",
		async : true,
		data : {
			PLANT : plant,
			CUR : CUR,
			Submit : "GET_EDIT_INVOICE_DETAILS",
			Id : TranId
		},
		dataType : "json",
		success : function(data) {
			loadcopyTable(data.orders);
		}
	});
}

function loadcopyTable(orders){
	var curency = document.form1.curency.value;
	var deductInv = document.form1.DEDUCT_INV.value;
	var origin = document.form1.ORIGIN.value;
	var educations = document.form1.EDUCATION.value;
	var echeck = "0";
	var body="";
	taxList = [];
	
	$.each(orders, function( key, data ) {
		var numberOfDecimal = $("#numberOfDecimal").val();
		/*var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");
		var numberOfDecimal = $("#numberOfDecimal").val();
		tax.types = percentage[0].trim();
		tax.percentage = percentage[1];
		tax.display = data.TAX_TYPE;
		var taxpercentage=percentage[1];
		var taxtype=percentage[0].trim();
		var loadtype=taxtype.substr(0, taxtype.indexOf('('));*/ 
		var uprice= parseFloat((data.UNITPRICE*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
		var amt= parseFloat((data.AMOUNT*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
		var disc= "0";
		var ITEMDESC = escapeHtml(data.ITEMDESC);
		/*if(loadtype=="ZERO RATE")
			{
				zerotype++;
			}*/
		
			body += '<tr>';
			body += '<td class="item-img text-center">';
			//body += '<input type="hidden" name="basecost" value="'+data.BASECOST+'">';
			if(data.UNITPRICE=='' || data.UNITPRICE=='0' ||data.UNITPRICE=='0.00'||data.UNITPRICE==undefined){
				body += '<input type="hidden" name="basecost" value="'+data.BASECOST+'">';
			}else{
				body += '<input type="hidden" name="basecost" value="'+data.UNITPRICE+'">';
			}
			body += '<input type="hidden" name="lnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="dolnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="ORDQTY" value="0.0">';
			body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"> <img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
			body += '<input type="hidden" name="minsp" value="'+data.minSellingConvertedUnitCost+'">';
			body += '<input type="hidden" name="customerdiscount" value="'+data.outgoingOBDiscount+'">';
			body += '<input type="hidden" name="outgoingqty" value="'+data.outgoingqty+'">';
			body += '<input type="hidden" name="itemprice" value="0.00">';
			body += '<input type="hidden" name="discounttype" value="">';
			body += '<input type="hidden" name="itemcost" value="">';
			body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.TAX_TYPE+'">';
			body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+data.TAX_TYPE+'">';
			if(deductInv == "1" && origin == "manual"){
			}else{
				body += '<input type="hidden" name="uom" class="form-control" onchange="checkprduom(this.value,this)" value="'+data.UOM+'" readonly="readonly">';
			}
			body += '</td>';
			body += '<td class="bill-item">';
			body += '<input type="text" name="item" style="width:87%" class="form-control itemSearch" onchange="checkitems(this.value,this)" value="'+data.ITEM+'" onchange="calculateAmount(this)">';
			body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
			body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly  data-pd="'+ITEMDESC+'" value="'+ITEMDESC+'">';
			body += '</td>';
			body += '<td class="">';
			body += '<input type="text" name="account_name" class="form-control" value="'+data.ACCOUNT_NAME+'" readonly="readonly">';
			body += '</td>';
			if(deductInv == "1" && origin == "manual"){
				body += '<td class="">';
				body += '<input type="text" name="uom" class="form-control" onchange="checkprduom(this.value,this)" value="'+data.UOM+'" readonly="readonly">';
				body += '</td>';
				body += '<td class="">';
				body += '<input type="text" name="loc" class="form-control" onchange="checkprdloc(this.value,this)" value="'+data.LOC+'" readonly="readonly">';
				body += '</td>';
				body += '<td class="">';
				body += '<input type="text" name="batch" class="form-control" value="'+data.BATCH+'" readonly="readonly">';
				body += '</td>';
			}
			body += '<td class="item-qty text-right"><input type="text" name="qty" data-rl="'+data.stkqty+'" data-msq="'+data.maxstkqty+'" data-soh="'+data.stockonhand+'" data-eq="'+data.EstQty+'" data-aq="'+data.AvlbQty+'" class="form-control text-right" value="'+parseFloat(data.QTY).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
			body += '<td>';
			body += '<input type="text" name="unitcost" class="form-control text-right" value="'+data.UCOST+'" readonly>';
			body += '</td>';
			body += '<td>';
			body += '<input type="text" name="addonshow" class="form-control text-right" value="'+data.AOD+'" readonly>';
			//body += '<input type="hidden" name="addonprice" value="'+szero+'">';
			//body += '<input type="hidden" name="addonprice" value="'+data.CPPI+'">';
			body += '<input type="hidden" name="addonprice" value="'+data.CPPI+'">';
			//body += '<input type="hidden" name="addontype" value="'+curency+'">';
			body += '<input type="hidden" name="addontype" value="'+data.DETADDTYPE+'">';
			body += '</td>';
			body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+uprice+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
			//		IMTHIYAS Modified on 02.03.2022
			if(educations == 'Education'){
			body += '<td class="item-discount text-right" disabled>';
			body += '<div class="row"><div class=" col-lg-12 col-sm-3 col-12"><div class="input-group my-group" style="width:120px;">';
			/*body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEM_DISCOUNT+'" value="0.00" readonly="readonly">';*/
			/*body += '<input name="item_discounttype" type="text" class="form-control text-right" value="%" hidden>';*/
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<input name="item_discount" type="text" class="form-control text-right" disabled value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			}else{
				body += '<input name="item_discount" type="text" class="form-control text-right" disabled value="'+parseFloat(data.ITEM_DISCOUNT*data.CURRENCYUSEQT).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			}
			body += '<select name="item_discounttype" class="discountPicker form-control item_discount_type" disabled onchange="calculateAmount(this)">';
			//body += '<option value="'+data.ITEM_DISCOUNT_TYPE+'">'+data.ITEM_DISCOUNT_TYPE+'</option>';		
			
			if(data.ITEM_DISCOUNT_TYPE == data.CURRENCYID){
				body += "<option selected value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
			}else{
				body += "<option value="+data.CURRENCYID+">"+data.DISPLAY+"</option>";
			}
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<option selected >%</option>';		
			}else{
				body += '<option>%</option>';		
			}
			body += '</select>';
			body += '</div></div></div>';
			body += '</td>';
			}else{
			body += '<td class="item-discount text-right">';
			body += '<div class="row"><div class=" col-lg-12 col-sm-3 col-12"><div class="input-group my-group" style="width:120px;">';
			/*body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEM_DISCOUNT+'" value="0.00" readonly="readonly">';*/
			/*body += '<input name="item_discounttype" type="text" class="form-control text-right" value="%" hidden>';*/
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT).toFixed(3)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			}else{
				body += '<input name="item_discount" type="text" class="form-control text-right" value="'+parseFloat(data.ITEM_DISCOUNT*data.CURRENCYUSEQT).toFixed(numberOfDecimal)+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
			}
			body += '<select name="item_discounttype" class="discountPicker form-control item_discount_type" onchange="calculateAmount(this)">';
			//body += '<option value="'+data.ITEM_DISCOUNT_TYPE+'">'+data.ITEM_DISCOUNT_TYPE+'</option>';		
			
			if(data.ITEM_DISCOUNT_TYPE == data.CURRENCYID){
				body += "<option selected value="+data.CURRENCYID+">"+data.CURRENCYID+"</option>";
			}else{
				body += "<option value="+data.CURRENCYID+">"+data.DISPLAY+"</option>";
			}
			if(data.ITEM_DISCOUNT_TYPE == "%"){
				body += '<option selected >%</option>';		
			}else{
				body += '<option>%</option>';		
			}
			body += '</select>';
			body += '</div></div></div>';
			body += '</td>';
			}
			/*body += '<td class="item-tax">';
			body += '<input type="hidden" name="tax_type" value="'+data.TAX_TYPE+'" class="form-control">';			
			body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" readonly>';*/
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

					if(data.TAX_TYPE == "" || data.TAX_TYPE == null){
						body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax">';
					}else{
						body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAX_TYPE+'" placeholder="Select a Tax">';
					}
					
					
					}*/
			/*body += '</td>';*/
			body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
			body += '<input name="amount" type="text" class="form-control text-right" value="'+amt+'" readonly="readonly" style="display:inline-block;"></td>';
			body += '<td class="table-icon" style="position:relative;">';
			body += '<a href="#" onclick="showRemarksDetails(this)">';
			body += '<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>';
			body += '</a>';
			body += '<span class="glyphicon glyphicon-remove-circle bill-action" style="left: 35px;"aria-hidden="true"></span>';
			body += '</td>';
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
		if(document.form1.CUST_CODE2.value==""||document.form1.CUST_CODE2.value==null)
		{
		$("input[name ='CUST_CODE']").val(data.CUST_CODE);
		$("input[name ='CUSTOMER']").val(data.CUSTOMER);
		$("input[name ='CUSTOMER1']").val(data.CUSTOMER);
		$('select[name ="nTAXTREATMENT"]').val(data.TAXTREATMENT);
		}
		$("input[name ='EMPNO']").val(data.EMPNO);
		$("input[name ='EMP_NAME']").val(data.EMP_NAME);		
		$("input[name ='due_date']").val(data.DUE_DATE);		
		$("input[name ='payment_terms']").val(data.PAYMENT_TERMS);
		$("textarea[name ='terms']").val(data.TERMSCONDITIONS);
		$("textarea[name ='note']").val(data.NOTE);
		$("select[name ='item_rates']").val(data.ITEM_RATES);
		disc = data.DISCOUNT;
		if(data.DISCOUNT_TYPE != "%"){
			disc=parseFloat(data.DISCOUNT*data.CURRENCYUSEQT).toFixed(numberOfDecimal);	
		}
		$("input[name ='discount']").val(disc);
		$("select[name ='discount_type']").val(data.DISCOUNT_TYPE);
		//$("input[name ='invoice_date']").val(data.INVOICE_DATE);
		$("input[name ='discount_account']").val(data.DISCOUNT_ACCOUNT);
		$("input[name ='adjustment']").val(parseFloat(data.ADJUSTMENT*data.CURRENCYUSEQT).toFixed(numberOfDecimal));
		$("input[name ='SALES_LOC']").val(data.STATE_PREFIX);
		$("select[name ='SALES_LOC']").val(data.STATE_PREFIX);
		$("input[name ='STATE_PREFIX']").val(data.STATE_PREFIX);
		$('#SALES_LOC').attr('disabled',true); 		
 		$('#SALES_LOC').css("background-color", "#EEEEEE");
 		$("input[name ='shippingcost']").val(parseFloat("0.0").toFixed(numberOfDecimal));	
		$("input[name ='oShippingcost']").val(parseFloat(data.SHIPPINGCOST*data.CURRENCYUSEQT).toFixed(numberOfDecimal));
		$("input[name ='orderdiscount']").val(parseFloat("0.0").toFixed(3));
		
		$("input[name ='CURRENCYID']").val(data.CURRENCYID);
		$("input[name ='CURRENCY']").val(data.DISPLAY);
		$("input[name ='CURRENCYUSEQT']").val(data.CURRENCYUSEQT);
		var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
		document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+data.CURRENCYID+")";
		document.getElementById('AODTYPE').innerHTML = "Add On (% / "+data.CURRENCYID+")";
		document.getElementById('lbltotal').innerHTML = "Total ("+data.CURRENCYID+")"; //  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency

		if(basecurrency!=data.CURRENCYID)
			document.getElementById('showtotalcur').style.display = 'block';
		else
			document.getElementById('showtotalcur').style.display = 'none';
		
//		$('#CURRENCY').css("background-color", "#eeeeee");
 		
	/*	$('#discount_type').empty();
		$('#discount_type').append('<option value="' + data.CURRENCYID + '">' + data.DISPLAY + '</option>');
		$('#discount_type').append('<option value="%">%</option>');*/
		
		/*if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNote").html(data.ATTACHNOTE_COUNT +" files attached");*/
		
		$("input[name ='CURRENCYUSEQTOLD']").val(data.CURRENCYUSEQT);
		$("input[name ='shiptaxstatus']").val(data.ISSHIPPINGTAX);
		$("input[name ='odiscounttaxstatus']").val(data.ISORDERDISCOUNTTAX);
		$("input[name ='discounttaxstatus']").val(data.ISDISCOUNTTAX);
		$("input[name ='ptaxtype']").val(data.FINTAXTYPE);
		$("input[name ='ptaxpercentage']").val(data.OUTBOUD_GST);
		$("input[name ='ptaxdisplay']").val(data.TAX_TYPE);
		$("input[name ='Salestax']").val(data.TAX_TYPE);
		$("input[name ='ptaxiszero']").val(data.FINTAXISZERO);
		$("input[name ='ptaxisshow']").val(data.FINTAXSHOW);
		$("input[name ='taxid']").val(data.TAXID);
		$("input[name ='GST']").val(data.OUTBOUD_GST);
		//$("input[name ='aorderdiscount']").val(data.ORDER_DISCOUNT);
		$("input[name ='project']").val(data.PROJECTNAME);
		$("input[name ='PROJECTID']").val(data.PROJECTID);
		$("input[name ='transport']").val(data.TRANSPORTNAME);
		$("input[name ='TRANSPORTID']").val(data.TRANSPORTID);
		$("input[name ='SHIPPINGID']").val(data.SHIPPINGID);
		$("input[name ='SHIPPINGCUSTOMER']").val(data.SHIPPINGCUSTOMER);
		$("input[name=DOBYEAR]").val(data.DOB);
		$("input[name=NATIONAL]").val(data.NATIONAL);
		
		
		/*if(data.ORDERDISCOUNTTYPE == "%"){
			$('#oddiscount_type').empty();
			$('#oddiscount_type').append('<option value="' + data.CURRENCYID + '">' + data.DISPLAY + '</option>');
			$('#oddiscount_type').append('<option selected value="%">%</option>');
		}else{
			$('#oddiscount_type').empty();
			$('#oddiscount_type').append('<option selected value="' + data.CURRENCYID + '">' + data.DISPLAY + '</option>');
			$('#oddiscount_type').append('<option value="%">%</option>');
		}*/

		/*
		$('#discount_type').empty();
		$('#discount_type').append('<option value="' + data.CURRENCYID + '">' + data.CURRENCYID + '</option>');
		$('#discount_type').append('<option value="%">%</option>');*/
		
		if(data.DISCOUNT_TYPE == "%"){
			$('#discount_type').empty();
			$('#discount_type').append('<option value="' + data.CURRENCYID + '">' + data.CURRENCYID + '</option>');
			$('#discount_type').append('<option selected value="%">%</option>');
		}else{
			$('#discount_type').empty();
			$('#discount_type').append('<option selected value="' + data.CURRENCYID + '">' + data.CURRENCYID + '</option>');
			$('#discount_type').append('<option value="%">%</option>');
		}
		
		if (data.ISSHIPPINGTAX == "1"){
			 $('input[name ="shiptaxstatus"]').val("1");
			 $("#shtax").html("");
			 $("#shtax").html("(Tax Inclusive)");
			 $('.isshiptax').prop('checked', true); 
		 }else{
			 $('input[name ="shiptaxstatus"]').val("0");
			 $("#shtax").html("");
			 $("#shtax").html("(Tax Exclusive)");
			 $('.isshiptax').prop('checked', false); 
		 }
		 
		 if (data.ISORDERDISCOUNTTAX == "1"){
			 $('input[name ="odiscounttaxstatus"]').val("1");
			 $("#odtax").html("");
			 $("#odtax").html("(Tax Inclusive)");
			 $('.isodisctax').prop('checked', true); 
		 }else{
			 $('input[name ="odiscounttaxstatus"]').val("0");
			 $("#odtax").html("");
			 $("#odtax").html("(Tax Exclusive)");
			 $('.isodisctax').prop('checked', false); 
		 }
		 
		 if (data.ISDISCOUNTTAX == "1"){
			 $('input[name ="discounttaxstatus"]').val("1");
			 $("#dtax").html("");
			 $("#dtax").html("(Tax Inclusive)");
			 $('.isdtax').prop('checked', true); 
		 }else{
			 $('input[name ="discounttaxstatus"]').val("0");
			 $("#dtax").html("");
			 $("#dtax").html("(Tax Exclusive)");
			 $('.isdtax').prop('checked', false); 
		 }
	});
	$(".bill-table tbody").html(body);
	//renderTaxDetails();
	calculateTotal();
	removeSuggestionToTable();
	addSuggestionToTable();
	calltooltip();
	
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

/*function setCURRENCYUSEQT(CURRENCYUSEQTCost){

	var cmd = $("#cmd").val();
	var numberOfDecimal = $("#numberOfDecimal").val();
	var plant = document.form1.plant.value;
	var cost = 0;
	var ischange=0;
	if(cmd=="IssueingGoodsInvoice"){
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
		var qty = parseFloat($(this).find('input[name=qty]').val()).toFixed(3);
		cost = parseFloat($(this).find("td:nth-child(1)").find('input[name=basecost]').val()) *parseFloat(CURRENCYUSEQTCost);
		cost = parseFloat(cost).toFixed(numberOfDecimal);
		$(this).find('input[name=cost]').val(cost);
		
		var ordQty = parseFloat($(this).find("td:nth-child(1)").find('input[name=ORDQTY]').val());
		if(!isNaN(ordQty)){
			if(qty<ordQty){
				alert("Quantity cannot be less than "+ordQty);
				$(this).find('input[name=qty]').val(parseFloat(ordQty).toFixed(3));
				return false;
			}
		}
		
		var itemDiscount = parseFloat($(this).find('input[name=item_discount]').val()).toFixed(numberOfDecimal);
		var discounType = $(this).find('select[name=item_discounttype]').val();
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
	
	changetaxfordiscountnew();
	});
}
else
	{
	$('tr').each(function () {
		
	var qty = parseFloat($(this).find("td:nth-child(3)").find('input').val()).toFixed(3);
	var costcheck = parseFloat($(this).find("td:nth-child(4)").find('input').val()).toFixed(numberOfDecimal);
	var cost=0;
	if(cost <= costcheck){
		cost=costcheck;
	}else{
		//alert("Unit Price should be greater than the Order price");
		cost=parseFloat(ocost).toFixed(numberOfDecimal);
		
	}
	var itemDiscount = parseFloat($(this).find("td:nth-child(5)").find('input').val()).toFixed(numberOfDecimal);
	var discounType = $(this).find("td:nth-child(5)").find('select').val();
	
	var itemDiscountval=itemDiscount;
	 if(discounType == "%"){
		 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
	 }
	 
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
	
	$(this).find("td:nth-child(3)").find('input').val(qty);
	$(this).find("td:nth-child(4)").find('input').val(cost);
	
	$(this).find("td:nth-child(7)").find('input').val(amount);
		
		amount = parseFloat($(this).find("td:nth-child(1)").find('input[name=basecost]').val()) *parseFloat(CURRENCYUSEQTCost);
		$(this).find("td:nth-child(1)").find('input[name=convcost]').val(amount);
		amount = parseFloat(amount).toFixed(numberOfDecimal);
		$(this).find("td:nth-child(5)").find('input').val(amount);
		
		var qtyamount = parseFloat($(this).find("td:nth-child(1)").find('input[name=basecost]').val()) *parseFloat(CURRENCYUSEQTCost) ;
		amount = parseFloat($(this).find("td:nth-child(4)").find('input').val()) *parseFloat(qtyamount);
		amount = parseFloat(amount).toFixed(numberOfDecimal);
		$(this).find("td:nth-child(8)").find('input').val(amount);
	
	});
}
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
	var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
	document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";
	document.getElementById('AODTYPE').innerHTML = "Add On (% / "+CURRENCYID+")";
	document.getElementById('lbltotal').innerHTML = "Total ("+CURRENCYID+")"; //  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency

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
	var dono = $("input[name=ORDERNO]").val();
	var item = $(obj).closest('tr').find("input[name=item]").val();

	if(item != ''){
		$.ajax({
			type : "GET",
			url : "../invoice/getSalesOrderRemarks",
			data : {
				ITEM: item,
				INVOICE: invoice,
				DONO: dono,
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
				body += '<input type="hidden" name="r_dnno" value="'+dono+'">';
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

/*function calculateAmount(obj){

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
}*/

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
	$(".bill-table tbody tr td:last-child").prev().each(function() {
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

	 if(ptaxiszero == "1"){
		 taxTotal = "0";
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
	 var CURRENCYUSEQT = $('input[name ="CURRENCYUSEQT"]').val();//Author: Azees  Create date: July 17,2021  Description:  Total of Local Currency
	 var convttotalvalue= (totalvalue/CURRENCYUSEQT).toFixed(numberOfDecimal)
	 $("#totalcur").html(convttotalvalue);
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
	var customer = $("input[name=CUSTOMER]").val();
	var replacePreviousInvoiceCost = $("input[name=replacePreviousInvoiceCost]").val();
	if(replacePreviousInvoiceCost=="1") {
	if(customer == ""){
		alert("Please select a Customer.");
		return false;
	}
	}
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
	
	//imthi added to change COST and ADDONCOST by changing currency
		Ucost = ((parseFloat($(this).find('input[name=unitcost]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
		Ucost = parseFloat(Ucost).toFixed(numberOfDecimal);
		$(this).find('input[name=unitcost]').val(Ucost);
		
		
		aodType = $(this).find('input[name=addontype]').val();
		aodcost = ((parseFloat($(this).find('input[name=addonshow]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
		//aodcost = parseFloat(aodcost).toFixed(numberOfDecimal);
		
		if(aodType == "%"){
		
		}else{
			aodcost = parseFloat(aodcost).toFixed(numberOfDecimal)+" "+CURRENCYID;
			$(this).find('input[name=addonshow]').val(aodcost);
		}
	//imthi end
	
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
//	$(obj).closest('tr').find("input[name=qty]").tooltip("destroy");
	var content = "<p class='text-left'>Reorder Level: "+$(obj).closest('tr').find("input[name=qty]").data("rl")+"</p>";
	content += "<p class='text-left'>Max Stock Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("msq")+"</p>";
	content += "<p class='text-left'>Stock on Hand: "+$(obj).closest('tr').find("input[name=qty]").data("soh")+"</p>";
	content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(obj).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
	content += "<p class='text-left'>Sales Estimate Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("eq")+"</p>";
	content += "<p class='text-left'>Available Quantity: "+$(obj).closest('tr').find("input[name=qty]").data("aq")+"</p>";	
//	$(obj).closest('tr').find("input[name=qty]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=qty]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}

function loadUnitPriceToolTip(obj){
//$(obj).closest('tr').find("input[name=cost]").tooltip("destroy");
	
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
	
//	$(obj).closest('tr').find("input[name=cost]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=cost]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}

function calltooltip(){
	$("input[name=ITEMDES]").mouseenter(function(){
		var content = "<p class='text-left'>"+$(this).data("pd")+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"}); 
	});

	$("input[name=qty]").mouseenter(function(){
		var content = "<p class='text-left'>Reorder Level: "+$(this).data("rl")+"</p>";
		content += "<p class='text-left'>Max Stock Quantity: "+$(this).data("msq")+"</p>";
		content += "<p class='text-left'>Stock on Hand: "+$(this).data("soh")+"</p>";
		content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(this).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
		content += "<p class='text-left'>Sales Estimate Quantity: "+$(this).data("eq")+"</p>";
		content += "<p class='text-left'>Available Quantity: "+$(this).data("aq")+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"}); 
	});
	
	$("input[name=cost]").mouseenter(function(){
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
} 

function calltooltips(){
	$("input[name=ITEMDES]").mouseenter(function(){
		var content = "<p class='text-left'>"+$(this).data("pd")+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"}); 
	});
	
	$("input[name=qty]").mouseenter(function(){
		var content = "<p class='text-left'>Reorder Level: "+$(this).closest('tr').find("input[name=reorder]").val()+"</p>";
		content += "<p class='text-left'>Max Stock Quantity: "+$(this).closest('tr').find("input[name=maxstkqty]").val()+"</p>";
		content += "<p class='text-left'>Stock on Hand: "+$(this).closest('tr').find("input[name=stkqty]").val()+"</p>";
		content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(this).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
		content += "<p class='text-left'>Sales Estimate Quantity: "+$(this).closest('tr').find("input[name=estqty]").val()+"</p>";
		content += "<p class='text-left'>Available Quantity: "+$(this).closest('tr').find("input[name=avlqty]").val()+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"}); 
	});
	
	$("input[name=cost]").mouseenter(function(){
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
} 

function CheckPriceVal(obj, uom) {
	var productId = $(obj).closest('tr').find("td:nth-child(2)").find('input[name=item]').val();
	var dono = $("input[name=DONO]").val();
    var desc = "";
    var disc = $(obj).closest('tr').find("input[name=customerdiscount]").val();
    var discounttype = $(obj).closest('tr').find("input[name=discounttype]").val();
    var replacePreviousSalesCost = $("input[name=replacePreviousInvoiceCost]").val();
    var price = $(obj).closest('tr').find("input[name=itemprice]").val();
    var cost = $(obj).closest('tr').find("input[name=itemcost]").val();
    var addonprice = $(obj).closest('tr').find("input[name=addonprice]").val();
    var addontype = $(obj).closest('tr').find("input[name=addontype]").val();
    if(replacePreviousSalesCost==3) {
    if(addontype!=="%") 
    price = cost;
    }
    var ConvertedUnitCost="0";
    var ConvertedUnitCostWTC="0";
    var Convertedmsprice = parseFloat($(obj).closest('tr').find("input[name=minsp]").val());
    var numberOfDecimal = $("input[name=numberOfDecimal]").val();
    var currencyID = $("input[name=CURRENCY]").val();
    var currencyUSEQT = $("input[name=CURRENCYUSEQT]").val();
    if(replacePreviousSalesCost==0) {
	if(addontype!=="%"){		 
    	price = cost;
    	} else {
		//price=parseFloat(price/currencyUSEQT);
		}
	}
	if((productId=="" || productId.length==0) && (desc == "" ||desc.length == 0)) {
		alert("Enter Product ID/Description !");
	}else{
	var urlStr = "/track/MiscOrderHandlingServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ITEM : productId,
			DESC : desc,
			CURRENCYID:currencyID,
            UOM:uom,
            PRICE : price,
            COST : cost,
            TYPE:"Sales",
            DISC:disc.replace("%",""),
            MINPRICE:Convertedmsprice,
			ACTION : "VALIDATE_PRODUCT_UOM_BY_CURRENCY"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "100") {
					var resultVal = data.result;
					var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
					Convertedmprice=resultVal.MinPriceWTC;
					$(obj).closest('tr').find("input[name=minsp]").val(Convertedmprice);
					if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
						$(obj).closest('tr').find("input[name=cost]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
						$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
					}else{
						ConvertedUnitCost=resultVal.ConvertedUnitCost;
						ConvertedUnitCostWTC=resultVal.ConvertedUnitCostWTC.match(regex)[0];
						if(replacePreviousSalesCost==3 || replacePreviousSalesCost==0) {
						if(addontype!=="%") {
						ConvertedUnitCost=parseFloat(resultVal.ConvertedUnitCost)+parseFloat(addonprice*currencyUSEQT);
						ConvertedUnitCostWTC=parseFloat(resultVal.ConvertedUnitCostWTC)+parseFloat(addonprice*currencyUSEQT);		
						}					
						}
						$(obj).closest('tr').find("input[name=cost]").val(parseFloat(ConvertedUnitCost).toFixed(numberOfDecimal));
						$(obj).closest('tr').find("input[name=unitpricerd]").val(ConvertedUnitCostWTC);
					}
					if(resultVal.cost == null || resultVal.cost == undefined || resultVal.cost == 0){
						$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
					}else{
						$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(resultVal.cost*currencyUSEQT).toFixed(numberOfDecimal));
					}

					if(parseFloat(disc)>0)
                    {
                    	if(discounttype=="BYPERCENTAGE")
						{
							if(replacePreviousSalesCost==0) {
								if(addontype!=="%"){
									var getdist = disc.replace("%","");
									discount = parseFloat((ConvertedUnitCost*getdist)/100);
									price = parseFloat(ConvertedUnitCost-(discount));		
								} else
									price = parseFloat(ConvertedUnitCost);
							} else {
								var getdist = disc.replace("%","");
								discount = parseFloat((ConvertedUnitCost*getdist)/100);
								price = parseFloat(ConvertedUnitCost-(discount));
							}							
						}
							//var getdist = disc.replace("%","");
							//discount = parseFloat((ConvertedUnitCost*getdist)/100);
							//price = parseFloat(ConvertedUnitCost-((ConvertedUnitCost*getdist)/100));
						//}
						else
						{
							var getdist = disc.replace("%","");
							price = parseFloat(ConvertedUnitCost-(getdist*currencyUSEQT));
							//price = parseFloat(resultVal.ConvertedDiscWTC);
 						}
                    	var calAmount = parseFloat(price).toFixed(numberOfDecimal);
						$(obj).closest('tr').find("input[name=cost]").val(parseFloat(calAmount).toFixed(numberOfDecimal));
						$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
						$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);
                    }
					calculateAmount(obj);
					loadUnitPriceToolTip(obj);
					loadItemDescToolTip(obj);
				} else {
				}
			}
		});
	}
}
function loadItemDescToolTip(obj){
//	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip("destroy");
	var content = "<p class='text-left'>"+$(obj).closest('tr').find("input[name=ITEMDES]").data("pd")+"</p>";
//	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
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
		
function getDobNationality(DOB,NATIONALITY){
	var numberOfDecimal = $("input[name='numberOfDecimal']").val();
	var amount = 0, discountValue=0, shippingvalue=0, adjustmentvalue=0,totalvalue=0,orderdisc=0;
	var ptaxstatus = $("#item_rates").val();
	var ptaxiszero = $("input[name='ptaxiszero']").val();
	var ptaxisshow = $("input[name='ptaxisshow']").val();
	var ptaxpercentage = $("input[name='ptaxpercentage']").val();
	var ptaxdisplay = $("input[name='ptaxdisplay']").val();
	$('#discount_type').append('<option value="%">%</option>');
	var education = $("input[name='EDUCATION']").val(); // COMPANY INDUSTRY by company details
	const date = new Date(); 
	const currentYear = date.getFullYear(); //getting current year
	var DOB = $("input[name='DOBYEAR']").val(); // DOB by given customer name
	var DOBsplit = DOB.split("/"); // splitting  
	var DOByear = DOBsplit[2]; // getting year value
	var IntDobYear = parseInt(DOByear); // interger format
	var age = currentYear - IntDobYear; // subtracting currentyear with dobyear 
	var national = $("input[name='NATIONAL']").val(); // NATIONALITY by customer
	var discount= $("input[name='discount']").val();
	var odiscounttype= $("select[name='discount_type']").val();
		discount = checkno(discount);
		discount = parseFloat(discount).toFixed(3);
		if((national == "Singapore Citizen") && (age >= "40")){
//		alert("ab sin");
		discount = 70;
		}else if((national == "Singapore Citizen") && (age <= "40")){
		discount = 50;
//		alert("be sin");
		}else if((national == "Permanent Residence")){
		discount = 50;
//		alert("per");
		}else if((national == "Foreigner")){
		discount = 0;
//		alert("for");
		}
		discount = parseFloat(discount).toFixed(3);
		$("input[name ='discount']").val(discount);
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