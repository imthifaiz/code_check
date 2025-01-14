<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>OutBound Order Issue</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'OutBoundPicking', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
  
    
function validatePO(form)//form
{
 
   var frmRoot=document.form;
  
   if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 )
	 {
		alert("Please Enter ORDERNO!");
		frmRoot.ORDERNO.focus();
		return false;
   }
   else  if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Please Enter ITEMNO!");
		frmRoot.ITEMNO.focus();
		return false;
   }
   else if(frmRoot.LOC.value=="" || frmRoot.LOC.value.length==0 )
	 {
		alert("Please Enter LOC!");
		frmRoot.LOC.focus();
		return false;
   }
  else if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter BATCH!");
		frmRoot.BATCH.focus();
		return false;
   }
  else if(isNaN(document.form.QTY.value)) {alert("Please enter valid QTY.");document.form.QTY.focus(); return false;}
  
  else if(document.form.QTY.value == 0  )
  {
     
     alert("Qty Should not be 0 or less than 0");
	   frmRoot.QTY.focus();
		 return false;
  }
  
  
 /* else if(document.form.QTY.value > document.form.ORDERQTY.value  )
  {
     
     alert("Qty Should less than or equal to Ordered Qty");
	   frmRoot.QTY.focus();
		 return false;
  }*/
  /*else if(document.form.QTY.value > document.form.CHECKQTY.value)
   {
     
     alert("Qty Should not be greater than batch qty:"+document.form.CHECKQTY.value);
	   frmRoot.QTY.focus();
		 return false;
   }*/
   else
   {
      return true;
   }
 
}

