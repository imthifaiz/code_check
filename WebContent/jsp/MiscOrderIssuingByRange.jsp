<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>

<%--New page design begin --%>
<%
String title = "Goods Issue By Serial";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
    <jsp:param name="submenu" value="<%=IConstants.SALES_TRANSACTION%>"/>
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
	}</style>
<%--New page design end --%>


<!-- <html>
<script src="js/jquery-1.4.2.js"></script> -->
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/general.js"></script>
<!-- 
<title>Misc Order Issue(By Range)</title>
<link rel="stylesheet" href="css/style.css"> -->
<SCRIPT>

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
       if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 ) {
            alert("Please Enter Product ID!");
            frmRoot.ITEMNO.focus();
            return false;
     }
       if(frmRoot.TRANSACTIONDATE.value=="" || frmRoot.TRANSACTIONDATE.value.length==0 ) {
    	   alert("Please select Transaction Date");	
            frmRoot.TRANSACTIONDATE.focus();
            return false;
     }

       
   
                var qty = document.form.QTY.value;
                var sRange = document.form.SRANGE.value;
                var eRange = document.form.ERANGE.value;
                var rangeSize = eRange-sRange;
                rangeSize= rangeSize+1;
                  
               
		
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
        
                 if(parseInt(sRange)>parseInt(eRange)){
                        alert("Entered invalid Range,Start Range is Greater than End Range !");
                        document.form.SRANGE.value = "";
                        document.form.SRANGE.focus();
                        return false;
		} 
/*                  if(document.form.UOM.value == ""){
                	    alert("Please enter a UMO value");
                	    document.form.UOM.focus();
                	    document.form.UOM.select();
                	    return false;
                } */
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
               document.form.action.value="MiscOrderIssueByRange";
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
    document.form.INVQTY.value="";
    document.form.QTY.value="";
    document.form.REFDET.value="";
    document.form.REASONCODE.value="";
    document.form.REFDET.value="";
    
     return true;
}
  
