<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>QC Reverse</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
     subWin = window.open(URL, 'QCReverse', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
  
  
function validatePO(form)//form
{
   var frmRoot=document.form;
   var table = document.getElementById("QC_REVERSE");
   var rowCount = table.rows.length;
   var checkcnt= rowCount-1; 
   document.form.DYNAMIC_ISSUING_SIZE.value = rowCount;
   processSelection();
   for(var i=0;i<rowCount;i++){
	   var loc= document.getElementById("LOC"+"_"+i);
	   var product = document.getElementById("ITEMNO"+"_"+i);
	   var batch = document.getElementById("BATCH"+"_"+i);
	   var qty= document.getElementById("QTY"+"_"+i);
	   var reasoncode = document.getElementById("REASONCODE"+"_"+i);
	   var availqty =  document.getElementById("INVQTY"+"_"+i);
	   

            if(document.form.SERIAL_SELECTION.checked){
                     for(var j=0;j<rowCount;j++){
                if(i!=j){
                        var chkbatch = document.getElementById("BATCH"+"_"+j);
                         if(batch.value==chkbatch.value){
                              alert("Duplicate batch Scanned !");
                              chkbatch.select();
                              return false;
                          }
                }
                }
            }
	   //getAvailableQty(i);
	  
   var issueqty = parseInt(removeCommas(qty.value),10);
   var inavailqty = parseInt(removeCommas(availqty.value),10);

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
   else   if(availqty.value=="" || availqty.value.length==0)
	 {
		alert("Press Enter After Scan Batch");
		availqty.style.backgroundColor = "#FFE5EC";
		availqty.focus();
		return false;
 }
   else if(isNaN(qty.value)) {alert("Enter valid QTY.");qty.focus(); return false;}


   else   if(reasoncode.value=="" || reasoncode.value.length==0 )
	 {
		alert("Enter REASONCODE!");
		reasoncode.focus();
		return false;
   }
     
   else if(issueqty>inavailqty) {alert("Entered Quantity exceeded the available Qty!");qty.focus(); return false;}
	
   else
    {  
	   
	    if(i==checkcnt)
	    {	   
	   document.form.action.value="QCReverse";
	   document.form.submit();
	   return true;}
	   
    }
   }
	 
}
function getSumByLocItemAndBatch(index)
{
	var cnt = parseInt(index);
	var loc0=document.getElementById("LOC_"+index).value;
	var item0=document.getElementById("ITEMNO_"+index).value;
	var batch0 = document.getElementById("BATCH_"+index).value;
	var qty=0;
	for(var i=0;i<cnt;i++)
	{
		var loc = document.getElementById("LOC_"+i).value;
		var item = document.getElementById("ITEMNO_"+i).value;
		var batch = document.getElementById("BATCH_"+i).value;
		if(loc0==loc && batch0==batch&&item0==item)
		{
			var pickqty = document.getElementById("QTY_"+i).value;
			pickqty = removeCommas(pickqty);
			qty = qty + parseInt(pickqty);
			
		}
	}
	return qty;
}


function onClear(){
   document.form.ITEMNO_0.value="";
  document.form.ITEMDESC_0.value="";
  document.form.LOC_0.value="";
  document.form.BATCH_0.value="";
  document.form.INVQTY_0.value="";
  document.form.QTY_0.value="";
  document.form.REFDET_0.value="";
  document.form.REASONCODE_0.value="";
  document.form.REFDET_0.value="";
  
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
	action="/track/MoveToQCServlet?action=QCReverse">
<br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Move To QC Reversal</FONT>&nbsp;</TH>

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
		<TD colspan="4" ><br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<left><b>Always use the same for the following :</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="Checkbox" name="SERIAL_SELECTION" id="SERIAL_SELECTION" value="SERIAL_SELECTION" onClick="processSerialSelection();"></input> Serial Reverse
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_LOCATION" id="ALWAYS_SAME_LOCATION" value="ALWAYS_SAME_LOCATION" onClick="processSameLocation();"></input> Location
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_PRODUCT" id="ALWAYS_SAME_PRODUCT" value="ALWAYS_SAME_PRODUCT" onClick="processSameProduct();"></input> Product
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_REMARKS" id="ALWAYS_SAME_REMARKS" value="ALWAYS_SAME_REMARKS" onClick="processSameRemarks();"></input>Remarks
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_REASONCODE" id="ALWAYS_SAME_REASONCODE" value="ALWAYS_SAME_REASONCODE" onClick="processSameReasonCode();"></input>Reason Code</left>
                <br>
		<br>
		<table align="center" width="100%" border="0" >
		<tr>
		<td width="11.5%">
		<b>Loc:</b>
		</td>
		<td width="11.5%">
		<b>Product ID:</b>
		</td>
		<td width="12%">
		<b>Product Desc:</b>
		</td>
		<td width="12%">
		<b>Batch:</b>
		</td>
		<td width="10%">
		<b>Available Qty:</b>
		</td>
		<td width="12%">
		<b>Reverse Qty:</b>
		</td>
		<td width="10%">
		<b>Remarks:</b>
		</td><td width="12%">
		<b>Reason Code:</b>
		</td>
		</tr>
		</table>
		<table align="center" width="100%" border="0" id="QC_REVERSE">
		
			<tr>
				<td width="11.5%"><INPUT name="LOC_0" id="LOC_0" type="TEXT"
					value="<%=LOC%>" size="15"
					onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(0);}"
					MAXLENGTH=80> <a href="#"
					onClick="javascript:popUpWin('loc_list_QCReverse.jsp?LOC='+form.LOC_0.value+'&INDEX=0');"><img
					src="images/populate.gif" border="0"></a></td>
				
				<td width="11.5%">
				<INPUT name="ITEMNO_0" type="TEXT" id="ITEMNO_0" value="<%=ITEMNO%>"
			size="15" MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.ITEMNO_0.value.length > 0)){validateProduct(0);}">
		        <a href="#" onClick="javascript:popUpWin('list/multimiscissue_item_list.jsp?ITEMNO='+form.ITEMNO_0.value+'&LOC='+form.LOC_0.value+'&INDEX=0');"> <img src="images/populate.gif" border="0" /> </a>
				</td>
				<td width="12%">
			<INPUT name="ITEMDESC_0"  type="TEXT" id="ITEMDESC_0"
			value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="15" MAXLENGTH=100  >
                        <a href="#" onClick="javascript:popUpWin('list/multimiscissue_item_list.jsp?DESC='+form.ITEMDESC_0.value+'&LOC='+form.LOC_0.value+'&INDEX=0');"> <img src="images/populate.gif" border="0" /> </a>	
				</td>
				<td width="12%"><INPUT name="BATCH_0" id="BATCH_0" type="TEXT" value="<%=BATCH%>" size="15"
			   	 MAXLENGTH=40
			    	onkeypress="if((event.keyCode=='13') && ( document.form.BATCH_0.value.length > 0)){validateBatch(0);}">
					<a href="#" onClick="javascript:popUpWin('batch_list_QCTransfer.jsp?LOC='+form.LOC_0.value+'&ITEMNO='+form.ITEMNO_0.value+'&BATCH'+form.BATCH_0.value+'&INDEX=0');">
					<img src="images/populate.gif" border="0"/></a>
				</td>
				   <td width="10%">
			<INPUT name="INVQTY_0" type="TEXT" id="INVQTY_0" class="inactivegry" readonly value="<%=INVQTY%>"
			size="8" MAXLENGTH=80 > 
			</td>
					
			<td width="12%"><INPUT name="QTY_0" type="TEXT" id="QTY_0" value="<%=QTY%>" size="4"
			MAXLENGTH=50
			onkeypress="if((event.keyCode=='13') && ( document.form.QTY_0.value.length > 0)){validateQuantity(this.value,0);}">
			<INPUT name="UOM_0" id="UOM_0" type="TEXT" value="" size="10"
			MAXLENGTH=10 ></TD>
			<td width="10%">
			<INPUT name="REFDET_0" type="TEXT"  id="REFDET_0" value="<%=REFDET%>"
			size="15" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.REASONCODE_0.focus();}"> 
			</TD>
			<td width="12%">
			<INPUT name="REASONCODE_0" type="TEXT"  id="REASONCODE_0" value="<% if(REASONCODE.length() > 0) { out.print(REASONCODE); }else{out.print("NOREASONCODE");}%>"
			size="15"  MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.REASONCODE_0.value.length > 0)){ document.form.SubmitButton.focus();}"> <a href="#"
			onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form.ITEMNO_0.value+'&INDEX=0');">
			<img src="images/populate.gif" border="0" /> </a>
			<INPUT type="hidden"
					name="AVAILABLEQTY_0" id="AVAILABLEQTY_0">
			</td>
							</tr>

		</table>
		<br></br>
		<table align="center" width="85%" border="0">
			<tr>
				<td><INPUT type="button" value="ADD NEW REVERSE" onclick="addRow();" /> <INPUT type="button"
					value="REMOVE LAST ADDED REVERSE"
					onclick="deleteRow('QC_REVERSE');" /> <INPUT type="hidden"
					name="DYNAMIC_ISSUING_SIZE"></td>
			</tr>
		</table>
		</TD>
	</TR>
	
	<TR>
		<TD COLSPAN=2>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT class="Submit" type="BUTTON" value="Cancel"
			onClick="window.location.href='../home'" />&nbsp;&nbsp; <input
			type="button" value="QCReverse" name="SubmitButton"
			onClick="return validatePO(document.form)" /> <INPUT class="Submit"
			type="BUTTON" value="Clear" onClick="return onClear();">&nbsp;&nbsp;

		</TD>
	</TR>

