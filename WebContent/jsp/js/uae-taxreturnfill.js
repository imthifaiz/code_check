var taxheaderid;
var globalFromDate;
var globalToDate;
$(document).ready(function(){
  $('[data-toggle="tooltip"]').tooltip();  
  taxheaderid=$('#taxheaderid').val();
  loadTaxHeader();
  $('#unfillButton').hide();
  //alert(taxheaderid);
  loadTaxFillTable_1();
  
  
  $('#includeprevioustax').on('change', function(){
		var value = $(this).val();
		//alert(value);
		if(this.checked) {
			//alert("Checked");
			includePreviousTax();
		}
		else
		{
			//alert("UnChecked");
			excludePreviousTax();
		}
	});
  
  
}); 	
/*var tabledata = [
    {id:1, box:"1a", description:"Standard rated supplies in Abu Dhabi Total value of all standard rated goods and services (exclusive of VAT) sold in the Abu Dhabi in the current reporting period, and the VAT that was collected on their sale or adjustments to similar sales made in the previous reporting periods.", taxableamt:"AED0.00", taxamt:"AED0.00", adjustment:"AED0.00"},
    {id:2, box:"1b", description:"Standard rated supplies in Abu Dhabi Total value of all standard rated goods and services (exclusive of VAT) sold in the Abu Dhabi in the current reporting period, and the VAT that was collected on their sale or adjustments to similar sales made in the previous reporting periods.", taxableamt:"AED0.00", taxamt:"AED0.00", adjustment:"AED0.00"},
    {id:3, box:"1c", description:"Standard rated supplies in Abu Dhabi Total value of all standard rated goods and services (exclusive of VAT) sold in the Abu Dhabi in the current reporting period, and the VAT that was collected on their sale or adjustments to similar sales made in the previous reporting periods.", taxableamt:"AED0.00", taxamt:"AED0.00", adjustment:"AED0.00"},
    {id:4, box:"1d", description:"Standard rated supplies in Abu Dhabi Total value of all standard rated goods and services (exclusive of VAT) sold in the Abu Dhabi in the current reporting period, and the VAT that was collected on their sale or adjustments to similar sales made in the previous reporting periods.", taxableamt:"AED0.00", taxamt:"AED0.00", adjustment:"AED0.00"},
    {id:5, box:"1e", description:"Standard rated supplies in Abu Dhabi Total value of all standard rated goods and services (exclusive of VAT) sold in the Abu Dhabi in the current reporting period, and the VAT that was collected on their sale or adjustments to similar sales made in the previous reporting periods.", taxableamt:"AED0.00", taxamt:"AED0.00", adjustment:"AED0.00"},
    {id:6, box:"1f", description:"Standard rated supplies in Abu Dhabi Total value of all standard rated goods and services (exclusive of VAT) sold in the Abu Dhabi in the current reporting period, and the VAT that was collected on their sale or adjustments to similar sales made in the previous reporting periods.", taxableamt:"AED0.00", taxamt:"AED0.00", adjustment:"AED0.00"},
    {id:7, box:"1g", description:"Standard rated supplies in Abu Dhabi Total value of all standard rated goods and services (exclusive of VAT) sold in the Abu Dhabi in the current reporting period, and the VAT that was collected on their sale or adjustments to similar sales made in the previous reporting periods.", taxableamt:"AED0.00", taxamt:"AED0.00", adjustment:"AED0.00"},
];*/
var tabledata;
var table_1;
var table_2;
var table_3;
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
			if(data.taxheader.STATUS=="Unfiled")
				{
					$('#ribbon-unfill').show();
					$('#ribbon-fill').hide();
					$('#fillButton').show()
					$('#unfillButton').hide();
				}
			else
				{
					$('#ribbon-unfill').hide();
					$('#ribbon-fill').show();
					$('#fillButton').hide()
					$('#unfillButton').show();
				}
			if(data.taxheader.TAXPREVIOUSINCLUDED)
				{
					$('#includeprevioustax').prop('checked', true);
				}
			
			var to = moment(data.taxheader.TO_DATE,"DD/MM/YYYY");
			var from = moment(data.taxheader.FROM_DATE,"DD/MM/YYYY");
			globalFromDate=data.taxheader.FROM_DATE;
			var fromDate=from.format("DD MMM YYYY");
			var toDate=to.format("DD MMM YYYY");
			//var toDate=transformDate(to);
			//alert(fromDate);
			$('#taxFrom').html(fromDate);
			$('#taxTo').html(toDate); 
			$('#reportperiod').html('('+data.taxheader.REPORTING_PERIOD+')');
			$('#inFrom').html(fromDate);
			$('#inTo').html(toDate); 
			//table_1.setData(data);
			
			//console.log(data);
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
function loadTaxSalesData()
{
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		dataType: 'json',
		data : {
			action : "getSalesTaxReturn",
			taxheaderid:taxheaderid,
		},
		success : function(data) {
			//alert(data.isTaxReg);
			var myJSON = JSON.stringify(data);
			//alert(myJSON);
			table_1.replaceData(data);
			loadTaxExpenseData();
			//console.log(data);
		}
	});

}
function loadTaxExpenseData()
{
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		dataType: 'json',
		data : {
			action : "getExpenseTaxReturn",
			taxheaderid:taxheaderid,
		},
		success : function(data) {
			//alert(data.isTaxReg);
			var myJSON = JSON.stringify(data);
			//alert(myJSON);
			table_2.setData(data);
			loadTaxDueData();
			//console.log(data);
		}
	});

}
function loadTaxDueData()
{
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		dataType: 'json',
		data : {
			action : "getDueTaxReturn",
		},
		success : function(data) {
			//alert(data.isTaxReg);
			var myJSON = JSON.stringify(data);
			//alert(myJSON);
			table_3.setData(data);
			
			//console.log(data);
		}
	});

}
function loadTaxFillTable_1()
{
		table_1 = new Tabulator("#taxreturnfill-table1", {
	    layout:"fitColumns",
	    columns:[
	    {title:"BOX#", field:"BOX",headerSort:false,width:50},
	    {title:"DESCRIPTION", field:"DESCRIPTION",headerSort:false,width:700,formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	      
	            return "<span style='font-weight:bold;'>" + data.DESC_HEADING + "</span><br><span style='white-space: normal;height:40px'>" + data.DESCRIPTION + "</span>";
	    }},
	    {title:"TAXABLE AMOUNT", field:"TAXABLE_AMOUNT",headerSort:false,formatter:function(cell, formatterParams){
	    	var data = cell.getData();	
	    	if(data.BOX=='8')
    		{
	    		
    		}
	    	else if(data.BOX=='2')
	    		{
	    			return data.CURRENCY +"0.00";
	    		}
	    	else
	    		{
	    		return "<a style='align:center' href='/track/jsp/uae-transactionsummary.jsp?DESCHEAD="+data.DESC_HEADING+"&BOX="+data.BOX+"&taxheader="+taxheaderid+"'>" + data.CURRENCY +data.taxableamount+"</a>";
	    		}
            
    }},
	    {title:"TAX AMOUNT", field:"TAXAMOUNT",align:"center",headerSort:false,formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	    	
	    		return data.CURRENCY +data.taxamount;
    }},
	    {title:"ADJUSTMENTS", field:"ADJUSTMENTS",align:"center",headerSort:false,formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	    	if(data.BOX=='7')
	    		{
	    			return "<a style='align:center' data-toggle='modal' data-target='#uae-taximportadjustmentModal' onclick='callAdjustmentModal("+data.BOX+")'>"+data.CURRENCY +"0.00</a>";
	    		}
	    	else
	    		{
	    		return data.CURRENCY +"0.00";
	    		}
           
    }},
	    ],
	});
		loadTaxSalesData();
		loadTaxFillTable_2();
		  
		  
}
function loadTaxFillTable_2()
{
		table_2 = new Tabulator("#taxreturnfill-table2", {
	    layout:"fitColumns",
	    columns:[
	    {title:"BOX#", field:"BOX",headerSort:false,width:50},
	    {title:"DESCRIPTION", field:"DESCRIPTION",headerSort:false,width:700,formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	      
	            return "<span style='font-weight:bold;'>" + data.DESC_HEADING + "</span><br><span style='white-space: normal;height:40px'>" + data.DESCRIPTION + "</span>";
	    }},
	    {title:"TAXABLE AMOUNT", field:"TAXABLE_AMOUNT",headerSort:false,formatter:function(cell, formatterParams){
	    	var data = cell.getData();		      
	    	if(data.BOX=='11')
    			{
	    		
    			}
	    	else
	    		{
	    			return "<a style='align:center' href='/track/jsp/uae-transactionsummary.jsp?DESCHEAD="+data.DESC_HEADING+"&BOX="+data.BOX+"&taxheader="+taxheaderid+"'>" + data.CURRENCY +data.taxableamount+"</a>";
	    		}
            
    }},
	    {title:"TAX AMOUNT", field:"TAXAMOUNT",align:"center",headerSort:false,formatter:function(cell, formatterParams){
	    	var data = cell.getData();		      
	    	return data.CURRENCY +data.taxamount;
    }},
	    {title:"ADJUSTMENTS", field:"ADJUSTMENTS",align:"center",headerSort:false,formatter:function(cell, formatterParams){
	    	var data = cell.getData();		      
            return data.CURRENCY +"0.00";
    }},
	    ],
	});
		loadTaxFillTable_3();
}
function loadTaxFillTable_3()
{
		table_3 = new Tabulator("#taxreturnfill-table3", {
	    layout:"fitColumns",
	    columns:[
	    {title:"BOX#", field:"BOX",headerSort:false,width:50},
	    {title:"DESCRIPTION", field:"DESCRIPTION",headerSort:false,width:700,formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	      
	            return "<span style='font-weight:bold;'>" + data.DESC_HEADING + "</span><br><span style='white-space: normal;height:40px'>" + data.DESCRIPTION + "</span>";
	    }},
	    {title:"TAX AMOUNT", field:"TAXAMOUNT",align:"center",headerSort:false,formatter:function(cell, formatterParams){
	    	var data = cell.getData();		      
	    	return data.CURRENCY +data.taxamount;
    }},
	    ],
	});
}

