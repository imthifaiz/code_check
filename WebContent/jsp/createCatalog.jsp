<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Create Catalog";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
 	subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
	function onClear()
	{
		 document.form.PRODUCTID.value="";
		document.form.PRICE.value="";
		document.form.DESCRIPTION1.value="";
		document.form.DESCRIPTION2.value="";
		document.form.DESCRIPTION3.value="";
		document.form.IMAGE_UPLOAD.value="";
		document.form.DETAIL1.value="";
		document.form.DETAIL2.value="";
		document.form.DETAIL3.value="";
		document.form.DETAIL4.value="";
		document.form.DETAIL5.value="";
		document.form.DETAIL6.value="";
		document.form.DETAIL7.value="";
	
		//document.form.action="createCatalog.jsp?action=clear";
		//document.form.submit();
	}

	function onAdd(){
		
		if (form.DETAIL1.value.length > 100)
	     {
	    	alert("Detail1 should not exceed 100 characters !");
	    	form.DETAIL1.focus();
	    	return false;
	     }
		if (form.DETAIL2.value.length > 100)
	     {
	    	alert("Detail2 should not exceed 100 characters !");
	    	form.DETAIL2.focus();
	    	return false;
	     }
		if (form.DETAIL3.value.length > 100)
	     {
	    	alert("Detail3 should not exceed 100 characters !");
	    	form.DETAIL3.focus();
	    	return false;
	     }
		if (form.DETAIL4.value.length > 100)
	     {
	    	alert("Detail4 should not exceed 100 characters !");
	    	form.DETAIL4.focus();
	    	return false;
	     }
		if (form.DETAIL5.value.length > 100)
	     {
	    	alert("Detail5 should not exceed 100 characters !");
	    	form.DETAIL5.focus();
	    	return false;
	     }
		if (form.DETAIL6.value.length > 100)
	     {
	    	alert("Detail6 should not exceed 100 characters !");
	    	form.DETAIL6.focus();
	    	return false;
	     }
		if(document.form.IMAGE_UPLOAD.value.length<0){
			alert('Please browse image');
   	   	    return false;
	   		
		}
		else
			
		{
   			document.form.action  = "/track/CatalogServlet?Submit=CREATE_CATALOG";
   		    document.form.submit();
   	        return true;
   	    }
   	
	}
	function onviewImage()
	{
		var plant = document.form.PLANT.value;
		var productid = document.form.PRODUCTID.value;
		var imgupload = document.form.IMAGE_UPLOAD.value;
		if(productid.length>0){
			document.form.action  = "/track/CatalogServlet?Submit=PREVIEW_IMAGE"+"&PRODUCTID="+productid;
	//window.location="list/catalogImage.jsp?PLANT="+plant+"&PRODUCTID="+productid;		
		 document.form.submit();
		}
		if(productid.length<0){
			alert('Please enter productid');
			return false;
		}
		if(imgupload.length<0)	{
			alert('Please browse image');
			return false;
		}
				
	}     
	
        
        
        function limitText(limitField, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} 
        }
	
</script>

