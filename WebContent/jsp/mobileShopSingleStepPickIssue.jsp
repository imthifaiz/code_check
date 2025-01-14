
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<html>
<%
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>

<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Pick/Issue By OutBound Order</title>
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
       String   ORDERNO    = "",ITEMNO   = "", ITEMDESC  = "",UOM="",
       LOC   = "" , CHECKQTY="",BATCH  = "", REF   = "",ORDERLNO,
       QTY = "",RECEIVEQTY="",CUSTNAME="",ORDERQTY="",INVQTY="",PICKINGQTY="",PICKEDQTY="",
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="",AVAILABLEQTY="";
              
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
       REF = strUtils.fString(request.getParameter("REMARKS"));
       ORDERQTY = strUtils.formatNum(strUtils.fString(request.getParameter("ORDERQTY")));
       
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       PICKEDQTY = strUtils.formatNum(strUtils.fString(request.getParameter("PICKEDQTY")));
       AVAILABLEQTY = strUtils.fString(request.getParameter("AVAILABLEQTY"));
       ArrayList list = _DoHdrDAO.getOutBoundOrderCustamerDetailsByWMS(plant,ORDERNO);
       ItemMstDAO itemmstdao = new ItemMstDAO();
       itemmstdao.setmLogger(mLogger);
       UOM = itemmstdao.getItemUOM(plant,ITEMNO);
     
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
           TELNO=(String)m1.get("hpno");
           EMAIL=(String)m1.get("email");
           ADD1=(String)m1.get("add1");
           ADD2=(String)m1.get("add2");
           ADD3=(String)m1.get("add3");
       // }
      }
     
    }

