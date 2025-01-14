<div id="incotermModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="incotermsForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New INCOTERM</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">INCOTERM:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="INCOTERMS" id="INCOTERMS" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onIncotermAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>
function onIncotermAdd() {
	var incoterms = document.incotermsForm.INCOTERMS.value;
	if(incoterms=="" || incoterms.length==0 ) {
		alert("Enter INCOTERMS");
		document.incotermsForm.INCOTERMS.focus();
		return false;
	}
    var urlStr = "/track/MasterServlet";
    // Call the method of JQuery Ajax provided
    $.ajax({
    	type: "POST",
    	url: urlStr, 
    	data: {INCOTERMS:incoterms,action: "ADD_INCOTERMS",ISMODAL:"Y"},
    	dataType: "json", 
    	success: function(data) {
			if (data.STATUS == "99") {		                               
				alert(data.MESSAGE);
			}
			else{
				document.incotermsForm.reset();
				$('#incotermModal').modal('toggle');
				incotermsCallback(data);	/* Define this method in parent page*/
			}						
		}
   	});
          
}
</script>