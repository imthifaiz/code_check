$(document).ready(function(){
	var plant = document.form.plant.value;
	
	/* To get the suggestion data for Currency */
	$("#ECURRENCY").typeahead({
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
//			return '<div onclick="setCurrencyid(\''+data.CURRENCY+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')"><p>'+data.CURRENCY+'</p></div>';
			return '<p>'+data.CURRENCY+'</p>';
			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	}).on('typeahead:select',function(event,selection){
		setCurrencyid(selection.CURRENCY,selection.CURRENCYID,selection.CURRENCYUSEQT);
	
	}).on('typeahead:change',function(event,selection){
		if($(this).val() == ""){
			$("input[name ='CURRENCYID']").val("");	
			clearConversion();
			
		}
	});
	
	/* Product Number Auto Suggestion */
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
			return '<div><p class="item-suggestion">'+data.ITEM+'</p></div>';
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
	
	/* Basic UOM Auto Suggestion */
	$('#Basicuom').typeahead({
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p><br/><p class="item-suggestion">DESC: '+data.UOMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="uomAddBtn footer"  data-toggle="modal" data-target="#prduomModal"  onclick="document.form.uomModal.value=\'basicuom\'"> <a href="#"> + Add UOM</a></div>');
		//	menuElement.after( "<div class=\"uomAddBtn footer\" ><a href=\"#\"  onClick=\"javascript:popUpWin('../jsp/list/uomlist_save.jsp?UOM="+form.UOM.value+"');return false;\"> + Add UOM</a></div>");
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
	
	/* Product Class Auto Suggestion */
	$('#PRD_CLS_DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_CLS_DESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRD_CLS_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_CLSMST);
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
				return '<div onclick="document.form.PRD_CLS_ID.value = \''+data.PRD_CLS_ID+'\'"><p class="item-suggestion">' + data.PRD_CLS_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_CLS_DESC+'</p></div>';
//				return '<div><p class="item-suggestion">'+data.PRD_CLS_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_CLS_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
		//	menuElement.after( "<div class=\"PRD_CLS_IDAddBtn footer\" ><a href=\"#\"  onClick=\"javascript:popUpWin('../jsp/list/classlist_save.jsp?prdclsid="+form.PRD_CLS_DESC.value+"');return false;\"> + Add Product Category</a></div>");
			menuElement.after( '<div class="PRD_CLS_IDAddBtn footer"  data-toggle="modal" data-target="#prdcatModal"> <a href="#"> + Add Product Category</a></div>');
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
			if($(this).val() == ""){
				document.form.PRD_CLS_ID.value = "";
				}
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				document.form.PRD_CLS_ID.value = "";
		});
	/* Product Department Auto Suggestion */
	$('#PRD_DEPT_DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_DEPT_DESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRD_DEPT_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_DEPTMST);
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
				return '<div onclick="document.form.PRD_DEPT_ID.value = \''+data.PRD_DEPT_ID+'\'"><p class="item-suggestion">' + data.PRD_DEPT_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_DEPT_DESC+'</p></div>';
//			return '<div><p class="item-suggestion">'+data.PRD_DEPT_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_DEPT_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="PRD_DEPT_IDAddBtn footer"  data-toggle="modal" data-target="#prddepModal"> <a href="#"> + Add Product Department</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.PRD_DEPT_IDAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.PRD_DEPT_IDAddBtn').hide();}, 180);
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			if($(this).val() == ""){
				document.form.PRD_DEPT_ID.value = "";
				document.form.PRD_DEPT_DESC.value = "";
				}
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.PRD_DEPT_ID.value = "";
				document.form.PRD_DEPT_DESC.value = "";
				}
		});
	
	
	
	/* Product Department Auto Suggestion */
	$('#DEPT_DISPLAY_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DEPT_DISPLAY_DESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_DEPT_DISPLAY_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_DEPTMST);
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
			return '<div><p class="item-suggestion">'+data.DEPT_DISPLAY_ID+'</p><br/><p class="item-suggestion">DESC: '+data.DEPT_DISPLAY_DESC+'</p></div>';
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
	/* Product Sub Category Auto Suggestion */
	$('#PRD_TYPE_DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_TYPE_DESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRD_TYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_TYPEMST);
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
				return '<div onclick="document.form.ARTIST.value = \''+data.PRD_TYPE_ID+'\'"><p class="item-suggestion">' + data.PRD_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_TYPE_DESC+'</p></div>';
//			return '<div><p class="item-suggestion">'+data.PRD_TYPE_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_TYPE_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}

		//	menuElement.after( "<div class=\"ARTISTAddBtn footer\" ><a href=\"#\"  onClick=\"javascript:popUpWin('../jsp/list/typelist_save.jsp?sArtist="+form.ARTIST.value+"');return false;\"> + Add Product Sub Category</a></div>");
			menuElement.after( '<div class="ARTISTAddBtn footer"  data-toggle="modal" data-target="#prdsubcatModal"> <a href="#"> + Add Product Sub Category</a></div>');
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
			if($(this).val() == ""){
				document.form.ARTIST.value = "";
				}
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				document.form.ARTIST.value = "";
		});
	
	/* Product Brand Auto Suggestion */
	$('#PRD_BRAND_DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_BRAND_DESC',  
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
				return '<div onclick="document.form.PRD_BRAND.value = \''+data.PRD_BRAND_ID+'\'"><p class="item-suggestion">' + data.PRD_BRAND_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
//			return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
			}    
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
		//	menuElement.after( "<div class=\"PRD_BRANDAddBtn footer\" ><a href=\"#\"  onClick=\"javascript:popUpWin('../jsp/list/brandlist_save.jsp?prdBrand="+form.PRD_BRAND.value+"');return false;\"> + Add Product Brand</a></div>");
			menuElement.after( '<div class="PRD_BRANDAddBtn footer"  data-toggle="modal" data-target="#prdbrandModal"> <a href="#"> + Add Product Brand</a></div>');
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
			if($(this).val() == ""){
				document.form.PRD_BRAND.value = "";
				}
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				document.form.PRD_BRAND.value = "";
		});
	
	/* HSCODE Auto Suggestion */
	$('#HSCODE').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'HSCODE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_HSCODE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.HSCODEMST);
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
			return '<div><p class="item-suggestion">'+data.HSCODE+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="HSCODEAddBtn footer"  data-toggle="modal" data-target="#prdhscodeModal"> <a href="#"> + Add HS Code</a></div>');
		//	menuElement.after( "<div class=\"HSCODEAddBtn footer\" ><a href=\"#\"  onClick=\"javascript:popUpWin('../jsp/list/hscodelist_save.jsp?hscode="+form.HSCODE.value+"');return false;\"> + Add HS Code</a></div>");
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
	
	/* COO Auto Suggestion */
	$('#COO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'COO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_COO_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.COOMST);
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
			return '<div><p class="item-suggestion">'+data.COO+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="cooAddBtn footer"  data-toggle="modal" data-target="#prdcooModal"> <a href="#"> + Add COO</a></div>');
	//		menuElement.after( "<div class=\"cooAddBtn footer\" ><a href=\"#\"  onClick=\"javascript:popUpWin('../jsp/list/coolist_save.jsp?coo="+form.COO.value+"');return false;\"> + Add COO</a></div>");
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
		}).on('typeahead:select',function(event,selection){
			cooCountryCurrency(selection.COO);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
					$("input[name='COOCURRENCY']").val("");
			}
		});
	
	/* Purchase UOM Auto Suggestion */
	$('#purchaseuom').typeahead({
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p><br/><p class="item-suggestion">DESC: '+data.UOMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="uomAddBtn footer"  data-toggle="modal" data-target="#prduomModal"  onclick="document.form.uomModal.value=\'purchaseuom\'"> <a href="#"> + Add UOM</a></div>');
	//		menuElement.after( "<div class=\"uomAddBtn footer\" ><a href=\"#\"  onClick=\"UOMpopUpwin('PURCHASEUOM');return false;\" onclick="document.productuomForm.uomModal.value=\'purchaseuom\'"> + Add UOM</a></div>");
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

	/* Sales UOM Auto Suggestion */
	$('#salesuom').typeahead({
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p><br/><p class="item-suggestion">DESC: '+data.UOMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="uomAddBtn footer"  data-toggle="modal" data-target="#prduomModal"  onclick="document.form.uomModal.value=\'salesuom\'"> <a href="#"> + Add UOM</a></div>');
		//	menuElement.after( "<div class=\"uomAddBtn footer\" ><a href=\"#\"  onClick=\"UOMpopUpwin('SALESUOM');return false;\"> + Add UOM</a></div>");
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

	/* Rental UOM Auto Suggestion */
	$('#rentaluom').typeahead({
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p><br/><p class="item-suggestion">DESC: '+data.UOMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="uomAddBtn footer"  data-toggle="modal" data-target="#prduomModal"  onclick="document.form.uomModal.value=\'rentuom\'"> <a href="#"> + Add UOM</a></div>');
		//	menuElement.after( "<div class=\"uomAddBtn footer\" ><a href=\"#\"  onClick=\"UOMpopUpwin('RENTALUOM');return false;\"> + Add UOM</a></div>");
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
	
	
	/* Inventory UOM Auto Suggestion */
	$('#inventoryuom').typeahead({
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
			return '<div><p class="item-suggestion">'+data.UOM+'</p><br/><p class="item-suggestion">DESC: '+data.UOMDESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="uomAddBtn footer"  data-toggle="modal" data-target="#prduomModal"  onclick="document.form.uomModal.value=\'inventuom\'"> <a href="#"> + Add UOM</a></div>');
	//		menuElement.after( "<div class=\"uomAddBtn footer\" ><a href=\"#\"  onClick=\"UOMpopUpwin('INVENTORYUOM');return false;\"> + Add UOM</a></div>");
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
		
		
		
		/* Supplier Auto Suggestion */
	$('#vendname').typeahead({
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
				PLANT : plant,
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
			return '<div onclick="document.form.vendno.value = \''+data.VENDO+'\'"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
		    }
		  }
		}).on('typeahead:render',function(event,selection){
			
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#" onclick="document.form.custModal.value=\'cust\'"> + New Supplier</a></div>');
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
				document.form.vendno.value = "";
			}
		});
	
	removeSuggestionSearch();
	addSuggestionSearch();
});

