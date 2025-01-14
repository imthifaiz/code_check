<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />

<%
String title = "Payroll User Access Rights Summary";

//imthziaf start

%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>


<script language="javascript">
 var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
function onGo(){
	document.form1.action="../useraccess/paysummary";
   document.form1.submit();
}
</script>


<%
	String plant = (String) session.getAttribute("PLANT");
	String userName = StrUtils.fString(
			(String) session.getAttribute("LOGIN_USER")).trim();
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	boolean displaySummaryNew=false,displaySummaryEdit=false,displaySummaryLink=false;
	if(systatus.equalsIgnoreCase("PAYROLL")) {
		displaySummaryNew = ub.isCheckValPay("newuaMaintAcct", plant,username);
		displaySummaryEdit = ub.isCheckValPay("edituaMaintAcct", plant,username);
		displaySummaryLink = ub.isCheckValPay("summarylnkuaMaintAcct", plant,username);	
	}
	
	
	
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	ArrayList invQryList = new ArrayList();
	_userBean.setmLogger(mLogger);
	
	String fieldDesc = "";
	String PLANT = "", GROUP = "";
	String html = "";
	int Total = 0;
	String SumColor = "";
	boolean flag = false;
	session = request.getSession();
	String PGaction = strUtils
			.fString(request.getParameter("PGaction")).trim();
	PLANT = session.getAttribute("PLANT").toString();
	GROUP = strUtils.fString(request.getParameter("GROUP"));
	fieldDesc = strUtils.fString(request.getParameter("RESULT"));
	if (PGaction.equalsIgnoreCase("View")) {
		try {
			Hashtable ht = new Hashtable();
			if (strUtils.fString(PLANT).length() > 0)
				ht.put("PLANT", PLANT);
			if (strUtils.fString(GROUP).length() > 0)
				ht.put("LEVEL_NAME", GROUP);

			invQryList = _userBean
					.getGroupListSummarypayroll(ht, PLANT, GROUP);
			if (invQryList.size() == 0) {
				fieldDesc = "Data Not Found";
			}
		} catch (Exception e) {
		}
	}else{
		Hashtable ht = new Hashtable();
		if (strUtils.fString(PLANT).length() > 0)
			ht.put("PLANT", PLANT);
		invQryList = _userBean
				.getGroupListSummarypayroll(ht, PLANT, GROUP);
		if (invQryList.size() == 0) {
			fieldDesc = "Data Not Found";
		}
	}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Payroll User Access Rights Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                 <div class="box-title pull-right">
                 <% if (displaySummaryNew) { %>
              <button type="button" class="btn btn-default" onClick="window.location.href='../useraccess/paynew'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;<% } %>
              </div>
		</div>
		
 <div class="box-body">
 
<%-- <div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div>  --%>
	
<form class="form-horizontal" name="form1" method="post" action="../useraccess/paysummary"> 
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
		
<div class="form-group">
<label class="control-label col-sm-4" for="Group">Payroll User Access Rights</label>
<div class="col-sm-4 ac-box" >
<div class="input-group">
<INPUT class="ac-selected form-control typeahead" name="GROUP" id="GROUP" type = "TEXT" value="<%=GROUP%>" size="20" MAXLENGTH=20>
<span class="select-icon" onclick="$(this).parent().find('input[name=\'GROUP\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/group_list_payroll.jsp?GROUP='+form1.GROUP.value);"> -->
<!--  <span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>
  <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
		</div>
	</form>
	<br>

<div style="overflow-x:auto;">
<table id="table" class="table table-bordered table-hover dataTable no-footer "
   role="grid" aria-describedby="tableInventorySummary_info" > 
   
   <thead style="background: #eaeafa;text-align: center">  
          <tr>  
            <th >S/N</th>  
            <th>Payroll User Access Rights Name</th>
            <th>Edit</th>       
          </tr>  
        </thead> 
        
        <tbody>
	<%
		for (int iCnt = 0; iCnt < invQryList.size(); iCnt++) {
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#dddddd";
			Map lineArr = (Map) invQryList.get(iCnt);
			
			String lname = (String) lineArr.get("LEVEL_NAME");
	%>
	  <TR>
		<TD align="center"><%=iIndex%></TD>
		<%if(displaySummaryLink){%>
		<TD align="center"><a href="../useraccess/paydetail?action=View&LEVEL_NAME=<%=(String) lineArr.get("LEVEL_NAME")%>"><%=(String) lineArr.get("LEVEL_NAME")%></a></TD>
		<% } else { %>
		<TD align="center" class="textboald">&nbsp; <%=strUtils.removeQuotes((String)lineArr.get("LEVEL_NAME"))%></TD>		
		<% } %>
		 <%-- <%if(!lname.equalsIgnoreCase("EmployeeGroup")){%> --%>
		 <%if(!lname.equalsIgnoreCase("DefaultGroup")){%> 
		<%if(displaySummaryEdit){%> 
		<TD align="center"><a href="../useraccess/payedit?action=View&LEVEL_NAME=<%=(String) lineArr.get("LEVEL_NAME")%>"><i class="fa fa-pencil-square-o"></i></a></TD>	
		 <%} else {%>
		 <TD align="center"><a style="pointer-events: none; cursor: default; color: darkgrey;" href="#"><i class="fa fa-pencil-square-o"></i></a></TD> 
		 <% } }%>
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
/* 	$('#table').dataTable({
		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
	}); */
    $('[data-toggle="tooltip"]').tooltip();
});
</script>

<script>
$(document).ready(function(){

$('[data-toggle="tooltip"]').tooltip();
var plant= '<%=PLANT%>';


// UserId Auto Suggestion //
$('#GROUP').typeahead({
hint: true,
minLength:0,
searchOnFocus: true
},
{
display: 'LEVEL_NAME',
async: true,
source: function (query, process,asyncProcess) {
var urlStr = "/track/MasterServlet";
$.ajax( {
type : "POST",
url : urlStr,
async : true,
data : {
PLANT : plant,
ACTION : "GET_USERADMIN_FOR_USERACCESS",
GROUP : query
},
dataType : "json",
success : function(data) {
return asyncProcess(data.USERLEVEL);
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
return '<p class="item-suggestion">'+ data.LEVEL_NAME +'</p>';
}
}

}).on('typeahead:open',function(event,selection){
var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
element.toggleClass("glyphicon-menu-up",true);
element.toggleClass("glyphicon-menu-down",false);
}).on('typeahead:close',function(){
var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
element.toggleClass("glyphicon-menu-up",false);
element.toggleClass("glyphicon-menu-down",true);
});

});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>