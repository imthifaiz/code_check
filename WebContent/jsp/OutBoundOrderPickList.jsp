<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.DoDetDAO"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<title>OutBound Order List</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Order No </font></TH>
       <TH align="left"><font color="white">Customer Name</font></TH>
    </TR>
<%
    session = request.getSession();
    DoDetDAO  _DoDetDAO  = new DoDetDAO();  
    StrUtils strUtils = new StrUtils();
    String PLANT = strUtils.fString((String)session.getAttribute("PLANT"));
    String ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
    String sBGColor = "";
    try{
   
    	List listQry =  _DoDetDAO.getOutBoundOrderDetailsByWMS(PLANT,ORDERNO);
    	session.setAttribute("customerlistqry",listQry);
       	for(int i =0; i<listQry.size(); i++) {
   
     		sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
     		Map m=(Map)listQry.get(i);
     		String sOrderno=(String)m.get("dono");
     		String sOrderlno=(String)m.get("dolnno");
     		String sCustname    = (String)m.get("custname");
     		String cname  =  (String)m.get("contactname");
     		String telno  =  (String)m.get("telno");
     		String email  =  (String)m.get("email");
     		String add1  =  (String)m.get("add1");
     		String add2  =  (String)m.get("add2");
     		String add3 =  (String)m.get("add3");
 %>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick="window.opener.form.ORDERNO.value='<%=sOrderno%>';
        window.opener.form.CUSTNAME.value='<%=sCustname%>';
        window.opener.form.CONTACTNAME.value='<%=cname%>';
        window.opener.form.TELNO.value='<%=telno%>';
        window.opener.form.EMAIL.value='<%=email%>';
        window.opener.form.ADD1.value='<%=add1%>';
        window.opener.form.ADD2.value='<%=add2%>';
        window.opener.form.ADD3.value='<%=add3%>';
        window.close();"><%=sOrderno%></a>
      </td>
       <td class="main2"><%=sCustname%></td>
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