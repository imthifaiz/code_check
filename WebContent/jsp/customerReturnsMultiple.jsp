<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>OutBound Order Returns Multiple</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'customerReturns', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }

function validatePO(form)//form
{
 
	 var frmRoot=document.form;
	   var table = document.getElementById("MULTI_CUSTOMER_RETURNS");
	   var rowCount = table.rows.length;
	   var checkcnt = rowCount-1;
	   document.form.DYNAMIC_RETURNS_SIZE.value = rowCount;
	   if(document.form.ALWAYS_SAME_MERCHANT.checked){
			processSameMerchant();
		} 
	   if(document.form.ALWAYS_SAME_LOCATION.checked){
		   processSameLocation();
		} 
	  for(var i=0;i<rowCount;i++)
	  { 
	   var product = document.getElementById("ITEM"+"_"+i);
	   var batch = document.getElementById("BATCH"+"_"+i);
	   var qty= document.getElementById("QTY"+"_"+i);
	   var orderno = document.getElementById("ORDERNO"+"_"+i);
   
   if(product.value=="" || product.value.length==0 )
	 {
		alert("Enter Product ID!");
		product.focus();
		return false;
   }
   
   else if(batch.value=="" || batch.value.length==0 )
	 {
		alert("Enter BATCH!");
		batch.focus();
	
		return false;
   }
   
     else if(orderno.value=="" || orderno.value.length==0)
	 {
		alert("Enter OrderNo");
		orderno.focus();
		return false;
}
   else
    {
	   document.form.action.value="customerReturnsMultiple";
	   document.form.submit();
	   return true;
    }
	  }
}

