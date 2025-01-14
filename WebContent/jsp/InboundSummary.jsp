<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<!--<script language="JavaScript" type="text/javascript" src="js/newPO.js"></script> -->
<SCRIPT LANGUAGE="JavaScript">
<!-- Begin

var subWin = null;
/*
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
*/
function popUpWin(URL) {
 subWin = window.open(URL, 'InboundSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}


function validatePO(form)
{
	if (form.PONO.value.length < 1)
    {
    alert("Please Enter PO Number !");
    form.PONO.focus();
    return false;
    }
    
    if (form.CUST_NAME.value.length < 1)
    {
    alert("Please Select Supplier !");
    form.CUST_NAME.focus();
    return false;
    }
    
    
}

function popWin(vname){
    window.open('vendSumm.jsp?VENDNAME='+vname+'&P=Y');
}

function onDelete(form)
{
   
	if (form.PONO.value.length < 1)
    {
    alert("Please Enter PO Number !");
    form.PONO.focus();
    return false;
    }
    else{
     var mes=confirm("Do you want to delete the incoming order !");
      if(mes==true)
      {
      return true;
      }
      else
      {  
      return  false;
      }
    }
    
}
function onUpdate(form)
{
   
	if (form.PONO.value.length < 1)
    {
    	alert("Please Enter PO Number !");
    	form.PONO.focus();
    	return false;
    }
    else{
     	var mes=confirm("Do you want to update the incoming order !");
      	if(mes==true)
      	{
      		return true;
      	}
      else
      {  
      return  false;
      }
    }
    
}

function onNew(form)
{
	document.form.PONO.value		="DUMMY";
    document.form.CUST_NAME.value	="DUMMY";
    form.JOB_NUM.value				="";
    form.PERSON_INCHARGE.value		="";
    form.CONTACT_NUM.value			="";
    form.TELNO.value				="";
    form.EMAIL.value				="";
    form.ADDRESS.value				="";
    form.ADDRESS2.value				="";
    form.ADDRESS3.value				="";
    form.DELDATE.value         		="";
    form.COLLECTION_TIME.value 		="";
    form.REMARK1.value         		="";
    form.REMARK2.value         		="";
    return true;

}

//-->
</script>

<title>Inbound Order Summary</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<!--<jsp:useBean id="_poUtil" class="com.track.db.util.POUtil" /> -->

<%
 	session = request.getSession();
    String pono     = su.fString(request.getParameter("PONO"));
    String action   = su.fString(request.getParameter("action")).trim();
    String plant    = su.fString((String)session.getAttribute("PLANT"));
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    String RFLAG=    (String) session.getAttribute("RFLAG");
  
    
    String vend = "",deldate="",jobNum = "",custName = "",personIncharge = "",contactNum = "";
    String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="",
    contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="";
    String sSaveEnb    = "disabled";
    String fieldDesc="<tr><td> Please enter any search criteria</td></tr>";
   
     if(action.equalsIgnoreCase("View")){
      try{
      logger.log(0,"Entering into action go ");
   
      Map m=(Map)request.getSession().getAttribute("podetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULT");
      
       logger.log(0,"fieldDesc : " + fieldDesc.length());
       logger.log(0,"m : " + m.size());
       if(m.size()>0){
     //  logger.log(0,"Getting value from map");
       jobNum=(String)m.get("jobNum");
       custName=(String)m.get("custName");
       personIncharge=(String)m.get("contactname");
       contactNum=(String)m.get("contactNum");
       telno=(String)m.get("telno");
       email=(String)m.get("email");
       add1=(String)m.get("add1");
       add2=(String)m.get("add2");
       add3=(String)m.get("add3");
       add4=(String)m.get("add4");
       country=(String)m.get("country"); zip=(String)m.get("zip");
       remarks=(String)m.get("remarks");
       address=(String)m.get("address");
       address2=(String)m.get("address2");
       address3=(String)m.get("address3");
       collectionDate=(String)m.get("collectionDate");
       collectionTime=(String)m.get("collectionTime");
       remark1=(String)m.get("remark1");
       remark2=(String)m.get("remark2");
    
           
       }
       logger.log(0,"jobNum : " + jobNum);
      //remove the session
      request.getSession().setAttribute("RESULT","");
      request.getSession().setAttribute("podetVal","");
    }
    catch(Exception e){
     logger.log(0,"Exception ::" + e.getMessage());
    }
  }
  else if(action.equalsIgnoreCase("NEW")){
     sSaveEnb  = "enabled";
     com.track.dao.PoHdrDAO _PoHdrDAO=new   com.track.dao.PoHdrDAO();
     pono=_PoHdrDAO.getNextOrder(plant);
     collectionDate=du.getDate();
     collectionTime=du.getTimeHHmm();

  }
  
%>
<FORM name="form" method="post" action="/track/purchaseorderservlet?" onSubmit="return validatePO(document.form)">
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Inbound Order Summary </font>
  </table>
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
                     <th WIDTH="10%" ALIGN = "left"> InBound Order : </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="PONO" value="<%=pono%>"  >
                     <a href="#" onClick="javascript:popUpWin('po_list_po.jsp?PONO='+form.PONO.value);">
                       <img src="images/populate.gif" border="0"/>
                     </a>
                     <input type="Submit" value="View" name="Submit">
                   <!--   <input type="Submit" value="New" name="Submit" onclick="return onNew(document.form)" /></td>-->
                     <th WIDTH="20%" ALIGN = "left">
                       <P>Supplier Name:</P>
                     </th>
                   
                    
                  <TD>
                   <INPUT name="CUST_NAME" type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
                  <!--  <a href="#" onClick="javascript:popUpWin('vendor_hdrlist.jsp?CUST_NAME='+form.CUST_NAME.value);"><img src="images/populate.gif" border="0"></a> -->
                   <INPUT type = "hidden" name="CUST_CODE" value = "">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "">
                      <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
                   </TD>
                   </tr>
                    <tr>
                     <th WIDTH="15%" ALIGN = "left">Ref No: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="JOB_NUM" value="<%=jobNum%>"></td>
                     <th WIDTH="20%" ALIGN = "left">Person Incharge:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20  class = "inactiveGry" readonly name="PERSON_INCHARGE" value="<%=personIncharge%>"></td>
                   </tr>
                       <tr>
                     <th WIDTH="15%" ALIGN = "left">Order Date:</th>
                     <td>
                       <INPUT type="TEXT" size="20" MAXLENGTH="10" name="DELDATE" value="<%=collectionDate%>"/>
                      <!--   <a href="javascript:show_calendar('form.DELDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;">
                         <img src="images\show-calendar.gif" width="24" height="22" border="0"/>
                       </a>-->
                     </td>
                      <th WIDTH="15%" ALIGN = "left">TelNo/ Email:</th>
                     <td>
                       <INPUT type="TEXT" size="20"  class = "inactiveGry" MAXLENGTH="20" readonly name="TELNO" value="<%=telno%>"/>
                         <INPUT type="TEXT" size="20" class = "inactiveGry"  readonly MAXLENGTH="20" name="EMAIL" value="<%=email%>"/>
                     </td>
             
                    
                    </tr>
                   <tr>
                     <th WIDTH="15%" ALIGN = "left">Time</th>
                   <!--  <td><INPUT type = "TEXT" size="20"  MAXLENGTH=10  name="DELDATE" value="<%=deldate%>">&nbsp;<a href="javascript:show_calendar('form.DELDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></td>-->
                       <td>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" name="COLLECTION_TIME" value="<%=collectionTime%>"/>
                       </td>
                       <th WIDTH="20%" ALIGN = "left" height="30">Unit No/ Building:</th>
                     <td height="30">
                       <INPUT type="TEXT" size="20" class = "inactiveGry" readonly  MAXLENGTH="20" name="ADD1" value="<%=add1%>"/>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" class = "inactiveGry" readonly    name="ADD2" value="<%=add2%>"/>
                     </td>
              </tr>
                  <tr>
                     <th WIDTH="15%" ALIGN = "left" height="30">Remarks</th>
                     <td height="30">
                       <INPUT type="TEXT" size="20" MAXLENGTH="20" name="REMARK1" value="<%=remark1%>"/>
                     </td>
                    <th WIDTH="20%" ALIGN="left">Street/ City:</th>
                      <td>
                        <INPUT type="TEXT" size="20" class = "inactiveGry" MAXLENGTH="20" name="ADD3"  readonly  value="<%=add3%>"/>
                         <INPUT type="TEXT" size="20" class = "inactiveGry" MAXLENGTH="20" name="ADD4"  readonly  value="<%=add4%>"/>
                      </td>
                    </tr>
                    <tr>
                      <th WIDTH="15%" ALIGN="left">&nbsp;</th>
                      <td>&nbsp;</td>
                      <th WIDTH="15%" ALIGN="left">Country/ Postal Code:</th>
                      <td>
                        <INPUT type="TEXT" size="20" class = "inactiveGry" MAXLENGTH="20" name="COUNTRY"  readonly  value="<%=country%>"/>
                         <INPUT type="TEXT" size="20" class = "inactiveGry" MAXLENGTH="20" name="ZIP"  readonly  value="<%=zip%>"/>
                      </td>
                    </tr>
                      <tr>
                     <th WIDTH="15%" ALIGN = "left">&nbsp;</th>
                     <td>&nbsp;</td>
                     <th WIDTH="20%" ALIGN = "left">Supplier Remarks </th>
                     <td><INPUT type = "TEXT" size="20" class = "inactiveGry" readonly MAXLENGTH=20 name="REMARK2" value="<%=remarks%>">
                   
                  <!--   <input type="Submit" value="Delete" name="Submit" onclick="return onDelete(document.form)" disabled /> 
                 <!--    <input type="Submit" value="Update" name="Submit" onclick="return onUpdate(document.form)" /> -->
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
        <th width="10%"><font color="#ffffff">Order Line No </th>
         <th width="20%"><font color="#ffffff">Product ID </th>
         <th width="30%"><font color="#ffffff">Description </th>
         <th width="15%">
           <FONT color="#ffffff">Order Quantity</FONT>&nbsp;
         </th>
           <th width="15%">
           <FONT color="#ffffff">Receive Quantity</FONT>&nbsp;
         </th>
         <th width="10%"><font color="#ffffff">Status </th>
      </tr>
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#eeeeee">
      <%=fieldDesc%>
    </table>
    <td><INPUT type = "hidden" size="20"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>"></td>
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="2">
        <div align="center"><center>
          <br>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  <input type="Button" value="Cancel" onClick="window.location.href='../home'">
                  &nbsp;
                   <!--  <input type="Submit" value="Save" name="Submit"  />
                  <input type="Submit" value="Add" name="Submit"> -->
                  <input type="Submit" value="Print" name="Submit"/>
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
<% 

 logger.log(-1,"CreateIncomingOredr.jsp  ");
%>
<%@ include file="footer.jsp" %>
