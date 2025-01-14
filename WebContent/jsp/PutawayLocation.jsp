<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<html>

<title>Putaway Location</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'PutawayLocation', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
  function validatePO(form)//form
  {
    var frmRoot=document.form;
  

    var table = document.getElementById("MULTI_ADD_PRODUCT");
    var rowCount = table.rows.length;
    var checkcnt = rowCount-1;
    document.form.DYNAMIC_PRODUCT_SIZE.value = rowCount;
    processSelection();
    
    if(frmRoot.FROM_LOC.value=="" || frmRoot.FROM_LOC.value.length==0 )
	 {
		alert("Please Enter FROMLOC!");
		frmRoot.FROM_LOC.focus();
		return false;
     }
        
    	for(var i =0;i<rowCount;i++)
        {   
        var product = document.getElementById("ITEMNO"+"_"+i);
    	   var batch = document.getElementById("BATCH"+"_"+i);
    	   var qty= document.getElementById("QTY"+"_"+i);
		   var availQty = document.getElementById("AVAILQTY_"+i);
		   var toloc = document.getElementById("TOLOC_"+i);
  		   
  		   var pkqty = parseFloat(removeCommas(qty.value)).toFixed(3);
  		   var avqty = parseFloat(removeCommas(availQty.value)).toFixed(3);
  		   pkqty = round_float(pkqty,3);
  		   avqty = round_float(avqty,3);
         if(product.value=="" || product.value.length==0 )
    	 {
    		alert("Please Enter PRODUCTID!");
    		product.focus();
    		return false;
         }
       else if(batch.value=="" || batch.value.length==0 )
    	 {
    		alert("Please Enter BATCH!");
    		batch.focus();
    		return false;
         }
       else if(toloc.value=="" || toloc.value.length==0 )
  	 {
  		alert("Please Enter TO Location!");
  		toloc.focus();
  		
  		return false;
       }
       else if(toloc.value==frmRoot.FROM_LOC.value)
       {
    	   alert("Please choose TO LOC different from FROM LOC"); 
    	   toloc.focus();
    	   toloc.select();
    	   return false;
        }
         
       else if(removeCommas(qty.value)=="" || removeCommas(qty.value).length==0 )
  	 {
  		alert("Please Enter Qty!");
  		qty.focus();
  		return false;
       }

       else if(isNaN(removeCommas(qty.value))) {alert("Please enter valid QTY.");qty.focus(); return false;}
      
       else if(removeCommas(qty.value) <=0  )
       {
         alert("Qty Should not be 0 or less than 0");
         qty.focus();
    	 return false;
       }
       else if(pkqty>avqty)
		   {
			   alert("Entered Quantity is greater than Available Qty");
			   document.getElementById("QTY"+"_"+i).value="";
			   document.getElementById("QTY"+"_"+i).focus();
			   
			   return false;
		   }

        
       else
       {
           if(i==checkcnt){
       document.form.action.value="/track/LocationTransferServlet?action=PUTAWAYLOC";
    	  document.form.submit();
          return true; }
       }
    }
   
    
    }

 /* else if(parseInt(qty) > parseInt(invqty))
 
   {
	   alert("Transfer quantity Should <= InvQty"); 
	   return false;
    }*/
  
 

