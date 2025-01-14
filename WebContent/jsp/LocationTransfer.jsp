<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<html>
<!-- Not in Use - Menus status 0 -->
<title>Stock Move</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<SCRIPT LANGUAGE="JavaScript">
  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'List', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
  
function onClear(){
  document.form.FROM_LOC.value="";
  document.form.ITEMNO.value="";
  document.form.ITEMDESC.value="";
  document.form.TOLOC.value="";
  document.form.BATCH.value="";
  document.form.QTY.value="";
  document.form.UOM.value="";
  return true;
}

</script>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
  <%
       	  
       vmb.setmLogger(mLogger);
  	
  		  
       StrUtils strUtils=new StrUtils();
       String action   = su.fString(request.getParameter("action")).trim();
       
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String PLANT = (String) session.getAttribute("PLANT");
       String  fieldDesc="";
       String   ORDERNO    = "",ORDERLNO="",CUSTNAME = "", ITEMNO   = "", ITEMDESC  = "",
       FROM_LOC= "",  LOC= "" ,TO_LOC   = "" , CHECKQTY="",BATCH  = "", REF   = "",UOM="",
       QTY = "",INVQTY="",RECEIVEQTY="";
       FROM_LOC = strUtils.fString(request.getParameter("FROM_LOC"));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       INVQTY = strUtils.fString(request.getParameter("INVQTY"));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       LOC = strUtils.fString(request.getParameter("TOLOC"));
       UOM = strUtils.fString(request.getParameter("UOM"));
        
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
      
      else if (action.equalsIgnoreCase("resultcatcherror"))
      {
       fieldDesc=(String)request.getSession().getAttribute("RESULTCATCHERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="/track/LocationTransferServlet?action=LocationTransfer"  >
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Stock Move</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
 <font face="Times New Roman" size="4">
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
  <%=fieldDesc%>
  </font>
  </table>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcol or="#dddddd">
   
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >From Loc&nbsp;&nbsp;: </TH>
            <TD>
                <INPUT name="FROM_LOC" type = "TEXT" value="<%=FROM_LOC%>" size="50" 
            onkeypress="if((event.keyCode=='13') && ( document.form.FROM_LOC.value.length > 0)) {validateLocation(this.value);}"    MAXLENGTH="20">
              <a href="#" onClick="javascript:popUpWin('loc_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
    </TR>
    
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Product ID: </TH>
         <TD><INPUT name="ITEMNO" type = "TEXT" value="<%=ITEMNO%>" size="50" onkeypress="if((event.keyCode=='13') && ( document.form.ITEMNO.value.length > 0)) {validateProduct();}"  MAXLENGTH=80 >
         <a href="#" onClick="javascript:popUpWin('item_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value+'&ITEMNO='+form.ITEMNO.value);">
           <img src="images/populate.gif" border="0"/>
         </a></TD>
              
    </TR>
       
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > &nbsp;Product Desc &nbsp;: </TH>
         <TD><INPUT name="ITEMDESC"  type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="50" 
         MAXLENGTH=80 > <a href="#" onClick="javascript:popUpWin('item_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value+'&DESC='+form.ITEMDESC.value);">
           <img src="images/populate.gif" border="0"/>
         </a></TD>
    </TR>
     
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Batch&nbsp; : </TH>
         <TD><INPUT name="BATCH" type = "TEXT" value="<%=BATCH%>" size="50" 
       onkeypress="if((event.keyCode=='13') && ( document.form.BATCH.value.length > 0)) {validateBatch();}"  MAXLENGTH=40  >
         <a href="#" onClick="javascript:popUpWin('batch_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value+'&ITEMNO='+form.ITEMNO.value+'&BATCH'+form.BATCH.value);">
               
           <img src="images/populate.gif" border="0"/>
         </a>
         </TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Qty&nbsp;&nbsp;: </TH>
         <TD><INPUT name="QTY" type = "TEXT" value="<%=QTY%>" size="33" onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length > 0)) {validateQuantity();}" MAXLENGTH=80 >&nbsp;&nbsp;&nbsp;<INPUT name="UOM" type = "TEXT" value="<%=UOM %>" size="10"  MAXLENGTH=80 ></TD>
    </TR>
       <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >To Loc&nbsp;&nbsp;: </TH>
         <TD>
           <INPUT name="TOLOC" type="TEXT" value="<%=LOC%>" size="50"  onkeypress="if((event.keyCode=='13') && ( document.form.TOLOC.value.length > 0)) {validateTOLocation(this.value);}"  MAXLENGTH="80" />
            <a href="#" onClick="javascript:popUpWin('loc_list_dotransferto.jsp?TOLOC='+form.TOLOC.value);">
           <img src="images/populate.gif" border="0"/>
         </a>
         </TD>
    </TR>
    <TR>
         <TD COLSPAN = 2><BR>
         <INPUT name="noOfLabelToPrint" type="HIDDEN" size="50" readonly MAXLENGTH="80"/><B><CENTER><%=REF%></B></TD>
    </TR>
    <TR>
         <TD COLSPAN = 2>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='<%=com.track.constants.PageConstant.CancelButton%>'"/>&nbsp;&nbsp;
              <input  type="button" value="LocationTransfer" name="SubmitButton" onClick="return validatePO(document.form)"/>
              <INPUT class="Submit" type="BUTTON" value="Clear" onClick="return onClear();">&nbsp;&nbsp;    
         </TD>
    </TR>
