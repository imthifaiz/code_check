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

var tabledata;
var table_1;
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
			action : "getSalesTaxReturnForSG",
			taxheaderid:taxheaderid,
		},
		success : function(data) {
			//alert(data.isTaxReg);
			var myJSON = JSON.stringify(data);
			//alert(myJSON);
			table_1.replaceData(data);
			//loadTaxExpenseData();
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
	    {title:"<div style='text-align:right'>AMOUNT</div>", field:"TAXABLE_AMOUNT", align:"right",headerSort:false,formatter:function(cell, formatterParams){
	    	var data = cell.getData();	
	    	if(data.BOX=='4' || data.BOX=='9' || data.BOX=='13'){
	    		return data.CURRENCY +data.tamount;
    		}else if(data.BOX=='8'){
    			if (data.tamount >= 0) {
    				return data.CURRENCY +data.tamount;
    			}else{
    				return "- "+data.CURRENCY +Math.abs(data.tamount);
    			}
	    	}else if(data.BOX=='1' || data.BOX=='2' || data.BOX=='3' || data.BOX=='5' || data.BOX=='6' || data.BOX=='7'){
	    		return "<a style='align:center' href='/track/jsp/sg-transactionsummary.jsp?DESCHEAD="+data.DESC_HEADING+"&BOX="+data.BOX+"&taxheader="+taxheaderid+"'>" + data.CURRENCY +data.tamount+"</a>";
	    	}else{
	    		return "<a style='align:center' href='/track/jsp/sg-transactionsummary.jsp?DESCHEAD="+data.DESC_HEADING+"&BOX=13&taxheader="+taxheaderid+"'>" + data.CURRENCY +data.tamount+"</a>";
	    	}
            
    }},
    	{title:"ADJUSTMENTS", field:"ADJUSTMENTS",align:"center",headerSort:false,width:90,formatter:function(cell, formatterParams){
	    	var data = cell.getData();
	    	if(data.BOX=='6')
	    		{
	    			return "<a style='align:center' data-toggle='modal' data-target='#sg-taximportadjustmentModal' onclick='callAdjustmentModal("+data.BOX+")'>Adjust</a>";
	    		}else if(data.BOX=='7')
	    		{
	    			return "<a style='align:center' data-toggle='modal' data-target='#sg-taximportadjustmentModal' onclick='callAdjustmentModal("+data.BOX+")'>Adjust</a>";
	    		}
	    	else
	    		{
	    		return "";
	    		}
       
    	}},
    
	    ],
	});
	loadTaxSalesData();
		  
}


function callAdjustmentModal(abox)
{
	if(abox == "6"){
		$('input[name ="adtaxkey"]').val("OUTBOUND");
	}
	if(abox == "7"){
		$('input[name ="adtaxkey"]').val("INBOUND");
	}
	$('#POPBOX').val(abox);
	$('#modaltaxheaderid').val(taxheaderid);
	removetaxTable();
	addtaxTable();
}

function reloadData()
{
	loadTaxSalesData();
	//table.replaceData
}

function markFiled()
{
	$("#sg-taxreturnfilepopup").modal("show");
}


function fileTax()
{
	$("#sg-taxreturnfilepopup").modal("hide");
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
			window.location.href="../tax/sg-gstreturnsummary";
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
			window.location.href="../tax/sg-gstreturnsummary";
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