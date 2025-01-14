
<!-- PAGE CREATED BY : IMTHI -->
<!-- DATE 24-02-2023 -->
<!-- DESC : Inventory Valuation Summary-->
<!-- URL : inventory/valuationsummary-->

<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ItemMstUtil"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Inventory Valuation Summary";
%>
<%@include file="sessionCheck.jsp" %>
<%!@SuppressWarnings({"rawtypes", "unchecked"})%>
<jsp:include page="header2.jsp" flush="false">
<jsp:param value="<%=title%>" name="title"/>
	<jsp:param name="mainmenu" value="<%=IConstants.REPORTS%>"/>
<jsp:param name="submenu" value="<%=IConstants.INVENTORY%>"/>
</jsp:include>
<style type="text/css">
.dt-button-collection.dropdown-menu{
left: 50px; !important;
}
</style>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script src="../jsp/js/general.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script>

var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'SalesPerformance', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


function onGo(){
	var checkFound = false;
	var len = document.form1.chkdLoc.length;
	 var orderLNo; 
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form1.chkdLoc.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form1.chkdLoc.checked) {
			checkFound = true;
			
		}

		else {
			if (document.form1.chkdLoc[i].checked) {
				checkFound = true;
				
			}
		}

	}
	if (checkFound != true) {
		alert("Please check at least one checkbox.");
		return false;
	}	
document.form1.action="../inventory/valuationsummary";
document.form1.submit();
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
    var divContents = document.getElementById("tablePerformanceSummary_wrapper").innerHTML;
    printWindow.document.write(divContents);
    printWindow.document.write('</body>');

    printWindow.document.write('</html>');
    printWindow.document.close();
    printWindow.print();
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
 	/* .printtable td:nth-last-child(1),
	td:nth-last-child(2) {
	text-align: right;
	} */	 
    .page {
        width: 21cm;
        min-height: 28.7cm;
        margin: 1cm auto;
        border: 1px #D3D3D3 solid;
        border-radius: 5px;
        background: white;
        box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
    }
    .subpage {
        padding: 1cm;
        height: 237mm;
    } 
</style>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
InvUtil invUtil       = new InvUtil();
invUtil.setmLogger(mLogger);
ArrayList invQryList  = new ArrayList();
List saleList =null, stockin=null;
ShipHisDAO shipdao = new ShipHisDAO();
PlantMstDAO plantMstDAO = new PlantMstDAO();


String fieldDesc="";
String USERID ="",PLANT="",FROM_DATE ="",TO_DATE = "",fdate="",tdate="";
String  PRD_DEPT_ID="",PRD_CLS_ID="",PRD_BRAND_ID="",Loc="",ALLOWPRDNAME="";
boolean flag=false;

String PGaction        = StrUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
String loooc="";
String LOGIN_USER= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();
USERID= session.getAttribute("LOGIN_USER").toString();
PRD_DEPT_ID    = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
PRD_CLS_ID    = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
PRD_BRAND_ID    = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
String[] chkdLoc = request.getParameterValues("chkdLoc");
ALLOWPRDNAME = StrUtils.fString(request.getParameter("ALLOWPRDNAME"));//Resvi
FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
ArrayList al = _PlantMstDAO.getPlantMstDetails(PLANT);
Map map = (Map) al.get(0);
String collectionDate=DateUtils.getDate();
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
	
String curDate =DateUtils.getDateMinusDays();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))
	FROM_DATE=curDate;
if(ALLOWPRDNAME.equals(""))
{
	ALLOWPRDNAME="Department";
}
ItemMstUtil itemMstUtil = new ItemMstUtil();

itemMstUtil.setmLogger(mLogger);
String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);


boolean cntRec=false;

%>
<div class="container-fluid m-t-20">
	<div class="box">
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../inventory/reports"><span class="underline-on-hover">Inventory Reports</span></a></li>                                 
                <li><label>Inventory Valuation Summary</label></li>                                   
            </ul>             
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
				<div class="box-title pull-right">
                  <div class="btn-group" role="group">
