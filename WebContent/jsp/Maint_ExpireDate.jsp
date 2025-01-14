<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Edit Expiry Date";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PURCHASE_TRANSACTION%>"/>
</jsp:include> 

<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script src="https://cdn.jsdelivr.net/npm/autosize@4.0.0/dist/autosize.min.js"></script>
<script language="javascript">

var subWin = null;
function popUpWin(URL) {
  
  subWin = window.open(URL, 'Inventory', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function checkAll(isChk)
{
	var len = document.form1.chkitem.length;
	if(len == undefined) len = 1;  
    if (document.form1.chkitem)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkitem.checked = isChk;
              		 
              	}
              	else{
              		document.form1.chkitem[i].checked = isChk;
              		 
              	}
            	 	
                
        }
    }
}

function onGo(){
document.form1.action="../purchasetransaction/inventoryexpdate";
document.form1.submit();
}
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();

String fieldDesc="";
String USERID ="",PLANT="",LOC ="",  ITEM = "",EXPIREDATE="", BATCH="",PRD_TYPE_ID="",PRD_DEPT_ID="",PRD_BRAND_ID="",PRD_CLS_ID="",PRD_DESCRIP="", QTY ="",
REMARKS="",REASONCODE="";
boolean flag=false;

String PGaction        = strUtils.fString(request.getParameter("PGaction")).trim();
String action        = strUtils.fString(request.getParameter("action")).trim();
String result        = strUtils.fString(request.getParameter("result")).trim();
PLANT= session.getAttribute("PLANT").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
ITEM    = strUtils.fString(request.getParameter("ITEM"));
BATCH   = strUtils.fString(request.getParameter("BATCH"));
PRD_CLS_ID = strUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_TYPE_ID = strUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = strUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_DEPT_ID = strUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
PRD_DESCRIP = strUtils.fString(request.getParameter("PRD_DESCRIP"));
EXPIREDATE = strUtils.fString(request.getParameter("EXPIREDATE"));
REMARKS = strUtils.fString(request.getParameter("REMARKS"));
REASONCODE = strUtils.fString(request.getParameter("REASONCODE"));

boolean cntRec=false;

if(result.equalsIgnoreCase("result"))
{
  fieldDesc=(String)request.getSession().getAttribute("RESULT");
  fieldDesc = "<font class='maingreen'>" + fieldDesc + "</font>";
}
else if(result.equalsIgnoreCase("resulterror"))
{
	fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
	fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
}

if(result.equalsIgnoreCase("catchrerror"))
{
  fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
  fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
  
  
}

%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../purchaseTransactionDashboard"><span class="underline-on-hover">Purchase Transaction Dashboard</span> </a></li>
                <li><label>Edit Expiry Date</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../purchaseTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
		
 <div class="box-body">
<form class="form-horizontal" name="form1" method="post" action="Maint_ExpireDate.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">


<table  border="0" width="100%" cellspacing="1" cellpadding="2"  bgcolor="" align="center">

 	<tr>
 		<td align="center">
 			<font class="maingreen"> <%=fieldDesc%></font>
 		</td>
 	</tr>
 </table>
 
 
<div class="form-group">
  <label class="control-label col-sm-3" for="Product ID">Product ID:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control"  name="ITEM" type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=50>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/item_list_inventory.jsp?ITEM='+form1.ITEM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class="form-inline">
  <label class="control-label col-sm-3" for="Product Department ID">Product Department ID:</label> 
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT" value="<%=PRD_DEPT_ID%>" size="36"  MAXLENGTH=20>
   		 		<span class="select-icon" onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		 	
  		</div>        
       </div>
       </div>
       </div>
       
       
