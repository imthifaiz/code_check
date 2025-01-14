<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript"	src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Goods Receipt By Transfer Order</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
  var subWin = null;
  function popUpWin(URL) 
  {
    subWin = window.open(URL, 'InboundOrderList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
  
  
  function validatePO(form)//form
  {
    var frmRoot=document.form;
    if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 )
	 {
		alert("Please Enter Order Number!");
		frmRoot.ORDERNO.focus();
		return false;
   }
   
   
   if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
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
  function validateBatchGen(form)
  {
 	  var frmRoot=document.form;
   
   	  if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 )
	  {
		alert("Please Enter Order Number!");
		frmRoot.ORDERNO.focus();
		return false;
      }
     else if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	  {
		alert("Please Enter Product ID!");
		frmRoot.ITEMNO.focus();
		return false;
      }
   
     if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter Batch!");
		frmRoot.BATCH.focus();
		return false;
     }
    else
    {
     return true;
    }
}

	function onClear()
	{
  		document.form.ORDERNO.value="";
  		document.form.CUSTNAME.value="";
  		document.form.ITEMNO.value="";
  		document.form.ITEMDESC.value="";
  		document.form.LOC.value="";
  		document.form.BATCH.value="";
  		document.form.REF.value="";
  		document.form.ORDERQTY.value="";
  		document.form.RECEIVEDQTY.value="";
  		document.form.RECEIVEQTY.value="";
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
<jsp:useBean id="phb" class="com.track.tables.TOHDR" />
<jsp:useBean id="pdb" class="com.track.tables.TODET" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
  <%
       StrUtils strUtils=new StrUtils();
       TOUtil itemUtil = new TOUtil();
       session = request.getSession();
       String plant = session.getAttribute("PLANT").toString();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String  fieldDesc="";
       String   ORDERNO    = "",ITEMNO   = "", ITEMDESC  = "",UOM="",
       FROMLOC   = "" ,TOLOC="",TEMPLOC="", CHECKQTY="",BATCH  = "", REF   = "",ORDERLNO,
       QTY = "",RECEIVEQTY="",CUSTNAME="",ORDERQTY="",INVQTY="",PICKEDQTY="",PICKINGQTY="", RECEIVEDQTY="",RECEIVINGQTY="",BALANCEQTY="",AVAILABLEQTY="",
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="",TRANSACTIONDATE="";
       ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=strUtils.fString(request.getParameter("ORDERLNO"));
      // CUSTNAME= strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUSTNAME")));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC =  strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")));
       FROMLOC = strUtils.fString(request.getParameter("FROMLOC"));
       TOLOC = strUtils.fString(request.getParameter("TOLOC"));
       String TEMP_TO="TEMP_TO_"+FROMLOC;
       TEMPLOC = strUtils.fString(request.getParameter("TEMPLOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       PICKINGQTY = strUtils.fString(request.getParameter("PICKINGQTY"));
       INVQTY = strUtils.fString(request.getParameter("QTY"));
       REF = strUtils.fString(request.getParameter("REF"));
       PICKEDQTY =StrUtils.formatNum(strUtils.fString(request.getParameter("PICKEDQTY")));
       ORDERQTY = StrUtils.formatNum(strUtils.fString(request.getParameter("ORDERQTY")));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       RECEIVINGQTY=strUtils.fString(request.getParameter("RECEIVINGQTY"));
       RECEIVEDQTY =  strUtils.fString(request.getParameter("RECEIVED_QTY"));
       AVAILABLEQTY=strUtils.fString(request.getParameter("AVAILABLEQTY"));
	   TRANSACTIONDATE = strUtils.fString(request.getParameter("TRANSACTIONDATE"));
       DateUtils _dateUtils = new DateUtils();
       String curDate =_dateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
       ItemMstDAO itemmstdao = new ItemMstDAO();
      itemmstdao.setmLogger(mLogger);
      UOM = itemmstdao.getItemUOM(plant,ITEMNO);
       //Get podet
       ToDetDAO  _ToDetDAO  = new ToDetDAO();  
       List listQry =  _ToDetDAO.getTransferItemListByWMS(plant,ORDERNO,ITEMNO);
       for(int i =0; i<listQry.size(); i++) {
          Map m=(Map)listQry.get(i);
          
         // ORDERQTY =(String)m.get("qtyor"); // commmented by samatha as OrderQty is retrived above in URl
        //  RECEIVEDQTY = StrUtils.formatNum((String)m.get("qtyrc"));
      //System.out.println("Received qty"+RECEIVEDQTY);         
          BALANCEQTY=(String)m.get("balanceqty");
          REF= (String)m.get("ref");
       }
        //Get end
       
       //Get Supplier Details
         Hashtable ht=new Hashtable();
         String extCond="";
         ht.put("PLANT",plant);
         ht.put("TONO",ORDERNO);
       
  
         if(ORDERNO.length()>0) extCond="a.plant='"+plant+"' and a.tono = '"+ORDERNO+"' and a.custname=b.assignename";
         	extCond=extCond+" order by a.tono desc"; 
         	ArrayList listQry1 = itemUtil.getToHdrDetailsReceiving(" a.custname,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3",ht,extCond);
         	for(int i =0; i<listQry1.size(); i++) {
            	Map m1=(Map)listQry1.get(i);
            	CUSTNAME= (String)m1.get("custname");
              	CONTACTNAME  =  (String)m1.get("contactname");
            	TELNO  =  (String)m1.get("telno");
            	EMAIL  =  (String)m1.get("email");
            	ADD1  =  (String)m1.get("add1");
            	ADD2  =  (String)m1.get("add2");
            	ADD3 =  (String)m1.get("add3");
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
       }
       else if(action.equalsIgnoreCase("batchresult"))
       {
        System.out.println("batchresult error");
       }
      else if(action.equalsIgnoreCase("batcherror"))
      {
       fieldDesc=(String)request.getSession().getAttribute("BATCHERROR");
      }
      else if(action.equalsIgnoreCase("catchbatcherror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("CATCHBATCHERROR");
      }

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="/track/TransferOrderServlet?"  >
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Multiple Goods Receipt By Transfer Order</FONT>&nbsp;
      </TH>
       
    </TR>
  </TABLE>
  <br>
  <font face="Times New Roman" size="4">
    <table border="0" cellspacing="1" cellpadding="2" bgcolor="">
        <%= fieldDesc%>
    </table>
    
    <TABLE border="0" CELLSPACING="1" WIDTH="100%" bgcolor="#dddddd" cellpadding="2">
      <TR>
        <TH WIDTH="12%" ALIGN="Right">*Order No. :</TH>
        <TD width="45%">
          <INPUT name="ORDERNO" type="TEXT" value="<%=ORDERNO%>" size="35" MAXLENGTH="20"/>
         </TD>
        <TH WIDTH="7%"/>
        <TD width="35%"/>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">Assignee Name :</TH>
        <TD width="45%">
          <INPUT name="CUSTNAME" class="inactivegry" type="TEXT" value="<%=su.forHTMLTag(CUSTNAME)%>" size="35" MAXLENGTH="80" readonly/>
        </TD>
        <TD width="7%"/>
        <TH WIDTH="35%" ALIGN="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Assignee Details</TH>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">Item No:</TH>
        <TD width="45%">
         <INPUT name="ITEMNO" type="TEXT" class="inactivegry" readOnly  value="<%=ITEMNO%>"  size="35" MAXLENGTH="80"/>
        </TD>
        <TH WIDTH="7%" ALIGN="Right">Contact Name:</TH>
        <TD width="35%">
          <INPUT name="CONTACTNAME" class="inactivegry" value="<%=CONTACTNAME%>" type="TEXT" size="35" MAXLENGTH="80" readonly/>
        </TD>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">&nbsp;Product Desc &nbsp;:</TH>
        <TD width="45%">
          <INPUT name="ITEMDESC" class="inactivegry" type="TEXT" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="35" MAXLENGTH="80" readonly/>
        </TD>
        <TH WIDTH="7%" ALIGN="Right">Telephone :</TH>
        <TD width="35%">
          <INPUT name="TELNO" class="inactivegry" value="<%=TELNO%>" type="TEXT" size="35" MAXLENGTH="80" readonly/>
        </TD>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">Order&nbsp;Qty&nbsp; :</TH>
        <TD width="45%">
          <INPUT name="ORDERQTY" class="inactivegry" type="TEXT" value="<%=ORDERQTY%>" size="35" MAXLENGTH="80" readonly/>
          &nbsp;&nbsp;&nbsp;&nbsp;<b><%=IDBConstants.UOM_LABEL %> :</b>&nbsp;<INPUT name="UOM" class="inactivegry" type = "TEXT" value="<%=UOM%>" size=10"  MAXLENGTH=15 readonly>
          
        </TD>
        <TH WIDTH="7%" ALIGN="Right">Email :</TH>
        <TD width="35%">
          <INPUT name="EMAIL" value="<%=EMAIL%>" class="inactivegry" type="TEXT" size="35" MAXLENGTH="80" readonly/>
        </TD>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right" height="35">Picked&nbsp;Qty :</TH>
        <TD width="45%" height="35">
          <INPUT name="PICKEDQTY" class="inactivegry" type="TEXT" value="<%=PICKEDQTY%>" size="35" MAXLENGTH="80" readonly/>
        </TD>
        <TH WIDTH="7%" ALIGN="Right" height="35">Unit&nbsp;No :</TH>
        <TD width="35%" height="35">
          <INPUT name="ADD1" class="inactivegry" value="<%=ADD1%>"  type="TEXT" size="35" MAXLENGTH="80" readonly/>
        </TD>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">From&nbsp;Loc&nbsp;:</TH>
        <TD width="45%">
          <INPUT name="FROMLOC" id ="FROMLOC" class="inactivegry" readOnly     type="TEXT" value="<%=FROMLOC%>" size="35" MAXLENGTH="80"/>
        </TD>
        <TH WIDTH="7%" ALIGN="Right">Building :</TH>
        <TD width="35%">
          <INPUT name="ADD2" value="<%=ADD2%>" class="inactivegry" type="TEXT" size="35" MAXLENGTH="80" readonly/>
        </TD>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">Temp&nbsp;Loc&nbsp;&nbsp;:</TH>
        <TD width="45%">
         <INPUT name="TEMPLOC" type="TEXT" value="<%=TEMP_TO%>" size="35" class="inactivegry" readOnly  onkeypress="javascript:selectAll();" MAXLENGTH="80"/>
        </TD>
        <TH WIDTH="7%" ALIGN="Right">Street :</TH>
        <TD width="35%">
          <INPUT name="ADD3" value="<%=ADD3%>" class="inactivegry" type="TEXT" size="35" MAXLENGTH="80" readonly/>
        </TD>
      </TR>
    
       <TR>
        <TH WIDTH="12%" ALIGN="Right">Received Qty&nbsp;&nbsp;:</TH>
        <TD width="45%">
          <INPUT name="RECEIVEDQTY" class="inactivegry" type="TEXT" value="<%=RECEIVEDQTY%>" size="35" MAXLENGTH="80" readonly/>
        </TD>
        <TH WIDTH="7%"/>
        <TD width="35%"/>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">Remarks&nbsp;&nbsp;:</TH>
        <TD width="45%">
          <INPUT name="REF" type="TEXT" value="<%=REF%>" size="35" MAXLENGTH="80" onkeypress="if(event.keyCode=='13') {document.form.SubmitForm.focus();}"/>
        </TD>
        <TH WIDTH="7%"/>
        <TD width="35%"/>
      </TR>
      <TR>
		<TD colspan="4"><br>
		<center><input type="Checkbox" id="SELECTION" name="SELECTION" value="SERIAL_SELECTION" onClick="processSerialSelection();"></input>
		Use Serial Receiving
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</center>
		<br>
		<table align="center" width="85%" border="0" id="MULTIPLE_RECEIVING">
			<tr>
				<td width="19%"><b>To Loc : </b><INPUT name="TOLOC_0" id="TOLOC_0" type="TEXT"
					value="<%=TOLOC%>" size="20"  class="inactivegry"
				onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}"	
					MAXLENGTH=80> </td>
				<td width="1%">&nbsp;</td>
				<td width="20%"><b>Batch : </b><INPUT name="BATCH_0" id="BATCH_0" type="TEXT"
					value="<%=BATCH%>" size="20"  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value,0);}"
					MAXLENGTH=40> <a href="#"
			
			onClick="javascript:popUpWin('batch_list_toreceive.jsp?ITEMNO='+form.ITEMNO.value+'&ORDERNO='+form.ORDERNO.value+'&ORDERLNO='+form.ORDERLNO.value+'&BATCH='+form.BATCH_0.value+'&INDEX='+'0');">
		<img src="images/populate.gif" border="0" /> </a></td>
		<td width="1%">&nbsp;</td>
				<td width="18%"><b>Available Qty : </b><INPUT name="AVAILABLEQTY_0" id="AVAILABLEQTY_0"
					value="<%= StrUtils.formatNum(AVAILABLEQTY)%>" type="TEXT" size="10" MAXLENGTH="80"\></td>
				<td width="1%">&nbsp;</td>
				
				<td width="16%"><b>Receiving Qty : </b><INPUT name="RECEIVINGQTY_0" id="RECEIVINGQTY_0"
					value="<%= RECEIVINGQTY%>" type="TEXT" size="10" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value,0);}"  MAXLENGTH="80"\></td>
			
			</tr>

		</table>
		<br></br>
		<table align="center" width="85%" border="0">
			<tr>
				<td><INPUT type="button" value="ADD NEW RECEIVING" onclick="addRow();" /> <INPUT type="button"
					value="REMOVE LAST ADDED RECEIVING"
					onclick="deleteRow('MULTIPLE_RECEIVING');" /> <INPUT type="hidden"
					name="DYNAMIC_RECEIVING_SIZE">
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
        <TD COLSPAN="2">
          <BR/>
          <!-- &amp;lt;INPUT name=&amp;quot;noOfLabelToPrint&amp;quot; type=&amp;quot;HIDDEN&amp;quot; size=&amp;quot;50&amp;quot; readonly MAXLENGTH=&amp;quot;80&amp;quot;/&amp;gt;&amp;lt;B&amp;gt;&amp;lt;CENTER&amp;gt;&amp;lt;%=REF%&amp;gt;&amp;lt;/B&amp;gt;&amp;lt;/TD&amp;gt;-->
        </TD>
      </TR>
      <TR>
        <TD WIDTH="35%" COLSPAN="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <INPUT class="Submit" type="BUTTON" value="Cancel"  onClick="window.location.href='MultiTransferOrderReceiving.jsp?action=MultipleView&TONO='+form.ORDERNO.value "/>&nbsp;&nbsp;
          <input type="BUTTON" value="TO_ReceiveConfirm" name="SubmitForm" onClick="if(validatePO(document.form)){ submitFormValues();}"/>
         
        </TD>
        <TH WIDTH="10%"/>
        <TH WIDTH="10%"/>
        <TD width="35%"/>
      </TR>
    </TABLE>
  </font>

