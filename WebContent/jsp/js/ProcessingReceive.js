$(document).ready(function(){
	var plant = document.form.plant.value;
	document.getElementById("divProcessing").style.display = "none"; 
	$(document).on("focusout","input[name ='ITEM']",function(){
		var product = document.form.ITEM.value;
		if(product.length>0)
		onGo(1);
	});
	

	$('#LOC_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCATION_DATA",
				LOCATION : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCMST);
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
			return '<div onclick="GetLoc(\''+data.LOCDESC+'\',\''+data.LOC_TYPE_ID+'\',\''+data.LOC_TYPE_ID2+'\')"><p class="item-suggestion">'+data.LOC+'</p></div>';
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
		}).on('typeahead:change',function(event,selection){
			if($(this).val() != "")
				{
				$("#BATCH_1").typeahead('val', '"');
				$("#BATCH_1").typeahead('val', '');
				}	
		});
	
	/* location Auto Suggestion */
	$('#LOC_TYPE_ID1').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOCTYPE_MST);
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
			return '<div onclick="document.form.LOC_TYPE_DESC.value = \''+data.LOC_TYPE_DESC+'\'"><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
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
	
	/* Product Number Auto Suggestion */
    $('#ITEM').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PROCESSING_BOM_PRODUCT_LIST_FOR_SUGGESTION",
				TYPE : "KITBOM",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
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
				return '<div onclick="loadItemData(this,\''+ data.PURCHASEUOM+'\',\''+ data.COST+'\',\''+ data.PQPUOM+'\',\''+ data.STKUOMQTY+'\',\''+ data.ITEMDESC+'\')"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.STKUOMQTY+' </p></div>';
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
		}).on('typeahead:change',function(event,selection){
			if($(this).val() != "")
				onGo(0);
			$("#CITEM").typeahead('val', '"');
			$("#CITEM").typeahead('val', '');
		});
    
    
	$('#BATCH_0').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'BATCH',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_KITING_BATCH_DATA",
				ITEM : document.form.ITEM.value,
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.batches);
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
				return '<div onclick="document.form.PARENTQTY.value = \''+data.QTY+'\'"><p class="item-suggestion">'+data.BATCH+'</p><p class="item-suggestion pull-right">Quantity: '+data.QTY+'</p></div>';
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
	addRow();
	//addSuggestionToTable();
});


function GetLoc(LOCDESC,LOC_TYPE_ID,LOC_TYPE_ID2){
document.form.LOC_DESC.value = LOCDESC;
document.form.LOC_TYPE_ID.value = LOC_TYPE_ID;
document.form.LOC_TYPE_ID2.value = LOC_TYPE_ID2;
}

