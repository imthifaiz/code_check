<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*" %>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Pick Reversal</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">
function onReverse(rowId,pickQty)
{
	
	var fromloc = document.getElementById("row_" + rowId).getElementsByTagName("td")[0].innerHTML;
	var toloc = document.getElementById("row_" + rowId).getElementsByTagName("td")[1].innerHTML;
	var batch = document.getElementById("row_" + rowId).getElementsByTagName("td")[2].innerHTML;
	var reverseQty = document.getElementById("Reverse_" + rowId).value;
	if(!IsNumeric(reverseQty)){
		alert("Please enter a valid Qty to reverse");
		return false;
	}
	if(reverseQty > pickQty){
		alert("Cannot reverse more than pick QTY");
		return false;
	}
	document.form.reverseQty.value = reverseQty;
	document.form.FROMLOC.value = fromloc;
	document.form.TOLOC.value = toloc;
	document.form.BATCH.value = batch;
	document.form.ACTION.value = "PICK_REVERSAL_RANDOMISSUE";
	document.form.submit();
}

function onClose(){
	
	window.opener.onGo();
	window.close();
}

</script>
<%
ShipHisDAO shipDao = new ShipHisDAO();
DOUtil _DOUtil = new DOUtil();	
TempUtil _TempUtil = new TempUtil();
String item = "", tono = "",tolno = "",fieldDesc="";

StrUtils strUtils = new StrUtils();
String plant=(String)session.getAttribute("PLANT");
String username=(String)session.getAttribute("LOGIN_USER");
String action   = strUtils.fString(request.getParameter("action")).trim();
item     = strUtils.fString(request.getParameter("ITEM"));
tono     = strUtils.fString(request.getParameter("TONO"));
tolno     = strUtils.fString(request.getParameter("TOLNO"));


if(action.equalsIgnoreCase("result"))
{
   fieldDesc=(String)request.getSession().getAttribute("RESULT");
   fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
}
else if(action.equalsIgnoreCase("resulterror"))
{

 fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
 fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
}

%>

<FORM name="form" method="post" action = "/track/TransferOrderHandlerServlet" >
<table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
<tr><td>ORDER NO : &nbsp;&nbsp;<input type = "text" name = "TONO" READONLY value = "<%=tono %>"></td>
<td>ITEM : &nbsp;&nbsp;<input type = "text" name = "ITEM" READONLY value = "<%=item %>"></td>
</tr>
</table>
<br>
<CENTER>
<font face="Times New Roman" size="4">
<table width="60%" border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<%=fieldDesc%>
</table>
</font>
</CENTER>
<br>
<table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
    		 <TH ><font color="white" align="left"><b>From Loc</TH>
    		 <TH ><font color="white" align="left"><b>To Loc</TH>
            <TH><font color="#ffffff" align="left"><b>Batch</TH>
            <TH ><font color="white" align="left"><b>Pick Qty</TH>
            <TH></TH>           
    </TR>  
    <input type = "hidden" name = "ACTION" value = "">
      <input type = "hidden" name = "BATCH" value = "">
     <input type = "hidden" name = "FROMLOC" value = "">
     <input type = "hidden" name = "TOLOC" value = "">
       <input type = "hidden" name = "reverseQty" value = "">
					<%
					
					Hashtable<String,String> ht = new Hashtable<String,String>();
		   			ht.put("PLANT", plant);
		   			ht.put("ORDERNO",tono);
		   			ht.put("ITEM",item );
		   			String extCond = "group by  LOC,LOC1,BATCH,QTY having  sum(QTY) > 0 " ;
		   		    String query = " LOC,LOC1,BATCH,ISNULL(QTY,0) TOTPCKQTY " ;
		   			ArrayList al = _TempUtil.listtempdata(query,ht,extCond);				
					      
						if (al.size() > 0) {
							for (int i = 0; i < al.size(); i++) {
								 Map lineArr = (Map) al.get(i);
								 String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#dddddd";								
					%>
					<tr bgcolor=<%=bgcolor %> id = "row_<%= (i+1)%>">
                          <td  align = left><%=StrUtils.fString((String)lineArr.get("LOC"))%></td>
                          <td  align = left><%=StrUtils.fString((String)lineArr.get("LOC1"))%></td>
                          <td  align = left><%=StrUtils.fString((String)lineArr.get("BATCH")) %></td>
                          <td   align = right><input type = "text"  id = "Reverse_<%=(i+1)%>" value = '<%= StrUtils.fString((String)lineArr.get("TOTPCKQTY"))%>' READONLY  style= "border: 0px;background-color:#FFFFFF" /></td>
                          <td   align = left><input type = "button" value = "Reverse" onClick = "onReverse(<%=(i+1) %>,<%= StrUtils.fString((String)lineArr.get("TOTPCKQTY"))%>);" ></td>
                </tr>

					<%
						}
						} else {
					%>

					<TR>
						<TD align="center" width="30%">Data's Not Found For Receiving</TD>
					</TR>
					
					
					<%
						}
					%>
				</table>
				<br><br>
				 <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  <input type="button"  value="Close" onClick="onClose();">
                </td>
              </tr>
            </table>
          <% request.getSession().setAttribute("RESULT",""); %>  
</FORM>
</BODY>
</HTML>

