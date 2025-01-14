<div id="bankMstModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="bankMstForm" method="post">
			<input type="hidden" name="MODAL" value="Y">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Bank Details</h4>
		      	</div>
		      	<div class="modal-body">
		      		<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-2 required">Bank Name:</label>
		      			<div class="col-sm-4">
			        		<div class="input-group">
				        		<input class="form-control" name="BANK_NAME" id="b_bank" MAXLENGTH=100 type="text" value=""> 
					   		</div>
			        	</div>
			        	
			        	<label class="col-form-modal-label col-sm-2">Branch:</label>
		      			<div class="col-sm-4">
			        		<div class="input-group">
				        		<input class="form-control" name="BANK_BRANCH" id="b_branch_name" MAXLENGTH=100 type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
		      		
		      		<div class="row form-group">
		      			<label class="col-form-modal-label col-sm-2 required">Branch Code:</label>
		      			<div class="col-sm-4">
			        		<div class="input-group">
				        		<input class="form-control" name="BANK_BRANCH_CODE" id="b_branch" MAXLENGTH=50 type="text" value=""> 
					   		</div>
			        	</div>
			        	
			        	<label class="col-form-modal-label col-sm-2">IFSC CODE:</label>
		      			<div class="col-sm-4">
			        		<div class="input-group">
				        		<input class="form-control" name="IFSC" id="b_ifsc" MAXLENGTH=50 type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
		      		
		      		<div class="row form-group">
		      			<label class="col-form-modal-label col-sm-2">Swift Code:</label>
		      			<div class="col-sm-4">
			        		<div class="input-group">
				        		<input class="form-control" name="SWIFT_CODE" id="b_swift_code" MAXLENGTH=50 type="text" value=""> 
					   		</div>
			        	</div>
			        	
			        	<label class="col-form-modal-label col-sm-2">Tel No:</label>
		      			<div class="col-sm-4">
			        		<div class="input-group">
				        		<input class="form-control" name="TELNO" id="b_telno" MAXLENGTH=30 type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
		      		
		      		<div class="row form-group">
			        	<label class="col-form-modal-label col-sm-2">Fax:</label>
		      			<div class="col-sm-4">
			        		<div class="input-group">
				        		<input class="form-control" name="FAX" id="b_fax" MAXLENGTH=30 type="text" value=""> 
					   		</div>
			        	</div>
			        	
			        	<label class="col-form-modal-label col-sm-2">Email:</label>
		      			<div class="col-sm-4">
			        		<div class="input-group">
				        		<input class="form-control" name="EMAIL" id="b_email" MAXLENGTH=50 type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
		      		
		      		<div class="row form-group">
			        	<label class="col-form-modal-label col-sm-2">Web site:</label>
		      			<div class="col-sm-4">
			        		<div class="input-group">
				        		<input class="form-control" name="WEBSITE" id="b_website" MAXLENGTH=50 type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
		      		
		      		<div class="bs-example">
		      			<ul class="nav nav-tabs" id="myTabBank">
		      				<li class="nav-item active">
					            <a href="#b_address" class="nav-link" data-toggle="tab" aria-expanded="true">Address</a>
					        </li>
					        <li class="nav-item">
					            <a href="#b_contact_details" class="nav-link" data-toggle="tab">Contact Details</a>
					        </li>
					        <li class="nav-item">
					            <a href="#b_other" class="nav-link" data-toggle="tab">Other Details</a>
					        </li>
					        <li class="nav-item">
					            <a href="#b_remarks" class="nav-link" data-toggle="tab">Remarks</a>
					        </li>
		      			</ul>
		      			
		      			<div class="tab-content">
		      				<div class="tab-pane active" id="b_address">
		      				<br>
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">Unit No:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="UNITNO" id="b_unitno" MAXLENGTH=50 
							        		type="text" value=""> 
								   		</div>
						        	</div>
		      					</div>
		      					
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">Building:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="BUILDING" id="b_building" MAXLENGTH=50
							        		type="text" value=""> 
								   		</div>
						        	</div>
		      					</div>
		      					
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">Street:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="STREET" id="b_street" MAXLENGTH=100 
							        		type="text" value=""> 
								   		</div>
						        	</div>
		      					</div>
		      					
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">City:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="CITY" id="b_city" MAXLENGTH=50
							        		type="text" value=""> 
								   		</div>
						        	</div>
		      					</div>
		      					
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">State:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="STATE" id="b_state" MAXLENGTH=50
							        		type="text" value=""> 
								   		</div>
						        	</div>
		      					</div>
		      					
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">Country:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="COUNTRY" id="b_country" MAXLENGTH=50
							        		type="text" value=""> 
								   		</div>
						        	</div>
		      					</div>
		      					
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">Postal Code:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="ZIP" id="b_postal_code" MAXLENGTH=10
							        		type="text" value=""> 
								   		</div>
						        	</div>
		      					</div>
		      				</div>
		      				
		      				<div class="tab-pane fade" id="b_contact_details">
		      				<br>
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">Contact Person:</label>
					      			<div class="col-sm-4">
					      				<div class="input-group">
					      					<input class="form-control" name="CONTACT_PERSON" id="b_contact" MAXLENGTH=50
					      					type="text" value="">
						        		</div>
						        	</div>
					        	</div>
					        	
					        	<div class="row form-group">
						        	<label class="col-form-modal-label col-sm-2">Hand Phone:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="HPNO" id="b_hpno" MAXLENGTH=30 type="text" value=""> 
								   		</div>
						        	</div>
					        	</div>
		      				</div>
		      				
		      				<div class="tab-pane fade" id="b_other">
		      				<br>
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">Facebook Id:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="FACEBOOKID" id="b_facebook_id" MAXLENGTH=50
							        		type="text" value=""> 
								   		</div>
						        	</div>
		      					</div>
		      					
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">Twitter Id:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="TWITTERID" id="b_twitter_id" MAXLENGTH=50
							        		type="text" value=""> 
								   		</div>
						        	</div>
		      					</div>
		      					
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">LinkedIn Id:</label>
					      			<div class="col-sm-4">
						        		<div class="input-group">
							        		<input class="form-control" name="LINKEDINID" id="b_linkedin_in" MAXLENGTH=50
							        		type="text" value=""> 
								   		</div>
						        	</div>
		      					</div>		      					
		      				</div>
		      				<div class="tab-pane fade" id="b_remarks">
		      				<br>
		      					<div class="row form-group">
		      						<label class="col-form-modal-label col-sm-2">Remarks:</label>
					      			<div class="col-sm-4">
					      				<div class="input-group">
							        		<textarea rows="2" name="NOTE" id="b_note" MAXLENGTH=1000 
							        		class="form-control"></textarea>
						        		</div>
						        	</div>
					        	</div>
		      				</div>
		      			</div>
		      		</div>
		      	</div>
      		    <div class="modal-footer">
      		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" 
			    		onClick="onBankAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			</div>
		</form>
	</div>
