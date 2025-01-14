var subWin = null;
var itemList="";
var taxList = [];
var zerotype=0;
$(document).ready(function(){
	var plant = document.cpoform.plant.value;
	var dono = document.cpoform.DONO.value;
	getEditPODetails(dono);
	/*$("#PURCHASE_LOC").val("Abu Dhabi");
	document.cpoform.STATE_PREFIX.value="AUH";*/
	  autoGen();
	var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.";
    document.getElementById('TaxLabelCust').innerHTML = d +" No.";
	
	document.getElementById('CHK1').style.display = 'none';	
	if(document.cpoform.nTAXTREATMENT.value =="GCC VAT Registered"||document.cpoform.nTAXTREATMENT.value=="GCC NON VAT Registered"||document.cpoform.nTAXTREATMENT.value=="NON GCC")
	{
		document.getElementById('CHK1').style.display = 'block';
	}
	
	$(document).on('click','#autoGen',function(){
		$.ajax({
			type: "GET",
			url: "../purchaseorder/Auto-Generate",
			dataType: "json",
			beforeSend: function(){
				showLoader();
			}, 
			success: function(data) {
				$("#PONO").val(data.PONO);
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
	
	/* Sales Location Auto Suggestion */
	$('#PURCHASE_LOC').typeahead({
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
		    	return '<p onclick="document.cpoform.STATE_PREFIX.value = \''+data.STATE_PREFIX+'\'">' + data.STATE + '</p>';		    
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
	
	
//	/* Supplier Auto Suggestion */
//	$('#vendname').typeahead({
//		  hint: true,
//		  minLength:0,  
//		  searchOnFocus: true
//		},
//		{
//		  display: 'VNAME',  
//		  async: true,   
//		  source: function (query, process,asyncProcess) {
//			var urlStr = "/track/MasterServlet";
//			$.ajax( {
//			type : "POST",
//			url : urlStr,
//			async : true,
//			data : {
//				PLANT : plant,
//				ACTION : "GET_SUPPLIER_DATA",
//				QUERY : query
//			},
//			dataType : "json",
//			success : function(data) {
//				return asyncProcess(data.VENDMST);
//			}
//			});
//		  },
//		  limit: 9999,
//		  templates: {
//		  empty: [
//		      '<div style="padding:3px 20px">',
//		        'No results found',
//		      '</div>',
//		    ].join('\n'),
//		    suggestion: function(data) {
//		    return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.TRANSPORTNAME+'\',\''+data.TRANSPORTID+'\',\''+data.VENDO
//		    +'\',\''+data.PAYMENTTYPE+'\',\''+data.NAME+'\',\''+data.TELNO+'\',\''+data.EMAIL
//			+'\',\''+data.ADDR1+'\',\''+data.ADDR2+'\',\''+data.ADDR3+'\',\''+data.ADDR4
//			 +'\',\''+data.REMARKS+'\',\''+data.COUNTRY+'\',\''+data.ZIP+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYUSEQT
//		    +'\')"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
//			}
//		  }
//		}).on('typeahead:render',function(event,selection){
//			
//			/*var menuElement = $(this).parent().find(".tt-menu");
//			var top = $(".tt-menu").height()+35;
//			top+="px";
//			$('.supplierAddBtn').remove();  
//			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
//			$(".supplierAddBtn").width($(".tt-menu").width());
//			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
//			if($(this).parent().find(".tt-menu").css('display') != "block")
//				menuElement.next().hide();
//			*/
//			var menuElement = $(this).parent().find(".tt-menu");
//			var top = menuElement.height()+35;
//			top+="px";	
//			if(menuElement.next().hasClass("footer")){
//				menuElement.next().remove();  
//			}
//			menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#" onclick="document.cpoform.custModal.value=\'cust\'"> + New Supplier</a></div>');
//			menuElement.next().width(menuElement.width());
//			menuElement.next().css({ "top": top,"padding":"3px 20px" });
//			if($(this).parent().find(".tt-menu").css('display') != "block")
//				menuElement.next().hide();
//		}).on('typeahead:open',function(event,selection){
//			$('.supplierAddBtn').show();
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",true);
//			element.toggleClass("glyphicon-menu-down",false);    
//		}).on('typeahead:close',function(){
//			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",false);
//			element.toggleClass("glyphicon-menu-down",true);
//		}).on('typeahead:change',function(event,selection){
//			if($(this).val() == ""){
//				document.cpoform.vendno.value = "";
//				document.cpoform.nTAXTREATMENT.value ="";
////				document.getElementById('nTAXTREATMENT').innerHTML="";
//				$("input[name ='TAXTREATMENT_VALUE']").val("");
//				$("input[name=PERSON_INCHARGE]").val("");
//				$("input[name=TELNO]").val("");
//				$("input[name=EMAIL]").val("");
//				$("input[name=ADD1]").val("");
//				$("input[name=ADD2]").val("");
//				$("input[name=ADD3]").val("");
//				$("input[name=REMARK2]").val("");
//				$("input[name=ADD4]").val("");
//				$("input[name=COUNTRY]").val("");
//				$("input[name=ZIP]").val("");
//				$("input[name=PAYMENTTYPE]").val("");
//				$("input[name=transports]").val("");
//			}
//			$('#nTAXTREATMENT').attr('disabled',false);
//			if($('select[name ="nTAXTREATMENT"]').val() =="GCC VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="GCC NON VAT Registered"||$('select[name ="nTAXTREATMENT"]').val()=="NON GCC")
//			{
//				document.getElementById('CHK1').style.display = 'block';
//			}
//			else
//				document.getElementById('CHK1').style.display = 'none';
//			
//		});
	
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
			    return '<div onclick="getvendname(\''+data.TAXTREATMENT+'\',\''+data.TRANSPORTNAME+'\',\''+data.TRANSPORTID+'\',\''+data.PAYMENT_TERMS+'\',\''+data.PAY_TERMS+'\',\''+data.VENDO
			    +'\',\''+data.PAYMENTTYPE+'\',\''+data.NAME+'\',\''+data.TELNO+'\',\''+data.EMAIL
				+'\',\''+data.ADDR1+'\',\''+data.ADDR2+'\',\''+data.ADDR3+'\',\''+data.ADDR4
				 +'\',\''+data.REMARKS+'\',\''+data.COUNTRY+'\',\''+data.ZIP+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYUSEQT
			    +'\')"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){

			/*var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();
			$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
			*/
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();
			}
			menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"  onclick="document.cpoform.custModal.value=\'cust\'"> + New Supplier</a></div>');
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
				document.cpoform.vendno.value = "";
				document.cpoform.nTAXTREATMENT.value ="";
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
				
				$('#shipbilladd').empty();
			}
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
				$('#shipbilladd').empty();
				var addr = '<div class="col-sm-2"></div>';
				addr += '<div><h5 style="font-weight: bold;">Billing Address</h5>';
				addr += '<p>'+selection.NAME+'</p>';
				addr += '<p>'+selection.ADDR1+' '+selection.ADDR2+'</p>';
				addr += '<p>'+selection.ADDR3+' '+selection.ADDR4+'</p>';
				addr += '<p>'+selection.STATE+'</p>';
				addr += '<p>'+selection.COUNTRY+' '+selection.ZIP+'</p>';
				addr += '<p>'+selection.COUNTRY+'</p>';
				addr += '<p>'+selection.HPNO+'</p>';
				addr += '<p>'+selection.EMAIL+'</p>';
				addr += '</div>';
				$('#shipbilladd').append(addr);
				}

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
			menuElement.after( '<div class="transportAddBtn footer"  data-toggle="modal" data-target="#transportModal" onclick="document.cpoform.custModal.value=\'\'"><a href="#"> + New Transport </a></div>');
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
			menuElement.after( '<div class="payTermsAddBtn footer"  data-toggle="modal" data-target="#payTermsModal" onclick="document.cpoform.custModal.value=\'\'"><a href="#"> + Add Payment Terms</a></div>');
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
	
	/* Payment Type Auto Suggestion */
//	$('#payment_type').typeahead({
//		  hint: true,
//		  minLength:0,  
//		  searchOnFocus: true
//		},
//		{
//		  display: 'PAYMENTTYPE',  
//		  source: function (query, process,asyncProcess) {
//			  	var urlStr = "../MasterServlet";
//				$.ajax({
//					type : "POST",
//					url : urlStr,
//					async : true,
//					data : {				
//						ACTION : "GET_PAYMENT_TYPE_LIST",
//						QUERY : query
//					},
//					dataType : "json",
//					success : function(data) {
//						return asyncProcess(data.payTypes);
//					}
//				});
//		  },
//		  limit: 9999,
//		  templates: {
//		  empty: [
//		      '<div style="padding:3px 20px">',
//		        'No results found',
//		      '</div>',
//		    ].join('\n'),
//		    suggestion: function(data) {
//		    return '<p>' + data.PAYMENTTYPE + '</p>';
//			}
//		  }
//		}).on('typeahead:render',function(event,selection){
//			var menuElement = $(this).parent().find(".tt-menu");
//			var top = menuElement.height()+35;
//			top+="px";	
//			if(menuElement.next().hasClass("footer")){
//				menuElement.next().remove();  
//			}
//			menuElement.after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#paymentTypeModal"><a href="#"> + New Payment Type</a></div>');
//			menuElement.next().width(menuElement.width());
//			menuElement.next().css({ "top": top,"padding":"3px 20px" });
//			if($(this).parent().find(".tt-menu").css('display') != "block")
//				menuElement.next().hide();
//		  
//		}).on('typeahead:open',function(event,selection){
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",true);
//			element.toggleClass("glyphicon-menu-down",false);
//			$('.employeeAddBtn').show();
//		}).on('typeahead:close',function(){
//			setTimeout(function(){ $('.employeeAddBtn').hide();}, 150);	
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",false);
//			element.toggleClass("glyphicon-menu-down",true);
//		});
	
	/* Payment Mode Auto Suggestion */
	$("#payment_type").typeahead({
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
			menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal" onclick="document.cpoform.custModal.value=\'\'"><a href="#"> + New Payment Mode</a></div>');
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
//					if(pthrough != ""){
//						$("#paid_through_account_name").typeahead('val', '');
//						alert("Please select Bank Balance account type");
//					}
				}
			}
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
						Type : "PURCHASE",
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
			menuElement.after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#orderTypeModal"><a href="#"> + New Order Type</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			$('.employeeAddBtn').show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.employeeAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
//	/* Shipping Customer Auto Suggestion */
//	$('#SHIPPINGCUSTOMER').typeahead({
//		  hint: true,
//		  minLength:0,  
//		  searchOnFocus: true
//		},
//		{
//		  //display: 'CUSTNO',
//			display: 'VNAME',
//		  async: true,   
//		  source: function (query, process,asyncProcess) {
//			  var urlStr = "../MasterServlet";
//				$.ajax( {
//					type : "POST",
//					url : urlStr,
//					async : true,
//					data : {
//						//ACTION : "GET_CUSTOMER_DATA",
//						ACTION : "GET_SUPPLIER_DATA",
//						QUERY : query
//					},
//					dataType : "json",
//					success : function(data) {
//						//return asyncProcess(data.CUSTMST);
//						return asyncProcess(data.VENDMST);
//					}
//				});
//		  },
//		  limit: 9999,
//		  templates: {
//		  empty: [
//		      '<div style="padding:3px 20px">',
//		        'No results found',
//		      '</div>',
//		    ].join('\n'),
//		    suggestion: function(data) {
//		    	return '<div onclick="document.cpoform.SHIPPINGID.value = \''+data.VENDO+'\'"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Mobile: ' + data.HPNO + '</p><br/><p class="item-suggestion">Address: ' + data.ADDR1 + '  '+ data.ADDR2 +'</p><p class="item-suggestion pull-right">Customer TelNo: ' + data.TELNO + '</p></br><p class="item-suggestion"> ' + data.ADDR3 + '  '+ data.ADDR4 +'</p><p class="item-suggestion pull-right">Email: ' + data.EMAIL + '</p></br><p class="item-suggestion"> ' + data.COUNTRY + '  '+ data.ZIP +'</p></div>';
////		    	return '<div onclick="document.cpoform.SHIPPINGID.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
//			}
//		  }
//		}).on('typeahead:render',function(event,selection){
//			var menuElement = $(this).parent().find(".tt-menu");
//			var top = menuElement.height()+35;
//			top+="px";
//			if(menuElement.next().hasClass("footer")){
//				menuElement.next().remove();  
//			} 
//			menuElement.after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#" onclick="document.cpoform.custModal.value=\'shipcust\'"> +  New Shipping Addres</a></div>');
//			menuElement.next().width(menuElement.width());
//			menuElement.next().css({ "top": top,"padding":"3px 20px" });
//			if($(this).parent().find(".tt-menu").css('display') != "block")
//				menuElement.next().hide();
//		}).on('typeahead:open',function(event,selection){
//			$('.shipCustomerAddBtn').show();
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",true);
//			element.toggleClass("glyphicon-menu-down",false);    
//		}).on('typeahead:close',function(){
//			setTimeout(function(){ $('.shipCustomerAddBtn').hide();}, 150);	
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",false);
//			element.toggleClass("glyphicon-menu-down",true);
//		}).on('typeahead:change',function(event,selection){
//			if($(this).val() == ""){
//				document.cpoform.SHIPPINGID.value = "";
//			}
//		});
	
	/* Shipping Customer Auto Suggestion */
//	$('#SHIPPINGCUSTOMER').typeahead({
//		  hint: true,
//		  minLength:0,
//		  searchOnFocus: true
//		},
//		{
//		  display: 'CUSTNO',
//			//display: 'VNAME',
//		  async: true,
//		  source: function (query, process,asyncProcess) {
//			  var urlStr = "../MasterServlet";
//				$.ajax( {
//					type : "POST",
//					url : urlStr,
//					async : true,
//					data : {
//						ACTION : "GET_CUSTOMER_DATA",
//						//ACTION : "GET_SUPPLIER_DATA",
//						QUERY : query
//					},
//					dataType : "json",
//					success : function(data) {
//						return asyncProcess(data.CUSTMST);
//						//return asyncProcess(data.VENDMST);
//					}
//				});
//		  },
//		  limit: 9999,
//		  templates: {
//		  empty: [
//		      '<div style="padding:3px 20px">',
//		        'No results found',
//		      '</div>',
//		    ].join('\n'),
//		    suggestion: function(data) {
//		    	return '<div onclick="document.cpoform.SHIPPINGID.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
//		    	//return '<div onclick="document.cpoform.SHIPPINGID.value = \''+data.VENDO+'\'"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Mobile: ' + data.HPNO + '</p><br/><p class="item-suggestion">Address: ' + data.ADDR1 + '  '+ data.ADDR2 +'</p><p class="item-suggestion pull-right">Customer TelNo: ' + data.TELNO + '</p></br><p class="item-suggestion"> ' + data.ADDR3 + '  '+ data.ADDR4 +'</p><p class="item-suggestion pull-right">Email: ' + data.EMAIL + '</p></br><p class="item-suggestion"> ' + data.COUNTRY + '  '+ data.ZIP +'</p></div>';
//			}
//		  }
//		}).on('typeahead:render',function(event,selection){
//			var menuElement = $(this).parent().find(".tt-menu");
//			var top = menuElement.height()+35;
//			top+="px";
//			if(menuElement.next().hasClass("footer")){
//				menuElement.next().remove();  
//			}
////			menuElement.after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal" onclick="document.cpoform.custModal.value=\'shipcust\'"><a href="#" > + New Shipping Address</a></div>');
//			//menuElement.after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#supplierModal" onclick="document.cpoform.custModal.value=\'shipcust\'"><a href="#" > + New Shipping Address</a></div>');
//			menuElement.next().width(menuElement.width());
//			menuElement.next().css({ "top": top,"padding":"3px 20px" });
//			if($(this).parent().find(".tt-menu").css('display') != "block")
//				menuElement.next().hide();
//		}).on('typeahead:open',function(event,selection){
//			$('.shipCustomerAddBtn').show();
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",true);
//			element.toggleClass("glyphicon-menu-down",false);
//		}).on('typeahead:close',function(){
//			setTimeout(function(){ $('.shipCustomerAddBtn').hide();}, 150);
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",false);
//			element.toggleClass("glyphicon-menu-down",true);
//		}).on('typeahead:change',function(event,selection){
//			if($(this).val() == ""){
//				document.cpoform.SHIPPINGID.value = "";				
//				$('#shipadd').empty();
//				
//				$("input[name=SHIPCONTACTNAME]").val("");
//				$("input[name=SHIPDESGINATION]").val("");
//				$("input[name=SHIPADDR1]").val("");
//				$("input[name=SHIPADDR2]").val("");
//				$("input[name=SHIPADDR3]").val("");
//				$("input[name=SHIPADDR4]").val("");
//				$("input[name=SHIPSTATE]").val("");
//				$("input[name=SHIPZIP]").val("");
//				$("input[name=SHIPWORKPHONE]").val("");
//			}
//			}).on('typeahead:select',function(event,selection){
//				if($(this).val() == ""){
//				$('#shipadd').empty();
//				$('#shipadd').append('');
//			}else{
//				$('#shipadd').empty();
//				var addr = '<div><h5 style="font-weight: bold;">Shipping Address   <a><span data-toggle="modal" data-target="#shipaddr" style="font-size: 15px;font-weight: 450;">Change</span></a></h5>';
//				addr += '<div id="cshipaddr">';
//				addr += '<p>'+selection.SHIPCONTACTNAME+'</p>';
//				addr += '<p>'+selection.SHIPDESGINATION+'</p>';
//				addr += '<p>'+selection.SHIPADDR1+' '+selection.SHIPADDR2+'</p>';
//				addr += '<p>'+selection.SHIPADDR3+' '+selection.SHIPADDR4+'</p>';
//				addr += '<p>'+selection.SHIPSTATE+' '+selection.SHIPCOUNTRY+' '+selection.SHIPZIP+'</p>';
//				addr += '<p>'+selection.SHIPHPNO+'</p>';
//				addr += '<p>'+selection.SHIPWORKPHONE+'</p>';
//				addr += '<p>'+selection.SHIPEMAIL+'</p>';
//				addr += '</div>';
//				addr += '</div>';
//				$('#shipadd').append(addr);
//				
//				$("input[name=SHIPCONTACTNAME]").val(selection.SHIPCONTACTNAME);
//				$("input[name=SHIPDESGINATION]").val(selection.SHIPDESGINATION);
//				$("input[name=SHIPADDR1]").val(selection.SHIPADDR1);
//				$("input[name=SHIPADDR2]").val(selection.SHIPADDR2);
//				$("input[name=SHIPADDR3]").val(selection.SHIPADDR3);
//				$("input[name=SHIPADDR4]").val(selection.SHIPADDR4);
//				$("input[name=SHIPSTATE]").val(selection.SHIPSTATE);
//				$("input[name=SHIPZIP]").val(selection.SHIPZIP);
//				$("input[name=SHIPWORKPHONE]").val(selection.SHIPWORKPHONE);
//				$("input[name=SHIPCOUNTRY]").val(selection.SHIPCOUNTRY);
//				$("input[name=SHIPHPNO]").val(selection.SHIPHPNO);
//				$("input[name=SHIPEMAIL]").val(selection.SHIPEMAIL);				
//				
//			}
//		});

/* Shipping Customer Auto Suggestion */
	$('#SHIPPINGCUSTOMER').typeahead({
		  hint: true,
		  minLength:0,
		  searchOnFocus: true
		},
		{
		  display: 'CUSTNO',
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
		    	return '<div onclick="document.cpoform.SHIPPINGID.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
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
			menuElement.after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal" onclick="document.cpoform.custModal.value=\'shipcust\'"><a href="#" > + New Shipping Address</a></div>');
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
				document.cpoform.SHIPPINGID.value = "";				
				$('#shipadd').empty();
				
				$("input[name=SHIPCONTACTNAME]").val("");
				$("input[name=SHIPDESGINATION]").val("");
				$("input[name=SHIPADDR1]").val("");
				$("input[name=SHIPADDR2]").val("");
				$("input[name=SHIPADDR3]").val("");
				$("input[name=SHIPADDR4]").val("");
				$("input[name=SHIPSTATE]").val("");
				$("input[name=SHIPZIP]").val("");
				$("input[name=SHIPWORKPHONE]").val("");
			}
			}).on('typeahead:select',function(event,selection){
				if($(this).val() == ""){
				$('#shipadd').empty();
				$('#shipadd').append('');
			}else{
				$('#shipadd').empty();
				var addr = '<div><h5 style="font-weight: bold;">Shipping Address   <a><span data-toggle="modal" data-target="#shipaddr" onclick="getshipaddr();" style="font-size: 15px;font-weight: 450;">Change</span></a></h5>';
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
	
	/* Project Customer Auto Suggestion */
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
			return '<div onclick="setCurrencyid(\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')"><p>'+data.CURRENCY+'</p></div>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
	
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			$("input[name ='CURRENCYID']").val("");	
	});
	

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
	
	$(".po-table tbody").on('click','.bill-action',function(){
	   // var obj = $(this).closest('tr').find('td:nth-child(9)');
	   // calculateTax(obj, "", "", "");
		$(this).parent().parent().remove();
	    setLineNo();
	    calculateTotal();
	});
	
	$("#remarks-table tbody").on('click','.remark-action',function(){
	    $(this).parent().parent().remove();
	});
	
	$("#purchaseAttch").change(function(){
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
		var isValidOrd = validateOrderNo();
		if (isValidOrd){
		var isValid = validatePurchaseOrder();
		if(isValid){
			$( "#purchaseOrderForm" ).submit();
		}
		}		
	});
	
	$("#btnSalesOpen").click(function(){
		$('input[name ="orderstatus"]').val('Open');
		var isValidOrd = validateOrderNo();
		if (isValidOrd){
		var isValid = validatePurchaseOrder();
		if(isValid){
			$( "#purchaseOrderForm" ).submit();
		}
		}		
	});
	
	$("#btnSalesOpenEmail").click(function(){
		if ($('#supplier_email').val() == ''){
			alert('Supplier record does not have an email address. Please update supplier email and try again or Save as Draft OR Save as Open');
			return;
		}
		$('input[name ="orderstatus"]').val('Open');
		var isValidOrd = validateOrderNo();
		if (isValidOrd){
		var isValid = validatePurchaseOrder();
		if(isValid){
			var data = new FormData($("#purchaseOrderForm")[0]); //$("#purchaseOrderForm").serialize();
			$.ajax({
				type : "POST",
				url : "../purchaseorder/save",
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
												.replace(/\{ORDER_NO\}/, $('#PONO').val())
												);
						$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(
									$('#template_body').val()
									.replace(/\{ORDER_NO\}/, $('#PONO').val())
									.replace(/\{SUPPLIER_NAME\}/, $('#vendname').val())
									);
						$('#send_attachment').val('Purchase Order');
					}else{
						$('.success-msg').html(data.MESSAGE).addClass('error-msg').removeClass('success-msg').css('display', 'inline');
					}
		        		
				}
			});
			}
		}
	});
	
	$("input[name=QTY]").mouseenter(function(){
		var content = "<p class='text-left'>Min Stock Quantity: "+$(this).data("rl")+"</p>";
		content += "<p class='text-left'>Max Stock Quantity: "+$(this).data("msq")+"</p>";
		content += "<p class='text-left'>Stock on Hand: "+$(this).data("soh")+"</p>";
		content += "<p class='text-left'>Estimated Quantity: "+$(this).data("eq")+"</p>";
		content += "<p class='text-left'>Available Quantity: "+$(this).data("aq")+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"}); 
	});
	
	$("input[name=ITEMDES]").mouseenter(function(){
		var content = "<p class='text-left'> "+$(obj).closest('tr').find("input[name=ITEMDES]").val()+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"});
	});

	$("input[name=unitprice]").mouseenter(function(){
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
	

	
	$("#btnSaveRemarks").click(function(){
		var data = $("#remarksForm").serialize();
		$.ajax({
			type : "POST",
			url : "../purchaseorder/addRemarks",
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
	
	$('select[name="PURCHASE_LOC"]').on('change', function(){		
	    var text = $("#PURCHASE_LOC option:selected").text();
	    $("input[name ='COUNTRY']").val(text.trim());
	});
	
	$(document).on("focusout","input[name ='CURRENCYUSEQT']",function(){
		var CURRENCYUSEQTCost = parseFloat($("input[name ='CURRENCYUSEQT']").val());
		if(!Number.isNaN(CURRENCYUSEQTCost))
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});
	
	$("input[name=QTY]").focusout(function(){
		alert("ok");
		var value = $(this).val();
		var rcqry = $(this).closest('tr').find("input[name=RecQty]").val();
		var plno = $(this).closest('tr').find("input[name=polnno]").val();
		alert(value);
		alert(rcqry);
		alert(plno);
		if(plno != "0"){
			if(value < rcqry){
				alert("Quantity should not be less than received quantity, Received quantity = "+rcqry);
				return false;
			}
		}
	});
	
	addSuggestionToTable();

});
function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
	}
function generatePdfMail(dataUrl,attachName){	
	formData = new FormData();
	formData.append("PONO", $("#PONO").val());
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
function checkqty(obj){
	var value = $(obj).val();
	var rcqry = $(obj).closest('tr').find("input[name=RecQty]").val();
	var plno = $(obj).closest('tr').find("input[name=polnno]").val();
	if(plno != "0"){
		if(parseFloat(value) < parseFloat(rcqry)){
			alert("Quantity should not be less than received quantity, Received quantity = "+rcqry);
			$(obj).val(rcqry);
			return false;
		}
	}
	calculateAmount(obj);
}

function getvendname(TAXTREATMENT,TRANSPORTNAME,TRANSPORTID,PAYMENT_TERMS,PAY_TERMS,VENDO,PAYMENTTYPE,NAME,TELNO,EMAIL,ADD1,ADD2,ADD3,ADD4,REMARKS,COUNTRY,ZIP,CURRENCYID,CURRENCY,CURRENCYUSEQT){
	//document.getElementById('nTAXTREATMENT').innerHTML = TAXTREATMENT;
	$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
	if(TAXTREATMENT =="GCC VAT Registered"||TAXTREATMENT=="GCC NON VAT Registered"||TAXTREATMENT=="NON GCC")
	{
		document.getElementById('CHK1').style.display = 'block';
	}
	else
		document.getElementById('CHK1').style.display = 'none';
	$('input[name ="PERSON_INCHARGE"]').val(NAME);
	document.cpoform.vendno.value =VENDO;
	$("input[name=TELNO]").val(TELNO);
	$("input[name=EMAIL]").val(EMAIL);
	$("input[name=ADD1]").val(ADD1);
	$("input[name=ADD2]").val(ADD2);
	$("input[name=ADD3]").val(ADD3);
	$("input[name=REMARK2]").val(REMARKS);
	$("input[name=ADD4]").val(ADD4);
	$("input[name=COUNTRY]").val(COUNTRY);
	$("input[name=ZIP]").val(ZIP);
	$("input[name=PAYMENTTYPE]").val(PAYMENTTYPE);
	$("input[name=transport]").val(TRANSPORTNAME);
	$("input[name=TRANSPORTID]").val(TRANSPORTID);
	$("input[name=payment_type]").val(PAY_TERMS);
	$("input[name=payment_terms]").val(PAYMENT_TERMS);	
	setCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT);

}

function headerReadable(){
	if(document.cpoform.DATEFORMAT.checked)
	{
		
		document.cpoform.DELDATE.value="";
		$('#DELDATE').attr('readonly',true).datepicker({ dateFormat: 'dd/mm/yy'});
		
	}
	else{
		document.cpoform.DELDATE.value="";
		 $('#DELDATE').attr('readonly',false).datepicker("destroy");

	}
}

//RESVI
function autoGen(){
	$.ajax({
		type: "GET",
		url: "../purchaseorder/Auto-Generate",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#PONO").val(data.PONO);
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
/*----------------------------------*/

function setITEMDESC(obj,desc){
		$(obj).closest('tr').find("input[name ='ITEMDES']").val(desc);
	}

function addSuggestionToTable(){
	var plant = document.cpoform.plant.value;
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
			//return '<div onclick="loadItemData(this,\''+ data.ITEM+'\',\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.PURCHASEUOM+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' '+data.UOM+'</p></div>';
			//	return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.PURCHASEINVQTY+' '+data.PURCHASEUOM+'</p></div>';
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
			loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.PURCHASEUOM);
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 150);	
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				$(this).closest('tr').find('input[name="ITEMDES"]').val("");
			}
		});
		
		//Imthiyas added (press Enter Key for item to appear)
			$(".itemSearch").on('keydown', function(event) {
			    if (event.key === 'Enter') {
			        const item = $(this).val();
			        $.ajax({
			            type: "POST",
			            url: "../ItemMstServlet",
			            data: {
			               ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				           CUSTNO : $("input[name=CUST_CODE]").val(),
			                ITEM: item
			            },
			            dataType: "json",
			            success: function(data) {
			                if (data.items && data.items.length > 0) {
			                    const selection = data.items[0];
			                    loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.PURCHASEUOM);
			                    $(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
			                    $(this).closest('tr').find("input[name='item']").val(selection.ITEM);
			                } else {
			                    $(this).closest('tr').find('input[name="ITEMDES"]').val("");
			                    $(this).closest('tr').find('input[name="item"]').val("");
//			                    alert('No item found.');
			                }
			            }.bind(this),
			            error: function() {
			                alert('Error fetching item data.');
			            }
			        });
			        event.preventDefault();
			    }
			});
			//Imthiyas end
	
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
	
	/* To get NEW the suggestion data for Tax */
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
					ACTION : "GET_GST_TYPE_DATA_PO",
					SALESLOC : $("input[name=STATE_PREFIX]").val(),
					GST_PERCENTAGE : $("input[name=GST]").val(),
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
			return '<p class="item-suggestion">'+data.UOM+'</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:select',function(event,selection){
			//loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.PURCHASEUOM);
			CheckPriceVal(this,selection.UOM)
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
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
	body += '<td><input type="Checkbox" style="border:0;background=#dddddd"	name="chkdPONO" value="">';
	body += '</td>';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">';
	body += '<input type="hidden" name="lnno" value="1">';
	body += '<input type="hidden" name="prlnno" value="0">';
	body += '<input type="hidden" name="itemdesc" value="">';
	body += '<input type="hidden" name="unitpricerd" value="">';
	body += '<input type="hidden" name="minstkqty" value="">';
	body += '<input type="hidden" name="maxstkqty" value="">';
	body += '<input type="hidden" name="stockonhand" value="">';
	body += '<input type="hidden" name="incommingqty" value="">';
	body += '<input type="hidden" name="outgoingqty" value="">';
	body += '<input type="hidden" name="customerdiscount" value="">';
	body += '<input type="hidden" name="unitpricediscount" value="">';
	body += '<input type="hidden" name="discounttype" value="">';
	body += '<input type="hidden" name="customerdiscount" value="">';
	body += '<input type="hidden" name="minsp" value="">';
	body += '<input type="hidden" name="RecQty" value="0">';
	body += '<input type="hidden" name="polnno" value="0">';
	body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+taxdisplay+'">';
	body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
	body += '</td>';
	body += '<td class="bill-item">';
	//body += '<input type="text" name="item" class="form-control itemSearch" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<input type="text" name="item" class="form-control itemSearch" onchange="checkitems(this.value,this)" style="width:87%" placeholder="Type or click to select an item.">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';	
	body += '<td>';
	body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Account" value="Inventory Asset">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="UOM" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="UOM">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" name="QTY" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="1.000" onchange="checkqty(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
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
//	body += '<td class="item-tax">';
//	body += '<input type="hidden" name="tax">';
//	body += '<input type="text" name="tax_type" class="form-control taxSearch" value="'+taxdisplay+'" placeholder="Select a Tax" readonly>';
//	body += '</td>';
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
	$(".po-table tbody").append(body);
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
function loadItemDescToolTip(obj){
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip("destroy");
	var content = "<p class='text-left'>"+$(obj).closest('tr').find("input[name=ITEMDES]").data("pd")+"</p>";
	//$(obj).closest('tr').find("input[name=ITEMDES]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}

//table validation
function checkitems(itemvalue,obj){	
//	 if(itemvalue=="" || itemvalue.length==0 ) {
//		alert("Enter Item!");
//		document.cpoform.item.value;
//		return false;
//	}else{
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
						//document.cpoform.item.value = "";
						$(obj).typeahead('val', '');
						$(obj).parent().find('input[name="item"]').focus();
						//document.getElementById("item").value="";
						return false;	
						
					} 
					else 
						return true;
				}
			});
		 return true;
//		}	
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
						$(obj).parent().find('input[name="UOM"]').focus();
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
		var urlStr = "../purchaseorder/CheckOrderno";
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
						document.getElementById("PONO").focus();
						document.getElementById("PONO").value="";
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function validateOrderNo(){
	var orderno = $("input[name=PONO]").val();
	if(orderno != ""){	
		var urlStr = "../purchaseorder/CheckOrderno";
		$.ajax( {
			type : "GET",
			url : urlStr,
			async : false,
			data : {
				ORDERNO : orderno,
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						alert("Order Number Already Exists");
						document.getElementById("PONO").focus();
						document.getElementById("PONO").value="";
						return false;	
					} 
					else 
						return true;
				}
			});
			}
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
						document.getElementById("payment_type").focus();
						$("#payment_type").typeahead('val', '');
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
						document.getElementById("INCOTERMS").focus();
						$("#INCOTERMS").typeahead('val', '');
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

//supplier popup changes
function checksuppliercurrency(currency){	
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
						document.getElementById("SUP_CURRENCY").focus();
						$("#SUP_CURRENCY").typeahead('val', '');
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

function getPreviousPurchaseOrderDetails(obj) {
	var item = $(obj).parent().parent().find('input[name=item]').val();
	var precost = $("input[name=precost]").val();
	if(precost == "1"){
		if(item != ''){
		var uom = document.cpoform.UOM.value;
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
				$("#lastTranPriceModal").modal();
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
	}else{
		alert("Not allowed to view previous purchase orders cost details");
	}
}

function loadItemData(obj, productId, catalogPath, account, cost, salesuom){
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(cost));
	$(obj).closest('tr').find("input[name=UOM]").val(salesuom);
	$(obj).closest('tr').find('input[name = "account_name"]').val(account);
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var currencyID = $("input[name=CURRENCYID]").val();
	var custCode = $("input[name=CUST_CODE]").val();
	var currencyUSEQT = $("input[name=CURRENCYUSEQT]").val();
	
	 var urlStr = "/track/ItemMstServlet";
	 var discount;
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				CURRENCY : currencyID,
				VENDNO : document.cpoform.vendno.value,
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
								price = parseFloat(resultVal.ConvertedUnitCost-(resultVal.incomingIBDiscount*currencyUSEQT));
								//price = parseFloat(resultVal.incomingIBDiscount);
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
					$(obj).closest('tr').find("input[name=ITEMDES]").data('pd',resultVal.prdesc);
					
					calculateAmount(obj);
					loadQtyToolTip(obj);
					loadUnitPriceToolTip(obj);
					loadItemDescToolTip(obj);
					$(obj).closest('tr').find('input[name="QTY"]').focus();
					$(obj).closest('tr').find('input[name="QTY"]').select();
				}
			});
		
	}

