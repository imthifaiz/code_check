<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<link rel="stylesheet" href="css/style.css">

<title>Move To WIP</title>

<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
     subWin = window.open(URL, 'MoveTOWIP', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
  
  



function onClear(){
	document.form.EMPNO.value="";
	document.form.WONO.value="";
	document.form.ITEM.value="";
	document.form.DESC.value="";
  document.form.UOM.value="";
  document.form.PQTY.value="";
  document.form.OPR_SEQNUM.value="";
  document.form.LOC_0.value="";
  document.form.REMARKS.value="";
  document.form.REASONCODE_0.value="";
  document.form.CITEM.value="";
  document.form.CDESC.value="";
  document.form.CUOM.value="";
  document.form.BATCH_0.value="";
  document.form.INVQTY_0.value="";
  document.form.QTY.value="";
  
  document.form.action="MoveToWIP.jsp";
  document.form.submit();
  
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
       String ITEM   = "", DESC  = "",UOM="",
       LOC   = "" , BATCH  = "", REF   = "",CITEM="",
       QTY = "",PQTY="",REMARKS="",REASONCODE="",EMPNO="",WONO="",OPR_SEQNUM="";
       
       ITEM = strUtils.fString(request.getParameter("ITEM"));
       DESC = strUtils.fString(request.getParameter("ITEMDESC"));
       UOM = strUtils.fString(request.getParameter("UOM"));
       PQTY = strUtils.fString(request.getParameter("PQTY"));
       LOC = strUtils.fString(request.getParameter("LOC_0"));
       EMPNO = strUtils.fString(request.getParameter("EMPNO"));
       WONO = strUtils.fString(request.getParameter("WONO"));
       OPR_SEQNUM = strUtils.fString(request.getParameter("OPR_SEQNUM"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       REMARKS = strUtils.fString(request.getParameter("REMARKS"));
       REASONCODE = strUtils.fString(request.getParameter("REASONCODE"));

     if(action.equalsIgnoreCase("CLEAR"))
      {
    	 ITEM = "";
        DESC = "";
        LOC = "";
        BATCH = "";
        REF = "";
        EMPNO="";
        WONO = "";
        QTY="";
        OPR_SEQNUM="";
        REMARKS="";
        REASONCODE="";
           
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
      else if(action.equalsIgnoreCase("resultcatcherror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("RESULTCATCHERROR");
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
   %>
<%@ include file="body.jsp"%>
<FORM name="form" method="post"
	action="/track/MoveToWIPServlet?action=MoveToWIP">
<br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><FONT color="#ffffff">Move To WIP</FONT>&nbsp;</TH>

	</TR>
</TABLE>
<br>
<INPUT name="DIRTYPE"  type ="hidden" value="WIPMOVE"  >
<font face="Times New Roman" size="4">
<table border="0" cellspacing="1" cellpadding="2" bgcolor="">
	<%=fieldDesc%>
	</font>
</table>

<TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
   <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Employee&nbsp;&nbsp;: </TH>
            <TD>
                <INPUT name="EMPNO" type = "TEXT" value="<%=EMPNO%>" size="30" 
            onkeypress="if((event.keyCode=='13') && ( document.form.EMPNO.value.length > 0)) {ValidateEmployee(this.value);}"    MAXLENGTH="50">
              <a href="#" onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.EMPNO.value+'&TYPE=MOVETOWIP');"><img src="images/populate.gif" border="0"></a>
          </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Work Order No&nbsp;&nbsp;: </TH>
         <TD>
          <INPUT type="TEXT" size="30" MAXLENGTH="50" name="WONO"	value="<%=WONO%>" onkeypress="if((event.keyCode=='13') && ( document.form.WONO.value.length > 0)) {ValidateWorkOrder(this.value);}" /> 
                         <a href="#" onClick="javascript:popUpWin('WO_list.jsp?WONO='+form.WONO.value+'&TYPE=MOVETOWIP');">
                           <img src="images/populate.gif" border="0"/>
                          </a>
                          <input type="button" value="View" name="Submit" onclick="onGo(1)"/>
         </TD>
    </TR>
    <TR>
    	<th WIDTH="35%" ALIGN="RIGHT">Parent Product:</th>
								<td><INPUT type="TEXT" size="30" MAXLENGTH=50 name="ITEM" id="ITEM"
									value="<%=ITEM%>" class="inactiveGry" readonly
									onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}">
									<INPUT name="DESC" id="DESC" type="TEXT" value="<%=DESC%>" size="30" class="inactiveGry" readonly>
								<INPUT name="UOM" id="UOM" type="TEXT" value="<%=UOM%>" size="6" class="inactiveGry" readonly>
								<b>Qty :</b> <INPUT name="PQTY" id="PQTY" type="TEXT" value="<%=PQTY%>" size="6" class="inactiveGry" readonly>
								</td>
										
							
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >OP SEQ&nbsp;&nbsp;: </TH>
         <TD>
            <INPUT name="OPR_SEQNUM" id="OPR_SEQNUM" type = "TEXT" value="<%=OPR_SEQNUM%>" size="30"  onkeypress="if((event.keyCode=='13') && ( document.form.OPR_SEQNUM.value.length > 0)) {ValidateSeqnum(this.value);}"  MAXLENGTH=50 >
              <a href="#" onClick="javascript:popUpWin('OpSeqListfromRouting.jsp?OPERATIONSEQ='+form.OPR_SEQNUM.value+'&PITEM='+form.ITEM.value+'&TYPE=MOVETOWIP');"><img
			src="images/populate.gif" border="0"></a> 
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Location&nbsp;&nbsp;: </TH>
         <TD>
           <INPUT name="LOC_0" id="LOC_0" type="TEXT"
					value="<%=LOC%>" size="30"
					onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(0);}"
					MAXLENGTH=80> <a href="#"
					onClick="javascript:popUpWin('loc_list_QCTransfer.jsp?LOC='+form.LOC_0.value+'&INDEX=0');"><img
					src="images/populate.gif" border="0"></a>
					</td>
				
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Remarks&nbsp;&nbsp;: </TH>
         <TD>
          <INPUT name="REMARKS" type="TEXT"  id="REMARKS" value="<%=REMARKS%>"
			size="30" MAXLENGTH=100 onkeypress="if(event.keyCode=='13') {document.form.REASONCODE_0.focus();}"> 
			
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Reason Code&nbsp;&nbsp;: </TH>
         <TD>
           <INPUT name="REASONCODE_0" type="TEXT"  id="REASONCODE_0" value="<% if(REASONCODE.length() > 0) { out.print(REASONCODE); }else{out.print("NOREASONCODE");}%>"
			size="30"  MAXLENGTH=50 onkeypress="if((event.keyCode=='13') && ( document.form.REASONCODE_0.value.length > 0)){ document.form.CITEM.focus();}"> <a href="#"
			onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form.ITEM.value+'&INDEX=0');">
			<img src="images/populate.gif" border="0" /> </a>
		
         </TD>
    </TR>
    </TABLE>
    <div id = "ERROR_MSG"></div>
    <br>
    <TABLE border="0" width="100%" cellspacing="0"  bgcolor="#dddddd">
	
	<TR>
		<TH ALIGN="right" >Scan Product : &nbsp;&nbsp;</TH>
		<TD ALIGN="left" ><INPUT name="CITEM" type="TEXT" id="CITEM"
			value="<%=CITEM%>" size="20" MAXLENGTH=50 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){ValidateChildProduct();}">
			<a href="#"	onClick="javascript:popUpWin('ProductionBOMchilditemList.jsp?PITEM='+form.ITEM.value+'&CITEM='+form.CITEM.value+'&OPSEQ='+form.OPR_SEQNUM.value+'&TYPE=MOVETOWIP');">
			<img src="images/populate.gif" border="0" /> </a></TD>
		 <TH ALIGN="right" >Desc :  &nbsp;&nbsp;</TH>
		<TD  ALIGN="left"><INPUT name="CDESC" id="CDESC" type="TEXT" value="" size="30" maxlength="50"><INPUT name="CUOM" id="CUOM" type="TEXT" value="" size="6" ></td>
		<TH ALIGN="right" >Batch :  &nbsp;&nbsp;</TH>
		<TD  ALIGN="left"><INPUT name="BATCH_0" id="BATCH_0" type="TEXT" value="<%=BATCH%>" size="20" maxlength="40"
		onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(0);}">
		<a href="#" onClick="javascript:popUpWin('batch_list_QCTransfer.jsp?LOC='+form.LOC_0.value+'&ITEMNO='+form.CITEM.value+'&BATCH'+form.BATCH_0.value+'&INDEX=0');">
					<img src="images/populate.gif" border="0"/></a>
		</td>
		<TH ALIGN="right" >Available Qty :  &nbsp;&nbsp;</TH>
		<TD  ALIGN="left"><INPUT name="INVQTY_0" type="TEXT" id="INVQTY_0" class="inactivegry" readonly value=""
			size="8" MAXLENGTH=80 > 
		<INPUT     name="UOM_0"  type ="hidden" value=""  >			
		</td>	
		<TH ALIGN="right" >Qty :  &nbsp;&nbsp;</TH>
		<TD  ALIGN="left"><INPUT name="QTY" id="QTY" type="TEXT" value="<%=QTY%>" size="1" maxlength="10"
		onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value,0);}">	
		 
		</td>
		<td ALIGN="left" ><input type="button" 
			value="ADD" onclick="onAdd()"></td>
		</tr>
    </TABLE>
    <br>
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  <div align="center">
				<center><br>
				<table border="0" width="30%" cellspacing="0" cellpadding="0">
				<TR>
				<TD COLSPAN=2>
				
				<INPUT class="Submit" type="BUTTON" value="Clear" onClick="onClear()">&nbsp;&nbsp;
				<INPUT class="Submit" type="BUTTON" value="Remove" onClick="onRemove()">&nbsp;&nbsp;
				<input	type="button" value="Move To WIP" name="SubmitButton" onClick="MoveToWIP()" /> 
				

		</TD>
	</TR>
				</table>
				</center>
				</div>
	
    
	<!--  
	<TR>
		<TD COLSPAN=2>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<INPUT class="Submit" type="BUTTON" value="Cancel"
			onClick="window.location.href='../home'" />&nbsp;&nbsp; <input
			type="button" value="MoveToWIP" name="SubmitButton"
			onClick="return validatePO(document.form)" /> <INPUT class="Submit"
			type="BUTTON" value="Clear" onClick="return onClear();">&nbsp;&nbsp;

		</TD>
	</TR>

