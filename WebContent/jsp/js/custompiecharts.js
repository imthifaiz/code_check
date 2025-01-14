
	 $(document).ready(function(){
	 
	 $(document).on("click", "#sales-stat-tab li", function () {  	
    $("#sales-stat-tab li").each(function () {
        if ($("#sales-stat-tab li").hasClass('active'))
            $("#sales-stat-tab li").removeClass('active');			
    });	
	 $(this).addClass('active');
});
	 
	 
	 var fullDate = new Date()

 
//convert month to 2 digits
var twoDigitMonth = ((fullDate.getMonth().length+1) === 1)? (fullDate.getMonth()+1) : '0' + (fullDate.getMonth()+1);

 var currentDate = (fullDate.getDate()) + "/" + twoDigitMonth + "/" + fullDate.getFullYear();
 
var previousWeek = (fullDate.getDate()-7) + "/" + twoDigitMonth + "/" + fullDate.getFullYear();
	 
var previousMonth = fullDate.getDate() + "/" + (twoDigitMonth-1) + "/" + fullDate.getFullYear();

var previousYear = fullDate.getDate() + "/" + twoDigitMonth + "/" + (fullDate.getFullYear()-1);
	 	  $(document).on("click", "#week", function () {
	alert(previousWeek);
	  });
	  	  $(document).on("click", "#month", function () {
	alert(previousMonth);
	  });
	   	  $(document).on("click", "#year", function () {
	alert(previousYear);
	  });
	 

});

	 function callback(data){
		 
		 var errorBoo = false;
		 var errorMessage = "";
		 
		 $.each(data.errors, function(i,error){
				if(error.ERROR_CODE=="99"){
					errorBoo = true;
					errorMessage = error.ERROR_MESSAGE;
					
				}
				if(error.ERROR_CODE=="98"){
					errorBoo = true;
					errorMessage = error.ERROR_MESSAGE;
					
				}
			});
		 		
			var ii = 0;
			if(!errorBoo){
					
			var chart = new CanvasJS.Chart("chartContainer", {
				backgroundColor: "#f9f9f9",
				
			      axisX:{
			        title:'Products',
					
			      },
				  	  legend:{        
						horizontalAlign:"right",
						verticalAlign:"top",
						
					  },
			      axisY:{
			        title:'Quantity',
					gridThickness: 1
			      },       
					data: [              
					{
						// Change type to "doughnut", "line", "splineArea", etc.
						type: "area",
			           name: "users",
					   markerType: "circle",
					   legendText:"circle",
			          showInLegend: true,
					legendMarkerType: "square",
					  color:"#008000",
					  fillOpacity:.1,
						dataPoints: [
								$.each(data.items, function(i,item){
																
								       alert(item.PRODUCT_ID + " - "+ item.PRODUCT_RECEIVED_QTY);
									   ii++;
								    
								  }),
						            
						             
							{ label: x,  y: 50  },
							/*{ label: "Product2", y:  85 },
							{ label: "Product3", y: 100  },
							{ label: "Product4",  y: 80  },
							{ label: "Product5",  y: 90  },*/
			              	
						]
					},

			          
					]
				});
				chart.render();
			}
			else
				{
					alert(errorMessage);
				}
			}
			

window.onload = function () {
	
	
	var x = 100;
	
	var FROM_DATE      = "03/02/2015";
	   var TO_DATE        = "";
	   var DIRTYPE        = "";
	   var JOBNO          = "";
	   var ITEMNO         = "";
	   var ORDERNO        = "";
	   var ORDERTYPE      = "";
	   var SUPPLIER              = "";
	   var TO_ASSIGNEE           = "";
	   var LOANASSINEE           = "";
	   var BATCH        = "";
	   var LOC          = "";
	   var ITEMDESC       = "";
	  
	   var PRD_BRAND_ID      ="";
	   var PRD_TYPE_ID      = "";
	   var PRD_CLS_ID      = "";
	   var REASONCODE      = "";
	   var LOC_TYPE_ID         = "";
	   var FILTER         = "";
	
	
	
	
	var urlStr = "/NSeries/InboundOrderHandlerServlet";
	
	$.ajax({type: "POST",url: urlStr, data: { ITEM: ITEMNO,PRD_DESCRIP:ITEMDESC,FDATE:FROM_DATE,TDATE:TO_DATE,DTYPE:DIRTYPE,CNAME:SUPPLIER,ORDERNO:ORDERNO,JOBNO:JOBNO,ORDERTYPE:ORDERTYPE,PRD_BRAND_ID:PRD_BRAND_ID,PRD_TYPE_ID:PRD_TYPE_ID,PRD_CLS_ID:PRD_CLS_ID,LOC_TYPE_ID:LOC_TYPE_ID,TOASSINEE:TO_ASSIGNEE,LOANASSINEE:LOANASSINEE,REASONCODE:REASONCODE,BATCH:BATCH,LOC:LOC,FILTER:FILTER,ACTION: "DASHBOARD_VIEW_GOODS_RECIPT_SMRY",PLANT:"<%=plant%>",LOGIN_USER:"<%=userID%>"},dataType: "json", success: callback });
}
	