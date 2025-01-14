<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.StrUtils"%>
<%@ page import="com.track.util.StrUtils"%>
<%@ page import="com.track.serviceImplementation.DoHdrServiceImpl"%>
<%@ page import="com.track.db.object.DoHdr"%>
<%@ include file="header.jsp" %>
<%@page import="com.track.constants.IConstants"%>
<jsp:useBean id="PackingDAO"  class="com.track.dao.PackingDAO" />

<%@page import="com.track.constants.IDBConstants"%>

<%--New page design begin --%>
<%
String title = "Create Packing List/Deliver Note";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
    <jsp:param name="submenu" value="<%=IConstants.SALES_TRANSACTION%>"/>
</jsp:include>
<%--New page design end --%>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/typeahead.jquery.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>

 
 <script>

	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'DNPL', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
	function verifyIssuing(orderLNo)
	{

			var netweight = document.getElementById("netweight_"+(orderLNo)).value;
			if (netweight == "" || netweight.length == 0) {
				alert("Please provide net weight for all selected items!");
				document.getElementById("netweight_"+(orderLNo)).focus();
				document.getElementById("netweight_"+(orderLNo)).select();
		        return false;
			}
			var grossweight = document.getElementById("grossweight_"+(orderLNo)).value;
			if (grossweight == "" || grossweight.length == 0) {
				alert("Please provide gross weight for all selected items!");
				document.getElementById("grossweight_"+(orderLNo)).focus();
				document.getElementById("grossweight_"+(orderLNo)).select();
		        return false;
			}
			if(form.packing.selectedIndex==-1)
			{
			   alert("Please select packing");
			 	form.packing.focus();
			 	return false;
			}
			var dimension = document.getElementById("dimension_"+(orderLNo)).value;
			if (dimension == "" || dimension.length == 0) {
				alert("Please provide dimension for all selected items!");
				document.getElementById("dimension_"+(orderLNo)).focus();
				document.getElementById("dimension_"+(orderLNo)).select();
		        return false;
			}
			
			return true;
	}
	
	function checkAll(isChk)
	{
		var len = document.form.chkdDoNo.length;
		 var orderLNo; 
		 if(len == undefined) len = 1;  
	    if (document.form.chkdDoNo)
	    {
	        for (var i = 0; i < len ; i++)
	        {      
	              	if(len == 1){
	              		document.form.chkdDoNo.checked = isChk;
	               	}
	              	else{
	              		document.form.chkdDoNo[i].checked = isChk;
	              	}
	            	
	        }
	    }
	}
	
	/*Need to hode the below  */
 function onUpdate(form){
 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Create Delivery Note/Packing List");
   	 	return false;
    }
 	
  	
    
    var checkFound = false;  
	 var orderLNo;
	 //var len = $('#itemstab tbody tr').lenth;
	 var len = document.form.chkdDoNo.length;
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
    {
		if(len == 1 && (!document.form.chkdDoNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdDoNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = document.form.chkdDoNo.value;
	    	if(!verifyIssuing(orderLNo))	
		    	  return false;
	     }
	
	     else {
		     if(document.form.chkdDoNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = document.form.chkdDoNo[i].value;
		    	 if(!verifyIssuing(orderLNo))	
			    	  return false;
		     }
	     }
          		
        	     
    }
	 //checkFound = true;
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 if (document.form.plno.value == ''){
		    alert ("Please generate packing list & delivery note");
		    return false;		 
	 }
	 
	 /* if (document.form.tnw.value == ''){
		    alert ("Please provide Total Net Weight.");
		    return false;		 
	 }
	 if (document.form.tgw.value == ''){
		    alert ("Please provide Total Gross Weight.");
		    return false;		 
	 } */
	 /* for (var i = 0; i < len ; i++)
	    {
		 if(document.form.chkdDoNo[i].checked && document.getElementById("SRNO_" + (i + 1)).value == ""){
			 alert("Please provide serial numbers for all selected items");
			 return false;
		 }
		 if(document.form.chkdDoNo[i].checked && document.getElementById("netweight_" + (i + 1)).value == ""){
			 alert("Please provide net weight for all selected items");
			 return false;
		 }
		 if(document.form.chkdDoNo[i].checked && document.getElementById("grossweight_" + (i + 1)).value == ""){
			 alert("Please provide gross weight for all selected items");
			 return false;
		 }
		 if(document.form.chkdDoNo[i].checked && document.getElementById("dimension_" + (i + 1)).value == ""){
			 alert("Please provide dimension for all selected items");
			 return false;
		 }
		 if(document.form.chkdDoNo[i].checked && document.getElementById("packing_" + (i + 1)).value == ""){
			 alert("Please provide packing for all selected items");
			 return false;
		 }
	    } */
	   document.form.action ="/track/OrderIssuingServlet?action=DNPL&EDIT="+document.form.EDIT.value;
	   document.form.submit();
  }
  
 
     
 function fullIssuing(isChk)
 {
 	var len = document.form.chkdDoNo.length;
 	 var orderLNo; 
 	 if(len == undefined) len = 1;  
     if (document.form.chkdDoNo)
     {
         for (var i = 0; i < len ; i++)
         {      
               	if(len == 1 && document.form.chkdDoNo.checked){
               		orderLNo = document.form.chkdDoNo.value;
               		setIssuingQty(orderLNo,i); 
               	}
               	else if(len == 1 && !document.form.chkdDoNo.checked){
               		return;
               	}
               	else{
                   	if(document.form.chkdDoNo[i].checked){
               		 orderLNo = document.form.chkdDoNo[i].value;
               		setIssuingQty(orderLNo,i); 
                   	}
               	}
             	      	
                 
         }
     }
 }

 

 function setIssuingQty(orderLNo,i){
 	var len = document.form.chkdDoNo.length;
 	if(len == undefined) len = 1; 
 	 if(len !=1 && document.form.chkdDoNo[i].checked){
 		document.getElementById("issuingQty_"+orderLNo).value = 0;
 		
 	}
 	else{
 		document.getElementById("issuingQty_"+orderLNo).value = 0;	
 	}
 		
 }
 
 function onClear()
	{
	 $(':input').val('');
	 $('#itemstab tbody').empty();
// 	 document.form.action  = "creatednpl.jsp";
// 	 document.form.submit();
	 
	 document.form.DONO.value="";
	 document.form.GINO.value="";
	 document.form.INVOICENO.value="";
	 document.form.CUSTOMER.value="";
	 document.form.tnw.value="";
	 document.form.tgw.value="";
	 document.form.netpacking.value="";
	 document.form.netdimension.value="";
	 document.form.dnplremarks.value="";
	}
