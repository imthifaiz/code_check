$(document).ready(function(){
	var plant = document.form.plant.value;	
	var cmd = document.form.cmd.value;
	if(cmd == "Edit")
		{
		document.getElementById('shipmentCode').readOnly = true;
		}
	
	/* Order Number Auto Suggestion */
	$('#pono').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PONO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/purchaseorderservlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
				PONO : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.orders);
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
		    return '<p>' + data.PONO + '</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	
	$("#btnBillOpen").click(function(){
		$("#createShipmentForm").submit();
	});
});

function Get_ShipmentCode()
{
	var plant = document.form.plant.value;
	var cmd = document.form.cmd.value;
	if(cmd != "Edit")
		{
	$.ajax({
		type : "POST",
		url : "/track/MasterServlet",
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_SHIPMENT_SEQUENCE"
		},
		dataType : "json",
		success : function(data) {
			$("input[name ='shipmentCode']").val(data.SHIPMENTCODE);
		}
	});
	}
}

function validateShipment(){
	var shipmentCode = document.form.shipmentCode.value;
	var pono = document.form.pono.value;
	if(shipmentCode == ""){
		alert("Please enter Shipment Code.");
		return false;
	}	
	if(pono == ""){
		alert("Please enter the PONO.");
		return false;
	}
	return true;
}