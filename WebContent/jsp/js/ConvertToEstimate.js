var subWin = null;
var itemList="";
var taxList = [];
var zerotype=0;
$(document).ready(function(){
	document.form1.STATE_PREFIX.value="AUH";
	var plant = document.form1.plant.value;
	var displayCustomerpop = document.form1.displayCustomerpop.value;
	var displayPaymentTypepop = document.form1.displayPaymentTypepop.value;
	autoGen();
	var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.";
	
	$(document).on('click','#autoGen',function(){
		$.ajax({
			type: "GET",
			url: "../salesestimate/Auto-Generate",
			dataType: "json",
			beforeSend: function(){
				showLoader();
			}, 
			success: function(data) {
				$("#ESTNO").val(data.ESTNO);
				$("input[name=ISAUTOGENERATE]").val(true);
			},
			error: function(data) {
				alert('Unable to generate Order Number. Please try again later.');
			},
			complete: function(){
				hideLoader();
			}
		});
	});
	
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
	},
	{
		display: 'CNAME',  
		async: true,
		source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax({
				type : "GET",
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
			   return '<div onclick="setCustomerData(\''+data.TAXTREATMENT+'\',\''+data.TRANSPORTNAME+'\',\''+data.TRANSPORTID+'\',\''+data.CUSTNO
			   +'\',\''+data.CNAME+'\',\''+data.NAME+'\',\''+data.TELNO+'\',\''+data.EMAIL
			   +'\',\''+data.ADDR1+'\',\''+data.ADDR2+'\',\''+data.ADDR3+'\',\''+data.ADDR4
			   +'\',\''+data.REMARKS+'\',\''+data.COUNTRY+'\',\''+data.ZIP+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT
			   +'\',\''+data.PAYMENTTYPE+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
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
				$("input[name=CUST_CODE]").val("");
				$('select[name ="nTAXTREATMENT"]').val("");
				$('select[name=nTAXTREATMENT]').attr('disabled',false);
				$("input[name=CUST_CODE1]").val("");
				$("input[name=CUST_NAME]").val("");
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
				$("input[name=transport]").val("");
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
				$("input[name=TRANSPORTID]").val("");
			}
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
		    	//return '<div><p class="item-suggestion" onclick="document.form1.SHIPPINGID.value = \''+data.CUSTNO+'\'">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
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
		
/* Payment Type Auto Suggestion */
	$("#PAYMENTTYPE").typeahead({
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
						PLANT : "<%=plant%>",
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
				menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal"><a href="#"> + New Payment Type</a></div>');
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
						Type : "SALES ESTIMATE",
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
			if(displayPaymentTypepop == 'true'){
				menuElement.after( '<div class="ordertypeAddBtn footer"  data-toggle="modal" data-target="#orderTypeModal"><a href="#"> + New Order Type</a></div>');
				}
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
	
	/* To get the suggestion data for Currency */
	$("#CURRENCY").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CURRENCY',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
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
				return '<div><p onclick="setCurrencyid(\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
	
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			$("input[name ='CURRENCYID']").val("");	
	});
	
	/* Employee Auto Suggestion */
	$('#EMP_NAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'FNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			  var urlStr = "../MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
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
	
	/* Incoterms Auto Suggestion */
	$('#INCOTERMS').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'INCOTERM',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
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
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="incotermAddBtn footer"  data-toggle="modal" data-target="#incotermModal"><a href="#"> + New INCOTERMS</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			$('.incotermAddBtn').show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.incotermAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	
	/* Sales Location Auto Suggestion */
	/*$('#SALES_LOC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'STATE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
						ACTION : "GET_SALES_LOCATION_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.SLOCMST);
					}
				});
		  },
		  autoSelect: true,
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<p>' + data.STATE + '</p>';
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
			$('.projAddBtn').remove();  
			/*if(displayCustomerpop == 'true'){
			$('.shipCustomer-section .tt-menu').after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#" onclick="document.form1.custModal.value=\'shipcust\'"> + New Cutomer</a></div>');
			}*/
			$(".projAddBtn").width(menuElement.width());
			$(".projAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.projAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.projAddBtn').hide();}, 150);	
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
	
	$(".so-table tbody").on('click','.bill-action',function(){
	    var obj = $(this).closest('tr').find('td:nth-child(10)');
	    calculateTax(obj, "", "", "");
	    $(this).parent().parent().remove();
	    setLineNo();
	    calculateTotal();
	});
	
	$("#remarks-table tbody").on('click','.remark-action',function(){
	    $(this).parent().parent().remove();
	});
	
	$("#supplierAttch").change(function(){
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
	
	$("#btnSalesDraft").click(function(){
		$('input[name ="orderstatus"]').val('Draft');
		var isValid = validateSalesOrder();
		if(isValid){
			var formData = new FormData($('#salesOrderForm')[0]);
		    $.ajax({
		        type: 'POST',
		        url: "../salesestimate/converttoestimate",
			    data:  formData,
			    contentType: false,
			    processData: false,
		        success: function (data) {
		        	window.location.href="../salesestimate/summary?msg="+data.MESSAGE;
		        },
		        error: function (data) {
		            alert(data.responseText);
		        }
		    });
	        return false; 
		}		
	});
	
	$("#btnSalesOpen").click(function(){
		$('input[name ="orderstatus"]').val('Open');
		var isValid = validateSalesOrder();
		if(isValid){
			var formData = new FormData($('#salesOrderForm')[0]);
		    $.ajax({
		        type: 'POST',
		        url: "../salesestimate/converttoestimate",
			    data:  formData,
			    contentType: false,
			    processData: false,
		        success: function (data) {
		        	if(data.ERROR_CODE == 100)
		        		window.location.href="../salesestimate/summary?msg="+data.MESSAGE;
		        	else
		        		alert(data.MESSAGE);
		        },
		        error: function (data) {
		            alert(data.responseText);
		        }
		    });
	        return false; 
		}		
	});
	
	$("input[name=QTY]").mouseenter(function(){
		var content = "<p class='text-left'>Reorder Level: "+$(this).data("rl")+"</p>";
		content += "<p class='text-left'>Max Stock Quantity: "+$(this).data("msq")+"</p>";
		content += "<p class='text-left'>Stock on Hand: "+$(this).data("soh")+"</p>";
		content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(this).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
		content += "<p class='text-left'>Sales Estimate Quantity: "+$(this).data("eq")+"</p>";
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
	
	/*$("input[name=unitprice]").focusout(function(){
		var value = $(this).val();
		var minSellingPrice = $(this).closest('tr').find("input[name=minsp]").val();
		if(value != '0' && value < minSellingPrice){
			alert("Unit Price should not be less than minimum selling price");
			return false;
		}else{
			checkPreviousCost(this);
		}
	});*/
	
	$("#btnSaveRemarks").click(function(){
		var data = $("#remarksForm").serialize();
		$.ajax({
			type : "POST",
			url : "../salesestimate/addRemarks",
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
	
	$('select[name="COUNTRY_CODE_C"]').on('change', function(){		
	    var text = $("#COUNTRY_CODE_C option:selected").text();
	    $("input[name ='COUNTRY']").val(text.trim());
	});
	
	$(document).on("focusout","input[name ='CURRENCYUSEQT']",function(){
		var CURRENCYUSEQTCost = parseFloat($("input[name ='CURRENCYUSEQT']").val());
		if(!Number.isNaN(CURRENCYUSEQTCost))
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});
	
	/*$(".so-table tbody tr").each(function() {
	    var obj = $(this).closest('tr').find('td:nth-child(10)');
	    calculateTax(obj, "", "", "");
	});*/
	//renderTaxDetails();
	//changetaxforgst();
	calculateTotal();
	addSuggestionToTable();
	
});/* document ready*/

function addSuggestionToTable(){
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
			var urlStr = "../ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
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
			return '<div onclick="loadItemData(this,\''+ data.ITEM+'\',\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.SALESUOM+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
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
				var urlStr = "../ChartOfAccountServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
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
			menuElement.after( '<div class="expenseaccountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>');
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
	/*$(".taxSearch").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'DISPLAY',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax({
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ACTION : "GET_GST_TYPE_DATA_SALES_SO",
				SALESLOC : $("input[name=STATE_PREFIX]").val(),
				GST : $("input[name=GST]").val(),
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
		changetaxforgst();
	});*/	
	
	$('.uomSearch').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'UOM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
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
			return '<div><p class="item-suggestion" onclick="CheckPriceVal(this,\''+data.UOM+'\')">'+data.UOM+'</p></div>';
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
	
	
}

function removeSuggestionToTable(){
	$(".itemSearch").typeahead('destroy');
	$(".taxSearch").typeahead('destroy');
	$(".accountSearch").typeahead('destroy');
	$(".uomSearch").typeahead('destroy');
}


function addRow(){
	var curency = $("input[name=CURRENCYID]").val();
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var taxdisplay = $("input[name=ptaxdisplay]").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var body="";
	body += '<tr>';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">';
	body += '<input type="hidden" name="lnno" value="1">';
	body += '<input type="hidden" name="itemdesc" value="">';
	body += '<input type="hidden" name="unitpricerd" value="">';
	body += '<input type="hidden" name="minstkqty" value="">';
	body += '<input type="hidden" name="maxstkqty" value="">';
	body += '<input type="hidden" name="stockonhand" value="">';
	body += '<input type="hidden" name="outgoingqty" value="">';
	body += '<input type="hidden" name="customerdiscount" value="">';
	body += '<input type="hidden" name="unitpricediscount" value="">';
	body += '<input type="hidden" name="discounttype" value="">';
	body += '<input type="hidden" name="customerdiscount" value="">';
	body += '<input type="hidden" name="minsp" value="">';
	body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
	body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+taxdisplay+'">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" style="width:87%" class="form-control itemSearch" onchange="checkitems(this.value,this)" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '</td>';	
	body += '<td>';
	body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Account">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="UOM" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="UOM">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" name="QTY" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
	body += '<td>';
	body += '<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" READONLY>';
	body += '</td>';
	body += "<td class=\"item-cost text-right\"><input type=\"text\" name=\"unitprice\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\"></td>";
	body += '<td class=\"table-icon\"><a href="#" class="fa fa-info-circle" onclick="getPreviousPurchaseOrderDetails(this)"></a></td>';
	body += '<td class="item-discount text-right">';
	body += '<div class="row">';							
	body += '<div class=" col-lg-12 col-sm-3 col-12">';
	body += '<div class="input-group my-group" style="width:120px;">';
	body += "<input name=\"item_discount\" type=\"text\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\">";
	body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
	body += "<option value="+curency+">"+curency+"</option>";
	body += '<option>%</option>';										
	body += '</select>';
	body += '</div>';
	body += '</div>'; 
	body += '</div>';
	body += '</td>';
	//body += '<td class="item-tax">';
	//body += '<input type="hidden" name="tax_type"  value="'+taxdisplay+'">';
	//body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax"  value="'+taxdisplay+'" readonly>';
	//body += '</td>';
	body += '<td class="item-amount text-right grey-bg">';
	body += "<input name=\"amount\" type=\"text\" class=\"form-control text-right\" value="+szero+" readonly=\"readonly\" style=\"display:inline-block;\">";
	body += '</td>';
	body += '<td class="table-icon" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += '<a href="#" onclick="showRemarksDetails(this)">';
	body += '<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>';
	body += '</a>';
	body += '</td>';
	body += '</tr>';
	$(".so-table tbody").append(body);
	removeSuggestionToTable();
	addSuggestionToTable();
	if($("input[name=DEDUCT_INV]").val() == "0"){
		$(".invEl").hide();
	}
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	});
	setLineNo();
}

function setCustomerData(TAXTREATMENT,TRANSPORTNAME,TRANSPORTID,CUSTNO,CNAME,NAME,TELNO,EMAIL,ADD1,ADD2,ADD3,ADD4,REMARKS,COUNTRY,ZIP,CURRENCY,CURRENCYID,CURRENCYUSEQT,PAYMENTTYPE){
	$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
	$("input[name=CUST_CODE]").val(CUSTNO);
	$("input[name=CUST_CODE1]").val(CUSTNO);
	$("input[name=CUST_NAME]").val(CNAME);
	$("input[name=PERSON_INCHARGE]").val(NAME);
	$("input[name=TELNO]").val(TELNO);
	$("input[name=EMAIL]").val(EMAIL);
	$("input[name=ADD1]").val(ADD1);
	$("input[name=ADD2]").val(ADD2);
	$("input[name=ADD3]").val(ADD3);
	$("input[name=REMARK2]").val(REMARKS);
	$("input[name=ADD4]").val(ADD4);
	$("input[name=COUNTRY]").val(COUNTRY);
	$("input[name=ZIP]").val(ZIP);
	$("input[name=transport]").val(TRANSPORTNAME);
	$("input[name=TRANSPORTID]").val(TRANSPORTID);
	setCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT);

	$("input[name=PAYMENTTYPE]").val(PAYMENTTYPE);
}

function getPreviousPurchaseOrderDetails(obj) {
	var item = $(obj).parent().parent().find('input[name=item]').val();
	
	if(item != ''){
	var uom = document.form1.UOM.value;
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	$.ajax({
		type : "POST",
		url : "/track/purchaseorderservlet",
		data : {
			ITEM : item,
			CUSTCODE:"",
			UOM:uom,
			ROWS:"2",
			Submit : "GET_PREVIOUS_ORDER_DETAILS"
		},
		dataType : "json",
		success : function(data) {
			if(data.orders[0].PONO != undefined){
			var result = "";			
			$.each(data.orders, function( key, value ) {
				result += "<tr>";
				result += "<td>"+value.PONO+"</td>";
				result += "<td>"+value.CUSTNAME+"</td>";
				result += "<td>"+value.COLLECTIONDATE+"</td>";
				result += "<td>"+parseFloat(value.UNITCOST).toFixed(numberOfDecimal)+"</td>";
				result += "</tr>";
			});
			}else{
				result += "<tr>";
				result += "<td colspan='4' style='text-align: center;'> No details found</td>";
				result += "</tr>";
			}
			$(".lastPurCostDetails tbody").html(result);
			getPreviousSalesOrderDetails(item);
		}
	});
	}else{
		var result = "<tr>";
		result += "<td colspan='4' style='text-align: center;'> No details found</td>";
		result += "</tr>";
		$(".lastPurCostDetails tbody").html(result);
		
		result = "<tr>";
		result += "<td colspan='4' style='text-align: center;'> No details found</td>";
		result += "</tr>";
		$(".lastSalesPriceDetails tbody").html(result);
		$("#lastTranPriceModal").modal();
	}
}

function getPreviousSalesOrderDetails(item) {
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	$.ajax({
		type : "POST",
		url : "/track/deleveryorderservlet",
		data : {
			ITEM : item,
			CUSTCODE:document.form1.CUST_CODE.value,
			Submit : "GET_PREVIOUS_ORDER_DETAILS"
		},
		dataType : "json",
		success : function(data) {
			if(data.orders[0].ESTNO != undefined){
			var result = "";			
			
			$.each(data.orders, function( key, value ) {
				result += "<tr>";
				result += "<td>"+value.ESTNO+"</td>";
				result += "<td>"+value.CNAME+"</td>";
				result += "<td>"+value.COLLECTIONDATE+"</td>";
				result += "<td>"+parseFloat(value.UNITPRICE).toFixed(numberOfDecimal)+"</td>";
				result += "</tr>";
			});
			}else{
				result += "<tr>";
				result += "<td colspan='4' style='text-align: center;'> No details found</td>";
				result += "</tr>";
			}
			$(".lastSalesPriceDetails tbody").html(result);
			$("#lastTranPriceModal").modal();
		}
	});
}

function setCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT){
	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	$("#CURRENCY").typeahead('val', CURRENCY);
	
	var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
	document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi
	
	document.getElementById('lbltotal').innerHTML = "Total ("+CURRENCYID+")"; //  Author: Azees  Create date: July 16,2021  Description:  Total of Local Currency
	if(basecurrency!=CURRENCYID)
		document.getElementById('showtotalcur').style.display = 'block';
	else
		document.getElementById('showtotalcur').style.display = 'none';
	
	/*$('#discount_type').empty();
	$('#discount_type').append('<option value="' + CURRENCYID + '">' + CURRENCY + '</option>');
	$('#discount_type').append('<option value="%">%</option>');
	
	$('#oddiscount').empty();
	$('#oddiscount').append('<option value="' + CURRENCYID + '">' + CURRENCY + '</option>');
	$('#oddiscount').append('<option value="%">%</option>');*/
	setCURRENCYUSEQT(CURRENCYUSEQT);
}

function headerReadable(){
	document.form1.DELIVERYDATE.value="";
	if(document.form1.DATEFORMAT.checked){
		$('#DELIVERYDATE').attr('readonly',true).datepicker({ dateFormat: 'dd/mm/yy'});
		$('#DELIVERYDATE').attr('placeholder','');
	}
	else{
		$('#DELIVERYDATE').attr('readonly',false).datepicker("destroy");
		$('#DELIVERYDATE').attr('placeholder','Max 20 Characters');
	}
}

/*function loadItemData(obj, catalogPath, account, cost, salesuom){
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(cost));
	$(obj).closest('tr').find("input[name=UOM]").val(salesuom);
	calculateAmount(obj);
}*/

function loadItemData(obj, productId, catalogPath, account, cost, salesuom){
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(cost));
	$(obj).closest('tr').find("input[name=UOM]").val(salesuom);
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var currencyID = $("input[name=CURRENCY]").val();
	var custCode = $("input[name=CUST_CODE]").val();
	
	 var estno = document.form1.ESTNO.value;
	 var urlStr = "/track/ItemMstServlet";
	 var discount;
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				DONO: estno,
				TYPE: "ESTIMATE",
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
						
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
						}else{
							$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricerd]").val(resultVal.ConvertedUnitCostWTC.match(regex)[0]);
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
								
								discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100);
								price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100));
							}
							else
							{
								$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat(resultVal.outgoingOBDiscount).toFixed(numberOfDecimal));
								price = parseFloat(resultVal.outgoingOBDiscount);
							}
							var calAmount = parseFloat(price).toFixed(numberOfDecimal);
							$(obj).closest('tr').find("input[name=unitprice]").val(calAmount);
							$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
							$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);
						}
					} 
					$(obj).closest('tr').find("input[name=QTY]").data('rl',resultVal.stkqty);
					$(obj).closest('tr').find("input[name=QTY]").data('msq',resultVal.maxstkqty);
					$(obj).closest('tr').find("input[name=QTY]").data('soh',resultVal.stockonhand);
					$(obj).closest('tr').find("input[name=QTY]").data('eq',resultVal.EstQty);
					$(obj).closest('tr').find("input[name=QTY]").data('aq',resultVal.AvlbQty);
					
					calculateAmount(obj);
					loadQtyToolTip(obj);
					loadUnitPriceToolTip(obj);
				}
			});
		
	}

