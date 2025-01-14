<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@page import="com.track.util.http.HttpUtils"%>
<%
String title = "Bill Of Materials Formula Detail";
String rootURI = HttpUtils.getRootURI(request);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="<%=rootURI %>/jsp/js/jspdf.debug.js"></script>
<script src="<%=rootURI %>/jsp/js/jspdf.plugin.autotable.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'ProductionBOM', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",pitem="",pdescitem="",fieldDesc="",allChecked="",pItemDesc="",Puom="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
pitem  = strUtils.fString(request.getParameter("ITEM"));
pItemDesc  = strUtils.fString(request.getParameter("DESC"));
Puom  = strUtils.fString(request.getParameter("PUOM"));


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
  
  
}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../billofmaterials/summary"><span class="underline-on-hover">Bill Of Materials Formula Summary</span></a></li>                       
                <li><label>Bill Of Materials Formula Detail</label></li>                                   
            </ul>
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class=" pull-right">
				<div class="btn-group" role="group">
				<button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
				</button>
				<button type="button" class="btn btn-default" onclick="generatePrint()"
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
              	</div>
              	<div class="btn-group" role="group">
					<button type="button" class="btn btn-default" data-toggle="dropdown" >More 
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu" style="min-width: 0px;">
						<li id=""><a href="../billofmaterials/copy?ITEM=<%=pitem%>&DESC=<%=pItemDesc%>&PUOM=<%=Puom%>">Copy</a></li>
				  	</ul>
				</div>
				
              	<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../billofmaterials/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
              
             <!--  <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../billofmaterials/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1> -->
		</div>
		
 <div class="container-fluid">

  <%--  <CENTER><strong><%=res%></strong></CENTER> --%>

  <form class="form-horizontal" name="form" method="post" action="/track/ProductionBomServlet?">
  <INPUT type="hidden" name="RFLAG" value="2">
  <INPUT type="hidden" name="BATCH_0" value="">
  
  <div id = "ERROR_MSG"></div>
  
    <div class="form-group">
     <label class="control-label col-form-label col-sm-2 required">Parent Product</label>
     <div class="col-sm-4 ac-box">
				<div class="input-group">  
      	<INPUT type="hidden" name="plant" value=<%=plant%>>   
    		<input type="TEXT"  name="ITEM" id="ITEM"	value="<%=pitem%>"	onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}"
			size="20" MAXLENGTH=100 class="form-control">
			<span class="select-icon" style="right:10px;" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	<!-- <span class="btn-danger input-group-addon"
   		   onClick="javascript:popUpWin('../jsp/ProductionBOMPitemList.jsp?ITEM='+form.ITEM.value+'&TYPE=KITBOM');"> 	<span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
   		 	
  		</div>
  		</div>
  	</div>
  	
  	  	  <div class="form-group">
				<label class="control-label col-form-label col-sm-2">Parent Product Description:</label>
				<div class="col-sm-4">
					<input type="text" readonly class="form-control" id="ITEM_DESC" name="ITEM_DESC" value="<%=pItemDesc%>" >
				</div>
		  </div>  
  	
    
     <div class="form-group">
      <label class="control-label col-form-label col-sm-2  ">Parent Quantity</label>
     <div class="col-sm-4 ac-box">
				<div class="input-group">         
         <INPUT  class="form-control" name="PARENTQTY" type="TEXT" id="PARENTQTY" value="1" size="4" MAXLENGTH=50 readonly>
		</div>	
      </div>

      
      <div class="form-inline">        
       	<button type="button" class="Submit btn btn-default" leftalign="center" onClick="onGo(1)">View</button>&nbsp;&nbsp; 	
      </div>
      </div>
   
    
   
    <br>
    <div class="form-group">        
      <div class="col-sm-12" align="center">
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      </div>
 </div>
 
   
       
    <div id="RESULT_MESSAGE">
          <table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
	     <%-- <tr><td align="center"><%=fieldDesc%></td></tr> --%>
	</table>
     </div>
     
     <br>
 
