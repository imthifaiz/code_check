<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" 	src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Pick/Issue By Transfer Order</title>
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
		alert("Please Enter Transfer Order No!");
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
        ToHdrDAO _ToHdrDAO = new ToHdrDAO();
        _ToHdrDAO.setmLogger(mLogger);
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String plant=(String)session.getAttribute("PLANT");
       String  fieldDesc="",TEMP_TO="";
       String   ORDERNO    = "",ITEMNO   = "", ITEMDESC  = "",UOM="",
       FROMLOC   = "" ,TOLOC="",TEMPLOC="", CHECKQTY="",BATCH  = "", REF   = "",ORDERLNO,
       QTY = "",RECEIVEQTY="",CUSTNAME="",ORDERQTY="",INVQTY="",PICKEDQTY="",PICKINGQTY="",
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="",TRANSACTIONDATE="";
       ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=strUtils.fString(request.getParameter("ORDERLNO"));
       CUSTNAME=strUtils.fString(request.getParameter("CUSTNAME"));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")));
       FROMLOC = strUtils.fString(request.getParameter("FROMLOC"));
       TOLOC = strUtils.fString(request.getParameter("TOLOC"));
       TEMPLOC = strUtils.fString(request.getParameter("TEMPLOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       PICKINGQTY = strUtils.fString(request.getParameter("PICKINGQTY"));
       INVQTY = strUtils.fString(request.getParameter("QTY"));
       REF = strUtils.fString(request.getParameter("REF"));
       QTY = StrUtils.formatNum(strUtils.fString(request.getParameter("QTY")));
       PICKEDQTY = StrUtils.formatNum(strUtils.fString(request.getParameter("PICKEDQTY")));
       ORDERQTY = StrUtils.formatNum(strUtils.fString(request.getParameter("ORDERQTY")));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       TRANSACTIONDATE = strUtils.fString(request.getParameter("TRANSACTIONDATE"));
       DateUtils _dateUtils = new DateUtils();
       String curDate =_dateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
       TEMP_TO="TEMP_TO_"+FROMLOC;
       ItemMstDAO itemstdao = new ItemMstDAO();
       itemstdao.setmLogger(mLogger);
       UOM = itemstdao.getItemUOM(plant,ITEMNO);
      
      
       ArrayList list=_ToHdrDAO.getTransferAssigneeDetailsByWMS(plant, ORDERNO);
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
       
          ArrayList list1=_ToHdrDAO.getTransferAssigneeDetailsByWMS(plant, ORDERNO);
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
<FORM name="form" method="post" action="/track/TransferOrderServlet?"  >
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Pick/Issue By Transfer Order</FONT>&nbsp;
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
         <TH WIDTH="13%" ALIGN="Right" >Assignee Name : </TH>
         <TD width="42%"><INPUT name="CUSTNAME" class="inactivegry" type = "TEXT" value="<%=su.forHTMLTag(CUSTNAME)%>" size="35"  MAXLENGTH=80 readonly></TD>
         <TD width="8%"></TD>
          <TH WIDTH="37%" ALIGN="left" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Assignee&nbsp; Details </TH>
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
           <INPUT name="ORDERQTY" type="TEXT" value="<%=ORDERQTY%>" size="15" MAXLENGTH="25" class="inactivegry" readonly/>
           &nbsp;<INPUT name="UOM" type="TEXT" value="<%=UOM%>" size="12" MAXLENGTH="15" class="inactivegry" readonly/>
         </TD>
         <TH WIDTH="8%" ALIGN="Right" >Email : </TH>
         <TD width="37%"><INPUT name="EMAIL" value="<%=EMAIL%>" class="inactivegry" type = "TEXT"  size="35"  MAXLENGTH=80 readonly></TD>
    </TR>
     <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >Picked Qty:</TH>
         <TD width="42%">
           <INPUT name="PICKEDQTY" type="TEXT" class="inactivegry" readonly value="<%=PICKEDQTY%>" size="35" MAXLENGTH="80"/>
           </TD>
         <TH WIDTH="8%" ALIGN="Right" >Unit&nbsp;No : </TH>
         <TD width="37%"><INPUT name="ADD1"  class="inactivegry" value="<%=ADD1%>"  type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD> 
    </TR>
     <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >From Loc:</TH>
         <TD width="42%">
           <INPUT name="FROMLOC_0" id="FROMLOC_0" type="TEXT" value="<%=FROMLOC%>" size="35" class="inactivegry" readonly MAXLENGTH="80"/>
         </TD>
      <TH WIDTH="8%" ALIGN="Right" >Building : </TH>
         <TD width="37%"><INPUT name="ADD2"  class="inactivegry" value="<%=ADD2%>"  type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD> 
      </TR>
     <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >Temp&nbsp;Loc&nbsp; :</TH>
         <TD width="42%">
          <INPUT name="TEMPLOC" type="TEXT" value="<%=TEMP_TO%>" size="35" class="inactivegry" readonly MAXLENGTH="80"/>
         </TD>
         <TH WIDTH="8%" ALIGN="Right" >Street : </TH>
         <TD width="37%"><INPUT name="ADD3" value="<%=ADD3%>" class="inactivegry" type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD>
    </TR>
     <TR>
      <TH WIDTH="13%" ALIGN="Right">Remarks&nbsp;&nbsp;:</TH>
      <TD width="42%">
        <INPUT name="REF" type="TEXT" value="<%=REF%>" size="35" MAXLENGTH="80" onkeypress="if((event.keyCode=='13') ){document.form.SubmitForm.focus();}"/>
      </TD>
      <TH WIDTH="8%">&nbsp;</TH>
      <TD width="37%">&nbsp;</TD>
    </TR>
    <TR>
		<TD colspan="4"><br>
		<center><input type="Checkbox" id="SELECTION" name="SELECTION" value="SERIAL_SELECTION" onClick="processSelection();"></input>
		Use Serial Picking
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</center>
		<br>
		<table align="center" width="85%" border="0" id="MULTIPLE_PICKING">
			<tr>
				<td width="19%"><b>To Loc : </b><INPUT name="TOLOC_0" id="TOLOC_0" type="TEXT"
					value="<%=TOLOC%>" size="20"  class="inactivegry"
				onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}"	
					MAXLENGTH=80> </td>
				<td width="1%">&nbsp;</td>
				<td width="20%"><b>Batch : </b><INPUT name="BATCH_0" id="BATCH_0" type="TEXT"
					value="<%=BATCH%>" size="20"  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value,0);}"
					MAXLENGTH=40> <a href="#"
			
			onClick="javascript:popUpWin('OutBoundMultiPickingBatch.jsp?ITEMNO='+form.ITEMNO.value+'&LOC_0='+form.FROMLOC_0.value+'&BATCH_0='+form.BATCH_0.value+'&INDEX='+'0');">
		<img src="images/populate.gif" border="0" /> </a></td>
		<td width="1%">&nbsp;</td>
				<td width="18%"><b>Available Qty : </b><INPUT name="AVAILABLEQTY_0" id="AVAILABLEQTY_0"
					value="<%= QTY%>" type="TEXT" size="10" MAXLENGTH="80"\></td>
				<td width="1%">&nbsp;</td>
				
				<td width="16%"><b>Picking Qty : </b><INPUT name="PICKINGQTY_0" id="PICKINGQTY_0"
					value="<%= PICKINGQTY%>" type="TEXT" size="10" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value,0);}"  MAXLENGTH="80"\></td>
			<td width="6%"></td>
						
				
			</tr>

		</table>
		<br></br>
		<table align="center" width="85%" border="0">
			<tr>
				<td><INPUT type="button" value="ADD NEW PICKING" onclick="addRow();" /> <INPUT type="button"
					value="REMOVE LAST ADDED PICKING"
					onclick="deleteRow('MULTIPLE_PICKING');" /> <INPUT type="hidden"
					name="DYNAMIC_PICKING_SIZE">
					&nbsp;&nbsp;<b>Transaction Date : </b><INPUT name="TRANSACTIONDATE" id="TRANSACTIONDATE"
					value="<%=TRANSACTIONDATE%>" type="TEXT" size="10" MAXLENGTH="80" readonly="readonly">
						<a href="javascript:show_calendar('form.TRANSACTIONDATE');"
			   			onmouseover="window.status='Date Picker';return true;"
			   			onmouseout="window.status='';return true;"> <img
			   			src="images\show-calendar.gif" width="24" height="22" border="0" /></a>
					</td>
		</tr>
		</table>
		</TD>
	</TR>
    <TR>
      <TH WIDTH="13%" ALIGN="Right">&nbsp;</TH>
      <TD width="42%">&nbsp;
      </TD>
      <TH WIDTH="8%">&nbsp;</TH>
      <TD width="37%">&nbsp;</TD>
    </TR>
    
    <TR align="center">
           <TH WIDTH="25%" > </TH>
          <TD WIDTH="50%" align="center" COLSPAN = 2>
             <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='MultipleTransferOrderSummary.jsp?action=MultipleView&TONO='+form.ORDERNO.value+'&PLANT='+form.PLANT.value "/>&nbsp;&nbsp;
             <input type="button" value="Pick/Issue Confirm" name="SubmitForm" onClick="if(validatePO(document.form)){submitFormValues();}"/> 
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
   <INPUT    type="Hidden" name="BFLAG" value="1">
  </TR>
