<div id="prduomModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="productuomForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">New Product UOM</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Unit Of Measure</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="ITEM_ID1" id="ITEM_ID1" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Unit Of Measure Description</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="ITEM_ID_DESC1" id="ITEM_ID_DESC1" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Display</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="Display" id="Display" type="text" value=""> 
					   		</div>
			        	</div>
		      		</div>
				</div>
				<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Quantity Per UOM</label>
		      			<div class="col-sm-3">
			        		<div class="input-group">
				        		<input class="form-control"   onchange="checkno1()" name="QPUOM" id="QPUOM" type="text" value=""> 
					   		</div>
					   	</div>
			        	<div class="col-sm-3 squom" hidden>
        				 	<input class="form-control " type="text" value="" name="CUOM" id="CUOM" placeholder="Select UOM" >
     				</div>
		      		</div>
		      		</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onPrdUomAdd();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    
			</div>
			
		</form>
	</div>
</div>
<script>

$(document).ready(function(){
	var plant = document.form.plant.value;
	$('#CUOM').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'UOM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_UOM_DATA",
				UOM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.UOMMST);
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p></div>';
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
});
function onPrdUomAdd(){
	   var ITEM_ID1   = document.productuomForm.ITEM_ID1.value;
	   var Display   = document.productuomForm.Display.value;
	   var QPUOM   = document.productuomForm.QPUOM.value;
	    if(ITEM_ID1 == "" || ITEM_ID1 == null)
		{
			alert("Please Enter UOM  ");
			document.productuomForm.ITEM_ID1.focus(); 
			return false; 
			}
		
		else if(Display == "" || Display == null) 
	    {
	    	alert("Please Enter Display for UOM");
	    	document.productuomForm.Display.focus();
	    	return false; 
	    	}
	    else if(!IsNumeric(document.productuomForm.QPUOM.value))
	    {
	      alert("Please Enter valid Quantity Per UOM");
	      productuomForm.QPUOM.focus(); productuomForm.QPUOM.select();
	      return false;
	    }
	    else if(QPUOM == "" || QPUOM <= 0)
	    {
	        alert("Please Enter Quantity Per UOM");
	        document.productuomForm.QPUOM.focus();
	        return false;
	    }else{
	    	 if (QPUOM != 1){
	    		 if(CUOM == "" || CUOM <= 0)
	    		    {
	    			 	alert("Please select Convertible UOM");
	    		        document.productuomForm.CUOM.focus();
	    		        return false;
	    		    }
	    	 }
	    }
	    
	var data = $("form[name ='productuomForm']").serialize();
	
	var urlStr = "/track/MasterServlet?action=CREATE_PRODUCT_UOM";
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
				document.productuomForm.reset();
				$('#prduomModal').modal('toggle');
				productuomCallback(data);	/* Define this method in parent page*/
			}						
		}
	});	
	return false;
}
function checkno1(){
	var baseamount = $("input[name=QPUOM]").val();
	var zeroval = "0";
	if(baseamount != ""){
		var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(baseamount.match(decimal) || baseamount.match(numbers)) 
		{ 
			$("input[name=QPUOM]").val(baseamount);
			if(baseamount == '1'){
				$(".squom").hide();
			}else{
				$(".squom").show();
			}
			
		}else{
			$("input[name=QPUOM]").val(zeroval);
			$(".squom").hide();
		}
	}else{
		$(".squom").hide();
		$("input[name=QPUOM]").val(zeroval);
	}
	
}
</script>