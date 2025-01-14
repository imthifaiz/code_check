<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Edit Catalog";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>

<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
 	subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function popUpMobWin(URL) {
 	subWin = window.open(URL, 'Catalog', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=350,height=500,left = 420,top = 140');
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
		else
			
		{
     	
     		document.form.action  = "/track/CatalogServlet?Submit=EDIT_CATALOG";
     		
       	    document.form.submit();
       	   
       	    return true;
			
		}
         		
	}

	//Start code added by Deen for DEL Catalog on 12th july 2012
	function onDelete() {

		var ITEM = document.form.PRODUCTID;
		if (ITEM.value == "" || ITEM == null) {
			alert("Please Key in Prdocuct ID");
			document.form.PRODUCTID.focus();
			return false;
		}
		var delmsg = confirm("Are you sure you would like to delete?");
		if (delmsg) {
			document.form.action = "/track/CatalogServlet?Submit=DEL_CATALOG&PRODUCTID="
					+ ITEM.value;
			document.form.submit();
		} else {
			return false;
		}
	}
	//End code added by Deen for DEL Catalog on 12th july 2012

	function onView(){
		   var CUST_CODE   = document.form.PRODUCTID.value;
		   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Product ID"); return false; }
		 else{
		 document.form.action  = "editCatalog.jsp?action=VIEW&PRODUCTID="+CUST_CODE;
		 document.form.submit();}
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
        
	String res = "",sNewEnb="enabled";
	
	String action = "",detailsPage="";
	StringBuffer detail1,detail2,detail3,detail4,detail5,detail6,detail7; 
	StrUtils strUtils = new StrUtils();
	detail1 = new StringBuffer();
	detail2 = new StringBuffer();
	detail3 = new StringBuffer();
	detail4 = new StringBuffer();
	detail5 = new StringBuffer();
	detail6 = new StringBuffer();
	detail7 = new StringBuffer();
	
	String RefNo   = "", Terms  = "", TermsDetails   = "",sQuery="",SoNo ="",Item ="",isActive="";
	String description1 = "",description2="",description3="", catlogPath="",OrderQty  = "",productid="",price="";
	
	res =  strUtils.fString(request.getParameter("result"));
	InstructionUtil insUtil = new InstructionUtil();
	Hashtable ht = new Hashtable();
	ht.put(IConstants.PLANT,plant);
	isActive= strUtils.fString(request.getParameter("ISACTIVE"));  
         
        //System.out.println("stepq"+step1.toString());          
	CatalogUtil catlogUtil = new CatalogUtil();
		Hashtable htsel = new Hashtable();
		htsel.put(IConstants.PLANT,plant);
		
	try {
		action = strUtils.fString(request.getParameter("action"));
		
		if (action.equalsIgnoreCase("Clear")) {
			action = "";
				}
		  if(action.equalsIgnoreCase("editvalues")){
		     	 
 	        	 Hashtable htvalues = (Hashtable)session.getAttribute("EDITLIST");
 	        	 productid = (String)htvalues.get(IDBConstants.PRODUCTID);
 	        	 description1 = (String)htvalues.get(IDBConstants.DESCRIPTION1);
 	        	 price = (String)htvalues.get(IDBConstants.CATLOGPRICE);
 	        	 description2 = (String)htvalues.get(IDBConstants.DESCRIPTION2);
 	        	 description3 = (String)htvalues.get(IDBConstants.DESCRIPTION3);
 	        	 detail1.append(htvalues.get(IDBConstants.DETAIL1));
 	        	 detail2.append(htvalues.get(IDBConstants.DETAIL2));
 	        	 detail3.append(htvalues.get(IDBConstants.DETAIL3));
 	        	 detail4.append(htvalues.get(IDBConstants.DETAIL4));
 	        	 detail5.append(htvalues.get(IDBConstants.DETAIL5));
 	        	 detail6.append(htvalues.get(IDBConstants.DETAIL6));
 	        	 detail7.append(htvalues.get(IDBConstants.DETAIL7));
 	        	
			  	         }
		if(action.equalsIgnoreCase("VIEW"))
		{
			productid = request.getParameter("PRODUCTID");
			htsel.put(IConstants.PRODUCTID,productid);
		sQuery ="ISNULL(DETAIL1,'') DETAIL1,ISNULL(DETAIL2,'') DETAIL2,ISNULL(DETAIL3,'') DETAIL3,ISNULL(DETAIL4,'') DETAIL4,";
		sQuery = sQuery +"ISNULL(DETAIL5,'') DETAIL5,ISNULL(DETAIL6,'') DETAIL6,ISNULL(DETAIL7,'') DETAIL7,CATLOGID, PRODUCTID,PRICE,isnull(CATLOGPATH,'') CATLOGPATH ,DESCRIPTION1,DESCRIPTION2,DESCRIPTION3,ISNULL(DETAILLABEL,'') DETAILLABEL,ISACTIVE";
	         List m= catlogUtil.listCatalogDet(sQuery,htsel,"");
	         
	         for(int i=0;i<m.size();i++)
	         {
	        	Map insmp= (Map)m.get(i);
	         detail1.append(insmp.get("DETAIL1"));
	         detail2.append(insmp.get("DETAIL2"));
	         detail3.append(insmp.get("DETAIL3"));
	         detail4.append(insmp.get("DETAIL4"));
	         detail5.append(insmp.get("DETAIL5"));
	         detail6.append(insmp.get("DETAIL6"));
	         detail7.append(insmp.get("DETAIL7"));
	         description1 = StrUtils.formatHTML((String)insmp.get("DESCRIPTION1"));
	         description2 = StrUtils.formatHTML((String)insmp.get("DESCRIPTION2"));
	         description3 = StrUtils.formatHTML((String)insmp.get("DESCRIPTION3"));
               
	         price = StrUtils.currencyWtoutCommSymbol((String)insmp.get(IDBConstants.CATLOGPRICE));
	         isActive= (String)insmp.get(IDBConstants.ISACTIVE);
	         catlogPath = (String)insmp.get(IDBConstants.CATLOGPATH);
	         detailsPage = (String)insmp.get(IDBConstants.DETAILLABEL);
	         }
	       
	         } 
	      
	} catch (Exception e) {
	}

	
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form" method="post" enctype="multipart/form-data" action="">
    <input type="hidden" name="PLANT" value="<%=plant%>">
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Product ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product ID:</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input class="form-control" type="text" name="PRODUCTID" value="<%=productid%>" size="50" MAXLENGTH=50 >
   		 	<span class="input-group-addon"  
   		 	onClick="javascript:popUpWin('list/catalogList.jsp?ITEM='+form.PRODUCTID.value);">
   		 	<a href="#"  data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  	      </div>
<div class="form-inline">
<div class="col-sm-4">
<button type="button" class="Submit btn btn-default" onClick="onView();"><b>View</b></button>
</div>
</div>

    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Description1:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="DESCRIPTION1" value="<%=description1 %>" size="50" MAXLENGTH=200>
      </div>
    </div>
    
    <!-- <div class="form-group">
      <label class="control-label col-sm-4" for="Price">Price:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="PRICE" size="50" MAXLENGTH=35 value="<%=StrUtils.currencyWtoutCommSymbol(price)%>">
      </div>
    </div>-->
     <input type="hidden" class="form-control" type="text" name="PRICE" size="50" MAXLENGTH=35 value="<%=StrUtils.currencyWtoutCommSymbol(price)%>">
    
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
       <div class="col-sm-offset-4 col-sm-8">          
      	<input type="button" class="Submit btn btn-default" value="view catalog Image Details" size="50" onClick="javascript:popUpMobWin('catalogBrowseImage.jsp?PRODUCTID='+document.form.PRODUCTID.value+'&PLANT='+document.form.PLANT.value);">&nbsp;&nbsp;
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="Upload Image">Upload Image:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="IMAGE_UPLOAD" type="File" value="" size="20" MAXLENGTH=100>
          </div>
          <div class="form-inline">
   <!--  <font style="color: #2B547E"><b>Upload Images Only </b></font> -->
    </div>
    </div>
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">More Details Label:</label>
      <div class="col-sm-4">          
        <input class="form-control" type="text" name="DETAILHEADING" value="<%=detailsPage%>" size="50" MAXLENGTH=50>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details1:</label>
      <div class="col-sm-4">          
<textarea class="form-control" name="DETAIL1"  id="DETAIL1"
			cols="40" rows="2" onKeyDown="limitText(this.form.DETAIL1,100);"><%=detail1.toString()%></textarea>            </div>
    <div class="form-inline">
    <font style="color: #2B547E"><b>Maximum 100 Characters</b></font>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details2:</label>
      <div class="col-sm-4">          
		<textarea class="form-control" name="DETAIL2" cols="40" rows="2" wrap ="hard" onKeyDown="limitText(this.form.DETAIL2,100);" ><%=detail2.toString()%></textarea>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details3:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL3"  cols="40" rows="2"  wrap ="hard" onKeyDown="limitText(this.form.DETAIL3,100);"><%=detail3.toString()%></textarea>
      </div>
    </div>
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="More Details Label">Details4:</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="DETAIL4"  cols="40" rows="2"  wrap ="hard" onKeyDown="limitText(this.form.DETAIL4,100);"><%=detail4.toString()%></textarea>
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
	  <textarea class="form-control" name="DETAIL7" cols="55" rows="2"><%=detail7.toString()%></textarea>
			              </div>
    <div class="form-inline">
    <font style="color: #2B547E"><b>Web Address Only</b></font>
    </div>
    </div>
    
     <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y"<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N"<%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div>
        
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      
        <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;  
      	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%> ><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="return onDelete();" ><b>Delete</b></button>&nbsp;&nbsp;
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
function onviewImage()
{
	var plant = document.form.PLANT.value;
	var productid =document.form.PRODUCTID.value; 
	if(productid.length>0){
	//alert(plant+productid);		
 document.form.action  = "catalogImage.jsp?PLANT="+plant+"&PRODUCTID="+productid+"&PAGE=EDIT_LOG";
		
	    document.form.submit();}
	else{
		alert('Please Enter ProductID');
	}
	
}
function onviewEnqImage()
{
	var plant = document.form.PLANT.value;
	var productid =document.form.PRODUCTID.value; 
	if(productid.length>0){
	//alert(plant+productid);		
 document.form.action  = "catalogEnqImage.jsp?PLANT="+plant+"&PRODUCTID="+productid+"&PAGE=EDIT_LOG";
		
	    document.form.submit();}
	else{
		alert('Please Enter ProductID');
	}
	
}   
function onviewRegImage()
{
	var plant = document.form.PLANT.value;
	var productid =document.form.PRODUCTID.value; 
	if(productid.length>0){
	//alert(plant+productid);		
 document.form.action  = "catalogRegisterImage.jsp?PLANT="+plant+"&PRODUCTID="+productid+"&PAGE=EDIT_LOG";
		
	    document.form.submit();}
	else{
		alert('Please Enter ProductID');
	}
	
}    
function onviewBrowseImage()
{
	var plant = document.form.PLANT.value;
	var productid =document.form.PRODUCTID.value; 
	if(productid.length>0){
	//alert(plant+productid);		
 document.form.action  = "catalogBrowseImage.jsp?PLANT="+plant+"&PRODUCTID="+productid+"&PAGE=EDIT_LOG";
		
	    document.form.submit();}
	else{
		alert('Please Enter ProductID');
	}
	
}    
function onViewDetail()
{
	var item = document.getElementById('PRODUCTID').value;
	var urlStr = "/track/CatalogServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		Submit : "VIEW_CATALOG_DETAILS",
		PRODUCTID: item
	},
	dataType : "json",
	success : function(data) {
		if (data.status == "100") {
			
			var resultVal = data.result;
			
			document.form.PRODUCTID.value=resultVal.productid;
			document.getElementById("DESCRIPTION1").value=resultVal.description1;
			document.getElementById("DESCRIPTION2").value=resultVal.description2;
			document.getElementById("DESCRIPTION3").value=resultVal.description3;
			
			//window.location=window.location;
		
		} 
	}
});
	
}

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>