function addSuggestionSearch()
{
	var plant = document.form.plant.value;
	/* To get the suggestion data for Product */
	$(".supplierSearch").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'VENDO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
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
				return '<div><p class="item-suggestion">'+data.VENDO+'</p><br/><p class="item-suggestion">DESC: '+data.VNAME+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="supplierAddBtn1 footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + Add Supplier</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	
	/* Customer Auto Suggestion */
	$('.customerSearch').typeahead({
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
		
		
		/* Supplier Auto Suggestion */
	$('.vendorSearch').typeahead({
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
				PLANT : plant,
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
			return '<div onclick="document.form.vendno.value = \''+data.VENDO+'\'"><p class="item-suggestion">Name: ' + data.VNAME + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><p class="item-suggestion pull-right">Currency: ' + data.CURRENCY + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
		    }
		  }
		}).on('typeahead:render',function(event,selection){
			
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
//			menuElement.after( '<div class="itemsupplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#" onclick="document.form.custModal.value=\'cust\'"> + New Supplier</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
//			$('.itemsupplierAddBtn').show();
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",true);
//			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 150);
//			setTimeout(function(){ $('.itemsupplierAddBtn').hide();}, 180);	
//			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
//			element.toggleClass("glyphicon-menu-up",false);
//			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
			}
		});
		
		/* Bank Name Auto Suggestion */
	$('#BANKNAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'NAME',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/BankServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT :"<%=plant%>",
						action : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
						NAME : query
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
		    	  return '<p onclick="document.form1.BRANCH.value = \''+data.BRANCH_NAME+'\'">'+data.NAME+'</p>';
			}
		  }

		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
			
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			$('.employeeAddBtn').show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.employeeAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});


	/* To get the suggestion data for Currency */
	$("#SUP_CURRENCY").typeahead({
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
			return '<div onclick="document.form1.CURRENCYID_S.value = \''+data.CURRENCYID+'\'"> <p>'+data.CURRENCY+'</p></div>';

			}
		  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){

	}).on('typeahead:change',function(event,selection){
		if($(this).val() == "")
			$("input[name ='CURRENCYID_S']").val("");	
	});

}


