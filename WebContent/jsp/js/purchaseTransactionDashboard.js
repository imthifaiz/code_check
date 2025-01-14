var rootURI;
$(document).ready(function() {
	rootURI = location.href.toString();
	rootURI = rootURI.substring(0, rootURI.indexOf("/purchaseTransactionDashboard"));
	$('[data-toggle="tooltip"]').tooltip(); 
		$('.box-header').each(function() {
			if ($(this).find('select').length == 0) {
				$(this).find('h3').css('padding-bottom', '10px');
			}
		});
		$('.right-div').css({
			'height' : $('.left-div').innerHeight()
		});
		
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			  var target = $(e.target).attr("href") // activated tab
			 if(target == "#warehouseDashboard"){
				  var totalReceivedItems = getPeriod('TotalReceivedItemsSummary', 'Last 30 days', 'receivedItemsRange');
				  getTotalReceivedItems(totalReceivedItems);
				  var totalIssuedItems = getPeriod('TotalIssuedItemsSummary', 'Last 30 days', 'TotalIssuedRage');
				  getTotalIssuedItems(totalIssuedItems);
				  var PoWoPriceSummary = getPeriod('POWithOutPriceSummary','Last 30 days','POWithoutPrice' );
				  getPoWoPriceSummary(PoWoPriceSummary);
				  var grnSummary = getPeriod('GrnSummary', 'Last 30 days','GRNSummary');
				  getGrnSummary(grnSummary);
				  var ReadyToPackOrders = getPeriod('ProductsReadyToPackFromSalesOrderSummary', 'Today','ProductsReadyToPackFromSalesOrder');
				  getReadyToPackOrders(ReadyToPackOrders);
				  getStockReplanishmentProducts();
				  calendar5.render();
			  }
		});	
				
		    
	    var calendarE5 = document.getElementById('calendar5');

	    calendar5 = new FullCalendar.Calendar(calendarE5, {
	      plugins: [ 'interaction', 'dayGrid' ],
	      header: {
	        left: 'prevYear,prev,next,nextYear',
	        center: 'title',
	        right: 'dayGridMonth,dayGridWeek,dayGridDay'
	      },
	      defaultDate: '2020-06-18',
	      navLinks: true, // can click day/week names to navigate views
	      editable: true,
	      eventLimit: true, // allow "more" link when too many events
	      events: {
	          url: rootURI + '/DashboardServlet?ACTION=PURCHASE_DELIVERY_DATE',
	          failure: function() {
	            document.getElementById('script-warning').style.display = 'block'
	          }
	        },
		      eventRender: function(info) {
		    	  tippy(info.el, {
		    		  content: 'Pono : '+ info.event.extendedProps.PONO +'<br>'+
	    	        		'Supplier Name : '+ info.event.extendedProps.CUSTNAME +'<br>'+
	    	        		'Order Date : '+ info.event.extendedProps.COLLECTIONDATE +'<br>'+
	    	        		'Delivery Date : '+ info.event.extendedProps.DELIVERY_DATE +'<br>'+
	    	        		'Total Qty : '+ info.event.extendedProps.TOTAL_QTY +'<br>',
	    	        		allowHTML: true,
	    	        		placement: 'right',
	    	      });
	    	  }
	    });
	    
	    var calendarE6 = document.getElementById('calendar6');

	    calendar6 = new FullCalendar.Calendar(calendarE6, {
	      plugins: [ 'interaction', 'dayGrid' ],
	      header: {
	        left: 'prevYear,prev,next,nextYear',
	        center: 'title',
	        right: 'dayGridMonth,dayGridWeek,dayGridDay'
	      },
	      defaultDate: '2020-06-18',
	      navLinks: true, // can click day/week names to navigate views
	      editable: true,
	      eventLimit: true, // allow "more" link when too many events
	      events: {
	          url: rootURI + '/DashboardServlet?ACTION=SALES_ORDER_EXPIRED_DELIVERY_DATE',
	          failure: function() {
	            document.getElementById('script-warning').style.display = 'block'
	          }
	        },
		      eventRender: function(info) {
		    	  tippy(info.el, {
		    		content: 'Order No : '+ info.event.extendedProps.DONO +'<br>'+
  	        		'Customer Name : '+ info.event.extendedProps.CUSTNAME +'<br>'+
  	        		'Order Date : '+ info.event.extendedProps.COLLECTIONDATE +'<br>'+
  	        		'Delivery Date : '+ info.event.extendedProps.DELIVERY_DATE +'<br>'+
  	        		'Total Qty : '+ info.event.extendedProps.TOTAL_QTY +'<br>',
  	        		allowHTML: true,
  	        		placement: 'right',
	    	      });
	    	  }
	    });

	    


	    $(".NoRcvItemSel>.select-items>div").on("click",function(){
	    	getTotalReceivedItems($(this).html().trim());
	    });
	    $(".NoItemIssueSel>.select-items>div").on("click",function(){
	    	getTotalIssuedItems($(this).html().trim());
	    });
	    $(".PoWopriceSel>.select-items>div").on("click",function(){
	    	getPoWoPriceSummary($(this).html().trim());
	    });
	    $(".grnSel>.select-items>div").on("click",function(){
	    	getGrnSummary($(this).html().trim());
	    });

	    $(".rtpSel>.select-items>div").on("click",function(){
	    	getReadyToPackOrders($(this).html().trim());
	    });	 
 
	   
	  //line
	    
	
		
		var ctx6 = document.getElementById('canvas6').getContext('2d');
		window.poWoPriceSumryChart = new Chart(ctx6, {
			type: 'line',
			data: poWoPriceSumryChartConfig,
			options: {
				responsive: true,
				legend: {
			        display: false
			    },
				title: {
					display: false,
					text: ''
				},
				tooltips: {
					mode: 'index',
					intersect: false,
				},
				hover: {
					mode: 'nearest',
					intersect: true
				},
				scales: {
					x: {
						display: true,
						scaleLabel: {
							display: true,
							labelString: 'Month'
						}
					},
					y: {
						display: true,
						scaleLabel: {
							display: true,
							labelString: 'Qty'
						}
					}
				}
			}
		});
		
		
		window.grnSumryChart = new Chart(ctx7, {
			type: 'line',
			data: grnSumryChartConfig,
			options: {
				responsive: true,
				legend: {
			        display: false
			    },
				title: {
					display: false,
					text: ''
				},
				tooltips: {
					mode: 'index',
					intersect: false,
				},
				hover: {
					mode: 'nearest',
					intersect: true
				},
				scales: {
					x: {
						display: true,
						scaleLabel: {
							display: true,
							labelString: 'Month'
						}
					},
					y: {
						display: true,
						scaleLabel: {
							display: true,
							labelString: 'Qty'
						}
					}
				}
			}
		});
		
		
		
});
var ctx7 = document.getElementById('canvas7').getContext('2d');
const lineVerticalPattern3 = ctx7.createPattern(lineVertical(), 'repeat');

var utils = Samples.utils;
utils.srand(110);

var poWoPriceSumryChartConfig = {
		labels: '',
		datasets: [{
			label: '',
			backgroundColor: 'rgba(238, 247, 254,0.4)',
			borderColor: 'rgba(23, 160, 134,1)',
			data: '',
			fill: true,
		}]
};

var grnSumryChartConfig = {
		labels: '',
		datasets: [{
			label: '',
			backgroundColor: lineVerticalPattern3,
			borderColor: 'rgba(61, 162, 239,1)',
			data: '',
			fill: true,
		}]
};

function getTotals(period){
	var totalReceiptPeriod = getPeriod('TotalReceipt', period, 'purchaseRange');
	getTotalReceipt(totalReceiptPeriod);
	var totalNumberReceiptPeriod = getPeriod('TotalNumberReceipt', period, 'purchaseQuantityRange');
	getTotalNumberReceipt(totalNumberReceiptPeriod);
	var purchaseSummaryPeriod = getPeriod('PurchaseSummary', period, 'purchaseSummaryRange');
	getPurchaseSummary(purchaseSummaryPeriod);
	var topSuppliersPeriod = getPeriod('TopSuppliers', period, 'topSuppliersRange');
	getTopSuppliers(topSuppliersPeriod);
}

function getTotalReceipt(period){
	storePeriod('TotalReceipt', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_RECEIPT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.receipts[0].TOTAL_RECEIPT)){
			$('#total_receipt').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#total_receipt').html(parseFloat(data.receipts[0].TOTAL_RECEIPT).toFixed(document.form.numberOfDecimal.value));
		}
		$('#total_receipt').countTo({from: 0, to: $('#total_receipt').html()});
	});
}

function getTotalNumberReceipt(period){
	storePeriod('TotalNumberReceipt', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_RECEIPT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.receipts[0].TOTAL_RECQTY)){
			$('#total_recv_qty').html(parseFloat(0.00000).toFixed(3));					
		}else{
			$('#total_recv_qty').html(parseFloat(data.receipts[0].TOTAL_RECQTY).toFixed(3));
		}
		$('#total_recv_qty').countTo({from: 0, to: $('#total_recv_qty').html()});
	});
}

function getStockReplanishmentProducts(){
	var dataURL = rootURI + '/DashboardServlet?ACTION=LOW_STOCK_PRODUCTS';
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			var body="";
			if(typeof data.products[0].PRODUCT === 'undefined'){
        		body += "<tr><td> No Records Found </td></tr>"
        	}
        	else {
        		for (var prodIndex = 0; prodIndex < data.products.length; prodIndex ++){
        			body += "<tr>";
        			body += "<td>"+ data.products[prodIndex].PRODUCT+"</td>";
        			body += "<td>"+ data.products[prodIndex].ITEMDESC+"</td>";
        			body += "<td>"+ parseFloat(data.products[prodIndex].MIN_QUANTITY).toFixed(3)+"</td>";
        			body += "<td>"+ parseFloat(data.products[prodIndex].CURR_QUANTITY).toFixed(3)+"</td>";
        			body += "<td style='width:35%'>"+
        			"<div class='mini-chart_container'>"+
	        			"<div class='progress'>" +
	        				"<div class='progress-bar' role='progressbar'"+
	        				"style='width:"+data.products[prodIndex].TOTAL_AVG+"%;background: #66bb6a;'>"+
	        				"</div>"+
	        				"<div class='mini-chart-line1' style='left: "+data.products[prodIndex].TOTAL_AVG+"%;'>"+	
								"<div class='mini-chart-data'>"+ parseFloat(data.products[prodIndex].CURR_QUANTITY).toFixed(3)+"</div>"+
							"</div>"+
							"<div class='mini-chart-line2'>"+	
								"<div class='mini-chart-data2'>"+ parseFloat(data.products[prodIndex].MIN_QUANTITY).toFixed(3)+"</div>"+
							"</div>"+
							"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+
	        			"</div>"+
        			"</div>"+"</td>";
        			body += "<td>"+ data.products[prodIndex].INVENTORYUOM+"</td>";        			
        			body += "</tr>";
        		}
        	}
			$(".stkReplshPrd tbody").html(body);
		}
   	});
}

function getNewSuppliers(){
	var dataURL = rootURI + '/DashboardServlet?ACTION=NEW_SUPPLIERS';
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			var body="";
			if(typeof data.suppliers[0].VENDNO === 'undefined'){
        		body += "<tr><td> No Records Found </td></tr>"
        	}
        	else {
        		for (var prodIndex = 0; prodIndex < data.suppliers.length; prodIndex ++){
        			body += "<tr>";
        			body += "<td>"+ data.suppliers[prodIndex].VENDNO+"</td>";
        			body += "<td>"+ data.suppliers[prodIndex].VNAME+"</td>";
        			body += "<td>"+ data.suppliers[prodIndex].NAME+"</td>";
        			body += "<td>"+ data.suppliers[prodIndex].HPNO+"</td>";
        			body += "<td>"+ data.suppliers[prodIndex].EMAIL+"</td>";
        			body += "</tr>";
        		}
        	}
			$("#newSuppliers tbody").html(body);
		}
   	});
}

function getTotalSuppliers(){
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_SUPPLIERS';
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
	}).done(function(data) {
		if (isNaN(data.suppliers[0].TOTAL_SUPPLIERS)){
			$('#total_suppliers').html(parseFloat(0));					
		}else{
			$('#total_suppliers').html(parseFloat(data.suppliers[0].TOTAL_SUPPLIERS));
		}
		$('#total_suppliers').countTo({from: 0, to: $('#total_suppliers').html()});
	});
}

function getTopSuppliers(period){
	storePeriod('TopSuppliers', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOP_SUPPLIERS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			
			if(typeof data.suppliers[0].CUSTNAME === 'undefined'){
        	}
        	else {
        		setTopSuppliersCharData(data);
        	}
		}
   	});
}

function getPurchaseSummary(period){
	storePeriod('PurchaseSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=PURCHASE_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			
			if(typeof data.suppliers[0].PURCHASE_DATE === 'undefined'){
        	}
        	else {
        		setPurchaseSummaryCharData(data);
        	}
		}
   	});
}

