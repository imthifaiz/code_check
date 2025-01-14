<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="com.track.db.util.BankUtil"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Bank Summary";
String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
String mainmenu="";
boolean displaySummaryNew=false,displaySummaryExport=false,displaySummaryEdit=false,displaySummaryDelete=false;
if(systatus.equalsIgnoreCase("PAYROLL")) {
	displaySummaryNew = ub.isCheckValPay("newbank", plant,username);
	displaySummaryExport = ub.isCheckValPay("exportbank", plant,username);
	displaySummaryEdit = ub.isCheckValPay("editbank", plant,username);
	displaySummaryDelete = ub.isCheckValPay("deletebank", plant,username);
	mainmenu = IConstants.SETTINGS;
}
if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	displaySummaryNew = ub.isCheckValAcc("newbank", plant,username);
	displaySummaryExport = ub.isCheckValAcc("exportbank", plant,username);
	displaySummaryEdit = ub.isCheckValAcc("editbank", plant,username);
	displaySummaryDelete = ub.isCheckValAcc("deletebank", plant,username);
	mainmenu = IConstants.ACCOUNTING;
}


PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
String collectionDate=du.getDate();
ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
Map map = (Map) al.get(0);
String CNAME = (String) map.get("PLNTDESC");
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%= mainmenu %>"/>
    <jsp:param name="submenu" value="<%=IConstants.BANKING%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/Bank.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onGo(){
	   var flag    = "false";
	   //document.form.xlAction.value="";
	   var BANK_NAME    = document.form1.BANK_NAME.value;
	   var BANK_BRANCH      = document.form1.BANK_BRANCH.value;
	   var BANK_BRANCH_CODE      = document.form1.BANK_BRANCH_CODE.value;	   

	   if(BANK_NAME != null     && BANK_NAME != "") { flag = true;}
	   if(BANK_BRANCH != null    && BANK_BRANCH != "") { flag = true;}
	   if(BANK_BRANCH_CODE != null     && BANK_BRANCH_CODE != "") { flag = true;}
	   
	   if(flag == "false"){ /* alert("Please define any one search criteria"); */ return false;}
	  
	document.form1.action="../banking/summary";
	document.form1.submit();
}

function ExportReport(){
	document.form1.action = "/track/ReportServlet?action=Export_Excel_Bank";
	document.form1.submit();
	
}

</script>

<%
StrUtils strUtils     = new StrUtils();
//ArrayList movQryList  = new ArrayList();
BankUtil bankUtil = new BankUtil();
bankUtil.setmLogger(mLogger);
String cntRec ="false";
session = request.getSession();
String responseMsg = request.getParameter("response");
String BANK_NAME = strUtils.fString(request.getParameter("BANK_NAME")).trim();
String BANK_BRANCH = strUtils.fString(request.getParameter("BANK_BRANCH")).trim();
String BANK_BRANCH_CODE = strUtils.fString(request.getParameter("BANK_BRANCH_CODE")).trim();
List bankItemsList = new ArrayList();
if( session.getAttribute("bankItemsList")!=null){
	 bankItemsList = (ArrayList)session.getAttribute("bankItemsList");
	 session.setAttribute("bankItemsList",null);
}
	
		if(responseMsg == null)
			responseMsg = "";
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
           <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                       
                <li><label>Bank Summary</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
               <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              
              <div class="btn-group" role="group">
               <% if (displaySummaryNew) { %>
              <button type="button" class="btn btn-default" onClick="window.location.href='../banking/new'" style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;<% } %>
              </div>
              <h1 style="font-size: 18px; cursor: pointer;"	class="box-title pull-right" onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
			</div>
		</div>
		
 <div class="box-body">
 
 <div class="mainred">
 
	  	<CENTER><strong><%=responseMsg%></strong></CENTER>
	</div>
 
