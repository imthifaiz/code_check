<div id="Additional_Expenses_modal" class="modal fade" role="dialog">
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
					<form id="createExpensesForm" class="form-horizontal" name="createExpensesForm"
					action="/track/AddExpensesServlet?Submit=Save" method="post"
					enctype="multipart/form-data"> 
					<div class="form-group">

						<div class="col-sm-2">
							<label class="control-label col-form-label">PO Number</label>
						</div>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="ponoexp" name="ponoexp" readonly="readonly">
						</div>
					</div>


					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Shipment Reference</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="shipmentexp" name="shipmentexp" disabled> 
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'shipmentexp\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
						</div>
					</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Date</label>
						<div class="col-sm-4">
							<input type="text" class="form-control datepicker" value="" id="expenses_date" name="expenses_date" READONLY>
						</div>
					</div>
					<input type="text" name="usernameexp" value="" hidden>
					<input type="hidden" name="vendnoexp" id="evendcode" value=""> 
					<input type="text" name="plantexp" value="" hidden> 
					<input type="hidden" name="CUST_CODEexp" id="ecustcode"> 
					<input type="text" name="cmdexp" value="" hidden> 
					<input type="hidden" name="TranIdexp" value="">
					<input type="hidden" name="shipcmdexp" value="">
					<input type="text" name="rvname" value="" hidden>
					<input type="text" name="rvno" value="" hidden>
					<input type="text" name="rgrn" value="" hidden>
					<input type="text" name="billhdrid" value="" hidden>
					<input type="text" name="billhdrstatus" value="" hidden>
					<input type="text" name="rTAXTREATMENT" value="" hidden>
					<input type="text" name="rREVERSECHARGE" value="" hidden>
					<input type="text" name="rGOODSIMPORT" value="" hidden>
					<input type="text" name="risgrn" value="" hidden>
					<input type="text" name="refnumexp" value="" hidden>
					<INPUT type="hidden" name="CURRENCYIDexp"  value="">
					<input type = "hidden" name="ptaxtype" value="">
					<input type = "hidden" name="ptaxpercentageexp" value="0">
					<input type = "hidden" name="ptaxdisplayexp" value="">
					<input type = "hidden" name="ptaxiszeroexp" value="1">
					<input type = "hidden" name="ptaxisshowexp" value="0">
					<input type = "hidden" name="taxidexp" value="0">
					<input type="hidden" name="CURRENCYUSEQTOLDexp" value="">
					<input type="hidden" name="PROJECTIDexp" value="0">
					<INPUT type="hidden" name="BASECURRENCYIDexp"  value=""> <%--Resvi--%>
					
					<div class="form-group vendor-section">
						<label class="control-label col-form-label col-sm-2 required">Supplier Name</label>
						<div class="col-sm-4 ac-box">
<!-- 							<div class="input-group" style="padding-right: 151px;"> -->
								<input type="text" class="ac-selected  form-control typeahead" id="vendnameexp" placeholder="Select a Supplier" name="vendnameexp" value=""> 
									<span class="select-icon" onclick="$(this).parent().find('input[name=\'vendnameexp\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span></span> 
	
<!-- 							</div> -->

							<SELECT class="form-control taxdropdown" data-toggle="dropdown" style="border: none;" data-placement="right" onchange="OnTaxeChange(this.value)" id="eTAXTREATMENT" name="eTAXTREATMENT" value="" style="width: 100%">
							<OPTION style="display:none;"></OPTION>
							</SELECT>
			 <div id="eCHK1">        
      		 <input type = "checkbox" class="check" id = "eREVERSECHARGE" name = "eREVERSECHARGE" /><b>&nbsp;This transaction is applicable for reverse charge </b>
      	     </br>
      	     <input type = "checkbox" class="check" id = "eGOODSIMPORT" name = "eGOODSIMPORT" /><b>&nbsp;This transaction is applicable for goods import &nbsp; &nbsp; </b>
      	     </div>
						</div>
						<div class="col-sm-4">
				<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="paid" name="paid" Onclick="changePaid()">
				Paid</lable>
				</div>
					</div>
					<div class="form-group" style="display:none" id="paidthroughfield">
						<label class="control-label col-form-label col-sm-2 required">Paid Through</label>
						<div class="col-sm-4">
							<input type="text" id="paid_through_account_name" name="paid_through_account_name" class="form-control"> 
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'paid_through_account_name\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
						</div>
					</div>

<!-- Resvi Includes For Currency -->
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required">Currency</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="currencyexp" name="currencyexp">
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'currencyexp\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
						</div>
					
				<div class="col-sm-6 no-padding">
						<label class="control-label col-form-label col-sm-5 required" id="exchangerateexp" ></label>
						<div class="col-sm-6 ac-box">
						<input type="text" class="form-control" id="CURRENCYUSEQTexp" name="CURRENCYUSEQTexp" placeholder="Enter Exchange Rate" required >
					</div>
				</div>
				</div>
