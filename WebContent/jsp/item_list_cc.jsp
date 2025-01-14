<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<!-- Not in Use -->
<html>
<head>
<title>Product List </title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Item No </font></TH>
      <TH align="left"><font color="white">Description</font></TH>
    </TR>
<%
  
    InvUtil itemUtil = new InvUtil();
    StrUtils strUtils = new StrUtils();
    String PLANT = (String)session.getAttribute("PLANT");
    String sItem = strUtils.fString(request.getParameter("ITEM"));
    String sCustomerCode = strUtils.fString(request.getParameter("CUST_CODE"));

    String extCond="";
    if(sItem.length()>0)  extCond=extCond+" and item like '"+sItem+"%'";
    if(sCustomerCode.length()>0)  extCond=extCond+" and userfld2 like '"+sCustomerCode+"%'";
    String sBGColor = "";
   try{
   
     ArrayList listQry = itemUtil.getItemListFromInv("sis","",extCond);
    com.track.dao.ItemMstDAO _ItemMstDAO= new com.track.dao.ItemMstDAO();
    com.track.dao.CustMstDAO _CustMstDAO= new com.track.dao.CustMstDAO();
    
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
      Map m=(Map)listQry.get(i);
     String sItem1     = (String)m.get("item");
     String sDesc = _ItemMstDAO.getItemDesc("sis",sItem1);
     sDesc      = strUtils.replaceCharacters2Send1(sDesc);
     String sStatus    = (String)m.get("status");
       String sCustName ="";   
      String sDesc1 = strUtils.insertEscp(sDesc);

%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick="window.opener.form.ITEM.value='<%=sItem1%>';
      window.opener.form.DESC.value='<%=sDesc1%>';
      window.close();"><%=sItem1%></a>
      </td>
      <td class="main2"><%=strUtils.replaceCharacters2Recv(sDesc)%></td>
      <td class="main2"><%=sStatus%></td>
      <td class="main2"><%=sCustName%></td>
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





