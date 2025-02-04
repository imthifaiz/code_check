var invoiceList=null;
var amountreceived=0;
var paidamounttotal=0;
function paidamountchanged(invoiceamt,paidnode)
{
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var invoiceamount=parseFloat(invoiceamt);
	var paidamount=parseFloat(paidnode.value);
		if(isNaN(paidamount)){
		paidamount = "0";
	}

	if(paidamount>invoiceamount)
		{
			alert("Please enter amount equal/less than invoice amount");
			paidnode.value=invoiceamount;
		}
	else
		{
			paidnode.value=parseFloat(paidamount).toFixed(numberOfDecimal);
		}
	paidamounttotal=totalpaidamount();
	/*if(paidamounttotal>amountreceived)
		{
			alert("Please enter amount equal/less than received amount");
			paidnode.value=parseFloat("0").toFixed(numberOfDecimal);
		}*/
		
		if ($('#paycaltype').is(":checked")){
  		autocalculatebasecurrency(parseFloat(paidamounttotal).toFixed(numberOfDecimal));
	}else{
		if(paidamounttotal>amountreceived)
			{
				var difamt = parseFloat(paidnode.value)-(parseFloat(paidamounttotal)-parseFloat(amountreceived))
				alert("Please enter amount equal/less than received amount.");
				paidnode.value=parseFloat(difamt).toFixed(numberOfDecimal);
			}
	}
	
	callTotalDetail(numberOfDecimal);
}

function autocalculatebasecurrency(amountnode){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = document.getElementById("CURRENCYUSEQT").value;
	$("input[name ='amount_Curr']").val(amountnode);
	$("input[name ='conv_amount_Curr']").val(amountnode);
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='amount']").val(baseamount);
	calculateTotalbasecurrency(baseamount);

}

function v_amountchanged(amountnode)
{

	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	if(amountnode.value.match(decimal) || amountnode.value.match(numbers)) 
	{ 
		var numberOfDecimal = document.getElementById("numberOfDecimal").value;
		var amount=parseFloat(amountnode.value).toFixed(numberOfDecimal);
		$("input[name ='v_amount']").val(amount);
		$("input[name ='v_cheque-amount']").each(function() {
		    $(this).val(parseFloat("0.0").toFixed(numberOfDecimal));
		});
		v_calculatetotal();
		
		//v_calculatecurrency();
		
	}else{
		$("input[name ='v_amount']").val(parseFloat("0").toFixed(numberOfDecimal));
		alert("Please Enter Valid Amount");
	}
}


function amountchanged(amountnode)
{
	/*var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var amount=parseFloat(amountnode.value).toFixed(numberOfDecimal);
	document.getElementById('amountreceived').innerHTML=amount;
	amountreceived=amount;*/
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	if(amountnode.value.match(decimal) || amountnode.value.match(numbers)) 
	{ 
		var numberOfDecimal = document.getElementById("numberOfDecimal").value;
		var amount=parseFloat(amountnode.value).toFixed(numberOfDecimal);
		document.getElementById('amountreceived').innerHTML=amount;
		$("input[name ='amount']").val(amount);
		amountreceived=amount;
		$("input[name ='cheque-amount']").each(function() {
		    $(this).val(parseFloat("0.0").toFixed(numberOfDecimal));
		});
		calculatetotal();
		
		//calculatecurrency();
		
	}else{
		$("input[name ='amount']").val('0');
		document.getElementById('amountreceived').innerHTML='';
		alert("Please Enter Valid Amount");
	}
	callTotalDetail(numberOfDecimal);
}



