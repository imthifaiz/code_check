<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>
<%
String title = "Import Inventory/Order Management Data";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script type="text/javascript" src="js/general.js"></script>

<script type="text/javascript">


function onImportTest()
{
  var frmRoot=document.form1;
  frmRoot.action = "importInvExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportInbound()
{
  var frmRoot=document.form1;
  frmRoot.action = "importInboundOrderExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportOutbound()
{
  var frmRoot=document.form1;
  frmRoot.action = "importOutboundOrderExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportTransfer()
{
  var frmRoot=document.form1;
  frmRoot.action = "importTransferOrderExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}


function onImportEstimate()
{
  var frmRoot=document.form1;
  frmRoot.action = "importEstimateOrderExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}
function onImportKitting()
{
  var frmRoot=document.form1;
  frmRoot.action = "importKittingExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}


function onImportOutboundProdRemarks()
{
	var frmRoot=document.form1;
	frmRoot.action = "importOutboundProductRemarksExcelSheet.jsp?action=new" ;
	frmRoot.submit();
	return true;
}
function onImportInboundProdRemarks()
{
	var frmRoot=document.form1;
	frmRoot.action = "importInboundProductRemarksExcelSheet.jsp?action=new" ;
	frmRoot.submit();
	return true;
}

function onImportStockTake()
{
  var frmRoot=document.form1;
  frmRoot.action = "ImportStockTake.jsp?action=new" ;
  frmRoot.submit();
  return true;
}


function onDownloadInvTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportInvServlet?action=downloadInvTemplate" ;
  frmRoot.submit();
  return true;
}


function onDownloadInboundTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/OrderImportServlet?action=downloadInboundTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadOutboundTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/OutboundOrderImportServlet?action=downloadOutboundTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadTransferTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/TransferOrderImportServlet?action=downloadTransferTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadEstimateTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/OutboundOrderImportServlet?action=downloadEstimateTemplate" ;
  frmRoot.submit();
  return true;
}
function onDownloadKittingTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadKittingTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadOutboundProdRemarksTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/OutboundOrderImportServlet?action=downloadOutboundProdRemarksTemplate" ;
  frmRoot.submit();
  return true;
}
function onDownloadInboundProdRemarksTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/OrderImportServlet?action=downloadInboundProdRemarksTemplate" ;
  frmRoot.submit();
  return true;
}

function onDownloadStockTakeTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportServlet?action=downloadStockTakeTemplate" ;
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
 

 
 <div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Import Inventory Master"    onclick="javascript:return onImportTest();"><b>Import Inventory Master</b></button>&nbsp;&nbsp;
          </div>
 </div>
 
 <div class="form-group">        
      <div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link" name="Submit" value="Download Inventory Master"    onclick="javascript:return onDownloadInvTemplate();"><b>Download Inventory Master</b></button>&nbsp;&nbsp;
      </div> 

 </div>
 

 <br>
  <div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" type="button" name="Submit" value="Import Purchase Order"    onclick="javascript:return onImportInbound();"><b>Import Purchase Order</b></button>&nbsp;&nbsp;
          </div>  
<div class="form-inline">
   <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Import Sales Order"    onclick="javascript:return onImportOutbound();"><b>Import Sales Order</b></button>&nbsp;&nbsp;
       </div> 
         </div>
           </div>
<div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Download Purchase Order Template"    onclick="javascript:return onDownloadInboundTemplate();"><b>Download Purchase Order Template</b></button>&nbsp;&nbsp;
      </div>
<div class="form-inline">
   <div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link"  name="Submit" value="Download Sales Order Template"    onclick="javascript:return onDownloadOutboundTemplate();"><b>Download Sales Order Template</b></button>&nbsp;&nbsp;
       </div> 
         </div>
           </div>
  <br>
  <div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Import Purchase Product Remarks"    onclick="javascript:return onImportInboundProdRemarks();"><b>Import Purchase Product Remarks</b></button>&nbsp;&nbsp;
          </div>
<div class="form-inline">
   <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Import Sales Product Remarks"    onclick="javascript:return onImportOutboundProdRemarks();"><b>Import Sales Product Remarks</b></button>&nbsp;&nbsp;
       </div> 
         </div>
            </div>
 <div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Download Purchase Product Remarks Template"    onclick="javascript:return onDownloadInboundProdRemarksTemplate();"><b>Download Purchase Product Remarks Template</b></button>&nbsp;&nbsp;
         </div>
<div class="form-inline">
   <div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link"  name="Submit" value="Download Sales Product Remarks Template"    onclick="javascript:return onDownloadOutboundProdRemarksTemplate();"><b>Download Sales Product Remarks Template</b></button>&nbsp;&nbsp;
       </div> 
         </div>
          </div>
 
 <br>
  <div class="form-group">        
      <div class="col-sm-6" align="center">
         <button type="button" class="Submit btn btn-link" name="Submit" value="Import Sales Estimate Order"    onclick="javascript:return onImportEstimate();"><b>Import Sales Estimate Order</b></button>&nbsp;&nbsp;
      </div>
<div class="form-inline">
   <div class="col-sm-6" align="center">
      <button type="button" class="Submit btn btn-link" name="Submit" value="Import Transfer Order"    onclick="javascript:return onImportTransfer();"><b>Import Consignment Order</b></button>&nbsp;&nbsp;
    </div>
   </div>
 </div>
 <div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Download Sales Estimate Order Template"    onclick="javascript:return onDownloadEstimateTemplate();"><b>Download Sales Estimate Order Template</b></button>&nbsp;&nbsp;
          </div>
<div class="form-inline">
   <div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link"  name="Submit" value="Download Transfer Order Template"    onclick="javascript:return onDownloadTransferTemplate();"><b>Download Consignment Order Template</b></button>&nbsp;&nbsp;
       </div> 
          </div>
             </div>
 
 <br>
 
 <div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Import Kitting"    onclick="javascript:return onImportKitting();"><b>Import Kitting</b></button>&nbsp;&nbsp;
          </div>
<div class="form-inline">
   <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" type="button" name="Submit" value="Import Stock Take"    onclick="javascript:return onImportStockTake();"><b>Import Stock Take</b></button>&nbsp;&nbsp;
       </div>  
         </div>
            </div>
 <div class="form-group">        
      <div class="col-sm-6" align="center">  
      <button type="button" class="Submit btn btn-link" name="Submit" value="Download Kitting Template"    onclick="javascript:return onDownloadKittingTemplate();"><b>Download Kitting Template</b></button>&nbsp;&nbsp;
      </div>
 <div class="form-inline">
   <div class="col-sm-6" align="center">  
      <button type="button" class="Submit btn btn-link" name="Submit" value="Download Stock Take Template"    onclick="javascript:return onDownloadStockTakeTemplate();"><b>Download Stock Take Template</b></button>&nbsp;&nbsp;
      </div>
         </div>
            </div>
         
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