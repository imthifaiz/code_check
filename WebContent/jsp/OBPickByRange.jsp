<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<!-- <html> -->
<%--New page design begin --%>
<%
String title = "Picking Sales Order By Range";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
    <jsp:param name="mainmenu" value="<%=IConstants.SALES_TRANSACTION%>"/>
</jsp:include>
<%--New page design end --%>
<%
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
         String plant=(String)session.getAttribute("PLANT");
	
%>

<!-- <script src="js/jquery-1.4.2.js"></script> -->
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/general.js"></script>
<!-- <title>Picking By OutBound Order(By Range)</title>
<link rel="stylesheet" href="css/style.css"> -->

<script>
	var subWin = null;
      function popUpWin(URL) {
        subWin = window.open(URL, 'OutBoundPicking', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
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
 var itemNo = document.form.ITEMNO.value;
     var orderQty = document.form.ORDERQTY.value;
     var pickedQty = document.form.PICKEDQTY.value;
     orderQty = removeCommas(orderQty);
     pickedQty = removeCommas(pickedQty);
     var balanceQty =parseFloat(orderQty)-parseFloat(pickedQty);
     
		var qty = document.form.SERIALQTY.value;
              
                var sRange = document.form.SRANGE.value;
                var eRange = document.form.ERANGE.value;
                var rangeSize = eRange-sRange;
                rangeSize= rangeSize+1;
                var locId = document.form.LOC.value;
                  
                  var pickingQty = parseFloat(qty)*parseFloat(rangeSize);
		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.form.SERIALQTY.focus();
                        return false;
		} 
                
       if (isNumericInput(qty) == false) {
                            alert("Entered Quantity is not a valid Qty!");
                            document.form.SERIALQTY.value = "";
                            document.form.SERIALQTY.focus();
                            return false;
		} 
       
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
                

      if (parseFloat(balanceQty) < parseFloat(pickingQty)) {
                            alert(" Entered Range is greater than the Balance Qty to Pick!");
                            document.form.ERANGE.value = "";
                            document.form.ERANGE.focus();
                            return false;
		} 


	  if(locId=="" || locId.length==0 ) {
                    alert("Enter Location!");
                    document.getElementById("LOC").focus();
                    return false;
	   }
	  else{
		  var urlStr = "/track/OutboundOrderHandlerServlet";
		  $.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
                                USERID : "<%=sUserId%>",
				PLANT : "<%=plant%>",
				ITEMNO : itemNo,
				ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
                               
					if (data.status != "100") {
					
						alert("Not a valid Location");
						var location = document.getElementById("LOC");
						location.value="";
						location.focus();
                                                return false;
					}else{
                                        getView();
                                        }
				}
			});
		}
  }
  
  

function validateLocation(locId) {
var itemNo = document.form.ITEMNO.value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC").focus();
	}else{
		var urlStr = "/track/OutboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
                                USERID : "<%=sUserId%>",
				PLANT : "<%=plant%>",
				ITEMNO : itemNo,
				ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status != "100") {
						alert("Not a valid Location");
						document.getElementById("LOC").value = "";
						document.getElementById("LOC").focus();
					}
				}
			});
		}
	}
        
  
