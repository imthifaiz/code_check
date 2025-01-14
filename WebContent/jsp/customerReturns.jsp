<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ include file="header.jsp"%>
<!-- Not in Use - Menus status 0 -->
<html>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>OutBound Order Returns</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'customerReturns', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }

function validatePO(form)//form
{
 
   var frmRoot=document.form;
   
   if(frmRoot.ITEM.value=="" || frmRoot.ITEM.value.length==0 )
	 {
		alert("Enter Product ID!");
		frmRoot.ITEM.focus();
		return false;
   }
   else   if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value==0)
	 {
		alert("Enter OrderNo");
		frmRoot.ORDERNO.focus();
		return false;
    }
   
   else   if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Enter BATCH!");
		frmRoot.BATCH.focus();
	
		return false;
   }
   
   else   if(frmRoot.QTY.value=="" || frmRoot.QTY.value==0)
	 {
		alert("Enter QTY!");
		frmRoot.QTY.focus();
		return false;
   }
   else   if(frmRoot.LOC.value=="" || frmRoot.LOC.value==0)
	 {
		alert("Enter LOC");
		frmRoot.LOC.focus();
		return false;
     }
   
  
   else if(isNaN(document.form.QTY.value)) {alert("Enter valid QTY.");document.form.QTY.focus(); return false;}

 
   else
    {
	   document.form.action.value="customerReturns";
	   document.form.submit();
	   return true;
    }
 
}



function onClear(){
   
  document.form.ITEM.value="";
  document.form.ITEMDESC.value="";
 
  document.form.BATCH.value="";
  document.form.QTY.value="";
  document.form.REMARK.value="";
  document.form.ORDERNO.value="";
  document.form.LOC.value="";
  document.form.REASONCODE="NOREASONCODE";
  return true;
}
  
</script>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="db" class="com.track.gates.defaultsBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />


<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
       
     //  POUtil  _POUtil= new POUtil();
     
       StrUtils strUtils=new StrUtils();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       
       String PLANT= (String) session.getAttribute("PLANT");
       String  fieldDesc="", fieldDescError="";
       String ITEM   = "", ITEMDESC  = "",
       LOC   = "" , BATCH  = "", REF   = "",
       QTY = "",REMARK="",CUST_NAME="",EXPIREDATE="",UOM="",ORDERNO="",REASONCODE="";
       ItemMstDAO itemdao = new ItemMstDAO();
     
       ITEM = strUtils.fString(request.getParameter("ITEM"));
       ITEMDESC =strUtils.replaceCharacters2Recv( strUtils.fString(request.getParameter("ITEMDESC")));
       LOC= strUtils.fString(request.getParameter("LOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       REMARK = strUtils.fString(request.getParameter("REMARK"));
       CUST_NAME = strUtils.fString(request.getParameter("CUST_NAME"));
       ORDERNO=strUtils.fString(request.getParameter("ORDERNO"));
       REASONCODE = strUtils.fString(request.getParameter("REASONCODE"));
      
     if(action.equalsIgnoreCase("CLEAR"))
      {
       
        ITEM = "";
        ITEMDESC = "";
        LOC = "";
        BATCH = "";
        REF = "";
        REMARK  = "";
        CUST_NAME = "";
       
      }
      
      else if(action.equalsIgnoreCase("result"))
      {
        fieldDesc=(String)request.getSession().getAttribute("RESULT");
      }
      else if(action.equalsIgnoreCase("resulterror"))
      {
 
    	  fieldDescError=(String)request.getSession().getAttribute("RESULTERROR");
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
	action="/track/CustomerReturnsServlet?action=customerReturns"><br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">OutBound Order Returns</FONT>&nbsp;</TH>

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
<input type="hidden" name="CUST_CODE">
<input type="hidden" name="CUST_CODE1">
	
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Product ID:</TH>
		<TD><INPUT name="ITEM" type="TEXT" value="<%=ITEM%>"
			size="50" MAXLENGTH=50 onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}">
		<a href="#" onClick="javascript:popUpWin('list/itemList.jsp?isProductCheck=true&ITEM='+form.ITEM.value);"> <img src="images/populate.gif" border="0" /> </a></TD>

	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">&nbsp;Description &nbsp;:</TH>
		<TD><INPUT name="ITEMDESC"  type="TEXT"
			value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="50" MAXLENGTH=100  readonly="readonly"  class="inactivegry" >
                        </TD>
	</TR>
	
	<TR>
		  <TH WIDTH="35%"  ALIGN="RIGHT">&nbsp;Order No :</TH>
		<TD><INPUT name="ORDERNO" type="TEXT" value="<%=ORDERNO%>"

			size="50" MAXLENGTH=80 onkeypress="if(event.keyCode=='13'){validateOrderno();}">
			<!-- <a href="#" onClick="javascript:popUpWin('list/customerreturnorderlist.jsp?ITEM='+form.ITEM.value+'&ORDERNO='+form.ORDERNO.value);"> <img src="images/populate.gif" border="0" /> </a>-->
			</TD>
	</TR>
	
	<TR>
        
        <TH WIDTH="35%" ALIGN="RIGHT">Batch:</TH>
		<TD><INPUT name="BATCH" type="TEXT" value="<%=BATCH%>"
			size="50" MAXLENGTH=40 onkeypress="if((event.keyCode=='13') && ( document.form.BATCH.value.length > 0)){validateBatch();}">
		</TD>

		
	</TR>
			
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Qty&nbsp;&nbsp;:</TH>
		<TD><INPUT name="QTY" type="TEXT" value="<%=QTY%>" size="50"
			MAXLENGTH=50  
			onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length > 0)){document.form.REMARK.focus();}">
			</TD>
	</TR>
	

	
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Remark&nbsp;:</TH>
		<TD align="left"><INPUT name="REMARK" type="TEXT" value="<%=REMARK%>"
			size="50" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.REASONCODE.focus();}"> </a></TD>
	</TR>

	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">ReasonCode&nbsp;:</TH>
		<TD><INPUT name="REASONCODE" type="TEXT" value="<% if(REASONCODE.length() > 0) { out.print(REASONCODE); }else{out.print("NOREASONCODE");}%>"
			size="50" readonly MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.LOC.focus();}"> <a href="#"
			onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form.ITEM.value);">
		<img src="images/populate.gif" border="0" /> </a></TD>
	</TR>
		<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Loc&nbsp; :</TH>
		<TD><INPUT name="LOC" type="TEXT" value="<%=LOC%>" size="50"
			MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.LOC.value.length > 0)){ document.form.SubmitButton.focus();}"> 
		<a href="#"
			onClick="javascript:popUpWin('loc_list_receivewms.jsp?LOC='+form.LOC.value);">
	  <img src="images/populate.gif" border="0" /> </a></TD>
	</TR>
	<TR>
		<TD COLSPAN=2><BR>
		</TD>
	</TR>
	<TR>
		<TD COLSPAN=2>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT class="Submit" type="BUTTON" value="Cancel"
			onClick="window.location.href='../home'" />&nbsp;&nbsp; <input
			type="BUTTON" value="Submit" name="SubmitButton"
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