</TABLE>-->	
<INPUT name="LOGINUSER" type="hidden" value="<%=sUserId %>" size="1"
	MAXLENGTH=80> <INPUT name="action" type="hidden"></center>
	    <INPUT     name="CHECKQTY"  type ="hidden" value="<%=QTY%>" size="1"   MAXLENGTH=80 ></TD>
	 

</FORM>
</HTML>
<script>
onGo(0);
function ValidateEmployee() {
	var employee = document.form.EMPNO.value;
	if(employee=="" || employee.length==0 ) {
		alert("Enter Employee Number");
		document.getElementById("EMPNO").focus();
	}else{
	var urlStr = "/track/MoveToWIPServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		EMPNO : employee,
		PLANT : "<%=PLANT%>",
		action : "VALIDATE_EMPLOYEE"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.form.WONO.focus();

						} else {
							alert("Not a valid Employee !");
							document.form.EMPNO.value = "";
							document.form.EMPNO.focus();
						}
					}
				});
		}
	}
	
function ValidateWorkOrder() {
	var wono = document.form.WONO.value;
	if(wono=="" || wono.length==0 ) {
		alert("Enter Work Order Number");
		document.getElementById("WONO").focus();
	}else{
	var urlStr = "/track/MoveToWIPServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		WONO : wono,
		PLANT : "<%=PLANT%>",
		action : "VALIDATE_WORKORDER"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.form.ITEM.value = resultVal.item;
							document.form.DESC.value = resultVal.discription;
							document.form.UOM.value = resultVal.uom;
							document.form.PQTY.value = resultVal.qty;
							document.form.OPR_SEQNUM.focus();
						} else {
							alert("Not a valid Work Order !");
							document.form.WONO.value = "";
							document.form.WONO.focus();
						}
					}
				});
		}
	}

