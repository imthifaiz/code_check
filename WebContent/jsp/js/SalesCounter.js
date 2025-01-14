$(document).ready(function(){
	var plant = document.form1.plant.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	
	$('#item').typeahead({
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
				//ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
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
				return '<div onclick="loadItemData(this,\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' '+data.UOM+'</p></div>';
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
	
	$('#LOC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
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
			return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
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
	
//	$('#LOC_TYPE_ID').typeahead({
//		  hint: true,
//		  minLength:0,  
//		  searchOnFocus: true
//		},
//		{
//		  display: 'LOC_TYPE_ID',  
//		  source: function (query, process,asyncProcess) {
//			var urlStr = "/track/MasterServlet";
//			$.ajax( {
//			type : "POST",
//			url : urlStr,
//			async : true,
//			data : {
//				PLANT : plant,
//				ACTION : "GET_LOCATION_TYPE_DATA",
//				LOCATION : query
//			},
//			dataType : "json",
//			success : function(data) {
//				return asyncProcess(data.LOCTYPEMST);
//			}
//				});
//		},
//		  limit: 9999,
//		  templates: {
//		  empty: [
//			  '<div style="padding:3px 20px">',
//				'No results found',
//			  '</div>',
//			].join('\n'),
//			suggestion: function(data) {
//			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
//			}
//		  }
//		}).on('typeahead:open',function(event,selection){
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",true);
//			element.toggleClass("glyphicon-menu-down",false);
//		}).on('typeahead:close',function(){
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",false);
//			element.toggleClass("glyphicon-menu-down",true);
//		});
	
	/* location Auto Suggestion */
	$('#LOC_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCATIONTYPE_SUMMARY",
				LOCATIONTYPE : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTOMERTYPELIST);
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID+'</p></div>';
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
	
	
	
	$('#LOC_TYPE_ID2').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID2',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETWO_LIST_FOR_SUGGESTION",
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID2+'</p></div>';
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
	
	  /* location three Auto Suggestion */
	$('#LOC_TYPE_ID3').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC_TYPE_ID3',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOCTYPETHREE_LIST_FOR_SUGGESTION",
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
			return '<div><p class="item-suggestion">'+data.LOC_TYPE_ID3+'</p></div>';
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
	
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CUSTNO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_CUSTOMER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTMST);
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
//			return '<div><p class="item-suggestion" onclick="document.form1.CUSTNO.value = \''+data.CUSTNO+'\'">'+data.CNAME+'</p></div>';
			return '<div><p class="item-suggestion" onclick="document.form1.CUSTNO.value = \''+data.CUSTNO+'\'"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
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
		}).on('typeahead:select',function(event,selection){
			document.form1.CNAME.value=selection.CNAME;
			document.form1.TAXTREATMENT.value=selection.TAXTREATMENT;
			document.form1.NAME.value=selection.NAME;
			document.form1.CUSTOMER_TYPE_ID.value=selection.CUSTOMER_TYPE_ID;
			document.form1.CUSTNO.value=selection.CNAME;
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form1.CUSTNO.value = "";
			}
		});
	
	$('#UOM').typeahead({
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
	
	$('#MODEL').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'MODEL',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_ITEM_MODEL_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.MODELS);
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
			return '<div><p class="item-suggestion">'+data.MODEL+'</p></div>';
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



function onAddList(){
	var productId=[],productDesc=[],catalog=[],brand=[],model=[],location=[],
	unitPrice=[],invUom=[],invQty=[],avlQty=[],ordUom=[],nonStkFlag=[],checkFound=false;
	$("#tableInventorySummary > tbody > tr").each(function(index, tr) {
		if($(tr).find("td:nth-child(1)").find('input').is(":checked")){
			productId.push($(tr).find("td:nth-child(2)").text());
			productDesc.push($(tr).find("td:nth-child(3)").text());
			catalog.push($(tr).find("td:nth-child(4)").html());
			brand.push($(tr).find("td:nth-child(5)").text());
			model.push($(tr).find("td:nth-child(6)").text());
			location.push($(tr).find("td:nth-child(7)").text());
			unitPrice.push($(tr).find("td:nth-child(8)").text());
			invUom.push($(tr).find("td:nth-child(12)").text());
			invQty.push($(tr).find("td:nth-child(13)").text());
			avlQty.push($(tr).find("td:nth-child(14)").text());
			ordUom.push($(tr).find("td:nth-child(1)").find('input[name="SALESUOM"]').val());
			nonStkFlag.push($(tr).find("td:nth-child(1)").find('input[name="NONSTKFLAG"]').val());
			checkFound = true;
		}
	});
	if(!checkFound){
		alert("Please Check at least one checkbox.");
		return false;
	}
	$.ajax( {
		type : "POST",
		url : "../InvMstServlet",
		data : {
			PRODUCTID : productId,
			PRODUCTDESC : productDesc,
			CATALOG : catalog,
			BRAND : brand,
			MODEL : model,
			LOCATION : location,
			UNITPRICE : unitPrice,
			INVUOM : invUom,
			INVQTY : invQty,
			AVLQTY : avlQty,
			ORDUOM : ordUom,
			NONSTKFLAG : nonStkFlag,
			ACTION : "ADD_ESTIMATE_LIST",
		},
		dataType : "json",
		success : function(data) {
			var result="";
			for(var i = 0; i < data.estList.length; i ++){
				result += "<tr>";
				result += "<td>"
						+'<input type="checkbox" style="border:0;" name="chkdEstList">'
						+'<input type="hidden" name="listId" value="'+i+'">'
						+'<input type="hidden" name="nonStkFlag" value="'+data.estList[i]["NONSTKFLAG"]+'">'
						+"</td>";
				result += "<td>"+data.estList[i]["PRODUCT"]+"</td>";
				result += "<td>"+data.estList[i]["PRODUCTDESC"]+"</td>";
				result += "<td class='item-img'>"+data.estList[i]["CATALOG"]+"</td>";
				result += "<td>"+data.estList[i]["BRAND"]+"</td>";
				result += "<td>"+data.estList[i]["MODEL"]+"</td>";
				result += "<td>"+data.estList[i]["LOCATION"]+"</td>";
				result += "<td><input type='text' class='form-control input-sm' name='UNITPRICE' onchange='validateUp(this)' onkeypress='return isNumberKey(event,this,4)' value='"+data.estList[i]["UNITPRICE"]+"'></td>";
				result += "<td>"+data.estList[i]["INVUOM"]+"</td>";
				result += "<td>"+data.estList[i]["INVQTY"]+"</td>";
				result += "<td>"+data.estList[i]["AVLQTY"]+"</td>";
				var uomStr="";
				uomStr += "<select class='form-control input-sm' name='SALESUOM' onchange='CheckPriceVal(this.value,\""+data.estList[i]["PRODUCT"]+"\",\""+data.estList[i]["LOCATION"]+"\",this)'>";
				for(var j = 0; j < data.uomList.length; j ++){
					if(data.uomList[j]["UOM"] == data.estList[i]["ORDUOM"]){
						uomStr += "<option value='"+ data.uomList[j]["UOM"] +"' selected>"+ data.uomList[j]["UOM"] +"</option>";
					}else{
						uomStr += "<option value='"+ data.uomList[j]["UOM"] +"'>"+ data.uomList[j]["UOM"] +"</option>";
					}
					
				}
				uomStr += "</select>";
				result += "<td>"+uomStr+"</td>";
				result += "<td style='position:relative;'>"+'<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" onclick="deleteList('+i+')"></span><input type="text" class="form-control input-sm" onchange="validateOrderQty(this)" onkeypress="return isNumberKey(event,this,4)" name="SALESUOMQTY" value="0.000">'+"</td>";
				result += "</tr>";
			}
			$("#estListSummary > tbody").html(result);
			$("#estListSummary").show();
			$('input[type="checkbox"]').prop('checked', false);
		}
	});
}

function CheckPriceVal(uom,item,loc,obj) {
	var urlStr = "/track/MiscOrderHandlingServlet";
	var plant = document.form1.plant.value;
	var numberofdecimal = document.form1.numberOfDecimal.value;
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ITEM : item,
			DESC : "",
			PLANT : "plant",
			PONO:"",
            UOM:uom,
            TYPE:"SalesEstimate",
            DISC:0,
            MINPRICE:0,
			ACTION : "VALIDATE_PRODUCT_UOM"
		},
		dataType : "json",
		success : function(data) {				
			if (data.status == "100") {
				var resultVal = data.result;
				var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
				if(resultVal.unitcost == null || resultVal.unitcost == undefined || resultVal.unitcost == 0){
					$(obj).parent().parent().find("td:nth-child(8)").find('input').val(parseFloat("0.00000").toFixed(numberofdecimal))
				}else{
					$(obj).parent().parent().find("td:nth-child(8)").find('input').val(parseFloat(resultVal.unitcost).toFixed(numberofdecimal))
				}
			}
			getAvailableQuantity(uom,item,loc,obj)
		}
		});
}

