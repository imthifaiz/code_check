<!-- Created By IMTHI - 15.09.22  -->
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.*"%>
<%-- <%@ page import="com.track.dao.PrdDeptDAO"%> --%>
<%@ page import="com.track.dao.ClearAgentDAO"%>
<%@ page import="com.track.db.object.ClearingAgentTypeDET"%>
<%@ page import="com.track.dao.ClearingAgentTypeDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Clearing Agent";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<style>
.type-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -25%;
	top: 15px;
}
</style>
<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=420,left = 200,top = 184');
}
function onNew(){
	document.form1.CLEARING_AGENT_ID.value = ""; 
	document.form1.CLEARING_AGENT_NAME.value = "";

   
}

function onUpdate(){
 
    var DESID   = document.form1.CLEARING_AGENT_ID.value;
    var DES_DESC = document.form1.CLEARING_AGENT_NAME.value;
    if(DESID == "" || DESID == null) {alert("Please Enter Clearing Agent ID ");document.form1.CLEARING_AGENT_ID.focus(); return false; }
    if(DES_DESC == "" || DES_DESC == null) {alert("Please Enter Clearing Agent Name");document.form1.CLEARING_AGENT_NAME.focus(); return false; }  
    var radio_choice = false;
    
    // Loop from zero to the one minus the number of radio button selections
    for (i = 0; i < document.form1.ACTIVE.length; i++)
    {
    if (document.form1.ACTIVE[i].checked)
    radio_choice = true; 
    }
    
    if (!radio_choice)
    {
    // If there were no selections made display an alert box 
    alert("Please select Active or non Active mode.")
    return (false);
    }
    
    var chkmsg=confirm("Are you sure you would like to update?");
    if(chkmsg){
   document.form1.action  = "../jsp/MaintClearAgent.jsp?action=UPDATE";
   document.form1.submit();}
    else
    { return false;
    }
}
function onDelete() {
	var DESID   = document.form1.CLEARING_AGENT_ID.value;
	if (DESID == "" || DESID == null) {
		alert("Please Enter Clearing Agent ID");
		return false;
	}
	var chkmsg = confirm("Are you sure you would like to delete?");
	if (chkmsg) {
		document.form1.action = "../jsp/MaintClearAgent.jsp?action=DELETE";
		document.form1.submit();
	} else {
		return false;
	}
}


function onView(){
    var DESID   = document.form1.CLEARING_AGENT_ID.value;
   if(DESID == "" || DESID == null) {alert("Please Enter Clearing Agent ID "); return false; }

   document.form1.action  = "../jsp/MaintClearAgent.jsp?action=VIEW";
   document.form1.submit();
}


</script>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "disabled";
	String sUpdateEnb = "enabled";
	sAddEnb = "enabled";
	String action = "";
	String sAgentId = "", sAgentName = "", isActive = "",sSAVE_RED,sSAVE_REDELETE;

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	ClearingAgentTypeDAO clearingAgentTypeDAO = new ClearingAgentTypeDAO();

	DateUtils dateutils = new DateUtils();
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	String result = (String) request.getAttribute("result");
	sAgentId = strUtils.fString(request.getParameter("CLEARING_AGENT_ID"));
	sAgentName = strUtils.fString(request.getParameter("CLEARING_AGENT_NAME"));
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
	 String pltCountry = new PlantMstDAO().getCOUNTRY_TIMEZONE(plant);//Get Country Based Date -Azees 07.22
		if(pltCountry.equalsIgnoreCase(""))
			pltCountry="Asia/Singapore";
		List<ClearingAgentTypeDET>  clearagentdet = new ArrayList<ClearingAgentTypeDET>();
	if (sAgentId.length() <= 0)
		sAgentId = strUtils.fString(request.getParameter("CLEARING_AGENT_ID1"));
if(sAgentId.length()>0) {
	ArrayList locQryList =  new ClearAgentDAO().getClearingAgentDetails(sAgentId,plant,"");
	if (locQryList.size() > 0) {
		for(int i =0; i<locQryList.size(); i++) {
		Map arrCustLine = (Map)locQryList.get(i);

		}
	}
}
List customeruserlist= new ClearingAgentTypeDAO().getclearAgentDetails(sAgentId, plant, "");

