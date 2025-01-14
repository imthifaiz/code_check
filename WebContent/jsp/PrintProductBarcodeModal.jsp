<div id="printProductBarcodeModal" class="modal fade" role="dialog">
	<div class="modal-dialog modal-md">
		<form name="printProductBarcodeForm" method="post">
			<div class="modal-content">
				<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">Generate Product Barcode</h4>
		      	</div>
		      	
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Product Barcode:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="ITEM_BARCODE" type="text" value="" readonly>
				        		<input type="hidden" name="LabelType" value="Single">
				        		<input type="hidden" name="BarcodeWidth" value="3">
				        		<input type="hidden" name="BarcodeHeight" value="60">
				        		<input type="hidden" name="FontSize" value="30">
				        		<input type="hidden" name="TextAlign" value="Center"> 
				        		<input type="hidden" name="TextPosition" value="Bottom"> 
				        		<input type="hidden" name="DisplayText" value="Show"> 
				        		<input type="hidden" name="PAGE_TYPE" value="PRDBARCODE">
				        		<input type="hidden" name="totrecqty" value="1"> 
				        		<input type="hidden" name="printdate" value=""> 
				        		<input type="hidden" name="CNAME" value=""> 
				        		
					   		</div>
			        	</div>
		      		</div>
				</div>
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Product Description:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="ITEM_DESC_BARCODE" type="text" value="" readonly> 
					   		</div>
			        	</div>
		      		</div>
				</div>
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Label Size:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="printtype" type="text" value="30X25" readonly> 
					   		</div>
			        	</div>
		      		</div>
				</div>
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">List Price:</label>
		      			<div class="col-sm-3">
			        		<div class="input-group">
				        		<input class="form-control" name="SELLING_PRICE_BARCODE" type="text" value="$" onchange="ViewPrtBarcode()" > 
					   		</div>
			        	</div>
		      			<div class="col-sm-3">
			        		<div class="input-group">
				        		<input class="form-control" name="UNITPRICE_BARCODE" type="text" value="0" onchange="ViewPrtBarcode()" > 
					   		</div>
			        	</div>
		      		</div>
				</div>
		      	<div class="modal-body">
					<div class="row form-group">		      		
		      			<label class="col-form-modal-label col-sm-4 required">Print Quantity:</label>
		      			<div class="col-sm-6">
			        		<div class="input-group">
				        		<input class="form-control" name="QUANTITY_BARCODE" type="text" onchange="ViewPrtBarcode()" value="1"> 
					   		</div>
			        	</div>
		      		</div>
				</div>
			
				<div class="modal-footer">
		  		    	<div class="form-group">  
			    		<button type="button" class="btn btn-success" onClick="printme();">Generate Barcode</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
			    </div>
			    <div class="modal-body">
			    <div class="row form-group"> <!-- Author: Azees  Create date: August 30,2021  Description: Print Barcode PDF -->
  	<div class="col-sm-12" id="doubleup" style="display: none">  	
  	</div>
  	</div>
  	</div>
  	<div class="modal-body">
  	<div class="row form-group"> <!-- Author: Azees  Create date: Sep 02,2021  Description: Print Barcode PDF -->
  	<div class="col-sm-12" id="singleup" style="display: none">
  	</div>
  	</div>
  	</div>
			</div>
			
		</form>
	</div>
</div>
<script>

var labelType =   [{
	    "year": "Single",
	    "value": "Single",
	    "tokens": [
	      "Single"
	    ]
	  },
			  {
				    "year": "Double",
				    "value": "Double",
				    "tokens": [
				      "Double"
				    ]
			  }];

var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    // an array that will be populated with substring matches
	    matches = [];
	    // regex used to determine if a string contains the substring `q`
	    substrRegex = new RegExp(q, 'i');
	    // iterate through the pool of strings and for any string that
	    // contains the substring `q`, add it to the `matches` array
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};

