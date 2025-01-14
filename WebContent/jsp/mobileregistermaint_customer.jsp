<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<!-- Not in Use - Menus status 0 -->
<html>
 <script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>

<title>Maintain Register </title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">


var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){

   document.form.action  = "mobileregistermaint_customer.jsp?action=NEW";
   document.form.submit();
}

function onUpdate(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   var productid   = document.form.PRODUCTID.value;
   if(productid == "" || productid == null) {alert("Please Enter Registration Event"); return false; }
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID"); return false; }
    var radio_choice = false;
   // for (i = 0; i < document.form.ACTIVE.length; i++)
   // {
     //   if (document.form.ACTIVE[i].checked)
       // radio_choice = true; 
   // }
   // if (!radio_choice)
   // {
  //  alert("Please select Active or non Active mode.")
  //  return (false);
  //  }
    
   var chk = confirm("Are you sure you would like to Unregister?");
	if(chk){
   document.form.action  = "/track/MobileEventRegServlet?Submit=PCEVENT_UNREGISTER";
   document.form.submit();
	}
	else{
		return false;}	   
}

function onView(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Register ID"); return false; }

   document.form.action  = "mobileregistermaint_customer.jsp?action=VIEW";
   document.form.submit();
}

function onPrint(){

	   var CUST_CODE   = document.form.CUST_CODE.value;
	    if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Register ID"); return false; }
	    document.form.action="/track/DynamicFileServlet?action=printRegister&CUST_CODE="+CUST_CODE;
	    document.form.submit();
	}

</SCRIPT>
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
 String sContactName ="", sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="",isActive="";
StrUtils strUtils = new StrUtils();
CustUtil custUtil = new CustUtil();
custUtil.setmLogger(mLogger);
String itemno="";
action            = strUtils.fString(request.getParameter("action"));
String plant = strUtils.fString((String)session.getAttribute("PLANT"));

sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
DateUtils dateutils = new DateUtils();
if(sCustCode.length() <= 0) sCustCode  = strUtils.fString(request.getParameter("CUST_CODE1"));
sCustName  = strUtils.fString(request.getParameter("CUST_NAME"));
sCustNameL  = strUtils.fString(request.getParameter("L_CUST_NAME"));
sAddr1     = strUtils.fString(request.getParameter("ADDR1"));
sAddr2     = strUtils.fString(request.getParameter("ADDR2"));
sAddr3     = strUtils.fString(request.getParameter("ADDR3"));
sAddr4     = strUtils.fString(request.getParameter("ADDR4"));
sCountry   = strUtils.fString(request.getParameter("COUNTRY"));
sZip       = strUtils.fString(request.getParameter("ZIP"));
sCons      = strUtils.fString(request.getParameter("CONSIGNMENT"));
sContactName      = strUtils.fString(request.getParameter("CONTACTNAME"));
sDesgination  = strUtils.fString(request.getParameter("DESGINATION"));
sTelNo  = strUtils.fString(request.getParameter("TELNO"));
sHpNo  = strUtils.fString(request.getParameter("HPNO"));
sFax  = strUtils.fString(request.getParameter("FAX"));
sEmail= strUtils.fString(request.getParameter("EMAIL"));
sRemarks= strUtils.fString(request.getParameter("REMARKS"));
isActive= strUtils.fString(request.getParameter("ACTIVE"));
itemno = strUtils.fString(request.getParameter("PRODUCTID"));
res = strUtils.fString(request.getParameter("MSG"));
//1. >> New
if(action.equalsIgnoreCase("NEW")){
     
      sCustCode  = "";
      sCustName  = "";
      sCustNameL="";itemno="";
      sAddr1     = "";
      sAddr2     = "";
      sAddr3     = ""; sAddr4     = "";
      sCountry   = "";
      sZip       = "";
      sCons      = "Y";
      sContactName =""; sDesgination="";sTelNo="";sHpNo="";sFax="";sEmail="";sRemarks="";

}
//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {

   if(custUtil.isExistCustomer(sCustCode,plant))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(IConstants.PLANT,plant);
          htUpdate.put(IConstants.CUSTOMER_CODE,sCustCode);
          htUpdate.put(IConstants.CUSTOMER_NAME,sCustName);
          htUpdate.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
          htUpdate.put(IConstants.ADDRESS1,sAddr1);
          htUpdate.put(IConstants.ADDRESS2,sAddr2);
          htUpdate.put(IConstants.ADDRESS3,sAddr3);
          htUpdate.put(IConstants.ADDRESS4,sAddr4);
          htUpdate.put(IConstants.COUNTRY,sCountry);
          htUpdate.put(IConstants.ZIP,sZip);
          htUpdate.put(IConstants.USERFLG1,sCons);
          htUpdate.put(IConstants.NAME,sContactName);
          htUpdate.put(IConstants.DESGINATION,sDesgination);
          htUpdate.put(IConstants.TELNO,sTelNo);
          htUpdate.put(IConstants.HPNO,sHpNo);
          htUpdate.put(IConstants.FAX,sFax);
          htUpdate.put(IConstants.EMAIL,sEmail);
          htUpdate.put(IConstants.REMARKS,sRemarks);
          htUpdate.put(IConstants.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(IConstants.UPDATED_BY,sUserId);
          htUpdate.put(IConstants.ISACTIVE,isActive);


          Hashtable htCondition = new Hashtable();
          htCondition.put("CUSTNO",sCustCode);
          htCondition.put(IConstants.PLANT,plant);
            
                MovHisDAO mdao = new MovHisDAO(plant);
                mdao.setmLogger(mLogger);
       Hashtable htm = new Hashtable();
          htm.put(IDBConstants.PLANT,plant);
          htm.put(IDBConstants.DIRTYPE,TransactionConstants.UPD_CUST);
          htm.put("RECID","");
          htm.put(IDBConstants.CREATED_BY,sUserId);   htm.put("CRBY",sUserId);
           htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
           htm.put(IDBConstants.REMARKS,sCustCode+","+sRemarks);
           htm.put(IDBConstants.UPDATED_AT,dateutils.getDateTime());
           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
          boolean custUpdated = custUtil.updateCustomer(htUpdate,htCondition);
            boolean  inserted = mdao.insertIntoMovHis(htm);
          if(custUpdated&&inserted) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Customer Updated Successfully</font>";
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Update Customer</font>";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Customer doesn't not Exists. Try again</font>";

    }
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
try{


    ArrayList arrCust = custUtil.getCustomerDetails(sCustCode,plant);
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
   }catch(Exception e)
   {

       res="no details found for customer id : "+  sCustCode;
   }
 
}

