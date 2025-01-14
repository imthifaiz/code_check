<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<!-- Not in Use -->
<html>
<head>
<title>Item List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Item No </font></TH>
      <TH align="left"><font color="white">Description</font></TH>
      <TH align="left"><font color="white">UOM</font></TH>
    </TR>
<%
    ItemUtil itemUtil = new ItemUtil();
    StrUtils strUtils = new StrUtils();
    String sItem = strUtils.fString(request.getParameter("ITEM"));

    String sBGColor = "";
   try{
    List listQry = itemUtil.qryItemMst(sItem);
    for(int i =0; i<listQry.size(); i++) {
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     Vector vecItem   = (Vector)listQry.get(i);
     String sItem1     = (String)vecItem.get(0);
	 String sDesc     = strUtils.replaceCharacters2Send1((String)vecItem.get(1));
     String sUom      = (String)vecItem.get(2);
	  String sDesc1 = strUtils.insertEscp(sDesc);

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.ITEM.value='<%=sItem1%>';window.opener.form1.DESC.value='<%=sDesc1%>';window.opener.form1.UOM.value='<%=sUom%>';window.close();"><%=sItem1%></td>
      <td class="main2"><%=strUtils.replaceCharacters2Recv(sDesc)%></td>
      <td class="main2"><%=sUom%></td>
</TR>
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
   <TR>
        <TH COLSPAN="8">&nbsp;</TH>
   </TR>
    <TR>
      <TH COLSPAN="8" align="center"><a href="#" onclick="window.close();"><input type="submit" value="Close"></a></TH>
    </TR>
  </table>

</body>
</html>