</TABLE>
<INPUT name="LOGINUSER" type="hidden" value="<%=sUserId %>" size="1"
	MAXLENGTH=80> <INPUT name="action" type="hidden"></center>
	    <INPUT     name="CHECKQTY"  type ="hidden" value="<%=QTY%>" size="1"   MAXLENGTH=80 ></TD>
	 

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
        if(document.form.ALWAYS_SAME_REASONCODE.checked){
		processSameReasonCode();	

	}

	
}
function processSerialSelection() {
	var table = document.getElementById("QC_REVERSE");
	var rowCount = table.rows.length;
	for(var index = 0; index<rowCount; index++) {
		var pickQty = document.getElementById("QTY_"+index);
		if(document.form.SERIAL_SELECTION.checked){
		pickQty.value = 1;
                pickQty.readOnly = true;
		}
		else
		{
			pickQty.value = "";
			pickQty.readOnly = false;
		}
		
	}
}
function processSameLocation(){
	var table = document.getElementById("QC_REVERSE");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("LOC_0").value;
	for(var index = 1; index<rowCount; index++) {
		var locID = document.getElementById("LOC_"+index);
		if(document.form.ALWAYS_SAME_LOCATION.checked){
			locID.value = defaultValue;
			
			}
		else
		{
			locID.value = "";
			locID.readOnly = false;
		}	
		
	}
}
function processSameProduct(){
	var table = document.getElementById("QC_REVERSE");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("ITEMNO_0").value;
        var descValue = document.getElementById("ITEMDESC_0").value;
        var uomValue = document.getElementById("UOM_0").value;
	for(var index = 1; index<rowCount; index++) {
		var productID = document.getElementById("ITEMNO_"+index);
                var productDesc = document.getElementById("ITEMDESC_"+index);
              var uom = document.getElementById("UOM_"+index);
		if(document.form.ALWAYS_SAME_PRODUCT.checked){
			productID.value = defaultValue;
                        productDesc.value = descValue;
                        uom.value=uomValue;

                }
		else
		{
			productID.value="";
                        productDesc.value="";
                        uom.value="";
			productID.readOnly = false;
                        productDesc.readOnly = false;
                        uom.readOnly = false;
		}
		
		
	}
}