/*function CheckPriceVal(obj, uom) {
	var productId = $(obj).closest('tr').find("td:nth-child(2)").find('input[name=item]').val();
	var dono = $("input[name=DONO]").val();
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
}*/

function CheckPriceVal(obj, uom) {
	var productId = $(obj).closest('tr').find('input[name=item]').val(); //Author: Azees  Create date: July 23,2021  Description: UOM Change Issue
	var pono = $("input[name=DONO]").val();
	var desc = "";
    var disc = $(obj).closest('tr').find("input[name=customerdiscount]").val();
    var numberOfDecimal = $("input[name=numberOfDecimal]").val();
    var currencyUSEQT = $("input[name=CURRENCYUSEQT]").val();
    var currencyID = $("input[name=CURRENCYID]").val();
	if((productId=="" || productId.length==0) && (desc == "" ||desc.length == 0)) {
		alert("Enter Product ID/Description !");
		document.cpoform.ITEM.focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				DESC : desc,
				PLANT :document.cpoform.plant.value,
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
							$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
						}else{
							$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricerd]").val(resultVal.ConvertedUnitCostWTC.match(regex)[0]);
						}
						//alert(disc);
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
								//cprice = parseFloat(resultVal.ConvertedDiscWTC);
	 						}
	                    	var calAmount = parseFloat(price).toFixed(numberOfDecimal);						
							$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(calAmount).toFixed(numberOfDecimal));
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


