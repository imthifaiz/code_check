<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<html>
<head>
<title>Batch List</title>
<link rel="stylesheet" href="css/style.css">

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

</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Batch List</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div >
<table id="myTable" class="table">
 <thead style="background: #eaeafa"> 
        <tr>
          <th>Batch</th>
          <th>Qty</th>
      </tr>
    </thead>
    <tbody>

	<%
	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
			.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
			(String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);

    InvMstDAO  _InvMstDAO  = new InvMstDAO();  
    _InvMstDAO.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
    String FROM_LOC = strUtils.fString(request.getParameter("FROM_LOC"));
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
    String BATCH = strUtils.fString(request.getParameter("BATCH"));
    String prdCnt = strUtils.fString(request.getParameter("INDEX"));
    String type = strUtils.fString(request.getParameter("TYPE"));
System.out.println("prdcnt"+prdCnt);  
    String sBGColor = "";
   try{

    List listQry =  _InvMstDAO.getLocationTransferBatch(PLANT,ITEMNO,FROM_LOC,BATCH);
    for(int i =0; i<listQry.size(); i++) {
     ItemMstDAO itemMD = new ItemMstDAO();
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sBatch    = (String)m.get("batch");
     String sQty=StrUtils.formatNum((String)m.get("qty"));
     String uom = itemMD.getItemUOM(PLANT,ITEMNO);
    
   %>
   
		
	<TR>
	<%if(prdCnt=="" && type.equalsIgnoreCase("KITDEKIT")) {%>
	<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.BATCH_1.value='<%=sBatch%>';window.close();"><%=sBatch%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sQty)%></td>
		
		
	<% }else if(prdCnt==""){%>
	<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
    onClick=" window.opener.form.BATCH.value='<%=sBatch%>';
              window.opener.form.QTY.value='<%=sQty%>';
              window.opener.form.INVQTY.value='<%=sQty%>';
      		  window.opener.form.UOM.value='<%=uom%>';
              window.close();"><%=sBatch%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sQty)%></td>
			
	<% }else {%>
			<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
            onClick=" window.opener.form.BATCH_<%=prdCnt%>.value='<%=sBatch%>';
                      window.opener.form.INVQTY_<%=prdCnt%>.value='<%=sQty%>';
                      window.opener.form.AVAILQTY_<%=prdCnt%>.value='<%=sQty%>';
      		          window.opener.form.UOM_<%=prdCnt%>.value='<%=uom%>';
     		          window.opener.getavailqty('<%=sQty%>',<%=prdCnt%>);
     		          window.close();"><%=sBatch%></a></td>

<% }%>
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
    /* $('#myModal1').click(function(){
    	if(document.getElementById("alertValue").value!="")
    	{
    		//$("#myModal").modal();
    		document.getElementById('myModal').style.display = "block";
    	}
    }); */
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script>