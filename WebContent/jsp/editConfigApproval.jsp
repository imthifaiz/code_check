<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.tables.USER_INFO"%>
<%
String title = "Order Management Approval and Email Configuration";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
	 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}

	function onClear()
	{
		document.form1.adminmail.value="";
		document.form1.usermail.value="";		
        document.form1.isactive.checked = false;        
        
	}
	
	/* function onAdd(){
		 var PurchaseAdminmail = document.form1.PurchaseAdminmail.value;
		if (PurchaseAdminmail == "" || PurchaseAdminmail == null) {
			alert("Please Enter Purchase Admin Email Address");
			document.form1.PurchaseAdminmail.focus();
			return false;
		}
		var atpos = PurchaseAdminmail.indexOf("@");
		var dotpos = PurchaseAdminmail.lastIndexOf(".");
		if (atpos < 1 || dotpos < atpos + 2
				|| dotpos + 2 >= PurchaseAdminmail.length) {
			alert("Enter a valid Purchase Admin Email address");
			document.form1.PurchaseAdminmail.focus();
			return false;
		}
                
		 var PurchaseUsermail = document.form1.PurchaseUsermail.value;                
                
                    if (PurchaseUsermail == "" || PurchaseUsermail == null) {
                     alert("Please Enter Purchase User Email Address");
                     document.form1.PurchaseUsermail.focus();
                     return false;
                    }
                    
                    atpos = PurchaseUsermail.indexOf("@");
		    dotpos = PurchaseUsermail.lastIndexOf(".");
		if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= PurchaseUsermail.length) {
			alert("Enter a valid Purchase User Email address");
			document.form1.PurchaseUsermail.focus();
			return false;
		} 
               
		document.form1.cmd.value = "UPDATE_CONFIGAPPROVAL";
		document.form1.action = "editConfigApproval.jsp";
		document.form1.submit();
	} */
	
	
</script>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	EmailMsgUtil mailUtil = new EmailMsgUtil();
	String res = "";
	Vector userlist=null,purcuserlist=null;
	String action = "";
	String PurchaseAdminmail  = "", PurchaseUsermail  = "", PurchaseIsActive  = "",PurchaseAdminUser="",PurchaseUser="",PurchaseCreateEmail="",PurchaseCreateAtt="",
			PurchaseCreateAttTo="",PurchaseApproveEmail="",PurchaseRejectEmail="",PurchaseisAutoEmail="",
			SalesAdminmail  = "", SalesUsermail  = "", SalesIsActive  = "",SalesAdminUser  = "", SalesUser  = "",SalesCreateEmail="",SalesCreateAtt="",
					SalesCreateAttTo="",SalesApproveEmail="",SalesRejectEmail="",SalesisAutoEmail="",
					EstimateAdminmail  = "", EstimateUsermail  = "", EstimateIsActive  = "",EstimateAdminUser  = "", EstimateUser  = "",EstimateCreateEmail="",EstimateCreateAtt="",
					EstimateCreateAttTo="",EstimateApproveEmail="",EstimateRejectEmail="",EstimateisAutoEmail="",
					RentalAdminmail  = "", RentalUsermail  = "", RentalIsActive  = "",RentalAdminUser  = "", RentalUser  = "",RentalCreateEmail="",RentalCreateAtt="",
					RentalCreateAttTo="",RentalApproveEmail="",RentalRejectEmail="",RentalisAutoEmail="";
	
	StrUtils strUtils = new StrUtils();
    res =  strUtils.fString(request.getParameter("result"));
    action = StrUtils.fString(request.getParameter("cmd"));
    System.out.println(action);
    session.putValue("recvuserlist", null);
    session.putValue("purcuserlist", null);
    userlist = (Vector)session.getValue("recvuserlist");
    purcuserlist = (Vector)session.getValue("purcuserlist");    
    if (action.equalsIgnoreCase("Clear")) {
		action = "";
		PurchaseAdminmail="";
		PurchaseUsermail = "";		
		PurchaseIsActive  = "";
		SalesAdminmail  = "";
		SalesUsermail  = "";
		SalesIsActive  = "";
	}
    if (action.equalsIgnoreCase("UPDATE_CONFIGAPPROVAL")) {
		try{
			System.out.println("OP"+userlist.size());    
			PurchaseAdminmail=StrUtils.fString(request.getParameter("PurchaseAdminmail"));
			PurchaseUsermail=StrUtils.fString(request.getParameter("PurchaseUsermail"));			
			PurchaseIsActive =(request.getParameter("PurchaseIsActive").equals("N"))? "N":"Y";
			
			SalesAdminmail=StrUtils.fString(request.getParameter("SalesAdminmail"));
			SalesUsermail=StrUtils.fString(request.getParameter("SalesUsermail"));			
			SalesIsActive =(request.getParameter("SalesIsActive").equals("N"))? "N":"Y";
			
			EstimateAdminmail=StrUtils.fString(request.getParameter("EstimateAdminmail"));
			EstimateUsermail=StrUtils.fString(request.getParameter("EstimateUsermail"));			
			EstimateIsActive =(request.getParameter("EstimateIsActive").equals("N"))? "N":"Y";
			
			RentalAdminmail=StrUtils.fString(request.getParameter("RentalAdminmail"));
			RentalUsermail=StrUtils.fString(request.getParameter("RentalUsermail"));			
			RentalIsActive =(request.getParameter("RentalIsActive").equals("N"))? "N":"Y";
			
			boolean isUpdated=false;
			Hashtable<String,String> ht = new Hashtable<String,String>();
			if(userlist.size()>0)
			{
				for (int j = 0; j < userlist.size(); j++) {
					USER_INFO userdao = (USER_INFO)userlist.elementAt(j);
                ht = new Hashtable<String,String>();
                ht.put(IDBConstants.PLANT,plant);
                ht.put(IDBConstants.LOGIN_USER,sUserId);
                ht.put(IDBConstants.ORDERTYPE,"PURCHASE");
                ht.put(IDBConstants.USERTYPE,"ADMIN");
                ht.put(IDBConstants.USERID,userdao.getUSER_ID());
                ht.put(IDBConstants.EMAIL, userdao.getEMAIL());             
                if(!PurchaseIsActive.equalsIgnoreCase("Y"))PurchaseIsActive="N";
                ht.put(IDBConstants.ISACTIVE,PurchaseIsActive);               
                
                isUpdated =  mailUtil.updateConfigApproval(ht);
				}
			}
			
			if(purcuserlist.size()>0)
			{
				for (int j = 0; j < purcuserlist.size(); j++) {
					USER_INFO userdao = (USER_INFO)userlist.elementAt(j);
                ht = new Hashtable<String,String>();
                ht.put(IDBConstants.PLANT,plant);
                ht.put(IDBConstants.LOGIN_USER,sUserId);
                ht.put(IDBConstants.ORDERTYPE,"PURCHASE");
                ht.put(IDBConstants.USERTYPE,"OTHER");
                ht.put(IDBConstants.USERID,userdao.getUSER_ID());
                ht.put(IDBConstants.EMAIL, userdao.getEMAIL());             
                if(!PurchaseIsActive.equalsIgnoreCase("Y"))PurchaseIsActive="N";
                ht.put(IDBConstants.ISACTIVE,PurchaseIsActive);               
                
                isUpdated =  mailUtil.updateConfigApproval(ht);
				}
			}
                /* ht = new Hashtable<String,String>();
                ht.put(IDBConstants.PLANT,plant);
                ht.put(IDBConstants.LOGIN_USER,sUserId);
                ht.put(IDBConstants.ORDERTYPE,"SALES");
                ht.put(IDBConstants.ADMINEMAIL,SalesAdminmail);
                ht.put(IDBConstants.USEREMAIL,SalesUsermail);                
                if(!SalesIsActive.equalsIgnoreCase("Y"))SalesIsActive="N";
                ht.put(IDBConstants.ISACTIVE,SalesIsActive);               
                
                isUpdated =  mailUtil.updateConfigApproval(ht);
                
                ht = new Hashtable<String,String>();
                ht.put(IDBConstants.PLANT,plant);
                ht.put(IDBConstants.LOGIN_USER,sUserId);
                ht.put(IDBConstants.ORDERTYPE,"SALES ESTIMATE");
                ht.put(IDBConstants.ADMINEMAIL,EstimateAdminmail);
                ht.put(IDBConstants.USEREMAIL,EstimateUsermail);                
                if(!EstimateIsActive.equalsIgnoreCase("Y"))EstimateIsActive="N";
                ht.put(IDBConstants.ISACTIVE,EstimateIsActive);               
                
                isUpdated =  mailUtil.updateConfigApproval(ht);
                
                ht = new Hashtable<String,String>();
                ht.put(IDBConstants.PLANT,plant);
                ht.put(IDBConstants.LOGIN_USER,sUserId);
                ht.put(IDBConstants.ORDERTYPE,"TAX INVOICE");
                ht.put(IDBConstants.ADMINEMAIL,RentalAdminmail);
                ht.put(IDBConstants.USEREMAIL,RentalUsermail);                
                if(!RentalIsActive.equalsIgnoreCase("Y"))RentalIsActive="N";
                ht.put(IDBConstants.ISACTIVE,RentalIsActive);               
                
                isUpdated =  mailUtil.updateConfigApproval(ht); */
                
                if (!isUpdated) {
                        res = "<font class = " + IConstants.FAILED_COLOR+ ">Failed to edit the details</font>";
                } else {
                        res = "<font class = " + IConstants.SUCCESS_COLOR+ ">Config Approval edited successfully</font>";
                }
                }catch (Exception e){res = "<font class = " + IConstants.FAILED_COLOR+ ">"+e.getMessage()+"</font>";}
	}
    try{
    	
        /* Map m= mailUtil.getConfigApprovalDetails(plant,"PURCHASE","ADMIN");        
        if(!m.isEmpty()){
             
        	PurchaseAdminmail= (String) m.get("ADMINEMAIL");
        	PurchaseUsermail= (String) m.get("USEREMAIL");        	
        	PurchaseIsActive = (String) m.get("ISACTIVE");            
            
           }
        m= mailUtil.getConfigApprovalDetails(plant,"SALES","ADMIN");        
        if(!m.isEmpty()){
             
        	SalesAdminmail= (String) m.get("ADMINEMAIL");
        	SalesUsermail= (String) m.get("USEREMAIL");        	
        	SalesIsActive = (String) m.get("ISACTIVE");            
            
           }       
        m= mailUtil.getConfigApprovalDetails(plant,"SALES ESTIMATE","ADMIN");        
        if(!m.isEmpty()){
             
        	EstimateAdminmail= (String) m.get("ADMINEMAIL");
        	EstimateUsermail= (String) m.get("USEREMAIL");        	
        	EstimateIsActive = (String) m.get("ISACTIVE");            
            
           }
        m= mailUtil.getConfigApprovalDetails(plant,"RENTAL","ADMIN");        
        if(!m.isEmpty()){
             
        	RentalAdminmail= (String) m.get("ADMINEMAIL");
        	RentalUsermail= (String) m.get("USEREMAIL");        	
        	RentalIsActive = (String) m.get("ISACTIVE");            
            
           } */
        }catch(Exception e){
        res = "<font class = " + IConstants.FAILED_COLOR+ ">"+e.getMessage()+"</font>";
        }
    %>

    
    <div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post" action="editConfigApproval.jsp">
      <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8" style="text-align: right;">
     <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;       
      	<!-- <button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
      </div>
    </div>
        <INPUT type="hidden" name="cmd" value="" />
        <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Purchase Order</strong></div>
    <div class="panel-body">
                  <style>
  .toggle.ios, .toggle-on.ios, .toggle-off.ios { border-radius: 20px; }
  .toggle.ios .toggle-handle { border-radius: 20px; }
