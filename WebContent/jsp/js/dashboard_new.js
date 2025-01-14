$(document).ready(function() {
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
			  if(target == "#accountMgtDashboard"){
				  getTotalPurchaseByBillForMgt('Last 30 days');
				  getTotalSalesByInvoiceForMgt('Last 30 days');
				  getTotalPurchaseSummaryByBillForMgt('Last 30 days');
				  getTotalSalesSummaryByInvoiceForMgt('Last 30 days');
				  getTotalIncomeForMgt('Last 30 days');
				  getTotalIncomeSummaryByInvoiceForMgt('Last 30 days');
				  getTotalExpenseForMgt('Last 30 days');
				  getTotalExpenseSummaryByBillForMgt('Last 30 days');
				  getAccountpayableForMgt('Last 30 days');
				  getAccountPayableBySupplierForMgt('Last 30 days');
				  getSupplierAgeingForMgt('Last 30 days',1);
				  getAccountReceivableForMgt('Last 30 days');
				  getAccountReceivableByCustomerForMgt('Last 30 days');
				  getPaymentPdcForMgt('Last 30 days');
				  getPaymentRecvPdcForMgt('Last 30 days');
				  getTotalAsset('Last 30 days');
				  getTotalLiability('Last 30 days');
				  getNetProfit('Last 30 days');
				  getGrossProfit('Last 30 days');
			  }
			  else if(target == "#accountsDashboard"){
				  getTotalPurchaseByBill('Last 30 days');
				  getTotalSalesByInvoice('Last 30 days');
				  getTotalPurchaseSummaryByBill('Last 30 days');
				  getTotalSalesSummaryByInvoice('Last 30 days');
				  getTotalIncome('Last 30 days');
				  getTotalExpense('Last 30 days');
				  getTotalIncomeSummaryByInvoice('Last 30 days');
				  getTotalExpenseSummaryByBill('Last 30 days');
				  getAccountpayable('Last 30 days');
				  getAccountReceivable('Last 30 days');
				  getAccountPayableBySupplier('Last 30 days');
				  getAccountReceivableByCustomer('Last 30 days');
				  getPaymentPdc('Last 30 days');
				  getPaymentRecvPdc('Last 30 days');
				  getSupplierAgeing('Last 30 days',1);
			  }else if(target == "#purchaseDashboard"){
				  getTotals('Last 30 days');
				  getTopIssuedProducts('Last 30 days');
				  getTopReceivedProducts('Last 30 days');
				  getExpiringProducts('Tomorrow');
				  getStockReplanishmentProducts();
				  getNewSuppliers();
				  getTotalSuppliers();
				  calendar.destroy();
				  calendar.render();				  
			  }else if(target == "#salesDashboard"){
				  getTotalIssue('Last 30 days');
				  getTotalNumberSales('Last 30 days');
				  getSalesSummary('Last 30 days');
				  getTopCustomers('Last 30 days');
				  getTopSalesPrd('Last 30 days');
				  getNewCustomers();
				  getTotalCustomers();
				  calendar3.render();
				  calendar4.render();
			  }else if(target == "#warehouseDashboard"){
				  getTotalReceivedItems('Last 30 days');
				  getTotalIssuedItems('Last 30 days');
				  getPoWoPriceSummary('Last 30 days');
				  getGrnSummary('Last 30 days');
				  getGriSummary('Last 30 days');
				  getReadyToPackOrders('Today');
				  getExpiringProducts('Today');
				  getStockReplanishmentProducts();
				  calendar5.render();
				  calendar6.render();
			  }
		});	
				
		var calendarEl = document.getElementById('calendar');

	    calendar = new FullCalendar.Calendar(calendarEl, {
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
	          url: '../DashboardServlet?ACTION=PURCHASE_DELIVERY_DATE',
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
	    	        		'Total Cost : '+ info.event.extendedProps.TOTAL_COST +'<br>',
	    	        		allowHTML: true,
	    	        		placement: 'right',
	    	      });
	    	  }
	    });
	    
	    /*var calendarE2 = document.getElementById('calendar2');

	    calendar2 = new FullCalendar.Calendar(calendarE2, {
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
	          url: '../DashboardServlet?ACTION=EXPIRED_PURCHASE_ORDER',
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
	        		'Total Cost : '+ info.event.extendedProps.TOTAL_COST +'<br>',
	        		allowHTML: true,
	        		placement: 'right',
    	      });
    	  }
	    });*/
	    
	    var calendarE3 = document.getElementById('calendar3');

	    calendar3 = new FullCalendar.Calendar(calendarE3, {
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
	          url: '../DashboardServlet?ACTION=PENDING_SALES_ESTIMATE',
	          failure: function() {
	            document.getElementById('script-warning').style.display = 'block'
	          }
	        },
		      eventRender: function(info) {
		    	  tippy(info.el, {
		    		  content: 'Order No : '+ info.event.extendedProps.ESTNO +'<br>'+
	    	        		'Customer Name : '+ info.event.extendedProps.CUSTNAME +'<br>'+
	    	        		'Order Date : '+ info.event.extendedProps.COLLECTIONDATE +'<br>'+
	    	        		'Delivery Date : '+ info.event.extendedProps.DELIVERY_DATE +'<br>'+
	    	        		'Total Price : '+ info.event.extendedProps.TOTAL_PRICE +'<br>',
	    	        		allowHTML: true,
	    	        		placement: 'right',
	    	      });
	    	  }
	    });
	    
	    var calendarE4 = document.getElementById('calendar4');

	    calendar4 = new FullCalendar.Calendar(calendarE4, {
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
	          url: '../DashboardServlet?ACTION=SALES_ORDER_DELIVERY_DATE',
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
	    	        		'Total Price : '+ info.event.extendedProps.TOTAL_PRICE +'<br>',
	    	        		allowHTML: true,
	    	        		placement: 'right',
	    	      });
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
	          url: '../DashboardServlet?ACTION=PURCHASE_DELIVERY_DATE',
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
	          url: '../DashboardServlet?ACTION=SALES_ORDER_EXPIRED_DELIVERY_DATE',
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

	    $(".totalPurchaseSel>.select-items>div").on("click",function(){
	    	getTotalReceipt($(this).html().trim());
	    });
	    $(".NoItemPurSel>.select-items>div").on("click",function(){
	    	getTotalNumberReceipt($(this).html().trim());
	    });
	    $(".PurSumrySel>.select-items>div").on("click",function(){
	    	getPurchaseSummary($(this).html().trim());
	    });
	    $(".TopSupSel>.select-items>div").on("click",function(){
	    	getTopSuppliers($(this).html().trim());
	    });
	    
	    $(".totalSalesSel>.select-items>div").on("click",function(){
	    	getTotalIssue($(this).html().trim());
	    });
	    $(".NoItemSaleSel>.select-items>div").on("click",function(){
	    	getTotalNumberSales($(this).html().trim());
	    });
	    $(".saleSumrySel>.select-items>div").on("click",function(){
	    	getSalesSummary($(this).html().trim());
	    });
	    $(".TopCustSel>.select-items>div").on("click",function(){
	    	getTopCustomers($(this).html().trim());
	    });
	    $(".TopSalPrdSel>.select-items>div").on("click",function(){
	    	getTopSalesPrd($(this).html().trim());
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
	    $(".griSel>.select-items>div").on("click",function(){
	    	getGriSummary($(this).html().trim());
	    });
	    $(".rtpSel>.select-items>div").on("click",function(){
	    	getReadyToPackOrders($(this).html().trim());
	    });	 
	    $(".expPrdSel>.select-items>div").on("click",function(){
	    	getExpiringProducts($(this).html().trim());
	    });	 
	    $(".totalPurchaseByBillSel>.select-items>div").on("click",function(){
	    	getTotalPurchaseByBill($(this).html().trim());
	    });
	    $(".totalSalesByInvoiceSel>.select-items>div").on("click",function(){
	    	getTotalSalesByInvoice($(this).html().trim());
	    });
	    $(".totalPurchaseByBillSumrySel>.select-items>div").on("click",function(){
	    	getTotalPurchaseSummaryByBill($(this).html().trim());
	    });
	    $(".totalSalesByInvoiceSumrySel>.select-items>div").on("click",function(){
	    	getTotalSalesSummaryByInvoice($(this).html().trim());
	    });
	    $(".totalIncomeSel>.select-items>div").on("click",function(){
	    	getTotalIncome($(this).html().trim());
	    });
	    $(".totalExpenseSel>.select-items>div").on("click",function(){
	    	getTotalExpense($(this).html().trim());
	    });
	    $(".totalIncomeByInvoiceSumrySel>.select-items>div").on("click",function(){
	    	getTotalIncomeSummaryByInvoice($(this).html().trim());
	    });
	    $(".totalExpenseByBillSumrySel>.select-items>div").on("click",function(){
	    	getTotalExpenseSummaryByBill($(this).html().trim());
	    });
	    $(".payPdcSel>.select-items>div").on("click",function(){
	    	getPaymentPdc($(this).html().trim());
	    });
	    $(".payRecvPdcSel>.select-items>div").on("click",function(){
	    	getPaymentRecvPdc($(this).html().trim());
	    });
	    $(".totalAccPaySel>.select-items>div").on("click",function(){
	    	getAccountpayable($(this).html().trim());
	    });
	    $(".totalAccRecvSel>.select-items>div").on("click",function(){
	    	getAccountReceivable($(this).html().trim());
	    });
	    $(".accPaySel>.select-items>div").on("click",function(){
	    	getAccountPayableBySupplier($(this).html().trim());
	    });
	    $(".accRecvSel>.select-items>div").on("click",function(){
	    	getAccountReceivableByCustomer($(this).html().trim());
	    });
	    $(".supAgeSel>.select-items>div").on("click",function(){
	    	getSupplierAgeing($(this).html().trim(),0);
	    });    
	    $(".custAgeSel>.select-items>div").on("click",function(){
	    	getCustomerAgeing($(this).html().trim());
	    });    
	    $(".totalPurchaseByBillSelForMgt>.select-items>div").on("click",function(){
	    	getTotalPurchaseByBillForMgt($(this).html().trim());
	    });
	    $(".totalSalesByInvoiceSelForMgt>.select-items>div").on("click",function(){
	    	getTotalSalesByInvoiceForMgt($(this).html().trim());
	    });
	    $(".totalPurchaseByBillSumrySelForMgt>.select-items>div").on("click",function(){
	    	getTotalPurchaseSummaryByBillForMgt($(this).html().trim());
	    });
	    $(".totalSalesByInvoiceSumrySelForMgt>.select-items>div").on("click",function(){
	    	getTotalSalesSummaryByInvoiceForMgt($(this).html().trim());
	    });
	    $(".totalIncomeForMgtSel>.select-items>div").on("click",function(){
	    	getTotalIncomeForMgt($(this).html().trim());
	    });
	    $(".totalIncomeByInvoiceForMgtSumrySel>.select-items>div").on("click",function(){
	    	getTotalIncomeSummaryByInvoiceForMgt($(this).html().trim());
	    });
	    $(".totalExpenseForMgtSel>.select-items>div").on("click",function(){
	    	getTotalExpenseForMgt($(this).html().trim());
	    });
	    $(".totalExpenseByBillSumryForMgtSel>.select-items>div").on("click",function(){
	    	getTotalExpenseSummaryByBillForMgt($(this).html().trim());
	    });
	    $(".totalAccPaySelForMgt>.select-items>div").on("click",function(){
	    	getAccountpayableForMgt($(this).html().trim());
	    });
	    $(".accPaySelForMgt>.select-items>div").on("click",function(){
	    	getAccountPayableBySupplierForMgt($(this).html().trim());
	    });
	    $(".supAgeSelForMgt>.select-items>div").on("click",function(){
	    	getSupplierAgeingForMgt($(this).html().trim(),0);
	    });
	    $(".totalAccRecvForMgtSel>.select-items>div").on("click",function(){
	    	getAccountReceivableForMgt($(this).html().trim());
	    });
	    $(".accRecvSelForMgt>.select-items>div").on("click",function(){
	    	getAccountReceivableByCustomerForMgt($(this).html().trim());
	    });
	    $(".custAgeSelForMgt>.select-items>div").on("click",function(){
	    	getCustomerAgeingForMgt($(this).html().trim());
	    });
	    $(".payPdcSelForMgt>.select-items>div").on("click",function(){
	    	getPaymentPdcForMgt($(this).html().trim());
	    });
	    $(".payRecvPdcForMgtSel>.select-items>div").on("click",function(){
	    	getPaymentRecvPdcForMgt($(this).html().trim());
	    });
	    $(".totalAssetSel>.select-items>div").on("click",function(){
	    	getTotalAsset($(this).html().trim());
	    });
	    $(".totalLiabilitySel>.select-items>div").on("click",function(){
	    	getTotalLiability($(this).html().trim());
	    });
	    $(".netProfitSel>.select-items>div").on("click",function(){
	    	getNetProfit($(this).html().trim());
	    });
	    $(".grossProfitSel>.select-items>div").on("click",function(){
	    	getGrossProfit($(this).html().trim());
	    });
	    
		var ctx = document.getElementById('canvas').getContext('2d');
		window.purSumryChart = new Chart(ctx, {
			type: 'line',
			data: purSumryChartConfig,
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
							labelString: 'Amount'
						}
					}
				}
			}
		});
		
		var ctx2 = document.getElementById('canvas2').getContext('2d');
		window.topSuppchart = new Chart(ctx2, {
			type: 'horizontalBar',
			data: topSuppChartConfig,
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
					xAxes: [{
		                ticks: {
		                    display: false
		                }
		            }]
				}
			}
		});
		
		var ctx3 = document.getElementById('canvas3').getContext('2d');
		window.salesSumryChart = new Chart(ctx3, {
			type: 'line',
			data: salesSumryChartConfig,
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
							labelString: 'Amount'
						}
					}
				}
			}
		});
		
		var ctx4 = document.getElementById('canvas4').getContext('2d');
		window.topCustchart = new Chart(ctx4, {
			type: 'horizontalBar',
			data: topCustChartConfig,
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
					xAxes: [{
		                ticks: {
		                    display: false
		                }
		            }]
				}
			}
		});
		
		var ctx5 = document.getElementById('canvas5').getContext('2d');
		window.topSalPrdchart = new Chart(ctx5, {
			type: 'bar',
			data: topSalPrdChartConfig,
			options: {
				responsive: true,
				legend: {
			        display: true
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
							labelString: 'Amount'
						}
					}
				}
			}
		});
		
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
		
		
		window.griSumryChart = new Chart(ctx8, {
			type: 'line',
			data: griSumryChartConfig,
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
		

		window.purSumryByBillChart = new Chart(ctx9, {
			type: 'line',
			data: purSumryByBillChartConfig,
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
				}
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
						labelString: 'Amount'
					}
				}
			}
		});
		
		window.salesSumryByInvoiceChart = new Chart(ctx10, {
			type: 'line',
			data: salesSumryByInvoiceChartConfig,
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
				}
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
						labelString: 'Amount'
					}
				}
			}
		});
		
		var ctx11 = document.getElementById('canvas11').getContext('2d');
		window.totalIncomeChart = new Chart(ctx11, {
			type: 'pie',
			data: totalIncomeChartConfig,
			options: {
				responsive: true,
				maintainAspectRatio: false,
				legend: {
			        display: true,
			        position: "right"
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
				elements: {
					arc: {
						backgroundColor: colorize.bind(null, false, false),
						hoverBackgroundColor: hoverColorize
					}
				}
			}
		});
		window.totalIncomeChart.canvas.style.height="230px"
		
		var ctx12 = document.getElementById('canvas12').getContext('2d');
		window.totalExpenseChart = new Chart(ctx12, {
			type: 'pie',
			data: totalExpenseChartConfig,
			options: {
				responsive: true,
				maintainAspectRatio: false,
				legend: {
			        display: true,
			        position: "right"
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
				}
			}
		});
		window.totalExpenseChart.canvas.style.height="230px"
		
		var ctx13 = document.getElementById('canvas13').getContext('2d');
		window.totalIncomeByInvoiceChart = new Chart(ctx13, {
			type: 'bar',
			data: totalIncomeByInvoiceChartConfig,
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
				}
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
						labelString: 'Amount'
					}
				}
			}
		});
		
		var ctx14 = document.getElementById('canvas14').getContext('2d');
		window.totalExpenseByBillChart = new Chart(ctx14, {
			type: 'bar',
			data: totalExpenseByBillChartConfig,
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
				}
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
						labelString: 'Amount'
					}
				}
			}
		});
		
		var ctx15 = document.getElementById('canvas15').getContext('2d');
		window.totalAccPayChart = new Chart(ctx15, {
			type: 'doughnut',
			data: totalAccPayChartConfig,
			options: {
				responsive: true,
				maintainAspectRatio: false,
				circumference : 2 * Math.PI,
				rotation : 0.5 * Math.PI,
				legend: {
			        display: true,
			        position: "right"
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
				}
			}
		});
		window.totalAccPayChart.canvas.style.height="150px"
		
		var ctx16 = document.getElementById('canvas16').getContext('2d');
		window.totalAccRecvChart = new Chart(ctx16, {
			type: 'doughnut',
			data: totalAccRecvChartConfig,
			options: {
				responsive: true,
				maintainAspectRatio: false,
				circumference : 2 * Math.PI,
				rotation : 0.5 * Math.PI,
				legend: {
			        display: true,
			        position: "right"
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
				}
			}
		});
		window.totalAccRecvChart.canvas.style.height="150px"
		
		window.purSumryByBillForMgtChart = new Chart(ctx17, {
			type: 'line',
			data: purSumryByBillForMgtChartConfig,
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
				}
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
						labelString: 'Amount'
					}
				}
			}
		});
		
		window.salesSumryByInvoiceForMgtChart = new Chart(ctx18, {
			type: 'line',
			data: salesSumryByInvoiceForMgtChartConfig,
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
				}
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
						labelString: 'Amount'
					}
				}
			}
		});
		
		var ctx19 = document.getElementById('canvas19').getContext('2d');
		window.totalIncomeForMgtChart = new Chart(ctx19, {
			type: 'pie',
			data: totalIncomeForMgtChartConfig,
			options: {
				responsive: true,
				maintainAspectRatio: false,
				legend: {
			        display: true,
			        position: "right"
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
				elements: {
					arc: {
						backgroundColor: colorize.bind(null, false, false),
						hoverBackgroundColor: hoverColorize
					}
				}
			}
		});
		window.totalIncomeForMgtChart.canvas.style.height="230px"
			
		var ctx20 = document.getElementById('canvas20').getContext('2d');
		window.totalIncomeByInvoiceForMgtChart = new Chart(ctx20, {
			type: 'bar',
			data: totalIncomeByInvoiceForMgtChartConfig,
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
				}
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
						labelString: 'Amount'
					}
				}
			}
		});
		
		var ctx21 = document.getElementById('canvas21').getContext('2d');
		window.totalExpenseForMgtChart = new Chart(ctx21, {
			type: 'pie',
			data: totalExpenseForMgtChartConfig,
			options: {
				responsive: true,
				maintainAspectRatio: false,
				legend: {
			        display: true,
			        position: "right"
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
				}
			}
		});
		window.totalExpenseForMgtChart.canvas.style.height="230px"
			
		var ctx22 = document.getElementById('canvas22').getContext('2d');
		window.totalExpenseByForMgtBillChart = new Chart(ctx22, {
			type: 'bar',
			data: totalExpenseByBillForMgtChartConfig,
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
				}
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
						labelString: 'Amount'
					}
				}
			}
		});
		
		var ctx23 = document.getElementById('canvas23').getContext('2d');
		window.totalAccPayForMgtChart = new Chart(ctx23, {
			type: 'doughnut',
			data: totalAccPayForMgtChartConfig,
			options: {
				responsive: true,
				maintainAspectRatio: false,
				circumference : 2 * Math.PI,
				rotation : 0.5 * Math.PI,
				legend: {
			        display: true,
			        position: "right"
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
				}
			}
		});
		window.totalAccPayForMgtChart.canvas.style.height="150px"
			
		var ctx24 = document.getElementById('canvas24').getContext('2d');
		window.totalAccRecvForMgtChart = new Chart(ctx24, {
			type: 'doughnut',
			data: totalAccRecvForMgtChartConfig,
			options: {
				responsive: true,
				maintainAspectRatio: false,
				circumference : 2 * Math.PI,
				rotation : 0.5 * Math.PI,
				legend: {
			        display: true,
			        position: "right"
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
				}
			}
		});
		window.totalAccRecvChart.canvas.style.height="150px"
		
		calendar.render();
});
var ctx9 = document.getElementById('canvas9').getContext('2d');
const lineVerticalPattern = ctx9.createPattern(lineVertical(), 'repeat');