/* TransportModeDAO transportmodedao = new TransportModeDAO();
String transportmode;
int trans = String.valueOf(transport);
if(trans > 0){
	transportmode = transportmodedao.getTransportModeById(plant,trans);
}
else{
transportmode = "";
} */
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sAgentId = "";
		sAgentName = "";
	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {
     result="";
    
		sAddEnb = "disabled";
		Hashtable ht = new Hashtable();
		String[] transport = request.getParameterValues("TRANSPORTID");
		String[] contactname = request.getParameterValues("CONTACTNAME");
		String[] telno = request.getParameterValues("TELNO");
		String[] email = request.getParameterValues("EMAIL");
		ht.put("CLEARING_AGENT_ID", sAgentId);
		ht.put(IDBConstants.PLANT, sPlant);
		boolean chkagentid=false;
		String chkcustomeruserid="";
		if(transport !=null) {
			for(int i =0 ; i < transport.length ; i++){
			chkcustomeruserid =(String)transport[i];
			if(!chkcustomeruserid.equalsIgnoreCase("")) {
				chkagentid=true;
				break;
			}
			}					
			}
//		if(!chkagentid) {
		
		if ((new ClearAgentDAO().isExistsClearagent(ht))) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put("CLEARING_AGENT_NAME",  strUtils.InsertQuotes(sAgentName));
			htUpdate.put(IConstants.ISACTIVE, isActive);
			htUpdate.put(IDBConstants.UPDATED_AT, new DateUtils().getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, sUserId);

			Hashtable htCondition = new Hashtable();
			htCondition.put("CLEARING_AGENT_ID", sAgentId);
			htCondition.put(IDBConstants.PLANT, sPlant);

			
			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE", "UPDATE CLEARING AGENT");
			htm.put("RECID", "");
			htm.put("ITEM",sAgentId);
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("REMARKS",  strUtils.InsertQuotes(sAgentName));
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean Updated = new ClearAgentDAO().updateClearingAgentMst(htUpdate,
					htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
		//	if(Updated){
			if(transport != null){
				Boolean chktrans=clearingAgentTypeDAO.DeleteClearagentdet(plant,(sAgentId));
				for(int i =0 ; i < transport.length ; i++){
			/* 	 TransportModeDAO transportmodedao = new TransportModeDAO();
				String transportmode = "";
				
					transportmode = transportmodedao.getTransportModeByName(plant,(String)transport[i]); */ 
				
      			 ClearingAgentTypeDET clearingAgentTypeDET = new ClearingAgentTypeDET();
      			 clearingAgentTypeDET.setPLANT(plant);
      			 clearingAgentTypeDET.setCLEARING_AGENT_ID(sAgentId);
      			 clearingAgentTypeDET.setTRANSPORTID(Integer.valueOf((String)transport[i]));
      			 clearingAgentTypeDET.setCONTACTNAME((String)contactname[i]);
      			 clearingAgentTypeDET.setTELNO((String)telno[i]);
      			 clearingAgentTypeDET.setEMAIL((String)email[i]);
      			 clearingAgentTypeDET.setCRAT(dateutils.getDate());
      			 clearingAgentTypeDET.setCRBY(sUserId);
      			 clearingAgentTypeDAO.addClearAgentTypedet(clearingAgentTypeDET);
      	  }
			}
			
		//	}
			
			if (true) {
				 sSAVE_RED = "Update";
				
			}
		else {
				sSAVE_RED="Failed to Update Clearing Agent";
			
			}
		}
	//	}
	
		else {
			sSAVE_RED="Clearing Agent  doesn't not Exists. Try again";
			

		}

	}
    
		
	
	
	else if(action.equalsIgnoreCase("DELETE")){
		result="";
		Hashtable htCondition = new Hashtable();
		htCondition.put("CLEARING_AGENT_ID", sAgentId);
		htCondition.put(IDBConstants.PLANT, sPlant);
		
			if(new ClearAgentDAO().isExistsClearagent(htCondition))
		 	{
				boolean flag = new ClearAgentDAO().deleteClearingAgent(htCondition);
				
				MovHisDAO mdao = new MovHisDAO(sPlant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", sPlant);
				htm.put("DIRTYPE", "DELETE CLEARING AGENT");
				htm.put("RECID", "");
				htm.put("ITEM",sAgentId);
				htm.put("UPBY", sUserId);
				htm.put("CRBY", sUserId);
				htm.put("CRAT", dateutils.getDateTime());
				htm.put("REMARKS",  strUtils.InsertQuotes(sAgentName));
				htm.put("UPAT", dateutils.getDateTime());
				htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));

				 flag = mdao.insertIntoMovHis(htm);
				
				if(flag)
					{
					sSAVE_REDELETE = "Delete";
						}
				else {
					sSAVE_REDELETE="Failed to Delete Clearing Agent";
      				}
			}else{
				sSAVE_REDELETE="Clearing Agent doesn't  Exists. Try again";
	        }
	}
	
	if(!result.equalsIgnoreCase("")) {
		sSAVE_RED = "";
		sSAVE_REDELETE = "";
		res = "<font class = " + IDBConstants.FAILED_COLOR+ ">"+result+"</font>";
		}	
	
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../clearagent/summary"><span class="underline-on-hover">Clearing Agent Summary</span></a></li>                    
                <li><label>Edit Clearing Agent</label></li>                                   
            </ul>
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../clearagent/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
   <CENTER><strong><%=res%></strong></CENTER>

