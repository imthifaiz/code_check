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

<title>Remarks List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Remarks List</h3> 
</div>
</div>
<body>
<form method="post" name="form1">
<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Remarks</TH>

     
     </TR>
     </thead>
     <tbody>
<%

	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);
	 MasterUtil  _MasterUtil = new MasterUtil();
	 _MasterUtil.setmLogger(mLogger);
	 String INDEX="";
    StrUtils strUtils = new StrUtils();
    String plant = (String)session.getAttribute("PLANT");
    String remarks = strUtils.fString(request.getParameter("REMARKS"));
    String type = strUtils.fString(request.getParameter("TYPE"));
    session=request.getSession();
      String sBGColor = "";
   try{
	
	   ArrayList arrList = _MasterUtil.getRemarksList(plant, " remarks like  '"+remarks+"%'");
	   System.out.println("arraysize"+arrList.size());
	    for (int iCnt =0; iCnt<arrList.size(); iCnt++){
			int id=iCnt+1;
            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
            Map lineArr = (Map) arrList.get(iCnt);
            String strremarks = strUtils.replaceCharacters2Send((String)lineArr.get("REMARKS"));
           if(strremarks == null || strremarks.equals("")||strremarks.equals("NOREMARKSDETAILS"))
			{
        	   strremarks="";
			}
           
          if(type.equalsIgnoreCase("REMARKS1")){

%>

<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.REMARK1.value='<%=strUtils.insertEscp(strremarks)%>';
                     window.close();"><%=strUtils.insertEscp(strremarks)%></a></td>
	</TR>

    
<%}else if(type.equalsIgnoreCase("REMARKS2")){%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.REMARK3.value='<%=strUtils.insertEscp(strremarks)%>';
	                 window.close();"><%=strUtils.insertEscp(strremarks)%></a></td>
	</TR>

         
	        <%}else if(type.equalsIgnoreCase("MULTI")){
	        	INDEX = strUtils.fString(request.getParameter("INDEX"));
	        %>
	        
	        <TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.PRDREMARKS_<%=INDEX%>.value='<%=strUtils.insertEscp(strremarks)%>';
	                 window.close();"><%=strUtils.insertEscp(strremarks)%></a></td>
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
