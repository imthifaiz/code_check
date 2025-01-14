var subWin = null;
var itemList="";
var taxList = [];
var zerotype=0;

$(document).ready(function(){
	//document.form1.STATE_PREFIX.value="AUH";
	
	var plant = document.form1.plant.value;
	var parent_plant = $("input[name=PARENT_PLANT]").val();
	if(parent_plant == plant){
	seltax('STANDARD RATED');
	}
	
	var displayCustomerpop = document.form1.displayCustomerpop.value;
	var displayPaymentTypepop = document.form1.displayPaymentTypepop.value;
	  autoGen();
	var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.";
    var itemchk=0;
    var replacePreviousSalesCost = $("input[name=replacePreviousSalesCost]").val();

	if(replacePreviousSalesCost!="1")
		addSuggestionToitem();
	
	$("#btnSalesDraft").click(function(){
		$('input[name ="orderstatus"]').val('Draft');
		/*$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());*/
		//$("#salesOrderForm").submit();
		var isValidOrd = validateOrderNo();
		if (isValidOrd){
		var isValid = validateSalesOrder();
		if(isValid){
			/*var formData = new FormData($('#salesOrderForm')[0]);
		    $.ajax({
		        type: 'POST',
		        url: "../salesorder/new",
			    data:  formData,
			    contentType: false,
			    processData: false,
			    beforeSend: function(){
					showLoader();
				},
				complete: function(){
					hideLoader();
				},
		        success: function (data) {
		        	window.location.href="../salesorder/summary?msg="+data.MESSAGE;
		        },
		        error: function (data) {
		            alert(data.responseText);
		        }
		    });
	        return false;*/
	        $( "#salesOrderForm" ).submit();
		}
		}
	});

	$("#btnWaitForApp").click(function(){
		$('input[name ="orderstatus"]').val('APPROVAL PENDING');
		var isValidOrd = validateOrderNo();
		if (isValidOrd){
		var isValid = validateSalesOrder();
		if(isValid){
			$( "#salesOrderForm" ).submit();
		}
		}		
	});
	
	$("#btnSalesOpen").click(function(){
		$('input[name ="orderstatus"]').val('Open');
		/*$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());*/
		//$("#salesOrderForm").submit();
		var isValidOrd = validateOrderNo();
		if (isValidOrd){
		var isValid = validateSalesOrder();
		if(isValid){
			/*var formData = new FormData($('#salesOrderForm')[0]);
		    $.ajax({
		        type: 'POST',
		        url: "../salesorder/new",
			    data:  formData,
			    contentType: false,
			    processData: false,
			    beforeSend: function(){
					showLoader();
				},
				complete: function(){
					hideLoader();
				},
		        success: function (data) {
		        	if(data.ERROR_CODE == 100)
		        		window.location.href="../salesorder/summary?msg="+data.MESSAGE;
		        	else
		        		alert(data.MESSAGE);
		        },
		        error: function (data) {
		            alert(data.responseText);
		        }
		    });
	        return false;*/
	        $( "#salesOrderForm" ).submit();
		}
		}
	});

	$("#btnSalesOpenEmail").click(function(){
		if ($('#customer_email').val() == ''){
			alert('Customer record does not have an email address. Please update customer email and try again or Save as Draft OR Save as Open');
			return;
		}
		$('input[name ="orderstatus"]').val('Open');
		/*$("#sub_total").val($("#subTotal").html());
		$("#total_amount").val($("#total").html());*/
		//$("#salesOrderForm").submit();
		var isValidOrd = validateOrderNo();
		if (isValidOrd){
		var isValid = validateSalesOrder();
		if(isValid){
			var formData = new FormData($('#salesOrderForm')[0]);
		    $.ajax({
		        type: 'POST',
		        url: "../salesorder/new",
			    data:  formData,
			    contentType: false,
			    processData: false,
				beforeSend: function(){
					showLoader();
				},
				complete: function(){
					hideLoader();
				},
		        success: function (data) {
		        	if(data.ERROR_CODE == 100){
		        		$('.success-msg').html(data.MESSAGE).css('display', 'inline');
						$('#common_email_modal').modal('toggle');
						$('#send_to').val($('#customer_email').val()).multiEmail();
						$('#send_subject').val($('#template_subject').val()
												.replace(/\{COMPANY_NAME\}/, $('#plant_desc').val())
												.replace(/\{ORDER_NO\}/, $('#DONO').val())
												);
						$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(
									$('#template_body').val()
									.replace(/\{ORDER_NO\}/, $('#DONO').val())
									.replace(/\{CUSTOMER_NAME\}/, $('#CUSTOMER').val())
									);
						$('#send_attachment').val('Sales Order');
					}else{
						$('.success-msg').html(data.MESSAGE).addClass('error-msg').removeClass('success-msg').css('display', 'inline');
						alert(data.MESSAGE);
					}

		        },
		        error: function (data) {
		            alert(data.responseText);
		        }
		    });
	        return false;
		}
		}
	});
	
	/*$("#btnshipaddr").click(function(){
		$('#cshipaddr').empty();
		$("input[name=SHIPCONTACTNAME]").val($("input[name=SHIP_CONTACTNAME]").val());
		$("input[name=SHIPDESGINATION]").val($("input[name=SHIP_DESGINATION]").val());
		$("input[name=SHIPADDR1]").val($("input[name=SHIP_ADDR1]").val());
		$("input[name=SHIPADDR2]").val($("input[name=SHIP_ADDR2]").val());
		$("input[name=SHIPADDR3]").val($("input[name=SHIP_ADDR3]").val());
		$("input[name=SHIPADDR4]").val($("input[name=SHIP_ADDR4]").val());
		$("input[name=SHIPSTATE]").val($("input[name=SHIP_STATE]").val());
		$("input[name=SHIPZIP]").val($("input[name=SHIP_ZIP]").val());
		$("input[name=SHIPWORKPHONE]").val($("input[name=SHIP_WORKPHONE]").val());

		var addr = '<p>'+$("input[name=SHIP_CONTACTNAME]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_DESGINATION]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_ADDR1]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_ADDR2]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_ADDR3]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_ADDR4]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_STATE]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_WORKPHONE]").val()+'</p>';
		addr += '<p>'+$("input[name=SHIP_ZIP]").val()+'</p>';
		$('#cshipaddr').append(addr);
	});*/

	$(document).on('click','#autoGen',function(){
		$.ajax({
			type: "GET",
			url: "../salesorder/Auto-Generate",
			dataType: "json",
			beforeSend: function(){
				showLoader();
			},
			success: function(data) {
				$("#DONO").val(data.DONO);
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
			   return '<div onclick="setCustomerData(\''+data.TAXTREATMENT+'\',\''+data.TRANSPORTNAME+'\',\''+data.TRANSPORTID+'\',\''+data.CUSTNO
			   +'\',\''+data.CNAME+'\',\''+data.NAME+'\',\''+data.TELNO+'\',\''+data.EMAIL
			   +'\',\''+data.ADDR1+'\',\''+data.ADDR2+'\',\''+data.ADDR3+'\',\''+data.ADDR4+'\',\''+data.REMARKS+'\',\''+data.COUNTRY+'\',\''+data.ZIP+'\',\''+data.HPNO
			   +'\',\''+data.EMAIL+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT
			   +'\',\''+data.PAYMENTTYPE+'\',\''+data.SHOWSALESBYPURCHASECOST+'\',\''+data.CUST_ADDONCOST+'\',\''+data.CUST_ADDONCOSTTYPE+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
		   }
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();
			if(displayCustomerpop == 'true'){
			$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal" onclick="document.form1.custModal.value=\'cust\'"><a href="#"> + New Customer</a></div>');
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
				$("input[name=STATE]").val("");
				$("input[name=PAYMENTTYPE]").val("");
				$("input[name=transports]").val("");
				$("input[name=CUST_ADDONCOST]").val("0.0");
				$("input[name=CUST_ADDONCOSTTYPE]").val("0");
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
				//$("#transport").typeahead('val', '"');
				$("#transport").typeahead('val', '');
				$("#PAYMENTTYPE").typeahead('val', '');
				$("input[name=payment_terms]").val("");
				
			}
		}).on('typeahead:select',function(event,selection){
			$("#project").typeahead('val', '"');
			$("#project").typeahead('val', '');
			$("input[name=PROJECTID]").val('');

			if(replacePreviousSalesCost=="1") {
			if(itemchk==1||itemchk==0)
				{
				$(".itemSearch").typeahead('destroy');
				addSuggestionToitem();
				itemchk=1;
				}
			}

			if($(this).val() == ""){
				$('#shipbilladd').empty();
				$('#shipbilladd').append('');
			}else{
				
				setCustomerData(selection.TAXTREATMENT,selection.TRANSPORTNAME,selection.TRANSPORTID,selection.CUSTNO,selection.CNAME,selection.NAME,selection.TELNO,selection.EMAIL,selection.ADDR1,selection.ADDR2,selection.ADDR3,selection.ADDR4,selection.REMARKS,selection.COUNTRY,selection.ZIP,selection.HPNO,selection.EMAIL,selection.CURRENCY,selection.CURRENCYID,selection.CURRENCYUSEQT,selection.PAYMENTTYPE,selection.SHOWSALESBYPURCHASECOST,selection.CUST_ADDONCOST,selection.CUST_ADDONCOSTTYPE);
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
				$("input[name=CUST_ADDONCOST]").val(selection.CUST_ADDONCOST);
				$("input[name=CUST_ADDONCOSTTYPE]").val(selection.CUST_ADDONCOSTTYPE);
				
			}
			
			/*$("textarea#shipaddres").val(selection.SHIPCONTACTNAME+"\n"+
					selection.SHIPDESGINATION+"\n"+
					selection.SHIPADDR1+"\n"+
					selection.SHIPADDR2+"\n"+
					selection.SHIPADDR3+"\n"+
					selection.SHIPADDR4+"\n"+
					selection.SHIPSTATE+"\n"+
					selection.SHIPZIP+"\n"+
					selection.SHIPWORKPHONE);*/
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
			menuElement.after( '<div class="transportAddBtn footer"  data-toggle="modal" data-target="#transportModal"  onclick="document.form1.custModal.value=\'\'"><a href="#"> + New Transport </a></div>');
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
			menuElement.after( '<div class="payTermsAddBtn footer"  data-toggle="modal" data-target="#payTermsModal"  onclick="document.form1.custModal.value=\'\'"><a href="#"> + Add Payment Terms</a></div>');
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
//			menuElement.after( '<div class="taxAddBtn footer"  data-toggle="modal" data-target="#gstModal"><a href="#"> + New Tax</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		})
		.on('typeahead:open',function(event,selection){
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
				menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal"><a href="#"> + New Payment Mode</a></div>');
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
						Type : "SALES",
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

	$(".so-table tbody").on('click','.bill-action',function(){
	    /*var obj = $(this).closest('tr').find('td:nth-child(6)');
	    calculateTax(obj, "", "", "");*/
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

	$("input[name=QTY]").mouseenter(function(){
		var content = "<p class='text-left'>Reorder Level: "+$(this).data("rl")+"</p>";
		content += "<p class='text-left'>Max Stock Quantity: "+$(this).data("msq")+"</p>";
		content += "<p class='text-left'>Stock on Hand: "+$(this).data("soh")+"</p>";
		content += "<p class='text-left'>Sales Estimate Quantity: "+$(this).data("eq")+"</p>";
		content += "<p class='text-left'>Available Quantity: "+$(this).data("aq")+"</p>";
		$(this).tooltip({title: content, html: true, placement: "top"});
	});

	$("input[name=unitprice]").mouseenter(function(){
		var cust_addoncost = parseFloat($("input[name=CUST_ADDONCOST]").val()).toFixed(3);
		var cust_addoncosttype = $("input[name=CUST_ADDONCOSTTYPE]").val();
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

		if(cust_addoncosttype=='1'){
		content += "<p class='text-left'>Add On Cost: "+cust_addoncost+" %</p>";
		}else{
		content += "<p class='text-left'>Add On Cost: "+cust_addoncost+"</p>";
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
			url : "../salesorder/addRemarks",
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
		if(!Number.isNaN(CURRENCYUSEQTCost))//  Author: Azees  Create date: July 19,2021  Description:  Exchange Rate Issue
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});

	if(replacePreviousSalesCost=="1") {
	$(".itemSearch").click(function(){
		var customer = $("input[name=CUST_NAME]").val();
		if(customer == ""){
			itemchk=1;
			alert("Please select a Customer.");
			return false;
		}
		itemchk=itemchk+1;
	});
	}
	addSuggestionToTable();
	$("input[name ='CUST_NAME']").focus();
});/* document ready*/
function addSuggestionToitem(){
	var replacePreviousSalesCost = $("input[name=replacePreviousSalesCost]").val();
	if(replacePreviousSalesCost=="1") {
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
				//ACTION : "GET_INVOICE_PRODUCT_LIST_FOR_SUGGESTION",
				ACTION : "GET_INVOICE_PRODUCT_LIST_AUTO_SUGGESTION",
				CUSTNO : $("input[name=CUST_CODE]").val(),
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
		//	return '<div onclick="loadItemData(this,\''+ data.ITEM+'\',\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.SALESUOM+'\',\''+ data.COST+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';			
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
			loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.SALESUOM,selection.COST);
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				$(this).closest('tr').find('input[name="ITEMDES"]').val("");
			}		
		});
	} else {

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
				//	return '<div onclick="loadItemData(this,\''+ data.ITEM+'\',\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.SALESUOM+'\',\''+ data.COST+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
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
				loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.SALESUOM,selection.COST);
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
	formData.append("DONO", $("#DONO").val());
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
function addSuggestionToTable(){


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
				GSTTYPE : query,
				GST : $("input[name=GST]").val()
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
	});	*/

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
		
		$('.custom-scroll-table tbody tr').each(function () {
		$(this).find("input[name ='item']").focus();
		var obj1 = $('td:eq(1)', this);
		loaddropdata(obj1);
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

//		addSuggestionToitem();
}
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
	/*var curency = $("input[name=currency]").val();
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();*/
	var replacePreviousSalesCost = $("input[name=replacePreviousSalesCost]").val();
	if(replacePreviousSalesCost=="1") {
		var customer = $("input[name=CUST_NAME]").val();
		if(customer == ""){
			alert("Please select a Customer.");
			return false;
		}
	}
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
	body += '<input type="hidden" name="unitpricerd" value="0.00">';
	body += '<input type="hidden" name="itemprice" value="0.00">';
	body += '<input type="hidden" name="itemcost" value="">';
	body += '<input type="hidden" name="minstkqty" value="">';
	body += '<input type="hidden" name="maxstkqty" value="">';
	body += '<input type="hidden" name="stockonhand" value="">';
	body += '<input type="hidden" name="outgoingqty" value="">';
	body += '<input type="hidden" name="unitpricediscount" value="">';
	body += '<input type="hidden" name="discounttype" value="">';
	body += '<input type="hidden" name="customerdiscount" value="">';
	body += '<input type="hidden" name="minsp" value="">';
	body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" class="form-control itemSearch" onchange="checkitems(this.value,this)"  style="width:87%" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" data-pd="" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';
	body += '<td>';
	body += '<input type="text" name="account_name" class="form-control accountSearch" onchange="checkaccount(this.value,this)" placeholder="Account"  value="Local sales - retail">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="UOM" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="Select UOM">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" name="QTY" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
	body += '<td>';
	body += '<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" READONLY  tabindex="-1">';
	body += '</td>';
	body += '<td>';
	body += '<input type="text" name="unitcost" class="form-control text-right" value="'+szero+'" readonly tabindex="-1">';
	body += '</td>';
	body += '<td>';
	body += '<input type="text" name="addonshow" class="form-control text-right" value="'+szero+'" readonly tabindex="-1">';
	body += '<input type="hidden" name="addonprice" value="'+szero+'">';
	body += '<input type="hidden" name="addontype" value="'+curency+'">';
	body += '</td>';
	body += "<td class=\"item-cost text-right\"><input type=\"text\" name=\"unitprice\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\"><input type=\"hidden\" name=\"CUST_ADDONCOST_DET\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\"></td>";
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
	/*body += '<td class="item-tax">';
	body += '<input type="hidden" name="tax_type">';
	body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
	body += '</td>';*/
	body += '<td class="item-amount text-right grey-bg">';
	body += "<input name=\"amount\" type=\"text\" class=\"form-control text-right\" value="+szero+" readonly=\"readonly\" style=\"display:inline-block;\" tabindex=\"-1\">";
	body += '</td>';
//	body += '<td>';
//	body += '<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" READONLY>';
//	body += '</td>';
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
	//addSuggestionToitem();
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
		var urlStr = "../salesorder/CheckOrderno";
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
						alert("Order Number Already Exists, New Number Generated");
						document.getElementById("DONO").focus();
						document.getElementById("DONO").value="";
						autoGen();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function validateOrderNo(){
	var orderno = $("input[name=DONO]").val();
	if(orderno != ""){	
		var urlStr = "../salesorder/CheckOrderno";
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
						alert("Order Number Already Exists, New Number Generated");
						document.getElementById("DONO").focus();
						document.getElementById("DONO").value="";
						autoGen();
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
						document.getElementById("PAYMENTTYPE").focus();
						$("#PAYMENTTYPE").typeahead('val', '');
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
						$("input[name=Salestax]").val("");
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
		var trid = $(obj).closest('tr').attr('id');
		addcustonsuggesiondropdown(trid);	
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
						$(obj).parent().find('input[name="UOM"]').focus();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkaccount(account,obj){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ACCOUNT : account,
				ACTION : "ACCOUNT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Account Does't Exists");
						$(obj).typeahead('val', 'Local sales - retail');
						$(obj).parent().find('input[name="account_name"]').focus();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
//table validation ends

function setCustomerData(TAXTREATMENT,TRANSPORTNAME,TRANSPORTID,CUSTNO,CNAME,NAME,TELNO,EMAIL,ADD1,ADD2,ADD3,ADD4,REMARKS,COUNTRY,ZIP,HPNO,EMAIL,CURRENCY,CURRENCYID,CURRENCYUSEQT,PAYMENTTYPE,SHOWSALESBYPURCHASECOST,CUST_ADDONCOST,CUST_ADDONCOSTTYPE){
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
	$("input[name=CUST_ADDONCOST]").val(CUST_ADDONCOST);
	$("input[name=CUST_ADDONCOSTTYPE]").val(CUST_ADDONCOSTTYPE);
	
	$("input[name=HPNO]").val(HPNO);
	$("input[name=EMAIL]").val(EMAIL);
	setCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT);

	$("input[name=PAYMENTTYPE]").val(PAYMENTTYPE);
	
	
	$("input[name=SHIPPINGID]").val(CUSTNO);
	
	if(SHOWSALESBYPURCHASECOST==="1"){
	//$("input[name=replacePreviousSalesCost]").val(3);
	$("input[name=replacePreviousSalesCost]").val(0);
		removeSuggestionToTable();
		addSuggestionToTable();
	}else if(SHOWSALESBYPURCHASECOST==="2"){
	$("input[name=replacePreviousSalesCost]").val(3);
		removeSuggestionToTable();
		addSuggestionToTable();
	} else {
	var oldreplacePreviousSalesCost = $("input[name=oldreplacePreviousSalesCost]").val();	
	$("input[name=replacePreviousSalesCost]").val(oldreplacePreviousSalesCost);
		removeSuggestionToTable();
		addSuggestionToTable();		
	}
	
//	$(".so-table tbody tr").each(function() {
//	var catalogPath =  $(this).find("input[name=imgcatalogpath]").val();
//	var productId =  $(this).find("input[name=item]").val();
//	var price =  $(this).find("input[name=itemprice]").val();
//	var cost =  $(this).find("input[name=itemcost]").val();
//	var salesuom =  $(this).find("input[name=UOM]").val();
//	loadItemData(this, productId, catalogPath, '', price, salesuom,cost);
//	calculateAmount(this);
//	});
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
			if(data.orders[0].DONO != undefined){
			var result = "";

			$.each(data.orders, function( key, value ) {
				result += "<tr>";
				result += "<td>"+value.DONO+"</td>";
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
	
	

	/*$('#discount_type').empty();
	$('#discount_type').append('<option value="' + CURRENCYID + '">' + CURRENCY + '</option>');
	$('#discount_type').append('<option value="%">%</option>');

	$('#oddiscount').empty();
	$('#oddiscount').append('<option value="' + CURRENCYID + '">' + CURRENCY + '</option>');
	$('#oddiscount').append('<option value="%">%</option>');*/
	setCURRENCYUSEQT(CURRENCYUSEQT);
	
	var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
	document.getElementById('exchangerate').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi
	document.getElementById('AODTYPE').innerHTML = "Add On (% / "+CURRENCYID+")";
	
	document.getElementById('lbltotal').innerHTML = "Total ("+CURRENCYID+")"; //  Author: Azees  Create date: July 19,2021  Description:  Total of Local Currency
	if(basecurrency!=CURRENCYID)
		document.getElementById('showtotalcur').style.display = 'block';
	else
		document.getElementById('showtotalcur').style.display = 'none';
	
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

function loadItemData(obj, productId, catalogPath, account, price, salesuom,cost){
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(price));
	$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat(price));
	$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(price));
	$(obj).closest('tr').find("input[name=itemcost]").val(parseFloat(cost));
	$(obj).closest('tr').find("input[name=UOM]").val(salesuom);
	var cust_addoncost = parseFloat($("input[name=CUST_ADDONCOST]").val()).toFixed(3);
	var cust_addoncosttype = $("input[name=CUST_ADDONCOSTTYPE]").val();
	var addcustprice = "";
	
	var currencyID = $("input[name=CURRENCYID]").val();
	var currencyUSEQT = $("input[name=CURRENCYUSEQT]").val();
	var custCode = $("input[name=CUST_CODE]").val();
	var replacePreviousSalesCost = $("input[name=replacePreviousSalesCost]").val();

	 var dono = document.form1.DONO.value;
	 var urlStr = "/track/ItemMstServlet";
	 var discount;
	 if(replacePreviousSalesCost=="1") {
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

						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat("0.00000"));
							$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
						}else{
							if(cust_addoncosttype=='1'){
								addcustprice = (parseFloat(resultVal.ConvertedUnitCost)+((parseFloat(resultVal.ConvertedUnitCost)*parseFloat(cust_addoncost))/100));
							}else{
							addcustprice = (parseFloat(resultVal.ConvertedUnitCost)+parseFloat(cust_addoncost));
							}
							$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(addcustprice).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
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
							if(cust_addoncosttype=='1'){
								calAmount = (parseFloat(calAmount)+((parseFloat(calAmount)*parseFloat(cust_addoncost))/100));
							}else{
							calAmount = (parseFloat(calAmount)+parseFloat(cust_addoncost));
							}
							$(obj).closest('tr').find("input[name=unitprice]").val(calAmount);
							$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(calAmount);
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
					$(obj).closest('tr').find('input[name="QTY"]').focus();
					$(obj).closest('tr').find('input[name="QTY"]').select();
				}
			});
		
			
	 } else if(replacePreviousSalesCost=="0"){

		 
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

							var auprice = 0;
							if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
								$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
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
								if(cust_addoncosttype=='1'){
									auprice = (parseFloat(auprice)+((parseFloat(auprice)*parseFloat(cust_addoncost))/100));
								}else{
								auprice = (parseFloat(auprice)+parseFloat(cust_addoncost));
								}
								$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(auprice).toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat(auprice).toFixed(numberOfDecimal));
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
								if(cust_addoncosttype=='1'){
									calAmount = (parseFloat(calAmount)+((parseFloat(calAmount)*parseFloat(cust_addoncost))/100));
								}else{
								calAmount = (parseFloat(calAmount)+parseFloat(cust_addoncost));
								}
								$(obj).closest('tr').find("input[name=unitprice]").val(calAmount);
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(calAmount);
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
						$(obj).closest('tr').find('input[name="QTY"]').focus();
						$(obj).closest('tr').find('input[name="QTY"]').select();
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
					REPLACEPREVIOUSSALESCOST: replacePreviousSalesCost,
					//ACTION : "GET_PRODUCT_DETAILS"
					ACTION :"GET_PRODUCT_AUTO_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						//outgoingOBDiscount
						if (data.status == "100") {
							var resultVal = data.result;
							var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;

							if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
								$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
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
								
								if(replacePreviousSalesCost==3) {
								$(obj).closest('tr').find("input[name=addonshow]").val(addshow);
								$(obj).closest('tr').find("input[name=addonprice]").val(resultVal.incprice);
								$(obj).closest('tr').find("input[name=addontype]").val(resultVal.incpriceunit);
								$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(auprice));
								}
								//imthi end
								if(cust_addoncosttype=='1'){
								addcustprice = (parseFloat(resultVal.ConvertedUnitCost)+((parseFloat(resultVal.ConvertedUnitCost)*parseFloat(cust_addoncost))/100));
								}else{
								addcustprice = (parseFloat(resultVal.ConvertedUnitCost)+parseFloat(cust_addoncost));
								}
								$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(addcustprice).toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
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
								if(cust_addoncosttype=='1'){
									calAmount = (parseFloat(calAmount)+((parseFloat(calAmount)*parseFloat(cust_addoncost))/100));
								}else{
								calAmount = (parseFloat(calAmount)+parseFloat(cust_addoncost));
								}
								$(obj).closest('tr').find("input[name=unitprice]").val(calAmount);
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(calAmount);
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
						$(obj).closest('tr').find('input[name="QTY"]').focus();
						$(obj).closest('tr').find('input[name="QTY"]').select();
					}
				});

	 }
	}