%>

<%@ include file="body.jsp"%>
<FORM name="form" method="post">
<input type="hidden" name="DESCRIPTION1" value="">
<input type="hidden" name="PRICE" value="">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Maintain Registration Order Details</font></TH>
    </TR>
  </TABLE><B><CENTER><%=res%></B>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
   <tr>
     <TH WIDTH="35%" ALIGN="RIGHT" > * Registration Events : </TH>
      <TD><INPUT name="PRODUCTID" type = "TEXT" value="<%=itemno%>" size="40"  MAXLENGTH=50>
<a href="#" onClick="javascript:popUpWin('list/catalogList.jsp?ITEM='+form.PRODUCTID.value);">
								<img src="images/populate.gif" border="0" /> </a>           
           </TD>
      </tr>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Customer ID : </TH>
         <TD>
                <INPUT name="CUST_CODE" type = "TEXT" value="<%=sCustCode%>" size="40"  MAXLENGTH=20 <%=sCustEnb%> >
                <INPUT type = "hidden" name="CUST_CODE1" value = <%=sCustCode%>>
                </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Customer Name : </TH>
         <TD>
             <INPUT name="CUST_NAME" type = "TEXT" value="<%=sCustName%>" size="40"  MAXLENGTH=100>
             <a href="#" onClick="javascript:popUpWin('customer_list.jsp?CUST_NAME='+form.CUST_NAME.value);"><img src="images/populate.gif" border="0"></a>
             <INPUT class="Submit" type="BUTTON" value="View" onClick="onViewDetails();">
         </TD>
    </TR>
   
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Designation:</TH>
      <TD>
        <INPUT name="DESGINATION" type="TEXT" value="<%=sDesgination%>" size="40" MAXLENGTH="30" class="inactivegry" readonly="readonly"/>
      </TD>
    </TR>
   
         <INPUT name="L_CUST_NAME" type = "hidden" value="<%=sCustNameL%>" size="40"  MAXLENGTH=100>
  
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Contact Details:</TH>
      <TD>&nbsp;</TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Company Name:</TH>
      <TD>
        <INPUT name="CONTACTNAME" type="TEXT" value="<%=sContactName%>" size="40" MAXLENGTH="100" class="inactivegry" readonly="readonly"/>
      </TD>
    </TR>
    
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Telephone No:</TH>
      <TD>
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="40" MAXLENGTH="20" class="inactivegry" readonly="readonly"/>
      </TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Hand Phone No:</TH>
      <TD>
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="40" MAXLENGTH="20" class="inactivegry" readonly="readonly"/>
      </TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Fax:</TH>
      <TD>
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="40" MAXLENGTH="20" class="inactivegry" readonly="readonly"/>
      </TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Email:</TH>
      <TD >
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="40" MAXLENGTH="30" class="inactivegry" readonly="readonly"/>
      </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT">Unit No : </TH>
         <TD><INPUT name="ADDR1" type = "TEXT" value="<%=sAddr1%>" size="40"  MAXLENGTH=40 class="inactivegry" readonly="readonly"></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Building : </TH>
         <TD><INPUT name="ADDR2" type = "TEXT" value="<%=sAddr2%>" size="40"  MAXLENGTH=40 class="inactivegry" readonly="readonly"></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Street : </TH>
         <TD><INPUT name="ADDR3" type = "TEXT" value="<%=sAddr3%>" size="40"  MAXLENGTH=40 class="inactivegry" readonly="readonly"></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >City : </TH>
         <TD><INPUT name="ADDR4" type = "TEXT" value="<%=sAddr4%>" size="40"  MAXLENGTH=40 class="inactivegry" readonly="readonly"></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Country : </TH>
         <TD><INPUT name="COUNTRY" type = "TEXT" value="<%=sCountry%>" size="40"  MAXLENGTH=40 class="inactivegry" readonly="readonly"></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Postal&nbsp;Code : </TH>
         <TD><INPUT name="ZIP" type = "TEXT" value="<%=sZip%>" size="40"  MAXLENGTH=10 class="inactivegry" readonly="readonly"></TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Remarks : </TH>
         <TD><INPUT name="REMARKS" type = "TEXT" value="<%=sRemarks%>" size="40"  MAXLENGTH=100 class="inactivegry" readonly="readonly"></TD>
    </TR>
    
  <!-- <tr> <td>&nbsp;&nbsp;</td><TD align=" left">
            <INPUT name="ACTIVE" type = "radio" value="Y"    <%if(isActive.equalsIgnoreCase("Y")) {%>checked <%}%> >Active 
            <INPUT name="ACTIVE" type = "radio" value="N"    <%if(isActive.equalsIgnoreCase("N")) {%>checked <%}%>  >Non Active </TD>
                      </tr>  -->
 
    <TR>
         <TD COLSPAN = 2><BR></TD>
    </TR>
    <TR>
         <TD COLSPAN = 2><center>
                <INPUT class="Submit" type="BUTTON" value="Back" onClick="window.location.href='../home'">&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Clear" onClick="onNew();" <%=sNewEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="UnRegister" onClick="onUpdate();" <%=sUpdateEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Print"  onClick="onPrint();" <%=sAddEnb%>>&nbsp;&nbsp; 
         </TD>
    </TR>
