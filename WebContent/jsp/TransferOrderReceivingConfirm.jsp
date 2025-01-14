<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
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
    var recvingqty = frmRoot.RECEIVINGQTY.value;
    recvingqty = removeCommas(recvingqty);
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
   
    if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter Batch!");
		frmRoot.BATCH.focus();
		return false;
   }
     if(recvingqty=="" || recvingqty.length==0 ||recvingqty<=0)
	 {
		alert("Please Enter Receiving Qty!");
		frmRoot.REVERSEQTY.focus();
		return false;
   }
      
  
   if(isNaN(recvingqty)) {alert("Please enter valid Receiving Qty");document.form.RECEIVINGQTY.focus(); return false;}
   
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
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="";
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
       PICKEDQTY = strUtils.fString(request.getParameter("PICKEDQTY"));
       PICKINGQTY = strUtils.fString(request.getParameter("PICKINGQTY"));
       INVQTY = strUtils.fString(request.getParameter("QTY"));
       REF = strUtils.fString(request.getParameter("REF"));
       ORDERQTY = strUtils.fString(request.getParameter("ORDERQTY"));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       RECEIVINGQTY=strUtils.fString(request.getParameter("RECEIVINGQTY"));
       AVAILABLEQTY= StrUtils.formatNum(strUtils.fString(request.getParameter("AVAILABLEQTY")));
       ItemMstDAO itemmstdao = new ItemMstDAO();
      itemmstdao.setmLogger(mLogger);
      UOM = itemmstdao.getItemUOM(plant,ITEMNO);
       //Get podet
       ToDetDAO  _ToDetDAO  = new ToDetDAO();  
       List listQry =  _ToDetDAO.getTransferItemListByWMS(plant,ORDERNO,ITEMNO);
       for(int i =0; i<listQry.size(); i++) {
          Map m=(Map)listQry.get(i);
          
         // ORDERQTY =(String)m.get("qtyor"); // commmented by samatha as OrderQty is retrived above in URl
          RECEIVEDQTY =(String)m.get("qtyrc");
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
        <FONT color="#ffffff">Goods Receipt By Transfer Order</FONT>&nbsp;
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
          <INPUT name="ORDERQTY" class="inactivegry" type="TEXT" value="<%=StrUtils.formatNum(ORDERQTY)%>" size="35" MAXLENGTH="80" readonly/>
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
          <INPUT name="PICKEDQTY" class="inactivegry" type="TEXT" value="<%=StrUtils.formatNum(PICKEDQTY)%>" size="35" MAXLENGTH="80" readonly/>
        </TD>
        <TH WIDTH="7%" ALIGN="Right" height="35">Unit&nbsp;No :</TH>
        <TD width="35%" height="35">
          <INPUT name="ADD1" class="inactivegry" value="<%=ADD1%>"  type="TEXT" size="35" MAXLENGTH="80" readonly/>
        </TD>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">From&nbsp;Loc&nbsp;:</TH>
        <TD width="45%">
          <INPUT name="FROMLOC" class="inactivegry" readOnly     type="TEXT" value="<%=FROMLOC%>" size="35" MAXLENGTH="80"/>
        </TD>
        <TH WIDTH="7%" ALIGN="Right">Building :</TH>
        <TD width="35%">
          <INPUT name="ADD2" value="<%=ADD2%>" class="inactivegry" type="TEXT" size="35" MAXLENGTH="80" readonly/>
        </TD>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">To&nbsp;Loc&nbsp;:</TH>
        <TD width="45%">
            <INPUT name="TOLOC" type="TEXT" value="<%=TOLOC%>" size="35" class="inactivegry" readOnly  onkeypress="javascript:selectAll();" MAXLENGTH="80"/>
        </TD>
        <TH WIDTH="7%" ALIGN="Right">Street :</TH>
        <TD width="35%">
          <INPUT name="ADD3" value="<%=ADD3%>" class="inactivegry" type="TEXT" size="35" MAXLENGTH="80" readonly/>
        </TD>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">Temp&nbsp;Loc&nbsp;&nbsp;:</TH>
        <TD width="45%">
          <INPUT name="TEMPLOC" type="TEXT" value="<%=TEMP_TO%>" size="35" class="inactivegry" readOnly  onkeypress="javascript:selectAll();" MAXLENGTH="80"/>
        </TD>
        <TH WIDTH="7%"/>
        <TD width="35%"/>
      </TR>
      <TR>
       <TR>
        <TH WIDTH="12%" ALIGN="Right">Received Qty&nbsp;&nbsp;:</TH>
        <TD width="45%">
          <INPUT name="RECEIVEDQTY" class="inactivegry" type="TEXT" value="<%=RECEIVEDQTY%>" size="35" MAXLENGTH="80" readonly/>
        </TD>
        <TH WIDTH="7%"/>
        <TD width="35%"/>
      </TR>
      <TR></TR>
        <TH WIDTH="12%" ALIGN="Right" height="26">Batch:</TH>
        <TD width="45%" height="26">
          <INPUT name="BATCH" type="TEXT" value="<%=BATCH%>" size="35" onkeypress="if((event.keyCode=='13') && ( document.form.BATCH.value.length > 0)){validateBatchDetails();}" MAXLENGTH="40"/>
          <a href="#" onClick="javascript:popUpWin('batch_list_toreceive.jsp?ITEMNO='+form.ITEMNO.value+'&ORDERNO='+form.ORDERNO.value+'&ORDERLNO='+form.ORDERLNO.value);">
            <img src="images/populate.gif" border="0"/>
          </a>
        </TD>
        <TH WIDTH="7%" height="26"/>
        <TD width="35%" height="26"/>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">Availabe&nbsp;Qty&nbsp;&nbsp;:</TH>
        <TD width="45%">
          <INPUT name="AVAILABLEQTY" type="TEXT" value="<%=AVAILABLEQTY%>" size="35" MAXLENGTH="80" class="inactivegry"  readonly/>
        </TD>
        <TH WIDTH="7%"/>
        <TD width="35%"/>
      </TR>
      <TR>
        <TH WIDTH="12%" ALIGN="Right">Receiving&nbsp;Qty&nbsp;&nbsp;:</TH>
        <TD width="45%">
          <INPUT name="RECEIVINGQTY" type="TEXT" value="<%=RECEIVINGQTY%>" size="35" MAXLENGTH="80" onkeypress="if((event.keyCode=='13') && ( document.form.RECEIVINGQTY.value.length > 0)){document.form.REF.focus();}"/>
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
        <TD COLSPAN="2">
          <BR/>
          <!-- &amp;lt;INPUT name=&amp;quot;noOfLabelToPrint&amp;quot; type=&amp;quot;HIDDEN&amp;quot; size=&amp;quot;50&amp;quot; readonly MAXLENGTH=&amp;quot;80&amp;quot;/&amp;gt;&amp;lt;B&amp;gt;&amp;lt;CENTER&amp;gt;&amp;lt;%=REF%&amp;gt;&amp;lt;/B&amp;gt;&amp;lt;/TD&amp;gt;-->
        </TD>
      </TR>
      <TR>
        <TD WIDTH="35%" COLSPAN="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <INPUT class="Submit" type="BUTTON" value="Cancel"  onClick="window.location.href='TransferOrderReceiving.jsp?action=View&TONO='+form.ORDERNO.value "/>&nbsp;&nbsp;
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
			document.form.action="/track/TransferOrderServlet?Submit=TO_ReceiveConfirm";
			//document.form.action.value ="View";
            document.form.submit();
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
	document.form.BATCH.focus()
</script>
<%@ include file="footer.jsp"%>
