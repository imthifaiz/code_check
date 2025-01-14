<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ include file="header.jsp"%>

<!-- <html> -->

<%--New page design begin --%>
<%
String title = "Goods Receipt By Serial";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PURCHASE_TRANSACTION%>"/>
</jsp:include>
<style type="text/css">
.backpageul
{
	background-color: rgb(255, 255, 255);
    padding: 0px 10px;
    margin-bottom: 0px;
    margin-top: 15px;
}
.underline-on-hover:hover {
  		text-decoration: underline;
	}
	</style>
<%--New page design end --%>

<script src="../jsp/js/calendar.js"></script>
<!-- <script src="js/jquery-1.4.2.js"></script> -->
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/general.js"></script>

<!-- <title>Misc Order Receipt (By Range)</title>
<link rel="stylesheet" href="css/style.css"> -->
<script>

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'InboundOrderList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
  
  
  
function validatePO(form) {
   var frmRoot=document.form;
   var locId = document.form.LOC.value;
  
   
   
     if(locId=="" || locId.length==0 ) {
        alert("Enter Location!");
        document.getElementById("LOC").focus();
        return false
       }
     if(frmRoot.RECVTRANID.value=="" || frmRoot.RECVTRANID.value.length==0 ) {
    	 alert ("Please Enter GRNO");
         document.getElementById("GRNO").focus();
         return false
        }
     if(frmRoot.TRANSACTIONDATE.value=="" || frmRoot.TRANSACTIONDATE.value.length==0 ) {
    	 alert("Please select Transaction Date");
         document.getElementById("TRANSACTIONDATE").focus();
         return false
        }
       
       if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 ) {
            alert("Please Enter Product ID!");
            frmRoot.ITEMNO.focus();
            return false;
     }
       if(document.form.UOM.value == ""){
   	    alert("Please enter a UOM value");
   	    document.form.UOM.focus();
   	    document.form.UOM.select();
   	    return false;
   }
        var qty = document.form.QTY.value;
              
                var sRange = document.form.SRANGE.value;
                var eRange = document.form.ERANGE.value;
               
                  
                 if (sRange == "" || sRange.length == 0) {
			alert("Enter Start Range!");
			document.form.SRANGE.focus();
                        return false;
		} 
                
                 if (eRange == "" || eRange.length == 0) {
			alert("Enter End Range!");
			document.form.ERANGE.focus();
                        return false;
		} 
                 if (isNumericInput(sRange) == false) {
                            alert("Entered Start Range is not a valid Number !");
                            document.form.SRANGE.value = "";
                            document.form.SRANGE.focus();
                            return false;
		} 
                
                if (isNumericInput(eRange) == false) {
                            alert("Entered End Range is not a valid Number !");
                            document.form.ERANGE.value = "";
                            document.form.ERANGE.focus();
                            return false;
		} 
        
           /*      if(parseInt(sRange)>parseInt(eRange)){
                            alert("Entered invalid Range,Start Range is Greater than End Range !");
                             document.form.SRANGE.value = "";
                             document.form.SRANGE.focus();
                            return false;
		} */
                
                 var rangeSize = parseInt(eRange);
              
                
		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.form.QTY.focus();
                        return false;
		} 
                
                if (isNumericInput(qty) == false) {
                            alert("Entered Quantity is not a valid Qty!");
                            document.form.QTY.value = "";
                            document.form.QTY.focus();
                            return false;
		} 
              
                
                if(frmRoot.REASONCODE.value=="" || frmRoot.REASONCODE.value.length==0 )
	        {
		alert("Enter REASONCODE!");
		frmRoot.REASONCODE.focus();
		return false;
                 }
  


		

   if(rangeSize>0){
	 
           document.form.action.value="MiscOrderReceivingByRange";
	   document.form.submit();
	   return true;
         
	
	
   }
 
}


function onClear(){
 
  document.form.ITEMNO.value="";
  document.form.ITEMDESC.value="";
  document.form.LOC.value="";
  document.form.SUFFIX.value="";
  document.form.DTFRMT.value="";
  document.form.SRANGE.value="";
  document.form.ERANGE.value="";
  document.form.QTY.value="";
  document.form.REASONCODE.value="";
  document.form.REFDET.value="";
  document.form.EXPIREDATE.value="";
  return true;
}
  