function onClear(){
 
  // document.form.action  = "MiscOrderReceiving.jsp?action=CLEAR";
  // document.form.submit();
  document.form.ORDERNO.value="";
  document.form.CUSTNAME.value="";
  document.form.ITEMNO.value="";
  document.form.ITEMDESC.value="";
  document.form.LOC.value="";
  document.form.BATCH.value="";
  document.form.REF.value="";
  document.form.ORDERQTY.value="";
  document.form.PICKEDQTY.value="";
  document.form.ISSUEDQTY.value="";
  document.form.ISSUINGQTY.value="";
  document.form.QTY.value="";
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
       
     //  POUtil  _POUtil= new POUtil();
       StrUtils strUtils=new StrUtils();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
      
    
       String  fieldDesc="";
       String   ORDERNO    = "",ITEMNO   = "", ITEMDESC  = "",
       LOC   = "" , CHECKQTY="",BATCH  = "", REF   = "",ORDERLNO,
       QTY = "",RECEIVEQTY="",CUSTNAME="",ORDERQTY="",PICKEDQTY="",ISSUEDQTY="",INVQTY="",ISSUINGQTY="",
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="";
       
       
       ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=strUtils.fString(request.getParameter("ORDERLNO"));
       CUSTNAME=strUtils.fString(request.getParameter("CUSTNAME"));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.fString(request.getParameter("ITEMDESC"));
       LOC = strUtils.fString(request.getParameter("LOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       PICKEDQTY = strUtils.fString(request.getParameter("PICKEDQTY"));
       ISSUEDQTY = strUtils.fString(request.getParameter("ISSUEDQTY"));
       ISSUINGQTY = strUtils.fString(request.getParameter("ISSUINGQTY"));
       INVQTY = strUtils.fString(request.getParameter("QTY"));
       REF = strUtils.fString(request.getParameter("REF"));
       ORDERQTY = strUtils.fString(request.getParameter("ORDERQTY"));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
    
   
     /* session = request.getSession();
      session.setAttribute("TRANSFERINQTY",CHECKQTY);*/
      
      
     if(action.equalsIgnoreCase("result"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("RESULT");
      }
      else if(action.equalsIgnoreCase("resulterror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
      }
      
      else if(action.equalsIgnoreCase("qtyerror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("QTYERROR");
       ArrayList list=(ArrayList)session.getAttribute("customerlistqry");

        for(int i=0;i<list.size();i++)
        {
          Map m = (Map)list.get(i);
          if(CUSTNAME.equalsIgnoreCase((String)m.get("custname")))
         {
           CONTACTNAME = (String)m.get("contactname");
           TELNO=(String)m.get("telno");
           EMAIL=(String)m.get("email");
           ADD1=(String)m.get("add1");
           ADD2=(String)m.get("add2");
           ADD3=(String)m.get("add3");
        }
      }
    }

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="/track/OrderIssuingServlet?"  >
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">OutBound Order Issue</FONT>&nbsp;
      </TH>
       
    </TR>
  </TABLE>
  <br>

  <font face="Times New Roman" size="4" >
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
  <font class="maingreen"><%=fieldDesc%></font>
  </font>
  </table>

  <TABLE border="0" CELLSPACING=1 WIDTH="100%" bgcolor="#dddddd">
    <TR>
      <TH WIDTH="12%" ALIGN="RIGHT">Order No</TH>
      <TD width="45%">
        <INPUT name="ORDERNO" type="TEXT" value="<%=ORDERNO%>" size="35" MAXLENGTH="80"/>
        <a href="#" onClick="javascript:popUpWin('OutBoundOrderIssueList.jsp?ORDERNO='+form.ORDERNO.value);">
          <img src="images/populate.gif" border="0"/>
        </a>
      </TD>
       <TH WIDTH="7"  > </TH>
        <TD width="35%"></TD>
    </TR>
    <TR>
         <TH WIDTH="12%" ALIGN="Right" >Customer Name : </TH>
         <TD width="35%"><INPUT name="CUSTNAME" class="inactive" type = "TEXT" value="<%=su.forHTMLTag(CUSTNAME)%>" size="35"  MAXLENGTH=80 readonly></TD>
         <TD width="7%"></TD>
          <TH WIDTH="35%" ALIGN="left" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Customer&nbsp; Details </TH>
           
    </TR>

    <TR>
         <TH WIDTH="12%" ALIGN="RIGHT" >Item No: </TH>
         <TD width="35%"><INPUT name="ITEMNO" type = "TEXT" value="<%=ITEMNO%>" size="35" class="inactive" readonly MAXLENGTH=80 />
         <!--<a href="#" onClick="javascript:popUpWin('item_list_putawaywms.jsp?ITEMNO='+form.ITEMNO.value);">
           <img src="images/populate.gif" border="0"/>
         </a>--></TD>
         
         <!-- <a href="#" onClick="javascript:popUpWin('inboundorderlist.jsp?PLANT='+PLANT+'&ITEM='+form.ITEM.value);"><img src="images/populate.gif" border="0"></a>-->
          <TH WIDTH="7%" ALIGN="Right" >Contact Name: </TH>
         <TD width="35%"><INPUT name="CONTACTNAME" class="inactive" value="<%=CONTACTNAME%>"  type = "TEXT"  size="35"  MAXLENGTH=80 readonly></TD>
      
    </TR>
     <TR>
         <TH WIDTH="12%" ALIGN="RIGHT" > &nbsp;Product Desc &nbsp;: </TH>
         <TD width="35%"><INPUT name="ITEMDESC" type = "TEXT" value="<%=ITEMDESC%>" size="35" class="inactive" readonly MAXLENGTH=80 /></TD>
          <TH WIDTH="7%" ALIGN="Right" >Telephone : </TH>
         <TD width="35%"><INPUT name="TELNO" class="inactive"  value="<%=TELNO%>"  type = "TEXT"  size="35"  MAXLENGTH=80 readonly></TD>
    </TR>
     <TR>
         <TH WIDTH="12%" ALIGN="RIGHT" > &nbsp;Order Qty&nbsp;: </TH>
         <TD WIDTH="35%"><INPUT name="ORDERQTY" type = "TEXT" value="<%=ORDERQTY%>" size="35"  MAXLENGTH=80 class="inactive" readonly /></TD>
         <TH WIDTH="7%" ALIGN="Right" >Email : </TH>
         <TD width="35%"><INPUT name="EMAIL" value="<%=EMAIL%>" class="inactive" type = "TEXT"  size="35"  MAXLENGTH=80 readonly></TD>
    </TR>
     <TR>
         <TH WIDTH="12%" ALIGN="RIGHT" >Picking Qty</TH>
         <TD width="45%">
          <!-- <a href="#" onClick="javascript:popUpWin('OutBoundPickingLoc.jsp?ITEMNO='+form.ITEMNO.value+'&Loc='+form.LOC.value);">
             <img src="images/populate.gif" border="0"/>
           </a>-->
           <INPUT name="PICKEDQTY" type="TEXT" value="<%=PICKEDQTY%>" size="35" MAXLENGTH="80" class="inactive" readonly/>
           </TD>
         <TH WIDTH="7%" ALIGN="Right" >Add1 : </TH>
         <TD width="35%"><INPUT name="ADD1"  class="inactive" value="<%=ADD1%>"  type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD> 
    </TR>
     <TR>
         <TH WIDTH="12%" ALIGN="RIGHT" >Issued Qty</TH>
         <TD width="35%">
          <a href="#" onClick="javascript:popUpWin('OutBoundIssueBatch.jsp?ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value+'&BATCH='+form.BATCH.value);">
          
         <!-- <a href="#" onClick="javascript:popUpWin('batch_list_reversewms.jsp?ORDERNO='+form.ORDERNO.value+'&ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value+'&BATCH='+form.BATCH.value);">-->
         <INPUT name="ISSUEDQTY" type="TEXT" value="<%=ISSUEDQTY%>" size="35" MAXLENGTH="80" class="inactive" readonly/>
         </a></TD>
      <TH WIDTH="7%" ALIGN="Right" >Add2 : </TH>
         <TD width="35%"><INPUT name="ADD2"  class="inactive" value="<%=ADD2%>"  type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD> 
         
    </TR>
     <TR>
         <TH WIDTH="12%" ALIGN="RIGHT" >Loc&nbsp; :</TH>
         <TD width="35%">
           <INPUT name="LOC" type="TEXT" value="<%=LOC%>" size="35" MAXLENGTH="80"/>
         </TD>
         <TH WIDTH="7%" ALIGN="Right" >Add3 : </TH>
         <TD width="35%"><INPUT name="ADD3" value="<%=ADD3%>" class="inactive" type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD>
    </TR>
    <TR>
      <TH WIDTH="12%" ALIGN="Right">Batch&nbsp; :</TH>
      <TD width="35%">
        <INPUT name="BATCH" type="TEXT" value="<%=BATCH%>" size="35" MAXLENGTH="40"/>
        <a href="#" onClick="javascript:popUpWin('OutBoundIssueBatch.jsp?ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value+'&BATCH='+form.BATCH.value);">
          <img src="images/populate.gif" border="0"/>
        </a>
      </TD>
      <TH WIDTH="7%">&nbsp;</TH>
      <TD width="35%">&nbsp;</TD>
    </TR>
    <TR>
      <TH WIDTH="12%" ALIGN="Right">Qty&nbsp;&nbsp;:</TH>
      <TD width="35%">
        <INPUT name="QTY" type="TEXT" class="inactive" readonly value="<%=QTY%>" size="35" MAXLENGTH="80"/>
      </TD>
      <TH WIDTH="7%">&nbsp;</TH>
      <TD width="35%">&nbsp;</TD>
    </TR>
    <TR>
      <TH WIDTH="12%" ALIGN="Right">Issuing Qty</TH>
      <TD width="35%">
        <INPUT name="ISSUINGQTY" type="TEXT" value="<%=ISSUINGQTY%>" size="35" MAXLENGTH="80"/>
      </TD>
      <TH WIDTH="7%">&nbsp;</TH>
      <TD width="35%">&nbsp;</TD>
    </TR>
     <TR>
         <TH WIDTH="12%" ALIGN="Right" >Remarks&nbsp;&nbsp;: </TH>
         <TD width="35%"><INPUT name="REF" type = "TEXT" value="<%=REF%>" size="35"  MAXLENGTH=80 /></TD>
         <TH WIDTH="7%" > </TH>
         <TD width="35%"></TD>
      </TR>
    <!--<TR>
         <TD COLSPAN = 2><BR>
         <INPUT name="noOfLabelToPrint" type="HIDDEN" size="35" readonly MAXLENGTH="80"><B><CENTER><%=REF%></B></TD>
    </TR>-->
    <TR>
         <TD WIDTH="35%" COLSPAN = 2>
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='../home'"/>&nbsp;&nbsp;
             <input type="Submit" value="OrderIssuing" name="action" onClick="return validatePO(document.form)"/>
             <INPUT class="Submit" type="BUTTON" value="Clear" onClick="return onClear();">
                 
         </TD>
         <TH WIDTH="10%" > </TH>
         <TH WIDTH="10%" > </TH>
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
 
    </TR>
</Table>
</FORM>

</HTML>
<%@ include file="footer.jsp"%>
