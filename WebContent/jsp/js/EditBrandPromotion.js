var subWin = null;
var itemList="";
var taxList = [];
var zerotype=0;
$(document).ready(function(){
	var plant = document.cpoform.plant.value;
	var Id = document.cpoform.HDRID.value;
	getEditPromotionDetails(Id);
	$(".BV").hide();

/* Customer Auto Suggestion */
	$('#CUSTOMER_TYPE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CUSTOMER_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_CUSTOMERTYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTYPEMST);
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
				return '<div><p class="item-suggestion">'+data.CUSTOMER_TYPE_ID+'</p><br/><p class="item-suggestion">DESC: '+data.CUSTOMER_TYPE_DESC+'</p></div>';
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


	$(".po-table tbody").on('click','.bill-action',function(){
		$(this).parent().parent().remove();
	    setLineNo();
	});
		
	$("#btnSalesOpen").click(function(){
		var isValid = validateProductPromotion();
		if(isValid){
			$( "#brandpromotionForm" ).submit();
		}
	});

	addSuggestionToTable();

});

function headerReadable(){
	if(document.cpoform.DATEFORMAT.checked)
	{
		document.cpoform.DELDATE.value="";
		$('#DELDATE').attr('readonly',true).datepicker({ dateFormat: 'dd/mm/yy'});
	}
	else{
		document.cpoform.DELDATE.value="";
		 $('#DELDATE').attr('readonly',false).datepicker("destroy");
	}
}

function addSuggestionToTable(){
	var plant = document.cpoform.plant.value;
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
			//return '<div onclick="loadItemData(this,\''+ data.ITEM+'\',\''+ data.CATLOGPATH+'\',\''+ data.ACCOUNT+'\',\''+ data.UNITPRICE+'\',\''+ data.PURCHASEUOM+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.INVQTY+' '+data.UOM+'</p></div>';
				return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.PURCHASEINVQTY+' '+data.PURCHASEUOM+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();
			}
			menuElement.after( '<div class="itemAddBtn footer"  data-toggle="modal" data-target="#productModal"><a href="#"> + Add New Product</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".bigdrop").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			menuElement.next().show();
		}).on('typeahead:select',function(event,selection){
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		});
		
	/*Search Product Brand Id Auto Suggestion */
	$('.ProductBrand').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_BRAND_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRD_BRAND_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_BRANDMST);
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
			return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			/*menuElement.after( "<div class=\"PRD_BRANDAddBtn footer\" ><a href=\"#\"  onClick=\"javascript:popUpWin('list/brandlist_save.jsp?prdBrand='"+form.PRD_BRAND.value+");return false;\"> + Add Product Brand</a></div>");*/
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

}

function removeSuggestionToTable(){
	$(".itemSearch").typeahead('destroy');
	$(".ProductBrand").typeahead('destroy');
}

function addRow(){
	var curency = $("input[name=CURRENCYID]").val();
	var body="";
	body += '<tr>';
	body += '<td class="item-img text-center"  style="width:2%">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">';
	body += '<input type="hidden" name="lnno" value="1">';
	body += '<input type="hidden" name="itemdesc" value="">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item_brand" class="form-control ProductBrand" onchange="checkprdcat(this.value,this)" style="width:87%" placeholder="Type or click to select an Brand.">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '</td>';	
	body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="1.000" onkeypress="return isNumberKey(event,this,4)">';
	body += '</td>';	
	body += '<td class="item-img text-center BQ"  style="width:2%">';
	body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">';
	body += '</td>';
	body += '<td class="BQ">';
	body += '<input type="text" name="get_item" class="form-control itemSearch" onchange="checkitems(this.value,this)" style="width:95%" placeholder="Type or click to select an item.">';
	body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '</td>';
	body += '<td class="BQ" style="position:relative;"><span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" style="left: 345px;"></span><input type="text" name="get_qty" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="1.000" onkeypress="return isNumberKey(event,this,4)"></td>';
	body += '<td class="BV">';
	body += '<div class="row">';							
	body += '<div class=" col-lg-12 col-sm-3 col-12">';
	body += '<div class="input-group my-group" style="width:120px;">';
	body += '<select name="promotion_type" class="discountPicker form-control" style="left: 60px;" >';
	body += "<option value="+curency+">"+curency+"</option>";
	body += '<option>%</option>';										
	body += '</select>';
	body += '</div>';
	body += '</div>'; 
	body += '</div>';
	body += '</td>';	
	body += '<td class="BV" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" style="left: 475px;"></span>';
	body += '<input type="text" name="promotionvalue" onkeypress="return isNumberKey(event,this,4)" value="0.000" class="form-control" >';
	body += '</td>';
	body += '<td class="bill" style="position:relative;">';
	body += '<input type="text" name="LIMIT_USAGE" onkeypress="return isNumberKey(event,this,4)" value="0.000" class="form-control" >';
	body += '</td>';
	body += '</tr>';
	$(".po-table tbody").append(body);
	removeSuggestionToTable();
	addSuggestionToTable();
	if($("input[name=DEDUCT_INV]").val() == "0"){
		$(".invEl").hide();
	}
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	});
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
	
		if($("input[name=PRMDESC]").val() == "0"){
		    $(".BV").hide();
		    $(".BQ").show();
	 	 }
	
	   	if($("input[name=PRMDESC]").val() == "1"){
		   $(".BQ").hide();
		   $(".Bv").show();
	    }
	    
	    if($("input[name=PRMDESC]").val() == "byqty"){
		    $(".BV").hide();
	 	 }
	
	   	if($("input[name=PRMDESC]").val() == "byval"){
		   $(".BQ").hide();
	    }
}


