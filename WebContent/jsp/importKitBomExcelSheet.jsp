<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>
<%
String title = "Import Kit BOM Master";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
 <jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/fileshowcasedesign.css">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

    <script language="javascript">

    function onDownloadKitBOMTemplate()
    {
      var frmRoot=document.form1;
      frmRoot.action = "/track/WorkOrderImportServlet?action=downloadKitBOMTemplate" ;
      frmRoot.submit();
      return true;
    }
    
function onExportExcel(){
    //  document.form1.excelexport.disabled = true;
      document.form1.action="exportCntSheet.jsp";
      document.form1.submit();
 }   

function onConfirm()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/WorkOrderImportServlet?action=confirmKitBOMCountSheet" ;
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
         
          frmRoot.action = "/track/WorkOrderImportServlet?action=importKitBOMCountSheet" + "&ImportFile=" + file +"&SheetName=" + sheet;
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
	document.form1.action  = "importKitBomExcelSheet.jsp";
	document.form1.submit();
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
System.out.println("Setting plant value ");
//System.out.println ("PLANT : " + PLANT);

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
  
   session.removeAttribute("IMP_KITBOMRESULT");
   session.removeAttribute("KITBOMRESULT");
   ImportFile="";
   SheetName="";
   action="";
   
}

 ArrayList alRes = null;
 System.out.println("Action *** :" + action);

 fieldDesc=StrUtils.fString((String)session.getAttribute("KITBOMRESULT"));

 if(null == session.getAttribute("IMP_KITBOMRESULT"))
    {
      alRes = new ArrayList();
      buttonStat= "disabled";
   } else{
    
      alRes = (ArrayList)session.getAttribute("IMP_KITBOMRESULT");
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
				onclick="window.location.href='view_kitting.jsp'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="/WorkOrderImportServlet">
 <input type="hidden"  name="isDataPresent" value="true" ></input>
    <input type="hidden" value="" name="download"></input>
    <div class="form-group" style="margin-bottom: 3%;">
     	<div class="col-sm-3">  
     		<button type="button" class="btn btn-success" style="margin-left: 14%;" onclick="javascript:return onDownloadKitBOMTemplate();">Download Kit BOM Template</button>
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
          <button type="button" class="Submit btn btn-default" name="Submit" value="Import" onclick="javascript:return onGo();">Import</button>&nbsp;&nbsp;
          </div>
          </div>
          </div>
 
 <div class="form-group">        
      	<div class="col-sm-12" align="center">
       	<button type="button" class="Submit btn btn-default" onclick="onClear();">Clear</button>&nbsp;
       	<button type="button" class="Submit btn btn-success" name="Submit" value="Confirm" onclick="javascript:return onConfirm();" <%=buttonStat%>>Confirm</button>&nbsp;
          </div>
          </div>
 
 
  
    
    <br>
  <TABLE class="table table-bordred table-striped" WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
  <thead>
	 <tr>
          <th style="font-size: smaller;" width="3%"><font  align="center">SNO</font></th>
          <th style="font-size: smaller;" width="20%"><font align="left">Parent Product</font></th> 
	      <th style="font-size: smaller;" width="20%"><font align="left">Child Product</font></th>
	      <th style="font-size: smaller;" width="20%"><font align="left">Equivalent Product</font></th>
          <th style="font-size: smaller;" width="17%"><font align="left">Child Qty</font></th>
     
	 
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
               <TD align="left" width="3%"><%=iIndex%></TD>
               <TD align="left" width="20%"><%=(String)lineArr.get(IDBConstants.PARENTITEM)%></TD>
               <TD align="left" width="20%"><%=(String)lineArr.get(IDBConstants.CHILDITEM)%></TD>
               <TD align="left" width="20%"><%=(String)lineArr.get(IDBConstants.EQUIVALENTITEM)%></TD>
               <TD align="left" width="17%"><%=(String)lineArr.get(IDBConstants.QTY)%></TD>
             </TR> 
       <%}%>
        
    </tbody>
    </TABLE>
    
      <table border="0" cellspacing="1" cellpadding = 2 align="center" bgcolor="">
             <%=fieldDesc%>
      </table>
    <!-- <td>
      <input type="button" value="Back"   onclick="window.location.href='importExcelPage.jsp'" ></input>
    </td> --> 
  
  </form>
  </div>
  </div>
  </div>
  
  <% MLogger.log(-1, "_______________________ " + this.getClass() + ""); %>
  
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>