function CheckPriceVal(obj, uom) {
	var productId = $(obj).closest('tr').find("td:nth-child(2)").find('input[name=item]').val();
	var estno = $("input[name=ESTNO]").val();
    var desc = "";
    var disc = $(obj).closest('tr').find("input[name=customerdiscount]").val();
    var Convertedmsprice = parseFloat($(obj).closest('tr').find("input[name=minsp]").val());
    var numberOfDecimal = $("input[name=numberOfDecimal]").val();
    var currencyID = $("input[name=CURRENCY]").val();
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
						$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
						$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
					}else{
						$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
						$(obj).closest('tr').find("input[name=unitpricerd]").val(resultVal.ConvertedUnitCostWTC.match(regex)[0]);
					}
					
					if(parseFloat(disc)>0)
                    {   
                    	if(document.form1.DISCOUNTTYPE.value=="BYPERCENTAGE")
						{								 
							var getdist = disc.replace("%","");								
							discount = parseFloat((resultVal.ConvertedUnitCost*getdist)/100);
							price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*getdist)/100));
						}
						else
						{								
							price = parseFloat(resultVal.ConvertedDiscWTC);
 						}
                    	var calAmount = parseFloat(price).toFixed(numberOfDecimal);						
						$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(calAmount).toFixed(numberOfDecimal));
						$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
						$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);
                    }
					calculateAmount(obj);
					loadUnitPriceToolTip(obj);					
				} else {
				}
			}
		});
	}
}