function addRowDesc(tableID,footer) {
	
  	
	var table = document.getElementById(tableID);
	
	var rowCount = table.rows.length;
	if(rowCount!=10)
		{
	var row = table.insertRow(rowCount);
	var newRowCount = rowCount + 1; 
	
	var itemCell = row.insertCell(0);
	var itemCellText =  "<label for=\"Detail Description"+rowCount+"\">Detail Description "+newRowCount+"\</label>&nbsp; ";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"col-sm-10\"><div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" style=\"left: 910px;\" aria-hidden=\"true\" onclick=\"deleteRowDesc('descriptiontbl');return false;\"></span><INPUT class=\"form-control\" name=\"DESCRIPTION\" ";
	itemCellText = itemCellText+ " id=\"DESCRIPTION"+newRowCount+"\" type = \"TEXT\" size=\"100\" value=\""+footer+"\" onkeypress=\"return blockSpecialCharcter(event)\" placeholder=\"Max 1000 Characters\" MAXLENGTH=\"1000\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
		}
	else
		{
		alert("Can not add more then 10 detail description ");
		}
}
function deleteRowDesc(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}

function addRowPrd(tableID,footer) {
	
  	
	var table = document.getElementById(tableID);
	
	var rowCount = table.rows.length;
	if(rowCount!=10)
		{
	var row = table.insertRow(rowCount);
	var newRowCount = rowCount + 1; 
	
	var itemCell = row.insertCell(0);
	var itemCellText =  "<label for=\"Product"+rowCount+"\">Product "+newRowCount+"\</label>&nbsp; ";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"col-sm-6\"><div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" style=\"left: 558px;\" aria-hidden=\"true\" onclick=\"deleteRowPrd('prdtbl');return false;\"></span><INPUT class=\"form-control itemSearch\" name=\"PRODUCT\" ";
	itemCellText = itemCellText+ " id=\"PRODUCT"+newRowCount+"\" onkeypress=\"return blockSpecialCharcter(event)\" type = \"TEXT\" size=\"100\" value=\""+footer+"\" placeholder=\"Select Product\" MAXLENGTH=\"200\"></div></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
		}
	else
		{
		alert("Can not add more then 10 Product ");
		}
	$(".itemSearch").typeahead('destroy');
	addSuggestionprd();
}

function deleteRowPrd(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}

function cooCountryCurrency(coo){	
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_COO_CURRENCY_CODE",
			COUNTRY : coo,
		},
		success : function(dataitem) {
			var cur=dataitem.CURRENCY;
	 		$("input[name='COOCURRENCY']").val(cur);
		}
	});	
}