function CheckPriceVal(obj, uom) {
	var cust_addoncost = parseFloat($("input[name=CUST_ADDONCOST]").val()).toFixed(3);
	var cust_addoncosttype = $("input[name=CUST_ADDONCOSTTYPE]").val();
	var productId = $(obj).closest('tr').find("td:nth-child(2)").find('input[name=item]').val();
	var dono = $("input[name=DONO]").val();
    var desc = "";
    var disc = $(obj).closest('tr').find("input[name=customerdiscount]").val();
    var discounttype = $(obj).closest('tr').find("input[name=discounttype]").val();
    var replacePreviousSalesCost = $("input[name=replacePreviousSalesCost]").val();
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
						$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
						$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
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
						if(cust_addoncosttype=='1'){
								ConvertedUnitCost = (parseFloat(ConvertedUnitCost)+((parseFloat(ConvertedUnitCost)*parseFloat(cust_addoncost))/100));
							}else{
						ConvertedUnitCost = (parseFloat(ConvertedUnitCost)+parseFloat(cust_addoncost));
							}
						$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(ConvertedUnitCost).toFixed(numberOfDecimal));
						$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat(ConvertedUnitCost).toFixed(numberOfDecimal));
						$(obj).closest('tr').find("input[name=unitpricerd]").val(ConvertedUnitCostWTC);
					}
					if(resultVal.cost == null || resultVal.cost == undefined || resultVal.cost == 0){
						$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
					}else{
						$(obj).closest('tr').find("input[name=unitcost]").val(parseFloat(resultVal.cost*currencyUSEQT).toFixed(numberOfDecimal));
					}

					if(parseFloat(disc)>0)
                    {
                    	//if(document.form1.discounttype.value=="BYPERCENTAGE")
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
						else
						{
							//discount only for product master uom (changed by azees 13.12.22) 
							//price = parseFloat(resultVal.ConvertedDiscWTC);
							var getdist = disc.replace("%","");
							price = parseFloat(ConvertedUnitCost-(getdist*currencyUSEQT));
 						}
                    	var calAmount = parseFloat(price).toFixed(numberOfDecimal);
                    			if(cust_addoncosttype=='1'){
									calAmount = (parseFloat(calAmount)+((parseFloat(calAmount)*parseFloat(cust_addoncost))/100));
								}else{
			                    	calAmount = (parseFloat(calAmount)+parseFloat(cust_addoncost));
								}
						$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(calAmount).toFixed(numberOfDecimal));
						$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat(calAmount).toFixed(numberOfDecimal));
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