/*function calculateAmount(obj){

	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var state = $("input[name=STATE_PREFIX]").val();
	
	var qty = parseFloat($(obj).closest('tr').find('input[name=QTY]').val()).toFixed(3);
	var cost = parseFloat($(obj).closest('tr').find('input[name=unitprice]').val()).toFixed(numberOfDecimal);
	var itemDiscount = parseFloat($(obj).closest('tr').find('input[name=item_discount]').val()).toFixed(numberOfDecimal);
	var discounType = $(obj).closest('tr').find("td:nth-child(10)").find('select').val();
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
	
	$(obj).closest('tr').find('input[name=QTY]').val(qty);
	$(obj).closest('tr').find('input[name=unitprice]').val(cost);
	
	changetaxfordiscountnew();
	calculateTotal();
}

function calculateTotal(){
	
	var numberOfDecimal = $("input[name='numberOfDecimal']").val();
	var amount = 0, discountValue=0, shippingvalue=0, adjustmentvalue=0,totalvalue=0,orderdisc=0;
	
	var discount= $("input[name='discount']").val();
	discount = parseFloat(discount).toFixed(numberOfDecimal);
	$("input[name ='discount']").val(discount);
	
	var shippingcost= $("input[name='shippingcost']").val();
	shippingcost = parseFloat(shippingcost).toFixed(numberOfDecimal);
	$("input[name ='shippingcost']").val(shippingcost);
	
	var adjustment= $("input[name ='adjustment']").val();
	adjustment = parseFloat(adjustment).toFixed(numberOfDecimal);
	$("input[name ='adjustment']").val(adjustment);
	
	$(".so-table tbody tr td:last-child").prev().each(function() {
		if($(this).find('input').attr("disabled") != "disabled"){
		    amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
		    amount = parseFloat(amount).toFixed(numberOfDecimal);
		    $("#subTotal").html(amount);
		    $('input[name ="sub_total"]').val(amount);// hidden input
		}
	});
	 
	 var taxTotal = 0;
	 if($(".taxDetails").html().length > 0 && ($("#item_rates").val() == 0)){
	 if($(".taxDetails").html().length > 0){
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

function changetaxfordiscountnew(){
	var urlStr = "/track/MasterServlet";
	$.ajax({
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		ACTION : "GET_GST_TYPE_DATA_SALES_SO",
		SALESLOC : $("input[name=STATE_PREFIX]").val(),
		GST : $("input[name=GST]").val()
	},
	dataType : "json",
	success : function(data) {
		taxList=[];
		$(".so-table tbody tr").each(function() {
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

function changetaxforgst(){
	var urlStr = "/track/MasterServlet";
	$.ajax({
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		ACTION : "GET_GST_TYPE_DATA_SALES_SO",
		SALESLOC : $("input[name=STATE_PREFIX]").val(),
		GST : $("input[name=GST]").val()
	},
	dataType : "json",
	success : function(data) {
		taxList=[];
		$(".so-table tbody tr").each(function() {
			var taxing = $(this).find('input[name="tax_type"]').val();
			var taxtype=taxing.substr(0, taxing.indexOf('(')); 
			if(taxtype=="" || taxtype==undefined)
				{
					taxtype=taxing;
				}
			var SGSTTYPES="0";
			var SGSTPERCENTAGE="0";
			var DISPLAY="0";
			$.each(data.records,function(i,v){
				var cgettaxtype = v.SGSTTYPES;
				var gettaxtype=cgettaxtype.substr(0, cgettaxtype.indexOf('(')); 
				if(cgettaxtype == taxtype){	
					pertax = v.DISPLAY;
					SGSTTYPES=v.SGSTTYPES;
					SGSTPERCENTAGE=v.SGSTPERCENTAGE;
					DISPLAY=v.DISPLAY;
				}
			});
			$(this).find('input[name="tax_type"]').val(pertax);
			$(this).find('input[name="tax"]').val(pertax)
				if(SGSTTYPES != "0"){
					var obj1 = $('td:eq(5)', this);
					calculateTaxforsatate(obj1,SGSTTYPES,SGSTPERCENTAGE,DISPLAY);
				}
		});
		renderTaxDetails();
	}
	});
}

function calculateTaxforsatate(obj, types, percentage, display){

	if(types=="ZERO RATE")
		{
			zerotype++;
		}
	$(obj).closest('td').find('input[name = "tax_type"]').val(display);
	var numberOfDecimal = $("input[name='numberOfDecimal']").val();
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

function renderTaxDetails(){
	var html="";
	var taxTotal = 0;
	var subtotal =0;
	var numberOfDecimal = $("input[name='numberOfDecimal']").val();
	$.each(taxList, function( key, data ) {
		var mydata=JSON.stringify(data);
		var originalTaxType= data.types;
		var taxtype = originalTaxType;
		console.log("Tax Type:"+taxtype);
		console.log("Zero Type:"+zerotype);
		percentageget=data.percentage;
		
		var shipingcost = $('input[name ="shippingcost"]').val();
		var shipingtax = parseFloat(shipingcost*(percentageget/100)).toFixed(numberOfDecimal);
		var discount = $('input[name ="discount"]').val();
		var discounType = $('select[name ="discount_type"]').val();
		$(".so-table tbody tr td:last-child").prev().each(function() {
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
				html+='<div class="total-amount taxAmount">'+parseFloat(parseFloat(data.value)).toFixed(numberOfDecimal)+'</div>';
				//html+='<div class="total-amount taxAmount">'+parseFloat((parseFloat(data.value)+parseFloat(shipingtax))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal)+'</div>';
				html+='</div>';taxTotal += parseFloat(parseFloat(data.value)).toFixed(numberOfDecimal);
				//taxTotal += parseFloat((parseFloat(data.value)+parseFloat(shipingtax))-(parseFloat(discounttax)+parseFloat(orderdiscounttax))).toFixed(numberOfDecimal);
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

function calculateTax(obj, types, percentage, display){
	if(types=="ZERO RATE"){
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

function validateSalesOrder(){
	var customer = $("input[name=CUST_NAME]").val();
	var estno = $("input[name=ESTNO]").val();
	var currency = $("input[name=CURRENCY]").val();
	var CURRENCYUSEQT = $("input[name=CURRENCYUSEQT]").val();//  Author: Azees  Create date: July 31,2021  Description:  Exchange Rate Issue
	var msg = "";
	var isItemValid = true, isAccValid = true, isUnitPriceValid = true,
	isUnitPriceValid = true;
	
	//resvi starts
	var ValidNumber   = document.form1.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" order's you can create"); return false; }
	//ends
	
	if(customer == ""){
		alert("Please select a Customer.");
		return false;
	}	
	if(estno == ""){
		alert("Please Enter Order Number.");
		return false;
	}	
	
	if(currency == ""){
		alert("Please select a Currency.");
		return false;
	}
	if(CURRENCYUSEQT == ""){
		alert("Please Enter Exchange Rate.");
		return false;
	}
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
	
	/*$("input[name ='unitprice']").each(function() {
		var value = $(this).val();
		var minSellingPrice = $(this).closest('tr').find("input[name=minsp]").val();
		var item = $(this).closest('tr').find("input[name=item]").val();
		
		if(value == ""){
			isUnitPriceValid =  false;
	    }
		
		if(value != '0' && value < minSellingPrice){
			isUnitPriceValid =  false;
			msg = "Unit Price should not be less than minimum selling price for Product "+item;
		}
	});	*/
	
	if(!isUnitPriceValid){
		alert(msg);
		return false;
	}
	return true;		
}