function deleteList(id){
	$.ajax( {
		type : "POST",
		url : "../InvMstServlet",
		data : {
			ID:id,
			ACTION : "DELETE_ESTIMATE_LIST",
		},
		dataType : "json",
		success : function(data) {
			var result="";
			if(data.estList != undefined){
				for(var i = 0; i < data.estList.length; i ++){
					result += "<tr>";
					result += "<td>"+'<input type="checkbox" style="border:0;" name="chkdEstList">'
							+'<input type="hidden" name="listId" value="'+i+'">'
							+'<input type="hidden" name="nonStkFlag" value="'+data.estList[i]["NONSTKFLAG"]+'">'
							+"</td>";
					result += "<td>"+data.estList[i]["PRODUCT"]+"</td>";
					result += "<td>"+data.estList[i]["PRODUCTDESC"]+"</td>";
					result += "<td class='item-img'>"+data.estList[i]["CATALOG"]+"</td>";
					result += "<td>"+data.estList[i]["BRAND"]+"</td>";
					result += "<td>"+data.estList[i]["MODEL"]+"</td>";
					result += "<td>"+data.estList[i]["LOCATION"]+"</td>";
					result += "<td><input type='text' class='form-control input-sm' name='UNITPRICE' onchange='validateUp(this)' onkeypress='return isNumberKey(event,this,4)' value='"+data.estList[i]["UNITPRICE"]+"'></td>";
					result += "<td>"+data.estList[i]["INVUOM"]+"</td>";
					result += "<td>"+data.estList[i]["INVQTY"]+"</td>";
					result += "<td>"+data.estList[i]["AVLQTY"]+"</td>";
					var uomStr="";
					uomStr += "<select class='form-control input-sm' name='SALESUOM' onchange='CheckPriceVal(this.value,\""+data.estList[i]["PRODUCT"]+"\",\""+data.estList[i]["LOCATION"]+"\",this)'>";
					for(var j = 0; j < data.uomList.length; j ++){
						if(data.uomList[j]["UOM"] == data.estList[i]["ORDUOM"]){
							uomStr += "<option value='"+ data.uomList[j]["UOM"] +"' selected>"+ data.uomList[j]["UOM"] +"</option>";
						}else{
							uomStr += "<option value='"+ data.uomList[j]["UOM"] +"'>"+ data.uomList[j]["UOM"] +"</option>";
						}					
					}
					uomStr += "</select>";
					result += "<td>"+uomStr+"</td>";
					result += "<td style='position:relative;'>"+'<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" onclick="deleteList('+i+')"></span><input type="text" class="form-control input-sm" onchange="validateOrderQty(this)" onkeypress="return isNumberKey(event,this,4)" name="SALESUOMQTY" value="0.000">'+"</td>";
					result += "</tr>";
				}
			}			
			$("#estListSummary > tbody").html(result);
			$("#estListSummary").show();
			$('input[type="checkbox"]').prop('checked', false);
		}
	});
}

