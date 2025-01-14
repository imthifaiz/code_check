<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.DoHdrDAO"%>
<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.track.dao.CustMstDAO"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Additional Details</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

	
	function onChkStatus()
	{
		 //document.form.action  ="/track/CatalogServlet?Submit=CHKORDER_STATUS";	
		 document.form.action  ="mobileOrderStatus.jsp?action=CHKORDER_STATUS";	 
		 document.form.submit();
		 
	}	
	function onBack(pageName)
	{
		var pagename = pageName;		
		 var plant =document.form.PLANT.value;		
		 var scanid = document.form.SCANID.value;
		 var index = document.form.INDEX.value;
		 var delim="AAABBACCB";
		if(pagename=='mobileshopping'){
	document.form.action  ="mobileShopping.jsp?ID="+plant+delim+scanid+delim+"N"+delim+index;
	document.form.submit();
		}
		
	}
</script>
<%
String plant="";
if(request.getParameter("PLANT")!=null)
	 plant = request.getParameter("PLANT");
      List orderlist=null;
	String res = "",sNewEnb="enabled";
		String  mobileno="",Item ="",action = "";
	String Description = "", OrderQty  = "",sQuery="",pagename="",
      	   TotalTax="",Total="",mobileNo="",orderno="",scanid="",index="";
	mobileno = StrUtils.fString(request.getParameter("MOBILENO"));
	orderno = StrUtils.fString(request.getParameter("ORDERNO"));
     orderlist = (ArrayList)session.getAttribute("MBORDERLIST");
     pagename = StrUtils.fString(request.getParameter("PAGENAME"));
     scanid = StrUtils.fString(request.getParameter("SCANID"));
     index= StrUtils.fString(request.getParameter("INDEX"));
  	res = StrUtils.fString(request.getParameter("result"));	
  	boolean existFlag=true,existmobflg=true;
  	DoHdrDAO dohdrdao = new DoHdrDAO();
   CustMstDAO	_custmst = new CustMstDAO();
	try {
		Hashtable htutil = new Hashtable();
		action = StrUtils.fString(request.getParameter("action"));
		if(action.equalsIgnoreCase("CHKORDER_STATUS")){
		htutil.put("DONO",orderno);
		htutil.put("PLANT",plant);
		
		//check valid order no
		if(orderno.length()>0)
	 	existFlag = dohdrdao.isExisit(htutil, "");
		
		//check for valid mobile no
		htutil.remove("DONO");
		htutil.put("USER_ID",mobileno);
		
		if(mobileno.length()>0)
		existmobflg= _custmst.isExisit(htutil, "");
				
		if(!existFlag){
			res= "Not a valid order number";
		}
		else if(!existmobflg){
			res="Not a valid mobile number";
		}
		else{
		htutil.remove("USER_ID");
		htutil.put("DONO",orderno);
		htutil.put("MOBILENO",mobileno);
	
		orderlist= dohdrdao.OrderStatus(htutil, " order by dono desc");
	}}} catch (Exception e) {
	}

	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		
	}  
%>


<BODY >
<FORM name="form" method="post" action="" ><br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
 <TH BGCOLOR="#669900" COLSPAN="11"><font color="white" size="7">Mobile Order Status</font></TH>
	</TR>
</TABLE>

<font size=5><%=res%></font>	
 <br>
	<input type="hidden" name="SCANID" id="SCANID" value="<%=scanid%>">
	<input type="hidden" name="INDEX" id="INDEX" value="<%=index%>">
	<input type="hidden" name="PLANT" id="PLANT" value="<%=plant%>">
	<input type="hidden" name="PAGENAME" id="PAGENAME" value="<%=pagename%>">
<TABLE border="0" CELLSPACING="0" WIDTH="100%" bgcolor="#ffffff" align="center">
	<TR>
		<TH WIDTH="45%" ALIGN="RIGHT"></TH>
		<TD align="left" ></TD>
	</TR>
	<TR>
		<TH  ALIGN="RIGHT" WIDTH="45%"><font size="7">Mobile Number</font> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
		<TD align="left" ><INPUT type="text" class="inactivemobile" name="MOBILENO" id="MOBILENO"  value="<%=mobileNo%>"> </TD>
	</TR><TR>
		<TH  ALIGN="RIGHT" WIDTH="45%"></TH>
		<TD align="left"><font size="5"> OR </font></TD>
	</TR><TR>
		<TH  ALIGN="RIGHT" WIDTH="45%"><font size="7">Order Number</font>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
		<TD align="left"><INPUT type="text"  class="inactivemobile" name="ORDERNO" id="ORDERNO" name="ORDERNO" value="<%=orderno %>"></TD>
	</TR>
	
	<TR>
		<TD COLSPAN=2>
		
		</TD>
	</TR>
	<TR>
		<TH WIDTH="45%" ALIGN="right"><input type="button" name="Back" class="mobileButton"  value="Back" onclick="onBack('<%=pagename%>');">&nbsp;&nbsp;</TH>
		<TD align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="Submit" class="mobileButton"  value="Submit" onclick="onChkStatus();">&nbsp;&nbsp;</TD>
	</TR>
	
	<TR height="25px">
		<TD COLSPAN=2>&nbsp;
		</TD>
	</TR>
	<TR height="25px">
		<TD COLSPAN=2>&nbsp;
		</TD>
	</TR>
</TABLE>
<%if(orderlist!=null&&!orderlist.isEmpty()) {%>
<% Map linearr = (Map)orderlist.get(0); %>
<TABLE WIDTH="70%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
     <TR BGCOLOR="#669900">
         <TH><font  align="center" color="#ffffff" size="7"><font size="6">Order Number</font></TH>
         <TH><font color="#ffffff" align="left" size="7"><b><font size="6">Status</font></TH>
          
      </TR>
   	 
          <TR >
	           
	            <TD align="left" > &nbsp;<font class="mobilelabel" size="6"><%=(String)linearr.get("DONO")%></font></TD>
	    	    <TD align="left" > &nbsp;<font class="mobilelabel" size="6">&nbsp; <%=(String)linearr.get("STATUS")%></TD>
                    
          </TR>
     
       </TABLE>
       <% } %>
</CENTER>

</FORM>
</BODY>
</HTML>

