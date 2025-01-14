<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Product Class List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/style.css">

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">PRODUCT CLASS ID LIST</h3> 
</div>
</div>
</head>

<body>
<form method="post" name="form1">
<div>
<table id="myTable" class="table"> 
<thead style="background: #eaeafa"> 
        <tr>
          <th>Product Class ID</th>
          <th>Product Class Description</th>
        </tr>
    </thead>
    <tbody>

	<%
	
		String plant = (String) session.getAttribute("PLANT");
		String userName = StrUtils.fString(
				(String) session.getAttribute("LOGIN_USER")).trim();
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, plant);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userName);
		MLogger mLogger = new MLogger();
		mLogger.setLoggerConstans(loggerDetailsHasMap);
		
		StrUtils strUtils = new StrUtils();
		PrdClassUtil prdclsutil = new PrdClassUtil();
		prdclsutil.setmLogger(mLogger);
		DateUtils dateUtil = new DateUtils();

		String item_id = strUtils.fString(request
				.getParameter("PRD_CLS_ID"));

		String sBGColor = "";

		try {
			List listQry = prdclsutil.getPrdTypeList(item_id, plant,
					" AND ISACTIVE ='Y'");

			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				sBGColor = ((i == 0) || (i % 2 == 0))
						? "#FFFFFF"
						: "#dddddd";
				String prdclsid = (String) map.get("prd_cls_id");
				String desc = strUtils.replaceCharacters2Send1((String) map.get("prd_cls_desc"));
	%>
	<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.PRD_CLS_ID.disabled = false;window.opener.form.PRD_CLS_ID.value='<%=prdclsid%>';window.opener.form.PRD_CLS_DESC.value='<%=strUtils.insertEscp(desc)%>';window.opener.form.PRD_CLS_ID1.value='<%=prdclsid%>';window.close();"><%=prdclsid%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(desc)%></td>

	</TR>
	
	<%
		}
		} catch (Exception he) {
			he.printStackTrace();
			System.out.println("Error in reterieving data");
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