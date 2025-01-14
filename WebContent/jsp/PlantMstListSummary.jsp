<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>PlantMst List</title>
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
<h3 class="panel-title">PlantMst List</h3> 
</div>
</div>


<body>
<form method="post" name="form1">
<div>
<table id="myTable" class="table" >
<thead style="background: #eaeafa">
        <tr>
          <th>Company</th>
          <th>Description</th>
      </tr>
    </thead>
    <tbody>

	<%
		StrUtils strUtils = new StrUtils();
		PlantMstUtil plantutil = new PlantMstUtil();
		DateUtils dateUtil = new DateUtils();

		MLogger mLogger = new MLogger();
		String splant = strUtils.fString(request.getParameter("PLANT"));

		String sBGColor = "";
		String plant = "", plantdesc = "", startdate = "", expirydate = "", actualexpirydate = "", name = "", desgination = "", telno = "", hpno = "", email = "", add1 = "", add2 = "", add3 = "", add4 = "", county = "", zip = "";
		try {
			List listQry = plantutil.getPlantMstDetails(splant);
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				sBGColor = ((i == 0) || (i % 2 == 0))
						? "#FFFFFF"
						: "#dddddd";
				plant = (String) map.get("PLANT");
				plantdesc = (String) map.get("PLNTDESC");
				startdate = (String) map.get("STARTDATE");
				expirydate = (String) map.get("EXPIRYDATE");
				name = (String) map.get("NAME");
				desgination = (String) map.get("DESGINATION");
				telno = (String) map.get("TELNO");
				hpno = (String) map.get("HPNO");
				email = (String) map.get("EMAIL");
				add1 = (String) map.get("ADD1");
				add2 = (String) map.get("ADD2");
				add3 = (String) map.get("ADD3");
				add4 = (String) map.get("ADD4");
				county = (String) map.get("COUNTY");
				zip = (String) map.get("ZIP");
	%>
	<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onclick="window.opener.form1.PLANT.disabled = false;
                     window.opener.form1.PLANT.value='<%=plant%>';
                     window.opener.form1.STARTDATE.value='<%=startdate%>';
                     window.opener.form1.EXPIRYDATE.value='<%=expirydate%>';
                     window.close();"><%=plant%></a></td>
		<td align="left" class="main2">&nbsp;<%=plantdesc%></td>
		
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