function onClear(){
  document.form.FROM_LOC.value="";
  document.form.ITEMNO_0.value="";
  document.form.ITEMDESC_0.value="";
  document.form.TOLOC_0.value="";
  document.form.BATCH_0.value="";
  document.form.QTY_0.value="";
  document.form.UOM_0.value="";
  document.form.REFDET_0.value="";
  document.form.REASONCODE_0.value="";
  document.form.AVAILQTY_0.value="";
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
       FROM_LOC= "",  LOC= "" ,TO_LOC   = "" , CHECKQTY="",BATCH  = "", REF   = "",UOM="",TRANSACTIONDATE="",
       QTY = "",INVQTY="",RECEIVEQTY="",REFDET="",REASONCODE="";
       FROM_LOC = strUtils.fString(request.getParameter("FROM_LOC"));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       INVQTY = strUtils.fString(request.getParameter("INVQTY"));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       LOC = strUtils.fString(request.getParameter("TOLOC"));
       UOM = strUtils.fString(request.getParameter("UOM"));
       REFDET = strUtils.fString(request.getParameter("REFDET"));
       REASONCODE = strUtils.fString(request.getParameter("REASONCODE"));
       TRANSACTIONDATE =su.fString(request.getParameter("TRANSACTIONDATE"));
       DateUtils _dateUtils = new DateUtils();
       String curDate =_dateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
        
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
<FORM name="form" method="post" action="/track/LocationTransferServlet?action=PUTAWAYLOC"  >
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Putaway Location</FONT>&nbsp;
      </TH>
    </TR>
  </TABLE>
  <br>
 <font face="Times New Roman" size="4">
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
  <%=fieldDesc%>
  </font>
  </table>
  <table bgcolor="#dddddd" border = "0" WIDTH="100%">
  <tr>
  <td>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
   
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >From Loc&nbsp;&nbsp;: </TH>
            <TD>
                <INPUT name="FROM_LOC" type = "TEXT" value="<%=FROM_LOC%>" size="50" 
            onkeypress="if((event.keyCode=='13') && ( document.form.FROM_LOC.value.length > 0)) {validateLocation(this.value);}"    MAXLENGTH="20">
              <a href="#" onClick="javascript:popUpWin('loc_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value+'&TYPE=PUTAWAYLOC');"><img src="images/populate.gif" border="0"></a>
          </TD>
    </TR>
    
    </TABLE>
    </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
    <td>
    <table >
    
    <TR>         
         <TD width = "12%">
        <input type="Checkbox" name="SERIAL_SELECTION" id="SERIAL_SELECTION" value="SERIAL_SELECTION" onClick="processSerialSelection();"></input>
	 Use Serial Transfer</td>
	<td width = "12%">Always use the same for the following :</td>
	<td width = "10%"><input type="Checkbox" name="ALWAYS_SAME_PRODUCT" id="ALWAYS_SAME_PRODUCT" value="ALWAYS_SAME_PRODUCT" onClick="processSameProduct();"></input>Product</td>
         <td width = "10%">
         <input type="Checkbox" name="ALWAYS_SAME_REMARKS" id="ALWAYS_SAME_REMARKS" value="ALWAYS_SAME_REMARKS" onClick="processSameRemarks();"></input>Remarks</td>
         <td width = "10%">
         <input type="Checkbox" name="ALWAYS_SAME_REASONCODE" id="ALWAYS_SAME_REASONCODE" value="ALWAYS_SAME_REASONCODE" onClick="processSameReasonCode();"></input>Reason Code</td>
        
    </TR>
    </table>
<br>
     <table align="center" width="100%" border="0">
		<tr>
		<td width="12%">
		<b>Product ID:</b>
		</td>
		<td width="14%">
		<b>Product Desc:</b>
		</td>
		<td WIDTH="12%">
		<b>To Loc:</b>
		</td>
		<td width="12%">
		<b>Batch:</b>
		</td>
		<td width="10%">
		<b>Available QTY:</b>
		</td>
		<td width="14%">
		<b>Qty:</b>
		</td>
		<td width="12%">
		<b>Remarks:</b>
		</td><td width="14%">
		<b>Reason Code:</b>
		</td>
		</tr>
		</table>
    <table align="center" border="0" CELLSPACING=0 width="100%" border="0" id="MULTI_ADD_PRODUCT" >
    		<tr>
				<td width = "12%">
				<INPUT name="ITEMNO_0" type = "TEXT" id="ITEMNO_0" value="<%=ITEMNO%>" size="18" onkeypress="if((event.keyCode=='13') && (document.form.ITEMNO_0.value.length > 0)) {validateProduct(0);}"  MAXLENGTH=80 >
         <a href="#" onClick="javascript:popUpWin('item_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value+'&ITEMNO='+form.ITEMNO_0.value+'&INDEX=0');">
           <img src="images/populate.gif" border="0"/>
         </a>
			</td>
				<td width = "14%">
			
        <INPUT name="ITEMDESC_0"  type = "TEXT" id="ITEMDESC_0" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="25" 
         MAXLENGTH=80 > <a href="#" onClick="javascript:popUpWin('item_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value+'&DESC='+form.ITEMDESC_0.value+'&INDEX=0');">
           <img src="images/populate.gif" border="0"/>
         </a>
				</td>
		<td width = "12%">
		     <INPUT name="TOLOC_0" type="TEXT" id="TOLOC_0" value="<%=LOC%>" size="18"  onkeypress="if((event.keyCode=='13') && ( document.form.TOLOC_0.value.length > 0)) {validateTOLocation(0);}"  MAXLENGTH="80" />
            <a href="#" onClick="javascript:popUpWin('loc_list_dotransferto.jsp?TOLOC='+form.TOLOC_0.value+'&TYPE=PUTAWAYLOC&INDEX=0');">
           <img src="images/populate.gif" border="0"/>
         </a>
         </TD>
   			<td width = "12%">     
				
         <INPUT name="BATCH_0" type = "TEXT" id="BATCH_0" value="<%=BATCH%>" size="18"  MAXLENGTH=40 onkeypress="if((event.keyCode=='13') && ( document.form.BATCH_0.value.length > 0)) {validateBatch(0);}">
       <a href="#" onClick="javascript:popUpWin('batch_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value+'&ITEMNO='+form.ITEMNO_0.value+'&BATCH'+form.BATCH_0.value+'&INDEX=0');">
                  <img src="images/populate.gif" border="0"/>
         </a>
        
          </TD>
          <td width="10%">
			<INPUT name="AVAILQTY_0" type="TEXT" id="AVAILQTY_0" class="inactivegry" readonly value="<%=QTY%>" size="8" MAXLENGTH=80 >
			 
			</td>
 					 
         <TD width = "14%"> <INPUT name="QTY_0" type = "TEXT" id="QTY_0" value="<%=QTY%>" size="5" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value,0);}" MAXLENGTH=80 >&nbsp;
         <INPUT name="UOM_0" type = "TEXT" id="UOM_0" value="<%=UOM %>" size="10"  MAXLENGTH=80 ></TD>
         
         <td width = "12%"><INPUT name="REFDET_0" type="TEXT"  id="REFDET_0" value="<%=REFDET%>"
			size="15" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.REASONCODE_0.focus();}"> 
			</TD>
			<td width = "14%" >
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
				<td><INPUT type="button" value="ADD NEW PRODUCT" onclick="addRow();" /> <INPUT type="button"
					value="REMOVE LAST ADDED PRODUCT"
					onclick="deleteRow('MULTI_ADD_PRODUCT');" /> <INPUT type="hidden"
					name="DYNAMIC_PRODUCT_SIZE">
					&nbsp;&nbsp;<b>Transaction Date : </b><INPUT name="TRANSACTIONDATE" id="TRANSACTIONDATE"
					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" MAXLENGTH="80" readonly="readonly">
				    <a href="javascript:show_calendar('form.TRANSACTIONDATE');"
			   		onmouseover="window.status='Date Picker';return true;"
			   		onmouseout="window.status='';return true;"> <img
			   		src="images\show-calendar.gif" width="24" height="22" border="0" /></a>
			     </td>
			</tr>
		</table>
   &nbsp;