function checkImageSize(names,file) {
var fileInput = document.getElementById(names);
  if (file.size > 250000) {
	  var plant = $("input[name=PLANT]").val();
	  if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' 
	  || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' 
	  || plant=='C947002346S2T')){
    alert("Image size should be less than 250 Pixel");
	fileInput.value = "";
    return false;
		  }
  }
  return true;
}

function addSuggestionprd()
{
	var plant = document.form.plant.value;
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
				ACTION : "GET_PRODUCT_LIST_SUGGESTION",
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
				return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Brand:'+data.PRD_BRAND_ID+'</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right"> Category:'+data.PRD_CLS_ID+'</p></div>';
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
		}).on('typeahead:select',function(event,selection){
			$(this).closest('tr').find("input[name ='ITEMDES']").val(selection.ITEMDESC);
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".bigdrop");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
// 					$(this).closest('tr').find('input[name="ITEMDES"]').val("");
			}
		});

}

function checkAll(isChk)
{
	var len = document.form.checkoutlet.length;
 	var orderLNo;
 	if(len == undefined) len = 1;
	if (document.form.checkoutlet){
        for (var i = 0; i < len ; i++){
			if(len == 1){
				document.form.checkoutlet.checked = isChk;
			}
			else{
				document.form.checkoutlet[i].checked = isChk;
			}
        }
    }
}

function checkAllout(isChk)
{
	var len = document.form.checkoutlets.length;
 	var orderLNo;
 	if(len == undefined) len = 1;
	if (document.form.checkoutlets){
        for (var i = 0; i < len ; i++){
			if(len == 1){
				document.form.checkoutlets.checked = isChk;
			}
			else{
				document.form.checkoutlets[i].checked = isChk;
			}
        }
    }
}

function checkprice(obj){
	var checkedFlag = false;
	if($(obj).closest('tr').find('input[name=checkoutlet]').is(':checked')){
			checkedFlag = true;
	}
		if(checkedFlag){
		//var price = $(obj).closest('tr').find("input[name=newprice]").val();
		//$(obj).val(parseFloat(price).toFixed(2));
		return false;
	}else{
		alert ("Please select checkbox.");
	}
}

function isChecked(obj){
	 if ($(obj).is(":checked")){
		 $(obj).closest('tr').find("input[name=checkoutlet]").val("1");
	 }else{
		 $(obj).closest('tr').find("input[name=checkoutlet]").val("0");
	 }
}
function SuppDiscountType(obj){
var rowcount=0;
var disPerValue =100;
var purchasePrice = $('input[name ="COST"]').val();
purchasePrice =  parseFloat(purchasePrice);
	$("#supplierDiscount tr").each(function() {
		var Discount = $("input[id=DYNAMIC_SUPPLIER_DISCOUNT_"+rowcount+"]").val();
		Discount =  parseFloat(Discount);
			if(obj=='BYCOST'){
				if(purchasePrice <= Discount){ 
				alert('Discount should be less than Cost');
				Discount = $("input[id=DYNAMIC_SUPPLIER_DISCOUNT_"+rowcount+"]").val('');
				deleteRowCost('supplierDiscount');
				}
			}else{
				if(disPerValue <= Discount){ 
				alert('Discount should be less than Cost');
				Discount = $("input[id=DYNAMIC_SUPPLIER_DISCOUNT_"+rowcount+"]").val('');
				deleteRowCost('supplierDiscount');
				}
			}
		rowcount=rowcount+1;
	});
}

function addItemSupplieredit(tableID,vendNo) {
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	var form = document.forms['form'];
	var itemCell = row.insertCell(0);
	var itemCellText =  "<div class=\"input-group\"> <span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteItemSupplieredit('multiitemsup');return false;\"></span> <INPUT class=\"form-control vendorSearch\" name=\"ITEMSUPPLIER_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"ITEMSUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"55\"   value=\""+vendNo+"\" placeholder=\"Select Supplier\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateItemSupplier("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
	removeSuggestionSearch();
	addSuggestionSearch();
}

function deleteItemSupplieredit(tableID) {
	try {
	var table = document.getElementById(tableID);	
	var rowCount = table.rows.length;
	rowCount = rowCount*1 - 1;
	if(rowCount==0){
		alert("Can not remove the default Item");
	}else{
		table.deleteRow(rowCount);
	}
	}catch(e) {
		alert(e);
	}
}


function loadItemSupplier(productId) {
	var urlStr = "/track/ItemMstServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ITEM : productId,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_ITEM_SUPPLIER_DETAILS"
			},
			
			dataType : "json",
			success : formatItemSupplierData
			
		});
	}
	
function formatItemSupplierData(data) {
	 console.log('loading supplier data');
	 console.log(data);
	var ii = 0;
	var errorBoo = false;
	if (data.status == "9") {
		errorBoo = true;
	}
	if (!errorBoo) {
		var table = document.getElementById('multiitemsup');
		var rowCount = table.rows.length;
		rowCount = rowCount * 1 - 1;
		for(; rowCount>=0; rowCount--) {
			table.deleteRow(rowCount);
		}
		 $.each(data.items, function(i,item){
	    	       addItemSupplieredit('multiitemsup',item.VENDNO);
	    	       setCheckedValue('',item.VENDNO); 	
	     });
		 if (data.items == null || data.items.length == 0){
			 addItemSupplieredit('multiitemsup','','');
	     }
	} else {
		//alert("No Data found....!!!!");
	}
}

