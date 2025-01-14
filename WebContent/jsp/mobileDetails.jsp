<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Additional Details</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
	
	function onClear()
	{
		document.form.DETAIL1.value="";
		document.form.DETAIL2.value="";
		document.form.DETAIL3.value="";
		document.form.DETAIL4.value="";
		document.form.DETAIL5.value="";
		document.form.DETAIL6.value="";
		document.form.DETAIL7.value="";
	}

	function onAdd(){
     	
   		document.form.action  =  "/track/instructions?Submit=EDIT_DETAIL";
   		
   	    document.form.submit();
   	
	}
	

</script>
<%
	
	String plant = request.getParameter("PLANT");
	//String sUserId = (String) session.getAttribute("LOGIN_USER");
    String productid = request.getParameter("CATALOGID");    
	String res = "",sNewEnb="enabled";
	
	String action = "";
	
	String RefNo   = "", Terms  = "", TermsDetails   = "",pagename="",SoNo ="",scanid="",index="",Item ="";
	String Description = "", OrderQty  = "",sQuery="",
      	   TotalTax="",Total="";
	StringBuffer detail1,detail2,detail3,detail4,detail5,detail6,detail7; 
	StrUtils strUtils = new StrUtils();
	detail1 = new StringBuffer();
	detail2 = new StringBuffer();
	detail3 = new StringBuffer();
	detail4 = new StringBuffer();
	detail5 = new StringBuffer();
	detail6 = new StringBuffer();
	detail7 = new StringBuffer();
    res =  strUtils.fString(request.getParameter("result"));
    pagename = strUtils.fString(request.getParameter("PAGENAME"));
    
	CatalogUtil catlogUtil = new CatalogUtil();
	Hashtable ht = new Hashtable();
	ht.put(IConstants.PLANT,plant);
	ht.put(IConstants.PRODUCTID,productid);
	sQuery ="ISNULL(DETAIL1,'') DETAIL1,ISNULL(DETAIL2,'') DETAIL2,ISNULL(DETAIL3,'') DETAIL3,ISNULL(DETAIL4,'') DETAIL4,";
	sQuery = sQuery +"ISNULL(DETAIL5,'') DETAIL5,ISNULL(DETAIL6,'') DETAIL6,ISNULL(DETAIL7,'') DETAIL7";
         List m= catlogUtil.listCatalogDet(sQuery,ht,"");
         
         for(int i=0;i<m.size();i++)
         {
        	Map insmp= (Map)m.get(i);
         detail1.append(insmp.get("DETAIL1"));
         detail2.append(insmp.get("DETAIL2"));
         detail3.append(insmp.get("DETAIL3"));
         detail4.append(insmp.get("DETAIL4"));
         detail5.append(insmp.get("DETAIL5"));
         detail6.append(insmp.get("DETAIL6"));
         String urlcheck = (String)insmp.get("DETAIL7");
         boolean flag = urlcheck.startsWith("http://");
         System.out.println("detail7 flag"+flag);
         if(flag){
        	 urlcheck= urlcheck.replaceFirst("http://","");
        	 urlcheck= urlcheck.trim();
         detail7.append(urlcheck);
         }
         else{
        	 detail7.append(insmp.get("DETAIL7"));	
         }
         	 
         }    
      

	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}

	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		
	}  
%>


<BODY >
<FORM name="form" method="post" ><br>
<CENTER>
<input type="hidden" name="PAGENAME" value="<%=pagename%>">
<input type="hidden" name="PRODUCTID" value="<%=productid%>">
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
 <TH BGCOLOR="#669900" COLSPAN="11"><font color="white" size="12">DETAILS</font></TH>
	</TR>
</TABLE>
<B>
<CENTER><%=res%>


	
	
</B> <br>
<TABLE border="0" CELLSPACING="0" WIDTH="100%"  align="center">
	<TR>
		<TH WIDTH="25%" ALIGN="right"></TH>
		<TD align="left"><p><font size="8"> <%=detail1.toString()%></font></p></TD>
	</TR>
	<TR>
		<TH WIDTH="25%" ALIGN="right"></TH>
		<TD align="left"><p><font size="8"> <%=detail2.toString()%></font></p></TD>
	</TR><TR>
		<TH WIDTH="25%" ALIGN="right"></TH>
		<TD align="left"><p><font size="8"> <%=detail3.toString()%></font></p></TD>
	</TR><TR>
		<TH WIDTH="25%" ALIGN="right"></TH>
		<TD align="left"><p><font size="8"> <%=detail4.toString()%></font></p></TD>
	</TR><TR>
		<TH WIDTH="25%" ALIGN="right"></TH>
		<TD align="left"><p><font size="8"> <%=detail5.toString()%></font></p></TD>
	</TR>
	<TR>
		<TH WIDTH="25%" ALIGN="right"></TH>
		<TD align="left"><p><font size="8"> <%=detail6.toString()%></font></p></TD>
	</TR>
	<TR>
		<TH WIDTH="25%" ALIGN="right"></TH>
		<TD align="left"><a href="http://<%=detail7.toString()%>"   target="main"><font size="8" color="Blue"><%=detail7.toString()%></font></a></TD>
	</TR>
	<TR>
		<TD COLSPAN=2><BR>
		</TD>
	</TR>
	<TR>
		<TD COLSPAN=2 ALIGN="left">
		<input type="button" name="Back" class="mobileButton"  value="Back" onclick="onPrevious();">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</TD>
	</TR>
	<TR>
		<TD COLSPAN=2>&nbsp;
		</TD>
	</TR>
	<TR>
		<TD COLSPAN=2>&nbsp;
		</TD>
	</TR>
</TABLE>
</CENTER>

</FORM>
</BODY>
<script>
function onPrevious(){
	 var plant ="<%=plant%>";
	 var pagename= document.form.PAGENAME.value;
	 var productid= document.form.PRODUCTID.value;
	 var delim="AAABBACCB";
	if(pagename=='mobileshopping'){
	 window.history.back();   	
	}
	else if(pagename=='mobileenquiry'){
		window.history.back(); 	
	}
	else if(pagename=='shopimage'){
		document.form.action  ="catalogImage.jsp?PAGE=EDIT_LOG&PLANT="+plant+"&PRODUCTID="+productid;
   	    document.form.submit();		
	}
	else if(pagename=='enquiryimage'){
		document.form.action  ="catalogEnqImage.jsp?PAGE=EDIT_LOG&PLANT="+plant+"&PRODUCTID="+productid;
   	    document.form.submit();		
	}
	else if(pagename=='registerimage'){
		document.form.action  ="catalogRegisterImage.jsp?PAGE=EDIT_LOG&PLANT="+plant+"&PRODUCTID="+productid;
   	    document.form.submit();		
	}
	else if(pagename=='browseimage'){
		document.form.action  ="catalogBrowseImage.jsp?PAGE=EDIT_LOG&PLANT="+plant+"&PRODUCTID="+productid;
   	    document.form.submit();		
	}
	}

</script>
</HTML>