<!-- <div class="panel panel-default">
<div class="panel-heading" style="background-color: #ffffff  " align="center">
<h3 class="panel-title">Child Product Summary</h3> 
</div>
</div> -->

     
     

<div class="form-group">
      
      <div class="col-sm-2" hidden>    
      <label class="checkbox-inline">      
        <INPUT Type=Checkbox  class="form-check-input" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);"
			size="50" MAXLENGTH=100 onclick="DisplayAddress();">Select/Unselect</label>
			</div>
    </div>
		
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
   <div class="form-group">        
      <div class="col-sm-12" align="center" hidden>
					<button class="Submit btn btn-default" type="button" onClick="onDelete()">Remove from  BOM</button>
					</div>
					</div>
	
<input type="hidden" name="PTYPE" id="PTYPE" value="CREATEKITBOM">



    
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    var plant= '<%=plant%>'; 
    /* Product Number Auto Suggestion */
    $('#ITEM').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEM',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				//ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ACTION : "GET_PRODUCT_LIST_AUTO_SUGGESTION",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.items);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
				return '<div><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Stock On Hand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.UOM+' </p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
  

});
</script>


<script>

	
onGo(0);
function checkAll(isChk)
{
	var len = document.form.chkitem.length;
	 var orderLNo; 
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
function validateProduct() {
	var productId = document.form.ITEM.value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
	}
	}
