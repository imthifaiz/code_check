<!-- PAGE CREATED BY -->
<!-- RESVIYA -14.12.21 -->



<div id="locationType2Modal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="locationType2Form" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Location Type Two</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Location Type ID</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="LOC_TYPE_ID_2" id="LOC_TYPE_ID_2" type="text" value=""> 
				        	<span class="input-group-addon"  onClick="onGen2ID();">  
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
			    		<button type="button" class="btn btn-success" onClick="onLocationType2Add();">Save</button>
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

function onGen2ID(){
	$.ajax({
		type: "GET",
		url: "../locationtypetwo/Auto-Generate",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#LOC_TYPE_ID_2").val(data.LOCTYPETWO);
		},
		error: function(data) {
			alert('Unable to generate Location Type ID. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
}
	
function onLocationType2Add(){
	var loctypeid2 = document.locationType2Form.LOC_TYPE_ID_2.value;
	if(loctypeid2=="" || loctypeid2.length==0 ) {
		alert("Enter Location Type");
		document.getElementById("LOC_TYPE_ID_2").focus();
		return false;
	}
	
	var sLocDesc    = document.locationType2Form.LOC_DESC.value;
	if(sLocDesc   =="" || sLocDesc .length==0 ) {
		alert("Enter Description");
		document.getElementById("LOC_DESC").focus();
		return false;
	}  
	
	
	 
	var data = $("form[name ='locationType2Form']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_LOCATION_TYPE_TWO";
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
				document.locationType2Form.reset();
				$('#locationType2Modal').modal('toggle');
				locationType2Callback(data);	/* Define this method in parent page*/
			}						
		}
	
	});	
	return false;
}
</script>