<div id="emailReportModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="emailReportForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Email Report</h4>
		      	</div>
		      	<div class="modal-body">
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">To</label>
			        	<div class="col-sm-6">
			        		<input class="form-control" name="EMAIL_TO" id="EMAIL_TO" type="text" value=""> 
			        	</div>
		      		</div>
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">CC</label>
			        	<div class="col-sm-6">
			        		<input class="form-control" name="EMAIL_CC" id="EMAIL_CC" type="text" value=""> 
			        	</div>
		      		</div>
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">Body</label>
			        	<div class="col-sm-6">
			        		<textarea class="form-control" name="EMAIL_BODY" id="EMAIL_BODY" value=""></textarea> 
			        	</div>
		      		</div>
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">Report</label>
			        	<div class="col-sm-6">
			        		<input class="form-control" name="EMAIL_ATTCH" id="EMAIL_ATTCH" type="text" value="">			        		 
			        	</div>
			        	<div class="col-sm-2">
			        	<span style="position: relative;bottom: -5px;left: -25px;">.pdf</span>
		      		</div>
		      	</div>
		      	<div class="modal-footer">
			      	<button id="btnSend" type="button" class="btn btn-success" onClick="onSendEmail();">Send</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			     </div>
			</div>
		</form>
	</div>
</div>
<script>
function onSendEmail(){
	var EMAIL_TO = document.emailReportForm.EMAIL_TO.value;
	var EMAIL_ATTCH = document.emailReportForm.EMAIL_ATTCH.value;
	
	if(EMAIL_TO == "" || EMAIL_TO == null){
		alert("Please enter To");
		document.emailReportForm.EMAIL_TO.focus();
		return false;
	}
	
	if(EMAIL_ATTCH == "" || EMAIL_ATTCH == null){
		alert("Please enter Report");
		document.emailReportForm.EMAIL_ATTCH.focus();
		return false;
	}

	
	
	var data = $("form[name ='emailReportForm']").serialize();
	
	var urlStr = "/track/TrialBalanceServlet?ACTION=SEND_EMAILREPORT";
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
				$('#emailReportModal').modal('toggle');
				//payTermsCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}
</script>