<%@ page import="com.track.dao.ShipHisDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.DoHdrDAO"%>
<%@ page import="com.track.db.object.DoHdr"%>
<%@ include file="header.jsp"%>
<%
String title = "Sales Report Detail";
String Srctype="";
StrUtils strUtils = new StrUtils();
Srctype = strUtils.fString(request.getParameter("TYPE"));
 if(Srctype.equals("Order") || (Srctype.equals("Customer")) ) {
	 title = "Sales Report Detail";
	 
 } else if(Srctype.equals("Void") ) {
	 title = "Void Detail";
 } else if(Srctype.equals("Return") ) {
	 title = "Return Detail";
 } else if(Srctype.equals("Discount") ) {
	 title = "Discount Detail";
 } else if(Srctype.equals("FOC") ) {
	 title = "FOC Detail";
}
 


 
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
                <% if(Srctype.equals("Order") || (Srctype.equals("Customer")) ) { %> 
                <li><a href="../posreports/salesreports"><span class="underline-on-hover">POS Sales Report</span> </a></li>                
                <li><label>Sales Report Details</label></li>                                   
                <%}else if(Srctype.equals("Void") ) { %>
                <li><a href="../posreports/voidsales"><span class="underline-on-hover">Void Sales Report</span> </a></li>                
                <li><label>Void Report Details</label></li>                                   
                <%}else if(Srctype.equals("Return") ) { %>
                <li><a href="../posreports/return"><span class="underline-on-hover">Return Sales Report</span> </a></li>                
                <li><label>Return Sales Report Details</label></li>                                   
                <%}else if(Srctype.equals("Discount") ) { %>
                <li><a href="../posreports/discount"><span class="underline-on-hover">Discount Summary</span> </a></li>                
                <li><label>Discount Details</label></li>                                   
                <%}else if(Srctype.equals("FOC") ) { %>
                <li><a href="../posreports/FOC"><span class="underline-on-hover">FOC Summary</span> </a></li>                
                <li><label>FOC Details</label></li>                                   
                <%} %>
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
              <% if(Srctype.equals("Order") || (Srctype.equals("Customer")) ) { %>
              <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right"   onclick="window.location.href='../posreports/salesreports'">
               <%}else if(Srctype.equals("Void") ) { %>
              <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right"   onclick="window.location.href='../posreports/voidsales'">
               <%}else if(Srctype.equals("Return") ) { %>
              <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right"   onclick="window.location.href='../posreports/return'">
               <%}else if(Srctype.equals("Discount") ) { %>
              <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right"   onclick="window.location.href='../posreports/discount'">
               <%}else if(Srctype.equals("FOC") ) { %>
              <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right"   onclick="window.location.href='../posreports/FOC'">
               <%} %>
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
<!--               <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right"  value="PrintWithBatch/Sno"  name="action" onclick="window.location.href='javascript:history.back()'"> -->
		</div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post">

  <%
StrUtils _strUtils = new StrUtils();
ShipHisDAO _ShipHisDAO = new ShipHisDAO();
HTReportUtil movHisUtil       = new HTReportUtil();
_ShipHisDAO.setmLogger(mLogger);
//ArrayList movQryList =  null;
DOUtil doUtil = new DOUtil();
DateUtils dateUtils = new DateUtils();
session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
    String userID = (String) session.getAttribute("LOGIN_USER");
	String DONO="",INVOICE="",TYPE="",FROMDATE="",TODATE="",PRNO="",CUSTCODE="";
	DONO = _strUtils.fString(request.getParameter("DONO"));
	PRNO = _strUtils.fString(request.getParameter("PRNO"));
	CUSTCODE = _strUtils.fString(request.getParameter("CUSTCODE"));
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
       
      
       ArrayList movQryList  = new ArrayList();
       
       String orddate="";

       if(Srctype.equals("Order")) {
       	DoHdr dohdr = new DoHdrDAO().getDoHdrById(plant, DONO);
       	orddate = dateUtils.parsecratDate(dohdr.getCRAT());
       } 
       
      Hashtable htData = new Hashtable();
      String querys="SELECT A.DELDATE AS SALESDATE,A.DONO,A.CustCode,A.CustName,ISNULL((SELECT STRING_AGG(cm.PAYMENTMODE,',') FROM "+plant+"_POS_PAYMODE_AMOUNT AS cm WHERE cm.DONO = A.DONO),'') AS PAYMENTMODE " 
    		  +"FROM ["+plant+"_DOHDR] A JOIN ["+plant+"_DODET] B ON A.DONO=B.DONO " 