function loadQtyToolTip(obj){
	$(obj).closest('tr').find("input[name=QTY]").tooltip("destroy");
	var content = "<p class='text-left'>Reorder Level: "+$(obj).closest('tr').find("input[name=QTY]").data("rl")+"</p>";
	content += "<p class='text-left'>Max Stock Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("msq")+"</p>";
	content += "<p class='text-left'>Stock on Hand: "+$(obj).closest('tr').find("input[name=QTY]").data("soh")+"</p>";
	content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(obj).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
	content += "<p class='text-left'>Sales Estimate Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("eq")+"</p>";
	content += "<p class='text-left'>Available Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("aq")+"</p>";	
	$(obj).closest('tr').find("input[name=QTY]").tooltip({title: content, html: true, placement: "top"}); 
}

function loadUnitPriceToolTip(obj){
	$(obj).closest('tr').find("input[name=unitprice]").tooltip("destroy");
	
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
	
	$(obj).closest('tr').find("input[name=unitprice]").tooltip({title: content, html: true, placement: "top"});
}

function showRemarksDetails(obj) {
	var lnno = $(obj).closest('tr').find("input[name=lnno]").val();
	var estno = $("input[name=ESTNO]").val();
	var item = $(obj).closest('tr').find("input[name=item]").val();
	
	if(item != ''){
		$.ajax({
			type : "GET",
			url : "../salesestimate/getsalesestimateRemarks",
			data : {
				ITEM: item,
				ESTNO: estno,
				ESTLNO: lnno
			},
			dataType : "json",
			success : function(data) {
				var body="";
				if(data.REMARKS[0].remarks != undefined){
					for(i=0; i< data.REMARKS.length; i++){
						body += '<tr>';
						body += '<td style="position:relative;">';
						if(i != 0){
							body += '<span class="glyphicon glyphicon-remove-circle remark-action" aria-hidden="true"></span>';
						}						
						body += '<input type="text" class="form-control" name="remarks" value="'+data.REMARKS[i].remarks+' "placeholder="Max 500 Characters" maxlength="500">';
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
				body += '<input type="hidden" name="r_estno" value="'+estno+'">';
				body += '<input type="hidden" name="r_lnno" value="'+lnno+'">';
				$("#remarks-table tbody").html(body);
			}
		});
	}else{
		
	}
	/*var item = $(obj).parent().parent().find('input[name=item]').val();
	
	if(item != ''){
	var uom = document.form.UOM.value;
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	$.ajax({
		type : "POST",
		url : "/track/purchaseorderservlet",
		data : {
			ITEM : item,
			CUSTCODE:"",
			UOM:uom,
			ROWS:"2",
			Submit : "GET_PREVIOUS_ORDER_DETAILS"
		},
		dataType : "json",
		success : function(data) {
			if(data.orders[0].PONO != undefined){
			var result = "";			
			$.each(data.orders, function( key, value ) {
				result += "<tr>";
				result += "<td>"+value.PONO+"</td>";
				result += "<td>"+value.CUSTNAME+"</td>";
				result += "<td>"+value.COLLECTIONDATE+"</td>";
				result += "<td>"+parseFloat(value.UNITCOST).toFixed(numberOfDecimal)+"</td>";
				result += "</tr>";
			});
			}else{
				result += "<tr>";
				result += "<td colspan='4' style='text-align: center;'> No details found</td>";
				result += "</tr>";
			}
			$(".lastPurCostDetails tbody").html(result);
			getPreviousSalesOrderDetails(item);
		}
	});
	}else{
		var result = "<tr>";
		result += "<td colspan='4' style='text-align: center;'> No details found</td>";
		result += "</tr>";
		$(".lastPurCostDetails tbody").html(result);
		
		result = "<tr>";
		result += "<td colspan='4' style='text-align: center;'> No details found</td>";
		result += "</tr>";
		$(".lastSalesPriceDetails tbody").html(result);
		$("#lastTranPriceModal").modal();
	}*/
	
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

function setLineNo(){
	var i=1;
	$(".so-table tbody tr td:first-child").each(function() {
		$(this).find('input[name=lnno]').val(i);
		i++;
	});
}

function OnChange(dropdown)
{
    var myindex  = dropdown.selectedIndex;
    var SelValue = dropdown.options[myindex].value;
    document.form1.STATE_PREFIX.value = SelValue;
    changetax();
}

function onAdd(){
	 var CUST_CODE   = document.form.CUST_CODE_C.value;
	 var CUST_NAME   = document.form.CUST_NAME_C.value;
	 var PEPPOL_C  = document.form.PEPPOL_C.value;
	 var PEPPOL_IDC   = document.form.PEPPOL_IDC.value;
	 var cus_companyregnumber   = document.form.cus_companyregnumber.value;
	 var CL   = document.form.CREDITLIMIT.value;
	 var TAXTREATMENT   = document.form.TAXTREATMENT.value;
	   var RCBNO   = document.form.RCBNO.value;
		 var CURRENCY = document.form.CUS_CURRENCY.value;
		   var region = document.form.COUNTRY_REG.value;
		   
	   var rcbn = RCBNO.length;
	   
	   var ValidNumber   = document.form.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" customers you can create"); return false; }
	   
	 if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE_C.focus(); return false; }
	 if(CUST_NAME == "" || CUST_NAME == null) {
		 alert("Please Enter Customer Name"); 
		 document.form.CUST_NAME_C.focus();
		 return false; 
		 }
		 
		  if(document.getElementById("PEPPOL_C").checked == true)
			PEPPOL_C="1";
		 else 
			PEPPOL_C="0";

		   if(PEPPOL_IDC == "" &&  PEPPOL_C == "1") {
		 	  alert("Please Enter Peppol Id"); 
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
	 
//	 <!-- Author Name:Resviya ,Date:15/07/21 -->

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
				
				$("input[name=TRANSPORTID]").val(data.customer[0].TRANSPORTID);
				$("#transport").typeahead('val', data.customer[0].TRANSPORT_MODE);
				$("#PAYMENTTYPE").typeahead('val', data.customer[0].PAY_TERMS);
				
				setCurrencyid(data.customer[0].CURRENCY,data.customer[0].CURRENCY_ID,data.customer[0].CURRENCYUSEQT);

				document.getElementById('nTAXTREATMENT').value = data.customer[0].CTAXTREATMENT;
				document.form.reset();
				$('#customerModal').modal('hide');
			}else{
				document.form.reset();
				document.form1.SHIPPINGCUSTOMER.value = data.customer[0].CName;
				document.form1.SHIPPINGID.value = data.customer[0].CID;
				$('#customerModal').modal('hide');
			}
		}
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
function onIDGen()
{
	var urlStr = "/track/CreateCustomerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			action : "JAuto-ID",
			reurl : "createInvoice"
		},
		dataType : "json",
		success : function(data) {
			$("input[name ='CUST_CODE_C']").val(data.customer[0].CID);
			$("input[name ='CUST_CODE1_C']").val(data.customer[0].CID);			
		}
	});
}

