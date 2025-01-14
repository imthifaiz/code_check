<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp" %>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script type="text/javascript">
function printpage()
  {
     response.sendRedirect("jsp/ShipConfirmPrint.jsp?DONO="+dono+"&action=View&SHIPPINGNO="+shipno);
  }
</script>
<title>Shipping Details</title>
<link rel="stylesheet" href="css/style.css">
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
   String plant=(String)session.getAttribute("PLANT");
   String shippingno=su.fString(request.getParameter("SHIPPINGNO"));
   String DONO    = su.fString(request.getParameter("DONO"));
   String action   = su.fString(request.getParameter("action")).trim();
   String sUserId = (String) session.getAttribute("LOGIN_USER");
   boolean confirm = false;
    
   DOUtil _DOUtil=new DOUtil();
   ItemMstDAO _ItemMstDAO=new ItemMstDAO();
   ArrayList al=null;
   logger.log(1,"OutBoundsOrderIssue.jsp : dono " + DONO + " Action : " + action);
    
   String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
   String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
   String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",chkString ="";
   String sSaveEnb    = "disabled";
   String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
   logger.log(0,"Stage 1 ");
   if(action.equalsIgnoreCase("View")){
    	  Map m=(Map)request.getSession().getAttribute("podetVal");
      	  fieldDesc=(String)request.getSession().getAttribute("RESULT");
          logger.log(0,"fieldDesc : " + fieldDesc.length());
          logger.log(0,"m : " + m.size());
          if(m.size()>0){
          	jobNum=(String)m.get("jobNum");
        	custName=(String)m.get("custName");
        	custCode=(String)m.get("custCode");
        	logger.log(0,"custCode : " + custCode);
           	personIncharge=(String)m.get("contactname");
        	contactNum=(String)m.get("contactNum");
        	telno=(String)m.get("telno");
        	email=(String)m.get("email");
        	add1=(String)m.get("add1");
        	add2=(String)m.get("add2");
        	add3=(String)m.get("add3");
        	add4=(String)m.get("add4");
        	country=(String)m.get("country"); 
        	zip=(String)m.get("zip");
        	remarks=(String)m.get("remarks");
           	contactNum=(String)m.get("contactNum");
        	address=(String)m.get("address");
        	address2=(String)m.get("address2");
        	address3=(String)m.get("address3");
        	deldate=(String)m.get("collectionDate");
        	collectionTime=(String)m.get("collectionTime");
        	remark1=(String)m.get("remark1");
        	remark2=(String)m.get("remarks");
          	al= _DOUtil.listShippingPrint(plant,DONO,shippingno);
          	logger.log(0,"jobNum : " + custCode);
      }
    }