function ValidateSeqnum() {
	var oprseqnum = document.form.OPR_SEQNUM.value;
	if(oprseqnum=="" || oprseqnum.length==0 ) {
		alert("Enter Operation Sequence Number");
		document.getElementById("OPR_SEQNUM").focus();
	}else{
	var urlStr = "/track/RoutingServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		OPRSEQNUM : oprseqnum,
		PLANT : "<%=PLANT%>",
		action : "VALIDATE_OPRSEQNUM"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.form.LOC_0.value = resultVal.fromloc;
							document.form.LOC_0.select();
							document.form.LOC_0.focus();

						} else {
							alert("Not a valid operation sequence number!");
							document.form.OPR_SEQNUM.value = "";
							document.form.OPR_SEQNUM.focus();
						}
					}
				});
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
                USERID : "<%=sUserId%>",
				PLANT : "<%=PLANT%>",
					ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						document.getElementById("REMARKS").value = "";
						document.getElementById("REMARKS").focus();
					} else {
						alert("Not a valid Location");
						document.getElementById("LOC"+"_"+index).value = "";
						document.getElementById("LOC"+"_"+index).focus();
					}
				}
			});
		}
	}
	
function ValidateChildProduct() {
	var parentproduct = document.form.ITEM.value;
	var childproduct = document.form.CITEM.value;
	var locId = document.getElementById("LOC_0").value;
	var oprseqnum = document.form.OPR_SEQNUM.value;
	
	if(oprseqnum=="" || oprseqnum.length==0 ) {
		alert("Enter Operation Sequence Number");
		document.getElementById("OPR_SEQNUM").focus();
		document.form.CITEM.value;
		return false;
	}
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC_0").focus();
		document.form.CITEM.value;
		return false;
	}
	
	if(childproduct=="" || childproduct.length==0 ) {
		alert("Enter Child Product");
		document.getElementById("CITEM").focus();
	}else{
	var urlStr = "/track/ProductionBomServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PITEM : parentproduct,
		CITEM : childproduct,
		PLANT : "<%=PLANT%>",
		action : "VALIDATE_CHILD_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("CDESC").value = resultVal.discription;
							document.getElementById("CUOM").value = resultVal.uom;
							document.getElementById("BATCH_0").value = "NOBATCH";
							var urlStr = "/track/MiscOrderHandlingServlet";
							$.ajax( {
								type : "POST",
								url : urlStr,
								data : {
									ITEM : childproduct,
									LOC : locId,
									BATCH : "NOBATCH",
									PLANT : "<%=PLANT%>",
									ACTION : "VALIDATE_BATCH"
									},
									dataType : "json",
									success : function(data) {
										
										if (data.status == "100") {
											var resultVal = data.result;
											document.getElementById("INVQTY_0").value =resultVal.availableQty;
											document.getElementById("BATCH_0").select();
											document.getElementById("BATCH_0").focus();
											
			                                             
			                                                                 
										} else {
											document.getElementById("INVQTY_0").value =0;
											document.getElementById("BATCH_0").select();
											document.getElementById("BATCH_0").focus();
											
										}
									}
								});
							
						} else {
							alert("Not a valid child product for work order parent product:"+parentproduct);
							document.form.CITEM.value = "";
							document.form.CITEM.focus();
						}
					}
				});
		}
	}