function OnTaxChange(TAXTREATMENT)
{
	
	if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	{
		$("#TaxLabel").addClass("required");
	}
	else
		$("#TaxLabel").removeClass("required");
	}
function OnBank(BankName)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_BANK_DATA",
			PLANT : "<%=plant%>",
			NAME : BankName,
		},
		success : function(dataitem) {
			var BankList=dataitem.BANKMST;
			var myJSON = JSON.stringify(BankList);						
			var dt = JSON.stringify(BankList).replace('[', '').replace(']', '');
			if (dt) {
			  var result = jQuery.parseJSON(dt);			  
			  var val = result.BRANCH;			  
			  $("input[name ='BRANCH']").val(val);
			}
		}
	});		
}
function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}

function isNumber(evt) {	
evt = (evt) ? evt : window.event;
var charCode = (evt.which) ? evt.which : evt.keyCode;

if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
	if( (charCode!=43 && charCode!=32 && charCode!=45))
		{
		
  alert("  Please enter only Numbers.  ");
  return false;
		}
	}
return true;
}

function OnCountry(Country)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_STATE_DATA",
			PLANT : "<%=plant%>",
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
				 $('#C_STATE').empty();
				//	$('#C_STATE').append('<OPTION style="display:none;">Select State</OPTION>');
					$('#C_STATE').append('<OPTION>Select State</OPTION>');
						 $.each(StateList, function (key, value) {
							   $('#C_STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
							});
		}
	});	
	
}


