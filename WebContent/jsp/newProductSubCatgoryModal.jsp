<div id="prdsubcatModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="productsubcategoryForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Product Sub Category</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Product Sub Category ID:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="ITEM_ID1" id="ITEM_ID1" type="text" value=""> 
				        		<span class="input-group-addon"  onClick="getNextPrdSubCat();">
   		 	              <a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	                 <i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Product Sub Category Description:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="ITEM_DESC1" id="ITEM_DESC1" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onPrdSubCatAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>

function onPrdSubCatAdd(){
	 var productsubcategoryid   = document.productsubcategoryForm.ITEM_ID1.value;
	 var productsubcategorydesc = document.productsubcategoryForm.ITEM_DESC1.value;
	
	if(productsubcategoryid=="" || productsubcategoryid.length==0 ) {
		alert("Enter Product Sub Category ID");
		document.getElementById("ITEM_ID1").focus();
		return false;
	}

	if(productsubcategorydesc=="" || productsubcategorydesc.length==0 ) {
		alert("Enter Product Sub Category Description");
		document.getElementById("ITEM_DESC1").focus();
		return false;
	}
	    
	var data = $("form[name ='productsubcategoryForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_PRODUCT_SUB_CATEGORY";
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
				document.productsubcategoryForm.reset();
				$('#prdsubcatModal').modal('toggle');
				productsubcategoryCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}

function getNextPrdSubCat(){
	$.ajax({
		type: "GET",
// 		url: "../productdept/Auto_ID",
        url : "/track/MasterServlet?ACTION=GET_PRODUCT_SUB_CATEGORY_SEQUENCE",
		async : true,
		dataType: "json",
		contentType: false,
        processData: false,
		success: function(data) {
			if (data.ERROR_CODE == "100") {
				document.productsubcategoryForm.ITEM_ID1.value = data.PRDTYPE			
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