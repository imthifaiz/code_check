function populateJournal(tableId, limit, dataToPopulate, numberOfDecimal, pagination) {
	var tabulatorObj = new Tabulator("#" + tableId, {
		height:"500px", 
		//height:false,  
		data: dataToPopulate,
		layout: "fitColumns",
		pagination: pagination, //enable local pagination.
		//maxHeight:"100%", 
		paginationSize: limit,
		groupBy: ["JOURNALHDRID"],
		groupHeader: function (value, count, data, group) {
			var rows = group.getRows();
			var type = rows[0].getData().TRANSACTION_TYPE;
			var jdate = rows[0].getData().DATE;
			var jhdrid = rows[0].getData().JOURNALHDRID;
			/* var stringDate=""+jdate;
			console.log("Date"+stringDate);
			var journaldate=moment(stringDate).format('DD/MM/YYYY');
			var journaldateFormatted=moment(journaldate).format('DD MMM YYYY'); */
			var id;
			if (type === "JOURNAL" || type === "CONTRA") {
				id = rows[0].getData().JOURNALHDRID;
			}
			else {
				id = rows[0].getData().TRANSACTION_ID;
			}
			return "<i style='font-size:14px;' align='right'>" + jdate + "</i>-" + type + "&nbsp;&nbsp;&nbsp;" + id+"<button class='btn btn-danger' style='float: right;float: right;margin-right: 1%;font-size: 13px; padding: 0px 5px;' onclick='journalcontra("+jhdrid+")'>REVERSE</button><button class='btn btn-success' style='float: right;float: right;margin-right: 1%;font-size: 13px; padding: 0px 5px;' onclick='journalcopy("+jhdrid+")'>COPY</button>";
		},
		/* footerElement:"<div class='row'><div class='pull-left' style='margin-left:30px'>Gross Profit:</div><div class='pull-right' style='margin-right:10px' id='grossprofit'></div></div>", */
		columns: [
			{ title: "ACCOUNT",  field: "ACCOUNT" },

			{
				title: "DEBITS", field: "DEBIT",  bottomCalc: "sum", bottomCalcParams: {
					precision: numberOfDecimal,
				},
		        bottomCalcFormatter: "money",
		        bottomCalcFormatterParams:  {
		          decimal: ".",
		          thousand: ","
		        }, formatter: function (cell, formatterParams, onRendered) {
					var value = cell.getValue();
					return "<div align='right'>" + parseFloat(value).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,') + "</div>";

				}
			},
			{
				title: "CREDITS", field: "CREDIT", bottomCalc: "sum", bottomCalcParams: {
					precision: numberOfDecimal,
				},
		        bottomCalcFormatter: "money",
		        bottomCalcFormatterParams:  {
		          decimal: ".",
		          thousand: ","
		        }, formatter: function (cell, formatterParams, onRendered) {
					var value = cell.getValue();
					return "<div align='right'>" + parseFloat(value).toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,') + "</div>";

				}
			}
		],
	});
	return tabulatorObj;
}

/*var debitcreditCalc = function(values, data, calcParams){
	 var calc = 0;
	 values.forEach(function(value){
		 calc = parseFloat(calc) + parseFloat(value);
	 });
	console.log("values----------/"+values);
	console.log("calc----------/"+calc.toFixed(numberOfDecimal).replace(/\d(?=(\d{3})+\.)/g, '$&,'));
	return calc;
}
*/

function journalcontra(jid){
	//alert("reverse-----"+jid);
	
	/*$.ajax({
		type : "POST",
		url: '../JournalServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "JOURNAL_REVERSE_CONTRA",
			JID: jid
		},
		success : function(journalledgerlist) {
			
		}
	});*/
	
	  window.open("/track/JournalServlet?action=JOURNAL_REVERSE_CONTRA&JID="+jid,'_blank');
}

function journalcopy(jid){
	//alert("copy-----"+jid);
    //var url = rootURI + '/banking/journalcopy?ID='+jid; 
    //window.open(url, '_blank');
    
    window.open("/track/banking/journalcopy?ID="+jid,
						  '_blank');
}

function loadJournalDetailByJournalId(rootURI, journalId, containerId, tableId, limit, numberOfDecimal)
{
	$.ajax({
		type : "GET",
		url: rootURI + '/JournalServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "getJournalReportByJournalId",
			journalId: journalId
		},
		success : function(journalledgerlist) {
			var journalledgerArray=journalledgerlist;
			// var myJSON = JSON.stringify(journalledgerArray);
			//console.log(myJSON);
			if(limit=='All'){
				limit=journalledgerArray.length;
				limit=limit*2;
				//	If there is only one entry in journal detail, the total is going to the next page.
				if (limit == 2){
					limit = 3;
				}
			}
			populateJournal(tableId, limit, journalledgerArray, numberOfDecimal, false);
		}
	});
}		

function showJournalDetail(containerId, tableId){
	$('#' + containerId).show(500, function(){Tabulator.prototype.findTable("#" + tableId)[0].redraw()});
	$('#SVG_Down_Arrow').css("display", "none");
	$('#SVG_Up_Arrow').css("display", "block");
	$('html, body').animate({
		scrollTop: $('#journal_detail_box').offset().top
	}, 500);
}
function hideJournalDetail(containerId){
	$('#' + containerId).hide(500);
	$('#SVG_Down_Arrow').css("display", "block");
	$('#SVG_Up_Arrow').css("display", "none");
}