//     		  +"JOIN [FINCOUNTRYTAXTYPE] C ON C.ID = A.TAXID JOIN ["+plant+"_ITEMMST] D ON D.ITEM = B.ITEM " 
    		  +"WHERE A.ORDER_STATUS != 'DRAFT' AND A.ORDER_STATUS = 'PROCESSED' AND A.ORDERTYPE = 'POS'AND A.ORDER_STATUS != 'CANCELLED' AND A.DELDATE != '' AND A.PLANT='"+plant+"' AND A.DONO='"+DONO+"' "
    		  +"GROUP BY A.DONO,A.DELDATE,A.CustCode,A.CustName  ";
 	  ArrayList arrLists = new ItemMstUtil().selectForReport(querys,htData,"");
 	  String  CustName ="",paymode="";
 	  if (arrLists.size() > 0) {
 		  for (int i=0; i < arrLists.size(); i++ ) {
 			  Map ms = (Map) arrLists.get(i);
 			 CustName = (String) ms.get("CustName");
 			 paymode = (String) ms.get("PAYMENTMODE");
 		  }
 	  }
       
	%>

<br>
	<div id="dvContents" >
<TABLE WIDTH="100%" border="0" cellspacing="1" cellpadding=2 id="headerNO"	class="headerNO" >
	<%if(TYPE.equalsIgnoreCase("Order")){ %>
	<TR>
		<TH align="left"><b>Customer : <%=CustName%></b></TH>
		<TH style="text-align: end;"><b>Payment Mode: <%=paymode%></b></TH>
	</TR>
	<TR>
		<TH align="left"><b>Sales Order No : <%=DONO%></b></TH>
		<TH style="text-align: end;"><b>Sales Order Date: <%=orddate%></b></TH>
	</TR>
	<%}else if(TYPE.equalsIgnoreCase("Customer")){ %>
	<TR>
		<TH align="left"><b>Customer Code : <%=CUSTCODE%></b></TH>
	</TR>
	<%}else{ %>
	<TR>
		<TH align="left"><b>Sales Order No : <%=DONO%></b></TH>
	</TR>
	<%} %>
	</TABLE>
	<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2	align="center" class="table">
	<thead>
	<TR style="background-color: #eaeafa;">
		   <td style="text-align: center"><b>Product</b></td>
		   <td style="text-align: center"><b>Product Description</b></td>
           <td style="text-align: center"><b>Qty</b></td>
           <%if(!TYPE.equalsIgnoreCase("Customer")){ %>
           <td style="text-align: center"><b>Price</b></td>
           <%} %>
           <td style="text-align: center"><b>Amount</b></td>
           <td style="text-align: center"><b>Discount</b></td>
           <td style="text-align: center"><b>Total Amount</b></td>
	</tr>
	</thead>
