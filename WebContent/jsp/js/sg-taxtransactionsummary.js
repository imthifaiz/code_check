var taxheaderid;
var tabledata;
var box;
$(document).ready(function() {
	 taxheaderid=$('#taxheaderid').val();
	 box=$('#box').val();
	 loadTaxHeader();
	taxTranSummaryData();
	$('.printMe').click(function(){
		printdoc();
	 
	});
	
	$("#pdfdownload").click(function(){
		generate();
	});
		
});
function loadTaxHeader()
{
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		dataType: 'json',
		data : {
			action : "getTaxReturnById",
			taxheaderid:taxheaderid,
		},
		success : function(data) {
			//alert(data.taxheader.STATUS);
			//var toOrginDate=data.taxheader.TO_DATE;
			var to = moment(data.taxheader.TO_DATE,"DD/MM/YYYY");
			var from = moment(data.taxheader.FROM_DATE,"DD/MM/YYYY");
			var fromDate=from.format("DD MMM YYYY");
			var toDate=to.format("DD MMM YYYY");
			//alert(fromDate);
			$('#taxFrom').html(fromDate);
			$('#taxTo').html(toDate); 
			$("input[name=taxFrom]").val(fromDate);
			$("input[name=taxTo]").val(toDate);
			
			//console.log(data);
		}
	});

}
function loadTaxTranSummary()
{
	var table = new Tabulator("#taxtransactionsummary", {
	    layout:"fitColumns",
	    data:tabledata,
	    columns:[
	    {title:"DATE", field:"DATE"},
	    {title:"ENTRY#", field:"ENTRY"},
	    {title:"TRANSACTION TYPE", field:"TRANSACTION_TYPE"},
	    {title:"<div style='text-align:right'>TAXABLE AMOUNT</div>", 
	    	field:"TAXABLE_AMOUNT", 
	    	align:"right",
	    	formatter:function(cell, formatterParams){
	    	    var data = cell.getData();	
	    	    return parseFloat(data.TAXABLE_AMOUNT).toFixed("2");
	        }
	    },
	    {title:"<div style='text-align:right'>TAX AMOUNT</div>", 
	    	field:"TAX_AMOUNT", 
	    	align:"right",
	    	formatter:function(cell, formatterParams){
	    		var data = cell.getData();	
	    		return parseFloat(data.TAX_AMOUNT).toFixed("2");
	        }	
	    },
	    ],
	});
}
function taxTranSummaryData()
{
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		dataType: 'json',
		data : {
			action : "transactionSummaryForSG",
			taxheaderid:taxheaderid,
			box:box
		},
		success : function(data) {
			var myJSON = JSON.stringify(data);
			//alert(myJSON);
			tabledata=data;
			loadTaxTranSummary();
		}
	});
}
function transformDate(d)
{
	var months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
	var month=months[d.getMonth()];
	var date=d.getDate();
	var year=d.getFullYear();
	
	return date+' '+month+' '+year+' ';
	}