&nbsp; 	
    <table align="left" bgcolor="#dddddd">
    <TR>
         <TD COLSPAN = 2>
              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='<%=com.track.constants.PageConstant.CancelButton%>'"/>&nbsp;&nbsp;
              <input  type="button" value="Putaway" name="SubmitButton" onClick="return validatePO(document.form)"/>
              <INPUT class="Submit" type="BUTTON" value="Clear" onClick="return onClear();">&nbsp;&nbsp;    
         </TD>
    </TR>
</TABLE>
</center>
<Table border=0 bgcolor="#dddddd">
    <TR  bgcolor="#dddddd"> <INPUT name="action" type="hidden">
      <INPUT     name="CHECKQTY"  type ="hidden" value="<%=QTY%>" size="1"   MAXLENGTH=80 ></TD>
      <INPUT     name="LOGINUSER"  type ="hidden" value="<%=sUserId %>" size="1"   MAXLENGTH=80 ></TD>
      <INPUT     name="INVQTY_0"  type ="hidden" id ="INVQTY_0" value="<%=INVQTY%>" size="10"   MAXLENGTH=80 >
     </TD>
    </TR>
</Table>
</td>
</tr>
</table>
</FORM>

</HTML>
<%@ include file="footer.jsp"%>
<script>
function processSelection()
{
	var table = document.getElementById("MULTI_ADD_PRODUCT");
    var rowCount = table.rows.length;
	if(document.form.SERIAL_SELECTION.checked){
		
		processSerialSelection();
	}
	if(document.form.ALWAYS_SAME_PRODUCT.checked){
		processSameProduct();	
	}
	 if(document.form.ALWAYS_SAME_REMARKS.checked){
			processSameRemarks();	

		}
	        if(document.form.ALWAYS_SAME_REASONCODE.checked){
			processSameReasonCode();	

		}


}
function processSerialSelection() {
	var table = document.getElementById("MULTI_ADD_PRODUCT");
	var rowCount = table.rows.length;
	for(var index = 0; index<rowCount; index++) {
		
		var pickQty = document.getElementById("QTY_"+index);
		if(document.form.SERIAL_SELECTION.checked){
			
		pickQty.value = 1;
		pickQty.readOnly=true;
		}
		else{
			pickQty.value = "";
			pickQty.readOnly=false;
		}
		
		
	}
}

