<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Consignment Order Customer Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res        = "";
	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb    = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb   = "enabled";
	String action     = "";
	String sCustCode  = "",
    sCustName  = "",
    sCustNameL  = "",
    sAddr1     = "",
    sAddr2     = "",
    sAddr3     = "",sAddr4    = "",
    		sState   = "",
    sCountry   = "",
    sZip       = "",
    sCons      = "Y";
 	String sContactName ="", sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="",isActive="";
       
	DateUtils dateutils=new DateUtils();
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	VendUtil vendUtil = new VendUtil();
	action            = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String)session.getAttribute("PLANT"));
	String username = strUtils.fString((String)session.getAttribute("LOGIN_USER"));
	sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
	
	ArrayList arrCust = custUtil.getToAssigneeDetails(sCustCode,plant);
	sCustCode = (String) arrCust.get(0);
	sCustName = (String) arrCust.get(1);
	sAddr1 = (String) arrCust.get(2);
	sAddr2 = (String) arrCust.get(3);
	sAddr3 = (String) arrCust.get(4);
	sAddr4 = (String) arrCust.get(15);
	sCountry = (String) arrCust.get(5);
	sZip = (String) arrCust.get(6);
	sCons = (String) arrCust.get(7);
	sContactName = (String) arrCust.get(8);
	sDesgination = (String) arrCust.get(9);
	sTelNo = (String) arrCust.get(10);
	sHpNo = (String) arrCust.get(11);
	sEmail = (String) arrCust.get(12);
	sFax = (String) arrCust.get(13);
	sRemarks = (String) arrCust.get(14);
	isActive = (String) arrCust.get(16);
	//sPayTerms = (String) arrCust.get(17);
	//sPayInDays = (String) arrCust.get(18);
	sState = (String) arrCust.get(17);

%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Transfer Assignee ID">Customer ID:</label>
      <div class="col-sm-4">
      	    
    		<input name="CUST_CODE" type="TEXT" value="<%=sCustCode%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>     	
      </div>
    </div>
    
    <div class="form-group">  
      <label class="control-label col-sm-4" for="Transfer Assignee Name">Customer Name:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="CUST_NAME" type="TEXT" value="<%=sCustName%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
          
         
    <div class="form-group">
         <label class="control-label col-sm-4" for="Contact Name">Contact Name:</label>
      <div class="col-sm-4">          
       <INPUT name="CONTACTNAME" type="TEXT" class="form-control"
			value="<%=sContactName%>" size="50" MAXLENGTH="100" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Designation">Designation:</label>
      <div class="col-sm-4">          
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" size="50" MAXLENGTH="30" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Telephone No">Telephone:</label>
      <div class="col-sm-4">          
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control"
			MAXLENGTH="30" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Hand Phone">Mobile:</label>
      <div class="col-sm-4">          
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control"
			MAXLENGTH="30" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Fax">Fax:</label>
      <div class="col-sm-4">          
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="50"
			MAXLENGTH="30" class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Email">Email:</label>
      <div class="col-sm-4">          
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50"
			MAXLENGTH="50" class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Unit No">Unit No:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR1" type="TEXT" value="<%=sAddr1%>" size="50"
			MAXLENGTH=50 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Building">Building:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>" size="50"
			MAXLENGTH=50 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Street">Street:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" size="50"
			MAXLENGTH=50 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="City">City:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR4" type="TEXT" value="<%=sAddr4%>" size="50"
			MAXLENGTH=50  class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="State">State:</label>
      <div class="col-sm-4">          
        <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Country">Country:</label>
      <div class="col-sm-4">          
       <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=30 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Postal Code">Postal Code:</label>
      <div class="col-sm-4">          
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
        <INPUT name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
      </div>
    </div>
     <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y" disabled="disabled"<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N" disabled="disabled"<%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div>
   
   <div class="form-group">        
      <div class="col-sm-offset-5 col-sm-7">
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='toassigneeSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp;
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
    </div>
    </div>
   
   
  </form>
</div>
</div>
</div>


<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>