function setTopSuppliersCharData(data){
	var top_suppliers = data.suppliers.map(e=>e.CUSTNAME);
    var top_totalcost = data.suppliers.map(e=>e.TOTAL_COST);
    
    topSuppChartConfig.labels = top_suppliers;
    for (var index = 0; index < topSuppChartConfig.datasets.length; ++index) {
    	topSuppChartConfig.datasets[index].data=top_totalcost;
	}
    window.topSuppchart.update();
}

function setPurchaseSummaryCharData(data){
	var purchase_date = data.suppliers.map(e=>e.PURCHASE_DATE);
    var total_cost = data.suppliers.map(e=>e.TOTAL_COST);
    
    purSumryChartConfig.labels = purchase_date;
    for (var index = 0; index < purSumryChartConfig.datasets.length; ++index) {
    	purSumryChartConfig.datasets[index].data=total_cost;
	}
    window.purSumryChart.update();
}

function getTotalIssue(period){
	storePeriod('TotalIssue', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_ISSUE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.issues[0].TOTAL_ISSUE)){
			$('#total_issue').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));
		}else{
	  		$('#total_issue').html(parseFloat(data.issues[0].TOTAL_ISSUE).toFixed(document.form.numberOfDecimal.value));
		}
		$('#total_issue').countTo({from: 0, to: $('#total_issue').html()});
	});
}

function getTotalNumberSales(period){
	storePeriod('TotalNumberSales', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_ISSUE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.issues[0].TOTAL_PICKQTY)){
			$('#total_sales_qty').html(parseFloat(0.00000).toFixed(3));					
		}else{
			$('#total_sales_qty').html(parseFloat(data.issues[0].TOTAL_PICKQTY).toFixed(3));
		}
		$('#total_sales_qty').countTo({from: 0, to: $('#total_sales_qty').html()});
	});
}

function getSalesSummary(period){
	storePeriod('SalesSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=SALES_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			
			if(typeof data.sales[0].SOLD_DATE === 'undefined'){
        	}
        	else {
        		setSalesSummaryCharData(data);
        	}
		}
   	});
}

function setSalesSummaryCharData(data){
	var sold_date = data.sales.map(e=>e.SOLD_DATE);
    var total_price = data.sales.map(e=>e.TOTAL_PRICE);
    
    salesSumryChartConfig.labels = sold_date;
    for (var index = 0; index < salesSumryChartConfig.datasets.length; ++index) {
    	salesSumryChartConfig.datasets[index].data=total_price;
	}
    window.salesSumryChart.update();
}

function getTopCustomers(period){
	storePeriod('TopCustomers', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOP_CUSTOMERS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			
			if(typeof data.customers[0].CUSTNAME === 'undefined'){
        	}
        	else {
        		setTopCustomersCharData(data);
        	}
		}
   	});
}

function setTopCustomersCharData(data){
	var top_customers = data.customers.map(e=>e.CUSTNAME);
    var top_totalprice = data.customers.map(e=>e.TOTAL_PRICE);
    
    topCustChartConfig.labels = top_customers;
    for (var index = 0; index < topCustChartConfig.datasets.length; ++index) {
    	topCustChartConfig.datasets[index].data=top_totalprice;
	}
    window.topCustchart.update();
}

function getNewCustomers(){
	var dataURL = rootURI + '/DashboardServlet?ACTION=NEW_CUSTOMERS';
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			var body="";
			if(typeof data.customers[0].CUSTNO === 'undefined'){
        		body += "<tr><td> No Records Found </td></tr>"
        	}
        	else {
        		for (var prodIndex = 0; prodIndex < data.customers.length; prodIndex ++){
        			body += "<tr>";
        			body += "<td>"+ data.customers[prodIndex].CUSTNO+"</td>";
        			body += "<td>"+ data.customers[prodIndex].CNAME+"</td>";
        			body += "<td>"+ data.customers[prodIndex].NAME+"</td>";
        			body += "<td>"+ data.customers[prodIndex].HPNO+"</td>";
        			body += "<td>"+ data.customers[prodIndex].EMAIL+"</td>";
        			body += "</tr>";
        		}
        	}
			$("#newcustomers tbody").html(body);
		}
   	});
}

function getTotalCustomers(){
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_CUSTOMERS';
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
	}).done(function(data) {
		if (isNaN(data.customers[0].TOTAL_CUSTOMERS)){
			$('#total_customers').html(parseFloat(0));					
		}else{
			$('#total_customers').html(parseFloat(data.customers[0].TOTAL_CUSTOMERS));
		}
		$('#total_customers').countTo({from: 0, to: $('#total_customers').html()});
	});
}

function getTopSalesPrd(period){
	storePeriod('TopSales', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOP_SALES_PRODUCT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			
			if(typeof data.label1[0].SOLD_DATE === 'undefined'){
        	}
        	else {
        		setgetTopSalesPrdCharData(data);
        	}
		}
   	});
}

function setgetTopSalesPrdCharData(data){
	var sold_date = data.label1.map(e=>e.SOLD_DATE);
    var top_totalprice1 = data.label1.map(e=>e.TOTAL_PRICE);
    var top_totalprice2 = data.label2.map(e=>e.TOTAL_PRICE);
    var top_totalprice3 = data.label3.map(e=>e.TOTAL_PRICE);
    var top_totalprice4 = data.label4.map(e=>e.TOTAL_PRICE);
    var top_totalprice5 = data.label5.map(e=>e.TOTAL_PRICE);
    
    var label1 = "",label2 = "",label3 = "",label4 = "",label5 = "";
    if(typeof data.label1[0] != 'undefined'){
    	label1 = data.label1[0].ITEM;
	}
    if(typeof data.label2[0] != 'undefined'){
		label2 = data.label2[0].ITEM;
	}
    if(typeof data.label3[0] != 'undefined'){
		label3 = data.label3[0].ITEM;
	}
    if(typeof data.label4[0] != 'undefined'){
		label4 = data.label4[0].ITEM;
	}
    if(typeof data.label5[0] != 'undefined'){
		label5 = data.label5[0].ITEM;
	}
    
    
    topSalPrdChartConfig.labels = sold_date;
    for (var index = 0; index < topSalPrdChartConfig.datasets.length; ++index) {
    	if(index == 0){
    		topSalPrdChartConfig.datasets[index].label = label1;
    		topSalPrdChartConfig.datasets[index].data=top_totalprice1;
    	}else if(index == 1){
    		topSalPrdChartConfig.datasets[index].label = label2;
    		topSalPrdChartConfig.datasets[index].data=top_totalprice2;
    	} else if(index == 2){
    		topSalPrdChartConfig.datasets[index].label = label3;
    		topSalPrdChartConfig.datasets[index].data=top_totalprice3;
    	}else if(index == 3){
    		topSalPrdChartConfig.datasets[index].label = label4;
    		topSalPrdChartConfig.datasets[index].data=top_totalprice4;
    	}else if(index == 4){
    		topSalPrdChartConfig.datasets[index].label = label5;
    		topSalPrdChartConfig.datasets[index].data=top_totalprice5;
    	}
	}
    window.topSalPrdchart.update();
}

function getTotalReceivedItems(period){
	storePeriod('TotalReceivedItemsSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_RECEIPT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.receipts[0].TOTAL_RECQTY)){
			$('#total_items_recv_qty').html(parseFloat(0.00000).toFixed(3));					
		}else{
			$('#total_items_recv_qty').html(parseFloat(data.receipts[0].TOTAL_RECQTY).toFixed(3));
		}
		$('#total_items_recv_qty').countTo({from: 0, to: $('#total_items_recv_qty').html()});
	});
}

function getTotalIssuedItems(period){
	storePeriod('TotalIssuedItemsSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_ISSUE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.issues[0].TOTAL_PICKQTY)){
			$('#total_items_issue_qty').html(parseFloat(0.00000).toFixed(3));					
		}else{
			$('#total_items_issue_qty').html(parseFloat(data.issues[0].TOTAL_PICKQTY).toFixed(3));
		}
		$('#total_items_issue_qty').countTo({from: 0, to: $('#total_items_issue_qty').html()});
	});
}

function getPoWoPriceSummary(period){
	storePeriod('POWithOutPriceSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=PO_WO_PRICE_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			
			if(typeof data.suppliers[0].PURCHASE_DATE === 'undefined'){
        	}
        	else {
        		setPoWoPriceSummaryCharData(data);
        	}
		}
   	});
}

function setPoWoPriceSummaryCharData(data){
	var purchase_date = data.suppliers.map(e=>e.PURCHASE_DATE);
    var total_Recv_qty = data.suppliers.map(e=>e.TOTAL_RECV_QTY);
    
    poWoPriceSumryChartConfig.labels = purchase_date;
    for (var index = 0; index < poWoPriceSumryChartConfig.datasets.length; ++index) {
    	poWoPriceSumryChartConfig.datasets[index].data=total_Recv_qty;
	}
    window.poWoPriceSumryChart.update();
}

function getGrnSummary(period){
	storePeriod('GrnSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GRN_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			
			if(typeof data.suppliers[0].RECEIVED_DATE === 'undefined'){
        	}
        	else {
        		setGrnSummaryCharData(data);
        	}
		}
   	});
}

function setGrnSummaryCharData(data){
	var received_date = data.suppliers.map(e=>e.RECEIVED_DATE);
    var total_Recv_qty = data.suppliers.map(e=>e.TOTAL_RECV_QTY);
    
    grnSumryChartConfig.labels = received_date;
    for (var index = 0; index < grnSumryChartConfig.datasets.length; ++index) {
    	grnSumryChartConfig.datasets[index].data=total_Recv_qty;
	}
    window.grnSumryChart.update();
}

function getReadyToPackOrders(period){
	storePeriod('ProductsReadyToPackFromSalesOrderSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_READY_TO_PACK_ORDERS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableReadyToPackOrders){
		tableReadyToPackOrders.ajax.url( dataURL ).load();
	}else{
		tableReadyToPackOrders = $('#tableReadtToPackProducts').DataTable({
			"processing": true,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-5"i><"col-xs-7"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.orders[0].DONO === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.orders;
		        	}
		        }
		    },
			"columns": [{'data': 'DONO', "orderable": false},{'data': 'CUSTNAME', "orderable": false},{'data': 'DOLNNO', "orderable": false},{'data': 'COLLECTIONDATE', "orderable": false},{'data': 'UNITMO', "orderable": false},{'data': 'QTYOR', "orderable": false}]
		});
	}
}

function getExpiringProducts(period){
	storePeriod('ExpiringProductsSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=EXPIRING_PRODUCTS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period + ' (F)'))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period + ' (F)')));
	if (tableExpProducts){
		tableExpProducts.ajax.url( dataURL ).load();
	}else{
		tableExpProducts = $('#tableExpProducts').DataTable({
			"processing": true,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-5"i><"col-xs-7"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(typeof data.products[0].PRODUCT === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.products;
		        	}
		        }
		    },
			"columns": [{'data': 'PRODUCT', "orderable": false},{'data': 'LOCATION', "orderable": false},{'data': 'BATCH', "orderable": false},{'data': 'EXPIRY_DATE', "orderable": false},{'data': 'QUANTITY', "orderable": false}]
		});
	}
}

/*function getTotalPurchaseByBill(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_PURCHASE_BY_BILL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.bills[0].TOTAL_PURCHASE)){
			$('#total_purchase_by_bill').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#total_purchase_by_bill').html(parseFloat(data.bills[0].TOTAL_PURCHASE).toFixed(document.form.numberOfDecimal.value));
		}
		$('#total_purchase_by_bill').countTo({from: 0, to: $('#total_purchase_by_bill').html()});
	});
}*/

function getTotalPurchaseByBill(period){
	storePeriod('TotalPurchaseByBillSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_PURCHASE_BY_BILL_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.bills[0].TOTAL_PURCHASE)){
			$('#total_purchase_by_bill').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#total_purchase_by_bill').html(parseFloat(data.bills[0].TOTAL_PURCHASE).toFixed(document.form.numberOfDecimal.value));
		}
		$('#total_purchase_by_bill').countTo({from: 0, to: $('#total_purchase_by_bill').html()});
	});
}

/*function getTotalSalesByInvoice(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_SALES_BY_INVOICE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.invoices[0].TOTAL_SALES)){
			$('#total_sales_by_invoice').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#total_sales_by_invoice').html(parseFloat(data.invoices[0].TOTAL_SALES).toFixed(document.form.numberOfDecimal.value));
		}
		$('#total_sales_by_invoice').countTo({from: 0, to: $('#total_sales_by_invoice').html()});
	});
}*/
function getTotalSalesByInvoice(period){
	storePeriod('TotalSalesByInvoiceSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_SALES_BY_INVOICE_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.invoices[0].TOTAL_SALES)){
			$('#total_sales_by_invoice').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#total_sales_by_invoice').html(parseFloat(data.invoices[0].TOTAL_SALES).toFixed(document.form.numberOfDecimal.value));
		}
		$('#total_sales_by_invoice').countTo({from: 0, to: $('#total_sales_by_invoice').html()});
	});
}

