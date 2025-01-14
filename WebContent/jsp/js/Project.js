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
			url: "../project/Auto-Generate",
			dataType: "json",
			beforeSend: function(){
				showLoader();
			}, 
			success: function(data) {
				$("#PRNO").val(data.PRNO);
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
			   return '<div onclick="setCustomerData(\''+data.TAXTREATMENT+'\',\''+data.CUSTNO
			   +'\',\''+data.CNAME+'\',\''+data.NAME+'\',\''+data.TELNO+'\',\''+data.EMAIL
			   +'\',\''+data.ADDR1+'\',\''+data.ADDR2+'\',\''+data.ADDR3+'\',\''+data.ADDR4
			   +'\',\''+data.REMARKS+'\',\''+data.COUNTRY+'\',\''+data.ZIP
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
				$('select[name ="TAXTREATMENT"]').val("");
				$('select[name=TAXTREATMENT]').attr('disabled',false);
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
			}
		}).on('typeahead:select',function(event,selection){
			$("#project").typeahead('val', '"');
			$("#project").typeahead('val', '');
			$("input[name=PROJECTID]").val('');
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
	
	
	$("#btnSalesOpen").click(function(){
		   var CUST_NAME   = document.form1.CUST_NAME.value;
		   var PRNO = document.form1.PRNO.value;
		   var PR_NAME = document.form1.PR_NAME.value;
		   var DELDATE = document.form1.DELDATE.value;
		   var EXPIRYDATE= document.form1.EXPIRYDATE.value;
		   var PRESTCOST = document.form1.PRESTCOST.value;
		   var isValid = validateSalesOrder();
			if(isValid){
			var formData = new FormData($('#salesOrderForm')[0]);
		    $.ajax({
		        type: 'POST',
		        url: "../project/new",
			    data:  formData,
			    contentType: false,
			    processData: false,
		        success: function (data) {
		        	if(data.ERROR_CODE == 100)
		        		window.location.href="../project/summary?msg="+data.MESSAGE;
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
	
	
	$('select[name="COUNTRY_CODE_C"]').on('change', function(){		
	    var text = $("#COUNTRY_CODE_C option:selected").text();
	    $("input[name ='COUNTRY']").val(text.trim());
	});
	
	/* Bank Name Auto Suggestion */
$('#BANKNAMECUS').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'NAME',  
	  source: function (query, process,asyncProcess) {
		  var urlStr = "../BankServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT :"<%=plant%>",
					action : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
					NAME : query
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
	    	  return '<p onclick="document.form.BRANCH.value = \''+data.BRANCH_NAME+'\'">'+data.NAME+'</p>';
		}
	  }

	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
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

/* To get the suggestion data for Currency */
$("#CUS_CURRENCY").typeahead({
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
		return '<div onclick="document.form.CURRENCYID_C.value = \''+data.CURRENCYID+'\'"> <p>'+data.CURRENCY+'</p></div>';

		}
	  }
}).on('typeahead:render',function(event,selection){
	  
}).on('typeahead:open',function(event,selection){
	
}).on('typeahead:close',function(){

}).on('typeahead:change',function(event,selection){
	if($(this).val() == "")
		$("input[name ='CURRENCYID_C']").val("");	
});
	
});/* document ready*/


function validateSalesOrder(){
	var customer = $("input[name=CUST_NAME]").val();
	var CUST_CODE = $("input[name=CUST_CODE]").val();
	var prno = $("input[name=PRNO]").val();
	var PR_NAME = $("input[name=PR_NAME]").val();
	var CL = $("input[name=PRESTCOST]").val();
	var msg = "";
	
	if(customer == ""){
		alert("Please select a Customer.");
		return false;
	}
	if(CUST_CODE == ""){
		alert("Please select a Customer.");
		return false;
	}	
	if(prno == ""){
		alert("Please Enter Project Number.");
		return false;
	}
	if(PR_NAME == ""){
		alert("Please Enter Project Name.");
		return false;
	}	
	
	if(!IsNumeric(CL))
	 {
	   alert(" Please Enter Project Estimate Cost Input In Number");
	   return false;
	 }
	
	return true;
}

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
//jsp validation ends

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
$('select[name="COUNTRY_CODE"]').on('change', function(){		
    var text = $("#COUNTRY_CODE option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
});
$('select[name="COUNTRY_CODE_C"]').on('change', function(){		
    var text = $("#COUNTRY_CODE_C option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
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
function onAdd(){
	 var CUST_CODE   = document.form.CUST_CODE_C.value;
	 var CUST_NAME   = document.form.CUST_NAME_C.value;
	 var PEPPOL_ID   = document.form.PEPPOL_ID.value;
	 var PEPPOL   = document.form.PEPPOL.value;
	// var cus_companyregnumber   = document.form.cus_companyregnumber.value;
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
		 

if(document.getElementById("PEPPOL").checked == true)
	PEPPOL="1";
 else 
	PEPPOL="0";

   if(PEPPOL_ID == "" &&  PEPPOL == "1") {
 	  alert("Please Enter Peppol Id"); 
 	return false; 
}
	 
	 
//   Author Name:Resviya ,Date:9/08/21 , Description -UEN Alert    

//	   if(region == "GCC"){
//	   document.form.cus_companyregnumber.value="";
//		}else if(region == "ASIA PACIFIC"){
//		if (cus_companyregnumber == "" || cus_companyregnumber == null) {
//		alert("Please Enter Unique Entity Number (UEN)");
			document.form.cus_companyregnumber.focus();
//		return false; 
//		}
//	}

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
			if(document.form1.custModal.value == "cust"){
				//alert(JSON.stringify(data));
				$("input[name ='CUST_CODE']").val(data.customer[0].CID);
				//document.getElementById("CUST_CODE").value  = data.customer[0].CID;
				document.getElementById("CUSTOMER").value  = data.customer[0].CName;
				$("input[name ='CUSTOMER']").val(data.customer[0].CName);
				//$("input[name ='TAXTREATMENT_VALUE']").val(data.customer[0].CTAXTREATMENT);				
//				document.getElementById('TAXTREATMENT').value = data.customer[0].CTAXTREATMENT;
				//document.getElementById('nTAXTREATMENT').value = data.customer[0].CTAXTREATMENT;
				document.form.reset();
				$('#customerModal').modal('hide');
			}else{
				document.form.reset();
				document.form1.SHIPPINGCUSTOMER.value = data.customer[0].CName;
				document.form1.SHIPPINGID.value = data.customer[0].CID;
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

function setCustomerData(TAXTREATMENT,CUSTNO,CNAME,NAME,TELNO,EMAIL,ADD1,ADD2,ADD3,ADD4,REMARKS,COUNTRY,ZIP,PAYMENTTYPE){
	$('select[name ="TAXTREATMENT"]').val(TAXTREATMENT);
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
	$("input[name=PAYMENTTYPE]").val(PAYMENTTYPE);
}

function showLoader(){
	$("#loader").show();
	$(".wrapper").css("opacity","0.1");
}

function hideLoader(){
	$("#loader").hide();
	$(".wrapper").css("opacity","1");
}
//RESVI
function autoGen(){
$.ajax({
	type: "GET",
	url: "../project/Auto-Generate",
	dataType: "json",
	beforeSend: function(){
		showLoader();
	}, 
	success: function(data) {
		$("#PRNO").val(data.PRNO);
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
			$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					   $('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
				 $('#C_STATE').empty();
					$('#C_STATE').append('<OPTION style="display:none;">Select State</OPTION>');
						 $.each(StateList, function (key, value) {
							   $('#C_STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
							});
		}
	});	
	
}
function removetaxtrestl(){
	$('select[name=TAXTREATMENT]').attr('disabled',false);
}
function paymentTypeCallback(data){
	if(data.STATUS="SUCCESS"){
//		$("#payment_type").typeahead('val', data.PAYMENTMODE);
//		$("#payment_type").typeahead('val', data.PAYMENTTYPE);
	if(document.form1.custModal.value=="shipcust"){
		$("#PAYTERMS").typeahead('val', data.PAYMENTMODE);
		}else if(document.form1.custModal.value=="cust"){
		$("#CUST_PAYTERMS").typeahead('val', data.PAYMENTMODE);
		}else{
		$("#payment_type").typeahead('val', data.PAYMENTMODE);
		}
	}
}

function transportCallback(data){
	if(data.STATUS="SUCCESS"){
	if(document.form1.custModal.value=="shipcust"){
		$("#transportC").typeahead('val', data.transport);
		$("input[name=TRANSPORTIDC]").val(data.TRANSPORTID);
		}else if(document.form1.custModal.value=="cust"){
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
	if(document.form1.custModal.value=="shipcust"){
		$("input[name ='sup_payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=SUP_PMENT_DAYS]").val(payTermsData.NO_DAYS);
		}else if(document.form1.custModal.value=="cust"){
		$("input[name ='cpayment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=PMENT_DAYS]").val(payTermsData.NO_DAYS);
		}else{
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		}
	}
}
function bankCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#BANKNAMECUS").typeahead('val', data.BANK_NAME);
		  $("input[name ='BRANCH']").val(data.BANK_BRANCH);
		
	}
}
