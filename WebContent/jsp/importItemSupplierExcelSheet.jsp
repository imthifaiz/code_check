<!-- BY : IMTHI
DATE : 31-01-2023
DESC : IMPORT ITEM SUPPLIER PAGES SCREEN -->

<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>

<%
String cmd =StrUtils.fString(request.getParameter("cmd"));
String title = "";
	title = "Import Product Assigned Supplier";
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


function onDownloadAddItemTemplate(){
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportItemServlet?action=downloadItemsupplierTemplate" ;
  frmRoot.submit();
  return true;
}    

function onConfirmItem(){
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportItemServlet?action=confirmItemSupplierCountSheet" ;
  frmRoot.submit();
  return true;
}

function onGoItem(){
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
	          frmRoot.action = "/track/ImportItemServlet?action=importItemSupplierCountSheet" + "&ImportFile=" + file +"&SheetName=" + sheet;
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
String ServerName 		  = StrUtils.fString(request.getRemoteHost()).trim();
String action         	  = StrUtils.fString(request.getParameter("action")).trim();
String ImportFile         = StrUtils.fString(request.getParameter("ImportFile")).trim();
String SheetName          = StrUtils.fString(request.getParameter("SheetName")).trim();
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
   session.removeAttribute("IMP_ITEMRESULT");
   session.removeAttribute("ITEMRESULT");
   ImportFile="";
   SheetName="";
   action="";
}

 ArrayList alRes = null;
 fieldDesc=StrUtils.fString((String)session.getAttribute("ITEMRESULT"));
 session.removeAttribute("ITEMRESULT");
 if(null == session.getAttribute("IMP_ITEMRESULT"))
    {
      alRes = new ArrayList();
      buttonStat= "disabled";
   } else{
      alRes = (ArrayList)session.getAttribute("IMP_ITEMRESULT");
      if(alRes.size()==0)
      buttonStat= "disabled";
  }
 MLogger.log(1, "_______________________ " + this.getClass() + "" +msg);
%>
  <div class="container-fluid m-t-20">
	 <div class="box"> 
            <ol class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                <li><a href="../product/summary"><span class="underline-on-hover">Product Summary</span> </a></li> 
                <li><label>Import Product Assigned Supplier</label></li>                                   
            </ol>             
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../product/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="/ImportItemServlet">
 <input type="hidden"  name="isDataPresent" value="true" ></input>
    <input type="hidden" value="" name="download"></input>
    <div class="form-group" style="margin-bottom: 3%;">
     	<div class="col-sm-3">  
     		<button type="button" class="btn btn-success" style="margin-left: 14%;" onclick="javascript:return onDownloadAddItemTemplate();">Download Product Assigned Supplier Template</button>
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
          <button type="button" class="Submit btn btn-success" name="Submit" value="Import" onclick="javascript:return onGoItem();">Import</button>&nbsp;&nbsp;
          </div>
          </div>
          </div>
          
 <div class="form-group">        
      	<div class="col-sm-12" align="center">
       		<button type="button" class="Submit btn btn-default" onclick="window.location.href='../product/additional?cmd=item'"> Clear</button>&nbsp;
       		<button type="button" class="Submit btn btn-success" name="Submit" value="Confirm" onclick="javascript:return onConfirmItem();" <%=buttonStat%>>Confirm</button>&nbsp;
          </div>
          </div>
<TABLE class="table table-bordred table-striped" WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   <thead>
	<tr>
		 <th style="font-size: smaller;" width="3%"><font align="center">SNO</font></th>
	     <th style="font-size: smaller;" width="10%"><font align="left">Product ID</font></th> 
		 <th style="font-size: smaller;" width="5%"><font align="left">Supplier 1</font></th>
		 <th style="font-size: smaller;" width="5%"><font align="left">Supplier 2</font></th>
		 <th style="font-size: smaller;" width="5%"><font align="left">Supplier 3</font></th> 
		 <th style="font-size: smaller;" width="5%"><font align="left">Supplier 4</font></th> 
		 <th style="font-size: smaller;" width="5%"><font align="left">Supplier 5</font></th>
		 <th style="font-size: smaller;" width="5%"><font align="left">Supplier 6</font></th>
		 <th style="font-size: smaller;" width="5%"><font align="left">Supplier 7</th>
		 <th style="font-size: smaller;" width="5%"><font align="left">Supplier 8</font></td>
		 <th style="font-size: smaller;" width="5%"><font align="left">Supplier 9</font></th>
		 <th style="font-size: smaller;" width="5%"><font align="left">Supplier 10</font></th>
     </tr>
     </thead>
	 <tbody>
       <%
          for (int iCnt =0; iCnt<alRes.size(); iCnt++){
          Map lineArr = (Map) alRes.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
       %>
           <TR>
             <TD align="left" width="3%"><%=iIndex%></TD>
             <TD align="left" width="10%"><%=(String)lineArr.get(IConstants.ITEM)%></TD>          
             <TD align="left" width="5%"><%=(String)lineArr.get("SUP1")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("SUP2")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("SUP3")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("SUP4")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("SUP5")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("SUP6")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("SUP7")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("SUP8")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("SUP9")%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get("SUP10")%></TD>
           </TR> 
       <%}%>
        
    </tbody>
    </TABLE>
      <table border="0" cellspacing="1" cellpadding = 2 align="center" bgcolor="">
             <%=fieldDesc%>
      </table>
  </form>
  </div>
  </div>
  </div>
  
  
  <% MLogger.log(-1, "_______________________ " + this.getClass() + ""); %>

  <jsp:include page="footer2.jsp" flush="true">
<jsp:param name="title" value="<%=title %>" />
</jsp:include>