<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ include file="header.jsp"%>

<%@page import="com.track.constants.IDBConstants"%><script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript"
	src="js/calendar.js"></script>
<!-- Not in Use - Menus status 0 -->
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;

function popUpWin(URL) {
 subWin = window.open(URL, 'InboundOrderSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function popWin(vname){
    window.open('vendSumm.jsp?VENDNAME='+vname+'&P=Y');
}

function onReceive(form){
 var ischeck = false;
 var Traveler ;
 var concatTraveler="";
 var j=0;
var pono=document.form.PONO.value;
   var i = 0;
   var noofcheckbox=1;
   if(pono==null||pono=="")
	{
	alert("Please select Inbound Order");	
	return false;
	}    
    if(document.form.AFLAG.value==null||document.form.AFLAG.value=="" ||document.form.AFLAG.value!="DATA")
    {
	   	alert("No Data's Found For Receiving");
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
               alert("Please Select Product For Receive");
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
                alert("Please Select Product For Receive");
                return false;
              }
              document.form.TRAVELER.value=concatTraveler;
             return true;
            
    }
   
    
    return false;
   }
  
   

    
</SCRIPT>

<title>Goods Receipt by Inbound Order</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp"%>
<jsp:useBean id="gn" class="com.track.gates.Generator" />

<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />

<%
    POUtil _poUtil=new POUtil();
	_poUtil.setmLogger(mLogger);
    session = request.getSession();

    String action   = su.fString(request.getParameter("action")).trim();
    String pono     = su.fString(request.getParameter("PONO"));
    String plant = su.fString((String)session.getAttribute("PLANT"));
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    String RFLAG=    (String) session.getAttribute("RFLAG");
    String AFLAG=    (String) session.getAttribute("AFLAG");

  
    String vend = "",deldate="",jobNum = "",custName = "",personIncharge = "",contactNum = "",chkString ="";
    String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="",
    contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="";
    String sSaveEnb    = "disabled";
    String fieldDesc="<tr><td> Please enter any search criteria</td></tr>";
   
     if(action.equalsIgnoreCase("View")){
     // try{

   
      Map m=(Map)request.getSession().getAttribute("isummaryvalue");
      //fieldDesc=(String)request.getSession().getAttribute("ISUMMARYRESULT");
       fieldDesc=(String)request.getSession().getAttribute("RESULTINBRECEIVE");

      

       if(m.size()>0){
     
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
      
      
    }
  
    else if(action.equalsIgnoreCase("catcerror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
    
    request.getSession().setAttribute("RESULTINBRECEIVE","");
    request.getSession().setAttribute("podetVal","");
    request.getSession().setAttribute("CATCHERROR","");
      
  //}
 
%>
<FORM name="form" method="post" action="/track/OrderReceivingServlet?">

<br>
<table border="1" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Goods
		Receipt by Inbound Order</FONT></TH>
</table>
<br>
<CENTER>
<table border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<font class="maingreen"> <%=fieldDesc%></font>
</table>
</CENTER>
<table border="1" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<tr>
		<td width="100%">&nbsp; <font face="Times New Roman" size="2">
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
								<th WIDTH="20%" ALIGN="left">InBound Order :</th>
								<td><INPUT type="TEXT" size="20" MAXLENGTH=20 name="PONO"
									value="<%=pono%>"
									onkeypress="if((event.keyCode=='13') && ( document.form.PONO.value.length > 0)){loadInbundOrderDetails();}">
								<a href="#"
									onClick="javascript:popUpWin('po_list_po.jsp?PONO='+form.PONO.value);">
								<img src="images/populate.gif" border="0" /> </a> <input
									type="button" value="View" name="actionButton"
									onclick="viewInboundOrders();"></td>
								<th WIDTH="20%" ALIGN="left">
								<P>Supplier Name:</P>
								</th>


								<TD><INPUT name="CUST_NAME" type="TEXT"
									value="<%=su.forHTMLTag(custName)%>" size="30" MAXLENGTH=80> <INPUT
									type="hidden" name="CUST_CODE" value=""> <INPUT
									type="hidden" name="CUST_CODE1" value=""> <INPUT
									type="hidden" name="LOGIN_USER" value="<%=sUserId%>"></TD>
							</tr>
							<tr>
								<th WIDTH="20%" ALIGN="left">Ref No:</th>
								<td><INPUT type="TEXT" size="20" MAXLENGTH=20
									name="JOB_NUM" class="inactivegry" readonly value="<%=jobNum%>"></td>
								<th WIDTH="20%" ALIGN="left">Person Incharge:</th>
								<td><INPUT type="TEXT" size="20" MAXLENGTH=20
									class="inactivegry" readonly name="PERSON_INCHARGE"
									value="<%=personIncharge%>"></td>
							</tr>
							<tr>
								<th WIDTH="20%" ALIGN="left">Order Date:</th>
								<td><INPUT type="TEXT" size="20" MAXLENGTH="10"
									name="DELDATE" class="inactivegry" readonly
									value="<%=collectionDate%>" /></td>
								<th WIDTH="20%" ALIGN="left">TelNo/ Email:</th>
								<td><INPUT type="TEXT" size="20" class="inactivegry"
									MAXLENGTH="20" readonly name="TELNO" value="<%=telno%>" /> <INPUT
									type="TEXT" size="20" class="inactivegry" readonly
									MAXLENGTH="20" name="EMAIL" value="<%=email%>" /></td>


							</tr>
							<tr>
								<th WIDTH="20%" ALIGN="left">Time</th>

								<td><INPUT type="TEXT" size="20" class="inactivegry"
									readonly MAXLENGTH="20" name="COLLECTION_TIME"
									value="<%=collectionTime%>" /></td>
								<th WIDTH="20%" ALIGN="left" height="30">Unit No/ Building:
								</th>
								<td height="30"><INPUT type="TEXT" size="20"
									class="inactivegry" readonly MAXLENGTH="20" name="ADD1"
									value="<%=add1%>" /> <INPUT type="TEXT" size="20"
									MAXLENGTH="20" class="inactivegry" readonly name="ADD2"
									value="<%=add2%>" /></td>
							</tr>


							<tr>
								<th WIDTH="20%" ALIGN="left" height="30">Remarks</th>
								<td height="30"><INPUT type="TEXT" size="20" MAXLENGTH="20"
									name="REMARK1" class="inactivegry" readonly
									value="<%=remark1%>" /></td>
								<th WIDTH="20%" ALIGN="left">Street/ City:</th>
								<td><INPUT type="TEXT" size="20" class="inactivegry"
									MAXLENGTH="20" name="ADD3" readonly value="<%=add3%>" /> <INPUT
									type="TEXT" size="20" class="inactivegry" MAXLENGTH="20"
									name="ADD4" readonly value="<%=add4%>" /></td>

							</tr>

							<tr>
								<th WIDTH="20%" ALIGN="left">&nbsp;</th>
								<td>&nbsp;</td>
								<th WIDTH="20%" ALIGN="left">Country/ Postal Code:</th>
								<td><INPUT type="TEXT" size="20" class="inactivegry"
									MAXLENGTH="20" name="COUNTRY" readonly value="<%=country%>" />
								<INPUT type="TEXT" size="20" class="inactivegry" MAXLENGTH="20"
									name="ZIP" readonly value="<%=zip%>" /></td>
							</tr>
							<tr>
								<th WIDTH="20%" ALIGN="left">&nbsp;</th>
								<td>&nbsp;</td>
								<th WIDTH="20%" ALIGN="left">Supplier Remarks</th>
								<td><INPUT type="TEXT" size="20" class="inactivegry"
									readonly MAXLENGTH=20 name="REMARK2" value="<%=remarks%>">

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

						<th width="5%"><font color="#ffffff">Select</font> </th>
						<th width="5%"><font color="#ffffff">OrderLno</font> </th>
						<th width="15%"><font color="#ffffff">Product ID</font> </th>
						<th width="20%"><font color="#ffffff">Description</font> </th>
                      <th width="20%"><font color="#ffffff">Detail Description </th>
                      <th width="10%"><font color="#ffffff">Supplier </font></th>
						<th width="10%"><FONT color="#ffffff">Order Quantity</FONT>&nbsp;
						</th>
						<th width="10%"><FONT color="#ffffff">Received
						Quantity</FONT>&nbsp;</th>
						<th width="5%"><FONT color="#ffffff"><%=IDBConstants.UOM_LABEL%></FONT>&nbsp;</th>
						<th width="5%"><font color="#ffffff">Status </th>
					</tr>
				</TABLE>
				</center>
				<table width="100%" border="0" cellspacing="0" cellpadding="5"
					bgcolor="#eeeeee">


					<% 
       ArrayList al= _poUtil.listInboundSummaryPODET(pono,plant);
       
       if(al.size()==0)
       {
    	   AFLAG="";
       }
      
       if(al.size()>0)
       {
        ItemMstDAO _ItemMstDAO= new ItemMstDAO();
        InvMstDAO _InvMstDAO= new InvMstDAO();
        _ItemMstDAO.setmLogger(mLogger);
        _InvMstDAO.setmLogger(mLogger);
        AFLAG="DATA";
       for(int i=0 ; i<al.size();i++)
       {
          
          Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          String strpono = (String)m.get("pono");
          String polnno = (String)m.get("polnno");
          String sname= (String)m.get("sname");
          String item= (String)m.get("item");
          Map mitem= _ItemMstDAO.getItemDetailDescription(plant ,item);
          String uom= _ItemMstDAO.getItemUOM(plant ,item);
          String desc="",detaildesc="",supplier="";
            if(mitem.size()>0){
                 desc = StrUtils.fString((String) mitem.get("itemDesc"));
                 detaildesc = StrUtils.fString((String) mitem.get("DetailItemDesc"));
                 supplier = StrUtils.fString((String) mitem.get("Supplier"));
            }else{
                desc=" "; detaildesc=" ";supplier="";
            }
          MLogger.log(0, "Item Desc = " + desc);
          //getting item loc
     
          String qtyor= (String)m.get("qtyor");
          String qtyrc= (String)m.get("qtyrc");
          String ref=(String)m.get("ref");
          String lnstat= (String)m.get("lnstat");
          chkString  =pono+","+polnno+","+item+","+StrUtils.replaceCharacters2Send(desc)+","+custName+","+personIncharge+","+telno+","+email+","+add1+","+add2+","+add3+","+qtyor+","+qtyrc+","+ref;
         
      %>
					<TR bgcolor="<%=bgcolor%>">

						<TD width="5%" align="CENTER" ><font color="black"><INPUT
							Type=radio style="border: 0;" name="chkdDoNo"
							value="<%=chkString%>"></font></TD>
						<TD width="5%" align="left" ><font color="black"><%=(String)m.get("polnno")%></font></TD>

						<TD align="left" width="15%" ><%=(String)m.get("item")%></TD>
						<TD align="left" width="20%" ><%=(String)desc%></TD>
                                                <TD align="left" width="20%"><%=(String)detaildesc%></TD>
                                                <TD align="left" width="10%"><%=(String)supplier%></TD>
						<TD align="center" width="8%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
						<TD align="center" width="8%"><%=StrUtils.formatNum((String)m.get("qtyrc"))%></TD>
						<TD align="center" width="5%"><%=uom%></TD>
						<TD align="center" width="5%"><%=(String)m.get("lnstat")%></TD>
					</TR>
					<%}} else 
    	   {%>
					<TR>
						<TD align="center" width="10%">Data's Not Found</TD>
					</TR>
					<%}%>

				</table>
				<td><INPUT type="hidden" size="20" MAXLENGTH=20
					name="CONTACT_NUM" value="<%=contactNum%>"></td>
				<INPUT type="Hidden" name="ENCRYPT_FLAG" value="1"> <INPUT
					type="Hidden" name="RFLAG" value="1"> <INPUT type="Hidden"
					name="AFLAG" value="<%=AFLAG%>">
                                        <INPUT type="hidden" size="20" MAXLENGTH=20
					name="RECFLAG" value="ByOrder">
				<div align="center">
				<center><br>
				<table border="0" width="100%" cellspacing="0" cellpadding="0">
					<tr>
						<td align="center"><input type="Button" value="Cancel"
							onClick="window.location.href='../home'"> <input
							type="button" value="Receive" name="actionButton"
							onClick="if(onReceive(document.form)) {submitForm();}" /> <INPUT
							type="hidden" name="TRAVELER" value=""> &nbsp;</td>
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

		</BODY>
		</HTML>
		<script>
function viewInboundOrders(){
			document.form.action="/track/OrderReceivingServlet?action=View";
			//document.form.action.value ="View";
            document.form.submit();
		}
function submitForm(){
	document.form.action ="/track/OrderReceivingServlet?action=Receive";
    document.form.submit();  
}
function loadInbundOrderDetails() {
	var inboundOrderNo = document.form.PONO.value;
	var urlStr = "/track/InboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : inboundOrderNo,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_INBOUND_ORDER_DETAILS"
			},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.form.JOB_NUM.value = resultVal.JOBNUM;
							document.form.CUST_NAME.value = resultVal.CUSTNAME;
							document.form.action = "/track/OrderReceivingServlet?action=View";
							document.form.submit();

						} else {
							alert("Not a valid Order Number!");
							document.form.PONO.value = "";
							document.form.JOB_NUM.value = "";
							document.form.CUST_NAME.value = "";
							document.form.PONO.focus();
						}
					}
				});
	}
</script> <%@ include file="footer.jsp"%>