</script>


<jsp:useBean id="su"  class="com.track.util.StrUtils" />

<%
   String plant=(String)session.getAttribute("PLANT");
   Map checkedDOS = (Map) request.getSession().getAttribute("checkedDOS");
   String DONO     = StrUtils.fString(request.getParameter("DONO")); 
   String INVOICENO     = StrUtils.fString(request.getParameter("INVOICENO"));
   String GINO     = StrUtils.fString(request.getParameter("GINO"));
   String HID     = StrUtils.fString(request.getParameter("HID"));
   String EDIT = StrUtils.fString(request.getParameter("EDIT")).trim();
   System.out.println("EDIT "+ INVOICENO+ ", " +EDIT);
   String Urlret="../salestransaction/packinglistsummary";
   if(EDIT!="")
   {
   title = "Edit Packing List/Deliver Note";
   Urlret="../salestransaction/packinglistdetail?HID="+HID+"&INVOICENO="+INVOICENO+"&GINO="+GINO+"&DONO="+DONO;
   }
   String action   = StrUtils.fString(request.getParameter("action")).trim();
   String result   = StrUtils.fString(request.getParameter("result")).trim();
   String sUserId = (String) session.getAttribute("LOGIN_USER");
   String RFLAG=    (String) session.getAttribute("RFLAG");
   String AFLAG=    (String) session.getAttribute("AFLAG");
   String dnplremarks = StrUtils.fString(request.getParameter("dnplremarks")).trim();
   String dnno = StrUtils.fString(request.getParameter("dnno")).trim();
   String tnw = StrUtils.fString(request.getParameter("tnw")).trim();
   String tgw = StrUtils.fString(request.getParameter("tgw")).trim();
   String plno = StrUtils.fString(request.getParameter("plno")).trim();
   String netdimension = StrUtils.fString(request.getParameter("netdimension")).trim();
   String netpacking = StrUtils.fString(request.getParameter("netpacking")).trim();
   String Packing = StrUtils.fString(request.getParameter("packing")).trim();
   String JobNum = StrUtils.fString(request.getParameter("JobNum")).trim();
   String custName = StrUtils.fString(request.getParameter("custName")).trim();
   
   boolean confirm = false;
   DOUtil _DOUtil=new DOUtil();
   POSUtil _POSUtil=new POSUtil();
   PackingUtil _PackingUtil=new PackingUtil();
   DateUtils _dateUtils = new DateUtils();
   ItemMstDAO _ItemMstDAO=new ItemMstDAO();
   InvoiceDAO invoiceDAO=new InvoiceDAO();
   
   _DOUtil.setmLogger(mLogger);
   _POSUtil.setmLogger(mLogger);
   _ItemMstDAO.setmLogger(mLogger);
   
   
   String vend = "",deldate="",custCode="",personIncharge = "",cmd="",contactNum = "",INVOICE="",INVOICEHDRID="";
   String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
   String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",loc = "",REF = "",ISSUEDATE="",CashCust="";
   String sSaveEnb    = "disabled",fullIssue = "",allChecked = "";;
   
   String SHIPSTATUS="",SHIPDATE="",INTRANSITSTATUS="",INTRANSITDATE="",DELIVERYSTATUS="",DELIVERYDATE="",TRANSPORT="",CLEARAGENT="",CONTACTNAME="",TRACKINGNO="",FREIGHT="",JOURNEY="",CARRIER="";
   String sSTATUS="",STATUSDATE="",SHIPDNPLID="",transportmode = "";
   
   CashCust = StrUtils.fString(request.getParameter("CashCust"));
   ISSUEDATE     = StrUtils.fString(request.getParameter("ISSUEDATE"));
   custCode= StrUtils.fString(request.getParameter("custCode"));
   String curDate =_dateUtils.getDate();
   if(ISSUEDATE.length()<0|ISSUEDATE==null||ISSUEDATE.equalsIgnoreCase(""))ISSUEDATE=curDate;
   Vector poslist=null;
	 ArrayList prdlist=null;
  boolean invoicexists=false; 
  String fieldDesc="";
  
  Hashtable htCond = new Hashtable();
  htCond.put("A.PLANT", plant);
   
   ArrayList al= new ArrayList();
	  
  	if(HID!="" && EDIT!="")
	  {	  
	  
		al= _PackingUtil.dnplGINO(plant,DONO,GINO, INVOICENO,HID);
	  
	  }
  	DoHdr doHdr = new DoHdr();
  	String sAddr4 = "",sCountry ="",sZip ="",sDesgination ="",sTelNo ="",sHpNo ="",sEmail ="",sFax ="",sState="",sWorkphone ="";
  	String CustName="",Address="",Address2="",Address3="",SHIPCONTACTNAME="",SHIPDESGINATION="",SHIPADDR1="",SHIPADDR2="",SHIPADDR3="",SHIPADDR4="",SHIPSTATE="",SHIPCOUNTRY="",SHIPZIP="",SHIPHPNO="",SHIPWORKPHONE="",SHIPEMAIL="";
  	if(DONO.length()>0){
  	if(DONO!=null){
  	doHdr =new DoHdrServiceImpl().getDoHdrById(plant, DONO);
  	ArrayList arrCust =  new CustUtil().getCustomerDetails(doHdr.getCustCode(), plant);
	sAddr4 = (String) arrCust.get(16);
	sCountry = (String) arrCust.get(5);
	sZip = (String) arrCust.get(6);
	sDesgination = (String) arrCust.get(10);
	sTelNo = (String) arrCust.get(11);
	sHpNo = (String) arrCust.get(12);
	sEmail = (String) arrCust.get(14);
	sFax = (String) arrCust.get(13);
	sState = (String) arrCust.get(22);
	 sWorkphone = (String) arrCust.get(33);
	 CustName=doHdr.getCustName();
	 Address=doHdr.getAddress();
	 Address2=doHdr.getAddress2();
	 Address3=doHdr.getAddress3();
	 SHIPCONTACTNAME=doHdr.getSHIPCONTACTNAME();
	 SHIPDESGINATION=doHdr.getSHIPDESGINATION();
	 SHIPADDR1=doHdr.getSHIPADDR1();
	 SHIPADDR2=doHdr.getSHIPADDR2();
	 SHIPADDR3=doHdr.getSHIPADDR3();
	 SHIPADDR4=doHdr.getSHIPADDR4();
	 SHIPSTATE=doHdr.getSHIPSTATE();
	 SHIPCOUNTRY=doHdr.getSHIPCOUNTRY();
	 SHIPZIP=doHdr.getSHIPZIP();
	 SHIPHPNO=doHdr.getSHIPHPNO();
	 SHIPWORKPHONE=doHdr.getSHIPWORKPHONE();
	 SHIPEMAIL=doHdr.getSHIPEMAIL();
  	}}
     if(result.equalsIgnoreCase("catchrerror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
       // fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
        fieldDesc = "<font class = "+IConstants.FAILED_COLOR+">" + fieldDesc + "</font>";
        allChecked = StrUtils.fString(request.getParameter("allChecked"));
        fullIssue = StrUtils.fString(request.getParameter("fullReceive"));
     }
 %>
 <% if(EDIT.equals("EDIT")) { System.out.println("EDIT "+EDIT);%>
<script type="text/javascript">
document.addEventListener("DOMContentLoaded", function() {
//document.getElementById("AutoId").style.display = "none";
$('#GINO').attr('readonly','readonly');
$('#DONO').attr('readonly','readonly');
$('#CUSTOMER').attr('readonly','readonly');
<%if(!INVOICENO.equals("")) {%>
$('#INVOICENO').attr('readonly','readonly');
<%}%>
document.getElementById("btnView").style.display = "none";
document.getElementById("btnCustomer").style.display = "none";
document.getElementById("btnCustomerDown").style.display = "none";
});
</script>
<%}else{ System.out.println("EDIT 0");%>
<script type="text/javascript">
document.addEventListener("DOMContentLoaded", function() {
	onNew();
});
</script>
<%}%>
 <%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 
<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><a href="../salesTransactionDashboard"><span class="underline-on-hover">Sales Transaction Dashboard</span> </a></li>       
                <li><a href="../salestransaction/packinglistsummary"><span class="underline-on-hover">Packing List/Delivery Note Summary</span> </a></li>            
                <li><label>Create Packing List/Deliver Note</label></li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='<%=Urlret %>'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 <CENTER><strong><font style="font-size:18px;"><%=fieldDesc%></font></strong></CENTER>

<form class="form-horizontal" name="form" id="form" method="post" action="/track/OrderIssuingServlet?">

   		
       <div class="form-group">
        						<div class="row">
							<div class="col-sm-2.5">
								<label class="control-label col-sm-2" for="search">Search</label>
							</div>
							<div class="col-sm-4 ac-box">  		
					  			<input type="text" class="ac-selected form-control" id="DONO" 
					  			name="DONO" value="<%=DONO%>" placeholder="ORDER NO">
								<span class="select-icon" 
								onclick="$(this).parent().find('input[name=\'DONO\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
					  		</div>
							<div class="col-sm-4 ac-box">
								<div class="input-group"> 
								 <input type="text" class="ac-selected form-control typeahead" id="GINO" 
								 placeholder="GINO" name="GINO" value="<%=GINO%>">				
								<span class="select-icon"  
								onclick="$(this).parent().find('input[name=\'GINO\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>							
								</div>
							</div>	
						</div>
						</br>
						<div class="row">
					  		<div class="col-sm-2">
					  		</div>
							<div class="col-sm-4 ac-box">
								<div class="input-group"> 
								 <input type="text" class="ac-selected form-control typeahead" id="INVOICENO" 
								 placeholder="INVOICE NO" name="INVOICENO" value="<%=INVOICENO%>">				
								<span class="select-icon"  
								onclick="$(this).parent().find('input[name=\'INVOICENO\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>							
								</div>
							</div>
							<div class="col-sm-4 ac-box">
								<div class="input-group">
								<input type="text" class="ac-selected  form-control typeahead" id="CUSTOMER" placeholder="CUSTOMER" name="CUSTOMER" type = "TEXT" value="<%=su.forHTMLTag(custName)%>">
				<span class="select-icon" id="btnCustomerDown" onclick="$(this).parent().find('input[name=\'CUSTOMER\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" id="btnCustomer" onClick="javascript:popUpWin('customer_list_issue_summary.jsp?TYPE=ACCTCUST&CUSTOMER='+form.CUSTOMER.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
								</div>
							</div>	
						</div>
						<%if(CustName.length()>0){ %>
						<div class="form-group shipCustomer-section">
					<div class="col-sm-2"></div>
					<div class="col-sm-4" style="line-height: 7px; font-size: 13px;">
						<h5 style="font-weight: bold;">Delivery To</h5>
						<p><%=CustName %></p>
						<p><%=Address %> <%=Address2 %></p>
						<p><%=Address3 %>  <%=sAddr4%></p>
						<p><%=sState%></p>
						<p> <%=sCountry%> <%=sZip%></p>
						<p><%=sHpNo%></p>
						<p><%=sWorkphone%></p>
						<p><%=sEmail%></p>
						
						
					</div>
					<div class="col-sm-4" style="line-height: 7px; font-size: 13px;">
						<h5 style="font-weight: bold;">Shipped To</h5>
						<div id="cshipaddr">							
						<p><%=SHIPCONTACTNAME %></p>
						<p><%=SHIPDESGINATION%></p>
						<p><%=SHIPADDR1 %> <%=SHIPADDR2 %></p>
						<p><%=SHIPADDR3 %> <%=SHIPADDR4 %></p>
						<p><%=SHIPSTATE%></p>
						<p><%=SHIPCOUNTRY %> <%=SHIPZIP %></p>
						<p><%=SHIPHPNO %></p>
						<p><%=SHIPWORKPHONE %></p>
						<p><%=SHIPEMAIL%></p>
						</div>
					</div>
				</div>
				<% } %>
						<div class="row" style="padding:1px">
					  		<div class="col-sm-2">
					  		</div>
					  		<%-- <div class="col-sm-4">
								<div class="input-group">
								<INPUT name="JOB_NUM"   class="form-control" MAXLENGTH="20" readonly value="<%=JobNum%>" placeholder="Reference No">
								</div>
							</div> --%>
							<div class="col-sm-10 txn-buttons">
							<button type="button" class="btn btn-success" id="btnView" onClick="javascript:return viewOutboundOrders();">Search</button>
							</div>
					  	</div>
 				</div> 
 		
 					<INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
                    <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
                    <INPUT type = "hidden"  name="EDIT" value="<%=EDIT%>">
                    <INPUT type = "hidden"  name="HID" value="<%=HID%>">
                    <INPUT  Type=Checkbox hidden style="border:0;" name = "CashCust" id="CashCust" >
         <div class="row">
  		<div class="col-12 col-sm-12">   
  		<INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
        	<b>Select/Unselect All</b>
  		</div>
  		</div>           
       			  		
  		 <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%" class="table table-bordred table-striped" id="itemstab" >
         <thead>
         <tr>
         <th style="font-size: smaller;" width="4%"">S/N</th>
         <!-- <th style="font-size: smaller;" width="4%">Order Line No</th> -->
         <th style="font-size: smaller;" width="15%">PRODUCT ID</th>
         <th style="font-size: smaller;" width="16%">DESCRIPTION</th>
         <th style="font-size: smaller;" width="6%">HSCODE</th>
         <th style="font-size: smaller;" width="6%">COO</th>
         <th style="font-size: smaller;" width="5%">UOM</th>
          <th style="font-size: smaller;" width="4%">QTY</th>
         <th style="font-size: smaller;" width="8%">NET WEIGHT</th>
         <th style="font-size: smaller;" width="8%">GROSS WEIGHT</th>
         <th style="font-size: smaller;" width="14%">PACKING</th>
          <th style="font-size: smaller;" width="14%">DIMENSION</th>
    
        
         </tr>
       	 </thead>
       	 <tbody style="font-size: 15px;">
       	  <%
       	  
       	  if(EDIT=="")
       	  {
       	  if(DONO.isEmpty() && GINO.isEmpty())
       	  {
       		if(!INVOICENO.isEmpty())
       		{
       		invoicexists = invoiceDAO.isExisit(htCond, " AND GINO='' AND INVOICE like '%"+INVOICENO+"%' ");
       		al= invoiceDAO.dnplINVOICE(plant,INVOICENO);
       		}
       	  }
       	  if(!invoicexists)
       	  {
            al= _DOUtil.dnplGINO(plant,DONO, GINO,INVOICENO);
       	  }
       	  }
      String issuingQty = "",value = null,batch = "NOBATCH";
      if(al.size()==0)
      {
  	    AFLAG="";
      }
       if(al.size()>0)
       {
    	 AFLAG="DATA";
       for(int i=0 ; i<al.size();i++)
       {
    	   
    	   issuingQty = "";
    	   batch = "NOBATCH";
          Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
          String dono = (String)m.get("dono");
          String dolnno = (String)m.get("dolnno");
          String custname= (String)m.get("custname");
          String item= (String)m.get("item");
           String qtyis= (String)m.get("qtyis");
          String desc= (String)m.get("Itemdesc");//_ItemMstDAO.getItemDesc(plant ,item);
          String uom = (String)m.get("uom");//_ItemMstDAO.getItemUOM(plant ,item);
          String netweight = (String)m.get("netweight");
          String grossweight = (String)m.get("grossweight");
          String hscode = (String)m.get("hscode");
          String coo = (String)m.get("coo");
          String dimension = (String)m.get("dimension");
          String packing = (String)m.get("PACKING");
           plno =(String)m.get("PLNO");
           dnno =(String)m.get("DNNO");
           tgw =(String)m.get("TOTALGROSSWEIGHT");
           tnw =(String)m.get("TOTALNETWEIGHT");
           netpacking =(String)m.get("NETPACKING");
           netdimension =(String)m.get("NETDIMENSION");
           dnplremarks =(String)m.get("DNPLREMARKS");
           ISSUEDATE =(String)m.get("ISSUEDATE");
           
           if(EDIT!="") {
	           SHIPSTATUS =(String)m.get("SHIPSTATUS");
	           SHIPDATE =(String)m.get("SHIPDATE");
	           INTRANSITSTATUS =(String)m.get("INTRANSITSTATUS");
	           INTRANSITDATE =(String)m.get("INTRANSITDATE");
	           DELIVERYSTATUS =(String)m.get("DELIVERYSTATUS");
	           DELIVERYDATE =(String)m.get("DELIVERYDATE");
	           TRANSPORT =(String)m.get("TRANSPORT");
	           CLEARAGENT =(String)m.get("CLEARAGENT");
	           CONTACTNAME =(String)m.get("CONTACTNAME");
	           TRACKINGNO =(String)m.get("TRACKINGNO");
	           FREIGHT =(String)m.get("FREIGHT");
	           JOURNEY =(String)m.get("JOURNEY");
	           CARRIER =(String)m.get("CARRIER");
	           SHIPDNPLID  =(String)m.get("SHIPDNPLID");
	           int trans = Integer.valueOf(TRANSPORT);
		       	TransportModeDAO transportmodedao = new TransportModeDAO();
				if(trans > 0){
					transportmode = transportmodedao.getTransportModeById(plant, Integer.valueOf(TRANSPORT));
				}else{
					transportmode = "";
				}
	           
	           if(SHIPSTATUS.equalsIgnoreCase("")&&SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")){
	        	   sSTATUS = INTRANSITSTATUS;
	        	   STATUSDATE = INTRANSITDATE;
	           }else if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&INTRANSITSTATUS.equalsIgnoreCase("")&&INTRANSITDATE.equalsIgnoreCase("")){
	        	   sSTATUS = SHIPSTATUS;
	        	   STATUSDATE = SHIPDATE;
	           }else if(!SHIPSTATUS.equalsIgnoreCase("")&&!SHIPDATE.equalsIgnoreCase("")&&!INTRANSITSTATUS.equalsIgnoreCase("")&&!INTRANSITDATE.equalsIgnoreCase("")){
	        	   sSTATUS = INTRANSITSTATUS;
	        	   STATUSDATE = INTRANSITDATE;
	           }
        }

           
           INVOICE =(String)m.get("INVOICE");
           INVOICEHDRID=(String)m.get("HID");
           if(checkedDOS!=null && result.equalsIgnoreCase("catchrerror")){
              value = (String)checkedDOS.get(dolnno);   
              
              if(value!=null)
              {
            	  issuingQty  = value.split(":")[0];
            	  batch  = value.split(":")[1];
            	             	  
            	 
              }
              } 
       
       %>
       
      		
        <TR>
               <TD align="center" width="4" ><INPUT Type="checkbox"  style="border:0;background=#dddddd" 
              name="chkdDoNo"  value="<%=dolnno%>" <%if(value!=null){%>checked <%}%>  <%if(EDIT!=""){%>checked <%}%> >
               <input type="hidden" class="form-control" size="20" name="SRNO_<%=dolnno %>" id="SRNO_<%=dolnno %>" value="<%=iIndex%>" />  
               </TD>
               <%-- <TD align="center" width="4"><%=(String)m.get("dolnno")%></TD> --%>
              <TD align="left" width="15%"><%=(String)m.get("item")%><input type="hidden" class="form-control" name="ITEM_<%=dolnno %>" id="ITEM_<%=dolnno %>" value="<%=(String)m.get("item")%>" /> </TD>
              <TD align="left" width="16%"><%=(String)desc%></TD>
             <TD align="left" width="6%"><%=(String)m.get("hscode")%><input type="hidden" class="form-control" name="HSCODE_<%=dolnno %>" id="HSCODE_<%=dolnno %>" value="<%=(String)m.get("hscode")%>" /></TD>
             <TD align="left" width="6%"><%=(String)m.get("coo")%><input type="hidden" class="form-control" name="COO_<%=dolnno %>" id="COO_<%=dolnno %>" value="<%=(String)m.get("coo")%>" /></TD>
             <TD align="left" width="5%"><%=(String)m.get("uom")%><input type="hidden" class="form-control" name="UOM_<%=dolnno %>" id="UOM_<%=dolnno %>" value="<%=(String)m.get("uom")%>" /></TD>
            <TD align="center" id = "QtyIssued_<%=dolnno%>" width="4%"><%=StrUtils.formatNum((String)m.get("qtyis"))%><input type="hidden" class="form-control" name="QTY_<%=dolnno %>" id="QTY_<%=dolnno %>" value="<%=(String)m.get("qtyis")%>" /></TD>
             <TD align="center" width="8%"><INPUT class="form-control" onkeypress="return isNumberKey(event,this,4)" name="netweight_<%=dolnno%>" id="netweight_<%=dolnno%>" value = "<%=StrUtils.formatNum((String)m.get("netweight"))%>" type="TEXT" size="15"  /></TD>
             <TD align="center" width="8%"><INPUT class="form-control" onkeypress="return isNumberKey(event,this,4)" name="grossweight_<%=dolnno%>" id="grossweight_<%=dolnno%>" value = "<%=StrUtils.formatNum((String)m.get("grossweight"))%>" type="TEXT" size="15"  /></TD>
             <TD align="center" width="14%"><select class="form-control" data-toggle="dropdown" data-placement="left" name="packing" style="width: 100%" id="packing"> 
             <%
				  ArrayList ccList = PackingDAO.getPackingDetails(plant,"");
					for(int j=0 ; j < ccList.size();j++)
		      		 {
						Map mm=(Map)ccList.get(j);
						packing = (String)mm.get("PACKING"); %>
				        <option value=<%=packing%>><%=packing%></option>	          
		        <%
       		}
			%>  </select></TD>
             <TD align="center" width="14%"><INPUT class="form-control"  name="dimension_<%=dolnno%>" id="dimension_<%=dolnno%>" value = "<%=(String)m.get("dimension")%>" type="TEXT" size="5" /></TD>
                                    </TR>
           
       <%}} else {%>
       
             <TR> <TD align="center" colspan="11"> Data's Not Found</TD></TR>
       <%}%>
         </tbody>
       </TABLE>
       
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="7">
        <INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
        <INPUT type="Hidden" name="DATACOUNT" value="<%=al.size()%>">
       <INPUT type="Hidden" name="INVOICE" value="<%=INVOICE%>">
       <INPUT type="Hidden" name="INVOICEHDRID" value="<%=INVOICEHDRID%>">
       <INPUT type = "hidden"  name="SHIPDNPLID" value="<%=SHIPDNPLID%>">
       
        <div class="form-group">
        <label class="control-label col-form-label col-sm-2 required" for="plno">Packing List</label>
        <div class="col-sm-4">
        <div class="input-group">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="plno" class="form-control" value="<%=plno%>" readonly/>
        <!-- <span class="input-group-addon"  onClick="onNew();" sNewEnb id="AutoId" >
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span> -->
        </div>
        </div>
