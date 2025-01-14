<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.IOException"%>
<%@ page import="net.sf.jasperreports.engine.JasperRunManager"%>
<%@ page import="net.sf.jasperreports.engine.*"%>
<%@ page import="javax.print.PrintService"%>
<%@ page import="javax.print.PrintServiceLookup"%>
<%@ page import="net.sf.jasperreports.engine.export.JRPrintServiceExporter"%>
<%@ page import="net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter"%>
<%@ page import="java.awt.print.PrinterJob"%>

<%@ page
   import=" net.sf.jasperreports.engine.*,
           net.sf.jasperreports.engine.design.JasperDesign,
            net.sf.jasperreports.engine.design.JRDesignQuery,
           net.sf.jasperreports.engine.xml.JRXmlLoader,
           net.sf.jasperreports.engine.export.*"
%>

<%@ include file="header.jsp"%>

<!-- Not in Use - Menus status 0 -->
<html>
 <script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<title>Register Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">


var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'REGISTER', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){

   document.form.action  = "mobileregistercustomer_view.jsp?action=NEW";
   document.form.submit();
}
function onAdd(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   var cust_code1 = document.form.CUST_CODE1.value;
   var CUST_NAME   = document.form.CUST_NAME.value;
   var productid   = document.form.PRODUCTID.value;
   if(productid == "" || productid == null) {alert("Please Enter Registration Event"); return false; }
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID"); return false; } 
   if(CUST_NAME == "" || CUST_NAME == null) {
     document.form.CUST_NAME.focus();
   return false; 
   }   
   document.form.action  = "/track/MobileEventRegServlet?Submit=PCEVENTREG_CONFIRM";
   document.form.submit();
}
function onUpdate(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Register ID"); return false; }

   document.form.action  = "mobileregistercustomer_view.jsp?action=UPDATE";
   document.form.submit();
}
function onDelete(){

   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");  return false; }
   confirm("Are you sure to delete Register Customer permanently ");
   document.form.action  = "mobileregistercustomer_view.jsp?action=DELETE";
   document.form.submit();
}
function onView(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Register ID"); return false; }

   document.form.action  = "mobileregistercustomer_view.jsp?action=VIEW";
   document.form.submit();
}
function onIDGen()
{
 document.form.action  = "mobileregistercustomer_view.jsp?action=Auto-ID";
 document.form.submit(); 
}

	
function onPrint(){

   var CUST_CODE   = document.form.CUST_CODE.value;
    if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Register ID"); return false; }
    document.form.action="/track/DynamicFileServlet?action=printRegister&CUST_CODE="+CUST_CODE;
    document.form.submit();
}

</script>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "enabled";
String sUpdateEnb = "enabled";
String sPrint = "disabled";
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
String sContactName ="", sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="",itemno="";
StrUtils strUtils = new StrUtils();
CustUtil custUtil = new CustUtil();
custUtil.setmLogger(mLogger);
action            = strUtils.fString(request.getParameter("action"));
String plant = strUtils.fString((String)session.getAttribute("PLANT"));
itemno = strUtils.fString(request.getParameter("PRODUCTID"));
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