function onGo(index) {

	var index = index;
	var product = document.form.ITEM.value;
	//var childproduct = document.form.CITEM.value;
	
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
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,action: "VIEW_KITBOM_DETAILS_FOR_DETAIL",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
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
	  	  document.form.action="/track/ProductionBomServlet?action=DELETE_KITBOM";
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
		
        $.each(data.kittingbom, function(i,item){
                   
        	outPutdata = outPutdata+item.KITBOMDATA;
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
        return '<TABLE class="table table-bordred table-striped" id="table1">'+
        	   '<tr style="background:#ffffff ">'+
         		/* '<th width="5%"><font >Select</font></th>'+ */
         		'<th width="5%"  style="text-align: center;"><font >No</font></th>'+
         		'<th width="10%" style="text-align: center;"><font >Child Product</font></th>'+
         		'<th width="11%" style="text-align: center;"><font >Child Product Desc</font></th>'+
         		'<th width="13%" style="text-align: center;"><font >Child Product Detail Desc</font></th>'+
         		'<th width="10%" style="text-align: center;">Child UOM</th>'+
         		/* '<th width="10%"><font >Equivalent Product</font></th>'+
         		'<th width="11%"><font >Equivalent Product Desc</font></th>'+
         		'<th width="15%"><font >Equivalent Product Detail Desc</font></th>'+ */
         		'<th width="10%" style="text-align: center;"><font >Child BOM QTY</font></th>'+
         		'</tr>';
       
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	function generate() {
	
	var img = toDataURL($("#logo_content").attr("src"),
			function(dataUrl) {
				alert("ok");
				generatePdf(dataUrl);
		  	},'image/jpeg');
		
	}
	
	function toDataURL(src, callback, outputFormat) {
		  var img = new Image();
		  img.crossOrigin = 'Anonymous';
		  img.onload = function() {
		    var canvas = document.createElement('CANVAS');
		    var ctx = canvas.getContext('2d');
		    var dataURL;
		    canvas.height = this.naturalHeight;
		    canvas.width = this.naturalWidth;
		    ctx.drawImage(this, 0, 0);
		    dataURL = canvas.toDataURL(outputFormat);
		    callback(dataURL);
		  };
		  img.src = src;
		  if (img.complete || img.complete === undefined) {
		    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
		    img.src = src;
		  }
	}
	
	function generate(){
		var doc = new jsPDF('p', 'mm', 'a4');
		var pageNumber;
		var cwidth = doc.internal.pageSize.getWidth()/2;
		var pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
		var pageWidth = doc.internal.pageSize.width || doc.internal.pageSize.getWidth();

		/* Top Left */
		var rY=25;
		/*From Address*/
		doc.setFontSize(15);
		doc.setFontStyle("bold");
		doc.text('Bill Of Materials Formula Detail',cwidth,rY+2,'center');
		doc.setFontSize(12);
		doc.setFontStyle("normal");
		doc.text('Parent Product   : '+$("input[name ='ITEM']").val(), 16, rY+=15);
		doc.text('Parent Quantity  : '+$("input[name ='PARENTQTY']").val(), 16, rY+=10);
		doc.setFontSize(10);
		
		var totalPagesExp = "{total_pages_cont_string}";
		doc.fromHTML(document.getElementById('table'));
		doc.autoTable({
			html : '#table1',
			startY : rY+=10,
			headStyles : {
				fillColor : [ 0, 0, 0 ],
				textColor : [ 255, 255, 255 ],
				halign : 'center',
				fontSize : 10
			},
			bodyStyles : {
				fillColor : [ 255, 255, 255 ],
				textColor : [ 0, 0, 0 ],
				fontSize : 10
			},
			footStyles : {
				fillColor : [ 255, 255, 255 ],
				textColor : [ 0, 0, 0 ],
				fontStyle : 'normal',
				halign : 'right'
			},
			theme : 'grid',
			columnStyles: {0: {halign: 'center'},1: {halign: 'center'},2: {halign: 'center'},3: {halign: 'center'},4: {halign: 'center'},5: {halign: 'center'}},
			didDrawPage : function(data) {
				doc.setFontStyle("normal");
				// Footer
				pageNumber = doc.internal.getNumberOfPages();
				var str = "Page " + doc.internal.getNumberOfPages();
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					str = str + " of " + totalPagesExp;
				}
				doc.setFontSize(10);

				// jsPDF 1.4+ uses getWidth, <1.4 uses .width
				var pageSize = doc.internal.pageSize;
				var pageHeight = pageSize.height ? pageSize.height
						: pageSize.getHeight();
				doc.text(str, 185,
						pageHeight - 10);
			},
			didParseCell : function(data) {						
				for(i=0;i<data.table.body.length;i++){
					
				}
			}
		});
		
		doc.setFontStyle("normal");

		if(pageNumber < doc.internal.getNumberOfPages()){
			// Footer
			var str = "Page " + doc.internal.getNumberOfPages()
			// Total page number plugin only available in jspdf v1.0+
			if (typeof doc.putTotalPages === 'function') {
				str = str + " of " + totalPagesExp;
			}
			doc.setFontSize(10);

			// jsPDF 1.4+ uses getWidth, <1.4 uses .width
			var pageSize = doc.internal.pageSize;
			var pageHeight = pageSize.height ? pageSize.height
					: pageSize.getHeight();
			doc.text(str, 185, pageHeight - 10);
		}
		
		if(pageNumber == doc.internal.getNumberOfPages()){
			// Footer
			doc.setFontSize(10);
			var pageSize = doc.internal.pageSize;
			var pageHeight = pageSize.height ? pageSize.height : pageSize.getHeight();
			
		}
		// Total page number plugin only available in jspdf v1.0+
		if (typeof doc.putTotalPages === 'function') {
			doc.putTotalPages(totalPagesExp);
		}
		doc.save('BillOfMaterial.pdf');
	}
	
	function generatePrint(){
		var doc = new jsPDF('p', 'mm', 'a4');
		var pageNumber;
		var cwidth = doc.internal.pageSize.getWidth()/2;
		var pageHeight = doc.internal.pageSize.height || doc.internal.pageSize.getHeight();
		var pageWidth = doc.internal.pageSize.width || doc.internal.pageSize.getWidth();

		/* Top Left */
		var rY=25;
		/*From Address*/
		doc.setFontSize(15);
		doc.setFontStyle("bold");
		doc.text('Bill Of Materials Formula Detail',cwidth,rY+2,'center');
		doc.setFontSize(12);
		doc.setFontStyle("normal");
		doc.text('Parent Product   : '+$("input[name ='ITEM']").val(), 16, rY+=15);
		doc.text('Parent Quantity  : '+$("input[name ='PARENTQTY']").val(), 16, rY+=10);
		doc.setFontSize(10);
		
		var totalPagesExp = "{total_pages_cont_string}";
		doc.fromHTML(document.getElementById('table'));
		doc.autoTable({
			html : '#table1',
			startY : rY+=10,
			headStyles : {
				fillColor : [ 0, 0, 0 ],
				textColor : [ 255, 255, 255 ],
				halign : 'center',
				fontSize : 10
			},
			bodyStyles : {
				fillColor : [ 255, 255, 255 ],
				textColor : [ 0, 0, 0 ],
				fontSize : 10
			},
			footStyles : {
				fillColor : [ 255, 255, 255 ],
				textColor : [ 0, 0, 0 ],
				fontStyle : 'normal',
				halign : 'right'
			},
			theme : 'grid',
			columnStyles: {0: {halign: 'center'},1: {halign: 'center'},2: {halign: 'center'},3: {halign: 'center'},4: {halign: 'center'},5: {halign: 'center'}},
			didDrawPage : function(data) {
				doc.setFontStyle("normal");
				// Footer
				pageNumber = doc.internal.getNumberOfPages();
				var str = "Page " + doc.internal.getNumberOfPages();
				// Total page number plugin only available in jspdf v1.0+
				if (typeof doc.putTotalPages === 'function') {
					str = str + " of " + totalPagesExp;
				}
				doc.setFontSize(10);

				// jsPDF 1.4+ uses getWidth, <1.4 uses .width
				var pageSize = doc.internal.pageSize;
				var pageHeight = pageSize.height ? pageSize.height
						: pageSize.getHeight();
				doc.text(str, 185,
						pageHeight - 10);
			},
			didParseCell : function(data) {						
				for(i=0;i<data.table.body.length;i++){
					
				}
			}
		});
		
		doc.setFontStyle("normal");

		if(pageNumber < doc.internal.getNumberOfPages()){
			// Footer
			var str = "Page " + doc.internal.getNumberOfPages()
			// Total page number plugin only available in jspdf v1.0+
			if (typeof doc.putTotalPages === 'function') {
				str = str + " of " + totalPagesExp;
			}
			doc.setFontSize(10);

			// jsPDF 1.4+ uses getWidth, <1.4 uses .width
			var pageSize = doc.internal.pageSize;
			var pageHeight = pageSize.height ? pageSize.height
					: pageSize.getHeight();
			doc.text(str, 185, pageHeight - 10);
		}
		
		if(pageNumber == doc.internal.getNumberOfPages()){
			// Footer
			doc.setFontSize(10);
			var pageSize = doc.internal.pageSize;
			var pageHeight = pageSize.height ? pageSize.height : pageSize.getHeight();
			
		}
		// Total page number plugin only available in jspdf v1.0+
		if (typeof doc.putTotalPages === 'function') {
			doc.putTotalPages(totalPagesExp);
		}
		doc.autoPrint();
		const hiddFrame = document.createElement('iframe');
		hiddFrame.style.position = 'fixed';
		hiddFrame.style.width = '1px';
		hiddFrame.style.height = '1px';
		hiddFrame.style.opacity = '0.01';
		const isSafari = /^((?!chrome|android).)*safari/i.test(window.navigator.userAgent);
		if (isSafari) {
		  // fallback in safari
		  hiddFrame.onload = () => {
		    try {
		      hiddFrame.contentWindow.document.execCommand('print', false, null);
		    } catch (e) {
		      hiddFrame.contentWindow.print();
		    }
		  };
		}
		hiddFrame.src = doc.output('bloburl');
		document.body.appendChild(hiddFrame);
	}

	
</script> 


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>