function validatePO(form) {
   var frmRoot=document.form;
   
   var locId = document.form.LOC.value;
   var totalQtypick=0;
   
   
   if(locId=="" || locId.length==0 ) {
        alert("Enter Location!");
        document.getElementById("LOC").focus();
        return false;
    }
               
                var orderQty = document.form.ORDERQTY.value;
                var pickedQty = document.form.PICKEDQTY.value;
                   
                orderQty =  removeCommas(orderQty);
                pickedQty =  removeCommas(pickedQty);
                
                var balanceQty =parseFloat(orderQty)-parseFloat(pickedQty);
                
             
		var qty = document.form.SERIALQTY.value;
              
                var sRange = document.form.SRANGE.value;
                var eRange = document.form.ERANGE.value;
                var rangeSize = parseInt(eRange)- parseInt(sRange);
                rangeSize= rangeSize+1;
                var locId = document.form.LOC.value;
                  
                var pickingQty = parseFloat(qty)*parseFloat(rangeSize);
		if (qty == "" || qty.length == 0) {
			alert("Enter Pick Quantity!");
			document.form.SERIALQTY.focus();
                        return false;
		} 
                
                if (isNumericInput(qty) == false) {
                            alert("Entered Quantity is not a valid Qty!");
                            document.form.SERIALQTY.value = "";
                            document.form.SERIALQTY.focus();
                            return false;
		} 
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
                
                 if (parseFloat(balanceQty) < parseFloat(pickingQty)) {
                
                             alert(" Entered Range is greater than the Balance Qty to Pick!");
                             document.form.ERANGE.value = "";
                             document.form.ERANGE.focus();
                             return false;
		} 
         		
                 if(frmRoot.TRANSACTIONDATE.value=="" || frmRoot.TRANSACTIONDATE.value.length==0 )
            	 {
                	   alert("Please select Transaction Date");
            		frmRoot.TRANSACTIONDATE.focus();
            		return false;
                 }

		

   if(rangeSize>0){
	 
	   document.form.action ="/track/DoPickingServlet?action=Pick/IssueConfirmByRange";
	   document.form.submit();
	   return true;
   }
 
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
  
	
function getView(){

  var QtyValue = document.getElementById("SERIALQTY").value;
        var stratRangeValue = document.getElementById("SRANGE").value;
                 var endRangeValue = document.getElementById("ERANGE").value;
                 var rangeCnt = endRangeValue-stratRangeValue;
                     var locId = document.getElementById("LOC").value;
                  var itemNo = document.form.ITEMNO.value;
                   var suffix = document.getElementById("SUFFIX").value;
                var dtfrmt = document.getElementById("DTFRMT").value;
                var uom = document.form.UOM.value;
		var formAction = "GET_RANGE_BATCH_DETAILS";
		var urlStr = "/track/OutboundOrderHandlerServlet";
		// Call the method of JQuery Ajax provided
		$.ajax({type: "POST",url: urlStr, data: { 
                  LOC : locId,
                            ITEMNO : itemNo,
                            USERID : "<%=sUserId%>",
                            PLANT : "<%=plant%>",
                            SUFFIX:suffix,
                            DTFRMT:dtfrmt,
                            RANGECNT:rangeCnt,
                            SRANGE:stratRangeValue,
                            PICKQTY:QtyValue,
                            UOM:uom,
                ACTION: formAction},dataType: "json", success: callback });
	
	}
        
        function callback(data){
		//var data2 = '{"items":[{"ITEM_ID":"1231","ORDER_SEQ":"12","ORDER_DES":"12312","WAREHOUSE":"1231","LOCATION":"123123","BUDGET":"NULL","REMARKS":"123123"},{"ITEM_ID":"1231","ORDER_SEQ":"234","ORDER_DES":"DFSF SDFSDF SDF","WAREHOUSE":"234","LOCATION":"2344","BUDGET":"NULL","REMARKS":"2344"}]}';
		//var data = JSON.parse(data1);
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
									'<TD align=right><FONT color="'+datacolor+'">'+
									item.AVAILQTY+
									'</FONT></TD>'+
									'<TD align=right><FONT color="'+datacolor+'">'+
									item.PICKQTY+
									'</FONT></TD>'+
									
								'</TR>';
	        	ii++;            
	          });
	        document.form.TOTALAVAILQTY.value = data.TOTAL_QTY;
		}else{
			 document.form.TOTALAVAILQTY.value = "0";
			outPutdata = outPutdata+ '<TR bgcolor="#FFFFFF"><TD COLSPAN="8"><BR><CENTER><B><FONT COLOR="RED">NO DETAILS FOUND!</FONT></B></CENTER></TD></TR>';
		}
        outPutdata = outPutdata +'</TABLE>';
        document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
        
      }
	function getTable(){
		return '<TABLE border="0" cellspacing="0"  align="center" class="table">'+
		'<thead style="background: #eaeafa; font-size: 15px">'+
		'<TR>'+
		'<TH  WIDTH="10%">Batch</FONT></TH>'+
		'<TH  WIDTH="20%">Available Quantity</FONT></TH>'+
		'<TH  WIDTH="10%">Pick Quantity</FONT></TH>'+
    		'</TR>'+
    		'<thead>';		
	}
        
        

  
