<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.MLogger"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Import Salary Type Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PAYROLL%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALARY_TYPE%>" />
</jsp:include>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/fileshowcasedesign.css">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


<script language="javascript">
function onConfirm()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=confirmSalaryMstCountSheet" ;
  frmRoot.submit();
  return true;
}

function onDownloadSalaryMstTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadSalaryMstTemplate" ;
  frmRoot.submit();
  return true;
}

function onGo()
{
 
 var frmRoot=document.form1;
  
 	if(frmRoot.ImportFile.value=="" || frmRoot.ImportFile.value.length==0)
	{
		alert("Please Select Import file!");
		frmRoot.ImportFile.focus();
		return false;
	}
  else if(frmRoot.SheetName.value=="" || frmRoot.SheetName.value.length==0)
	{
		alert("Please Enter Sheet Name!");
		frmRoot.SheetName.focus();
		return false;
	}
  else{
   
      var con = confirm ("Process will take few minutes  to download");
      if(con) {
         
          var file=frmRoot.ImportFile.value;
          var sheet=frmRoot.SheetName.value;
          var trunkateData ="";
          frmRoot.action = "/track/ImportServlet?action=importSalaryMstCountSheet" + "&ImportFile=" + file +"&SheetName=" + sheet ;
          frmRoot.submit();
          return true;
   }else
   {
         return false;
   }
    
  
}
  
 
}

function onClear()
{
	document.form.action  = "importSalaryMst.jsp";
	document.form.submit();
}

</script>
    
  <%
 


MLogger logger = new MLogger();
String fieldDesc="";
String msg="";

String PLANT              = (String)session.getAttribute("PLANT");
String _login_user        = (String)session.getAttribute("LOGIN_USER");
String ServerName =  StrUtils.fString(request.getRemoteHost()).trim();
String action         = StrUtils.fString(request.getParameter("action")).trim();
String ImportFile         = StrUtils.fString(request.getParameter("ImportFile")).trim();
String SheetName         = StrUtils.fString(request.getParameter("SheetName")).trim();

System.out.println("ImportFileNAme *** :" + ImportFile); 
System.out.println("SheetName *** :" + SheetName);
String buttonStat ="";
boolean isExistsCountSheetData = false;

Hashtable ht = new Hashtable();


if(PLANT==null){
String result = "New user diverting to login page";
%>
    <jsp:forward page="login.jsp" >
    <jsp:param name="warn" value="<%=result%>" />
    </jsp:forward>
<%
}

ht.put("PLANT",PLANT);

if(action.equalsIgnoreCase("new")){
  
   session.removeAttribute("IMP_SALARYRESULT");
   session.removeAttribute("SALARYRESULT");
   ImportFile="";
   SheetName="";
   action="";
   
}

 ArrayList alRes = null;
 System.out.println("Action *** :" + action);

 fieldDesc=StrUtils.fString((String)session.getAttribute("SALARYRESULT"));

 if(null == session.getAttribute("IMP_SALARYRESULT"))
    {
      alRes = new ArrayList();
      buttonStat= "disabled";
   } else{
    
      alRes = (ArrayList)session.getAttribute("IMP_SALARYRESULT");
      
      if(alRes.size()==0)
      buttonStat= "disabled";
  }
  
 MLogger.log(1, "_______________________ " + this.getClass() + "" +msg);

     
   
%>
  <div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='EmployeeSalarySummary.jsp'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="/ImportServlet">
 <input type="hidden"  name="isDataPresent" value="true" ></input>
    <input type="hidden" value="" name="download"></input>
     <div class="form-group" style="margin-bottom: 3%;">
     	<div class="col-sm-3">  
     		<button type="button" class="btn btn-success" style="margin-left: 14%;" onclick="javascript:return onDownloadSalaryMstTemplate();">Download Salary Type Template</button>
     	</div> 
     </div>
 		
 <div class="form-group">
      <label class="control-label col-sm-2">Select Excel Sheet:</label>
      <div class="col-sm-3">          
        <INPUT class="form-control" name="ImportFile" type="file" size="20" value="<%=ImportFile%>" maxlength="20">
          </div>
          
          
 <div class="form-inline">
      <label class="control-label col-sm-2">Sheet Name:</label>
      <div class="col-sm-2">          
        <INPUT class="form-control" name="SheetName" type="text" value="Sheet1" size="20" maxlength="20">
          </div>
          <div class="col-sm-1">
          <button type="button" class="Submit btn btn-default" name="Submit" value="Import" onclick="javascript:return onGo();"><b>Import</b></button>&nbsp;&nbsp;
          </div>
          </div>
          </div>
          
      <div class="form-group">        
      	<div class="col-sm-12" align="center">
       	<button type="button" class="Submit btn btn-default" onclick="onClear();">Clear</button>&nbsp;
       	<button type="button" class="btn btn-success" name="Submit" value="Confirm" onclick="javascript:return onConfirm();" <%=buttonStat%>>Confirm</button>&nbsp;
          </div>
          </div>    
 
 
  
    
    <br>
    <div style="overflow-x:auto;">
  <TABLE class="table table-bordred table-striped" WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   <thead>
        <tr>
        <th style="font-size: smaller;"><font align="center">SNO</font></th>
        <th style="font-size: smaller;"><font align="left">SALARY TYPE</font></th>
         </tr>
         </thead>
         <tbody>
      <%
          for (int iCnt =0; iCnt<alRes.size(); iCnt++){
          Map lineArr = (Map) alRes.get(iCnt);
         
         int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
         
      
       %>
              <TR>
                <TD align="left" ><%= iIndex %></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("SALARYTYPE"))%></TD>  
           </TR> 
       <%}%>
    </tbody>
    </TABLE>
    
      <table border="0" cellspacing="1" cellpadding = 2 align="center" bgcolor="">
             <%=fieldDesc%>
      </table>
   </div>
  

  </form>
  </div>
  </div>
  </div>
  
  <% MLogger.log(-1, "_______________________ " + this.getClass() + ""); %>
  
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>