var subWin = null;
var itemList="";
var taxList = [];
var zerotype=0;
$(document).ready(function(){
	var plant = document.cpoform.plant.value;
	var POMULTIESTNO = document.cpoform.PONOOLD.value;
	getEditPODetails(POMULTIESTNO);

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
			url: "../purchaseorderestimate/Auto-Generate",
			dataType: "json",
			beforeSend: function(){
				showLoader();
			}, 
			success: function(data) {
				$("#POMULTIESTNO").val(data.POESTNO);
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

	$("#btnSalesDraft").click(function(){
		$('input[name ="orderstatus"]').val('Draft');
		var isValid = validatePurchaseOrder();
		if(isValid){
			$( "#purchaseOrderForm" ).submit();
		}
	});

	$("#btnSalesOpen").click(function(){
		$('input[name ="orderstatus"]').val('Open');
		var isValid = validatePurchaseOrder();
		if(isValid){
			$( "#purchaseOrderForm" ).submit();
		}
	});

	$("#btnSalesOpenEmail").click(function(){
		if ($('#supplier_email').val() == ''){
			alert('Supplier record does not have an email address. Please update supplier email and try again or Save as Draft OR Save as Open');
			return;
		}
		$('input[name ="orderstatus"]').val('Open');
		var isValid = validatePurchaseOrder();
		if(isValid){
			var data = new FormData($("#purchaseOrderForm")[0]); //$("#purchaseOrderForm").serialize();
			$.ajax({
				type : "POST",
				url : "../purchaseestimate/converttope",
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
												.replace(/\{ORDER_NO\}/, $('#POMULTIESTNO').val())
												);
						$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(
									$('#template_body').val()
									.replace(/\{ORDER_NO\}/, $('#POMULTIESTNO').val())
									.replace(/\{SUPPLIER_NAME\}/, $('#vendname').val())
									);
						$('#send_attachment').val('Purchase Order');
					}else{
						$('.success-msg').html(data.MESSAGE).addClass('error-msg').removeClass('success-msg').css('display', 'inline');
					}

				}
			});
		}
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

	

	/* Payment Type Auto Suggestion */
	$('#payment_type').typeahead({
		  hint: true,
		  minLength:0,
		  searchOnFocus: true
		},
		{
		  display: 'PAYMENTTYPE',
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						ACTION : "GET_PAYMENT_TYPE_LIST",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.payTypes);
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
		    return '<p>' + data.PAYMENTTYPE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();
			}
			menuElement.after( '<div class="paymenttypeAddBtn footer"  data-toggle="modal" data-target="#paymentTypeModal"><a href="#"> + New Payment Type</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();

		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			$('.paymenttypeAddBtn').show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.paymenttypeAddBtn').hide();}, 150);
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
	/* Shipping Customer Auto Suggestion */
	$('#SHIPPINGCUSTOMER').typeahead({
		  hint: true,
		  minLength:0,
		  searchOnFocus: true
		},
		{
			display: 'VNAME',
		  async: true,
		  source: function (query, process,asyncProcess) {
			  var urlStr = "../MasterServlet";
				$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
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
		    	return '<div onclick="document.cpoform.SHIPPINGID.value = \''+data.VENDO+'\'"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Mobile: ' + data.HPNO + '</p><br/><p class="item-suggestion">Address: ' + data.ADDR1 + '  '+ data.ADDR2 +'</p><p class="item-suggestion pull-right">Customer TelNo: ' + data.TELNO + '</p></br><p class="item-suggestion"> ' + data.ADDR3 + '  '+ data.ADDR4 +'</p><p class="item-suggestion pull-right">Email: ' + data.EMAIL + '</p></br><p class="item-suggestion"> ' + data.COUNTRY + '  '+ data.ZIP +'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#" onclick="document.cpoform.custModal.value=\'shipcust\'"> + New Shipping Address</a></div>');
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
	   
		$(this).parent().parent().remove();
	    setLineNo();
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



	$("input[name=QTY]").mouseenter(function(){
		var content = "<p class='text-left'>Min Stock Quantity: "+$(this).data("rl")+"</p>";
		content += "<p class='text-left'>Max Stock Quantity: "+$(this).data("msq")+"</p>";
		content += "<p class='text-left'>Stock on Hand: "+$(this).data("soh")+"</p>";
		content += "<p class='text-left'>Estimated Quantity: "+$(this).data("eq")+"</p>";
		content += "<p class='text-left'>Available Quantity: "+$(this).data("aq")+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"});
	});
	
	$("input[name=ITEMDES]").mouseenter(function(){
		var content = "<p class='text-left'> "+$(this).closest('tr').find("input[name=ITEMDES]").val()+"</p>";
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
			url : "../purchaseestimate/addRemarks",
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
		/*if(!Number.isNaN(CURRENCYUSEQTCost))
		setCURRENCYUSEQT(CURRENCYUSEQTCost);*/
	});

	$("input[name=QTY]").focusout(function(){
		alert("ok");
		var value = $(this).val();
		var rcqry = $(this).closest('tr').find("input[name=RecQty]").val();
		var plno = $(this).closest('tr').find("input[name=POMULTIESTLNNO]").val();
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

function checkqty(obj){
	var value = $(obj).val();
	var rcqry = $(obj).closest('tr').find("input[name=RecQty]").val();
	var plno = $(obj).closest('tr').find("input[name=POMULTIESTLNNO]").val();
	if(plno != "0"){
		if(parseFloat(value) < parseFloat(rcqry)){
			alert("Quantity should not be less than received quantity, Received quantity = "+rcqry);
			$(obj).val(rcqry);
			return false;
		}
	}
	calculateAmount(obj);
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



function autoGen(){
	$.ajax({
		type: "GET",
		url: "../purchaseorderestimate/Auto-Generate",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#POMULTIESTNO").val(data.POESTNO);
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
				ACTION : "GET_MULTIPURCHASE_PRODUCT_LIST_AUTO_SUGGESTION",
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
			loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.PURCHASEUOM, selection.vendno, selection.vendname,selection.TAXTREATMENT,selection.CURRENCY,selection.CURRENCY_ID,selection.CURRENCYUSEQT);
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
					$(this).closest('tr').find('input[name="ITEMDES"]').val("");
			}
		});


	
	// Supplier Auto Suggestion 
	$(".vendsearch").typeahead({
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
	    return '<div><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
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
	}).on('typeahead:select',function(event,selection){
		loadVendData(this, selection.VENDO,selection.TAXTREATMENT,selection.CURRENCY,selection.CURRENCYID,selection.CURRENCYUSEQT);
	}).on('typeahead:close',function(){
		var menuElement = $(this).parent().find(".tt-menu");
		setTimeout(function(){ menuElement.next().hide();}, 150);
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == ""){
			document.cpoform.vendno.value = "";

			$("input[name=PERSON_INCHARGE]").val("");
			$("input[name=TAXTREATMENT_VALUE]").val("");
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
			$("input[name=nTAXTREATMENT]").val("");

			$("input[name=vendno]").val("");
		}
		
	});



// To get the suggestion data for Currency 

  $(".CURRENCYSEARCH").typeahead({
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
			return '<div><p onclick="setCurrencyid(this,\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";	
			  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		
		}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			$("input[name ='CURRENCYID']").val("");	
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
			CheckPriceVal(this,selection.UOM)
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});


}

function removeSuggestionToTable(){
	$(".itemSearch").typeahead('destroy');
	$(".vendsearch").typeahead('destroy');
	$(".CURRENCYSEARCH").typeahead('destroy');
	$(".taxSearch").typeahead('destroy');
	$(".accountSearch").typeahead('destroy');
	$(".uomSearch").typeahead('destroy');
}

function addRow(){
	var curency = $("input[name=BASECURRENCYID]").val();
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var taxdisplay = $("input[name=ptaxdisplay]").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var body="";
	body += '<tr>';
	body += '<td><input type="Checkbox" style="border:0;background=#dddddd"	name="chkdPOMULTIESTNO" value="">';
	body += '</td>';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">';
	body += '<input type="hidden" name="lnno" value="1">';
	body += '<input type="hidden" name="prlnno" value="0">';
	body += '<input type="hidden" name="itemdesc" value="">';
	body += '<input type="hidden" name="vendno" value="">';
	body += '<input type="hidden" name="nTAXTREATMENT" value="">';
	body += '<input type="hidden" name="CURRENCYID" value="">';
	body += '<input type="hidden" name="CURRENCYUSEQTOLD" value="">';
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
	body += '<input type="hidden" name="POMULTIESTLNNO" value="0">';
	body += '<input type="hidden" name="item_discount" value="0.00">';
	body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
	body += '<input type="hidden" name="tax_type" class="taxSearch" value="'+taxdisplay+'">';
	body += '<select name="item_discounttype" hidden>';
	body += "<option value="+curency+">"+curency+"</option>";
	body += '</select>';
	body += '<input type="hidden" name="account_name" value="Inventory Asset">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" class="form-control itemSearch" onchange="checkitems(this.value,this)" style="width:87%" placeholder="Type or click to select an item.">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';
	

	body += '<td class="bill-item">';
	body += '<input type="text" name="vendname" onchange="checksupplier(this.value,this)" class="form-control vendsearch" style="width:90%" placeholder="Supplier">';
	body += '</td>';
	
	body += '<td class="bill-item text-right" style="width:6%">';	
	body += '<input type="text" name="CURRENCY" onchange="checkcurrency(this.value,this)" class="form-control CURRENCYSEARCH">';
	body += '</td>';
	
	body += '<td class="bill-item" style="width:6%">';
	body += '<input type="text" name="CURRENCYUSEQT" class="form-control " READONLY >';
	
	body += '</td>';
	
	
	body += '<td class="bill-item">';
	body += '<input type="text" name="UOM" onchange="checkprduom(this.value,this)" class="form-control uomSearch" placeholder="UOM">';
	body += '</td>';
	body += '<td class="item-qty">';
	body += '<input type="text" name="ESTQTY" class="form-control">';
	body += '</td>';
	
	body += '<td class="item-qty">';
	body += '<input type="text" name="QTYRC" class="form-control">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" name="QTY" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="1.000" onchange="checkqty(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
	
	body += "<td class=\"item-cost text-right\"><input type=\"text\" name=\"unitprice\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\"></td>";
	body += '<td class=\"table-icon\"><a href="#" class="fa fa-info-circle" onclick="getPreviousPurchaseOrderDetails(this)"></a></td>';
	/*body += '<td class="item-discount text-right">';
	body += '<div class="row">';
	body += '<div class=" col-lg-12 col-sm-3 col-12">';
	body += '<div class="input-group my-group" style="width:120px;">';
	body += "<input name=\"item_discount\" type=\"text\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\">";
	body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmount(this)">';
	body += "<option value="+curency+">"+curency+"</option>";
	body += '<option>%</option>';
	body += '</select>';
	body += '</div>';
	body += '</div>';
	body += '</div>';
	body += '</td>';*/
//	body += '<td class="bill-item">';
// 	body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
//	body += '<input type="text" name="tax" class="form-control taxSearch" value="'+taxdisplay+'" placeholder="Select a Tax" readonly>';
//	body += '</td>';
	body += '<td class="item-amount text-right grey-bg">';
	body += "<input name=\"amount\" type=\"text\" class=\"form-control text-right\" value="+szero+" readonly=\"readonly\" style=\"display:inline-block;\">";
	body += '</td>';
	body += '<td class="item-amount grey-bg">';
	body += '<input type="text" name="EXPIREDATE" class="form-control datepicker" READONLY>';
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
						//document.cpoform.item.value = "";
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

function checksupplier(supplier,obj){	
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
						$(obj).typeahead('val', '');
						$(obj).parent().find('input[name="vendname"]').focus();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkcurrency(currency,obj){	
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
						$(obj).typeahead('val', '');
						$(obj).parent().find('input[name="CURRENCY"]').focus();
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

function loadItemData(obj, productId, catalogPath, account, cost,purchaseuom,vendno ,vendname,TAXTREATMENT,CURRENCY,CURRENCYID,CURRENCYUSEQT){
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(cost));
	$(obj).closest('tr').find("input[name=UOM]").val(purchaseuom);
	$(obj).closest('tr').find('input[name = "account_name"]').val(account);
	$(obj).closest('tr').find("input[name=vendno]").val(vendno);
	$(obj).closest('tr').find("input[name=vendname]").val(vendname);
	$(obj).closest('tr').find("input[name=nTAXTREATMENT]").val(TAXTREATMENT);
	$(obj).closest('tr').find("input[name=CURRENCY]").val(CURRENCY);
	$(obj).closest('tr').find("input[name=CURRENCYID]").val(CURRENCYID);
	$(obj).closest('tr').find('input[name = "CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var currencyID = $("input[name=BASECURRENCYID]").val();
	var custCode = $("input[name=CUST_CODE]").val();

	 var urlStr = "/track/ItemMstServlet";
	 var discount;
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				CURRENCY : CURRENCYID,
				VENDNO : vendno,
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
								$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat(resultVal.incomingIBDiscount).toFixed(numberOfDecimal));
								price = parseFloat(resultVal.incomingIBDiscount);
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


function loadItemDescToolTip(obj){
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip("destroy");
	var content = "<p class='text-left'>"+$(obj).closest('tr').find("input[name=ITEMDES]").data("pd")+"</p>";
	//$(obj).closest('tr').find("input[name=ITEMDES]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}


function CheckPriceVal(obj, uom) {
	var productId = $(obj).closest('tr').find('input[name=item]').val(); //Author: Azees  Create date: July 15,2021  Description: UOM Change Issue  
	var POMULTIESTNO = $("input[name=DONO]").val();
	var desc = "";
    var disc = $(obj).closest('tr').find("input[name=customerdiscount]").val();
    var numberOfDecimal = $("input[name=numberOfDecimal]").val();
    //var currencyID = $("input[name=CURRENCYID]").val();
    var currencyID = $(obj).closest('tr').find("input[name=CURRENCY]").val();
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
								cprice = parseFloat(resultVal.ConvertedDiscWTC);
	 						}
	                    	var calAmount = parseFloat(price).toFixed(numberOfDecimal);
							$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(calAmount).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
							$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);

	                    }
	                   	//calculateAmount(obj);
	                    calculateAmount(obj);
						loadUnitPriceToolTip(obj);
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
	$(obj).closest('tr').find("input[name=unitpriceS]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}

function showRemarksDetails(obj) {
	var lnno = $(obj).closest('tr').find("input[name=lnno]").val();
	var POMULTIESTNO = $("input[name=POMULTIESTNO]").val();
	var item = $(obj).closest('tr').find("input[name=item]").val();
	if(POMULTIESTNO == ''){
		alert("Please generate purchase order number");
	}else if(item != ''){
		$.ajax({
			type : "GET",
			url : "../purchaseestimate/getPurchaseOrderRemarks",
			data : {
				ITEM: item,
				POMULTIESTNO: POMULTIESTNO,
				POLNO: lnno
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
						body += '<input type="text" class="form-control" name="remarks" value="'+data.REMARKS[i].remarks+' "placeholder="Max 100 Characters" maxlength="100">';
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
				body += '<input type="hidden" name="r_POMULTIESTNO" value="'+POMULTIESTNO+'">';
				body += '<input type="hidden" name="r_lnno" value="'+lnno+'">';
				$("#remarks-table tbody").html(body);
			}
		});

		$("#remarksModal").modal();
	}else{
		alert("Please select Product");
	}
	


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
	var i=0;
	$(".po-table tbody tr td:first-child").each(function() {
		$(this).find('input[name=lnno]').val(i+1);
		$(this).find('input[name=chkdPOMULTIESTNO]').val(i);
		i++;
	});
}

function onAddSupplier(){
	   var CUST_CODE   = document.form1.CUST_CODE.value;
	   var CUST_NAME   = document.form1.CUST_NAME.value;
	   var companyregnumber   = document.form1.companyregnumber.value;
	   var TAXTREATMENT   = document.form1.TAXTREATMENT.value;
	   var RCBNO   = document.form1.RCBNO.value;
	   var rcbn = RCBNO.length;
	   var CURRENCY = document.form1.SUP_CURRENCY.value;
	   var region = document.form1.COUNTRY_REG.value;

	   var ValidNumber   = document.form1.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" suppliers you can create"); return false; }

	   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Supplier ID");;document.form1.CUST_CODE.focus(); return false; }

	   if(CUST_NAME == "" || CUST_NAME == null) {
	   alert("Please Enter Supplier Name");
	   document.form1.CUST_NAME.focus();
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
	   
//		 <!-- Author Name:Resviya ,Date:20/07/21 -->

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
	   if(!IsNumeric(form1.PMENT_DAYS.value))
	   {
	     alert(" Please Enter Days In Number");
	     form1.PMENT_DAYS.focus();  form1.PMENT_DAYS.select(); return false;
	   }
	   if(document.form1.COUNTRY_CODE.selectedIndex==0)
		{
		   alert("Please Select Country from Address");
		   document.form1.COUNTRY_CODE.focus();
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
			$('select[name ="nTAXTREATMENT"]').val(data.supplier[0].sTAXTREATMENT);
			setCurrencyid(obj,data.supplier[0].CURRENCY,data.supplier[0].sCURRENCY_ID,data.supplier[0].CURRENCYUSEQT);
			$('#nTAXTREATMENT').attr('disabled',false);
			if(data.supplier[0].sTAXTREATMENT =="GCC VAT Registered"||data.supplier[0].sTAXTREATMENT=="GCC NON VAT Registered"||data.supplier[0].sTAXTREATMENT=="NON GCC")
			{
				document.getElementById('CHK1').style.display = 'block';
			}
			else
				document.getElementById('CHK1').style.display = 'none';

			document.getElementById("REVERSECHARGE").checked = false;
			document.getElementById("GOODSIMPORT").checked = false;
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

}

function onAdd(){
	 var CUST_CODE   = document.form.CUST_CODE_C.value;
	 var CUST_NAME   = document.form.CUST_NAME_C.value;
	 var companyregnumber   = document.form.cus_companyregnumber.value;
	 var CL   = document.form.CREDITLIMIT.value;
	 var TAXTREATMENT   = document.form.TAXTREATMENT.value;
     var RCBNO   = document.form.RCBNO.value;
	 var rcbn = RCBNO.length;
	 var CURRENCY = document.form.CUS_CURRENCY.value;

	   var ValidNumber   = document.form.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" customers you can create"); return false; }

	 if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE_C.focus(); return false; }
	 if(CUST_NAME == "" || CUST_NAME == null) {
		 alert("Please Enter Customer Name");
		 document.form.CUST_NAME_C.focus();
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


	
	 if(document.form.COUNTRY_CODE_C.selectedIndex==0)
		{
		   alert("Please Select Country from Address");
		   document.form.COUNTRY_CODE_C.focus();
		 return false;
		}
	
	 var datasend = $('#formCustomer').serialize();


	   var urlStr = "/track/CreateCustomerServlet?action=JADD&reurl=createInvoice";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : datasend,
		dataType : "json",
		success : function(data) {
			if(document.cpoform.custModal.value == "cust"){
				//alert(JSON.stringify(data));
				$("input[name ='CUST_CODE']").val(data.customer[0].CID);
				//document.getElementById("CUST_CODE").value  = data.customer[0].CID;
				document.getElementById("CUSTOMER").value  = data.customer[0].CName;
				$("input[name ='CUSTOMER']").val(data.customer[0].CName);
				$("input[name ='TAXTREATMENT_VALUE']").val(data.customer[0].CTAXTREATMENT);
				document.getElementById('TAXTREATMENT').value = data.customer[0].CTAXTREATMENT;
				setCurrencyid(obj,data.customer[0].CURRENCY,data.customer[0].CURRENCY_ID,data.customer[0].CURRENCYUSEQT);
				document.cpoform.reset();
				$('#customerModal').modal('hide');
			}else{
				document.cpoform.reset();
				document.cpoform.SHIPPINGCUSTOMER.value = data.customer[0].CName;
				document.cpoform.SHIPPINGID.value = data.customer[0].CID;
				$('#customerModal').modal('hide');
			}
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
			$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					   $('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
				 $('#STATE_C').empty();
					$('#STATE_C').append('<OPTION style="display:none;">Select State</OPTION>');
						 $.each(StateList, function (key, value) {
							   $('#STATE_C').append('<option value="' + value.text + '">' + value.text + '</option>');
							});
		}
	});

}


function employeeCallback(data){
	if(data.STATUS="SUCCESS"){
		//alert(data.MESSAGE);
		$("#EMP_NAME").typeahead('val', data.EMP_NAME);
	}
}

function paymentTypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#payment_type").typeahead('val', data.PAYMENTTYPE);
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

function productCallbackpotable(productData){
	if(productData.STATUS="SUCCESS"){
		//alert(productData.MESSAGE);
		/*$("input[name ='item']").typeahead('val', productData.ITEM);*/
		var $tbody = $(".po-table tbody");
		var $last = $tbody.find('tr:last');
		var taxdisplay = $("input[name=ptaxdisplay]").val();
		var numberOfDecimal = $("input[name=numberOfDecimal]").val();
		 $last.remove();
		var ITEM_DESC = escapeHtml(productData.ITEM_DESC);
		//var curency = document.form1.curency.value;
		var curency = $("input[name=BASECURRENCYID]").val();
		var body="";
		body += '<tr>';
		body += '<td><input type="Checkbox" style="border:0;background=#dddddd"	name="chkdPOMULTIESTNO" value="">';
		body += '</td>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+productData.CATLOGPATH+'" style="width: 100%;">';
		body += '<input type="hidden" name="lnno" value="1">';
		body += '<input type="hidden" name="prlnno" value="0">';
		body += '<input type="hidden" name="itemdesc" value="">';
		body += '<input type="hidden" name="vendno" value="'+productData.vendno+'">';
		body += '<input type="hidden" name="nTAXTREATMENT" value="'+productData.TAXTREATMENT+'">';
		body += '<input type="hidden" name="CURRENCYID"  value="'+productData.CURRENCY_ID+'">';
		body += '<input type="hidden" name="CURRENCYUSEQTOLD"  value="">';
		if(productData.incomingIBDiscount=='' || productData.incomingIBDiscount=='0' ||productData.incomingIBDiscount=='0.00'||productData.incomingIBDiscount==undefined)
		{
		body += '<input type="hidden" name="unitpricerd" value=" value="'+productData.ConvertedUnitCostWTC+'"">';
		}
		else
		{
		body += '<input type="hidden" name="unitpricerd" value=" value="'+parseFloat(productData.incomingIBDiscount).toFixed(numberOfDecimal)+'"">';
		}
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
		body += '<input type="hidden" name="POMULTIESTLNNO" value="0">';
		body += '<input type="hidden" name="item_discount" value="0.00">';
		body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
		body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
		body += '<select name="item_discounttype" hidden>';
		body += "<option value="+curency+">"+curency+"</option>";
		body += '</select>';
		body += '<input type="hidden" name="account_name" value="Inventory Asset">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control itemSearch" style="width:87%" value="'+productData.ITEM+'" style="width:87%" placeholder="Type or click to select an item.">';
		body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
		body += '<input class="form-control"  name="ITEMDES"   value="'+ITEM_DESC+'" readonly style="height: 23px;background-color: #fff;" readonly>';
		body += '</td>';
		
		body += '<td class="bill-item">';
		body += '<input type="text" name="vendname"  class="form-control vendsearch" style="width:90%" placeholder="Supplier" value="'+productData.vendname+'">';
		body += '</td>';
		
		body += '<td class="bill-item text-right" style="width:6%">';
		body += '<input type="text" name="CURRENCY" class="form-control CURRENCYSEARCH" value="'+productData.CURRENCY+'">';
		body += '</td>';
		
		body += '<td class="bill-item" style="width:6%">';
		body += '<input type="text" name="CURRENCYUSEQT" class="form-control " READONLY value="'+productData.CURRENCYUSEQT+'">';
		body += '</td>';
		
		body += '<td class="bill-item">';
		body += '<input type="text" name="UOM" class="form-control uomSearch" value="'+productData.PURCHASEUOM+'" placeholder="UOM">';
		body += '</td>';
		
		body += '<td class="item-qty">';
		body += '<input type="text" name="ESTQTY" class="form-control" >';
		body += '</td>';
		
		body += '<td class="item-qty">';
		body += '<input type="text" name="QTYRC" class="form-control">';
		body += '</td>';
		
		body += '<td class="item-qty text-right"><input type="text" name="QTY" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		
		body += '<td class="item-cost text-right"><input type="text" name="unitprice" class="form-control text-right" value="'+productData.UNITPRICE+'" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td class=\"table-icon\"><a href="#" class="fa fa-info-circle" onclick="getPreviousPurchaseOrderDetails(this)"></a></td>';
		/*body += '<td class="item-discount text-right">';
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
		body += '</td>';*/
//		body += '<td class="bill-item">';
//		body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
//		body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
//		body += '</td>';
		
		body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
		body += '<input name="amount" type="text" class="form-control text-right" value="'+parseFloat(productData.ConvertedUnitCost).toFixed(numberOfDecimal)+'" readonly="readonly" style="display:inline-block;"></td>';
		body += '<td class="item-amount grey-bg">';
		body += '<input type="text" name="EXPIREDATE" class="form-control datepicker" READONLY>';
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
		loadItemData($tbody.find('tr:last'), productData.ITEM,productData.CATLOGPATH,"Inventory Asset",productData.UNITPRICE,productData.PURCHASEUOM, productData.vendno, productData.vendname,productData.TAXTREATMENT,productData.CURRENCY,productData.CURRENCY_ID,productData.CURRENCYUSEQT);
	}
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

function removetaxtrestl(){
	$('select[name=TAXTREATMENT]').attr('disabled',false);
}


function validatePurchaseOrder(){
	var POMULTIESTNO = $("input[name=POMULTIESTNO]").val();
	

	var msg = "";
	var isItemValid = true, isSupplierValid= true, iscurrencyValid= true, iscurrqtValid= true, isAccValid = true, isUnitPriceValid = true,
	isUnitPriceValid = true;

	var ValidNumber   = document.cpoform.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" order's you can create"); return false; }

	   if(POMULTIESTNO == ""){
			alert("Please Enter Order Number.");
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

	
	
	$("input[name ='vendname']").each(function() {
	    if($(this).val() == ""){	    	
	    	isSupplierValid = false;
	    }
	});
	
	if(!isSupplierValid){
		alert("The Supplier field cannot be empty.");
		return false;
	}
	
	
	
	
	
	$("input[name ='CURRENCY']").each(function() {
	    if($(this).val() == ""){	    	
	    	iscurrencyValid = false;
	    }
	});
	
	if(!iscurrencyValid){
		alert("The Currency field cannot be empty.");
		return false;
	}
	
	$("input[name ='CURRENCYUSEQT']").each(function() {
	    if($(this).val() == ""){	    	
	    	iscurrqtValid = false;
	    }
	});
	
	if(!iscurrqtValid){
		alert("The ExchangeRate field cannot be empty.");
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
		if($(this).find('input[name=chkdPOMULTIESTNO]').is(':checked')){
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
				
				issueQty = parseFloat(issueQty).toFixed(3);
				var orderedQty =  $(this).find('input[name=ESTQTY]').val();
				orderedQty = removeCommas(orderedQty);
				orderedQty = parseFloat(orderedQty).toFixed(3);
				var issuedQty = $(this).find('input[name=QTYRC]').val();
				issuedQty = removeCommas(issuedQty);
				issuedQty = parseFloat(issuedQty).toFixed(3);
				 
				
			if(issueQty >(orderedQty - issuedQty))
			{
				alert("Entered quantity exceeded above Ordered Qty");
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

function getEditPODetails(POMULTIESTNO){
	var plant = document.cpoform.plant.value;
	$.ajax({
		type : "POST",
		url : "../purchaseestimate/LoadEditDetails?POMULTIESTNO="+POMULTIESTNO,
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
	var curency = $("input[name=BASECURRENCYID]").val();
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var ptax ="";
	$(".po-table tbody").html("");
	var body="";
	var ch="0";
	var j=0;
	$.each(orders, function( key, data) {


			var ITEMDESC = escapeHtml(data.ITEMDESC);
			body += '<tr>';
			body += '<td>';
			body += '<input type="Checkbox" style="border:0;background=#dddddd"	name="chkdPOMULTIESTNO" value="'+j+'">';
			body += '</td>';
			body += '<td class="item-img text-center">';
			body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
			body += '<input type="hidden" name="lnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="prlnno" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="itemdesc" value="'+ITEMDESC+'">';
			body += '<input type="hidden" name="vendno" value="'+data.VENDNO+'">';
			body += '<input type="hidden" name="nTAXTREATMENT" value="'+data.TAXTREATMENT+'">';
			body += '<input type="hidden" name="CURRENCYID" value="'+data.CURRENCYID+'">';
			body += '<input type="hidden" name="CURRENCYUSEQTOLD" value="'+data.CURRENCYUSEQT+'">';
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
			body += '<input type="hidden" name="POMULTIESTLNNO" value="'+data.LNNO+'">';
			body += '<input type="hidden" name="item_discount" value="0.00">';
			body += '<input type="hidden" name="tax" class="taxSearch" value="'+data.TAXTYPE+'">';
			body += '<input type="hidden" name="tax_type" value="'+data.TAXTYPE+'">';
			body += '<select name="item_discounttype" hidden>';
			body += "<option value="+curency+">"+curency+"</option>";
			body += '</select>';
			body += '<input type="hidden" name="account_name" value="Inventory Asset">';
			body += '</td>';
			body += '<td class="bill-item">';
			body += '<input type="text" name="item" class="form-control itemSearch" onchange="checkitems(this.value,this)" value="'+data.ITEM+'" style="width:87%" placeholder="Type or click to select an item.">';
			body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
			body += '<input class="form-control"  name="ITEMDES" value="'+ITEMDESC+'" readonly style="height: 23px;background-color: #fff;" readonly>';
			body += '</td>';
			
			
			body += '<td class="bill-item">';
			body += '<input type="text" name="vendname" onchange="checkcurrency(this.value,this)" value="'+data.VNAME+'" class="form-control vendsearch" style="width:90%" placeholder="Supplier Name/ID">';
			body += '</td>';
			
			body += '<td class="bill-item text-right" style="width:6%">';			
			body += '<input type="text" name="CURRENCY" onchange="checkcurrency(this.value,this)" value="'+data.CURRENCY+'" class="form-control CURRENCYSEARCH">';
			body += '</td>';
			
			body += '<td class="bill-item" style="width:6%">';
			body += '<input type="text" name="CURRENCYUSEQT"  value="'+data.CURRENCYUSEQT+'" class="form-control " READONLY >';			
			body += '</td>';
			
			body += '<td class="bill-item">';
			body += '<input type="text" name="UOM" class="form-control uomSearch" onchange="checkprduom(this.value,this)" value="'+data.UOM+'" placeholder="UOM">';
			body += '</td>';
			
			body += '<td class="item-qty">';
			body += '<input type="text" name="ESTQTY" value="'+data.QTY+'" class="form-control" READONLY>';
			body += '</td>';
			
			body += '<td class="item-qty">';
			body += '<input type="text" name="QTYRC" value="'+data.QTYRC+'" class="form-control" READONLY>';
			body += '</td>';
			
			body += '<td class="item-qty text-right"><input type="text" name="QTY" value="'+data.QTY+'" class="form-control text-right" data-rl="'+data.minstkqty+'" data-msq="'+data.maxstkqty+'" data-soh="'+data.stockonhand+'" data-eq="'+data.EstQty+'" data-aq="'+data.AvlbQty+'" value="1.000" onchange="checkqty(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
			
			body += '<td class="item-cost text-right"><input type="text" name="unitprice" value="'+data.UNITCOST+'" class="form-control text-right" value="+szero+" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
			body += '<td class=\"table-icon\"><a href="#" class="fa fa-info-circle" onclick="getPreviousPurchaseOrderDetails(this)"></a></td>';
			/*body += '<td class="item-discount text-right">';
			body += '<div class="row">';
			body += '<div class=" col-lg-12 col-sm-3 col-12">';
			body += '<div class="input-group my-group" style="width:120px;">';
			body += '<input name="item_discount" type="text" class="form-control text-right" value="'+data.ITEMDISCOUNT+'" onchange="calculateAmountForDiscount(this)">';
			body += '<select name="item_discounttype" class="discountPicker form-control" onchange="calculateAmountForDiscount(this)">';
			
			if(data.ITEMDISCOUNTTYPE == "%"){
				body += '<option selected >%</option>';
			}else{
				body += "<option selected value="+data.ITEMDISCOUNTTYPE+">"+data.ITEMDISCOUNTTYPE+"</option>";

			}
			body += '</select>';
			body += '</div>';
			body += '</div>';
			body += '</div>';
			body += '</td>';*/
	//		body += '<td class="bill-item">';
	//		body += '<input type="hidden" value="'+data.TAXTYPE+'" name="tax_type">';
	//		body += '<input type="text" name="tax" class="form-control taxSearch" value="'+data.TAXTYPE+'" placeholder="Select a Tax" readonly>';
	//		body += '</td>';
			body += '<td class="item-amount text-right grey-bg">';
			body += '<input name="amount" type="text" class="form-control text-right"  value="'+data.AMOUNT+'" readonly="readonly" style="display:inline-block;">';
			body += '</td>';
			body += '<td class="item-amount grey-bg">';
			body += '<input type="text" name="EXPIREDATE" class="form-control datepicker" READONLY value="'+data.EXPIREDATE+'">';
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
	$("input[name=ptaxdisplay]").val(ptax);
	$("input[name=Purchasetax]").val(ptax);

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
			$(obj).closest('tr').find('input[name="QTY"]').focus();
			$(obj).closest('tr').find('input[name="QTY"]').select();
	});
}

function loadQtyToolTipedit(obj){
	//$(obj).closest('tr').find("input[name=QTY]").tooltip("destroy");
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
	//$(obj).closest('tr').find("input[name=ITEMDES]").tooltip("destroy");
	var content = "<p class='text-left'> "+$(obj).closest('tr').find("input[name=ITEMDES]").val()+"</p>";
	//$(obj).closest('tr').find("input[name=ITEMDES]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=ITEMDES]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"});
}



function loadUnitPriceToolTipedit(obj){
//$(obj).closest('tr').find("input[name=unitprice]").tooltip("destroy");

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
	 var urlStrAttach = "../purchaseestimate/downloadAttachmentById?attachid="+id;
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
	var urlStrAttach = "../purchaseestimate/removeAttachmentById?removeid="+id;
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
}

function loadVendData(obj,SName,TAXTREATMENT,sCURRENCY_ID,currencyID,Curramt){
	
	$(obj).closest('tr').find("input[name=vendno]").val(SName);
	$(obj).closest('tr').find("input[name=nTAXTREATMENT]").val(TAXTREATMENT);
	$(obj).closest('tr').find("input[name=CURRENCY]").val(sCURRENCY_ID);
	$(obj).closest('tr').find("input[name=CURRENCYID]").val(currencyID);
	$(obj).closest('tr').find('input[name = "CURRENCYUSEQT"]').val(Curramt);
	var productId = $(obj).closest('tr').find("input[name=item]").val();
	var VENDNO = $(obj).closest('tr').find("input[name=vendno]").val();
	var urlStr = "/track/ItemMstServlet";
	 var discount;
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				CURRENCY : currencyID,
				VENDNO : SName,
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
						}

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
								$(obj).closest('tr').find("input[name=customerdiscount]").val(parseFloat(resultVal.incomingIBDiscount).toFixed(numberOfDecimal));
								price = parseFloat(resultVal.incomingIBDiscount);
							}
							var calAmount = parseFloat(price).toFixed(numberOfDecimal);
							$(obj).closest('tr').find("input[name=unitprice]").val(calAmount);
							$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
							$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);
						}
					}



					calculateAmount(obj);
					loadUnitPriceToolTip(obj);
				}
			});
	$(obj).closest('tr').find('select[name ="item_discounttype"]').empty();
	$(obj).closest('tr').find('select[name ="item_discounttype"]').append('<option selected value="' + currencyID + '">' + currencyID + '</option>');
	$(obj).closest('tr').find('select[name ="item_discounttype"]').append('<option value="%">%</option>');
};
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
	
	var orderedQty = parseFloat($(obj).closest('tr').find('input[name=ESTQTY]').val()).toFixed(3);
	var issuedQty = parseFloat($(obj).closest('tr').find('input[name=QTYRC]').val()).toFixed(3);
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

function setCurrencyid(obj,CURRENCY,CURRENCYID,CURRENCYUSEQT){
	$(obj).closest('tr').find("input[name=CURRENCY]").val(CURRENCY);
	$(obj).closest('tr').find("input[name=CURRENCYID]").val(CURRENCYID);
	$(obj).closest('tr').find('input[name = "CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	var productId = $(obj).closest('tr').find("input[name=item]").val();
	var VENDNO = $(obj).closest('tr').find("input[name=vendno]").val();
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var urlStr = "/track/ItemMstServlet";
	 var discount;
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				CURRENCY : CURRENCYID,
				VENDNO : VENDNO,
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
						}

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
								price = parseFloat(resultVal.ConvertedUnitCost-(resultVal.incomingIBDiscount*CURRENCYUSEQT));
								//price = parseFloat(resultVal.incomingIBDiscount);
							}
							var calAmount = parseFloat(price).toFixed(numberOfDecimal);
							$(obj).closest('tr').find("input[name=unitprice]").val(calAmount);
							$(obj).closest('tr').find("input[name=unitpricerd]").val(calAmount.match(regex)[0]);
							$(obj).closest('tr').find("input[name=unitpricediscount]").val(calAmount);
						}
					}



					calculateAmount(obj);
					loadUnitPriceToolTip(obj);
				}
			});
	$(obj).closest('tr').find('select[name ="item_discounttype"]').empty();
	$(obj).closest('tr').find('select[name ="item_discounttype"]').append('<option selected value="' + CURRENCYID + '">' + CURRENCYID + '</option>');
	$(obj).closest('tr').find('select[name ="item_discounttype"]').append('<option value="%">%</option>');
}

