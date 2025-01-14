<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>


<html>
<head>
<!-- <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
 <link rel="stylesheet" href="../dist/css/bootstrap.min.css"/>
  <!-- DataTables -->
  <link rel="stylesheet" href="../dist/css/lib/datatables.min.css"/>
  <!-- jQuery 3 -->
  <script type="text/javascript" src="../dist/js/jquery.min.js"></script>
  <script type="text/javascript" src="../dist/js/jquery.dataTables.min.js"></script>
	<!-- jQuery UI -->
	<script src="../dist/js/jquery-ui-1.12.1.js"></script>
<title>Pos TranID List</title>
<link rel="stylesheet" href="../css/style.css">


</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">POS Transaction ID List</h3> 
</div>
</div>
<body bgcolor="#ffffff">
<form method="post" name="form">
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Transaction ID</TH>
      <TH>No Of Products</TH>
    </TR>
    </thead>
    <tbody>
<%
    String plant= (String)session.getAttribute("PLANT");
    String userID= (String)session.getAttribute("LOGIN_USER");
    POSDetDAO posdao = new POSDetDAO();
    StrUtils strUtils = new StrUtils();
    String sLoc = strUtils.fString(request.getParameter("LOC_ID"));
    String sBGColor = "";
    String trantype = StrUtils.fString(request.getParameter("TYPE")).trim();
   
   try{
   
    ArrayList arrList = posdao.getDistinctTranId(plant,"",trantype);
    
    for(int i =0; i<arrList.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
        Map arrLocLine = (Map)arrList.get(i);
        String sTranId    = (String)arrLocLine.get("POSTRANID");
        String sNoOfItems   = strUtils.removeQuotes((String)arrLocLine.get("NOOFITEMS"));
     if (trantype.equalsIgnoreCase("GOODSRECEIPTWITHBATCH")){
%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form.RECVTRANID.disabled = false;
      window.opener.form.RECVTRANID.value='<%=StrUtils.forHTMLTagPopup(sTranId)%>';
      window.close();"><%=StrUtils.forHTMLTag(sTranId)%>
      </a>
      </td>
      <td class="main2"><%=StrUtils.forHTMLTag(sNoOfItems)%></td>
</TR>
<%
     }else if (trantype.equalsIgnoreCase("GOODSISSUEWITHBATCH")){%>
     <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form.TRANID.disabled = false;
      window.opener.form.TRANID.value='<%=StrUtils.forHTMLTagPopup(sTranId)%>';
      window.close();"><%=StrUtils.forHTMLTag(sTranId)%>
      </a>
      </td>
      <td class="main2"><%=StrUtils.forHTMLTag(sNoOfItems)%></td>
</TR>
<%
     }else if (trantype.equalsIgnoreCase("MOVEWITHBATCH")){%>
     <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form.TRANID.disabled = false;
      window.opener.form.TRANID.value='<%=StrUtils.forHTMLTagPopup(sTranId)%>';
      window.close();"><%=StrUtils.forHTMLTag(sTranId)%>
      </a>
      </td>
      <td class="main2"><%=StrUtils.forHTMLTag(sNoOfItems)%></td>
</TR>
<%
     }  else if (trantype.equalsIgnoreCase("GOODSISSUEWITHBATCHPRICE")){%>
     <TR bgcolor="<%=sBGColor%>">
     <td class="main2"><a href="#" onClick="window.opener.form.TRANID.disabled = false;
     window.opener.form.TRANID.value='<%=StrUtils.forHTMLTagPopup(sTranId)%>';
     window.close();"><%=StrUtils.forHTMLTag(sTranId)%>
     </a>
     </td>
     <td class="main2"><%=StrUtils.forHTMLTag(sNoOfItems)%></td>
</TR>
<%
     } //else end
    }
}catch(Exception he){he.printStackTrace(); 
System.out.println("Error in reterieving data");}
%>
   </tbody>
    
  </table>
<div class="text-center">       
        <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
	     </div>    

</form>
<script>
$(document).ready(function(){
	$('#myTable').dataTable();
      
});
</script>
</body>
</html>





