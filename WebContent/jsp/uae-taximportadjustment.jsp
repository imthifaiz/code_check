<div id="uae-taximportadjustmentModal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<form class="form-horizontal" name="taximportform" method="post">
		<input type="hidden" name="modaltaxheaderid" id="modaltaxheaderid"/>
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Adjust amount in box - 7</h4>
				</div>
				<div class="modal-body">
				<div class="row form-group">
						<label class="control-label col-form-label col-sm-4 required"
							for="Date">Date</label>
						<div class="col-sm-6">
						<INPUT class="form-control datepicker" type="TEXT" size="30" MAXLENGTH="10" name="DATE"/>
						</div>
				</div>
				
				<div class="row form-group">
	        	<label class="control-label col-form-label col-sm-4">Reference</label>
	        	<div class="col-sm-6">
	        		<input class="form-control" name="REFERENCE" id="REFERENCE" type="TEXT"/> 
	        	</div>
	        	</div>
	        	
	        	<div class="row form-group">
						<label class="control-label col-form-label col-sm-4 required"
							for="Amount">Amount</label>
						<div class="col-sm-6">
						<INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="AMOUNT"  id="AMOUNT" onkeypress="return isNumberKey(event,this,6)" />
						</div>
				</div>
				
				<div class="row form-group">
						<label class="control-label col-form-label col-sm-4 required"
							for="TaxAmount">Tax Amount</label>
						<div class="col-sm-6">
						<INPUT class="form-control" type="TEXT" MAXLENGTH="20" name="TAXAMOUNT"  id="TAXAMOUNT" onkeypress="return isNumberKey(event,this,6)" />
						</div>
				</div>
				
				<div class="row form-group">
	        	<label class="control-label col-form-label col-sm-4 required">Account</label>
	        	<div class="col-sm-6">
	        		<input class="form-control accountSearch" name="ACCOUNT" id="ACCOUNT" type="TEXT" MAXLENGTH="100" placeholder="Select Account" /> 
	        	</div>
	        	</div>
	        	
	        	<div class="form-group">
					<label class="control-label col-form-label col-sm-4 required"
							for="Reason">Reason</label>
									<div class="col-sm-6">
										<textarea name="REASON" MAXLENGTH=200 class="form-control "></textarea>
									</div>
				</div>
				
				<span>Note:You can view the adjustments you make here by going to <a herf="">Tax Adjustments
				</a> </span>
	        	
				</div>
				<div class="modal-footer">
	    		<button id="btnBillOpen" type="button" class="btn btn-success" onClick="onAddTaxImport();">Adjust</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	    </div>
			</div>
		</form>
	</div>
</div>
<SCRIPT type="text/javascript">
<!-- Begin

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}
	
$(document).ready(function(){	
	var plant = $('input[name ="plant"]').val();
/* To get the suggestion data for Account */
$(".accountSearch").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true,
	  classNames: {
		 	menu: 'smalldrop'
		  }
	},
	{	  
	  display: 'text',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ChartOfAccountServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "getSubAccountTypeGroup",
				module:"taximportaccount",
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
			/* if (item.issub) {
				var $state = $(
					    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
					  );
				}
			else
				{ */
				var $state = $(
						 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
					  );
				//}
			  
			  return $state;
		}
	  }
	}).on('typeahead:render',function(event,selection){
		
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	});
});

function onAddTaxImport(){
	   var DATE   = document.taximportform.DATE.value;
	   
	   if(DATE == "" || DATE == null) {
	   alert("Please Enter Date"); 
	   document.taximportform.DATE.focus();
	   return false; 
	   }
	   
	   var AMOUNT  = document.taximportform.AMOUNT.value;
	   
	   if(AMOUNT == "" || AMOUNT == null) {
		   alert("Please Enter Amount"); 
		   document.taximportform.AMOUNT.focus();
		   return false; 
		   }
	   
	   if(!IsNumeric(AMOUNT))
	   {
	     alert(" Please Enter Amount");
	     document.taximportform.AMOUNT.focus();  document.taximportform.AMOUNT.select(); return false;
	   }
	   
		var TAXAMOUNT  = document.taximportform.TAXAMOUNT.value;
	   
	   if(TAXAMOUNT == "" || TAXAMOUNT == null) {
		   alert("Please Enter Tax Amount"); 
		   document.taximportform.TAXAMOUNT.focus();
		   return false; 
		   }
	   
	   if(!IsNumeric(taximportform.TAXAMOUNT.value))
	   {
	     alert(" Please Enter Tax Amount");
	     document.taximportform.TAXAMOUNT.focus();  document.taximportform.TAXAMOUNT.select(); return false;
	   }
	   
	   var ACCOUNT   = document.taximportform.ACCOUNT.value;
	   if(ACCOUNT == "" || ACCOUNT == null) {alert("Please Enter Account");document.taximportform.ACCOUNT.focus(); return false; }
	   
	   var REASON   = document.taximportform.REASON.value;
	   if(REASON == "" || REASON == null) {alert("Please Enter Reason");document.taximportform.REASON.focus(); return false; }
	   
		var formData = $('form[name="taximportform"]').serialize();
		$.ajax({
			type : 'post',
			url : "/track/TaxReturnServlet?action=create",
			dataType : 'json',
			data : formData,//{key:val}
			success : function(data) {
				if (data.STATUS == "FAIL") {		                               
					alert(data.MESSAGE);
				}else{
					$('#uae-taximportadjustmentModal').modal('toggle');		
					var frm = document.getElementsByName('taximportform')[0];
					frm.reset(); 
					reloadData();
				}
			},
			error : function(data) {
				
				alert(data.responseText);
			}
		});
	   
	}

</SCRIPT>