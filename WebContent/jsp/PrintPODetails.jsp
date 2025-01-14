<%@page import="com.track.dao.PlantMstDAO"%>
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
String title = "Purchase Order Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
 <jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
   <jsp:param name="submenu" value="<%=IConstants.PURCHASE_REPORTS%>"/>
</jsp:include>
<script language="javascript">

</script>

<script language="JavaScript" type="text/javascript"
	src="js/calendar.js"></script>

<div class="container-fluid m-t-20">
	<div class="box">
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post">
<br>
  <%
StrUtils _strUtils = new StrUtils();
ShipHisDAO _ShipHisDAO = new ShipHisDAO();
_ShipHisDAO.setmLogger(mLogger);
ArrayList movQryList =  null;
POUtil poUtil = new POUtil();

session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
    String userID = (String) session.getAttribute("LOGIN_USER");
	String PONO="",INVOICE="",fdate="",tdate="";
	PONO = _strUtils.fString(request.getParameter("PONO"));
	INVOICE = _strUtils.fString(request.getParameter("INVOICE"));
	fdate = _strUtils.fString(request.getParameter("FROMDATE"));
	tdate = _strUtils.fString(request.getParameter("TODATE"));
	
	%>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"	>
	<TR>
		<TH  align="left"><b>Purchase Order No : <%=PONO%></b></TH>
	</TR>
</TABLE>


<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	align="center" class="table">
	<thead style="background-color: #eaeafa;">
	<TR>
			<th align="left">Line No</th>
           <td align="left"><b>Product ID</b></td>
           <td align="left"><b>Description</b></td>
              <td align="left"><b>Received Date</b></td>
           <td align="left"><b>Order Qty</b></td>
           <td align="left"><b>Received Qty</b></td>
           <td align="left"><b>UOM</b></td>
           <%
           		if(INVOICE.equalsIgnoreCase("Y")){
           %>
           <td align="left"><b>Unit Cost</b></td>
           <td align="left"><b>Cost</b></td>
           <td align="left"><b>Currency</b></td>
         <%} %>
         <td align="left"><b>Status</b></td>
	</tr>
	</thead>
	<tbody>

	<%
	
	try{
		String dtCondStr =    " and ISNULL(recvdate,'')<>'' AND CAST((SUBSTRING(recvdate, 7, 4) + '-' + SUBSTRING(recvdate, 4, 2) + '-' + SUBSTRING(recvdate, 1, 2)) AS date)";
        String sCondition=""; 
        PlantMstDAO plantMstDAO = new PlantMstDAO();
        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		if (fdate.length() > 0) {
        	sCondition = sCondition +dtCondStr+ " >= '" 
					+ fdate
					+ "'  ";
			if (tdate.length() > 0) {
				sCondition = sCondition + dtCondStr + " <= '" 
				+ tdate
				+ "'  ";
				}
        } else {
			if (tdate.length() > 0) {
				sCondition = sCondition +dtCondStr+ "  <= '" 
				+ tdate
				+ "'  ";
				}
        }  
		movQryList =  poUtil.listPODetilstoPrintNew(plant,PONO,sCondition);
	
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			//k=k+1;
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#FFFFFF";
			
			String ordQtyValue =(String)lineArr.get("ORDQTY");
			String recQtyValue =(String)lineArr.get("RECQTY");
			String unitCostValue = (String)lineArr.get("UCOST");
			String costValue = (String)lineArr.get("COST");
			
			float ordQtyVal="".equals(ordQtyValue) ? 0.0f :  Float.parseFloat(ordQtyValue);
			float recQtyVal="".equals(recQtyValue) ? 0.0f :  Float.parseFloat(recQtyValue);
			/* float unitCostVal="".equals(unitCostValue) ? 0.0f :  Float.parseFloat(unitCostValue);
			float costVal="".equals(costValue) ? 0.0f :  Float.parseFloat(costValue); */
			double unitCostVal ="".equals(unitCostValue) ? 0.0d :  Double.parseDouble(unitCostValue);
            double costVal ="".equals(costValue) ? 0.0d :  Double.parseDouble(costValue);
			
			
			if(ordQtyVal==0f){
				ordQtyValue="0.000";
			}else{
				ordQtyValue=ordQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			}if(recQtyVal==0f){
				recQtyValue="0.000";
			}else{
				recQtyValue=recQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			}/* if(unitCostVal==0f){
				unitCostValue="0.00000";
			}else{
				unitCostValue=unitCostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			}if(costVal==0f){
				costValue="0.00000";
			}else{
				costValue=costValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
			} */
			unitCostValue = StrUtils.addZeroes(unitCostVal, numberOfDecimal);
			costValue = StrUtils.addZeroes(costVal, numberOfDecimal);
			
	%>
	

	<TR bgcolor="<%=bgcolor%>">
		<TD align="left"> <%=(String)lineArr.get("POLNO")%></TD>
                <TD align="left"><%=(String)lineArr.get("ITEM")%></TD>
                <TD align="left"><%=(String)lineArr.get("ITEMDESC")%></TD>
                <TD align="left"><%=(String)lineArr.get("RECVDATE")%></TD> 
                <TD align="right"><%=ordQtyValue%></TD>
                <TD align="right"><%=recQtyValue%></TD>
                <TD align="left"><%=(String)lineArr.get("UOM")%></TD>
                <%
           			if(INVOICE.equalsIgnoreCase("Y")){
         	    %>
                <TD align="right"><%=unitCostValue%></TD>
                <TD align="right"><%=costValue%></TD>
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
  	<button type="button" class="Submit btn btn-default"  value="PrintWithBatch/Sno"  name="action" onclick="window.location.href='javascript:history.back()'"><b>Back</b></button>
  	
  	</div>
  	</div>


</FORM>
</div></div></div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="nobackblock" value="1" />
</jsp:include>