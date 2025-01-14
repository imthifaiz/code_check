<%@ include file="header.jsp" %>

<%
String title = "Consignment Product";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/receiving.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="javascript">
function onDelete(form)
{
   
    if (form.TONO.value.length < 1)
    {
    alert("Please Enter Consignment Order No !");
    form.DONO.focus();
    return false;
    }
    else{
     var mes=confirm("Do you want to Delete the Product ID?");
      if(mes==true)
      {
    	  document.form.action="/track/TransferOrderServlet?Submit=Delete";
	      document.form.submit();
      
      }
      else
      {  
      return  false;
      }
    }
    
}
function onUpdate()
{
	  if(document.form.QTY.value == ""){
	    alert("Please enter Quantity");
	    document.form.QTY.focus();
	    return false;
	  }
	  else if(!IsNumeric(removeCommas(document.form.QTY.value)))
	   {
	     alert(" Please Enter valid  Qty !");
	     form.QTY.focus();  form.QTY.select(); return false;
	  }
	  var qtyval = document.form.QTY.value;
	  if (qtyval.indexOf('.') == -1) qtyval += ".";
		var cdecNum = qtyval.substring(qtyval.indexOf('.')+1, qtyval.length);
		if (cdecNum.length > 3)
		{
			alert("Invalid more than 3 digits after decimal in QTY");
			document.form.QTY.focus();
			return false;
			
		}
	  else{
		  document.form.action="/track/TransferOrderServlet?Submit=Updatetodet";
	      document.form.submit();
	}
}
</script>

<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="ddb"  class="com.track.tables.TODET" />
<jsp:useBean id="dhb"  class="com.track.tables.TOHDR" />

<%
    String tono    = su.fString(request.getParameter("TONO"));
    String tolnno  = su.fString(request.getParameter("TOLNNO"));
    String item  = su.fString(request.getParameter("ITEM"));
    String desc  = su.fString(request.getParameter("DESC"));
    String prdRemarks  = su.fString(request.getParameter("PRDREMARKS"));
    String RFLAG=    (String) session.getAttribute("RFLAG");
    String qty1  = su.removeFormat(su.fString(request.getParameter("QTY")));
    Float qty = new Float(qty1);
    String uom  = su.fString(request.getParameter("UOM"));  
	String pickstatus = su.fString(request.getParameter("PICKSTATUS"));
    String qtyPick1  = su.removeFormat(su.fString(request.getParameter("QTYPICK")));
    Float qtyPick = new Float(qtyPick1);
    String qtyRc1  = su.removeFormat(su.fString(request.getParameter("QTYRC")));
    Float qtyRc = new Float(qtyRc1);
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    if ((tono.length()>0)||(tolnno.length()>0))
    {
      ddb.setTONO(tono);
      ddb.setTOLNNO(tolnno);
      ddb.setITEM(item);
      ddb.setITEMDESC(desc);
      ddb.setQTYOR(qty);
      ddb.setUNITMO(uom);
      ddb.setCOMMENT1(prdRemarks);
	
    }

%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
 
<form class="form-horizontal" name="form" method="post" action="/track/TransferOrderServlet?">
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Transfer Order No">Order Number:</label>
      <div class="col-sm-4">
     <INPUT type = "TEXT" class="form-control" size="20"  MAXLENGTH=20 name="TONO" value="<%=ddb.getTONO()%>" READONLY>
	  </div>
      </div>
            
      <div class="form-group">
      <label class="control-label col-sm-4" for="Order Line No">Order Line No:</label>
      <div class="col-sm-4">          
      <INPUT type = "TEXT"  class="form-control"  size="5"  MAXLENGTH=6 name="TOLNNO" value="<%=ddb.getTOLNNO()%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Product Class ID">Product ID:</label>
      <div class="col-sm-4">
      <INPUT type = "TEXT" size="20"  class="form-control"  MAXLENGTH=50 name="ITEM" value="<%=ddb.getITEM()%>" READONLY>
  	  </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Discription">Description:</label>
      <div class="col-sm-4">
      <INPUT  type = "TEXT" size="40" class="form-control" MAXLENGTH=100 name="DESC" 
      value="<%=su.formatHTML(su.replaceCharacters2Recv(ddb.getITEMDESC()))%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Picked Quantity">Picked Quantity:</label>
      <div class="col-sm-4">
      <INPUT  type = "TEXT" size="15" class="form-control" MAXLENGTH=20 name="PICKEDQTY" value="<%=su.formatNum(String.valueOf(qtyPick))%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Picked Quantity">Received Quantity:</label>
      <div class="col-sm-4">
      <INPUT  type = "TEXT" size="15" class="form-control" MAXLENGTH=20 name="RECVQTY" value="<%=su.formatNum(String.valueOf(qtyRc))%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="QTY">Quantity:</label>
      <div class="col-sm-4">
      <INPUT  type = "TEXT" size="20" class="form-control" MAXLENGTH=20 name="QTY" id="QTY" value="<%=su.formatNum(String.valueOf(ddb.getQTYOR()))%>" >
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-1" for="UOM">UOM:</label>
      <div class="col-sm-2">          
      <INPUT  type = "TEXT" size="20" class="form-control" MAXLENGTH=20 name="UOM" value="<%=ddb.getUNITMO()%>" READONLY>
      </div>
      </div>
      </div> 
          
      <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" type = "TEXT" size="40"  MAXLENGTH=100 name="PRDREMARKS" value="<%=ddb.getCOMMENT1()%>" >
      </div>
      </div>
      
      <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
      <INPUT type="Hidden" name="CUSTNO" value="<%=dhb.getCustNo()%>">
      <INPUT type="Hidden" name="RFLAG" value="5">
      
      <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default" onClick="javascript: history.go(-1)"><b>Back</b></button>&nbsp;&nbsp;
      
      <%if(pickstatus.equalsIgnoreCase("N")){%>
      <button type="button" class="Submit btn btn-default" name="Submit" onclick="return onDelete(document.form)"><b>Delete</b></button>&nbsp;&nbsp;
       <%} %>
      
      <button type="button" class="Submit btn btn-default" name="Submit" onclick="return onUpdate()"><b>Update</b></button>&nbsp;&nbsp; 
      </div>
      </div>
      </form>
      </div>
      </div>
      </div>
      
      <script>
	$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
	});
	</script>					
      
      <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	
	<jsp:param name="nobackblock" value="1" />
</jsp:include>