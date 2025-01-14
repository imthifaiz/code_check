<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Customer Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/bootstrap-datepicker.css">
<script src="js/bootstrap-datepicker.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

</script>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
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
    sAddr3     = "", sAddr4     = "",
    sCountry   = "",
    sZip       = "",
    sCons      = "Y";
 	String sContactName ="", sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="",sPayTerms="",sPayInDays="",isActive="",sRcbno="",customer_type_id="",
    	   customer_status_id="",sState="",CREDITLIMIT="",ISCREDITLIMIT="";
 	String CUSTOMEREMAIL="",WEBSITE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",OPENINGBALANCE="",WORKPHONE="";
 	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	action            = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String)session.getAttribute("PLANT"));
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
    sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
	String taxbylabel= ub.getTaxByLable(plant);
	List customertypelist=custUtil.getCustTypeList("",plant," AND ISACTIVE ='Y'");
    List customerstatuslist=custUtil.getCustStatusList("",plant," AND ISACTIVE ='Y'");
    float CREDITLIMITVALUE ="".equals(CREDITLIMIT) ? 0.0f :  Float.parseFloat(CREDITLIMIT);
    float OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
	sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
	List arrCust = custUtil.getCustomerDetails(sCustCode,plant);
	DateUtils dateutils = new DateUtils();
	if(sCustCode.length() <= 0) sCustCode  = strUtils.fString(request.getParameter("CUST_CODE1"));
        sCustCode   = (String)arrCust.get(0);
        sCustName   = (String)arrCust.get(1);
        sAddr1      = (String)arrCust.get(2);
        sAddr2      = (String)arrCust.get(3);
        sAddr3      = (String)arrCust.get(4);
        sCountry    = (String)arrCust.get(5);
        sZip        = (String)arrCust.get(6);
        sCons       = (String)arrCust.get(7);
        sCustNameL  = (String)arrCust.get(8);
        sContactName=(String)arrCust.get(9);
        sDesgination=(String)arrCust.get(10);
        sTelNo=(String)arrCust.get(11);
        sHpNo=(String)arrCust.get(12);
        sFax=(String)arrCust.get(13);
        sEmail= (String)arrCust.get(14);
        sRemarks=(String)arrCust.get(15);
        sAddr4=(String)arrCust.get(16);
        isActive=(String)arrCust.get(17);
        sPayTerms = (String) arrCust.get(18);
  		sPayInDays = (String) arrCust.get(19);
     	customer_type_id= (String) arrCust.get(20);
 		customer_status_id= (String) arrCust.get(21);
 		sState=(String) arrCust.get(22);
 		sRcbno=(String) arrCust.get(23);
 		CREDITLIMIT=(String) arrCust.get(24);
 		ISCREDITLIMIT=(String) arrCust.get(25);
 		CUSTOMEREMAIL = (String) arrCust.get(26);
 		WEBSITE = (String) arrCust.get(27);
 		FACEBOOK = (String) arrCust.get(28);
 		TWITTER = (String) arrCust.get(29);
 		LINKEDIN = (String) arrCust.get(30);
 		SKYPE = (String) arrCust.get(31);
 		OPENINGBALANCE = (String) arrCust.get(32);
 		WORKPHONE = (String) arrCust.get(33);
 		CREDITLIMITVALUE ="".equals(CREDITLIMIT) ? 0.0f :  Float.parseFloat(CREDITLIMIT);
 		CREDITLIMIT = StrUtils.addZeroes(CREDITLIMITVALUE, numberOfDecimal);
 		
 		OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
 		OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);

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
      <label class="control-label col-form-label col-sm-2" for="Create Customer ID">Customer Id</label>
      <div class="col-sm-4">
       	  	<input name="CUST_CODE" type="TEXT" value="<%=sCustCode%>"
			size="50" MAXLENGTH=100 class="form-control" width="50" readonly>
   		 	
  		<INPUT type="hidden" name="CUST_CODE1" value="<%=sCustCode%>">
      	<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Customer Name">Customer Name</label>
      <div class="col-sm-4">
                
        <INPUT  class="form-control" name="CUST_NAME" type="TEXT" value="<%=sCustName%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
    
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Tax Type">Customer Type</label>
      <div class="col-sm-4">           	
  	
  <SELECT NAME="CUSTOMER_TYPE_ID" class="form-control" data-toggle="dropdown" disabled="disabled" data-placement="right" size="1">
			<OPTION selected value="NOCUSTOMERTYPE">Choose</OPTION>
			<%       for (int i =0; i<customertypelist.size(); i++){
  			Map map = (Map) customertypelist.get(i);
   			String scustomertypeid   = (String) map.get("CUSTOMER_TYPE_ID");
  			String desc   = (String) map.get("CUSTOMER_TYPE_DESC");%>
			<OPTION value="<%=scustomertypeid %>" <%if(customer_type_id.equalsIgnoreCase(scustomertypeid)){%> selected <%}%>><%=desc%></OPTION>
			<%}%>
			
		</SELECT>
  
  	</div>
   </div>
	
	<INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
    
    <div class="form-group">
         <label class="control-label col-form-label col-sm-2" for="TRN No." id="TaxLabel"></label>
      <div class="col-sm-4">
                 
       <INPUT name="RCBNO" type="TEXT" class="form-control"
			value="<%=sRcbno%>" size="50" MAXLENGTH="100" readonly>
      </div>
    </div>
    
     <INPUT name="L_CUST_NAME" type = "hidden" value="<%=sCustNameL%>" size="50"  MAXLENGTH=80 readonly="readonly">

   
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Telephone No">Customer Phone</label>
      <div class="col-sm-4">
                 
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control"
			MAXLENGTH="30" readonly>
      </div>
    </div>

    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Fax">Customer Fax</label>
      <div class="col-sm-4">
               
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="50"
			MAXLENGTH="30" class="form-control" readonly>
      </div>
    </div>
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Customer Email">Customer Email</label>
      	<div class="col-sm-4">  
      	<INPUT name="CUSTOMEREMAIL" type="TEXT" value="<%=CUSTOMEREMAIL%>" size="50" MAXLENGTH=50 class="form-control" READONLY>
      	</div>
      	</div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Website">Website</label>
      	<div class="col-sm-4">  
    	<INPUT name="WEBSITE" type="TEXT" value="<%=WEBSITE%>" size="50" MAXLENGTH=50 class="form-control" READONLY>
      	</div>
    	</div>

