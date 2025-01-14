<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Order Status Summary";
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
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
 function onGo(){
 
     document.form1.submit();
}
 function onView(){
	   document.form1.action  = "orderstatusSummary.jsp?action=VIEW";
	   document.form1.submit();
	}
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
userBean _userBean      = new userBean();
ArrayList statusQryList  = new ArrayList();

_userBean.setmLogger(mLogger);

String fieldDesc="";
String PLANT="",STATUSID ="";
String html = "";
int Total=0;
String SumColor="";
boolean flag=false;
session=request.getSession();
String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
STATUSID     = strUtils.fString(request.getParameter("STATUS_ID"));


OrderStatusUtil ordstatusutil = new OrderStatusUtil();

ordstatusutil.setmLogger(mLogger);

if(PGaction.equalsIgnoreCase("View")){
 try{
     
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)  ht.put("PLANT",PLANT);
			//if(LOCID.equalsIgnoreCase("")||LOCID==null)	RSN = strUtils.fString(request.getParameter("PRD_TYPE_ID1"));
         statusQryList =  ordstatusutil.getOrdStatusList(STATUSID,PLANT,"");
      
      if(statusQryList.size()== 0)
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
	
	
<FORM class="form-horizontal" name="form1" method="post" action="orderstatusSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="STATUS_DESC" value="">
  
<div class="form-group">
<label class="control-label col-sm-4" for="Order Status">Order Status:</label>
<div class="col-sm-4" >
<div class="input-group">
<INPUT name="STATUS_ID" type = "TEXT" value="<%=STATUSID%>" size="20"  MAXLENGTH=100 class="form-control">
<span class="input-group-addon" onClick="javascript:popWin('OrderStatusList.jsp?ORDERSTATUS='+form1.STATUS_ID.value);">
 <a href="#" data-toggle="tooltip" data-placement="top" title="Order Status Details">
 <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>
  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
  <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
		</div>
		<INPUT name="ACTIVE" type = "hidden" value="Y"   >
            <INPUT name="ACTIVE" type = "hidden" value="N"   >
	</form>
	<br>
  
<div style="overflow-x:auto;">
<table id="table" class="table table-bordered table-hover dataTable no-footer "
   role="grid" aria-describedby="tableInventorySummary_info" > 
   
   <thead style="background: #eaeafa;text-align: center">  
          <tr>  
            <th >S/N</th>  
            <th>Order Status</th> 
            <th>Description</th>
            <th>IsActive</th>   
          </tr>  
        </thead> 
        
        <tbody>
  
 
   	  <%
         
         
          for (int iCnt =0; iCnt<statusQryList.size(); iCnt++){
			     int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
           
          
            Map lineArr = (Map) statusQryList.get(iCnt);
       %>

          <TR>
            <TD align="center"><%=iIndex%></TD>
            
           <TD align="center"> <a href ="orderstatusDetail.jsp?action=View&ORDERSTATUS=<%=(String)lineArr.get("STATUS_ID")%>&STATUS_DESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get("STATUS_DESC"))%>&ISACTIVE=<%=(String)lineArr.get("ISACTIVE")%>"%><%=(String)lineArr.get("STATUS_ID")%></a></TD>
    	     <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get("STATUS_DESC"))%></TD>
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