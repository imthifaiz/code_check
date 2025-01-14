<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%
String title = "Edit Contra";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String journalid = StrUtils.fString(request.getParameter("ID"));
int id=Integer.parseInt(journalid);
PlantMstDAO plantMstDAO = new PlantMstDAO();
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
String basecurrency = plantMstDAO.getBaseCurrency(plant);
DateUtils _dateUtils = new DateUtils();
String curDate =_dateUtils.getDate();
List AttachList=new ArrayList<>();
JournalDAO journalDAO=new JournalDAO();
Journal journal=journalDAO.getJournalById(plant, id);
JournalHeader journalHeader=journal.getJournalHeader();
List<JournalDetail> journalDetails=journal.getJournalDetails();
List<JournalAttachment> journalAttachments=journal.getJournalAttachment();
String fieldDesc = StrUtils.fString(request.getParameter("result"));

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.ACCOUNTING%>"/>
    <jsp:param name="mainmenu" value="<%=IConstants.CONTRA_ENTRY%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
  <script type="text/javascript" src="../jsp/js/calendar.js"></script>
   <!-- <script type="text/javascript" src="js/taxreturn.js"></script> -->
  <style>
  .text-dashed-underline:after {
    padding-bottom: 2px;
    border-bottom: 1px dashed #969696!important;
    width:10%;
}
  .requiredlabel {
    color: #b94a48;
}
.form-text {
    color: #999;
}
.d-block {
    display: block!important;
}
.alert-warning {
    background-color: #fff4e7 !important;
    border: 0;
    color: #222 !important;
}
  </style>
 
