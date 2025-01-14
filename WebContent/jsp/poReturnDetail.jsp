<%@page import="com.track.dao.CustomerBeanDAO"%>
<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String title = "Purchase Order Return Detail";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String pono = StrUtils.fString(request.getParameter("PONO"));
	String grno = StrUtils.fString(request.getParameter("GRNO"));
	String bill = StrUtils.fString(request.getParameter("BILL"));
	String vendname = StrUtils.fString(request.getParameter("VEND_NAME"));
	String returnDate = StrUtils.fString(request.getParameter("RETURNDATE"));
	String prno = StrUtils.fString(request.getParameter("PORETURN"));
	
	String fieldDesc=StrUtils.fString(request.getParameter("result"));
	
	boolean displayApplyToCredit=false,displayExportExcel=false;
	if(systatus.equalsIgnoreCase("ACCOUNTING")) {
		displayApplyToCredit = ub.isCheckValAcc("applycreditnotepurchasereturn", plant,username);
		displayExportExcel = ub.isCheckValAcc("excelpurchasereturn", plant,username);	
	}
	
	if(systatus.equalsIgnoreCase("INVENTORY"))
	{
		displayApplyToCredit = ub.isCheckValinv("applycreditnotepurchasereturn", plant,username);
		displayExportExcel = ub.isCheckValinv("excelpurchasereturn", plant,username);	
	}
	
	ReturnOrderUtil roUtil = new ReturnOrderUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	
	ArrayList plntList = _PlantMstDAO.getPlantMstDetails(plant);
	Map plntMap = (Map) plntList.get(0);
	String PLNTDESC = (String) plntMap.get("PLNTDESC");
	String ADD1 = (String) plntMap.get("ADD1");
	String ADD2 = (String) plntMap.get("ADD2");
	String ADD3 = (String) plntMap.get("ADD3");
	String ADD4 = (String) plntMap.get("ADD4");
	String STATE = (String) plntMap.get("STATE");
	String COUNTRY = (String) plntMap.get("COUNTY");
	String ZIP = (String) plntMap.get("ZIP");
	
	String fromAddress_BlockAddress = ADD1 + " " + ADD2;
	String fromAddress_RoadAddress = ADD3 + " " + ADD4;
	String fromAddress_Country = STATE + " " + COUNTRY;
	
	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + plant.toLowerCase() + DbBean.LOGO_FILE;
	File checkImageFile = new File(imagePath);
	if (!checkImageFile.exists()) {
		imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
	}
	CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	String vendno =customerBeanDAO.getVendorCode(plant, vendname);
	List<Map<String, String>> returnOrderList = roUtil.getPOReturnDetailsbyprno(prno,pono,grno,plant);
	boolean applycreditstatus = false;
	for(Map<String, String> map:returnOrderList){
		if(!map.get("STATUS").equalsIgnoreCase("Applied")){
			applycreditstatus = true;
		}
	}
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	 <jsp:param name="submenu" value="<%=IConstants.PURCHASE_RETURN%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/font-awesome.min.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script src="../jsp/js/jspdf.debug.js"></script>
<script src="../jsp/js/jspdf.plugin.autotable.js"></script>
<script src="../jsp/js/Printpagepdf.js"></script>
<style>
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
.text-ellipsis {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}
.ribbon {
    position: absolute!important;
    top: -5px;
    left: -5px;
    overflow: hidden;
    width: 96px;
    height: 94px;
}
.ribbon .ribbon-draft {
    background-color: #94a5a6;
    border-color: #788e8f;
}
.ribbon .ribbon-inner {
    text-align: center;
    color: #fff;
    top: 24px;
    left: -31px;
    width: 135px;
    padding: 3px;
    position: relative;
    transform: rotate(-45deg);
}
.ribbon .ribbon-inner:before {
    left: 0;
    border-left: 2px solid transparent;
}
.ribbon .ribbon-inner:after {
    right: -2px;
    border-bottom: 3px solid transparent;
}
.ribbon .ribbon-inner:after, .ribbon .ribbon-inner:before {
    content: "";
    border-top: 5px solid transparent;
    border-left: 5px solid;
    border-left-color: inherit;
    border-right: 5px solid transparent;
    border-bottom: 5px solid;
    border-bottom-color: inherit;
    position: absolute;
    top: 20px;
    transform: rotate(-45deg);
}
</style>
<center>
	<h2><small class="error-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../purchasereturn/summary"><span class="underline-on-hover">Purchase Order Return Summary</span> </a></li>
                <li><label>Purchase Order Return Detail</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<div class=" pull-right">
			<% if (displayExportExcel) { %>
				<div class="btn-group" role="group">
					<button type="button" class="btn btn-default" onclick="ExportReport()"
					data-toggle="tooltip"  data-placement="bottom" title="Excel">
						<i class="fa fa-file-excel-o" aria-hidden="true"></i>
					</button>
				</div>
				<% } %>
				<% if (displayApplyToCredit) { %>
				<%if(applycreditstatus){ %>
				&nbsp;
				<%if(!systatus.equalsIgnoreCase("INVENTORY")){%>
				<div class="btn-group" role="group">
					<button type="button pull-right" class="btn btn-default" 
					data-toggle="modal" data-target="#creditListModal" onclick="coverttocredit()">Apply To Debit Note</button>
				</div>
				<%} %>
				&nbsp;
				<%} %>
				<% } %>
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
					onclick="window.location.href='../purchasereturn/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
			</div>
		</div>
		<div style="position:relative;padding: 0px 20px;box-shadow: 0 0 6px #ccc;">
			<div id="ember1308" class="ribbon text-ellipsis tooltip-container ember-view">
				<div class="ribbon-inner ribbon-draft"><%=returnOrderList.get(0).get("STATUS")%></div>
			</div>
			<div style="height: 0.700000in;"></div>
			<span id="print_id">
			<div class="row">
				<div class="col-xs-6">
				<img src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>"
					style="width: 130.00px;" id="logo_content">
				<div>
					<b><%=PLNTDESC%></b>
				</div>