function SHIP_OnCountry(Country)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_STATE_DATA_POPUP",
			PLANT : "<%=plant%>",
			COUNTRY : Country,
		},
		success : function(dataitem) {
			var StateList=dataitem.STATEMST;
			var myJSON = JSON.stringify(StateList);
			//alert(myJSON);
			$('#SHIP_STATE').empty();
		//	$('#SHIP_STATE').append('<OPTION style="display:none;">Select State</OPTION>');
			$('#SHIP_STATE').append('<OPTION>Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					 if(document.form.STATE.value==value.text)
						 	$('#SHIP_STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
						else if(document.form1.SHIPSTATE.value==value.text)
						 	$('#SHIP_STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
					 else
					   $('#SHIP_STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
		}
	});	
	
}
$('select[name="COUNTRY_CODE"]').on('change', function(){		
var text = $("#COUNTRY_CODE option:selected").text();
$("input[name ='COUNTRY']").val(text.trim());
});
$('select[name="COUNTRY_CODE_C"]').on('change', function(){		
var text = $("#COUNTRY_CODE_C option:selected").text();
$("input[name ='COUNTRY']").val(text.trim());
});

$('select[name="SHIP_COUNTRY_CODE"]').on('change', function(){
var text = $("#SHIP_COUNTRY_CODE option:selected").text();
$("input[name ='SHIP_COUNTRY']").val(text.trim());
});

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

function checkPreviousCost(obj) {
var item = $(obj).parent().parent().find('input[name=item]').val();
var price = $(obj).parent().parent().find('input[name=unitprice]').val();
	if(item != ''){
	var uom = document.form1.UOM.value;
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	$.ajax({
		type : "POST",
		url : "/track/purchaseorderservlet",
		data : {
			ITEM : item,
			CUSTCODE:"",
			UOM:uom,
			ROWS:"1",
			Submit : "GET_PREVIOUS_ORDER_DETAILS"
		},
		dataType : "json",
		success : function(data) {
			if(data.orders[0].UNITCOST != undefined){
				previousCost = parseFloat(data.orders[0].UNITCOST).toFixed(numberOfDecimal);
				if(parseFloat(price) < parseFloat(previousCost)){
					alert("Entered Price is less than previous cost : "+previousCost);
				}
			}
		}
	});
	}
}


function employeeCallback(data){
	if(data.STATUS="SUCCESS"){
		//alert(data.MESSAGE);
		$("#EMP_NAME").typeahead('val', data.EMP_NAME);
	}
}


function incotermsCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#INCOTERMS").typeahead('val', data.INCOTERM);
	}
}

