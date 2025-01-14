var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

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

 var textAlign =   [{
	    "year": "Center",
	    "value": "Center",
	    "tokens": [
	      "Center"
	    ]
	  },
			  {
				    "year": "Left",
				    "value": "Left",
				    "tokens": [
				      "Left"
				    ]
			  },
			  {
				    "year": "Right",
				    "value": "Right",
				    "tokens": [
				      "Right"
				    ]
			  }];

 var textPosition =   [{
	    "year": "Top",
	    "value": "Top",
	    "tokens": [
	      "Top"
	    ]
	  },
			  {
				    "year": "Bottom",
				    "value": "Bottom",
				    "tokens": [
				      "Bottom"
				    ]
			  }];

 var displayText =   [{
	    "year": "Hide",
	    "value": "Hide",
	    "tokens": [
	      "Hide"
	    ]
	  },
			  {
				    "year": "Show",
				    "value": "Show",
				    "tokens": [
				      "Show"
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

	$(document).on("focusout","input[name ='TextAlign']",function(){
		newBarcode();
	});

	/* To get the suggestion data for Status */
	$("#TextAlign").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'textAlign',
	  display: 'value',  
	  source: substringMatcher(textAlign),
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

	$(document).on("focusout","input[name ='TextPosition']",function(){
		newBarcode();
	});

	/* To get the suggestion data for Status */
	$("#TextPosition").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'textPosition',
	  display: 'value',  
	  source: substringMatcher(textPosition),
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

	$(document).on("focusout","input[name ='DisplayText']",function(){
		newBarcode();
	});

	/* To get the suggestion data for Status */
	$("#DisplayText").typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  name: 'displayText',
	  display: 'value',  
	  source: substringMatcher(displayText),
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

	$(document).on("focusout","input[name ='BarcodeWidth']",function(){
		newBarcode();
	});

	$(document).on("focusout","input[name ='BarcodeHeight']",function(){
		newBarcode();
	});

	$(document).on("focusout","input[name ='FontSize']",function(){
		var FontSize=$("input[name=FontSize]").val();
		$(".lbluprow").css("font-size", FontSize+"px");
		$(".lbldnrow").css("font-size", FontSize+"px");
		$(".lblcnrow").css("font-size", FontSize+"px");
		$(".lblporow").css("font-size", FontSize+"px");
		$(".lblqtyrow").css("font-size", FontSize+"px");
		newBarcode();
	});
	
});

function ViewBarcode(icount,orderPrefix,generalDate,nextSeqno,str,totrecqty)
{
	var chkdata = str.split(",");
	var grno = chkdata[0];				
	var item = chkdata[1];
	var lnno = chkdata[2];
	var loc = chkdata[3];
	var batch = chkdata[4];
	var recqty = chkdata[5];
	var uom = chkdata[6];
	var pono = chkdata[7];
	var description = chkdata[8];
	var barcode=orderPrefix+generalDate;
	var DisplayText=$("input[name=DisplayText]").val();
	var TextPosition=$("input[name=TextPosition]").val();
	var TextAlign=$("input[name=TextAlign]").val();
	var FontSize=$("input[name=FontSize]").val();
	var BarcodeHeight=$("input[name=BarcodeHeight]").val();
	var BarcodeWidth=$("input[name=BarcodeWidth]").val();
	var LabelType=$("input[name=LabelType]").val();
	var isDisplayText=false;
	if(DisplayText=='Show')
		isDisplayText=true;
	var batchBarcodeWidth=parseInt(BarcodeWidth)/2;
	
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
	
	if(textPosition=="Top")
	textPosition="top";
	else if(textPosition=="Bottom")
	textPosition="bottom";
	
	//console.log(FontSize);
	//console.log(DisplayText);
	//if(description.length()>34)
		//description=description.substring(0, 33);
		if (document.form1.printwithlot.value=="") {
		var j = totrecqty;
		totrecqty=parseInt(totrecqty)+parseInt(recqty);		
		for (j= j; j < totrecqty; j++) {
	var bid =j;		
	var body="";
	var bodysingle="";
	var seqval=parseInt(nextSeqno)+parseInt(j+1);
	var opcount=pad(seqval,7);
	var seqval1=seqval;
	var opcount1= opcount;
	body += '<div class="row divuprow ">';
	body += '<div class="col-sm-6">';
	body += '<input type="hidden" name="itemprint'+j+'" value="'+item+'">';
	body += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	body += '<input type="hidden" name="batchprint'+j+'" value="'+batch+'">';
	body += '<input type="hidden" name="grnoprint'+j+'" value="'+grno+'">';
	body += '<input type="hidden" name="lnno'+j+'" value="'+lnno+'">';
	body += '<input type="hidden" name="uom'+j+'" value="'+uom+'">';
	body += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+(barcode+opcount1)+'">';
	if (document.form1.printwithpobill.value!=""){
	body += '<label class="lblporow"  id="lblporow'+j+'"style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
	}
	body += '<label class="lbluprow"  id="lbluprow'+j+'"style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
	body += '<img style="display: block;" alt="" src="'+(barcode+opcount)+'" class=barcode'+j+' id=barcode'+j+'>';
	if (document.form1.printwithbatch.value!="") {
	body += '<input type="hidden" name="batchvalue'+icount+'"  class=batchvalue'+icount+' value="'+(batch)+'">';
	body += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+j+'>';
	}
	body += '</div>';

	bodysingle += '<div class="row divuprow">';
	bodysingle += '<div class="col-sm-6">';
	if (document.form1.printwithpobill.value!=""){
		bodysingle += '<label class="lblporow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
	}
	bodysingle += '<input type="hidden" name="itemprint'+j+'" value="'+item+'">';
	bodysingle += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	bodysingle += '<input type="hidden" name="batchprint'+j+'" value="'+batch+'">';
	bodysingle += '<input type="hidden" name="grnoprint'+j+'" value="'+grno+'">';
	bodysingle += '<input type="hidden" name="lnno'+j+'" value="'+lnno+'">';
	bodysingle += '<input type="hidden" name="uom'+j+'" value="'+uom+'">';
	bodysingle += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+(barcode+opcount)+'">';
	bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
	bodysingle += '<img style="display: block;" alt="" src="'+(barcode+opcount)+'" class=barcode'+j+' id=barcode'+j+'>';
	if (document.form1.printwithbatch.value!="") {
		bodysingle += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+j+'>';
	}
	bodysingle += '</div>';
	bodysingle += '</div>';
	$("#singleup").append(bodysingle);
	
	j = j + 1;
    if (totrecqty > j)
    {
    seqval1=parseInt(nextSeqno)+parseInt(j+1);
    opcount1=pad(seqval1,7);
	body += '<div class="col-sm-6">';
	if (document.form1.printwithpobill.value!=""){
		body += '<label class="lblporow" id="lblporow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
	}
	body += '<input type="hidden" name="itemprint'+j+'" value="'+item+'">';
	body += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	body += '<input type="hidden" name="batchprint'+j+'" value="'+batch+'">';
	body += '<input type="hidden" name="grnoprint'+j+'" value="'+grno+'">';
	body += '<input type="hidden" name="lnno'+j+'" value="'+lnno+'">';
	body += '<input type="hidden" name="uom'+j+'" value="'+uom+'">';
	body += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+(barcode+opcount1)+'">';
	body += '<label class="lbluprow" id="lbluprow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
	body += '<img style="display: block;" alt="" src="'+(barcode+opcount1)+'" class=barcode'+j+' id=barcode'+j+'>';
	if (document.form1.printwithbatch.value!="") {
	body += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+j+'>';
	}
	body += '</div>';
	
	bodysingle="";
	bodysingle += '<div class="row divuprow">';
	bodysingle += '<div class="col-sm-6">';
	if (document.form1.printwithpobill.value!=""){
		bodysingle += '<label class="lblporow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
	}
	bodysingle += '<input type="hidden" name="itemprint'+j+'" value="'+item+'">';
	bodysingle += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	bodysingle += '<input type="hidden" name="batchprint'+j+'" value="'+batch+'">';
	bodysingle += '<input type="hidden" name="grnoprint'+j+'" value="'+grno+'">';
	bodysingle += '<input type="hidden" name="lnno'+j+'" value="'+lnno+'">';
	bodysingle += '<input type="hidden" name="uom'+j+'" value="'+uom+'">';
	bodysingle += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+(barcode+opcount1)+'">';
	bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
	bodysingle += '<img style="display: block;" alt="" src="'+(barcode+opcount1)+'" class=barcode'+j+' id=barcode'+j+'>';
	if (document.form1.printwithbatch.value!="") {
		bodysingle += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+j+'>';
	}
	bodysingle += '</div>';
	bodysingle += '</div>';
	$("#singleup").append(bodysingle);
	
    }
	body += '</div>';
	//console.log(icount);
	$("#doubleup").append(body);				 
	//alert(icount);
	if (totrecqty > j)
	JsBarcode("#barcode"+j, ""+ (barcode+opcount1) +"", {format: "CODE128", width:BarcodeWidth,  height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText} );
	JsBarcode("#barcode"+bid, ""+ (barcode+opcount) +"", {format: "CODE128", width:BarcodeWidth,  height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText} );
	if (document.form1.printwithbatch.value!="") {
		JsBarcode(".batch"+icount, ""+ (batch) +"", {format: "CODE128", width:batchBarcodeWidth, height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText});
	}
		}
		}
		else{
			
			totrecqty=parseInt(totrecqty)+parseInt(icount+1);
			var body="";
			var bodysingle="";
			var seqval=parseInt(nextSeqno)+parseInt(icount+1);
			var opcount=pad(seqval,7);
			body += '<div class="row divuprow ">';
			body += '<div class="col-sm-6">';
			body += '<input type="hidden" name="itemprint'+icount+'" value="'+item+'">';
			body += '<input type="hidden" name="locprint'+icount+'" value="'+loc+'">';
			body += '<input type="hidden" name="batchprint'+icount+'" value="'+batch+'">';
			body += '<input type="hidden" name="grnoprint'+icount+'" value="'+grno+'">';
			body += '<input type="hidden" name="lnno'+icount+'" value="'+lnno+'">';
			body += '<input type="hidden" name="uom'+icount+'" value="'+uom+'">';
			body += '<input type="hidden" name="recqtyprint'+icount+'" value="'+recqty+'">';
			body += '<input type="hidden" name="barcodevalue'+icount+'"  class=barcodevalue'+icount+' value="'+(barcode+opcount)+'">';
			if (document.form1.printwithpobill.value!=""){
			body += '<label class="lblporow" id="lblporow'+icount+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
			}
			body += '<label class="lbluprow" id="lbluprow'+icount+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
			body += '<img style="display: block;" alt="" src="'+(barcode+opcount)+'" class=barcode'+icount+' id=barcode'+icount+'>';
			if (document.form1.printwithbatch.value!="") {
			body += '<input type="hidden" name="batchvalue'+icount+'"  class=batchvalue'+icount+' value="'+(batch)+'">';
			body += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+icount+'>';
			}
			body += '<label class="lblqtyrow" id="lblqtyrow'+icount+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">QTY.: '+recqty+'</label>';
			body += '</div>';

			body += '<div class="col-sm-6">';
			body += '<input type="hidden" name="itemprint'+icount+'" value="'+item+'">';
			body += '<input type="hidden" name="locprint'+icount+'" value="'+loc+'">';
			body += '<input type="hidden" name="batchprint'+icount+'" value="'+batch+'">';
			body += '<input type="hidden" name="grnoprint'+icount+'" value="'+grno+'">';
			body += '<input type="hidden" name="lnno'+icount+'" value="'+lnno+'">';
			body += '<input type="hidden" name="uom'+icount+'" value="'+uom+'">';
			body += '<input type="hidden" name="recqtyprint'+icount+'" value="'+recqty+'">';
			body += '<input type="hidden" name="barcodevalue'+icount+'"  class=barcodevalue'+icount+' value="'+(barcode+opcount)+'">';
			if (document.form1.printwithpobill.value!=""){
			body += '<label class="lblporow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
			}
			body += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
			body += '<img style="display: block;" alt="" src="'+(barcode+opcount)+'" class=barcode'+icount+' id=barcode'+icount+'>';
			if (document.form1.printwithbatch.value!="") {
			body += '<input type="hidden" name="batchvalue'+icount+'"  class=batchvalue'+icount+' value="'+(batch)+'">';
			body += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+icount+'>';
			}
			body += '<label class="lblqtyrow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">QTY.: '+recqty+'</label>';
			body += '</div>';
			
			body += '</div>';
			$("#doubleup").append(body);

			bodysingle += '<div class="row divuprow">';
			bodysingle += '<div class="col-sm-6">';
			if (document.form1.printwithpobill.value!=""){
				bodysingle += '<label class="lblporow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
			}
			bodysingle += '<input type="hidden" name="itemprint'+icount+'" value="'+item+'">';
			bodysingle += '<input type="hidden" name="locprint'+icount+'" value="'+loc+'">';
			bodysingle += '<input type="hidden" name="batchprint'+icount+'" value="'+batch+'">';
			bodysingle += '<input type="hidden" name="grnoprint'+icount+'" value="'+grno+'">';
			bodysingle += '<input type="hidden" name="lnno'+icount+'" value="'+lnno+'">';
			bodysingle += '<input type="hidden" name="uom'+icount+'" value="'+uom+'">';
			bodysingle += '<input type="hidden" name="recqtyprint'+icount+'" value="'+recqty+'">';
			bodysingle += '<input type="hidden" name="barcodevalue'+icount+'"  class=barcodevalue'+icount+' value="'+(barcode+opcount)+'">';
			bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
			bodysingle += '<img style="display: block;" alt="" src="'+(barcode+opcount)+'" class=barcode'+icount+' id=barcode'+icount+'>';
			if (document.form1.printwithbatch.value!="") {
				bodysingle += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+icount+'>';
			}
			bodysingle += '<label class="lblqtyrow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">QTY.: '+recqty+'</label>';
			bodysingle += '</div>';
			bodysingle += '</div>';
			$("#singleup").append(bodysingle);

			JsBarcode("#barcode"+icount, ""+ (barcode+opcount) +"", {format: "CODE128", width:BarcodeWidth,  height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText} );
			if (document.form1.printwithbatch.value!="") {
				JsBarcode(".batch"+icount, ""+ (batch) +"", {format: "CODE128", width:batchBarcodeWidth, height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText});
			}
			
			}
}

function pad(number, length) {
    var negative = number < 0;
    var str = '' + Math.abs(number);
    while (str.length < length) {
        str = '0' + str;
    }
    if(negative) str = '-' + str;
 return str;
}

function ViewProductBarcode(icount,str,totrecqty)
{
	var chkdata = str.split(",");
	var grno = chkdata[0];				
	var item = chkdata[1];
	var lnno = chkdata[2];
	var loc = chkdata[3];
	var batch = chkdata[4];
	var recqty = chkdata[5];
	var uom = chkdata[6];
	var pono = chkdata[7];
	var description = chkdata[8];
	
	var DisplayText=$("input[name=DisplayText]").val();
	var TextPosition=$("input[name=TextPosition]").val();
	var TextAlign=$("input[name=TextAlign]").val();
	var FontSize=$("input[name=FontSize]").val();
	var BarcodeHeight=$("input[name=BarcodeHeight]").val();
	var BarcodeWidth=$("input[name=BarcodeWidth]").val();
	var LabelType=$("input[name=LabelType]").val();
	var isDisplayText=false;
	if(DisplayText=='Show')
		isDisplayText=true;
	var batchBarcodeWidth=parseInt(BarcodeWidth)/2;
	
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

	if (document.form1.printwithlot.value=="") {
		var j = totrecqty;
		totrecqty=parseInt(totrecqty)+parseInt(recqty);		
		for (j= j; j < totrecqty; j++) {
	var bid =j;		
	var body="";
	var bodysingle="";

	body += '<div class="row divuprow ">';
	body += '<div class="col-sm-6">';
	body += '<input type="hidden" name="itemprint'+j+'" value="'+item+'">';
	body += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	body += '<input type="hidden" name="batchprint'+j+'" value="'+batch+'">';
	body += '<input type="hidden" name="grnoprint'+j+'" value="'+grno+'">';
	body += '<input type="hidden" name="lnno'+j+'" value="'+lnno+'">';
	body += '<input type="hidden" name="uom'+j+'" value="'+uom+'">';
	body += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+item+'">';
	if (document.form1.printwithpobill.value!=""){
	body += '<label class="lblporow" id="lblporow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
	}
	body += '<label class="lbluprow" id="lbluprow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
	body += '<img style="display: block;" alt="" src="'+item+'" class=barcode'+j+' id=barcode'+j+'>';
	if (document.form1.printwithbatch.value!="") {
	body += '<input type="hidden" name="batchvalue'+icount+'"  class=batchvalue'+icount+' value="'+(batch)+'">';
	body += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+j+'>';
	}
	body += '</div>';

	bodysingle += '<div class="row divuprow">';
	bodysingle += '<div class="col-sm-6">';
	if (document.form1.printwithpobill.value!=""){
		bodysingle += '<label class="lblporow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
	}
	bodysingle += '<input type="hidden" name="itemprint'+j+'" value="'+item+'">';
	bodysingle += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	bodysingle += '<input type="hidden" name="batchprint'+j+'" value="'+batch+'">';
	bodysingle += '<input type="hidden" name="grnoprint'+j+'" value="'+grno+'">';
	bodysingle += '<input type="hidden" name="lnno'+j+'" value="'+lnno+'">';
	bodysingle += '<input type="hidden" name="uom'+j+'" value="'+uom+'">';
	bodysingle += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+item+'">';
	bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
	bodysingle += '<img style="display: block;" alt="" src="'+item+'" class=barcode'+j+' id=barcode'+j+'>';
	if (document.form1.printwithbatch.value!="") {
		bodysingle += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+j+'>';
	}
	bodysingle += '</div>';
	bodysingle += '</div>';
	$("#singleup").append(bodysingle);
	
	j = j + 1;
    if (totrecqty > j)
    {   
	body += '<div class="col-sm-6">';
	if (document.form1.printwithpobill.value!=""){
		body += '<label class="lblporow" id="lblporow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
	}
	body += '<input type="hidden" name="itemprint'+j+'" value="'+item+'">';
	body += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	body += '<input type="hidden" name="batchprint'+j+'" value="'+batch+'">';
	body += '<input type="hidden" name="grnoprint'+j+'" value="'+grno+'">';
	body += '<input type="hidden" name="lnno'+j+'" value="'+lnno+'">';
	body += '<input type="hidden" name="uom'+j+'" value="'+uom+'">';
	body += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+item+'">';
	body += '<label class="lbluprow" id="lbluprow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
	body += '<img style="display: block;" alt="" src="'+item+'" class=barcode'+j+' id=barcode'+j+'>';
	if (document.form1.printwithbatch.value!="") {
	body += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+j+'>';
	}
	body += '</div>';
	
	bodysingle="";
	bodysingle += '<div class="row divuprow">';
	bodysingle += '<div class="col-sm-6">';
	if (document.form1.printwithpobill.value!=""){
		bodysingle += '<label class="lblporow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
	}
	bodysingle += '<input type="hidden" name="itemprint'+j+'" value="'+item+'">';
	bodysingle += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	bodysingle += '<input type="hidden" name="batchprint'+j+'" value="'+batch+'">';
	bodysingle += '<input type="hidden" name="grnoprint'+j+'" value="'+grno+'">';
	bodysingle += '<input type="hidden" name="lnno'+j+'" value="'+lnno+'">';
	bodysingle += '<input type="hidden" name="uom'+j+'" value="'+uom+'">';
	bodysingle += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+item+'">';
	bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
	bodysingle += '<img style="display: block;" alt="" src="'+item+'" class=barcode'+j+' id=barcode'+j+'>';
	if (document.form1.printwithbatch.value!="") {
		bodysingle += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+j+'>';
	}
	bodysingle += '</div>';
	bodysingle += '</div>';
	$("#singleup").append(bodysingle);
	
    }
	body += '</div>';
	//console.log(icount);
	$("#doubleup").append(body);				 
	//alert(icount);
	if (totrecqty > j)
	JsBarcode("#barcode"+j, ""+ item +"", {format: "CODE128", width:BarcodeWidth,  height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText} );
	JsBarcode("#barcode"+bid, ""+ item +"", {format: "CODE128", width:BarcodeWidth,  height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText} );
	if (document.form1.printwithbatch.value!="") {
		JsBarcode(".batch"+icount, ""+ (batch) +"", {format: "CODE128", width:batchBarcodeWidth, height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText});
	}
		}		
		}
		else {
			totrecqty=parseInt(totrecqty)+parseInt(icount+1);
			var body="";
			var bodysingle="";

			body += '<div class="row divuprow ">';
			body += '<div class="col-sm-6">';
			body += '<input type="hidden" name="itemprint'+icount+'" value="'+item+'">';
			body += '<input type="hidden" name="locprint'+icount+'" value="'+loc+'">';
			body += '<input type="hidden" name="batchprint'+icount+'" value="'+batch+'">';
			body += '<input type="hidden" name="grnoprint'+icount+'" value="'+grno+'">';
			body += '<input type="hidden" name="lnno'+icount+'" value="'+lnno+'">';
			body += '<input type="hidden" name="uom'+icount+'" value="'+uom+'">';
			body += '<input type="hidden" name="recqtyprint'+icount+'" value="'+recqty+'">';
			body += '<input type="hidden" name="barcodevalue'+icount+'"  class=barcodevalue'+icount+' value="'+item+'">';
			if (document.form1.printwithpobill.value!=""){
			body += '<label class="lblporow" id="lblporow'+icount+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
			}
			body += '<label class="lbluprow" id="lbluprow'+icount+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
			body += '<img style="display: block;" alt="" src="'+item+'" class=barcode'+icount+' id=barcode'+icount+'>';
			if (document.form1.printwithbatch.value!="") {
			body += '<input type="hidden" name="batchvalue'+icount+'"  class=batchvalue'+icount+' value="'+(batch)+'">';
			body += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+icount+'>';
			}
			body += '<label class="lblqtyrow" id="lblqtyrow'+icount+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">QTY.: '+recqty+'</label>';
			body += '</div>';

			body += '<div class="col-sm-6">';
			body += '<input type="hidden" name="itemprint'+icount+'" value="'+item+'">';
			body += '<input type="hidden" name="locprint'+icount+'" value="'+loc+'">';
			body += '<input type="hidden" name="batchprint'+icount+'" value="'+batch+'">';
			body += '<input type="hidden" name="grnoprint'+icount+'" value="'+grno+'">';
			body += '<input type="hidden" name="lnno'+icount+'" value="'+lnno+'">';
			body += '<input type="hidden" name="uom'+icount+'" value="'+uom+'">';
			body += '<input type="hidden" name="recqtyprint'+icount+'" value="'+recqty+'">';
			body += '<input type="hidden" name="barcodevalue'+icount+'"  class=barcodevalue'+icount+' value="'+item+'">';
			if (document.form1.printwithpobill.value!=""){
			body += '<label class="lblporow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
			}
			body += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
			body += '<img style="display: block;" alt="" src="'+item+'" class=barcode'+icount+' id=barcode'+icount+'>';
			if (document.form1.printwithbatch.value!="") {
			body += '<input type="hidden" name="batchvalue'+icount+'"  class=batchvalue'+icount+' value="'+(batch)+'">';
			body += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+icount+'>';
			}
			body += '<label class="lblqtyrow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">QTY.: '+recqty+'</label>';
			body += '</div>';
			
			body += '</div>';
			$("#doubleup").append(body);

			bodysingle += '<div class="row divuprow">';
			bodysingle += '<div class="col-sm-6">';
			if (document.form1.printwithpobill.value!=""){
				bodysingle += '<label class="lblporow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+pono+'</label>';
			}
			bodysingle += '<input type="hidden" name="itemprint'+icount+'" value="'+item+'">';
			bodysingle += '<input type="hidden" name="locprint'+icount+'" value="'+loc+'">';
			bodysingle += '<input type="hidden" name="batchprint'+icount+'" value="'+batch+'">';
			bodysingle += '<input type="hidden" name="grnoprint'+icount+'" value="'+grno+'">';
			bodysingle += '<input type="hidden" name="lnno'+icount+'" value="'+lnno+'">';
			bodysingle += '<input type="hidden" name="uom'+icount+'" value="'+uom+'">';
			bodysingle += '<input type="hidden" name="recqtyprint'+icount+'" value="'+recqty+'">';
			bodysingle += '<input type="hidden" name="barcodevalue'+icount+'"  class=barcodevalue'+icount+' value="'+item+'">';
			bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+description+'</label>';
			bodysingle += '<img style="display: block;" alt="" src="'+item+'" class=barcode'+icount+' id=barcode'+icount+'>';
			if (document.form1.printwithbatch.value!="") {
				bodysingle += '<img alt="" src="'+(batch)+'" class=batch'+icount+' id=batch'+icount+'>';
			}
			bodysingle += '<label class="lblqtyrow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">QTY.: '+recqty+'</label>';
			bodysingle += '</div>';
			bodysingle += '</div>';
			$("#singleup").append(bodysingle);

			JsBarcode("#barcode"+icount, ""+ item +"", {format: "CODE128", width:BarcodeWidth,  height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText} );
			if (document.form1.printwithbatch.value!="") {
				JsBarcode(".batch"+icount, ""+ (batch) +"", {format: "CODE128", width:batchBarcodeWidth, height:BarcodeHeight, fontOptions: "bold", fontSize: FontSize, textAlign: TextAlign, textPosition: TextPosition, displayValue: isDisplayText});
			}
			
			}
}

var newBarcode = function() {
	var totrecqty=$("input[name=totrecqty]").val();
	var totcount=$("input[name=totcount]").val();
	var DisplayText=$("input[name=DisplayText]").val();
	var isDisplayText=false;
	if(DisplayText=='Show')
		isDisplayText=true;
	var BarcodeWidth=$("input[name=BarcodeWidth]").val();
	var batchBarcodeWidth=parseInt(BarcodeWidth)/2;
	
	var textPosition =$("input[name=TextPosition]").val();
	if(textPosition=="Top")
	textPosition="top";
	else if(textPosition=="Bottom")
	textPosition="bottom";
	
	var textAlign =$("input[name=TextAlign]").val();
	if(textAlign=="Left")
	textAlign="left";
	else if(textAlign=="Center")
	textAlign="center";
	else if(textAlign=="Right")
	textAlign="right";
	
	for (var j= 0; j < totrecqty; j++) {
		//var mytxtcontent = $(".barcodevalue"+j).val();
		//console.log(mytxtcontent);
		$(".barcode"+j).JsBarcode(
		        $(".barcodevalue"+j).val(),
		        {
		          "format": "CODE128",
		          "fontSize": parseInt($("input[name=FontSize]").val()),
		          "height": parseInt($("input[name=BarcodeHeight]").val()),
		          "width": BarcodeWidth,
		          "displayValue": isDisplayText,
		          "fontOptions": "bold",
		          "textPosition": textPosition,
		          "textAlign": textAlign		          
		        });
	}
	if (document.form1.printwithbatch.value!="") {
		for (var j= 0; j < totcount; j++) {
			$(".batch"+j).JsBarcode(
			        $(".batchvalue"+j).val(),
			        {
			          "format": "CODE128",
			          "fontSize": parseInt($("input[name=FontSize]").val()),
			          "height": parseInt($("input[name=BarcodeHeight]").val()),
			          "width": batchBarcodeWidth,
			          "displayValue": isDisplayText,
			          "fontOptions": "bold",
			          "textPosition": textPosition,
		          	  "textAlign": textAlign	          
			        });
			}
		}
};

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
	var PRINT_WITH_MODEL=$("input[name=PRINT_WITH_MODEL]").val();
	var MODEL=$("input[name=MODEL]").val();
	var PRINT_WITH_PLANT=$("input[name=PRINT_WITH_PLANT]").val();
	var PRINT_DATE_LABEL=$("input[name=PRINT_DATE_LABEL]").val();
	var CEMAIL=$("input[name=CEMAIL]").val();
	var CHPNO=$("input[name=CHPNO]").val();
	
    if(LabelType=='Double') {
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
    var i=0,s=0;
    var lblsize=FontSize/3.5;
    var lblsizeprd=(parseInt(  FontSize)+15)/3.5;
    var lblsizeprd50_25=(parseInt(  FontSize)+10)/3.5;
    var barWidth=BarcodeWidth*10;
    var barheight=BarcodeHeight/5;
    var Textheight=BarcodeHeight/2.3;
    if(Textheight>35)
    Textheight=Textheight-15;
    $("#doubleup").each(function(){
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
	  var pData ="",qData="";
	  if (document.form1.printwithpobill.value!=""){
      	pData =($("#lblporow"+j).text());
	  }
	  if (document.form1.printwithlot.value!="") {
		  qData =($("#lblqtyrow"+j).text());
	  }
      //console.log('labelval :' + lblData);
      
      var imgData =($(imgnw).attr('src'));
      //console.log('len2 :' + imgData);
     	var img = new Image();
        img.src = imgData || '';

        var imgbchData =($(imgbchnw).attr('src'));
        //console.log('len2 :' + imgbchData);
       	var imgbch = new Image();
          imgbch.src = imgbchData || '';
        
        //console.log('len3 :' + s);  
         if(i>1 && s==0)
        doc.addPage("", "landscape");
        if(s==0){
          doc.setFontSize(lblsize);
		  doc.setFont("monospace","bold");
		  if (document.form1.printwithpobill.value!=""){	         
	          doc.text(pData, 3, 3);
	          doc.text(lblData, 3, 6);
	          doc.addImage(img, "JPG", 2, 7, barWidth, barheight);
	          if (document.form1.printwithbatch.value!=""){
	        	  if(BarcodeHeight<11) {
		        	  Textheight=12;
		          } else {
		        	  Textheight=12+(parseInt(BarcodeHeight)/10);
		          }
	        	  doc.addImage(imgbch, "JPG", 2, Textheight, (barWidth/2), barheight);
	          if (document.form1.printwithlot.value!="") {
	              var finalY= Textheight-9;
	          	doc.text(qData, 3, (Textheight+finalY));
	              }
	          } else {
	        	  doc.text(qData, 3, Textheight);
		          } 
				    } else {          
          doc.text(lblData, 1, 3);
          doc.addImage(img, "JPG", 1, 4, barWidth, barheight);
          if (document.form1.printwithbatch.value!=""){
        	  if(BarcodeHeight<9) {
	        	  Textheight=9;
	          } else {
	        	  Textheight=9+(parseInt(BarcodeHeight)/10);
	          }
        	  doc.addImage(imgbch, "JPG", 2, Textheight, (barWidth/2), barheight);
          if (document.form1.printwithlot.value!="") {
              var finalY= Textheight-6;
          	doc.text(qData, 3, (Textheight+finalY));
              }
          } else {
        	  if(BarcodeHeight<9) {
	        	  Textheight=9;
	          } else {
	        	  Textheight=9+(parseInt(BarcodeHeight)/10);
	          }
        	  doc.text(qData, 3, Textheight);
              }
                     
				    }
			if(document.form1.PAGE_TYPE.value=="PRDBARCODE"||document.form1.PAGE_TYPE.value=="MULTIPLEPRODUCT"){
			doc.setFontSize(lblsizeprd50_25);
            Textheight=Textheight+1.5;
            doc.text(lblPriceData, 1, Textheight);
            if(PRINT_WITH_MODEL=="1"){
			doc.setFontSize(lblsize);
            Textheight=Textheight+2.5;
            doc.text(MODEL, 1, Textheight);	
			}
            if(printdate!=""){
            if(printtype=="30X25"){
            doc.setFontSize(lblsize-2.5);
          	Textheight=Textheight+2.5;
	        doc.text(PRINT_DATE_LABEL+" "+printdate, 20, Textheight);
	        } else {
			doc.setFontSize(lblsize);
			Textheight=Textheight+2.5;
            doc.text(PRINT_DATE_LABEL+" "+printdate, 1, Textheight);
			}		
			}
            doc.setFontSize(lblsize-1.6);
            Textheight=Textheight+2.5;
            if(PRINT_WITH_PLANT=="1"){
	        doc.text(lblPlantData, 1, Textheight);
	        } else {
			doc.setFontStyle("normal");
	        doc.text(CHPNO, 3, Textheight);
	        Textheight=Textheight+2;
	        doc.text(CEMAIL, 3, Textheight);
	        } 
	        }
          s=1;
        }
        else {        	
         
          doc.setFontSize(lblsize);
		  doc.setFont("monospace","bold");
		  if (document.form1.printwithpobill.value!=""){
			  doc.text(pData, 54, 3);
			  doc.text(lblData, 54, 6);
	          doc.addImage(img, "JPG", 53, 7, barWidth, barheight);
	          if (document.form1.printwithbatch.value!=""){
	        	  if(BarcodeHeight<11) {
		        	  Textheight=12;
		          } else {
		        	  Textheight=12+(parseInt(BarcodeHeight)/10);
		          }
	        	  doc.addImage(imgbch, "JPG", 53, Textheight, (barWidth/2), barheight);
	          if (document.form1.printwithlot.value!="") {
	              var finalY= Textheight-9;
	          	doc.text(qData, 54, (Textheight+finalY));
	              }
	          } else {
	        	  doc.text(qData, 54, Textheight);
		          } 
				    } else {
          doc.text(lblData, 52, 3);
          doc.addImage(img, "JPG", 52, 4, barWidth, barheight);
          if (document.form1.printwithbatch.value!=""){
        	  doc.addImage(imgbch, "JPG", 53, Textheight, (barWidth/2), barheight);	          
          if (document.form1.printwithlot.value!="") {
              var finalY= Textheight-6;
          	doc.text(qData, 54, (Textheight+finalY));
              }
          } else {
	
			  if(BarcodeHeight<9) {
	        	  Textheight=9;
	          } else {
	        	  Textheight=9+(parseInt(BarcodeHeight)/10);
	        	  if(Textheight>13)
	        		  Textheight=Textheight+((parseInt(BarcodeHeight)/10)-3);
		          }
	
        	  doc.text(qData, 54, Textheight);
              } 
				    }
				    if(document.form1.PAGE_TYPE.value=="PRDBARCODE"||document.form1.PAGE_TYPE.value=="MULTIPLEPRODUCT"){
			doc.setFontSize(lblsizeprd50_25);
            Textheight=Textheight+1.5;
            doc.text(lblPriceData, 52, Textheight);
            if(PRINT_WITH_MODEL=="1"){
			doc.setFontSize(lblsize);
            Textheight=Textheight+2.5;
            doc.text(MODEL, 52, Textheight);	
			}
            if(printdate!=""){
            if(printtype=="30X25"){
            doc.setFontSize(lblsize-2.5);
          	Textheight=Textheight+2.5;
	        doc.text(PRINT_DATE_LABEL+" "+printdate, 71, Textheight);
	        } else {
			doc.setFontSize(lblsize);
			Textheight=Textheight+2.5;
            doc.text(PRINT_DATE_LABEL+" "+printdate, 52, Textheight);
			}		
			}
            doc.setFontSize(lblsize-1.6);
            Textheight=Textheight+2.5;
            if(PRINT_WITH_PLANT=="1"){
	        doc.text(lblPlantData, 52, Textheight);
	        } else {
			doc.setFontStyle("normal");
	        doc.text(CHPNO, 54, Textheight);
	        Textheight=Textheight+2;
	        doc.text(CEMAIL, 54, Textheight);
	        }
	        }
          s=0;
        
        }
        i++;        
    }

    }
    });
    /*if(document.form1.PAGE_TYPE.value=="RECEIPT")
	doc.save('Doubleup_Receipt_Pdf.pdf');
else if(document.form1.PAGE_TYPE.value=="LOCATION")
	doc.save('Doubleup_Location_Pdf.pdf');
else if(document.form1.PAGE_TYPE.value=="MANUAL")
	doc.save('Doubleup_Manual_Pdf.pdf');
else if(document.form1.PAGE_TYPE.value=="PRDBARCODE")
	doc.save('Singleup_Product_Pdf.pdf');
else
    doc.save('DoubleupPdf.pdf');*/
    //document.getElementById("Print_Barcode").setAttribute('src', doc.output('datauri'));//TO VIEW IN IFRAME
	setTimeout(function() {
	var printWindow = window.open(doc.output('bloburl'), '_blank');
	printWindow.print();
	if(document.form1.PAGE_TYPE.value=="RECEIPT") {
  	document.form1.action="/track/DynamicFileServlet?action=printBarcode&Submit="+document.form1.printtype.value;
    document.form1.submit();
    }
    else if(document.form1.PAGE_TYPE.value=="LOCATION") {
  	document.form1.action="/track/DynamicFileServlet?action=printLocBarcode&Submit="+document.form1.printtype.value;
    document.form1.submit();
    }
    else if(document.form1.PAGE_TYPE.value=="MANUAL") {
	document.form1.action="/track/DynamicFileServlet?action=printManualBarcode&Submit="+document.form1.printtype.value;
    document.form1.submit();
    }  
    else if(document.form1.PAGE_TYPE.value=="PRDBARCODE") {
	document.form1.action="/track/DynamicFileServlet?action=printProductBarcode&Submit="+document.form1.printtype.value;
    document.form1.submit();
    }
    else if(document.form1.PAGE_TYPE.value=="MULTIPLEPRODUCT") {
	document.form1.action="/track/DynamicFileServlet?action=printMultiProductBarcode&Submit="+document.form1.printtype.value;
    document.form1.submit();
    }
      }, 1000); // Adjust the time as needed
  }
  else{
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
	    var lblsizeprd50_25=(parseInt(FontSize)+10)/3.5;
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
    		  if (document.form1.printwithpobill.value!=""){
    	      	pData =($("#lblporow"+j).text());
    		  }
    		  if (document.form1.printwithlot.value!="") {
    			  qData =($("#lblqtyrow"+j).text());
    		  }
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
		  if (document.form1.printwithpobill.value!=""){	         
	          doc.text(pData, 3, 3);
	          doc.text(lblData, 3, 6);
	          doc.addImage(img, "JPG", 2, 7, barWidth, barheight);
	          if (document.form1.printwithbatch.value!=""){
	          if(BarcodeHeight<11) {
	        	  Textheight=12;
	          } else {
	        	  Textheight=12+(parseInt(BarcodeHeight)/10);
	        	  if(Textheight>19)
	        		  Textheight=Textheight+((parseInt(BarcodeHeight)/10)-5);
		          }
	          	  //console.log('len2 :' + Textheight);
	        	  doc.addImage(imgbch, "JPG", 2, Textheight, (barWidth/2), barheight);
		          
	          if (document.form1.printwithlot.value!="") {
	              var finalY= Textheight-7;
	        	doc.text(qData, 3, (Textheight+finalY));
	              }
	          }
	          else{
	        	  if(BarcodeHeight<11) {
		        	  Textheight=12;
		          } else {
		        	  Textheight=12+(parseInt(BarcodeHeight)/10);
		        	  if(Textheight>16)
		        		  Textheight=Textheight+((parseInt(BarcodeHeight)/10)-3);
			          }
		          	  //console.log('len2 :' + Textheight);
	        	  doc.text(qData, 3, Textheight);
		          } 
				    } else {
		if(printtype=="30X25"){
			//console.log('002');			          
        doc.text(lblData, 0, 5);
        doc.addImage(img, "JPG", 0, 6, barWidth, barheight);
        } else {
        doc.text(lblData, 1, 3);
        doc.addImage(img, "JPG", 1, 4, barWidth, barheight);
        }
        if (document.form1.printwithbatch.value !=""){
        	if(BarcodeHeight<9) {
	        	  Textheight=9;
	          } else {
	        	  Textheight=9+(parseInt(BarcodeHeight)/10);
	        	  if(Textheight>15)
	        		  Textheight=Textheight+((parseInt(BarcodeHeight)/10)-4);
		          }
	          	  //console.log('len2 :' + Textheight);
      	  doc.addImage(imgbch, "JPG", 2, Textheight, (barWidth/2), barheight);
	          
        if (document.form1.printwithlot.value!="") {
            var finalY= Textheight-5;
        	doc.text(qData, 3, (Textheight+finalY));
            }
        } else {
	if(printtype=="30X25"){
	if(BarcodeHeight<9) {
	        	  Textheight=11;
	          } else {
	        	  Textheight=11+(parseInt(BarcodeHeight)/10);
	        	  if(Textheight>13)
	        		  Textheight=Textheight+((parseInt(BarcodeHeight)/10)-3);
		          }
	} else {
        	if(BarcodeHeight<9) {
	        	  Textheight=9;
	          } else {
	        	  Textheight=9+(parseInt(BarcodeHeight)/10);
	        	  if(Textheight>13)
	        		  Textheight=Textheight+((parseInt(BarcodeHeight)/10)-3);
		          }
		          }
	          	  //console.log('len2 :' + Textheight);
        	doc.text(qData, 3, Textheight);
            }
            if(document.form1.PAGE_TYPE.value=="PRDBARCODE"||document.form1.PAGE_TYPE.value=="MULTIPLEPRODUCT"){
			if(printtype=="30X25"){
			doc.setFontSize(lblsizeprd+1);
        	Textheight=Textheight+2;
          	doc.text(lblPriceData, 3, Textheight);
          	} else {
			doc.setFontSize(lblsizeprd50_25+1);
            Textheight=Textheight+1.5;
            doc.text(lblPriceData, 1, Textheight);
            }
            if(PRINT_WITH_MODEL=="1"){
			doc.setFontSize(lblsize);
            Textheight=Textheight+2.5;
            doc.text(MODEL, 1, Textheight);	
			}
            if(printdate!=""){
			if(printtype=="30X25"){
            doc.setFontSize(lblsize-2.5);
          	Textheight=Textheight+2;
            if(PRINT_DATE_LABEL!="")
	        doc.text(PRINT_DATE_LABEL+" "+printdate, 20, Textheight);
	        else
	        doc.text(printdate, 20, Textheight);
	        } else {
			doc.setFontSize(lblsize);
			Textheight=Textheight+2.5;
            doc.text(PRINT_DATE_LABEL+" "+printdate, 1, Textheight);
			}
	        }
            Textheight=Textheight+2.5;
	        if(PRINT_WITH_PLANT=="1"){
			if(printtype=="30X25"){
            doc.setFontSize(lblsize-3.5);
            Textheight=Textheight-0.5;
            } else {
            doc.setFontSize(lblsize-1.6);
            }
	        doc.text(lblPlantData, 1, Textheight);
	        } else { 
			doc.setFontStyle("normal");
			doc.setFontSize(lblsize-1.6);
	        doc.text(CHPNO, 3, Textheight);
	        Textheight=Textheight+2;
	        doc.text(CEMAIL, 3, Textheight);
	        } 
	        }  
				    }
        i++;        
    }
    	}
  });