<%@ include file="header.jsp"%>
<h2>
		<center><small class="error-msg"><%=fieldDesc%></small></center>
	</h2>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>    
                <li><a href="../banking/contrasummary"><span class="underline-on-hover">Manual Journals Summary</span> </a></li>    
                <li><a href="../banking/contradetail?ID=<%=journalid%>"><span class="underline-on-hover">Manual Journals Detail</span> </a></li>                     
                <li><label>Edit Contra</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../banking/contradetail?ID=<%=journalid%>'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
		</div>

	<div class="container-fluid">
	<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
	<form id="journalForm" class="form-horizontal" name="form1"  action="/track/JournalServlet?action=update"  method="post" enctype="multipart/form-data">			
		<input type="text" name="username" value=<%=username%> hidden>
		<input type="text" name="plant" value="<%=plant%>" hidden>
		<input type="text" name="tran_type" value="CONTRA" hidden>
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Date</label>
			<div class="col-sm-4">
				<input type="text" class="form-control datepicker" value="<%=journalHeader.getJOURNAL_DATE()%>" id="journal_date" name="journal_date">
			</div>
		</div>
		
		<div class="form-group">
					
			<div class="col-sm-2">
			<label class="control-label col-form-label required">Contra#</label>
			</div>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="journalno" name="journalno" value="<%=journalHeader.getID() %>" readonly>
		    </div>  
		</div>
		<div class="form-group">	
			<div class="col-sm-2">
				<label class="control-label col-form-label">Reference#</label>
			</div>
			<div class="col-sm-4">
				<input type="text" class="form-control" id="reference" name="reference" value="<%=journalHeader.getREFERENCE()%>" autocomplete="off">
			</div>  
		</div>
		
		<div class="form-group">
			<div class="col-sm-2">
				<label class="control-label col-form-label">Notes</label>
			</div>
			<div class="col-sm-4">
				<textarea id="notes" name="notes" class="form-control" maxlength="1000" placeholder="Max 1000 characters"><%=journalHeader.getNOTE()%></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Currency</label>
			<div class="col-sm-4">				
				<input type="text" class="form-control" id="currency" name="currency" value="<%=journalHeader.getCURRENCYID()%>">
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'currency\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>				
			</div>
		</div>
		
		
		
		<div class="row" style="margin:0px;">
			<table class="table table-bordered line-item-table bill-table" style="width:97%;">
			<thead>
			  <tr>
				<th class="journal-acc" >Account</th>
				<th class="journal-desc">Description</th>								
				<th class="journal-debit">Debits</th>
				<th class="journal-credit">Credits</th>
			  </tr>
			</thead>
			<tbody>
			<%for(int i=0;i<journalDetails.size();i++){ 
				JournalDetail journDetail=journalDetails.get(i);
				if(i<2)
				{
					String dec = journDetail.getDESCRIPTION();
					if(dec.contains("-")){
						String[] decInfo = dec.split("-");
						dec = decInfo[0];
					}
			%>
			  <tr>
				<td class="journal-acc">
					<input type="text" name="journal_account_name" class="form-control journalaccountSearch" placeholder="Select Account" value="<%=journDetail.getACCOUNT_NAME()%>">
				</td>
				<td  class="col-sm-6 journal-desc">
					<textarea rows="2" name="desc" class="ember-text-area form-control ember-view" maxlength="300" placeholder="Description"><%=dec%></textarea>
				</td>
				<td class="journal-debit">
					<input name="debit" type="text" onchange="journaldebit(this)" onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" value="<%=String.format("%."+numberOfDecimal+"f", journDetail.getDEBITS())%>">
				</td>
				<td class="journal-credit">
					<input type="text" name="credit" onchange="journalcredit(this)" onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" value="<%=String.format("%."+numberOfDecimal+"f", journDetail.getCREDITS())%>">
				</td>				
				
			  </tr>
			  <%} else { %>
			  <tr>
				<td class="journal-acc">
					<input type="text" name="journal_account_name" class="form-control journalaccountSearch" placeholder="Select Account" value="<%=journDetail.getACCOUNT_NAME()%>">
				</td>
				<td  class="col-sm-6 journal-desc">
					<textarea rows="2" name="desc" class="ember-text-area form-control ember-view" maxlength="300" placeholder="Description"><%=journDetail.getDESCRIPTION() %></textarea>
				</td>
				<td class="journal-debit">
					<input name="debit" type="text" onchange="journaldebit(this)" onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" value="<%=String.format("%."+numberOfDecimal+"f", journDetail.getDEBITS())%>">
				</td>
				<td class="journal-credit" style="position:relative"><span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>
					<input type="text" name="credit" onchange="journalcredit(this)" onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" value="<%=String.format("%."+numberOfDecimal+"f", journDetail.getCREDITS())%>">
				</td>				
				
			  </tr>
			  <%} } %>
			</tbody>
			</table>
		</div>
		<div class="row">
			<div class="col-sm-7">
				<a style="text-decoration:none;cursor: pointer;" onclick="addRow()">+ Add another line</a>
			</div>
			<div class="total-section col-sm-5">
				<div class="row sub-total">
					<div class="col-sm-4 total-label"> Sub Total <br>  
					</div> 
					<div class="subtotal-debitamount col-sm-3" id="subtot-debitamt" hidden><%=String.format("%."+numberOfDecimal+"f", journalHeader.getSUB_TOTAL())%></div>
					<div class="subtotal-creditamount col-sm-3 col-sm-offset-1" id="subtot-creditamt" hidden><%=String.format("%."+numberOfDecimal+"f", journalHeader.getSUB_TOTAL())%></div>
					<div class="subtotal-debitamount col-sm-3" id="subtot-debitamt-show"><%=Numbers.toMillionFormat(journalHeader.getSUB_TOTAL().toString(), Integer.valueOf(numberOfDecimal))%></div>
					<div class="subtotal-creditamount col-sm-3 col-sm-offset-1" id="subtot-creditamt-show"><%=Numbers.toMillionFormat(journalHeader.getSUB_TOTAL().toString(), Integer.valueOf(numberOfDecimal))%></div>
				</div>
				<br>
				<div class="row gross-total">
					<div class="col-sm-4 total-label"> Total </div> 
					<div class="total-debitamount col-sm-3" id="total-debitamount" hidden><%=String.format("%."+numberOfDecimal+"f", journalHeader.getTOTAL_AMOUNT())%> </div>
					<div class="total-creditamount col-sm-3 col-sm-offset-1" id="total-creditamount" hidden><%=String.format("%."+numberOfDecimal+"f", journalHeader.getTOTAL_AMOUNT())%></div>
					<div class="total-debitamount col-sm-3" id="total-debitamount-show"><%=Numbers.toMillionFormat(journalHeader.getTOTAL_AMOUNT().toString(), Integer.valueOf(numberOfDecimal))%> </div>
					<div class="total-creditamount col-sm-3 col-sm-offset-1" id="total-creditamount-show"><%=Numbers.toMillionFormat(journalHeader.getTOTAL_AMOUNT().toString(), Integer.valueOf(numberOfDecimal))%></div>
				</div>
				
			</div>
		</div>
			
			<div class="row grey-bg">
			<div class="col-sm-4">
				<div class="form-inline">
					<label for="email">Attach Files(s)</label>
					<div class="attch-section">
						<input type="file" class="form-control input-attch" id="billAttch" name="file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
				<%if(journalAttachments.size()>0){ %>
						<div id="billAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=journalAttachments.size()%> files Attached</a>
									<div class="tooltiptext">
										
										<%for(int i =0; i<journalAttachments.size(); i++) {   
									  		JournalAttachment attach=journalAttachments.get(i); %>
												<div class="row" style="padding-left:10px;padding-top:10px">
													<span class="text-danger col-sm-3">
														<%if(attach.getFileType().toString().equalsIgnoreCase("application/pdf")) {%>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M314.2 318.9c-6.4-3.7-13-7.7-18.2-12.5-13.9-13-25.5-31.1-32.7-50.8.5-1.9.9-3.5 1.3-5.2 0 0 7.8-44.5 5.8-59.6-.3-2.1-.5-2.6-1-4.3l-.7-1.8c-2.1-4.9-6.3-10.6-12.9-10.4l-3.8-.6h-.1c-7.3 0-13.3 4.2-14.8 9.9-4.8 17.5.2 43.9 9 77.9l-2.2 5.7c-6.3 15.5-14.4 31.2-21.4 44.9l-.9 1.9c-7.4 14.5-14.2 26.8-20.2 37.2l-6.2 3.3c-.5.2-11.2 6-13.8 7.4-21.4 12.8-35.6 27.3-38 38.9-.8 3.7-.2 8.4 3.6 10.5l6.1 3c2.6 1.4 5.4 2 8.3 2 15.2 0 33-19 57.4-61.5 28.2-9.2 60.3-16.8 88.4-21 21.4 12 47.8 20.4 64.5 20.4 2.9 0 5.5-.3 7.6-.9 3.2-.8 5.9-2.6 7.5-5.1 3.2-4.9 3.9-11.5 3-18.5-.3-2.1-1.9-4.6-3.6-6.2-4.9-4.9-15.9-7.4-32.5-7.6-11.6 0-25.2 1-39.5 3zM158 405c2.8-7.6 13.8-22.7 30.1-36 1.1-.8 3.5-3.2 5.9-5.4-17.1 27.1-28.5 38-36 41.4zm96.5-222.2c4.9 0 7.7 12.4 7.9 23.9.2 11.6-2.4 19.7-5.9 25.8-2.8-8.9-4.1-22.9-4.1-32.1 0 0-.2-17.6 2.1-17.6zm-28.8 158.3c3.4-6.2 6.9-12.6 10.6-19.4 8.9-16.7 14.5-29.9 18.7-40.6 8.3 15 18.6 27.8 30.8 38.2 1.5 1.3 3.1 2.5 4.8 3.8-24.9 4.8-46.2 10.8-64.9 18zm148.1-9.1c8.8 2.2 8.9 6.7 7.4 7.7s-5.8 1.5-8.6 1.5c-8.9 0-20-4.1-35.4-10.7 6-.5 11.4-.7 16.3-.7 8.9 0 11.5 0 20.3 2.2z"></path><path d="M441.6 116.6L329 4.7c-3-3-7.1-4.7-11.3-4.7H94.1C76.5 0 62.4 14.2 62.4 31.7v448.5c0 17.5 14.2 31.7 31.7 31.7h320.6c17.3 0 31.3-14 31.4-31.3l.3-352.7c-.1-4.1-1.8-8.2-4.8-11.3zm-14.9 358c0 9.4-7.8 17.1-17.3 17.1H99.2c-9.5 0-17.3-7.7-17.3-17.1V36.3c0-9.4 7.8-17.1 17.3-17.1h172.4c9.5 0 17.3 7.7 17.3 17.1v83.5c0 18.7 14.7 33.8 34.1 33.8h86.5c9.5 0 17.3 7.7 17.3 17.1l-.1 303.9zM326.8 136c-10.8 0-19.6-8.8-19.6-19.6V24.6c0-4.4 5.3-6.5 8.3-3.4l106.6 106.5c3.1 3.1.9 8.3-3.4 8.3h-91.9z"></path></svg>
														<%}else if(attach.getFileType().toString().equalsIgnoreCase("image/jpeg") || attach.getFileType().toString().equalsIgnoreCase("image/png") || attach.getFileType().toString().equalsIgnoreCase("image/gif") || attach.getFileType().toString().equalsIgnoreCase("image/tiff")){ %>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M417.2 378.8H95.3c-7 0-12.8-5.7-12.8-12.8v-34.9c0-2.7.8-5.2 2.3-7.4l44.6-63c4-5.6 11.6-7 17.4-3.3l60.8 39.7c4.9 3.2 11.1 2.7 15.5-1.1l116.8-103.2c5.5-4.9 14.1-4.1 18.5 1.8l66.3 86c1.7 2.2 2.7 5 2.7 7.8v80.2c0 5.6-4.6 10.2-10.2 10.2z" fill="#40bab5"></path><path d="M212.2 157.7c23.2 0 42 19 42 42.4s-18.8 42.4-42 42.4-42-19-42-42.4c.1-23.4 18.9-42.4 42-42.4z" fill="#fbbe01"></path><path d="M462 60.8c16.5 0 30 13.5 30 30V422c0 16.5-13.5 30-30 30H50.4c-16.5 0-30-13.5-30-30V90.8c0-16.5 13.5-30 30-30H462m0-20H50.4c-27.6 0-50 22.4-50 50V422c0 27.6 22.4 50 50 50H462c27.6 0 50-22.4 50-50V90.8c0-27.6-22.4-50-50-50z" fill="#888"></path></svg>
														<%} else{%>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M270.825,70.55L212.17,3.66C210.13,1.334,207.187,0,204.093,0H55.941C49.076,0,43.51,5.566,43.51,12.431V304.57  c0,6.866,5.566,12.431,12.431,12.431h205.118c6.866,0,12.432-5.566,12.432-12.432V77.633  C273.491,75.027,272.544,72.51,270.825,70.55z M55.941,305.073V12.432H199.94v63.601c0,3.431,2.78,6.216,6.216,6.216h54.903  l0.006,222.824H55.941z"></path></svg>	
														<%} %>
													</span>
													<div class="col-sm-9" style="padding-left:16px"><span class="fileNameFont"><a><%=attach.getFileName().toString() %></a></span><br><span class="fileTypeFont">File Size: <%=attach.getFileSize()/1024 %>KB</span></div>
												</div>
												<div class="row bottomline">
														<span class="col-sm-6" Style="font-size:14px;"><i class="fa fa-download" aria-hidden="true" onclick="downloadFile(<%=attach.getID() %>,'<%=(String) attach.getFileName() %>')"> Download</i></span>
														<span class="col-sm-6" Style="font-size:14px;float:right"><i class="fa fa-trash" aria-hidden="true" onclick="removeFile(<%=attach.getID() %>)"> Remove</i></span>
												</div>	
										<%} %>
										
									</div>
								</div>
								
							</small>
						</div>
						<%}else{ %>
						<div id="billAttchNote">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
						<%} %>
			</div>
			<!-- <div class="col-sm-6 notes-sec">
				<p>Notes <span class="text-muted">(For Internal Use)</span></p>
				<div> <textarea rows="2" name="note" class="ember-text-area form-control ember-view"></textarea> </div>
			</div> -->
		</div>
	
		<input id="sub_total" name="sub_total" value="" hidden>
		<input id="total_amount" name="total_amount" value="" hidden>
		<input name="Submit" value="Save" hidden>
		<input name="bill_status" value="Save" hidden>
		<div class="row">
			<div class="col-sm-12 txn-buttons">
				<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
			</div>
		</div>
	</form>
