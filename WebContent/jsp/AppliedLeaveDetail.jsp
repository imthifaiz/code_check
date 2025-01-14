<%@page import="com.track.gates.Generator"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>
<%@include file="EmpSessionCheck.jsp"%>
<%
	String title = "Applied Leave Details";
%>
<jsp:include page="EmpHeader.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
<%
	StrUtils StrUtils = new StrUtils();
	String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
	String empid = session.getAttribute("EMP_USER_ID").toString();
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
	EmployeeDAO employeeDAO = new EmployeeDAO();
	EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
	HrHolidayMstDAO hrHolidayMstDAO = new HrHolidayMstDAO();
	HrLeaveApplyHdrDAO hrLeaveApplyHdrDAO =new HrLeaveApplyHdrDAO();
	HrLeaveApplyDetDAO hrLeaveApplyDetDAO = new HrLeaveApplyDetDAO();
	HrLeaveTypeDAO hrLeaveTypeDAO = new HrLeaveTypeDAO();
	
	DateUtils dateutils = new DateUtils();

	String hdrid = StrUtils.fString(request.getParameter("HDRID"));
	HrLeaveApplyHdr hrLeaveApplyHdr = hrLeaveApplyHdrDAO.getHrLeaveApplyHdrById(plant, Integer.valueOf(hdrid));
	List<HrLeaveApplyDet> hrLeaveApplyDetlist = hrLeaveApplyDetDAO.getHrLeaveApplyDetbyhdrid(plant, Integer.valueOf(hdrid));
	
	ArrayList empmstlist = employeeDAO.getEmployeeListbyid(String.valueOf(hrLeaveApplyHdr.getEMPNOID()),plant);
	Map empmst=(Map)empmstlist.get(0);
	
	EmployeeLeaveDET emplvtdet = employeeLeaveDetDAO.getEmployeeLeavedetById(plant, hrLeaveApplyHdr.getLEAVETYPEID());
	HrLeaveType hrLeaveType = hrLeaveTypeDAO.getLeavetypeById(plant, emplvtdet.getLEAVETYPEID());
	
	List<HrLeaveApplyAttachment> HrLeaveApplyAttachlist = hrLeaveApplyHdrDAO.getHrLeaveApplyAttachmentbyhdr(plant, Integer.valueOf(hdrid));