</script>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="db" class="com.track.gates.defaultsBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
       
       StrUtils strUtils=new StrUtils();
       
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
       String SETCURRENTDATE_GOODSRECEIPT = new PlantMstDAO().getSETCURRENTDATE_GOODSRECEIPT(plant);//Thanzith
       
       String userId = (String) session.getAttribute("LOGIN_USER");
       com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
       String grno = _TblControlDAO.getNextOrder(plant,userId,"GRN");
       
      
       String PLANT= (String) session.getAttribute("PLANT");
       String  fieldDesc="", fieldDescError="";
       String ITEMNO   = "", ITEMDESC  = "",
       LOC   = "" , BATCH  = "", REF   = "",SUFFIX="",DTFRMT="",SRANGE="",ERANGE="",
       QTY = "",REFDET="",REASONCODE="",EXPIREDATE="",UOM="",TRANSACTIONDATE="";
       ITEMNO = StrUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC =strUtils.replaceCharacters2Recv( StrUtils.fString(request.getParameter("ITEMDESC")));
       LOC = StrUtils.fString(request.getParameter("LOC"));
        SUFFIX = StrUtils.fString(request.getParameter("SUFFIX"));
        DTFRMT= StrUtils.fString(request.getParameter("DTFRMT"));
        SRANGE = StrUtils.fString(request.getParameter("SRANGE"));
        ERANGE = StrUtils.fString(request.getParameter("ERANGE"));
       String uom = su.fString(request.getParameter("UOM"));
       QTY = StrUtils.fString(request.getParameter("QTY"));
       REFDET = StrUtils.fString(request.getParameter("REFDET"));
       REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
       EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
       
       if(SRANGE.length()==0){
           TblControlUtil tblContrlUtil = new TblControlUtil();
           tblContrlUtil.setmLogger(mLogger);
           SRANGE = tblContrlUtil.getNextSeqNo4RecvByRange(PLANT,sUserId);
           QTY="1";
       }
 	   TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
       DateUtils _dateUtils = new DateUtils();
       String curDate =_dateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
       if(action.equalsIgnoreCase("CLEAR"))
       {
        ITEMNO = "";
        ITEMDESC = "";
        LOC = "";
        SUFFIX="";
        DTFRMT="";
        SRANGE = "";
        ERANGE="";
        REF = "";
        REFDET  = "";
        REASONCODE = "";
        EXPIREDATE ="";  
      }
      
      else if(action.equalsIgnoreCase("result"))
      {
        fieldDesc=(String)request.getSession().getAttribute("RESULT");
        
      }
      else if(action.equalsIgnoreCase("resulterror"))
      {
 
    	  fieldDescError=(String)request.getSession().getAttribute("RESULTERROR");
      }
      else if(action.equalsIgnoreCase("batchresult"))
      {
 
       //fieldDesc=(String)request.getSession().getAttribute("BATCHRESULT");
      }
      else if(action.equalsIgnoreCase("batcherror"))
      {
 
    	  fieldDescError=(String)request.getSession().getAttribute("BATCHERROR");
      }
       else if(action.equalsIgnoreCase("catchbatcherror"))
      {
 
       fieldDescError=(String)request.getSession().getAttribute("CATCHBATCHERROR");
      }
   


%>
<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../purchaseTransactionDashboard"><span class="underline-on-hover">Purchase Transaction Dashboard</span> </a></li>
                <li>Goods Receipt By Serial</li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../purchaseTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

 <%-- <h2>
      <small><%=fieldDesc%></small></h2> --%>
<%-- <h2>
      <small class="mainred"><%=fieldDescError%></small></h2>   --%>  

<center><h2><small><font class='maingreen'><%=fieldDesc%></font></small></h2></center>

