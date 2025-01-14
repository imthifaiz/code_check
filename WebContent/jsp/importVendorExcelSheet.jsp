<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.MLogger"%>
<%@page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp"%>
<%
String title = "Import Supplier Master";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

    <script language="javascript">

    function onDownloadSupTemplate()
    {
      var frmRoot=document.form1;
      frmRoot.action = "/track/ImportServlet?action=downloadSupplierTemplate" ;
      frmRoot.submit();
      return true;
    }

function onConfirm()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=confirmSupplierCountSheet" ;
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
          frmRoot.action = "/track/ImportServlet?action=importSupplierCountSheet" + "&ImportFile=" + file +"&SheetName=" + sheet ;
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
  
   session.removeAttribute("IMP_SUPRESULT");
   session.removeAttribute("SUPRESULT");
   ImportFile="";
   SheetName="";
   action="";
   
}

 ArrayList alRes = null;
 System.out.println("Action *** :" + action);

 fieldDesc=StrUtils.fString((String)session.getAttribute("SUPRESULT"));
 session.removeAttribute("SUPRESULT");

 if(null == session.getAttribute("IMP_SUPRESULT"))
    {
      alRes = new ArrayList();
      buttonStat= "disabled";
   } else{
    
      alRes = (ArrayList)session.getAttribute("IMP_SUPRESULT");
      
      if(alRes.size()==0)
      buttonStat= "disabled";
  }
  
 MLogger.log(1, "_______________________ " + this.getClass() + "" +msg);

     
   
%>
  
  <div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../supplier/summary"><span class="underline-on-hover">Supplier Summary</span></a></li>	
                <li><label>Import Supplier Master</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../supplier/summary'" >
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="/ImportServlet">
 <input type="hidden"  name="isDataPresent" value="true" ></input>
    <input type="hidden" value="" name="download"></input>
    <div class="form-group" style="margin-bottom: 3%;">
     	<div class="col-sm-3">  
     		<button type="button" class="btn btn-success" style="margin-left: 14%;" onclick="javascript:return onDownloadSupTemplate();">Download Supplier Template</button>
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
       	<button type="button" class="Submit btn btn-default" onclick="window.location.href='../supplier/import'"> Clear</button>&nbsp;
       	<button type="button" class="Submit btn btn-success" name="Submit" value="Confirm" onclick="javascript:return onConfirm();" <%=buttonStat%>>Confirm</button>&nbsp;
          </div>
          </div>
                 
         
      
    
    <br>
        <div style="overflow-x:auto;">
  <TABLE class="table table-bordred table-striped" WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   <thead>
        <tr>
        <th style="font-size: smaller;"><font align="center">SNO</font></th>
        <th style="font-size: smaller;"><font align="left">Supplier ID</font></th> 
        <th style="font-size: smaller;"><font align="left">Supplier Name</font></th>
        <th style="font-size: smaller;"><font align="left">Supplier Currency</font></th>
         <th style="font-size: smaller;"><font align="left">Supplier Type</font></th>         
		 <th style="font-size: smaller;"><font align="left">Supplier Phone</font></th>
        <th style="font-size: smaller;"><font align="left">Supplier Fax</font></th>
        <th style="font-size: smaller;"><font align="left">Suppler Email</font></th>
        <th style="font-size: smaller;"><font align="left">Website</font></th>
        <th style="font-size: smaller;"><font align="left">Tax Treatment</font></th>
        <th style="font-size: smaller;"><font align="left">TRN/GST NO</font></th>
<!--         <th style="font-size: smaller;"><font align="left">Opening Balance</font></th> -->
        <th style="font-size: smaller;"><font align="left">Facebook Id</font></th>
     	<th style="font-size: smaller;"><font align="left">Twitter Id</font></th>
        <th style="font-size: smaller;"><font align="left">LinkedIn Id</font></th>
        <th style="font-size: smaller;"><font align="left">Skype Id</font></th>
        <th style="font-size: smaller;"><font align="left">Contact Person</font></th>
        <th style="font-size: smaller;"><font align="left">Designation</font></th>
        <th style="font-size: smaller;"><font align="left">Work Phone</font></th>
        <th style="font-size: smaller;"><font align="left">Mobile</font></th>
        <th style="font-size: smaller;"><font align="left">Email</font></th>
     	<th style="font-size: smaller;"><font align="left">Unitno</font></th>
        <th style="font-size: smaller;"><font align="left">Building</font></th>
        <th style="font-size: smaller;"><font align="left">Street</font></th>
        <th style="font-size: smaller;"><font align="left">City</font></th>
        <th style="font-size: smaller;"><font align="left">Postal Code</font></th>
        
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
                <TD align="left"><%=StrUtils.fString((String)lineArr.get("VENDNO"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("VNAME"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("CURRENCY_ID"))%></TD> 
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("SUPPLIER_TYPE_ID"))%></TD>                
				<TD align="left" ><%=StrUtils.fString((String)lineArr.get("TELNO"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("FAX"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("EMAIL"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("WEBSITE"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("TAXTREATMENT"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("RCBNO"))%></TD>
<%--                 <TD align="left" ><%=StrUtils.fString((String)lineArr.get("OPENINGBALANCE"))%></TD> --%>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("FACEBOOKID"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("TWITTERID"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("LINKEDINID"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("SKYPEID"))%></TD>                
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("NAME"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("DESGINATION"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("WORKPHONE"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("HPNO"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("CUSTOMEREMAIL"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("ADDR1"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("ADDR2"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("ADDR3"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("ADDR4"))%></TD>
                <TD align="left" ><%=StrUtils.fString((String)lineArr.get("ZIP"))%></TD>
                
                
                
                
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