%>
<%@ include file="body.jsp"%>
<body  onload="alert();" >
<FORM name="form" method="post" action="/track/DoPickingServlet?">
<br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Pick/Issue By OutBound Order</FONT>&nbsp;</TH>

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
		<!--<TH WIDTH="8%" ALIGN="Right">Contact Name:</TH>-->
                <TH WIDTH="8%" ALIGN="Right"></TH>
		<TD width="37%"><INPUT name="CONTACTNAME" class="inactivegry" value="<%=CONTACTNAME%>" type="hidden" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Product Desc &nbsp;:</TH>
		<TD width="42%"><INPUT name="ITEMDESC" type="TEXT"
			value="<%=ITEMDESC%>" size="35" class="inactivegry" readonly
			MAXLENGTH="80" /></TD>
		<TH WIDTH="8%" ALIGN="Right">Telephone :</TH>
		<TD width="37%"><INPUT name="TELNO" class="inactivegry"
			value="<%=TELNO%>" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT">Order Qty:</TH>
		<TD WIDTH="42%"><INPUT name="ORDERQTY" type="TEXT"
			value="<%=ORDERQTY%>" size="15" MAXLENGTH="80" class="inactivegry"
			readonly />&nbsp;
			<INPUT name="UOM" type="TEXT"
			value="<%=UOM%>" size="12" MAXLENGTH="10" class="inactivegry"
			readonly />
			</TD>
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
	<TR>
	 	<TH WIDTH="13%" ALIGN="RIGHT"></TH>
		<TD width="42%"></TD>  
		<TH WIDTH="8%" ALIGN="Right">Building :</TH>
		<TD width="37%"><INPUT name="ADD2" class="inactivegry"
			value="<%=ADD2%>" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="RIGHT"></TH>
		<TD width="42%">
		  </TD>
		<TH WIDTH="8%" ALIGN="Right">Street :</TH>
		<TD width="37%"><INPUT name="ADD3" value="<%=ADD3%>"
			class="inactivegry" type="TEXT" size="35" MAXLENGTH=80 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="Right"></TH>
		<TD width="42%"></TD>
		<TH WIDTH="8%">&nbsp;</TH>
		<TD width="37%">&nbsp;</TD>
	</TR>
	<TR>
		<TH WIDTH="13%" ALIGN="Right"></TH>
		<TD width="42%">
		
		</TD>
		<TH WIDTH="8%">&nbsp;</TH>
		<TD width="37%">&nbsp;</TD>
	</TR>

	<TR>
		<TH WIDTH="13%" ALIGN="Right">&nbsp;&nbsp;</TH>
		<TD width="42%">
		</TD>
		<TH WIDTH="8%">&nbsp;</TH>
		<TD width="37%">&nbsp;</TD>
	</TR>

	<TR>
		<TD colspan="4"><br>
		<center><b>Always use the same for the following :</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="Checkbox" name="SERIAL_SELECTION" id="SERIAL_SELECTION"  value="SERIAL_SELECTION" onClick="processSerialSelection();"></input>  
		 Serial Picking
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_LOCATION" id="ALWAYS_SAME_LOCATION" value="ALWAYS_SAME_LOCATION" onClick="processSameLocation();"></input> Location
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="Checkbox" name="ALWAYS_SAME_REMARKS" id="ALWAYS_SAME_REMARKS" value="ALWAYS_SAME_REMARKS" onClick="processSameRemarks();"></input>Remarks
                </center>
		<br>
		<table align="center" width="85%" border="0" id="MULTIPLE_PICKING">
			<tr>
				<td width="19%"><b>Loc : </b><INPUT name="LOC_0" id="LOC_0" type="TEXT"
					value="<%=LOC%>" size="20"
				onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}"	
					MAXLENGTH=80> <a href="#"
			onClick="javascript:popUpWin('OutBoundMultiplePickingLoc.jsp?ITEMNO='+form.ITEMNO.value+'&LOC_0='+form.LOC_0.value+'&INDEX='+'0');">
		<img src="images/populate.gif" border="0" /> </a></td>
				<td width="1%">&nbsp;</td>
				<td width="20%"><b>Batch : </b><INPUT name="BATCH_0" id="BATCH_0" type="TEXT"
					value="<%=BATCH%>" size="20"  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value,0);}"
					MAXLENGTH=40> <a href="#"
			
			onClick="javascript:popUpWin('OutBoundMultiPickingBatch.jsp?ITEMNO='+form.ITEMNO.value+'&LOC_0='+form.LOC_0.value+'&BATCH_0='+form.BATCH_0.value+'&INDEX='+'0');">
		<img src="images/populate.gif" border="0" /> </a></td>
		<td width="1%">&nbsp;</td>
				<td width="18%"><b>Available Qty : </b><INPUT name="AVAILABLEQTY_0" id="AVAILABLEQTY_0"
					value="<%= QTY%>" type="TEXT" size="10" MAXLENGTH="80"\></td>
				<td width="1%">&nbsp;</td>
				
				<td width="16%"><b>Picking Qty : </b><INPUT name="PICKINGQTY_0" id="PICKINGQTY_0"
					value="<%= PICKINGQTY%>" type="TEXT" size="10" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value,0);}"  MAXLENGTH="80"\></td>
				<td width="1%">&nbsp;</td>
				<td width="20%"><b>Remarks : </b><INPUT name="REMARKS_0" id="REMARKS_0"
					value="<%= REF%>" type="TEXT" size="10" MAXLENGTH="80">
				</td>
			</tr>

		</table>
		<br></br>
		<table align="center" width="85%" border="0">
			<tr>
				<td><INPUT type="button" value="ADD NEW PICKING" onclick="addRow();" /> <INPUT type="button"
					value="REMOVE LAST ADDED PICKING"
					onclick="deleteRow('MULTIPLE_PICKING');" /> <INPUT type="hidden"
					name="DYNAMIC_PICKING_SIZE"></td>
			</tr>
		</table>
		</TD>
	</TR>
	<TR align="center" >
		<TD WIDTH="35%" COLSPAN=2>
		
		
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      
		<INPUT class="Submit" type="BUTTON" value="Cancel"
			onClick="window.location.href='mobileShoppingFullfillment.jsp?action=View&DONO='+form.ORDERNO.value+'&PLANT='+form.PLANT.value " />&nbsp;&nbsp;
		<input type="button" value="Pick/Issue Confirm" name="actionSubmit"
			onClick="if(validatePO(document.form)) {submitForm();}" /></TD>
		<TH WIDTH="8%"></TH>
		
		
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
</body>
</HTML>
<script>
function keCache(){
	//alert(event.keyCode);
	if(event.keyCode==8 || event.keyCode==13)
	{
	window.history.forward(1);
	}
}
//document.onkeydown = keCache();
function addRow() {
	var table = document.getElementById("MULTIPLE_PICKING");
	var rowCount = table.rows.length;

	var serialselection = false;
	var sameLocaionUse = false;
        var sameRemarksUse= false;
       var  allchecked=false;
	if(document.form.SERIAL_SELECTION.checked){
		   serialselection=true;
	   }
	   if(document.form.ALWAYS_SAME_LOCATION.checked){
			sameLocaionUse=true;
	   }
          if(document.form.ALWAYS_SAME_REMARKS.checked){
			sameRemarksUse=true;
	   }

        if(serialselection && sameLocaionUse && sameRemarksUse ){
        allchecked=true;
        }
	var row = table.insertRow(rowCount);
	var firstElementLocationValue = document.getElementById("LOC_0").value;
	var firstElementremarksValue = document.getElementById("REMARKS_0").value;
	var locationCell = row.insertCell(0);
		var locationCellText =  "<b>Loc : </b><INPUT name=\"LOC_"+rowCount+"\" ";
		if(sameLocaionUse){
			locationCellText = locationCellText+ "value=\""+firstElementLocationValue+"\" readonly   onClick=\"javascript:keCache();\" ";
		}
		locationCellText = locationCellText+ " id=\"LOC_"+rowCount+"\" type = \"TEXT\" size=\"20\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value, "+rowCount+");}\" MAXLENGTH=\"80\">";
	//	if(!sameLocaionUse) {
			locationCellText = locationCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('OutBoundMultiplePickingLoc.jsp?ITEMNO='+form.ITEMNO.value+'&LOC_"+rowCount+"='+form.LOC_"+rowCount+".value+'&INDEX="+rowCount+"');\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value, "+rowCount+");}\"><img src=\"images/populate.gif\" border=\"0\"></a>";
		//}
	locationCell.innerHTML = locationCellText;
	
	var firstEmptyCell = row.insertCell(1);
	firstEmptyCell.innerHTML = "&nbsp;";
	
        var batchCell = row.insertCell(2);
	batchCell.innerHTML = "<b>Batch : </b><INPUT name=\"BATCH_"+rowCount+"\" id=\"BATCH_"+rowCount+"\" value=\"\" type = \"TEXT\" size=\"20\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value, "+rowCount+");}\"  MAXLENGTH=\"40\">&nbsp; "+
	"<a href=\"#\" onClick=\"javascript:popUpWin('OutBoundMultiPickingBatch.jsp?ITEMNO='+form.ITEMNO.value+'&LOC_"+rowCount+"='+form.LOC_"+rowCount+".value+'&BATCH_"+rowCount+"='+form.BATCH_"+rowCount+".value+'&INDEX="+rowCount+"');\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value, "+rowCount+");}\"><img src=\"images/populate.gif\" border=\"0\"></a>";

	var secondEmptyCell = row.insertCell(3);
	secondEmptyCell.innerHTML = "&nbsp";
	var availableqtyCell = row.insertCell(4);
	availableqtyCell.innerHTML ="<b>Available Qty : </b><INPUT name=\"AVAILABLEQTY_"+rowCount+"\" id=\"AVAILABLEQTY_"+rowCount+"\" type = \"TEXT\"  size=\"10\"  MAXLENGTH=\"80\" \> ";
	var thirdemptycell = row.insertCell(5);
	thirdemptycell.innerHTML = "&nbsp";
	var receivingQtyCell = row.insertCell(6);
	var receiveQtyText = "<b>Picking Qty : </b><INPUT name=\"PICKINGQTY_"+rowCount+"\" ";
		if(serialselection){
			receiveQtyText = receiveQtyText + " value=\"1\"  ";
		}
		receiveQtyText = receiveQtyText + " id=\"PICKINGQTY_"+rowCount+"\" type = \"TEXT\"  size=\"10\"  MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value, "+rowCount+");}\"  >";
	receivingQtyCell.innerHTML = receiveQtyText;
	var thirdEmptyCell = row.insertCell(7);
	thirdEmptyCell.innerHTML = "&nbsp;";
	var remarksCell = row.insertCell(8);
	var remarkCellText ="<b>Remarks : </b><INPUT name=\"REMARKS_"+rowCount+"\" ";
        if(sameRemarksUse){
			remarkCellText = remarkCellText+ "value=\""+firstElementremarksValue+"\" readonly ";
	}
	remarkCellText=remarkCellText+" type=\"TEXT\" id=\"REMARKS_"+rowCount+"\" size=\"10\" MAXLENGTH=\"80\" > ";
	remarksCell.innerHTML=	remarkCellText;	  
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
			alert("Can not remove the default Picking");
		} else {
			table.deleteRow(rowCount);
		}
	} catch (e) {
		alert(e);
	}
}
function submitForm(){
	   var frmRoot=document.form;
	   var table = document.getElementById("MULTIPLE_PICKING");
	   var rowCount = table.rows.length;
	   document.form.DYNAMIC_PICKING_SIZE.value = rowCount;
	   var totalQtyPick=0;
	   processSelection();
	   for(var index = 0; index<rowCount; index++) {
		    var locationText = document.getElementById("LOC_"+index);
			var pickingQty = document.getElementById("PICKINGQTY_"+index);
			var batchText = document.getElementById("BATCH_"+index);

 if(document.form.SERIAL_SELECTION.checked){
                     for(var j=0;j<rowCount;j++){
                if(index!=j){

                        var chkbatch = document.getElementById("BATCH"+"_"+j);
                         if(batchText.value==chkbatch.value){
                          alert("Duplicate batch Scanned !");
                 chkbatch.select();
                   return false;
               }
               }
                }
}
			if(locationText.value== "" || locationText.value.length==0 ) {
				alert("Please Enter LOC!");
				locationText.focus();
				locationText.style.backgroundColor = "#FFE5EC";
				return false;
			}
			if(batchText.value== "" || batchText.value.length==0 ) {
				alert("Please Enter Valid batch!");
				batchText.style.backgroundColor = "#FFE5EC";
				batchText.focus();
				return false;
			}

			if(removeCommas(pickingQty.value)== "" || removeCommas(pickingQty.value).length==0 || removeCommas(pickingQty.value)<=0) {
				alert("Please Enter Valid Qty!");
				pickingQty.style.backgroundColor = "#FFE5EC";
				pickingQty.focus();
				return false;
			}else{
				totalQtyPick = (totalQtyPick*1)+ (pickingQty.value*1);
				totalQtyPick = totalQtyPick * 1;
				//pickingQty.style.backgroundColor = "#FFFFFF";
			}
			
	   }
	   for(var index = 0; index<rowCount; index++) {
		   var pickQty = document.getElementById("PICKINGQTY_"+index);
		   var availQty = document.getElementById("AVAILABLEQTY_"+index);
		   
		   var pkqty = parseFloat(removeCommas(pickQty.value)).toFixed(3);
		   var avqty = parseFloat(removeCommas(availQty.value)).toFixed(3);
		   pkqty = round_float(pkqty,3);
		   avqty = round_float(avqty,3);
		  
		   if(pkqty>avqty)
		   {
			   alert("Entered Quantity is greater than Available Qty");
			   return false;
		   }
		   if (isNumericInput(removeCommas(pickQty.value)) == false) {
				alert("Entered Quantity is not a valid Qty!");
				return false;
			}
		   
		   //pickQty.style.backgroundColor = "#FFE5EC";ORDERQTY,PICKEDQTY
	   }
	   var orderedQty = document.getElementById("ORDERQTY").value;
	   var pickedQty = document.getElementById("PICKEDQTY").value;
	   orderedQty = removeCommas(orderedQty);
	   pickedQty = removeCommas(pickedQty);
	   if((totalQtyPick*1)+(pickedQty*1)>(orderedQty*1)){
		   for(var index = 0; index<rowCount; index++) {
			   var receiveQty = document.getElementById("PICKINGQTY_"+index);
			   receiveQty.style.backgroundColor = "#FFE5EC";
		   }
		   alert("Exceeded the Orderd Qty. Please check all the Qtys.!");
		   return false;
	   }else{
		   //document.form.PICKINGQTY.value = totalQtyPick;  
		   document.form.action="/track/DoPickingServlet?action=Pick/IssueForMobileShop";
		   document.form.submit();
		   return true;
	   }
	  
       
}