%>
<style>

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
       <form class="form-horizontal" name="form" method="post" action="">
       </form>
             <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title"><%=title %></h3>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
            <div class="row">
	            <div class="col-md-6">
	            	<table class="table pull-left">
						<tbody>
							<tr>
								<td style="width: 35%;">Employee Code</td>
								<td>:</td>
								<td><%=empmst.get("EMPNO")%></td>
							</tr>
							<tr>
								<td style="width: 35%;">Employee Name</td>
								<td>:</td>
								<td><%=empmst.get("FNAME")%></td>
							</tr>
							<tr>
								<td style="width: 35%;">Leave Type</td>
								<td>:</td>
								<td><%=hrLeaveType.getLEAVETYPE()%></td>
							</tr>
							<tr>
								<td style="width: 35%;">Number of Days</td>
								<td>:</td>
								<td><%=hrLeaveApplyHdr.getNUMBEROFDAYS()%></td>
							</tr>
							<tr>
								<td style="width: 35%;">Status</td>
								<td>:</td>
								<td><%=hrLeaveApplyHdr.getSTATUS()%></td>
							</tr>
						</tbody>
					</table>
	            </div>
	        </div>
            <div class="row">
	            <div class="col-md-8">
	              <div class="table-responsive">
	                <table class="table table-bordered" style="text-align: center;">
	                  <thead>
	                  <tr>
	                    <th>Date</th>
	                    <th>Fullday/Halfday</th>
	                    <th>Morning/Afternoon</th>
	                  </tr>
	                  </thead>
	                  <tbody>
	                  <%for(HrLeaveApplyDet hrLeaveApplyDet:hrLeaveApplyDetlist){ %>
		                  <tr>
		                  	<td><%=hrLeaveApplyDet.getLEAVE_DATE()%></td>
		                  	<td><%=hrLeaveApplyDet.getPREPOSTLUNCHTYPE()%></td>
		                  	<%if(hrLeaveApplyDet.getPREPOSTLUNCHTYPE().equalsIgnoreCase("fullday")){ %>
		                  		<td>-</td>
		                  	<%}else{ %>
		                  		<td><%=hrLeaveApplyDet.getPREPOSTLUNCH()%></td>
		                  	 <%} %>
		                  </tr>
	               	  <%} %>
	                  </tbody>
	                </table>
	              </div>
	             </div>
	         </div>
	          <div class="row">
	            <div class="col-md-6">
	            <table class="table pull-left">
						<tbody>
							<tr>
								<td style="width: 35%;">Notes</td>
								<td>:</td>
								<%if(hrLeaveApplyHdr.getNOTES() == null || hrLeaveApplyHdr.getNOTES().equalsIgnoreCase("null")){%>
									<td>-</td>
								<%}else{ %>								
									<td><%=hrLeaveApplyHdr.getNOTES()%></td>
								<%} %>
							</tr>
							<%if(hrLeaveApplyHdr.getSTATUS().equalsIgnoreCase("pending")){ %>
								<tr>
									<td style="width: 35%;">Reason</td>
									<td>:</td>
									<td><textarea name="reason" style="box-sizing: border-box;max-width: 50%;" rows="3" cols="30" maxlength="500"></textarea></td>
								</tr>      
				            <%} else if(hrLeaveApplyHdr.getSTATUS().equalsIgnoreCase("approved")){ %>
					            <tr>
									<td style="width: 35%;">Reason</td>
									<td>:</td>
									<td><textarea name="reason" style="box-sizing: border-box;max-width: 50%;" rows="3" cols="30" maxlength="500"></textarea></td>
								</tr>
				  			<%} %>

							<tr>
								<td style="width: 35%;">Attachments</td>
								<td>:</td>
								<%if(HrLeaveApplyAttachlist.isEmpty()){%>
									<td>-</td>
								<%}else{ %>
								<td>						
									<%for(HrLeaveApplyAttachment hrLeaveApplyAttachment:HrLeaveApplyAttachlist){ %>
										<p><i class="fa fa-download" aria-hidden="true" onclick="downloadFile(<%=hrLeaveApplyAttachment.getID()%>,'<%=hrLeaveApplyAttachment.getFileName() %>')"> <%=hrLeaveApplyAttachment.getFileName() %></i></p>
									<%} %>
								</td>
								<%} %>
							</tr>

						</tbody>
					</table>
					<%if(hrLeaveApplyHdr.getSTATUS().equalsIgnoreCase("pending")){ %>
									      	<div class="col-sm-offset-4 col-sm-10">
													<button type="button" class="btn btn-success" onclick="singleapprove('<%=hrLeaveApplyHdr.getID()%>')">
													Approve
													</button>
													<button type="button" style="margin-left:5%"class="btn btn-danger" onclick="singlereject('<%=hrLeaveApplyHdr.getID()%>')">
													Reject
													</button>
											</div>
							
				                  	<%} else if(hrLeaveApplyHdr.getSTATUS().equalsIgnoreCase("approved")){ %>
				           
				                  			<div class=" col-sm-offset-2 col-sm-10">
												<button type="button" class="btn btn-info" onclick="singlecancel('<%=hrLeaveApplyHdr.getID()%>')">
												Cancel
												</button>
											</div>	
				  <%} %>
	            </div>
	         </div>
            </div>

			        				

          </div>
      </div>

      </div>
      <!-- /.row -->
    </section>
    <!-- /.content -->
  <!-- /.content-wrapper -->

