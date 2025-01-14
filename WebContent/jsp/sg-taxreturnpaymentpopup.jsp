<div id="sg-taxreturnpaymentpopup" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<form class="form-horizontal" name="taxpayment" method="post">
		<input type="hidden" name="modaltaxpayheaderid" id="modaltaxpayheaderid"/>
		<input type="hidden" name="modaltaxreturndate" id="modaltaxreturndate"/>
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Record GST Payment</h4>
				</div>
				<div class="modal-body">
				<div class="row form-group">
	        	<label class="control-label col-form-label col-sm-4 required">Paid Through</label>
	        	<div class="col-sm-6">
	        		<select class="form-control" name="paidthr" id="paidthr">
	        			<option value="Undeposited Funds">Undeposited Funds</option>
	        			<option value="Petty Cash">Petty Cash</option>
	        		</select>
	        		
	        	</div>
	        	</div>
	        	<div class="row form-group">
	        	<label class="control-label col-form-label col-sm-4">Total GST Payable</label>
	        	<div class="col-sm-6">
	        		<input class="form-control" name="tottaxpay" id="tottaxpay" type="TEXT" disabled/> 
	        	</div>
	        	</div>
	        	<div class="row form-group">
	        	<label class="control-label col-form-label col-sm-4">Amount Due</label>
	        	<div class="col-sm-6">
	        		<input class="form-control" name="amountdue" id="amountdue" type="TEXT" disabled/> 
	        	</div>
	        	</div>
	        	<div class="row form-group">
	        	<label class="control-label col-form-label col-sm-4 required">Amount Paid</label>
	        	<div class="col-sm-6">
	        		<input class="form-control" name="amountpaid" id="amountpaid" type="TEXT" required/> 
	        	</div>
	        	</div>
				<div class="row form-group">
						<label class="control-label col-form-label col-sm-4 required"
							for="Date">Payment Date</label>
						<div class="col-sm-6">
						<INPUT class="form-control datepicker" id="paymentdate" type="TEXT" size="30" MAXLENGTH="10" name="paymentdate" required/>
						</div>
				</div>
				<div class="row form-group">
	        	<label class="control-label col-form-label col-sm-4">Reference Number</label>
	        	<div class="col-sm-6">
	        		<!-- <input class="form-control" name="referencenum" id="referencenum" type="TEXT"/>  -->
	        		<textarea class="form-control" name="referencenum" id="referencenum" MAXLENGTH="600"></textarea>
	        	</div>
	        	</div>
	        	<div class="row form-group">
	        	<label class="control-label col-form-label col-sm-4">Description</label>
	        	<div class="col-sm-6">
	        		<textarea class="form-control" name="desc" id="desc" MAXLENGTH="100"></textarea> 
	        	</div>
	        	</div>
				</div>
				<div class="modal-footer">
	    		<button id="payTaxBtn" type="button" class="btn btn-success" onClick="payTax();">Save</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	    </div>
			</div>
		</form>
	</div>
</div>