/*if(document.form1.PAGE_TYPE.value=="RECEIPT")
	doc.save('Singleup_Receipt_Pdf.pdf');
else if(document.form1.PAGE_TYPE.value=="LOCATION")
	doc.save('Singleup_Location_Pdf.pdf');
else if(document.form1.PAGE_TYPE.value=="MANUAL")
	doc.save('Singleup_Manual_Pdf.pdf');
else if(document.form1.PAGE_TYPE.value=="PRDBARCODE")
	doc.save('Singleup_Product_Pdf.pdf');
else
    doc.save('SingleupPdf.pdf');*/
    //document.getElementById("Print_Barcode").setAttribute('src', doc.output('datauri'));//TO VIEW IN IFRAME
	setTimeout(function() {
		var printWindow = window.open(doc.output('bloburl'), '_blank');
	printWindow.print();
	if(document.form1.PAGE_TYPE.value=="RECEIPT") {
  	document.form1.action="/track/DynamicFileServlet?action=printBarcode&Submit="+document.form1.printtype.value;
    document.form1.submit();
    }
    else if(document.form1.PAGE_TYPE.value=="LOCATION") {
  	document.form1.action="/track/DynamicFileServlet?action=printLocBarcode&Submit="+document.form1.printtype.value;
    document.form1.submit();
    }
    else if(document.form1.PAGE_TYPE.value=="MANUAL") {
	document.form1.action="/track/DynamicFileServlet?action=printManualBarcode&Submit="+document.form1.printtype.value;
    document.form1.submit();
    }  
    else if(document.form1.PAGE_TYPE.value=="PRDBARCODE") {
	document.form1.action="/track/DynamicFileServlet?action=printProductBarcode&Submit="+document.form1.printtype.value;
    document.form1.submit();
    }
    else if(document.form1.PAGE_TYPE.value=="MULTIPLEPRODUCT") {
	document.form1.action="/track/DynamicFileServlet?action=printMultiProductBarcode&Submit="+document.form1.printtype.value;
    document.form1.submit();
    }
      }, 1000); // Adjust the time as needed
  }
  
}

