<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.dao.SalesDetailDAO"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<!-- <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
 <link rel="stylesheet" href="../dist/css/bootstrap.min.css"/>
  <!-- DataTables -->
  <link rel="stylesheet" href="../dist/css/lib/datatables.min.css"/>
  <!-- jQuery 3 -->
  <script type="text/javascript" src="../dist/js/jquery.min.js"></script>
  <script type="text/javascript" src="../dist/js/jquery.dataTables.min.js"></script>
	<!-- jQuery UI -->
	<script src="../dist/js/jquery-ui-1.12.1.js"></script>
<title>Batch List</title>
<link rel="stylesheet" href="../css/style.css">
<style type="text/css">
	.cratno{
		display: none;
	}
</style>
</head>
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Batch List</h3> 
</div>
</div>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Batch</TH>
      <TH>PC/PCS/EA UOM</TH>
      <TH>PC/PCS/EA UOM Quantity</TH>
      <TH>Inventory UOM</TH>
      <TH>Inventory UOM Quantity</TH>      
      <TH>Received Date</TH>
       <TH>Expiry Date</TH>       
   </TR>
   </thead>
   <tbody>
<%
    InvMstDAO  _InvMstDAO  = new InvMstDAO(); 
    SalesDetailDAO _SalesDetailDAO = new SalesDetailDAO();
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
    String ITEMNO = StrUtils.replaceStr2Char(strUtils.fString(request.getParameter("ITEMNO")));
    String index = strUtils.fString(request.getParameter("INDEX"));
    String LOC = StrUtils.replaceStr2Char(strUtils.fString(request.getParameter("LOC"+index)));
    String BATCH = StrUtils.replaceStr2Char(strUtils.fString(request.getParameter("BATCH"+index)));
    String TYPE = strUtils.fString(request.getParameter("TYPE"));
    String UOM =strUtils.fString(request.getParameter("UOM"));
    System.out.println("TYpe"+TYPE);
    String sBGColor = "";
    List listQry;
    if (ITEMNO.length() > 0) {
        boolean itemFound = true;
        ItemMstDAO itM = new ItemMstDAO();
                //itM.setmLogger(mLogger);
                String scannedItemNo = itM.getItemIdFromAlternate(PLANT, ITEMNO);
                if (scannedItemNo == null) {
                        itemFound = false;
                }else{
                	ITEMNO = scannedItemNo;
                }
    }
    _InvMstDAO.setmLogger(logger);
    try{
    	if(TYPE.equalsIgnoreCase("POSREFUND")){
    		listQry =  _SalesDetailDAO.getBatchfromsalesdetailsposrefund(PLANT,ITEMNO,LOC,BATCH);}
    	else{
  	     listQry =  _InvMstDAO.getOutBoundPickingBatchByWMSMultiUOM(PLANT,ITEMNO,LOC,BATCH,UOM);}
       	
  	  	
  	    for(int i =0; i<listQry.size(); i++) {
     		sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
     		Map m=(Map)listQry.get(i);
     		String sBatch   = (String)m.get("batch");
     		String sQty   = (String)m.get("qty");
     		String crat   =(String)m.get("crat");
     		String pcsQty   =StrUtils.formatNum((String)m.get("pcsqty"));
     		String pcsUom   =(String)m.get("pcsuom");
     		String cratNumber ="";
     		if(crat.equalsIgnoreCase(""))
     			cratNumber ="";
     		else
     		 	cratNumber = crat.substring(6) + crat.substring(3, 4) + crat.substring(0, 1); 
     		String expirydate   =(String)m.get("expirydate");
     		String sid = (String)m.get("id");
        if(TYPE.equalsIgnoreCase("GOODSISSUEWITHBATCH")){ %> 

    <TR bgcolor="<%=sBGColor%>">
      <td class="main2" align="left">
      <a href="#" onClick=" window.opener.form.BATCH_<%=index%>.value='<%=StrUtils.forHTMLTagPopup(sBatch)%>';
      try{window.opener.form.BATCH_ID_<%=index%>.value='<%=sid%>';}catch(e){console.trace();}
      window.opener.form.AVAILQTY.value='<%=StrUtils.forHTMLTagPopup(sQty)%>';window.opener.form.QTY.focus();
      window.close();"><%=sBatch%></a>
      </td>
      <td class="main2" align="left"><%=pcsUom%></td>
      <td align="left" class="main2">&nbsp;<%=pcsQty%></td>
      <td class="main2" align="left"><%=UOM%></td>
       <td class="main2" align="left"><%=sQty%></td>       		
       <td align="left" class="main2">&nbsp;<span class='cratno'><%=cratNumber %></span><%=crat%></td>
		<td align="left" class="main2">&nbsp;<%=expirydate%></td>
 </TR>
<% }else if(TYPE.equalsIgnoreCase("MOVEWITHBATCH")){ %> 

    <TR bgcolor="<%=sBGColor%>">
      <td class="main2" align="left">
      <a href="#" onClick=" window.opener.form.BATCH_<%=index%>.value='<%=StrUtils.forHTMLTagPopup(sBatch)%>';
      try{window.opener.form.BATCH_ID_<%=index%>.value='<%=sid%>';}catch(e){console.trace();}
      window.opener.form.AVAILQTY.value='<%=StrUtils.forHTMLTagPopup(sQty)%>';window.opener.form.QTY.focus();
      window.close();"><%=sBatch%></a>
      </td>
       <td class="main2" align="left"><%=pcsUom%></td>
      <td align="left" class="main2">&nbsp;<%=pcsQty%></td>
      <td class="main2" align="left"><%=UOM%></td>
       <td class="main2" align="left"><%=sQty%></td>
       <td align="left" class="main2">&nbsp;<span class='cratno'><%=cratNumber %></span><%=crat%></td>
		<td align="left" class="main2">&nbsp;<%=expirydate%></td>
 </TR>
  <%} else { %>
   <TR bgcolor="<%=sBGColor%>">
      <td class="main2" align="left">
      <a href="#" onClick=" window.opener.form.BATCH_<%=index%>.value='<%=StrUtils.forHTMLTagPopup(sBatch)%>';
      try{window.opener.form.BATCH_ID_<%=index%>.value='<%=sid%>';}catch(e){console.trace();}
        window.opener.form.QTY_<%=index%>.value='<%=StrUtils.forHTMLTagPopup(sQty)%>';
      window.close();"><%=sBatch%></a>
      </td>
       <td class="main2" align="left"><%=pcsUom%></td>
      <td align="left" class="main2">&nbsp;<%=pcsQty%></td>
      <td class="main2" align="left"><%=UOM%></td>
       <td class="main2" align="left"><%=sQty%></td>
       <td align="left" class="main2">&nbsp;<span class='cratno'><%=cratNumber %></span><%=crat%></td>
		<td align="left" class="main2">&nbsp;<%=expirydate%></td>
 </TR>
 <%} 
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
   </tbody>
    
  </table>
  <div class="text-center">       
        <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
	     </div>    

</form>
<script>
$(document).ready(function(){
	$('#myTable').dataTable({ "order": [[ 2, 'asc' ]] });
});
</script>
</body>
</html>