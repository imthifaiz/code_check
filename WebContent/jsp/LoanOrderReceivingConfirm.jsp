<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Load Order Receiving</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'LoanOrderReceiving', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
      
function validateform(form)//form
{
 
        var frmRoot=document.form;
        var recvQty = document.form.RECVQTY.value;
        var Aailqty = document.form.QTY.value;
        var orQty=document.form.ORDERQTY.value;
        var PickedQty=document.form.PICKED_QTY.value;
        var alreadyReceivedQty=document.form.RECVED_QTY.value;
        recvQty = removeCommas(recvQty);
        Aailqty = removeCommas(Aailqty);
 if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter BATCH!");
		frmRoot.BATCH.focus();
		return false;
         }
         
          if (frmRoot.LOC1.value.length < 1)
  {
    alert("Please Enter/Select To Loc !");
    form.LOC1.focus();
    return false;
  }
  else if(isNaN(recvQty)) {
  alert("Please enter valid Receive QTY.");
  document.form.RECVQTY.focus();
  return false;
  
  }
  else if( parseFloat(recvQty) > parseFloat(Aailqty) ) {
  alert("Entered Receiving Qty is Greater than Available Qty To Recv.");
  document.form.RECVQTY.focus();
  return false;
  }
 // else if( parseFloat(alreadyReceivedQty)+parseFloat(recvQty) > parseFloat(PickedQty) ) {alert("Entered Received Qty is Greater than the Available balance to Pick.");document.form.PICKINGQTY.focus(); return false;}
 
  else if(document.form.RECVQTY.value == 0  )
  {
     
     alert("Qty Should not be 0 or less than 0");
     frmRoot.RECVQTY.focus();
     return false;
  }
  else
   {
      return true;
   }
 
}


function onClear(){
 

  document.form.ORDERNO.value="";
  document.form.CUSTNAME.value="";
  document.form.ITEMNO.value="";
  document.form.ITEMDESC.value="";
  document.form.LOC.value="";
  document.form.BATCH.value="";
  document.form.REF.value="";
  document.form.ORDERQTY.value="";
  document.form.QTY.value="";
  document.form.CONTACTNAME.value="";
  document.form.TELNO.value="";
  document.form.EMAIL.value="";
  document.form.ADD1.value="";
  document.form.ADD2.value="";
  document.form.ADD3.value="";
  document.form.FRLOC.value="";
  document.form.TOLOC.value="";
 
  
  return true;
}
  
</script>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="db" class="com.track.gates.defaultsBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />

<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
       
     //POUtil  _POUtil= new POUtil();
       StrUtils strUtils=new StrUtils();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String plant=(String)session.getAttribute("PLANT");
       
       LoanHdrDAO _loHdrDAO = new LoanHdrDAO();
       
       String  fieldDesc="";
       String   ORDERNO    = "",ITEMNO   = "", ITEMDESC  = "",UOM="",
       LOC   = "" ,LOC1="" ,CHECKQTY="",BATCH  = "", REF   = "",ORDERLNO,
       QTY = "",RECEIVEQTY="",CUSTNAME="",ORDERQTY="",INVQTY="",RECVQTY="",PICKED_QTY="",RECVED_QTY="",AVAILQTY="",
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="";
              
       ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=strUtils.fString(request.getParameter("ORDERLNO"));
       CUSTNAME= strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUSTNAME")));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC =  strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")+" "));
       LOC = strUtils.fString(request.getParameter("FRLOC"));
       LOC1 = strUtils.fString(request.getParameter("TOLOC"));
       PICKED_QTY=strUtils.fString(request.getParameter("PICKED_QTY"));
       RECVED_QTY=strUtils.fString(request.getParameter("RECVED_QTY"));
       RECVQTY = strUtils.fString(request.getParameter("RECVQTY"));
        
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       RECVQTY = strUtils.fString(request.getParameter("RECVQTY"));
       INVQTY = strUtils.fString(request.getParameter("QTY"));
       REF = strUtils.fString(request.getParameter("REF"));
       ORDERQTY = strUtils.fString(request.getParameter("ORDERQTY"));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       ItemMstDAO itemmstdao = new ItemMstDAO();
       UOM = itemmstdao.getItemUOM(plant,ITEMNO);
       
      // ArrayList list=(ArrayList)session.getAttribute("customerlistqry3");
      
       ArrayList list=_loHdrDAO.getLoanOrderHderDetails(plant,	ORDERNO);
       
      
              
        for(int i=0;i<list.size();i++)
        {
          Map m = (Map)list.get(i);
          
        //  if(CUSTNAME.equalsIgnoreCase(strUtils.replaceCharacters2Recv((String)m.get("custname"))))
        // {
           CONTACTNAME = (String)m.get("contactname");
           TELNO=(String)m.get("telno");
           EMAIL=(String)m.get("email");
           ADD1=(String)m.get("add1");
           ADD2=(String)m.get("add2");
           ADD3=(String)m.get("add3");
       // }
      }
    

      
      
     if(action.equalsIgnoreCase("result"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("RESULT");
          fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
      }
      else if(action.equalsIgnoreCase("resulterror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
          fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
      
      else if(action.equalsIgnoreCase("qtyerror"))
      {
 
        fieldDesc=(String)request.getSession().getAttribute("QTYERROR");
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
     
    }

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post"
	action="/track/LoanOrderReceivingServlet?"><INPUT
	type="hidden" name="RECVED_QTY" value="<%=RECVED_QTY%>"> <INPUT
	type="hidden" name="PLANT" value="<%=plant%>"> <br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Goods
		Receipt By Loan Order </FONT>&nbsp;</TH>

	</TR>
</TABLE>
<br>

<font face="Times New Roman" size="4">
<table border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<%=fieldDesc%>
	</font>
</table>

<TABLE border="0" CELLSPACING=1 WIDTH="100%" bgcolor="#dddddd">
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Order No</TH>
		<TD width="42%"><INPUT name="ORDERNO" type="TEXT"
			value="<%=ORDERNO%>" size="35" class="inactivegry" readonly
			MAXLENGTH="80" /></TD>
		<TH WIDTH="8%"></TH>
		<TD width="37%"></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="Right">Customer Name :</TH>
		<TD width="42%"><INPUT name="CUSTNAME" class="inactivegry"
			type="TEXT" value="<%=su.forHTMLTag(CUSTNAME)%>" size="35" MAXLENGTH=80 readonly></TD>
		<TD width="8%"></TD>
		<TH WIDTH="37%" ALIGN="left">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Assignee&nbsp; Details</TH>

	</TR>

	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Product ID:</TH>
		<TD width="42%"><INPUT name="ITEMNO" type="TEXT"
			value="<%=ITEMNO%>" size="35" class="inactivegry" readonly
			MAXLENGTH="80" /></TD>
		<TH WIDTH="8%" ALIGN="Right">Contact Name:</TH>
		<TD width="37%"><INPUT name="CONTACTNAME" class="inactivegry"
			value="<%=CONTACTNAME%>" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>

	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Product Desc &nbsp;:</TH>
		<TD width="42%"><INPUT name="ITEMDESC" type="TEXT"
			value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="35" class="inactivegry" readonly
			MAXLENGTH="80" /></TD>
		<TH WIDTH="8%" ALIGN="Right">Telephone :</TH>
		<TD width="37%"><INPUT name="TELNO" class="inactivegry"
			value="<%=TELNO%>" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Order Qty:</TH>
		<TD WIDTH="42%"><INPUT name="ORDERQTY" type="TEXT"
			value="<%=StrUtils.formatNum(ORDERQTY)%>" size="35" MAXLENGTH="80" class="inactivegry"
			readonly />
	&nbsp;&nbsp;&nbsp;&nbsp;<b><%=IDBConstants.UOM_LABEL %> :</b>&nbsp;<INPUT name="UOM" class="inactivegry" type = "TEXT" value="<%=UOM%>" size=10"  MAXLENGTH=15 readonly>		
			</TD>
		<TH WIDTH="8%" ALIGN="Right">Email :</TH>
		<TD width="37%"><INPUT name="EMAIL" value="<%=EMAIL%>"
			class="inactivegry" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Fr Loc&nbsp; :</TH>
		<TD width="42%"><INPUT name="LOC" type="TEXT" value="<%=LOC%>"
			size="35" class="inactivegry" MAXLENGTH="80" readonly /></TD>

		<TH WIDTH="8%" ALIGN="Right">Unit&nbsp;No :</TH>
		<TD width="37%"><INPUT name="ADD1" class="inactivegry"
			value="<%=ADD1%>" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">To Loc&nbsp; :</TH>
		<TD width="45%"><INPUT name="LOC1" type="TEXT" value="<%=LOC1%>"
			size="35" MAXLENGTH=20> <a href="#"
			onClick="javascript:popUpWin('list/loanLocList.jsp?FEILD_NAME=LOC1&LOC='+form.LOC1.value);">
		<img src="images/populate.gif" border="0" /> </a>


		<TH WIDTH="8%" ALIGN="Right">Building :</TH>
		<TD width="37%"><INPUT name="ADD2" class="inactivegry"
			value="<%=ADD2%>" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Batch&nbsp; :</TH>
		<TD width="42%"><INPUT name="BATCH" type="TEXT"
			value="<%=BATCH%>" size="35" MAXLENGTH="40" onkeypress="if((event.keyCode=='13') && ( document.form.BATCH.value.length > 0)){validateBatchDetails();}" /> <a href="#"
			onClick="javascript:popUpWin('list/LoanOrderListToRecv.jsp?ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value+'&BATCH='+form.BATCH.value+'&ORDERNO='+form.ORDERNO.value+'&ORDERLNO='+form.ORDERLNO.value);">
		<img src="images/populate.gif" border="0" /> </a></TD>
		<TH WIDTH="8%" ALIGN="Right">Street :</TH>
		<TD width="37%"><INPUT name="ADD3" value="<%=ADD3%>"
			class="inactivegry" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Available Qty&nbsp;&nbsp;:</TH>
		<TD width="42%"><INPUT name="QTY" type="TEXT" class="inactivegry"
			readonly value="<%=StrUtils.formatNum(QTY)%>" size="35" MAXLENGTH="80" /></TD>
		<TH WIDTH="13%" ALIGN="Right">Remarks&nbsp;&nbsp;:</TH>
		<TD width="42%"><INPUT name="REF" type="TEXT" value="<%=REF%>"
			size="35" MAXLENGTH="80" /></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="Right">Issue Qty</TH>
		<TD width="42%"><INPUT name="PICKED_QTY" type="TEXT"
			class="inactivegry" value="<%=StrUtils.formatNum(PICKED_QTY)%>" size="35" MAXLENGTH="80" />
		</TD>
		<TH WIDTH="13%" ALIGN="Right">&nbsp;&nbsp;</TH>
		<TD width="42%">&nbsp;&nbsp;</TD>

	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="Right">Receiving Qty</TH>
		<TD width="42%"><INPUT name="RECVQTY" type="TEXT"
			value="<%=RECVQTY%>" size="35" MAXLENGTH="80" onkeypress="if((event.keyCode=='13') && ( document.form.BATCH.value.length > 0)){document.form.actionSubmit.focus();}"/></TD>
		<TH WIDTH="13%" ALIGN="Right">&nbsp;&nbsp;</TH>
		<TD width="42%">&nbsp;&nbsp;</TD>

	</TR>
	<TR>
		<TD WIDTH="35%" COLSPAN=2>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT class="Submit" type="BUTTON" value="Cancel"
			onClick="window.location.href='LoanOrderReceiving.jsp?action=View&DONO='+form.ORDERNO.value+'&PLANT='+form.PLANT.value " />&nbsp;&nbsp;
		<input type="button" value="Receiving Confirm" name="actionSubmit"
			onClick="if(validateform(document.form)) { submitFormValues(); }" />


		</TD>
		<TH WIDTH="8%"></TH>
		<TH WIDTH="37%"></TH>
		<TD width="35%"></TD>
	</TR>

</TABLE></center>
<Table border=0 bgcolor="#dddddd">
	<TR bgcolor="#dddddd">
		<INPUT name="ORDERLNO" type="hidden" value="<%=ORDERLNO%>" size="1"
			MAXLENGTH=80>
		</TD>
		<INPUT name="LOGIN_USER" type="hidden" value="<%=sUserId %>" size="1"
			MAXLENGTH=80>
		</TD>
		<INPUT name="CHECKQTY" type="hidden" value="<%=QTY%>" size="1"
			MAXLENGTH=80>
		</TD>
		<INPUT name="INVQTY" type="hidden" value="<%=INVQTY%>" size="1"
			MAXLENGTH=80>
		</TD>
	</TR>
</Table>
</FORM>

</HTML>
<script>
function submitFormValues(){
			document.form.action="/track/LoanOrderReceivingServlet?action=Receiving Confirm";
			//document.form.action.value ="View";
            document.form.submit();
		}
function validateBatchDetails() {
	var loanOrderNo = document.form.ORDERNO.value;
	var orderLineNumber = document.form.ORDERLNO.value;
	var itemNo = document.form.ITEMNO.value;
	var location = document.form.LOC.value;
	var batch = document.form.BATCH.value;
	var urlStr = "/track/LoanOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : loanOrderNo,
			ORDER_LNNO : orderLineNumber,
			ITEMNO : itemNo,
			LOC : location,
			BATCH : batch,
			PLANT : "<%=plant%>",
			ACTION : "VALIDATE_BATCH_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.form.BATCH.value = resultVal.BATCH;
							document.form.QTY.value = ((resultVal.PICK_QTY) * 1 - (resultVal.RECEIVED_QTY) * 1);
							document.form.RECVQTY.focus();

						} else {
							alert("Not a valid Batch No!");
							document.form.BATCH.value = "";
							document.form.QTY.value = "";
							document.form.BATCH.focus();
						}
					}
				});
	}
	document.form.BATCH.focus()
</script>
<%@ include file="footer.jsp"%>
