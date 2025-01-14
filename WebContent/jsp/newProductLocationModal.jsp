<div id="prdlocModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="productlocationForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Product Location</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Product Location ID:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="LOC_ID1" id="LOC_ID1" type="text" value=""> 
				        		<span class="input-group-addon"  onClick="getNextLocCat();">
   		 	              <a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	                 <i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4">Product Location Description:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="LOC_DESC1" id="LOC_DESC1" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onPrdLocAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>

	
function onPrdLocAdd(){
	 var productlocationid   = document.productlocationForm.LOC_ID1.value;
	// var productlocationdesc = document.productlocationForm.LOC_DESC1.value;
	
	if(productlocationid=="" || LOC_ID.length==0 ) {
		alert("Enter Product Location ID");
		document.getElementById("LOC_ID1").focus();
		return false;
	}

	/* if(productlocationdesc=="" || productlocationdesc.length==0 ) {
		alert("Enter Product Location Description");
		document.getElementById("LOC_DESC1").focus();
		return false;
	} */
	    
	var data = $("form[name ='productlocationForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_PRODUCT_LOCATION";
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
				document.productlocationForm.reset();
				$('#prdlocModal').modal('toggle');
				productlocationCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}

function getNextLocCat(){
	$.ajax({
		type: "GET",
// 		url: "../productdept/Auto_ID",
        url : "/track/MasterServlet?ACTION=GET_PRODUCT_LOCATION_SEQUENCE",
		async : true,
		dataType: "json",
		contentType: false,
        processData: false,
		success: function(data) {
			if (data.ERROR_CODE == "100") {
				document.productlocationForm.LOC_ID1.value = data.LOCATION			
				}
		    
	     else{
				alert("Something went wrong. Please try again later.");
			}	
	},		
				error: function(data) {
		        	alert(data.Message);
		
				}
		
});	
	return false;
}
</script>