/*function calculateAmount(obj){

	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var state = $("input[name=STATE_PREFIX]").val();

	var qty = parseFloat($(obj).closest('tr').find("td:nth-child(5)").find('input[name=QTY]').val()).toFixed(3);
	var cost = parseFloat($(obj).closest('tr').find("td:nth-child(7)").find('input[name=unitprice]').val()).toFixed(numberOfDecimal);
	var itemDiscount = parseFloat($(obj).closest('tr').find("td:nth-child(9)").find('input[name=item_discount]').val()).toFixed(numberOfDecimal);
	var discounType = $(obj).closest('tr').find("td:nth-child(9)").find('select').val();
	var itemDiscountval=itemDiscount;
	 if(discounType == "%"){
		 itemDiscountval = parseFloat((qty*cost)*(itemDiscount/100)).toFixed(3);
		 itemDiscount = parseFloat(itemDiscount).toFixed(3);
	 }
	var amount = parseFloat(((qty*cost)-itemDiscountval)).toFixed(numberOfDecimal);

	if(parseFloat(amount) >= parseFloat("0")){
		$(obj).closest('tr').find("td:nth-child(9)").find('input[name=item_discount]').val(itemDiscount);
		$(obj).closest('tr').find("td:nth-child(11)").find('input[name=amount]').val(amount);
	}else{
		alert("discout should be less than the amount");
		if(discounType == "%"){
			$(obj).closest('tr').find("td:nth-child(9)").find('input[name=item_discount]').val(parseFloat("0").toFixed(3));
		}else{
			$(obj).closest('tr').find("td:nth-child(9)").find('input[name=item_discount]').val(parseFloat("0").toFixed(numberOfDecimal));
		}
		var originalamount = parseFloat((qty*cost)).toFixed(numberOfDecimal);
		$(obj).closest('tr').find("td:nth-child(11)").find('input[name=amount]').val(originalamount);
	}

	$(obj).closest('tr').find("td:nth-child(5)").find('input[name=QTY]').val(qty);
	$(obj).closest('tr').find("td:nth-child(7)").find('input[name=unitprice]').val(cost);

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
	var parent_plant = $("input[name=PARENT_PLANT]").val();
	var plant = $("input[name=plant]").val();
	var Salestax = $("input[name=Salestax]").val();
	var customer = $("input[name=CUST_NAME]").val();
	var dono = $("input[name=DONO]").val();
	var currency = $("input[name=CURRENCY]").val();
	var currencyuseqt = $("input[name=CURRENCYUSEQT]").val();//  Author: Azees  Create date: July 19,2021  Description:  Exchange Rate Issue
	var isEmployee = $("input[name=ISEMPLOYEEVALIDATESO]").val();
	var Employeee = $("input[name=EMP_NAME]").val();
	
	var msg = "";
	var isItemValid = true, isAccValid = true, isUnitPriceValid = true,
	isUnitPriceValid = true;
	//RESVI START
	var ValidNumber   = document.form1.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" order's you can create"); return false; }
	//RESVI END
	if(customer == ""){
		alert("Please select a Customer.");
		return false;
	}
	if(dono == ""){
		alert("Please Enter Order Number.");
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
	if(currencyuseqt == ""){
		alert("Please Enter Exchange Rate.");
			return false;
		}
		
	if(parent_plant == plant && Salestax==""){
		alert("Please select a Tax.");
		document.getElementById("Salestax").focus();
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
		minSellingPrice = parseFloat($('input[name ="CURRENCYUSEQT"]').val())*parseFloat(minSellingPrice);
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
//	$(obj).closest('tr').find("input[name=QTY]").tooltip("destroy");
	var content = "<p class='text-left'>Reorder Level: "+$(obj).closest('tr').find("input[name=QTY]").data("rl")+"</p>";
	content += "<p class='text-left'>Max Stock Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("msq")+"</p>";
	content += "<p class='text-left'>Stock on Hand: "+$(obj).closest('tr').find("input[name=QTY]").data("soh")+"</p>";
	content += "<p class='text-left'>Committed Stock(Sales order qty): "+$(obj).closest('tr').find("input[name=outgoingqty]").val()+"</p>";
	content += "<p class='text-left'>Sales Estimate Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("eq")+"</p>";
	content += "<p class='text-left'>Available Quantity: "+$(obj).closest('tr').find("input[name=QTY]").data("aq")+"</p>";
//	$(obj).closest('tr').find("input[name=QTY]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=QTY]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"}); 
}

function loadUnitPriceToolTip(obj){
//$(obj).closest('tr').find("input[name=unitprice]").tooltip("destroy");
	var cust_addoncost = parseFloat($("input[name=CUST_ADDONCOST]").val()).toFixed(3);
	var cust_addoncosttype = $("input[name=CUST_ADDONCOSTTYPE]").val();
	
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

	if(cust_addoncosttype=='1'){
		content += "<p class='text-left'>Add On Cost: "+cust_addoncost+" %</p>";
		}else{
		content += "<p class='text-left'>Add On Cost: "+cust_addoncost+"</p>";
		}
//	$(obj).closest('tr').find("input[name=unitprice]").tooltip({title: content, html: true, placement: "top"});
	$(obj).closest('tr').find("input[name=unitprice]").tooltip('hide').attr('data-original-title', content).tooltip({show: true,html: true, placement: "top"}); 
	
}

function showRemarksDetails(obj) {
	var lnno = $(obj).closest('tr').find("input[name=lnno]").val();
	var dono = $("input[name=DONO]").val();
	var item = $(obj).closest('tr').find("input[name=item]").val();

	if(item != ''){
		$.ajax({
			type : "GET",
			url : "../salesorder/getSalesOrderRemarks",
			data : {
				ITEM: item,
				DONO: dono,
				DOLNO: lnno
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
				body += '<input type="hidden" name="r_dono" value="'+dono+'">';
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
//	 var cus_companyregnumber   = document.form.cus_companyregnumber.value;
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

//	   if(region == "GCC"){
//		   document.form.cus_companyregnumber.value="";
//		}else if(region == "ASIA PACIFIC"){
//			if (cus_companyregnumber == "" || cus_companyregnumber == null) {
//			alert("Please Enter Unique Entity Number (UEN)");
//			document.form.cus_companyregnumber.focus();
//			return false; 
//			}
//		}

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
				$("#transport").typeahead('val', data.customer[0].TRANSPORTNAME);
				$("#PAYMENTTYPE").typeahead('val', data.customer[0].PAY_TERMS);
				$("input[name=payment_terms]").val(data.customer[0].PAYMENT_TERMS);
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
			PLANT : document.form1.plant.value,
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
    $("input[name ='CUST_SHIP_COUNTRY']").val(text.trim());
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

//function paymentTypeCallback(data){
//	if(data.STATUS="SUCCESS"){
//		$("#PAYMENTTYPE").typeahead('val', data.PAYMENTTYPE);
//	}
//}

//function transportCallback(data){
	//if(data.STATUS="SUCCESS"){
	//	$("#transport").typeahead('val', data.transport);
	//	$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
//	}
//}

function transportCallback(data){
	if(data.STATUS="SUCCESS"){
	if(document.form1.custModal.value=="cust"){
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
	if(document.form1.custModal.value=="cust")
	{
		$("input[name ='cpayment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=PMENT_DAYS]").val(payTermsData.NO_DAYS);
	} else {
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
	}
  }
}
function paymentTypeCallback(payTermsData)
{
	if(payTermsData.STATUS="100"){
	if(document.form1.custModal.value=="cust"){
		$("input[name ='CUST_PAYTERMS']").typeahead('val',payTermsData.PAYMENTMODE);
	} else {
		$("input[name ='PAYMENTTYPE']").typeahead('val', payTermsData.PAYMENTMODE);
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

//RESVI
function autoGen(){
	$.ajax({
		type: "GET",
		url: "../salesorder/Auto-Generate",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		},
		success: function(data) {
			$("#DONO").val(data.DONO);
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
function popUpWin(URL) {
	subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


/*---------------------------------------------------------*/

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
	
