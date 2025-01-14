<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.constants.IDBConstants"%>
<%@ include file="header.jsp"%>


<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%
String fieldDesc = "Please enter any search criteria";
session = request.getSession();
String plant = (String) session.getAttribute("PLANT");
String tono = su.fString(request.getParameter("TONO"));
String action = su.fString(request.getParameter("action")).trim();
String sUserId = (String) session.getAttribute("LOGIN_USER");
session.setAttribute("RFLAG", "7");
String isData =    (String) session.getAttribute("AFLAG");
Map checkedTOS = (Map) request.getSession().getAttribute("checkedTOS");
boolean confirm = false;
TOUtil _TOUtil = new TOUtil();
ToHdrDAO _ToHdrDAO = new ToHdrDAO();
ItemMstDAO _ItemMstDAO = new ItemMstDAO();
ToDetDAO _ToDetDAO = new ToDetDAO();

_TOUtil.setmLogger(mLogger);
_ToHdrDAO.setmLogger(mLogger);
_ItemMstDAO.setmLogger(mLogger);

String custName = "", custCode = "", personIncharge = "",item_loc= "";
String collectionDate = "", collectionTime = "";
String contactname = "", remark = "";
String fromWareHouse = "", toWareHouse = "";
String fullReceive = "",allChecked = "", REMARKS  = "",TRANSACTIONDATE="";
TRANSACTIONDATE = su.fString(request.getParameter("TRANSACTIONDATE"));
DateUtils _dateUtils = new DateUtils();
String curDate =_dateUtils.getDate();
if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;

if (action.equalsIgnoreCase("MultipleView")) {
	Map<String,String> m = (Map<String,String>) (request.getSession().getAttribute("todetVal"));
 	
 	if(request.getSession().getAttribute("RESULTRECIVE")!=null)
    {
 		fieldDesc = (String) request.getSession().getAttribute("RESULTRECIVE");  	 
    }
 	else 
 		 fieldDesc="";
 	
 	request.getSession().setAttribute("RESULTRECIVE","");

		if (m.size() > 0) {			
			fromWareHouse = (String) m.get("fromwarehouse");
			toWareHouse = (String) m.get("towarehouse");
			custName = (String) m.get("custName");
			personIncharge = (String) m.get("contactname");
			collectionDate = (String) m.get("collectionDate");
			collectionTime = (String) m.get("collectionTime");
			remark = (String) m.get("remark1");

		} else {
			fieldDesc = "Details not found for transfer order:" + tono;
		}
	}
