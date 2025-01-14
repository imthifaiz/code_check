<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.BomDAO"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<title>Kitting Product List</title>
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
<h3 class="panel-title">Kitting Product List</h3> 
</div>
</div>
<body>
<form method="post" name="form1">
<div>
<table id="myTable" class="table">
 <thead style="background: #eaeafa"> 
        <tr>
          <th>Product ID</th>
          <th>Product Description</th>
      </tr>
    </thead>
    <tbody>
  
<%
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
		loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
		MLogger mLogger = new MLogger();
		mLogger.setLoggerConstans(loggerDetailsHasMap);
        session = request.getSession();
        BomDAO  bomDAO  = new  BomDAO();  
        bomDAO.setmLogger(mLogger);
        StrUtils strUtils = new StrUtils();
        String PLANT= session.getAttribute("PLANT").toString();
        String ITEM = strUtils.fString(request.getParameter("ITEM"));
        String FROM_DATE= strUtils.fString(request.getParameter("FROM_DATE"));
        String TO_DATE= strUtils.fString(request.getParameter("TO_DATE"));
        String SORT=strUtils.fString(request.getParameter("SORT"));
        String fdate="",tdate="";
        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
    	if (FROM_DATE.length()>5)
    		fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
    	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
    	if (TO_DATE.length()>5)
    		tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
    	String sBGColor = "";
         try{
    	    List listQry =   bomDAO.getKittingItemByWMS(PLANT,ITEM,fdate,tdate,SORT);
	        ItemMstDAO itemMstDAO = new ItemMstDAO();
	        itemMstDAO.setmLogger(mLogger);
	        for(int i =0; i<listQry.size(); i++) {
	        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
	         Map m=(Map)listQry.get(i);
	         String sItem    = (String)m.get("item");
	         String sDesc = strUtils.fString(itemMstDAO.getItemDesc(PLANT,sItem));
	         String sdetdesc = strUtils.fString(itemMstDAO.getItemDetailDesc(PLANT,sItem));

%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form1.ITEM.value='<%=sItem%>';
                     window.opener.form1.PRD_DESCRIP_PARENT.value='<%=strUtils.insertEscp(sDesc)%>';
                     window.opener.form1.DESC.value='<%=strUtils.insertEscp(sDesc)%>';
                     window.opener.form1.DETDESC.value='<%=strUtils.insertEscp(sdetdesc)%>';
                     window.close();"><%=sItem%></a></td>
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