<%@ page import="com.track.gates.DbBean"%>
<%@ page import=" com.track.dao.CustomerBeanDAO"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.io.*"%>
<%@ page import="java.lang.String.*"%>
<%@page import="java.math.BigDecimal" %>
<%@ include file="header.jsp"%>
<%
String title = "Rental Order Summary With Price";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
</jsp:include>
<style type="text/css">
.dt-button-collection.dropdown-menu{
left: 50px; !important;
}
</style>

<script language="javascript">
function popUpWin(URL) {
 subWin = window.open(URL, 'List', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
//---Modified by Deen on June 11 2014, Description:To open Rental And Service order summary  in excel powershell format
function ExportReport()
{
   var flag    = "false";
   var  DIRTYPE= document.form1.DIRTYPE.value;
  document.form1.FROM_DATE.value;
  document.form1.TO_DATE.value;
  document.form1.ITEM.value;
  document.form1.DONO.value;
  document.form1.JOBNO.value;
  document.form1.CUST_NAME.value;
  document.form1.action="/track/ReportServlet?action=ExportExcelLoanOrderSummaryWithPrice";
  document.form1.submit();
  
}
//---Modified by Deen on June 11 2014, Description:To open Rental And Service order summary  in excel powershell format

function onGo(){

   var flag    = "false";

   var FROM_DATE      = document.form1.FROM_DATE.value;
   var TO_DATE        = document.form1.TO_DATE.value;
   var DIRTYPE        = document.form1.DIRTYPE.value;
   var USER           = document.form1.CUST_NAME.value;
   var ITEMNO         = document.form1.ITEM.value;
   var ORDERNO        = document.form1.DONO.value;
   var JOBNO          = document.form1.JOBNO.value;
   var CUST_CODE      = document.form1.CUST_CODE.value;
   var PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
   var PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
   var PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}
     if(CUST_CODE != null     && CUST_CODE != "") { flag = true;}

     if(PRD_BRAND_ID != null     && PRD_BRAND_ID != "") { flag = true;}
     if(PRD_TYPE_ID != null     && PRD_TYPE_ID != "") { flag = true;}
     if(PRD_CLS_ID != null     && PRD_CLS_ID != "") { flag = true;}
    


  document.form1.action="loanOrderSummaryWithPrice.jsp";
  document.form1.submit();
}

</script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
LoanUtil loanUtil       = new LoanUtil();
loanUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList QryList  = new ArrayList();
Hashtable ht = new Hashtable();
CustomerBeanDAO _customerBeanDAO = new CustomerBeanDAO();
CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
session= request.getSession();
String plant = (String)session.getAttribute("PLANT");

String FROM_DATE ="",  TO_DATE = "",status="", DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",DONO="",CUST_NAME="",CUST_CODE="",EXPIREDATE="",PGaction="";
PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false",chkExpirydate="",strEmpty="";
String PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="";

FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));


if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();

String curDate =_dateUtils.getDate();

if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;


if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);


if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
JOBNO         = _strUtils.fString(request.getParameter("JOBNO"));
USER          = _strUtils.fString(request.getParameter("USER"));
ITEM          = _strUtils.fString(request.getParameter("ITEM"));
DESC          = _strUtils.fString(request.getParameter("DESC"));
DONO          = _strUtils.fString(request.getParameter("DONO"));
CUST_NAME     = _strUtils.fString(request.getParameter("CUST_NAME"));
CUST_CODE     = _strUtils.fString(request.getParameter("CUST_CODE"));
status 		  = _strUtils.fString(request.getParameter("STATUS"));
//Start code added by deen for product brand,type on 2/sep/13
PRD_TYPE_ID = _strUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID = _strUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_CLS_ID = _strUtils.fString(request.getParameter("PRD_CLS_ID"));
//End code added by deen for product brand,type on 2/sep/13

if(DIRTYPE.length()<=0){
DIRTYPE = "LOANORDER";
}

