<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Picking By OutBound Order</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'OutBoundPicking', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
  }
      
      
 function IsNumeric(sText)
{
var ValidChars = "0123456789.";
for (i = 0; i < sText.length; i++) { 
if (ValidChars.indexOf(sText.charAt(i)) == -1) {
return false;
}
}
return true;
}

  function validatePO(form)//form
  {
  	var frmRoot=document.form;
    if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 )
	 {
		alert("Please Enter Order No!");
		frmRoot.ORDERNO.focus();
		return false;
     }
     else  if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Please Enter Product ID!");
		frmRoot.ITEMNO.focus();
		return false;
     }
  

  else if(isNaN(document.form.PICKINGQTY.value)) {alert("Please enter valid Quantity.");document.form.PICKINGQTY.focus(); return false;}
   if(!IsNumeric(form.PICKINGQTY.value))
   {
     alert(" Please Enter valid Tran Qty !");
     form.PICKINGQTY.focus();  form.PICKINGQTY.select(); return false;
   }
   
    else if(document.form.PICKINGQTY.value == ""){
        alert("Please enter Quantity");
        document.form.PICKINGQTY.focus();
        return false;
    }

  else if(document.form.PICKINGQTY.value == 0  )

  { 
         alert("Qty Should not be 0 or less than 0");
	 frmRoot.PICKINGQTY.focus();
	 return false;
  }
  else
  {
      return true;
  }
 
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
       StrUtils strUtils=new StrUtils();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String plant=(String)session.getAttribute("PLANT");
       
       DoHdrDAO _DoHdrDAO = new DoHdrDAO();
    
       String  fieldDesc="";
       String   ORDERNO    = "",ITEMNO   = "", ITEMDESC  = "",
       LOC   = "" , CHECKQTY="",BATCH  = "", REF   = "",ORDERLNO,
       QTY = "",RECEIVEQTY="",CUSTNAME="",ORDERQTY="",INVQTY="",PICKINGQTY="",PICKEDQTY="",
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="",UOM="";
              
       ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=strUtils.fString(request.getParameter("ORDERLNO"));
      // CUSTNAME=strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUSTNAME")));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")));
       LOC = strUtils.fString(request.getParameter("LOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       PICKINGQTY = strUtils.fString(request.getParameter("PICKINGQTY"));
       INVQTY = strUtils.fString(request.getParameter("QTY"));
       REF = strUtils.fString(request.getParameter("REF"));
       ORDERQTY = StrUtils.formatNum(strUtils.fString(request.getParameter("ORDERQTY")));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       PICKEDQTY = StrUtils.formatNum(strUtils.fString(request.getParameter("PICKEDQTY")));
        ItemMstDAO itemmstdao = new ItemMstDAO();
        itemmstdao.setmLogger(mLogger);
        UOM = itemmstdao.getItemUOM(plant,ITEMNO);
       ArrayList list = _DoHdrDAO.getOutBoundOrderCustamerDetailsByWMS(plant,ORDERNO);
       
      
     
        for(int i=0;i<list.size();i++)
        {
          Map m = (Map)list.get(i);
          
           CUSTNAME=(String)m.get("custname");
           CONTACTNAME = (String)m.get("contactname");
           TELNO=(String)m.get("hpno");
           EMAIL=(String)m.get("email");
           ADD1=(String)m.get("add1");
           ADD2=(String)m.get("add2");
           ADD3=(String)m.get("add3");
       
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
        //ArrayList list1=(ArrayList)session.getAttribute("customerlistqry2");
        ArrayList list1 = _DoHdrDAO.getOutBoundOrderCustamerDetailsByWMS(plant,ORDERNO);

        for(int i=0;i<list1.size();i++)
        {
          Map m1 = (Map)list1.get(i);
         // if(CUSTNAME.equalsIgnoreCase((String)m1.get("custname")))
        // {
           CUSTNAME=(String)m1.get("custname");
           CONTACTNAME = (String)m1.get("contactname");
           TELNO=(String)m1.get("telno");
           EMAIL=(String)m1.get("email");
           ADD1=(String)m1.get("add1");
           ADD2=(String)m1.get("add2");
           ADD3=(String)m1.get("add3");
       // }
      }
     
    }

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="/track/DoPickingServlet?">
<br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Picking By Mobile Order </FONT>&nbsp;</TH>

	</TR>
</TABLE>
<br>
<font face="Times New Roman" size="4">
<table border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<%=fieldDesc%>
</table>
</font> <INPUT type="hidden" name="PLANT" value="<%=plant%>">
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
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Customer&nbsp; Details</TH>
	</TR>

	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Product ID:</TH>
		<TD width="42%"><INPUT name="ITEMNO" type="TEXT"
			value="<%=ITEMNO%>" size="35" class="inactivegry" readonly
			MAXLENGTH="80" /></TD>
                <TH WIDTH="8%" ALIGN="Right"></TH>
		<!--<TH WIDTH="8%" ALIGN="Right">Contact Name:</TH>-->
		<TD width="37%"><INPUT name="CONTACTNAME" class="inactivegry"
			value="<%=CONTACTNAME%>" type="hidden" size="35" MAXLENGTH=80 readonly></TD>
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
			value="<%=ORDERQTY%>" size="17" MAXLENGTH="80" class="inactivegry"
			readonly />&nbsp;
			<INPUT name="UOM" type="TEXT"
			value="<%=UOM%>" size="10" MAXLENGTH="80" class="inactivegry"
			readonly /></TD>
		<TH WIDTH="8%" ALIGN="Right">Email :</TH>
		<TD width="37%"><INPUT name="EMAIL" value="<%=EMAIL%>"
			class="inactivegry" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Picked&nbsp;Qty&nbsp; :</TH>
		<TD width="42%"><INPUT name="PICKEDQTY" type="TEXT"
			class="inactivegry" value="<%=PICKEDQTY%>" size="35" MAXLENGTH="80"
			readonly /></TD>
		<TH WIDTH="8%" ALIGN="Right">Unit&nbsp;No :</TH>
		<TD width="37%"><INPUT name="ADD1" class="inactivegry"
			value="<%=ADD1%>" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR><TH WIDTH="13%" ALIGN="Right">Picking&nbsp;Qty&nbsp;&nbsp;:</TH>
		<TD width="42%"><INPUT name="PICKINGQTY" type="TEXT"
			value="<%=PICKINGQTY%>" size="35" MAXLENGTH="80"
			onkeypress="if((event.keyCode=='13') && ( document.form.PICKINGQTY.value.length > 0)){validateQuantity();}" />
		</TD>
		<TH WIDTH="8%" ALIGN="Right">Building :</TH>
		<TD width="37%"><INPUT name="ADD2" class="inactivegry"
			value="<%=ADD2%>" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="Right">Remarks&nbsp;&nbsp;:</TH>
		<TD width="42%"><INPUT name="REF" type="TEXT" value="<%=REF%>"
			size="35" MAXLENGTH="80"
			onkeypress="if((event.keyCode=='13') ){document.form.actionSubmit.focus();}" />
		</TD>
		<TH WIDTH="8%" ALIGN="Right">Street :</TH>
		<TD width="37%"><INPUT name="ADD3" value="<%=ADD3%>"
			class="inactivegry" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	
	

	
	<TR>
		<TD WIDTH="35%" COLSPAN=2>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT class="Submit" type="BUTTON" value="Cancel"
			onClick="window.location.href='mobileShopWOInv.jsp?action=View&DONO='+form.ORDERNO.value+'&PLANT='+form.PLANT.value " />&nbsp;&nbsp;
		<input type="button" value="Pick Confirm" name="actionSubmit"
			onClick="if(validatePO(document.form)) {submitForm();}" /></TD>
		<TH WIDTH="8%"></TH>
		<TH WIDTH="37%"></TH>
		<TD width="35%"></TD>
	</TR>

</TABLE>
</center>
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

function submitForm(){
	document.form.action="/track/DoPickingServlet?action=Pick/IssueForMobileShop_WOInvcheck";
    document.form.submit();
}

function validateQuantity() {
	var qty = document.form.PICKINGQTY.value;
	removeCommas(qty);
	if (qty == "" || qty.length == 0) {
		alert("Enter Quantity!");
		document.form.PICKINGQTY.focus();
	} else {
		if (isNumericInput(qty) == false) {
			alert("Entered Quantity is not a valid Qty!");
		} else {
			
				document.form.REF.value = "";
				document.form.REF.focus();
			
		}

	}
}
function isNumericInput(strString) {
	var strValidChars = "0123456789.-";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for (i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
}

</script>
<%@ include file="footer.jsp"%>