function setLineNo(){
	var i=1;
	$(".po-table tbody tr td:first-child").each(function() {
		$(this).find('input[name=lnno]').val(i);
		i++;
	});
}

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}

function checkitems(itemvalue,obj){	
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				ITEM : itemvalue,
				ACTION : "PRODUCT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Does't Exists");
						$(obj).typeahead('val', '');
						$(obj).parent().find('input[name="item"]').focus();
						return false;	
						
					} 
					else 
						return true;
				}
			});
		 return true;
}

function validateProductPromotion(){
	var promotion = $("input[name=PROMOTION_NAME]").val();
	var outletname = $("input[name=OUTLET_NAME]").val();
	var promotiondesc = $("input[name=PROMOTION_DESC]").val();
	var limitusage = $("input[name=LIMIT_USAGE]").val();
	var prm = $("input[name=PRMDESC]").val();
	var buyitem = $("input[name=item]").val();
	var buyqty = $("input[name=qty]").val();
	var getitem = $("input[name=get_item]").val();
	var getqty = $("input[name=get_qty]").val();
	var prmtype = $("input[name=promotion_type]").val();
	var prmvalue = $("input[name=promotionvalue]").val();
	
	if(promotion == ""){
		alert("Please Enter Promotion Name.");
		document.getElementById("PROMOTION_NAME").focus();
		return false;
	}	
	
	if(promotiondesc == ""){
		alert("Please Enter Promotion Description.");
		document.getElementById("PROMOTION_DESC").focus();
		return false;
	}	
	if(outletname == ""){
		alert("Please Enter Outlet.");
		document.getElementById("OUTLET_NAME").focus();
		return false;
	}	
	
	if(limitusage == ""){
		alert("Please Enter Limti Of Usage.");
		document.getElementById("LIMIT_USAGE").focus();
		return false;
	}	
	
	if((prm == "byqty") || (prm == "0")){
			if(buyitem == ""){alert("Please Select by Product.");return false;}	
			if(buyqty == ""){alert("Please enter by Qty.");return false;}	
			if(getitem == ""){alert("Please Select Get Product.");return false;}	
			if(getqty == ""){alert("Please enter Get Qty.");return false;}	
	}	
	
	if((prm == "byval")  || (prm == "1")){
			if(buyitem == ""){alert("Please Select by Product.");return false;}	
			if(buyqty == ""){alert("Please enter by Qty.");return false;}	
			if(prmtype == ""){alert("Please Select a Type.");return false;}	
			if(prmvalue == ""){alert("Please Enter a Promotion Value.");return false;}	
	}	

	return true;
}

function getEditPromotionDetails(Id){
	var plant = document.cpoform.plant.value;
	$.ajax({
		type : "POST",
		url : "../brandpromotion/LoadEditDetails?ID="+Id,
		async : true,
		data : {
			PLANT : plant,
		},
		dataType : "json",
		success : function(data) {
			loadPromotionTable(data.orders);
		}
	});
}