</script>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="db" class="com.track.gates.defaultsBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<%
       

     
       StrUtils strUtils=new StrUtils();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String userId = (String) session.getAttribute("LOGIN_USER");
       String PLANT= (String) session.getAttribute("PLANT");
       String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
       
       com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
       String gino = _TblControlDAO.getNextOrder(plant,userId,"GINO");
       
       String  fieldDesc="";
       String ITEMNO   = "", ITEMDESC  = "",
       LOC   = "" , SUFFIX  = "",DTFRMT="",SRANGE="",ERANGE="", REF   = "",
       QTY = "",REFDET="",REASONCODE="",INVQTY="",TRANSACTIONDATE="";
       String uom = su.fString(request.getParameter("UOM"));
     
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.fString(request.getParameter("ITEMDESC"));
       LOC = strUtils.fString(request.getParameter("LOC"));
       SUFFIX = strUtils.fString(request.getParameter("SUFFIX"));
       DTFRMT = strUtils.fString(request.getParameter("DTFRMT"));
       SRANGE = strUtils.fString(request.getParameter("SRANGE"));
       ERANGE = strUtils.fString(request.getParameter("ERANGE"));
       INVQTY = strUtils.fString(request.getParameter("INVQTY"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       REFDET = strUtils.fString(request.getParameter("REFDET"));
       REASONCODE = strUtils.fString(request.getParameter("REASONCODE"));
	   TRANSACTIONDATE = strUtils.fString(request.getParameter("TRANSACTIONDATE"));
	   String SETCURRENTDATE_PICKANDISSUE = new PlantMstDAO().getSETCURRENTDATE_PICKANDISSUE(plant);//Thanzith
       DateUtils _dateUtils = new DateUtils();
       String curDate =_dateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
     	QTY = "1";
     if(action.equalsIgnoreCase("CLEAR"))
      {
       
        ITEMNO   = "";
        ITEMDESC = "";
        LOC      = "";
        SUFFIX   = "";
        DTFRMT   = "";
        SRANGE   = "";
        ERANGE   = "";
        REF      = "";
        REFDET   = "";
        REASONCODE = "";
        QTY        ="";
        INVQTY     ="";
           
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
      else if(action.equalsIgnoreCase("catcherror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
   %>

<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 
<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>                
                <li><a href="../salesTransactionDashboard"><span class="underline-on-hover">Sales Transaction Dashboard</span> </a></li>                
                <li>Goods Issue By Serial</li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../salesTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>



<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form" method="post" action="/track/MiscOrderIssuingServlet?action=MiscOrderIssueByRange">

 <center>
  <h2><small><%=fieldDesc%></small></h2>
   </center>  
      
      
      <div class="form-group">
          	<label class="control-label col-sm-2" for="Transaction ID">GINO:</label>
        <div class="col-sm-3">
                <div class="input-group">
        <INPUT class="form-control" name="TRANID" id="TRANID" type="TEXT" value="<%= gino %>" style="width: 100%" MAXLENGTH=100>
        <span class="input-group-addon"  onClick="javascript:onNew();return false;">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
         <div class="form-inline">
      <label class="control-label col-sm-2" for="Location">Location:</label>
      <div class="col-sm-3">          
      <div class="input-group">
      <INPUT class="form-control" name="LOC" type="TEXT" value="<%=LOC%>" size="30"	MAXLENGTH=80
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
       
<!--                <label class="control-label col-sm-2 required" for="Transaction Date">Transaction Date:</label> -->
        <div class="col-sm-3">
        <div class="input-group">          
        <%if(SETCURRENTDATE_PICKANDISSUE.equals("1")){%>
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" type="TEXT" id ="TRANSACTIONDATE" value="<%=TRANSACTIONDATE%>" readonly="readonly" size="30" MAXLENGTH="80">
        <%}else {%>
       <INPUT class="form-control datepicker" name="TRANSACTIONDATE" type="TEXT" id ="TRANSACTIONDATE" value="" readonly="readonly" size="30" MAXLENGTH="80">
        <%}%>
        </div>
    	</div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
      <div class="col-sm-3">          
      <div class="input-group">
      <INPUT class="form-control" name="ITEMNO" type="TEXT" value="<%=ITEMNO%>" size="30" MAXLENGTH=80
	  onkeypress="if((event.keyCode=='13') && ( document.form.ITEMNO.value.length > 0)){validateProduct();}">
	  <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('../jsp/miscreceiving_item_list.jsp?MISCTYPE=BYRANGE&ITEMNO='+form.ITEMNO.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px"></i></a></span>
      </div>
      </div>
    	</div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-2" for="Remarks">Remarks:</label>
      <div class="col-sm-3">          
      <INPUT class="form-control" name="REFDET" type="TEXT" value="<%=REFDET%>" style="width: 100%" MAXLENGTH=80 
      onkeypress="if(event.keyCode=='13') {document.form.REASONCODE.focus();}">
	  </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Description">Description:</label>
      <div class="col-sm-3">          
      <div class="input-group">
      <INPUT class="form-control" name="ITEMDESC"  type="TEXT" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="30" MAXLENGTH=80 >
	  <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('../jsp/miscissuing_item_list.jsp?MISCISSUETYPE=BYRANGE&LOC='+form.LOC.value+'&DESC='+form.ITEMDESC.value);"> 	
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
      <INPUT class="form-control" name="REASONCODE" type="TEXT" value="<% if(REASONCODE.length() > 0) { out.print(REASONCODE); }else{out.print("NOREASONCODE");}%>"
	  size="30" MAXLENGTH=80 onkeypress="if((event.keyCode=='13') && ( document.form.REASONCODE.value.length > 0)){ document.form.SubmitButton.focus();}">
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
      <INPUT class="form-control" name="SRANGE" id="SRANGE" type="TEXT" value="<%=SRANGE%>" size="30" MAXLENGTH=18 
      onkeypress="if(event.keyCode=='13'){validateSRange();}" >
      </div>
      </div>
      </div>
      
       <div class="form-group">
      <label class="control-label col-sm-2" for="range end">End:</label>
      <div class="col-sm-3">          
      <INPUT class="form-control" name="ERANGE" id="ERANGE" type="TEXT"  value="<%=ERANGE%>" style="width: 100%" MAXLENGTH=18 
      onkeypress="if(event.keyCode=='13'){validateERange();}">
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Quantity">Issue Quantity:</label>
      <div class="col-sm-3">          
      <div class="input-group">
      <INPUT class="form-control" name="QTY" type="TEXT" value="<%=QTY%>" size="30" MAXLENGTH=80 
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
      
      	<INPUT name="INVQTY" type="hidden"  >
		<INPUT name="NONSTKFLG" type="hidden"  >
      
 	  
 	  <div class="form-group">
      <div class="col-sm-offset-5 col-sm-7">
      <button type="button" class="Submit btn btn-default" onClick="validateInputs()"><b>Issue</b></button>&nbsp;&nbsp;
      </div>
      </div>
 	  
 	  <DIV ID='VIEW_RESULT_HERE'></DIV>
 	  	
      <div class="form-group">        
      <div class="col-sm-12" align="center">
<!--       <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      <button type="button" class="Submit btn btn-default" onClick="return validatePO(document.form)"><b>Confirm</b></button>&nbsp;&nbsp;
      <button type="button" class="Submit btn btn-default" onClick="return onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      </div>
      </div>
           
      </form>
      </div>

<script>
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
                                USERID : "<%=sUserId%>",
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
						document.form.ITEMNO.value=resultVal.item;
                        document.form.UOM.value=resultVal.uom;
                        document.form.NONSTKFLG.value=resultVal.isNonStk;
                        document.form.SUFFIX.focus();
					} else {
						alert("Not a valid product");
						document.form.ITEMNO.value = "";
						document.form.ITEMNO.focus();
					}
				}
			});
		}
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
        
           function validateInputs() {
           var recflag =false;
                var productId = document.form.ITEMNO.value;
                var locId = document.form.LOC.value;
                var sRange = document.form.SRANGE.value;
                var eRange = document.form.ERANGE.value;
                var rangeSize = eRange-sRange;
                rangeSize= rangeSize+1;
              
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
                                                       recflag=false;
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
        
                 if(parseInt(sRange)>parseInt(eRange)){
                            alert("Entered invalid Range,Start Range is Greater than End Range !");
                             document.form.SRANGE.value = "";
                             document.form.SRANGE.focus();
                            return false;
		} 
                
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
                           getView();
                }
       }
               
    }
     
           function onNew()
           {
           	
           	var urlStr = "/track/OutboundOrderHandlerServlet";
           	$.ajax( {
           		type : "POST",
           		url : urlStr,
           		data : {
           		PLANT : "<%=PLANT%>",
           		ACTION : "GINO"
           		},
           		dataType : "json",
           		success : function(data) {
           			if (data.status == "100") {
           				var resultVal = data.result;				
           				var resultV = resultVal.invno;
           				document.form.TRANID.value= resultV;
           	
           			} else {
           				alert("Unable to genarate GINO");
           				document.form.TRANID.value = "";
           			}
           		}
           	});	
           	}
    	