var ctx10 = document.getElementById('canvas10').getContext('2d');
const lineVerticalPattern2 = ctx10.createPattern(lineVertical2(), 'repeat');

var ctx7 = document.getElementById('canvas7').getContext('2d');
const lineVerticalPattern3 = ctx7.createPattern(lineVertical(), 'repeat');

var ctx8 = document.getElementById('canvas8').getContext('2d');
const lineVerticalPattern4 = ctx8.createPattern(lineVertical2(), 'repeat');

var ctx17 = document.getElementById('canvas17').getContext('2d');
const lineVerticalPattern5 = ctx17.createPattern(lineVertical(), 'repeat');

var ctx18 = document.getElementById('canvas18').getContext('2d');
const lineVerticalPattern6 = ctx18.createPattern(lineVertical2(), 'repeat');

var utils = Samples.utils;
utils.srand(110);

var purSumryChartConfig = {
	labels: '',
	datasets: [{
		label: document.form.baseCurrency.value,
		backgroundColor: 'rgba(238, 247, 254,0.4)',
		borderColor: 'rgba(23, 160, 134,1)',
		data: '',
		fill: true,
	}]
};

var topSuppChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(16, 96, 174,1)',
			borderColor: 'rgba(238, 247, 254,0.4)',
			borderWidth: 1,
			barPercentage: 0.5,
		}]
};

var salesSumryChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(238, 247, 254,0.4)',
			borderColor: 'rgba(100, 181, 247,1)',
			data: '',
			fill: true,
		}]
};

var topCustChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(16, 96, 174,1)',
			borderColor: 'rgba(238, 247, 254,0.4)',
			borderWidth: 1,
			barPercentage: 0.5,
		}]
};

var topSalPrdChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(240, 86, 61,1)',
			borderColor: 'rgba(240, 86, 61,0.4)',
			data: '',
			fill: true,
		},
		{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(23, 160, 134,1)',
			borderColor: 'rgba(23, 160, 134,0.4)',
			data: '',
			fill: true,
		},
		{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(41, 182, 246,1)',
			borderColor: 'rgba(41, 182, 246,0.4)',
			data: '',
			fill: true,
		},
		{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(168, 98, 38,1)',
			borderColor: 'rgba(168, 98, 38,0.4)',
			data: '',
			fill: true,
		},
		{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(16, 96, 174,1)',
			borderColor: 'rgba(238, 247, 254,0.4)',
			data: '',
			fill: true,
		}]
};

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

var griSumryChartConfig = {
		labels: '',
		datasets: [{
			label: '',
			backgroundColor: lineVerticalPattern4,
			borderColor: 'rgba(168, 98, 38,1)',
			data: '',
			fill: true,
		}]
};

var purSumryByBillChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: lineVerticalPattern,
			borderColor: 'rgba(58, 129, 216,1)',
		}]
};

var salesSumryByInvoiceChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: lineVerticalPattern2,
			borderColor: 'rgba(168, 98, 38,1)',
		}]
};

var totalIncomeChartConfig = {
		labels: '',
		datasets: [{
		}]
};

var totalExpenseChartConfig = {
		labels: '',
		datasets: [{
		}]
};

var totalAccPayChartConfig = {
		labels: '',
		datasets: [{
		}]
};

var totalAccRecvChartConfig = {
		labels: '',
		datasets: [{
		}]
};

var totalIncomeByInvoiceChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(16, 96, 174,1)',
			borderColor: 'rgba(238, 247, 254,0.4)',
			borderWidth: 1,
			barPercentage: 0.5,
		}]
};

var totalExpenseByBillChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(16, 96, 174,1)',
			borderColor: 'rgba(238, 247, 254,0.4)',
			borderWidth: 1,
			barPercentage: 0.5,
		}]
};

var purSumryByBillForMgtChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: lineVerticalPattern5,
			borderColor: 'rgba(58, 129, 216,1)',
		}]
};

var salesSumryByInvoiceForMgtChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: lineVerticalPattern6,
			borderColor: 'rgba(168, 98, 38,1)',
		}]
};

