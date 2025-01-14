<%@page import="java.math.BigDecimal"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="org.json.JSONObject"%>
<%@page import="com.track.service.ShopifyService"%>
<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PlantMstUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<%
	String title = "Shopping Cart Integration";
	
    String plant    = StrUtils.fString((String)session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String region = StrUtils.fString((String) session.getAttribute("REGION"));
	String msg = (String)request.getAttribute("Msg");
	if (msg == null){
		msg = "";
	}
	JSONObject json;
	IntegrationsUtil integrationsUtil = new IntegrationsUtil();
	Map mapShopifyConfig = integrationsUtil.getShopifyConfigDetail(plant);
	String shopify_api_key = StrUtils.fString((String)mapShopifyConfig.get(IDBConstants.SHOPIFY_API_KEY));
	String shopify_api_password = StrUtils.fString((String)mapShopifyConfig.get(IDBConstants.SHOPIFY_API_PASSWORD));
	String shopify_api_store_domain_name = StrUtils.fString((String)mapShopifyConfig.get(IDBConstants.SHOPIFY_API_STORE_DOMAIN_NAME));
	String shopify_api_loc_id = StrUtils.fString((String)mapShopifyConfig.get(IDBConstants.SHOPIFY_API_LOC_ID));
	String shopify_webhook_key = StrUtils.fString((String)mapShopifyConfig.get(IDBConstants.SHOPIFY_WEBHOOK_KEY));
	json = new ShopifyService().getShopifyLocationList(plant);
	HashMap slocations = new HashMap();
	ArrayList locations = new ArrayList();
	if(json != null){
		slocations = new Gson().fromJson(json.toString(), HashMap.class);
		System.out.println(slocations.get("locations"));
		locations = ((ArrayList)slocations.get("locations"));
	} 
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.INTEGRATIONS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SHOPPING_CART%>"/>
</jsp:include>
<style>
 .select2drop{
 	width:487px !important;
 }
 .table-icon{
 	text-align: center;
 }
 .table-icon i{
    vertical-align: middle;
 }
 #remarks-table>tbody>tr>td{
 	padding: 8px;
 }
 .remark-action {
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -5%;
    top: 15px;
}
.bill-action {
    right: -60% !important;
}
.sideiconspan {
	color: #23527c;
	position: absolute;
	right: 20px;
	top: 8px;
	z-index: 2;
	vertical-align: middle;
	font-size: 15px;
	font-weight: bold;
}
 </style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Thanzith Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                <li>Shopping Cart Integration</li>                                   
            </ol>             
    <!-- Thanzith Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<div class="panel panel-default">
    <div class="panel-body">
    	<div class="col-md-2">
    		<img src="../jsp/images/shopify.png" alt="Shopify" style="width: 150px;height: auto;" />
    	</div>
    	<div class="col-md-10">
    		<div><strong>Shopify</strong> <i>Developed by</i> <i><strong>SUNPRO</strong></i><br/><br/></div>
			<div>Integrating your Shopify store with SUNPRO Inventory bridges the gap between your sales channel and inventory.<br/><br/></div>
			<div>
			<%-- <button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#shopifyParametersModel" <%if (!plant.equalsIgnoreCase((String)mapShopifyConfig.get("PLANT"))){ %>disabled="disabled"<%} %> style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">Setup</button> --%>
			<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#shopifyParametersModel" style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">Setup</button>
			</div>
    	</div>
    </div>
</div>
		</div>
		<!-- Modal -->
		<!-- Modal -->
		
	</div>
</div>