function processSameRemarks(){
	var table = document.getElementById("QC_REVERSE");
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
	var table = document.getElementById("QC_REVERSE");
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
	var table = document.getElementById("QC_REVERSE");
	var rowCount = table.rows.length;
	
	var serialselection = false;
	var sameLocaionUse = false;
        var sameProductUse = false;
        var sameRemarksUse = false;
        var sameReasonCodeUse = false;
        var allchecked  =false;

	if(document.form.SERIAL_SELECTION.checked){
		serialselection=true;
	}
	if(document.form.ALWAYS_SAME_LOCATION.checked){
		sameLocaionUse=true;	
	}

        if(document.form.ALWAYS_SAME_PRODUCT.checked){
		sameProductUse="true";
	}
        if(document.form.ALWAYS_SAME_REMARKS.checked){
		sameRemarksUse = true;
	}
        if(document.form.ALWAYS_SAME_REASONCODE.checked){
		sameReasonCodeUse = true;	
	}
        if(serialselection && sameProductUse && sameRemarksUse  && sameReasonCodeUse ){
        allchecked=true;
        }
	var row = table.insertRow(rowCount);
	var firstElementLocationValue = document.getElementById("LOC_0").value;
        var firstElementProductValue = document.getElementById("ITEMNO_0").value;
        var firstElementProductDescValue = document.getElementById("ITEMDESC_0").value;
        var firstElementUomValue = document.getElementById("UOM_0").value;
        var firstElementremarksValue = document.getElementById("REFDET_0").value;
        var firstElementReasoncodeValue = document.getElementById("REASONCODE_0").value;
	
	var locationCell = row.insertCell(0);
		var locationCellText =  "<INPUT name=\"LOC_"+rowCount+"\" ";
		if(sameLocaionUse){
			locationCellText = locationCellText+ "value=\""+firstElementLocationValue+"\" readonly ";
		}
		locationCellText = locationCellText+ " id=\"LOC_"+rowCount+"\" type = \"TEXT\" size=\"15\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation( "+rowCount+");}\" MAXLENGTH=\"80\">";
		if(!sameLocaionUse) {
			locationCellText = locationCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('loc_list_QCReverse.jsp?LOC='+form.LOC"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
		}
	locationCell.innerHTML = locationCellText;
	
	var itemCell = row.insertCell(1);

	var itemCellText =  "<INPUT name=\"ITEMNO_"+rowCount+"\" ";
	if(sameProductUse){
			itemCellText = itemCellText+ "value=\""+firstElementProductValue+"\" readonly ";
	}
	itemCellText = itemCellText+ " id=\"ITEMNO_"+rowCount+"\" type = \"TEXT\" size=\"15\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateProduct("+rowCount+");}\" MAXLENGTH=\"80\">";
	
	itemCellText = itemCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('list/multimiscissue_item_list.jsp?ITEMNO='+form.ITEMNO"+'_'+rowCount+".value+'&LOC='+form.LOC"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	
	
	itemCell.innerHTML = itemCellText;

	var itemdescCell = row.insertCell(2);
		
	var itemdescCellText =  "<INPUT name=\"ITEMDESC_"+rowCount+"\" ";
	if(sameProductUse){
			itemdescCellText = itemdescCellText+ "value=\""+firstElementProductDescValue+"\" readonly ";
	}
	itemdescCellText = itemdescCellText+ " id=\"ITEMDESC_"+rowCount+"\" type = \"TEXT\" size=\"15\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateProduct("+rowCount+");}\" MAXLENGTH=\"80\">";
	
	itemdescCellText = itemdescCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('list/multimiscissue_item_list.jsp?DESC='+form.ITEMDESC"+'_'+rowCount+".value+'&LOC='+form.LOC"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	

	itemdescCell.innerHTML = itemdescCellText;
      
	
	var batchCell = row.insertCell(3);
    var batchCellText = "<INPUT name=\"BATCH_"+rowCount+"\" id=\"BATCH_"+rowCount+"\"  type = \"TEXT\" size=\"15\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch("+rowCount+");}\"  MAXLENGTH=\"40\"> &nbsp;";
	batchCell.innerHTML = batchCellText;
	batchCell.innerHTML = batchCellText + "<a href=\"#\" onClick=\"javascript:popUpWin('batch_list_QCTransfer.jsp?LOC='+form.LOC"+'_'+rowCount+".value+'&ITEMNO='+form.ITEMNO"+'_'+rowCount+".value+'&BATCH='+form.BATCH"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";

	var availableQtyCell = row.insertCell(4);
	var availableQtyText = "<INPUT name=\"INVQTY_"+rowCount+"\" ";
		
	availableQtyText = availableQtyText + " id=\"INVQTY_"+rowCount+"\" type = \"TEXT\" class=\"inactivegry\" readonly  size=\"8\"   MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){document.getElementById(\'INVQTY_"+rowCount+"').focus();}\"  >";
	
	availableQtyCell.innerHTML = availableQtyText;
	
	
	var receivingQtyCell = row.insertCell(5);
	var receiveQtyText = "<INPUT name=\"QTY_"+rowCount+"\" ";
	if(serialselection){
			receiveQtyText = receiveQtyText + " value=\"1\" readonly ";
		}

        receiveQtyText = receiveQtyText + " id=\"QTY_"+rowCount+"\" type = \"TEXT\"  size=\"4\"   MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value, "+rowCount+");}\"  >";
        receiveQtyText = receiveQtyText + "	<INPUT name=\"UOM_"+rowCount+"\" ";
        if(sameProductUse){
			receiveQtyText = receiveQtyText+ "value=\""+firstElementUomValue+"\" readonly ";
	}
         receiveQtyText = receiveQtyText + " id=\"UOM_"+rowCount+"\" type = \"TEXT\" size=\"10\"  MAXLENGTH=\"10\" >";
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
	reasonCellText=reasonCellText+"<INPUT type=\"hidden\" name=\"AVAILABLEQTY_"+rowCount+"\" id=\"AVAILABLEQTY_"+rowCount+"\">";
	
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
			alert("Can not remove the default Issuing");
		} else {
			table.deleteRow(rowCount);
		}
	} catch (e) {
		alert(e);
	}
}


function validateLocation(index) {
	var locId = document.getElementById("LOC"+"_"+index).value;
		
	if(locId.substring(0,3)!="QC_" ) {
		alert("Not a valid Locaion!");
		document.getElementById("LOC"+"_"+index).focus();
	}else{
		
			document.getElementById("ITEMNO"+"_"+index).value = "";
			document.getElementById("ITEMNO"+"_"+index).focus();
					
		}
	}
function validateProduct(index) {
	var productId = document.getElementById("ITEMNO"+"_"+index).value;
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
				PLANT : "<%=PLANT%>",
				ACTION : "VALIDATE_PRODUCT"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						document.getElementById("ITEMDESC"+"_"+index).value = resultVal.discription;
						document.getElementById("ITEMNO"+"_"+index).value=resultVal.item;
						document.getElementById("BATCH"+"_"+index).value = "NOBATCH";
						document.getElementById("UOM"+"_"+index).value = resultVal.uom;
						document.getElementById("BATCH"+"_"+index).select();

					} else {
						alert("Not a valid product");
						document.getElementById("ITEMNO"+"_"+index).value = "";
						document.getElementById("ITEMNO"+"_"+index).focus();
					}
				}
			});
		}
	}
