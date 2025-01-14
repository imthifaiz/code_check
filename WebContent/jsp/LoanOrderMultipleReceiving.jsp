<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<!-- <html> -->

<%--New page design begin --%>
<%
String title = "Goods Receipt By Rental Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<%--New page design end --%>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">

  var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'LoanOrderReceiving', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
      
  function validateform(form)//form
  {
	 
  	var frmRoot=document.form;
    if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 )
	 {
   	
		alert("Please Enter Order No!");
		frmRoot.ORDERNO.focus();
		return false;
     }
     else  if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 )
	 {
    	
		alert("Please Enter Product ID!");
		frmRoot.ITEMNO.focus();
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
       
     //POUtil  _POUtil= new POUtil();
       StrUtils strUtils=new StrUtils();
       String action   = su.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String plant=(String)session.getAttribute("PLANT");
       LoanDetDAO _LoanDetDAO=new LoanDetDAO();
       
       LoanHdrDAO _loHdrDAO = new LoanHdrDAO();
       
       String  fieldDesc="";
       String   ORDERNO    = "",ITEMNO   = "", ITEMDESC  = "",UOM="",
       LOC   = "" ,LOC1="" ,CHECKQTY="",BATCH  = "", REF   = "",ORDERLNO,
       QTY = "",RECEIVEQTY="",CUSTNAME="",ORDERQTY="",INVQTY="",RECVQTY="",PICKINGQTY ="",PICKED_QTY="",RECVED_QTY="",AVAILABLEQTY="",
       CONTACTNAME="",TELNO="",EMAIL="",ADD1="",ADD2="",ADD3="",AVAILQTY="",TRANSACTIONDATE="",UOMQTY="";
       ORDERNO = strUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=strUtils.fString(request.getParameter("ORDERLNO"));
       CUSTNAME= strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUSTNAME")));
       ITEMNO = strUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC =  strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEMDESC")+" "));
       LOC = strUtils.fString(request.getParameter("FRLOC"));
       LOC1 = strUtils.fString(request.getParameter("TOLOC"));
       UOM = strUtils.fString(request.getParameter("UOM"));
       UOMQTY = strUtils.fString(request.getParameter("UOMQTY"));
       PICKED_QTY=strUtils.fString(request.getParameter("PICKED_QTY"));
       RECVED_QTY=strUtils.fString(request.getParameter("RECVED_QTY"));
       RECVQTY = strUtils.fString(request.getParameter("RECVQTY"));
       PICKINGQTY = strUtils.fString(request.getParameter("PICKINGQTY"));
       AVAILABLEQTY=strUtils.fString(request.getParameter("AVAILABLEQTY"));
        
       BATCH = strUtils.fString(request.getParameter("BATCH"));
       
       QTY = strUtils.fString(request.getParameter("QTY"));
       RECVQTY = strUtils.fString(request.getParameter("RECVQTY"));
       INVQTY = strUtils.fString(request.getParameter("QTY"));
       REF = strUtils.fString(request.getParameter("REF"));
       ORDERQTY = strUtils.fString(request.getParameter("ORDERQTY"));
       CHECKQTY = strUtils.fString(request.getParameter("CHECKQTY"));
       ItemMstDAO itemmstdao = new ItemMstDAO();
      // UOM = itemmstdao.getItemUOM(plant,ITEMNO);
	   TRANSACTIONDATE = strUtils.fString(request.getParameter("TRANSACTIONDATE"));
       DateUtils _dateUtils = new DateUtils();
       String curDate =_dateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
             
       QTY=_LoanDetDAO.getBatchQtyWithRecv(plant,ORDERNO,ITEMNO,ORDERLNO);
       if(QTY.equals(""))
       {
    	   QTY=_LoanDetDAO.getBatchQtyWithOutRecv(plant,ORDERNO,ITEMNO,ORDERLNO);
       }
        if(QTY.equals("")){QTY="0";}
       
       QTY=StrUtils.formatNum(QTY);
       AVAILABLEQTY=StrUtils.formatNum(AVAILABLEQTY);
            
      // ArrayList list=(ArrayList)session.getAttribute("customerlistqry3");
      
       ArrayList list=_loHdrDAO.getLoanOrderHderDetails(plant,	ORDERNO);
       
      
              
        for(int i=0;i<list.size();i++)
        {
          Map m = (Map)list.get(i);
          
        //  if(CUSTNAME.equalsIgnoreCase(strUtils.replaceCharacters2Recv((String)m.get("custname"))))
        // {
           CONTACTNAME = (String)m.get("contactname");
           TELNO=(String)m.get("telno");
           EMAIL=(String)m.get("email");
           ADD1=(String)m.get("add1");
           ADD2=(String)m.get("add2");
           ADD3=(String)m.get("add3");
       // }
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
<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

<form class="form-horizontal" name="form" method="post" action="/track/LoanOrderReceivingServlet?">
   
   <INPUT type="hidden" name="RECVED_QTY" value="<%=RECVED_QTY%>"> 
   <INPUT type="hidden" name="PLANT" value="<%=plant%>">
        
<center>
<h2><small><%=fieldDesc%></small></h2>
</center>
   
       <div class="form-group">
       <label class="control-label col-sm-2" for="Order No">Order Number:</label>
       <div class="col-sm-3">
       <INPUT name="ORDERNO" type="TEXT" value="<%=ORDERNO%>" size="30" class="form-control" readonly MAXLENGTH="80" />
   	   </div> 
   	   <div class="form-inline">
        <label class="control-label col-sm-2" for="From Location">From Location:</label>
        <div class="col-sm-3">
        <INPUT name="LOC" type="TEXT" value="<%=LOC%>"	style="width: 100%" class="form-control" MAXLENGTH="80" readonly />
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Unit Number">Unit No:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD1" class="form-control"	value="<%=ADD1%>" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		</div> 
 	   </div>
 	   
 	   
 	   <div class="form-group">
  	   <label class="control-label col-sm-2" for="customer name">Customer Name:</label>
       <div class="col-sm-3">
       <INPUT name="CUSTNAME" class="form-control" type="TEXT" value="<%=su.forHTMLTag(CUSTNAME)%>" size="30" MAXLENGTH=80 readonly>
    	</div>
    	<div class="form-inline">
        <label class="control-label col-sm-2" for="Order Qty">Order Quantity:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT name="ORDERQTY" class="form-control"	type="TEXT" value="<%=ORDERQTY%>" style="width: 100%" MAXLENGTH=80 readonly>
        <span class="input-group-btn"></span>
        <INPUT name="UOM" class="form-control" type = "TEXT" value="<%=UOM%>" style="width: 100%"  MAXLENGTH=15 readonly>
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
    	<label class="control-label col-sm-2" for="Contact Name">Contact Name:</label>
        <div class="col-sm-3">
        <INPUT name="CONTACTNAME" class="form-control" value="<%=CONTACTNAME%>" type="TEXT" size="30" MAXLENGTH=80 readonly>
        </div>
        <div class="form-inline">
        <label class="control-label col-sm-2" for="Received Quantity">Received Quantity:</label>
        <div class="col-sm-3">
        <INPUT name="RECEIVEDQTY" type="TEXT" class="form-control" value="<%=StrUtils.formatNum(RECVED_QTY)%>" style="width: 100%" MAXLENGTH="80" />
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Buiding Name">Building:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD2" value="<%=ADD2%>" class="form-control" type="hidden" value="" size="30" MAXLENGTH=80	readonly>
        <!-- </div>
 		</div> -->
 		</div>
 		</div>
 		
        <div class="form-group">
        <label class="control-label col-sm-2" for="Product Id">Product ID:</label>
        <div class="col-sm-3">
        <INPUT name="ITEMNO" type="TEXT" value="<%=ITEMNO%>" size="30" class="form-control" readonly MAXLENGTH="80" />
       </div>
       <div class="form-inline">
        <label class="control-label col-sm-2" for="Issue Quantity">Issue Quantity:</label>
        <div class="col-sm-3">
        <INPUT name="PICKED_QTY" type="TEXT" class="form-control" value="<%=StrUtils.formatNum(PICKED_QTY)%>" style="width: 100%" MAXLENGTH="80" />
        </div>
        <!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="street">Street:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD3" value="<%=ADD3%>" class="form-control" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
    	</div>
 		</div>
 		 
        <div class="form-group">
        <label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-3">
        <INPUT name="ITEMDESC" type="TEXT"	value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="30" class="form-control" readonly MAXLENGTH="80">
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Telephone">Telephone:</label>
        <div class="col-sm-3"> -->
        <INPUT name="TELNO" class="form-control"	value="<%=TELNO%>" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="Remarks">Remarks:</label>
        <div class="col-sm-3">
        <INPUT class="form-control name="REF" type="TEXT" value="<%=REF%>" style="width: 100%" MAXLENGTH="80" />
        </div>
    	</div>
 		</div>
 
 		
 		<div class="form-group">
 		<label class="control-label col-sm-4" for="Remarks">Always use the same for the following:</label>
        <div class="col-sm-8">
        <label class="checkbox-inline"> 
		<input type="Checkbox" name="SERIAL_SELECTION" id="SERIAL_SELECTION"  value="SERIAL_SELECTION" onClick="processSerialSelection();"></input>  
		 <b>Serial Receiving</b></label> 
        <label class="checkbox-inline">      
        <input type="Checkbox" name="ALWAYS_SAME_LOCATION" id="ALWAYS_SAME_LOCATION" value="ALWAYS_SAME_LOCATION" onClick="processSameLocation();"></input><b>Location</b></label>
		</div>
        </div>
 		
 		<div class="form-group">
 		<div class="col-sm-12">
 		<table align="center" width="85%" border="0" id="MULTIPLE_PICKING">
			<tr>
				<td width="19%"><b>To Location : </b><div class="input-group"><INPUT class="form-control" name="LOC1_0" id="LOC1_0" type="TEXT"
					value="<%=LOC1%>" size="20"
				onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}"	
					MAXLENGTH=80> <a href="#" class="input-group-addon" data-toggle="tooltip" data-placement="top" Title="Location Details"
			onClick="javascript:popUpWin('LoanOrderMultipleLocList.jsp?ITEMNO='+form.ITEMNO.value+'&LOC1_0='+form.LOC1_0.value+'&INDEX='+'0');">
		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></div></td>
				<td width="2%">&nbsp;&nbsp;</td>
				<td width="20%"><b>Batch : </b><div class="input-group"><INPUT class="form-control" name="BATCH_0" id="BATCH_0" type="TEXT"
					value="<%=BATCH%>" size="20"  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value,0);}"
					MAXLENGTH=40> <a href="#" class="input-group-addon" data-toggle="tooltip" data-placement="top" Title="Batch Details"
			
			onClick="javascript:popUpWin('LoanOrderMultiplePickingBatchList.jsp?ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value+'&BATCH_0='+form.BATCH_0.value+'&INDEX='+'0'+'&UOM='+form.UOM.value);">
			<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></div></td>
		<td width="1%">&nbsp;&nbsp;</td>
				<td width="18%"><b>Available Qty : </b><INPUT class="form-control" name="AVAILABLEQTY_0" id="AVAILABLEQTY_0"
					value="<%=QTY%>" type="TEXT" size="10" readonly MAXLENGTH="80"\></td>
				<td width="1%">&nbsp;</td>
				
				<td width="18%"><b>Receiving Qty : </b><INPUT class="form-control" name="PICKINGQTY_0" id="PICKINGQTY_0"
					value="<%=PICKINGQTY%>" type="TEXT" size="10" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value,0);}"  MAXLENGTH="80"\></td>
				
			</tr>

		</table>		 </div>	
 		 </div>
 		
 				
 		<div class="form-group">        
      	<div class="col-sm-1">&nbsp;</div>
      	<INPUT type="hidden" name="DYNAMIC_PICKING_SIZE">
      	<div class="col-sm-5">
      	<button type="button" class="Submit btn btn-default" onClick="addRow();"><b>Add New Receiving</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="deleteRow('MULTIPLE_PICKING');"><b>Remove Last Added Receiving </b></button>
      	</div>     	
      	 <div class="form-inline">
        <label class="control-label col-sm-2" for="Transaction Date">Transaction Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE"	value="<%=TRANSACTIONDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
        </div>
    	</div>
    	</div>
    	</div>
		
    	
    	<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='LoanOrderReceiving.jsp?action=View&DONO='+form.ORDERNO.value+'&PLANT='+form.PLANT.value "><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" name="actionSubmit" onClick="if(validateform(document.form)){submitForm();}"><b>Confirm Receiving</b></button>&nbsp;&nbsp;
      	</div>
      	</div> 
      	<INPUT name="ORDERLNO" type="hidden" value="<%=ORDERLNO%>" size="1"
			MAXLENGTH=80>
		
		<INPUT name="LOGIN_USER" type="hidden" value="<%=sUserId %>" size="1"
			MAXLENGTH=80>
		
		<INPUT name="CHECKQTY" type="hidden" value="<%=QTY%>" size="1"
			MAXLENGTH=80>
		
		<INPUT name="INVQTY" type="hidden" value="<%=INVQTY%>" size="1"
			MAXLENGTH=80>
			
			<INPUT name="UOMQTY" type="hidden" value="<%=UOMQTY%>" size="1"	>
  		       
  		</form>
		</div>
		





