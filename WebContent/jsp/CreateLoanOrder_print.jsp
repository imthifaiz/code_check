<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<SCRIPT LANGUAGE="JavaScript">
<!-- Begin

var subWin = null;

function popUpWin(URL) {
 subWin = window.open(URL, 'LOANORDER', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function sf()
{
document.form.PONO.focus();
}

function validatePO(form)
{

//alert("length CUST_NAME : " + form.CUST_NAME.value.length);
	if (form.DONO.value.length < 1)
  {
    alert("Please Enter Loan Order Number !");
    form.DONO.focus();
    return false;
  }
 
  if (form.CUST_NAME.value.length < 1)
  {
    alert("Please Enter Loan Assignee !");
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
    alert("Please Enter Loan Order Number !");
    form.PONO.focus();
    return false;
    }
    else{
     var mes=confirm("Do you want to delete the Loan order !");
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
   
	  document.form.DONO.value   ="DUMMY"
    document.form.CUST_NAME.value       ="DUMMY"
    form.JOB_NUM.value         =""
    form.PERSON_INCHARGE.value =""
    form.CONTACT_NUM.value     =""
    form.ADDRESS.value         =""
    form.DELDATE.value         =""
    form.COLLECTION_TIME.value =""
    form.REMARK1.value         =""
    form.REMARK2.value         =""
    form.FRLOC.value         =""
    form.TOLOC.value         =""
     
    return true;
  
    
}

//-->
</script>

<script type="text/javascript">
<!-- Begin
function printpage()
  {
 // alert("printing starts");
  window.print();
  }
//-->
</script>


<title>Print Loan Order </title>
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


<%
    String pono     = su.fString(request.getParameter("DONO"));
    String action   = su.fString(request.getParameter("action")).trim();

    
    String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
    String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="",frLoc="",toLoc="";
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
       frLoc=(String)m.get("frLoc");
       toLoc=(String)m.get("toLoc");
      }
    }
    
  
%>
<body onload="window.print();window.location.href='javascript:history.back()';">
<FORM name="form" method="post" action="/track/loanorderservlet?" onSubmit="return validatePO(document.form)">
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">PRINT LOAN ORDER </font></table>
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
                     <th WIDTH="20%" ALIGN = "left"> Loan Order : </th>
                     <td>
                       <P>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" name="DONO" readonly value="<%=pono%>"/>
                       </P>
                      </td>
                     <th WIDTH="20%" ALIGN = "left">Loan Assignee Name:</th>
                  <TD>
                   <INPUT name="CUST_NAME" type = "TEXT" value="<%=su.forHTMLTag(custName)%>" readonly size="30"  MAXLENGTH=80>
                   <INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
                   </TD>
                   </tr>
                    <tr>
                     <th WIDTH="20%" ALIGN = "left">FrLoc: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="FRLOC" readonly value="<%=frLoc%>"></td>
                     <th WIDTH="20%" ALIGN = "left">ToLoc:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="TOLOC" readonly value="<%=toLoc%>"></td>
                   </tr>
                    <tr>
                     <th WIDTH="20%" ALIGN = "left">Ref No: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="JOB_NUM" readonly value="<%=jobNum%>"></td>
                     <th WIDTH="20%" ALIGN = "left">Person Incharge:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="PERSON_INCHARGE" readonly value="<%=personIncharge%>"></td>
                   </tr>
                    <tr>
                     <th WIDTH="20%" ALIGN = "left">Contact No: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="CONTACT_NUM" readonly value="<%=contactNum%>"></td>
                      <th WIDTH="20%" ALIGN = "left">Unit No:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="ADDRESS"  readonly value="<%=address%>"></td>
             
                    
                    </tr>
                   <tr>
                     <th WIDTH="20%" ALIGN = "left">Order Date: </th>
                   
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=10 name="DELDATE" readonly value="<%=deldate%>"></td>
              
                     <th WIDTH="20%" ALIGN = "left">Building: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="ADDRESS2" readonly value="<%=address2%>"></td>
                      </tr>
                   <tr>
                     <th WIDTH="20%" ALIGN = "left">Time:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="COLLECTION_TIME" readonly  value="<%=collectionTime%>"></td>
                    <th WIDTH="20%" ALIGN = "left">Street:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="ADDRESS3" readonly value="<%=address3%>"></td>
                 
                    </tr>
                      <tr>
                     <th WIDTH="20%" ALIGN = "left">Remark #1: </th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="REMARK1" readonly value="<%=remark1%>"></td>
                     <th WIDTH="20%" ALIGN = "left">Remark #2:</th>
                     <td><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="REMARK2" readonly value="<%=remark2%>">
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
         <th width="17%"><font color="#ffffff">Product ID</font></th>
         <th width="27%"><font color="#ffffff">Description </font></th>
         <th width="17%"><font color="#ffffff">Order Qty </font></th>
        
        </tr>
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#eeeeee">
      <%=fieldDesc%>
    </table>

        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
       
       </td>
    </tr>
  </table>
  </body>
</FORM>

<%@ include file="footer.jsp" %>
