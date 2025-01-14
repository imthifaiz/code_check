$(document).ready(function(){
 onGo();
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
		    	return '<p onclick="getcustname(\''+data.TAXTREATMENT+'\',\''+data.CUSTNO+'\')">' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.customerAddBtn').remove();  
			$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');
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
			$('#nTAXTREATMENT').attr('disabled',false);
		});
	
	/* Order Number Auto Suggestion */
	$('#ORDERNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ESTNO',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "../salesestimate/GET_ORDER_NO_FOR_AUTO_SUGGESTION";
					$.ajax( {
					type : "GET",
					url : urlStr,
					async : true,
					data : {
						
						CNAME : document.form1.CUSTOMER.value,
						ESTNO : query
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
		    return '<p>' + data.ESTNO + '</p>';
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
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.ORDERNO.value = "";
			}
			/* To reset Order number Autosuggestion*/
			$("#invoice").typeahead('val', '"');
			$("#invoice").typeahead('val', '');
			$("#auto_invoiceNo").typeahead('val', '"');
			$("#auto_invoiceNo").typeahead('val', '');
			$("#plno").typeahead('val', '"');
			$("#plno").typeahead('val', '');
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
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	
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
	
 });

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
		    "year": "PROCESSED",
		    "value": "PROCESSED",
		    "tokens": [
		      "PROCESSED"
		    ]
		  },
		  {
			    "year": "PARTIALLY PROCESSED",
			    "value": "PARTIALLY PROCESSED",
			    "tokens": [
			      "PARTIALLY PROCESSED"
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

function getcustname(TAXTREATMENT,CUSTNO){
	$('select[name ="nTAXTREATMENT"]').val(TAXTREATMENT);
	document.form1.CUST_CODE.value = CUSTNO;
}

function popUpWin(URL) {
	subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
var tableSalesEstimateSummary;
var FROM_DATE,TO_DATE,USER,ORDERNO,REFERENCE,STATUS,ORDERTYPE, groupRowColSpan = 6;

function getParameters(){
	return {
		"FDATE":FROM_DATE,"TDATE":TO_DATE,
		"CNAME":USER,"ORDERNO":ORDERNO,"REFERENCE":REFERENCE,"STATUS":STATUS,"ORDERTYPE":ORDERTYPE
	}
}  

function onGo(){
   var flag = "false";
    FROM_DATE = document.form1.FROM_DATE.value;
    TO_DATE = document.form1.TO_DATE.value;		    
    USER = document.form1.CUSTOMER.value;
    ORDERNO = document.form1.ORDERNO.value;
    REFERENCE = document.form1.reference.value;
    STATUS = document.form1.status.value;
    ORDERTYPE = document.form1.ORDERTYPE.value;
   var displaySummaryLink = document.form1.displaySummaryLink.value;
   var displaySummaryExport = document.form1.displaySummaryExport.value;
    
    if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   	if(TO_DATE != null    && TO_DATE != "") { flag = true;} 
   	if(USER != null    && USER != "") { flag = true;}
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(REFERENCE != null     && REFERENCE != "") { flag = true;}
    if(STATUS != null     && STATUS != "") { flag = true;}
    if(ORDERTYPE != null     && ORDERTYPE != "") { flag = true;}
    
    var urlStr = "../salesestimate/summary";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 1;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableSalesEstimateSummary){
    	tableSalesEstimateSummary.ajax.url( urlStr ).load();
    }else{
    	tableSalesEstimateSummary = $('#tableSalesEstimateSummary').DataTable({
			"processing": true,
			"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
			"ajax": {
				"type": "GET",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.ORDERS[0] === 'undefined'){
		        		return [];
		        	}else {		
		        		for(var dataIndex = 0; dataIndex < data.ORDERS.length; dataIndex ++){
		        			if(displaySummaryLink == 'true'){ 
		        			data.ORDERS[dataIndex]['ESTNO'] = '<a href="../salesestimate/detailPro?estno='+data.ORDERS[dataIndex]['ESTNO']+'">'+data.ORDERS[dataIndex]['ESTNO']+'</a>';
		        			}else{
		        				data.ORDERS[dataIndex]['ESTNO'] = data.ORDERS[dataIndex]['ESTNO'];	
		        			}
		        			if(data.ORDERS[dataIndex]['STATUS']=='PROCESSED')
		        				data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(2,151,69);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
		        			else if(data.ORDERS[dataIndex]['STATUS']=='Open')
			        			data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(38,141,221);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
			        		else if(data.ORDERS[dataIndex]['STATUS']=='Draft')
				        		data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(0,0,0);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';
			        		else
			        			data.ORDERS[dataIndex]['STATUS'] = '<span style="color:rgb(235,97,0);text-transform:uppercase;">'+data.ORDERS[dataIndex]['STATUS']+'</span>';	
		        		}
		        		return data.ORDERS;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'DATE', "orderable": true},
    			{"data": 'ESTNO', "orderable": true},
    			{"data": 'CUSTOMER', "orderable": true},
    			{"data": 'DELIVERY_DATE', "orderable": true},
    			{"data": 'STATUS', "orderable": true},    			
    			{"data": 'AMOUNT', "orderable": true}
			],
			/*"columnDefs": [{"className": "t-right", "targets": [6,7]}],*/
			"orderFixed": [ ], 
			/*"dom": 'lBfrtip',*/
			"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
			"<'row'<'col-md-6'><'col-md-6'>>" +
			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
	        buttons: [
	        	{
	                extend: 'collection',
	                text: 'Export',
	                buttons: [
	                    {
	                    	extend : 'excel',
	                    	exportOptions: {
	    	                	columns: [':visible']
	    	                }
	                    },
	                    {
	                    	extend : 'pdf',
	                    	exportOptions: {
	                    		columns: [':visible']
	                    	},
                    		orientation: 'landscape',
                            pageSize: 'A3',
                            	extend : 'pdfHtml5',
    	                    	exportOptions: {
    	                    		columns: [':visible']
    	                    	},	                    	
    	                        title: function () { var dataview = "<%=CNAME%> \n \n <%=title%> \n \n As On <%=collectionDate%>"  ; return dataview },    	                        
                     		orientation: 'landscape',
                     		customize: function (doc) {
                     			doc.defaultStyle.fontSize = 16;
                     	        doc.styles.tableHeader.fontSize = 16;
                     	        doc.styles.title.fontSize = 20;
                     	       doc.content[1].table.body[0].forEach(function (h) {
                     	          h.fillColor = '#ECECEC';                 	         
                     	          alignment: 'center'
                     	      });
                     	      var rowCount = doc.content[1].table.body.length;
                     	     /*for (i = 1; i < rowCount; i++) {                     	     
                     	     doc.content[1].table.body[i][6].alignment = 'right';
                     	    doc.content[1].table.body[i][7].alignment = 'right';
                     	     }*/ 
                     	      doc.styles.tableHeader.color = 'black';
                     	      
                     	        // Create a footer
                     	        doc['footer']=(function(page, pages) {
                     	            return {
                     	                columns: [
                     	                    '',
                     	                    {
                     	                        // This is the right column
                     	                        alignment: 'right',
                     	                        text: ['page ', { text: page.toString() },  ' of ', { text: pages.toString() }]
                     	                    }
                     	                ],
                     	                margin: [10, 0]
                     	            }
                     	        });
                     		},
                             pageSize: 'A2',
                             footer: true
	                    }
	                ]
	            },
	            {
                    extend: 'colvis',
                    columns: ':not(:eq('+groupColumn+')):not(:last)'
                }
	        ],
	        "order": [],
	        drawCallback: function() {
	        	if(displaySummaryExport == 'false'){ 
	        	$('.buttons-collection')[0].style.display = 'none';
	        	 } 
	        	}
		});
    }
}