<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Customer Status Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

var subWin = null;
  function popWin(URL) {
    subWin = window.open(URL, 'CustomerStatusSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
 function onGo(){
 
     document.form1.submit();
}
 function onView(){
	   document.form1.action  = "customerStatusSummary.jsp?action=VIEW";
	   document.form1.submit();
	}
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
userBean _userBean      = new userBean();
ArrayList QryList  = new ArrayList();
String fieldDesc="";
String PLANT="",CUSTOMERSTATUSID  ="";
String html = "";
int Total=0;
String SumColor="";
boolean flag=false;
session=request.getSession();
String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
CUSTOMERSTATUSID     = strUtils.fString(request.getParameter("CUSTOMER_STATUS_ID"));
CustUtil _CustUtil = new CustUtil();
if(PGaction.equalsIgnoreCase("View")){
 try{
      Hashtable ht = new Hashtable();
      ht.put("CUSTOMER_STATU_ID",CUSTOMERSTATUSID);
      if(strUtils.fString(PLANT).length() > 0)  ht.put("PLANT",PLANT);
	     QryList =  _CustUtil.getCustTypeList(CUSTOMERSTATUSID ,PLANT,"");
	     QryList = _CustUtil.getCustomerStatusDetails(CUSTOMERSTATUSID,PLANT," ",ht);
      if(QryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }

 }catch(Exception e) { }
}
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
<div class="mainred">
	  	<CENTER><strong><%=fieldDesc%></strong></CENTER>
	</div>
	
<FORM class="form-horizontal" name="form1" method="post" action="customerStatusSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="CUSTOMER_STATHS_DESC" value="">


<div class="form-group">
<label class="control-label col-sm-4" for="Customer Status Desc or ID">Customer Status Desc or ID:</label>
<div class="col-sm-4" >
<div class="input-group">
<INPUT name="CUSTOMER_STATUS_ID" type = "TEXT" value="<%=CUSTOMERSTATUSID %>" size="20"  MAXLENGTH=100 class="form-control">
<span class="input-group-addon" onClick="javascript:popWin('Customer_Status_List.jsp?CUSTOMERSTATUID='+form1.CUSTOMER_STATUS_ID.value);">
 <a href="#" data-toggle="tooltip" data-placement="top" title="Customer Status Details">
 <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>
  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
		</div>
		
   <INPUT name="ACTIVE" type = "hidden" value="Y" >
   <INPUT name="ACTIVE" type = "hidden" value="N" >
   <INPUT name="DESC" type = "hidden" value="" >
   <INPUT name="REMARKS" type = "hidden" value="">
	</form>
	<br>
  
 <div style="overflow-x:auto;">
<table id="table" class="table table-bordered table-hover dataTable no-footer "
   role="grid" aria-describedby="tableInventorySummary_info" > 
   
   <thead style="background: #eaeafa;text-align: center">  
          <tr>  
            <th >S/N</th>  
            <th>Customer Status ID</th>
            <th>Customer Status Description</th> 
            <th>IsActive</th>     
          </tr>  
        </thead> 
        
        <tbody>
 
   	  <%  	
          for (int iCnt =0; iCnt<QryList.size(); iCnt++){
		  	int iIndex = iCnt + 1;
         	 String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          	Map lineArr = (Map) QryList.get(iCnt);
       %>
          <TR>
            <TD align="center"><%=iIndex%></TD>
             <TD align="center"> <a href ="customerStatusSummaryDetails.jsp?action=View&CUSTOMER_STATUS_ID=<%=(String)lineArr.get("CUSTOMERSTATUID")%>&CUSTOMER_STATUS_DESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get("CUSTOMERSTATUSDESC"))%>&REMARKS=<%=strUtils.replaceCharacters2Send((String)lineArr.get("REMARKS"))%>&ISACTIVE=<%=(String)lineArr.get("ISACTIVE")%>"%><%=(String)lineArr.get("CUSTOMERSTATUID")%></a></TD>
    	     <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get("CUSTOMERSTATUSDESC"))%></TD>
              <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get("ISACTIVE"))%></TD>
          </TR>
       <%}%>
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