
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%
String title = "Consignment Order-Add Product";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>



<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

</script>
<script language="javascript">

function validateForm(){
 
  if(document.form.ITEM.value == "")
  {
    alert("Please select an item");
    document.form.ITEM.focus();
    return false;
  }
 else if(document.form.QTY.value == ""){
    alert("Please enter Quantity");
    document.form.QTY.focus();
    return false;
  }
  if(!IsNumeric(form.QTY.value))
   {
     alert(" Please Enter valid  Qty !");
     form.QTY.focus();  form.QTY.select(); return false;
   }
   
  else if(document.form.QTY.value <= 0){
	    alert("Please enter > 0 quantity");
	    document.form.QTY.focus();
	    return false;
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
	  if(document.form.UOM.value == ""){
	    alert("Please enter a UMO value");
	    document.form.UOM.focus();
	    document.form.UOM.select();
	    return false;
}
  else{

  // document.form.Submit.value="Add Product";
	  document.form.submit();
	    return true;
 
   }
}

</script>

<script language="javascript">

function ref()
{

  document.form.ITEM.focus();
  document.form.ITEM.value="";
}

</script>



<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="tpdb"  class="com.track.tables.TODET" />
<jsp:useBean id="tphb"  class="com.track.tables.TOHDR" />
<jsp:useBean id="imb"  class="com.track.tables.ITEMMST" />
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />

<%
	session= request.getSession();
	String plant=su.fString((String)session.getAttribute("PLANT"));
    String action  = su.fString(request.getParameter("Submit")).trim();
    String tono    = su.fString(request.getParameter("TONO"));
    String jobNum  = su.fString(request.getParameter("JOB_NUM"));
    String custName  = su.replaceCharacters2Recv(su.fString(request.getParameter("CUST_NAME")));
    String custCode  = su.fString(request.getParameter("CUST_CODE"));
    String deldate = su.fString(request.getParameter("DELDATE"));
    String UNITPRICE=su.fString(request.getParameter("UNITPRICE"));
    String uom =su.fString(request.getParameter("UOM"));
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    String RFLAG=     su.fString(request.getParameter("RFLAG"));
  
    if (action.equalsIgnoreCase("Go"))
    {
      response.sendRedirect("createTO.jsp?TONO="+tono);
    }
    if (action.equalsIgnoreCase("Delete"))
    {
      tono    = request.getParameter("TONO");
      n = tpdb.deleteTODET(tono);
      if(n==1) result = "<font color=\"green\">Consignment Order Deleted Successfully</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='../home'\">";
      else     result = "<font color=\"red\"> Error in Delete Consignment Order  -  <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";

      result = "Consignment Order<br><h3>"+result;
      session.setAttribute("RESULT",result);
      response.sendRedirect("displayResult2User.jsp");
    }

%>


<body onload="ref()">
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

<form class="form-horizontal" name="form" method="post" action="/track/TransferOrderServlet?Submit=Add Product" onSubmit="return validateForm()">
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Transfer Order No">Order Number:</label>
      <div class="col-sm-4">
      <input name="TONO" type="TEXT" value="<%=tono%>" size="20" MAXLENGTH=100 class="form-control" readonly>
	  </div>
      </div>
      
	  <div class="form-group">
      <label class="control-label col-sm-4" for="Assignee Name">Transfer Order Customer Name:</label>
      <div class="col-sm-4">          
      <INPUT  class="form-control" name="CUST_NAME" type="TEXT" value="<%=su.forHTMLTag(custName)%>" size="50" MAXLENGTH=100 readonly>
      <INPUT type = "hidden"  class="inactive" size="20"  MAXLENGTH=20 name="CUST_CODE"  value="<%=custCode%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Order Line No">Order Line No:</label>
      <div class="col-sm-4">          
      <INPUT  class="form-control" name="TOLNNO" type="TEXT" value="<%=tpdb.getNextLineNo(tono,plant)%>" size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Product Class ID">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Product ID:</label>
      <div class="col-sm-4">
      <div class="input-group">    
      <input name="ITEM" type="TEXT" onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)) {validateProduct();}" 
      size="50" MAXLENGTH=100 class="form-control" id="item">
      <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('item_list_order.jsp?ITEM='+form.ITEM.value);"> 	
      <a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
  	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
  	  </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Discription">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Description:</label>
      <div class="col-sm-4">
      <div class="input-group">    
      <input name="DESC" type="TEXT" size="50" MAXLENGTH=100 class="form-control">
      <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('item_list_order.jsp?ITEM='+form.ITEM.value+'&DESC='+form.DESC.value);"> 	
      <a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
  	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
  	  </div>
      </div>
      
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK1">
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK2">
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK3">
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="ITEM_CONDITION">
	
	  <div class="form-group">
      <label class="control-label col-sm-4" for="Quantity">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Quantity:</label>
      <div class="col-sm-4">          
      <INPUT  class="form-control" name="QTY" type="TEXT" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateForm();}"
      size="50" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-1" for="UOM">UOM:</label>
      <div class="col-sm-2">          
       <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOM" style="width: 80%" id="UOM">
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
	 </SELECT>
      </div>
      </div>
       </div>
            
      <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
      <INPUT  class="form-control" name="PRDREMARKS" type="TEXT" size="50" MAXLENGTH=100>
      </div>
      </div>
      
      <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
      <INPUT type = "Hidden"  name="VENDNAME"  value="<%=su.forHTMLTag(custName)%>">
      <INPUT type = "Hidden"  name="LOGIN_USER"  value="<%=enrolledBy%>">
      <INPUT type="Hidden" name="RFLAG" value="<%=RFLAG%>">
      <INPUT type = "Hidden"  name="UNITPRICE"  value="<%=UNITPRICE%>">
      
      <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default" 
      onClick="window.location.href='/track/TransferOrderServlet?TONO=<%=tono%>&RFLAG=<%=RFLAG%>&Submit=View';">
      <b>Back</b></button>&nbsp;&nbsp;
      
      <button type="button" class="Submit btn btn-default" name="Submit" onclick="return validateForm();">
      <b>Add Product & Save</b></button>&nbsp;&nbsp;
      
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
      </body>
	  
	  <Script>
function validateProduct() {
	var productId = document.form.ITEM.value;
        var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=plant%>",
                           
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						GetUOM();
						document.form.ITEM.value = resultVal.sItem;
						document.form.DESC.value = resultVal.sItemDesc;
                                                 document.form.UOM.value = resultVal.ServiceUOM;
						 document.form.QTY.focus();
					} 
				}
			});
		
	}
function GetUOM()
{
	  var itemValue = document.getElementById("item").value;
	 
	
		  var urlStr = "/track/ItemMstServlet";				  
		  
	  
		  $.ajax({type: "POST",url: urlStr, data: { ITEM: itemValue ,ITEM_DESC:"", ACTION: "PRODUCT_UOM",PLANT:"<%=plant%>"},dataType: "json", success: onGetUOM });
		  

}
function onGetUOM(data){
	
	  var errorBoo = false;
		$.each(data.errors, function(i,error){					
			if(error.ERROR_CODE=="99"){
				errorBoo = true;
				document.getElementById("UOM").value=0;
			
			}
		});
		
		if(errorBoo == false){
			if(data.status=="99"){
				alert("Invalid Scan Product!");
				  document.getElementById("item").focus();	
				  return false;
			}
			else{		 
				var salesuom= data.result.SALESUOM;
      	document.getElementById("UOM").value =salesuom;
      }
		}
	  
} 
        </Script>
        <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="nobackblock" value="1" />
</jsp:include>