res= strUtils.fString(request.getParameter("MSG"));
//1. >> New
if(action.equalsIgnoreCase("NEW")){
     
      sCustCode  = "";
      sCustName  = "";
      sCustNameL="";
      sAddr1     = "";
      sAddr2     = "";
      sAddr3     = ""; sAddr4     = "";
      sCountry   = "";
      sZip       = "";
      sCons      = "Y";
      sContactName =""; sDesgination="";sTelNo="";sHpNo="";sFax="";sEmail="";sRemarks="";

}
else if(action.equalsIgnoreCase("Auto-ID"))
{

String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
TblControlDAO _TblControlDAO =new TblControlDAO();
_TblControlDAO.setmLogger(mLogger);
      Hashtable  ht=new Hashtable();
     
      String query=" isnull(NXTSEQ,'') as NXTSEQ";
      ht.put(IDBConstants.PLANT,plant);
      ht.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
      try{
      		boolean exitFlag=false; boolean resultflag=false;
      		exitFlag=_TblControlDAO.isExisit(ht,"",plant);
     
     
    	 	if (exitFlag==false)
      		{ 
                    
            	Map htInsert=null;
            	Hashtable htTblCntInsert  = new Hashtable();
           
            	htTblCntInsert.put(IDBConstants.PLANT,plant);
          
            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"C");
             	htTblCntInsert.put("MINSEQ","0000");
             	htTblCntInsert.put("MAXSEQ","9999");
            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
            	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
            
        		sCustCode="C"+"0001";
      		}
      		else
      		{
           //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
         
           	   Map m= _TblControlDAO.selectRow(query, ht,"");
         	   sBatchSeq=(String)m.get("NXTSEQ");
          
           	   int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
          
           	   String updatedSeq=Integer.toString(inxtSeq);
               if(updatedSeq.length()==1)
               {
               	  sZero="000";
               }
           	   else if(updatedSeq.length()==2)
          	   {
             	  sZero="00";
           	   }
           	   else if(updatedSeq.length()==3)
           	   {
                  sZero="0";
               }
           
          
           		Map htUpdate = null;
          
           		Hashtable htTblCntUpdate = new Hashtable();
           		htTblCntUpdate.put(IDBConstants.PLANT,plant);
           		htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"CUSTOMER");
           		htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"C");
           		StringBuffer updateQyery=new StringBuffer("set ");
           		updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
         

        		boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
              	sCustCode="C"+sZero+updatedSeq;
           		}
           	} catch(Exception e)
            {
           		mLogger.exception(true,
    					"ERROR IN JSP PAGE - customer_view.jsp ", e);
            }

}
//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
  if(!custUtil.isExistCustomer(sCustCode,plant)) // if the Customer exists already
    {
          Hashtable ht = new Hashtable();
          ht.put(IConstants.PLANT,plant);
          ht.put(IConstants.CUSTOMER_CODE,sCustCode);
          ht.put(IConstants.CUSTOMER_NAME,sCustName);
          ht.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
          ht.put(IConstants.ADDRESS1,sAddr1);
          ht.put(IConstants.ADDRESS2,sAddr2);
          ht.put(IConstants.ADDRESS3,sAddr3);
          ht.put(IConstants.ADDRESS4,sAddr4);
          ht.put(IConstants.COUNTRY,sCountry);
          ht.put(IConstants.ZIP,sZip);
          ht.put(IConstants.USERFLG1,sCons);
          ht.put(IConstants.NAME,sContactName);
          ht.put(IConstants.DESGINATION,sDesgination);
          ht.put(IConstants.TELNO,sTelNo);
          ht.put(IConstants.HPNO,sHpNo);
          ht.put(IConstants.FAX,sFax);
          ht.put(IConstants.EMAIL,sEmail);
          ht.put(IConstants.REMARKS,sRemarks);
          ht.put(IConstants.CREATED_AT,new DateUtils().getDateTime());
          ht.put(IConstants.CREATED_BY,sUserId);
           ht.put(IConstants.ISACTIVE,"Y");
          
          MovHisDAO mdao = new MovHisDAO(plant);
          mdao.setmLogger(mLogger);
          Hashtable htm = new Hashtable();
          htm.put(IDBConstants.PLANT,plant);
          htm.put(IDBConstants.DIRTYPE,TransactionConstants.ADD_CUST);
          htm.put("RECID","");
          htm.put(IDBConstants.CREATED_BY,sUserId);
         
          if(!sRemarks.equals(""))
		  {
			htm.put(IDBConstants.REMARKS, sCustCode+","+sRemarks);
		  }
		  else
		  {
			htm.put(IDBConstants.REMARKS, sCustCode);
		  }
          
          htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
          
          boolean custInserted = custUtil.insertCustomer(ht);
          boolean  inserted = mdao.insertIntoMovHis(htm);
          if(custInserted&&inserted) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Customer Added Successfully</font>";

          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to add New Customer</font>";

          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Customer Exists already. Try again</font>";

    }
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
try{

	sPrint="enabled";
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
   }catch(Exception e)
   {
       res="no details found for customer id : "+  sCustCode;
   }
 
}


%>

<%@ include file="body.jsp"%>
<FORM name="form" method="post">
  <br>
  <CENTER>
  <input type="hidden" name="PRICE" value="">
  <input type="hidden" name="DESCRIPTION1" value="">
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Create Register Master</font></TH>
    </TR>
  </TABLE><B><CENTER><%=res%></B>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
   <tr>
     <TH WIDTH="35%" ALIGN="RIGHT" >Registration Events : </TH>
      <TD><INPUT name="PRODUCTID" type = "TEXT" value="<%=itemno%>" size="40"  MAXLENGTH=50>
