<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>


<%
String title = "Import Product Master";
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

    function onDownloadItemTemplate()
    {
      var frmRoot=document.form1;
      frmRoot.action = "/track/ImportItemServlet?action=downloadItemTemplate" ;
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
  frmRoot.action = "/track/ImportItemServlet?action=confirmCountSheet" ;
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
      
      
    /*   if(frmRoot.isDataPresent.value=="true"){
         var isDataPresentcon = confirm ("Data Already Exists,Use Reset StkTake option to Clear the Data and then Import CountSheet");
          if(isDataPresentcon) {
          return false;
          }else{
           return false;
          }
          
       }*/
      var con = confirm ("Process will take few minutes  to download");
      if(con) {
         
          var file=frmRoot.ImportFile.value;
          var sheet=frmRoot.SheetName.value;
          var trunkateData ="";
          if(frmRoot.TruncateStatus.checked){
          trunkateData = frmRoot.TruncateStatus.value;
          }
         
         
          frmRoot.action = "/track/ImportItemServlet?action=importCountSheet" + "&ImportFile=" + file +"&SheetName=" + sheet +"&Truncate=" + trunkateData;
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
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li> 
                <li><a href="../product/summary"><span class="underline-on-hover">Product Summary</span> </a></li>               
                <li><label>Import Product Master</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
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
     		<button type="button" class="btn btn-success" style="margin-left: 14%;" onclick="javascript:return onDownloadItemTemplate();">Download Product Template</button>
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
      <!-- <input name="SheetName" type="text" value="<%=SheetName%>" size="20" maxlength="20"></input> -->         
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
                     <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Product Description,Detail Description (Max 100 Characters)
                     <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Manufacture,Remarks3 (Max 100 Characters)
                     <br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Remarks4 (Max 200 Characters)</strong> 
         </div>
            </div>
          
 <div class="form-group">        
      	<div class="col-sm-12" align="center">
       	<button type="button" class="Submit btn btn-default" onclick="window.location.href='../product/u-cloproduct'"> Clear</button>&nbsp;
       	<button type="button" class="Submit btn btn-success" name="Submit" value="Confirm" onclick="javascript:return onConfirm();" <%=buttonStat%>>Confirm</button>&nbsp;
          </div>
          </div>
          
  
    
   
  <TABLE class="table table-bordred table-striped" WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
   <thead>
<tr>
     <th style="font-size: smaller;" width="3%"><font align="center">SNO</font></th>
     <th style="font-size: smaller;" width="10%"><font align="left">Product ID</font></th> 
	 <th style="font-size: smaller;" width="10%"><font align="left">Product Description</font></th>
	 <th style="font-size: smaller;" width="3%"><font align="left">Base UOM</font></th>
	 <th style="font-size: smaller;" width="9%"><font align="left">Product Department</font></th> 
	 <th style="font-size: smaller;" width="9%"><font align="left">Product Category</font></th> 
	 <th style="font-size: smaller;" width="5%"><font align="left">Product Sub Category</font></th>
	 <th style="font-size: smaller;" width="5%"><font align="left">Product Brand</font></th>
	 <th style="font-size: smaller;" width="5%"><font align="left">Cost</th>
	 <th style="font-size: smaller;" width="5%"><font align="left">Price</font></td>
	 <th style="font-size: smaller;" width="10%"><font align="left">Min.Selling Price</font></th>
	 <!-- <th width="5%"><font align="left">Discount(%)</font></th> -->
	 <th style="font-size: smaller;" width="9%"><font align="left">Min Stock Qty</font></th>
	 <th style="font-size: smaller;" width="9%"><font align="left">Max Stock Qty</font></th>
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
             <TD align="left" width="12%"><%=(String)lineArr.get(IConstants.ITEM_DESC)%></TD>
             <TD align="left" width="6%"><%=(String)lineArr.get(IConstants.STKUOM)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IDBConstants.PRDDEPTID)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IDBConstants.PRDCLSID)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IDBConstants.ITEMMST_ITEM_TYPE)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IDBConstants.PRDBRANDID)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.COST)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.PRICE)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.MIN_S_PRICE)%></TD>
            <%--  <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.DISCOUNT)%></TD> --%>
             <TD align="left" width="6%"><%=(String)lineArr.get(IConstants.STKQTY)%></TD>
             <TD align="left" width="6%"><%=(String)lineArr.get(IConstants.MAXSTKQTY)%></TD>
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