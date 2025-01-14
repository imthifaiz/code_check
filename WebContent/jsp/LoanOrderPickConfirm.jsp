


<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Pick/Issue By Loan Order</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'LoanOrderPicking', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
      
function validateform(form)//form
{
 
   var frmRoot=document.form;
   var pickQty = document.form.PICKINGQTY.value;
    var Aailqty = document.form.QTY.value;
    var orQty=document.form.ORDERQTY.value;
    pickQty = removeCommas(pickQty);
    Aailqty=removeCommas(Aailqty);
    orQty= removeCommas(orQty);
     var alreadyPickedQty=document.form.PICKED_QTY.value;
     alreadyPickedQty= removeCommas(alreadyPickedQty);
    
 if(frmRoot.BATCH.value=="" || frmRoot.BATCH.value.length==0 )
	 {
		alert("Please Enter BATCH!");
		frmRoot.BATCH.focus();
		return false;
         }
  else if(isNaN(pickQty)) {alert("Please enter valid Pick QTY.");document.form.PICKINGQTY.focus(); return false;}
  else if( parseFloat(pickQty) > parseFloat(orQty) ) {alert("Entered pick Qty is Greater than Order Qty.");document.form.PICKINGQTY.focus(); return false;}
   else if( parseFloat(alreadyPickedQty)+parseFloat(pickQty) > parseFloat(orQty) ) {alert("Entered pick Qty is Greater than the Available balance to Pick.");document.form.PICKINGQTY.focus(); return false;}
  else if(parseFloat(pickQty) > parseFloat(Aailqty)  ) {alert("Entered pick Qty is Greater than Available Qty.");document.form.PICKINGQTY.focus(); return false;}

  else if(pickQty <= 0  )
  {
     
     alert("Qty Should not be 0 or less than 0");
	   frmRoot.QTY.focus();
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
  document.form.LOC.value="";
  document.form.BATCH.value="";
  document.form.REF.value="";
  document.form.ORDERQTY.value="";
  document.form.QTY.value="";
  document.form.CONTACTNAME.value="";
  document.form.TELNO.value="";
  document.form.EMAIL.value="";
  document.form.ADD1.value="";
  document.form.ADD2.value="";
  document.form.ADD3.value="";
  document.form.FRLOC.value="";
  document.form.TOLOC.value="";
 
  
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
       
     //POUtil  _POUtil= new POUtil();
       StrUtils strUtils=new StrUtils();
       LoanHdrDAO _loHdrDAO = new LoanHdrDAO();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
        String plant=(String)session.getAttribute("PLANT");
    
       String  fieldDesc="";
       String   ORDERNO    = "",ITEMNO   = "", ITEMDESC  = "",UOM="",
       LOC   = "" ,LOC1="" ,CHECKQTY="",BATCH  = "", REF   = "",ORDERLNO,
       QTY = "",RECEIVEQTY="",CUSTNAME="",ORDERQTY="",INVQTY="",PICKINGQTY="",PICKED_QTY="",
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="";
              
       ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=strUtils.fString(request.getParameter("ORDERLNO"));
       //CUSTNAME=strUtils.fString(request.getParameter("CUSTNAME"));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")));
       LOC = strUtils.fString(request.getParameter("FRLOC"));
       LOC1 = strUtils.fString(request.getParameter("TOLOC"));
       PICKED_QTY=strUtils.fString(request.getParameter("PICKED_QTY"));
        
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = StrUtils.formatNum(strUtils.fString(request.getParameter("QTY")));
       PICKINGQTY = strUtils.fString(request.getParameter("PICKINGQTY"));
     
       REF = strUtils.fString(request.getParameter("REF"));
       INVQTY = strUtils.fString(request.getParameter("QTY"));
       ORDERQTY = StrUtils.formatNum(strUtils.fString(request.getParameter("ORDERQTY")));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       
       ItemMstDAO itemmstdao = new ItemMstDAO();
       itemmstdao.setmLogger(mLogger);
       UOM = itemmstdao.getItemUOM(plant,ITEMNO);
       //getLoanOrderAssigneeDetails
       //ArrayList list=(ArrayList)session.getAttribute("customerlistqry3");
       ArrayList list= _loHdrDAO.getLoanOrderAssigneeDetails(plant, ORDERNO);
       
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
      
    }

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="/track/LoanOrderPickingServlet?"  >
  <INPUT type = "hidden" name="PICKED_QTY" value = "<%=PICKED_QTY%>">
   <INPUT type = "hidden" name="PLANT" value = "<%=plant%>">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11">
        <FONT color="#ffffff">Pick/Issue By Loan Order</FONT>&nbsp;
      </TH>
       
    </TR>
  </TABLE>
  <br>

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
           <INPUT name="ORDERQTY" type="TEXT" value="<%=ORDERQTY%>" size="15" MAXLENGTH="20" class="inactivegry" readonly/>
           &nbsp; <INPUT name="UOM" type="TEXT" value="<%=UOM%>" size="11" MAXLENGTH="10" class="inactivegry" readonly/>
         </TD>
         <TH WIDTH="8%" ALIGN="Right" >Email : </TH>
         <TD width="37%"><INPUT name="EMAIL" value="<%=EMAIL%>" class="inactivegry" type = "TEXT"  size="35"  MAXLENGTH=80 readonly></TD>
    </TR>
     <TR>
         <TH WIDTH="13%" ALIGN="RIGHT" >Fr Loc&nbsp; :</TH>
         <TD width="42%">
             <INPUT name="LOC" type="TEXT" value="<%=LOC%>" size="35" class="inactivegry" MAXLENGTH="80" readonly/>
         </TD>
         
         <TH WIDTH="8%" ALIGN="Right" >Unit&nbsp;No : </TH>
         <TD width="37%"><INPUT name="ADD1"  class="inactivegry" value="<%=ADD1%>"  type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD> 
    </TR>
     <TR>
      <TH WIDTH="13%" ALIGN="RIGHT" >To Loc&nbsp; :</TH>
         <TD width="42%">
             <INPUT name="LOC1" type="TEXT" value="<%=LOC1%>" size="35" class="inactivegry" MAXLENGTH="80" readonly/>
         </TD>
       
      <TH WIDTH="8%" ALIGN="Right" >Building : </TH>
         <TD width="37%"><INPUT name="ADD2"  class="inactivegry" value="<%=ADD2%>"  type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD> 
         
    </TR>
     <TR>
        <TH WIDTH="13%" ALIGN="RIGHT" >Batch&nbsp; :</TH>
         <TD width="42%">
           
         <INPUT name="BATCH" type="TEXT" value="<%=BATCH%>" size="35" MAXLENGTH="40"/>
         <a href="#" onClick="javascript:popUpWin('OutBoundPickingBatch.jsp?ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value+'&BATCH='+form.BATCH.value);">
           <img src="images/populate.gif" border="0"/>
         </a>
         </TD>
         <TH WIDTH="8%" ALIGN="Right" >Street : </TH>
         <TD width="37%"><INPUT name="ADD3" value="<%=ADD3%>" class="inactivegry" type = "TEXT" size="35"  MAXLENGTH=80 readonly ></TD>
    </TR>
    <TR>
     <TH WIDTH="13%" ALIGN="RIGHT" >Available Qty&nbsp;&nbsp;:</TH>
         <TD width="42%">
           <INPUT name="QTY" type="TEXT" class="inactivegry" readonly value="<%=QTY%>" size="35" MAXLENGTH="80"/>
         </TD>
       <TH WIDTH="13%" ALIGN="Right">Remarks&nbsp;&nbsp;:</TH>
      <TD width="42%">
        <INPUT name="REF" type="TEXT" value="<%=REF%>" size="35" MAXLENGTH="80"/>
      </TD>
    </TR>
    <TR>
    <TH WIDTH="13%" ALIGN="Right">Picking Qty</TH>
      <TD width="42%">
        <INPUT name="PICKINGQTY" type="TEXT" value="<%=PICKINGQTY%>" size="35" MAXLENGTH="80"/>
      </TD>
      <TH WIDTH="13%" ALIGN="Right">&nbsp;&nbsp;</TH>
      <TD width="42%">&nbsp;&nbsp; </TD>
  
    </TR>
    
    
   
    <TR>
         <TD WIDTH="35%" COLSPAN = 2>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
             <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='LoanOrderPicking.jsp?action=View&DONO='+form.ORDERNO.value+'&PLANT='+form.PLANT.value "/>&nbsp;&nbsp;
             <input type="Submit" value="Pick/Issue Confirm" name="action" onClick="return validateform(document.form)"/>
       
         </TD>
         <TH WIDTH="8%" > </TH>
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
  </TR>
</Table>
</FORM>

</HTML>
<%@ include file="footer.jsp"%>