function getTotalIncome(period){
	storePeriod('TotalIncomeSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_INCOME_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.invoices[0].ACCOUNTDETAILTYPE === 'undefined'){
				totalIncomeChartConfig.labels = [];
				for (var index = 0; index < totalIncomeChartConfig.datasets.length; ++index) {
			    	totalIncomeChartConfig.datasets[index].data = [];
				}
				window.totalIncomeChart.update();
				$(".totalIncomeMsg").show();
				$("#totalIncome").text("");
        	}
        	else {
        		setTotalIncomeCharData(data);
        		$(".totalIncomeMsg").hide();
        	}
		}
   	});
}

function setTotalIncomeCharData(data){
	var accountdetailtype = data.invoices.map(e=>e.ACCOUNTDETAILTYPE);
    var total_amount = data.invoices.map(e=>e.TOTAL_AMOUNT);
    var backgroundColor = [];
    totalIncomeChartConfig.labels = accountdetailtype;
    
    for(var index = 0; index < data.invoices.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalIncomeChartConfig.datasets.length; ++index) {
    	totalIncomeChartConfig.datasets[index].data = total_amount;
    	totalIncomeChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalIncome").text(data.TOTAL_AMOUNT);
    window.totalIncomeChart.update();
}

function getTotalExpense(period){
	storePeriod('TotalExpenseSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_EXPENSE_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.bills[0].ACCOUNTDETAILTYPE === 'undefined'){
				totalExpenseChartConfig.labels = [];
				for (var index = 0; index < totalExpenseChartConfig.datasets.length; ++index) {
			    	totalExpenseChartConfig.datasets[index].data = [];
				}
				window.totalExpenseChart.update();
				$(".totalExpenseMsg").show();
				$("#totalExpense").text("");
        	}
        	else {
        		setTotalExpenseCharData(data);
        		$(".totalExpenseMsg").hide();
        	}
		}
   	});
}

function setTotalExpenseCharData(data){
	var accountdetailtype = data.bills.map(e=>e.ACCOUNTDETAILTYPE);
    var total_amount = data.bills.map(e=>e.TOTAL_AMOUNT);
    var backgroundColor = [];
    totalExpenseChartConfig.labels = accountdetailtype;
    
    for(var index = 0; index < data.bills.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalExpenseChartConfig.datasets.length; ++index) {
    	totalExpenseChartConfig.datasets[index].data = total_amount;
    	totalExpenseChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalExpense").text(data.TOTAL_AMOUNT);
    window.totalExpenseChart.update();
}

/*function getTotalIncomeSummaryByInvoice(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_INCOME_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.invoices[0].INVOICE_DATE === 'undefined'){
        	}
        	else {
        		setTotalIncomeSummaryByInvoiceCharData(data);
        	}
		}
   	});
}*/

function getTotalIncomeSummaryByInvoice(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_INCOME_SUMMARY_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.invoices[0].INVOICE_DATE === 'undefined'){
        	}
        	else {
        		setTotalIncomeSummaryByInvoiceCharData(data);
        	}
		}
   	});
}

function setTotalIncomeSummaryByInvoiceCharData(data){
	var invoice_date = data.invoices.map(e=>e.INVOICE_DATE);
    var total_price = data.invoices.map(e=>e.TOTAL_PRICE);
    
    totalIncomeByInvoiceChartConfig.labels = invoice_date;
    for (var index = 0; index < totalIncomeByInvoiceChartConfig.datasets.length; ++index) {
    	totalIncomeByInvoiceChartConfig.datasets[index].data=total_price;
	}
    window.totalIncomeByInvoiceChart.update();
}

/*function getTotalExpenseSummaryByBill(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_EXPENSE_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.bills[0].BILL_DATE === 'undefined'){
        	}
        	else {
        		setTotalExpenseSummaryByBillCharData(data);
        	}
		}
   	});
}*/

function getTotalExpenseSummaryByBill(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_EXPENSE_SUMMARY_FROM_JOURNY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.bills[0].BILL_DATE === 'undefined'){
        	}
        	else {
        		setTotalExpenseSummaryByBillCharData(data);
        	}
		}
   	});
}

function setTotalExpenseSummaryByBillCharData(data){
	var bill_date = data.bills.map(e=>e.BILL_DATE);
    var total_cost = data.bills.map(e=>e.TOTAL_COST);
    
    totalExpenseByBillChartConfig.labels = bill_date;
    for (var index = 0; index < totalExpenseByBillChartConfig.datasets.length; ++index) {
    	totalExpenseByBillChartConfig.datasets[index].data=total_cost;
	}
    window.totalExpenseByBillChart.update();
}

function getPaymentPdc(period){
	storePeriod('PaymentPdcSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_PAYMENT_PDC&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tablePaymentPdc){
		tablePaymentPdc.ajax.url( dataURL ).load();
	}else{
		tablePaymentPdc = $('#tablePaymentPdc').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmPaymentPdc()) ? d : getParmPaymentPdc();
				},
		        "dataSrc": function(data){
		        	if(typeof data.payments[0].PAYMENT_DATE === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.payments;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'SNO', "orderable": false},{'data': 'PAYMENT_DATE', "orderable": false},{'data': 'SUPPLIER', "orderable": false},{'data': 'BANK_BRANCH', "orderable": false},{'data': 'CHECQUE_NO', "orderable": false},{'data': 'CHEQUE_DATE', "orderable": false},{'data': 'CHEQUE_REVERSAL_DATE', "orderable": false},{'data': 'CHEQUE_AMOUNT', "orderable": false}]
		});
	}
}

function getPaymentRecvPdc(period){
	storePeriod('PaymentRecvPdcSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_PAYMENT_RECV_PDC&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tablePaymentRecvPdc){
		tablePaymentRecvPdc.ajax.url( dataURL ).load();
	}else{
		tablePaymentRecvPdc = $('#tablePaymentRecvPdc').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmPaymentRecvPdc()) ? d : getParmPaymentRecvPdc();
				},
		        "dataSrc": function(data){
		        	if(typeof data.payments[0].PAYMENT_DATE === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.payments;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'SNO', "orderable": false},{'data': 'PAYMENT_DATE', "orderable": false},{'data': 'CUSTOMER', "orderable": false},{'data': 'BANK_BRANCH', "orderable": false},{'data': 'CHECQUE_NO', "orderable": false},{'data': 'CHEQUE_DATE', "orderable": false},{'data': 'CHEQUE_REVERSAL_DATE', "orderable": false},{'data': 'CHEQUE_AMOUNT', "orderable": false}]
		});
	}
}


function getAccountpayable(period){
	storePeriod('AccountpayableSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_ACCOUNT_PAYABLE_TO_SUPPLIER&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY")+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.reportContent[0].name === 'undefined'){
				totalAccPayChartConfig.labels = [];
				for (var index = 0; index < totalAccPayChartConfig.datasets.length; ++index) {
					totalAccPayChartConfig.datasets[index].data = [];
				}
				window.totalAccPayChart.update();
				$(".totalAccPayMsg").show();
				$("#totalAccPay").text("");
        	}
        	else {
        		setAccountpayableCharData(data);
        		$(".totalAccPayMsg").hide();
        	}
		}
   	});
}

function setAccountpayableCharData(data){
	var type = data.reportContent.map(e=>e.name);
    var total_amount = data.reportContent.map(e=>e.total_due);
    var backgroundColor = [];
    totalAccPayChartConfig.labels = type;
    
    for(var index = 0; index < data.reportContent.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalAccPayChartConfig.datasets.length; ++index) {
    	totalAccPayChartConfig.datasets[index].data = total_amount;
    	totalAccPayChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalAccPay").text(data.totalOutstanding);
    window.totalAccPayChart.update();
}

function getAccountReceivable(period){
	storePeriod('AccountReceivableSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_ACCOUNT_RECEIVABLE_FROM_CUSTOMER&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY")+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.reportContent[0] === 'undefined'){
				totalAccRecvChartConfig.labels = [];
				for (var index = 0; index < totalAccRecvChartConfig.datasets.length; ++index) {
					totalAccRecvChartConfig.datasets[index].data = [];
				}
				window.totalAccRecvChart.update();
				$(".totalAccRecvMsg").show();
				$("#totalAccRecv").text("");
        	}
        	else {
        		setAccountReceivableCharData(data);
        		$(".totalAccRecvMsg").hide();
        	}
		}
   	});
}

function setAccountReceivableCharData(data){
	var type = data.reportContent.map(e=>e.name);
    var total_amount = data.reportContent.map(e=>e.total_due);
    var backgroundColor = [];
    totalAccRecvChartConfig.labels = type;
    
    for(var index = 0; index < data.reportContent.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalAccRecvChartConfig.datasets.length; ++index) {
    	totalAccRecvChartConfig.datasets[index].data = total_amount;
    	totalAccRecvChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalAccRecv").text(data.totalOutstanding);
    window.totalAccRecvChart.update();
}

function getSupplierAgeing(period, loadCust){
	var dataURL = rootURI + "/DashboardServlet?ACTION=GET_SUPPLIER_AGEING_SUMMARY&Order=BILL&FROM_DATE=" + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + "&TO_DATE=" + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY") + "&LOADCUST="+loadCust;
	if (tableSupAge){
		tableSupAge.ajax.url( dataURL ).load();
	}else{
		tableSupAge = $('#tableSupAge').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				/*"async": false,*/
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(data.LOADCUST == 1){
		        		getCustomerAgeing(period);
		        	}
		        	if((data.reportContent[0] === '') || (typeof data.reportContent[0] === 'undefined')){
		        		return [];
		        	}
		        	else {
		        		loadSupplierAgeingTotal(data.reportContent);
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'name', "orderable": false},{'data': 'amountdue', "orderable": false},{'data': 'notDue', "orderable": false},{'data': 'v30daysdue', "orderable": false},{'data': 'v60daysdue', "orderable": false},{'data': 'v90daysdue', "orderable": false},{'data': 'v90plusdaysdue', "orderable": false}],
		    order: [[1, 'desc']]
		});
	}
}

function getCustomerAgeing(period){
	var dataURL = rootURI + "/DashboardServlet?ACTION=GET_CUSTOMER_AGEING_SUMMARY&Order=INVOICE&FROM_DATE=" + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + "&TO_DATE=" + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY");
	if (tablecustAge){
		tablecustAge.ajax.url( dataURL ).load();
	}else{
		tablecustAge = $('#tablecustAge').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				/*"async": false,*/
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if((data.reportContent[0] === '') || (typeof data.reportContent[0] === 'undefined')){
		        		return [];
		        	}
		        	else {
		        		loadCustomerAgeingTotal(data.reportContent);
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'name', "orderable": false},{'data': 'amountdue', "orderable": false},{'data': 'notDue', "orderable": false},{'data': 'v30daysdue', "orderable": false},{'data': 'v60daysdue', "orderable": false},{'data': 'v90daysdue', "orderable": false},{'data': 'v90plusdaysdue', "orderable": false}],
		    order: [[1, 'desc']]
		});
	}
}

function redirectToPurchaseSummary(){
	window.location.href=rootURI + '/jsp/printSupplierPOInvoice.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".PurSumrySel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".PurSumrySel>.select-selected").html())), 'DD/MM/YYYY');
}
function redirectToSalesSummary(){
	window.location.href=rootURI + '/jsp/OrderSummaryOutBoundWithPrice.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".saleSumrySel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".saleSumrySel>.select-selected").html())), 'DD/MM/YYYY');
}1
function redirectToPurchaseWOPSummary(){
	window.location.href=rootURI + '/jsp/OrderSummaryInbound.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".PoWopriceSel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".PoWopriceSel>.select-selected").html())), 'DD/MM/YYYY');
}
function redirectToGrnSummary(){
	window.location.href=rootURI + '/jsp/grntobillSummary.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".grnSel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".grnSel>.select-selected").html())), 'DD/MM/YYYY');
}
function redirectToGiSummary(){
	window.location.href=rootURI + '/jsp/ginotoinvoiceSummary.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".griSel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".griSel>.select-selected").html())), 'DD/MM/YYYY');
}
function redirectToPaymentPdcSummary(){
	window.open(
	  rootURI + '/jsp/pdcpaymentprocess.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".payPdcSel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".payPdcSel>.select-selected").html())), 'DD/MM/YYYY'),
	  '_blank'
	);
}
function redirectToPaymentReceivePdcSummary(){
	window.open(
	  rootURI + '/jsp/pdcpaymentReceiveprocess.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".payRecvPdcSel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".payRecvPdcSel>.select-selected").html())), 'DD/MM/YYYY'),
	  '_blank'
	);
}
function colorize(opaque, hover, ctx) {
	var v = ctx.dataset.data[ctx.dataIndex];
	var c = v < -50 ? '#D60000'
		: v < 0 ? '#F46300'
		: v < 50 ? '#0358B6'
		: '#44DE28';

	var opacity = hover ? 1 - Math.abs(v / 150) - 0.2 : 1 - Math.abs(v / 150);

	return opaque ? c : utils.transparentize(c, opacity);
}
function hoverColorize(ctx) {
	return colorize(false, true, ctx);
}
function lineVertical () {
    let lineCanvas = document.createElement('canvas');
    let lineContext = lineCanvas.getContext('2d');

    lineCanvas.width = 5;
    lineCanvas.height = 10;

    lineContext.strokeStyle = 'rgba(194, 215, 243, 1)';
    lineContext.lineWidth = 6;

    lineContext.moveTo(5, 0);
    lineContext.lineTo(5, 10);
    lineContext.moveTo(10, 0);
    lineContext.lineTo(10, 10);
    lineContext.stroke();

    return lineCanvas;
}
function lineVertical2 () {
    let lineCanvas = document.createElement('canvas');
    let lineContext = lineCanvas.getContext('2d');

    lineCanvas.width = 5;
    lineCanvas.height = 10;

    lineContext.strokeStyle = 'rgba(239, 192, 153, 1)';
    lineContext.lineWidth = 6;

    lineContext.moveTo(5, 0);
    lineContext.lineTo(5, 10);
    lineContext.moveTo(10, 0);
    lineContext.lineTo(10, 10);
    lineContext.stroke();

    return lineCanvas;
}

function getTotalPurchaseByBillForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_PURCHASE_BY_BILL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.bills[0].TOTAL_PURCHASE)){
			$('#total_purchase_by_bill_for_mgt').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#total_purchase_by_bill_for_mgt').html(parseFloat(data.bills[0].TOTAL_PURCHASE).toFixed(document.form.numberOfDecimal.value));
		}
		$('#total_purchase_by_bill_for_mgt').countTo({from: 0, to: $('#total_purchase_by_bill_for_mgt').html()});
	});
}

function getTotalSalesByInvoiceForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_SALES_BY_INVOICE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.invoices[0].TOTAL_SALES)){
			$('#total_sales_by_invoice_for_mgt').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#total_sales_by_invoice_for_mgt').html(parseFloat(data.invoices[0].TOTAL_SALES).toFixed(document.form.numberOfDecimal.value));
		}
		$('#total_sales_by_invoice_for_mgt').countTo({from: 0, to: $('#total_sales_by_invoice_for_mgt').html()});
	});
}


function getTotalPurchaseSummaryByBillForMgt(period){
	storePeriod('TotalPurchaseSummaryByBillForMgt', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_PURCHASE_SUMMARY_BY_BILL_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			
			if(typeof data.bills[0].BILL_DATE === 'undefined'){
				$("#totalPurchaseByBillSumryForMgt").text("");
        	}
        	else {
        		setTotalPurchaseSummaryByBillForMgtCharData(data);
        	}
		}
   	});
}

function setTotalPurchaseSummaryByBillForMgtCharData(data){
	var bill_date = data.bills.map(e=>e.BILL_DATE);
    var total_cost = data.bills.map(e=>e.TOTAL_COST);
    
    purSumryByBillForMgtChartConfig.labels = bill_date;
    for (var index = 0; index < purSumryByBillForMgtChartConfig.datasets.length; ++index) {
    	purSumryByBillForMgtChartConfig.datasets[index].data=total_cost;
	}
    $("#totalPurchaseByBillSumryForMgt").text(data.TOTAL_AMOUNT);
    window.purSumryByBillForMgtChart.update();
}

/*function getTotalSalesSummaryByInvoiceForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_SALES_SUMMARY_BY_INVOICE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.invoices[0].INVOICE_DATE === 'undefined'){
				$("#totalSalesByInvoiceSumryForMgt").text("");
        	}
        	else {
        		setTotalSalesSummaryByInvoiceForMgtCharData(data);
        	}
		}
   	});
}*/

function getTotalSalesSummaryByInvoiceForMgt(period){
	storePeriod('TotalSalesSummaryByInvoiceForMgt', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=TOTAL_SALES_SUMMARY_BY_INVOICE_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.invoices[0].INVOICE_DATE === 'undefined'){
				$("#totalSalesByInvoiceSumryForMgt").text("");
        	}
        	else {
        		setTotalSalesSummaryByInvoiceForMgtCharData(data);
        	}
		}
   	});
}

function setTotalSalesSummaryByInvoiceForMgtCharData(data){
	var invoice_date = data.invoices.map(e=>e.INVOICE_DATE);
    var total_price = data.invoices.map(e=>e.TOTAL_PRICE);
    
    salesSumryByInvoiceForMgtChartConfig.labels = invoice_date;
    for (var index = 0; index < salesSumryByInvoiceForMgtChartConfig.datasets.length; ++index) {
    	salesSumryByInvoiceForMgtChartConfig.datasets[index].data=total_price;
	}
    $("#totalSalesByInvoiceSumryForMgt").text(data.TOTAL_AMOUNT);
    window.salesSumryByInvoiceForMgtChart.update();
}

/*function getTotalIncomeForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_INCOME&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.invoices[0].ACCOUNTDETAILTYPE === 'undefined'){
				totalIncomeForMgtChartConfig.labels = [];
				for (var index = 0; index < totalIncomeForMgtChartConfig.datasets.length; ++index) {
					totalIncomeForMgtChartConfig.datasets[index].data = [];
				}
				window.totalIncomeForMgtChart.update();
				$(".totalIncomeMsg").show();
				$("#totalIncomeForMgt").text("");
        	}
        	else {
        		setTotalIncomeForMgtCharData(data);
        		$(".totalIncomeMsg").hide();
        	}
		}
   	});
}*/

function getTotalIncomeForMgt(period){
	storePeriod('TotalIncomeSummaryMgt', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_INCOME_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.invoices[0].ACCOUNTDETAILTYPE === 'undefined'){
				totalIncomeForMgtChartConfig.labels = [];
				for (var index = 0; index < totalIncomeForMgtChartConfig.datasets.length; ++index) {
					totalIncomeForMgtChartConfig.datasets[index].data = [];
				}
				window.totalIncomeForMgtChart.update();
				$(".totalIncomeMsg").show();
				$("#totalIncomeForMgt").text("");
        	}
        	else {
        		setTotalIncomeForMgtCharData(data);
        		$(".totalIncomeMsg").hide();
        	}
		}
   	});
}

function setTotalIncomeForMgtCharData(data){
	var accountdetailtype = data.invoices.map(e=>e.ACCOUNTDETAILTYPE);
    var total_amount = data.invoices.map(e=>e.TOTAL_AMOUNT);
    var backgroundColor = [];
    totalIncomeForMgtChartConfig.labels = accountdetailtype;
    
    for(var index = 0; index < data.invoices.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalIncomeForMgtChartConfig.datasets.length; ++index) {
    	totalIncomeForMgtChartConfig.datasets[index].data = total_amount;
    	totalIncomeForMgtChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalIncomeForMgt").text(data.TOTAL_AMOUNT);
    window.totalIncomeForMgtChart.update();
}

/*function getTotalIncomeSummaryByInvoiceForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_INCOME_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.invoices[0].INVOICE_DATE === 'undefined'){
        	}
        	else {
        		setTotalIncomeSummaryByInvoiceForMgtCharData(data);
        	}
		}
   	});
}*/

function getTotalIncomeSummaryByInvoiceForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_INCOME_SUMMARY_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.invoices[0].INVOICE_DATE === 'undefined'){
        	}
        	else {
        		setTotalIncomeSummaryByInvoiceForMgtCharData(data);
        	}
		}
   	});
}

function setTotalIncomeSummaryByInvoiceForMgtCharData(data){
	var invoice_date = data.invoices.map(e=>e.INVOICE_DATE);
    var total_price = data.invoices.map(e=>e.TOTAL_PRICE);
    
    totalIncomeByInvoiceForMgtChartConfig.labels = invoice_date;
    for (var index = 0; index < totalIncomeByInvoiceForMgtChartConfig.datasets.length; ++index) {
    	totalIncomeByInvoiceForMgtChartConfig.datasets[index].data=total_price;
	}
    window.totalIncomeByInvoiceForMgtChart.update();
}

function getRandomColor() {
    var letters = 'BCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.floor(Math.random() * letters.length)];
    }
    return color;
}


function getTotalPaymentMadeSummaryForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_PAYMENT_MADE_SUMMARY_FROM_JOURNY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.payment[0].BILL_DATE === 'undefined'){
        	}
        	else {
        		setTotalPaymentMadeSummaryForMgtCharData(data);
        	}
		}
   	});
}

function setTotalPaymentMadeSummaryForMgtCharData(data){
	var bill_date = data.payment.map(e=>e.BILL_DATE);
    var total_cost = data.payment.map(e=>e.TOTAL_COST);
    
    totalPaymentMadeForMgtChartbarConfig.labels = bill_date;
    for (var index = 0; index < totalPaymentMadeForMgtChartbarConfig.datasets.length; ++index) {
    	totalPaymentMadeForMgtChartbarConfig.datasets[index].data=total_cost;
	}
    window.totalPaymentMadeForMgtChartbar.update();
}
/*----------------payment made-------------------*/

/*----------------payment made Receipt-------------------*/
function getTotalPaymentReceiptForMgt(period){
	storePeriod('TotalPaymentReceiptForMgt', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_PAYMENT_RECEIPT_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			//if(typeof data.payment[0].ACCOUNTDETAILTYPE === 'undefined'){
			if(data.payment.length == 0){
				totalPaymentReceiptForMgtChartConfig.labels = [];
				for (var index = 0; index < totalPaymentReceiptForMgtChartConfig.datasets.length; ++index) {
					totalPaymentReceiptForMgtChartConfig.datasets[index].data = [];
				}
				window.totalPaymentReceiptForMgtChart.update();
				$(".totalPaymentReceiptMsg").show();
				$("#totalPaymentReceiptMgt").text("");
        	}
        	else {
        		setTotalPaymentReceiptForMgtCharData(data);
        		$(".totalPaymentReceiptMsg").hide();
        	}
		}
   	});
}

function setTotalPaymentReceiptForMgtCharData(data){
	var accountdetailtype = data.payment.map(e=>e.ACCOUNTDETAILTYPE);
    var total_amount = data.payment.map(e=>e.TOTAL_AMOUNT);
    var backgroundColor = [];
    totalPaymentReceiptForMgtChartConfig.labels = accountdetailtype;
    
    for(var index = 0; index < data.payment.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalPaymentReceiptForMgtChartConfig.datasets.length; ++index) {
    	totalPaymentReceiptForMgtChartConfig.datasets[index].data = total_amount;
    	totalPaymentReceiptForMgtChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalPaymentReceiptMgt").text(data.TOTAL_AMOUNT);
    window.totalPaymentReceiptForMgtChart.update();
}


function getTotalPaymentReceiptSummaryForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_PAYMENT_RECEIPT_SUMMARY_FROM_JOURNY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.payment[0].BILL_DATE === 'undefined'){
        	}
        	else {
        		setTotalPaymentReceiptSummaryForMgtCharData(data);
        	}
		}
   	});
}

function setTotalPaymentReceiptSummaryForMgtCharData(data){
	var bill_date = data.payment.map(e=>e.BILL_DATE);
    var total_cost = data.payment.map(e=>e.TOTAL_COST);
    
    totalPaymentReceiptForMgtChartbarConfig.labels = bill_date;
    for (var index = 0; index < totalPaymentReceiptForMgtChartbarConfig.datasets.length; ++index) {
    	totalPaymentReceiptForMgtChartbarConfig.datasets[index].data=total_cost;
	}
    window.totalPaymentReceiptForMgtChartbar.update();
}
/*----------------payment made Receipt-------------------*/

/*----------------payment made account-------------------*/
function getTotalPaymentMade(period){
	storePeriod('TotalPaymentMadeSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_PAYMENT_MADE_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.payment[0].ACCOUNTDETAILTYPE === 'undefined'){
				totalPaymentMadeChartConfig.labels = [];
				for (var index = 0; index < totalPaymentMadeChartConfig.datasets.length; ++index) {
					totalPaymentMadeChartConfig.datasets[index].data = [];
				}
				window.totalPaymentMadeChart.update();
				$(".totalPaymentMadeMsgAcc").show();
				$("#totalPaymentMade").text("");
        	}
        	else {
        		setTotalPaymentMadeCharData(data);
        		$(".totalPaymentMadeMsgAcc").hide();
        	}
		}
   	});
}

function setTotalPaymentMadeCharData(data){
	var accountdetailtype = data.payment.map(e=>e.ACCOUNTDETAILTYPE);
    var total_amount = data.payment.map(e=>e.TOTAL_AMOUNT);
    var backgroundColor = [];
    totalPaymentMadeChartConfig.labels = accountdetailtype;
    
    for(var index = 0; index < data.payment.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalPaymentMadeChartConfig.datasets.length; ++index) {
    	totalPaymentMadeChartConfig.datasets[index].data = total_amount;
    	totalPaymentMadeChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalPaymentMade").text(data.TOTAL_AMOUNT);
    window.totalPaymentMadeChart.update();
}

function getTotalPaymentMadeSummary(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_PAYMENT_MADE_SUMMARY_FROM_JOURNY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.payment[0].BILL_DATE === 'undefined'){
        	}
        	else {
        		setTotalPaymentMadeSummaryCharData(data);
        	}
		}
   	});
}