<script type="text/javascript">
	$(document).ready(function(){
		setTimeout(function() {
		    $('.alert').fadeOut('fast');
		}, 4000);

	 });
	
	function downloadFile(id,fileName)
	{
		 var urlStrAttach = "/track/HrLeaveApplyServlet?Submit=downloadAttachmentById&attachid="+id;
		 var xhr=new XMLHttpRequest();
		 xhr.open("POST", urlStrAttach, true);
		 //Now set response type
		 xhr.responseType = 'arraybuffer';
		 xhr.addEventListener('load',function(){
		   if (xhr.status === 200){
		     console.log(xhr.response) // ArrayBuffer
		     console.log(new Blob([xhr.response])) // Blob
		     var datablob=new Blob([xhr.response]);
		     var a = document.createElement('a');
	         var url = window.URL.createObjectURL(datablob);
	         a.href = url;
	         a.download = fileName;
	         document.body.append(a);
	         a.click();
	         a.remove();
	         //window.URL.revokeObjectURL(url); 
		   }
		 })
		 xhr.send();
	}
	
	function singleapprove(hdrid){
		$(".content").css("display", "none");
 		$("#emploader").show();
 		var reason = $("textarea[name ='reason']").val();
 		$.ajax({
 			type : "POST",
 			url : "/track/HrLeaveApplyServlet",
 			async : true,
 			data : {
 				Submit : "APPROVE_LEAVE",
 				HDRID : hdrid,
 				REASON : reason
 			},
 			dataType : "json",
 			success : function(data) {
 				 if(data.STATUS == "OK"){
 					  document.form.action  = "EmpDashboard.jsp?rsuccess=Leave Approved successfully";
 					  document.form.submit();
 				}else{
 					 document.form.action  = "EmpDashboard.jsp?resultnew=Leave Approve failed";
 					  document.form.submit();
 				}
 			}
 		});
 	}
 	
 	function singlereject(hdrid){
 		$(".content").css("display", "none");
 		$("#emploader").show();
 		var reason = $("textarea[name ='reason']").val();
 		if(reason == ""){
			$(".content").css("display", "block");
	 		$("#emploader").hide();
			$("textarea[name ='reason']").focus();
			alert("Please enter reason for reject.")
		}else{
	 		$.ajax({
	 			type : "POST",
	 			url : "/track/HrLeaveApplyServlet",
	 			async : true,
	 			data : {
	 				Submit : "REJECT_LEAVE",
	 				HDRID : hdrid,
	 				REASON : reason
	 			},
	 			dataType : "json",
	 			success : function(data) {
	 				 if(data.STATUS == "OK"){
	 					  document.form.action  = "EmpDashboard.jsp?rsuccess=Leave Rejected successfully";
	 					  document.form.submit();
	 				}else{
	 					 document.form.action  = "EmpDashboard.jsp?resultnew=Leave Reject failed";
	 					  document.form.submit();
	 				}
	 			}
	 		});
		}
 	}
 	
 	function singlecancel(hdrid){
 		$(".content").css("display", "none");
 		$("#emploader").show();
 		var reason = $("textarea[name ='reason']").val();
 		if(reason == ""){
			$(".content").css("display", "block");
	 		$("#emploader").hide();
			$("textarea[name ='reason']").focus();
			alert("Please enter reason for cancel.")
		}else{
	 		$.ajax({
	 			type : "POST",
	 			url : "/track/HrLeaveApplyServlet",
	 			async : true,
	 			data : {
	 				Submit : "CANCEL_LEAVE",
	 				HDRID : hdrid,
	 				REASON : reason
	 			},
	 			dataType : "json",
	 			success : function(data) {
	 				 if(data.STATUS == "OK"){
	 					  document.form.action  = "EmpDashboard.jsp?rsuccess=Leave cancelled successfully";
	 					  document.form.submit();
	 				}else{
	 					 document.form.action  = "EmpDashboard.jsp?resultnew=Leave cancel failed";
	 					  document.form.submit();
	 				}
	 			}
	 		});
		}
 	}

</script>
<jsp:include page="EmpFooter.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>