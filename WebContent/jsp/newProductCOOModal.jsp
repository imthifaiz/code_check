<div id="prdcooModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="productcooForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New COO</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">COO:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="ITEM_ID1" id="ITEM_ID1" type="text" value=""> 
				        		<!-- <span class="input-group-addon"  onClick="getNextPrdDep();">
   		 	              <a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	                 <i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span> -->
					   		</div>
			        	</div>
		      		</div>
				</div>
				
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onCooAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>
function onCooAdd(){
	var coo = document.productcooForm.ITEM_ID1.value;
	
	
	if(coo=="" || coo.length==0 ) {
		alert("Enter COO");
		document.getElementById("ITEM_ID").focus();
		return false;
	}

	/* if(productdepartmentdesc=="" || productdepartmentdesc.length==0 ) {
		alert("Enter Product Department Description");
		document.getElementById("ITEM_ID1").focus();
		return false;
	} */
	    
	var data = $("form[name ='productcooForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_PRODUCT_COO";
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
				document.productcooForm.reset();
				$('#prdcooModal').modal('toggle');
				productcooCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}


</script>