function CusDiscountType(obj){
var rowcount=0;
var disPerValue =100;
var salesPrice = $('input[name ="PRICE"]').val();
salesPrice =  parseFloat(salesPrice);
	$("#customerDiscount tr").each(function() {
		var Discount = $("input[id=DYNAMIC_CUSTOMER_DISCOUNT_"+rowcount+"]").val();
		Discount =  parseFloat(Discount);
			if(obj=='BYPRICE'){
				if(salesPrice <= Discount){ 
				alert('Discount should be less than Cost');
				Discount = $("input[id=DYNAMIC_CUSTOMER_DISCOUNT_"+rowcount+"]").val('');
				deleteRowCost('customerDiscount');
				}
			}else{
				if(disPerValue <= Discount){ 
				alert('Discount should be less than Cost');
				Discount = $("input[id=DYNAMIC_CUSTOMER_DISCOUNT_"+rowcount+"]").val('');
				deleteRowCost('customerDiscount');
				}
			}
		rowcount=rowcount+1;
	});
}

function calculateSuppDiscount(obj){
	var rowcount=0;
	var disPerValue =100;
	var purchasePrice = $('input[name ="COST"]').val();
	purchasePrice =  parseFloat(purchasePrice);
	var DisType = $('input[name ="IBDISCOUNT"]').val();
	var DiscountType = $('input[name ="SUPDISCOUNTTYPE"]').val();
	var Type = $('input[name ="TYPE"]').val();
		if(DiscountType==''){
			DiscountType = DisType;
			DisType = DiscountType;
		}else{
			DisType = DiscountType;
		}
	var Discount = $(obj).closest('tr').find("input[id=DYNAMIC_SUPPLIER_DISCOUNT_0]").val();
	Discount =  parseFloat(Discount);
		if(purchasePrice=='0.00'||purchasePrice==''){
			alert('Please Enter Cost to Apply Discount');
			$(obj).closest('tr').find("input[id=DYNAMIC_SUPPLIER_DISCOUNT_0]").val('');
			document.getElementById("COST").focus();
			document.getElementById("COST").select();
			return false;
		}
		if(DisType=='BYCOST'){
			if(purchasePrice <= Discount){ 
				alert('Discount should be less than Cost');
				$(obj).closest('tr').find("input[id=DYNAMIC_SUPPLIER_DISCOUNT_"+rowcount+"]").val('');
				}
		}else{
			if(disPerValue <= Discount){ 
				alert('Discount should be less than Cost');
				$(obj).closest('tr').find("input[id=DYNAMIC_SUPPLIER_DISCOUNT_"+rowcount+"]").val('');
				}
		}
	$("#supplierDiscount tr").each(function() {
			rowcount=rowcount+1;
			Discount = $(obj).closest('tr').find("input[id=DYNAMIC_SUPPLIER_DISCOUNT_"+rowcount+"]").val();
			if(DisType=='BYCOST'){
				if(purchasePrice <= Discount){ 
					alert('Discount should be less than Cost');
					$(obj).closest('tr').find("input[id=DYNAMIC_SUPPLIER_DISCOUNT_"+rowcount+"]").val('');
					deleteRowCost('supplierDiscount');
				}
			}else{
				if(disPerValue <= Discount){ 
					alert('Discount should be less than Cost');
					$(obj).closest('tr').find("input[id=DYNAMIC_SUPPLIER_DISCOUNT_"+rowcount+"]").val('');
					deleteRowCost('supplierDiscount');
				}
			}
	});
}