</TABLE>
</center>
<Table border=0 bgcolor="#dddddd">
    <TR  bgcolor="#dddddd"> <INPUT name="action" type="hidden">
      <INPUT     name="CHECKQTY"  type ="hidden" value="<%=QTY%>" size="1"   MAXLENGTH=80 ></TD>
      <INPUT     name="LOGINUSER"  type ="hidden" value="<%=sUserId %>" size="1"   MAXLENGTH=80 ></TD>
      <INPUT     name="INVQTY"  type ="hidden" value="<%=INVQTY%>" size="10"   MAXLENGTH=80 >
      <INPUT     name="AVAILQTY"  type ="hidden" value="" size="10"   MAXLENGTH=80 ></TD>
    </TR>
</Table>
</FORM>

</HTML>
<%@ include file="footer.jsp"%>
<script>
function validatePO(form)//form
{
  var frmRoot=document.form;
  
  if(frmRoot.FROM_LOC.value=="" || frmRoot.FROM_LOC.value.length==0 )
	 {
		alert("Please Enter FROMLOC!");
		frmRoot.FROM_LOC.focus();
		return false;
   }
   if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Please Enter PRODUCTID!");
		frmRoot.ITEMNO.focus();
		return false;
   }
 else if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter BATCH!");
		frmRoot.BATCH.focus();
		return false;
   }
 else if(removeCommas(frmRoot.QTY.value)=="" || removeCommas(frmRoot.QTY.value).length==0 )
 {
		alert("Please Enter Qty");
		frmRoot.QTY.focus();
					
		return false;
 }
 else if(isNaN(removeCommas(frmRoot.QTY.value))) {alert("Please enter valid QTY.");frmRoot.QTY.focus(); return false;}

 else if(removeCommas(frmRoot.QTY.value) < 0  )
 {
   alert("Qty Should not be 0 or less than 0");
	 frmRoot.QTY.focus();
	 return false;
 }
 else if(frmRoot.TOLOC.value=="" || frmRoot.TOLOC.value.length==0 )
 {
		alert("Please Enter TOLOC!");
		frmRoot.TOLOC.focus();
					
		return false;
 }
 else if(frmRoot.TOLOC.value==frmRoot.FROM_LOC.value)
 {
	   alert("Please choose TO LOC different from FROM LOC"); 
	   return false;
  }

/* else if(parseInt(qty) > parseInt(invqty))
 {
	   alert("Transfer quantity Should <= InvQty"); 
	   return false;
  }*/
 else
 {
 document.form.action.value="/track/LocationTransferServlet?action=LocationTransfer";
	  document.form.submit();
    return true;
 }

}
function validateLocation(locId) {
	
	if(locId=="" || locId.length==0 ) {
		alert("Enter From Location!");
		document.form.FROM_LOC.focus();
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
						alert("Not a valid From Location");
						document.form.FROM_LOC.value = "";
						document.form.FROM_LOC.focus();
					}
				}
			});
		}
	}
        function validateTOLocation(locId) {
	
	if(locId=="" || locId.length==0 ) {
		alert("Enter To Location!");
		document.form.FROM_LOC.focus();
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
                                document.form.action.value="/track/LocationTransferServlet?action=LocationTransfer";
	                        document.form.submit();
					} else {
						alert("Not a valid To Location");
						document.form.TOLOC.value = "";
						document.form.TOLOC.focus();
					}
				}
			});
		}
	}
  function validateProduct() {
	var productId = document.form.ITEMNO.value;
        var loc =document.form.FROM_LOC.value;
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
				PLANT : "<%=PLANT%>",FROMLOC:loc,
				ACTION : "VALIDATE_PRODUCT_BYLOC"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						document.form.ITEMDESC.value = resultVal.discription;
						document.form.ITEMNO.value=resultVal.item;
                                               // document.form.QTY.value = resultVal.qty;
                                                 //document.form.INVQTY.value = resultVal.qty;
                                                 document.form.UOM.value = resultVal.uom;
						document.form.BATCH.value = "NOBATCH";
						document.form.BATCH.focus();

					} else {
						alert("Not a valid product for From Loc");
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
		var locId = document.form.FROM_LOC.value;
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
        
		var qty = document.getElementById("QTY").value;
		removeCommas(qty);
		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.form.QTY.focus();
		} else {
                 
			if (isNumericInput(qty) == false) {
				alert("Entered Quantity is not a valid Qty!");
			} else {
                       
				var availableQty = document.getElementById("INVQTY").value;
				removeCommas(availableQty);
				availableQty = availableQty*1;
                             
				//alert(qty);
				if(qty>availableQty){
					alert("Entered Quantity is greater than the Available Qty!");
					document.form.QTY.value = "";
					document.form.QTY.focus();
				}else{
					document.form.TOLOC.value = "";
					document.form.TOLOC.focus();
				}
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