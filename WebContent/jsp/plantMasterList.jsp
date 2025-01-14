<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
<head>
<meta http-equiv="Content-Type"	content="text/html; charset=windows-1252">
<title>Company List</title>
</head>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
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
</Script>
<body>
<form method="post" name="form1">
<table border="0" width="100%" cellspacing="1" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR BGCOLOR="#000066">
		<TH align="center"><font color="white">Company ID</font></TH>
		<TH align="center"><font color="white">Company Name</font></TH>
	</TR>
	<%
		StrUtils strUtils = new StrUtils();

		PlantMstUtil plantutil = new PlantMstUtil();
		DateUtils dateUtil = new DateUtils();

		MLogger mLogger = new MLogger();
		String splant = strUtils.fString(request.getParameter("PLANT"));

		String sBGColor = "";
		String plant = "", plantdesc = "",rcbno="", startdate = "", expirydate = "", sdate = "", edate = "", actexpirydate = "", name = "", desgination = "", telno = "", hpno = "", email = "", add1 = "", add2 = "", add3 = "", add4 = "", remarks = "", fax = "", county = "", zip = "",
                sales="",sales_percent="",sales_fr_dollars="",sales_fr_cents="",enquiry_fr_dollars="",enquiry_fr_cents="";
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
                                
				if (startdate.equals("")) {
					startdate = "Dummystartdate";
				}
				if (expirydate.equals("")) {
					startdate = "Dummyexpirydate";
				}
	%>
	<TR bgcolor="<%=sBGColor%>">
		<td class="main2" width="40%"><a href="#" onclick="window.opener.form1.COMPANY.disabled = false;
	       window.opener.form1.COMPANY.value='<%=plant%>';
	  
	       window.opener.form1.SALESPERCENT.value='<%=sales_percent%>';
	       window.opener.form1.SDOLLARFRATE.value='<%=sales_fr_dollars%>';
	       window.opener.form1.SCENTSFRATE.value='<%=sales_fr_cents%>';
	       window.opener.form1.EDOLLARFRATE.value='<%=enquiry_fr_dollars%>';
	       window.opener.form1.ECENTSFRATE.value='<%=enquiry_fr_cents%>';
               
	       window.close();"><%=plant%></a>
	    </td>
	   <td align="center" class="main2" width="60%"><%=plantdesc%></td>
	</TR>
	<%
		}
		} catch (Exception he) {
			he.printStackTrace();
			System.out.println("Error in reterieving data");
		}
	%>
	<TR>
		<TH colspan="2">&nbsp;</TH>
	</TR>
	<TR>
		<TH colspan="2" align="center"><a href="#"
			onclick="window.close();">
		    <input type="submit" value="Close"></a></TH>
	</TR>
</table>
</form>
</body>
</html>
