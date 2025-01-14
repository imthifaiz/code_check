<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<!--- Not in use -->
<html>
<head>
<title>Product List</title>
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
<link rel="stylesheet" href="css/style.css">




</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
<table border="0" width="100%" cellspacing="1" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR BGCOLOR="#000066">
		<TH align="left"><font color="white">Product ID </font></TH>
		<TH align="left"><font color="white">Description</font></TH>
		<TH align="left"><font color="white">UOM</font></TH>
		<TH align="left"><font color="white">Product Type</font></TH>
		<TH align="left"><font color="white">IsActive</font></TH>
	</TR>
	<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
    ItemUtil itemUtil = new ItemUtil();
    StrUtils strUtils = new StrUtils();
    
    itemUtil.setmLogger(mLogger);
    String sItem = strUtils.fString(request.getParameter("ITEM_DESC"));
    String plant = strUtils.fString((String)session.getAttribute("PLANT"));
    String sBGColor = "";
   try{
    List listQry = null;//itemUtil.queryItemMstWithItemDesc(sItem,plant," ");
    for(int i =0; i<listQry.size(); i++) {
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Vector vecItem   = (Vector)listQry.get(i);
     String sItem1     = (String)vecItem.get(0);
     String sDesc      = strUtils.replaceCharacters2Send1((String)vecItem.get(1));
     String sArtist  = (String)vecItem.get(2);//artist
     String sUom  = (String)vecItem.get(3);
     String sRemarks  = (String)vecItem.get(4);
     String sMedium    = (String)vecItem.get(5);
     String sCondition = (String)vecItem.get(6);
     String sTitle    = (String)vecItem.get(7);
     String stkqty    = (String)vecItem.get(8);
      String asset    = (String)vecItem.get(9);
      String prd_cls_id    = (String)vecItem.get(10);    
         String isactive    =  strUtils.fString((String)vecItem.get(11));     
     String sDesc1 = strUtils.insertEscp(sDesc);

%>
	<TR bgcolor="<%=sBGColor%>">
		<td class="main2"><a href="#"
			onClick="window.opener.form.ITEM.value='<%=sItem1%>';
      window.opener.form.DESC.value='<%=sDesc1%>';
      window.opener.form.ARTIST.value='<%=sArtist%>';
       window.opener.form.UOM.value='<%=sUom%>';
      window.opener.form.TITLE.value='<%=sTitle%>';
      window.opener.form.MEDIUM.value='<%=sMedium%>';
      window.opener.form.ITEM_CONDITION.value='<%=sCondition%>';
      window.opener.form.REMARKS.value='<%=sRemarks%>';
      window.opener.form.STKQTY.value='<%=stkqty%>';
      window.opener.form.PRD_CLS_ID.value='<%=prd_cls_id%>';
       setCheckedValue( window.opener.form.ACTIVE,'<%=isactive%>');
      window.close();"><%=sItem1%></a>

		</td>

		<td class="main2"><%=strUtils.replaceCharacters2Recv(sDesc)%></td>
		<td class="main2"><%=sUom%></td>
		<td class="main2"><%=sArtist%></td>
		<td class="main2"><%=isactive%></td>
	</TR>
	<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
	<TR>
		<TH COLSPAN="8">&nbsp;</TH>
	</TR>
	<TR>
		<TH COLSPAN="8" align="center"><a href="#"
			onclick=
	window.close();;
><input type="submit" value="Close"></a></TH>
	</TR>
</table>
</body>
</html>