function callTotalDetail(numberOfDecimal)
{
	
	var totalamount=0;
	totalamount=totalpaidamount();
	var totalInvoiceAmount=parseFloat(totalamount).toFixed(numberOfDecimal);
	document.getElementById("totalInvoiceAmount").innerHTML=totalInvoiceAmount;
	document.getElementById("amountufp").innerHTML=totalInvoiceAmount;
	var amountExcess=document.getElementById("amount").value-totalInvoiceAmount;
	amountExcess=parseFloat(amountExcess).toFixed(numberOfDecimal);
	document.getElementById("amountexcess").innerHTML=amountExcess;
}
function totalpaidamount()
{
	var numberOfDecimal = $("#numberOfDecimal").val();
	var x = document.getElementsByClassName("paymentamountclass");
	var i;
	var totalamount=0;
	var amount;
	var billamt =0;
	for (i = 0; i < x.length; i++) {
		amount=parseFloat(x[i].value);
		totalamount+=parseFloat(amount);
		billamt+=parseFloat(amount);
	}
//	$('input[name = "imti"]').each(function() {
//		amount=$(this).val();
//		totalamount+=parseFloat(amount);
//		billamt+=parseFloat(amount);
//	});
	$("td.tpayamt").html(parseFloat(totalamount).toFixed(numberOfDecimal));
//	calculatebasecurrency_new(totalamount);
	calculatebasecurrency_new($("input[name ='amount_Curr']").val());
	return totalamount;
}
function customerchanged()
{
	$("#invoicelisttablebody").html("");
	var custno=document.form1.CUST_CODE.value;
	var Decimal= document.form1.NOOFDECIMAL.value;
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;

    if(custno!=""||custno!=null)
    	{
		var urlStr = "/track/InvoicePayment?CMD=getInvoiceByCust";
		$.ajax( {
			type : "GET",
			url : urlStr,
			data: {
				custnumber:custno
			},
	        success: function (data) {
	        	$("#invoicelisttablebody").html("");
	        	var json = $.parseJSON(data);
	        	invoiceList=json.custlist;
	        	var tbillamt = 0;
	        	var tcbillamt = 0;
	        	var tbilldue = 0;
	        	if(invoiceList.length>0)
        		{
	        		$.each(json.custlist,function(i,v){
	        			
	        			var balanceamount = "0";
	        			var urlStr = "/track/InvoicePayment?CMD=getbalanceofinvoice";
	        			$.ajax( {
	        				type : "GET",
	        				url : urlStr,
	        				data: {
	        					CUSTNO:custno,
	        					DONO:v.DONO,
	        					INVID:v.ID
	        				},
	        		        success: function (data1) {
	        		    
	        		        	var obj = JSON.parse(data1);
	        		        	
	        		        	console.log(obj);
	        		        	var zeroshow = parseFloat("0").toFixed(numberOfDecimal);
	        		        	balanceamount = v.TOTAL_AMOUNT - obj.BALANCE;
	        		        	balanceamount = parseFloat(balanceamount).toFixed(numberOfDecimal);
	        			
	        		        	if(balanceamount > 0){
					        		var stringjson=JSON.stringify(v);
					        		var amount1=parseFloat(v.TOTAL_AMOUNT);
						        	var amount=amount1.toFixed(Decimal);
						        	var covunitCostValue= (parseFloat(v.TOTAL_AMOUNT)*parseFloat(v.CURRENCYUSEQT));
					        		var key="invoice_"+v.ID+"_"+v.INVOICE+"_"+v.DONO+"_"+v.INVOICE_DATE+"_"+amount;
					        		var tableapp =null;
					        		tableapp ="<tr>"+
					        				"<td class='text-center'>"+v.INVOICE+"</td>"+
					        				"<td class='text-center'>"+v.DONO+"</td>"+
					        				"<td class='text-center'>"+v.INVOICE_DATE+"</td>"+
					        				"<td class='text-center'>"+parseFloat(covunitCostValue).toFixed(numberOfDecimal)+"</td>"+
					        				"<td class='text-center'>"+parseFloat(v.CURRENCYUSEQT).toFixed(numberOfDecimal)+"</td>"+
					        				"<td class='text-center'>"+amount+"</td>"+
					        				"<td class='text-center'>"+balanceamount+"</td>";
					        		if(obj.CREDIT > 0){
					        			tableapp += "<td class='text-center'><input type='text' style='border:1px solid #eee' disabled class='paymentamountclass' id="+key+" name="+key+" value='"+zeroshow+"' onchange='paidamountchanged("+balanceamount+",this)' onkeypress='return isNumberKey(event,this,4)'></input></td>";
//					        			tableapp += "<td class='text-center'><input type='text' style='border:1px solid #eee' disabled class='paymentamountclass' id="+key+" name='imti' value='"+zeroshow+"' onchange='paidamountchanged("+balanceamount+",this)' onkeypress='return isNumberKey(event,this,4)'></input></td>";
					        			tableapp +=	'<td class="text-center"><button type="button" class="btn btn-default acredits" onClick="aCredit('+v.ID+',\''+v.DONO+'\',\''+v.CUSTNO+'\',\''+v.INVOICE+'\',\''+balanceamount+'\',\''+v.CURRENCYID+'\',\''+v.CURRENCYUSEQT+'\')" id="applycrd" data-toggle="modal" data-target="#applycredit">Apply Credits</button></td>';
					        			tableapp +=	"</tr>";
					        		}else{
					        			tableapp += "<td class='text-center'><input type='text' style='border:1px solid #eee' class='paymentamountclass' id="+key+" name="+key+" value='"+zeroshow+"' onchange='paidamountchanged("+balanceamount+",this)' onkeypress='return isNumberKey(event,this,4)'></input></td>";
//					        			tableapp += "<td class='text-center'><input type='text' style='border:1px solid #eee' class='paymentamountclass' id="+key+" name='imti' value='"+zeroshow+"' onchange='paidamountchanged("+balanceamount+",this)' onkeypress='return isNumberKey(event,this,4)'></input></td>";
					        			if(obj.ALLCREDIT > 0){
					        				tableapp +=	'<td class="text-center"><button type="button" class="btn btn-default acredits" onClick="aCredit('+v.ID+',\''+v.DONO+'\',\''+v.CUSTNO+'\',\''+v.INVOICE+'\',\''+balanceamount+'\',\''+v.CURRENCYID+'\',\''+v.CURRENCYUSEQT+'\')"  data-toggle="modal" data-target="#applycredit">Apply Credits</button></td>';
					        			}else{
					        				tableapp +=	"<td class='text-center'>NO CREDITS</td>";
					        			}
					        			
					        			tableapp +=	"</tr>";
					        		}
			        		
			        		
					        		$("#invoicelisttablebody").append(tableapp);
					        		
					        		tbillamt = parseFloat(tbillamt)+parseFloat(covunitCostValue);
						        	tcbillamt = parseFloat(tcbillamt)+parseFloat(amount);
						        	tbilldue = parseFloat(tbilldue)+parseFloat(balanceamount);
						        	
						        	/*$("td.tbillamt").html("");
						        	$("td.tcbillamt").html("");
						        	$("td.tbilldue").html("");*/
						        	
						        	$("td.tbillamt").html(parseFloat(tbillamt).toFixed(numberOfDecimal));
						        	$("td.tcbillamt").html(parseFloat(tcbillamt).toFixed(numberOfDecimal));
						        	$("td.tbilldue").html(parseFloat(tbilldue).toFixed(numberOfDecimal));
	        		        	}
	        		        }
	        			});
		        		
		        		
		        		
		        		});
        		}
	        	else
        		{
        			$("#invoicelisttablebody").html("");
        			$("td.tbillamt").html(parseFloat(tbillamt).toFixed(numberOfDecimal));
	    	        	$("td.tcbillamt").html(parseFloat(tcbillamt).toFixed(numberOfDecimal));
	    	        	$("td.tbilldue").html(parseFloat(tbilldue).toFixed(numberOfDecimal));
        		}
	        	
//	        	callTotalDetail(numberOfDecimal);
	        	sortTable();
	            
	        },
	        cache: false
			   });
    	}
    
    sortTable();
//window.location.href="invoicePaymentNew.jsp?CUSTNO="+custno;
}




var subWin = null;
function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
$(document).ready(function(){
	var plant = document.form1.plant.value;
	var cmd = document.form1.cmd.value;
	var TranId = document.form1.TranId.value;
	$( ".paymentamountclass" ).prop( "disabled", false );
	
	$(".payment-table tbody").on('click','.payment-action',function(){
	    $(this).parent().parent().remove();
	    calculatetotal();
	});
	
	$(".voucher-table tbody").on('click','.voucher-action',function(){
	    $(this).parent().parent().remove();
	    v_calculatetotal();
	});
	
/*	
	$('#invoicePaymentForm').submit(function(event) {
		var urlStr = "/track/InvoicePayment?CMD=SAVE";
		event.preventDefault();
		if(validatePayment()){
	        var formData = new FormData(document.getElementById("invoicePaymentForm"));
	        formData.append("invoicelist",invoiceList);
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				enctype: 'multipart/form-data',
	            xhr: function() {
	                var myXhr = $.ajaxSettings.xhr();
	                return myXhr;
	            },
	            success: function (data) {
	                window.location.href="invoicePaymentSummary.jsp";
	            },
	            data: formData,
	            cache: false,
	            contentType: false,
	            processData: false
				   });
		}
	 });
	
	$('#voucherPaymentForm').submit(function(event) {
		var urlStr = "/track/InvoicePayment?CMD=V_SAVE";
		event.preventDefault();
		if(v_validatePayment()){
	        var formData = new FormData(document.getElementById("voucherPaymentForm"));
	        formData.append("invoicelist",invoiceList);
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				enctype: 'multipart/form-data',
	            xhr: function() {
	                var myXhr = $.ajaxSettings.xhr();
	                return myXhr;
	            },
	            success: function (data) {
	                window.location.href="invoicePaymentSummary.jsp";
	            },
	            data: formData,
	            cache: false,
	            contentType: false,
	            processData: false
				   });
		}
	 });*/
	
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
	    	//return '<p onclick="document.form1.CUST_CODE.value = \''+data.CUSTNO+'\'">' + data.CNAME + '</p>';
	    	return '<p onclick="custnoandbank(\''+data.CUSTNO+'\',\''+data.BANKNAME+'\')">' + data.CNAME + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		} 
		menuElement.after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');
		menuElement.next().width(menuElement.width());
		menuElement.next().css({ "top": top,"padding":"3px 20px" });						
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
		customerchanged();
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == ""){
			document.form1.CUST_CODE.value = "";
		}
		/* To reset Order number Autosuggestion*/
		$("#ORDERNO").typeahead('val', '"');
		$("#ORDERNO").typeahead('val', '');
		
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