function loadQtyToolTip(obj){
	$(obj).closest('tr').find("input[name=QTY]").tooltip("destroy");
	var content = "<p class='text-left'>Min Stock Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("rl")+"</p>";
	content += "<p class='text-left'>Max Stock Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("msq")+"</p>";
	content += "<p class='text-left'>Stock on Hand: "+$(obj).closest('tr').find("input[name=QTY]").data("soh")+"</p>";
	content += "<p class='text-left'>Incomming Quantity: "+$(obj).closest('tr').find("input[name=incommingqty]").val()+"</p>";
	content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(obj).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
	content += "<p class='text-left'>Sales Estimate Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("eq")+"</p>";
	content += "<p class='text-left'>Available Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("aq")+"</p>";	
	//$(obj).closest('tr').find("input[name=QTY]").tooltip({title: content, html: true, placement: "top"}); 
	$(obj).closest('tr').find("input[name=QTY]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}

function loadUnitPriceToolTip(obj){
$(obj).closest('tr').find("input[name=unitprice]").tooltip("destroy");
	
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
	
	//$(obj).closest('tr').find("input[name=unitprice]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=unitprice]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}

function showRemarksDetails(obj) {
	var lnno = $(obj).closest('tr').find("input[name=lnno]").val();
	var pono = $("input[name=PONO]").val();
	var item = $(obj).closest('tr').find("input[name=item]").val();
	if(pono == ''){
		alert("Please generate purchase order number");
	}else if(item != ''){
		$.ajax({
			type : "GET",
			url : "../purchaseorder/getPurchaseOrderRemarks",
			data : {
				ITEM: item,
				PONO: pono,
				POLNO: lnno
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
						body += '<input type="text" class="form-control" name="remarks" value="'+remkval+' "placeholder="Max 100 Characters" maxlength="100">';
						body += '</td>';
						body += '</tr>';
					}
				}else{
					body += '<tr>';
					body += '<td style="position:relative;">';
					body += '<input type="text" class="form-control" name="remarks" placeholder="Max 100 Characters" maxlength="100">';
					body += '</td>';
					body += '</tr>';
				}
				body += '<input type="hidden" name="r_item" value="'+item+'">';
				body += '<input type="hidden" name="r_pono" value="'+pono+'">';
				body += '<input type="hidden" name="r_lnno" value="'+lnno+'">';
				$("#remarks-table tbody").html(body);
			}
		});
		
		$("#remarksModal").modal();
	}else{
		alert("Please select Product");
	}
	/*var item = $(obj).parent().parent().find('input[name=item]').val();
	
	if(item != ''){
	var uom = document.cpoform.UOM.value;
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
	
	
}

function addRemarksRow(){
	var body="";
	body += '<tr>';
	body += '<td style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle remark-action" aria-hidden="true"></span>';
	body += '<input type="text" class="form-control" name="remarks" placeholder="Max 100 Characters" maxlength="100">';
	body += '</td>';
	body += '</tr>';
	$("#remarks-table tbody").append(body);
}

function setLineNo(){
	var i=1;
	$(".po-table tbody tr td:first-child").each(function() {
		$(this).find('input[name=lnno]').val(i);
		$(this).find('input[name=chkdPONO]').val(i-1);
		i++;
	});
}

function onAddSupplier(){
	   var CUST_CODE   = document.form1.CUST_CODE.value;
	   var CUST_NAME   = document.form1.CUST_NAME.value;
	   var PEPPOL   = document.form1.PEPPOL.value;
	   var PEPPOL_ID   = document.form1.PEPPOL_ID.value;
	   var companyregnumber   = document.form1.companyregnumber.value;
	   var TAXTREATMENT   = document.form1.TAXTREATMENT.value;
	   var RCBNO   = document.form1.RCBNO.value;
	   var CURRENCY = document.form1.SUP_CURRENCY.value;
	   var region = document.form1.COUNTRY_REG.value;

	   var rcbn = RCBNO.length;
	   
	   var ValidNumber   = document.form1.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" suppliers you can create"); return false; }
	   
	   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Supplier ID");;document.form1.CUST_CODE.focus(); return false; }
	   
	   if(CUST_NAME == "" || CUST_NAME == null) {
	   alert("Please Enter Supplier Name"); 
	   document.form1.CUST_NAME.focus();
	   return false; 
	   }
	   
	    if(document.getElementById("PEPPOL").checked == true)
			PEPPOL="1";
		 else 
			PEPPOL="0";

		   if(PEPPOL_ID == "" &&  PEPPOL == "1") {
		 	  alert("Please Enter Peppol Id"); 
		 	return false; 
		}
	   
	   
//         Author Name:Resviya ,Date:9/08/21 , Description -UEN Alert    

	   if(region == "GCC"){
		   document.form1.companyregnumber.value="";
		}else if(region == "ASIA PACIFIC"){
			if (companyregnumber == "" || companyregnumber == null) {
			alert("Please Enter Unique Entity Number (UEN)");
			document.form1.companyregnumber.focus();
			return false; 
			}
		}

//	   END
	   
//		 <!-- Author Name:Resviya ,Date:19/07/21 -->

		 if(CURRENCY == "" || CURRENCY == null) {
			 alert("Please Enter Currency ID"); 
			 document.form1.SUP_CURRENCY.focus();
			 return false; 
			 }
//		 End
		 
	   if(document.form1.TAXTREATMENT.selectedIndex==0)
		{
		alert("Please Select TAXTREATMENT");
		document.form1.TAXTREATMENT.focus();
		return false;
		}
	   if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	   {
	   var  d = document.getElementById("TaxByLabel").value;
	   	if(RCBNO == "" || RCBNO == null) {
	   		
		   alert("Please Enter "+d+" No."); 
		   document.form1.RCBNO.focus();
		   return false; 
		   }
	   	//if(document.form1.COUNTRY_REG.value=="GCC")// region based validtion
		//{
		if(!IsNumeric(RCBNO))
		{
	    alert(" Please Enter "+d+" No. Input In Number"); 
	   	document.form1.RCBNO.focus();
	   	return false; 
	  	}

		if("15"!=rcbn)
		{
		alert(" Please Enter your 15 digit numeric "+d+" No."); 
			document.form1.RCBNO.focus();
			return false; 
			}
		//}
	   }
	else if(50<rcbn)
	{
	   var  d = document.getElementById("TaxByLabel").value;
	   alert(" "+d+" No. length has exceeded the maximum value"); 
	   document.form1.RCBNO.focus();
	   return false; 
	 }
	   if(!IsNumeric(form1.SUP_PMENT_DAYS.value))
	   {
	     alert(" Please Enter Days In Number");
	     form1.SUP_PMENT_DAYS.focus();  form1.SUP_PMENT_DAYS.select(); return false;
	   }
	   if(document.form1.COUNTRY_CODE_S.selectedIndex==0)
		{
		   alert("Please Select Country from Address");
		   document.form1.COUNTRY_CODE_S.focus();
		 return false;
		}
	   
	   
//	   document.getElementById('nTAXTREATMENT').innerHTML="";
		$("select[name ='nTAXTREATMENT']").val("");
		var rc="0";
		var gd="0";
		if(document.getElementById("REVERSECHARGE").checked == true)
			rc="1";
		if(document.getElementById("GOODSIMPORT").checked == true)
			gd="1";
			
		/* if(pono!="")
	   		document.form1.action  = "/track/CreateSupplierServlet?action=ADD&reurl=createBill.jsp?action=View&PONO="+pono+"&GRNO="+grno+"&REVERSECHARGE="+rc+"&GOODSIMPORT="+gd+"&isgrn="+isgrn;
		else
			document.form1.action  = "/track/CreateSupplierServlet?action=ADD&reurl=createBill";
	   document.form1.submit(); */
	   
		var datasend = $('#formsupplierid').serialize();
	   
		var urlStr = "/track/CreateSupplierServlet?action=JADD&reurl=createBill";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : datasend,
		dataType : "json",
		success : function(data) {
			/* console.log(data);*/
			//alert(data.supplier[0].SID);
			if(document.cpoform.custModal.value == "cust"){
			$("input[name ='vendno']").val(data.supplier[0].SID);
			/* document.getElementById("evendcode").value  = data.supplier[0].SID; */
			$("input[name ='vendname']").val(data.supplier[0].SName);
			$("input[name ='transport']").val(data.supplier[0].TRANSPORTNAME);
			$("input[name ='TRANSPORTID']").val(data.supplier[0].TRANSPORTID);
			$("input[name ='payment_type']").val(data.supplier[0].PAYMENT_TERMS);
			$("input[name ='payment_terms']").val(data.supplier[0].PAY_TERMS);
			$('select[name ="nTAXTREATMENT"]').val(data.supplier[0].sTAXTREATMENT);
			setCurrencyid(data.supplier[0].CURRENCY,data.supplier[0].sCURRENCY_ID,data.supplier[0].CURRENCYUSEQT);
			$('#nTAXTREATMENT').attr('disabled',false);
			if(data.supplier[0].sTAXTREATMENT =="GCC VAT Registered"||data.supplier[0].sTAXTREATMENT=="GCC NON VAT Registered"||data.supplier[0].sTAXTREATMENT=="NON GCC")
			{
				document.getElementById('CHK1').style.display = 'block';
			}
			else
				document.getElementById('CHK1').style.display = 'none';

			document.getElementById("REVERSECHARGE").checked = false;
			document.getElementById("GOODSIMPORT").checked = false;
			

			$('#shipbilladd').empty();
			var addr = '<div><h5 style="font-weight: bold;">Billing Address</h5>';
			addr += '<p>'+data.supplier[0].NAME+'</p>';
			addr += '<p>'+data.supplier[0].ADDR1+' '+data.supplier[0].ADDR2+'</p>';				
			addr += '<p>'+data.supplier[0].ADDR3+' '+data.supplier[0].ADDR4+'</p>';
			addr += '<p>'+data.supplier[0].STATE+'</p>';
			addr += '<p>'+data.supplier[0].COUNTRY+' '+data.supplier[0].ZIP+'</p>';
			addr += '<p>'+data.supplier[0].HPNO+'</p>';
			addr += '<p>'+data.supplier[0].EMAIL+'</p>';
			addr += '</div>';
			$('#shipbilladd').append(addr);
			
			document.form1.reset();
			$('#supplierModal').modal('hide');
			}else{
				document.form1.reset();
				document.cpoform.SHIPPINGCUSTOMER.value = data.supplier[0].SName;
				document.cpoform.SHIPPINGID.value = data.supplier[0].SID;
				$('#supplierModal').modal('hide');
			}
		}
		});
	   
	}