function onClear(){
   
  document.form.ITEM_0.value="";
  document.form.ITEMDESC_0.value="";
  
  document.form.BATCH_0.value="";
  document.form.QTY_0.value="";
  document.form.REMARK_0.value="";
  document.form.CUST_NAME_0.value="";
  document.form.FROMLOC_0.value="";
  
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
       FROMLOC   = "" , BATCH  = "", REF   = "",CLAIMPRICE="",
       QTY = "",REMARK="",CUST_NAME="",EXPIREDATE="",UOM="",ORDERNO="",REASONCODE="";
       ItemMstDAO itemdao = new ItemMstDAO();
     
       ITEM = strUtils.fString(request.getParameter("ITEM"));
       ITEMDESC =strUtils.replaceCharacters2Recv( strUtils.fString(request.getParameter("ITEMDESC")));
       
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       REMARK = strUtils.fString(request.getParameter("REMARK"));
      // CUST_NAME = strUtils.fString(request.getParameter("CUST_NAME"));
      ORDERNO=strUtils.fString(request.getParameter("ORDERNO"));
      REASONCODE = strUtils.fString(request.getParameter("REASONCODE"));
      
      
     if(action.equalsIgnoreCase("CLEAR"))
      {
       
        ITEM = "";
        ITEMDESC = "";
        FROMLOC = "";
        BATCH = "";
        REF = "";
        REMARK  = "";
        ORDERNO = "";
        REASONCODE="NOREASONCODE";
       
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
	action="/track/CustomerReturnsServlet?action=customerReturnsMultiple"><br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">OutBound Order Returns Multiple</FONT>&nbsp;</TH>

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
		<TD colspan="4" ><br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<left>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_MERCHANT" id="ALWAYS_SAME_MERCHANT" value="ALWAYS_SAME_MERCHANT" onClick="processSameMerchant();"></input> Always use
		the same ReasonCode</left>
		<left>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_LOCATION" id="ALWAYS_SAME_LOCATION" value="ALWAYS_SAME_LOCATION" onClick="processSameLocation();"></input> Always use
		the same Location</left>
		<br>
		<table align="center" width="100%" border="0" >
		<tr>
		<td width="12%">
		<b>Product ID:</b>
		</td>
		<td width="12%">
		<b>Description:</b>
		</td>
		<td width="12%"><b>Order No:</b></td>
		<td width="10%">
		<b>Batch:</b>
		</td>
		<td width="5%">
		<b>Qty:</b>
		</td>
		<td width="12%">
		<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Remark:</b>
		</td>
		
		<td width="15%">
		<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ReasonCode:</b>
		</td>
		
		<td width="15%">
		<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Loc:</b>
		</td>
		
		</tr>
		
		</table>

	
	   <table align="center" width="100%" border="0" id="MULTI_CUSTOMER_RETURNS">
		
			<tr>
				<td width="12%"><INPUT name="ITEM_0" id="ITEM_0" type="TEXT" value="<%=ITEM%>"
			size="15" MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.ITEM_0.value.length > 0)){validateProduct(0);}">
		<a href="#" onClick="javascript:popUpWin('list/multiItemList.jsp?ITEM='+form.ITEM_0.value+'&INDEX=0');"> <img src="images/populate.gif" border="0" /> </a>
		
		</td>
		<td width="12%">
				<INPUT name="ITEMDESC_0"  id="ITEMDESC_0" type="TEXT"
			value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="20" MAXLENGTH=100  readonly="readonly"  class="inactivegry" >
            
				</td>
			<TD width="12%"><INPUT name="ORDERNO_0" type="TEXT" value="<%=ORDERNO%>"
			size="15" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.BATCH_0.focus();}">
			
				<td width="10%">
			<INPUT name="BATCH_0"   id="BATCH_0" type="TEXT" value="<%=BATCH%>" size="15"
			MAXLENGTH=40 onkeypress="if((event.keyCode=='13') && ( document.form.BATCH_0.value.length > 0)){validateBatch(0);}">
			
				</td>
				<TD width="5%"><INPUT name="QTY_0"  id="QTY_0"  type="TEXT" value="1" size="5"
			MAXLENGTH=50   
			onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length > 0)){validateQuantity();}">
			</TD>
				<td width="12%"><INPUT name="REMARK_0" id="REMARK_0" type="TEXT" value="<%=REMARK%>"
			size="20" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.REASONCODE_0.focus();}"> </a>
        </td>
       
			<td width="15%">
			<INPUT name="REASONCODE_0"   id="REASONCODE_0"  type="TEXT" value="<% if(REASONCODE.length() > 0) { out.print(REASONCODE); }else{out.print("NOREASONCODE");}%>"
			size="20" MAXLENGTH=80 onkeypress="if((event.keyCode=='13')) {document.form.FROMLOC_0.focus();}"> 
			<a href="#" onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form.ITEM_0.value+'&INDEX=0');"> <img src="images/populate.gif" border="0" /> </a>
			
			<TD width="12%"><INPUT name="FROMLOC_0" type="TEXT" value="<%=FROMLOC%>"
			size="15" MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.FROMLOC_0.value.length > 0)){ document.form.SubmitButton.focus();}"> 
			 <a href="#" onClick="javascript:popUpWin('list/loc_list_domultitransfrom.jsp?FROMLOC='+form.FROMLOC_0.value+'&INDEX=0');"> <img src="images/populate.gif" border="0" /> 
			 </a></TD>
			
			</TD>	
		</tr>

		</table>
	   	<table align="center" width="85%" border="0">
			<tr>
				<td><INPUT type="button" value="ADD NEW RETURNS" onclick="addRow();" /> <INPUT type="button"
					value="REMOVE LAST ADDED RETURNS"
					onclick="deleteRow('MULTI_CUSTOMER_RETURNS');" /> <INPUT type="hidden"
					name="DYNAMIC_RETURNS_SIZE"></td>
			</tr>
		</table>
	   
	   </td>
	   
		</tr>
		
	</td></tr>
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
function processSameMerchant(){
	var table = document.getElementById("MULTI_CUSTOMER_RETURNS");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("REASONCODE_0").value;
	for(var index = 0; index<rowCount; index++) {
		var merchant = document.getElementById("REASONCODE_"+index);

		if(document.form.ALWAYS_SAME_MERCHANT.checked){
		merchant.value = defaultValue;
		merchant.readOnly=true;
		}
		else
		{
			merchant.value = "NOREASONCODE";
			merchant.readOnly=false;
		}
		
	}
}