<script>

	
	function addRow() {
	var table = document.getElementById("MULTIPLE_PICKING");
	var rowCount = table.rows.length;

	var serialselection = false;
	var sameLocaionUse = false;
        var sameRemarksUse= false;
       var  allchecked=false;
	if(document.form.SERIAL_SELECTION.checked){
		   serialselection=true;
	   }
	   if(document.form.ALWAYS_SAME_LOCATION.checked){
			sameLocaionUse=true;
	   }
          

        if(serialselection && sameLocaionUse  ){
        allchecked=true;
        }
	var row = table.insertRow(rowCount);
	var firstElementLocationValue = document.getElementById("LOC1_0").value;
	//var firstElementremarksValue = document.getElementById("REMARKS_0").value;
	var locationCell = row.insertCell(0);
		var locationCellText =  "<b>To Location : </b><div class=\"input-group\"><INPUT class=\"form-control\" name=\"LOC1_"+rowCount+"\" ";
		if(sameLocaionUse){
			locationCellText = locationCellText+ "value=\""+firstElementLocationValue+"\" readonly   onClick=\"javascript:keCache();\" ";
		}
		locationCellText = locationCellText+ " id=\"LOC1_"+rowCount+"\" type = \"TEXT\" size=\"20\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value, "+rowCount+");}\" MAXLENGTH=\"80\">";
	//	if(!sameLocaionUse) {
			locationCellText = locationCellText+ "<a class=\"input-group-addon\" data-toggle=\"tooltip\" data-placement=\"right\" Title=\"Location Details\" href=\"#\" onClick=\"javascript:popUpWin('LoanOrderMultipleLocList.jsp?ITEMNO='+form.ITEMNO.value+'&LOC1_"+rowCount+"='+form.LOC1_"+rowCount+".value+'&INDEX="+rowCount+"');\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value, "+rowCount+");}\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></div>";
		//}
	locationCell.innerHTML = locationCellText;
	
	var firstEmptyCell = row.insertCell(1);
	firstEmptyCell.innerHTML = "&nbsp;";
	
        var batchCell = row.insertCell(2);
	batchCell.innerHTML = "<b>Batch : </b><div class=\"input-group\"><INPUT class=\"form-control\" name=\"BATCH_"+rowCount+"\" id=\"BATCH_"+rowCount+"\" value=\"\" type = \"TEXT\" size=\"20\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value, "+rowCount+");}\"  MAXLENGTH=\"40\"> "+
	"<a class=\"input-group-addon\" data-toggle=\"tooltip\" data-placement=\"right\" Title=\"Batch Details\" href=\"#\" onClick=\"javascript:popUpWin('LoanOrderMultipleBatchList.jsp?ITEMNO='+form.ITEMNO.value+'&LOC='+form.LOC.value+'&ORDERNO='+form.ORDERNO.value+'&ORDERLNO='+form.ORDERLNO.value+'&BATCH_"+rowCount+"='+form.BATCH_"+rowCount+".value+'&INDEX="+rowCount+"');\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateBatch(this.value, "+rowCount+");}\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></div>";

	var secondEmptyCell = row.insertCell(3);
	secondEmptyCell.innerHTML = "&nbsp;";
	var availableqtyCell = row.insertCell(4);
	availableqtyCell.innerHTML ="<b>Available Qty : </b><INPUT class=\"form-control\"  name=\"AVAILABLEQTY_"+rowCount+"\" id=\"AVAILABLEQTY_"+rowCount+"\" type = \"TEXT\"  size=\"10\" readonly  MAXLENGTH=\"80\" \> ";
	var thirdemptycell = row.insertCell(5);
	thirdemptycell.innerHTML = "&nbsp;";
	var receivingQtyCell = row.insertCell(6);
	var receiveQtyText = "<b>Receiving Qty : </b><INPUT class=\"form-control\" name=\"PICKINGQTY_"+rowCount+"\" ";
		if(serialselection){
			receiveQtyText = receiveQtyText + " value=\"1\"  ";
		}
    receiveQtyText = receiveQtyText + " id=\"PICKINGQTY_"+rowCount+"\" type = \"TEXT\"  size=\"10\"  MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateQuantity(this.value, "+rowCount+");}\"  >";
	receivingQtyCell.innerHTML = receiveQtyText;
	
	   if(allchecked ){
            document.getElementById("BATCH_"+rowCount).focus();
        }else{
            document.getElementById("LOC1_"+rowCount).focus();
        }
}
function deleteRow(tableID) {
	try {
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		rowCount = rowCount * 1 - 1;
		if (rowCount == 0) {
			alert("Can not remove the default Picking");
		} else {
			table.deleteRow(rowCount);
		}
	} catch (e) {
		alert(e);
	}
}
function submitForm(){
	   var frmRoot=document.form;
	   var table = document.getElementById("MULTIPLE_PICKING");
	   var alreadyReceivedQty=document.getElementById("RECVED_QTY"); 
	   var PICKED_QTY= document.getElementById("PICKED_QTY"); //<--issued qty 
	   var ORDERQTY=document.getElementById("ORDERQTY"); //<--issued qty 
	 
    
	   var rowCount = table.rows.length;
	   document.form.DYNAMIC_PICKING_SIZE.value = rowCount;
	   var totalQtyPick=0;
	   processSelection();
	   for(var index = 0; index<rowCount; index++) {
		    var locationText = document.getElementById("LOC1_"+index);
			var pickingQty = document.getElementById("PICKINGQTY_"+index);
			var batchText = document.getElementById("BATCH_"+index);
			 
		if(document.form.SERIAL_SELECTION.checked){
                  for(var j=0;j<rowCount;j++){
             if(index!=j){
             	  
                     var chkbatch = document.getElementById("BATCH"+"_"+j);
                      if(batchText.value==chkbatch.value){
                       alert("Duplicate batch Scanned !");
              chkbatch.select();
                return false;
            }
            }
             }
}
			if(locationText.value== "" || locationText.value.length==0 ) {
				 
			
				alert("Please Enter To LOC!");
				locationText.focus();
				locationText.style.backgroundColor = "#FFE5EC";
				return false;
			}
			if(batchText.value== "" || batchText.value.length==0 ) {
				
				alert("Please Enter Valid batch!");
				batchText.style.backgroundColor = "#FFE5EC";
				batchText.focus();
				return false;
			}

			if(removeCommas(pickingQty.value)== "" || removeCommas(pickingQty.value).length==0 || removeCommas(pickingQty.value)<=0) {
				alert("Please Enter Valid Qty!");
				 
				pickingQty.style.backgroundColor = "#FFE5EC";
				pickingQty.focus();
				return false;
			}
			
	   }
	   for(var index = 0; index<rowCount; index++) {
		 
	   
		   var pickQty = document.getElementById("PICKINGQTY_"+index);
		   var availQty = document.getElementById("AVAILABLEQTY_"+index);
		   
		   var pkqty = parseFloat(removeCommas(pickQty.value)).toFixed(3);
		   var avqty = parseFloat(removeCommas(availQty.value)).toFixed(3);
		   pkqty = round_float(pkqty,3);
		   avqty = round_float(avqty,3);
		   
		   
		  
		   if(pkqty>avqty)
		   {
			   alert("Entered Quantity is greater than Available Qty");
			  
			   return false;
		   }
		   if (isNumericInput(removeCommas(pickQty.value)) == false) {
			  
				alert("Entered Quantity is not a valid Qty!");
				return false;
			}
		   
		   }
	
	   var alreadyReceivedQty=document.getElementById("RECVED_QTY"); 
	   var PICKED_QTY= document.getElementById("PICKED_QTY"); //<--issued qty 
	   var ORDERQTY=document.form.ORDERQTY.value;
	   
	   alreadyReceivedQty = round_float(alreadyReceivedQty,3);
	   PICKED_QTY = round_float(PICKED_QTY,3);
	   ORDERQTY=round_float(ORDERQTY,3);
	    
 
	   
	  if(parseFloat(alreadyReceivedQty)+parseFloat(pkqty) > parseFloat(PICKED_QTY) ) 
	   {
		  for(var index = 0; index<rowCount; index++) {
			   var receiveQty = document.getElementById("PICKINGQTY_"+index);
			   receiveQty.style.backgroundColor = "#FFE5EC";
		   }
		   alert("Exceeded the Issued Qty. Please check all the Qtys.!");
		   return false;
		      	  
	
	   }else{
		   document.form.action="/track/LoanOrderReceivingServlet?action=Receiving Confirm";
		   document.form.submit();
		   return true;
		   
		  	   	  
	   }
	  
    
}