if(PGaction.equalsIgnoreCase("View")){
 
 try{
        //Hashtable ht = new Hashtable();
       
       if(DIRTYPE.equalsIgnoreCase("LOANORDER"))
       {
        if(_strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
        if(_strUtils.fString(ITEM).length() > 0)        ht.put("B.ITEM",ITEM);
        if(_strUtils.fString(DONO).length() > 0)        ht.put("B.ORDNO",DONO);
        //if(_strUtils.fString(CUST_NAME).length() > 0)        ht.put("A.CUSTNAME",CUST_NAME);
          if(_strUtils.fString(CUST_CODE).length() > 0)        ht.put("A.CUSTCODE",CUST_CODE);
          if(_strUtils.fString(status).length() > 0)        ht.put("A.STATUS",status);
          if(_strUtils.fString(PRD_TYPE_ID).length() > 0) ht.put("C.ITEMTYPE",PRD_TYPE_ID);
          if(_strUtils.fString(PRD_BRAND_ID).length() > 0) ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
          if(_strUtils.fString(PRD_CLS_ID).length() > 0) ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
      
       }
 

 }catch(Exception e) { }
}
%>
<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="loanOrderSummaryWithPrice.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">

<div id="target" style="display:none">
 		<div class="form-group">    	 
  		<label class="control-label col-sm-2" for="From Date">From Date:</label>
        <div class="col-sm-3">
        <div class="input-group" > 
        <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />         
        <INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY>
      	</div>
    	</div>
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="To Date">To Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY>
      	</div>
    	</div>
    	</div>
    	</div>
    	
    	<div class="form-group">
        <label class="control-label col-sm-2" for="Reference No">Reference No:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="JOBNO" type = "TEXT" value="<%=StrUtils.forHTMLTag(JOBNO)%>" size="30"  MAXLENGTH=20>
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Product ID">Product ID:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="ITEM" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEM)%>" style="width: 100%"  MAXLENGTH=20>
        <span class="input-group-addon"  onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);" >
      <a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
        <i class="glyphicon glyphicon-log-in"  style="font-size: 20px;"></i></a></span>
   		</div>
   		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Loan Order No">Order Number:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="DONO" type = "TEXT" value="<%=StrUtils.forHTMLTag(DONO)%>" size="30"  MAXLENGTH=20>
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="DESC" type = "TEXT" value="<%=_strUtils.forHTMLTag(DESC)%>" style="width: 100%"  MAXLENGTH=20>
   		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Status">Status:</label>
        <div class="col-sm-3">
        <SELECT class="form-control" NAME ="STATUS" style="width: 100%">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='O' <%if(status.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(status.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
     		<OPTION   value='N' <%if(status.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
                           </SELECT>
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Loan Assignee Name">Customer Name:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="CUST_NAME" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUST_NAME)%>" style="width: 100%"  MAXLENGTH=20>
   		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="LOan Assignee ID">Customer ID:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="CUST_CODE" type = "TEXT" value="<%=StrUtils.forHTMLTag(CUST_CODE)%>" size="30"  MAXLENGTH=20>
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Product Class">Product Class:</label>
       	<div class="col-sm-3">
       	 <div class="input-group">
       	<INPUT class="form-control" name="PRD_CLS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" size="30"  MAXLENGTH=20>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Class Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
   		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Product Type">Product Type:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="PRD_TYPE_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_TYPE_ID)%>" size="30"  MAXLENGTH=20>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Type Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Product brand">Product Brand:</label>
       <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" size="30"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div> 
 		</div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
  	 	<div class="col-sm-offset-5 col-sm-4">   
  		<button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;  	
  		<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; 
  		<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; 	  
  		<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('orderManagement.jsp','LBO');}"><b>Back</b></button>&nbsp;&nbsp; -->
  		</div>
  		</div>
  		</div> 
  		
  	<div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
        <button type="button" class="Submit btn btn-default" onClick="javascript:return onGo();"><b>View</b></button>&nbsp;  	
  		<button type="button" class="Submit btn btn-default"  onClick="javascript:ExportReport();"><b>Export All Data</b></button>&nbsp; 
  		<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>	  
  		<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('orderManagement.jsp','LBO');}"><b>Back</b></button> -->
  	    </div>
         </div>
       	  </div>
  
    	<INPUT type="Hidden" name="DIRTYPE" value="LOANORDER">	
  
  		<div class="table-responsive">
  		<div id="tableMovementHistory_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
              <div class="row"><div class="col-sm-12">
              	<table id="tableMovementHistory"
									class="table table-bordered table-hover dataTable no-footer"
									role="grid" aria-describedby="tableMovementHistory_info">
					<thead>
		                <tr role="row">
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">S/N</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Order No</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Ref No</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Customer Name</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Product ID</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Description</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Order Date</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Expiry Date</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">UOM</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Order Qty</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Issued Qty</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Received Qty</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Employee</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Rental Price</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Tax %</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Order Price</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Tax</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Total</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">Pick/Issue Status</th>
		                	<th style="background-color: #eaeafa; color:#333; text-align: center;">User</th>
		                			                	
		                </tr>
		            </thead>
				</table>
            		</div>
						</div>
					</div>
					</div>
 
   <script type="text/javascript">
  var tableData = [];
 
       <%
	   QryList = loanUtil.getLoanOrderSummaryWithPrice(ht,fdate,tdate,DESC,plant,CUST_NAME);
       
       
       if(QryList.size()<=0)
       cntRec ="true";
	       if(QryList.size()<=0 && cntRec=="true" ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
          for (int iCnt =0; iCnt<QryList.size(); iCnt++){
			    Map lineArr = (Map) QryList.get(iCnt);
			    BigDecimal  rentalprice =  new BigDecimal(((String) lineArr.get("RENTALPRICE").toString())) ;
			    String rentalpriceValue=String.valueOf(rentalprice);
				float rentalpriceVal= "".equals(rentalpriceValue) ? 0.0f :  Float.parseFloat(rentalpriceValue);
				//System.out.println(subTotalVal);
				if(rentalpriceVal==0f){
					rentalpriceValue="0.00000";
				}else{
					rentalpriceValue=rentalpriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
				}
				
				 BigDecimal  orderprice =  new BigDecimal(((String) lineArr.get("orderprice").toString())) ;
				    String orderpriceValue=String.valueOf(orderprice);
					float orderpriceVal= "".equals(orderpriceValue) ? 0.0f :  Float.parseFloat(orderpriceValue);
					//System.out.println(subTotalVal);
					if(orderpriceVal==0f){
						orderpriceValue="0.00000";
					}else{
						orderpriceValue=orderpriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
					
					 BigDecimal  tax =  new BigDecimal(((String) lineArr.get("taxval").toString())) ;
					    String taxValue=String.valueOf(tax);
						float taxVal = "".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
						//System.out.println(subTotalVal);
						if(taxVal==0f){
							taxValue="0.00000";
						}else{
							taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
						}
						
						 BigDecimal  totalwithtax =  new BigDecimal(((String) lineArr.get("totalwithtax").toString())) ;
						    String totalwithtaxValue=String.valueOf(totalwithtax);
							float totalwithtaxVal = "".equals(totalwithtaxValue) ? 0.0f :  Float.parseFloat(totalwithtaxValue);
							//System.out.println(subTotalVal);
							if(totalwithtaxVal==0f){
								totalwithtaxValue="0.00000";
							}else{
								totalwithtaxValue=totalwithtaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							}
				

			    
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
         
          %>
       var rowData = [];
       rowData[rowData.length] = '<%=iIndex%>';
       rowData[rowData.length] = '<a href="/track/loanorderservlet?DONO=<%=StrUtils.replaceCharacters2Str((String)lineArr.get("Ordno"))%>&Submit=View&RFLAG=4"><%=(String)lineArr.get("Ordno")%></a>';
       rowData[rowData.length] = '<%=(String)lineArr.get("jobNum")%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("custname")%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("item") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("itemdesc") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("collectionDate") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("expiredate") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("UNITMO") %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum(String.valueOf(lineArr.get("qtyor"))) %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum(String.valueOf(lineArr.get("qtyis"))) %>';
       rowData[rowData.length] = '<%=StrUtils.formatNum(String.valueOf(lineArr.get("qtyrc"))) %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("employeeid") %>';
       rowData[rowData.length] = '<%=rentalpriceValue%>';
       rowData[rowData.length] = '<%=StrUtils.formatNum(String.valueOf(lineArr.get("prodgst"))) %>';
       rowData[rowData.length] = '<%=orderpriceValue%>';
       rowData[rowData.length] = '<%=taxValue%>';
       rowData[rowData.length] = '<%=totalwithtaxValue%>';
       rowData[rowData.length] = '<%=(String)lineArr.get("lnstat") %>';
       rowData[rowData.length] = '<%=(String)lineArr.get("users") %>';
       tableData[tableData.length] = rowData;
     <%}%>

 $(document).ready(function(){

	 for(var rowIndex = 0; rowIndex < tableData.length; rowIndex ++){
		 tableData[rowIndex][13] = parseFloat(tableData[rowIndex][13]).toFixedSpecial1(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		 tableData[rowIndex][14] = parseFloat(tableData[rowIndex][14]).toFixedSpecial1(3);
		 tableData[rowIndex][15] = parseFloat(tableData[rowIndex][15]).toFixedSpecial1(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		 tableData[rowIndex][16] = parseFloat(tableData[rowIndex][16]).toFixedSpecial1(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORCURRENCY%>);
		 tableData[rowIndex][17] = parseFloat(tableData[rowIndex][17]).toFixedSpecial1(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORCURRENCY%>);
	 }
	 
  	 $('#tableMovementHistory').DataTable({
  		 	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
  		  	data: tableData,
  		  	
  		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
  			"<'row'<'col-md-6'><'col-md-6'>>" +
  			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
  	        buttons: [
  	        	{
  	                extend: 'collection',
  	                text: 'Export',
  	                buttons: [
  	                    {
  	                    	extend : 'excel',
  	                    	exportOptions: {
  	    	                	columns: [':visible']
  	    	                }
  	                    },
  	                    {
  	                    	extend : 'pdf',
  	                    	exportOptions: {
  	                    		columns: [':visible']
  	                    	},
                      		orientation: 'landscape',
                              pageSize: 'A3'
  	                    }
  	                ]
  	            },
  	            {
  	            	extend: 'colvis',
                     
                  }
  	        ],"drawCallback": function ( settings ) {
	        	this.attr('width', '100%');
	        	var groupColumn = 4;
				var groupRowColSpan= 14;
			   	var api = this.api();
	            var rows = api.rows( {page:'current'} ).nodes();
	            var last=null;
	            var totalPickQty = 0;
	            var groupTotalPickQty = 0;
	            var totalIssueQty = 0;
	            var groupTotalIssueQty = 0;
	            var totalIssuePriceQty = 0;
	          
	            var groupEnd = 0;
	            var currentRow = 0;
	            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
	                if ( last !== group ) {
	                	if (i > 0) {
	                		$(rows).eq( i ).before(
			                        '<tr class="group"><td colspan="' + groupRowColSpan + '"></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(5).replace(/\.?0+$/, '') + '</td><td>' + parseFloat(groupTotalIssueQty).toFixed(5).replace(/\.?0+$/, '') + '</td><td>' + parseFloat(groupTotalIssuePriceQty).toFixed(5).replace(/\.?0+$/, '') + '</td><td></td><td></td></tr>'
			                    );
	                	}
	                    last = group;
	                    groupEnd = i;    
	                    groupTotalPickQty = 0;
	                    groupTotalIssueQty = 0;
	                    groupTotalIssuePriceQty = 0;
	                    	                    
	                }
	                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
	                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(15)').html().replace(',', '').replace('$', ''));
	                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', '').replace('$', ''));
	                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(16)').html().replace(',', '').replace('$', ''));
	                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
	                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(17)').html().replace(',', '').replace('$', ''));
	                     
	                
	                currentRow = i;
	            } );
	            if (groupEnd > 0 || rows.length == (currentRow + 1)){
	            	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Grand Total</td><td>' + parseFloat(totalPickQty).toFixed(5).replace(/\.?0+$/, '') + '</td><td>' + parseFloat(totalIssueQty).toFixed(5).replace(/\.?0+$/, '') + '</td><td>' + parseFloat(totalIssuePriceQty).toFixed(5).replace(/\.?0+$/, '') + '</td><td></td><td></td></tr>'
                   );
               	$(rows).eq( currentRow ).after(
	                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td>' + parseFloat(groupTotalPickQty).toFixed(5).replace(/\.?0+$/, '') + '</td><td>' + parseFloat(groupTotalIssueQty).toFixed(5).replace(/\.?0+$/, '') + '</td><td>' + parseFloat(groupTotalIssuePriceQty).toFixed(5).replace(/\.?0+$/, '') + '</td><td></td><td></td></tr>'
                   );
	            }
	            
  	        },        
  			
  	      "createdRow": function(row, data, dataIndex){
	        	var parts = data[7].split("/");
	        	var dt = new Date(parseInt(parts[2], 10),
	        	                  parseInt(parts[1], 10) - 1,
	        	                  parseInt(parts[0], 10));
	        	if (dt.getTime() < new Date().getTime()){
	        		$(row).css('color', 'red');
	        	}
	        },  
  		  });	 
 });
   


</script>
 
  </FORM>
  </div></div></div>
  
                <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
    $('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('.Hide').click(function() {
	    $('#target').hide(500);
	    $('.ShowSingle').show(0);
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
    if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('.Show').click();
    }else{
    	$('.Hide').click();
    }
 });
 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>