<form class="form-horizontal" name="form1" method="post" id="form">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for=" Clearing Agent ID">Clearing Agent ID</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="CLEARING_AGENT_ID" type="TEXT"  value="<%=sAgentId%> "
			size="50" MAXLENGTH=50 class="form-control" readonly>
			
        </div>
  		<INPUT type="hidden" name="CLEARING_AGENT_ID1" value="">
  		<!-- <INPUT type="hidden" name="TRANSID" value=""> -->
  		 <INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      <INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
  	</div>
   </div>
   
<div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Clearing Agent Name">Clearing Agent Name</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="CLEARING_AGENT_NAME" placeholder="Max 100 Characters" type="TEXT"  value="<%=sAgentName%>"
			size="50" MAXLENGTH=100>
      </div>
      </div>
      <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y"<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N"<%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div> 
</div>
      <div id="leavedet">
        <br>
        	<div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table clearagent-table">
						<thead>
							<tr>
								<th>Transport Mode</th>
								<th>Contact Name</th>
								<th>Contact Number</th>
								<th>Email</th>
							</tr>
						</thead>
						<%if(!customeruserlist.isEmpty()){ %>
						<tbody>
						<%for(int i =0; i<customeruserlist.size(); i++) {
							Map arrCurrLine = (Map)customeruserlist.get(i);
							// String decryptpassword = "";
						//	String transport;
							 String sTransport=(String)arrCurrLine.get("TRANSPORT"); 
							 String sTransportid=(String)arrCurrLine.get("TRANSPORTID"); 
							 String sContactname = (String)arrCurrLine.get("CONTACTNAME");
							 String sTelno = (String)arrCurrLine.get("TELNO");
							 String sEmail = (String)arrCurrLine.get("EMAIL");
						%>
							<tr>
								<td class="text-center">
									<input type="hidden" name="TRANSPORTID" value="<%= sTransportid %>">
									<input class="form-control text-left" name="transport" type="text" placeholder="Enter Transport Mode" maxlength="50"  value="<%= sTransport %>" readonly></td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="CONTACTNAME" maxlength="100" placeholder="Enter Contact Name" value="<%=sContactname%>" readonly>
								</td>
								<td class="text-center">
									<input class="form-control text-left " name="TELNO" maxlength="30" placeholder="Enter Contact Number" value="<%=sTelno%>" readonly></td>
								<td class="text-center">
									<input  name="EMAIL" class="form-control text-left" maxlength="50" placeholder="Enter Email" value="<%=sEmail%>" readonly>
								</td>
							
							</tr>
							<%} %>
						</tbody>
						<%}else{%>
						<tbody>
								<tr>
								<td class="text-center">
									<input type="hidden" name="TRANSPORTID" value="">
									<input class="form-control text-left transport" name="transport" type="text" placeholder="Enter Transport Mode" maxlength="50"></td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="CONTACTNAME" maxlength="100" placeholder="Enter Contact Name" value="" >
								</td>
								<td class="text-center">
									<input class="form-control text-left " name="TELNO" maxlength="30" placeholder="Enter Contact Number" value="" ></select>
								<td class="text-center">
								<span class="glyphicon glyphicon-remove-circle type-action" aria-hidden="true"></span>
									<input  name="EMAIL" class="form-control text-left" maxlength="50" placeholder="Enter Email" value="">
								</td>
							
							</tr>
						
						</tbody>
						<%}%>
					</table>
			</div>
			<div class="form-group">
					<div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;margin-left: 5%;"
								onclick="addRow()">+ Add New Transport Detail</a>
						</div>
					</div>
			</div>
        
        </div>
       
   
		

    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">

      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" onClick="onUpdate();" <%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onDelete();" <%=sDeleteEnb%>>Delete</button>&nbsp;&nbsp;
      	
     </div>
    </div>
  </form>