function OnChange(dropdown)
{
/*    var myindex  = dropdown.selectedIndex;
    var SelValue = dropdown.options[myindex].value;
    document.form1.STATE_PREFIX.value = SelValue;
    changetax();*/
}

function onAdd(){
	 var CUST_CODE   = document.form.CUST_CODE_C.value;
	 var CUST_NAME   = document.form.CUST_NAME_C.value;
	 var PEPPOL_C  = document.form.PEPPOL_C.value;
	 var PEPPOL_IDC   = document.form.PEPPOL_IDC.value;
	 var companyregnumber   = document.form.cus_companyregnumber.value;
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
		   document.form.companyregnumber.value="";
		}else if(region == "ASIA PACIFIC"){
			if (companyregnumber == "" || companyregnumber == null) {
			alert("Please Enter Unique Entity Number (UEN)");
			document.form.companyregnumber.focus();
			return false; 
			}
		}

//	   END
	   
	 //	 <!-- Author Name:Resviya ,Date:19/07/21 -->

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
	 /* if(this.cpoform.ISCREDITLIMIT.checked == true && CL == ""){
		   alert("Please Enter Credit Limit");
		   document.cpoform.CREDITLIMIT.focus();
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
			   document.cpoform.CREDITLIMIT.focus();
			   return false; 
		  }	 */
	 
	 /* document.cpoform.action  = "/track/CreateCustomerServlet?action=ADD&reurl=createInvoice";
	 document.cpoform.submit(); */
	 var datasend = $('#formCustomer').serialize();
	   
	  
	   var urlStr = "/track/CreateCustomerServlet?action=JADD&reurl=createInvoice";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : datasend,
		dataType : "json",
		success : function(data) {
				document.form.reset();
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
				
				$('#shipadd').empty();
				var addr = '<div><h5 style="font-weight: bold;">Shipping Address   <a><span data-toggle="modal" data-target="#shipaddr" onclick="getshipaddr();" style="font-size: 15px;font-weight: 450;">Change</span></a></h5>';
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
				$('#shipadd').append(addr);
				document.cpoform.SHIPPINGCUSTOMER.value = "";
				document.cpoform.SHIPPINGCUSTOMER.value = data.customer[0].CName;
				document.cpoform.SHIPPINGID.value = data.customer[0].CID;
				$('#customerModal').modal('hide');
		}
		});
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