function validateUp(obj){
	var numberofdecimal = document.form1.numberOfDecimal.value;
	var value = $(obj).val();
	var patt = new RegExp(/^[+-]?\d+(\.\d+)?$/);
	var res = patt.test(value);
	if(!res){
		alert("Please enter correct value for Unit Price");
		$(obj).val(parseFloat(0).toFixed(numberofdecimal));
	}else if($(obj).val() < 0){
		alert("Unit Price value cannot be less than 0");
		$(obj).val(parseFloat(0).toFixed(numberofdecimal));
	}else{
		$(obj).val(parseFloat(value).toFixed(numberofdecimal));
	}
}

function validateOrderQty(obj){
	var numberofdecimal = document.form1.numberOfDecimal.value;
	var nonStkFlag = $(obj).closest('tr').find("input[name=nonStkFlag]").val();
	var value = $(obj).val();
	var patt = new RegExp(/^[+-]?\d+(\.\d+)?$/);
	var res = patt.test(value);
	if(!res){
		alert("Please enter correct value for Order Qty");
		$(obj).val("0.000");
	}else if(($(obj).val() < 0) && (nonStkFlag != "Y")){
		alert("Order Qty value cannot be less than or equal 0");
		$(obj).val("0.000");
	}else{
		$(obj).val(parseFloat(value).toFixed(3));
	}
}

