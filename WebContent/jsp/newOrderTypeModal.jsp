<!-- PAGE CREATED BY -->
<!-- RESVIYA -06.12.21 -->

<div id="orderTypeModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="orderTypeForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Order Type </h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Order Type</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="ORDERTYPE" id="ORDERTYPE" type="text" value=""> 
				        		<input name="TYPE" type="hidden" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Description</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="DESCRIPTION" id="DESCRIPTION" type="text" value=""> 
				        		
					   		</div>
			        	</div>
		      		</div>
				</div>
			
			<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 ">Remarks</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="REMARKS" id="REMARKS" type="text" value=""> 
				        		
					   		</div>
			        	</div>
		      		</div>
				</div>
			
			
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onOrderTypeAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>

$(document).ready(function(){
	var ORDER_TYPE_MODAL = document.form1.ORDER_TYPE_MODAL.value;
	document.orderTypeForm.TYPE.value=ORDER_TYPE_MODAL;
	});
	
function onOrderTypeAdd(){
	var ordertype = document.orderTypeForm.ORDERTYPE.value;
	if(ordertype=="" || ordertype.length==0 ) {
		alert("Enter Order Type");
		document.getElementById("ORDERTYPE").focus();
		return false;
	}
	
	var orderdesc = document.orderTypeForm.DESCRIPTION.value;
	if(orderdesc=="" || orderdesc.length==0 ) {
		alert("Enter Description");
		document.getElementById("DESCRIPTION").focus();
		return false;
	}  
	
	 
	var data = $("form[name ='orderTypeForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_ORDER_TYPE";
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
				document.orderTypeForm.reset();
				$('#orderTypeModal').modal('toggle');
				orderTypeCallback(data);	/* Define this method in parent page*/
			}						
		}
	
	});	
	return false;
}
</script>