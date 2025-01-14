<%@page import="java.math.BigDecimal"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.track.service.ShopifyService"%>
<%@page import="org.json.JSONObject"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>


<%
String title = "Import Shopify Product";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<%
 MLogger logger = new MLogger();
String fieldDesc="";
String msg="";

String PLANT              = (String)session.getAttribute("PLANT");
String _login_user        = (String)session.getAttribute("LOGIN_USER");
String action         = StrUtils.fString(request.getParameter("action")).trim();

JSONObject json;
json = new ShopifyService().getShopifyProductsList(PLANT);
HashMap productMap = new Gson().fromJson(json.toString(), HashMap.class);
ArrayList products = ((ArrayList)productMap.get("products"));
if(PLANT==null){
String result = "New user diverting to login page";
%>
    <jsp:forward page="login.jsp" >
    <jsp:param name="warn" value="<%=result%>" />
    </jsp:forward>
<%
}  
%>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
 <script>

function onGo()
    {
    	
    }

function onConfirm()
{
	/* var checkFound = false;
	var len = document.form.chkdItem.length; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form.chkdItem.checked)) {
			checkFound = false;
		}
		else if (len == 1 && document.form.chkdItem.checked) {
			checkFound = true;
		}
		else {
			if (document.form.chkdItem[i].checked) {
				checkFound = true;		
			}
		}
	} */
	if($("input[name=chkdItem]:checked").length == 0){
		alert("Please check at least one checkbox.");
		return false;
	}
	var prevprdId = "";
	var prdObj = [], variantObj = [];
	var item = {}, variant = {}, productJson = {};
	var count = 1;
	$("input[name=chkdItem]:checked").each(function(){
	    var prdId = $(this).attr("data-prdId");
	    var variantid = $(this).attr("data-variantid");
	    
	    if(prdId === prevprdId){
	    	variant['id']=variantid;
	    	variantObj.push(variant);
	    	
	    }else{
	    	if(Object.keys(item).length > 0){
		    	item['variant_id']=variantObj;
		    	prdObj.push(item);
		    	item = {};
		    	variantObj = [];
	    	}
	    	item['product_id']=prdId;
	    	variant['id']=variantid;
	    	variantObj.push(variant)
	    	
	    } 
	    
	    if($("input[name=chkdItem]:checked").length == count){
    		item['variant_id']=variantObj;
	    	prdObj.push(item);
    	}
	    variant = {};
	    prevprdId = prdId;
	    count++;
	});
	productJson['plant']="<%=PLANT%>";
	productJson['product_details']=prdObj;
	
	
	console.log(productJson);
	
	$.ajax({
		type : "POST",
		url : "../integrations/addShopifyProducts",
		data : JSON.stringify(productJson),
		dataType : "json",
		contentType: 'application/json',
		success : function(data) {
			if(data.ERROR_CODE == 100){
				alert(data.MESSAGE);
				document.form.reset();
			}else
        		alert(data.MESSAGE);
		}
	});
    /* document.form1.action="/NSeries/shopifyListner?action=SHOPIFYPRODUCTS";
    document.form1.submit(); */
}

function checkAll(isChk)
{
	var len = document.form.chkdItem.length; 
 	if(len == undefined) len = 1;  
    if (document.form.chkdItem){
        for (var i = 0; i < len ; i++){      
	      	if(len == 1){
	      		document.form.chkdItem.checked = isChk;
	       	}
	      	else{
	      		document.form.chkdItem[i].checked = isChk;
	      	}            	
        }
    }
}



/* function onClear()
{
	document.form1.action  = "importshopifyproduct.jsp";
	document.form1.submit();
} */

  
</script>
    
  

  <div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                <li><a href="../product/summary"><span class="underline-on-hover">Product Summary</span> </a></li>               
                <li><label>Import Shopify Product</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right"
				onclick="window.location.href='../product/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>		
 <div class="box-body">     
 	<div class="form-group">        
		<div class="col-sm-12" align="center">
	       	<button type="button" class="Submit btn btn-default" onclick="window.location.href='../product/shopifyproduct'">Clear</button>&nbsp;
	       	<button type="button" class="Submit btn btn-success" name="Submit" value="Confirm" 
	       	onclick="javascript:return onConfirm();" >Confirm</button>&nbsp;
		</div>
	</div>
             
	<div class="row">
  		<div class="col-12 col-sm-12">
  			<label>
  			<input type="checkbox" class="form-check-input"  style="border:0;" name = "select" value="select" onclick="return checkAll(this.checked);">
			&nbsp; Select/Unselect All &nbsp;
			</label>
		</div>                        
  	</div>
  	<form name="form" method="post" action="" >
	<table class="table table-bordred table-striped" WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
		<thead>
			<tr style="text-align:left">
				<th style="font-size: smaller;">Chk</th> 
				<th style="font-size: smaller;">Product ID</th> 
				<!-- <th style="font-size: smaller;" width="6%">Product Class</th> 
				<th style="font-size: smaller;" width="5%">Product Type</th>
				<th style="font-size: smaller;" width="5%">Product Brand</th> -->
				<th style="font-size: smaller;">Description</th>
				<th style="font-size: smaller;">List Price</th>	
			</tr>
		</thead>
		<tbody>
			
			<%
			for (int i=0;i<products.size();i++){
				Map product = (Map)products.get(i);
				ArrayList variants = ((ArrayList)product.get("variants"));
				for(int j=0;j<variants.size();j++){
				Map variant = (Map)variants.get(j);
				String prdTitle="", variantTitle="";
				prdTitle = (String)product.get("title");
				variantTitle = (String)variant.get("title");
				prdTitle = ((variantTitle.equalsIgnoreCase("Default Title")) ? prdTitle : (prdTitle+" "+variantTitle));
				
			%>
			<tr>
				<td><input type="checkbox" style="border: 0;" name="chkdItem" 
				data-prdId="<%=BigDecimal.valueOf((Double)variant.get("product_id")).toString()%>" 
				data-variantId="<%=BigDecimal.valueOf((Double)variant.get("id")).toString()%>" 
				value="" >
				</td>
				<td><%=StrUtils.forHTMLTag((String)variant.get("sku"))%></td>
				<td><%=StrUtils.forHTMLTag(prdTitle)%></td>
				<td><%=StrUtils.forHTMLTag((String)variant.get("price"))%></td>
			</tr>
			<%
				}
			}			
			%>
			
		</tbody>
     	 
	 
    </table>
    </form>
    
      <table border="0" cellspacing="1" cellpadding = 2 align="center" bgcolor="">
             
      </table>
  
  
  
  
  </div>
  </div>
  </div>
  

  <jsp:include page="footer2.jsp" flush="true">
<jsp:param name="title" value="<%=title %>" />
</jsp:include>