</div>
</div>
</div>					

<%@include file="newTransportModeModal.jsp"%>

 <script>

 
 $(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();
	    if(document.form1.SAVE_RED.value!="") {
	    	if(document.form1.SAVE_RED.value=="Updated") {  
		document.form1.action  = "../clearagent/summary?PGaction=View&result=Clearing Agent Updated Successfully";	   
	    document.form1.submit();
		}
	    else{
	    		document.form1.action = "../clearagent/summary?PGaction=View&result=Clearing Agent Updated Successfully";
	    		document.form1.submit();
	    }
	    }
	    
	});
	$(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();
	    if(document.form1.SAVE_REDELETE.value!=""){
	    	if(document.form1.SAVE_REDELETE.value=="Delete") {
	    	document.form1.action  = "../clearagent/summary?PGaction=View&result=Clearing Agent Deleted Successfully";
	    	 document.form1.submit();
		}
	    	else{
	    		document.form1.action = "../clearagent/summary?PGaction=View&result=Clearing Agent Deleted Successfully";
	    		document.form1.submit();
	    		}
	    		}
	});

	function addRow() {

		var body = "";
		body += '<tr>';
		body += '<td class="text-center">';
		body += '<input type="hidden" name="TRANSPORTID" value="">';
		body += '<input class="form-control text-left transport" name="transport" type="text" placeholder="Enter Transport Mode" maxlength="50"></td>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left" type="text" name="CONTACTNAME" maxlength="100" placeholder="Enter Contact Name" value="">';
		body += '</td>';
		body += '<td class="text-center">';
		body += '<input class="form-control text-left" type="text" name="TELNO" maxlength="30"  placeholder="Enter Contact Number" value="">';
		body += '</td>';
		body += '<td class="text-center grey-bg" style="position:relative;">';
		body += '<span class="glyphicon glyphicon-remove-circle type-action" aria-hidden="true"></span>';
		body += '<input type="text"  name="EMAIL" maxlength="50" class="form-control text-left"  placeholder="Enter Email" value="">';
		body += '</td>';
		body += '</tr>';
		$(".clearagent-table tbody").append(body);
		removerowclasses();
		addrowclasses();
	}
	$(".clearagent-table tbody").on('click', '.type-action', function() {
		$(this).parent().parent().remove();
	});

	function removerowclasses(){
		$(".transport").typeahead('destroy');
	}
	function addrowclasses(){
		$('.transport').typeahead({
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
			//    return '<p> ' + data.TRANSPORT_MODE + '</p>';
			    return '<div onclick="settransportid(this, \''+data.ID+'\')"><p class="item-suggestion">'+ data.TRANSPORT_MODE +'</p></div></div>';
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
			//	$("input[name=TRANSPORTID]").val(selection.ID);
				
			});
	}
	function transportCallback(data){
		if(data.STATUS="SUCCESS"){
			$(".transport").typeahead('val', data.transport);
			$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
		}
	}
function settransportid(obj,id){
	$(obj).closest('tr').find('input[name = "TRANSPORTID"]').val(id);
}

function isNumber(evt) {	
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    
    if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
    	if( (charCode!=43 && charCode!=32 && charCode!=45))
    		{
    		
        alert("  Please enter only Numbers.  ");
        return false;
    		}
    	}
    return true;
}

	
		
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();  

  //transport
	$('.transport').typeahead({
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
		//    return '<p>' + data.TRANSPORT_MODE + '</p>';
		    	 return '<div onclick="settransportid(this, \''+data.ID+'\')"><p class="item-suggestion">'+ data.TRANSPORT_MODE +'</p></div></div>';
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
		//	$("input[name=TRANSPORTID]").val(selection.ID);
		}); 
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>