/* project Auto Suggestion */
$('#v_project').typeahead({
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
		$("input[name=v_PROJECTID]").val(selection.ID);
	});

/* Payment Mode Auto Suggestion */
/*$("#payment_mode").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{	  
	  display: 'PAYMENTTYPE',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "GET_PAYMENT_TYPE_LIST",
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
		menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentTypeModal"><a href="#"> + New Payment Mode</a></div>');
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
	});*/


/* Payment Mode Auto Suggestion */
/*$("#v_payment_mode").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{	  
	  display: 'PAYMENTTYPE',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "GET_PAYMENT_TYPE_LIST",
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
		menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentTypeModal"><a href="#"> + New Payment Mode</a></div>');
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
	});*/

/* Payment Mode Auto Suggestion */
$("#payment_mode").typeahead({
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
				if(pthrough != ""){
					$("#paid_through_account_name").typeahead('val', '');
					alert("Please select Bank Balance account type");
				}
			}
		}
	});

/* Payment Mode Auto Suggestion */
$("#v_payment_mode").typeahead({
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
	}).on('typeahead:select',function(event,selection){
		//loadItemData(this, selection.ITEM,selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.PURCHASEUOM);
		var isbank = $("input[name ='v_isbank']").val();
		var vpthrough = $("input[name ='v_paid_through_account_name']").val();
		if(selection.PAYMENTMODE != "Cheque"){
			$("input[name ='v_paidpdcstatus']").val("0");
			$("input[name ='v_bank_charge']").val("");
			$("#voucher_bankCharge").attr("readonly", true);
			$(".v_hidepdc").hide();
		}else{
			if(isbank == "1"){
				v_addnewRow();
				$("input[name ='v_paidpdcstatus']").val("1");
				$("#voucher_bankCharge").attr("readonly", false);
				$(".v_hidepdc").show();
			}else{
				$("input[name ='v_paidpdcstatus']").val("0");
				$("input[name ='v_bank_charge']").val("");
				$("#voucher_bankCharge").attr("readonly", true);
				$(".v_hidepdc").hide();
				if(vpthrough != ""){
					$("#v_paid_through_account_name").typeahead('val', '');
					alert("Please select Bank Balance account type");
				}
			}
		}
	});


