<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="sb" class="com.track.gates.selectBean" />

<%
	String title = "Create Brand Promotion";

    String plant    = StrUtils.fString((String)session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String region = StrUtils.fString((String) session.getAttribute("REGION"));

	//Validate Order azees - 12/2020
	PoHdrDAO poHdrDAO = new PoHdrDAO();
	DateUtils _dateUtils = new DateUtils();
	String FROM_DATE = DateUtils.getDate();
	if (FROM_DATE.length() > 5)
		FROM_DATE = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + "01";

	String TO_DATE = DateUtils.getLastDayOfMonth();
	if (TO_DATE.length() > 5)
		TO_DATE = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);

	String msg = (String)request.getAttribute("Msg");
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	CurrencyDAO currencyDAO = new CurrencyDAO();

	POUtil posUtil = new POUtil();
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
	String taxbylabel = ub.getTaxByLable(plant);
	String collectionDate=DateUtils.getDate();
	String zeroval = StrUtils.addZeroes(0.0, numberOfDecimal);
	String zerovalper = StrUtils.addZeroes(Float.parseFloat("0"), "3");
	String collectionTime=DateUtils.getTimeHHmm();
	String currency=plantMstDAO.getBaseCurrency(plant);
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);


	 ArrayList countryList = new MasterUtil().getCountryList("",plant,region);
	 ArrayList taxTreatmentList =  new MasterUtil().getTaxTreatmentList("",plant,"");
	 ArrayList bankList =  new MasterUtil().getBankList("",plant);

	 Map mcost= posUtil.getPOReceiptInvoiceHdrDetails(plant);

	 String COUNTRYCODE = "";
	 PlantMstUtil plantmstutil = new PlantMstUtil();
	 List viewlistQry = plantmstutil.getPlantMstDetails(plant);
	 for (int i = 0; i < viewlistQry.size(); i++) {
	     Map map = (Map) viewlistQry.get(i);
	     COUNTRYCODE = StrUtils.fString((String)map.get("COUNTRY_CODE"));
	 }

	 String CURRENCYUSEQT="0",DISPLAY="",DISPLAYID="";
	 List curQryList=new ArrayList();

	 curQryList = currencyDAO.getCurrencyDetails(currency,plant);
	 for(int i =0; i<curQryList.size(); i++) {
			ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
			DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
			DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
	        CURRENCYUSEQT	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(3)));
	 }
	 
	    String sOutlet = StrUtils.InsertQuotes(StrUtils.fString(request.getParameter("OUTLET_NAME")));
		String sOutCode = StrUtils.InsertQuotes(StrUtils.fString(request.getParameter("OUTCODE")));
		
		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        String chkdDoNo="",AssignedUser="",AssignedLoc="",PLANT="";
        ArrayList al = _PlantMstDAO.getPlantMstDetails(plant);
        Map map = (Map) al.get(0);
    	String CNAME = (String) map.get("PLNTDESC");
    	String ADD1 = (String) map.get("ADD1");
    	String ADD2 = (String) map.get("ADD2");
    	String ADD3 = (String) map.get("ADD3");
    	String ADD4 = (String) map.get("ADD4");
    	String STATE = (String) map.get("STATE");
    	String COUNTRY = (String) map.get("COUNTY");
    	String ZIP = (String) map.get("ZIP");
    	String TELNO = (String) map.get("TELNO");

    	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
    	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
    	String fromAddress_Country = STATE + " " + COUNTRY+" "+ZIP;
    	
    	boolean displaySummaryEdit=false,displaySummaryNew=false;

%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<style>
 .select2drop{
 	width:487px !important;
 }
 .table-icon{
 	text-align: center;
 }
 .table-icon i{
    vertical-align: middle;
 }
 #remarks-table>tbody>tr>td{
 	padding: 8px;
 }
 .remark-action {
    cursor: pointer;
    font-size: 15px;
    opacity: 0.4;
    position: absolute;
    right: -5%;
    top: 15px;
}
.bill-action {
    right: -25% !important;
}
.sideiconspan {
	color: #23527c;
	position: absolute;
	right: 20px;
	top: 8px;
	z-index: 2;
	vertical-align: middle;
	font-size: 15px;
	font-weight: bold;
}
.phideshow{
    float: right;
    margin-top: -34px;
    position: relative;
    z-index: 2;
    margin-right: 40px;
}

 </style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/clockpicker.js"></script>