<!-- 					    Ends -->
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2" id="sdtaxexp"></label><!-- this is dynamic value -->
					<div class="col-sm-4">
						<%-- <div class="input-group">
							<input type="text" class="form-control" id="GST" name="GST" onchange="changingtaxpercentage()" value="<%=gst%>">
							<span class="input-group-addon" style="font-size: 15px; color: #0059b3">
								<b>%</b>
							</span>
			   		 	</div> --%>
			   		 	<input type="text" class="form-control" id="GSTexp" name="GSTexp" onchange="changingtaxpercentageexp()" value="">
						<span class="sideiconspan"><p>%</p></span>

					</div>
				</div>
		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Tax:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="Expensetaxexp" name="Expensetaxexp" placeholder="Select a Tax">
						<span class="select-icon" 
							onclick="$(this).parent().find('input[name=\'Expensetaxexp\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
						
					</div>
				</div>

					<div class="row" style="margin: 0px;">
						<table class="table table-bordered line-item-table bill-tableexp"
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
										name="expenses_account_name"
										class="form-control expensesaccountSearch"
										placeholder="Select Account"></td>
									<td class="col-sm-6 notes-sec">
										<textarea rows="2" name="note" class="ember-text-area form-control ember-view" maxlength="300"></textarea>
									</td>
									<td class="item-amount text-right"><input name="amountexp"
										type="text" class="form-control text-right" value="0.00"
										onchange="calculateAmountexp(this)"></td>
									<td class="item-tax"><input type="hidden" name="tax_typeexp">
										<input type="text" name="taxexp" class="form-control taxSearchexp"
										placeholder="Select a Tax" readonly></td>

								</tr>
							</tbody>
						</table>
					</div>
					<div class="row">
						<div class="col-sm-6">
							<a style="text-decoration: none; cursor: pointer;" onclick="addRowexp()">+ Add another line</a>
						</div>
						<div class="total-section col-sm-5">
							<div class="total-row sub-totalexp">
								<div class="total-label">
									Sub Total <br> <span class="productRateexp" hidden>(Tax
										Inclusive)</span>
								</div>
								<div class="total-amount" id="subTotalexp">0.00</div>
							</div>

							<div class="taxDetailsexp"></div>
                    <!-- Resvi Include for Total Currency -->
							<div class="total-section total-row">
							<div class="gross-total-removemargin"><!--  Author: Resvi  Add date: July 27,2021  Description: Total of Local Currency-->
								<div class="total-label"><label id="lbltotalexp"></label></div>
								<div class="total-amount" id="totalexp">0.00</div>
							</div>
						</div>
						
						<%-- <input type="hidden" name="adjustment" value="<%=zeroval %>"> --%>
						<div class="total-section total-row" id="showtotalcurexp">
							<div class="gross-total">
								<div class="total-label"><label id="basetotal"></label></div>
								<div class="total-amount" id="totalcurexp">0.00</div>
							</div>
						</div>
						</div>
					</div>

                 <!-- 					Ends -->
 

					<div class="form-group">
						<label class="control-label col-form-label col-sm-2">Reference</label>
						<div class="col-sm-4">
							<input type="text" class="form-control" id="referenceexp"
								name="referenceexp" maxlength="100">
						</div>
					</div>

					<div class="form-group customer-section">
						<label id="cusnamereq" class="control-label col-form-label col-sm-2">Customer
							Name</label>
						<div class="col-sm-4 ac-box">
<!-- 							<div class="input-group"  style="padding-right: 151px;"> -->
								<input type="text" id="cusnameexp" class="ac-selected  form-control typeahead"
									 placeholder="Select a customer" name="cusnameexp">
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'cusnameexp\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span></span>
									