<!-- 	        <div class="form-inline"> -->
<label class="control-label col-form-label col-sm-2" for="netdimension">Freight Forwarder</label>
	        <div class="col-sm-4 ac-box">
	        <div class="input-group">
        <input type="hidden" name="FREIGHT_FORWARDERID" value="<%=FREIGHT%>">
        	        <%   if(EDIT=="") {%>
        <input type="text" class="form-control typeahead" id="FREIGHTFORWARDERNO" placeholder="Select a Freight Forwarder" name="FREIGHTFORWARDERNO" value="">
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'FREIGHTFORWARDERNO\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
	        <%}else{ %>
        <input type="text" class="form-control typeahead" id="FREIGHTFORWARDERNO" placeholder="Select a Freight Forwarder" name="FREIGHTFORWARDERNO" value="<%=FREIGHT%>">
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'FREIGHTFORWARDERNO\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
							</span>
	        <%}%>
	        </div>
	        </div>
<!-- 	    	</div> -->
        </div>
        <div class="form-group">
        <label class="control-label col-form-label col-sm-2 required" for="dnno">Delivery Note</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="dnno" class="form-control" value="<%=dnno%>" readonly/>
        </div>
        <!--         	        <div class="form-inline"> -->
	        <label class="control-label col-form-label col-sm-2" for="transport">Transport</label>
	        <div class="col-sm-4 ac-box">
	        <div class="input-group">
	        <input type="hidden" name="TRANSPORTID" value="<%=TRANSPORT%>">
	        <%   if(EDIT=="") {%>
	        <input type="text" class="ac-selected  form-control typeahead" id="transport" placeholder="Select a transport" name="transport" onchange="checktransport(this.value)" value="" >
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i>
			</span>
	        <%}else{ %>
	        <input type="text" class="ac-selected  form-control typeahead" id="transport" placeholder="Select a transport" name="transport" onchange="checktransport(this.value)" value="<%=transportmode%>" >
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i>
			</span>
	        <%}%>
	        </div>
	        </div>