function callAdjustmentModal()
{
	//alert(boxid);
	$('#modaltaxheaderid').val(taxheaderid);
}
function reloadData()
{
	loadTaxSalesData();
	//table.replaceData
}

function markFiled()
{
	$("#uae-taxreturnfilepopup").modal("show");
}
function fileTax()
{
	$("#uae-taxreturnfilepopup").modal("hide");
	var fileon=$("#filedon").val();
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		data : {
			action : "fillTax",
			taxheaderid:taxheaderid,
			fileddate:fileon
		},
		success : function(data) {
			//alert(data);
			$('#fillButton').hide()
			$('#unfillButton').show();
			$('#ribbon-unfill').hide();
			$('#ribbon-fill').show();
			
		}
	});
}
function unFiled()
{
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		data : {
			action : "unfillTax",
			taxheaderid:taxheaderid,
		},
		success : function(data) {
			//alert(data);
			$('#fillButton').show()
			$('#unfillButton').hide();
			$('#ribbon-unfill').show();
			$('#ribbon-fill').hide();
		}
	});
}

function includePreviousTax()
{
	$.ajax({
		type : "POST",
		url: '/track/TaxReturnFiling',
		data : {
			action : "includePrevious",
			taxheaderid:taxheaderid,
			from:globalFromDate
		},
		success : function(data) {
			if(data.STATUS=="SUCCESS")
				{
					loadTaxSalesData();
				}
		}
	});
}
function excludePreviousTax()
{
	$.ajax({
		type : "POST",
		url: '/track/TaxReturnFiling',
		data : {
			action : "excludePrevious",
			taxheaderid:taxheaderid,
		},
		success : function(data) {
			if(data.STATUS=="SUCCESS")
			{
				loadTaxSalesData();
			}
		}
	});
}