<!-- 							</div> -->
						</div>
						<div class="col-sm-4" >
				<input type="hidden" name="billable_status" value="0">
				<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="billable" name="billable" Onclick="checkbillable()">
				Billable</lable>
				</div>	
					</div>
					<div class="form-group shipCustomer-section">
					<label class="control-label col-form-label col-sm-2">Project:</label>
					<div class="col-sm-4 ac-box">
								<input type="text" class="form-control typeahead" 
									id="projectexp" placeholder="Select a project" 
									name="projectexp" value=""> 
								<span class="select-icon" onclick="$(this).parent().find('input[name=\'projectexp\']').focus()">
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
										id="billAttch" name="file" multiple="true">
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
							<div id="billAttchNoteexp">
								<small class="text-muted"> You can upload a maximum of 5
									files, 2MB each </small>
							</div>
						</div>
						<!-- <div class="col-sm-6 notes-sec">
				<p>Notes <span class="text-muted">(For Internal Use)</span></p>
				<div> <textarea rows="2" name="note" class="ember-text-area form-control ember-view"></textarea> </div>
			</div> -->
					</div>

					<input id="sub_totalexp" name="sub_totalexp" value="" hidden> 
					<input id="total_amountexp" name="total_amountexp" value="" hidden> 
					<input id="total_tax_amountexp" name="total_tax_amountexp" value="" hidden>
					<input name="Submitexp" value="Save" hidden> 
					<input name="bill_statusexp" value="Save" hidden>
					<div class="row">
						<div class="col-sm-12 txn-buttons">
							<button id="saveadditonexpense" type="button" class="btn btn-success">Save</button>
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
var addexpzerotype=0;
var addExpTaxList= [];
var typeSwitch=false;
$(document).ready(function(){
	var plant = document.form.plant.value;
	var numberOfDecimal = $("#numberOfDecimal").val();
	$('input[name ="amount"]').val(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	$("#subTotalexp").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	$("#totalexp").html(parseFloat("0.00000").toFixed(numberOfDecimal));
	
	/* $(".datepicker").datepicker({
		format: "dd/mm/yyyy",
		autoclose: true,
		todayHighlight: true
	}); */
	
	var d = new Date();

	var month = d.getMonth()+1;
	var day = d.getDate();

	var output = (day<10 ? '0' : '') + day + '/' +
	    (month<10 ? '0' : '') + month + '/' +
	    d.getFullYear();
	
	$("input[name ='expenses_date']").val(output);
	
	loadTaxTreatment();	
	loadtaxtretval();
	
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
			$("input[name ='currencyexp']").val(data.currency[0].DISPLAY);
			$("input[name ='CURRENCYIDexp']").val(data.currency[0].bcur);
			$("input[name ='CURRENCYUSEQTexp']").val(data.currency[0].CURRENCYUSEQT);
		}
	});
	
	
	$("#createExpensesForm").submit(function(e) {

	    e.preventDefault(); // avoid to execute the actual submit of the form.
	    var url = "/track/AddExpensesServlet?Submit=Save";
		if(validateExpenses()){
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
		               $(".expense-details").html('');
		               getExpenseDetailForBill(data1.shipmentcode,data1.pono);
		               $('#Additional_Expenses_modal').modal('toggle');
		           }
		         });
		}
	});
   
	
	/* Supplier Auto Suggestion */
	$('#vendnameexp').typeahead({
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
		    return '<div  onclick="getvendnameexp(\''+data.TAXTREATMENT+'\',\''+data.VENDO+'\',\''+data.CURRENCYID+'\',\''+data.CURRENCY+'\',\''+data.CURRENCYUSEQT+'\')"><p class="item-suggestion">Name: ' + data.VNAME 
		    + '</p><br/><p class="item-suggestion">Code: ' + data.VENDO + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME 
		    + '</p><p class="item-suggestion pull-right">Supplier Type: ' + data.SUPPLIERTYPE + '</p></div>';
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
				document.createExpensesForm.vendnoexp.value = "";
				document.createExpensesForm.eTAXTREATMENT.value ="";
				document.getElementById('eTAXTREATMENT').innerHTML="";
			}
			$('#eTAXTREATMENT').attr('disabled',false);
			if($('select[name ="eTAXTREATMENT"]').val() =="GCC VAT Registered"||$('select[name ="eTAXTREATMENT"]').val()=="GCC NON VAT Registered"||$('select[name ="eTAXTREATMENT"]').val()=="NON GCC")
			{
				document.getElementById('eCHK1').style.display = 'block';
			}
			else
				document.getElementById('eCHK1').style.display = 'none';
		});
	
	
	/* To get the suggestion data for Account */
	$("#paid_through_account_name").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'bigdrop'
			  }
		},
		{
			display: 'accountname',  
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
	$('#projectexp').typeahead({
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
						CUSTNO : $("input[name=CUST_CODEexp]").val(),
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
			$("input[name=PROJECTIDexp]").val(selection.ID);
		});

	
	/* Customer Auto Suggestion */
	$('#cusnameexp').typeahead({
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
		    	return ' <div  onclick="document.createExpensesForm.CUST_CODEexp.value = \''+data.CUSTNO+'\'"> <p class="item-suggestion">Name: ' + data.CNAME + '</p> <br/> <p class="item-suggestion">Customer ID: ' + data.CUSTNO + '</p> <p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
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
			$("#projectexp").typeahead('val', '"');
			$("#projectexp").typeahead('val', '');
			$("input[name=PROJECTIDexp]").val('0');
		});
	
	/* Shipment Auto Suggestion */
	$('#shipmentexp').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{		  
		  display: 'SHIPMENTCODE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					action : "VIEW_SHIPMENT_SUMMARY_VIEW",
					ORDERNO : document.createExpensesForm.ponoexp.value,
					SHIPMENT_CODE : query
				},
				dataType : "json",
				success : function(data) {
					if(document.createExpensesForm.ponoexp.value!="")
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
		    	return '<p onclick="Shipmentcheckexp(\''+data.PONO+'\',\''+data.SHIPMENTCODE+'\')">' + data.SHIPMENTCODE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="shipmentAddBtn footer"  onclick="shipmentpopup()"><a href="#"> + Add Shipment</a></div>');
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
	
	/* currency Auto Suggestion */
	$('#currencyexp').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DISPLAY',  
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
		    	return '<div><p onclick="getCurrencyidexp(\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.DISPLAY+'</p></div>';
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
		
	$(".bill-tableexp tbody").on('click','.bill-action',function(){
		debugger;	    
	    var obj = $(this).closest('tr').find('td:nth-child(4)');
	    calculateTaxexp(obj, "", "", "");
	    $(this).parent().parent().remove();
	    calculateTotalexp();
	});
	
	
	$("#btnBillOpenexp").click(function(){
		$('input[name ="bill_statusexp"]').val('NON-BILLABLE');
		$("#sub_totalexp").val($("#subTotalexp").html());
		$("#total_amountexp").val($("#totalexp").html());
		$("#createExpensesForm").submit();
	});
	
	$("#Expensetaxexp").typeahead({
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
					GST_PERCENTAGE : $("input[name=GSTexp]").val(),
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
			return '<p onclick="calculateTaxEXP(this,\''+data.SGSTTYPES+'\',\''+data.SGSTPERCENTAGE+'\',\''+data.DISPLAY+'\',\''+data.ISZERO+'\',\''+data.ISSHOW+'\',\''+data.ID+'\')">' 
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
	
	$(document).on("focusout","input[name ='CURRENCYUSEQTexp']",function(){
		var CURRENCYUSEQTCost = checknoexp($("input[name ='CURRENCYUSEQTexp']").val());
// 		$("input[name ='CURRENCYUSEQTexp']").val(CURRENCYUSEQTCost);
		if(!Number.isNaN(CURRENCYUSEQTCost))
		setCURRENCYUSEQT(CURRENCYUSEQTCost);
	});
	addSuggestionToTableexp();
});

function addRowexp(){
	var taxdisplay = $("input[name=ptaxdisplayexp]").val();
	var body="";
	body += '<tr>';
	body += '<td class="bill-acc">';
	body += '<input type="text" name="expenses_account_name" class="form-control expensesaccountSearch" placeholder="Select Account">';
	body += '</td>';
	body += '<td class="col-sm-6 notes-sec">';
	body += '<textarea rows="2" name="note" class="ember-text-area form-control ember-view" maxlength="300"></textarea>';
	body += '</td>';
	body += '<td class="item-amount text-right">';	
	body += '<input name="amountexp" type="text" class="form-control text-right" value="0.00" onchange="calculateAmountexp(this)"></td>';
	body += '<td class="item-tax grey-bg" style="position:relative;">';
	body += '<input type="hidden" name="tax_typeexp">';
	body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += '<input type="text" name="taxexp" class="form-control taxSearchexp" placeholder="Select a Tax" value="'+taxdisplay+'" readonly>';
	body += '</td>';	
	body += '</tr>';
	$(".bill-tableexp tbody").append(body);
	removeSuggestionToTableexp();
	addSuggestionToTableexp();
}

function addSuggestionToTableexp(){
	var plant = document.form.plant.value;	
	
	/* To get the suggestion data for Expenses Account */
	$(".expensesaccountSearch").typeahead({
		hint: true,
		  minLength:0,  
		  searchOnFocus: true,
		  classNames: {
			 	menu: 'smalldrop'
			  }
		},
		{
			display: 'accountname',  
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

function removeSuggestionToTableexp(){
	$(".expensesaccountSearch").typeahead('destroy');
	$(".taxSearchexp").typeahead('destroy');
}

/* function calculateAmountexp(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var item=$(obj);
	var taxrate=$(obj).closest('tr').find("td:nth-child(4)").find('input').val();
	//alert(taxrate);
	var display = taxrate.replace("%]","");
	var percentage = display.split("[");
	var numberOfDecimal = $("#numberOfDecimal").val();
	var taxpercentage=percentage[1];
	var taxtype=percentage[0].trim();
	typeSwitch=true;
	calculateTaxexp(item,taxtype,taxpercentage,taxrate);
	
	
	var amount = parseFloat($(obj).closest('tr').find("td:nth-child(3)").find('input').val()).toFixed(numberOfDecimal);
	
	$(obj).closest('tr').find("td:nth-child(3)").find('input').val(amount);
	calculateTotalexp();
}

function calculateTotalexp(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = 0, totalvalue=0;
	$(".bill-tableexp tbody tr td:nth-child(3").each(function() {
	    amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
	    amount = parseFloat(amount).toFixed(numberOfDecimal);
	    $("#subTotalexp").html(amount);
	    $('input[name ="sub_totalexp"]').val(amount);// hidden input
	});
	
	 
	 var taxTotal = 0;
	 if($(".taxDetailsexp").html().length > 0){
		 $('.taxAmountexp').each(function(i, obj) {
			 taxTotal += parseFloat($(this).html());
		 });
	 }
	 
	 $("#total_tax_amountexp").val(taxTotal); 
	 totalvalue = parseFloat(parseFloat(amount) + parseFloat(taxTotal)).toFixed(numberOfDecimal);
	 $("#totalexp").html(totalvalue);
	 $("#total_amountexp").val(totalvalue); // hidden input}
}
 */
function validDecimal(str) {
	if (str.indexOf('.') == -1) str += ".";
	var decNum = str.substring(str.indexOf('.')+1, str.length);
	var declength =	parseInt(document.getElementById("numberOfDecimal").value);
	if (decNum.length > declength)
	{
		return false;
		
	}
	return true;
}

function calculateTaxexp(obj, types, percentage, display){
	if(!typeSwitch)
		{
			if(types=="ZERO RATE")
			{
				addexpzerotype++;
			}
		}
	typeSwitch=false;
	$(obj).closest('td').find('input[name = "tax_typeexp"]').val(display);
	var numberOfDecimal = $("#numberOfDecimal").val();
	var tax = new Object();
	tax.types = types;
	tax.percentage = percentage;
	tax.display = display;
	var prevTypes = $(obj).closest('td').data('name');
	if(prevTypes !== "" && prevTypes != undefined){
		var name = $(obj).closest('td').data('name');
		var prevPercentage = $(obj).closest('td').data('tax');
		
		$(obj).closest('td').data('name','');
		$(obj).closest('td').data('tax','');
		var amount = $(obj).closest('tr').find('input[name = "amount"]').val();
		discountValue = parseFloat(amount*(prevPercentage/100)).toFixed(numberOfDecimal);
		
		$.each(addExpTaxList, function( key, data ) {
			if(data.types == name){
				data.value = parseFloat(parseFloat(data.value)-parseFloat(discountValue)).toFixed(numberOfDecimal);
				if(name=="ZERO RATE")
				{
					addexpzerotype--;
				}
			}
		});
	}
	
	$(obj).closest('td').data('name', types);
	$(obj).closest('td').data('tax', percentage);
	var amount = $(obj).closest('tr').find('input[name = "amount"]').val();
	discountValue = parseFloat(amount*(percentage/100)).toFixed(numberOfDecimal);
	tax.value = discountValue;
	
	if(addExpTaxList.length == 0){
		addExpTaxList.push(tax);	
	}else{
		var match = false;
		$.each(addExpTaxList, function( key, data ) {
			if(data.types == types){
				data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
				match = true;
			}
		});
		if(!match){
			addExpTaxList.push(tax);
		}
	}
	renderTaxDetailsexp();
}

function renderTaxDetailsexp(){
	var html="";
	var taxTotal = 0;
	var numberOfDecimal = $("#numberOfDecimal").val();
	$.each(addExpTaxList, function( key, data ) {
		var mydata=JSON.stringify(data);
		console.log("Tax dta:"+mydata);
		var originalTaxType= data.types;
		console.log("Zero Type:"+addexpzerotype);
		if(data.value > 0 || originalTaxType=="ZERO RATE" && addexpzerotype>0){
			html+='<div class="total-row">';
			html+='<div class="total-label">'+data.display+'</div>';
			html+='<div class="total-amount taxAmountexp">'+data.value+'</div>';
			html+='</div>';
			taxTotal += parseFloat(data.value);
		}
	});
	$(".taxDetailsexp").html(html);
	calculateTotalexp();
	 
	}
	
function validateExpenses(){

	
		var pono = document.createExpensesForm.ponoexp.value;
		var shipment = document.createExpensesForm.shipmentexp.value;
		
		if(pono == ""){
			alert("Please select a pono.");
			return false;
		}	
		if(shipment == ""){
			alert("Please select a shipment.");
			return false;
		}
	
	var supplier = document.createExpensesForm.vendnoexp.value;
	var paid_through = document.createExpensesForm.paid_through_account_name.value;
	var expenses_date = document.createExpensesForm.expenses_date.value;
	var CURR = document.createExpensesForm.currencyexp.value;
	var CURRQT = document.createExpensesForm.CURRENCYUSEQTexp.value;
	var cusno = document.createExpensesForm.cusnameexp.value;
	
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

	if(CURR == ""){
		alert("Please select a currency.");
		return false;
	}
	if(CURRQT == ""){
		alert("Please enter the Exchange Rate..");
		return false;
	}
	
	var checkBox = document.getElementById("billable");
	 if (checkBox.checked == true){	
		 if(cusno == ""){
			 alert("Please select Customer.");
			 return false;
		 }
	 } 
	
	
	
	$("input[name ='expenses_account_name']").each(function() {
	    if($(this).val() == ""){	    	
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The Expenses field cannot be empty.");
		return false;
	}	
	return true;
}

function getEditDetailexp(TranId){
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
			loadEditTableexp(data.orders);
		}
	});
}

function loadEditTableexp(orders){
	/*
	var body="";
	addExpTaxList = [];
	$.each(orders, function( key, data ) {	
		var tax = new Object();
		var display = data.TAX_TYPE.replace("%]","");
		var percentage = display.split("[");
		var numberOfDecimal = $("#numberOfDecimal").val();
		tax.types = percentage[0].trim();
		tax.percentage = percentage[1];
		tax.display = data.TAX_TYPE;
		var taxpercentage=percentage[1];
		var taxtype=percentage[0].trim();
		
		if(taxtype=="ZERO RATE")
		{
			addexpzerotype++;
		}
		
		body += '<tr>';
		
		body += '<td class="bill-acc">';
		body += '<input type="hidden" name="DETID" class="form-control" value="'+data.DETID+'"><input type="text" name="expenses_account_name" class="form-control expensesaccountSearch" value="'+data.EXPENSES_ACCOUNT+'" placeholder="Select Account">';
		body += '</td>';
		body += '<td class="col-sm-6 notes-sec">';

		
		body += '<textarea rows="2" name="note" class="ember-text-area form-control ember-view" maxlength="300">'+data.DESCRIPTION+'</textarea>';
		body += '</td>';
		body += '<td class="item-amount text-right">';	
		body += '<input name="amount" type="text" class="form-control text-right" value="'+data.AMOUNT+'" onchange="calculateAmountexp(this)"></td>';
		body += '<td class="item-tax grey-bg" data-name="'+taxtype+'" data-tax="'+taxpercentage+'" style="position:relative;">';
		body += '<input type="hidden" name="tax_typeexp" value="'+data.TAX_TYPE+'" class="form-control">';
		body += '<span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
		body += '<input type="text" name="taxexp" class="form-control taxSearchexp" placeholder="Select a Tax" value="'+data.TAX_TYPE+'">';
		body += '</td>';
		body += '</tr>';		
		

		
		
		var amount = data.AMOUNT;		
		discountValue = parseFloat(amount*(percentage[1]/100)).toFixed(numberOfDecimal);
		tax.value = discountValue;
		
		if(addExpTaxList.length == 0){
			addExpTaxList.push(tax);
		}else{
			var match = false;
			$.each(addExpTaxList, function( key, data ) {
				if(data.types == percentage[0].trim()){
					data.value = parseFloat(parseFloat(data.value)+parseFloat(discountValue)).toFixed(numberOfDecimal);
					match = true;
				}
			});
			if(!match){
				addExpTaxList.push(tax);
			}
		}	
		
		$("input[name ='CUST_CODEexp']").val(data.CUST_CODE);
		$("input[name ='CUSTOMER']").val(data.CUSTOMER);
		$("input[name ='vendnoexp']").val(data.VENDNO);
		$("input[name ='vendnameexp']").val(data.VNAME);		
		$("input[name ='shipmentexp']").val(data.SHIPMENT_CODE);
		$("input[name ='ponoexp']").val(data.ORDERNO);
		$("input[name ='expenses_date']").val(data.EXPENSES_DATE);		
		$("input[name ='paid_through_account_name']").val(data.PAID_THROUGH);
		$("input[name ='referenceexp']").val(data.REFERENCE);
		$("input[name ='currencyexp']").val(data.CURRENCYID);
		if(data.ATTACHNOTE_COUNT!="0")
			$("#billAttchNoteexp").html(data.ATTACHNOTE_COUNT +" files attached");
	
	});
	$(".bill-tableexp tbody").html(body);
	renderTaxDetailsexp();
	removeSuggestionToTableexp();
	addSuggestionToTableexp();*/
	$.each(orders, function( key, data ) {	
		/* $("input[name ='CUST_CODEexp']").val(data.CUST_CODE);
		$("input[name ='CUSTOMER']").val(data.CUSTOMER); */
		/* $("input[name ='vendnoexp']").val(data.VENDNO);
		$("input[name ='vendnameexp']").val(data.VNAME); */		
		$("input[name ='shipmentexp']").val(data.SHIPMENT_CODE);
		$("input[name ='ponoexp']").val(data.ORDERNO);
		$("input[name ='cmdexp']").val('');
	});
}

$("#addexpensemodel").click(function(){
	var plant = document.form.plant.value;
	var cmd = document.createExpensesForm.cmdexp.value;
	var TranId = document.createExpensesForm.TranIdexp.value;
	var shiref = document.form.shipRef.value;
	var pono = document.form.pono.value;
	var refnum = document.form.refNum.value;
	var CURRENCY = document.form.CURRENCY.value;
	var CURRENCYID = document.form.CURRENCYID.value;
	var CURRENCYUSEQT = document.form.CURRENCYUSEQT.value;
	var basecurrency = document.form.BASECURRENCYID.value;
	var taxlable = $("input[name ='taxbylabelordermanagement']").val();
	var expencetax = $("input[name ='expencetax']").val();
	
	$("input[name ='refnumexp']").val(refnum);
	//var vendnameexp = document.createExpensesForm.vendnameexp.value;
	var sTAXTREATMENT = document.form.TAXTREATMENT_VALUE.value;
	document.getElementById('eCHK1').style.display = 'none';
	if(vendnameexp!="")
		{
			$('select[name ="eTAXTREATMENT"]').val(sTAXTREATMENT);
			if(sTAXTREATMENT =="GCC VAT Registered"||sTAXTREATMENT=="GCC NON VAT Registered"||sTAXTREATMENT=="NON GCC")
			{
				document.getElementById('eCHK1').style.display = 'block';
			}
		}
	if(cmd=="Edit")
	{

	if(TranId!="")
		{
		getEditDetailexp(TranId);
		
		}else{
			$("input[name ='ponoexp']").val(pono);
			document.getElementById('shipmentexp').disabled  = false;
			//$("input[name ='cmdexp']").val("");
			removeSuggestionToTableexp();
			addSuggestionToTableexp();
		}
	}else{
		$("input[name ='ponoexp']").val(pono);
		document.getElementById('shipmentexp').disabled  = false;
		removeSuggestionToTableexp();
		addSuggestionToTableexp();
	}
	
	$('input[name ="CURRENCYIDexp"]').val(CURRENCYID);
	$('input[name ="CURRENCYUSEQTexp"]').val(CURRENCYUSEQT);
	$('input[name ="CURRENCYUSEQTOLDexp"]').val(CURRENCYUSEQT);
	$('input[name ="currencyexp"]').val(CURRENCY);
	$('input[name ="BASECURRENCYIDexp"]').val(basecurrency);
	
	document.getElementById('exchangerateexp').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";
	document.getElementById('lbltotalexp').innerHTML = "Total ("+CURRENCYID+")"; //Author: Azees  Create date: July 29,2021  Description:  Total of Local Currency
	document.getElementById('basetotal').innerHTML = "Total ("+basecurrency+")"; //Author: Azees  Create date: July 29,2021  Description:  Total of Local Currency
	
	if(basecurrency!=CURRENCYID)
		document.getElementById('showtotalcurexp').style.display = 'block';	
	else
		document.getElementById('showtotalcurexp').style.display = 'none';
	
	$('#sdtaxexp').text('Standard '+taxlable);
	$('input[name ="GSTexp"]').val(expencetax);
	$("#Additional_Expenses_modal").modal("show");
	});
	
	function shipmentpopup(){
		var pono = document.createExpensesForm.ponoexp.value;
		$("input[name ='shipmentpono']").val(pono);
		$('#shipmentModal').css('z-index','1700');
		$("#shipmentModal").modal("show");
	} 
	
	function shipmentCallback(shipmentData){
		if(shipmentData.STATUS="SUCCESS"){
			//alert(shipmentData.MESSAGE);
			/* var checkBox = document.getElementById("expenses_for_PO");
			 if (checkBox.checked == true){	*/
			$("input[name ='ponoexp']").typeahead('val', shipmentData.PONO); 
			$("input[name ='shipmentexp']").typeahead('val', shipmentData.SHIPMENT_CODE);
			$('#Additional_Expenses_modal').css('overflow-y','scroll');
			$("#Additional_Expenses_modal").modal("show");
			/*  } */
		}
	}
	
	function checkbillable()
	{
		 var checkBox = document.getElementById("billable");
		 if (checkBox.checked == true){		 
			 $("input[name ='billable_status']").val("1");
			 $("#cusnamereq").addClass("required");
		 } else{		 
			 $("input[name ='billable_status']").val("0");
			 $("#cusnamereq").removeClass("required");
		 } 
	}
	
	$("#saveadditonexpense").click(function(){
		document.getElementById('shipmentexp').disabled  = false;
		var checkBox = document.getElementById("billable");
		if (checkBox.checked == true){	
			$('input[name ="bill_statusexp"]').val('UNBILLED');
		}else{
			$('input[name ="bill_statusexp"]').val('NON-BILLABLE');
		}
		$("#createExpensesForm").submit();
	});
	
/*	function Shipmentcheckexp(pono, shipmentcode){
		var plant = document.form.plant.value;
		$.ajax({
			type : "POST",
			url : "/track/ExpensesServlet",
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_EXPENSEHDRID",
				SHIPMENT_CODE : shipmentcode,
				PONO : pono
			},
			dataType : "json",
			success : function(data) {
				console.log(data);
				if(data.expense[0].length != 0){*/
					/*if(data.expense[0].STATUS == "BILLED"){
						alert("Shipping Reference "+shipmentcode+" processed already");
						$("input[name ='shipment']").val("");
						loadEmptyTable();
					}else{
						
						$("input[name ='cmd']").val("Edit");
						$("input[name ='TranId']").val(data.expense[0].ID);		
						
						getEditDetail(data.expense[0].ID);
						
					}*/
					/*$("#shipmentexp").typeahead('val', '"');
					$("#shipmentexp").typeahead('val', '');
					alert("Shipment Code already processed");
				
				}else{
					
				}
				
			}
		});
	}*/
	
	function Shipmentcheckexp(pono, shipmentcode){
		var plant = document.form.plant.value;
		
		
			$.ajax({
				type : "POST",
				url : "/track/MasterServlet",
				async : true,
				data : {
					PLANT : plant,
					action : "GET_EXPENSE_DETAIL_FOR_BILL",
					SHIPMENT_CODE : shipmentcode,
					ORDERNO : pono
				},
				dataType : "json",
				success : function(data) {
					if(data.expenses[0].SCODESTATUS == "YES"){
						$("#shipmentexp").typeahead('val', '"');
						$("#shipmentexp").typeahead('val', '');
						alert("Shipment Code already processed");
					}
				}
			});
		
		
	}
	
	
	function loadTaxTreatment()
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
						   $('#eTAXTREATMENT').append('<option value="' + value.text + '">' + value.text + '</option>');
						});
			}
		});
	}
	
	function getvendnameexp(TAXTREATMENT,VENDO,CURRENCYID,CURRENCY,CURRENCYUSEQT){

		getCurrencyidexp(CURRENCYID,CURRENCYUSEQT);
		$("#currencyexp").typeahead('val', CURRENCY);
		
		$('select[name ="eTAXTREATMENT"]').val(TAXTREATMENT);
		document.createExpensesForm.vendnoexp.value =VENDO;
		if(TAXTREATMENT =="GCC VAT Registered"||TAXTREATMENT=="GCC NON VAT Registered"||TAXTREATMENT=="NON GCC")
		{
			document.getElementById('eCHK1').style.display = 'block';
		}
		else
			document.getElementById('eCHK1').style.display = 'none';
		}
	
	function OnTaxeChange(TAXTREATMENT)
	{			
		document.getElementById("eREVERSECHARGE").checked = false;
		document.getElementById("eGOODSIMPORT").checked = false;
		if(TAXTREATMENT =="GCC VAT Registered"||TAXTREATMENT=="GCC NON VAT Registered"||TAXTREATMENT=="NON GCC")
		{
			document.getElementById('eCHK1').style.display = 'block';
		}
		else
			document.getElementById('eCHK1').style.display = 'none';
		}
	
	function setSupplierData(suppierData){	
		$("input[name ='vendnoexp']").val(suppierData.vendno);
		$("#vendnameexp").typeahead('val', suppierData.vendname);
		$('select[name ="eTAXTREATMENT"]').val(suppierData.sTAXTREATMENT);
		$('#eTAXTREATMENT').attr('disabled',false);
		if(suppierData.sTAXTREATMENT =="GCC VAT Registered"||suppierData.sTAXTREATMENT=="GCC NON VAT Registered"||suppierData.sTAXTREATMENT=="NON GCC")
		{
			document.getElementById('eCHK1').style.display = 'block';
		}
		else
			document.getElementById('eCHK1').style.display = 'none';
	}
	function loadtaxtretval()
	{

		var sTAXTREATMENT = $("input[name ='TAXTREATMENT_VALUE']").val();
		$('select[name ="eTAXTREATMENT"]').val(sTAXTREATMENT);
		if(sTAXTREATMENT =="GCC VAT Registered"||sTAXTREATMENT=="GCC NON VAT Registered"||sTAXTREATMENT=="NON GCC")
		{
			document.getElementById('eCHK1').style.display = 'block';
		}
		else
			document.getElementById('eCHK1').style.display = 'none';
	}
	function setCustomerData(customerData){	
		document.createExpensesForm.CUST_CODEexp.value=customerData.custno;
		//document.createExpensesForm.cusnameexp.value=customerData.custname;		
		$("#cusnameexp").typeahead('val', customerData.custname); 
	}
	$(".bill-tableexp tbody").on('click','.accrmv',function(){
		debugger;	    
	    var obj = $(this);
	    var timestamp = new Date().getUTCMilliseconds();
	    kayid = "key"+timestamp;
	    $(obj).closest('td').attr('id', kayid); 
	    $("input[name ='ukey']").val(kayid);
	});
	function getCurrencyidexp(CURRENCYID,CURRENCYUSEQT){
		var basecurrency = document.form.BASECURRENCYID.value;
		
		$('input[name ="CURRENCYIDexp"]').val(CURRENCYID);
		$('input[name ="CURRENCYUSEQTexp"]').val(CURRENCYUSEQT);
		setCURRENCYUSEQTexp(CURRENCYUSEQT);
		
		
		document.getElementById('exchangerateexp').innerHTML ="Exchange Rate ("+basecurrency+"/"+CURRENCYID+")";
		document.getElementById('lbltotalexp').innerHTML = "Total ("+CURRENCYID+")"; //Author: Azees  Create date: July 29,2021  Description:  Total of Local Currency
		document.getElementById('basetotal').innerHTML = "Total ("+basecurrency+")"; //Author: Azees  Create date: July 29,2021  Description:  Total of Local Currency
		
		if(basecurrency!=CURRENCYID)
			document.getElementById('showtotalcurexp').style.display = 'block';	
		else
			document.getElementById('showtotalcurexp').style.display = 'none';
	}