function onIDGenSupplier()
{
	var plant = document.cpoform.plant.value;
	var urlStr = "/track/CreateSupplierServlet";
	$.ajax( {
	type : "POST",
	url : urlStr,
	async : true,
	data : {
		PLANT : plant,
		action : "JAuto-ID",
		reurl : "createBill"
	},
	dataType : "json",
	success : function(data) {
		
		$("input[name ='CUST_CODE']").val(data.supplier[0].SID);
		$("input[name ='CUST_CODE1']").val(data.supplier[0].SID);
		
	}
	});
   
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
	
	if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	{
		$("#TaxLabel").addClass("required");
		$("#TaxLabelCust").addClass("required");
	}
	else {
		$("#TaxLabel").removeClass("required");
		$("#TaxLabelCust").removeClass("required");
	}
}
function OnBank(BankName)
{
	var plant = document.cpoform.plant.value;
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_BANK_DATA",
			PLANT : plant,
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
function OnCountry(Country)
{
	var plant = document.cpoform.plant.value;
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

function OnCountrys(Country)
{
	var plant = document.cpoform.plant.value;
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

function checkitemcus(aCustCode)
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
		var email =document.form.EMAIL.value;
		var country_code = document.getElementById("COUNTRY_CODE_C").value;
		SHIPCUST_OnCountry(country_code);
		var scountry = document.form.COUNTRY.value;
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
	body += '<input type="hidden" name="MANAGER_APP_VAL" value="0">';
	body += '<input type="Checkbox" style="border:0;background=#dddddd"	name="MANAGER_APP" value="1" onclick="checkManagerApp(this)">';
	body += '<span class="glyphicon glyphicon-remove-circle user-action" aria-hidden="true" style="right: -90px;"></span>';    	
	body += '</td>';
	body += '</tr>';
	$(".user-table tbody").append(body);
}

function employeeCallback(data){
	if(data.STATUS="SUCCESS"){
		//alert(data.MESSAGE);
		$("#EMP_NAME").typeahead('val', data.EMP_NAME);
	}
}

function paymentTypeCallback(data){
	if(data.STATUS="SUCCESS"){
//		$("#payment_type").typeahead('val', data.PAYMENTMODE);
//		$("#payment_type").typeahead('val', data.PAYMENTTYPE);
	if(document.cpoform.custModal.value=="shipcust"){
		$("#CUST_PAYTERMS").typeahead('val', data.PAYMENTMODE);
		}else if(document.cpoform.custModal.value=="cust"){
		$("#PAYTERMS").typeahead('val', data.PAYMENTMODE);
		}else{
		$("#payment_type").typeahead('val', data.PAYMENTMODE);
		}
	}
}

//function transportCallback(data){
//	if(data.STATUS="SUCCESS"){
//		$("#transport").typeahead('val', data.transport);
//		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
//	}
//}

function transportCallback(data){
	if(data.STATUS="SUCCESS"){
	if(document.cpoform.custModal.value=="shipcust"){
		$("#transportC").typeahead('val', data.transport);
		$("input[name=TRANSPORTIDC]").val(data.TRANSPORTID);
		}else if(document.cpoform.custModal.value=="cust"){
		$("#transports").typeahead('val', data.transport);
		$("input[name=TRANSPORTSID]").val(data.TRANSPORTID);
		}else{
		$("#transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
		}
	}
}

function payTermsCallback(payTermsData){
	if(payTermsData.STATUS="SUCCESS"){
//		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
//		}
	if(document.cpoform.custModal.value=="shipcust"){
		$("input[name ='payment_term']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=PMENT_DAYS]").val(payTermsData.NO_DAYS);
		}else if(document.cpoform.custModal.value=="cust"){
		$("input[name ='sup_payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=SUP_PMENT_DAYS]").val(payTermsData.NO_DAYS);
		}else{
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		}
	}
}

function orderTypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#ORDERTYPE").typeahead('val', data.ORDERTYPE);
	}
}

function incotermsCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#INCOTERMS").typeahead('val', data.INCOTERM);
	}
}

