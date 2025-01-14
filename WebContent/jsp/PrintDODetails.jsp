<%@ page import="com.track.dao.ShipHisDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Sales Order Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
<jsp:param name="mainmenu" value="<%=IConstants.SALES_REPORTS%>"/>
</jsp:include>
<script language="javascript">

</script>

<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>

<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>  
                <!-- <li><a href="../salesorder/pdfwithoutprice"><span class="underline-on-hover">Sales Order Summary</span> </a></li> -->                
                <li><a href="javascript:history.back()"><span class="underline-on-hover">Sales Order Summary</span> </a></li>                
                <li><label>Sales Order Details</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right"  value="PrintWithBatch/Sno"  name="action" onclick="window.location.href='javascript:history.back()'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post">

  <%
StrUtils _strUtils = new StrUtils();
ShipHisDAO _ShipHisDAO = new ShipHisDAO();
_ShipHisDAO.setmLogger(mLogger);
ArrayList movQryList =  null;
DOUtil doUtil = new DOUtil();

session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
    String userID = (String) session.getAttribute("LOGIN_USER");
	String DONO="",INVOICE="",TYPE="",FROMDATE="",TODATE="";
	DONO = _strUtils.fString(request.getParameter("DONO"));
	INVOICE = _strUtils.fString(request.getParameter("INVOICE"));
	TYPE = _strUtils.fString(request.getParameter("TYPE"));
	FROMDATE = _strUtils.fString(request.getParameter("FROMDATE"));
	TODATE = _strUtils.fString(request.getParameter("TODATE"));
	String fdate="",tdate="";
    if (FROMDATE.length()>5)
        fdate    = FROMDATE.substring(6)+"-"+ FROMDATE.substring(3,5)+"-"+FROMDATE.substring(0,2);

       if(TODATE==null) TODATE=""; else TODATE = TODATE.trim();
       if (TODATE.length()>5)
       tdate    = TODATE.substring(6)+"-"+ TODATE.substring(3,5)+"-"+TODATE.substring(0,2);
	%>

<br>
<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	 >
	
	<TR>
		<TH align="left"><b>Sales Order No : <%=DONO%></b></TH>
	</TR>
	</TABLE>
	<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	align="center" class="table">
	<thead>
	<TR style="background-color: #eaeafa;">
			<th align="left">Line No</th>
           <td align="left"><b>Product ID</b></td>
           <td align="left"><b>Description</b></td>
           <td align="left"><b>Date</b></td>
           <td align="left"><b>Order Qty</b></td>
           <td align="left"><b>Pick Qty</b></td>
           <td align="left"><b>Issue Qty</b></td>
           <td align="left"><b>UOM</b></td>
           <%
           		if(INVOICE.equalsIgnoreCase("Y")){
           %>
           <td align="left"><b>Unit Price</b></td>
           <td align="left"><b>Price</b></td>
           <td align="left"><b>Currency</b></td>
         <%} %>
         <td align="left"><b>Status</b></td>
	</tr>
	</thead>
<tbody>
	<%
	
	try{
			if(TYPE.equalsIgnoreCase("PICKISSUE")){
				movQryList =  doUtil.listDODetilstoPrintshiphis(plant,DONO,fdate,tdate);
			}
			else{
				movQryList =  doUtil.listDODetilstoPrintNew(plant,DONO,fdate,tdate);
			}
	
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			//k=k+1;
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#FFFFFF";
			
	%>
	

	<TR bgcolor="<%=bgcolor%>">
		<TD align="left"> <%=(String)lineArr.get("DOLNO")%></TD>
                <TD align="left"><%=(String)lineArr.get("ITEM")%></TD>
                <TD align="left"><%=(String)lineArr.get("ITEMDESC")%></TD>
                <TD align="left"><%=(String)lineArr.get("Trandate")%></TD>
                <TD align="left"><%=StrUtils.formatNum((String)lineArr.get("ORDQTY"))%></TD>
                <TD align="left"><%=StrUtils.formatNum((String)lineArr.get("PICKQTY"))%></TD>
                <TD align="left"><%=StrUtils.formatNum((String)lineArr.get("ISSUEQTY"))%></TD>
                <TD align="left"><%=(String)lineArr.get("UOM")%></TD>
                <%
           			if(INVOICE.equalsIgnoreCase("Y")){
         	    %>
                <TD align="right"><%=StrUtils.currencyWtoutSymbol((String)lineArr.get("UPRICE"))%></TD>
                <TD align="right"><%=StrUtils.currencyWtoutSymbol((String)lineArr.get("PRICE"))%></TD>
                <TD align="left"><%=(String)lineArr.get("CurrencyID")%></TD>
                <%} %>
				<TD align="left"><%=(String)lineArr.get("STATUS")%></TD>
	</TR>
 
<%

		
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
	
%>
	
</tbody>
</TABLE>
	<div class="form-group">
  	<div class="col-sm-12" align="center">   
<!--   	<button type="button" class="Submit btn btn-default"  value="PrintWithBatch/Sno"  name="action" onclick="window.location.href='javascript:history.back()'"><b>Back</b></button> -->
  	  	</div>
  	</div>



</FORM>
</div></div></div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="nobackblock" value="1" />
</jsp:include>