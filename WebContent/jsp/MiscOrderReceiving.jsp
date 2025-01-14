<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ include file="header.jsp"%>

<!-- Not in Use - Menus status 0 -->
<html>
<script language="JavaScript" type="text/javascript"
	src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Misc Order Receipt</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'InboundOrderList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }

function validatePO(form)//form
{
 
   var frmRoot=document.form;
   
   if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Enter Product ID!");
		frmRoot.ITEMNO.focus();
		return false;
   }
   
   else   if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Enter BATCH!");
		frmRoot.BATCH.focus();
		frmRoot.BATCH.value="NOBATCH";
		return false;
   }
   
   else   if(frmRoot.QTY.value=="" || frmRoot.QTY.value==0)
	 {
		alert("Enter QTY!");
		frmRoot.QTY.focus();
		return false;
   }
   else   if(frmRoot.REASONCODE.value=="" || frmRoot.REASONCODE.value.length==0 )
	 {
		alert("Enter REASONCODE!");
		frmRoot.REASONCODE.focus();
		return false;
   }
  
   else if(isNaN(document.form.QTY.value)) {alert("Enter valid QTY.");document.form.QTY.focus(); return false;}
   
   else
    {
	   document.form.action.value="MiscOrderReceiving";
	   document.form.submit();
	   return true;
    }
 
}



function onClear(){
 
  // document.form.action  = "MiscOrderReceiving.jsp?action=CLEAR";
  // document.form.submit();
  document.form.ITEMNO.value="";
  document.form.ITEMDESC.value="";
  document.form.LOC.value="";
  document.form.BATCH.value="";
  document.form.QTY.value="";
  document.form.REASONCODE.value="";
  document.form.REFDET.value="";
  document.form.EXPIREDATE.value="";
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

<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
       
     //  POUtil  _POUtil= new POUtil();
     
       StrUtils strUtils=new StrUtils();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
      
       //String action = strUtils.fString(request.getParameter("action")).trim();
       String PLANT= (String) session.getAttribute("PLANT");
       String  fieldDesc="", fieldDescError="";
       String ITEMNO   = "", ITEMDESC  = "",
       LOC   = "" , BATCH  = "", REF   = "",
       QTY = "",REFDET="",REASONCODE="",EXPIREDATE="",UOM="";
       
     
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC =strUtils.replaceCharacters2Recv( strUtils.fString(request.getParameter("ITEMDESC")));
       LOC = strUtils.fString(request.getParameter("LOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       REFDET = strUtils.fString(request.getParameter("REFDET"));
       REASONCODE = strUtils.fString(request.getParameter("REASONCODE"));
       EXPIREDATE = strUtils.fString(request.getParameter("EXPIREDATE"));
       
      
     if(action.equalsIgnoreCase("CLEAR"))
      {
       
        ITEMNO = "";
        ITEMDESC = "";
        LOC = "";
        BATCH = "";
        REF = "";
        REFDET  = "";
        REASONCODE = "";
        EXPIREDATE ="";  
      }
      
      else if(action.equalsIgnoreCase("result"))
      {
        fieldDesc=(String)request.getSession().getAttribute("RESULT");
      }
      else if(action.equalsIgnoreCase("resulterror"))
      {
 
    	  fieldDescError=(String)request.getSession().getAttribute("RESULTERROR");
      }
      else if(action.equalsIgnoreCase("batchresult"))
      {
 
       //fieldDesc=(String)request.getSession().getAttribute("BATCHRESULT");
      }
      else if(action.equalsIgnoreCase("batcherror"))
      {
 
    	  fieldDescError=(String)request.getSession().getAttribute("BATCHERROR");
      }
       else if(action.equalsIgnoreCase("catchbatcherror"))
      {
 
       fieldDescError=(String)request.getSession().getAttribute("CATCHBATCHERROR");
      }
   


%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post"
	action="/track/MiscOrderReceivingServlet?action=MiscOrderReceiving"><br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Misc
		Receipt </FONT>&nbsp;</TH>

	</TR>
</TABLE>
<br>

<font face="Times New Roman" size="4">
<table border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<font class="maingreen"><%=fieldDesc%></font>
	<font class="mainred"><%=fieldDescError%></font>
	</font>
</table>

<TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">



	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Loc&nbsp; :</TH>
		<TD><INPUT name="LOC" type="TEXT" value="<%=LOC%>" size="50"
			MAXLENGTH=80
			onkeypress="if((event.keyCode=='13') && ( document.form.LOC.value.length > 0)) {validateLocation();}">
		<a href="#"
			onClick="javascript:popUpWin('loc_list_receivewms.jsp?LOC='+form.LOC.value);">
		<!-- <a href="#" onClick="javascript:popUpWin('batch_list_putawaywms.jsp?ITEMNO='+form.ITEMNO.value+'&BATCH='+form.BATCH.value);">-->
		<img src="images/populate.gif" border="0" /> </a></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Product ID:</TH>
		<TD><INPUT name="ITEMNO" type="TEXT" value="<%=ITEMNO%>"
			size="50" MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.ITEMNO.value.length > 0)){validateProduct();}">
		<a href="#" onClick="javascript:popUpWin('miscreceiving_item_list.jsp?ITEMNO='+form.ITEMNO.value);"> <img src="images/populate.gif" border="0" /> </a></TD>

	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">&nbsp;Product Desc &nbsp;:</TH>
		<TD><INPUT name="ITEMDESC"  type="TEXT"
			value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="50" MAXLENGTH=100 >
                        <a href="#" onClick="javascript:popUpWin('miscreceiving_item_list.jsp?DESC='+form.ITEMDESC.value);"> <img src="images/populate.gif" border="0" /> </a></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Batch&nbsp; :</TH>
		<TD><INPUT name="BATCH" type="TEXT" value="<%=BATCH%>" size="40"
			MAXLENGTH=40 onkeypress="if(event.keyCode=='13'){validateBatch();}">
		<input type="button" onclick="generateBatch()" value="Generate Batch"
			name="action" /></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Qty&nbsp;&nbsp;:</TH>
		<TD><INPUT name="QTY" type="TEXT" value="<%=QTY%>" size="40"
			MAXLENGTH=50
			onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length > 0)){validateQuantity();}">
			<INPUT name="UOM" type="TEXT" value="" size="10"
			MAXLENGTH=10
			></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Remarks&nbsp;:</TH>
		<TD><INPUT name="REFDET" type="TEXT" value="<%=REFDET%>"
			size="50" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.EXPIREDATE.focus();}"> </a></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Expiry Date&nbsp;:</TH>
		<TD><INPUT name="EXPIREDATE" type="TEXT" readonly="readonly" value="<%=EXPIREDATE%>"
			size="50" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.REASONCODE.focus();}">
			<a href="javascript:show_calendar('form.EXPIREDATE');"
			   onmouseover="window.status='Date Picker';return true;"
			   onmouseout="window.status='';return true;"> <img
			   src="images\show-calendar.gif" width="24" height="22" border="0" />
				</a>
		</TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">ReasonCode&nbsp;:</TH>
		<TD><INPUT name="REASONCODE" type="TEXT" value="<% if(REASONCODE.length() > 0) { out.print(REASONCODE); }else{out.print("NOREASONCODE");}%>"
			size="50" readonly MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.REASONCODE.value.length > 0)){ document.form.SubmitButton.focus();}"> <a href="#"
			onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form.ITEMNO.value);">
		<img src="images/populate.gif" border="0" /> </a></TD>
	</TR>
	<TR>
		<TD COLSPAN=2><BR>
		<INPUT name="noOfLabelToPrint" type="HIDDEN" size="50" readonly
			MAXLENGTH="80" /><B>
		<CENTER><%=REF%>
		</B></TD>
	</TR>
	<TR>
		<TD COLSPAN=2>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT class="Submit" type="BUTTON" value="Cancel"
			onClick="window.location.href='../home'" />&nbsp;&nbsp; <input
			type="BUTTON" value="MiscOrderReceiving" name="SubmitButton"
			onClick="validatePO(document.form)" /> <INPUT class="Submit"
			type="BUTTON" value="Clear" onClick="return onClear();">&nbsp;&nbsp;

		</TD>
	</TR>

