$(document).ready(function(){
	var plant = document.form.plant.value;
	removeSuggestionToTables();
	addSuggestionToTables1();
	
//	var trid = $(obj).closest('tr').attr('id');
//		addSuggestionToTables(trid);
	$(".stk-table tbody").on('click','.bill-action',function(){
	    $(this).parent().parent().remove();
	});
	
	
	var STATUS = $("#STATUS").val();
	
/* From location Auto Suggestion */
			var previousValue = "";
			var previousdesValue = "";
			if(STATUS!=''){
				previousValue =$("#FROMLOC").val();
				previousdesValue =$("#FROM_LOCDESC").val();
			}
			$('#FROMLOC').typeahead({
			    hint: true,
			    minLength: 0,
			    searchOnFocus: true
			},
			{
			    display: 'LOC',
			    source: function (query, process, asyncProcess) {
			        var urlStr = "/track/ItemMstServlet";
			        $.ajax({
			            type: "POST",
			            url: urlStr,
			            async: true,
			            data: {
			                PLANT: plant,
			                ACTION: "GET_LOC_LIST_FOR_SUGGESTION",
			                QUERY: query
			            },
			            dataType: "json",
			            success: function (data) {
			                return asyncProcess(data.LOC_MST);
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
			        suggestion: function (data) {
			            return '<div onclick="document.form.FROM_LOCDESC.value = \'' + data.LOCDESC + '\'"><p class="item-suggestion">' + data.LOC + '</p><br/><p class="item-suggestion">DESC:' + data.LOCDESC + '</p></div>';
			        }
			    }
			})
			.on('typeahead:select', function (event, selection) {
			    var FROMLOC = $("#FROM_LOCDESC").val();
//			    alert("Previous Value: " + previousValue + "\nCurrent Value: " + FROMLOC);
		    	if(previousValue!=''&&previousValue!=FROMLOC){
		        	 confirmClearProductDetails(previousValue,previousdesValue);						
				}
//				removeSuggestionToTables();
//				addSuggestionToTables1();
//			    if ($(this).val() != "") {
//			        document.form.FROM_LOCDESC.value = selection.LOCDESC;
//			    }
			})
			.on('typeahead:open', function (event, selection) {
			    var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			    element.toggleClass("glyphicon-menu-up", true);
			    element.toggleClass("glyphicon-menu-down", false);
			})
			.on('typeahead:close', function () {
			    var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			    element.toggleClass("glyphicon-menu-up", false);
			    element.toggleClass("glyphicon-menu-down", true);
			})
			.on('typeahead:change', function (event, selection) {
			    if ($(this).val() == "") {
			        document.form.FROM_LOCDESC.value = "";
			        document.getElementById("FROM_LOCDESC").value = "";
			    }
			    previousValue = $(this).val();
			    previousdesValue = $(this).val();
			});
			
/* To location Auto Suggestion */
			$('#TOLOC').typeahead({
				  hint: true,
				  minLength:0,  
				  searchOnFocus: true
				},
				{
				  display: 'LOC',  
				  source: function (query, process,asyncProcess) {
					var urlStr = "/track/ItemMstServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.LOC_MST);
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
					return '<div onclick="document.form.TOLOCDESC.value = \''+data.LOCDESC+'\'"><p class="item-suggestion">'+data.LOC+'</p><br/><p class="item-suggestion">DESC:'+data.LOCDESC+'</p></div>';
					}
				  }
				}).on('typeahead:select',function(event,selection){
					if($(this).val() != ""){
						document.form.TOLOCDESC.value = selection.LOCDESC; 
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
		 			if($(this).val() == ""){
		 				document.form.TOLOCDESC.value = "";
		 				document.getElementById("TOLOCDESC").value = "";
		 			}
			});
	
});

function addaction(type,exist){
	 var fromlocdesc = document.form.FROM_LOCDESC.value;
	  var tolocdesc = document.form.TOLOCDESC.value;
	 var empname =document.form.EMP_NAME.value;
	  var item = document.form.ITEM.value;	
	  var loc = document.form.FROM_LOC.value;	
	  var toloc = document.form.TOLOC.value;	
	  var tranid = document.form.TRANID.value;	
	  var scanqty = document.form.QTY.value;	
	  var availqty = document.form.AVAILQTY.value;
	  var uom = document.form.UOM.value;
	  var isItemValid = true,isBatchValid=true;
	  if((tranid==null||tranid=="")&&(loc==null||loc==""))
	  {
		  alert("Please create Transaction ID & select location before adding product!");
		  document.form.TRANID.focus();
		  return false;
	  }
	  if(tranid==null||tranid=="")
	  {
		  alert("Please Enter Tran Id!");
		  document.form.TRANID.focus();
		  return false;
	  }
	  if(loc==null||loc=="")
	  {
		  alert("Please Enter Location!");
		  document.form.FROM_LOC.focus();
		  return false;
	  }
	  if(toloc==null||toloc=="")
	  {
		  alert("Please Enter To Location!");
		  document.form.TOLOC.focus();
		  return false;
	  }
	  if(toloc==loc){
		  alert("Please choose TO LOC different from FROM LOC");
		  document.form.TOLOC.focus();
		  return false;  
	  }
	  
	  $("input[name ='item']").each(function() {
	    if($(this).val() == ""){	    	
	    	isItemValid = false;
	    }
	});
	
	if(!isItemValid){
		alert("The Product field cannot be empty.");
		return false;
	}
	  
	  $("input[name ='batch']").each(function() {
	    if($(this).val() == ""){	    	
	    	isBatchValid = false;
	    }
	});
	
	if(!isBatchValid){
		alert("The Batch field cannot be empty.");
		return false;
	}
	
	if(type==1){
		printaction(type,exist);
	}else{
		document.form.cmd.value="ADD" ;
	   	document.form.action  = "/track/DynamicProductServlet?action=ADD";
		document.form.submit();
	}
	return false;	

  }
  
function printaction(type,exist){
	 var itemvalue=document.getElementById("item").value;
	  var TOLOCID =document.getElementById("TOLOC").value;
	  var url = "/track/DynamicProductServlet?action=ADD&cmd=printstockmoveproduct";
//	  if(exist == 'true')
//	  		url = "/track/DynamicProductServlet?cmd=printstockmoveproduct"
//	  		alert(url);

	  var tranid=document.form.TRANID.value;
	  if(tranid==null||tranid==""){
		 alert("Generate Transaction Id");
		 return false;
	  }

	        var formData = $('form#dynamicprdswithoutpriceForm').serialize();

				$.ajax({
					type : 'get',
					url : url,
					dataType : 'html',
					responseType: 'arraybuffer',
					data : formData,

					success : function(data, status, xhr) {
						
			var result = data.split(":");
			if(result[0] == "Success" && result[2] == "1")
			{
						onNewPOS();
						setTimeout(function(){ $('#appenddiv').html("Products transfered to "+  TOLOCID + " successfully."); }, 1000);
						setTimeout(function(){window.location.href = "../inhouse/stockmove?msg="+tranid+" Created and Products transfered to "+  TOLOCID + " successfully";}, 2500); // Slightly delay redirection if you want to show the success message
			}
			else if(result[0] == "Success" && result[2] == "0"){
				onNewPOS();
				setTimeout(function(){ $('#appenddiv').html("Products transfered to "+  TOLOCID + " successfully."); }, 1000);
				setTimeout(function(){window.location.href = "../inhouse/stockmove?msg="+tranid+" Created and Products transfered to "+  TOLOCID + " successfully";}, 2500); // Slightly delay redirection if you want to show the success message
			}
			else{
				$('#appendbody').html(data);
				setTimeout(function(){ $('#appenddiv').html("Products transfered to "+  TOLOCID + " successfully."); }, 1000);
				setTimeout(function(){window.location.href = "../inhouse/stockmove?msg="+tranid+" Created and Products transfered to "+  TOLOCID + " successfully";}, 2500); // Slightly delay redirection if you want to show the success message
			} 
					

					},
					error : function(data) {

						alert(data.responseText);
					}
				});
	 
	  return false;
  }
	
function addSuggestionToTables1(){
	var plant = document.form.plant.value;
	var CUSTNO = '';
		
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
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				CUSTNO : CUSTNO,
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
			return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".bigdrop").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:select',function(event,selection){
			loadItemData(this, selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.SALESUOM,selection.ITEM,selection.nonStkFlag,selection.COST);	
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				$(this).closest('tr').find('input[name="ITEMDES"]').val("");
			}
		});	
		
		//Imthiyas added (press Enter Key for item to appear)
			$(".itemSearch").on('keydown', function(event) {
			    if (event.key === 'Enter') {
			        const item = $(this).val();
			        $.ajax({
			            type: "POST",
			            url: "../ItemMstServlet",
			            data: {
			                ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				            CUSTNO : CUSTNO,
			                ITEM: item
			            },
			            dataType: "json",
			            success: function(data) {
			                if (data.items && data.items.length > 0) {
			                    const selection = data.items[0];
			                    loadItemData(this, selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.SALESUOM,selection.ITEM,selection.nonStkFlag,selection.COST);	
			                    $(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
			                    $(this).closest('tr').find("input[name='item']").val(selection.ITEM);
			                } else {
			                    $(this).closest('tr').find('input[name="ITEMDES"]').val("");
			                    $(this).closest('tr').find('input[name="item"]').val("");
//			                    alert('No item found.');
			                }
			            }.bind(this),
			            error: function() {
			                alert('Error fetching item data.');
			            }
			        });
			        event.preventDefault();
			    }
			});
			//Imthiyas end
	
	/* To get the suggestion data for Uom */
	$('.uomSearch').typeahead({
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
			return '<div onclick="CheckPriceVal(this,\''+data.UOM+'\')"><p class="item-suggestion">'+data.UOM+'</p></div>';
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
	
	$(".batchSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
		  display: 'BATCH',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			var obj = $(this)[0].$el.parent().parent().parent().parent().closest('tr');
			var FROMLOC = $("#FROMLOC").val();
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_BATCH_DATA",
				QUERY : query,
				ITEMNO : obj.find("input[name=item]").val(),
				UOM : obj.find("input[name=uom]").val(),
				LOC : FROMLOC
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
				console.log(data);
				return '<div onclick="CheckinvqtyVal(this,\''+data.QTY+'\')"><p class="item-suggestion">Batch: '+data.BATCH+'</p><p class="item-suggestion pull-right">PC/PCS/EA UOM :'+data.PCSUOM+'</p><br/><p class="item-suggestion">PC/PCS/EA UOM Quantity: '+data.PCSQTY+'</p><p class="item-suggestion pull-right">Inventory UOM: '+data.UOM+'</p><br/><p class="item-suggestion">Inventory UOM Quantity: '+data.QTY+'</p><p class="item-suggestion pull-right">Received Date: '+data.CRAT+'</p><br/><p class="item-suggestion">Expiry Date: '+data.EXPIRYDATE+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
			$(this).parent().parent().parent().find('input[name=batch]').typeahead('val', 'NOBATCH');
			$(this).parent().parent().parent().find('input[name=batch]').typeahead('val', '');
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:select',function(event,selection){
			CheckinvqtyVal(this, selection.QTY);
			$(this).closest('tr').find('input[name="qty"]').focus();
			$(this).closest('tr').find('input[name="qty"]').select();
		});
		
		$('.stk-table tbody tr').each(function () {
			$(this).find("input[name ='item']").focus();
		});
		
		
}	

function addSuggestionToTables(rowid){
	removeSuggestionToTables();
	var plant = document.form.plant.value;
	var CUSTNO = '';
		
	$("#"+rowid+" .itemSearch").typeahead({
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
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				CUSTNO : CUSTNO,
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
			return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.SALESINVQTY+' '+data.SALESUOM+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".bigdrop").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".bigdrop");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:select',function(event,selection){
			loadItemData(this, selection.CATLOGPATH,selection.ACCOUNT,selection.UNITPRICE,selection.SALESUOM,selection.ITEM,selection.nonStkFlag,selection.COST);	
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				$(this).closest('tr').find('input[name="ITEMDES"]').val("");
			}
		});	
	
	/* To get the suggestion data for Uom */
	$('#'+rowid+' .uomSearch').typeahead({
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
			return '<div onclick="CheckPriceVal(this,\''+data.UOM+'\')"><p class="item-suggestion">'+data.UOM+'</p></div>';
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
	
	$('#'+rowid+' .batchSearch').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
		  display: 'BATCH',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			var obj = $(this)[0].$el.parent().parent().parent().parent().closest('tr');
			var FROMLOC = $("#FROMLOC").val();
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_BATCH_DATA",
				QUERY : query,
				ITEMNO : obj.find("input[name=item]").val(),
				UOM : obj.find("input[name=uom]").val(),
				LOC : FROMLOC
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
				console.log(data);
				return '<div onclick="CheckinvqtyVal(this,\''+data.QTY+'\')"><p class="item-suggestion">Batch: '+data.BATCH+'</p><p class="item-suggestion pull-right">PC/PCS/EA UOM :'+data.PCSUOM+'</p><br/><p class="item-suggestion">PC/PCS/EA UOM Quantity: '+data.PCSQTY+'</p><p class="item-suggestion pull-right">Inventory UOM: '+data.UOM+'</p><br/><p class="item-suggestion">Inventory UOM Quantity: '+data.QTY+'</p><p class="item-suggestion pull-right">Received Date: '+data.CRAT+'</p><br/><p class="item-suggestion">Expiry Date: '+data.EXPIRYDATE+'</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
			$(this).parent().parent().parent().find('input[name=batch]').typeahead('val', 'NOBATCH');
			$(this).parent().parent().parent().find('input[name=batch]').typeahead('val', '');
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:select',function(event,selection){
			$(this).closest('tr').find('input[name="qty"]').focus();
			$(this).closest('tr').find('input[name="qty"]').select();
		});
		
		$("#"+rowid).each(function () {
			$(this).find("input[name ='item']").focus();
		});
		
}