</style>    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Subject">Order Type:</label>
      <div class="col-sm-4">
      <div class="form-inline">      
      <INPUT name="PurchaseOrderType" readonly value="PURCHASE" class="form-control"> &nbsp;&nbsp;      

<!-- <div id="console-event" class="col-sm-4"></div> -->
<input type="hidden" name="PurchaseIsActive" value="<%=PurchaseIsActive%>" id="PurchaseIsActive">
<input type="checkbox" id="Purchaseradio-one" <%if(PurchaseIsActive.equalsIgnoreCase("Y")) {%> checked <%}%> data-toggle="toggle" data-style="ios" data-on="On" data-off="Off"/>
      </div>
      </div>
    
    <div class="form-inline">
  <div class="col-sm-4">    
    	<%-- <div class="switch-field">
		<input type="radio" name="PurchaseIsActive" value="Y" id="Purchaseradio-one" <%if(PurchaseIsActive.equalsIgnoreCase("Y")) {%> checked <%}%>/>
		<label for="Purchaseradio-one">Active</label>
		<input type="radio" name="PurchaseIsActive" value="N" id="Purchaseradio-two" <%if(PurchaseIsActive.equalsIgnoreCase("N")) {%> checked <%}%>/>
		<label for="Purchaseradio-two">Non Active</label>
	</div> --%>
	<label class="control-label col-sm-4" for="Subject">Auto Email: </label>    
<input type="hidden" name="PurchaseisAutoEmail" value="<%=PurchaseisAutoEmail%>" id="PurchaseisAutoEmail">
<input type="checkbox" id="Purchaseradio-Purchaseturn" <%if(PurchaseisAutoEmail.equalsIgnoreCase("Y")) {%> checked <%}%> data-toggle="toggle" data-style="ios" data-on="On" data-off="Off"/>
     </div>
