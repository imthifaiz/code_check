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


<title>Transfer Order</title>
<link rel="stylesheet" href="css/style.css">

<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />


<%
String plant=(String)session.getAttribute("PLANT");
String tono     = su.fString(request.getParameter("TONO"));
String action   = su.fString(request.getParameter("action")).trim();
String sUserId = (String) session.getAttribute("LOGIN_USER");
session.setAttribute("RFLAG","4");

logger.log(1,"maintTransferOrder.jsp : tono " + tono + " Action : " + action);

String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="";
 String fromWareHouse="",toWareHouse="";
String sSaveEnb    = "disabled";
String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
logger.log(0,"Stage 1 ");
String UNITPRICE=su.fString(request.getParameter("UNITPRICE"));

 if(action.equalsIgnoreCase("View")){

  Map m=(Map)request.getSession().getAttribute("todetVal");
  fieldDesc=(String)request.getSession().getAttribute("RESULT1");
  
  logger.log(0,"fieldDesc : " + fieldDesc.length());
  logger.log(0,"m : " + m.size());
  
   if(m.size()>0){
	   	jobNum=(String)m.get("jobNum");
       	fromWareHouse=(String)m.get("fromwarehouse");
   	   	toWareHouse=(String)m.get("towarehouse");
   	   	custName=(String)m.get("custName");
       	custCode=(String)m.get("custCode");
       	logger.log(0,"custCode : " + custCode);
       	personIncharge=(String)m.get("personInCharge");
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
       	remark2=(String)m.get("remark2");
   
       	logger.log(0,"jobNum : " + custCode);
      }
	}
     else if(action.equalsIgnoreCase("NEW")){
     	com.track.dao.ToHdrDAO _CustMstDAO=new   com.track.dao.ToHdrDAO();
     	tono=_CustMstDAO.getNextOrder("SIS");
     	deldate=du.getDate();
     	collectionTime=du.getTimeHHmm();
    }
  
%>
<body onload="window.print();window.location.href='javascript:history.back()';">
<FORM name="form" method="post"  >
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Maintain Transfer Order</font></table>
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
                     <th WIDTH="20%" ALIGN = "left"> Transfer Order : </th>
                     <td>
                       <P>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" name="TONO" value="<%=tono%>"/>
                       <!--   <input type="Submit" value="New" name="Submit" onclick="return onNew(document.form)"/> -->
                       </P>
                      </td>
                     <th WIDTH="20%" ALIGN = "left">Assignee Name:</th>
                                
                  <TD>
                   <INPUT name="CUST_NAME" type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
                   <INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
                     <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
                   </TD>
                   </tr>
                    <tr>
                    <th WIDTH="20%" ALIGN = "left">*From Loc</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="FROM_WAREHOUSE" value="<%=fromWareHouse%>">
                     </TD>
                    <th WIDTH="20%" ALIGN = "left">Person Incharge:</th>
                     <td><INPUT type = "TEXT" size="20"   class = "inactiveGry" MAXLENGTH=20 readonly name="PERSON_INCHARGE" value="<%=personIncharge%>"></td>
                   </tr>
                    <tr>
                      <th WIDTH="20%" ALIGN = "left">*To Loc</th>
                     <td>
                       <INPUT type="TEXT" size="20" MAXLENGTH="20" name="TO_WAREHOUSE" value="<%=toWareHouse%>"/>
                       </TD>    
                     <INPUT type = "Hidden" size="20"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>"></td>
                      <th WIDTH="20%" ALIGN = "left">TelNo/ Email:</th>
                     <td>
                       <INPUT type="TEXT" size="20"  class = "inactiveGry" MAXLENGTH="20" readonly name="TELNO" value="<%=telno%>"/>
                         <INPUT type="TEXT" size="20" class = "inactiveGry"  readonly MAXLENGTH="20" name="EMAIL" value="<%=email%>"/>
                     </td>
                         <tr>
                     <th WIDTH="20%" ALIGN = "left">Order Date:</th>
                   
                       <td>
                         <INPUT type="TEXT" size="20" MAXLENGTH="10" name="DELDATE" value="<%=deldate%>"/>
                       </td>
              
                    <th WIDTH="20%" ALIGN = "left" height="30">
                          Unit No / Building:
                        </th>
                     <td height="30">
                       <INPUT type="TEXT" size="20" class = "inactiveGry" readonly  MAXLENGTH="20" name="ADD1" value="<%=add1%>"/>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" class = "inactiveGry" readonly    name="ADD2" value="<%=add2%>"/>
                     </td>
                      </tr>
                   <tr>
                    <th WIDTH="20%" ALIGN = "left" height="30">Time:</th>
                     <td height="30">
                       <INPUT type="TEXT" size="20" MAXLENGTH="20" name="COLLECTION_TIME" value="<%=collectionTime%>"/>
                     </td>
                    <th WIDTH="20%" ALIGN="left">
                          Street/ City:
                        </th>
                      <td>
                        <INPUT type="TEXT" size="20" class = "inactiveGry" MAXLENGTH="20" name="ADD3"  readonly  value="<%=add3%>"/>
                         <INPUT type="TEXT" size="20" class = "inactiveGry" MAXLENGTH="20" name="ADD4"  readonly  value="<%=add4%>"/>
                      </td>
                 
                    </tr>
                    <tr>
                      <th WIDTH="20%" ALIGN="left">Remarks</th>
                      <td>
                        <INPUT type="TEXT" size="20" MAXLENGTH="20" name="REMARK1" value="<%=remark1%>"/>
                      </td>
                      <th WIDTH="20%" ALIGN="left">Country/ Postal Code:</th>
                      <td>
                        <INPUT type="TEXT" size="20" class = "inactiveGry" MAXLENGTH="20" name="COUNTRY"  readonly  value="<%=country%>"/>
                         <INPUT type="TEXT" size="20" class = "inactiveGry" MAXLENGTH="20" name="ZIP"  readonly  value="<%=zip%>"/>
                      </td>
                    </tr>
                      <tr>
                     <th WIDTH="20%" ALIGN = "left">&nbsp;</th>
                     <td>&nbsp;
                     </td>
                     <th WIDTH="20%" ALIGN = "left">Customer Remarks </th>
                     <td><INPUT readonly class = "inactiveGry" type = "TEXT" size="20"  MAXLENGTH=20 name="REMARK2" value="<%=remarks%>">
                   
                     
                     </td>
                   </tr>
                    </tr>
                    
                  </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
       <TABLE BORDER="1" CELLSPACING="0" WIDTH="100%" bgcolor="navy">
        <tr>
         <th width="10%"><font color="#ffffff">Order Line No </th>
         <th width="17%"><font color="#ffffff">Product ID </th>
         <th width="27%"><font color="#ffffff">Description </th>
         <th width="17%"><font color="#ffffff">Order Qty </th>
         <th width="14%"><font color="#ffffff">Pick Qty </th>
         <th width="10%"><font color="#ffffff">Issue Qty </th>
         <th width="5%"><font color="#ffffff">Status </th>
        </tr>
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#eeeeee">
      <%=fieldDesc%>
    </table>

        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="4">
        <INPUT type = "Hidden"  name="UNITPRICE"  value="<%=UNITPRICE%>">
        
        <div align="center"><center>
          <br>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  &nbsp;
                                 
                  </td>
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
