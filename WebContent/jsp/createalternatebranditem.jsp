<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title1 = "Edit Alternate Brand Product";
String title = "Create Alternate Brand Product";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Alternate Brand Product', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

</script>
<SCRIPT LANGUAGE="JavaScript">
/*function onNew(){
   document.form.action  = "createalternatebranditem.jsp?action=Clear";
   document.form.submit();
}*/

function onClear()
{
	 document.form.ITEM.value="";
	document.form.DESC.value="";
	document.form.PRD_BRAND_ID.value="";
	document.form.ALTERNATEBRANDITEM.value="";
    document.form.ALTERNATEBRANDDESC.value="";
	document.form.ALTERNATE_PRD_BRAND_ID.value="";
	document.form.VINNO.value="";
	document.form.MODEL.value="";
	document.form.ALTERNATEVINNO.value="";
	document.form.ALTERNATEMODEL.value="";
// 	 document.form.action  = "../jsp/createalternatebranditem.jsp?action=Clear";
// 	 document.form.submit();
	
	
	}
	
function onDelete(){
	
	 document.form.action  = "../jsp/createalternatebranditem.jsp?action=DELETE";
	 document.form.submit();

}
function onUpdate()
{
	var status=true;
	 var sameParent=false;
	 var sameChild=false;
	 var sameChildMsg="";
	 var lineItemStatus=true;
	 var ITEM   = document.form.ITEM.value;
	 var ALTERNATEBRANDITEM = document.form.ALTERNATEBRANDITEM.value;
	 var altitemarr=[];
	 if(ITEM == "" || ITEM == null)
	    {
	      alert("Please key in Product ID.");
	      document.form.ITEM.focus(); 
	      status= false;
	    }
	 $("input[name=ALTERNATEBRANDITEM]").each(function() {
	       var pItem=$(this).val();
	       //alert(pItem);
	       if(ITEM===pItem)
	    	   {
	    	   	status= false;
	    	   	sameParent=true;
	    	   	return false;
	    	   }
	       else
	    	   {
	    	   		status= true;
	    	   		sameParent=false;
	    	   }
	       
	       if(pItem==="")
	    	   {
	    	   	status= false;
	    	   	lineItemStatus=false;
	    	   }
	       else
	    	   {
	    	   	 if(jQuery.inArray(pItem,altitemarr)==-1)
	    	   		 {
	    	   		 
	    	   		 	altitemarr.push(pItem);
	    	   			 status= true;
	    	   			 sameChild=false;
	    	   		 }
	    	   	 else
	    	   		 {
	    	   			status= false;
	    	   		 	sameChild=true;
	    	   			sameChildMsg=pItem;
	    	   			return false;
	    	   		 }
	    	  	
	    	   }
	       
	     });
	 $("input[name=ALTERNATEBRANDDESC]").each(function() {
	       var pItemDesc=$(this).val();
	       //alert(pItem);
	       if(pItemDesc==="")
	    	   {
	    	   	status= false;
	    	   	lineItemStatus=false;
	    	   	return false;
	    	   }
	       
	     });
	 if(sameParent)
		 {
		 alert("Alternate Brand Product should not be same as main product");
		 }
	 if(sameChild)
	 {
		 alert("Product "+sameChildMsg+" already exist as alternate product");
	 }
	 if(!lineItemStatus)
		 {
			 alert("Alternate product should not be blank.");
		 }
	 if(status)
		 {
		 	document.form.action  = "/track/ItemMstServlet?ACTION=UPDATE_ALT_PRODUCT";
		    document.form.submit();
		 }
	}
