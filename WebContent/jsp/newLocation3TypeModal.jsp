
<div id="locationType3Modal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="locationType3Form" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Location Type Three</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Location Type ID</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="LOC_TYPE_ID_3" id="LOC_TYPE_ID_3" type="text" value=""> 
				        	<span class="input-group-addon"  onClick="onGen3ID();">  
   		 	                <a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	                <i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Location Type Description</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="LOC_DESC" id="LOC_DESC" type="text" value=""> 
				        		
					   		</div>
			        	</div>
		      		</div>
				</div>
			
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onLocationType3Add();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>

function showLoader(){
	$("#loader").show();
	$(".wrapper").css("opacity","0.1");
}

function hideLoader(){
	$("#loader").hide();
	$(".wrapper").css("opacity","1");
}

function onGen3ID(){
	$.ajax({
		type: "GET",
		url: "../locationtypethree/Auto-Generate",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#LOC_TYPE_ID_3").val(data.LOCTYPETHREE);
		},
		error: function(data) {
			alert('Unable to generate Location Type ID. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
}
	
function onLocationType3Add(){
	var loctypeid3 = document.locationType3Form.LOC_TYPE_ID_3.value;
	if(loctypeid3=="" || loctypeid3.length==0 ) {
		alert("Enter Location Type");
		document.getElementById("LOC_TYPE_ID_3").focus();
		return false;
	}
	
	var sLocDesc    = document.locationType3Form.LOC_DESC.value;
	if(sLocDesc   =="" || sLocDesc .length==0 ) {
		alert("Enter Description");
		document.getElementById("LOC_DESC").focus();
		return false;
	}  
	
	
	 
	var data = $("form[name ='locationType3Form']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_LOCATION_TYPE_THREE";
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
				document.locationType3Form.reset();
				$('#locationType3Modal').modal('toggle');
				locationType3Callback(data);	/* Define this method in parent page*/
			}						
		}
	
	});	
	return false;
}
</script>