function removeSuggestionToTables(){
	$(".itemSearch").typeahead('destroy');
	$(".uomSearch").typeahead('destroy');
	$(".batchSearch").typeahead('destroy');
}

function loadItemData(obj, catalogPath, account, cost,salesuom, productId, nonStkFlag,price){
	$(obj).closest('tr').find("input[name=uom]").val(salesuom);
	$(obj).closest('tr').find("input[name=batch]").val('');
	$(obj).closest('tr').find("input[name=ITEMINVQTY]").val('');
	$(obj).closest('tr').find("input[name=qty]").val('1.000');
	$(obj).closest('tr').find('input[name="batch"]').focus();
	$(obj).closest('tr').find('input[name="batch"]').select();
}	

function CheckinvqtyVal(obj, qty) {
	qty = parseFloat(qty).toFixed("3")
	$(obj).closest('tr').find('input[name="ITEMINVQTY"]').val(qty);
}

function setITEMDESC(obj,desc){
	$(obj).closest('tr').find("input[name ='ITEMDES']").val(desc);
}	

function addRow(event){
	removeSuggestionToTables();
	event.preventDefault(); 
    event.stopPropagation();
    var rowid = Date.now().toString(36) + Math.random().toString(36).substr(2);
	var body="";
	body += '<tr id="'+rowid+'">';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" style="width:87%" class="form-control itemSearch" onchange="checkitems(this.value,this)" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -20px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;width: 25px;"  onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';	
	body += '<td class="bill-item">';
	body += '<input type="text" name="uom" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="UOM">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="batch" value="" class="form-control batchSearch" placeholder="Batch">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="ITEMINVQTY" tabindex="-1" class="form-control text-right" value=""  onkeypress="return isNumberKey(event,this,4)" readonly></td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" style="position: relative;right: -33px;top: -23px;"></span>';
	body += '</td>';
	body += '</tr>';
	$(".stk-table tbody").append(body);
	removeSuggestionToTables();
	addSuggestionToTables1();
//	addSuggestionToTables(rowid);
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

function addRowclear(event){
	removeSuggestionToTables();
	event.preventDefault(); 
    event.stopPropagation();
    var rowid = Date.now().toString(36) + Math.random().toString(36).substr(2);
	var body="";
	body += '<tr id="'+rowid+'">';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" style="width:87%" class="form-control itemSearch" onchange="checkitems(this.value,this)" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -20px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;width: 25px;"  onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';	
	body += '<td class="bill-item">';
	body += '<input type="text" name="uom" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="UOM">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="batch" value="" class="form-control batchSearch" placeholder="Batch">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="ITEMINVQTY" tabindex="-1" class="form-control text-right" value=""  onkeypress="return isNumberKey(event,this,4)" readonly></td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
	body += '</td>';
	body += '</tr>';
	$(".stk-table tbody").append(body);
	removeSuggestionToTables();
	addSuggestionToTables1();
//	addSuggestionToTables(rowid);
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

function addRow2(event){
	removeSuggestionToTables();
	event.preventDefault(); 
    event.stopPropagation();
	var rowid = Date.now().toString(36) + Math.random().toString(36).substr(2);
	var body="";
	body += '<tr id="'+rowid+'">';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" style="width:87%" class="form-control itemSearch" onchange="checkitems(this.value,this)" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -20px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;width: 25px;"  onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';	
	body += '<td class="bill-item">';
	body += '<input type="text" name="uom" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="UOM">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="batch" value="" class="form-control batchSearch" placeholder="Batch">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="ITEMINVQTY" tabindex="-1" class="form-control text-right" value=""  onkeypress="return isNumberKey(event,this,4)" readonly></td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
	body += '</td>';
	body += '</tr>';
	$(".stk-table tbody").append(body);
	addSuggestionToTables(rowid);
	addSuggestionToTables1();
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

function addRow1(event){
	event.preventDefault(); 
    event.stopPropagation();
	var body="";
	body += '<tr>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" style="width:87%" class="form-control itemSearch" onchange="checkitems(this.value,this)" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -20px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;width: 25px;"  onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';	
	body += '<td class="bill-item">';
	body += '<input type="text" name="uom" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="UOM">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="batch" value="" class="form-control batchSearch" placeholder="Batch">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="ITEMINVQTY" tabindex="-1" class="form-control text-right" value=""  onkeypress="return isNumberKey(event,this,4)" readonly></td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true" style="position: relative;right: -33px;top: -23px;"></span>';
	body += '</td>';
	body += '</tr>';
	$(".stk-table tbody").append(body);
	removeSuggestionToTables();
	addSuggestionToTables();
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
	
	
function addRow23(event){
	event.preventDefault(); 
    event.stopPropagation();
	var body="";
	body += '<tr>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="item" style="width:87%" class="form-control itemSearch" onchange="checkitems(this.value,this)" placeholder="Type or click to select an item." onchange="calculateAmount(this)">';
	body += '<button type="button" style="position: absolute;margin-left: -20px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;width: 25px;"  onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>';
	body += '<input class="form-control"  name="ITEMDES" readonly style="height: 23px;background-color: #fff;" readonly>';
	body += '</td>';	
	body += '<td class="bill-item">';
	body += '<input type="text" name="uom" class="form-control uomSearch" onchange="checkprduom(this.value,this)" placeholder="UOM">';
	body += '</td>';
	body += '<td class="bill-item">';
	body += '<input type="text" name="batch" value="" class="form-control batchSearch" placeholder="Batch">';
	body += '</td>';
	body += '<td class="item-qty text-right"><input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="ITEMINVQTY" tabindex="-1" class="form-control text-right" value=""  onkeypress="return isNumberKey(event,this,4)" readonly></td>';
	body += '<td class="item-amount text-right grey-bg" style="position:relative;">';
	body += '<input type="text" data-rl="0.000" data-msq="0.000" data-soh="0.000" data-eq="0.000" data-aq="0.000" name="qty" class="form-control text-right" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">';
	body += '</td>';
	body += '</tr>';
	$(".stk-table tbody").append(body);
	removeSuggestionToTables();
	addSuggestionToTables();
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
	
//table validation
function checkitems(itemvalue,obj){	
		var trid = $(obj).closest('tr').attr('id');
		addSuggestionToTables(trid);
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
						$(obj).parent().find('input[name="uom"]').focus();
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checkfromloc(loc){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				LOC : loc,
				ACTION : "LOC_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Location Does't Exists");
						document.getElementById("FROMLOC").focus();
						$("#FROMLOC").typeahead('val', '');
						document.getElementById("FROM_LOCDESC").value = "";
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}

function checktoloc(loc){	
		var urlStr = "/track/MasterServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				LOC : loc,
				ACTION : "LOC_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "99") {
						alert("Location Does't Exists");
						document.getElementById("TOLOC").focus();
						$("#TOLOC").typeahead('val', '');
						document.getElementById("TOLOCDESC").value = "";
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}	

function changeitem1(obj){
	var obj2 = $(obj).parent().find('input[name="item"]');
	$(obj2).typeahead('val', '');
	$(".itemSearch").typeahead('val', '');
	$(obj).parent().find('input[name="item"]').focus();
}

function changeitem(obj){
	var obj2 = $(obj).parent().find('input[name="item"]');
	$(obj2).typeahead('val', '');
	$(obj).parent().find('input[name="item"]').focus();
	
	var trid = $(obj).closest('tr').attr('id');
	addSuggestionToTables(trid);
}

function calculateAmount(obj){

	var loc = $("#FROMLOC").val();
	var batch = $(obj).closest('tr').find('input[name=batch]').val();
	
	var iteminvqty = parseFloat($(obj).closest('tr').find('input[name=ITEMINVQTY]').val()).toFixed(3);
	if(isNaN(iteminvqty))
		iteminvqty = parseFloat(0).toFixed(3);
		
		
	var qty = parseFloat($(obj).closest('tr').find('input[name=qty]').val()).toFixed(3);
	if(isNaN(qty))
		qty = parseFloat(0).toFixed(3);
		
	if(loc!=''){
		if(iteminvqty=='0.000'){
//			alert('No Inventory For this Product')
			alert('Select Batch field to Provide QTY')
			$(obj).closest('tr').find('input[name="batch"]').focus();
			$(obj).closest('tr').find('input[name="batch"]').select();
			qty = parseFloat(0).toFixed(3);
		}else{
			if(parseFloat(qty)  > parseFloat(iteminvqty)){
//				alert('Qty should not be Greater than Available Qty  '+iteminvqty+' ')
//				qty = iteminvqty;
				alert('Qty should not be Greater than Available Qty.')
				qty = parseFloat(0).toFixed(3);
				$(obj).closest('tr').find('input[name="qty"]').focus();
				$(obj).closest('tr').find('input[name="qty"]').select();
			}
		}
	}
	/*else{
		alert('Select From location')
			qty = parseFloat(0).toFixed(3);
	}*/
	
	$(obj).closest('tr').find('input[name=qty]').val(qty);
}
	
function confirmClearProductDetails(previousValue,des) {
    var productTable = document.getElementById("stockproductTable"); 
    var rows = productTable.getElementsByTagName("tr");
    if (rows.length > 1) {
//        var confirmClear = confirm("Changing the 'From Location' will clear the product details. Do you want to continue?");
        var confirmClear = confirm("Changing the 'From Location' will clear the [ Batch,Available Qty,Quantity ]. Do you want to continue?");
        if (confirmClear) {
            clearProductDetails();
        } else {
			$("#FROMLOC").typeahead('val', previousValue);
			$("#FROM_LOCDESC").val(des);
			document.form.FROM_LOCDESC.value = des;
			document.getElementById("FROM_LOCDESC").value = des;
        }
    }
}

function clearProductDetails1() {
    var productTable = document.getElementById("stockproductTable");
    while (productTable.rows.length > 1) {
        productTable.deleteRow(1);
    }
    addRowclear(event);
}

function clearProductDetails() {
    $('.stk-table tbody tr').each(function () {
		$(this).closest('tr').find("input[name=ITEMINVQTY]").val("0.000");
		$(this).closest('tr').find("input[name=qty]").val("0.000");
		$(this).parent().parent().parent().find('input[name=batch]').typeahead('val', '');
		$(this).parent().parent().parent().find('input[name=batch]').typeahead('val', 'NOBATCH');
		$(this).parent().parent().parent().find('input[name=batch]').typeahead('val', '');
		$(this).closest('tr').find("input[name=batch]").val("");
		var trid = $(this).closest('tr').attr('id');
		addSuggestionToTables(trid);
		removeSuggestionToTables();
		addSuggestionToTables1();
//		$(this).closest('tr').find('input[name="batch"]').focus();
	});
}

 function onNewPOS(){    
	     document.form.TRANID.value="";
	     document.form.FROM_LOC.value="";
	     document.form.TOLOC.value="";
	     document.form.EMP_ID.value="";
	     document.form.EMP_NAME.value="";
	     document.form.REASONCODE.value="";
	     document.form.TRANSACTIONDATE.value="";
	     document.form.REMARKS.value="";
	     document.form.REFERENCENO.value="";
	     document.form.FROM_LOCDESC.value="";
		  document.form.TOLOCDESC.value="";
		 document.form.cmd.value="NewPOS" ;
		 clearProductDetails1();
	 
			var formData = $('form#dynamicprdswithoutpriceForm').serialize(); 

		    $.ajax({
		        type: 'get',
		        url: '/track/DynamicProductServlet?cmd=NewPOS',
		        dataType:'html',
		        data:  formData,
		       
		        success: function (data) {
		        	$('#appendbody').html(data); 
		            
		        },
		        error: function (data) {
		        	
		            alert(data.responseText);
		        }
		    });
		    //clearProductDetails();
		    // document.form.submit();
}


function isNumberKey(evt, element, id) {
	var charCode = (evt.which) ? evt.which : event.keyCode;
	if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45)){return false;}
  	return true;
}	