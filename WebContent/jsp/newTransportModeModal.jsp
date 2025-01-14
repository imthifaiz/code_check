<div id="transportModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="transportForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Transport Mode</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Transport Mode:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="transport" id="transport" MAXLENGTH=50 type="text" value="" > 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="ontransportAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>
function ontransportAdd(){
	var transportmode = document.transportForm.transport.value;
	if(transportmode=="" || transportmode.length==0 ) {
		alert("Enter Transport Mode");
		document.getElementById("transport").focus();
		return false;
	}
	    
	var data = $("form[name ='transportForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_TRANSPORTMODE_TYPE_MODAL";
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
				document.transportForm.reset();
				$('#transportModal').modal('toggle');
				transportCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}
</script>