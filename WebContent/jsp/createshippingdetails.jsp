<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>


<%
String title = "Create Shipping Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">



<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'ShippingDetail', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String plant="",res= "",action="",fieldDesc="",allChecked="";
String customername="",contactname="",telephone="",handphone="",fax="",email="",unitno="",building="",street="",city="",state="",country="",postalcode="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();

action            = strUtils.fString(request.getParameter("action"));
plant  = (String)session.getAttribute("PLANT");
customername  = strUtils.fString(request.getParameter("CUSTOMERNAME"));
contactname  = strUtils.fString(request.getParameter("CONTACTNAME"));
telephone  = strUtils.fString(request.getParameter("TELEPHONE"));
handphone = strUtils.fString(request.getParameter("HANDPHONE"));
fax  = strUtils.fString(request.getParameter("FAX"));
email  = strUtils.fString(request.getParameter("EMAIL"));
unitno  = strUtils.fString(request.getParameter("UNITNO"));
building  = strUtils.fString(request.getParameter("BUILDING"));
street = strUtils.fString(request.getParameter("STREET"));
city = strUtils.fString(request.getParameter("CITY"));
state = strUtils.fString(request.getParameter("STATE"));
country = strUtils.fString(request.getParameter("COUNTRY"));
postalcode  = strUtils.fString(request.getParameter("POSTALCODE"));



if(action.equalsIgnoreCase("result"))
{
  fieldDesc=(String)request.getSession().getAttribute("RESULT");
  fieldDesc = "<font class='maingreen'>" + fieldDesc + "</font>";
}
else if(action.equalsIgnoreCase("resulterror"))
{
	fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
	fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
}

if(action.equalsIgnoreCase("catchrerror"))
{
  fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
  fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
  allChecked = strUtils.fString(request.getParameter("allChecked"));

  
  
}

%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
  
  
<form class="form-horizontal" name="form" method="post" action="/trackk/MasterServlet?">
<div id = "ERROR_MSG"></div>
<div class="form-group">
<label class="control-label col-sm-4" for="Customer Name">Customer Name:</label>
<INPUT type="hidden" name="RFLAG" value="1">
<div class="col-sm-4">
<input name="CUSTOMERNAME" id="CUSTOMERNAME" value="<%=customername%>"
			size="50" MAXLENGTH=40 class="form-control">
   </div>
        </div>
        
     
<div class="form-group">
<label class="control-label col-sm-4" for="Contact Name">Contact Name:</label>
<div class="col-sm-4">
<input name="CONTACTNAME" id="CONTACTNAME" value="<%=contactname%>"
			size="50" MAXLENGTH=30 class="form-control">
   </div>
        </div> 
        
<div class="form-group">
<label class="control-label col-sm-4" for="Telephone">Telephone:</label>
<div class="col-sm-4">
<input name="TELEPHONE" id="TELEPHONE"	value="<%=telephone%>"
			size="50" MAXLENGTH=20 class="form-control">
   </div>
        </div>  
        
        
<div class="form-group">
<label class="control-label col-sm-4" for="Hand phone">Handphone:</label>
<div class="col-sm-4">
<input name="HANDPHONE" id="HANDPHONE"	value="<%=handphone%>"
			size="50" MAXLENGTH=20 class="form-control">
   </div>
        </div>
        
<div class="form-group">
<label class="control-label col-sm-4" for="Fax"> Fax:</label>
<div class="col-sm-4">
<input name="FAX" id="FAX"	value="<%=fax%>"
		size="50" MAXLENGTH=20 class="form-control">
   </div>
        </div>
        
<div class="form-group">
<label class="control-label col-sm-4" for="Email">Email:</label>
<div class="col-sm-4">
<input name="EMAIL" id="EMAIL"	value="<%=email%>"
		size="50" MAXLENGTH=30 class="form-control">
   </div>
        </div>
        
       
<div class="form-group">
<label class="control-label col-sm-4" for="Unit No.">Unit No.:</label>
<div class="col-sm-4">
<input name="UNITNO" id="UNITNO" value="<%=unitno%>"
		size="50" MAXLENGTH=20 class="form-control">
   </div>
        </div>   
        
