<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Alternate Product Barcode Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/itemsummary.js"></script>
<script language="javascript">
function setCheckedValue(temp){
	
}
var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }
     
function isNumericInput(strString) {
	var strValidChars = "0123456789";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for (i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
} 

function ExportReport(){
	document.form.action = "/track/ImportItemServlet?action=AlternetItemExport_Excel";
	document.form.submit();
	
} 

/* function onGo(){
	document.form.action ="AlternetItemSummary.jsp";
var recsPerPage = document.form.RECS_PER_PAGE.value;
 if (isNumericInput(recsPerPage) == false) {
                    alert("Entered Records display Per Page as Number!");
                    document.form.RECS_PER_PAGE.value = "";
                    document.form.RECS_PER_PAGE.focus();
                    return false;
} 
  document.form.submit();

} */
</script>

<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	_userBean.setmLogger(mLogger);
        long listRec=0;
	List locQryList  = new ArrayList();
	String fieldDesc="";
	String PLANT="",ITEM ="",ITEM_DESC="",PRD_CLS_ID="",PRD_DEPT_ID="",PRD_BRAND_ID = "",PRD_TYPE_ID="",view_Inv="",RECS_PER_PAGE="";
   


	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	session=request.getSession();
	String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	String systatus = session.getAttribute("SYSTEMNOW").toString();
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	ITEM     = strUtils.fString(request.getParameter("ITEM"));
	ITEM_DESC = strUtils.fString(request.getParameter("ITEM_DESC"));
        PRD_CLS_ID =  strUtils.fString(request.getParameter("PRD_CLS_ID"));
        PRD_TYPE_ID =  strUtils.fString(request.getParameter("PRD_TYPE_ID"));
        PRD_DEPT_ID =  strUtils.fString(request.getParameter("PRD_DEPT_ID"));//resvi
        PRD_BRAND_ID =  strUtils.fString(request.getParameter("PRD_BRAND_ID"));
        RECS_PER_PAGE = strUtils.fString(request.getParameter("RECS_PER_PAGE"));
//         RESVI INCLUDES
        PlantMstDAO _PlantMstDAO = new PlantMstDAO();
        DateUtils _dateUtils = new DateUtils();
        String collectionDate=_dateUtils.getDate();
        ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
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
//         ENDS
        boolean displaySummaryExport=false,displaySummaryImport=false;
        if(systatus.equalsIgnoreCase("ACCOUNTING"))
    	{
    	displaySummaryExport = ub.isCheckValAcc("exportalternateItem", PLANT,username);
    	displaySummaryImport = ub.isCheckValAcc("importalternateItem", PLANT,username);
    	}
    	if(systatus.equalsIgnoreCase("INVENTORY"))
    	{
    	displaySummaryExport = ub.isCheckValinv("exportalternateItem", PLANT,username);
    	displaySummaryImport = ub.isCheckValinv("importalternateItem", PLANT,username);
    	}
    int curPage      = 1;
  //  int recPerPage   = Integer.parseInt(DbBean.STKTAKE_NUMOFREC);
  if(RECS_PER_PAGE.length()==0) RECS_PER_PAGE = "100";
    int recPerPage   = Integer.parseInt(RECS_PER_PAGE);
    long totalRec     = 0;
    String isDisabled ="disabled";
        view_Inv      = strUtils.fString(request.getParameter("VIEW_INV"));
String currentPage        = strUtils.fString(request.getParameter("cur_page"));

String listRecSize =strUtils.fString(request.getParameter("listRecSize")); 
if(listRecSize.length()==0){
listRecSize ="0";
}
	ItemUtil itemUtil = new ItemUtil();
	itemUtil.setmLogger(mLogger);
	if(PGaction.equalsIgnoreCase("View")){
	 try{
               currentPage="1";
                listRec=new ItemSesBeanDAO().getProductCount(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,PLANT,"");
               int start =  Integer.parseInt(currentPage);
               int end = Integer.parseInt(currentPage)*recPerPage;
	      Hashtable ht = new Hashtable();
	      if(strUtils.fString(PLANT).length() > 0)
	      ht.put("PLANT",PLANT);
	      locQryList= itemUtil.queryAlternetItemMstForSearchCriteriaNew(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,PLANT," ORDER BY ITEM",start,end);
	      if(locQryList.size()== 0)
	      {
	        fieldDesc="Data Not Found";
	      }
             if (listRec >0 )  {         totalRec = listRec; listRecSize=new Long(totalRec).toString();
             isDisabled="";
             }
             if (currentPage.length() > 0)
             {
            try  {   curPage = (new Integer(currentPage)).intValue(); 
            System.out.println("curPage :: "+curPage); 
            }
            catch (Exception e)   {   curPage = 1;                                      }
            }
     	 }catch(Exception e) { }
         
   
}
if(PGaction.equalsIgnoreCase("PREVORNEXT")){
 try{

int start = (new Integer(currentPage)-1)*recPerPage;
int end = (new Integer(currentPage)*recPerPage)+1;
   Hashtable ht = new Hashtable();
	      if(strUtils.fString(PLANT).length() > 0)
	      ht.put("PLANT",PLANT);
	      locQryList= itemUtil.queryAlternetItemMstForSearchCriteriaNew(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,PLANT,"",start,end);
     if (locQryList != null)  {         totalRec = locQryList.size();
     isDisabled="";
     }
     if (currentPage.length() > 0)
     {
     
    try                   {   curPage = (new Integer(currentPage)).intValue(); System.out.println("curPage :: "+curPage); }
    catch (Exception e)   {   curPage = 1;                                      }
    }
     
   
   
 }catch(Exception e) {System.out.println("Exception :getStockTakeList"+e.toString()); }
}

 int totalPages = (Integer.parseInt(listRecSize) + recPerPage -1)/recPerPage;
    if (curPage > Integer.parseInt(listRecSize)) curPage = 1;                    // Out of range

    System.out.println("totalPages :: "+totalPages);
    System.out.println("curPage :: "+curPage);
%>
<SCRIPT LANGUAGE="JavaScript">

    var cur_page     = <%=curPage%>;                            // Current display page
    var total_pages   = <%=totalPages%>;                         // The total number of records

    // Display previous page of user list
    function onPrev()
    {
	if (cur_page <= 1)  return false;
	cur_page = parseInt(cur_page) -1;
	document.form.cur_page.value = cur_page;
       document.form.PGaction.value="PREVORNEXT";
      document.form.action = "AlternetItemSummary.jsp";
	document.form.submit();
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // Display next page of user list
    function onNext()
    {
    if (cur_page >= total_pages)  {
      return false;
    }else{
      cur_page = parseInt(cur_page) + 1;
      document.form.cur_page.value = cur_page;
      document.form.PGaction.value="PREVORNEXT";
      document.form.action = "AlternetItemSummary.jsp";
      document.form.submit();
      }
    }
 </SCRIPT>
<div class="container-fluid m-t-20">
	<div class="box">
	<!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li class="underline-on-hover"><a href="../home">Dashboard </a></li>                       
                <li><label>Alternate Product Barcode Summary</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <div class="box-title pull-right">
              <div class="btn-group" role="group">
              <% if (displaySummaryImport) { %>
              <button type="button" class="btn btn-default"
						onClick="window.location.href='../product/importalterprd'">
						Import Alternate Product Barcode</button>
					&nbsp;
					<% }%>
					
					  		<% if (displaySummaryExport) { %>
  		<button type="button" class="Submit btn btn-default"  onClick="ExportReport();">Export All Data</button>&nbsp;
  		 <% } %>
					
				</div>
				
				<h1
					style="font-size: 18px; cursor: pointer; position: relative; bottom: -7px;"
					class="box-title pull-right"
					onclick="window.location.href='../home'">
					<i class="glyphicon glyphicon-remove"></i>
				</h1>
              </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form" method="post" action="AlternetItemSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<INPUT type="hidden" name="listRecSize" value="<%=listRecSize%>">
<INPUT type="hidden" name="VIEW_INV" value="">

  <center><small><font class="mainred"> <%=fieldDesc%></font></small></center>
  
    <%-- <center><h1><small><font class="mainred"> <%=fieldDesc%></font></small></h1></center> --%>
   
<div id="target" style="display:none"> 
   		<div class="form-group">
<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div><div class="col-sm-4 ac-box">
				<div class="input-group"> 
				<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
				<INPUT type="hidden" name="plant" value=<%=PLANT%>>    
				<input type="text" class="ac-selected  form-control typeahead item" id="ITEM" placeholder="Product" name="ITEM">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/view_product_list.jsp?ITEM='+form.ITEM.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>	 -->
				</div>
			</div>
  		<div class="col-sm-4 ac-box">
				<div class="input-group">
				<input type="text" class="ac-selected  form-control typeahead itemdesc" id="ITEM_DESC" placeholder="Product Description" name="ITEM_DESC">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'ITEM_DESC\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/view_product_list.jsp?ITEM_DESC='+form.ITEM_DESC.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  -->
				</div>
			</div>
  		</div>
  		
	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="input-group">  		
  		<input type="text" class="ac-selected  form-control typeahead ProductDept" id="PRD_DEPT_ID" placeholder="Product Department" name="PRD_DEPT_ID">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/list/ListProductClass.jsp?PRD_CLS_ID='+form.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  		</div>
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="input-group">  		
  		<input type="text" class="ac-selected  form-control typeahead ProductClass" id="PRD_CLS_ID" placeholder="Product Category" name="PRD_CLS_ID">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_CLS_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/list/ListProductClass.jsp?PRD_CLS_ID='+form.PRD_CLS_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  		</div>
  		</div>
  		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
  		<div class="input-group">
  		<input type="text" class="ac-selected  form-control typeahead ProductType" id="PRD_TYPE_ID" placeholder="Product sub category " name="PRD_TYPE_ID">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_TYPE_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/list/ListProductType.jsp?PRD_TYPE_ID='+form.PRD_TYPE_ID.value);"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span>  		 -->
  		</div>
  		</div>
  		
  		<div class="col-sm-4 ac-box">
  		<div class="input-group">  		
  		<input type="text" class="ac-selected  form-control typeahead ProductBrand" id="PRD_BRAND_ID" placeholder="Product Brand" name="PRD_BRAND_ID">
				<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 				<span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/PrdBrandList.jsp?ITEM_ID='+form.PRD_BRAND_ID.value+'&Cond=OnlyActive');"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  		</div>
  		</div>
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		<div class="col-sm-4 ac-box">
  		<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
  		<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'">Back</button>
  		 <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button> -->
  		</div>
  		</div>
  		</div>
  		
  	
  		
	<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-10 txn-buttons">
			
			</div>
		</div>
 		</div>
 		 <br>   
        </div>
 		
 	  <div class="form-group">
      <div class="col-sm-3">
      <a href="#" class="Show" style="font-size: 15px;">Show Advanced Search</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide Advanced Search</a>
      </div>
      
       	   </div>
   
  		<div id="VIEW_RESULT_HERE" class="table-responsive">
  		<div id="tableMovementHistory_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer">
        <div class="row"><div class="col-sm-12">
        <div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
		                  </div> 
        <table id="tableMovementHistory" class="table table-bordred table-striped">
					<thead>
		            <tr role="row">
		            <th style="font-size: smaller;">S/N</th>
		            <th style="font-size: smaller;">Product ID</th>
		            <th style="font-size: smaller;">Product Description</th>
		            <th style="font-size: smaller;">Alternate Product Barcode</th>
		            </tr>
		            </thead>
<!-- 		            RESVI INCLUDES -->
		            <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
<!-- 						ENDS -->
				</table>
            		</div>
						</div>
					        </div>
  </div>
  <INPUT name="RECS_PER_PAGE" type = "hidden" value="<%=RECS_PER_PAGE%>" size="10"  MAXLENGTH=4>
   <INPUT name="ACTIVE" type = "hidden" value="" size="10"  MAXLENGTH=4>
    <INPUT name="PRD_BRAND_DESC" type = "hidden" value="" size="10"  MAXLENGTH=4>
   
  <script type="text/javascript">
 <%--  var tableData = [];
   
   	  <%
           for (int iCnt =0; iCnt<locQryList.size(); iCnt++){
				int iIndex = iCnt + 1;
	            String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
	            Vector vecItem   = (Vector)locQryList.get(iCnt);
	            
	            
	            
	            //float pricef = Float.parseFloat((String)vecItem.get(12));
	            //pricef = strUtils.Round(pricef,2);
       %>
       
       var rowData = [];
       rowData[rowData.length] = '<%=(String)vecItem.get(3)%>';
       rowData[rowData.length] = '<%=(String)vecItem.get(0)%>';
       rowData[rowData.length] = '<%=(String)vecItem.get(1)%>';
       rowData[rowData.length] = '<%=(String)vecItem.get(2)%>';
       tableData[tableData.length] = rowData;
     <%}%> --%>
         
  //var groupColumn = 1;
/*  $(document).ready(function(){
	 $('#tableMovementHistory').DataTable({
		 	"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]],
		  	data: tableData,
		  	//"columnDefs": [{"className": "t-right", "targets": []}],
			"orderFixed": [ groupColumn, 'asc' ], 
		  	"dom": "<'row'<'col-md-6'l><'col-md-6'Bf>>" +
			"<'row'<'col-md-6'><'col-md-6'>>" +
			"<'row'<'col-md-12't>><'row'<'col-md-12'ip>>",
	        buttons: [
	        	
	        ],
		  });	 
 }); */
 var plant = document.form.plant.value;
 var tabletype;
 var ITEM,ITEM_DESC,PRD_CLS_ID,PRD_DEPT_ID,PRD_TYPE_ID,PRD_BRAND_ID, groupRowColSpan = 6;
 function getParameters(){
		return {
			"ITEM":ITEM,
			"ITEM_DESC":ITEM_DESC,
			"PRD_CLS_ID":PRD_CLS_ID,
			"PRD_TYPE_ID":PRD_TYPE_ID,
			"PRD_BRAND_ID":PRD_BRAND_ID,
			"PRD_DEPT_ID":PRD_DEPT_ID,
			"PLANT":plant,
			"action":"GET_ALTERNATEPRODUCT_FOR_SUMMARY"
		}
	}
 
 function onGo(){
	 ITEM=document.form.ITEM.value;
	 ITEM_DESC=document.form.ITEM_DESC.value;
	 PRD_CLS_ID=document.form.PRD_CLS_ID.value;
	 PRD_TYPE_ID=document.form.PRD_TYPE_ID.value;
	 PRD_BRAND_ID=document.form.PRD_BRAND_ID.value;
	 PRD_DEPT_ID=document.form.PRD_DEPT_ID.value;
		   var urlStr = "/track/MasterServlet";
		   var groupColumn = 1;	
		    if (tabletype){
		    	tabletype.ajax.url( urlStr ).load();
		    }else{
		    	tabletype = $('#tableMovementHistory').DataTable({
					"processing": true,
					"lengthMenu": [[100, 500, 1000, 2500], [100, 500, 1000, "All"]],
					searching: true, // Enable searching
			        search: {
			            regex: true,   // Enable regular expression searching
			            smart: false   // Disable smart searching for custom matching logic
			        },
					"ajax": {
						"type": "GET",
						"url": urlStr,
						"data": function(d){
							return jQuery.isEmptyObject(getParameters()) ? d : getParameters();
						}, 
						"contentType": "application/x-www-form-urlencoded; charset=utf-8",
				        "dataType": "json",
				        "dataSrc": function(data){
				        	
					        	if(typeof data.SUPPLIERTYPELIST[0].ID === 'undefined'){
					        		return [];
					        	}else {				        		
					        		return data.SUPPLIERTYPELIST;
					        	}
				        	
				        }
				    },
			        "columns": [
			        	{"data": 'ID', "orderable": true},
			        	{"data": '0', "orderable": true},
			        	{"data": '1', "orderable": true},
			        	{"data": '2', "orderable": true},
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
			    	                	//columns: [':visible']
			                    		columns: [0,1,2,3]
			    	                },
			    	                title: '<%=title%>',
			    	                footer: true
			                    },
			                    {
			                    	extend : 'pdf',
		                            footer: true,
			                    	text: 'PDF Portrait',
			                    	exportOptions: {
			                    		//columns: [':visible']
			                    		columns: [0,1,2,3]
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
			                    		//columns: [':visible']
			                    		columns: [0,1,2,3]
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

		    	$("#tableMovementHistory_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
		            var searchTerm = $('.dataTables_filter input').val().toLowerCase(); // Get the search input value
		             var searchType = $('#searchType').val(); // Get the selected search type
		            if (!searchTerm) return true; // If no search term, show all rows

		           // var searchPattern = new RegExp('^' + searchTerm + '|\\B' + searchTerm + '\\B|' + searchTerm + '$', 'i'); // Match at beginning, middle, or end
		           var searchPattern;

		            // Define the search pattern based on the selected search type
		            if (searchType === 'first') {
		                searchPattern = new RegExp('^' + searchTerm, 'i'); // Match if the search term is at the start
		            } else if (searchType === 'middle') {
		                searchPattern = new RegExp('\\B' + searchTerm + '\\B', 'i'); // Match if the search term is in the middle
		            } else if (searchType === 'last') {
		                searchPattern = new RegExp(searchTerm + '$', 'i'); // Match if the search term is at the end
		            }
		            // Check each column in the row for a match
		            for (var i = 0; i < data.length; i++) {
		                var columnData = data[i].toLowerCase(); // Convert the column data to lowercase
		                if (columnData.match(searchPattern)) {
		                    return true; // Match found, include this row in results
		                }
		            }
		            return false; // No match, exclude this row from results
		        });

		        // Redraw table when the search input changes
		        $('.dataTables_filter input').on('keyup', function () {
		        	tableMovementHistory.draw();
		        });
		    }
		    
		}

 
  </script>
 
   <input type = "hidden" value= "Prev" onclick="return onPrev();" <%=isDisabled%> >&nbsp;
   <input type = "hidden" value= "Next" onclick="return onNext();" <%=isDisabled%> >&nbsp;
   <input type="hidden" name="cur_page" size="4" maxlength="4" value="<%=curPage%>" readonly >&nbsp;&nbsp;
	    
   
  </FORM>
  
  <SCRIPT>

    function validateProduct() {
        var productId = document.form.ITEM.value;
	var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=PLANT%>",
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					
                                    if (data.status == "100") {
                                            var resultVal = data.result;
                                            document.form.ITEM_DESC.value = resultVal.sItemDesc;
                                     } 
				}
			});
		
	}


              
</SCRIPT>
</div></div></div>
 
          <!-- Below Jquery Script used for Show/Hide Function-->
 <script>
 $(document).ready(function(){
	 onGo();
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
 });
 </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>