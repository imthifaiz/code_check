<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%-- <%@ page import="com.track.dao.PrdDeptDAO"%> --%>
<%@ page import="com.track.dao.ClearAgentDAO"%>
<%@ page import="com.track.dao.TransportModeDAO"%>
<%@ page import="com.track.dao.ClearingAgentTypeDAO"%>
<%@ page import="com.track.db.object.ClearingAgentTypeDET"%>
<%@ page import="com.track.db.util.PrdClassUtil"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="org.apache.commons.fileupload.FileItem"%>
<%@ page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ include file="header.jsp"%>

<%
String title = "Create Clearing Agent";
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
<<style>
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


function onNew(){
	document.form1.CLEARING_AGENT_ID.value = ""; 
	document.form1.CLEARING_AGENT_NAME.value = "";
}
function onAdd(){
   var AGENT_ID   = document.form1.CLEARING_AGENT_ID.value;
   var AGENT_NAME = document.form1.CLEARING_AGENT_NAME.value;
    if(AGENT_ID == "" || AGENT_ID == null) {alert("Please Enter Clearing Agent ID ");document.form1.CLEARING_AGENT_ID.focus(); return false; }
    if(AGENT_NAME == "" || AGENT_NAME == null) {alert("Please Enter Clear Agent Name");document.form1.CLEARING_AGENT_NAME.focus(); return false; }
   document.form1.action  = "../jsp/CreateClearAgent.jsp?action=ADD";
   document.form1.submit();
}