function validateLocation(locId, index) {
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC_"+index).focus();
	}else{
		var urlStr = "/track/OutboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
                USERID : "<%=sUserId%>",
				PLANT : "<%=plant%>",
				ITEMNO : "<%=ITEMNO%>",
				ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						document.getElementById("BATCH_"+index).value = "";
						document.getElementById("BATCH_"+index).focus();
						
						
					} else {
						alert("Not a valid Location");
						document.getElementById("LOC_"+index).value = "";
						document.getElementById("LOC_"+index).focus();
					}
				}
			});
		}
	}

function validateBatch(batch,index) {
	var locId = document.getElementById("LOC_"+index).value;
	var itemNo = document.form.ITEMNO.value;
	var batch = document.getElementById("BATCH_"+index).value;
        var serialselection = false;
	var sameLocaionUse = false;
        var sameRemarksUse= false;
        var  allchecked=false;
	if(document.form.SERIAL_SELECTION.checked){
		   serialselection=true;
	   }
	   if(document.form.ALWAYS_SAME_LOCATION.checked){
			sameLocaionUse=true;
	   }
          if(document.form.ALWAYS_SAME_REMARKS.checked){
			sameRemarksUse=true;
	   }

        if(serialselection && sameLocaionUse && sameRemarksUse ){
        allchecked=true;
        }

	if(batch=="" || batch.length==0 ) {
		alert("Enter Batch!");
		document.getElementById("BATCH_"+index).focus();
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
						document.getElementById("BATCH_"+index).value = resultVal.BATCH;
						//document.form.QTY.value=resultVal.QTY;
						
						document.getElementById("AVAILABLEQTY_"+index).value=resultVal.QTY;
						if(index>0)
						 {
                                                    var totalpickqty = getSumByLocAndBatch(index);	
                                                    totalpickqty = round_float(totalpickqty,3);				
                                                    document.getElementById("AVAILABLEQTY_"+index).value=round_float(resultVal.QTY-totalpickqty,3);
						  }	
						//document.getElementById("AVAILABLEQTY_"+index).focus();
 
                                              if(allchecked){
                                                 addRow();
                                               }else{
                                                document.getElementById("PICKINGQTY"+"_"+index).focus();
                                               }

					} else {
						alert("Not a valid Batch");
						document.getElementById("BATCH_"+index).value = "";
						document.getElementById("AVAILABLEQTY_"+index).value="";
						document.getElementById("BATCH_"+index).focus();
					}
				}
			});
		}
	}

