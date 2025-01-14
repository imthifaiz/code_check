<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Multiple Misc Order Receipt</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'InboundOrderList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }

function validatePO(form,isserial)//form
{
 
   var frmRoot=document.form;
   var table = document.getElementById("MULTI_MISC_RECEIVING");
   var rowCount = table.rows.length;
   var checkcnt = rowCount-1;
   document.form.DYNAMIC_RECEIVING_SIZE.value = rowCount;
   processSelection();
   for(var i=0;i<rowCount;i++){
	   var loc= document.getElementById("LOC"+"_"+i);
	   var product = document.getElementById("ITEMNO"+"_"+i);
	   var batch = document.getElementById("BATCH"+"_"+i);
	   var qty= document.getElementById("QTY"+"_"+i);
	   var reasoncode = document.getElementById("REASONCODE"+"_"+i);
if(document.form.SERIAL_SELECTION.checked){
for(var j=0;j<rowCount;j++){
 if(i!=j){
 var chkbatch = document.getElementById("BATCH"+"_"+j);
if(batch.value==chkbatch.value){
alert("Duplicate batch Scanned !")
chkbatch.select();
chkbatch.focus();
return false;
}
}
}
}

   if(loc.value=="" || loc.value.length==0 )
	 {
		alert("Enter Location!");
		loc.style.backgroundColor = "#FFE5EC";
		loc.focus();
		return false;
 }
   else if(product.value=="" || product.value.length==0 )
	 {
		alert("Enter Product ID!");
		product.style.backgroundColor = "#FFE5EC";
		product.focus();
		return false;
   }
   
   else   if(batch.value=="" || batch.value.length==0 )
	 {
		alert("Enter BATCH!");
		batch.style.backgroundColor = "#FFE5EC";
		batch.focus();
                if(document.form.SERIAL_SELECTION.checked){
		batch.value="";
                }else{
                batch.value="NOBATCH";
                }
		return false;
   }
   
   else   if(qty.value=="" || qty.value.length==0)
	 {
		alert("Enter QTY!");
		qty.style.backgroundColor = "#FFE5EC";
		qty.focus();
		return false;
   }
   else if(isNaN(qty.value)) {alert("Enter valid QTY.");qty.focus(); return false;}
   else   if(reasoncode.value=="" || reasoncode.value.length==0 )
	 {
		alert("Enter REASONCODE!");
		reasoncode.focus();
		return false;
   }
   
   else
    {
	    if(i==checkcnt)
	    {	   
	   document.form.action.value="MultipleMiscOrderReceiving";
	   document.form.submit();
	   return true;}
	   
    }
   }
}



