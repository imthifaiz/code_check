<%@page import="com.track.constants.IConstants"%>
<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<!-- navas -->
<%@page import="com.track.dao.PlantMstDAO"%>
<!-- END -->
<%
String title = "Currency Summary";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>

<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>

<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'CurrencySummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
  }

 function onView(){
	   document.form.action  = "../currency/summary?action=VIEW";
	   document.form.submit();
	}
</script>

<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
userBean _userBean      = new userBean();
List currencyList  = new ArrayList();

_userBean.setmLogger(mLogger);

String fieldDesc="";
String PLANT="",CURRENCYID ="",DESCRIPTION="",DISPLAY="",CURRUSEQT="",isActive="";



boolean flag=false;
session=request.getSession();
// String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
String USERID= session.getAttribute("LOGIN_USER").toString();
String systatus = session.getAttribute("SYSTEMNOW").toString();

//Start code added by Deen for base Currency inclusion on Aug 15 2012 
String baseCurrency = (String) session.getAttribute("BASE_CURRENCY");
//End code added by Deen for base Currency inclusion on Aug 15 2012 
//CURRENCYID     = strUtils.fString(request.getParameter("CURRENCY_ID"));
DESCRIPTION   = strUtils.fString(request.getParameter("DESCRIPTION"));
DISPLAY   = strUtils.fString(request.getParameter("DISPLAY"));
CURRUSEQT   = strUtils.fString(request.getParameter("DESCRIPTION"));
// fieldDesc=StrUtils.fString(request.getParameter("result"));
String msg = (String)request.getAttribute("Msg");
fieldDesc = (String)request.getAttribute("result");
String PGaction = (String)request.getAttribute("PGaction");
//navas
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
	//END
	boolean displaySummaryLink=false,displaySummaryEdit=false,displaySummaryNew=false;
if(systatus.equalsIgnoreCase("ACCOUNTING"))
{
displaySummaryLink = ub.isCheckValAcc("summarylnkcurs", PLANT,USERID);
displaySummaryEdit = ub.isCheckValAcc("editcurs", PLANT,USERID);
displaySummaryNew = ub.isCheckValAcc("newcurs", PLANT,USERID);
}
if(systatus.equalsIgnoreCase("INVENTORY"))
{
displaySummaryLink = ub.isCheckValinv("summarylnkcurs", PLANT,USERID);
displaySummaryEdit = ub.isCheckValinv("editcurs", PLANT,USERID);
displaySummaryNew = ub.isCheckValinv("newcurs", PLANT,USERID);
}

String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
CurrencyUtil curUtil = new CurrencyUtil();

curUtil.setmLogger(mLogger);

if(PGaction.equalsIgnoreCase("View")){
 try{
     
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)  ht.put("PLANT",PLANT);
            if(CURRENCYID.equalsIgnoreCase("")||CURRENCYID==null)	CURRENCYID = strUtils.fString(request.getParameter("CURRENCY_ID1"));
                        
              currencyList=      curUtil.getCurrencyDetails(CURRENCYID,PLANT);
      
      if(currencyList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }
                 
     
 }catch(Exception e) { }
}
%>
<center>
	<h2><small class="success-msg"><%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Currency Summary</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <div class="box-title pull-right">
               <%if(displaySummaryNew){ %>
              <button type="button" class="btn btn-default" onclick="window.location.href='../currency/new'"  style="background-color: rgb(214, 72, 48);color: rgb(255, 255, 255);" >+ New</button>&nbsp;
              <%} %>
              </div>
		</div>
		
 <div class="box-body">
 
<div class="mainred">
	  	
	</div>
	
<FORM class="form-horizontal" name="form" method="post" action="../currency/summary">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="DESC"></input>
<input type="hidden" name="DISPLAY"></input>
<!-- navas -->
<input type="hidden" name="plant" value="<%=PLANT%>">
<!-- end -->
 

<div class="form-group">
<label class="control-label col-sm-4" for="Currency ID">Currency ID</label>
<div class="col-sm-4" >
<div class="input-group">
<INPUT name="CURRENCY_ID1" id="CURRENCY_ID" type = "TEXT" value="<%=CURRENCYID%>" size="20"  MAXLENGTH=100 class="form-control">
<span class="select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY_ID\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- <span class="btn-danger input-group-addon" onClick="javascript:popUpWin('../jsp/list/currencyList.jsp?CURRENCY_ID='+form.CURRENCY_ID.value);">
  <span class="glyphicon glyphicon-search" aria-hidden="true"></span></a></span> -->
  	</div>	&nbsp;&nbsp;
  	</div>
  <button type="button" class="btn btn-success" value="View" onClick="onView();">Search</button>
