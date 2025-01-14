<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script> -->
<link rel="stylesheet" href="../jsp/dist/css/bootstrap.min.css"/>
  <!-- DataTables -->
  <link rel="stylesheet" href="../jsp/dist/css/lib/datatables.min.css"/>
  <!-- jQuery 3 -->
  <script type="text/javascript" src="../jsp/dist/js/jquery.min.js"></script>
  <script type="text/javascript" src="../jsp/dist/js/jquery.dataTables.min.js"></script>
	<!-- jQuery UI -->
	<script src="../jsp/dist/js/jquery-ui-1.12.1.js"></script>



<title>Customer List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Customer List</h3> 
</div>
</div>
<body>
<form method="post" name="form1">
<div>
   <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
       <TH>Customer ID</TH>
        <TH>Customer Name</TH>
      <!--  <TH align="left"><font color="white">Active</font></TH>-->
          </TR>
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
      session = request.getSession();
      InvMstDAO  _InvMstDAO   = new  InvMstDAO();  
      _InvMstDAO.setmLogger(mLogger);
      StrUtils strUtils = new StrUtils();
      String PLANT= session.getAttribute("PLANT").toString();
      String CUSTOMER = strUtils.fString(request.getParameter("CUSTOMER"));
      String TYPE = strUtils.fString(request.getParameter("TYPE"));
     
    String sBGColor = "";
    try{
    	
      
        List listQry =   _InvMstDAO.getCustList(PLANT,CUSTOMER);
        ItemMstDAO itemMstDAO = new ItemMstDAO();
        itemMstDAO.setmLogger(mLogger);
        
        for(int i =0; i<listQry.size(); i++) {
       
         sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
         Map m=(Map)listQry.get(i);
         String cId    = (String)m.get("CUSTNO");
         String cName    = strUtils.replaceCharacters2Send((String)m.get("CNAME"));
         String cActive    = (String)m.get("IsActive");
         String sTAXTREATMENT=strUtils.removeQuotes((String)m.get("TAXTREATMENT"));
  		
if(TYPE.equalsIgnoreCase("MULTIPLEOB")){%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form.CUSTOMER.value='<%=strUtils.insertEscp(cName)%>';
      			      window.close();"><%=cId%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(cName)%></td>
	</TR>

		
<%} else if(TYPE.equalsIgnoreCase("ACCTCUST")){ %>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" setCustomerData('<%=cId%>','<%=strUtils.insertEscp(cName)%>','<%=sTAXTREATMENT%>');"><%=cId%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(cName)%></td>
	</TR>

   
<%} else{ %>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick=" window.opener.form1.CUSTOMER.value='<%=strUtils.insertEscp(cName)%>';
                      window.close();"><%=cId%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(cName)%></td>
	</TR>

   
<%
}
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
function setCustomerData(custno, custname, sTAXTREATMENT){
	var customerData = new Object();
	customerData.custno = custno;
	customerData.custname = custname;
	customerData.sTAXTREATMENT = sTAXTREATMENT;
	window.opener.setCustomerData(customerData);
	window.close();
}
</script>