//	var cust_addoncost = parseFloat($("input[name=CUST_ADDONCOST]").val()).toFixed(3);
//	var CUST_ADDONCOST_DET = parseFloat($(obj).closest('tr').find('input[name=CUST_ADDONCOST_DET]').val()).toFixed(numberOfDecimal);
//	cost = parseFloat(parseFloat(cust_addoncost)+parseFloat(CUST_ADDONCOST_DET)).toFixed(numberOfDecimal);
	
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
	 
	 var CURRENCYUSEQT = $('input[name ="CURRENCYUSEQT"]').val();//Author: Azees  Create date: July 16,2021  Description:  Total of Local Currency
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
	var customer = $("input[name=CUST_NAME]").val();
	var replacePreviousSalesCost = $("input[name=replacePreviousSalesCost]").val();
	if(replacePreviousSalesCost=="1") {
	if(customer == ""){
		alert("Please select a Customer.");
		return false;
	}
	}
	var obj2 = $(obj).parent().find('input[name="item"]');
	$(obj2).typeahead('val', '');
	$(obj).parent().find('input[name="item"]').focus();
	
	var trid = $(obj).closest('tr').attr('id');
	addcustonsuggesiondropdown(trid);
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
	var aodcost = 0;
	var Ucost = 0;
	var aodType = 0;
	var ischange=0;