function calculateCustDiscount(obj){
	var rowcount=0;
	var disPerValue =100;
	var salesPrice = $('input[name ="PRICE"]').val();
	salesPrice =  parseFloat(salesPrice);
	var DisType = $('input[name ="OBDISCOUNT"]').val();
	var DiscountType = $('input[name ="CUSDISCOUNTTYPE"]').val();
	var Type = $('input[name ="TYPE"]').val();
		if(DiscountType==''){
			DiscountType = DisType;
			DisType = DiscountType;
		}else{
			DisType = DiscountType;
		}
	var Discount = $(obj).closest('tr').find("input[id=DYNAMIC_CUSTOMER_DISCOUNT_0]").val();
	Discount =  parseFloat(Discount);
		if(salesPrice=='0.00'||salesPrice==''){
			alert('Please Enter List Price to Apply Discount');
			$(obj).closest('tr').find("input[id=DYNAMIC_CUSTOMER_DISCOUNT_0]").val('');
			document.getElementById("PRICE").focus();
			document.getElementById("PRICE").select();
			return false;
		}
		if(DisType=='BYPRICE'){
			if(salesPrice <= Discount){ 
				alert('Discount should be less than List Price');
				$(obj).closest('tr').find("input[id=DYNAMIC_CUSTOMER_DISCOUNT_"+rowcount+"]").val('');
				}
		}else{
			if(disPerValue <= Discount){ 
				alert('Discount should be less than List Price');
				$(obj).closest('tr').find("input[id=DYNAMIC_CUSTOMER_DISCOUNT_"+rowcount+"]").val('');
				}
		}
		
	$("#customerDiscount tr").each(function() {
			rowcount=rowcount+1;
			Discount = $(obj).closest('tr').find("input[id=DYNAMIC_CUSTOMER_DISCOUNT_"+rowcount+"]").val();
			if(DisType=='BYPRICE'){
				if(salesPrice <= Discount){ 
					alert('Discount should be less than List Price');
					$(obj).closest('tr').find("input[id=DYNAMIC_CUSTOMER_DISCOUNT_"+rowcount+"]").val('');
					deleteRow('customerDiscount');
				}
			}else{
				if(disPerValue <= Discount){ 
					alert('Discount should be less than List Price');
					$(obj).closest('tr').find("input[id=DYNAMIC_CUSTOMER_DISCOUNT_"+rowcount+"]").val('');
					deleteRow('customerDiscount');
				}
			}
	});
}

function removeSuggestionSearch()
{
	$(".supplierSearch").typeahead('destroy');
	$(".customerSearch").typeahead('destroy');
	$(".itemSearch").typeahead('destroy');
	$(".vendorSearch").typeahead('destroy');
}

function enableSellingPrice(obj){

}


function productdepartmentCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=PRD_DEPT_DESC]").typeahead ('val',data.PRD_DEPT_DESC);
		$("input[name ='PRD_DEPT_ID']").val(data.PRD_DEPT_ID);
//		$("input[name=PRODUCTDEPARTMENTDESC]").typeahead ('val',data.PRD_DEPT_DESC);
	}
}
function productcategoryCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=PRD_CLS_DESC]").typeahead ('val',data.PRD_CLS_DESC);
		$("input[name ='PRD_CLS_ID']").val(data.PRD_CLS_ID);
	}
}
function productsubcategoryCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=PRD_TYPE_DESC]").typeahead ('val',data.PRD_TYPE_DESC);
		$("input[name ='ARTIST']").val(data.PRD_TYPE_ID);
	}
}

function productbrandCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=PRD_BRAND_DESC]").typeahead ('val',data.PRD_BRAND_DESC);
		$("input[name ='PRD_BRAND']").val(data.PRD_BRAND_ID);
	}
}

function productlocationCallback(data){
		if(data.STATUS="100"){
		$("input[name=LOC_ID]").typeahead ('val',data.LOC);
			$("input[name ='LOC_DESC']").val(data.LOCDESC);
		}
	}

function producthscodeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#HSCODE").typeahead('val', data.HSCODE);
			}
}
function productcooCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#COO").typeahead('val', data.COO);
			}
}
function productuomCallback(data){
	if(data.STATUS="SUCCESS"){
	if(document.form.uomModal.value=="basicuom"){
		$("#Basicuom").typeahead('val', data.UOMCODE);
		}else if(document.form.uomModal.value=="purchaseuom"){
		$("input[name ='PURCHASEUOM']").typeahead('val', data.UOMCODE);
		}else if (document.form.uomModal.value=="salesuom"){
		$("input[name ='SALESUOM']").typeahead('val', data.UOMCODE);
		}else if(document.form.uomModal.value=="inventuom"){
		$("input[name ='INVENTORYUOM']").typeahead('val', data.UOMCODE);
		}
		else{
		$("#rentaluom").typeahead('val', data.UOMCODE);
		}
	}
	}
	

function paymentTypeCallback(data){
	if(data.STATUS="SUCCESS"){
//		$("#payment_type").typeahead('val', data.PAYMENTMODE);
//		$("#payment_type").typeahead('val', data.PAYMENTTYPE);
	if(document.form.custModal.value=="shipcust"){
		$("#CUST_PAYTERMS").typeahead('val', data.PAYMENTMODE);
		}else if(document.form.custModal.value=="cust"){
		$("#PAYTERMS").typeahead('val', data.PAYMENTMODE);
		}else{
		$("#payment_type").typeahead('val', data.PAYMENTMODE);
		}
	}
}

