<div id="Additional_Expenses_inv_modal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h3 class="modal-title popaddexp">New Expenses</h3>
			</div>
			<div class="modal-body">
				<!-- <form id="createExpensesForm" class="form-horizontal" name="createExpensesForm"
					action="/track/AddExpensesServlet?Submit=Save" method="post"
					enctype="multipart/form-data" onsubmit="return validateExpenses()"> -->
					<form id="createExpensesForminv" class="form-horizontal" name="createExpensesForminv"
					action="/track/AddExpensesServlet?Submit=Save_inv" method="post"
					enctype="multipart/form-data"> 
					<div class="form-group">

						<div class="col-sm-2">
							<label class="control-label col-form-label">Bill Number</label>
						</div>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="billexpinv" name="billexpinv" readonly="readonly">
						</div>
					</div>


					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Shipment Reference</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="shipmentexpinv" name="shipmentexpinv" readonly> 
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Date</label>
						<div class="col-sm-4">
							<input type="text" class="form-control datepicker" value="" id="expenses_dateinv" name="expenses_dateinv" READONLY>
						</div>
					</div>
					<input type="text" name="usernameexpinv" value="" hidden>
					<input type="hidden" name="vendnoexpinv" id="evendcodeinv" value=""> 
					<input type="text" name="plantexpinv" value="" hidden> 
					<input type="hidden" name="CUST_CODEexpinv" id="ecustcodeinv"> 
					<input type="text" name="cmdexpinv" value="" hidden> 
					<input type="hidden" name="TranIdexpinv" value="">
					<input type="hidden" name="shipcmdexpinv" value="">
					<input type="text" name="rvnameinv" value="" hidden>
					<input type="text" name="rvnoinv" value="" hidden>
					<input type="text" name="rgrninv" value="" hidden>
					<input type="text" name="billhdridinv" value="" hidden>
					<input type="text" name="billhdrstatusinv" value="" hidden>
					<input type="text" name="rTAXTREATMENTinv" value="" hidden>
					<input type="text" name="rREVERSECHARGEinv" value="" hidden>
					<input type="text" name="rGOODSIMPORTinv" value="" hidden>
					<input type="text" name="risgrninv" value="" hidden>
					<input type="text" name="refnumexpinv" value="" hidden>
					<INPUT type="hidden" name="CURRENCYIDexpinv"  value="">
					<input type = "hidden" name="ptaxtypeinv" value="">
					<input type = "hidden" name="ptaxpercentageexpinv" value="0">
					<input type = "hidden" name="ptaxdisplayexpinv" value="">
					<input type = "hidden" name="ptaxiszeroexpinv" value="1">
					<input type = "hidden" name="ptaxisshowexpinv" value="0">
					<input type = "hidden" name="taxidexpinv" value="0">
					<input type="hidden" name="CURRENCYUSEQTOLDexpinv" value="">
					<input type="hidden" name="PROJECTIDexpinv" value="0">
					
					<div class="form-group vendor-section">
						<label class="control-label col-form-label col-sm-2">Supplier Name</label>
						<div class="col-sm-6 ac-box">
							<div class="input-group" style="padding-right: 151px;">
								<input type="text" class="ac-selected  form-control typeahead" id="vendnameexpinv" placeholder="Select a Supplier" name="vendnameexpinv" value=""> <span class="select-icon"
									style="right: 45px;"
									onclick="$(this).parent().find('input[name=\'vendnameexpinv\']').focus()"></span> <span
									class="btn-danger input-group-addon"
									onclick="javascript:popUpWin('vendor_list.jsp?AUTO_SUGG=Y&CUST_NAME='+createExpensesForminv.vendnameexpinv.value);"><span
									class="glyphicon glyphicon-search" aria-hidden="true"></span></span>
							</div>
							<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" onchange="OnTaxeChangeinv(this.value)" id="eTAXTREATMENTinv" name="eTAXTREATMENTinv" value="" style="width: 100%">
							<OPTION style="display:none;"></OPTION>
							</SELECT>
			 <div id="eCHK1inv">        
      		 <input type = "checkbox" class="check" id = "eREVERSECHARGEinv" name = "eREVERSECHARGEinv" /><b>&nbsp;This transaction is applicable for reverse charge </b>
      	     </br>
      	     <input type = "checkbox" class="check" id = "eGOODSIMPORTinv" name = "eGOODSIMPORTinv" /><b>&nbsp;This transaction is applicable for goods import &nbsp; &nbsp; </b>
      	     </div>
						</div>
						<div class="col-sm-4">
				<lable class="checkbox-inline" style="margin-left: -171px;"><input type="checkbox" class="form-check-input" id="paidinv" name="paidinv" Onclick="changePaidinv()">
				Paid</lable>
				</div>
					</div>
					<div class="form-group" style="display:none" id="paidthroughfieldinv">
						<label class="control-label col-form-label col-sm-2 required">Paid Through</label>
						<div class="col-sm-4">
							<input type="text" id="paid_through_account_nameinv" name="paid_through_account_nameinv" class="form-control"> 
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'paid_through_account_nameinv\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
						</div>
					</div>


					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Currency</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="currencyexpinv" name="currencyexpinv">
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'currencyexpinv\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
						</div>
					</div>
					<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Equivalent Currency</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="CURRENCYUSEQTexpinv" name="CURRENCYUSEQTexpinv" placeholder="Enter Equivalent Currency" >
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2" id="sdtaxexpinv"></label><!-- this is dynamic value -->
					<div class="col-sm-4">
						<%-- <div class="input-group">
							<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" value="<%=gst%>">
							<span class="input-group-addon" style="font-size: 15px; color: #0059b3">
								<b>%</b>
							</span>
			   		 	</div> --%>
			   		 	<input type="text" class="form-control" id="GSTexpinv" name="GSTexpinv" onchange="changingtaxpercentageexpinv()" value="">
						<span class="sideiconspan"><p>%</p></span>

					</div>
				</div>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Expensetaxexpinv" name="Expensetaxexpinv" placeholder="Select a Tax">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'Expensetaxexpinv\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
						
					</div>
				</div>

					<div class="row" style="margin: 0px;">
						<table class="table table-bordered line-item-table bill-tableexpinv"
							style="width: 95%;">
							<thead>
								<tr>
									<th class="bill-desc">Expenses Account</th>
									<th class="bill-acc">Notes</th>
									<th class="item-amount text-right">Amount</th>
									<th class="item-tax">Tax</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="bill-acc"><input type="text"
										name="expenses_account_nameinv"
										class="form-control expensesaccountSearchinv"
										placeholder="Select Account"></td>
									<td class="col-sm-6 notes-sec">
										<textarea rows="2" name="noteinv" class="ember-text-area form-control ember-view" maxlength="300"></textarea>
									</td>
									<td class="item-amount text-right"><input name="amountexpinv"
										type="text" class="form-control text-right" value="0.00"
										onchange="calculateAmountexpinv(this)"></td>
									<td class="item-tax"><input type="hidden" name="tax_typeexpinv">
										<input type="text" name="taxexpinv" class="form-control taxSearchexpinv"
										placeholder="Select a Tax" readonly></td>

								</tr>
							</tbody>
						</table>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<a style="text-decoration: none; cursor: pointer;" onclick="addRowexpinv()">+ Add another line</a>
						</div>
						<div class="total-section col-sm-5">
							<div class="total-row sub-totalexp">
								<div class="total-label">
									Sub Total <br> <span class="productRateexpinv" hidden>(Tax
										Inclusive)</span>
								</div>
								<div class="total-amount" id="subTotalexpinv">0.00</div>
							</div>

							<div class="taxDetailsexpinv"></div>

							<div class="total-section total-row">
								<div class="gross-total">
									<div class="total-label">Total</div>
									<div class="total-amount" id="totalexpinv">0.00</div>
								</div>
							</div>
						</div>
					</div>

					


					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Reference</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="referenceexpinv"
								name="referenceexpinv" maxlength="100">
						</div>
					</div>

					<div class="form-group customer-section">
						<label id="cusnamereqinv" class="control-label col-form-label col-sm-2">Customer
							Name</label>
						<div class="col-sm-6 ac-box">
							<div class="input-group"  style="padding-right: 151px;">
								<input type="text" id="cusnameexpinv" class="ac-selected  form-control typeahead"
									 placeholder="Select a customer" name="cusnameexpinv">
								<span class="select-icon" style="right: 45px;"
									onclick="$(this).parent().find('input[name=\'cusnameexpinv\']').focus()"></span> <span
									class="btn-danger input-group-addon"
									onclick="javascript:popUpWin('customer_list_issue_summary.jsp?TYPE=ACCTCUST&CUST_NAME='+createExpensesForminv.cusnameexpinv.value);"><span
									class="glyphicon glyphicon-search" aria-hidden="true"></span></span>
							</div>
						</div>
						<div class="col-sm-4" style="margin-left: -171px;">
				<input type="hidden" name="billable_statusinv" value="0">
				<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="billableinv" name="billableinv" Onclick="checkbillableinv()">
				Billable</lable>
				</div>	
					</div>
					<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4 ac-box">
								<input type="text" class="form-control typeahead" 
									id="projectexpinv" placeholder="Select a project" 
									name="projectexpinv" value=""> 
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'projectexpinv\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span> 
								<!-- <span class="btn-danger input-group-addon">
									<span class="glyphicon glyphicon-search" aria-hidden="true"></span>
								</span> -->
					</div>
				</div>

					<div class="row grey-bg">
						<div class="col-sm-4">
							<div class="form-inline">
								<label for="email">Attach Files(s)</label>
								<div class="attch-section">
									<input type="file" class="form-control input-attch"
										id="billAttchexpinv" name="file" multiple="true">
									<div class="input-group">
										<svg version="1.1" id="Layer_1"
											xmlns="http://www.w3.org/2000/svg" x="0" y="0"
											viewBox="0 0 512 512" xml:space="preserve"
											class="icon icon-xs align-text-top action-icons input-group-addon"
											style="height: 30px; display: inline-block; color: #c63616;">
											<path
												d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
										<button type="button" class="btn btn-sm btn-attch">Upload
											File</button>
									</div>

								</div>
							</div>
							<div id="billAttchNoteexpinv">
								<small class="text-muted"> You can upload a maximum of 5
									files, 2MB each </small>
							</div>
						</div>
						<!-- <div class="col-sm-6 notes-sec">
				<p>Notes <span class="text-muted">(For Internal Use)</span></p>
				<div> <textarea rows="2" name="note" class="ember-text-area form-control ember-view"></textarea> </div>
			</div> -->
					</div>

					<input id="sub_totalexpinv" name="sub_totalexpinv" value="0" hidden> 
					<input id="total_amountexpinv" name="total_amountexpinv" value="0" hidden> 
					<input id="total_tax_amountexpinv" name="total_tax_amountexpinv" value="0" hidden>
					<input name="Submitexpinv" value="Save" hidden> 
					<input name="bill_statusexpinv" value="Save" hidden>
					<div class="row">
						<div class="col-sm-12 txn-buttons">
							<button id="saveadditonexpenseinv" type="button" class="btn btn-success">Save</button>
							<!-- <button type="button" class="btn btn-default"onclick="window.location.href='../home'">Cancel</button> -->
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
						</div>
					</div>
				</form>
			</div>
			<!-- <div class="modal-footer">
				<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				
			</div> -->
		</div>
	</div>
