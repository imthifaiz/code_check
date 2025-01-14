<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.constants.IDBConstants"%>
<%@ page import="com.track.util.MLogger"%>
<%@ include file="header.jsp"%>
<%
String title = "Import Sales Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<!-- RESVI -->
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/fileshowcasedesign.css">
<!-- ENDS -->

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

    <script language="javascript">

    // Resvi starts
    function onDownloadOutboundTemplate()
    {
      var frmRoot=document.form1;
      frmRoot.action = "/track/OutboundOrderImportServlet?action=downloadOutboundTemplate" ;
      frmRoot.submit();
      return true;
    }    
//  Resvi Ends
    

function onExportExcel(){
    //  document.form1.excelexport.disabled = true;
      document.form1.action="exportCntSheet.jsp";
      document.form1.submit();
 }   

function onConfirm()
{
  var frmRoot=document.form1;
  frmRoot.action = "/track/OutboundOrderImportServlet?action=confirmCountSheet" ;
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
         
          frmRoot.action = "/track/OutboundOrderImportServlet?action=importCountSheet" + "&ImportFile=" + file +"&SheetName=" + sheet;
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
	document.form1.action  = "importOutboundOrderExcelSheet.jsp";
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
  
   session.removeAttribute("IMP_OUTBOUNDRESULT");
   session.removeAttribute("OUTBOUNDRESULT");
   ImportFile="";
   SheetName="";
   action="";
   
}

 ArrayList alRes = null;
 System.out.println("Action *** :" + action);

 fieldDesc=StrUtils.fString((String)session.getAttribute("OUTBOUNDRESULT"));

 if(null == session.getAttribute("IMP_OUTBOUNDRESULT"))
    {
      alRes = new ArrayList();
      buttonStat= "disabled";
   } else{
    
      alRes = (ArrayList)session.getAttribute("IMP_OUTBOUNDRESULT");
      if(alRes.size()==0)
      buttonStat= "disabled";
  }
  
 MLogger.log(1, "_______________________ " + this.getClass() + "" +msg);

     
   
%>
    <div class="container-fluid m-t-20">
	<div class="box"> 
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>      
                <li><a href="../salesorder/summary"><span class="underline-on-hover">Sales Order Summary</span> </a></li>              
                <li><label>Import Sales Order </label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onClick="window.location.href='../salesorder/summary'" > <!-- resvi -->
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
 <div class="box-body">
 <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data" action="/OrderImportServlet">
 <input type="hidden"  name="isDataPresent" value="true" ></input>
    <input type="hidden" value="" name="download"></input>
    
        <!--     RES START -->

     <div class="form-group" style="margin-bottom: 3%;">
     	<div class="col-sm-3">  
     		<button type="button" class="btn btn-success" style="margin-left: 14%;" onclick="javascript:return onDownloadOutboundTemplate();">Download Sales Template</button>
     	</div> 
     </div>
<!--      ENDS -->
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

  <div class="form-group">        
      	<div class="col-sm-12" align="center">
       	<button type="button" class="Submit btn btn-default" onclick="onClear();">Clear</button>&nbsp;
       	<button type="button" class="Submit btn btn-success" name="Submit" value="Confirm" onclick="javascript:return onConfirm();" <%=buttonStat%>>Confirm</button>&nbsp;
          </div>
          </div>
    <br>
    <div class="table-responsive">
  <TABLE class="table" WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
  <thead>
	 <tr>
         <th style="font-size: smaller;" width="3%"><font align="center">SNO</font></th>
         <th style="font-size: smaller;" width="5%"><font align="left">DONO</font></th> 
		 <th style="font-size: smaller;" width="7%"><font align="left">DOLNNO</font></th>
     	 <th style="font-size: smaller;" width="7%"><font align="left">RefNo</font></th>
     	 <th style="font-size: smaller;" width="7%"><font align="left">Order Type</font></th>
	 	 <th style="font-size: smaller;" width="7%"><font align="left">Order Date</font></th>
	  	 <th style="font-size: smaller;" width="7%"><font align="left">Order Time</font></th>
	 	 <th style="font-size: smaller;" width="7%"><font align="left">Remarks1</font></th>
	 	 <th style="font-size: smaller;" width="7%"><font align="left">Remarks2</font></th>
	 	 <th style="font-size: smaller;" width="7%"><font align="left">Sales VAT</font></th>
	 	 <th style="font-size: smaller;" width="10%"><font align="left">Employee Code</font></th>
	 	 <th style="font-size: smaller;" width="10"><font align="left">Customer Code</font></th> 
	 	 <th style="font-size: smaller;" width="7%"><font align="left">Product ID</font></th>
     	 <th style="font-size: smaller;" width="7%"><font align="left">Quantity</font></th>
	 	 <th style="font-size: smaller;" width="3%"><font align="left">UOM</font></th> 
	  	 <th style="font-size: smaller;" width="7%"><font align="left">Unit Price</font></th>
	  	 <th style="font-size: smaller;" width="10%"><font align="left">Sales Location</font></th>
         <th style="font-size: smaller;" width="7%"><font align="left">Currency ID</font></th>
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
             <TD align="left" width="10%"><%=(String)lineArr.get(IConstants.OUT_DONO)%></TD>
             <TD align="left" width="12%"><%=(String)lineArr.get(IConstants.OUT_DOLNNO)%></TD>
             <TD align="left" width="12%"><%=(String)lineArr.get(IConstants.OUT_REF_NO)%></TD>
             <TD align="left" width="12%"><%=(String)lineArr.get(IConstants.OUT_ORDERTYPE)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.OUT_COLLECTION_DATE)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.OUT_COLLECTION_TIME)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.OUT_REMARK1)%></TD>
			 <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.OUT_REMARK3)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.OUT_OUTBOUND_GST)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.DOHDR_EMPNO)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.OUT_CUST_CODE)%></TD>
             <TD align="left" width="5%"><%=(String)lineArr.get(IConstants.OUT_ITEM)%></TD>
             <TD align="left" width="6%"><%=(String)lineArr.get(IConstants.OUT_QTYOR)%></TD>
            <TD align="left" width="6%"><%=(String)lineArr.get(IConstants.OUT_UNITMO)%></TD> 
             <TD align="left" width="6%"><%=(String)lineArr.get(IConstants.OUT_UNITCOST)%></TD>
             <TD align="left" width="6%"><%=(String)lineArr.get(IConstants.SALES_LOCATION)%></TD>
              <TD align="left" width="6%"><%=(String)lineArr.get(IConstants.OUT_CURRENCYID)%></TD>
           </TR> 
       <%}%>
        
   </tbody>
    </TABLE>
    </div>
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
  