<%@page import="com.track.dao.PlantMstDAO"%>
<%@ include file="header.jsp" %>

<%
String title = "Rental Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<script language="JavaScript" type="text/javascript" src="js/receiving.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

function validateForm(){
if(document.form.QTY.value == ""){
    alert("Please enter Quantity");
    document.form.QTY.focus();
    return false;
  }
}

function onUpdate(form)
{
	
	 var frmRoot=document.form;
	 var table = document.getElementById("MULTI_IB_REMARKS");
	 var rowCount = table.rows.length;
	 var checkcnt = rowCount-1;
	 document.form.DYNAMIC_REMARKS_SIZE.value = rowCount;
	 for(var i=0;i<rowCount;i++){
		   var prdremarks= document.getElementById("PRDREMARKS"+"_"+i);
		   
	 }	
	if(document.form.qtyor.value == ""){
	    alert("Please enter Quantity");
	    document.form.qtyor.focus();
	    return false;
	  }
	  else if(!IsNumeric(removeCommas(document.form.qtyor.value)))
	   {
	     alert(" Please Enter Valid  Quantity !");
	     form.qtyor.focus();  
	     form.qtyor.select(); 
	     return false;
	  }  
	 
	  else if(document.form.RENTALPRICE.value == ""){
	    alert("Please Enter Rental Price");
	    document.form.RENTALPRICE.focus();
	    return false;
	  }
	 var priceamt = document.form.RENTALPRICE.value;
	  if (priceamt.indexOf('.') == -1) priceamt += ".";
		var decNum = priceamt.substring(priceamt.indexOf('.')+1, priceamt.length);
		var declength =	parseInt(document.getElementById("numberOfDecimal").value);
		if (decNum.length > declength)
		{
			alert("Invalid more than "+declength+" digits after decimal in RENTAL PRICE");
			document.form.RENTALPRICE.focus();
			return false;
			
		}
	
	  else{
	  document.form.action = "/track/loanorderservlet?Submit=Updateloandet";
	      document.form.submit();
			 
	  }
}

