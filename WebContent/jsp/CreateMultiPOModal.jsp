<div id="GetMultiPoProductModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="MultiProductForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Add Item</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Product:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="ITEM_BARCODE" type="text" value="" readonly>
				        		<input type="hidden" name="LabelType" value="Single">
				        		<input type="hidden" name="BarcodeWidth" value="3">
				        		<input type="hidden" name="BarcodeHeight" value="60">
				        		<input type="hidden" name="FontSize" value="30">
				        		<input type="hidden" name="TextAlign" value="Center"> 
				        		<input type="hidden" name="TextPosition" value="Bottom"> 
				        		<input type="hidden" name="DisplayText" value="Show"> 
				        		<input type="hidden" name="PAGE_TYPE" value="PRDBARCODE">
				        		<input type="hidden" name="totrecqty" value="1"> 
				        		<input type="hidden" name="printdate" value=""> 
				        		<input type="hidden" name="CNAME" value=""> 
				        		
				        		<input type="hidden" name="POPLANT" value=""> 
				        		<input type="hidden" name="POITEM" value=""> 
				        		<input type="hidden" name="POSUP" value=""> 
				        		<input type="hidden" name="POCUR" value=""> 
				        		<input type="hidden" name="POCUREQ" value=""> 
				        		<input type="hidden" name="POUOM" value=""> 
				        		<input type="hidden" name="POCOST" value=""> 
				        		<input type="hidden" name="POUNITCOST" value=""> 
				        		<input type="hidden" name="POSUPID" value=""> 
				        		<input type="hidden" name="PONUMBEROFDECIMAL" value=""> 
				        		
					   		</div>
			        	</div>
		      		</div>
				</div>
		      	<div class="body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Product Description:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="ITEM_DESC_BARCODE" type="text" value="" readonly> 
					   		</div>
			        	</div>
		      		</div>
				</div>
		      	<div class="body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4">Supplier:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input type="hidden" name="POvendno" value="" >
				        		<input type = "hidden" name="POcustModal">
				    		 	<INPUT class=" form-control" id="POvendname" value="" name="POvendname" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select PO Estimate Order Supplier" onchange="checksupplier(this.value,this)">
				    			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'POvendname\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i> 
					   		</div>
			        	</div>
		      		</div>
				</div>
				
		      	<div class="body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Currency:</label>
		      			<div class="col-sm-3">
			        		<div class="input-group">
				        		<input name="CURRENCY"  type="text" onchange="checkcurrency(this.value,this)" class="form-control CURRENCYSEARCH"> 
					   		</div>
			        	</div>
		      			<div class="col-sm-3">
			        		<div class="input-group">
				        		<input type="text" name="CURRENCYUSEQT"  class="form-control" READONLY> 
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				      	<div class="body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">UOM:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input type="text" name="UOM" onchange="checkprduom(this.value,this)" class="form-control uomSearch" placeholder="UOM"> 
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				<div class="body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Quantity:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="POQTY" type="text"  value="1.000" onchange="calcamt()" onkeypress="return isNumberKey(event,this,4)">
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				<div class="body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Unit Cost:</label>
		      			<div class="col-sm-3">
			        		<div class="input-group">
				        		<input class="form-control" name="POPRICE" type="text" value="" onchange="calcamt()" onkeypress="return isNumberKey(event,this,4)">
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				<div class="body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Amount:</label>
		      			<div class="col-sm-3">
			        		<div class="input-group">
				        		<input class="form-control" name="POAMT" type="text" value="" readonly> 
					   		</div>
			        	</div>
		      		</div>
				</div>
				
				
				
				
		      	<div class="modal-body" style="display: none;">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Label Size:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="printtype" type="text" value="30X25" readonly> 
					   		</div>
			        	</div>
		      		</div>
				</div>
		      	<div class="modal-body" style="display: none;">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">List Price:</label>
		      			<div class="col-sm-3">
			        		<div class="input-group">
				        		<input class="form-control" name="SELLING_PRICE_BARCODE" type="text" value="$" onchange="ViewPrtBarcode()" > 
					   		</div>
			        	</div>
		      			<div class="col-sm-3">
			        		<div class="input-group">
				        		<input class="form-control" name="UNITPRICE_BARCODE" type="text" value="0" onchange="ViewPrtBarcode()" > 
					   		</div>
			        	</div>
		      		</div>
				</div>
		      	<div class="modal-body" style="display: none;">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Print Quantity:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="QUANTITY_BARCODE" type="text" onchange="ViewPrtBarcode()" value="1"> 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="onsave();">Save</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    <div class="modal-body" style="display: none">
			    <div class="row form-group">
  	<div class="col-sm-12" id="doubleup" style="display: none">  	
  	</div>
  	</div>
  	</div>
  	<div class="modal-body" style="display: none">
  	<div class="row form-group">
  	<div class="col-sm-12" id="singleup" style="display: none">
  	</div>
  	</div>
  	</div>
			</div>
			
		</form>
	</div>
