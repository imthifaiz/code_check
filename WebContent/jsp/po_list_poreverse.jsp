<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
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

<html>
<head>
<title>Order in Product list </title>
<link rel="stylesheet" href="css/style.css">
</head>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Order In Product List</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Order Number</TH>
      <TH>Customer Name</TH>
      <TH>Reference No</TH>
      <TH>Status</TH>
    </TR>
    </thead>
    <tbody>
<%
   // ItemUtil itemUtil = new ItemUtil();
    POUtil itemUtil = new POUtil();
    StrUtils strUtils = new StrUtils();
    session= request.getSession();
    String plant = strUtils.fString((String)session.getAttribute("PLANT"));
    String pono = strUtils.fString(request.getParameter("PONO"));
    System.out.println("Getting details for pono : " + pono);
    String sBGColor = "";
   try{
   

     Hashtable ht=new Hashtable();
     String extCond="";
     ht.put("PLANT",plant);

       if(pono.length()>0) extCond=" AND plant='"+plant+"' and pono like '"+pono+"%' ";
       //extCond=extCond + " and STATUS <> 'C'"; 
       extCond=extCond + " and STATUS <> 'N'";
     extCond=extCond+" order by CAST((SUBSTRING(CollectionDate, 7, 4) + SUBSTRING(CollectionDate, 4, 2) + SUBSTRING(CollectionDate, 1, 2)) AS date) desc";
     ArrayList listQry = itemUtil.getPoHdrDetails("pono,custName,jobNum,status",ht,extCond);
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
      Map m=(Map)listQry.get(i);
      pono     = (String)m.get("pono");

     String custName    = strUtils.replaceCharacters2Send((String)m.get("custName"));
     String jobNum    = (String)m.get("jobNum");
     String status      =  (String)m.get("status");
      

%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.PONO.value='<%=pono%>';
                     window.opener.form.JOB_NUM.value='<%=jobNum%>';
                     window.opener.form.CUST_NAME.value='<%=strUtils.insertEscp(custName)%>';
                     window.close();"><%=pono%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(custName)%></td>
		<td align="left" class="main2">&nbsp;<%=jobNum%></td>
		<td align="left" class="main2">&nbsp;<%=status%></td>
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
	  $('[data-toggle="tooltip"]').tooltip(); 
	   $('#myTable').dataTable({
		   "order": []
		} );
	});
</script>