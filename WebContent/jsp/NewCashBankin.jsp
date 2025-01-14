<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.POSBankInByPayModeDAO"%>
<%@ page import="com.track.db.object.POSBankInByPayMode"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<!-- IMTIZIAF -->
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.OutletBeanDAO"%>
<!-- END -->
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%! @SuppressWarnings({"rawtypes", "unchecked"}) %>
<%
String title = "POS Cash Bank IN";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
		<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.POS_REPORT%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	List locQryList = new ArrayList();
	POSBankInByPayModeDAO pOSBankInByPayModeDAO = new POSBankInByPayModeDAO();
	
	String fieldDesc = "";
	String PLANT = "";
	String html = "";
	int Total = 0;
	String SumColor = "";
	
	String FROM_DATE="",TO_DATE="",status="",TERMINAL_CODE ="",sTerminalCode="", OUTLET_CODE="";
	
	
	boolean flag = false;
	session = request.getSession();
	PLANT = session.getAttribute("PLANT").toString();
	String USERID= session.getAttribute("LOGIN_USER").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String msg =  StrUtils.fString(request.getParameter("Msg"));
	String types =  StrUtils.fString(request.getParameter("srctype"));
	fieldDesc =  StrUtils.fString(request.getParameter("result"));
	String PGaction =  StrUtils.fString(request.getParameter("PGaction"));
	
	String ENABLE_POS =new PlantMstDAO().getispos(PLANT);
	String ISTAXREGISTRED =new PlantMstDAO().getistaxregistred(PLANT);
	Map arrCustLine = new HashMap();
	String finalString="";
	
	FROM_DATE    = strUtils.fString(request.getParameter("FROM_DATE"));
	String curDate =du.getDateMinusDays();
	FROM_DATE=du.getDateinddmmyyyy(curDate);
	status  = strUtils.fString(request.getParameter("STATUS"));
	sTerminalCode = strUtils.InsertQuotes(strUtils.fString(request.getParameter("TERMINALCODE")));
	
	//IMTIZIAF
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
	String zerovalue = StrUtils.addZeroes(0,numberOfDecimal);
	String collectionDate=DateUtils.getDate();
// 	ArrayList arrCust = new OutletBeanDAO().PaymentModeMstlist(PLANT);
	ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	//END
	
	POSBankInByPayMode pOSBankInByPayMode = pOSBankInByPayModeDAO.getbypaymodeandmaxid(PLANT, "Cash");
	String bankaccount = "";
	String chargeaccount = "";
	if(pOSBankInByPayMode.getPLANT() == null){	
	}else{
		bankaccount = pOSBankInByPayMode.getBANKINACCOUNT();
		chargeaccount = pOSBankInByPayMode.getCHARGESACCOUNT();
	}
	
%>