$('tr').each(function () {

	var qty = parseFloat($(this).find('input[name ="QTY"]').val()).toFixed(3);
	//cost = ((parseFloat($(this).find('input[name=unitprice]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
	cost = ((parseFloat($(this).find('input[name=unitpricerd]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
	$(this).find('input[name=unitpricerd]').val(cost);
	cost = parseFloat(cost).toFixed(numberOfDecimal);
	$(this).find('input[name=unitprice]').val(cost);
	$(this).find('input[name=CUST_ADDONCOST_DET]').val(cost);
	
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
			$(this).find('input[name=addontype]').val(CURRENCYID);
		}
	//imthi end

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
function productCallback(productData){
	if(productData.STATUS="SUCCESS"){
		var $tbody = $(".so-table tbody");
		var $last = $tbody.find('tr:last');
		 $last.remove();
		 var productId=productData.ITEM;
		 var catalogPath=productData.CATLOGPATH;
		 var account="Local sales - retail";
		 var cost=productData.UNITCOST;
		 var price=productData.UNITPRICE;
		 var salesuom=productData.SALESUOM;
		 var curency = document.form1.curency.value;
		 var numberOfDecimal = $("input[name=numberOfDecimal]").val();
			var currencyID = $("input[name=CURRENCYID]").val();
			var custCode = $("input[name=CUST_CODE]").val();
			var taxdisplay = $("input[name=ptaxdisplay]").val();
			var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
			var ITEM_DESC = escapeHtml(productData.ITEM_DESC);
			 
		var body="";
		body += '<tr>';
		body += '<td class="item-img text-center">';
		body += '<img alt="" src="'+productData.CATLOGPATH+'" style="width: 100%;">';
		body += '<input type="hidden" name="lnno" value="1">';
		body += '<input type="hidden" name="itemdesc" value="">';
		body += '<input type="hidden" name="unitpricerd" value="0.00">';
		body += '<input type="hidden" name="itemprice" value="0.00">';
	    body += '<input type="hidden" name="itemcost" value="">';
		body += '<input type="hidden" name="minstkqty" value="">';
		body += '<input type="hidden" name="maxstkqty" value="">';
		body += '<input type="hidden" name="stockonhand" value="">';
		body += '<input type="hidden" name="outgoingqty" value="">';
		body += '<input type="hidden" name="customerdiscount" value="">';
		body += '<input type="hidden" name="unitpricediscount" value="">';
		body += '<input type="hidden" name="discounttype" value="">';
		body += '<input type="hidden" name="customerdiscount" value="">';
		body += '<input type="hidden" name="minsp" value="">';
		body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
		body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="item" class="form-control itemSearch" value="'+productId+'" style="width:87%" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
		body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
		body += '<input class="form-control"  name="ITEMDES" value="'+ITEM_DESC+'" readonly style="height: 23px;background-color: #fff;" readonly>';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="account_name" class="form-control accountSearch" placeholder="Select Account" value="'+account+'">';
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="UOM" class="form-control uomSearch" placeholder="UOM" value="'+salesuom+'">';
		body += '</td>';
		body += '<td class="item-qty text-right"><input type="text" name="QTY" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" data-pq="0.000" data-iq="0.000" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
		body += '<td>';
		body += '<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" READONLY  tabindex="-1">';
		body += '</td>';
		body += '<td>';
		body += '<input type="text" name="unitcost" class="form-control text-right" value="0" readonly>';
		body += '</td>';
		body += '<td>';
		body += '<input type="text" name="addonshow" class="form-control text-right" value="'+szero+'" "'+curency+'" readonly>';
		body += '<input type="hidden" name="addonprice" value="'+szero+'">';
		body += '<input type="hidden" name="addontype" value="'+curency+'">';
		body += '</td>';
		body += "<td class=\"item-cost text-right\"><input type=\"text\" name=\"unitprice\" class=\"form-control text-right\" value="+cost+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\"><input type=\"hidden\" name=\"CUST_ADDONCOST_DET\" class=\"form-control text-right\" value="+cost+" onkeypress=\"return isNumberKey(event,this,4)\"></td>";
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
		//body += '<input type="hidden" name="tax_type" value="'+taxdisplay+'">';
		//body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
		//body += '</td>';
		body += '<td class="item-amount text-right grey-bg">';
		body += "<input name=\"amount\" type=\"text\" class=\"form-control text-right\" value="+cost+" readonly=\"readonly\" style=\"display:inline-block;\" tabindex=\"-1\">";
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
		loadItemData($tbody.find('tr:last'), productId, catalogPath, account, price, salesuom, cost);
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
		
function loadItemDataLow(obj,productId){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var cust_addoncost = parseFloat($("input[name=CUST_ADDONCOST]").val()).toFixed(3);
	var cust_addoncosttype = $("input[name=CUST_ADDONCOSTTYPE]").val();
	var addcustprice="";
	/* catalogPath, account, cost,salesuom, nonStkFlag,price */
	var urlStr = "/track/ItemMstServlet";
	
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ITEM : productId,
			ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION_FOR_ITEM"
			},
			dataType : "json",
			success : function(data) {
	
				var catalogPath = data.items.CATLOGPATH;
				var account = data.items.ACCOUNT;
				var cost = data.items.UNITPRICE;
				var salesuom = data.items.SALESUOM;
				var nonStkFlag = data.items.nonStkFlag;
				var price = data.items.COST;
	
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(price));
	$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat(price));
	$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(price));
	$(obj).closest('tr').find("input[name=itemcost]").val(parseFloat(cost));
	$(obj).closest('tr').find("input[name=UOM]").val(salesuom);
	
	var currencyID = $("input[name=CURRENCYID]").val();
	var currencyUSEQT = $("input[name=CURRENCYUSEQT]").val();
	var custCode = $("input[name=CUST_CODE]").val();
	var replacePreviousSalesCost = $("input[name=replacePreviousSalesCost]").val();

	 var dono = document.form1.DONO.value;
	 var urlStr = "/track/ItemMstServlet";
	 var discount;
	 if(replacePreviousSalesCost=="1") {
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

						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat("0.00000"));
							$(obj).closest('tr').find("input[name=unitpricerd]").val("0.00000");
						}else{
							if(cust_addoncosttype=='1'){
								addcustprice = (parseFloat(resultVal.ConvertedUnitCost)+((parseFloat(resultVal.ConvertedUnitCost)*parseFloat(cust_addoncost))/100));
							}else{
							addcustprice = (parseFloat(resultVal.ConvertedUnitCost)+parseFloat(cust_addoncost));
							}
							$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(addcustprice).toFixed(numberOfDecimal));
							$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
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
								if(cust_addoncosttype=='1'){
									calAmount = (parseFloat(calAmount)+((parseFloat(calAmount)*parseFloat(cust_addoncost))/100));
								}else{
							calAmount = (parseFloat(calAmount)+parseFloat(cust_addoncost));
								}
							$(obj).closest('tr').find("input[name=unitprice]").val(calAmount);
							$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(calAmount);
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
					$(obj).closest('tr').find('input[name="QTY"]').focus();
					$(obj).closest('tr').find('input[name="QTY"]').select();
				}
			});
		
			
	 } else if(replacePreviousSalesCost=="0"){

		 
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

							var auprice = 0;
							if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
								$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
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
								if(cust_addoncosttype=='1'){
									auprice = (parseFloat(auprice)+((parseFloat(auprice)*parseFloat(cust_addoncost))/100));
								}else{
								auprice = (parseFloat(auprice)+parseFloat(cust_addoncost));
								}
								$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(auprice).toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat(auprice).toFixed(numberOfDecimal));
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
								if(cust_addoncosttype=='1'){
									calAmount = (parseFloat(calAmount)+((parseFloat(calAmount)*parseFloat(cust_addoncost))/100));
								}else{
								calAmount = (parseFloat(calAmount)+parseFloat(cust_addoncost));
								}
								$(obj).closest('tr').find("input[name=unitprice]").val(calAmount);
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(calAmount);
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
						$(obj).closest('tr').find('input[name="QTY"]').focus();
						$(obj).closest('tr').find('input[name="QTY"]').select();
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
					REPLACEPREVIOUSSALESCOST: replacePreviousSalesCost,
					//ACTION : "GET_PRODUCT_DETAILS"
					ACTION :"GET_PRODUCT_AUTO_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						//outgoingOBDiscount
						if (data.status == "100") {
							var resultVal = data.result;
							var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;

							if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
								$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
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
								
								if(replacePreviousSalesCost==3) {
								$(obj).closest('tr').find("input[name=addonshow]").val(addshow);
								$(obj).closest('tr').find("input[name=addonprice]").val(resultVal.incprice);
								$(obj).closest('tr').find("input[name=addontype]").val(resultVal.incpriceunit);
								$(obj).closest('tr').find("input[name=itemprice]").val(parseFloat(auprice));
								}
								//imthi end
								if(cust_addoncosttype=='1'){
								addcustprice = (parseFloat(resultVal.ConvertedUnitCost)+((parseFloat(resultVal.ConvertedUnitCost)*parseFloat(cust_addoncost))/100));
								}else{
								addcustprice = (parseFloat(resultVal.ConvertedUnitCost)+parseFloat(cust_addoncost));
								}
								$(obj).closest('tr').find("input[name=unitprice]").val(parseFloat(addcustprice).toFixed(numberOfDecimal));
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
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
								if(cust_addoncosttype=='1'){
									calAmount = (parseFloat(calAmount)+((parseFloat(calAmount)*parseFloat(cust_addoncost))/100));
								}else{
								calAmount = (parseFloat(calAmount)+parseFloat(cust_addoncost));
								}
								$(obj).closest('tr').find("input[name=unitprice]").val(calAmount);
								$(obj).closest('tr').find("input[name=CUST_ADDONCOST_DET]").val(calAmount);
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
						$(obj).closest('tr').find('input[name="QTY"]').focus();
						$(obj).closest('tr').find('input[name="QTY"]').select();
					}
				});

	 }
		
			}
	});
}