function removetaxtrestl(){
	$('select[name=nTAXTREATMENT]').attr('disabled',false);
}

function isNumericInput(strString) {
	var strValidChars = "0123456789.";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for (var i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
}

//resvi start
function autoGen(){
$.ajax({
	type: "GET",
	url: "../salesestimate/Auto-Generate",
	dataType: "json",
	beforeSend: function(){
		showLoader();
	}, 
	success: function(data) {
		$("#ESTNO").val(data.ESTNO);
		$("input[name=ISAUTOGENERATE]").val(true);
	},
	error: function(data) {
		alert('Unable to generate Order Number. Please try again later.');
	},
	complete: function(){
		hideLoader();
	}
});
}
//ends
function popUpWin(URL) {
	subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
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
	
	var qty = parseFloat($(obj).closest('tr').find('input[name=QTY]').val()).toFixed(3);
	var cost = parseFloat($(obj).closest('tr').find('input[name=unitprice]').val()).toFixed(numberOfDecimal);
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
	
	$(obj).closest('tr').find('input[name=QTY]').val(qty);
	$(obj).closest('tr').find('input[name=unitprice]').val(cost);
	
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
	$(".so-table tbody tr td:last-child").prev().each(function() {
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
	 
	 var CURRENCYUSEQT = $('input[name ="CURRENCYUSEQT"]').val();//Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency
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

$('tr').each(function () {
	
	var qty = parseFloat($(this).find('input[name ="QTY"]').val()).toFixed(3);
	cost = ((parseFloat($(this).find('input[name=unitprice]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
	cost = parseFloat(cost).toFixed(numberOfDecimal);
	$(this).find('input[name=unitprice]').val(cost);

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
	$("select[name='oddiscount_type']").append('<option value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
	$("select[name='oddiscount_type']").append('<option selected value="%">%</option>');
	orderdiscount = checkno(orderdiscount);
	orderdiscount = parseFloat(orderdiscount).toFixed(3);
	$("input[name ='orderdiscount']").val(orderdiscount);
}else{
	$("select[name='oddiscount_type']").empty();
	$("select[name='oddiscount_type']").append('<option selected value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
	$("select[name='oddiscount_type']").append('<option value="%">%</option>');
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

function changeitem(obj){
	var obj2 = $(obj).parent().find('input[name="item"]');
	$(obj2).typeahead('val', '');
	$(obj).parent().find('input[name="item"]').focus();
}

function orderTypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#ORDERTYPE").typeahead('val', data.ORDERTYPE);
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