</div>
	<!-- Modal -->
	<%-- <%@include file="newSupplierModal.jsp" %> --%>
	<%-- <%@include file="NewChartOfAccountAdd.jsp"%> --%>
	<%@include file="NewChartOfAccountpopup.jsp"%>
	<!-- Modal -->
	<!-- <div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog">	
	    Modal content
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Modal Header</h4>
	      </div>
	      <div class="modal-body">
	        <p>Some text in the modal.</p>
	      </div>
	      <div class="modal-footer">
	      		<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	  </div>
	</div> -->
	</div>
</div>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script>
$(document).ready(function(){
	var plant = document.form1.plant.value;
$(".journalaccountSearch").typeahead({
	input:".journalaccountSearch",
  hint: true,
  minLength:0,  
  searchOnFocus: true,
  classNames: {
	 	menu: 'bigdrop'
	  }
},
{	  
  display: 'accountname',  
  source: function (query, process,asyncProcess) {
		var urlStr = "/track/ChartOfAccountServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			action : "getSubAccountTypeGroup",
			module:"contraaccount",
			ITEM : query
		},
		dataType : "json",
		success : function(data) {
			return asyncProcess(data.results);
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
	suggestion: function(item) {
		if (item.issub) {
			var $state = $(
				    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
				  );
			}
		else
			{
			var $state = $(
					 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
				  );
			}
		  
		  return $state;
	}
  }
}).on('typeahead:render',function(event,selection){
	  
}).on('typeahead:open',function(event,selection){

}).on('typeahead:close',function(){

});
$('#currency').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{
	  display: 'CURRENCYID',  
	  async: true,   
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/ExpensesServlet";
		$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			PLANT : plant,
			ACTION : "GET_CURRENCY",
			QUERY : query
		},
		dataType : "json",
		success : function(data) {
			return asyncProcess(data.currency);
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
	    return '<p>' + data.CURRENCYID + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = $(".tt-menu").height()+35;
		top+="px";
		$('.supplierAddBtn').remove();  
		/*$('.vendor-section .tt-menu').after( '<div class="supplierAddBtn footer"  data-toggle="modal" data-target="#supplierModal"><a href="#"> + New Supplier</a></div>');*/
		$(".supplierAddBtn").width($(".tt-menu").width());
		$(".supplierAddBtn").css({ "top": top,"padding":"3px 20px" });			
		if($(this).parent().find(".tt-menu").css('display') != "block")
			menuElement.next().hide();		  
	}).on('typeahead:open',function(event,selection){
		$('.supplierAddBtn').show();
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);    
	}).on('typeahead:close',function(){
		setTimeout(function(){ $('.supplierAddBtn').hide();}, 180);	
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
	});
