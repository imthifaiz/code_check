<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.LoanDetDAO"%>
<%@ page import="com.track.util.*"%>


<%@page import="com.track.constants.IConstants"%><html>
<head>
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

<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
         <TH>Batch</TH>
     
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
    LoanDetDAO  _loanMstDAO  = new LoanDetDAO();  
      _loanMstDAO.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
     String ORDNO = strUtils.fString(request.getParameter("ORDERNO"));
    String ORDLNNO = strUtils.fString(request.getParameter("ORDERLNO"));
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
    String LOC = strUtils.fString(request.getParameter("LOC"));
    String INDEX = strUtils.fString(request.getParameter("INDEX"));
    String BATCH =  strUtils.fString(request.getParameter("BATCH"+"_"+INDEX));

    String sBGColor = "";
   try{
   

    List listQry =  _loanMstDAO.getLoanOrderBatchListToRecv(PLANT,ORDNO,ORDLNNO,ITEMNO,LOC,BATCH);
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Map m=(Map)listQry.get(i);
     String sBatch   = (String)m.get("batch");
     String spickQty   = (String)m.get("pickQty");
     String srecvQty   = (String)m.get("recQty");
     
    double availQty = Double.parseDouble(spickQty) - Double.parseDouble(srecvQty);
	availQty = StrUtils.RoundDB(availQty,IConstants.DECIMALPTS);


%>
<% if(availQty>0){%>

    <TR bgcolor="<%=sBGColor%>">
      <td class="main2" align="left">
      <a href="#" onClick=" window.opener.form.BATCH_<%=INDEX%>.value='<%=sBatch%>';
      window.opener.form.AVAILABLEQTY_<%=INDEX%>.value='<%=StrUtils.formatNum(Double.toString(availQty))%>';
      window.opener.form.PICKINGQTY_<%=INDEX%>.focus();
      window.close();"><%=sBatch%></a>
      </td>
          
    
 </TR>
 <%}%>
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
</tbody>
  
  </table>
  <div class="form-group">        
       <div class="col-sm-offset-5 col-sm-7">
      
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
      	
      </div>
    </div>
</form>
<script>
$(document).ready(function(){
	$('#myTable').dataTable();
   
   
});
</script>

</body>
</html>