function setTotalPaymentMadeSummaryCharData(data){
	var bill_date = data.payment.map(e=>e.BILL_DATE);
    var total_cost = data.payment.map(e=>e.TOTAL_COST);
    
    totalPaymentMadeChartbarConfig.labels = bill_date;
    for (var index = 0; index < totalPaymentMadeChartbarConfig.datasets.length; ++index) {
    	totalPaymentMadeChartbarConfig.datasets[index].data=total_cost;
	}
    window.totalPaymentMadeChartbar.update();
}

/*----------------payment made account-------------------*/


/*----------------payment Receipt account-------------------*/
function getTotalPaymentReceipt(period){
	storePeriod('TotalPaymentReceiptSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_PAYMENT_RECEIPT_FROM_JOURNAL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			//if(typeof data.payment[0].ACCOUNTDETAILTYPE === 'undefined'){
			if(data.payment.length == 0){
				totalPaymentReceiptChartConfig.labels = [];
				for (var index = 0; index < totalPaymentReceiptChartConfig.datasets.length; ++index) {
					totalPaymentReceiptChartConfig.datasets[index].data = [];
				}
				window.totalPaymentReceiptChart.update();
				$(".totalPaymentReceiptMsgAcc").show();
				$("#totalPaymentReceipt").text("");
        	}
        	else {
        		setTotalPaymentReceiptCharData(data);
        		$(".totalPaymentReceiptMsgAcc").hide();
        	}
		}
   	});
}

function setTotalPaymentReceiptCharData(data){
	var accountdetailtype = data.payment.map(e=>e.ACCOUNTDETAILTYPE);
    var total_amount = data.payment.map(e=>e.TOTAL_AMOUNT);
    var backgroundColor = [];
    totalPaymentReceiptChartConfig.labels = accountdetailtype;
    
    for(var index = 0; index < data.payment.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalPaymentReceiptChartConfig.datasets.length; ++index) {
    	totalPaymentReceiptChartConfig.datasets[index].data = total_amount;
    	totalPaymentReceiptChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalPaymentReceipt").text(data.TOTAL_AMOUNT);
    window.totalPaymentReceiptChart.update();
}

function getTotalPaymentReceiptSummary(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_PAYMENT_RECEIPT_SUMMARY_FROM_JOURNY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.payment[0].BILL_DATE === 'undefined'){
        	}
        	else {
        		setTotalPaymentReceiptSummaryCharData(data);
        	}
		}
   	});
}

function setTotalPaymentReceiptSummaryCharData(data){
	var bill_date = data.payment.map(e=>e.BILL_DATE);
    var total_cost = data.payment.map(e=>e.TOTAL_COST);
    
    totalPaymentReceiptChartbarConfig.labels = bill_date;
    for (var index = 0; index < totalPaymentReceiptChartbarConfig.datasets.length; ++index) {
    	totalPaymentReceiptChartbarConfig.datasets[index].data=total_cost;
	}
    window.totalPaymentReceiptChartbar.update();
}

/*----------------payment Receipt account-------------------*/

/*function getTotalExpenseSummaryByBillForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_EXPENSE_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.bills[0].BILL_DATE === 'undefined'){
        	}
        	else {
        		setTotalExpenseSummaryByBillForMgtCharData(data);
        	}
		}
   	});
}*/

function getTotalExpenseSummaryByBillForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_EXPENSE_SUMMARY_FROM_JOURNY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.bills[0].BILL_DATE === 'undefined'){
        	}
        	else {
        		setTotalExpenseSummaryByBillForMgtCharData(data);
        	}
		}
   	});
}

function setTotalExpenseSummaryByBillForMgtCharData(data){
	var bill_date = data.bills.map(e=>e.BILL_DATE);
    var total_cost = data.bills.map(e=>e.TOTAL_COST);
    
    totalExpenseByBillForMgtChartConfig.labels = bill_date;
    for (var index = 0; index < totalExpenseByBillForMgtChartConfig.datasets.length; ++index) {
    	totalExpenseByBillForMgtChartConfig.datasets[index].data=total_cost;
	}
    window.totalExpenseByForMgtBillChart.update();
}

function getAccountpayableForMgt(period){
	storePeriod('AccountpayableForMgtSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_ACCOUNT_PAYABLE_TO_SUPPLIER&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY");
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.reportContent[0] === 'undefined'){
				totalAccPayForMgtChartConfig.labels = [];
				for (var index = 0; index < totalAccPayForMgtChartConfig.datasets.length; ++index) {
					totalAccPayForMgtChartConfig.datasets[index].data = [];
				}
				window.totalAccPayForMgtChart.update();
				$(".totalAccPayMsg").show();
				$("#totalAccPayForMgt").text("");
        	}
        	else {
        		setAccountpayableForMgtCharData(data);
        		$(".totalAccPayMsg").hide();
        	}
		}
   	});
}

function setAccountpayableForMgtCharData(data){
	var type = data.reportContent.map(e=>e.name);
    var total_amount = data.reportContent.map(e=>e.total_due);
    var backgroundColor = [];
    totalAccPayForMgtChartConfig.labels = type;
    
    for(var index = 0; index < data.reportContent.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalAccPayForMgtChartConfig.datasets.length; ++index) {
    	totalAccPayForMgtChartConfig.datasets[index].data = total_amount;
    	totalAccPayForMgtChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalAccPayForMgt").text(data.totalOutstanding);
    window.totalAccPayForMgtChart.update();
}

/*function getAccountPayableBySupplierForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_ACCOUNT_PAYABLE_BY_SUPPLIER&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableAccPayForMgt){
		tableAccPayForMgt.ajax.url( dataURL ).load();
	}else{
		tableAccPayForMgt = $('#tableAccPayForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmAccPayForMgt()) ? d : getParmAccPayForMgt();
				},
		        "dataSrc": function(data){
		        	if(typeof data.bills[0].SUPPLIER === 'undefined'){
		        		$("#accPayForMgt").text("");
		        		return [];
		        	}
		        	else {
		        		for(var dataIndex = 0; dataIndex < data.bills.length; dataIndex ++){
		        			data.bills[dataIndex]['NON_PDC_PAYMENT'] = "<div class='mini-chart_container'>"+
		        			"<div class='progress' title='PDC PAYMENT' data-toggle='tooltip' style='background: #e41b1b;'>" +
	        					"<div class='progress-bar' role='progressbar' title='NON PDC PAYMENT' data-toggle='tooltip' "+
	        						"style='width:"+(data.bills[dataIndex]['NON_PDC_PAYMENT']/data.bills[dataIndex]['TOTAL_PAYABLE'])*100+"%;background: #3cb371;'>"+
        						"</div>"+
        						"<div class='mini-chart-line5' style='left: "+(data.bills[dataIndex]['NON_PDC_PAYMENT']/data.bills[dataIndex]['TOTAL_PAYABLE'])*100+"%;'>"+	
									"<div class='mini-chart-data'>"+ parseFloat(data.bills[dataIndex]['NON_PDC_PAYMENT']).toFixed(document.form.numberOfDecimal.value)+"</div>"+
								"</div>"+
								"<div class='mini-chart-line6'>"+	
									"<div class='mini-chart-data2'>"+ parseFloat(data.bills[dataIndex]['PDC_PAYMENT']).toFixed(document.form.numberOfDecimal.value)+"</div>"+
									"</div>"+
								"</div>"+
							"</div>"+"</td>";
		        		}
		        		$("#accPayForMgt").text(data.TOTAL_AMOUNT);
		        		return data.bills;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [
		    	{'data': 'SUPPLIER', "orderable": false},
		    	{'data': 'TOTAL_PAYABLE', "orderable": false},
		    	{'data': 'NON_PDC_PAYMENT', "orderable": false},
		    	{'data': 'PDC_PAYMENT', "orderable": false}
	    	],
	        "columnDefs": [
	            { "visible": false,  "targets": [ 3 ] }
	        ]
		});
	}
}*/

function getSupplierAgeingForMgt(period, loadCust){
	var dataURL = rootURI + "/DashboardServlet?ACTION=GET_SUPPLIER_AGEING_SUMMARY&Order=BILL&FROM_DATE=" + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + "&TO_DATE=" + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY") + "&LOADCUST="+loadCust;
	if (tableSupAgeForMgt){
		tableSupAgeForMgt.ajax.url( dataURL ).load();
	}else{
		tableSupAgeForMgt = $('#tableSupAgeForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				/*"async": false,*/
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(data.LOADCUST == 1){
		        		getCustomerAgeingForMgt(period);
		        	}
		        	if((data.reportContent[0] === '') || (typeof data.reportContent[0] === 'undefined')){
		        		return [];
		        	}
		        	else {
		        		loadSupplierAgeingTotal(data.reportContent);
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'name', "orderable": false},{'data': 'amountdue', "orderable": false},{'data': 'notDue', "orderable": false},{'data': 'v30daysdue', "orderable": false},{'data': 'v60daysdue', "orderable": false},{'data': 'v90daysdue', "orderable": false},{'data': 'v90plusdaysdue', "orderable": false}],
		    order: [[1, 'desc']]
		});
	}
}

function getAccountReceivableForMgt(period){
	storePeriod('AccountReceivableForMgt', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_ACCOUNT_RECEIVABLE_FROM_CUSTOMER&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY")+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.reportContent[0] === 'undefined'){
				totalAccRecvForMgtChartConfig.labels = [];
				for (var index = 0; index < totalAccRecvForMgtChartConfig.datasets.length; ++index) {
					totalAccRecvForMgtChartConfig.datasets[index].data = [];
				}
				window.totalAccRecvForMgtChart.update();
				$(".totalAccRecvMsg").show();
				$("#totalAccRecvForMgt").text("");
        	}
        	else {
        		setAccountReceivableForMgtCharData(data);
        		$(".totalAccRecvMsg").hide();
        	}
		}
   	});
}

function setAccountReceivableForMgtCharData(data){
	var type = data.reportContent.map(e=>e.name);
    var total_amount = data.reportContent.map(e=>e.total_due);
    var backgroundColor = [];
    totalAccRecvForMgtChartConfig.labels = type;
    
    for(var index = 0; index < data.reportContent.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalAccRecvForMgtChartConfig.datasets.length; ++index) {
    	totalAccRecvForMgtChartConfig.datasets[index].data = total_amount;
    	totalAccRecvForMgtChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalAccRecvForMgt").text(data.totalOutstanding);
    window.totalAccRecvForMgtChart.update();
}

/*function getAccountReceivableByCustomerForMgt(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_ACCOUNT_RECEIVABLE_BY_CUSTOMER&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableRecvPayForMgt){
		tableRecvPayForMgt.ajax.url( dataURL ).load();
	}else{
		tableRecvPayForMgt = $('#tableRecvPayForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmAccountReceivableByCustomer()) ? d : getParmAccountReceivableByCustomer();
				},
		        "dataSrc": function(data){
		        	if(typeof data.invoices[0].CUSTOMER === 'undefined'){
		        		$("#accRecvForMgt").text("");
		        		return [];
		        	}
		        	else {
		        		for(var dataIndex = 0; dataIndex < data.invoices.length; dataIndex ++){
		        			data.invoices[dataIndex]['NON_PDC_PAYMENT'] = "<div class='mini-chart_container'>"+
		        			"<div class='progress' title='PDC PAYMENT' data-toggle='tooltip' style='background: #3cb371;'>" +
	        					"<div class='progress-bar' role='progressbar' title='NON PDC PAYMENT' data-toggle='tooltip' "+
	        						"style='width:"+(data.invoices[dataIndex]['NON_PDC_PAYMENT']/data.invoices[dataIndex]['TOTAL_RECEIVABLE'])*100+"%;background: #e41b1b;'>"+
        						"</div>"+
        						"<div class='mini-chart-line5' style='left: "+(data.invoices[dataIndex]['NON_PDC_PAYMENT']/data.invoices[dataIndex]['TOTAL_RECEIVABLE'])*100+"%;'>"+	
									"<div class='mini-chart-data'>"+ parseFloat(data.invoices[dataIndex]['NON_PDC_PAYMENT']).toFixed(document.form.numberOfDecimal.value)+"</div>"+
								"</div>"+
								"<div class='mini-chart-line6'>"+	
									"<div class='mini-chart-data2'>"+ parseFloat(data.invoices[dataIndex]['PDC_PAYMENT']).toFixed(document.form.numberOfDecimal.value)+"</div>"+
									"</div>"+
								"</div>"+
							"</div>"+"</td>";
		        		}
		        		$("#accRecvForMgt").text(data.TOTAL_AMOUNT);
		        		return data.invoices;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [
		    	{'data': 'CUSTOMER', "orderable": false},
		    	{'data': 'TOTAL_RECEIVABLE', "orderable": false},
		    	{'data': 'NON_PDC_PAYMENT', "orderable": false},
		    	{'data': 'PDC_PAYMENT', "orderable": false}
	    	],
	        "columnDefs": [
	        	{
	                "render": function ( data, type, row ) {
	                	return $("<div></div>", {
                            "class": "bar-chart-bar"
                        }).append(function(){
                            var bars = [];
                            for(var i = 2; i < Object.keys(row).length; i++){
                                bars.push($("<div ></div>",{
                                    "class": "bar " + "bar_" + i,
                                    "data-toggle":"tooltip",
                                    "title": Object.keys(row)[i] + " : " + Object.values(row)[i],
                                }).css({
                                    "width": (Object.values(row)[i]/Object.values(row)[1])*100 + "%"
                                }))
                            }
                            //$(".bar").tooltip();
                            return bars;
                        }).prop("outerHTML");
	                	
	                },
	                "targets": 2
	            },
	            { "visible": false,  "targets": [ 3 ] }
	        ]
		});
	}
}*/

function getCustomerAgeingForMgt(period){
	var dataURL = rootURI + "/DashboardServlet?ACTION=GET_CUSTOMER_AGEING_SUMMARY&Order=INVOICE&FROM_DATE=" + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + "&TO_DATE=" + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY");
	if (tablecustAgeForMgt){
		tablecustAgeForMgt.ajax.url( dataURL ).load();
	}else{
		tablecustAgeForMgt = $('#tablecustAgeForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				/*"async": false,*/
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if((data.reportContent[0] === '') || (typeof data.reportContent[0] === 'undefined')){
		        		return [];
		        	}
		        	else {
		        		loadCustomerAgeingTotal(data.reportContent);
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'name', "orderable": false},{'data': 'amountdue', "orderable": false},{'data': 'notDue', "orderable": false},{'data': 'v30daysdue', "orderable": false},{'data': 'v60daysdue', "orderable": false},{'data': 'v90daysdue', "orderable": false},{'data': 'v90plusdaysdue', "orderable": false}],
		    order: [[1, 'desc']]
		});
	}
}