function changePaid(){
	if($("#paid").is(':checked')){
		$("#paidthroughfield").show();
	} else {
		$("#paidthroughfield").hide();
		$("#paid_through_account_name").val("");
	}
	
}

function calculateTaxEXP(obj, types, percentage, display, iszero, isshow, id){
	$("input[name=ptaxtypeexp]").val(types);
	$("input[name=ptaxpercentageexp]").val(percentage);
	$("input[name=ptaxdisplayexp]").val(display);
	$("input[name=ptaxiszeroexp]").val(iszero);
	$("input[name=ptaxisshowexp]").val(isshow);
	$("input[name=taxidexp]").val(id);
	$('.taxSearchexp').each(function(){
	    $(this).val(display);
	});
	calculateTotalexp();
}


function calculateAmountexp(obj){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var amount = parseFloat($(obj).closest('tr').find("td:nth-child(3)").find('input').val()).toFixed(numberOfDecimal);
	$(obj).closest('tr').find("td:nth-child(3)").find('input').val(amount);
	calculateTotalexp();
}

function calculateTotalexp(){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var ptaxiszero = $("input[name='ptaxiszeroexp']").val();
	var ptaxisshow = $("input[name='ptaxisshowexp']").val();
	var ptaxpercentage = $("input[name='ptaxpercentageexp']").val();
	var ptaxdisplay = $("input[name='ptaxdisplayexp']").val();
	
	var amount = 0, totalvalue=0;
	var rowcount=$(".bill-tableexp tbody tr").length;
	if(rowcount>0)
		{
			$(".bill-tableexp tbody tr td:nth-child(3").each(function() {
			    amount =  parseFloat(amount) + parseFloat($(this).find('input').val());
			    amount = parseFloat(amount).toFixed(numberOfDecimal);
			    $("#subTotalexp").html(amount);
			    $('input[name ="sub_totalexp"]').val(amount);// hidden input
			});
		}
	else
	{
		amount=0;
		amount=parseFloat(amount).toFixed(numberOfDecimal);
		$("#subTotalexp").html(amount);
		$('input[name ="sub_totalexp"]').val(amount);// hidden input
	}
	 
	var taxTotal = 0;

	if(ptaxiszero == "0" && ptaxisshow == "1"){
		taxTotal = parseFloat((amount/100)*ptaxpercentage);
	}

	 if(ptaxisshow == "1"){
			var html ="";
			html+='<div class="total-row">';
			html+='<div class="total-label">'+ptaxdisplay+'</div>';
			html+='<div class="total-amount taxAmountexp">'+parseFloat(parseFloat(taxTotal)).toFixed(numberOfDecimal)+'</div>';
			html+='</div>';
			$(".taxDetailsexp").html(html);
			$("#total_tax_amountexp").val(taxTotal); // hidden input}
		}else{
			$(".taxDetailsexp").html("");
			$("#total_tax_amountexp").val("0"); // hidden input}
		}
	 
	 
	 
	 totalvalue = parseFloat(parseFloat(amount) + parseFloat(taxTotal)).toFixed(numberOfDecimal);
	 $("#totalexp").html(totalvalue);
	 $("#total_amountexp").val(totalvalue); // hidden input}

	 var CURRENCYUSEQT = $('input[name ="CURRENCYUSEQTexp"]').val();//Author: Resvi  Add date: July 28,2021  Description:  Total of Local Currency
	 var convttotalvalue= (totalvalue/CURRENCYUSEQT).toFixed(numberOfDecimal)
	 $("#totalcurexp").html(convttotalvalue);
}

