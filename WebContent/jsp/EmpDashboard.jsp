<%@page import="com.track.gates.Generator"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>
<%@include file="EmpSessionCheck.jsp"%>
<%
	String title = "Employee Home Page";
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
	HrClaimDAO hrClaimDAO = new HrClaimDAO();
	DateUtils dateutils = new DateUtils();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	
	String curtyear = dateutils.getYear();
	ArrayList arrCust = employeeDAO.getEmployeeListbyid(empid,plant);
	Map m=(Map)arrCust.get(0);
	
	String sEmpCode   = (String)m.get("EMPNO");
	String sEmpName   = (String)m.get("FNAME");
	String IMAGEPATH = (String)m.get("CATLOGPATH");
	
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
 	String zeroval = StrUtils.addZeroes(Float.parseFloat("0"), numberOfDecimal);

	
	List<EmployeeLeaveDETpojo> employeeleave = employeeLeaveDetDAO.EmployeeLeavedetlistpojo(plant, Integer.valueOf(empid), curtyear);
	String sdate = "01/01/"+curtyear;
	String edate = "31/12/"+curtyear;
	List<HolidayMstPojo> holidaylist =  hrHolidayMstDAO.getHolidaybydate(plant, sdate, edate);
	
	List<LeaveApplyHdrPojo> appliedleavelist = hrLeaveApplyHdrDAO.getHrLeaveApplyHdrPOJObyEmpid(plant, Integer.valueOf(empid));
	
	List<LeaveApplyHdrPojo> approvalleavelist = hrLeaveApplyHdrDAO.getHrLeaveApplyHdrPOJObyRepid(plant, Integer.valueOf(empid));
	
	List<HrClaimPojo> hrclaimlist = hrClaimDAO.getHrClaimPojoObyEmpid(plant, Integer.valueOf(empid));
	
	List<HrClaimPojo> hrclaimapprovelist = hrClaimDAO.getHrClaimPojobyRepid(plant, Integer.valueOf(empid));
			
	
%>
<style>
table.dataTable.nowrapapplied td {
    white-space: normal;
}

table.dataTable.nowrapapplied th {
    white-space: nowrap;
}