function getPaymentPdcForMgt(period){
	storePeriod('PaymentPdcSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_PAYMENT_PDC&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tablePaymentPdcForMgt){
		tablePaymentPdcForMgt.ajax.url( dataURL ).load();
	}else{
		tablePaymentPdcForMgt = $('#tablePaymentPdcForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmPaymentPdcForMgt()) ? d : getParmPaymentPdcForMgt();
				},
		        "dataSrc": function(data){
		        	if(typeof data.payments[0].PAYMENT_DATE === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.payments;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'SNO', "orderable": false},{'data': 'PAYMENT_DATE', "orderable": false},{'data': 'SUPPLIER', "orderable": false},{'data': 'BANK_BRANCH', "orderable": false},{'data': 'CHECQUE_NO', "orderable": false},{'data': 'CHEQUE_DATE', "orderable": false},{'data': 'CHEQUE_REVERSAL_DATE', "orderable": false},{'data': 'CHEQUE_AMOUNT', "orderable": false}]
		});
	}
}

function getPaymentRecvPdcForMgt(period){
	storePeriod('PaymentRecvPdcSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_PAYMENT_RECV_PDC&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tablePaymentRecvPdcForMgt){
		tablePaymentRecvPdcForMgt.ajax.url( dataURL ).load();
	}else{
		tablePaymentRecvPdcForMgt = $('#tablePaymentRecvPdcForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmPaymentRecvPdcForMgt()) ? d : getParmPaymentRecvPdcForMgt();
				},
		        "dataSrc": function(data){
		        	if(typeof data.payments[0].PAYMENT_DATE === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.payments;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'SNO', "orderable": false},{'data': 'PAYMENT_DATE', "orderable": false},{'data': 'CUSTOMER', "orderable": false},{'data': 'BANK_BRANCH', "orderable": false},{'data': 'CHECQUE_NO', "orderable": false},{'data': 'CHEQUE_DATE', "orderable": false},{'data': 'CHEQUE_REVERSAL_DATE', "orderable": false},{'data': 'CHEQUE_AMOUNT', "orderable": false}]
		});
	}
}

function getTotalAsset(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_ASSET&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"YYYY-MM-DD") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"YYYY-MM-DD");
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.TOTAL_ASSET)){
			$('#totalAsset').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#totalAsset').html(parseFloat(data.TOTAL_ASSET).toFixed(document.form.numberOfDecimal.value));
		}
		$('#totalAsset').countTo({from: 0, to: $('#totalAsset').html()});
	});
}


function getCashInHand(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_CASH_IN_HAND&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"YYYY-MM-DD") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"YYYY-MM-DD");
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.TOTAL_CASHINHAND)){
			$('#totalCashInHand').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#totalCashInHand').html(parseFloat(data.TOTAL_CASHINHAND).toFixed(document.form.numberOfDecimal.value));
		}
		$('#totalCashInHand').countTo({from: 0, to: $('#totalCashInHand').html()});
	});
}

function getCashInHandAll(){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_CASH_IN_HAND_ALL';
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.TOTAL_CASHINHAND)){
			$('#totalCashInHand').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#totalCashInHand').html(parseFloat(data.TOTAL_CASHINHAND).toFixed(document.form.numberOfDecimal.value));
		}
		$('#totalCashInHand').countTo({from: 0, to: $('#totalCashInHand').html()});
	});
}

function getTotalLiability(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_LIABILITY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"YYYY-MM-DD") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"YYYY-MM-DD");
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data){
		if (isNaN(data.TOTAL_LIABILITY)){
			$('#totalLiability').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#totalLiability').html(parseFloat(data.TOTAL_LIABILITY).toFixed(document.form.numberOfDecimal.value));
		}
		$('#totalLiability').countTo({from: 0, to: $('#totalLiability').html()});
	});
}

function getTotalCashAtBank(period){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_CASH_AT_BANK&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"YYYY-MM-DD") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"YYYY-MM-DD");
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data){
		if (isNaN(data.TOTAL_CASHATBANK)){
			$('#totalCashAtBank').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#totalCashAtBank').html(parseFloat(data.TOTAL_CASHATBANK).toFixed(document.form.numberOfDecimal.value));
		}
		$('#totalCashAtBank').countTo({from: 0, to: $('#totalCashAtBank').html()});
	});
}

function getTotalCashAtBankAll(){
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_TOTAL_CASH_AT_BANK_ALL'
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data){
		if (isNaN(data.TOTAL_CASHATBANK)){
			$('#totalCashAtBank').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#totalCashAtBank').html(parseFloat(data.TOTAL_CASHATBANK).toFixed(document.form.numberOfDecimal.value));
		}
		$('#totalCashAtBank').countTo({from: 0, to: $('#totalCashAtBank').html()});
	});
}

function getNetProfit(period){
	storePeriod('netProfitSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_NET_PROFIT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"YYYY-MM-DD") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"YYYY-MM-DD");
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.NET_PROFIT)){
			$('#netProfit').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#netProfit').html(parseFloat(data.NET_PROFIT).toFixed(document.form.numberOfDecimal.value));
		}
		$('#netProfit').countTo({from: 0, to: $('#netProfit').html()});
	});
}

function getGrossProfit(period){
	storePeriod('grossProfitSummary', period);
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_GROSS_PROFIT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.GROSS_PROFIT)){
			$('#grossProfit').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#grossProfit').html(parseFloat(data.GROSS_PROFIT).toFixed(document.form.numberOfDecimal.value));
		}
		$('#grossProfit').countTo({from: 0, to: $('#grossProfit').html()});
	});
}


function getNetSalary(period){
	storePeriod('NetSalarySummary', period);
	var dataURL = rootURI + '/PayrollServlet?Submit=GET_TOTAL_NET_SALARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY");
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.NET_SALARY)){
			$('#netSalary').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#netSalary').html(parseFloat(data.NET_SALARY).toFixed(document.form.numberOfDecimal.value));
		}
		$('#netSalary').countTo({from: 0, to: $('#netSalary').html()});
	});
}

function getGrossSalary(period){
	storePeriod('GrossSalarySummary', period);
	var dataURL = rootURI + '/PayrollServlet?Submit=GET_TOTAL_GROSS_SALARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY");
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.GROSS_SALARY)){
			$('#grossSalary').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#grossSalary').html(parseFloat(data.GROSS_SALARY).toFixed(document.form.numberOfDecimal.value));
		}
		$('#grossSalary').countTo({from: 0, to: $('#grossSalary').html()});
	});
}

function getDeduction(period){
	storePeriod('DeductionSummary', period);
	var dataURL = rootURI + '/PayrollServlet?Submit=GET_TOTAL_DEDUCTION&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY");
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		if (isNaN(data.DEDUCTION)){
			$('#sdeduction').html(parseFloat(0.00000).toFixed(document.form.numberOfDecimal.value));					
		}else{
	  		$('#sdeduction').html(parseFloat(data.DEDUCTION).toFixed(document.form.numberOfDecimal.value));
		}
		$('#sdeduction').countTo({from: 0, to: $('#sdeduction').html()});
	});
}


function getNetSalandGrosssSal(period){
	storePeriod('NetSalandGrosssSalSummary', period);
	var dataURL = rootURI + '/PayrollServlet?Submit=GET_TOTAL_NET_GROSS_SALARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY");
	$.ajax({
	  type: "POST", 
	  url: dataURL,
	  context: document.body
	}).done(function(data) {
		
		console.log(data)
		
		var ctxL = document.getElementById("lineChart").getContext('2d');
	    var myLineChart = new Chart(ctxL, {
	    type: 'line',
	    data: {
	    labels: data.LABLE,
	    datasets: [{
	    label: "NET SALARY",
	    data: data.NETSAL,
	    backgroundColor: [
	    'rgba(105, 0, 132, .2)',
	    ],
	    borderColor: [
	    'rgba(240,86,61)',
	    ],
	    borderWidth: 2
	    },
	    {
	    label: "GROSS SALARY",
	    data: data.GROSSSAL,
	    backgroundColor: [
	    'rgba(0, 137, 132, .2)',
	    ],
	    borderColor: [
	    'rgba(23,160,134)',
	    ],
	    borderWidth: 2
	    },
	    {
		    label: "DEDUCTIONS",
		    data: data.DEDUCTION,
		    backgroundColor: [
		    'rgba(211,241,255)',
		    ],
		    borderColor: [
		    'rgba(41,182,246)',
		    ],
		    borderWidth: 2
		    }
	    ]
	    },
	    options: {
	    responsive: true,
	    scales: {
            yAxes: [{
                ticks: {
                    // Include a dollar sign in the ticks
                    callback: function(value, index, values) {
                        return data.BCURRENCY + value;
                    }
                }
            }]
        }
	    }
	    });
	});
}


function getParmPurchaseSummaryMgt(){
	return {
		"CNAME": $('input[name ="vendnameMgt"]').val()
	}
}

function getParmAccPayForMgt(){
	return {
		"VNAME": $('input[name ="vendnameaccPay"]').val()
	}
}

function getParmAccountReceivableByCustomer(){
	return {
		"CNAME": $('input[name ="custnameaccRecv"]').val()
	}
}

function getParmPaymentPdcForMgt(){
	return {
		"VNAME": $('input[name ="vendnamepayPdc"]').val()
	}
}

function getParmPaymentRecvPdcForMgt(){
	return {
		"CNAME": $('input[name ="custnamepayRecvPdc"]').val()
	}
}

function getParmAccountPayable(){
	return {
		"VNAME": $('input[name ="vendNameAccPay"]').val()
	}
}

function getParmAccountReceivable(){
	return {
		"CNAME": $('input[name ="custNameAccRecv"]').val()
	}
}

function getParmPaymentPdc(){
	return {
		"VNAME": $('input[name ="vendNamePayPdc"]').val()
	}
}

function getParmPaymentRecvPdc(){
	return {
		"CNAME": $('input[name ="custNamePayRecvPdc"]').val()
	}
}

function getpurchaseSummaryForMgt(period){
	storePeriod('purchaseSummaryForMgt', period);
	//var dataURL = rootURI + '/DashboardServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&CNAME=' ++ '&STATUS=' +;
	// var cname = $('input[name ="vendnameMgt"]').val();
	var status = $('input[name ="purstatusMgt"]').val();
	
	var dataURL = rootURI + '/BillingServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tablePurchaseSummaryForMgt){
		tablePurchaseSummaryForMgt.ajax.url( dataURL ).load();
	}else{
		tablePurchaseSummaryForMgt = $('#tablePurchaseSummaryForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmPurchaseSummaryMgt()) ? d : getParmPurchaseSummaryMgt();
				},
		        "dataSrc": function(data){
		        	if(data.items.length == 0){
		        		$("#totalpurchaseSummaryForMgt").text("");
		        		return [];
		        	}
		        	else {
		        		$("#totalpurchaseSummaryForMgt").text(data.total);
		        		return data.items;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'billdate', "orderable": false},
		    			{'data': 'bill', "orderable": false},
		    			{'data': 'custname', "orderable": false},
		    			{'data': 'status', "orderable": false},
		    			{'data': 'DEBITS', "orderable": false}]
		});
	}
}

function getParmSummaryMgt(){
	return {
		"CNAME": $('input[name ="custnameMgt"]').val()
	}
}

