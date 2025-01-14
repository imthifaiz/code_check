<%@ include file="header.jsp" %>
<%@page import="com.track.constants.IDBConstants"%>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
/*
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
*/
function popUpWin(URL) {
 subWin = window.open(URL, 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

function sf()
{
document.form.DONO.focus();
}

function validateDO(form)
{


	if (form.DONO.value.length < 1)
  {
    alert("Please Enter Order Number !");
    form.DONO.focus();
    return false;
  }
 
 
  
   if (form.DELDATE.value.length > 1){
    if (isDate(form.DELDATE.value)==false){
		form.DELDATE.focus();
		return false;
    }
   }
  
  
}

function popWin(vname){
    window.open('vendSumm.jsp?VENDNAME='+vname+'&P=Y');
}

function onDelete(form)
{
	  if (form.DONO.value.length < 1)
	    {
	    alert("Please Enter Order Number !");
	    form.DONO.focus();
	    return false;
	    }
          
	  else{
		     var mes=confirm("Do you want to Delete the Mobile order !");
		      if(mes==true)
		      {
		      
		      document.form.action="/track/deleveryorderservlet?Submit=REMOVE_DO";
		      document.form.submit();
		      }
		      else
		      {  
		      return  false;
		      }
		    }
	}

function onUpdate(form)
{
   
	if (form.DONO.value.length < 1)
    {
    alert("Please Enter Order Number !");
    form.DONO.focus();
    return false;
    }
    if (form.CUST_CODE1.value.length < 1)
      {
        alert("Please select valid Customer Name !");
        form.CUST_NAME.focus();
        form.CUST_NAME.select();
        return false;
      }
    if (form.DISPLAY.value.length < 1)
    {
    	alert("Please Select Currency ID");
    	form.DISPLAY.focus();
            form.DISPLAY.select();
    	return false;
		    
    }
      if (form.DELDATE.value.length > 1){
            if (isDate(form.DELDATE.value)==false){
                        form.DELDATE.focus();
                        return false;
            }
             else{
     var mes=confirm("Do you want to update the Mobile Order !");
      if(mes==true)
      {
      document.form.action="/track/deleveryorderservlet?Submit=Update";

      document.form.submit();
      }
      else
      {  
      return  false;
      }
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
    form.ADDRESS2.value         =""
    form.ADDRESS3.value         =""
    form.DELDATE.value         =""
    form.COLLECTION_TIME.value =""
    form.REMARK1.value         =""
    form.REMARK2.value         =""
    form.DELIVERYDATE.value         =""
    form.TIMESLOTS.value         =""
     
    return true;
  
    
}

function onClear(){
	 
            document.form.DONO.value   =""
            document.form.CUST_NAME.value       =""
            form.JOB_NUM.value         =""
            form.PERSON_INCHARGE.value =""
            form.CONTACT_NUM.value     =""
            form.COLLECTION_TIME.value  =""
            form.DELDATE.value         =""
            form.COLLECTION_TIME.value =""
            form.REMARK1.value         =""
            form.REMARK2.value         =""
            
            form.CUST_NAME.value         =""
            form.PERSON_INCHARGE.value   =""
            form.TELNO.value         =""
            form.EMAIL.value         =""
            form.ADD1.value         =""
            form.ADD2.value         =""
            form.ADD3.value         =""
            form.ADD4.value         =""
            form.COUNTRY.value         =""
            form.ZIP.value         =""
            form.DELIVERYDATE.value         =""
            form.TIMESLOTS.value         =""
              
            return true;
	}


</script>

<title>Maint Mobile Order</title>
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


<%
session = request.getSession();
    String plant=(String)session.getAttribute("PLANT");
    String pono     = su.fString(request.getParameter("DONO"));
    String action   = su.fString(request.getParameter("action")).trim();
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    session.setAttribute("RFLAG","6");
 
    String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
    String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
    String contactname="",telno="",hpno="",email="",add1="",currencyid="",add2="",add3="",add4="",country="",zip="",remarks="";
    String sSaveEnb    = "disabled";
    String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
    String DELIVERYDATE="",TIMESLOTS="";
    
     if(action.equalsIgnoreCase("View")){
    
      Map m=(Map)request.getSession().getAttribute("podetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULT");
      
       if(m.size()>0){
    	   
    	   currencyid = (String)m.get("currencyid");
    	   jobNum=(String)m.get("jobNum");
       	   custName=(String)m.get("custName");
           custCode=(String)m.get("custCode");
           personIncharge=(String)m.get("contactname");
           contactNum=(String)m.get("contactNum");
           telno=(String)m.get("hpno");
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
           DELIVERYDATE=(String) m.get("deliverydate");
		   TIMESLOTS=(String) m.get("timeslots");
       
      }
   //   request.getSession().setAttribute("RESULT","");
   //   request.getSession().setAttribute("podetVal","");
    }
     else if(action.equalsIgnoreCase("NEW")){
        
        sSaveEnb  = "enabled";
        com.track.dao.DoHdrDAO _CustMstDAO=new   com.track.dao.DoHdrDAO();
        _CustMstDAO.setmLogger(mLogger);
        pono=_CustMstDAO.getNextOrder(plant);
        deldate=du.getDate();
        collectionTime=du.getTimeHHmm();
      
     
  }
  
%>
<FORM name="form" method="post" action="/track/deleveryorderservlet?" onSubmit="return validatePO(document.form)">
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Maint Mobile Order </font></table>
  <br>
  <%
  String statusValue = su.fString(request.getParameter("statusValue"));
  if(statusValue.equals("100")) {
	  out.println("<div><b><font color='green'><center>Mobile Order "+pono+" has sucessfully Deleted!</center></font></b></div>");
  }
  if(statusValue.equals("99")) {
	  out.println("<div><b><font color='red'><center>Mobile Order "+pono+"  does not exist</center></font></b></div>");
  }
  if(statusValue.equals("98")) {
	  out.println("<div'><b><font color='red'><center>Mobile Order "+pono+" is in use. Cannot Deleted!</center></font></b></div>");
  }
  if(statusValue.equals("97")) {
	  out.println("<div'><b><font color='red'><center>Unable to remove Mobile Order "+pono+". Some Error Occured!</center></font></b></div>");
  }
  %>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <font face="Times New Roman" size="2">
        <center>
          <table border="0" width="100%">
            <tr>
              <td width="100%">
        <center>
          <table border="0" width="100%">
            <tr>
              <td width="100%">
                <CENTER>
                  <TABLE BORDER="0" CELLSPACING=1 WIDTH="100%">
                   <tr>
                     <th WIDTH="20%" ALIGN = "left"> Order No : </th>
                     <td>
                       <P>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" name="DONO" value="<%=pono%>"/>
                         <a href="#" onClick="javascript:popUpWin('do_list_do.jsp?DONO='+form.DONO.value);">
                           <img src="images/populate.gif" border="0"/>
                         </a>
                         <input type="Submit" value="View" name="Submit" onclick="return validateDO(document.form)"/>
                       </P>
                        <th WIDTH="20%" ALIGN="left">Handphone No</th>
				<td><INPUT type="TEXT" size="25" class="inactiveGry" MAXLENGTH="20" readonly name="TELNO" value="<%=telno%>" /></td>
				
			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left">Ref No:</th>
				<td><INPUT type="TEXT" size="20" MAXLENGTH=20 name="JOB_NUM"
					value="<%=jobNum%>"></td>
				<th WIDTH="20%" ALIGN="left">Customer Name:</th>


				<TD><INPUT name="CUST_NAME" type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80  class="inactiveGry" readonly >
               <!--   <a href="#" onClick="javascript:popUpWin('customer_list_order.jsp?CUST_NAME='+form.CUST_NAME.value);"><img src="images/populate.gif" border="0"></a> -->
             <INPUT type="hidden" name="CUST_CODE" value="<%=custCode%>"> <INPUT
					type="hidden" name="LOGIN_USER" value="<%=sUserId%>"> <INPUT
					type="hidden" name="CUST_CODE1" value="<%=custCode%>"></TD>
			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left">Order Date:</th>
				<td><INPUT type="TEXT" size="20" MAXLENGTH="10" name="DELDATE"
					value="<%=deldate%>" /> <a
					href="javascript:show_calendar('form.DELDATE');"
					onmouseover="window.status='Date Picker';return true;"
					onmouseout="window.status='';return true;"> <img
					src="images\show-calendar.gif" width="24" height="22" border="0" />
				</a> <INPUT type="Hidden" size="20" MAXLENGTH=20 name="CONTACT_NUM"
					value="<%=contactNum%>"></td>
				<th WIDTH="20%" ALIGN="left">Email:</th>
				<td><INPUT type="TEXT" size="25" class="inactiveGry" readonly MAXLENGTH="20"  name="EMAIL" value="<%=email%>" /></td>


			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left">Time:</th>
				<!--  <td><INPUT type = "TEXT" size="20"  MAXLENGTH=10  name="DELDATE" value="<%=deldate%>">&nbsp;<a href="javascript:show_calendar('form.DELDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></td>-->
				<td><INPUT type="TEXT" size="20" MAXLENGTH="6"
					name="COLLECTION_TIME" value="<%=collectionTime%>" /></td>

				<th WIDTH="20%" ALIGN="left" height="30">Unit No / Building:</th>
				<td height="30"><INPUT type="TEXT" size="25"
					class="inactiveGry" readonly MAXLENGTH="20" name="ADD1"
					value="<%=add1%>" /> <INPUT type="TEXT" size="25" MAXLENGTH="20"
					class="inactiveGry" readonly name="ADD2" value="<%=add2%>" /></td>
			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left" height="30">Remarks</th>
				<td height="30"><INPUT type="TEXT" size="20" MAXLENGTH="20"
					name="REMARK1" value="<%=remark1%>" /></td>
				<th WIDTH="20%" ALIGN="left">Street:</th>
				<td><INPUT type="TEXT" size="25" class="inactiveGry"
					MAXLENGTH="20" name="ADD3" readonly value="<%=add3%>" /> </td>

			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left">&nbsp;Order Type</th>
				<td><INPUT type="TEXT" size="20" MAXLENGTH="20"
					name="ORDERTYPE" value="MOBILE ORDER" class="inactiveGry" readonly />
					<!--<a href="#" onClick="javascript:popUpWin('OrderType_list.jsp?ORDERTYPE='+form.ORDERTYPE.value+'&FORMTYPE='+form.formtype.value);"><img src="images/populate.gif" border="0"></a>--></td>
				<th WIDTH="20%" ALIGN="left">Country/ Postal Code:</th>
				<td><INPUT type="TEXT" size="25" class="inactiveGry"
					MAXLENGTH="20" name="COUNTRY" readonly value="<%=country%>" /> <INPUT
					type="TEXT" size="25" class="inactiveGry" MAXLENGTH="20" name="ZIP"
					readonly value="<%=zip%>" /></td>
			</tr>
                      <tr>
                       <th WIDTH="20%" ALIGN="left">  &nbsp;&nbsp;Currency ID</th>
                     <input type="hidden" name="CURRENCY_ID">
                    <input type="hidden" name="DESC">
                     <td height="30"><INPUT type = "TEXT" size="20"   MAXLENGTH=20 name="DISPLAY" value="<%=currencyid%>" class="inactiveGry" readonly >
                     </td>
                    <INPUT type="hidden" size="20" class="inactiveGry" MAXLENGTH=20 readonly name="PERSON_INCHARGE" value="<%=personIncharge%>">
                      <INPUT type="hidden" size="20" class="inactiveGry" MAXLENGTH="20" name="ADD4" readonly value="<%=add4%>" />
                      <INPUT readonly class="inactiveGry" type="hidden" size="20" MAXLENGTH="100" name="REMARK2" value="<%=remarks%>">

                     <th WIDTH="20%" ALIGN = "left"></th>
                     <td></td>
                   </tr>
                     <tr>
						<th WIDTH="20%" ALIGN="left">Delivery Date:</th>
						<td><INPUT type="TEXT" size="20" MAXLENGTH="10" name="DELIVERYDATE"
						value="<%=DELIVERYDATE%>" class="inactiveGry" readonly/> <a
						href="javascript:show_calendar('form.DELIVERYDATE');"
						onmouseover="window.status='Date Picker';return true;"
						onmouseout="window.status='';return true;"> <img
						src="images\show-calendar.gif" width="24" height="22" border="0" />
						</a>
						</td>
					</tr>
					<tr>
						<th WIDTH="20%" ALIGN="left">Delivery Time:</th>
						<td><INPUT type="TEXT" size="20" class="inactiveGry"
							MAXLENGTH="20" name="TIMESLOTS" value="<%=TIMESLOTS%>" class="inactiveGry" readonly/>
							<a href="#" onClick="javascript:popUpWin('TimeSlotEditList.jsp?TIMESLOTS='+form.TIMESLOTS.value);"><img src="images/populate.gif" border="0"></a>
						</td>
					
						    
						 <th WIDTH="20%" ALIGN="right">  &nbsp;&nbsp;</th>
	                     <td height="30">&nbsp;&nbsp; 
	                      <input type="button" value="Save" name="Submit" onclick="return onUpdate(document.form)" />&nbsp;&nbsp;
	                        <INPUT class="Submit" type="BUTTON" value="Clear" onClick="onClear();">&nbsp;&nbsp;
	                         <INPUT class="Submit" type="BUTTON" value="Delete" onClick="return onDelete(document.form);">&nbsp;&nbsp;
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
         <th width="8%"><font color="#ffffff">Order Line No </font> </th>
         <th width="11%"><font color="#ffffff">Order Type </font> </th>
         <th width="11%"><font color="#ffffff">Product ID </font></th>
         <th width="18%"><font color="#ffffff">Description </font></th>
          <th width="11%"><font color="#ffffff">Unit Price </font></th>
         <th width="11%"><font color="#ffffff">Order Qty </font></th>
         <th width="9%"><font color="#ffffff">Pick Qty </font></th>
         <th width="9%"><font color="#ffffff">Issue Qty </font></th>
         <th width="5%"><font color="#ffffff"><%=IDBConstants.UOM_LABEL%></font></th>
         <th width="5%"><font color="#ffffff">Status </font></th>
        </tr>
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#eeeeee">
      <%=fieldDesc%>
    </table>

        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="6">
        
        <div align="center"><center>
          <br>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                 <input type="Button" value="Cancel" onClick="window.location.href='../home'">
                  &nbsp;
                   <input type="Submit" value="Add Products" name="Submit">
                  &nbsp;
                 <!-- <input type="Submit" value="Print Mobile Order" name="Submit"/>
                   &nbsp;-->
                  <input type="Submit" value="Print Mobile Order With Price" name="Submit"/>
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

 logger.log(-1,"maintOutgoingOrder.jsp  ");
%>
<%@ include file="footer.jsp" %>
