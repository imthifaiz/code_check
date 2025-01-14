<%@page import="com.track.gates.Generator"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>
<%@include file="EmpSessionCheck.jsp"%>
<%
	String title = "Apply Claim";
%>
<jsp:include page="EmpHeader.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<%
	StrUtils StrUtils = new StrUtils();
	String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
	String empid = session.getAttribute("EMP_USER_ID").toString();
	String username = StrUtils.fString((String) session.getAttribute("EMP_LOGIN_USER"));
	
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
	EmployeeDAO employeeDAO = new EmployeeDAO();
	
	DateUtils dateutils = new DateUtils();
	String curtyear = dateutils.getYear();
	String cdate = dateutils.getDate();
	
  	ArrayList emplist = employeeDAO.getEmployeeListbyid(empid,plant);
 	Map empmap=(Map)emplist.get(0);
 	String repid = (String)empmap.get("REPORTING_INCHARGE");
 	
 	PlantMstDAO plantMstDAO = new PlantMstDAO();
	CurrencyDAO currencyDAO = new CurrencyDAO();
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String num = "1";
 	String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);
 	String zerovald = "0.0";
 	
 	//String uniqueID = UUID.randomUUID().toString();
 	Date date = new Date();
    long timeMilli = date.getTime();
    
    String uniqueID = empid +"-"+String.valueOf(timeMilli);

%>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script type="text/javascript" src="js/general.js"></script>
<style>
.payment-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
#emploader {
    position: absolute;
    left: 50%;
    top: 350px;
    z-index: 1;
    width: 150px;
    height: 150px;
    margin: -25px 0 0 -25px;
    border: 5px solid #f3f3f3;
    border-top: 5px solid #6495ed;
    border-radius: 50%;
    width: 50px;
    height: 50px;
    -webkit-animation: spin 1s linear infinite;
    animation: spin 1s linear infinite;
}

@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.claim-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -25%;
	top: 15px;
}

.fclaimup {
   opacity: 0;
   position: absolute;
   z-index: -1;
}
</style>
<script>
  $(window).on('load', function() {
	 $("#emploader").hide();
});
</script>

    <div id="emploader"></div>

    <!-- Main content -->
    <section class="content">		

      <!-- Main row -->
      <div class="row">
      <div class="col-md-12">
          	<%if(resultnew.equals("") || resultnew == null){}else{ %>
		  <div class="alert alert-danger alert-dismissible" style="margin:0 auto;">
		    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		    <%=resultnew %>
		  </div>
		  <%} %>
		  
		  <%if(rsuccess.equals("") || resultnew == null){}else{ %>
			    <div class="alert alert-success alert-dismissible" style="margin:0 auto;">
		    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		    <%=rsuccess %>
		  </div>
		  <%} %>
      </div>
      <div class="col-md-12">
                      <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title"><%=title %></h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
            <div class="row">
      
      		<form id="claimform" class="form-vertical" name="form" action="/track/HrClaimServlet?Submit=SAVE"  method="post" onsubmit="return validate()">
				<input type="hidden" name="plant" value="<%=plant%>">
				<input type="hidden" name="username" value="<%=username%>">
				<input type="hidden" name="empid" value="<%=empid%>">
				<input type="hidden" name="repid" value="<%=repid%>">
				<input type="hidden" name="cdate" value="<%=cdate%>">
				<input type="hidden" name="numberOfDecimal" value=<%=numberOfDecimal%>>
				
				<div class="row" style="margin: 0px;width: 95%;margin-left: 15px;">
					<table class="table table-bordered line-item-table claim-table">
						<thead>
							<tr>
								<th>Date</th>
								<th>Claim</th>
								<th>Description</th>
								<th>From Place</th>
								<th>To Place</th>
								<th>Distance (km)</th>
								<th>Amount</th>
								<th>Attachments</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="text-center">
									<input type="hidden" class="attachtocken" name="clkey" value="<%=uniqueID%>"> 
									<input class="form-control text-left datepicker" name="claimdate" type="text" placeholder="" maxlength="15" value="<%=cdate%>"> 
								</td>
								<td class="text-center">
									<input name="claimid" type="hidden"> 
									<input class="form-control text-left claimname" name="claimmane" type="text" onclick="deductpopup(this)" readonly> 
								</td>
								<td class="text-center">
									<textarea  name="description" class="form-control text-left" style="height: 34px;width: 150px;" maxlength="1000" placeholder="Max 1000 characters"></textarea>
								</td>
								<td class="text-center">
									<input class="form-control text-left" name="fromplace" type="text" placeholder="" maxlength="50"> 
								</td>
								<td class="text-center">
									<input class="form-control text-left" name="toplace" type="text" placeholder="" maxlength="50"> 
								</td>
								<td class="text-center">
									<input class="form-control text-left" name="distance" type="text" value="<%=zerovald%>" maxlength="50" onchange="validatedistance(this)"> 
								</td>
								<td class="text-center">
									<input class="form-control text-left" name="amount" type="text" value="<%=zeroval%>" maxlength="50" onchange="validateamount(this)"> 
								</td>
								<td class="text-center">
									<input type="hidden" class="attachtocken" name="attkey" value="<%=uniqueID%>">
									<input type="hidden" name="attnumberid" value="<%=timeMilli%>">
									<div class="col-sm-4">
										<label for="<%=uniqueID%>" style="color: #337ab7;padding-top: 5px;"><i class="fa fa-paperclip"></i></label>
										<input type="file" class="form-control fclaimup claimApplyAttach" name="<%=uniqueID%>" id="<%=uniqueID%>" multiple="true">
									</div>
									<div id="<%=timeMilli%>" class="col-sm-8">
									</div>
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
								onclick="addRow()">+ Add another claim</a>
						</div>
					</div>
				</div>
				<div class="row form-group">      
					<div class="col-sm-12 txn-buttons">
						<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
						<button type="submit" class="btn btn-success">Save</button>&nbsp;&nbsp;
						<!-- <button type="button" class="Submit btn btn-default" onClick="add_attachments();"><b>attach</b></button> -->
					</div>
				</div>
			</form>
			
			</div>
			</div>
			</div>
			
			
      </div>
      </div>
      <!-- /.row -->
    </section>
    <!-- /.content -->

  <!-- /.content-wrapper -->