function ViewLocationBarcode(str,totrecqty)
{
	var chkdata = str.split(",");
	var loc = chkdata[0];				
	var locdesc = chkdata[1];
	var recqty = chkdata[2];
	
	var DisplayText=$("input[name=DisplayText]").val();
	var TextPosition=$("input[name=TextPosition]").val();
	var TextAlign=$("input[name=TextAlign]").val();
	var FontSize=$("input[name=FontSize]").val();
	var BarcodeHeight=$("input[name=BarcodeHeight]").val();
	var BarcodeWidth=$("input[name=BarcodeWidth]").val();
	var LabelType=$("input[name=LabelType]").val();
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
	body += '<label class="lbluprow" id="lbluprow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	body += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
	body += '</div>';

	bodysingle += '<div class="row divuprow">';
	bodysingle += '<div class="col-sm-6">';
	
	bodysingle += '<input type="hidden" name="locdescprint'+j+'" value="'+locdesc+'">';
	bodysingle += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	bodysingle += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+loc+'">';
	bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	bodysingle += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
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
	body += '<label class="lbluprow" id="lbluprow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	body += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
	body += '</div>';
	
	bodysingle="";
	bodysingle += '<div class="row divuprow">';
	bodysingle += '<div class="col-sm-6">';
	bodysingle += '<input type="hidden" name="locdescprint'+j+'" value="'+locdesc+'">';
	bodysingle += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	bodysingle += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+loc+'">';
	bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	bodysingle += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
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
		
		}

