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
		String splant = strUtils.fString(request.getParameter("COMPANY"));
                String sBillingType = strUtils.fString(request.getParameter("TYPE"));
String enQuiryFlatRate ="",salesCharchableRate="";			
		String sBGColor = "";
		String plant = "", plantdesc = "",rcbno="", startdate = "", expirydate = "", sdate = "", edate = "", actexpirydate = "", name = "", desgination = "", telno = "", hpno = "", email = "", add1 = "", add2 = "", add3 = "", add4 = "", remarks = "", fax = "", county = "", zip = "",
                chargeBy="",sales_percent="",sales_fr_dollars="",sales_fr_cents="",enquiry_fr_dollars="",enquiry_fr_cents="";
		try {
			List listQry = plantutil.getCompanyDetails(splant);
			session = request.getSession();
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				sBGColor = ((i == 0) || (i % 2 == 0))? "#FFFFFF" : "#dddddd";
				plant = (String) map.get("PLANT");
				plantdesc = (String) map.get("PLNTDESC");
                                chargeBy = (String) map.get("SALES_CHARGE_BY");
				salesCharchableRate = (String) map.get("SALESRATE");
                                
                                if (chargeBy.equalsIgnoreCase("PERCENTAGE")){
                                salesCharchableRate=salesCharchableRate+"%";
                                }else if (chargeBy.equalsIgnoreCase("FLATRATE")){
                                salesCharchableRate="$"+salesCharchableRate;
                                }
     
				enQuiryFlatRate = (String) map.get("EFLATRATE");
				
                      
	
	%>
        <% if(sBillingType.equalsIgnoreCase("MOBILE ENQUIRY")){%>
	<TR bgcolor="<%=sBGColor%>">
		<td class="main2" width="40%"><a href="#" onclick=" window.opener.form1.COMPANY.value='<%=plant%>';
	          window.opener.form1.ENQUIRYRATE.value='<%=enQuiryFlatRate%>';
                 window.close();"><%=plant%></a>
	    </td>
            <%}else if (sBillingType.equalsIgnoreCase("MOBILE ORDER")){%>
            <TR bgcolor="<%=sBGColor%>">
		<td class="main2" width="40%"><a href="#" onclick=" window.opener.form1.COMPANY.value='<%=plant%>';
	          window.opener.form1.SALESRATE.value='<%=salesCharchableRate%>';  window.opener.form1.SALESPERCNT.value='<%=chargeBy%>';
                 window.close();"><%=plant%></a>
	    </td>
            <%}%>
            
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