function onClear(){
 
  // document.form.action  = "MiscOrderReceiving.jsp?action=CLEAR";
  // document.form.submit();
  document.form.ITEMNO_0.value="";
  document.form.ITEMDESC_0.value="";
  document.form.LOC_0.value="";
  document.form.BATCH_0.value="";
  document.form.QTY_0.value="";
  document.form.REASONCODE_0.value="";
  document.form.REFDET_0.value="";
  document.form.EXPIREDATE_0.value="";
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
       QTY = "",REFDET="",REASONCODE="",EXPIREDATE="",UOM="",TRANSACTIONDATE="";
       
     
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC =strUtils.replaceCharacters2Recv( strUtils.fString(request.getParameter("ITEMDESC")));
       LOC = strUtils.fString(request.getParameter("LOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       REFDET = strUtils.fString(request.getParameter("REFDET"));
       REASONCODE = strUtils.fString(request.getParameter("REASONCODE"));
       EXPIREDATE = strUtils.fString(request.getParameter("EXPIREDATE"));
	    TRANSACTIONDATE = strUtils.fString(request.getParameter("TRANSACTIONDATE"));
       DateUtils _dateUtils = new DateUtils();
       String curDate =_dateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
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
	action="/track/MiscOrderReceivingServlet?action=MultipleMiscOrderReceiving"><br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Multiple Misc
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
		<TD colspan="4" ><br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<left><b>Always use the same for the following :</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="Checkbox" name="SERIAL_SELECTION" id="SERIAL_SELECTION" value="SERIAL_SELECTION" onClick="processSerialSelection();"></input>
		 Serial Receiving
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_LOCATION" id="ALWAYS_SAME_LOCATION" value="ALWAYS_SAME_LOCATION" onClick="processSameLocation();"></input> Location
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_PRODUCT" id="ALWAYS_SAME_PRODUCT" value="ALWAYS_SAME_PRODUCT" onClick="processSameProduct();"></input> Product
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_REMARKS" id="ALWAYS_SAME_REMARKS" value="ALWAYS_SAME_REMARKS" onClick="processSameRemarks();"></input>Remarks
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_EXPIRY" id="ALWAYS_SAME_EXPIRY" value="ALWAYS_SAME_EXPIRY" onClick="processSameExpiry();"></input>Expiry Date
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_REASONCODE" id="ALWAYS_SAME_REASONCODE" value="ALWAYS_SAME_REASONCODE" onClick="processSameReasonCode();"></input>Reason Code</left>
		<br>
<br>
		<table align="center" width="100%" border="0" >
		<tr>
		<td width="11%">
		<b></b>
		</td>
		<td width="11.5%">
		<b></b>
		</td>
		<td width="12%">
		<b></b>
		</td>
		<td width="20%">
		<b></b>
		</td>
		<td width="12%">
		<b></b>
		</td>
		<td width="10%">
		<b></b>
		</td>
		<td width="10%">
		<b></b>
		</td><td width="17%">
		<b></b>
		</td>
		</tr>
		<tr>
		<td width="11%">
		<b>Loc:</b>
		</td>
		<td width="11.5%">
		<b>Product ID:</b>
		</td>
		<td width="12%">
		<b>Product Desc:</b>
		</td>
		<td width="20%">
		<b>Batch:</b>
		</td>
		<td width="12%">
		<b>Qty:</b>
		</td>
		<td width="10%">
		<b>Remarks:</b>
		</td>
		<td width="13%">
		<b>Expiry Date:</b>
		</td>
		<td width="17%" align="left">
		<b>Reason Code:</b>
		</td>
		</tr>
			</table>
		<table align="center" width="100%" border="0" id="MULTI_MISC_RECEIVING">
		
			<tr>
				<td width="11%"><INPUT name="LOC_0" id="LOC_0" type="TEXT"
					value="<%=LOC%>" size="15"
					onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(0);}"
					MAXLENGTH=80> <a href="#"
					onClick="javascript:popUpWin('loc_list_MultiReceivewms.jsp?LOC='+form.LOC_0.value+'&INDEX=0');"><img
					src="images/populate.gif" border="0"></a></td>
				
				<td width="11.5%">
				<INPUT name="ITEMNO_0" type="TEXT" id="ITEMNO_0" value="<%=ITEMNO%>"
			size="15" MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.ITEMNO_0.value.length > 0)){validateProduct(0);}">
		<a href="#" onClick="javascript:popUpWin('list/multimiscreceive_item_list.jsp?ITEMNO='+form.ITEMNO_0.value+'&INDEX=0');"> <img src="images/populate.gif" border="0" /> </a>
				</td>
				<td width="12%">
			<INPUT name="ITEMDESC_0"  type="TEXT" id="ITEMDESC_0"
			value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="15" MAXLENGTH=100 >
                        <a href="#" onClick="javascript:popUpWin('list/multimiscreceive_item_list.jsp?DESC='+form.ITEMDESC_0.value+'&INDEX=0');"> <img src="images/populate.gif" border="0" /> </a>	
				</td>
				<td width="20%"><INPUT name="BATCH_0" id="BATCH_0" type="TEXT"
					value="<%=BATCH%>" size="15" 
					MAXLENGTH=40 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch();}"> <input type="button"
					onclick="generateBatch(0);" size="7" value="Generate Batch"
					name="actionBatch" /></td>
					<td width="12%"><INPUT name="QTY_0" type="TEXT" id="QTY_0" value="<%=QTY%>" size="4"
			MAXLENGTH=50
			onkeypress="if((event.keyCode=='13') && ( document.form.QTY_0.value.length > 0)){validateQuantity(0);}">
			<INPUT name="UOM_0" id="UOM_0" type="TEXT" value="" size="10"
			MAXLENGTH=10
			></TD>
			<td width="10%">
			<INPUT name="REFDET_0" type="TEXT" id="REFDET_0" value="<%=REFDET%>"
			size="17" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.EXPIREDATE_0.focus();}"> 
			</td>
			<td width="13%">
			<INPUT name="EXPIREDATE_0" type="TEXT"  id="EXPIREDATE_0" value="<%=EXPIREDATE%>" readonly="readonly"
			size="15" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.REASONCODE_0.focus();}"> 
			<a href="javascript:show_calendar('form.EXPIREDATE_0');"
			   onmouseover="window.status='Date Picker';return true;"
			   onmouseout="window.status='';return true;"> <img
			   src="images\show-calendar.gif" width="20" height="15" border="0" />
				</a>
			</TD>
			<td width="17%" valign="top">
			<INPUT name="REASONCODE_0" type="TEXT"  id="REASONCODE_0" value="<% if(REASONCODE.length() > 0) { out.print(REASONCODE); }else{out.print("NOREASONCODE");}%>"
			size="15"  MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.REASONCODE_0.value.length > 0)){ document.form.SubmitButton.focus();}"> <a href="#"
			onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form.ITEMNO_0.value+'&INDEX=0');">
			<img src="images/populate.gif" border="0" /> </a>
			</td>
			</tr>

		</table>
		<br></br>
		<table align="center" width="85%" border="0">
			<tr>
				<td><INPUT type="button" value="ADD NEW RECEIVING" onclick="addRow();" /> <INPUT type="button"
					value="REMOVE LAST ADDED RECEIVING"
					onclick="deleteRow('MULTI_MISC_RECEIVING');" /> <INPUT type="hidden"
					name="DYNAMIC_RECEIVING_SIZE">
					&nbsp;&nbsp;<b>Transaction Date : </b><INPUT name="TRANSACTIONDATE" id="TRANSACTIONDATE"
					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" MAXLENGTH="80" readonly="readonly">
				<a href="javascript:show_calendar('form.TRANSACTIONDATE');"
			   onmouseover="window.status='Date Picker';return true;"
			   onmouseout="window.status='';return true;"> <img
			   src="images\show-calendar.gif" width="24" height="22" border="0" /></a></td>
			</tr>
		</table>
		</TD>
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
function processSelection()
{
	if(document.form.SERIAL_SELECTION.checked){
		processSerialSelection();
	}
	if(document.form.ALWAYS_SAME_LOCATION.checked){
		processSameLocation();	
	}
        if(document.form.ALWAYS_SAME_PRODUCT.checked){
		processSameProduct();	
	}
        if(document.form.ALWAYS_SAME_REMARKS.checked){
		processSameRemarks();	
	}

        if(document.form.ALWAYS_SAME_EXPIRY.checked){
		processSameExpiry();	
	}
        if(document.form.ALWAYS_SAME_REASONCODE.checked){
		processSameReasonCode();	
	}
	
}
function processSerialSelection() {
	var table = document.getElementById("MULTI_MISC_RECEIVING");
	var rowCount = table.rows.length;
	for(var index = 0; index<rowCount; index++) {
		var pickQty = document.getElementById("QTY_"+index);
		if(document.form.SERIAL_SELECTION.checked==true){
		pickQty.value = 1;
		pickQty.readOnly=true;}
		else
		{
			pickQty.value = "";
			pickQty.readOnly=false;
		}
			
	}
}

