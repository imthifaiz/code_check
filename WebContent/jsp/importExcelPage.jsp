<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>
<%
String title = "Import Data Templates";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<style type="text/css">.backpageul
{
	background-color: rgb(255, 255, 255);
    padding: 0px 10px;
    margin-bottom: 0px;
    margin-top: 15px;
}
.underline-on-hover:hover {
  		text-decoration: underline;
	}</style>

<script type="text/javascript" src="../jsp/js/general.js"></script>

<script type="text/javascript">

function popUpWin(URL) {
	 subWin = window.open(encodeURI(URL), 'IMPORT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}

function onImportTest()
{
  var frmRoot=document.form1;
  frmRoot.action = "../import/inventoryexcel" ;
  frmRoot.submit();
  return true;
}

function onImportDisplay()
{
  var frmRoot=document.form1;
  frmRoot.action = "../import/orderdisplaymaxqty" ;
  frmRoot.submit();
  return true;
}

function onImportRental()
{
  var frmRoot=document.form1;
  frmRoot.action = "../jsp/importloanorderexcelsheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportInbound()
{
  var frmRoot=document.form1;
  frmRoot.action = "../jsp/importInboundOrderExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportOutbound()
{
  var frmRoot=document.form1;
  frmRoot.action = "../jsp/importOutboundOrderExcelSheet.jsp?action=new" ;
  frmRoot.submit();
  return true;
}

function onImportTransfer()
{
  var frmRoot=document.form1;
  frmRoot.action = "../jsp/importTransferOrderExcelSheet.jsp?action=new" ;
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
// function onImportKitting()
// {
//   var frmRoot=document.form1;
//   frmRoot.action = "importKittingExcelSheet.jsp?action=new" ;
//   frmRoot.submit();
//   return true;
// }


function onImportOutboundProdRemarks()
{
	var frmRoot=document.form1;
	frmRoot.action = "../jsp/importOutboundProductRemarksExcelSheet.jsp?action=new" ;
	frmRoot.submit();
	return true;
}
function onImportInboundProdRemarks()
{
	var frmRoot=document.form1;
	frmRoot.action = "../jsp/importInboundProductRemarksExcelSheet.jsp?action=new" ;
	frmRoot.submit();
	return true;
}

function onImportStockTake()
{
  var frmRoot=document.form1;
  frmRoot.action = "../import/stocktake" ;
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
function onDownloadInvDisplay()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/ImportInvServlet?action=downloadInvDisplay" ;
  frmRoot.submit();
  return true;
}


function onDownloadRentalTemplate()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/OrderImportServlet?action=downloadRentalTemplate" ;
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
<!-- resvi -->
  <div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li>Import Data Templates</li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
<!-- ends -->
 <div class="box-body">
 
 <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="/ImportServlet">
 <input type="hidden" value="" name="download"></input>
    <br>
 

 
 <div class="form-group">        
      <div class="col-sm-6" align="center">
        <button type="button" class="Submit btn btn-link" name="Submit" value="Import Inventory Master"    onclick="javascript:return onImportTest();"><b>Import Inventory Master</b></button>&nbsp;&nbsp;
          </div>
           <div class="form-inline">        
      <div class="col-sm-6" align="center">
        <!-- <button type="button" class="Submit btn btn-link" type="button" name="Submit" value="Import Stock Take"    onclick="javascript:return onImportStockTake();"><b>Import Stock Take</b></button>&nbsp;&nbsp; -->
     	<button type="button" class="Submit btn btn-link" name="Submit" value="Import Order Display/Max Quantity"    onclick="javascript:return onImportDisplay();"><b>Import Order Display/Max Quantity</b></button>&nbsp;&nbsp;
          </div>
 </div>
 </div>
 
 <div class="form-group">        
      <div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link" name="Submit" value="Download Inventory Master"    onclick="javascript:return onDownloadInvTemplate();"><b>Download Inventory Master</b></button>&nbsp;&nbsp;
      </div>
      <div class="form-inline">        
      <div class="col-sm-6" align="center">
     <!-- <button type="button" class="Submit btn btn-link" name="Submit" value="Download Stock Take Template"    onclick="javascript:return onDownloadStockTakeTemplate();"><b>Download Stock Take Template</b></button>&nbsp;&nbsp;
     <br>
     <div class="Submit btn btn-link" Style="font-weight: bold;" data-toggle="modal" data-target="#stockTakeModal"><a href="#"> Download Stock Take Template With Data</a></div>&nbsp;&nbsp; -->
     <button type="button" class="Submit btn btn-link" name="Submit" value="Download Order Display/Max Quantity"    onclick="javascript:return onDownloadInvDisplay();"><b>Download Order Display/Max Quantity</b></button>&nbsp;&nbsp;
      </div> 
 </div>
 
 	<!-- <div class="form-group"> 
 	<div class="col-sm-6" align="center">
    <button type="button" class="Submit btn btn-link" name="Submit" value="Import Order Display/Max Quantity"    onclick="javascript:return onImportDisplay();"><b>Import Order Display/Max Quantity</b></button>&nbsp;&nbsp;
    </div>
 	</div> -->
 	<!-- <div class="form-group"> 
  	<div class="col-sm-6" align="center">
     <button type="button" class="Submit btn btn-link" name="Submit" value="Download Order Display/Max Quantity"    onclick="javascript:return onDownloadInvDisplay();"><b>Download Order Display/Max Quantity</b></button>&nbsp;&nbsp;
     </div>
   </div> -->
      </div>
      
      
 
 

 <br>
<!--   <div class="form-group">         -->
<!--       <div class="col-sm-6" align="center"> -->
<!--         <button type="button" class="Submit btn btn-link" type="button" name="Submit" value="Import Purchase Order"    onclick="javascript:return onImportInbound();"><b>Import Purchase Order</b></button>&nbsp;&nbsp; -->
<!--           </div>   -->
<!-- <div class="form-inline"> -->
<!--    <div class="col-sm-6" align="center"> -->
<!--       <button type="button" class="Submit btn btn-link" name="Submit" value="Import Sales Order"    onclick="javascript:return onImportOutbound();"><b>Import Sales Order</b></button>&nbsp;&nbsp; -->
<!--        </div>  -->
<!--          </div> -->
<!--            </div> -->
<!-- <div class="form-group">         -->
<!--       <div class="col-sm-6" align="center"> -->
<!--         <button type="button" class="Submit btn btn-link" name="Submit" value="Download Purchase Order Template"    onclick="javascript:return onDownloadInboundTemplate();"><b>Download Purchase Order Template</b></button>&nbsp;&nbsp; -->
<!--       </div> -->
<!-- <div class="form-inline"> -->
<!--    <div class="col-sm-6" align="center"> -->
<!--      <button type="button" class="Submit btn btn-link"  name="Submit" value="Download Sales Order Template"    onclick="javascript:return onDownloadOutboundTemplate();"><b>Download Sales Order Template</b></button>&nbsp;&nbsp; -->
<!--        </div>  -->
<!--          </div> -->
<!--            </div> -->
<!--   <br> -->
<!--   <div class="form-group">         -->
<!--       <div class="col-sm-6" align="center"> -->
<!--         <button type="button" class="Submit btn btn-link" name="Submit" value="Import Purchase Product Remarks"    onclick="javascript:return onImportInboundProdRemarks();"><b>Import Purchase Product Remarks</b></button>&nbsp;&nbsp; -->
<!--           </div> -->
<!-- <div class="form-inline"> -->
<!--    <div class="col-sm-6" align="center"> -->
<!--       <button type="button" class="Submit btn btn-link" name="Submit" value="Import Sales Product Remarks"    onclick="javascript:return onImportOutboundProdRemarks();"><b>Import Sales Product Remarks</b></button>&nbsp;&nbsp; -->
<!--        </div>  -->
<!--          </div> -->
<!--             </div> -->
<!--  <div class="form-group">         -->
<!--       <div class="col-sm-6" align="center"> -->
<!--         <button type="button" class="Submit btn btn-link" name="Submit" value="Download Purchase Product Remarks Template"    onclick="javascript:return onDownloadInboundProdRemarksTemplate();"><b>Download Purchase Product Remarks Template</b></button>&nbsp;&nbsp; -->
<!--          </div> -->
<!-- <div class="form-inline"> -->
<!--    <div class="col-sm-6" align="center"> -->
<!--      <button type="button" class="Submit btn btn-link"  name="Submit" value="Download Sales Product Remarks Template"    onclick="javascript:return onDownloadOutboundProdRemarksTemplate();"><b>Download Sales Product Remarks Template</b></button>&nbsp;&nbsp; -->
<!--        </div>  -->
<!--          </div> -->
<!--           </div> -->
 
<!--  <br> -->
<!--   <div class="form-group">         -->
<!--       <div class="col-sm-6" align="center"> -->
<!--          <button type="button" class="Submit btn btn-link" name="Submit" value="Import Sales Estimate Order"    onclick="javascript:return onImportEstimate();"><b>Import Sales Estimate Order</b></button>&nbsp;&nbsp; -->
<!--       </div> -->
<!-- <div class="form-inline"> -->
<!--    <div class="col-sm-6" align="center"> -->
<!--       <button type="button" class="Submit btn btn-link" name="Submit" value="Import Transfer Order"    onclick="javascript:return onImportTransfer();"><b>Import Consignment Order</b></button>&nbsp;&nbsp; -->
<!--     </div> -->
<!--    </div> -->
<!--  </div> -->
<!--  <div class="form-group">         -->
<!--       <div class="col-sm-6" align="center"> -->
<!--         <button type="button" class="Submit btn btn-link" name="Submit" value="Download Sales Estimate Order Template"    onclick="javascript:return onDownloadEstimateTemplate();"><b>Download Sales Estimate Order Template</b></button>&nbsp;&nbsp; -->
<!--           </div> -->
<!-- <div class="form-inline"> -->
<!--    <div class="col-sm-6" align="center"> -->
<!--      <button type="button" class="Submit btn btn-link"  name="Submit" value="Download Transfer Order Template"    onclick="javascript:return onDownloadTransferTemplate();"><b>Download Consignment Order Template</b></button>&nbsp;&nbsp; -->
<!--        </div>  -->
<!--           </div> -->
             </div>
 
 <br>
 
<!--  <div class="form-group">         -->
<!--       <div class="col-sm-6" align="center"> -->
<!--         <button type="button" class="Submit btn btn-link" name="Submit" value="Import Kitting"    onclick="javascript:return onImportKitting();"><b>Import Kitting</b></button>&nbsp;&nbsp; -->
<!--           </div> -->
<!-- <div class="form-inline"> -->
<!--    <div class="col-sm-6" align="center"> -->
<!--    <button type="button" class="Submit btn btn-link" name="Submit" value="Import Inventory Master"    onclick="javascript:return onImportRental();"><b>Import Rental Order</b></button>&nbsp;&nbsp; -->
<!--       </div>   -->
<!--          </div> -->
<!--             </div> -->
<!--  <div class="form-group">         -->
<!--       <div class="col-sm-6" align="center">   -->
<!--       <button type="button" class="Submit btn btn-link" name="Submit" value="Download Kitting Template"    onclick="javascript:return onDownloadKittingTemplate();"><b>Download Kitting Template</b></button>&nbsp;&nbsp; -->
<!--       </div> -->
<!--  <div class="form-inline"> -->
<!--    <div class="col-sm-6" align="center">   -->
<!--    <button type="button" class="Submit btn btn-link" name="Submit" value="Download Inventory Master"    onclick="javascript:return onDownloadRentalTemplate();"><b>Download Rental Order Template</b></button>&nbsp;&nbsp; -->
<!--       </div> -->
<!--          </div> -->
<!--             </div> -->
         
        <br>
<!--  <div class="form-group">        
      <div class="col-sm-12" align="center"> 
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; 
      <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp;
      </div>
        </div> -->
        
 </form>
 </div>
 <%@include file="stockTakePopupModal.jsp" %>
	<!-- Modal -->
	<div id="myModal" class="modal fade" role="dialog">
	  <div class="modal-dialog">	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h3 class="modal-title">Modal Header</h3>
	      </div>
	      <div class="modal-body">
	        <p>Some text in the modal.</p>
	      </div>
	      <div class="modal-footer">
	      		<button id="btnBillOpen" type="button" class="btn btn-success">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div>
	    </div>
	  </div>
	</div>
 </div>
 </div>
 
  <% MLogger.log(-1, "_______________________ " + this.getClass() + ""); %>
  
  <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>