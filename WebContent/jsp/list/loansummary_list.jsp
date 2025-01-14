<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Loan Assignee List</title>
<link rel="stylesheet" href="../css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
 <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
    CustUtil custUtils = new CustUtil();
    StrUtils strUtils = new StrUtils();
    
    custUtils.setmLogger(mLogger);
    String plant= (String)session.getAttribute("PLANT");
    String type= strUtils.fString(request.getParameter("TYPE"));
    String sCustName = strUtils.fString(request.getParameter("CUST_NAME"));
      String sCustCode = strUtils.fString(request.getParameter("CUST_CODE"));
     String ordno = strUtils.fString(request.getParameter("DONO"));
     String sItem = strUtils.fString(request.getParameter("ITEM"));
  
    String sBGColor = "";
   try{
 if(type.equalsIgnoreCase("LOAN_ASSIGNEE")){
    ArrayList arrCust = custUtils.getLoanAssigneeListStartsWithName(sCustName,sCustCode,plant," AND ISACTIVE='Y'");
    %>
     
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Assignee Code</font></TH>
      <TH align="left"><font color="white">Assignee Name</font></TH>
    </TR>
    <%
    for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrCustLine = (Map)arrCust.get(i);
         sCustCode     = (String)arrCustLine.get("LASSIGNNO");
        String sCustName1     = strUtils.removeQuotes((String)arrCustLine.get("CNAME"));
  %>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.CUST_NAME.value='<%=sCustName1%>';window.opener.form1.CUST_CODE.value='<%=sCustCode%>'; window.close();"><%=sCustName1%></a></td>
      <td class="main2"><%=sCustCode%></td>
    
   </TR>
  <% }}
  }catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}%>
  
   <%  
   try{
   if(type.equalsIgnoreCase("LOAN_ORDER")){
      Hashtable ht=new Hashtable();
      ht.put("PLANT",plant);
       LoanUtil _loanUtil = new LoanUtil();
       _loanUtil.setmLogger(mLogger);
     String extCond = "";
      if(ordno.length()>0) extCond=" AND plant='"+plant+"' and ORDNO like '"+ordno+"%'";
     extCond=extCond+" order by ORDNO desc";
     ArrayList listQry = _loanUtil.getLoanHdrList("ORDNO,custName,custCode,jobNum,status",ht,extCond);
     %>
    
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Loan Order No</font></TH>
      <TH align="left"><font color="white">Assignee Name</font></TH>
    </TR>
    <%
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
      Map m=(Map)listQry.get(i);
      ordno     = (String)m.get("ORDNO");
     String custName    = (String)m.get("custName");
     String custCode    = (String)m.get("custCode");
     String jobNum    = (String)m.get("jobNum");
     String status      =  (String)m.get("status");
     %>
     
      <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick="window.opener.form1.DONO.value='<%=ordno%>'; window.close();"><%=ordno%></a>
      </td>
      <td class="main2"><%=strUtils.replaceCharacters2Recv(custName)%></td>
</TR>
 <% }  } }catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}%>
 
 <%  try{ 
      if(type.equalsIgnoreCase("ITEM_LIST")){
        ItemUtil itemUtil = new ItemUtil();
        itemUtil.setmLogger(mLogger);
        String extraCon =" ITEM LIKE '"+sItem+"%'";
        ArrayList listQry = itemUtil.getItemList(plant," ISACTIVE='Y'");
         %>
    
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Product ID</font></TH>
      <TH align="left"><font color="white">Description</font></TH>
    </TR>
    <%
        for(int i =0; i<listQry.size(); i++) {
       
         sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
          Map m=(Map)listQry.get(i);
          sItem     = (String)m.get("item");
          String sDesc =(String)m.get("itemDesc");
   %>
   
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2"><a href="#" onClick="window.opener.form1.ITEM.value='<%=sItem%>';window.close();"><%=sItem%></a></td>
      <td class="main2"><%=strUtils.replaceCharacters2Recv(sDesc)%></td>
     
</TR>
   
   <% } }}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}%>


   <TR>
        <TH COLSPAN="8">&nbsp;</TH>
   </TR>
    <TR>
      <TH COLSPAN="8" align="center"><a href="#" onclick="window.close();"><input type="submit" value="Close"></a></TH>
    </TR>
  </table>
</form>
</body>
</html>





