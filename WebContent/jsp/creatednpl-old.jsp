<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.StrUtils"%>
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
    <jsp:param name="mainmenu" value="<%=IConstants.SALES_TRANSACTION%>"/>
</jsp:include>
<%--New page design end --%>

<script src="js/json2.js"></script>
<script src="js/general.js"></script>
<script src="js/calendar.js"></script>
 
 <script>

	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'DNPL', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}

	function popWinBatch(URL) {
		var locId = document.getElementById("LOC_0").value;
		if(locId=="" || locId.length==0 ) {
			alert("Enter Location!");
			document.getElementById("LOC_0").focus();
			return false;
		}
		else{
	 		subWin = window.open(URL, 'Outboundpickissuebyprodid', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
			}
	}
	
	
	/*Need to hode the below  */
 function onUpdate(form){
 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Create Delivery Note/Packing List");
   	 	return false;
    }
 	
  	
    
    var checkFound = true;  
	 var orderLNo;
	 var len = $('#itemstab tbody tr').lenth;
	 /*var len = document.form.chkdDoNo.length; 
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
	    	//if(!verifyIssuingQty(orderLNo))	
		    	//  return false;
	     }
	
	     else {
		     if(document.form.chkdDoNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = document.form.chkdDoNo[i].value;
		    	// if(!verifyIssuingQty(orderLNo))	
			    	//  return false;
		     }
	     }
          		
        	     
    }
	 checkFound = true;*/
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 if (document.form.dnno.value == ''){
		    alert ("Please provide delivery note number.");
		    return false;		 
	 }
	 if (document.form.plno.value == ''){
		    alert ("Please provide packing list number.");
		    return false;		 
	 }
	 if (document.form.tnw.value == ''){
		    alert ("Please provide Total Net Weight.");
		    return false;		 
	 }
	 if (document.form.tgw.value == ''){
		    alert ("Please provide Total Gross Weight.");
		    return false;		 
	 }
	 for (var i = 0; i < len ; i++)
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
	    }
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
               		 orderLNo = document.form.chkdDoNo.value;
               	}
               	else{
               		document.form.chkdDoNo[i].checked = isChk;
               		 orderLNo = document.form.chkdDoNo[i].value;
               	}
             	setIssuingQty(orderLNo,i);       	
                 
         }
     }
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
               		 orderLNo = document.form.chkdDoNo.value;
               	}
               	else{
               		document.form.chkdDoNo[i].checked = isChk;
               		 orderLNo = document.form.chkdDoNo[i].value;
               	}
             	//setIssuingQty(orderLNo,i);       	
                 
         }
     }
 }
 
 /*function checkAll(isChk)
 {
 	var len = document.form.chkitem.length;
 	 var orderLNo; 
 	 if(len == undefined) len = 1;  
     if (document.form.chkitem)
     {
         for (var i = 0; i < len ; i++)
         {      
               	if(len == 1){
               		document.form.chkitem.checked = isChk;
               		 
               	}
               	else{
               		document.form.chkitem[i].checked = isChk;
               		 
               	}
             	   	
                 
         }
     }
 }*/

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

</script>


<jsp:useBean id="su"  class="com.track.util.StrUtils" />