function convertToEstimate(){
	var checkFound=false, zeroQty = false;
	var formdata = "";
	$("#estListSummary > tbody > tr").each(function(index, tr) {
		if($(tr).find("td:nth-child(1)").find('input').is(":checked")){
			formdata += "<input type='text' name='CUSTNO' value='"+document.form1.CUSTOMER.value+"'>";
			formdata += "<input type='text' name='CNAME' value='"+document.form1.CNAME.value+"'>";
			formdata += "<input type='text' name='TAXTREATMENT' value='"+document.form1.TAXTREATMENT.value+"'>";
			formdata += "<input type='text' name='NAME' value='"+document.form1.NAME.value+"'>";
			formdata += "<input type='text' name='CUSTOMER_TYPE_ID' value='"+document.form1.CUSTOMER_TYPE_ID.value+"'>";
			formdata += "<input type='text' name='ITEM' value='"+document.form1.ITEM.value+"'>";
			formdata += "<input type='text' name='LOC' value='"+document.form1.LOC.value+"'>";
			formdata += "<input type='text' name='LOC_TYPE_ID' value='"+document.form1.LOC_TYPE_ID.value+"'>";
			formdata += "<input type='text' name='LOC_TYPE_ID2' value='"+document.form1.LOC_TYPE_ID2.value+"'>";
			formdata += "<input type='text' name='MODEL' value='"+document.form1.MODEL.value+"'>";
			formdata += "<input type='text' name='FUOM' value='"+document.form1.UOM.value+"'>";
			formdata += "<input type='text' name='ID' value='"+index+"'>";
			formdata += "<input type='text' name='UNITPRICE' value='"+$(tr).find("td:nth-child(8)").find('input').val()+"'>";
			formdata += "<input type='text' name='UOM' value='"+$(tr).find("td:nth-child(12)").find('select').val()+"'>";
			formdata += "<input type='text' name='ORDERPRICE' value='"+$(tr).find("td:nth-child(13)").find('input').val()+"'>";
			
			checkFound = true;
			if($(tr).find("td:nth-child(13)").find('input').val() == 0 || 
					$(tr).find("td:nth-child(13)").find('input').val() == ""){
				zeroQty = true;
			}
		}
	});
	if(!checkFound){
		alert("Please Check at least one checkbox.");
		return false;
	}else if(zeroQty){
		alert("Please Enter Order Qty");
		return false;
	}else{
		$("#convertData").html('');
		$("#convertData").html(formdata);
		/*document.convertDataForm.action='/track/EstimateServlet?Submit=Copy EST';*/
		document.convertDataForm.action="../salesestimate/convertestimate";
		document.convertDataForm.submit();
	}
}
function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}
function convertToInvoice(){
	var checkFound=false, zeroQty = false, orderLimit = false;
	var formdata = "";
	$("#estListSummary > tbody > tr").each(function(index, tr) {
		if($(tr).find("td:nth-child(1)").find('input').is(":checked")){
			formdata += "<input type='text' name='CUSTNO' value='"+document.form1.CUSTOMER.value+"'>";
			formdata += "<input type='text' name='CNAME' value='"+document.form1.CNAME.value+"'>";
			formdata += "<input type='text' name='TAXTREATMENT' value='"+document.form1.TAXTREATMENT.value+"'>";
			formdata += "<input type='text' name='NAME' value='"+document.form1.NAME.value+"'>";
			formdata += "<input type='text' name='CUSTOMER_TYPE_ID' value='"+document.form1.CUSTOMER_TYPE_ID.value+"'>";
			formdata += "<input type='text' name='ITEM' value='"+document.form1.ITEM.value+"'>";
			formdata += "<input type='text' name='LOC' value='"+document.form1.LOC.value+"'>";
			formdata += "<input type='text' name='LOC_TYPE_ID' value='"+document.form1.LOC_TYPE_ID.value+"'>";
			formdata += "<input type='text' name='LOC_TYPE_ID2' value='"+document.form1.LOC_TYPE_ID2.value+"'>";
			formdata += "<input type='text' name='MODEL' value='"+document.form1.MODEL.value+"'>";
			formdata += "<input type='text' name='FUOM' value='"+document.form1.UOM.value+"'>";
			formdata += "<input type='text' name='ID' value='"+index+"'>";
			formdata += "<input type='text' name='UNITPRICE' value='"+$(tr).find("td:nth-child(8)").find('input').val()+"'>";
			formdata += "<input type='text' name='UOM' value='"+$(tr).find("td:nth-child(12)").find('select').val()+"'>";
			formdata += "<input type='text' name='ORDERPRICE' value='"+$(tr).find("td:nth-child(13)").find('input').val()+"'>";
			
			checkFound = true;
			if($(tr).find("td:nth-child(13)").find('input').val() == 0 || 
					$(tr).find("td:nth-child(13)").find('input').val() == ""){
				zeroQty = true;
			}else{
				var nonStkFlag = $(tr).find("input[name=nonStkFlag]").val();
				if(nonStkFlag != "Y"){
					var ordQty = parseFloat($(tr).find("td:nth-child(13)").find('input').val());
					var avlQty = parseFloat($(tr).find("td:nth-child(11)").html());
					
					if(ordQty > avlQty){
						orderLimit = true;
					}
				}
			}
		}
	});
	if(!checkFound){
		alert("Please Check at least one checkbox.");
		return false;
	}else if(zeroQty){
		alert("Please Enter Order Qty");
		return false;
	}else if(orderLimit){
		alert("Order Qty should not exceed Available Qty.");
		return false;
	}else{
		$("#convertData").html('');
		$("#convertData").html(formdata);
		document.convertDataForm.action='/track/EstimateServlet?Submit=Copy Invoice';
		document.convertDataForm.submit();
	}
}