<center><h2><small><font class='mainred'><%=fieldDescError%></font></small></h2></center>
  

  <form class="form-horizontal" name="form" method="post" action="/track/MiscOrderReceivingServlet?action=MiscOrderReceivingByRange">
      
      <div class="form-group">
      <label class="control-label col-sm-2" for="Transaction ID">GRNO:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    	<INPUT class="form-control" name="RECVTRANID" type="TEXT" id="RECVTRANID" value="<%= grno %>" size="30"  MAXLENGTH=50>
         <span class="input-group-addon"  onClick="javascript:onNew();return false;">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  	 <div class="form-inline">
      <label class="control-label col-sm-2" for="Location">Location:</label>
      <div class="col-sm-3">          
      <div class="input-group">
      <INPUT class="form-control" name="LOC" type="TEXT" value="<%=LOC%>" size="30" id ="LOC"	MAXLENGTH=80
	  onkeypress="if((event.keyCode=='13') && ( document.form.LOC.value.length > 0)) {validateLocation();}">
	  <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('../jsp/loc_list_receivewms.jsp?LOC='+form.LOC.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px"></i></a></span>
      </div>
      </div>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-2" for="Transaction Date">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Transaction Date:</label>
<!--         <label class="control-label col-sm-2 required" for="Transaction Date">Transaction Date:</label> -->
        <div class="col-sm-3">
        <div class="input-group">         
        <%if(SETCURRENTDATE_GOODSRECEIPT.equals("1")){%> 
      <INPUT class="form-control datepicker" name="TRANSACTIONDATE" type="TEXT" id ="TRANSACTIONDATE" value="<%=TRANSACTIONDATE%>" readonly="readonly" size="30" MAXLENGTH="80">
       <%}else {%>
       <INPUT class="form-control datepicker" name="TRANSACTIONDATE" type="TEXT" id ="TRANSACTIONDATE" value="" readonly="readonly" size="30" MAXLENGTH="80">
       <%}%>
        </div>
    	</div>
    	<div class="form-inlne">
      <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
      <div class="col-sm-3">          
      <div class="input-group">
      <INPUT class="form-control" name="ITEMNO" type="TEXT" value="<%=ITEMNO%>" id ="ITEMNO"
	  size="30" MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.ITEMNO.value.length > 0)){validateProduct();}">
	  <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('../jsp/miscreceiving_item_list.jsp?MISCTYPE=BYRANGE&ITEMNO='+form.ITEMNO.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px"></i></a></span>
      </div>
      </div>
      </div>
      </div>
      
      
      <div class="form-group">
 <label class="control-label col-sm-2" for="Expiry Date">Expiry Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
       <INPUT class="form-control datepicker" name="EXPIREDATE" type="TEXT" id ="EXPIREDATE" value="<%=EXPIREDATE%>" readonly="readonly"
			size="30" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.REASONCODE.focus();}">
        </div>
    	</div>
      <div class="form-inlne">
             <label class="control-label col-sm-2" for="Description">Description:</label>
      <div class="col-sm-3">          
      <div class="input-group">
      <INPUT class="form-control" name="ITEMDESC"  type="TEXT" id ="ITEMDESC" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="30" MAXLENGTH=100 >
	  <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('../jsp/miscreceiving_item_list.jsp?MISCTYPE=BYRANGE&DESC='+form.ITEMDESC.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px"></i></a></span>
      </div>
      </div>
    	</div>
      </div>
      
      <div class="form-group">
     <label class="control-label col-sm-2" for="Reason Code">Reason Code:</label>
      <div class="col-sm-3">          
      <div class="input-group">
      <INPUT class="form-control" name="REASONCODE" type="TEXT" id ="REASONCODE" value="<% if(REASONCODE.length() > 0) { out.print(REASONCODE); }else{out.print("NOREASONCODE");}%>"
	   size="30" readonly MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.REASONCODE.value.length > 0)){ document.form.SubmitButton.focus();}">
	  <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/miscreasoncode.jsp?ITEMNO='+form.ITEMNO.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Reason Code Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px"></i></a></span>
      </div>
      </div>
      <div class="form-inline">
       <label class="control-label col-sm-2" for="Suffix">Suffix:</label>
      <div class="col-sm-3">          
      <INPUT class="form-control" name="SUFFIX" id="SUFFIX"	value="<%=SUFFIX%>" type="TEXT" size="30" MAXLENGTH="5">
      </div>
      </div>
      </div>
      
      <div class="form-group">
 <label class="control-label col-sm-2" for="Dt Frmt">Dt Frmt:</label>
      <div class="col-sm-3">          
      <INPUT class="form-control" name="DTFRMT" id="DTFRMT"	value="<%=DTFRMT%>" type="TEXT" style="width: 100%" MAXLENGTH="6">
      </div>
      <div class="form-inline">
       <label class="control-label col-sm-2" for="Rnage">Range-Start:</label>
      <div class="col-sm-3">          
      <INPUT class="form-control" name="SRANGE" id="SRANGE" type="TEXT" value="<%=SRANGE%>" size="30" MAXLENGTH=18 onkeypress="if(event.keyCode=='13'){validateSRange();}" >
      </div>
      </div>
      </div>
      
       <div class="form-group">
      <label class="control-label col-sm-2" for="range end">No of Batch:</label>
      <div class="col-sm-3">          
      <INPUT class="form-control" name="ERANGE" id="ERANGE" type="TEXT"  value="<%=ERANGE%>" style="width: 100%" MAXLENGTH=18 onkeypress="if(event.keyCode=='13'){validateERange();}">
      </div>
      <div class="form-inline">
          <label class="control-label col-sm-2" for="Quantity">Quantity:</label>
      <div class="col-sm-3">          
      <div class="input-group">
      <INPUT class="form-control" name="QTY" type="TEXT" id ="QTY" value="<%=QTY%>" size="30" MAXLENGTH=50
	  onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length > 0)){validateQuantity();}">
	  <span class="input-group-btn"></span>
	  <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOM" style="width: 150%" onchange="CheckPriceVal(this.value)">
			
					<%
				  ArrayList ccList = UomDAO.getUOMList(PLANT);
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
      </div>
      <div class="form-group">
      <label class="control-label col-sm-2" for="Remarks">Remarks:</label>
      <div class="col-sm-3">          
      <INPUT class="form-control" name="REFDET" type="TEXT" value="<%=REFDET%>" style="width: 100%" MAXLENGTH=80 onkeypress="if(event.keyCode=='13') {document.form.EXPIREDATE.focus();}">
	  </div>
      </div>
      
      <div class="form-group">
      <div class="col-sm-12" align="center">
      <button type="button" class="Submit btn btn-default" onClick="validateInputs()"><b>Receive</b></button>&nbsp;&nbsp;
      </div>
      </div>
 	  
 	  <table border ="0" cellspacing="0"  align = "center"  id="MIS_RECEIVING_BY_RANGE" class="table">
	  <thead style="background: #eaeafa; font-size: 15px;">
                    <TR>
                    <TH>SERIAL BATCH NUMBER</TH>
                    <TH>  QUANTITY  </TH>
                    </TR>
    </thead>
	</table>
      <div class="form-group">        
      <div class="col-sm-12" align="center">
      <!--       <button type="button" class="Submit btn btn-default" onClick="window.location.href='home.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='inboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      <button type="button" class="Submit btn btn-default" onClick="validatePO(document.form)"><b>Confirm</b></button>&nbsp;&nbsp;
      <button type="button" class="Submit btn btn-default" onClick="return onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      </div>
      </div>
      
      <INPUT name="BATCH" type="hidden">
      <INPUT name="LOGINUSER" type="hidden" value="<%=sUserId %>" size="1"  MAXLENGTH=80> 
	  <INPUT name="action" type="hidden">
      
      </form>
      </div>
      </div>
      </div>
      
      