</script>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="db" class="com.track.gates.defaultsBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
       StrUtils strUtils=new StrUtils();
       String action   = su.fString(request.getParameter("action")).trim();
       
       
       DoHdrDAO _DoHdrDAO = new DoHdrDAO();
    _DoHdrDAO.setmLogger(mLogger);
       String  fieldDesc="";
       String   ORDERNO    = "",ITEMNO   = "", ITEMDESC  = "",UOM="",
       LOC   = "" , CHECKQTY="",BATCH  = "", REF   = "",ORDERLNO,
       QTY = "",RECEIVEQTY="",CUSTNAME="",ORDERQTY="",INVQTY="",PICKINGQTY="",PICKEDQTY="",SRANGE="",ERANGE="",SUFFIX="",DTFRMT="",
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="",AVAILABLEQTY="",TRANSACTIONDATE="",UOMQTY="";
              
       ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=strUtils.fString(request.getParameter("ORDERLNO"));
     
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")));
       LOC = strUtils.fString(request.getParameter("LOC"));
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       QTY = strUtils.fString(request.getParameter("QTY"));
       PICKINGQTY = strUtils.fString(request.getParameter("PICKINGQTY"));
       INVQTY = strUtils.fString(request.getParameter("QTY"));
       SRANGE = strUtils.fString(request.getParameter("SRANGE"));
       ERANGE = strUtils.fString(request.getParameter("ERANGE"));
       SUFFIX = strUtils.fString(request.getParameter("SUFFIX"));
       DTFRMT = strUtils.fString(request.getParameter("DTFRMT"));
       REF = strUtils.fString(request.getParameter("REMARKS"));
       ORDERQTY = strUtils.formatNum(strUtils.fString(request.getParameter("ORDERQTY")));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       PICKEDQTY = strUtils.formatNum(strUtils.fString(request.getParameter("PICKEDQTY")));
       AVAILABLEQTY = strUtils.fString(request.getParameter("AVAILABLEQTY"));
 	   TRANSACTIONDATE = strUtils.fString(request.getParameter("TRANSACTIONDATE"));
       	 String SETCURRENTDATE_PICKANDISSUE = new PlantMstDAO().getSETCURRENTDATE_PICKANDISSUE(plant);//Thanzith

       DateUtils _dateUtils = new DateUtils();
       String curDate =_dateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
       
       ArrayList list = _DoHdrDAO.getOutBoundOrderCustamerDetailsByWMS(plant,ORDERNO);
       ItemMstDAO itemmstdao = new ItemMstDAO();
       itemmstdao.setmLogger(mLogger);
       //UOM = itemmstdao.getItemUOM(plant,ITEMNO);
     UOM =  StrUtils.fString(request.getParameter("UOM"));
       UOMQTY =  StrUtils.fString(request.getParameter("UOMQTY"));
       
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
        ArrayList list1 = _DoHdrDAO.getOutBoundOrderCustamerDetailsByWMS(plant,ORDERNO);

        for(int i=0;i<list1.size();i++)
        {
          Map m1 = (Map)list1.get(i);
      
           CUSTNAME=(String)m1.get("custname");
           CONTACTNAME = (String)m1.get("contactname");
           TELNO=(String)m1.get("telno");
           EMAIL=(String)m1.get("email");
           ADD1=(String)m1.get("add1");
           ADD2=(String)m1.get("add2");
           ADD3=(String)m1.get("add3");
      
      }
     
    }

%>
<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onClick="window.location.href='../salestransaction/orderpickserial?action=View&DONO='+form.ORDERNO.value+'&PLANT='+form.PLANT.value">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

