<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Alternate Brand Product Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Alternate Brand Product', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

</script>

<%
	session = request.getSession();
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	String res = "";
    String sItemEnb   = "enabled";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "disabled";
	String sUpdateEnb = "enabled";
	sAddEnb = "enabled";
	String action = "";

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	ItemUtil itemUtil = new ItemUtil();
	itemUtil.setmLogger(mLogger);
	DateUtils dateutils = new DateUtils();
	ItemMstUtil itemMstUtil=new ItemMstUtil();
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	String sItemId     = StrUtils.fString(request.getParameter("ITEM"));
	String sItemDesc      = StrUtils.fString(request.getParameter("DESC"));
	String productBrandID    = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
	String sAlternateBrandItem    = StrUtils.fString(request.getParameter("ALTERNATEBRANDITEM"));
	String sAlternateBrandItemDesc      = StrUtils.fString(request.getParameter("ALTERNATEBRANDDESC"));
	String alternateProductBrandID    = StrUtils.fString(request.getParameter("ALTERNATE_PRD_BRAND_ID"));
	String vinno    = StrUtils.fString(request.getParameter("VINNO"));
	String model    = StrUtils.fString(request.getParameter("MODEL"));
	String alternatevinno      = StrUtils.fString(request.getParameter("ALTERNATEVINNO"));
	String alternatemodel    = StrUtils.fString(request.getParameter("ALTERNATEMODEL"));
	String EDIT    = StrUtils.fString(request.getParameter("EDIT"));
    System.out.println("EDIT" +EDIT);
    List resultList=new ArrayList<>();
    List altPrdList;
    Map altItem=new HashMap<>();
    if(action.equalsIgnoreCase("EDIT"))
    {
    	resultList =itemMstUtil.getMasterProductListByItem(sItemId,plant,"");
    	altItem = (Map) resultList.get(0);
    }
    else
    {
    	altItem.put("ITEM","");
    	altItem.put("ITEMDESC","");
    	altItem.put("PRD_BRAND_ID","");
    	altItem.put("VINNO","");
    	altItem.put("MODEL","");
    }

%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../alternateproduct/summary"><span class="underline-on-hover">Alternate Brand Product Summary</span></a></li>                         
                <li><label>Alternate Brand Product Details</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
		 <div class="box-header menu-drop">
           
              <h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
            <div class=" pull-right">
            &nbsp;
				<h1 style="font-size: 18px; cursor: pointer; vertical-align:middle;" class="box-title pull-right"
						onclick="window.location.href='../alternateproduct/summary'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
		</div>
		</div>
 <div class="box-body">
  <CENTER><strong><%=res%></strong></CENTER>


  <form class="form-horizontal" name="form" method="post" enctype="multipart/form-data" onSubmit="return onCheck();">
 <INPUT type = "hidden"  name="EDIT" value="<%=EDIT%>"> 
 <input type="text" name="plant" value="<%=plant%>" hidden>
  		<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Product ID</label>
					<div class="col-sm-6 ac-box">
						<div class="input-group">
							<input type="text" class="ac-selected  form-control typeahead" readonly
								id="ITEM" placeholder="Select a Product" name="ITEM"
								value="<%=altItem.get("ITEM")%>"> 
						</div>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Product Description:</label>
					<div class="col-sm-4">
					<div class="input-group">
						<INPUT class="form-control typeahead" readonly name="DESC" id="DESC" placeholder="Select Product Desc" type="TEXT" value="<%=altItem.get("ITEMDESC")%>" size="20" MAXLENGTH=100>
			   		 	</div>
					</div>
				</div>
  		 <div class="form-group">
	      	<label class="control-label col-form-label col-sm-2" for="Product Brand">Product Brand ID:</label>
	      	<div class="col-sm-4">
	      	<div class="input-group">
	    	<input name="PRD_BRAND_ID" id="PRD_BRAND_ID" type="TEXT" readonly value="<%=altItem.get("PRD_BRAND_ID")%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	
  		</div>
  		</div>
  		</div>
  		
  		 <div class="form-group">
	      	
	      	<label class="control-label col-form-label col-sm-2" for="VINNO">VIN No.:</label>
	      	<div class="col-sm-4">
	      	<div class="input-group">
	    	<input name="VINNO" id="VINNO" type="TEXT" readonly value="<%=altItem.get("VINNO")%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	
  		</div>
  		</div>
  		</div>
  		
  		 <div class="form-group">
	      	
	      	<label class="control-label col-form-label col-sm-2" for="MODEL">Model:</label>
	      	<div class="col-sm-4">
	      	<div class="input-group">
	    	<input name="MODEL" id="MODEL" type="TEXT" readonly value="<%=altItem.get("MODEL")%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	
  		</div>
  		</div>
  		</div>
  		
  		<div class="row" style="margin: 0px;">
					<table class="table table-bordered line-item-table alt-table">
						<thead>
							<tr>
								<th class="alt_bpid" colspan=2>Alt. Brand Product ID</th>
								<th class="alt_bd">Alt. Brand Description</th>
								<th class="alt_pbid">Alt. Product Brand ID</th>
								<th class="alt_vinno">Alt. VIN No.</th>
								<th class="alt_model">Alt. Model</th>
							</tr>
						</thead>
						<tbody id="alt-body">
						<%if (action.equalsIgnoreCase("EDIT")) {
							for(int i=0;i<resultList.size();i++)
							{
								Map master = (Map) resultList.get(i);
								String altProdId=master.get("ALTERNATE_ITEM_NAME").toString();
								altPrdList =itemMstUtil.getAltProductListByItem(altProdId,plant,"");
									Map m = (Map) altPrdList.get(0);
									String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
				                    String prdimage=((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath);
						%>
							<tr>
								<td class="item-img text-center">
									<!-- <span class="glyphicon glyphicon-picture"></span>  --> <img
									alt="" src="<%=prdimage%>" style="width: 100%;"> <!-- 				  <input type="hidden" name="lnno" value="1"> -->
								</td>
								<td class="alt-bpid" style="width: 30%;"><INPUT class="form-control alt_productsearch" name="ALTERNATEBRANDITEM" type="TEXT" readonly value="<%=m.get("ALTERNATE_ITEM_NAME")%>"
				MAXLENGTH=50> 
   								</td>
								<td class="alt-bd" style="width: 22%;"><INPUT class="form-control alt_productdesc" name="ALTERNATEBRANDDESC" type="TEXT" readonly value="<%=m.get("ITEMDESC")%>" size="20" MAXLENGTH=100>
								</td>
								<td class="alt-pbid" style="width: 16%;"><input name="ALTERNATE_PRD_BRAND_ID" type="TEXT" readonly value="<%=m.get("PRD_BRAND_ID")%>"
			size="50" MAXLENGTH=100 class="form-control"></td>
								<td class="alt-vinno" style="width: 16%;"><input name="ALTERNATEVINNO" type="TEXT" readonly value="<%=m.get("VINNO")%>"
			size="50" MAXLENGTH=100 class="form-control"></td>
								<td class="alt-model" style="width: 16%;position:relative;">								
								<input name="ALTERNATEMODEL" type="TEXT" readonly value="<%=m.get("MODEL")%>"
			size="50" MAXLENGTH=100 class="form-control"></td>
							</tr>
						<%}
						}else{ %>
							
						<%} %>
							
						</tbody>
					</table>
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
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