<!-- </HTML> -->
<script>

function addRow() {

	var table = document.getElementById("MIS_RECEIVING_BY_RANGE");
        var stratRangeValue = document.getElementById("SRANGE").value;
        var endRangeValue = document.getElementById("ERANGE").value;
        var sfix = document.getElementById("SUFFIX").value;
        var dtFmt = document.getElementById("DTFRMT").value;
                         var rangeCnt = parseInt(endRangeValue);;
		       
		         var rowCount = table.rows.length;	
                      
                        for(var i = rowCount; i > 1;i--)
                        {
                          table.deleteRow(i -1);
                        }
                        
                      
                        rowCount = 1

			for(var index = 1; index<=rangeCnt; index++) {
			var rowColor = ((index == 0) || (index % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF"; 
                    
			var row = table.insertRow(index);
                        row.bgColor = rowColor;
			var cell2 = row.insertCell(0);
			cell2.align="left";
                        var batch = parseInt(stratRangeValue)+index -1;
			cell2.innerHTML =sfix+dtFmt+batch;

			var cell3 = row.insertCell(1);
			cell3.innerHTML = document.getElementById("QTY").value;
			cell3.align="left";
                        }

	}
        
function validateLocation() {
	var locId = document.form.LOC.value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.form.LOC.focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
				PLANT : "<%=PLANT%>",
					ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						document.form.ITEMNO.value = "";
						document.form.ITEMNO.focus();
					} else {
						alert("Not a valid Location");
						document.form.LOC.value = "";
						document.form.LOC.focus();
					}
				}
			});
		}
}