<tbody>
	<%
	
	try{

				if(Srctype.equals("Return") ){
				movQryList =  new HTReportUtil().getPOSreturnDetailToPrint(plant,DONO,fdate,tdate,PRNO);
				}else if(TYPE.equalsIgnoreCase("PICKISSUE")){
				movQryList =  doUtil.listDODetilstoPrintshiphis(plant,DONO,fdate,tdate);
				}else if(TYPE.equalsIgnoreCase("Order") || TYPE.equalsIgnoreCase("Discount")){
					 movQryList = movHisUtil.getPosSalesorderDetailByDono(DONO, "POS", plant);
				}else if(TYPE.equalsIgnoreCase("Customer")){
					 movQryList = movHisUtil.getPosSalesorderDetailByCustomer(CUSTCODE, "POS", plant);
				}else{
					movQryList =  doUtil.listVoidDetilstoPrint(plant,DONO,fdate,tdate);
				}

		double totdiscount =0,totamount=0,totqty=0,totqtyprice=0;
		for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
			//k=k+1;
			Map lineArr = (Map) movQryList.get(iCnt);
			int iIndex = iCnt + 1;

			if(Srctype.equals("Return") || Srctype.equals("Void") ){

			String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"
					: "#FFFFFF";
			
			 String item_discounttype = StrUtils.fString((String)lineArr.get("DISCOUNT_TYPE"));
			 String item_discount = StrUtils.fString((String)lineArr.get("DISCOUNT"));
			 String PRICE = (String)lineArr.get("UPRICE");
			 
			 PlantMstDAO _PlantMstDAO = new PlantMstDAO();
			 DecimalFormat decimalFormat = new DecimalFormat("#.#####");
			 decimalFormat.setRoundingMode(java.math.RoundingMode.FLOOR);
			 
			 //discount calculation
			 double discount =0,dDiscount=0,priceamt=0;
			 if(item_discounttype.equalsIgnoreCase("%")){
				 dDiscount = Double.parseDouble(item_discount);
				 discount = ((Double.parseDouble(PRICE))/100)*dDiscount;
			 }else{
				 dDiscount = Double.parseDouble(item_discount);
				 discount =  dDiscount;
			 }
			 
			 
			 String price = (String)lineArr.get("UPRICE");
			 String UPRICE = StrUtils.addZeroes(Double.parseDouble(price), numberOfDecimal);
			 String TOTPRICE = StrUtils.addZeroes((Double.parseDouble(price)*Double.parseDouble((String)lineArr.get("ORDQTY"))), numberOfDecimal);
			 double finaltotal = ((Double.parseDouble(TOTPRICE))-discount);
			 String FINAL = StrUtils.addZeroes(finaltotal, numberOfDecimal);
			 String Dis = StrUtils.addZeroes(discount, numberOfDecimal);
			 totqty=totqty+Double.parseDouble((String)lineArr.get("ORDQTY"));
			 totqtyprice=totqtyprice+Double.parseDouble(TOTPRICE);
			 totdiscount=totdiscount+discount;
			 totamount=totamount+finaltotal;
			
	%>
	

	<TR bgcolor="<%=bgcolor%>">

                <TD align="left"><%=(String)lineArr.get("ITEM")%></TD>
                <TD align="left"><%=(String)lineArr.get("ITEMDESC")%></TD>
                <TD align="right"><%=StrUtils.formatNum((String)lineArr.get("ORDQTY"))%></TD>
				<TD align="right"> <%=UPRICE%></TD>
				<TD align="right"> <%=TOTPRICE%></TD>
                <TD align="right"><%=Dis%></TD>
                <TD align="right"><%=FINAL%></TD>
<%-- 			<TD align="left"> <%=(String)lineArr.get("DOLNO")%></TD>
                <TD align="left"><%=(String)lineArr.get("ITEM")%></TD>
                <TD align="left"><%=(String)lineArr.get("ITEMDESC")%></TD>
                <TD align="left"><%=(String)lineArr.get("Trandate")%></TD>
                <TD align="left"><%=StrUtils.formatNum((String)lineArr.get("ORDQTY"))%></TD> --%>



	</TR>
	<%
	}else if(TYPE.equalsIgnoreCase("Order") || TYPE.equalsIgnoreCase("Customer")|| Srctype.equals("Discount")){
		totqty=totqty+Double.parseDouble((String)lineArr.get("QTY"));
		totqtyprice=totqtyprice+Double.parseDouble((String)lineArr.get("AMOUNT"));
		totdiscount=totdiscount+Double.parseDouble((String)lineArr.get("DISCOUNT_AMOUNT"));
		totamount=totamount+Double.parseDouble((String)lineArr.get("TOTAL_PRICE"));
	%>
	<TR>
 <TD align="left"> <%=(String)lineArr.get("ITEM")%></TD>
 <TD align="left"> <%=(String)lineArr.get("ItemDesc")%></TD>
                 <TD align="right"><%=StrUtils.addZeroes(Double.valueOf((String)lineArr.get("QTY")), "3")%></TD>
                 <%if(!TYPE.equalsIgnoreCase("Customer")){ %>
                 <TD align="right"><%=Numbers.toMillionFormat((String)lineArr.get("UNITPRICE"),Integer.valueOf(numberOfDecimal))%></TD>
                 <%} %>
                 <TD align="right"><%=Numbers.toMillionFormat((String)lineArr.get("AMOUNT"),Integer.valueOf(numberOfDecimal))%></TD>
                 <TD align="right"><%=Numbers.toMillionFormat((String)lineArr.get("DISCOUNT_AMOUNT"),Integer.valueOf(numberOfDecimal))%></TD>
                 <TD align="right"><%=Numbers.toMillionFormat((String)lineArr.get("TOTAL_PRICE"),Integer.valueOf(numberOfDecimal))%></TD>
                 <%-- <TD align="left"><%=(String)lineArr.get("AMOUNT")%></TD>
                 <TD align="left"><%=(String)lineArr.get("DISCOUNT_AMOUNT")%></TD>
                 <TD align="left"><%=(String)lineArr.get("TOTAL_PRICE")%></TD> --%>
                 </TR>
<%
}
		
}
if(movQryList.size()>0)
{
	if(Srctype.equals("Return") || Srctype.equals("Void") ){
	%>
	<tr class="group"><td><b>Total</b></td><td></td><td class=" t-right"><b><%=StrUtils.addZeroes(totqty, "3")%></b></td><td></td><td class=" t-right"><b><%=StrUtils.addZeroes(totqtyprice, numberOfDecimal)%></b></td><td class=" t-right"><b><%=StrUtils.addZeroes(totdiscount, numberOfDecimal)%></b></td><td class=" t-right"><b><%=StrUtils.addZeroes(totamount, numberOfDecimal)%></b></td></tr>
			<%
	} else {
		if(!TYPE.equalsIgnoreCase("Customer")){
	%>
	<tr class="group"><td><b>Total</b></td><td></td><td class=" t-right"><b><%=StrUtils.addZeroes(totqty, "3")%></b></td><td></td><td class=" t-right"><b><%=StrUtils.addZeroes(totqtyprice, numberOfDecimal)%></b></td><td class=" t-right"><b><%=StrUtils.addZeroes(totdiscount, numberOfDecimal)%></b></td><td class=" t-right"><b><%=StrUtils.addZeroes(totamount, numberOfDecimal)%></b></td></tr>
	<%}else{ %>
	<tr class="group"><td><b>Total</b></td><td></td><td class=" t-right"><b><%=StrUtils.addZeroes(totqty, "3")%></b></td><td class=" t-right"><b><%=StrUtils.addZeroes(totqtyprice, numberOfDecimal)%></b></td><td class=" t-right"><b><%=StrUtils.addZeroes(totdiscount, numberOfDecimal)%></b></td><td class=" t-right"><b><%=StrUtils.addZeroes(totamount, numberOfDecimal)%></b></td></tr>
			<%}
}
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