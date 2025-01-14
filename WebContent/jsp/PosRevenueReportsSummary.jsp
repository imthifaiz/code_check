<!-- PAGE CREATED BY : IMTHI -->
<!-- DATE 21-01-2023 -->
<!-- DESC : POS Revenue Reports Summary -->
<%@ page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
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
String title = "POS Revenue Report";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
		<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.POS_REPORT%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<script src="js/dataTables.fixedHeader.min.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<style>
/* table thead tr{
    display:block;
}
table th,table td{
    width:100px;
}


table  tbody{
  display:block;
  height:500px;
  overflow:auto;
}  

table  tbody{
  height:500px;
  overflow:auto;
} */
tbody {
    display: block;
    /*height: 200px;*/
    overflow: auto;
}
thead,tfoot, tbody tr {
    display: table;
    width: 100%;
    table-layout: fixed;/* even columns width , fix width of table too*/
}
thead {
    width: calc( 100% - 1em )/* scrollbar is average 1em/16px width, remove it from thead width */
}

tfoot{
    width: calc( 100% - 1em );/* scrollbar is average 1em/16px width, remove it from thead width */
    font-weight: bold;
}

.tbodyfixheight>tbody {
	height: 600px;
}

.tbodyheight>tbody {
	height: 100%;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/ExcelSave.js"></script>
<script src="../jsp/js/exceljs.min.js"></script>


<%
	StrUtils strUtils = new StrUtils();
	Generator generator = new Generator();
	userBean _userBean = new userBean();
	_userBean.setmLogger(mLogger);
	List locQryList = new ArrayList();

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
	
	//FROM_DATE    = strUtils.fString(request.getParameter("FROM_DATE"));
	String curDate =du.getDateMinusDays();
	//FROM_DATE=du.getDateinddmmyyyy(curDate);
	status  = strUtils.fString(request.getParameter("STATUS"));
	sTerminalCode = strUtils.InsertQuotes(strUtils.fString(request.getParameter("TERMINALCODE")));
	
	//new data
	FROM_DATE    = strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE    = strUtils.fString(request.getParameter("TO_DATE"));
	TERMINAL_CODE    = strUtils.fString(request.getParameter("TERMINAL_CODE"));
	OUTLET_CODE    = strUtils.fString(request.getParameter("OUTLET_CODE"));
	//new data
	
	//IMTIZIAF
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
	String collectionDate=DateUtils.getDate();
// 	ArrayList arrCust = new OutletBeanDAO().PaymentModeMstlist(PLANT);
	ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
	Map map = (Map) al.get(0);
	String CNAME = (String) map.get("PLNTDESC");
	//END
	
	int year = Calendar.getInstance().get(Calendar.YEAR);
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
                <li><label>POS Revived and Bank In Report</label></li>                                   
            </ul>
	 <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
              <button type="button" class="btn btn-default"
						onClick="window.location.href='../posreports/cashbankin'">
						Cash Bank In</button>
					&nbsp;
				</div>
                            
                           <div class="btn-group" role="group">
              <button type="button" class="btn btn-default printMe" onclick="ExportTableToExcel();"
					 data-toggle="tooltip"  data-placement="bottom" title="Export">
						<i class="fa fa-table" aria-hidden="true"></i>
					</button>
					&nbsp;
				</div>
				  <!-- <div class="btn-group" role="group">
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
 
	
<FORM class="form-horizontal" name="form" method="post" action="PosRevenueReportsSummary.jsp">
<input type="hidden" name="xlAction" value=""> 
<input type="hidden" name="PGaction" value="View"> 
<input type="hidden" name="plant" value="<%=PLANT%>">
<input type="hidden" name="TERMINAL_CODE" value="">
<INPUT type="hidden" name="TERMINALCODE" value="<%=sTerminalCode%>">
<%-- <input type="hidden" name="srctype" value="<%=searchtype%>"> --%>
<input type="hidden" name="srctype" value="">

	<div id="target" style="padding: 18px; display:none;">
   	<div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Search</label>
				<div class="col-sm-2"><select class="form-control" id="reportperiod" onchange="pldatechanged();">
				<option value="CUSTOM">Custom</option>
				<option value="TODAY">Today</option>
				<option value="YESTERDAY">Yesterday</option>
				<option value="THISMONTH">This Month</option>
				<option value="LASTMONTH">Last Month</option>
				<option value="YEAR">Year</option>
				</select></div>
				<div class="col-sm-2">
				<select class="form-control" id="selectyear" disabled>
				<%for(int i=2020; i < 2051; i++){
				if(year == i){
				%>
				<option selected value="<%=i%>"><%=i%></option>
				<%}else{ %>
					<option value="<%=i%>"><%=i%></option>
				<%}}%>
				</select>
				</div>
  		<div class="col-sm-2">
			<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" /> 
			<input name="FROM_DATE" type = "text" value="<%=FROM_DATE%>" id="FROM_DATE" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY placeholder="FROM DATE">
		</div>
		<div class="col-sm-2">
			<input class="form-control datepicker" name="TO_DATE" type = "text" id="TO_DATE" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 READONLY placeholder="TO DATE">
		</div>
  		
  		
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
   	<div class="col-sm-4 ac-box">
		<input type="hidden" name="DEDUCT_TAB" value="1">
		<input type="hidden" name="OUTLET_CODE" value="">
		<input type="hidden" name="OUTLET_CODE1" value="">
		<INPUT name="OUTLET_NAME" id="OUTLET_NAME" type="TEXT" value="<%=OUTLET_CODE%>" size="20" placeholder="OUTLET NAME" MAXLENGTH=100 class="ac-selected form-control"> 
		<span class="select-icon" onclick="$(this).parent().find('../jsp/input[name=\'OUTLET_NAME\']').focus()">
		<i class="glyphicon glyphicon-menu-down"></i></span>
	</div>
   	<div class="col-sm-4 ac-box">
    		<INPUT name="TERMINALNAME" id="TERMINALNAME" type="TEXT" value="<%=TERMINAL_CODE%>" placeholder="TERMINAL" size="20" MAXLENGTH=100 class="ac-selected form-control"> 
			<span class="select-icon" onclick="$(this).parent().find('../jsp/input[name=\'TERMINALNAME\']').focus()">
			<i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
       <div class="col-sm-4 ac-box" style="display:none;"> 
    		<input type="text" name="CSTATUS" id="CSTATUS" value="<%=status%>" class="ac-selected form-control" placeholder="STATUS" >
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'CSTATUS\']').focus()">
			<i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
 	</div>

   	<div class="form-group">
   	<label class="control-label col-sm-2" for=""> </label>
  		<div class="col-sm-4 ac-box">
  		</div>
  		
 		<div class="col-sm-4 ac-box">
  			<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
  		</div>
 	</div>

  	</div>
  	  	
  	      <!-- <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px;  display:none;">Hide Advanced Search</a>
      </div>
      <div class="ShowSingle">
      <div class="col-sm-offset-9">
  	  </div>
         </div>
       	   </div> -->
  	
</form>
<br>
 


<!-- <div style="overflow-x:auto;"> -->
    <div class="bs-example">
    <ul class="nav nav-tabs" id="myTab" style="font-size: 94%;"> 
     	<li class="nav-item active">
            <a href="#salesdetail" class="nav-link" data-toggle="tab" aria-expanded="true" onclick="openTabs(event, 'Sales')">Sales Detail</a>
        </li>
        <li class="nav-item">
            <a href="#received" class="nav-link" data-toggle="tab" onclick="openTabs(event, 'Received')">Received</a>
        </li>
        </ul>
        <div class="tab-content clearfix">
        	<div class="table-responsive tab-pane active" id="salesdetail" style="margin-top: 1%">
        <table id="table" class="table table-bordred table-striped tbodyfixheight"  style="width: 100%;"> 
        <caption style="display: none"><%=CNAME%></caption> 
     			<thead style="text-align: center">  

		          <tr id="headerRow">  
		             
		            <th style="font-size: smaller;">Date</th>
		            <th style="font-size: smaller;">Outlet</th> 
		            <th style="font-size: smaller;">Terminal</th> 
		            <%
		            List listQrys =   new OutletBeanDAO().PaymentModeMstlist(PLANT);
		            if (listQrys.size() > 0) {
		            for(int i =0; i<listQrys.size(); i++) {
		            	int iIndex = i + 1;
						Map m=(Map)listQrys.get(i);
						finalString = (String)m.get("PAYMENTMODE");
		
		            %>
		            <th style="font-size: smaller;"><%=finalString%></th>
		            <%-- <th style="font-size: smaller;"><%=finalString%> Status</th> --%>
		            <th style="font-size: smaller;"></th>
		            <%
						}}
		            %>
		            <th style="font-size: smaller;">Expense</th>
		            <th style="font-size: smaller;">Total</th>
		            <th style="font-size: smaller;">Shift.Var</th>
		            <th style="font-size: smaller;">Act.Var</th>
		            <th style="font-size: smaller;"></th>
		          </tr>  
		        </thead> 
        </table>
        	</div>
        	
        	<div class="table-responsive tab-pane" id="received"  style="margin-top: 1%">
        <table id="tablePrint" class="table table-bordred table-striped tbodyfixheight"  style="width: 100%;">
        <caption style="display: none"><%=CNAME%></caption>  
     			<thead style="text-align: center">  

		          <tr id="headerRow">  
		             
		            <th style="font-size: smaller;">Date</th>
		            <th style="font-size: smaller;">Outlet</th> 
		            <th style="font-size: smaller;">Terminal</th> 
		            <%
		            if (listQrys.size() > 0) {
		            for(int i =0; i<listQrys.size(); i++) {
		            	int iIndex = i + 1;
						Map m=(Map)listQrys.get(i);
						finalString = (String)m.get("PAYMENTMODE");
		
		            %>
		            <th style="font-size: smaller;"><%=finalString%></th>
		            <%-- <th style="font-size: smaller;"><%=finalString%> Status</th> --%>
		            <th style="font-size: smaller;"></th>
		            <%
						}}
		            %>
		            <th style="font-size: smaller;">Expense</th>
		            <th style="font-size: smaller;">Total</th>
		            <th style="font-size: smaller;">Journal</th>
		          </tr>  
		        </thead> 
        </table>
        	</div>
        </div>
    </div>