<span style="white-space: pre-wrap; word-wrap: break-word;">
<%=fromAddress_BlockAddress%>
<%=fromAddress_RoadAddress%>
<%=fromAddress_Country%> <%=ZIP%>
</span>
				</div>
			</div>
				<div class="row">
			<div class="col-xs-12">
				<table id="table2" class="table">
					<thead>
						<tr>
							<td>#</td>
							<td>PO RETURN NO</td>
							<td>ORDER NO</td>
							<td>GRNO</td>
							<td>BILL</td>
							<td>RETURN DATE</td>
							<td>PRODUCT</td>
							<td>RETURN QTY</td>
							<td>DEBIT NOTE QTY</td>
						</tr>
					</thead>
					<tbody>
					<%for(int i =0; i<returnOrderList.size(); i++) {   
				  		Map m=(Map)returnOrderList.get(i);
				  		float conretqty =Float.parseFloat((String) m.get("RETURN_QTY"));
				  		String retqty=StrUtils.addZeroes(conretqty, "3");
				  		String dnqty = StrUtils.addZeroes(Double.valueOf((String) m.get("CREDITED_QTY")), "3");
			  		%>
				  		<tr>
							<td class="text-center"><%=i+1 %></td>
							<td class="text-center"><%=m.get("PORETURN") %></td>
							<td class="text-center"><%=m.get("PONO") %></td>
							<td class="text-center"><%=m.get("GRNO")%></td>
							<td class="text-center"><%=m.get("BILL")%></td>
							<td class="text-center"><%=m.get("RETURN_DATE")%></td>
							<td class="text-center"><%=m.get("ITEM")%></td>
							<td class="text-center"><%= retqty %></td>
							<td class="text-center"><%= dnqty %></td>
						</tr>
				  	<%}%>
					</tbody>
				</table>
			</div>
		</div>
			</span>
		</div>
	</div>
</div>
<form name="form" method="post" action="" hidden>
	<input type="hidden" name="PONO" value="<%=pono%>">
	<input type="hidden" name="GRNO" value="<%=grno%>">
	<input type="hidden" name="BILL" value="<%=bill%>">
	<input type="hidden" name="VEND_NAME" value="<%=vendname%>">
	<input type="hidden" name="RETURNDATE" value="<%=returnDate%>">
</form>
<script>
function ExportReport(){
	document.form.action ="/track/ReturnOrderServlet?Submit=ExportExcelPurchaseOrderReturnSummary";
  	document.form.submit();
 }
 function coverttocredit(){
	 window.location.href = "../purchasereturn/converttocredit?action=View&PORETURN=<%=prno%>&PONO=<%=pono%>&GRNO=<%=grno%>";
 }
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>