<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Goods Receipt Reversal By Inbound Order</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'InboundOrderList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
  
  function selectAll() {
     var tempval=document.form.BATCH;//eval("document."+theField)
     tempval.focus()
     tempval.select()
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
   
    if(frmRoot.LOC.value=="" || frmRoot.LOC.value.length==0 )
	 {
		alert("Please Enter LOC!");
		frmRoot.LOC.focus();
		return false;
   }
   
   if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Please Enter ITEMNO!");
		frmRoot.ITEMNO.focus();
		return false;
   }
   
    if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter BATCH!");
		frmRoot.BATCH.value="NOBATCH";
		frmRoot.BATCH.focus();
		return false;
   }
     if(frmRoot.REVERSEQTY.value=="" || frmRoot.REVERSEQTY.value.length==0 ||frmRoot.REVERSEQTY.value<=0)
	 {
		alert("Please Enter valid REVERSEQTY!");
		frmRoot.REVERSEQTY.focus();
		return false;
   }
      
      
   if(isNaN(removeCommas(document.form.REVERSEQTY.value))) {alert("Please enter valid REVERSEQTY.");document.form.REVERSEQTY.focus(); return false;}
   
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
		alert("Please Enter ORDERNO!");
		frmRoot.ORDERNO.focus();
		return false;
   }
   
   else if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
		alert("Please Enter ITEMNO!");
		frmRoot.ITEMNO.focus();
		return false;
   }
   
   if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter BATCH!");
		frmRoot.BATCH.focus();
		return false;
   }
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
  document.form.RECEIVEQTY.value="";
  document.form.REVERSEQTY.value="";
 
  
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
        String PLANT = (String) session.getAttribute("PLANT");
      
       //String action = strUtils.fString(request.getParameter("action")).trim();
       String  fieldDesc="";
       String   ORDERNO    = "",ORDERLNO="",CUSTNAME = "", ITEMNO   = "", ITEMDESC  = "",
       LOC   = "" , BATCH  = "", REF   = "",
       ORDERQTY = "",RECEIVEQTY="", REVERSEDQTY="",REVERSEQTY="",BALANCEQTY="",UOM="",UOMQTY="";
       
       ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=strUtils.fString(request.getParameter("ORDERLNO"));
       CUSTNAME = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUSTNAME")));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")));
     //  ITEMDESC= new ItemMstDAO().getItemDesc(PLANT ,ITEMNO);
       System.out.println("ITEMDESC :: "+ITEMDESC);
       LOC = strUtils.fString(request.getParameter("LOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       REF = strUtils.fString(request.getParameter("REF"));
       ORDERQTY = StrUtils.formatNum(strUtils.fString(request.getParameter("ORDERQTY")));
       RECEIVEQTY = StrUtils.formatNum( strUtils.fString(request.getParameter("RECEIVEQTY")));
       REVERSEDQTY = strUtils.fString(request.getParameter("REVERSEDQTY"));
       REVERSEQTY= strUtils.fString(request.getParameter("REVERSEQTY"));
       ItemMstDAO itemmstdao = new ItemMstDAO();
       itemmstdao.setmLogger(mLogger);
       //UOM = itemmstdao.getItemUOM(PLANT,ITEMNO);
       UOM =strUtils.fString(request.getParameter("UOM"));
       UOMQTY =strUtils.fString(request.getParameter("UOMQTY"));
      if(action.equalsIgnoreCase("CLEAR"))
      {
        ORDERNO = "";
        ORDERLNO="";
        CUSTNAME = "";
        ITEMNO = "";
        ITEMDESC = "";
        LOC = "";
        BATCH = "";
        REF = "";
        ORDERQTY = "";
        RECEIVEQTY = "";
        REVERSEDQTY = "";
        REVERSEQTY= "";
        BALANCEQTY=strUtils.fString(request.getParameter("BALANCEQTY"));
       
     
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
      else if(action.equalsIgnoreCase("catchreverseerror")) 
      {
 
       fieldDesc=(String)request.getSession().getAttribute("CATCHREVERSERROR");
       
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
      
   

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="/track/OrderReceivingByPOServlet?"  >
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Goods Receipt Reversal By Inbound Order</FONT>&nbsp;
      </TH>
       
    </TR>
  </TABLE>
  <br>


 <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
   <%=fieldDesc%>
 </table>

  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >*Order No. : </TH>
         <TD>
                <INPUT name="ORDERNO" type = "TEXT" value="<%=ORDERNO%>" size="50"  MAXLENGTH=20 class="inactivegry"  readonly>
               <!-- <a href="#" onClick="javascript:popUpWin('inboundorderlistrev.jsp?ORDERNO='+form.ORDERNO.value);"><img src="images/populate.gif" border="0"></a>-->
           
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Supplier Name : </TH>
         <TD><INPUT name="CUSTNAME" class="inactivegry" type = "TEXT" value="<%=su.forHTMLTag(CUSTNAME)%>" size="50"  MAXLENGTH=80 readonly></TD>
    </TR>
   

    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Item No: </TH>
         <TD><INPUT name="ITEMNO" type = "TEXT" value="<%=ITEMNO%>" size="50"  MAXLENGTH=80 class="inactivegry"  readonly>
        <!-- <a href="#" onClick="javascript:popUpWin('item_list_reverse.jsp?ORDERNO='+form.ORDERNO.value+'&ITEMNO='+form.ITEMNO.value);">
           <img src="images/populate.gif" border="0"/>
         </a>--></TD>
      
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > &nbsp;Product Desc &nbsp;: </TH>
         <TD><INPUT class="inactivegry" name="ITEMDESC" type = "TEXT" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="50"  MAXLENGTH=80 readonly></TD>
         
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Order Qty&nbsp;&nbsp;: </TH>
         <TD><INPUT name="ORDERQTY"  class="inactivegry" type = "TEXT" value="<%=ORDERQTY%>" size="50"  MAXLENGTH=80 readonly>
          &nbsp;&nbsp;&nbsp;<b><%=IDBConstants.UOM_LABEL %> :</b>&nbsp;<INPUT name="UOM" class="inactivegry" type = "TEXT" value="<%=UOM%>" size=10"  MAXLENGTH=15 readonly>
         </TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Received Qty&nbsp;&nbsp;: </TH>
         <TD><INPUT name="RECEIVEQTY" class="inactivegry" type = "TEXT" value="<%=RECEIVEQTY%>"  size="50"  MAXLENGTH=80 readonly></TD>
    </TR>
    
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Loc&nbsp; : </TH>
         <TD><INPUT name="LOC"  type = "TEXT" value="<%=LOC%>" size="50"  MAXLENGTH=80  class="inactivegry">
        <!-- <a href="#" onClick="javascript:popUpWin('loc_list_reversewms.jsp?ORDERNO='+form.ORDERNO.value+'&ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value);">
                 
           <img src="images/populate.gif" border="0"/> -->
         </a></TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Batch&nbsp; : </TH>
         <TD><INPUT name="BATCH" type = "TEXT"  value="<%=BATCH%>" size="50"    class="inactivegry"     onkeypress="javascript:selectAll();" readonly MAXLENGTH=40 >
       
         </a>
         </TD>
    </TR>
    
     <TR>
      
         <TD><INPUT name="REVERSEDQTY" class="inactivegry"  type ="hidden"  value="<%=REVERSEDQTY%>"  size="50"  MAXLENGTH=80 readonly></TD>
    </TR>
       <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Reversing Qty&nbsp;&nbsp;: </TH>
         <TD><INPUT name="REVERSEQTY" value="<%=RECEIVEQTY%>"type = "TEXT" class="inactivegry"  size="50"  MAXLENGTH=80 readonly ></TD>
    </TR>
     <TR> 
         <TH WIDTH="35%" ALIGN="RIGHT" >Remarks&nbsp;&nbsp;: </TH>
         <TD><INPUT name="REF" type = "TEXT" value="<%=REF%>" size="50"  MAXLENGTH=80 ></TD>
    </TR>
    
    <TR>
         <TD COLSPAN = 2><BR>
         <INPUT name="noOfLabelToPrint" type="HIDDEN" size="50" readonly MAXLENGTH="80"/><B><CENTER></B></TD>
    </TR>
    <TR>
         <TD COLSPAN = 2>
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             
             <input type="Submit" value="ReverseConfirm" name="action" onClick="return validatePO(document.form)"/>&nbsp;&nbsp; 
            <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='InboundsOrderReverse.jsp?action=View&PONO='+form.ORDERNO.value"/>&nbsp;&nbsp;
            <!--  <INPUT class="Submit" type="BUTTON" value="Back"   onClick="javascript: history.go(-1)">&nbsp;&nbsp; -->
         
         
         </TD>
    </TR>
   
</TABLE>
</center>
<Table border=0 bgcolor="#dddddd">
 <TR  bgcolor="#dddddd">
  <INPUT     name="ORDERLNO"  type ="hidden" value="<%=ORDERLNO%>" size="1"   MAXLENGTH=80 ></TD>
  <INPUT     name="BALANCEQTY"  type ="hidden" value="<%=BALANCEQTY%>" size="1"   MAXLENGTH=80 ></TD>
   <INPUT     name="LOGIN_USER"  type ="hidden" value="<%=sUserId%>" size="1"   MAXLENGTH=80 ></TD>
   <INPUT name="UOMQTY" type="hidden" value="<%=UOMQTY%>"	MAXLENGTH=80>  
    </TR>
</Table>
</FORM>

</HTML>
<%@ include file="footer.jsp"%>
