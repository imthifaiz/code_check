<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp" %>

<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%><html>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Mobile Shopping Order Full Fillment</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;

function popUpWin(URL) {
    subWin = window.open(URL, 'OutBoundPicking', 
'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
  }

function sf()
{
document.form.DONO.focus();
}




function SetChecked(val)
{
dml=document.form;
len = dml.elements.length;
var i=0;

for( i=0; i<len; i++)
 {
	 dml.elements[i].checked=val;

 }
}

function popWin(vname){
    window.open('vendSumm.jsp?VENDNAME='+vname+'&P=Y');
}

function onNew(form)
{
    document.form.DONO.value   ="DUMMY"
    document.form.CUST_NAME.value       ="DUMMY"
    form.JOB_NUM.value         =""
    form.PERSON_INCHARGE.value =""
    form.CONTACT_NUM.value     =""
    form.ADDRESS.value         =""
    form.ADDRESS2.value        =""
    form.ADDRESS3.value        =""
    form.DELDATE.value         =""
    form.COLLECTION_TIME.value =""
    form.REMARK1.value         =""
    form.REMARK2.value         =""
    return true;
}


function onIssue(form){
 var ischeck = false;
 var Traveler ;
 var concatTraveler="";
 var j=0;
 var pono=document.form.DONO.value;
   var i = 0;
   var noofcheckbox=1;
   if(pono==null||pono=="")
	{
	alert("Please select Order No");	
	return false;
	}  

   if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
   {
  	   	alert("No Data's Found For Picking");
  	 	return false;
   }  
   var noofcheckbox = document.form.chkdDoNo.length;
	
    if(form.chkdDoNo.length == undefined)
    {
            
            if(form.chkdDoNo.checked)
            {
              document.form.TRAVELER.value=form.chkdDoNo.value+"=";
              return true;
            }
            
            else
            {
               alert("Please Select Product For Picking");
               return false;
            }
    
    }else
    {
           
             for (i = 0; i < noofcheckbox; i++ )
              {
               ischeck = document.form.chkdDoNo[i].checked;
                   if(ischeck)
                    {
                      j=j+1;
                      Traveler=document.form.chkdDoNo[i].value;
                     
                      concatTraveler=concatTraveler+Traveler+"=";
                    
                    }
   
               }
               
              
              if(j==0)
              {
                alert("Please Select Product For Picking");
                return false;
              }
              document.form.TRAVELER.value=concatTraveler;
              return true;
              //document.form.action ="/track/OrderIssuingServlet?";
              //document.form.submit();  
            
    }
    return true;
  }
  
function onViewToConfirm(form)
{
  var flag    = "false";
  
    var DONO   = document.form.DONO.value;
    var CUST_NAME = document.form.CUST_NAME.value;
   if(DONO == "" || DONO == null) {alert("Please Select Order No "); document.form.DONO.focus(); return false; }

  document.form.action="mobileShoppingWOInvcheck.jsp?DONO="+DONO+"&CUST_NAME="+CUST_NAME;
  document.form.submit();
 }
 
</script>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />

<%

db.setmLogger(mLogger);
vmb.setmLogger(mLogger);
phb.setmLogger(mLogger);
pdb.setmLogger(mLogger);



   session = request.getSession();
   StrUtils strUtils = new StrUtils();
	
   String plant=(String)session.getAttribute("PLANT");
   String pono     = su.fString(request.getParameter("DONO"));
   String action   = su.fString(request.getParameter("action")).trim();
   String sUserId = (String) session.getAttribute("LOGIN_USER");
   String RFLAG=    (String) session.getAttribute("RFLAG");
   String AFLAG=    (String) session.getAttribute("AFLAG");
   boolean confirm = false;
   DOUtil _DOUtil=new DOUtil();
   ItemMstDAO _ItemMstDAO=new ItemMstDAO();
   _DOUtil.setmLogger(mLogger);
   _ItemMstDAO.setmLogger(mLogger);
   String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
   String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
   String contactname="",telno="",hpno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",chkString ="";
   String sSaveEnb    = "disabled";
   String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
     
   if(action.equalsIgnoreCase("View")){
    
      Map m=(Map)request.getSession().getAttribute("podetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULT");
     
       if(m.size()>0){
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
             remark2=(String)m.get("remarks");
     }
      else 
      {
        fieldDesc="Details not found for oubound order:"+ pono;  
      }
    }
     else if(action.equalsIgnoreCase("result"))
     {
        fieldDesc=(String)request.getSession().getAttribute("RESULT");
      }
     else if(action.equalsIgnoreCase("resulterror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
     }
     else if(action.equalsIgnoreCase("catchrerror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
     }
      
%>
<%@ include file="body.jsp" %>
<FORM name="form" method="post" action="/track/OrderIssuingServlet">
  <br>
   <CENTER>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11">
          <FONT color="#ffffff">Mobile Shopping Order Full Fillment</FONT>&nbsp;
        </TH></table>
  <br>
  
 <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
   <font class="maingreen"> <%=fieldDesc%></font>
 </table>
 
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
                     <th WIDTH="20%" ALIGN = "left"> Order No : </th>
                     <td>
                       <P>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" name="DONO" value="<%=pono%>" onkeypress="if
((event.keyCode=='13') && ( document.form.DONO.value.length > 0)){loadOutboundOrderDetails();}"/>
                         <a href="#" onClick="javascript:popUpWin('do_list_for_singlepickissue.jsp?ORDERTYPE=MOBILE ORDER&DONO='+form.DONO.value);">
                           <img src="images/populate.gif" border="0"/>
                         </a>
                         <input type="button" value="View" name="actionView" onclick="viewOutboundOrders();"/>
                     
                       </P>
                    <th WIDTH="20%" ALIGN="left">Handphone No</th>
				<td><INPUT type="TEXT" size="20" class="inactiveGry" MAXLENGTH="20" readonly name="TELNO" value="<%=telno%>" /></td>
				
			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left">Ref No:</th>
				<td><INPUT type="TEXT" size="20" MAXLENGTH=20 name="JOB_NUM" value="<%=jobNum%>" class="inactiveGry" readonly></td>
				<th WIDTH="20%" ALIGN="left">Customer Name:</th>
                                <TD><INPUT name="CUST_NAME" type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80 class="inactiveGry" readonly>
                
                         <INPUT type="hidden" name="CUST_CODE" value="<%=custCode%>"> <INPUT
                        type="hidden" name="LOGIN_USER" value="<%=sUserId%>"> <INPUT
                        type="hidden" name="CUST_CODE1" value="<%=custCode%>"></TD>
			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left">Order Date:</th>
				<td><INPUT type="TEXT" size="20" MAXLENGTH="10" name="DELDATE" value="<%=deldate%>" class="inactiveGry" readonly /> 
                                <INPUT type="Hidden" size="20" MAXLENGTH=20 name="CONTACT_NUM"
					value="<%=contactNum%>"></td>
				<th WIDTH="20%" ALIGN="left">Email:</th>
				<td><INPUT type="TEXT" size="20" class="inactiveGry" readonly MAXLENGTH="20"  name="EMAIL" value="<%=email%>" /></td>


			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left">Time:</th>
				<!--  <td><INPUT type = "TEXT" size="20"  MAXLENGTH=10  name="DELDATE" value="<%=deldate%>">&nbsp;<a href="javascript:show_calendar('form.DELDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></td>-->
				<td><INPUT type="TEXT" size="20" MAXLENGTH="6"
					name="COLLECTION_TIME" value="<%=collectionTime%>" class="inactiveGry" readonly /></td>

				<th WIDTH="20%" ALIGN="left" height="30">Unit No / Building:</th>
				<td height="30"><INPUT type="TEXT" size="20"
					class="inactiveGry" readonly MAXLENGTH="20" name="ADD1"
					value="<%=add1%>" /> <INPUT type="TEXT" size="20" MAXLENGTH="20"
					class="inactiveGry" readonly name="ADD2" value="<%=add2%>" /></td>
			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left" height="30">Remarks</th>
				<td height="30"><INPUT type="TEXT" size="20" MAXLENGTH="20"
					name="REMARK1" value="<%=remark1%>" class="inactiveGry" readonly/></td>
				<th WIDTH="20%" ALIGN="left">Street:</th>
				<td><INPUT type="TEXT" size="20" class="inactiveGry"
					MAXLENGTH="20" name="ADD3" readonly value="<%=add3%>" /> </td>

			</tr>
			<tr>
				<th WIDTH="20%" ALIGN="left"></th>
				<td></td>
				<th WIDTH="20%" ALIGN="left">Country/ Postal Code:</th>
				<td><INPUT type="TEXT" size="20" class="inactiveGry"
					MAXLENGTH="20" name="COUNTRY" readonly value="<%=country%>" /> <INPUT
					type="TEXT" size="20" class="inactiveGry" MAXLENGTH="20" name="ZIP"
					readonly value="<%=zip%>" /></td>
			</tr>
                  </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
       <TABLE BORDER="1" CELLSPACING="0" WIDTH="100%" bgcolor="navy">
        <tr>
         <th width="8%"><font color="#ffffff">Select </font></th>
         <th width="10%"><font color="#ffffff">Order Line No </font></th>
         <th width="15%"><font color="#ffffff">Product ID </font></th>
         <th width="20%"><font color="#ffffff">Description </font></th>
         <th width="15%"><font color="#ffffff">Order Qty </font></th>
         <th width="14%"><font color="#ffffff">Pick Qty </font></th>
         <th width="8%"><font color="#ffffff">Issue Qty </font></th>
         <th width="5%"><font color="#ffffff"><%=IDBConstants.UOM_LABEL%></font></th>
         <th width="5%"><font color="#ffffff">Status </font></th>
        </tr>
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#eeeeee">
     <% 
       ArrayList al= _DOUtil.listDODETDetails(plant,pono);
     	if(al.size()==0)
     	{
  	   AFLAG="";
    	}
       if(al.size()>0)
       {
    	AFLAG="DATA";
       	for(int i=0 ; i<al.size();i++)
       	{
       
          Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          String dono = (String)m.get("dono");
          String dolnno = (String)m.get("dolnno");
          String custname= (String)m.get("custname");
          String item= (String)m.get("item");
          String loc= (String)m.get("loc");
          String batch= (String)m.get("batch");
          String qtyor= (String)m.get("qtyor");
          String qtyPick= (String)m.get("qtyPick");
          double qtypk = Double.parseDouble(qtyPick);
          
          qtypk = StrUtils.RoundDB(qtypk,IConstants.DECIMALPTS);
          qtyPick = String.valueOf(qtypk);
          String qtyis= (String)m.get("qtyis");
          String desc= _ItemMstDAO.getItemDesc(plant ,item);
          String uom= _ItemMstDAO.getItemUOM(plant ,item);
          chkString  
=dono+","+dolnno+","+item+","+su.replaceCharacters2Send(desc)+","+qtyor+","+qtyPick+","+qtyis+","+sUserId+","+loc+","+batch+","+strUtils.replaceCharacters2Send(strUtils.fString(custname));
        
      %>
        <TR bgcolor = "<%=bgcolor%>">
               <TD width="8%"  align="CENTER"><font color="black"><INPUT Type=radio  style="border:0;background=#dddddd" 
name="chkdDoNo" value="<%=chkString%>"></font></TD>
              <TD width="10%" align="center"><font color="black"><%=(String)m.get("dolnno")%></font></TD>
              <TD align="center" width="15%"><%=(String)m.get("item")%></TD>
              <TD align="center" width="20%"><%=(String)desc%></TD>
              <TD align="center" width="15%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
              <TD align="center" width="14%"><%=StrUtils.formatNum(qtyPick)%></TD>
              <TD align="center" width="8%"><%=StrUtils.formatNum((String)m.get("qtyis"))%></TD>
              <TD align="center" width="5%"><%=uom%></TD>
              <TD align="center" width="5%"><%=(String)m.get("lnstat")%></TD>
           </TR>
           
       <%}} else {%>
       
             <TR> <TD align="center" width="15%"> Data's Not Found For Issuing</TD></TR>
       <%}%>
    </table>

        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="6">
        <INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
        
        <div align="center"><center>
          <br>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  <input type="Button" value="Cancel" onClick="window.location.href='../home'">
                  <input type="button" value="Pick" name="actionSubmit" onClick="if(onIssue(document.form)){submitForm();}"/>
                   <input type="button" value="Issue" onClick="onViewToConfirm(document.form);" >
                  <INPUT type="hidden" name="TRAVELER" value="">
                  <INPUT     name="LOGIN_USER"  type ="hidden" value="<%=sUserId %>" size="1"   MAXLENGTH=80 ></TD>
              </tr>
              <p>
              <p>
            </table>
          </center>
        </div>
      </td>
    </tr>
  </table>
  <TR>
	 </TR>
</FORM>
</HTML>
	<script>
    function viewOutboundOrders(){
            document.form.action="/track/OrderIssuingServlet?action=MultipleView&ORDERTYPE=MOBILE ORDER";
            document.form.submit();
    }
    function submitForm(){
            document.form.action="/track/OrderIssuingServlet?action=Mobile_Shopping_skip_invCheck";
            document.form.submit();
    }
    function loadOutboundOrderDetails() {
            var outboundOrderNo = document.form.DONO.value;
            var urlStr = "/track/OutboundOrderHandlerServlet";
            $.ajax( {
                    type : "POST",
                    url  : urlStr,
                    data : {
                            ORDER_NO : outboundOrderNo,
                            PLANT : "<%=plant%>",
                            ACTION : "LOAD_OUTBOUND_ORDER_DETAILS"
                                            },
                                            dataType : "json",
                                            success : function(data) {
                                                    if (data.status == "100") {
                                                            var resultVal = data.result;
                                                            
                                                            document.form.CUST_NAME.value = resultVal.CUSTNAME;
                                                            document.form.JOB_NUM.value=resultVal.JOBNUM;
                                                            document.form.action = "/track/OrderIssuingServlet?action=View";
                                                            document.form.submit();
    
                                                    } else {
                                                            alert("Not a valid Order Number!");
                                                            document.form.DONO.value = "";
                                                            document.form.CUST_NAME.value = "";
                                                            document.form.DONO.focus();
                                                    }
                                            }
                                    });
            }
</script>
<%@ include file="footer.jsp" %>
