<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Consignment Order Customer Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
	var subWin = null;
  	function popUpWin(URL) {
    	subWin = window.open(URL, 'To_Assignee', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  	}
 	function onGo(){
 	  document.form.action ="toassigneeSummary.jsp";
      document.form.submit();
	}
 	

	function ExportReport(){
		document.form.action = "/track/ReportServlet?action=Export_Contacts_Excel&ReportType=Transfer Order Customer";
		document.form.submit();
		
	} 
</script>

<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	List locQryList = new ArrayList();
	String fieldDesc = "";
	String PLANT = "", CUST_NAME = "",CUST_CODE="";
	String html = "";
	int Total = 0;
	String SumColor = "";
	boolean flag = false;
	session = request.getSession();
	String PGaction = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT = session.getAttribute("PLANT").toString();
	CUST_NAME = strUtils.fString(request.getParameter("CUST_NAME"));
	CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE"));
	CustUtil custUtils = new CustUtil();
	custUtils.setmLogger(mLogger);
	if (PGaction.equalsIgnoreCase("View")) {
		try {
			Hashtable ht = new Hashtable();
			if (strUtils.fString(PLANT).length() > 0)	ht.put("PLANT", PLANT);
			locQryList = custUtils.getToAssingeListStartsWithName(CUST_NAME, PLANT);
			if (locQryList.size() == 0) {
				fieldDesc = "Data Not Found";
			}

		} catch (Exception e) {
		}
	}
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="window.location.href='importTransferAssigneeExcelSheet.jsp'">
						Import Consignment Order Customer</button>
					&nbsp;
				</div>
				
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-default"
						onClick="window.location.href='toassignee_view.jsp'"
						style="background-color: rgb(214, 72, 48); color: rgb(255, 255, 255);">
						+ New</button>
					&nbsp;
				</div>
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
		
 <div class="box-body">
 
<div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div>
	
<FORM class="form-horizontal" name="form" method="post" action="toassigneeSummary.jsp">
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<input type="hidden" name="CUST_CODE1" value="<%=CUST_CODE%>"> 
<input type="hidden" name="CUST_CODE" value="<%=CUST_CODE%>"> <br>


<div class="form-group">
<label class="control-label col-sm-4" for="Assignee Name">Customer Name or ID:</label>
<div class="col-sm-4" >
<div class="input-group">
<input type="hidden" name="CUST_CODE1" value="">
<INPUT name="CUST_NAME" type="TEXT" value="<%=CUST_NAME%>" size="20" MAXLENGTH=100 class="form-control">
<span class="input-group-addon" onClick="javascript:popUpWin('toassignee_list.jsp?CUST_NAME='+form.CUST_NAME.value);">
 <a href="#" data-toggle="tooltip" data-placement="top" title="Customer Details">
 <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>
  <button type="button" class="Submit btn btn-default" value='Export Master Data'  onclick="ExportReport();"><b>Export Master Data</b></button>
  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
		</div>
	</form>
	<br>

<div style="overflow-x:auto;">
<table id="table" class="table table-bordered table-hover dataTable no-footer "
   role="grid" aria-describedby="tableInventorySummary_info" > 
   
   <thead style="background: #eaeafa;text-align: center">  
          <tr>  
            <th >S/N</th>  
            <th>Customer ID</th>
            <th>Customer Name</th>
            <th>Contact Name</th>
            <th>Telephone</th>
            <th>IsActive</th>    
          </tr>  
        </thead> 
        
        <tbody>

	<%
		for (int iCnt = 0; iCnt < locQryList.size(); iCnt++) {
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
			Map lineArr = (Map) locQryList.get(iCnt);
	%>
	
		<TR>
		<TD align="center">&nbsp;<%=iIndex%></TD>
	
		<TD align="center" class="textbold">&nbsp; <%=strUtils.fString((String) lineArr.get("ASSIGNENO"))%></TD>
		<TD align="center" class="textbold">&nbsp; <%=strUtils.removeQuotes((String) lineArr.get("ASSIGNENAME"))%></TD>
		<TD align="center" class="textbold">&nbsp;<%=strUtils.fString((String) lineArr.get("NAME"))%></TD>
		<TD align="center" class="textbold">&nbsp; <%=strUtils.fString((String) lineArr.get("TELNO"))%></TD>
		<TD align="center" class="textbold">&nbsp; <%=strUtils.fString((String) lineArr.get("ISACTIVE"))%></TD>
	</TR>
	
	<%
		}
	%>
 </tbody>  
</table>
</div>  
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>
$(document).ready(function(){
	$('#table').dataTable({
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
	});
    
    $('[data-toggle="tooltip"]').tooltip();
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>