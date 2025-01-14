<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Pick/Transfer By OutBound Order</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">


  var subWin = null;
  function popUpWin(URL) 
  {
    subWin = window.open(URL, 'TransferOrderPicking', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
 
      
  function validatePO(form)
  {
    var frmRoot=document.form;
    if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 )
	 {
		alert("Please Enter OutBound Transfer Order No!");
		frmRoot.ORDERNO.focus();
		return false;
     }
    else  if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Please Enter Product ID!");
		frmRoot.ITEMNO.focus();
		return false;
         }
    
    else if(frmRoot.FROMLOC.value=="" || frmRoot.FROMLOC.value.length==0 )
	 {
		alert("Please Enter FROM LOC");
		frmRoot.FROMLOC.focus();
		return false;
         }
   else if(frmRoot.TOLOC.value=="" || frmRoot.TOLOC.value.length==0 )
	 {
		alert("Please Enter TO LOC");
		frmRoot.TOLOC.focus();
		return false;
         }
   else if(frmRoot.FROMLOC.value==frmRoot.TOLOC.value )
	 {
		alert("Please choose TO LOC different from FROM LOC");
		frmRoot.TOLOC.focus();
		return false;
  }
   else if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter BATCH!");
		frmRoot.BATCH.focus();
		return false;
         }
  else if(isNaN(document.form.PICKINGQTY.value)) {alert("Please enter valid QTY.");document.form.PICKINGQTY.focus(); return false;}
  
  else if(document.form.PICKINGQTY.value == 0)
      {
         alert("Qty Should not be 0 or less than 0");
         frmRoot.PICKINGQTY.focus();
         return false;
      }
    
  else if(document.form.QTY.value=="" || document.form.QTY.value.length==0)
    {
		alert("Press Enter After Scan Batch");
		frmRoot.BATCH.focus();
		return false;
         }
    else if(document.form.QTY.value.length>0)
    {
    	
    var pickQty = document.form.PICKINGQTY.value;
	var availQty = document.form.QTY.value;
			
	   var pkqty = parseInt(removeCommas(pickQty),10);
	   var avqty = parseInt(removeCommas(availQty),10);
	  
	   if(pkqty>avqty)
	   {
		   alert("Entered Quantity is greater than Available Qty");
		   document.form.PICKINGQTY.focus();
		   return false;
	   }
	   else
	   {
	      return true;
	   }
	   
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
  	document.form.FROMLOC.value="";
  	document.form.TOLOC.value="";
  	document.form.TEMPLOC.value="";
  	document.form.BATCH.value="";
  	document.form.REF.value="";
  	document.form.ORDERQTY.value="";
  	document.form.QTY.value="";
  	document.form.PICKINGQTY.value="";
  	document.form.CONTACTNAME.value="";
  	document.form.TELNO.value="";
  	document.form.EMAIL.value="";
  	document.form.ADD1.value="";
  	document.form.ADD2.value="";
  	document.form.ADD3.value="";
   	return true;
}
  
