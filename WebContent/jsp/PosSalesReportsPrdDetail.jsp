<%@ page import="com.track.dao.ShipHisDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Sales Report Detail";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="mainmenu" value="<%=IConstants.POS_REPORT%>"/>
</jsp:include>
<script language="javascript">

</script>

<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>

<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>  
                <li><a href="../posreports/salesreports"><span class="underline-on-hover">POS Sales Report</span> </a></li>                
                <li><label>Sales Report Details</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class=" pull-right">
				<div class="btn-group" role="group">
            	<button type="button" class="btn btn-default printMe" onclick="PrintTable();"
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
            	</div>
              <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right"  value="PrintWithBatch/Sno"  name="action" onclick="window.location.href='javascript:history.back()'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		 	  </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post">

  <%
StrUtils _strUtils = new StrUtils();
ShipHisDAO _ShipHisDAO = new ShipHisDAO();
HTReportUtil movHisUtil       = new HTReportUtil();
_ShipHisDAO.setmLogger(mLogger);
ArrayList movQryList =  null;
DOUtil doUtil = new DOUtil();

session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
    String userID = (String) session.getAttribute("LOGIN_USER");
	String DONO="",INVOICE="",TYPE="",FROMDATE="",TODATE="",ITEM="";
	DONO = _strUtils.fString(request.getParameter("DONO"));
	ITEM = _strUtils.fString(request.getParameter("ITEM"));
	INVOICE = _strUtils.fString(request.getParameter("INVOICE"));
	TYPE = _strUtils.fString(request.getParameter("TYPE"));
	FROMDATE = _strUtils.fString(request.getParameter("FROMDATE"));
	TODATE = _strUtils.fString(request.getParameter("TODATE"));
	String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
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
		<TH align="left"></TH>
	</TR>
	</TABLE>
	<div id="dvContents" >
	<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	align="center" class="table">
	<thead>
	<TR style="background-color: #eaeafa;">
		   <td style="text-align: center"><b>Order No</b></td>
           <td style="text-align: center"><b>Date</b></td>
           <td style="text-align: center"><b>Customer</b></td>
           <td style="text-align: center"><b>Qty</b></td>
           <td style="text-align: center"><b>Discount</b></td>
           <td style="text-align: center"><b>Amount</b></td>
	</tr>
	</thead>
<tbody>
	<%
	try{
			if(TYPE.equalsIgnoreCase("PICKISSUE")){
				movQryList =  doUtil.listDODetilstoPrintshiphis(plant,DONO,fdate,tdate);
			}else if(TYPE.equalsIgnoreCase("Product")){
				 movQryList = movHisUtil.getPosSalesorderDetailByProduct(ITEM, "POS", plant,fdate,tdate);
			}else{
				movQryList =  doUtil.listDODetilstoPrintNew(plant,DONO,fdate,tdate);
			}
			double totdiscount =0,totprice=0,totamount=0,totqty=0;
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			//k=k+1;
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;
			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#FFFFFF";
			totqty=totqty+Double.parseDouble((String)lineArr.get("QTY"));
			totdiscount=totdiscount+Double.parseDouble((String)lineArr.get("DISCOUNT_AMOUNT"));
			totamount=totamount+Double.parseDouble((String)lineArr.get("TOTAL_PRICE"));
	%>
	<TR bgcolor="<%=bgcolor%>">
		        <TD align="left"> <%=(String)lineArr.get("DONO")%></TD>
                <TD align="left"><%=(String)lineArr.get("DELDATE")%></TD>
                <TD align="left"><%=(String)lineArr.get("CustCode")%></TD>
                <TD align="right"><%=StrUtils.addZeroes(Double.valueOf((String)lineArr.get("QTY")), "3")%></TD>
                <TD align="right"><%=Numbers.toMillionFormat((String)lineArr.get("DISCOUNT_AMOUNT"),Integer.valueOf(numberOfDecimal))%></TD>
                <TD align="right"><%=Numbers.toMillionFormat((String)lineArr.get("TOTAL_PRICE"),Integer.valueOf(numberOfDecimal))%></TD>
	</TR>
 
<%

		
}
		if(movQryList.size()>0)
		{
			%>
			<tr class="group"><td></td><td></td><td><b>Total</b></td><td class=" t-right"><b><%=StrUtils.addZeroes(totqty, "3")%></b></td><td class=" t-right"><b><%=StrUtils.addZeroes(totdiscount, numberOfDecimal)%></b></td><td class=" t-right"><b><%=StrUtils.addZeroes(totamount, numberOfDecimal)%></b></td></tr>
					<%
		}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
	
%>
	
</tbody>
</TABLE>
</div>
	<div class="form-group">
  	<div class="col-sm-12" align="center">   
<!--   	<button type="button" class="Submit btn btn-default"  value="PrintWithBatch/Sno"  name="action" onclick="window.location.href='javascript:history.back()'"><b>Back</b></button> -->
  	  	</div>
  	</div>



</FORM>
</div></div></div>
<style id="table_style" type="text/css">
    table
    {
        border-collapse: collapse;
    }
    table td
    {
        padding: 5px;
        border: 1px solid #ccc;
    }
    table td:nth-last-child(3),
    td:nth-last-child(2),
    td:nth-last-child(1) {
    text-align: right;
	}
	
</style>
<script type="text/javascript">
    function PrintTable() {
        var printWindow = window.open('', '', '');
        printWindow.document.write('<html><head><title>Print <%=title %></title>');
 
        //Print the Table CSS.
        var table_style = document.getElementById("table_style").innerHTML;
        printWindow.document.write('<style type = "text/css">');
        printWindow.document.write(table_style);
        printWindow.document.write('</style>');
        printWindow.document.write('</head>');
 
        //Print the DIV contents i.e. the HTML Table.
        printWindow.document.write('<body>');
        var divContents = document.getElementById("dvContents").innerHTML;
        printWindow.document.write(divContents);
        printWindow.document.write('</body>');
 
        printWindow.document.write('</html>');
        printWindow.document.close();
        printWindow.print();
    }
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="nobackblock" value="1" />
</jsp:include>