<!-- 	    	</div> -->	
        </div>
        <div class="form-group">
        <label class="control-label col-form-label col-sm-2" for="tnw">Total Net Weight</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="tnw" class="form-control" value="<%=tnw%>" placeholder="Max 50 Characters" />
        </div>
        <label class="control-label col-form-label col-sm-2" for="carrierdetails">Carrier Details</label>
	        <div class="col-sm-4 ac-box">
	        <div class="input-group">
	        	        <%   if(EDIT=="") {%>
	         <input type="TEXT" size="30" maxlength="100" name="CARRIER" class="form-control" value="" placeholder="Max 100 Characters" >
	        <%}else{ %>
	         <input type="TEXT" size="30" maxlength="100" name="CARRIER" class="form-control" value="<%=CARRIER%>" placeholder="Max 100 Characters" >
	        <%}%>
	        </div>
	        </div>
        </div>
        
        <div class="form-group">
        <label class="control-label col-form-label col-sm-2" for="tgw">Total Gross Weight</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="tgw" class="form-control" value="<%=tgw%>" placeholder="Max 50 Characters" />
        </div>
        <label class="control-label col-form-label col-sm-2" for="durationofjourney">Duration of Journey</label>
	        <div class="col-sm-4 ac-box">
	        <div class="input-group">
	        	        <%   if(EDIT=="") {%>
	         <input type="TEXT" size="30" maxlength="20" name="JOURNEY" class="form-control" value="" placeholder="Max 20 Characters" >
	        <%}else{ %>
	         <input type="TEXT" size="30" maxlength="20" name="JOURNEY" class="form-control" value="<%=JOURNEY%>" placeholder="Max 20 Characters" >
	        <%}%>
	        </div>
	        </div>
        </div>
       
        <div class="form-group">
        <label class="control-label col-form-label col-sm-2" for="netpacking">Total Packing</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="100"	name="netpacking" class="form-control" value="<%=netpacking%>" placeholder="Max 100 Characters" />
        </div>
        <div class="form-inline">
	        <label class="control-label col-form-label col-sm-2" for="trackingno">Tracking No</label>
	        <div class="col-sm-4">
	        <div class="input-group">
	        	        <%   if(EDIT=="") {%>
	        <INPUT class="form-control" name="TRACKINGNO" id="TRACKINGNO" type="TEXT"  value=""  style="width: 100%" MAXLENGTH=100 placeholder="Max 100 characters">
	        <%}else{ %>
	        <INPUT class="form-control" name="TRACKINGNO" id="TRACKINGNO" type="TEXT"  value="<%=TRACKINGNO%>"  style="width: 100%" MAXLENGTH=100 placeholder="Max 100 characters">
	        <%}%>
	        </div>
	        </div>
	    	</div>	        
        </div>
        <div class="form-group">
        <label class="control-label col-form-label col-sm-2" for="netdimension">Total Dimension</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="100"	name="netdimension" class="form-control" value="<%=netdimension%>" placeholder="Max 100 Characters" />
        </div>
        <label class="control-label col-form-label col-sm-2" for="Status">Status</label>
	        <div class="col-sm-4 ac-box">
	        <div class="input-group">
	        <%   if(EDIT=="") {%>
	        	<input type="text" name="status" id="status" class="ac-selected form-control" placeholder="STATUS" onchange="getStatus()">
					  <span class="select-icon" onclick="$(this).parent().find('input[name=\'status\']').focus()">
					  <i class="glyphicon glyphicon-menu-down"></i> </span>
<!-- 					  <select class="form-control" data-toggle="dropdown" data-placement="right" name="status" id="status" onchange="getStatus()">
						  <option value="Shipped" selected>Shipped</option>
  	  					  <option value="Intransit">Intransit</option>
  	  					  <option value="Delivery">Delivery</option>
		      		  </select>  -->
	        <%}else{ %>
		     	<input type="text" name="status" id="status" class="ac-selected form-control" placeholder="STATUS" value="<%=sSTATUS%>" onchange="getStatus()">
					  <span class="select-icon" onclick="$(this).parent().find('input[name=\'status\']').focus()">
					  <i class="glyphicon glyphicon-menu-down"></i> </span> 
<%-- 					  <select class="form-control" data-toggle="dropdown" data-placement="right" name="status" id="status" onchange="getStatus()">
						  	<option value="<%=sSTATUS%>" selected><%=sSTATUS%></option>
						  	<option value="Shipped" selected>Shipped</option>
  	  						<option value="Intransit">Intransit</option>
  	  						<option value="Delivery">Delivery</option>
      				  </select>  --%>
	        <%}%>
	        </div>
	        </div>
        	        
        </div>
        <div class="form-group">
        <label class="control-label col-form-label col-sm-2" for="Remarks">Notes</label>
        <div class="col-sm-4">        
        <textarea  class="form-control" name="dnplremarks" placeholder="Max 1000 Characters"  MAXLENGTH=1000><%=dnplremarks%></textarea>
        </div>
	    <div class="form-inline">
	        <label class="control-label col-form-label col-sm-2" for="TRN/RCB/Tax No" id="TRANDATES"></label>
	        <div class="col-sm-4">
	        <div class="input-group">
	        <%if(EDIT=="") {%>
	        	<INPUT class="form-control datepicker" name="TRANDATE" id="TRANDATE" value="<%=ISSUEDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
	        <%}else{ %>
	        	<INPUT class="form-control datepicker" name="TRANDATE" id="TRANDATE" value="<%=STATUSDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
	        <%}%>
	        </div>
	        </div>
	    	</div>		        
        </div>
        <div class="form-group" style="display:none">
        <label class="control-label col-form-label col-sm-2" for="dummy"></label>
        <div class="col-sm-4">        
        </div>
        <!--         	        <div class="form-inline"> -->
	        <label class="control-label col-form-label col-sm-2" for="clearingagent">Clearing Agent</label>
	        <div class="col-sm-4 ac-box">
	        <div class="input-group">
	        <input type="hidden" id="typeofclearance" name="typeofclearance" value="" />   
	        	        <%   if(EDIT=="") {%>
	        <input type="text" class="ac-selected  form-control typeahead clearingagent" id="clearingagent" placeholder="Select a clearing agent" name="clearingagent" onchange="checkclearingagent(this.value)" value="">
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'clearingagent\']').focus()">
			<i class="glyphicon glyphicon-menu-down"  style="font-size: 8px;"></i>
			</span>
	        <%}else{ %>
	        <input type="text" class="ac-selected  form-control typeahead clearingagent" id="clearingagent" placeholder="Select a clearing agent" name="clearingagent" onchange="checkclearingagent(this.value)" value="<%=CLEARAGENT%>">
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'clearingagent\']').focus()">
			<i class="glyphicon glyphicon-menu-down"  style="font-size: 8px;"></i>
			</span>
	        <%}%> 
	        </div>
	        </div>