function getAvailableQty(index)
{
	var batch = document.getElementById("BATCH"+"_"+index).value;
	var productId = document.getElementById("ITEMNO"+"_"+index).value;
	var locId = document.getElementById("LOC"+"_"+index).value;
	var urlStr = "/track/MiscOrderHandlingServlet";
	var availableqty;
	$.ajax( {
		type : "GET",
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
					var totissueqty = getSumByLocItemAndBatch(index);
					document.getElementById("INVQTY"+"_"+index).value = resultVal.availableQty-totissueqty;
					

				} else {
					availableqty=0;
					
				}
			}
		}); 
	
}
	function validateBatch(index) {
		var batch = document.getElementById("BATCH"+"_"+index).value;
		var productId = document.getElementById("ITEMNO"+"_"+index).value;
		var locId = document.getElementById("LOC"+"_"+index).value;

                var serialselection = false;
	var sameLocaionUse = false;
        var sameProductUse = false;
        var sameRemarksUse = false;
        var sameReasonCodeUse = false;
        var allchecked  =false;

	if(document.form.SERIAL_SELECTION.checked){
		serialselection=true;
	}
	if(document.form.ALWAYS_SAME_LOCATION.checked){
		sameLocaionUse=true;	
	}

        if(document.form.ALWAYS_SAME_PRODUCT.checked){
		sameProductUse="true";
	}
        if(document.form.ALWAYS_SAME_REMARKS.checked){
		sameRemarksUse = true;
	}
        if(document.form.ALWAYS_SAME_REASONCODE.checked){
		sameReasonCodeUse = true;	
	}
        if(serialselection && sameProductUse && sameRemarksUse  && sameReasonCodeUse ){
        allchecked=true;
        }

		if(locId=="" || locId.length==0 ) {
			alert("Enter Location!");
			document.form.LOC.focus();
		}else{
			if(document.getElementById("ITEMNO"+"_"+index).value=="" || document.getElementById("ITEMNO"+"_"+index).value.length==0 ) {
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
									var totissueqty = getSumByLocItemAndBatch(index);
									var invqty = addCommas(resultVal.availableQty-totissueqty);
									document.getElementById("INVQTY"+"_"+index).value =invqty;
									//document.getElementById("QTY"+"_"+index).value = "";
									 if(allchecked){
                                         addRow();
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
                       
				var availableQty =document.getElementById("INVQTY"+"_"+index).value;
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
	
	function getavailqty(qty,index)
	{
		
		var availqty=qty;
		if(index>0){  
			
		var prevIndex = parseFloat(index);
		var totalpickqty = getSumByPrdAndBatch(index);
		var curavailqty = document.getElementById("AVAILQTY_"+index).value;
		curavailqty = removeCommas(curavailqty);
		curavailqty = round_float(curavailqty,3);
		//document.getElementById("AVAILQTY_"+index).value = round_float(curavailqty-totalpickqty,3);
		document.getElementById("AVAILQTY_"+index).value = addCommas(curavailqty-totalpickqty);
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
	
	document.form.LOC_0.focus();
</script>
<%@ include file="footer.jsp"%>