function onDelete(form)
{
   
    if (form.DONO.value.length < 1)
    {
    alert("Please Enter Rental Order No !");
    form.DONO.focus();
    return false;
    }
    else{
     var mes=confirm("Do you want to Delete the Product ID !");
      if(mes==true)
      {
    	  document.form.action="/track/loanorderservlet?Submit=Delete";
	      document.form.submit();
      }
      else
      {  
      return  false;
      }
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
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<%

LoanUtil _loanUtil = new LoanUtil();
PlantMstDAO plantMstDAO = new PlantMstDAO();
    String plant=su.fString((String)session.getAttribute("PLANT"));
    String dono    = su.fString(request.getParameter("DONO"));
    String dolnno  = su.fString(request.getParameter("DOLNNO"));
    String item  = su.fString(request.getParameter("ITEM"));
    String itemdesc  = su.fString(request.getParameter("ITEMDESC"));
    String PRODGST  = su.fString(request.getParameter("PRODGST"));
    String STOCKONHAND  = su.fString(request.getParameter("STOCKONHAND"));
    String OUTGOINGQTY = su.fString(request.getParameter("OUTGOINGQTY"));
    String uom =su.fString(request.getParameter("UOM"));
    String PRDREMARKS =su.fString(request.getParameter("PRDREMARKS"));
    String qtyor =su.fString(request.getParameter("qtyor"));
    String RENTALPRICE  = su.fString(request.getParameter("RENTALPRICE"));
	String LISTPRICE  = su.fString(request.getParameter("LISTPRICE"));
    String rflag  = su.fString(request.getParameter("RFLAG"));
    String pickstatus = su.fString(request.getParameter("PICKSTATUS"));
    String qtyPick1  = su.removeFormat(su.fString(request.getParameter("QTYPICK")));
    Float qtyPick = new Float(qtyPick1);
    String qtyRc1  = su.removeFormat(su.fString(request.getParameter("QTYRC")));
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
    Float qtyRc = new Float(qtyRc1);
        
        db.setmLogger(mLogger);
        ddb.setmLogger(mLogger);
        dhb.setmLogger(mLogger);
        cm.setmLogger(mLogger);
        am.setmLogger(mLogger);
        
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    if ((dono.length()>0)||(dolnno.length()>0))
    {
      ddb.setDONO(dono);
      ddb.setDOLNNO(dolnno);
      ddb.setITEM(item);
      //ddb.selectDODET();
	 
    }
    double rentalPriceVal ="".equals(RENTALPRICE) ? 0.0d :  Double.parseDouble(RENTALPRICE);
    double listPriceVal ="".equals(LISTPRICE) ? 0.0d :  Double.parseDouble(LISTPRICE);
    
    RENTALPRICE = StrUtils.addZeroes(rentalPriceVal, numberOfDecimal);
    LISTPRICE = StrUtils.addZeroes(listPriceVal, numberOfDecimal);
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 <input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
<form class="form-horizontal" name="form" method="post" action="/track/loanorderservlet?">
      
      <div class="col-sm-6"> 
      <div class="form-group">
      <label class="control-label col-sm-4" for="Transfer Order No">Order Number:</label>
      <div class="col-sm-6">
      <INPUT type = "TEXT" class="form-control" size="20"  MAXLENGTH=20 name="DONO" value="<%=ddb.getDONO()%>" READONLY>
	  </div>
      </div>
            
      <div class="form-group">
      <label class="control-label col-sm-4" for="Order Line No">Order Line No:</label>
      <div class="col-sm-6">          
      <INPUT type = "TEXT"  class="form-control"  size="50"  MAXLENGTH=6 name="DOLNNO" value="<%=ddb.getDOLNNO()%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Product VAT">Product VAT:</label>
      <div class="col-sm-6">
      <INPUT type = "TEXT" size="50"  class="form-control"  MAXLENGTH=50 name="PRODGST" value="<%=PRODGST%>" READONLY>
  	  </div>
      </div>
      
       <div class="form-group">
      <label class="control-label col-sm-4" for="Out going Quantity">Outgoing Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="OUTGOINGQTY" value="<%=OUTGOINGQTY%>" class="form-control" READONLY>
      </div>
      </div>
      
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Stock On Hand">Stock On Hand:</label>
      <div class="col-sm-6">
      <INPUT  type = "TEXT" size="15" class="form-control" MAXLENGTH=20 name="STOCKONHAND" value="<%=STOCKONHAND%>" READONLY>
      </div>
      </div>
	  
	  <div class="form-group">
      <label class="control-label col-sm-4" for="Stock On Hand">Rental Price:</label>
      <div class="col-sm-6">
      <INPUT  type = "TEXT" size="15" class="form-control" MAXLENGTH=20 name="LISTPRICE" value="<%=LISTPRICE%>" READONLY>
      </div>
      </div>
      
      </div>
      
      <div class="col-sm-6">
      <div class="form-group">
      <label class="control-label col-sm-4" for="Product Class ID">Product ID:</label>
      <div class="col-sm-6">
      <INPUT type = "TEXT" size="50"  class="form-control"  MAXLENGTH=50 name="ITEM" value="<%=ddb.getITEM()%>" READONLY>
  	  </div>
      </div>
      
       <div class="form-group">
      <label class="control-label col-sm-4" for="Product Desc">Product Desc:</label>
      <div class="col-sm-6">
      <INPUT type = "TEXT" size="50"  class="form-control"  MAXLENGTH=50 name="ITEMDESC" value="<%=itemdesc%>" READONLY>
  	  </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Picked Quantity">Picked Quantity:</label>
      <div class="col-sm-6">
      <INPUT  type = "TEXT" size="15" class="form-control" MAXLENGTH=20 name="PICKEDQTY" value="<%=su.formatNum(String.valueOf(qtyPick))%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Received Quantity">Received Quantity:</label>
      <div class="col-sm-6">
      <INPUT  type = "TEXT" size="15" class="form-control" MAXLENGTH=20 name="RECVQTY" value="<%=su.formatNum(String.valueOf(qtyRc))%>" READONLY>
      </div>
      </div>
            
      <div class="form-group">
      <label class="control-label col-sm-4" for="Quantity">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Quantity:</label>
      <div class="col-sm-6">  
      <div class="input-group">        
      <INPUT class="form-control"  type = "TEXT" size="30"  MAXLENGTH=20 name="qtyor" value="<%=qtyor%>" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateForm();}">
      <span class="input-group-btn"></span>
      
       <INPUT class="form-control" type = "TEXT" size="5"  MAXLENGTH=20 name="UOM" value="<%=uom%>" READONLY>
       <%-- <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOM" style="width: 150%">
					<%
				  ArrayList ccList = UomDAO.getUOMList(plant);
					for(int i=0 ; i < ccList.size();i++)
		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
				        <option value=<%=uom%>><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT> --%>
      </div>
      </div>
      </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Rental Price">Rental Price:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="RENTALPRICE" value="<%=RENTALPRICE%>" class="form-control">
      </div>
      </div>
         
      <div class="form-group">
      <div class="col-sm-10">
      <table id="MULTI_IB_REMARKS">
       <% 
                      ArrayList al= _loanUtil.listLoandetMultiRemarks(plant,dono,dolnno);
                      String lblRemarks="";
                     // String PRDREMARKS="";
                     if(al.size()> 0)
                       {
	                     for(int i=0 ; i<al.size();i++)
	                      {
	                    	  Map m=(Map)al.get(i);
	                    	  lblRemarks="Remarks";
	                    	  PRDREMARKS = (String)m.get("remarks");
	                    %>
	                    <TR>
	                       	<%if(i==0){ %>
	                        		<TH WIDTH="19%" ALIGN="RIGHT" Style="font-size: 15px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=lblRemarks%>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	                        	<%}else{ %>
	                        		<TH>
	                       		 <%}%> 
	                       		 
	                      	 <TH  WIDTH="74%"><INPUT class="form-control" type = "TEXT" size="100"  MAXLENGTH="100" name="PRDREMARKS_<%=i%>" id="PRDREMARKS_<%=i%>" value="<%=PRDREMARKS%>" ></TH>
	                     </TR>
                     <%   } 
	                   }else {lblRemarks="Remarks1"; %>
	                     <TR>
	                        <TH WIDTH="19%" ALIGN="RIGHT" Style="font-size: 15px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=lblRemarks%>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	                      	 <TH  WIDTH="74%"><INPUT class="form-control"  type = "TEXT" size="100"  MAXLENGTH="100" name="PRDREMARKS_0" id="PRDREMARKS_0" value="<%=PRDREMARKS%>" ></TH>
	                     </TR>
                       <% } %>
                       
                   
               </TABLE>
                         
	 <INPUT type="hidden"	name="DYNAMIC_REMARKS_SIZE">
      </div>
      </div>
     </div>
      <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
      <INPUT type="Hidden" name="CUSTNO" value="<%=dhb.getCustNo()%>">
      <INPUT type="Hidden" name="RFLAG" value="<%=rflag%>">
      
      <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <!-- <button type="button" class="Submit btn btn-default" onClick="javascript: history.go(-1)"><b>Back</b></button>&nbsp;&nbsp; -->
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='/track/loanorderservlet?DONO=<%=dono%>&RFLAG=<%=rflag%>&Submit=View';"><b>Back</b></button>&nbsp;&nbsp;
      
      <%if(pickstatus.equalsIgnoreCase("N")){%>
      <button type="button" class="Submit btn btn-default" name="Submit" onclick="return onDelete(document.form)"><b>Delete</b></button>&nbsp;&nbsp;
       <%} %>
        <button type="button" class="Submit btn btn-default" onClick="return onUpdate()"><b>Update</b></button>&nbsp;&nbsp;
      
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