</Table>
</FORM>

</HTML>
<script type="text/javascript">
function addRow() {
	var table = document.getElementById("MULTIPLE_PICKING");
	var rowCount = table.rows.length;

	var serialselection = false;
	var sameLocaionUse = false;
	
		if( document.form.SELECTION.checked == true )
		  {
		 var val = document.form.SELECTION.value;
		  if(val=='SERIAL_SELECTION')
		  {
			  serialselection=true;
		  }
		 
		  }
	
	var row = table.insertRow(rowCount);
	var firstElementLocationValue = document.getElementById("TOLOC_0").value;
	
	var locationCell = row.insertCell(0);
		var locationCellText =  "<b>To Loc : </b><INPUT name=\"TOLOC_"+rowCount+"\" class=\"inactivegry\" readonly ";
		
			locationCellText = locationCellText+ "value=\""+firstElementLocationValue+"\" readonly   onClick=\"javascript:keCache();\" ";
	
		locationCellText = locationCellText+ " id=\"TOLOC_"+rowCount+"\" type = \"TEXT\" size=\"20\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value, "+rowCount+");}\" MAXLENGTH=\"80\">";
	
	locationCell.innerHTML = locationCellText;
	
	var firstEmptyCell = row.insertCell(1);
	firstEmptyCell.innerHTML = "&nbsp;";
	var batchCell = row.insertCell(2);
	batchCell.innerHTML = "<b>Batch : </b><INPUT name=\"BATCH_"+rowCount+"\" id=\"BATCH_"+rowCount+"\" value=\"\" type = \"TEXT\" size=\"20\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value, "+rowCount+");}\"  MAXLENGTH=\"40\">&nbsp; "+
	"<a href=\"#\" onClick=\"javascript:popUpWin('OutBoundMultiPickingBatch.jsp?ITEMNO='+form.ITEMNO.value+'&LOC_"+rowCount+"='+form.FROMLOC_"+rowCount+".value+'&BATCH_"+rowCount+"='+form.BATCH_"+rowCount+".value+'&INDEX="+rowCount+"');\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value, "+rowCount+");}\"><img src=\"images/populate.gif\" border=\"0\"></a>";
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

	var fromlocCell = row.insertCell(7);
	var fromElementLocationValue = document.getElementById("FROMLOC_0").value;
	var fromlocCellText =  "<INPUT name=\"FROMLOC_"+rowCount+"\"   ";
	
	fromlocCellText = fromlocCellText+ "value=\""+fromElementLocationValue+"\" readonly    ";

	fromlocCellText = fromlocCellText+ " id=\"FROMLOC_"+rowCount+"\" type = \"Hidden\" size=\"20\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value, "+rowCount+");}\" MAXLENGTH=\"80\">";

	fromlocCell.innerHTML = fromlocCellText;

  if(serialselection ){
            document.getElementById("BATCH_"+rowCount).focus();
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
function processSerialSelection() {
	var table = document.getElementById("MULTIPLE_PICKING");
	var rowCount = table.rows.length;

	for(var index = 0; index<rowCount; index++) {
		var pickQty = document.getElementById("PICKINGQTY_"+index);
                var availQty = document.getElementById("AVAILABLEQTY_"+index);
		if( document.form.SELECTION.checked == true )
		{
		        pickQty.value = 1;
                        pickQty.readOnly = true;
                        availQty.readOnly=true;
		}
		else
		{
			pickQty.value = "";	
                        pickQty.readOnly = false;
                       
		}
		
	}
}
function processSelection()
{
	     processSerialSelection();
		
}
function getavailqty(qty,index)
{
	var availqty=qty;
	if(index>0){
		
		var prevIndex = parseFloat(index);
		
		var totalpickqty = getSumByLocAndBatch(index);
		//alert(totalpickqty);
	//var prevavailqty= document.getElementById("AVAILABLEQTY_"+prevIndex-1);
	var curavailqty = document.getElementById("AVAILABLEQTY_"+index).value;
	curavailqty = removeCommas(curavailqty);
	document.getElementById("AVAILABLEQTY_"+index).value= round_float(curavailqty-totalpickqty,3);
	}	
}

function getSumByLocAndBatch(index)
{
	var cnt = parseFloat(index);
	var loc0=document.getElementById("TOLOC_"+index).value;
	var batch0 = document.getElementById("BATCH_"+index).value;
	var qty=0;
	for(var i=0;i<cnt;i++)
	{
		var loc = document.getElementById("TOLOC_"+i).value;
		var batch = document.getElementById("BATCH_"+i).value;
		if(loc0==loc && batch0==batch)
		{
			var pickqty = document.getElementById("PICKINGQTY_"+i).value;
			pickqty = removeCommas(pickqty);
			qty = qty + parseFloat(pickqty);
			if(isNaN(qty))
			{
				qty = 0;	
			}
			qty= round_float(qty,3);
		}
	}
	return qty;
}
function submitFormValues(){
	var frmRoot=document.form;
	   var table = document.getElementById("MULTIPLE_PICKING");
	   var rowCount = table.rows.length;
	   document.form.DYNAMIC_PICKING_SIZE.value = rowCount;
	   var totalQtyPick=0;
	   if( document.form.SELECTION.checked == true ){
	   processSelection();}
	   for(var index = 0; index<rowCount; index++) {
		    var locationText = document.getElementById("TOLOC_"+index);
			var pickingQty = document.getElementById("PICKINGQTY_"+index);
			var batchText = document.getElementById("BATCH_"+index);
	     if(document.form.SELECTION.checked){
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
			if(batchText.value== "" || batchText.value.length==0 ) {
				alert("Please Enter Valid batch!");
				batchText.style.backgroundColor = "#FFE5EC";
				batchText.focus();
				return false;
			}else{
				batchText.style.backgroundColor = "#FFFFFF";
			}

			if(removeCommas(pickingQty.value)== "" || removeCommas(pickingQty.value).length==0 || pickingQty.value<=0) {
				alert("Please Enter Valid Qty!");
				pickingQty.style.backgroundColor = "#FFE5EC";
				pickingQty.focus();
				return false;
			}else{
				totalQtyPick = (totalQtyPick*1)+ (pickingQty.value*1);
				totalQtyPick = totalQtyPick * 1;
				pickingQty.style.backgroundColor = "#FFFFFF";
			}
			
	   }
	   for(var index = 0; index<rowCount; index++) {
		   var pickQty = document.getElementById("PICKINGQTY_"+index).value;
		   var availQty = document.getElementById("AVAILABLEQTY_"+index).value;
		   pickQty  =  removeCommas(pickQty);
		   availQty =  removeCommas(availQty);
		   if (isNumericInput(pickQty) == false) {
				alert("Entered Quantity is not a valid Qty!");
				return false;
			}
		   var recvqty = parseInt(pickQty,10);
		   var inavailqty = parseInt(availQty,10);
		  if(recvqty>inavailqty)
		  {
			  alert("Entered Qty greater than Available Qty");
			  return false;
		  }
		  
	   }
	 /*  var orderedQty = document.getElementById("ORDERQTY").value;
	   var pickedQty = document.getElementById("PICKEDQTY").value;
	   orderedQty  =  removeCommas(orderedQty);
	   pickedQty =  removeCommas(pickedQty); */

	   var orderedQty = <%=StrUtils.removeFormat(ORDERQTY)%>;
	   orderedQty = parseFloat(orderedQty).toFixed(3);
	   var pickedQty = <%=StrUtils.removeFormat(PICKEDQTY)%>;
	   pickedQty = parseFloat(pickedQty).toFixed(3);
	     
	   
	   if((totalQtyPick*1)+(pickedQty*1)>(orderedQty*1)){
		   for(var index = 0; index<rowCount; index++) {
			   var receiveQty = document.getElementById("PICKINGQTY_"+index);
			   receiveQty.style.backgroundColor = "#FFE5EC";
		   }
		   alert("Exceeded the Orderd Qty. Please check all the Qtys.!");
		   return false;
	   }else{
		   //document.form.PICKINGQTY.value = totalQtyPick;  
		   
		   document.form.action="/track/TransferOrderServlet?Submit=MultiPick/Issue Confirm";
		   document.form.submit();
		   return true;
	   }
			
	}

function validateBatch(batch,index) {
	var loanOrderNo = document.form.ORDERNO.value;
	var orderLineNumber = document.form.ORDERLNO.value;
	var itemNo = document.form.ITEMNO.value;
	
	var loc = document.getElementById("FROMLOC_"+index).value;
	
	var locId = document.getElementById("TOLOC_"+index).value;
	
	var batch = document.getElementById("BATCH_"+index).value;
	if(batch=="" || batch.length==0 ) {
		alert("Enter Batch!");
		document.getElementById("BATCH_"+index).focus();
	}else{
		var urlStr = "/track/TransferOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
			ORDER_NO : loanOrderNo,
			ORDER_LNNO : orderLineNumber,
			ITEMNO : itemNo,
			BATCH : batch,
			LOCATION : loc,
				PLANT : "<%=plant%>",
				ACTION : "VALIDATE_BATCH_DETAILS_FOR_PICKING"
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

                                              if(document.form.SELECTION.checked){
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
	qty= removeCommas(qty);
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
			
			if(availableQty<qty){
				alert("Entered Quantity is greater than the Available Qty!");
				document.getElementById("PICKINGQTY_"+index).value="";
				document.getElementById("PICKINGQTY_"+index).focus();
			}else{
				
				document.getElementById("PICKINGQTY_"+index).focus();
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


document.form.BATCH_0.focus();
</script>
<%@ include file="footer.jsp"%>