function addcustonsuggesiondropdown(rowid){
	removeSuggestionToTable();
	var plant = document.form1.plant.value;
	var replacePreviousSalesCost = $("input[name=replacePreviousSalesCost]").val();
	if(replacePreviousSalesCost=="1") {
		/* To get the suggestion data for Product */
	$("#"+rowid+" .itemSearch").typeahead({
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
				//ACTION : "GET_INVOICE_PRODUCT_LIST_FOR_SUGGESTION",
				ACTION : "GET_INVOICE_PRODUCT_LIST_AUTO_SUGGESTION",
				CUSTNO : $("input[name=CUST_CODE]").val(),
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
		//	return '<div onclick="loadItemData(this,\''+ data.ITEM+'\',\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.SALESUOM+'\',\''+ data.COST+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';			
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
//			loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.SALESUOM,selection.COST);
			loadItemDataLow(this,selection.ITEM);
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				$(this).closest('tr').find('input[name="ITEMDES"]').val("");
			}		
		});
		
		//Imthiyas added (press Enter Key for item to appear)
			$("#"+rowid+" .itemSearch").on('keydown', function(event) {
			    if (event.key === 'Enter') {
			        const item = $(this).val();
			        $.ajax({
			            type: "POST",
			            url: "../ItemMstServlet",
			            data: {
			                ACTION : "GET_INVOICE_PRODUCT_LIST_AUTO_SUGGESTION",
				            CUSTNO : $("input[name=CUST_CODE]").val(),
			                ITEM: item
			            },
			            dataType: "json",
			            success: function(data) {
			                if (data.items && data.items.length > 0) {
			                    const selection = data.items[0];
			                    loadItemDataLow(this,selection.ITEM);
			                    $(this).closest('tr').find("input[name='ITEMDES']").val(selection.ITEMDESC);
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
		
	} else {

		/* To get the suggestion data for Product */
		$("#"+rowid+" .itemSearch").typeahead({
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
//					ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
					ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION_LOW",
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
				//	return '<div onclick="loadItemData(this,\''+ data.ITEM+'\',\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.SALESUOM+'\',\''+ data.COST+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
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
//				loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.SALESUOM,selection.COST);
				loadItemDataLow(this,selection.ITEM);
				$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".bigdrop");
				setTimeout(function(){ menuElement.next().hide();}, 150);
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					$(this).closest('tr').find('input[name="ITEMDES"]').val("");
				}	
			});
			
			//Imthiyas added (press Enter Key for item to appear)
			$("#"+rowid+" .itemSearch").on('keydown', function(event) {
			    if (event.key === 'Enter') {
			        const item = $(this).val();
			        $.ajax({
			            type: "POST",
			            url: "../ItemMstServlet",
			            data: {
			                ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION_LOW",
			                ITEM: item
			            },
			            dataType: "json",
			            success: function(data) {
			                if (data.items && data.items.length > 0) {
			                    const selection = data.items[0];
			                    loadItemDataLow(this,selection.ITEM);
			                    $(this).closest('tr').find("input[name='ITEMDES']").val(selection.ITEMDESC);
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

	}
/* To get the suggestion data for Account */
	$("#"+rowid+" .accountSearch").typeahead({
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


	$('#'+rowid+' .uomSearch').typeahead({
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

	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	});

		$("#"+rowid).each(function () {
			$(this).find("input[name ='item']").focus();
		});
}


function addRow2(event){
	event.preventDefault();
    event.stopPropagation();
	var replacePreviousSalesCost = $("input[name=replacePreviousSalesCost]").val();
	if(replacePreviousSalesCost=="1") {
		var customer = $("input[name=CUST_NAME]").val();
		if(customer == ""){
			alert("Please select a Customer.");
			return false;
		}
	}
	var curency = $("input[name=CURRENCYID]").val();
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var taxdisplay = $("input[name=ptaxdisplay]").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var rowid = Date.now().toString(36) + Math.random().toString(36).substr(2);
	var body="";
	body += '<tr id="'+rowid+'">';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">';
	body += '<input type="hidden" name="lnno" value="1">';
	body += '<input type="hidden" name="itemdesc" value="">';
	body += '<input type="hidden" name="unitpricerd" value="0.00">';
	body += '<input type="hidden" name="itemprice" value="0.00">';
	body += '<input type="hidden" name="itemcost" value="">';
	body += '<input type="hidden" name="minstkqty" value="">';
	body += '<input type="hidden" name="maxstkqty" value="">';
	body += '<input type="hidden" name="stockonhand" value="">';
	body += '<input type="hidden" name="outgoingqty" value="">';
	body += '<input type="hidden" name="unitpricediscount" value="">';
	body += '<input type="hidden" name="discounttype" value="">';
	body += '<input type="hidden" name="customerdiscount" value="">';
	body += '<input type="hidden" name="minsp" value="">';
	body += '<input type="hidden" name="tax" class="taxSearch" value="'+taxdisplay+'">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" class="form-control itemSearch" onclick="loaddropdata(this)" onchange="checkitems(this.value,this)"  style="width:87%" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" data-pd="" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';
	body += '<td>';
	body += '<input type="text" name="account_name" class="form-control accountSearch" onchange="checkaccount(this.value,this)" placeholder="Account"  value="Local sales - retail">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="UOM" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="Select UOM">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" name="QTY" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)"></td>';
	body += '<td>';
	body += '<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" READONLY  tabindex="-1">';
	body += '</td>';
	body += '<td>';
	body += '<input type="text" name="unitcost" class="form-control text-right" value="'+szero+'" readonly  tabindex="-1">';
	body += '</td>';
	body += '<td>';
	body += '<input type="text" name="addonshow" class="form-control text-right" value="'+szero+'" readonly  tabindex="-1">';
	body += '<input type="hidden" name="addonprice" value="'+szero+'">';
	body += '<input type="hidden" name="addontype" value="'+curency+'">';
	body += '</td>';
	body += "<td class=\"item-cost text-right\"><input type=\"text\" name=\"unitprice\" class=\"form-control text-right\" value="+szero+" onchange=\"calculateAmount(this)\" onkeypress=\"return isNumberKey(event,this,4)\"><input type=\"hidden\" name=\"CUST_ADDONCOST_DET\" class=\"form-control text-right\" value="+szero+" onkeypress=\"return isNumberKey(event,this,4)\"></td>";
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
	/*body += '<td class="item-tax">';
	body += '<input type="hidden" name="tax_type">';
	body += '<input type="text" name="tax" class="form-control taxSearch" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
	body += '</td>';*/
	body += '<td class="item-amount text-right grey-bg">';
	body += "<input name=\"amount\" type=\"text\" class=\"form-control text-right\" value="+szero+" readonly=\"readonly\" style=\"display:inline-block;\" tabindex=\"-1\">";
	body += '</td>';
//	body += '<td>';
//	body += '<input type="text" name="PRODUCTDELIVERYDATE" class="form-control datepicker" READONLY>';
//	body += '</td>';
	body += '<td class="table-icon" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += '<a href="#" onclick="showRemarksDetails(this)">';
	body += '<i class="fa fa-comment-o" title="Add Product Remarks" style="font-size: 15px;"></i>';
	body += '</a>';
	body += '</td>';
	body += '</tr>';
	$(".so-table tbody").append(body);
	if($("input[name=DEDUCT_INV]").val() == "0"){
		$(".invEl").hide();
	}
	setLineNo();
	addcustonsuggesiondropdown(rowid);
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
	
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	});
}

function loaddropdata(obj){
	console.log("onclick working");
	var trid = $(obj).closest('tr').attr('id');
	addcustonsuggesiondropdown(trid);
}		



function seltax(gst){	
	var plant = document.form1.plant.value;
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_TAX_TYPE_DATA_PO",
				SALESLOC : $("input[name=STATE_PREFIX]").val(),
				GST_PERCENTAGE : $("input[name=GST]").val(),
				TAXKEY : "OUTBOUND",
				GSTTYPE : gst
				},
				dataType : "json",
				success : function(data) {
					const selection = data.records[0];
					var ggst = $("input[name=GST]").val();
					$("input[name=Salestax]").val("STANDARD RATED ["+ggst+"%]");
					calculateTaxPO("", selection.SGSTTYPES, selection.SGSTPERCENTAGE, selection.DISPLAY, selection.ISZERO, selection.ISSHOW, selection.ID)
				}
			});
		 return true;
}