function validateProduct() {
	var productId = document.form.ITEM.value;
	if(document.form.ITEM.value=="" || document.form.ITEM.value.length==0 ) {
		alert("Enter Product ID!");
		document.form.ITEM.focus();
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
						document.form.ORDERNO.focus();
					} else {
						alert("Not a valid product");
						document.form.ITEM.value = "";
						document.form.ITEM.focus();
					}
				}
			});
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
			document.form.REMARK.value = "";
			document.form.REMARK.focus();
		}

	}
}

function validateOrderno() {
	var orderno = document.form.ORDERNO.value;
	if (orderno == "" || orderno.length == 0) {
		alert("Enter OrderNo!");
		document.form.ORDERNO.focus();
	} else {
		
			document.form.BATCH.value = "";
			document.form.BATCH.focus();
		

	}
}



function validateBatch() {
	var batch = document.getElementById("BATCH").value;
	var productId = document.getElementById("ITEM").value;
	var orderno = document.getElementById("ORDERNO").value;
			if (batch == "" || batch.length == 0) {
				alert("Enter Batch!");
				document.getElementById("BATCH").focus();
			}else{
				var urlStr = "/track/CustomerReturnsServletjson";
				$.ajax( {
					type : "POST",
					url : urlStr,
					data : {
					    PLANT : "<%=PLANT%>",
						ITEM : productId,
						ORDERNO : orderno,
					    BATCH : batch,
					    
						
						ACTION : "VALIDATE_BATCH"
						},
						dataType : "json",
						success : function(data) {
							
							if (data.status == "100") {
								var resultVal = data.result;
								document.getElementById("QTY").focus();
							} else {
								alert("Not a valid Batch");
								document.getElementById("BATCH").value = "";
								document.getElementById("BATCH").focus();
								
							}
						}
					});
				}
			
		
}

	
</script>
<%@ include file="footer.jsp"%>
