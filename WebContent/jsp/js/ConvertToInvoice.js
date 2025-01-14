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
	var displayCustomerpop = document.form1.displayCustomerpop.value;
	//$("#SALES_LOC").val("Abu Dhabi");
	document.form1.STATE_PREFIX.value="AUH";
	
	if(cmd=="Edit")
	{
	if(TranId!="")
		{
		getEditInvoiceDetail(TranId);
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
	/*$('input[name ="cost"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));*/
	$('input[name ="item_discount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	/*$('input[name ="amount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));*/
	
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
		    	return '<div onclick="getcustname(\''+data.TAXTREATMENT+'\',\''+data.TRANSPORTNAME+'\',\''+data.TRANSPORTID+'\',\''+data.CUSTNO+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();
			if(displayCustomerpop == 'true'){
				$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal" onclick="document.form1.custModal.value=\'cust\'"><a href="#"> + New Cutomer</a></div>');
//				$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal" ><a href="#"> + New Cutomer</a></div>');
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
			$("input[name=transports]").val("");
			//document.form1.CUST_CODE.value = $(this).val();
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
			
		}).on('typeahead:select',function(event,selection){
			$("#project").typeahead('val', '"');
			$("#project").typeahead('val', '');
			$("input[name=PROJECTID]").val('');
			
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
//				addr += '<p>'+selection.ADDR3+'</p>';
//				addr += '<p>'+selection.ADDR4+'</p>';
				addr += '<p>'+selection.STATE+'</p>';
				addr += '<p>'+selection.COUNTRY+' '+selection.ZIP+'</p>';
//				addr += '<p>'+selection.STATE+'</p>';
				addr += '<p>'+selection.HPNO+'</p>';
				addr += '<p>'+selection.EMAIL+'</p>';
				addr += '</div>';
				addr += '<div class="col-sm-5" style="line-height: 7px;font-size: 13px;"><h5 style="font-weight: bold;">Shipping Address   <a><span data-toggle="modal" data-target="#shipaddr" onclick="getshipaddr();" style="font-size: 15px;font-weight: 450;">Change</span></a></h5>';
				addr += '<div id="cshipaddr">';
				addr += '<p>'+selection.SHIPCONTACTNAME+'</p>';
				addr += '<p>'+selection.SHIPDESGINATION+'</p>';
				addr += '<p>'+selection.SHIPADDR1+' '+selection.SHIPADDR2+'</p>';
				addr += '<p>'+selection.SHIPADDR3+' '+selection.SHIPADDR4+'</p>';
//				addr += '<p>'+selection.SHIPADDR3+'</p>';
//				addr += '<p>'+selection.SHIPADDR4+'</p>';
				addr += '<p>'+selection.SHIPSTATE+'</p>';
				addr += '<p>'+selection.SHIPCOUNTRY+' '+selection.SHIPZIP+'</p>';
//				addr += '<p>'+selection.SHIPCOUNTRY+'</p>';
//				addr += '<p>'+selection.SHIPZIP+'</p>';
				addr += '<p>'+selection.SHIPHPNO+'</p>';
				addr += '<p>'+selection.SHIPWORKPHONE+'</p>';
				addr += '<p>'+selection.SHIPEMAIL+'</p>';
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
				//$("input[name=transport]").val(selection.TRANSPORT_MODE);
				$("#transport").typeahead('val', selection.TRANSPORT_MODE);
				
				$("#PAYMENTTYPE").typeahead('val', selection.PAY_TERMS);
				$("input[name=payment_terms]").val(selection.PAYMENT_TERMS);
				$("input[name=SHIPPINGID]").val(selection.CUSTNO);
				$("input[name=SHIPPINGCUSTOMER]").val(selection.CNAME);
			}
		});
		function onAdd(){
	 var CUST_CODE   = document.form.CUST_CODE_C.value;
	 var CUST_NAME   = document.form.CUST_NAME_C.value;
	 var cus_companyregnumber   = document.form.cus_companyregnumber.value;
	 var CL   = document.form.CREDITLIMIT.value;
	 var TAXTREATMENT   = document.form.TAXTREATMENT.value;
	 var RCBNO   = document.form.RCBNO.value;
	 var rcbn = RCBNO.length;
	 var region = document.form.COUNTRY_REG.value;
	 if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE_C.focus(); return false; }
	 if(CUST_NAME == "" || CUST_NAME == null) {
		 alert("Please Enter Customer Name"); 
		 document.form.CUST_NAME.focus();
		 return false; 
		 }


//   Author Name:Resviya ,Date:9/08/21 , Description -UEN Alert    

	   if(region == "GCC"){
		   document.form.cus_companyregnumber.value="";
		}else if(region == "ASIA PACIFIC"){
			if (cus_companyregnumber == "" || cus_companyregnumber == null) {
			alert("Please Enter Unique Entity Number (UEN)");
			document.form.cus_companyregnumber.focus();
			return false; 
			}
		}

//	   END

//	 <!-- Author Name:Resviya ,Date:16/07/21 -->

	 if(CURRENCY == "" || CURRENCY == null) {
		 alert("Please Enter Currency ID"); 
		 document.form.CUS_CURRENCY.focus();
		 return false; 
		 }
//	 End

	 if(document.form.TAXTREATMENT.selectedIndex==0)
		{
		alert("Please Select TAXTREATMENT");
		document.form.TAXTREATMENT.focus();
		return false;
		}
	 if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	   {
	   var  d = document.getElementById("TaxByLabel").value;
	   	if(RCBNO == "" || RCBNO == null) {
	   		
		   alert("Please Enter "+d+" No."); 
		   document.form.RCBNO.focus();
		   return false; 
		   }
	   	//if(document.form1.COUNTRY_REG.value=="GCC")// region based validtion
		//{
		if(!IsNumeric(RCBNO))
		{
	    alert(" Please Enter "+d+" No. Input In Number"); 
	   	document.form.RCBNO.focus();
	   	return false; 
	  	}

		if("15"!=rcbn)
		{
		alert(" Please Enter your 15 digit numeric "+d+" No."); 
			document.form.RCBNO.focus();
			return false; 
			}
		//}
	   }
	else if(50<rcbn)
	{
	   var  d = document.getElementById("TaxByLabel").value;
	   alert(" "+d+" No. length has exceeded the maximum value"); 
	   document.form.RCBNO.focus();
	   return false; 
	 }

	 if(CL < 0) 
	 {
		   alert("Credit limit cannot be less than zero");
		   document.form.CREDITLIMIT.focus(); 
		   return false; 
		   }	 
	// alert(isCL);
	
	 //alert("2nd");
	 if(!IsNumeric(CL))
	 {
	   alert(" Please Enter Credit Limit Input In Number");
	   form.CREDITLIMIT.focus();  
	   form.CREDITLIMIT.select(); 
	   return false;
	 }
	 if(!IsNumeric(form.PMENT_DAYS.value))
	 {
	   alert(" Please Enter Days Input In Number");
	   form.PMENT_DAYS.focus();  form.PMENT_DAYS.select(); return false;
	 }
	 
	 
	//  alert(CL);
	 /* if(this.form.ISCREDITLIMIT.checked == true && CL == ""){
		   alert("Please Enter Credit Limit");
		   document.form.CREDITLIMIT.focus();
		   return false; 
	 } */
	 if(document.form.COUNTRY_CODE_C.selectedIndex==0)
		{
		   alert("Please Select Country from Address");
		   document.form.COUNTRY_CODE_C.focus();
		 return false;
		}
	 /* if(isCL.equals("1") && CL.equals(""))
		  {
			  alert("Please Enter Credit Limit"); 
			   document.form.CREDITLIMIT.focus();
			   return false; 
		  }	 */
	 
	 /* document.form.action  = "/track/CreateCustomerServlet?action=ADD&reurl=createInvoice";
	 document.form.submit(); */
	 var datasend = $('#formCustomer').serialize();
	   
	  
	   var urlStr = "/track/CreateCustomerServlet?action=JADD&reurl=createInvoice";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : datasend,
		dataType : "json",
		success : function(data) {
			if(data.customer[0].STATUS=="FAILED")
			{
				alert(data.customer[0].MESSAGE);
			}else{
			if(document.form1.custModal.value == "cust"){
			//alert(JSON.stringify(data));
			$("input[name ='CUST_CODE']").val(data.customer[0].CID);
			//document.getElementById("CUST_CODE").value  = data.customer[0].CID;
			document.getElementById("CUSTOMER").value  = data.customer[0].CName;
			$("input[name ='CUSTOMER']").val(data.customer[0].CName);
			$("input[name ='transport']").val(data.customer[0].TRANSPORTNAME);
			$("input[name ='TRANSPORTID']").val(data.customer[0].TRANSPORTID);
			$("input[name ='TAXTREATMENT_VALUE']").val(data.customer[0].CTAXTREATMENT);
			getCurrencyid(data.customer[0].CURRENCY,data.customer[0].CURRENCY_ID,data.customer[0].CURRENCYUSEQT);
			document.getElementById('nTAXTREATMENT').value = data.customer[0].CTAXTREATMENT;

			$('#shipbilladd').empty();
			var addr = '<div class="col-sm-2"></div>';
			addr += '<div class="col-sm-5" style="line-height: 7px;font-size: 13px;"><h5 style="font-weight: bold;">Billing Address</h5>';
			addr += '<p>'+data.customer[0].CNAME+'</p>';
			addr += '<p>'+data.customer[0].ADDR1+' '+data.customer[0].ADDR2+'</p>';
			addr += '<p>'+data.customer[0].ADDR3+' '+data.customer[0].ADDR4+'</p>';
			addr += '<p>'+data.customer[0].STATE+'</p>';
			addr += '<p>'+data.customer[0].COUNTRY+' '+data.customer[0].ZIP+'</p>';
			addr += '<p>'+data.customer[0].HPNO+'</p>';
			addr += '<p>'+data.customer[0].EMAIL+'</p>';
			addr += '</div>';
			addr += '<div class="col-sm-5" style="line-height: 7px;font-size: 13px;"><h5 style="font-weight: bold;">Shipping Address   <a><span data-toggle="modal" data-target="#shipaddr" onclick="getshipaddr();" style="font-size: 15px;font-weight: 450;">Change</span></a></h5>';
			addr += '<div id="cshipaddr">';
			addr += '<p>'+data.customer[0].SHIPCONTACTNAME+'</p>';
			addr += '<p>'+data.customer[0].SHIPDESGINATION+'</p>';
			addr += '<p>'+data.customer[0].SHIPADDR1+' '+data.customer[0].SHIPADDR2+'</p>';
			addr += '<p>'+data.customer[0].SHIPADDR3+' '+data.customer[0].SHIPADDR4+'</p>';
			addr += '<p>'+data.customer[0].SHIPSTATE+'</p>';
			addr += '<p>'+data.customer[0].SHIPCOUNTRY+' '+data.customer[0].SHIPZIP+'</p>';
			addr += '<p>'+data.customer[0].SHIPHPNO+'</p>';
			addr += '<p>'+data.customer[0].SHIPWORKPHONE+'</p>';
			addr += '<p>'+data.customer[0].SHIPEMAIL+'</p>';
			addr += '</div>';
			addr += '</div>';
			$('#shipbilladd').append(addr);
			$("input[name=SHIPCONTACTNAME]").val(data.customer[0].SHIPCONTACTNAME);
			$("input[name=SHIPDESGINATION]").val(data.customer[0].SHIPDESGINATION);
			$("input[name=SHIPADDR1]").val(data.customer[0].SHIPADDR1);
			$("input[name=SHIPADDR2]").val(data.customer[0].SHIPADDR2);
			$("input[name=SHIPADDR3]").val(data.customer[0].SHIPADDR3);
			$("input[name=SHIPADDR4]").val(data.customer[0].SHIPADDR4);
			$("input[name=SHIPSTATE]").val(data.customer[0].SHIPSTATE);
			$("input[name=SHIPZIP]").val(data.customer[0].SHIPZIP);
			$("input[name=SHIPWORKPHONE]").val(data.customer[0].SHIPWORKPHONE);
			$("input[name=SHIPCOUNTRY]").val(data.customer[0].SHIPCOUNTRY);
			$("input[name=SHIPHPNO]").val(data.customer[0].SHIPHPNO);
			$("input[name=SHIPEMAIL]").val(data.customer[0].SHIPEMAIL);
			
			$("input[name=payment_terms]").val(data.customer[0].PAYMENT_TERMS);
			$("input[name=SHIPPINGID]").val(data.customer[0].CID);
			$("input[name=SHIPPINGCUSTOMER]").val(data.customer[0].CName);
			
			document.form.reset();
			$('#customerModal').modal('hide');
		}
		else{
			document.form.reset();
			document.form1.SHIPPINGCUSTOMER.value = data.customer[0].CName;
			document.form1.SHIPPINGID.value = data.customer[0].CID;
			$('#customerModal').modal('hide');
		}
		}
		}
		});
	}
	
	
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
			menuElement.after( '<div class="transportAddBtn footer"  data-toggle="modal" data-target="#transportModal" onclick="document.form1.custModal.value=\'\'"><a href="#"> + New Transport </a></div>');
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
			removeSuggestionToTable();
			addSuggestionToTable();
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
			menuElement.after( '<div class="payTermsAddBtn footer"  data-toggle="modal" data-target="#payTermsModal" onclick="document.form1.custModal.value=\'\'"><a href="#"> + Add Payment Terms</a></div>');
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
	$('#gino').typeahead({
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
			removeSuggestionToTable();
			addSuggestionToTable();
		});
	
	$(".bill-table tbody").on('click','.bill-action',function(){
	    
	    var obj = $(this).closest('tr').find('td:nth-child(6)');
	    calculateTax(obj, "", "", "");
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
	
	//IMTHI modified on 21-03-20222
		$("input[name=qty]").mouseenter(function(){
		var content = "<p class='text-left'>Reorder Level: "+$(this).closest('tr').find("input[name=reorder]").val()+"</p>";
		content += "<p class='text-left'>Max Stock Quantity: "+$(this).closest('tr').find("input[name=maxstkqty]").val()+"</p>";
		content += "<p class='text-left'>Stock on Hand: "+$(this).closest('tr').find("input[name=stockonhand]").val()+"</p>";
		content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(this).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
		content += "<p class='text-left'>Sales Estimate Quantity: "+$(this).closest('tr').find("input[name=estimateqty]").val()+"</p>";
		content += "<p class='text-left'>Available Quantity: "+$(this).closest('tr').find("input[name=avalqty]").val()+"</p>";
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
	//END
	
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
	
	/* Shipping Customer Auto Suggestion */
	$('#SHIPPINGCUSTOMER').typeahead({
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
	
	/* Incoterm Auto Suggestion */
	$('#INCOTERM').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'INCOTERM',  
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
						ACTION : "GET_INCOTERM_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.INCOTERMS);
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
		    	return '<p>' + data.INCOTERM + '</p>';
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
	
	
	addSuggestionToTable();
	calculateTotal();
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
	var curency = $("input[name=CURRENCYID]").val();
	var taxdisplay = $("input[name=ptaxdisplay]").val();
	var numberOfDecimal = $("#numberOfDecimal").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var body="";
	body += '<tr>';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" class="form-control itemSearch" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '</td>';	
	body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)"></td>';
	body += "<td class=\"item-cost text-right\"><input type=\"text\" name=\"cost\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\"></td>";
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
	body += '<td class="item-tax">';
	body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
	body += '<input type="text" name="tax" class="form-control taxSearch" value="'+taxdisplay+'" placeholder="Select a Tax" readonly>';
	body += '</td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += "<input name=\"amount\" type=\"text\" class=\"form-control text-right\" value="+szero+" readonly=\"readonly\" style=\"display:inline-block;\">";
	body += '</td>';
	body += '</tr>';
	$(".bill-table tbody").append(body);
	removeSuggestionToTable();
	addSuggestionToTable();
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

//customer popup changes
function checkcustomercurrency(currency){	
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
						document.getElementById("CUS_CURRENCY").focus();
						$("#CUS_CURRENCY").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

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

//jsp validation
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

function checkincoterms(incoterms){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				INCOTERMS : incoterms,
				ACTION : "INCOTERMS_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Incoterms Does't Exists");
						document.getElementById("INCOTERM").focus();
						$("#INCOTERM").typeahead('val', '');
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
			return '<div onclick="loadItemData(this,\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
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
	});	
	
	$(".batchSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
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
				ITEMNO : obj.find("td:nth-child(2) input").val(),
				UOM : obj.find("td:nth-child(4) input").val(),
				LOC : obj.find("td:nth-child(5) input").val()
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
	//$(".item_discounttypeSearch").typeahead('destroy');
}

function loadItemData(obj, catalogPath, account, cost){
	
	
	
	
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("td:nth-child(4)").find('input').val(cost);
	calculateAmount(obj);
}

/*function calculateAmount(obj){

	var numberOfDecimal = $("#numberOfDecimal").val();
	var state = document.form1.STATE_PREFIX.value;
	var plant = document.form1.plant.value;
	
	var qty = parseFloat($(obj).closest('tr').find("td:nth-child(7)").find('input').val()).toFixed(3);
	var cost = parseFloat($(obj).closest('tr').find("td:nth-child(8)").find('input').val()).toFixed(numberOfDecimal);
	var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(9)").find('input').val()).toFixed(numberOfDecimal);
	var discounType = $(obj).closest('tr').find("td:nth-child(9)").find('select').val();
	var itemDiscountval=itemDiscount;
	 if(discounType == "%"){
		 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
		 itemDiscount = parseFloat(itemDiscount).toFixed(3);
	 }
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);
	
	if(parseFloat(amount) >= parseFloat("0")){
		$(obj).closest('tr').find("td:nth-child(9)").find('input').val(itemDiscount);
		$(obj).closest('tr').find("td:nth-child(11)").find('input').val(amount);
	}else{
		alert("discout should be less than Unit Price");
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
	var bill = document.form1.invoice.value;
	var billDate = document.form1.invoice_date.value;
	var itemRates = document.form1.item_rates.value;
	var discount = document.form1.discount.value;
	var discountAccount = document.form1.discount_account.value;
	var Currency = document.form1.CURRENCY.value;
	var CURRENCYUSEQT = document.form1.CURRENCYUSEQT.value;	
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

//function transportCallback(data){
//	if(data.STATUS="SUCCESS"){
//		$("#transport").typeahead('val', data.transport);
//		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
//	}
//}

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
		body += '<input type="hidden" name="lnno" value="1">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-qty text-right">';
		body += '<input type="text" name="qty" class="form-control text-right" value="'+data.QTYOR+'" readonly="readonly">';
		body += '<input type="hidden" name="ORDQTY" value="'+data.QTYOR+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITPRICE+'" readonly="readonly"></td>';
		body += '<td class="item-discount text-right">';
		body += '<input name="item_discount" type="text" class="form-control text-right" value="0.00" readonly="readonly" onkeypress="return isNumberKey(event,this,4)">';
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
	setLineNo();
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
		body += '<input type="hidden" name="lnno" value="1">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control" value="'+data.ITEM+'" onchange="calculateAmount(this)">';
		body += '</td>';
		body += '<td class="item-qty text-right">';
		body += '<input type="text" name="qty" class="form-control text-right" value="'+data.QTYOR+'" onchange="calculateAmount(this)">';
		body += '<input type="hidden" name="ORDQTY" value="'+data.QTYOR+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="item-cost text-right"><input type="text" name="cost" class="form-control text-right" value="'+data.UNITPRICE+'" onchange="calculateAmount(this)"></td>';
		body += '<td class="item-discount text-right">';
		body += '<div class="row">';							
		body += '<div class=" col-lg-12 col-sm-3 col-12">';
		body += '<div class="input-group my-group" style="width:120px;">';
		body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEM_DISCOUNT+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
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
	setLineNo();
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
		ACTION : "INVOICE"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.invno;
				document.form1.invoice.value= resultV;
	
			} else {
				alert("Unable to genarate INVOICE NO");
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

function getcustname(TAXTREATMENT,TRANSPORTNAME,TRANSPORTID,CUSTNO,CURRENCY,CURRENCYID,CURRENCYUSEQT){
//document.getElementById(TAXTREATMENT).innerHTML = TAXTREATMENT;
$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
$("input[name=transport]").val(TRANSPORTNAME);
$("input[name=TRANSPORTID]").val(TRANSPORTID);
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
			var taxing = $('td:eq(5)', this).find('input[name="tax_type"]').val();
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
			var taxtype = $('td:eq(5)', this).find('input[name="tax_type"]').val();
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
		var email =document.form.EMAIL.value;
		var country_code = document.getElementById("COUNTRY_CODE_C").value;
		SHIPCUST_OnCountry(country_code);
		var scountry = document.form.COUNTRY.value;
		var addr1 = document.getElementById("ADDR1").value;
		var addr2 = document.getElementById("ADDR2").value;
		var addr3 = document.getElementById("ADDR3").value;
		var addr4 = document.getElementById("ADDR4").value;
		var state = document.getElementById("C_STATE").value;
		var zip = document.getElementById("ZIP").value;
			     
		document.getElementById("CUST_SHIP_CONTACTNAME").value = contactname;
		document.getElementById("CUST_SHIP_DESGINATION").value = desgination;
		document.getElementById("CUST_SHIP_WORKPHONE").value = workphone;
		document.getElementById("CUST_SHIP_HPNO").value = hpno;
		document.getElementById("CUST_SHIP_EMAIL").value = email;
		document.getElementById("CUST_SHIP_COUNTRY_CODE").value = country_code;
		document.form.CUST_SHIP_COUNTRY.value = scountry;
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
		document.form.CUST_SHIP_COUNTRY.value = "";
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
	body += '<span class="glyphicon glyphicon-remove-circle user-action" aria-hidden="true"></span>';    	
	body += '<input  name="PASSWORD" class="form-control text-left" maxlength="60" placeholder="Enter Password" type="password" autocomplete="off">';
	body += '<span class="input-group-btn phideshow" onclick="javascript:viewpassword(this);return false;" >';
    body += '<button class="btn btn-default reveal" type="button" style="background: white;"><i class="fa fa-fw fa-eye" id="peye" style="color: #23527c;"></i></button>';
    body += '</span>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle user-action" aria-hidden="true"></span>';    	
	body += '<input type="hidden" name="MANAGER_APP_VAL" value="0">';
	body += '<input type="Checkbox" style="border:0;background=#dddddd"	name="MANAGER_APP" value="1" onclick="checkManagerApp(this)">';
	body += '</td>';
	body += '</tr>';
	$(".user-table tbody").append(body);
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