else if(action.equalsIgnoreCase("CATCHERROR"))
{

 fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
 fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
 fromWareHouse = su.fString(request.getParameter("FROMLOC"));
 toWareHouse = su.fString(request.getParameter("TOLOC"));
 collectionDate=su.fString(request.getParameter("COLLECTION_DATE"));
 collectionTime=su.fString(request.getParameter("COLLECTION_TIME"));
 remark=su.fString(request.getParameter("REMARK"));
 item_loc = su.fString(request.getParameter("LOC"));
 custName=su.fString(request.getParameter("CUST_NAME"));
 personIncharge = su.fString(request.getParameter("PERSON_INCHARGE"));
 REMARKS=su.fString(request.getParameter("remark"));
 allChecked = su.fString(request.getParameter("allChecked"));
 fullReceive = su.fString(request.getParameter("fullReceive"));

}
%>
<html>
<head>
<title>Goods Receipt(Bulk) By Transfer Order</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript"src="js/calendar.js"></script>
	<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript">
	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'TransferOrderReceiving', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
	function viewTransferOrders(){
		document.form.action="/track/TransferOrderServlet?Submit=MultipleView";
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

	function submitForm(){
		document.form.action="/track/TransferOrderServlet?Submit=Bulk_ReceiveConfirm";
	    document.form.submit();
	}
	function validateLocation(locId, index) {
		var isValid;
		if(locId=="" || locId.length==0 ) {
			alert("Enter Location!");
			document.getElementById("LOC_"+index).focus();
			return false;
		}else{
			var urlStr = "/track/InboundOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : false,
				data : {
					LOC : locId,
	                USERID : "<%=sUserId%>",
					PLANT : "<%=plant%>",
					ACTION : "VALIDATE_LOCATION"
					},
					dataType : "json",
					success : function(data) {
						if (data.status != "100") {
	                               
							alert("Not a valid Location");
							document.getElementById("LOC_"+index).focus();
							document.getElementById("LOC_"+index).value="";
							isValid = false;	
							
							
						} 
						else 
							isValid =  true;
					}
				});
			 return isValid;
			}
		}

	
	function fullReceiving(isChk) {
		var len = document.form.chkdToNo.length;
		var orderLNo;
		if (len == undefined)
			len = 1;
		if (document.form.chkdToNo) {
			for ( var i = 0; i < len; i++) {
				if (len == 1 && document.form.chkdToNo.checked) {
					orderLNo = document.form.chkdToNo.value.split(":")[0];
					batch = document.form.chkdToNo.value.split(":")[1];
					setReceivingQty(orderLNo, batch, i);
				} else if (len == 1 && !document.form.chkdToNo.checked) {
					return;
				} else {
					if (document.form.chkdToNo[i].checked) {
						orderLNo = document.form.chkdToNo[i].value.split(":")[0];
						batch = document.form.chkdToNo[i].value.split(":")[1];
						setReceivingQty(orderLNo, batch, i);
					}
				}

			}
		}
	}
	
	function checkAll(isChk) {
		var len = document.form.chkdToNo.length;
		var orderLNo;
		if (len == undefined)
			len = 1;
		if (document.form.chkdToNo) {
			for ( var i = 0; i < len; i++) {
				if (len == 1) {
					document.form.chkdToNo.checked = isChk;
					orderLNo = document.form.chkdToNo.value.split(":")[0];
					batch = document.form.chkdToNo.value.split(":")[1];
				} else {
					document.form.chkdToNo[i].checked = isChk;
					orderLNo = document.form.chkdToNo[i].value.split(":")[0];
					batch = document.form.chkdToNo[i].value.split(":")[1];
				}
				setReceivingQty(orderLNo, batch, i);

			}
		}
	}

	function setReceivingQty(orderLNo, batch, i) {
		var len = document.form.chkdToNo.length;
		if (len == undefined)
			len = 1;
		if (len == 1 && document.form.chkdToNo.checked) {
			if (document.form.fullReceive.checked) {
				setQty(orderLNo, batch);
			}

		}

		else if (len != 1 && document.form.chkdToNo[i].checked) {
			if (document.form.fullReceive.checked) {
				setQty(orderLNo, batch);
			} else {
				document.getElementById("receivingQty_" + orderLNo + "_"
						+ batch).value = 0;
			}
		} else {
			document.getElementById("receivingQty_" + orderLNo + "_" + batch).value = 0;
		}

	}

	function setQty(orderLNo, batch) {
		var QtyPicked = document.getElementById("Qtypicked_" + orderLNo + "_"
				+ batch).value;
		QtyPicked = removeCommas(QtyPicked);
		var QtyReceived = document.getElementById("QtyReceived_" + orderLNo
				+ "_" + batch).value;
		QtyReceived = removeCommas(QtyReceived);
		var QtyReceiving = QtyPicked - QtyReceived;
		QtyReceiving = parseFloat(QtyReceiving).toFixed(3);
		document.getElementById("receivingQty_" + orderLNo + "_" + batch).value = QtyReceiving;
	}

	function onReceive(form) {
		var tono = document.form.TONO.value;

		if (tono == null || tono == "") {
			alert("Please select Transfer Order");
			return false;
		}
		if (document.form.isData.value == null
				|| document.form.isData.value == ""
				|| document.form.isData.value != "DATA") {
			alert("No Data's Found For Receiving");
			return false;
		}
		var locId = document.form.LOC_0.value;
		if (!validateLocation(locId, 0)) {
			return false;

		}
		var checkFound = false;
		var orderLNo;
		var len = document.form.chkdToNo.length;
		if (len == undefined)
			len = 1;
		for ( var i = 0; i < len; i++) {
			if (len == 1 && (!document.form.chkdToNo.checked)) {
				checkFound = false;
			}

			else if (len == 1 && document.form.chkdToNo.checked) {
				checkFound = true;
				orderLNo = document.form.chkdToNo.value.split(":")[0];
				batch = document.form.chkdToNo.value.split(":")[1];
				if (!verifyRecvQty(orderLNo, batch))
					return false;
			}

			else {
				if (document.form.chkdToNo[i].checked) {
					checkFound = true;
					orderLNo = document.form.chkdToNo[i].value.split(":")[0];
					batch = document.form.chkdToNo[i].value.split(":")[1];
					if (!verifyRecvQty(orderLNo, batch))
						return false;
				}
			}

		}
		if (checkFound != true) {
			alert("Please check at least one checkbox.");
			return false;
		}
		document.form.action = "/track/TransferOrderServlet?Submit=Bulk_ReceiveConfirm";
		document.form.submit();
	}

	function verifyRecvQty(orderLNo, batch) {

		var recvQty = document.getElementById("receivingQty_" + orderLNo + "_"
				+ batch).value;
		if (recvQty == "" || recvQty.length == 0 || recvQty <= 0) {
			alert("Enter a valid Quantity!");
			document.getElementById("receivingQty_" + orderLNo + "_" + batch)
					.focus();
			document.getElementById("receivingQty_" + orderLNo + "_" + batch)
					.select();
			return false;
		}
		if(!isNumericInput(recvQty)){
			alert("Entered Quantity is not a valid number!");
			document.getElementById("receivingQty_"+orderLNo+"_"+batch).focus();
			document.getElementById("receivingQty_"+orderLNo+"_"+batch).select();
		        return false;
			}
		recvQty = parseFloat(recvQty).toFixed(3);
		var recvdQty = document.getElementById("QtyReceived_" + orderLNo + "_"
				+ batch).value;
		recvdQty = removeCommas(recvdQty);
		recvdQty = parseFloat(recvdQty).toFixed(3);
		var Qtypicked = document.getElementById("Qtypicked_" + orderLNo + "_"
				+ batch).value;
		Qtypicked = removeCommas(Qtypicked);
		Qtypicked = parseFloat(Qtypicked).toFixed(3);
		var balanceQty =  Qtypicked - recvdQty;
		balanceQty = parseFloat(balanceQty).toFixed(3);
		

		if (recvQty > balanceQty) {
			alert("Exceeded the Available Qty of LineOrderNO:: " + orderLNo
					+ "_" + batch);
			document.getElementById("receivingQty_" + orderLNo + "_" + batch)
					.focus();
			document.getElementById("receivingQty_" + orderLNo + "_" + batch)
					.select();
			return false;
		}
		return true;
	}
	function isNumericInput(strString) {
		var strValidChars = "0123456789.-";
		var strChar;
		var blnResult = true;
		if (strString.length == 0)
			return false;
		//  test strString consists of valid characters listed above
		for ( var i = 0; i < strString.length && blnResult == true; i++) {
			strChar = strString.charAt(i);
			if (strValidChars.indexOf(strChar) == -1) {
				blnResult = false;
			}
		}
		return blnResult;
	}
