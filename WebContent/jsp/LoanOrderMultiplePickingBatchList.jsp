<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.util.*"%>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>


<title>Batch List</title>
<link rel="stylesheet" href="css/style.css">
</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Batch List</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Batch</TH>
      <TH>PC/PCS/EA UOM</TH>
      <TH>PC/PCS/EA UOM Quantity</TH>
      <TH>Inventory UOM</TH>
      <TH>Inventory UOM Quantity</TH>
      <TH>Received Date</TH>
       <TH>Expire Date</TH>
   </TR>
   </thead>
   <tbody>
<%
    InvMstDAO  _InvMstDAO  = new InvMstDAO();  
   
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
    String INDEX = strUtils.fString(request.getParameter("INDEX"));
   // String LOC = StrUtils.fString(request.getParameter("LOC"+"_"+INDEX));
    String LOC = strUtils.fString(request.getParameter("LOC"));
    String BATCH = strUtils.fString(request.getParameter("BATCH"+"_"+INDEX));
    String UOM =strUtils.fString(request.getParameter("UOM"));
    String sBGColor = "";
    List listQry = new ArrayList();
    try{
    	
    	if(BATCH.equalsIgnoreCase("NOBATCH"))
    	{
    		BATCH="";
    	}
  	   // List listQry =  _InvMstDAO.getOutBoundPickingBatchByWMS(PLANT,ITEMNO,LOC,BATCH);
  	  listQry =  _InvMstDAO.getOutBoundPickingBatchByWMSMultiUOM(PLANT,ITEMNO,LOC,BATCH,UOM);
  	    
        	for(int i =0; i<listQry.size(); i++) {
     		sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     		Map m=(Map)listQry.get(i);
     		String sBatch   = (String)m.get("batch");
     		String sQty   =StrUtils.formatNum((String)m.get("qty"));
     		String crat   =(String)m.get("crat");
     		String expirydate   =(String)m.get("expirydate");
			String sid = (String)m.get("id"); 
			String pcsQty   =StrUtils.formatNum((String)m.get("pcsqty"));
			String pcsUom   =(String)m.get("pcsuom");
			%>

<TR>
		 <td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"	onClick="window.opener.form.BATCH_<%=INDEX%>.value='<%=sBatch%>';
			try{window.opener.form.BATCH_ID_<%=INDEX%>.value='<%=sid%>';}catch(e){console.trace();} window.opener.form.AVAILABLEQTY_<%=INDEX%>.value='<%=sQty%>';
			 window.opener.getavailqty('<%=sQty%>',<%=INDEX%>); window.opener.form.PICKINGQTY_<%=INDEX%>.focus(); window.close();"><%=sBatch%></a></td>
		<td align="left" class="main2">&nbsp;<%=pcsUom%></td>
        <td align="left" class="main2">&nbsp;<%=pcsQty%></td>
        <td align="left" class="main2">&nbsp;<%=UOM%></td>
		<td align="left" class="main2">&nbsp;<%=sQty%></td>
		<td align="left" class="main2">&nbsp;<%=crat%></td>
		<td align="left" class="main2">&nbsp;<%=expirydate%></td>
		
	</TR>
    
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
</tbody>
 </table>
 <br>
  <div class="text-center">       
        <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
	     </div>    
</div>
</form>
</body>
</html>

<script>
$(document).ready(function(){
	$('#myTable').dataTable();
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script>