<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Alternate Brand Product";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Alternate Brand Product', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

</script>
<SCRIPT LANGUAGE="JavaScript">
function onClear()
{
	document.form.ITEM.value="";
	document.form.DESC.value="";
	document.form.PRD_BRAND_ID.value="";
	document.form.ALTERNATEBRANDITEM.value="";
    document.form.ALTERNATEBRANDDESC.value="";
	document.form.ALTERNATE_PRD_BRAND_ID.value="";
	}
function onAdd(){
	
	 //var frmRoot=document.form
	 var ITEM   = document.form.ITEM.value;
	 var ALTERNATEBRANDITEM = document.form.ALTERNATEBRANDITEM.value;
	 
    if(ITEM == "" || ITEM == null)
    {
      alert("Please key in Product ID.");
      document.form.ITEM.focus(); 
      return false;
    }
   
    else if(ALTERNATEBRANDITEM == "" || ALTERNATEBRANDITEM == null)
    {
   	 alert("Please key in Alternate Brand Product ID.");
   	 document.form.ALTERNATEBRANDITEM.focus(); return false;
    }
    
    else if(ITEM == ALTERNATEBRANDITEM)
    {
      alert("Please choose Alternate Product ID different from Product ID."); 
      document.form.ALTERNATEBRANDITEM.focus(); return false;
    }
    else
    {
	   document.form.action  = "editalternatebranditem.jsp?action=DELETE";
	   document.form.submit();
	  // return true;
   }
}



