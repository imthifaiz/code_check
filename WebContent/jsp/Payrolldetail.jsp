<%@ page import="com.track.db.util.ItemUtil"%>
<%@page import="com.track.db.object.*"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Payroll Detail";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc=StrUtils.fString(request.getParameter("result"));
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	EmployeeDAO employeeDAO = new EmployeeDAO();
	HrPayrollHDRDAO hrPayrollHDRDAO = new HrPayrollHDRDAO();
	HrPayrollDETDAO hrPayrollDETDAO = new HrPayrollDETDAO();
	HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
	
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	
	String id =StrUtils.fString(request.getParameter("ID"));
	String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	
	HrPayrollHDR hrPayrollHDR = hrPayrollHDRDAO.getpayrollhdrById(plant, Integer.valueOf(id));
	List<HrPayrollDET> hrPayrollDETlist = hrPayrollDETDAO.getpayrolldetByhdrid(plant, Integer.valueOf(id));
	String empname = employeeDAO.getEmpnamebyid(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
	String empcode =employeeDAO.getEmpcode(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
	
	int payhdrid = hrPayrollPaymentDAO.getHrPayrollPaymendetbypayId(plant, Integer.valueOf(id));
	String pmode = "-";
	String pthrough = "-";
	
	if(payhdrid != 0){
		HrPayrollPaymentHdr pphdr = hrPayrollPaymentDAO.getHrPayrollPaymentHdrId(plant, payhdrid);
		pmode = pphdr.getPAYMENT_MODE();
		pthrough = pphdr.getPAID_THROUGH();
	}
		
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYROLL_SUMMARY%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/font-awesome.min.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<style>
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
.text-ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.ribbon {
    position: absolute!important;
    top: -5px;
    left: -5px;
    overflow: hidden;
    width: 96px;
    height: 94px;
}
.ribbon .ribbon-draft {
    background-color: #94a5a6;
    border-color: #788e8f;
}
.ribbon .ribbon-inner {
    text-align: center;
    color: #fff;
    top: 24px;
    left: -31px;
    width: 135px;
    padding: 3px;
    position: relative;
    transform: rotate(-45deg);
}
.ribbon .ribbon-inner:before {
    left: 0;
    border-left: 2px solid transparent;
}
.ribbon .ribbon-inner:after {
    right: -2px;
    border-bottom: 3px solid transparent;
}
.ribbon .ribbon-inner:after, .ribbon .ribbon-inner:before {
    content: "";
    border-top: 5px solid transparent;
    border-left: 5px solid;
    border-left-color: inherit;
    border-right: 5px solid transparent;
    border-bottom: 5px solid;
    border-bottom-color: inherit;
    position: absolute;
    top: 20px;
    transform: rotate(-45deg);
}
.invoice-banner {
    margin-top: 15px;
    margin-bottom: 10px;
    font-size: 13px;
    background-color: #fdfae4;
    border: 1px solid #ede5ae;
    padding: 10px;
    overflow: visible;
}

@media print {
  @page { margin: 0; }
  body { margin: 1.6cm; }
}
</style>
<div class="container-fluid m-t-20"><%if(resultnew.equals("") || resultnew == null){}else{ %>
	  <div class="alert alert-danger alert-dismissible" style="width: max-content;margin:0 auto;">
	    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
	    <%=resultnew %>
	  </div>
  <%} %>
  
  <%if(rsuccess.equals("") || resultnew == null){}else{ %>
	    <div class="alert alert-success alert-dismissible" style="width: max-content;margin:0 auto;">
    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
    <%=rsuccess %>
  </div>
  <%} %>
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../payroll/summary"><span class="underline-on-hover">Payroll Summary</span> </a></li>                    
                <li><label>Payroll Detail</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">
				<%-- <%if(CustcrdnoteHdr.get("CREDIT_STATUS").equals("Draft")){ %> --%>
					<%if(hrPayrollHDR.getSTATUS().equalsIgnoreCase("OPEN")){%>
					<button type="button" class="btn btn-default" style="margin-right: 15px;"
					 data-toggle="tooltip"  data-placement="bottom" title="Edit">
						<a href="../payroll/edit?id=<%=id%>"><i class="fa fa-pencil" aria-hidden="true"></i></a>
					</button>
					<%}%>
					<%if(hrPayrollHDR.getSTATUS().equalsIgnoreCase("PAID")){%>
					<button type="button" class="btn btn-default" style="margin-right: 15px;"
					 data-toggle="tooltip"  data-placement="bottom"  title="Payslip">
						<a href="/track/PayrollServlet?CMD=GET_PAYSLIP&HDRID=<%=id%>"><i class="fa fa-file-pdf-o" aria-hidden="true"></i></a>
					</button>
					<%}%>
					<%-- <button type="button" class="btn btn-default" style="margin-right: 15px;"
					 data-toggle="tooltip"  data-placement="bottom"  title="Print Payslip">
						<a href="/track/PayrollServlet?CMD=GET_PAYSLIP_OPEN&HDRID=<%=id%>" target="_blank"><i class="fa fa-print" aria-hidden="true"></i></a>
					</button>
					 --%>
					
					
					
					<!-- <button type="button" class="btn btn-default" onclick="generate()"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button> -->
				</div>


				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../payroll/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;" id="print_id1">
			<div style="height: 0.300000in;"></div>
			<span id="print_id">
				<div class="row">
					<div class="col-lg-2 form-group">
						<label>Payroll Number</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-3 form-group">
						<div class="input-group">
							<p><%=hrPayrollHDR.getPAYROLL()%></p>
						</div>
					</div>
					<div class="col-lg-2 form-group">
						<label>Employee Id</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-3 form-group">
						<p><%=empcode %></p>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-2 form-group">
						<label>From Date</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-3 form-group">
						<p><%=hrPayrollHDR.getFROMDATE()%></p>
					</div>
					<div class="col-lg-2 form-group">
						<label>Employee</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-3 form-group">
						<p><%=empname %></p>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-2 form-group">
						<label>To Date</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-3 form-group">
						<p><%=hrPayrollHDR.getTODATE()%></p> 
					</div>
					<div class="col-lg-2 form-group">
						<label>Payroll Date</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-3 form-group">
						<p><%=hrPayrollHDR.getPAYMENT_DATE()%></p>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 form-group">
						<label>Month</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-3 form-group">
						<p><%=months[Integer.valueOf(hrPayrollHDR.getMONTH())-1]%>-<%=hrPayrollHDR.getYEAR()%></p>
					</div>
					<div class="col-lg-2 form-group">
						<label>Attendance Days</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-3 form-group">
						<p><%=String.valueOf(hrPayrollHDR.getPAYDAYS())%></p>
					</div>
				</div>
				<%-- <div class="row">
					<div class="col-lg-2 form-group">
						<label>Year</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-3 form-group">
						<p><%=hrPayrollHDR.getYEAR()%></p>
					</div>
				</div> --%>
				<%if(hrPayrollHDR.getBANK_BRANCH() == null || hrPayrollHDR.getBANK_BRANCH().equalsIgnoreCase("")){%>
				
				<div class="row">
					<div class="col-lg-12" style="margin: 0px;width: 50%;"> <!-- style="margin: 0px;width: 40%;margin-left: 15px;" -->
					<h5 style="font-weight: 700;">Salary Breakdown Details</h5>
						<table class="table table-bordered table-striped line-item-table">
							<tbody>
								<%for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){ %>
								<%if(hrPayrollDET.getAMOUNT_TYPE().equalsIgnoreCase("addition")){ %>
									<tr>
										<td style="width: 300px;">
											<p><%=hrPayrollDET.getSALARYTYPE()%></p>
										</td>
										<td>
											<p style="margin-left: 10px;"><%=StrUtils.addZeroes(hrPayrollDET.getAMOUNT(), numberOfDecimal)%></p>
										</td>
									</tr>
									<%} %>
								<%} %>
							</tbody>
						</table>
						<%for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){ 
							int i=0; 
							if(hrPayrollDET.getAMOUNT_TYPE().equalsIgnoreCase("addition")){
							}else{
								if(i == 0){%>
									<h5 style="font-weight: 700;">Deduction</h5>
								<%} %>
							<% i = 1+1;} 
						
						}%>
						<table class="table table-bordered table-striped line-item-table">
							<tbody>
								<%for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){ %>
									<%if(hrPayrollDET.getAMOUNT_TYPE().equalsIgnoreCase("addition")){ %>
									<%}else{%>
										<tr>
											<td style="width: 300px;">
												<p><%=hrPayrollDET.getSALARYTYPE()%></p>
											</td>
											<td>
												<p style="margin-left: 5px;"> -<%=StrUtils.addZeroes(hrPayrollDET.getAMOUNT(), numberOfDecimal)%></p>
											</td>
										</tr>
									<%} %>
								<%} %>
							</tbody>
						</table>
						<table class="table table-bordered table-striped line-item-table">
							<tbody>
									<tr>
										<td style="background-color: darkgray;font-weight: bold;width: 300px;">
											<p>Net Amount</p>
										</td>
										<td style="background-color: darkgray;font-weight: bold;">
											<p style="margin-left: 10px;"><%=StrUtils.addZeroes(hrPayrollHDR.getTOTAL_AMOUNT(), numberOfDecimal)%></p>
										</td>
									</tr>
							</tbody>
						</table>
					</div>					
				</div>
				<%}else{%>
				
				<div class="row">
					<div class="col-lg-6"> <!-- style="margin: 0px;width: 40%;margin-left: 15px;" -->
					<h5 style="font-weight: 700;">Salary Breakdown Details</h5>
						<table class="table table-bordered table-striped line-item-table">
							<tbody>
								<%for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){ %>
								    <%if(hrPayrollDET.getAMOUNT_TYPE().equalsIgnoreCase("addition")){ %>
									<tr>
										<td style="width: 300px;">
											<p><%=hrPayrollDET.getSALARYTYPE()%></p>
										</td>
										<td>
											<p style="margin-left: 10px;"><%=StrUtils.addZeroes(hrPayrollDET.getAMOUNT(), numberOfDecimal)%></p>
										</td>
									</tr>
									<%} %>
								<%} %>
							</tbody>
						</table>
						<%for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){ 
							int i=0; 
							if(hrPayrollDET.getAMOUNT_TYPE().equalsIgnoreCase("addition")){
							}else{
								if(i == 0){%>
									<h5 style="font-weight: 700;">Deduction</h5>
								<%} %>
							<% i = 1+1;} 
						
						}%>
						<table class="table table-bordered table-striped line-item-table">
							<tbody>
								<%for(HrPayrollDET hrPayrollDET:hrPayrollDETlist){ %>
								<%if(hrPayrollDET.getAMOUNT_TYPE().equalsIgnoreCase("addition")){ %>
								<%}else{%>
									<tr>
										<td style="width: 300px;">
											<p><%=hrPayrollDET.getSALARYTYPE()%></p>
										</td>
										<td>
											<p style="margin-left: 5px;"> -<%=StrUtils.addZeroes(hrPayrollDET.getAMOUNT(), numberOfDecimal)%></p>
										</td>
									</tr>
								<%} %>
								<%} %>
							</tbody>
						</table>
						<table class="table table-bordered table-striped line-item-table">
							<tbody>
									<tr>
										<td style="background-color: darkgray;font-weight: bold;width: 300px;">
											<p>Net Amount</p>
										</td>
										<td style="background-color: darkgray;font-weight: bold;">
											<p style="margin-left: 10px;"><%=StrUtils.addZeroes(hrPayrollHDR.getTOTAL_AMOUNT(), numberOfDecimal)%></p>
										</td>
									</tr>
							</tbody>
						</table>
					</div>
					
					<div class="col-lg-6"> <!-- style="margin: 0px;width: 40%;margin-left: 15px;" -->
					<h5 style="font-weight: 700;">Cheque Details</h5>
						<table class="table table-bordered table-striped line-item-table">
							<tbody>
									<tr>
										<td>
											<p>Choose a Bank</p>
										</td>
										<td>
											<p><%=hrPayrollHDR.getBANK_BRANCH()%></p>
										</td>
									</tr>
									<tr>
										<td>
											<p>Cheque No</p>
										</td>
										<td>
											<p><%=hrPayrollHDR.getCHECQUE_NO()%></p>
										</td>
									</tr>
									<tr>
										<td>
											<p>Cheque Date</p>
										</td>
										<td>
											<p><%=hrPayrollHDR.getCHEQUE_DATE()%></p>
										</td>
									</tr>
									<tr>
										<td>
											<p>Cheque Amount</p>
										</td>
										<td>
											<p><%=hrPayrollHDR.getCHEQUE_AMOUNT()%></p>
										</td>
									</tr>
							</tbody>
						</table>
					</div>
				</div>
				<%} %>
				<div class="row">
					<div class="col-lg-2 form-group">
						<label>Payment Mode</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-4 form-group">
						<p><%=pmode%></p>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-2 form-group">
						<label>Paid Through</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-4 form-group">
						<p><%=pthrough%></p>
					</div>
				</div>
				 
				<%-- <%if(hrPayrollHDR.getBANK_BRANCH() == null || hrPayrollHDR.getBANK_BRANCH().equalsIgnoreCase("")){%>
				<%}else{%>	
				<div class="row hidecheque">
					<div class="row" style="margin: 0px;width: 95%;margin-left: 15px;">
						<table class="table table-bordered line-item-table payment-table">
							<thead>
								<tr>
									<th>Choose a Bank</th>
									<th>Cheque No</th>
									<th>Cheque Date</th>
									<th>Cheque Amount</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="text-center">
										<p><%=hrPayrollHDR.getBANK_BRANCH()%></p>
									</td>
									<td class="text-center">
										<p><%=hrPayrollHDR.getCHECQUE_NO()%></p>
									</td>
									<td class="text-center">
										<p><%=hrPayrollHDR.getCHEQUE_DATE()%></p> 
									</td>
									<td class="text-center">
										<p><%=hrPayrollHDR.getCHEQUE_AMOUNT()%></p> 
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<%} %> --%>

				<%-- <div class="form-group hidepdc" hidden>
					<div class="row">
						<div class="col-sm-6">
						</div>
						<div class="total-section col-sm-6">
							<div class="total-row sub-total">
								<div class="total-label" style="text-align: right;">
									Balance Cheque Amount:
								</div>
								<div style="padding-right: 8%;" class="total-amount" id="balamount"><%=zeroval%></div>
							</div>
						</div>
					</div>
				</div> --%>
				
				
				<div class="row">
					<div class="col-lg-2 form-group">
						<label>Reference</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-6 form-group">
						<p><%=hrPayrollHDR.getREFERENCE()%></p>
					</div>
				</div>
				
				<div class="row">
					<div class="col-lg-2 form-group">
						<label>Notes</label>
					</div>
					<div class="col-lg-1 form-group">
						<label>:</label>
					</div>
					<div class="col-lg-6 form-group">
						<p><%=hrPayrollHDR.getNOTE()%></p>
					</div>
				</div>
			
			</span>
		</div>



	</div>
	
</div>

<script>
	$(document).ready(function(){
		setTimeout(function() {
			$('.alert').fadeOut('fast');
		}, 2000);
		$('[data-toggle="tooltip"]').tooltip();  
	});
			
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>