<!--     </div> -->
<script>
		var item,fdate,tdate,orderno,terminalname,terminalcode,status,outletcode,outlet_name;
		var tabletype;
		var tablePrint;
		function getParameters(){
			return {
				"FDATE":fdate,"TDATE":tdate,"TERMINALNAME":terminalname,"TERMINALCODE":terminalcode,"STATUS":status,"OUTLET_NAME":outlet_name,"OUTLET_CODE":outletcode,
				"action": "POS_SALES_RECIEVE_REPORT","PLANT":"<%=PLANT%>",SRC:"POSRETURN",LOGIN_USER:"<%=USERID%>"
			}
		}
		
		function getParameters1(){
			return {
				"FDATE":fdate,"TDATE":tdate,"TERMINALNAME":terminalname,"TERMINALCODE":terminalcode,"STATUS":status,"OUTLET_NAME":outlet_name,"OUTLET_CODE":outletcode,
				"action": "POS_SALES_BANKIN_REPORT","PLANT":"<%=PLANT%>",SRC:"POSRETURN",LOGIN_USER:"<%=USERID%>"
			}
		}
		function onGo(){
			onGo1();
			
		}

		function onGo1(){
			 plant = document.form.plant.value;
			 fdate = document.form.FROM_DATE.value;
			 tdate = document.form.TO_DATE.value;
			 outlet_name = document.form.OUTLET_NAME.value;
			 outletcode = document.form.OUTLET_CODE.value;
			 terminalname = document.form.TERMINALNAME.value;
			 terminalcode = document.form.TERMINALCODE.value;
			 status = document.form.CSTATUS.value;
		    
			   var urlStr = "../PosReport";
			   var groupColumn = 1;	
			    if (tablePrint){
			    	tablePrint.ajax.url( urlStr ).load();
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tablePrint = $('#tablePrint').DataTable({
						"processing": true,
						"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
						"fixedHeader": true,
						"ajax": {
							"type": "POST",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters1()) ? d : getParameters1();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	
					        	if(typeof data.POS[0].shiftdate === 'undefined'){
						        		return [];
						        	}else {				        		
						        		for(var dataIndex = 0; dataIndex < data.POS.length; dataIndex ++){
						        			console.log("-------------bank in-----------");
						        			console.log(data.POS);
						        			data.POS[dataIndex]['expenses'] = parseFloat(data.POS[dataIndex]['expenses']).toFixed(<%=numberOfDecimal%>);
						        			data.POS[dataIndex]['shifttoatal'] = parseFloat(data.POS[dataIndex]['shifttoatal']).toFixed(<%=numberOfDecimal%>);
						        			
						        			var jstatus=0;
						        			 <%
									            if (listQrys.size() > 0) {
									            for(int i =0; i<listQrys.size(); i++) {
									            	int iIndex = i + 1;
													Map m=(Map)listQrys.get(i);
													finalString = (String)m.get("PAYMENTMODE");
									
									            %>
									            data.POS[dataIndex]['<%=finalString%>pmramount'] = parseFloat(data.POS[dataIndex]['<%=finalString%>pmramount']).toFixed(<%=numberOfDecimal%>);
									            <%-- if(data.POS[dataIndex]['<%=finalString%>pmid'] == 0){
							        				data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = "-";
							        			}else{
							        				var paymode = '<%=finalString%>';
							        				if(paymode.toUpperCase() === 'CASH'){
							        					data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = 'CASH';
							        					if(data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] == 0){
							        						data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '-'
							        					}else{
							        						data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = 'BANK';
							        						jstatus = 1;
							        					}
							        				}else{
							        					if(data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] == 0){
							        						if(data.POS[dataIndex]['<%=finalString%>pmramount'] == 0){
							        							data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '-'
							        						}else{
							        							data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '<button class="btn btn-primary ember-view" onclick="recievebankin(\''+data.POS[dataIndex]['shiftdate']+'\',\''+data.POS[dataIndex]['outletname']+'\',\''+data.POS[dataIndex]['terminalname']+'\',\'<%=finalString%>\',\''+data.POS[dataIndex]['<%=finalString%>pmramount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmid']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmraccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>raccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>eaccount']+'\')">Bank In</button>';
							        						}
							        					}else{
							        						data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = 'BANK';
							        						jstatus = 1;
							        					}
							        				}
							        			} --%>

							        			if(data.POS[dataIndex]['<%=finalString%>pmid'] == 0){
							        				<%-- data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '<i class="fa fa-minus-circle" aria-hidden="true" style="color: red;" ></i>'; --%>
							        				data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '';
							        			}else{
							        				var paymode = '<%=finalString%>';
							        				if(paymode.toUpperCase() === 'CASH'){
							        					<%-- data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = 'CASH'; --%>
							        					if(data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] == 0){
							        						<%-- data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '<i class="fa fa-minus-circle" aria-hidden="true" style="color: red;" ></i>'; --%>
							        						data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '';
							        					}else{
							        						data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '<i class="fa fa-check-circle" aria-hidden="true"  style="color: green;" title="Bank"></i>';
							        						jstatus = 1;
							        					}
							        				}else{
							        					if(data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] == 0){
							        						if(data.POS[dataIndex]['<%=finalString%>pmramount'] == 0){
							        							<%-- data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '<i class="fa fa-minus-circle" aria-hidden="true" style="color: red;" ></i>'; --%>
							        							data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '';
							        						}else{
							        							data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '<i class="fa fa-arrow-circle-down" aria-hidden="true" style="color: darkorange;" title="Bank In" onclick="recievebankin(\''+data.POS[dataIndex]['shiftdate']+'\',\''+data.POS[dataIndex]['outletname']+'\',\''+data.POS[dataIndex]['terminalname']+'\',\'<%=finalString%>\',\''+data.POS[dataIndex]['<%=finalString%>pmramount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmid']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmraccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>raccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>eaccount']+'\')"></i>';
							        						}
							        					}else{
							        						data.POS[dataIndex]['<%=finalString%>pmbankinstatus'] = '<i class="fa fa-check-circle" aria-hidden="true"  style="color: green;" title="Bank"></i>';
							        						jstatus = 1;
							        					}
							        				}
							        			}
									            <%
													}}
									            %>
						        			
						        			if(jstatus == 1){
						        				data.POS[dataIndex]['journal'] = '<i class="fa fa-file" aria-hidden="true" style="color: blueviolet;" title="Journal" onclick="posbankinjournal(\''+data.POS[dataIndex]['sid']+'\')"></i>';
						        			}else{
						        				data.POS[dataIndex]['journal'] = '-';
						        			}
						        		}
						        		return data.POS;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'shiftdate', "orderable": true},
			    			{"data": 'outletname', "orderable": true},
			    			{"data": 'terminalname', "orderable": true},
			    			
			    			 <%
					            if (listQrys.size() > 0) {
					            for(int i =0; i<listQrys.size(); i++) {
					            	int iIndex = i + 1;
									Map m=(Map)listQrys.get(i);
									finalString = (String)m.get("PAYMENTMODE");
					
					            %>
					            {"data": '<%=finalString%>pmramount', "orderable": true},
					            {"data": '<%=finalString%>pmbankinstatus', "orderable": true},
					            <%
									}}
					            %>
			    			
			    			{"data": 'expenses', "orderable": true},
			    			{"data": 'shifttoatal', "orderable": true},
			    			{"data": 'journal', "orderable": true}
			    			],
			    			
			    		"columnDefs": [],	
						"orderFixed": [ ], 
						"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
						"<'row'<'col-md-6'><'col-md-6'>>" +
						"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
						buttons: [{
		                    extend: 'colvis',
		                    columns: ':not(:eq('+groupColumn+')):not(:last)'
		                }],
				        "order": []
					});
			    	tabletype = $('#table').DataTable({
						"processing": true,
						"lengthMenu": [[100, 500, 1000], [100, 500, 1000]],
						"fixedHeader": true,
						"ajax": {
							"type": "POST",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	
					        	if(typeof data.POS[0].shiftdate === 'undefined'){
						        		return [];
						        	}else {				        		
						        		for(var dataIndex = 0; dataIndex < data.POS.length; dataIndex ++){
						        			console.log(data.POS);
						        			<%-- data.POS[dataIndex]['Index'] = dataIndex + 1;
						        			data.POS[dataIndex]['CollectionDate'] = '<a href="../posreports/salesreportsdetail?TYPE=Return&DONO=&PRNO='+data.POS[dataIndex]['prno']+'">' + data.POS[dataIndex]['CollectionDate'] + '</a>';
						        			data.POS[dataIndex]['avgcost'] = parseFloat(data.POS[dataIndex]['avgcost']).toFixed(<%=numberOfDecimal%>);
						        			data.POS[dataIndex]['issAvgcostwTax'] = parseFloat(data.POS[dataIndex]['issAvgcostwTax']).toFixed(<%=numberOfDecimal%>);
						        			data.POS[dataIndex]['tax'] = parseFloat(data.POS[dataIndex]['tax']).toFixed(<%=numberOfDecimal%>);
						        			data.POS[dataIndex]['issueAvgcost'] = parseFloat(data.POS[dataIndex]['issueAvgcost']).toFixed(<%=numberOfDecimal%>); --%>
						        			data.POS[dataIndex]['expenses'] = parseFloat(data.POS[dataIndex]['expenses']).toFixed(<%=numberOfDecimal%>);
						        			var totalamt= parseFloat(data.POS[dataIndex]['shifttoatal']) + parseFloat(data.POS[dataIndex]['expenses'])
						        			data.POS[dataIndex]['shifttoatal'] = parseFloat(totalamt).toFixed(<%=numberOfDecimal%>);
						        			 <%
									            if (listQrys.size() > 0) {
									            for(int i =0; i<listQrys.size(); i++) {
									            	int iIndex = i + 1;
													Map m=(Map)listQrys.get(i);
													finalString = (String)m.get("PAYMENTMODE");
									
									            %>
									            data.POS[dataIndex]['<%=finalString%>'] = parseFloat(data.POS[dataIndex]['<%=finalString%>']).toFixed(<%=numberOfDecimal%>);
									            <%-- if(data.POS[dataIndex]['<%=finalString%>pmid'] == 0){
							        				data.POS[dataIndex]['<%=finalString%>pmstatus'] = "-";
							        			}else{
							        				if(data.POS[dataIndex]['hdrstatus'] == 0){
							        					if(data.POS[dataIndex]['<%=finalString%>pmstatus'] == 0){
							        						data.POS[dataIndex]['<%=finalString%>pmstatus'] = '<button class="btn btn-primary ember-view" onclick="recieve(\''+data.POS[dataIndex]['shiftdate']+'\',\''+data.POS[dataIndex]['outletname']+'\',\''+data.POS[dataIndex]['terminalname']+'\',\'<%=finalString%>\',\''+data.POS[dataIndex]['<%=finalString%>']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmid']+'\',0,\''+data.POS[dataIndex]['<%=finalString%>raccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>eaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdamount']+'\',\''+data.POS[dataIndex]['shiftdiffval']+'\')">Receive</button>';
							        					}else{
							        						data.POS[dataIndex]['<%=finalString%>pmstatus'] = '<button class="btn btn-primary ember-view" onclick="recieveedit(\''+data.POS[dataIndex]['shiftdate']+'\',\''+data.POS[dataIndex]['outletname']+'\',\''+data.POS[dataIndex]['terminalname']+'\',\'<%=finalString%>\',\''+data.POS[dataIndex]['<%=finalString%>']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmid']+'\',0,\''+data.POS[dataIndex]['<%=finalString%>raccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>eaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmramount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmraccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmeamount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmeaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdamount']+'\')">Edit</button>';
							        					}
							        					
							        				}else if(data.POS[dataIndex]['hdrstatus'] == 1){
							        					if(data.POS[dataIndex]['<%=finalString%>pmstatus'] == 0){
							        						data.POS[dataIndex]['<%=finalString%>pmstatus'] = '<button class="btn btn-primary ember-view" onclick="recieve(\''+data.POS[dataIndex]['shiftdate']+'\',\''+data.POS[dataIndex]['outletname']+'\',\''+data.POS[dataIndex]['terminalname']+'\',\'<%=finalString%>\',\''+data.POS[dataIndex]['<%=finalString%>']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmid']+'\',0,\''+data.POS[dataIndex]['<%=finalString%>raccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>eaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdamount']+'\',\''+data.POS[dataIndex]['shiftdiffval']+'\')">Receive</button>';
							        					}else{
							        						data.POS[dataIndex]['<%=finalString%>pmstatus'] = '<button class="btn btn-primary ember-view" onclick="recieveedit(\''+data.POS[dataIndex]['shiftdate']+'\',\''+data.POS[dataIndex]['outletname']+'\',\''+data.POS[dataIndex]['terminalname']+'\',\'<%=finalString%>\',\''+data.POS[dataIndex]['<%=finalString%>']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmid']+'\',0,\''+data.POS[dataIndex]['<%=finalString%>raccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>eaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmramount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmraccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmeamount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmeaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdamount']+'\')">Edit</button>';
							        					}
							        					
							        				}else{
							        					data.POS[dataIndex]['<%=finalString%>pmstatus'] = "Received";
							        				}
							        				
							        			} --%>

									            if(data.POS[dataIndex]['<%=finalString%>pmid'] == 0){
							        				<%-- data.POS[dataIndex]['<%=finalString%>pmstatus'] = '<i class="fa fa-minus-circle" aria-hidden="true" style="color: red;"></i>'; --%>
							        				data.POS[dataIndex]['<%=finalString%>pmstatus'] = '';
							        			}else{
							        				if(data.POS[dataIndex]['hdrstatus'] == 0){
							        					if(data.POS[dataIndex]['<%=finalString%>pmstatus'] == 0){
							        						data.POS[dataIndex]['<%=finalString%>pmstatus'] = '<i class="fa fa-arrow-circle-down" aria-hidden="true" style="color: darkorange;" title="Receive" onclick="recieve(\''+data.POS[dataIndex]['shiftdate']+'\',\''+data.POS[dataIndex]['outletname']+'\',\''+data.POS[dataIndex]['terminalname']+'\',\'<%=finalString%>\',\''+data.POS[dataIndex]['<%=finalString%>']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmid']+'\',0,\''+data.POS[dataIndex]['<%=finalString%>raccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>eaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdamount']+'\',\''+data.POS[dataIndex]['shiftdiffval']+'\')"></i>';
							        					}else{
							        						data.POS[dataIndex]['<%=finalString%>pmstatus'] = '<i class="fa fa-pencil-square" aria-hidden="true" style="color: blue;" title="Edit" onclick="recieveedit(\''+data.POS[dataIndex]['shiftdate']+'\',\''+data.POS[dataIndex]['outletname']+'\',\''+data.POS[dataIndex]['terminalname']+'\',\'<%=finalString%>\',\''+data.POS[dataIndex]['<%=finalString%>']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmid']+'\',0,\''+data.POS[dataIndex]['<%=finalString%>raccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>eaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmramount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmraccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmeamount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmeaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdamount']+'\')"></i>';
							        					}
							        					
							        				}else if(data.POS[dataIndex]['hdrstatus'] == 1){
							        					if(data.POS[dataIndex]['<%=finalString%>pmstatus'] == 0){
							        						data.POS[dataIndex]['<%=finalString%>pmstatus'] = '<i class="fa fa-arrow-circle-down" aria-hidden="true" style="color: darkorange;" title="Receive" onclick="recieve(\''+data.POS[dataIndex]['shiftdate']+'\',\''+data.POS[dataIndex]['outletname']+'\',\''+data.POS[dataIndex]['terminalname']+'\',\'<%=finalString%>\',\''+data.POS[dataIndex]['<%=finalString%>']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmid']+'\',0,\''+data.POS[dataIndex]['<%=finalString%>raccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>eaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdamount']+'\',\''+data.POS[dataIndex]['shiftdiffval']+'\')"></i>';
							        					}else{
							        						data.POS[dataIndex]['<%=finalString%>pmstatus'] = '<i class="fa fa-pencil-square" aria-hidden="true" style="color: blue;" title="Edit" onclick="recieveedit(\''+data.POS[dataIndex]['shiftdate']+'\',\''+data.POS[dataIndex]['outletname']+'\',\''+data.POS[dataIndex]['terminalname']+'\',\'<%=finalString%>\',\''+data.POS[dataIndex]['<%=finalString%>']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmid']+'\',0,\''+data.POS[dataIndex]['<%=finalString%>raccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>eaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmramount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmraccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmeamount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pmeaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdaccount']+'\',\''+data.POS[dataIndex]['<%=finalString%>pdamount']+'\')"></i>';
							        					}
							        					
							        				}else{
							        					data.POS[dataIndex]['<%=finalString%>pmstatus'] = '<i class="fa fa-check-circle" aria-hidden="true" style="color: green;" title="Received"></i>';
							        				}
							        				
							        			}


							        			
									            <%
													}}
									            %>
									            
									            /* if(data.POS[dataIndex]['hdrstatus'] == 1){
									            	data.POS[dataIndex]['hdrstatus'] = '<button class="btn btn-danger ember-view" onclick="postrecieved(\''+data.POS[dataIndex]['sid']+'\')">POST</button>'; 
									            }else if(data.POS[dataIndex]['hdrstatus'] == 2){

									            	data.POS[dataIndex]['hdrstatus'] = '<button class="btn btn-danger ember-view" onclick="posrecivedjournal(\''+data.POS[dataIndex]['sid']+'\')">JOURNAL</button>'; 
									            }else{
									            	data.POS[dataIndex]['hdrstatus'] = " ";
									            } */

									            if(data.POS[dataIndex]['hdrstatus'] == 1){
									            	data.POS[dataIndex]['hdrstatus'] = '<i class="fa fa-plus-circle" aria-hidden="true" style="color: green;" title="Post" onclick="postrecieved(\''+data.POS[dataIndex]['sid']+'\')"></i>'; 
									            }else if(data.POS[dataIndex]['hdrstatus'] == 2){
									            	/* data.POS[dataIndex]['hdrstatus'] = "Posted" */
									            	data.POS[dataIndex]['hdrstatus'] = '<i class="fa fa-file" aria-hidden="true" style="color: blueviolet;" title="Show Journal" onclick="posrecivedjournal(\''+data.POS[dataIndex]['sid']+'\')"></i>'; 
									            }else{
									            	data.POS[dataIndex]['hdrstatus'] = " ";
									            }
						        			
						        		}
						        		return data.POS;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'shiftdate', "orderable": true},
			    			{"data": 'outletname', "orderable": true},
			    			{"data": 'terminalname', "orderable": true},
			    			
			    			 <%
					            if (listQrys.size() > 0) {
					            for(int i =0; i<listQrys.size(); i++) {
					            	int iIndex = i + 1;
									Map m=(Map)listQrys.get(i);
									finalString = (String)m.get("PAYMENTMODE");
					
					            %>
					            {"data": '<%=finalString%>', "orderable": true},
					           	{"data": '<%=finalString%>pmstatus', "orderable": true},
					            <%
									}}
					            %>
			    			
			    			{"data": 'expenses', "orderable": true},
			    			{"data": 'shifttoatal', "orderable": true},
			    			{"data": 'shiftdiffval', "orderable": true},
			    			{"data": 'actdiffval', "orderable": true},
			    			{"data": 'hdrstatus', "orderable": true}
			    			],
			    			
			    		"columnDefs": [ ],	
						"orderFixed": [ ], 
						"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
						"<'row'<'col-md-6'><'col-md-6'>>" +
						"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
						buttons: [{
		                    extend: 'colvis',
		                    //columns: ':not(:eq('+groupColumn+')):not(:last)'
		                }],
				        "order": [],
				        
				   	    "drawCallback": function ( settings ) {
							var groupColumn = 0;
							var groupRowColSpan= 2;
						   	var api = this.api();
				            var rows = api.rows( {page:'current'} ).nodes();
				            var last=null;
				            var groupEnd = 0;
				            var currentRow = 0;
				            var groupexpense=0;
				            var grouptotal=0;
				            <%
				            for(int i =0; i<listQrys.size(); i++) {
				            	int iIndex = i + 1;
								Map m=(Map)listQrys.get(i);
								String paymodestring = (String)m.get("PAYMENTMODE");
				            %>
								var <%=paymodestring%>data = 0;
				            <%
							}
				            %>

				            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
				            	 if ( last !== group ) {
					                	last = group;
					                    groupEnd = i;    
					                }
				            	
				            	<%
				            	int icout=3;
					            for(int i =0; i<listQrys.size(); i++) {
					            	int iIndex = i + 1;
									Map m=(Map)listQrys.get(i);
									String paymodestring = (String)m.get("PAYMENTMODE");
					            %>
					            	<%=paymodestring%>data += parseFloat($(rows).eq( i ).find('td:eq(<%=icout%>)').html().replace(',', '').replace('$', ''));
					            	console.log("test----------"+<%=paymodestring%>data);
					            	
					            <%
					            icout = icout+2;
								}
					            %>
					            groupexpense += parseFloat($(rows).eq( i ).find('td:eq(<%=icout%>)').html().replace(',', '').replace('$', ''));
					            grouptotal += parseFloat($(rows).eq( i ).find('td:eq(<%=icout+1%>)').html().replace(',', '').replace('$', ''));
				                currentRow = i;
				            } );
				            
				            if (groupEnd > 0 || rows.length == (currentRow + 1)){
				            	var groupexpenseVal=null,grouptotalVal=null
				            	 <%
						            for(int i =0; i<listQrys.size(); i++) {
						            	int iIndex = i + 1;
										Map m=(Map)listQrys.get(i);
										String paymodestring = (String)m.get("PAYMENTMODE");
						            %>
										var <%=paymodestring%>dataval = 0;
						            <%
									}
						            %>
				            	
				            	
					            <%
					            for(int i =0; i<listQrys.size(); i++) {
					            	int iIndex = i + 1;
									Map m=(Map)listQrys.get(i);
									String paymodestring = (String)m.get("PAYMENTMODE");
					            %>
									console.log("<%=paymodestring%>------------"+<%=paymodestring%>data);
									if(<%=paymodestring%>data==null || <%=paymodestring%>data==0){
										<%=paymodestring%>dataval="0.000";
				                	}else{
				                		<%=paymodestring%>dataval=parseFloat(<%=paymodestring%>data).toFixed(<%=numberOfDecimal%>);
				                	}
					            <%
								}
					            %>
					            console.log("groupexpense------------"+groupexpense);
					            console.log("grouptotal------------"+grouptotal);
					            
					            if(groupexpense==null || groupexpense==0){
					            	groupexpenseVal="0.000";
			                	}else{
			                		groupexpenseVal=parseFloat(groupexpense).toFixed(<%=numberOfDecimal%>);
			                	}
					            
					            if(grouptotal==null || grouptotal==0){
					            	grouptotalVal="0.000";
			                	}else{
			                		grouptotalVal=parseFloat(grouptotal).toFixed(<%=numberOfDecimal%>);
			                	}
					            
					            var radd = '<tr class="group"><td></td><td></td><td>Total</td>';
					            <%
					            for(int i =0; i<listQrys.size(); i++) {
					            	int iIndex = i + 1;
									Map m=(Map)listQrys.get(i);
									String paymodestring = (String)m.get("PAYMENTMODE");
					            %>
					            radd += '<td>' + <%=paymodestring%>dataval + '</td><td></td>';
					            <%
								}
					            %>
					            radd += '<td>' + groupexpenseVal + '</td><td>' + grouptotalVal + '</td><td></td><td></td><td></td></tr>';
					            
					        	$(rows).eq( currentRow ).after(radd);
					            
				            }
				            
				            <%-- if (groupEnd > 0 || rows.length == (currentRow + 1)){
				            	
				            	var totalPickQtyVal=null,groupTotalPickQtyVal=null,totalIssueQtyVal=null,groupTotalIssueQtyVal=null,
				            	totalIssuePriceQtyVal=null,groupTotalIssuePriceQtyVal=null,totalTaxVal=null,groupTotalTaxVal=null;
				            	
				            	if(totalPickQty==null || totalPickQty==0){
				            		totalPickQtyVal="0.000";
			                	}else{
			                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
			                		groupTotalPickQtyVal="0.000";
			                	}else{
			                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
			                	}if(totalIssueQty==null || totalIssueQty==0){
			                		totalIssueQtyVal=parseFloat("0.000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
			                		groupTotalIssueQtyVal="0.000";
			                	}else{
			                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
			                	}if(totalIssuePriceQty==null || totalIssuePriceQty==0){
			                		totalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalIssuePriceQtyVal=parseFloat(totalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
			                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
			                	}if(totalTax==null || totalTax==0){
			                		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalTax==null || groupTotalTax==0){
			                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}
		                		<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
			                	$(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
							    	<%} else {%>
			                	$(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalIssuePriceQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
				                        <%}%>
			                    );
			                	
			                } --%>
				        } 
				       
					});
			                	
			    }
			    
			    //onGo2();
			    
			}
		
		
		
		function onGo2(){
			 plant = document.form.plant.value;
			 fdate = document.form.FROM_DATE.value;
			 tdate = document.form.TO_DATE.value;
			 outlet_name = document.form.OUTLET_NAME.value;
			 outletcode = document.form.OUTLET_CODE.value;
			 terminalname = document.form.TERMINALNAME.value;
			 terminalcode = document.form.TERMINALCODE.value;
			 status = document.form.CSTATUS.value;
		    alert("ongo2");
			   var urlStr = "../PosReport";
			   var groupColumn = 1;	
			   //alert(tablePrint);
			    if (tablePrint){
			    	tablePrint.ajax.url( urlStr ).load();
			    }else{
			    	
			    	tablePrint = $('#tableprint').DataTable({
						"processing": true,
						"lengthMenu": [[-1],["All"]],
						"ajax": {
							"type": "POST",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	
					        	if(typeof data.POS[0].prno === 'undefined'){
						        		return [];
						        	}else {				        		
						        		for(var dataIndex = 0; dataIndex < data.POS.length; dataIndex ++){
						        			<%-- data.POS[dataIndex]['Index'] = dataIndex + 1;
						        			data.POS[dataIndex]['avgcost'] = parseFloat(data.POS[dataIndex]['avgcost']).toFixed(<%=numberOfDecimal%>);
						        			data.POS[dataIndex]['issAvgcostwTax'] = parseFloat(data.POS[dataIndex]['issAvgcostwTax']).toFixed(<%=numberOfDecimal%>);
						        			data.POS[dataIndex]['tax'] = parseFloat(data.POS[dataIndex]['tax']).toFixed(<%=numberOfDecimal%>);
						        			data.POS[dataIndex]['issueAvgcost'] = parseFloat(data.POS[dataIndex]['issueAvgcost']).toFixed(<%=numberOfDecimal%>); --%>
						        			
						        		}
						        		return data.POS;
						        	}
					        	
					        }
					    },
					    "columns": [
				        	{"data": 'shiftdate', "orderable": true},
			    			{"data": 'outletname', "orderable": true},
			    			{"data": 'terminalname', "orderable": true},
			    			
			    			 <%
					            if (listQrys.size() > 0) {
					            for(int i =0; i<listQrys.size(); i++) {
					            	int iIndex = i + 1;
									Map m=(Map)listQrys.get(i);
									finalString = (String)m.get("PAYMENTMODE");
					
					            %>
					            {"data": '<%=finalString%>', "orderable": true},
					            <%
									}}
					            %>
			    			
			    			{"data": 'expenses', "orderable": true},
			    			{"data": 'shifttoatal', "orderable": true}
			    			
			    			],	
						"orderFixed": [ ], 
						"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
						"<'row'<'col-md-6'><'col-md-6'>>" +
						"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
						buttons: [],
				        "order": [],
				        searching: false,
				        paging: false,
				        info: false,
				        "bLengthChange" : false, //thought this line could hide the LengthMenu
				        "drawCallback": function ( settings ) {
							var groupColumn = 0;
							var groupRowColSpan= 3;
						   	var api = this.api();
				            var rows = api.rows( {page:'current'} ).nodes();
				            var last=null;
				            var totalPickQty = 0;
				            var groupTotalPickQty = 0;
				            var totalIssueQty = 0;
				            var groupTotalIssueQty = 0;
				            var totalIssuePriceQty = 0;
				            var groupTotalIssuePriceQty = 0;
				            var totalTax = 0;
				            var groupTotalTax = 0;
				            var groupEnd = 0;
				            var currentRow = 0;
				            var totalGstTax=0;
				            var groupTotalGstTax=0;
				            api.column(groupColumn, {page:'current'} ).data().each( function ( group, i ) {
				                if ( last !== group ) {
				                	var groupTotalTaxVal=null,groupTotalPickQtyVal=null,groupTotalIssueQtyVal=null,groupTotalIssuePriceQtyVal=null;
				               
				                	if(groupTotalTax==null || groupTotalTax==0){
				                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
				                	}else{
				                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
				                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
				                		groupTotalPickQtyVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
				                	}else{
				                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
				                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
				                		groupTotalIssueQtyVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
				                	}else{
				                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
				                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
				                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
				                	}else{
				                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
				                	}
				                    last = group;
				                    groupEnd = i;    
				                    groupTotalPickQty = 0;
				                    groupTotalIssueQty = 0;
				                    groupTotalIssuePriceQty = 0;
				                    groupTotalTax = 0;
				                    
				                }
				                
				                groupTotalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                totalPickQty += parseFloat($(rows).eq( i ).find('td:eq(4)').html().replace(',', '').replace('$', ''));
				                groupTotalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                totalIssueQty += parseFloat($(rows).eq( i ).find('td:eq(5)').html().replace(',', '').replace('$', ''));
				                <%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
				                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                totalTax += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
						    	<%} else {%>
				                groupTotalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                totalIssuePriceQty += parseFloat($(rows).eq( i ).find('td:eq(6)').html().replace(',', '').replace('$', ''));
				                groupTotalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
				                totalTax += parseFloat($(rows).eq( i ).find('td:eq(7)').html().replace(',', '').replace('$', ''));
				                <%}%>
				                currentRow = i;
				            } );
				            if (groupEnd > 0 || rows.length == (currentRow + 1)){
				            	
				            	var totalPickQtyVal=null,groupTotalPickQtyVal=null,totalIssueQtyVal=null,groupTotalIssueQtyVal=null,
				            	totalIssuePriceQtyVal=null,groupTotalIssuePriceQtyVal=null,totalTaxVal=null,groupTotalTaxVal=null;
				            	
				            	if(totalPickQty==null || totalPickQty==0){
				            		totalPickQtyVal="0.000";
			                	}else{
			                		totalPickQtyVal=parseFloat(totalPickQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalPickQty==null || groupTotalPickQty==0){
			                		groupTotalPickQtyVal="0.000";
			                	}else{
			                		groupTotalPickQtyVal=parseFloat(groupTotalPickQty).toFixed(<%=numberOfDecimal%>);
			                	}if(totalIssueQty==null || totalIssueQty==0){
			                		totalIssueQtyVal=parseFloat("0.000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalIssueQtyVal=parseFloat(totalIssueQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssueQty==null || groupTotalIssueQty==0){
			                		groupTotalIssueQtyVal="0.000";
			                	}else{
			                		groupTotalIssueQtyVal=parseFloat(groupTotalIssueQty).toFixed(<%=numberOfDecimal%>);
			                	}if(totalIssuePriceQty==null || totalIssuePriceQty==0){
			                		totalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalIssuePriceQtyVal=parseFloat(totalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalIssuePriceQty==null || groupTotalIssuePriceQty==0){
			                		groupTotalIssuePriceQtyVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalIssuePriceQtyVal=parseFloat(groupTotalIssuePriceQty).toFixed(<%=numberOfDecimal%>);
			                	}if(totalTax==null || totalTax==0){
			                		totalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		totalTaxVal=parseFloat(totalTax).toFixed(<%=numberOfDecimal%>);
			                	}if(groupTotalTax==null || groupTotalTax==0){
			                		groupTotalTaxVal=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
			                	}else{
			                		groupTotalTaxVal=parseFloat(groupTotalTax).toFixed(<%=numberOfDecimal%>);
			                	}
		                		<%if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {%>
			                	$(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
							    	<%} else {%>
			                	$(rows).eq( currentRow ).after(
				                        '<tr class="group"><td colspan=' + groupRowColSpan + '></td><td>Total</td><td class=" t-right">' + totalPickQtyVal + '</td><td class=" t-right">' + totalIssueQtyVal + '</td><td class=" t-right">' + totalIssuePriceQtyVal + '</td><td class=" t-right">' + totalTaxVal + '</td></tr>'
				                        <%}%>
			                    );
			                }
				        }
					});
			    }
			    
			}

		</script>
	  </div>
	  </div>
		  </div>
	
	<div id="posRevenueModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg box-body">
			<form name="posRevenueForn" id="posRevenueForn">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h3 class="modal-title">Receive POS</h3>
					</div>
					<div class="modal-body">
	
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Outlet</label>
							<div class="col-sm-4">
								<input class="form-control" name="moutlet" id="moutlet"
									type="text" value="" disabled> <input name="medit"
									id="medit" type="text" value="" hidden> <input
									name="mpayid" id="mpayid" type="text" value="" hidden>
							</div>
							<label class="col-form-modal-label col-sm-2">Terminal</label>
							<div class="col-sm-4">
								<input class="form-control" name="mterminal" id="mterminal"
									type="text" value="" disabled>
							</div>
						</div>
	
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Date</label>
							<div class="col-sm-4">
								<input class="form-control" name="mdate" id="mdate" type="text"
									value="" disabled>
							</div>
							<label class="col-form-modal-label col-sm-2">Pay Mode</label>
							<div class="col-sm-4">
								<input class="form-control" name="mpaymode" id="mpaymode"
									type="text" value="" disabled>
							</div>
						</div>
	
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Amount</label>
							<div class="col-sm-4">
								<input class="form-control" name="mamount" id="mamount"
									type="text" value="" disabled>
							</div>
						</div>
	
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Received
								Amount</label>
							<div class="col-sm-4">
								<input class="form-control" name="mamtrecived" id="mamtrecived"
									type="text" value="" onchange="changeramount(this)">
							</div>
							<label class="col-form-modal-label col-sm-2">Received
								Account</label>
							<div class="col-sm-4">
								<input class="form-control mraccount" name="mraccount"
									id="mraccount" type="text" value="">
							</div>
						</div>
	
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Variance
								Amount</label>
							<div class="col-sm-4">
								<input class="form-control" name="mvamount" id="mvamount"
									type="text" value="" readonly>
							</div>
							<label class="col-form-modal-label col-sm-2">Variance
								Account</label>
							<div class="col-sm-4">
								<input class="form-control" name="mvaccount" id="mvaccount"
									type="text" value="">
							</div>
						</div>
						
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">POS Difference
								Amount</label>
							<div class="col-sm-4">
								<input class="form-control" name="pdamount" id="pdamount"
									type="text" value="" readonly>
							</div>
							<label class="col-form-modal-label col-sm-2">POS Difference
								Account</label>
							<div class="col-sm-4">
								<input class="form-control" name="pdaccount" id="pdaccount"
									type="text" value="" readonly>
							</div>
						</div>
						
						<div class="row form-group">
							<div class="col-sm-5"></div>
							<div class="col-sm-3">
								<div class="alert-actions btn-toolbar">
									<button type="button" class="btn btn-primary ember-view" id="posRevenueModalbtn"
										onclick="ReceiveAmt()" style="background: green;">
										Receive</button>
									<button type="button" class="btn btn-primary ember-view"
										data-dismiss="modal" style="background: red;">Cancel</button>
								</div>
							</div>
							<div class="col-sm-5"></div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>



	<div id="bankInModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg box-body">
			<form name="bankInForm" id="bankInForm">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h3 class="modal-title">Bank In</h3>
					</div>
					<div class="modal-body">
	
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Outlet</label>
							<div class="col-sm-4">
								<input class="form-control" name="bioutlet" id="bioutlet" type="text" value="" disabled>  
								<input name="bipayid" id="bipayid" type="text" value="" hidden>
							</div>
							<label class="col-form-modal-label col-sm-2">Terminal</label>
							<div class="col-sm-4">
								<input class="form-control" name="biterminal" id="biterminal" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Date</label>
							<div class="col-sm-4">
								<input class="form-control" name="bidate" id="bidate" type="text" value="" disabled>
							</div>
							<label class="col-form-modal-label col-sm-2">Pay Mode</label>
							<div class="col-sm-4">
								<input class="form-control" name="bipaymode" id="bipaymode" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Bank In Date</label>
							<div class="col-sm-4">
								<input type="text" class="form-control datepicker" id="depositedate" value="<%=curDate%>" name="depositedate" READONLY>
							</div>
							
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Amount</label>
							<div class="col-sm-4">
								<input class="form-control" name="biamount" id="biamount" type="text" value="" disabled>
							</div>
							<label class="col-form-modal-label col-sm-2">Account</label>
							<div class="col-sm-4">
								<input class="form-control" name="biaccount" id="biaccount" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Bank In Amount</label>
							<div class="col-sm-4">
								<input class="form-control" name="biramount" id="biramount" type="text" value="" onchange="changebiamount(this)" disabled>
							</div>
							<label class="col-form-modal-label col-sm-2">Bank Account</label>
							<div class="col-sm-4">
								<input class="form-control mraccount" name="biraccount" id="biraccount" type="text" value="">
							</div>
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Charges Per/Amount</label>
							<div class="col-sm-2">
								<input class="form-control" name="bicper" id="bicper"type="text" value="" onchange="changepercharge(this)"><span style="float: right;position: relative;top: -30px;right: -20px;font-size: large;">%</span>
							</div>
							<div class="col-sm-2">
								<input class="form-control" name="bicamt" id="bicamt"type="text" value="" onchange="changeamtcharge(this)">
							</div>
							<label class="col-form-modal-label col-sm-2">Charges Account</label>
							<div class="col-sm-4">
								<input class="form-control" name="bicaccount" id="bicaccount" type="text" value="">
							</div>
						</div>
						<div class="row form-group">
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
					</div>
				</div>
			</form>
		</div>
	</div>
	
	<div id="postsalesdetail" class="modal fade" role="dialog">
	  <input type="hidden" name="salespostid" value="" />
	  <div class="modal-dialog modal-sm">	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-body">
	        <div class="row">
			   <div class="col-lg-2">
			      <i>
			         <svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xxlg-md icon-attention-circle" style="fill: red">
			            <path d="M256 32c30.3 0 59.6 5.9 87.2 17.6 26.7 11.3 50.6 27.4 71.2 48s36.7 44.5 48 71.2c11.7 27.6 17.6 56.9 17.6 87.2s-5.9 59.6-17.6 87.2c-11.3 26.7-27.4 50.6-48 71.2s-44.5 36.7-71.2 48C315.6 474.1 286.3 480 256 480s-59.6-5.9-87.2-17.6c-26.7-11.3-50.6-27.4-71.2-48s-36.7-44.5-48-71.2C37.9 315.6 32 286.3 32 256s5.9-59.6 17.6-87.2c11.3-26.7 27.4-50.6 48-71.2s44.5-36.7 71.2-48C196.4 37.9 225.7 32 256 32m0-32C114.6 0 0 114.6 0 256s114.6 256 256 256 256-114.6 256-256S397.4 0 256 0z"></path>
			            <circle cx="256" cy="384" r="32"></circle>
			            <path d="M256.3 96.3h-.6c-17.5 0-31.7 14.2-31.7 31.7v160c0 17.5 14.2 31.7 31.7 31.7h.6c17.5 0 31.7-14.2 31.7-31.7V128c0-17.5-14.2-31.7-31.7-31.7z"></path>
			         </svg>
			      </i>
			   </div>
			   <div class="col-lg-10" style="padding-left: 2px">
			      <p> Posted recived sales details cannot be Changed. Are you sure about Post data ?</p>
			      
			      <div class="alert-actions btn-toolbar">
			         <button class="btn btn-primary ember-view" onclick="cmfpostrecieved()" style="background:red;">
			        	Yes 
			         </button>
			         <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
			      </div>
			   </div>
			</div>
	      </div>
	    </div>
	  </div>
	</div>
	
	
	
	<div id="journalInModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg box-body">
			<form>
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h3 class="modal-title">Journal Detail</h3>
					</div>
					<div class="modal-body">
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Shift ID</label>
							<div class="col-sm-4">
								<input class="form-control" name="rshiftid" type="text" value="" disabled>  
							</div>
							<label class="col-form-modal-label col-sm-2">Employee Name</label>
							<div class="col-sm-4">
								<input class="form-control" name="rempname" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Outlet</label>
							<div class="col-sm-4">
								<input class="form-control" name="routlet" type="text" value="" disabled>  
							</div>
							<label class="col-form-modal-label col-sm-2">Terminal</label>
							<div class="col-sm-4">
								<input class="form-control" name="rterminal" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Shift Date</label>
							<div class="col-sm-4">
								<input class="form-control" name="rshiftdate" type="text" value="" disabled>
							</div>
							<label class="col-form-modal-label col-sm-2">Shift Time</label>
							<div class="col-sm-4">
								<input class="form-control" name="rshifttime" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Sales Received By</label>
							<div class="col-sm-4">
								<input class="form-control" name="rsalesrby" type="text" value="" disabled>
							</div>
							<label class="col-form-modal-label col-sm-2">Sales Received Date</label>
							<div class="col-sm-4">
								<input class="form-control" name="rsalesrd" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-sm-12">
							 <table class="table table-striped rjournal-table">
							    <thead>
							      <tr>
							        <th>Account Name</th>
							        <th>Reference</th>
							        <th>Debit</th>
							        <th>Credit</th>
							      </tr>
							    </thead>
							    <tbody>
							     
							    </tbody>
							  </table>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	
	
		
	<div id="bankjournalInModal" class="modal fade" role="dialog">
		<div class="modal-dialog modal-lg box-body">
			<form>
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h3 class="modal-title">Journal Detail</h3>
					</div>
					<div class="modal-body">
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Shift ID</label>
							<div class="col-sm-4">
								<input class="form-control" name="bshiftid" type="text" value="" disabled>  
							</div>
							<label class="col-form-modal-label col-sm-2">Employee Name</label>
							<div class="col-sm-4">
								<input class="form-control" name="bempname" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Outlet</label>
							<div class="col-sm-4">
								<input class="form-control" name="boutlet" type="text" value="" disabled>  
							</div>
							<label class="col-form-modal-label col-sm-2">Terminal</label>
							<div class="col-sm-4">
								<input class="form-control" name="bterminal" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Shift Date</label>
							<div class="col-sm-4">
								<input class="form-control" name="bshiftdate" type="text" value="" disabled>
							</div>
							<label class="col-form-modal-label col-sm-2">Shift Time</label>
							<div class="col-sm-4">
								<input class="form-control" name="bshifttime" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<label class="col-form-modal-label col-sm-2">Sales Received By</label>
							<div class="col-sm-4">
								<input class="form-control" name="bsalesrby" type="text" value="" disabled>
							</div>
							<label class="col-form-modal-label col-sm-2">Sales Received Date</label>
							<div class="col-sm-4">
								<input class="form-control" name="bsalesrd" type="text" value="" disabled>
							</div>
						</div>
						<div class="row form-group">
							<div class="col-sm-12">
							 <table class="table table-striped bjournal-table">
							    <thead>
							      <tr>
							        <th>Account Name</th>
							        <th>Reference</th>
							        <th>Debit</th>
							        <th>Credit</th>
							      </tr>
							    </thead>
							    <tbody>
							     
							    </tbody>
							  </table>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>

<%@include file="../jsp/NewChartOfAccountpopup.jsp"%> <!-- imti for add account --> 	  
 <script>
 
$(document).ready(function(){
	 	onGo();
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
	  
	  $("#CSTATUS").typeahead({
	  
    	  hint: true,
    	  minLength:0,  
    	  searchOnFocus: true
    	},
    	{
    	  name: 'sts',
    	  display: 'value',  
    	  source: substringMatcher(sts),
    	  limit: 9999,
    	  templates: {
    	  empty: [
    		  '<div style="padding:3px 20px">',
    			'No results found',
    		  '</div>',
    		].join('\n'),
    		suggestion: function(data) {
//     		return '<p>' + data.value + '</p>';
    		return '<div onclick="setType(\''+data.value+'\')"><p>' + data.value + '</p></div>';
    		}
    	  }
    	}).on('typeahead:render',function(event,selection){
    		  
    	}).on('typeahead:open',function(event,selection){
    		
    	}).on('typeahead:close',function(){
    		
    	});	  


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
		
});

var sts =   [{
    "year": "Open",
    "value": "Open",
    "tokens": [
      "Open"
    ]
  },
	  	{
    "year": "Receive",
    "value": "Receive",
    "tokens": [
      "Receive"
    ]
  },
	  	{
    "year": "Banking",
    "value": "Banking",
    "tokens": [
      "Banking"
    ]
	  }];
		
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

function PrintTable() {
    var printWindow = window.open('', '', '');
    printWindow.document.write('<html><head><title>Print <%=title %></title>');

    //Print the Table CSS.
    var table_style = document.getElementById("table_style").innerHTML;
    printWindow.document.write('<style type = "text/css">');
    printWindow.document.write(table_style);
    printWindow.document.write('</style>');
    printWindow.document.write('</head>');

    //Print the DIV contents i.e. the HTML Table.
    printWindow.document.write('<body>');
    var divContents = document.getElementById("dvContents").innerHTML;
    printWindow.document.write(divContents);
    printWindow.document.write('</body>');

    printWindow.document.write('</html>');
    printWindow.document.close();
    printWindow.print();
}

function recieve(date,outlet,terminal,paymode,amount,id,edit,raccount,eaccount,pdaccount,pdamount,diffamt){
	$("input[name ='moutlet']").val(outlet);
	$("input[name ='mpayid']").val(id)
	$("input[name ='mterminal']").val(terminal);
	$("input[name ='mdate']").val(date);
	$("input[name ='mpaymode']").val(paymode);
	$("input[name ='mamount']").val(parseFloat(amount).toFixed(<%=numberOfDecimal%>));
	
	
	$("input[name ='mraccount']").val(raccount);
	$("input[name ='mvaccount']").val(eaccount);
	$("input[name ='pdaccount']").val(pdaccount);
	<%-- $("input[name ='pdamount']").val(parseFloat(pdamount).toFixed(<%=numberOfDecimal%>)); --%>
	
	<%-- $("input[name ='mamtrecived']").val(parseFloat(amount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='mvamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>)); --%>
	if(paymode.toUpperCase() === 'CASH'){
		
		if(isNegative(parseFloat(diffamt))){
			$("input[name ='mamtrecived']").val((parseFloat(amount)+parseFloat(Math.abs(diffamt))).toFixed(<%=numberOfDecimal%>));
			$("input[name ='mvamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
			$("input[name ='pdamount']").val((parseFloat(pdamount)+parseFloat(Math.abs(diffamt))).toFixed(<%=numberOfDecimal%>));
		}else{
			$("input[name ='mamtrecived']").val((parseFloat(amount)-parseFloat(Math.abs(diffamt))).toFixed(<%=numberOfDecimal%>));
			$("input[name ='mvamount']").val(parseFloat(Math.abs(diffamt)).toFixed(<%=numberOfDecimal%>));
			$("input[name ='pdamount']").val(parseFloat(pdamount).toFixed(<%=numberOfDecimal%>));
		}
		
	}else{
		$("input[name ='mamtrecived']").val(parseFloat(amount).toFixed(<%=numberOfDecimal%>));
		$("input[name ='mvamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
		$("input[name ='pdamount']").val(parseFloat(pdamount).toFixed(<%=numberOfDecimal%>));
	}
	
	$("input[name ='medit']").val(edit);
	$('#posRevenueModalbtn').attr('disabled',false);
	$('#posRevenueModal').modal('show');
}

function isNegative(num) {
	  if (Math.sign(num) === -1) {
	    return true;
	  }

	  return false;
	}

function recieveedit(date,outlet,terminal,paymode,amount,id,edit,raccount,eaccount,pmramount,pmraccount,pmeamount,pmeaccount,pdaccount,pdamount){
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
	$("input[name ='pdaccount']").val(pdaccount);
	$("input[name ='pdamount']").val(parseFloat(pdamount).toFixed(<%=numberOfDecimal%>));
	$("input[name ='medit']").val(edit);
	$('#posRevenueModalbtn').attr('disabled',false);
	$('#posRevenueModal').modal('show');
}

function changeramount(data)
{
	var amountc = $("input[name ='mamount']").val();
	var amountr = data.value;
	if(parseFloat(amountc) < parseFloat(amountr)){
		<%-- $("input[name ='mamtrecived']").val(amountc);
		$("input[name ='mvamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
		alert("Amount should't exceed "+ amountc); --%>
		
		var amdiff = parseFloat(amountr) - parseFloat(amountc);
		var expamount = $("input[name ='mvamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
		$("input[name ='pdamount']").val(parseFloat(amdiff).toFixed(<%=numberOfDecimal%>));
		
		
	}else{
		$("input[name ='mamtrecived']").val(parseFloat(amountc -(amountc - amountr)).toFixed(<%=numberOfDecimal%>));
		$("input[name ='mvamount']").val(parseFloat(amountc - amountr).toFixed(<%=numberOfDecimal%>));
		$("input[name ='pdamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
	}
}

function changeeamount(data)
{
	var amountc = $("input[name ='mamount']").val();
	var amountr = $("input[name ='mamtrecived']").val()
	var amounte = data.value;
	
	<%-- if(parseFloat(amountc) < parseFloat(amounte)){
		$("input[name ='mvamount']").val(amountc);
		$("input[name ='mamtrecived']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
		alert("Expense Amount should't exceed "+ amountc); 
	}else{
		$("input[name ='mvamount']").val(parseFloat(amountc -(amountc - amounte)).toFixed(<%=numberOfDecimal%>));
		$("input[name ='mamtrecived']").val(parseFloat(amountc - amounte).toFixed(<%=numberOfDecimal%>));
		$("input[name ='pdamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
	}--%>
	
	var tamt = parseFloat(amountr) + parseFloat(amounte);
	if(parseFloat(amountc) < parseFloat(tamt)){
		<%-- $("input[name ='mvamount']").val(amountc);
		$("input[name ='mamtrecived']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
		alert("Expense Amount should't exceed "+ amountc); --%>
		var amdiff = parseFloat(tamt) - parseFloat(amountc);
		$("input[name ='pdamount']").val(parseFloat(amdiff).toFixed(<%=numberOfDecimal%>));
		
	}else{
		$("input[name ='mvamount']").val(parseFloat(amounte).toFixed(<%=numberOfDecimal%>));
		<%-- $("input[name ='mamtrecived']").val(parseFloat(amountc - amounte).toFixed(<%=numberOfDecimal%>)); --%>
		$("input[name ='pdamount']").val(parseFloat("0").toFixed(<%=numberOfDecimal%>));
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
	$('#posRevenueModalbtn').attr('disabled',true);
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



/* function postrecieved(sid){
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
} */


function postrecieved(sid){
	
	$('input[name ="salespostid"]').val(sid);
	$('#postsalesdetail').modal('show');
	
}

function cmfpostrecieved(){
	var sid = $('input[name ="salespostid"]').val();
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
				$('#postsalesdetail').modal('hide');
				alert(data.MESSAGE);
			}else{
				onGo1();
				$('#postsalesdetail').modal('hide');
				//alert(data.MESSAGE);
			}
		},
		error : function(data) {
			alert(data.responseText);
		}
	});
}

function posrecivedjournal(sid){
	$.ajax({
		type : 'post',
		url : "../PosReport",
		dataType : 'json',
		data : {
			action : "posrecivedjournal",
			sid : sid
		},
		success : function(data) {
			console.log(data);
			
			if (data.STATUS == "FAIL") {	
				alert("fail");
			}else{
				if(data.DETSTATUS == "1"){
					/* console.log(data.SHIFTHDR.ID);
					console.log(data.SHIFTHDR.CRBY);
					console.log(data.SHIFTHDR.outlet);
					console.log(data.SHIFTHDR.terminal);
					console.log(data.SHIFTHDR.shiftDate);
					console.log(data.SHIFTHDR.shiftTime); */
					$(".rjournal-table tbody").html("");
					$("input[name ='rshiftid']").val(data.SHIFTHDR.ID);
					$("input[name ='rempname']").val(data.SHIFTHDR.CRBY);
					$("input[name ='routlet']").val(data.SHIFTHDR.outlet);
					$("input[name ='rterminal']").val(data.SHIFTHDR.terminal);
					$("input[name ='rshiftdate']").val(data.SHIFTHDR.shiftDate);
					$("input[name ='rshifttime']").val(data.SHIFTHDR.shiftTime);
					$("input[name ='rsalesrby']").val(data.DETNAME);
					$("input[name ='rsalesrd']").val(data.DETDATETIME);
					var body="";
					for(var i = 0; i < data.JOURNAL.length; i ++){
						/* console.log("----------"+i+"----------");
						console.log(data.JOURNAL[i]['ACCOUNT_NAME']);
						console.log(data.JOURNAL[i]['DESCRIPTION']);
						console.log(data.JOURNAL[i]['DEBITS']);
						console.log(data.JOURNAL[i]['CREDITS']);
						console.log("----------------------"); */
						
						body += '<tr>';
						body += '<td>'+data.JOURNAL[i]['ACCOUNT_NAME']+'</td>';
						body += '<td>'+data.JOURNAL[i]['DESCRIPTION']+'</td>';
						body += '<td>'+parseFloat(data.JOURNAL[i]['DEBITS']).toFixed(<%=numberOfDecimal%>)+'</td>';
						body += '<td>'+parseFloat(data.JOURNAL[i]['CREDITS']).toFixed(<%=numberOfDecimal%>)+'</td>';
						body += '</tr>';
					}
					$(".rjournal-table tbody").append(body);
					$('#journalInModal').modal('show');
				}

			}
		},
		error : function(data) {
			alert(data.responseText);
		}
	});
}

function posbankinjournal(sid){
	$.ajax({
		type : 'post',
		url : "../PosReport",
		dataType : 'json',
		data : {
			action : "posbankinjournal",
			sid : sid
		},
		success : function(data) {
			console.log(data);
			
			if (data.STATUS == "FAIL") {	
				alert("fail");
			}else{
				if(data.DETSTATUS == "1"){
					/* console.log(data.SHIFTHDR.ID);
					console.log(data.SHIFTHDR.CRBY);
					console.log(data.SHIFTHDR.outlet);
					console.log(data.SHIFTHDR.terminal);
					console.log(data.SHIFTHDR.shiftDate);
					console.log(data.SHIFTHDR.shiftTime); */
					$(".bjournal-table tbody").html("");
					$("input[name ='bshiftid']").val(data.SHIFTHDR.ID);
					$("input[name ='bempname']").val(data.SHIFTHDR.CRBY);
					$("input[name ='boutlet']").val(data.SHIFTHDR.outlet);
					$("input[name ='bterminal']").val(data.SHIFTHDR.terminal);
					$("input[name ='bshiftdate']").val(data.SHIFTHDR.shiftDate);
					$("input[name ='bshifttime']").val(data.SHIFTHDR.shiftTime);
					$("input[name ='bsalesrby']").val(data.DETNAME);
					$("input[name ='bsalesrd']").val(data.DETDATETIME);
					var body="";
					for(var i = 0; i < data.JOURNAL.length; i ++){
						/* console.log("----------"+i+"----------");
						console.log(data.JOURNAL[i]['ACCOUNT_NAME']);
						console.log(data.JOURNAL[i]['DESCRIPTION']);
						console.log(data.JOURNAL[i]['DEBITS']);
						console.log(data.JOURNAL[i]['CREDITS']);
						console.log("----------------------"); */
						
						body += '<tr>';
						body += '<td>'+data.JOURNAL[i]['ACCOUNT_NAME']+'</td>';
						body += '<td>'+data.JOURNAL[i]['DESCRIPTION']+'</td>';
						body += '<td>'+parseFloat(data.JOURNAL[i]['DEBITS']).toFixed(<%=numberOfDecimal%>)+'</td>';
						body += '<td>'+parseFloat(data.JOURNAL[i]['CREDITS']).toFixed(<%=numberOfDecimal%>)+'</td>';
						body += '</tr>';
					}
					$(".bjournal-table tbody").append(body);
					$('#bankjournalInModal').modal('show');
				}

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
	$("input[name ='depositedate']").val("");
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


function BankInAmt(){

	if ($('#bankInForm #biraccount').val() == "") {
		alert("please select bank account");
		$('#bankInForm #biraccount').focus();
		return false;
	}
	
	if ($('#bankInForm #bicaccount').val() == "") {
		alert("please select charges account");
		$('#bankInForm #bicaccount').focus();
		return false;
	}
	
	if ($('#bankInForm #depositedate').val() == "") {
		alert("please select bankin  date");
		$('#bankInForm #depositedate').focus();
		return false;
	}
	
	$("#bipaymode").prop('disabled', false);
	$("#biramount").prop('disabled', false);
	
	var formData = $('form[name="bankInForm"]').serialize();
	$.ajax({
		type : 'post',
		url : "/track/PosReport?action=bankinreceivepayment",
		dataType : 'json',
		data : formData,//{key:val}
		success : function(data) {
			if (data.STATUS == "FAIL") {	
				$("#bipaymode").prop('disabled', true);
				$("#biramount").prop('disabled', true);
				alert(data.MESSAGE);
			}else{
				$("#bipaymode").prop('disabled', true);
				$("#biramount").prop('disabled', true);
				$('#bankInModal').modal('toggle');
				//$('#posRevenueModal').modal('hide');
				onGo1();
			}
		},
		error : function(data) {
			$("#bipaymode").prop('disabled', true);
			$("#biramount").prop('disabled', true);
			alert(data.responseText);
		}
	}); 
	
}

function BANKTEST(){
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
		},
		error : function(data) {
			alert(data.responseText);
		}
	}); 
}

function openTabs(evt, ref) {
	if(ref == "Received"){
		$("input[name=DEDUCT_TAB]").val ("0");
		//tablePrint.fixedHeader.enable();
		//tabletype.fixedHeader.disable();
		onGo1();
	} else {
		$("input[name=DEDUCT_TAB]").val ("1");
		//tabletype.fixedHeader.enable();
		//tablePrint.fixedHeader.disable();
		onGo1();
	}
}


function ExportTableToExcel() {
	  var workbook = new ExcelJS.Workbook();
	  var worksheet = workbook.addWorksheet('Sheet 1');

	  var table = document.getElementById('table');
	  var DEDUCT_TAB = document.form.DEDUCT_TAB.value;
	  if(DEDUCT_TAB=='0')
	   table = document.getElementById('tablePrint');

	  var captionText = table.querySelector('caption').innerText;

	  // Set the caption as a header across all columns
	  var numColumns = table.rows[0].cells.length;
	
	  // Merge cells based on the width of the table
	  var mergeRange = 'A1:' + String.fromCharCode(64 + numColumns) + '1';
	  worksheet.mergeCells(mergeRange);	  
	  var captionCell = worksheet.getCell('A1');
	  captionCell.value = captionText;
	  captionCell.alignment = { horizontal: 'center' };
	  captionCell.font = { bold: true, size: 10 };
   

	  for (var i = 0; i < table.rows.length; i++) {
		    var row = table.rows[i];
		    var excelRow = worksheet.addRow();
		   // var excelRow = worksheet.getRow(i + 1);

		    for (var j = 0; j < row.cells.length-1; j++) {
		      var cell = row.cells[j];
		      
		      if (i === table.rows.length) {
			      //console.log(j);
			      //console.log(cell);
		    	  var excelCell = excelRow.getCell(j + 4);
			      }

		      else {
		    	  var excelCell = excelRow.getCell(j + 1);
		    	  }
		      excelCell.value = cell.innerText;

		      if (i === 0) {
		          excelCell.font = { bold: true };
		    }
		      if(DEDUCT_TAB=='1'){
	    	  if (i === table.rows.length-1 && j===2) {
	    		  excelCell.font = { bold: true };
		    	  }
		      }
			    

		    }
		  }
	  worksheet.columns.forEach((column) => {
		    column.width = 15; 
		  });

	  workbook.xlsx.writeBuffer().then(function (buffer) {
	    var blob = new Blob([buffer], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
	    saveAs(blob, 'POS Revenue Report.xlsx');
	  });
	}

	
var fromDateFormatted;
var toDateFormatted;
var period;
function pldatechanged(){
	period=$("#reportperiod").val();
	sessionStorage.setItem('period', period);
	var to;
	var from;	
	$('#FROM_DATE').prop('disabled', true);
	$('#TO_DATE').prop('disabled', true);
	$('#selectyear').prop('disabled', true);

	if(period=="TODAY")
		{
			from = moment().format('DD/MM/YYYY');
			to   = moment().format('DD/MM/YYYY');
			fromDateFormatted = moment().format('YYYY-MM-DD');
			toDateFormatted   = moment().format('YYYY-MM-DD');
		}
	else if(period=="THISYEARTODATE")
	 { 
		 var fiscalyear=$('#fiscalyear').val();
		 var formattedFiscal=moment(fiscalyear).format('YYYY-MM-DD');
		 from = moment(formattedFiscal).year(moment().year()).format('DD/MM/YYYY');
		 to   = moment().format('DD/MM/YYYY');
		 fromDateFormatted = moment(formattedFiscal).year(moment().year()).format('YYYY-MM-DD');
		 toDateFormatted   = moment().format('YYYY-MM-DD');
	 }
	else if(period=="THISMONTH")
	{
		from = moment().startOf('month').format('DD/MM/YYYY');
		to   = moment().endOf('month').format('DD/MM/YYYY');
		fromDateFormatted = moment().startOf('month').format('YYYY-MM-DD');
		toDateFormatted   = moment().endOf('month').format('YYYY-MM-DD');
	}
	else if(period=="THISQUARTER")
	{
		from = moment().quarter(moment().quarter()).startOf('quarter').format('DD/MM/YYYY');
		to   = moment().quarter(moment().quarter()).endOf('quarter').format('DD/MM/YYYY');
		fromDateFormatted = moment().quarter(moment().quarter()).startOf('quarter').format('YYYY-MM-DD');
		toDateFormatted   = moment().quarter(moment().quarter()).endOf('quarter').format('YYYY-MM-DD');
	}
	else if(period=="THISYEAR")
	{
		from = moment().year(moment().year()).startOf('year').format('DD/MM/YYYY');
		to   = moment().year(moment().year()).endOf('year').format('DD/MM/YYYY');
		fromDateFormatted = moment().year(moment().year()).startOf('year').format('YYYY-MM-DD');
		toDateFormatted   = moment().year(moment().year()).endOf('year').format('YYYY-MM-DD');
	}
	else if(period=="YEAR")
	{
		var slectedyear=$('#selectyear').find(":selected").val();;
		from = moment().year(slectedyear).startOf('year').format('DD/MM/YYYY');
		to   = moment().year(slectedyear).endOf('year').format('DD/MM/YYYY');
		fromDateFormatted = moment().year(slectedyear).startOf('year').format('YYYY-MM-DD');
		toDateFormatted   = moment().year(slectedyear).endOf('year').format('YYYY-MM-DD');
		$('#selectyear').prop('disabled', false);
	}
	else if(period=="YESTERDAY")
	{
		from = moment().subtract(1, 'days').format('DD/MM/YYYY');
		to = moment().subtract(1, 'days').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(1, 'days').format('YYYY-MM-DD');
		toDateFormatted = moment().subtract(1, 'days').format('YYYY-MM-DD');
	}
	else if(period=="LASTMONTH")
	{
		from = moment().subtract(1, 'months').startOf('month').format('DD/MM/YYYY');
		to = moment().subtract(1, 'months').endOf('month').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(1, 'months').startOf('month').format('YYYY-MM-DD');
		toDateFormatted = moment().subtract(1, 'months').endOf('month').format('YYYY-MM-DD');
	}
	else if(period=="LASTQUARTER")
	{
		from = moment().subtract(3, 'months').startOf('month').format('DD/MM/YYYY');
		to = moment().subtract(1, 'months').endOf('month').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(3, 'months').startOf('month').format('YYYY-MM-DD');
		toDateFormatted = moment().subtract(1, 'months').endOf('month').format('YYYY-MM-DD');
	}
	else if(period=="LASTYEAR")
	{
		from = moment().subtract(1, 'year').startOf('year').format('DD/MM/YYYY');
		to = moment().subtract(1, 'year').endOf('year').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
		toDateFormatted = moment().subtract(1, 'year').endOf('year').format('YYYY-MM-DD');
	}
	else if(period == "LASTYEARTODATE")
	{
		from = moment().subtract(1, 'year').startOf('year').format('DD/MM/YYYY');
		to = moment().subtract(1, 'year').startOf('day').format('DD/MM/YYYY');
		fromDateFormatted = moment().subtract(1, 'year').startOf('year').format('YYYY-MM-DD');
		toDateFormatted = moment().subtract(1, 'year').startOf('day').format('YYYY-MM-DD');
				
	}
	else if(period == "CUSTOM"){
		$('#FROM_DATE').prop('disabled', false);
		$('#TO_DATE').prop('disabled', false);
		//$('#FROM_DATE').val('');
		//$('#TO_DATE').val('');

	}
	
	if(period != "YEAR"){
		$("#selectyear").val("<%=year%>");
	}

	if(period != "CUSTOM"){
		$('#FROM_DATE').val(from);
		$('#TO_DATE').val(to);
		onGo();
	}
}

$('#selectyear').change(function () { 
	pldatechanged();
 });
 
$('a.sidebar-toggle').click(function() {
	onGo1();
});

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