table.dataTable.nowrapapprove th, table.dataTable.nowrap td {
    white-space: nowrap;
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

 <!--  <div class="content-wrapper">
    Content Header (Page header)
    <section class="content-header">
		
    </section> -->
    <!-- Main content -->
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
        <!-- Left col -->
        <div class="col-md-8">
        <form class="form-horizontal" name="form" method="post" action="">
         <%if(!approvalleavelist.isEmpty()){ %>
          	
          		   <div class="box box-info">
			            <div class="box-header with-border">
			              <h3 class="box-title">Leave Approval List</h3>
			              <div class="box-tools pull-right">
			                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
			                </button>
			              </div>
			            </div>
			            <!-- /.box-header -->
			            <div class="box-body">
			           
			           <!--  <div class="form-group">
							<div class="col-sm-6">    
				 				<label class="checkbox-inline">      
			  						 <input type=Checkbox name ="select" value="select" onclick="checkAll(this.checked);">
					 				Select/Unselect All
								</label>						
							</div>				 	
		      			</div> -->
			              <div class="table-responsive">
			                <table class="table table-striped table-hover dt-responsive display nowrap" cellspacing="0" id="approveleave">
			                  <thead>
			                  <tr>
			                 	<th><input type=Checkbox name ="select" value="select" onclick="checkAll(this.checked);"></th>
			                    <th>Leave Type</th>
			                    <th>From Date</th>
			                    <th>To Date</th>
			                    <th>No of Days</th>
			                    <th>Status Date</th>
			                    <th>Status</th>
			                    <th>Attachments</th>
			                    <th>Reason</th>
			                    <th>Action</th>
			                  </tr>
			                  </thead>
			                  <tbody>
			                  <%for(LeaveApplyHdrPojo applyleave:approvalleavelist){ %>
				                  <tr>
				                  	<td>
				                  		<%if(applyleave.getSTATUS().equalsIgnoreCase("pending")){ %>
				                  			<input type="checkbox" name="appcheck" value="<%=applyleave.getID()%>">
				                  			
				                  		<%}else{%>
				                  			<input type="checkbox" name="appcheckdisable" disabled>
				                  		<%} %>
				                  	</td>
				                  	<td><a href="/track/jsp/AppliedLeaveDetail.jsp?HDRID=<%=applyleave.getID()%>"><%=applyleave.getLEAVETYPE()%></a></td>
				                  	<td><%=applyleave.getFROM_DATE()%></td>
				                  	<td><%=applyleave.getTO_DATE()%></td>
				                  	<td><%=applyleave.getNUMBEROFDAYS()%></td>
				                  	<td><%=applyleave.getSTATUSDATE()%></td>
				                  	<%if(applyleave.getSTATUS().equalsIgnoreCase("pending")){ %>
				                  		<td><p class="pbuttonblue"><%=applyleave.getSTATUS()%></p></td>
				                  	<%} else if(applyleave.getSTATUS().equalsIgnoreCase("approved")){ %>
				                  		<td><p class="pbuttongreen"><%=applyleave.getSTATUS()%></p></td>
				                  	<%} else if(applyleave.getSTATUS().equalsIgnoreCase("rejected")){ %>
				                  		<td><p class="pbuttonred"><%=applyleave.getSTATUS()%></p></td>
				                  	<%}else{ %>
				                  		<td><p class="pbuttonpurple"><%=applyleave.getSTATUS()%></p></td>
				                  	 <%} %>
				                  	<td>
					                  	<%List<HrLeaveApplyAttachment> hrleaveattachlist = hrLeaveApplyHdrDAO.getHrLeaveApplyAttachmentbyhdr(plant, applyleave.getID());
					                  	for(HrLeaveApplyAttachment cattach: hrleaveattachlist){
					                  	String finame = cattach.getFileName();
										int fiid = cattach.getID();%>
					                  		<li style="list-style-type:none"><i class="fa fa-download" aria-hidden="true" onclick="downloadleaveFile(<%=fiid%>,'<%=finame%>')"> <%=finame%></i></li>
					                  	<%}%>
									</td> 
				                  	<!-- <td><textarea name="reason" rows="2" cols="20" maxlength="500"></textarea></td> -->
				                  	<%if(applyleave.getSTATUS().equalsIgnoreCase("pending")){ %>
				                  		<td>
									      	<textarea name="reason" rows="2" cols="20" maxlength="500"></textarea>
									      	<input type="checkbox" name="reasoncheck" hidden>
									    </td>
				                  	<%} else if(applyleave.getSTATUS().equalsIgnoreCase("approved")){ %>
				                  		<td>
				                  			<textarea name="reason" rows="2" cols="20" maxlength="500"></textarea>
				                  			<input type="checkbox" name="reasoncheck" hidden>
				                  		</td>	
				                  	<%}else{%>
				                  		<td></td>	
				                  	<%} %>
				                  	
				                  	<%if(applyleave.getSTATUS().equalsIgnoreCase("pending")){ %>
				                  		<td>
									      	<div class=" pull-left">
													<button type="button" class="btn btn-success" onclick="singleapprove(this,'<%=applyleave.getID()%>')">
													Approve
													</button>
													<button type="button" class="btn btn-danger" style="width:75px;" onclick="singlereject(this,'<%=applyleave.getID()%>')">
													Reject
													</button>
											</div>
									    </td>
				                  	<%} else if(applyleave.getSTATUS().equalsIgnoreCase("approved")){ %>
				                  		<td>
				                  			<div class=" pull-left">
												<button type="button" class="btn btn-info" onclick="singlecancel(this,'<%=applyleave.getID()%>')">
												Cancel
												</button>
											</div>
				                  		</td>	
				                  	<%}else{%>
				                  		<td></td>	
				                  	<%} %>
				                  </tr>
			               	  <%} %>
			                  </tbody>
			                </table>
			              </div>
			              <!-- /.table-responsive -->
			            </div>
			            <!-- /.box-body -->
			          	<div class="box-footer clearfix">
			             <!--  <a href="javascript:void(0)" onclick="if(onProcessApprove(document.form)) {submitForm();}"  class="btn btn-success btn-sm btn-flat pull-left" style="margin:1%">Approve</a>
			              <a href="javascript:void(0)" onclick="if(onProcessReject(document.form)) {submitForm();}" class="btn btn-danger btn-sm btn-flat pull-left" style="margin:1%">Reject</a> -->
			           		<div class="col-sm-12">
								<button type="button" class="btn btn-success" onclick="if(onProcessApprove(document.form)) {submitForm();}" >
								Approve
								</button>
								<button type="button" class="btn btn-danger" onclick="if(onProcessReject(document.form)) {submitForm();}">
								Reject
								</button>
							</div>
			           
			            </div> 
			            <!-- /.box-footer -->
			          </div>
          	
          <%} %>
          </form>
           <form class="form-horizontal" name="form1" method="post" action="">
          <%if(!hrclaimapprovelist.isEmpty()){ %>
          	
          		   <div class="box box-info">
			            <div class="box-header with-border">
			              <h3 class="box-title">Claim Approval List</h3>
			              <div class="box-tools pull-right">
			                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
			                </button>
			              </div>
			            </div>
			            <!-- /.box-header -->
			            <div class="box-body">
			           
			           <!--  <div class="form-group">
							<div class="col-sm-6">    
				 				<label class="checkbox-inline">      
			  						 <input type=Checkbox name ="select" value="select" onclick="checkAll(this.checked);">
					 				Select/Unselect All
								</label>						
							</div>				 	
		      			</div> -->
			              <div class="table-responsive">
			                <table class="table table-striped table-hover dt-responsive display nowrap" cellspacing="0" id="claimapprove">
			                  <thead>
			                  <tr>
			                 	<th><input type=Checkbox name ="select" value="select" onclick="checkAllclaim(this.checked);"></th>
			                    <th>Date</th>
			                    <th>Claim</th>
			                    <th>Description</th>
			                    <th>From Place</th>
			                    <th>To Place</th>
			                    <th>Distance</th>
			                    <th>Amount</th>
			                    <th>Status Date</th>
			                    <th>Status</th>
			                    <th>Attachments</th>
			                    <th>Reason</th>
			                    <th>Action</th>
			                  </tr>
			                  </thead>
			                  <tbody>
			                  <%for(HrClaimPojo claim:hrclaimapprovelist){ %>
				                  <tr>
				                  	<td>
				                  		<%if(claim.getSTATUS().equalsIgnoreCase("pending")){ %>
				                  			<input type="checkbox" name="appclaimcheck" value="<%=claim.getID()%>">
				                  		<%}else{%>
				                  			<input type="checkbox" name="appclaimcheckdisable" disabled>
				                  		<%} %>
				                  	</td>
				                  	<td><%=claim.getCLAIMDATE()%></td>
				                  	<td><%=claim.getCLAIMNAME()%></td>
				                  	<td style="white-space: break-spaces;"><%=claim.getDESCRIPTION()%></td>
				                  	<td><%=claim.getFROM_PLACE()%></td>
				                  	<td><%=claim.getTO_PLACE()%></td>
				                  	<td><%=claim.getDISTANCE()%></td>
				                  	<td><%=StrUtils.addZeroes(claim.getAMOUNT(), numberOfDecimal)%></td>
				                  	<td><%=claim.getSTATUSDATE()%></td>
				                  	<%if(claim.getSTATUS().equalsIgnoreCase("pending")){ %>
				                  		<td><p class="pbuttonblue"><%=claim.getSTATUS()%></p></td>
				                  	<%} else if(claim.getSTATUS().equalsIgnoreCase("approved")){ %>
				                  		<td><p class="pbuttongreen"><%=claim.getSTATUS()%></p></td>
				                  	<%} else if(claim.getSTATUS().equalsIgnoreCase("rejected")){ %>
				                  		<td><p class="pbuttonred"><%=claim.getSTATUS()%></p></td>
				                  	<%}else{ %>
				                  		<td><p class="pbuttonpurple"><%=claim.getSTATUS()%></p></td>
				                  	 <%} %>
				                  	<td>
					                  	<%List<HrClaimAttachment> hrclaimattachlist = hrClaimDAO.getHrClaimAttachmentbyhdr(plant, claim.getCLKEY());
					                  	for(HrClaimAttachment cattach: hrclaimattachlist){
					                  	String finame = cattach.getFileName();
										int fiid = cattach.getID();%>
					                  		<li style="list-style-type:none"><i class="fa fa-download" aria-hidden="true" onclick="downloadFile(<%=fiid%>,'<%=finame%>')"> <%=finame%></i></li>
					                  	<%}%>
									</td> 
									
				                  	<%if(claim.getSTATUS().equalsIgnoreCase("pending")){ %>
				                  		<td>
									      	<textarea name="clreason" rows="2" cols="20" maxlength="500"></textarea>
									      	<input type="checkbox" name="clreasoncheck" hidden>
									    </td>
				                  	<%}else{%>
				                  		<td></td>	
				                  	<%} %>
				                  	<%if(claim.getSTATUS().equalsIgnoreCase("pending")){ %>
				                  		<td>
									      	<div class=" pull-left">
													<button type="button" class="btn btn-success" onclick="singleclaimapprove(this,'<%=claim.getID()%>')">
													Approve
													</button>
													<button type="button" class="btn btn-danger" style="width:75px;" onclick="singleclaimreject(this,'<%=claim.getID()%>')">
													Reject
													</button>
											</div>
									    </td>
				                  	<%}else{%>
				                  		<td></td>	
				                  	<%} %>
				                  </tr>
			               	  <%} %>
			                  </tbody>
			                </table>
			              </div>
			              <!-- /.table-responsive -->
			            </div>
			            <!-- /.box-body -->
			          	<div class="box-footer clearfix">
			             <!--  <a href="javascript:void(0)" onclick="if(onProcessApprove(document.form)) {submitForm();}"  class="btn btn-success btn-sm btn-flat pull-left" style="margin:1%">Approve</a>
			              <a href="javascript:void(0)" onclick="if(onProcessReject(document.form)) {submitForm();}" class="btn btn-danger btn-sm btn-flat pull-left" style="margin:1%">Reject</a> -->
			           		<div class="col-sm-12">
								<button type="button" class="btn btn-success" onclick="if(onProcessClaimApprove(document.form1)) {submitForm();}" >
								Approve
								</button>
								<button type="button" class="btn btn-danger" onclick="if(onProcessClaimReject(document.form1)) {submitForm();}">
								Reject
								</button>
							</div>
			           
			            </div> 
			            <!-- /.box-footer -->
			          </div>
          	
          <%} %>
		 </form>
          <!-- TABLE: LATEST ORDERS -->
          <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">Applied Leave History</h3>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <div class="table-responsive">
                <table class="table table-striped table-hover dt-responsive display nowrapapplied" cellspacing="0"  id="appliedleave">
                  <thead>
                  <tr>
                    <th>Leave Type</th>
                    <th>From Date</th>
                    <th>To Date</th>
                    <th>No of Days</th>
                    <th>Status Date</th>
                    <th>Status</th>
                    <th>Reason</th>
                  </tr>
                  </thead>
                  <tbody>
                  <%for(LeaveApplyHdrPojo applyleave:appliedleavelist){ %>
	                  <tr>
	                  	<td><%=applyleave.getLEAVETYPE()%></td>
	                  	<td><%=applyleave.getFROM_DATE()%></td>
	                  	<td><%=applyleave.getTO_DATE()%></td>
	                  	<td><%=applyleave.getNUMBEROFDAYS()%></td>
	                  	<td><%=applyleave.getSTATUSDATE()%></td>
	                  	<%if(applyleave.getSTATUS().equalsIgnoreCase("pending")){ %>
	                  		<td><p class="pbuttonblue"><%=applyleave.getSTATUS()%></p></td>
	                  	<%} else if(applyleave.getSTATUS().equalsIgnoreCase("approved")){ %>
	                  		<td><p class="pbuttongreen"><%=applyleave.getSTATUS()%></p></td>
	                  	<%} else if(applyleave.getSTATUS().equalsIgnoreCase("rejected")){ %>
	                  		<td><p class="pbuttonred"><%=applyleave.getSTATUS()%></p></td>
	                  	<%}else{ %>
	                  		<td><p class="pbuttonpurple"><%=applyleave.getSTATUS()%></p></td>
	                  	 <%} %>
	                  	 <td><%=applyleave.getREASON()%></td>
	                  </tr>
               	  <%} %>
                  </tbody>
                </table>
              </div>
              <!-- /.table-responsive -->
            </div>

          </div>
          <!-- /.box -->
          
          
                   <!-- TABLE: LATEST ORDERS -->
          <div class="box box-info">
            <div class="box-header with-border">
              <h3 class="box-title">Claim History</h3>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <div class="table-responsive">
                <table class="table table-striped table-hover dt-responsive display nowrapapplied" cellspacing="0"  id="appliedcliam">
                  <thead>
                  <tr>
                    <th>Date</th>
                    <th>Claim</th>
                    <th>Description</th>
                    <th>From Place</th>
                    <th>To Place</th>
                    <th>Distance</th>
                    <th>Amount</th>
                    <th>Reason</th>
                    <th>Status Date</th>
                    <th>Status</th>
                  </tr>
                  </thead>
                  <tbody>
                  <%for(HrClaimPojo claim:hrclaimlist){ %>
	                  <tr>
	                  	<td><%=claim.getCLAIMDATE()%></td>
	                  	<td><%=claim.getCLAIMNAME()%></td>
	                  	<td><%=claim.getDESCRIPTION()%></td>
	                  	<td><%=claim.getFROM_PLACE()%></td>
	                  	<td><%=claim.getTO_PLACE()%></td>
	                  	<td><%=claim.getDISTANCE()%></td>
	                  	<td><%=StrUtils.addZeroes(claim.getAMOUNT(), numberOfDecimal)%></td>
	                  	<td><%=claim.getREASON()%></td>
	                  	<td><%=claim.getSTATUSDATE()%></td>
	                  	<%if(claim.getSTATUS().equalsIgnoreCase("pending")){ %>
	                  		<td><p class="pbuttonblue"><%=claim.getSTATUS()%></p></td>
	                  	<%} else if(claim.getSTATUS().equalsIgnoreCase("approved")){ %>
	                  		<td><p class="pbuttongreen"><%=claim.getSTATUS()%></p></td>
	                  	<%} else if(claim.getSTATUS().equalsIgnoreCase("rejected")){ %>
	                  		<td><p class="pbuttonred"><%=claim.getSTATUS()%></p></td>
	                  	<%}else{ %>
	                  		<td><p class="pbuttonpurple"><%=claim.getSTATUS()%></p></td>
	                  	 <%} %>
	                  	
	                  </tr>
               	  <%} %>
                  </tbody>
                </table>
              </div>
              <!-- /.table-responsive -->
            </div>

          </div>
          <!-- /.box -->
          
        </div>
        <!-- /.col -->

        <div class="col-md-4">
          <div class="box box-default">
            <div class="box-header with-border">
              <h3 class="box-title">Leave Balance</h3>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
               			<%if(employeeleave.size() > 0){ %>
              	  		<div class="table-responsive">
                    		<table class="table no-margin" style="font-size: 90%;">
                      			<thead>
                  					<tr>
                    					<th>Leave Type</th>
					                    <th>Total Days</th>
					                    <th>Balance Days</th>
					                 </tr>
                  				</thead>
                  				<tbody>
                  				<%for(EmployeeLeaveDETpojo empleave:employeeleave){%>
				                   <tr>
				                   		<td><%=empleave.getLEAVETYPE() %></td>
				                    	<td><%=empleave.getTOTALENTITLEMENT() %></td>
				                    	<td><%=empleave.getLEAVEBALANCE() %></td>
				                  </tr>
				                <%} %>
								</tbody>
				             </table>
						</div>
						<%}else{ %>
						
						<%} %>
              <!-- /.row -->
            </div>

            <!-- /.footer -->
          </div>
          <!-- /.box -->

          <!-- PRODUCT LIST -->
          <div class="box box-primary">
            <div class="box-header with-border">
              <h3 class="box-title">Holiday List of <%=curtyear%></h3>

              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
                </button>
              </div>
            </div>
            <!-- /.box-header -->
            <div class="box-body">
				<div class="table-responsive">
                    <table id="holidaylist" class="table no-margin">
                    			<thead>
                  					<tr>
                    					<th>Holiday Date</th>
					                    <th></th>
					                    <th>Description</th>
					                 </tr>
                  				</thead>
                  				<tbody>
                  		<% for(HolidayMstPojo hlist:holidaylist){%>
				              <tr>
				                 <td><%=hlist.getHOLIDAY_DATE()%></td>
				                 <td>-</td>	
				                 <td><%=hlist.getHOLIDAY_DESC()%></td>				              
				              </tr>
				        <%} %>
					    </tbody>
				   </table>
			   </div>
           </div>
            
            
            <!-- /.box-footer -->
          </div>
          
         
          <!-- /.box -->
        </div>
        <!-- /.col -->
      </div>
      <!-- /.row -->
    </section>
    <!-- /.content -->
 <!--  </div> -->
  <!-- /.content-wrapper -->

<script type="text/javascript">


	$(document).ready(function(){
		setTimeout(function() {
		    $('.alert').fadeOut('fast');
		}, 4000);
		
		$("#dash_menu").addClass('empactive');
		
		$('#holidaylist').DataTable({
		      'paging'      : true,
		      'lengthChange': false,
		      'searching'   : false,
		      'ordering'    : false,
		      'info'        : false,
		      'autoWidth'   : false,
		      'bInfo'       : false,
		       pagingType: "simple"
		 });
		 
		 $('#appliedleave').DataTable({
		      'paging'      : true,
		      'lengthChange': true,
		      'searching'   : true,
		      'ordering'    : true,
		      'info'        : true,
		      'autoWidth'   : true,
		      'responsive'  : true
		 });

		 $('#approveleave').DataTable({
		      'paging'      : true,
		      'lengthChange': true,
		      'searching'   : true,
		      'ordering'    : true,
		      'info'        : true,
		      'autoWidth'   : true,
		      'responsive'  : true
		 });
		 
		 $('#claimapprove').DataTable({
		      'paging'      : true,
		      'lengthChange': true,
		      'searching'   : true,
		      'ordering'    : true,
		      'info'        : true,
		      'autoWidth'   : true,
		      'responsive'  : true
		 });
		 
		  $('#appliedcliam').DataTable({
		      'paging'      : true,
		      'lengthChange': true,
		      'searching'   : true,
		      'ordering'    : true,
		      'info'        : true,
		      'autoWidth'   : true,
		      'responsive'  : true
		 });
		 
		  
		
	 });


	 
	 
	 function checkAll(isChk) {
		 var len = document.form.appcheck.length;	
		 if(len == undefined) len = 1;  
	     if (document.form.appcheck)
	     {
	        for (var i = 0; i < len ; i++)
	        {      
	              	if(len == 1){
	              		document.form.appcheck.checked = isChk;
	               	}
	              	else{
	              		document.form.appcheck[i].checked = isChk;
	              	}
	        }
	    }
	}
	 
	 function checkAllclaim(isChk) {
		 var len = document.form1.appclaimcheck.length;	
		 if(len == undefined) len = 1;  
	     if (document.form1.appclaimcheck)
	     {
	        for (var i = 0; i < len ; i++)
	        {      
	              	if(len == 1){
	              		document.form1.appclaimcheck.checked = isChk;
	               	}
	              	else{
	              		document.form1.appclaimcheck[i].checked = isChk;
	              	}
	        }
	    }
	}
	 
	 function onProcessApprove(form){	
	 	$(".content").css("display", "none");
	 	$("#emploader").show();
		var checkFound = false; 
		
		$("input[name ='appcheck']").each(function() {
			if($(this).is(":checked")){
				checkFound = true;
		    }
		});
		
		
		if (checkFound != true) {
	 		$(".content").css("display", "block");
	 		$("#emploader").hide();
			alert ("Please check at least one checkbox.");
			return false;
		} 
		
		$("input[name ='appcheck']").each(function() {
			if($(this).is(":checked")){
				var reason = $(this).closest('tr').find("textarea[name ='reason']").val();
				$(this).closest('tr').find("input[name ='reasoncheck']").prop('checked', true);
				$(this).closest('tr').find("input[name ='reasoncheck']").val(reason);
		    }else{
		    	$(this).closest('tr').find("input[name ='reasoncheck']").prop('checked', false); // Checks it
				$(this).closest('tr').find("input[name ='reasoncheck']").val("")
		    }
		});
		
		document.form.action ="/track/HrLeaveApplyServlet?Submit=APPROVE_LEAVE_LIST";
		document.form.submit(); 		
	}
	 
	 function onProcessClaimApprove(form1){	
		 	$(".content").css("display", "none");
		 	$("#emploader").show();
			var checkFound = false; 
			
			$("input[name ='appclaimcheck']").each(function() {
				if($(this).is(":checked")){
					checkFound = true;
			    }
			});
			
			
			if (checkFound != true) {
		 		$(".content").css("display", "block");
		 		$("#emploader").hide();
				alert ("Please check at least one checkbox.");
				return false;
			} 
			
			$("input[name ='appclaimcheck']").each(function() {
				if($(this).is(":checked")){
					var reason = $(this).closest('tr').find("textarea[name ='clreason']").val();
					$(this).closest('tr').find("input[name ='clreasoncheck']").prop('checked', true);
					$(this).closest('tr').find("input[name ='clreasoncheck']").val(reason);
			    }else{
			    	$(this).closest('tr').find("input[name ='clreasoncheck']").prop('checked', false); // Checks it
					$(this).closest('tr').find("input[name ='clreasoncheck']").val("")
			    }
			});
			
			document.form1.action ="/track/HrClaimServlet?Submit=APPROVE_CLAIM_LIST";
			document.form1.submit(); 		
		}
	 
	 
	 	function onProcessReject(form){	
	 		$(".content").css("display", "none");
	 		$("#emploader").show();
	 		var checkFound = false; 
			$("input[name ='appcheck']").each(function() {
				if($(this).is(":checked")){
					checkFound = true;
			    }
			});
			if (checkFound != true) {
		 		$(".content").css("display", "block");
		 		$("#emploader").hide();
				alert ("Please check at least one checkbox.");
				return false;
			}
			var isItemValid = true;
			$("input[name ='appcheck']").each(function() {
				if($(this).is(":checked")){
					var reason = $(this).closest('tr').find("textarea[name ='reason']").val();
					if(reason == ""){
						$(this).closest('tr').find("textarea[name ='reason']").focus();
						isItemValid = false;
					}else{
						$(this).closest('tr').find("input[name ='reasoncheck']").prop('checked', true);
						$(this).closest('tr').find("input[name ='reasoncheck']").val(reason);
					}
			    }else{
			    	$(this).closest('tr').find("input[name ='reasoncheck']").prop('checked', false); // Checks it
					$(this).closest('tr').find("input[name ='reasoncheck']").val("")
			    }
			});
			
			if(!isItemValid){
				$(".content").css("display", "block");
				$("#emploader").hide();
				alert("Please enter reason for reject.");
				return false;
			}

			
			document.form.action ="/track/HrLeaveApplyServlet?Submit=REJECT_LEAVE_LIST";
			document.form.submit(); 
		}
	 	
	 	function onProcessClaimReject(form1){	
	 		$(".content").css("display", "none");
	 		$("#emploader").show();
	 		var checkFound = false; 
			$("input[name ='appclaimcheck']").each(function() {
				if($(this).is(":checked")){
					checkFound = true;
			    }
			});
			if (checkFound != true) {
		 		$(".content").css("display", "block");
		 		$("#emploader").hide();
				alert ("Please check at least one checkbox.");
				return false;
			}
			var isItemValid = true;
			$("input[name ='appclaimcheck']").each(function() {
				if($(this).is(":checked")){
					var reason = $(this).closest('tr').find("textarea[name ='clreason']").val();
					if(reason == ""){
						$(this).closest('tr').find("textarea[name ='clreason']").focus();
						isItemValid = false;
					}else{
						$(this).closest('tr').find("input[name ='clreasoncheck']").prop('checked', true);
						$(this).closest('tr').find("input[name ='clreasoncheck']").val(reason);
					}
			    }else{
			    	$(this).closest('tr').find("input[name ='clreasoncheck']").prop('checked', false); // Checks it
					$(this).closest('tr').find("input[name ='clreasoncheck']").val("")
			    }
			});
			
			if(!isItemValid){
				$(".content").css("display", "block");
				$("#emploader").hide();
				alert("Please enter reason for reject.");
				return false;
			}

			
			document.form1.action ="/track/HrClaimServlet?Submit=REJECT_CLAIM_LIST";
			document.form1.submit(); 
		}
	 	
	 	function singleapprove(obj,hdrid){
	 		$(".content").css("display", "none");
	 		$("#emploader").show();
	 		var reason = $(obj).closest('tr').find("textarea[name ='reason']").val();
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
	 	
	 	function singleclaimapprove(obj,hdrid){
	 		$(".content").css("display", "none");
	 		$("#emploader").show();
	 		$.ajax({
	 			type : "POST",
	 			url : "/track/HrClaimServlet",
	 			async : true,
	 			data : {
	 				Submit : "APPROVE_CLAIM",
	 				HDRID : hdrid,
	 			},
	 			dataType : "json",
	 			success : function(data) {
	 				 if(data.STATUS == "OK"){
	 					  document.form.action  = "EmpDashboard.jsp?rsuccess=Claim Approved successfully";
	 					  document.form.submit();
	 				}else{
	 					 document.form.action  = "EmpDashboard.jsp?resultnew=Claim Approve failed";
	 					  document.form.submit();
	 				}
	 			}
	 		});
	 	}
	 	
	 	function singlereject(obj,hdrid){
	 		$(".content").css("display", "none");
	 		$("#emploader").show();
	 		var reason = $(obj).closest('tr').find("textarea[name ='reason']").val();
	 		if(reason == ""){
				$(obj).closest('tr').find("textarea[name ='reason']").focus();
				$(".content").css("display", "block");
		 		$("#emploader").hide();
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
	 	
	 	function singleclaimreject(obj,hdrid){
	 		$(".content").css("display", "none");
	 		$("#emploader").show();
	 		var reason = $(obj).closest('tr').find("textarea[name ='clreason']").val();
	 		if(reason == ""){
				$(obj).closest('tr').find("textarea[name ='clreason']").focus();
				$(".content").css("display", "block");
		 		$("#emploader").hide();
				alert("Please enter reason for reject.")
			}else{
		 		$.ajax({
		 			type : "POST",
		 			url : "/track/HrClaimServlet",
		 			async : true,
		 			data : {
		 				Submit : "REJECT_CLAIM",
		 				HDRID : hdrid,
		 				REASON : reason
		 			},
		 			dataType : "json",
		 			success : function(data) {
		 				 if(data.STATUS == "OK"){
		 					  document.form.action  = "EmpDashboard.jsp?rsuccess=Claim Rejected successfully";
		 					  document.form.submit();
		 				}else{
		 					 document.form.action  = "EmpDashboard.jsp?resultnew=Claim Reject failed";
		 					  document.form.submit();
		 				}
		 			}
		 		});
			}
	 	}
	 	
	 	function singlecancel(obj,hdrid){
	 		$(".content").css("display", "none");
	 		$("#emploader").show();
	 		var reason = $(obj).closest('tr').find("textarea[name ='reason']").val();
	 		if(reason == ""){
				$(obj).closest('tr').find("textarea[name ='reason']").focus();
				$(".content").css("display", "block");
		 		$("#emploader").hide();
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
		 					  document.form.action  = "EmpDashboard.jsp?rsuccess=Leave Cancelled successfully";
		 					  document.form.submit();
		 				}else{
		 					 document.form.action  = "EmpDashboard.jsp?resultnew=Leave cancel failed";
		 					  document.form.submit();
		 				}
		 			}
		 		});
			}
	 	}
	 	
	 	function downloadFile(id,fileName)
	 	{
	 		 var urlStrAttach = "/track/HrClaimServlet?Submit=downloadAttachmentById&attachid="+id;
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
	 	
	 	
	 	function downloadleaveFile(id,fileName)
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
</script>
<jsp:include page="EmpFooter.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>