function validateBatch(index) {
	var batch = document.getElementById("BATCH"+"_"+index).value;
	var productId = document.getElementById("CITEM").value;
	var locId = document.getElementById("LOC"+"_"+index).value;

           
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.form.LOC_0.focus();
	}else{
		if(document.getElementById("CITEM").value=="" || document.getElementById("CITEM").value.length==0 ) {
			alert("Enter Child Product ID!");
			document.getElementById("CITEM").focus();
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
								//var totissueqty = getSumByLocItemAndBatch(index);
								//var invqty = addCommas(resultVal.availableQty-totissueqty);
								document.getElementById("INVQTY"+"_"+index).value =resultVal.availableQty;
								//document.getElementById("QTY"+"_"+index).value = "";
								 document.getElementById("QTY").focus();
                                             
                                                                 
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
    
	var qty = document.getElementById("QTY").value;
	qty = removeCommas(qty);
           
	if (qty == "" || qty.length == 0) {
		alert("Enter Quantity!");
		document.getElementById("QTY").focus();
	} 
		else if(qty <=0  )
	       {
	         alert("Qty Should not be 0 or less than 0");
	         document.getElementById("QTY").value = "";
	         document.getElementById("QTY").focus();
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
				document.getElementById("QTY").value = "";
				document.getElementById("QTY").focus();
				return false;
			}else{
                 //document.getElementById("REFDET"+"_"+index).focus();
				
			}
		}

	}
	onAdd();
	
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