</div>
<script>

var labelType =   [{
	    "year": "Single",
	    "value": "Single",
	    "tokens": [
	      "Single"
	    ]
	  },
			  {
				    "year": "Double",
				    "value": "Double",
				    "tokens": [
				      "Double"
				    ]
			  }];

var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    matches = [];
	    substrRegex = new RegExp(q, 'i');
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};

$(document).ready(function(){

	var plnt =$("input[name=POPLANT]").val();
	var pitem =$("input[name=POITEM]").val();

	$('#POvendnamea').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plnt,
				ACTION : "GET_SUPPLIER_DATA_ACTIVE",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
			return '<div onclick="document.MultiProductForm.POvendno.value = \''+data.VENDO+'\'"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
		    }
		  }
		}).on('typeahead:render',function(event,selection){
			
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.supplierAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.MultiProductForm.POvendno.value = "";
			}
		});


	$('#POvendname').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var item;
			var action = "GET_SUPPLIER_DATA_ACTIVE";
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plnt,
				ACTION : action,
				ITEM : pitem,
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.VENDMST);
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
				    return '<div><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
			
			
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:select',function(event,selection){
			loadVendData(this, selection.VENDO, selection.TAXTREATMENT,selection.CURRENCY,selection.CURRENCYID,selection.CURRENCYUSEQT);
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.MultiProductForm.POSUPID.value = "";
			}
			
		});

	$(".CURRENCYSEARCH").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CURRENCY',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					ACTION : "GET_CURRENCY_DATA",
					CURRENCY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.CURRENCYMST);
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
			return '<div onclick="setCurrencyid(this,\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')"><p>'+data.CURRENCY+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";	
			  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		
		}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			$("input[name ='CURRENCYID']").val("");	
	    });


	$('.uomSearch').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'UOM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
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
			return '<p class="item-suggestion">'+data.UOM+'</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:select',function(event,selection){
			CheckPriceVal(this,selection.UOM)
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});

	

    

	$(document).on("focusout","input[name ='LabelType']",function(){
			var LabelTypeval = $("input[name ='LabelType']").val();

		    var doubleElement = document.getElementById('doubleup');
		    var singleElement =document.getElementById('singleup');
			if(LabelTypeval=='Double') {
			    doubleElement.style.display='inline-block';
			    singleElement.style.display='none';
			    }
			    else {
			    doubleElement.style.display='none';
			    singleElement.style.display='inline-block';
			    }
		});

	/* To get the suggestion data for labelType */
	$("#LabelType").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'labelType',
	  display: 'value',  
	  source: substringMatcher(labelType),
	  limit: 9999,
	  templates: {
	  empty: [
		  '<div style="padding:3px 20px">',
			'No results found',
		  '</div>',
		].join('\n'),
		suggestion: function(data) {
		return '<p>' + data.value + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	});

		});

function setCurrencyid(obj,CURRENCY,CURRENCYID,CURRENCYUSEQT){	
	$("input[name=CURRENCYUSEQT]").val(CURRENCYUSEQT);

	var numberOfDecimal = $("input[name=PONUMBEROFDECIMAL]").val();
	var productId = $("input[name=POITEM]").val();
    var VENDNO = $("input[name=POSUPID]").val();

	 var urlStr = "/track/ItemMstServlet";
	 var discount;
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				CURRENCY : CURRENCYID,
				VENDNO : VENDNO,
				ACTION : "GET_PURCHASE_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
						
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							$("input[name=POPRICE]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
						}else{
							$("input[name=POPRICE]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
						}
						calcamt();
					} 
				}
			});
}