</TABLE>
<INPUT name="LOGINUSER" type="hidden" value="<%=sUserId %>" size="1"
	MAXLENGTH=80> <INPUT name="action" type="hidden">
</TD></center>

</FORM>

</HTML>
<script>
function validateLocation() {
	var locId = document.form.LOC.value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.form.LOC.focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
				PLANT : "<%=PLANT%>",
					ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						document.form.ITEMNO.value = "";
						document.form.ITEMNO.focus();
					} else {
						alert("Not a valid Location");
						document.form.LOC.value = "";
						document.form.LOC.focus();
					}
				}
			});
		}
	}
function validateProduct() {
	var productId = document.form.ITEMNO.value;
	if(document.form.ITEMNO.value=="" || document.form.ITEMNO.value.length==0 ) {
		alert("Enter Product ID!");
		document.form.ITEMNO.focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=PLANT%>",
				ACTION : "VALIDATE_PRODUCT"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						document.form.ITEMDESC.value = resultVal.discription;
						document.form.UOM.value = resultVal.uom;
						document.form.BATCH.value = "NOBATCH";
						document.form.BATCH.focus();
					} else {
						alert("Not a valid product or It is a parent product");
						document.form.ITEMNO.value = "";
						document.form.ITEMNO.focus();
					}
				}
			});
		}
	}

	function validateBatch() {
		var batch = document.form.BATCH.value;
		if (batch == "" || batch.length == 0) {
			document.form.BATCH.value = "NOBATCH";
		}
		document.form.QTY.value = "";
		document.form.QTY.focus();
	}
	function validateQuantity() {
		var qty = document.form.QTY.value;
		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.form.QTY.focus();
		} else {
			if (isNumericInput(qty) == false) {
				alert("Entered Quantity is not a valid Qty!");
			} else {
				document.form.REFDET.value = "";
				document.form.REFDET.focus();
			}

		}
	}
	function generateBatch(){

			var urlStr = "/track/MiscOrderHandlingServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
				PLANT : "<%=PLANT%>",
				ACTION : "GENERATE_BATCH"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "100") {
					var resultVal = data.result;
					document.form.BATCH.value = resultVal.batchCode;
					document.form.QTY.focus();

				} else {
					alert("Unable to genarate Batch");
					document.form.BATCH.value = "";
					document.form.BATCH.focus();
				}
			}
		});
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
	//document.form.LOC.value = "";
	document.form.LOC.focus();
</script>
<%@ include file="footer.jsp"%>