<script>
	var subWin = null;
	function popUpWin(URL) {
		subWin = window
				.open(
						URL,
						'GroupSummary',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}

	function ExportReport(){
		document.form.action="/track/deleveryorderservlet?Submit=ExportExcelPOSreturnSummary";
		document.form.submit();
	} 
	
/* 	var headerData = ["Header 1", "Header 2", "Header 3"];
	var headerRow = document.querySelector("#headerRow");

	headerData.forEach(header => {
	var th = document.createElement("th");
	th.innerHTML = header;
	headerRow.appendChild(th);
	});
	
	for(let i = 0; i < headerData.length; i++) {
		  let th = document.createElement("th");
		  th.innerHTML = headerData[i];
		  headerRow.appendChild(th);
		} */
</script>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box">
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../posreports/reports"><span class="underline-on-hover">POS Reports</span> </a></li>
                <li><a href="../posreports/revenuereports"><span class="underline-on-hover">POS Revenue Report</span> </a></li>
                <li><label>Cash Bank In</label></li>                                   
            </ul>
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
             <!--  <button type="button" class="btn btn-default"
						onClick="window.location.href='../posreports/cashbankin'">
						Cash Bank In</button>
					&nbsp;
				</div> -->
                            
                           <!--  <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="javascript:ExportReport();">
						Export All Data</button>
					&nbsp;
				</div>
				<div class="btn-group" role="group">
            	<button type="button" class="btn btn-default printMe" onclick="PrintTable();"
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
            	</div> -->
              <h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../posreports/revenuereports'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
		
 <div class="box-body">
				<FORM class="form-horizontal" name="form">
					<input type="hidden" name="xlAction" value=""> <input
						type="hidden" name="PGaction" value="View"> <input
						type="hidden" name="plant" value="<%=PLANT%>"> <input
						type="hidden" name="TERMINAL_CODE" value=""> <INPUT
						type="hidden" name="TERMINALCODE" value="<%=sTerminalCode%>">
					<%-- <input type="hidden" name="srctype" value="<%=searchtype%>"> --%>
					<input type="hidden" name="srctype" value="">

					<div style="margin-top: 3%;">
						<div class="row">
							<!-- <label class="control-label col-sm-2" for="Product ID">Search</label> -->
							<div class="col-sm-2">
								<input type="hidden" value="hide" name="search_criteria_status"
									id="search_criteria_status" /> <input name="FROM_DATE"
									type="text" value="<%=FROM_DATE%>" id="FROM_DATE" size="30"
									MAXLENGTH=20 class="form-control datepicker" READONLY
									placeholder="FROM DATE">
							</div>
							<div class="col-sm-2">
								<input class="form-control datepicker" name="TO_DATE"
									type="text" id="TO_DATE" value="<%=TO_DATE%>" size="30"
									MAXLENGTH=20 READONLY placeholder="TO DATE">
							</div>
							<div class="col-sm-3 ac-box">
								<input type="hidden" name="OUTLET_CODE" value=""> <input
									type="hidden" name="OUTLET_CODE1" value=""> <INPUT
									name="OUTLET_NAME" id="OUTLET_NAME" type="TEXT"
									value="<%=OUTLET_CODE%>" size="20" placeholder="OUTLET NAME"
									MAXLENGTH=100 class="ac-selected form-control"> <span
									class="select-icon"
									onclick="$(this).parent().find('../jsp/input[name=\'OUTLET_NAME\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
							</div>
							<div class="col-sm-3 ac-box">
								<INPUT name="TERMINALNAME" id="TERMINALNAME" type="TEXT"
									value="<%=TERMINAL_CODE%>" placeholder="TERMINAL" size="20"
									MAXLENGTH=100 class="ac-selected form-control"> <span
									class="select-icon"
									onclick="$(this).parent().find('../jsp/input[name=\'TERMINALNAME\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
							</div>
							<div class="col-sm-2 ac-box">
								<button type="button" class="btn btn-success"
									onClick="BANKIN()">Search</button>
								&nbsp;
							</div>
						</div>
						<div class="row">
							<div class="col-sm-2" style="margin-top: 2%;">
								<input type="checkbox" id="alldays" name="alldays" onclick="allowallday(this)"><label for="alldays">  All Days</label>
							</div>
						</div>


					</div>
				</form>

 


<!-- <div style="overflow-x:auto;"> -->
				<div class="bs-example" style="height: 245px;overflow-y: scroll;margin-top: 2%;">
					<div class="row"
						style="margin: 0px; width: 95%; margin-left: 15px;">
						<table class="table table-bordered line-item-table cashin-table">
							<thead>
								<tr>
									<th>Select</th>
									<th>Date</th>
									<th>Outlet</th>
									<th>Terminal</th>
									<th>Employee</th>
									<th>Received Cash</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td class="text-center"></td>
									<td class="text-center"></td>
									<td class="text-center"></td>
									<td class="text-center"></td>
									<td class="text-center"></td>
									<td class="text-center"></td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<!--     </div> -->
				
				<div >
				<FORM class="form-vertical form2" id="form2" name="form2">
					<input type="hidden" name="payidlist" value="">
					<div class="row">
						<div class="col-lg-4">
							<label class="required">Pay Mode</label>
							<div class="input-group"> 
								<input class="form-control" name="bipaymode" id="bipaymode" type="text" value="Cash" disabled>
							</div>
						</div>
						<div class="col-lg-4">
							<label class="required">Bank In Date</label>
							<div class="input-group"> 
								<input type="text" class="form-control datepicker" id="depositedate" value="" name="depositedate" readonly>
							</div>
						</div>
						<div class="col-lg-4">
						<label class="required">Amount</label>
							<div class="input-group"> 
								<input class="form-control" name="biamount" id="biamount" type="text" value="<%=zerovalue%>" disabled>
							</div>
						</div>
					</div>
					<div class="row" style="margin-top: 2%;">
					<div class="col-lg-4">
						<label class="required">Bank In Amount</label>
						<div class="input-group"> 
							<input class="form-control" name="biramount" id="biramount" type="text" value="<%=zerovalue%>" onchange="changebiamount(this)" disabled>
						</div>
					</div>
					<div class="col-lg-4">
						<label class="required">Bank Account</label>
						<div class="input-group"> 
							<input class="form-control mraccount" name="biraccount" id="biraccount" type="text" value="<%=bankaccount%>">
						</div>
					</div>
					</div>
					<div class="row" style="margin-top: 2%;">
					<div class="col-lg-4">
						<label class="required">Charges Per/Amount</label>
						<div class="input-group"> 
							<div class="col-sm-6" style="padding-left: 0%;">
								<input class="form-control" name="bicper" id="bicper"type="text" value="0" onchange="changepercharge(this)"><span style="float: right;position: relative;top: -30px;right: -20px;font-size: large;">%</span>
							</div>
							<div class="col-sm-6">
								<input class="form-control" name="bicamt" id="bicamt"type="text" value="<%=zerovalue%>" onchange="changeamtcharge(this)">
							</div>
						</div>
					</div>
					<div class="col-lg-4">
						<label class="required">Charges Account</label>
						<div class="input-group"> 
							<input class="form-control" name="bicaccount" id="bicaccount" type="text" value="<%=chargeaccount%>">
						</div>
					</div>
				</div>
				<div class="row">
							<div class="col-sm-5"></div>
							<div class="col-sm-3">
								<div class="alert-actions btn-toolbar">
									<button type="button" class="btn btn-primary ember-view"
										onclick="BankInAmt()" style="background: green;">
										Bank In</button>
									<button type="button" class="btn btn-primary ember-view"
										data-dismiss="modal" style="background: red;">Cancel</button>
								</div>
							</div>
							<div class="col-sm-5"></div>
						</div>
						</FORM>

	  </div>
	  </div>
		  </div>
	



<%@include file="../jsp/NewChartOfAccountpopup.jsp"%> <!-- imti for add account --> 	  
 <script>
 var item,fdate,tdate,orderno,terminalname,terminalcode,status,outletcode,outlet_name;
$(document).ready(function(){
		BANKIN();
	 	$('[data-toggle="tooltip"]').tooltip();   
	   $('.Show').click(function() {
		    $('#target').show(500);
		    $('.ShowSingle').hide(0);
		    $('.Show').hide(0);
		    $('.Hide').show(0);
		    $('#search_criteria_status').val('show');
		});
	 
	    $('.Hide').click(function() {
		    $('#target').hide(500);
		    $('.ShowSingle').show(0);
		    $('.Show').show(0);
		    $('.Hide').hide(0);
		    $('#search_criteria_status').val('hide');
		});
	     if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
	    	$('.Show').click();
	    }else{
	    	$('.Hide').click();
	    }
	$('[data-toggle="tooltip"]').tooltip();	
	  var plant= '<%=PLANT%>';  
	  
	 
	  /* OUTLET Auto Suggestion */
		$('#OUTLET_NAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'OUTLET_NAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_OUTLET_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.POSOUTLETS);
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
			    return '<div onclick="document.form.OUTLET_CODE.value = \''+data.OUTLET+'\'"><p class="item-suggestion"> ' + data.OUTLET_NAME + '</p></div>';
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
			}).on('typeahead:select',function(event,selection){
				$("#TERMINALNAME").typeahead('val', '"');
				$("#TERMINALNAME").typeahead('val', '');
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == ""){
					document.form.OUTLET_CODE.value = "";
				}	
			});
	  
		/* terminal Auto Suggestion */
		$('#TERMINALNAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'TERMINAL_NAME',  
			  async: true,   
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT : plant,
					ACTION : "GET_TERMINAL_DATA",
					QUERY : query,
					ONAME : document.form.OUTLET_NAME.value
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.POSOUTLETS);
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
			    return '<div onclick="setTerminalData(\''+data.TERMINAL+'\',\''+data.TERMINAL_NAME+'\')"><p class="item-suggestion">Name: ' + data.TERMINAL_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.TERMINAL + '</p></div>';
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
					document.form.TERMINALCODE.value = "";
				}
			});
		
		
		$("#mraccount").typeahead({
			  hint: true,
			  minLength:0,
			  searchOnFocus: true,
			  classNames: {
				 	menu: 'bigdrop'
				  }
			},
			{
			  display: 'accountname',
			  source: function (query, process,asyncProcess) {
					var urlStr = "../ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						action : "getSubAccountTypeGroup",
						module:"journalaccount",
						ITEM : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.results);
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
				suggestion: function(item) {
					if (item.issub) {
						var $state = $(
							    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
							  );
						}
					else
						{
						var $state = $(
								 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
							  );
						}

					  return $state;
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				var top = menuElement.height()+35;
				top+="px";
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();
				}
				menuElement.after( '<div class="expenseaccountAddBtn footer accrmv" onclick="rcoaclass(\'mraccount\')" data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".bigdrop").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".bigdrop");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		
		$("#mvaccount").typeahead({
			  hint: true,
			  minLength:0,
			  searchOnFocus: true,
			  classNames: {
				 	menu: 'bigdrop'
				  }
			},
			{
			  display: 'accountname',
			  source: function (query, process,asyncProcess) {
					var urlStr = "../ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						action : "getSubAccountTypeGroup",
						module:"journalaccount",
						ITEM : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.results);
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
				suggestion: function(item) {
					if (item.issub) {
						var $state = $(
							    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
							  );
						}
					else
						{
						var $state = $(
								 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
							  );
						}

					  return $state;
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				var top = menuElement.height()+35;
				top+="px";
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();
				}
				menuElement.after( '<div class="expenseaccountAddBtn footer accrmv"  onclick="vcoaclass(\'mvaccount\')" data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".bigdrop").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".bigdrop");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		
		$("#biraccount").typeahead({
			  hint: true,
			  minLength:0,
			  searchOnFocus: true,
			  classNames: {
				 	menu: 'bigdrop'
				  }
			},
			{
			  display: 'accountname',
			  source: function (query, process,asyncProcess) {
					var urlStr = "../ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						action : "getSubAccountTypeGroup",
						module:"journalaccount",
						ITEM : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.results);
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
				suggestion: function(item) {
					if (item.issub) {
						var $state = $(
							    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
							  );
						}
					else
						{
						var $state = $(
								 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
							  );
						}

					  return $state;
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				var top = menuElement.height()+35;
				top+="px";
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();
				}
				menuElement.after( '<div class="expenseaccountAddBtn footer accrmv" onclick="rcoaclass(\'biraccount\')" data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".bigdrop").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".bigdrop");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		
		$("#bicaccount").typeahead({
			  hint: true,
			  minLength:0,
			  searchOnFocus: true,
			  classNames: {
				 	menu: 'bigdrop'
				  }
			},
			{
			  display: 'accountname',
			  source: function (query, process,asyncProcess) {
					var urlStr = "../ChartOfAccountServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						action : "getSubAccountTypeGroup",
						module:"journalaccount",
						ITEM : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.results);
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
				suggestion: function(item) {
					if (item.issub) {
						var $state = $(
							    '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;"><i>  Sub-account:'+ item.sub + '</p></span>'
							  );
						}
					else
						{
						var $state = $(
								 '<span>'+ item.text +'&nbsp;&nbsp;&nbsp;<p style="float:right;font-size:14px;font-style: italic;font-weight: 500;">  '+ item.type + '</p></span>'
							  );
						}

					  return $state;
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				var top = menuElement.height()+35;
				top+="px";
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();
				}
				menuElement.after( '<div class="expenseaccountAddBtn footer accrmv" onclick="rcoaclass(\'bicaccount\')" data-toggle="modal" data-target="#create_account_modalcoa"><a href="#"> + New Account</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".bigdrop").css('display') != "block")
					menuElement.next().hide();
			}).on('typeahead:open',function(event,selection){
				var menuElement = $(this).parent().find(".bigdrop");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var menuElement = $(this).parent().find(".bigdrop");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		
});


		
var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    matches = [];
	    substrRegex = new RegExp(q, 'i');
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};

function removeterminaldropdown(){
	$("#TERMINALNAME").typeahead('destroy');
}

function setType(value){
	document.form.srctype.value= value;
	}

function setTerminalData(TERMINAL,TERMINAL_NAME){
	$("input[name=TERMINALCODE]").val(TERMINAL);
}



function recieve(date,outlet,terminal,paymode,amount,id,edit,raccount,eaccount){
	$("input[name ='moutlet']").val(outlet);
	$("input[name ='mpayid']").val(id)
	$("input[name ='mterminal']").val(terminal);
	$("input[name ='mdate']").val(date);
	$("input[name ='mpaymode']").val(paymode);
	$("input[name ='mamount']").val(parseFloat(amount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='mamtrecived']").val(parseFloat(amount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='mvamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
	$("input[name ='mraccount']").val(raccount);
	$("input[name ='mvaccount']").val(eaccount);
	$("input[name ='medit']").val(edit);
	$('#posRevenueModal').modal('show');
}

function recieveedit(date,outlet,terminal,paymode,amount,id,edit,raccount,eaccount,pmramount,pmraccount,pmeamount,pmeaccount){
	$("input[name ='moutlet']").val(outlet);
	$("input[name ='mpayid']").val(id)
	$("input[name ='mterminal']").val(terminal);
	$("input[name ='mdate']").val(date);
	$("input[name ='mpaymode']").val(paymode);
	$("input[name ='mamount']").val(parseFloat(amount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='mamtrecived']").val(parseFloat(pmramount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='mvamount']").val(parseFloat(pmeamount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='mraccount']").val(pmraccount);
	$("input[name ='mvaccount']").val(pmeaccount);
	$("input[name ='medit']").val(edit);
	$('#posRevenueModal').modal('show');
}

function changeramount(data)
{
	var amountc = $("input[name ='mamount']").val();
	var amountr = data.value;
	if(parseFloat(amountc) < parseFloat(amountr)){
		$("input[name ='mamtrecived']").val(amountc);
		$("input[name ='mvamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
		alert("Amount should't exceed "+ amountc);
	}else{
		$("input[name ='mamtrecived']").val(parseFloat(amountc -(amountc - amountr)).toFixed(<%=numberOfDecimal%>));
		$("input[name ='mvamount']").val(parseFloat(amountc - amountr).toFixed(<%=numberOfDecimal%>));
	}
}

function changeeamount(data)
{
	var amountc = $("input[name ='mamount']").val();
	var amountr = $("input[name ='mamtrecived']").val(amountc)
	var amounte = data.value;
	
	if(parseFloat(amountc) < parseFloat(amounte)){
		$("input[name ='mvamount']").val(amountc);
		$("input[name ='mamtrecived']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
		alert("Expense Amount should't exceed "+ amountc);
	}else{
		$("input[name ='mvamount']").val(parseFloat(amountc -(amountc - amounte)).toFixed(<%=numberOfDecimal%>));
		$("input[name ='mamtrecived']").val(parseFloat(amountc - amounte).toFixed(<%=numberOfDecimal%>));
	}
	
}

function create_accountcoa() {
	
	if ($('#create_account_modalcoa #acc_type').val() == "") {
		alert("please fill account type");
		$('#create_account_modalcoa #acc_type').focus();
		return false;
	}
	
	if ($('#create_account_modalcoa #acc_det_type').val() == "") {
		alert("please fill account detail type");
		$('#create_account_modalcoa #acc_det_type').focus();
		return false;
	}
	
	if ($('#create_account_modalcoa #acc_name').val() == "") {
		alert("please fill account name");
		$('#create_account_modalcoa #acc_name').focus();
		return false;
	}
	
	if(document.create_formcoa.acc_is_sub.checked)
	{
		if ($('#create_account_modalcoa #acc_subAcct').val() == "") {
			alert("please fill sub account");
			$('#create_account_modalcoa #acc_subAcct').focus();
			return false;
		}
		else
			{
			 var parType=$('#create_account_modalcoa #acc_det_type').val();
			 subType=subType.trim();
			 var n = parType.localeCompare(subType);
			    if(n!=0)
			    	{
			    	$(".alert").show();
			    	$('.alert').html("For subaccounts, you must select the same account and extended type as their parent.");
			    	/* setTimeout(function() {
			            $(".alert").alert('close');
			        }, 5000); */
			    	 return false;
			    	}
			}
	}
	/* if ($('#create_account_modalcoa #acc_balance').val() != "") {
		if ($('#create_account_modalcoa #acc_balance').val() != "0") {
		if ($('#create_account_modalcoa #acc_balanceDate').val() == "") {
		alert("please fill date");
		$('#create_account_modalcoa #acc_balanceDate').focus();
		return false;
		}
		}
	} */
	
	var formData = $('form[name="create_formcoa"]').serialize();
	$.ajax({
		type : 'post',
		url : "/track/ChartOfAccountServlet?action=create",
		dataType : 'json',
		data : formData,//{key:val}
		success : function(data) {
			if (data.STATUS == "FAIL") {		                               
				alert(data.MESSAGE);
			}else{
				var ukey = document.create_formcoa.ukey.value;
				ukey = "#"+ukey;
				//$(ukey).parents("tr").find('input[name="account_name"]').val(data.ACCOUNT_NAME); 
				$(ukey).val(data.ACCOUNT_NAME);
				$('#create_account_modalcoa').modal('toggle');
			}
		},
		error : function(data) {
			alert(data.responseText);
		}
	});
	return false;
}

function rcoaclass(value){
	$("input[name ='ukey']").val(value);
}

function vcoaclass(value){
	$("input[name ='ukey']").val(value);
}


function ReceiveAmt(){

	if ($('#posRevenueForn #mraccount').val() == "") {
		alert("please select received account");
		$('#posRevenueForn #mraccount').focus();
		return false;
	}
	
	if ($('#posRevenueForn #mvaccount').val() == "") {
		alert("please select variance account");
		$('#posRevenueForn #mvaccount').focus();
		return false;
	}
	
	var formData = $('form[name="posRevenueForn"]').serialize();
	$.ajax({
		type : 'post',
		url : "/track/PosReport?action=receivepayment",
		dataType : 'json',
		data : formData,//{key:val}
		success : function(data) {
			if (data.STATUS == "FAIL") {	
				alert("test");
				alert(data.MESSAGE);
			}else{
				$('#posRevenueModal').modal('toggle');
				//$('#posRevenueModal').modal('hide');
				onGo1();
			}
		},
		error : function(data) {
			alert(data.responseText);
		}
	}); 
	
}

function postrecieved(sid){
	$.ajax({
		type : 'post',
		url : "../PosReport",
		dataType : 'json',
		data : {
			action : "postreceivepayment",
			sid : sid
		},
		success : function(data) {
			if (data.STATUS == "FAIL") {	
				alert(data.MESSAGE);
			}else{
				onGo1();
				alert(data.MESSAGE);
			}
		},
		error : function(data) {
			alert(data.responseText);
		}
	}); 
}


function recievebankin(sdate,outlet,terminal,paymode,amount,payid,raccount,baccount,caccount){
	
	$("input[name ='bioutlet']").val(outlet);
	$("input[name ='bipayid']").val(payid)
	$("input[name ='biterminal']").val(terminal);
	$("input[name ='bidate']").val(sdate);
	$("input[name ='bipaymode']").val(paymode);
	$("input[name ='biaccount']").val(raccount);
	$("input[name ='biamount']").val(parseFloat(amount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='biramount']").val(parseFloat(amount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='bicper']").val(0);
	$("input[name ='bicamt']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
	$("input[name ='biraccount']").val(baccount);
	$("input[name ='bicaccount']").val(caccount);
	$('#bankInModal').modal('show');
	
}

function changepercharge(data){
	var bpercentage = $("input[name ='bicper']").val();
	var biamt = $("input[name ='biamount']").val();
	
	var bchargemaount = (parseFloat(biamt)/100)*parseFloat(bpercentage);
	
	$("input[name ='bicamt']").val(parseFloat(bchargemaount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='biramount']").val(parseFloat(biamt-bchargemaount).toFixed(<%=numberOfDecimal%>));
}

function changeamtcharge(data){
	var chargeamt = $("input[name ='bicamt']").val();
	var biamt = $("input[name ='biamount']").val();
	var bpercentage = (parseFloat(chargeamt)*100)/parseFloat(biamt);
	
	$("input[name ='bicper']").val(parseFloat(bpercentage).toFixed(1));
	$("input[name ='biramount']").val(parseFloat(biamt-chargeamt).toFixed(<%=numberOfDecimal%>));
}



function BANKIN(){
	 fdate = document.form.FROM_DATE.value;
	 tdate = document.form.TO_DATE.value;
	 outletcode = document.form.OUTLET_CODE.value;
	 terminalcode = document.form.TERMINALCODE.value;
	 
	$.ajax({
		type : 'post',
		url : "../PosReport",
		dataType : 'json',
		data : {
			action : "BANKTEST",
			fdate : fdate,
			tdate : tdate,
			outletcode : outletcode,
			terminalcode : terminalcode
		},
		success : function(data) {
			console.log(data);
			$(".cashin-table tbody").html("");
			for(var k = 0; k < data.cashlist.length; k ++){
				/* console.log("---------------");
    			console.log(data.cashlist[k]['ID']);
    			console.log(data.cashlist[k]['OUTLET']);
    			console.log(data.cashlist[k]['TERMINAL']);
    			console.log(data.cashlist[k]['SHIFTDATE']);
    			console.log(data.cashlist[k]['EMPLOYEE_ID']);
    			console.log(data.cashlist[k]['PAYMENTMODE']);
    			console.log(data.cashlist[k]['RECIVEDSTATUS']);
    			console.log(data.cashlist[k]['RECIVEDAMOUNT']);
    			console.log("---------------"); */
    			
    			var body="";
    			body += '<tr>';
    			body += '<td class="text-center">';
    			body += '<input type="checkbox" class="form-check-input ccheck" name="ccheck" value="'+data.cashlist[k]['ID']+'" Onclick="cashcheckbox(this,\''+data.cashlist[k]['RECIVEDAMOUNT']+'\',\''+data.cashlist[k]['ID']+'\')">';
    			body += '</td>';
    			body += '<td class="text-center">'+data.cashlist[k]['SHIFTDATE']+'</td>';
    			body += '<td class="text-center">'+data.cashlist[k]['OUTLET']+'</td>';
    			body += '<td class="text-center">'+data.cashlist[k]['TERMINAL']+'</td>';
    			body += '<td class="text-center">'+data.cashlist[k]['EMPLOYEE_ID']+'</td>';
    			body += '<td class="text-center">'+parseFloat(data.cashlist[k]['RECIVEDAMOUNT']).toFixed(<%=numberOfDecimal%>)+'</td>';
    			body += '</tr>';
    			$(".cashin-table tbody").append(body);
    		}
		},
		error : function(data) {
			$(".cashin-table tbody").html("");
			alert(data.responseText);
		}
	}); 
}

function changeinamount(){
	var bpercentage = $("input[name ='bicper']").val();
	var biamt = $("input[name ='biamount']").val();
	
	var bchargemaount = (parseFloat(biamt)/100)*parseFloat(bpercentage);
	
	$("input[name ='bicamt']").val(parseFloat(bchargemaount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='biramount']").val(parseFloat(biamt-bchargemaount).toFixed(<%=numberOfDecimal%>));
}

function cashcheckbox(data,ramount,pid){
	var biamt = $("input[name ='biamount']").val();
	var bchargemaount = $("input[name ='bicamt']").val();
	if ($(data).prop('checked')==true){ 
		biamt = parseFloat(biamt) + parseFloat(ramount);
    }else{
    	biamt = parseFloat(biamt) - parseFloat(ramount);
    }
	$("input[name ='biamount']").val(parseFloat(biamt).toFixed(<%=numberOfDecimal%>));
	$("input[name ='biramount']").val(parseFloat(biamt-bchargemaount).toFixed(<%=numberOfDecimal%>));
	changeinamount();
	var payids = "";
	var l = 0;
	$("input[name ='ccheck']").each(function() {
		if ($(this).prop('checked')==true){ 
			if(l == 0){
				payids = $(this).val();
			}else{
				payids +=","+$(this).val();
			}
			l++;
		}
	});
	$("input[name ='payidlist']").val(payids)
}

function BankInAmt(){

	if ($('#form2 #biraccount').val() == "") {
		alert("please select bank account");
		$('#form2 #biraccount').focus();
		return false;
	}
	
	if ($('#form2 #bicaccount').val() == "") {
		alert("please select charges account");
		$('#form2 #bicaccount').focus();
		return false;
	}
	
	if ($('#form2 #depositedate').val() == "") {
		alert("please select bank in date");
		$('#form2 #depositedate').focus();
		return false;
	}
	
	var c = 0;
	$("input[name ='ccheck']").each(function() {
		if ($(this).prop('checked')==true){ 
			c = 1;
		}
	});
	
	if(c == 0){
		alert("please select the check box");
		return false;
	}
	
	$("#biramount").prop('disabled', false);
	$("#biamount").prop('disabled', false);
	
	var formData = $('form[name="form2"]').serialize();
	$.ajax({
		type : 'post',
		url : "/track/PosReport?action=bankincashreceivepayment",
		dataType : 'json',
		data : formData,//{key:val}
		success : function(data) {
			if (data.STATUS == "FAIL") {	
				$("#biramount").prop('disabled', true);
				$("#biamount").prop('disabled', true);
				alert(data.MESSAGE);
			}else{
				$("#biramount").prop('disabled', true);
				$("#biamount").prop('disabled', true);
				//$('#posRevenueModal').modal('hide');
				$("input[name ='biamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
				$("input[name ='biramount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
				$("input[name ='bicper']").val(0);
				$("input[name ='bicamt']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
				$("input[name ='biraccount']").val('<%=bankaccount%>');
				$("input[name ='bicaccount']").val('<%=chargeaccount%>');
				$("input[name ='depositedate']").val('');
				BANKIN();
				alert(data.MESSAGE);
			}
		},
		error : function(data) {
			$("#biamount").prop('disabled', true);
			$("#biramount").prop('disabled', true);
			alert(data.responseText);
		}
	}); 
	
}

function allowallday(data){
	if ($(data).prop('checked')==true){
		$("input[name ='FROM_DATE']").val("");
		$("input[name ='TO_DATE']").val("");
		BANKIN();
		
		$("input[name ='biamount']").val('<%=zerovalue%>');
		$("input[name ='biramount']").val('<%=zerovalue%>');
		$("input[name ='bicper']").val('0');
		$("input[name ='bicamt']").val('<%=zerovalue%>');
	}
	
	

}
</script>
<style id="table_style" type="text/css">
.printtable
{
border: 1px solid #ccc;
border-collapse: collapse;
}
.printtable th, .printtable td
{
padding: 5px;
border: 1px solid #ccc;
}
.printtable tr:last-child {
font-weight: bold;
}
.printtable td:nth-last-child(3),
td:nth-last-child(2),
td:nth-last-child(1) {
text-align: right;
}
</style>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>