function transportCallback(data){
	if(data.STATUS="SUCCESS"){
	if(document.form.custModal.value=="shipcust"){
		$("#transportC").typeahead('val', data.transport);
		$("input[name=TRANSPORTIDC]").val(data.TRANSPORTID);
		}else if(document.form.custModal.value=="cust"){
		$("#transports").typeahead('val', data.transport);
		$("input[name=TRANSPORTSID]").val(data.TRANSPORTID);
		}else{
		$("#transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
		}
	}
}

function payTermsCallback(payTermsData){
	if(payTermsData.STATUS="SUCCESS"){
//		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
//		}
	if(document.form.custModal.value=="shipcust"){
		$("input[name ='payment_term']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=PMENT_DAYS]").val(payTermsData.NO_DAYS);
		}else if(document.form.custModal.value=="cust"){
		$("input[name ='sup_payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=SUP_PMENT_DAYS]").val(payTermsData.NO_DAYS);
		}else{
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		}
	}
}

function bankCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#BANKNAME").typeahead('val', data.BANK_NAME);
		$("#BANKNAMECUS").typeahead('val', data.BANK_NAME);
		  $("input[name ='BRANCH']").val(data.BANK_BRANCH);
		
	}
	}
	function readURLEdit(input) {
			    if (input.files && input.files[0]) {
			        var reader = new FileReader();
			        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
			        //alert(input.files[0].type);
			        if(allowedExtension.indexOf(input.files[0].type)>-1)
			        {
			        } else {
			       		//Validate Image -Azees 02.02.23
			             alert('Upload valid image file');
			             $('#productImg').val('');
			             return false;
			    	}
			        reader.onload = function (e) {
			        	var image = new Image();
			        	image.src = e.target.result;
			        	//Validate the File Height and Width.250px -Azees 02.02.23
			        	  image.onload = function () {
			        	    var height = this.height;
			        	    var width = this.width;
			        	    /*var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
			        	    if (height > 250 || width > 250) {
			        	    $('#productImg').val('');
			        	    alert("Image size should be less than 250 Pixel");
			        	      return false;
			        	    }
			        	    }*/
			        	    //alert("Uploaded image has valid Height and Width.");
				            //$('#item_img').attr('src', e.target.result);
				            //$('#item_img0').attr('src', e.target.result);
			        	    return true;
			        	  };
			        }

			        reader.readAsDataURL(input.files[0]);
			    }
			}
			function readURLEdit1(input) {
			    if (input.files && input.files[0]) {
			        var reader = new FileReader();
			        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
			        if(allowedExtension.indexOf(input.files[0].type)>-1)
			        {
			        } else {
			       		//Validate Image -Azees 02.02.23
			             alert('Upload valid image file');
			             $('#productImg_2').val('');
			             return false;
			    	}
			        reader.onload = function (e) {
			        	
			        	var image = new Image();
			        	image.src = e.target.result;
			        	//Validate the File Height and Width.250px -Azees 02.02.23
			        	  image.onload = function () {
			        	    var height = this.height;
			        	    var width = this.width;
			        	    var plant = $("input[name=PLANT]").val();
			        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
			        	    if (height > 250 || width > 250) {
			        	    $('#productImg_2').val('');
			        	      alert("Height and Width must not exceed 250px.");
			        	      return false;
			        	    }
			        	    }
			            	$('#item_img1').attr('src', e.target.result);
			        	    return true;
			        	  };
			        }

			        reader.readAsDataURL(input.files[0]);
			    }
			}
			function readURLEdit2(input) {
			    if (input.files && input.files[0]) {
			        var reader = new FileReader();
			        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
			        if(allowedExtension.indexOf(input.files[0].type)>-1)
			        {
			        } else {
			       		//Validate Image -Azees 02.02.23
			             alert('Upload valid image file');
			             $('#productImg_3').val('');
			             return false;
			    	}
			        reader.onload = function (e) {
			        	
			        	var image = new Image();
			        	image.src = e.target.result;
			        	//Validate the File Height and Width.250px -Azees 02.02.23
			        	  image.onload = function () {
			        	    var height = this.height;
			        	    var width = this.width;
			        	    var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
			        	    if (height > 250 || width > 250) {
			        	    $('#productImg_3').val('');
			        	      alert("Height and Width must not exceed 250px.");
			        	      return false;
			        	    }
			        	    }
			            	$('#item_img2').attr('src', e.target.result);
			        	    return true;
			        	  };
			        }

			        reader.readAsDataURL(input.files[0]);
			    }
			}
			function readURLEdit3(input) {
			    if (input.files && input.files[0]) {
			        var reader = new FileReader();
			        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
			        if(allowedExtension.indexOf(input.files[0].type)>-1)
			        {
			        } else {
			       		//Validate Image -Azees 02.02.23
			             alert('Upload valid image file');
			             $('#productImg_4').val('');
			             return false;
			    	}
			        reader.onload = function (e) {
			        	
			        	var image = new Image();
			        	image.src = e.target.result;
			        	//Validate the File Height and Width.250px -Azees 02.02.23
			        	  image.onload = function () {
			        	    var height = this.height;
			        	    var width = this.width;
			        	    var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
			        	    if (height > 250 || width > 250) {
			        	    $('#productImg_4').val('');
			        	      alert("Height and Width must not exceed 250px.");
			        	      return false;
			        	    }
			        	    }
			            	$('#item_img3').attr('src', e.target.result);
			        	    return true;
			        	  };
			        }

			        reader.readAsDataURL(input.files[0]);
			    }
			}
			function readURLEdit4(input) {
			    if (input.files && input.files[0]) {
			        var reader = new FileReader();
			        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
			        if(allowedExtension.indexOf(input.files[0].type)>-1)
			        {
			        } else {
			       		//Validate Image -Azees 02.02.23
			             alert('Upload valid image file');
			             $('#productImg_5').val('');
			             return false;
			    	}
			        reader.onload = function (e) {
			        	
			        	var image = new Image();
			        	image.src = e.target.result;
			        	//Validate the File Height and Width.250px -Azees 02.02.23
			        	  image.onload = function () {
			        	    var height = this.height;
			        	    var width = this.width;
			        	    var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
			        	    if (height > 250 || width > 250) {
			        	    $('#productImg_5').val('');
			        	      alert("Height and Width must not exceed 250px.");
			        	      return false;
			        	    }
			        	    }
			            	$('#item_img4').attr('src', e.target.result);
			        	    return true;
			        	  };
			        }

			        reader.readAsDataURL(input.files[0]);
			    }
			}
			function readURLEdit5(input) {
			    if (input.files && input.files[0]) {
			        var reader = new FileReader();
			        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
			        if(allowedExtension.indexOf(input.files[0].type)>-1)
			        {
			        } else {
			       		//Validate Image -Azees 02.02.23
			             alert('Upload valid image file');
			             $('#productImg_6').val('');
			             return false;
			    	}
			        reader.onload = function (e) {
			        	
			        	var image = new Image();
			        	image.src = e.target.result;
			        	//Validate the File Height and Width.250px -Azees 02.02.23
			        	  image.onload = function () {
			        	    var height = this.height;
			        	    var width = this.width;
			        	    var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
			        	    if (height > 250 || width > 250) {
			        	    $('#productImg_6').val('');
			        	      alert("Height and Width must not exceed 250px.");
			        	      return false;
			        	    }
			        	    }
			            	$('#item_img5').attr('src', e.target.result);
			        	    return true;
			        	  };
			        }

			        reader.readAsDataURL(input.files[0]);
			    }
			}
			
