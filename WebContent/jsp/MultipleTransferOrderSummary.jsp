<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp" %>

<%@page import="com.track.constants.IDBConstants"%><html>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Multiple Pick/Issue By Transfer Order </title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'TransferOrderSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}

	function sf()
	{
		document.form.TONO.focus();
	}
	function validatePO(form)
	{
	if (form.TONO.value.length < 1)
  	{
    	alert("Please Enter TO Number !");
    	form.TONO.focus();
    	return false;
 	 }
 
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

	function onDelete(form)
	{
   
	if (form.DONO.value.length < 1)
    {
    	alert("Please Enter TO Number !");
    	form.DONO.focus();
    	return false;
    }
    else{
     	var mes=confirm("Do you want to delete the Transfer order !");
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
    	if (form.TONO.value.length < 1)
    	{
    		alert("Please Enter TO Number !");
    		form.DONO.focus();
    		return false;
    	}
    	else
        {
     		var mes=confirm("Do you want to update the  Transfer order !");
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
    	document.form.TONO.value   ="DUMMY";
    	document.form.CUST_NAME.value       ="DUMMY";
    	form.JOB_NUM.value         ="";
    	form.PERSON_INCHARGE.value ="";
    	form.CONTACT_NUM.value     ="";
    	form.ADDRESS.value         ="";
    	form.ADDRESS2.value        ="";
    	form.ADDRESS3.value        ="";
    	form.DELDATE.value         ="";
    	form.COLLECTION_TIME.value ="";
    	form.REMARK1.value         ="";
    	form.REMARK2.value         ="";
    	return true;
	}
 
	function onIssue(form){
 		var ischeck = false;
 		var Traveler ;
 		var concatTraveler="";
 		var j=0;
		var pono=document.form.TONO.value;
  		var i = 0;
   		var noofcheckbox=1;
   		if(pono==null||pono=="")
		{
			alert("Please select Transfer Order");	
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
        	  document.form.Submit.value="MultiplePick/Issue";
              document.form.TRAVELER.value=concatTraveler;
              document.form.action ="/track/TransferOrderServlet?";
              document.form.submit();  
            
   		 }
    return false;
  }
</script>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.TOHDR" />
<jsp:useBean id="pdb" class="com.track.tables.TODET" />

<jsp:useBean id="du" class="com.track.util.DateUtils" />

<%
   session = request.getSession();
   String plant=(String)session.getAttribute("PLANT");
   String tono     = su.fString(request.getParameter("TONO"));
   String action   = su.fString(request.getParameter("action")).trim();
   String sUserId = (String) session.getAttribute("LOGIN_USER");
   String AFLAG=    (String) session.getAttribute("AFLAG");
   session.setAttribute("RFLAG","5");
   boolean confirm = false;
    
   TOUtil _TOUtil=new TOUtil();
   ToHdrDAO _ToHdrDAO =new ToHdrDAO(); 
   ItemMstDAO _ItemMstDAO=new ItemMstDAO();
 
   _TOUtil.setmLogger(mLogger);
   _ToHdrDAO.setmLogger(mLogger);
   _ItemMstDAO.setmLogger(mLogger);
   
   String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
   String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
   String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",chkString ="";
   String fromWareHouse="",toWareHouse="";
   String sSaveEnb    = "disabled";
   String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";

     
   if(action.equalsIgnoreCase("MultipleView")){
      Map m=(Map)request.getSession().getAttribute("todetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULTPICKING");
      if(request.getSession().getAttribute("RESULTPICKING")==null )
      {
    	  fieldDesc="";
      }
      request.getSession().setAttribute("RESULTPICKING","");
   
      if(m.size()>0){
         jobNum=(String)m.get("jobNum");
         fromWareHouse=(String)m.get("fromwarehouse");
    	 toWareHouse=(String)m.get("towarehouse");
         custName=(String)m.get("custName");
         custCode=(String)m.get("custCode");

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
      }
      else 
      {
        fieldDesc="Details not found for transfer order:"+ tono;  
      }
    }
     else if(action.equalsIgnoreCase("result"))
     {
        fieldDesc=(String)request.getSession().getAttribute("RESULT");
        fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
      }
     else if(action.equalsIgnoreCase("resulterror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
     }
     else if(action.equalsIgnoreCase("catchrerror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
     }
   
%>
<%@ include file="body.jsp" %>
<FORM name="form" method="post" action="/track/TransferOrderServlet?" onsubmit="return validatePO();">
  <br>
   <CENTER>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11">
          <FONT color="#ffffff">Multiple Pick/Issue By Transfer Order</FONT>&nbsp;
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
                     <th WIDTH="20%" ALIGN = "left"> Transfer Order Number : </th>
                     <td>
                       <P>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" name="TONO" value="<%=tono%>" onkeypress="if((event.keyCode=='13') && ( document.form.TONO.value.length > 0)){loadTransferOrderDetails();}"/>
                         <a href="#" onClick="javascript:popUpWin('to_list_do.jsp?TONO='+form.TONO.value);">
                           <img src="images/populate.gif" border="0"/>
                         </a>
                         <input type="button" value="View" name="Submit" onclick="viewTransferOrders();"/>
                       </P>
                      </td>
                     <th WIDTH="20%" ALIGN = "left">Assignee Name:</th>
                   <TD>
                   <INPUT name="CUST_NAME"   class="inactivegry" MAXLENGTH="20" readonly type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
                   <INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
                     <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
                   </TD>
                   </tr>
                    <tr>
                     <th WIDTH="20%" ALIGN = "left">From Loc</th>
                     <td><INPUT type = "TEXT" size="20"   class="inactivegry" MAXLENGTH="20" readonly  MAXLENGTH=20 name="FROM_WAREHOUSE" value="<%=fromWareHouse%>"></td>
                     <th WIDTH="20%" ALIGN = "left">Person Incharge:</th>
                     <td><INPUT type = "TEXT" size="20"   class="inactivegry" MAXLENGTH=20 readonly name="PERSON_INCHARGE" value="<%=personIncharge%>"></td>
                   </tr>
                    <tr>
                     <th WIDTH="20%" ALIGN = "left">To Loc</th>
                     <td>
                       <INPUT type="TEXT" size="20" class="inactivegry" MAXLENGTH="20" readonly name="TO_WAREHOUSE" value="<%=toWareHouse%>"/>
                    
                     <INPUT type = "Hidden" size="20"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>"></td>
                      <th WIDTH="20%" ALIGN = "left">TelNo/ Email:</th>
                     <td>
                       <INPUT type="TEXT" size="20"  class="inactivegry"  MAXLENGTH="20" readonly name="TELNO" value="<%=telno%>"/>
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
                      <th WIDTH="20%" ALIGN="left">Remarks</th>
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
                     <th WIDTH="20%" ALIGN = "left">Assignee Remarks </th>
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
         <th width="8%"><font color="#ffffff">Select </font></th>
         <th width="10%"><font color="#ffffff">Order Line No </font></th>
         <th width="15%"><font color="#ffffff">Product ID </font></th>
         <th width="20%"><font color="#ffffff">Description </font></th>
         <th width="15%"><font color="#ffffff">Order Qty </font></th>
         <th width="14%"><font color="#ffffff">Picked Qty </font></th>
         <th width="10%"><font color="#ffffff">Received Qty </font></th>
         <th width="5%"><font color="#ffffff"><%=IDBConstants.UOM_LABEL%> </font></th>
         <th width="5%"><font color="#ffffff">Status </font></th>
        </tr>
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#eeeeee">
     <% 
      	String fromwarehouse=""; 
      	String towarehouse="";
      	ArrayList al= _TOUtil.listTODETForPicking(plant,tono);
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
          tono = (String)m.get("tono");
          String tolnno = (String)m.get("tolnno");
          String custname= (String)m.get("custname");
          String item= (String)m.get("item");
          String loc= (String)m.get("loc");
          String batch= (String)m.get("batch");
          String qtyor= (String)m.get("qtyor");
          String qtyPick= (String)m.get("qtyPick");
          String qtyrc= (String)m.get("qtyrc");
          String desc= _ItemMstDAO.getItemDesc(plant ,item);
          String uom= _ItemMstDAO.getItemUOM(plant ,item);
          ArrayList alLoc= _ToHdrDAO.getTransferOrderLocDetailsByWms(plant,tono);
          if(alLoc.size()>0)
          	{
          		for(int j=0 ;j<alLoc.size();j++)
          		{
          			 MLogger.log(0, "******************get Transfer Order TOHDR Loc Detail : " + i);
          		 	 Map k=(Map)alLoc.get(j);
          		     fromwarehouse = (String)k.get("fromwarehouse");
          		     towarehouse = (String)k.get("towarehouse");
                	  
          		}
           }
         chkString  =tono+","+tolnno+","+item+","+StrUtils.replaceCharacters2Send(desc)+","+qtyor+","+qtyPick+","+qtyrc+","+sUserId+","+loc+","+batch+","+custname+","+fromwarehouse+","+towarehouse;
       %>
        <TR bgcolor = "<%=bgcolor%>">
              <TD width="8%"  align="CENTER"><font color="black"><INPUT Type=radio  style="border:0;background=#dddddd" name="chkdDoNo" value="<%=chkString%>"></font></TD>
               <!--  <TD width="10%" align="center"><font color="black"><a  href="ModifyOutgoingOrderDetail.jsp?TONO=<%=(String)m.get("tono")%>&TOLNNO=<%=(String)m.get("tolnno")%>&CUSTNAME=<%=(String)m.get("custname")%>&ITEM=<%=(String)m.get("item")%>&ITEMDESC=<%=(String)desc%>&ORDERQTY=<%=(String)m.get("qtyor")%>&PICKEDQTY=<%=(String)m.get("qtyPick")%>&ISSUEDQTY=<%=(String)m.get("qtyis")%>")%><%=(String)m.get("tolnno")%></a></font></TD>-->
               <TD width="10%" align="center"><%=(String)m.get("tolnno")%></TD>
              <TD align="center" width="15%"><%=(String)m.get("item")%></TD>
              <TD align="center" width="20%"><%=(String)desc%></TD>
              <TD align="center" width="15%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
              <TD align="center" width="14%"><%=StrUtils.formatNum((String)m.get("qtyPick"))%></TD>
              <TD align="center" width="10%"><%=StrUtils.formatNum((String)m.get("qtyrc"))%></TD>
              <TD align="center" width="5%"><%=uom%></TD>
              <TD align="center" width="5%"><%=(String)m.get("pickstatus")%></TD>
           </TR>
           
       <%}} else {%>
       
             <TR> <TD align="center" width="15%"> Data's Not Found For Order</TD></TR>
       <%}%>
    </table>
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="5">
        <INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
        <div align="center"><center>
          <br>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  <input type="Button" value="Cancel" onClick="window.location.href='../home'">
                  <input type="button" value="Pick/Issue" name="Submit" onClick="if(onIssue(document.form)){submitForm();}"/>
                  <INPUT type="hidden" name="TRAVELER" value="">
                  <INPUT type="hidden" name="Submit" value="MultiplePick/Issue">
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
function viewTransferOrders(){
	
			document.form.action="/track/TransferOrderServlet?Submit=MultipleView";
			//document.form.action.value ="View";
            document.form.submit();
		}
function submitForm(){

	document.form.action="/track/TransferOrderServlet?Submit=MultiplePick/Issue";
	
    document.form.submit();
}
function loadTransferOrderDetails() {
	var transferOrderNo = document.form.TONO.value;
	var urlStr = "/track/TransferOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : transferOrderNo,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_TRANSER_ORDER_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							
							document.form.CUST_NAME.value = resultVal.CUSTNAME;
							document.form.action = "/track/TransferOrderServlet?Submit=MultipleView";
							document.form.submit();

						} else {
							alert("Not a valid Order Number!");
							document.form.TONO.value = "";
							document.form.CUST_NAME.value = "";
							document.form.TONO.focus();
						}
					}
				});
	}
</script>
<%@ include file="footer.jsp" %>