<!-- ----------------Modal-------------------------- -->
<div id="shopifyParametersModel" class="modal fade" role="dialog">
  	<div class="modal-dialog modal-lg">
  		<div class="modal-content">
  			<div class="modal-header">
          		<button type="button" class="close" data-dismiss="modal">&times;</button>
          		<h4 class="modal-title">Shopify Parameters</h4>
        	</div>
        	<div class="modal-body container-fluid">
        		<form id="shopifyParametersForm" class="form-horizontal" name="form1">
        			<input type="hidden" name="plant" value="<%= plant %>" />
		          	<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required" for="supplier name">API Key:</label>
	        			<div class="col-sm-6 ac-box">
	    		 				<input class="form-control" id="<%=IDBConstants.SHOPIFY_API_KEY %>" name="<%=IDBConstants.SHOPIFY_API_KEY %>" type="TEXT" size="30" MAXLENGTH=100 value="<%=shopify_api_key%>" >
	    		 		</div>
	    		 	</div> 
	    		 	<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required" for="supplier name">Password:</label>
	        			<div class="col-sm-6 ac-box">
	    		 				<input class="form-control" id="<%=IDBConstants.SHOPIFY_API_PASSWORD %>" name="<%=IDBConstants.SHOPIFY_API_PASSWORD %>" type="PASSWORD" size="30" MAXLENGTH=100 value="<%=shopify_api_password%>">
	    		 		</div>
	    		 	</div> 
	    		 	<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required" for="supplier name">Store Domain Name:</label>
	        			<div class="col-sm-6 ac-box">
	    		 				<input class="form-control" id="<%=IDBConstants.SHOPIFY_API_STORE_DOMAIN_NAME %>" name="<%=IDBConstants.SHOPIFY_API_STORE_DOMAIN_NAME %>" type="TEXT" size="30" MAXLENGTH=100 value="<%=shopify_api_store_domain_name%>">
	    		 		</div>
	    		 	</div> 
	    		 	<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required" for="supplier name">Location:</label>
	        			<div class="col-sm-6 ac-box">
	        			<div class="input-group">
	        			<%String LOC_ID =""; %>
						 <input class="ac-selected  form-control typeahead" id="shopify_api_loc_id" type ="TEXT"  MAXLENGTH=20  value="">
						  <span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'shopify_api_loc_id\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
						  	</div>
	        			</div>
					</div>
					<div class="form-group">
						<label class="control-label col-form-label col-sm-2 required" for="supplier name">Webhook Key:</label>
	        			<div class="col-sm-6 ac-box">
	    		 				<input class="form-control" id="<%=IDBConstants.SHOPIFY_WEBHOOK_KEY %>" name="<%=IDBConstants.SHOPIFY_WEBHOOK_KEY %>" type="TEXT" size="30" MAXLENGTH=100 value="<%=shopify_webhook_key%>">
	    		 		</div>
	    		 	</div> 
	    		 	<input type="hidden" name="<%=IDBConstants.SHOPIFY_API_LOC_ID %>" id="location" value="<%=shopify_api_loc_id%>">
				</form>
	        </div>
	        <div class="modal-footer">
	          <button type="button" id="btnSaveShopifyParameters" class="btn btn-success pull-left">Connect</button>
	        </div>
  		</div>

  	</div>
</div>

<script>
	var locations=[];
	
	$(document).ready(function(){
		$('#shopify_api_loc_id').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
			  display: 'name',  
			  source: substringMatcher(locations),
			  limit: 9999,
			  templates: {
			  empty: [
				  '<div style="padding:3px 20px">',
					'No results found',
				  '</div>',
				].join('\n'),
				suggestion: function(data) {
				return '<p  onclick="$(\'#location\').val(\''+data.id+'\')">' + data.name + '</p>';
				}
			  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			
		});
	});
	
	<%
	if(locations.size() > 0){
	for (int i=0;i<locations.size();i++){%>
		var loaction = new Object();
		<% Map loaction = (Map)locations.get(i);%>
		loaction.id="<%=BigDecimal.valueOf((Double)loaction.get("id"))%>";
		loaction.name="<%=loaction.get("name")%>";
		locations.push(loaction);
		if(<%=shopify_api_loc_id.equalsIgnoreCase(BigDecimal.valueOf((Double)loaction.get("id")).toString())%>){
			$("#shopify_api_loc_id").val("<%=loaction.get("name")%>");
		}
	<%}
	}
	%>
	$("#btnSaveShopifyParameters").click(function(){
		var data = $("#shopifyParametersForm").serialize();
		$.ajax({
			type : "POST",
			url : "../integrations/addShopifyParameters",
			data : data,
			dataType : "json",
			success : function(data) {
				if(data.ERROR_CODE == 100){
					$("#shopifyParametersModel").modal('hide');
					alert(data.MESSAGE);
				}else
	        		alert(data.MESSAGE);
			}
		});
	});
	
	
	var substringMatcher = function(strs) {
		  return function findMatches(q, cb) {
		    var matches, substringRegex;
		    // an array that will be populated with substring matches
		    matches = [];
		    // regex used to determine if a string contains the substring `q`
		    substrRegex = new RegExp(q, 'i');
		    // iterate through the pool of strings and for any string that
		    // contains the substring `q`, add it to the `matches` array
		    $.each(strs, function(i, str) {
		      if (substrRegex.test(str.value)) {
		        matches.push(str);
		      }
		    });
		    cb(matches);
		  };
	};
	
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

