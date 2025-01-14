var subWin = null;
function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
var table;
function onGo(){
	   var flag    = "false";
	    var FROM_DATE      = document.form1.FROM_DATE.value;
	    var TO_DATE        = document.form1.TO_DATE.value;		    
	    var CUST_NO         = document.form1.CUST_CODE.value;
	    var Decimal         = document.form1.NOOFDECIMAL.value;
	    var REFERENCE         = document.form1.REFERENCE.value;
	    var curency         = document.form1.curency.value;
	  
	    if(REFERENCE != null     && REFERENCE != "") { flag = true;}
	    if(TO_DATE!=null & TO_DATE!="")
	    	{
	    		if(FROM_DATE==null || FROM_DATE=="")
	    			{
		    			alert("Please select from date.");
		    			return false;
	    			}
	    	}
	    
	    var urlStr = "/track/InvoicePayment?CMD=getInvoicePaymentsByFilter&FROM_DATE="+FROM_DATE+"&TO_DATE="+TO_DATE+"&CUST_NO="+CUST_NO+"&REFERENCE="+REFERENCE;
	    table.destroy();
	    table=$('#tableInvoicePaymentSummary').DataTable( {
		lengthMenu: [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
		    ajax: {
		        url: urlStr,
		        dataSrc: 'data'
		    },
		    "ordering": false,
		    columns: [ 
		    	 { data: 'RECEIVE_DATE' },
		    	 { data: 'ID' },
		         { data: 'TRANSACTIONID' },
		         { data: 'REFERENCE' },
		         { data: 'CUSTNO' },
		         { data: 'ACCOUNT_NAME' },
		         { data: 'RECEIVE_MODE' },
		         { data: 'ISPDCPROCESS' },
		         { data: 'CONV_AMOUNTRECEIVED' },
	    		 { data: 'CURRENCYUSEQT' },
		         { data: 'AMOUNTRECEIVED' },
		         { data: 'AMOUNTUFP' },
		    ],
		    "columnDefs": [ {
		        "targets": 1,
		        "data": "ID",
		        "render": function ( data, type, row, meta ) {
		          return '<a href="../banking/invoicepaydetail?CUSTNO='+row.CUSTNO+'&ID='+row.ID+'">'+data+'</a>';
		        }
		      },{
			        "targets": 4,
			        "data": "CUSTNO",
			        "render": function ( data, type, row, meta ) {
			         if(data == "N/A"){
			        	 data="";
			         }
			         return data;
			        }
			      },{
	                "targets": [ 2 ],
	                "visible": false,
	                "searchable": false
			      },{
		            	"className": "t-right",
		            	"targets": 9,
				        "data": "CURRENCYUSEQT",
				        "render": function ( data, type, row, meta ) {
				        	/*var amount1=parseFloat(row.AMOUNTRECEIVED);*/
				        	var amount2=parseFloat(data);
				        	/*var unusedamount=amount1-amount2;*/
				        	unusedamount=amount2.toFixed(Decimal);
				          return getNumberInMillionFormat(unusedamount);
				        }
		            },{
		            	"className": "t-right",
		            	"targets": 10,
				        "data": "AMOUNTRECEIVED",
				        "render": function ( data, type, row, meta ) {
				        	var amount1=parseFloat(data);
				        	var amount=amount1.toFixed(Decimal);
				          return getNumberInMillionFormat(amount);
				        }
		            },{
		            	"className": "t-right",
		            	"targets": 11,
				        "data": "AMOUNTUFP",
				        "render": function ( data, type, row, meta ) {
				        	var amount1=parseFloat(data);
				        	var amount=amount1.toFixed(Decimal);
				          return getNumberInMillionFormat(amount);
				        }
		            },{
		            	"className": "t-right",
		            	"targets": 8,
				        "data": "CONV_AMOUNTRECEIVED",
				        "render": function ( data, type, row, meta ) {
				        	//var amount1=parseFloat(data);
				        	//var amount=amount1.toFixed(Decimal);
				          return row['CURRENCYID'] + getNumberInMillionFormat(data.replace(row['CURRENCYID'], ''));
				        }
			        
	            },{
	            	"className": "t-right",
	            	"targets": 7,
			        "data": "ISPDCPROCESS",
			        "render": function ( data, type, row, meta ) {
			        	var pdc='NON PDC';
			        	if(data == '1'){
			        		pdc = 'PDC';
			        	}
			        	
			          return pdc;
			        },
			        
	            }    
	            
	            
	            ]
		} );
	    table.ajax.reload();
	    // Apply the search
	    table.columns().every( function () {
	        var that = this;
	 
	        $( 'input', this.footer() ).on( 'keyup change clear', function () {
	            if ( that.search() !== this.value ) {
	                that
	                    .search( this.value )
	                    .draw();
	            }
	        } );
	    } );
}

$(document).ready(function(){
 

	var plant = document.form1.plant.value;
	 var Decimal         = document.form1.NOOFDECIMAL.value;
	 var FROM_DATE      = document.form1.FROM_DATE.value;
	    var TO_DATE        = document.form1.TO_DATE.value;
	    var curency         = document.form1.curency.value;
	$('#createNew').click(function() {
		 window.location.href="../banking/createinvoicepay";
			
			
		});
	//var urlStr = "/track/InvoicePayment?CMD=getAllInvoicePayments";
	var urlStr = "/track/InvoicePayment?CMD=getInvoicePaymentsByFilter&FROM_DATE="+FROM_DATE+"&TO_DATE="+TO_DATE;
	var groupColumn = 1;
	initializepaymentsummary();
	function initializepaymentsummary()
	{
		table=$('#tableInvoicePaymentSummary').DataTable( {
			lengthMenu: [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
		    ajax: {
		        url: urlStr,
		        dataSrc: 'data'
		    },
		    "ordering": false,
		    columns: [ 
		    	 { data: 'RECEIVE_DATE' },
		    	 { data: 'ID' },
		         { data: 'TRANSACTIONID' },
		         { data: 'REFERENCE' },
		         { data: 'CUSTNO' },
		         { data: 'ACCOUNT_NAME' },
		         { data: 'RECEIVE_MODE' },
		         { data: 'ISPDCPROCESS' },
		         { data: 'CONV_AMOUNTRECEIVED' },
	    		 { data: 'CURRENCYUSEQT' },
		         { data: 'AMOUNTRECEIVED' },
		         { data: 'AMOUNTUFP' },
		    ],
		    "columnDefs": [ {
		        "targets": 1,
		        "data": "ID",
		        "render": function ( data, type, row, meta ) {
		          return '<a href="../banking/invoicepaydetail?CUSTNO='+row.CUSTNO+'&ID='+row.ID+'">'+data+'</a>';
		        }
		      },{
			        "targets": 4,
			        "data": "CUSTNO",
			        "render": function ( data, type, row, meta ) {
			         if(data == "N/A"){
			        	 data="";
			         }
			         return data;
			        }
			      },{
	                "targets": [ 2 ],
	                "visible": false,
	                "searchable": false
	            },{
	            	"className": "t-right",
	            	"targets": 9,
			        "data": "CURRENCYUSEQT",
			        "render": function ( data, type, row, meta ) {
			        	/*var amount1=parseFloat(row.AMOUNTRECEIVED);*/
			        	var amount2=parseFloat(data);
			        	/*var unusedamount=amount1-amount2;*/
			        	unusedamount=amount2.toFixed(Decimal);
			          return getNumberInMillionFormat(unusedamount);
			        }
	            },{
	            	"className": "t-right",
	            	"targets": 10,
			        "data": "AMOUNTRECEIVED",
			        "render": function ( data, type, row, meta ) {
			        	var amount1=parseFloat(data);
			        	var amount=amount1.toFixed(Decimal);
			          return getNumberInMillionFormat(amount);
			        }
	            },{
	            	"className": "t-right",
	            	"targets": 11,
			        "data": "AMOUNTUFP",
			        "render": function ( data, type, row, meta ) {
			        	var amount1=parseFloat(data);
			        	var amount=amount1.toFixed(Decimal);
			          return getNumberInMillionFormat(amount);
			        }
	            },{
	            	"className": "t-right",
	            	"targets": 8,
			        "data": "CONV_AMOUNTRECEIVED",
			        "render": function ( data, type, row, meta ) {
			        	//var amount1=parseFloat(data);
			        	//var amount=amount1.toFixed(Decimal);
			          return row['CURRENCYID'] + getNumberInMillionFormat(data.replace(row['CURRENCYID'], ''));
			        }
			        
	            },{
	            	"className": "t-right",
	            	"targets": 7,
			        "data": "ISPDCPROCESS",
			        "render": function ( data, type, row, meta ) {
			        	var pdc='NON PDC';
			        	if(data == '1'){
			        		pdc = 'PDC';
			        	}
			        	
			          return pdc;
			        },
			        
	            }   ]
		} );
	 
	    // Apply the search
	//    table.columns().every( function () {
	//        var that = this;
	 
	 //       $( 'input', this.footer() ).on( 'keyup change clear', function () {
	//            if ( that.search() !== this.value ) {
	//                that
	   //                 .search( this.value )
	  //                  .draw();
	     //       }
	   //     } );
	  //  } );
	}
	
	
	$('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	    $('#search_criteria_status').val('show');
	});

 $('.Hide').click(function() {
	    $('#target').hide(500);
	    $('.ShowSingle').show(0);
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
 if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
 	$('.Show').click();
 }else{
 	$('.Hide').click();
 }
 
 
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
// 	    	return '<p onclick="document.form1.CUST_CODE.value = \''+data.CUSTNO+'\'">' + data.CNAME + '</p>';
 	    	return '<div onclick="document.form1.CUST_CODE.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
 		}
 	  }
 	}).on('typeahead:render',function(event,selection){
 		var menuElement = $(this).parent().find(".tt-menu");
 		var top = $(".tt-menu").height()+35;
 		top+="px";
 		$('.customerAddBtn').remove();  
 		$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');
 		$(".customerAddBtn").width($(".tt-menu").width());
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
 		}
 		/* To reset Order number Autosuggestion*/
 		$("#ORDERNO").typeahead('val', '"');
 		$("#ORDERNO").typeahead('val', '');
 		document.form1.CUST_CODE.value = $(this).val();
 		
 	});
});