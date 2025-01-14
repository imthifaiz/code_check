<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.MLogger"%>
<%@page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp"%>

<%
String title = "Import Location Master";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<script language="javascript">

function onDownloadLocTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadLocTemplate" ;
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
  frmRoot.action = "/track/ImportServlet?action=confirmLocCountSheet" ;
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
          if(frmRoot.TruncateStatus.checked){
          trunkateData = frmRoot.TruncateStatus.value;
          }
          frmRoot.action = "/track/ImportServlet?action=importLocCountSheet" + "&ImportFile=" + file +"&SheetName=" + sheet +"&Truncate=" + trunkateData;
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
//CountSheetDAO dao = new CountSheetDAO();



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

//isExistsCountSheetData= dao.isExisit(ht,"");

if(action.equalsIgnoreCase("new")){
  
   session.removeAttribute("IMP_RESULT");
   session.removeAttribute("RESULT");
   ImportFile="";
   SheetName="";
   action="";
   
}

 ArrayList alRes = null;
 System.out.println("Action *** :" + action);

 fieldDesc=StrUtils.fString((String)session.getAttribute("RESULT"));

 if(null == session.getAttribute("IMP_RESULT"))
    {
      alRes = new ArrayList();
      buttonStat= "disabled";
   } else{
    
      alRes = (ArrayList)session.getAttribute("IMP_RESULT");
      if(alRes.size()==0)
      buttonStat= "disabled";
  }
  
 MLogger.log(1, "_______________________ " + this.getClass() + "" +msg);

     
   
