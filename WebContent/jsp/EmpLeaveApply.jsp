<%@page import="com.track.gates.Generator"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>
<%@include file="EmpSessionCheck.jsp"%>
<%
	String title = "Apply Leave";
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
	
  	ArrayList emplist = employeeDAO.getEmployeeListbyid(empid,plant);
 	Map empmap=(Map)emplist.get(0);
 	String repid = (String)empmap.get("REPORTING_INCHARGE");

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
      
      		<form id="" class="form-horizontal" name="form" action="/track/HrLeaveApplyServlet?Submit=EMP_SAVE"  method="post" enctype="multipart/form-data" onsubmit="return validateLeaveApply()">
				<div class="col-md-12">
				<div class="col-md-6">
				<input type="hidden" name="plant" value="<%=plant%>">
				<input type="hidden" name="username" value="<%=username%>">
				<input type="hidden" name="lyear" value="<%=curtyear%>">
				<input type="hidden" name="empid" value="<%=empid%>">
				<input type="hidden" name="repid" value="<%=repid%>">
				<input type="hidden" name="lvtid" value="">
				<input type="hidden" name="nodays" value="0">
				<input type="hidden" name="leavebal" value="0">
				<input type="hidden" name="lop" value="0">
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-4 required">Leave Type</label>
					<div class="col-sm-8">
						<input type="text" class="form-control leavetype" name="leavetype" placeholder="Select a leave type" maxlength="100">
					</div>
				</div>
				
				<div class="form-group hidetdays" hidden>

				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-4 required">Leave Duration</label>
					<div class="col-sm-8">
						<select class="form-control" name="leaveduration" id="smday" onchange="setdays(this)">
						    <option value="1">Single day</option>
						    <option value="2">Multiple days</option>
						 </select>
					</div>
				</div>
				<div class="form-group noofdays">
					<!-- <div class="col-sm-2">
					</div>
					<div class="col-sm-2">
						<b><p>Number of Days 0.0</p></b> 
					</div> -->
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-offset-4 col-sm-6 required">Start Date</label>
				</div>
				<div class="form-group">
					<div class="col-sm-4">
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control sdatepickerleave" id="sdate" onchange="blocktodate(this)" name="sdate" readonly>
					</div>
					<div class="col-sm-4">
						<select class="form-control" name ="sdaystatus" id="sdaystatus" onchange="getnoofdays()">
							<option value="Fullday">All day</option>
							<option value="Morning">First part of the day</option>
							<option value="Afternoon">Second part of the day</option>
						</select>
					</div>
				</div>
				<div class="form-group hideenddate" hidden>
					<label class="control-label col-form-label col-sm-offset-4 col-sm-3 required">End Date</label>
				</div>
				<div class="form-group hideenddate" hidden>
					<div class="col-sm-4">
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control edatepickerleave" id="edate" onchange="ehalfdaystatus(this)" name="edate" readonly>
					</div>
					<div class="col-sm-4">
						<select class="form-control" name ="edaystatus" id="edaystatus" onchange="getnoofdays()">
						 	<option value="Fullday">All day</option>
							<option value="Morning">First part of the day</option>
						</select>
					</div>
				</div>
				
 				<!-- <div class="form-group">
						<div class="form-inline">
							<label class="control-label col-form-label col-sm-2" for="email">Attach Files(s)</label>
							<div class="attch-section col-sm-2">
								<input type="file" class="form-control input-attch" id="applyLeaveAttach" name="file" multiple="true">
								<div class="input-group">
									<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
									<button type="button" class="btn btn-sm btn-attch">Upload File</button>
								</div>								
							</div>
						</div>
						<div id="applyLeaveAttachNote" class="col-sm-4">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
				</div> -->
				
				
				</div>
				<div class="col-md-6">
				<div class="form-group">
						<div class="form-inline">
							<label class="control-label col-form-label col-sm-3" for="email">Attach Files(s)</label>
							<div class="attch-section col-sm-4">
								<input type="file" class="form-control input-attch" id="applyLeaveAttach" name="file" multiple="true">
								<div class="input-group">
									<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
									<button type="button" class="btn btn-sm btn-attch">Upload File</button>
								</div>								
							</div>
						</div>
						<div id="applyLeaveAttachNote" class="col-sm-5">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
				</div>
				</div>
				</div>
				<div class="col-md-6">
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-10" style="margin-left: 35%;">
							<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
							<button type="submit" class="btn btn-success startloader">Apply</button>
						</div>
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


