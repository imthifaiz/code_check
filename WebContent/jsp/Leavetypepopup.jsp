
<div id="create_leave_type" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h3 class="modal-title">Add Leave Type</h3>
			</div>
			<div class="modal-body">
				<form class="form-horizontal" name="leavetypeformpopup">
					<input type="hidden" class="form-control" id="ukeylt" name="ukeylt" value="">
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-3 required">Leave Type</label>
						<div class="col-sm-6">
							<input type="text" class="form-control" name="leavetypelt" maxlength="100" placeholder="Enter Leave Type">
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-3 required">Employee Type</label>
						<div class="col-sm-6">
							<input type="hidden" name="employeetypeidlt" value="">
							<input type="text" class="form-control emptypelt" name="employeetypelt" placeholder="Select Employee Type">						
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-3 required">Total Entitlement</label>
						<div class="col-sm-6">
							<input type="text" class="form-control totalentitlementlt" name="totalentitlementlt" value="">
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-3 required">Carry Forward</label>
						<div class="col-sm-6">
							<input type="text" class="form-control carryforwardlt" name="carryforwardlt" value="">
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-3">Notes</label>
						<div class="col-sm-6">
							<textarea  name="notelt" class="form-control text-left" maxlength="1000" placeholder="Max 1000 characters"></textarea>
						</div>
					</div>
					
					<div class="form-group">
						<label class="control-label col-form-label col-sm-3">Is No Pay Leave</label>
						<div class="col-sm-6">
							<input type="hidden" name="isnopayleavelt" value="0">
							<input type="checkbox" name="nopayleavelt" onclick="setnoplayleavelt(this)">
						</div>
					</div>
					<div class="row">
						<div class="col-sm-12 txn-buttons">
							<button type="button" class="btn btn-success" onclick="create_leavetype()">Save and Close</button>
							<button class="btn btn-default" data-dismiss="modal" id="cancel">Cancel</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script>

$(document).ready(function(){
	$('.emptypelt').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{		  
		  display: 'EMPLOYEETYPE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/EmployeeTypeServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				async : true,
				data : {
					CMD : "GET_EMPLOYEE_TYPE_DROPDOWN",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.EMPTYPELIST);
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
		    		return '<div onclick="setemployetpridlt(this,\''+data.ID+'\')"><p>' + data.EMPLOYEETYPE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			/*menuElement.after( '<div class="shipmentAddBtn footer"  data-toggle="modal" data-target="#shipmentModal"><a href="#"> + Add Shipment</a></div>');*/
			menuElement.after( '<div class="accountAddBtn footer emptypepopup"  data-toggle="modal" data-target="#create_employee_type"><a href="#"> + New Employee Type</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	
		$(document).on("focusout",".totalentitlementlt", function(){
			var value = $(this).val();
			var decimal=  /^[-+]?[0-9]+\.[0,5]+$/; 
			var numbers = /^[0-9]+$/;
			if(value.match(decimal) || value.match(numbers)) 
			{ 
				var ldays=parseFloat(value).toFixed(1);
				$(this).val(ldays);
			}else{
				alert("Please enter valid total entitlement");
				var ldays=parseFloat("0").toFixed(1);
				$(this).val(ldays);
			}
		});
		
		$(document).on("focusout",".carryforwardlt", function(){
			var value = $(this).val();
			var decimal=  /^[-+]?[0-9]+\.[0,5]+$/; 
			var numbers = /^[0-9]+$/;
			if(value.match(decimal) || value.match(numbers)) 
			{ 
				var ldays=parseFloat(value).toFixed(1);
				$(this).val(ldays);
			}else{
				alert("Please enter valid carry forward");
				var ldays=parseFloat("0").toFixed(1);
				$(this).val(ldays);
			}
		});

});

function setemployetpridlt(obj, id){
	$('input[name = "employeetypeidlt"]').val(id);
}

function setnoplayleavelt(obj) {
	if ($(obj).is(":checked")) {
		$(obj).closest('tr').find("input[name ='isnopayleavelt']").val("1");
		$("input[name ='totalentitlementlt']").val("0.0");
		$("input[name ='carryforwardlt']").val("0.0");
		$("input[name ='totalentitlementlt']").prop( "readonly", true );
		$("input[name ='carryforwardlt']").prop( "readonly", true );
	} else {
		$(obj).closest('tr').find("input[name ='isnopayleavelt']").val("0");
		$("input[name ='totalentitlementlt']").prop( "readonly", false );
		$("input[name ='carryforwardlt']").prop( "readonly", false )
	}
}



	$(".table tbody").on('click','.lvtypepopup',function(){	
		 var obj = $(this);
		 var timestamp = new Date().getUTCMilliseconds();
		 kayid = "key"+timestamp;
		 $(obj).closest('td').attr('id', kayid); 
		 $("input[name ='ukeylt']").val(kayid);
	});
	
	
	function create_leavetype() {
		
		var leavetype=$('input[name = "leavetypelt"]').val();
		var employeetype=$('input[name = "employeetypelt"]').val();
		var totalentitlement=$('input[name = "totalentitlementlt"]').val();
		var carryforward=$('input[name = "carryforwardlt"]').val();
			
		if(leavetype == ""){
			alert("Please enter leave type.");
			document.leavetypeformpopup.leavetypelt.focus();
			return false;
		}
		
		if(employeetype == ""){
			alert("Please enter employee type.");
			document.leavetypeformpopup.employeetypelt.focus();
			return false;
		}
		
		if(totalentitlement == ""){
			alert("Please enter total entitlement.");
			document.leavetypeformpopup.totalentitlementlt.focus();
			return false;
		}
		
		if(carryforward == ""){
			alert("Please enter carry forward.");
			document.leavetypeformpopup.carryforwardlt.focus();
			return false;
		}
		
		
		var formData = $('form[name="leavetypeformpopup"]').serialize();
		$.ajax({
			type : 'post',
			url : "/track/HrLeaveTypeServlet?Submit=Savepopup",
			dataType : 'json',
			data : formData,//{key:val}
			success : function(data) {
				if (data.STATUS == "FAIL") {		                               
					alert(data.MESSAGE);
				}else{
					var ukey = document.leavetypeformpopup.ukeylt.value;
					ukey = "#"+ukey;
					$(ukey).parents("tr").find('input[name="leavetype"]').val(data.LEAVETYPE);
					$(ukey).parents("tr").find('input[name="leavetypeid"]').val(data.LEAVETYPEID);
					$(ukey).parents("tr").find('input[name="totalentitlement"]').val(data.TDAYS);
					$('#create_leave_type').modal('toggle');
				}
			},
			error : function(data) {

				alert(data.responseText);
			}
		});
		return false;
	}
	
</script>