<FORM class="form-horizontal" name="form1" method="post" action="../banking/summary">
<!--   <input type="hidden" name="PRD_BRAND_DESC" value="">
 <INPUT name="ACTIVE" type = "hidden" value="">
  -->
  <input type="text" name="plant" value="<%=plant%>" hidden>
    <div class="form-group">
	<label class="control-label col-form-label col-sm-2">Bank Name</label>
	<div class="col-sm-4" >
	<INPUT name="BANK_NAME" type = "TEXT" value="<%=StrUtils.forHTMLTag(BANK_NAME)%>" id="pono" MAXLENGTH=100 class="form-control" placeholder="BANK NAME">
	<span class="select-icon"><i class="glyphicon glyphicon-menu-down"></i></span>
  	</div>
   	</div>
  	<div class="form-group">
	<label class="control-label col-form-label col-sm-2">Branch Name</label>
	<div class="col-sm-4" >
	<INPUT name="BANK_BRANCH" type = "TEXT" value="<%=StrUtils.forHTMLTag(BANK_BRANCH)%>" id="branchname" MAXLENGTH=100 class="form-control" placeholder="BRANCH NAME">
  	<span class="select-icon"><i class="glyphicon glyphicon-menu-down"></i></span>
  	</div>
   	</div>
 	<div class="form-group">
	<label class="control-label col-form-label col-sm-2">Branch Code</label>
	<div class="col-sm-4" >
	<INPUT name="BANK_BRANCH_CODE" type = "TEXT" value = "<%=StrUtils.forHTMLTag(BANK_BRANCH_CODE)%>" id="branchcode" MAXLENGTH=100 class="form-control" placeholder="BRANCH CODE">
	<span class="select-icon"><i class="glyphicon glyphicon-menu-down"></i></span>
  	</div>	&nbsp;&nbsp;
    <button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;&nbsp;
     <% if (displaySummaryExport) { %>
    <button type="button" class="btn btn-default" value='Export Master Data'  onclick="ExportReport();">Export Master Data</button>&nbsp;&nbsp;<% } %>
  	<!-- <button type="button" class="btn btn-default" onClick="window.location.href='../home'">Back</button> -->&nbsp;&nbsp; 
 	</div>
    </FORM>
   
  	<br>

	<div id="VIEW_RESULT_HERE" class="table-responsive">
  	<div id="tableMovementHistory_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
    <div class="row"><div class="col-sm-12">
    <table id="tableMovementHistory"
	class="table table-bordered table-hover dataTable no-footer"
	role="grid" aria-describedby="tableMovementHistory_info">
	<thead>
	<tr role="row">  
          	<tr>  
            <th>S/N</th>  
            <th>Bank Name</th>
            <th>Branch Name</th> 
            <th>Branch code</th>
            <th>Swift Code</th>
            <th>IFSC Code</th>
            <th>Contact Person</th>
            <th>Mobile No.</th>
            <th>Tel No.</th>
            <th>Fax</th>
            <th>Email</th>
            <th>Website</th>
            <th>Unit No</th>
            <th>Building</th>
            <th>Street</th>
            <th>City</th>
            <th>State</th>
            <th>Country</th>
            <th>Postal Code</th>
            <th>Facebook Id</th>
            <th>Twitter Id</th>
            <th>LinkedIn Id</th>
            <th>Remarks</th>
       <!--      <th>IsActive</th>-->
             <th>Edit</th>     
          </tr>  
        </thead> 
        </table>
        </div>
						</div>
					</div>
  </div>
      <script type="text/javascript">
      
  var tableData = [];
  
  <%
  bankItemsList = bankUtil.getToBankSummary(plant,BANK_BRANCH,BANK_BRANCH_CODE,BANK_NAME);
  if(bankItemsList.size()<=0)
          cntRec ="true";
  if(bankItemsList.size()<=0 && cntRec=="true" ){ %>

   	   <%}else{
         for (int iCnt =0; iCnt<bankItemsList.size(); iCnt++){
			     int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
           
          
             Map lineArr = (Map) bankItemsList.get(iCnt);
        
          
       %>
       var rowData = [];
       rowData[rowData.length] = '<%=iIndex%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("NAME")%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("BRANCH_NAME"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("BRANCH"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("SWIFT_CODE"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("IFSC_CODE"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("CONTACT_PERSON")) %>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("HPNO"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("TELNO"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("FAX"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("EMAIL"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("WEBSITE")) %>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("UNITNO")) %>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("BUILDING")) %>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("STREET"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("CITY"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("STATE"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("COUNTRY"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("ZIP"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("FACEBOOKID"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("TWITTERID"))%>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("LINKEDINID")) %>';
       rowData[rowData.length] = '<%=StrUtils.fString((String)lineArr.get("NOTE")) %>';
       <% if (displaySummaryEdit) { %>
       rowData[rowData.length] = '<a href ="../banking/edit?action=VIEW&BANK_BRANCH_CODE=<%=(String)lineArr.get("BRANCH")%>"%><i class="fa fa-pencil-square-o"></i></a>';
       <% } else {%>
       rowData[rowData.length] = '<a style="pointer-events: none; cursor: default; color: darkgrey;" href ="#"><i class="fa fa-pencil-square-o"></i></a>';
       <% } %>
       <%-- rowData[rowData.length] = '<a href ="maint_Bank.jsp?&BANK_NAME=<%=(String)lineArr.get("NAME")%>&BANK_BRANCH=<%=(String)lineArr.get("BRANCH_NAME")%>&BANK_BRANCH_CODE=<%=(String)lineArr.get("BRANCH")%>&SWIFT_CODE=<%=(String)lineArr.get("SWIFT_CODE")%>&IFSC=<%=(String)lineArr.get("IFSC_CODE")%>&TELNO=<%=(String)lineArr.get("TELNO")%>&FAX=<%=(String)lineArr.get("FAX")%>&EMAIL=<%=(String)lineArr.get("EMAIL")%>&WEBSITE=<%=(String)lineArr.get("WEBSITE")%>&FACEBOOKID=<%=(String)lineArr.get("FACEBOOKID")%>&TWITTERID=<%=(String)lineArr.get("TWITTERID")%>&LINKEDINID=<%=(String)lineArr.get("LINKEDINID")%>&UNITNO=<%=(String)lineArr.get("UNITNO")%>&BUILDING=<%=(String)lineArr.get("BUILDING")%>&STREET=<%=(String)lineArr.get("STREET")%>&CITY=<%=(String)lineArr.get("CITY")%>&STATE=<%=(String)lineArr.get("STATE")%>&COUNTRY=<%=(String)lineArr.get("COUNTRY")%>&ZIP=<%=(String)lineArr.get("ZIP")%>&CONTACT_PERSON=<%=(String)lineArr.get("CONTACT_PERSON")%>&HPNO=<%=(String)lineArr.get("HPNO")%>&NOTE=<%=(String)lineArr.get("NOTE")%>"%><i class="fa fa-pencil-square-o"></i></a>'; --%>
       tableData[tableData.length] = rowData;
             
       <%}%> 
       
       <%}%>
       
       var groupColumn = 1;
       $(document).ready(function(){
      	 $('#tableMovementHistory').DataTable({
      		"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
      		  	data: tableData,
      		  	"columnDefs": [{"className": "t-right", "targets": []}],
      			/* "orderFixed": [ groupColumn, 'asc' ], */ 
      		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
      			"<'row'<'col-md-6'><'col-md-6'>>" +
      			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
      	        buttons: [
      	        	{
      	                extend: 'collection',
      	                text: 'Export',
      	                buttons: [
      	                    {
      	                    	extend : 'excel',
      	                    	exportOptions: {
      	    	                	columns: [':visible']
      	    	                }
      	                    },
      	                    {
      	                    	extend : 'pdf',
      	                    	exportOptions: {
      	                    		columns: [':visible']
      	                    	},
                          		orientation: 'landscape',
                                  pageSize: 'A3'
      	                    }
      	                ]
      	            },
      	            {
      	            	extend: 'colvis',
                          columns: ':not(:eq('+groupColumn+')):not(:eq(2)):not(:eq(3)):not(:eq(last))'
                      }
      	        ],
      	       "order": [],
		        drawCallback: function() {
		        	<%if(!displaySummaryExport){ %>
		        	$('.buttons-collection')[0].style.display = 'none';
		        	<% } %>
		        	} 
      		  });	 
      	
       });
 
</script>
	  
 <script>
 $(document).ready(function(){

	$('#table').dataTable({
		"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
	});
    
    $('[data-toggle="tooltip"]').tooltip();
});

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>