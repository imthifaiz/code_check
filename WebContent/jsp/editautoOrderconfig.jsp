<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%

String title = "Auto Order Alert";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.INTEGRATIONS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PRINTOUT_CONFIGURATION%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<style>
  .pay-select-icon-invoice {
    position: absolute;
    right: 22px;
    top: 12px;
    z-index: 2;
    vertical-align: middle;
    font-size: 10px;
    opacity: 0.5;
}
.extraInfo {
    border: 1px dashed #555;
    background-color: #f9f8f8;
    border-radius: 3px;
    color: #555;
    padding: 15px;
}
.offset-lg-7 {
    margin-left: 58.33333%;
}
#table2 thead {
	text-align: center;
	background: black;
	color: white;
}

#table1>tbody>tr>td, #table3>tbody>tr>td {
	border: none;
}

#table2>tbody>tr>td {
	border-bottom: 1px solid #ddd;
}
/* Style the tab */
.tab {
  overflow: hidden;
  border: 1px solid #ccc;
  background-color: #f1f1f1;
  line-height: 0.5;
}

/* Style the buttons that are used to open the tab content */
.tab button {
  background-color: inherit;
  float: left;
  border: none;
  outline: none;
  cursor: pointer;
  padding: 14px 16px;
  transition: 0.3s;
}

/* Change background color of buttons on hover */
.tab button:hover {
  background-color: #ddd;
}

/* Create an active/current tablink class */
.tab button.active {
  background-color: #ccc;
}

/* Style the tab content */
.tabcontent {
  display: none;
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-top: none;
}
.payment-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
.voucher-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -12%;
    top: 15px;
}
.emptype-action{
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -10%;
    top: 18px;
}
</style>
<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
		
	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'GSTTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
	function onClear()
	{
		   document.form1.ORDERTYPE.value="";
		   document.form1.AutoPopupDelay.value="";
            document.form1.enableOutletAutoPrintPopup.Checked = false;
            
	}
	function onAdd(){
   		document.form1.action  =  "/track/SettingsServlet?Submit=EDIT_AUTOPRINT_HDR";
   		document.form1.submit();
   	}
		
</script>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String taxbylabel= ub.getTaxByLable(plant);
	String fieldDesc=StrUtils.fString(request.getParameter("result"));  
	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String AutoPopupDelay="",enableOutletAutoPrintPopup="0",AutoPopupOrderType="";
	StrUtils strUtils = new StrUtils();
    res =  strUtils.fString(request.getParameter("result"));

	DOUtil doUtil = new DOUtil();
	     Map m= doUtil.getDOReceiptHdrDetails(plant,"Outbound order");
         if(!m.isEmpty()){
        	 AutoPopupOrderType = (String) m.get("AUTOPOPUPORDERTYPE");
        	 AutoPopupDelay = (String) m.get("AUTOPOPUPDELAY");
             enableOutletAutoPrintPopup = (String)m.get("ENABLEOUTLETAUTOPRINTPOPUP");
         }
         try {
     		action = strUtils.fString(request.getParameter("action"));
     	} catch (Exception e) {
     	}

     	if (action.equalsIgnoreCase("Clear")) {
     		action = "";
     		AutoPopupOrderType="";
             AutoPopupDelay="";
             enableOutletAutoPrintPopup="";
     	}
     	%>
     	
     	<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><label>Auto Order Alert</label></li>                                   
            </ul>             
        <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
     	<center>
     		<h2><small class="error-msg"><%=fieldDesc%></small></h2>
     	</center>
     	<form id="creationForm" class="form-horizontal" name="form1"  method="post">
		<input type="hidden" name="OrderType" value="Outbound Order">
		<input name="ORDER_TYPE_MODAL" type="hidden" value="SALES">
		<div class="form-group">
 <label class="control-label col-sm-3" for="Order header">Order Type</label>
      
      <div class="col-sm-4">          
       <input type="text" class="form-control" id="ORDERTYPE" name="ORDERTYPE" value="<%=AutoPopupOrderType%>"	>
							<span class="select-icon"
								onclick="$(this).parent().find('input[name=\'ORDERTYPE\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
							</span>
      </div>
      
      </div>
		
	<div class="form-group">
      <label class="control-label col-sm-3" for="Prepared By">Auto Order Alert Popup Time Interval</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="AutoPopupDelay" type="TEXT" value="<%=AutoPopupDelay%>"	 placeholder="Max 30 Characters" size="25" MAXLENGTH=30>
      </div> 
      
      <div class="form-inline">
      <div class="col-sm-4">
      <label class="checkbox-inline">      
        <input type = "checkbox" id = "enableOutletAutoPrintPopup" name = "enableOutletAutoPrintPopup" value = "enableOutletAutoPrintPopup" 
			<%if(enableOutletAutoPrintPopup.equals("1")) {%>checked <%}%> />Enable Outlet Auto Print Popup </label>
      </div>
      </div>
      </div>
	 <div class="form-group">        
       <div class="col-sm-offset-4 col-sm-8">
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" >Clear</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>&nbsp;&nbsp;
      	</div>
      </div>
      </form>
    </div>		 
   </div>
</div> 

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip(); 

    /* Order Type Auto Suggestion */
	$('#ORDERTYPE').typeahead({
		  hint: true,
		  minLength:0,
		  searchOnFocus: true
		},
		{
		  display: 'ORDERTYPE',
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						ACTION : "GET_ORDERTYPE_DATA",
						Type : "SALES",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.ORDERTYPES);
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
		    return '<p>' + data.ORDERTYPE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();
			}
			menuElement.after( '<div class="ordertypeAddBtn footer"  data-toggle="modal" data-target="#orderTypeModal"><a href="#"> + New Order Type</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();

		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			$('.ordertypeAddBtn').show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.ordertypeAddBtn').hide();}, 150);
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
});
function orderTypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#ORDERTYPE").typeahead('val', data.ORDERTYPE);
	}
}
</script>


<%@include file="../jsp/newOrderTypeModal.jsp"%>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>