<!-- 	    	</div> -->
        </div>

        <div class="form-group" style="display:none">
        <label class="control-label col-form-label col-sm-2" for="dummy"></label>
        <div class="col-sm-4">        
        </div>
        <div class="form-inline">
	        <label class="control-label col-form-label col-sm-2" for="contactname">Contact Name</label>
	        <div class="col-sm-4 ac-box">
	        <div class="input-group">
	        	        <%   if(EDIT=="") {%>
	        <INPUT class="form-control" name="CONTACT_NAME" id="CONTACT_NAME" value="" type="TEXT" readonly="readonly">
	        <%}else{ %>
	        <INPUT class="form-control" name="CONTACT_NAME" id="CONTACT_NAME" value="<%=CONTACTNAME%>" type="TEXT" readonly="readonly">
	        <%}%>
	        </div>
	        </div>
	    	</div>
        </div>
        
  		<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" sNewEnb >Clear</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-success" onClick="onUpdate(document.form)">Save</button>&nbsp;&nbsp;
      
      	</div>
        </div>
        
  		</form>
		</div>
 
<script>
$(document).ready(function(){
	if(form.INVOICE.value!='')
		form.INVOICENO.value=form.INVOICE.value;
	loaddata();
	if(form.status.value==''){
		form.status.value = "Shipped";
		}
		getStatus();
});



	var sts = '<%=sSTATUS%>';
	if(sts=="Intransit"){
var postatus =   [
		{
		    "year": "Intransit",
		    "value": "Intransit",
		    "tokens": [
		      "Intransit"
		    ]
		  },
		  {
			    "year": "Delivered",
			    "value": "Delivered",
			    "tokens": [
			      "Delivered"
			    ]
			  }];

		}else{
var postatus =   [

			{
			    "year": "Shipped",
			    "value": "Shipped",
			    "tokens": [
			      "Shipped"
			    ]
			  },
				  {
					    "year": "Intransit",
					    "value": "Intransit",
					    "tokens": [
					      "Intransit"
					    ]
					  },
					  {
						    "year": "Delivered",
						    "value": "Delivered",
						    "tokens": [
						      "Delivered"
						    ]
						  }];
			}
	
	
			  
			  