</script>
</head>

	

<%@ include file="body.jsp"%>
<body>
<FORM name="form" method="post" action="/track/TransferOrderServlet?">
</br>
<table border="1" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Bulk 
		Receipt By Transfer Order</FONT>&nbsp;</TH>
</table>
<CENTER>
<table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
   <tr><td><font class="maingreen"> <%=fieldDesc%></font></td> </tr>
 </table>
 </CENTER>
 <table border="1" width="90%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd" >
	<tr>	
		<td width="100%">&nbsp;
 						<TABLE BORDER="0" CELLSPACING=1 WIDTH="90%" align = "center">							
							<tr>
								<th WIDTH="20%" ALIGN="left">Transfer Order Number :</th>
								<td>
								<P><INPUT type="TEXT" size="20" MAXLENGTH="20" name="TONO"
									value="<%=tono%>" onkeypress="if((event.keyCode=='13') && ( document.form.TONO.value.length > 0)){loadTransferOrderDetails();}"/> <a href="#"
									onClick="javascript:popUpWin('to_list_do.jsp?TONO='+form.TONO.value);">
								<img src="images/populate.gif" border="0" /> </a> <input
									type="button" value="View" name="SubmitView" onclick="viewTransferOrders();"/></P>
								</td>
								<th WIDTH="20%" ALIGN="left">Assignee Name:</th>
								<TD><INPUT name="CUST_NAME" class="inactivegry"
									MAXLENGTH="20" readonly type="TEXT" value="<%=su.forHTMLTag(custName)%>"
									size="30" MAXLENGTH=80> <INPUT type="hidden"
									name="CUST_CODE" value="<%=custCode%>"> 
								</TD>
							</tr>
							<tr>
								<th WIDTH="20%" ALIGN="left">From Loc</th>
								<td><INPUT type="TEXT" size="20" class="inactivegry"
									MAXLENGTH="20" readonly MAXLENGTH=20 name="FROM_WAREHOUSE"
									value="<%=fromWareHouse%>"></td>
								<th WIDTH="20%" ALIGN="left">Person Incharge:</th>
								<td><INPUT type="TEXT" size="20" class="inactivegry"
									MAXLENGTH=20 readonly name="PERSON_INCHARGE"
									value="<%=personIncharge%>"></td>
							</tr>
							<tr>
								<th WIDTH="20%" ALIGN="left">To Loc</th>
								<td colspan = "2"><INPUT type="TEXT" size="20" class="inactivegry"
									MAXLENGTH="20" readonly name="TO_WAREHOUSE"
									value="<%=toWareHouse%>" /> </td>
								
							</tr>
							<tr>
								<th WIDTH="20%" ALIGN="left">Order Date:</th>

								<td colspan = "2"><INPUT type="TEXT" class="inactivegry" MAXLENGTH="20"
									readonly size="20" name="COLLECTION_DATE" value="<%=collectionDate%>" /></td>

								
							</tr>
							<tr>
								<th WIDTH="20%" ALIGN="left" height="30">Time:</th>
								<td colspan = "2" height="30"><INPUT type="TEXT" size="20"
									class="inactivegry" MAXLENGTH="20" readonly
									name="COLLECTION_TIME" value="<%=collectionTime%>" /></td>
								

							</tr>
							<tr>
								<th WIDTH="20%" ALIGN="left">Remarks</th>
								<td colspan = "2"><INPUT type="TEXT" size="20" MAXLENGTH="20"
									class="inactivegry" readonly name="REMARK1"
									value="<%=remark%>" /></td>
								
							</tr>
							 
						</TABLE>
						</td>
					</tr>
				</table>
				<br>
						<table BORDER = "1" WIDTH = "90%" align="center" bgcolor="#dddddd" >
						<tr><td width = "15%">  <INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
                        &nbsp; Select/Unselect All <INPUT Type=Checkbox  style="border:0;" name="fullReceive"  <%if(fullReceive.equalsIgnoreCase("true")){%>checked <%}%> value="fullReceive" onclick="return fullReceiving(this.checked)">
                        &nbsp; Full Receiving </td>