<form class="form-horizontal" name="form" method="post"  action="/track/DoPickingServlet?">
   
        
      	<center>
      <h2><small><%=fieldDesc%></small></h2>	
   </center>
   
   		<INPUT name="ORDERLNO" type="hidden" value="<%=ORDERLNO%>" size="1"	MAXLENGTH=80>
		<INPUT name="LOGIN_USER" type="hidden" value="<%=sUserId %>" size="1"	MAXLENGTH=80>
		<INPUT name="CHECKQTY" type="hidden" value="<%=QTY%>" size="1" MAXLENGTH=80>
		<INPUT name="INVQTY" type="hidden" value="<%=INVQTY%>" size="1"		MAXLENGTH=80>
        <INPUT type="hidden" name="PLANT" value="<%=plant%>">
      
       <div class="form-group">
       <label class="control-label col-sm-2" for="Order No">Order Number:</label>
       <div class="col-sm-3">
       <INPUT name="ORDERNO" type="TEXT" value="<%=ORDERNO%>" size="30" MAXLENGTH=20 class="form-control" readonly="readonly">
   	   </div>  
   	   <div class="form-inline">
        <label class="control-label col-sm-2" for="Order Qty">Order Quantity:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT name="ORDERQTY" class="form-control"	type="TEXT" value="<%=ORDERQTY%>" size="20" MAXLENGTH=80 readonly>
        <span class="input-group-btn"></span>
        <INPUT name="UOM" type="TEXT" value="<%=UOM%>" size="10" MAXLENGTH="15" class="form-control" readonly />
        </div>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Email Address">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT name="EMAIL" value="<%=EMAIL%>" class="form-control" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		</div>
 		</div>
 		
 		<div class="form-group">
  	   <label class="control-label col-sm-2" for="Customer name">Customer Name:</label>
       <div class="col-sm-3">
       <INPUT name="CUSTNAME" class="form-control" type="TEXT" value="<%=su.forHTMLTag(CUSTNAME)%>" size="30" MAXLENGTH=80 readonly>
    	</div>
    	<div class="form-inline">
        <label class="control-label col-sm-2" for="Picked Qty">Picked Quantity:</label>
        <div class="col-sm-3">
        <INPUT name="PICKEDQTY" type="TEXT"		class="form-control" value="<%=PICKEDQTY%>" style="width: 100%" MAXLENGTH="80"	readonly />
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Unit Number">Unit No:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD1" class="form-control"	value="<%=ADD1%>" type="hidden" size="30" MAXLENGTH=80 readonly>
          		<INPUT name="UOMQTY" type="hidden" value="<%=UOMQTY%>" size="1"	MAXLENGTH=80>
       <!--  </div>
 		</div> -->
 		</div>
 		</div>
 		
 		
 	    <div class="form-group">
    	<label class="control-label col-sm-2" for="Contact Name">Contact Name:</label>
        <div class="col-sm-3">
        <INPUT name="CONTACTNAME" class="form-control" value="<%=CONTACTNAME%>" type="TEXT" size="30" MAXLENGTH=80 readonly>
        </div>
 		</div>
 		
        <div class="form-group">
        <label class="control-label col-sm-2" for="Product Id">Product ID:</label>
        <div class="col-sm-3">
        <INPUT name="ITEMNO" type="TEXT" value="<%=ITEMNO%>" size="30" MAXLENGTH=80 class="form-control" readonly="readonly">
       </div>
 		</div>
 		 
        <div class="form-group">
        <label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-3">
        <INPUT name="ITEMDESC" class="form-control" type="TEXT" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="30" MAXLENGTH=80 readonly>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Telephone">Telephone:</label>
        <div class="col-sm-3"> -->
        <INPUT name="TELNO" class="form-control"	value="<%=TELNO%>" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		</div>
  
  
 		<!-- <div class="form-group">
       	<div class="form-inline">
    	<label class="control-label col-sm-7" for="Buiding Name">Building:</label> 
        <div class="col-sm-3"> -->
        <INPUT name="ADD2" value="<%=ADD2%>" class="form-control" type="hidden" value="" size="30" MAXLENGTH=80	readonly>
        <!-- </div>
 		</div>
 		</div> -->
 		
 		<!-- <div class="form-group">
        <div class="form-inline">
    	<label class="control-label col-sm-7" for="street">Street:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD3" value="<%=ADD3%>" class="form-control" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div>
 		</div> -->
 		
 		<div class="form-group">
       <label class="control-label col-sm-1" for="Location" >Location:</label>
       <div class="col-sm-2">
       <div class="input-group">
      <INPUT class="form-control" name="LOC" id="LOC" type="TEXT" value="<%=LOC%>" size="15"
      onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value);}"	MAXLENGTH="80">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/OutBoundPickingLoc.jsp?ITEMNO='+form.ITEMNO.value+'&Loc='+form.LOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-1 pull-left" for="Suffix" >Suffix:</label>
  		<div class="col-sm-1">   
  		<INPUT class="form-control" name="SUFFIX" id="SUFFIX"  value="<%=SUFFIX%>" type="TEXT" size="6" MAXLENGTH="7">
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-1" for="Dt Frmt" >Dt Frmt:</label>
        <div class="col-sm-1">
      	<INPUT class="form-control" name="DTFRMT" id="DTFRMT" value="<%=DTFRMT%>" type="TEXT" size="7" MAXLENGTH="7">
    	</div>
 		</div> 		
 		<div class="form-inline">
       <label class="control-label col-sm-1" for="Range Start" >Range-Start:</label>
       <div class="col-sm-1">
       <INPUT class="form-control" name="SRANGE" id="SRANGE" type="TEXT" value="<%=SRANGE%>" size="7"	MAXLENGTH=10> 
   		</div>
   		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-1" for="No of Batch End" >End:</label>
  		<div class="col-sm-1">   
  		<INPUT class="form-control" name="ERANGE" id="ERANGE" type="TEXT"  value="<%=ERANGE%>" size="10" MAXLENGTH=10>
  		</div>
  		</div>  		
 		</div>
 		 		
 		<div class="form-group">
  		<label class="control-label col-sm-1" for="Receiving Qty">Pick Qty:</label>
        <div class="col-sm-1">
      	<INPUT class="form-control" name="SERIALQTY" id="SERIALQTY" value="1" type="TEXT" size="15" MAXLENGTH="80" \>
    	</div> 		  
 		<div class="form-inline">
 				<label class="control-label col-sm-2" for="Transaction Date">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Transaction Date:</label>