function loadEstList(){
	var productId=[],productDesc=[],catalog=[],brand=[],model=[],location=[],
	unitPrice=[],invUom=[],invQty=[],avlQty=[],ordUom=[], checkFound=false;
	$.ajax( {
		type : "POST",
		url : "../InvMstServlet",
		data : {
			PRODUCTID : productId,
			PRODUCTDESC : productDesc,
			CATALOG : catalog,
			BRAND : brand,
			MODEL : model,
			LOCATION : location,
			UNITPRICE : unitPrice,
			INVUOM : invUom,
			INVQTY : invQty,
			AVLQTY : avlQty,
			ORDUOM : ordUom,
			ACTION : "ADD_ESTIMATE_LIST",
		},
		dataType : "json",
		success : function(data) {
			var result="";
			if(data.estList != undefined){
			for(var i = 0; i < data.estList.length; i ++){
				result += "<tr>";
				result += "<td>"+'<input type="checkbox" style="border:0;" name="chkdEstList">'
						+'<input type="hidden" name="listId" value="'+i+'">'
						+'<input type="hidden" name="nonStkFlag" value="'+data.estList[i]["NONSTKFLAG"]+'">'
						+"</td>";
				result += "<td>"+data.estList[i]["PRODUCT"]+"</td>";
				result += "<td>"+data.estList[i]["PRODUCTDESC"]+"</td>";
				result += "<td class='item-img'>"+data.estList[i]["CATALOG"]+"</td>";
				result += "<td>"+data.estList[i]["BRAND"]+"</td>";
				result += "<td>"+data.estList[i]["MODEL"]+"</td>";
				result += "<td>"+data.estList[i]["LOCATION"]+"</td>";
				result += "<td><input type='text' class='form-control input-sm' name='UNITPRICE' onchange='validateUp(this)' onkeypress='return isNumberKey(event,this,4)' value='"+data.estList[i]["UNITPRICE"]+"'></td>";
				result += "<td>"+data.estList[i]["INVUOM"]+"</td>";
				result += "<td>"+data.estList[i]["INVQTY"]+"</td>";
				result += "<td>"+data.estList[i]["AVLQTY"]+"</td>";
				var uomStr="";
				uomStr += "<select class='form-control input-sm' name='SALESUOM' onchange='CheckPriceVal(this.value,\""+data.estList[i]["PRODUCT"]+"\",this)'>";
				for(var j = 0; j < data.uomList.length; j ++){
					if(data.uomList[j]["UOM"] == data.estList[i]["ORDUOM"]){
						uomStr += "<option value='"+ data.uomList[j]["UOM"] +"' selected>"+ data.uomList[j]["UOM"] +"</option>";
					}else{
						uomStr += "<option value='"+ data.uomList[j]["UOM"] +"'>"+ data.uomList[j]["UOM"] +"</option>";
					}
					
				}
				uomStr += "</select>";
				result += "<td>"+uomStr+"</td>";
				result += "<td style='position:relative;'>"+'<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" onclick="deleteList('+i+')"></span><input type="text" class="form-control input-sm" onchange="validateOrderQty(this)" onkeypress="return isNumberKey(event,this,4)" name="SALESUOMQTY" value="0.000">'+"</td>";
				result += "</tr>";
			}
			$("#estListSummary > tbody").html(result);
			$("#estListSummary").show();
			$('input[type="checkbox"]').prop('checked', false);
			}
		}
	});
}

function checkAllEstList(ischeck){
	$("input[name='chkdEstList']").prop('checked', ischeck);
}

function getAvailableQuantity(uom,item,loc,obj) {
	var urlStr = "/track/InvMstServlet";
	var plant = document.form1.plant.value;
	var numberofdecimal = document.form1.numberOfDecimal.value;
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ITEM : item,			
            UOM:uom,
            LOC:loc,
            PLANT : plant,
            ACTION : "GET_AVAILABLE_QTY",
		},
		dataType : "json",
		success : function(data) {				
			if (data.status == "100") {
				var resultVal = data.result;
				var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
				if(resultVal.AVAILABLEQTY == null || resultVal.AVAILABLEQTY == undefined || resultVal.AVAILABLEQTY == 0){
					$(obj).parent().parent().find("td:nth-child(11)").html(parseFloat("0.00000").toFixed("3"))
				}else{
					$(obj).parent().parent().find("td:nth-child(11)").html(parseFloat(resultVal.AVAILABLEQTY).toFixed("3"))
				}
			}
		}
	});
}

function loadItemData(obj, catalogPath, account, cost){
	
}