$("#account_name").typeahead({
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
				module:"invoicepaymentvoucheraccount",
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
				module:"invoicepaymentdepositto",
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
					    '<span onclick="isbankcharge(this,\''+item.accountname+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
					  );
				}
			else
				{
				var $state = $(
						 '<span onclick="isbankcharge(this,\''+item.accountname+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
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



$("#v_paid_through_account_name").typeahead({
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
				module:"invoicepaymentdepositto",
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
					    '<span onclick="v_isbankcharge(\''+item.accountname+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
					  );
				}
			else
				{
				var $state = $(
						 '<span onclick="v_isbankcharge(\''+item.accountname+'\')">'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
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


$("#invAttch").change(function(){
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


$("#v_invAttch").change(function(){
	var files = $(this)[0].files.length;
	var sizeFlag = false;
		if(files > 5){
			$(this)[0].value="";
			alert("You can upload only a maximum of 5 files");
			$("#v_billAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
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
				$("#v_billAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
			}else{
				$("#v_billAttchNote").html(files +" files attached");
			}
			
		}
	});
/* Bank Charges */
$(document).on("focusout","#bankcharges", function(){
	
	
		/*var value = $(this).val();
		var newvalue=parseFloat(value);
		if(value.length > 0 && newvalue>0){
			 $(".bankAccountSection").show();
			 $("#bankAccountSearch").prop('disabled', false); 
		 }else{
			 $(".bankAccountSection").hide();
			 $("#bankAccountSearch").prop('disabled', true);
			 $("input[name ='bank_name']").typeahead('val','');
			 $("input[name ='bank_branch']").val('');
		 }
		*/
		var value = $(this).val();
		
		var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(value.match(decimal) || value.match(numbers)) 
		{ 
			var numberOfDecimal = document.getElementById("numberOfDecimal").value;
			var paidamount = document.form1.amount.value;
			var amount=parseFloat(value).toFixed(numberOfDecimal);
			$(this).val(amount);
			
			if(value.length > 0){
				if(parseFloat(paidamount) > parseFloat(value)){
					 $(".bankAccountSection").show();
					 $("#bankAccountSearch").prop('disabled', false); 
				}else{
					$(this).val('');
					 $(".bankAccountSection").hide();
					 $("#bankAccountSearch").prop('disabled', true);
					 $("input[name ='bank_name']").typeahead('val','');
					 $("input[name ='bank_branch']").val('');
					 alert("Please Enter Bank Charges Less Than Amount");
				}
			 }else{
				 $(".bankAccountSection").hide();
				 $("#bankAccountSearch").prop('disabled', true);
				 $("input[name ='bank_name']").typeahead('val','');
				 $("input[name ='bank_branch']").val('');
			 }
		}else{
			if(value.length > 0){
				$(this).val('');
				 $(".bankAccountSection").hide();
				 $("#bankAccountSearch").prop('disabled', true);
				 $("input[name ='bank_name']").typeahead('val','');
				 $("input[name ='bank_branch']").val('');
				 alert("Please Enter Valid Bank Charges");
			}
		}
		
		if(value.length > 0){
			
		}else{
			$(".bankAccountSection").hide();
			 $("#bankAccountSearch").prop('disabled', true);
			 $("input[name ='bank_name']").typeahead('val','');
			 $("input[name ='bank_branch']").val('');
		}
		
	});



$(document).on("focusout","#v_bankcharges", function(){
	

	var value = $(this).val();
	
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	if(value.match(decimal) || value.match(numbers)) 
	{ 
		var numberOfDecimal = document.getElementById("numberOfDecimal").value;
		var paidamount = document.v_form1.v_amount.value;
		var amount=parseFloat(value).toFixed(numberOfDecimal);
		$(this).val(amount);
		
		if(value.length > 0){
			if(parseFloat(paidamount) > parseFloat(value)){
				 $(".v_bankAccountSection").show();
				 $("#v_bankAccountSearch").prop('disabled', false); 
			}else{
				$(this).val('');
				 $(".v_bankAccountSection").hide();
				 $("#v_bankAccountSearch").prop('disabled', true);
				 $("input[name ='v_bank_name']").typeahead('val','');
				 $("input[name ='v_bank_branch']").val('');
				 alert("Please Enter Bank Charges Less Than Amount");
			}
		 }else{
			 $(".v_bankAccountSection").hide();
			 $("#v_bankAccountSearch").prop('disabled', true);
			 $("input[name ='v_bank_name']").typeahead('val','');
			 $("input[name ='v_bank_branch']").val('');
		 }
	}else{
		if(value.length > 0){
			$(this).val('');
			 $(".v_bankAccountSection").hide();
			 $("#v_bankAccountSearch").prop('disabled', true);
			 $("input[name ='v_bank_name']").typeahead('val','');
			 $("input[name ='v_bank_branch']").val('');
			 alert("Please Enter Valid Bank Charges");
		}
	}
	
	if(value.length > 0){
		
	}else{
		$(".v_bankAccountSection").hide();
		 $("#v_bankAccountSearch").prop('disabled', true);
		 $("input[name ='v_bank_name']").typeahead('val','');
		 $("input[name ='v_bank_branch']").val('');
	}
	
});



/* Bank Auto Suggestion */
$(".bankAccountSearch").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{	  
	  display: 'NAME',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "/track/BankServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "GET_BRANCH_NAME_FOR_AUTO_SUGGESTION",
				QUERY : query
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
			return '<div onclick="document.form1.bank_branch.value=\''+data.BRANCH_CODE+'\';"><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></div>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="bnkAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
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



/* Bank Auto Suggestion */
$(".v_bankAccountSearch").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{	  
	  display: 'NAME',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "/track/BankServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "GET_BRANCH_NAME_FOR_AUTO_SUGGESTION",
				QUERY : query
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
			return '<div onclick="document.v_form1.v_bank_branch.value=\''+data.BRANCH_CODE+'\';"><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></div>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="bnkAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
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
		return '<div><p onclick="getCurrencyid(\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
		}
	  }
}).on('typeahead:render',function(event,selection){
	  
}).on('typeahead:open',function(event,selection){
	
}).on('typeahead:close',function(){

}).on('typeahead:change',function(event,selection){
	if($(this).val() == "")
		{
		$("input[name ='CURRENCYID']").val("");
		$("input[name ='CURRENCYUSEQT']").val("1");
		}
});

/* To get the suggestion data for Currency */
$("#v_CURRENCY").typeahead({
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
		return '<div><p onclick="getvoucher_Currencyid(\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
		}
	  }
}).on('typeahead:render',function(event,selection){
	  
}).on('typeahead:open',function(event,selection){
	
}).on('typeahead:close',function(){

}).on('typeahead:change',function(event,selection){
	if($(this).val() == ""){
		$("input[name ='v_CURRENCYID']").val("");
		$("input[name ='v_CURRENCYUSEQT']").val("1");
	}
});


});



function validatePayment(){
	
	//calculatecurrency();
	
	var bankCharges = document.form1.bankcharges.value;
	var paymentmode = document.form1.payment_mode.value;
	var depositto = document.form1.paid_through_account_name.value;
	
	//RESVI START
	var ValidNumber   = document.form1.ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" Payment's you can create"); return false; }
	// END
	   
	if(paymentmode == ""){
		alert("Please choose payment mode.");
		document.form1.payment_mode.focus();
		return false;
	}
	
	if(depositto == ""){
		alert("Please choose Deposit To.");
		document.form1.paid_through_account_name.focus();
		return false;
	}
	
	var pdastatus=$('input[name = "paidpdcstatus"]').val();
	var pdcamount=$('input[name = "pdcamount"]').val();
	var amount_paid=$('input[name = "amount"]').val();
	

	
	
	if(pdastatus == "1"){
		
		var isItemValid = true;
		var ischeck = false;
		$("input[name ='pdc']").each(function() {
			if($(this).is(":checked")){
				ischeck = true;
		    }
		});
		
		if(ischeck){
			$("input[name ='pdc']").each(function() {
				if(!$(this).is(":checked")){
					isItemValid = false;
			    }
			});
		}else{
			$("input[name ='pdc']").each(function() {
				if($(this).is(":checked")){
					isItemValid = false;
			    }
			});
		}
		
		if(!isItemValid){
			alert("Payment not allow with PDC and CDC. It should be either PDC(Check All) or CDC(UnCheck All)");
			return false;
		}
		
		var isItemValid = true;
		$("input[name ='bankname']").each(function() {
		    if($(this).val() == ""){
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The bank field cannot be empty.");
			return false;
		}	
		
		$("input[name ='cheque-no']").each(function() {
		    if($(this).val() == ""){	
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque number field cannot be empty.");
			return false;
		}
		
		$("input[name ='cheque-date']").each(function() {
		    if($(this).val() == ""){
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque date field cannot be empty.");
			return false;
		}
		
		$("input[name ='cheque-amount']").each(function() {
		    if(parseFloat($(this).val()) == parseFloat("0")){	
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque amount field cannot be zero.");
			return false;
		}
		
		if(parseFloat(pdcamount) != parseFloat(amount_paid)){
			alert("Cheque amount should be equal to Amount Received");
			return false;
		}
	}
	
	$('.datepicker').prop('disabled',false);

	return true;
}

function v_validatePayment(){
	
	//v_calculatecurrency();
	
	var bankCharges = document.v_form1.v_bankcharges.value;
	var paymentmode = document.v_form1.v_payment_mode.value;
	var depositto = document.v_form1.v_paid_through_account_name.value;
	var acountname = document.v_form1.account_name.value;
	
	//RESVI START
	var ValidNumber   = document.v_form1.v_ValidNumber.value;
	   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" Payment's you can create"); return false; }
	// END
	var pdastatus=$('input[name = "v_paidpdcstatus"]').val();
	var pdcamount=$('input[name = "v_pdcamount"]').val();
	var amount_paid=$('input[name = "v_amount"]').val();
	
	if(acountname == ""){
		alert("Please choose account name.");
		document.v_form1.account_name.focus();
		return false;
	}
	
	if(paymentmode == ""){
		alert("Please choose Mode of Receipt.");
		document.v_form1.v_payment_mode.focus();
		return false;
	}
	
	if(depositto == ""){
		alert("Please choose Deposit To.");
		document.v_form1.v_paid_through_account_name.focus();
		return false;
	}
	
	
	
	if(pdastatus == "1"){
		
		var isItemValid = true;
		var ischeck = false;
		$("input[name ='v_pdc']").each(function() {
			if($(this).is(":checked")){
				ischeck = true;
		    }
		});
		
		if(ischeck){
			$("input[name ='v_pdc']").each(function() {
				if(!$(this).is(":checked")){
					isItemValid = false;
			    }
			});
		}else{
			$("input[name ='v_pdc']").each(function() {
				if($(this).is(":checked")){
					isItemValid = false;
			    }
			});
		}
		
		if(!isItemValid){
			alert("Payment not allow with PDC and CDC. It should be either PDC(Check All) or CDC(UnCheck All)");
			return false;
		}
		
		var isItemValid = true;
		$("input[name ='v_bankname']").each(function() {
		    if($(this).val() == ""){
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The bank field cannot be empty.");
			return false;
		}	
		
		$("input[name ='v_cheque-no']").each(function() {
		    if($(this).val() == ""){	
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque number field cannot be empty.");
			return false;
		}
		
		$("input[name ='v_cheque-date']").each(function() {
		    if($(this).val() == ""){
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque date field cannot be empty.");
			return false;
		}
		
		$("input[name ='v_cheque-amount']").each(function() {
		    if(parseFloat($(this).val()) == parseFloat("0")){	
		    	$(this).focus();
		    	isItemValid = false;
		    }
		});
		if(!isItemValid){
			alert("The cheque amount field cannot be zero.");
			return false;
		}
		
		if(parseFloat(pdcamount) != parseFloat(amount_paid)){
			alert("Cheque amount should be equal to amount received");
			return false;
		}
	}
	
	$('.datepicker').prop('disabled',false);
	
	return true;
}


function setCustomerData(customerData){	
	$("input[name ='CUST_CODE']").val(customerData.custno);
	$("#CUSTOMER").typeahead('val', customerData.custname);	
	customerchanged();
	/* To reset Order number Autosuggestion*/
	$("#ORDERNO").typeahead('val', '"');
	$("#ORDERNO").typeahead('val', '');
}

function sortTable() {
	  var table, rows, switching, i, x, y, shouldSwitch;
	  table = document.getElementById("table2");
	  switching = true;
	  /* Make a loop that will continue until
	  no switching has been done: */
	  while (switching) {
	    // Start by saying: no switching is done:
	    switching = false;
	    rows = table.rows;
	    /* Loop through all table rows (except the
	    first, which contains table headers): */
	    for (i = 1; i < (rows.length - 1); i++) {
	      // Start by saying there should be no switching:
	      shouldSwitch = false;
	      /* Get the two elements you want to compare,
	      one from current row and one from the next: */
	      x = rows[i].getElementsByTagName("TD")[0];
	      y = rows[i + 1].getElementsByTagName("TD")[0];
	      // Check if the two rows should switch place:
	      if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
	        // If so, mark as a switch and break the loop:
	        shouldSwitch = true;
	        break;
	      }
	    }
	    if (shouldSwitch) {
	      /* If a switch has been marked, make the switch
	      and mark that a switch has been done: */
	      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
	      switching = true;
	    }
	  }
	}



function paysubmit(){

		var urlStr = "/track/InvoicePayment?CMD=SAVE";
		
		
			
		event.preventDefault();
		if(validatePayment()){
	        var formData = new FormData(document.getElementById("invoicePaymentForm"));
	        formData.append("invoicelist",invoiceList);
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				enctype: 'multipart/form-data',
	            xhr: function() {
	                var myXhr = $.ajaxSettings.xhr();
	                return myXhr;
	            },
	            success: function (data) {
	                window.location.href="../banking/invoicepaysummary";
	            },
	            data: formData,
	            cache: false,
	            contentType: false,
	            processData: false
				   });
		}

}

function vouchersubmit(){
	var urlStr = "/track/InvoicePayment?CMD=V_SAVE";
	

	
	   
	event.preventDefault();
	if(v_validatePayment()){
        var formData = new FormData(document.getElementById("voucherPaymentForm"));
        formData.append("invoicelist",invoiceList);
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			enctype: 'multipart/form-data',
            xhr: function() {
                var myXhr = $.ajaxSettings.xhr();
                return myXhr;
            },
            success: function (data) {
                window.location.href="../banking/invoicepaysummary";
            },
            data: formData,
            cache: false,
            contentType: false,
            processData: false
			   });
	}
	

}

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}

function addRow(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var newdate = $("input[name ='payment_date']").val();
	var bankname = $("input[name ='vbank']").val();
	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="pdcstatus" value="0">';
	body += '<input type="checkbox" class="form-check-input" id="pdc" name="pdc" Onclick="checkpdc(this)">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input name="bank_branch" type="hidden" value="">';
	if(bankname.length > 0){
		body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" value="'+bankname+'">';
	}else{
		body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" placeholder="Select a bank">';
	}
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" type="text" name="cheque-no" placeholder="Enter Cheque No" >';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date" value="'+newdate+'" disabled>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle payment-action" aria-hidden="true"></span>';
	body += '<input class="form-control text-right" type="text" name="cheque-amount" onchange="calculateAmount(this)" value="'+zeroval+'">';
	body += '</td>';
	body += '</tr>';
	$(".payment-table tbody").append(body);
	removerowclasses();
	addrowclasses();
}

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
						document.getElementById("payment_mode").focus();
						$("#payment_mode").typeahead('val', '');
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
//jsp validation ends

//customer popup changes
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
//customer popup changes end

function v_addRow(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var newdate = $("input[name ='payment_date']").val();
	var bankname = $("input[name ='v_vbank']").val();
	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="v_pdcstatus" value="0">';
	body += '<input type="checkbox" class="form-check-input" id="v_pdc" name="v_pdc" Onclick="v_checkpdc(this)">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input name="v_bank_branch" type="hidden" value="">';
	if(bankname.length > 0){
		body += '<input class="form-control text-left v_bankAccountSearch" type="text" name="v_bankname" value="'+bankname+'">';
	}else{
		body += '<input class="form-control text-left v_bankAccountSearch" type="text" name="v_bankname" placeholder="Select a bank">';
	}
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" type="text" name="v_cheque-no" placeholder="Enter Cheque No">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left datepicker" type="text" name="v_cheque-date" placeholder="Enter Cheque Date" value="'+newdate+'" disabled>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle voucher-action" aria-hidden="true"></span>';
	body += '<input class="form-control text-right" type="text" name="v_cheque-amount" onchange="v_calculateAmount(this)" value="'+zeroval+'">';
	body += '</td>';
	body += '</tr>';
	$(".voucher-table tbody").append(body);
	v_removerowclasses();
	v_addrowclasses();
}

function addnewRow(){
	$(".payment-table tbody").html("");
	var numberOfDecimal = $("#numberOfDecimal").val();
	var newdate = $("input[name ='payment_date']").val();
	var bankname = $("input[name ='vbank']").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="pdcstatus" value="0">';
	body += '<input type="checkbox" class="form-check-input pdc" name="pdc" Onclick="checkpdc(this)">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input name="bank_branch" type="hidden" value="">';
	if(bankname.length > 0){
		body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" value="'+bankname+'">';
	}else{
		body += '<input class="form-control text-left bankAccountSearch" type="text" name="bankname" placeholder="Select a bank">';
	}
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" type="text" name="cheque-no" placeholder="Enter Cheque No">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left datepicker" type="text" name="cheque-date" placeholder="Enter Cheque Date" value="'+newdate+'" disabled>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<input class="form-control text-right" type="text" name="cheque-amount" onchange="calculateAmount(this)" value="'+zeroval+'">';
	body += '</td>';
	body += '</tr>';
	$(".payment-table tbody").append(body);
	$("#subTotal").html(parseFloat("0.0").toFixed(numberOfDecimal));
	$("#balamount").html(parseFloat("0.0").toFixed(numberOfDecimal));
	 $("input[name ='pdcamount']").val("0");
	removerowclasses();
	addrowclasses();
}

function v_addnewRow(){
	$(".voucher-table tbody").html("");
	var numberOfDecimal = $("#numberOfDecimal").val();
	var newdate = $("input[name ='payment_date']").val();
	var bankname = $("input[name ='v_vbank']").val();
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);
	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="v_pdcstatus" value="0">';
	body += '<input type="checkbox" class="form-check-input v_pdc" name="v_pdc" Onclick="v_checkpdc(this)">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input name="v_bank_branch" type="hidden" value="">';
	if(bankname.length > 0){
		body += '<input class="form-control text-left v_bankAccountSearch" type="text" name="v_bankname" value="'+bankname+'">';
	}else{
		body += '<input class="form-control text-left v_bankAccountSearch" type="text" name="v_bankname" placeholder="Select a bank">';
	}
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left" type="text" name="v_cheque-no" placeholder="Enter Cheque No">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left datepicker" type="text" name="v_cheque-date" placeholder="Enter Cheque Date" value="'+newdate+'" disabled>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<input class="form-control text-right" type="text" name="v_cheque-amount" onchange="v_calculateAmount(this)" value="'+zeroval+'">';
	body += '</td>';
	body += '</tr>';
	$(".voucher-table tbody").append(body);
	$("#v_subTotal").html(parseFloat("0.0").toFixed(numberOfDecimal));
	$("#v_balamount").html(parseFloat("0.0").toFixed(numberOfDecimal));
	 $("input[name ='v_pdcamount']").val("0");
	 v_removerowclasses();
	 v_addrowclasses();
}

$(document).on("focus", ".bankAccountSearch" , function() {
	$(this).typeahead('val', '"');
	$(this).typeahead('val', '');	
});

$(document).on("focus", ".v_bankAccountSearch" , function() {
	$(this).typeahead('val', '"');
	$(this).typeahead('val', '');	
});

function removerowclasses(){
	$(".bankAccountSearch").typeahead('destroy');
}

function v_removerowclasses(){
	$(".v_bankAccountSearch").typeahead('destroy');
}

function addrowclasses(){
	var plant = document.form.plant.value;
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
	
		$(".bankAccountSearch").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'NAME',   
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/BankServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						action : "GET_BRANCH_NAME_FOR_AUTO_SUGGESTION",
						QUERY : query
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
					return '<span onclick="addbankcode(this,\''+data.BRANCH_CODE+'\')" ><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></span>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="bnkAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
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

function v_addrowclasses(){
	var plant = document.form.plant.value;
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	  });
	
		$(".v_bankAccountSearch").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'NAME',   
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/BankServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						action : "GET_BRANCH_NAME_FOR_AUTO_SUGGESTION",
						QUERY : query
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
					return '<span onclick="v_addbankcode(this,\''+data.BRANCH_CODE+'\')" ><p class="item-suggestion">' + data.NAME + '</p><br/><p class="item-suggestion">'+data.BRANCH_NAME+'</p><p class="item-suggestion pull-right">'+data.BRANCH_CODE+'</p></span>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="bnkAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
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


function checkpdc(obj){
	var newdate = $("input[name ='payment_date']").val();
	 if ($(obj).is(":checked")){
		 $(obj).closest('td').find("input[name ='pdcstatus']").val("1");
		 $(obj).closest('tr').find("input[name ='cheque-date']").prop('disabled',false);
	 }else{
		 $(obj).closest('td').find("input[name ='pdcstatus']").val("0");
		 $(obj).closest('tr').find('input[name = "cheque-date"]').val(newdate);
		 $(obj).closest('tr').find("input[name ='cheque-date']").prop('disabled',true);
	 }
}

function v_checkpdc(obj){
	var newdate = $("input[name ='payment_date']").val();
	 if ($(obj).is(":checked")){
		 $(obj).closest('td').find("input[name ='v_pdcstatus']").val("1");
		 $(obj).closest('tr').find("input[name ='v_cheque-date']").prop('disabled',false);
	 }else{
		 $(obj).closest('td').find("input[name ='v_pdcstatus']").val("0");
		 $(obj).closest('tr').find('input[name = "v_cheque-date"]').val(newdate);
		 $(obj).closest('tr').find("input[name ='v_cheque-date']").prop('disabled',true);
	 }
}

function addbankcode(obj,branchcode){
	$(obj).closest('td').find('input[name = "bank_branch"]').val(branchcode);
}

function v_addbankcode(obj,branchcode){
	$(obj).closest('td').find('input[name = "v_bank_branch"]').val(branchcode);
}

function calculateAmount(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var subtotal = "0";
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	var amountpaid =  $("input[name ='amount']").val();
	if(!(parseFloat(amountpaid)>0)){
		$(".payment-table tbody tr td:last-child").each(function() {
			$(this).find('input').val(parseFloat("0.0").toFixed(numberOfDecimal));
	});
	}else{
		$(".payment-table tbody tr td:last-child").each(function() {
			var amount = $(this).find('input').val();
			if(amount.match(decimal) || amount.match(numbers)) 
			{ 
				subtotal =  parseFloat(subtotal) + parseFloat(amount);
				$(this).find('input').val(parseFloat(amount).toFixed(numberOfDecimal));
			}else{
				$(this).find('input').val(parseFloat("0.0").toFixed(numberOfDecimal));
				alert("Please Enter Valid Cheque Amount")
			}
		});
	}
	
	if(parseFloat(subtotal) <= parseFloat(amountpaid)){
		 $("#subTotal").html(parseFloat(subtotal).toFixed(numberOfDecimal));
		 $("#balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(subtotal)).toFixed(numberOfDecimal));
		 $("input[name ='pdcamount']").val(subtotal);
	}else{
		alert("Cheque amount exceeds Amount Received")
		$(obj).val(parseFloat("0.0").toFixed(numberOfDecimal));
		var esubtotal = "0";
		$(".payment-table tbody tr td:last-child").each(function() {
			var amount = $(this).find('input').val();
			esubtotal =  parseFloat(esubtotal) + parseFloat(amount);
		});
		$("#subTotal").html(parseFloat(esubtotal).toFixed(numberOfDecimal));
		$("#balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(esubtotal)).toFixed(numberOfDecimal));
		$("input[name ='pdcamount']").val(esubtotal);
	}
	
}

function v_calculateAmount(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var subtotal = "0";
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	var amountpaid =  $("input[name ='v_amount']").val();
	if(!(parseFloat(amountpaid)>0)){
		$(".voucher-table tbody tr td:last-child").each(function() {
			$(this).find('input').val(parseFloat("0.0").toFixed(numberOfDecimal));
	});
	}else{
		$(".voucher-table tbody tr td:last-child").each(function() {
			var amount = $(this).find('input').val();
			if(amount.match(decimal) || amount.match(numbers)) 
			{ 
				subtotal =  parseFloat(subtotal) + parseFloat(amount);
				$(this).find('input').val(parseFloat(amount).toFixed(numberOfDecimal));
			}else{
				$(this).find('input').val(parseFloat("0.0").toFixed(numberOfDecimal));
				alert("Please Enter Valid Cheque Amount")
			}
		});
	}
	
	if(parseFloat(subtotal) <= parseFloat(amountpaid)){
		 $("#v_subTotal").html(parseFloat(subtotal).toFixed(numberOfDecimal));
		 $("#v_balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(subtotal)).toFixed(numberOfDecimal));
		 $("input[name ='v_pdcamount']").val(subtotal);
	}else{
		alert("Cheque amount exceeds amount received")
		$(obj).val(parseFloat("0.0").toFixed(numberOfDecimal));
		var esubtotal = "0";
		$(".voucher-table tbody tr td:last-child").each(function() {
			var amount = $(this).find('input').val();
			esubtotal =  parseFloat(esubtotal) + parseFloat(amount);
		});
		$("#v_subTotal").html(parseFloat(esubtotal).toFixed(numberOfDecimal));
		$("#v_balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(esubtotal)).toFixed(numberOfDecimal));
		$("input[name ='v_pdcamount']").val(esubtotal);
	}
	
}

function calculatetotal(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amountpaid =  $("input[name ='amount']").val();
	var subtotal = "0";
	$(".payment-table tbody tr td:last-child").each(function() {
		var amount = $(this).find('input').val();
		subtotal =  parseFloat(subtotal) + parseFloat(amount);
		$(this).find('input').val(parseFloat(amount).toFixed(numberOfDecimal));
	});
	$("#subTotal").html(parseFloat(subtotal).toFixed(numberOfDecimal));
	$("#balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(subtotal)).toFixed(numberOfDecimal));
	$("input[name ='pdcamount']").val(subtotal);
}

function v_calculatetotal(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amountpaid =  $("input[name ='v_amount']").val();
	var subtotal = "0";
	$(".voucher-table tbody tr td:last-child").each(function() {
		var amount = $(this).find('input').val();
		subtotal =  parseFloat(subtotal) + parseFloat(amount);
		$(this).find('input').val(parseFloat(amount).toFixed(numberOfDecimal));
	});
	$("#v_subTotal").html(parseFloat(subtotal).toFixed(numberOfDecimal));
	$("#v_balamount").html(parseFloat(parseFloat(amountpaid)-parseFloat(subtotal)).toFixed(numberOfDecimal));
	$("input[name ='v_pdcamount']").val(subtotal);
}

/*function isbankcharge(obj,acountname){
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/ChartOfAccountServlet",
		async : true,
		data : {
			PLANT : plant,
			action : "isbankcharge",
			accountname : acountname
		},
		dataType : "json",
		success : function(data) {
			if(data.status == "OK"){
				addnewRow();
				$("input[name ='paidpdcstatus']").val("1");
				$("#bankcharges").attr("readonly", false);
				$(".hidepdc").show();
			}else{
				$("input[name ='paidpdcstatus']").val("0");
				$("input[name ='bankcharges']").val("");
				$("#bankcharges").attr("readonly", true);
				$(".hidepdc").hide();
			}
		}
	});
}

function v_isbankcharge(acountname){
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/ChartOfAccountServlet",
		async : true,
		data : {
			PLANT : plant,
			action : "isbankcharge",
			accountname : acountname
		},
		dataType : "json",
		success : function(data) {
			if(data.status == "OK"){
				v_addnewRow();
				$("input[name ='v_paidpdcstatus']").val("1");
				$("#v_bankcharges").attr("readonly", false);
				$(".v_hidepdc").show();
			}else{
				$("input[name ='v_paidpdcstatus']").val("0");
				$("input[name ='v_bankcharges']").val("");
				$("#v_bankcharges").attr("readonly", true);
				$(".v_hidepdc").hide();
			}
		}
	});
}*/

function isbankcharge(obj,acountname){
	var plant = document.form.plant.value;
	var paymode =  $("input[name ='payment_mode']").val();
	$.ajax({
		type : "POST",
		url : "/track/ChartOfAccountServlet",
		async : true,
		data : {
			PLANT : plant,
			action : "isbankcharge",
			accountname : acountname
		},
		dataType : "json",
		success : function(data) {
			if(data.status == "OK"){
				$("input[name ='isbank']").val("1");
				$("input[name ='vbank']").val(acountname);
				var pdcstatus = $("input[name ='paidpdcstatus']").val();
				if(pdcstatus == "1"){
					$("input[name ='bankname']").each(function() {
					    $(this).val(acountname);
					});
				}else{
					if(paymode == "Cheque"){
						addnewRow();
						$("input[name ='paidpdcstatus']").val("1");
						$("#bankCharge").attr("readonly", false);
						$(".hidepdc").show();
					}
				}
			}else{
				$("input[name ='isbank']").val("0");
				$("input[name ='paidpdcstatus']").val("0");
				$("input[name ='bank_charge']").val("");
				$("#bankCharge").attr("readonly", true);
				$(".hidepdc").hide();
				if(paymode == "Cheque"){
					$("#paid_through_account_name").typeahead('val', '');
					alert("Please select Bank Balance account type");
				}
			}
		}
	});
}

function v_isbankcharge(acountname){
	var plant = document.form.plant.value;
	var paymode =  $("input[name ='v_payment_mode']").val();
	$.ajax({
		type : "POST",
		url : "/track/ChartOfAccountServlet",
		async : true,
		data : {
			PLANT : plant,
			action : "isbankcharge",
			accountname : acountname
		},
		dataType : "json",
		success : function(data) {
			if(data.status == "OK"){
				$("input[name ='v_isbank']").val("1");
				$("input[name ='v_vbank']").val(acountname);
				var pdcstatus = $("input[name ='v_paidpdcstatus']").val();
				if(pdcstatus == "1"){
					$("input[name ='v_bankname']").each(function() {
					    $(this).val(acountname);
					});
				}else{
					if(paymode == "Cheque"){
						v_addnewRow();
						$("input[name ='v_paidpdcstatus']").val("1");
						$("#voucher_bankCharge").attr("readonly", false);
						$(".v_hidepdc").show();
					}
				}
			}else{
				$("input[name ='v_isbank']").val("0");
				$("input[name ='v_paidpdcstatus']").val("0");
				$("input[name ='v_bank_charge']").val("");
				$("#voucher_bankCharge").attr("readonly", true);
				$(".v_hidepdc").hide();
				if(paymode == "Cheque"){
					$("#v_paid_through_account_name").typeahead('val', '');
					alert("Please select Bank Balance account type");
				}
			}
		}
	});
}

function custnoandbank(cno,vbank){
	
	$("input[name ='CUST_CODE']").val(cno);
	$("input[name ='vbank']").val(vbank);
	$("input[name ='bankname']").val(vbank);
	
}
function paymentTypeCallback(data)
{
	var inpay = $("input[name ='inpayment']").val();
	if(inpay=="0")
	//$("input[name ='payment_mode']").val(data.PAYMENTTYPE);
		$("input[name ='payment_mode']").val(data.PAYMENTMODE);
	else if(inpay=="1")
	//$("input[name ='v_payment_mode']").val(data.PAYMENTTYPE);
		$("input[name ='v_payment_mode']").val(data.PAYMENTMODE);
}
function getCurrencyid(CURRENCYID,CURRENCYUSEQT){
	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	
	var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
	
	document.getElementById('PaymentMade').innerHTML = "Amount Received ("+CURRENCYID+")";
	document.getElementById('exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var amountnode = document.getElementById("amount").value;
	
	var baseamount=parseFloat(amountnode) * parseFloat(CURRENCYUSEQT);
	$("input[name ='conv_amount_Curr']").val(baseamount);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	$("input[name ='amount_Curr']").val(baseamount);
    
}

function getvoucher_Currencyid(CURRENCYID,CURRENCYUSEQT){
	
	$('input[name ="v_CURRENCYID"]').val(CURRENCYID);
	$('input[name ="v_CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	
	var basecurrency = document.form1.BASECURRENCYID.value; //Resvi
	
	document.getElementById('v_PaymentMade').innerHTML = "Amount Received ("+CURRENCYID+")";
	document.getElementById('v_exchangerate').innerHTML = "Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";//Resvi
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var amountnode = document.getElementById("v_amount").value;
	
	var baseamount=parseFloat(amountnode) * parseFloat(CURRENCYUSEQT);
	$("input[name ='v_conv_amount_Curr']").val(baseamount);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	$("input[name ='v_amount_Curr']").val(baseamount);
    
}
/*function calculatecurrency(){
	
var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
var baseCurrency = document.getElementById("CURRENCYUSEQT").value;
var amountnode = document.getElementById("amount_paid").value;

var baseamount=parseFloat(amountnode) * parseFloat(baseCurrency);
$("input[name ='conv_amount_Curr']").val(baseamount);
baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
$("input[name ='amoun_Curr']").val(baseamount);

}
function v_calculatecurrency(){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = document.getElementById("v_CURRENCYUSEQT").value;
	var amountnode = document.getElementById("v_amount").value;

	var baseamount=parseFloat(amountnode) * parseFloat(baseCurrency);
	$("input[name ='v_conv_amount_Curr']").val(baseamount);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	$("input[name ='v_amount_Curr']").val(baseamount);
	

}*/
function currencychanged(data)
{
	//calculatecurrency();
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = data.value;
	
	var amountnode = $("input[name ='amount_Curr']").val();
	$("input[name ='conv_amount_Curr']").val(amountnode);
	
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='amount']").val(baseamount);
	calculateTotalbasecurrency(baseamount);
}
function voucher_currencychanged(data)
{
	//v_calculatecurrency();
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = data.value;
	var amountnode = $("input[name ='v_amount_Curr']").val();
	$("input[name ='v_conv_amount_Curr']").val(amountnode);
	
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='v_amount_paid']").val(baseamount);
	v_calculateTotalbasecurrency(baseamount);
}

function calculatebasecurrency(data){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = document.getElementById("CURRENCYUSEQT").value;
	var amountnode = data.value;
	$("input[name ='conv_amount_Curr']").val(amountnode);
	
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='amount']").val(baseamount);
	calculateTotalbasecurrency(baseamount);

}

function v_calculatebasecurrency(data){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = document.getElementById("v_CURRENCYUSEQT").value;
	var amountnode = data.value;
	$("input[name ='v_conv_amount_Curr']").val(amountnode);
	
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='v_amount']").val(baseamount);
	v_calculateTotalbasecurrency(baseamount);

}


function calculateTotalbasecurrency(amountnode){

var numberOfDecimal = document.getElementById("numberOfDecimal").value;
var zeroval=parseFloat("0").toFixed(numberOfDecimal);
var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
var numbers = /^[0-9]+$/;
if(amountnode.match(decimal) || amountnode.match(numbers)) 
{ 
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var amount=parseFloat(amountnode).toFixed(numberOfDecimal);
	document.getElementById('amountreceived').innerHTML=amount;
	$("input[name ='amount']").val(amount);
	amountreceived=amount;
	$("input[name ='cheque-amount']").each(function() {	
	    $(this).val(zeroval);
	});
	calculatetotal();
	
}else{
	$("input[name ='amount']").val('1');
	document.getElementById('amountreceived').innerHTML='';
	alert("Please Enter Valid Amount Received");
}

callTotalDetail(numberOfDecimal);
}

function v_calculateTotalbasecurrency(baseamount){

	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
	var numbers = /^[0-9]+$/;
	var zeroval=parseFloat("0").toFixed(numberOfDecimal);
	if(baseamount.match(decimal) || baseamount.match(numbers)) 
	{ 
		var amount=parseFloat(baseamount).toFixed(numberOfDecimal);
		$("input[name ='v_amount']").val(amount);
		$("input[name ='v_cheque-amount']").each(function() {	
		    $(this).val(zeroval);
		});
		v_calculatetotal();
		
		
	}else{
		$("input[name ='v_amount']").val(zeroval);
		alert("Please Enter Valid Amount Received");
	}

}


function calculatebasecurrency_new(data){
	
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;	
	var baseCurrency = document.getElementById("CURRENCYUSEQT").value;
	
	$("input[name ='amount_Curr']").val(parseFloat(data).toFixed(numberOfDecimal));
	var amountnode = data;
	$("input[name ='conv_amount_Curr']").val(amountnode);
	var baseamount=parseFloat(amountnode) / parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	$("input[name ='amount']").val(baseamount);
	calculateTotalbasecurrency_new(baseamount);
	
	/*$("input[name ='amount']").val(parseFloat(data).toFixed(numberOfDecimal));
	var amountnode = data;
	var baseamount=parseFloat(amountnode) * parseFloat(baseCurrency);
	baseamount=parseFloat(baseamount).toFixed(numberOfDecimal);
	
	$("input[name ='conv_amount_Curr']").val(baseamount);
	$("input[name ='amount_Curr']").val(baseamount);
	
	calculateTotalbasecurrency_new(parseFloat(data).toFixed(numberOfDecimal));*/
	

}


function calculateTotalbasecurrency_new(amountnode){

var numberOfDecimal = document.getElementById("numberOfDecimal").value;
var zeroval=parseFloat("0").toFixed(numberOfDecimal);
var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
var numbers = /^[0-9]+$/;
if(amountnode.match(decimal) || amountnode.match(numbers)) 
{ 
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	var amount=parseFloat(amountnode).toFixed(numberOfDecimal);
	document.getElementById('amountreceived').innerHTML=amount;
	$("input[name ='amount']").val(amount);
	amountreceived=amount;
	$("input[name ='cheque-amount']").each(function() {	
	    $(this).val(zeroval);
	});
	calculatetotal();
	
}else{
	$("input[name ='amount']").val('1');
	document.getElementById('amountreceived').innerHTML='';
	alert("Please Enter Valid Amount");
}

	var totalamount=0;
	totalamount=amountnode;
	var totalInvoiceAmount=parseFloat(totalamount).toFixed(numberOfDecimal);
	document.getElementById("totalInvoiceAmount").innerHTML=totalInvoiceAmount;
	document.getElementById("amountufp").innerHTML=totalInvoiceAmount;
	var amountExcess=document.getElementById("amount").value-totalInvoiceAmount;
	amountExcess=parseFloat(amountExcess).toFixed(numberOfDecimal);
	document.getElementById("amountexcess").innerHTML=amountExcess;
}