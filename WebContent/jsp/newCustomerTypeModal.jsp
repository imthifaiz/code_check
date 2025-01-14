<div id="CustTypeModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="CustTypeForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Customer Group</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Customer Group ID:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="CUSTOMER_TYPE_ID1" id="CUSTOMER_TYPE_ID1" type="text" value=""> 
				        		<span class="input-group-addon"  onClick="getNextCustType();">
   		 	              <a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	                 <i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Customer Group Description:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="CUSTOMER_TYPE_DESC1" id="CUSTOMER_TYPE_DESC1" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onCustTypeAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>
function onCustTypeAdd(){
	var custTypeid = document.CustTypeForm.CUSTOMER_TYPE_ID1.value;
	var CustTypedesc = document.CustTypeForm.CUSTOMER_TYPE_DESC1.value;
	
	if(custTypeid=="" || custTypeid.length==0 ) {
		alert("Enter Customer Group ID");
		document.getElementById("CUSTOMER_TYPE_ID1").focus();
		return false;
	}

	if(CustTypedesc=="" || CustTypedesc.length==0 ) {
		alert("Enter Customer Group Description");
		document.getElementById("CUSTOMER_TYPE_DESC1").focus();
		return false;
	}
	    
	var data = $("form[name ='CustTypeForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_CUSTOMER_TYPE";
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
				document.CustTypeForm.reset();
				$('#CustTypeModal').modal('toggle');
				customertypeCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}

function getNextCustType(){
	$.ajax({
		type: "GET",
// 		url: "../productdept/Auto_ID",
        url : "/track/MasterServlet?ACTION=GET_CUSTOMER_TYPE_SEQUENCE",
		async : true,
		dataType: "json",
		contentType: false,
        processData: false,
		success: function(data) {
			if (data.ERROR_CODE == "100") {
				document.CustTypeForm.CUSTOMER_TYPE_ID1.value = data.CUSTOMERTYPE			
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