function removetaxtrestl(){
	$('select[name=TAXTREATMENT]').attr('disabled',false);
}


function validatePurchaseOrder(){
	var supplier = $("input[name=vendname]").val();
	var pono = $("input[name=PONO]").val();
	var currency = $("input[name=CURRENCY]").val();
	var CURRENCYUSEQT = $("input[name=CURRENCYUSEQT]").val();
//	var currencyfoc = document.cpoform.CURRENCY.focus();
	var msg = "";
	var isItemValid = true, isAccValid = true, isUnitPriceValid = true,
	isUnitPriceValid = true;
	
	var ValidNumber   = document.cpoform.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" order's you can create"); return false; }
	
	if(supplier == ""){
		alert("Please select a Supplier.");
		return false;
	}	
	if(pono == ""){
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
	var checkedFlag = false;
	$(".po-table tbody tr").each(function() {
		if($(this).find('input[name=chkdPONO]').is(':checked')){
			checkedFlag = true;
			var issueQty = $(this).find('input[name=QTY]').val();
			if (issueQty == "" || issueQty.length == 0 || issueQty == '0') {
				alert("Enter a valid Quantity!");
				$(this).find('input[name=QTY]').focus();
				$(this).find('input[name=QTY]').select();
		        return false;
			}
			if(!isNumericInput(issueQty)){
				alert("Entered Quantity is not a valid number!");
				$(this).find('input[name=QTY]').focus();
				$(this).find('input[name=QTY]').select();
		        return false;
			}
			var costamt = issueQty;
			  if (costamt.indexOf('.') == -1) costamt += ".";
				var cdecNum = costamt.substring(costamt.indexOf('.')+1, costamt.length);
				if (cdecNum.length > 3){
					alert("Invalid more than 3 digits after decimal in QTY");	
					$(this).find('input[name=QTY]').focus();
					$(this).find('input[name=QTY]').select();
					return false;
				}
				
		}
	});	
	if(checkedFlag){
		return true;
	}else{
		alert ("Please check at least one checkbox.");
		return false;	
	}
	return true;
}