</center>
<Table border=0 bgcolor="#dddddd">
 <TR  bgcolor="#dddddd">
  <INPUT     name="ORDERLNO"  type ="hidden" value="<%=ORDERLNO%>" size="1"   MAXLENGTH=80 ></TD>
  <INPUT     name="BALANCEQTY"  type ="hidden" value="<%=BALANCEQTY%>" size="1"   MAXLENGTH=80 ></TD>
   <INPUT     name="LOGIN_USER"  type ="hidden" value="<%=sUserId%>" size="1"   MAXLENGTH=80 ></TD>
    </TR>
</Table>
</FORM>

</HTML>
<script>
function submitFormValues(){
	var frmRoot=document.form;
	   var table = document.getElementById("MULTIPLE_RECEIVING");
	   var rowCount = table.rows.length;
	   document.form.DYNAMIC_RECEIVING_SIZE.value = rowCount;
	   var totalQtyPick=0;
	   processSelection();
	   for(var index = 0; index<rowCount; index++) {
		    var locationText = document.getElementById("TOLOC_"+index);
			var recvingQty = document.getElementById("RECEIVINGQTY_"+index);
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

			if(recvingQty.value== "" || recvingQty.value.length==0 || recvingQty.value<=0) {
				alert("Please Enter Valid Qty!");
				recvingQty.style.backgroundColor = "#FFE5EC";
				recvingQty.focus();
				return false;
			}else{
				totalQtyPick = (totalQtyPick*1)+ (recvingQty.value*1);
				totalQtyPick = totalQtyPick * 1;
				recvingQty.style.backgroundColor = "#FFFFFF";
			}
			
	   }
	   for(var index = 0; index<rowCount; index++) {
		   var pickQty = document.getElementById("RECEIVINGQTY_"+index).value;
		   var availQty = document.getElementById("AVAILABLEQTY_"+index).value;
		   pickQty = removeCommas(pickQty);
		   availQty = removeCommas(availQty);
		   if (isNumericInput(availQty) == false) {
				alert("Entered Quantity is not a valid Qty!");
				return false;
			}
		   var recvqty = parseFloat(pickQty,10);
		   var inavailqty = parseFloat(availQty,10);
		   recvqty = round_float(recvqty,3);
		   inavailqty = round_float(inavailqty,3);
		  if(recvqty>inavailqty)
		  {
			  alert("Entered Qty greater than Available Qty");
			  return false;
		  }
	   }
	   var orderedQty = <%=StrUtils.removeFormat(ORDERQTY)%>;
	   var recvedQty = <%=StrUtils.removeFormat(RECEIVEDQTY)%>;
	   if((totalQtyPick*1)+(recvedQty*1)>(orderedQty*1)){
		   for(var index = 0; index<rowCount; index++) {
			   var receiveQty = document.getElementById("RECEIVINGQTY_"+index);
			   receiveQty.style.backgroundColor = "#FFE5EC";
		   }
		   alert("Exceeded the Orderd Qty. Please check all the Qtys.!");
		   return false;
	   }else{
		   //document.form.PICKINGQTY.value = totalQtyPick;  
		   
		   document.form.action="/track/TransferOrderServlet?Submit=MultiTO_ReceiveConfirm";
		   document.form.submit();
		   return true;
	   }
		
			
		}
