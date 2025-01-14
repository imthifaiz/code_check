<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>

<html>
<head>
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>


<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css"></style>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>


<title>Footers List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table  id="myTable" class="table">
     <thead  style="background: #eaeafa">
    <TR>  
      <TH>Footers</TH>
         </TR>
</thead>
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
    String footer = strUtils.fString(request.getParameter("FOOTER"));
    String type = strUtils.fString(request.getParameter("TYPE"));
    session=request.getSession();
      String sBGColor = "";
   try{
	
	   ArrayList arrList = _MasterUtil.getFooterList(plant, " footer like  '"+footer+"%'");
	   System.out.println("arraysize"+arrList.size());
	    for (int iCnt =0; iCnt<arrList.size(); iCnt++){
			int id=iCnt+1;
            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
            Map lineArr = (Map) arrList.get(iCnt);
            String strfooter = strUtils.replaceCharacters2Send((String)lineArr.get("FOOTER"));
           if(strfooter == null || strfooter.equals("")||strfooter.equals("NOFOOTERDETAILS"))
			{
        	   strfooter="";
			}
           
          if(type.equalsIgnoreCase("Footer1DO")){
 
%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="
      window.opener.form1.Footer1DO.value='<%=strUtils.insertEscp(strfooter)%>';
      window.close();"><%=strUtils.insertEscp(strfooter)%></td>
        
    </TR>
<%}
          else if(type.equalsIgnoreCase("Footer2DO")){%>
          <TR bgcolor="<%=sBGColor%>">
	          <td class="main2"><a href="#" onClick="
	          window.opener.form1.Footer2DO.value='<%=strUtils.insertEscp(strfooter)%>';
	          window.close();"><%=strUtils.insertEscp(strfooter)%></td>
	            
	        </TR>
	        <%}
          else if(type.equalsIgnoreCase("Footer3DO")){%>
          <TR bgcolor="<%=sBGColor%>">
	          <td class="main2"><a href="#" onClick="
	          window.opener.form1.Footer3DO.value='<%=strUtils.insertEscp(strfooter)%>';
	          window.close();"><%=strUtils.insertEscp(strfooter)%></td>
	            
	        </TR>
	         <%}
          else if(type.equalsIgnoreCase("Footer4DO")){%>
          <TR bgcolor="<%=sBGColor%>">
	          <td class="main2"><a href="#" onClick="
	          window.opener.form1.Footer4DO.value='<%=strUtils.insertEscp(strfooter)%>';
	          window.close();"><%=strUtils.insertEscp(strfooter)%></td>
	            
	        </TR>
	       <%}
          else if(type.equalsIgnoreCase("Footer5DO")){%>
          <TR bgcolor="<%=sBGColor%>">
	          <td class="main2"><a href="#" onClick="
	          window.opener.form1.Footer5DO.value='<%=strUtils.insertEscp(strfooter)%>';
	          window.close();"><%=strUtils.insertEscp(strfooter)%></td>
	            
	        </TR>
	        <%}
          else if(type.equalsIgnoreCase("Footer6DO")){%>
          <TR bgcolor="<%=sBGColor%>">
	          <td class="main2"><a href="#" onClick="
	          window.opener.form1.Footer6DO.value='<%=strUtils.insertEscp(strfooter)%>';
	          window.close();"><%=strUtils.insertEscp(strfooter)%></td>
	            
	        </TR>
	        <%}
          else if(type.equalsIgnoreCase("Footer7DO")){%>
          <TR bgcolor="<%=sBGColor%>">
	          <td class="main2"><a href="#" onClick="
	          window.opener.form1.Footer7DO.value='<%=strUtils.insertEscp(strfooter)%>';
	          window.close();"><%=strUtils.insertEscp(strfooter)%></td>
	            
	        </TR>
	        <%}
          else if(type.equalsIgnoreCase("Footer8DO")){%>
          <TR bgcolor="<%=sBGColor%>">
	          <td class="main2"><a href="#" onClick="
	          window.opener.form1.Footer8DO.value='<%=strUtils.insertEscp(strfooter)%>';
	          window.close();"><%=strUtils.insertEscp(strfooter)%></td>
	            
	        </TR>
	        <%}
          else if(type.equalsIgnoreCase("Footer9DO")){%>
          <TR bgcolor="<%=sBGColor%>">
	          <td class="main2"><a href="#" onClick="
	          window.opener.form1.Footer9DO.value='<%=strUtils.insertEscp(strfooter)%>';
	          window.close();"><%=strUtils.insertEscp(strfooter)%></td>
	            
	        </TR>
	        <%}else if(type.equalsIgnoreCase("MULTI")){
	        	INDEX = strUtils.fString(request.getParameter("INDEX"));
	        %>
	         
	        
	    <%
	    }
   }
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
   
  </table>
  <div class="form-group">        
       <div class="col-sm-offset-4 col-sm-8">
      
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
      	
      </div>
    </div>
  
  </form>
</body>
</html>
<script>
$(document).ready(function(){
	$('#myTable').dataTable();
   
    $('[data-toggle="tooltip"]').tooltip(); 
});
</script>