<!--   <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'">Back</button>&nbsp;&nbsp; -->
   <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
		</div>
		<INPUT name="ACTIVE" type = "hidden" value="Y"   >
            <INPUT name="ACTIVE" type = "hidden" value="N" >
	</form>
	<br>
 
 
<div style="overflow-x:auto;">
<!-- <table id="table" class="table table-bordered table-hover dataTable no-footer " -->
<!--    role="grid" aria-describedby="tableInventorySummary_info" >  -->

      <div class="searchType-filter">
             <!-- <label for="searchType">Select Search Type:</label> -->
		                      <select id="searchType" class="form-control" style="height: 30px;">
		                        <option value="all">All</option>
		                        <option value="first">First</option>
		                           <option value="center">Middle</option>
		                           <option value="last">Last</option>
		                       </select>
		                  </div>
   <table id="table" class="table table-bordred table-striped" > 
   
<!--    <thead style="background: #eaeafa;text-align: center">  -->
    <thead style="text-align: center">   
          <tr> 
           <th style="font-size: smaller;">S/N</th>  
           <th style="font-size: smaller;">Currency ID</th>  
           <th style="font-size: smaller;">Description</th>
           <th style="font-size: smaller;">Display</th>  
           <th style="font-size: smaller;">Currency/<%=baseCurrency%> Equivalent</th>  
           <th style="font-size: smaller;">IsActive</th>  
           <th style="font-size: smaller;">Edit</th>   
           <%--  <th >S/N</th>  
            <th>Currency ID</th> 
            <th>Description</th>
            <th>Display</th>
            <th>Currency/<%=baseCurrency%> Equivalent</th>
            <th>IsActive</th>
            <th>Edit</th>     --%>
          </tr>  
        </thead> 
         <!-- navas -->
        <tfoot align="right" style="display: none;">
							<tr>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
								<th></th>
							
							</tr>
						</tfoot>
						<!-- END -->
        
        <%-- <tbody>
 
   	  <%
         
         
          for (int iCnt =0; iCnt<currencyList.size(); iCnt++){
			     int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
           
          
           ArrayList lineArr = (ArrayList)currencyList.get(iCnt);
       %>

          <TR>
            <TD align="center"><%=iIndex%></TD>
            
           <TD align="center"> <a href ="currencyDetail.jsp?action=View&CURRENCY_ID=<%=(String)lineArr.get(0)%>&DESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get(1))%>&ISACTIVE=<%=(String)lineArr.get(6)%>&DISPLAY=<%=(String)lineArr.get(2)%>&CURUSEQT=<%=(String)lineArr.get(3)%>&REMARK=<%=(String)lineArr.get(4)%>&STATUS=<%=(String)lineArr.get(5)%>"><%=(String)lineArr.get(0)%></a></TD>
    	     <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get(1))%></TD>
              <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get(2))%></TD>
              <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.addZeroes(Double.parseDouble(strUtils.fString((String)lineArr.get(3))), numberOfDecimal)%></TD>
              <TD align="center">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get(6))%></TD>
              <TD align="center"> <a href ="maintCurrency.jsp?action=View&CURRENCY_ID=<%=(String)lineArr.get(0)%>&DESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get(1))%>&ISACTIVE=<%=(String)lineArr.get(6)%>&DISPLAY=<%=(String)lineArr.get(2)%>&CURUSEQT=<%=(String)lineArr.get(3)%>&REMARK=<%=(String)lineArr.get(4)%>&STATUS=<%=(String)lineArr.get(5)%>"><i class="fa fa-pencil-square-o"></i></a></TD>
          </TR>
       <%}%>
      </tbody>  --%> 