$(".bill-table tbody").on('click','.bill-action',function(){
	debugger;	    
    $(this).parent().parent().remove();
    calculateCreditTotal();
	calculateDebitTotal();
});
$(".bill-table tbody").on('click','.accrmv',function(){
	debugger;	    
    var obj = $(this);
    var timestamp = new Date().getUTCMilliseconds();
    kayid = "key"+timestamp;
    $(obj).closest('td').attr('id', kayid); 
    $("input[name ='ukey']").val(kayid);
});
function addJournal()
{
	$.ajax({
		type : 'POST',
		url : "/track/JournalServlet?action=create",
		dataType : 'json',
		success : function(data) {
			
		},
		error : function(data) {

			alert(data.responseText);
		}
	});
	}
	
function getJournal()
{
	$.ajax({
		type : 'GET',
		url : "/track/JournalServlet?action=getAll",
		dataType : 'json',
		success : function(data) {
			
		},
		error : function(data) {

			alert(data.responseText);
		}
	});
	}
});
function create_accountcoa() {
	
	if ($('#create_account_modalcoa #acc_type').val() == "") {
		alert("please fill account type");
		$('#create_account_modalcoa #acc_type').focus();
		return false;
	}
	
	if ($('#create_account_modalcoa #acc_det_type').val() == "") {
		alert("please fill account detail type");
		$('#create_account_modalcoa #acc_det_type').focus();
		return false;
	}
	
	if ($('#create_account_modalcoa #acc_name').val() == "") {
		alert("please fill account name");
		$('#create_account_modalcoa #acc_name').focus();
		return false;
	}
	
	if(document.create_formcoa.acc_is_sub.checked)
	{
		if ($('#create_account_modalcoa #acc_subAcct').val() == "") {
			alert("please fill sub account");
			$('#create_account_modalcoa #acc_subAcct').focus();
			return false;
		}
		else
			{
			 var parType=$('#create_account_modalcoa #acc_det_type').val();
			 subType=subType.trim();
			 var n = parType.localeCompare(subType);
			    if(n!=0)
			    	{
			    	$("#create_account_modalcoa .alert").show();
			    	$('#create_account_modalcoa .alert').html("For subaccounts, you must select the same account and extended type as their parent.");
			    	/* setTimeout(function() {
			            $(".alert").alert('close');
			        }, 5000); */
			    	 return false;
			    	}
			}
	}
	/* if ($('#create_account_modalcoa #acc_balance').val() != "") {
		if ($('#create_account_modalcoa #acc_balance').val() != "0") {
		if ($('#create_account_modalcoa #acc_balanceDate').val() == "") {
		alert("please fill date");
		$('#create_account_modalcoa #acc_balanceDate').focus();
		return false;
		}
		}
	} */
	
	var formData = $('form[name="create_formcoa"]').serialize();
	$.ajax({
		type : 'post',
		url : "/track/ChartOfAccountServlet?action=create",
		dataType : 'json',
		data : formData,//{key:val}
		success : function(data) {
			if (data.STATUS == "FAIL") {		                               
				alert(data.MESSAGE);
			}else{
				var ukey = document.create_formcoa.ukey.value;
				ukey = "#"+ukey;
				$(ukey).parents("tr").find('input[name="journal_account_name"]').val(data.ACCOUNT_NAME); 
				$('#create_account_modalcoa').modal('toggle');
			}
		},
		error : function(data) {

			alert(data.responseText);
		}
	});
	return false;
}
function addRow(){
	var body="";
	body += '<tr>';
	body += '<td class="journal-acc">';
	body += '<input type="text" name="journal_account_name" class="form-control journalaccountSearch" placeholder="Select Account">';
	body += '</td>';
	body += '<td  class="col-sm-6 journal-desc">';
	body += '<textarea rows="2" name="desc" class="ember-text-area form-control ember-view" maxlength="300" placeholder="Description"></textarea>';
	body += '</td>';
	body += '<td class="journal-debit">';	
	body += '<input name="debit" type="text" onchange="journaldebit(this)" onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
	body += '</td>';
	body += '<td class="journal-credit" style="position:relative"><span class="glyphicon glyphicon-remove-circle bill-action" aria-hidden="true"></span>';
	body += '<input type="text" name="credit" onchange="journalcredit(this)" onkeypress="return isNumberKey(event,this,4)" class="form-control text-right" autocomplete="off">';
	body += '</td>';	
	body += '</tr>';
	$(".bill-table tbody").append(body);
	removeSuggestionToTable();
	addSuggestionToTable();
}
function addSuggestionToTable(){
	var plant = document.form1.plant.value;	
	$(".journalaccountSearch").typeahead({
		input:".journalaccountSearch",
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true,
	  classNames: {
		 	menu: 'bigdrop'
		  }
	},
	{	  
	  display: 'accountname',  
	  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ChartOfAccountServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				action : "getSubAccountTypeGroup",
				module:"contraaccount",
				ITEM : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.results);
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
		suggestion: function(item) {
			if (item.issub) {
				var $state = $(
					    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
					  );
				}
			else
				{
				var $state = $(
						 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
					  );
				}
			  
			  return $state;
		}
	  }
	}).on('typeahead:render',function(event,selection){
		
	}).on('typeahead:open',function(event,selection){

	}).on('typeahead:close',function(){

	});
}
var numberOfDecimal = $("#numberOfDecimal").val();
function removeSuggestionToTable(){
	$(".journalaccountSearch").typeahead('destroy');
}
function journaldebit(node)
{
	//var debit=$(node).val();
	var debit= checkamount($(node).val(),node);
	debit=parseFloat(debit).toFixed(numberOfDecimal);
	$(node).val(debit);
	$(node).closest('tr').find("td:nth-child(4)").find('input').val("");
	calculateDebitTotal();
	calculateCreditTotal();
	}