</div>
<script>
var addexpzerotypeinv=0;
var addExpTaxListinv= [];
var typeSwitch=false;
$(document).ready(function(){
	var plant = document.form.plant.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	$('input[name ="amount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	$("#subTotalexpinv").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#totalexpinv").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	
	var d = new Date();

	var month = d.getMonth()+1;
	var day = d.getDate();

	var output = (day<10 ? '0' : '') + day + '/' +
	    (month<10 ? '0' : '') + month + '/' +
	    d.getFullYear();
	
	$("input[name ='expenses_dateinv']").val(output);
	
	loadTaxTreatmentinv();	
	loadtaxtretvalinv();
	
	$.ajax({
		type : "POST",
		url : "/track/ExpensesServlet",
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_BASECURRENCY"
		},
		dataType : "json",
		success : function(data) {
			console.log(data);
			$("input[name ='currencyexpinv']").val(data.currency[0].DISPLAY);
			$("input[name ='CURRENCYIDexpinv']").val(data.currency[0].bcur);
			$("input[name ='CURRENCYUSEQTexpinv']").val(data.currency[0].CURRENCYUSEQT);
		}
	});
	
	
	$("#createExpensesForminv").submit(function(e) {

	    e.preventDefault(); // avoid to execute the actual submit of the form.
	    var url = "/track/AddExpensesServlet?Submit=Save_inv";
		if(validateExpensesinv()){
		    var formData = new FormData(this);   
		    $.ajax({
		           type: "POST",
		           url: url,
		           cache: false,
		           contentType: false,
		           processData: false,
		           data: formData, // serializes the form's elements.
		           success: function(data)
		           {
		        	   var data1 = JSON.parse(data);
		               $("input[name ='shipRef']").val(data1.shipmentcode);
		               $(".expense-details-inv").html('');
		               getExpenseDetailForInventoryBill(data1.shipmentcode);
		               $('#Additional_Expenses_inv_modal').modal('toggle');
		           }
		         });
		}
	});
   
	
	/* Supplier Auto Suggestion */
	$('#vendnameexpinv').typeahead({
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
				ACTION : "GET_SUPPLIER_DATA",
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
		    return '<p onclick="getvendnameexpinv(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\')">' + data.VNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
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
				document.createExpensesForminv.vendnoexpinv.value = "";
				document.createExpensesForminv.eTAXTREATMENTinv.value ="";
				document.getElementById('eTAXTREATMENTinv').innerHTML="";
			}
			$('#eTAXTREATMENTinv').attr('disabled',false);
			if($('select[name ="eTAXTREATMENTinv"]').val() =="GCC VAT Registered"||$('select[name ="eTAXTREATMENTinv"]').val()=="GCC NON VAT Registered"||$('select[name ="eTAXTREATMENTinv"]').val()=="NON GCC")
			{
				document.getElementById('eCHK1inv').style.display = 'block';
			}
			else
				document.getElementById('eCHK1inv').style.display = 'none';
		});
	
	
	/* To get the suggestion data for Account */
	$("#paid_through_account_nameinv").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
			display: 'text',  
			  /*source: substringMatcher(states),*/
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						action : "getSubAccountTypeGroup",
						module:"expensepaiddrop",
						NAME : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.results);
					}
			   });
			  },
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px;">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(item) {
					if (item.issub) {
						var $state = $(
							    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
							  );
						}
					else
						{
						var $state = $(
								 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
							  );
						}
					  
					  return $state;
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".smalldrop1");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modal"><a href="#"> + New Account</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".smalldrop1").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".smalldrop1");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".smalldrop1");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
	

	/* project Auto Suggestion */
	$('#projectexpinv').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PROJECT_NAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			  var urlStr = "../FinProject";
				$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						action : "GET_PROJECT_LIST",
						CUSTNO : $("input[name=CUST_CODEexpinv]").val(),
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.PROJECT);
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
		    	return '<div><p class="item-suggestion">Project Name: ' + data.PROJECT_NAME + '</p><br/><p class="item-suggestion">Project No: ' + data.PROJECT + '</p><br/><p class="item-suggestion">Customer No: ' + data.CUSTNO + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			$('.shipCustomerAddBtn').remove();  
			/*if(displayCustomerpop == 'true'){
			$('.shipCustomer-section .tt-menu').after( '<div class="shipCustomerAddBtn footer"  data-toggle="modal" data-target="#customerModal"><a href="#" onclick="document.form1.custModal.value=\'shipcust\'"> + New Cutomer</a></div>');
			}*/
			$(".shipCustomerAddBtn").width(menuElement.width());
			$(".shipCustomerAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.shipCustomerAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.shipCustomerAddBtn').hide();}, 150);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			
		}).on('typeahead:select',function(event,selection){
			$("input[name=PROJECTIDexpinv]").val(selection.ID);
		});

	
	/* Customer Auto Suggestion */
	$('#cusnameexpinv').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'CNAME',  
		  async: true,   
		  //source: substringMatcher(states),
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
		    	return '<p onclick="document.createExpensesForminv.CUST_CODEexpinv.value = \''+data.CUSTNO+'\'">' + data.CNAME + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.customerAddBtn').remove();  
			
			$(".customerAddBtn").width($(".tt-menu").width());
			$(".customerAddBtn").css({ "top": top,"padding":"3px 20px" });			
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.customerAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.customerAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.createExpensesForm.CUST_CODEexp.value = "";
			}			
		}).on('typeahead:select',function(event,selection){
			$("#projectexpinv").typeahead('val', '"');
			$("#projectexpinv").typeahead('val', '');
			$("input[name=PROJECTIDexpinv]").val('0');
		});
	
	
	
	/* currency Auto Suggestion */
	$('#currencyexpinv').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CURRENCYID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ExpensesServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_CURRENCY",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.currency);
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
		    	return '<div><p onclick="getCurrencyidexpinv(\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.DISPLAY+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = $(".tt-menu").height()+35;
			top+="px";
			$('.supplierAddBtn').remove();  
			/*$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');*/
			$(".supplierAddBtn").width($(".tt-menu").width());
			$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
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
			if($(this).val() == "")
				$("input[name ='CURRENCYIDexp']").val("");	
		});
		
	$(".bill-tableexpinv tbody").on('click','.bill-action',function(){
		debugger;	    
	    var obj = $(this).closest('tr').find('td:nth-child(4)');
	    $(this).parent().parent().remove();
	    calculateTotalexpinv()
	});
	
	
	$("#btnBillOpenexp").click(function(){
		$('input[name ="bill_statusexpinv"]').val('NON-BILLABLE');
		$("#sub_totalexpinv").val($("#subTotalexpinv").html());
		$("#total_amountexpinv").val($("#totalexpinv").html());
		$("#createExpensesForminv").submit();
	});
	
	$("#Expensetaxexpinv").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DISPLAY',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax({
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_TAX_TYPE_DATA_PO",
					SALESLOC : "",
					GST_PERCENTAGE : $("input[name=GSTexpinv]").val(),
					TAXKEY : "EXPENSE",
					GSTTYPE : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.records);
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
			return '<p onclick="calculateTaxEXPinv(this,\''+data.SGSTTYPES+'\',\''+data.SGSTPERCENTAGE+'\',\''+data.DISPLAY+'\',\''+data.ISZERO+'\',\''+data.ISSHOW+'\',\''+data.ID+'\')">' 
			+ data.DISPLAY + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			/*menuElement.after( '<div class="taxAddBtn footer"  data-toggle="modal" data-target="#gstModal"><a href="#"> + New Tax</a></div>');*/
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		});
	
	$(document).on("focusout","input[name ='CURRENCYUSEQTexpinv']",function(){
		var CURRENCYUSEQTCost = checknoexpinv($("input[name ='CURRENCYUSEQTexpinv']").val());
		$("input[name ='CURRENCYUSEQTexpinv']").val(CURRENCYUSEQTCost);
		setCURRENCYUSEQTexpinv(CURRENCYUSEQTCost);
	});
	
	$("#billAttchexpinv").change(function(){
		var files = $(this)[0].files.length;
		var sizeFlag = false;
			if(files > 5){
				$(this)[0].value="";
				alert("You can upload only a maximum of 5 files");
				$("#billAttchNoteexpinv").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
			}else{
				for (var i = 0; i < $(this)[0].files.length; i++) {
				    var imageSize = $(this)[0].files[i].size;
				    if(imageSize > 2097152 ){
				    	sizeFlag = true;
				    }
				}	
				if(sizeFlag){
					$(this)[0].value="";
					alert("Maximum file size allowed is 2MB, please try with different file.");
					$("#billAttchNoteexpinv").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
				}else{
					$("#billAttchNoteexpinv").html(files +" files attached");
				}
				
			}
		});
	
	addSuggestionToTableexpinv();
});

