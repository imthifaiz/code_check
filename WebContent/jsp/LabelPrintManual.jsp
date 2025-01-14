<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>LabelPrintManual</title>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script src="js/JsBarcode.all.js"></script>
<script src="js/jspdf.js"></script>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'ProductionBOM', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
		
}


function onPrint()
{
	 var checkFound = false;  
	 var orderLNo;
	 var len = document.form.chkdLnNo.length; 
	 if(len == undefined) len = 1;
	for (var i = 0; i < len ; i++)
    {
    	if(len ==1 && document.form.chkdLnNo.checked)  
    	{
    		chkstring = document.form.chkdLnNo.value;
     	}
    	else
    	{
    		chkstring = document.form.chkdLnNo[i].value;
     	}
    	chkdvalue = chkstring.split(',');
		if(len == 1 && (!document.form.chkdLnNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdLnNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
	    	 
	    }
	
	     else {
		     if(document.form.chkdLnNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
		    	 
		    }
		   
	     }
              	     
    }
	
    if (checkFound!=true) {
    	alert("Please check at least one checkbox.");
		return false;
	}
     
   //document.form.action ="/track/LabelPrintServlet?action=PrintManual";
   //document.form.submit();
   
   
   var formData = $('form#LabelPrintManualForm').serialize();
	$.ajax({
		
      type: 'post',
      url: '/track/LabelPrintServlet?action=PrintManual', 
      dataType:'html',
      data:  formData,
      async:    false,
     success: function (data) {
      	$('#labelSettingPopUpContent').html(data);
      	
	         
      },
      error: function (data) {	        	
          alert(data.responseText);
      }
  });
 
 }
 

function onViewReport()
{
  document.form1.action ="/track/LabelPrintServlet?action=ViewReportProduct";
  document.form1.submit();

}

function onClear()
{
	document.form.ITEM.value = "";
	document.form.DESC.value = "";
	document.form.BATCH.value = "";
	document.form.action ="LabelPrintManual.jsp?action=clear";
    document.form.submit();
	
}
 
</SCRIPT>
</head>

<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
//String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",ITEM="",ITEMDESC="",BATCH="",fieldDesc="",allChecked="",DIRTYPE="",REFNO="",PGaction="",ISPOPUP="",errorDesc="",MODULETYPE ="",USER="";
session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
PGaction        = strUtils .fString(request.getParameter("PGaction")).trim();
action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
DIRTYPE       = strUtils.fString(request.getParameter("DIRTYPE"));
ITEM = strUtils.fString(request.getParameter("ITEM"));
ITEMDESC      = strUtils.fString(request.getParameter("DESC"));
BATCH = strUtils.fString(request.getParameter("BATCH"));
if(DIRTYPE.length()<=0){
	DIRTYPE = "LABEL MANUAL";
}
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
if(action.equalsIgnoreCase("clear"))
{

	   boolean flag=false;
	   ItemMstDAO _ItemMstDAO=new ItemMstDAO() ;
	  flag= _ItemMstDAO.removeLabelDetailsTemp(plant,"");
	   
}

if(PGaction.equalsIgnoreCase("View")){
  	try{
  		
  		 Hashtable ht = new Hashtable();

       
       
     	//   movQryList = movHisUtil.getLabelPrintProductWithBatch(ht,"LABEL PRINT PRODUCT WITH BATCH",plant,ITEMDESC,start,end);
        REFNO=(String)request.getSession().getAttribute("refNo");
        ISPOPUP=(String)request.getSession().getAttribute("ISPOPUP");
            
     	errorDesc=(String)request.getSession().getAttribute("RESULTERROR");
     	
        if(errorDesc.length() != 0 && !errorDesc.equals(null))
    	{
        	fieldDesc = "<font class='mainred'>" + errorDesc + "</font>";
      		allChecked = strUtils.fString(request.getParameter("allChecked"));
    	}
    	else
    	{    
    		System.out.println("ISPOPUP"+ISPOPUP);
    		if(ISPOPUP.equals("OPENED")){
    		if(REFNO.length()>0  )
    		{
    			
    			PrintWriter outWriter = response.getWriter();
    			outWriter.println("<script type=\"text/javascript\">");
    			outWriter.println("window.open('LabelSettings.jsp?PRINTTYPE=8','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')");
    		    outWriter.println("</script>");
    		  }
    		}
    		
    	}
        request.getSession().setAttribute("RESULT","");
        request.getSession().setAttribute("RESULTERROR","");
           
       }catch(Exception e) { 
      	  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
      	  if(e.getMessage()==null)
      	  {
      		  fieldDesc="";
      	  }
       
       	
      }
}


%>
<%@ include file="body.jsp"%>
<img id="productBarCode"/ hidden>
<img id="batchBarCode"/ hidden>
 <center><div id="labelSettingPopUpContent" class='mainred'></div></center>
  
<FORM name="form" method="post" id="LabelPrintManualForm" action="/track/LabelPrintServlet?" >
  <br>
   <INPUT type="hidden" name="RFLAG" value="1">
   <input type="hidden" name="PGaction" value="View">
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Label Print Manual</font>
  </table>
 
   <div id = "ERROR_MSG"></div>
  <br>
  
<TABLE border="0" width="50%" height = "10%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
   
     <TR>
          <TH ALIGN="left">&nbsp;Product ID : </TH>
          <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=50>
           <a href="#" onClick="javascript:popUpWin('view_product_list.jsp?ITEM='+form.ITEM.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
          
		<TH ALIGN="left">&nbsp;Product Description : </TH>
          <TD><INPUT name="DESC" type = "TEXT" value="<%=strUtils.forHTMLTag(ITEMDESC)%>" size="20"  MAXLENGTH=100></TD>
	 </TR>
    
          <TR>
               
            <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Batch:  </TH>
          <TD width="13%"><INPUT name="BATCH" type = "TEXT" value="<%=BATCH%>" size="20"  MAXLENGTH=20>
          </TD>
          <TH ALIGN="left" width="15%">  </TH>
          <TD width="13%">
          		<input type="button" value="Add" onClick="onAdd()"/>
           </TD>	
        
        </TR>
 
	 
  </TABLE>

</CENTER>
 <br> 
 <div id="RESULT_MESSAGE">
 <table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
	<tr><td align="center"></td></tr>
	
</table>
</div>
<br>


<table width="50%" cellspacing="0" cellpadding="0" align="center" >
    <tr>
    	<td >  
    		<INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
                        &nbsp; Select/Unselect
        </td>
</table>
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  <br>
 <table align="center" >
				<tr>
					 <td>  <input type="button" value="Back" onClick="window.location.href='LabelPrintMenu.jsp'">&nbsp; </td>
   					 <td>   <input type="button" value="Generate Label" onClick="onPrint();" > </td>
   					 <td> <input type="button" value="Clear Data" onClick="onClear();" > </td>
   
   
     
   				<INPUT type="Hidden" name="DIRTYPE" value="LABELMANUAL">
  				 <INPUT type="Hidden" name="REFNO" value=<%=REFNO%>>
  				    
    
				</tr>
</table>
				
</FORM>
<script>
onGo(0);

function checkAll(isChk)
{
	var len = document.form.chkdLnNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form .chkdLnNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkdLnNo.checked = isChk;
               	}
              	else{
              		document.form.chkdLnNo[i].checked = isChk;
              	}
            	
        }
    }
}
function validateProduct() {
    var productId = document.form.ITEM.value;
    var urlStr = "/track/ItemMstServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ITEM : productId,
			PLANT : "<%=plant%>",
			ACTION : "GET_PRODUCT_DETAILS"
			},
			dataType : "json",
			success : function(data) {
				
                                if (data.status == "100") {
                                        var resultVal = data.result;
                                        document.form.DESC.value = resultVal.sItemDesc;
                                 } 
			}
		});
	
}

