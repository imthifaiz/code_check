<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.CustomerBeanDAO"%>
<html>
<head>
 
<title>Supplier List</title>
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
<body>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Supplier List</h3> 
</div>
</div>

<form method="post" name="form1">
<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH >Supplier ID</TH>
       <TH >Supplier Name</TH>
        <TH >IsActive</TH>
    </TR>
    </thead>
    <tbody>

<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);

StrUtils strUtils = new StrUtils();
String plant= session.getAttribute("PLANT").toString();
String USERID= session.getAttribute("LOGIN_USER").toString();
String sSupplier = strUtils.fString(request.getParameter("SUPPLIER"));
String index = strUtils.fString(request.getParameter("INDEX"));


CustomerBeanDAO _CustomerBeanDAO = new CustomerBeanDAO(); 
List listQry = new ArrayList();
  
_CustomerBeanDAO.setmLogger(mLogger);

listQry = _CustomerBeanDAO.getSupplierDetails(sSupplier,plant,"");


try {

	String supplierType="";
	if(request.getParameter("SUPPLIERTYPE")!=null)
		supplierType=request.getParameter("SUPPLIERTYPE");
	
   for (int iCnt =0; iCnt<listQry.size(); iCnt++){
         Map m=(Map)listQry.get(iCnt);
                 String Supplierno   =  StrUtils.fString((String)m.get("VENDNO"));
                 String Suppliername   =  StrUtils.fString((String)m.get("VNAME"));
                 String isactive   =  StrUtils.fString((String)m.get("ISACTIVE"));     
                 
%>
<TR>
<%if(supplierType.equalsIgnoreCase("PRDMODALUOM")){%>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.productForm.SUPPLIER_<%=index%>.value='<%=Supplierno%>';
                     window.close();"><%=Supplierno%></a></td>
 		<%} else {%>                    
   		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.SUPPLIER_<%=index%>.value='<%=Supplierno%>';
                     window.close();"><%=Supplierno%></a></td>
 	<%} %>	                    
		<td align="left" class="main2">&nbsp;<%=Suppliername%></td>
		<td align="left" class="main2">&nbsp;<%=isactive%></td>

	</TR>
	
   
<%
    }	
    }catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");
	}
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

<script type="text/javascript">
$(document).ready(function(){
	$('#myTable').dataTable();
    
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script> 
