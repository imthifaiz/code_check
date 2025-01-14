<div id="sg-taxreturncustompopup" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<form class="form-horizontal" name="taxcustomperiod" method="post">
		<input type="hidden" name="modaltaxheaderid" id="modaltaxheaderid"/>
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Reporting Period</h4>
				</div>
				<div class="modal-body">
				<div class="row form-group">
	        	<label class="control-label col-form-label col-sm-4 required">GST Return Start Date</label>
	        	<div class="col-sm-6">
	        		<input class="form-control" name="vatstartdate" id="REFERENCE" type="TEXT" disabled/> 
	        	</div>
	        	</div>
				<div class="row form-group">
						<label class="control-label col-form-label col-sm-4 required"
							for="Date">GST Return End Date</label>
						<div class="col-sm-6">
						<INPUT class="form-control datepicker" id="vatenddate" type="TEXT" size="30" MAXLENGTH="10" name="vatenddate"/>
						</div>
				</div>
				</div>
				<div class="modal-footer">
	    		<button id="btnBillOpen" type="button" class="btn btn-success" onClick="generateTaxCustom();">Generate</button>
		<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
	    </div>
			</div>
		</form>
	</div>
</div>