<%@page import="com.track.dao.CustMstDAO"%>
<%@page import="java.util.*"%>
<%@page import="com.track.dao.CustMstDAO"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.track.constants.IDBConstants"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
<%
	String EMAIL="",PASSWORD="",CONFIRMPASSWORD="",CUSTOMERNAME="",PAYMENTMODE="",NRIC="",DATEOFBIRTH="";
	String NATIONALITY="",GENDER="",UNITNO="",BUILDING="",STREET="",POSTALCODE="",LANDMARK="";
	String MOBILE="",HOME="",FAX="",PRODUCTID="",QTY="",CNAME="";
	CustMstDAO _custdao = new CustMstDAO();
    
    String plant=(String)session.getAttribute("PLANT");
  String  loginuser = (String)session.getAttribute("LOGIN_USER");
  try{
    Hashtable ht = new Hashtable();
    ht.put(IDBConstants.PLANT,plant);
    ht.put(IDBConstants.USER_ID,loginuser);
    List custlist  = _custdao.selectCustMst("CUSTNO,USER_ID,isnull(CNAME,'') CNAME,ADDR1,ADDR2,ADDR3,ADDR4,HPNO,ZIP,GENDER,EMAIL", ht, "");
    for (int i = 0; i < custlist.size(); i++) {
		Map linemap = (Map)custlist.get(0);
		
		UNITNO = (String)linemap.get("ADDR1");
		BUILDING = (String)linemap.get("ADDR2");
		STREET = (String)linemap.get("ADDR3");
		LANDMARK = (String)linemap.get("ADDR4");
		POSTALCODE = (String)linemap.get("ZIP");
		CUSTOMERNAME = (String)linemap.get("CNAME");
		GENDER = (String)linemap.get("GENDER");
		EMAIL = (String)linemap.get("EMAIL");
		MOBILE = (String)linemap.get("HPNO");
	}	}
  catch(Exception e){
	  throw e;
  }
		
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link rel="stylesheet" href="css/style.css" type="text/css" >
<title>Delivery Address</title>
</head>
<form  class="form" name="frmdeliveryaddr" method="post"  action="">
 <INPUT type="hidden" name="CUSTOMERNAME" value="<%=CUSTOMERNAME%>">	
 <INPUT type="hidden" name="EMAIL" value="<%=EMAIL%>">		
  <input type="hidden" name="PLANT" value="<%=plant%>" >
  <input type="hidden" name="MOBILE" value="<%=MOBILE%>" >
  
	<TABLE    border="0" width="100%"  cellspacing="0" cellpadding="0" align="center" >
	 <tr>
      <td width="left"></td>
     </tr>
     <TR >
		<TH BGCOLOR="#669900" colspan="11"><FONT color="#ffffff" size=7>Delivery Address</FONT>&nbsp;</TH>
	</TR>
	
     </table>
 <TABLE    border="0" width="80%"  cellspacing="0" cellpadding="0" align="center" bgcolor="#ffffff">    
	<TR height="35px"><td colspan="2">&nbsp;</td></TR>
	
	 <TR align="center">	
	   	<TD align="right"  class="mobilelabel" width="30%"  ><font size=7 >Unit No <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="50%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="UNITNO" type="TEXT"  class="inactivemobile1"
			value="<%=UNITNO%>" size="15" MAXLENGTH=20  >
		</TD>
	 </TR>
	 
	 <TR height="15px"><td colspan="2">&nbsp;</td></TR>
	 
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="30%" ><font size=7 >Building <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="50%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="BUILDING" type="TEXT" width="55%"  class="inactivemobile1"
			value="<%=BUILDING%>" size="15" MAXLENGTH=20  >
		</TD>
	</TR>
	<TR height="15px"><td colspan="2">&nbsp;</td></TR>
	
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="30%" ><font size=7 >Street <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" width="50%" >
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="STREET" type="TEXT"  class="inactivemobile1"
			value="<%=STREET%>" size="15" MAXLENGTH=20  >
		</TD>
	</TR>
	
	<TR height="15px"><td colspan="2">&nbsp;</td></TR>
	<TR align="center">	
	    <TD  ></TD>
		<TD align="left" width="30%">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="LANDMARK" type="TEXT"  class="inactivemobile1"
			value="<%=LANDMARK%>" size="15" MAXLENGTH=20  >
		</TD>
    </TR>
	<TR height="15px"><td colspan="2">&nbsp;</td></TR>
	<TR align="center">	
	    <TD align="right"  class="mobilelabel" width="33%" ><font size=7 >Postal Code <font color=red size =7 >*</font>&nbsp;:</font></td>
		<TD align="left" >
		&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT name="POSTALCODE" type="TEXT"  class="inactivemobile1"
			value="<%=POSTALCODE%>" size="15" MAXLENGTH=20  >
		</TD>
    </TR>
    
      <TR height="45px"><td colspan="2">&nbsp;</td></TR>
 	 <TR >
	
	  <TD  align="center" colspan=2> 
	  	
	   <INPUT type="button"  value="Submit"  onClick="if(validate(document.frmdeliveryaddr)) {submitForm();}"  class="mobileButton" />
	  	 	
	   </TD>
	</TR>
 </TABLE>

</form>

</html>
<script>
function submitForm(){
	        document.frmdeliveryaddr.action  ="mobileEnquiryConfirm.jsp";
		   document.frmdeliveryaddr.submit();
}

function validate(frmdeliveryaddr)
{
  var frmRoot=document.frmdeliveryaddr;
  
  if(frmRoot.UNITNO.value=="" || frmRoot.UNITNO.value.length==0 )
	 {  
		alert("Please enter unitno!");
		frmRoot.UNITNO.focus();
		return false;
   }
  if(frmRoot.BUILDING.value=="" || frmRoot.BUILDING.value.length==0 )
	 {  
		alert("Please enter building!");
		frmRoot.BUILDING.focus();
		return false;
    }
  if(frmRoot.STREET.value=="" || frmRoot.STREET.value.length==0 )
	 {  
		alert("Please enter street!");
		frmRoot.STREET.focus();
		return false;
    }
  if(frmRoot.POSTALCODE.value=="" || frmRoot.POSTALCODE.value.length==0 )
	 {  
		alert("Please enter postalcode!");
		frmRoot.POSTALCODE.focus();
		return false;
    }
   else
      {
	    frmRoot.submit();
	    return true;
	   }
}
</script>