</TABLE>
</CENTER>
<script type="text/javascript">
function onViewDetails() {
	var custcode = document.form.CUST_CODE.value;	
	
		var urlStr = "/track/mobilehandlingservlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {				
				PLANT : "<%=plant%>",
				CUSTCODE : custcode,
				ACTION : "VIEW_CUSTDETAIL"
				},
				dataType : "json",
				async: false,
				success : function(data) {
					if (data.status == "101") {
						alert("Session cookies are Disabled/Expired,Please enable cookies to proceed");
                                         
					}
					if (data.status == "100") {
						var resultobj = data.result;
						document.getElementById("CUST_CODE").value=resultobj.custcode;
						document.getElementById("CUST_CODE1").value=resultobj.custcode;
						document.getElementById("CUST_NAME").value=resultobj.custname;
						document.getElementById("ADDR1").value=resultobj.addr1;
						document.getElementById("ADDR2").value=resultobj.addr2;
						document.getElementById("ADDR3").value=resultobj.addr3;
						document.getElementById("ADDR4").value=resultobj.addr4;
						document.getElementById("TELNO").value=resultobj.telno;
						document.getElementById("HPNO").value=resultobj.hpno;
						document.getElementById("EMAIL").value=resultobj.email;
						document.getElementById("ZIP").value=resultobj.zip;
						document.getElementById("FAX").value=resultobj.fax;
						document.getElementById("COUNTRY").value=resultobj.country;
						document.getElementById("REMARKS").value=resultobj.remarks;
						document.getElementById("DESGINATION").value=resultobj.desgination;
						document.getElementById("CONTACTNAME").value=resultobj.contactname;
					}
					
				}
			});
		}
</script>
</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