</table>
</div>
<script LANGUAGE="JavaScript">
		var plant = document.form.plant.value;
		var tabletype;
		var CURRENCY, groupRowColSpan = 6;
		
		function getParameters(){
			return {
				"CURRENCY":CURRENCY,
				"PLANT":plant,
				"action":"GET_CURRENCYTYPE_SUMMARY"
			}
		}
		function onGo(){
			CURRENCY=document.form.CURRENCY_ID1.value;
			   var urlStr = "/track/MasterServlet";
			   var groupColumn = 1;	
			    if (tabletype){
			    	tabletype.ajax.url( urlStr ).load();
			    }else{
			    	tabletype = $('#table').DataTable({
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
					        	
						        	if(typeof data.CUSTOMERTYPELIST[0].CURRENCYID === 'undefined'){
						        		return [];
						        	}else {				        		
						        			for(var dataIndex = 0; dataIndex < data.CUSTOMERTYPELIST.length; dataIndex ++){
						        				var lineno = data.CUSTOMERTYPELIST[dataIndex].CURRENCYID;
						        				var sno=dataIndex+1;
						        				data.CUSTOMERTYPELIST[dataIndex]['SNO'] = sno;
						        				<%if(displaySummaryEdit){ %>
						        				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a  href ="../currency/edit?action=View&CURRENCY_ID='+lineno+'&DESC=' +data.CUSTOMERTYPELIST[dataIndex].DESCRIPTION+'&ACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].ISACTIVE+'"><i class="fa fa-pencil-square-o"></i></a>';
						        				<% } else { %>
						        				data.CUSTOMERTYPELIST[dataIndex]['EDIT'] = '<a href="#"><i class="fa fa-pencil-square-o"></i></a>';
						        				<%  } %>
						        				<%if(displaySummaryLink){ %>
						        				data.CUSTOMERTYPELIST[dataIndex]['CURRENCYID'] = '<a  href ="../currency/detail?action=View&CURRENCY_ID='+lineno+'&DESC=' +data.CUSTOMERTYPELIST[dataIndex].DESCRIPTION+'&ACTIVE=' +data.CUSTOMERTYPELIST[dataIndex].ISACTIVE+'&DISPLAY=' +data.CUSTOMERTYPELIST[dataIndex].DISPLAY+'&CURUSEQT=' +data.CUSTOMERTYPELIST[dataIndex].CURREQT+'&REMARK=' +data.CUSTOMERTYPELIST[dataIndex].REMARK+'">'+data.CUSTOMERTYPELIST[dataIndex].CURRENCYID+'</a>';
<%-- 						        				<a href ="currencyDetail.jsp?action=View&CURRENCY_ID=<%=(String)lineArr.get(0)%>&DESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get(1))%>&ISACTIVE=<%=(String)lineArr.get(6)%>&DISPLAY=<%=(String)lineArr.get(2)%>&CURUSEQT=<%=(String)lineArr.get(3)%>&REMARK=<%=(String)lineArr.get(4)%>&STATUS=<%=(String)lineArr.get(5)%>"><%=(String)lineArr.get(0)%></a></TD> --%>
						        				<%  } %>
							        			}
							        			
						        		return data.CUSTOMERTYPELIST;
						        	}
					        	
					        }
					    },
					    
				        "columns": [
				        	{"data": 'SNO', "orderable": true},
				        	{"data": 'CURRENCYID', "orderable": true},
				        	{"data": 'DESCRIPTION', "orderable": true},
				        	{"data": 'DISPLAY', "orderable": true},
				        	{"data": 'CURREQT', "orderable": true},	
				        	{"data": 'ISACTIVE', "orderable": true},					        	
			    			{"data": 'EDIT', "orderable": true},
			    			],
						"columnDefs": [{"className": "text-center", "targets": [4]}],
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
				                    		columns: [0,1,2,3,4,5]
				    	                },
				    	                title: '<%=title%>',
				    	                footer: true
				                    },
				                    {
				                    	extend : 'pdf',
			                            footer: true,
				                    	text: 'PDF Portrait',
				                    	exportOptions: {
				                    		columns: [0,1,2,3,4,5]
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
				                    		columns: [0,1,2,3,4,5]
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

			    	$("#table_filter.dataTables_filter").append($("#searchType")); $.fn.dataTable.ext.search.push(function (settings, data, dataIndex) {
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
			        	table.draw();
			        });
			    }
			    
			}

		</script>  
	  </div>
	  </div>
	  </div>
	  
	  
	  
 <script>
$(document).ready(function(){
// 	$('#table').dataTable({
// 		"lengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100, "All"]]
// 	});
	onGo();
    
    $('[data-toggle="tooltip"]').tooltip();
});
</script>

<script>
$(document).ready(function(){

$('[data-toggle="tooltip"]').tooltip();
var plant= '<%=PLANT%>';


// UserId Auto Suggestion //
$('#CURRENCY_ID').typeahead({
hint: true,
minLength:0,
searchOnFocus: true
},
{
display: 'CURRENCYID',
async: true,
source: function (query, process,asyncProcess) {
var urlStr = "/track/MasterServlet";
$.ajax( {
type : "POST",
url : urlStr,
async : true,
data : {
PLANT : plant,
ACTION : "GET_CURRENCYTYPE_SUMMARY",
CURRENCY : query
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
return '<p class="item-suggestion">'+ data.CURRENCYID +'</p>';
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
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>