function getsalesSummaryForMgt(period){
	storePeriod('salesSummaryForMgt', period);
	// var cname = $('input[name ="custnameMgt"]').val();
	var status = $('input[name ="salesstatusMgt"]').val();
	
	//var dataURL = rootURI + '/InvoiceServlet?ACTION=VIEW_INVOICE_SUMMARY_DASHBOARD_VIEW&CNAME='+cname+'&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	var dataURL = rootURI + '/InvoiceServlet?ACTION=VIEW_INVOICE_SUMMARY_DASHBOARD_VIEW&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableSalesSummaryForMgt){
		tableSalesSummaryForMgt.ajax.url( dataURL ).load();
	}else{
		tableSalesSummaryForMgt = $('#tableSalesSummaryForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmSummaryMgt()) ? d : getParmSummaryMgt();
				}, 
		        "dataSrc": function(data){
		        	if(data.items.length == 0){
		        		$("#totalSalesSummaryForMgt").text("");
		        		return [];
		        	}
		        	else {
		        		$("#totalSalesSummaryForMgt").text(data.total);
		        		return data.items;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'billdate', "orderable": false},
		    			{'data': 'bill', "orderable": false},
		    			{'data': 'custname', "orderable": false},
		    			{'data': 'status', "orderable": false},
		    			{'data': 'CREDITS', "orderable": false}]
		});
	}
}

function getParameters(){
	return {
		"ACCOUNTNAME": $('input[name ="accountSearchMgt"]').val(),
		"CUSTOMER": $('input[name ="custnameIncomeMgt"]').val(),
	}
}

function getInvoiceSummaryForMgt(period){
	storePeriod('InvoiceSummaryMgt', period);
	//var account = $('input[name ="accountSearchMgt"]').val();

	//var dataURL = rootURI + '/BillingServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&CNAME='+cname+'&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));+'&ACCOUNT='+account
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_INCOME_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	//var dataURL = rootURI + '/DashboardServlet';
	if (tableIncomeSummaryForMgt){
		tableIncomeSummaryForMgt.ajax.url( dataURL ).load();
	}else{
		tableIncomeSummaryForMgt = $('#tableIncomeSummaryForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
				}, 
		        "dataSrc": function(data){
		        	if(data.INCOME.length == 0){
		        		$("#totalincomeSummaryForMgt").text("");
		        		return [];
		        	}
		        	else {
		        		$("#totalincomeSummaryForMgt").text(data.total);
		        		return data.INCOME;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'JOURNAL_DATE', "orderable": false},
		    			{'data': 'NAME', "orderable": false},
		    			{'data': 'ACCOUNT_NAME', "orderable": false},
		    			{'data': 'REFERENCE', "orderable": false},
		    			{'data': 'TOTAL_AMOUNT', "orderable": false}]
		});
	}
}


function getParametersExpMgt(){
	return {
		"ACCOUNTNAME": $('input[name ="accountExpSearchMgt"]').val(),
		"SUPPLIER": $('input[name ="vendnameExpenseMgt"]').val()
	}
}

function getExpenseSummaryForMgt(period){
	storePeriod('ExpenseSummaryForMgt', period);
	//var account = $('input[name ="accountSearchMgt"]').val();

	//var dataURL = rootURI + '/BillingServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&CNAME='+cname+'&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));+'&ACCOUNT='+account
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_EXPENSE_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	//var dataURL = rootURI + '/DashboardServlet';
	if (tableExpenseSummaryForMgt){
		tableExpenseSummaryForMgt.ajax.url( dataURL ).load();
	}else{
		tableExpenseSummaryForMgt = $('#tableExpenseSummaryForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParametersExpMgt()) ? d : getParametersExpMgt();
				}, 
		        "dataSrc": function(data){
		        	if(data.EXPENSE.length == 0){
		        		$("#totalexpenseSummaryForMgt").text("");
		        		return [];
		        	}
		        	else {
		        		$("#totalexpenseSummaryForMgt").text(data.total);
		        		return data.EXPENSE;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'JOURNAL_DATE', "orderable": false},
		    			{'data': 'NAME', "orderable": false},
		    			{'data': 'ACCOUNT_NAME', "orderable": false},
		    			{'data': 'REFERENCE', "orderable": false},
		    			{'data': 'TOTAL_AMOUNT', "orderable": false}]
		});
	}
}


function getParametersPayIssueMgt(){
	return {
		"ACCOUNTNAME": $('input[name ="accountPayIssuedSearchMgt"]').val(),
		"SUPPLIER": $('input[name ="vendnamePayIssueenseMgt"]').val()
	}
}

function getPaymentIssuedSummaryForMgt(period){
	storePeriod('PaymentIssuedSummaryForMgt', period);
	//var account = $('input[name ="accountSearchMgt"]').val();

	//var dataURL = rootURI + '/BillingServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&CNAME='+cname+'&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));+'&ACCOUNT='+account
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_PAYMENT_ISSUED_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	//var dataURL = rootURI + '/DashboardServlet';
	if (tablePaymentIssuedSummaryForMgt){
		tablePaymentIssuedSummaryForMgt.ajax.url( dataURL ).load();
	}else{
		tablePaymentIssuedSummaryForMgt = $('#tablePayIssueSummaryForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParametersPayIssueMgt()) ? d : getParametersPayIssueMgt();
				}, 
		        "dataSrc": function(data){
		        	if(data.PAYMENT.length == 0){
		        		$("#PaymentIssuedSummaryForMgt").text("");
		        		return [];
		        	}
		        	else {
		        		for(var dataIndex = 0; dataIndex < data.PAYMENT.length; dataIndex ++){
		        			if(data.PAYMENT[dataIndex]['NAME'] == '-'){
		        				data.PAYMENT[dataIndex]['ACNAME'] = data.PAYMENT[dataIndex]['ACCOUNT_NAME'];
		        			}else{
		        				data.PAYMENT[dataIndex]['ACNAME'] = data.PAYMENT[dataIndex]['NAME']+"-"+data.PAYMENT[dataIndex]['ACCOUNT_NAME'];
		        			}
		        		}
		        		$("#PaymentIssuedSummaryForMgt").text(data.total);
		        		return data.PAYMENT;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'JOURNAL_DATE', "orderable": false},
		    			{'data': 'ACNAME', "orderable": false},
		    			{'data': 'PAIDTO', "orderable": false},
		    			{'data': 'TRANSACTION_ID', "orderable": false},
		    			{'data': 'TOTAL_AMOUNT', "orderable": false}]
		});
	}
}

function getParametersPayReceiptMgt(){
	return {
		"ACCOUNTNAME": $('input[name ="accountPayReceiptSearchMgt"]').val(),
		"CUSTOMER": $('input[name ="custnamePayRecpMgt"]').val()
	}
}

function getPaymentReceiptSummaryForMgt(period){
	storePeriod('PaymentReceiptSummaryForMgt', period);
	//var account = $('input[name ="accountSearchMgt"]').val();

	//var dataURL = rootURI + '/BillingServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&CNAME='+cname+'&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));+'&ACCOUNT='+account
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_PAYMENT_RECEIPT_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	//var dataURL = rootURI + '/DashboardServlet';
	if (tablePayReceiptSummaryForMgt){
		tablePayReceiptSummaryForMgt.ajax.url( dataURL ).load();
	}else{
		tablePayReceiptSummaryForMgt = $('#tablePayReceiptSummaryForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParametersPayReceiptMgt()) ? d : getParametersPayReceiptMgt();
				}, 
		        "dataSrc": function(data){
		        	if(data.PAYMENT.length == 0){
		        		$("#PaymentReceiptSummaryForMgt").text("");
		        		return [];
		        	}
		        	else {
		        		for(var dataIndex = 0; dataIndex < data.PAYMENT.length; dataIndex ++){
		        			if(data.PAYMENT[dataIndex]['NAME'] == '-'){
		        				data.PAYMENT[dataIndex]['ACNAME'] = data.PAYMENT[dataIndex]['ACCOUNT_NAME'];
		        			}else{
		        				data.PAYMENT[dataIndex]['ACNAME'] = data.PAYMENT[dataIndex]['NAME']+"-"+data.PAYMENT[dataIndex]['ACCOUNT_NAME'];
		        			}
		        		}
		        		
		        		$("#PaymentReceiptSummaryForMgt").text(data.total);
		        		return data.PAYMENT;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'JOURNAL_DATE', "orderable": false},
		    			{'data': 'ACNAME', "orderable": false},
		    			{'data': 'DEPOSITTO', "orderable": false},
		    			{'data': 'TRANSACTION_ID', "orderable": false},
		    			{'data': 'TOTAL_AMOUNT', "orderable": false}]
		});
	}
}

/*--------------------- accounts dashboard-------------------------------*/



function getParmPurchaseSummary(){
	return {
		"CNAME": $('input[name ="vendname"]').val()
	}
}

function getpurchaseSummary(period){
	storePeriod('PurchaseSummary', period);
	//var dataURL = rootURI + '/DashboardServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&CNAME=' ++ '&STATUS=' +;
	// var cname = $('input[name ="vendname"]').val();
	var status = $('input[name ="purstatus"]').val();
	
	var dataURL = rootURI + '/BillingServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tablePurchaseSummary){
		tablePurchaseSummary.ajax.url( dataURL ).load();
	}else{
		tablePurchaseSummary = $('#tablePurchaseSummary').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmPurchaseSummary()) ? d : getParmPurchaseSummary();
				},
		        "dataSrc": function(data){
		        	if(data.items.length == 0){
		        		$("#totalpurchaseSummary").text("");
		        		return [];
		        	}
		        	else {
		        		$("#totalpurchaseSummary").text(data.total);
		        		return data.items;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'billdate', "orderable": false},
		    			{'data': 'bill', "orderable": false},
		    			{'data': 'custname', "orderable": false},
		    			{'data': 'status', "orderable": false},
		    			{'data': 'DEBITS', "orderable": false}]
		});
	}
}

function getParmSummary(){
	return {
		"CNAME": $('input[name ="custname"]').val()
	}
}

function getsalesSummaryAcc(period){
	storePeriod('salesSummaryAcc', period);
	// var cname = $('input[name ="custname"]').val();
	var status = $('input[name ="salesstatus"]').val();
	
	//var dataURL = rootURI + '/InvoiceServlet?ACTION=VIEW_INVOICE_SUMMARY_DASHBOARD_VIEW&CNAME='+cname+'&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	var dataURL = rootURI + '/InvoiceServlet?ACTION=VIEW_INVOICE_SUMMARY_DASHBOARD_VIEW&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableSalesSummary){
		tableSalesSummary.ajax.url( dataURL ).load();
	}else{
		tableSalesSummary = $('#tableSalesSummary').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmSummary()) ? d : getParmSummary();
				}, 
		        "dataSrc": function(data){
		        	if(data.items.length == 0){
		        		$("#totalSalesSummary").text("");
		        		return [];
		        	}
		        	else {
		        		$("#totalSalesSummary").text(data.total);
		        		return data.items;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'billdate', "orderable": false},
		    			{'data': 'bill', "orderable": false},
		    			{'data': 'custname', "orderable": false},
		    			{'data': 'status', "orderable": false},
		    			{'data': 'CREDITS', "orderable": false}]
		});
	}
}

function getParametersinv(){
	return {
		"ACCOUNTNAME": $('input[name ="accountSearch"]').val(),
		"CUSTOMER": $('input[name ="custnameIncome"]').val(),
	}
}

function getInvoiceSummary(period){
	storePeriod('InvoiceSummary', period);
	//var account = $('input[name ="accountSearchMgt"]').val();

	//var dataURL = rootURI + '/BillingServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&CNAME='+cname+'&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));+'&ACCOUNT='+account
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_INCOME_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	//var dataURL = rootURI + '/DashboardServlet';
	if (tableIncomeSummary){
		tableIncomeSummary.ajax.url( dataURL ).load();
	}else{
		tableIncomeSummary = $('#tableIncomeSummary').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParametersinv()) ? d : getParametersinv();
				}, 
		        "dataSrc": function(data){
		        	if(data.INCOME.length == 0){
		        		$("#totalincomeSummary").text("");
		        		return [];
		        	}
		        	else {
		        		$("#totalincomeSummary").text(data.total);
		        		return data.INCOME;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'JOURNAL_DATE', "orderable": false},
		    			{'data': 'NAME', "orderable": false},
		    			{'data': 'ACCOUNT_NAME', "orderable": false},
		    			{'data': 'REFERENCE', "orderable": false},
		    			{'data': 'TOTAL_AMOUNT', "orderable": false}]
		});
	}
}


function getParametersExp(){
	return {
		"ACCOUNTNAME": $('input[name ="accountExpSearch"]').val(),
		"SUPPLIER": $('input[name ="vendnameExpense"]').val()
	}
}