function onGenID(){

	$.ajax({
		type: "GET",
		url: "../clearagent/Auto_ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#CLEARING_AGENT_ID").val(data.AGENTID);
		},
		error: function(data) {
			alert('Unable to generate Clearing Agent ID. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
 

}

</script>
<%
	session = request.getSession();
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	String res = "";

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "disabled";
	String sUpdateEnb = "enabled";
	sAddEnb = "enabled";
	String action = "";
	TblControlDAO _TblControlDAO =new TblControlDAO();
	String sAgentId = "", sDepId = "", sAgentName = "",sSAVE_RED;

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	DateUtils dateutils = new DateUtils();
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	String result = (String) request.getAttribute("result");
	sAgentId = strUtils.fString(request.getParameter("CLEARING_AGENT_ID"));
	sAgentName = strUtils.fString(request.getParameter("CLEARING_AGENT_NAME"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	if (sAgentId.length() <= 0)
		sAgentId = strUtils.fString(request.getParameter("CLEARING_AGENT_ID1"));

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sAgentId = "";
		sAgentName = "";
		sDepId = "";
	} 

	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {
		String strpath = "", fileLocation="",filetempLocation = "";
		ClearingAgentTypeDAO clearingAgentTypeDAO = new ClearingAgentTypeDAO();
		List transport = new ArrayList(), contactname = new ArrayList(), telno = new ArrayList(),email = new ArrayList();
		int transportCount  = 0, contactnameCount  = 0, telnoCount  = 0, emailCount  = 0;
		String pltCountry = new PlantMstDAO().getCOUNTRY_TIMEZONE(plant);//Get Country Based Date -Azees 07.22
		if(pltCountry.equalsIgnoreCase(""))
			pltCountry="Asia/Singapore";
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			try {
				

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				
				String ITEM = "",sLogo="";;
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {
						if (item.getFieldName().equalsIgnoreCase("CLEARING_AGENT_ID")) {
							sAgentId = StrUtils.fString(item.getString());
						}
						if (item.getFieldName().equalsIgnoreCase("CLEARING_AGENT_NAME")) {
							sAgentName = StrUtils.fString(item.getString());
						}
						if (item.isFormField()) {
							if (item.getFieldName().equalsIgnoreCase("transport")) {
								transport.add(transportCount, StrUtils.fString(item.getString()).trim());
								transportCount++;
							}
						}
						if (item.isFormField()) {
							if (item.getFieldName().equalsIgnoreCase("CONTACTNAME")) {
								contactname.add(contactnameCount, StrUtils.fString(item.getString()).trim());
								contactnameCount++;
							}
						}
						if (item.isFormField()) {
							if (item.getFieldName().equalsIgnoreCase("TELNO")) {
								telno.add(telnoCount, StrUtils.fString(item.getString()).trim());
								telnoCount++;
							}
						}
						if (item.isFormField()) {
							if (item.getFieldName().equalsIgnoreCase("EMAIL")) {
								email.add(emailCount, StrUtils.fString(item.getString()).trim());
								emailCount++;
							}
						}
						
					}

				}
					} catch (Exception e) {

					}
		}
		
		result="";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT, sPlant);
		ht.put("CLEARING_AGENT_ID", sAgentId);
		if (!(new ClearAgentDAO().isExistsClearagent(ht))) // if the designation exists already
		{
			ht.put(IDBConstants.PLANT, sPlant);
			ht.put("CLEARING_AGENT_ID", sAgentId);
			ht.put("CLEARING_AGENT_NAME", strUtils.InsertQuotes(sAgentName));
			ht.put(IConstants.ISACTIVE, "Y");
			ht.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
			ht.put(IDBConstants.LOGIN_USER, sUserId);
            
			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE", "CREATE CLEARING AGENT");
			htm.put("RECID", "");
			htm.put("ITEM",sAgentId);
			htm.put("REMARKS",strUtils.InsertQuotes( sAgentName));
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
             
   		  	boolean updateFlag;
   			if(sAgentId!="CA001"){	
   			boolean exitFlag = false;
   			Hashtable htv = new Hashtable();				
   			htv.put(IDBConstants.PLANT, plant);
   			htv.put(IDBConstants.TBL_FUNCTION, "CLEARING_AGENT");
   			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
   			if (exitFlag) 
     		    updateFlag=_TblControlDAO.updateSeqNo("CLEARING_AGENT",plant);
   			else
   			{
   				boolean insertFlag = false;
   				Map htInsert=null;
               	Hashtable htTblCntInsert  = new Hashtable();           
               	htTblCntInsert.put(IDBConstants.PLANT,plant);          
               	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CLEARING_AGENT");
               	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"CA");
                	htTblCntInsert.put("MINSEQ","0000");
                	htTblCntInsert.put("MAXSEQ","9999");
               	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
               	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
               	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
               	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
   			}
   		}					
			
			boolean itemInserted = new ClearAgentDAO().insertClearagentMst(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if(itemInserted){
			  for(int i =0 ; i < transport.size() ; i++){
					TransportModeDAO transportmodedao = new TransportModeDAO();
					String transportmode = "";
					
						transportmode = transportmodedao.getTransportModeByName(plant,(String)transport.get(i));
					
        		  /* String tranportid = (String)transport.get(i);
        		 if(!tranportid.equalsIgnoreCase("0")){ */
        			 ClearingAgentTypeDET clearingAgentTypeDET = new ClearingAgentTypeDET();
        			 clearingAgentTypeDET.setPLANT(plant);
        		//	 clearingAgentTypeDET.setID(0);
        			 clearingAgentTypeDET.setCLEARING_AGENT_ID(sAgentId);
        			 clearingAgentTypeDET.setTRANSPORTID(Integer.valueOf(transportmode));
        		//	 clearingAgentTypeDET.setTRANSPORTID((String)transport.get(i));
        			 clearingAgentTypeDET.setCONTACTNAME((String)contactname.get(i));
        			 clearingAgentTypeDET.setTELNO((String)telno.get(i));
        			 clearingAgentTypeDET.setEMAIL((String)email.get(i));
        			 clearingAgentTypeDET.setCRAT(dateutils.getDate());
        			 clearingAgentTypeDET.setCRBY(sUserId);
        			 clearingAgentTypeDAO.addClearAgentTypedet(clearingAgentTypeDET);
        	  	 }
        	  }
			  
		//	}

			if (itemInserted && inserted) {
				sSAVE_RED = "Updated"; 
			} else {
				sSAVE_RED="Failed to add New Clearing Agent";
			}
		} else {
			sSAVE_RED="Clearing Agent Exists already.Try again";
		}

	}
	
	if(!result.equalsIgnoreCase("")) {
		sSAVE_RED = "";
		res = "<font class = " + IDBConstants.FAILED_COLOR
		+ ">"+result+"</font>";
		}	
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../clearagent/summary"><span class="underline-on-hover">Clearing Agent Summary</span></a></li>                        
                <li><label>Create Clearing Agent</label></li>                                   
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
  <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Clearing Agent ID">Clearing Agent ID</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="CLEARING_AGENT_ID" id="CLEARING_AGENT_ID" type="TEXT" value="<%=sAgentId%>" placeholder="Max 50 Characters" onkeypress="return blockSpecialChar(event)"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();" <%=sAddEnb%>>
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="CLEARING_AGENT_ID1" value="">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      </div>
    </div>
     
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Clearing Agent Desc">Clearing Agent Name</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="CLEARING_AGENT_NAME" type="TEXT" placeholder="Max 100 Characters" value="<%=sAgentName%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
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
						<tbody>
							<tr>
								<td class="text-center">
									<input type="hidden" name="TRANSPORTID" value="">
									<input type="hidden" name="TRANSID" value="">
									<input class="form-control text-left transport " name="transport" type="text" placeholder="Enter Transport Mode" maxlength="50"></td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="CONTACTNAME" maxlength="100" placeholder="Enter Contact Name" value="">
								</td>
								<td class="text-center">
									<input class="form-control text-left" name="TELNO" placeholder="Enter Contact Number" maxlength="30"></td>
								<td class="text-center">
									<input  name="EMAIL" class="form-control text-left" maxlength="50" placeholder="Enter Email">
								</td>
							
							</tr>
						</tbody>
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
        
    
    
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>>Save</button>&nbsp;&nbsp;

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
    if(document.form1.SAVE_RED.value!="")
	{
	document.form1.action  = "../clearagent/summary?PGaction=View&result=Clearing Agent Created Successfully";
	document.form1.submit();
	}
    else{
		document.form1.action =  "../clearagent/summary?PGaction=View&result=Clearing Agent Created Successfully";
		document.form1.submit();
}
    }

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
		 //   return '<p>' + data.TRANSPORT_MODE + '</p>';
		    return '<div onclick="document.form1.TRANSID.value = \''+data.ID+'\'"><p class="item-suggestion">'+ data.TRANSPORT_MODE +'</p></div></div>';
		//    return '<div onclick="settransportiddays(this,\''+data.ID+'\',\''+data.CONTACTNAME+'\')"><p>' + data.TELNO + '</p>';
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
		});
});