function addRowexpinv(){
	var taxdisplay = $("input[name=ptaxdisplayexpinv]").val();
	var body="";
	body += '<tr>';
	body += '<td class="bill-acc">';
	body += '<input type="text" name="expenses_account_nameinv" class="form-control expensesaccountSearchinv" placeholder="Select Account">';
	body += '</td>';
	body += '<td class="col-sm-6 notes-sec">';
	body += '<textarea rows="2" name="noteinv" class="ember-text-area form-control ember-view" maxlength="300"></textarea>';
	body += '</td>';
	body += '<td class="item-amount text-right">';	
	body += '<input name="amountexpinv" type="text" class="form-control text-right" value="0.00" onchange="calculateAmountexpinv(this)"></td>';
	body += '<td class="item-tax grey-bg" style="position:relative;">';
	body += '<input type="hidden" name="tax_typeexpinv">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += '<input type="text" name="taxexpinv" class="form-control taxSearchexpinv" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
	body += '</td>';	
	body += '</tr>';
	$(".bill-tableexpinv tbody").append(body);
	removeSuggestionToTableexpinv();
	addSuggestionToTableexpinv();
}

function addSuggestionToTableexpinv(){
	var plant = document.form.plant.value;	
	
	/* To get the suggestion data for Expenses Account */
	$(".expensesaccountSearchinv").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'smalldrop'
			  }
		},
		{
			display: 'text',  
			  /*source: substringMatcher(states),*/
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : plant,
						action : "getSubAccountTypeGroup",
						module:"expenseaccount",
						NAME : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.results);
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
				suggestion: function(item) {
					if (item.issub) {
						var $state = $(
							    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
							  );
						}
					else
						{
						var $state = $(
								 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
							  );
						}
					  
					  return $state;
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="accountAddBtn footer accrmv"  data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".bigdrop").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".bigdrop");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});

	
	/* To get the suggestion data for Tax */
	/* $(".taxSearchexp").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'DISPLAY',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax({
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_GST_TYPE_DATA_EXPENSE",
				//GSTTYPE : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.records);
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
		return '<p onclick="calculateTaxexp(this,\''+data.SGSTTYPES+'\',\''+data.SGSTPERCENTAGE+'\',\''+data.DISPLAY+'\')">' 
		+ data.DISPLAY + '</p>';
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
	}).on('typeahead:close',function(){
		var menuElement = $(this).parent().find(".tt-menu");
		setTimeout(function(){ menuElement.next().hide();}, 180);
	});	 */
}

