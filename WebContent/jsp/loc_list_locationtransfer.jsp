<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<title>Location List</title>
<link rel="stylesheet" href="css/style.css">
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
</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Location List</h3> 
</div>
</div>

<body>
<form method="post" name="form1">
<div >
<table id="myTable" class="table">
 <thead style="background: #eaeafa"> 
        <tr>
          <th>Location</th>
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

    InvMstDAO  _InvMstDAO  = new InvMstDAO(); 
    _InvMstDAO.setmLogger(mLogger);
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
        String userId = (String)session.getAttribute("LOGIN_USER");
    String FROM_LOC =strUtils.fString(request.getParameter("FROM_LOC"));
    String type  = strUtils.fString(request.getParameter("TYPE"));
    PlantMstDAO plantMstDAO = new PlantMstDAO();
	Boolean isuserloc = plantMstDAO.getisuserloc(PLANT);
    String sBGColor = "";
   	try{
      List listQry =new ArrayList();
      if(isuserloc)
      listQry =  _InvMstDAO.getStockTransferLocByWMS(PLANT,FROM_LOC,userId);
      else
      listQry =  _InvMstDAO.getStockTransferLocByWMS(PLANT,FROM_LOC,"");
      for(int i =0; i<listQry.size(); i++) {
   
     	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     	Map m=(Map)listQry.get(i);
        String sFromLoc=(String)m.get("FromLoc");
         String sFromLocDesc= strUtils.fString((String)m.get("locdesc"));
        
  if(type.equalsIgnoreCase("PUTAWAYLOC")){%>
  <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			 onClick="window.opener.form.FROM_LOC.value='<%=sFromLoc%>'; 
                      window.opener.form.ITEMNO_0.focus();
                      window.close();"><%=sFromLoc%></a></td>
		<td align="left" class="main2">&nbsp;<%=sFromLocDesc%></td>
	</TR>
     	
              
<% } else{%>
<TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick="window.opener.form.FROM_LOC.value='<%=StrUtils.forHTMLTagPopup(sFromLoc)%>';
      window.opener.form.FROM_LOCDESC.value='<%=StrUtils.forHTMLTagPopup(sFromLocDesc)%>';
      window.opener.form.TOLOC.focus();
      window.close();"><%=StrUtils.forHTMLTag(sFromLoc)%>
      </a></td>
      
        <td class="main2" align="left"><%=StrUtils.forHTMLTag(sFromLocDesc)%></td>
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
</script>