</script>
<%
	session = request.getSession();
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	String res = "";
    String sItemEnb   = "enabled";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "disabled";
	String sUpdateEnb = "enabled";
	sAddEnb = "enabled";
	String action = "";
	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	ItemUtil itemUtil = new ItemUtil();
	itemUtil.setmLogger(mLogger);
	DateUtils dateutils = new DateUtils();
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	String sItemId     = StrUtils.fString(request.getParameter("ITEM"));
	String sItemDesc      = StrUtils.fString(request.getParameter("DESC"));
	String productBrandID    = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
	String sAlternateBrandItem    = StrUtils.fString(request.getParameter("ALTERNATEBRANDITEM"));
	String sAlternateBrandItemDesc      = StrUtils.fString(request.getParameter("ALTERNATEBRANDDESC"));
	String alternateProductBrandID    = StrUtils.fString(request.getParameter("ALTERNATE_PRD_BRAND_ID"));
	ItemMstUtil itemMstUtil = new ItemMstUtil();
	
	if (action.equalsIgnoreCase("Clear")) {
		sItemId = "";
		sItemDesc = "";
		productBrandID="";
		sAlternateBrandItem  = "";
		sAlternateBrandItemDesc   = "";
		alternateProductBrandID ="";
	}else if (action.equalsIgnoreCase("DELETE")) {
		   
		boolean validateItem=itemMstUtil.isValidAlternateBrand(sPlant, sItemId);
		boolean validateAlternateBrandItem=itemMstUtil.isValidAlternateBrand(sPlant, sAlternateBrandItem);
		if(validateItem==false)
		{
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Scan/Enter a Valid Product ID</font>";
		}
		else if(validateAlternateBrandItem==false)
		{
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Scan/Enter a Valid Alternate Brand Product ID</font>";
		}
		else
		{
			
			    Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, sPlant);
				ht.put(IDBConstants.ITEM, sItemId);
				ht.put(IDBConstants.PRDBRANDID, productBrandID);
				ht.put(IDBConstants.ALTERNATE_ITEM_NAME, sAlternateBrandItem);
				ht.put(IDBConstants.ALTERNATEPRDBRANDID, alternateProductBrandID);
						
				MovHisDAO mdao = new MovHisDAO(sPlant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", sPlant);
				htm.put("DIRTYPE", TransactionConstants.DELETE_ALTERNATE_BRAND_PRODUCT);
				htm.put("RECID", "");
				htm.put("ITEM",sAlternateBrandItem);
				htm.put("REMARKS",strUtils.InsertQuotes("Main Product:" +sItemId));
				htm.put("UPBY", sUserId);
				htm.put("CRBY", sUserId);
				htm.put("CRAT", dateutils.getDateTime());
				htm.put("UPAT", dateutils.getDateTime());
				htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
				boolean itemDeleted = itemUtil.deleteAlternateBrandItem(ht);
				boolean inserted = mdao.insertIntoMovHis(htm);
	
				if (itemDeleted  && inserted) {
					res = "<font class = " + IDBConstants.SUCCESS_COLOR
							+ ">Alternate Brand Product Deleted Successfully</font>";
	
							
				} else {
					res = "<font class = " + IDBConstants.FAILED_COLOR
							+ ">Failed to Delete Alternate Brand Product</font>";
	
				}
			
		}

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
  <form class="form-horizontal" name="form" method="post" onSubmit="return onCheck();">
        <div class="form-group">
	      	<label class="control-label col-sm-4" for="Product ID">
	      	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product ID:</label>
	      	<div class="col-sm-3">
	      	<div class="input-group">
	    	<INPUT class="form-control" name="ITEM" Id="ITEM" type="TEXT" value="<%=sItemId%>" size="20" 
				MAXLENGTH=50 <%=sItemEnb%>> 
   			<span class="input-group-addon"  onClick="javascript:popUpWin('alternatebranditemlist.jsp?ITEM='+form.ITEM.value+'&TYPE=MAINITEM');" >
   			<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
	   		<i class="glyphicon glyphicon-log-in"  style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		</div>
  		
  		 <div class="form-group">
  		   <label class="control-label col-sm-4" for="Product Description">Product Description:</label>
	 	     <div class="col-sm-3">
	      	<div class="input-group">
	    	<input name="DESC" type="TEXT" readonly value="<%=sItemDesc%>" 	size=50  MAXLENGTH=100 class="form-control">
  		</div>
  		</div>
  		</div>
  		
  		 <div class="form-group">
	      	
	      	<label class="control-label col-sm-4" for="Product Brand">Product Brand ID:</label>
	      	<div class="col-sm-3">
	      	<div class="input-group">
	    	<input name="PRD_BRAND_ID" type="TEXT" readonly value="<%=productBrandID%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	
  		</div>
  		</div>
  		</div>
  		
  		<div class="form-group">
	      	<label class="control-label col-sm-4" for="Alternate Brand Product ID">
	      	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Alternate Brand Product ID:</label>
	      	<div class="col-sm-3">
	      	<div class="input-group">
	    	<INPUT class="form-control" name="ALTERNATEBRANDITEM" type="TEXT" value="<%=sAlternateBrandItem%>" size="20" 
				MAXLENGTH=50 <%=sItemEnb%>> 
   			<span class="input-group-addon"  onClick="javascript:popUpWin('alternatebranditemlist.jsp?ITEM='+form.ALTERNATEBRANDITEM.value+'&TYPE=ALTERNATEITEM');" >
   			<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
	   		<i class="glyphicon glyphicon-log-in"  style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		</div>
  		
  		 <div class="form-group">
  		    <label class="control-label col-sm-4" for="Alternate Brand Description">Alternate Brand Description:</label>
	         	<div class="col-sm-3">
	      	<div class="input-group">
	    	<input name="ALTERNATEBRANDDESC" type="TEXT" readonly value="<%=sAlternateBrandItemDesc%>"	size=50 MAXLENGTH=100 class="form-control">
  		</div>
  		</div>
  		</div>
  		
  		 <div class="form-group">
  		 <label class="control-label col-sm-4" for="Alternate Product Brand">Alternate Product Brand ID:</label>
	      <div class="col-sm-3">
	      	<div class="input-group">
	    	<input name="ALTERNATE_PRD_BRAND_ID" type="TEXT" readonly value="<%=alternateProductBrandID%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	
  		</div>
  		</div>
  		</div>
     
  
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"  <%=sAddEnb%>><b>Delete</b></button>&nbsp;&nbsp;
      	

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

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>