<%
   String plant=(String)session.getAttribute("PLANT");
   Map checkedDOS = (Map) request.getSession().getAttribute("checkedDOS");
   String DONO     = StrUtils.fString(request.getParameter("DONO")); 
   String INVOICENO     = StrUtils.fString(request.getParameter("INVOICENO"));
   String EDIT = StrUtils.fString(request.getParameter("EDIT")).trim();
   System.out.println("EDIT "+ INVOICENO+ ", " +EDIT);
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
   
   
   _DOUtil.setmLogger(mLogger);
   _POSUtil.setmLogger(mLogger);
   _ItemMstDAO.setmLogger(mLogger);
   
   
   String vend = "",deldate="",custCode="",personIncharge = "",cmd="",contactNum = "";
   String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
   String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",loc = "",REF = "",ISSUEDATE="",CashCust="";
   String sSaveEnb    = "disabled",fullIssue = "",allChecked = "";;
   CashCust = StrUtils.fString(request.getParameter("CashCust"));
   ISSUEDATE     = StrUtils.fString(request.getParameter("ISSUEDATE"));
   String curDate =_dateUtils.getDate();
   if(ISSUEDATE.length()<0|ISSUEDATE==null||ISSUEDATE.equalsIgnoreCase(""))ISSUEDATE=curDate;
   Vector poslist=null;
	 ArrayList prdlist=null;
   String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
  
   if(action.equalsIgnoreCase("View")){
      Map m=(Map)request.getSession().getAttribute("podetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULT");
      fieldDesc = "<font class = "+IConstants.SUCCESS_COLOR+">" + fieldDesc + "</font>";
      
      if(m != null && m.size()>0){
       JobNum=(String)m.get("JobNum");
       custName=(String)m.get("custName");
	   custCode=(String)m.get("CustCode");
          }
      else 
      {
        fieldDesc="Details not found for Order:"+ DONO;  
      }
    }
   ArrayList al= new ArrayList();
	  
  	if(DONO!="" && EDIT!="")
	  {	  
	  
		al= _PackingUtil.dnpl(plant,DONO, INVOICENO);
	  
	  }
  	  
   
     if(result.equalsIgnoreCase("catchrerror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
       // fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
        fieldDesc = "<font class = "+IConstants.FAILED_COLOR+">" + fieldDesc + "</font>";
        allChecked = StrUtils.fString(request.getParameter("allChecked"));
        fullIssue = StrUtils.fString(request.getParameter("fullReceive"));
     }
 %>
 <% if(EDIT.equals("EDIT")) { System.out.println("EDIT "+EDIT);%>
<script type="text/javascript">
document.addEventListener("DOMContentLoaded", function() {
document.getElementById("back").style.display = "none";
});
</script>
<%}else{ System.out.println("EDIT 0");%>
<script type="text/javascript">
document.addEventListener("DOMContentLoaded", function() {
document.getElementById("editback").style.display = "none";
document.getElementById("deleteall").style.display = "none";
});
</script>
<%}%>
 <%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 <CENTER><strong><font style="font-size:18px;"><%=fieldDesc%></font></strong></CENTER>

<form class="form-horizontal" name="form" method="post" action="/track/OrderIssuingServlet?">

   		
       <div class="form-group">
       <div class="row">
       <label class="control-label col-sm-4" for="outbound Order">
       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Invoice Number:</label>
       <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="INVOICENO" value="<%=INVOICENO%>" 
    		onkeypress="if((event.keyCode=='13') && ( document.form.DONO.value.length > 0)){loadOutboundOrderDetails();}"/>
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('invoice_list.jsp?DONO='+form.DONO.value);"> -->
   		 	<span class="input-group-addon"  onClick="itempopUpwin();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Invoice Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="col-sm-4">
  		<lable class="checkbox-inline"><INPUT  Type=Checkbox  style="border:0;" name = "CashCust" id="CashCust" >
                     <b>Search By Tax Invoice</b></lable> 
  		</div>
       </div>
        <div class="row" style="padding:6px">
       <label class="control-label col-sm-4" for="outbound Order">
       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="DONO" value="<%=DONO%>" readonly />
    		<!-- onkeypress="if((event.keyCode=='13') && ( document.form.DONO.value.length > 0)){popUpWin('invoice_list.jsp?DONO='+form.DONO.value);}" -->    		
   		 	<!-- <span class="input-group-addon"  onClick="itempopUpwin();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		 <INPUT type = "hidden"  name="EDIT" value="<%=EDIT%>">
  		</div>
       
  		<div class="form-inline">
  		<div class="col-sm-1 ">   
  		<button type="button" class="Submit btn btn-default" onClick="viewOutboundOrders();"><b>View</b></button>
  		</div>
  		</div> 
 		</div>
 		<div class="row" style="padding:6px">
  		<label class="control-label col-sm-4" for="customer name">Customer Name:</label>
        <div class="col-sm-4">
        <INPUT name="CUST_NAME"   class="form-control" MAXLENGTH="20" readonly type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
    	</div>
 		</div> 
 		<div class="row" style="padding:6px">
  		<label class="control-label col-sm-4" for="Reference no">Reference No:</label>
        <div class="col-sm-4">
        <INPUT name="JOB_NUM"   class="form-control" MAXLENGTH="20" readonly value="<%=JobNum%>" size="30"  MAXLENGTH=80>
    	</div>
 		</div> 
 		</div> 
 		
 					<INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
                    <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
                    
                    
       			  		
  		 <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%" class="table" id="itemstab" >
         <thead style="background: #eaeafa; font-size: 15px">
         <tr>
         <th width="4%"">Sr. No.</th>
         <th width="4%">Order Line No</th>
         <th width="15%">Product ID</th>
         <th width="15%">Description</th>
         <th width="5%">UOM</th>
         <th width="5%">HSCODE</th>
         <th width="5%">COO</th>
          <th width="4%">Qty</th>
         <th width="5%">Netweight</th>
         <th width="5%">Grossweight</th>
         <th width="10%">Packing</th>
          <th width="10%">Dimension</th>
    
        
         </tr>
       	 </thead>
       	 <tbody style="font-size: 15px;">
       	  <%
       	  
       	  if(DONO!="" && EDIT=="")
       	  {
       	  if(DONO.substring(0, 1).equalsIgnoreCase("T"))
       	  {
       		al= _POSUtil.dnpl(plant,DONO, INVOICENO);
       	  }
       	  else
       	  {
            al= _DOUtil.dnpl(plant,DONO, INVOICENO);
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
          
           if(checkedDOS!=null && result.equalsIgnoreCase("catchrerror")){
              value = (String)checkedDOS.get(dolnno);   
              
              if(value!=null)
              {
            	  issuingQty  = value.split(":")[0];
            	  batch  = value.split(":")[1];
            	             	  
            	 
              }
              } 
       
       %>
       
      		
        <TR bgcolor = "<%=bgcolor%>">
               <TD align="center" width="4" ><INPUT Type="hidden"  style="border:0;background=#dddddd" 
              name="chkdDoNo"  value="<%=dolnno%>" <%if(value!=null){%>checked <%}%> ><%=iIndex%>
               <input type="hidden" class="form-control" size="20" name="SRNO_<%=dolnno %>" id="SRNO_<%=dolnno %>" value="<%=iIndex%>" />  
               </TD><TD align="center" width="4">
              <%=(String)m.get("dolnno")%></TD>
              <TD align="left" width="15%"><%=(String)m.get("item")%></TD>
              <TD align="left" width="15%"><%=(String)desc%></TD>
              <TD align="left" width="5%"><%=(String)m.get("uom")%></TD>
             <TD align="left" width="5%"><%=(String)m.get("hscode")%></TD>
             <TD align="left" width="5%"><%=(String)m.get("coo")%></TD>
            <TD align="center" id = "QtyIssued_<%=dolnno%>" width="4%"><%=StrUtils.formatNum((String)m.get("qtyis"))%></TD>
             <TD align="center" width="5%"><INPUT class="form-control" name="netweight_<%=dolnno%>" id="netweight_<%=dolnno%>" value = "<%=StrUtils.formatNum((String)m.get("netweight"))%>" type="TEXT" size="15"  /></TD>
             <TD align="center" width="5%"><INPUT class="form-control" name="grossweight_<%=dolnno%>" id="grossweight_<%=dolnno%>" value = "<%=StrUtils.formatNum((String)m.get("grossweight"))%>" type="TEXT" size="15"  /></TD>
             <TD align="center" width="10%"><select class="form-control" data-toggle="dropdown" data-placement="left" name="packing" style="width: 100%" id="packing"> 
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
             <TD align="center" width="10%"><INPUT class="form-control"  name="dimension_<%=dolnno%>" id="dimension_<%=dolnno%>" value = "<%=(String)m.get("dimension")%>" type="TEXT" size="5" /></TD>
                                    </TR>
           
       <%}} else {%>
       
             <TR> <TD align="center" colspan="9"> Data's Not Found</TD></TR>
       <%}%>
         </tbody>
       </TABLE>
       
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="7">
        <INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
       
        <div class="form-group">
        <label class="control-label col-sm-4" for="plno">Packing List # :</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="plno" class="form-control" value="<%=plno%>" />
        </div>
        </div>
        <div class="form-group">
        <label class="control-label col-sm-4" for="dnno">Delivery Note # :</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="dnno" class="form-control" value="<%=dnno%>" />
        </div>
        </div>
        <div class="form-group">
        <label class="control-label col-sm-4" for="tnw">Total Net Weight :</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="tnw" class="form-control" value="<%=tnw%>" />
        </div>
        </div>
        
        <div class="form-group">
        <label class="control-label col-sm-4" for="tgw">Total Gross Weight :</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="tgw" class="form-control" value="<%=tgw%>" />
        </div>
        </div>
       
        <div class="form-group">
        <label class="control-label col-sm-4" for="netpacking">Total Packing :</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="netpacking" class="form-control" value="<%=netpacking%>" />
        </div>
        </div>
        <div class="form-group">
        <label class="control-label col-sm-4" for="netdimension">Total Dimension :</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="netdimension" class="form-control" value="<%=netdimension%>" />
        </div>
        </div>
        <div class="form-group">
        <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="dnplremarks" class="form-control" value="<%=dnplremarks%>" />
        </div>
        </div>
        
  		<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" id="back" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" id="editback" class="Submit btn btn-default" onClick="window.location.href='editpl.jsp'"><b>Back</b></button>&nbsp;&nbsp;
       	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onUpdate(document.form)"><b>Submit</b></button>&nbsp;&nbsp;
      
      	</div>
        </div>
        
  		</form>
		</div>
 
<script>
function itempopUpwin(){
	if(document.getElementById("CashCust").checked)
		//popUpWin('do_list_do.jsp?DONO='+form.DONO.value+'&ORDERTYPE=DNPL&TAXINVOICE=1');
		popUpWin('invoice_list.jsp?INVOICENO='+form.INVOICENO.value+'&ORDERTYPE=DNPL&TAXINVOICE=1');
	else
		//popUpWin('do_list_do.jsp?DONO='+form.DONO.value+'&ORDERTYPE=DNPL&TAXINVOICE=0');
		popUpWin('invoice_list.jsp?INVOICENO='+form.INVOICENO.value+'&ORDERTYPE=DNPL&TAXINVOICE=0');
}

function viewOutboundOrders(){
	document.form.action="/track/OrderIssuingServlet?action=View";
    document.form.submit();
}



function loadOutboundOrderDetails() {
	var outboundOrderNo = document.form.DONO.value;
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : outboundOrderNo,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_OUTBOUND_ORDER_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							
							document.form.CUST_NAME.value = resultVal.CUSTNAME;
							document.form.JOB_NUM.value=resultVal.JOBNUM;
							document.form.action = "/track/OrderIssuingServlet?action=View";
							document.form.submit();

						} else {
							alert("Not a valid Order Number!");
							document.form.DONO.value = "";
							document.form.CUST_NAME.value = "";
							document.form.DONO.focus();
						}
					}
				});
	}
	
	
function validateLocation(locId, index) {
	var isValid;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC_"+index).focus();
		return false;
	}else{
		var urlStr = "/track/InboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			async : false,
			data : {
				LOC : locId,
                USERID : "<%=sUserId%>",
				PLANT : "<%=plant%>",
				ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status != "100") {
                               
						alert("Not a valid Location");
						document.getElementById("LOC_"+index).focus();
						document.getElementById("LOC_"+index).value="";
						isValid = false;	
						
						
					} 
					else 
						isValid =  true;
				}
			});
		 return isValid;
		}
	}
</script>

</div>
</div>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
