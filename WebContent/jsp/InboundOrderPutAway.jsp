<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Inbound Order Transfer In</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'InboundOrderList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
  
    
function validatePO(form)//form
{
 
   var frmRoot=document.form;
  
   if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Please Enter ITEMNO!");
		frmRoot.ITEMNO.focus();
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
  else if(document.form.QTY.value > document.form.CHECKQTY.value)
   {
     
     alert("Qty Should not be greater than batch qty:"+document.form.CHECKQTY.value);
	   frmRoot.QTY.focus();
		 return false;
   }
   else
   {
      return true;
   }
 
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
       String   ORDERNO    = "",ORDERLNO="",CUSTNAME = "", ITEMNO   = "", ITEMDESC  = "",
       FROMLOC   = "" ,TOLOC   = "" , CHECKQTY="",BATCH  = "", REF   = "",
       QTY = "",RECEIVEQTY="";
       
       
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.fString(request.getParameter("ITEMDESC"));
       FROMLOC = strUtils.fString(request.getParameter("LOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       FROMLOC = strUtils.fString(request.getParameter("FROMLOC"));
       TOLOC = strUtils.fString(request.getParameter("TOLOC"));
   
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
      
      else if (action.equalsIgnoreCase("resultcatcherror"))
      {
 
     
       fieldDesc=(String)request.getSession().getAttribute("RESULTCATCHERROR");
      }
   


%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="/track/LocationTransferServlet?"  >
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Inbound Transfer In </FONT>&nbsp;
      </TH>
       
    </TR>
  </TABLE>
  <br>

  <font face="Times New Roman" size="4" >
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
  <font class="maingreen"><%=fieldDesc%></font>
  </font>
  </table>

  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
   

    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Item No: </TH>
         <TD><INPUT name="ITEMNO" type = "TEXT" value="<%=ITEMNO%>" size="50"  MAXLENGTH=80 >
         <a href="#" onClick="javascript:popUpWin('item_list_putawaywms.jsp?ITEMNO='+form.ITEMNO.value);">
           <img src="images/populate.gif" border="0"/>
         </a></TD>
         
         <!-- <a href="#" onClick="javascript:popUpWin('inboundorderlist.jsp?PLANT='+PLANT+'&ITEM='+form.ITEM.value);"><img src="images/populate.gif" border="0"></a>-->
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > &nbsp;Product Desc &nbsp;: </TH>
         <TD><INPUT name="ITEMDESC" type = "TEXT" value="<%=ITEMDESC%>" size="50"  MAXLENGTH=80 readonly></TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >From Loc&nbsp; : </TH>
         <TD>
           <INPUT name="FROMLOC" type="TEXT" value="<%=FROMLOC%>" size="50" MAXLENGTH="80" readonly/>
         </TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Batch&nbsp; : </TH>
         <TD><INPUT name="BATCH" type = "TEXT" value="<%=BATCH%>" size="50"  MAXLENGTH=40  readonly>
         <a href="#" onClick="javascript:popUpWin('batch_list_putawaywms.jsp?ITEMNO='+form.ITEMNO.value+'&BATCH='+form.BATCH.value);">
           <img src="images/populate.gif" border="0"/>
         </a></TD>
      
         
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Qty&nbsp;&nbsp;: </TH>
         <TD><INPUT name="QTY" type = "TEXT" value="<%=QTY%>" size="50"  MAXLENGTH=80 ></TD>
    </TR>
       <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >To Loc&nbsp;&nbsp;: </TH>
         <TD>
           <INPUT name="TOLOC" type="TEXT" value="<%=TOLOC%>" size="50" MAXLENGTH="80" />
         </TD>
    </TR>
    <TR>
         <TD COLSPAN = 2><BR>
         <INPUT name="noOfLabelToPrint" type="HIDDEN" size="50" readonly MAXLENGTH="80"/><B><CENTER><%=REF%></B></TD>
    </TR>
    <TR>
         <TD COLSPAN = 2>
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='../home'"/>&nbsp;&nbsp;
             <input type="Submit" value="TransferIn" name="action" onClick="return validatePO(document.form)"/>
                 
         </TD>
    </TR>
   
</TABLE>
</center>
<Table border=0 bgcolor="#dddddd">
 <TR  bgcolor="#dddddd">
  <INPUT     name="CHECKQTY"  type ="hidden" value="<%=QTY%>" size="1"   MAXLENGTH=80 ></TD>
  <INPUT     name="LOGINUSER"  type ="hidden" value="<%=sUserId %>" size="1"   MAXLENGTH=80 ></TD>
    </TR>
</Table>
</FORM>

</HTML>
<%@ include file="footer.jsp"%>
