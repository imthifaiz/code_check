<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
<head>
<meta http-equiv="Content-Type"	content="text/html; charset=windows-1252">
<title>Company List</title>

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
<link rel="stylesheet" href="css/style.css">

<script>
	function setCheckedValue(radioObj, newValue) {
		if (!radioObj)
			return;
		var radioLength = radioObj.length;
		if (radioLength == undefined) {
			radioObj.checked = (radioObj.value == newValue.toString());
			return;
		}
		for ( var i = 0; i < radioLength; i++) {
			radioObj[i].checked = false;
			if (radioObj[i].value == newValue.toString()) {
				radioObj[i].checked = true;
			}
		}
	}
</script>

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
          <th>Company ID</th>
          <th>Company Name</th>
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
		String plant = "", plantdesc = "",rcbno="", startdate = "", noofcatalogs="",expirydate = "", sdate = "", edate = "", actexpirydate = "", name = "", desgination = "", telno = "", hpno = "", email = "", add1 = "", add2 = "", add3 = "", add4 = "", remarks = "", fax = "", county = "", zip = "",
                sales="",sales_percent="",sales_fr_dollars="",sales_fr_cents="",enquiry_fr_dollars="",enquiry_fr_cents="",currencyCode="",noofsignatures="",state="",ENABLE_INVENTORY="",ENABLE_ACCOUNTING="",istaxreg="",reportbasis="";
		try {
			List listQry = plantutil.getPlantMstDetails(splant);
			session = request.getSession();
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				sBGColor = ((i == 0) || (i % 2 == 0))
						? "#FFFFFF"
						: "#dddddd";
				plant = (String) map.get("PLANT");
				plantdesc = (String) map.get("PLNTDESC");
				istaxreg = (String) map.get("ISTAXREGISTRED");
				reportbasis = (String) map.get("REPROTSBASIS");
				startdate = (String) map.get("STARTDATE");
				expirydate = (String) map.get("EXPIRYDATE");
				actexpirydate = (String) map.get("ACTEXPIRYDATE");
				session.setAttribute("ACTEXPIRYDATE", (String) map.get("ACTEXPIRYDATE"));
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
				remarks = (String) map.get("REMARKS");
				fax = (String) map.get("FAX");
				rcbno = (String) map.get("RCBNO");
                sales = (String) map.get("SALES_CHARGE_BY");
				sales_percent = (String) map.get("SALES_PERCENT");
				sales_fr_dollars = (String) map.get("SALES_FR_DOLLARS");
				sales_fr_cents = (String) map.get("SALES_FR_CENTS");
				enquiry_fr_dollars = (String) map.get("ENQUIRY_FR_DOLLARS");
				enquiry_fr_cents = (String) map.get("ENQUIRY_FR_CENTS");
                noofcatalogs = (String) map.get("NUMBER_OF_CATALOGS");
                currencyCode = (String) map.get("BASE_CURRENCY");
                noofsignatures = (String) map.get("NUMBER_OF_SIGNATURES");
                state = (String) map.get("STATE");
                ENABLE_INVENTORY = (String) map.get("ENABLE_INVENTORY");
                ENABLE_ACCOUNTING = (String) map.get("ENABLE_ACCOUNTING");
				
				if (startdate.equals("")) {
					startdate = "Dummystartdate";
				}
				if (expirydate.equals("")) {
					startdate = "Dummyexpirydate";
				}
	%>
	<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onclick="window.opener.form1.PLANT.disabled = false;
	       window.opener.form1.PLANT.value='<%=plant%>';
	       window.opener.form1.PLANT_CODE.value='<%=plant%>';
	       window.opener.form1.PLNTDESC.value='<%=plantdesc%>';
	       window.opener.form1.ISTAXREGISTRED.value='<%=istaxreg%>';
	       window.opener.form1.REPROTSBASIS.value='<%=reportbasis%>';
	       window.opener.form1.STARTDATE.value='<%=startdate%>';
	       window.opener.form1.EXPIRYDATE.value='<%=expirydate%>';
	       window.opener.form1.ACTUALEXPIRYDATE.value='<%=actexpirydate%>';
	       window.opener.form1.DESGINATION.value='<%=desgination%>';
	       window.opener.form1.NAME.value='<%=name%>';
	       window.opener.form1.TELNO.value='<%=telno%>';
	       window.opener.form1.HPNO.value='<%=hpno%>';
	       window.opener.form1.EMAIL.value='<%=email%>';
	       window.opener.form1.REMARKS.value='<%=remarks%>';
	       window.opener.form1.ADD1.value='<%=add1%>';
	       window.opener.form1.ADD2.value='<%=add2%>';
	       window.opener.form1.ADD3.value='<%=add3%>';
	       window.opener.form1.RCBNO.value='<%=rcbno%>';
	       window.opener.form1.ADD4.value='<%=add4%>';
	       window.opener.form1.COUNTY.value='<%=county%>';
	       window.opener.form1.ZIP.value='<%=zip%>';
	       window.opener.form1.FAX.value='<%=fax%>';
           window.opener.form1.NOOFCATALOGS.value='<%=noofcatalogs%>';  
           window.opener.form1.NOOFSIGNATURES.value='<%=noofsignatures%>';  
            window.opener.form1.BaseCurrency.value='<%=currencyCode%>';
	        window.opener.form1.STATE.value='<%=state%>';
           window.opener.form1.ENABLE_INVENTORY.checked=(window.opener.form1.ENABLE_INVENTORY && '1' == '<%=ENABLE_INVENTORY%>');    
           window.opener.form1.ENABLE_ACCOUNTING.checked=(window.opener.form1.ENABLE_ACCOUNTING && '1' == '<%=ENABLE_ACCOUNTING%>');
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
