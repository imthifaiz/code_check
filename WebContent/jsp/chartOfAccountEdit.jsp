<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.util.StrUtils"%>
<%
	String title = "Edit Chart of Accounts";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
%>
<%@include file="sessionCheck.jsp"%>

<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
    <jsp:param name="mainmenu" value="<%=IConstants.CHART_OF_ACCOUNTS%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>

<div class="container-fluid m-t-20">

	<div class="box">
<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../banking/chartofaccounts"><span class="underline-on-hover">Chart of Accounts</span> </a></li>                      
                <li><label>Edit Chart of Accounts</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">

			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"	class="box-title pull-right" onclick="window.location.href='../banking/chartofaccounts'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>	

		</div>
		<div class="box-body">
		<form id="createBillForm" class="form-horizontal" name="form1" action="/track/ChartOfAccountServlet?action=UpdateAccount"  method="post" enctype="multipart/form-data" >
		<div class="row" style="margin: 0px;">
		<table class="table table-bordered line-item-table bill-table">
			<thead>
			  <tr>
			  	<!-- <th class="bill-id">ID</th>	 -->		  
				<th class="bill-acc">Account Code</th>
				<th class="bill-acc">Account Name</th>
				<th class="bill-item">Account Group</th>
				<th class="bill-item">Account Type</th>
				<!-- <th class="item-cost text-right">Balance</th> -->
			  </tr>
			</thead>
			<tbody>
			 
			</tbody>
			</table>
</div>
		<div class="row">
			<div class="col-sm-12 txn-buttons">
			<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../banking/chartofaccounts'">Cancel</button>
			</div>
		</div>
</form>
</div>
</div>
</div>
<script type="text/javascript">
$(document).ready(function() {
	loadTable();
				});
function loadTable(){	
	$.ajax({
		type : "POST",
		url : "/track/ChartOfAccountServlet",
		async : true,
		data : {
			action : "read"
		},
		dataType : "json",
		success : function(data) {
			loadDataTable(data.orders);
		}
	});
}

function loadDataTable(orders){
	
	var body="";
	$.each(orders, function( key, data ) {	
		
		body += '<tr>';
		/* body += '<td class="bill-id" style="width:10%">';
		body += '<input type="text" name="id" class="form-control" value="'+data.id+'" readonly="readonly">';
		body += '</td>'; */
		body += '<td class="bill-code" style="width:15%">';
		body += '<input type="text" name="id" class="form-control" value="'+data.account_code+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="bill-acc" style="width:33%">';
		if(data.issubaccount == "1")
			body += '<div style="margin-left: 10px"><input type="text" name="account_name" class="form-control" value="'+data.account_name+'" readonly="readonly"></div>';
		else
			body += '<div style="margin-left: 0px"><input type="text" name="account_name" class="form-control" value="'+data.account_name+'" readonly="readonly"></div>';	
		body += '</td>';
		body += '<td class="bill-item">';
		body += '<input type="text" name="account_type" class="form-control" value="'+data.account_type+'" readonly="readonly">';
		body += '</td>';
		body += '<td class="bill-item"><input type="text" name="account_det_type" class="form-control" value="'+data.account_det_type+'" readonly="readonly"></td>';
		//body += '<td class="item-cost text-right"><input type="text" name="account_balance" class="form-control text-right" value="'+data.account_balance+'" readonly="readonly"></td>';		
		body += '</tr>';
	});
	
	$(".bill-table tbody").html(body);
}

$("#btnBillOpen").click(function(){
	$("#createBillForm").submit();
});

</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>