var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    // an array that will be populated with substring matches
	    matches = [];
	    // regex used to determine if a string contains the substring `q`
	    substrRegex = new RegExp(q, 'i');
	    // iterate through the pool of strings and for any string that
	    // contains the substring `q`, add it to the `matches` array
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};

const input = document.getElementById('status');

input.addEventListener('keypress', function(event) {
  event.preventDefault();
});

function viewOutboundOrders(){
	
	if (document.form.GINO.value == ''){
		if (document.form.INVOICENO.value == '')
			{
	    alert ("Enter GINO");
	    document.getElementById("GINO").focus();
	    return false;
			}
 	}
	
	document.form.action="/track/OrderIssuingServlet?action=View";
    document.form.submit();
}
	
function onNew(){
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		ACTION : "PAKINGLIST_DELIVERYNOTE"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.PLNO;
				var resultV2 = resultVal.DNNO;
				document.form.plno.value= resultV;
				document.form.dnno.value= resultV2;
	
			} else {
				alert("Unable to genarate Packing List/Deliver Note");
				document.form.plno.value = "";
				document.form.dnno.value = "";
			}
		}
	});
}

function loaddata()
{
	
	/* Customer Auto Suggestion */
	$('#CUSTOMER').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  //name: 'states',
		  display: 'CNAME',  
		  async: true,   
		  //source: substringMatcher(states),
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/MasterServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : "<%=plant%>",
						ACTION : "GET_CUSTOMER_DATA",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CUSTMST);
					}
		   });
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
// 		    	return '<p onclick="getcustname(\''+data.TAXTREATMENT+'\',\''+data.CUSTNO+'\')">' + data.CNAME + '</p>';
		    	return '<div onclick="getcustname(\''+data.TAXTREATMENT+'\',\''+data.CUSTNO+'\')"><p class="item-suggestion">Name: ' + data.CNAME + '</p><br/><p class="item-suggestion">Contact Name: ' + data.NAME + '</p><p class="item-suggestion pull-right">Customer Type: ' + data.CUSTOMER_TYPE_ID + '</p></div>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.CUST_CODE.value = "";
			}
			/* To reset Order number Autosuggestion*/
			$("#DONO").typeahead('val', '"');
			$("#DONO").typeahead('val', '');
			
			$("#INVOICENO").typeahead('val', '"');
			$("#INVOICENO").typeahead('val', '');
			
			$("#GINO").typeahead('val', '"');
			$("#GINO").typeahead('val', '');
		});

	
	/* Order Number Auto Suggestion */
	$('#DONO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'DONO',  
		  source: function (query, process,asyncProcess) {
			  var urlStr = "/track/InvoiceServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : "<%=plant%>",
						Submit : "GET_ORDER_NO_FOR_AUTO_SUGGESTION_DNPL",
						TYPE : "DNPL",
						CNAME : document.form.CUSTOMER.value, 
						DONO : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.orders);
					}
		   });
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<p onclick="getordercustname(\''+data.CUSTNAME+'\',\''+data.CUSTCODE+'\')">' + data.DONO + '</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			
			$("#INVOICENO").typeahead('val', '"');
			$("#INVOICENO").typeahead('val', '');
			
			$("#GINO").typeahead('val', '"');
			$("#GINO").typeahead('val', '');
		});
	
	
	$('#GINO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'GINO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvoiceServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : "<%=plant%>",
				Submit : "GET_GI_NO_FOR_AUTO_SUGGESTION_DNPL",
				CUST_CODE : document.form.CUST_CODE.value,
				ORDERNO : document.form.DONO.value,
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.gino);
			}
		   });
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<p onclick="getginocustname(\''+data.CUSTNAME+'\',\''+data.CUSTCODE+'\',\''+data.DONO+'\')">' + data.GINO + '</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			
			$("#INVOICENO").typeahead('val', '"');
			$("#INVOICENO").typeahead('val', '');
		});
	
	
	
	/* Invoice No Auto Suggestion */
	$('#INVOICENO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'INVOICE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/InvoiceServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : "<%=plant%>",
				Submit : "GET_INVOICE_NO_FOR_AUTO_SUGGESTION_DNPL",
				CNAME : document.form.CUSTOMER.value,
				ORDERNO : document.form.DONO.value,
				GINO : document.form.GINO.value,
				INVOICE : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.InvoiceDetails);
			}
		   });
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<p onclick="getinvocustname(\''+data.CUSTNAME+'\',\''+data.CUSTNO+'\',\''+data.DONO+'\',\''+data.GINO+'\')">' + data.INVOICE + '</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});

	$("#status").typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  name: 'postatus',
		  display: 'value',  
		  source: substringMatcher(postatus),
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
			return '<p>' + data.value + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			  
		}).on('typeahead:open',function(event,selection){
			
		}).on('typeahead:close',function(){
			
		}).on('typeahead:change',function(event,selection){
				var  d = $(this).val();
			     document.getElementById('TRANDATES').innerHTML = d + " Date";
	});

	$('.clearingagent').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'clearing_agent_id',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : '<%=plant%>',
				ACTION : "GET_TRANSPORT_CLEARING_AGENT",
				TRANSPORTID : $("input[name=TRANSPORTID]").val(),
				CLEARAGENTID : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.DESIGNATIONLIST);
			}
				});
		},
		  limit: 9999,
		  templates: {
		  empty: [
			  '<div style="padding:3px 20px">',
				'No results found',
			  '</div>',
			].join('\n'),
			suggestion: function(data) {
				return '<div onclick="document.form.CONTACT_NAME.value = \''+data.CONTACTNAME+'\'"><p class="item-suggestion">'+ data.clearing_agent_id + '</p><p class="item-suggestion pull-right">' + data.TRANSPORT + '</p><br/><p class="item-suggestion">' + data.clearing_agent_name + '</p><p class="item-suggestion pull-right">' + data.CONTACTNAME + '</p></div>';
	}
	}
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();
			}
			menuElement.after( '<div class="clearingagentAddBtn footer"  data-toggle="modal" data-target="#clearingagentModal"><a href="#"> + New Clearaing Agent </a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide(); 
		}).on('typeahead:open',function(event,selection){
			$('.clearingagentAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.clearingagentAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == ""){
				document.form.CONTACT_NAME.value = "";
			}
	});

	//transport
	$('#transport').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TRANSPORT_MODE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
						ACTION : "GET_TRANSPORT_LIST",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.TRANSPORTMODE);
					}
				});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<p>' + data.TRANSPORT_MODE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="transportAddBtn footer"  data-toggle="modal" data-target="#transportModal"><a href="#"> + New Transport </a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.transportAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.transportAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			$("input[name=TRANSPORTID]").val(selection.ID);
			$("#clearingagent").typeahead('val', '"');
			$("#clearingagent").typeahead('val', '');
			document.form.CONTACT_NAME.value = "";
		}).on('typeahead:change',function(event,selection){
 			if($(this).val() == ""){
			$("input[name=TRANSPORTID]").val('');
			$("#clearingagent").typeahead('val', '"');
			$("#clearingagent").typeahead('val', '');
			document.form.CONTACT_NAME.value = "";
 			}	
		});

	$('#FREIGHTFORWARDERNO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'FREIGHT_FORWARDERNAME',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : '<%=plant%>',
				ACTION : "GET_FREIGHT_FORWARDER_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.FREIGHT_FORWARDERMST);
			}
			});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<div onclick="document.form.FREIGHT_FORWARDERID.value = \''+data.FREIGHT_FORWARDERNO+'\'"><p class="item-suggestion"> ' + data.FREIGHT_FORWARDERNAME + '</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();
			}