<a href="#" onClick="javascript:popUpWin('list/catalogList.jsp?ITEM='+form.PRODUCTID.value);">
								<img src="images/populate.gif" border="0" /> </a>           
           </TD>
      </tr>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Customer ID : </TH>
         <TD>
                <INPUT name="CUST_CODE" type = "TEXT" value="<%=sCustCode%>" size="40"  MAXLENGTH=20 <%=sCustEnb%>>
                <INPUT type = "hidden" name="CUST_CODE1" value = <%=sCustCode%>>
            <INPUT class="Submit" type="BUTTON" value="New" onClick="onIDGen();" >&nbsp;&nbsp;           
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Customer Name : </TH>
         <TD>
             <INPUT name="CUST_NAME" type = "TEXT" value="<%=sCustName%>" size="40"  MAXLENGTH=100>
            <a href="#" onClick="javascript:popUpWin('customer_list.jsp?CUST_NAME='+form.CUST_NAME.value);"><img src="images/populate.gif" border="0"></a>
             <INPUT class="Submit" type="BUTTON" value="View" onClick="onViewDetails();">
         </TD>

    </TR>
         <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Designation:</TH>
      <TD>
        <INPUT name="DESGINATION" type="TEXT" value="<%=sDesgination%>" size="40" MAXLENGTH="30"/>
      </TD>
    </TR>
    
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Contact Details:</TH>
      <TD>&nbsp;</TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Company Name:</TH>
      <TD>
            <INPUT name="CONTACTNAME" type="TEXT" value="<%=sContactName%>" size="40" MAXLENGTH="100"/>
      </TD>
    </TR>
   
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Telephone No:</TH>
      <TD>
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="40" MAXLENGTH="20"/>
      </TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Hand Phone No:</TH>
      <TD>
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="40" MAXLENGTH="20"/>
      </TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Fax:</TH>
      <TD>
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="40" MAXLENGTH="20"/>
      </TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Email:</TH>
      <TD>
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="40" MAXLENGTH="30"/>
      </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT">Unit No : </TH>
         <TD><INPUT name="ADDR1" type = "TEXT" value="<%=sAddr1%>" size="40"  MAXLENGTH=40></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Building : </TH>
         <TD><INPUT name="ADDR2" type = "TEXT" value="<%=sAddr2%>" size="40"  MAXLENGTH=40></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Street : </TH>
         <TD><INPUT name="ADDR3" type = "TEXT" value="<%=sAddr3%>" size="40"  MAXLENGTH=40></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >City : </TH>
         <TD><INPUT name="ADDR4" type = "TEXT" value="<%=sAddr4%>" size="40"  MAXLENGTH=40></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Country : </TH>
         <TD><INPUT name="COUNTRY" type = "TEXT" value="<%=sCountry%>" size="40"  MAXLENGTH=40></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Postal&nbsp;Code : </TH>
         <TD><INPUT name="ZIP" type = "TEXT" value="<%=sZip%>" size="40"  MAXLENGTH=10></TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Remarks : </TH>
         <TD><INPUT name="REMARKS" type = "TEXT" value="<%=sRemarks%>" size="40"  MAXLENGTH=100></TD>
    </TR>
       <TR>
        
         <TD><INPUT name="L_CUST_NAME" type = "hidden" value="lastname" size="40"  MAXLENGTH=80></TD>
    </TR> 
 
    <TR>
         <TD COLSPAN = 2><BR></TD>
    </TR>
    <TR>
         <TD COLSPAN = 2><center>
                <INPUT class="Submit" type="BUTTON" value="Back" onClick="window.location.href='../home'">&nbsp;&nbsp;
             <!--   <INPUT class="Submit" type="BUTTON" value="New" onClick="onNew();" <%=sNewEnb%>>&nbsp;&nbsp; -->
                <INPUT class="Submit" type="BUTTON" value="Clear" onClick="onNew();" <%=sNewEnb%>>&nbsp;&nbsp;
          
                <INPUT class="Submit" type="BUTTON" value="Register" onClick="onAdd();" <%=sAddEnb%>>&nbsp;&nbsp;
             <!--   <INPUT class="Submit" type="BUTTON" value="Update" onClick="onUpdate();" <%=sUpdateEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Delete" onClick="onDelete();" <%=sDeleteEnb%>> -->
                             
              <INPUT class="Submit" type="BUTTON" value="Print"  onClick="onPrint();" <%=sPrint%>>&nbsp;&nbsp; 
              
         </TD>
    </TR>
</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>
<script>
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
<%@ include file="footer.jsp"%>