var totalIncomeForMgtChartConfig = {
		labels: '',
		datasets: [{
		}]
};

var totalIncomeByInvoiceForMgtChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(16, 96, 174,1)',
			borderColor: 'rgba(238, 247, 254,0.4)',
			borderWidth: 1,
			barPercentage: 0.5,
		}]
};

var totalExpenseForMgtChartConfig = {
		labels: '',
		datasets: [{
		}]
};

var totalExpenseByBillForMgtChartConfig = {
		labels: '',
		datasets: [{
			label: document.form.baseCurrency.value,
			backgroundColor: 'rgba(16, 96, 174,1)',
			borderColor: 'rgba(238, 247, 254,0.4)',
			borderWidth: 1,
			barPercentage: 0.5,
		}]
};

var totalAccPayForMgtChartConfig = {
		labels: '',
		datasets: [{
		}]
};

var totalAccRecvForMgtChartConfig = {
		labels: '',
		datasets: [{
		}]
};

function getTotals(period){
	getTotalReceipt(period);
	getTotalNumberReceipt(period);
	getPurchaseSummary(period);
	getTopSuppliers(period);
}

function getTotalReceipt(period){
	var dataURL = '../DashboardServlet?ACTION=TOTAL_RECEIPT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
	var dataURL = '../DashboardServlet?ACTION=TOTAL_RECEIPT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
	var dataURL = '../DashboardServlet?ACTION=LOW_STOCK_PRODUCTS';
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
	var dataURL = '../DashboardServlet?ACTION=NEW_SUPPLIERS';
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
	var dataURL = '../DashboardServlet?ACTION=TOTAL_SUPPLIERS';
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
	var dataURL = '../DashboardServlet?ACTION=TOP_SUPPLIERS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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
	var dataURL = '../DashboardServlet?ACTION=PURCHASE_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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
	var dataURL = '../DashboardServlet?ACTION=TOTAL_ISSUE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
	var dataURL = '../DashboardServlet?ACTION=TOTAL_ISSUE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
	var dataURL = '../DashboardServlet?ACTION=SALES_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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
	var dataURL = '../DashboardServlet?ACTION=TOP_CUSTOMERS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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
	var dataURL = '../DashboardServlet?ACTION=NEW_CUSTOMERS';
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
	var dataURL = '../DashboardServlet?ACTION=TOTAL_CUSTOMERS';
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
	var dataURL = '../DashboardServlet?ACTION=TOP_SALES_PRODUCT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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
	var dataURL = '../DashboardServlet?ACTION=TOTAL_RECEIPT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
	var dataURL = '../DashboardServlet?ACTION=TOTAL_ISSUE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
	var dataURL = '../DashboardServlet?ACTION=PO_WO_PRICE_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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
	var dataURL = '../DashboardServlet?ACTION=GRN_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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

function getGriSummary(period){
	var dataURL = '../DashboardServlet?ACTION=GRI_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			
			if(typeof data.orders[0].ISSUEDATE === 'undefined'){
        	}
        	else {
        		setGriSummaryCharData(data);
        	}
		}
   	});
}

function setGriSummaryCharData(data){
	var issuedate = data.orders.map(e=>e.ISSUEDATE);
    var total_issue_qty = data.orders.map(e=>e.TOTAL_ISSUE_QTY);
    
    griSumryChartConfig.labels = issuedate;
    for (var index = 0; index < griSumryChartConfig.datasets.length; ++index) {
    	griSumryChartConfig.datasets[index].data=total_issue_qty;
	}
    window.griSumryChart.update();
}

function getReadyToPackOrders(period){
	var dataURL = '../DashboardServlet?ACTION=GET_READY_TO_PACK_ORDERS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
	var dataURL = '../DashboardServlet?ACTION=EXPIRING_PRODUCTS&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period + ' (F)'))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period + ' (F)')));
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

function getTotalPurchaseByBill(period){
	var dataURL = '../DashboardServlet?ACTION=TOTAL_PURCHASE_BY_BILL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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

function getTotalSalesByInvoice(period){
	var dataURL = '../DashboardServlet?ACTION=TOTAL_SALES_BY_INVOICE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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

function getTotalPurchaseSummaryByBill(period){
	var dataURL = '../DashboardServlet?ACTION=TOTAL_PURCHASE_SUMMARY_BY_BILL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax( {
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {
			
			if(typeof data.bills[0].BILL_DATE === 'undefined'){
        	}
        	else {
        		setTotalPurchaseSummaryByBillCharData(data);
        	}
		}
   	});
}

function setTotalPurchaseSummaryByBillCharData(data){
	var bill_date = data.bills.map(e=>e.BILL_DATE);
    var total_cost = data.bills.map(e=>e.TOTAL_COST);
    
    purSumryByBillChartConfig.labels = bill_date;
    for (var index = 0; index < purSumryByBillChartConfig.datasets.length; ++index) {
    	purSumryByBillChartConfig.datasets[index].data=total_cost;
	}
    window.purSumryByBillChart.update();
}

function getTotalSalesSummaryByInvoice(period){
	var dataURL = '../DashboardServlet?ACTION=TOTAL_SALES_SUMMARY_BY_INVOICE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.invoices[0].INVOICE_DATE === 'undefined'){
        	}
        	else {
        		setTotalSalesSummaryByInvoiceCharData(data);
        	}
		}
   	});
}