function getEditPODetails(dono){
	var plant = document.cpoform.plant.value;
	$.ajax({
		type : "POST",
		url : "../salesorder/LoadEditDetails?DONO="+dono,
		async : true,
		data : {
			PLANT : plant,
		},
		dataType : "json",
		success : function(data) {
			loadPOTable(data.orders);
		}
	});
}

function loadPOTable(orders){
	var curency = $("input[name=CURRENCYID]").val();
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var ptax ="";
	$(".po-table tbody").html("");
	var body="";
	var ch="0";
	var j=0;
	$.each(orders, function( key, data) {
		

			var uprice= parseFloat((data.UNITCOST*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
			var amt= parseFloat((data.AMOUNT*data.CURRENCYUSEQT)).toFixed(numberOfDecimal);
			var ITEMDESC = escapeHtml(data.ITEMDESC);			
			body += '<tr>';
			body += '<td>';
			body += '<input type="Checkbox" style="border:0;background=#dddddd"	name="chkdPONO" value="'+j+'">';
			body += '</td>';
			body += '<td class="item-img text-center">';
			body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
			body += '<input type="hidden" name="lnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="prlnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="itemdesc" value="'+ITEMDESC+'">';
			body += '<input type="hidden" name="unitpricerd" value="">';
			body += '<input type="hidden" name="minstkqty" value="'+data.minstkqty+'">';
			body += '<input type="hidden" name="maxstkqty" value="'+data.maxstkqty+'">';
			body += '<input type="hidden" name="stockonhand" value="'+data.stockonhand+'">';
			body += '<input type="hidden" name="ITEMDESH" value="'+ITEMDESC+'">';
			body += '<input type="hidden" name="incommingqty" value="'+data.incommingqty+'">';
			body += '<input type="hidden" name="outgoingqty" value="'+data.outgoingqty+'">';
			body += '<input type="hidden" name="customerdiscount" value="'+data.customerdiscount+'">';
			body += '<input type="hidden" name="unitpricediscount" value="">';
			body += '<input type="hidden" name="discounttype" value="'+data.discounttype+'">';
			body += '<input type="hidden" name="minsp" value="">';
			body += '<input type="hidden" name="RecQty" value="'+data.QTYRC+'">';
			body += '<input type="hidden" name="polnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.TAXTYPE+'">';
			body += '<input type="hidden" name="tax_type" value="'+data.TAXTYPE+'">';
			body += '</td>';
			body += '<td class="bill-item">';
			//body += '<input type="text" name="item" class="form-control itemSearch" value="'+data.ITEM+'" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
			body += '<input type="text" name="item" class="form-control itemSearch" onchange="checkitems(this.value,this)" value="'+data.ITEM+'" style="width:87%" placeholder="Type or click to select an item.">';
			body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
			body += '<input class="form-control"  name="ITEMDES" value="'+ITEMDESC+'" readonly style="height: 23px;background-color: #fff;" readonly>';
			body += '</td>';	
			body += '<td>';
			body += '<input type="text" name="account_name" class="form-control accountSearch" value="Inventory Asset" placeholder="Account">';
			body += '</td>';
			body += '<td class="bill-item">';
			body += '<input type="text" name="UOM" class="form-control uomSearch" onchange="checkprduom(this.value,this)" value="'+data.UOM+'" placeholder="UOM">';
			body += '</td>';
			body += '<td class="item-qty text-right"><input type="text" name="QTY" value="'+data.QTY+'" class="form-control text-right" data-rl="'+data.minstkqty+'" data-msq="'+data.maxstkqty+'" data-soh="'+data.stockonhand+'" data-eq="'+data.EstQty+'" data-aq="'+data.AvlbQty+'" value="1.000" onchange="checkqty(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
			body += '<td>';
			body += '<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" value="'+data.PDELDATE+'" READONLY>';
			body += '</td>';
			body += '<td class="item-cost text-right"><input type="text" name="unitprice" value="'+uprice+'" class="form-control text-right" value="+szero+" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
			body += '<td class=\"table-icon\"><a href="#" class="fa fa-info-circle" onclick="getPreviousPurchaseOrderDetails(this)"></a></td>';
			body += '<td class="item-discount text-right">';
			body += '<div class="row">';							
			body += '<div class=" col-lg-12 col-sm-3 col-12">';
			body += '<div class="input-group my-group" style="width:120px;">';
			body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEMDISCOUNT+'" onchange="calculateAmountForDiscount(this)" onkeypress="return isNumberKey(event,this,4)">';
			body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmountForDiscount(this)">';
			if(data.ITEMDISCOUNTTYPE == curency){
				body += "<option selected value="+curency+">"+curency+"</option>";
			}else{
				body += "<option value="+curency+">"+curency+"</option>";
			}
			if(data.ITEMDISCOUNTTYPE == "%"){
				body += '<option selected >%</option>';		
			}else{
				body += '<option>%</option>';		
			}				
			body += '</select>';
			body += '</div>';
			body += '</div>'; 
			body += '</div>';
			body += '</td>';
//			body += '<td class="item-tax">';
//			body += '<input type="hidden" value="" name="tax">';
//			body += '<input type="text" name="tax_type" class="form-control taxSearch" value="" placeholder="Select a Tax" readonly>';
//			body += '</td>';
			body += '<td class="item-amount text-right grey-bg">';
			body += '<input name="amount" type="text" class="form-control text-right"  value="'+amt+'" readonly="readonly" style="display:inline-block;">';
			body += '</td>';
			body += '<td class="table-icon" style="position:relative;">';
			if(ch != "0"){
				//body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
			}
			body += '<a href="#" onclick="showRemarksDetails(this)">';
			body += '<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>';
			body += '</a>';
			body += '</td>';
			body += '</tr>';
			
			ptax = data.TAXTYPE;
		ch ="1";
		j++;
	});

	$(".po-table tbody").html(body);
	//$("input[name=ptaxdisplay]").val(ptax);
	//$("input[name=Purchasetax]").val(ptax);
	
	calculateTotal();
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
	
	tooltioedit();
	
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

function tooltioedit(){
	$(".po-table tbody tr").each(function() {
			var obj = $('td:eq(1)', this);
			loadQtyToolTipedit(obj);
			loadUnitPriceToolTipedit(obj);
			loadItemDescToolTipedit(obj);
	});
}

function loadQtyToolTipedit(obj){
	$(obj).closest('tr').find("input[name=QTY]").tooltip("destroy");
	var content = "<p class='text-left'>Min Stock Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("rl")+"</p>";
	content += "<p class='text-left'>Max Stock Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("msq")+"</p>";
	content += "<p class='text-left'>Stock on Hand: "+$(obj).closest('tr').find("input[name=QTY]").data("soh")+"</p>";
	content += "<p class='text-left'>Incomming Quantity: "+$(obj).closest('tr').find("input[name=incommingqty]").val()+"</p>";
	content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(obj).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
	content += "<p class='text-left'>Sales Estimate Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("eq")+"</p>";
	content += "<p class='text-left'>Available Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("aq")+"</p>";	
	//$(obj).closest('tr').find("input[name=QTY]").tooltip({title: content, html: true, placement: "top"}); 
	$(obj).closest('tr').find("input[name=QTY]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}
function loadItemDescToolTipedit(obj){
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip("destroy");
	var content = "<p class='text-left'> "+$(obj).closest('tr').find("input[name=ITEMDESH]").val()+"</p>";
	//$(obj).closest('tr').find("input[name=ITEMDES]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}


function loadUnitPriceToolTipedit(obj){
$(obj).closest('tr').find("input[name=unitprice]").tooltip("destroy");
	
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
	
	//$(obj).closest('tr').find("input[name=unitprice]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=unitprice]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}

function downloadFile(id,fileName)
{
	 var urlStrAttach = "../purchaseorder/downloadAttachmentById?attachid="+id;
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
	var urlStrAttach = "../purchaseorder/removeAttachmentById?removeid="+id;	
	$.ajax( {
		type : "POST",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
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
	$("#Purchasetax").typeahead('val', '"');
	$("#Purchasetax").typeahead('val', '');
	calculateTotal();
}

function calculateAmount(obj){

	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var state = $("input[name=STATE_PREFIX]").val();
	
	var qty = parseFloat($(obj).closest('tr').find('input[name=QTY]').val()).toFixed(3);
	
	var avlqty = parseFloat($(obj).closest('tr').find("input[name=QTY]").data("aq")).toFixed(3);
	var maxqty = parseFloat($(obj).closest('tr').find("input[name=QTY]").data("msq")).toFixed(3);
	
	var isproductmaxqty = $("input[name=ISPRODUCTMAXQTY]").val();
	if(isproductmaxqty == 1) {
		if(maxqty>0) {
		if(maxqty<(parseFloat(qty)+parseFloat(avlqty))){
		var chk = confirm("You order more than Max Stock Quantity. Do you want continue to add the product?");
			if (!chk) {
				$(obj).closest('tr').find('input[name=QTY]').val(parseFloat("0").toFixed(3));
				qty =parseFloat("0").toFixed(3);
				}
			}
		}
	}
	
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
	discount = checkno(discount);
	discount = parseFloat(discount).toFixed(numberOfDecimal);
	$("input[name ='discount']").val(discount);
	
	var shippingcost= $("input[name='shippingcost']").val();
	shippingcost = checkno(shippingcost);
	shippingcost = parseFloat(shippingcost).toFixed(numberOfDecimal);
	$("input[name ='shippingcost']").val(shippingcost);
	
	var adjustment= $("input[name ='adjustment']").val();
	adjustment = checkno(adjustment);
	adjustment = parseFloat(adjustment).toFixed(numberOfDecimal);
	$("input[name ='adjustment']").val(adjustment);
	
	var subtotalamount = "0";
	$(".po-table tbody tr td:last-child").prev().each(function() {
		if($(this).find('input').attr("disabled") != "disabled"){
		    amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
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
	 
	 
	 var adjustmentvalue = $('input[name ="adjustment"]').val();
	 adjustmentvalue = (adjustmentvalue == "") ? 0.00 : adjustmentvalue;
	 
	 
	 var taxTotal = "0";
	 var shiptaxstatus = $("input[name='shiptaxstatus']").val();
	 var odisctaxstatus = $("input[name='odiscounttaxstatus']").val();
	 
	 if(ptaxstatus == "0"){
			if(ptaxisshow == "1"){
				taxTotal = parseFloat(taxTotal) + parseFloat((subtotalamount/100)*ptaxpercentage);
				
				if(shiptaxstatus == "1"){
					taxTotal = parseFloat(taxTotal) + parseFloat((shippingvalue/100)*ptaxpercentage);
				}
				
				if(odisctaxstatus == "1"){
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
	}else{
		$(".taxDetails").html("");
		$('input[name ="taxamount"]').val("0");
	}
	 
	 
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

function removetaxdropdown(){
	$("#Purchasetax").typeahead('destroy');
}
function addtaxdropdown(){
	var plant = document.cpoform.plant.value;
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
//			menuElement.after( '<div class="taxAddBtn footer"  data-toggle="modal" data-target="#gstModal"><a href="#"> + New Tax</a></div>');
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

function changeitem(obj){
	var obj2 = $(obj).parent().find('input[name="item"]');
	$(obj2).typeahead('val', '');
	$(obj).parent().find('input[name="item"]').focus();
}

function setCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT){	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	$("#CURRENCY").typeahead('val', CURRENCY);
	setCURRENCYUSEQT(CURRENCYUSEQT);
	var basecurrency = document.cpoform.BASECURRENCYID.value; //Resvi
	document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi
	document.getElementById('lbltotal').innerHTML = "Total ("+CURRENCYID+")"; //  Author: Resvi  Add date: July 27,2021  Description:  Total of Local Currency
	if(basecurrency!=CURRENCYID)
		document.getElementById('showtotalcur').style.display = 'block';
	else
		document.getElementById('showtotalcur').style.display = 'none';
}

function setCURRENCYUSEQT(CURRENCYUSEQTCost){
	var plant = document.cpoform.plant.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	var CURRENCYID = $('input[name ="CURRENCYID"]').val();
	var CURRENCY = $('input[name ="CURRENCY"]').val();
	var oldeqtcost = $('input[name ="CURRENCYUSEQTOLD"]').val();
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
	
	var adjustment= $("input[name ='adjustment']").val();
	adjustment = checkno(adjustment);
	adjustment = ((parseFloat(adjustment))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
	adjustment = parseFloat(adjustment).toFixed(numberOfDecimal);
	$("input[name ='adjustment']").val(adjustment);
	
	$("input[name ='CURRENCYUSEQTOLD']").val(CURRENCYUSEQTCost);
	
	calculateTotal();
}
function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  
	  return true;
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

function checkAll(isChk)
{
	var len = document.cpoform.chkdPONO.length;
 	var orderLNo; 
 	if(len == undefined) len = 1;  
	if (document.cpoform.chkdPONO){
        for (var i = 0; i < len ; i++){
			if(len == 1){
				document.cpoform.chkdPONO.checked = isChk;
			}
			else{
				document.cpoform.chkdPONO[i].checked = isChk;
			}   
        }
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