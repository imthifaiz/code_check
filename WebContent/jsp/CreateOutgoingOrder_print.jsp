<%@ include file="header.jsp" %>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<SCRIPT LANGUAGE="JavaScript">


var subWin = null;

function popUpWin(URL) {
 subWin = window.open(URL, 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
</script>

<script type="text/javascript">

function printpage()
  {
  window.print();
  }

</script>


<title>Outbound Order</title>
<link rel="stylesheet" href="css/style.css">

<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />


<%
    String pono     = su.fString(request.getParameter("DONO"));
    String action   = su.fString(request.getParameter("action")).trim();
    
    String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
    String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
    String sSaveEnb    = "disabled";
    String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
     logger.log(0,"Stage 1 ");
     if(action.equalsIgnoreCase("View")){
    
      Map m=(Map)request.getSession().getAttribute("podetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULT");
      if(m.size()>0){
       jobNum=(String)m.get("jobNum");
       custName=(String)m.get("custName");
       custCode=(String)m.get("custCode");
       personIncharge=(String)m.get("personInCharge");
       contactNum=(String)m.get("telno");
       address=(String)m.get("add1");
       address2=(String)m.get("add2");
       address3=(String)m.get("add3");
       deldate=(String)m.get("collectionDate");
       collectionTime=(String)m.get("collectionTime");
       remark1=(String)m.get("remark1");
       remark2=(String)m.get("remark2");
        
      
      }
    }
     else if(action.equalsIgnoreCase("NEW")){
     com.track.dao.DoHdrDAO _CustMstDAO=new   com.track.dao.DoHdrDAO();
     pono=_CustMstDAO.getNextOrder("SIS");
     deldate=du.getDate();
     collectionTime=du.getTimeHHmm();
    }
  
%>
<body onload="window.print();window.location.href='javascript:history.back()';">
<FORM name="form" method="post"  >
 <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">PRINT OUTGOING ORDER </font></table>
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <font face="Times New Roman" size="2">
        <center>
          <table border="0" width="90%">
            <tr>
              <td width="100%">
        <center>
          <table border="0" width="90%">
            <tr>
              <td width="100%">
                <CENTER>
                  <TABLE BORDER="0" CELLSPACING=1 WIDTH="100%">
                   <tr>
                     <th WIDTH="20%" ALIGN = "left"> OutBound Order : </th>
                     <td>
                       <P>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" name="DONO" value="<%=pono%>"/>
                       </P>
                      </td>
                     <th WIDTH="20%" ALIGN = "left">Customer Name:</th>
                  <TD>
                   <INPUT name="CUST_NAME" type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
                   <INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
                   </TD>
                   </tr>
                    <tr>
                     <th WIDTH="20%" ALIGN = "left">Ref No: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="JOB_NUM" value="<%=jobNum%>"></td>
                     <th WIDTH="20%" ALIGN = "left">Person Incharge:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="PERSON_INCHARGE" value="<%=personIncharge%>"></td>
                   </tr>
                    <tr>
                     <th WIDTH="20%" ALIGN = "left">Contact No: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>"></td>
                      <th WIDTH="20%" ALIGN = "left">Address 1:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="ADDRESS" value="<%=address%>"></td>
             
                    
                    </tr>
                   <tr>
                     <th WIDTH="20%" ALIGN = "left">Order Date: </th>
                   
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=10 name="DELDATE" value="<%=deldate%>"></td>
              
                     <th WIDTH="20%" ALIGN = "left">Address 2: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="ADDRESS2" value="<%=address2%>"></td>
                      </tr>
                   <tr>
                     <th WIDTH="20%" ALIGN = "left">Time:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="COLLECTION_TIME" value="<%=collectionTime%>"></td>
                    <th WIDTH="20%" ALIGN = "left">Address 3:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="ADDRESS3" value="<%=address3%>"></td>
                 
                    </tr>
                      <tr>
                     <th WIDTH="20%" ALIGN = "left">Remark #1: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="REMARK1" value="<%=remark1%>"></td>
                     <th WIDTH="20%" ALIGN = "left">Remark #2:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="REMARK2" value="<%=remark2%>">
                      </td>
                   </tr>
                 
                  </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
       <TABLE BORDER="1" CELLSPACING="0" WIDTH="100%" bgcolor="navy">
        <tr>
      <th width="10%"><font color="#ffffff">Order Line No </font></th>
		<th width="11%"><font color="#ffffff">Order Type </font></th>
		<th width="11%"><font color="#ffffff">Product ID </font></th>
		<th width="18%"><font color="#ffffff">Description </font></th>
               <th width="11%"><font color="#ffffff">Unit Price </font></th>
		<th width="11%"><font color="#ffffff">Order Qty </font></th>
		<th width="11%"><font color="#ffffff">Pick Qty </font></th>
		<th width="11%"><font color="#ffffff">Issue Qty </font></th>
		<th width="5%"><font color="#ffffff">Status </font></th>
        </tr>
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#eeeeee">
      <%=fieldDesc%>
    </table>

        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <div align="center"><center>
          <br>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  <input type="Button" value="Cancel" onClick="window.location.href='../home'">
                  &nbsp;
                  <input type="Submit" value="Print" name="Submit" onclick="printpage()"/></td>
              </tr>
            </table>
          </center>
        </div>
        <div align="center">
          <center>
            <p>&nbsp;</p>
          </center>
        </div>
        </font></td>
    </tr>
  </table>
</FORM>

<%@ include file="footer.jsp" %>
