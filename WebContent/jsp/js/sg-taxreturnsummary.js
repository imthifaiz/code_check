
/*var tabledata = [
    {id:1, status:"Unfiled", taxreturn:"June-2019", filedon:"11 Mar 2020", taxpayable:"AED0.00", taxreclaimable:"-",balancedue:"AED0.00" },
    {id:2, status:"Unfiled", taxreturn:"June-2019", filedon:"11 Mar 2020", taxpayable:"AED0.00", taxreclaimable:"-",balancedue:"AED0.00" },
    {id:3, status:"Unfiled", taxreturn:"June-2019", filedon:"11 Mar 2020", taxpayable:"AED0.00", taxreclaimable:"-",balancedue:"AED0.00" },
    {id:4, status:"Unfiled", taxreturn:"June-2019", filedon:"11 Mar 2020", taxpayable:"AED0.00", taxreclaimable:"-",balancedue:"AED0.00" },
    {id:5, status:"Unfiled", taxreturn:"June-2019", filedon:"11 Mar 2020", taxpayable:"AED0.00", taxreclaimable:"-",balancedue:"AED0.00" },
];*/
var tabledata = [];
var fillButton = function(value, data, cell, row, options){ //plain text value
	//var currStatus=cell.getRow().getData();
	//alert(currStatus.STATUS);
	alert(data.STATUS);
    return "<button type='button' class='taxfilestatusbutton'>Unfiled</button>"
};
$(document).ready(function() {
	taxSummaryData();
	
		
					});

function loadTaxSummary()
{
	var bcurrency =$('[name=baseCurrency]').val();
	var nodecimal =$('[name=numberOfDecimal]').val();
	var table = new Tabulator("#taxreturnsummary-table", {
	    layout:"fitColumns",
	    data:tabledata,
	    columns:[
	    {title:"STATUS", field:"STATUS",formatter:function(cell, formatterParams, onRendered){
	    	if(cell.getValue()==="Unfiled")
	    		return "<button type='button' class='taxunfiledstatusbutton'>"+cell.getValue()+"</button>";
	    	else
	    		return "<button type='button' class='taxfiledstatusbutton'>"+cell.getValue()+"</button>";
	    },cellClick:function(e, cell){
	    	
	    	showTaxReturnDetail(cell.getRow().getData());
        }},
	    {title:"GST RETURNS", field:"FROM_DATE",formatter:function(cell, formatterParams, onRendered){
	    	var value=cell.getValue();
	    	var to = moment(value,"DD/MM/YYYY");
			var toDate=to.format("MMM YYYY");
    		return toDate;
    }},
	    {title:"FILED ON", field:"FILED_ON"},
	    {title:"TOTAL GST PAYABLE", field:"TOTAL_TAXPAYABLE",formatter:function(cell, formatterParams, onRendered){
	    	var value=cell.getValue();
    		return bcurrency+value.toFixed(nodecimal);
    }},
	    {title:"TOTAL GST RECLAIMABLE", field:"TOTAL_TAXRECLAIMABLE",formatter:function(cell, formatterParams, onRendered){
	    	var value=cell.getValue();
    		return bcurrency+value.toFixed(nodecimal);
    }},
	    {title:"BALANCE DUE", field:"BALANCEDUE",formatter:function(cell, formatterParams, onRendered){
	    	var value=cell.getValue();
    		return bcurrency+value.toFixed(nodecimal);
    }},
    ],
	});
}
function taxSummaryData()
{
	//alert("stt");
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		dataType: 'json',
		data : {
			action : "getTaxReturns",
		},
		success : function(data) {
			var myJSON = JSON.stringify(data);
			//alert(myJSON);
			tabledata=data;
			loadTaxSummary();
		}
	});
}
function generateTaxReturn()
{
	$.ajax({
		type : "GET",
		url: '/track/TaxReturnFiling',
		dataType: 'json',
		data : {
			action : "getTaxSettings",
		},
		success : function(data) {
			//alert(data.REPORTINGPERIOD);
			if(data.REPORTINGPERIOD=="Monthly" || data.REPORTINGPERIOD=="Quarterly" || data.REPORTINGPERIOD=="Half-Yearly" || data.REPORTINGPERIOD=="Yearly")
				{
					//alert("Mon");
					$.ajax({
						type : "POST",
						url: '/track/TaxReturnFiling',
						data : {
							action : "generateTaxForSG",
							reportingperiod:data.REPORTINGPERIOD,
						},
						success : function(data) {
							taxSummaryData();
						},
						error: function(xhr, status, error) {
							$("#sg-taxreturncustompopup").modal("hide");
							  if(xhr.responseText==="pending")
								  {
								  	alert("You cannot generate new GST Returns until you file your previous GST Return");
								  }
							  
							}
					});
				}else if(data.REPORTINGPERIOD=="Custom")
				{
					//$('#uae-taxreturncustompopup').show();
					//$('#vatenddate').datepicker({minDate: 0});
				     $('[name=vatstartdate]').val(data.fromDate);
					 $("#sg-taxreturncustompopup").modal("show");
				}
			
			
			//console.log(data);
		}
	});
}
function generateTaxCustom()
{
	var vatenddate=$('[name=vatenddate]').val();
	if(vatenddate=="" || vatenddate==null)
		{
			alert("please enter end date");
			return false;
		}
	$.ajax({
		type : "POST",
		url: '/track/TaxReturnFiling',
		data : {
			action : "generateTaxForSG",
			reportingperiod:"Custom",
			enddate:vatenddate
		},
		success : function(data) {
			taxSummaryData();
			$("#sg-taxreturncustompopup").modal("hide");
			var frm = document.getElementsByName('taxcustomperiod')[0];
			frm.reset(); 
		},
		error: function(xhr, status, error) {
			$("#sg-taxreturncustompopup").modal("hide");
			  if(xhr.responseText==="pending")
				  {
				  	alert("You cannot generate new GST Returns until you file your previous GST Return");
				  }
			  
			}
	});
}
function showTaxReturnDetail(data)
{
	//var myJSON = JSON.stringify(data);
	//alert(myJSON);
	window.location.href="../tax/sg-returnfill?ID="+data.ID;
}

