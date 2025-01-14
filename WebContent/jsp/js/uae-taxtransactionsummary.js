var taxheaderid;
var box;
$(document).ready(function() {
	 taxheaderid=$('#taxheaderid').val();
	 box=$('#box').val();
	 loadTaxHeader();
	taxTranSummaryData();

		
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
	    {title:"TAXABLE AMOUNT", field:"TAXABLE_AMOUNT"},
	    {title:"TAX AMOUNT", field:"TAX_AMOUNT"},
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
			action : "transactionSummary",
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