<%@include file="Payrolladdmstmodel.jsp"%>

<script>
	$(document).ready(function(){
		
		setTimeout(function() {
		    $('.alert').fadeOut('fast');
		}, 4000);
		
		$("#apclaim").addClass('empactive');
		
		$('.startloader').click(function(){
			$(".content").css("display", "none");
			$("#emploader").show();
	  	});
		
		$( ".datepicker" ).datepicker({
		    changeMonth: true,
		    changeYear: true,
			dateFormat: 'dd/mm/yy',
		  });
		
		
		$('.claimname').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{		  
			  display: 'ADDITION_NAME',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/HrPayrollAdditionMstServlet";
				$.ajax( {
					type : "GET",
					url : urlStr,
					async : true,
					data : {
						CMD : "GET_PAYROLL_ADDTION_MST_DROPDOWN",
						DTYPE : "claim",
						QUERY : ""
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.PAYADDMSTLIST);
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
			    		//return '<p>' + data.ADDITION_NAME + '</p>';
			    		return '<div onclick="setclaimid(this,\''+data.ID+'\')"><p>' + data.ADDITION_NAME + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				/* menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#PayaddmstModal"><a href="#"> + New Addition</a></div>'); */
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		
	});
	
	function onClear()
	{
	  document.form.action  = "EmpClaimApply.jsp?action=Clear";
	  document.form.submit();
	}
	
	function setclaimid(obj , id){
		$(obj).closest('tr').find('input[name = "claimid"]').val(id); 
	}
	
	function validateamount(obj)
	{
		var invalue = $(obj).val();
		var numberOfDecimal = $("input[name ='numberOfDecimal']").val();
		var zeroval = parseFloat("0").toFixed(numberOfDecimal);
		var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(invalue.match(decimal) || invalue.match(numbers)) 
		{ 
			var amount=parseFloat(invalue).toFixed(numberOfDecimal);
			$(obj).val(amount);	
		}else{
			$(obj).val(zeroval);
			alert("Please Enter Valid Amount");
		}
		netamountcal();
	}
	
	function validatedistance(obj)
	{
		var invalue = $(obj).val();
		var numberOfDecimal = "1";
		var zeroval = parseFloat("0").toFixed(numberOfDecimal);
		var decimal=  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(invalue.match(decimal) || invalue.match(numbers)) 
		{ 
			var amount=parseFloat(invalue).toFixed(numberOfDecimal);
			$(obj).val(amount);	
		}else{
			$(obj).val(zeroval);
			alert("Please Enter Valid Distance");
		}
		netamountcal();
	}
	
	function addRow() {
		var tok = Math.floor(Math.random() * 26) + Date.now()
		var atttoken = "<%=empid%>"+"-"+tok;
		var body = "";
		body += '<tr>';
		body += '<td class="text-center">';
		body += '<input type="hidden" class="attachtocken" name="clkey" value="'+atttoken+'">';
		body += '<input class="form-control text-left datepicker" name="claimdate" type="text" placeholder="" maxlength="15" value="<%=cdate%>">';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<input name="claimid" type="hidden">'; 
		body += '<input class="form-control text-left claimname" type="text" onclick="deductpopup(this)" readonly>'; 
		body += '</td>';
		body += '<td class="text-center">';
		body += '<textarea  name="description" class="form-control text-left" style="height: 34px;width: 150px;" maxlength="1000" placeholder="Max 1000 characters"></textarea>';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left" name="fromplace" type="text" placeholder="" maxlength="50">';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left" name="toplace" type="text" placeholder="" maxlength="50">';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left" name="distance" type="text" value="<%=zerovald%>" maxlength="50" onchange="validatedistance(this)">';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left" name="amount" type="text" value="<%=zeroval%>" maxlength="50" onchange="validateamount(this)">'; 
		body += '</td>';
		body += '<td class="text-center" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle claim-action" aria-hidden="true"></span>';
		body += '<input type="hidden" class="attachtocken" name="attkey" value="'+atttoken+'">';  
		/* body += '<label for="'+atttoken+'" style="color: #337ab7;padding-top: 5px;"><i class="fa fa-paperclip"></i></label>';
		body += '<input type="file" class="form-control fclaimup" name="fileclaim" id="'+atttoken+'" multiple="true">';
		body += '</td>';  */
		body += '<input type="hidden" name="attnumberid" value="'+tok+'">';
		body += '<div class="col-sm-4">';
		body += '<label for="'+atttoken+'" style="color: #337ab7;padding-top: 5px;"><i class="fa fa-paperclip"></i></label>';
		body += '<input type="file" class="form-control fclaimup claimApplyAttach" name="'+atttoken+'" id="'+atttoken+'" multiple="true">';
		body += '</div>';
		body += '<div id="'+tok+'" class="col-sm-8">';
		body += '</div>';
		body += '</td>';
		body += '</tr>';
		$(".claim-table tbody").append(body);
		removerowclasses();
		addrowclasses();
	}

	$(".claim-table tbody").on('click', '.claim-action', function() {
		$(this).parent().parent().remove();
	});
	
	function removerowclasses(){
		$(".claimname").typeahead('destroy');
	}
	
	function addrowclasses(){
		$( ".datepicker" ).datepicker({
		    changeMonth: true,
		    changeYear: true,
			dateFormat: 'dd/mm/yy',
		  });
		
		$('.claimname').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{		  
			  display: 'ADDITION_NAME',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/HrPayrollAdditionMstServlet";
				$.ajax( {
					type : "GET",
					url : urlStr,
					async : true,
					data : {
						CMD : "GET_PAYROLL_ADDTION_MST_DROPDOWN",
						DTYPE : "claim",
						QUERY : ""
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.PAYADDMSTLIST);
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
			    		//return '<p>' + data.ADDITION_NAME + '</p>';
			    		return '<div onclick="setclaimid(this,\''+data.ID+'\')"><p>' + data.ADDITION_NAME + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				/* menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#PayaddmstModal"><a href="#"> + New Addition</a></div>'); */
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		
		$(".claimApplyAttach").change(function(){
			var attachdata = $(this).closest('td').find('input[name = "attnumberid"]').val();
			var files = $(this)[0].files.length;
			var sizeFlag = false;
				if(files > 5){
					$(this)[0].value="";
					alert("You can upload only a maximum of 5 files");
					$("#"+attachdata).html("");
				}else{
					for (var i = 0; i < $(this)[0].files.length; i++) {
					    var imageSize = $(this)[0].files[i].size;
					    if(imageSize > 2097152 ){	
					    	sizeFlag = true;
					    }
					}
					if(sizeFlag){
						$(this)[0].value="";
						alert("Maximum file size allowed is 2MB, please try with different file.");
						$("#"+attachdata).html("");
					}else{
						$("#"+attachdata).html(files +" files attached");
					}
					
				}
			});
	}
	
	
	function validate() {
		var isItemValid = true;

		$("input[name ='claimdate']").each(function() {
			if ($(this).val() == "") {
				$(this).focus();
				isItemValid = false;
			}
		});
		if (!isItemValid) {
			alert("The date field cannot be empty.");
			return false;
		}

		$("input[name ='claimmane']").each(function() {
			if ($(this).val() == "") {
				$(this).focus();
				isItemValid = false;
			}
		});
		if (!isItemValid) {
			alert("The clime field cannot be empty.");
			return false;
		}
		
		$("input[name ='amount']").each(function() {
			if ($(this).val() == "") {
				$(this).focus();
				isItemValid = false;
			}
		});
		if (!isItemValid) {
			alert("The amount field cannot be empty.");
			return false;
		}
		
		add_attachments();
		
		return true;
	}
	
	$(".claimApplyAttach").change(function(){
		var attachdata = $(this).closest('td').find('input[name = "attnumberid"]').val();
		var files = $(this)[0].files.length;
		var sizeFlag = false;
			if(files > 5){
				$(this)[0].value="";
				alert("You can upload only a maximum of 5 files");
				$("#"+attachdata).html("");
			}else{
				for (var i = 0; i < $(this)[0].files.length; i++) {
					var filename = $(this)[0].files[i].name;
					//var fileExt = filename.split('.')[filename.split('.').length-1];
				    var imageSize = $(this)[0].files[i].size;
				    if(imageSize > 2097152 ){	
				    	sizeFlag = true;
				    }
				}
				if(sizeFlag){
					$(this)[0].value="";
					alert("Maximum file size allowed is 2MB, please try with different file.");
					$("#"+attachdata).html("");
				}else{
					$("#"+attachdata).html(files +" files attached");
				}
				
			}
		});
	

	function add_attachments(){
	    var formData = new FormData($('#claimform')[0]);
	    $.ajax({
	        type: 'post',
	        url: "/track/HrClaimServlet?Submit=add_attachments",
		    data:  formData,//{key:val}
		    contentType: false,
		    processData: false,
	        success: function (data) {
	        },
	        error: function (data) {
	        }
	    });
	  }
	
</script>


<jsp:include page="EmpFooter.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>