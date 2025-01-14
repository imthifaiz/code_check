<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*" session="true"%>
<%@ include file="header.jsp"%>

<%@page import="com.track.dao.ItemSesBeanDAO"%>
<%@page import="com.track.tables.ITEMMST"%>
<%@page import="com.track.dao.SalesDetailDAO"%>
<%@page import="java.text.DecimalFormat"%><html>
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<head>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
  
 function Reprint() 
 {
	 document.form.action="/track/PosReport?action=Reprint";
	 document.form.submit();
 }
  function ExportReport()
  {
     var flag    = "false";
     document.form.TRANID.value;
   
   
    document.form.action="posExcel.jsp";
     
    document.form.submit();
    
  }
</script>
<title> Point Of Sales</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	_userBean.setmLogger(mLogger);
	SalesDetailDAO saledao = new SalesDetailDAO();
	String fieldDesc="";
	String PLANT="",ITEM ="",ITEM_DESC="",SCANQTY="",action="",LOC="";//added by deen
	String html = ""; 
	int Total=0; float sumSubTotal=0;
	DecimalFormat decformat = new DecimalFormat("#,##0.00");
	PLANT = (String)session.getAttribute("PLANT");
	LOC = request.getParameter("LOC").trim();//added by deen
	if(request.getParameter("action")!=null)
	{action =request.getParameter("action");
	
	}
	String SumColor=""; 
	boolean flag=false; List saleslist = new ArrayList();
	String gst = sb.getGST("GST",PLANT);
	
	String tranid = request.getParameter("Tranid").trim();
	
	if(tranid!=null ||tranid.length()>0)
		saleslist=	saledao.getSalesByTranid(PLANT,tranid,"");
	if(action.equalsIgnoreCase("print"))
System.out.println("Inside print fn");		

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post"  >
  <br>
  
  <input type="hidden" name="TRANID" value='<%=tranid%>' ></input>
  <input type="hidden" name="LOC" value='<%=LOC%>' ></input> <!--added by deen-->
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Point Of Sales Details</font></TH>
    </TR>
  </TABLE>
    <br>
  <center>
   
   </center>
  <TABLE border="0" width="40%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
   
  </TABLE>
  <br>
 <TABLE WIDTH="50%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
     <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="left"><b>Receipt No</TH>
         <TH><font color="#ffffff" align="left"><b>Product ID</TH>
          <TH><font color="#ffffff" align="left"><b>Product Description</TH>
          <TH><font color="#ffffff" align="left"><b>Qty</TH>
          <TH><font color="#ffffff" align="left"><b>Price</TH>
          
      </TR>
      <% if(saleslist!=null && (saleslist.size()>0)){
         for(int k=0;k<saleslist.size();k++)
         {
        	 
        	 Map vecitem =(Map)saleslist.get(k);
        // sumSubTotal = sumSubTotal+itemord.getUNITPRICE();
         //sumSubTotal = strUtils.Round(sumSubTotal,2);
         float unitpc = Float.parseFloat((String)vecitem.get("unitprice"));
        
         unitpc = strUtils.Round(unitpc,2);
      %>
   	  
          <TR bgcolor = "">
	            <TD align="left" > &nbsp;<font class="textbold"><%=tranid%></TD>
	            <TD align="left" > &nbsp;<font class="textbold"><%=vecitem.get("item")%></TD>
	    	    <TD align="left" class="textbold">&nbsp; <%=vecitem.get("itemdesc")%></TD>
	    	    <TD align="center" class="textbold">&nbsp;<%=vecitem.get("qty")%> </TD> 
	            <TD align="center" class="textbold">&nbsp;<%=DbBean.CURRENCYSYMBOL%><%if(unitpc==0){%>0.00<%}else{%><%=decformat.format(unitpc)%><%}%></TD>
	            
          </TR><%}} %>
          
	    	    <% String total="";
	    	  //  float gstval=Float.parseFloat(gst);
	    	    //float totalsum = sumSubTotal + sumSubTotal*gstval/100;
	    	    	//  totalsum = strUtils.Round(totalsum,2); 	    
	    	    %>
	      
            </TABLE>
          <table align="center">
	<TR>
		
		<td><input type="button" value="ExportReport"
			onClick="javascript:ExportReport();"></td>
			 <td  ><input type="Button" name="print" value="RePrint" onclick="Reprint();"></td>
		
	</TR>
</table>
 
  </FORM>
  
<%@ include file="footer.jsp"%>
<script>
//function Reprint() //added by deen
//{
	
	//var loc = "<%=LOC%>";
	// document.form.action="/track/PosReport?action=Reprint&LOC="+loc;
	// document.form.submit();
//}

</script>