function loadVendData(obj,SName,TAXTREATMENT,sCURRENCY_ID,currencyID,Curramt){
	$("input[name=CURRENCY]").val(currencyID);
	$("input[name=CURRENCYUSEQT]").val(Curramt);
	$("input[name=POSUPID]").val(SName);

	var numberOfDecimal = $("input[name=PONUMBEROFDECIMAL]").val();

	var productId = $("input[name=POITEM]").val();
    var currencyID = $("input[name=CURRENCY]").val();

	 var urlStr = "/track/ItemMstServlet";
	 var discount;
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				CURRENCY : currencyID,
				VENDNO : SName,
				ACTION : "GET_PURCHASE_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
						
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							$("input[name=POPRICE]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
						}else{
							$("input[name=POPRICE]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
						}
							calcamt();
					} 
				}
			});
}


function calcamt() {
	var numberOfDecimal = $("input[name=PONUMBEROFDECIMAL]").val();
// 	var qty = parseFloat($("input[name=POQTY]").val()).toFixed(3);
// 	var cost = parseFloat($("input[name=POPRICE]").val()).toFixed(numberOfDecimal);
	var qty = $("input[name=POQTY]").val();
	if (!qty || isNaN(parseFloat(qty))) {qty = parseFloat(1).toFixed(3);} else {qty = parseFloat(qty).toFixed(3);}
	var cost = $("input[name=POPRICE]").val();
	if (!cost || isNaN(parseFloat(cost))) {cost = parseFloat(1).toFixed(numberOfDecimal);} else {cost = parseFloat(cost).toFixed(numberOfDecimal);}
	
// 	alert(cost);
	var amt;

	amt = parseFloat(qty*cost).toFixed(numberOfDecimal);
	$("input[name=POAMT]").val(amt);
	$("input[name=POQTY]").val(qty);
	$("input[name=POPRICE]").val(cost);
	$("input[name=POUNITCOST]").val(cost);
}


function CheckPriceVal(obj, uom) {
	var productId = $("input[name=POITEM]").val();
	var desc = "";
    var disc = 0;
    var numberOfDecimal = $("input[name=PONUMBEROFDECIMAL]").val();
    var currencyID = $("input[name=CURRENCY]").val();
    var currencyUSEQT = $("input[name=CURRENCYUSEQT]").val();
    var plant =$("input[name=POPLANT]").val();
    
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				DESC : desc,
				PLANT :plant,
				CURRENCY:currencyID,
	            UOM:uom,
	            TYPE:"Purchase",
	            DISC:disc,
	            MINPRICE:1,
				ACTION : "VALIDATE_PRODUCT_UOM_CURRENCY_PURCHASE"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;						
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							$("input[name=POPRICE]").val(parseFloat("0.00000").toFixed(numberOfDecimal));
						}else{
							$("input[name=POPRICE]").val(parseFloat(resultVal.ConvertedUnitCost).toFixed(numberOfDecimal));
						}
					} else {
						
					}
		calcamt();
				}
			});
}

function checksupplier(supplier,obj){	
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			CUST_CODE : supplier,
			ACTION : "SUPPLIER_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Supplier Does't Exist");
					$(obj).typeahead('val', '');
					$("input[name ='POvendname']").focus();
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}

function checkcurrency(currency,obj){	
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			CURRENCYID : currency,
			ACTION : "CURRENCY_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Currency Does't Exists");
					$(obj).typeahead('val', '');
					$("input[name=CURRENCY]").val();
					$("input[name ='CURRENCY']").focus();
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}

function checkprduom(uom,obj){	
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			UOM : uom,
			ACTION : "UOM_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Uom Does't Exists");
					$(obj).typeahead('val', '');
					$("input[name ='UOM']").focus();
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}


function onsave() {
	var CURRENCY   = document.MultiProductForm.CURRENCY.value;
	var POQTY   = document.MultiProductForm.POQTY.value;
	var POPRICE   = document.MultiProductForm.POPRICE.value;
	
	if(CURRENCY == "" || CURRENCY == null) {alert("Please Select Currency");;document.MultiProductForm.CURRENCY.focus(); return false; }
	if(POQTY == "" || POQTY == null || POQTY == "0.000") {alert("Please Enter QTY");;document.MultiProductForm.POQTY.focus(); return false; }
	if(POPRICE == "" || POPRICE == null || POPRICE == "0.000") {alert("Please Enter Unit Cost");;document.MultiProductForm.POPRICE.focus(); return false; }
	
	document.MultiProductForm.action="/track/DynamicFileServlet?action=SavemultiPO&Submit="+document.MultiProductForm.ITEM_BARCODE.value;
	document.MultiProductForm.submit();
}

</script>