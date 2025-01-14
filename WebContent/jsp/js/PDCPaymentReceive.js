function popUpWin(URL) {
	 subWin = window.open(encodeURI(URL), 'CUSTOMER', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}

var billpostatus =   [{
    "id": "ALL",
    "value": "ALL",
    "tokens": [
      "ALL"
    ]
  },
  {
	    "id": "PROCESSED",
	    "value": "PROCESSED",
	    "tokens": [
	      "PROCESSED"
	    ]
	  },
	  {
		    "id": "NOT PROCESSED",
		    "value": "NOT PROCESSED",
		    "tokens": [
		      "NOT PROCESSED"
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
	var numberOfDecimal = $("#numberOfDecimal").val();
	
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
//		    	return '<p onclick="custnoandbank(\''+data.CUSTNO+'\',\''+data.BANKNAME+'\')">' + data.CNAME + '</p>';
		    	return '<div onclick="custnoandbank(\''+data.CUSTNO+'\',\''+data.BANKNAME+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.customerAddBtn').remove();  
			/*$('.customer-section .tt-menu').after( '<div class="customerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#"> + New Cutomer</a></div>');*/
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
		});

	/*$("#bank_account_name").typeahead({
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
					BANK : query
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
				return '<span>'+data.NAME+'</p></span>';
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
		});*/
	
	
	
	/* Bank Name Auto Suggestion */
	$('#bank_account_name').typeahead({
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
		    return '<p>' + data.NAME + '</p>';
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
	$(".status").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'billpostatus',
	  display: 'value',  
	  source: substringMatcher(billpostatus),
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

function custnoandbank(cno,vbank){
	$("input[name ='CUST_CODE']").val(cno);
	$("input[name ='bank_account_name']").val(vbank);
	
}

function setCustomerData(customerData){	
	$("input[name ='CUST_CODE']").val(customerData.custno);
	$("#CUSTOMER").typeahead('val', customerData.custname);	
	
}

function setDate(i)
{
	/*var len = document.form.chkdPoNo.length;
	if(len == undefined) len = 1;
	if(len == 1 && document.form.chkdPoNo.checked){
		$('#Reverse_'+i).datepicker("enable");
		$('#Reverse_'+i).datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
	}
	else if(len != 1 && document.form.chkdPoNo[i].checked){
		$('#Reverse_'+i).datepicker("enable");
		$('#Reverse_'+i).datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
	}
	else
		{
		document.getElementById("Reverse_"+i).value = "";
		$('#Reverse_'+i).datepicker("disable");
		}*/	
}

function checkAll(isChk)
{
	var len = document.form1.chkdPoNo.length;	
	 if(len == undefined) len = 1;  
    if (document.form1.chkdPoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkdPoNo.checked = isChk;
              		document.form1.chkhdrid.checked = isChk;
              		/*if(isChk==true)
          			{
          		$('#Reverse_'+i).datepicker("enable");
          		$('#Reverse_'+i).datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
          			}
          		else
          			{
          			document.getElementById("Reverse_"+i).value = "";
          			$('#Reverse_'+i).datepicker("disable");
          			}*/
               	}
              	else{
              		document.form1.chkdPoNo[i].checked = isChk;
              		document.form1.chkhdrid[i].checked = isChk;
              		/*if(isChk==true)
              			{
              		$('#Reverse_'+i).datepicker("enable");
              		$('#Reverse_'+i).datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
              			}
              		else
              			{
              			document.getElementById("Reverse_"+i).value = "";
              			$('#Reverse_'+i).datepicker("disable");
              			}*/
              	}
        }
    }
}