function processSameLocation(){
	var table = document.getElementById("MULTI_MISC_RECEIVING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("LOC_0").value;
	for(var index = 1; index<rowCount; index++) {
		var locID = document.getElementById("LOC_"+index);
		if(document.form.ALWAYS_SAME_LOCATION.checked){
			locID.value = defaultValue;}
		else
		{
			locID.value="";
			locID.readOnly = false;
		}
		
		
	}
}
function processSameProduct(){
	var table = document.getElementById("MULTI_MISC_RECEIVING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("ITEMNO_0").value;
        var descValue = document.getElementById("ITEMDESC_0").value;
	for(var index = 1; index<rowCount; index++) {
		var productID = document.getElementById("ITEMNO_"+index);
                var productDesc = document.getElementById("ITEMDESC_"+index);
		if(document.form.ALWAYS_SAME_PRODUCT.checked){
			productID.value = defaultValue;
                        productDesc.value = descValue;
                }
		else
		{
			productID.value="";
                        productDesc.value="";
			productID.readOnly = false;
                        productDesc.readOnly = false;
		}
		
		
	}
}
function processSameExpiry(){
	var table = document.getElementById("MULTI_MISC_RECEIVING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("EXPIREDATE_0").value;
	for(var index = 1; index<rowCount; index++) {
		var expiryDt = document.getElementById("EXPIREDATE_"+index);
		if(document.form.ALWAYS_SAME_EXPIRY.checked){
			expiryDt.value = defaultValue;}
		else
		{
			expiryDt.value="";
			expiryDt.readOnly = false;
		}
		
		
	}
}
function processSameRemarks(){
	var table = document.getElementById("MULTI_MISC_RECEIVING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("REFDET_0").value;
	for(var index = 1; index<rowCount; index++) {
		var remarks = document.getElementById("REFDET_"+index);
		if(document.form.ALWAYS_SAME_REMARKS.checked){
			remarks.value = defaultValue;}
		else
		{
			remarks.value="";
			remarks.readOnly = false;
		}
		
		
	}
}
function processSameReasonCode(){
	var table = document.getElementById("MULTI_MISC_RECEIVING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("REASONCODE_0").value;
	
	for(var index = 1; index<rowCount; index++) {
		var reasonCode = document.getElementById("REASONCODE_"+index);
		if(document.form.ALWAYS_SAME_REASONCODE.checked){
			reasonCode.value = defaultValue;}
		else
		{
			reasonCode.value="";
			reasonCode.readOnly = false;
		}
		
		
	}
}



function addRow() {
	var table = document.getElementById("MULTI_MISC_RECEIVING");
	var rowCount = table.rows.length;
	
	var serialselection = false;
	var sameLocaionUse = false;
        var sameProductUse =false;
        var sameRemarksUse =false;
        var sameExpiryUse =false;
        var sameReasonCodeUse = false;
        var allchecked=false;
	if(document.form.SERIAL_SELECTION.checked){
		serialselection = true;
	}
	if(document.form.ALWAYS_SAME_LOCATION.checked){
		sameLocaionUse = true;
	}
        if(document.form.ALWAYS_SAME_PRODUCT.checked){
		sameProductUse = true;
              
	}
        if(document.form.ALWAYS_SAME_EXPIRY.checked){
		sameExpiryUse = true;
	}
 if(document.form.ALWAYS_SAME_REMARKS.checked){
		sameRemarksUse = true;
	}
        if(document.form.ALWAYS_SAME_REASONCODE.checked){
		sameReasonCodeUse = true;
	}
  if(serialselection && sameProductUse && sameRemarksUse && sameExpiryUse && sameReasonCodeUse ){
   allchecked=true;
  }
	var row = table.insertRow(rowCount);
	var firstElementLocationValue = document.getElementById("LOC_0").value;
        var firstElementProductValue = document.getElementById("ITEMNO_0").value;
        var firstElementProductDescValue = document.getElementById("ITEMDESC_0").value;
        var firstElementUomValue = document.getElementById("UOM_0").value;
        var firstElementremarksValue = document.getElementById("REFDET_0").value;
        var firstElementExpiryValue = document.getElementById("EXPIREDATE_0").value;
        var firstElementReasoncodeValue = document.getElementById("REASONCODE_0").value;

   
	
	var locationCell = row.insertCell(0);
		var locationCellText =  "<INPUT name=\"LOC_"+rowCount+"\" ";
		if(sameLocaionUse){
			locationCellText = locationCellText+ "value=\""+firstElementLocationValue+"\" readonly ";
		}
		locationCellText = locationCellText+ " id=\"LOC_"+rowCount+"\" type = \"TEXT\" size=\"15\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation("+rowCount+");}\" MAXLENGTH=\"80\">";
		//if(!sameLocaionUse) {
			locationCellText = locationCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('loc_list_MultiReceivewms.jsp?LOC='+form.LOC"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
		//}
	locationCell.innerHTML = locationCellText;
	
	var itemCell = row.insertCell(1);

	var itemCellText =  "<INPUT name=\"ITEMNO_"+rowCount+"\" ";
	if(sameProductUse){
			itemCellText = itemCellText+ "value=\""+firstElementProductValue+"\" readonly ";
	}
	itemCellText = itemCellText+ " id=\"ITEMNO_"+rowCount+"\" type = \"TEXT\" size=\"15\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateProduct("+rowCount+");}\" MAXLENGTH=\"80\">";
	
	itemCellText = itemCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('list/multimiscreceive_item_list.jsp?ITEMNO='+form.ITEMNO"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	
	
	itemCell.innerHTML = itemCellText;

	var itemdescCell = row.insertCell(2);
		
	var itemdescCellText =  "<INPUT name=\"ITEMDESC_"+rowCount+"\" ";
	if(sameProductUse){
			itemdescCellText = itemdescCellText+ "value=\""+firstElementProductDescValue+"\" readonly ";
	}
	itemdescCellText = itemdescCellText+ " id=\"ITEMDESC_"+rowCount+"\" type = \"TEXT\" size=\"15\"   MAXLENGTH=\"80\">";
	
	itemdescCellText = itemdescCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('list/multimiscreceive_item_list.jsp?DESC='+form.ITEMDESC"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	

	itemdescCell.innerHTML = itemdescCellText;
	
	var batchCell = row.insertCell(3);
var Batchtext = "<INPUT name=\"BATCH_"+rowCount+"\" id=\"BATCH_"+rowCount+"\"  type = \"TEXT\" size=\"15\"";
if(allchecked){
Batchtext= Batchtext+ " onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)  ){addRow();}\"  ";
}else{
Batchtext= Batchtext+ " onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)  ){document.getElementById(\'QTY_"+rowCount+"').focus();}\"  ";
}
	 Batchtext= Batchtext+  " MAXLENGTH=\"40\" > &nbsp;<input type=\"button\" onclick=\"generateBatch("+rowCount+");\"   size=\"20\" value=\"Generate Batch\" name=\"actionBatch\" />";
	 batchCell.innerHTML =Batchtext
	 var receivingQtyCell = row.insertCell(4);
	 var receiveQtyText = "<INPUT name=\"QTY_"+rowCount+"\" ";
		if(serialselection){
			receiveQtyText = receiveQtyText + " value=\"1\" readonly ";
		}

		receiveQtyText = receiveQtyText + " id=\"QTY_"+rowCount+"\" type = \"TEXT\"  size=\"4\"   MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){document.getElementById(\'REFDET_"+rowCount+"').focus();}\"  >";
		receiveQtyText = receiveQtyText + "	<INPUT name=\"UOM_"+rowCount+"\" ";
        if(sameProductUse){
			receiveQtyText = receiveQtyText+ "value=\""+firstElementUomValue+"\" readonly ";
	}
         receiveQtyText = receiveQtyText + " id=\"UOM_"+rowCount+"\" type = \"TEXT\" size=\"10\"  MAXLENGTH=\"10\" \>";
	receivingQtyCell.innerHTML = receiveQtyText;
		
	var remarksCell = row.insertCell(5);
	var remarkCellText ="<INPUT name=\"REFDET_"+rowCount+"\" ";
        if(sameRemarksUse){
			remarkCellText = remarkCellText+ "value=\""+firstElementremarksValue+"\" readonly ";
	}
	remarkCellText=remarkCellText+" type=\"TEXT\" id=\"REFDET_"+rowCount+"\" size=\"17\" MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){document.getElementById(\'EXPIREDATE_"+rowCount+"').focus();}\"> ";
	remarksCell.innerHTML=	remarkCellText;
	
	var expiredateCell = row.insertCell(6);
        var expiryDtCellText =  "<INPUT name=\"EXPIREDATE_"+rowCount+"\" ";
        if(sameExpiryUse){
			expiryDtCellText = expiryDtCellText+ "value=\""+firstElementExpiryValue+"\" readonly ";
	}
        expiryDtCellText = expiryDtCellText+ " id=\"EXPIREDATE_"+rowCount+"\" readonly=\"readonly\" type = \"TEXT\"  size=\"15\"  MAXLENGTH=\"80\" \> <a href=\"javascript:show_calendar('form.EXPIREDATE_"+rowCount+"');\" onmouseover=\"window.status='Date Picker';return true;\" onmouseout=\"window.status='';return true;\"> <img src=\"images/show-calendar.gif\" width=\"20\" height=\"15\" border=\"0\" /> </a>";
	
	expiredateCell.innerHTML = expiryDtCellText

	var reasonCell = row.insertCell(7);
	
	var reasonCellText =  "<INPUT name=\"REASONCODE_"+rowCount+"\" ";
	
        if(sameReasonCodeUse){
          
			reasonCellText = reasonCellText+ "value=\""+firstElementReasoncodeValue+"\" readonly ";
	}
        else
        {
        	reasonCellText = reasonCellText+ "value=\"NOREASONCODE\" readonly ";
        }
        reasonCellText = reasonCellText+ " id=\"REASONCODE_"+rowCount+"\" type = \"TEXT\" size=\"15\"   MAXLENGTH=\"80\">";
	
	reasonCellText = reasonCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form.ITEMNO"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	
	
	reasonCell.innerHTML = reasonCellText;

if(allchecked ){
document.getElementById("BATCH_"+rowCount).focus();
}else{
document.getElementById("LOC_"+rowCount).focus();
}

}

function deleteRow(tableID) {
	try {
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		rowCount = rowCount * 1 - 1;
		if (rowCount == 0) {
			alert("Can not remove the default Receiving");
		} else {
			table.deleteRow(rowCount);
		}
	} catch (e) {
		alert(e);
	}
}

function validateLocation(index) {
	var locId = document.getElementById("LOC"+"_"+index).value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC"+"_"+index).focus();
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
						document.getElementById("ITEMNO"+"_"+index).value = "";
						document.getElementById("ITEMNO"+"_"+index).focus();
					} else {
						alert("Not a valid Location");
						document.getElementById("LOC"+"_"+index).value = "";
						document.getElementById("LOC"+"_"+index).focus();
					}
				}
			});
		}
	}
function validateProduct(index) {
	var productId = document.getElementById("ITEMNO_"+index).value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product ID!");
		document.getElementById("ITEMNO_"+index).focus();
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
						document.getElementById("ITEMDESC_"+index).value = resultVal.discription;
						document.getElementById("UOM_"+index).value = resultVal.uom;
						/*if(resultVal.isNonStk=="Y"){
							alert("Not a valid product or It is a Non Stock product");
							document.getElementById("ITEMNO_"+index).value = "";
							document.getElementById("ITEMNO_"+index).focus();
						}else{*/
							document.getElementById("BATCH_"+index).value = "NOBATCH";
							document.getElementById("BATCH_"+index).select();
						
					
					} else {
						alert("Not a valid product or It is a parent product");
						document.getElementById("ITEMNO_"+index).value = "";
						document.getElementById("ITEMNO_"+index).focus();
					}
				}
			});
		}
	}

	function validateBatch() {
		var batch = document.form.BATCH_0.value;

		var serialselection = false;
		var sameLocaionUse = false;
	        var sameProductUse =false;
	        var sameRemarksUse =false;
	        var sameExpiryUse =false;
	        var sameReasonCodeUse = false;
	        var allchecked=false;
		if(document.form.SERIAL_SELECTION.checked){
			serialselection = true;
		}
		if(document.form.ALWAYS_SAME_LOCATION.checked){
			sameLocaionUse = true;
		}
	        if(document.form.ALWAYS_SAME_PRODUCT.checked){
			sameProductUse = true;
	              
		}
	        if(document.form.ALWAYS_SAME_EXPIRY.checked){
			sameExpiryUse = true;
		}
	 if(document.form.ALWAYS_SAME_REMARKS.checked){
			sameRemarksUse = true;
		}
	        if(document.form.ALWAYS_SAME_REASONCODE.checked){
			sameReasonCodeUse = true;
		}
	  if(serialselection && sameLocaionUse && sameProductUse && sameRemarksUse && sameExpiryUse && sameReasonCodeUse ){
	   allchecked=true;
	  }
		if (batch == "" || batch.length == 0) {
			document.form.BATCH.value = "NOBATCH";
		}
		if(allchecked)
			{
			addRow();
			 }
		else
		{
		//document.form.QTY_0.value = "";
		document.form.QTY_0.focus();
		}
	}
	function validateQuantity(index) {
		var qty = document.getElementById("QTY_"+index).value;
		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.getElementById("QTY_"+index).focus();
		} else {
			if (isNumericInput(qty) == false) {
				alert("Entered Quantity is not a valid Qty!");
			} else {
				document.getElementById("REFDET_"+index).value = "";
				document.getElementById("REFDET_"+index).focus();
			}

		}
	}
	function generateBatch(index){
		var currentbatch=index;
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
					document.getElementById("BATCH_"+currentbatch).value = resultVal.batchCode;
					document.getElementById("QTY_"+currentbatch).focus();

				} else {
					alert("Unable to genarate Batch");
					document.getElementById("BATCH_"+currentbatch).value = "";
					document.getElementById("BATCH_"+currentbatch).focus();
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
	document.form.LOC_0.focus();
</script>
<%@ include file="footer.jsp"%>