function journalcredit(node)
{
	//var credit=$(node).val();
	var credit= checkamount($(node).val(),node);
	credit=parseFloat(credit).toFixed(numberOfDecimal);
	$(node).val(credit);
	$(node).closest('tr').find("td:nth-child(3)").find('input').val("");
	calculateCreditTotal();
	calculateDebitTotal();
	}	
	function calculateDebitTotal()
	{
		
		var totaldebit=0.00;
		$('input[name=debit]').each(function(){
			var value=$(this).val();
			if(value!="")
				totaldebit+=parseFloat(value);
			
		})
		totaldebit=parseFloat(totaldebit).toFixed(numberOfDecimal);
		$('#subtot-debitamt').html(totaldebit);
		$('#total-debitamount').html(totaldebit);
		$('#subtot-debitamt-show').html(totaldebit.replace(/\d(?=(\d{3})+\.)/g, "$&,"));
		$('#total-debitamount-show').html(totaldebit.replace(/\d(?=(\d{3})+\.)/g, "$&,"));
		
	}
	function calculateCreditTotal()
	{
		var totalcredit=0.00;
		$('input[name=credit]').each(function(){
			var value=$(this).val();
			if(value!="")
				totalcredit+=parseFloat(value);
		})
		totalcredit=parseFloat(totalcredit).toFixed(numberOfDecimal);
		$('#subtot-creditamt').html(totalcredit);
		$('#total-creditamount').html(totalcredit);
		$('#subtot-creditamt-show').html(totalcredit.replace(/\d(?=(\d{3})+\.)/g, "$&,"));
		$('#total-creditamount-show').html(totalcredit.replace(/\d(?=(\d{3})+\.)/g, "$&,"));
	}
	$("#btnBillOpen").click(function(){
		var totdebitamt= parseFloat($('#total-debitamount').html());
		var totcreditamt=parseFloat($('#total-creditamount').html());
		var isItemValid = true;
		$("#sub_total").val(totdebitamt);
		$("#total_amount").val(totcreditamt);
		$("input[name ='journal_account_name']").each(function() {
		    if($(this).val() == ""){	    	
		    	alert("The Contra account field cannot be empty.");
		    	isItemValid = false;
				return false;
		    }
		});
		
		 if(totdebitamt!=totcreditamt)
			{
				alert("Debit and Credit should be equal");
				isItemValid = false;
				return false;
			}
		else if(totdebitamt<=0)
			{
				alert("Please fill all the details");
				isItemValid = false;
				return false;
			}
		if(isItemValid)
			$("#journalForm").submit();
	});
	$("#billAttch").change(function(){
		var files = $(this)[0].files.length;
		var sizeFlag = false;
			if(files > 5){
				$(this)[0].value="";
				alert("You can upload only a maximum of 5 files");
				$("#billAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
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
					$("#billAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
				}else{
					$("#billAttchNote").html(files +" files attached");
				}
				
			}
		});
	function viewFile(id)
	{
		 var urlStrAttach = "/track/InvoicePayment?action=downloadAttachmentById&attachid="+id;
		 var xhr=new XMLHttpRequest();
		 xhr.open("GET", urlStrAttach, true);
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
	         a.target = '_blank';
	         document.body.append(a);
	         a.click();
	         a.remove();
	         window.URL.revokeObjectURL(url); 
		   }
		 })
		 xhr.send();
	}
	function downloadFile(id,fileName)
	{
		 var urlStrAttach = "/track/InvoicePayment?action=downloadAttachmentById&attachid="+id;
		 var xhr=new XMLHttpRequest();
		 xhr.open("GET", urlStrAttach, true);
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
	function removeFile(id)
	{
		var urlStrAttach = "/track/JournalServlet?action=removeAttachmentById&removeid="+id;	
		$.ajax( {
			type : "GET",
			url : urlStrAttach,
			success : function(data) {
						window.location.reload();
					}
				});
	}
	
	function checkamount(baseamount,node){
		var zeroval = parseFloat("0").toFixed(numberOfDecimal);
		if(baseamount != ""){
			var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
			var numbers = /^[0-9]+$/;
			var dotdecimal = /^\.[0-9]+$/; 
			if(baseamount.match(decimal) || baseamount.match(numbers)) 
			{ 
				//node.val(parseFloat(baseamount).toFixed(3));	
				return $(node).val();
			}else if(baseamount.match(dotdecimal)){
				return parseFloat("0"+baseamount).toFixed(numberOfDecimal);
			}else{
				alert("Please Enter Valid Amount");
				return zeroval;
			}
		}else{
			return $(node).val();
		}
	}

	function isNumberKey(evt, element, id) {
		  var charCode = (evt.which) ? evt.which : event.keyCode;
		  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
			  {
		    	return false;
			  }
		  return true;
		}
</script>