function validateLocation(locId, index) {
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC1_"+index).focus();
	}else{
		var urlStr = "/track/OutboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
                USERID : "<%=sUserId%>",
				PLANT : "<%=plant%>",
				ITEMNO : "<%=ITEMNO%>",
				ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						document.getElementById("BATCH_"+index).value = "";
						document.getElementById("BATCH_"+index).focus();
						
						
					} else {
						alert("Not a valid Location");
						document.getElementById("LOC1_"+index).value = "";
						document.getElementById("LOC1_"+index).focus();
					}
				}
			});
		}
	}
	
function validateBatch(batch,index) {
	var loanOrderNo = document.form.ORDERNO.value;
	var orderLineNumber = document.form.ORDERLNO.value;
	var locId = document.form.LOC.value;
	var itemNo = document.form.ITEMNO.value;
	var batch = document.getElementById("BATCH_"+index).value;
	var uom = document.form.UOM.value;
        var serialselection = false;
	var sameLocaionUse = false;
        var sameRemarksUse= false;
        var  allchecked=false;
	if(document.form.SERIAL_SELECTION.checked){
		   serialselection=true;
	   }
	   if(document.form.ALWAYS_SAME_LOCATION.checked){
			sameLocaionUse=true;
	   }
          

        if(serialselection && sameLocaionUse ){
           allchecked=true;
        }

       
	if(batch=="" || batch.length==0 ) {
		alert("Enter Batch!");
		document.getElementById("BATCH_"+index).focus();
	}else{
		var urlStr = "/track/LoanOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ORDER_NO : loanOrderNo,
				ORDER_LNNO : orderLineNumber,
				ITEMNO : itemNo,
				LOC : locId,
				BATCH : batch,
				PLANT : "<%=plant%>",
				UOM : uom,
				ACTION : "VALIDATE_BATCH_DETAILS_LOAN_RECEIVE"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						document.getElementById("BATCH_"+index).value = resultVal.BATCH;
					   document.getElementById("AVAILABLEQTY_"+index).value=resultVal.QTY;
						if(index>0)
						 {
                                    var totalpickqty = getSumByLocAndBatch(index);	
                                    totalpickqty = round_float(totalpickqty,3);				
                                    document.getElementById("AVAILABLEQTY_"+index).value=round_float(resultVal.QTY-totalpickqty,3);
						  }	
						
 
                                     if(allchecked){
                                             addRow();
                                          }else{
                                          document.getElementById("PICKINGQTY"+"_"+index).focus();
                                      }

					} else {
						alert("Not a valid Batch");
						document.getElementById("BATCH_"+index).value = "";
						document.getElementById("AVAILABLEQTY_"+index).value="";
						document.getElementById("BATCH_"+index).focus();
					}
				}
			});
		}
	}