</div>
</div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Admin Email">Admin User:</label>
      <div class="col-sm-4">
          <INPUT class="form-control"  name="PurchaseAdminmail" type="hidden" value="<%=PurchaseAdminmail%>"> 
          <div class="input-group">     
        <INPUT class="form-control" name="PurchaseAdminUser" type="TEXT" value="<%=PurchaseAdminUser%>" size="50" MAXLENGTH=50>
        <span class="input-group-addon" onClick="javascript:popWin('userList.jsp?USER_ID='+form1.PurchaseAdminUser.value+'&TYPE=PurchaseAdminUser');">
			<a href="#" data-toggle="tooltip" data-placement="top" title="User Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
    </div>
    <div class="form-inline">
  <div class="col-sm-4">
  <input type="button" class="Submit btn btn-default" onClick="AddPurchaseAdminUser('PurchaseAdminUser');" value="Add"/>
  </div>
  </div>
    </div>
    <div class="container-fluid">
		<div class="col-xs-12 col-sm-12 col-md-12" id="showdata">
		
		</div>
	</div>   
	
    
		             
    <div class="form-group">
         <label class="control-label col-sm-4" for="User Email">Other User:</label>
      <div class="col-sm-4">
      <INPUT class="form-control"  name="PurchaseUsermail" type="hidden" value="<%=PurchaseUsermail%>">
      <div class="input-group">           
       <INPUT class="form-control" name="PurchaseUser" type="TEXT" value="<%=PurchaseUser%>" size="50" MAXLENGTH=50>
       <span class="input-group-addon" onClick="javascript:popWin('userList.jsp?USER_ID='+form1.PurchaseUser.value+'&TYPE=PurchaseUser');">
			<a href="#" data-toggle="tooltip" data-placement="top" title="User Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
      </div>
          <div class="form-inline">
  <div class="col-sm-4">
  <input type="button" class="Submit btn btn-default" onClick="AddPurchaseAdminUser('PurchaseUser');" value="Add"/>
  </div>
  </div>
    </div>
    <div class="container-fluid">
		<div class="col-xs-12 col-sm-12 col-md-12" id="showPurchaseUser">

		</div>
	</div>
	
	<div class="container-fluid">
	<div class="col-xs-12 col-sm-12 col-md-12">
	<div class="panel panel-default">
    <div class="panel-heading" style="background: rgba(60, 141, 188, 0.29); height: 36px"><strong>Email To & Attachment Configuration</strong></div>
    <div class="panel-body">
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Create Email">Upon Create:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="PurchaseCreateEmail" value="All" id="Purchaseradio-CreateEmailAll" <%if(PurchaseCreateEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseCreateEmail" value="Admin" id="Purchaseradio-CreateEmailAdmin" <%if(PurchaseCreateEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseCreateEmail" value="User" id="Purchaseradio-CreateEmailUser" <%if(PurchaseCreateEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateEmailUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseCreateEmail" value="Supplier" id="Purchaseradio-CreateEmailSupplier" <%if(PurchaseCreateEmail.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateEmailSupplier">Supplier</label>
		</div>
		</div>
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Approve Email">Upon Approve:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="PurchaseApproveEmail" value="All" id="Purchaseradio-ApproveEmailAll" <%if(PurchaseApproveEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseApproveEmail" value="Admin" id="Purchaseradio-ApproveEmailAdmin" <%if(PurchaseApproveEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseApproveEmail" value="User" id="Purchaseradio-ApproveEmailUser" <%if(PurchaseApproveEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveEmailUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseApproveEmail" value="Supplier" id="Purchaseradio-ApproveEmailSupplier" <%if(PurchaseApproveEmail.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveEmailSupplier">Supplier</label>
		</div>
		</div>
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Reject Email">Upon Reject:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="PurchaseRejectEmail" value="All" id="Purchaseradio-RejectEmailAll" <%if(PurchaseRejectEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Purchaseradio-RejectEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseRejectEmail" value="Admin" id="Purchaseradio-RejectEmailAdmin" <%if(PurchaseRejectEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Purchaseradio-RejectEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseRejectEmail" value="User" id="Purchaseradio-RejectEmailUser" <%if(PurchaseRejectEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Purchaseradio-RejectEmailUser">User</label>
		</div>
		</div>
		<div class="form-group">
      <label class="control-label col-sm-4" for="Attachment">Attachment:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="PurchaseCreateAtt" value="All" id="Purchaseradio-CreateAttAll" <%if(PurchaseCreateAtt.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateAttAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseCreateAtt" value="PO" id="Purchaseradio-CreateAttPO" <%if(PurchaseCreateAtt.equalsIgnoreCase("PO")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateAttPO">PO</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseCreateAtt" value="Invoice" id="Purchaseradio-CreateAttInvoice" <%if(PurchaseCreateAtt.equalsIgnoreCase("Invoice")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateAttInvoice">Invoice</label>&nbsp;&nbsp;		
		<input type="radio" name="PurchaseCreateAtt" value="None" id="Purchaseradio-CreateAttNone" <%if(PurchaseCreateAtt.equalsIgnoreCase("None")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateAttNone">None</label>
		</div>
		</div>
		<div class="form-group">
      <label class="control-label col-sm-4" for="Attachment To">Attachment To:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="PurchaseCreateAttTo" value="All" id="Purchaseradio-CreateAttToAll" <%if(PurchaseCreateAttTo.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateAttToAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseCreateAttTo" value="Admin" id="Purchaseradio-CreateAttToAdmin" <%if(PurchaseCreateAttTo.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateAttToAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseCreateAttTo" value="User" id="Purchaseradio-CreateAttToUser" <%if(PurchaseCreateAttTo.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateAttToUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="PurchaseCreateAttTo" value="Supplier" id="Purchaseradio-CreateAttToSupplier" <%if(PurchaseCreateAttTo.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateAttToSupplier">Supplier</label>
		<input type="radio" name="PurchaseCreateAttTo" value="None" id="Purchaseradio-CreateAttToNone" <%if(PurchaseCreateAttTo.equalsIgnoreCase("None")) {%> checked <%}%>/>
		<label for="Purchaseradio-CreateAttToNone">None</label>
		</div>
		</div>
    </div>
    </div>
    </div>
    </div>
    <%-- <div class="col-xs-12 col-sm-12 col-md-12">
	<div class="panel panel-default">
    <div class="panel-heading" style="background: rgba(60, 141, 188, 0.29); height: 36px"><strong>Approve</strong></div>
    <div class="panel-body">
    <div class="form-group">
      <label class="control-label col-sm-5" for="Admin Email">Email To:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="PurchaseApproveEmail" value="Both" id="Purchaseradio-ApproveEmailBoth" <%if(PurchaseApproveEmail.equalsIgnoreCase("Both")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveEmailBoth">Both</label>
		<input type="radio" name="PurchaseApproveEmail" value="Admin" id="Purchaseradio-ApproveEmailAdmin" <%if(PurchaseApproveEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveEmailAdmin">Admin</label>
		<input type="radio" name="PurchaseApproveEmail" value="User" id="Purchaseradio-ApproveEmailUser" <%if(PurchaseApproveEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveEmailUser">User</label>
		</div>
		</div>
		<div class="form-group">
      <label class="control-label col-sm-5" for="Admin Email">Attachment:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="PurchaseApproveAtt" value="Both" id="Purchaseradio-ApproveAttBoth" <%if(PurchaseApproveAtt.equalsIgnoreCase("Both")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveAttBoth">Both</label>
		<input type="radio" name="PurchaseApproveAtt" value="PO" id="Purchaseradio-ApproveAttPO" <%if(PurchaseApproveAtt.equalsIgnoreCase("PO")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveAttPO">PO</label>
		<input type="radio" name="PurchaseApproveAtt" value="Invoice" id="Purchaseradio-ApproveAttGRN" <%if(PurchaseApproveAtt.equalsIgnoreCase("GRN")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveAttGRN">GRN</label>
		<input type="radio" name="PurchaseApproveAtt" value="None" id="Purchaseradio-ApproveAttNone" <%if(PurchaseApproveAtt.equalsIgnoreCase("None")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveAttNone">None</label>
		</div>
		</div>
		<div class="form-group">
      <label class="control-label col-sm-5" for="Admin Email">Attachment To:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="PurchaseApproveAttTo" value="Both" id="Purchaseradio-ApproveAttToBoth" <%if(PurchaseApproveAttTo.equalsIgnoreCase("Both")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveAttToBoth">Both</label>
		<input type="radio" name="PurchaseApproveAttTo" value="Admin" id="Purchaseradio-ApproveAttToAdmin" <%if(PurchaseApproveAttTo.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveAttToAdmin">Admin</label>
		<input type="radio" name="PurchaseApproveAttTo" value="User" id="Purchaseradio-ApproveAttToUser" <%if(PurchaseApproveAttTo.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Purchaseradio-ApproveAttToUser">User</label>
		</div>
		</div>
    </div>
    </div>
    </div> --%>
    
    
     </div>
       </div>
    
    
    <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Sales Estimate Order</strong></div>
    <div class="panel-body">
     <style>
  .toggle.ios, .toggle-on.ios, .toggle-off.ios { border-radius: 20px; }
  .toggle.ios .toggle-handle { border-radius: 20px; }
</style>       
    <div class="form-group">
      <label class="control-label col-sm-4" for="Subject">Type:</label>
      <div class="col-sm-4">
      <div class="form-inline">
      <INPUT name="EstimateOrderType" readonly value="SALES ESTIMATE" class="form-control">&nbsp;&nbsp;
      <input type="hidden" name="EstimateIsActive" value="<%=EstimateIsActive%>" id="EstimateIsActive">
	  <input type="checkbox" id="Estimateradio-one" <%if(EstimateIsActive.equalsIgnoreCase("Y")) {%> checked <%}%> data-toggle="toggle" data-style="ios"/>
     </div>
      </div>      
    
    <div class="form-inline">
  <div class="col-sm-4">      
        <%-- <div class="switch-field">
		<input type="radio" name="EstimateIsActive" id="Estimateradio-one" value="Y" <%if(EstimateIsActive.equalsIgnoreCase("Y")) {%> checked <%}%>/>
		<label for="Estimateradio-one">Active</label>
		<input type="radio" name="EstimateIsActive" id="Estimateradio-two" value="N" <%if(EstimateIsActive.equalsIgnoreCase("N")) {%> checked <%}%>/>
		<label for="Estimateradio-two">Non Active</label>		
     </div> --%>         
<label class="control-label col-sm-4" for="Subject">Auto Email: </label>    
<input type="hidden" name="EstimateisAutoEmail" value="<%=EstimateisAutoEmail%>" id="EstimateisAutoEmail">
<input type="checkbox" id="Estimateradio-Estimateturn" <%if(EstimateisAutoEmail.equalsIgnoreCase("Y")) {%> checked <%}%> data-toggle="toggle" data-style="ios" data-on="On" data-off="Off"/>
     </div>
</div>
</div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Admin Email">Admin User:</label>
      <div class="col-sm-4">
      <INPUT class="form-control"  name="EstimateAdminmail" type="hidden" value="<%=EstimateAdminmail%>"> 
          <div class="input-group">          
        <INPUT class="form-control" name="EstimateAdminUser" type="TEXT" value="<%=EstimateAdminUser%>" size="100" MAXLENGTH=50>
        <span class="input-group-addon" onClick="javascript:popWin('userList.jsp?USER_ID='+form1.EstimateAdminUser.value+'&TYPE=EstimateAdminUser');">
			<a href="#" data-toggle="tooltip" data-placement="top" title="User Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
    </div>
              <div class="form-inline">
  <div class="col-sm-4">
  <input type="button" class="Submit btn btn-default" onClick="AddPurchaseAdminUser('EstimateAdminUser');" value="Add"/>
  </div>
  </div>
    </div>
        <div class="container-fluid">
		<div class="col-xs-12 col-sm-12 col-md-12" id="showEstimateAdmin">

		</div>
	</div>  
                 
    <div class="form-group">
         <label class="control-label col-sm-4" for="User Email">Other User:</label>
      <div class="col-sm-4"> 
      <INPUT class="form-control"  name="EstimateUsermail" type="hidden" value="<%=EstimateUsermail%>">
      <div class="input-group">         
       <INPUT class="form-control" name="EstimateUser" type="TEXT" value="<%=EstimateUser%>" size="100" MAXLENGTH=50>
       <span class="input-group-addon" onClick="javascript:popWin('userList.jsp?USER_ID='+form1.EstimateUser.value+'&TYPE=EstimateUser');">
			<a href="#" data-toggle="tooltip" data-placement="top" title="User Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
      </div>
                <div class="form-inline">
  <div class="col-sm-4">
  <input type="button" class="Submit btn btn-default" onClick="AddPurchaseAdminUser('EstimateUser');" value="Add"/>
  </div>
  </div>
    </div>
     <div class="container-fluid">
		<div class="col-xs-12 col-sm-12 col-md-12" id="showEstimateUser">

		</div>
	</div>
	
	<div class="container-fluid">
	<div class="col-xs-12 col-sm-12 col-md-12">
	<div class="panel panel-default">
    <div class="panel-heading" style="background: rgba(60, 141, 188, 0.29); height: 36px"><strong>Email To & Attachment Configuration</strong></div>
    <div class="panel-body">
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Create Email">Upon Create:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="EstimateCreateEmail" value="All" id="Estimateradio-CreateEmailAll" <%if(EstimateCreateEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateCreateEmail" value="Admin" id="Estimateradio-CreateEmailAdmin" <%if(EstimateCreateEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateCreateEmail" value="User" id="Estimateradio-CreateEmailUser" <%if(EstimateCreateEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateEmailUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateCreateEmail" value="Supplier" id="Estimateradio-CreateEmailSupplier" <%if(EstimateCreateEmail.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateEmailSupplier">Supplier</label>
		</div>
		</div>
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Approve Email">Upon Approve:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="EstimateApproveEmail" value="All" id="Estimateradio-ApproveEmailAll" <%if(EstimateApproveEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Estimateradio-ApproveEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateApproveEmail" value="Admin" id="Estimateradio-ApproveEmailAdmin" <%if(EstimateApproveEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Estimateradio-ApproveEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateApproveEmail" value="User" id="Estimateradio-ApproveEmailUser" <%if(EstimateApproveEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Estimateradio-ApproveEmailUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateApproveEmail" value="Supplier" id="Estimateradio-ApproveEmailSupplier" <%if(EstimateApproveEmail.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Estimateradio-ApproveEmailSupplier">Supplier</label>
		</div>
		</div>
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Reject Email">Upon Reject:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="EstimateRejectEmail" value="All" id="Estimateradio-RejectEmailAll" <%if(EstimateRejectEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Estimateradio-RejectEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateRejectEmail" value="Admin" id="Estimateradio-RejectEmailAdmin" <%if(EstimateRejectEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Estimateradio-RejectEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateRejectEmail" value="User" id="Estimateradio-RejectEmailUser" <%if(EstimateRejectEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Estimateradio-RejectEmailUser">User</label>
		</div>
		</div>
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Attachment">Attachment:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="EstimateCreateAtt" value="All" id="Estimateradio-CreateAttAll" <%if(EstimateCreateAtt.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateAttAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateCreateAtt" value="PO" id="Estimateradio-CreateAttPO" <%if(EstimateCreateAtt.equalsIgnoreCase("PO")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateAttPO">PO</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateCreateAtt" value="Invoice" id="Estimateradio-CreateAttInvoice" <%if(EstimateCreateAtt.equalsIgnoreCase("Invoice")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateAttInvoice">Invoice</label>&nbsp;&nbsp;		
		<input type="radio" name="EstimateCreateAtt" value="None" id="Estimateradio-CreateAttNone" <%if(EstimateCreateAtt.equalsIgnoreCase("None")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateAttNone">None</label>
		</div>
		</div>
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Attachment To">Attachment To:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="EstimateCreateAttTo" value="All" id="Estimateradio-CreateAttToAll" <%if(EstimateCreateAttTo.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateAttToAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateCreateAttTo" value="Admin" id="Estimateradio-CreateAttToAdmin" <%if(EstimateCreateAttTo.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateAttToAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateCreateAttTo" value="User" id="Estimateradio-CreateAttToUser" <%if(EstimateCreateAttTo.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateAttToUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="EstimateCreateAttTo" value="Supplier" id="Estimateradio-CreateAttToSupplier" <%if(EstimateCreateAttTo.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateAttToSupplier">Supplier</label>
		<input type="radio" name="EstimateCreateAttTo" value="None" id="Estimateradio-CreateAttToNone" <%if(EstimateCreateAttTo.equalsIgnoreCase("None")) {%> checked <%}%>/>
		<label for="Estimateradio-CreateAttToNone">None</label>
		</div>
		</div>
    </div>
    </div>
    </div>
    </div>
	
       </div>
    </div>
    
    <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Sales Order</strong></div>
    <div class="panel-body">
    <style>
  .toggle.ios, .toggle-on.ios, .toggle-off.ios { border-radius: 20px; }
  .toggle.ios .toggle-handle { border-radius: 20px; }
</style>        
    <div class="form-group">
      <label class="control-label col-sm-4" for="Subject">Type:</label>
      <div class="col-sm-4">
      <div class="form-inline">
      <INPUT name="SalesOrderType" readonly value="SALES" class="form-control">
      <input type="hidden" name="SalesIsActive" value="<%=SalesIsActive%>" id="SalesIsActive">
	  <input type="checkbox" id="Salesradio-one" <%if(SalesIsActive.equalsIgnoreCase("Y")) {%> checked <%}%> data-toggle="toggle" data-style="ios"/>		
     </div>
      </div>      
    
    <div class="form-inline">
  <div class="col-sm-4">
        <%-- <div class="switch-field">
		 <input type="radio" name="SalesIsActive" id="Salesradio-one" value="Y" <%if(SalesIsActive.equalsIgnoreCase("Y")) {%> checked <%}%>/>
		<label for="Salesradio-one">Active</label>
		<input type="radio" name="SalesIsActive" id="Salesradio-two" value="N" <%if(SalesIsActive.equalsIgnoreCase("N")) {%> checked <%}%>/>
		<label for="Salesradio-two">Non Active</label> 
		</div> --%>
<label class="control-label col-sm-4" for="Subject">Auto Email: </label>    
<input type="hidden" name="SalesisAutoEmail" value="<%=SalesisAutoEmail%>" id="SalesisAutoEmail">
<input type="checkbox" id="Salesradio-Salesturn" <%if(SalesisAutoEmail.equalsIgnoreCase("Y")) {%> checked <%}%> data-toggle="toggle" data-style="ios" data-on="On" data-off="Off"/>		
     </div>
</div>
</div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Admin Email">Admin User:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="SalesAdminmail" type="hidden" value="<%=SalesAdminmail%>">
        <div class="input-group">         
       <INPUT class="form-control" name="SalesAdminUser" type="TEXT" value="<%=SalesAdminUser%>" size="100" MAXLENGTH=50>
       <span class="input-group-addon" onClick="javascript:popWin('userList.jsp?USER_ID='+form1.SalesAdminUser.value+'&TYPE=SalesAdminUser');">
			<a href="#" data-toggle="tooltip" data-placement="top" title="User Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
      </div>
                    <div class="form-inline">
  <div class="col-sm-4">
  <input type="button" class="Submit btn btn-default" onClick="AddPurchaseAdminUser('SalesAdminUser');" value="Add"/>
  </div>
  </div>
    </div>
          <div class="container-fluid">
		<div class="col-xs-12 col-sm-12 col-md-12" id="showSalesAdmin">

		</div>
	</div>            
    <div class="form-group">
         <label class="control-label col-sm-4" for="User Email">Other User:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="SalesUsermail" type="hidden" value="<%=SalesUsermail%>">
       <div class="input-group">         
       <INPUT class="form-control" name="SalesUser" type="TEXT" value="<%=SalesUser%>" size="100" MAXLENGTH=50>
       <span class="input-group-addon" onClick="javascript:popWin('userList.jsp?USER_ID='+form1.SalesUser.value+'&TYPE=SalesUser');">
			<a href="#" data-toggle="tooltip" data-placement="top" title="User Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
      </div>
      <div class="form-inline">
  <div class="col-sm-4">
  <input type="button" class="Submit btn btn-default" onClick="AddPurchaseAdminUser('SalesUser');" value="Add"/>
  </div>
  </div>
    </div>
     <div class="container-fluid">
		<div class="col-xs-12 col-sm-12 col-md-12" id="showSalesUser">

		</div>
	</div>
	
	<div class="container-fluid">
	<div class="col-xs-12 col-sm-12 col-md-12">
	<div class="panel panel-default">
    <div class="panel-heading" style="background: rgba(60, 141, 188, 0.29); height: 36px"><strong>Email To & Attachment Configuration</strong></div>
    <div class="panel-body">
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Create Email">Upon Create:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="SalesCreateEmail" value="All" id="Salesradio-CreateEmailAll" <%if(SalesCreateEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Salesradio-CreateEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="SalesCreateEmail" value="Admin" id="Salesradio-CreateEmailAdmin" <%if(SalesCreateEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Salesradio-CreateEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="SalesCreateEmail" value="User" id="Salesradio-CreateEmailUser" <%if(SalesCreateEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Salesradio-CreateEmailUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="SalesCreateEmail" value="Supplier" id="Salesradio-CreateEmailSupplier" <%if(SalesCreateEmail.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Salesradio-CreateEmailSupplier">Supplier</label>
		</div>
		</div>
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Approve Email">Upon Approve:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="SalesApproveEmail" value="All" id="Salesradio-ApproveEmailAll" <%if(SalesApproveEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Salesradio-ApproveEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="SalesApproveEmail" value="Admin" id="Salesradio-ApproveEmailAdmin" <%if(SalesApproveEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Salesradio-ApproveEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="SalesApproveEmail" value="User" id="Salesradio-ApproveEmailUser" <%if(SalesApproveEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Salesradio-ApproveEmailUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="SalesApproveEmail" value="Supplier" id="Salesradio-ApproveEmailSupplier" <%if(SalesApproveEmail.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Salesradio-ApproveEmailSupplier">Supplier</label>
		</div>
		</div>
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Reject Email">Upon Reject:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="SalesRejectEmail" value="All" id="Salesradio-RejectEmailAll" <%if(SalesRejectEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Salesradio-RejectEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="SalesRejectEmail" value="Admin" id="Salesradio-RejectEmailAdmin" <%if(SalesRejectEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Salesradio-RejectEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="SalesRejectEmail" value="User" id="Salesradio-RejectEmailUser" <%if(SalesRejectEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Salesradio-RejectEmailUser">User</label>
		</div>
		</div>
		<div class="form-group">
      <label class="control-label col-sm-4" for="Attachment">Attachment:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="SalesCreateAtt" value="All" id="Salesradio-CreateAttAll" <%if(SalesCreateAtt.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Salesradio-CreateAttAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="SalesCreateAtt" value="PO" id="Salesradio-CreateAttPO" <%if(SalesCreateAtt.equalsIgnoreCase("PO")) {%> checked <%}%>/>
		<label for="Salesradio-CreateAttPO">PO</label>&nbsp;&nbsp;
		<input type="radio" name="SalesCreateAtt" value="Invoice" id="Salesradio-CreateAttInvoice" <%if(SalesCreateAtt.equalsIgnoreCase("Invoice")) {%> checked <%}%>/>
		<label for="Salesradio-CreateAttInvoice">Invoice</label>&nbsp;&nbsp;		
		<input type="radio" name="SalesCreateAtt" value="None" id="Salesradio-CreateAttNone" <%if(SalesCreateAtt.equalsIgnoreCase("None")) {%> checked <%}%>/>
		<label for="Salesradio-CreateAttNone">None</label>
		</div>
		</div>
		<div class="form-group">
      <label class="control-label col-sm-4" for="Attachment To">Attachment To:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="SalesCreateAttTo" value="All" id="Salesradio-CreateAttToAll" <%if(SalesCreateAttTo.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Salesradio-CreateAttToAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="SalesCreateAttTo" value="Admin" id="Salesradio-CreateAttToAdmin" <%if(SalesCreateAttTo.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Salesradio-CreateAttToAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="SalesCreateAttTo" value="User" id="Salesradio-CreateAttToUser" <%if(SalesCreateAttTo.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Salesradio-CreateAttToUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="SalesCreateAttTo" value="Supplier" id="Salesradio-CreateAttToSupplier" <%if(SalesCreateAttTo.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Salesradio-CreateAttToSupplier">Supplier</label>
		<input type="radio" name="SalesCreateAttTo" value="None" id="Salesradio-CreateAttToNone" <%if(SalesCreateAttTo.equalsIgnoreCase("None")) {%> checked <%}%>/>
		<label for="Salesradio-CreateAttToNone">None</label>
		</div>
		</div>
    </div>
    </div>
    </div>
    </div>
	
       </div>
    </div>  
     
        
        <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Rental Order</strong></div>
    <div class="panel-body">
         <style>
  .toggle.ios, .toggle-on.ios, .toggle-off.ios { border-radius: 20px; }
  .toggle.ios .toggle-handle { border-radius: 20px; }
</style>   
    <div class="form-group">
      <label class="control-label col-sm-4" for="Subject">Type:</label>
      <div class="col-sm-4">
      <div class="form-inline">
      <INPUT name="InvoiceOrderType" readonly value="RENTAL" class="form-control">
      <input type="hidden" name="RentalIsActive" value="<%=RentalIsActive%>" id="RentalIsActive">
	  <input type="checkbox" id="Rentalradio-one" <%if(RentalIsActive.equalsIgnoreCase("Y")) {%> checked <%}%> data-toggle="toggle" data-style="ios"/>
     </div>
      </div>      
    
    <div class="form-inline">
  <div class="col-sm-4">    
        <%-- <div class="switch-field">
		<input type="radio" name="RentalIsActive" id="Invoiceradio-one" value="Y" <%if(RentalIsActive.equalsIgnoreCase("Y")) {%> checked <%}%>/>
		<label for="Invoiceradio-one">Active</label>
		<input type="radio" name="RentalIsActive" id="Invoiceradio-two" value="N" <%if(RentalIsActive.equalsIgnoreCase("N")) {%> checked <%}%>/>
		<label for="Invoiceradio-two">Non Active</label>		
     </div> --%>
<label class="control-label col-sm-4" for="Subject">Auto Email: </label>    
<input type="hidden" name="RentalisAutoEmail" value="<%=RentalisAutoEmail%>" id="RentalisAutoEmail">
<input type="checkbox" id="Rentalradio-Rentalturn" <%if(RentalisAutoEmail.equalsIgnoreCase("Y")) {%> checked <%}%> data-toggle="toggle" data-style="ios" data-on="On" data-off="Off"/>
     </div>
</div>
</div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Admin Email">Admin User:</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="RentalAdminmail" type="hidden" value="<%=RentalAdminmail%>">
        <div class="input-group">         
       <INPUT class="form-control" name="RentalAdminUser" type="TEXT" value="<%=RentalAdminUser%>" size="100" MAXLENGTH=50>
       <span class="input-group-addon" onClick="javascript:popWin('userList.jsp?USER_ID='+form1.RentalAdminUser.value+'&TYPE=RentalAdminUser');">
			<a href="#" data-toggle="tooltip" data-placement="top" title="User Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
      </div>
                          <div class="form-inline">
  <div class="col-sm-4">
  <input type="button" class="Submit btn btn-default" onClick="AddPurchaseAdminUser('RentalAdminUser');" value="Add"/>
  </div>
  </div>
    </div>
                   <div class="container-fluid">
		<div class="col-xs-12 col-sm-12 col-md-12" id="showRentalAdmin">

		</div>
	</div>          
    <div class="form-group">
         <label class="control-label col-sm-4" for="User Email">Other User:</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="RentalUsermail" type="hidden" value="<%=RentalUsermail%>">
       <div class="input-group">         
       <INPUT class="form-control" name="RentalUser" type="TEXT" value="<%=RentalUser%>" size="100" MAXLENGTH=50>
       <span class="input-group-addon" onClick="javascript:popWin('userList.jsp?USER_ID='+form1.RentalUser.value+'&TYPE=RentalUser');">
			<a href="#" data-toggle="tooltip" data-placement="top" title="User Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
      </div>
      <div class="form-inline">
  <div class="col-sm-4">
  <input type="button" class="Submit btn btn-default" onClick="AddPurchaseAdminUser('RentalUser');" value="Add"/>
  </div>
  </div>
    </div>
    <div class="container-fluid">
		<div class="col-xs-12 col-sm-12 col-md-12" id="showRentalUser">

		</div>
	</div>
	
	<div class="container-fluid">
	<div class="col-xs-12 col-sm-12 col-md-12">
	<div class="panel panel-default">
    <div class="panel-heading" style="background: rgba(60, 141, 188, 0.29); height: 36px"><strong>Email To & Attachment Configuration</strong></div>
    <div class="panel-body">
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Create Email">Upon Create:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="RentalCreateEmail" value="All" id="Rentalradio-CreateEmailAll" <%if(RentalCreateEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="RentalCreateEmail" value="Admin" id="Rentalradio-CreateEmailAdmin" <%if(RentalCreateEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="RentalCreateEmail" value="User" id="Rentalradio-CreateEmailUser" <%if(RentalCreateEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateEmailUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="RentalCreateEmail" value="Supplier" id="Rentalradio-CreateEmailSupplier" <%if(RentalCreateEmail.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateEmailSupplier">Supplier</label>
		</div>
		</div>
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Approve Email">Upon Approve:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="RentalApproveEmail" value="All" id="Rentalradio-ApproveEmailAll" <%if(RentalApproveEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Rentalradio-ApproveEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="RentalApproveEmail" value="Admin" id="Rentalradio-ApproveEmailAdmin" <%if(RentalApproveEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Rentalradio-ApproveEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="RentalApproveEmail" value="User" id="Rentalradio-ApproveEmailUser" <%if(RentalApproveEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Rentalradio-ApproveEmailUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="RentalApproveEmail" value="Supplier" id="Rentalradio-ApproveEmailSupplier" <%if(RentalApproveEmail.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Rentalradio-ApproveEmailSupplier">Supplier</label>
		</div>
		</div>
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Reject Email">Upon Reject:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="RentalRejectEmail" value="All" id="Rentalradio-RejectEmailAll" <%if(RentalRejectEmail.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Rentalradio-RejectEmailAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="RentalRejectEmail" value="Admin" id="Rentalradio-RejectEmailAdmin" <%if(RentalRejectEmail.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Rentalradio-RejectEmailAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="RentalRejectEmail" value="User" id="Rentalradio-RejectEmailUser" <%if(RentalRejectEmail.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Rentalradio-RejectEmailUser">User</label>
		</div>
		</div>
		<div class="form-group">
      <label class="control-label col-sm-4" for="Attachment">Attachment:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="RentalCreateAtt" value="All" id="Rentalradio-CreateAttAll" <%if(RentalCreateAtt.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateAttAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="RentalCreateAtt" value="PO" id="Rentalradio-CreateAttPO" <%if(RentalCreateAtt.equalsIgnoreCase("PO")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateAttPO">PO</label>&nbsp;&nbsp;
		<input type="radio" name="RentalCreateAtt" value="Invoice" id="Rentalradio-CreateAttInvoice" <%if(RentalCreateAtt.equalsIgnoreCase("Invoice")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateAttInvoice">Invoice</label>&nbsp;&nbsp;		
		<input type="radio" name="RentalCreateAtt" value="None" id="Rentalradio-CreateAttNone" <%if(RentalCreateAtt.equalsIgnoreCase("None")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateAttNone">None</label>
		</div>
		</div>
		<div class="form-group">
      <label class="control-label col-sm-4" for="Attachment To">Attachment To:</label>
      <div class="col-sm-4" style="padding: 7px;">
      <input type="radio" name="RentalCreateAttTo" value="All" id="Rentalradio-CreateAttToAll" <%if(RentalCreateAttTo.equalsIgnoreCase("All")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateAttToAll">All</label>&nbsp;&nbsp;
		<input type="radio" name="RentalCreateAttTo" value="Admin" id="Rentalradio-CreateAttToAdmin" <%if(RentalCreateAttTo.equalsIgnoreCase("Admin")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateAttToAdmin">Admin</label>&nbsp;&nbsp;
		<input type="radio" name="RentalCreateAttTo" value="User" id="Rentalradio-CreateAttToUser" <%if(RentalCreateAttTo.equalsIgnoreCase("User")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateAttToUser">User</label>&nbsp;&nbsp;
		<input type="radio" name="RentalCreateAttTo" value="Supplier" id="Rentalradio-CreateAttToSupplier" <%if(RentalCreateAttTo.equalsIgnoreCase("Supplier")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateAttToSupplier">Supplier</label>
		<input type="radio" name="RentalCreateAttTo" value="None" id="Rentalradio-CreateAttToNone" <%if(RentalCreateAttTo.equalsIgnoreCase("None")) {%> checked <%}%>/>
		<label for="Rentalradio-CreateAttToNone">None</label>
		</div>
		</div>
    </div>
    </div>
    </div>
    </div>
     
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

<script>
  $(function() {	  
    $('#Purchaseradio-one').change(function() {
      $('#console-event').html('Toggle: ' + $(this).prop('checked'))
      if($(this).prop('checked')==true)
    	  document.form1.PurchaseIsActive.value="Y";
      else 
    	  document.form1.PurchaseIsActive.value="N";
    })
    $('#Purchaseradio-Purchaseturn').change(function() {
      $('#console-event').html('Toggle: ' + $(this).prop('checked'))
      if($(this).prop('checked')==true)
    	  document.form1.PurchaseisAutoEmail.value="Y";
      else 
    	  document.form1.PurchaseisAutoEmail.value="N";
    })
    $('#Estimateradio-Estimateturn').change(function() {
      $('#console-event').html('Toggle: ' + $(this).prop('checked'))
      if($(this).prop('checked')==true)
    	  document.form1.EstimateisAutoEmail.value="Y";
      else 
    	  document.form1.EstimateisAutoEmail.value="N";
    })
    $('#Salesradio-Salesturn').change(function() {
      $('#console-event').html('Toggle: ' + $(this).prop('checked'))
      if($(this).prop('checked')==true)
    	  document.form1.SalesisAutoEmail.value="Y";
      else 
    	  document.form1.SalesisAutoEmail.value="N";
    })
    $('#Rentalradio-Rentalturn').change(function() {
      $('#console-event').html('Toggle: ' + $(this).prop('checked'))
      if($(this).prop('checked')==true)
    	  document.form1.RentalisAutoEmail.value="Y";
      else 
    	  document.form1.RentalisAutoEmail.value="N";
    })
        $('#Estimateradio-one').change(function() {
      $('#console-event').html('Toggle: ' + $(this).prop('checked'))
      if($(this).prop('checked')==true)
    	  document.form1.EstimateIsActive.value="Y";
      else 
    	  document.form1.EstimateIsActive.value="N";
    })
    $('#Salesradio-one').change(function() {
      $('#console-event').html('Toggle: ' + $(this).prop('checked'))
      if($(this).prop('checked')==true)
    	  document.form1.SalesIsActive.value="Y";
      else 
    	  document.form1.SalesIsActive.value="N";
    })
    $('#Rentalradio-one').change(function() {
      $('#console-event').html('Toggle: ' + $(this).prop('checked'))
      if($(this).prop('checked')==true)
    	  document.form1.RentalIsActive.value="Y";
      else 
    	  document.form1.RentalIsActive.value="N";
    })
  })
</script>
<SCRIPT LANGUAGE="JavaScript">
$(document).ready(function() {
	GetUser("PurchaseAdminUser");
	GetUser("PurchaseUser");
	GetUser("EstimateAdminUser");
	GetUser("EstimateUser");
	GetUser("SalesAdminUser");
	GetUser("SalesUser");
	GetUser("RentalAdminUser");
	GetUser("RentalUser");
	GetConfigEmail("PURCHASE");
	GetConfigEmail("ESTIMATE");
	GetConfigEmail("SALES");
	GetConfigEmail("RENTAL");
});
function GetUser(OrderType)
{
	var UserType="";
	$.ajax( {
		type : "get",
		url : '/track/EmailServlet?action=GETUSER',
		data : {
		PLANT : "<%=plant%>",
		ACTION : "GETUSER",
		OrderType:OrderType
	},
	dataType : "json",
	success : function(data) {
		//alert(JSON.stringify(data));
		if(OrderType=="PurchaseAdminUser")
			{
		 $("#showdata").empty();
		 UserType="ADMIN";
			}
		else if(OrderType=="PurchaseUser")
			{
			 $("#showPurchaseUser").empty();
			 UserType="OTHER";
			}
		else if(OrderType=="EstimateAdminUser")
		{
			 $("#showEstimateAdmin").empty();
			 UserType="ADMIN";
				}
			else if(OrderType=="EstimateUser")
				{
				 $("#showEstimateUser").empty();
				 UserType="OTHER";
				}
			else if(OrderType=="SalesAdminUser")
			{
				 $("#showSalesAdmin").empty();
				 UserType="ADMIN";
					}
				else if(OrderType=="SalesUser")
					{
					 $("#showSalesUser").empty();
					 UserType="OTHER";
					}
				else if(OrderType=="RentalAdminUser")
				{
					 $("#showRentalAdmin").empty();
					 UserType="ADMIN";
						}
					else if(OrderType=="RentalUser")
						{
						 $("#showRentalUser").empty();
						 UserType="OTHER";
						}
         var dat = "";
         dat = dat + "<table class=\"table table-bordered table-hover dataTable no-footer\">";
         dat = dat + "<thead>";
         dat = dat + "<tr class=\"table-primary\">";
         dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> S.No </th>";
         dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> User </th>";
         dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> Mail </th>";
         dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> Remove </th>";
         dat = dat + "</tr>";
         dat = dat + "</thead>";
         dat = dat + "<tbody>";
         $.each(data.items, function (index, listdata) {
             dat = dat + "<tr style=\"text-align: center;\">";
             dat = dat + "<td>" + listdata.Id + "</td>";
             dat = dat + "<td>" + listdata.userid + "</td>";
             dat = dat + "<td>" + listdata.usermail + "</td>";
             dat = dat + "<td><a onclick=\"deleteRow(" + listdata.Id + ",'"+OrderType+"','"+ UserType+"','"+ listdata.userid + "')\" ><i class=\"fa fa-trash-o\" style=\"color: red\"></i></a></td>";
             dat = dat + "</tr>";
         });

         dat = dat + "</tbody>";
         dat = dat + "</table>";
         if(OrderType=="PurchaseAdminUser")
        	 {
    	      $("#showdata").append(dat);
    	      if(data.IsActive=="Y")
    	      //$("#Purchaseradio-one").prop("checked", true);
    	    	  $('#Purchaseradio-one').bootstrapToggle('on')
    	      document.form1.PurchaseIsActive.value=data.IsActive;
        	 }
     	else if(OrderType=="PurchaseUser")
     		{
	         $("#showPurchaseUser").append(dat);
	         if(data.IsActive=="Y")
	    	     $('#Purchaseradio-one').bootstrapToggle('on')
	         document.form1.PurchaseIsActive.value=data.IsActive;
     		}
     	else if(OrderType=="EstimateAdminUser")
   	 	{
  	      $("#showEstimateAdmin").append(dat);
  	      if(data.IsActive=="Y")
  	      //$("#Estimateradio-one").prop("checked", true);
  	    	$('#Estimateradio-one').bootstrapToggle('on')
  	      document.form1.EstimateIsActive.value=data.IsActive;
      	 }
   	else if(OrderType=="EstimateUser")
   		{
	         $("#showEstimateUser").append(dat);
	         if(data.IsActive=="Y")
	    	     $('#Estimateradio-one').bootstrapToggle('on')
	         document.form1.EstimateIsActive.value=data.IsActive;
   		}
   	else if(OrderType=="SalesAdminUser")
	 	{
	      $("#showSalesAdmin").append(dat);
	      if(data.IsActive=="Y")
	      //$("#Salesradio-one").prop("checked", true);
	    	  $('#Salesradio-one').bootstrapToggle('on')
	      document.form1.SalesIsActive.value=data.IsActive;
  	 }
	else if(OrderType=="SalesUser")
		{
         $("#showSalesUser").append(dat);
         if(data.IsActive=="Y")
    	     $('#Salesradio-one').bootstrapToggle('on')
         document.form1.SalesIsActive.value=data.IsActive;
		}
	else if(OrderType=="RentalAdminUser")
 	{
      $("#showRentalAdmin").append(dat);
      if(data.IsActive=="Y")
      //$("#Rentalradio-one").prop("checked", true);
    	  $('#Rentalradio-one').bootstrapToggle('on')
      document.form1.RentalIsActive.value=data.IsActive;
	 }
else if(OrderType=="RentalUser")
	{
     $("#showRentalUser").append(dat);
     if(data.IsActive=="Y")
	     $('#Rentalradio-one').bootstrapToggle('on')
     document.form1.RentalIsActive.value=data.IsActive;
	}
	 
	},
	error:function(data)
	{
		
	}
});
	}
function GetConfigEmail(OrderType)
{
	$.ajax( {
		type : "get",
		url : '/track/EmailServlet?action=GETCONFIGEMAIL',
		data : {
		PLANT : "<%=plant%>",
		ACTION : "GETCONFIGEMAIL",
		OrderType:OrderType
	},
	dataType : "json",
	success : function(data) {
		if(OrderType=="PURCHASE")
			{
		document.form1.PurchaseCreateEmail.value=data.PurchaseCreateEmail;
		document.form1.PurchaseCreateAtt.value=data.PurchaseCreateAtt;
		document.form1.PurchaseCreateAttTo.value=data.PurchaseCreateAttTo;
		document.form1.PurchaseApproveEmail.value=data.PurchaseApproveEmail;
		document.form1.PurchaseRejectEmail.value=data.PurchaseRejectEmail;
		if(data.PurchaseisAutoEmail=="Y")
		{  	      
  	    $('#Purchaseradio-Purchaseturn').bootstrapToggle('on')
		  document.form1.PurchaseisAutoEmail.value=data.PurchaseisAutoEmail;
      	}
			}
		else if(OrderType=="ESTIMATE")
		{
			document.form1.EstimateCreateEmail.value=data.EstimateCreateEmail;
			document.form1.EstimateCreateAtt.value=data.EstimateCreateAtt;
			document.form1.EstimateCreateAttTo.value=data.EstimateCreateAttTo;
			document.form1.EstimateApproveEmail.value=data.EstimateApproveEmail;
			document.form1.EstimateRejectEmail.value=data.EstimateRejectEmail;
			if(data.EstimateisAutoEmail=="Y")
			{  	      
	  	    $('#Estimateradio-Estimateturn').bootstrapToggle('on')
			  document.form1.EstimateisAutoEmail.value=data.EstimateisAutoEmail;
	      	}
				}
		else if(OrderType=="SALES")
		{
			document.form1.SalesCreateEmail.value=data.SalesCreateEmail;
			document.form1.SalesCreateAtt.value=data.SalesCreateAtt;
			document.form1.SalesCreateAttTo.value=data.SalesCreateAttTo;
			document.form1.SalesApproveEmail.value=data.SalesApproveEmail;
			document.form1.SalesRejectEmail.value=data.SalesRejectEmail;
			if(data.SalesisAutoEmail=="Y")
			{  	      
	  	    $('#Salesradio-Salesturn').bootstrapToggle('on')
			  document.form1.SalesisAutoEmail.value=data.SalesisAutoEmail;
	      	}
				}
		else if(OrderType=="RENTAL")
		{
			document.form1.RentalCreateEmail.value=data.RentalCreateEmail;
			document.form1.RentalCreateAtt.value=data.RentalCreateAtt;
			document.form1.RentalCreateAttTo.value=data.RentalCreateAttTo;
			document.form1.RentalApproveEmail.value=data.RentalApproveEmail;
			document.form1.RentalRejectEmail.value=data.RentalRejectEmail;
			if(data.RentalisAutoEmail=="Y")
			{  	      
	  	    $('#Rentalradio-Rentalturn').bootstrapToggle('on')
			  document.form1.RentalisAutoEmail.value=data.RentalisAutoEmail;
	      	}
				}
	},
	error:function(data)
	{
		
	}
});
	}
function AddPurchaseAdminUser(OrderType)
{
	var User = "";
	var mail = "";
	var UserType="";
	if(OrderType=="PurchaseAdminUser"||OrderType=="EstimateAdminUser"||OrderType=="SalesAdminUser"||OrderType=="RentalAdminUser")
		UserType="ADMIN";
		else if(OrderType=="PurchaseUser"||OrderType=="EstimateUser"||OrderType=="SalesUser"||OrderType=="RentalUser")
			UserType="OTHER";
	if(OrderType=="PurchaseAdminUser")
		{
		User = document.form1.PurchaseAdminUser.value;
		mail = document.form1.PurchaseAdminmail.value;
	if(User==null||User=="")
	  {
		  alert("Please Enter Purchase Admin User");
		  return false;
	  }
	if(mail==null||mail=="")
	  {
		  alert("Purchase Admin Email Does Not Exists ");
		  return false;
	  }
		}
	else if(OrderType=="PurchaseUser")
		{
		User = document.form1.PurchaseUser.value;
		mail = document.form1.PurchaseUsermail.value;
	if(User==null||User=="")
	  {
		  alert("Please Enter Purchase Other User");
		  return false;
	  }
	if(mail==null||mail=="")
	  {
		  alert("Purchase Other Email Does Not Exists ");
		  return false;
	  }
		}
		else if(OrderType=="EstimateAdminUser")
		{
		User = document.form1.EstimateAdminUser.value;
		mail = document.form1.EstimateAdminmail.value;
	if(User==null||User=="")
	  {
		  alert("Please Enter Estimate Admin User");
		  return false;
	  }
	if(mail==null||mail=="")
	  {
		  alert("Estimate Admin Email Does Not Exists ");
		  return false;
	  }
		}
	else if(OrderType=="EstimateUser")
		{
		User = document.form1.EstimateUser.value;
		mail = document.form1.EstimateUsermail.value;
	if(User==null||User=="")
	  {
		  alert("Please Enter Estimate Other User");
		  return false;
	  }
	if(mail==null||mail=="")
	  {
		  alert("Estimate Other Email Does Not Exists ");
		  return false;
	  }
		}
	else if(OrderType=="SalesAdminUser")
	{
	User = document.form1.SalesAdminUser.value;
	mail = document.form1.SalesAdminmail.value;
if(User==null||User=="")
  {
	  alert("Please Enter Sales Admin User");
	  return false;
  }
if(mail==null||mail=="")
  {
	  alert("Sales Admin Email Does Not Exists ");
	  return false;
  }
	}
	else if(OrderType=="SalesUser")
	{
	User = document.form1.SalesUser.value;
	mail = document.form1.SalesUsermail.value;
if(User==null||User=="")
  {
	  alert("Please Enter Sales Other User");
	  return false;
  }
if(mail==null||mail=="")
  {
	  alert("Sales Other Email Does Not Exists ");
	  return false;
  }
	}
	else if(OrderType=="RentalAdminUser")
	{
	User = document.form1.RentalAdminUser.value;
	mail = document.form1.RentalAdminmail.value;
if(User==null||User=="")
  {
	  alert("Please Enter Rental Admin User");
	  return false;
  }
if(mail==null||mail=="")
  {
	  alert("Rental Admin Email Does Not Exists ");
	  return false;
  }
	}
	else if(OrderType=="RentalUser")
	{
	User = document.form1.RentalUser.value;
	mail = document.form1.RentalUsermail.value;
if(User==null||User=="")
  {
	  alert("Please Enter Rental Other User");
	  return false;
  }
if(mail==null||mail=="")
  {
	  alert("Rental Other Email Does Not Exists ");
	  return false;
  }
	}
	$.ajax( {
		type : "get",
		url : '/track/EmailServlet?action=ADDUSER',
		data : {
		PLANT : "<%=plant%>",
		ACTION : "ADDUSER",
		OrderType:OrderType,
		USERID:User,
		USERMAIL:mail
	},
	dataType : "json",
	success : function(data) {		
		if(OrderType=="PurchaseAdminUser")
		 $("#showdata").empty();
		else if(OrderType=="PurchaseUser")
			 $("#showPurchaseUser").empty();
		else if(OrderType=="EstimateAdminUser")
		 $("#showEstimateAdmin").empty();
		else if(OrderType=="EstimateUser")
			 $("#showEstimateUser").empty();
		else if(OrderType=="SalesAdminUser")
			 $("#showSalesAdmin").empty();
			else if(OrderType=="SalesUser")
				 $("#showSalesUser").empty();
			else if(OrderType=="RentalAdminUser")
				 $("#showRentalAdmin").empty();
				else if(OrderType=="RentalUser")
					 $("#showRentalUser").empty();
         var dat = "";
         dat = dat + "<table class=\"table table-bordered table-hover dataTable no-footer\">";
         dat = dat + "<thead>";
         dat = dat + "<tr class=\"table-primary\">";
         dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> S.No </th>";
         dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> User </th>";
         dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> Mail </th>";
         dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> Remove </th>";
         dat = dat + "</tr>";
         dat = dat + "</thead>";
         dat = dat + "<tbody>";
         $.each(data.items, function (index, listdata) {
             dat = dat + "<tr style=\"text-align: center;\">";
             dat = dat + "<td>" + listdata.Id + "</td>";
             dat = dat + "<td>" + listdata.userid + "</td>";
             dat = dat + "<td>" + listdata.usermail + "</td>";
             dat = dat + "<td><a onclick=\"deleteRow(" + listdata.Id + ",'"+OrderType+"','"+ UserType+"','"+ listdata.userid + "')\" ><i class=\"fa fa-trash-o\" style=\"color: red\"></i></a></td>";
             dat = dat + "</tr>";
         });

         dat = dat + "</tbody>";
         dat = dat + "</table>";
         	if(OrderType=="PurchaseAdminUser")
        	 {
		         $("#showdata").append(dat);
		         document.form1.PurchaseAdminUser.value="";
		         document.form1.PurchaseAdminmail.value="";
        	 }
         	else if(OrderType=="PurchaseUser")
         	{
		         $("#showPurchaseUser").append(dat);
		         document.form1.PurchaseUser.value="";
		         document.form1.PurchaseUsermail.value="";
       	 	}
         	else if(OrderType=="EstimateAdminUser")
       	 	{
		         $("#showEstimateAdmin").append(dat);
		         document.form1.EstimateAdminUser.value="";
		         document.form1.EstimateAdminmail.value="";
       	 	}
        	else if(OrderType=="EstimateUser")
        	{
		         $("#showEstimateUser").append(dat);
		         document.form1.EstimateUser.value="";
		         document.form1.EstimateUsermail.value="";
      	 	}
        	else if(OrderType=="SalesAdminUser")
       	 	{
		         $("#showSalesAdmin").append(dat);
		         document.form1.SalesAdminUser.value="";
		         document.form1.SalesAdminmail.value="";
       	 	}
        	else if(OrderType=="SalesUser")
        	{
		         $("#showSalesUser").append(dat);
		         document.form1.SalesUser.value="";
		         document.form1.SalesUsermail.value="";
      	 	}
        	else if(OrderType=="RentalAdminUser")
       	 	{
		         $("#showRentalAdmin").append(dat);
		         document.form1.RentalAdminUser.value="";
		         document.form1.RentalAdminmail.value="";
       	 	}
        	else if(OrderType=="RentalUser")
        	{
		         $("#showRentalUser").append(dat);
		         document.form1.RentalUser.value="";
		         document.form1.RentalUsermail.value="";
      	 	}
	},
	error:function(data)
	{
		
	}
});
	}
function deleteRow(Id,OrderType,UserType,Userid)
{
	var chk = confirm("Are you sure you would like to Delete?");
	if (chk) {  
	
$.ajax( {
	type : "get",
	url : '/track/EmailServlet?action=DELETEUSER',
	data : {
	PLANT : "<%=plant%>",
	ACTION : "DELETEUSER",
	Id:Id,
	OrderType:OrderType,
	USERID:Userid,
	USERTYPE:UserType
},
dataType : "json",
success : function(data) {		
	if(OrderType=="PurchaseAdminUser")
	 $("#showdata").empty();
	else if(OrderType=="PurchaseUser")
		$("#showPurchaseUser").empty();
	else if(OrderType=="EstimateAdminUser")
		 $("#showEstimateAdmin").empty();
		else if(OrderType=="EstimateUser")
			 $("#showEstimateUser").empty();
		else if(OrderType=="SalesAdminUser")
			 $("#showSalesAdmin").empty();
			else if(OrderType=="SalesUser")
				 $("#showSalesUser").empty();
			else if(OrderType=="RentalAdminUser")
				 $("#showRentalAdmin").empty();
				else if(OrderType=="RentalUser")
					 $("#showRentalUser").empty();
     var dat = "";
     dat = dat + "<table class=\"table table-bordered table-hover dataTable no-footer\">";
     dat = dat + "<thead>";
     dat = dat + "<tr class=\"table-primary\">";
     dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> S.No </th>";
     dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> User </th>";
     dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> Mail </th>";
     dat = dat + "<th style=\"background-color: rgba(60, 141, 188, 0.29);\" scope=\"col\"> Remove </th>";
     dat = dat + "</tr>";
     dat = dat + "</thead>";
     dat = dat + "<tbody>";
     $.each(data.items, function (index, listdata) {
         dat = dat + "<tr style=\"text-align: center;\">";
         dat = dat + "<td>" + listdata.Id + "</td>";
         dat = dat + "<td>" + listdata.userid + "</td>";
         dat = dat + "<td>" + listdata.usermail + "</td>";
         dat = dat + "<td><a onclick=\"deleteRow(" + listdata.Id + ",'"+OrderType+"','"+ UserType+"','"+ listdata.userid + "')\" ><i class=\"fa fa-trash-o\" style=\"color: red\"></i></a></td>";
         dat = dat + "</tr>";
     });

     dat = dat + "</tbody>";
     dat = dat + "</table>";
     if(OrderType=="PurchaseAdminUser")
     		$("#showdata").append(dat);
     else if(OrderType=="PurchaseUser")
  			$("#showPurchaseUser").append(dat);
     else if(OrderType=="EstimateAdminUser")
	         $("#showEstimateAdmin").append(dat);
 	 else if(OrderType=="EstimateUser")
	         $("#showEstimateUser").append(dat);
 	else if(OrderType=="SalesAdminUser")
        $("#showSalesAdmin").append(dat);
 else if(OrderType=="SalesUser")
        $("#showSalesUser").append(dat);
 else if(OrderType=="RentalAdminUser")
     $("#showRentalAdmin").append(dat);
else if(OrderType=="RentalUser")
     $("#showRentalUser").append(dat);
},
error:function(data)
{
	
}
});
	}
}
function onAdd(){
	
	var PurchaseIsActive =document.form1.PurchaseIsActive.value;
	var EstimateIsActive =document.form1.EstimateIsActive.value;
	var SalesIsActive =document.form1.SalesIsActive.value;
	var RentalIsActive =document.form1.RentalIsActive.value;
	
	var PurchaseCreateEmail =document.form1.PurchaseCreateEmail.value;
	var PurchaseCreateAtt =document.form1.PurchaseCreateAtt.value;
	var PurchaseCreateAttTo =document.form1.PurchaseCreateAttTo.value;
	var PurchaseApproveEmail =document.form1.PurchaseApproveEmail.value;
	var PurchaseRejectEmail =document.form1.PurchaseRejectEmail.value;
	var PurchaseisAutoEmail =document.form1.PurchaseisAutoEmail.value;
	
	var EstimateCreateEmail =document.form1.EstimateCreateEmail.value;
	var EstimateCreateAttTo =document.form1.EstimateCreateAttTo.value;
	var EstimateApproveEmail =document.form1.EstimateApproveEmail.value;
	var EstimateCreateAtt =document.form1.EstimateCreateAtt.value;
	var EstimateRejectEmail =document.form1.EstimateRejectEmail.value;
	var EstimateisAutoEmail =document.form1.EstimateisAutoEmail.value;
	
	var SalesCreateEmail =document.form1.SalesCreateEmail.value;
	var SalesCreateAttTo =document.form1.SalesCreateAttTo.value;
	var SalesApproveEmail =document.form1.SalesApproveEmail.value;
	var SalesCreateAtt =document.form1.SalesCreateAtt.value;
	var SalesRejectEmail =document.form1.SalesRejectEmail.value;
	var SalesisAutoEmail =document.form1.SalesisAutoEmail.value;
	
	var RentalCreateEmail =document.form1.RentalCreateEmail.value;
	var RentalCreateAttTo =document.form1.RentalCreateAttTo.value;
	var RentalApproveEmail =document.form1.RentalApproveEmail.value;
	var RentalCreateAtt =document.form1.RentalCreateAtt.value;
	var RentalRejectEmail =document.form1.RentalRejectEmail.value;
	var RentalisAutoEmail =document.form1.RentalisAutoEmail.value;
	
	if(PurchaseCreateEmail==null||PurchaseCreateEmail=="")
	  {
		  alert("Select Purchase Upon Create");
		  return false;
	  }
	else if(PurchaseApproveEmail==null||PurchaseApproveEmail=="")
	  {
		  alert("Select Purchase Upon Approve");
		  return false;
	  }
	else if(PurchaseRejectEmail==null||PurchaseRejectEmail=="")
	  {
		  alert("Select Purchase Upon Reject");
		  return false;
	  }
	else if(PurchaseCreateAtt==null||PurchaseCreateAtt=="")
	  {
		  alert("Select Purchase Attachment");
		  return false;
	  }
	else if(PurchaseCreateAttTo==null||PurchaseCreateAttTo=="")
	  {
		  alert("Select Purchase Attachment To");
		  return false;
	  }
	
	if(EstimateCreateEmail==null||EstimateCreateEmail=="")
	  {
		  alert("Select Estimate Upon Create");
		  return false;
	  }
	else if(EstimateApproveEmail==null||EstimateApproveEmail=="")
	  {
		  alert("Select Estimate Upon Approve");
		  return false;
	  }
	else if(EstimateRejectEmail==null||EstimateRejectEmail=="")
	  {
		  alert("Select Estimate Upon Reject");
		  return false;
	  }
	else if(EstimateCreateAtt==null||EstimateCreateAtt=="")
	  {
		  alert("Select Estimate Attachment");
		  return false;
	  }
	else if(EstimateCreateAttTo==null||EstimateCreateAttTo=="")
	  {
		  alert("Select Estimate Attachment To");
		  return false;
	  }
	
	if(SalesCreateEmail==null||SalesCreateEmail=="")
	  {
		  alert("Select Sales Upon Create");
		  return false;
	  }
	else if(SalesApproveEmail==null||SalesApproveEmail=="")
	  {
		  alert("Select Sales Upon Approve");
		  return false;
	  }
	else if(SalesRejectEmail==null||SalesRejectEmail=="")
	  {
		  alert("Select Sales Upon Reject");
		  return false;
	  }
	else if(SalesCreateAtt==null||SalesCreateAtt=="")
	  {
		  alert("Select Sales Attachment");
		  return false;
	  }
	else if(SalesCreateAttTo==null||SalesCreateAttTo=="")
	  {
		  alert("Select Sales Attachment To");
		  return false;
	  }
	
	if(RentalCreateEmail==null||RentalCreateEmail=="")
	  {
		  alert("Select Rental Upon Create");
		  return false;
	  }
	else if(RentalApproveEmail==null||RentalApproveEmail=="")
	  {
		  alert("Select Rental Upon Approve");
		  return false;
	  }
	else if(RentalRejectEmail==null||RentalRejectEmail=="")
	  {
		  alert("Select Rental Upon Reject");
		  return false;
	  }
	else if(RentalCreateAtt==null||RentalCreateAtt=="")
	  {
		  alert("Select Rental Attachment");
		  return false;
	  }
	else if(RentalCreateAttTo==null||RentalCreateAttTo=="")
	  {
		  alert("Select Rental Attachment To");
		  return false;
	  }
	
	$.ajax( {
		type : "get",
		url : '/track/EmailServlet?action=SAVEUSER',
		data : {
		PLANT : "<%=plant%>",
		ACTION : "SAVEUSER",
    	LOGIN_USER:"<%=sUserId%>",
    	PURCHASEISACTIVE:PurchaseIsActive,
    	ESTIMATEISACTIVE:EstimateIsActive,
    	SALESISACTIVE:SalesIsActive,
    	RENTALISACTIVE:RentalIsActive,
    	PURCHASECREATEEMAIL:PurchaseCreateEmail,
    	PURCHASECREATEATT:PurchaseCreateAtt,
    	PURCHASECREATEATTTO:PurchaseCreateAttTo,
    	PURCHASEAPPROVEEMAIL:PurchaseApproveEmail,
    	PURCHASEREJECTEMAIL:PurchaseRejectEmail,
    	PURCHASEISAUTOEMAIL:PurchaseisAutoEmail,
    	ESTIMATECREATEEMAIL:EstimateCreateEmail,
    	ESTIMATECREATEATT:EstimateCreateAtt,
    	ESTIMATECREATEATTTO:EstimateCreateAttTo,
    	ESTIMATEAPPROVEEMAIL:EstimateApproveEmail,
    	ESTIMATEREJECTEMAIL:EstimateRejectEmail,
    	ESTIMATEISAUTOEMAIL:EstimateisAutoEmail,
    	SALESCREATEEMAIL:SalesCreateEmail,
    	SALESCREATEATT:SalesCreateAtt,
    	SALESCREATEATTTO:SalesCreateAttTo,
    	SALESAPPROVEEMAIL:SalesApproveEmail,
    	SALESREJECTEMAIL:SalesRejectEmail,
    	SALESISAUTOEMAIL:SalesisAutoEmail,
    	RENTALCREATEEMAIL:RentalCreateEmail,
    	RENTALCREATEATT:RentalCreateAtt,
    	RENTALCREATEATTTO:RentalCreateAttTo,
    	RENTALAPPROVEEMAIL:RentalApproveEmail,
    	RENTALREJECTEMAIL:RentalRejectEmail,
    	RENTALISAUTOEMAIL:RentalisAutoEmail
	},
	dataType : "json",
	success : function(data) {
		alert(data.items);		
	},
	error:function(data)
	{
		
	}
	});	
}

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>