<script>
	$(document).ready(function(){
		
		setTimeout(function() {
		    $('.alert').fadeOut('fast');
		}, 4000);
		
		$("#apleave").addClass('empactive');
		
		$('.startloader').click(function(){
			$(".content").css("display", "none");
			$("#emploader").show();
	  	});
		
		$('.leavetype').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{		  
			  display: 'LEAVETYPE',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/HrLeaveTypeServlet";
				$.ajax( {
					type : "GET",
					url : urlStr,
					async : true,
					data : {
						CMD : "GET_EMP_LEAVE_TYPE_DROPDOWN",
						EMPTYPEID : document.form.empid.value,
						LYEAR : document.form.lyear.value,
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.LEAVETYPELIST);
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
			    		return '<div onclick="setleavetype(this,\''+data.ID+'\',\''+data.TOTALENTITLEMENT+'\',\''+data.LEAVEBALANCE+'\',\''+data.LEAVETYPE+'\',\''+data.LOP+'\')"><p>' + data.LEAVETYPE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				/*menuElement.after( '<div class="shipmentAddBtn footer"  data-toggle="modal" data-target="#shipmentModal"><a href="#"> + Add Shipment</a></div>');*/
				/* menuElement.after( '<div class="accountAddBtn footer lvtypepopup"  data-toggle="modal" data-target="#create_leave_type"><a href="#"> + New Leave Type</a></div>'); */
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
				removerowclasses();
			});
		
		
		$("#applyLeaveAttach").change(function(){
			var files = $(this)[0].files.length;
			var sizeFlag = false;
				if(files > 5){
					$(this)[0].value="";
					alert("You can upload only a maximum of 5 files");
					$("#applyLeaveAttachNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
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
						$("#applyLeaveAttachNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
					}else{
						$("#applyLeaveAttachNote").html(files +" files attached");
					}
					
				}
			});
	    
	});
	
	function onClear()
	{
	  document.form.action  = "EmpLeaveApply.jsp?action=Clear";
	  document.form.submit();
	}
	
	
	function removerowclasses(){
		 $("#sdaystatus").find('option').remove();
		 $("#sdaystatus").append(new Option("All day", "Fullday"));
		 $("#sdaystatus").append(new Option("First part of the day", "Morning"));
		 $("#sdaystatus").append(new Option("Second part of the day", "Afternoon"));
		 $("input[name ='edate']").val("");
		 $('#smday').prop('selectedIndex',0);
		 $('#edaystatus').prop('disabled',true);
		 $('#edate').prop('disabled',true);
		 $(".hideenddate").hide();
	}
	
	
	
	
	function setleavetype(obj,lid,tdays,baldays,ltype,lop){
		$("input[name ='lvtid']").val(lid);
		$("input[name ='leavebal']").val(baldays);
		$("input[name ='lop']").val(lop);
		var body = "";
		body += '<div class="col-sm-4">';
		body += '</div>';
		body += '<div class="col-sm-4">';
		body += '<b><p style="font-size: 90%;">'+ltype+' Balance '+baldays+'</p></b>';
		body += '</div>';
		body += '<div class="col-sm-4">';
		body += '<b><p style="font-size: 90%;">Total Entitlement '+tdays+'</p></b>';
		body += '</div>';
		$(".hidetdays").html(body);
		$(".hidetdays").show();
		
		var datesForDisable = new Array();
		$.ajax({
		       url: '/track/HrLeaveApplyServlet',
		       method: 'GET',
		       dataType: 'json',
		       data : {
					PLANT : document.form.plant.value,
					CMD: "GET_HOLIDAY_LEAVE_DATE",
					EMPID: document.form.empid.value
				},
		       success: function(data) {
		    	   console.log(data);
		    	for(var dataIndex = 0; dataIndex < data.BLOCKLIST.length; dataIndex ++){
	   				var hdate = data.BLOCKLIST[dataIndex].BLOCKING_DATE;
	   				datesForDisable.push(hdate);
	   			}  
		       }
		     });
		     

		     $('.sdatepickerleave').datepicker({
		    	 dateFormat: 'dd/mm/yy',
		         beforeShowDay: function(date){
		             var string = jQuery.datepicker.formatDate('dd/mm/yy', date);
		             return [ datesForDisable.indexOf(string) == -1 ]
		         }
		     });
		     
		   $('.edatepickerleave').datepicker({
		    	 dateFormat: 'dd/mm/yy',
		    	 minDate: new Date(getsdate("Y"), getsdate("M") - 1, getsdate("D")),
		         beforeShowDay: function(date){
		             var string = jQuery.datepicker.formatDate('dd/mm/yy', date);
		             return [ datesForDisable.indexOf(string) == -1 ]
		         }
		     }); 
	}
	
	function setdays(obj){
		var muldays = $(obj).val();
		if(muldays == "1"){
			 $("#sdaystatus").find('option').remove();
			 $("#sdaystatus").append(new Option("All day", "Fullday"));
			 $("#sdaystatus").append(new Option("First part of the day", "Morning"));
			 $("#sdaystatus").append(new Option("Second part of the day", "Afternoon"));
			 $('#edaystatus').prop('disabled',true);
			 $('#edate').prop('disabled',true);
			 $(".hideenddate").hide();
		}else{
			 $("#sdaystatus").find('option').remove();
			 $("#sdaystatus").append(new Option("All day", "Fullday"));
			 $("#sdaystatus").append(new Option("Second part of the day", "Afternoon"));
			 $('#edaystatus').prop('disabled',false);
			 $('#edate').prop('disabled',false);
			 $(".hideenddate").show();
		}
	}
	
	function getsdate(dmy){
		var sdate = document.form.sdate.value;
		var sp = sdate.split("/");
		if(dmy == "D"){
			return sp[0];
		}else if(dmy == "M"){
			return sp[1];
		}else{
			return sp[2];
		}
	}
	
	function blocktodate(obj){
		var sdate = $(obj).val();
		var leaveduration =  $("select[name ='leaveduration']").val();
		if(leaveduration != 1){
			var sp = sdate.split("/");
			var datesForDisable = new Array();
			$.datepicker._clearDate('.edatepickerleave');
			var minDate = $( ".edatepickerleave" ).datepicker( "option", "minDate" );
			var date = new Date(sp[2], sp[1] - 1, sp[0])
			date.setDate(date.getDate() + 1);
			$( ".edatepickerleave" ).datepicker( "option", "minDate", date );
		}
		var lvbal = $("input[name ='leavebal']").val();
		$.ajax({
		       url: '/track/HrLeaveApplyServlet',
		       method: 'GET',
		       dataType: 'json',
		       data : {
					PLANT : document.form.plant.value,
					CMD: "GET_FULL_OR_HALFDAY_STATUS",
					EMPID: document.form.empid.value,
					LEAVEDATE: sdate
				},
		       success: function(data) {
		    	    if(data.STATUS == "OK"){
		    	    	if(data.DAY == "Fullday"){
		    	    		if(leaveduration == "1"){
		    	    			if(lvbal == "0.5"){
		    	    				 $("#sdaystatus").find('option').remove();
				    				 $("#sdaystatus").append(new Option("First part of the day", "Morning"));
				    				 $("#sdaystatus").append(new Option("Second part of the day", "Afternoon"));
				    				// getnoofdays();
				    				 getnoofdayssingle("Morning",sdate);
		    	    			}else{
		    	    				$("#sdaystatus").find('option').remove();
				    				 $("#sdaystatus").append(new Option("All day", "Fullday"));
				    				 $("#sdaystatus").append(new Option("First part of the day", "Morning"));
				    				 $("#sdaystatus").append(new Option("Second part of the day", "Afternoon"));
				    				 //getnoofdays();
				    				 getnoofdayssingle("Fullday",sdate);
		    	    			}
		    	    		}else{
		    	    			$("#sdaystatus").find('option').remove();
			    				 $("#sdaystatus").append(new Option("All day", "Fullday"));
			    				 $("#sdaystatus").append(new Option("Second part of the day", "Afternoon"));
			    				 getnoofdays();
		    	    		}
		    	    	}else{
		    	    		
		    	    		if(leaveduration == "1"){
		    	    			if(data.DAYSESSION == "Morning"){
			    	    			$("#sdaystatus").find('option').remove();
			    	   				$("#sdaystatus").append(new Option("First part of the day", "Morning"));
			    	   				getnoofdayssingle("Morning",sdate);
			    	    		}else{
			    	    			 $("#sdaystatus").find('option').remove();
				    				 $("#sdaystatus").append(new Option("Second part of the day", "Afternoon"));
				    				 getnoofdayssingle("Afternoon",sdate);
			    	    		}
		    	    		}else{
		    	    			if(data.DAYSESSION == "Morning"){
			    	    			$("#sdaystatus").find('option').remove();
			    	   				$("#sdaystatus").append(new Option("First part of the day", "Morning"));
			    	   				getnoofdays();
			    	    		}else{
			    	    			 $("#sdaystatus").find('option').remove();
				    				 $("#sdaystatus").append(new Option("Second part of the day", "Afternoon"));
				    				 getnoofdays();
			    	    		}
		    	    		}
		    	    		
		    	    	}
		    	    }
		       }
		     });
	}
	
	function ehalfdaystatus(obj){
		var edate = $(obj).val();
		$.ajax({
		       url: '/track/HrLeaveApplyServlet',
		       method: 'GET',
		       dataType: 'json',
		       data : {
					PLANT : document.form.plant.value,
					CMD: "GET_FULL_OR_HALFDAY_STATUS",
					EMPID: document.form.empid.value,
					LEAVEDATE: edate
				},
		       success: function(data) {
		    	    if(data.STATUS == "OK"){
		    	    	if(data.DAY == "Fullday"){
		    	    		 $("#edaystatus").find('option').remove();
		    				 $("#edaystatus").append(new Option("All day", "Fullday"));
		    				 $("#edaystatus").append(new Option("First part of the day", "Morning"));
		    				// getnoofdays();
		    				 getnoofdaysdouble();
		    	    	}else{
		    	    		if(data.DAYSESSION == "Morning"){
		    	    			$("#edaystatus").find('option').remove();
		    	   				$("#edaystatus").append(new Option("First part of the day", "Morning"));
		    	   				getnoofdays();
		    	    		}else{
		    	    			 $("#edaystatus").find('option').remove();
		    	    			 $("#edaystatus").append(new Option("Second part of the day", "Afternoon"));
		    	    			 getnoofdays();
		    	    		}
		    	    	}
		    	    }
		       }
		     });
	}
	
	function getnoofdays(){
		var muldays = $("select[name ='leaveduration']").val();
		var lvbal = $("input[name ='leavebal']").val();
		var lop = $("input[name ='lop']").val();
		if(muldays == "1"){
			var sdaystatus = $("select[name ='sdaystatus']").val();
			var NDAYS = "0";
			if(sdaystatus == "Fullday"){
				NDAYS = "1";
			}else{
				NDAYS = "0.5";
			}
			if(lop == "1"){
				var body = "";
		   		body += '<div class="col-sm-4">';
		   		body += '</div>';
		   		body += '<div class="col-sm-4">';
		   		body += '<b><p>Number of Days '+NDAYS+'</p></b>';
		   		body += '</div>';
		   		$(".noofdays").html("");
		   		$(".noofdays").html(body);
		   		$("input[name ='nodays']").val(NDAYS);
			}else{
				if(parseFloat(NDAYS) <= parseFloat(lvbal)){
					var body = "";
			   		body += '<div class="col-sm-4">';
			   		body += '</div>';
			   		body += '<div class="col-sm-4">';
			   		body += '<b><p>Number of Days '+NDAYS+'</p></b>';
			   		body += '</div>';
			   		$(".noofdays").html("");
			   		$(".noofdays").html(body);
			   		$("input[name ='nodays']").val(NDAYS);
				}else{
			   		body += '<div class="col-sm-4">';
			   		body += '</div>';
			   		body += '<div class="col-sm-4">';
			   		body += '<b><p>Number of Days 0.0</p></b>';
			   		body += '</div>';
			   		$(".noofdays").html("");
			   		$(".noofdays").html(body);
			   		$("input[name ='nodays']").val("0.0");
			   		$("input[name ='sdate']").val("");
				}
			}
			
			
		}else{
			$.ajax({
			       url: '/track/HrLeaveApplyServlet',
			       method: 'GET',
			       dataType: 'json',
			       data : {
						PLANT : document.form.plant.value,
						CMD: "GET_NO_OF_DAYS",
						EMPID: document.form.empid.value,
						STRDATE: document.form.sdate.value,
						ENDDATE: document.form.edate.value,
						SSTATUS: document.form.sdaystatus.value,
						ESTATUS: document.form.edaystatus.value
					},
			       success: function(data) {
			    	   
			    	   if(lop == "1"){
			    		   var body = "";
					   		body += '<div class="col-sm-4">';
					   		body += '</div>';
					   		body += '<div class="col-sm-4">';
					   		body += '<b><p>Number of Days '+data.NDAYS+'</p></b>';
					   		body += '</div>';
					   		$(".noofdays").html("");
					   		$(".noofdays").html(body);
					   		$("input[name ='nodays']").val(data.NDAYS);
			    	   }else{
				   		
				    	   if(parseFloat(data.NDAYS) <= parseFloat(lvbal)){
								var body = "";
						   		body += '<div class="col-sm-4">';
						   		body += '</div>';
						   		body += '<div class="col-sm-4">';
						   		body += '<b><p>Number of Days '+data.NDAYS+'</p></b>';
						   		body += '</div>';
						   		$(".noofdays").html("");
						   		$(".noofdays").html(body);
						   		$("input[name ='nodays']").val(data.NDAYS);
							}else{
						   		body += '<div class="col-sm-4">';
						   		body += '</div>';
						   		body += '<div class="col-sm-4">';
						   		body += '<b><p>Number of Days 0.0</p></b>';
						   		body += '</div>';
						   		$(".noofdays").html("");
						   		$(".noofdays").html(body);
						   		$("input[name ='nodays']").val("0.0");
						   		$("input[name ='sdate']").val("");
							}
			    	   }
			       }
					
			     });
		}
		
	}
	
	function getnoofdayssingle(sdaystatus,sdate){
		var muldays = $("select[name ='leaveduration']").val();
		var lvbal = $("input[name ='leavebal']").val();
		var lop = $("input[name ='lop']").val();
			var NDAYS = "0";
			if(sdaystatus == "Fullday"){
				NDAYS = "1";
			}else{
				NDAYS = "0.5";
			}
			if(lop == "1"){
				var body = "";
		   		body += '<div class="col-sm-4">';
		   		body += '</div>';
		   		body += '<div class="col-sm-4">';
		   		body += '<b><p>Number of Days '+NDAYS+'</p></b>';
		   		body += '</div>';
		   		$(".noofdays").html("");
		   		$(".noofdays").html(body);
		   		$("input[name ='nodays']").val(NDAYS);
		   		$("input[name ='sdate']").val(sdate);
			}else{
			
				if(parseFloat(NDAYS) <= parseFloat(lvbal)){
					var body = "";
			   		body += '<div class="col-sm-4">';
			   		body += '</div>';
			   		body += '<div class="col-sm-4">';
			   		body += '<b><p>Number of Days '+NDAYS+'</p></b>';
			   		body += '</div>';
			   		$(".noofdays").html("");
			   		$(".noofdays").html(body);
			   		$("input[name ='nodays']").val(NDAYS);
			   		$("input[name ='sdate']").val(sdate);
				}else{
			   		body += '<div class="col-sm-4">';
			   		body += '</div>';
			   		body += '<div class="col-sm-4">';
			   		body += '<b><p>Number of Days 0.0</p></b>';
			   		body += '</div>';
			   		$(".noofdays").html("");
			   		$(".noofdays").html(body);
			   		$("input[name ='nodays']").val("0.0");
			   		$("input[name ='sdate']").val("");
				}
			}
	}
	
	
	function getnoofdaysdouble(){
		var muldays = $("select[name ='leaveduration']").val();
		var lvbal = $("input[name ='leavebal']").val();
		var lop = $("input[name ='lop']").val();
		$.ajax({
		       url: '/track/HrLeaveApplyServlet',
		       method: 'GET',
		       dataType: 'json',
		       data : {
					PLANT : document.form.plant.value,
					CMD: "GET_NO_OF_DAYS",
					EMPID: document.form.empid.value,
					STRDATE: document.form.sdate.value,
					ENDDATE: document.form.edate.value,
					SSTATUS: document.form.sdaystatus.value,
					ESTATUS: document.form.edaystatus.value
				},
		       success: function(data) {
		    	   
		    	   var bal =parseFloat(data.NDAYS) - parseFloat(lvbal);
		    	   if(bal == "0.5"){
		    		     $("#edaystatus").find('option').remove();
	    				 $("#edaystatus").append(new Option("First part of the day", "Morning"));
	    				 var body = "";
					   	    body += '<div class="col-sm-4">';
					   		body += '</div>';
					   		body += '<div class="col-sm-4">';
					   		body += '<b><p>Number of Days '+lvbal+'</p></b>';
					   		body += '</div>';
					   		$(".noofdays").html("");
					   		$(".noofdays").html(body);
					   		$("input[name ='nodays']").val(lvbal);
	    				 
		    	   }else{
		    		  
		    		   if(lop == "1"){
		    			   var body = "";
					   		body += '<div class="col-sm-4">';
					   		body += '</div>';
					   		body += '<div class="col-sm-4">';
					   		body += '<b><p>Number of Days '+data.NDAYS+'</p></b>';
					   		body += '</div>';
					   		$(".noofdays").html("");
					   		$(".noofdays").html(body);
					   		$("input[name ='nodays']").val(data.NDAYS);
		    		   }else{
		    			  
		    			   if(parseFloat(data.NDAYS) <= parseFloat(lvbal)){
								var body = "";
						   		body += '<div class="col-sm-4">';
						   		body += '</div>';
						   		body += '<div class="col-sm-4">';
						   		body += '<b><p>Number of Days '+data.NDAYS+'</p></b>';
						   		body += '</div>';
						   		$(".noofdays").html("");
						   		$(".noofdays").html(body);
						   		$("input[name ='nodays']").val(data.NDAYS);
							}else{
						   		/* body += '<div class="col-sm-4">';
						   		body += '</div>';
						   		body += '<div class="col-sm-4">';
						   		body += '<b><p>Number of Days 0.0</p></b>';
						   		body += '</div>';
						   		$(".noofdays").html("");
						   		$(".noofdays").html(body);
						   		$("input[name ='nodays']").val("0.0");
						   		$("input[name ='sdate']").val(""); */
								var body = "";
						   		body += '<div class="col-sm-4">';
						   		body += '</div>';
						   		body += '<div class="col-sm-4">';
						   		body += '<b><p>Number of Days 0.0</p></b>';
						   		body += '</div>';
						   		$(".noofdays").html("");
						   		$(".noofdays").html(body);
						   		$("input[name ='nodays']").val("0.0");
						   		$("input[name ='sdate']").val(""); 
						   		$("input[name ='edate']").val(""); 
							}
		    		   }
			    	   
		    	   }
		       }
				
		     });
		
	}
	
	function validateLeaveApply(){
		var EMP_NAME=$('input[name = "EMP_NAME"]').val();
		var leavetype=$('input[name = "leavetype"]').val();
		var leaveduration=$('select[name = "leaveduration"]').val();
		var sdate=$('input[name = "sdate"]').val();
		var edate=$('input[name = "edate"]').val();

		if(EMP_NAME == ""){
			alert("Please select employee.");
			document.form.EMP_NAME.focus();
			$(".content").css("display", "block");
	 		$("#emploader").hide();
			return false;
		}
			
		if(leavetype == ""){
			alert("Please select leave type.");
			document.form.leavetype.focus();
			$(".content").css("display", "block");
	 		$("#emploader").hide();
			return false;
		}
		
		if(leaveduration == "1"){
			if(sdate == ""){
				alert("Please select start date.");
				document.form.sdate.focus();
				$(".content").css("display", "block");
		 		$("#emploader").hide();
				return false;
			}
		}else{
			if(sdate == ""){
				alert("Please select start date.");
				document.form.sdate.focus();
				$(".content").css("display", "block");
		 		$("#emploader").hide();
				return false;
			}
			
			if(edate == ""){
				alert("Please select end date.");
				document.form.edate.focus();
				$(".content").css("display", "block");
		 		$("#emploader").hide();
				return false;
			}
		}
		
		return true;
	}

	
</script>


<jsp:include page="EmpFooter.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>