function setTotalSalesSummaryByInvoiceCharData(data){
	var invoice_date = data.invoices.map(e=>e.INVOICE_DATE);
    var total_price = data.invoices.map(e=>e.TOTAL_PRICE);
    
    salesSumryByInvoiceChartConfig.labels = invoice_date;
    for (var index = 0; index < salesSumryByInvoiceChartConfig.datasets.length; ++index) {
    	salesSumryByInvoiceChartConfig.datasets[index].data=total_price;
	}
    window.salesSumryByInvoiceChart.update();
}

function getTotalIncome(period){
	var dataURL = '../DashboardServlet?ACTION=GET_TOTAL_INCOME&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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
	var dataURL = '../DashboardServlet?ACTION=GET_TOTAL_EXPENSE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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

function getTotalIncomeSummaryByInvoice(period){
	var dataURL = '../DashboardServlet?ACTION=GET_TOTAL_INCOME_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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

function getTotalExpenseSummaryByBill(period){
	var dataURL = '../DashboardServlet?ACTION=GET_TOTAL_EXPENSE_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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
	var dataURL = '../DashboardServlet?ACTION=GET_PAYMENT_PDC&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tablePaymentPdc){
		tablePaymentPdc.ajax.url( dataURL ).load();
	}else{
		tablePaymentPdc = $('#tablePaymentPdc').DataTable({
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
	var dataURL = '../DashboardServlet?ACTION=GET_PAYMENT_RECV_PDC&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tablePaymentRecvPdc){
		tablePaymentRecvPdc.ajax.url( dataURL ).load();
	}else{
		tablePaymentRecvPdc = $('#tablePaymentRecvPdc').DataTable({
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
	var dataURL = '../DashboardServlet?ACTION=GET_ACCOUNT_PAYABLE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.payments[0].TYPE === 'undefined'){
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
	var type = data.payments.map(e=>e.TYPE);
    var total_amount = data.payments.map(e=>e.AMOUNT);
    var backgroundColor = [];
    totalAccPayChartConfig.labels = type;
    
    for(var index = 0; index < data.payments.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalAccPayChartConfig.datasets.length; ++index) {
    	totalAccPayChartConfig.datasets[index].data = total_amount;
    	totalAccPayChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalAccPay").text(data.TOTAL_AMOUNT);
    window.totalAccPayChart.update();
}

function getAccountReceivable(period){
	var dataURL = '../DashboardServlet?ACTION=GET_ACCOUNT_RECEIVABLE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.payments[0].TYPE === 'undefined'){
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
	var type = data.payments.map(e=>e.TYPE);
    var total_amount = data.payments.map(e=>e.AMOUNT);
    var backgroundColor = [];
    totalAccRecvChartConfig.labels = type;
    
    for(var index = 0; index < data.payments.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalAccRecvChartConfig.datasets.length; ++index) {
    	totalAccRecvChartConfig.datasets[index].data = total_amount;
    	totalAccRecvChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalAccRecv").text(data.TOTAL_AMOUNT);
    window.totalAccRecvChart.update();
}

function getAccountPayableBySupplier(period){
	var dataURL = '../DashboardServlet?ACTION=GET_ACCOUNT_PAYABLE_BY_SUPPLIER&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableAccPay){
		tableAccPay.ajax.url( dataURL ).load();
	}else{
		tableAccPay = $('#tableAccPay').DataTable({
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
		        	if(typeof data.bills[0].SUPPLIER === 'undefined'){
		        		$("#accPay").text("");
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
		        		$("#accPay").text(data.TOTAL_AMOUNT);
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
	           /* {
	                "render": function ( data, type, row ) {
	                	return $("<div></div>", {
                            "class": "bar-chart-bar"
                        }).append(function(){
                            var bars = [];
                            for(var i = 2; i < Object.keys(row).length; i++){
                                bars.push($("<div ></div>",{
                                    "class": "bar " + "bar" + i,
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
	            },*/
	            { "visible": false,  "targets": [ 3 ] }
	        ]
		});
	}
}

function getAccountReceivableByCustomer(period){
	var dataURL = '../DashboardServlet?ACTION=GET_ACCOUNT_RECEIVABLE_BY_CUSTOMER&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableRecvPay){
		tableRecvPay.ajax.url( dataURL ).load();
	}else{
		tableRecvPay = $('#tableRecvPay').DataTable({
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
		        	if(typeof data.invoices[0].CUSTOMER === 'undefined'){
		        		$("#accRecv").text("");
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
		        		$("#accRecv").text(data.TOTAL_AMOUNT);
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
	            /*{
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
	            },*/
	            { "visible": false,  "targets": [ 3 ] }
	        ]
		});
	}
}

function getSupplierAgeing(period, loadCust){
	var dataURL = "/track/DashboardServlet?ACTION=PRINTAGEINGREPORT&Order=BILL&FROM_DATE=" + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + "&TO_DATE=" + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY") + "&LOADCUST="+loadCust;
	if (tableSupAge){
		tableSupAge.ajax.url( dataURL ).load();
	}else{
		tableSupAge = $('#tableSupAge').DataTable({
			"processing": true,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-5"i><"col-xs-7"p>>',
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
		        	if(typeof data.reportContent[0].currentdue === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'To_CompanyName', "orderable": false},{'data': 'amountdue', "orderable": false},{'data': 'v30daysdue', "orderable": false},{'data': 'v60daysdue', "orderable": false},{'data': 'v90daysdue', "orderable": false},{'data': 'v90plusdaysdue', "orderable": false}]
		});
	}
}

function getCustomerAgeing(period){
	var dataURL = "/track/DashboardServlet?ACTION=PRINTAGEINGREPORT&Order=INVOICE&FROM_DATE=" + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + "&TO_DATE=" + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY");
	if (tablecustAge){
		tablecustAge.ajax.url( dataURL ).load();
	}else{
		tablecustAge = $('#tablecustAge').DataTable({
			"processing": true,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-5"i><"col-xs-7"p>>',
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
		        	if(typeof data.reportContent[0].currentdue === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'To_CompanyName', "orderable": false},{'data': 'amountdue', "orderable": false},{'data': 'v30daysdue', "orderable": false},{'data': 'v60daysdue', "orderable": false},{'data': 'v90daysdue', "orderable": false},{'data': 'v90plusdaysdue', "orderable": false}]
		});
	}
}

function redirectToPurchaseSummary(){
	window.location.href='printSupplierPOInvoice.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".PurSumrySel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".PurSumrySel>.select-selected").html())), 'DD/MM/YYYY');
}
function redirectToSalesSummary(){
	window.location.href='OrderSummaryOutBoundWithPrice.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".saleSumrySel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".saleSumrySel>.select-selected").html())), 'DD/MM/YYYY');
}
function redirectToPurchaseWOPSummary(){
	window.location.href='OrderSummaryInbound.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".PoWopriceSel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".PoWopriceSel>.select-selected").html())), 'DD/MM/YYYY');
}
function redirectToGrnSummary(){
	window.location.href='grntobillSummary.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".grnSel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".grnSel>.select-selected").html())), 'DD/MM/YYYY');
}
function redirectToGiSummary(){
	window.location.href='ginotoinvoiceSummary.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".griSel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".griSel>.select-selected").html())), 'DD/MM/YYYY');
}
function redirectToPaymentPdcSummary(){
	window.open(
	  'pdcpaymentprocess.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".payPdcSel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".payPdcSel>.select-selected").html())), 'DD/MM/YYYY'),
	  '_blank'
	);
}
function redirectToPaymentReceivePdcSummary(){
	window.open(
	  'pdcpaymentReceiveprocess.jsp?FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod($(".payRecvPdcSel>.select-selected").html())), 'DD/MM/YYYY') + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod($(".payRecvPdcSel>.select-selected").html())), 'DD/MM/YYYY'),
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
	var dataURL = '../DashboardServlet?ACTION=TOTAL_PURCHASE_BY_BILL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
	var dataURL = '../DashboardServlet?ACTION=TOTAL_SALES_BY_INVOICE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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
	var dataURL = '../DashboardServlet?ACTION=TOTAL_PURCHASE_SUMMARY_BY_BILL&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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

function getTotalSalesSummaryByInvoiceForMgt(period){
	var dataURL = '../DashboardServlet?ACTION=TOTAL_SALES_SUMMARY_BY_INVOICE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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

function getTotalIncomeForMgt(period){
	var dataURL = '../DashboardServlet?ACTION=GET_TOTAL_INCOME&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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

function getTotalIncomeSummaryByInvoiceForMgt(period){
	var dataURL = '../DashboardServlet?ACTION=GET_TOTAL_INCOME_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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

function getTotalExpenseForMgt(period){
	var dataURL = '../DashboardServlet?ACTION=GET_TOTAL_EXPENSE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.bills[0].ACCOUNTDETAILTYPE === 'undefined'){
				totalExpenseForMgtChartConfig.labels = [];
				for (var index = 0; index < totalExpenseForMgtChartConfig.datasets.length; ++index) {
					totalExpenseForMgtChartConfig.datasets[index].data = [];
				}
				window.totalExpenseForMgtChart.update();
				$(".totalExpenseMsg").show();
				$("#totalExpenseForMgt").text("");
        	}
        	else {
        		setTotalExpenseForMgtCharData(data);
        		$(".totalExpenseMsg").hide();
        	}
		}
   	});
}

function setTotalExpenseForMgtCharData(data){
	var accountdetailtype = data.bills.map(e=>e.ACCOUNTDETAILTYPE);
    var total_amount = data.bills.map(e=>e.TOTAL_AMOUNT);
    var backgroundColor = [];
    totalExpenseForMgtChartConfig.labels = accountdetailtype;
    
    for(var index = 0; index < data.bills.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalExpenseForMgtChartConfig.datasets.length; ++index) {
    	totalExpenseForMgtChartConfig.datasets[index].data = total_amount;
    	totalExpenseForMgtChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalExpenseForMgt").text(data.TOTAL_AMOUNT);
    window.totalExpenseForMgtChart.update();
}

function getTotalExpenseSummaryByBillForMgt(period){
	var dataURL = '../DashboardServlet?ACTION=GET_TOTAL_EXPENSE_SUMMARY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
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
	var dataURL = '../DashboardServlet?ACTION=GET_ACCOUNT_PAYABLE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.payments[0].TYPE === 'undefined'){
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
	var type = data.payments.map(e=>e.TYPE);
    var total_amount = data.payments.map(e=>e.AMOUNT);
    var backgroundColor = [];
    totalAccPayForMgtChartConfig.labels = type;
    
    for(var index = 0; index < data.payments.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalAccPayForMgtChartConfig.datasets.length; ++index) {
    	totalAccPayForMgtChartConfig.datasets[index].data = total_amount;
    	totalAccPayForMgtChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalAccPayForMgt").text(data.TOTAL_AMOUNT);
    window.totalAccPayForMgtChart.update();
}

function getAccountPayableBySupplierForMgt(period){
	var dataURL = '../DashboardServlet?ACTION=GET_ACCOUNT_PAYABLE_BY_SUPPLIER&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableAccPayForMgt){
		tableAccPayForMgt.ajax.url( dataURL ).load();
	}else{
		tableAccPayForMgt = $('#tableAccPayForMgt').DataTable({
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
	            /*{
	                "render": function ( data, type, row ) {
	                	return $("<div></div>", {
                            "class": "bar-chart-bar"
                        }).append(function(){
                            var bars = [];
                            for(var i = 2; i < Object.keys(row).length; i++){
                                bars.push($("<div ></div>",{
                                    "class": "bar " + "bar" + i,
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
	            },*/
	            { "visible": false,  "targets": [ 3 ] }
	        ]
		});
	}
}

function getSupplierAgeingForMgt(period, loadCust){
	var dataURL = "/track/DashboardServlet?ACTION=PRINTAGEINGREPORT&Order=BILL&FROM_DATE=" + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + "&TO_DATE=" + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY") + "&LOADCUST="+loadCust;
	if (tableSupAgeForMgt){
		tableSupAgeForMgt.ajax.url( dataURL ).load();
	}else{
		tableSupAgeForMgt = $('#tableSupAgeForMgt').DataTable({
			"processing": true,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-5"i><"col-xs-7"p>>',
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
		        	if(typeof data.reportContent[0].currentdue === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'To_CompanyName', "orderable": false},{'data': 'amountdue', "orderable": false},{'data': 'v30daysdue', "orderable": false},{'data': 'v60daysdue', "orderable": false},{'data': 'v90daysdue', "orderable": false},{'data': 'v90plusdaysdue', "orderable": false}]
		});
	}
}

function getAccountReceivableForMgt(period){
	var dataURL = '../DashboardServlet?ACTION=GET_ACCOUNT_RECEIVABLE&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)))+ '&PERIOD='+ period;
	$.ajax({
		"type": "GET",
		"url": dataURL,
		"contentType": "application/json; charset=utf-8",
        "dataType": "json",
		success : function(data) {			
			if(typeof data.payments[0].TYPE === 'undefined'){
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
	var type = data.payments.map(e=>e.TYPE);
    var total_amount = data.payments.map(e=>e.AMOUNT);
    var backgroundColor = [];
    totalAccRecvForMgtChartConfig.labels = type;
    
    for(var index = 0; index < data.payments.length; ++index){
    	backgroundColor.push('#'+(Math.random() * 0xFFFFFF << 0).toString(16).padStart(6, '0'));
    }
    for (var index = 0; index < totalAccRecvForMgtChartConfig.datasets.length; ++index) {
    	totalAccRecvForMgtChartConfig.datasets[index].data = total_amount;
    	totalAccRecvForMgtChartConfig.datasets[index].backgroundColor = backgroundColor;
	}
    $("#totalAccRecvForMgt").text(data.TOTAL_AMOUNT);
    window.totalAccRecvForMgtChart.update();
}

function getAccountReceivableByCustomerForMgt(period){
	var dataURL = '../DashboardServlet?ACTION=GET_ACCOUNT_RECEIVABLE_BY_CUSTOMER&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tableRecvPayForMgt){
		tableRecvPayForMgt.ajax.url( dataURL ).load();
	}else{
		tableRecvPayForMgt = $('#tableRecvPayForMgt').DataTable({
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
	        	/*{
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
	            },*/
	            { "visible": false,  "targets": [ 3 ] }
	        ]
		});
	}
}

function getCustomerAgeingForMgt(period){
	var dataURL = "/track/DashboardServlet?ACTION=PRINTAGEINGREPORT&Order=INVOICE&FROM_DATE=" + getFormattedDate(moment(getFromDateForPeriod(period)),"DD/MM/YYYY") + "&TO_DATE=" + getFormattedDate(moment(getToDateForPeriod(period)),"DD/MM/YYYY");
	if (tablecustAgeForMgt){
		tablecustAgeForMgt.ajax.url( dataURL ).load();
	}else{
		tablecustAgeForMgt = $('#tablecustAgeForMgt').DataTable({
			"processing": true,
			"dom": '<"row"<"col-xs-6"l><"col-xs-6"f>> <"row"<"col-sm-12"rt>> <"row"<"col-xs-5"i><"col-xs-7"p>>',
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
		        	if(typeof data.reportContent[0].currentdue === 'undefined'){
		        		return [];
		        	}
		        	else {
		        		return data.reportContent;
		        	}
		        }
		    },
		    "pageLength": 5,
		    "columns": [{'data': 'To_CompanyName', "orderable": false},{'data': 'amountdue', "orderable": false},{'data': 'v30daysdue', "orderable": false},{'data': 'v60daysdue', "orderable": false},{'data': 'v90daysdue', "orderable": false},{'data': 'v90plusdaysdue', "orderable": false}]
		});
	}
}

function getPaymentPdcForMgt(period){
	var dataURL = '../DashboardServlet?ACTION=GET_PAYMENT_PDC&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tablePaymentPdcForMgt){
		tablePaymentPdcForMgt.ajax.url( dataURL ).load();
	}else{
		tablePaymentPdcForMgt = $('#tablePaymentPdcForMgt').DataTable({
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
	var dataURL = '../DashboardServlet?ACTION=GET_PAYMENT_RECV_PDC&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
	if (tablePaymentRecvPdcForMgt){
		tablePaymentRecvPdcForMgt.ajax.url( dataURL ).load();
	}else{
		tablePaymentRecvPdcForMgt = $('#tablePaymentRecvPdcForMgt').DataTable({
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
	var dataURL = '../DashboardServlet?ACTION=GET_TOTAL_ASSET&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"YYYY-MM-DD") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"YYYY-MM-DD");
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

function getTotalLiability(period){
	var dataURL = '../DashboardServlet?ACTION=GET_TOTAL_LIABILITY&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"YYYY-MM-DD") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"YYYY-MM-DD");
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

function getNetProfit(period){
	var dataURL = '../DashboardServlet?ACTION=GET_NET_PROFIT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period)),"YYYY-MM-DD") + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)),"YYYY-MM-DD");
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
	var dataURL = '../DashboardServlet?ACTION=GET_GROSS_PROFIT&FROM_DATE=' + getFormattedDate(moment(getFromDateForPeriod(period))) + '&TO_DATE=' + getFormattedDate(moment(getToDateForPeriod(period)));
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