function changingtaxpercentageexp(){
	var baseamount = $("#GSTexp").val();
	var zeroval = parseFloat("0").toFixed(3);
	if(baseamount != ""){
		var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(baseamount.match(decimal) || baseamount.match(numbers)) 
		{ 
			$("#GSTexp").val(parseFloat(baseamount).toFixed(3));	
		}else{
			$("#GSTexp").val(zeroval);
			alert("Please Enter Valid Percentage");
		}
	}else{
		$("#GSTexp").val(zeroval);
	}
	removetaxdropdownexp();
	addtaxdropdownexp();
	taxresetexp();
}


function taxresetexp(){
	$("input[name=ptaxtypeexp]").val("");
	$("input[name=ptaxpercentageexp]").val("0");
	$("input[name=ptaxdisplayexp]").val("");
	$("input[name=ptaxiszeroexp]").val("1");
	$("input[name=ptaxisshowexp]").val("0");
	$("input[name=taxidexp]").val("0");
	$('.taxSearchexp').each(function(){
	    $(this).val("");
	});
	$("#Expensetaxexp").typeahead('val', '');
	calculateTotalexp();
}

function removetaxdropdownexp(){
	$("#Expensetaxexp").typeahead('destroy');
}
function addtaxdropdownexp(){
	var plant = document.form.plant.value;
	$("#Expensetaxexp").typeahead({
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
					GST_PERCENTAGE : $("input[name=GSTexp]").val(),
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
			return '<p onclick="calculateTaxEXP(this,\''+data.SGSTTYPES+'\',\''+data.SGSTPERCENTAGE+'\',\''+data.DISPLAY+'\',\''+data.ISZERO+'\',\''+data.ISSHOW+'\',\''+data.ID+'\')">' 
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

function setCURRENCYUSEQTexp(CURRENCYUSEQTCost){
	var numberOfDecimal = $("#numberOfDecimal").val();
	var oldeqtcost = $('input[name ="CURRENCYUSEQTOLDexp"]').val();
	var cost = 0;
	$('.bill-tableexp tbody tr').each(function () {
		cost = ((parseFloat($(this).find('input[name=amountexp]').val()))/parseFloat(oldeqtcost))*parseFloat(CURRENCYUSEQTCost);
		cost = parseFloat(cost).toFixed(numberOfDecimal);
		$(this).find('input[name=amountexp]').val(cost);
	});
	
	$("input[name ='CURRENCYUSEQTOLDexp']").val(CURRENCYUSEQTCost);
	
	calculateTotalexp();
}

function checknoexp(amount){
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