function getExpenseSummary(period){
	storePeriod('ExpenseSummary', period);
	//var account = $('input[name ="accountSearchMgt"]').val();

	//var dataURL = rootURI + '/BillingServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&CNAME='+cname+'&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));+'&ACCOUNT='+account
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_EXPENSE_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	//var dataURL = rootURI + '/DashboardServlet';
	if (tableExpenseSummary){
		tableExpenseSummary.ajax.url( dataURL ).load();
	}else{
		tableExpenseSummary = $('#tableExpenseSummary').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParametersExp()) ? d : getParametersExp();
				}, 
		        "dataSrc": function(data){
		        	if(data.EXPENSE.length == 0){
		        		$("#totalexpenseSummary").text("");
		        		return [];
		        	}
		        	else {
		        		$("#totalexpenseSummary").text(data.total);
		        		return data.EXPENSE;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'JOURNAL_DATE', "orderable": false},
		    			{'data': 'NAME', "orderable": false},
		    			{'data': 'ACCOUNT_NAME', "orderable": false},
		    			{'data': 'REFERENCE', "orderable": false},
		    			{'data': 'TOTAL_AMOUNT', "orderable": false}]
		});
	}
}


function getParametersPayIssue(){
	return {
		"ACCOUNTNAME": $('input[name ="accountPayIssuedSearch"]').val(),
		"SUPPLIER": $('input[name ="vendnamePayIssueense"]').val()
	}
}

function getPaymentIssuedSummary(period){
	storePeriod('PaymentIssuedSummary', period);
	//var account = $('input[name ="accountSearchMgt"]').val();

	//var dataURL = rootURI + '/BillingServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&CNAME='+cname+'&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));+'&ACCOUNT='+account
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_PAYMENT_ISSUED_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	//var dataURL = rootURI + '/DashboardServlet';
	if (tablePaymentIssuedSummary){
		tablePaymentIssuedSummary.ajax.url( dataURL ).load();
	}else{
		tablePaymentIssuedSummary = $('#tablePayIssueSummary').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParametersPayIssue()) ? d : getParametersPayIssue();
				}, 
		        "dataSrc": function(data){
		        	if(data.PAYMENT.length == 0){
		        		$("#PaymentIssuedSummary").text("");
		        		return [];
		        	}
		        	else {
		        		for(var dataIndex = 0; dataIndex < data.PAYMENT.length; dataIndex ++){
		        			if(data.PAYMENT[dataIndex]['NAME'] == '-'){
		        				data.PAYMENT[dataIndex]['ACNAME'] = data.PAYMENT[dataIndex]['ACCOUNT_NAME'];
		        			}else{
		        				data.PAYMENT[dataIndex]['ACNAME'] = data.PAYMENT[dataIndex]['NAME']+"-"+data.PAYMENT[dataIndex]['ACCOUNT_NAME'];
		        			}
		        		}
		        		$("#PaymentIssuedSummary").text(data.total);
		        		return data.PAYMENT;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'JOURNAL_DATE', "orderable": false},
    			{'data': 'ACNAME', "orderable": false},
    			{'data': 'PAIDTO', "orderable": false},
    			{'data': 'TRANSACTION_ID', "orderable": false},
    			{'data': 'TOTAL_AMOUNT', "orderable": false}]
		});
	}
}

function getParametersPayReceipt(){
	return {
		"ACCOUNTNAME": $('input[name ="accountPayReceiptSearch"]').val(),
		"CUSTOMER": $('input[name ="custnamePayRecp"]').val()
	}
}

function getPaymentReceiptSummary(period){
	storePeriod('PaymentReceiptSummary', period);
	//var account = $('input[name ="accountSearchMgt"]').val();

	//var dataURL = rootURI + '/BillingServlet?ACTION=VIEW_PURCHASE_DASHBOARD_VIEW&CNAME='+cname+'&STATUS='+status+'&FDATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TDATE=' + getFormattedDate(moment(getToDateForPeriod(period)));+'&ACCOUNT='+account
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_PAYMENT_RECEIPT_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	//var dataURL = rootURI + '/DashboardServlet';
	if (tablePayReceiptSummary){
		tablePayReceiptSummary.ajax.url( dataURL ).load();
	}else{
		tablePayReceiptSummary = $('#tablePayReceiptSummary').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>> ',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParametersPayReceipt()) ? d : getParametersPayReceipt();
				}, 
		        "dataSrc": function(data){
		        	if(data.PAYMENT.length == 0){
		        		$("#PaymentReceiptSummary").text("");
		        		return [];
		        	}
		        	else {
		        		for(var dataIndex = 0; dataIndex < data.PAYMENT.length; dataIndex ++){
		        			if(data.PAYMENT[dataIndex]['NAME'] == '-'){
		        				data.PAYMENT[dataIndex]['ACNAME'] = data.PAYMENT[dataIndex]['ACCOUNT_NAME'];
		        			}else{
		        				data.PAYMENT[dataIndex]['ACNAME'] = data.PAYMENT[dataIndex]['NAME']+"-"+data.PAYMENT[dataIndex]['ACCOUNT_NAME'];
		        			}
		        			
		        		}
		        		$("#PaymentReceiptSummary").text(data.total);
		        		return data.PAYMENT;
		        	}
		        }
		    },
		    "pageLength": 5,"order": [[ 0, "desc" ]],
		    "columns": [{'data': 'JOURNAL_DATE', "orderable": false},
    			{'data': 'ACNAME', "orderable": false},
    			{'data': 'DEPOSITTO', "orderable": false},
    			{'data': 'TRANSACTION_ID', "orderable": false},
    			{'data': 'TOTAL_AMOUNT', "orderable": false}]
		});
	}
}


/*--------------------- accounts dashboard-------------------------------*/

function loadCustomerAgeingTotal(data){
	var notDue=0, v30daysdue=0, v60daysdue=0, v90daysdue=0, v90plusdaysdue=0;
	for(i=0;i<data.length;i++){
		notDue+=data[i].notDue;
		v30daysdue+=data[i].v30daysdue;
		v60daysdue+=data[i].v60daysdue;
		v90daysdue+=data[i].v90daysdue;
		v90plusdaysdue+=data[i].v90plusdaysdue;
	}
	var tbody="<tr>";
	tbody+="<td>"+parseFloat(parseFloat(notDue)+parseFloat(v30daysdue)+parseFloat(v60daysdue)
			+parseFloat(v90daysdue)+parseFloat(v90plusdaysdue)).toFixed(document.form.numberOfDecimal.value)+"</td>";
	tbody+="<td>"+parseFloat(notDue).toFixed(document.form.numberOfDecimal.value)+"</td>";
	tbody+="<td>"+parseFloat(v30daysdue).toFixed(document.form.numberOfDecimal.value)+"</td>";
	tbody+="<td>"+parseFloat(v60daysdue).toFixed(document.form.numberOfDecimal.value)+"</td>";
	tbody+="<td>"+parseFloat(v90daysdue).toFixed(document.form.numberOfDecimal.value)+"</td>";
	tbody+="<td>"+parseFloat(v90plusdaysdue).toFixed(document.form.numberOfDecimal.value)+"</td>";
	$(".tableCusAgeTotal > tbody").html(tbody);
}

function loadSupplierAgeingTotal(data){
	var notDue=0, v30daysdue=0, v60daysdue=0, v90daysdue=0, v90plusdaysdue=0;
	for(i=0;i<data.length;i++){
		notDue+=data[i].notDue;
		v30daysdue+=data[i].v30daysdue;
		v60daysdue+=data[i].v60daysdue;
		v90daysdue+=data[i].v90daysdue;
		v90plusdaysdue+=data[i].v90plusdaysdue;
	}
	var tbody="<tr>";
	tbody+="<td>"+parseFloat(parseFloat(notDue)+parseFloat(v30daysdue)+parseFloat(v60daysdue)
			+parseFloat(v90daysdue)+parseFloat(v90plusdaysdue)).toFixed(document.form.numberOfDecimal.value)+"</td>";
	tbody+="<td>"+parseFloat(notDue).toFixed(document.form.numberOfDecimal.value)+"</td>";
	tbody+="<td>"+parseFloat(v30daysdue).toFixed(document.form.numberOfDecimal.value)+"</td>";
	tbody+="<td>"+parseFloat(v60daysdue).toFixed(document.form.numberOfDecimal.value)+"</td>";
	tbody+="<td>"+parseFloat(v90daysdue).toFixed(document.form.numberOfDecimal.value)+"</td>";
	tbody+="<td>"+parseFloat(v90plusdaysdue).toFixed(document.form.numberOfDecimal.value)+"</td>";
	$(".tableSupAgeTotal > tbody").html(tbody);
}

function getAccountPayableForMgt(period, loadCust){
	storePeriod('AccountPayableForMgt', period);
	var vendName = $("#vendnameaccPay").val();
	var dataURL = rootURI + "/DashboardServlet?ACTION=GET_ACCOUNT_PAYABLE_TO_SUPPLIER&FROM_DATE=" 
		+ getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + "&TO_DATE=" 
		+ getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY") 
		+ "&LOADCUST="+loadCust+"&vendName="+vendName;
	if (tableAccPayForMgt){
		tableAccPayForMgt.ajax.url( dataURL ).load();
	}else{
		tableAccPayForMgt = $('#tableAccPayForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				/*"async": false,*/
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(data.LOADCUST == 1){
		        		getCustomerAgeingForMgt(period);
		        	}
		        	if((data.reportContent[0] === '') || (typeof data.reportContent[0] === 'undefined')){
		        		return [];
		        	}
		        	else {
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'name', "orderable": false},{'data': 'total_due', "orderable": false},{'data': 'over_due', "orderable": false}],
		    order: [[1, 'desc']]
		});
	}
}

function getAccountPayableToSupplier(period, loadCust){
	storePeriod('AccountPayableToSupplierSummary', period);
	var vendName = $("#vendNameAccPay").val();
	var dataURL = rootURI + "/DashboardServlet?ACTION=GET_ACCOUNT_PAYABLE_TO_SUPPLIER&FROM_DATE=" 
		+ getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") 
		+ "&TO_DATE=" + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY") 
		+ "&LOADCUST="+loadCust+"&vendName="+vendName;
	if (tableAccPay){
		tableAccPay.ajax.url( dataURL ).load();
	}else{
		tableAccPay = $('#tableAccPay').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				/*"async": false,*/
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "dataSrc": function(data){
		        	if(data.LOADCUST == 1){
		        		getCustomerAgeingForMgt(period);
		        	}
		        	if((data.reportContent[0] === '') || (typeof data.reportContent[0] === 'undefined')){
		        		return [];
		        	}
		        	else {
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'name', "orderable": false},{'data': 'total_due', "orderable": false},{'data': 'over_due', "orderable": false}],
		    order: [[1, 'desc']]
		});
	}
}

function getAccountReceivableByCustomerForMgt(period){
	storePeriod('AccountReceivableByCustomerSummary', period);
	var custName = $("#custnameaccRecv").val();
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_ACCOUNT_RECEIVABLE_FROM_CUSTOMER&FROM_DATE=' 
		+ getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") 
		+ '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY")+"&custName="+custName;
	if (tableRecvPayForMgt){
		tableRecvPayForMgt.ajax.url( dataURL ).load();
	}else{
		tableRecvPayForMgt = $('#tableRecvPayForMgt').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmAccountReceivableByCustomer()) ? d : getParmAccountReceivableByCustomer();
				},
		        "dataSrc": function(data){
		        	if(typeof data.reportContent[0] === 'undefined'){
		        		$("#accRecvForMgt").text("");
		        		return [];
		        	}
		        	else {
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'name', "orderable": false},
		    	{'data': 'total_due', "orderable": false},
		    	{'data': 'over_due', "orderable": false}],
	    	order: [[1, 'desc']]
		});
	}
}

function getAccountReceivableByCustomer(period){
	storePeriod('AccountReceivableByCustomerSummary', period);
	var custName = $("#custNameAccRecv").val();
	var dataURL = rootURI + '/DashboardServlet?ACTION=GET_ACCOUNT_RECEIVABLE_FROM_CUSTOMER&FROM_DATE=' 
		+ getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + '&TO_DATE=' 
		+ getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY")+"&custName="+custName;
	if (tableRecvPay){
		tableRecvPay.ajax.url( dataURL ).load();
	}else{
		tableRecvPay = $('#tableRecvPay').DataTable({
			"processing": true,
			"bLengthChange": false,
			"bInfo": false,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-12"p>>',
			"language": {
			      "emptyTable": "No data available"
		    },
			"ajax": {
				"type": "GET",
				"url": dataURL,
				"contentType": "application/json; charset=utf-8",
		        "dataType": "json",
		        "data": function(d){
		        	return jQuery.isEmptyObject(getParmAccountReceivable()) ? d : getParmAccountReceivable();
				},
		        "dataSrc": function(data){
		        	if(typeof data.reportContent[0] === 'undefined'){
		        		$("#accRecv").text("");
		        		return [];
		        	}
		        	else {
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'name', "orderable": false},
		    	{'data': 'total_due', "orderable": false},
		    	{'data': 'over_due', "orderable": false}],
	    	order: [[1, 'desc']]
		});
	}
}

function getPeriod(keyPrefix, period, selectControlId){
	period = getLocalStorageValue("purchaseTransactionDashboard_" + keyPrefix + "Period", period, selectControlId);
	$('#' + selectControlId).siblings('.select-selected').html(period);
	return period;
}

function storePeriod(keyPrefix, period){
	storeInLocalStorage("purchaseTransactionDashboard_" + keyPrefix + "Period", period);
}