<link rel="stylesheet" href="../jsp/css/clockpicker.css">
<script src="../jsp/js/ProductPromotion.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
             <ul class="breadcrumb backpageul">      	
                  <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                  <li><a href="../brandpromotion/summary"><span class="underline-on-hover">Brand Promotion Summary</span></a></li> 
                  <li><label>Create Brand Promotion</label></li>                                   
             </ul>   
     <!-- Muruganantham Modified on 16.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../brandpromotion/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" id="brandpromotionForm" name="cpoform" action="../brandpromotion/save"  method="post"  enctype="multipart/form-data">
				<input type="hidden" name="plant" id="plant" value="<%=plant%>" >
				<input type="hidden" name="numberOfDecimal" value="<%=numberOfDecimal%>" >
				<input type="hidden" name="CURRENCYID" value="<%=DISPLAYID%>">
				<input type="hidden" name="CURRENCYUSEQTOLD" value="<%=CURRENCYUSEQT%>">
				<INPUT type="Hidden" name="COLLECTION_TIME" value="<%=collectionTime%>"/>
				<INPUT type ="hidden" name="NOFO_DEC" id="NOFO_DEC" value="<%=numberOfDecimal%>">
				<input type = "hidden" name="currency" value="<%=currency%>">
				<INPUT type="hidden" name="curency"  value="<%=DISPLAYID%>">
	            <INPUT type="hidden" name="BASECURRENCYID"  value="<%=currency%>"> <%--Resvi--%>    
	            <INPUT type="hidden" name="PRMDESC"  value="byqty">   <!-- imthi -->
				

				<div class="form-group">
				<label class="control-label col-form-label col-sm-2 required" for="Promotion name">Promotion Name:</label>
        			<div class="col-sm-4">
        				<INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="100" name="PROMOTION_NAME" id="PROMOTION_NAME" placeholder="Max 100 Characters">
       				</div>
				</div>

				<div class="form-group">
				<label class="control-label col-form-label col-sm-2 required">Promotion Description:</label>
					<div class="col-sm-4 ac-box">
						<INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="100" name="PROMOTION_DESC" id="PROMOTION_DESC" placeholder="Max 100 Characters">
					</div>
					
				<div class="col-sm-4">
				    <label class="radio-inline">
			      		<input type="radio" name="BYQTY" id="BYQTY" value=0 checked="checked" onclick="SelectVal(event, 'byqty')">By Quantity</label>
			    	<label class="radio-inline">
			      		<input type="radio" name="BYQTY" id="BYQTY" value=1 onclick="SelectVal(event, 'byval')">By Value</label>
			    </div>
				</div>
				
				<div class="form-group" style="display:none;">
      		<label class="control-label col-form-label col-sm-2 required" for="Outlet Name">Outlet</label>
      			<div class="col-sm-4 ac-box">
                	<INPUT class=" form-control" id="OUTLET_NAME" value="<%=sOutlet%>" name="OUTLET_NAME" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select Outlet">
    		 		<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'OUTLET_NAME\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i></span>    						
					<INPUT type="hidden" name="OUTCODE" value="<%=sOutCode%>">
     			</div>
    	</div>
				
				<div class="form-group" style="display:none;">
				<label class="control-label col-form-label col-sm-2" for="supplier name">Customer Type:</label>
        			<div class="col-sm-4">
    		 			<INPUT class=" form-control" id="CUSTOMER_TYPE" value="" name="CUSTOMER_TYPE" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select Customer Type">
    		 			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE\']').focus()">
						<i class="glyphicon glyphicon-menu-down"></i></span>    						
        			</div>
        			
        		</div>
				
				<div class="form-group">
				<label class="control-label col-form-label col-sm-2" for="Start date">Start Date:</label>
					<div class="col-sm-4 ac-box">
						<INPUT class="form-control datepicker" type="TEXT" size="30" MAXLENGTH="10" name="START_DATE" id="START_DATE" readonly value="<%=collectionDate%>"/>
					</div>
				
				<div class="col-sm-6 no-padding">
				<label class="control-label col-form-label col-sm-3" for="time">Start Time:</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="form-control" id="START_TIME" name="START_TIME" value="00:00" maxlength="5" placeholder="HH:MM" readonly>
					</div>
			    </div>
			    </div>
				
				<div class="form-group">
				<label class="control-label col-form-label col-sm-2" for="End Date">End Date:</label>
					<div class="col-sm-4 ac-box">
						<INPUT class="form-control datepicker" type="TEXT" size="30" MAXLENGTH="10" name="END_DATE" id="END_DATE" readonly value="<%=collectionDate%>"/>
					</div>
				
				<div class="col-sm-6 no-padding">
				<label class="control-label col-form-label col-sm-3" for="time">End Time:</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="form-control" id="END_TIME" name="END_TIME" value="00:00" maxlength="5" placeholder="HH:MM" readonly>
					</div>
			    </div>
			    </div>
						
				<!-- <div class="form-group">
					 <label class="control-label col-form-label col-sm-2 required" for="Limit of usage">Limit Of Usage:</label>
        			<div class="col-sm-4">
        				<INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="20" value="0" name="LIMIT_USAGE" id="LIMIT_USAGE" onkeypress="return isNumberKey(event,this,4)">
       				</div>
				</div> -->
				
			    <div class="form-group">
			  	<div class="col-sm-offset-2 col-sm-8">
				    <label class="radio-inline">
			      		<input type="radio" checked="checked" name="ACTIVE" value="Y">Active</label>
			    	<label class="radio-inline">
			      		<input type="radio" name="ACTIVE" value="N">Non Active</label>
			    </div>
			</div>  
			
			<div class="row">
  		    <div class="col-12 col-sm-12">
  			<label>
  			<input type="checkbox" class="form-check-input"  style="border:0;" name = "select" value="select" onclick="return checkAll(this.checked);">
			&nbsp; Select/Unselect All &nbsp;
			</label>
		    </div>                        
        	</div>
        	
        	<div style="overflow-x:auto;">
               <table id="table12" class="table table-bordred table-striped" > 
               
                   <thead style="text-align: center"> 
                      <tr>  
                           <th style="font-size: smaller;">Chk</th>  
                            <th style="font-size: smaller;">Outlet</th>
                            
                           </tr>  
             </thead> 
             <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								
							</tr>
						</tfoot>
               </table>
               </div>


				<div class="row" style="margin: 0px;">
					<table class="table table-bordered line-item-table po-table">
						<thead>
							<tr>
								<th colspan=2 style="width:15%">Buy Brand</th>
								<th style="width:5%">Buy Quantity</th>
								<th colspan=2 class="bill-desc BQ" style="width:15%">Get Product</th>
								<th class="bill-desc BQ" style="width:5%">Get Quantity</th>
								<th class="bill-desc BV" style="width:2%">Type</th>
								<th class="bill-desc BV" style="width:8%">Promotion Value</th>
								<th class="bill" style="width:8%">Limit Of Usage</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="item-img text-center" style="width:2%">
									<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">
									<input type="hidden" name="lnno" value="1">
									<input type="hidden" name="itemdesc" value="">
								</td>
								<td class="bill-item">
									<input type="text" name="item_brand" class="form-control ProductBrand" onchange="checkprdcat(this.value,this)" style="width:87%"  placeholder="Type or click to select an Brand">
									<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
							    </td>
								<td class="item-qty">
									<input type="text" name="qty" class="form-control text-right"
									 data-rl="0.000" data-msq="0.000" data-soh="0.000"
									 data-eq="0.000" data-aq="0.000" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
								</td>
								<td class="item-img text-center BQ" style="width:2%">
									<img alt="" src="../jsp/dist/img/NO_IMG.png" style="width: 100%;">
								</td>
								<td class="bill-desc BQ">
									<input type="text" name="get_item" class="form-control itemSearch" style="width:95%" onchange="checkitems(this.value,this)" placeholder="Type or click to select an item.">
									<button type="button" style="position: absolute;margin-left: -18px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeitem(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
							    </td>
							    <td class="item-qty BQ">
									<input type="text" name="get_qty" class="form-control text-right"
									 data-rl="0.000" data-msq="0.000" data-soh="0.000"
									 data-eq="0.000" data-aq="0.000" value="1.000" onkeypress="return isNumberKey(event,this,4)">
								</td>
									<td class="bill-desc BV">
									<div class="row">
									<div class=" col-lg-12 col-sm-3 col-12">
									<div class="input-group my-group" style="width:120px;">
									<select name="promotion_type" class="discountPicker form-control"
										 onchange="calculateAmount(this)"  style="left: 60px;">
										<option value="<%=DISPLAYID%>"><%=DISPLAYID%></option>
										<option value="%">%</option>
									</select>
									</div>
									</div>
									</div>
								</td>
								<td class="bill-desc BV">
									<input type="text" name="promotionvalue" value="0.000" onkeypress="return isNumberKey(event,this,4)" class="form-control">
								</td>
								<td class="bill">
									<input type="text" name="LIMIT_USAGE" value="0.000" onkeypress="return isNumberKey(event,this,4)" class="form-control">
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<a class="add-line" style="text-decoration: none; cursor: pointer;" onclick="addRowBrand()">
							+ Add another line
						</a>
					</div>
				</div>
				<div class="row grey-bg">
					<div class="col-sm-6 notes-sec">
						<p>Notes</p>
						<div>
							<textarea rows="2" name="NOTES" id="NOTES" class="ember-text-area form-control ember-view"
								placeholder="Max 1000 characters"  maxlength="1000"></textarea>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 txn-buttons">
						<div class="dropup">
							<button id="btnBrandOpen" type="button" class="btn btn-success">Save</button>
							<button type="button" class="btn btn-default"
								onclick="window.location.href='../home'">Cancel</button>
						</div>
					</div>
				</div>

			</form>
			
			<script LANGUAGE="JavaScript">
	    
		var plant = document.cpoform.plant.value;
		var tabletype;
		var OUTLETNAME, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"OUTLETNAME":OUTLETNAME,
				"PLANT":plant,
				"action":"GET_OUTLET_FOR_SUMMARY"
			}
		}
		function onGo(){
			
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table12').DataTable({
						"processing": true,
						"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
						"ajax": {
							"type": "GET",
							"url": urlStr,
							"data": function(d){
								return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
							}, 
							"contentType": "application/x-www-form-urlencoded; charset=utf-8",
					        "dataType": "json",
					        "dataSrc": function(data){
					        	
						        	if(typeof data.OUTLETLIST[0].OUTLET === 'undefined'){
						        		return [];
						        	}else {				        		
						        		for(var dataIndex = 0; dataIndex < data.OUTLETLIST.length; dataIndex ++){
					        				var lineno = data.OUTLETLIST[dataIndex].OUTLET;
					        				var sno=dataIndex+1;
					        				data.OUTLETLIST[dataIndex]['SNO'] = sno;
											

					        				data.OUTLETLIST[dataIndex]['CHK'] = '<INPUT Type=checkbox style=border: 0; name=chkdDoNo value="'+lineno+'" >';
											
					        				
					        			}
						        		return data.OUTLETLIST;
						        	}
					        	
					        }
					    },
				        "columns": [
				        	{"data": 'CHK', "orderable": true},
				        	{"data": 'OUTLET', "orderable": true},
				        	
			    			],
						"orderFixed": [ ], 
						/*"dom": 'lBfrtip',*/
						"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
						"<'row'<'col-md-6'><'col-md-6'>>" +
						"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
						buttons: [
				            {
				                extend: 'collection',
				                text: 'Export',
				                buttons: [
				                    {
				                    	extend : 'excel',
				                    	exportOptions: {
				    	                columns: [':visible']
				                    		
				    	                },
				    	                title: '<%=title%>',
				    	                footer: true
				                    },
				                    {
				                    	extend : 'pdf',
			                            footer: true,
				                    	text: 'PDF Portrait',
				                    	exportOptions: {
				                    	columns: [':visible']
				                    		
				                    	},
				                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
				                    	<%} else {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                              As On <%=collectionDate%>"  ; return dataview },
				                    	<%}%>
			                    		orientation: 'portrait',
				                    	customize: function(doc) {
				                    		doc.defaultStyle.fontSize = 7;
			                     	        doc.styles.tableHeader.fontSize = 7;
			                     	        doc.styles.title.fontSize = 10;
			                     	        doc.styles.tableFooter.fontSize = 7;
				                    	},
			                            pageSize: 'A4'
				                    },
				                    {
				                    	extend : 'pdf',
				                    	footer: true,
				                    	text: 'PDF Landscape',
				                    	exportOptions: {
				                    	columns: [':visible']
				                    		
				                    	},
				                    	<% if(fromAddress_BlockAddress.trim().equals("")) {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
				                    	<%} else {%>
				                    	title: function () { var dataview = "<%=CNAME%> \n <%=fromAddress_BlockAddress.trim()%> \n <%=fromAddress_RoadAddress.trim()%> \n <%=fromAddress_Country.trim()%> \n Phone <%=TELNO.trim()%> \n \n <%=title%>                                                                                          As On <%=collectionDate%>"  ; return dataview },
				                    	<%}%>
			                    		orientation: 'landscape',
			                    		customize: function(doc) {
				                    		doc.defaultStyle.fontSize = 6;
			                     	        doc.styles.tableHeader.fontSize = 6;
			                     	        doc.styles.title.fontSize = 8;                     	       
			                     	        doc.content[1].table.widths = "*";
			                     	       doc.styles.tableFooter.fontSize = 7;
				                    	     },
			                            pageSize: 'A4'
				                    }
				                ]
				            },
				            {
			                    extend: 'colvis',
			                   /*  columns: ':not(:eq('+groupColumn+')):not(:last)' */
			                }		                
				        ],
				        "order": [],

					});
			    }
			    
			}

		</script>
		</div>

	</div>
