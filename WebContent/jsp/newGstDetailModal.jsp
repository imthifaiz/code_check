<div id="gstModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="newGstForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title"><label for="gsttitle" id="TaxLabelOrderManagementTitle"></label></h4>
		      	</div>		      	
		      	<div class="modal-body">
		      		<div class="row form-group">		      			
		      			<label class="col-form-modal-label col-sm-4" for="gsttype" id="TaxLabelOrderManagementName"></label>
			        	<div class="col-sm-6">
		        			<SELECT NAME="GSTTYPE" style="width: 100%" class="form-control" data-toggle="dropdown" data-placement="right" maxlength="9">
								<OPTION selected value="0">Choose</OPTION>
								<OPTION value="PURCHASE">PURCHASE</OPTION>
								<OPTION value="SALES ESTIMATE">SALES ESTIMATE</OPTION>
								<OPTION value="SALES">SALES</OPTION>
								<OPTION value="RENTAL">RENTAL</OPTION>
							</SELECT>
			        	</div>
		      		</div>		      		
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">Description</label>
			        	<div class="col-sm-6">
			        		<input class="form-control" name="GSTDESC" id="GSTDESC" type="text" value=""> 
			        	</div>
		      		</div>		      		
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">Percentage</label>
			        	<div class="col-sm-6">
			        		<input class="form-control" name="GSTPERCENTAGE" id="GSTPERCENTAGE" type="text" value=""> 
			        	</div>
		      		</div>		      		
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">Remarks</label>
			        	<div class="col-sm-6">
			        		<input class="form-control" name="REMARKS" id="REMARKS" type="text" value=""> 
			        	</div>
		      		</div>
		      	</div>
		      	<div class="modal-footer">
			      	<button id="btnBillOpen" type="button" class="btn btn-success" onClick="onGstAdd();">Save</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			     </div>
			</div>
		</form>
	</div>
</div>
<script>
$(document).ready(function(){
	
    $('[data-toggle="tooltip"]').tooltip(); 
    var d = $("#TaxByLabelOrderManagement").val();
    document.getElementById('TaxLabelOrderManagementName').innerHTML =   d +" Type :";
    document.getElementById('TaxLabelOrderManagementTitle').innerHTML = "Create "+ d +" DETAIL :";
});

function onGstAdd(){
	if(document.newGstForm.GSTTYPE.selectedIndex==0){
		alert("Please Select VAT/GST/TAX TYPE");
		document.newGstForm.GSTTYPE.focus();
		return false;
	}

	if(isNaN(document.newGstForm.GSTPERCENTAGE.value)) {
		alert("Please Enter valid VAT/GST/TAX Percentage.");
		document.newGstForm.GSTPERCENTAGE.focus(); 
		return false;
	}
	
	var data = $("form[name ='newGstForm']").serialize();
	
	//document.form1.action  = "/track/GstTypeServlet?action=ADD_GST";
	//document.form1.submit();
	
	var urlStr = "/track/GstTypeServlet?action=ADD_GST";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : data,
		dataType : "json",
		success : function(data) {
			if (data.STATUS == "FAIL") {		                               
				alert(data.MESSAGE);
			}
			else{
				$('#gstModal').modal('toggle');
				gstCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}
</script>