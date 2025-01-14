<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>
<%
String title = "Import Master Data";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>

<script type="text/javascript" src="js/general.js"></script>

<script type="text/javascript">

function onImport()
{
  var frmRoot=document.form1;
  frmRoot.action = "importExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}
function onImportItem()
{
  var frmRoot=document.form1;
  frmRoot.action = "importItemExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onAltrPrdImport()
{
  var frmRoot=document.form1;
  frmRoot.action = "importAltrPrdExcel.jsp?action=new" ;
  frmRoot.submit();
  return true;
}


function onSupImport()
{
  var frmRoot=document.form1;
  frmRoot.action = "importVendorExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onCustImport()
{
  var frmRoot=document.form1;
  frmRoot.action = "importCustomerExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}






function onImportTransferAssignee()
{
  var frmRoot=document.form1;
  frmRoot.action = "importTransferAssigneeExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportLoanAssignee() 
{
  var frmRoot=document.form1;
  frmRoot.action = "importLoanAssigneeExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportEmployee() 
{
  var frmRoot=document.form1;
  frmRoot.action = "importEmployeeExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportEmployeeLeavedet()
{
	  var frmRoot=document.form1;
	  frmRoot.action = "importEmployeeLeaveDetExcelSheet.jsp?action=new" ;
	  frmRoot.submit();
	  return true;
}
function onImportEmployeeSalarydet()
{
	  var frmRoot=document.form1;
	  frmRoot.action = "importEmployeeSalaryDetExcelSheet.jsp?action=new" ;
	  frmRoot.submit();
	  return true;
}
function onImportProdBOM()
{
  var frmRoot=document.form1;
  frmRoot.action = "importProdBomExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}



function onImportSupplierDiscount()
{
  var frmRoot=document.form1;
  frmRoot.action = "importSupplierDiscountExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportCustomerDiscount()
{
  var frmRoot=document.form1;
  frmRoot.action = "importCustomerDiscountExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportKitBOM()
{
	var frmRoot=document.form1;
	frmRoot.action = "importKitBomExcelSheet.jsp?action=new" ;
	frmRoot.submit();
	return true;
}




function onDownloadLocTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadLocTemplate" ;
  frmRoot.submit();
  return true;
}


function onDownloadItemTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportItemServlet?action=downloadItemTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadAltrPrdTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportItemServlet?action=downloadAltPrdTemplate" ;
  frmRoot.submit();
  return true;
}
function onDownloadSupTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadSupplierTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadCustTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadCustomerTemplate" ;
  frmRoot.submit();
  return true;
}








function onDownloadTransferAssigneeTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadTransferAssigneeTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadLoanAssigneeTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadLoanAssigneeTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadEmployeeTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadEmployeeTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadProdBOMTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/WorkOrderImportServlet?action=downloadProdBOMTemplate" ;
  frmRoot.submit();
  return true;
}



function onDownloadSupplierDiscountTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadSupplierDiscountTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadCustomerDiscountTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadCustomerDiscountTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadKitBOMTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/WorkOrderImportServlet?action=downloadKitBOMTemplate" ;
  frmRoot.submit();
  return true;
}






function onReset()
{
	var frmRoot=document.form1;
 var con = confirm ("Stock Take/Cycle Count Data will be Permanently Removed Do You Want to Continue?");
      if(con) {
         
          frmRoot.action = "/track/ImportServlet?action=reset";
          frmRoot.submit();
          return true;
   }else
   {
         return false;
   }
 
}



  
</script>
    
  <%
  


MLogger logger = new MLogger();
String fieldDesc="";
String msg="";

String PLANT              = (String)session.getAttribute("PLANT");
String _login_user        = (String)session.getAttribute("LOGIN_USER");
String action         = StrUtils.fString(request.getParameter("action")).trim();



 ArrayList alRes = null;
 System.out.println("Action *** :" + action);

 fieldDesc=StrUtils.fString((String)session.getAttribute("RESULT"));
 session.setAttribute("RESULT", "");
   
%>
  <div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
 <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="/ImportServlet">
 <input type="hidden" value="" name="download"></input>
    <br>
 

<!--  <div class="form-group">        
      <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Import Product Master" onclick="javascript:return onImportItem();"><b>Import Product Master</b></button>&nbsp;&nbsp;
      </div>
 <div class="form-inline">
   <div class="col-sm-6" align="center">
    <button type="button" class="Submit btn btn-link" name="Submit" value="Import Location Master"    onclick="javascript:return onImport();"><b>Import Location Master</b></button>&nbsp;&nbsp;
  </div> 
 </div>
 </div>
 
 <div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Download Product Template"   onclick="javascript:return onDownloadItemTemplate();"><b>Download Product Template</b></button>&nbsp;&nbsp;
      </div>
<div class="form-inline">
<div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link" name="Submit" value="Download Location Template"   onclick="javascript:return onDownloadLocTemplate();"><b>Download Location Template</b></button>&nbsp;&nbsp;
   </div>
 </div>
 </div>
 
 
 <br>
 <div class="col-sm-6" align="center">
 <div>
      <button type="button" class="Submit btn btn-link" name="Submit" value="Import Alternate Product"    onclick="javascript:return onAltrPrdImport();"><b>Import Alternate Product</b></button>&nbsp;&nbsp;
  </div> 
 </div> 
 <div class="form-group">    
   <div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link" name="Submit" value="Download Alternate Product Template"    onclick="javascript:return onDownloadAltrPrdTemplate();"><b>Download Alternate Product Template</b></button>&nbsp;&nbsp;
      </div> 
 </div>
 
 <br>
 <div class="form-group">        
      <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Import Supplier Master"    onclick="javascript:return onSupImport();"><b>Import Supplier Master</b></button>&nbsp;&nbsp;
        </div>   
 <div class="form-inline">
   <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Import Customer Master "    onclick="javascript:return onCustImport();"><b>Import Customer Master</b></button>&nbsp;&nbsp;
       </div> 
        </div>
           </div>

<div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Download Supplier Template"    onclick="javascript:return onDownloadSupTemplate();"><b>Download Supplier Template</b></button>&nbsp;&nbsp;
          </div>   
<div class="form-inline">
   <div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link" name="Submit" value="Download Customer Template"    onclick="javascript:return onDownloadCustTemplate();"><b>Download Customer Template</b></button>&nbsp;&nbsp;
     </div> 
      </div>
         </div>
 <br>
 
 <div class="form-group">        
      <div class="col-sm-6" align="center">
          <button type="button" class="Submit btn btn-link" name="Submit" value="Import Supplier Discount Purchase Order "    onclick="javascript:return onImportSupplierDiscount();"><b>Import Supplier Discount Purchase Order</b></button>&nbsp;&nbsp;
           </div>
<div class="form-inline">
   <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Import Customer Discount Sales Order"    onclick="javascript:return onImportCustomerDiscount();"><b>Import Customer Discount Sales Order</b></button>&nbsp;&nbsp;
        </div> 
          </div>
            </div>
<div class="form-group">        
      <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Download Supplier Discount Purchase Order Template"    onclick="javascript:return onDownloadSupplierDiscountTemplate();"><b>Download Supplier Discount Purchase Order Template</b></button>&nbsp;&nbsp;
      </div> 
<div class="form-inline">
   <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Download Customer Discount Sales Order Template"    onclick="javascript:return onDownloadCustomerDiscountTemplate();"><b>Download Customer Discount Sales Order Template</b></button>&nbsp;&nbsp;
         </div> 
           </div>
             </div>
 
 
 
 <br>
 
<div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Import Transfer Order Customer Master"    onclick="javascript:return onImportTransferAssignee();"><b>Import Consignment Order Customer Master</b></button>&nbsp;&nbsp;
          </div>   
<div class="form-inline">
   <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Import Rental and Service Customer Master"    onclick="javascript:return onImportLoanAssignee();"><b>Import Rental and Service Customer Master</b></button>&nbsp;&nbsp;
       </div> 
         </div>
          </div>
 <div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Download Transfer Order Customer Template"    onclick="javascript:return onDownloadTransferAssigneeTemplate();"><b>Download Consignment Order Customer Template</b></button>&nbsp;&nbsp;
         </div>
 <div class="form-inline">
   <div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link" name="Submit" value="Download Rental and Service Customer Template"    onclick="javascript:return onDownloadLoanAssigneeTemplate();"><b>Download Rental and Service Customer Template</b></button>&nbsp;&nbsp;
     </div> 
        </div>
            </div>
 
 <br>
 
 <div class="form-group">        
      <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Import Kit BOM Master"    onclick="javascript:return onImportKitBOM();"><b>Import Kit BOM Master</b></button>&nbsp;&nbsp;
         <button type="button" class="Submit btn btn-link" name="Submit" value="Import Employee Master"    onclick="javascript:return onImportEmployee();"><b>Import Employee Master</b></button>&nbsp;&nbsp;
           </div>
<div class="form-inline">
      <div class="col-sm-6" align="center">
       
        </div> 
         </div>
          </div>
 <div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Download Employee Template"    onclick="javascript:return onDownloadEmployeeTemplate();"><b>Download Employee Template</b></button>&nbsp;&nbsp;
        <button type="button" class="Submit btn btn-link"  name="Submit" value="Download Kit BOM Master Template"    onclick="javascript:return onDownloadKitBOMTemplate();"><b>Download Kit BOM Master Template</b></button>&nbsp;&nbsp;
          </div>   
<div class="form-inline">
   <div class="col-sm-6" align="center">
     
     </div> 
      </div>
        </div>
  

 <br>
 
   <div class="form-group">        
      <div class="col-sm-6" align="center">
         <button type="button" class="Submit btn btn-link" name="Submit" value="Import Employee Leave Type Master"    onclick="javascript:return onImportEmployeeLeavedet();"><b>Import Employee Leave Type Master</b></button>&nbsp;&nbsp;
           </div>
           
          </div>
          
          <div class="form-group">
   <div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link" name="Submit" value="Import Employee Salary Type Master"    onclick="javascript:return onImportEmployeeSalarydet();"><b>Import Employee Salary Type Master</b></button>&nbsp;&nbsp;
     </div> 
      </div> -->
          
 <br>
 
 <div class="form-group">        
      <div class="col-sm-12" align="center"> 
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; 
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      </div>
        </div>
 </form>
 </div>
 </div>
 </div>
 
  <% MLogger.log(-1, "_______________________ " + this.getClass() + ""); %>
  
  <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>