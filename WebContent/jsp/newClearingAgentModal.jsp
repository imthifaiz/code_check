<style>
.user-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -15%;
	top: 15px;
}
.phideshow{
    float: right;
    margin-top: -34px;
    position: relative;
    z-index: 2;
    margin-right: 40px;
}
</style>
<div id="clearingagentModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="clearingAgentForm" id="clearingAgentForm" method="post" enctype="multipart/form-data">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New CLEARING AGENT</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Clearing Agent ID:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        	<input class="form-control" name="CLEARING_AGENT_ID" id="CLEARING_AGENT_ID" type="text" placeholder="Max Character 50" size="50" MAXLENGTH=50 value="" onkeypress="return blockSpecialChar(event)"> 
			                <span class="input-group-addon"  onClick="onGenID();">
   		 	                <a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	                <i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
					   		</div>
			        	</div>
		      		</div>
		      	<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Clearing Agent Name:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="CLEARING_AGENT_NAME" id="CLEARING_AGENT_NAME" placeholder="Max 100 Characters" size="50" MAXLENGTH=100 type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
		      	        	<div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table clearagent-table">
						<thead>
							<tr>
								<th>Transport Mode</th>
								<th>Contact Name</th>
								<th>Contact Number</th>
								<th>Email</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center">
									<input type="hidden" name="NEWTRANSPORTID" value="">
									<input class="form-control text-left transport " name="newtransport" type="text" placeholder="Enter Transport Mode" maxlength="50"></td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="CONTACTNAME" maxlength="100" placeholder="Enter Contact Name" value="">
								</td>
								<td class="text-center">
									<input class="form-control text-left" name="TELNO" placeholder="Enter Contact Number" maxlength="30"></td>
								<td class="text-center">
									<input  name="EMAIL" class="form-control text-left" maxlength="50" placeholder="Enter Email">
								</td>
							
							</tr>
						</tbody>
					</table>
			         </div>	
						<div class="row form-group">
					    <div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;margin-left: 5%;"
								onclick="addRow()">+ Add New Transport Detail</a>
						</div>
					</div>
			</div>	
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onClearingAgentAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>
$(document).ready(function(){
	$(".clearagent-table tbody").on('click','.user-action',function(){
	    $(this).parent().parent().remove();
	});
	//transport
	$('.transport').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TRANSPORT_MODE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
						ACTION : "GET_TRANSPORT_LIST",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.TRANSPORTMODE);
					}
				});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<div onclick="document.clearingAgentForm.NEWTRANSPORTID.value = \''+data.ID+'\'"><p class="item-suggestion">'+ data.TRANSPORT_MODE +'</p></div></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			/* menuElement.after( '<div class="newtransportBtn footer"  data-toggle="modal" data-target="#transportModal"><a href="#"> + New Transport </a></div>'); */
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.newtransportBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.newtransportBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			$("input[name=NEWTRANSPORTID]").val(selection.ID);
		});
});
function addRow() {

	var body = "";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="NEWTRANSPORTID" value="">';
	body += '<input class="form-control text-left transport" name="newtransport" type="text" placeholder="Enter Transport Mode" maxlength="50"></td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left " type="text" name="CONTACTNAME" maxlength="100" placeholder="Enter Contact Name" value="">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left " type="text" name="TELNO" maxlength="30" placeholder="Enter Contact Number" value="">';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle user-action" aria-hidden="true"></span>';
	body += '<input type="text"  name="EMAIL" maxlength="50" class="form-control text-left"  placeholder="Enter Email" value="">';
	body += '</td>';
	body += '</tr>';
	$(".clearagent-table tbody").append(body);
	removerowclasses();
	addrowclasses();
}
function removerowclasses(){
	$(".transport").typeahead('destroy');
}
function addrowclasses(){
	$('.transport').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TRANSPORT_MODE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
						ACTION : "GET_TRANSPORT_LIST",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.TRANSPORTMODE);
					}
				});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		 //   return '<p>' + data.TRANSPORT_MODE + '</p>';
		    return '<div onclick="document.clearingAgentForm.NEWTRANSPORTID.value = \''+data.ID+'\'"><p class="item-suggestion">'+ data.TRANSPORT_MODE +'</p></div></div>';
		//    return '<div onclick="settransportiddays(this,\''+data.ID+'\',\''+data.CONTACTNAME+'\')"><p>' + data.TELNO + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
/* 			menuElement.after( '<div class="newtransportAddBtn footer"  data-toggle="modal" data-target="#transportModal"><a href="#"> + New Transport </a></div>'); */
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.newtransportAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.newtransportAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			$("input[name=NEWTRANSPORTID]").val(selection.ID);
		});
}
function onGenID(){

	$.ajax({
		type: "GET",
		url: "../clearagent/Auto_ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#CLEARING_AGENT_ID").val(data.AGENTID);
		},
		error: function(data) {
			alert('Unable to generate Clearing Agent ID. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
 

}

function onClearingAgentAdd() {	
 CLEARING_AGENT_ID = document.clearingAgentForm.CLEARING_AGENT_ID.value; 
	if(CLEARING_AGENT_ID=="" || CLEARING_AGENT_ID.length==0 ) {
		alert("Enter Clearing Agent ID");
		document.clearingAgentForm.CLEARING_AGENT_ID.focus();
		return false;
	}
	//var data = $("form[name ='clearingAgentForm']").serialize();
	var data = new FormData($('form#clearingAgentForm')[0]);
	var urlStr = "/track/MasterServlet?action=CREATE_CLEAR_AGENT_MODAL";
    // Call the method of JQuery Ajax provided
    $.ajax({
    	type: "POST",
    	url: urlStr, 
    	async : true,
		data : data,
		dataType : "json",
		contentType: false,
        processData: false,
    	success: function(data) {
			if (data.STATUS == "99") {		                               
				alert(data.MESSAGE);
			}
			else{
				document.clearingAgentForm.reset();
				$('#clearingagentModal').modal('toggle');
				clearingAgentCallback(data);	/* Define this method in parent page*/
			}						
		}
   	});
          
}
</script>