</div>
<script>
function onBankAdd(){
	var BANK_NAME   = document.bankMstForm.BANK_NAME.value;
//	var BANK_BRANCH = document.bankMstForm.BANK_BRANCH.value;
	var BANK_BRANCH_CODE = document.bankMstForm.BANK_BRANCH_CODE.value;
    if(BANK_NAME == "" || BANK_NAME == null) {
    	alert("Please Enter Bank Name");
    	document.bankMstForm.BANK_NAME.focus(); 
    	return false; 
   	}
   /*  if(BANK_BRANCH == "" || BANK_BRANCH == null) {
    	alert("Please Enter Bank Branch");
    	document.bankMstForm.BANK_BRANCH.focus(); 
    	return false; 
   	} */
    if(BANK_BRANCH_CODE == "" || BANK_BRANCH_CODE == null) {
    	alert("Please Enter Bank Branch Code");
    	document.bankMstForm.BANK_BRANCH_CODE.focus(); 
    	return false; 
   	}
	    
	var data = $("form[name ='bankMstForm']").serialize();
	
	var urlStr = "/track/BankServlet?action=ADD";
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
				document.bankMstForm.reset();
				$('#bankMstModal').modal('toggle');
				 $("input[name ='bank_name']").val(data.BANK_NAME);
				bankCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}
</script>