

function replace(string,text,by) {
    var strLength = string.length, txtLength = text.length;
    if ((strLength == 0) || (txtLength == 0)) return string;

    var i = string.indexOf(text);
    if ((!i) && (text != string.substring(0,txtLength))) return string;
    if (i == -1) return string;

    var newstr = string.substring(0,i) + by;

    if (i+txtLength < strLength)
        newstr += replace(string.substring(i+txtLength,strLength),text,by);

    return newstr;
}

function trim (myString)
{
return myString.replace(/^\s+/g,'').replace(/\s+$/g,'')
}
function round_float(x,n){
	  if(!parseInt(n))
	  	var n=0;
	  if(!parseFloat(x))
	  	return false;
	  return Math.round(x*Math.pow(10,n))/Math.pow(10,n);
	}

/**
 * DHTML date validation script for dd/mm/yyyy. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1900;
var maxYear=2100;

function isInteger(s){
	var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
	var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}

function daysInFebruary (year){
	// February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
	for (var i = 1; i <= n; i++) {
		this[i] = 31
		if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
		if (i==2) {this[i] = 29}
   } 
   return this
}

function isDate(dtStr){
	var daysInMonth = DaysArray(12)
	var pos1=dtStr.indexOf(dtCh)
	var pos2=dtStr.indexOf(dtCh,pos1+1)
	var strDay=dtStr.substring(0,pos1)
	var strMonth=dtStr.substring(pos1+1,pos2)
	var strYear=dtStr.substring(pos2+1)
	strYr=strYear
	if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
	if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
	for (var i = 1; i <= 3; i++) {
		if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
	}
	month=parseInt(strMonth)
	day=parseInt(strDay)
	year=parseInt(strYr)
	if (pos1==-1 || pos2==-1){
		alert("The date format should be : dd/mm/yyyy")
		return false
	}
	if (strMonth.length<1 || month<1 || month>12){
		alert("Please enter a valid month")
		return false
	}
	if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
		alert("Please enter a valid day")
		return false
	}
	if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
		alert("Please enter a valid 4 digit year between "+minYear+" and "+maxYear)
		return false
	}
	if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
		alert("Please enter a valid date")
		return false
	}
return true
}

function ValidateForm(){
	var dt=document.frmSample.txtDate
	if (isDate(dt.value)==false){
		dt.focus()
		return false
	}
    return true
 }


function validatetime(strval)

 {
  var strval1;

    
  //minimum lenght is 6. example 1:2 AM
  if(strval.length < 3)
  {
   alert("Invalid time. Time format should be HH:MM");
   return false;
  }
  
  //Maximum length is 8. example 10:45 AM
  if(strval.lenght > 5)
  {
   alert("invalid time. Time format should be HH:MM");
   return false;
  }
  
  //Removing all space
  strval = trimAllSpace(strval); 
  
      
  var pos1 = strval.indexOf(':');
  
  
  if(pos1 < 0 )
  {
   alert("invalid time. A color(:) is missing between hour and minute.");
   return false;
  }
 
  
  //Checking hours
  var horval =  trimString(strval.substring(0,pos1));
   
  if(horval == -100)
  {
   alert("Invalid time. Hour should contain only integer value (0-11).");
   return false;
  }
      
  if(horval > 24)
  {
   alert("invalid time. Hour can not be greater that 24.");
   return false;
  }
  else if(horval < 0)
  {
   alert("Invalid time. Hour can not be hours less than 0.");
   return false;
  }
  //Completes checking hours.
  
  //Checking minutes.
  var minval =  trimString(strval.substring(pos1+1,pos1 + 3));
  
  if(minval == -100)
  {
   alert("Invalid time. Minute should have only integer value (0-59).");
   return false;
  }
    
  if(minval > 59)
  {
     alert("Invalid time. Minute can not be more than 59.");
     return false;
  }   
  else if(minval < 0)
  {
   alert("Invalid time. Minute can not be less than 0.");
   return false;
  }
   
  //Checking minutes completed.  
  
  //Checking one space after the mintues 

    
  return true;
  
  
 }

function trimAllSpace(str) 
{ 
    var str1 = ''; 
    var i = 0; 
    while(i != str.length) 
    { 
        if(str.charAt(i) != ' ') 
            str1 = str1 + str.charAt(i); i ++; 
    } 
    return str1; 
}

function trimString(str) 
{ 
     var str1 = ''; 
     var i = 0; 
     while ( i != str.length) 
     { 
         if(str.charAt(i) != ' ') str1 = str1 + str.charAt(i); i++; 
     }
     var retval = IsNumeric(str1); 
     if(retval == false) 
         return -100; 
     else 
         return str1; 
}


function IsNumeric(sText)
{
var ValidChars = "0123456789.";
for (i = 0; i < sText.length; i++) { 
if (ValidChars.indexOf(sText.charAt(i)) == -1) {
return false;
}
}
return true;
}
function removeCommas(number)
{
	
	var splitArr = new Array();
	var appendStr = new String();
	splitArr = number.split(',');
	for(var i=0; i<splitArr.length; i++){
		appendStr = appendStr + splitArr[i];}

     return appendStr;

}

function IsValidStringWithoutSpace(sText)
{

if (sText.indexOf(' ') == -1) {
return true;
}else{
return false;
}
}
function addCommas(nStr)
{
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}
function isInteger(val){
    if(val==null){return false;}
    if (val.length==0){return false;}
    for (var i = 0; i < val.length; i++) {
          var ch = val.charAt(i);
          if (i==0 && ch == '-') {
              continue;
        }   
    if (ch < "0" || ch > "9") {
          return false;
    }
}
return true;
}

$(document).ready(function(){


	$('input,textarea').on('keypress', function (e) {
	    var ingnore_key_codes = [39, 91];
	  
	    if ($.inArray(e.which, ingnore_key_codes) >= 0) {
	        e.preventDefault();
			alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	       
	    } else {
	      
	    }
	}).on("paste",function(e){
		var textboxvalue = this;
	   setTimeout(function(){
	    var sValue = $(textboxvalue).val();
	    convertCharToString3(sValue); 
	 },100);
	 
	})
	});
	
function blockSpecialChar(e) {
	var k = e.keyCode;
	if((k > 64 && k < 92) || (k > 96 && k < 123) || k == 8   || k == 95 || k == 45 || k == 46 || k == 47 || k==39 || (k >= 48 && k <= 57)){ 
	}else{
	e.preventDefault();
	alert("Only ./-_ special characters are allowed in Id");
	}
}
	
function blockSpecialCharcter(e) {
	var k = e.keyCode;
	if((k ==39  || k ==91)){ 
	e.preventDefault();
	alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	}else{
	}
}

function blockSpecialCharOrderNo(e) {
	var k = e.keyCode;
	//if((k > 64 && k < 92) || (k > 96 && k < 123) || k == 8   || k == 95 || k == 46 || k==39 || (k >= 48 && k <= 57)){
	if((k > 64 && k < 92) || (k > 96 && k < 123) || k == 8   || k == 95 || k == 45 || k == 46 || k == 47 || k==39 || (k >= 48 && k <= 57)){
	}else{
	e.preventDefault();
	alert("Only ./-_ special characters are allowed in order number");
	}
}

function blockQoute(){
	$(document).ready(function(){


		$('input,textarea').on('keypress', function (e) {
		    var ingnore_key_codes = [39, 91];
		  
		    if ($.inArray(e.which, ingnore_key_codes) >= 0) {
		        e.preventDefault();
				alert("Apostrophe and Left Square Bracket Characters are not allowed.");
		       
		    } else {
		      
		    }
		}).on("paste",function(e){
			var textboxvalue = this;
		   setTimeout(function(){
		    var sValue = $(textboxvalue).val();
		    convertCharToString4(sValue); 
		 },100);
		 
		})
		});
}

function convertCharToString3(char){
	  var str="";
	  for(var i=0;i<char.length;i++) { 
	  if(char[i] == "'")
	  alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	  else if(char[i] == "[") 
	  alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	  }
	}
function convertCharToString4(char){
	debugger;
	  var str="";
	  for(var i=0;i<char.length;i++) { 
	  if(char[i] == "'")
	  alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	  else if(char[i] == "[") 
	  alert("Apostrophe and Left Square Bracket Characters are not allowed.");
	  }
	}

// -->