<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
        
	String res = "",sNewEnb="enabled",sView ="disabled";
	
	String action = "";
	
	String RefNo   = "", Terms  = "", TermsDetails   = "",SoNo ="",Item ="",catlogpath="",imagePath="";
	String description1 = "",description2="",description3="", OrderQty  = "",productid="",price="",DETAILHEADING="";
	StringBuffer detail1,detail2,detail3,detail4,detail5,detail6,detail7; 
	StrUtils strUtils = new StrUtils();
	detail1 = new StringBuffer();
	detail2 = new StringBuffer();
	detail3 = new StringBuffer();
	detail4 = new StringBuffer();
	detail5 = new StringBuffer();
	detail6 = new StringBuffer();
	detail7 = new StringBuffer();
	HashMap catalogmp = new HashMap();
	res =  strUtils.fString(request.getParameter("result"));
	CatalogUtil catlogUtil = new CatalogUtil();
	Hashtable ht = new Hashtable();
	ht.put(IConstants.PLANT,plant);
    Hashtable htcratlog = new Hashtable();    
      
	try {				
			action = strUtils.fString(request.getParameter("action"));
			DETAILHEADING="More Details";
			if (action.equalsIgnoreCase("view")) {	
		if((Hashtable)session.getAttribute("HTCRATLOG")!=null)
		{
System.out.println("view page"+action);				
		htcratlog = (Hashtable)session.getAttribute("HTCRATLOG");
		System.out.println("view page"+action+htcratlog.get(IDBConstants.PRODUCTID));			
		productid = (String)htcratlog.get(IDBConstants.PRODUCTID);
		price = (String)htcratlog.get(IDBConstants.CATLOGPRICE);
		description1 = (String)htcratlog.get(IDBConstants.DESCRIPTION1);
		description2 = (String)htcratlog.get(IDBConstants.DESCRIPTION2);
		description3 = (String)htcratlog.get(IDBConstants.DESCRIPTION3);
		catlogpath = (String)htcratlog.get(IDBConstants.CATLOGPATH);
		detail1.append(htcratlog.get(IDBConstants.DETAIL1));
		detail2.append(htcratlog.get(IDBConstants.DETAIL2));
		detail3.append(htcratlog.get(IDBConstants.DETAIL3));
		detail4.append(htcratlog.get(IDBConstants.DETAIL4));
		detail5.append(htcratlog.get(IDBConstants.DETAIL5));
		detail6.append(htcratlog.get(IDBConstants.DETAIL6));
		detail7.append(htcratlog.get(IDBConstants.DETAIL7));	
		//DETAILHEADING = (String)htcratlog.get(IDBConstants.DETAILLABEL);
		DETAILHEADING="";
			}
		}
		if (action.equalsIgnoreCase("clear")) {
			htcratlog = (Hashtable)session.getAttribute("HTCRATLOG");
			htcratlog.clear();
			session.setAttribute("HTCRATLOG",htcratlog);
			
		}  	
		
	} catch (Exception e) {
		throw e;
	}

	
	
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form" method="post" enctype="multipart/form-data" action="">
    <input type="hidden" name="PLANT" value="<%=plant%>">
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Product Brand ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product ID:</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input class="form-control" type="text" name="PRODUCTID" value="<%=productid%>" size="50" MAXLENGTH=50 >
   		 	<span class="input-group-addon"  
   		 	onClick="javascript:popUpWin('list/mobileItemList.jsp?ITEM='+form.PRODUCTID.value);">
   		 	<a href="#"  data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  	      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Description1:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="DESCRIPTION1" value="<%=description1 %>" size="50" MAXLENGTH=200>
      </div>
    </div>
    <input type ="hidden" class="form-control" type="text" name="PRICE" size="50" MAXLENGTH=35 value="<%=StrUtils.currencyWtoutCommSymbol(price)%>">
    <!--  <div class="form-group">
      <label class="control-label col-sm-4" for="Price">Price:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="PRICE" size="50" MAXLENGTH=35 value="<%=StrUtils.currencyWtoutCommSymbol(price)%>">
      </div>
    </div>-->
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Description2:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="DESCRIPTION2" value="<%=description2 %>" size="50" MAXLENGTH=200>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Description3:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="DESCRIPTION3" value="<%=description3 %>" size="50" MAXLENGTH=200>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="Upload Image">Upload Image:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="IMAGE_UPLOAD" type="File" size="20" MAXLENGTH=100>
      </div>
    <div class="form-inline">
     <INPUT class="Submit btn btn-default" type="BUTTON" value="Preview Catalog Image" onClick="onviewImage();" >
    </div>
   
    </div>
    <!--  <div class="form-group">
      <div class="col-sm-2">          
         <center><font style="color: #2B547E"><b>*Upload Images Only </b></font></center>
      </div>
    </div>-->
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">More Details Label:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="DETAILHEADING" value="<%=DETAILHEADING%>" size="50" MAXLENGTH=50>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details1:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL1"  id="DETAIL1" cols="40" rows="2" wrap="hard" onKeyDown="limitText(this.form.DETAIL1,100);" ><%=detail1.toString()%></textarea>&nbsp;&nbsp;
            </div>
    <div class="form-inline">
    <font style="color: #2B547E"><b>Maximum 100 Characters</b></font>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details2:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL2"  cols="40" rows="2" wrap="hard" onKeyDown="limitText(this.form.DETAIL2,100);" ><%=detail2.toString()%></textarea>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details3:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL3"  cols="40" rows="2"  wrap="hard" onKeyDown="limitText(this.form.DETAIL3,100);" ><%=detail3.toString()%></textarea>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details4:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL4"  cols="40" rows="2" wrap="hard" onKeyDown="limitText(this.form.DETAIL4,100);"  ><%=detail4.toString()%></textarea>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details5:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL5"  cols="40" rows="2" wrap="hard" onKeyDown="limitText(this.form.DETAIL5,100);"  ><%=detail5.toString()%></textarea>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details6:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL6"  cols="40" rows="2" wrap="hard" onKeyDown="limitText(this.form.DETAIL6,100);" ><%=detail6.toString()%></textarea>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details7:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL7"  cols="40" rows="1"  onKeyDown="limitText(this.form.DETAIL7,40);"><%=detail7.toString()%></textarea>&nbsp;&nbsp;
              </div>
    <div class="form-inline">
    
    <font style="color: #2B547E"><b>Web Address Only</b></font>
    </div>
   
    </div>
        
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      
        <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" ><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
      	
      </div>
    </div>
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<script type="text/javascript">

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

