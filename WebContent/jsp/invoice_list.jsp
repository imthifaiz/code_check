<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>

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

<title>Invoice List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Invoice List</h3> 
</div>
</div>


<body>
<form method="post" name="form1">
<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Invoice NO</TH>
      <TH>DONO</TH>     
     </TR>
     </thead>
     <tbody>
<%

	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);
	 ShipHisDAO  _ShipHisDAO = new ShipHisDAO();
	 _ShipHisDAO.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String plant = (String)session.getAttribute("PLANT");
    String dono = strUtils.fString(request.getParameter("DONO"));
	String invoiceno = strUtils.fString(request.getParameter("INVOICENO"));
	String CashCust = StrUtils.fString(request.getParameter("TAXINVOICE"));
     session=request.getSession();
      String sBGColor = "";
   try{
	  // '"+ item	+"%'"+
   		Hashtable<String, String> htCondition = new Hashtable<>();
	  htCondition.put("PLANT", plant);
	   ArrayList<Map<String, String>> arrList= new ArrayList<Map<String, String>>();
	  String  extCond="";
	  if(invoiceno.length()>0)
	  extCond="  and INVOICENO like '"+invoiceno+"%' order by DONO desc";
	  else
		  extCond="  order by DONO desc";  
	  if(CashCust.equalsIgnoreCase("1"))
	    arrList = _ShipHisDAO.selectShipHis("distinct DONO, INVOICENO,CNAME", htCondition, " INVOICENO IS NOT NULL AND INVOICENO <> '' AND 0 = (SELECT COUNT(*) FROM " + plant + "_dnplhdr where INVOICENO = S.INVOICENO) AND DONO like'T%'"+extCond);
	  else
		  arrList = _ShipHisDAO.selectShipHis("distinct DONO, INVOICENO,CNAME", htCondition, " INVOICENO IS NOT NULL AND INVOICENO <> '' AND 0 = (SELECT COUNT(*) FROM " + plant + "_dnplhdr where INVOICENO = S.INVOICENO) AND DONO like'S%'"+extCond);
	    for (int iCnt =0; iCnt<arrList.size(); iCnt++){
			int id=iCnt+1;
            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
            Map lineArr = (Map) arrList.get(iCnt);
            String invoiceNO  = strUtils.replaceCharacters2Send((String)lineArr.get("INVOICENO"));
            String doNo = strUtils.replaceCharacters2Send((String)lineArr.get("DONO"));
			String custName = strUtils.replaceCharacters2Send((String)lineArr.get("CNAME"));				
%>

<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.DONO.value='<%=doNo%>';
			window.opener.form.CUST_NAME.value='<%=strUtils.insertEscp(custName)%>';
			window.opener.form.INVOICENO.value='<%=strUtils.insertEscp(invoiceNO)%>';
                     window.close();"><%=strUtils.insertEscp(invoiceNO)%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.insertEscp(doNo)%></td>
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
		      "order": [[ 0, "desc" ]]
		    
		} );
	});
</script>