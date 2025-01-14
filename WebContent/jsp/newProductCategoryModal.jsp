<div id="prdcatModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="productcategoryForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Product Category</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Product Category ID:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="PRD_CLS_ID1" id="PRD_CLS_ID1" type="text" value=""> 
				        		<span class="input-group-addon"  onClick="getNextPrdCat();">
   		 	              <a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	                 <i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Product Category Description:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="PRD_CLS_DESC1" id="PRD_CLS_DESC1" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onPrdCatAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>
/* function onAdd(){
	   var ITEM_ID   = document.form1.PRD_CLS_ID.value;
	   var PRD_CLS_DESC = document.form1.PRD_CLS_DESC.value;
	    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Category ID ");document.form1.PRD_CLS_ID.focus(); return false; }
	    if(PRD_CLS_DESC == "" || PRD_CLS_DESC == null) {alert("Please Enter Product Category Description");document.form1.PRD_CLS_DESC.focus(); return false; }
	   document.form1.action  = "classlist_save.jsp?action=ADD";
	   document.form1.submit();
	} */
	
function onPrdCatAdd(){
	 var productcategoryid   = document.productcategoryForm.PRD_CLS_ID1.value;
	 var productcategorydesc = document.productcategoryForm.PRD_CLS_DESC1.value;
	
	if(productcategoryid=="" || productcategoryid.length==0 ) {
		alert("Enter Product Category ID");
		document.getElementById("PRD_CLS_ID1").focus();
		return false;
	}

	if(productcategorydesc=="" || productcategorydesc.length==0 ) {
		alert("Enter Product Category Description");
		document.getElementById("PRD_CLS_DESC1").focus();
		return false;
	}
	    
	var data = $("form[name ='productcategoryForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_PRODUCT_CATEGORY";
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
				document.productcategoryForm.reset();
				$('#prdcatModal').modal('toggle');
				productcategoryCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}

function getNextPrdCat(){
	$.ajax({
		type: "GET",
// 		url: "../productdept/Auto_ID",
        url : "/track/MasterServlet?ACTION=GET_PRODUCT_CATEGORY_SEQUENCE",
		async : true,
		dataType: "json",
		contentType: false,
        processData: false,
		success: function(data) {
			if (data.ERROR_CODE == "100") {
				document.productcategoryForm.PRD_CLS_ID1.value = data.PRDCLASS			
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