<div class="form-group">
<label class="control-label col-sm-4" for="Building">Building:</label>
<div class="col-sm-4">
<input name="BUILDING" id="BUILDING" value="<%=building%>"
		size="50" MAXLENGTH=20 class="form-control">
   </div>
        </div> 
        
<div class="form-group">
<label class="control-label col-sm-4" for="Street">Street:</label>
<div class="col-sm-4">
<input name="STREET" id="STREET" value="<%=street%>"
		size="50" MAXLENGTH=40 class="form-control">
   </div>
        </div>
        
        
<div class="form-group">
<label class="control-label col-sm-4" for="City">City:</label>
<div class="col-sm-4">
<input name="CITY" id="CITY" value="<%=city%>"
		size="50" MAXLENGTH=20 class="form-control">
   </div>
        </div>
        
<div class="form-group">
<label class="control-label col-sm-4" for="State">State:</label>
<div class="col-sm-4">
<input name="STATE" id="STATE"	value="<%=state%>"
		size="50" MAXLENGTH=20 class="form-control">
   </div>
        </div>
        
<div class="form-group">
<label class="control-label col-sm-4" for="Country">Country:</label>
<div class="col-sm-4">
<input name="COUNTRY" id="COUNTRY"	value="<%=country%>"
		size="50" MAXLENGTH=20 class="form-control">
   </div>
        </div>
        
<div class="form-group">
<label class="control-label col-sm-4" for="Postal Code">Postal Code:</label>
<div class="col-sm-4">
<input name="POSTALCODE" id="POSTALCODE" value="<%=postalcode%>"
		size="50" MAXLENGTH=15 class="form-control">
   </div>
        </div>
        
<div class="form-group">        
<div class="col-sm-offset-4 col-sm-8">
<button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp;
<button type="button" class="Submit btn btn-default" value="Add To Shipping Details" onClick="onAdd()"><b>Add To Shipping Details</b></button>&nbsp;&nbsp;
<button type="button" class="Submit btn btn-default" value="Clear" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
 
	</div>
	 </div>
	 
<div id="RESULT_MESSAGE">
<table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center" bgcolor="white">
<tr><td align="center"><%=fieldDesc%></td></tr>
</table>
</div>

<div class="row">
<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
   <strong>  &nbsp; Select/Unselect All </strong> </div> 
   </div>
   
<div id="VIEW_RESULT_HERE"></div>
<div id="spinnerImg" ></div>
<div class="form-group">        
<div class="col-sm-12" align="center">
<button type="button" class="Submit btn btn-default" value="Remove From Shipping Details" onClick="onDelete()"><b>Remove From Shipping Details</b></button>&nbsp;&nbsp;
	</div>
	 </div>
	 
	 </form>
  </div>
  </div>
  </div>
  
  

