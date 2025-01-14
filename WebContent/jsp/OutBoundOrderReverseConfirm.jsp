<%@ include file="header.jsp" %>
<%@page import="com.track.dao.ItemMstDAO"%>

<%
String title = "Pick Return By Sales Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script src="js/general.js"></script>
<script src="js/receiving.js"></script>
<script src="js/calendar.js"></script>

<script>
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

function validateForm(){
 if(document.form.ITEM.value == ""){
    alert("Please select an item");
    document.form.ITEM.focus();
    return false;
 }
 
 if(document.form.REVERSEQTY.value == ""){
   alert("Please enter quantity");
   document.form.REVERSEQTY.focus();
   return false;
 }
 if(!IsNumeric(form.REVERSEQTY.value))
   {
     alert(" Please enter valid quantity !");
     form.REVERSEQTY.focus();  form.REVERSEQTY.select(); return false;
   }
   else if(document.form.REVERSEQTY.value == 0  )
  {      
         alert("Qty Should not be 0 ");
	 frmRoot.REVERSEQTY.focus();
	 return false;
  }
}

</script>

<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="ddb"  class="com.track.tables.DODET" />
<jsp:useBean id="dhb"  class="com.track.tables.DOHDR" />
<jsp:useBean id="imb"  class="com.track.tables.ITEMMST" />
<jsp:useBean id="cm"  class="com.track.tables.CLASSMST" />
<jsp:useBean id="am"  class="com.track.tables.ATTMST" />
<%