function validateBatchDetails() {
	var loanOrderNo = document.form.ORDERNO.value;
	var orderLineNumber = document.form.ORDERLNO.value;
	var itemNo = document.form.ITEMNO.value;
	var batch = document.form.BATCH.value;
	var urlStr = "/track/TransferOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : loanOrderNo,
			ORDER_LNNO : orderLineNumber,
			ITEMNO : itemNo,
			BATCH : batch,
			PLANT : "<%=plant%>",
			ACTION : "VALIDATE_BATCH_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.form.BATCH.value = resultVal.BATCH;
							document.form.AVAILABLEQTY.value = ((resultVal.PICK_QTY) * 1 - (resultVal.RECEIVED_QTY) * 1);
							document.form.RECEIVINGQTY.focus();

						} else {
							alert("Not a valid Batch No!");
							document.form.BATCH.value = "";
							document.form.AVAILABLEQTY.value = "";
							document.form.BATCH.focus();
						}
					}
				});
	}

function validateBatch(batch,index) {
	var loanOrderNo = document.form.ORDERNO.value;
	var orderLineNumber = document.form.ORDERLNO.value;
	var itemNo = document.form.ITEMNO.value;
	
	var loc = document.getElementById("FROMLOC").value;
	
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
				ACTION : "VALIDATE_BATCH_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						document.getElementById("BATCH_"+index).value = resultVal.BATCH;
						//document.form.QTY.value=resultVal.QTY;
						
						document.getElementById("AVAILABLEQTY_"+index).value= addCommas(((resultVal.PICK_QTY) * 1 - (resultVal.RECEIVED_QTY) * 1));
						if(index>0)
						{
							var totalpickqty = getSumByLocAndBatch(index);
							totalpickqty= round_float(totalpickqty,3);		
							document.getElementById("AVAILABLEQTY_"+index).value= round_float(addCommas(((resultVal.PICK_QTY) * 1 - (resultVal.RECEIVED_QTY) * 1)-totalpickqty),3);
							}	
						//document.getElementById("AVAILABLEQTY_"+index).focus();
                                            if(document.form.SELECTION.checked){
                                                 addRow();
                                             }else{
                                                 document.getElementById("RECEIVINGQTY"+"_"+index).focus();
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
function addRow() {
	var table = document.getElementById("MULTIPLE_RECEIVING");
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
	"<a href=\"#\" onClick=\"javascript:popUpWin('batch_list_toreceive.jsp?ITEMNO='+form.ITEMNO.value+'&ORDERNO='+form.ORDERNO.value+'&ORDERLNO='+form.ORDERLNO.value+'&BATCH='+form.BATCH_"+rowCount+".value+'&INDEX="+rowCount+"');\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value, "+rowCount+");}\"><img src=\"images/populate.gif\" border=\"0\"></a>";
	var secondEmptyCell = row.insertCell(3);
	secondEmptyCell.innerHTML = "&nbsp";
	var availableqtyCell = row.insertCell(4);
	availableqtyCell.innerHTML ="<b>Available Qty : </b><INPUT name=\"AVAILABLEQTY_"+rowCount+"\" id=\"AVAILABLEQTY_"+rowCount+"\" type = \"TEXT\"  size=\"10\"  MAXLENGTH=\"80\" \> ";
	var thirdemptycell = row.insertCell(5);
	thirdemptycell.innerHTML = "&nbsp";
	var receivingQtyCell = row.insertCell(6);
	var receiveQtyText = "<b>Receiving Qty : </b><INPUT name=\"RECEIVINGQTY_"+rowCount+"\" ";
	if(serialselection){
		receiveQtyText = receiveQtyText + " value=\"1\"  ";
	}
		receiveQtyText = receiveQtyText + " id=\"RECEIVINGQTY_"+rowCount+"\" type = \"TEXT\"  size=\"10\"  MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value, "+rowCount+");}\"  >";
	receivingQtyCell.innerHTML = receiveQtyText;
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
			alert("Can not remove the default Receiving");
		} else {
			table.deleteRow(rowCount);
		}
	} catch (e) {
		alert(e);
	}
}
function processSerialSelection() {
	var table = document.getElementById("MULTIPLE_RECEIVING");
	var rowCount = table.rows.length;

	for(var index = 0; index<rowCount; index++) {
		var pickQty = document.getElementById("RECEIVINGQTY"+"_"+index);
             var curavailqty = document.getElementById("AVAILABLEQTY_"+index);
		if( document.form.SELECTION.checked == true )
		{
		pickQty.value = 1;
		pickQty.readOnly=true;
                curavailqty.readOnly=true;
}
		else
		{
			pickQty.value = "";
			pickQty.readOnly=false;
                        
		}

	}
}
function processSelection() {
	var table = document.getElementById("MULTIPLE_RECEIVING");
	var rowCount = table.rows.length;

	for(var index = 0; index<rowCount; index++) {
		var pickQty = document.getElementById("RECEIVINGQTY"+"_"+index);
             var curavailqty = document.getElementById("AVAILABLEQTY_"+index);
		if( document.form.SELECTION.checked == true )
		{
		pickQty.value = 1;
		pickQty.readOnly=true;
                curavailqty.readOnly=true;
}	}
}


function getavailqty(qty,index)
{
	var availqty=qty;
	if(index>0){
		
		var prevIndex = parseFloat(index);
		
		var totalpickqty = getSumByLocAndBatch(index);
		
		var curavailqty = document.getElementById("AVAILABLEQTY_"+index).value;
		curavailqty = removeCommas(curavailqty);
		curavailqty = round_float(curavailqty,3);
		
		document.getElementById("AVAILABLEQTY_"+index).value=round_float(addCommas(curavailqty-totalpickqty),3);
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
			var pickqty = document.getElementById("RECEIVINGQTY_"+i).value;
			pickqty = removeCommas(pickqty);
			pickqty = round_float(pickqty,3);
			qty = qty + parseFloat(pickqty);
			if(isNaN(qty))
			{
				qty = 0;	
			}
			
		}
		qty= round_float(qty,3);
		
	}
	return qty;
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
	document.form.BATCH_0.focus()
</script>
<%@ include file="footer.jsp"%>
