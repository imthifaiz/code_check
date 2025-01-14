$(document).ready(function(){
	
});

function generateLocation()
{
	$.ajax({
		type : "POST",
		url: '/track/Locations',
		dataType: 'json',
		data : {
			action : "generateLocation",
		},
		success : function(data) {
			
		}
	});
}