%>
  <div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                <li><a href="../location/summary"><span class="underline-on-hover">Location Summary</span> </a></li>               
                <li>Import Location Master</li>                                   
            </ol>             
    <!-- Thanzith Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../location/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="/ImportServlet">
 <input type="hidden"  name="isDataPresent" value="true" ></input>
    <input type="hidden" value="" name="download"></input>
    <div class="form-group" style="margin-bottom: 3%;">
     	<div class="col-sm-3">  
     		<button type="button" class="btn btn-success" style="margin-left: 14%;" onclick="javascript:return onDownloadLocTemplate();">Download Location Template</button>
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
          
    <div class="row">
  		<div class="col-12 col-sm-12">
  		<p><INPUT Type=Checkbox class="form-check-input"  style="border:0;"  name="TruncateStatus" value="Y">
        <strong> &nbsp;Truncate exceeded data for the following:
                     <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Location Description (Max 100 Characters)
                     <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Remarks (Max 100 Characters)</strong> 
         </div>
            </div>
    
 <div class="form-group">        
      	<div class="col-sm-12" align="center">
       	<button type="button" class="Submit btn btn-default" onclick="window.location.href='../location/import'">Clear</button>&nbsp;
       	<button type="button" class="Submit btn btn-success" name="Submit" value="Confirm" onclick="javascript:return onConfirm();" <%=buttonStat%>>Confirm</button>&nbsp;
          </div>
          </div>
                 
                 
  
    <br>
  <TABLE class="table table-bordred table-striped" WIDTH="100%" border="0" cellspacing="1" cellpadding = 2 align = "center">
   <thead>
	    <tr>
          <th style="font-size: smaller;" width="2%"><font align="center">SNO</font></th>
          <th style="font-size: smaller;" width="4%"><font align="left">PLANT</font></th> 
          <th style="font-size: smaller;" width="5%"><font align="left">LOC</font></th> 
          <th style="font-size: smaller;" width="10%"><font align="left">LOCDESC</font></th>
          <th style="font-size: smaller;" width="5%"><font align="left">LOCTYPE ONE</font></th>
          <th style="font-size: smaller;" width="5%"><font align="left">LOCTYPE TWO</font></th>
          <th style="font-size: smaller;" width="5%"><font align="left">LOCTYPE THREE</font></th>
          <th style="font-size: smaller;" width="10%"><font align="left">REMARKS</font></th>
          <th style="font-size: smaller;" width="5%"><font align="left">COMNAME</font></th>
          <th style="font-size: smaller;" width="4%"><font align="left">RCBNO</font></th>
		  <th style="font-size: smaller;" width="10%"><font align="left">ADD1</font></th> 
          <th style="font-size: smaller;" width="10%"><font align="left">ADD2</font></th> 
          <th style="font-size: smaller;" width="10%"><font align="left">ADD3</font></th>
          <th style="font-size: smaller;" width="10%"><font align="left">ADD4</font></th>
          <th style="font-size: smaller;" width="8%"><font align="left">STATE</font></th>
          <th style="font-size: smaller;" width="8%"><font align="left">COUNTRY</font></th>
		  <th style="font-size: smaller;" width="5%"><font align="left">ZIP</font></th>
          <th style="font-size: smaller;" width="5%"><font align="left">TELNO</font></th>
          <th style="font-size: smaller;" width="5%"><font align="left">FAX</font></th>
		  <th style="font-size: smaller;" width="2%"><font align="left">ISACTIVE</font></th>
	 </tr>
	 </thead>
	 <tbody>
       <%
          for (int iCnt =0; iCnt<alRes.size()-1; iCnt++){
          Map lineArr = (Map) alRes.get(iCnt);
         
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
         
      
       %>
           <TR>
             <TD align="left" width="2%"><%= iIndex %></TD>
             <TD align="left" width="4%"><%=(String)lineArr.get("PLANT")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("LOC")%></TD>
             <TD align="left" width="10%"><%=(String)lineArr.get("LOCDESC")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("LOC_TYPE_ID")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("LOC_TYPE_ID2")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("LOC_TYPE_ID3")%></TD>
             <TD align="left" width="10%"><%=(String)lineArr.get("REMARKS")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("COMNAME")%></TD>
             <TD align="left" width="4%"><%=(String)lineArr.get("RCBNO")%></TD>
             <TD align="left" width="10%"><%=(String)lineArr.get("ADD1")%></TD>
             <TD align="left" width="10%"><%=(String)lineArr.get("ADD2")%></TD>
             <TD align="left" width="10%"><%=(String)lineArr.get("ADD3")%></TD>
             <TD align="left" width="10%"><%=(String)lineArr.get("ADD4")%></TD>
             <TD align="left" width="8%"><%=(String)lineArr.get("STATE")%></TD>
             <TD align="left" width="8%"><%=(String)lineArr.get("COUNTRY")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("ZIP")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("TELNO")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("FAX")%></TD>
        	 <TD align="left" width="2%"><%=(String)lineArr.get("ISACTIVE")%></TD>
          </TR> 
       <%}%>
        <% if(alRes.size()>0){%>
       <% Map linemap =(Map)alRes.get(alRes.size()-1); %>
       <TR bgcolor = "">
             <TD align="left" width="2%"><%= alRes.size()%></TD>
              <TD align="left" width="4%"><%=(String)linemap.get("PLANT")%></TD>
             <TD align="left" width="5%"><%=(String)linemap.get("LOC")%></TD>
             <TD align="left" width="10%"><%=(String)linemap.get("LOCDESC")%></TD>
             <TD align="left" width="5%"><%=(String)linemap.get("LOC_TYPE_ID")%></TD>
              <TD align="left" width="5%"><%=(String)linemap.get("LOC_TYPE_ID2")%></TD>
              <TD align="left" width="5%"><%=(String)linemap.get("LOC_TYPE_ID3")%></TD>
             <TD align="left" width="10%"><%=(String)linemap.get("REMARKS")%></TD>
             <TD align="left" width="5%"><%=(String)linemap.get("COMNAME")%></TD>
             <TD align="left" width="4%"><%=(String)linemap.get("RCBNO")%></TD>
             <TD align="left" width="10%"><%=(String)linemap.get("ADD1")%></TD>
             <TD align="left" width="10%"><%=(String)linemap.get("ADD2")%></TD>
             <TD align="left" width="10%"><%=(String)linemap.get("ADD3")%></TD>
             <TD align="left" width="10%"><%=(String)linemap.get("ADD4")%></TD>
             <TD align="left" width="8%"><%=(String)linemap.get("STATE")%></TD>
             <TD align="left" width="8%"><%=(String)linemap.get("COUNTRY")%></TD>
             <TD align="left" width="5%"><%=(String)linemap.get("ZIP")%></TD>
             <TD align="left" width="5%"><%=(String)linemap.get("TELNO")%></TD>
             <TD align="left" width="5%"><%=(String)linemap.get("FAX")%></TD>
       	     <TD align="left" width="2%"><%=(String)linemap.get("ISACTIVE")%></TD>
       </TR>
    <%} %>
    
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