function getView(){

               // var QtyValue = document.getElementById("QTY").value;
               // var stratRangeValue = document.getElementById("SRANGE").value;
               // var endRangeValue = document.getElementById("ERANGE").value;
               // var suffix = document.getElementById("SUFFIX").value;
               // var dtfrmt = document.getElementById("DTFRMT").value;
               // var rangeCnt = endRangeValue-stratRangeValue;
               // var locId = document.getElementById("LOC").value;
                var QtyValue = document.form.QTY.value;
                var stratRangeValue = document.form.SRANGE.value;
                var endRangeValue = document.form.ERANGE.value;
                var suffix = document.form.SUFFIX.value;
                var dtfrmt = document.form.DTFRMT.value;
                var rangeCnt = endRangeValue-stratRangeValue;
                var locId = document.form.LOC.value;
                var itemNo = document.form.ITEMNO.value;
                var nostkflg = document.form.NONSTKFLG.value;
                var uom = document.form.UOM.value;
                
		
		var formAction = "GET_RANGE_BATCH_DETAILS";
		var urlStr = "/track/OutboundOrderHandlerServlet";
		// Call the method of JQuery Ajax provided
		$.ajax({type: "POST",url: urlStr, data: { 
                  LOC : locId,
                            ITEMNO : itemNo,
                            USERID : "<%=sUserId%>",
                            PLANT : "<%=PLANT%>",
                            RANGECNT:rangeCnt,
                            SUFFIX:suffix,
                            DTFRMT:dtfrmt,
                            RANGECNT:rangeCnt,
                            SRANGE:stratRangeValue,
                            PICKQTY:QtyValue,
                            NONSTKFLG:nostkflg,
                            UOM:uom,
                ACTION: formAction},dataType: "json", success: callback });
	
	}
        
        function callback(data){
		
		var outPutdata = getTable();
		var ii = 0;
		var errorBoo = false;
		$.each(data.ERROR, function(i,error){
			if(error.ERROR_CODE=="98"){
				errorBoo = true;
			}
		});
               
		if(!errorBoo){
	        $.each(data.SEARCH_DATA, function(i,item){
	        	var bgcolor= ((ii == 0) || (ii % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";
                        var datacolor  =item.DATACOLOR;
	        	outPutdata = outPutdata+
					        	'<TR bgcolor="'+bgcolor+'">'+
									'<TD><FONT color="'+datacolor+'">'+
									'<CENTER>'+item.BATCH+'</a></CENTER>'+
									'</FONT></TD>'+
									'<TD align="left"><FONT color="'+datacolor+'">'+
									item.AVAILQTY+
									'</FONT></TD>'+
									'<TD align="left"><FONT color="'+datacolor+'">'+
									item.PICKQTY+
									'</FONT></TD>'+
									
								'</TR>';
	        	ii++;            
	          });
	        document.form.INVQTY.value = data.TOTAL_QTY;
		}else{
			 document.form.INVQTY.value = "0";
			 outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="8"><BR><CENTER><B><FONT>NO DETAILS FOUND!</FONT></B></CENTER></TD></TR>';
		}
        outPutdata = outPutdata +'</TABLE>';
        document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
        
      }
      
	function getTable(){
		return '<TABLE border="0" cellspacing="0"  align="center" class="table">'+
		'<thead style="background: #eaeafa; font-size: 15px;">'+
		'<TR>'+
		'<TH align="center"  WIDTH="10%">BATCH</TH>'+
		'<TH  WIDTH="20%">AVAIL QUANTITY</TH>'+
		'<TH  WIDTH="10%">ISSUE QUANTITY</TH>'+
    		'</TR>'+
    		'</thead>';		
	}
        
	document.form.LOC.focus();
        document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
</script>

</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
