<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.db.object.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Payroll Payment Detail";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String resultnew=StrUtils.fString(request.getParameter("resultnew"));
	String rsuccess=StrUtils.fString(request.getParameter("rsuccess"));
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	HrPayrollPaymentDAO hrPayrollPaymentDAO = new HrPayrollPaymentDAO();
	EmployeeDAO employeeDAO = new EmployeeDAO();
	HrPayrollHDRDAO hrPayrollHDRDAO = new HrPayrollHDRDAO();
	
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	String id = StrUtils.fString(request.getParameter("TRANID"));
	
	List taxList =new ArrayList();	
	ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
	Map plntMap = (Map) plntList.get(0);
	String PLNTDESC = (String) plntMap.get("PLNTDESC");
	String ADD1 = (String) plntMap.get("ADD1");
	String ADD2 = (String) plntMap.get("ADD2");
	String ADD3 = (String) plntMap.get("ADD3");
	String ADD4 = (String) plntMap.get("ADD4");
	String STATE = (String) plntMap.get("STATE");
	String COUNTRY = (String) plntMap.get("COUNTY");
	String ZIP = (String) plntMap.get("ZIP");
	
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	File checkImageFile = new File(imagePath);
	if (!checkImageFile.exists()) {
		imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	}
	
	String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	HrPayrollPaymentHdr payhdr = hrPayrollPaymentDAO.getHrPayrollPaymentHdrId(plant, Integer.valueOf(id));
	
	double exchangerate = 0;
	List<PayrollPojo> PayrollPojoList = new ArrayList<PayrollPojo>();
	List<HrPayrollPaymentDet> paydet = hrPayrollPaymentDAO.getdetbyhdrid(plant, payhdr.getID());
    for (HrPayrollPaymentDet hrPayrollPaymentDet : paydet) {
    	HrPayrollHDR hrPayrollHDR = hrPayrollHDRDAO.getpayrollhdrById(plant, hrPayrollPaymentDet.getPAYID());
    	exchangerate = exchangerate + hrPayrollPaymentDet.getCURRENCYUSEQT();
    	String empname = employeeDAO.getEmpnamebyid(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
	    String empcode =employeeDAO.getEmpcode(plant, String.valueOf(hrPayrollHDR.getEMPID()), "");
		String monthyear = months[Integer.valueOf(hrPayrollHDR.getMONTH())-1]+"-"+hrPayrollHDR.getYEAR();
			
		PayrollPojo PayrollPojo = new PayrollPojo();
		PayrollPojo.setID(hrPayrollHDR.getID());
		PayrollPojo.setEMPCODE(empcode);
		PayrollPojo.setEMPNAME(empname);
		PayrollPojo.setMONTH(monthyear);
		PayrollPojo.setPAYROLL(hrPayrollHDR.getPAYROLL());
		PayrollPojo.setATDAYS(String.valueOf(hrPayrollHDR.getPAYDAYS()));
		PayrollPojo.setSALARY(StrUtils.addZeroes(hrPayrollHDR.getTOTAL_AMOUNT(), numberOfDecimal));
		PayrollPojo.setSTATUS(hrPayrollHDR.getSTATUS());
		PayrollPojoList.add(PayrollPojo);
	}

%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PAYROLL_PAYMENT%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/font-awesome.min.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<script src="../jsp/dist/js/jquery.toaster.js"></script>
<style>
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td, #table3>tbody>tr>td {
	border: none;
}
.paytable>table>tbody>tr>td{
	padding: 10px;
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

@media print {
  @page { margin: 0; }
  body { margin: 1.6cm; }
}
</style>
<div class="container-fluid m-t-20">
	  <%if(resultnew.equals("") || resultnew == null){}else{ %>
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
                <li><a href="../payroll/paymentsummary"><span class="underline-on-hover">Payroll Payment Summary</span> </a></li>                     
                <li><label>Payroll Payment Detail</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<div class="btn-group" role="group">

					<button type="button" class="btn btn-default" style="margin-right: 5px;"
					 data-toggle="tooltip"  data-placement="bottom" title="Edit" onclick="window.location.href='../payroll/editpayment?ID=<%=id%>'">
						<i class="fa fa-pencil" aria-hidden="true"></i>
					</button>
					
					<button type="button" class="btn btn-default" style="margin-right: 5px;" onclick="window.location.href='/track/HrPayrollPaymentServlet?CMD=GET_PAYROLL&HDRID=<%=id%>'"
					data-toggle="tooltip"  data-placement="bottom" title="PDF">
						<i class="fa fa-file-pdf-o" aria-hidden="true"></i>
					</button>
					<!-- <button type="button" class="btn btn-default printMe" 
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button> -->
					<button type="button" class="btn btn-default"  style="margin-right: 5px;" onclick="window.open('/track/HrPayrollPaymentServlet?CMD=GET_PAYROLL_PRINT&HDRID=<%=id%>','popup','width=600,height=600'); return false;"
					data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					
				<%-- 	<button type="button" class="btn btn-default"  style="margin-right: 5px;" onclick="sendemailpay('<%=id%>')"
					data-toggle="tooltip"  data-placement="bottom" title="Email">
						<i class="fa fa-envelope-o" aria-hidden="true"></i>
					</button> --%>
					
				</div>
				
				
				&nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../payroll/paymentsummary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;">		
			<div style="height: 0.200000in;"></div>
			<span id="print_id">
					<div class="row">
						<div class="col-sm-2">
							<img src="<%=request.getContextPath() %>/GetCustomerLogoServlet" style="width:100px;height:50px;">
						</div>
						<div class="col-sm-10" style="text-align:center">
							<div class="col-sm-12">
								<h5 style="font-weight: bold;margin-right: 140px;">PAYROLL PAYMENT</h5>
							</div>
							<div class="col-sm-12">
								<h5 style="font-weight: bold;margin-right: 140px;"><%=PLNTDESC%></h5>
							</div>
						</div>
					</div>
					<div class="paytable">
					
						<table style="margin-top: 1%;">
							<tr>
								<td>Payment Date</td>
								<td>:</td>
								<td><%=payhdr.getPAYMENT_DATE()%></td>
							</tr>
							<tr>
								<td>Currency</td>
								<td>:</td>
								<td><%=payhdr.getCURRENCYID()%></td>
							</tr>
							<tr>
								<td>Exchange Rate </td>
								<td>:</td>
								<td><%=StrUtils.addZeroes(exchangerate, numberOfDecimal)%></td>
							</tr>
							<%-- <tr>
								<td>Payment Made </td>
								<td>:</td>
								<td><%=payhdr.getpa%></td>
							</tr> --%>
							<tr>
								<td>Payment Mode</td>
								<td>:</td>
								<td><%=payhdr.getPAYMENT_MODE()%></td>
							</tr>
							<tr>
								<td>Paid Through</td>
								<td>:</td>
								<td><%=payhdr.getPAID_THROUGH()%></td>
							</tr>
							<tr>
								<td>Bank Charges</td>
								<td>:</td>
								<td><%=StrUtils.addZeroes(payhdr.getBANK_CHARGE(), numberOfDecimal)%></td>
							</tr>
						</table>
						<%-- <%if(!payhdr.getBANK_BRANCH().equalsIgnoreCase("")){%>
						<div class="container" style="width: 100%;margin-left: -10px;margin-top: 10px;">          
						  <table class="table table-bordered line-item-table">
						    <thead>
						      <tr>
						       		<th class="text-center">Choose a Bank</th>
									<th class="text-center">Cheque No</th>
									<th class="text-center">Cheque Date</th>
									<th class="text-center">Cheque Amount</th>
						      </tr>
						    </thead>
						    <tbody>
						      <tr>
						        <td class="text-center"><%=payhdr.getBANK_BRANCH()%></td>
						        <td class="text-center"><%=payhdr.getCHEQUE_NO()%></td>
						        <td class="text-center"><%=payhdr.getCHEQUE_DATE()%></td>
						        <td class="text-center"><%=StrUtils.addZeroes(payhdr.getCHEQUE_AMOUNT(), numberOfDecimal)%></td>
						      </tr>
						    </tbody>
						  </table>
						</div>
						<%}%> --%>
						<div class="container" style="width: 100%;margin-left: -10px;margin-top: 10px;">          
						  <table class="table">
						    <thead>
						      <tr>
						       		<th style="font-size: smaller;">PAYROLL</th>
									<th style="font-size: smaller;">EMPLOYEE ID</th>
									<th style="font-size: smaller;">EMPLOYEE</th>
									<th style="font-size: smaller;">MONTH</th>
									<th style="font-size: smaller;">ATTENDANCE</th>
									<th style="font-size: smaller;">SALARY</th>
						      </tr>
						    </thead>
						    <tbody>
						      <%for(PayrollPojo payrollPojo:PayrollPojoList){%>
						      <tr>
						        <td><%=payrollPojo.getPAYROLL()%></td>
						        <td><%=payrollPojo.getEMPCODE()%></td>
						        <td><%=payrollPojo.getEMPNAME()%></td>
						        <td><%=payrollPojo.getMONTH()%></td>
						        <td><%=payrollPojo.getATDAYS()%></td>
						        <td><%=StrUtils.addZeroes(Double.valueOf(payrollPojo.getSALARY()), numberOfDecimal)%></td>
						      </tr>
						      <%}%>
						      <tr> 
						      	<td style="font-weight: bold;">Total Amount</td>
						        <td></td>
						        <td></td>
						        <td></td>
						        <td></td>
						        <td><%=StrUtils.addZeroes(payhdr.getAMOUNTPAID(), numberOfDecimal)%></td>
						      </tr>
						    </tbody>
						  </table>
						</div>
						<table style="margin-top: 1%;">
							<tr>
								<td>Reference</td>
								<td>:</td>
								<td><%=payhdr.getREFERENCE()%></td>
							</tr>
							<tr>
								<td>Notes</td>
								<td>:</td>
								<td><%=payhdr.getNOTE()%></td>
							</tr>
							
						</table>
					</div>
					
					
			</span>
		</div>

			

		<script>
			$(document).ready(function(){
				setTimeout(function() {
				    $('.alert').fadeOut('fast');
				}, 4000);
			  $('[data-toggle="tooltip"]').tooltip();
			  $('.printMe').click(function(){
				  $(".hideprintpdf").css("display", "none");
				     $("#print_id").print();
				     $(".hideprintpdf").css("display", "block");
				  
				});
			 
			});
			
			 function sendemailpay(hid){

					$(".container-fluid").css("display", "none");
					$("#loader").show();
					$.ajax({
						type : "GET",
						url : "/track/HrClaimServlet?CMD=SEND_CLAIM_EMAIL",
						async : true,
						data : {
							HDRID:hid
						},
						dataType : "json",
						success : function(data) {
							//var objc = JSON.parse(data);
				        	if(data.STATUS == "OK"){
				        		$(".container-fluid").css("display", "block");
				    			$("#loader").hide();
				    			$.toaster({ priority: 'success', title: '', message: 'Email Sent Successfully!' });
				        	}else{
				        		$(".container-fluid").css("display", "block");
				    			$("#loader").hide();
				    			$.toaster({ priority: 'danger', title: '', message: 'Email Sent Failed!' });
				        	}
						}
							
					}); 
			 }
			
		</script>
	</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>