// 			menuElement.after( '<div class="freightforwardernameAddBtn footer"  data-toggle="modal" data-target="#freightforwardernameModal"><a href="#"> + New Freight Forwarder</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();   
		}).on('typeahead:open',function(event,selection){
			$('.freightforwardernameAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.freightforwardernameAddBtn').hide();}, 180);
			if($(this).val() == ""){
				document.form.FREIGHT_FORWARDERID.value = "";
				}
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
}

function getStatus() {
	var  d = document.getElementById("status").value;
	  	if(d==""){
		   document.getElementById('TRANDATES').innerHTML = "Shipped Date";
		}else{
		   document.getElementById('TRANDATES').innerHTML = d + " Date";
		}
	  	<%   if(EDIT!="") {%>
		var sts = '<%=sSTATUS%>';
	  	if(sts==""){
			document.getElementById('TRANDATES').innerHTML = "Shipped Date";
		}else if(sts!=""){
		   	document.getElementById('TRANDATES').innerHTML = sts + " Date";
		}
	  	<% }%>

	}
	
function checktransport(transport){	
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			TRANSPORT_MODE : transport,
			ACTION : "TRANSPORT_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Transport Does't Exists");
					document.getElementById("transport").focus();
					$("#transport").typeahead('val', '');
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}

function checkclearingagent(clearingagent){
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			CLEARINGAGENT : clearingagent,
			ACTION : "CLEARING_AGENT_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Clearing Agent Does't Exists");
					document.getElementById("clearingagent").focus();
					$("#clearingagent").typeahead('val', '');
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}	