function ViewManualBarcode(loc,locdesc,recqty,totrecqty)
{
	
	var DisplayText=$("input[name=DisplayText]").val();
	var TextPosition=$("input[name=TextPosition]").val();
	var TextAlign=$("input[name=TextAlign]").val();
	var FontSize=$("input[name=FontSize]").val();
	var BarcodeHeight=$("input[name=BarcodeHeight]").val();
	var BarcodeWidth=$("input[name=BarcodeWidth]").val();
	var LabelType=$("input[name=LabelType]").val();
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
	body += '<label class="lbluprow" id="lbluprow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	body += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
	body += '</div>';

	bodysingle += '<div class="row divuprow">';
	bodysingle += '<div class="col-sm-6">';
	
	bodysingle += '<input type="hidden" name="locdescprint'+j+'" value="'+locdesc+'">';
	bodysingle += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	bodysingle += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+loc+'">';
	bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	bodysingle += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
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
	body += '<label class="lbluprow" id="lbluprow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	body += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
	body += '</div>';
	
	bodysingle="";
	bodysingle += '<div class="row divuprow">';
	bodysingle += '<div class="col-sm-6">';
	bodysingle += '<input type="hidden" name="locdescprint'+j+'" value="'+locdesc+'">';
	bodysingle += '<input type="hidden" name="locprint'+j+'" value="'+loc+'">';
	bodysingle += '<input type="hidden" name="barcodevalue'+j+'"  class=barcodevalue'+j+' value="'+loc+'">';
	bodysingle += '<label class="lbluprow" style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+locdesc+'</label>';
	bodysingle += '<img style="display: block;" alt="" src="'+loc+'" class=barcode'+j+' id=barcode'+j+'>';
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
		
		}
