<div id="clearanceTypeModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="clearanceTypeForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New CLEARANCE TYPE</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">CLEARANCE TYPE:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="CLEARANCE_TYPE_ID" id="CLEARANCE_TYPE_ID" type="text" placeholder="Max Character 200" MAXLENGTH=200 value=""> 
					   		</div>
			        	</div>
		      		</div>
		      	       <div class="row form-group">
                        <label class="col-form-modal-label col-sm-4 required">Type</label>
                        <div class="col-sm-6">
                      <label class="radio-inline">          
			      	<input type="radio" checked="checked" name="TYPE" value="0">Import</label>
			    	<label class="radio-inline">
			      	<input type="radio" name="TYPE" value="1">Export</label>
                 </div>
                 </div>	
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onClearanceTypeAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>
function onClearanceTypeAdd() {
	
	   var CLEARANCE_TYPE_ID   = document.clearanceTypeForm.CLEARANCE_TYPE_ID.value;
 	if(CLEARANCE_TYPE_ID=="" || CLEARANCE_TYPE_ID.length==0 ) {
		alert("Enter Clearance Type");
		document.clearanceTypeForm.CLEARANCE_TYPE_ID.focus();
		return false;
	} 
	var data = $("form[name ='clearanceTypeForm']").serialize();
	var urlStr = "/track/MasterServlet?action=CREATE_CLEARANCE_TYPE_MODAL";
    // Call the method of JQuery Ajax provided
    $.ajax({
    	type: "POST",
    	url: urlStr, 
    	async : true,
		data : data,
		dataType : "json",
    	success: function(data) {
			if (data.STATUS == "99") {		                               
				alert(data.MESSAGE);
			}
			else{
				document.clearanceTypeForm.reset();
				$('#clearanceTypeModal').modal('toggle');
				clearanceTypeCallback(data);	/* Define this method in parent page*/
			}						
		}
   	});
          
}
</script>