function addRow() {

	var body = "";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="TRANSPORTID" value="">';
	body += '<input type="hidden" name="TRANSID" value="">';
	body += '<input class="form-control text-left transport" name="transport" type="text" placeholder="Enter Transport Mode" maxlength="50"></td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left " type="text" name="CONTACTNAME" maxlength="100" placeholder="Enter Contact Name" value="">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left " type="text" name="TELNO" maxlength="30" placeholder="Enter Contact Number" value="">';
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
		 //   return '<p>' + data.TRANSPORT_MODE + '</p>';
		    return '<div onclick="document.form1.TRANSID.value = \''+data.ID+'\'"><p class="item-suggestion">'+ data.TRANSPORT_MODE +'</p></div></div>';
		//    return '<div onclick="settransportiddays(this,\''+data.ID+'\',\''+data.CONTACTNAME+'\')"><p>' + data.TELNO + '</p>';
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
		});
}
function transportCallback(data){
	if(data.STATUS="SUCCESS"){
		$(".transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
	}
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

/* function settransportiddays(obj,id,tdays){
	var count = "0";
	$("input[name ='TRANSPORTID']").each(function() {
		if($(this).val() == id){
			count = "1";
	    }
	});
	if(count == "0"){
		$(obj).closest('tr').find("input[name ='TRANSPORTID']").val(id);
		$(obj).closest('tr').find("input[name ='CONTACTNAME']").val(tdays);
	}else{
		alert("Transport type alredy selected");
		$(obj).closest('tr').remove();
	}
} */
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   

 
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>