db.setmLogger(mLogger);
ddb.setmLogger(mLogger);
dhb.setmLogger(mLogger);
imb.setmLogger(mLogger);
am.setmLogger(mLogger);

    String action   = StrUtils.fString(request.getParameter("action")).trim();
    String  fieldDesc="",UOM="";
    String dono    = StrUtils.fString(request.getParameter("DONO"));
    String dolnno  = StrUtils.fString(request.getParameter("DOLNNO"));
    String custname = su.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUSTNAME")));
    String item  = StrUtils.fString(request.getParameter("ITEM"));
    String itemdesc   =su.replaceCharacters2Recv(StrUtils.fString(request.getParameter("ITEMDESC")));
    String loc  = StrUtils.fString(request.getParameter("LOC"));
    String batch  = StrUtils.fString(request.getParameter("BATCH"));
    String orderqty  = StrUtils.fString(request.getParameter("ORDERQTY"));
    String pickedqty  = StrUtils.fString(request.getParameter("PICKEDQTY"));
    String issuedqty = StrUtils.fString(request.getParameter("ISSUEDQTY"));
    //String reverseqty = StrUtils.fString(request.getParameter("REVERSEQTY"));
    String reverseqty = StrUtils.fString(request.getParameter("BALANCEQTY"));
    String ref = StrUtils.fString(request.getParameter("REF"));
    String TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
    DateUtils _dateUtils = new DateUtils();
    String curDate =_dateUtils.getDate();
    if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
     ItemMstDAO itemmstdao = new ItemMstDAO();
     itemmstdao.setmLogger(mLogger);
     String plant = (String)session.getAttribute("PLANT");
     //UOM = itemmstdao.getItemUOM(plant,item);
     UOM = StrUtils.fString(request.getParameter("UOM"));
     String  UOMQTY = StrUtils.fString(request.getParameter("UOMQTY"));
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    if ((dono.length()>0)||(dolnno.length()>0))
    {
      ddb.setDONO(dono);
      ddb.setDOLNNO(dolnno);
      ddb.setITEM(item);
      ddb.setUSERFLD1(itemdesc);
      ddb.setLOC(loc);
      ddb.setBATCH(batch);
      ddb.setUSERFLD2(StrUtils.removeFormat(orderqty));
      ddb.setUSERFLD3(StrUtils.removeFormat(pickedqty));
      ddb.setUSERFLD4(issuedqty );
      ddb.setUSERFLD5(reverseqty );
      ddb.setUSERFLD6(custname);
      ddb.setREMARK(ref);
      ddb.selectDODET(plant);
	  }
    
    if(action.equalsIgnoreCase("result"))
    {
       fieldDesc=(String)request.getSession().getAttribute("RESULT");
       fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
    }
   if(action.equalsIgnoreCase("resulterror"))
   {
      fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
      
      fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
   }
   if(action.equalsIgnoreCase("catchrerror"))
   {
      fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
      fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
   }
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
 <form class="form-horizontal" name="form" method="post"  action="/track/OrderIssuingServlet?">
 
 <center>
   <h2><small><%=fieldDesc%></small></h2>
  </center>
  
 
 <div class="form-group">
      <label class="control-label col-sm-4" for="Outbound Order No">Order Number:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type = "TEXT"  size="30"  MAXLENGTH=20 name="DONO" value="<%=ddb.getDONO()%>" READONLY>
      </div>
    </div>
 
 
 <div class="form-group">
      <label class="control-label col-sm-4" for="Product ID">Product ID:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type = "TEXT" size="30"  MAXLENGTH=50 name="ITEM" value="<%=ddb.getITEM()%>" READONLY>
      </div>
    </div>
 
 
 <div class="form-group">
      <label class="control-label col-sm-4" for="Product Description">Product Description:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type = "TEXT" size="30"  MAXLENGTH=20 name="ITEMDESC" value="<%=StrUtils.forHTMLTag(ddb.getUSERFLD1())%>" READONLY>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Location">Location:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type = "TEXT" size="30"  MAXLENGTH=20 name="LOC" value="<%=loc%>" READONLY>
      </div>
    </div>
    
 <div class="form-group">
      <label class="control-label col-sm-4" for="Batch">Batch:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type = "TEXT" size="30"  MAXLENGTH=20 name="BATCH" value="<%=ddb.getBATCH()%>" READONLY>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Order Quantity">Order Quantity:</label>
      <div class="col-sm-3">          
        <INPUT  class="form-control" type = "TEXT" size="15"  class="inactivegry"  MAXLENGTH=20 name="ORDERQTY" value="<%=StrUtils.formatNum("" + ddb.getQTYOR())%>" READONLY>
      </div>
      <div class="col-sm-1">
      <INPUT  class="form-control" type = "TEXT" size="8"  class="inactivegry"  MAXLENGTH=10 name="UOM" value="<%=UOM%>" READONLY>
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Picked Quantity">Picked Quantity:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type = "TEXT" size="30"  MAXLENGTH=20 name="PICKEDQTY" value="<%=StrUtils.formatNum(pickedqty)%>" READONLY>
      </div>
    </div>
    
    
     <!--  <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Issued Qty :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  class="inactive"  MAXLENGTH=20 name="ISSUEDQTY" value="<%=ddb.getUSERFLD4()%>" READONLY>
                     </TR>-->
    
   <div class="form-group">
      <label class="control-label col-sm-4" for="Reverse Quantity">Reverse Quantity:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type = "TEXT" size="30"  readonly  value="<%=StrUtils.formatNum(reverseqty)%>"  MAXLENGTH=20 name="REVERSEQTY" value="<%=ddb.getUSERFLD5()%>" >
      </div>
    </div> 
    
    
   <div class="form-group">
      <label class="control-label col-sm-4"  for="Transaction Date">Transaction Date:</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input class="form-control datepicker" name="TRANSACTIONDATE" type="TEXT" id ="TRANSACTIONDATE" value="<%=TRANSACTIONDATE%>" readonly="readonly" size="50" MAXLENGTH="20">
   		 	</div>
  		      </div>
  		      </div>
  		      
 <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type = "TEXT" size="30"    MAXLENGTH=20 name="REF" value="<%=ddb.getREMARK()%>" >
      </div>
    </div>
  		
  		<INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="CUSTNO" value="<%=dhb.getCustNo()%>">
         <INPUT type="Hidden" name="CUSTNAME" value="<%=custname%>">
         <INPUT type = "Hidden" name="DOLNNO" value="<%=ddb.getDOLNNO()%>" >
         <INPUT type = "Hidden" name="QTYISSUE" value="<%=issuedqty%>" >
         <INPUT type = "Hidden" name="UOMQTY" value="<%=UOMQTY%>" >
  		   
  		   
 <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      
      	<button type="Submit" class="Submit btn btn-default" value="ReverseConfirm" name="action"><b>Return Confirm</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Back" onClick="window.location.href='OutBoundOrderReverse.jsp?action=View&DONO='+form.DONO.value"><b>Back</b></button>&nbsp;&nbsp;
      	

      </div>
    </div>
    
    </form>
    </div>
    </div>
    </div>
    

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