function removeSuggestionToTableexpinv(){
	$(".expensesaccountSearchinv").typeahead('destroy');
	$(".taxSearchexpinv").typeahead('destroy');
}


function validDecimalinv(str) {
	if (str.indexOf('.') == -1) str += ".";
	var decNum = str.substring(str.indexOf('.')+1, str.length);
	var declength =	parseInt(document.getElementById("numberOfDecimal").value);
	if (decNum.length > declength)
	{
		return false;
		
	}
	return true;
}


	
function validateExpensesinv(){

	
		var bill = document.createExpensesForminv.billexpinv.value;
		var shipment = document.createExpensesForminv.shipmentexpinv.value;
		
		if(bill == ""){
			alert("Please select a bill.");
			return false;
		}	
		if(shipment == ""){
			alert("Please select a shipment.");
			return false;
		}
	
	var supplier = document.createExpensesForminv.vendnoexpinv.value;
	var paid_through = document.createExpensesForminv.paid_through_account_nameinv.value;
	var expenses_date = document.createExpensesForminv.expenses_dateinv.value;
	var cusno = document.createExpensesForminv.cusnameexpinv.value;
	
	var isItemValid = true, isAccValid = true;
	
	/* if(paid_through == ""){
		alert("Please select a paid_through.");
		return false;
	}	 */
	if($("#paid").is(':checked')){
		if(paid_through == ""){
			alert("Please select a paid_through.");
			return false;
		}	
	}
	if($("#paid").is(':checked')==false){
		if(supplier == ""){
			alert("Please select a Supplier.");
			return false;
		}
		$("#paid_through_account_name").val("");
	}
	if(expenses_date == ""){
		alert("Please enter a valid expenses date.");
		return false;
	}
	
	var checkBox = document.getElementById("billableinv");
	 if (checkBox.checked == true){	
		 if(cusno == ""){
			 alert("Please select Customer.");
			 return false;
		 }
	 } 
	
	
	
	$("input[name ='expenses_account_nameinv']").each(function() {
	    if($(this).val() == ""){	    	
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The Expenses field cannot be empty.");
		return false;
	}	
	
	$("input[name ='amountexpinv']").each(function() {
		if($(this).val() <= 0){
			isItemValid =  false;
	    }
	});	
	
	if(!isItemValid){
		alert("Amount cannot be Zero ");
		return false;
	}
	return true;
}

/* function getEditDetailexpinv(TranId){
	var plant = document.form.plant.value;
	$.ajax({
		type : "POST",
		url : "/track/ExpensesServlet",
		async : true,
		data : {
			PLANT : plant,
			Submit : "GET_EDIT_EXPENSES_DETAILS",
			Id : TranId
		},
		dataType : "json",
		success : function(data) {
			loadEditTableexpinv(data.orders);
		}
	});
}

function loadEditTableexpinv(orders){
	
	$.each(orders, function( key, data ) {		
		$("input[name ='shipmentexpinv']").val(data.SHIPMENT_CODE);
		$("input[name ='billexpinv']").val(data.BILL);
		$("input[name ='cmdexpinv']").val('');
	});
} */

$("#addexpenseinvmodel").click(function(){
	var plant = document.form.plant.value;
	var cmd = document.createExpensesForminv.cmdexpinv.value;
	var TranId = document.createExpensesForminv.TranIdexpinv.value;
	var shiref = document.form.shipRef.value;
	var bill = document.form.bill.value;
	var refnum = document.form.refNum.value;
	var CURRENCY = document.form.CURRENCY.value;
	var CURRENCYID = document.form.CURRENCYID.value;
	var CURRENCYUSEQT = document.form.CURRENCYUSEQT.value;
	var taxlable = $("input[name ='taxbylabelordermanagement']").val();
	var expencetax = $("input[name ='expencetax']").val();
	
	$("input[name ='refnumexpinv']").val(refnum);
	//var vendnameexp = document.createExpensesForm.vendnameexp.value;
	var sTAXTREATMENT = document.form.TAXTREATMENT_VALUE.value;
	document.getElementById('eCHK1inv').style.display = 'none';

	$("input[name ='billexpinv']").val(bill);
	removeSuggestionToTableexpinv();
	addSuggestionToTableexpinv();
		
	
	$('input[name ="CURRENCYIDexpinv"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQTexpinv"]').val(CURRENCYUSEQT);
	$('input[name ="CURRENCYUSEQTOLDexpinv"]').val(CURRENCYUSEQT);
	$('input[name ="currencyexpinv"]').val(CURRENCY);
	$('#sdtaxexpinv').text('Standard '+taxlable);
	$('input[name ="GSTexpinv"]').val(expencetax);
	$("#Additional_Expenses_inv_modal").modal("show");
	
});
	
	
	function checkbillableinv()
	{
		 var checkBox = document.getElementById("billableinv");
		 if (checkBox.checked == true){		 
			 $("input[name ='billable_statusinv']").val("1");
			 $("#cusnamereqinv").addClass("required");
		 } else{		 
			 $("input[name ='billable_statusinv']").val("0");
			 $("#cusnamereqinv").removeClass("required");
		 } 
	}
	
	$("#saveadditonexpenseinv").click(function(){
		document.getElementById('shipmentexpinv').disabled  = false;
		var checkBox = document.getElementById("billableinv");
		if (checkBox.checked == true){	
			$('input[name ="bill_statusexpinv"]').val('UNBILLED');
		}else{
			$('input[name ="bill_statusexpinv"]').val('NON-BILLABLE');
		}
		$("#createExpensesForminv").submit();
	});
	

	function loadTaxTreatmentinv()
	{
		$.ajax({
			type : "POST",
			url: '/track/MasterServlet',
			async : true,
			dataType: 'json',
			data : {
				action : "GET_TAXTREATMENT_DATA",
			},
			success : function(dataitem) {
				var TaxTreatmentList=dataitem.TAXTREATMENTMST;
				var myJSON = JSON.stringify(TaxTreatmentList);
				//alert(myJSON);
					 $.each(TaxTreatmentList, function (key, value) {
						   $('#eTAXTREATMENTinv').append('<option value="' + value.text + '">' + value.text + '</option>');
						});
			}
		});
	}
	
	function getvendnameexpinv(TAXTREATMENT,VENDO){
		$('select[name ="eTAXTREATMENTinv"]').val(TAXTREATMENT);
		document.createExpensesForminv.vendnoexpinv.value =VENDO;
		if(TAXTREATMENT =="GCC VAT Registered"||TAXTREATMENT=="GCC NON VAT Registered"||TAXTREATMENT=="NON GCC")
		{
			document.getElementById('eCHK1inv').style.display = 'block';
		}
		else
			document.getElementById('eCHK1inv').style.display = 'none';
		}
	
	function OnTaxeChangeinv(TAXTREATMENT)
	{			
		document.getElementById("eREVERSECHARGEinv").checked = false;
		document.getElementById("eGOODSIMPORTinv").checked = false;
		if(TAXTREATMENT =="GCC VAT Registered"||TAXTREATMENT=="GCC NON VAT Registered"||TAXTREATMENT=="NON GCC")
		{
			document.getElementById('eCHK1inv').style.display = 'block';
		}
		else
			document.getElementById('eCHK1inv').style.display = 'none';
		}
	

	
	function loadtaxtretvalinv()
	{

		var sTAXTREATMENT = $("input[name ='TAXTREATMENT_VALUE']").val();
		$('select[name ="eTAXTREATMENTinv"]').val(sTAXTREATMENT);
		if(sTAXTREATMENT =="GCC VAT Registered"||sTAXTREATMENT=="GCC NON VAT Registered"||sTAXTREATMENT=="NON GCC")
		{
			document.getElementById('eCHK1inv').style.display = 'block';
		}
		else
			document.getElementById('eCHK1inv').style.display = 'none';
	}
	
	function setCustomerData(customerData){	
		document.createExpensesForm.CUST_CODEexp.value=customerData.custno;
		//document.createExpensesForm.cusnameexp.value=customerData.custname;		
		$("#cusnameexpinv").typeahead('val', customerData.custname); 
	}
	$(".bill-tableexpinv tbody").on('click','.accrmv',function(){
		debugger;	    
	    var obj = $(this);
	    var timestamp = new Date().getUTCMilliseconds();
	    kayid = "key"+timestamp;
	    $(obj).closest('td').attr('id', kayid); 
	    $("input[name ='ukey']").val(kayid);
	});
	function getCurrencyidexpinv(CURRENCYID,CURRENCYUSEQT){
		
		$('input[name ="CURRENCYIDexpinv"]').val(CURRENCYID);
		$('input[name ="CURRENCYUSEQTexpinv"]').val(CURRENCYUSEQT);
		setCURRENCYUSEQTexpinv(CURRENCYUSEQT);
	}
function changePaidinv(){
	if($("#paidinv").is(':checked')){
		$("#paidthroughfieldinv").show();
	} else {
		$("#paidthroughfieldinv").hide();
		$("#paid_through_account_nameinv").val("");
	}
	
}

function calculateTaxEXPinv(obj, types, percentage, display, iszero, isshow, id){
	$("input[name=ptaxtypeexpinv]").val(types);
	$("input[name=ptaxpercentageexpinv]").val(percentage);
	$("input[name=ptaxdisplayexpinv]").val(display);
	$("input[name=ptaxiszeroexpinv]").val(iszero);
	$("input[name=ptaxisshowexpinv]").val(isshow);
	$("input[name=taxidexpinv]").val(id);
	$('.taxSearchexpinv').each(function(){
	    $(this).val(display);
	});
	calculateTotalexpinv();
}


function calculateAmountexpinv(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = parseFloat($(obj).closest('tr').find("td:nth-child(3)").find('input').val()).toFixed(numberOfDecimal);
	$(obj).closest('tr').find("td:nth-child(3)").find('input').val(amount);
	calculateTotalexpinv();
}

function calculateTotalexpinv(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var ptaxiszero = $("input[name='ptaxiszeroexpinv']").val();
	var ptaxisshow = $("input[name='ptaxisshowexpinv']").val();
	var ptaxpercentage = $("input[name='ptaxpercentageexpinv']").val();
	var ptaxdisplay = $("input[name='ptaxdisplayexpinv']").val();
	
	var amount = 0, totalvalue=0;
	var rowcount=$(".bill-tableexpinv tbody tr").length;
	if(rowcount>0)
		{
			$(".bill-tableexpinv tbody tr td:nth-child(3").each(function() {
			    amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
			    amount = parseFloat(amount).toFixed(numberOfDecimal);
			    $("#subTotalexpinv").html(amount);
			    $('input[name ="sub_totalexpinv"]').val(amount);// hidden input
			});
		}
	else
	{
		amount=0;
		amount=parseFloat(amount).toFixed(numberOfDecimal);
		$("#subTotalexpinv").html(amount);
		$('input[name ="sub_totalexpinv"]').val(amount);// hidden input
	}
	 
	var taxTotal = 0;

	if(ptaxiszero == "0" && ptaxisshow == "1"){
		taxTotal = parseFloat((amount/100)*ptaxpercentage);
	}

	 if(ptaxisshow == "1"){
			var html ="";
			html+='<div class="total-row">';
			html+='<div class="total-label">'+ptaxdisplay+'</div>';
			html+='<div class="total-amount taxAmountexpinv">'+parseFloat(parseFloat(taxTotal)).toFixed(numberOfDecimal)+'</div>';
			html+='</div>';
			$(".taxDetailsexpinv").html(html);
			$("#total_tax_amountexpinv").val(taxTotal); 
		}else{
			$(".taxDetailsexpinv").html("");
			$("#total_tax_amountexpinv").val("0"); 
		}
	 
	 
	 
	 totalvalue = parseFloat(parseFloat(amount) + parseFloat(taxTotal)).toFixed(numberOfDecimal);
	 $("#totalexpinv").html(totalvalue);
	 $("#total_amountexpinv").val(totalvalue); 
}

function changingtaxpercentageexpinv(){
	var baseamount = $("#GSTexpinv").val();
	var zeroval = parseFloat("0").toFixed(3);
	if(baseamount != ""){
		var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(baseamount.match(decimal) || baseamount.match(numbers)) 
		{ 
			$("#GSTexpinv").val(parseFloat(baseamount).toFixed(3));	
		}else{
			$("#GSTexpinv").val(zeroval);
			alert("Please Enter Valid Percentage");
		}
	}else{
		$("#GSTexpinv").val(zeroval);
	}
	removetaxdropdownexp();
	addtaxdropdownexp();
	taxresetexpinv();
}


function taxresetexpinv(){
	$("input[name=ptaxtypeexpinv]").val("");
	$("input[name=ptaxpercentageexpinv]").val("0");
	$("input[name=ptaxdisplayexpinv]").val("");
	$("input[name=ptaxiszeroexpinv]").val("1");
	$("input[name=ptaxisshowexpinv]").val("0");
	$("input[name=taxidexpinv]").val("0");
	$('.taxSearchexpinv').each(function(){
	    $(this).val("");
	});
	$("#Expensetaxexpinv").typeahead('val', '');
	calculateTotalexpinv();
}

function removetaxdropdownexp(){
	$("#Expensetaxexpinv").typeahead('destroy');
}
function addtaxdropdownexp(){
	var plant = document.form.plant.value;
	$("#Expensetaxexpinv").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DISPLAY',  
		  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax({
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_TAX_TYPE_DATA_PO",
					SALESLOC : "",
					GST_PERCENTAGE : $("input[name=GSTexpinv]").val(),
					TAXKEY : "EXPENSE",
					GSTTYPE : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.records);
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
			return '<p onclick="calculateTaxEXPinv(this,\''+data.SGSTTYPES+'\',\''+data.SGSTPERCENTAGE+'\',\''+data.DISPLAY+'\',\''+data.ISZERO+'\',\''+data.ISSHOW+'\',\''+data.ID+'\')">' 
			+ data.DISPLAY + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			/*menuElement.after( '<div class="taxAddBtn footer"  data-toggle="modal" data-target="#gstModal"><a href="#"> + New Tax</a></div>');*/
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 150);
		});
}

function setCURRENCYUSEQTexpinv(CURRENCYUSEQTCost){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var oldeqtcost = $('input[name ="CURRENCYUSEQTOLDexpinv"]').val();
	var cost = 0;
	$('.bill-tableexpinv tbody tr').each(function () {
		cost = ((parseFloat($(this).find('input[name=amountexpinv]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
		cost = parseFloat(cost).toFixed(numberOfDecimal);
		$(this).find('input[name=amountexpinv]').val(cost);
	});
	
	$("input[name ='CURRENCYUSEQTOLDexpinv']").val(CURRENCYUSEQTCost);
	
	calculateTotalexpinv();
}

function checknoexpinv(amount){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var baseamount = amount;
	var zeroval = parseFloat("0").toFixed(numberOfDecimal);;
	if(baseamount != ""){
		var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(baseamount.match(decimal) || baseamount.match(numbers)) 
		{ 
			return parseFloat(baseamount).toFixed(numberOfDecimal);
		}else{
			return zeroval;
		}
	}else{
		return zeroval;
	}
	
}


</script>