function ViewPrtBarcode(loc,locdesc,cursyml,price,recqty,totrecqty,model,printWithPLANT)
{
	
	var DisplayText=$("input[name=DisplayText]").val();
	var TextPosition=$("input[name=TextPosition]").val();
	var TextAlign=$("input[name=TextAlign]").val();
	var FontSize=$("input[name=FontSize]").val();
	var PriceFontSize=parseFloat(FontSize)+8;
	var PlantFontSize=parseFloat(FontSize)-10;
	var printtype=$("input[name=printtype]").val();
	if(printtype=="30X25")
		PlantFontSize=parseFloat(FontSize)-15;
	var BarcodeHeight=$("input[name=BarcodeHeight]").val();
	var BarcodeWidth=$("input[name=BarcodeWidth]").val();
	var LabelType=$("input[name=LabelType]").val();
	var printdate=$("input[name=printdate]").val();
	var cname =$("input[name=CNAME]").val();
	var chpno =$("input[name=CHPNO]").val();
	var cemail =$("input[name=CEMAIL]").val();
	var printlabel =$("input[name=PRINT_DATE_LABEL]").val();
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
	if(model!=""){
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+model+'</label>';
	}
	if(printdate!=""){
	if(printtype=="30X25"){
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;left: 30%;">'+printlabel+' '+printdate+'</label>';
	} else {
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+printlabel+' '+printdate+'</label>';
	}
	}
	if(printWithPLANT=="1"){
	body += '<label class="lblcnrow" id="lblcnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cname+'</label>';
	} else {
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+chpno+'</label>';	
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cemail+'</label>';	
	}
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
	if(model!=""){
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+model+'</label>';
	}
	if(printdate!=""){
	if(printtype=="30X25"){
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;left: 30%;">'+printlabel+' '+printdate+'</label>';
	} else {
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+printlabel+' '+printdate+'</label>';
	}
	}
	if(printWithPLANT=="1"){
	bodysingle += '<label class="lblcnrow" id="lblcnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cname+'</label>';
	} else {
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+chpno+'</label>';	
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cemail+'</label>';	
	}
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
	if(model!=""){
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+model+'</label>';
	}
	if(printdate!=""){	
	if(printtype=="30X25"){
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;left: 30%;">'+printlabel+' '+printdate+'</label>';
	} else {
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+printlabel+' '+printdate+'</label>';
	}
	}
	if(printWithPLANT=="1"){
	body += '<label class="lblcnrow" id="lblcnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cname+'</label>';
	} else {
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+chpno+'</label>';	
	body += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cemail+'</label>';	
	}
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
	if(model!=""){
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+model+'</label>';
	}
	if(printdate!="") {	
	if(printtype=="30X25"){
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;left: 30%;">'+printlabel+' '+printdate+'</label>';
	} else {
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+FontSize+'px;">'+printlabel+' '+printdate+'</label>';
	}
	}
	if(printWithPLANT=="1"){
	bodysingle += '<label class="lblcnrow" id="lblcnrow'+j+'" style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cname+'</label>';
	} else {
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+chpno+'</label>';
	bodysingle += '<label style="font-weight: bold; font-family: monospace; display: block; font-size: '+PlantFontSize+'px;">'+cemail+'</label>';
	}
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
		
		}
