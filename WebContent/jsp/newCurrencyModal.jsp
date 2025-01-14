<div id="CurrencyModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="CurrencyForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Currency</h4>
		      	</div>
		      	
				<input type = "hidden" id="CURRENCY_ID" name="CURRENCY_ID" >
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Currency ID:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		 <input type="text" class="ac-selected form-control typeahead" id="CURRENCYid" name="CURRENCYid" MAXLENGTH=15 style="width: 100%">
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4">Description:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="DESC" id="DESC" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Display</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="DISPLAY" id="DISPLAY" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Base Currency</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="BASECURRENCY" id="BASECURRENCY" MAXLENGTH=50 size="48" type="text" value="1.000" readonly> 
				        		<span class="input-group-btn"></span>
				        		<input name="Basecur" id="Basecur" type="TEXT" value=""
								size="15" MAXLENGTH=50  class="form-control" readonly>
					   		</div>
			        	</div>
		      		</div>
				</div>
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Equivalent Currency</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="CURREQT" id="CURREQT" type="text"  size="40" MAXLENGTH=50 value=""> 
				        		<span class="input-group-btn"></span>
				        		<input name="currency" type="TEXT" value="" size="15" MAXLENGTH=50  class="form-control" readonly>
					   		</div>
			        	</div>
		      		</div>
				</div>
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4">Remarks</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="REMARK" id="REMARK" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onCurrencyAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>
function onCurrencyAdd(){
    var CURRENCYID   = document.CurrencyForm.CURRENCY_ID.value;
    var DISPLAY   = document.CurrencyForm.DISPLAY.value;
    var CURREQT   = document.CurrencyForm.CURREQT.value;
    if(CURRENCYID == "0" || CURRENCYID == null) {alert("Please Select Currency ID"); document.CurrencyForm.CURRENCY_ID.focus();return false; }
    if(DISPLAY == "" || DISPLAY == null) {alert("Please Enter Display for Currency ID"); document.CurrencyForm.DISPLAY.focus(); return false; }
    if(CURREQT == "" || CURREQT == null) {alert("Please Enter Currency/US Equavalent");document.CurrencyForm.CURREQT.focus(); return false; }
    if(!IsNumeric(document.CurrencyForm.CURREQT.value))
    {
      alert(" Please enter valid  Currency Equivalent !");
      document.CurrencyForm.CURREQT.focus();   document.CurrencyForm.CURREQT.select(); return false;
    }
	
	    
	var data = $("form[name ='CurrencyForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_CURRENCY";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : data,
		dataType : "json",
		success : function(data) {
			if (data.STATUS == "99") {		                               
				alert(data.MESSAGE);
			}
			else{
				document.CurrencyForm.reset();
				$('#CurrencyModal').modal('toggle');
				currencyCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}

$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 
    
    var basecurncy = document.getElementById("Basecurrency").value;
    $("input[name ='Basecur']").val(basecurncy);



/* To get the suggestion data for Currency */
$("#CURRENCYid").typeahead({
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
				ACTION : "GET_CURRENCY_NAME_DATA",
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
			return '<div onclick="getCURRENCYID(\''+data.CURRENCYID+'\')"><p>' + data.CURRENCY + '</p></div>';

		}
	  }
}).on('typeahead:render',function(event,selection){
	  
}).on('typeahead:open',function(event,selection){	
	
}).on('typeahead:close',function(){

}).on('typeahead:change',function(event,selection){
	if($(this).val() == ""){
		document.CurrencyForm.currency.value ="";
	    document.CurrencyForm.CURRENCY_ID.value ="";
	}
});

});


function getCURRENCYID(cur){
	 var basecurncy = document.getElementById("Basecurrency").value;
	//    $("input[name ='currency']").val(basecurncy,cur);
	    document.CurrencyForm.currency.value ="("+ basecurncy +"/" + cur+")";
	    document.CurrencyForm.CURRENCY_ID.value = cur;


}
</script>
