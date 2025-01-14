<%@ page import="com.track.gates.DbBean"%>

<div id="multicompanyinventoryModal" class="modal fade" role="dialog">
    <div class="modal-dialog modal-md" style="width: 800px">
        <form name="MultiCompanyInventoryForm" method="post">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">Multiple Company Inventory Summary With Movement</h4>
                </div>

                <div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 ">Company:</label>
		      			<div class="col-sm-6">
				        		<input type="text"
											class="ac-selected form-control typeahead"
											id="parent_company" name="PLANT_COM_NAME"
											placeholder="Select Company">
								<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeparent_company(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>			
									<input type="hidden" name="ITEM_COM">		
									<input type="hidden" name="PLANT_COM">		
			        	</div>
		      		</div>
				</div>

                
                
                 <div id="VIEW_RESULT_HERE" class="table-responsive">
                <div id="tableInventorySummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
                    <div class="row">
                        <div class="col-sm-12">
                            <table id="tableCompanyInventorySummary" class="table table-bordred table-striped">
                                <thead>
                                    <tr role="row">
                                        <th style="font-size: smaller;">LOC</th>
                                        <th style="font-size: smaller;">LOC DESC</th>
                                       
                                        <th style="font-size: smaller;">PRODUCT</th>
                                        <th style="font-size: smaller;">PRODUCT DESC</th>
                                        <th style="font-size: smaller;">CATEGORY</th>
                                        <th style="font-size: smaller;">SUB CATEGORY</th>
                                        <th style="font-size: smaller;">BRAND</th>
                                      
                                        <th style="font-size: smaller;">INV.UOM</th>
                                        <th style="font-size: smaller;">INV.QTY</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Data rows will be dynamically generated here -->
                                </tbody>
                                <tfoot style="display:none">
                                    <tr class="group">
                                        <th></th>
                                        <th></th>
                                       
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                        <th></th>
                                        <th style="text-align: left !important">Total</th>
                                        
                                        <th></th>
                                        <th style="text-align: right !important"></th>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-12">
                            <table id="tableCompanyInventorySummarySM" class="table table-bordred table-striped">
                                <thead>
                                    <tr role="row">
                                        <th style="font-size: smaller;">TYPE</th>
                                        <th style="font-size: smaller;">UOM</th>
                                        <th style="font-size: smaller;">IN QTY</th>
                                        <th style="font-size: smaller;">OUT QTY</th>
                                        <th style="font-size: smaller;">BALANCE QTY</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Data rows will be dynamically generated here -->
                                </tbody>
                                <tfoot style="display:none">
                                    <tr class="group">                                       
                                        <th style="text-align: left !important">Total</th>
                                        <th></th>
                                        <th style="text-align: right !important"></th>
                                        <th style="text-align: right !important"></th>
                                        <th style="text-align: right !important"></th>
                                    </tr>
                                </tfoot>
                            </table>
                        </div>
                    </div>
                    
                    
                </div>
            </div>

            </div>
            <div id="spinnerImg" ></div>
        </form>
        <script type="text/javascript">
  var tableCompanyInventorySummary;
  var tableCompanyInventorySummarySM;
  var productId, desc, loc, prdBrand, prdClass,prdDep,prdType, locType,locType2,locType3,model,uom,SORT,plant, groupRowColSpan1 = 6;
  function getParameter1(){
  	return { 
  		"ITEM": productId,
  		"PRD_DESCRIP":desc,
  		"LOC":loc,
  		"PRD_BRAND_ID":prdBrand,
  		"PRD_CLS_ID":prdClass,
  		"PRD_DEPT_ID":prdDep,
  		"PRD_TYPE_ID":prdType,
  		"LOC_TYPE_ID":locType, 
  		"LOC_TYPE_ID2":locType2, 
  		"LOC_TYPE_ID3":locType3, 
  		"MODEL": model,
  		"UOM": uom,
  		"SORT": SORT,
  		"ACTION": "VIEW_INV_SUMMARY_GROUPBY_PRD_MULTIUOM",
  		"PLANT":plant
  	}
  } 
  function getParameter2(){
  	return { 
  		"ITEM": productId,
  		"PRD_DESCRIP":desc,
  		"LOC":loc,
  		"PRD_BRAND_ID":prdBrand,
  		"PRD_CLS_ID":prdClass,
  		"PRD_DEPT_ID":prdDep,
  		"PRD_TYPE_ID":prdType,
  		"LOC_TYPE_ID":locType, 
  		"LOC_TYPE_ID2":locType2, 
  		"LOC_TYPE_ID3":locType3, 
  		"MODEL": model,
  		"UOM": uom,
  		"SORT": SORT,
  		"ACTION": "VIEW_INV_SUMMARY_GROUPBY_PRD_MOVEMENT",
  		"PLANT":plant
  	}
  } 



function onCompanyLoad(){
	 productId = document.MultiCompanyInventoryForm.ITEM_COM.value;
	 plant = document.MultiCompanyInventoryForm.PLANT_COM.value;
  
  
    var urlStr = "../InvMstServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 1;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableCompanyInventorySummary){
    	tableCompanyInventorySummary.ajax.url( urlStr ).load();
    }else{
    	tableCompanyInventorySummary = $('#tableCompanyInventorySummary').DataTable({
			"processing": true,
			"lengthMenu": [[10, 100, 500, 1000], [10, 100, 500, 1000]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameter1()) ? d : getParameter1();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].ITEM === 'undefined'){
		        		return [];
		        	}else {
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
    			{"data": 'LOC', "orderable": true},
    			{"data": 'LOCDESC', "orderable": true},
    		
    			{"data": 'ITEM', "orderable": true},
    			{"data": 'ITEMDESC', "orderable": true},
    			{"data": 'PRDCLSID', "orderable": true},
    			{"data": 'ITEMTYPE', "orderable": true},
    			{"data": 'PRDBRANDID', "orderable": true},
    			
    			{"data": 'INVENTORYUOM', "orderable": true},
    			{"data": 'INVUOMQTY', "orderable": true}
    			
    			],
			"columnDefs": [{"className": "t-right", "targets": [8]}],
			//"orderFixed": [ groupColumn, 'asc' ], 
			"orderFixed": [ ],
			/*"dom": 'lBfrtip',*/
			"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
			"<'row'<'col-md-6'><'col-md-6'>>" +
			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
			buttons: [],
	        "order":[],
	       
			
		});
    }
    onCompanyLoadSM();
}

