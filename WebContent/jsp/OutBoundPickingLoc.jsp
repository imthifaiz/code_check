<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

<title>Location List</title>
<link rel="stylesheet" href="css/style.css">
</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Location Details</h3> 
</div>
</div>


<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Location</font></TH>
       <TH>Description</font></TH>
    </TR>
    </thead>
    <tbody>
<%
    InvMstDAO  _InvMstDAO  = new InvMstDAO();  
    StrUtils strUtils = new StrUtils();
    LocUtil _LocUtil = new LocUtil();
    String PLANT = (String)session.getAttribute("PLANT");
     String userID = (String)session.getAttribute("LOGIN_USER");
    String ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
    String LOC = strUtils.fString(request.getParameter("LOC"));
    String sBGColor = "";
    List listQry= new ArrayList();
    try{
    	String nonstocktype = new ItemMstDAO().getNonStockFlag(PLANT, ITEMNO);
    	if(nonstocktype.equalsIgnoreCase("Y")){
    	listQry = _LocUtil.getLocDetails(LOC,PLANT,userID);
    	}else{
    	 listQry =  _InvMstDAO.getOutBoundPickingLocByWMS(PLANT,ITEMNO,LOC,userID);
    	}
    	for(int i =0; i<listQry.size(); i++) {
   
     		sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
     		Map m=(Map)listQry.get(i);
     		String sLoc    = (String)m.get("LOC");
                String sLocDesc    = (String)m.get("LOCDESC");
%>

    <TR bgcolor="<%=sBGColor%>">
      	<td class="main2" align="left">
      		<a href="#" onClick=" window.opener.form.LOC.value='<%=sLoc%>';
            window.close();"><%=sLoc%>
           </a>
       </td>
       <td class="main2" align="left"><%=sLocDesc%></td>
  	</TR>
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
</tbody>
   
  </table>
  <div class="form-group">        
       <div class="col-sm-offset-4 col-sm-8">
      
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