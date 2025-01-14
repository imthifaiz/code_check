<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.MLogger"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Import Employee Master";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.EMPLOYEE%>" />
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

    <script language="javascript">

    function onDownloadEmployeeTemplate()
    {
      var frmRoot=document.form1;
      frmRoot.action = "/track/ImportServlet?action=downloadEmployeeTemplate" ;
      frmRoot.submit();
      return true;
    }
    
    function onDownloadEmployeeithoutPayrollTemplate()
    {
      var frmRoot=document.form1;
      frmRoot.action = "/track/ImportServlet?action=downloadEmployeeTemplateWithoutPayroll" ;
      frmRoot.submit();
      return true;
    }

function onConfirm()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=confirmEmployeeCountSheet" ;
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
          frmRoot.action = "/track/ImportServlet?action=importEmployeeCountSheet" + "&ImportFile=" + file +"&SheetName=" + sheet ;
          frmRoot.submit();
          return true;
   }else
   {
         return false;
   }
    
  
}
  
 
}

/* function onClear()
{
	document.form1.action  = "importEmployeeExcelSheet.jsp";
	document.form1.submit();
}
  */ 
</script>
    
  <%
 


MLogger logger = new MLogger();
String fieldDesc="";
String msg="";
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
String PLANT              = (String)session.getAttribute("PLANT");
String _login_user        = (String)session.getAttribute("LOGIN_USER");
String ServerName =  StrUtils.fString(request.getRemoteHost()).trim();
String action         = StrUtils.fString(request.getParameter("action")).trim();
String ImportFile         = StrUtils.fString(request.getParameter("ImportFile")).trim();
String SheetName         = StrUtils.fString(request.getParameter("SheetName")).trim();

String ispay = _PlantMstDAO.getispayroll(PLANT);

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
  
   session.removeAttribute("IMP_EMPLOYEERESULT");
   session.removeAttribute("EMPLOYEERESULT");
   ImportFile="";
   SheetName="";
   action="";
   
}

 ArrayList alRes = null;
 System.out.println("Action *** :" + action);

 fieldDesc=StrUtils.fString((String)session.getAttribute("EMPLOYEERESULT"));

 if(null == session.getAttribute("IMP_EMPLOYEERESULT"))
    {
      alRes = new ArrayList();
      buttonStat= "disabled";
   } else{
    
      alRes = (ArrayList)session.getAttribute("IMP_EMPLOYEERESULT");
      
      if(alRes.size()==0)
      buttonStat= "disabled";
  }
  
 MLogger.log(1, "_______________________ " + this.getClass() + "" +msg);

     
   
%>
  <div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../payroll/employee"><span class="underline-on-hover">Employee Summary</span> </a></li>                                           
                <li><label>Import Employee Master</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/employee'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="/ImportServlet">
 <input type="hidden"  name="isDataPresent" value="true" ></input>
    <input type="hidden" value="" name="download"></input>
    <div class="form-group" style="margin-bottom: 3%;">
     	<div class="col-sm-3">
     		<%if(ispay.equalsIgnoreCase("1")){%>  
     			<button type="button" class="btn btn-success" style="margin-left: 14%;" onclick="javascript:return onDownloadEmployeeTemplate();">Download Employee Template</button>
     		<%}else{ %>
     			<button type="button" class="btn btn-success" style="margin-left: 14%;" onclick="javascript:return onDownloadEmployeeithoutPayrollTemplate();">Download Employee Template</button>
     		<%} %>
     	</div> 
     </div>
    <br>
 
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
          <button type="button" class="Submit btn btn-success" name="Submit" value="Import" onclick="javascript:return onGo();">Import</button>&nbsp;&nbsp;
          </div>
          </div>
          </div>
          
      <div class="form-group">        
      	<div class="col-sm-12" align="center">
       	<button type="button" class="Submit btn btn-default" onclick="window.location.href='../payroll/importemployee'"> Clear</button>&nbsp;
       	<button type="button" class="Submit btn btn-success" name="Submit" value="Confirm" onclick="javascript:return onConfirm();" <%=buttonStat%>>Confirm</button>&nbsp;
          </div>
          </div>    
 
 
  
    
    <br>
    <div style="overflow-x:auto;">
  <TABLE class="table table-bordred table-striped" WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   <thead>
        <tr>
        <th style="font-size: smaller;"><font align="center">SNO</font></th>
        <th style="font-size: smaller;"><font align="left">EMPNO</font></th> 
        <th style="font-size: smaller;"><font align="left">NAME</font></th> 
        <th style="font-size: smaller;"><font align="left">EMPLOYEE LOGIN ID</font></th>  
        <th style="font-size: smaller;"><font align="left">EMPLOYEE TYPE</font></th>      
        <th style="font-size: smaller;"><font align="left">GENDER</font></th>
        <th style="font-size: smaller;"><font align="left">DOB</font></th>
        <th style="font-size: smaller;"><font align="left">PHONE</font></th>
        <th style="font-size: smaller;"><font align="left">EMAIL</font></th>
        <th style="font-size: smaller;"><font align="left">PASSPORT NUMBER</font></th>
        <th style="font-size: smaller;"><font align="left">COUNTRY OF ISSUE</font></th>
        <th style="font-size: smaller;"><font align="left">PASSPORT EXPIRYDATE</font></th>
        <th style="font-size: smaller;"><font align="left">COUNTRY</font></th>
		<th style="font-size: smaller;"><font align="left">UNITNO</font></th>
        <th style="font-size: smaller;"><font align="left">BUILDING</font></th>
        <th style="font-size: smaller;"><font align="left">STREET</font></th>
        <th style="font-size: smaller;"><font align="left">CITY</font></th>
        <th style="font-size: smaller;"><font align="left">ZIP</font></th>
        <th style="font-size: smaller;"><font align="left">DEPARTMENT</font></th>
        <th style="font-size: smaller;"><font align="left">DESIGNATION</font></th>
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
                <TD align="left"><%=StrUtils.fString((String)lineArr.get("EMPNO"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("FNAME"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("EMPUSERID"))%></TD>   
                 <TD align="left" ><%=StrUtils.fString((String)lineArr.get("EMPLOYEETYPEID"))%></TD>                
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("GENDER"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("DATEOFBIRTH"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("TELNO"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("EMAIL"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("PASSPORTNUMBER"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("COUNTRYOFISSUE"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("PASSPORTEXPIRYDATE"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("COUNTRY"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("ADDR1"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("ADDR2"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("ADDR3"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("ADDR4"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("ZIP"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("DEPTARTMENT"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("DESGINATION"))%></TD>
           </TR> 
       <%}%>
    </tbody>
    </TABLE>
    
      <table border="0" cellspacing="1" cellpadding = 2 align="center" bgcolor="">
             <%=fieldDesc%>
      </table>
   </div>
  
    <!-- <td>
      <input type="button" value="Back"   onclick="window.location.href='importExcelPage.jsp'" ></input>
    </td>  -->
  </form>
  </div>
  </div>
  </div>
  
  <% MLogger.log(-1, "_______________________ " + this.getClass() + ""); %>
  
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>