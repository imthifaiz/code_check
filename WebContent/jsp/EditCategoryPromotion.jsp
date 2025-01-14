<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.db.object.PoDet"%>
<%@page import="com.track.db.object.PosPrd_ClassPromotionHdr"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<%
	String title = "Edit Category Promotion";

    String plant    = StrUtils.fString((String)session.getAttribute("PLANT"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String region = StrUtils.fString((String) session.getAttribute("REGION"));
	String msg = StrUtils.fString(request.getParameter("Msg"));
	String ID = (String)request.getAttribute("ID");
	CategoryPromotionHdrDAO posPromotionHdr= new CategoryPromotionHdrDAO();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	CurrencyDAO currencyDAO = new CurrencyDAO();
	DateUtils _dateUtils = new DateUtils();
	String collectionDate=DateUtils.getDate();
	String collectionTime=DateUtils.getTimeHHmm();
	String currency=plantMstDAO.getBaseCurrency(plant);
	String promotion_name = "",promotion_desc = "",outletname="", by_value = "",customer_type_id ="",start_date ="",start_time ="",end_date ="",end_time ="",limit_of_usage ="",notes ="",isactive = "";
	String sOutlet = StrUtils.InsertQuotes(StrUtils.fString(request.getParameter("OUTLET_NAME")));
	String sOutCode = StrUtils.InsertQuotes(StrUtils.fString(request.getParameter("OUTCODE")));
	PosPrd_ClassPromotionHdr posheader = posPromotionHdr.getPosHdrById(plant, ID);
    List viewlistQry = posPromotionHdr.getCategoryPromotionDetails(plant,ID);
    for (int i = 0; i < viewlistQry.size(); i++) {
        Map map = (Map) viewlistQry.get(i);
        promotion_name = StrUtils.fString((String)map.get("PROMOTION_NAME"));
        promotion_desc = StrUtils.fString((String)map.get("PROMOTION_DESC"));
        //sOutCode = StrUtils.fString((String)map.get("OUTLET"));
        //sOutlet = StrUtils.fString((String)map.get("OUTLET_NAME"));
        by_value = StrUtils.fString((String)map.get("BY_VALUE"));
        customer_type_id = StrUtils.fString((String)map.get("CUSTOMER_TYPE_ID"));
        start_date = StrUtils.fString((String)map.get("START_DATE"));
        start_time = StrUtils.fString((String)map.get("START_TIME"));
        end_date = StrUtils.fString((String)map.get("END_DATE"));
        end_time = StrUtils.fString((String)map.get("END_TIME"));
//         limit_of_usage = StrUtils.fString((String)map.get("LIMIT_OF_USAGE"));
        notes = StrUtils.fString((String)map.get("NOTES"));
	 	isactive = StrUtils.fString((String)map.get("IsActive"));
	 	OutletBeanDAO outletdao = new OutletBeanDAO();
	 	 sOutCode = StrUtils.fString((String)map.get("OUTLET"));
	 	String outlet_id = String.valueOf(sOutCode);
	 	if(outlet_id.length() > 0){
	 		 outletname = outletdao.getOutletname(plant,outlet_id);
	 	}
    }
	 String COUNTRYCODE = "",DISPLAY="",DISPLAYID="";
	 List curQryList=new ArrayList();
	 curQryList = currencyDAO.getCurrencyDetails(currency,plant);
	 for(int i =0; i<curQryList.size(); i++) {
			ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
			DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
			DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
	 }
		
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
    right: -40% !important;
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
<script src="../jsp/js/EditCategoryPromotion.js"></script>
<center>
	<h2>
		<small class="success-msg"><%=msg%></small>
	</h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>	
                 <li><a href="../categorypromotion/summary"><span class="underline-on-hover">Category Promotion Summary</span></a></li>
                <li><label>Edit Category Promotion</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
				<h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../categorypromotion/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>

		<div class="container-fluid">
			<form class="form-horizontal" id="categorypromotionForm" name="cpoform" action="../categorypromotion/edit"  method="post"  enctype="multipart/form-data">
				<input type="hidden" name="plant" value="<%=plant%>" >
				<input type="hidden" name="CURRENCYID" value="<%=DISPLAYID%>">
				<input type = "hidden" name="COUNTRYCODE"  value="<%=COUNTRYCODE%>">
				<INPUT type="hidden" name="curency"  value="<%=DISPLAYID%>">
	            <INPUT type="hidden" name="BASECURRENCYID"  value="<%=currency%>">
	            <INPUT type="hidden" name="HDRID"  value="<%=ID%>">
	            <INPUT type="hidden" name="PRMDESC"  value="<%=by_value%>">
	            
	            				<div class="form-group">
				<label class="control-label col-form-label col-sm-2 required" for="Promotion name">Promotion Name:</label>
        			<div class="col-sm-4">
        				<INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="100" name="PROMOTION_NAME" id="PROMOTION_NAME" value="<%=promotion_name%>" placeholder="Max 100 Characters">
       				</div>
				</div> 

				<div class="form-group">
				<label class="control-label col-form-label col-sm-2 required">Promotion Description:</label>
					<div class="col-sm-4 ac-box">
						<INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="100" name="PROMOTION_DESC" id="PROMOTION_DESC"   value="<%=promotion_desc%>" placeholder="Max 100 Characters">
					</div>
					
				<div class="col-sm-4">
				    <label class="radio-inline">
			      		<input type="radio" name="BYQTY" id="BYQTY" value=0  <%if(by_value.equals("0")){%> checked <%}%> onclick="SelectVal(event, 'byqty')">By Quantity</label>
			    	<label class="radio-inline">
			      		<input type="radio" name="BYQTY" id="BYQTY" value=1 <%if(by_value.equals("1")){%> checked <%}%> onclick="SelectVal(event, 'byval')">By Value</label>
			    </div>
				</div>
				
				<div class="form-group">
	      <label class="control-label col-form-label col-sm-2 required" for="Outlet">Outlet</label>
	      	<div class="col-sm-4 ac-box">
	      	<div class="input-group">   
	      		<INPUT class=" form-control" id="OUTLET_NAME" value="<%=outletname%>" name="OUTLET_NAME" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select Outlet">
    		 	<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'OUTLET_NAME\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i></span>    
					<INPUT type="hidden" name="OUTCODE" value="<%=sOutCode%>">
	  		</div>
	        </div>
	</div>
				
				<div class="form-group" style="display:none;">
				<label class="control-label col-form-label col-sm-2" for="supplier name">Customer Type:</label>
        			<div class="col-sm-4">
    		 			<INPUT class=" form-control" id="CUSTOMER_TYPE"  name="CUSTOMER_TYPE"  value="<%=customer_type_id%>" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select Customer Type">
    		 			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE\']').focus()">
						<i class="glyphicon glyphicon-menu-down"></i></span>    						
        			</div>
        		</div>
        		
        						<div class="form-group">
				<label class="control-label col-form-label col-sm-2" for="Start date">Start Date:</label>
					<div class="col-sm-4 ac-box">
						<INPUT class="form-control datepicker" type="TEXT" size="30" MAXLENGTH="10" name="START_DATE" id="START_DATE"  value="<%=start_date%>" readonly />
					</div>
				
				<div class="col-sm-6 no-padding">
				<label class="control-label col-form-label col-sm-3" for="time">Start Time:</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="form-control" id="START_TIME" name="START_TIME"  value="<%=start_time%>"  maxlength="5" placeholder="HH:MM" readonly>
					</div>
			    </div>
			    </div>
			    
			    <div class="form-group">
				<label class="control-label col-form-label col-sm-2" for="End Date">End Date:</label>
					<div class="col-sm-4 ac-box">
						<INPUT class="form-control datepicker" type="TEXT" size="30" MAXLENGTH="10" name="END_DATE" id="END_DATE"  value="<%=end_date%>" readonly />
					</div>
				
				<div class="col-sm-6 no-padding">
				<label class="control-label col-form-label col-sm-3" for="time">End Time:</label>
					<div class="col-sm-4 ac-box">
						<input type="text" class="form-control" id="END_TIME" name="END_TIME"  value="<%=end_time%>" maxlength="5" placeholder="HH:MM" readonly>
					</div>
			    </div>
			    </div>
			    
			    <%-- <div class="form-group">
					 <label class="control-label col-form-label col-sm-2 required" for="Limit of usage">Limit Of Usage:</label>
        			<div class="col-sm-4">
        				<INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="20" value="<%=limit_of_usage%>" name="LIMIT_USAGE" id="LIMIT_USAGE" onkeypress="return isNumberKey(event,this,4)">
       				</div>
				</div> --%>
				
			    <div class="form-group">
			  	<div class="col-sm-offset-2 col-sm-8">
				    <label class="radio-inline">
			      		<input type="radio" <%if(isactive.equals("Y")){%> checked <%}%> name="ACTIVE" value="Y">Active</label>
			    	<label class="radio-inline">
			      		<input type="radio" name="ACTIVE" <%if(isactive.equals("N")){%> checked <%}%> value="N">Non Active</label>
			    </div>
			</div>

				<div class="row" style="margin: 0px;">
					<table class="table table-bordered line-item-table po-table">
						<thead>
							<tr>
								<th colspan=2 style="width:15%">Buy Category</th>
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
									<input type="text" name="item_category" class="form-control ProductClass" onchange="checkprdcat(this.value,this)" style="width:87%"  placeholder="Type or click to select an Category">
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
									 data-eq="0.000" data-aq="0.000" value="1.000" onchange="calculateAmount(this)" onkeypress="return isNumberKey(event,this,4)">
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
									<input type="text" name="promotionvalue" onkeypress="return isNumberKey(event,this,4)" class="form-control">
								</td>
								<td class="bill">
									<input type="text" name="LIMIT_USAGE" onkeypress="return isNumberKey(event,this,4)" class="form-control">
								</td>
							</tr>
						</tbody>
					</table>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<a class="add-line" style="text-decoration: none; cursor: pointer;" onclick="addRow()">
							+ Add another line
						</a>
					</div>
				</div>
				
				<div class="row grey-bg">
					<div class="col-sm-6 notes-sec">
						<p>Notes</p>
						<div>
							<textarea rows="2" name="NOTES" id="NOTES" class="ember-text-area form-control ember-view"
								placeholder="Max 1000 characters"  maxlength="1000"><%=notes%></textarea>
						</div>

					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 txn-buttons">
					      <div class="dropup">
							<button id="btnSalesOpen" type="button" class="btn btn-success">Save</button>
							<button type="button" class="btn btn-default" onclick="window.location.href='../home'">Cancel</button>
					  </div>
					</div>
				</div>

			</form>
		</div>
	</div>
</div>

<!-- ----------------Modal-------------------------- -->

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

function popUpWin(URL) {
	 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}


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

$(".po-table tbody").on('click','.accrmv',function(){
	debugger;	    
    var obj = $(this);
    var timestamp = new Date().getUTCMilliseconds();
    kayid = "key"+timestamp;
    $(obj).closest('td').attr('id', kayid); 
    $("input[name ='ukey']").val(kayid);
});

$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip(); 

		});

</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>