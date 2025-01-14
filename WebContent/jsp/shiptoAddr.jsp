<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript">
var subWin = null;

function popUpWin(URL) {
 subWin = window.open(URL, 'Shipping Address List', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function submitShippingAddr() {
 			
                        var  custname=document.form.CUST_NAME.value;
                        var  contactName=document.form.CONTACTNAME.value;
                        var  addr1=document.form.ADDR1.value;
                        var  addr2=document.form.ADDR2.value;
                        var  city=document.form.CITY.value;
                        var  country=document.form.COUNTRY.value;
                        var  zip=document.form.ZIP.value;
                        var  telno=document.form.TELNO.value;
                         
                         if (custname.length < 1)
                        {
                        alert("Please Enter Customer Name !");
                        document.form.CUST_NAME.focus();
                        return false;
                        }
 		 	window.opener.form.SCUST_NAME.value= custname;
                        window.opener.form.SCONTACT_NAME.value= contactName;
                        window.opener.form.SADDR1.value=  addr1;
                        window.opener.form.SADDR2.value=  addr2;
                        window.opener.form.SCITY.value=  city;
                        window.opener.form.SCOUNTRY.value=  country;
                        window.opener.form.SZIP.value=  zip
                        window.opener.form.STELNO.value= telno
 			window.close();
                      
 		}
</script>

<title>Shipping Deatils</title>
<link rel="stylesheet" href="css/style.css">


<%
String sUserId = (String) session.getAttribute("LOGIN_USER");

String sOrdType="",sCustName ="",sContactName ="",sAddr1="",sAddr2="", sDesgination="",sTelNo="",sCity="",sCountry="",sZip="";
StrUtils strUtils = new StrUtils();
String plant = strUtils.fString((String)session.getAttribute("PLANT"));
sCustName  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("SCUST_NAME")));
sAddr1     = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("SADDR1")));
sAddr2     = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("SADDR2")));
sCity   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("SCITY")));
sCountry   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("SCOUNTRY")));
sZip       = strUtils.fString(request.getParameter("SZIP"));
sContactName      = strUtils.InsertQuotes(strUtils.fString(request.getParameter("SCONTACT_NAME")));
sTelNo  = strUtils.fString(request.getParameter("STELNO"));
sOrdType  = strUtils.fString(request.getParameter("ORDTYPE"));


%>


<FORM name="form" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Shipping Information</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
   
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > * Customer Name : </TH>
         <TD>
             <INPUT name="CUST_NAME" type = "TEXT" value="<%=sCustName%>" size="50"  MAXLENGTH=40>
           <!--  <a href="#" onClick="javascript:popUpWin('list/shipAdrList.jsp?CUST_NAME='+form.CUST_NAME.value+'&ORDTYPE='+form.ORDTYPE.value);"><img src="images/populate.gif" border="0"></a>--> 
       </TD>

    </TR>
    
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Contact Details:</TH>
      <TD>&nbsp;</TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Name:</TH>
      <TD>
            <INPUT name="CONTACTNAME" type="TEXT" value="<%=sContactName%>" size="50" MAXLENGTH="30"/>
            <INPUT name="ORDTYPE" type="HIDDEN" value="<%=sOrdType%>" size="50" MAXLENGTH="30"/>
      </TD>
    </TR>
   
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Telephone No:</TH>
      <TD>
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" MAXLENGTH="20"/>
      </TD>
    </TR>
    
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT">Address1 : </TH>
         <TD><INPUT name="ADDR1" type = "TEXT" value="<%=sAddr1%>" size="50"  MAXLENGTH=40></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Address 2 : </TH>
         <TD><INPUT name="ADDR2" type = "TEXT" value="<%=sAddr2%>" size="50"  MAXLENGTH=40></TD>
    </TR>
   
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >City : </TH>
         <TD><INPUT name="CITY" type = "TEXT" value="<%=sCity%>" size="50"  MAXLENGTH=20></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Country : </TH>
         <TD><INPUT name="COUNTRY" type = "TEXT" value="<%=sCountry%>" size="50"  MAXLENGTH=20></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Postal&nbsp;Code : </TH>
         <TD><INPUT name="ZIP" type = "TEXT" value="<%=sZip%>" size="50"  MAXLENGTH=10></TD>
    </TR>
    
 
    <TR>
         <TD COLSPAN = 2><BR></TD>
    </TR>
    <TR>
         <TD align="right"><a href="#"onclick="submitShippingAddr();"><input type="submit" value="Close"  ></a></TD>
         <TD ><a href="#"onclick="window.close();"><input type="submit" value="Cancel"></a></TD>
    </TR>
</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>