</div>

<input type="text" id="PageName" style="display: none;" value="BrandPromotion">
<input type="hidden" name="pronumberOfDecimal" id="pronumberOfDecimal" value="<%=numberOfDecimal%>">
<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>

<script>

$('#END_TIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});

$('#START_TIME').clockpicker({
	autoclose: true,
	twelvehour: false,
	donetext: 'Done'
});

</script>

<script>

$(document).ready(function(){
	onGo();
	$('[data-toggle="tooltip"]').tooltip(); 

});

function SelectVal(evt, ref) {

	document.cpoform.PRMDESC.value= ref;
	
	  if(ref == "byqty"){
		  $(".BV").hide();
		  $(".BQ").show();
	  }
	  if(ref == "byval"){
		  $(".BQ").hide();
		  $(".BV").show();
	  }
	}

		
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


$(".po-table tbody").on('click','.accrmv',function(){
	debugger;	    
    var obj = $(this);
    var timestamp = new Date().getUTCMilliseconds();
    kayid = "key"+timestamp;
    $(obj).closest('td').attr('id', kayid); 
    $("input[name ='ukey']").val(kayid);
});


function popUpWin(URL) {
	 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}

function checkAll(isChk)
{
	var len = document.cpoform.chkdDoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.cpoform.chkdDoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.cpoform.chkdDoNo.checked = isChk;
               	}
              	else{
              		document.cpoform.chkdDoNo[i].checked = isChk;
              	}
            	
        }
    }
}

</script>
 <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>
