<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%
String fieldDesc=StrUtils.fString(request.getParameter("result"));
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="Create Department" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.DEPARTMENT%>" />
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/fileshowcasedesign.css">
<script type="text/javascript" src="js/general.js"></script>
<style>
.extraInfo {
    border: 1px dashed #555;
    background-color: #f9f8f8;
    border-radius: 3px;
    color: #555;
    padding: 15px;
}
.offset-lg-7 {
    margin-left: 58.33333%;
}
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td, #table3>tbody>tr>td {
	border: none;
}

#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}
.emptype-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -5%;
    top: 15px;
}
.backpageul
{
	background-color: rgb(255, 255, 255);
    padding: 0px 10px;
    margin-bottom: 0px;
    margin-top: 15px;
}
.underline-on-hover:hover {
  		text-decoration: underline;
	}
</style>
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box"> 
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../payroll/department"><span class="underline-on-hover">Department Summary</span> </a></li>   
                <li>Create Department</li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title">Create Department</h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../payroll/department'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>
		
		<div class="container-fluid">
			<form id="" class="form-vertical" name="form" action="/track/HrDepartmentServlet?Submit=SAVE"  method="post" onsubmit="return validateDepartment()">
				<div class="row" style="margin: 0px;width: 95%;margin-left: 15px;">
					<table class="table table-bordered line-item-table emptype-table">
						<thead>
							<tr>
								<th>Department</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center">
									<input class="form-control text-left" name="department" type="text" placeholder="Enter Department" onchange="checkitem(this.value)" maxlength="100"> 
								</td>								
							</tr>
						</tbody>
					</table>
				</div>
				<div class="form-group">
					<div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;"
								onclick="addRow()">+ Add another Department</a>
						</div>
					</div>
				</div>
				<div class="row form-group">      
					<div class="col-sm-12 txn-buttons">
					<button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>

<script type="text/javascript">

function validateDepartment(){
	var isItemValid = true;
	
	$("input[name ='department']").each(function() {
	    if($(this).val() == ""){	
	    	$(this).focus();
	    	isItemValid = false;
	    }
	});
	if(!isItemValid){
		alert("The Department field cannot be empty.");
		return false;
	}
	
	return true;
}

function addRow(){

	var body="";
	body += '<tr>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true"></span>';
	body += '<input class="form-control text-left" name="department" type="text" placeholder="Enter Department" maxlength="100" onchange="checkitem(this.value)" >';
	body += '</td>';
	body += '</tr>';
	$(".emptype-table tbody").append(body);
}

$(".emptype-table tbody").on('click','.emptype-action',function(){
    $(this).parent().parent().remove();
});

function checkitem(itemvalue)
{	
	
	 if(itemvalue!="" || itemvalue.length>0 ) {
		
		var urlStr = "/track/HrDepartmentServlet";
		$.ajax( {
			type : "GET",
			url : urlStr,
			async : true,
			data : {
				QUERY : itemvalue,
				PLANT : "<%=plant%>",
				CMD : "Department_CHECK"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						alert("Department Already Exists");
						return false;	
						
					} 
					else 
						return true;
					    
				}
			});
		 return true;
	 }
}

function onClear()
{
	document.form.action  = "CreateDepartment.jsp";
	document.form.submit();
}

</script>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="Create Department" />
</jsp:include>