</tr>
</table>

				<TABLE BORDER="1" CELLSPACING="0" WIDTH="90%" bgcolor="navy" ALIGN = "CENTER">
					<tr>
						<th width="8%"><font color="#ffffff">Select </font></th>
						<th width="10%"><font color="#ffffff">Order Line No </font></th>
						<th width="15%"><font color="#ffffff">Product ID </font></th>
						<th width="20%"><font color="#ffffff">Description </font></th>
						<th width="14%"><font color="#ffffff">Picked Qty </font></th>
						<th width="10%"><font color="#ffffff">Received Qty </font></th>
						<th width="10%"><font color="#ffffff">Receiving	Qty</font>&nbsp;</th>						
						<th width="15%"><font color="#ffffff">Batch </font></th>
						<th width="5%"><font color="#ffffff"><%=IDBConstants.UOM_LABEL%> </font></th>
					</tr>
				</TABLE>
				<table width="90%" border="0" cellspacing="0" cellpadding="5"
					bgcolor="#eeeeee" align = "center">
					<%
						String recvingQty = null,value = null;
						ArrayList al = _TOUtil.listTODETForBulkReceiving(plant,tono);
						if(al.size()==0)
					    {
							isData="";
					    }
					      
						if (al.size() > 0) {
							isData="DATA";
							for (int i = 0; i < al.size(); i++) {
								recvingQty = null;
								Map m = (Map) al.get(i);
								int iIndex = i + 1;
								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"
										: "#dddddd";
								tono = (String) m.get("TONO");								
								String tolnno = StrUtils.fString((String)m.get("TOLNO"));
								String item = (String) m.get("ITEM");
								String batch = (String) m.get("BATCH");
								String qtyPick = (String) m.get("PICKQTY");
								String qtyrc = (String) m.get("RECVQTY");
								String desc = (String) m.get("Itemdesc");//_ItemMstDAO.getItemDesc(plant, item);
								String uom = (String) m.get("uom");//_ItemMstDAO.getItemUOM(plant, item);
								
								 if(checkedTOS!=null && action.equalsIgnoreCase("CATCHERROR")){
									 recvingQty = (String)checkedTOS.get(tolnno+":"+batch); 
							     }
								
					%>
					<TR bgcolor="<%=bgcolor%>">

						<TD width="8%" align="CENTER"><font color="black"><INPUT
							Type=checkbox style="border: 0;" name="chkdToNo" <%if(recvingQty!=null){%>checked <%}%>
							value="<%=tolnno%>:<%=batch%>" onclick = "setReceivingQty('<%=tolnno%>','<%=batch%>','<%=i%>');"></font></TD>
						<TD width="10%" align="center"><%=tolnno%></TD>
						<TD align="center" width="15%"><%=item%></TD>
						<TD align="center" width="20%"><%=(String) desc%></TD>
						<TD align="center" width="14%"><input type="text" name = "Qtypicked_<%=tolnno%>_<%= batch%>" id = "Qtypicked_<%=tolnno%>_<%= batch%>" 
						 size = "6" maxlength = "10"   style="border: 0px; background-color: '<%=bgcolor%>';" readonly  value = <%=qtyPick%>></input>
						</TD>
						<TD align="center" width="10%"><input type="text" name = "QtyReceived_<%=tolnno%>_<%= batch%>" id = "QtyReceived_<%=tolnno%>_<%= batch%>" 
						 size = "6" maxlength = "10" style="border: 0px;background-color: '<%=bgcolor%>';" readonly  value = <%=qtyrc%> ></input>
						</TD>
						<TD align="center" width="10%"><input type="text" name = "receivingQty_<%=tolnno%>_<%= batch%>" id = "receivingQty_<%=tolnno%>_<%=batch%>" 
						 size = "6" maxlength = "10" <%if( recvingQty!=null){ %> value = <%=recvingQty%> <%} %>>
						</TD>
						<TD align="center" width="15%"><%=batch%></TD>
						<TD align="center" width="5%"><%=uom%></TD>
											
					</TR>

					<%
						}
						} else {
					%>

					<TR>
						<TD align="center" width="15%">Data's Not Found For Receiving</TD>
					</TR>
					<%
						}
					%>
				</table>
				<INPUT	type="Hidden" name="RFLAG" value="7">
				<INPUT type="Hidden" name="isData" value="<%=isData%>">
				
				
				<div align="center">
				<center><br>
				<table border="0" width="70%" cellspacing="20" cellpadding="0">
				<tr>
				<td width="18%"><b>To Loc : </b><INPUT name="LOC_0" id="LOC_0" type="TEXT"
					 size="20"  <% if(action.equalsIgnoreCase("CATCHERROR")){ %> value = '<%=item_loc%>' <%}else{ %>  value = '<%=toWareHouse%>'<%}%>
					  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}"
					MAXLENGTH=80><a href="#"
					onClick="javascript:popUpWin('loc_list_MultiReceivewms.jsp?INDEX=0&LOC='+form.LOC_0.value);"><img
					src="images/populate.gif" border="0"></a> </td>
			
			<td width="24%"><b>Transaction Date : </b><INPUT name="TRANSACTIONDATE" id="RECVDATE_0"
					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" MAXLENGTH="80" readonly="readonly">
					<a href="javascript:show_calendar('form.TRANSACTIONDATE');"
			  		 onmouseover="window.status='Date Picker';return true;"
			   		onmouseout="window.status='';return true;"> <img
			   		src="images\show-calendar.gif" width="24" height="22" border="0" /></a>
			   	</td>
			<td width="15%"><b>Remarks : </b><INPUT name="REF" type="TEXT" value="<%=REMARKS%>" size="35" MAXLENGTH=100></td>
				
				</tr>
					<tr>
						<td colspan = "2" align="center"><input type="Button" value="Cancel" 
							onClick="window.location.href='../home'"> <input
							type="Button" value="Receiving" name="Submit"
							onClick="if(onReceive(document.form)) {submitForm();}" /><INPUT name="LOGIN_USER"
							type="hidden" value="<%=sUserId%>">
						</TD>
					</tr>
					</table>
				</center>
				</div>
 <br>
</FORM>
</body>
</html>
<%@ include file="footer.jsp"%>