function checkcurrency(currency){	
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
						document.getElementById("ECURRENCY").focus();
						$("#ECURRENCY").typeahead('val', '');
						return false;	
					} 
					else 
						return true;
				}
			});
		 return true;
}
			
function setCurrencyid(CURRENCY,CURRENCYID,CURRENCYUSEQT){	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="SELCUR"]').val(CURRENCYID);
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	$("#ECURRENCY").typeahead('val', CURRENCY);
	currencyEQ(CURRENCYUSEQT);
//	if (document.getElementById("APPLYCONPRICE").checked == true) {}
	if ($('#APPLYCONPRICE').is(":checked")) {
		var CONPRICE = $('input[name ="CONPRICE"]').val();
		if(CONPRICE!='')
		$('input[name ="COST"]').val(CONPRICE);
    }
}		

function currencyEQ(CURRENCYUSEQT){
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	$('input[name ="CURRENCYUSEQT"]').val(CURRENCYUSEQT);
	var curval = $('input[name ="CURPRICE"]').val();
	curval = parseFloat(curval).toFixed(numberOfDecimal);
	var ecrate = $('input[name ="CURRENCYUSEQT"]').val();
	var conv = parseFloat(curval)/parseFloat(ecrate);
	conv = parseFloat(conv).toFixed(numberOfDecimal);
	if(isNaN(curval))curval = "";
	if(isNaN(conv))conv = "";
	$('input[name ="CURPRICE"]').val(curval);
	$('input[name ="CONPRICE"]').val(conv);
	if ($('#APPLYCONPRICE').is(":checked")) {
		var CONPRICE = $('input[name ="CONPRICE"]').val();
		if(CONPRICE!='')
		$('input[name ="COST"]').val(CONPRICE);
    }
}
	
function currencyconv(price){
	var numberOfDecimal = document.getElementById("numberOfDecimal").value;
	$('input[name ="CURPRICE"]').val(price);
	var curval = $('input[name ="CURPRICE"]').val();
	curval = parseFloat(curval).toFixed(numberOfDecimal);
	var ecrate = $('input[name ="CURRENCYUSEQT"]').val();
	var conv = parseFloat(curval)/parseFloat(ecrate);
	conv = parseFloat(conv).toFixed(numberOfDecimal);
	if(isNaN(curval))curval = "";
	if(isNaN(conv))conv = "";
	$('input[name ="CURPRICE"]').val(curval);
	$('input[name ="CONPRICE"]').val(conv);
	if ($('#APPLYCONPRICE').is(":checked")) {
		var CONPRICE = $('input[name ="CONPRICE"]').val();
		if(CONPRICE!='')
		$('input[name ="COST"]').val(CONPRICE);
    }
}	

function Isapplyconprice(obj){
	var CONPRICE = "0.00";
	 if ($(obj).is(":checked")){
		CONPRICE = $('input[name ="CONPRICE"]').val();
		if(CONPRICE!='')
		 $('input[name ="COST"]').val(CONPRICE);
	 }else{
		 $('input[name ="COST"]').val('0.00');
	 }
}
	
function clearConversion(){
	$('input[name ="CURPRICE"]').val('');
	$('input[name ="CURRENCYUSEQT"]').val('');
	$('input[name ="CONPRICE"]').val('');
	$('input[name ="SELCUR"]').val('');
}	




