<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<!-- Not in Use - Menus status 0 -->
<html>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Misc Order Issue</title>
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
		return false;
   }
   
   else   if(frmRoot.QTY.value=="" || frmRoot.QTY.value.length==0 )
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
	  document.form.action.value="MiscOrderIssue";
	  document.form.submit();
      return true;
    }
 
}



function onClear(){

  document.form.ITEMNO.value="";
  document.form.ITEMDESC.value="";
  document.form.LOC.value="";
  document.form.BATCH.value="";
  document.form.INVQTY.value="";
  document.form.QTY.value="";
  document.form.REFDET.value="";
  document.form.REASONCODE.value="";
  document.form.REFDET.value="";
  
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
       

     
       StrUtils strUtils=new StrUtils();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
      
       String PLANT= (String) session.getAttribute("PLANT");
       String  fieldDesc="";
       String ITEMNO   = "", ITEMDESC  = "",
       LOC   = "" , BATCH  = "", REF   = "",
       QTY = "",REFDET="",REASONCODE="",INVQTY="";
       
     
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.fString(request.getParameter("ITEMDESC"));
       LOC = strUtils.fString(request.getParameter("LOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       INVQTY = strUtils.fString(request.getParameter("INVQTY"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       REFDET = strUtils.fString(request.getParameter("REFDET"));
       REASONCODE = strUtils.fString(request.getParameter("REASONCODE"));

     if(action.equalsIgnoreCase("CLEAR"))
      {
       
        ITEMNO = "";
        ITEMDESC = "";
        LOC = "";
        BATCH = "";
        REF = "";
        REFDET  = "";
        REASONCODE = "";
        QTY="";
        INVQTY="";
           
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
      else if(action.equalsIgnoreCase("catcherror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
   %>
<%@ include file="body.jsp"%>
<FORM name="form" method="post"
	action="/track/MiscOrderIssuingServlet?action=MiscOrderIssue">
<br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Misc
		Issue </FONT>&nbsp;</TH>

	</TR>
</TABLE>
<br>

<font face="Times New Roman" size="4">
<table border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<%=fieldDesc%>
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
		<!-- <INPUT name="LOC" type = "TEXT" value="<%=LOC%>" size="50"  MAXLENGTH=80 >
         <a href="#" onClick="javascript:popUpWin('misc_loc_list.jsp?ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value);">
                 <a href="#" onClick="javascript:popUpWin('batch_list_putawaywms.jsp?ITEMNO='+form.ITEMNO.value+'&BATCH='+form.BATCH.value);">-->
		<img src="images/populate.gif" border="0" /> </a></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Product ID</TH>
		<TD><INPUT name="ITEMNO" type="TEXT" value="<%=ITEMNO%>"
			size="50" MAXLENGTH=80
			onkeypress="if((event.keyCode=='13') && ( document.form.ITEMNO.value.length > 0)){validateProduct();}">
		<a href="#" onClick="javascript:popUpWin('miscissuing_item_list.jsp?ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value);">
		<img src="images/populate.gif" border="0" /> </a></TD>

	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">&nbsp;Product Desc &nbsp;:</TH>
		<TD><INPUT name="ITEMDESC"  type="TEXT"
			value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="50" MAXLENGTH=80 ><a href="#" onClick="javascript:popUpWin('miscissuing_item_list.jsp?LOC='+form.LOC.value+'&DESC='+form.ITEMDESC.value);">
		<img src="images/populate.gif" border="0" /> </a></TD>
	</TR>

	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Batch&nbsp; :</TH>
		<TD><INPUT name="BATCH" type="TEXT" value="<%=BATCH%>" size="40"
			MAXLENGTH=40
			onkeypress="if((event.keyCode=='13') && ( document.form.BATCH.value.length > 0)){validateBatch();}">
                 <a href="#" onClick="javascript:popUpWin('batch_list_putawaywms.jsp?ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value+'&BATCH='+form.BATCH.value);">
		<img src="images/populate.gif" border="0" /> </a>		
		</TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Availabe Qty&nbsp;&nbsp;:</TH>
		<TD><INPUT name="INVQTY" type="TEXT" value="<%=INVQTY%>"
			size="50" MAXLENGTH=80 class="inactivegry" readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Issuing Qty&nbsp;&nbsp;:</TH>
		<TD><INPUT name="QTY" type="TEXT" value="<%=QTY%>" size="33"
			MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length > 0)){validateQuantity();}"><INPUT name="UOM" type="TEXT" value="" size="10" MAXLENGTH=10></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Remarks&nbsp;:</TH>
		<TD><INPUT name="REFDET" type="TEXT" value="<%=REFDET%>"
			size="50" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.REASONCODE.focus();}"> </a></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">ReasonCode&nbsp;:</TH>
		<TD><INPUT name="REASONCODE" type="TEXT" value="<% if(REASONCODE.length() > 0) { out.print(REASONCODE); }else{out.print("NOREASONCODE");}%>"
			size="50" MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.REASONCODE.value.length > 0)){ document.form.SubmitButton.focus();}"> <a href="#"
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
			type="button" value="MiscOrderIssue" name="SubmitButton"
			onClick="return validatePO(document.form)" /> <INPUT class="Submit"
			type="BUTTON" value="Clear" onClick="return onClear();">&nbsp;&nbsp;

		</TD>
	</TR>

</TABLE>
<INPUT name="LOGINUSER" type="hidden" value="<%=sUserId %>" size="1"
	MAXLENGTH=80> <INPUT name="action" type="hidden"></center>

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
                USERID : "<%=sUserId%>",
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
						document.form.ITEMNO.value=resultVal.item;
						document.form.BATCH.value = "NOBATCH";
						document.form.UOM.value = resultVal.uom;
						document.form.BATCH.focus();

					} else {
						alert("Not a valid product");
						document.form.ITEMNO.value = "";
						document.form.ITEMNO.focus();
					}
				}
			});
		}
	}

	function validateBatch() {
		var batch = document.form.BATCH.value;
		var productId = document.form.ITEMNO.value;
		var locId = document.form.LOC.value;
		if(locId=="" || locId.length==0 ) {
			alert("Enter Location!");
			document.form.LOC.focus();
		}else{
			if(document.form.ITEMNO.value=="" || document.form.ITEMNO.value.length==0 ) {
				alert("Enter Product ID!");
				document.form.ITEMNO.focus();
			}
			else{
				if (batch == "" || batch.length == 0) {
					alert("Enter Batch!");
					document.form.BATCH.focus();
				}else{
					var urlStr = "/track/MiscOrderHandlingServlet";
					$.ajax( {
						type : "POST",
						url : urlStr,
						data : {
							ITEM : productId,
							LOC : locId,
							BATCH : batch,
							PLANT : "<%=PLANT%>",
							ACTION : "VALIDATE_BATCH"
							},
							dataType : "json",
							success : function(data) {
								
								if (data.status == "100") {
									var resultVal = data.result;
									document.form.BATCH.value = resultVal.batchCode;
									document.form.INVQTY.value = resultVal.availableQty;
									document.form.QTY.value = "";
									document.form.QTY.focus();
		
								} else {
									alert("Not a valid Batch");
									document.form.BATCH.value = "";
									document.form.BATCH.focus();
								}
							}
						});
					}
				}
			}
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
				var availableQty = document.form.INVQTY.value;
				availableQty = availableQty*1;
				//alert(qty);
				if(availableQty<qty){
					alert("Entered Quantity is greater than the Available Qty!");
					document.form.QTY.value = "";
					document.form.QTY.focus();
				}else{
					document.form.REFDET.value = "";
					document.form.REFDET.focus();
				}
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
