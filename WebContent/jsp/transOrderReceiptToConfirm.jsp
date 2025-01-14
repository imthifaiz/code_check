<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.constants.IDBConstants"%>

<%--New page design begin --%>
<%
String title = "Transfer Order Receipt Confirm";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<%--New page design end --%>

<!-- <html>
<head> -->
<script language="javascript">
 function submitFormValues(){
	document.form1.action="/track/TransferOrderServlet?Submit=TOConfirmMultipleRecipt";
        document.form1.submit();
 }
</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<!-- <title>Transfer Order Receipt Confirm</title>
</head>
<link rel="stylesheet" href="css/style.css"> -->
<%
	StrUtils _strUtils     = new StrUtils();
	Generator generator   = new Generator();
	TOUtil toutil       = new TOUtil();
	toutil.setmLogger(mLogger);
	DateUtils _dateUtils = new DateUtils();
	ArrayList  dataList  = new ArrayList();
	String  fieldDesc="",loc="",expiredate="",item="",batch="",isrecfound="",TRANSACTIONDATE="";
	session= request.getSession();
	String plant = (String)session.getAttribute("PLANT");
    String userID = (String)session.getAttribute("LOGIN_USER");
	String ORDERNO="", CUSTNAME="",action="",FROMLOC="",TOLOC="";
    action         = _strUtils.fString(request.getParameter("action")).trim();
	String html = "",cntRec ="false";
	ORDERNO       = _strUtils.fString(request.getParameter("ORDERNO"));
	CUSTNAME      = _strUtils.fString(request.getParameter("CUSTNAME"));
    FROMLOC       = _strUtils.fString(request.getParameter("FROMLOC"));
	TOLOC      = _strUtils.fString(request.getParameter("TOLOC"));
    TRANSACTIONDATE = _strUtils.fString(request.getParameter("TRANSACTIONDATE"));
    String curDate =_dateUtils.getDate();
    if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
    try{
    if(action.equalsIgnoreCase("qtyerror"))
      {
         fieldDesc=(String)request.getSession().getAttribute("QTYERROR");
         fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
       dataList = toutil.getToPickDetailsToConfirm(plant,ORDERNO);
       if(dataList.size()<=0){
			cntRec ="true";
                        isrecfound="disabled";
                        }
 }catch(Exception e) { 
	  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
 }

%>
<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body" onload="alert();">
<%--New page design end --%>


<form class="form-horizontal" name="form1" method="post" action="transferOrderReceiptToConfirm.jsp" >
   	<input type="hidden" name="xlAction" value="">
	<input type="hidden" name="PGaction" value="View">
	<input type="hidden" name="PLANT" value="<%=plant%>">
        
      	<center>
      <h2><small><%=fieldDesc%></small></h2>
   </center>
   
   <br>
   
       <div class="form-group">
       <label class="control-label col-sm-2" for="Order No">Order Number:</label>
       <div class="col-sm-3">
       <INPUT class="form-control" name="ORDERNO" type="TEXT" value="<%=ORDERNO%>" size="30"  readonly MAXLENGTH="80"/>
   	   </div>
  	   <div class="form-inline">
  	   <label class="control-label col-sm-2" for="customer name">Customer Name</label>
       <div class="col-sm-3">
       <INPUT class="form-control" name="CUSTNAME" type="TEXT" value="<%=_strUtils.forHTMLTag(CUSTNAME)%>" style="width: 100%" MAXLENGTH=50 readonly>
    	</div>
 		</div>  
 		</div>
 		
        <div class="form-group">
        <label class="control-label col-sm-2" for="From Location">From Location:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="FROMLOC" type = "TEXT" value="<%=FROMLOC%>" size="30"  MAXLENGTH=20 readonly>
       </div>
    	<div class="form-inline">
    	<label class="control-label col-sm-2" for="To Location">To Location:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="TOLOC" type="TEXT" value="<%=TOLOC%>"   style="width: 100%" MAXLENGTH=50 readonly>
        </div>
 		</div>
 		</div>
 		
 		<TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 class="table">
    	<thead style="background: #eaeafa; font-size: 15px;">
    	<TR>
         <TH>Order Line</TH>
         <TH>Product ID</TH>
         <TH>Description</TH>
          <TH>Batch</TH>
         <TH>Pick Qty</TH>
         <TH>UOM</TH>
           </tr>
           </thead>
           <tbody>
            <%
	       if(dataList.size()<=0 && cntRec=="true" ){ %>
		    <TR><TD colspan="6" align="center">No Data For This criteria</TD></TR>
		  <%}%>

		  <%
          for (int iCnt =0; iCnt<dataList.size(); iCnt++){
            Map lineArr = (Map) dataList.get(iCnt);
            int iIndex = iCnt + 1;
            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
         %>

            <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=(String)lineArr.get("TOLNO")%></TD>
            <TD align= "left"><%=(String)lineArr.get("ITEM")%></TD>
            <TD align= "left"><%=(String)lineArr.get("ITEMDESC")%></TD>
             <TD align= "left"><%=(String)lineArr.get("BATCH")%></TD>
            <TD align= "center"><%=StrUtils.formatNum((String)lineArr.get("PICKQTY"))%></TD>
            <TD align= "left"><%=(String)lineArr.get("Uom")%></TD>
            
           </TR>
       <%}%>           
           </tbody>
       </TABLE>
 		
 		<div class="form-group">        
      	<div class="col-sm-offset-3 col-sm-3">
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='TOSummaryForSingleStepPickIssue.jsp?action=View&TONO='+form.ORDERNO.value+'&PLANT='+form.PLANT.value "><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="submitFormValues();" <%=isrecfound%> ><b>Confirm Receiving</b></button>
      	</div>     	
      	 <div class="form-inline">
        <label class="control-label col-sm-2" for="Transaction Date">Transaction Date:</label>
        <div class="col-sm-2">
        <div class="input-group">          
		<INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE"	value="<%=TRANSACTIONDATE%>" type="TEXT" size="20" MAXLENGTH="80" readonly="readonly">
			</div>
    	</div>
    	</div>
    	</div>
 		        		       
  		</form>
		</div>
		</div>
		</div>
		
	

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

