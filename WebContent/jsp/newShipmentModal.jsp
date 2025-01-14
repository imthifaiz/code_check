<div id="shipmentModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<form name="shipmentForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Shipment</h4>
		      	</div>
		      	<div class="modal-body">
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">Shipment Code</label>
			        	<div class="col-sm-6">
			        		<input type="text" class="form-control" id="shipmentCode" name="shipmentCode" readonly>				
							<span class="auto-icon" onclick="Get_ShipmentCode();"><i class="fa fa-cog" style="font-size: 20px; color: #0059b3"></i></span> 
			        	</div>
		      		</div>
		      		<div class="row form-group">
			      		<label class="col-form-modal-label col-sm-4">PONO</label>
			        	<div class="col-sm-6">
			        		<input type="text" class="form-control" id="shipmentpono" name="shipmentpono" readonly>
							<!-- <span class="select-icon" onclick="$(this).parent().find('input[name=\'shipmentpono\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span> --> 
			        	</div>
		      		</div>
		      	</div>
		      	<div class="modal-footer">
			      	<button id="btnBillOpen" type="button" class="btn btn-success" onClick="onAddShipment();">Save and Close</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			     </div>
			</div>
		</form>
	</div>
</div>

<script>

function Get_ShipmentCode()
{	
	var pono = document.shipmentForm.shipmentpono.value;
	$.ajax( {
		type : "GET",
		url : "/track/MasterServlet?ACTION=GET_SHIPMENT_SEQUENCE&ORDERNO="+pono,
		async : true,
		dataType : "json",
		contentType: false,
        processData: false,
        success : function(data) {
			if (data.ERROR_CODE == "100") {
				document.shipmentForm.shipmentCode.value = data.SHIPMENTCODE;
			}
			else{
				alert("Something went wrong. Please try again later.");
			}						
		},
        error: function (data) {	        	
        	alert(data.Message);
        }
	});	
	return false;
}
function onAddShipment(){
	var shipmentCode = document.shipmentForm.shipmentCode.value;
	var pono = document.shipmentForm.shipmentpono.value;
	
	if(shipmentCode == "" || shipmentCode == null){
		alert("Please enter Shipment Code");
		document.payTermsForm.shipmentCode.focus();
		return false;
	}
	
	if(pono == "" || pono == null){
		alert("Please enter pono");
		document.shipmentForm.shipmentpono.focus();
		return false;
	}
	
	
	var data = $("form[name ='shipmentForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=SAVE_SHIPMENT";
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
				$('#shipmentModal').modal('toggle');
				shipmentCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}
//Blocked By Azees (29.2.20)
/* $('#shipmentpono').typeahead({
	hint : true,
	minLength : 0,
	searchOnFocus : true
	},
	{
	//name: 'states',
	display : 'PONO',
	async : true,
	//source: substringMatcher(states),
	source : function(query, process, asyncProcess) {
		var urlStr = "/track/purchaseorderservlet";
		$.ajax({
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : document.form1.plant.value,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				PONO : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.orders);
			}
		});
	},
	limit : 9999,
	templates : {
		empty : [ '<div style="padding:3px 20px">',
				'No results found', '</div>', ].join('\n'),
		suggestion : function(data) {
			return '<p>' +data.PONO+ '</p>';
		}
	}
}); */
// End

/* $('#shipmentpono').typeahead({
	var pono=document.form1.plant;
	alert(pono);
	//document.shipmentForm.shipmentpono.value = pono;
	hint : true,
	minLength : 0,
	searchOnFocus : true
	},
	{
	//name: 'states',
	display : 'PONO',
	async : true,
	//source: substringMatcher(states),
	source : function(query, process, asyncProcess) {
		var urlStr = "/track/purchaseorderservlet";
		$.ajax({
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : document.form1.plant.value,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				PONO : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.orders);
			}
		});
	},
	limit : 9999,
	templates : {
		empty : [ '<div style="padding:3px 20px">',
				'No results found', '</div>', ].join('\n'),
		suggestion : function(data) {
			return '<p>' +data.PONO + '</p>';
		}
	}
}); */
</script>