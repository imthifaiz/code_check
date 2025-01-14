<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<!--<script language="JavaScript" type="text/javascript" src="js/newPO.js"></script> -->
<SCRIPT LANGUAGE="JavaScript">
<!-- Begin

var subWin = null;

function popUpWin(URL) {
 subWin = window.open(URL, 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
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
    alert("Please Select Customer !");
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
   
	  document.form.PONO.value   ="DUMMY"
    document.form.CUST_NAME.value       ="DUMMY"
    form.JOB_NUM.value         =""
    form.PERSON_INCHARGE.value =""
    form.CONTACT_NUM.value     =""
    form.ADDRESS.value         =""
    form.DELDATE.value         =""
    form.COLLECTION_TIME.value =""
    form.REMARK1.value         =""
    form.REMARK2.value         =""
     
    return true;
  
    
}

//-->
</script>

<script type="text/javascript">
function printpage()
  {
  window.print()
  }
</script>

<title>Inbound Order</title>
<link rel="stylesheet" href="css/style.css">

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
    String pono     = su.fString(request.getParameter("PONO"));
    String action   = su.fString(request.getParameter("action")).trim();
    //
    
    //
    logger.log(1,"CreateIncomingOredr.jsp : pono " + pono + " Action : " + action);
    
    String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
    String remark1 = "",remark2 = "",address="",address2 ="",address3="",collectionDate="",collectionTime="";
    String sSaveEnb    = "disabled";
    String fieldDesc="<tr><td> Please enter any search criteria</td></tr>";
   
     if(action.equalsIgnoreCase("View")){
      try{
      logger.log(0,"Entering into action go ");
   
      java.util.Map m=( java.util.Map)request.getSession().getAttribute("podetVal");
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
       collectionDate=(String)m.get("collectionDate");
       collectionTime=(String)m.get("collectionTime");
       remark1=(String)m.get("remark1");
       remark2=(String)m.get("remark2");
      }
      //remove the session
      request.getSession().setAttribute("RESULT","");
      request.getSession().setAttribute("podetVal","");
    }
    catch(Exception e){
     logger.log(0,"Exception ::" + e.getMessage());
    }
  }
  else if(action.equalsIgnoreCase("NEW")){
     com.track.dao.PoHdrDAO _PoHdrDAO=new   com.track.dao.PoHdrDAO();
     pono=_PoHdrDAO.getNextOrder("SIS");
     collectionDate=du.getDate();
     collectionTime=du.getTimeHHmm();
        

  }
  
%>
<body onload="window.print();window.location.href='javascript:history.back()';">
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">INBOUND ORDER </font>
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
                     <th WIDTH="20%" ALIGN = "left"> InBound Order : </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="PONO" value="<%=pono%>"  >
                     </td>
                     <th WIDTH="20%" ALIGN = "left">Customer Name:</th>
                                      
                  <TD>
                   <INPUT name="CUST_NAME" type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
                   <INPUT type = "hidden" name="CUST_CODE" value = "">
                    <INPUT type = "hidden" name="CUST_CODE1"  value = "<%=custCode%>" >
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
                      <th WIDTH="20%" ALIGN = "left">Unit No:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="ADDRESS" value="<%=address%>"></td>
             
                    
                    </tr>
                   <tr>
                     <th WIDTH="20%" ALIGN = "left">Order Date: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=10 name="DELDATE" value="<%=collectionDate%>">&nbsp;<a href="javascript:show_calendar('form.DELDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></td>
              
                     <th WIDTH="20%" ALIGN = "left">Building: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="ADDRESS2" value="<%=address2%>"></td>
                      </tr>
                   <tr>
                     <th WIDTH="20%" ALIGN = "left">Time:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="COLLECTION_TIME" value="<%=collectionTime%>"></td>
                    <th WIDTH="20%" ALIGN = "left">Street:</th>
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
         <th width="5%"><font color="#ffffff">Order Line No </font></th>
        <th width="15%"><font color="#ffffff">Order Type </font></th>
           <th width="15%"><font color="#ffffff">Product ID </font></th>
         <th width="15%"><font color="#ffffff">Description </font></th>
         <th width="15%"><font color="#ffffff">Detail Description </font></th>
         <th width="10%"><font color="#ffffff">Manufacturer </font></th>
         <th width="10%">
           <FONT color="#ffffff">Order Quantity</FONT>&nbsp;
         </th>
           <th width="10%">
           <FONT color="#ffffff">Receive Quantity</FONT>&nbsp;
         </th>
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
                  <input type="Submit" value="Print" name="Submit" onclick="printpage()"/>
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