function validateQuantity(qty,index) {
	var qty = document.getElementById("PICKINGQTY_"+index).value;
	qty = removeCommas(qty);
	if (qty == "" || qty.length == 0) {
		alert("Enter Quantity!");
		document.getElementById("PICKINGQTY_"+index).focus();
	} else {
		if (isNumericInput(qty) == false) {
			alert("Entered Quantity is not a valid Qty!");
		} else {
			var availableQty = document.getElementById("AVAILABLEQTY_"+index).value;
			availableQty =removeCommas(availableQty);
			availableQty = availableQty*1;
			//alert(qty);
			if(availableQty<qty){
				alert("Entered Quantity is greater than the Available Qty!");
				document.getElementById("PICKINGQTY_"+index).value="";
				document.getElementById("PICKINGQTY_"+index).focus();
			}else{
				document.getElementById("REMARKS_"+index).value="";
				document.getElementById("REMARKS_"+index).focus();
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
function processSelection()
{	
	if(document.form.SERIAL_SELECTION.checked){
		 processSerialSelection();
	   }
	   if(document.form.ALWAYS_SAME_LOCATION.checked){
		   processSameLocation();
	   }
		  	  
}

function getavailqty(qty,index)
{
	var availqty=qty;
	removeCommas(availqty);
	if(index>0){
		
		var prevIndex = parseInt(index);
		
		var totalpickqty = getSumByLocAndBatch(index);
		//alert(totalpickqty);
	//var prevavailqty= document.getElementById("AVAILABLEQTY_"+prevIndex-1);
	var curavailqty = document.getElementById("AVAILABLEQTY_"+index).value;
	curavailqty= removeCommas(curavailqty);
	curavailqty = round_float(curavailqty,3);
	document.getElementById("AVAILABLEQTY_"+index).value = addCommas(curavailqty-totalpickqty);
	}	
}

function getSumByLocAndBatch(index)
{
	var cnt = parseFloat(index);
	var loc0=document.getElementById("LOC_"+index).value;
	var batch0 = document.getElementById("BATCH_"+index).value;
	var qty=0;
	for(var i=0;i<cnt;i++)
	{
		var loc = document.getElementById("LOC_"+i).value;
		var batch = document.getElementById("BATCH_"+i).value;
		if(loc0==loc && batch0==batch)
		{
			var pickqty = document.getElementById("PICKINGQTY_"+i).value;
			
			pickqty = removeCommas(pickqty);
			
			qty = qty + parseFloat(pickqty);
			
				}
		qty = round_float(qty,3);
	}
	return qty;
}
function processSerialSelection() {
	var table = document.getElementById("MULTIPLE_PICKING");
	var rowCount = table.rows.length;
	for(var index = 0; index<rowCount; index++) {
		var pickQty = document.getElementById("PICKINGQTY_"+index);
                var availQty = document.getElementById("AVAILABLEQTY_"+index);
		if(document.form.SERIAL_SELECTION.checked){

				pickQty.value = 1;
		      pickQty.readOnly = true;
availQty.readOnly=true;
		}
		else
		{
			pickQty.value = "";	 
                        pickQty.readOnly = false;
                        availQty.readOnly=true;
		}
				
	}
}
function processSameLocation(){
	var table = document.getElementById("MULTIPLE_PICKING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("LOC_0").value;
	for(var index = 1; index<rowCount; index++) {
		var locind = document.getElementById("LOC_"+index);
		 if(document.form.ALWAYS_SAME_LOCATION.checked){
			 locind.value = defaultValue; }
		 else{
			 locind.value = "";
			 locind.readOnly = false;
		 }
		
		
	}
}

function processSameRemarks(){
	var table = document.getElementById("MULTIPLE_PICKING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("REMARKS_0").value;
	for(var index = 1; index<rowCount; index++) {
		var remarks = document.getElementById("REMARKS_"+index);
		if(document.form.ALWAYS_SAME_REMARKS.checked){
			remarks.value = defaultValue;}
		else
		{
			remarks.value="";
			remarks.readOnly = false;
		}
		
		
	}
}
document.form.LOC_0.focus();
</script>
<%@ include file="footer.jsp"%>
