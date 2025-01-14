function popUpWin(URL) {
	 subWin = window.open(encodeURI(URL), 'SUPPLIER', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
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
	var plant = document.form.plant.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	/* Paid Through Auto Suggestion */
	$("#paid_through_account_name_x").typeahead({
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
					module:"reconciliationaccounts",
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
			/*menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>');*/
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".smalldrop").css('display') != "block")
				menuElement.next().hide();	  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".smalldrop");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".smalldrop");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}).on('typeahead:select',function(event,selection){
			//onGo(selection.id);
			$("input[name ='paidthrough']").val(selection.id);
		});
	

});

function vendornoandbank(vno,vbank){
	$("input[name ='vendno']").val(vno);
	$("input[name ='bank_account_name']").val(vbank);
	
}

function setSupplierData(suppierData){	
	$("input[name ='vendno']").val(suppierData.vendno);
	$("#vendname").typeahead('val', suppierData.vendname);	
	
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
	var len = document.form.chkdPoNo.length;	
	 if(len == undefined) len = 1;  
    if (document.form.chkdPoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkdPoNo.checked = isChk;
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
              		document.form.chkdPoNo[i].checked = isChk;
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

function openPaymentprocess(evt, pay) {
	  // Declare all variables
	  var i, tabcontent, tablinks;

	  // Get all elements with class="tabcontent" and hide them
	  tabcontent = document.getElementsByClassName("tabcontent");
	  for (i = 0; i < tabcontent.length; i++) {
	    tabcontent[i].style.display = "none";
	  }

	  // Get all elements with class="tablinks" and remove the class "active"
	  tablinks = document.getElementsByClassName("tablinks");
	  for (i = 0; i < tablinks.length; i++) {
	    tablinks[i].className = tablinks[i].className.replace(" active", "");
	  }

	  // Show the current tab, and add an "active" class to the button that opened the tab
	  document.getElementById(pay).style.display = "block";
	  evt.currentTarget.className += " active";
	}