function onAdd() {
	
		var wono = document.form.WONO.value;
		var parentproduct = document.form.ITEM.value;
		var childproduct = document.form.CITEM.value;
		var desc = document.form.CDESC.value;
		var uom = document.form.CUOM.value;
		var batch = document.form.BATCH_0.value;
		var qty = document.form.QTY.value;
		var loc = document.form.LOC_0.value;
		var opseq = document.form.OPR_SEQNUM.value;
		
		if(wono=="" || wono.length==0 ) {
			alert("Enter Work Order Number");
			document.getElementById("WONO").focus();
			return false;
		}
		if(parentproduct=="" || parentproduct.length==0 ) {
			alert("Enter Parent Product");
			document.getElementById("ITEM").focus();
			return false;
		}
		if(childproduct=="" || childproduct.length==0 ) {
			alert("Enter Child  Product");
			document.getElementById("CITEM").focus();
			return false;
		}
		
		
		if(batch=="" || batch.length==0 ) {
			alert("Enter Batch");
			document.getElementById("BATCH_0").focus();
			return false;
		}
		
		if(qty=="" || qty.length==0 ) {
			alert("Enter Quantity");
			document.getElementById("QTY").focus();
			return false;
		}
		   
	    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
	    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
	    var urlStr = "/track/MoveToWIPServlet";
	    
	    // Call the method of JQuery Ajax provided
	    $.ajax({type: "POST",url: urlStr, data: {WONO:wono,ITEM:parentproduct,CITEM:childproduct,DESC:desc,UOM:uom,BATCH:batch,QTY:qty,TYPE:"WIPMOVE",LOC:loc,OPSEQ:opseq,action: "ADD_WIPTRAN_TEMP",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
	    document.form.CITEM.value = "";
	    document.form.CDESC.value = "";
	    document.form.CUOM.value = "";
	    document.form.QTY.value = "";
	    document.form.INVQTY_0.value = "";
	    document.form.BATCH_0.value = "";
	    document.form.CITEM.focus();
	    
	}

function onGo(index) {

		var index = index;
		var wono = document.form.WONO.value;
		var product = document.form.ITEM.value;
		
		if(index == '1'){
		if(wono=="" || wono.length==0 ) {
			alert("Enter Work Order");
			document.getElementById("WONO").focus();
			return false;
			}
	     
		}
	    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
	    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
	    var urlStr = "/track/MoveToWIPServlet";
	    
	    // Call the method of JQuery Ajax provided
	    $.ajax({type: "POST",url: urlStr, data: {WONO:wono,ITEM:product,action: "VIEW_WIPTRAN_DETAILS",PLANT:"<%=PLANT%>"},dataType: "json", success: callback });
	  
	}
function MoveToWIP()//form
{debugger;
	var empno = document.form.EMPNO.value;
	var wono = document.form.WONO.value;
	var parentproduct = document.form.ITEM.value;   
	var loc= document.form.LOC_0.value;
	var reasoncode = document.form.REASONCODE_0.value;
	var oprseq =  document.form.OPR_SEQNUM.value;
	   
	 if(empno=="" || empno.length==0 )
	 {
		alert("Enter Employee!");
		//empno.style.backgroundColor = "#FFE5EC";
		document.form.EMPNO.focus();
		return false;
         }
	 if(wono=="" || wono.length==0 )
	 {
		alert("Enter Work Order!");
		//wono.style.backgroundColor = "#FFE5EC";
		document.form.WONO.focus();
		return false;
         }
	 if(parentproduct=="" || parentproduct.length==0 )
	 {
		alert("Enter Parent Product!");
		//parentproduct.style.backgroundColor = "#FFE5EC";
		document.form.ITEM.focus();
		return false;
         }
	 
	/* if(oprseq=="" || oprseq.length==0 )
	 {
		alert("Enter Operation Sequence!");
		//oprseq.style.backgroundColor = "#FFE5EC";
		document.form.OPR_SEQNUM.focus();
		return false;
         }
	 if(loc=="" || loc.length==0 )
	 {
		alert("Enter Location!");
		//loc.style.backgroundColor = "#FFE5EC";
		document.form.LOC_0.focus();
		return false;
         }
      */      
   
   
    if(reasoncode=="" || reasoncode.length==0 )
	 {
		alert("Enter REASONCODE!");
		document.form.REASONCODE_0.focus();
		return false;
   }
    
	 var chkmsg = confirm("Are you sure you would like to Move To WIP?");
		if (chkmsg) {
	  	  document.form.action="/track/MoveToWIPServlet?action=MoveToWIP";
	 	 document.form.submit();
	 	 return true;   
		}
		 else {
				return false;
			}
	 
  
	 
}	
	
function onRemove()
{
	var checkFound = false;
	var chkitems = document.form.chkitem.value;
	 var len = document.form.chkitem.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
   {
		if(len == 1 && (!document.form.chkitem.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkitem.checked)
	     {
	    	 checkFound = true;
	    	 
	     }
	
	     else {
		     if(document.form.chkitem[i].checked){
		    	 checkFound = true;
		    	 
		     }
	     }
         		
       	     
   }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var chkmsg = confirm("Are you sure you would like to delete?");
		if (chkmsg) {
	  	  document.form.action="/track/MoveToWIPServlet?action=REMOVE_WIPTRAN_TEMP";
	 	 document.form.submit();
	 	 return true;   
		}
		 else {
				return false;
			}
	
}
function callback(data){
	
	var outPutdata = getTable();
	var ii = 0;
	var errorBoo = false;
	$.each(data.errors, function(i,error){
		if(error.ERROR_CODE=="99"){
			errorBoo = true;
			
		}
	});
	
	if(!errorBoo){
		
        $.each(data.wiptran, function(i,item){
                   
        	outPutdata = outPutdata+item.WIPTRANDATA;
                    	ii++;
            
          });
        
	}
    outPutdata = outPutdata +'</TABLE>';
                                                  
    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
     document.getElementById('spinnerImg').innerHTML =''; 
     var errorMsg = data.errorMsg;
     if(typeof(errorMsg) == "undefined"){
    	 errorMsg = "";
     }
     errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
     document.getElementById('ERROR_MSG').innerHTML = errorHTML;
     //document.form.TRANSACTIONNO.select();
	 //document.form.TRANSACTIONNO.focus();
      	     
}
function getTable(){
    return '<TABLE BORDER="0" id="tabledata" cellspacing="0" WIDTH="70%"  align = "center" bgcolor="navy">'+
    	   '<tr BGCOLOR="#000066">'+
     		'<th width="10%"><font color="#ffffff">Select</font></th>'+
     		'<th width="10%"><font color="#ffffff">Product ID</font></th>'+
     		'<th width="10%"><font color="#ffffff">Description</font></th>'+
     		'<th width="10%"><font color="#ffffff">SeqNo</font></th>'+
     		'<th width="10%"><font color="#ffffff">Location</font></th>'+
     		'<th width="10%"><font color="#ffffff">Batch</font></th>'+
     		'<th width="10%"><font color="#ffffff">Qty</font></th>'+
     		'<th width="10%"><font color="#ffffff">UOM</font></th>'+
     		'</tr>';
   
}

document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

</script>
<%@ include file="footer.jsp"%>
