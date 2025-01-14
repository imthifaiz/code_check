<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Item List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="../css/style.css">

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">ITEM LIST</h3> 
</div>
</div>

<body>
<form method="post" name="form">
<div>
<table id="myTable" class="table">
<thead style="background: #eaeafa"> 
        <tr>
          <th>Product ID</th>
          <th>Description</th>
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

    ItemUtil itemUtil = new ItemUtil();
    StrUtils strUtils = new StrUtils();
    
    itemUtil.setmLogger(mLogger);
    String extraCond ="",sDesc="";
    String sItem = strUtils.fString(request.getParameter("ITEM"));
    
    String plant = strUtils.fString((String)session.getAttribute("PLANT"));
    String sBGColor = "";
   try{
    List listQry = itemUtil.qryItemMstCond(sItem,plant,extraCond+" AND ISACTIVE= 'Y' ");
    for(int i =0; i<listQry.size(); i++) {
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Vector vecItem   = (Vector)listQry.get(i);
     String sItem1     = (String)vecItem.get(0);
      sDesc      = strUtils.replaceCharacters2Send1((String)vecItem.get(1));
     String sArtist  = (String)vecItem.get(2);//artist
     String sUom  = (String)vecItem.get(3);
     String sRemarks  = strUtils.replaceCharacters2Send1((String)vecItem.get(4));
     String sMedium    = strUtils.replaceCharacters2Send1((String)vecItem.get(5));
     String sCondition = (String)vecItem.get(6);
     String sTitle    = (String)vecItem.get(7);
     String stkqty    = (String)vecItem.get(8);
      String asset    = (String)vecItem.get(9);  
      String price    = (String)vecItem.get(12);   
     String sDesc1 = strUtils.insertEscp(sDesc);

%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.PRODUCTID.value='<%=sItem1%>';window.opener.form.DESCRIPTION1.value='<%=sDesc1%>';
                     window.opener.form.PRICE.value='<%=strUtils.currencyWtoutCommSymbol(price)%>';window.close();"><%=sItem1%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sDesc)%></td>

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