function onAdd(){
	
	 //var frmRoot=document.form
	 var status=true;
	 var sameParent=false;
	 var sameChild=false;
	 var sameChildMsg="";
	 var lineItemStatus=true;
	 var ITEM   = document.form.ITEM.value;
	 var ALTERNATEBRANDITEM = document.form.ALTERNATEBRANDITEM.value;
	 var altitemarr=[];
	 if(ITEM == "" || ITEM == null)
	    {
	      alert("Please key in Product ID.");
	      document.form.ITEM.focus(); 
	      status= false;
	    }
	 $("input[name=ALTERNATEBRANDITEM]").each(function() {
	       var pItem=$(this).val();
	       //alert(pItem);
	       if(ITEM===pItem)
	    	   {
	    	   	status= false;
	    	   	sameParent=true;
	    	   	return false;
	    	   }
	       else
	    	   {
	    	   		status= true;
	    	   		sameParent=false;
	    	   }
	       
	       if(pItem==="")
	    	   {
	    	   	status= false;
	    	   	lineItemStatus=false;
	    	   }
	       else
	    	   {
	    	   	 if(jQuery.inArray(pItem,altitemarr)==-1)
	    	   		 {
	    	   		 
	    	   		 	altitemarr.push(pItem);
	    	   			 status= true;
	    	   			 sameChild=false;
	    	   		 }
	    	   	 else
	    	   		 {
	    	   			status= false;
	    	   		 	sameChild=true;
	    	   		 	sameChildMsg=pItem;
	    	   			return false;
	    	   		 }
	    	  	
	    	   }
	       
	     });
	 $("input[name=ALTERNATEBRANDDESC]").each(function() {
	       var pItemDesc=$(this).val();
	       //alert(pItem);
	       if(pItemDesc==="")
	    	   {
	    	   	status= false;
	    	   	lineItemStatus=false;
	    	   	return false;
	    	   }
	       
	     });
	 if(sameParent)
		 {
		 alert("Alternate Brand Product should not be same as main product");
		 }
	 if(sameChild)
	 {
		 alert("Product "+sameChildMsg+" already exist as alternate product");
	 }
	 if(!lineItemStatus)
		 {
			 alert("Alternate product should not be blank.");
		 }
	 if(status)
		 {
		 document.form.action  = "/track/ItemMstServlet?ACTION=ADD_ALT_PRODUCT";
		   document.form.submit();
		 }
	
	 
    /* if(ITEM == "" || ITEM == null)
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
	   document.form.action  = "/track/ItemMstServlet?ACTION=ADD_ALT_PRODUCT";
	   document.form.submit();
	  // return true;
   } */
        
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
	ItemMstUtil itemMstUtil=new ItemMstUtil();
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	String sItemId     = StrUtils.fString(request.getParameter("ITEM"));
	String sItemDesc      = StrUtils.fString(request.getParameter("DESC"));
	String productBrandID    = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
	String sAlternateBrandItem    = StrUtils.fString(request.getParameter("ALTERNATEBRANDITEM"));
	String sAlternateBrandItemDesc      = StrUtils.fString(request.getParameter("ALTERNATEBRANDDESC"));
	String alternateProductBrandID    = StrUtils.fString(request.getParameter("ALTERNATE_PRD_BRAND_ID"));
	String vinno    = StrUtils.fString(request.getParameter("VINNO"));
	String model    = StrUtils.fString(request.getParameter("MODEL"));
	String alternatevinno      = StrUtils.fString(request.getParameter("ALTERNATEVINNO"));
	String alternatemodel    = StrUtils.fString(request.getParameter("ALTERNATEMODEL"));
	String EDIT    = StrUtils.fString(request.getParameter("EDIT"));
    System.out.println("EDIT" +EDIT);
    List resultList=new ArrayList<>();
    List altPrdList;
    Map altItem=new HashMap<>();
    if(action.equalsIgnoreCase("EDIT"))
    {
    	resultList =itemMstUtil.getMasterProductListByItem(sItemId,plant,"");
    	altItem = (Map) resultList.get(0);
    }
    else
    {
    	altItem.put("ITEM","");
    	altItem.put("ITEMDESC","");
    	altItem.put("PRD_BRAND_ID","");
    	altItem.put("VINNO","");
    	altItem.put("MODEL","");
    }

%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>	
                 <li><a href="../alternateproduct/summary"><span class="underline-on-hover">Alternate Brand Product Summary</span></a></li>	
                  <% if(action.equals("EDIT")) { %>
                <li><label><%=title1%></label></li>  
                <%}else{%> 
                <li><label><%=title%></label></li>  
                <%}%>                               
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
	 <% if(action.equals("EDIT")) { System.out.println("Action "+action);%>
            <div class="box-header menu-drop">
           
              <h1 style="font-size: 20px;" class="box-title"><%=title1%></h1>
            
		</div>
		<%}else{%>
		 <div class="box-header menu-drop">
           
              <h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
            
		</div>
		<%}%>
 <div class="box-body">
  <CENTER><strong><%=res%></strong></CENTER>


<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form" method="post" enctype="multipart/form-data" onSubmit="return onCheck();">
 <INPUT type = "hidden"  name="EDIT" value="<%=EDIT%>"> 
 <input type="text" name="plant" value="<%=plant%>" hidden>
  		<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Product ID</label>
					<div class="col-sm-6 ac-box">
						<div class="input-group">
							<input type="text" class="ac-selected  form-control typeahead"
								id="ITEM" placeholder="Select a Product" name="ITEM" MAXLENGTH=50 onkeypress="return blockSpecialChar(event)"
								value="<%=altItem.get("ITEM")%>"> <span class="select-icon"
								style="right: 45px;"
								onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i
								class="glyphicon glyphicon-menu-down"></i></span> <span
								class="btn-danger input-group-addon"
								onclick="javascript:popUpWin('../jsp/itemlist.jsp?ITEM='+form.ITEM.value+'&TYPE=MAINITEM');"><span
								class="glyphicon glyphicon-search" aria-hidden="true"></span></span>
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Product Description:</label>
					<div class="col-sm-4">
					<div class="input-group">
						<INPUT class="form-control typeahead" name="DESC" id="DESC" placeholder="Select Product Desc" type="TEXT" value="<%=altItem.get("ITEMDESC")%>" size="20" MAXLENGTH=100>
						<span class="select-icon"
								style="right: 45px;"
								onclick="$(this).parent().find('input[name=\'DESC\']').focus()"><i
								class="glyphicon glyphicon-menu-down"></i></span>
						<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/itemlist.jsp?ITEM_DESC='+form.DESC.value+'&TYPE=MAINITEM');">
				   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
				   		 		<i class="glyphicon glyphicon-search" style="font-size: 15px;"></i>
				   		 	</a>
			   		 	</span>
			   		 	</div>
					</div>
				</div>
  		 <div class="form-group">
	      	<label class="control-label col-form-label col-sm-2" for="Product Brand">Product Brand ID:</label>
	      	<div class="col-sm-4">
	      	<div class="input-group">
	    	<input name="PRD_BRAND_ID" id="PRD_BRAND_ID" type="TEXT" readonly value="<%=altItem.get("PRD_BRAND_ID")%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	
  		</div>
  		</div>
  		</div>
  		
  		 <div class="form-group">
	      	
	      	<label class="control-label col-form-label col-sm-2" for="VINNO">VIN No.:</label>
	      	<div class="col-sm-4">
	      	<div class="input-group">
	    	<input name="VINNO" id="VINNO" type="TEXT" readonly value="<%=altItem.get("VINNO")%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	
  		</div>
  		</div>
  		</div>
  		
  		 <div class="form-group">
	      	
	      	<label class="control-label col-form-label col-sm-2" for="MODEL">Model:</label>
	      	<div class="col-sm-4">
	      	<div class="input-group">
	    	<input name="MODEL" id="MODEL" type="TEXT" readonly value="<%=altItem.get("MODEL")%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	
  		</div>
  		</div>
  		</div>
  		
  		<div class="row" style="margin: 0px;">
					<table class="table table-bordered line-item-table alt-table">
						<thead>
							<tr>
								<th class="alt_bpid" colspan=2>Alt. Brand Product ID</th>
								<th class="alt_bd">Alt. Brand Description</th>
								<th class="alt_pbid">Alt. Product Brand ID</th>
								<th class="alt_vinno">Alt. VIN No.</th>
								<th class="alt_model">Alt. Model</th>
							</tr>
						</thead>
						<tbody id="alt-body">
						<%if (action.equalsIgnoreCase("EDIT")) {
							for(int i=0;i<resultList.size();i++)
							{
								Map master = (Map) resultList.get(i);
								String altProdId=master.get("ALTERNATE_ITEM_NAME").toString();
								altPrdList =itemMstUtil.getAltProductListByItem(altProdId,plant,"");
									Map m = (Map) altPrdList.get(0);
									String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
				                    String prdimage=((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath);
						%>
							<tr>
								<td class="item-img text-center">
									<!-- <span class="glyphicon glyphicon-picture"></span>  --> <img
									alt="" src="<%=prdimage%>" style="width: 100%;"> <!-- 				  <input type="hidden" name="lnno" value="1"> -->
								</td>
								<td class="alt-bpid" style="width: 30%;"><INPUT class="form-control alt_productsearch" name="ALTERNATEBRANDITEM" type="TEXT" value="<%=m.get("ALTERNATE_ITEM_NAME")%>"
				MAXLENGTH=50> 
   								</td>
								<td class="alt-bd" style="width: 22%;"><INPUT class="form-control alt_productdesc" name="ALTERNATEBRANDDESC" type="TEXT" value="<%=m.get("ITEMDESC")%>" size="20" MAXLENGTH=100>
								</td>
								<td class="alt-pbid" style="width: 16%;"><input name="ALTERNATE_PRD_BRAND_ID" type="TEXT" readonly value="<%=m.get("PRD_BRAND_ID")%>"
			size="50" MAXLENGTH=100 class="form-control"></td>
								<td class="alt-vinno" style="width: 16%;"><input name="ALTERNATEVINNO" type="TEXT" readonly value="<%=m.get("VINNO")%>"
			size="50" MAXLENGTH=100 class="form-control"></td>
								<td class="alt-model" style="width: 16%;position:relative;">
								<%if(i>0) {%>
								<span class='glyphicon glyphicon-remove-circle bill-action' aria-hidden='true'></span>
								<%} %>
								<input name="ALTERNATEMODEL" type="TEXT" readonly value="<%=m.get("MODEL")%>"
			size="50" MAXLENGTH=100 class="form-control"></td>
							</tr>
						<%}
						}else{ %>
							<tr>
								<td class="item-img text-center">
									<!-- <span class="glyphicon glyphicon-picture"></span>  --> <img
									alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;"> <!-- 				  <input type="hidden" name="lnno" value="1"> -->
								</td>
								<td class="alt-bpid" style="width: 30%;"><INPUT class="form-control alt_productsearch" name="ALTERNATEBRANDITEM" type="TEXT" value=""
				MAXLENGTH=50> 
   								</td>
								<td class="alt-bd" style="width: 22%;"><INPUT class="form-control alt_productdesc" name="ALTERNATEBRANDDESC" type="TEXT" value="<%=sAlternateBrandItemDesc%>" size="20" MAXLENGTH=100>
								</td>
								<td class="alt-pbid" style="width: 16%;"><input name="ALTERNATE_PRD_BRAND_ID" type="TEXT" readonly value="<%=alternateProductBrandID%>"
			size="50" MAXLENGTH=100 class="form-control"></td>
								<td class="alt-vinno" style="width: 16%;"><input name="ALTERNATEVINNO" type="TEXT" readonly value="<%=alternatevinno%>"
			size="50" MAXLENGTH=100 class="form-control"></td>
								<td class="alt-model" style="width: 16%;"><input name="ALTERNATEMODEL" type="TEXT" readonly value="<%=alternatemodel%>"
			size="50" MAXLENGTH=100 class="form-control"></td>
							</tr>
						<%} %>
							
						</tbody>
					</table>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<a class="add-line"
							style="text-decoration: none; cursor: pointer;"
							onclick="addRow()">+ Add another line</a>
					</div>
				</div>	
     
  
    <div class="form-group">
    <br>
    <br> 
    <div class="row">   
      <div class="col-sm-12">
     
              	
      	<% if(!action.equals("EDIT")) { %>
      	      <button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	      <button type="button" class="Submit btn btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();"  <%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	 <% } else {%>
      	      <button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	      <button type="button" class="Submit btn btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();"  <%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	 <% } %>
      	 
      	 
      	
      	

      </div>
      </div>    
    </div>
  </form>
</div>
</div>
</div>



<script>
var plant=$("input[name ='plant']").val();
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    addSuggestionToTable();
 
    $(".alt-table tbody").on('click','.bill-action',function(){
    	debugger;	    
        $(this).parent().parent().remove();
    });
    
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
				ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
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
			return '<div onclick="loadHeaderItemDataID(\''+ data.ITEM+'\',\''+ data.ITEMDESC+'\',\''+ data.BRAND+'\',\''+ data.VINNO+'\',\''+ data.MODEL+'\')"><p class="item-suggestion">'+data.ITEM+'</p></div>';
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
	 
	 $('#DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEMDESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCT_LIST_FOR_SUGGESTION",
				ITEM_DESC : query
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
			return '<div onclick="loadHeaderItemDataDesc(\''+ data.ITEM+'\',\''+ data.ITEMDESC+'\',\''+ data.BRAND+'\',\''+ data.VINNO+'\',\''+ data.MODEL+'\')"><p class="item-suggestion">'+data.ITEMDESC+'</p></div>';
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

function addRow()
{
	var addHtml="<tr>"+
				"<td class='item-img text-center'><img src='dist/img/NO_IMG.png' style='width: 100%;'></td>"+
				"<td class='alt-bpid' style='width: 30%;'><INPUT class='form-control alt_productsearch' name='ALTERNATEBRANDITEM' type='TEXT' value='' MAXLENGTH=50></td>"+
				"<td class='alt-bd' style='width: 22%;'><INPUT class='form-control alt_productdesc' name='ALTERNATEBRANDDESC' type='TEXT' size='20' MAXLENGTH=100></td>"+
				"<td class='alt-pbid' style='width: 16%;'><input name='ALTERNATE_PRD_BRAND_ID' type='TEXT' readonly  size='50' MAXLENGTH=100 class='form-control'></td>"+
				"<td class='alt-vinno' style='width: 16%;'><input name='ALTERNATEVINNO' type='TEXT' readonly  size='50' MAXLENGTH=100 class='form-control'></td>"+
				"<td class='alt-model' style='width: 16%;position:relative;'><span class='glyphicon glyphicon-remove-circle bill-action' aria-hidden='true'></span><input name='ALTERNATEMODEL' type='TEXT' readonly  size='50' MAXLENGTH=100 class='form-control'></td>"+
				"</tr>";

$('#alt-body').append(addHtml);
removeSuggestionToTable();
addSuggestionToTable();
	}
function addSuggestionToTable()
{
	$(".alt_productsearch").typeahead({
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
				ACTION : "ALT_PRODUCT_DETAILS",
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
			return '<div onclick="loadItemData(this,\''+ data.ITEM+'\',\''+ data.ITEMDESC+'\',\''+ data.BRAND+'\',\''+ data.VINNO+'\',\''+ data.MODEL+'\',\''+ data.CATLOGPATH+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Brand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.BRAND+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="itemAddBtn footer"  data-toggle="modal" data-target="#productModal"><a href="#"> + Add New Product</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	
	$(".alt_productdesc").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'ITEMDESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "ALT_PRODUCT_DETAILS_DESC",
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
			return '<div onclick="loadItemDataDesc(this,\''+ data.ITEM+'\',\''+ data.BRAND+'\',\''+ data.VINNO+'\',\''+ data.MODEL+'\');"><p class="item-suggestion">'+data.ITEM+'</p><p class="item-suggestion pull-right">Brand</p><br/><p class="item-suggestion">'+data.ITEMDESC+'</p><p class="item-suggestion pull-right">'+data.BRAND+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="itemAddBtn footer"  data-toggle="modal" data-target="#productModal"><a href="#"> + Add New Product</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();		  
		}).on('typeahead:open',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
}	
function removeSuggestionToTable(){
	$(".alt_productsearch").typeahead('destroy');
	$(".alt_productdesc").typeahead('destroy');
}
function loadItemData(obj,item,itemDesc,brandId,vinno,model,catalogpath)
{
	clearLineItems(obj);
	var parentItem=$('#ITEM').val();
	var childItem=item;
	if(parentItem===childItem)
		{
		alert("Cannot add parent product as alternate.");
		clearLineItems(obj);
		return false;
		}
	$(obj).closest('tr').find("td:nth-child(1)").find('img').attr("src",catalogpath);
	$(obj).closest('tr').find("td:nth-child(3)").find('input').val(itemDesc);
	$(obj).closest('tr').find("td:nth-child(4)").find('input').val(brandId);
	$(obj).closest('tr').find("td:nth-child(5)").find('input').val(vinno);
	$(obj).closest('tr').find("td:nth-child(6)").find('input').val(model);
	}
function loadItemDataDesc(obj,item,brandId,vinno,model)
{
	clearLineItems(obj);
	var parentItem=$('#ITEM').val();
	var childItem=item;
	if(parentItem===childItem)
		{
		alert("Cannot add parent product as alternate.");
		clearLineItems(obj);
		return false;
		}
	$(obj).closest('tr').find("td:nth-child(2)").find('input').val(item);
	$(obj).closest('tr').find("td:nth-child(4)").find('input').val(brandId);
	$(obj).closest('tr').find("td:nth-child(5)").find('input').val(vinno);
	$(obj).closest('tr').find("td:nth-child(6)").find('input').val(model);
	}
function loadHeaderItemDataID(item,desc,brand,vin,model)
{
	$('#DESC').val(desc);
	$('#PRD_BRAND_ID').val(brand);
	$('#VINNO').val(vin);
	$('#MODEL').val(model);
	checkItemExist(item);
	
}
function loadHeaderItemDataDesc(item,desc,brand,vin,model)
{
	$('#ITEM').val(item);
	$('#PRD_BRAND_ID').val(brand);
	$('#VINNO').val(vin);
	$('#MODEL').val(model);
	checkItemExist(item);
}
function checkItemExist(item)
{
	var urlStr = "/track/ItemMstServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			ACTION : "ALT_PRODUCT_DETAILS_ITEM",
			ITEM : item
		},
		dataType : "json",
		success : function(data) {
			if(data.items.length>0)
				{
					location.href = '../alternateproduct/new?action=EDIT&ITEM='+item;
				}
		}
			});
	}
	function clearLineItems(obj)
	{
		$(obj).closest('tr').find("td:nth-child(2)").find('input').val("");
		$(obj).closest('tr').find("td:nth-child(3)").find('input').val("");
		$(obj).closest('tr').find("td:nth-child(4)").find('input').val("");
		$(obj).closest('tr').find("td:nth-child(5)").find('input').val("");
		$(obj).closest('tr').find("td:nth-child(6)").find('input').val("");
	}
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>