function validateQuantity(qty,index) {
	var qty = document.getElementById("PICKINGQTY_"+index).value;
	qty = removeCommas(qty);
	if (qty == "" || qty.length == 0) {
		alert("Enter Quantity!");
		document.getElementById("PICKINGQTY_"+index).focus();
	} else {
		if (isNumericInput(qty) == false) {
			alert("Entered Quantity is not a valid Qty!");
		} else {
			var availableQty = document.getElementById("AVAILABLEQTY_"+index).value;
			availableQty =removeCommas(availableQty);
			availableQty = availableQty*1;
			//alert(qty);
			if(availableQty<qty){
				alert("Entered Quantity is greater than the Available Qty!");
				document.getElementById("PICKINGQTY_"+index).value="";
				document.getElementById("PICKINGQTY_"+index).focus();
			}
		}

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
function processSelection()
{	
	if(document.form.SERIAL_SELECTION.checked){
		 processSerialSelection();
	   }
	   if(document.form.ALWAYS_SAME_LOCATION.checked){
		   processSameLocation();
	   }
		  	  
}

function getavailqty(qty,index)
{
	var availqty=qty;
	removeCommas(availqty);
	if(index>0){
		
		var prevIndex = parseInt(index);
		
		var totalpickqty = getSumByLocAndBatch(index);
		
	var curavailqty = document.getElementById("AVAILABLEQTY_"+index).value;
	curavailqty= removeCommas(curavailqty);
	curavailqty = round_float(curavailqty,3);
	document.getElementById("AVAILABLEQTY_"+index).value = addCommas(curavailqty-totalpickqty);
	}	
}

function getSumByLocAndBatch(index)
{
	var cnt = parseFloat(index);
	var loc0= document.form.LOC.value;
	var batch0 = document.getElementById("BATCH_"+index).value;
	var qty=0;
	for(var i=0;i<cnt;i++)
	{
		var loc = document.form.LOC.value;
		var batch = document.getElementById("BATCH_"+i).value;
		if(loc0==loc && batch0==batch)
		{
			var pickqty = document.getElementById("PICKINGQTY_"+i).value;
			
			pickqty = removeCommas(pickqty);
			
			qty = qty + parseFloat(pickqty);
			
				}
		qty = round_float(qty,3);
	}
	return qty;
}
function processSerialSelection() {
	var table = document.getElementById("MULTIPLE_PICKING");
	var rowCount = table.rows.length;
	for(var index = 0; index<rowCount; index++) {
		var pickQty = document.getElementById("PICKINGQTY_"+index);
                var availQty = document.getElementById("AVAILABLEQTY_"+index);
		if(document.form.SERIAL_SELECTION.checked){

				pickQty.value = 1;
		      pickQty.readOnly = true;
availQty.readOnly=true;
		}
		else
		{
			pickQty.value = "";	 
                        pickQty.readOnly = false;
                        availQty.readOnly=true;
		}
				
	}
}
function processSameLocation(){
	var table = document.getElementById("MULTIPLE_PICKING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("LOC1_0").value;
	for(var index = 1; index<rowCount; index++) {
		var locind = document.getElementById("LOC1_"+index);
		 if(document.form.ALWAYS_SAME_LOCATION.checked){
			 locind.value = defaultValue; }
		 else{
			 locind.value = "";
			 locind.readOnly = false;
		 }
		
		
	}
}

function processSameRemarks(){
	var table = document.getElementById("MULTIPLE_PICKING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("REMARKS_0").value;
	for(var index = 1; index<rowCount; index++) {
		var remarks = document.getElementById("REMARKS_"+index);
		if(document.form.ALWAYS_SAME_REMARKS.checked){
			remarks.value = defaultValue;}
		else
		{
			remarks.value="";
			remarks.readOnly = false;
		}
		
		
	}
}
document.form.LOC1_0.focus();
</script>

</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