function loadPromotionTable(orders){
	var curency = $("input[name=CURRENCYID]").val();
	$(".po-table tbody").html("");
	var body="";
	var ch="0";
	$.each(orders, function( key, data) {
			body += '<tr>';
			body += '<td class="item-img text-center"style="width:2%">';
			body += '<img alt="" src="'+data.CATLOGPATH+'" style="width: 100%;">';
			body += '<input type="hidden" name="lnno" value="'+data.LNNO+'">';
			body += '</td>';
			body += '<td class="bill-item">';
			body += '<input type="text" name="item_brand" class="form-control ProductBrand" value="'+data.BUY_PRD_BRAND_ID+'" onchange="checkprdcat(this.value,this)" style="width:87%" placeholder="Type or click to select an Brand.">';
			body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
			body += '</td>';	
			body += '<td class="item-qty text-right"><input type="text" name="qty" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="'+data.BUY_QTY+'" onkeypress="return isNumberKey(event,this,4)">';
			body += '</td>';	
			body += '<td class="item-img text-center BQ"  style="width:2%">';
			body += '<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">';
			body += '</td>';
			body += '<td class="BQ">';
			body += '<input type="text" name="get_item" class="form-control itemSearch" value="'+data.GET_ITEM+'" onchange="checkitems(this.value,this)" style="width:95%" placeholder="Type or click to select an item.">';
			body += '<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
			body += '</td>';
			body += '<td class="BQ" style="position:relative;">';
			//if(ch != "0"){
			if(data.LNNO > 1){
			body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" style="left: 345px;"></span>';
			}
			body += '<input type="text" name="get_qty" class="form-control text-right" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" value="'+data.GET_QTY+'" onkeypress="return isNumberKey(event,this,4)"></td>';
			body += '<td class="BV">';
			body += '<div class="row">';							
			body += '<div class=" col-lg-12 col-sm-3 col-12">';
			body += '<div class="input-group my-group" style="width:120px;">';
			body += '<select name="promotion_type" class="discountPicker form-control" value="'+data.PROMOTION_TYPE+'" style="left: 60px;" >';
			if(data.PROMOTION_TYPE == curency){
				body += "<option selected value="+curency+">"+curency+"</option>";
			}else{
				body += "<option value="+curency+">"+curency+"</option>";
			}
			if(data.PROMOTION_TYPE == "%"){
				body += '<option selected >%</option>';
			}else{
				body += '<option>%</option>';
			}
			body += '</select>';
			body += '</div>';
			body += '</div>'; 
			body += '</div>';
			body += '</td>';	
			body += '<td class="BV" style="position:relative;">';
			//if(ch != "0"){
			if(data.LNNO > 1){
			body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" style="left: 475px;"></span>';
			}
			body += '<input type="text" name="promotionvalue" value="'+data.PROMOTION+'" onkeypress="return isNumberKey(event,this,4)" class="form-control" >';
			body += '</td>';
			body += '<td class="bill" style="position:relative;">';
			body += '<input type="text" name="LIMIT_USAGE" value="'+data.LIMIT_OF_USAGE+'" onkeypress="return isNumberKey(event,this,4)" class="form-control" >';
			body += '</td>';
			body += '</tr>';

	});

	$(".po-table tbody").html(body);

	removeSuggestionToTable();
	addSuggestionToTable();
	if($("input[name=DEDUCT_INV]").val() == "0"){
		$(".invEl").hide();
	}
	$( ".datepicker" ).datepicker({
	    changeMonth: true,
	    changeYear: true,
		dateFormat: 'dd/mm/yy',
		yearRange: $currentPageIndex == -1 ? "-10:+10":"-100:+0",
	});
	
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
	
	if($("input[name=PRMDESC]").val() == "0"){
		    $(".BV").hide();
		    $(".Bq").show();
	 	 }
	
	   	if($("input[name=PRMDESC]").val() == "1"){
		   $(".BQ").hide();
		   $(".BV").show();
	    }
	
}


function changeitem(obj){
	var obj2 = $(obj).parent().find('input[name="item"]');
	$(obj2).typeahead('val', '');
	$(obj).parent().find('input[name="item"]').focus();
}

function checkprdcat(cat,obj){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PRD_CLS_ID : cat,
				ACTION : "CAT_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Product Brand Does't Exists");
						$(obj).typeahead('val', '');
						$(obj).parent().find('input[name="item_brand"]').focus();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function toDataURL(src, callback, outputFormat) {
	  var img = new Image();
	  img.crossOrigin = 'Anonymous';
	  img.onload = function() {
	    var canvas = document.createElement('CANVAS');
	    var ctx = canvas.getContext('2d');
	    var dataURL;
	    canvas.height = this.naturalHeight;
	    canvas.width = this.naturalWidth;
	    ctx.drawImage(this, 0, 0);
	    dataURL = canvas.toDataURL(outputFormat);
	    callback(dataURL);
	  };
	  img.src = src;
	  if (img.complete || img.complete === undefined) {
	    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
	    img.src = src;
	  }
}