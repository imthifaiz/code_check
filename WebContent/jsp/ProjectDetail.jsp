<%@page import="com.track.db.object.FinProject"%>
<%@ page import="com.track.db.util.ItemUtil"%>
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
	String title = "Project Details";
	String isAutoGenerate = "false";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	
	boolean displayCustomerpop=false,displayPaymentTypepop=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING"))
	{
		displayCustomerpop = ub.isCheckValAcc("popcustomer", plant,USERID);
		displayPaymentTypepop = ub.isCheckValAcc("paymenttypepopup", plant,USERID);
		displayCustomerpop =true;
		displayPaymentTypepop=true;	
	}
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displayCustomerpop = ub.isCheckValinv("popcustomer", plant,USERID);	
		displayPaymentTypepop = ub.isCheckValinv("paymenttypepopup", plant,USERID);
	}
	
	
	String numberOfDecimal = (String)request.getAttribute("NUMBEROFDECIMAL");
	String deldate = (String)request.getAttribute("DelDate");
	String collectionTime = (String)request.getAttribute("CollectionTime");
	
	String region = (String)request.getAttribute("Region");
	String msg = (String)request.getAttribute("Msg");
	
	FinProject finProject = (FinProject)request.getAttribute("FinProject");
	
	String CustName = (String)request.getAttribute("CustName");
	
	List attachmentList = (List)request.getAttribute("AttachmentList");
	
	//Validate no.of Customers -- Azees 15.11.2020
		CustMstDAO custdao = new CustMstDAO();
		String NOOFCUSTOMER=((String) session.getAttribute("NOOFCUSTOMER"));
		String ValidNumber="";
		ArrayList arrCustot =custdao.getTotalCustomers(plant);
		Map mCustot=(Map)arrCustot.get(0);
		String Custot = (String)mCustot.get("TOTAL_CUSTOMERS");
		int novalid = Integer.valueOf(Custot);
		if(!NOOFCUSTOMER.equalsIgnoreCase("Unlimited"))
		{
			int convl = Integer.valueOf(NOOFCUSTOMER);
			if(novalid>=convl)
			{
				ValidNumber=NOOFCUSTOMER;
			}
		}
		
		Short mandayhour1 = Short.valueOf(finProject.getISMANDAY_HOUR());	
		
		String dayhour = String.valueOf(finProject.getMANDAY_HOUR()); 
	    String[] array = dayhour.split("\\.");
	    double hour = Double.parseDouble(array[1]);
	    if (hour > 0){
	    	
	    }else{
	    	double Days = finProject.getMANDAY_HOUR();
	    	int DayHour = (int)Days;
	    	dayhour = String.valueOf(DayHour);
	    }
	
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PROJECT%>"/>
    <jsp:param name="submenu" value="<%=IConstants.PROJECT%>"/>
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
 </style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/editProject.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
             <ul class="breadcrumb backpageul">      	
                  <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>       
                  <li><a href="../project/summary?PRNO=<%=finProject.getPROJECT()%>"><span class="underline-on-hover">Project Summary</span></a></li> 
                  <li><label>Project Detail</label></li>                                   
             </ul>   
     <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../project/summary?PRNO=<%=finProject.getPROJECT()%>'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" id="salesOrderForm" name="form1" method="post" 
			action="../project/new" enctype="multipart/form-data" onsubmit="return validateSalesOrder()">
				<div class="form-group customer-section">
					<label class="control-label col-form-label col-sm-2 required">Customer:</label>
					<div class="col-sm-6 ac-box">
						<input type="text"  disabled="disabled" class="ac-selected  form-control typeahead" id="CUSTOMER" 
							 name="CUST_NAME" value="<%=CustName%>">
							 <span class="select-icon" style="right:45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Project Number:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="PRNO" name="PRNO" value="<%=finProject.getPROJECT()%>" readonly>
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Project Name:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="PR_NAME" name="PR_NAME" value="<%=finProject.getPROJECT_NAME() %>" readonly
							placeholder="Max 20 Characters" maxlength="20">
			   		 	</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Project Date:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text"  disabled="disabled" class="form-control datepicker" id="DELDATE" 
							
								name="DELDATE" value="<%=finProject.getPROJECT_DATE()%>" >
						</div>
					</div>
					
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Expiry Date:</label>
					<div class="col-sm-4">
					<div class="input-group">
							<input type="text"  disabled="disabled" class="form-control datepicker" id=EXPIRYDATE 
								name="EXPIRYDATE" value="<%=finProject.getEXPIRY_DATE()%>" >
						</div>
					</div>
					
				</div>
				
			
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Project Estimate Cost:</label><!-- this is dynamic value -->
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="PRESTCOST" name="PRESTCOST" onchange="changetaxforgst()"  value="<%=finProject.getESTIMATE_COST()%>" readonly>
							
			   		 	</div>
					</div>
				</div><div class="form-group">
					<label class="control-label col-form-label col-sm-2"></label>
			 		<div class="col-sm-4">
			  		<label class="radio-inline">
			      	<input type="radio" name="MANDAYHOUR" type = "radio"  value="0"  id="manday" <%if (mandayhour1 == 0) {%> checked <%}%> readonly>Man-day</label>
			    	<label class="radio-inline">
			      	<input type="radio" name="MANDAYHOUR" type = "radio" value="1"  id = "hour"  <%if (mandayhour1 == 1) {%> checked <%}%> readonly>Hour</label>
			     	</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Number of Man-day/Hour</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="DAYHOUR" name="DAYHOUR" onkeypress="return isNumberKey(event,this,4)" value="<%=dayhour%>" readonly>
			   		 	</div>
					</div>
				</div>
				<hr />
				
				<div class="row grey-bg">
					<div class="col-sm-4">
						<div class="form-inline">
							<label for="email">Attach Files(s)</label>
							<div class="attch-section">
								<input type="file"  disabled="disabled" class="form-control input-attch"
									id="supplierAttch" name="file" multiple="true">
								<div class="input-group">
									<svg version="1.1" id="Layer_1"
										xmlns="http://www.w3.org/2000/svg" x="0" y="0"
										viewBox="0 0 512 512" xml:space="preserve"
										class="icon icon-xs align-text-top action-icons input-group-addon"
										 disabled="disabled" style="height: 30px; display: inline-block; color: #c63616;">
										<path
											d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
									<button type="button" class="btn btn-sm btn-attch">Upload
										File</button>
								</div>

							</div>
						</div>
						<%if(attachmentList.size()>0){ %>
					<div id="billAttchNote">
						<small class="text-muted"><div class="attachclass"><a><%=attachmentList.size()%> files Attached</a>
								<div class="tooltiptext">
									
									<%for(int i =0; i<attachmentList.size(); i++) {   
								  		Map attach=(Map)attachmentList.get(i); %>
											<div class="row" style="padding-left:10px;padding-top:10px">
												<span class="text-danger col-sm-3">
													<%if(attach.get("FileType").toString().equalsIgnoreCase("application/pdf")) {%>
													<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M314.2 318.9c-6.4-3.7-13-7.7-18.2-12.5-13.9-13-25.5-31.1-32.7-50.8.5-1.9.9-3.5 1.3-5.2 0 0 7.8-44.5 5.8-59.6-.3-2.1-.5-2.6-1-4.3l-.7-1.8c-2.1-4.9-6.3-10.6-12.9-10.4l-3.8-.6h-.1c-7.3 0-13.3 4.2-14.8 9.9-4.8 17.5.2 43.9 9 77.9l-2.2 5.7c-6.3 15.5-14.4 31.2-21.4 44.9l-.9 1.9c-7.4 14.5-14.2 26.8-20.2 37.2l-6.2 3.3c-.5.2-11.2 6-13.8 7.4-21.4 12.8-35.6 27.3-38 38.9-.8 3.7-.2 8.4 3.6 10.5l6.1 3c2.6 1.4 5.4 2 8.3 2 15.2 0 33-19 57.4-61.5 28.2-9.2 60.3-16.8 88.4-21 21.4 12 47.8 20.4 64.5 20.4 2.9 0 5.5-.3 7.6-.9 3.2-.8 5.9-2.6 7.5-5.1 3.2-4.9 3.9-11.5 3-18.5-.3-2.1-1.9-4.6-3.6-6.2-4.9-4.9-15.9-7.4-32.5-7.6-11.6 0-25.2 1-39.5 3zM158 405c2.8-7.6 13.8-22.7 30.1-36 1.1-.8 3.5-3.2 5.9-5.4-17.1 27.1-28.5 38-36 41.4zm96.5-222.2c4.9 0 7.7 12.4 7.9 23.9.2 11.6-2.4 19.7-5.9 25.8-2.8-8.9-4.1-22.9-4.1-32.1 0 0-.2-17.6 2.1-17.6zm-28.8 158.3c3.4-6.2 6.9-12.6 10.6-19.4 8.9-16.7 14.5-29.9 18.7-40.6 8.3 15 18.6 27.8 30.8 38.2 1.5 1.3 3.1 2.5 4.8 3.8-24.9 4.8-46.2 10.8-64.9 18zm148.1-9.1c8.8 2.2 8.9 6.7 7.4 7.7s-5.8 1.5-8.6 1.5c-8.9 0-20-4.1-35.4-10.7 6-.5 11.4-.7 16.3-.7 8.9 0 11.5 0 20.3 2.2z"></path><path d="M441.6 116.6L329 4.7c-3-3-7.1-4.7-11.3-4.7H94.1C76.5 0 62.4 14.2 62.4 31.7v448.5c0 17.5 14.2 31.7 31.7 31.7h320.6c17.3 0 31.3-14 31.4-31.3l.3-352.7c-.1-4.1-1.8-8.2-4.8-11.3zm-14.9 358c0 9.4-7.8 17.1-17.3 17.1H99.2c-9.5 0-17.3-7.7-17.3-17.1V36.3c0-9.4 7.8-17.1 17.3-17.1h172.4c9.5 0 17.3 7.7 17.3 17.1v83.5c0 18.7 14.7 33.8 34.1 33.8h86.5c9.5 0 17.3 7.7 17.3 17.1l-.1 303.9zM326.8 136c-10.8 0-19.6-8.8-19.6-19.6V24.6c0-4.4 5.3-6.5 8.3-3.4l106.6 106.5c3.1 3.1.9 8.3-3.4 8.3h-91.9z"></path></svg>
													<%}else if(attach.get("FileType").toString().equalsIgnoreCase("image/jpeg") || attach.get("FileType").toString().equalsIgnoreCase("image/png") || attach.get("FileType").toString().equalsIgnoreCase("image/gif") || attach.get("FileType").toString().equalsIgnoreCase("image/tiff")){ %>
													<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M417.2 378.8H95.3c-7 0-12.8-5.7-12.8-12.8v-34.9c0-2.7.8-5.2 2.3-7.4l44.6-63c4-5.6 11.6-7 17.4-3.3l60.8 39.7c4.9 3.2 11.1 2.7 15.5-1.1l116.8-103.2c5.5-4.9 14.1-4.1 18.5 1.8l66.3 86c1.7 2.2 2.7 5 2.7 7.8v80.2c0 5.6-4.6 10.2-10.2 10.2z" fill="#40bab5"></path><path d="M212.2 157.7c23.2 0 42 19 42 42.4s-18.8 42.4-42 42.4-42-19-42-42.4c.1-23.4 18.9-42.4 42-42.4z" fill="#fbbe01"></path><path d="M462 60.8c16.5 0 30 13.5 30 30V422c0 16.5-13.5 30-30 30H50.4c-16.5 0-30-13.5-30-30V90.8c0-16.5 13.5-30 30-30H462m0-20H50.4c-27.6 0-50 22.4-50 50V422c0 27.6 22.4 50 50 50H462c27.6 0 50-22.4 50-50V90.8c0-27.6-22.4-50-50-50z" fill="#888"></path></svg>
													<%} else{%>
													<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M270.825,70.55L212.17,3.66C210.13,1.334,207.187,0,204.093,0H55.941C49.076,0,43.51,5.566,43.51,12.431V304.57  c0,6.866,5.566,12.431,12.431,12.431h205.118c6.866,0,12.432-5.566,12.432-12.432V77.633  C273.491,75.027,272.544,72.51,270.825,70.55z M55.941,305.073V12.432H199.94v63.601c0,3.431,2.78,6.216,6.216,6.216h54.903  l0.006,222.824H55.941z"></path></svg>	
													<%} %>
												</span>
												<div class="col-sm-9" style="padding-left:16px"><span class="fileNameFont"><a><%=attach.get("FileName").toString() %></a></span><br><span class="fileTypeFont">File Size: <%=Integer.parseInt(attach.get("FileSize").toString())/1024 %>KB</span></div>
											</div>
											<div class="row bottomline">
													<span class="col-sm-6" Style="font-size:14px;"><i class="fa fa-download" aria-hidden="true" onclick="downloadFile(<%=attach.get("ID") %>,'<%=(String) attach.get("FileName") %>')"> Download</i></span>
													<span class="col-sm-6" Style="font-size:14px;float:right"><i class="fa fa-trash" aria-hidden="true" onclick="removeFile(<%=attach.get("ID") %>)"> Remove</i></span>
											</div>	
									<%} %>
									
								</div>
							</div>
							
						</small>
					</div>
					<%}else{ %>
						<div id="billAttchNote">
							<small class="text-muted"> You can upload a maximum of 5
								files, 2MB each </small>
						</div>
					<%} %>
					</div>
					<div class="col-sm-6 notes-sec">
						<p>Notes <span class="text-muted">(For Internal Use)</span></p>
						<div>
							<textarea rows="2" name="Notes" class="ember-text-area form-control ember-view" 
								placeholder="Max 1000 characters" readonly maxlength="1000"><%=finProject.getNOTE()%></textarea>
						</div>
					</div>
				</div>
				<div class="row">
					<input type="hidden" value="<%=displayCustomerpop%>" name="displayCustomerpop" id="displayCustomerpop" />
					<input type="hidden" value="<%=displayPaymentTypepop%>" name="displayPaymentTypepop" id="displayPaymentTypepop" />
				</div>
				<input id="sub_total" name="sub_total" value="" hidden> 
				<input id="total_amount" name="total_amount" value="" hidden>
				<input type ="hidden" name="ISAUTOGENERATE" value="false">
				<input id="taxamount" name="taxamount" value="" hidden>
				
				<input type = "hidden" name="PERSON_INCHARGE" value="">
				<input type = "hidden" name="CUSTOMERTYPEDESC" value="">				
				<input type = "hidden" name="CUST_CODE" value="<%=finProject.getCUSTNO()%>">
				<input type = "hidden" name="CUST_CODE1" value="<%=finProject.getCUSTNO()%>">
				<input type = "hidden" name="TELNO" value="">
				<input type = "hidden" name="EMAIL" value="">
				<input type = "hidden" name="ADD1" value="">
				<input type = "hidden" name="ADD2" value="">
				<input type = "hidden" name="ADD3" value="">
				<input type = "hidden" name="REMARK2" value="">
				<input type = "hidden" name="ADD4" value="">
				<input type = "hidden" name="COUNTRY" value="">
				<input type = "hidden" name="ZIP" value="">
				<input type = "hidden" name="CUSTOMERSTATUSDESC" value="">
				<!-- <input type = "hidden" name="TAXTREATMENT" value=""> -->
				
				<input type = "hidden" name="SHIPPINGID" value="">
				<input type = "hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>">
				<INPUT type="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
			
				
				<input type = "hidden" name="orderstatus" value="">
				
				<input type = "hidden" name="custModal">
				<input type="hidden" name="STATE_PREFIX" value="" />
			</form>
		</div>
		
	</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>