</script>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
  <%
       StrUtils strUtils=new StrUtils();
 	   DoHdrDAO _DoHdrDAO = new DoHdrDAO();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String plant=(String)session.getAttribute("PLANT");
       String  fieldDesc="";
       String   ORDERNO    = "",ITEMNO   = "", ITEMDESC  = "",
       FROMLOC   = "" ,TOLOC="",TEMPLOC="", CHECKQTY="",BATCH  = "",UOM="", REF   = "",ORDERLNO,
       QTY = "",RECEIVEQTY="",CUSTNAME="",ORDERQTY="",INVQTY="",ISSUEDQTY="",PICKEDQTY="",PICKINGQTY="",
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="";
       ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=strUtils.fString(request.getParameter("ORDERLNO"));
       CUSTNAME=strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUSTNAME")));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")));
       FROMLOC = strUtils.fString(request.getParameter("FROMLOC"));
       TOLOC = strUtils.fString(request.getParameter("TOLOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       ISSUEDQTY=strUtils.fString(request.getParameter("ISSUEDQTY"));
       PICKEDQTY = strUtils.fString(request.getParameter("PICKEDQTY"));
       PICKINGQTY = strUtils.fString(request.getParameter("PICKINGQTY"));
       INVQTY = strUtils.fString(request.getParameter("QTY"));
       REF = strUtils.fString(request.getParameter("REF"));
       ORDERQTY = strUtils.fString(request.getParameter("ORDERQTY"));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       ItemMstDAO itemmstdao = new ItemMstDAO();
       UOM = itemmstdao.getItemUOM(plant,ITEMNO);
       
      // ArrayList list=(ArrayList)session.getAttribute("assigneelistqry1");
      
      ArrayList list = _DoHdrDAO.getOutBoundOrderCustamerDetailsByWMS(plant,ORDERNO);
       
       for(int i=0;i<list.size();i++)
        {
          Map m = (Map)list.get(i);
           CUSTNAME=(String)m.get("custname");
           CONTACTNAME = (String)m.get("contactname");
           TELNO=(String)m.get("telno");
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
         fieldDesc=(String)request.getSession().getAttribute("QTYERROR1");
         fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
         ArrayList list1 = _DoHdrDAO.getOutBoundOrderCustamerDetailsByWMS(plant,ORDERNO);
         
         for(int i=0;i<list1.size();i++)
          {
              Map m1 = (Map)list1.get(i);
             CUSTNAME=(String)m1.get("custname");
             CONTACTNAME = (String)m1.get("contactname");
             TELNO=(String)m1.get("telno");
             EMAIL=(String)m1.get("email");
             ADD1=(String)m1.get("add1");
             ADD2=(String)m1.get("add2");
             ADD3=(String)m1.get("add3");
          
        }
    }

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="/track/DOTransferServlet?"  >
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Pick/Transfer By Transfer Order</FONT>&nbsp;
      </TH>
     </TR>
  </TABLE>
  <br>
<INPUT type = "hidden" name="PLANT" value = "<%=plant%>">
  <font face="Times New Roman" size="4" >
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
 <%=fieldDesc%>
  </font>
  </table>
  <TABLE border="0" CELLSPACING=1 WIDTH="100%" bgcolor="#dddddd">
    <TR>
      <TH WIDTH="13%" ALIGN="RIGHT">Order No</TH>
      <TD width="42%">
        <INPUT name="ORDERNO" type="TEXT" value="<%=ORDERNO%>" size="35"  class="inactivegry" readonly MAXLENGTH="80"/>
      </TD>
       <TH WIDTH="8%"  > </TH>
        <TD width="37%"></TD>
    </TR>
    <TR>
         <TH WIDTH="13%" ALIGN="Right" >Customer Name : </TH>
         <TD width="42%"><INPUT name="CUSTNAME" class="inactivegry" type = "TEXT" value="<%=su.forHTMLTag(CUSTNAME)%>" size="35"  MAXLENGTH=80 readonly></TD>
         <TD width="8%"></TD>
          <TH WIDTH="37%" ALIGN="left" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Customer&nbsp; Details </TH>
    </TR>

    <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >Product ID:</TH>
         <TD width="42%">
         <INPUT name="ITEMNO" type="TEXT" value="<%=ITEMNO%>" size="35"  class="inactivegry" readonly MAXLENGTH="80" />
        </TD>
         <TH WIDTH="8%" ALIGN="Right" >Contact Name: </TH>
         <TD width="37%"><INPUT name="CONTACTNAME" class="inactivegry" value="<%=CONTACTNAME%>"  type = "TEXT"  size="35"  MAXLENGTH=80 readonly></TD>
      
    </TR>
     <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >Product Desc &nbsp;:</TH>
         <TD width="42%">
           <INPUT name="ITEMDESC" type="TEXT" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="35" class="inactivegry" readonly MAXLENGTH="80"/>
         </TD>
          <TH WIDTH="8%" ALIGN="Right" >Telephone : </TH>
         <TD width="37%"><INPUT name="TELNO" class="inactivegry"  value="<%=TELNO%>"  type = "TEXT"  size="35"  MAXLENGTH=80 readonly></TD>
    </TR>
     <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >Order Qty:</TH>
         <TD WIDTH="42%">
           <INPUT name="ORDERQTY" type="TEXT" value="<%=StrUtils.formatNum(ORDERQTY)%>" size="35" MAXLENGTH="80" class="inactivegry" readonly/>
           &nbsp;<INPUT name="UOM" type="TEXT" value="<%=UOM%>" size="10" MAXLENGTH="10" class="inactivegry" readonly/>
         </TD>
         <TH WIDTH="8%" ALIGN="Right" >Email : </TH>
         <TD width="37%"><INPUT name="EMAIL" value="<%=EMAIL%>" class="inactivegry" type = "TEXT"  size="35"  MAXLENGTH=80 readonly></TD>
    </TR>
     <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >Picked Qty:</TH>
         <TD width="42%">
           <INPUT name="PICKEDQTY" type="TEXT" class="inactivegry" readonly value="<%=StrUtils.formatNum(PICKEDQTY)%>" size="35" MAXLENGTH="80"/>
           </TD>
         <TH WIDTH="8%" ALIGN="Right" >Unit&nbsp;No : </TH>
         <TD width="37%"><INPUT name="ADD1"  class="inactivegry" value="<%=ADD1%>"  type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD> 
    </TR>
     <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >From Loc:</TH>
         <TD width="42%">
           <INPUT name="FROMLOC" type="TEXT" value="<%=FROMLOC%>" size="35"   MAXLENGTH="80" onkeypress="if((event.keyCode=='13') && ( document.form.FROMLOC.value.length > 0)){validateLocation();}" />
          <a href="#" onClick="javascript:popUpWin('loc_list_dotransferfrom.jsp?FROMLOC='+form.FROMLOC.value);"><img src="images/populate.gif" border="0"></a></TD>
      <TH WIDTH="8%" ALIGN="Right" >Building : </TH>
         <TD width="37%"><INPUT name="ADD2"  class="inactivegry" value="<%=ADD2%>"  type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD> 
      </TR>
     <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >To Loc:</TH>
         <TD width="42%">
          <INPUT name="TOLOC" type="TEXT" value="<%=TOLOC%>" size="35"   MAXLENGTH="80" onkeypress="if((event.keyCode=='13') && ( document.form.TOLOC.value.length > 0)){validateToLocation();}" />
         <a href="#" onClick="javascript:popUpWin('loc_list_dotransferto.jsp?TOLOC='+form.TOLOC.value);"><img src="images/populate.gif" border="0"></TD>
         <TH WIDTH="8%" ALIGN="Right" >Street : </TH>
         <TD width="37%"><INPUT name="ADD3" value="<%=ADD3%>" class="inactivegry" type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD>
    </TR>
   <!--  <TR>
      <TH WIDTH="13%" ALIGN="Right">Temp&nbsp;Loc&nbsp; :</TH>
      <TD width="42%">
        <INPUT name="TEMPLOC" type="TEXT" value="TEMP_TO" size="35" class="inactivegry" readonly MAXLENGTH="80"/>
      </TD>
      <TH WIDTH="8%">&nbsp;</TH>
      <TD width="37%">&nbsp;</TD>
    </TR> -->
    <TR>
      <TH WIDTH="13%" ALIGN="Right">Batch&nbsp;:</TH>
      <TD width="42%">
        <INPUT name="BATCH" type="TEXT" value="<%=BATCH%>" size="35" MAXLENGTH="40" 
        onkeypress="if((event.keyCode=='13') && ( document.form.BATCH.value.length > 0)){validateBatch();}" />
        <a href="#" onClick="javascript:popUpWin('OutBoundPickingBatch.jsp?ITEMNO='+form.ITEMNO.value+'&LOC='+form.FROMLOC.value+'&BATCH='+form.BATCH.value);">
          <img src="images/populate.gif" border="0"/>
        </a>
      </TD>
      <TH WIDTH="8%">&nbsp;</TH>
      <TD width="37%">&nbsp;</TD>
    </TR>
     <TR>
      <TH WIDTH="13%" ALIGN="Right">Available Qty&nbsp;&nbsp;:</TH>
      <TD width="42%">
        <INPUT name="QTY" type="TEXT" class="inactivegry" readonly value="<%=QTY%>" size="35" MAXLENGTH="80"/>
      </TD>
      <TH WIDTH="8%">&nbsp;</TH>
      <TD width="37%">&nbsp;</TD>
    </TR>
    <TR>
      <TH WIDTH="13%" ALIGN="Right">Picking Qty:</TH>
      <TD width="42%">
        <INPUT name="PICKINGQTY" type="TEXT" value="<%=PICKINGQTY%>" size="35" MAXLENGTH="80" onkeypress="if((event.keyCode=='13') && ( document.form.PICKINGQTY.value.length > 0)){validateQuantity();}" />
      </TD>
      <TH WIDTH="8%">&nbsp;</TH>
      <TD width="37%">&nbsp;</TD>
    </TR>
   <TR>
		<TH WIDTH="13%" ALIGN="Right">Remarks&nbsp;&nbsp;:</TH>
		<TD width="42%"><INPUT name="REF" type="TEXT" value="<%=REF%>"
			size="35" MAXLENGTH="80"
			onkeypress="if((event.keyCode=='13') ){document.form.actionSubmit.focus();}" />
		</TD>
		<TH WIDTH="8%">&nbsp;</TH>
		<TD width="37%">&nbsp;</TD>
	</TR>
    <TR>
      <TH WIDTH="13%" ALIGN="Right">&nbsp;</TH>
      <TD width="42%">&nbsp;
      </TD>
      <TH WIDTH="8%">&nbsp;</TH>
      <TD width="37%">&nbsp;</TD>
    </TR>
    
    <TR align="center">
          <TH WIDTH="8%" > </TH>
          <TD WIDTH="35%" align="center" COLSPAN = 2>
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='DOTransferSummary.jsp?action=View&DONO='+form.ORDERNO.value+'&PLANT='+form.PLANT.value "/>&nbsp;&nbsp;
             <input type="button" value="Pick/Transfer Confirm" name="actionSubmit" 
             onClick="if(validatePO(document.form)) {submitForm();}" /></TD>
            
            
          </TD>
         <TH WIDTH="37%" > </TH>
         <TD width="35%"></TD>
    </TR>
 </TABLE>
</center>
<Table  border=0 bgcolor="#dddddd">
 <TR  bgcolor="#dddddd">
  <INPUT     name="ORDERLNO"  type ="hidden" value="<%=ORDERLNO%>" size="1"   MAXLENGTH=80 ></TD>
  <INPUT     name="LOGIN_USER"  type ="hidden" value="<%=sUserId %>" size="1"   MAXLENGTH=80 ></TD>
  <INPUT     name="CHECKQTY"  type ="hidden" value="<%=QTY%>" size="1"   MAXLENGTH=80 ></TD>
  <INPUT     name="INVQTY"  type ="hidden" value="<%=INVQTY%>" size="1"   MAXLENGTH=80 ></TD>
    <INPUT     name="ISSUEDQTY"  type ="hidden" value="<%=ISSUEDQTY%>" size="1"   MAXLENGTH=80 ></TD>
   <INPUT    type="Hidden" name="BFLAG" value="1">
  <INPUT     name="ITEMNO_0"  type ="hidden" value="" size="1"   MAXLENGTH=80 > 
  </TR>
</Table>
</FORM>

</HTML>
<script>
function submitForm(){
	document.form.action="/track/DOTransferServlet?action=Pick/Transfer Confirm";
    document.form.submit();
}
function validateLocation() {
	var locId = document.form.FROMLOC.value;
	var itemNo = document.form.ITEMNO.value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.form.FROMLOC.focus();
	}else{
		var urlStr = "/track/OutboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
				ITEMNO : itemNo,
                USERID : "<%=sUserId%>",
				PLANT : "<%=plant%>",
				ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						//document.form.BATCH.value = "";
						document.form.TOLOC.focus();
					} else {
						alert("Not a valid Location");
						document.form.FROMLOC.value = "";
						document.form.FROMLOC.focus();
					}
				}
			});
		}
	}
        
        function validateToLocation() {
	var locId = document.form.TOLOC.value;
	var itemNo = document.form.ITEMNO.value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.form.TOLOC.focus();
	}else{
		var urlStr = "/track/OutboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
				ITEMNO : itemNo,
                USERID : "<%=sUserId%>",
				PLANT : "<%=plant%>",
				ACTION : "VALIDATE_TO_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						//document.form.BATCH.value = "";
						document.form.BATCH.focus();
					} else {
						alert("Not a valid Location");
						document.form.TOLOC.value = "";
						document.form.TOLOC.focus();
					}
				}
			});
		}
	}
        function validateBatch() {
	var locId = document.form.FROMLOC.value;
	var itemNo = document.form.ITEMNO.value;
	var batch = document.form.BATCH.value;
	if(batch=="" || batch.length==0 ) {
		alert("Enter Batch!");
		document.form.BATCH.focus();
	}else{
		var urlStr = "/track/OutboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
				ITEMNO : itemNo,
				BATCH : batch,
                USERID : "<%=sUserId%>",
				PLANT : "<%=plant%>",
				ACTION : "VALIDATE_BATCH"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						document.form.BATCH.value = resultVal.BATCH;
						document.form.QTY.value=resultVal.QTY;
						document.form.PICKINGQTY.focus();
					} else {
						alert("Not a valid Batch");
						document.form.BATCH.value = "";
						document.form.QTY.value="";
						document.form.BATCH.focus();
					}
				}
			});
		}
	}

function validateQuantity() {
	var qty = document.form.PICKINGQTY.value;
	if (qty == "" || qty.length == 0) {
		alert("Enter Quantity!");
		document.form.PICKINGQTY.focus();
	} else {
		if (isNumericInput(qty) == false) {
			alert("Entered Quantity is not a valid Qty!");
		} else {
			var availableQty = document.form.QTY.value;
			availableQty = availableQty*1;
			//alert(qty);
			if(availableQty<qty){
				alert("Entered Quantity is greater than the Available Qty!");
				document.form.PICKINGQTY.value = "";
				document.form.PICKINGQTY.focus();
			}else{
				document.form.REF.value = "";
				document.form.REF.focus();
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
document.form.FROMLOC.focus();
</script>
<%@ include file="footer.jsp"%>