<!--         <label class="control-label col-sm-2 required" for="Transaction Date" >Transaction Date:</label> -->
        <div class="col-sm-3">
        <div class="input-group">  
        <%if(SETCURRENTDATE_PICKANDISSUE.equals("1")){%>        
       <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE" readonly="readonly"	value="<%=TRANSACTIONDATE%>" type="TEXT" size="30" MAXLENGTH="80"> 
       <%}else {%>
       <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE" readonly="readonly"	value="" type="TEXT" size="30" MAXLENGTH="80">
       <%}%>
        </div>
    	</div>
    	</div>
    	<div class="form-inline">
  		<label class="control-label col-sm-1" for="Remarks">Remarks:</label>
        <div class="col-sm-2">
      	<INPUT class="form-control" name="REMARKS" id="REMARKS"	value="<%= REF%>" type="TEXT" size="20" MAXLENGTH="80">
    	</div> 
    	</div>
    	<div class="form-inline">
 		<div class="col-sm-1">
 		<button type="button" class="Submit btn btn-default" onClick="validateInputs();" ><b>Pick</b></button>
 		</div>
 		</div>
		<INPUT name="TOTALAVAILQTY" type="hidden" >
		</div>
		
		<DIV ID='VIEW_RESULT_HERE'></DIV>
		
		<div class="form-group">        
      	<div class="col-sm-12" align="center">
<!--       	<button type="button" class="Submit btn btn-default" onClick="window.location.href='OBOrderPickSummaryByRange.jsp?action=View&DONO='+form.ORDERNO.value+'&PLANT='+form.PLANT.value "><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="validatePO(document.form)" ><b>Confirm</b></button>&nbsp;&nbsp;
      	</div>
        </div>
      

<script LANGUAGE="JavaScript">
   

document.form.LOC.focus();
document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

</script>
</form>
</div>
</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