$(document).ready(function(){

	$(document).on("focusout","input[name ='LabelType']",function(){
			var LabelTypeval = $("input[name ='LabelType']").val();

		    var doubleElement = document.getElementById('doubleup');
		    var singleElement =document.getElementById('singleup');
			if(LabelTypeval=='Double') {
			    doubleElement.style.display='inline-block';
			    singleElement.style.display='none';
			    }
			    else {
			    doubleElement.style.display='none';
			    singleElement.style.display='inline-block';
			    }
		});

	/* To get the suggestion data for labelType */
	$("#LabelType").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'labelType',
	  display: 'value',  
	  source: substringMatcher(labelType),
	  limit: 9999,
	  templates: {
	  empty: [
		  '<div style="padding:3px 20px">',
			'No results found',
		  '</div>',
		].join('\n'),
		suggestion: function(data) {
		return '<p>' + data.value + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		  
	}).on('typeahead:open',function(event,selection){
		
	}).on('typeahead:close',function(){
		
	});

		});

function ViewPrtBarcode()
{
	$('#singleup').empty();
	$('#doubleup').empty();
	var loc = $("input[name=ITEM_BARCODE]").val();
	var locdesc = $("input[name=ITEM_DESC_BARCODE]").val(); 
	var cursyml = $("input[name=SELLING_PRICE_BARCODE]").val(); 
	var price = $("input[name=UNITPRICE_BARCODE]").val();
	var recqty = $("input[name=QUANTITY_BARCODE]").val();
	var totrecqty = 0;

	var DisplayText=$("input[name=DisplayText]").val();
	var TextPosition=$("input[name=TextPosition]").val();
	var TextAlign=$("input[name=TextAlign]").val();
	var FontSize=$("input[name=FontSize]").val();
	var PriceFontSize=parseFloat(FontSize)+10;
	var PlantFontSize=parseFloat(FontSize)-15;
	var BarcodeHeight=$("input[name=BarcodeHeight]").val();
	var BarcodeWidth=$("input[name=BarcodeWidth]").val();
	var LabelType=$("input[name=LabelType]").val();
	var cname =$("input[name=CNAME]").val();
	var printdate =$("input[name=printdate]").val();
	var cprice=cursyml+' '+price;
	var isDisplayText=false;
	if(DisplayText=='Show')
		isDisplayText=true;
		
		if(TextPosition=="Top")
	TextPosition="top";
	else if(TextPosition=="Bottom")
	TextPosition="bottom";
	
	if(TextAlign=="Left")
	TextAlign="left";
	else if(TextAlign=="Center")
	TextAlign="center";
	else if(TextAlign=="Right")
	TextAlign="right";
		
		var j = totrecqty;
		totrecqty=parseInt(totrecqty)+parseInt(recqty);		
		for (j= j; j < totrecqty; j++) {
	var bid =j;		
	var body="";
	var bodysingle="";

	body += '<div class="row divuprow ">';
	body += '<div class="col-sm-6">';
	body += '<input type="hidden" name="locdescprint'+j+'" value="'+locdesc+'">';
	body += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	body += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+loc+'">';
	body += '<input type="hidden" name="pricesyblvalue'+j+'"  class=pricesyblvalue'+j+' value="'+cursyml+'">';
	body += '<input type="hidden" name="pricevalue'+j+'"  class=pricevalue'+j+' value="'+price+'">';
	body += '<label class="lbluprow" id="lbluprow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	body += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
	body += '<label class="lbldnrow" id="lbldnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PriceFontSize+'px;">'+cprice+'</label>';
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;left: 50%;">'+printdate+'</label>';
	body += '<label class="lblcnrow" id="lblcnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cname+'</label>';
	body += '</div>';

	bodysingle += '<div class="row divuprow">';
	bodysingle += '<div class="col-sm-6">';
	
	bodysingle += '<input type="hidden" name="locdescprint'+j+'" value="'+locdesc+'">';
	bodysingle += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	bodysingle += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+loc+'">';
	bodysingle += '<input type="hidden" name="pricesyblvalue'+j+'"  class=pricesyblvalue'+j+' value="'+cursyml+'">';
	bodysingle += '<input type="hidden" name="pricevalue'+j+'"  class=pricevalue'+j+' value="'+price+'">';
	bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	bodysingle += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
	bodysingle += '<label class="lbldnrow" id="lbldnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PriceFontSize+'px;">'+cprice+'</label>';
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;left: 50%;">'+printdate+'</label>';
	bodysingle += '<label class="lblcnrow" id="lblcnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cname+'</label>';
	bodysingle += '</div>';
	bodysingle += '</div>';
	$("#singleup").append(bodysingle);
	
	j = j + 1;
    if (totrecqty > j)
    {   
	body += '<div class="col-sm-6">';
	body += '<input type="hidden" name="locdescprint'+j+'" value="'+locdesc+'">';
	body += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	body += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+loc+'">';
	body += '<input type="hidden" name="pricesyblvalue'+j+'"  class=pricesyblvalue'+j+' value="'+cursyml+'">';
	body += '<input type="hidden" name="pricevalue'+j+'"  class=pricevalue'+j+' value="'+price+'">';
	body += '<label class="lbluprow" id="lbluprow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	body += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
	body += '<label class="lbldnrow" id="lbldnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PriceFontSize+'px;">'+cprice+'</label>';
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;left: 50%;">'+printdate+'</label>';
	body += '<label class="lblcnrow" id="lblcnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cname+'</label>';
	body += '</div>';
	
	bodysingle="";
	bodysingle += '<div class="row divuprow">';
	bodysingle += '<div class="col-sm-6">';
	bodysingle += '<input type="hidden" name="locdescprint'+j+'" value="'+locdesc+'">';
	bodysingle += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	bodysingle += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+loc+'">';
	bodysingle += '<input type="hidden" name="pricesyblvalue'+j+'"  class=pricesyblvalue'+j+' value="'+cursyml+'">';
	bodysingle += '<input type="hidden" name="pricevalue'+j+'"  class=pricevalue'+j+' value="'+price+'">';
	bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	bodysingle += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
	bodysingle += '<label class="lbldnrow" id="lbldnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PriceFontSize+'px;">'+cprice+'</label>';
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;left: 50%;">'+printdate+'</label>';
	bodysingle += '<label class="lblcnrow" id="lblcnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cname+'</label>';
	bodysingle += '</div>';
	bodysingle += '</div>';
	$("#singleup").append(bodysingle);
	
    }
	body += '</div>';
	//console.log(icount);
	$("#doubleup").append(body);				 
	//alert(icount);
	if (totrecqty > j)
	JsBarcode("#barcode"+j, ""+ loc +"", {format: "CODE128", width:BarcodeWidth,  height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText} );
	JsBarcode("#barcode"+bid, ""+ loc +"", {format: "CODE128", width:BarcodeWidth,  height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText} );	
		}
		$("input[name=totrecqty]").val(totrecqty);
}
function printme() {

	var DisplayText=$("input[name=DisplayText]").val();
	var TextPosition=$("input[name=TextPosition]").val();
	var TextAlign=$("input[name=TextAlign]").val();
	var FontSize=$("input[name=FontSize]").val();
	var BarcodeHeight=$("input[name=BarcodeHeight]").val();
	var BarcodeWidth=$("input[name=BarcodeWidth]").val();
	var LabelType=$("input[name=LabelType]").val();
	var totrecqty=$("input[name=totrecqty]").val();
	var totcount=$("input[name=totcount]").val();
	var printtype=$("input[name=printtype]").val();
	var printdate=$("input[name=printdate]").val();
	
let doc = "";
if(printtype=="30X25")
doc = new jsPDF({
    orientation: 'landscape',
    unit: 'mm',
    format: [85, 80]
    });	  
else if(printtype=="50X25")
doc = new jsPDF({
    orientation: 'landscape',
    unit: 'mm',
    format: [300, 80]
    });
else
	  doc = new jsPDF({
	      orientation: 'landscape',
	      unit: 'mm',
	      format: [300, 160]
	      });
  var i=1;
  var lblsize=FontSize/3.5;
  var lblsizeprd=(parseInt(FontSize)+15)/3.5;
  var barWidth=BarcodeWidth*10;
  var barheight=BarcodeHeight/5;
  var Textheight=BarcodeHeight/2.3;
  if(Textheight>35)
  Textheight=Textheight-15;
$("#singleup").each(function(){
	if( totrecqty > 0 ){
	   	for (var j= 0; j < totrecqty; j++) {
  	   	
	   		var imgnw = $(this).find(".barcode"+j);
	        var len = imgnw.length;
	      //console.log('len :' + len);
	      //console.log('len1 :' + j);
	      var imgbchnw = $(this).find("#batch"+j);
		  var lblData =($("#lbluprow"+j).text());
		  var lblPriceData =($("#lbldnrow"+j).text());
		  var lblPlantData =($("#lblcnrow"+j).text());
		  var pData ="",qData ="";
	      //console.log('labelval :' + lblData);
	      
	      var imgData =($(imgnw).attr('src'));
	      //console.log('len2 :' + imgData);
	     	var img = new Image();
	        img.src = imgData || '';

	        var imgbchData =($(imgbchnw).attr('src'));
	        //console.log('len2 :' + imgbchData);
	       	var imgbch = new Image();
	          imgbch.src = imgbchData || '';
	          
  if(i>1)
  doc.addPage("", "landscape");
  doc.setFontSize(lblsize);
	  doc.setFont("monospace","bold");
	  doc.text(lblData, 0, 5);
      doc.addImage(img, "JPG", 0, 6, barWidth, barheight);
      	if(BarcodeHeight<9) {
	        	  Textheight=11;
	          } else {
	        	  Textheight=11+(parseInt(BarcodeHeight)/10);
	        	  if(Textheight>13)
	        		  Textheight=Textheight+((parseInt(BarcodeHeight)/10)-3);
		          }
	          	  //console.log('len2 :' + Textheight);
      	doc.text(qData, 3, Textheight);
          if(document.printProductBarcodeForm.PAGE_TYPE.value=="PRDBARCODE" || document.printProductBarcodeForm.PAGE_TYPE.value=="PRDBARCODESALES" || document.printProductBarcodeForm.PAGE_TYPE.value=="PRDBARCODEINV"){
			doc.setFontSize(lblsizeprd+1);
          Textheight=Textheight+2;
          doc.text(lblPriceData, 3, Textheight);
          doc.setFontSize(lblsize-2.5);
          Textheight=Textheight+2;
	        doc.text(printdate, 20, Textheight); 
          doc.setFontSize(lblsize-3.5);
          Textheight=Textheight+2;
	        doc.text(lblPlantData, 1, Textheight); 
	        }
  i++;        
	}
	}
});


setTimeout(function() {
	var printWindow = window.open(doc.output('bloburl'), '_blank');
printWindow.print();
if(document.printProductBarcodeForm.PAGE_TYPE.value=="PRDBARCODE") {
document.printProductBarcodeForm.action="/track/DynamicFileServlet?action=printProductBarcodeModal&Submit="+document.printProductBarcodeForm.printtype.value;
document.printProductBarcodeForm.submit();
} else if(document.printProductBarcodeForm.PAGE_TYPE.value=="PRDBARCODESALES") {
document.printProductBarcodeForm.action="/track/DynamicFileServlet?action=printProductBarcodeModalSales&Submit="+document.printProductBarcodeForm.printtype.value;
document.printProductBarcodeForm.submit();
}else if(document.printProductBarcodeForm.PAGE_TYPE.value=="PRDBARCODEINV") {
	document.printProductBarcodeForm.action="/track/DynamicFileServlet?action=printProductBarcodeModalINV&Submit="+document.printProductBarcodeForm.printtype.value;
	document.printProductBarcodeForm.submit();
	}
}, 1000); // Adjust the time as needed
}

</script>