function onCompanyLoadSM(){
	 productId = document.MultiCompanyInventoryForm.ITEM_COM.value;
	 plant = document.MultiCompanyInventoryForm.PLANT_COM.value;
  
  
    var urlStr = "../InvMstServlet";
   	// Call the method of JQuery Ajax provided
   	var groupColumn = 1;
   	var totalQty = 0;
    // End code modified by Deen for product brand on 11/9/12
    if (tableCompanyInventorySummarySM){
    	tableCompanyInventorySummarySM.ajax.url( urlStr ).load();
    }else{
    	tableCompanyInventorySummarySM = $('#tableCompanyInventorySummarySM').DataTable({
			"processing": true,
			"lengthMenu": [[10, 100, 500, 1000], [10, 100, 500, 1000]],
// 			"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
			"ajax": {
				"type": "POST",
				"url": urlStr,
				"data": function(d){
					return jQuery.isEmptyObject(getParameter2()) ? d : getParameter2();
				}, 
				"contentType": "application/x-www-form-urlencoded; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.items[0].TYPE === 'undefined'){
		        		return [];
		        	}else {
		        		return data.items;
		        	}
		        }
		    },
	        "columns": [
	        	{"data": 'TYPE', "orderable": true},
				{"data": 'IN_UOM', "orderable": true},
				{"data": 'IN_QTY', "orderable": true},
				{"data": 'OUT_QTY', "orderable": true},
				{"data": 'BAL_INV_QTY', "orderable": false}
    			],
			"columnDefs": [{"className": "t-right", "targets": [4]}],
			//"orderFixed": [ groupColumn, 'asc' ], 
			"orderFixed": [ ],
			/*"dom": 'lBfrtip',*/
			"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
			"<'row'<'col-md-6'><'col-md-6'>>" +
			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
			buttons: [],
	        "order":[],
	       
			
		});
    }
}

$('#tableCompanyInventorySummary').on('column-visibility.dt', function(e, settings, column, state ){
	if (!state){
		groupRowColSpan1 = parseInt(groupRowColSpan1) - 1;
	}else{
		groupRowColSpan1 = parseInt(groupRowColSpan1) + 1;
	}
	$('#tableCompanyInventorySummary tr.group td:nth-child(1)').attr('colspan', groupRowColSpan1);
	$('#tableCompanyInventorySummary').attr('width', '100%');
});

	

function callback(data){
		
		var outPutdata = getTable();
		var ii = 0;
		var errorBoo = false;
		$.each(data.errors, function(i,error){
			if(error.ERROR_CODE=="99"){
				errorBoo = true;
				
			}
		});
		
		if(!errorBoo){
			
	        $.each(data.items, function(i,item){
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
                       
	        	outPutdata = outPutdata+item.PRODUCT
                        	ii++;
	            
	          });
		}else{
		//	outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="6"><BR><CENTER><B><FONT COLOR="RED">No details found!</FONT></B></CENTER></TD></TR>';
		}
        outPutdata = outPutdata +'</TABLE>';
        document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
         document.getElementById('spinnerImg').innerHTML ='';

     
   }

function getTable(){
            return '<TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">'+
                    '<TR BGCOLOR="#000066">'+
                    '<TH><font color="#ffffff" align="left" width = "15%"><b>Location</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Product ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>PRODUCT CATEGORY ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Product Sub Category ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Product Brand ID</TH>'+
                    '<TH><font color="#ffffff" align="left"><b>Description</TH>'+
                  
                    '<TH><font color="#ffffff" align="Right"><b>Inventory UOM</TH>'+
                    '<TH><font color="#ffffff" align="Right"><b>Inventory Quantity</TH>'+
                    '</TR>';
                
}
//document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
</SCRIPT>
    </div>
</div>
<script type="text/javascript">

$(document).ready(function(){
	//onGo();
	$('#parent_company').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{		  
		  display: 'NAME',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "../Parentchildcmp/GET_PARENT_CHILD_PLANT_DROPDOWN";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					CMD : "GET_PARENT_CHILD_PLANT_DROPDOWN",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					//alert(JSON.stringify(data));
					return asyncProcess(data.PLANTMST);
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
		    		return '<div onclick="document.MultiCompanyInventoryForm.PLANT_COM.value = \''+data.PLANT+'\'"><p class="item-suggestion">CODE :' + data.PLANT + '</p><br><p class="item-suggestion">NAME :' + data.NAME + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			//menuElement.after( '<div class="accountAddBtn footer salarytypepopup"  data-toggle="modal" data-target="#create_salary_type"><a href="#"> + New Salary Type</a></div>');
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
		}).on('typeahead:select',function(event,selection){
			onCompanyLoad();	
		});
	
});
function changeparent_company(obj){
	 $("#parent_company").typeahead('val', '"');
	 $("#parent_company").typeahead('val', '');
	 $("#parent_company").focus();
	}
</script>
