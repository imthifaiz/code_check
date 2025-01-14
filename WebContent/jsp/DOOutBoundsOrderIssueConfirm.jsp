<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp" %>

<%@page import="com.track.constants.IDBConstants"%><html>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Goods Issue By Outbound Order </title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'OutBoundsOrderIssue', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}

function onIssue(form){
 	var ischeck = false;
 	var Traveler ;
 	var concatTraveler="";
 	var j=0;

  	var i = 0;
    var noofcheckbox=1;
    if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
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
               alert("Please Select Product For Issue");
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
                alert("Please Select Product For Issue");
                return false;
              }
              document.form.TRAVELER.value=concatTraveler;
              return true;
           
    }
  }
  
function checkAll(isChk)
{
	 var k=0;
	 
	 if(form.chkdDoNo.length == undefined)
	 {
		 if(form.chkdDoNo.checked)
         {
		
          return false;
         }
	 }
	 
    if (document.form.chkdDoNo)
    {
        if (document.form.chkdDoNo.disabled == false)
        	  alert('come in 1');
        for (k = 0; k < document.form.chkdDoNo.length; k++)
        {
        	 //  alert('come in 2')
                if (document.form.chkdDoNo[k].disabled == false)
            	document.form.chkdDoNo[k].checked = isChk;
        }
    }
}



function checkAlltb(field)
{
	for (i = 0; i < field.length; i++)
		field[i].checked = true ;
}




</script>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%