function pqtychange(){
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var STKUOMQTY = parseFloat($("input[name=STKUOMQTY]").val()).toFixed(3);
	var pcost = document.form.CALCOST.value;
	var sumqty = document.form.SUMCQTY.value;
	var pqty = parseFloat(document.form.PARENTQTY.value).toFixed(3);
	var isItemValid = true;
	$("input[name ='citem']").each(function() {
	    if($(this).val() == ""){	    	
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("Add Child Product");
		return false;
	}
	if(parseFloat(pqty)>parseFloat(STKUOMQTY))
	{
		alert("Processing Qty is greater then inventory qty !!!");		
		document.getElementById("PARENTQTY").focus();
		return false;
	}
	var cost = pcost*pqty;
	var noncost = pcost;
	cost = parseFloat(cost).toFixed(numberOfDecimal);
	document.form.COST.value=cost;
	//onGo(1);
	
	$('.po-table tbody tr').each(function () {
		var qty = parseFloat($(this).closest('tr').find('input[name=QTY]').val()).toFixed(3);
		var uomqty = parseFloat($(this).closest('tr').find('input[name=uomqty]').val()).toFixed(3);
		var isnonstk = ($(this).closest('tr').find('input[name=isnonstock]').val());
	var tranqty = pqty*qty;
	//var transprice = parseFloat(qty)*(cost / parseFloat(sumqty));
	var transprice = (noncost / parseFloat(sumqty));
	if(isnonstk=="Y")
	transprice =0;
	$(this).closest('tr').find('input[name=TRANQTY]').val(parseFloat(tranqty).toFixed(3));
	$(this).closest('tr').find('input[name=amount]').val(parseFloat(transprice*uomqty).toFixed(numberOfDecimal));	
	$(this).closest('tr').find('input[name=totamount]').val(parseFloat(transprice*uomqty*tranqty).toFixed(numberOfDecimal));	
	$(this).closest('tr').find('input[name=unitpricerd]').val(transprice);	
		});
}


function loadItemData(obj,PURCHASEUOM,cost,PQPUOM,STKUOMQTY,ITEMDESC)
{
	var numberOfDecimal = $("#numberOfDecimal").val();
	document.form.PARENTUOM.value = PURCHASEUOM;
	document.form.PARENTQPUOM.value = PQPUOM;
	document.form.STKUOMQTY.value = STKUOMQTY;
	document.form.ITEM_DESC.value = ITEMDESC;
	cost = parseFloat(cost).toFixed(numberOfDecimal);
	document.form.COST.value = cost;
	document.form.PCOST.value = cost;
	document.form.CALCOST.value = cost;
}

function addSuggestionToTable(){
	var plant = document.form.plant.value;
	/* To get the suggestion data for Product */
	$(".itemSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
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
				return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' '+data.UOM+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			//menuElement.after( '<div class="itemAddBtn footer"  data-toggle="modal" data-target="#productModal"><a href="#"> + Add New Product</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".bigdrop").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			menuElement.next().show();
		}).on('typeahead:select',function(event,selection){
			loadChItemData(this, selection.ITEMDESC,selection.CATLOGPATH,selection.PRD_DEPT_ID,selection.COST,selection.PURCHASEUOM,selection.NONSTKFLAG,selection.PURCHASEUOMQTY);
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
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
	
	
}

function removeSuggestionToTable(){
	$(".itemSearch").typeahead('destroy');
	$(".uomSearch").typeahead('destroy');
}


function addRow(){
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var COMP_INDUSTRY = $("input[name=COMP_INDUSTRY]").val();
	var szero = addZeroes(parseFloat(0).toFixed(numberOfDecimal));
	var body="";
	body += '<tr>';
	body += '<td class="item-img text-center">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;"><input type="hidden" name="basecost" value="0.00">';
	body += '<input type="hidden" name="lnno" value="1">';
	body += '<input type="hidden" name="itemdesc" value="">';
	body += '<input type="hidden" name="uomqty" value="">';
	body += '<input type="hidden" name="isnonstock" value="">';
	body += '<input type="hidden" name="unitpricerd" value="">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="citem" class="form-control itemSearch" style="width:87%" placeholder="Type or click to select an item.">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="UOM" class="form-control uomSearch" placeholder="UOM">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="loc" class="form-control locSearch" placeholder="Location" READONLY>';
	body += '</td>';
	if(COMP_INDUSTRY=="Centralised Kitchen") {
		body += '<td class="bill-item">';
	body += '<input type="text" name="batch" class="form-control batchSearch" placeholder="Batch" value="NOBATCH" READONLY>';
	body += '</td>';
		}else {
	body += '<td class="bill-item"><div class="input-group">';
	body += '<input type="text" name="batch" value="NOBATCH" class="form-control batchSearch" placeholder="Batch">';
	body += '<span class="input-group-addon" onclick="javascript:generateBatch(this);return false;" id="actionBatch" name="actionBatch">';
	body += '<a href="#" data-toggle="tooltip" data-placement="top" title="Generate">';
	body += '<i class="glyphicon glyphicon-edit"></i></a></span>';
	body += '</div></td>';
	}
	body += '<td class="item-qty text-right"><input type="text" name="QTY" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="1.000" onchange="calculateAmount(this)"></td>';
	body += '<td class="item-qty text-right">';
	body += '<input type="text" name="TRANQTY" class="form-control text-right" value="1.000" READONLY>';
	body += '</td>';
	body += '<td class="item-amount text-right">';
	body += '<input name="amount" type="text" class="form-control text-right" value="0.00" readonly="readonly" style="display:inline-block;">';
	body += '</td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" onclick="javascript:removeRow(this);return false;"></span>';
	body += '<input name="totamount" type="text" class="form-control text-right" value="0.00" readonly="readonly" style="display:inline-block;">';
	body += '</td>';
	body += '</tr>';
	$(".po-table tbody").append(body);
	removeSuggestionToTable();
	addSuggestionToTable();

	setLineNo();
	
	$('input,textarea').on('keypress', function (e) {
	    var ingnore_key_codes = [39, 91];
	  
	    if ($.inArray(e.which, ingnore_key_codes) >= 0) {
	        e.preventDefault();
			alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	       
	    } else {
	      
	    }
	}).on("paste",function(e){
		var textboxvalue = this;
	   setTimeout(function(){
	    var sValue = $(textboxvalue).val();
	    convertCharToString3(sValue); 
	 },100);
	 
	});
}

function removeRow(obj){
	$(obj).parent().parent().remove();
	    setLineNo();
	    calculateAmount(obj);
}
function setLineNo(){
	var i=1;
	$(".po-table tbody tr td:first-child").each(function() {
		$(this).find('input[name=lnno]').val(i);
		i++;
	});
}

function calculateAmount(obj){
	
	var pqty = parseFloat($("input[name=PARENTQTY]").val()).toFixed(3);
	var qty = parseFloat($(obj).closest('tr').find('input[name=QTY]').val()).toFixed(3);
	var tranqty = pqty*qty;
	$(obj).closest('tr').find('input[name=TRANQTY]').val(parseFloat(tranqty).toFixed(3));
	getcost();
	pqtychange();
	}
	
function loadChItemData(obj, itemdesc, catalogPath, loc, cost, purchaseuom,isnonstock,uomqty){
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var pcost = document.form.PCOST.value;
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogPath);
	$(obj).closest('tr').find('input[name = "itemdesc"]').val(itemdesc);
	$(obj).closest('tr').find('input[name = "loc"]').val(loc);
	$(obj).closest('tr').find('input[name = "uomqty"]').val(uomqty);
	$(obj).closest('tr').find("input[name=UOM]").val(purchaseuom);
	$(obj).closest('tr').find("input[name=isnonstock]").val(isnonstock);
	if(isnonstock=="Y")
	{
	$(obj).closest('tr').find("input[name=basecost]").val(cost);
	var qcost = parseFloat(pcost)+parseFloat(cost);
	document.form.COST.value=parseFloat(qcost*1).toFixed(numberOfDecimal);
	document.form.CALCOST.value=parseFloat(qcost).toFixed(numberOfDecimal);
	}
	else
	$(obj).closest('tr').find("input[name=basecost]").val(0);
	
	calculateAmount(obj);
	}
	
	function CheckPriceVal(obj, uom) {
	var productId = $(obj).closest('tr').find("td:nth-child(2)").find('input[name=item]').val();
	}
	
	function generateBatch(obj){
	
	var urlStr = "/track/InboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : document.form.plant.value,
		ACTION : "GENERATE_BATCH"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				$(obj).closest('tr').find('input[name=batch]').val(resultVal.batchCode);
			} else {
				alert("Unable to genarate Batch");
				$(obj).closest('tr').find('input[name=batch]').val("NOBATCH");
			}
		}
	});
}