<div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
        <li class="nav-item">
            <a href="#other" class="nav-link" data-toggle="tab">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#profile" class="nav-link" data-toggle="tab">Contact Details</a>
        </li>
          <li class="nav-item">
            <a href="#home" class="nav-link" data-toggle="tab">Address</a>
        </li>
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
        </ul>
    <div class="tab-content">
        <div class="tab-pane fade" id="home">
        <br>
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Unit No">Unit No.</label>
      <div class="col-sm-4">  
               
        <INPUT name="ADDR1" type="TEXT" value="<%=sAddr1%>" size="50"
			MAXLENGTH=50 class="form-control" READONLY>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Building">Building</label>
      <div class="col-sm-4">
              
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>" size="50"
			MAXLENGTH=50 class="form-control" READONLY>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Street">Street</label>
      <div class="col-sm-4">
                
        <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" size="50"
			MAXLENGTH=50 class="form-control" READONLY>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="City">City</label>
      <div class="col-sm-4"> 
                
        <INPUT name="ADDR4" type="TEXT" value="<%=sAddr4%>" size="50"
			MAXLENGTH=80  class="form-control" READONLY>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="State">State</label>
      <div class="col-sm-4">           
        <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control" READONLY>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Country">Country</label>
      <div class="col-sm-4">  
             
       <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control" READONLY>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Postal Code">Postal Code</label>
      <div class="col-sm-4">
                
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control" READONLY>
      </div>
    </div>
		     
        </div>
        
        <div class="tab-pane fade" id="profile">
        <br>
        
        <div class="form-group">
         <label class="control-label col-form-label col-sm-2" for="Contact Name">Contact Name</label>
      <div class="col-sm-4">
                 
       <INPUT name="CONTACTNAME" type="TEXT" class="form-control"
			value="<%=sContactName%>" size="50" MAXLENGTH="100" READONLY >
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Designation">Designation</label>
      <div class="col-sm-4">
                
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" size="50" MAXLENGTH="30" READONLY>
      </div>
    </div>
    
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Work phone">Work Phone</label>
      	<div class="col-sm-4">  
        <INPUT name="WORKPHONE" type="TEXT" value="<%=WORKPHONE%>" onkeypress="return isNumber(event)"	size="50" MAXLENGTH=50 class="form-control" READONLY>
      	</div>
    	</div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Hand Phone">Mobile</label>
      <div class="col-sm-4">
              
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control" onkeypress="return isNumber(event)" READONLY
			MAXLENGTH="30">
      </div>
    </div>  
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Email">Email</label>
      <div class="col-sm-4"> 
              
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50"
			MAXLENGTH="50" class="form-control" READONLY>
      </div>
    </div>
     
		     
        </div>
        
        <div class="tab-pane fade" id="other">
        <br>
        
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Opening Balance">Opening Balance</label>
      	<div class="col-sm-4">  
        <INPUT name="OPENINGBALANCE" type="TEXT" READONLY value="<%=new java.math.BigDecimal(OPENINGBALANCE).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
        
   <div class="form-group">  
        <label class="control-label col-form-label col-sm-2" for="Payment Terms">Payment Type</label>
      <div class="col-sm-4">
	      
		    	<INPUT class="form-control" name="PAYTERMS" type="TEXT" value="<%=sPayTerms%>" size="20" MAXLENGTH=100 readonly>
		   		
  		</div>
  		
    <div class="form-inline">
    		<label class="control-label col-form-label col-sm-1" for="Payment Terms">Days</label>
    	    <input name="PMENT_DAYS" type="TEXT" value="<%=sPayInDays%>"
			size="5" MAXLENGTH=10  class="form-control" READONLY>
  	</div>
  </div>  
  
  <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Credit Limit">Credit Limi:</label>
      <div class="col-sm-4">  
      <INPUT name="CREDITLIMIT" type="TEXT" READONLY value="<%=new java.math.BigDecimal(CREDITLIMIT).toPlainString()%>" size="50" MAXLENGTH=50  class="form-control" onkeypress="return isNumberKey(event,this,4)">
      </div>
      <div class="form-inline">
      <div class="col-sm-2">
      <label class="checkbox-inline">      
     <input type = "checkbox" disabled="disabled" name = "ISCREDITLIMIT" value = "ISCREDITLIMIT" <%if(ISCREDITLIMIT.equals("Y")) {%>checked <%}%> /><b>Apply Credit Limit</b></label>
      </div>
      </div>
    </div>   
  
  <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Facebook">Facebook Id</label>
      	<div class="col-sm-4">  
        <INPUT name="FACEBOOK" type="TEXT" value="<%=FACEBOOK%>"	size="50" MAXLENGTH=50 class="form-control" READONLY>
      	</div>
    	</div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Twitter">Twitter Id</label>
      	<div class="col-sm-4">  
        <INPUT name="TWITTER" type="TEXT" value="<%=TWITTER%>"	size="50" MAXLENGTH=50 class="form-control" READONLY>
      	</div>
    	</div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Linkedin">LinkedIn Id</label>
      	<div class="col-sm-4">  
        <INPUT name="LINKEDIN" type="TEXT" value="<%=LINKEDIN%>"	size="50" MAXLENGTH=50 class="form-control" READONLY>
      	</div>
    	</div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Linkedin">Skype Id</label>
      	<div class="col-sm-4">  
        <INPUT name="SKYPE" type="TEXT" value="<%=SKYPE%>"	size="50" MAXLENGTH=50 class="form-control" READONLY>
      	</div>
    	</div> 
    	
    	  <div class="form-group">
  <div class="col-sm-offset-2 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" disabled="disabled" value="Y"<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" disabled="disabled" value="N"<%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div> 
		     
        </div>
        
        <div class="tab-pane fade" id="remark">
        <br>
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Remarks">Remarks</label>
      <div class="col-sm-4"> 
               
        <INPUT name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100 class="form-control " READONLY>
      </div>
    </div>
		     
        </div>
        </div>
        </div>
<br>
<div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
     <button type="button" class="Submit btn btn-default" onClick="window.location.href='custmerSummary.jsp'">Back</button>&nbsp;&nbsp;
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
</div>
</div>

 </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
   
    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.";
});
</script>

 <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>