db.setmLogger(mLogger);

   String plant=(String)session.getAttribute("PLANT");
   String DONO     = su.fString(request.getParameter("DONO"));
   String CUST_NAME     = su.fString(request.getParameter("CUST_NAME"));
   String action   = su.fString(request.getParameter("action")).trim();
   String result   = su.fString(request.getParameter("result")).trim();
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
   String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",chkString ="";
   String sSaveEnb    = "disabled";
   String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
   
   ArrayList arr = new ArrayList();
   Hashtable htCond = new Hashtable();
   Map m1 = new HashMap();
   
   if (DONO.length() > 0) {

		htCond.put("PLANT", plant);
		htCond.put("DONO", DONO);
   }
     DOUtil _doUtil = new DOUtil();
	if (DONO.length() > 0) {

		htCond.put("PLANT", plant);
		htCond.put("DONO",DONO);

		String query = "dono,custName,custCode,jobNum,custName,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
		arr = _doUtil.getOutGoingDoHdrDetails(query, htCond);
		if (arr.size() > 0) {
			m1 = (Map) arr.get(0);

		} else {

			fieldDesc = "<tr><td>No Records Available</td></tr>";

		}

	}
	request.getSession().setAttribute("podetVal", m1);
   
   if(action.equalsIgnoreCase("View")){
      Map m=(Map)request.getSession().getAttribute("podetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULT");
      
      if(m.size()>0){
       jobNum=(String)m.get("jobNum");
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
        fieldDesc="Details not found for Order:"+ DONO;  
      }
    }
     
     if(result.equalsIgnoreCase("catchrerror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
        fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
     }
 %>
<%@ include file="body.jsp" %>
<FORM name="form" method="post" action="/track/OrderIssuingServlet?" >
  <br>
   <CENTER>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Goods Issue By Outbound Order </font></table>
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
                     <th WIDTH="20%" ALIGN = "left"> OutBound Order : </th>
                     <td>
                       <P>
                         <INPUT type="TEXT" size="20" MAXLENGTH="20" readonly name="DONO" value="<%=DONO%>" onkeypress="if((event.keyCode=='13') && ( document.form.DONO.value.length > 0)){loadOutboundOrderDetails();}"/>
                         
                        
                      </td>
                     <th WIDTH="20%" ALIGN = "left">Customer Name:</th>
                   <TD>
                   <INPUT name="CUST_NAME"   class="inactivegry" MAXLENGTH="20" readonly type = "TEXT" value="<%=CUST_NAME%>" size="30"  MAXLENGTH=80>
                   <INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
                   <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
                    <input type="hidden" name="PLANT" value="<%=plant%>">
                   </TD>
                   </tr>
                   
                  </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
       <TABLE BORDER="1" CELLSPACING="0" WIDTH="100%" bgcolor="navy">
        <tr>
        <!--  <th width="14%"> 
          <INPUT Type=Checkbox   style="border:0;"  checked name="chkdDoNo"  onclick="return checkAll(this.checked);">
         &nbsp;<font color="#ffffff"> Select/Unselect All</font></th>-->
         <th width="8%"><font color="#ffffff">Chk </font></th>
         <th width="8%"><font color="#ffffff">Order Line No </font></th>
         <th width="15%"><font color="#ffffff">Product ID </font></th>
         <th width="20%"><font color="#ffffff">Description </font></th>
         <th width="11%"><font color="#ffffff">Order Qty </font></th>
         <th width="9%"><font color="#ffffff">Pick Qty </font></th>
         <th width="7%"><font color="#ffffff">Issue Qty </font></th>
         <th width="5%"><font color="#ffffff"><%=IDBConstants.UOM_LABEL%> </font></th>
         <th width="5%"><font color="#ffffff">Status </font></th>
        </tr>
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#eeeeee">
     <% 
      ArrayList al= _DOUtil.listOutGoingIssueDODET(plant,DONO);
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
          String qtyis= (String)m.get("qtyis");
          String desc= _ItemMstDAO.getItemDesc(plant ,item);
          String uom = _ItemMstDAO.getItemUOM(plant ,item);
          chkString  =dono+","+dolnno+","+item+","+su.replaceCharacters2Send(desc)+","+qtyor+","+qtyPick+","+qtyis+","+sUserId+","+loc+","+batch;
       %>
       
         <TR bgcolor = "<%=bgcolor%>">
              <TD width="8%"  align="CENTER"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="chkdDoNo" checked value="<%=chkString%>"></font></TD>
              <!--<TD width="10%" align="center"><font color="black"><a  href="ModifyOutgoingOrderDetail.jsp?DONO=<%=(String)m.get("dono")%>&DOLNNO=<%=(String)m.get("dolnno")%>&CUSTNAME=<%=(String)m.get("custname")%>&ITEM=<%=(String)m.get("item")%>&ITEMDESC=<%=(String)desc%>&ORDERQTY=<%=(String)m.get("qtyor")%>&PICKEDQTY=<%=(String)m.get("qtyPick")%>&ISSUEDQTY=<%=(String)m.get("qtyis")%>")%><%=(String)m.get("dolnno")%></a></font></TD>-->
              <TD align="center" width="8%"><%=(String)m.get("dolnno")%></TD>
              <TD align="center" width="15%"><%=(String)m.get("item")%></TD>
              <TD align="center" width="20%"><%=(String)desc%></TD>
              <TD align="center" width="11%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
              <TD align="center" width="9%"><%=StrUtils.formatNum((String)m.get("qtyPick"))%></TD>
              <TD align="center" width="7%"><%=StrUtils.formatNum((String)m.get("qtyis"))%></TD>
              <TD align="center" width="5%"><%=uom%></TD>
              <TD align="center" width="5%"><%=(String)m.get("lnstat")%></TD>
           </TR>
           
       <%}} else {%>
       
             <TR> <TD align="center" width="15%"> Data's Not Found For Issuing</TD></TR>
       <%}%>
    </table>
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="5">
        <INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
        
        <div align="center"><center>
          <br>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr align="center">
                <td >
                  <input type="Button" value="Cancel" onClick="window.location.href='DOSummaryForSingleStepPickIssue.jsp?action=&DONO='+form.DONO.value+'&PLANT='+form.PLANT.value "/>&nbsp; 
                  <input type="button" value="Confirm Issue" name="actionSubmit" onClick="if(onIssue(document.form)) {submitForm();}"/>
                 </td>
              </tr>
              <p>
              <p>
               <INPUT type="hidden" name="TRAVELER" value="">
                  <INPUT     name="LOGIN_USER"  type ="hidden" value="<%=sUserId %>" size="1"   MAXLENGTH=80 ></TD>
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

function submitForm(){
	document.form.action="/track/OrderIssuingServlet?action=Confirm Issue";
    document.form.submit();
}
function loadOutboundOrderDetails() {
	var outboundOrderNo = document.form.DONO.value;
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
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