function validateProduct() {
	var productId = document.form.ITEMNO.value;
	if(document.form.ITEMNO.value=="" || document.form.ITEMNO.value.length==0 ) {
		alert("Enter Product ID!");
		document.form.ITEMNO.focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=PLANT%>",
				ACTION : "VALIDATE_PRODUCT"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						document.form.ITEMDESC.value = resultVal.discription;
						document.form.UOM.value = resultVal.uom;
						/*if(resultVal.isNonStk=="Y"){
							alert("Not a valid product or It is a Non Stock product");
							document.form.ITEMNO.value = "";
							document.form.ITEMNO.focus();
						}else{*/
						//document.form.BATCH.value = "NOBATCH";
						document.form.SUFFIX.focus();
					
					} else {
						alert("Not a valid product or It is a parent product");
						document.form.ITEMNO.value = "";
						document.form.ITEMNO.focus();
					}
				}
			});
		}
	}

	function validateSRange() {
		var sRange = document.form.SRANGE.value;
		if (sRange == "" || sRange.length == 0) {
			alert("Enter Start Range");
                        document.form.SRANGE.value = "";
                        document.form.SRANGE.focus();
		}
		document.form.ERANGE.value = "";
		document.form.ERANGE.focus();
	}
        function validateERange() {
		var eRange = document.form.ERANGE.value;
		if (eRange == "" || eRange.length == 0) {
			alert("Enter End Range");
                        document.form.ERANGE.value = "";
                        document.form.ERANGE.focus();
		}
		
		document.form.QTY.focus();
	}
	function validateQuantity() {
		var qty = document.form.QTY.value;
		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.form.QTY.focus();
		} else {
			if (isNumericInput(qty) == false) {
				alert("Entered Quantity is not a valid Qty!");
			} else {
				document.form.REFDET.value = "";
				document.form.REFDET.focus();
			}

		}
	}
	
            function validateInputs() {
                var recflag =false;
                var sRange = document.form.SRANGE.value;
                var eRange = document.form.ERANGE.value;
                var rangeSize = eRange-sRange;
                rangeSize= rangeSize+1;
                var locId = document.form.LOC.value;
                var productId = document.form.ITEMNO.value;
                var qty = document.form.QTY.value;
                
                   if(locId=="" || locId.length==0 ) {
                    alert("Enter Location!");
                    document.form.LOC.focus();
                    return false;
                   }else{
                    var urlStr = "/track/MiscOrderHandlingServlet";
                    $.ajax( {
                            type : "POST",
                            url : urlStr,
                            data : {
                                    LOC : locId,
                                    PLANT : "<%=PLANT%>",
                                            ACTION : "VALIDATE_LOCATION"
                                    },
                                    dataType : "json",
                                    success : function(data) {
                                            if (data.status != "100") {
                                                    alert("Not a valid Location");
                                                    document.form.LOC.value = "";
                                                    document.form.LOC.focus();
                                                    return false;
                                            }else{
                                              recflag=true;
                                            }
                                    }
                            });
                    }
                    
                    
                 
	if(document.form.ITEMNO.value=="" || document.form.ITEMNO.value.length==0 ) {
		alert("Enter Product ID!");
		document.form.ITEMNO.focus();
                return false;
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=PLANT%>",
				ACTION : "VALIDATE_PRODUCT"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status != "100") {
						alert("Not a valid product");
						document.form.ITEMNO.value = "";
						document.form.ITEMNO.focus();
                                                return false;
					}else{
                                               recflag=true;
                                             }
				}
			});
		}
	

                 if(recflag=true){
                 if (sRange == "" || sRange.length == 0) {
			alert("Enter Start Range!");
			document.form.SRANGE.focus();
                        return false;
		} 
                
                 if (eRange == "" || eRange.length == 0) {
			alert("Enter End Range!");
			document.form.ERANGE.focus();
                        return false;
		} 
                 if (isNumericInput(sRange) == false) {
                            alert("Entered Start Range is not a valid Number !");
                            document.form.SRANGE.value = "";
                            document.form.SRANGE.focus();
                            return false;
		} 
                
                if (isNumericInput(eRange) == false) {
                            alert("Entered End Range is not a valid Number !");
                            document.form.ERANGE.value = "";
                            document.form.ERANGE.focus();
                            return false;
		} 
        
             /*    if(parseInt(sRange)>parseInt(eRange)){
                            alert("Entered invalid Range,Start Range is Greater than End Range !");
                             document.form.SRANGE.value = "";
                             document.form.SRANGE.focus();
                            return false;
		} 
                */

		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.form.QTY.focus();
                        return false;
		} 
                
                if (isNumericInput(qty) == false) {
                            alert("Entered Quantity is not a valid Qty!");
                            document.form.QTY.value = "";
                            document.form.QTY.focus();
                            return false;
		} else{
                       addRow();
                }
               
          }
         
        
               
  }
 function onNew()
            {
            	
            	var urlStr = "/track/InboundOrderHandlerServlet";
            	$.ajax( {
            		type : "POST",
            		url : urlStr,
            		data : {
            		PLANT : "<%=PLANT%>",
            		ACTION : "GRN"
            		},
            		dataType : "json",
            		success : function(data) {
            			if (data.status == "100") {
            				var resultVal = data.result;				
            				var resultV = resultVal.grno;
            				document.form.RECVTRANID.value= resultV;
            	
            			} else {
            				alert("Unable to genarate GRN NO");
            				document.form.RECVTRANID.value = "";
            			}
            		}
            	});	
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
	//document.form.LOC.value = "";
	document.form.LOC.focus();
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