<!--                   <button type="button" class="btn btn-default" onClick="javascript:ExportReport();" >Export All Data</button>  -->
            	<button type="button" class="btn btn-default printMe" onclick="PrintTable();"
					 data-toggle="tooltip"  data-placement="bottom" title="Print">
						<i class="fa fa-print" aria-hidden="true"></i>
					</button>
				</div>
				&nbsp;
               <h1 style="font-size: 18px;cursor:pointer;" class="box-title pull-right" onclick="window.location.href='../inventory/reports'">
              <i class="glyphicon glyphicon-remove"></i>
              </h1>
               </div>
		</div>
<div class="box-body">
<FORM class="form-horizontal" name="form1" method="post" action="../inventory/valuationsummary">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
  
  <span style="text-align: center;"><small><font color="red"> <%=fieldDesc%> </font></small></span>
  
		<div class="form-group">
		<div class="row"><div class="col-sm-2.5">		    	 
  		<label class="control-label col-sm-2" for="search">Search</label>
  		</div>
  		       <div class="col-sm-4 ac-box">
    	<INPUT class="ac-selected  form-control typeahead" name="PRD_DEPT_ID" ID="PRD_DEPT_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_DEPT_ID)%>" placeholder="PRODUCT DEPARTMENT" size="30"  MAXLENGTH=20>
    	<span class="select-icon"  onclick="$(this).parent().find('input[name=\'PRD_DEPT_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  		<div class="col-sm-4 ac-box">
				<div class=""> 
				<INPUT class="ac-selected  form-control typeahead" name="PRD_CLS_ID" ID="PRD_CLS_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_CLS_ID)%>" placeholder="PRODUCT CATEGORY" size="30"  MAXLENGTH=20>
    	<button type="button" style="position: absolute;margin-left: -22px;z-index: 2;vertical-align: middle;font-size: 20px;opacity: 0.5;" onclick="changeCategory(this)"><i class="glyphicon glyphicon-menu-down" style="font-size: 8px;"></i></button>
				</div>
			</div>			
  		</div>
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-4 ac-box">
		<div class=""> 
		<INPUT class="ac-selected  form-control typeahead" name="PRD_BRAND_ID" ID="PRD_BRAND_ID" type = "TEXT" value="<%=StrUtils.forHTMLTag(PRD_BRAND_ID)%>" placeholder="PRODUCT BRAND" size="30"  MAXLENGTH=20>
    	<span class="select-icon"   onclick="$(this).parent().find('input[name=\'PRD_BRAND_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>				
  		</div>
		<div class="col-sm-6 ac-box">
		<label class="radio-inline">
		<input name="ALLOWPRDNAME" id="ALLOWPRDNAME"  type="radio" value="Department" <%if(ALLOWPRDNAME.equalsIgnoreCase("Department")) {%>checked <%}%>>
  		Show by Department</label> 
   		<label class="radio-inline">
		<input name="ALLOWPRDNAME" id="ALLOWPRDNAME"  type="radio" value="Category" <%if(ALLOWPRDNAME.equalsIgnoreCase("Category")) {%>checked <%}%>>
  		Show by Category</label> 
   		<label class="radio-inline">
		<input name="ALLOWPRDNAME" id="ALLOWPRDNAME"  type="radio" value="Brand" <%if(ALLOWPRDNAME.equalsIgnoreCase("Brand")) {%>checked <%}%>>
  		Show by Brand</label> 
  		</div>
  		</div>
  		
  		<div class="row" style="padding: 0px;">
  		<div class="col-sm-2">
  		</div>
  		<label>
  			<input type="checkbox" class="form-check-input" style="border:0;" name="select" value="select" onclick="return checkAll(this.checked);">
			&nbsp; Select/Unselect All &nbsp;
			</label>
		
		</div>
  		
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  		</div>
  		<div class="col-sm-8 ac-box">
		<div class=""> 
		<table id="table" class="table table-bordred table-striped" > 
   
   <thead style="text-align: center"> 
          <tr>  
            <th style="font-size: smaller;">Chk</th>  
            <th style="font-size: smaller;">Location</th>
          </tr>  
        </thead> 
						<tbody>
						<%
						Hashtable ht = new Hashtable();
						ArrayList locQryList =    new LocUtil().getAllLocDetails(PLANT,"","");
						if (locQryList.size() > 0) {
						for(int i =0; i<locQryList.size(); i++) {
							Map arrCustLine = (Map) locQryList.get(i);
							Loc = (String)arrCustLine.get("LOC");
						%>
    <tr>
      <td style="text-align: left;"><INPUT Type=checkbox style="border: 0;" name=chkdLoc id=chkdLoc value="<%=Loc%>" ></td>
      <td><%=Loc%></td>
						<%
						}
						}
						%>
    </tr>
  </tbody>
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
							</tr>
						</tfoot>