function onAdd() {
	var product = document.form.ITEM.value;
	var desc = document.form.DESC.value;
	var batch = document.form.BATCH.value;
	
	
	if(product=="" || product.length==0 ) {
		alert("Enter Parent Product");
		document.getElementById("ITEM").focus();
		return false;
	}
	
	document.getElementById('RESULT_MESSAGE').innerHTML = '';   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,DESC:desc,BATCH:batch,action: "ADD_LABEL",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    document.form.ITEM.value = "";
    document.form.DESC.value = "";
    document.form.BATCH.value = "";
    
       
}

function onGo(index) {

	var index = index;
	var product = document.form.ITEM.value;
		
	if(index == '1'){
	if(product=="" || product.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
		return false;
		}
     
	}
	document.getElementById('RESULT_MESSAGE').innerHTML = ''; 
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,action: "VIEW_LABEL_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
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
		
        $.each(data.labeldetails, function(i,item){
                   
        	outPutdata = outPutdata+item.LBLDATA;
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
        return '<TABLE BORDER="0" id="tabledata" cellspacing="0" WIDTH="50%"  align = "center" bgcolor="navy">'+
        	   '<tr BGCOLOR="#000066">'+
         		'<th width="5%"><font color="#ffffff">Select</font></th>'+
         		'<th width="10%"><font color="#ffffff">Prod ID</font></th>'+
         		'<th width="11%"><font color="#ffffff">Prod Description</font></th>'+
         		'<th width="13%"><font color="#ffffff">Batch</font></th>'+
         		'</tr>';
       
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';


function labelManual(data,barcodeWidth,fontsize){
	var productID=[];	
	var Description=[];
	var productImgData=[];
	var batchImgData=[];
	var batchNumber = [];
	var dataLength=data.items.length;
	$.each(data.items, function(i,prodItem){
		var item = prodItem['ITEM'];
		var itemDescription = prodItem['ITEMDESC'];
		var batchNo = prodItem['BATCH'];
		productID.push(item);
		Description.push(itemDescription);
		batchNumber.push(batchNo);
		JsBarcode("#productBarCode",
			item,
			{displayValue: false ,
			orientation: 'landscape',
			format:'CODE128',
			lineColor: "black",
			textPosition: "bottom",
			width:barcodeWidth,
			height:150,
			text:item,
			textAlign:"left",
			textMargin:2,
			fontSize:10,
			background:"",
			lineColor:"",
			marginLeft:1,
			marginRight:0,				
			marginTop:10,
			marginBottom:10,
		});
		productImgData.push($("#productBarCode").attr("src"));
		if(batchNo != ''){
		JsBarcode("#batchBarCode",
			batchNo,
			{displayValue: false ,
			orientation: 'landscape',
			format:'CODE128',
			lineColor: "black",
			textPosition: "bottom",
			width:barcodeWidth,
			height:150,
			text:batchNo,
			textAlign:"left",
			textMargin:2,
			fontSize:10,
			background:"",
			lineColor:"",
			marginLeft:1,
			marginRight:0,				
			marginTop:10,
			marginBottom:10,
		});
		batchImgData.push($("#batchBarCode").attr("src"));
		}
		else{
			batchImgData.push('');
		}
		
		
		});
			var doc;
			doc = new jsPDF('l', 'mm', [50, 25]);
			doc.setFont("Arial Narrow","bold");
			doc.setFontSize(fontsize);
			//doc.lineHeightProportion = 2;
			
		//	doc.text(0, 60, "_________________________________________________________________________________________________________________________________________________________________________");
			if(batchImgData[0] != ''){
			doc.text(2, 3, "Product ID:"+productID[0]);
			doc.addImage(productImgData[0], 'JPEG', 2, 5,45,5);
			doc.text(2, 13, Description[0]);				
			doc.addImage(batchImgData[0], 'JPEG', 2, 14,45,5);
			doc.text(2, 23, "Batch/SN:"+batchNumber[0]);
			}
			else{
				doc.text(2, 4, "Product ID:"+productID[0]);
				doc.addImage(productImgData[0], 'JPEG', 2,5,45,16);
				doc.text(2, 23, Description[0]);
			}
			for(var i=1;i<dataLength;i++){
				doc.addPage();
				if(batchImgData[i] != ''){
					doc.text(2, 3, "Product ID: "+productID[i]);
					doc.addImage(productImgData[i], 'JPEG', 2, 4,45,6);
					doc.text(2, 13, "Desc: "+productID[i]);
					doc.addImage(batchImgData[i], 'JPEG', 2, 14,45,6);
					doc.text(2, 23, "Batch/SN:"+batchNumber[i]);
				}
				else{
					doc.text(2, 4, "Product ID:"+productID[i]);
					doc.addImage(productImgData[i], 'JPEG', 2,5,45,16);
					doc.text(2, 23, Description[i]);
				}
			}
			data = [];
			doc.save("BarCodeManual.pdf");
	
}

function labelManual100x50(data,barcodeWidth,fontsize){
	var productID=[];	
	var Description=[];
	var productImgData=[];
	var batchImgData=[];
	var batchNumber = [];
	var dataLength=data.items.length;
	$.each(data.items, function(i,prodItem){
		var item = prodItem['ITEM'];
		var itemDescription = prodItem['ITEMDESC'];
		var batchNo = prodItem['BATCH'];
		productID.push(item);
		Description.push(itemDescription);
		batchNumber.push(batchNo);
		JsBarcode("#productBarCode",
			item,
			{displayValue: false ,
			orientation: 'landscape',
			format:'CODE128',
			lineColor: "black",
			textPosition: "bottom",
			width:barcodeWidth,
			height:150,
			text:item,
			textAlign:"left",
			textMargin:2,
			fontSize:10,
			background:"",
			lineColor:"",
			marginLeft:1,
			marginRight:0,				
			marginTop:10,
			marginBottom:10,
		});
		productImgData.push($("#productBarCode").attr("src"));
		if(batchNo != ''){
		JsBarcode("#batchBarCode",
			batchNo,
			{displayValue: false ,
			orientation: 'landscape',
			format:'CODE128',
			lineColor: "black",
			textPosition: "bottom",
			width:barcodeWidth,
			height:150,
			text:batchNo,
			textAlign:"left",
			textMargin:2,
			fontSize:10,
			background:"",
			lineColor:"",
			marginLeft:1,
			marginRight:0,				
			marginTop:10,
			marginBottom:10,
		});
		batchImgData.push($("#batchBarCode").attr("src"));
		}
		else{
			batchImgData.push('');
		}
		
		
		});
			var doc;
			doc = new jsPDF('l', 'mm', [100, 50]);
			doc.setFont("Arial Narrow","bold");
			doc.setFontSize(fontsize);
			//doc.lineHeightProportion = 2;
			
		//	doc.text(0, 60, "_________________________________________________________________________________________________________________________________________________________________________");
			if(batchImgData[0] != ''){
			doc.text(11, 4, "Product ID:"+productID[0]);
			doc.addImage(productImgData[0], 'JPEG', 11, 4,85,19);
			doc.text(11, 27, Description[0]);				
			doc.addImage(batchImgData[0], 'JPEG', 11, 28,85,19);
			doc.text(11, 49, "Batch/SN:"+batchNumber[0]);
			}
			else{
				doc.text(11, 4, "Product ID:"+productID[0]);
				doc.addImage(productImgData[0], 'JPEG', 11,3,85,45);
				doc.text(11, 49, Description[0]);
			}
			for(var i=1;i<dataLength;i++){
				doc.addPage();
				if(batchImgData[i] != ''){
					doc.text(11, 4, "Product ID: "+productID[i]);
					doc.addImage(productImgData[i], 'JPEG', 11, 4,85,19);
					doc.text(11, 27, "Desc: "+productID[i]);
					doc.addImage(batchImgData[i], 'JPEG', 11, 28,85,19);
					doc.text(11, 49, "Batch/SN:"+batchNumber[i]);
				}
				else{
					doc.text(11, 4, "Product ID:"+productID[i]);
					doc.addImage(productImgData[i], 'JPEG', 11,3,85,45);
					doc.text(11, 49, Description[i]);
				}
			}
			data = [];
			doc.save("BarCodeManual100x50.pdf");
	
}

	
</script>
<%@ include file="footer.jsp"%>