function setCURRENCYUSEQT(CURRENCYUSEQTCost){
	var plant = document.cpoform.plant.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	var CURRENCYID = $('input[name ="BASECURRENCYID"]').val();
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
	var len = document.cpoform.chkdPOMULTIESTNO.length;
 	var orderLNo;
 	if(len == undefined) len = 1;
	if (document.cpoform.chkdPOMULTIESTNO){
        for (var i = 0; i < len ; i++){
			if(len == 1){
				document.cpoform.chkdPOMULTIESTNO.checked = isChk;
			}
			else{
				document.cpoform.chkdPOMULTIESTNO[i].checked = isChk;
			}
        }
    }
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
	formData.append("POMULTIESTNO", $("#POMULTIESTNO").val());
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

function getPreviousPurchaseOrderDetails(obj) {
	var item = $(obj).parent().parent().find('input[name=item]').val();
	var precost = $("input[name=precost]").val();
	if(precost == "1"){
		if(item != ''){
		var vendno = $(obj).parent().parent().find('input[name=vendno]').val();
		var uom =$(obj).parent().parent().find('input[name=UOM]').val();
		var numberOfDecimal = $("input[name=numberOfDecimal]").val();
		$.ajax({
			type : "POST",
			//url : "/track/purchaseestimate",
			url : "../purchaseestimate/GET_PREVIOUS_ORDER_DETAILS",
			data : {
				ITEM : item,
				CUSTCODE:vendno,
				UOM:uom,
				ROWS:"2",
				Submit : "GET_PREVIOUS_ORDER_DETAILS"
			},
			dataType : "json",
			success : function(data) {
				if(data.orders[0].POMULTIESTNO != undefined){
				var result = "";			
				$.each(data.orders, function( key, value ) {
					result += "<tr>";
					result += "<td>"+value.POMULTIESTNO+"</td>";
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
