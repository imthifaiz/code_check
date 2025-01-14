var tabledata1 = [];
var tabledata2 = [];
var table1;
var table2;
$(document).ready(function() {
	taxPaymentHdrSummaryData();
	taxPaymentDetSummaryData();
	$(".nav-tabs a").click(function(){
	    $(this).tab('show');
	  });
	  $('.nav-tabs a').on('show.bs.tab', function(){
	    //alert('The new tab is about to be shown.');
	  });
	  $('.nav-tabs a').on('shown.bs.tab', function(e){
		 // $("#taxreturnpayments-taxdues").tabulator("redraw");
		 // $("#taxreturnpayments-paymenthistory").tabulator("redraw");
		  if($(e.target).attr('id')=="due-tab")
			  {
			  	table1.redraw();
			  }
		  else
			  {
			  	table2.redraw();
			  }
		  
		  
	  });
	  
					});
function taxPaymentHdrSummaryData()
{
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		dataType: 'json',
		data : {
			action : "taxPaymentHdrSummary",
		},
		success : function(data) {
			var myJSON = JSON.stringify(data);
			//alert(myJSON);
			tabledata1=data;
			loadTaxPaymentDueSummary();
		}
	});
}
function taxPaymentDetSummaryData()
{
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		dataType: 'json',
		data : {
			action : "taxPaymentDetSummary",
		},
		success : function(data) {
			var myJSON = JSON.stringify(data);
			//alert(myJSON);
			tabledata2=data;
			loadTaxPaymentHistory();
		}
	});
}
function loadTaxPaymentDueSummary()
{
	table1 = new Tabulator("#taxreturnpayments-taxdues", {
	    layout:"fitColumns",
	    data:tabledata1,
	    columns:[
	    {title:"GST RETURNS", field:"TO_DATE",formatter:function(cell, formatterParams, onRendered){
	    	var to = moment(cell.getValue(),"DD/MM/YYYY");
	    	var toDate=to.format("MMM-YYYY");
    		return toDate;
    }},
	    {title:"TOTAL GST PAYABLE", field:"TOTAL_TAXPAYABLE",formatter:function(cell, formatterParams, onRendered){
	    	var value=cell.getValue();
    		return "AED"+value.toFixed(2);
    }},
	    {title:"TOTAL GST RECLAIMABLE", field:"TOTAL_TAXRECLAIMABLE",formatter:function(cell, formatterParams, onRendered){
	    	
	    	var value=cell.getValue();
    		return "AED"+value.toFixed(2);
    }},
	    {title:"BALANCE DUE", field:"BALANCEDUE",formatter:function(cell, formatterParams, onRendered){
	    	
	    	var value=cell.getValue();
    		return "AED"+value.toFixed(2);
    }},
	    {title:" ", field:"ID",formatter:function(cell, formatterParams, onRendered){
	    	
	    		return "<button type='button' class='btn btn-primary'>Record Payment</button>";
	    },cellClick:function(e, cell){
	    	var data = cell.getData();
	    	$("#modaltaxpayheaderid").val(cell.getValue());
	    	$("#modaltaxreturndate").val(data.TO_DATE);
	    	$("#tottaxpay").val(data.TOTAL_TAXPAYABLE);
	    	$("#amountdue").val(data.BALANCEDUE);
	    	$("#uae-taxreturnpaymentpopup").modal("show");
	    	//alert("Record Payment");
        }},
	    ],
	});
	 table1.redraw();
}
function loadTaxPaymentHistory()
{
	table2 = new Tabulator("#taxreturnpayments-paymenthistory", {
	    layout:"fitColumns",
	    data:tabledata2,
	    columns:[
	    {title:"GST RETURNS", field:"TAX_RETURN"},
	    {title:"DATE", field:"DATE"},
	    {title:"AMOUNT PAID", field:"AMOUNT_PAID",formatter:function(cell, formatterParams, onRendered){
	    	
	    	var value=cell.getValue();
    		return "AED"+value.toFixed(2);
    }},
	    {title:"AMOUNT RECLAIMED", field:"AMOUNT_RECLAIMED",formatter:function(cell, formatterParams, onRendered){
	    	
	    	var value=cell.getValue();
    		return "AED"+value.toFixed(2);
    }},
	    {title:" ", field:"ID",formatter:function(cell, formatterParams, onRendered){
	    	
	    		return "<button type='button' class='btn btn-primary'>Delete Payment</button>";
	    },cellClick:function(e, cell){
	    	var data = cell.getData();
	    	var value=cell.getValue();
	    	var tax_id=data.PAYMENTHDR_ID;
	    	$.ajax({
	    		type : "POST",
	    		url: '/track/TaxReturnFiling',
	    		dataType: 'json',
	    		data : {
	    			action : "deleteTax",
	    			paymentid:value,
	    			taxid:tax_id,
	    			amount:data.AMOUNT_PAID
	    		},
	    		success : function(data) {
	    			if(data.STATUS=="SUCCESS")
					{
	    				taxPaymentHdrSummaryData();
	    				taxPaymentDetSummaryData();
					}
	    		}
	    	});
	    	
        }},
	    ],
	});
	table2.redraw();
}

function payTax()
{
	var formData = $('form[name="taxpayment"]').serialize();
	$.ajax({
		type : "POST",
		url: '/track/TaxReturnFiling?action=payTax',
		dataType: 'json',
		data : formData,
		success : function(data) {
			//alert(data.STATUS);
			if(data.STATUS=="SUCCESS")
				{
					var frm = document.getElementsByName('taxpayment')[0];
					frm.reset(); 
					$("#uae-taxreturnpaymentpopup").modal("hide");
					taxPaymentHdrSummaryData();
					taxPaymentDetSummaryData();
				}
			else
				{
				$("#uae-taxreturnpaymentpopup").modal("hide");
				alert("Payment Failed!");
				}
			
		}
	});
}