<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%
String title = "Inventory Summary With Goods Receipt / Goods Issue Detail";
String Srctype="";
StrUtils strUtils = new StrUtils();
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="mainmenu" value="<%=IConstants.INVENTORY%>"/>
</jsp:include>
<script language="javascript">

</script>

<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<% 
StrUtils _strUtils = new StrUtils();
String ITEM="",FROMDATE="",TODATE="";
ITEM = _strUtils.fString(request.getParameter("ITEM"));
FROMDATE = _strUtils.fString(request.getParameter("FROMDATE"));
TODATE = _strUtils.fString(request.getParameter("TODATE"));
%>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>  
				<li><a href="../inventory/incomingoutgoingqty?FROMDATE=<%=FROMDATE%>&TODATE=<%=TODATE%>"><span class="underline-on-hover">Inventory Summary With Goods Receipt / Goods Issue</span> </a></li>                
                <li><label>Inventory Summary With Goods Receipt / Goods Issue Detail</label></li>                                   
            </ol>
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
            
              <div class="btn-group" role="group">
              <button type="button" class="btn btn-default printMe" onclick="PrintTable();"
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
					&nbsp;
				</div>
				
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../inventory/incomingoutgoingqty?FROMDATE=<%=FROMDATE%>&TODATE=<%=TODATE%>'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
		
		<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post">

  <%
ShipHisDAO _ShipHisDAO = new ShipHisDAO();
HTReportUtil movHisUtil       = new HTReportUtil();
_ShipHisDAO.setmLogger(mLogger);
//ArrayList movQryList =  null;
ReportSesBeanDAO reportdoa = new ReportSesBeanDAO();

session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
    String userID = (String) session.getAttribute("LOGIN_USER");
	String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
	String fdate="",tdate="";
    if (FROMDATE.length()>5)
        fdate    = FROMDATE.substring(6)+"-"+ FROMDATE.substring(3,5)+"-"+FROMDATE.substring(0,2);

       if(TODATE==null) TODATE=""; else TODATE = TODATE.trim();
       if (TODATE.length()>5)
       tdate    = TODATE.substring(6)+"-"+ TODATE.substring(3,5)+"-"+TODATE.substring(0,2);
       
      
       ArrayList movQryList  = new ArrayList();
		movQryList =  reportdoa.IncomingoutgoingqtyDetailReport(plant,ITEM,FROMDATE,TODATE);
       String ITEMDESC="";
       if(movQryList.size()>0){
       Map lineArr = (Map) movQryList.get(0);
       ITEMDESC = (String)lineArr.get("ITEMDESC");
       }
	%>

<br>
	<div id="dvContents" >
<TABLE WIDTH="100%" border="0" cellspacing="1" cellpadding=2	 >
<TR>
		<TH align="left"><b>Product : <%=ITEM%></b></TH>
		<TH align="left"><b>Description : <%=ITEMDESC%></b></TH>
	</TR>
	</TABLE>
	<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	align="center" class="table">
	<thead>
	<TR style="background-color: #eaeafa;">
		   <td style="text-align: center"><b>Type</b></td>
           <td style="text-align: center"><b>Tran.Type</b></td>
           <td style="text-align: center"><b>Order No</b></td>
           <td style="text-align: center"><b>Supplier/Customer Name</b></td>
           <td style="text-align: center"><b>Location</b></td>
           <td style="text-align: center"><b>Batch</b></td>
           <td style="text-align: center"><b>Qty</b></td>
           <td style="text-align: center"><b>Tran.Date</b></td>
           <td style="text-align: center"><b>Remarks</b></td>
           <td style="text-align: center"><b>User</b></td>
	</tr>
	</thead>
<tbody>
	<%
	
	try{

		double totrecvqty=0,totissuqty=0;
		int recvtotal=0,issutotal=0,istot=0;
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			//k=k+1;
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;

			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#FFFFFF";	
			
			if(((String)lineArr.get("TYPE")).equalsIgnoreCase("RECEIVE") ){
			totrecvqty=totrecvqty+Double.parseDouble((String)lineArr.get("QTY"));
			recvtotal=1;
			} else {
			totissuqty=totissuqty+Double.parseDouble((String)lineArr.get("QTY"));
			recvtotal=0;
			Map fstlineArr = (Map) movQryList.get(0);
			if(!((String)fstlineArr.get("TYPE")).equalsIgnoreCase("RECEIVE"))
			recvtotal=1;
			}
	%>
			<% 
			if(recvtotal==0 && istot==0){
				istot=1;
				%>
			
	<tr class="group"><td><b>Total</b></td><td></td><td></td><td></td><td></td><td></td><td><b><%=StrUtils.addZeroes(totrecvqty, "3")%></b></td><td></td><td></td><td></td></tr>
	<% } %>

	<TR bgcolor="<%=bgcolor%>">

                <TD align="left"><%=(String)lineArr.get("TYPE")%></TD>
			<TD align="left"> <%=(String)lineArr.get("TRAN_TYPE")%></TD>
                <TD align="left"><%=(String)lineArr.get("ORDER_NO")%></TD>
                <TD align="left"><%=(String)lineArr.get("CNAME")%></TD>
                <TD align="left"><%=(String)lineArr.get("LOC")%></TD>
                <TD align="left"><%=(String)lineArr.get("BATCH")%></TD>
                <TD align="left"><%=StrUtils.formatNum((String)lineArr.get("QTY"))%></TD>
                <TD align="left"><%=(String)lineArr.get("TRANDATE")%></TD>
                <TD align="left"><%=(String)lineArr.get("REMARK")%></TD>
                <TD align="left"><%=(String)lineArr.get("users")%></TD>
	</TR>
	
	<%
		
}
		if(totissuqty>0){
				%>
			
	<tr class="group"><td><b>Total</b></td><td></td><td></td><td></td><td></td><td></td><td><b><%=StrUtils.addZeroes(totissuqty, "3")%></b></td><td></td><td></td><td></td></tr>
	<%
		} else {
	%>
	<tr class="group"><td><b>Total</b></td><td></td><td></td><td></td><td></td><td></td><td><b><%=StrUtils.addZeroes(totrecvqty, "3")%></b></td><td></td><td></td><td></td></tr>	
	<%	
		}

}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
	
%>
	
</tbody>
</TABLE>
</div>
	<div class="form-group">
  	<div class="col-sm-12" align="center">   
  	
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
    table td:nth-last-child(4),
    td:nth-last-child(3),
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