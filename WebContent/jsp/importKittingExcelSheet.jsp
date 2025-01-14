<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>

<%
String title = "Import Kitting";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

    <script language="javascript">

function onExportExcel(){
    //  document.form1.excelexport.disabled = true;
      document.form1.action="exportCntSheet.jsp";
      document.form1.submit();
 }   

function onConfirm()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=confirmKittingCountSheet" ;
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
         
          frmRoot.action = "/track/ImportServlet?action=importKittingCountSheet" + "&ImportFile=" + file +"&SheetName=" + sheet;
          frmRoot.submit();
          return true;
   }else
   {
         return false;
   }
  
}
  
 
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
  
   session.removeAttribute("IMP_KITTINGRESULT");
   session.removeAttribute("KITTINGRESULT");
   ImportFile="";
   SheetName="";
   action="";
   
}

 ArrayList alRes = null;
 System.out.println("Action *** :" + action);

 fieldDesc=StrUtils.fString((String)session.getAttribute("KITTINGRESULT"));

 if(null == session.getAttribute("IMP_KITTINGRESULT"))
    {
      alRes = new ArrayList();
      buttonStat= "disabled";
   } else{
    
      alRes = (ArrayList)session.getAttribute("IMP_KITTINGRESULT");
      if(alRes.size()==0)
      buttonStat= "disabled";
  }
  
 MLogger.log(1, "_______________________ " + this.getClass() + "" +msg);

     
   
%>
  <div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="/OrderImportServlet">
 <input type="hidden"  name="isDataPresent" value="true" ></input>
  <input type="hidden" value="" name="download"></input>
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
          <button type="button" class="Submit btn btn-default" name="Submit" value="Import" onclick="javascript:return onGo();"><b>Import</b></button>&nbsp;&nbsp;
          </div>
          </div>
          </div>
 
 <div class="form-group">        
      	<div class="col-sm-12" align="center">
       	<button type="button" class="Submit btn btn-default" value="Back"   onclick="window.location.href='importExcelPage.jsp'"><b>Back</b></button>&nbsp;
       	<button type="button" class="Submit btn btn-default" name="Submit" value="Confirm" onclick="javascript:return onConfirm();" <%=buttonStat%>><b>Confirm</b></button>&nbsp;
          </div>
          </div>
 
  
    
    <br>
  <TABLE class="table" WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
  <thead style="background: #eaeafa">
	 <tr>
         <th width="3%"><font align="center">SNO</font></th>
         <th width="10%"><font align="left">Parent Product ID</font></th> 
	     <th width="10%"><font align="left">Loc</font></th>
         <th width="10%"><font align="left">Parent Batch</font></th>
         <th width="10%"><font align="left">Child Product ID</font></th>
	     <th width="5%"><font align="left">Child Batch</font></th>
	     <th width="5%"><font align="left">Qty</font></th>
	     <th width="5%"><font align="left">Remarks</font></th>
	
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
             <TD align="left" width="10%"><%=(String)lineArr.get(IDBConstants.PARENTITEM)%></TD>
             <TD align="left" width="12%"><%=(String)lineArr.get(IDBConstants.LOC)%></TD>
             <TD align="left" width="12%"><%=(String)lineArr.get(IDBConstants.PARENT_BATCH)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IDBConstants.CHILDITEM)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IDBConstants.CHILD_BATCH)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IDBConstants.QTY)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IDBConstants.REMARKS)%></TD>
             
           </TR> 
       <%}%>
        
    </tbody>
    </TABLE>
    
      <table border="0" cellspacing="1" cellpadding = 2 align="center" bgcolor="">
             <%=fieldDesc%>
      </table>
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