</table>
  		</div>				
  		</div>
  		</div>
  		

	
  		<div class="row" style="padding:3px">
  		<div class="col-sm-2">
  	</div>
			<div class="col-sm-8 txn-buttons" style="text-align: center;">
				<button type="button" class="btn btn-success" onClick="javascript:return onGo();">Search</button>&nbsp;
			</div>
		</div>
  		</div>
        
			
  <div id="VIEW_RESULT_HERE" class="table-responsive">
  
              <%
          	if (chkdLoc!=null){
              for (int i = 0; i < chkdLoc.length; i++) { 
              loooc = chkdLoc[i];
              %>
           <table class="table table-bordred table-striped" >
					<thead>
		                <tr role="row">
       	  				<th><%=ALLOWPRDNAME%></th>
		                <th><%=loooc%></th>
		                <th>Total</th>
                     </tr>
		            </thead>
		            <br>
		            <tbody>
		            <%
              try{
              Double totqty=0.000;
              invQryList = new InvUtil().getInvListValuationSummary(PLANT,PRD_DEPT_ID,PRD_CLS_ID,PRD_BRAND_ID,loooc,ALLOWPRDNAME);
              for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
            	  Map lineArr = (Map) invQryList.get(iCnt);
            	  String name    = (String)lineArr.get("NAME");
                  String totalqty      =  (String)lineArr.get("TOTALQTY");
                  totqty=totqty+Double.valueOf(totalqty);
		            %>
		              <TR>
		<td align="left" class="main2">&nbsp;<%=name%></td>
		<td align="left" class="main2">&nbsp;<%=totalqty%></td>
		<td align="left" class="main2">&nbsp;<%=totalqty%></td>
	</TR>
		<%
              }
		%>
		<TR>
		<td align="left" class="main2" style="font-weight: bold;">&nbsp;TOTAL</td>
		<td align="left" class="main2" style="font-weight: bold;">&nbsp;<%=StrUtils.addZeroes(totqty,"3")%></td>
		<td align="left" class="main2" style="font-weight: bold;">&nbsp;<%=StrUtils.addZeroes(totqty,"3")%></td>
	</TR>
		<%
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
  
		            </tbody>
				</table>
				<%
              }}
              %>
	<div id="tablePerformanceSummary_wrapper" class="dataTables_wrapper form-inline dt-bootstrap no-footer" style="display:none;">
              <%
          	if (chkdLoc!=null){
              for (int i = 0; i < chkdLoc.length; i++) { 
              loooc = chkdLoc[i];
              %>
       <div class="row"><div class="col-sm-12">   
       <div class="page">       
       <div class="subpage">       
      <header class="clearfix">
      <div id="company" style="text-align:center">
        <h2 style="margin:0"><%=CNAME%></h2>
        <div><%=fromAddress_RoadAddress.trim()%></div>
        <div><%=fromAddress_Country.trim()%></div>
        <div>Phone <%=TELNO.trim()%></div>
      </div>
    </header>
              	<table id="tablePerformanceSummary" WIDTH="80%" border="0" cellspacing="1" cellpadding=2 align="center"	class="table printtable">
					<thead>
		                <tr role="row">
       	  				<th style="font-size: smaller;"><%=ALLOWPRDNAME%></th>
		                <th style="font-size: smaller;"><%=loooc%></th>
		                <th style="font-size: smaller;">Total</th>
                     </tr>
		            </thead>
		            <br>
		            <tbody>
		            <%
              try{
              Double totqty=0.000;
              invQryList = new InvUtil().getInvListValuationSummary(PLANT,PRD_DEPT_ID,PRD_CLS_ID,PRD_BRAND_ID,loooc,ALLOWPRDNAME);
              for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
            	  Map lineArr = (Map) invQryList.get(iCnt);
            	  String name    = (String)lineArr.get("NAME");
                  String totalqty      =  (String)lineArr.get("TOTALQTY");
                  totqty=totqty+Double.valueOf(totalqty);
		            %>
		              <TR>
		<td align="left" class="main2">&nbsp;<%=name%></td>
		<td align="left" class="main2">&nbsp;<%=totalqty%></td>
		<td align="left" class="main2">&nbsp;<%=totalqty%></td>
	</TR>
		<%
              }
		%>
		<TR>
		<td align="left" class="main2">&nbsp;TOTAL</td>
		<td align="left" class="main2">&nbsp;<%=StrUtils.addZeroes(totqty,"3")%></td>
		<td align="left" class="main2">&nbsp;<%=StrUtils.addZeroes(totqty,"3")%></td>
	</TR>
		<%
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
  
		            </tbody>
				</table>
				</div>
				</div>
				</div>
					</div>
				<%} }%>
            		</div>
						
  </div>
  </FORM>
  </div></div></div>
 <script>
 $(document).ready(function(){

    var plant= '<%=PLANT%>'; 
    
	/* Product Number Auto Suggestion */
	$('#PRD_CLS_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_CLS_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTCLASS_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_CLASS_MST);
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
				return '<div onclick="document.form1.PRD_CLS_ID.value = \''+data.PRD_CLS_ID+'\'"><p class="item-suggestion">' + data.PRD_CLS_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_CLS_DESC+'</p></div>';
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
		});

	/* Product Dept Auto Suggestion */
	$('#PRD_DEPT_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'prd_dep_id',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTDEPARTMENTID_FOR_SUMMARY",
				PRODUCTDEPARTMENTID : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CUSTOMERTYPELIST);
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
				return '<div onclick="document.form1.PRD_DEPT_ID.value = \''+data.prd_dep_id+'\'"><p class="item-suggestion">' + data.prd_dep_id + '</p><br/><p class="item-suggestion">DESC: '+data.prd_dep_desc+'</p></div>';
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
	});
	
	/* Product Type Number Auto Suggestion */
	$('#PRD_TYPE_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_TYPE_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTTYPE_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_TYPE_MST);
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
// 			return '<div><p class="item-suggestion">'+data.PRD_TYPE_ID+'</p></div>';
				return '<div onclick="document.form1.PRD_TYPE_ID.value = \''+data.PRD_TYPE_ID+'\'"><p class="item-suggestion">' + data.PRD_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_TYPE_DESC+'</p></div>';
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
		});
	/* Product Type Number Auto Suggestion */
	$('#PRD_BRAND_ID').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PRD_BRAND_ID',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_PRODUCTBRAND_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PRD_BRAND_MST);
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
// 			return '<div><p class="item-suggestion">'+data.PRD_BRAND_ID+'</p></div>';
				return '<div onclick="document.form1.PRD_BRAND_ID.value = \''+data.PRD_BRAND_ID+'\'"><p class="item-suggestion">' + data.PRD_BRAND_ID + '</p><br/><p class="item-suggestion">DESC: '+data.PRD_BRAND_DESC+'</p></div>';
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
		});
	/* location Auto Suggestion */
	$('#LOC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : plant,
				ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOC_MST);
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
			return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
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
		});
	
 });

 function changeitem(obj){
	 $("#ITEM").typeahead('val', '"');
	 $("#ITEM").typeahead('val', '');
	 $("#ITEM").focus();
	}
 function changeCategory(obj){
	 $("#PRD_CLS_ID").typeahead('val', '"');
	 $("#PRD_CLS_ID").typeahead('val', '');
	 $("#PRD_CLS_ID").focus();
	}
 function changeLocation(obj){
	 $("#LOC").typeahead('val', '"');
	 $("#LOC").typeahead('val', '');
	 $("#LOC").focus();
	}

	function checkAll(isChk)
 	{
 		var len = document.form1.chkdLoc.length;
 		 var orderLNo; 
 		 if(len == undefined) len = 1;  
 	    if (document.form1.chkdLoc)
 	    {
 	        for (var i = 0; i < len ; i++)
 	        {      
 	              	if(len == 1){
 	              		document.form1.chkdLoc.checked = isChk;
 	               	}
 	              	else{
 	              		document.form1.chkdLoc[i].checked = isChk;
 	              	}
 	            	
 	        }
 	    }
 	}
 </script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>