function changeitem(obj){
	var obj2 = $(obj).parent().find('input[name="item"]');
	$(obj2).typeahead('val', '');
	$(obj).parent().find('input[name="item"]').focus();
}

function chkProcessing()
	    {
		var numberOfDecimal = $("input[name=numberOfDecimal]").val();
      var val = 0;
	  for( i = 0; i < document.form.Processing.length; i++ )
	  {
		  if( document.form.Processing[i].checked == true )
		  {
			  val = document.form.Processing[i].value;
			  if(val=='1')
			  {
				  document.getElementById("divProcessing").style.display = "none";
				  onGo(1);  
			  }else{
                document.getElementById("divProcessing").style.display = "block";
                document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</tbody></TABLE>';
                addRow();
                var pcost = document.form.PCOST.value;
				var pqty = document.form.PARENTQTY.value;
				var cost = pcost*pqty;
				document.form.COST.value=parseFloat(cost).toFixed(numberOfDecimal);
				document.form.CALCOST.value=parseFloat(cost).toFixed(numberOfDecimal);
				document.form.SUMCQTY.value=parseFloat(0).toFixed(3);
           }
			  
			
		  }
		  
	  }
    
	}
	
	function getcost(){
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var pcost = document.form.PCOST.value;
	var pqty = document.form.PARENTQTY.value;
	var cost = pcost;
	var sumqty=0,sumuomqty=0;
	cost = parseFloat(cost).toFixed(numberOfDecimal);
	$('.po-table tbody tr').each(function () {
		var nonstkcost = parseFloat($(this).closest('tr').find('input[name=basecost]').val()).toFixed(numberOfDecimal);
		var qty = parseFloat($(this).closest('tr').find('input[name=QTY]').val()).toFixed(3);
		var uomqty = parseFloat($(this).closest('tr').find('input[name=uomqty]').val()).toFixed(3);
		var isnonstk = ($(this).closest('tr').find('input[name=isnonstock]').val());
		if(isnonstk=="N") {
		sumqty=parseFloat(sumqty)+parseFloat(qty);
		sumuomqty=parseFloat(sumuomqty)+(parseFloat(qty)*parseFloat(uomqty));
		}
		else
		nonstkcost=parseFloat(nonstkcost)*parseFloat(qty);
		cost=parseFloat(cost)+parseFloat(nonstkcost);
		});
	document.form.COST.value=parseFloat(cost*pqty).toFixed(numberOfDecimal);
	document.form.CALCOST.value=parseFloat(cost).toFixed(numberOfDecimal);
	document.form.SUMCQTY.value=parseFloat(sumuomqty).toFixed(3);
}
function getWastageCost(){
	var numberOfDecimal = $("input[name=numberOfDecimal]").val();
	var pcost = document.form.PCOST.value;
	var pqty = document.form.PARENTQTY.value;
	var puomqty = document.form.PARENTQPUOM.value;
	var wqty = document.form.WASTAGE.value;
	var cost=parseFloat(pcost)/parseFloat(puomqty);
	cost = parseFloat(cost)*pqty;
	if(parseFloat(cost*wqty)>pcost)
	{
	alert("Wastage is greater then processing qty");
	document.form.WASTAGE.value=0;
	document.form.WASTAGECOST.value=parseFloat(0).toFixed(numberOfDecimal);
	document.form.WASTAGE.focus();
		return false;	
	}
	document.form.WASTAGECOST.value=parseFloat(cost*wqty).toFixed(numberOfDecimal);
	}
	function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}