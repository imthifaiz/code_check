<%@page import="java.math.BigDecimal"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.track.service.ShopeeService"%>
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
String title = "Import Shopee Product";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<style type="text/css">
.backpageul
{
	background-color: rgb(255, 255, 255);
    padding: 0px 10px;
    margin-bottom: 0px;
    margin-top: 15px;
}
.underline-on-hover:hover {
  		text-decoration: underline;
	}</style>
<%
 MLogger logger = new MLogger();
String fieldDesc="";
String msg="";

String PLANT              = (String)session.getAttribute("PLANT");
String _login_user        = (String)session.getAttribute("LOGIN_USER");
String action         = StrUtils.fString(request.getParameter("action")).trim();

/* JSONObject json;
// json = new ShopeeService().getShopifyProductsList(PLANT);
json = new ShopeeService().getShopeeProductsList(PLANT);
HashMap productMap = new Gson().fromJson(json.toString(), HashMap.class);
ArrayList products = ((ArrayList)productMap.get("products")); */
if(PLANT==null){
String result = "New user diverting to login page";
%>
    <jsp:forward page="login.jsp" >
    <jsp:param name="warn" value="<%=result%>" />
    </jsp:forward>
<%
}  
%>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/fileshowcasedesign.css">
<script type="text/javascript" src="js/general.js"></script>
 <script>
 var pageNo = 1;
 function onGo(page){
		$.ajax({
			type : "GET",
			url : "/track/integrations/getShopeeItemsList?pageOffset="+page,
			dataType : "json",
			contentType: 'application/json',
			success : function(data) {
				console.log(data);
				var nextUrl = "", prevUrl = "", nextBtn = "", prevBtn = "";
				var body = "<thead>"
					+"<tr style='text-align:left'>"
					+"<th style='font-size: smaller;'>Chk</th>"   
					+"<th style='font-size: smaller;'>Product ID</th>"
					+"</tr></thead>";
					var k=0;
				if(data.content != undefined){
					body += "<tbody>";
					var content = JSON.parse(data.content);
					var items = content.items;
					for(var i=0;i<items.length;i++){
						var item = items[i];
						var sku = "";
						if(item.variations.length != 0){
							var variations = item.variations;
							for(var j=0;j<variations.length;j++){
								var variation = variations[j];
								if(j===0)
									sku = variation.variation_sku;
								else
									sku += ", " + variation.variation_sku;
							}
						}else{
							sku = item.item_sku;
						}
						body += "<tr>" +
						"<td><input type='checkbox' style='border:0;' name='chkdItem' "+ 
						"value='"+item.item_id+"'></td>" +
						"<td>"+sku+"</td></tr>";
					}
					body += "</tbody>";
				}else{
					body += "<tbody><tr><td colspan=2 class='text-center'> No Data Found </td></tr></tbody>";
				}
				$("#displayTable").html(body);
				
				if(data.more == false && page == 0){
					nextBtn = "<button type='button' disabled>Next</button>";
					prevBtn = "<button type='button' disabled>Prev</button>";
					pageNo = "";
				}else if(data.more == true && page == 0){
					nextBtn = "<button type='button' onclick=\"onGo('"+ ++page +"');pageNo++\">Next</button>";
					prevBtn = "<button type='button' disabled>Prev</button>";
				}else if(data.more == false){
					nextBtn = "<button type='button' disabled>Next</button>";
					prevBtn = "<button type='button' onclick=\"onGo('"+ --page +"');pageNo--\">Prev</button>";
				}else{
					nextBtn = "<button type='button' onclick=\"onGo('"+ ++page +"');pageNo++\">Next</button>";
					prevBtn = "<button type='button' onclick=\"onGo('"+ (page-2) +"');pageNo--\">Prev</button>";
				}
				
				$("#btnLink").html("");
				$("#btnLink").append(prevBtn);
				$("#btnLink").append("&nbsp;"+pageNo+"&nbsp;");
				$("#btnLink").append(nextBtn);
			}
		});
	}
	

function onConfirm(){
	var checkFound = false;
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
	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}
	var formData = $("form[name=form]").serialize();
	$.ajax({
		type : "GET",
		url : "../integrations/addShopeeProducts",
		data : formData,
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

function onClear(){
	document.form1.action  = "ImportShopee.jsp";
	document.form1.submit();
}

onGo(0);
</script>
    
  

  <div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                <li><a href="../product/summary"><span class="underline-on-hover">Product Summary</span> </a></li>               
                <li>Import Shopee Product</li>                                   
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
	       	<button type="button" class="Submit btn btn-default" onclick="onClear();">Clear</button>&nbsp;
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
	<table id="displayTable" class="table table-bordred table-striped" WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
		
     	 
	 
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