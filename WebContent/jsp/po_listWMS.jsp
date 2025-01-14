<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>

<html>
<head>
<title>Order  list </title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form1">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR BGCOLOR="#000066" >
      <TH align="left"><font color="white">Order Number </font></TH>
      <TH align="left"><font color="white"><font color="white">Supplier</font>Name</font></TH>
   
    </TR>
<%

    POUtil itemUtil = new POUtil();
    StrUtils strUtils = new StrUtils();
    session= request.getSession();
    String plant = strUtils.fString((String)session.getAttribute("PLANT"));
    String pono = strUtils.fString(request.getParameter("ORDERNO"));
    System.out.println("Getting details for pono : " + pono);
    String sBGColor = "";
   try{

     Hashtable ht=new Hashtable();
     String extCond="";
     ht.put("PLANT",plant);
     ht.put("PONO",pono);
     System.out.println("A.PONO"+pono);

       if(pono.length()>0) extCond="a.plant='"+plant+"' and a.pono = '"+pono+"' and a.custname=b.vname";
     extCond=extCond+" order by a.pono desc";
   
     ArrayList listQry = itemUtil.getPoHdrDetailsReceiving("  a.pono,a.custName,a.jobNum,a.status,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3",ht,extCond);
         session.setAttribute("suplistqry",listQry);
    for(int i =0; i<listQry.size(); i++) {
   
     sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
      Map m=(Map)listQry.get(i);
      pono     = (String)m.get("pono");
  
     String custName    = (String)m.get("custName");
     String jobNum    = (String)m.get("jobNum");
     String status      =  (String)m.get("status");
     String cname  =  (String)m.get("contactname");
     String telno  =  (String)m.get("telno");
     String email  =  (String)m.get("email");
     String add1  =  (String)m.get("add1");
     String add2  =  (String)m.get("add2");
     String add3 =  (String)m.get("add3");
  


%>
    <TR bgcolor="<%=sBGColor%>">
      <td class="main2">
      <a href="#" onClick="window.opener.form.ORDERNO.value='<%=pono%>';
      
      window.opener.form.CUSTNAME.value='<%=custName%>';
      window.opener.form.CONTACTNAME.value='<%=cname%>';
      window.opener.form.TELNO.value='<%=telno%>';
      window.opener.form.EMAIL.value='<%=email%>';
      window.opener.form.ADD1.value='<%=add1%>';
      window.opener.form.ADD2.value='<%=add2%>';
       window.opener.form.ADD3.value='<%=add3%>';
      window.close();"><%=pono%></a>
      </td>
      <td class="main2"><%=strUtils.replaceCharacters2Recv(custName)%></td>

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





