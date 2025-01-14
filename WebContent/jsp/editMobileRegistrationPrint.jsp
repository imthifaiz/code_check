<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Edit Mobile Registration Order Printout</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
		
	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
	}
	
	function onClear()
	{
		document.form1.InboundOrderHeader.value="";
		document.form1.InvoiceOrderToHeader.value="";
		document.form1.FromHeader.value="";
		document.form1.Date.value="";

		document.form1.RefNo.value="";
		document.form1.Terms.value="";
		document.form1.TermsDetails.value="";
		document.form1.SoNo.value="";

		document.form1.Item.value="";
		document.form1.Description.value="";
		document.form1.OrderQty.value="";
		document.form1.UOM.value="";

		document.form1.Footer1.value="";
		document.form1.Footer2.value="";
                document.form1.Footer3.value="";
		document.form1.Footer4.value="";

	}
	
	function onAdd(){
     		
   		
   		document.form1.action  =  "/track/SettingsServlet?Submit=EDIT_DO_RCPT_HDR";
   		   	
   	    document.form1.submit();
   	
	}
	

	
	
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
        
	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String InboundOrderHeader  = "", InvoiceOrderToHeader  = "", FromHeader  = "",Date="",OrderNo ="";
	String RefNo   = "", Terms  = "", TermsDetails   = "",SoNo ="",Item ="";
	String Description = "", OrderQty  = "", UOM = "",Footer1="",Footer2="",Footer3="",Footer4="";
	StrUtils strUtils = new StrUtils();
    res =  strUtils.fString(request.getParameter("result"));

	DOUtil doUtil = new DOUtil();
	
         Map m= doUtil.getDOReceiptHdrDetails(plant,"Mobile Registration");
         
         if(!m.isEmpty()){
             InboundOrderHeader= (String) m.get("HDR1");
             //InboundOrderHeader = "Registration Order";
             InvoiceOrderToHeader = (String) m.get("HDR2");
             FromHeader = (String) m.get("HDR3");
             Date = (String) m.get("DATE");
             OrderNo = (String) m.get("ORDERNO");
             
             RefNo = (String) m.get("REFNO");
             SoNo = (String) m.get("SONO");
             
             Item = (String) m.get("ITEM");
             Description = (String) m.get("DESCRIPTION");
             OrderQty = (String) m.get("ORDERQTY");
             UOM = (String) m.get("UOM");
             
             Footer1 = (String) m.get("F1");
             Footer2 = (String) m.get("F2");
              Footer3 = (String) m.get("F3");
             Footer4 = (String) m.get("F4");
         }
         
	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}

	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		InboundOrderHeader  = "";
		InvoiceOrderToHeader  = "";
		FromHeader  = "";
		Date= "";
		OrderNo  = "";
		RefNo   = "";
		Terms   = "";
		TermsDetails ="";
		SoNo=""; 
		Item   = "";
		Description ="";
		OrderQty =""; 
		UOM =""; 
		Footer1="";
		Footer2="";
                Footer3="";
		Footer4="";
	}  
%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post"><br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Edit Mobile Registration Order Printout</font></TH>
	</TR>
</TABLE>
<B>
<CENTER><%=res%>

</B> <br>
<INPUT name="OrderType" type="hidden" value="MOBILE REGISTRATION" >
<TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT"> Order Header :</TH>
		<TD><INPUT name="OutboundOrderHeader" type="TEXT" value="<%=InboundOrderHeader%>"
			size="50" MAXLENGTH=20></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Invoice Order To Header  :</TH>
		<TD><INPUT name="InvoiceOrderToHeader" type="TEXT" value="<%=InvoiceOrderToHeader%>"
			size="50" MAXLENGTH=100></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">From Header </TH>
		<TD><INPUT name="FromHeader" type="TEXT" value="<%=FromHeader%>"
			size="50" MAXLENGTH=100></TD>
	</TR>

        <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Date</TH>
		<TD><INPUT name="Date" type="TEXT" value="<%=Date%>"
			size="50" MAXLENGTH=100></TD>
	</TR>
	 <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Order No </TH>
		<TD><INPUT name="OrderNo" type="TEXT" value="<%=OrderNo%>"
			size="50" MAXLENGTH=100></TD>
	</TR>
	 <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Ref No </TH>
		<TD><INPUT name="RefNo" type="TEXT" value="<%=RefNo%>"
			size="50" MAXLENGTH=100></TD>
	</TR>
	 <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">SoNo </TH>
		<TD><INPUT name="SoNo" type="TEXT" value="<%=SoNo%>"
			size="50" MAXLENGTH=100></TD>
	</TR>
	 <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Product ID </TH>
		<TD><INPUT name="Item" type="TEXT" value="<%=Item%>"
			size="50" MAXLENGTH=100></TD>
	</TR>
	
	 <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Description </TH>
		<TD><INPUT name="Description" type="TEXT" value="<%=Description%>"
			size="50" MAXLENGTH=100></TD>
	</TR>
	
	 <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Order Qty  </TH>
		<TD><INPUT name="OrderQty" type="TEXT" value="<%=OrderQty%>"
			size="50" MAXLENGTH=100></TD>
	</TR>
	
	 <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">UOM </TH>
		<TD><INPUT name="UOM" type="TEXT" value="<%=UOM%>"
			size="50" MAXLENGTH=100></TD>
	</TR>
	

	
	 <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Footer </TH>
		<TD><INPUT name="Footer1" type="TEXT" value="<%=Footer1%>"
			size="50" MAXLENGTH=100>&nbsp;&nbsp;&nbsp;&nbsp;(100 chracters max)</TD>
	</TR>
	
	 <TR>
		<TH WIDTH="35%" ALIGN="RIGHT"> </TH>
		<TD><INPUT name="Footer2" type="TEXT" value="<%=Footer2%>"
			size="50" MAXLENGTH=100>&nbsp;&nbsp;&nbsp;&nbsp;(100 chracters max)</TD>
	</TR>
        <TR>
		<TH WIDTH="35%" ALIGN="RIGHT"> </TH>
		<TD><INPUT name="Footer3" type="TEXT" value="<%=Footer3%>"
			size="50" MAXLENGTH=50>&nbsp;&nbsp;&nbsp;&nbsp;(50 chracters max)</TD>
	</TR>
        <TR>
		<TH WIDTH="35%" ALIGN="RIGHT"> </TH>
		<TD><INPUT name="Footer4" type="TEXT" value="<%=Footer4%>"
			size="50" MAXLENGTH=50>&nbsp;&nbsp;&nbsp;&nbsp;(50 chracters max)</TD>
	</TR>

	<TR>
		<TD COLSPAN=2><BR>
		</TD>
	</TR>
	<TR>
		<TD COLSPAN=2>
		<center><INPUT class="Submit" type="BUTTON" value="Back"
			onClick="window.location.href='../home'">&nbsp;&nbsp; <INPUT
			class="Submit" type="BUTTON" value="Clear" onClick="onClear();"
			<%=sNewEnb%>>&nbsp;&nbsp; <INPUT class="Submit" type="BUTTON"
			value="Save" onClick="onAdd();" >&nbsp;&nbsp;
		</TD>
	</TR>
</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

