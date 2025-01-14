<div id="suppliertypeModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="SupplierForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Supplier Group</h4>
		      	</div>
		      	
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Supplier Group ID:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="SUPPLIER_TYPE_ID1" id="SUPPLIER_TYPE_ID1" type="text" value=""> 
				        		<span class="input-group-addon"  onClick="getNextSupType();">
   		 	              <a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	                 <i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Supplier Group Description:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="SUPPLIER_TYPE_DESC1" id="SUPPLIER_TYPE_DESC1" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onSuppliertypeAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>

<script>

function onSuppliertypeAdd(){
	var suppliertypetid = document.SupplierForm.SUPPLIER_TYPE_ID1.value;
	var suppliertypetdesc = document.SupplierForm.SUPPLIER_TYPE_DESC1.value;
	
	if(suppliertypetid=="" || suppliertypetid.length==0 ) {
		alert("Enter Supplier Group");
		document.getElementById("SUPPLIER_TYPE_ID1").focus();
		return false;
	}
	if(suppliertypetdesc=="" || suppliertypetdesc.length==0 ) {
		alert("Enter Supplier Group Description");
		document.getElementById("SUPPLIER_TYPE_DESC1").focus();
		return false;
	}
	    
	var data = $("form[name ='SupplierForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_SUPPLIER_TYPE_MODAL";
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
				document.SupplierForm.reset();
				$('#suppliertypeModal').modal('toggle');
				suppliertypeCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}


function getNextSupType(){
	$.ajax({
		type: "GET",
// 		url: "../productdept/Auto_ID",
        url : "/track/MasterServlet?ACTION=GET_SUPPLIER_TYPE_SEQUENCE",
		async : true,
		dataType: "json",
		contentType: false,
        processData: false,
		success: function(data) {
			if (data.ERROR_CODE == "100") {
				document.SupplierForm.SUPPLIER_TYPE_ID1.value = data.SUPPLIERTYPE			
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