function getordercustname(CUSTNAME,CUSTCODE){
	document.form.CUSTOMER.value = CUSTNAME;
	document.form.CUST_CODE.value = CUSTCODE;
	}
	
function getginocustname(CUSTNAME,CUSTCODE,DONO){
	document.form.DONO.value = DONO;
	document.form.CUSTOMER.value = CUSTNAME;
	document.form.CUST_CODE.value = CUSTCODE;
	}
	
function getinvocustname(CUSTNAME,CUSTCODE,DONO,GINO){
	document.form.GINO.value = GINO;
	document.form.DONO.value = DONO;
	document.form.CUSTOMER.value = CUSTNAME;
	document.form.CUST_CODE.value = CUSTCODE;
	}

function getcustname(TAXTREATMENT,CUSTNO){
	document.form.CUST_CODE.value = CUSTNO;
	}

function setCustomerData(customerData){
	$("input[name ='CUST_CODE']").val(customerData.custno);
	$("#CUSTOMER").typeahead('val', customerData.custname);	
	/* To reset Order number Autosuggestion*/
	$("#DONO").typeahead('val', '"');
	$("#DONO").typeahead('val', '');
	
	$("#INVOICENO").typeahead('val', '"');
	$("#INVOICENO").typeahead('val', '');
	
	$("#GINO").typeahead('val', '"');
	$("#GINO").typeahead('val', '');
}

function transportCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
	}
}

function clearingAgentCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#clearingagent").typeahead('val', data.CLEARING_AGENT_ID);
		$("input[name=clearingagent]").val(data.CLEARING_AGENT_ID);
	}	
}

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}

</script>

</div>
</div>

<%@include file="../jsp/newClearingAgentModal.jsp"%>
<jsp:include page="newTransportModeModal.jsp" flush="true"></jsp:include>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