%>
<FORM name="form" method="post"  >
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Shipping Details </font></table>
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
                     <th WIDTH="20%" ALIGN = "left">Shipping No:</th>
                     <td>
                       <P>
                         <a href="#" onClick="javascript:popUpWin('do_list_do.jsp?DONO='+form.DONO.value);">
                        
                         </a>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" name="SHIPPINGNO" value="<%=shippingno%>" class="inactivegry" readonly="readonly"/>
                     
                       </P>
                      </td>
                     <th WIDTH="20%" ALIGN = "left">Customer Name:</th>
                                 
                  <TD>
                   <INPUT name="CUST_NAME"   class="inactivegry" MAXLENGTH="20" readonly type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
                    <INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
                     <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
                   </TD>
                   </tr>
                    <tr>
                     <th WIDTH="20%" ALIGN = "left">OutBound Order:</th>
                     <td>
                       <INPUT type="TEXT" size="20" MAXLENGTH="20" name="DONO" value="<%=DONO%>" class="inactivegry" readonly="readonly"/>
                     </td>
                     <th WIDTH="20%" ALIGN = "left">Person Incharge:</th>
                     <td><INPUT type = "TEXT" size="20"   class="inactivegry" MAXLENGTH=20 readonly name="PERSON_INCHARGE" value="<%=personIncharge%>"></td>
                   </tr>
                    <tr>
                     <th WIDTH="20%" ALIGN = "left">Ref No:</th>
                     <td>
                     <INPUT type = "Hidden" size="20"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>">
                     <INPUT type="TEXT" size="20" class="inactivegry" MAXLENGTH="20" readonly name="JOB_NUM" value="<%=jobNum%>"/></td>
                      <th WIDTH="20%" ALIGN = "left">TelNo/ Email:</th>
                     <td>
                       <INPUT type="TEXT" size="20"  class="inactivegry" MAXLENGTH="20" readonly name="TELNO" value="<%=telno%>"/>
                         <INPUT type="TEXT" size="20" class="inactivegry"  readonly MAXLENGTH="20" name="EMAIL" value="<%=email%>"/>
                     </td>
                               
                    </tr>
                   <tr>
                     <th WIDTH="20%" ALIGN = "left">Order Date:</th>
                 
                       <td>
                         <INPUT type="TEXT" class="inactivegry" MAXLENGTH="20" readonly size="20" name="DELDATE" value="<%=deldate%>"/>
                       </td>
              
                    <th WIDTH="20%" ALIGN = "left" height="30">
                                    Unit No / Building:
                                  </th>
                     <td height="30">
                       <INPUT type="TEXT" size="20" class="inactivegry" readonly  MAXLENGTH="20" name="ADD1" value="<%=add1%>"/>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" class="inactivegry" readonly    name="ADD2" value="<%=add2%>"/>
                     </td>
                      </tr>
                   <tr>
                    <th WIDTH="20%" ALIGN = "left" height="30">Time:</th>
                     <td height="30">
                       <INPUT type="TEXT" size="20" class="inactivegry" MAXLENGTH="20" readonly name="COLLECTION_TIME" value="<%=collectionTime%>"/>
                     </td>
                    <th WIDTH="20%" ALIGN="left">
                                    Street/ City:
                                  </th>
                      <td>
                        <INPUT type="TEXT" size="20" class="inactivegry" MAXLENGTH="20" name="ADD3"  readonly  value="<%=add3%>"/>
                         <INPUT type="TEXT" size="20" class="inactivegry" MAXLENGTH="20" name="ADD4"  readonly  value="<%=add4%>"/>
                      </td>
                 
                    </tr>
                    <tr>
                      <th WIDTH="20%" ALIGN="left">Remarks:</th>
                      <td>
                        <INPUT type="TEXT" size="20" MAXLENGTH="20" class="inactivegry" readonly name="REMARK1" value="<%=remark1%>"/>
                      </td>
                      <th WIDTH="20%" ALIGN="left">Country/ Postal Code:</th>
                      <td>
                        <INPUT type="TEXT" size="20" class="inactivegry" MAXLENGTH="20" name="COUNTRY"  readonly  value="<%=country%>"/>
                         <INPUT type="TEXT" size="20" class="inactivegry" MAXLENGTH="20" name="ZIP"  readonly  value="<%=zip%>"/>
                      </td>
                    </tr>
                     <tr>
                     <th WIDTH="20%" ALIGN = "left">&nbsp;</th>
                     <td>&nbsp;</td>
                     <th WIDTH="20%" ALIGN = "left">Customer Remarks </th>
                      <td>
                       <INPUT type="TEXT" size="20" class="inactivegry"  MAXLENGTH="20" name="REMARK2"  readonly  value="<%=remarks%>"/>
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
         <th width="15%"><font color="#ffffff">Product ID</font></th>
         <th width="20%"><font color="#ffffff">Description </font></th>
          <th width="20%"><font color="#ffffff">Batch</font></th>
         <th width="10%"><font color="#ffffff">Issue Qty </font></th>
        </tr>
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#eeeeee">
     <% 
       if(al.size()>0)
       {
       for(int i=0; i<al.size();i++)
       {
          Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          String dono = (String)m.get("dono");
          String dolnno = (String)m.get("dolnno");
          String item= (String)m.get("item");
          String qtyis= (String)m.get("QtyPick");
          String desc= (String)m.get("ItemDesc");
      %>
          <TR bgcolor = "<%=bgcolor%>">
              <TD align="center" width="10%"><%=(String)m.get("dolnno")%></TD>
              <TD align="center" width="15%"><%=(String)m.get("item")%></TD>
              <TD align="center" width="20%"><%=desc%></TD>
              <TD align="center" width="10%"><%=(String)m.get("BATCH")%></TD>
              <TD align="center" width="10%"><%=(String)m.get("QtyPick")%></TD>
           </TR>
       <%}} else {%>
             <TR> <TD align="center" width="15%"> Data's Not Found For Issuing</TD></TR>
       <%}%>
    </table>

        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <div align="center"><center>
          <br>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                <input type="button" value="Back" onClick="window.location.href='OutBoundsOrderIssue.jsp?action=View&plant=<%=plant%>&DONO=<%=DONO%>'"/>
                    <input type="button" value="Print" name="Submit" onClick="window.location.href='ShipConfirmPrint.jsp?action=View&plant=<%=plant%>&DONO=<%=DONO%>&SHIPPINGNO=<%=shippingno%>'"  />
                    
                    </td>
                   <INPUT type="hidden" name="TRAVELER" value="">
                  <INPUT     name="LOGIN_USER"  type ="hidden" value="<%=sUserId %>" size="1"   MAXLENGTH=80 >
              </tr>
              <p>
              <p>
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
  <TR>
</FORM>
</HTML>
<% 
 logger.log(-1,"OutBoundOrderIssue.jsp  ");
%>
<%@ include file="footer.jsp" %>