function processSameProduct(){
	var table = document.getElementById("MULTI_ADD_PRODUCT");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("ITEMNO_0").value;
	var descValue = document.getElementById("ITEMDESC_0").value;
	var uomValue = document.getElementById("UOM_0").value;
	for(var index = 1; index<rowCount; index++) {
		var pickQty = document.getElementById("ITEMNO_"+index);

		var desc = document.getElementById("ITEMDESC_"+index);
		
		var uom = document.getElementById("UOM_"+index);
		
		if(document.form.ALWAYS_SAME_PRODUCT.checked){
			pickQty.value = defaultValue;
			desc.value = descValue;
			uom.value = uomValue;
		}else{
			pickQty.value = "";
			desc.value = "";
			uom.value = "";
			document.getElementById("BATCH_"+index).value="";
			document.getElementById("AVAILQTY_"+index).value="";
			document.getElementById("QTY_"+index).value="";
			document.getElementById("REFDET_"+index).value="";
			
			
		}		
	}
}
function processSameRemarks(){
	var table = document.getElementById("MULTI_ADD_PRODUCT");
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
	var table = document.getElementById("MULTI_ADD_PRODUCT");
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
function getavailqty(qty,index)
{
	
	var availqty=qty;
	if(index>0){  
		
		var prevIndex = parseFloat(index);
		
		var totalpickqty = getSumByPrdAndBatch(index);
		//alert(totalpickqty);
	//var prevavailqty= document.getElementById("AVAILABLEQTY_"+prevIndex-1);
	var curavailqty = document.getElementById("AVAILQTY_"+index).value;
	curavailqty = removeCommas(curavailqty);
	curavailqty = round_float(curavailqty,3);
	//document.getElementById("AVAILQTY_"+index).value = round_float(curavailqty-totalpickqty,3);
	document.getElementById("AVAILQTY_"+index).value = addCommas(curavailqty-totalpickqty);
	}
	
}

function getSumByPrdAndBatch(index)
{
	var cnt = parseFloat(index);
	var loc0=document.getElementById("ITEMNO_"+index).value;
	var batch0 = document.getElementById("BATCH_"+index).value;
	var qty=0;
	for(var i=0;i<cnt;i++)
	{
		var loc = document.getElementById("ITEMNO_"+i).value;
		var batch = document.getElementById("BATCH_"+i).value;
		if(loc0==loc && batch0==batch)
		{
			var pickqty = document.getElementById("QTY_"+i).value;
			pickqty=removeCommas(pickqty);
			
			qty = qty + parseFloat(pickqty);
			qty = round_float(qty,3);
		}
	}
	return qty;
}
function removeCommas(number)
{
	var numval=number.value;
	var splitArr = new Array();
	var appendStr = new String();
	splitArr = number.split(',');
	for(var i=0; i<splitArr.length; i++){
		appendStr = appendStr + splitArr[i];}

     return appendStr;

}
function addRow() {
	var table = document.getElementById("MULTI_ADD_PRODUCT");
	var rowCount = table.rows.length;
	
	var serialselection = false;
	var sameProductUse = false;
	 var sameRemarksUse = false;
     var sameReasonCodeUse = false;

	if(document.form.SERIAL_SELECTION.checked){
		processSerialSelection();
		serialselection = true;
	}
	if(document.form.ALWAYS_SAME_PRODUCT.checked){
		sameProductUse = true;
		
	}	
	 if(document.form.ALWAYS_SAME_REMARKS.checked){
			sameRemarksUse = true;
		}
	        if(document.form.ALWAYS_SAME_REASONCODE.checked){
			sameReasonCodeUse = true;	
		}
	var row = table.insertRow(rowCount);
	
	var itemCell = row.insertCell(0);
	var firstElementProductValue = document.getElementById("ITEMNO_0").value;
	var firstElementUomVal = document.getElementById("UOM_0").value;
	var firstElementPrddesc =  document.getElementById("ITEMDESC_0").value;
	 var firstElementremarksValue = document.getElementById("REFDET_0").value;
     var firstElementReasoncodeValue = document.getElementById("REASONCODE_0").value;
     var firstElementTolocVal = document.getElementById("TOLOC_0").value;
	var itemCellText =  "<INPUT name=\"ITEMNO_"+rowCount+"\" ";
	
	if(sameProductUse){
		itemCellText = itemCellText+ "value=\""+firstElementProductValue+"\" readonly ";
	}
	itemCellText = itemCellText +" id=\"ITEMNO_"+rowCount+"\" type = \"TEXT\" size=\"18\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateProduct("+rowCount+");}\" MAXLENGTH=\"80\"> &nbsp;";	
	if(!sameProductUse) {
		//itemCellText = itemCellText+ " id=\"ITEMNO_"+rowCount+"\" type = \"TEXT\" size=\"18\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateProduct("+rowCount+");}\" MAXLENGTH=\"80\"> &nbsp;";
		itemCellText = itemCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('item_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value+'&ITEMNO='+form.ITEMNO"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";	
	}
	
	itemCell.innerHTML = itemCellText;

	var itemdescCell = row.insertCell(1);
		
	var itemdescCellText =  "<INPUT name=\"ITEMDESC_"+rowCount+"\" ";
	if(sameProductUse){
		itemdescCellText = itemdescCellText+ "value=\""+firstElementPrddesc+"\" readonly ";
	}
	
	itemdescCellText = itemdescCellText+ " id=\"ITEMDESC_"+rowCount+"\" type = \"TEXT\" size=\"25\"   MAXLENGTH=\"80\"> &nbsp;";
	if(!sameProductUse) {
	itemdescCellText = itemdescCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('item_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value+'&ITEMNO='+form.ITEMNO"+'_'+rowCount+".value+'&DESC='+form.ITEMDESC"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	}

	itemdescCell.innerHTML = itemdescCellText;
	
	var tolocCell = row.insertCell(2);
	
	var tolocCellText = "<INPUT name=\"TOLOC_"+rowCount+"\" ";
	if(sameProductUse){
		tolocCellText = tolocCellText+ "value=\""+firstElementTolocVal+"\"  ";
	}
	
	tolocCellText = tolocCellText+ "id=\"TOLOC_"+rowCount+"\"  type = \"TEXT\" size=\"18\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateTOLocation("+rowCount+");}\"  MAXLENGTH=\"40\"> &nbsp;";
	tolocCell.innerHTML = tolocCellText;
	tolocCell.innerHTML = tolocCellText + "<a href=\"#\" onClick=\"javascript:popUpWin('loc_list_dotransferto.jsp?TOLOC='+form.TOLOC"+'_'+rowCount+".value+'&TYPE=PUTAWAYLOC&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";

	
	
	var batchCell = row.insertCell(3);
	var batchCellText = "<INPUT name=\"BATCH_"+rowCount+"\" id=\"BATCH_"+rowCount+"\"  type = \"TEXT\" size=\"18\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch("+rowCount+");}\"  MAXLENGTH=\"40\"> &nbsp;";
	batchCell.innerHTML = batchCellText;
	batchCell.innerHTML = batchCellText + "<a href=\"#\" onClick=\"javascript:popUpWin('batch_list_locationtransfer.jsp?FROM_LOC='+form.FROM_LOC.value+'&ITEMNO='+form.ITEMNO"+'_'+rowCount+".value+'&BATCH='+form.BATCH"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";

	var availableQtyCell = row.insertCell(4);
	var availableQtyText = "<INPUT name=\"AVAILQTY_"+rowCount+"\" ";
		
	availableQtyText = availableQtyText + " id=\"AVAILQTY_"+rowCount+"\" type = \"TEXT\" class=\"inactivegry\" readonly  size=\"8\"   MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){document.getElementById(\'INVQTY_"+rowCount+"').focus();}\"  >";
	
	availableQtyCell.innerHTML = availableQtyText;
	
	
	var receivingQtyCell = row.insertCell(5);
	var receiveQtyText = "<INPUT name=\"QTY_"+rowCount+"\" ";
		if(serialselection){
			receiveQtyText = receiveQtyText + " value=\"1\" readonly ";
		}
		receiveQtyText = receiveQtyText + " id=\"QTY_"+rowCount+"\" type = \"TEXT\"  size=\"5\"   MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value, "+rowCount+");}\"  >";
		receiveQtyText = receiveQtyText + "	&nbsp;<INPUT name=\"UOM_"+rowCount+"\" id=\"UOM_"+rowCount+"\" type = \"TEXT\" size=\"10\"  ";
		if(sameProductUse){
			receiveQtyText = receiveQtyText+ "value=\""+firstElementUomVal+"\" readonly ";
		}
		receiveQtyText= receiveQtyText+ " MAXLENGTH=\"10\" \>"
	receivingQtyCell.innerHTML = receiveQtyText;
	
	
	var remarksCell = row.insertCell(6);
	var remarkCellText ="<INPUT name=\"REFDET_"+rowCount+"\" type=\"TEXT\" id=\"REFDET_"+rowCount+"\"";
        if(sameRemarksUse){
			remarkCellText = remarkCellText+ "value=\""+firstElementremarksValue+"\" readonly ";
	}
	remarkCellText=remarkCellText+" size=\"15\" MAXLENGTH=\"80\" onkeypress=\"if(event.keyCode=='13') {document.form.REASONCODE.focus();}\"> ";
	remarksCell.innerHTML=	remarkCellText;
	
	var reasonCell = row.insertCell(7);
	
	var reasonCellText =  "<INPUT name=\"REASONCODE_"+rowCount+"\" ";
	  if(sameReasonCodeUse){
			reasonCellText = reasonCellText+ "value=\""+firstElementReasoncodeValue+"\" readonly ";
	}
	  else
	  {
		  reasonCellText = reasonCellText+" value=\"NOREASONCODE\"  ";
	  }
        reasonCellText = reasonCellText+ " id=\"REASONCODE_"+rowCount+"\" type = \"TEXT\" size=\"15\"   MAXLENGTH=\"80\">";
	
	reasonCellText = reasonCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form.ITEMNO"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	reasonCell.innerHTML = reasonCellText;
	
	var invQtyCell = row.insertCell(8);
	var invQtyCellText = "<INPUT name=\"INVQTY_"+rowCount+"\" id=\"INVQTY_"+rowCount+"\" type = \"HIDDEN\" >";
	invQtyCell.innerHTML = invQtyCellText;
}
function deleteRow(tableID) {
	try {
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		rowCount = rowCount * 1 - 1;
		if (rowCount == 0) {
			alert("Can not remove the default Product");
		} else {
			table.deleteRow(rowCount);
		}
	} catch (e) {
		alert(e);
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
						document.form.TOLOC.value = "";
						document.form.TOLOC.focus();
					} else {
						alert("Not a valid From Location");
						document.form.FROM_LOC.value = "";
						document.form.FROM_LOC.focus();
					}
				}
			});
		}
	}
  function validateTOLocation(index) {
	  var locId = document.getElementById("TOLOC"+"_"+index).value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter To Location!");
		document.getElementById("TOLOC"+"_"+index).focus();
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
						document.getElementById("BATCH"+"_"+index).focus();
						document.getElementById("BATCH"+"_"+index).select();
					} else {
						alert("Not a valid To Location");
						document.getElementById("TOLOC"+"_"+index).value = "";
						document.getElementById("TOLOC"+"_"+index).focus();
					}
				}
			});
		}
	}
  function validateProduct(index) {
	var productId = document.getElementById("ITEMNO"+"_"+index).value;
	var loc =document.form.FROM_LOC.value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product ID!");
		document.getElementById("ITEMNO"+"_"+index).focus();
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
						document.getElementById("ITEMDESC"+"_"+index).value = resultVal.discription;
						document.getElementById("ITEMNO"+"_"+index).value=resultVal.item;
						document.getElementById("UOM"+"_"+index).value = resultVal.uom;
						document.getElementById("TOLOC"+"_"+index).value = resultVal.itemloc;
                        document.getElementById("BATCH"+"_"+index).value = "NOBATCH";
                        if(document.getElementById("TOLOC"+"_"+index).value==""){
                        	document.getElementById("TOLOC"+"_"+index).focus();
                        }
                        else{
						document.getElementById("BATCH"+"_"+index).focus();
						document.getElementById("BATCH"+"_"+index).select();}
						// below code added by deen to get available qty
						var invqty = resultVal.qty;
						document.getElementById("AVAILQTY"+"_"+index).value = invqty;
                        getavailqty(invqty,index);
                        //end
                        //document.form.INVQTY.value = resultVal.qty;
                        

					} else {
						alert("Not a valid product for From Loc");
						document.getElementById("ITEMNO"+"_"+index).value = "";
						document.getElementById("ITEMNO"+"_"+index).focus();
					}
				}
			});
		}
	}  
    function validateBatch(index) {
      
		var batch = document.getElementById("BATCH"+"_"+index).value;
		var productId = document.getElementById("ITEMNO"+"_"+index).value;
		var locId = document.form.FROM_LOC.value;
		
		var sameProductUse = false;
		var sameRemarksUse = false;
		var sameReasonCodeUse = false;
		var serialselection = false;
		var allchecked = false;

		if (document.form.SERIAL_SELECTION.checked) {
			serialselection = true;
		}
		if (document.form.ALWAYS_SAME_PRODUCT.checked) {
			sameProductUse = "true";
		}
		if (document.form.ALWAYS_SAME_REMARKS.checked) {
			sameRemarksUse = true;
		}
		if (document.form.ALWAYS_SAME_REASONCODE.checked) {
			sameReasonCodeUse = true;
		}
				
		if (serialselection && sameProductUse && sameRemarksUse
				&& sameReasonCodeUse) {
			allchecked = true;
		}
		if (locId == "" || locId.length == 0) {
			alert("Enter Location!");
			document.form.LOC.focus();
		}else{
			if(productId=="" || productId.length==0 ) {
				alert("Enter Product ID!");
				document.getElementById("ITEMNO"+"_"+index).focus();
			}
			else{
				if (batch == "" || batch.length == 0) {
					alert("Enter Batch!");
					document.getElementById("BATCH"+"_"+index).focus();
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
									document.getElementById("BATCH"+"_"+index).value = resultVal.batchCode;
									document.getElementById("INVQTY"+"_"+index).value = resultVal.availableQty;
									var totissueqty = getSumByPrdAndBatch(index);
									var invqty = addCommas(resultVal.availableQty-totissueqty);
									document.getElementById("AVAILQTY"+"_"+index).value =invqty;
									if(allchecked){
                                         addRow();  
                                         index = index+1; 
                                         
                                         if(document.getElementById("TOLOC"+"_"+index).value==""){
                                         	document.getElementById("TOLOC"+"_"+index).focus();
                                         }
                                         else{
                 							document.getElementById("BATCH"+"_"+index).focus();
                 							}
                                      }else{
                                    	 document.getElementById("QTY"+"_"+index).focus();
                                         
                                     }
										} else {
									alert("Not a valid Batch");
									document.getElementById("BATCH"+"_"+index).value = "";
									document.getElementById("BATCH"+"_"+index).focus();
								}
							}
						});
					}
				}
			}
	}    
        function validateQuantity(qty,index) {
        
		var qty = document.getElementById("QTY"+"_"+index).value;
		qty = removeCommas(qty);
               
		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.getElementById("QTY"+"_"+index).focus();
		} 
			else if(qty <=0  )
		       {
		         alert("Qty Should not be 0 or less than 0");
		         document.getElementById("QTY"+"_"+index).value = "";
		         document.getElementById("QTY"+"_"+index).focus();
		    	 return false;
		       }
		else {
                 
			if (isNumericInput(qty) == false) {
				alert("Entered Quantity is not a valid Qty!");
			} else {
                       
				var availableQty = document.getElementById("AVAILQTY"+"_"+index).value;
				availableQty =removeCommas(availableQty);
				availableQty = availableQty*1;
                             
				//alert(qty);
				if(qty>availableQty){
					alert("Entered Quantity is greater than the Available Qty!");
					document.getElementById("QTY"+"_"+index).value = "";
					document.getElementById("QTY"+"_"+index).focus();
				}else{
                                        document.getElementById("REFDET"+"_"+index).focus();
					
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