<div class="form-group">
    <label class="control-label col-sm-3" for="Product Class ID">Product Category ID:</label> 
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="20"  MAXLENGTH=20>
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('../jsp/activePrdClsList.jsp?ITEM_ID='+form1.PRD_CLS_ID.value);" >
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class=form-inline>
   <label class="control-label col-sm-3" for="Product Description">Product Description:</label>
     <div class="col-sm-3">
     <div class="input-group">    
             <input class="form-control" name="PRD_DESCRIP" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DESCRIP)%>" size="20"  MAXLENGTH=60>
   		 	</div>
                 </div>
                 </div>
                 
               </div>  
                 
<div class="form-group">
   <label class="control-label col-sm-3" for="Product Type ID">Product Sub Category ID:</label>
      <div class="col-sm-3">
      <div class="input-group">
             <input class="form-control" name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="20"  MAXLENGTH=20>
             <span class="input-group-addon" onClick="javascript:popUpWin('../jsp/activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);" >
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
   		 	</div>
              </div>
 <div class=form-inline>
  <label class="control-label col-sm-3" for="Batch">Batch:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="BATCH" type = "TEXT" value="<%=BATCH%>" size="20"  MAXLENGTH=40>
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('../jsp/batch_list_inventory.jsp?BATCH='+form1.BATCH.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Batch Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
       </div>
 
<div class="form-group">
     <label class="control-label col-sm-3" for="Product Brand ID">Product Brand ID:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" size="20"  MAXLENGTH=20>
    		<input type="hidden" name="PRD_BRAND_DESC" value="">
 		    <INPUT name="ACTIVE" type = "hidden" value="">
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
  <div class="form-inline"> 
  <label class="control-label col-sm-3"  for="Expiry Date">Expiry Date:</label>
   <div class="col-sm-3">
      	<div class="input-group">    
    		<input class="form-control datepicker" name="EXPIREDATE" type="TEXT" readonly="readonly" value="<%=EXPIREDATE%>"
			size="30" MAXLENGTH=80>
   		 	</div>
  		      </div>  
  		      </div>
  		             </div>
  		             
     <div class="col-sm-offset-6">
      <button type="button" class="Submit btn btn-default" value="View"  onClick="javascript:return onGo();"><b>View</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Update"  onClick="onUpdate(document.form)"><b>Update</b></button>&nbsp;&nbsp;
   <!--   	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='inPremises.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
     </div>
       
   
    
    <div class="row">
  	<div class="col-12 col-sm-12"><INPUT Type=Checkbox  class="form-check-input" style="border:0;" name = "select" value="select" onclick="return checkAll(this.checked);">
                     <strong>  &nbsp; Select/Unselect </strong> </div>
  </div>
  
  <table class="table" WIDTH="100%">
 <thead style="background: #eaeafa"> 
        <tr>
            <th width="5%">CHK</th>
            <TH width="10%"><font  align="left"><b>PRODUCT ID</b></font></TH>
            <TH width="10%"><font  align="left"><b>PRODUCT CATEGORY ID</b></font></TH>
            <TH width="10%"><font  align="left"><b>PRODUCT SUB CATEGORY ID</b></font></TH>
            <TH width="10%"><font  align="left"><b>PRODUCT BRAND ID</b></font></TH>
            <TH width="15%"><font  align="left"><b>DESCRIPTION</b></font></TH>
            <TH width="5%"><font   align="left"><b>UOM</b></font></TH>
            <TH width="10%"><font  align="left"><b>BATCH</b></font></TH>
            <TH width="10%"><font  align="Right"><b>QUANTITY</b></font></TH>
            <TH width="15%"><font  align="left"><b>EXPIRY DATE</b></font></TH>
        </tr>
    </thead>
    <tbody>
  
     <%
     if(action.equalsIgnoreCase("view"))
     {
     Hashtable ht = new Hashtable();
	 String chkString="";
     if(PLANT.length() > 0)               ht.put("a.PLANT",PLANT);
     if(ITEM.length() > 0)              ht.put("a.ITEM",ITEM);
     if(BATCH.length() > 0)               ht.put("a.USERFLD4",BATCH);
     if(PRD_CLS_ID.length() > 0)          ht.put("b.PRD_CLS_ID",PRD_CLS_ID);
     if(PRD_TYPE_ID.length() > 0)         ht.put("b.ITEMTYPE",PRD_TYPE_ID) ;
     if(PRD_BRAND_ID.length() > 0)         ht.put("b.PRD_BRAND_ID",PRD_BRAND_ID) ;
     if(PRD_DEPT_ID.length() > 0)         ht.put("b.PRD_DEPT_ID",PRD_DEPT_ID) ;
     
      ArrayList al= new InvUtil().getInvListWithExpireDategroupbybatch(ht,PLANT,ITEM,PRD_DESCRIP,EXPIREDATE);
      if(al.size()>0)
      {
       for(int i=0 ; i<al.size();i++)
       {
    	  Map m=(Map)al.get(i);
          //int iIndex = i + 1;
          String sno =(String)m.get("Sno"); 
          String item =(String)m.get("ITEM");
          String batch =(String)m.get("BATCH");
          String expirydate =(String)m.get("EXPIREDATE");
          //batch = batch.replaceAll("[-+^]*", "");
          chkString = sno+","+item+","+batch;
          
          //String batchno = batch.replaceAll("\\W", ""); 
          //String sno=item+"_"+batchno;
          
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       
       %>
       
      		
        <TR>
              <TD width="5%"  align="CENTER"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkitem" id="chkitem"  value="<%=chkString%>"></font></TD>
              <td width="10%"  align="left"><font color="<%=strUtils.fString((String)m.get("COLORCODE"))%>"><%=strUtils.fString((String)m.get("ITEM"))%></font></td>
              <td  width="10%" align="left"><font color="<%=strUtils.fString((String)m.get("COLORCODE"))%>"><%=strUtils.fString((String)m.get("PRDCLSID"))%></font></td>
              <td width="10%" align="left"><font color="<%=strUtils.fString((String)m.get("COLORCODE"))%>"><%=strUtils.fString((String)m.get("ITEMTYPE"))%></font></td>
              <td width="10%" align="left"><font color="<%=strUtils.fString((String)m.get("COLORCODE"))%>"><%=strUtils.fString((String)m.get("PRD_BRAND_ID"))%></font></td>
              <td width="15%" align="left"><font color="<%=strUtils.fString((String)m.get("COLORCODE"))%>"><%=strUtils.fString((String)m.get("ITEMDESC"))%></font></td>
              <td  width="5%" align="left"><font color="<%=strUtils.fString((String)m.get("COLORCODE"))%>"><%=strUtils.fString((String)m.get("STKUOM"))%></font></td>
              <td width="10%" align="left"><font color="<%=strUtils.fString((String)m.get("COLORCODE"))%>"><%=strUtils.fString((String)m.get("BATCH"))%></font></td>
              <td width="10%" align ="center"><font color="<%=strUtils.fString((String)m.get("COLORCODE"))%>"><%=strUtils.fString((String)m.get("QTY"))%></font></td>
              <%if(expirydate.length()>0) {%>
              <td width="15%" align="center"><font color="<%=strUtils.fString((String)m.get("COLORCODE"))%>"></font><INPUT class="form-control datepicker" name="EXPIRYDATE_<%=sno%>" id="EXPIRYDATE_<%=sno%>"   value = "<%=expirydate%>" type="TEXT" size="15"  readonly="readonly">
             	<%} else{ %>
             	<td width="15%" align="center"><font color="<%=strUtils.fString((String)m.get("COLORCODE"))%>"></font><INPUT class="form-control datepicker" name="EXPIRYDATE_<%=sno%>" id="EXPIRYDATE_<%=sno%>" value=""  type="TEXT" size="15"  readonly="readonly">
             	<%}  %>
   		 	    <input type="hidden" name="OLDEXPIRE_<%=sno%>"  id="OLDEXPIRE_<%=sno%>"  value="<%=expirydate%>">
   		 	    </td>
             
           </TR>
           <%}} else {%>
       
             <TR> <TD align="center" width="15%"> Data's Not Found</TD></TR>
       <%} }%>
         </tbody>
      </table>
      <br>
<div class="form-group">
  <label class="control-label col-sm-2" for="Remarks">Remarks:</label>
      <div class="col-sm-3">
      <input class="form-control" name="REMARKS" type="TEXT"  value="<%=REMARKS%>" size="40" MAXLENGTH=100>
  	</div>
<div class=form-inline>
<label class="control-label col-sm-2" for="Reason Code">Reason Code:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="REASONCODE" type = "TEXT" value="<%=REASONCODE%>" size="40"  MAXLENGTH=20>
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('../jsp/miscreasoncode.jsp?ITEMNO='+form1.ITEM.value+'&FORM=form1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Reason Code Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
       </div>
        
  </form>
  </div>
  </div>
  </div>
  
   <script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();  
    var plant= '<%=PLANT%>'; 

    /* Product Dept Auto Suggestion */
	$('#PRD_DEPT_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'prd_dep_id',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTDEPARTMENTID_FOR_SUMMARY",
				PRODUCTDEPARTMENTID : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTOMERTYPELIST);
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
// 	return '<p class="item-suggestion">'+ data.prd_dep_id +'</p>';
				return '<div onclick="document.form1.PRD_DEPT_ID.value = \''+data.prd_dep_id+'\'"><p class="item-suggestion">' + data.prd_dep_id + '</p><br/><p class="item-suggestion">DESC: '+data.prd_dep_desc+'</p></div>';
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

  
  
  <SCRIPT LANGUAGE="JavaScript">
  function onUpdate(form){
	   debugger;  
	    var checkFound = false;  
		 var len = document.form1.chkitem.length; 
		 if(len == undefined) len = 1;  
		 for (var i = 0; i < len ; i++)
	    {
			 if(len ==1 && document.form1.chkitem.checked)
		    	{
		    		chkstring = document.form1.chkitem.value;
		    	}
		    	else
		    	{
		    		chkstring = document.form1.chkitem[i].value;
		    	}
		    	chkdvalue = chkstring.split(',');
			if(len == 1 && (!document.form1.chkitem.checked))
			{
				checkFound = false;
			}
			
			else if(len ==1 && document.form1.chkitem.checked)
		     {
		    	 checkFound = true;
		    	 //var batch = chkdvalue[1];
		    	 //batch = batch.replaceAll("\\W", ""); 
		    	 //batch = batch.replace(/[^a-zA-Z0-9]/g, '');
		    	 orderLNo = chkdvalue[0];
		    	 var expirydate = document.getElementById("EXPIRYDATE_"+orderLNo).value;
		    	 
				 if(expirydate=="" || expirydate.length==0 ){
					 alert("Enter Expiry date");
					 document.getElementById("EXPIRYDATE_"+orderLNo).focus();
					return false;
					 
				 }
			    
		    	 
		     }
		
		     else {
			     if(document.form1.chkitem[i].checked){
			    	 checkFound = true;
			    	 orderLNo = chkdvalue[0];
			    	 var expirydate = document.getElementById("EXPIRYDATE_"+orderLNo).value;
			    	 
					 if(expirydate=="" || expirydate.length==0 ){
						 alert("Enter Expiry date");
						 document.getElementById("EXPIRYDATE_"+orderLNo).focus();
						return false;
						 
					 }
			    	 
			     }
		     }
	          		
	        	     
	    }
		 if (checkFound != true) {
			    alert ("Please check at least one checkbox.");
			    return false;
			    }
		   document.form1.action ="/track/InvQueryServlet?action=Update_ExpireDate";
		   document.form1.submit();
	  }
</SCRIPT>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>