<script>
onGo(0);
function onClear()
{
	document.getElementById("CUSTOMERNAME").value = "";
	document.getElementById("CONTACTNAME").value = "";
	document.getElementById("TELEPHONE").value = "";
	document.getElementById("HANDPHONE").value = "";
	document.getElementById("FAX").value = "";
	document.getElementById("EMAIL").value = "";
	document.getElementById("UNITNO").value = "";
	document.getElementById("BUILDING").value = "";
	document.getElementById("STREET").value = "";
	document.getElementById("CITY").value = "";
	document.getElementById("STATE").value = "";
	document.getElementById("COUNTRY").value = "";
	document.getElementById("POSTALCODE").value = "";
}
function checkAll(isChk)
{
	var len = document.form.chkitem.length;
   if(len == undefined) len = 1;  
    if (document.form.chkitem)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkitem.checked = isChk;
              		 
              	}
              	else{
              		document.form.chkitem[i].checked = isChk;
              		 
              	}
            	   	
                
        }
    }
}
function onAdd() {
	var customername = document.form.CUSTOMERNAME.value;
	var contactname = document.form.CONTACTNAME.value;
	var telephone = document.form.TELEPHONE.value;
	var handphone = document.form.HANDPHONE.value;
	var fax = document.form.FAX.value;
	var email = document.form.EMAIL.value;
	var unitno = document.form.UNITNO.value;
	var building = document.form.BUILDING.value;
	var street = document.form.STREET.value;
	var city = document.form.CITY.value;
	var state = document.form.STATE.value;
	var country = document.form.COUNTRY.value;
	var postalcode = document.form.POSTALCODE.value;
	
	if(customername=="" || customername.length==0 ) {
		alert("Enter Customer Name");
		document.getElementById("CUSTOMERNAME").focus();
		return false;
	}
	document.getElementById('RESULT_MESSAGE').innerHTML = '';   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {CUSTOMERNAME:customername,CONTACTNAME:contactname,TELEPHONE:telephone,HANDPHONE:handphone,
    	FAX:fax,EMAIL:email,UNITNO:unitno,BUILDING:building,STREET:street,CITY:city,STATE:state,COUNTRY:country,POSTALCODE:postalcode,
    	action: "ADD_SHIPPING_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    document.getElementById("CUSTOMERNAME").value = "";
	document.getElementById("CONTACTNAME").value = "";
	document.getElementById("TELEPHONE").value = "";
	document.getElementById("HANDPHONE").value = "";
	document.getElementById("FAX").value = "";
	document.getElementById("EMAIL").value = "";
	document.getElementById("UNITNO").value = "";
	document.getElementById("BUILDING").value = "";
	document.getElementById("STREET").value = "";
	document.getElementById("CITY").value = "";
	document.getElementById("STATE").value = "";
	document.getElementById("COUNTRY").value = "";
	document.getElementById("POSTALCODE").value = "";
	//onGo(0);
          
}


function onGo(index) {

	var index = index;
	document.getElementById('RESULT_MESSAGE').innerHTML = ''; 
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/MasterServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {PLANT:"<%=plant%>",action: "VIEW_SHIPPING_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
}

function onDelete()
{
	var checkFound = false;
	var chkitems = document.form.chkitem.value;
	 var len = document.form.chkitem.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
   {
		if(len == 1 && (!document.form.chkitem.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkitem.checked)
	     {
	    	 checkFound = true;
	    	 
	     }
	
	     else {
		     if(document.form.chkitem[i].checked){
		    	 checkFound = true;
		    	 
		     }
	     }
         		
       	     
   }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var chkmsg = confirm("Are you sure you would like to delete?");
		if (chkmsg) {
	  	  document.form.action="/track/MasterServlet?action=DELETE_SHIPPING_DETAILS";
	 	 document.form.submit();
	 	 return true;   
		}
		 else {
				return false;
			}
	
}

function callback(data){
	
	var outPutdata = getTable();
	var ii = 0;
	var errorBoo = false;
	$.each(data.errors, function(i,error){
		if(error.ERROR_CODE=="99"){
			errorBoo = true;
			
		}
	});
	
	if(!errorBoo){
		
        $.each(data.shippingdetailsmaster, function(i,item){
                   
        	outPutdata = outPutdata+item.SHIPPINGDETAILSMASTERDATA;
                    	ii++;
            
          });
        
	}
    outPutdata = outPutdata +'</TABLE>';
                                                  
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
     document.getElementById('spinnerImg').innerHTML =''; 
     var errorMsg = data.errorMsg;
     if(typeof(errorMsg) == "undefined"){
    	 errorMsg = "";
     }
     errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
     document.getElementById('ERROR_MSG').innerHTML = errorHTML;
   	     
}

function getTable(){
	
        return '<TABLE class="table" id="tabledata" BORDER="0" cellspacing="0" WIDTH="98%" align = "center">'+
               '<thead style="background:#eaeafa">'+
               '<tr >'+
                '<th width="4%" align="left">Select</th>'+
         		'<th width="4%" align="left">No</th>'+
         		'<th width="10%" align="left">CustomerName</th>'+
         		'<th width="10%" align="left">ContactName</th>'+
         		'<th width="8%" align="left">Telephone</th>'+
         		'<th width="8%" align="left">HandPhone</th>'+
         		'<th width="8%" align="left">Fax</th>'+
         		'<th width="8%" align="left">Email</th>'+
         		'<th width="8%" align="left">UnitNo</th>'+
         		'<th width="8%" align="left">Building</th>'+
         		'<th width="10%" align="left">Street</th>'+
         		'<th width="8%" align="left">City</th>'+
         		'<th width="8%" align="left">State</th>'+
         		'<th width="7%" align="left">Country</th>'+
         		'<th width="6%" align="left">PostalCode</th>'+
         	    '</tr>'+
         	    '</thead>';
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