function processSameLocation(){
	var table = document.getElementById("MULTI_CUSTOMER_RETURNS");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("FROMLOC_0").value;
	for(var index = 1; index<rowCount; index++) {
		
		var pickQty = document.getElementById("FROMLOC_"+index);
		if(document.form.ALWAYS_SAME_LOCATION.checked){
		pickQty.value = defaultValue;
		}
		else
		{
			pickQty.value = "";
		}
		
	}
}

function addRow() {
	var table = document.getElementById("MULTI_CUSTOMER_RETURNS");
	var rowCount = table.rows.length;
	
	var serialselection = false;
	var sameMerchantUse = false;
	var sameLocaionUse = false;
	

	if(document.form.ALWAYS_SAME_MERCHANT.checked){
		sameMerchantUse = true;
	} 
	if(document.form.ALWAYS_SAME_LOCATION.checked){
	   sameLocaionUse  = true;
	}
	var row = table.insertRow(rowCount);
	
	var merchname = document.getElementById("REASONCODE_0").value;

	
	var itemCell = row.insertCell(0);
	var itemCellText =  "<INPUT name=\"ITEM_"+rowCount+"\"   ";
	itemCellText = itemCellText+ " id=\"ITEM_"+rowCount+"\"  type =\"TEXT\" size=\"15\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateProduct("+rowCount+");}\" MAXLENGTH=\"80\">";
	itemCellText = itemCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('list/multiItemList.jsp?ITEM='+form.ITEM"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	itemCell.innerHTML = itemCellText;
	
	
	
	

	var itemdescCell = row.insertCell(1);
	var itemdescCellText =  "<INPUT name=\"ITEMDESC_"+rowCount+"\" ";
	itemdescCellText = itemdescCellText+ " id=\"ITEMDESC_"+rowCount+"\" type = \"TEXT\" class=\"inactivegry\" size=\"20\"   MAXLENGTH=\"80\">";
	itemdescCell.innerHTML = itemdescCellText;



	var claimCell = row.insertCell(2);
	var claimCellText ="<INPUT name=\"ORDERNO_"+rowCount+"\" type=\"TEXT\" id=\"ORDERNO_"+rowCount+"\"";
	claimCellText=claimCellText+" size=\"15\" MAXLENGTH=\"80\" > ";
	claimCell.innerHTML=	claimCellText;
	
	
	var batchCell = row.insertCell(3);
	batchCell.innerHTML = "<INPUT name=\"BATCH_"+rowCount+"\" id=\"BATCH_"+rowCount+"\"  type = \"TEXT\" size=\"15\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch("+rowCount+");}\"  MAXLENGTH=\"40\">&nbsp;";
	
	var receivingQtyCell = row.insertCell(4);
	var receiveQtyText = "<INPUT name=\"QTY_"+rowCount+"\" ";
		//if(serialselection){
			receiveQtyText = receiveQtyText + " value=\"1\"  ";
		//}
		receiveQtyText = receiveQtyText + " id=\"QTY_"+rowCount+"\" type = \"TEXT\"    size=\"5\"   MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){document.getElementById(\'REMARK_"+rowCount+"').focus();}\"  >";
		
	receivingQtyCell.innerHTML = receiveQtyText;
		
	var remarksCell = row.insertCell(5);
	var remarkCellText ="<INPUT name=\"REMARK_"+rowCount+"\" type=\"TEXT\" id=\"REMARK_"+rowCount+"\"";
	remarkCellText=remarkCellText+" size=\"20\" MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13')){document.getElementById(\'REASONCODE_"+rowCount+"').focus();}\"  >";
	remarksCell.innerHTML=	remarkCellText;

	


	
	var expiredateCell = row.insertCell(6);
	var merchantCellText = "<INPUT name=\"REASONCODE_"+rowCount+"\" id=\"REASONCODE_"+rowCount+"\" type = \"TEXT\"  size=\"20\"  ";
	if(sameMerchantUse) 
	merchantCellText = merchantCellText +"value=\""+merchname+"\" readonly ";

	if(!sameMerchantUse) 
		merchantCellText = merchantCellText +"value=\"NOREASONCODE\" ";

	
	merchantCellText = merchantCellText+" MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13')){document.getElementById(\'FROMLOC_"+rowCount+"').focus();}\"  >";
	//merchantCellText = merchantCellText + "<a href=\"#\" onClick=\"javascript:popUpWin('miscreasoncodemulti.jsp?ITEMNO='+form.ITEM"+'_'+rowCount+".value);\"><img src=\"images/populate.gif\" border=\"0\"></a>";

	merchantCellText = merchantCellText + "<a href=\"#\" onClick=\"javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form.ITEM"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	expiredateCell.innerHTML = merchantCellText;

	//location
	var firstElementLocationValue = document.getElementById("FROMLOC_0").value;
		
	var locationCell = row.insertCell(7);
	var locationCellText =  "<INPUT name=\"FROMLOC_"+rowCount+"\"   ";
	if(sameLocaionUse){
		locationCellText = locationCellText+ "value=\""+firstElementLocationValue+"\" readonly ";
	}
	locationCellText = locationCellText+ " id=\"FROMLOC_"+rowCount+"\"  type =\"TEXT\" size=\"15\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateProduct("+rowCount+");}\" MAXLENGTH=\"80\">";
	locationCellText = locationCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('list/loc_list_domultitransfrom.jsp?FROMLOC='+form.FROMLOC"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	locationCell.innerHTML = locationCellText;





	/*var locationCell = row.insertCell(0);
	var locationCellText =  "<INPUT name=\"LOC_"+rowCount+"\" ";
	if(sameLocaionUse){
		locationCellText = locationCellText+ "value=\""+firstElementLocationValue+"\" readonly ";
	}
	locationCellText = locationCellText+ " id=\"LOC_"+rowCount+"\" type = \"TEXT\" size=\"15\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation("+rowCount+");}\" MAXLENGTH=\"80\">";
	if(!sameLocaionUse) {
		locationCellText = locationCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('loc_list_MultiReceivewms.jsp?LOC='+form.LOC"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	}
    locationCell.innerHTML = locationCellText;*/



	
	
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

function validateProduct(index) {
	var productId = document.getElementById("ITEM"+"_"+index).value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product ID!");
		document.getElementById("ITEM"+"_"+index).focus();
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
						document.getElementById("ORDERNO"+"_"+index).focus();
					} else {
						alert("Not a valid product");
						document.getElementById("ITEM"+"_"+index).value = "";
						document.getElementById("ITEM"+"_"+index).focus();
					}
				}
			});
		}
	}

function validateBatch(index) {
	var orderno = document.getElementById("ORDERNO"+"_"+index).value;
	var batch = document.getElementById("BATCH"+"_"+index).value;
	var productId = document.getElementById("ITEM"+"_"+index).value;
			if (batch == "" || batch.length == 0) {
				alert("Enter Batch!");
				document.getElementById("BATCH"+"_"+index).focus();
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
								document.getElementById("REMARK"+"_"+index).focus();
							} else {
								alert("Not a valid Batch");
								document.getElementById("BATCH"+"_"+index).value = "";
								document.getElementById("BATCH"+"_"+index).focus();
								
							}
						}
					});
				}
			
		
}


	
	
	
</script>
<%@ include file="footer.jsp"%>
